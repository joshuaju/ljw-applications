package de.ljw.aachen.flow.app;

import de.ljw.aachen.flow.adapter.AccountStore;
import de.ljw.aachen.flow.adapter.FileSystem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShowCreateAccountForm {

    private final FileSystem fs;
    private final AccountStore accountStore;

    public void process(){

    }

}
