package de.ljw.aachen.account.management.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang.Validate;

import java.util.UUID;

@EqualsAndHashCode
@ToString
public class AccountId {

    @Getter
    public final String value;

    public AccountId(){
        this(UUID.randomUUID().toString());
    }

    public AccountId(String value) {
        this.value = value;
    }

}
