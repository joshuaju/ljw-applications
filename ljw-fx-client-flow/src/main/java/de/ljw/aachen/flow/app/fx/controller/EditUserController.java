package de.ljw.aachen.flow.app.fx.controller;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.app.fx.util.BuildNotification;
import de.ljw.aachen.flow.data.Account;
import de.ljw.aachen.flow.logic.accounts.EditAccount;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Slf4j
@Component
@RequiredArgsConstructor
public class EditUserController extends UserDetailController {

    private final FileSystem fileSystem;
    private final AccountStore accountStore;

    private final ObjectProperty<Account> selectedAccountProperty;

    @FXML
    private ResourceBundle resources;

    @FXML
    private void initialize() {
        if (selectedAccountProperty.isNotNull().get()) {
            tfFirstName.setText(selectedAccountProperty.get().getFirstName());
            tfLastName.setText(selectedAccountProperty.get().getLastName());
        }
    }

    @Override
    protected void onAction(String firstName, String lastName) {
        var editedAccount = new Account(selectedAccountProperty.get().getId(), firstName, lastName);

        var editAccount = new EditAccount(fileSystem, accountStore);
        editAccount.process(editedAccount);
        closeStage();
    }

    @Override
    protected void onError(Exception e) {
        String errorMessage = resources.getString("error.edit.account");
        BuildNotification.about(errorMessage, e.getLocalizedMessage(), btnSave.getScene().getWindow())
                .showError();
        log.error(errorMessage, e);
    }
}
