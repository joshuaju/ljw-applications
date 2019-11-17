package de.ljw.aachen.lagerbank.domain;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang.Validate;

@EqualsAndHashCode
@ToString
public class TransactionId {

    String id;

    public TransactionId(String id) {
        this.id = id;
        Validate.notEmpty(this.id);
    }
}
