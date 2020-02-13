package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.account.management.port.in.CreateAccountUseCase;
import de.ljw.aachen.gui.BuildNotification;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

import static de.ljw.aachen.account.management.port.in.CreateAccountUseCase.*;

@Slf4j
@RequiredArgsConstructor
public class CreateUserController extends UserDetailController {

    private final CreateAccountUseCase createAccountUseCase;


    @Override
    protected void onAction(String firstName, String lastName) {
        var cmd = new CreateAccountCommand(firstName, lastName);
        var accountId = createAccountUseCase.createAccount(cmd);
        closeStage();
    }

    @Override
    protected void onError(Exception e) {
        BuildNotification.about("Could not create account", null,
                btnCancel.getScene().getWindow())
                .showError();
        log.error("Error creating account", e);
    }
}
