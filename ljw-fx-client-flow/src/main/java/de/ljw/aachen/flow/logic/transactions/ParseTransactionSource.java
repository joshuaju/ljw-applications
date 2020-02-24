package de.ljw.aachen.flow.logic.transactions;

import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.data.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVParser;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ParseTransactionSource {

    private final FileSystem fs;

    @SneakyThrows
    public List<Transaction> process(Path source) {
        try (var parser = new CSVParser(fs.newReader(source), TransactionConverter.getFormat())) {
            return parser.getRecords().stream()
                    .map(TransactionConverter::fromRecord)
                    .collect(Collectors.toList());
        }
    }

}
