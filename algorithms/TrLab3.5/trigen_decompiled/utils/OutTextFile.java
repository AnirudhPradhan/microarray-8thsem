/*
 * Decompiled with CFR 0.152.
 */
package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class OutTextFile {
    private PrintStream ps;

    public OutTextFile() {
        this.ps = System.out;
    }

    public OutTextFile(File file) throws FileNotFoundException {
        this.ps = new PrintStream(file);
    }

    public OutTextFile(String path) throws FileNotFoundException {
        this.ps = new PrintStream(new File(path));
    }

    public void print(String str) {
        this.ps.print(str);
    }

    public void println(String str) {
        this.ps.println(str);
    }

    public void close() {
        this.ps.close();
    }
}

