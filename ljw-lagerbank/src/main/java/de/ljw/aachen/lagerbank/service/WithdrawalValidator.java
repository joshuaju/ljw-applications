package de.ljw.aachen.lagerbank.service;

import de.ljw.aachen.lagerbank.domain.Money;

public class WithdrawalValidator {

    public static boolean isWithdrawalAllowed(Money balance, Money amountToWithdraw){
        boolean balanceWouldBeNegative = balance.getValue() < amountToWithdraw.getValue();

        return !balanceWouldBeNegative;
    }

}
