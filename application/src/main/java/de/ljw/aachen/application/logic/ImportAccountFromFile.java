package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.data.Money;
import de.ljw.aachen.application.data.Transaction;
import de.ljw.aachen.application.exceptions.ColumnMismatchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.Validate;

import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class ImportAccountFromFile {

    private static final List<String> EXPECTED_HEADER_VALUES = Arrays.asList("first name", "last name", "balance");
    private static final int EXPECTED_COLUMN_COUNT = EXPECTED_HEADER_VALUES.size();

    private final FileSystem fs;
    private final CreateAccount createAccount;
    private final ExecuteTransaction executeTransaction;
    private final Consumer<String> logger;

    public ImportAccountFromFile(FileSystem fs, AccountStore accountStore, TransactionStore transactionStore, Consumer<String> logger) {
        this.fs = fs;
        this.createAccount = new CreateAccount(fs, accountStore);
        this.executeTransaction = new ExecuteTransaction(fs, transactionStore);
        this.logger = logger;
    }

    public void importFile(Path importFile, String description) {
        var lines = fs.readLines(importFile);
        if (lines.isEmpty()) return;

        validateHeader(lines.get(0));
        var accountBalanceMap = parseContent(lines.stream().skip(1).collect(Collectors.toList()));
        accountBalanceMap.forEach((account, balance) -> importAccount(account, balance, description));
    }

    private void validateHeader(String headerLine) {
        var actualHeaderValues = splitLine(headerLine);

        if (!EXPECTED_HEADER_VALUES.equals(actualHeaderValues)) {
            var errorMessage = MessageFormat.format("CSV file header is expected to be {0}, but was {1}",
                    EXPECTED_HEADER_VALUES, actualHeaderValues);
            log.info(errorMessage);
            throw new ColumnMismatchException();
        }

    }

    private Map<Account, Money> parseContent(List<String> contentLines) {
        Map<Account, Money> accountBalanceMap = new HashMap<>();
        for (var line : contentLines) {
            var values = splitLine(line);

            var account = parseAccount(values);
            var balance = parseBalance(values);

            accountBalanceMap.put(account, balance);
        }
        return accountBalanceMap;
    }

    private static Account parseAccount(List<String> values) {
        var firstName = values.get(0);
        var lastName = values.get(1);
        return new Account(null, firstName, lastName);
    }

    private static Money parseBalance(List<String> values) {
        var amount = values.get(2);
        var amountAsDouble = Double.parseDouble(amount);
        return new Money(amountAsDouble);
    }

    private void importAccount(Account account, Money balance, String description) {
        try {
            var id = createAccount.process(account);
            var deposit = Transaction.deposit(id, balance);
            deposit.setDescription(description);
            executeTransaction.process(deposit, false);
            logger.accept(MessageFormat.format(" OK: {0}, {1}",
                    ComposeFullName.process(account), balance.formatWithCurrency()));
        } catch (Exception e) {
            logger.accept(MessageFormat.format("ERR: {0}, {1}\n>>> {2}",
                    ComposeFullName.process(account), balance.formatWithCurrency(), e.getClass().getSimpleName()));
        }
    }

    private static List<String> splitLine(String line) {
        var values = line.split(",");

        if (values.length != EXPECTED_COLUMN_COUNT) {
            var errorMsg = MessageFormat.format("Column count does not match. Expected {0} but found {1} columns in line \"{2}\"", EXPECTED_COLUMN_COUNT, values.length, line);
            log.error(errorMsg);
            throw new ColumnMismatchException();
        }

        return Arrays.stream(values)
                .map(String::trim)
                .collect(Collectors.toList());

    }
}
