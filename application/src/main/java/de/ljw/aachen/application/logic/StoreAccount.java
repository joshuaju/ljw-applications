package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.data.Account;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.function.Predicate;

@RequiredArgsConstructor
class StoreAccount {

    private final FileSystem fs;
    private final AccountStore accountStore;

    @SneakyThrows
    public void process(Account account) {
        assert account.getId() != null;
        var values = AccountConverter.toString(account);
        var destination = accountStore.getSource();
        fs.writeLine(destination, values);
        modifyOrStore(account);
    }

    private void modifyOrStore(Account account) {
        Predicate<Account> accountWithSameId = reference -> reference.getId().equals(account.getId());
        accountStore.getAccounts().stream()
                .filter(accountWithSameId)
                .findAny()
                .ifPresentOrElse(
                        reference -> modifyReference(reference, account),
                        () -> storeNewAccount(account)
                );
    }

    private void modifyReference(Account reference, Account edited) {
        reference.setFirstName(edited.getFirstName());
        reference.setLastName(edited.getLastName());
    }


    private void storeNewAccount(Account account) {
        accountStore.store(account);
    }
}
