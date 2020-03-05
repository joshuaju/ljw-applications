package de.ljw.aachen.client.controls;

import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.data.Money;
import de.ljw.aachen.application.data.Transaction;
import de.ljw.aachen.application.logic.DetermineTransactionType;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TableCell;

import java.text.MessageFormat;

public class MoneyTableCell extends TableCell<Transaction, Transaction> {

    private final ObjectProperty<Account> selectedAccountProperty;

    public MoneyTableCell(ObjectProperty<Account> selectedAccountProperty) {
        this.selectedAccountProperty = selectedAccountProperty;
        setStyle("-fx-alignment: BOTTOM-RIGHT;");
    }

    @Override
    protected void updateItem(Transaction transaction, boolean b) {
        super.updateItem(transaction, b);
        if (transaction == null) {
            setText("");
            return;
        }

        if (transaction.getSource()!=null && transaction.getSource().equals(selectedAccountProperty.get().getId()))
            setText(MessageFormat.format("-{0,number, #0.00}", transaction.getAmount().getValue()));
        else
            setText(MessageFormat.format("+{0,number, #0.00}", transaction.getAmount().getValue()));
    }
}
