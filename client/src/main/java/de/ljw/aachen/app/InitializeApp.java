package de.ljw.aachen.app;

import adapter.AccountStore;
import adapter.FileSystem;
import adapter.TransactionStore;
import logic.ParseAccountSource;
import logic.ReplayAccounts;
import logic.ParseTransactionSource;
import logic.ReplayTransactions;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class InitializeApp implements CommandLineRunner {

    private final FileSystem fs;
    private final AccountStore accountStore;
    private final TransactionStore transactionStore;

    @Override
    public void run(String... args) throws Exception {
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
    }
}
