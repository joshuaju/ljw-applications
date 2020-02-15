package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.gui.cell.table.DescriptionTableCell;
import de.ljw.aachen.gui.cell.table.InstantTableCell;
import de.ljw.aachen.gui.cell.table.MoneyTableCell;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.domain.event.MoneyDepositedEvent;
import de.ljw.aachen.lagerbank.domain.event.MoneyWithdrawnEvent;
import de.ljw.aachen.lagerbank.port.in.GetBalanceUseCase;
import de.ljw.aachen.lagerbank.port.in.ListTransactionsUseCase;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

import java.math.RoundingMode;
import java.net.URL;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
@RequiredArgsConstructor
public class TransactionsOverviewController implements Initializable {

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

    private final ListTransactionsUseCase listTransactionsUseCase;
    private final GetBalanceUseCase getBalanceUseCase;

    private final ObjectProperty<Account> selectedAccountProperty;
    private final ListProperty<Account> accountListProperty;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcDate.setCellValueFactory(new PropertyValueFactory<>("time"));
        tcDate.setCellFactory(transactionInstantTableColumn -> new InstantTableCell());

        tcDescription.setCellValueFactory(transactionStringCellDataFeatures ->
                new SimpleObjectProperty<>(transactionStringCellDataFeatures.getValue()));
        tcDescription.setCellFactory(transactionInstantTableColumn ->
                new DescriptionTableCell(selectedAccountProperty, accountListProperty));

        tcAmount.setCellValueFactory(transactionDoubleCellDataFeatures -> new SimpleObjectProperty<Money>(transactionDoubleCellDataFeatures.getValue().getAmount()));
        tcAmount.setCellFactory(transactionMoneyTableColumn -> new MoneyTableCell());

        selectedAccountProperty.addListener((observableValue, previous, selected) -> refresh());
    }

    @EventListener
    void on(MoneyDepositedEvent event) {
        var selectedAccount = selectedAccountProperty.getValue();
        if (isShowing(event.getAccountId())) update(selectedAccount);
    }

    @EventListener
    void on(MoneyWithdrawnEvent event) {
        var selectedAccount = selectedAccountProperty.getValue();
        if (isShowing(event.getAccountId())) update(selectedAccount);
    }

    private boolean isShowing(AccountId accountId) {
        var selectedAccount = selectedAccountProperty.getValue();
        if (selectedAccount == null) return false;
        else return selectedAccount.getId().equals(accountId);
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
        var balance = getBalanceUseCase.getBalance(account.getId());
        lblTotalBalance.setText(MessageFormat.format("{0, number, #0.00} {1}",
                balance.getAmount(),
                Currency.getInstance(Locale.getDefault()).getSymbol()));
        var transactions = listTransactionsUseCase.listTransactions(account.getId());
        tvTransactions.getItems().setAll(transactions);
    }

}
