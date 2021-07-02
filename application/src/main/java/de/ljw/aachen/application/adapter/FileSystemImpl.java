package de.ljw.aachen.application.adapter;

import lombok.SneakyThrows;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;

public class FileSystemImpl implements FileSystem
{
    @Override
    @SneakyThrows
    public Writer newWriter(Path path)
    {
        return Files.newBufferedWriter(path, StandardOpenOption.APPEND);
    }

    @Override
    @SneakyThrows
    public Reader newReader(Path path)
    {
        return Files.newBufferedReader(path);
    }

    @Override
    @SneakyThrows
    public void appendLine(Path destination, String line)
    {
        if (Files.notExists(destination)) {
            Files.createFile(destination);
        }
        Files.write(destination, List.of(line), StandardOpenOption.APPEND);
    }

    @Override
    @SneakyThrows
    public void writeLines(Path destination, Collection<String> lines)
    {
        Files.deleteIfExists(destination);
        Files.createFile(destination);
        Files.write(destination, lines);
    }

    @Override
    @SneakyThrows
    public List<String> readLines(Path destination)
    {
        return Files.readAllLines(destination);
    }
}
