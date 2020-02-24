package de.ljw.aachen.flow.app;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.adapter.FileSystem;
import de.ljw.aachen.flow.data.Account;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShowEditAccountForm {

    private final FileSystem fs;
    private final AccountStore accountStore;

    public void process(Account account){

    }

}
