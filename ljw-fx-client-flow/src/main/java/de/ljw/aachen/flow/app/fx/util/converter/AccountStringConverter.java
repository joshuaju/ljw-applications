package de.ljw.aachen.flow.app.fx.util.converter;

import de.ljw.aachen.flow.data.Account;
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
