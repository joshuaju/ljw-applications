package de.ljw.aachen.application.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction {

    private TransactionId id;
    private AccountId source;
    private AccountId target;
    private Money amount;
    private Instant time;
    private String description;

    public static Transaction deposit(AccountId to, Money amount){
        return new Transaction(new TransactionId(), null, to, amount, Instant.now(), "deposit");
    }

    public static Transaction withdraw(AccountId from, Money amount){
        return new Transaction(new TransactionId(), from, null, amount, Instant.now(), "withdraw");
    }

    public static Transaction transfer(AccountId from, AccountId to, Money amount){
        return new Transaction(new TransactionId(), from, to, amount, Instant.now(), "transfer");
    }


}
