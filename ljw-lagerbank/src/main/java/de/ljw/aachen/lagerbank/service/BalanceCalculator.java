package de.ljw.aachen.lagerbank.service;

import de.ljw.aachen.lagerbank.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;

import java.util.Collection;

class BalanceCalculator {

    static Money calculateBalance(AccountId accountId, Collection<Transaction> transactions) {
        Money earnings = calculateEarnings(accountId, transactions);
        Money expenses = calculateExpenses(accountId, transactions);
        return earnings.minus(expenses);
    }

    private static Money calculateEarnings(AccountId accountId, Collection<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> accountId.equals(transaction.getTarget()))
                .map(Transaction::getAmount)
                .reduce(Money.of(0.0), Money::plus);

    }

    private static Money calculateExpenses(AccountId accountId, Collection<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> accountId.equals(transaction.getSource()))
                .map(Transaction::getAmount)
                .reduce(Money.of(0.0), Money::plus);
    }
}
