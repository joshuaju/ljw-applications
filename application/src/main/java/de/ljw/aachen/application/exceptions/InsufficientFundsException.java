package de.ljw.aachen.application.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InsufficientFundsException extends RuntimeException implements LocalizedException {

    private final String titleKey = "error.title.insufficient.funds";
    private final String detailKey = "error.detail.insufficient.funds";

}
