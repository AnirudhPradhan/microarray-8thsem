/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots;

import de.erichseifert.gral.data.Column;
import de.erichseifert.gral.data.DataAccessor;
import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.graphics.Location;
import de.erichseifert.gral.navigation.AbstractNavigator;
import de.erichseifert.gral.navigation.Navigable;
import de.erichseifert.gral.navigation.Navigator;
import de.erichseifert.gral.plots.AbstractPlot;
import de.erichseifert.gral.plots.PlotArea;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.axes.LinearRenderer2D;
import de.erichseifert.gral.plots.colors.ContinuousColorMapper;
import de.erichseifert.gral.plots.colors.QuasiRandomColors;
import de.erichseifert.gral.plots.legends.AbstractLegend;
import de.erichseifert.gral.plots.legends.ValueLegend;
import de.erichseifert.gral.plots.points.AbstractPointRenderer;
import de.erichseifert.gral.plots.points.PointData;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.util.GeometryUtils;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.MathUtils;
import de.erichseifert.gral.util.PointND;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PiePlot
extends AbstractPlot
implements Navigable {
    private static final long serialVersionUID = 5486418164040578150L;
    public static final String AXIS_TANGENTIAL = "tangential";
    private final Map<DataSource, PointRenderer> a;
    private transient Map<DataSource, List<Slice>> b;
    private transient PiePlotNavigator c;
    private final Point2D d = new Point2D.Double(0.5, 0.5);
    private double e = 1.0;
    private double f = 0.0;
    private boolean g = true;

    public PiePlot(DataSource dataSource) {
        super(new DataSource[0]);
        this.a = new HashMap<DataSource, PointRenderer>();
        this.b = new HashMap<DataSource, List<Slice>>();
        this.setPlotArea(new PiePlotArea2D(this));
        this.setLegend(new PiePlotLegend(this));
        this.add(dataSource);
        this.createDefaultAxes();
        this.createDefaultAxisRenderers();
        this.dataUpdated(dataSource, new DataChangeEvent[0]);
    }

    @Override
    protected void createDefaultAxes() {
        Axis axis = new Axis();
        this.setAxis(AXIS_TANGENTIAL, axis);
    }

    @Override
    public void autoscaleAxis(String object) {
        if (!AXIS_TANGENTIAL.equals(object)) {
            super.autoscaleAxis((String)object);
            return;
        }
        Iterable<DataSource> iterable = this.getVisibleData();
        if (iterable.isEmpty()) {
            return;
        }
        if ((iterable = iterable.get(0)).getRowCount() == 0) {
            return;
        }
        double d = this.getSum((DataSource)iterable);
        if (d == 0.0) {
            return;
        }
        if ((object = this.getAxis((String)object)) == null || !((Axis)object).isAutoscaled()) {
            return;
        }
        ((Axis)object).setRange(0.0, d);
    }

    @Override
    protected void createDefaultAxisRenderers() {
        LinearRenderer2D linearRenderer2D = new LinearRenderer2D();
        Ellipse2D.Double double_ = new Ellipse2D.Double(-1.0, -1.0, 2.0, 2.0);
        linearRenderer2D.setShape(double_);
        linearRenderer2D.setShapeVisible(false);
        this.setAxisRenderer(AXIS_TANGENTIAL, linearRenderer2D);
    }

    @Override
    public void add(int n, DataSource dataSource, boolean bl) {
        if (this.getData().size() != 0) {
            throw new IllegalArgumentException("This plot type only supports a single data source.");
        }
        super.add(n, dataSource, bl);
        PieSliceRenderer pieSliceRenderer = new PieSliceRenderer(this);
        this.setPointRenderer(dataSource, pieSliceRenderer);
        this.setMapping(dataSource, AXIS_TANGENTIAL);
    }

    public PointRenderer getPointRenderer(DataSource dataSource) {
        return this.a.get(dataSource);
    }

    public void setPointRenderer(DataSource dataSource, PointRenderer pointRenderer) {
        this.a.put(dataSource, pointRenderer);
    }

    @Override
    public Navigator getNavigator() {
        if (this.c == null) {
            this.c = new PiePlotNavigator(this);
        }
        return this.c;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected Slice getSlice(DataSource iterable, int n) {
        if (n < 0) {
            return null;
        }
        Map<DataSource, List<Slice>> map = this.b;
        synchronized (map) {
            Object object3;
            Object object2;
            if (!this.b.containsKey(iterable) && ((AbstractPlot)(object2 = this)).isVisible((DataSource)(object3 = iterable))) {
                Column column = object3.getColumn(0);
                ArrayList<Object> arrayList = new ArrayList<Object>(column.size());
                ((PiePlot)object2).b.put((DataSource)object3, arrayList);
                double d = 0.0;
                for (Object object3 : column) {
                    object3 = (Number)object3;
                    double d2 = 0.0;
                    if (MathUtils.isCalculatable((Number)object3)) {
                        d2 = ((Number)object3).doubleValue();
                    }
                    double d3 = Math.abs(d2);
                    object3 = new Slice(d, d + d3);
                    arrayList.add(object3);
                    d += d3;
                }
            }
            iterable = this.b.get(iterable);
        }
        if (iterable == null || n >= iterable.size()) {
            return null;
        }
        return (Slice)iterable.get(n);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected double getSum(DataSource object) {
        double d = 0.0;
        DataSource dataSource = object;
        synchronized (dataSource) {
            object = this.getSlice((DataSource)object, object.getRowCount() - 1);
            if (object != null) {
                d = ((Slice)object).end;
            }
        }
        return d;
    }

    protected void revalidate(DataSource dataSource) {
        this.b.remove(dataSource);
        this.autoscaleAxes();
    }

    @Override
    protected void dataChanged(DataSource dataSource, DataChangeEvent ... dataChangeEventArray) {
        super.dataChanged(dataSource, dataChangeEventArray);
        this.revalidate(dataSource);
    }

    private void readObject(ObjectInputStream object) throws ClassNotFoundException, IOException {
        ((ObjectInputStream)object).defaultReadObject();
        this.b = new HashMap<DataSource, List<Slice>>();
        for (DataSource dataSource : this.getData()) {
            this.dataUpdated(dataSource, new DataChangeEvent[0]);
        }
    }

    public Point2D getCenter() {
        return this.d;
    }

    public void setCenter(Point2D point2D) {
        this.d.setLocation(point2D);
    }

    public double getRadius() {
        return this.e;
    }

    public void setRadius(double d) {
        this.e = d;
    }

    public double getStart() {
        return this.f;
    }

    public void setStart(double d) {
        Shape shape;
        double d2 = this.f;
        this.f = d;
        AxisRenderer axisRenderer = this.getAxisRenderer(AXIS_TANGENTIAL);
        if (axisRenderer != null && (shape = axisRenderer.getShape()) != null) {
            double d3 = Math.toRadians(d2 - d);
            AffineTransform affineTransform = AffineTransform.getRotateInstance(d3);
            shape = affineTransform.createTransformedShape(shape);
            axisRenderer.setShape(shape);
        }
    }

    public boolean isClockwise() {
        return this.g;
    }

    public void setClockwise(boolean bl) {
        Shape shape;
        this.g = bl;
        AxisRenderer axisRenderer = this.getAxisRenderer(AXIS_TANGENTIAL);
        if (axisRenderer != null && (shape = axisRenderer.getShape()) != null) {
            shape = GeometryUtils.reverse(shape);
            axisRenderer.setShape(shape);
        }
    }

    public static class PiePlotLegend
    extends ValueLegend {
        private static final long serialVersionUID = 309673490751330686L;
        private final PiePlot a;

        public PiePlotLegend(PiePlot piePlot) {
            this.a = piePlot;
        }

        @Override
        protected Iterable<Row> getEntries(DataSource object) {
            object = super.getEntries((DataSource)object);
            LinkedList<Row> linkedList = new LinkedList<Row>();
            object = object.iterator();
            while (object.hasNext()) {
                Number number;
                Row row = (Row)object.next();
                if (!row.isColumnNumeric(0)) continue;
                Number number2 = (Number)((Object)row.get(0));
                boolean bl = number.doubleValue() < 0.0;
                if (bl) continue;
                linkedList.add(row);
            }
            return linkedList;
        }

        @Override
        public Drawable getSymbol(Row row) {
            return new AbstractLegend.AbstractSymbol(this, this, row){
                private static final long serialVersionUID = -5460249256507481057L;
                private /* synthetic */ Row a;
                private /* synthetic */ PiePlotLegend b;
                {
                    this.b = piePlotLegend;
                    this.a = row;
                    super(legend);
                }

                @Override
                public final void draw(DrawingContext drawingContext) {
                    Object object = this.a.getSource();
                    Rectangle2D rectangle2D = this.getBounds();
                    object = this.b.a.getPointRenderer((DataSource)object);
                    Object object2 = new Rectangle2D.Double(0.0, 0.0, rectangle2D.getWidth(), rectangle2D.getHeight());
                    if (object == null) {
                        return;
                    }
                    Object object3 = new PointData(Arrays.asList(new Axis[]{null}), Arrays.asList(new AxisRenderer[]{null}), this.a, 0);
                    object = object.getPoint((PointData)object3, (Shape)object2);
                    object2 = drawingContext.getGraphics();
                    object3 = ((Graphics2D)object2).getTransform();
                    ((Graphics2D)object2).translate(rectangle2D.getX(), rectangle2D.getY());
                    object.draw(drawingContext);
                    ((Graphics2D)object2).setTransform((AffineTransform)object3);
                }
            };
        }
    }

    public static class PieSliceRenderer
    extends AbstractPointRenderer {
        private static final long serialVersionUID = 1135636437801090607L;
        private final PiePlot a;
        private double b;
        private double c;
        private double d;

        public PieSliceRenderer(PiePlot piePlot) {
            this.a = piePlot;
            this.setValueColumn(0);
            this.setErrorColumnTop(1);
            this.setErrorColumnBottom(2);
            this.setColor(new QuasiRandomColors());
            this.b = 1.0;
            this.c = 0.0;
            this.d = 0.0;
        }

        public double getOuterRadius() {
            return this.b;
        }

        public void setOuterRadius(double d) {
            this.b = d;
        }

        public double getInnerRadius() {
            return this.c;
        }

        public void setInnerRadius(double d) {
            this.c = d;
        }

        public double getGap() {
            return this.d;
        }

        public void setGap(double d) {
            this.d = d;
        }

        @Override
        public Drawable getPoint(PointData pointData, Shape shape) {
            return new AbstractDrawable(this, pointData, shape){
                private static final long serialVersionUID = -1783451355453643712L;
                private /* synthetic */ PointData a;
                private /* synthetic */ Shape b;
                private /* synthetic */ PieSliceRenderer c;
                {
                    this.c = pieSliceRenderer;
                    this.a = pointData;
                    this.b = shape;
                }

                @Override
                public final void draw(DrawingContext drawingContext) {
                    Object object = this.c;
                    Row row = this.a.row;
                    if (this.b == null) {
                        return;
                    }
                    Slice slice = this.c.a.getSlice(row.getSource(), row.getIndex());
                    if (slice == null) {
                        return;
                    }
                    if ((object = object.getColor()) instanceof ContinuousColorMapper) {
                        double d = this.c.a.getSum(row.getSource());
                        if (d == 0.0) {
                            return;
                        }
                        double d2 = slice.start / d;
                        double d3 = slice.end / d;
                        double d4 = 0.0;
                        int n = row.getSource().getRowCount();
                        if (n > 1) {
                            double d5 = (double)row.getIndex() / (double)(n - 1);
                            double d6 = 1.0 - d5;
                            d4 = d6 * d2 + d5 * d3;
                        }
                        object = ((ContinuousColorMapper)object).get(d4);
                    } else {
                        object = object.get(row.getIndex());
                    }
                    GraphicsUtils.fillPaintedShape(drawingContext.getGraphics(), this.b, (Paint)object, null);
                }
            };
        }

        @Override
        public Shape getPointShape(PointData pointData) {
            double d;
            double d2;
            Object object = pointData.row;
            int n = pointData.col;
            Object object2 = (Number)((Object)((Row)object).get(n));
            if (!MathUtils.isCalculatable((Number)object2) || ((Number)object2).doubleValue() <= 0.0) {
                return null;
            }
            object2 = this.getValueFont();
            double d3 = ((Font)object2).getSize2D();
            object2 = this.a.getPlotArea();
            double d4 = Math.min(((AbstractDrawable)object2).getWidth(), ((AbstractDrawable)object2).getHeight()) / 2.0;
            double d5 = this.a.getRadius();
            double d6 = d4 * d5;
            double d7 = this.getOuterRadius();
            double d8 = d6 * d7;
            double d9 = this.a.getSum(((DataAccessor)object).getSource());
            if (d9 == 0.0) {
                return null;
            }
            object2 = this.a.getSlice(((DataAccessor)object).getSource(), ((DataAccessor)object).getIndex());
            if (object2 == null) {
                return null;
            }
            double d10 = ((Slice)object2).start / d9;
            double d11 = ((Slice)object2).end / d9;
            double d12 = this.a.getStart();
            double d13 = (d11 - d10) * 360.0;
            double d14 = this.a.isClockwise() ? d12 - d11 * 360.0 : d12 + d10 * 360.0;
            MathUtils.normalizeDegrees(d12);
            object2 = new Arc2D.Double(-d8, -d8, d8 * 2.0, d8 * 2.0, d14, d13, 2);
            object = new Area((Shape)object2);
            double d15 = this.getGap();
            if (d2 > 0.0) {
                BasicStroke basicStroke = new BasicStroke((float)(d15 * d3));
                Area area = new Area(basicStroke.createStrokedShape((Shape)object2));
                ((Area)object).subtract(area);
            }
            double d16 = this.getInnerRadius();
            if (d > 0.0 && d16 < d7) {
                double d17 = d6 * d16;
                object2 = new Ellipse2D.Double(-d17, -d17, d17 * 2.0, d17 * 2.0);
                object2 = new Area((Shape)object2);
                ((Area)object).subtract((Area)object2);
            }
            return object;
        }

        protected void drawValueLabel(DrawingContext drawingContext, Slice object, double d, Row row, int n) {
            double d2;
            double d3;
            Object object2 = row.get(n);
            Object object3 = this.getValueFormat();
            if (object3 == null && object2 instanceof Number) {
                object3 = NumberFormat.getInstance();
            }
            object2 = object3 != null ? ((Format)object3).format(object2) : object2.toString();
            object3 = this.getValueColor();
            object3 = object3.get(row.getIndex());
            Font font = this.getValueFont();
            double d4 = font.getSize2D();
            Location location = this.getValueLocation();
            double d5 = this.getValueAlignmentX();
            double d6 = this.getValueAlignmentY();
            double d7 = this.getValueRotation();
            double d8 = this.getValueDistance();
            d8 = MathUtils.isCalculatable(d8) ? (d8 *= d4) : 0.0;
            double d9 = this.getOuterRadius();
            double d10 = this.getInnerRadius();
            double d11 = d * d9;
            double d12 = d * d10;
            double d13 = d8;
            if (location == Location.NORTH) {
                d3 = d11 + d13;
            } else if (location == Location.SOUTH) {
                d3 = Math.max(d12 - d13, 0.0);
            } else {
                d2 = d11 - d12;
                if (d8 * 2.0 >= d2) {
                    d6 = 0.5;
                    d13 = 0.0;
                }
                d3 = d12 + d13 + d6 * (d2 - d13 * 2.0);
            }
            d2 = this.a.getSum(row.getSource());
            if (d2 == 0.0) {
                return;
            }
            double d14 = d3 * 2.0 * Math.PI;
            double d15 = d8 / d14;
            double d16 = ((Slice)object).end / d2;
            double d17 = ((Slice)object).start / d2;
            double d18 = d16 - d17;
            if (d15 * 2.0 >= d18) {
                d5 = 0.5;
                d15 = 0.0;
            }
            double d19 = d17 + d15 + d5 * (d18 - d15 * 2.0);
            double d20 = this.a.getStart();
            double d21 = Math.toRadians(-d20);
            double d22 = 1.0;
            if (!this.a.isClockwise()) {
                d22 = -1.0;
            }
            double d23 = d21 + d22 * d19 * 2.0 * Math.PI;
            double d24 = Math.cos(d23);
            double d25 = Math.sin(d23);
            object = new Label((String)object2);
            ((Label)object).setAlignmentX(1.0 - d24 * 0.5 - 0.5);
            ((Label)object).setAlignmentY(d25 * 0.5 + 0.5);
            ((Label)object).setRotation(d7);
            ((Label)object).setColor((Paint)object3);
            ((Label)object).setFont(font);
            Dimension2D dimension2D = ((Label)object).getPreferredSize();
            double d26 = 0.5;
            double d27 = 0.5;
            if (location == Location.NORTH || location == Location.SOUTH) {
                d26 = d24 * dimension2D.getWidth() / 2.0;
                d27 = d25 * dimension2D.getHeight() / 2.0;
                if (location == Location.SOUTH) {
                    d26 = -d26;
                    d27 = -d27;
                }
            }
            double d28 = d3 * d24 + d26 - dimension2D.getWidth() / 2.0;
            double d29 = d3 * d25 + d27 - dimension2D.getHeight() / 2.0;
            double d30 = dimension2D.getWidth();
            double d31 = dimension2D.getHeight();
            ((Label)object).setBounds(d28, d29, d30, d31);
            ((Label)object).draw(drawingContext);
        }

        @Override
        public Drawable getValue(PointData object, Shape shape) {
            object = new AbstractDrawable(this, (PointData)object, shape){
                private static final long serialVersionUID = 8389872806138135038L;
                private /* synthetic */ PointData a;
                private /* synthetic */ Shape b;
                private /* synthetic */ PieSliceRenderer c;
                {
                    this.c = pieSliceRenderer;
                    this.a = pointData;
                    this.b = shape;
                }

                @Override
                public final void draw(DrawingContext drawingContext) {
                    PieSliceRenderer pieSliceRenderer = this.c;
                    Row row = this.a.row;
                    if (this.b == null) {
                        return;
                    }
                    Slice slice = this.c.a.getSlice(row.getSource(), row.getIndex());
                    if (slice == null) {
                        return;
                    }
                    PlotArea plotArea = this.c.a.getPlotArea();
                    double d = Math.min(plotArea.getWidth(), plotArea.getHeight()) / 2.0;
                    double d2 = this.c.a.getRadius();
                    double d3 = d * d2;
                    if (pieSliceRenderer.isValueVisible()) {
                        int n = pieSliceRenderer.getValueColumn();
                        this.c.drawValueLabel(drawingContext, slice, d3, row, n);
                    }
                }
            };
            return object;
        }
    }

    protected static final class Slice {
        public final double start;
        public final double end;

        public Slice(double d, double d2) {
            this.start = d;
            this.end = d2;
        }
    }

    public static class PiePlotArea2D
    extends PlotArea {
        private static final long serialVersionUID = 5646816099037852271L;
        private final PiePlot a;

        public PiePlotArea2D(PiePlot piePlot) {
            this.a = piePlot;
        }

        @Override
        public void draw(DrawingContext drawingContext) {
            this.drawBackground(drawingContext);
            this.drawBorder(drawingContext);
            this.drawPlot(drawingContext);
            this.a.drawLegend(drawingContext);
        }

        /*
         * WARNING - void declaration
         */
        @Override
        protected void drawPlot(DrawingContext drawingContext) {
            void var7_12;
            Graphics2D graphics2D = drawingContext.getGraphics();
            Shape shape = graphics2D.getClip();
            Insets2D insets2D = this.getClippingOffset();
            if (insets2D != null) {
                void var7_9;
                double d = this.getBaseFont().getSize2D();
                Rectangle2D.Double object2 = new Rectangle2D.Double(this.getX() + insets2D.getLeft() * d, this.getY() + insets2D.getTop() * d, this.getWidth() - insets2D.getHorizontal() * d, this.getHeight() - insets2D.getVertical() * d);
                if (shape != null) {
                    Area area = new Area(shape);
                    area.intersect(new Area(object2));
                    Object object = area;
                }
                graphics2D.setClip((Shape)var7_9);
            }
            AffineTransform affineTransform = graphics2D.getTransform();
            graphics2D.translate(this.getX(), this.getY());
            Rectangle2D rectangle2D = this.getBounds();
            Point2D point2D = this.a.getCenter();
            if (point2D == null) {
                Point2D.Double double_ = new Point2D.Double(0.5, 0.5);
            }
            graphics2D.translate(var7_12.getX() * rectangle2D.getWidth(), var7_12.getY() * rectangle2D.getHeight());
            for (DataSource dataSource : this.a.getVisibleData()) {
                Shape shape2;
                Object object;
                int n;
                if (dataSource.getColumnCount() == 0 || 0 >= dataSource.getColumnCount() || !dataSource.isColumnNumeric(0)) continue;
                PointRenderer pointRenderer = this.a.getPointRenderer(dataSource);
                Object object2 = this.a.getMapping(dataSource);
                Object object3 = this.a.getAxis(object2[0]);
                if (!((Axis)object3).isValid()) continue;
                object2 = this.a.getAxisRenderer(object2[0]);
                object3 = Arrays.asList(object3);
                object2 = Arrays.asList(object2);
                for (n = 0; n < dataSource.getRowCount(); ++n) {
                    object = dataSource.getRow(n);
                    object = new PointData((List<Axis>)object3, (List<? extends AxisRenderer>)object2, (Row)object, 0);
                    shape2 = pointRenderer.getPointShape((PointData)object);
                    object = pointRenderer.getPoint((PointData)object, shape2);
                    object.setBounds(rectangle2D);
                    object.draw(drawingContext);
                }
                for (n = 0; n < dataSource.getRowCount(); ++n) {
                    object = dataSource.getRow(n);
                    object = new PointData((List<Axis>)object3, (List<? extends AxisRenderer>)object2, (Row)object, 0);
                    shape2 = pointRenderer.getPointShape((PointData)object);
                    object = pointRenderer.getValue((PointData)object, shape2);
                    object.setBounds(rectangle2D);
                    object.draw(drawingContext);
                }
            }
            graphics2D.setTransform(affineTransform);
            if (insets2D != null) {
                graphics2D.setClip(shape);
            }
        }
    }

    public static class PiePlotNavigator
    extends AbstractNavigator {
        private final PiePlot a;
        private PointND<? extends Number> b;
        private double c;
        private double d;

        public PiePlotNavigator(PiePlot piePlot) {
            this.a = piePlot;
            this.d = 1.0;
            this.setDefaultState();
        }

        @Override
        public double getZoom() {
            return this.d;
        }

        @Override
        public void setZoom(double d) {
            if (!this.isZoomable() || d <= 0.0 || !MathUtils.isCalculatable(d)) {
                return;
            }
            double d2 = this.getZoom();
            if (d2 == (d = MathUtils.limit(d, this.getZoomMin(), this.getZoomMax()))) {
                return;
            }
            this.d = d;
            this.a.setRadius(this.c * this.getZoom());
        }

        @Override
        public PointND<? extends Number> getCenter() {
            Point2D point2D = this.a.getCenter();
            return new PointND(new Number[]{point2D.getX(), point2D.getY()});
        }

        @Override
        public void setCenter(PointND<? extends Number> object) {
            if (object == null || !this.isPannable()) {
                return;
            }
            object = ((PointND)object).getPoint2D();
            this.a.setCenter((Point2D)object);
        }

        @Override
        public void pan(PointND<? extends Number> pointND) {
            PlotArea plotArea = this.a.getPlotArea();
            PointND<? extends Number> pointND2 = this.getCenter();
            double d = pointND2.get(0).doubleValue();
            double d2 = pointND2.get(1).doubleValue();
            pointND2.set(0, (Number)(d += pointND.get(0).doubleValue() / plotArea.getWidth()));
            pointND2.set(1, (Number)(d2 += pointND.get(1).doubleValue() / plotArea.getHeight()));
            this.setCenter(pointND2);
        }

        @Override
        public void reset() {
            this.setCenter(this.b);
            this.setZoom(1.0);
        }

        @Override
        public void setDefaultState() {
            this.b = this.getCenter();
            this.c = this.a.getRadius();
        }
    }
}

