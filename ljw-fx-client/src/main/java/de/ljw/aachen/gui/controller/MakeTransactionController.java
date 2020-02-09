package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.event.AccountCreatedEvent;
import de.ljw.aachen.account.management.domain.event.AccountDeletedEvent;
import de.ljw.aachen.account.management.domain.event.AccountUpdatedEvent;
import de.ljw.aachen.gui.BuildNotification;
import de.ljw.aachen.gui.converter.AccountStringConverter;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.port.in.DepositMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.TransferMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.WithdrawMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.WithdrawalNotAllowedException;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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

import javax.management.Notification;
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
        cbReceivers.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            var noAccountIsSelected = selectedAccountProperty.isNull();
            var transferButtonIsNotSelected = rbTransfer.selectedProperty().not();
            return noAccountIsSelected.or(transferButtonIsNotSelected).get();
        }, rbTransfer.selectedProperty(), selectedAccountProperty));
        selectedReceiverProperty = new SimpleObjectProperty<Account>();
        selectedReceiverProperty.bind(cbReceivers.getSelectionModel().selectedItemProperty());
        selectedAccountProperty.addListener((observableValue, previous, selected) -> refresh());

    }

    @FXML
    void onApply(ActionEvent event) { // TODO refactor method
        var selectedAccount = selectedAccountProperty.getValue();
        if (selectedAccount == null) {
            BuildNotification.about("Invalid input", "No account selected", ((Node) event.getSource()).getScene().getWindow())
                    .showError();
            log.error("no account selected");
            return;
        }

        double amount = 0.0;
        try {
            amount = Double.parseDouble(tfAmount.getText());
        } catch (NumberFormatException e) {
            BuildNotification.about("Invalid input", "Amount needs to be a decimal number", ((Node) event.getSource()).getScene().getWindow())
                    .showError();
            log.error("amount is not a double", e);
            return;
        }

        if (rbDeposit.isSelected()) {
            log.info("deposit {} to {}", amount, selectedAccount);
            BuildNotification.about("Deposit successful", null, ((Node) event.getSource()).getScene().getWindow())
                    .showConfirm();
            depositMoneyUseCase.deposit(Money.of(amount), selectedAccount.getId());
        } else if (rbWithdraw.isSelected()) {
            try {
                withdrawMoneyUseCase.withdraw(Money.of(amount), selectedAccount.getId());
                BuildNotification.about("Withdrawal successful", null, ((Node) event.getSource()).getScene().getWindow())
                        .showConfirm();
            } catch (WithdrawalNotAllowedException e) { // TODO code clone
                BuildNotification.about("Invalid input", "Withdrawal not allowed", ((Node) event.getSource()).getScene().getWindow())
                        .showError();
                log.error("Withdrawal not allowed", e);
                return;
            }
        } else if (rbTransfer.isSelected()) {
            var selectedReceiver = selectedReceiverProperty.getValue();
            if (selectedReceiver == null) {
                BuildNotification.about("Invalid input", "No receiver selected", ((Node) event.getSource()).getScene().getWindow())
                        .showError();
                log.error("no receiver selected");
                return;
            }

            try {
                transferMoneyUseCase.transfer(Money.of(amount), selectedAccount.getId(), selectedReceiver.getId());
                BuildNotification.about("Transfer successful", null, ((Node) event.getSource()).getScene().getWindow())
                        .title("Transfer successful")
                        .showConfirm();
            } catch (WithdrawalNotAllowedException e) { // TODO code clone
                BuildNotification.about("Invalid input", "Withdrawal not allowed", ((Node) event.getSource()).getScene().getWindow())
                        .showError();
                log.error("Withdrawal not allowed", e);
                return;
            }
        } else {
            BuildNotification.about("Invalid input", "No transaction type selected", ((Node) event.getSource()).getScene().getWindow())
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

    private void refresh() {
        var selectedAccount = selectedAccountProperty.getValue();
        Predicate<Account> notSelected = account -> !account.equals(selectedAccount);
        var accounts = accountListProperty.filtered(notSelected);
        cbReceivers.getItems().setAll(accounts);
    }

}
