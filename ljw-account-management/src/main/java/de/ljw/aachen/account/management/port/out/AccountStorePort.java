package de.ljw.aachen.account.management.port.out;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;

import java.util.Set;

public interface AccountStorePort {

    void store(Account account);

    void update(Account account);

    boolean contains(AccountId id);

    Account read(AccountId id);

    void delete(AccountId id);

    Set<Account> readAll();

}
