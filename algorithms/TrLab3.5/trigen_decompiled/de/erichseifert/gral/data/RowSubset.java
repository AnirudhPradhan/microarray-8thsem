/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data;

import de.erichseifert.gral.data.AbstractDataSource;
import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataListener;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.Row;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class RowSubset
extends AbstractDataSource
implements DataListener {
    private static final long serialVersionUID = -5396152732545986903L;
    private final DataSource a;
    private transient List<Integer> b = new ArrayList<Integer>();

    public RowSubset(DataSource dataSource) {
        super(new Class[0]);
        this.a = dataSource;
        this.a.addDataListener(this);
        this.dataUpdated(this.a, new DataChangeEvent[0]);
    }

    @Override
    public Row getRow(int n) {
        n = this.b.get(n);
        return this.a.getRow(n);
    }

    @Override
    public Comparable<?> get(int n, int n2) {
        n2 = this.b.get(n2);
        return this.a.get(n, n2);
    }

    @Override
    public int getColumnCount() {
        return this.a.getColumnCount();
    }

    @Override
    public int getRowCount() {
        return this.b.size();
    }

    @Override
    public Class<? extends Comparable<?>>[] getColumnTypes() {
        return this.a.getColumnTypes();
    }

    @Override
    public void dataAdded(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.a();
        this.notifyDataAdded(dataChangeEventArray);
    }

    @Override
    public void dataUpdated(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.a();
        this.notifyDataUpdated(dataChangeEventArray);
    }

    @Override
    public void dataRemoved(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.a();
        this.notifyDataRemoved(dataChangeEventArray);
    }

    private void a() {
        RowSubset rowSubset = this;
        rowSubset.b.clear();
        for (int i = 0; i < rowSubset.a.getRowCount(); ++i) {
            Row row = rowSubset.a.getRow(i);
            if (!rowSubset.accept(row)) continue;
            rowSubset.b.add(i);
        }
    }

    public abstract boolean accept(Row var1);

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.b = new ArrayList<Integer>();
        this.dataUpdated(this.a, new DataChangeEvent[0]);
        this.a.addDataListener(this);
    }
}

