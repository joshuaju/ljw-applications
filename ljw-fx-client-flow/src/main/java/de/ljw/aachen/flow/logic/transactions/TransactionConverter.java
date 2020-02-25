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

    public static CSVFormat getFormat() {
        return CSVFormat.DEFAULT.withTrim();
    }

    public static List<String> toValues(Transaction transaction) {
        AccountId source = transaction.getSource();
        AccountId target = transaction.getTarget();
        return List.of(
                transaction.getId().getValue(),
                transaction.getTime().toString(),
                source == null ? "DEPOSIT" : source.getValue(),
                target == null ? "WITHDRAWAL" : target.getValue(),
                MessageFormat.format("{0,number,#.##}", transaction.getAmount().getValue())
                        .replace(",", ".") // avoid parsing issues with ',' as decimal delimiter

        );
    }

    public static Transaction fromRecord(CSVRecord record) {
        var transactionId = new TransactionId(record.get(0));
        var time = Instant.parse(record.get(1));
        var sourceId = new AccountId(record.get(2));
        var targetId = new AccountId(record.get(3));
        var amount = new Money(Double.parseDouble(record.get(4)));

        return new Transaction(transactionId, sourceId, targetId, amount, time);
    }
}
