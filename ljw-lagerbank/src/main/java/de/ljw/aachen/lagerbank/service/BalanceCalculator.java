package de.ljw.aachen.lagerbank.service;

import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

class BalanceCalculator {


    static Money calculateBalance(AccountId accountId, Collection<Transaction> transactions) {
        Predicate<Transaction> earned = transaction -> accountId.equals(transaction.getTarget());
        Predicate<Transaction> spent = transaction -> accountId.equals(transaction.getSource());

        Money earnings = sum(transactions, earned);
        Money expenses = sum(transactions, spent);

        return earnings.minus(expenses);
    }

    private static Money sum(Collection<Transaction> transactions, Predicate<Transaction> predicate) {
        return transactions.stream()
                .filter(predicate)
                .map(Transaction::getAmount)
                .reduce(Money.of(0.0), Money::plus);
    }

}
