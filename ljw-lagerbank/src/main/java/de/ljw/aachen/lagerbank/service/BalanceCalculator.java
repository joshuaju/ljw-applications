package de.ljw.aachen.lagerbank.service;

import de.ljw.aachen.lagerbank.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;

import java.util.Collection;
import java.util.stream.Stream;

class BalanceCalculator {

    static Money calculateBalance(AccountId accountId, Collection<Transaction> transactions) {
        Money earnings = getEarnings(accountId, transactions).reduce(Money.of(0.0), Money::plus);
        Money expenses = getExpenses(accountId, transactions).reduce(Money.of(0.0), Money::plus);
        return earnings.minus(expenses);
    }

    private static Stream<Money> getEarnings(AccountId accountId, Collection<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> accountId.equals(transaction.getTarget()))
                .map(Transaction::getAmount);

    }

    private static Stream<Money> getExpenses(AccountId accountId, Collection<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> accountId.equals(transaction.getSource()))
                .map(Transaction::getAmount);
    }
}
