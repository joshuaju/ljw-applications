package de.ljw.aachen.flow.logic.transactions;

import de.ljw.aachen.flow.adapter.TransactionStore;
import de.ljw.aachen.flow.data.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ReplayTransactions {

    private final TransactionStore transactionStore;

    void process(List<Transaction> transactions) {
        transactions.forEach(transactionStore::store);
    }
}
