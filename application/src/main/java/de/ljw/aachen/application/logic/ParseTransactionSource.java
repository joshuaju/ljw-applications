package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.data.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class ParseTransactionSource {

    private final FileSystem fs;

    @SneakyThrows
    public List<Transaction> process(Path source) {
        return fs.readLines(source).stream()
                .skip(1)
                .map(TransactionConverter::fromString)
                .collect(Collectors.toList());
    }

}
