package de.ljw.aachen.flow.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
public class TransactionId {

    private String value;

    public TransactionId(){
        this(UUID.randomUUID().toString());
    }

    public TransactionId(String value) {
        this.value = value;
    }

}
