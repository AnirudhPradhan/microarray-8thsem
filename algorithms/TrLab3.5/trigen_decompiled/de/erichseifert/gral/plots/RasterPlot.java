/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots;

import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.data.statistics.Statistics;
import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.Dimension2D;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.colors.ColorMapper;
import de.erichseifert.gral.plots.colors.ContinuousColorMapper;
import de.erichseifert.gral.plots.colors.Grayscale;
import de.erichseifert.gral.plots.points.AbstractPointRenderer;
import de.erichseifert.gral.plots.points.PointData;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.util.GraphicsUtils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

public class RasterPlot
extends XYPlot {
    private static final long serialVersionUID = 5844862286358250831L;
    private final Point2D a = new Point2D.Double();
    private final Dimension2D b = new Dimension2D.Double(1.0, 1.0);
    private ColorMapper c = new Grayscale();

    public RasterPlot(DataSource dataSource) {
        super(new DataSource[0]);
        ((XYPlot.XYPlotArea2D)this.getPlotArea()).setMajorGridX(false);
        ((XYPlot.XYPlotArea2D)this.getPlotArea()).setMajorGridY(false);
        this.getAxisRenderer("x").setIntersection(-1.7976931348623157E308);
        this.getAxisRenderer("y").setIntersection(-1.7976931348623157E308);
        this.add(dataSource);
        this.autoscaleAxes();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void autoscaleAxis(String string) {
        Axis axis;
        if (!"x".equals(string) && !"y".equals(string)) {
            super.autoscaleAxis(string);
            return;
        }
        Dimension2D dimension2D = this.getDistance();
        if (dimension2D == null) {
            dimension2D = new Dimension2D.Double(1.0, 1.0);
        }
        if ((axis = this.getAxis(string)) == null) return;
        if (!axis.isAutoscaled()) {
            return;
        }
        double d = this.getAxisMin(string);
        double d2 = this.getAxisMax(string);
        if ("x".equals(string)) {
            axis.setRange(d, d2 + dimension2D.getWidth());
            return;
        }
        if (!"y".equals(string)) return;
        axis.setRange(d - dimension2D.getHeight(), d2);
    }

    public static DataSource createRasterData(DataSource dataSource) {
        if (dataSource == null) {
            throw new NullPointerException("Cannot convert null data source.");
        }
        DataTable dataTable = new DataTable(Double.class, Double.class, Double.class);
        Statistics statistics = dataSource.getStatistics();
        double d = statistics.get("min");
        double d2 = statistics.get("max");
        double d3 = d2 - d;
        int n = 0;
        for (Object object : dataSource) {
            int n2 = n % dataSource.getColumnCount();
            int n3 = -n / dataSource.getColumnCount();
            double d4 = Double.NaN;
            if (object instanceof Number) {
                object = (Number)object;
                d4 = (((Number)object).doubleValue() - d) / d3;
            }
            dataTable.add(Double.valueOf(n2), Double.valueOf(n3), Double.valueOf(d4));
            ++n;
        }
        return dataTable;
    }

    @Override
    public void add(int n, DataSource dataSource, boolean bl) {
        if (this.getData().size() > 0) {
            throw new IllegalArgumentException("This plot type only supports a single data source.");
        }
        super.add(n, dataSource, bl);
        this.setLineRenderers(dataSource, null);
        this.setPointRenderers(dataSource, new RasterRenderer(this), new PointRenderer[0]);
    }

    public Point2D getOffset() {
        return this.a;
    }

    public void setOffset(Point2D point2D) {
        this.a.setLocation(point2D);
    }

    public Dimension2D getDistance() {
        return this.b;
    }

    public void setDistance(Dimension2D dimension2D) {
        this.b.setSize(dimension2D);
    }

    public ColorMapper getColors() {
        return this.c;
    }

    public void setColors(ColorMapper colorMapper) {
        this.c = colorMapper;
    }

    protected static class RasterRenderer
    extends AbstractPointRenderer {
        private static final long serialVersionUID = 1266585364126459761L;
        private final RasterPlot a;
        private int b;
        private int c;
        private int d;

        public RasterRenderer(RasterPlot rasterPlot) {
            this.a = rasterPlot;
            this.b = 0;
            this.c = 1;
            this.d = 2;
        }

        public int getXColumn() {
            return this.b;
        }

        public void setXColumn(int n) {
            this.b = n;
        }

        public int getYColumn() {
            return this.c;
        }

        public void setYColumn(int n) {
            this.c = n;
        }

        @Override
        public int getValueColumn() {
            return this.d;
        }

        @Override
        public void setValueColumn(int n) {
            this.d = n;
        }

        @Override
        public Drawable getPoint(PointData pointData, Shape shape) {
            return new AbstractDrawable(this, pointData, shape){
                private static final long serialVersionUID = -1136689797647794969L;
                private /* synthetic */ PointData a;
                private /* synthetic */ Shape b;
                private /* synthetic */ RasterRenderer c;
                {
                    this.c = rasterRenderer;
                    this.a = pointData;
                    this.b = shape;
                }

                @Override
                public final void draw(DrawingContext object) {
                    RasterRenderer rasterRenderer = this.c;
                    Object object2 = this.a.axes.get(0);
                    Object object3 = this.a.axes.get(1);
                    AxisRenderer axisRenderer = this.a.axisRenderers.get(0);
                    AxisRenderer axisRenderer2 = this.a.axisRenderers.get(1);
                    Row row = this.a.row;
                    int n = rasterRenderer.getXColumn();
                    if (n < 0 || n >= row.size() || !row.isColumnNumeric(n)) {
                        return;
                    }
                    int n2 = rasterRenderer.getYColumn();
                    if (n2 < 0 || n2 >= row.size() || !row.isColumnNumeric(n2)) {
                        return;
                    }
                    int n3 = rasterRenderer.getValueColumn();
                    if (n3 < 0 || n3 >= row.size() || !row.isColumnNumeric(n3)) {
                        return;
                    }
                    double d = ((Number)((Object)row.get(n))).doubleValue();
                    double d2 = ((Number)((Object)row.get(n2))).doubleValue();
                    Object object4 = (Number)((Object)row.get(n3));
                    double d3 = axisRenderer.getPosition((Axis)object2, d - 0.5, true, false).get(0);
                    double d4 = axisRenderer.getPosition((Axis)object2, d + 0.5, true, false).get(0);
                    double d5 = Math.abs(d4 - d3) + 1.0;
                    double d6 = axisRenderer2.getPosition((Axis)object3, d2 - 0.5, true, false).get(1);
                    double d7 = axisRenderer2.getPosition((Axis)object3, d2 + 0.5, true, false).get(1);
                    double d8 = Math.abs(d7 - d6) + 1.0;
                    object2 = this.b.getBounds2D();
                    object3 = new AffineTransform();
                    ((AffineTransform)object3).scale(d5 / ((RectangularShape)object2).getWidth(), d8 / ((RectangularShape)object2).getHeight());
                    ((AffineTransform)object3).translate(-((RectangularShape)object2).getMinX(), -((RectangularShape)object2).getMinY());
                    object2 = ((AffineTransform)object3).createTransformedShape(this.b);
                    object = ((DrawingContext)object).getGraphics();
                    object3 = this.c.a.getColors();
                    if (object3 instanceof ContinuousColorMapper) {
                        object4 = ((ContinuousColorMapper)object3).get(((Number)object4).doubleValue());
                    } else if (object3 != null) {
                        object4 = ((Number)object4).intValue();
                        object4 = object3.get((Number)object4);
                    } else {
                        object4 = Color.BLACK;
                    }
                    GraphicsUtils.fillPaintedShape((Graphics2D)object, (Shape)object2, (Paint)object4, object2.getBounds2D());
                }
            };
        }

        @Override
        public Shape getPointShape(PointData pointData) {
            return this.getShape();
        }

        @Override
        public Drawable getValue(PointData object, Shape shape) {
            object = new AbstractDrawable(this){
                private static final long serialVersionUID = -8402945980942955359L;

                @Override
                public final void draw(DrawingContext drawingContext) {
                }
            };
            return object;
        }
    }
}

