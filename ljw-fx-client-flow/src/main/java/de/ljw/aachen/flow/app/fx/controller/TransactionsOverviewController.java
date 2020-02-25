package de.ljw.aachen.flow.app.fx.controller;

import de.ljw.aachen.flow.adapter.TransactionStore;
import de.ljw.aachen.flow.app.fx.cell.table.DescriptionTableCell;
import de.ljw.aachen.flow.app.fx.cell.table.InstantTableCell;
import de.ljw.aachen.flow.app.fx.cell.table.MoneyTableCell;
import de.ljw.aachen.flow.data.Account;
import de.ljw.aachen.flow.data.Money;
import de.ljw.aachen.flow.data.Transaction;
import de.ljw.aachen.flow.logic.transactions.CalculateBalance;
import de.ljw.aachen.flow.logic.transactions.GetRelevantTransactions;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionsOverviewController {

    private final TransactionStore transactionStore;
    private final ObjectProperty<Account> selectedAccountProperty;
    private final ListProperty<Account> accountListProperty;

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
    private void initialize() {
        tcDate.setCellValueFactory(new PropertyValueFactory<>("time"));
        tcDate.setCellFactory(transactionInstantTableColumn -> new InstantTableCell());

        tcDescription.setCellValueFactory(transactionStringCellDataFeatures ->
                new SimpleObjectProperty<>(transactionStringCellDataFeatures.getValue()));
        tcDescription.setCellFactory(transactionInstantTableColumn ->
                new DescriptionTableCell(selectedAccountProperty, accountListProperty));

        tcAmount.setCellValueFactory(transactionDoubleCellDataFeatures -> new SimpleObjectProperty<Money>(transactionDoubleCellDataFeatures.getValue().getAmount()));
        tcAmount.setCellFactory(transactionMoneyTableColumn -> new MoneyTableCell());
        selectedAccountProperty.addListener((observableValue, previous, selected) -> refresh());
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
        var transactions = transactionStore.getTransactions();
        var relevantTransactions = GetRelevantTransactions.process(account.getId(), transactions);

        tvTransactions.getItems().setAll(relevantTransactions);

        var balance = CalculateBalance.process(account.getId(), relevantTransactions);
        lblTotalBalance.setText(MessageFormat.format("{0, number, #0.00} {1}",
                balance.getValue(),
                Currency.getInstance(Locale.getDefault(Locale.Category.FORMAT)).getSymbol()));

    }

}
