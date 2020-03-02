package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.application.data.Transaction;
import de.ljw.aachen.application.exceptions.InsufficientFundsException;
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
        checkCredit.setOnNotCredible((t) -> {
            throw new InsufficientFundsException();
        });

        determineTransactionType.process(transaction);
    }
}
