package de.ljw.aachen.lagerbank.adapter.out;

import de.ljw.aachen.lagerbank.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.port.out.TransactionStorePort;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TransactionCSVStore implements TransactionStorePort {

    private final Path out;
    private final TransactionStoreMem memoryStore;

    @SneakyThrows
    public TransactionCSVStore(Path out) {
        this.out = out;
        if (!Files.exists(out)) {
            Files.createFile(out);
        }

        var transactions = readAll(this.out);

        this.memoryStore = new TransactionStoreMem(transactions);
    }

    @SneakyThrows
    private List<Transaction> readAll(Path out) {
        return Files.readAllLines(out).stream()
                .map(TransactionCSVConverter::convert)
                .collect(Collectors.toList());
    }


    @Override
    @SneakyThrows
    public void add(Transaction transaction) {
        String transactionString = TransactionCSVConverter.convert(transaction) + "\n";
        Files.write(out, transactionString.getBytes(), StandardOpenOption.APPEND);
        memoryStore.add(transaction);
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
