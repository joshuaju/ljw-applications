package de.ljw.aachen.lagerbank.port.in;

import de.ljw.aachen.lagerbank.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Money;

public interface WithdrawMoneyUseCase {

    void withdraw(Money amount, AccountId accountId) throws WithdrawalNotAllowedException;

}
