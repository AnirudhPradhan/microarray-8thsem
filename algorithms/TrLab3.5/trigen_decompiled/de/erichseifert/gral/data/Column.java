/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data;

import de.erichseifert.gral.data.DataAccessor;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.graphics.Orientation;

public class Column
extends DataAccessor {
    private static final long serialVersionUID = 7380420622890027262L;

    public Column(DataSource dataSource, int n) {
        super(dataSource, n);
    }

    @Override
    public Comparable<?> get(int n) {
        DataSource dataSource = this.getSource();
        if (dataSource == null) {
            return null;
        }
        return dataSource.get(this.getIndex(), n);
    }

    @Override
    public int size() {
        return this.getSource().getRowCount();
    }

    @Override
    public double getStatistics(String string) {
        return this.getSource().getStatistics().get(string, Orientation.VERTICAL, this.getIndex());
    }

    public boolean isNumeric() {
        return this.getSource().isColumnNumeric(this.getIndex());
    }
}

