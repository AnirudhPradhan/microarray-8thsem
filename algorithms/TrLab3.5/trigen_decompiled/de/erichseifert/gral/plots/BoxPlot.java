/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots;

import de.erichseifert.gral.data.AbstractDataSource;
import de.erichseifert.gral.data.Column;
import de.erichseifert.gral.data.DataAccessor;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.colors.ColorMapper;
import de.erichseifert.gral.plots.colors.ContinuousColorMapper;
import de.erichseifert.gral.plots.colors.SingleColor;
import de.erichseifert.gral.plots.legends.AbstractLegend;
import de.erichseifert.gral.plots.legends.ValueLegend;
import de.erichseifert.gral.plots.points.AbstractPointRenderer;
import de.erichseifert.gral.plots.points.PointData;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.SerializationUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class BoxPlot
extends XYPlot {
    private static final long serialVersionUID = -3069831535208696337L;

    public BoxPlot(DataSource dataSource) {
        super(new DataSource[0]);
        this.setLegend(new BoxPlotLegend(this));
        ((XYPlot.XYPlotArea2D)this.getPlotArea()).setMajorGridX(false);
        this.getAxisRenderer("x").setTickSpacing(1.0);
        this.getAxisRenderer("x").setMinorTicksVisible(false);
        this.getAxisRenderer("x").setIntersection(-1.7976931348623157E308);
        this.getAxisRenderer("y").setIntersection(-1.7976931348623157E308);
        this.add(dataSource);
        this.autoscaleAxes();
    }

    public static DataSource createBoxData(DataSource dataSource) {
        if (dataSource == null) {
            throw new NullPointerException("Cannot extract statistics from null data source.");
        }
        DataTable dataTable = new DataTable(Integer.class, Double.class, Double.class, Double.class, Double.class, Double.class);
        for (int i = 0; i < dataSource.getColumnCount(); ++i) {
            Column column = dataSource.getColumn(i);
            if (!column.isNumeric()) continue;
            dataTable.add(Integer.valueOf(i + 1), Double.valueOf(column.getStatistics("quantile50")), Double.valueOf(column.getStatistics("min")), Double.valueOf(column.getStatistics("quantile25")), Double.valueOf(column.getStatistics("quantile75")), Double.valueOf(column.getStatistics("max")));
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
        this.setPointRenderers(dataSource, new BoxWhiskerRenderer(), new PointRenderer[0]);
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
        boolean bl = "x".equals(string);
        double d = Double.MAX_VALUE;
        double d2 = Double.MIN_VALUE;
        for (DataSource dataSource : list) {
            int n;
            int n2;
            list = null;
            for (PointRenderer pointRenderer : this.getPointRenderers(dataSource)) {
                if (!(pointRenderer instanceof BoxWhiskerRenderer)) continue;
                list = (BoxWhiskerRenderer)pointRenderer;
                break;
            }
            if (list == null) continue;
            if (bl) {
                n2 = ((BoxWhiskerRenderer)((Object)list)).getPositionColumn();
                n = ((BoxWhiskerRenderer)((Object)list)).getPositionColumn();
            } else {
                n2 = ((BoxWhiskerRenderer)((Object)list)).getBottomBarColumn();
                n = ((BoxWhiskerRenderer)((Object)list)).getTopBarColumn();
            }
            d = Math.min(d, dataSource.getColumn(n2).getStatistics("min"));
            d2 = Math.max(d2, dataSource.getColumn(n).getStatistics("max"));
        }
        double d3 = bl ? 0.5 : 0.05 * (d2 - d);
        axis.setRange(d - d3, d2 + d3);
    }

    public static class BoxPlotLegend
    extends ValueLegend {
        private static final long serialVersionUID = 1517792984459627757L;
        private static final DataSource a = new AbstractDataSource(new Class[]{Double.class, Double.class, Double.class, Double.class, Double.class, Double.class}){
            private static final long serialVersionUID = -8233716728143117368L;
            private final Double[] a = new Double[]{0.5, 0.0, 0.0, 1.0, 1.0};

            @Override
            public final int getRowCount() {
                return 1;
            }

            @Override
            public final Comparable<?> get(int n, int n2) {
                if (n == 0) {
                    return (double)(n2 + 1);
                }
                return this.a[n - 1];
            }
        };
        private final BoxPlot b;

        public BoxPlotLegend(BoxPlot boxPlot) {
            this.b = boxPlot;
        }

        @Override
        public Drawable getSymbol(Row row) {
            return new AbstractLegend.AbstractSymbol(this, this, row){
                private static final long serialVersionUID = 1906894939358065143L;
                private /* synthetic */ Row a;
                private /* synthetic */ BoxPlotLegend b;
                {
                    this.b = boxPlotLegend;
                    this.a = row;
                    super(legend);
                }

                @Override
                public final void draw(DrawingContext object) {
                    object = this.a.getSource();
                    object = this.b.b.getPointRenderers((DataSource)object);
                    object = object.iterator();
                    while (object.hasNext()) {
                        object.next();
                    }
                }
            };
        }
    }

    public static class BoxWhiskerRenderer
    extends AbstractPointRenderer {
        private static final long serialVersionUID = 2944482729753981341L;
        private int a = 0;
        private int b = 1;
        private int c = 2;
        private int d = 3;
        private int e = 4;
        private int f = 5;
        private double g = 0.75;
        private ColorMapper h = new SingleColor(Color.WHITE);
        private Paint i = Color.BLACK;
        private transient Stroke j = new BasicStroke(1.0f);
        private Paint k = Color.BLACK;
        private transient Stroke l = new BasicStroke(1.0f);
        private double m = 0.75;
        private Paint n = Color.BLACK;
        private transient Stroke o = new BasicStroke(2.0f, 0, 0);

        private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
            objectInputStream.defaultReadObject();
            this.j = (Stroke)SerializationUtils.unwrap((Serializable)objectInputStream.readObject());
            this.l = (Stroke)SerializationUtils.unwrap((Serializable)objectInputStream.readObject());
            this.o = (Stroke)SerializationUtils.unwrap((Serializable)objectInputStream.readObject());
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws ClassNotFoundException, IOException {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeObject(SerializationUtils.wrap(this.j));
            objectOutputStream.writeObject(SerializationUtils.wrap(this.l));
            objectOutputStream.writeObject(SerializationUtils.wrap(this.o));
        }

        public int getPositionColumn() {
            return this.a;
        }

        public void setPositionColumn(int n) {
            this.a = n;
        }

        public int getCenterBarColumn() {
            return this.b;
        }

        public void setCenterBarColumn(int n) {
            this.b = n;
        }

        public int getBottomBarColumn() {
            return this.c;
        }

        public void setBottomBarColumn(int n) {
            this.c = n;
        }

        public int getBoxBottomColumn() {
            return this.d;
        }

        public void setColumnBoxBottom(int n) {
            this.d = n;
        }

        public int getBoxTopColumn() {
            return this.e;
        }

        public void setBoxTopColumn(int n) {
            this.e = n;
        }

        public int getTopBarColumn() {
            return this.f;
        }

        public void setTopBarColumn(int n) {
            this.f = n;
        }

        public double getBoxWidth() {
            return this.g;
        }

        public void setBoxWidth(double d) {
            this.g = d;
        }

        public ColorMapper getBoxBackground() {
            return this.h;
        }

        public void setBoxBackground(ColorMapper colorMapper) {
            this.h = colorMapper;
        }

        public void setBoxBackground(Paint paint) {
            this.setBoxBackground(new SingleColor(paint));
        }

        public Paint getBoxBorderColor() {
            return this.i;
        }

        public void setBoxBorderColor(Paint paint) {
            this.i = paint;
        }

        public Stroke getBoxBorderStroke() {
            return this.j;
        }

        public void setBoxBorderStroke(Stroke stroke) {
            this.j = stroke;
        }

        public Paint getWhiskerColor() {
            return this.k;
        }

        public void setWhiskerColor(Paint paint) {
            this.k = paint;
        }

        public Stroke getWhiskerStroke() {
            return this.l;
        }

        public void setWhiskerStroke(Stroke stroke) {
            this.l = stroke;
        }

        public double getBarWidth() {
            return this.m;
        }

        public void setBarWidth(double d) {
            this.m = d;
        }

        public Paint getCenterBarColor() {
            return this.n;
        }

        public void setCenterBarColor(Paint paint) {
            this.n = paint;
        }

        public Stroke getCenterBarStroke() {
            return this.o;
        }

        public void setCenterBarStroke(Stroke stroke) {
            this.o = stroke;
        }

        @Override
        public Drawable getPoint(PointData pointData, Shape shape) {
            return new AbstractDrawable(this, pointData, shape){
                private static final long serialVersionUID = 2765031432328349977L;
                private /* synthetic */ PointData a;
                private /* synthetic */ Shape b;
                private /* synthetic */ BoxWhiskerRenderer c;
                {
                    this.c = boxWhiskerRenderer;
                    this.a = pointData;
                    this.b = shape;
                }

                @Override
                public final void draw(DrawingContext object) {
                    Object object2 = this.a.axes.get(0);
                    Object object3 = this.a.axes.get(1);
                    Object object4 = this.a.axisRenderers.get(0);
                    Object object5 = this.a.axisRenderers.get(1);
                    Object object6 = this.a.row;
                    BoxWhiskerRenderer boxWhiskerRenderer = this.c;
                    int n = boxWhiskerRenderer.getPositionColumn();
                    int n2 = boxWhiskerRenderer.getCenterBarColumn();
                    int n3 = boxWhiskerRenderer.getBottomBarColumn();
                    int n4 = boxWhiskerRenderer.getBoxBottomColumn();
                    int n5 = boxWhiskerRenderer.getBoxTopColumn();
                    int n6 = boxWhiskerRenderer.getTopBarColumn();
                    if (!(((Row)object6).isColumnNumeric(n) && ((Row)object6).isColumnNumeric(n2) && ((Row)object6).isColumnNumeric(n3) && ((Row)object6).isColumnNumeric(n4) && ((Row)object6).isColumnNumeric(n5) && ((Row)object6).isColumnNumeric(n6))) {
                        return;
                    }
                    double d = ((Number)((Object)((Row)object6).get(n))).doubleValue();
                    double d2 = ((Number)((Object)((Row)object6).get(n3))).doubleValue();
                    double d3 = ((Number)((Object)((Row)object6).get(n4))).doubleValue();
                    double d4 = ((Number)((Object)((Row)object6).get(n2))).doubleValue();
                    double d5 = ((Number)((Object)((Row)object6).get(n5))).doubleValue();
                    double d6 = ((Number)((Object)((Row)object6).get(n6))).doubleValue();
                    double d7 = this.c.getBoxWidth();
                    double d8 = object4.getPosition((Axis)object2, d - d7 * 0.5, true, false).get(0);
                    double d9 = object4.getPosition((Axis)object2, d, true, false).get(0);
                    double d10 = object4.getPosition((Axis)object2, d + d7 * 0.5, true, false).get(0);
                    double d11 = object5.getPosition((Axis)object3, d2, true, false).get(1);
                    double d12 = object5.getPosition((Axis)object3, d3, true, false).get(1);
                    double d13 = object5.getPosition((Axis)object3, d4, true, false).get(1);
                    double d14 = object5.getPosition((Axis)object3, d5, true, false).get(1);
                    double d15 = object5.getPosition((Axis)object3, d6, true, false).get(1);
                    double d16 = Math.abs(d10 - d8);
                    double d17 = this.c.getBarWidth();
                    double d18 = d8 + (1.0 - d17) * d16 / 2.0;
                    double d19 = d10 - (1.0 - d17) * d16 / 2.0;
                    object2 = new Rectangle2D.Double(d8 - d9, d14 - d13, d16, Math.abs(d14 - d12));
                    object3 = this.b.getBounds2D();
                    object4 = new AffineTransform();
                    ((AffineTransform)object4).translate(((RectangularShape)object2).getX(), ((RectangularShape)object2).getY());
                    ((AffineTransform)object4).scale(((RectangularShape)object2).getWidth() / ((RectangularShape)object3).getWidth(), ((RectangularShape)object2).getHeight() / ((RectangularShape)object3).getHeight());
                    ((AffineTransform)object4).translate(-((RectangularShape)object3).getMinX(), -((RectangularShape)object3).getMinY());
                    object2 = ((AffineTransform)object4).createTransformedShape(this.b);
                    object3 = new Line2D.Double(0.0, d14 - d13, 0.0, d15 - d13);
                    object4 = new Line2D.Double(0.0, d12 - d13, 0.0, d11 - d13);
                    object5 = new Line2D.Double(d18 - d9, d15 - d13, d19 - d9, d15 - d13);
                    Line2D.Double double_ = new Line2D.Double(d18 - d9, d11 - d13, d19 - d9, d11 - d13);
                    Line2D.Double double_2 = new Line2D.Double(d8 - d9, 0.0, d10 - d9, 0.0);
                    object = ((DrawingContext)object).getGraphics();
                    ColorMapper colorMapper = this.c.getBoxBackground();
                    if (colorMapper instanceof ContinuousColorMapper) {
                        object6 = ((ContinuousColorMapper)colorMapper).get(d);
                    } else {
                        int n7 = ((DataAccessor)object6).getIndex();
                        object6 = colorMapper.get(n7);
                    }
                    Paint paint = this.c.getBoxBorderColor();
                    Stroke stroke = this.c.getBoxBorderStroke();
                    Paint paint2 = this.c.getWhiskerColor();
                    Stroke stroke2 = this.c.getWhiskerStroke();
                    Paint paint3 = this.c.getCenterBarColor();
                    Stroke stroke3 = this.c.getCenterBarStroke();
                    GraphicsUtils.fillPaintedShape((Graphics2D)object, (Shape)object2, (Paint)object6, object2.getBounds2D());
                    Paint paint4 = ((Graphics2D)object).getPaint();
                    Stroke stroke4 = ((Graphics2D)object).getStroke();
                    ((Graphics2D)object).setPaint(paint2);
                    ((Graphics2D)object).setStroke(stroke2);
                    ((Graphics2D)object).draw((Shape)object3);
                    ((Graphics2D)object).draw((Shape)object4);
                    ((Graphics2D)object).setPaint(paint);
                    ((Graphics2D)object).setStroke(stroke);
                    ((Graphics2D)object).draw((Shape)object2);
                    ((Graphics2D)object).draw((Shape)object5);
                    ((Graphics2D)object).draw(double_);
                    ((Graphics2D)object).setPaint(paint3);
                    ((Graphics2D)object).setStroke(stroke3);
                    ((Graphics2D)object).draw(double_2);
                    ((Graphics2D)object).setStroke(stroke4);
                    ((Graphics2D)object).setPaint(paint4);
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
                private static final long serialVersionUID = 6788431763837737592L;

                @Override
                public final void draw(DrawingContext drawingContext) {
                }
            };
            return object;
        }
    }
}

