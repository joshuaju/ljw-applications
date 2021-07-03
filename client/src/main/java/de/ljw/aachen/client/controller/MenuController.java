package de.ljw.aachen.client.controller;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.data.Money;
import de.ljw.aachen.application.data.Transaction;
import de.ljw.aachen.application.logic.CalculateBalance;
import de.ljw.aachen.application.logic.CashUp;
import de.ljw.aachen.application.logic.ComposeFullName;
import de.ljw.aachen.application.logic.ExportAllTransactions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    void onCheckBalance(ActionEvent event)
    {
        var accounts = accountStore.getAccounts();
        var transactions = transactionStore.getTransactions();

        Map<Account, Money> accountBalanceMap = getAccountMoneyMap(accounts, transactions);
        var zeroBalance = new Money(0.00);
        accountBalanceMap.entrySet().removeIf(entry -> entry.getValue().isGreaterThanOrEqual(zeroBalance));

        displayAlert(resources.getString("balance.check.title"), accountBalanceMap);
    }

    @FXML
    @SneakyThrows
    void onListBalance(ActionEvent event)
    {
        URL resource = AccountSelectionController.class.getClassLoader().getResource("fxml/balance_overview.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setResources(resources);
        loader.setController(balanceOverviewController);
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(resources.getString("title.balance.overview"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow());
        stage.show();
    }

    private void displayAlert(String title, Map<Account, Money> accountBalanceMap)
    {
        Node content = getBalanceCheckContentNode(accountBalanceMap);

        var info = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
        info.setTitle(title);
        info.setHeaderText(null);

        info.getDialogPane().setContent(content);
        info.setWidth(200);
        info.setHeight(200);
        info.setResizable(true);
        info.setGraphic(null);
        info.show();
    }

    private Node getBalanceCheckContentNode(Map<Account, Money> accountBalanceMap)
    {
        if (accountBalanceMap.isEmpty())
            return new Label(resources.getString("balance.check.no.accounts.with.negative.balance"));

        GridPane nameBalanceGrid = new GridPane();
        nameBalanceGrid.setHgap(15);
        nameBalanceGrid.setVgap(5);

        var rowIndex = new AtomicInteger();
        accountBalanceMap.entrySet().stream()
                         .sorted(Comparator.comparing(entry -> ComposeFullName.process(entry.getKey())))
                         .forEach(entry -> {
                             nameBalanceGrid.addRow(rowIndex.getAndIncrement(),
                                     new Label(ComposeFullName.process(entry.getKey())),
                                     new Label(entry.getValue().formatWithCurrency()));
                         });
        return new ScrollPane(nameBalanceGrid);
    }

    private Map<Account, Money> getAccountMoneyMap(Collection<Account> accounts, List<Transaction> transactions)
    {
        return accounts.stream()
                       .collect(Collectors.toMap(Function.identity(),
                               account -> CalculateBalance.process(account.getId(), transactions)
                       ));
    }

    @FXML
    @SneakyThrows
    void onImport(ActionEvent event)
    {
        URL resource = AccountSelectionController.class.getClassLoader().getResource("fxml/import_accounts.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setController(importAccountsController);
        loader.setResources(resources);
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(resources.getString("title.import.accounts"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow());
        stage.show();
    }

    @FXML
    void onExportTransactions(ActionEvent event)
    {
        var importFileChooser = new FileChooser();
        importFileChooser.setTitle(resources.getString("select.file"));
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("Comma separated files (CSV)", "*.csv");
        importFileChooser.getExtensionFilters().add(csvFilter);
        File selected = importFileChooser.showSaveDialog(((MenuItem) event.getTarget()).getParentPopup()
                                                                                       .getScene()
                                                                                       .getWindow());

        if (selected == null) return;

        var exporter = new ExportAllTransactions(fs, transactionStore, accountStore);
        exporter.export(selected.toPath());
    }


}
