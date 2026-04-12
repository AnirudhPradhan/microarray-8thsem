/*
 * Decompiled with CFR 0.152.
 */
package utils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import utils.OutTextFile;

public class WriteTable {
    private double[][] table;

    public WriteTable(double[][] table) throws IOException {
        this.table = table;
    }

    public void write(String path, DecimalFormat fr) throws IOException {
        this.write(new File(path), ";", fr);
    }

    public void write(File loc, DecimalFormat fr) throws IOException {
        this.write(loc, ";", fr);
    }

    public void write(String path) throws IOException {
        this.write(new File(path), ";", null);
    }

    public void write(File loc) throws IOException {
        this.write(loc, ";", null);
    }

    public void write(String path, String sep, DecimalFormat fr) throws IOException {
        this.write(new File(path), ";", null);
    }

    public void write(File loc, String sep, DecimalFormat fr) throws IOException {
        int rowSize = this.table.length;
        int columnSize = this.table[0].length;
        OutTextFile f = new OutTextFile(loc);
        for (int i = 0; i < rowSize; ++i) {
            for (int j = 0; j < columnSize; ++j) {
                double value = this.table[i][j];
                String str = "";
                str = fr != null ? fr.format(value) : "" + value;
                if (j == columnSize - 1) {
                    f.print(str + "\n");
                    continue;
                }
                f.print(str + sep);
            }
        }
        f.close();
    }
}

