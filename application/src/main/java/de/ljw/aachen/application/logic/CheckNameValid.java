package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.data.Account;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CheckNameValid {

    public static boolean process(Account account) {
        String first = account.getFirstName();
        String last = account.getLastName();
        boolean invalid = first.isBlank() || last.isBlank();
        return !invalid;
    }


}
