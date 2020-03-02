package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.data.Account;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CheckNameUnique {

    private final AccountStore accountStore;

    public boolean process(Account account) {
        String fullName = getFullName(account);

        return accountStore.getAccounts().stream()
                .map(this::getFullName)
                .noneMatch(fullName::equalsIgnoreCase);
    }

    private String getFullName(Account account) {
        String firstName = account.getFirstName().trim();
        String lastName = account.getLastName().trim();
        return String.format("%s %s", firstName, lastName);
    }
}
