package de.ljw.aachen.account.management.service;

import de.ljw.aachen.account.management.adapter.out.AccountStoreMem;
import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.account.management.port.in.CreateAccountUseCase;
import de.ljw.aachen.account.management.port.in.CreateAccountUseCase.CreateAccountCommand;
import de.ljw.aachen.account.management.port.in.DeleteAccountUseCase;
import de.ljw.aachen.account.management.port.in.DeleteAccountUseCase.DeleteAccountCommand;
import de.ljw.aachen.account.management.port.in.ReadAccountUseCase;
import de.ljw.aachen.account.management.port.in.ReadAccountUseCase.ReadAccountCommand;
import de.ljw.aachen.account.management.port.in.UpdateAccountUseCase;
import de.ljw.aachen.account.management.port.in.UpdateAccountUseCase.UpdateAccountCommand;
import de.ljw.aachen.account.management.port.out.AccountStorePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AccountServiceTest {

    CreateAccountUseCase createAccountService;
    DeleteAccountUseCase deleteAccountService;
    ReadAccountUseCase readAccountService;
    UpdateAccountUseCase updateAccountService;


    AccountStorePort accountStore;
    public static final Account BENJAMIN_LINUS = Account.createFor("Benjamin", "Linus");

    @BeforeEach
    void beforeEach() {
        this.accountStore = new AccountStoreMem();

        AccountService accountService = new AccountService(accountStore);
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
    }

    @Test
    void deleteAccount() {
        var createBenjamin = new CreateAccountCommand(BENJAMIN_LINUS.getFirstName(), BENJAMIN_LINUS.getLastName());
        var benjaminsId = createAccountService.createAccount(createBenjamin);

        var deleteBenjamin = new DeleteAccountCommand(benjaminsId);
        deleteAccountService.deleteAccount(deleteBenjamin);

        var readBenjamin = new ReadAccountCommand(benjaminsId);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> readAccountService.readAccount(readBenjamin));

        assertThatCode(() -> deleteAccountService.deleteAccount(deleteBenjamin))
                .doesNotThrowAnyException();

    }

    @Test
    void updateAccount(){
        var createBenjamin = new CreateAccountCommand(BENJAMIN_LINUS.getFirstName(), BENJAMIN_LINUS.getLastName());
        var benjaminsId = createAccountService.createAccount(createBenjamin);
        ReadAccountCommand readBenjamin = new ReadAccountCommand(benjaminsId);

        var benjamin = readAccountService.readAccount(readBenjamin);
        benjamin.setFirstName("Ben");
        var benjaminWithChangedName = readAccountService.readAccount(readBenjamin);
        assertThat(benjamin).isNotEqualTo(benjaminWithChangedName);

        updateAccountService.updateAccount(new UpdateAccountCommand(benjaminWithChangedName));
        var benjaminAfterUpdate = readAccountService.readAccount(readBenjamin);
        assertThat(benjaminAfterUpdate).isNotEqualTo(benjamin);
        assertThat(benjaminAfterUpdate).isEqualTo(benjaminWithChangedName);

    }

}