package de.ljw.aachen.account.management.domain;

import lombok.*;
import org.apache.commons.lang.Validate;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode
public class Account {

    private final AccountId id;
    private final Instant created;

    private String firstName;
    private String lastName;


    public Account(AccountId id, Instant created, String firstName, String lastName) {
        this.id = id;
        this.created = created;
        this.firstName = firstName;
        this.lastName = lastName;

        Validate.notNull(id);
        Validate.notNull(created);
        Validate.isTrue(firstName != null && !firstName.isBlank());
        Validate.isTrue(lastName != null && !lastName.isBlank());
    }

    public static Account createFor(String firstName, String lastName){
        return new Account(new AccountId(), Instant.now(), firstName, lastName);
    }

}
