package de.ljw.aachen.client.controls;

import de.ljw.aachen.application.data.Balance;
import de.ljw.aachen.application.data.Money;
import javafx.scene.control.TableCell;

public class BalanceValueTableCell extends TableCell<Balance, Money>
{
    @Override
    protected void updateItem(Money item, boolean empty)
    {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            setStyle("");
        } else {
            setText(item.formatWithCurrency());
            if (item.isLessThan(new Money(0)))
                setStyle("-fx-background-color: orange");
            else
                setStyle("");
        }

    }
}
