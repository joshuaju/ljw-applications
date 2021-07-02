package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@RequiredArgsConstructor
public
class InitializeAccounts {

    private final FileSystem fs;
    private final AccountStore accountStore;

    public void process(Path source) {
        accountStore.setSource(source);

        var parseAccountSource = new ParseAccountSource(fs);
        var replayAccounts = new ReplayAccounts(accountStore);

        if (fs.readLines(source).isEmpty()) fs.appendLine(source, AccountConverter.getHeader());
        var accounts = parseAccountSource.process(source);
        replayAccounts.process(accounts);
    }
}
