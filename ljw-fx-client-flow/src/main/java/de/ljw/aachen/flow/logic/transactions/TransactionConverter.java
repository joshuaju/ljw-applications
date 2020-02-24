package de.ljw.aachen.flow.logic.transactions;

import de.ljw.aachen.flow.data.AccountId;
import de.ljw.aachen.flow.data.Money;
import de.ljw.aachen.flow.data.Transaction;
import de.ljw.aachen.flow.data.TransactionId;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringEscapeUtils;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;

public class TransactionConverter {

    static CSVFormat getFormat() {
        return CSVFormat.DEFAULT.withTrim();
    }

    static List<String> toValues(Transaction transaction) {
        return List.of(
                transaction.getId().getValue(),
                transaction.getTime().toString(),
                transaction.getSource().getValue(),
                transaction.getTarget().getValue(),
                MessageFormat.format("{0,number,#.##}", transaction.getAmount().getValue())
                        .replace(",", ".") // avoid parsing issues with ',' as decimal delimiter

        );
    }

    static Transaction fromRecord(CSVRecord record) {
        var transactionId = new TransactionId(record.get(0));
        var time = Instant.parse(record.get(1));
        var sourceId = new AccountId(record.get(2));
        var targetId = new AccountId(record.get(3));
        var amount = new Money(Double.parseDouble(record.get(4)));

        return new Transaction(transactionId, sourceId, targetId, amount, time);
    }
}
