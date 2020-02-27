package logic;

import adapter.FileSystem;
import adapter.TransactionStore;
import data.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ExecuteTransaction {

    private final FileSystem fs;
    private final TransactionStore transactionStore;

    public void process(Transaction transaction, Consumer<String> onError) {
        var determineTransactionType = new DetermineTransactionType();
        var checkCredit = new CheckCredit(transactionStore);
        var storeTransaction = new StoreTransaction(fs, transactionStore);

        determineTransactionType.setOnWithdrawal(checkCredit::process);
        determineTransactionType.setOnTransfer(checkCredit::process);
        determineTransactionType.setOnDeposit(storeTransaction::process);
        checkCredit.setOnCredible(storeTransaction::process);
        checkCredit.setOnNotCredible((t) -> onError.accept("Insufficient funds"));

        determineTransactionType.process(transaction);
    }
}
