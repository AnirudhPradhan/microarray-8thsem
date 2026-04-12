/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data;

import de.erichseifert.gral.data.Column;
import de.erichseifert.gral.data.DataListener;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.data.statistics.Statistics;

public interface DataSource
extends Iterable<Comparable<?>> {
    public Column getColumn(int var1);

    public Class<? extends Comparable<?>>[] getColumnTypes();

    public Row getRow(int var1);

    public Comparable<?> get(int var1, int var2);

    public Statistics getStatistics();

    public int getRowCount();

    public String getName();

    public int getColumnCount();

    public boolean isColumnNumeric(int var1);

    public void addDataListener(DataListener var1);

    public void removeDataListener(DataListener var1);
}

