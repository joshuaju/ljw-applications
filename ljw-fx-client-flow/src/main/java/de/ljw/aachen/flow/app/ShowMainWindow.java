package de.ljw.aachen.flow.app;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.adapter.TransactionStore;
import de.ljw.aachen.flow.logic.transactions.ExecuteTransaction;

public class ShowMainWindow {

    private final FileSystem fs;
    private final AccountStore accountStore;
    private final TransactionStore transactionStore;

    private final ShowCreateAccountForm showCreateAccount;
    private final ShowEditAccountForm showEditAccount;
    private final ExecuteTransaction executeTransaction;

    public ShowMainWindow(FileSystem fs, AccountStore accountStore, TransactionStore transactionStore) {
        this.fs = fs;
        this.accountStore = accountStore;
        this.transactionStore = transactionStore;

        this.showCreateAccount = new ShowCreateAccountForm(fs, accountStore);
        this.showEditAccount = new ShowEditAccountForm(fs, accountStore);
        this.executeTransaction = new ExecuteTransaction(fs, transactionStore);
    }

    public void process() {

    }


}
