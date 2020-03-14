package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.data.Account;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class ParseAccountSource {

    private final FileSystem fs;

    @SneakyThrows
    public List<Account> process(Path source) {
        return fs.readLines(source).stream()
                .skip(1)
                .map(AccountConverter::fromString)
                .collect(Collectors.toList());
    }

}
