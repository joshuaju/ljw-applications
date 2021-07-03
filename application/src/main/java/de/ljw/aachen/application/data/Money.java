package de.ljw.aachen.application.data;

import lombok.Data;
import org.joda.money.CurrencyUnit;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Currency;
import java.util.Locale;

@Data
public class Money implements Comparable<Money>
{


    private org.joda.money.Money value;

    public Money()
    {
        this(0.00);
    }

    public Money(double amount)
    {
        this.value = org.joda.money.Money.of(CurrencyUnit.EUR, amount);
    }

    public Money(Money money)
    {
        this.value = money.value;
    }

    public Money(org.joda.money.Money money)
    {
        this.value = money;
    }

    public BigDecimal getValue()
    {
        return value.getAmount();
    }

    public Money negated()
    {
        return new Money(value.negated());
    }

    public Money plus(Money amount)
    {
        var sum = this.value.plus(amount.value);
        return new Money(sum);
    }

    public Money minus(Money amount)
    {
        var sum = this.value.minus(amount.value);
        return new Money(sum);
    }

    private boolean isGreaterThan(Money money)
    {
        return this.value.isGreaterThan(money.value);
    }

    private boolean isEqualTo(Money money)
    {
        return this.value.isEqual(money.value);
    }

    public boolean isGreaterThanOrEqual(Money money)
    {
        return isGreaterThan(money) || isEqualTo(money);
    }

    public boolean isLessThan(Money money)
    {
        return this.value.isLessThan(money.value);
    }

    public String formatWithCurrency()
    {
        var currencySymbol = Currency.getInstance(Locale.getDefault(Locale.Category.FORMAT)).getSymbol();
        return MessageFormat.format("{0,number, #0.00}{1}", getValue(), currencySymbol);
    }


    @Override
    public int compareTo(Money o)
    {
        return value.compareTo(o.value);
    }
}
