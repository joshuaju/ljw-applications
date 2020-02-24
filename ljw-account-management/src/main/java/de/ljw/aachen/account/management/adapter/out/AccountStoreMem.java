package de.ljw.aachen.account.management.adapter.out;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.account.management.port.out.AccountStorePort;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.Validate;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AccountStoreMem implements AccountStorePort {

    private final Map<AccountId, Account> accounts;

    public AccountStoreMem() {
        this(List.of());
    }

    public AccountStoreMem(Collection<Account> accounts) {
        this.accounts = accounts.stream()
                .collect(Collectors.toMap(Account::getId, Function.identity()));
    }

    @Override
    public void store(Account account) {
        accounts.put(account.getId(), account);
    }

    @Override
    public void update(Account account) {
        accounts.put(account.getId(), account);
    }

    @Override
    public boolean contains(AccountId id) {
        return accounts.containsKey(id);
    }

    @Override
    public Account read(AccountId id) {
        return new Account(accounts.get(id));
    }

    @Override
    public Set<Account> readAll() {
        return new HashSet<>(accounts.values());
    }

}
