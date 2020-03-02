package de.ljw.aachen.client.controller;

import de.ljw.aachen.client.util.NotifyingExceptionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.ResourceBundle;

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

    ;

    @FXML
    void onSave(ActionEvent event) {
        var firstName = prepareName(tfFirstName.getText());
        var lastName = prepareName(tfLastName.getText());
        NotifyingExceptionHandler
                .tryRun(() -> {
                            onApply(firstName, lastName);
                            closeStage();
                        },
                        ((Node) event.getSource()).getScene().getWindow(),
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
