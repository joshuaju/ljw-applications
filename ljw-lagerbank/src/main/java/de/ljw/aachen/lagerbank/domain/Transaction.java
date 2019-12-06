package de.ljw.aachen.lagerbank.domain;

import de.ljw.aachen.account.management.domain.AccountId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.apache.commons.lang.Validate;

import java.time.Instant;
import java.util.UUID;

@FieldNameConstants
@Getter
@EqualsAndHashCode
@ToString
public class Transaction {

    private final TransactionId id;
    private final AccountId source;
    private final AccountId target;
    private final Money amount;
    private final Instant time;

    public Transaction(TransactionId id, AccountId source, AccountId target, Money amount, Instant time) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.amount = amount;
        this.time = time;

        Validate.notNull(this.id);
        Validate.isTrue(this.amount.isGreaterThan(Money.of(0.0)));
        Validate.notNull(this.time);
    }

    public static Transaction forDeposit(AccountId target, Money amount) {
        return new Transaction(generateTransactionId(), null, target, amount, Instant.now());
    }

    public static Transaction forWithdrawal(AccountId source, Money amount) {
        return new Transaction(generateTransactionId(), source, null, amount, Instant.now());
    }

    public static Transaction forTransfer(AccountId source, AccountId target, Money amount) {
        return new Transaction(generateTransactionId(), source, target, amount, Instant.now());
    }

    private static TransactionId generateTransactionId() {
        return new TransactionId(UUID.randomUUID().toString());
    }

}
