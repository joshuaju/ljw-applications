package de.ljw.aachen.application.adapter;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;

public interface FileSystem {

    Writer newWriter(Path file);

    Reader newReader(Path file);

    void writeLine(Path destination, String line);

    List<String> readLines(Path destination);

}
