package de.ljw.aachen.flow.app;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.adapter.TransactionStore;
import de.ljw.aachen.flow.logic.accounts.ParseAccountSource;
import de.ljw.aachen.flow.logic.accounts.ReplayAccounts;
import de.ljw.aachen.flow.logic.transactions.ParseTransactionSource;
import de.ljw.aachen.flow.logic.transactions.ReplayTransactions;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class InitializeApp {

    private final FileSystem fs;
    private final AccountStore accountStore;
    private final TransactionStore transactionStore;

    @Setter
    private Runnable onInitialized;

    public void process(String[] args) {
        var parseArgs = new ParseArgs();

        var parseAccountSource = new ParseAccountSource(fs);
        var replayAccounts = new ReplayAccounts(accountStore);

        var parseTransactionSource = new ParseTransactionSource(fs);
        var replayTransactions = new ReplayTransactions(transactionStore);

        parseArgs.setOnAccountSource(path -> {
            accountStore.setSource(path);
            parseAccountSource.process(path);
        });
        parseArgs.setOnTransactionSource(path -> {
            transactionStore.setSource(path);
            parseTransactionSource.process(path);
        });

        parseAccountSource.setOnSourcedAccounts(replayAccounts::process);
        parseTransactionSource.setOnSourcedTransactions(replayTransactions::process);

        parseArgs.process(args);
        onInitialized.run();
    }
}
