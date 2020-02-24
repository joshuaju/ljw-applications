package de.ljw.aachen.lagerbank.adapter.out.csv;

import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.domain.TransactionId;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringEscapeUtils;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;

class TransactionConverter {


    static CSVFormat getFormat() {
        return CSVFormat.DEFAULT.withTrim();
    }

    static List<String> convertToValues(Transaction transaction) {
        return List.of(
                transaction.getId().getValue(),
                transaction.getTime().toString(),
                AccountIdConverter.convert(transaction.getSource()),
                AccountIdConverter.convert(transaction.getTarget()),
                MessageFormat.format("{0,number,#.##}", transaction.getAmount().getAmount()).replace(",", ".")

        );
    }

    static Transaction convertToTransaction(CSVRecord record) {

        var transactionId = new TransactionId(record.get(0));
        var time = Instant.parse(record.get(1));
        var sourceId = AccountIdConverter.convert(record.get(2));
        var targetId = AccountIdConverter.convert(record.get(3));
        var amount = Money.of(Double.parseDouble(StringEscapeUtils.unescapeCsv(record.get(4))));


        return new Transaction(transactionId, sourceId, targetId, amount, time);
    }


}
