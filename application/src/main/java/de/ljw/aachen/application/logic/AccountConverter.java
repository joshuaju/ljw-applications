package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.data.AccountId;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.util.List;

class AccountConverter {

    public static CSVFormat getFormat() {
        return CSVFormat.DEFAULT;
    }

    public static List<String> toValues(Account account) {
        return List.of(
                account.getId().getValue(),
                account.getFirstName(),
                account.getLastName()
        );
    }

    public static Account fromRecord(CSVRecord record) {
        var accountId = new AccountId(record.get(0));
        var firstName = record.get(1);
        var lastName = record.get(2);

        return new Account(accountId, firstName, lastName);
    }
}
