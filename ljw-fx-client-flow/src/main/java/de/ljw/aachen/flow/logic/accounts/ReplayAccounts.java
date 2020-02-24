package de.ljw.aachen.flow.logic.accounts;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.data.Account;
import de.ljw.aachen.flow.data.AccountId;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class ReplayAccounts {

    private final AccountStore accountStore;

    public void process(Collection<Account> accounts) {
        Map<AccountId, Account> accountMap = new HashMap<>();
        accounts.forEach(account -> {
            accountMap.put(account.getId(), account);
        });
        accountMap.values().forEach(accountStore::store);
    }

}
