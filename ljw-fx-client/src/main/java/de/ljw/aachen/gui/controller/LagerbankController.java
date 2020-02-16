package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.event.AccountCreatedEvent;
import de.ljw.aachen.account.management.domain.event.AccountDeletedEvent;
import de.ljw.aachen.account.management.domain.event.AccountUpdatedEvent;
import de.ljw.aachen.account.management.port.in.ListAccountsUseCase;
import javafx.beans.property.ListProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
@RequiredArgsConstructor
public class LagerbankController {

    private final ListAccountsUseCase listAccountsUseCase;

    private final ListProperty<Account> accountListProperty;

    @FXML
    private void initialize() {
        updateAccountList();
    }

    @EventListener(classes = {AccountCreatedEvent.class, AccountDeletedEvent.class, AccountUpdatedEvent.class})
    void updateAccountList() {
        var accounts = listAccountsUseCase.listAccounts();
        accountListProperty.setAll(accounts);
    }

}
