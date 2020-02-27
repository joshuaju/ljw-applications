package de.ljw.aachen.client.controls;

import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.data.AccountId;
import de.ljw.aachen.application.data.Transaction;
import de.ljw.aachen.application.logic.DetermineTransactionType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.scene.control.TableCell;
import lombok.RequiredArgsConstructor;

import java.util.ResourceBundle;

@RequiredArgsConstructor
public class DescriptionTableCell extends TableCell<Transaction, Transaction> {

    private final ObjectProperty<Account> selectedAccountProperty;
    private final ReadOnlyListProperty<Account> accountListProperty;
    private final ResourceBundle resources;

    @Override
    protected void updateItem(Transaction transaction, boolean b) {
        super.updateItem(transaction, b);
        if (transaction == null) {
            setText("");
            return;
        }

        var determineTransactionType = new DetermineTransactionType();
        determineTransactionType.setOnDeposit(t -> setText(resources.getString("deposit")));
        determineTransactionType.setOnWithdrawal(t -> setText(resources.getString("withdrawal")));
        determineTransactionType.setOnTransfer(t -> {
            var source = t.getSource();
            var target = t.getTarget();

            if (source.equals(selectedAccountProperty.get().getId()))
                setText(resources.getString("transfer.sent.to")+ " " + target);
            else if (target.equals(selectedAccountProperty.get().getId())){
                setText(resources.getString("transfer.received.from")+ " " + source);
            }
        });

        setText("");
        determineTransactionType.process(transaction);
    }

    private Account searchAccount(AccountId accountId) {
        return accountListProperty.stream()
                .filter(account -> accountId.equals(account.getId()))
                .findFirst().orElseThrow();
    }
}
