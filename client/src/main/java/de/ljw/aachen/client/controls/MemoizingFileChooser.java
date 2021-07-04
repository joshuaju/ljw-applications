package de.ljw.aachen.client.controls;

import de.ljw.aachen.client.util.Memoize;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.Optional;

public class MemoizingFileChooser
{

    private final String memoryKey;
    private final FileChooser fileChooser;

    public MemoizingFileChooser(String memoryKey)
    {
        this.memoryKey = memoryKey;
        this.fileChooser = new FileChooser();

        var initialDir = Memoize.restore(memoryKey, this.fileChooser.getInitialDirectory());
        this.fileChooser.setInitialDirectory(initialDir);
    }

    public MemoizingFileChooser setTitle(String title)
    {
        this.fileChooser.setTitle(title);
        return this;
    }

    public MemoizingFileChooser addExtensionFilter(FileChooser.ExtensionFilter filter)
    {
        this.fileChooser.getExtensionFilters().add(filter);
        return this;
    }

    public Optional<File> showOpenDialog(Window ownerWindow)
    {
        var file = this.fileChooser.showOpenDialog(ownerWindow);
        if (file != null) Memoize.store(memoryKey, file.getParentFile());
        return Optional.ofNullable(file);
    }

    public Optional<File> showSaveDialog(Window ownerWindow)
    {
        var file = this.fileChooser.showSaveDialog(ownerWindow);
        if (file != null) Memoize.store(memoryKey, file.getParentFile());
        return Optional.ofNullable(file);
    }

}
