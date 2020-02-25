package de.ljw.aachen.flow.app.fx.controller;

import de.ljw.aachen.flow.app.fx.cell.list.AccountListCell;
import de.ljw.aachen.flow.app.fx.util.comparator.CompareAccounts;
import de.ljw.aachen.flow.data.Account;
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
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class AccountSelectionController {

    private final ObjectProperty<Account> selectedAccountProperty;
    private final ListProperty<Account> accountListProperty;
    private final CreateUserController createUserController;
    private final EditUserController editUserController;

    @FXML
    private ListView<Account> lvAccounts;
    @FXML
    private Button btnEditUser;

    @FXML
    private TextField tfSearchAccount;

    @FXML
    private ResourceBundle resources;





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
        loader.setResources(resources);
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(resources.getString("title.create.account"));
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
        loader.setResources(resources);
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(resources.getString("title.edit.account"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.show();
    }

}
