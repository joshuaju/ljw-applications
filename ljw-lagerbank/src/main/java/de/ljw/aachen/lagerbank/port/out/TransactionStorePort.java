package de.ljw.aachen.lagerbank.port.out;

import de.ljw.aachen.lagerbank.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Transaction;

import java.util.Collection;
import java.util.function.Predicate;

public interface TransactionStorePort {

    void add(Transaction transaction);

    Collection<Transaction> getAll();

    Collection<Transaction> getAll(AccountId accountId);

}
