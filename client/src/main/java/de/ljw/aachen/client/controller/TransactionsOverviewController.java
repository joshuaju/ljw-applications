package de.ljw.aachen.client.controller;

import de.ljw.aachen.client.controls.InstantTableCell;
import de.ljw.aachen.client.controls.MoneyTableCell;
import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.data.Money;
import de.ljw.aachen.application.data.Transaction;
import de.ljw.aachen.client.controls.DescriptionTableCell;
import de.ljw.aachen.application.logic.CalculateBalance;
import de.ljw.aachen.application.logic.GetRelevantTransactions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionsOverviewController {

    private final ObjectProperty<Account> selectedAccountProperty;
    private final ReadOnlyListProperty<Account> accountListProperty;
    private final ReadOnlyListProperty<Transaction> transactionListProperty;
    private final MakeTransactionController makeTransactionController;

    @FXML
    private TableView<Transaction> tvTransactions;

    @FXML
    private TableColumn<Transaction, Instant> tcDate;

    @FXML
    private TableColumn<Transaction, Transaction> tcDescription;

    @FXML
    private TableColumn<Transaction, Transaction> tcAmount;

    @FXML
    private Label lblTotalBalance;

    @FXML
    private Button btnMakeTransaction;

    @FXML
    private ResourceBundle resources;

    @FXML
    private void initialize() {
        tcDate.setCellValueFactory(new PropertyValueFactory<>("time"));
        tcDate.setCellFactory(transactionInstantTableColumn -> new InstantTableCell());

        tcDescription.setCellValueFactory(transactionStringCellDataFeatures ->
                new SimpleObjectProperty<>(transactionStringCellDataFeatures.getValue()));
        tcDescription.setCellFactory(transactionInstantTableColumn ->
                new DescriptionTableCell(selectedAccountProperty, accountListProperty, resources));

        tcAmount.setCellValueFactory(transactionStringCellDataFeatures ->
                new SimpleObjectProperty<>(transactionStringCellDataFeatures.getValue()));
        tcAmount.setCellFactory(transactionMoneyTableColumn -> new MoneyTableCell(selectedAccountProperty));
        selectedAccountProperty.addListener((observableValue, previous, selected) -> refresh());
        transactionListProperty.addListener((ListChangeListener<Transaction>) change -> refresh());
        btnMakeTransaction.disableProperty().bind(selectedAccountProperty.isNull());
        clear();
    }

    @FXML
    @SneakyThrows
    private void onMakeTransaction(ActionEvent event) {
        URL resource = AccountSelectionController.class.getClassLoader().getResource("fxml/make_transaction.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(any -> makeTransactionController);
        loader.setResources(resources);
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(resources.getString("make.transaction"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.showAndWait();
    }

    private void refresh() {
        Account account = selectedAccountProperty.getValue();
        if (account == null) clear();
        else update(account);
    }

    private void clear() {
        tvTransactions.getItems().clear();
        lblTotalBalance.setText("-");
    }

    private void update(Account account) {
        var transactions = transactionListProperty.get();
        var relevantTransactions = GetRelevantTransactions.process(account.getId(), transactions);

        tvTransactions.getItems().setAll(relevantTransactions);

        var balance = CalculateBalance.process(account.getId(), relevantTransactions);
        lblTotalBalance.setText(MessageFormat.format("{0} {1}",
                MessageFormat.format("{0,number, #0.00}", balance.getValue()),
                Currency.getInstance(Locale.getDefault(Locale.Category.FORMAT)).getSymbol()));

    }

}
