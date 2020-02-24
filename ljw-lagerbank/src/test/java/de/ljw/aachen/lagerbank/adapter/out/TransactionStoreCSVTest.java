package de.ljw.aachen.lagerbank.adapter.out;

import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.lagerbank.adapter.out.csv.ReadTransactions;
import de.ljw.aachen.lagerbank.adapter.out.csv.TransactionStoreCSV;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.port.in.GetBalanceUseCase;
import de.ljw.aachen.lagerbank.port.out.TransactionStorePort;
import de.ljw.aachen.lagerbank.service.BalanceService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionStoreCSVTest {

    private final AccountId peter = new AccountId();
    private final AccountId julia = new AccountId();

    private static Path out = Path.of("transactions.csv");

    @BeforeAll
    @SneakyThrows
    static void beforeEach() {
        Files.createFile(out);
    }

    @SneakyThrows
    TransactionStoreMem newInMemoryStore() {
        var transactions = ReadTransactions.read(Files.newBufferedReader(out));
        return new TransactionStoreMem(transactions);
    }

    @Test
    void interactWithStore() {
        TransactionStorePort transactionCSVStore = new TransactionStoreCSV(out, newInMemoryStore());
        assertThat(transactionCSVStore.getAll()).isEmpty();

        Transaction petersFirstDeposit = Transaction.forDeposit(peter, Money.of(15.0));
        transactionCSVStore.add(petersFirstDeposit);
        assertThat(transactionCSVStore.getAll()).containsOnly(petersFirstDeposit);

        transactionCSVStore = new TransactionStoreCSV(out, newInMemoryStore());
        assertThat(transactionCSVStore.getAll()).containsOnly(petersFirstDeposit);

        Transaction juliasFirstDeposit = Transaction.forDeposit(julia, Money.of(5.0));
        transactionCSVStore.add(juliasFirstDeposit);
        Transaction peterTransfersToJulia = Transaction.forTransfer(peter, julia, Money.of(10.0));
        transactionCSVStore.add(peterTransfersToJulia);
        assertThat(transactionCSVStore.getAll())
                .containsExactly(petersFirstDeposit, juliasFirstDeposit, peterTransfersToJulia);

        transactionCSVStore = new TransactionStoreCSV(out, newInMemoryStore());
        GetBalanceUseCase balanceService = new BalanceService(transactionCSVStore);
        assertThat(balanceService.getBalance(julia)).isEqualTo(Money.of(15.0));
        assertThat(balanceService.getBalance(peter)).isEqualTo(Money.of(5.0));
    }

    @AfterEach
    @SneakyThrows
    void afterEach() {
        Files.delete(out);
    }


}