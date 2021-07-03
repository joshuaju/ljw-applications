package de.ljw.aachen.client.util;

import de.ljw.aachen.application.data.Account;
import lombok.experimental.UtilityClass;

import java.util.Comparator;

@UtilityClass
public class CompareAccounts
{

    public static Comparator<Account> byFirstNameAndLastName()
    {
        return Comparator.comparing((Account account) -> account.getFirstName().toLowerCase())
                         .thenComparing(account1 -> account1.getLastName().toLowerCase());

    }

}
