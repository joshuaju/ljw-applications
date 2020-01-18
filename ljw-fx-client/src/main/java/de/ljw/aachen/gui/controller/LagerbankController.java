package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.event.AccountCreatedEvent;
import de.ljw.aachen.account.management.port.in.ListAccountsUseCase;
import de.ljw.aachen.account.management.port.in.ReadAccountUseCase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

import java.net.URL;
import java.util.List;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        var accounts = listAccountsUseCase.listAccounts();
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
    @SneakyThrows
    void onCreateAccount(ActionEvent event) {
        log.info("onCreateAccount");
        Stage stage = new Stage();
        URL resource = LagerbankController.class.getClassLoader().getResource("fxml/create_user.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(applicationContext::getBean);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Create new account");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.show();
    }

    @FXML
    void onReset(ActionEvent event) {
        log.info("onReset");

        cbAccounts.getSelectionModel().clearSelection();
        cbReceivers.getSelectionModel().clearSelection();

        tgTransaction.getToggles().forEach(toggle -> toggle.setSelected(false));
        tfAmount.clear();
    }

    @EventListener
    void on(AccountCreatedEvent event) {
        log.info("Received account created event");
        var accounts = listAccountsUseCase.listAccounts();
        cbAccounts.getItems().setAll(accounts);
    }


}
