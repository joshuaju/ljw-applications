package de.ljw.aachen.application.logic;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

@Slf4j
public class ParseArgs {

    @Setter
    private Consumer<Path> onAccountSource;
    @Setter
    private Consumer<Path> onTransactionSource;

    public void process(String[] args) {
        Path accountSourcePath = Paths.get("accounts.csv");
        Path transactionSourcePath = Paths.get("transaction.csv");

        if (args.length < 1)
            log.warn("args[0] missing: No account source specified. " +
                    "Switching to default path " + accountSourcePath.toAbsolutePath());
        else accountSourcePath = Paths.get(args[0]);
        onAccountSource.accept(accountSourcePath);

        if (args.length < 2)
            log.warn("args[1] missing: No transaction source specified. " +
                    "Switching to default path " + transactionSourcePath.toAbsolutePath());
        else transactionSourcePath = Paths.get(args[1]);
        onTransactionSource.accept(transactionSourcePath);
    }
}
