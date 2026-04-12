/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io.data;

import de.erichseifert.gral.io.IOCapabilitiesStorage;
import de.erichseifert.gral.io.data.DataReader;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDataReader
extends IOCapabilitiesStorage
implements DataReader {
    private final Map<String, Object> a = new HashMap<String, Object>();
    private final Map<String, Object> b = new HashMap<String, Object>();
    private final String c;

    public AbstractDataReader(String string) {
        this.c = string;
    }

    public String getMimeType() {
        return this.c;
    }

    @Override
    public <T> T getSetting(String string) {
        if (!this.a.containsKey(string)) {
            return (T)this.b.get(string);
        }
        return (T)this.a.get(string);
    }

    @Override
    public <T> void setSetting(String string, T t) {
        this.a.put(string, t);
    }

    protected <T> void setDefault(String string, T t) {
        this.b.put(string, t);
    }
}

