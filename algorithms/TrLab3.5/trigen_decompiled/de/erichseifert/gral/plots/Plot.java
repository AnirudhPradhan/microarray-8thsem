/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots;

import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.graphics.Container;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.graphics.Location;
import de.erichseifert.gral.plots.PlotArea;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.legends.Legend;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.Collection;
import java.util.List;

public interface Plot
extends Container,
Drawable {
    public Axis getAxis(String var1);

    public void setAxis(String var1, Axis var2);

    public void removeAxis(String var1);

    public Collection<String> getAxesNames();

    public void autoscaleAxis(String var1);

    public AxisRenderer getAxisRenderer(String var1);

    public void setAxisRenderer(String var1, AxisRenderer var2);

    public PlotArea getPlotArea();

    public Label getTitle();

    public Legend getLegend();

    public void add(DataSource var1);

    public void add(DataSource var1, boolean var2);

    public void add(int var1, DataSource var2, boolean var3);

    public boolean contains(DataSource var1);

    public DataSource get(int var1);

    public boolean remove(DataSource var1);

    public void clear();

    public String[] getMapping(DataSource var1);

    public void setMapping(DataSource var1, String ... var2);

    public List<DataSource> getData();

    public List<DataSource> getVisibleData();

    public boolean isVisible(DataSource var1);

    public void setVisible(DataSource var1, boolean var2);

    public Paint getBackground();

    public void setBackground(Paint var1);

    public Stroke getBorderStroke();

    public void setBorderStroke(Stroke var1);

    public Paint getBorderColor();

    public void setBorderColor(Paint var1);

    public Font getFont();

    public void setFont(Font var1);

    public boolean isLegendVisible();

    public void setLegendVisible(boolean var1);

    public Location getLegendLocation();

    public void setLegendLocation(Location var1);

    public double getLegendDistance();

    public void setLegendDistance(double var1);
}

