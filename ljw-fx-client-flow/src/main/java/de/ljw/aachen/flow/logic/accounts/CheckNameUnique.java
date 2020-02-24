package de.ljw.aachen.flow.logic.accounts;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.data.Account;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class CheckNameUnique {

    private final AccountStore accountStore;

    @Setter
    private Consumer<Account> onUnique;
    @Setter
    private Consumer<Account> onNotUnique;

    public void process(Account account) {
        String fullName = getFullName(account);

        var alreadyUsed = accountStore.getAccounts().stream()
                .map(this::getFullName)
                .anyMatch(fullName::equalsIgnoreCase);

        if (alreadyUsed) onNotUnique.accept(account);
        else onUnique.accept(account);
    }

    private String getFullName(Account account) {
        String firstName = account.getFirstName().trim();
        String lastName = account.getLastName().trim();
        return String.format("%s %s", firstName, lastName);
    }
}
