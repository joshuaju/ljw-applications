package de.ljw.aachen.gui;

import com.sun.javafx.fxml.FXMLLoaderHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

@SpringBootApplication
public class LagerbankApp extends Application {

    private ConfigurableApplicationContext context;
    private Parent root;
    private ResourceBundle resourceBundle;

    @Override
    public void init() throws Exception {
        this.context = SpringApplication.run(LagerbankApp.class);
        this.resourceBundle = ResourceBundle.getBundle("Bundle", Locale.getDefault());

        URL resource = getClass().getResource("/fxml/lagerbank.fxml");
        FXMLLoader loader = new FXMLLoader(resource);

        loader.setControllerFactory(context::getBean);
        loader.setResources(resourceBundle);

        this.root = loader.load();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle(resourceBundle.getString("app.name"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        context.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
