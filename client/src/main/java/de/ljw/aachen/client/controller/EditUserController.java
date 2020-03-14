package de.ljw.aachen.client.controller;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
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
    @Override
    protected void initialize() {
        super.initialize();
        if (selectedAccountProperty.isNotNull().get()) {
            tfFirstName.setText(selectedAccountProperty.get().getFirstName());
            tfLastName.setText(selectedAccountProperty.get().getLastName());
        }
    }

    @Override
    protected void onApply(String firstName, String lastName) {
        var editAccount = new EditAccount(fileSystem, accountStore);
        var selectAccount = selectedAccountProperty.get();
        Account editedAccount = new Account(selectAccount.getId(), firstName, lastName);
        editAccount.process(editedAccount);
        onEditedAccount.accept(editedAccount);
    }


}
