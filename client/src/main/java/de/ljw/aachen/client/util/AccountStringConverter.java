package de.ljw.aachen.client.util;

import de.ljw.aachen.application.data.Account;
import javafx.util.StringConverter;

public class AccountStringConverter extends StringConverter<Account> {

    @Override
    public String toString(Account account) {
        if (account == null) return "";
        else return String.format("%s %s", account.getFirstName(), account.getLastName());
    }

    @Override
    public Account fromString(String s) {
        throw new UnsupportedOperationException();
    }
}
