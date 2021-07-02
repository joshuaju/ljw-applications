package de.ljw.aachen.application.adapter;

import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.data.AccountId;

import java.nio.file.Path;
import java.util.List;

public interface AccountStore {
    List<Account> getAccounts();

    Path getSource();

    Account find(AccountId id);

    void store(Account account);

    void setSource(Path path);
}
