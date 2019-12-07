package de.ljw.aachen.lagerbank.adapter.out;

import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.domain.Transaction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionCSVTest {

    @ParameterizedTest
    @MethodSource("transactions")
    void convertBothWays(Transaction transaction) {
        String serialized = TransactionCSV.serialize(transaction);
        System.out.println(serialized);

        Transaction deserialized = TransactionCSV.deserialize(serialized);

        assertThat(transaction).isEqualTo(deserialized);
    }

    static Stream<Transaction> transactions() {
        AccountId peter = new AccountId();
        AccountId julia = new AccountId();
        return Stream.of(
                Transaction.forTransfer(peter, julia, Money.of(10.5)),
                Transaction.forDeposit(peter, Money.of(1)),
                Transaction.forTransfer(peter, null, Money.of(10.5)),
                Transaction.forTransfer(null, julia, Money.of(10.5)),
                Transaction.forTransfer(null, null, Money.of(10.5)),
                Transaction.forWithdrawal(julia, Money.of(100.1))
        );
    }

}