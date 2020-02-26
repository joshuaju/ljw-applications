package de.ljw.aachen.flow.app.controls;

import de.ljw.aachen.flow.data.Money;
import de.ljw.aachen.flow.data.Transaction;
import javafx.scene.control.TableCell;

import java.text.MessageFormat;

public class MoneyTableCell extends TableCell<Transaction, Money> {

    @Override
    protected void updateItem(Money money, boolean b) {
        super.updateItem(money, b);
        if (money == null){
            setText("");
            return;
        }

        var amount = money.getValue();
        setText(MessageFormat.format("{0,number, #0.00}", amount));
    }
}
