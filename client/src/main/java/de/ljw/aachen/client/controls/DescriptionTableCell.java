package de.ljw.aachen.client.controls;

import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.data.AccountId;
import de.ljw.aachen.application.data.Transaction;
import de.ljw.aachen.application.logic.ComposeFullName;
import de.ljw.aachen.application.logic.DetermineTransactionType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;

import java.util.ResourceBundle;

public class DescriptionTableCell extends TableCell<Transaction, Transaction> {

    private final ObjectProperty<Account> selectedAccountProperty;
    private final ReadOnlyListProperty<Account> accountListProperty;
    private final ResourceBundle resources;

    private VBox pane = new VBox();
    private Label title = new Label();
    private Label description = new Label();

    public DescriptionTableCell(ObjectProperty<Account> selectedAccountProperty, ReadOnlyListProperty<Account> accountListProperty, ResourceBundle resources) {
        this.selectedAccountProperty = selectedAccountProperty;
        this.accountListProperty = accountListProperty;
        this.resources = resources;

        pane.getChildren().addAll(title, description);
        setGraphic(pane);
    }

    @Override
    protected void updateItem(Transaction transaction, boolean b) {
        super.updateItem(transaction, b);
        if (transaction == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        var determineTransactionType = new DetermineTransactionType();
        determineTransactionType.setOnDeposit(t -> {
            title.setText(resources.getString("deposit"));
            description.setText(transaction.getDescription());
        });
        determineTransactionType.setOnWithdrawal(t -> {
            title.setText(resources.getString("withdrawal"));
            description.setText(transaction.getDescription());
        });
        determineTransactionType.setOnTransfer(t -> {
            var source = t.getSource();
            var target = t.getTarget();
            String title = null;
            if (source.equals(selectedAccountProperty.get().getId()))
                title = resources.getString("transfer.sent.to") + " " + ComposeFullName.process(searchAccount(target));
            else if (target.equals(selectedAccountProperty.get().getId())) {
                title = resources.getString("transfer.received.from") + " " + ComposeFullName.process(searchAccount(source));
            }
            this.title.setText(title);
            description.setText(transaction.getDescription());
        });

        determineTransactionType.process(transaction);
        setGraphic(pane);
    }

    private Account searchAccount(AccountId accountId) {
        return accountListProperty.stream()
                .filter(account -> accountId.equals(account.getId()))
                .findFirst().orElseThrow();
    }

}
