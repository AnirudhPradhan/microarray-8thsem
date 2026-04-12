/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data.filters;

import de.erichseifert.gral.data.AbstractDataSource;
import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataListener;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.util.MathUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Filter
extends AbstractDataSource
implements DataListener {
    private static final long serialVersionUID = -5004453681128601437L;
    private final DataSource a;
    private final int[] b;
    private transient ArrayList<Double[]> c;
    private Mode d;

    public Filter(DataSource dataSource, Mode classArray, int ... objectArray) {
        super(new Class[0]);
        int n;
        int n2;
        this.c = new ArrayList(dataSource.getRowCount());
        this.a = dataSource;
        this.d = classArray;
        this.b = Arrays.copyOf(objectArray, objectArray.length);
        Arrays.sort(this.b);
        classArray = dataSource.getColumnTypes();
        objectArray = this.b;
        int n3 = this.b.length;
        for (n2 = 0; n2 < n3; ++n2) {
            n = objectArray[n2];
            if (dataSource.isColumnNumeric(n)) continue;
            throw new IllegalArgumentException(MessageFormat.format("Column {0,number,integer} isn't numeric and cannot be filtered.", n));
        }
        objectArray = classArray;
        int[] nArray = this.b;
        n2 = this.b.length;
        for (n = 0; n < n2; ++n) {
            int n4 = nArray[n];
            objectArray[n4] = (int)Double.class;
        }
        this.setColumnTypes((Class<? extends Comparable<?>>[])objectArray);
        this.a.addDataListener(this);
        this.dataUpdated(this.a, new DataChangeEvent[0]);
    }

    protected DataSource getOriginal() {
        return this.a;
    }

    protected Comparable<?> getOriginal(int n, int n2) {
        int n3 = this.a.getRowCount() - 1;
        if (n2 < 0 || n2 > n3) {
            if (this.getMode() == Mode.OMIT) {
                return Double.NaN;
            }
            if (this.getMode() == Mode.ZERO) {
                return 0.0;
            }
            if (this.getMode() == Mode.REPEAT) {
                n2 = MathUtils.limit(n2, 0, n3);
            } else if (this.getMode() == Mode.MIRROR) {
                int n4 = Math.abs(n2) / n3;
                n2 = Math.abs(n2) % n3;
                if ((n4 & 1) != 0) {
                    n2 = n3 - n2;
                }
            } else if (this.getMode() == Mode.CIRCULAR) {
                n2 = n2 >= 0 ? (n2 %= n3 + 1) : (n2 + 1) % (n3 + 1) + n3;
            }
        }
        return this.a.get(n, n2);
    }

    protected void clear() {
        this.c.clear();
    }

    protected void add(Double[] doubleArray) {
        this.c.add(doubleArray);
    }

    protected void add(Number[] numberArray) {
        Double[] doubleArray = new Double[numberArray.length];
        int n = 0;
        for (Number number : numberArray) {
            doubleArray[n++] = number.doubleValue();
        }
        this.c.add(doubleArray);
    }

    @Override
    public Comparable<?> get(int n, int n2) {
        int n3 = this.getIndex(n);
        if (n3 < 0) {
            return this.a.get(n, n2);
        }
        return this.c.get(n2)[n3];
    }

    protected Number set(int n, int n2, Double d) {
        int n3 = this.getIndex(n);
        if (n3 < 0) {
            throw new IllegalArgumentException("Can't set value in unfiltered column.");
        }
        Double d2 = this.c.get(n2)[n3];
        this.c.get((int)n2)[n3] = d;
        this.notifyDataUpdated(new DataChangeEvent(this, n, n2, d2, d));
        return d2;
    }

    @Override
    public int getColumnCount() {
        return this.a.getColumnCount();
    }

    protected int getColumnCountFiltered() {
        if (this.b.length == 0) {
            return this.a.getColumnCount();
        }
        return this.b.length;
    }

    @Override
    public int getRowCount() {
        return this.a.getRowCount();
    }

    protected int getRowCountFiltered() {
        return this.a.getRowCount();
    }

    @Override
    public void dataAdded(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        dataSource = this;
        ((Filter)dataSource).filter();
        this.notifyDataAdded(dataChangeEventArray);
    }

    @Override
    public void dataUpdated(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        dataSource = this;
        ((Filter)dataSource).filter();
        this.notifyDataUpdated(dataChangeEventArray);
    }

    @Override
    public void dataRemoved(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        dataSource = this;
        ((Filter)dataSource).filter();
        this.notifyDataRemoved(dataChangeEventArray);
    }

    protected int getIndexOriginal(int n) {
        if (this.b.length == 0) {
            return n;
        }
        return this.b[n];
    }

    protected int getIndex(int n) {
        if (this.b.length == 0) {
            return n;
        }
        return Arrays.binarySearch(this.b, n);
    }

    protected boolean isFiltered(int n) {
        return this.getIndex(n) >= 0;
    }

    protected abstract void filter();

    public Mode getMode() {
        return this.d;
    }

    public void setMode(Mode mode) {
        this.d = mode;
        this.dataUpdated(this, new DataChangeEvent[0]);
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.c = new ArrayList();
        this.a.addDataListener(this);
    }

    public static enum Mode {
        OMIT,
        ZERO,
        REPEAT,
        MIRROR,
        CIRCULAR;

    }
}

