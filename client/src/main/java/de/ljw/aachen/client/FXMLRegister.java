package de.ljw.aachen.client;

import lombok.Getter;

import java.net.URL;

@Getter
public enum FXMLRegister
{
    ACCOUNT_SELECTION("/fxml/account_selection.fxml"),
    BALANCE_OVERVIEW("/fxml/balance_overview.fxml"),
    IMPORT_ACCOUNTS("/fxml/import_accounts.fxml"),
    LAGERBANK("/fxml/lagerbank.fxml"),
    MAKE_TRANSACTION("/fxml/make_transaction.fxml"),
    MENU("/fxml/menu.fxml"),
    TRANSACTIONS_OVERVIEW("/fxml/transactions_overview.fxml"),
    USER_DETAIL("/fxml/user_detail.fxml");

    private final String fileName;
    private final URL resource;

    FXMLRegister(String fileName)
    {
        this.fileName = fileName;
        this.resource = getClass().getResource(fileName);
    }
}
