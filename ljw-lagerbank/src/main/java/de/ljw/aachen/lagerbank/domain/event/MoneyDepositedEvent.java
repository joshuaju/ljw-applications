package de.ljw.aachen.lagerbank.domain.event;

import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.TransactionId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MoneyDepositedEvent {

    @Getter
    private final AccountId accountId;

    @Getter
    private final TransactionId transactionId;
}
