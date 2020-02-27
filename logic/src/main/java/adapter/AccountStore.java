package adapter;

import data.Account;

import java.nio.file.Path;
import java.util.List;

public interface AccountStore {
    List<Account> getAccounts();

    Path getSource();

    void store(Account account);

    void setSource(Path path);
}
