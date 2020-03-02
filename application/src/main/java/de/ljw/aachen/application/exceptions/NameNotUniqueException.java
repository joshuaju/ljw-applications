package de.ljw.aachen.application.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NameNotUniqueException extends RuntimeException implements LocalizedException {

    private final String titleKey = "error.title.name.not.unique";
    private final String detailKey = "error.detail.name.not.unique";

}
