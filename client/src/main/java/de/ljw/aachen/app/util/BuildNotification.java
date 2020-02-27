package de.ljw.aachen.app.util;

import javafx.stage.Window;
import org.controlsfx.control.Notifications;

public class BuildNotification {

    public static Notifications about(String theTitle, String theText, Window theOwner) {
        return Notifications.create()
                .owner(theOwner)
                .title(theTitle)
                .text(theText);
    }
}
