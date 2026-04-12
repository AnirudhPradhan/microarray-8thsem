/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data;

import de.erichseifert.gral.data.AbstractDataSource;
import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataListener;
import de.erichseifert.gral.data.DataSource;

public class EnumeratedData
extends AbstractDataSource
implements DataListener {
    private static final long serialVersionUID = -4952487410608980063L;
    private final DataSource a;
    private final double b;
    private final double c;

    public EnumeratedData(DataSource dataSource, double d, double d2) {
        super(new Class[0]);
        this.a = dataSource;
        this.b = d;
        this.c = d2;
        Class<? extends Comparable<?>>[] classArray = dataSource.getColumnTypes();
        Class[] classArray2 = new Class[classArray.length + 1];
        System.arraycopy(classArray, 0, classArray2, 1, classArray.length);
        classArray2[0] = Double.class;
        this.setColumnTypes(classArray2);
        dataSource.addDataListener(this);
    }

    public EnumeratedData(DataSource dataSource) {
        this(dataSource, 0.0, 1.0);
    }

    @Override
    public Comparable<?> get(int n, int n2) {
        if (n <= 0) {
            return (double)n2 * this.c + this.b;
        }
        return this.a.get(n - 1, n2);
    }

    @Override
    public int getRowCount() {
        return this.a.getRowCount();
    }

    @Override
    public void dataAdded(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.notifyDataAdded(this.a(dataChangeEventArray));
    }

    @Override
    public void dataUpdated(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.notifyDataUpdated(this.a(dataChangeEventArray));
    }

    @Override
    public void dataRemoved(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.notifyDataRemoved(this.a(dataChangeEventArray));
    }

    private DataChangeEvent[] a(DataChangeEvent[] dataChangeEventArray) {
        if (dataChangeEventArray == null || dataChangeEventArray.length == 0) {
            return new DataChangeEvent[]{new DataChangeEvent(this, 0, 0, null, null)};
        }
        DataChangeEvent[] dataChangeEventArray2 = new DataChangeEvent[dataChangeEventArray.length + 1];
        for (int i = 0; i < dataChangeEventArray2.length; ++i) {
            int n;
            int n2;
            Object object;
            if (i == 0) {
                object = dataChangeEventArray[0];
                n2 = 0;
                n = ((DataChangeEvent)object).getRow();
            } else {
                object = dataChangeEventArray[i - 1];
                n2 = ((DataChangeEvent)object).getCol() + 1;
                n = ((DataChangeEvent)object).getRow();
            }
            Comparable<?> comparable = ((DataChangeEvent)object).getOld();
            object = ((DataChangeEvent)object).getNew();
            dataChangeEventArray2[i] = new DataChangeEvent(this, n2, n, comparable, object);
        }
        return dataChangeEventArray2;
    }
}

