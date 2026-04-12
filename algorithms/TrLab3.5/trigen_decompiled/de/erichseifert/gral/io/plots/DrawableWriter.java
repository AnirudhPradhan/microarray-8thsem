/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io.plots;

import de.erichseifert.gral.graphics.Drawable;
import java.io.IOException;
import java.io.OutputStream;

public interface DrawableWriter {
    public String getMimeType();

    public void write(Drawable var1, OutputStream var2, double var3, double var5) throws IOException;

    public void write(Drawable var1, OutputStream var2, double var3, double var5, double var7, double var9) throws IOException;
}

