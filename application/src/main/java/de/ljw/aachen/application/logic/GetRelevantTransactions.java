package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.data.AccountId;
import de.ljw.aachen.application.data.Transaction;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GetRelevantTransactions {

    public static Collection<Transaction> process(AccountId accountId, Collection<Transaction> transactions) {
        Predicate<Transaction> earned = transaction -> accountId.equals(transaction.getTarget());
        Predicate<Transaction> spent = transaction -> accountId.equals(transaction.getSource());

        return transactions.stream()
                .filter(earned.or(spent))
                .collect(Collectors.toList());
    }

}
