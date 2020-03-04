package de.ljw.aachen.client.configuration;

import de.ljw.aachen.client.exception.NotifyingExceptionHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
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
        var application = new SpringApplication(App.class);
        application.setBannerMode(Banner.Mode.OFF);
        applicationContext = application.run(args);
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
        Thread.setDefaultUncaughtExceptionHandler(NotifyingExceptionHandler.asUncaughtExceptionHandler(stage, resources));
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
