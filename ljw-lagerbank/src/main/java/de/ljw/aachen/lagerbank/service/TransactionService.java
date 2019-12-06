package de.ljw.aachen.lagerbank.service;

import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.port.in.DepositMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.TransferMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.WithdrawMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.WithdrawalNotAllowedException;
import de.ljw.aachen.lagerbank.port.out.TransactionStorePort;
import lombok.RequiredArgsConstructor;

import static de.ljw.aachen.lagerbank.service.BalanceCalculator.calculateBalance;

@RequiredArgsConstructor
public class TransactionService implements DepositMoneyUseCase, WithdrawMoneyUseCase, TransferMoneyUseCase {

    private final TransactionStorePort transactionStore;

    @Override
    public void deposit(Money amount, AccountId accountId) {
        Transaction transaction = Transaction.forDeposit(accountId, amount);
        transactionStore.add(transaction);
    }

    @Override
    public void withdraw(Money amount, AccountId accountId) throws WithdrawalNotAllowedException {
        Money balance = calculateBalance(accountId, transactionStore.getAll(accountId));

        checkWithdrawal(amount, balance);

        Transaction transaction = Transaction.forWithdrawal(accountId, amount);
        transactionStore.add(transaction);
    }

    @Override
    public void transfer(Money amount, AccountId sourceId, AccountId targetId) throws WithdrawalNotAllowedException {
        Money sourceAccountBalance = calculateBalance(sourceId, transactionStore.getAll(sourceId));

        checkWithdrawal(amount, sourceAccountBalance);

        Transaction transaction = Transaction.forTransfer(sourceId, targetId, amount);
        transactionStore.add(transaction);
    }

    private void checkWithdrawal(Money amount, Money balance) throws WithdrawalNotAllowedException {
        boolean withdrawalAllowed = amount.isLessThanOrEqualTo(balance);
        if (!withdrawalAllowed) {
            throw new WithdrawalNotAllowedException();
        }
    }

}
