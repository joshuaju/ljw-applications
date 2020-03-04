package de.ljw.aachen.client.controller;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.logic.CreateAccount;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;



@Slf4j
@Component
@RequiredArgsConstructor
public class CreateUserController extends UserDetailController {

    private final FileSystem fileSystem;
    private final AccountStore accountStore;

    @FXML
    private ResourceBundle resources;

    @Override
    protected void onApply(String firstName, String lastName) {
        var account = new Account(null, firstName, lastName);
        var createAccount = new CreateAccount(fileSystem, accountStore);
        createAccount.process(account);
    }

}
