package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ListBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

@Slf4j
public class LagerbankController implements Initializable {

    @FXML
    private ComboBox<Account> cbAccounts;

    @FXML
    private Button btnNewUser;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var accounts = List.of(Account.createFor("Peter", "Peterson"), Account.createFor("Julia", "Juliette"));


        StringConverter<Account> accountStringConverter = new StringConverter<>() { // TODO move to own class
            @Override
            public String toString(Account account) {
                if (account == null) return "";
                else return String.format("%s %s", account.getFirstName(), account.getLastName());
            }

            @Override
            public Account fromString(String s) {
                throw new UnsupportedOperationException();
            }
        };

        cbAccounts.setConverter(accountStringConverter);
        cbReceivers.setConverter(accountStringConverter);

        cbAccounts.getItems().setAll(accounts);
        cbAccounts.setOnAction(event -> {
            var selectedAccount = cbAccounts.getSelectionModel().getSelectedItem();
            Predicate<Account> filter = account -> selectedAccount != null && !account.equals(selectedAccount);
            cbReceivers.getItems().setAll(cbAccounts.getItems().filtered(filter));
        });

        cbReceivers.disableProperty().bind(rbTransfer.selectedProperty().not());
    }

    @FXML
    void onAccountSelected(ActionEvent event) {
        log.info("onAccountSelected");

        // TODO load data for this account;
        //  current balance,
    }

    @FXML
    void onApply(ActionEvent event) {
        log.info("onApply");

        if (cbAccounts.getSelectionModel().isEmpty()) {
            log.info("no account selected");
            return;
        }

        if (rbDeposit.isSelected()) {
            log.info("deposit " + tfAmount.getText() +
                    " to " + cbAccounts.getSelectionModel().getSelectedItem().getFirstName());
            // TODO deposit money
        } else if (rbWithdraw.isSelected()) {
            log.info("withdraw " + tfAmount.getText() +
                    " from " + cbAccounts.getSelectionModel().getSelectedItem().getFirstName());
            // TODO withdraw money
        } else if (rbTransfer.isSelected()) {
            if (cbReceivers.getSelectionModel().isEmpty()) {
                log.info("no receiver selected");
                return;
            }
            log.info("transfer " + tfAmount.getText() +
                    " from " + cbAccounts.getSelectionModel().getSelectedItem().getFirstName() +
                    " to " + cbReceivers.getSelectionModel().getSelectedItem().getFirstName());
            // TODO transfer
        } else {
            log.info("no transaction type selected");
            // TODO indicate error in UI
        }

        // TODO call onReset when successful
    }

    @FXML
    void onCreateAccount(ActionEvent event) {
        log.info("onCreateAccount");

        // TODO open create user dialog
    }

    @FXML
    void onReset(ActionEvent event) {
        log.info("onReset");

        cbAccounts.getSelectionModel().clearSelection();
        cbReceivers.getSelectionModel().clearSelection();

        tgTransaction.getToggles().forEach(toggle -> toggle.setSelected(false));
        tfAmount.clear();
    }

}
