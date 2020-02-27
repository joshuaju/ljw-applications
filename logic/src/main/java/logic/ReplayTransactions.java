package logic;

import adapter.TransactionStore;
import data.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ReplayTransactions {

    private final TransactionStore transactionStore;

    public void process(List<Transaction> transactions) {
        transactions.forEach(transactionStore::store);
    }
}
