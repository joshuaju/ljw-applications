package de.ljw.aachen.flow.logic.accounts;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.data.Account;
import javafx.beans.property.ListProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EditAccount {

    private final FileSystem fs;
    private final AccountStore accountStore;

    public boolean process(Account account) {
        assert account.getId() != null : "Editing an account assumes an existing account";

        var checkNameUnique = new CheckNameUnique(accountStore);
        var storeAccount = new StoreAccount(fs, accountStore);

        if (checkNameUnique.process(account)) {
            storeAccount.process(account);
            return true;
        }
        return false;
    }

}