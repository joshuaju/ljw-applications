package de.ljw.aachen.client.exception;

import de.ljw.aachen.application.exceptions.LocalizedException;
import lombok.Getter;

@Getter
public class ValidationException extends IllegalArgumentException implements LocalizedException {

    private final String titleKey = "error.title.validation.failed";
    private String detailKey;

    public ValidationException(String detailKey) {
        super(detailKey);
        this.detailKey = detailKey;
    }
}
