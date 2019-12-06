package de.ljw.aachen.account.management.port.in;

import de.ljw.aachen.account.management.domain.AccountId;
import lombok.RequiredArgsConstructor;

public interface DeleteAccount {

    void deleteAccount(DeleteAccountCommand command);

    @RequiredArgsConstructor
    class DeleteAccountCommand {

        private final AccountId accountId;

    }
}
