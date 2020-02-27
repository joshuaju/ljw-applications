import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.data.Account;
import de.ljw.aachen.application.logic.CheckNameUnique;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        Assertions.assertThat(isUnique).isTrue();
    }

    @Test
    void checkNotUnique() {
        doReturn(accounts).when(accountStoreMock).getAccounts();

        var isUnique = checkNameUnique.process(new Account(null, "Benjamin", "Linus"));

        Assertions.assertThat(isUnique).isFalse();
    }

    @Test
    void checkFullNameComparison() {
        var accounts = List.of(new Account(null, "Benjamin Franklin", "Superstar"));
        doReturn(accounts).when(accountStoreMock).getAccounts();

        var isUnique = checkNameUnique.process(new Account(null, "benjamin", "franklin Superstar"));

        Assertions.assertThat(isUnique).isFalse();
    }

}