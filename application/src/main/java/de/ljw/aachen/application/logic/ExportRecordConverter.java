package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.data.ExportRecord;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@RequiredArgsConstructor
class ExportRecordConverter
{

    private static final String DELIMITER = ";";

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm:ss")
                             .withLocale(Locale.GERMAN)
                             .withZone(ZoneId.systemDefault());

    private final AccountStore accountStore;

    public static String getHeader()
    {
        return String.join(DELIMITER, "id", "Datum", "Typ", "Konto", "Betrag", "Betreff");
    }

    public static String toString(ExportRecord record)
    {
        return String.join(DELIMITER,
                String.valueOf(record.getId()),
                formatter.format(record.getDate()),
                record.getType(),
                record.getAccountName(),
                MessageFormat.format("{0,number,#.##}", record.getAmount().getValue()).replace(".", ","),
                record.getDescription()
        );
    }

}
