package de.ljw.aachen.gui;

import de.ljw.aachen.account.management.adapter.out.AccountStoreMem;
import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.port.in.CreateAccountUseCase;
import de.ljw.aachen.account.management.port.in.ListAccountsUseCase;
import de.ljw.aachen.account.management.port.out.AccountStorePort;
import de.ljw.aachen.account.management.service.AccountService;
import de.ljw.aachen.common.EventPort;
import de.ljw.aachen.gui.controller.CreateUserController;
import de.ljw.aachen.gui.controller.LagerbankController;
import de.ljw.aachen.lagerbank.adapter.out.TransactionStoreMem;
import de.ljw.aachen.lagerbank.port.in.DepositMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.ListTransactionsUseCase;
import de.ljw.aachen.lagerbank.port.in.TransferMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.WithdrawMoneyUseCase;
import de.ljw.aachen.lagerbank.port.out.TransactionStorePort;
import de.ljw.aachen.lagerbank.service.BalanceService;
import de.ljw.aachen.lagerbank.service.TransactionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LagerbankAppConfig {

    @Bean
    LagerbankController lagerbankController(ApplicationContext applicationContext,
                                            ListAccountsUseCase listAccountsUseCase,
                                            DepositMoneyUseCase depositMoneyUseCase,
                                            WithdrawMoneyUseCase withdrawMoneyUseCase,
                                            TransferMoneyUseCase transferMoneyUseCase,
                                            ListTransactionsUseCase listTransactionsUseCase) {
        return new LagerbankController(
                applicationContext,
                listAccountsUseCase,
                depositMoneyUseCase,
                withdrawMoneyUseCase,
                transferMoneyUseCase,
                listTransactionsUseCase);
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
    TransactionStorePort transactionStorePort() {
        return new TransactionStoreMem();
    }

    /* Event handling *********************************************************************************************** */

    @Bean
    EventPort eventPort(ApplicationEventPublisher publisher) {
        return publisher::publishEvent;
    }
}
