package de.ljw.aachen.flow.app.fx.controller;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.app.fx.util.BuildNotification;
import de.ljw.aachen.flow.data.Account;
import de.ljw.aachen.flow.logic.accounts.CreateAccount;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    protected void onAction(String firstName, String lastName) {
        var account = new Account(null, firstName, lastName);

        var createAccount = new CreateAccount(fileSystem, accountStore);
        createAccount.process(account);

        closeStage();
    }

    @Override
    protected void onError(Exception e) {
        String errorMessage = resources.getString("error.create.account");
        BuildNotification.about(errorMessage, null, btnCancel.getScene().getWindow())
                .showError();
        log.error(errorMessage, e);
    }
}
