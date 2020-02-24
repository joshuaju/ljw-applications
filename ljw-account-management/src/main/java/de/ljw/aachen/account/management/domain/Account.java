package de.ljw.aachen.account.management.domain;

import lombok.*;
import org.apache.commons.lang.Validate;

import java.time.Instant;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Account {

    private final AccountId id;

    private String firstName;
    private String lastName;

    public Account(Account account) {
        this(account.id, account.firstName, account.lastName);
    }

    public Account(AccountId id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;

        Validate.notNull(id);
        Validate.isTrue(firstName != null && !firstName.isBlank());
        Validate.isTrue(lastName != null && !lastName.isBlank());
    }

    public static Account createFor(String firstName, String lastName) {
        return new Account(new AccountId(), firstName, lastName);
    }

}
