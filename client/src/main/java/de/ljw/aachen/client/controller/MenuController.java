package de.ljw.aachen.client.controller;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.data.Money;
import de.ljw.aachen.application.data.Transaction;
import de.ljw.aachen.application.logic.CalculateBalance;
import de.ljw.aachen.application.logic.CashUp;
import de.ljw.aachen.application.logic.ComposeFullName;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.InfoProperties;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MenuController {

    @Autowired
    AccountStore accountStore;

    @Autowired
    TransactionStore transactionStore;

    @FXML
    ResourceBundle resources;

    @FXML
    void onCashUp(ActionEvent event) {
        var allTransactions = transactionStore.getTransactions();
        var availableCash = CashUp.cashUp(allTransactions);

        String content = MessageFormat.format(resources.getString("cash.up.info"), availableCash.formatWithCurrency());
        var info = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
        info.setTitle(resources.getString("cash.up.title"));
        info.setHeaderText(null);
        info.setGraphic(null);
        info.show();
    }

    @FXML
    void onCheckBalance(ActionEvent event) {
        var accounts = accountStore.getAccounts();
        var transactions = transactionStore.getTransactions();

        Map<Account, Money> accountBalanceMap = getAccountMoneyMap(accounts, transactions);
        GridPane nameBalanceGrid = getAccountMoneyGridPane(accountBalanceMap);

        var info = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
        info.setTitle(resources.getString("balance.check.title"));
        info.setHeaderText(null);
        info.getDialogPane().setContent(nameBalanceGrid);
        info.setResizable(true);
        info.setGraphic(null);
        info.show();
    }

    private GridPane getAccountMoneyGridPane(Map<Account, Money> accountBalanceMap) {
        GridPane nameBalanceGrid = new GridPane();
        nameBalanceGrid.setHgap(15);
        nameBalanceGrid.setVgap(5);

        var rowIndex = 0;
        for (var entry : accountBalanceMap.entrySet()) {
            nameBalanceGrid.addRow(rowIndex++,
                    new Label(ComposeFullName.process(entry.getKey())),
                    new Label(entry.getValue().formatWithCurrency()));
        }
        return nameBalanceGrid;
    }

    private Map<Account, Money> getAccountMoneyMap(List<Account> accounts, List<Transaction> transactions) {
        var zeroBalance = new Money(0.00);
        var accountBalanceMap = accounts.stream()
                .collect(Collectors.toMap(Function.identity(),
                        account -> CalculateBalance.process(account.getId(), transactions)
                ));
        accountBalanceMap.entrySet().removeIf(entry -> entry.getValue().isGreaterThanOrEqual(zeroBalance));
        return accountBalanceMap;
    }

    @FXML
    void onImport(ActionEvent event) {
        log.info("import accounts");
    }


}
