package de.ljw.aachen.account.management.adapter.out;

import de.ljw.aachen.account.management.port.out.AccountStorePort;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;

public class InitializeAccountStoreFromCSV {

    @SneakyThrows
    public static AccountStoreMem init(Path source) {
        AccountStoreMem accountStore = new AccountStoreMem();
        var accounts = ReadAccounts.read(Files.newBufferedReader(source));
        accounts.forEach(account -> {
            boolean alreadyStore = accountStore.contains(account.getId());
            if (alreadyStore) accountStore.update(account);
            else accountStore.store(account);
        });
        return accountStore;
    }
}
