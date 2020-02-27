package logic;

import adapter.AccountStore;
import data.Account;
import data.AccountId;
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
