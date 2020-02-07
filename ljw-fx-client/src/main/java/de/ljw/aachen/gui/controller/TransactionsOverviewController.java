package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.event.AccountCreatedEvent;
import de.ljw.aachen.account.management.domain.event.AccountDeletedEvent;
import de.ljw.aachen.account.management.domain.event.AccountUpdatedEvent;
import de.ljw.aachen.account.management.port.in.ReadAccountUseCase;
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

import java.net.URL;
import java.text.MessageFormat;
import java.time.Instant;
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

        tcDescription.setCellValueFactory(transactionStringCellDataFeatures -> {
            var transaction = transactionStringCellDataFeatures.getValue();
            return new SimpleObjectProperty<>(transaction);
        });
        tcDescription.setCellFactory(transactionInstantTableColumn ->
                new DescriptionTableCell(
                        selectedAccountProperty,
                        accountId -> accountListProperty.stream()
                                .filter(account -> accountId.equals(account.getId())).findFirst().orElseThrow()));

        tcAmount.setCellValueFactory(transactionDoubleCellDataFeatures -> new SimpleObjectProperty<Money>(transactionDoubleCellDataFeatures.getValue().getAmount()));
        tcAmount.setCellFactory(transactionMoneyTableColumn -> new MoneyTableCell());

        selectedAccountProperty.addListener((observableValue, previous, selected) -> refreshAccountDetails());
    }

    @EventListener
    void on(AccountDeletedEvent event) {
        // TODO reset receivers
    }

    @EventListener
    void on(MoneyDepositedEvent event) {
        var selectedAccount = selectedAccountProperty.getValue();
        if (selectedAccount == null) return;

        if (selectedAccount.getId().equals(event.getAccountId())) {
            refreshAccountDetails();
        }
    }

    @EventListener
    void on(MoneyWithdrawnEvent event) {
        var selectedAccount = selectedAccountProperty.getValue();
        if (selectedAccount == null) return;

        if (selectedAccount.getId().equals(event.getAccountId())) {
            refreshAccountDetails();
        }
    }

    private void refreshAccountDetails() {
        Account account = selectedAccountProperty.getValue();
        if (account == null) {
            tvTransactions.getItems().clear();
            lblTotalBalance.setText("");
            return;
        }

        var balance = getBalanceUseCase.getBalance(account.getId());
        lblTotalBalance.setText(MessageFormat.format("{0, number, #.##}", balance.getAmount()));
        var transactions = listTransactionsUseCase.listTransactions(account.getId());

        tvTransactions.getItems().setAll(transactions);
    }

}
