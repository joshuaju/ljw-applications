package de.ljw.aachen.flow.logic;

import de.ljw.aachen.flow.data.Account;
import de.ljw.aachen.flow.data.AccountId;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.util.List;

public class AccountConverter {

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
