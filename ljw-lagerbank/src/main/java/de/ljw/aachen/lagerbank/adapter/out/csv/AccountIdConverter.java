package de.ljw.aachen.lagerbank.adapter.out.csv;

import de.ljw.aachen.account.management.domain.AccountId;

class AccountIdConverter {

    private static final String NO_ACCOUNT = "-";


    static String convert(AccountId id) {
        return id == null ? NO_ACCOUNT : id.getValue();
    }

    static AccountId convert(String value) {
        if (NO_ACCOUNT.equals(value)) return null;

        return new AccountId(value);
    }
}
