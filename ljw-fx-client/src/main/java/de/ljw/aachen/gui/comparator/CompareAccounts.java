package de.ljw.aachen.gui.comparator;

import de.ljw.aachen.account.management.domain.Account;
import lombok.experimental.UtilityClass;

import java.util.Comparator;

@UtilityClass
public class CompareAccounts {

    public static Comparator<Account> byFirstName() {
        return Comparator.comparing(account -> account.getFirstName().toLowerCase());
    }

}
