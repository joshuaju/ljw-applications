package de.ljw.aachen.lagerbank.service;

import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.port.in.GetBalanceUseCase;
import de.ljw.aachen.lagerbank.port.in.ListTransactionsUseCase;
import de.ljw.aachen.lagerbank.port.out.TransactionStorePort;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
public class BalanceService implements GetBalanceUseCase, ListTransactionsUseCase {

    private final TransactionStorePort transactionStore;

    @Override
    public Money getBalance(AccountId accountId) {
        var transactions = listTransactions(accountId);
        return BalanceCalculator.calculateBalance(accountId, transactions);
    }

    @Override
    public Collection<Transaction> listTransactions(AccountId accountId) {
        return transactionStore.getAll(accountId);
    }
}