package de.ljw.aachen.application.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ColumnMismatchException extends RuntimeException implements LocalizedException {

    private final String titleKey = "error.title.columns.mismatch";
    private final String detailKey = "error.detail.columns.mismatch";

}
