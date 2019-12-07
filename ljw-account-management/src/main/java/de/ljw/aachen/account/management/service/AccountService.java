package de.ljw.aachen.account.management.service;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.account.management.port.in.CreateAccountUseCase;
import de.ljw.aachen.account.management.port.in.DeleteAccountUseCase;
import de.ljw.aachen.account.management.port.in.ReadAccountUseCase;
import de.ljw.aachen.account.management.port.in.UpdateAccountUseCase;
import de.ljw.aachen.account.management.port.out.AccountStorePort;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.Validate;

@RequiredArgsConstructor
public class AccountService implements CreateAccountUseCase, DeleteAccountUseCase, ReadAccountUseCase, UpdateAccountUseCase {

    private final AccountStorePort accountStore;

    @Override
    public AccountId createAccount(CreateAccountCommand command) {
        var account = Account.createFor(command.getFirstName(), command.getLastName());
        accountStore.store(account);
        return account.getId();
    }

    @Override
    public void deleteAccount(DeleteAccountCommand command) {
        var id = command.getAccountId();
        if (accountStore.contains(id)) {
            accountStore.delete(id);
        }
    }

    @Override
    public Account readAccount(ReadAccountCommand command) {
        var id = command.getAccountId();
        Validate.isTrue(accountStore.contains(id), "No account for id %s", id);
        return accountStore.read(id);
    }

    @Override
    public void updateAccount(UpdateAccountCommand command) {
        var account = command.getAccount();
        Validate.isTrue(accountStore.contains(account.getId()), "No account for id %s", account.getId());
        accountStore.update(command.getAccount());
    }
}
