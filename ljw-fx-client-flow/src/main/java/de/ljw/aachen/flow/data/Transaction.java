package de.ljw.aachen.flow.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction {

    private String id;
    private String sourceAccountId;
    private String targetAccountId;
    private String amount;
    private Instant time;

}
