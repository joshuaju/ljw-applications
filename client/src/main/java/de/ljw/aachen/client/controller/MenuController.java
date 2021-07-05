package de.ljw.aachen.client.controller;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.application.logic.CashUp;
import de.ljw.aachen.application.logic.ExportAllTransactions;
import de.ljw.aachen.client.FXMLRegister;
import de.ljw.aachen.client.controls.MemoizingFileChooser;
import de.ljw.aachen.client.util.Memoize;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

import static java.time.format.DateTimeFormatter.ofPattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuController
{

    private final FileSystem fs;
    private final AccountStore accountStore;
    private final TransactionStore transactionStore;
    private final ImportAccountsController importAccountsController;
    private final BalanceOverviewController balanceOverviewController;

    @FXML
    private ResourceBundle resources;

    @FXML
    void onCashUp(ActionEvent event)
    {
        var allTransactions = transactionStore.getTransactions();
        var availableCash = CashUp.cashUp(allTransactions);

        String content = MessageFormat.format(resources.getString("cash.up.info"), availableCash.formatWithCurrency());
        var info = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
        info.setTitle(resources.getString("cash.up.title"));
        info.setHeaderText(null);
        info.setGraphic(null);
        info.show();
    }

    @FXML
    @SneakyThrows
    void onListBalance(ActionEvent event)
    {
        URL resource = AccountSelectionController.class.getClassLoader().getResource("fxml/balance_overview.fxml");
        FXMLLoader loader = new FXMLLoader(FXMLRegister.BALANCE_OVERVIEW.getResource());
        loader.setResources(resources);
        loader.setController(balanceOverviewController);
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(resources.getString("title.balance.overview"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow());
        stage.show();

        Memoize.stagePosition(stage, FXMLRegister.BALANCE_OVERVIEW.getFileName());
    }

    @FXML
    @SneakyThrows
    void onImport(ActionEvent event)
    {
        FXMLLoader loader = new FXMLLoader(FXMLRegister.IMPORT_ACCOUNTS.getResource());
        loader.setController(importAccountsController);
        loader.setResources(resources);
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(resources.getString("title.import.accounts"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow());
        stage.show();

        Memoize.stagePosition(stage, FXMLRegister.IMPORT_ACCOUNTS.getFileName());
    }

    @FXML
    void onExportTransactions(ActionEvent event)
    {
        Window ownerWindow = ((MenuItem) event.getTarget()).getParentPopup().getScene().getWindow();
        var date = ofPattern("yyyyMMdd_HHmm").withLocale(Locale.GERMAN).withZone(ZoneId.systemDefault());

        var selected = new MemoizingFileChooser("file-chooser/export-transactions")
                .setTitle(resources.getString("select.file"))
                .addExtensionFilter(new ExtensionFilter("Comma separated files (CSV)", "*.csv"))
                .setInitialFileName(String.format("%s_export.csv", date.format(Instant.now())))
                .showSaveDialog(ownerWindow);
        selected.ifPresent(file -> {
            var exporter = new ExportAllTransactions(fs, transactionStore, accountStore);
            exporter.export(file.toPath());
        });
    }


}
