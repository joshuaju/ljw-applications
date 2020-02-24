package de.ljw.aachen.flow.logic.transactions;

import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.adapter.TransactionStore;
import de.ljw.aachen.flow.data.Transaction;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExecuteTransaction {

    private final FileSystem fs;
    private final TransactionStore transactionStore;

    public void process(Transaction transaction) {
        var determineTransactionType = new DetermineTransactionType();
        var checkCredit = new CheckCredit(transactionStore);
        var storeTransaction = new StoreTransaction(fs, transactionStore);

        determineTransactionType.setOnWithdrawal(checkCredit::process);
        determineTransactionType.setOnTransfer(checkCredit::process);

        determineTransactionType.setOnDeposit(storeTransaction::process);
        checkCredit.setOnCredible(storeTransaction::process);
        checkCredit.setOnNotCredible(storeTransaction::process);

        determineTransactionType.process(transaction);
    }
}
