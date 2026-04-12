/*
 * Decompiled with CFR 0.152.
 */
package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import utils.OutTextFile;

public class WriteCube {
    private double[][][] cube;

    public WriteCube(double[][][] cube) throws IOException {
        this.cube = cube;
    }

    public void write(String path) throws IOException {
        this.write(path, ";", null);
    }

    public void write(String path, String sep, DecimalFormat fr) throws IOException {
        int depthSize = this.cube[0][0].length;
        for (int k = 0; k < depthSize; ++k) {
            this.writeTable(k, path, sep, fr);
        }
    }

    private void writeTable(int k, String path, String sep, DecimalFormat fr) throws FileNotFoundException {
        OutTextFile f = new OutTextFile(path + "_" + k + ".csv");
        int rowSize = this.cube.length;
        int columnSize = this.cube[0].length;
        for (int i = 0; i < rowSize; ++i) {
            for (int j = 0; j < columnSize; ++j) {
                double value = this.cube[i][j][k];
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

