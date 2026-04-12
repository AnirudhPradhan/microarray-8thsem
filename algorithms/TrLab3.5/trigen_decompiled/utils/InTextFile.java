/*
 * Decompiled with CFR 0.152.
 */
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import utils.TextUtilities;

public class InTextFile
implements Iterable<String> {
    private BufferedReader bf;
    private String sep = "\n";

    public InTextFile(File file) throws IOException {
        this.bf = new BufferedReader(new FileReader(file));
    }

    public InTextFile(File file, String sep) throws IOException {
        this.bf = new BufferedReader(new FileReader(file));
        this.sep = sep;
    }

    public InTextFile(String path) throws IOException {
        this.bf = new BufferedReader(new FileReader(path));
    }

    public InTextFile(String path, String sep) throws IOException {
        this.bf = new BufferedReader(new FileReader(path));
        this.sep = sep;
    }

    public void close() throws IOException {
        this.bf.close();
    }

    @Override
    public Iterator<String> iterator() {
        return new InTextFileIterator(this.bf, this.sep);
    }

    private class InTextFileIterator
    implements Iterator<String> {
        private BufferedReader bf;
        private Iterator<String> wordIt;
        private String line;
        private String seps;

        public InTextFileIterator(BufferedReader bf, String seps) {
            this.seps = seps;
            this.bf = bf;
            try {
                this.line = bf.readLine();
                if (this.line != null) {
                    this.wordIt = TextUtilities.splitElements(this.line, seps).iterator();
                }
            }
            catch (IOException e) {
                // empty catch block
            }
        }

        @Override
        public boolean hasNext() {
            return this.wordIt != null && this.wordIt.hasNext();
        }

        @Override
        public String next() {
            String word = this.wordIt.next();
            if (!this.wordIt.hasNext()) {
                try {
                    this.line = this.bf.readLine();
                    if (this.line != null) {
                        this.wordIt = TextUtilities.splitElements(this.line, this.seps).iterator();
                    }
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
            return word;
        }

        @Override
        public void remove() {
        }
    }
}

