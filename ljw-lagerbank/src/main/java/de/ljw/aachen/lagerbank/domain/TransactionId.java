package de.ljw.aachen.lagerbank.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang.Validate;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@ToString
public class TransactionId {

    private final String value;

    public TransactionId(){
        this(UUID.randomUUID().toString());
    }

    public TransactionId(String value) {
        this.value = value;
        Validate.notEmpty(this.value);
    }
}
