package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.AccountStore;
import de.ljw.aachen.application.adapter.TransactionStore;
import de.ljw.aachen.application.data.Balance;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ListAllBalances
{
    private final AccountStore accounts;
    private final TransactionStore transactions;

    public List<Balance> listAll()
    {
        var accounts = this.accounts.getAccounts();
        var transactions = this.transactions.getTransactions();

        List<Balance> all = new ArrayList<>();
        for (var acc : accounts) {
            var balance = CalculateBalance.process(acc.getId(), transactions);
            all.add(new Balance(acc, balance));
        }

        return all;
    }

}
