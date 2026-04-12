/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data.comparators;

import java.io.Serializable;
import java.util.Comparator;

public abstract class DataComparator
implements Serializable,
Comparator<Comparable<?>[]> {
    private static final long serialVersionUID = -982173906879554838L;
    private final int a;

    public DataComparator(int n) {
        this.a = n;
    }

    public int getColumn() {
        return this.a;
    }
}

