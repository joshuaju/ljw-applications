package de.ljw.aachen.lagerbank.adapter.out;

import de.ljw.aachen.lagerbank.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.port.out.TransactionStorePort;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TransactionStoreMem implements TransactionStorePort {

    private final LinkedList<Transaction> transactions;

    public TransactionStoreMem() {
        this(List.of());
    }

    public TransactionStoreMem(Collection<Transaction> transactions) {
        this.transactions = new LinkedList<>(transactions);
    }

    @Override
    public void add(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public Collection<Transaction> getAll() {
        return List.copyOf(transactions);
    }

    @Override
    public Collection<Transaction> getAll(AccountId accountId) {
        return List.copyOf(transactions.stream()
                .filter(t -> accountId.equals(t.getSource()) || accountId.equals(t.getTarget()))
                .collect(Collectors.toList()));
    }
}
