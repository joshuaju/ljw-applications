package de.ljw.aachen.flow.adapter;

import de.ljw.aachen.flow.data.Account;

import java.nio.file.Path;
import java.util.List;

public interface AccountStore {
    List<Account> getAccounts();

    Path getSource();

    void store(Account account);
}
