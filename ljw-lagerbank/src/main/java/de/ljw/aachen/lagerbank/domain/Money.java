package de.ljw.aachen.lagerbank.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.joda.money.CurrencyUnit;

import java.math.BigDecimal;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class Money {


    private final org.joda.money.Money value;

    public BigDecimal getAmount(){
        return value.getAmount();
    }

    public Money plus(Money amount) {
        var sum = this.value.plus(amount.value);
        return new Money(sum);
    }

    public Money minus(Money amount) {
        var sum = this.value.minus(amount.value);
        return new Money(sum);
    }

    public boolean isLessThan(Money money) {
        return this.value.isLessThan(money.value);
    }

    public boolean isEqualTo(Money money) {
        return this.value.isEqual(money.value);
    }

    public boolean isLessThanOrEqualTo(Money money){
        return this.isLessThan(money) || this.isEqualTo(money);
    }

    public boolean isGreaterThan(Money money) {
        return this.value.isGreaterThan(money.value);
    }

    public static Money of(double amount) {
        var value = org.joda.money.Money.of(CurrencyUnit.EUR, amount);
        return new Money(value);
    }

}
