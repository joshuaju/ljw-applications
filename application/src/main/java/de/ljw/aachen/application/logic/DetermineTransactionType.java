package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.data.Transaction;
import lombok.Setter;

import java.util.function.Consumer;

public class DetermineTransactionType {

    @Setter
    Consumer<Transaction> onDeposit;
    @Setter
    Consumer<Transaction> onWithdrawal;
    @Setter
    Consumer<Transaction> onTransfer;

    public void process(Transaction transaction) {
        if (transaction.isDeposit()) onDeposit.accept(transaction);
        else if (transaction.isWithdrawal()) onWithdrawal.accept(transaction);
        else if (transaction.isTransfer()) onTransfer.accept(transaction);
        else throw new IllegalArgumentException("Transaction requires a source or a target or both");
    }
}
