package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.data.Balance;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class ExportAllBalances
{

    private final FileSystem fs;
    private final ListAllBalances listAllBalances;

    public void export(Path out)
    {
        List<String> outLines = new LinkedList<>();
        listAllBalances.listAll().stream()
                       .sorted(Comparator.comparing(Balance::getFirstName, String::compareToIgnoreCase)
                                         .thenComparing(Balance::getLastName, String::compareToIgnoreCase))
                       .map(BalanceConverter::toString)
                       .forEach(outLines::add);

        outLines.add(0, BalanceConverter.getHeader());
        fs.writeLines(out, outLines);
    }

}
