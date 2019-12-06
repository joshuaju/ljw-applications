package de.ljw.aachen.account.management.port.in;

import de.ljw.aachen.account.management.domain.AccountId;
import lombok.RequiredArgsConstructor;

public interface UpdateAccount {

    AccountId updateAccount(UpdateAccountCommand command);

    @RequiredArgsConstructor
    class UpdateAccountCommand {

        private final AccountId accountId;

    }
}
