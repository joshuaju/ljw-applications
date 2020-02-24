package de.ljw.aachen.flow.adapter;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

public interface FileSystem {

    Writer newWriter(Path file);

    Reader newReader(Path file);

}
