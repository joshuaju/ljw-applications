package de.ljw.aachen.flow.logic;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.data.Account;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CheckNameValid {

    public static boolean process(Account account) {
        String first = account.getFirstName();
        String last = account.getLastName();
        boolean invalid = first.isBlank() || last.isBlank();
        return !invalid;
    }


}
