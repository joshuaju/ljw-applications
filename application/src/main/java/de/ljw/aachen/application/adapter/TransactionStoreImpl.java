package de.ljw.aachen.application.adapter;

import de.ljw.aachen.application.data.Transaction;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class TransactionStoreImpl implements TransactionStore {

    private Path source;
    private List<Transaction> transactions;

    public TransactionStoreImpl(List<Transaction> transaction) {
        this.transactions = transaction;
    }

    @Override
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public void store(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public Path getSource() {
        return source;
    }

    @Override
    @SneakyThrows
    public void setSource(Path path) {
        if (Files.notExists(path)) Files.createFile(path);
        this.source = path;
    }
}
