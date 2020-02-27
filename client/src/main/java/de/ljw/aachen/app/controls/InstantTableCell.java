package de.ljw.aachen.app.controls;

import data.Transaction;
import javafx.scene.control.TableCell;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class InstantTableCell extends TableCell<Transaction, Instant> {

    public static final DateTimeFormatter formatter = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(Locale.GERMANY)
            .withZone(ZoneId.systemDefault());

    @Override
    protected void updateItem(Instant instant, boolean b) {
        super.updateItem(instant, b);
        if (instant == null) {
            setText("");
            return;
        }
        setText(formatter.format(instant));
    }

}
