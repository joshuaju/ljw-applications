package de.ljw.aachen.flow.adapter;

import de.ljw.aachen.flow.data.Account;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AccountStoreImpl implements AccountStore {

    private Path source;
    private List<Account> accounts;

    public AccountStoreImpl() {
        this.accounts = new LinkedList<>();
    }

    public AccountStoreImpl(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    @Override
    public Path getSource() {
        return this.source;
    }

    @Override
    public void store(Account account) {
        accounts.add(account);
    }

    @Override
    @SneakyThrows
    public void setSource(Path path) {
        if (Files.notExists(path)) Files.createFile(path);
        this.source = path;
    }
}
