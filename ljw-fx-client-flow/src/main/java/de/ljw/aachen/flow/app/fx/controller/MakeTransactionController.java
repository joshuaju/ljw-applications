package de.ljw.aachen.flow.app.fx.controller;

import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.adapter.TransactionStore;
import de.ljw.aachen.flow.app.fx.util.comparator.CompareAccounts;
import de.ljw.aachen.flow.app.fx.util.converter.AccountStringConverter;
import de.ljw.aachen.flow.data.Account;
import de.ljw.aachen.flow.data.Money;
import de.ljw.aachen.flow.data.Transaction;
import de.ljw.aachen.flow.logic.transactions.ExecuteTransaction;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Slf4j
@Component
@RequiredArgsConstructor
public class MakeTransactionController {
    private final ObjectProperty<Account> selectedAccountProperty;
    private final ListProperty<Account> accountListProperty;
    private final FileSystem fileSystem;
    private final TransactionStore transactionStore;

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

    @FXML
    private ResourceBundle resources;

    private ValidationSupport tfAmountValidation;
    private ValidationSupport cbReceiverValidation;

    @FXML
    private void initialize() {
        cbReceivers.setConverter(new AccountStringConverter());
        FilteredList<Account> filteredAccounts = new FilteredList<>(accountListProperty);
        selectedAccountProperty.addListener((observableValue, previousAccount, selectedAccount) -> {
            filteredAccounts.setPredicate(account -> {
                boolean receiverIsSelectedAccount = selectedAccount != null && selectedAccount.getId().equals(account.getId());
                return !(receiverIsSelectedAccount); // receiver should not be shown when it is the selected account
            });
        });
        SortedList<Account> sortedAndFilteredAccounts = new SortedList<>(filteredAccounts, CompareAccounts.byFirstName());
        cbReceivers.setItems(sortedAndFilteredAccounts);
        this.setupControlsDisableProperty();
        Platform.runLater(this::setupValidationSupport);
    }

    private void setupControlsDisableProperty() {
        BooleanBinding noAccountSelected = selectedAccountProperty.isNull();
        btnApply.disableProperty().bind(noAccountSelected);
        btnReset.disableProperty().bind(noAccountSelected);
        tfAmount.disableProperty().bind(noAccountSelected);
        rbDeposit.disableProperty().bind(noAccountSelected);
        rbWithdraw.disableProperty().bind(noAccountSelected);
        rbTransfer.disableProperty().bind(noAccountSelected);
        cbReceivers.disableProperty().bind(Bindings.createBooleanBinding(
                noAccountSelected.or(rbTransfer.selectedProperty().not())::get, rbTransfer.selectedProperty(), selectedAccountProperty));
    }

    private void setupValidationSupport() {
        /* amount validation **************************************************************************************** */
        tfAmountValidation = new ValidationSupport();
        Validator<String> decimalNumberValidator = Validator.createRegexValidator(
                "Decimal number with at most two decimal places required",
                "\\d+((.|,)\\d{1,2})?",
                Severity.ERROR);

        selectedAccountProperty.addListener(observable -> {
            tfAmountValidation.registerValidator(
                    tfAmount,
                    accountListProperty.isNotNull().and(selectedAccountProperty.isNotNull()).get(),
                    decimalNumberValidator);
        });
        /* receiver selection validation ************************************************************************** */
        cbReceiverValidation = new ValidationSupport();
        Validator<Object> receiverSelectedValidator = (control, receiver) ->
                ValidationResult.fromErrorIf(cbReceivers, "Receiver Selection required", receiver == null);
        rbTransfer.selectedProperty().addListener((observableValue, wasSelected, isSelected) -> {
            cbReceiverValidation.registerValidator(
                    cbReceivers,
                    isSelected,
                    receiverSelectedValidator);
        });
        /* decoration visibility ************************************************************************************ */
        tfAmountValidation.errorDecorationEnabledProperty()
                .bind(tfAmount.disableProperty().not());

        cbReceiverValidation.errorDecorationEnabledProperty()
                .bind(cbReceivers.disableProperty().not());
    }


    @FXML
    void onApply(ActionEvent event) {
        if (tfAmountValidation.isInvalid()) return;

        var selectedAccount = selectedAccountProperty.getValue();
        double amount = Double.parseDouble(tfAmount.getText().replace(",", "."));

        Transaction transaction = null;
        if (rbDeposit.isSelected()) {
            transaction = Transaction.deposit(selectedAccount.getId(), new Money(amount));
        } else if (rbWithdraw.isSelected()) {
            transaction = Transaction.withdraw(selectedAccount.getId(), new Money(amount));
        } else if (rbTransfer.isSelected()) {
            Account selectedReceiver = cbReceivers.getValue();
            transaction = Transaction.transfer(selectedAccount.getId(), selectedReceiver.getId(), new Money(amount));
        }
        var executeTransaction = new ExecuteTransaction(fileSystem, transactionStore);
        executeTransaction.process(transaction);

        onReset(event);
    }

    @FXML
    void onReset(ActionEvent event) {
        cbReceivers.getSelectionModel().clearSelection();
        tgTransaction.getToggles().forEach(toggle -> toggle.setSelected(false));
        tfAmount.clear();
    }

}
