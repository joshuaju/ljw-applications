package de.ljw.aachen.application.adapter;

import de.ljw.aachen.application.data.Transaction;

import java.nio.file.Path;
import java.util.List;

public interface TransactionStore {

    List<Transaction> getTransactions();

    void store(Transaction transaction);

    Path getSource();

    void setSource(Path path);
}
