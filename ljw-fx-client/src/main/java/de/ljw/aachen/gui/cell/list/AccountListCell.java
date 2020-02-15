package de.ljw.aachen.gui.cell.list;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.gui.util.converter.AccountStringConverter;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;

public class AccountListCell extends ListCell<Account> {

    private final AccountStringConverter accountStringConverter;

    public AccountListCell() {
        accountStringConverter = new AccountStringConverter();
        setOnMouseClicked(this::clearSelectionIfEmptyCell);
    }

    @Override
    protected void updateItem(Account account, boolean b) {
        super.updateItem(account, b);
        if (account == null) {
            setText("");
        }
        setText(accountStringConverter.toString(account));
    }

    private void clearSelectionIfEmptyCell(MouseEvent mouseEvent) {
        if (getItem() == null)
            getListView().getSelectionModel().clearSelection();
    }
}
