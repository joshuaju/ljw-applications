package de.ljw.aachen.account.management.adapter.out;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.util.List;

public class AccountConverter {

    static CSVFormat getFormat() {
        return CSVFormat.DEFAULT;
    }

    static List<String> convertToValues(Account account){
        return List.of(
                account.getId().getValue(),
                account.getFirstName(),
                account.getLastName()
        );
    }

    static Account convertToAccount(CSVRecord record){
        var accountId = new AccountId(record.get(0));
        var firstName = record.get(1);
        var lastName = record.get(2);

        return new Account(accountId, firstName, lastName);
    }
}
