package de.ljw.aachen.client.controller;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.application.logic.ImportAccountFromFile;
import de.ljw.aachen.client.controls.MemoizingFileChooser;
import de.ljw.aachen.client.exception.NotifyingExceptionHandler;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ResourceBundle;


@Component
@RequiredArgsConstructor
public class ImportAccountsController
{

    private final FileSystem fs;
    private final AccountStore accountStore;
    private final TransactionStore transactionStore;

    @FXML
    ResourceBundle resources;

    @FXML
    private TextField tfDescription;

    @FXML
    private TextArea taResults;

    @FXML
    private Button btnApply;

    @FXML
    private Button btnSelectFile;


    private SimpleObjectProperty<File> importFile = new SimpleObjectProperty<>();
    private String btnSelectFileDefaultText;

    @FXML
    private void initialize()
    {
        btnApply.disableProperty().bind(importFile.isNull());
        btnSelectFileDefaultText = btnSelectFile.getText();
    }

    @FXML
    public void onSelectFile(ActionEvent event)
    {
        Window ownerWindow = ((Node) event.getTarget()).getScene().getWindow();

        var fileChooser = new MemoizingFileChooser("file-chooser/import-accounts");
        var selected = fileChooser.setTitle(resources.getString("select.file"))
                                  .addExtensionFilter(new FileChooser.ExtensionFilter("Comma separated files (CSV)", "*.csv"))
                                  .showOpenDialog(ownerWindow);
        selected.ifPresentOrElse(
                file -> {
                    importFile.set(file);
                    btnSelectFile.setText(file.getAbsolutePath());
                },
                () -> {
                    importFile.set(null);
                    btnSelectFile.setText(btnSelectFileDefaultText);
                });
    }

    @FXML
    public void onApply(ActionEvent event)
    {
        NotifyingExceptionHandler.tryRun(this::importAccounts, event, resources);
    }

    private void importAccounts()
    {
        taResults.clear();

        var fileImporter = new ImportAccountFromFile(fs, accountStore, transactionStore, msg -> taResults.appendText(msg + "\n"));
        var file = importFile.get();

        var description = tfDescription.getText();
        if (description.isBlank()) description = resources.getString("import.default.description");

        fileImporter.importFile(file.toPath(), description);
        taResults.appendText("\nDone.");
    }

    @FXML
    public void onCancel(ActionEvent event)
    {
        closeStage();
    }

    private void closeStage()
    {
        Stage stage = (Stage) tfDescription.getScene().getWindow();
        importFile.set(null);
        stage.close();
    }

}
