package de.ljw.aachen.lagerbank.service;

import de.ljw.aachen.lagerbank.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.port.in.GetBalanceUseCase;
import de.ljw.aachen.lagerbank.port.out.TransactionStorePort;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
public class BalanceService implements GetBalanceUseCase {

    private final TransactionStorePort transactionStore;

    @Override
    public Money getBalance(AccountId accountId) {
        Collection<Transaction> transactions = transactionStore.getAll(accountId);
        return BalanceCalculator.calculateBalance(accountId, transactions);
    }
}