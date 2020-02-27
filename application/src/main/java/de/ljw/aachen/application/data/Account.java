package de.ljw.aachen.application.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {

    private AccountId id;
    private String firstName;
    private String lastName;
}
