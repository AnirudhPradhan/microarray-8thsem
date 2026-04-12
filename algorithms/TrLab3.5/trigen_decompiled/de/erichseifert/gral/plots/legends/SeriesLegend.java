/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.legends;

import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.plots.legends.AbstractLegend;
import java.util.LinkedList;

public abstract class SeriesLegend
extends AbstractLegend {
    private static final long serialVersionUID = 1092110896986707546L;

    @Override
    protected Iterable<Row> getEntries(DataSource iterable) {
        LinkedList<Row> linkedList = new LinkedList<Row>();
        iterable = new Row((DataSource)iterable, 0);
        linkedList.add((Row)iterable);
        return linkedList;
    }

    @Override
    protected String getLabel(Row row) {
        return row.getSource().toString();
    }
}

