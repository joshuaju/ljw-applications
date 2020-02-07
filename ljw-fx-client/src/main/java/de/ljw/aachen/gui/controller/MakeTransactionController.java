package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.event.AccountCreatedEvent;
import de.ljw.aachen.account.management.domain.event.AccountDeletedEvent;
import de.ljw.aachen.account.management.domain.event.AccountUpdatedEvent;
import de.ljw.aachen.gui.converter.AccountStringConverter;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.port.in.DepositMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.TransferMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.WithdrawMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.WithdrawalNotAllowedException;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.springframework.context.event.EventListener;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
public class MakeTransactionController implements Initializable {

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

    private final DepositMoneyUseCase depositMoneyUseCase;
    private final WithdrawMoneyUseCase withdrawMoneyUseCase;
    private final TransferMoneyUseCase transferMoneyUseCase;

    private final ObjectProperty<Account> selectedAccountProperty;
    private final ListProperty<Account> accountListProperty;

    private ObjectProperty<Account> selectedReceiverProperty;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbReceivers.setConverter(new AccountStringConverter());
        cbReceivers.disableProperty().bind(rbTransfer.selectedProperty().not());
        selectedReceiverProperty = new SimpleObjectProperty<Account>();
        selectedReceiverProperty.bind(cbReceivers.getSelectionModel().selectedItemProperty());
        selectedAccountProperty.addListener((observableValue, previous, selected) -> refresh());
    }

    @FXML
    void onApply(ActionEvent event) { // TODO refactor method
        var selectedAccount = selectedAccountProperty.getValue();
        if (selectedAccount == null) {
            Notifications.create().owner(((Node) event.getSource()).getScene().getWindow())
                    .title("Invalid input").text("No account selected")
                    .showError();
            log.error("no account selected");
            return;
        }

        double amount = 0.0;
        try {
            amount = Double.parseDouble(tfAmount.getText());
        } catch (NumberFormatException e) {
            Notifications.create().owner(((Node) event.getSource()).getScene().getWindow())
                    .title("Invalid input").text("Amount needs to be a decimal number")
                    .showError();
            log.error("amount is not a double", e);
            return;
        }

        if (rbDeposit.isSelected()) {
            log.info("deposit {} to {}", amount, selectedAccount);
            Notifications.create().owner(((Node) event.getSource()).getScene().getWindow())
                    .title("Deposit successful")
                    .showConfirm();
            depositMoneyUseCase.deposit(Money.of(amount), selectedAccount.getId());
        } else if (rbWithdraw.isSelected()) {
            try {
                withdrawMoneyUseCase.withdraw(Money.of(amount), selectedAccount.getId());
                Notifications.create().owner(((Node) event.getSource()).getScene().getWindow())
                        .title("Withdrawal successful")
                        .showConfirm();
            } catch (WithdrawalNotAllowedException e) { // TODO code clone
                Notifications.create().owner(((Node) event.getSource()).getScene().getWindow())
                        .title("Invalid transaction").text("Withdrawal not allowed")
                        .showError();
                log.error("Withdrawal not allowed", e);
                return;
            }
        } else if (rbTransfer.isSelected()) {
            var selectedReceiver = selectedReceiverProperty.getValue();
            if (selectedReceiver == null) {
                Notifications.create().owner(((Node) event.getSource()).getScene().getWindow())
                        .title("Invalid input").text("No receiver selected")
                        .showError();
                log.error("no receiver selected");
                return;
            }

            try {
                transferMoneyUseCase.transfer(Money.of(amount), selectedAccount.getId(), selectedReceiver.getId());
                Notifications.create().owner(((Node) event.getSource()).getScene().getWindow())
                        .title("Transfer successful")
                        .showConfirm();
            } catch (WithdrawalNotAllowedException e) { // TODO code clone
                Notifications.create().owner(((Node) event.getSource()).getScene().getWindow())
                        .title("Invalid transaction").text("Withdrawal not allowed")
                        .showError();
                log.error("Withdrawal not allowed", e);
                return;
            }
        } else {
            Notifications.create().owner(((Node) event.getSource()).getScene().getWindow())
                    .title("Invalid input").text("No transaction type selected")
                    .showError();
            log.error("no transaction type selected");
            return;
        }
        tfAmount.clear();
    }

    @FXML
    void onReset(ActionEvent event) {
        cbReceivers.getSelectionModel().clearSelection();
        tgTransaction.getToggles().forEach(toggle -> toggle.setSelected(false));
        tfAmount.clear();
    }

    @EventListener(classes = {AccountCreatedEvent.class, AccountDeletedEvent.class, AccountUpdatedEvent.class})
    void on() {
        refresh();
    }

    private void refresh() {
        var selectedAccount = selectedAccountProperty.getValue();
        Predicate<Account> notSelected = account -> !account.equals(selectedAccount);
        var accounts = accountListProperty.filtered(notSelected);
        cbReceivers.getItems().setAll(accounts);
    }

}
