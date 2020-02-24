package de.ljw.aachen.account.management.adapter.out;

import de.ljw.aachen.account.management.domain.Account;
import de.ljw.aachen.account.management.domain.AccountId;
import de.ljw.aachen.account.management.port.out.AccountStorePort;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;

public class AccountStoreCSV implements AccountStorePort {

    private final Path out;
    private final AccountStoreMem memoryStore;

    @SneakyThrows
    public AccountStoreCSV(Path out, AccountStoreMem memoryStore) {
        this.out = out;
        this.memoryStore = memoryStore;
        if (!Files.exists(out)) {
            Files.createFile(out);
        }
    }

    @Override
    @SneakyThrows
    public void store(Account account) {
        memoryStore.store(account);
        WriteAccounts.write(Files.newBufferedWriter(out, StandardOpenOption.APPEND), account);
    }

    @Override
    @SneakyThrows
    public void update(Account account) {
        memoryStore.update(account);
        WriteAccounts.write(Files.newBufferedWriter(out, StandardOpenOption.APPEND), account);
    }

    @Override
    public boolean contains(AccountId id) {
        return memoryStore.contains(id);
    }

    @Override
    public Account read(AccountId id) {
        return memoryStore.read(id);
    }

    @Override
    public Set<Account> readAll() {
        return memoryStore.readAll();
    }
}
