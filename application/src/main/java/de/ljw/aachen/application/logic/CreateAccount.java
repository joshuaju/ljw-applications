package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.data.AccountId;
import de.ljw.aachen.application.exceptions.NameNotUniqueException;
import de.ljw.aachen.application.exceptions.NameNotValidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CreateAccount {

    private final FileSystem fs;
    private final AccountStore accountStore;

    public AccountId process(Account account) {
        assert account.getId() == null : "Creating an account assumes a non existing account";
        account.setId(new AccountId());

        var checkNameUnique = new CheckNameUnique(accountStore);
        var storeAccount = new StoreAccount(fs, accountStore);

        if (!CheckNameValid.process(account)) throw new NameNotValidException();
        if (!checkNameUnique.process(account)) throw new NameNotUniqueException();

        storeAccount.process(account);
        return account.getId();
    }
}
