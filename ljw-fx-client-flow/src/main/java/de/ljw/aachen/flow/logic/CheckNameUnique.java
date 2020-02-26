package de.ljw.aachen.flow.logic;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.data.Account;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class CheckNameUnique {

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
