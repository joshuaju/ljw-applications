package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.application.data.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class CheckCredit {

    private final TransactionStore transactionStore;

    @Setter
    private Consumer<Transaction> onCredible;
    @Setter
    private Consumer<Transaction> onNotCredible;

    public void process(Transaction transaction) {
        assert transaction.getSource() != null;

        var accountToBeChecked = transaction.getSource();
        var transactions = transactionStore.getTransactions();

        var balance = CalculateBalance.process(accountToBeChecked, transactions);
        boolean sufficientBalance = balance.isGreaterThanOrEqual(transaction.getAmount());

        if (sufficientBalance) onCredible.accept(transaction);
        else onNotCredible.accept(transaction);
    }

}
