package de.ljw.aachen.account.management.port.in;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;
import lombok.RequiredArgsConstructor;

public interface ReadAccount {

    Account readAccount(ReadAccountCommand command);

    @RequiredArgsConstructor
    class ReadAccountCommand {

        private final AccountId accountId;

    }
}
