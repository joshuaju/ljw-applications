package de.ljw.aachen.flow.logic;

import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.data.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVParser;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ParseTransactionSource {

    private final FileSystem fs;

    @Setter
    private Consumer<List<Transaction>> onSourcedTransactions;

    @SneakyThrows
    public void process(Path source) {
        try (var parser = new CSVParser(fs.newReader(source), TransactionConverter.getFormat())) {
            var sourcedTransactions = parser.getRecords().stream()
                    .map(TransactionConverter::fromRecord)
                    .collect(Collectors.toList());
            onSourcedTransactions.accept(sourcedTransactions);
        }
    }

}
