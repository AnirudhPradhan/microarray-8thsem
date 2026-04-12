/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.legends;

import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataListener;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.plots.legends.AbstractLegend;
import java.text.Format;
import java.text.NumberFormat;
import java.util.LinkedList;

public abstract class ValueLegend
extends AbstractLegend
implements DataListener {
    private static final long serialVersionUID = -4274009997506638823L;
    private int a = 0;
    private Format b;

    @Override
    protected Iterable<Row> getEntries(DataSource dataSource) {
        LinkedList<Row> linkedList = new LinkedList<Row>();
        for (int i = 0; i < dataSource.getRowCount(); ++i) {
            Row row = new Row(dataSource, i);
            linkedList.add(row);
        }
        return linkedList;
    }

    @Override
    protected String getLabel(Row object) {
        int n = this.getLabelColumn();
        Comparable<?> comparable = ((Row)object).get(n);
        if (comparable == null) {
            return "";
        }
        Format format = this.getLabelFormat();
        if (format == null && ((Row)object).isColumnNumeric(n)) {
            format = NumberFormat.getInstance();
        }
        object = format != null ? format.format(comparable) : comparable.toString();
        return object;
    }

    @Override
    public void add(DataSource dataSource) {
        super.add(dataSource);
        dataSource.addDataListener(this);
    }

    @Override
    public void remove(DataSource dataSource) {
        super.remove(dataSource);
        dataSource.removeDataListener(this);
    }

    @Override
    public void dataAdded(DataSource iterable, DataChangeEvent ... dataChangeEventArray) {
        iterable = this;
        ((AbstractLegend)iterable).invalidate();
    }

    @Override
    public void dataUpdated(DataSource iterable, DataChangeEvent ... dataChangeEventArray) {
        iterable = this;
        ((AbstractLegend)iterable).invalidate();
    }

    @Override
    public void dataRemoved(DataSource iterable, DataChangeEvent ... dataChangeEventArray) {
        iterable = this;
        ((AbstractLegend)iterable).invalidate();
    }

    public int getLabelColumn() {
        return this.a;
    }

    public void setLabelColumn(int n) {
        this.a = n;
        this.invalidate();
        this.refresh();
    }

    public Format getLabelFormat() {
        return this.b;
    }

    public void setLabelFormat(Format format) {
        this.b = format;
        this.invalidate();
        this.refresh();
    }
}

