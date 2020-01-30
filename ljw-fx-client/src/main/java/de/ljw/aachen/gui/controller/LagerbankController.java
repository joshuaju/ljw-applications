package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.event.AccountCreatedEvent;
import de.ljw.aachen.account.management.domain.event.AccountDeletedEvent;
import de.ljw.aachen.account.management.domain.event.AccountUpdatedEvent;
import de.ljw.aachen.account.management.port.in.ListAccountsUseCase;
import de.ljw.aachen.gui.converter.AccountStringConverter;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.event.MoneyDepositedEvent;
import de.ljw.aachen.lagerbank.domain.event.MoneyWithdrawnEvent;
import de.ljw.aachen.lagerbank.port.in.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import javax.management.Notification;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
public class LagerbankController implements Initializable {

    @FXML
    private ComboBox<Account> cbAccounts;

    @FXML
    private Button btnNewUser;

    @FXML
    private TextField tfBalance;

    @FXML
    private RadioButton rbDeposit;

    @FXML
    private ToggleGroup tgTransaction;

    @FXML
    private RadioButton rbWithdraw;

    @FXML
    private RadioButton rbTransfer;

    @FXML
    private TextField tfAmount;

    @FXML
    private ComboBox<Account> cbReceivers;

    @FXML
    private Button btnApply;

    @FXML
    private Button btnReset;

    private final ApplicationContext applicationContext;

    private final ListAccountsUseCase listAccountsUseCase;
    private final DepositMoneyUseCase depositMoneyUseCase;
    private final WithdrawMoneyUseCase withdrawMoneyUseCase;
    private final TransferMoneyUseCase transferMoneyUseCase;
    private final GetBalanceUseCase getBalanceUseCase;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbAccounts.setConverter(new AccountStringConverter());
        cbReceivers.setConverter(new AccountStringConverter());
        cbReceivers.disableProperty().bind(rbTransfer.selectedProperty().not());
        refreshAccounts();
    }

    @FXML
    void onAccountSelected(ActionEvent event) {
        refreshReceivers();
        refreshAccountDetails();
    }

    @FXML
    void onApply(ActionEvent event) {
        if (cbAccounts.getSelectionModel().isEmpty()) {
            Notifications.create().owner(((Node) event.getSource()).getScene().getWindow()).darkStyle()
                    .title("Invalid input").text("No account selected")
                    .showError();
            log.error("no account selected");
            return;
        }

        var account = cbAccounts.getSelectionModel().getSelectedItem();
        if (cbAccounts.getSelectionModel().isEmpty()) {
            Notifications.create().owner(((Node) event.getSource()).getScene().getWindow()).darkStyle()
                    .title("Invalid input").text("No receiver selected")
                    .showError();
            log.error("no receiver selected");
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
            log.info("deposit {} to {}", amount, account);
            Notifications.create().owner(((Node) event.getSource()).getScene().getWindow()).darkStyle()
                    .title("Deposit successful")
                    .showConfirm();
            depositMoneyUseCase.deposit(Money.of(amount), account.getId());
        } else if (rbWithdraw.isSelected()) {
            try {
                withdrawMoneyUseCase.withdraw(Money.of(amount), account.getId());
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
            if (cbReceivers.getSelectionModel().isEmpty()) {
                Notifications.create().owner(((Node) event.getSource()).getScene().getWindow()).darkStyle()
                        .title("Invalid input").text("No receiver selected")
                        .showError();
                log.error("no receiver selected");
                return;
            }
            Account receiver = cbReceivers.getSelectionModel().getSelectedItem();
            try {
                transferMoneyUseCase.transfer(Money.of(amount), account.getId(), receiver.getId());
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
        var selectedAccount = cbAccounts.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) return;

        if (selectedAccount.getId().equals(event.getAccountId())) {
            refreshAccountDetails();
        }
    }

    @EventListener
    void on(MoneyWithdrawnEvent event) {
        var selectedAccount = cbAccounts.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) return;

        if (selectedAccount.getId().equals(event.getAccountId())) {
            refreshAccountDetails();
        }
    }

    private void refreshAccounts() {
        var accounts = listAccountsUseCase.listAccounts();
        cbAccounts.getItems().setAll(accounts);
    }

    private void refreshAccountDetails() {
        var account = cbAccounts.getSelectionModel().getSelectedItem();
        if (account == null) {
            tfBalance.clear();
            // TODO clear details
            return;
        }

        var balance = getBalanceUseCase.getBalance(account.getId());
        tfBalance.setText(Double.toString(balance.getAmount().doubleValue()));

        // TODO show details in UI
    }

    private void refreshReceivers() {
        var selectedAccount = cbAccounts.getSelectionModel().getSelectedItem();
        Predicate<Account> notSelected = account -> selectedAccount != null && !account.equals(selectedAccount);
        cbReceivers.getItems().setAll(cbAccounts.getItems().filtered(notSelected));
    }

    private void resetView() {
        cbAccounts.getSelectionModel().clearSelection();
        tfBalance.clear();
        cbReceivers.getSelectionModel().clearSelection();

        tgTransaction.getToggles().forEach(toggle -> toggle.setSelected(false));
        tfAmount.clear();
    }

}
