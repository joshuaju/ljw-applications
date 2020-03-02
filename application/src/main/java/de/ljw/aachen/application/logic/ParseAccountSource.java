package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.data.Account;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVParser;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class ParseAccountSource {

    private final FileSystem fs;

    @SneakyThrows
    public List<Account> process(Path source) {
        try (var parser = new CSVParser(fs.newReader(source), AccountConverter.getFormat())) {
            return parser.getRecords().stream()
                    .map(AccountConverter::fromRecord)
                    .collect(Collectors.toList());
        }
    }

}
