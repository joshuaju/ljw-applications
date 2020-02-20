package de.ljw.aachen.lagerbank.adapter.out.csv;

import de.ljw.aachen.lagerbank.domain.Transaction;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVPrinter;

import java.io.Writer;
import java.util.Collection;

class WriteTransactions {

    @SneakyThrows
    static void write(Writer sink, Collection<Transaction> transactions) {
        try (var writer = new CSVPrinter(sink, TransactionConverter.getFormat())) {
            for (var t : transactions) {
                writer.printRecord(TransactionConverter.convertToValues(t));
            }
        }
    }
}
