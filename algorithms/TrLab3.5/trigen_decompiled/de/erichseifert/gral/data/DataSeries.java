/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data;

import de.erichseifert.gral.data.AbstractDataSource;
import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataListener;
import de.erichseifert.gral.data.DataSource;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class DataSeries
extends AbstractDataSource
implements DataListener {
    private static final long serialVersionUID = 5568085894125740972L;
    private final DataSource a;
    private final List<Integer> b;

    public DataSeries(DataSource dataSource, int ... nArray) {
        this(null, dataSource, nArray);
    }

    public DataSeries(String classArray, DataSource classArray2, int ... nArray) {
        super((String)classArray, new Class[0]);
        this.a = classArray2;
        this.b = new ArrayList<Integer>();
        this.a.addDataListener(this);
        classArray = classArray2.getColumnTypes();
        if (nArray.length > 0) {
            classArray2 = new Class[nArray.length];
            int n = 0;
            for (int n2 : nArray) {
                this.b.add(n2);
                classArray2[n++] = classArray[n2];
            }
        } else {
            for (int i = 0; i < classArray2.getColumnCount(); ++i) {
                this.b.add(i);
            }
            classArray2 = classArray;
        }
        this.setColumnTypes(classArray2);
    }

    @Override
    public Comparable<?> get(int n, int n2) {
        try {
            n = this.b.get(n);
            return this.a.get(n, n2);
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            return null;
        }
    }

    @Override
    public int getColumnCount() {
        return this.b.size();
    }

    @Override
    public int getRowCount() {
        return this.a.getRowCount();
    }

    @Override
    public void dataAdded(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.notifyDataAdded(dataChangeEventArray);
    }

    @Override
    public void dataUpdated(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.notifyDataUpdated(dataChangeEventArray);
    }

    @Override
    public void dataRemoved(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.notifyDataRemoved(dataChangeEventArray);
    }

    public String toString() {
        return this.getName();
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.a.addDataListener(this);
    }
}

