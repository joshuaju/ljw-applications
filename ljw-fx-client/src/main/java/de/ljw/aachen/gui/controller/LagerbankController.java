package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.event.AccountCreatedEvent;
import de.ljw.aachen.account.management.domain.event.AccountDeletedEvent;
import de.ljw.aachen.account.management.domain.event.AccountUpdatedEvent;
import de.ljw.aachen.account.management.port.in.ListAccountsUseCase;
import de.ljw.aachen.gui.converter.AccountStringConverter;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.domain.event.MoneyDepositedEvent;
import de.ljw.aachen.lagerbank.domain.event.MoneyWithdrawnEvent;
import de.ljw.aachen.lagerbank.port.in.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import java.net.URL;
import java.time.Instant;
import java.util.ResourceBundle;
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
public class LagerbankController implements Initializable {

    @FXML
    private ToggleGroup tgTransaction;
    @FXML
    private RadioButton rbDeposit;
    @FXML
    private RadioButton rbWithdraw;
    @FXML
    private RadioButton rbTransfer;

    @FXML
    private ListView<Account> lvAccounts;

    @FXML
    private TableView<Transaction> tvTransactions;

    @FXML
    private TableColumn<Transaction, Instant> tcDate;

    @FXML
    private TableColumn<Transaction, String> tcDescription;

    @FXML
    private TableColumn<Transaction, Double> tcAmount;

    @FXML
    private TableColumn<Transaction, Double> tcBalance;


    @FXML
    private TextField tfAmount;

    @FXML
    private ComboBox<Account> cbReceivers;


    private final ApplicationContext applicationContext;

    private final ListAccountsUseCase listAccountsUseCase;
    private final DepositMoneyUseCase depositMoneyUseCase;
    private final WithdrawMoneyUseCase withdrawMoneyUseCase;
    private final TransferMoneyUseCase transferMoneyUseCase;
    private final ListTransactionsUseCase listTransactionsUseCase;

    private ObjectProperty<Account> selectedAccountProperty;
    private ObjectProperty<Account> selectedReceiverProperty;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lvAccounts.setCellFactory(accountListView -> new ListCell<Account>() {
            private AccountStringConverter accountStringConverter = new AccountStringConverter();

            @Override
            protected void updateItem(Account account, boolean b) {
                super.updateItem(account, b);
                if (account != null) {
                    setText(accountStringConverter.toString(account));
                }
            }
        });

        // TODO implement proper CellFactory or CellValueFactory for columns
        tcDate.setCellValueFactory(new PropertyValueFactory<>("time"));
        tcDescription.setCellValueFactory(new PropertyValueFactory<>("source"));
        tcAmount.setCellValueFactory(new PropertyValueFactory<>("target"));
        tcBalance.setCellValueFactory(new PropertyValueFactory<>("amount"));

        cbReceivers.setConverter(new AccountStringConverter());
        cbReceivers.disableProperty().bind(rbTransfer.selectedProperty().not());

        selectedAccountProperty = new SimpleObjectProperty<>();
        selectedAccountProperty.bind(lvAccounts.getSelectionModel().selectedItemProperty());

        selectedReceiverProperty = new SimpleObjectProperty<Account>();
        selectedReceiverProperty.bind(cbReceivers.getSelectionModel().selectedItemProperty());

        selectedAccountProperty.addListener((observableValue, previous, selected) -> {
            refreshAccountDetails();
            refreshReceivers();
        });

        refreshAccounts();
    }

    @FXML
    void onApply(ActionEvent event) {
        var selectedAccount = selectedAccountProperty.getValue();
        if (selectedAccount == null) {
            Notifications.create().owner(((Node) event.getSource()).getScene().getWindow()).darkStyle()
                    .title("Invalid input").text("No account selected")
                    .showError();
            log.error("no account selected");
            return;
        }

        double amount = 0.0;
        try {
            amount = Double.parseDouble(tfAmount.getText());
        } catch (NumberFormatException e) {
            Notifications.create().owner(((Node) event.getSource()).getScene().getWindow()).darkStyle()
                    .title("Invalid input").text("Amount needs to be a decimal number")
                    .showError();
            log.error("amount is not a double", e);
            return;
        }

        if (rbDeposit.isSelected()) {
            log.info("deposit {} to {}", amount, selectedAccount);
            Notifications.create().owner(((Node) event.getSource()).getScene().getWindow()).darkStyle()
                    .title("Deposit successful")
                    .showConfirm();
            depositMoneyUseCase.deposit(Money.of(amount), selectedAccount.getId());
        } else if (rbWithdraw.isSelected()) {
            try {
                withdrawMoneyUseCase.withdraw(Money.of(amount), selectedAccount.getId());
                Notifications.create().owner(((Node) event.getSource()).getScene().getWindow()).darkStyle()
                        .title("Withdrawal successful")
                        .showConfirm();
                resetView();
            } catch (WithdrawalNotAllowedException e) { // TODO code clone
                Notifications.create().owner(((Node) event.getSource()).getScene().getWindow()).darkStyle()
                        .title("Invalid transaction").text("Withdrawal not allowed")
                        .showError();
                log.error("Withdrawal not allowed", e);
                return;
            }
        } else if (rbTransfer.isSelected()) {
            var selectedReceiver = selectedReceiverProperty.getValue();
            if (selectedReceiver == null) {
                Notifications.create().owner(((Node) event.getSource()).getScene().getWindow()).darkStyle()
                        .title("Invalid input").text("No receiver selected")
                        .showError();
                log.error("no receiver selected");
                return;
            }

            try {
                transferMoneyUseCase.transfer(Money.of(amount), selectedAccount.getId(), selectedReceiver.getId());
                Notifications.create().owner(((Node) event.getSource()).getScene().getWindow()).darkStyle()
                        .title("Transfer successful")
                        .showConfirm();
            } catch (WithdrawalNotAllowedException e) { // TODO code clone
                Notifications.create().owner(((Node) event.getSource()).getScene().getWindow()).darkStyle()
                        .title("Invalid transaction").text("Withdrawal not allowed")
                        .showError();
                log.error("Withdrawal not allowed", e);
                return;
            }
        } else {
            Notifications.create().owner(((Node) event.getSource()).getScene().getWindow()).darkStyle()
                    .title("Invalid input").text("No transaction type selected")
                    .showError();
            log.error("no transaction type selected");
            return;
        }
        resetView();
    }

    @FXML
    @SneakyThrows
    void onCreateAccount(ActionEvent event) {
        URL resource = LagerbankController.class.getClassLoader().getResource("fxml/create_user.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(applicationContext::getBean);
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Create new account");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.show();
    }

    @FXML
    void onReset(ActionEvent event) {
        resetView();
    }

    @EventListener
    void on(AccountCreatedEvent event) {
        refreshAccounts();
        refreshReceivers();
    }

    @EventListener()
    void on(AccountDeletedEvent event) {
        refreshAccounts();
        refreshReceivers();
    }

    @EventListener
    void on(AccountUpdatedEvent event) {
        refreshAccounts();
        refreshReceivers();
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

    private void refreshAccounts() {
        var accounts = listAccountsUseCase.listAccounts();
        lvAccounts.getItems().setAll(accounts);
    }

    private void refreshAccountDetails() {
        Account account = selectedAccountProperty.getValue();
        if (account == null) {
            tvTransactions.getItems().clear();
            return;
        }

        var transactions = listTransactionsUseCase.listTransactions(account.getId());
        tvTransactions.getItems().setAll(transactions);
    }

    private void refreshReceivers() {
        var selectedAccount = selectedAccountProperty.getValue();
        Predicate<Account> notSelected = account -> (selectedAccountProperty != null) && !account.equals(selectedAccount);
        cbReceivers.getItems().setAll(lvAccounts.getItems().filtered(notSelected));
    }

    private void resetView() {
        lvAccounts.getSelectionModel().clearSelection();
        tvTransactions.getItems().clear();
        cbReceivers.getSelectionModel().clearSelection();

        tgTransaction.getToggles().forEach(toggle -> toggle.setSelected(false));
        tfAmount.clear();
    }

}
