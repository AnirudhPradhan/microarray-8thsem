/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data;

import de.erichseifert.gral.data.AbstractDataSource;
import java.util.Arrays;

public class DummyData
extends AbstractDataSource {
    private static final long serialVersionUID = 5780257823757438260L;
    private final Comparable<?> a;
    private final int b;
    private final int c;

    public DummyData(int n, int n2, Comparable<?> comparable) {
        super(new Class[0]);
        comparable.getClass();
        this.b = n;
        this.c = n2;
        this.a = comparable;
        Object[] objectArray = new Class[n];
        Arrays.fill(objectArray, comparable.getClass());
        this.setColumnTypes((Class<? extends Comparable<?>>[])objectArray);
    }

    @Override
    public Comparable<?> get(int n, int n2) {
        return this.a;
    }

    @Override
    public int getColumnCount() {
        return this.b;
    }

    @Override
    public int getRowCount() {
        return this.c;
    }
}

