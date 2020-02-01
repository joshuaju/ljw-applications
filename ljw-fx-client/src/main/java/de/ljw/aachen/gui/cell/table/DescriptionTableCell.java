package de.ljw.aachen.gui.cell.table;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.account.management.port.in.ReadAccountUseCase;
import de.ljw.aachen.lagerbank.domain.Transaction;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TableCell;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class DescriptionTableCell extends TableCell<Transaction, Transaction> {

    private final ObjectProperty<Account> selectedAccountProperty;
    private final Function<AccountId, Account> accountProvider;

    @Override
    protected void updateItem(Transaction transaction, boolean b) {
        super.updateItem(transaction, b);
        if (transaction == null){
            setText("");
            return;
        }

        String value = "";
        switch (transaction.getType()) {
            case Deposit:
                value = "Deposit";
                break;
            case Withdrawal:
                value = "Withdrawal";
                break;
            case Transfer:
                String direction = null;
                AccountId accountId = null;
                if (transaction.getSource().equals(selectedAccountProperty.getValue().getId())) {
                    direction = "Sent to";
                    accountId = transaction.getTarget();
                } else {
                    direction = "Received from";
                    accountId = transaction.getSource();
                }
                Account otherAccount = accountProvider.apply(accountId);
                value = String.format("%s %s %s", direction, otherAccount.getFirstName(), otherAccount.getLastName());
                break;
        }
        setText(value);
    }
}
