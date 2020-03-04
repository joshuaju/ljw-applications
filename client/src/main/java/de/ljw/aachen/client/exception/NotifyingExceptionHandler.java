package de.ljw.aachen.client.exception;

import de.ljw.aachen.application.exceptions.LocalizedException;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Window;
import lombok.Value;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;

import java.util.ResourceBundle;

import static javafx.scene.control.Alert.*;

@Slf4j
@UtilityClass
public class NotifyingExceptionHandler {

    public static Thread.UncaughtExceptionHandler asUncaughtExceptionHandler(Window window, ResourceBundle resources) {
        return (thread, throwable) -> {
            Throwable uncaughtException = throwable.getCause().getCause();

            ErrorInformation errorInfo = ErrorInformation.forUnexpectedException(resources);
            logAndShow(errorInfo, window, uncaughtException);
        };
    }

    public static void tryRun(Runnable runnable, Window window, ResourceBundle resources) {
        try {
            runnable.run();
        } catch (Exception e) {
            ErrorInformation errorInfo = null;
            if (e instanceof LocalizedException) {
                errorInfo = ErrorInformation.forLocalizedException(resources, (LocalizedException) e);
            } else {
                errorInfo = ErrorInformation.forUnexpectedException(resources);

            }
            logAndShow(errorInfo, window, e);
        }
    }

    public static void tryRun(Runnable runnable, ActionEvent event, ResourceBundle resources) {
        tryRun(runnable, ((Node) event.getSource()).getScene().getWindow(), resources);
    }

    private static void logAndShow(ErrorInformation errorInformation, Window window, Throwable exception) {
        log.error(errorInformation.getTitle(), exception);
        Notifications.create()
                .title(errorInformation.getTitle())
                .text(errorInformation.getDetail())
                .owner(window)
                .showError();
    }

    @Value
    private static class ErrorInformation {
        private String title;
        private String detail;

        static ErrorInformation forLocalizedException(ResourceBundle resources, LocalizedException localizedException) {
            return new ErrorInformation(
                    resources.getString(localizedException.getTitleKey()),
                    resources.getString(localizedException.getDetailKey())
            );
        }

        static ErrorInformation forUnexpectedException(ResourceBundle resources) {
            return new ErrorInformation(
                    resources.getString("error.title.unexpected"),
                    resources.getString("error.detail.unexpected")
            );
        }
    }

}
