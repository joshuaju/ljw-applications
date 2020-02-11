package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.gui.BuildNotification;
import de.ljw.aachen.gui.converter.AccountStringConverter;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.port.in.DepositMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.TransferMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.WithdrawMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.WithdrawalNotAllowedException;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
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

    @FXML
    private Button btnApply;

    @FXML
    private Button btnReset;

    private final DepositMoneyUseCase depositMoneyUseCase;
    private final WithdrawMoneyUseCase withdrawMoneyUseCase;
    private final TransferMoneyUseCase transferMoneyUseCase;

    private final ObjectProperty<Account> selectedAccountProperty;
    private final ListProperty<Account> accountListProperty;

    private ObjectProperty<Account> selectedReceiverProperty;
    private ValidationSupport amountValidation;
    private ValidationSupport receiverValidation;


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

        btnApply.disableProperty().bind(selectedAccountProperty.isNull().and(tgTransaction.selectedToggleProperty().isNull()));
        btnReset.disableProperty().bind(btnApply.disableProperty());

        Platform.runLater(this::setupValidationSupport);
    }

    void setupValidationSupport() {
        amountValidation = new ValidationSupport();


        Validator<String> decimalNumberValidator = Validator.createRegexValidator(
                "Decimal number with at most two decimal places required",
                "\\d+((.|,)\\d{1,2})?",
                Severity.ERROR);
        selectedAccountProperty.addListener(observable -> {
            amountValidation.registerValidator(
                    tfAmount,
                    accountListProperty.isNotNull()
                            .and(selectedAccountProperty.isNotNull()).get(),
                    decimalNumberValidator);
        });
        amountValidation.errorDecorationEnabledProperty()
                .bind(btnApply.disableProperty().not());

        receiverValidation = new ValidationSupport();
        rbTransfer.selectedProperty().addListener(observable -> {
            receiverValidation.registerValidator(
                    cbReceivers, rbTransfer.selectedProperty()
                            .and(selectedAccountProperty.isNotNull()).get(),
                    (control, receiver) -> ValidationResult.fromErrorIf(cbReceivers, "Receiver Selection required", receiver == null));
        });
        receiverValidation.errorDecorationEnabledProperty()
                .bind(rbTransfer.selectedProperty()
                        .and(amountValidation.errorDecorationEnabledProperty()));


    }


    @FXML
    void onApply(ActionEvent event) { // TODO refactor method
        if (amountValidation.isInvalid()) return;

        var selectedAccount = selectedAccountProperty.getValue();
        double amount = Double.parseDouble(tfAmount.getText().replace(",", "."));

        Runnable performTransaction = () -> { /* do nothing */ };
        if (rbDeposit.isSelected()) {
            performTransaction = () -> depositMoneyUseCase.deposit(Money.of(amount), selectedAccount.getId());
        } else if (rbWithdraw.isSelected()) {
            performTransaction = new Runnable() {
                @Override
                public void run() {
                    try {
                        withdrawMoneyUseCase.withdraw(Money.of(amount), selectedAccount.getId());
                    } catch (WithdrawalNotAllowedException e) {
                        BuildNotification.about("Invalid input", "Withdrawal not allowed", ((Node) event.getSource()).getScene().getWindow())
                                .showError();
                    }
                }
            };
        } else if (rbTransfer.isSelected()) {
            performTransaction = new Runnable() {
                @Override
                public void run() {
                    try {
                        transferMoneyUseCase.transfer(Money.of(amount), selectedAccount.getId(), selectedReceiverProperty.get().getId());
                    } catch (WithdrawalNotAllowedException e) {
                        BuildNotification.about("Invalid input", "Withdrawal not allowed", ((Node) event.getSource()).getScene().getWindow())
                                .showError();
                    }
                }
            };
        }
        performTransaction.run();
        onReset(event);
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
