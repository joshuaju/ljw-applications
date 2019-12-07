package de.ljw.aachen.lagerbank.service;

import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BalanceCalculatorTest {

    private static final AccountId ID = new AccountId();

    @Test
    void zeroBalance() {
        Collection<Transaction> transactions = List.of(
                Transaction.forDeposit(ID, Money.of(10.0)),
                Transaction.forWithdrawal(ID, Money.of(10.0))
        );
        Money expectedBalance = Money.of(0.0);

        Money balance = BalanceCalculator.calculateBalance(ID, transactions);

        assertThat(balance).isEqualTo(expectedBalance);
    }

    @Test
    void positiveBalance() {
        Collection<Transaction> transactions = List.of(
                Transaction.forDeposit(ID, Money.of(10.0)),
                Transaction.forWithdrawal(ID, Money.of(5.0))
        );
        Money expectedBalance = Money.of(5.0);

        Money balance = BalanceCalculator.calculateBalance(ID, transactions);

        assertThat(balance).isEqualTo(expectedBalance);
    }

    @Test
    void negativeBalance() {
        Collection<Transaction> transactions = List.of(
                Transaction.forDeposit(ID, Money.of(5.0)),
                Transaction.forWithdrawal(ID, Money.of(10.0))
        );
        Money expectedBalance = Money.of(-5.0);

        Money balance = BalanceCalculator.calculateBalance(ID, transactions);

        assertThat(balance).isEqualTo(expectedBalance);
    }

    @Test
    void zeroBalanceForNoTransactions() {
        Collection<Transaction> transactions = List.of();
        Money expectedBalance = Money.of(0.0);

        Money balance = BalanceCalculator.calculateBalance(ID, transactions);

        assertThat(balance).isEqualTo(expectedBalance);
    }

}