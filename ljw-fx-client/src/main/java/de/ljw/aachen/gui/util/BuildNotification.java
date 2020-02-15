package de.ljw.aachen.gui.util;

import javafx.scene.Node;
import javafx.stage.Stage;
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
