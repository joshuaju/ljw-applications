package de.ljw.aachen.flow.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.UUID;

@AllArgsConstructor
@Data
public class AccountId {

    private String value;

    public AccountId(){
        this(UUID.randomUUID().toString());
    }
}
