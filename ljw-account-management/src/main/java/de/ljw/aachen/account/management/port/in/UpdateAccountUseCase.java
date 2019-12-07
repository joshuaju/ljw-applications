package de.ljw.aachen.account.management.port.in;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface UpdateAccountUseCase {

    void updateAccount(UpdateAccountCommand command);

    @Getter
    @RequiredArgsConstructor
    class UpdateAccountCommand {

        private final Account account;

    }
}
