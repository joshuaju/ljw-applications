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
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AccountService implements CreateAccountUseCase, DeleteAccountUseCase, ReadAccountUseCase, UpdateAccountUseCase, ListAccountsUseCase {

    private final AccountStorePort accountStore;
    private final EventPort eventPort;

    @Override
    public AccountId createAccount(CreateAccountCommand command) {
        if (accountWithSameNameExists(listAccounts(), command.getFirstName(), command.getLastName()).isPresent()) {
            throw new IllegalArgumentException(String.format("An account with name '%s %s' already exists", command.getFirstName(), command.getLastName()));
        }
        var account = Account.createFor(command.getFirstName(), command.getLastName());
        accountStore.store(account);
        eventPort.publish(new AccountCreatedEvent());
        return account.getId();
    }

    private Optional<Account> accountWithSameNameExists(Collection<Account> accounts, String firstName, String lastName) {
        String fullName = makeTrimmedLowerCaseFullName(firstName, lastName);
        return accounts.stream().filter(account -> {
            String fullNameOther = makeTrimmedLowerCaseFullName(account.getFirstName(), account.getLastName());
            return fullName.equals(fullNameOther);
        }).findFirst();
    }

    private String makeTrimmedLowerCaseFullName(String firstName, String lastName) {
        Function<String, String> trimAndLower = s -> s.trim().toLowerCase();
        return String.format("%s %s", trimAndLower.apply(firstName), trimAndLower.apply(lastName));
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
        var otherAccountWithSameName = accountWithSameNameExists(
                listAccounts().stream()
                        .filter(account -> !account.getId().equals(command.getAccount().getId()))
                        .collect(Collectors.toSet()),
                command.getAccount().getFirstName(),
                command.getAccount().getLastName());

        if (otherAccountWithSameName.isPresent()) {
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
