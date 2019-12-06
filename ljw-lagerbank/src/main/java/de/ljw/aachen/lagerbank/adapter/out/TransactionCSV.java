package de.ljw.aachen.lagerbank.adapter.out;

import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.domain.TransactionId;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class TransactionCSV {

    private static final String NO_ACCOUNT = "";

    static String serialize(Transaction transaction) {
        return MessageFormat.format("{0}, {1}, {2}, {3,number,#.##}, {4}",
                transaction.getId().getValue(),
                (transaction.getSource() == null) ? NO_ACCOUNT : transaction.getSource().getId(),
                (transaction.getTarget() == null) ? NO_ACCOUNT : transaction.getTarget().getId(),
                transaction.getAmount().getAmount(),
                transaction.getTime()
        );
    }

    static Transaction deserialize(String transactionString) {
        List<String> fields = Arrays.stream(transactionString.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        var transactionId = new TransactionId(fields.get(0));
        var sourceId = parseAccountId(fields.get(1));
        var targetId = parseAccountId(fields.get(2));
        var amount = Money.of(Double.parseDouble(fields.get(3)));
        var time = Instant.parse(fields.get(4));

        return new Transaction(transactionId, sourceId, targetId, amount, time);
    }

    private static AccountId parseAccountId(String accountId) {
        if (NO_ACCOUNT.equals(accountId)) return null;

        long value = Long.parseLong(accountId);
        return new AccountId(value);
    }

}
