package de.ljw.aachen.flow.logic.accounts;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.data.Account;
import de.ljw.aachen.flow.data.AccountId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateAccount {

    private final FileSystem fs;
    private final AccountStore accountStore;

    public void process(Account account) {
        assert account.getId() == null : "Creating an account assumes a non existing account";
        account.setId(new AccountId());

        var checkNameUnique = new CheckNameUnique(accountStore);
        var storeAccount = new StoreAccount(fs, accountStore);

        if (checkNameUnique.process(account))
            storeAccount.process(account);
    }
}
