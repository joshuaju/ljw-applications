package de.ljw.aachen.client.controller;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.client.util.BuildNotification;
import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.logic.EditAccount;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class EditUserController extends UserDetailController {

    private final FileSystem fileSystem;
    private final AccountStore accountStore;
    private final ObjectProperty<Account> selectedAccountProperty;

    @Setter
    private Consumer<Account> onEditedAccount;

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
        var accountToBeEdited = selectedAccountProperty.get();
        var editAccount = new EditAccount(fileSystem, accountStore);
        var accountAfterEdit = new Account(accountToBeEdited.getId(), firstName, lastName);
        editAccount.process(accountAfterEdit);
        onEditedAccount.accept(accountToBeEdited);
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
