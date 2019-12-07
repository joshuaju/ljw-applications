package de.ljw.aachen.lagerbank.adapter.out;

import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import de.ljw.aachen.lagerbank.port.in.GetBalanceUseCase;
import de.ljw.aachen.lagerbank.port.out.TransactionStorePort;
import de.ljw.aachen.lagerbank.service.BalanceService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionCSVStoreTest {

    private final AccountId peter = new AccountId();
    private final AccountId julia = new AccountId();

    private static Path out = Path.of("transactions.csv");
    private TransactionStorePort transactionCSVStore;

    @Test
    void interactWithStore() {
        transactionCSVStore = new TransactionCSVStore(out);
        assertThat(transactionCSVStore.getAll()).isEmpty();

        Transaction petersFirstDeposit = Transaction.forDeposit(peter, Money.of(15.0));
        transactionCSVStore.add(petersFirstDeposit);
        assertThat(transactionCSVStore.getAll()).containsOnly(petersFirstDeposit);

        transactionCSVStore = new TransactionCSVStore(out);
        assertThat(transactionCSVStore.getAll()).containsOnly(petersFirstDeposit);

        Transaction juliasFirstDeposit = Transaction.forDeposit(julia, Money.of(5.0));
        transactionCSVStore.add(juliasFirstDeposit);
        Transaction peterTransfersToJulia = Transaction.forTransfer(peter, julia, Money.of(10.0));
        transactionCSVStore.add(peterTransfersToJulia);
        assertThat(transactionCSVStore.getAll())
                .containsExactly(petersFirstDeposit, juliasFirstDeposit, peterTransfersToJulia);

        transactionCSVStore = new TransactionCSVStore(out);
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