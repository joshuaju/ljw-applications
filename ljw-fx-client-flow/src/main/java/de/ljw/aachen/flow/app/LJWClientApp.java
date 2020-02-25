package de.ljw.aachen.flow.app;

import de.ljw.aachen.flow.adapter.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

@SpringBootApplication
public class LJWClientApp extends Application {

    private Parent root;
    private ResourceBundle resources;
private static String[] args;
    public static void main(String[] args) {
        LJWClientApp.args = args;
        launch();
    }

    @Override
    public void init() throws Exception {
        var applicationContext = SpringApplication.run(LJWClientApp.class, args);
        resources = ResourceBundle.getBundle("Bundle", Locale.getDefault());

        URL resource = getClass().getResource("/fxml/lagerbank.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(applicationContext::getBean);
        loader.setResources(resources);
        this.root = loader.load();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle(resources.getString("app.name"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
