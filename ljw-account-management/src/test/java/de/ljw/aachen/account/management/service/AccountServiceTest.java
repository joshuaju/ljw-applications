package de.ljw.aachen.account.management.service;

import de.ljw.aachen.account.management.adapter.out.AccountStoreMem;
import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.account.management.domain.event.AccountCreatedEvent;
import de.ljw.aachen.account.management.domain.event.AccountDeletedEvent;
import de.ljw.aachen.account.management.domain.event.AccountUpdatedEvent;
import de.ljw.aachen.account.management.port.in.CreateAccountUseCase;
import de.ljw.aachen.account.management.port.in.CreateAccountUseCase.CreateAccountCommand;
import de.ljw.aachen.account.management.port.in.DeleteAccountUseCase;
import de.ljw.aachen.account.management.port.in.DeleteAccountUseCase.DeleteAccountCommand;
import de.ljw.aachen.account.management.port.in.ReadAccountUseCase;
import de.ljw.aachen.account.management.port.in.ReadAccountUseCase.ReadAccountCommand;
import de.ljw.aachen.account.management.port.in.UpdateAccountUseCase;
import de.ljw.aachen.account.management.port.in.UpdateAccountUseCase.UpdateAccountCommand;
import de.ljw.aachen.account.management.port.out.AccountStorePort;
import de.ljw.aachen.common.EventPort;
import lombok.RequiredArgsConstructor;
import org.assertj.core.description.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class AccountServiceTest {

    CreateAccountUseCase createAccountService;
    DeleteAccountUseCase deleteAccountService;
    ReadAccountUseCase readAccountService;
    UpdateAccountUseCase updateAccountService;


    AccountStorePort accountStore;

    EventPort eventPortMock;
    public static final Account BENJAMIN_LINUS = Account.createFor("Benjamin", "Linus");
    public static final Account JULIA_JULIETTE = Account.createFor("Julia", "Juliette");

    @BeforeEach
    void beforeEach() {
        this.accountStore = new AccountStoreMem();
        this.eventPortMock = mock(EventPort.class);

        AccountService accountService = new AccountService(accountStore, eventPortMock);
        this.createAccountService = accountService;
        this.deleteAccountService = accountService;
        this.readAccountService = accountService;
        this.updateAccountService = accountService;
    }

    @Test
    void createAccount() {
        var createBenjamin = new CreateAccountCommand(BENJAMIN_LINUS.getFirstName(), BENJAMIN_LINUS.getLastName());

        var benjaminsId = createAccountService.createAccount(createBenjamin);

        var benjamin = readAccountService.readAccount(new ReadAccountCommand(benjaminsId));
        assertThat(benjamin.getFirstName()).isEqualTo(BENJAMIN_LINUS.getFirstName());
        assertThat(benjamin.getLastName()).isEqualTo(BENJAMIN_LINUS.getLastName());
        verify(eventPortMock).publish(any(AccountCreatedEvent.class));
    }

    @Test
    void createAccountWithIdenticalForeAndLastName() {
        var createBenjamin = new CreateAccountCommand(BENJAMIN_LINUS.getFirstName(), BENJAMIN_LINUS.getLastName());
        assertThatCode(() -> createAccountService.createAccount(createBenjamin))
                .doesNotThrowAnyException();
        assertThatIllegalArgumentException()
                .isThrownBy(() -> createAccountService.createAccount(createBenjamin));
    }

    @Test
    void updateAccountToBeIdenticalWithAnotherAccount() {
        var createBenjamin = new CreateAccountCommand(BENJAMIN_LINUS.getFirstName(), BENJAMIN_LINUS.getLastName());
        var createJulia = new CreateAccountUseCase.CreateAccountCommand(JULIA_JULIETTE.getFirstName(), JULIA_JULIETTE.getLastName());

        createAccountService.createAccount(createBenjamin);
        AccountId juliasId = createAccountService.createAccount(createJulia);
        Account julia = readAccountService.readAccount(new ReadAccountCommand(juliasId));
        julia.setFirstName(BENJAMIN_LINUS.getFirstName());
        julia.setLastName(BENJAMIN_LINUS.getLastName());

        var renameJuliaToBen = new UpdateAccountCommand(julia);
        assertThatIllegalArgumentException()
                .as("Renaming an account to the same first and last name like another account should not be possible")
                .isThrownBy(() -> updateAccountService.updateAccount(renameJuliaToBen));
    }

    @Test
    void deleteAccount() {
        var createBenjamin = new CreateAccountCommand(BENJAMIN_LINUS.getFirstName(), BENJAMIN_LINUS.getLastName());
        var benjaminsId = createAccountService.createAccount(createBenjamin);

        var deleteBenjamin = new DeleteAccountCommand(benjaminsId);
        deleteAccountService.deleteAccount(deleteBenjamin);

        verify(eventPortMock).publish(any(AccountDeletedEvent.class));

        var readBenjamin = new ReadAccountCommand(benjaminsId);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> readAccountService.readAccount(readBenjamin));
        assertThatCode(() -> deleteAccountService.deleteAccount(deleteBenjamin))
                .doesNotThrowAnyException();
    }

    @Test
    void updateAccount() {
        var createBenjamin = new CreateAccountCommand(BENJAMIN_LINUS.getFirstName(), BENJAMIN_LINUS.getLastName());
        var benjaminsId = createAccountService.createAccount(createBenjamin);
        ReadAccountCommand readBenjamin = new ReadAccountCommand(benjaminsId);

        var benjamin = readAccountService.readAccount(readBenjamin);
        benjamin.setFirstName("Ben");

        var benjaminAfterChangingName = readAccountService.readAccount(readBenjamin);
        assertThat(benjamin).isNotEqualTo(benjaminAfterChangingName);

        updateAccountService.updateAccount(new UpdateAccountCommand(benjaminAfterChangingName));
        verify(eventPortMock).publish(any(AccountUpdatedEvent.class));

        var benjaminAfterUpdate = readAccountService.readAccount(readBenjamin);
        assertThat(benjaminAfterUpdate).isNotEqualTo(benjamin);
        assertThat(benjaminAfterUpdate).isEqualTo(benjaminAfterChangingName);
    }

}