package de.ljw.aachen.account.management.adapter.out;

import de.ljw.aachen.account.management.domain.Account;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVParser;

import java.io.Reader;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ReadAccounts {

    @SneakyThrows
    static Collection<Account> read(Reader data) {
        try (var parser = CSVParser.parse(data, AccountConverter.getFormat())) {
            return StreamSupport.stream(parser.spliterator(), false)
                    .map(AccountConverter::convertToAccount)
                    .collect(Collectors.toList());
        }
    }
}
