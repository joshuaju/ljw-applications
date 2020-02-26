package de.ljw.aachen.flow.app.util;

import de.ljw.aachen.flow.data.Account;
import lombok.experimental.UtilityClass;

import java.util.Comparator;

@UtilityClass
public class CompareAccounts {

    public static Comparator<Account> byFirstName() {
        return Comparator.comparing(account -> account.getFirstName().toLowerCase());
    }

}
