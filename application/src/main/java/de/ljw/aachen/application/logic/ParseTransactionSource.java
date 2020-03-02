package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.data.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVParser;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class ParseTransactionSource {

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
