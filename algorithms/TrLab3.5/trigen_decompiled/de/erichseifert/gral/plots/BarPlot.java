/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots;

import de.erichseifert.gral.data.DataAccessor;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DummyData;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.graphics.Location;
import de.erichseifert.gral.plots.DataPoint;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.areas.AreaRenderer;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.axes.LinearRenderer2D;
import de.erichseifert.gral.plots.legends.AbstractLegend;
import de.erichseifert.gral.plots.legends.ValueLegend;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointData;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.MathUtils;
import de.erichseifert.gral.util.PointND;
import de.erichseifert.gral.util.SerializationUtils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class BarPlot
extends XYPlot {
    private static final long serialVersionUID = 3177733647455649147L;
    private double a;
    private double b;
    private boolean c;

    public BarPlot(DataSource ... dataSourceArray) {
        super(dataSourceArray);
        ((XYPlot.XYPlotArea2D)this.getPlotArea()).setMajorGridX(false);
        this.a = 1.0;
        this.b = 0.0;
        this.c = false;
        this.setLegend(new BarPlotLegend(this));
        this.autoscaleAxes();
    }

    @Override
    public void autoscaleAxis(String string) {
        Axis axis;
        if (!"x".equals(string) && !"y".equals(string)) {
            super.autoscaleAxis(string);
        }
        if ((axis = this.getAxis(string)) == null || !axis.isAutoscaled()) {
            return;
        }
        List<DataSource> list = this.getData();
        if (list.isEmpty()) {
            return;
        }
        int n = 0;
        for (DataSource dataSource : list) {
            n = Math.max(n, dataSource.getRowCount());
        }
        if (n == 0) {
            return;
        }
        double d = this.getAxisMin(string);
        double d2 = this.getAxisMax(string);
        double d3 = 0.0;
        if ("x".equals(string)) {
            double d4 = this.getBarWidth();
            double d5 = d4 * (d2 - d) / (double)n;
            d3 = d5 / 2.0;
        } else {
            d = Math.min(d, 0.0);
            d2 = Math.max(d2, 0.0);
        }
        axis.setRange(d - d3, d2 + d3);
    }

    @Override
    public void add(int n, DataSource dataSource, boolean bl) {
        super.add(n, dataSource, bl);
        BarRenderer barRenderer = new BarRenderer(this);
        this.setPointRenderers(dataSource, barRenderer, new PointRenderer[0]);
        this.setLineRenderers(dataSource, null, new LineRenderer[0]);
        this.setAreaRenderers(dataSource, null, new AreaRenderer[0]);
    }

    public double getBarWidth() {
        return this.a;
    }

    public void setBarWidth(double d) {
        this.a = d;
    }

    public double getBarHeightMin() {
        return this.b;
    }

    public void setBarHeightMin(double d) {
        this.b = d;
    }

    public boolean isPaintAllBars() {
        return this.c;
    }

    public void setPaintAllBars(boolean bl) {
        this.c = bl;
    }

    public static class BarPlotLegend
    extends ValueLegend {
        private static final long serialVersionUID = 4752278896167602641L;
        private static final DataSource a = new DummyData(2, 1, Double.valueOf(0.5));
        private final BarPlot b;

        public BarPlotLegend(BarPlot barPlot) {
            this.b = barPlot;
        }

        @Override
        public Drawable getSymbol(Row row) {
            return new AbstractLegend.AbstractSymbol(this, this, row){
                private static final long serialVersionUID = 5744026898590787285L;
                private /* synthetic */ Row a;
                private /* synthetic */ BarPlotLegend b;
                {
                    this.b = barPlotLegend;
                    this.a = row;
                    super(legend);
                }

                @Override
                public final void draw(DrawingContext drawingContext) {
                    Object object = this.a.getSource();
                    Object object2 = new Row(a, this.a.getIndex());
                    Cloneable cloneable = this.getBounds();
                    double d = this.b.b.getBarWidth();
                    Object object3 = new Axis(0.5 - d / 2.0, 0.5 + d / 2.0);
                    Object object4 = new LinearRenderer2D();
                    object4.setShape(new Line2D.Double(((RectangularShape)cloneable).getMinX(), ((RectangularShape)cloneable).getMaxY(), ((RectangularShape)cloneable).getMaxX(), ((RectangularShape)cloneable).getMaxY()));
                    Axis axis = new Axis(0.0, 0.5);
                    LinearRenderer2D linearRenderer2D = new LinearRenderer2D();
                    linearRenderer2D.setShape(new Line2D.Double(((RectangularShape)cloneable).getMinX(), ((RectangularShape)cloneable).getMaxY(), ((RectangularShape)cloneable).getMinX(), ((RectangularShape)cloneable).getMinY()));
                    object2 = new PointData(Arrays.asList(object3, axis), Arrays.asList(object4, linearRenderer2D), (Row)object2, 0);
                    object3 = null;
                    object = this.b.b.getPointRenderers((DataSource)object);
                    if (!object.isEmpty()) {
                        object3 = (PointRenderer)object.get(0);
                    }
                    object = null;
                    if (object3 != null) {
                        object4 = object3.getPointShape((PointData)object2);
                        object = object3.getPoint((PointData)object2, (Shape)object4);
                    }
                    object4 = new DataPoint((PointData)object2, new PointND((Number[])new Double[]{((RectangularShape)cloneable).getCenterX(), ((RectangularShape)cloneable).getMinY()}));
                    if (object != null) {
                        object2 = drawingContext.getGraphics();
                        cloneable = ((DataPoint)object4).position.getPoint2D();
                        object3 = ((Graphics2D)object2).getTransform();
                        ((Graphics2D)object2).translate(((Point2D)cloneable).getX(), ((Point2D)cloneable).getY());
                        object.draw(drawingContext);
                        ((Graphics2D)object2).setTransform((AffineTransform)object3);
                    }
                }
            };
        }
    }

    public static class BarRenderer
    extends DefaultPointRenderer2D {
        private static final long serialVersionUID = 2183638342305398522L;
        private final BarPlot a;
        private transient Stroke b;
        private Paint c;

        public BarRenderer(BarPlot barPlot) {
            this.a = barPlot;
            this.setValueLocation(Location.NORTH);
            this.b = null;
            this.c = Color.BLACK;
        }

        private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
            objectInputStream.defaultReadObject();
            this.b = (Stroke)SerializationUtils.unwrap((Serializable)objectInputStream.readObject());
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws ClassNotFoundException, IOException {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeObject(SerializationUtils.wrap(this.b));
        }

        public Stroke getBorderStroke() {
            return this.b;
        }

        public void setBorderStroke(Stroke stroke) {
            this.b = stroke;
        }

        public Paint getBorderColor() {
            return this.c;
        }

        public void setBorderColor(Paint paint) {
            this.c = paint;
        }

        @Override
        public Drawable getPoint(PointData pointData, Shape shape) {
            return new AbstractDrawable(this, pointData, shape){
                private static final long serialVersionUID = -3145112034673683520L;
                private /* synthetic */ PointData a;
                private /* synthetic */ Shape b;
                private /* synthetic */ BarRenderer c;
                {
                    this.c = barRenderer;
                    this.a = pointData;
                    this.b = shape;
                }

                @Override
                public final void draw(DrawingContext object) {
                    Object object2;
                    BarRenderer barRenderer = this.c;
                    Object object3 = this.a.row;
                    Rectangle2D.Double double_ = null;
                    object = ((DrawingContext)object).getGraphics();
                    Object object4 = barRenderer.getColor();
                    object3 = object4.get(((DataAccessor)object3).getIndex());
                    if (this.c.a.isPaintAllBars()) {
                        object4 = ((Graphics2D)object).getTransform();
                        object2 = this.b.getBounds2D();
                        double_ = new Rectangle2D.Double();
                        double_ = new Rectangle2D.Double(((RectangularShape)object2).getX(), ((RectangularShape)double_).getY() - ((AffineTransform)object4).getTranslateY(), ((RectangularShape)object2).getWidth(), ((RectangularShape)double_).getHeight());
                    }
                    GraphicsUtils.fillPaintedShape((Graphics2D)object, this.b, (Paint)object3, double_);
                    object4 = barRenderer.getBorderStroke();
                    object2 = barRenderer.getBorderColor();
                    if (object4 != null && object2 != null) {
                        GraphicsUtils.drawPaintedShape((Graphics2D)object, this.b, (Paint)object2, null, (Stroke)object4);
                    }
                }
            };
        }

        @Override
        public Shape getPointShape(PointData object) {
            Axis axis = ((PointData)object).axes.get(0);
            Axis axis2 = ((PointData)object).axes.get(1);
            AxisRenderer axisRenderer = ((PointData)object).axisRenderers.get(0);
            AxisRenderer axisRenderer2 = ((PointData)object).axisRenderers.get(1);
            object = ((PointData)object).row;
            if (!((Row)object).isColumnNumeric(0) || !((Row)object).isColumnNumeric(1)) {
                return null;
            }
            double d = ((Number)((Object)((Row)object).get(0))).doubleValue();
            double d2 = ((Number)((Object)((Row)object).get(1))).doubleValue();
            double d3 = this.a.getBarWidth();
            d3 = Math.max(d3, 0.0);
            double d4 = axisRenderer.getPosition(axis, d - d3 * 0.5, true, false).get(0);
            double d5 = axisRenderer.getPosition(axis, d + d3 * 0.5, true, false).get(0);
            double d6 = axisRenderer2.getPosition(axis2, d2, true, false).get(1);
            double d7 = axisRenderer2.getPosition(axis2, 0.0, true, false).get(1);
            double d8 = Math.min(d6, d7);
            double d9 = Math.max(d6, d7);
            double d10 = Math.abs(d5 - d4);
            double d11 = Math.abs(d9 - d8);
            double d12 = axisRenderer.getPosition(axis, d, true, false).get(0);
            boolean bl = d9 == d7;
            double d13 = bl ? 0.0 : -d11;
            double d14 = this.a.getBarHeightMin();
            if (MathUtils.isCalculatable(d14) && d14 > 0.0 && d11 < d14) {
                if (bl) {
                    d13 = 0.0 + (-d14 + d11);
                }
                d11 = d14;
            }
            Shape shape = this.getBarShape(d4 - d12, d13, d10, d11);
            return shape;
        }

        protected Shape getBarShape(double d, double d2, double d3, double d4) {
            Shape shape = this.getShape();
            Rectangle2D rectangle2D = shape.getBounds2D();
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.translate(d, d2);
            affineTransform.scale(d3 / rectangle2D.getWidth(), d4 / rectangle2D.getHeight());
            affineTransform.translate(-rectangle2D.getMinX(), -rectangle2D.getMinY());
            Shape shape2 = affineTransform.createTransformedShape(shape);
            return shape2;
        }

        @Override
        public Drawable getValue(PointData object, Shape shape) {
            object = new AbstractDrawable(this, (PointData)object, shape){
                private static final long serialVersionUID = -1133369168849171793L;
                private /* synthetic */ PointData a;
                private /* synthetic */ Shape b;
                private /* synthetic */ BarRenderer c;
                {
                    this.c = barRenderer;
                    this.a = pointData;
                    this.b = shape;
                }

                @Override
                public final void draw(DrawingContext drawingContext) {
                    BarRenderer barRenderer = this.c;
                    Row row = this.a.row;
                    if (barRenderer.isValueVisible()) {
                        int n = barRenderer.getValueColumn();
                        this.c.drawValueLabel(drawingContext, this.b, row, n);
                    }
                }
            };
            return object;
        }
    }
}

