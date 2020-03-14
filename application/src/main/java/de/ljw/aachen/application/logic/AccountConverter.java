package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.data.AccountId;

class AccountConverter {

    private static final String DELIMITER = ",";

    public static String getHeader() {
        return String.join(DELIMITER, "id", "first name", "last name");
    }

    public static String toString(Account account) {
        return String.join(DELIMITER,
                account.getId().getValue(),
                account.getFirstName(),
                account.getLastName()
        );
    }

    public static Account fromString(String accountString) {
        var values = accountString.split(DELIMITER);

        var accountId = new AccountId(values[0]);
        var firstName = values[1];
        var lastName = values[2];

        return new Account(accountId, firstName, lastName);
    }
}
