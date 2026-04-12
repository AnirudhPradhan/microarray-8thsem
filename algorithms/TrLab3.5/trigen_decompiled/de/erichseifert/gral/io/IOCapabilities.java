/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io;

public class IOCapabilities {
    private final String a;
    private final String b;
    private final String c;
    private final String[] d;

    public IOCapabilities(String string, String string2, String string3, String[] stringArray) {
        this.a = string;
        this.b = string2;
        this.c = string3;
        this.d = stringArray;
    }

    public String getFormat() {
        return this.a;
    }

    public String getName() {
        return this.b;
    }

    public String getMimeType() {
        return this.c;
    }

    public String[] getExtensions() {
        return this.d;
    }
}

