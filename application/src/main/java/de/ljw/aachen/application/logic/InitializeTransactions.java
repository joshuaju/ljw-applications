package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.adapter.TransactionStore;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@RequiredArgsConstructor
public class InitializeTransactions {

    private final FileSystem fs;
    private final TransactionStore transactionStore;

    public void process(Path source) {
        transactionStore.setSource(source);

        var parseTransactionSource = new ParseTransactionSource(fs);
        var replayTransactions = new ReplayTransactions(transactionStore);

        if (fs.readLines(source).isEmpty()) fs.writeLine(source, TransactionConverter.getHeader());
        var transactions = parseTransactionSource.process(source);
        replayTransactions.process(transactions);
    }
}
