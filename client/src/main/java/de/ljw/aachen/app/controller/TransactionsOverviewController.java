package de.ljw.aachen.app.controller;

import de.ljw.aachen.app.controls.InstantTableCell;
import de.ljw.aachen.app.controls.MoneyTableCell;
import data.Account;
import data.Money;
import data.Transaction;
import de.ljw.aachen.app.controls.DescriptionTableCell;
import logic.CalculateBalance;
import logic.GetRelevantTransactions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    @FXML
    private TableView<Transaction> tvTransactions;

    @FXML
    private TableColumn<Transaction, Instant> tcDate;

    @FXML
    private TableColumn<Transaction, Transaction> tcDescription;

    @FXML
    private TableColumn<Transaction, Money> tcAmount;

    @FXML
    private Label lblTotalBalance;

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

        tcAmount.setCellValueFactory(transactionDoubleCellDataFeatures -> new SimpleObjectProperty<Money>(transactionDoubleCellDataFeatures.getValue().getAmount()));
        tcAmount.setCellFactory(transactionMoneyTableColumn -> new MoneyTableCell());
        selectedAccountProperty.addListener((observableValue, previous, selected) -> refresh());
        transactionListProperty.addListener((ListChangeListener<Transaction>) change -> refresh());
        clear();
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
