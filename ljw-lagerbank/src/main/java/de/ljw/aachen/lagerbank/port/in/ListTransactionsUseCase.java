package de.ljw.aachen.lagerbank.port.in;

import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Transaction;

import java.util.Collection;

public interface ListTransactionsUseCase {

    Collection<Transaction> listTransactions(AccountId accountId);
}
