/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io.data;

import de.erichseifert.gral.data.DataSource;
import java.io.IOException;
import java.io.OutputStream;

public interface DataWriter {
    public void write(DataSource var1, OutputStream var2) throws IOException;

    public <T> T getSetting(String var1);

    public <T> void setSetting(String var1, T var2);
}

