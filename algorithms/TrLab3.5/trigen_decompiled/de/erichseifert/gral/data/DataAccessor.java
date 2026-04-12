/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data;

import de.erichseifert.gral.data.DataSource;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Locale;

public abstract class DataAccessor
implements Serializable,
Iterable<Comparable<?>> {
    private static final long serialVersionUID = -6977184455447753502L;
    private final DataSource a;
    private final int b;

    public DataAccessor(DataSource dataSource, int n) {
        this.a = dataSource;
        this.b = n;
    }

    public DataSource getSource() {
        return this.a;
    }

    public int getIndex() {
        return this.b;
    }

    public abstract Comparable<?> get(int var1);

    public abstract int size();

    public boolean equals(Object object) {
        if (!(object instanceof DataAccessor)) {
            return false;
        }
        object = (DataAccessor)object;
        int n = this.size();
        if (((DataAccessor)object).size() != n) {
            return false;
        }
        for (int i = 0; i < n; ++i) {
            Comparable<?> comparable = ((DataAccessor)object).get(i);
            Comparable<?> comparable2 = this.get(i);
            if (!(comparable == null ? comparable2 != null : !comparable.equals(comparable2))) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.a.hashCode() ^ this.b;
    }

    public String toString() {
        return String.format(Locale.US, "%s[source=%s,index=%d]", this.getClass().getName(), this.getSource(), this.getIndex());
    }

    public Comparable<?>[] toArray(Comparable<?>[] comparableArray) {
        if (comparableArray == null) {
            comparableArray = new Comparable[this.size()];
        }
        if (comparableArray.length != this.size()) {
            throw new IllegalArgumentException(MessageFormat.format("Array of size {0,number,integer} does not match {1,number,integer} elements.", comparableArray.length, this.size()));
        }
        for (int i = 0; i < comparableArray.length; ++i) {
            comparableArray[i] = this.get(i);
        }
        return comparableArray;
    }

    public abstract double getStatistics(String var1);

    @Override
    public Iterator<Comparable<?>> iterator() {
        return new Iterator<Comparable<?>>(this){
            private int a;
            private /* synthetic */ DataAccessor b;
            {
                this.b = dataAccessor;
            }

            @Override
            public final boolean hasNext() {
                return this.a < this.b.size();
            }

            @Override
            public final void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public final /* synthetic */ Object next() {
                Object object = this;
                object = object.b.get(object.a++);
                return object;
            }
        };
    }
}

