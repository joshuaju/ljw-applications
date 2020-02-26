package de.ljw.aachen.flow.logic;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.data.Account;
import de.ljw.aachen.flow.data.AccountId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CreateAccount {

    private final FileSystem fs;
    private final AccountStore accountStore;

    public void process(Account account) {
        assert account.getId() == null : "Creating an account assumes a non existing account";
        account.setId(new AccountId());

        var checkNameUnique = new CheckNameUnique(accountStore);
        var storeAccount = new StoreAccount(fs, accountStore);

        if (CheckNameValid.process(account))
            if (checkNameUnique.process(account))
                storeAccount.process(account);
            else log.error("Name not unique");
        else log.error("Name not valid");
    }
}
