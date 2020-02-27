package logic;

import adapter.FileSystem;
import adapter.TransactionStore;
import data.Transaction;
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
