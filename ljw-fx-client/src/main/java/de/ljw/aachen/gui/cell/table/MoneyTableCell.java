package de.ljw.aachen.gui.cell.table;

import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
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

        var amount = money.getAmount();
        setText(MessageFormat.format("{0,number,#.##}", amount));
    }
}
