package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.data.Account;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVPrinter;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class StoreAccount {

    private final FileSystem fs;
    private final AccountStore accountStore;

    @SneakyThrows
    public void process(Account account) {
        assert account.getId() != null;

        var values = AccountConverter.toValues(account);
        try (var printer = new CSVPrinter(fs.newWriter(accountStore.getSource()), AccountConverter.getFormat())) {
            printer.printRecord(values);
            store(account);
        }
    }

    private void store(Account account) {
        Predicate<Account> accountWithSameId = reference -> reference.getId().equals(account.getId());
        accountStore.getAccounts().stream()
                .filter(accountWithSameId)
                .findAny()
                .ifPresentOrElse(
                        this::modifyReference,
                        () -> storeNewAccount(account)
                );
    }

    private void modifyReference(Account reference) {
        reference.setFirstName(reference.getFirstName());
        reference.setLastName(reference.getLastName());
    }

    private void storeNewAccount(Account account) {
        accountStore.store(account);
    }
}
