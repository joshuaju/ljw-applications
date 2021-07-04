package de.ljw.aachen.client.controller;

import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.client.FXMLRegister;
import de.ljw.aachen.client.controls.AccountListCell;
import de.ljw.aachen.client.util.CompareAccounts;
import de.ljw.aachen.client.util.Memoize;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
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
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class AccountSelectionController
{

    private final ObjectProperty<Account> selectedAccountProperty;
    private final ReadOnlyListProperty<Account> accountListProperty;
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
    private void initialize()
    {
        editUserController.setOnEditedAccount(account -> lvAccounts.refresh());
        selectedAccountProperty.bind(lvAccounts.getSelectionModel().selectedItemProperty());
        btnEditUser.disableProperty().bind(selectedAccountProperty.isNull());

        // setup account filtering
        FilteredList<Account> filteredAccounts = new FilteredList<>(accountListProperty);
        SortedList<Account> sortedAndFilteredAccounts = new SortedList<>(filteredAccounts, CompareAccounts.byFirstNameAndLastName());
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
        lvAccounts.setOnKeyTyped(keyEvent -> {
            if (KeyCode.ESCAPE.getChar().equals(keyEvent.getCharacter()))
                lvAccounts.getSelectionModel().clearSelection();
        });
    }

    @FXML
    @SneakyThrows
    void onCreateAccount(ActionEvent event)
    {
        FXMLLoader loader = new FXMLLoader(FXMLRegister.USER_DETAIL.getResource());
        loader.setController(createUserController);
        loader.setResources(resources);
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(resources.getString("title.create.account"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.show();

        Memoize.stagePosition(stage, FXMLRegister.USER_DETAIL.getFileName());
    }

    @FXML
    @SneakyThrows
    void onEditAccount(ActionEvent event)
    {
        FXMLLoader loader = new FXMLLoader(FXMLRegister.USER_DETAIL.getResource());
        loader.setController(editUserController);
        loader.setResources(resources);
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(resources.getString("title.edit.account"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.show();

        Memoize.stagePosition(stage, FXMLRegister.USER_DETAIL.getFileName());
    }

}
