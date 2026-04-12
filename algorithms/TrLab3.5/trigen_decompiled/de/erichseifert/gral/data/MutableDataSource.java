/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data;

import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.data.comparators.DataComparator;
import java.util.Collection;

public interface MutableDataSource
extends DataSource {
    public int add(Comparable<?> ... var1);

    public int add(Collection<? extends Comparable<?>> var1);

    public int add(Row var1);

    public void remove(int var1);

    public void removeLast();

    public void clear();

    public <T> Comparable<T> set(int var1, int var2, Comparable<T> var3);

    public void sort(DataComparator ... var1);

    public void setName(String var1);
}

