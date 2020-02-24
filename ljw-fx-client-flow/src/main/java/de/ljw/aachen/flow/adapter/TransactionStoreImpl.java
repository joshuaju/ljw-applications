package de.ljw.aachen.flow.adapter;

import de.ljw.aachen.flow.data.Transaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class TransactionStoreImpl implements TransactionStore {

    private Path source;
    private final List<Transaction> transactions = new LinkedList<>();

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
    public void setSource(Path path) {
        this.source = path;
    }
}
