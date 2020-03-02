package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.data.AccountId;
import de.ljw.aachen.application.data.Money;
import de.ljw.aachen.application.data.Transaction;
import de.ljw.aachen.application.logic.CalculateBalance;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CalculateBalanceTest {

    private static final AccountId ID = new AccountId();

    @Test
    void zeroBalance() {
        Collection<Transaction> transactions = List.of(
                Transaction.deposit(ID, new Money(10.0)),
                Transaction.withdraw(ID, new Money(10.0))
        );
        Money expectedBalance = new Money(0.0);

        Money balance = CalculateBalance.process(ID, transactions);

        assertThat(balance).isEqualTo(expectedBalance);
    }

    @Test
    void positiveBalance() {
        Collection<Transaction> transactions = List.of(
                Transaction.deposit(ID, new Money(10.0)),
                Transaction.withdraw(ID, new Money(5.0))
        );
        Money expectedBalance = new Money(5.0);

        Money balance = CalculateBalance.process(ID, transactions);

        assertThat(balance).isEqualTo(expectedBalance);
    }

    @Test
    void negativeBalance() {
        Collection<Transaction> transactions = List.of(
                Transaction.deposit(ID, new Money(5.0)),
                Transaction.withdraw(ID, new Money(10.0))
        );
        Money expectedBalance = new Money(-5.0);

        Money balance = CalculateBalance.process(ID, transactions);

        assertThat(balance).isEqualTo(expectedBalance);
    }

    @Test
    void zeroBalanceForNoTransactions() {
        Collection<Transaction> transactions = List.of();
        Money expectedBalance = new Money(0.0);

        Money balance = CalculateBalance.process(ID, transactions);

        assertThat(balance).isEqualTo(expectedBalance);
    }

}