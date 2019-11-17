package de.ljw.aachen.lagerbank.domain;

import lombok.*;

import java.util.Currency;

@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode
@ToString
public class Money {

    @Getter
    private final Double value;

    public Money plus(Money amount) {
        return new Money(this.value + amount.value);

    }

    public Money minus(Money amount) {
        return new Money(this.value - amount.value);
    }

}
