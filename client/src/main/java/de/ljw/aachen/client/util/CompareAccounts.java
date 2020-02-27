package de.ljw.aachen.client.util;

import de.ljw.aachen.application.data.Account;
import lombok.experimental.UtilityClass;

import java.util.Comparator;

@UtilityClass
public class CompareAccounts {

    public static Comparator<Account> byFirstName() {
        return Comparator.comparing(account -> account.getFirstName().toLowerCase());
    }

}
