package de.ljw.aachen.client.controller;

import de.ljw.aachen.client.exception.NotifyingExceptionHandler;
import de.ljw.aachen.client.exception.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.util.ResourceBundle;

import static de.ljw.aachen.client.exception.NotifyingExceptionHandler.tryRun;

@Slf4j
abstract class UserDetailController {

    @FXML
    protected TextField tfFirstName;

    @FXML
    protected TextField tfLastName;

    @FXML
    protected Button btnSave;

    @FXML
    protected Button btnCancel;

    @FXML
    protected ResourceBundle resources;

    protected abstract void onApply(String firstName, String lastName);

    private ValidationSupport namesValidation;

    @FXML
    protected void initialize() {
        namesValidation = new ValidationSupport();
        namesValidation.registerValidator(tfFirstName, true,
                (control, o) -> ValidationResult.fromErrorIf(tfFirstName, resources.getString("validation.not.empty.or.blank"), tfFirstName.getText().isBlank()));
        namesValidation.registerValidator(tfLastName, true,
                (control, o) -> ValidationResult.fromErrorIf(tfLastName, resources.getString("validation.not.empty.or.blank"), tfLastName.getText().isBlank()));
        namesValidation.setErrorDecorationEnabled(true);
    }

    @FXML
    void onSave(ActionEvent event) {
        tryRun(() -> {
                    if (namesValidation.isInvalid())
                        throw new ValidationException("error.detail.validation.first.or.last.name.missing");

                    var firstName = prepareName(tfFirstName.getText());
                    var lastName = prepareName(tfLastName.getText());
                    onApply(firstName, lastName);
                    closeStage();
                },
                event,
                resources);
    }

    @FXML
    void onCancel(ActionEvent event) {
        closeStage();
    }

    private String prepareName(String name) {
        return name.trim();
    }

    private void closeStage() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
