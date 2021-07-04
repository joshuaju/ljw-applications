package de.ljw.aachen.client.util;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

public class Memoize
{

    public static void stagePosition(Stage stage, String name)
    {
        Preferences pref = Preferences.userRoot().node(name);

        stage.setX(pref.getDouble("x", stage.getX()));
        stage.setY(pref.getDouble("y", stage.getY()));
        stage.setWidth(pref.getDouble("width", stage.getWidth()));
        stage.setHeight(pref.getDouble("height", stage.getHeight()));

        stage.setOnCloseRequest(event -> {
            pref.putDouble("x", stage.getX());
            pref.putDouble("y", stage.getY());
            pref.putDouble("width", stage.getWidth());
            pref.putDouble("height", stage.getHeight());
        });
    }

}
