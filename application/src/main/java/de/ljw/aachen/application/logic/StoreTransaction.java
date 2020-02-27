package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.application.data.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVPrinter;

@RequiredArgsConstructor
public class StoreTransaction {

    private final FileSystem fs;
    private final TransactionStore transactionStore;

    @SneakyThrows
    public void process(Transaction transaction) {
        assert transaction.getId() != null;

        var values = TransactionConverter.toValues(transaction);
        try (var printer = new CSVPrinter(
                fs.newWriter(transactionStore.getSource()),
                TransactionConverter.getFormat())) {
            printer.printRecord(values);
            transactionStore.store(transaction);
        }
    }
}
