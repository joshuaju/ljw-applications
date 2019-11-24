package de.ljw.aachen.lagerbank.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang.Validate;

@Getter
@EqualsAndHashCode
@ToString
public class TransactionId {

    String value;

    public TransactionId(String value) {
        this.value = value;
        Validate.notEmpty(this.value);
    }
}
