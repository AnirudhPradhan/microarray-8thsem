/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io;

import de.erichseifert.gral.io.IOCapabilities;
import java.util.List;

public interface IOFactory<T> {
    public T get(String var1);

    public IOCapabilities getCapabilities(String var1);

    public List<IOCapabilities> getCapabilities();

    public String[] getSupportedFormats();

    public boolean isFormatSupported(String var1);
}

