package de.ljw.aachen.lagerbank.adapter.out.csv;

import de.ljw.aachen.lagerbank.domain.Transaction;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVPrinter;

import java.io.Writer;

class WriteTransactions {

    @SneakyThrows
    static void write(Writer sink, Transaction transaction) {
        try (var writer = new CSVPrinter(sink, TransactionConverter.getFormat())) {
            writer.printRecord(TransactionConverter.convertToValues(transaction));
        }
    }
}
