/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data;

import de.erichseifert.gral.data.Column;
import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataListener;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.data.statistics.Statistics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

public abstract class AbstractDataSource
implements DataSource,
Serializable {
    private static final long serialVersionUID = 9139975565475816812L;
    private String a;
    private int b;
    private Class<? extends Comparable<?>>[] c;
    private transient Set<DataListener> d;
    private transient Statistics e;

    public AbstractDataSource(String string, Class<? extends Comparable<?>> ... classArray) {
        this.a = string;
        this.setColumnTypes(classArray);
        this.d = new LinkedHashSet<DataListener>();
    }

    public AbstractDataSource(Class<? extends Comparable<?>> ... classArray) {
        this((String)null, classArray);
    }

    @Override
    public Statistics getStatistics() {
        if (this.e == null) {
            this.e = new Statistics(this);
        }
        return this.e;
    }

    @Override
    public void addDataListener(DataListener dataListener) {
        this.d.add(dataListener);
    }

    @Override
    public void removeDataListener(DataListener dataListener) {
        this.d.remove(dataListener);
    }

    @Override
    public Iterator<Comparable<?>> iterator() {
        return new a(this);
    }

    protected void notifyDataAdded(DataChangeEvent ... dataChangeEventArray) {
        Object object = new LinkedList<DataListener>(this.d);
        object = object.iterator();
        while (object.hasNext()) {
            DataListener dataListener = (DataListener)object.next();
            dataListener.dataAdded(this, dataChangeEventArray);
        }
    }

    protected void notifyDataRemoved(DataChangeEvent ... dataChangeEventArray) {
        Object object = new LinkedList<DataListener>(this.d);
        object = object.iterator();
        while (object.hasNext()) {
            DataListener dataListener = (DataListener)object.next();
            dataListener.dataRemoved(this, dataChangeEventArray);
        }
    }

    protected void notifyDataUpdated(DataChangeEvent ... dataChangeEventArray) {
        Object object = new LinkedList<DataListener>(this.d);
        object = object.iterator();
        while (object.hasNext()) {
            DataListener dataListener = (DataListener)object.next();
            dataListener.dataUpdated(this, dataChangeEventArray);
        }
    }

    @Override
    public Column getColumn(int n) {
        return new Column(this, n);
    }

    @Override
    public String getName() {
        return this.a;
    }

    protected void setName(String string) {
        this.a = string;
    }

    @Override
    public int getColumnCount() {
        return this.b;
    }

    @Override
    public Class<? extends Comparable<?>>[] getColumnTypes() {
        Class<? extends Comparable<?>>[] classArray = Arrays.copyOf(this.c, this.c.length);
        return classArray;
    }

    @Override
    public boolean isColumnNumeric(int n) {
        if (n < 0 || n >= this.c.length) {
            return false;
        }
        Class<? extends Comparable<?>> clazz = this.c[n];
        return Number.class.isAssignableFrom(clazz);
    }

    protected void setColumnTypes(Class<? extends Comparable<?>> ... classArray) {
        this.c = Arrays.copyOf(classArray, classArray.length);
        this.b = classArray.length;
    }

    @Override
    public Row getRow(int n) {
        return new Row(this, n);
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.d = new HashSet<DataListener>();
    }

    private final class a
    implements Iterator<Comparable<?>> {
        private int a = 0;
        private int b = 0;
        private /* synthetic */ AbstractDataSource c;

        public a(AbstractDataSource abstractDataSource) {
            this.c = abstractDataSource;
        }

        @Override
        public final boolean hasNext() {
            return this.a < this.c.getColumnCount() && this.b < this.c.getRowCount();
        }

        @Override
        public final void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public final /* synthetic */ Object next() {
            a a2 = this;
            if (!a2.hasNext()) {
                throw new NoSuchElementException();
            }
            Comparable<?> comparable = a2.c.get(a2.a, a2.b);
            if (++a2.a >= a2.c.getColumnCount()) {
                a2.a = 0;
                ++a2.b;
            }
            return comparable;
        }
    }
}

