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
                serializeAccountId(transaction.getSource()),
                serializeAccountId(transaction.getTarget()),
                transaction.getAmount().getAmount(),
                transaction.getTime()
        );
    }

    static Transaction deserialize(String transactionString) {
        List<String> fields = Arrays.stream(transactionString.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        var transactionId = new TransactionId(fields.get(0));
        var sourceId = deserializeAccountId(fields.get(1));
        var targetId = deserializeAccountId(fields.get(2));
        var amount = Money.of(Double.parseDouble(fields.get(3)));
        var time = Instant.parse(fields.get(4));

        return new Transaction(transactionId, sourceId, targetId, amount, time);
    }

    private static String serializeAccountId(AccountId id){
        return id == null ? NO_ACCOUNT : id.getValue();
    }

    private static AccountId deserializeAccountId(String value) {
        if (NO_ACCOUNT.equals(value)) return null;

        return new AccountId(value);
    }

}
