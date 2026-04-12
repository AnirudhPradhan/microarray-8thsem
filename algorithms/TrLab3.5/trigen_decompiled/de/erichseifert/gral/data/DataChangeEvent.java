/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data;

import de.erichseifert.gral.data.DataSource;
import java.util.EventObject;

public class DataChangeEvent
extends EventObject {
    private static final long serialVersionUID = -3791650088885473144L;
    private final int a;
    private final int b;
    private final Comparable<?> c;
    private final Comparable<?> d;

    public <T> DataChangeEvent(DataSource dataSource, int n, int n2, Comparable<T> comparable, Comparable<T> comparable2) {
        super(dataSource);
        this.a = n;
        this.b = n2;
        this.c = comparable;
        this.d = comparable2;
    }

    public int getCol() {
        return this.a;
    }

    public int getRow() {
        return this.b;
    }

    public Comparable<?> getOld() {
        return this.c;
    }

    public Comparable<?> getNew() {
        return this.d;
    }
}

