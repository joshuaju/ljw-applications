package de.ljw.aachen.lagerbank.domain;

import lombok.*;

import java.time.Instant;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class Transaction {

    private final TransactionId id;
    private final AccountId source;
    private final AccountId target;
    private final Money amount;
    private final Instant time;

    public static Transaction forDeposit(AccountId target, Money amount) {
        return new Transaction(new TransactionId(), null, target, amount, Instant.now());
    }

    public static Transaction forWithdrawal(AccountId source, Money amount) {
        return new Transaction(new TransactionId(), source, null, amount, Instant.now());
    }

    public static Transaction forTransfer(AccountId source, AccountId target, Money amount) {
        return new Transaction(new TransactionId(), source, target, amount, Instant.now());
    }

}
