package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.data.Money;
import de.ljw.aachen.application.data.Transaction;

import java.util.Collection;

public class CashUp {

    public static Money cashUp(Collection<Transaction> transactions) {
        var deposits = CalculateBalance.sum(transactions, Transaction::isDeposit);
        var withdrawals = CalculateBalance.sum(transactions, Transaction::isWithdrawal);
        return deposits.minus(withdrawals);
    }
}
