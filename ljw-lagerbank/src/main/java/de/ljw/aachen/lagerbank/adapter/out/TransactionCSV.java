package de.ljw.aachen.lagerbank.adapter.out;

import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.domain.TransactionId;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class TransactionCSVConverter {

    public static final String NO_ACCOUNT = "";

    /**
     * @return string representation of a transaction
     */
    static String convert(Transaction transaction) {
        return MessageFormat.format("{0}, {1}, {2}, {3,number,#.##}, {4}",
                extractTransactionId(transaction),
                extractAccountId(transaction.getSource()),
                extractAccountId(transaction.getTarget()),
                extractAmount(transaction),
                extractTime(transaction)
        );
    }

    private static Instant extractTime(Transaction transaction) {
        return transaction.getTime();
    }

    private static BigDecimal extractAmount(Transaction transaction) {
        return transaction.getAmount().getAmount();
    }

    private static Object extractAccountId(AccountId source) {
        return source != null ? source.getId() : NO_ACCOUNT;
    }

    private static String extractTransactionId(Transaction transaction) {
        return transaction.getId().getValue();
    }


    /**
     * @return transaction converted from the string representation
     */
    static Transaction convert(String transactionString) {
        List<String> fields = Arrays.stream(transactionString.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        assert fields.size() == 5;

        var transactionId = convertTransactionId(fields.get(0));
        var sourceId = convertAccountId(fields.get(1));
        var targetId = convertAccountId(fields.get(2));
        var amount = convertMoney(fields.get(3));
        var time = convertTime(fields.get(4));

        return new Transaction(transactionId, sourceId, targetId, amount, time);
    }

    private static TransactionId convertTransactionId(String transactionId) {
        return new TransactionId(transactionId);
    }

    private static AccountId convertAccountId(String accountId) {
        if (NO_ACCOUNT.equals(accountId)) return null;

        long value = Long.parseLong(accountId);
        return new AccountId(value);
    }

    private static Money convertMoney(String amount) {
        double value = Double.parseDouble(amount);
        return Money.of(value);
    }

    private static Instant convertTime(String time) {
        return Instant.parse(time);
    }
}
