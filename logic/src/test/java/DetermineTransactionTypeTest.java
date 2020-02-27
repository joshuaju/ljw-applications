import data.AccountId;
import data.Transaction;
import logic.DetermineTransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DetermineTransactionTypeTest {

    Boolean isDeposit;
    Boolean isWithdrawal;
    Boolean isTransfer;

    DetermineTransactionType determineTransactionType;

    @BeforeEach
    void beforeEach() {
        isDeposit = false;
        isWithdrawal = false;
        isTransfer = false;

        determineTransactionType = new DetermineTransactionType();
        determineTransactionType.setOnDeposit(t -> isDeposit = true);
        determineTransactionType.setOnWithdrawal(t -> isWithdrawal = true);
        determineTransactionType.setOnTransfer(t -> isTransfer = true);
    }

    @Test
    void determineDeposit() {
        var deposit = new Transaction(null, null, new AccountId(), null, null);
        determineTransactionType.process(deposit);
        assertThat(isDeposit).isTrue();
    }

    @Test
    void determineWitdrawal() {
        var withdrawal = new Transaction(null, new AccountId(), null, null, null);
        determineTransactionType.process(withdrawal);
        assertThat(isWithdrawal).isTrue();
    }

    @Test
    void determineTransfer() {
        var transfer = new Transaction(null, new AccountId(), new AccountId(), null, null);
        determineTransactionType.process(transfer);
        assertThat(isTransfer).isTrue();
    }

    @Test
    void determineError() {
        var invalidTransaction = new Transaction(null, null, null, null, null);
        assertThatIllegalArgumentException().isThrownBy(() -> determineTransactionType.process(invalidTransaction));
        assertThat(isDeposit).isFalse();
        assertThat(isWithdrawal).isFalse();
        assertThat(isTransfer).isFalse();
    }

}