package de.ljw.aachen.gui;

import de.ljw.aachen.account.management.adapter.out.AccountStoreCSV;
import de.ljw.aachen.account.management.adapter.out.AccountStoreMem;
import de.ljw.aachen.account.management.adapter.out.InitializeAccountStoreFromCSV;
import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.port.in.CreateAccountUseCase;
import de.ljw.aachen.account.management.port.in.ListAccountsUseCase;
import de.ljw.aachen.account.management.port.in.ReadAccountUseCase;
import de.ljw.aachen.account.management.port.in.UpdateAccountUseCase;
import de.ljw.aachen.account.management.port.out.AccountStorePort;
import de.ljw.aachen.account.management.service.AccountService;
import de.ljw.aachen.common.EventPort;
import de.ljw.aachen.gui.controller.*;
import de.ljw.aachen.lagerbank.adapter.out.TransactionStoreMem;
import de.ljw.aachen.lagerbank.adapter.out.csv.InitializeTransactionStoreFromCSV;
import de.ljw.aachen.lagerbank.adapter.out.csv.TransactionStoreCSV;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.port.in.*;
import de.ljw.aachen.lagerbank.port.out.TransactionStorePort;
import de.ljw.aachen.lagerbank.service.BalanceService;
import de.ljw.aachen.lagerbank.service.TransactionService;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import lombok.SneakyThrows;
import org.apache.commons.lang.math.IntRange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class LagerbankAppConfig {

    @PostConstruct
    void setLocale() {
        Locale.setDefault(Locale.GERMAN);
        Locale.setDefault(Locale.Category.FORMAT, Locale.GERMANY);
    }

    @Bean
    LagerbankController lagerbankController(ListAccountsUseCase listAccountsUseCase,
                                            ListProperty<Account> accountListProperty) {
        return new LagerbankController(listAccountsUseCase, accountListProperty);
    }

    @Bean
    CreateUserController createUserController(CreateAccountUseCase createAccountUseCase) {
        return new CreateUserController(createAccountUseCase);
    }

    @Bean
    EditUserController editUserController(UpdateAccountUseCase updateAccountUseCase,
                                          ObjectProperty<Account> selectedAccountProperty) {
        return new EditUserController(updateAccountUseCase, selectedAccountProperty);
    }

    @Bean
    AccountSelectionController accountSelectionController(CreateUserController createUserController,
                                                          EditUserController editUserController,
                                                          ObjectProperty<Account> selectedAccountProperty,
                                                          ListProperty<Account> accountListProperty) {
        return new AccountSelectionController(createUserController, editUserController,
                selectedAccountProperty, accountListProperty);
    }

    @Bean
    MakeTransactionController makeTransactionController(DepositMoneyUseCase depositMoneyUseCase,
                                                        WithdrawMoneyUseCase withdrawMoneyUseCase,
                                                        TransferMoneyUseCase transferMoneyUseCase,
                                                        ObjectProperty<Account> selectedAccountProperty,
                                                        ListProperty<Account> accountListProperty) {
        return new MakeTransactionController(depositMoneyUseCase, withdrawMoneyUseCase, transferMoneyUseCase,
                selectedAccountProperty, accountListProperty);
    }

    @Bean
    TransactionsOverviewController transactionsOverviewController(ListTransactionsUseCase listTransactionsUseCase,
                                                                  GetBalanceUseCase getBalanceUseCase,
                                                                  ReadAccountUseCase readAccountUseCase,
                                                                  ObjectProperty<Account> selectedAccountProperty,
                                                                  ListProperty<Account> accountListProperty) {
        return new TransactionsOverviewController(listTransactionsUseCase, getBalanceUseCase,
                selectedAccountProperty, accountListProperty);
    }

    @Bean
    ObjectProperty<Account> selectedAccountProperty() {
        return new SimpleObjectProperty<>();
    }

    @Bean
    ListProperty<Account> accountListProperty() {
        return new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    /* Account management ******************************************************************************************* */

    @Bean
    AccountService accountService(AccountStorePort accountStorePort, EventPort eventPort) {
        return new AccountService(accountStorePort, eventPort);
    }

    @Bean
    @SneakyThrows
    AccountStorePort accountStorePort() {
        var source = Paths.get("accountStore.csv");
        if (!Files.exists(source)) Files.createFile(source);
        var memoryStore = InitializeAccountStoreFromCSV.init(source);
        return new AccountStoreCSV(source, memoryStore);
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
    @SneakyThrows
    TransactionStorePort transactionStorePort(AccountStorePort accountStorePort) {
        var source = Paths.get("transactionStore.csv");
        if (!Files.exists(source)) Files.createFile(source);
        var memoryStore = InitializeTransactionStoreFromCSV.init(source);
        return new TransactionStoreCSV(source, memoryStore);
    }

    /* Event handling *********************************************************************************************** */

    @Bean
    EventPort eventPort(ApplicationEventPublisher publisher) {
        return publisher::publishEvent;
    }
}
