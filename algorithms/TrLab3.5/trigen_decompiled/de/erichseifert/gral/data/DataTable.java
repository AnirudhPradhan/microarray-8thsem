/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data;

import de.erichseifert.gral.data.AbstractDataSource;
import de.erichseifert.gral.data.DataAccessor;
import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.MutableDataSource;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.data.comparators.DataComparator;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataTable
extends AbstractDataSource
implements MutableDataSource {
    private static final long serialVersionUID = 535236774042654449L;
    private final List<Comparable<?>[]> a = new ArrayList<Comparable<?>[]>();

    public DataTable(Class<? extends Comparable<?>> ... classArray) {
        super(classArray);
    }

    public DataTable(int n, Class<? extends Comparable<?>> clazz) {
        this(new Class[0]);
        Object[] objectArray = new Class[n];
        Arrays.fill(objectArray, clazz);
        this.setColumnTypes((Class<? extends Comparable<?>>[])objectArray);
    }

    public DataTable(DataSource dataSource) {
        this(dataSource.getColumnTypes());
        for (int i = 0; i < dataSource.getRowCount(); ++i) {
            this.add(dataSource.getRow(i));
        }
    }

    @Override
    public int add(Comparable<?> ... comparableArray) {
        return this.add(Arrays.asList(comparableArray));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int add(Collection<? extends Comparable<?>> object) {
        int n;
        if (object.size() != this.getColumnCount()) {
            throw new IllegalArgumentException(MessageFormat.format("Wrong number of columns! Expected {0,number,integer}, got {1,number,integer}.", this.getColumnCount(), object.size()));
        }
        Comparable[] comparableArray = new Comparable[object.size()];
        DataChangeEvent[] dataChangeEventArray = new DataChangeEvent[comparableArray.length];
        Class<? extends Comparable<?>>[] classArray = this.getColumnTypes();
        List<Comparable<?>[]> list = this.a;
        synchronized (list) {
            int n2 = 0;
            object = object.iterator();
            while (object.hasNext()) {
                Comparable comparable = (Comparable)object.next();
                if (comparable != null && !classArray[n2].isAssignableFrom(comparable.getClass())) {
                    throw new IllegalArgumentException(MessageFormat.format("Wrong column type! Expected {0}, got {1}.", classArray[n2], comparable.getClass()));
                }
                comparableArray[n2] = comparable;
                dataChangeEventArray[n2] = new DataChangeEvent(this, n2, this.a.size(), null, comparable);
                ++n2;
            }
            this.a.add(comparableArray);
            n = this.a.size();
        }
        this.notifyDataAdded(dataChangeEventArray);
        return n - 1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int add(Row object) {
        ArrayList<Comparable> arrayList;
        Row row = object;
        synchronized (row) {
            arrayList = new ArrayList<Comparable>(((Row)object).size());
            object = ((DataAccessor)object).iterator();
            while (object.hasNext()) {
                Comparable comparable = (Comparable)object.next();
                arrayList.add(comparable);
            }
        }
        return this.add(arrayList);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void remove(int n) {
        DataChangeEvent[] dataChangeEventArray;
        List<Comparable<?>[]> list = this.a;
        synchronized (list) {
            Row row = new Row(this, n);
            dataChangeEventArray = new DataChangeEvent[this.getColumnCount()];
            for (int i = 0; i < dataChangeEventArray.length; ++i) {
                dataChangeEventArray[i] = new DataChangeEvent(this, i, n, row.get(i), null);
            }
            this.a.remove(n);
        }
        this.notifyDataRemoved(dataChangeEventArray);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void removeLast() {
        DataChangeEvent[] dataChangeEventArray;
        DataTable dataTable = this;
        synchronized (dataTable) {
            int n = this.getRowCount() - 1;
            Row row = new Row(this, n);
            dataChangeEventArray = new DataChangeEvent[this.getColumnCount()];
            for (int i = 0; i < dataChangeEventArray.length; ++i) {
                dataChangeEventArray[i] = new DataChangeEvent(this, i, n, row.get(i), null);
            }
            this.a.remove(n);
        }
        this.notifyDataRemoved(dataChangeEventArray);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void clear() {
        DataChangeEvent[] dataChangeEventArray;
        DataTable dataTable = this;
        synchronized (dataTable) {
            int n = this.getColumnCount();
            int n2 = this.getRowCount();
            dataChangeEventArray = new DataChangeEvent[n * n2];
            for (int i = 0; i < n2; ++i) {
                for (int j = 0; j < n; ++j) {
                    dataChangeEventArray[j + i * n] = new DataChangeEvent(this, j, i, this.get(j, i), null);
                }
            }
            this.a.clear();
        }
        this.notifyDataRemoved(dataChangeEventArray);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Comparable<?> get(int n, int n2) {
        Comparable<?>[] comparableArray;
        List<Comparable<?>[]> list = this.a;
        synchronized (list) {
            if (n2 >= this.a.size()) {
                return null;
            }
            comparableArray = this.a.get(n2);
        }
        if (comparableArray == null) {
            return null;
        }
        return comparableArray[n];
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public <T> Comparable<T> set(int n, int n2, Comparable<T> comparable) {
        Comparable<?> comparable2;
        DataChangeEvent dataChangeEvent = null;
        DataTable dataTable = this;
        synchronized (dataTable) {
            comparable2 = this.get(n, n2);
            if (comparable2 == null || !comparable2.equals(comparable)) {
                this.a.get((int)n2)[n] = comparable;
                dataChangeEvent = new DataChangeEvent(this, n, n2, comparable2, comparable);
            }
        }
        if (dataChangeEvent != null) {
            this.notifyDataUpdated(dataChangeEvent);
        }
        return comparable2;
    }

    @Override
    public int getRowCount() {
        return this.a.size();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void sort(DataComparator ... object) {
        List<Comparable<?>[]> list = this.a;
        synchronized (list) {
            object = new a(this, (DataComparator[])object);
            Collections.sort(this.a, object);
            return;
        }
    }

    @Override
    public void setName(String string) {
        super.setName(string);
    }

    private final class a
    implements Comparator<Comparable<?>[]> {
        private final DataComparator[] a;

        public a(DataTable dataTable, DataComparator[] dataComparatorArray) {
            this.a = dataComparatorArray;
        }
    }
}

