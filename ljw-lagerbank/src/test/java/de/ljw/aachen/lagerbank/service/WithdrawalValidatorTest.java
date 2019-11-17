package de.ljw.aachen.lagerbank.service;

import de.ljw.aachen.lagerbank.domain.Money;
import de.ljw.aachen.lagerbank.port.in.WithdrawalNotAllowedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WithdrawalValidatorTest {

    @Test
    void withdrawTooMuch() {
        boolean withdrawalAllowed = WithdrawalValidator.isWithdrawalAllowed(Money.of(10.0), Money.of(10.01));
        assertThat(withdrawalAllowed).isFalse();
    }

    @Test
    void withdrawEverything() {
        boolean withdrawalAllowed = WithdrawalValidator.isWithdrawalAllowed(Money.of(10.0), Money.of(10.00));
        assertThat(withdrawalAllowed).isTrue();
    }

}