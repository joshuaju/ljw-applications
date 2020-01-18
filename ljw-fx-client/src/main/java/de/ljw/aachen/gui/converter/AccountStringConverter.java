package de.ljw.aachen.gui.converter;

import de.ljw.aachen.account.management.domain.Account;
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
