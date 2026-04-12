/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data.filters;

import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.filters.Filter;
import de.erichseifert.gral.util.MathUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class Median
extends Filter {
    private static final long serialVersionUID = -1645928908580026536L;
    private int a;
    private int b;

    public Median(DataSource dataSource, int n, int n2, Filter.Mode mode, int ... nArray) {
        super(dataSource, mode, nArray);
        this.a = n;
        this.b = n2;
        this.filter();
    }

    @Override
    protected void filter() {
        int n;
        this.clear();
        if (this.getWindowSize() <= 0) {
            return;
        }
        ArrayList arrayList = new ArrayList(this.getColumnCount());
        for (n = 0; n < this.getColumnCountFiltered(); ++n) {
            int n2 = this.getIndexOriginal(n);
            ArrayList<Double> arrayList2 = new ArrayList<Double>(this.getWindowSize());
            arrayList.add(arrayList2);
            for (int i = this.getOffset() - this.getWindowSize(); i < 0; ++i) {
                Comparable<?> comparable = this.getOriginal(n2, i);
                double d = ((Number)((Object)comparable)).doubleValue();
                arrayList2.add(d);
            }
        }
        for (n = 0; n < this.getRowCount(); ++n) {
            Double[] doubleArray = new Double[this.getColumnCountFiltered()];
            for (int i = 0; i < doubleArray.length; ++i) {
                List list = (List)arrayList.get(i);
                if (list.size() >= this.getWindowSize()) {
                    list.remove(0);
                }
                int n3 = this.getIndexOriginal(i);
                Comparable<?> comparable = this.getOriginal(n3, n - this.getOffset() + this.getWindowSize());
                double d = ((Number)((Object)comparable)).doubleValue();
                list.add(d);
                doubleArray[i] = Median.a(list);
            }
            this.add(doubleArray);
        }
    }

    private static double a(List<Double> object) {
        if (object.size() == 1) {
            return object.get(0);
        }
        ArrayList<Double> arrayList = new ArrayList<Double>(object.size());
        object = object.iterator();
        while (object.hasNext()) {
            Double d = (Double)object.next();
            if (!MathUtils.isCalculatable(d)) {
                return Double.NaN;
            }
            arrayList.add(d);
        }
        int n = MathUtils.randomizedSelect(arrayList, 0, arrayList.size() - 1, arrayList.size() / 2);
        double d = (Double)arrayList.get(n);
        if ((arrayList.size() & 1) == 0) {
            n = MathUtils.randomizedSelect(arrayList, 0, arrayList.size() - 1, arrayList.size() / 2 + 1);
            double d2 = (Double)arrayList.get(n);
            d = (d + d2) / 2.0;
        }
        return d;
    }

    public int getWindowSize() {
        return this.a;
    }

    public void setWindowSize(int n) {
        this.a = n;
        this.dataUpdated(this, new DataChangeEvent[0]);
    }

    public int getOffset() {
        return this.b;
    }

    public void setOffset(int n) {
        this.b = n;
        this.dataUpdated(this, new DataChangeEvent[0]);
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.dataUpdated(this, new DataChangeEvent[0]);
    }
}

