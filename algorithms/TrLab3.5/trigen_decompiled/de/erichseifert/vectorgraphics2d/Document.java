/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d;

import de.erichseifert.vectorgraphics2d.intermediate.CommandHandler;
import java.io.IOException;
import java.io.OutputStream;

public interface Document
extends CommandHandler {
    public void write(OutputStream var1) throws IOException;

    public void close();
}

