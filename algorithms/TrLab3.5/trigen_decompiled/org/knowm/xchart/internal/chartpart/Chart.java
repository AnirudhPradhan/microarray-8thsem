/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.ChartPart;
import org.knowm.xchart.internal.chartpart.ChartTitle;
import org.knowm.xchart.internal.chartpart.Legend_;
import org.knowm.xchart.internal.chartpart.Plot_;
import org.knowm.xchart.style.Styler;

public abstract class Chart<ST extends Styler, S extends Series>
implements ChartPart {
    protected ST styler;
    private int width;
    private int height;
    private String title = "";
    private String xAxisTitle = "";
    private String yAxisTitle = "";
    protected ChartTitle chartTitle;
    protected Legend_ legend;
    protected Plot_ plot;
    protected AxisPair axisPair;
    protected Map<String, S> seriesMap = new LinkedHashMap<String, S>();

    public abstract void paint(Graphics2D var1, int var2, int var3);

    public Chart(int width, int height, ST styler) {
        this.width = width;
        this.height = height;
        this.styler = styler;
        this.chartTitle = new ChartTitle(this);
    }

    public List<Double> getNumberListFromDoubleArray(double[] data) {
        if (data == null) {
            return null;
        }
        ArrayList<Double> dataNumber = null;
        if (data != null) {
            dataNumber = new ArrayList<Double>();
            for (double d : data) {
                dataNumber.add(new Double(d));
            }
        }
        return dataNumber;
    }

    public List<Double> getNumberListFromIntArray(int[] data) {
        if (data == null) {
            return null;
        }
        ArrayList<Double> dataNumber = null;
        if (data != null) {
            dataNumber = new ArrayList<Double>();
            int[] nArray = data;
            int n = nArray.length;
            for (int i = 0; i < n; ++i) {
                double d = nArray[i];
                dataNumber.add(new Double(d));
            }
        }
        return dataNumber;
    }

    public List<Double> getGeneratedData(int length) {
        ArrayList<Double> generatedData = new ArrayList<Double>();
        for (int i = 1; i < length + 1; ++i) {
            generatedData.add(Double.valueOf(i));
        }
        return generatedData;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXAxisTitle() {
        return this.xAxisTitle;
    }

    public void setXAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
    }

    public String getyYAxisTitle() {
        return this.yAxisTitle;
    }

    public void setYAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
    }

    protected ChartTitle getChartTitle() {
        return this.chartTitle;
    }

    protected Legend_ getLegend() {
        return this.legend;
    }

    protected Plot_ getPlot() {
        return this.plot;
    }

    protected Axis getXAxis() {
        return this.axisPair.getXAxis();
    }

    protected Axis getYAxis() {
        return this.axisPair.getYAxis();
    }

    public AxisPair getAxisPair() {
        return this.axisPair;
    }

    public Map<String, S> getSeriesMap() {
        return this.seriesMap;
    }

    public S removeSeries(String seriesName) {
        return (S)((Series)this.seriesMap.remove(seriesName));
    }

    public ST getStyler() {
        return this.styler;
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(0.0, 0.0, this.width, this.height);
    }
}

