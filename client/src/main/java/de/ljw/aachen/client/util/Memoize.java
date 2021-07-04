package de.ljw.aachen.client.util;

import javafx.stage.Stage;

import java.io.File;
import java.util.prefs.Preferences;

public class Memoize
{

    private static final String BASE = "/ljw";

    public static void stagePosition(Stage stage, String name)
    {
        Preferences pref = prefs(name);

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

    public static File restore(String name, File defaultFile)
    {
        String path = prefs(name).get("absolutePath", "not_set");
        if (path.equals("not_set")) return defaultFile;
        return new File(path);
    }

    public static void store(String name, File file)
    {
        if (file == null) return;
        Preferences pref = prefs(name);
        pref.put("absolutePath", file.getAbsolutePath());

    }

    private static Preferences prefs(String name)
    {
        var pref = Preferences.userRoot();
        if (name.startsWith("/")) return pref.node(BASE + name);
        else return pref.node(BASE + "/" + name);
    }
}
