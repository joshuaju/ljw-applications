package de.ljw.aachen.flow.app;

import lombok.Setter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class ParseArgs {

    @Setter
    private Consumer<Path> onAccountSource;
    @Setter
    private Consumer<Path> onTransactionSource;

    void process(String[] args) {
        onAccountSource.accept(Paths.get(args[0]));
        onTransactionSource.accept(Paths.get(args[1]));
    }
}
