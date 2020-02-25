package de.ljw.aachen.flow.app.fx.cell.table;

import de.ljw.aachen.flow.data.Account;
import de.ljw.aachen.flow.data.AccountId;
import de.ljw.aachen.flow.data.Transaction;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TableCell;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DescriptionTableCell extends TableCell<Transaction, Transaction> {

    private final ObjectProperty<Account> selectedAccountProperty;
    private final ListProperty<Account> accountListProperty;

    @Override
    protected void updateItem(Transaction transaction, boolean b) {
        super.updateItem(transaction, b);
        if (transaction == null) {
            setText("");
            return;
        }

        String value = "DUMMY";
        /*switch (transaction.getType()) {
            case Deposit:
                value = "Deposit";
                break;
            case Withdrawal:
                value = "Withdrawal";
                break;
            case Transfer:
                String direction = null;
                AccountId otherAccountId = null;
                if (transaction.getSource().equals(selectedAccountProperty.getValue().getId())) {
                    direction = "Sent to";
                    otherAccountId = transaction.getTarget();
                } else {
                    direction = "Received from";
                    otherAccountId = transaction.getSource();
                }
                Account otherAccount = searchAccount(otherAccountId);
                value = String.format("%s %s %s", direction, otherAccount.getFirstName(), otherAccount.getLastName());
                break;
        }*/
        setText(value);
    }

    private Account searchAccount(AccountId accountId) {
        return accountListProperty.stream()
                .filter(account -> accountId.equals(account.getId()))
                .findFirst().orElseThrow();
    }
}
