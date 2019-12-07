package de.ljw.aachen.account.management.port.in;

import de.ljw.aachen.account.management.domain.AccountId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface DeleteAccountUseCase {

    void deleteAccount(DeleteAccountCommand command);

    @Getter
    @RequiredArgsConstructor
    class DeleteAccountCommand {

        private final AccountId accountId;

    }
}
