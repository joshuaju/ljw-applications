package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.data.AccountId;
import de.ljw.aachen.application.data.Money;
import de.ljw.aachen.application.data.Transaction;
import de.ljw.aachen.application.data.TransactionId;

import java.text.MessageFormat;
import java.time.Instant;

class TransactionConverter
{

    private static final String DELIMITER = ";";
    public static final String PLACEHOLDER_DEPOSIT = "DEPOSIT";
    public static final String PLACEHOLDER_WITHDRAWAL = "WITHDRAWAL";
    public static final String PLACEHOLDER_NO_DESCRIPTION = "-";

    public static String getHeader()
    {
        return String.join(DELIMITER, "id", "date", "source", "target", "amount", "description", "info");
    }

    public static String toString(Transaction transaction)
    {
        AccountId source = transaction.getSource();
        AccountId target = transaction.getTarget();

        return String.join(DELIMITER,
                transaction.getId().getValue(),
                transaction.getTime().toString(),
                source == null ? PLACEHOLDER_DEPOSIT : source.getValue(),
                target == null ? PLACEHOLDER_WITHDRAWAL : target.getValue(),
                MessageFormat.format("{0,number,#.##}", transaction.getAmount().getValue()).replace(".", ","),
                transaction.getDescription().isBlank() ? PLACEHOLDER_NO_DESCRIPTION : transaction.getDescription(),
                transaction.getInfo() == null ? "" : transaction.getInfo()
        );
    }

    public static Transaction fromString(String transactionString)
    {
        var values = transactionString.split(DELIMITER);

        var transactionId = new TransactionId(values[0]);
        var time = Instant.parse(values[1]);
        String sourceIdString = values[2];
        var sourceId = sourceIdString.equals(PLACEHOLDER_DEPOSIT) ? null : new AccountId(sourceIdString);
        String targetIdString = values[3];
        var targetId = targetIdString.equals(PLACEHOLDER_WITHDRAWAL) ? null : new AccountId(targetIdString);
        var amount = new Money(Double.parseDouble(values[4].replace(",", ".")));
        var descriptionString = values[5];
        var description = descriptionString.equals(PLACEHOLDER_NO_DESCRIPTION) ? "" : descriptionString;
        var info = (values.length >= 7) ? values[6] : "";

        return new Transaction(transactionId, sourceId, targetId, amount, time, description, info);
    }
}
