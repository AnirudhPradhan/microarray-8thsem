/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data.statistics;

import de.erichseifert.gral.data.DataAccessor;
import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.statistics.Histogram;
import de.erichseifert.gral.data.statistics.Statistics;
import de.erichseifert.gral.graphics.Orientation;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Histogram1D
extends Histogram {
    private static final long serialVersionUID = -4841658606362408312L;
    private final Orientation a;
    private final List<Number[]> b;
    private final List<long[]> c;
    private transient Map<Integer, Long> d;
    private transient Map<Integer, Long> e;

    private Histogram1D(DataSource dataSource, Orientation orientation) {
        super(dataSource);
        this.a = orientation;
        this.b = new ArrayList<Number[]>();
        this.c = new ArrayList<long[]>();
        this.d = new HashMap<Integer, Long>();
        this.e = new HashMap<Integer, Long>();
    }

    public Histogram1D(DataSource dataSource, Orientation orientation, int n) {
        this(dataSource, orientation);
        int n2 = this.getData().getColumnCount();
        if (orientation == Orientation.HORIZONTAL) {
            n2 = this.getData().getRowCount();
        }
        Statistics statistics = this.getData().getStatistics();
        for (int i = 0; i < n2; ++i) {
            double d = statistics.get("min", orientation, i);
            double d2 = statistics.get("max", orientation, i);
            double d3 = (d2 - d + Double.MIN_VALUE) / (double)n;
            Double[] doubleArray = new Double[n + 1];
            for (int j = 0; j < doubleArray.length; ++j) {
                doubleArray[j] = d + (double)j * d3;
            }
            this.b.add(doubleArray);
        }
        this.dataUpdated(this.getData(), new DataChangeEvent[0]);
    }

    public Histogram1D(DataSource dataSource, Orientation orientation, Number[] ... numberArray) {
        this(dataSource, orientation);
        int n = this.getData().getColumnCount();
        if (orientation == Orientation.HORIZONTAL) {
            n = this.getData().getRowCount();
        }
        if (numberArray.length != n) {
            throw new IllegalArgumentException(MessageFormat.format("Invalid number of breaks: got {0,number,integer}, expected {1,number,integer}.", numberArray.length, n));
        }
        Collections.addAll(this.b, numberArray);
        this.dataUpdated(this.getData(), new DataChangeEvent[0]);
    }

    @Override
    protected void rebuildCells() {
        this.c.clear();
        this.d.clear();
        this.e.clear();
        int n = 0;
        for (Number[] numberArray : this.b) {
            long[] lArray = new long[numberArray.length - 1];
            long l = Long.MAX_VALUE;
            long l2 = Long.MIN_VALUE;
            Object object = this.a == Orientation.VERTICAL ? this.getData().getColumn(n) : this.getData().getRow(n);
            object = ((DataAccessor)object).iterator();
            block1: while (object.hasNext()) {
                Object object2 = (Comparable)object.next();
                if (!(object2 instanceof Number)) continue;
                object2 = (Number)object2;
                double d = ((Number)object2).doubleValue();
                for (int i = 0; i < numberArray.length - 1; ++i) {
                    if (!(d >= numberArray[i].doubleValue()) || !(d < numberArray[i + 1].doubleValue())) continue;
                    int n2 = i;
                    lArray[n2] = lArray[n2] + 1L;
                    if (lArray[i] > l2) {
                        l2 = lArray[i];
                    }
                    if (lArray[i] >= l) continue block1;
                    l = lArray[i];
                    continue block1;
                }
            }
            this.c.add(lArray);
            this.d.put(n, l);
            this.e.put(n, l2);
            ++n;
        }
    }

    public Orientation getOrientation() {
        return this.a;
    }

    public Number[] getCellLimits(int n, int n2) {
        Object object = this.b.get(n);
        Number number = object[n2];
        object = object[n2 + 1];
        return new Number[]{number, object};
    }

    @Override
    public Comparable<?> get(int n, int n2) {
        return this.c.get(n)[n2];
    }

    @Override
    public int getRowCount() {
        int n = 0;
        for (long[] lArray : this.c) {
            n = Math.max(lArray.length, n);
        }
        return n;
    }

    @Override
    public int getColumnCount() {
        return this.c.size();
    }

    @Override
    public Class<? extends Comparable<?>>[] getColumnTypes() {
        Object[] objectArray = new Class[this.getColumnCount()];
        Arrays.fill(objectArray, Long.class);
        return objectArray;
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.d = new HashMap<Integer, Long>();
        this.e = new HashMap<Integer, Long>();
    }
}

