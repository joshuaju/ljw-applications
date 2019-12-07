package de.ljw.aachen.account.management.port.in;

import de.ljw.aachen.account.management.domain.AccountId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface CreateAccountUseCase {

    AccountId createAccount(CreateAccountCommand command);

    @Getter
    @RequiredArgsConstructor
    class CreateAccountCommand {

        private final String firstName;
        private final String lastName;
        
    }
}
