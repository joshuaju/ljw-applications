package de.ljw.aachen.lagerbank.adapter.out.csv;

import de.ljw.aachen.lagerbank.domain.Transaction;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVParser;

import java.io.Reader;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ReadTransactions {

    @SneakyThrows
    public static Collection<Transaction> read(Reader data){
        try (var parser = CSVParser.parse(data, TransactionConverter.getFormat())) {
            return StreamSupport.stream(parser.spliterator(), false)
                    .map(TransactionConverter::convertToTransaction)
                    .collect(Collectors.toList());

        }
    }
}
