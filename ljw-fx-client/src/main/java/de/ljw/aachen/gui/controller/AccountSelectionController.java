package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.gui.cell.list.AccountListCell;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
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
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class AccountSelectionController implements Initializable {

    @FXML
    private ListView<Account> lvAccounts;

    @FXML
    private Button btnEditUser;

    private final CreateUserController createUserController;
    private final EditUserController editUserController;
    private final ObjectProperty<Account> selectedAccountProperty;
    private final ListProperty<Account> accountListProperty;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedAccountProperty.bind(lvAccounts.getSelectionModel().selectedItemProperty());
        lvAccounts.setCellFactory(accountListView -> new AccountListCell());

        Comparator<Account> sortAccountByFirstName = Comparator.comparing(account -> account.getFirstName().toUpperCase());
        lvAccounts.itemsProperty().bind(Bindings.createObjectBinding(
                () -> accountListProperty.sorted(sortAccountByFirstName), accountListProperty));

        btnEditUser.disableProperty().bind(selectedAccountProperty.isNull());
    }

    @FXML
    @SneakyThrows
    void onCreateAccount(ActionEvent event) {
        URL resource = AccountSelectionController.class.getClassLoader().getResource("fxml/user_detail.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setController(createUserController);
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Create new account");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.show();
    }

    @FXML
    @SneakyThrows
    void onEditAccount(ActionEvent event) {
        URL resource = AccountSelectionController.class.getClassLoader().getResource("fxml/user_detail.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setController(editUserController);
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Create new account");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.show();
    }

}
