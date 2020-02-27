package de.ljw.aachen.app.controls;

import data.Transaction;
import data.Money;
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
