package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.data.Account;

public class ComposeFullName {

    public static String process(Account account) {
        String firstName = account.getFirstName().trim();
        String lastName = account.getLastName().trim();
        return String.format("%s %s", firstName, lastName);
    }
}
