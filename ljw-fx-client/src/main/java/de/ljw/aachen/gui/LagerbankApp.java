package de.ljw.aachen.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import java.net.URL;

@SpringBootApplication
public class LagerbankApp extends Application {

    private ConfigurableApplicationContext context;
    private Parent root;

    @Override
    public void init() throws Exception {
        this.context = SpringApplication.run(LagerbankApp.class);

        URL resource = getClass().getResource("/fxml/lagerbank.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(context::getBean);

        this.root = loader.load();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("LJW Lagerbank");
        Scene scene = new Scene(root, 650, 350);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        context.stop();
    }

    public static void main(String[] args){
        launch(LagerbankApp.class, args);
    }
}
