package de.ljw.aachen.client.configuration;

import de.ljw.aachen.client.util.BuildNotification;
import javafx.stage.Window;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ResourceBundle;

@Slf4j
@RequiredArgsConstructor
public class NotifyingExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Window window;
    private final ResourceBundle resources;

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Throwable uncaughtException = throwable.getCause().getCause();
        log.error("Uncaught exception", uncaughtException);
        BuildNotification.about(resources.getString("unexpected.exception"), uncaughtException.getMessage(), window)
                .showError();
    }
}
