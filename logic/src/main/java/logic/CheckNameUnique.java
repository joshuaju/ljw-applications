package logic;

import adapter.AccountStore;
import data.Account;
import lombok.RequiredArgsConstructor;

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
