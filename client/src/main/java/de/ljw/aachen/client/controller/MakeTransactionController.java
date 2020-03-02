package de.ljw.aachen.client.controller;

import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.client.util.BuildNotification;
import de.ljw.aachen.client.util.CompareAccounts;
import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.data.Transaction;
import de.ljw.aachen.client.util.AccountStringConverter;
import de.ljw.aachen.application.data.Money;
import de.ljw.aachen.application.logic.ExecuteTransaction;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;
import java.util.function.Consumer;

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
    private ComboBox<Account> cbReceivers;

    @FXML
    private Button btnApply;

    @FXML
    private Button btnReset;

    @FXML
    private ResourceBundle resources;

    private ValidationSupport tfAmountValidation;
    private ValidationSupport cbReceiverValidation;
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
        this.setupControlsDisableProperty();
        Platform.runLater(this::setupValidationSupport);
    }

    private void setupControlsDisableProperty() {
        BooleanBinding noAccountSelected = selectedAccountProperty.isNull();
        BooleanBinding noToggleSelected = tgTransaction.selectedToggleProperty().isNull();
        BooleanBinding transferNotSelected = rbTransfer.selectedProperty().not();

        rbDeposit.disableProperty().bind(noAccountSelected);
        rbWithdraw.disableProperty().bind(noAccountSelected);
        rbTransfer.disableProperty().bind(noAccountSelected);
        tfAmount.disableProperty().bind(noAccountSelected.or(noToggleSelected));
        cbReceivers.disableProperty().bind(noAccountSelected.or(transferNotSelected));
        btnApply.disableProperty().bind(noAccountSelected.or(noToggleSelected));
        btnReset.disableProperty().bind(noAccountSelected.or(noToggleSelected));
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
        createTransactionFromUserInput(
                transaction -> executeTransaction.process(transaction),
                () -> BuildNotification.about("Missing required fields", "", ((Node) event.getSource()).getScene().getWindow()).showError()
        );
    }

    private void createTransactionFromUserInput(Consumer<Transaction> onValid, Runnable onInvalid) {
        if (!tfAmountValidation.isInvalid()) {
            var selectedAccount = selectedAccountProperty.getValue();
            double amountValue = Double.parseDouble(tfAmount.getText().replace(",", "."));
            Money amount = new Money(amountValue);

            if (rbDeposit.isSelected())
                onValid.accept(Transaction.deposit(selectedAccount.getId(), amount));
            else if (rbWithdraw.isSelected())
                onValid.accept(Transaction.withdraw(selectedAccount.getId(), amount));
            else if (rbTransfer.isSelected() && !cbReceiverValidation.isInvalid()) {
                Account selectedReceiver = cbReceivers.getValue();
                onValid.accept(Transaction.transfer(selectedAccount.getId(), selectedReceiver.getId(), amount));
            } else onInvalid.run();
        } else onInvalid.run();
    }

    @FXML
    void onReset(ActionEvent event) {
        cbReceivers.getSelectionModel().clearSelection();
        tgTransaction.getToggles().forEach(toggle -> toggle.setSelected(false));
        tfAmount.clear();
    }

}
