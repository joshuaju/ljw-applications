package de.ljw.aachen.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LagerbankController {

    @FXML
    private ComboBox<?> cbAccounts;

    @FXML
    private Button btnNewUser;

    @FXML
    private RadioButton tbDeposit;

    @FXML
    private ToggleGroup tgTransaction;

    @FXML
    private RadioButton tbWithdraw;

    @FXML
    private RadioButton tbTransfer;

    @FXML
    private ComboBox<?> cbReceivers;

    @FXML
    private Button btnApply;

    @FXML
    private Button btnReset;

    @FXML
    void onAccountSelected(ActionEvent event) {
        log.debug("onAccountSelected");

        // TODO load data for this account;
        //  current balance,
    }

    @FXML
    void onApply(ActionEvent event) {
        log.debug("onApply");

        // TODO deposit, withdraw or transfer money
    }

    @FXML
    void onCreateAccount(ActionEvent event) {
        log.debug("onCreateAccount");

        // TODO open create user dialog
    }

    @FXML
    void onReset(ActionEvent event) {
        log.debug("onReset");

        // TODO clear everything;
        //  cbAccounts,
        //  tgTransaction,
        //  tfAmount
        //  cbReceivers,

    }

}
