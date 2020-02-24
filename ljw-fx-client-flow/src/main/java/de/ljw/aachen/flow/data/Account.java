package de.ljw.aachen.flow.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {

    private String id;
    private String firstName;
    private String lastName;
}
