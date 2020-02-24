package de.ljw.aachen.flow.logic.transactions;

import de.ljw.aachen.flow.adapter.TransactionStore;
import de.ljw.aachen.flow.data.AccountId;
import de.ljw.aachen.flow.data.Money;
import de.ljw.aachen.flow.data.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class CheckCreditTest {
    AccountId testAccount = new AccountId("testAccountId");

    List<Transaction> balanceNegativeFive = List.of(
            Transaction.withdraw(testAccount, new Money(10.0)),
            Transaction.deposit(testAccount, new Money(5.0))
    );

    List<Transaction> balanceZero = List.of(
            Transaction.withdraw(testAccount, new Money(2.5)),
            Transaction.deposit(testAccount, new Money(2.5))
    );

    List<Transaction> balancePositiveSeven = List.of(
            Transaction.withdraw(testAccount, new Money(3)),
            Transaction.deposit(testAccount, new Money(10))
    );

    CheckCredit checkCredit;
    Boolean isCredible;
    Boolean isNotCredible;

    TransactionStore mockedTransactionStore = mock(TransactionStore.class);

    @BeforeEach
    void beforeEach() {
        checkCredit = new CheckCredit(mockedTransactionStore);
        checkCredit.setOnCredible(t -> isCredible = true);
        checkCredit.setOnNotCredible(t -> isNotCredible = true);

        isCredible = null;
        isNotCredible = null;
    }

    @Test
    void withdrawWithNegativeBalance() {
        doReturn(balanceNegativeFive).when(mockedTransactionStore).getTransactions();

        checkCredit.process(Transaction.withdraw(testAccount, new Money(5.1)));

        assertThat(isCredible).isNull();
        assertThat(isNotCredible).isTrue();
    }

    @Test
    void transferWithNegativeBalance() {
        doReturn(balanceNegativeFive).when(mockedTransactionStore).getTransactions();

        checkCredit.process(Transaction.transfer(testAccount, new AccountId(), new Money(5.1)));

        assertThat(isCredible).isNull();
        assertThat(isNotCredible).isTrue();
    }

    @Test
    void withdrawWithZeroBalance() {
        doReturn(balanceZero).when(mockedTransactionStore).getTransactions();

        checkCredit.process(Transaction.withdraw(testAccount, new Money(0.01)));

        assertThat(isCredible).isNull();
        assertThat(isNotCredible).isTrue();
    }

    @Test
    void transferWithZeroBalance() {
        doReturn(balanceZero).when(mockedTransactionStore).getTransactions();

        checkCredit.process(Transaction.transfer(testAccount, testAccount, new Money(0.01)));

        assertThat(isCredible).isNull();
        assertThat(isNotCredible).isTrue();
    }

    @Test
    void withdrawWithPositiveBalance() {
        doReturn(balancePositiveSeven).when(mockedTransactionStore).getTransactions();

        checkCredit.process(Transaction.withdraw(testAccount, new Money(7.1)));

        assertThat(isCredible).isNull();
        assertThat(isNotCredible).isTrue();
    }

    @Test
    void transferWithPositiveBalance() {
        doReturn(balancePositiveSeven).when(mockedTransactionStore).getTransactions();

        checkCredit.process(Transaction.transfer(testAccount, new AccountId(), new Money(7.1)));

        assertThat(isCredible).isNull();
        assertThat(isNotCredible).isTrue();
    }

}