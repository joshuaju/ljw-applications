package de.ljw.aachen.flow.app;

import de.ljw.aachen.flow.adapter.*;
import de.ljw.aachen.flow.app.fx.controller.*;
import de.ljw.aachen.flow.data.Account;
import de.ljw.aachen.flow.data.Transaction;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Configuration
public class LJWClientConfig {

    private ObservableList<Account> accounts = FXCollections.observableArrayList();
    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    @PostConstruct
    void setLocale() {
        Locale.setDefault(Locale.GERMAN);
        Locale.setDefault(Locale.Category.FORMAT, Locale.GERMANY);
    }

    @Bean
    ObjectProperty<Account> selectedAccountProperty() {
        return new SimpleObjectProperty<>();
    }

    @Bean
    ListProperty<Account> accountListProperty() {
        return new SimpleListProperty<>(accounts);
    }

    @Bean
    ListProperty<Transaction> transactionListProperty(){
        return new SimpleListProperty<>(transactions);
    }

    @Bean
    FileSystem fileSystem() {
        return new FileSystemImpl();
    }

    @Bean
    TransactionStore transactionStore() {
        return new TransactionStoreImpl(transactions);
    }

    @Bean
    AccountStore accountStore() {
        return new AccountStoreImpl(accounts);
    }
}
