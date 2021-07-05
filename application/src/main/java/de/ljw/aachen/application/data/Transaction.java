package de.ljw.aachen.application.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.Validate;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction
{

    private TransactionId id;
    private AccountId source;
    private AccountId target;
    private Money amount;
    private Instant time;
    private String description;
    private String info;

    public static Transaction deposit(AccountId to, Money amount)
    {
        Validate.notNull(to);
        return new Transaction(new TransactionId(), null, to, amount, Instant.now(), "", "");
    }

    public static Transaction importDeposit(AccountId to, Money amount)
    {
        Validate.notNull(to);
        return new Transaction(new TransactionId(), null, to, amount, Instant.now(), "", "IMPORT");
    }

    public static Transaction withdraw(AccountId from, Money amount)
    {
        Validate.notNull(from);
        return new Transaction(new TransactionId(), from, null, amount, Instant.now(), "", "");
    }

    public static Transaction transfer(AccountId from, AccountId to, Money amount)
    {
        Validate.notNull(from);
        Validate.notNull(to);
        return new Transaction(new TransactionId(), from, to, amount, Instant.now(), "", "");
    }

    public boolean isDeposit()
    {
        return source == null && target != null;
    }

    public boolean isImportDeposit()
    {
        return isDeposit() && "IMPORT".equals(info);
    }

    public boolean isWithdrawal()
    {
        return source != null && target == null;
    }

    public boolean isTransfer()
    {
        return source != null && target != null;
    }

}
