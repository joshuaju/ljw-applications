package de.ljw.aachen.lagerbank.service;

import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.lagerbank.adapter.out.TransactionStoreMem;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.port.in.*;
import de.ljw.aachen.lagerbank.port.out.TransactionStorePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionServiceTest {

    private TransactionStorePort transactionStorePort;

    private DepositMoneyUseCase depositMoneyService;
    private WithdrawMoneyUseCase withdrawMoneyService;
    private TransferMoneyUseCase transferMoneyService;

    private GetBalanceUseCase getBalanceService;
    public static final AccountId peter = new AccountId();
    public static final AccountId julia = new AccountId();

    @BeforeEach
    void setup() {
        this.transactionStorePort = new TransactionStoreMem();

        TransactionService transactionService = new TransactionService(transactionStorePort);
        this.depositMoneyService = transactionService;
        this.withdrawMoneyService = transactionService;
        this.transferMoneyService = transactionService;

        this.getBalanceService = new BalanceService(transactionStorePort);
    }

    @Test
    void makeSomeTransactions() throws WithdrawalNotAllowedException {
        depositMoneyService.deposit(Money.of(10.0), peter);
        depositMoneyService.deposit(Money.of(5.0), julia);

        assertThat(getBalanceService.getBalance(peter)).isEqualTo(Money.of(10.0));
        assertThat(getBalanceService.getBalance(julia)).isEqualTo(Money.of(5.0));

        withdrawMoneyService.withdraw(Money.of(5.0), peter);
        assertThat(getBalanceService.getBalance(peter)).isEqualTo(Money.of(5.0));

        transferMoneyService.transfer(Money.of(5.0), peter, julia);
        assertThat(getBalanceService.getBalance(peter)).isEqualTo(Money.of(0.0));
        assertThat(getBalanceService.getBalance(julia)).isEqualTo(Money.of(10.0));
    }

    @Test
    void failToWithdrawWhenBalanceWouldBeOverdrawn() {
        Money balance = Money.of(10.0);
        depositMoneyService.deposit(balance, peter);

        assertThrows(WithdrawalNotAllowedException.class, () -> withdrawMoneyService.withdraw(Money.of(10.1), peter));

        assertThat(getBalanceService.getBalance(peter)).isEqualTo(balance);
    }

    @Test
    void failToTransferWhenBalanceWouldBeOverdrawn() {
        Money petersBalance = Money.of(10.0);
        Money juliasBalance = Money.of(0.5);

        depositMoneyService.deposit(petersBalance, peter);
        depositMoneyService.deposit(juliasBalance, julia);

        assertThrows(WithdrawalNotAllowedException.class, () -> transferMoneyService.transfer(Money.of(10.1), peter, julia));

        assertThat(getBalanceService.getBalance(peter)).isEqualTo(petersBalance);
        assertThat(getBalanceService.getBalance(julia)).isEqualTo(juliasBalance);

    }

}