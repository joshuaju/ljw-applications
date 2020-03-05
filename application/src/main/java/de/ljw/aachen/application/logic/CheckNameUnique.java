package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.data.Account;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CheckNameUnique {

    private final AccountStore accountStore;

    public boolean process(Account account) {
        String fullName = ComposeFullName.process(account);

        return accountStore.getAccounts().stream()
                .map(ComposeFullName::process)
                .noneMatch(fullName::equalsIgnoreCase);
    }


}
