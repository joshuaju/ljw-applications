package de.ljw.aachen.application.adapter;

import de.ljw.aachen.application.data.Account;

import java.nio.file.Path;
import java.util.List;

public interface AccountStore {
    List<Account> getAccounts();

    Path getSource();

    void store(Account account);

    void setSource(Path path);
}
