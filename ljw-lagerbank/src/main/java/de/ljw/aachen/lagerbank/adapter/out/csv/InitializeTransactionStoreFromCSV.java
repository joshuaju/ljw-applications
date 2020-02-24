package de.ljw.aachen.lagerbank.adapter.out.csv;

import de.ljw.aachen.lagerbank.adapter.out.TransactionStoreMem;
import de.ljw.aachen.lagerbank.domain.Transaction;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

public class InitializeTransactionStoreFromCSV {

    @SneakyThrows
    public static TransactionStoreMem init(Path source) {
        var transactions = ReadTransactions.read(Files.newBufferedReader(source));
        return new TransactionStoreMem(transactions);
    }
}
