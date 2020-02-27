package de.ljw.aachen.client.configuration;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.application.logic.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class InitializeRunner implements CommandLineRunner {

    private final FileSystem fs;
    private final AccountStore accountStore;
    private final TransactionStore transactionStore;

    @Override
    public void run(String... args) throws Exception {
        var parseArgs = new ParseArgs();

        var initializeAccounts = new InitializeAccounts(fs, accountStore);
        var initializeTransactions = new InitializeTransactions(fs, transactionStore);

        parseArgs.setOnAccountSource(initializeAccounts::process);
        parseArgs.setOnTransactionSource(initializeTransactions::process);

        parseArgs.process(args);
    }
}
