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

import javax.swing.*;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@RequiredArgsConstructor
public class AccountService implements CreateAccountUseCase, DeleteAccountUseCase, ReadAccountUseCase, UpdateAccountUseCase, ListAccountsUseCase {

    private final AccountStorePort accountStore;
    private final EventPort eventPort;

    @Override
    public AccountId createAccount(CreateAccountCommand command) {
        if (accountWithSameNameExists(command.getFirstName(), command.getLastName()).isPresent()) {
            throw new IllegalArgumentException(String.format("An account with name '%s %s' already exists", command.getFirstName(), command.getLastName()));
        }
        var account = Account.createFor(command.getFirstName(), command.getLastName());
        accountStore.store(account);
        eventPort.publish(new AccountCreatedEvent());
        return account.getId();
    }

    private Optional<Account> accountWithSameNameExists(String firstName, String lastName) {
        Function<String, String> trimAndLower = s -> s.trim().toLowerCase();
        return accountStore.readAll().stream().filter(account -> {
            boolean firstNameEquals = trimAndLower.apply(account.getFirstName()).equals(trimAndLower.apply(firstName));
            boolean lastNameEquals = trimAndLower.apply(account.getLastName()).equals(trimAndLower.apply(lastName));
            return firstNameEquals && lastNameEquals;
        }).findFirst();
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
        var idOfaccountWithSameName = accountWithSameNameExists(command.getAccount().getFirstName(), command.getAccount().getLastName())
                .map(Account::getId).orElse(null);

        boolean otherAccountHasDifferentId = !command.getAccount().getId().equals(idOfaccountWithSameName);
        if (otherAccountHasDifferentId) {
            throw new IllegalArgumentException(String.format("An other account with name '%s %s' already exists", command.getAccount().getFirstName(), command.getAccount().getLastName()));
        }

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
