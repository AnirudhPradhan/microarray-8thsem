/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data.statistics;

import de.erichseifert.gral.data.DataAccessor;
import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataListener;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.graphics.Orientation;
import de.erichseifert.gral.util.DataUtils;
import de.erichseifert.gral.util.MathUtils;
import de.erichseifert.gral.util.SortedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Statistics
implements DataListener {
    public static final String N = "n";
    public static final String SUM = "sum";
    public static final String SUM2 = "sum2";
    public static final String SUM3 = "sum3";
    public static final String SUM4 = "sum4";
    public static final String MIN = "min";
    public static final String MAX = "max";
    public static final String MEAN = "mean";
    public static final String SUM_OF_DIFF_SQUARES = "M2";
    public static final String SUM_OF_DIFF_CUBICS = "M3";
    public static final String SUM_OF_DIFF_QUADS = "M4";
    public static final String VARIANCE = "sample variance";
    public static final String POPULATION_VARIANCE = "population variance";
    public static final String SKEWNESS = "skewness";
    public static final String KURTOSIS = "kurtosis";
    public static final String MEDIAN = "quantile50";
    public static final String QUARTILE_1 = "quantile25";
    public static final String QUARTILE_2 = "quantile50";
    public static final String QUARTILE_3 = "quantile75";
    private final DataSource a;
    private final Map<String, Double> b = new HashMap<String, Double>();
    private final ArrayList<Map<String, Double>> c;
    private final ArrayList<Map<String, Double>> d;

    public Statistics(DataSource dataSource) {
        int n;
        this.c = new ArrayList(dataSource.getColumnCount());
        for (n = 0; n < dataSource.getColumnCount(); ++n) {
            this.c.add(new HashMap());
        }
        this.d = new ArrayList(dataSource.getRowCount());
        for (n = 0; n < dataSource.getRowCount(); ++n) {
            this.d.add(new HashMap());
        }
        this.a = dataSource;
        this.a.addDataListener(this);
    }

    public double get(String string) {
        return this.a(this.a, this.b, string);
    }

    public double get(String string, Orientation object, int n) {
        DataAccessor dataAccessor;
        if (object == Orientation.VERTICAL) {
            if (n >= this.c.size()) {
                this.c.add(new HashMap());
            }
            object = this.c.get(n);
            dataAccessor = this.a.getColumn(n);
        } else {
            if (n >= this.d.size()) {
                this.d.add(new HashMap());
            }
            object = this.d.get(n);
            dataAccessor = this.a.getRow(n);
        }
        return this.a(dataAccessor, (Map<String, Double>)object, string);
    }

    private double a(Iterable<Comparable<?>> object, Map<String, Double> map, String string) {
        if (!map.containsKey(string)) {
            if ("quantile50".equals(string) || QUARTILE_1.equals(string) || "quantile50".equals(string) || QUARTILE_3.equals(string)) {
                Map<String, Double> map2 = map;
                SortedList<Double> sortedList = new SortedList<Double>();
                Iterator<Comparable<?>> iterator = object.iterator();
                while (iterator.hasNext()) {
                    Number number;
                    double d;
                    Comparable<?> comparable = iterator.next();
                    if (!(comparable instanceof Number) || !MathUtils.isCalculatable(d = (number = (Number)((Object)comparable)).doubleValue())) continue;
                    sortedList.add(d);
                }
                if (sortedList.size() > 0) {
                    map2.put(QUARTILE_1, MathUtils.quantile(sortedList, 0.25));
                    map2.put("quantile50", MathUtils.quantile(sortedList, 0.5));
                    map2.put(QUARTILE_3, MathUtils.quantile(sortedList, 0.75));
                    map2.put("quantile50", map2.get("quantile50"));
                }
            } else {
                Map<String, Double> map3 = map;
                double d = 0.0;
                double d2 = 0.0;
                double d3 = 0.0;
                double d4 = 0.0;
                double d5 = 0.0;
                double d6 = 0.0;
                double d7 = 0.0;
                double d8 = 0.0;
                double d9 = 0.0;
                object = object.iterator();
                while (object.hasNext()) {
                    Object object2 = (Comparable)object.next();
                    if (!(object2 instanceof Number) || !MathUtils.isCalculatable((Number)(object2 = (Number)object2))) continue;
                    double d10 = ((Number)object2).doubleValue();
                    if (!map3.containsKey(MIN) || d10 < map3.get(MIN)) {
                        map3.put(MIN, d10);
                    }
                    if (!map3.containsKey(MAX) || d10 > map3.get(MAX)) {
                        map3.put(MAX, d10);
                    }
                    d += 1.0;
                    double d11 = d10 * d10;
                    d2 += d10;
                    d3 += d11;
                    d4 += d11 * d10;
                    d5 += d11 * d11;
                    double d12 = d10 - d6;
                    double d13 = d12 / d;
                    double d14 = d13 * d13;
                    double d15 = d12 * d13 * (d - 1.0);
                    d6 += d13;
                    d9 += d15 * d14 * (d * d - d * 3.0 + 3.0) + d14 * 6.0 * d7 - d13 * 4.0 * d8;
                    d8 += d15 * d13 * (d - 2.0) - d13 * 3.0 * d7;
                    d7 += d15;
                }
                map3.put(N, d);
                map3.put(SUM, d2);
                map3.put(SUM2, d3);
                map3.put(SUM3, d4);
                map3.put(SUM4, d5);
                map3.put(MEAN, d6);
                map3.put(SUM_OF_DIFF_QUADS, d9);
                map3.put(SUM_OF_DIFF_CUBICS, d8);
                map3.put(SUM_OF_DIFF_SQUARES, d7);
                map3.put(VARIANCE, d7 / (d - 1.0));
                map3.put(POPULATION_VARIANCE, d7 / d);
                map3.put(SKEWNESS, d8 / d / Math.pow(d7 / d, 1.5) - 3.0);
                map3.put(KURTOSIS, d * d9 / (d7 * d7) - 3.0);
            }
        }
        object = map.get(string);
        return DataUtils.getValueOrDefault((Number)object, Double.NaN);
    }

    @Override
    public void dataAdded(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.a(dataChangeEventArray);
    }

    @Override
    public void dataUpdated(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.a(dataChangeEventArray);
    }

    @Override
    public void dataRemoved(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.a(dataChangeEventArray);
    }

    private void a(DataChangeEvent ... dataChangeEventArray) {
        for (DataChangeEvent dataChangeEvent : dataChangeEventArray) {
            this.invalidate(dataChangeEvent.getCol(), dataChangeEvent.getRow());
        }
    }

    protected void invalidate(int n, int n2) {
        this.b.clear();
        if (n < this.c.size()) {
            this.c.get(n).clear();
        }
        if (n2 < this.d.size()) {
            this.d.get(n2).clear();
        }
    }
}

