package de.ljw.aachen.application.logic;

import de.ljw.aachen.application.adapter.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ImportAccountFromFileTest {

    FileSystem fs = new FileSystem() {
        @Override
        public Writer newWriter(Path file) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Reader newReader(Path file) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeLine(Path destination, String line) {
            // do nothing
        }

        @Override
        public List<String> readLines(Path destination) {
            return linesFromFile;
        }
    };

    private AccountStore accountStore;
    private TransactionStore transactionStore;
    private List<String> linesFromFile;
    private ImportAccountFromFile fileImporter;


    @BeforeEach
    void beforeEach() {
        linesFromFile = null;
        accountStore = new AccountStoreImpl(new ArrayList<>());
        transactionStore = new TransactionStoreImpl(new ArrayList<>());
        fileImporter = new ImportAccountFromFile(fs, accountStore, transactionStore, msg -> { /* do nothing */ });
    }

    @Test
    void importEmptyFile() {
        linesFromFile = List.of();

        fileImporter.importFile(Path.of("any"), "test deposit");

        assertThat(accountStore.getAccounts()).isEmpty();
        assertThat(transactionStore.getTransactions()).isEmpty();
    }

    @Test
    void importOnlyHeader() {
        linesFromFile = List.of("first name, last name, balance");

        fileImporter.importFile(Path.of("any"), "test deposit");

        assertThat(accountStore.getAccounts()).isEmpty();
        assertThat(transactionStore.getTransactions()).isEmpty();
    }

    @Test
    void importAccounts() {
        linesFromFile = List.of(
                "first name, last name, balance",
                "Benjamin, Linus, 10.",
                "Julia, Juliette, 25.50"
        );

        fileImporter.importFile(Path.of("any"), "test deposit");

        assertThat(accountStore.getAccounts()).hasSize(2);
        assertThat(transactionStore.getTransactions()).hasSize(2);
    }

}