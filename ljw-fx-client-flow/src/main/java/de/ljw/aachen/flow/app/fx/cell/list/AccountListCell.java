package de.ljw.aachen.flow.app.fx.cell.list;


import de.ljw.aachen.flow.app.fx.util.converter.AccountStringConverter;
import de.ljw.aachen.flow.data.Account;
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
