package de.ljw.aachen.lagerbank.port.in;

import de.ljw.aachen.lagerbank.domain.AccountId;
import de.ljw.aachen.lagerbank.domain.Money;

public interface TransferMoneyUseCase {

    void transfer(Money amount, AccountId sourceId, AccountId targetId) throws WithdrawalNotAllowedException;

}
