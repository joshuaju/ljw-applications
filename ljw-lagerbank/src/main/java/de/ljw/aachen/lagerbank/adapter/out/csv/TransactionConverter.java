package de.ljw.aachen.lagerbank.adapter.out.csv;

import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.domain.TransactionId;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;

class TransactionConverter {


    static CSVFormat getFormat() {
        return CSVFormat.DEFAULT
                .withHeader("id", "source", "target", "amount", "time");
    }

    static List<String> convertToValues(Transaction transaction) {
        return List.of(
                transaction.getId().getValue(),
                AccountIdConverter.convert(transaction.getSource()),
                AccountIdConverter.convert(transaction.getTarget()),
                MessageFormat.format("{0,number,#.##}", transaction.getAmount().getAmount()),
                transaction.getTime().toString()
        );
    }

    static Transaction convertToTransaction(CSVRecord record) {

        var transactionId = new TransactionId(record.get("id"));
        var sourceId = AccountIdConverter.convert(record.get("source"));
        var targetId = AccountIdConverter.convert(record.get("target"));
        var amount = Money.of(Double.parseDouble(record.get("amount")));
        var time = Instant.parse(record.get("time"));

        return new Transaction(transactionId, sourceId, targetId, amount, time);
    }


}
