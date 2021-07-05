package de.ljw.aachen.application.data;

import lombok.Data;

import java.time.Instant;

@Data(staticConstructor = "of")
public class ExportRecord
{
    private final int id;
    private final Instant date;
    private final String type;
    private final String accountName;
    private final Money amount;
    private final String description;
    private final String info;
}
