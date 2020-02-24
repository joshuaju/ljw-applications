package de.ljw.aachen.lagerbank.adapter.out.csv;

import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.lagerbank.adapter.out.TransactionStoreMem;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.port.out.TransactionStorePort;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

public class TransactionStoreCSV implements TransactionStorePort {

    private final Path out;
    private final TransactionStoreMem memoryStore;

    @SneakyThrows
    public TransactionStoreCSV(Path out, TransactionStoreMem memoryStore) {
        this.out = out;
        this.memoryStore = memoryStore;
        if (!Files.exists(out)) {
            Files.createFile(out);
        }
    }

    @Override
    @SneakyThrows
    public void add(Transaction transaction) {
        memoryStore.add(transaction);
        WriteTransactions.write(Files.newBufferedWriter(out, StandardOpenOption.APPEND), transaction);
    }

    @Override
    public Collection<Transaction> getAll() {
        return memoryStore.getAll();
    }

    @Override
    public Collection<Transaction> getAll(AccountId accountId) {
        return memoryStore.getAll(accountId);
    }
}
