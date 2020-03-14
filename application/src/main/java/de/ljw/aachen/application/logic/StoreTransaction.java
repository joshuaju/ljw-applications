package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.application.data.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
class StoreTransaction {

    private final FileSystem fs;
    private final TransactionStore transactionStore;

    @SneakyThrows
    public void process(Transaction transaction) {
        assert transaction.getId() != null;
        var values = TransactionConverter.toString(transaction);
        var destination = transactionStore.getSource();
        fs.writeLine(destination, values);
        transactionStore.store(transaction);
    }
}
