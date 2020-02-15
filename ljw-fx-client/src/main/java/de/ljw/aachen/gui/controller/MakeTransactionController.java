package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.gui.util.BuildNotification;
import de.ljw.aachen.gui.util.comparator.CompareAccounts;
import de.ljw.aachen.gui.util.converter.AccountStringConverter;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.port.in.DepositMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.TransferMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.WithdrawMoneyUseCase;
import de.ljw.aachen.lagerbank.port.in.WithdrawalNotAllowedException;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

    private ValidationSupport tfAmountValidation;
    private ValidationSupport cbReceiverValidation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedReceiverProperty = new SimpleObjectProperty<Account>();
        selectedReceiverProperty.bind(cbReceivers.getSelectionModel().selectedItemProperty());

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
    void onApply(ActionEvent event) { // TODO refactor method
        if (tfAmountValidation.isInvalid()) return;

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
            if (cbReceiverValidation.isInvalid()) return;
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

}
