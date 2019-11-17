package de.ljw.aachen.lagerbank.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang.Validate;

@EqualsAndHashCode
@ToString
public class AccountId {

    @Getter
    public final long id;

    public AccountId(long id) {
        this.id = id;
    }
}
