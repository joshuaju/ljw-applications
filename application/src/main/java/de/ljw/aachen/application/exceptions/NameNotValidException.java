package de.ljw.aachen.application.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NameNotValidException extends RuntimeException implements LocalizedException {

    private final String titleKey = "error.title.name.invalid";
    private final String detailKey = "error.detail.name.invalid";
}
