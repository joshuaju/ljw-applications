package de.ljw.aachen.application.adapter;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;

public interface FileSystem {

    Writer newWriter(Path file);

    Reader newReader(Path file);

    void appendLine(Path destination, String line);

    void writeLines(Path destination, Collection<String> line);

    List<String> readLines(Path destination);

}
