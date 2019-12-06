package de.ljw.aachen.account.management.port.in;

import de.ljw.aachen.account.management.domain.AccountId;
import lombok.RequiredArgsConstructor;

public interface CreateAccount {

    AccountId createAccount(CreateAccountCommand command);

    @RequiredArgsConstructor
    class CreateAccountCommand {
        
    }
}
