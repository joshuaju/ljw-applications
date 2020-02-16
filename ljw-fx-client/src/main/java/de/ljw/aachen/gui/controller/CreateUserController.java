package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.port.in.CreateAccountUseCase;
import de.ljw.aachen.gui.util.BuildNotification;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ResourceBundle;

import static de.ljw.aachen.account.management.port.in.CreateAccountUseCase.*;

@Slf4j
@RequiredArgsConstructor
public class CreateUserController extends UserDetailController {

    private final CreateAccountUseCase createAccountUseCase;

    @FXML
    private ResourceBundle resources;


    @Override
    protected void onAction(String firstName, String lastName) {
        var cmd = new CreateAccountCommand(firstName, lastName);
        var accountId = createAccountUseCase.createAccount(cmd);
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
