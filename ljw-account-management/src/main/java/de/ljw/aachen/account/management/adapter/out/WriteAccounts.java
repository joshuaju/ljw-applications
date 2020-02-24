package de.ljw.aachen.account.management.adapter.out;

import de.ljw.aachen.account.management.domain.Account;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVPrinter;

import java.io.Writer;

public class WriteAccounts {

    @SneakyThrows
    static void write(Writer sink, Account account) {
        try (var printer = new CSVPrinter(sink, AccountConverter.getFormat())) {
            printer.printRecord(AccountConverter.convertToValues(account));
        }
    }
}
