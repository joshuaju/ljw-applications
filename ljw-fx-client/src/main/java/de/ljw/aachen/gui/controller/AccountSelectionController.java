package de.ljw.aachen.gui.controller;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.gui.cell.list.AccountListCell;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class AccountSelectionController implements Initializable {

    @FXML
    private ListView<Account> lvAccounts;

    private final ApplicationContext applicationContext;
    private final ObjectProperty<Account> selectedAccountProperty;
    private final ListProperty<Account> accountListProperty;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lvAccounts.setCellFactory(accountListView -> new AccountListCell());
        selectedAccountProperty.bind(lvAccounts.getSelectionModel().selectedItemProperty());
        lvAccounts.itemsProperty().bind(Bindings.createObjectBinding(() ->
                accountListProperty.sorted(Comparator.comparing(Account::getFirstName)), accountListProperty));
    }

    @FXML
    @SneakyThrows
    void onCreateAccount(ActionEvent event) {
        URL resource = AccountSelectionController.class.getClassLoader().getResource("fxml/create_user.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(applicationContext::getBean);
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Create new account");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.show();
    }

}
