package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.data.AccountId;
import de.ljw.aachen.application.data.Money;
import de.ljw.aachen.application.data.Transaction;
import de.ljw.aachen.application.data.TransactionId;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringEscapeUtils;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;

class TransactionConverter {

    public static final String PLACEHOLDER_DEPOSIT = "DEPOSIT";
    public static final String PLACEHOLDER_WITHDRAWAL = "WITHDRAWAL";

    public static CSVFormat getFormat() {
        return CSVFormat.DEFAULT.withTrim();
    }

    public static List<String> toValues(Transaction transaction) {
        AccountId source = transaction.getSource();
        AccountId target = transaction.getTarget();
        return List.of(
                transaction.getId().getValue(),
                transaction.getTime().toString(),
                source == null ? PLACEHOLDER_DEPOSIT : source.getValue(),
                target == null ? PLACEHOLDER_WITHDRAWAL : target.getValue(),
                MessageFormat.format("{0,number,#.##}", transaction.getAmount().getValue())
                        .replace(",", "."), // avoid parsing issues with ',' as decimal delimiter
                transaction.getDescription()
        );
    }

    public static Transaction fromRecord(CSVRecord record) {
        var transactionId = new TransactionId(record.get(0));
        var time = Instant.parse(record.get(1));
        String sourceIdString = record.get(2);
        var sourceId = sourceIdString.equals(PLACEHOLDER_DEPOSIT) ? null : new AccountId(sourceIdString);
        String targetIdString = record.get(3);
        var targetId = targetIdString.equals(PLACEHOLDER_WITHDRAWAL) ? null : new AccountId(targetIdString);
        var amount = new Money(Double.parseDouble(record.get(4)));
        var description = record.get(5);

        return new Transaction(transactionId, sourceId, targetId, amount, time, description);
    }
}
