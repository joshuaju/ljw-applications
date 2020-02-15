package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.port.in.UpdateAccountUseCase;
import de.ljw.aachen.gui.util.BuildNotification;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.Initializable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

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
