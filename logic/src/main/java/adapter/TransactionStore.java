package adapter;

import data.Transaction;

import java.nio.file.Path;
import java.util.List;

public interface TransactionStore {

    List<Transaction> getTransactions();

    void store(Transaction transaction);

    Path getSource();

    void setSource(Path path);
}
