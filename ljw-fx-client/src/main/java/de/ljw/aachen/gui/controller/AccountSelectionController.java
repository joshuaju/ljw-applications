package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.gui.cell.list.AccountListCell;
import de.ljw.aachen.gui.util.comparator.CompareAccounts;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class AccountSelectionController {

    @FXML
    private ListView<Account> lvAccounts;

    @FXML
    private Button btnEditUser;

    @FXML
    private TextField tfSearchAccount;

    private final CreateUserController createUserController;
    private final EditUserController editUserController;
    private final ObjectProperty<Account> selectedAccountProperty;
    private final ListProperty<Account> accountListProperty;

    @FXML
    private void initialize() {
        selectedAccountProperty.bind(lvAccounts.getSelectionModel().selectedItemProperty());
        btnEditUser.disableProperty().bind(selectedAccountProperty.isNull());

        // setup account filtering
        FilteredList<Account> filteredAccounts = new FilteredList<>(accountListProperty);
        SortedList<Account> sortedAndFilteredAccounts = new SortedList<>(filteredAccounts, CompareAccounts.byFirstName());
        tfSearchAccount.textProperty().addListener((observableValue, previousValue, newValue) -> {
            filteredAccounts.setPredicate(account -> {
                if (newValue == null || newValue.isBlank()) return true;
                var expressions = List.of(newValue.split(" "));
                return expressions.stream().map(String::toLowerCase).map(String::trim).anyMatch(expression -> {
                    boolean expressionInFirstName = account.getFirstName().toLowerCase().contains(expression);
                    boolean expressionInLastName = account.getLastName().toLowerCase().contains(expression);
                    return expressionInFirstName || expressionInLastName;
                });

            });
        });

        lvAccounts.setItems(sortedAndFilteredAccounts);
        lvAccounts.setCellFactory(accountListView -> new AccountListCell());
    }

    @FXML
    @SneakyThrows
    void onCreateAccount(ActionEvent event) {
        URL resource = AccountSelectionController.class.getClassLoader().getResource("fxml/user_detail.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setController(createUserController);
        loader.setResources(ResourceBundle.getBundle("Bundle", Locale.getDefault()));// TODO inject bundle
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
        loader.setResources(ResourceBundle.getBundle("Bundle", Locale.getDefault()));// TODO inject bundle
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Create new account");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.show();
    }

}
