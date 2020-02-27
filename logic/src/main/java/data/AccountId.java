package data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class AccountId {

    private String value;

    public AccountId(){
        this(UUID.randomUUID().toString());
    }
}
