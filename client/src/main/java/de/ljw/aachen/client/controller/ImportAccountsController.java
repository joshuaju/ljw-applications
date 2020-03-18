package de.ljw.aachen.client.controller;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.application.logic.ImportAccountFromFile;
import de.ljw.aachen.client.exception.NotifyingExceptionHandler;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.RequiredArgsConstructor;
import org.controlsfx.control.Notifications;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ResourceBundle;


@Component
@RequiredArgsConstructor
public class ImportAccountsController {

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
    private void initialize() {
        btnApply.disableProperty().bind(importFile.isNull());
        btnSelectFileDefaultText = btnSelectFile.getText();

        importFile.addListener((observableValue, prev, now) -> {
            if (now == null) btnSelectFile.setText(btnSelectFileDefaultText);
            else btnSelectFile.setText(now.getAbsolutePath());
        });
    }

    @FXML
    public void onSelectFile(ActionEvent event) {
        var importFileChooser = new FileChooser();
        importFileChooser.setTitle(resources.getString("select.import.file"));
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("Comma separated files (CSV)", "*.csv");
        importFileChooser.getExtensionFilters().add(csvFilter);
        File selected = importFileChooser.showOpenDialog(((Node) event.getTarget()).getScene().getWindow());

        importFile.set(selected);
    }

    @FXML
    public void onApply(ActionEvent event) {
        NotifyingExceptionHandler.tryRun(this::importAccounts, event, resources);
    }

    private void importAccounts() {
        taResults.clear();

        var fileImporter = new ImportAccountFromFile(fs, accountStore, transactionStore, msg -> taResults.appendText(msg + "\n"));
        var file = importFile.get();

        var description = tfDescription.getText();
        if (description.isBlank()) description = resources.getString("import.default.description");

        fileImporter.importFile(file.toPath(), description);
        taResults.appendText("\nDone.");
    }

    @FXML
    public void onCancel(ActionEvent event) {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) tfDescription.getScene().getWindow();
        stage.close();
    }

}
