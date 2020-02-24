package de.ljw.aachen.flow.logic.accounts;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.data.Account;
import de.ljw.aachen.flow.data.AccountId;
import de.ljw.aachen.flow.logic.transactions.TransactionConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVPrinter;

@RequiredArgsConstructor
public class StoreAccount {

    private final FileSystem fs;
    private final AccountStore accountStore;

    @SneakyThrows
    public void process(Account account) {
        assert account.getId() != null;

        var values = AccountConverter.toValues(account);
        try (var printer = new CSVPrinter(
                fs.newWriter(accountStore.getSource()),
                AccountConverter.getFormat())) {
            printer.printRecord(values);
            accountStore.store(account);
        }
    }
}
