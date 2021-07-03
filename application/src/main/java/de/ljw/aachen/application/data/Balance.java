package de.ljw.aachen.application.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Balance
{
    private final Account account;
    private final Money value;

    public String getFirstName()
    {
        return account.getFirstName();
    }

    public String getLastName()
    {
        return account.getLastName();
    }

}
