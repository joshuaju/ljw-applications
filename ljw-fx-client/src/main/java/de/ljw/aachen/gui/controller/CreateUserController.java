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
public class CreateUserController {

    @FXML
    private TextField tfFirstName;

    @FXML
    private TextField tfLastName;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    private final CreateAccountUseCase createAccountUseCase;

    @FXML
    void onSave(ActionEvent event) {
        var firstName = tfFirstName.getText().trim();
        var lastName = tfLastName.getText().trim();

        var cmd = new CreateAccountCommand(firstName, lastName);
        try {
            var accountId = createAccountUseCase.createAccount(cmd);
            log.info("Created Account: {}", accountId);
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        } catch (IllegalArgumentException e){
            BuildNotification.about("Could not create account", null, ((Node) event.getSource()).getScene().getWindow())
                    .showError();
            log.error("Error creating account", e);
        }
    }

    @FXML
    void onCancel(ActionEvent event) {
        log.info("Creating a user was cancelled");
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
