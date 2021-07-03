package de.ljw.aachen.client.controller;

import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.data.Balance;
import de.ljw.aachen.application.data.Money;
import de.ljw.aachen.application.data.Transaction;
import de.ljw.aachen.application.logic.ListAllBalances;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
@Component
@RequiredArgsConstructor
public class BalanceOverviewController
{

    private final ReadOnlyListProperty<Account> accountListProperty;
    private final ReadOnlyListProperty<Transaction> transactionListProperty;

    private final ListAllBalances listAllBalances;

    @FXML
    ResourceBundle resources;

    @FXML
    private TableView<Balance> tvBalance;

    @FXML
    private TableColumn<Balance, String> tcFirstName;

    @FXML
    private TableColumn<Balance, String> tcLastName;

    @FXML
    private TableColumn<Balance, Money> tcAmount;

    @FXML
    private void initialize()
    {
        List<Balance> balanceList = listAllBalances.listAll();

        tvBalance.setItems(new SortedList<>(FXCollections.observableList(balanceList),
                Comparator.comparing(Balance::getFirstName)
                          .thenComparing(Balance::getLastName)));

        tcFirstName.setCellValueFactory(feat ->
                new SimpleStringProperty(feat.getValue().getFirstName()));
        tcLastName.setCellValueFactory(feat ->
                new SimpleStringProperty(feat.getValue().getLastName()));

//        tcAmount.setSortable(false);
        tcAmount.setCellValueFactory(feat ->
                new SimpleObjectProperty<>(feat.getValue().getValue()));
        tcAmount.setCellFactory(col -> new TableCell<>()
        {
            @Override
            protected void updateItem(Money item, boolean empty)
            {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setText(item.formatWithCurrency());
                    if (item.isLessThan(new Money(0)))
                        setStyle("-fx-background-color: orange");
                    else
                        setStyle("");
                }

            }


        });
    }

}
