package de.ljw.aachen.gui.cell.list;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.gui.converter.AccountStringConverter;
import javafx.scene.control.ListCell;

public class AccountListCell extends ListCell<Account> {

    private AccountStringConverter accountStringConverter = new AccountStringConverter();

    @Override
    protected void updateItem(Account account, boolean b) {
        super.updateItem(account, b);
        if (account == null) {
            setText("");
        }
        setText(accountStringConverter.toString(account));
    }
}
