package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.adapter.TransactionStore;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@RequiredArgsConstructor
public class InitializeAccounts {

    private final FileSystem fs;
    private final AccountStore accountStore;

    public void process(Path source) {
        accountStore.setSource(source);

        var parseAccountSource = new ParseAccountSource(fs);
        var replayAccounts = new ReplayAccounts(accountStore);

        var accounts = parseAccountSource.process(source);
        replayAccounts.process(accounts);
    }
}
