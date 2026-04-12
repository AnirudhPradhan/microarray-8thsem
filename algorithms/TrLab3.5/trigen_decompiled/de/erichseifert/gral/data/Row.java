/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data;

import de.erichseifert.gral.data.DataAccessor;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.graphics.Orientation;

public class Row
extends DataAccessor {
    private static final long serialVersionUID = 2725146484866525573L;

    public Row(DataSource dataSource, int n) {
        super(dataSource, n);
    }

    @Override
    public Comparable<?> get(int n) {
        DataSource dataSource = this.getSource();
        if (dataSource == null) {
            return null;
        }
        return dataSource.get(n, this.getIndex());
    }

    @Override
    public int size() {
        return this.getSource().getColumnCount();
    }

    @Override
    public double getStatistics(String string) {
        return this.getSource().getStatistics().get(string, Orientation.HORIZONTAL, this.getIndex());
    }

    public boolean isColumnNumeric(int n) {
        return this.getSource().isColumnNumeric(n);
    }
}

