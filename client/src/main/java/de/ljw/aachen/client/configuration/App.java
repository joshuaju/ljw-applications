package de.ljw.aachen.client.configuration;

import de.ljw.aachen.client.util.BuildNotification;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
@Configuration
@ComponentScan(basePackages = {"de.ljw.aachen.client.controller", "de.ljw.aachen.client.configuration"})
@Import(Config.class)
public class App extends Application {

    private Parent root;
    private ResourceBundle resources;
    private static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(App.class, args);
        launch();
    }

    @Override
    public void init() throws Exception {
        resources = ResourceBundle.getBundle("Bundle", Locale.getDefault());
        URL resource = getClass().getResource("/fxml/lagerbank.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(applicationContext::getBean);
        loader.setResources(resources);
        this.root = loader.load();
    }

    @Override
    public void start(Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            Throwable uncaughtException = throwable.getCause().getCause();
            log.error("Uncaught exception", uncaughtException);
            BuildNotification.about(resources.getString("unexpected.exception"), uncaughtException.getMessage(), stage)
                    .showError();
        });
        stage.setTitle(resources.getString("app.name"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        applicationContext.stop();
        super.stop();
    }

}
