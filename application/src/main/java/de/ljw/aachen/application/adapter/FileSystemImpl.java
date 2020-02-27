package de.ljw.aachen.application.adapter;

import lombok.SneakyThrows;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileSystemImpl implements FileSystem {
    @Override
    @SneakyThrows
    public Writer newWriter(Path path) {
        return Files.newBufferedWriter(path, StandardOpenOption.APPEND);
    }

    @Override
    @SneakyThrows
    public Reader newReader(Path path) {
        return Files.newBufferedReader(path);
    }
}
