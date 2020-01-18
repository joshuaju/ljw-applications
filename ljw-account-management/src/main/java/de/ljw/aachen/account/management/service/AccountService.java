package de.ljw.aachen.account.management.service;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.account.management.domain.event.AccountCreatedEvent;
import de.ljw.aachen.account.management.domain.event.AccountDeletedEvent;
import de.ljw.aachen.account.management.domain.event.AccountUpdatedEvent;
import de.ljw.aachen.account.management.port.in.*;
import de.ljw.aachen.account.management.port.out.AccountStorePort;
import de.ljw.aachen.common.EventPort;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.Validate;

import java.util.Set;

@RequiredArgsConstructor
public class AccountService implements CreateAccountUseCase, DeleteAccountUseCase, ReadAccountUseCase, UpdateAccountUseCase, ListAccountsUseCase {

    private final AccountStorePort accountStore;
    private final EventPort eventPort;

    @Override
    public AccountId createAccount(CreateAccountCommand command) {
        var account = Account.createFor(command.getFirstName(), command.getLastName());
        accountStore.store(account);
        eventPort.publish(new AccountCreatedEvent());
        return account.getId();
    }

    @Override
    public void deleteAccount(DeleteAccountCommand command) {
        var id = command.getAccountId();
        if (accountStore.contains(id)) {
            accountStore.delete(id);
            eventPort.publish(new AccountDeletedEvent());
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
        eventPort.publish(new AccountUpdatedEvent());
    }

    @Override
    public Set<Account> listAccounts() {
        return accountStore.readAll();
    }
}
