package de.ljw.aachen.gui;

import de.ljw.aachen.account.management.adapter.out.AccountStoreMem;
import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.port.in.CreateAccountUseCase;
import de.ljw.aachen.account.management.port.in.ListAccountsUseCase;
import de.ljw.aachen.account.management.port.in.ReadAccountUseCase;
import de.ljw.aachen.account.management.port.out.AccountStorePort;
import de.ljw.aachen.account.management.service.AccountService;
import de.ljw.aachen.common.EventPort;
import de.ljw.aachen.gui.controller.CreateUserController;
import de.ljw.aachen.gui.controller.LagerbankController;
import de.ljw.aachen.lagerbank.adapter.out.TransactionStoreMem;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.port.in.*;
import de.ljw.aachen.lagerbank.port.out.TransactionStorePort;
import de.ljw.aachen.lagerbank.service.BalanceService;
import de.ljw.aachen.lagerbank.service.TransactionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class LagerbankAppConfig {

    @Bean
    LagerbankController lagerbankController(ApplicationContext applicationContext,
                                            ReadAccountUseCase readAccountUseCase,
                                            ListAccountsUseCase listAccountsUseCase,
                                            DepositMoneyUseCase depositMoneyUseCase,
                                            WithdrawMoneyUseCase withdrawMoneyUseCase,
                                            TransferMoneyUseCase transferMoneyUseCase,
                                            ListTransactionsUseCase listTransactionsUseCase,
                                            GetBalanceUseCase getBalanceUseCase) {
        return new LagerbankController(
                applicationContext,
                readAccountUseCase,
                listAccountsUseCase,
                depositMoneyUseCase,
                withdrawMoneyUseCase,
                transferMoneyUseCase,
                listTransactionsUseCase,
                getBalanceUseCase);
    }

    @Bean
    CreateUserController createUserController(CreateAccountUseCase createAccountUseCase) {
        return new CreateUserController(createAccountUseCase);
    }

    /* Account management ******************************************************************************************* */

    @Bean
    AccountService accountService(AccountStorePort accountStorePort, EventPort eventPort) {
        return new AccountService(accountStorePort, eventPort);
    }

    @Bean
    AccountStorePort accountStorePort() {
        var accounts = List.of(Account.createFor("Peter", "Peterson"), Account.createFor("Julia", "Juliette"));
        return new AccountStoreMem(accounts);
    }

    /* Lagerbank **************************************************************************************************** */

    @Bean
    BalanceService balanceService(TransactionStorePort transactionStorePort) {
        return new BalanceService(transactionStorePort);
    }

    @Bean
    TransactionService transactionService(TransactionStorePort transactionStorePort, EventPort eventPort) {
        return new TransactionService(transactionStorePort, eventPort);
    }

    @Bean
    TransactionStorePort transactionStorePort(AccountStorePort accountStorePort) {

        List<Account> accounts = new ArrayList<Account>(accountStorePort.readAll());
        var first = accounts.get(0);
        var second = accounts.get(1);

        return new TransactionStoreMem(List.of(
                Transaction.forDeposit(first.getId(), Money.of(10)),
                Transaction.forWithdrawal(first.getId(), Money.of(1)),
                Transaction.forDeposit(first.getId(), Money.of(1)),
                Transaction.forDeposit(first.getId(), Money.of(5)),
                Transaction.forDeposit(first.getId(), Money.of(2.5)),
                Transaction.forTransfer(first.getId(), second.getId(), Money.of(5.25))
        ));
    }

    /* Event handling *********************************************************************************************** */

    @Bean
    EventPort eventPort(ApplicationEventPublisher publisher) {
        return publisher::publishEvent;
    }
}
