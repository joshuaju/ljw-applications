package logic;

import adapter.AccountStore;
import adapter.FileSystem;
import data.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class EditAccount {

    private final FileSystem fs;
    private final AccountStore accountStore;

    public void process(Account account) {
        assert account.getId() != null : "Editing an account assumes an existing account";

        var checkNameUnique = new CheckNameUnique(accountStore);
        var storeAccount = new StoreAccount(fs, accountStore);

        if (!CheckNameValid.process(account))
            if (checkNameUnique.process(account))
                storeAccount.process(account);
            else log.error("Name not unique");
        else log.error("Name not valid");
    }

}