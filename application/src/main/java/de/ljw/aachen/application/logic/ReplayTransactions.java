package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.application.data.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ReplayTransactions {

    private final TransactionStore transactionStore;

    public void process(List<Transaction> transactions) {
        transactions.forEach(transactionStore::store);
    }
}
