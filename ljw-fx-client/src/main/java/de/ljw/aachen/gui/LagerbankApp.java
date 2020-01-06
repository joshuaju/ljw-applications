package de.ljw.aachen.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.lang.Validate;

import java.net.URL;

public class HelloWorld extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        URL resource = getClass().getResource("/fxml/lagerbank.fxml");
        Validate.notNull(resource);

        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root, 300, 275);

        stage.setTitle("LJW Lagerbank");
        stage.setScene(scene);
        stage.show();
    }
}
