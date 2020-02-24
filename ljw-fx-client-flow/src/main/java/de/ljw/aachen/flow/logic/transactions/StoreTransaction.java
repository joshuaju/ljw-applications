package de.ljw.aachen.flow.logic.transactions;

import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.adapter.TransactionStore;
import de.ljw.aachen.flow.data.Transaction;
import de.ljw.aachen.flow.data.TransactionId;
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
