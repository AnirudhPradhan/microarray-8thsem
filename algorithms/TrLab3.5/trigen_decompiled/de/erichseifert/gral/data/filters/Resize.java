/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data.filters;

import de.erichseifert.gral.data.DataAccessor;
import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.data.filters.Filter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

public class Resize
extends Filter {
    private static final long serialVersionUID = -5601162872352170735L;
    private final int a;
    private final int b;

    public Resize(DataSource dataSource, int n, int n2) {
        super(dataSource, Filter.Mode.ZERO, new int[0]);
        this.a = n;
        this.b = n2;
        this.filter();
    }

    @Override
    public int getColumnCount() {
        if (this.a <= 0) {
            return super.getColumnCount();
        }
        return this.a;
    }

    @Override
    public int getRowCount() {
        if (this.b <= 0) {
            return super.getRowCount();
        }
        return this.b;
    }

    @Override
    public Comparable<?> get(int n, int n2) {
        if (!(this.a > 0 && this.a != this.getOriginal().getColumnCount() || this.b > 0 && this.b != this.getOriginal().getRowCount())) {
            return this.getOriginal(n, n2);
        }
        return super.get(n, n2);
    }

    @Override
    protected void filter() {
        double d;
        double d2;
        int n;
        DataAccessor dataAccessor;
        int n2;
        double d3;
        Iterable<Comparable<?>> iterable;
        this.clear();
        DataSource dataSource = this.getOriginal();
        if (this.getRowCount() == dataSource.getRowCount() && this.getColumnCount() == dataSource.getColumnCount()) {
            return;
        }
        DataSource dataSource2 = dataSource;
        if (this.getRowCount() != dataSource.getRowCount()) {
            Object[] objectArray = new Class[dataSource.getColumnCount()];
            Arrays.fill(objectArray, Double.class);
            iterable = new DataTable((Class<? extends Comparable<?>>[])objectArray);
            Resize.a((DataTable)iterable, this.getRowCount());
            d3 = (double)dataSource.getRowCount() / (double)this.getRowCount();
            for (n2 = 0; n2 < dataSource.getColumnCount(); ++n2) {
                dataAccessor = dataSource.getColumn(n2);
                for (n = 0; n < this.getRowCount(); ++n) {
                    d2 = (double)n * d3;
                    d = (double)(n + 1) * d3;
                    ((DataTable)iterable).set(n2, n, Double.valueOf(Resize.a(dataAccessor, d2, d)));
                }
            }
            dataSource2 = iterable;
        }
        if (this.getColumnCount() != dataSource.getColumnCount()) {
            Object[] objectArray = new Class[this.getColumnCount()];
            Arrays.fill(objectArray, Double.class);
            iterable = new DataTable((Class<? extends Comparable<?>>[])objectArray);
            Resize.a((DataTable)iterable, dataSource2.getRowCount());
            d3 = (double)dataSource.getColumnCount() / (double)this.getColumnCount();
            for (n2 = 0; n2 < dataSource2.getRowCount(); ++n2) {
                dataAccessor = dataSource2.getRow(n2);
                for (n = 0; n < this.getColumnCount(); ++n) {
                    d2 = (double)n * d3;
                    d = (double)(n + 1) * d3;
                    ((DataTable)iterable).set(n, n2, Double.valueOf(Resize.a(dataAccessor, d2, d)));
                }
            }
            dataSource2 = iterable;
        }
        for (n2 = 0; n2 < dataSource2.getRowCount(); ++n2) {
            iterable = dataSource2.getRow(n2);
            Comparable<?>[] comparableArray = ((DataAccessor)iterable).toArray(null);
            Double[] doubleArray = new Double[comparableArray.length];
            System.arraycopy(comparableArray, 0, doubleArray, 0, doubleArray.length);
            this.add(doubleArray);
        }
    }

    private static void a(DataTable dataTable, int n) {
        while (dataTable.getRowCount() < n) {
            Object[] objectArray = new Double[dataTable.getColumnCount()];
            Arrays.fill(objectArray, (Object)0.0);
            dataTable.add((Comparable<?>[])objectArray);
        }
    }

    private static double a(DataAccessor dataAccessor, double d, double d2) {
        int n = (int)Math.floor(d);
        int n2 = (int)Math.ceil(d);
        int n3 = (int)Math.floor(d2);
        int n4 = (int)Math.ceil(d2);
        double d3 = 0.0;
        for (int i = n; i < n4; ++i) {
            Number number = (Number)((Object)dataAccessor.get(i));
            double d4 = number.doubleValue();
            if (i == n && (double)n2 != d) {
                d3 += ((double)n2 - d) * d4;
                continue;
            }
            if (i == n4 - 1 && (double)n3 != d2) {
                d3 += (d2 - (double)n3) * d4;
                continue;
            }
            d3 += d4;
        }
        return d3 / (d2 - d);
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.dataUpdated(this, new DataChangeEvent[0]);
    }
}

