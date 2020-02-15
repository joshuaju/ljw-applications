package de.ljw.aachen.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

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

    protected abstract void onAction(String firstName, String lastName);

    protected void onSuccess() {
        closeStage();
    }

    protected abstract void onError(Exception e);

    @FXML
    void onSave(ActionEvent event) {
        var firstName = tfFirstName.getText().trim();
        var lastName = tfLastName.getText().trim();
        try {
            onAction(firstName, lastName);
            onSuccess();
        } catch (Exception e) {
            onError(e);
        }
    }

    @FXML
    void onCancel(ActionEvent event) {
        closeStage();
    }

    void closeStage(){
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
