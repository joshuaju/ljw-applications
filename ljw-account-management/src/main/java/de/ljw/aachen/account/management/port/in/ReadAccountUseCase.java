package de.ljw.aachen.account.management.port.in;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface ReadAccountUseCase {

    Account readAccount(ReadAccountCommand command);

    @Getter
    @RequiredArgsConstructor
    class ReadAccountCommand {

        private final AccountId accountId;

    }
}
