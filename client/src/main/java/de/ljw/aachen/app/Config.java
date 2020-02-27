package de.ljw.aachen.app;

import adapter.*;
import adapter.FileSystemImpl;
import adapter.TransactionStoreImpl;
import data.Account;
import data.Transaction;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Configuration
class Config {

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
    ReadOnlyListProperty<Account> accountListProperty() {
        return new SimpleListProperty<>(accounts);
    }

    @Bean
    ReadOnlyListProperty<Transaction> transactionListProperty() {
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
