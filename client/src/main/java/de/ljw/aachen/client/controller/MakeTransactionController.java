package de.ljw.aachen.client.controller;

import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.client.exception.ValidationException;
import de.ljw.aachen.client.util.CompareAccounts;
import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.data.Transaction;
import de.ljw.aachen.client.util.AccountStringConverter;
import de.ljw.aachen.application.data.Money;
import de.ljw.aachen.application.logic.ExecuteTransaction;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

import static de.ljw.aachen.client.exception.NotifyingExceptionHandler.tryRun;

@Slf4j
@Component
@RequiredArgsConstructor
public class MakeTransactionController {
    private final ObjectProperty<Account> selectedAccountProperty;
    private final ReadOnlyListProperty<Account> accountListProperty;
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
    private TextField tfDescription;

    @FXML
    private ComboBox<Account> cbReceivers;

    @FXML
    private Button btnApply;

    @FXML
    private Button btnReset;

    @FXML
    private CheckBox cbOverdraw;

    @FXML
    private ResourceBundle resources;

    private ValidationSupport tfAmountValidation;
    private ValidationSupport cbReceiverValidation;
    private ValidationSupport tgTransactionValidation;
    private ExecuteTransaction executeTransaction;

    @FXML
    private void initialize() {
        executeTransaction = new ExecuteTransaction(fileSystem, transactionStore);
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
        Platform.runLater(this::setupValidationSupport);

        cbReceivers.disableProperty().bind(Bindings.createBooleanBinding(() -> !rbTransfer.equals(tgTransaction.getSelectedToggle()), tgTransaction.selectedToggleProperty()));
        cbOverdraw.disableProperty().bind(Bindings.createBooleanBinding(() -> tgTransaction.getSelectedToggle() == null || rbDeposit.equals(tgTransaction.getSelectedToggle()), tgTransaction.selectedToggleProperty()));
    }

    private void setupValidationSupport() {
        /* amount validation **************************************************************************************** */
        tfAmountValidation = new ValidationSupport();
        Validator<String> decimalNumberValidator = Validator.createRegexValidator(
                "Decimal number with at most two decimal places required",
                "\\d+((.|,)\\d{1,2})?",
                Severity.ERROR);
        tfAmountValidation.registerValidator(tfAmount, true, decimalNumberValidator);

        /* receiver selection validation **************************************************************************** */
        cbReceiverValidation = new ValidationSupport();
        Validator<Object> receiverSelectedValidator = (control, receiver) ->
                ValidationResult.fromErrorIf(cbReceivers, "Receiver Selection required", receiver == null);
        rbTransfer.selectedProperty().addListener((observableValue, wasSelected, isSelected) -> {
            cbReceiverValidation.registerValidator(
                    cbReceivers,
                    isSelected,
                    receiverSelectedValidator);
        });
        cbReceiverValidation.errorDecorationEnabledProperty().bind(rbTransfer.selectedProperty());
        /* transaction type selection validation ******************************************************************** */
        tgTransactionValidation = new ValidationSupport();
        tgTransaction.selectedToggleProperty().addListener((observableValue, previousToggle, newToggle) -> {
            tgTransactionValidation.registerValidator(rbDeposit, true,
                    (control, t) -> ValidationResult.fromErrorIf(control, "Transaction type required", newToggle == null));
        });


    }


    @FXML
    void onApply(ActionEvent event) {
        if (tgTransactionValidation.isInvalid())
            throw new ValidationException("error.detail.transaction.type.not.selected");
        tryRun(() -> {
            var amount = getAmount();
            var selectedAccount = selectedAccountProperty.get();
            var transaction = getTransaction(selectedAccount, amount);
            executeTransaction.process(transaction, cbOverdraw.isSelected());
            Stage stage = (Stage) btnApply.getScene().getWindow();
            stage.close();
        }, event, resources);
    }

    private Money getAmount() {
        if (tfAmountValidation.isInvalid()) throw new ValidationException("error.detail.validation.amount.invalid");
        double amountValue = Double.parseDouble(tfAmount.getText().replace(",", "."));
        return new Money(amountValue);
    }

    private Transaction getTransaction(Account selectedAccount, Money amount) {
        Transaction transaction = null;
        if (rbDeposit.isSelected())
            transaction = Transaction.deposit(selectedAccount.getId(), amount);
        else if (rbWithdraw.isSelected())
            transaction = Transaction.withdraw(selectedAccount.getId(), amount);
        else if (rbTransfer.isSelected()) {
            if (cbReceiverValidation.isInvalid())
                throw new ValidationException("error.detail.validation.receiver.missing");
            Account selectedReceiver = cbReceivers.getValue();
            transaction = Transaction.transfer(selectedAccount.getId(), selectedReceiver.getId(), amount);
        } else throw new ValidationException("error.detail.transaction.type.not.selected");
        transaction.setDescription(tfDescription.getText().replaceAll("[\\r\\n]", ""));
        return transaction;
    }

    @FXML
    void onReset(ActionEvent event) {
        cbReceivers.getSelectionModel().clearSelection();
        tgTransaction.getToggles().forEach(toggle -> toggle.setSelected(false));
        tfAmount.clear();
        tfDescription.clear();
        cbOverdraw.setSelected(false);
    }

}
