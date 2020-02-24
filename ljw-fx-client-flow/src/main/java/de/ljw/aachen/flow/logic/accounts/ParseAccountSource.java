package de.ljw.aachen.flow.logic.accounts;

import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.data.Account;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVParser;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ParseAccountSource {

    private final FileSystem fs;

    @Setter
    private Consumer<List<Account>> onSourcedAccounts;

    @SneakyThrows
    public void process(Path source) {
        try (var parser = new CSVParser(fs.newReader(source), AccountConverter.getFormat())) {
            var sourcedAccounts = parser.getRecords().stream()
                    .map(AccountConverter::fromRecord)
                    .collect(Collectors.toList());
            onSourcedAccounts.accept(sourcedAccounts);
        }
    }

}
