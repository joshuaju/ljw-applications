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
        var sourcePresent = transaction.getSource() != null;
        var targetPresent = transaction.getTarget() != null;

        if (sourcePresent) {
            if (targetPresent) onTransfer.accept(transaction);
            else onWithdrawal.accept(transaction);
        } else if (targetPresent) {
            onDeposit.accept(transaction);
        } else throw new IllegalArgumentException("Transaction requires a source or a target or both");
    }
}
