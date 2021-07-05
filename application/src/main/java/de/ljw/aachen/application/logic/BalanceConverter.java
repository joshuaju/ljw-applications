package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.data.Balance;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;

@RequiredArgsConstructor
class BalanceConverter
{

    private static final String DELIMITER = ";";

    public static String getHeader()
    {
        return String.join(DELIMITER, "Vorname", "Nachname", "Guthaben");
    }

    public static String toString(Balance balance)
    {
        return String.join(DELIMITER,
                balance.getFirstName(),
                balance.getLastName(),
                MessageFormat.format("{0,number,#.##}", balance.getValue().getValue()).replace(".", ",")
        );
    }

}
