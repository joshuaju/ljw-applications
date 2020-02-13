package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.port.in.CreateAccountUseCase;
import de.ljw.aachen.account.management.port.in.UpdateAccountUseCase;
import de.ljw.aachen.gui.BuildNotification;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

import static de.ljw.aachen.account.management.port.in.CreateAccountUseCase.CreateAccountCommand;

@Slf4j
@RequiredArgsConstructor
public class EditUserController extends UserDetailController implements Initializable {

    private final UpdateAccountUseCase updateAccountUseCase;
    private final ObjectProperty<Account> selectedAccountProperty;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (selectedAccountProperty.isNotNull().get()) {
            tfFirstName.setText(selectedAccountProperty.get().getFirstName());
            tfLastName.setText(selectedAccountProperty.get().getLastName());
        }
    }

    @Override
    protected void onAction(String firstName, String lastName) {
        Account account = selectedAccountProperty.get();
        account.setFirstName(firstName);
        account.setLastName(lastName);

        var cmd = new UpdateAccountUseCase.UpdateAccountCommand(account);
        updateAccountUseCase.updateAccount(cmd);
        closeStage();
    }

    @Override
    protected void onError(Exception e) {
        BuildNotification.about("Could not edit account", null,
                btnSave.getScene().getWindow())
                .showError();
        log.error("Error editing account", e);
    }
}
