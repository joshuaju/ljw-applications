package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.FileSystem;
import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.application.data.ExportRecord;
import de.ljw.aachen.application.data.Transaction;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ExportAllTransactions
{

    private static final String TYPE_EINZAHLUNG = "EINZAHLUNG";
    private static final String TYPE_AUSZAHLUNG = "AUSZAHLUNG";

    private final FileSystem fs;
    private final TransactionStore transactionStore;
    private final AccountStore accountStore;

    public void export(Path out)
    {
        AtomicInteger transactionNumber = new AtomicInteger(0);

        List<String> outLines = new LinkedList<>();
        transactionStore.getTransactions().stream()
                        .flatMap(t -> toRecord(transactionNumber.getAndIncrement(), t))
                        .map(ExportRecordConverter::toString)
                        .forEach(outLines::add);

        outLines.add(0, ExportRecordConverter.getHeader());
        fs.writeLines(out, outLines);
    }

    private Stream<ExportRecord> toRecord(int nextTransactionNumber, Transaction transaction)
    {
        var determine = new DetermineTransactionType();
        List<ExportRecord> records = new LinkedList<>();

        determine.setOnDeposit(t -> {
            records.add(ExportRecord.of(nextTransactionNumber, t.getTime(), TYPE_EINZAHLUNG,
                    ComposeFullName.process(accountStore.find(t.getTarget())), t.getAmount(), t.getDescription()));
        });

        determine.setOnWithdrawal(t -> {
            records.add(ExportRecord.of(nextTransactionNumber, t.getTime(), TYPE_AUSZAHLUNG,
                    ComposeFullName.process(accountStore.find(t.getSource())), t.getAmount(), t.getDescription()));
        });

        determine.setOnTransfer(t -> {
            records.add(ExportRecord.of(nextTransactionNumber, t.getTime(), TYPE_AUSZAHLUNG,
                    ComposeFullName.process(accountStore.find(t.getSource())), t.getAmount(), t.getDescription()));
            records.add(ExportRecord.of(nextTransactionNumber, t.getTime(), TYPE_EINZAHLUNG,
                    ComposeFullName.process(accountStore.find(t.getTarget())), t.getAmount(), t.getDescription()));
        });

        determine.process(transaction);
        return records.stream();
    }

}
