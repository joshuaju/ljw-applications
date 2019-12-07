package de.ljw.aachen.account.management.port.out;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;

public interface AccountStorePort {

    void store(Account account);

    void update(Account account);

    boolean contains(AccountId id);

    Account read(AccountId id);

    void delete(AccountId id);

}
