/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.util;

import de.erichseifert.vectorgraphics2d.util.DataUtils;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

public class FormattingWriter
implements Closeable,
Flushable {
    private final OutputStream a;
    private final String b;
    private final String c;
    private long d;

    public FormattingWriter(OutputStream outputStream, String string, String string2) {
        this.a = outputStream;
        this.b = string;
        this.c = string2;
    }

    public FormattingWriter write(String object) throws IOException {
        object = ((String)object).getBytes(this.b);
        this.a.write((byte[])object, 0, ((Object)object).length);
        this.d += (long)((Object)object).length;
        return this;
    }

    public FormattingWriter write(Number number) throws IOException {
        this.write(DataUtils.format(number));
        return this;
    }

    public FormattingWriter writeln() throws IOException {
        this.write(this.c);
        return this;
    }

    public FormattingWriter writeln(String string) throws IOException {
        this.write(string);
        this.write(this.c);
        return this;
    }

    public FormattingWriter writeln(Number number) throws IOException {
        this.write(number);
        this.write(this.c);
        return this;
    }

    public FormattingWriter format(String string, Object ... objectArray) throws IOException {
        this.write(String.format(null, string, objectArray));
        return this;
    }

    @Override
    public void flush() throws IOException {
        this.a.flush();
    }

    @Override
    public void close() throws IOException {
        this.a.close();
    }

    public long tell() {
        return this.d;
    }
}

