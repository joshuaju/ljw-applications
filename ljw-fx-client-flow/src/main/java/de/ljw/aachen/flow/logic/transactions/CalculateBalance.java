package de.ljw.aachen.flow.logic.transactions;

import de.ljw.aachen.flow.data.AccountId;
import de.ljw.aachen.flow.data.Money;
import de.ljw.aachen.flow.data.Transaction;

import java.util.Collection;
import java.util.function.Predicate;

public class CalculateBalance {

    public static Money process(AccountId accountId, Collection<Transaction> transactions) {
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
                .reduce(new Money(0.00), Money::plus);
    }

}
