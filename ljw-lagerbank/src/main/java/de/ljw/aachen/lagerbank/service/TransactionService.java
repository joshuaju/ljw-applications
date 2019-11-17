package de.ljw.aachen.lagerbank.service;

import de.ljw.aachen.lagerbank.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.port.in.DepositMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.TransferMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.WithdrawMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.WithdrawalNotAllowedException;
import de.ljw.aachen.lagerbank.port.out.TransactionStorePort;
import lombok.RequiredArgsConstructor;

import static de.ljw.aachen.lagerbank.service.BalanceCalculator.calculateBalance;
import static de.ljw.aachen.lagerbank.service.WithdrawalValidator.isWithdrawalAllowed;

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

        boolean withdrawalAllowed = isWithdrawalAllowed(balance, amount);
        if (!withdrawalAllowed) {
            throw new WithdrawalNotAllowedException();
        }

        Transaction transaction = Transaction.forWithdrawal(accountId, amount);
        transactionStore.add(transaction);
    }

    @Override
    public void transfer(Money amount, AccountId sourceId, AccountId targetId) throws WithdrawalNotAllowedException {
        Money sourceAccountBalance = calculateBalance(sourceId, transactionStore.getAll(sourceId));

        boolean withdrawalAllowed = isWithdrawalAllowed(sourceAccountBalance, amount);
        if (!withdrawalAllowed) {
            throw new WithdrawalNotAllowedException();
        }

        Transaction transaction = Transaction.forTransfer(sourceId, targetId, amount);
        transactionStore.add(transaction);
    }

}
