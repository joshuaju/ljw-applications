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

    public void process(Transaction transaction, boolean ignoreCredibility) {
        var determineTransactionType = new DetermineTransactionType();
        var checkCredit = new CheckCredit(transactionStore);
        var storeTransaction = new StoreTransaction(fs, transactionStore);

        determineTransactionType.setOnWithdrawal(checkCredit::process);
        determineTransactionType.setOnTransfer(checkCredit::process);
        determineTransactionType.setOnDeposit(storeTransaction::process);
        checkCredit.setOnCredible(storeTransaction::process);
        if (ignoreCredibility) checkCredit.setOnNotCredible(storeTransaction::process);
        else checkCredit.setOnNotCredible(ExecuteTransaction::fail);

        determineTransactionType.process(transaction);
    }

    private static void fail(Transaction t) {
        throw new InsufficientFundsException();
    }
}
