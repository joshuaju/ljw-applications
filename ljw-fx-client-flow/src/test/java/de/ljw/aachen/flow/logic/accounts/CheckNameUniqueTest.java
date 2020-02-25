package de.ljw.aachen.flow.logic.accounts;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.data.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class CheckNameUniqueTest {

    List<Account> accounts = List.of(
            new Account(null, "Benjamin", "Linus"),
            new Account(null, "Jack", "Shepherd")
    );

    CheckNameUnique checkNameUnique;
    AccountStore accountStoreMock;


    @BeforeEach
    void beforeEach() {
        accountStoreMock = mock(AccountStore.class);
        checkNameUnique = new CheckNameUnique(accountStoreMock);
    }

    @Test
    void checkUnique() {
        doReturn(accounts).when(accountStoreMock).getAccounts();

        var isUnique = checkNameUnique.process(new Account(null, "Benjamin", "Shepherd"));

        assertThat(isUnique).isTrue();
    }

    @Test
    void checkNotUnique() {
        doReturn(accounts).when(accountStoreMock).getAccounts();

        var isUnique = checkNameUnique.process(new Account(null, "Benjamin", "Linus"));

        assertThat(isUnique).isFalse();
    }

    @Test
    void checkFullNameComparison() {
        var accounts = List.of(new Account(null, "Benjamin Franklin", "Superstar"));
        doReturn(accounts).when(accountStoreMock).getAccounts();

        var isUnique = checkNameUnique.process(new Account(null, "benjamin", "franklin Superstar"));

        assertThat(isUnique).isFalse();
    }

}