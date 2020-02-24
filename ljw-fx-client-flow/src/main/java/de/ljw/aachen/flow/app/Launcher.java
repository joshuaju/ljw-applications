package de.ljw.aachen.flow.app;

import de.ljw.aachen.flow.adapter.*;
import lombok.RequiredArgsConstructor;

public class Launcher {


    public static void main(String[] args) {
        FileSystem fs = new FileSystemImpl();
        AccountStore accountStore = new AccountStoreImpl();
        TransactionStore transactionStore = new TransactionStoreImpl();

        var initializeApp = new InitializeApp(fs, accountStore, transactionStore);
        var showMainWindow = new ShowMainWindow(fs, accountStore, transactionStore);

        initializeApp.setOnInitialized(showMainWindow::process);
    }
}
