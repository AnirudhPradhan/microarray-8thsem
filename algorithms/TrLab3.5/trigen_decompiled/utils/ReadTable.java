/*
 * Decompiled with CFR 0.152.
 */
package utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import utils.InTextFile;
import utils.TextUtilities;

public class ReadTable {
    List<List<String>> values;

    public ReadTable(String path, String sep) throws IOException {
        InTextFile f = new InTextFile(path);
        this.buildValues(f, sep);
    }

    public ReadTable(File file, String sep) throws IOException {
        InTextFile f = new InTextFile(file);
        this.buildValues(f, sep);
    }

    private void buildValues(InTextFile f, String sep) {
        this.values = new LinkedList<List<String>>();
        for (String s : f) {
            List<String> v = TextUtilities.splitElements(s, sep);
            this.values.add(v);
        }
    }

    public List<List<String>> getTable() {
        return this.values;
    }
}

