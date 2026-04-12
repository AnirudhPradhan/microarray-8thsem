/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LineWrapOutputStream
extends FilterOutputStream {
    public static final String STANDARD_EOL = "\r\n";
    private final int a;
    private final byte[] b;
    private int c;

    public LineWrapOutputStream(OutputStream outputStream, int n, String string) {
        super(outputStream);
        this.a = n;
        this.b = string.getBytes();
        if (n <= 0) {
            throw new IllegalArgumentException("Width must be at least 0.");
        }
    }

    public LineWrapOutputStream(OutputStream outputStream, int n) {
        this(outputStream, n, STANDARD_EOL);
    }

    @Override
    public void write(int n) throws IOException {
        if (this.c == this.a) {
            this.out.write(this.b);
            this.c = 0;
        }
        this.out.write(n);
        ++this.c;
    }
}

