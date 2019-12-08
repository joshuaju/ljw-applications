package de.ljw.aachen.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class HelloWorldController {

    @FXML
    private TextField tfMessage;

    @FXML
    void print(ActionEvent event) {
        String msg = tfMessage.getText();
        System.out.println("Message: " + msg);

    }

}