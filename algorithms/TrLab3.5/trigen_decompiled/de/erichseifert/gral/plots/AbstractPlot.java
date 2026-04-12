/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots;

import de.erichseifert.gral.data.Column;
import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataListener;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.graphics.Container;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawableContainer;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.graphics.Location;
import de.erichseifert.gral.graphics.layout.EdgeLayout;
import de.erichseifert.gral.graphics.layout.OuterEdgeLayout;
import de.erichseifert.gral.plots.Plot;
import de.erichseifert.gral.plots.PlotArea;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.legends.Legend;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.MathUtils;
import de.erichseifert.gral.util.SerializationUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractPlot
extends DrawableContainer
implements DataListener,
Plot {
    private static final long serialVersionUID = -6609155385940228771L;
    private final List<DataSource> a;
    private final Set<DataSource> b = new HashSet<DataSource>();
    private final Map<String, Axis> c = new HashMap<String, Axis>();
    private final Map<String, AxisRenderer> d = new HashMap<String, AxisRenderer>();
    private final Map<String, Drawable> e = new HashMap<String, Drawable>();
    private final Map<Column, String> f = new HashMap<Column, String>();
    private final Map<String, Double> g = new HashMap<String, Double>();
    private final Map<String, Double> h = new HashMap<String, Double>();
    private final Label i;
    private PlotArea j;
    private final Container k;
    private Legend l;
    private Paint m;
    private transient Stroke n;
    private Paint o;
    private Font p;
    private boolean q;
    private Location r;
    private double s;

    public AbstractPlot(DataSource ... dataSourceArray) {
        super(new EdgeLayout());
        this.a = new LinkedList<DataSource>();
        for (DataSource dataSource : dataSourceArray) {
            this.add(dataSource);
        }
        this.m = null;
        this.n = null;
        this.o = Color.BLACK;
        this.p = Font.decode(null);
        this.a();
        this.i = new Label();
        this.i.setFont(this.p.deriveFont(1.5f * this.p.getSize2D()));
        this.add(this.i, (Object)Location.NORTH);
        this.k = new DrawableContainer(new OuterEdgeLayout(0.0));
        this.r = Location.CENTER;
        this.s = 2.0;
        this.q = false;
        this.refreshLegendLayout();
    }

    @Override
    public void draw(DrawingContext drawingContext) {
        Graphics2D graphics2D = drawingContext.getGraphics();
        Object object = this.getBackground();
        if (object != null) {
            GraphicsUtils.fillPaintedShape(graphics2D, this.getBounds(), (Paint)object, null);
        }
        if ((object = this.getBorderStroke()) != null) {
            Paint paint = this.getBorderColor();
            GraphicsUtils.drawPaintedShape(graphics2D, this.getBounds(), paint, null, (Stroke)object);
        }
        this.drawComponents(drawingContext);
    }

    protected void drawAxes(DrawingContext drawingContext) {
        for (Drawable drawable : this.e.values()) {
            if (drawable == null) continue;
            drawable.draw(drawingContext);
        }
    }

    protected void drawLegend(DrawingContext drawingContext) {
        if (!this.isLegendVisible() || this.getLegend() == null) {
            return;
        }
        this.getLegend().draw(drawingContext);
    }

    @Override
    public void layout() {
        super.layout();
        this.layoutAxes();
        this.layoutLegend();
    }

    protected void layoutAxes() {
    }

    protected void layoutLegend() {
        if (this.getPlotArea() == null) {
            return;
        }
        Container container = this.getLegendContainer();
        Rectangle2D rectangle2D = this.getPlotArea().getBounds();
        container.setBounds(rectangle2D);
    }

    @Override
    public Axis getAxis(String string) {
        return this.c.get(string);
    }

    @Override
    public void setAxis(String string, Axis axis) {
        if (axis == null) {
            this.removeAxis(string);
            return;
        }
        this.c.put(string, axis);
    }

    @Override
    public void removeAxis(String string) {
        this.c.remove(string);
        this.d.remove(string);
        this.e.remove(string);
    }

    @Override
    public Collection<String> getAxesNames() {
        return this.c.keySet();
    }

    protected void createDefaultAxes() {
    }

    protected void createDefaultAxisRenderers() {
    }

    protected void autoscaleAxes() {
        if (this.a.isEmpty()) {
            return;
        }
        for (String string : this.getAxesNames()) {
            this.autoscaleAxis(string);
        }
    }

    @Override
    public void autoscaleAxis(String string) {
        Axis axis = this.getAxis(string);
        if (axis == null || !axis.isAutoscaled()) {
            return;
        }
        double d = this.getAxisMin(string);
        double d2 = this.getAxisMax(string);
        double d3 = 0.0 * (d2 - d);
        axis.setRange(d - d3, d2 + d3);
    }

    @Override
    public AxisRenderer getAxisRenderer(String string) {
        return this.d.get(string);
    }

    @Override
    public void setAxisRenderer(String object, AxisRenderer object2) {
        Object object3 = null;
        if (object2 == null) {
            this.d.remove(object);
        } else {
            this.d.put((String)object, (AxisRenderer)object2);
            object3 = this.getAxis((String)object);
            object3 = object2.getRendererComponent((Axis)object3);
        }
        object2 = object;
        object = this;
        if (object3 == null) {
            ((AbstractPlot)object).e.remove(object2);
        } else {
            ((AbstractPlot)object).e.put((String)object2, (Drawable)object3);
        }
        this.layout();
    }

    protected Drawable getAxisComponent(String string) {
        return this.e.get(string);
    }

    @Override
    public PlotArea getPlotArea() {
        return this.j;
    }

    protected void setPlotArea(PlotArea plotArea) {
        if (this.j != null) {
            this.remove(this.j);
            this.j.setBaseFont(null);
        }
        this.j = plotArea;
        if (this.j != null) {
            this.j.setBaseFont(this.p);
            this.add(this.j, (Object)Location.CENTER);
        }
    }

    @Override
    public Label getTitle() {
        return this.i;
    }

    protected Container getLegendContainer() {
        return this.k;
    }

    @Override
    public Legend getLegend() {
        return this.l;
    }

    protected void setLegend(Legend legend) {
        if (this.l != null) {
            this.k.remove(this.l);
            this.l.clear();
            this.l.setBaseFont(null);
        }
        this.l = legend;
        if (this.l != null) {
            this.l.setBaseFont(this.p);
            Location location = this.getLegendLocation();
            this.k.add(legend, (Object)location);
            for (DataSource dataSource : this.getVisibleData()) {
                legend.add(dataSource);
            }
        }
    }

    protected void refreshLegendLayout() {
        double d = 0.0;
        if (MathUtils.isCalculatable(this.s)) {
            d = this.s * (double)this.p.getSize2D();
        }
        OuterEdgeLayout outerEdgeLayout = new OuterEdgeLayout(d);
        this.k.setLayout(outerEdgeLayout);
    }

    @Override
    public Paint getBackground() {
        return this.m;
    }

    @Override
    public void setBackground(Paint paint) {
        this.m = paint;
    }

    @Override
    public Stroke getBorderStroke() {
        return this.n;
    }

    @Override
    public void setBorderStroke(Stroke stroke) {
        this.n = stroke;
    }

    @Override
    public Paint getBorderColor() {
        return this.o;
    }

    @Override
    public void setBorderColor(Paint paint) {
        this.o = paint;
    }

    @Override
    public Font getFont() {
        return this.p;
    }

    @Override
    public void setFont(Font font) {
        this.p = font;
        this.a();
    }

    private void a() {
        float f = 2.0f * this.p.getSize2D();
        this.getLayout().setGapX(f);
        this.getLayout().setGapY(f);
        if (this.j != null) {
            this.j.setBaseFont(this.p);
        }
        if (this.l != null) {
            this.l.setBaseFont(this.p);
        }
    }

    @Override
    public boolean isLegendVisible() {
        return this.q;
    }

    @Override
    public void setLegendVisible(boolean bl) {
        this.q = bl;
    }

    @Override
    public Location getLegendLocation() {
        return this.r;
    }

    @Override
    public void setLegendLocation(Location location) {
        this.r = location;
        if (this.l != null) {
            this.k.remove(this.l);
            this.k.add(this.l, (Object)this.r);
        }
    }

    @Override
    public double getLegendDistance() {
        return this.s;
    }

    @Override
    public void setLegendDistance(double d) {
        this.s = d;
        this.refreshLegendLayout();
    }

    @Override
    public void add(DataSource dataSource) {
        this.add(dataSource, true);
    }

    @Override
    public void add(DataSource dataSource, boolean bl) {
        this.add(this.a.size(), dataSource, bl);
    }

    @Override
    public void add(int n, DataSource dataSource, boolean bl) {
        this.a.add(n, dataSource);
        if (bl) {
            this.b.add(dataSource);
        }
        this.autoscaleAxes();
        if (this.getLegend() != null) {
            this.getLegend().add(dataSource);
        }
        dataSource.addDataListener(this);
        this.b();
    }

    @Override
    public boolean contains(DataSource dataSource) {
        return this.a.contains(dataSource);
    }

    @Override
    public DataSource get(int n) {
        return this.a.get(n);
    }

    @Override
    public boolean remove(DataSource dataSource) {
        dataSource.removeDataListener(this);
        this.b.remove(dataSource);
        if (this.getLegend() != null) {
            this.getLegend().remove(dataSource);
        }
        boolean bl = this.a.remove(dataSource);
        this.b();
        return bl;
    }

    @Override
    public void clear() {
        for (DataSource dataSource : this.a) {
            dataSource.removeDataListener(this);
        }
        this.b.clear();
        if (this.getLegend() != null) {
            this.getLegend().clear();
        }
        this.a.clear();
        this.b();
    }

    @Override
    public String[] getMapping(DataSource dataSource) {
        String[] stringArray = new String[dataSource.getColumnCount()];
        for (int i = 0; i < stringArray.length; ++i) {
            Object object;
            int n = i;
            Object object2 = this;
            Iterable<Comparable<?>> iterable = dataSource;
            if (!((AbstractPlot)object2).contains((DataSource)iterable)) {
                object = null;
            } else {
                iterable = iterable.getColumn(n);
                object = object2 = ((AbstractPlot)object2).f.get(iterable);
            }
            stringArray[i] = object;
        }
        return stringArray;
    }

    @Override
    public void setMapping(DataSource dataSource, String ... stringArray) {
        if (!this.contains(dataSource)) {
            throw new IllegalArgumentException("Data source does not exist in plot.");
        }
        if (stringArray.length > dataSource.getColumnCount()) {
            throw new IllegalArgumentException(MessageFormat.format("Data source only has {0,number,integer} column, {1,number,integer} values given.", dataSource.getColumnCount(), stringArray.length));
        }
        for (int i = 0; i < stringArray.length; ++i) {
            String string = stringArray[i];
            if (string == null) continue;
            Column column = dataSource.getColumn(i);
            this.f.put(column, string);
        }
        this.b();
    }

    protected Double getAxisMin(String string) {
        Double d = this.g.get(string);
        if (d == null) {
            this.c();
            d = this.g.get(string);
        }
        if (d == null) {
            d = 0.0;
        }
        return d;
    }

    protected Double getAxisMax(String string) {
        Double d = this.h.get(string);
        if (d == null) {
            this.c();
            d = this.h.get(string);
        }
        if (d == null) {
            return 0.0;
        }
        return d;
    }

    @Override
    public List<DataSource> getData() {
        return Collections.unmodifiableList(this.a);
    }

    @Override
    public List<DataSource> getVisibleData() {
        LinkedList<DataSource> linkedList = new LinkedList<DataSource>();
        for (DataSource dataSource : this.a) {
            if (!this.b.contains(dataSource)) continue;
            linkedList.add(dataSource);
        }
        return linkedList;
    }

    @Override
    public boolean isVisible(DataSource dataSource) {
        return this.b.contains(dataSource);
    }

    @Override
    public void setVisible(DataSource dataSource, boolean bl) {
        if (bl) {
            if (this.b.add(dataSource)) {
                this.b();
                return;
            }
        } else if (this.b.remove(dataSource)) {
            this.b();
        }
    }

    @Override
    public void dataAdded(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.dataChanged(dataSource, dataChangeEventArray);
    }

    @Override
    public void dataUpdated(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.dataChanged(dataSource, dataChangeEventArray);
    }

    @Override
    public void dataRemoved(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.dataChanged(dataSource, dataChangeEventArray);
    }

    protected void dataChanged(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        this.b();
        if (this.getLegend() != null) {
            this.getLegend().refresh();
        }
        this.autoscaleAxes();
        this.layout();
    }

    private void b() {
        this.g.clear();
        this.h.clear();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void c() {
        AbstractPlot abstractPlot = this;
        synchronized (abstractPlot) {
            for (Map.Entry<Column, String> entry : this.f.entrySet()) {
                Column column = entry.getKey();
                if (column.size() == 0) continue;
                entry = entry.getValue();
                Double d = this.g.get(entry);
                Double d2 = this.h.get(entry);
                if (d == null || d2 == null) {
                    d = column.getStatistics("min");
                    d2 = column.getStatistics("max");
                } else {
                    d = Math.min(d, column.getStatistics("min"));
                    d2 = Math.max(d2, column.getStatistics("max"));
                }
                this.g.put((String)((Object)entry), d);
                this.h.put((String)((Object)entry), d2);
            }
            return;
        }
    }

    private void readObject(ObjectInputStream object) throws ClassNotFoundException, IOException {
        ((ObjectInputStream)object).defaultReadObject();
        this.n = (Stroke)SerializationUtils.unwrap((Serializable)((ObjectInputStream)object).readObject());
        for (DataSource dataSource : this.getData()) {
            dataSource.addDataListener(this);
        }
    }

    private void writeObject(ObjectOutputStream object) throws ClassNotFoundException, IOException {
        ((ObjectOutputStream)object).defaultWriteObject();
        ((ObjectOutputStream)object).writeObject(SerializationUtils.wrap(this.n));
        for (DataSource dataSource : this.getData()) {
            dataSource.addDataListener(this);
        }
    }
}

