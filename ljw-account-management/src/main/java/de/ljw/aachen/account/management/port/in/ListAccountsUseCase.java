package de.ljw.aachen.account.management.port.in;

import de.ljw.aachen.account.management.domain.Account;

import java.util.Collection;
import java.util.Set;

public interface ListAccountsUseCase {

    Set<Account> listAccounts();

}
