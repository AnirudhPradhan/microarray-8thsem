/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data.statistics;

import de.erichseifert.gral.data.AbstractDataSource;
import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataListener;
import de.erichseifert.gral.data.DataSource;
import java.io.IOException;
import java.io.ObjectInputStream;

public abstract class Histogram
extends AbstractDataSource
implements DataListener {
    private static final long serialVersionUID = 5031290498142366257L;
    private final DataSource a;

    public Histogram(DataSource dataSource) {
        super(new Class[0]);
        this.a = dataSource;
        this.a.addDataListener(this);
    }

    protected abstract void rebuildCells();

    @Override
    public void dataAdded(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        dataSource = this;
        ((Histogram)dataSource).rebuildCells();
        this.notifyDataAdded(dataChangeEventArray);
    }

    @Override
    public void dataUpdated(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        dataSource = this;
        ((Histogram)dataSource).rebuildCells();
        this.notifyDataUpdated(dataChangeEventArray);
    }

    @Override
    public void dataRemoved(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        dataSource = this;
        ((Histogram)dataSource).rebuildCells();
        this.notifyDataRemoved(dataChangeEventArray);
    }

    public DataSource getData() {
        return this.a;
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.a.addDataListener(this);
    }
}

