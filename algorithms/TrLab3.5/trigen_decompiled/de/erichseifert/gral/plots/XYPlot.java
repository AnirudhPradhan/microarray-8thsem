/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots;

import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DummyData;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.Orientation;
import de.erichseifert.gral.navigation.Navigable;
import de.erichseifert.gral.navigation.NavigationDirection;
import de.erichseifert.gral.navigation.Navigator;
import de.erichseifert.gral.plots.AbstractPlot;
import de.erichseifert.gral.plots.DataPoint;
import de.erichseifert.gral.plots.Plot;
import de.erichseifert.gral.plots.PlotArea;
import de.erichseifert.gral.plots.PlotNavigator;
import de.erichseifert.gral.plots.areas.AreaRenderer;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.AxisListener;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.axes.LinearRenderer2D;
import de.erichseifert.gral.plots.axes.Tick;
import de.erichseifert.gral.plots.legends.AbstractLegend;
import de.erichseifert.gral.plots.legends.SeriesLegend;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointData;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.util.GeometryUtils;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.MathUtils;
import de.erichseifert.gral.util.PointND;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class XYPlot
extends AbstractPlot
implements Navigable,
AxisListener {
    private static final long serialVersionUID = 4501074701747572783L;
    public static final String AXIS_X = "x";
    public static final String AXIS_X2 = "x2";
    public static final String AXIS_Y = "y";
    public static final String AXIS_Y2 = "y2";
    private final Map<DataSource, List<PointRenderer>> a;
    private final Map<DataSource, List<LineRenderer>> b;
    private final Map<DataSource, List<AreaRenderer>> c;
    private transient XYPlotNavigator d;
    private transient boolean e;

    public XYPlot(DataSource ... object) {
        super(new DataSource[0]);
        this.a = new HashMap<DataSource, List<PointRenderer>>(((DataSource[])object).length);
        this.b = new HashMap<DataSource, List<LineRenderer>>(((DataSource[])object).length);
        this.c = new HashMap<DataSource, List<AreaRenderer>>(((DataSource[])object).length);
        this.setPlotArea(new XYPlotArea2D(this));
        this.setLegend(new XYLegend(this));
        for (DataSource dataSource : object) {
            this.add(dataSource);
        }
        this.createDefaultAxes();
        this.autoscaleAxes();
        this.createDefaultAxisRenderers();
        for (String string : this.getAxesNames()) {
            this.getAxis(string).addAxisListener(this);
        }
    }

    @Override
    protected void createDefaultAxes() {
        Axis axis = new Axis();
        Axis axis2 = new Axis();
        this.setAxis(AXIS_X, axis);
        this.setAxis(AXIS_Y, axis2);
    }

    @Override
    protected void createDefaultAxisRenderers() {
        LinearRenderer2D linearRenderer2D = new LinearRenderer2D();
        LinearRenderer2D linearRenderer2D2 = new LinearRenderer2D();
        this.setAxisRenderer(AXIS_X, linearRenderer2D);
        this.setAxisRenderer(AXIS_Y, linearRenderer2D2);
    }

    @Override
    protected void layoutAxes() {
        if (this.getPlotArea() == null) {
            return;
        }
        this.a(AXIS_X, Orientation.HORIZONTAL);
        this.a(AXIS_X2, Orientation.HORIZONTAL);
        this.a(AXIS_Y, Orientation.VERTICAL);
        this.a(AXIS_Y2, Orientation.VERTICAL);
        this.b(AXIS_X, Orientation.HORIZONTAL);
        this.b(AXIS_X2, Orientation.HORIZONTAL);
        this.b(AXIS_Y, Orientation.VERTICAL);
        this.b(AXIS_Y2, Orientation.VERTICAL);
    }

    private void a(String object, Orientation object2) {
        Rectangle2D rectangle2D = this.getPlotArea().getBounds();
        Object object3 = this.getAxisComponent((String)object);
        object = this.getAxisRenderer((String)object);
        if (object3 == null || object == null) {
            return;
        }
        object3 = object3.getPreferredSize();
        object2 = object2 == Orientation.HORIZONTAL ? new Line2D.Double(0.0, 0.0, rectangle2D.getWidth(), 0.0) : new Line2D.Double(((Dimension2D)object3).getWidth(), rectangle2D.getHeight(), ((Dimension2D)object3).getWidth(), 0.0);
        object.setShape((Shape)object2);
    }

    private void b(String pointND, Orientation orientation) {
        Drawable drawable = this.getAxisComponent((String)((Object)pointND));
        pointND = this.getAxisRenderer((String)((Object)pointND));
        if (drawable == null || pointND == null) {
            return;
        }
        Object object = orientation == Orientation.HORIZONTAL ? AXIS_Y : AXIS_X;
        Object object2 = this.getAxis((String)object);
        object = this.getAxisRenderer((String)object);
        if (object2 == null || !((Axis)object2).isValid() || object == null) {
            return;
        }
        pointND = pointND.getIntersection();
        if ((pointND = object.getPosition((Axis)object2, (Number)((Object)pointND), false, false)) == null) {
            pointND = new PointND((Number[])new Double[]{0.0, 0.0});
        }
        object = this.getPlotArea().getBounds();
        object2 = drawable.getPreferredSize();
        if (orientation == Orientation.HORIZONTAL) {
            drawable.setBounds(((RectangularShape)object).getMinX(), (Double)pointND.get(1) + ((RectangularShape)object).getMinY(), ((RectangularShape)object).getWidth(), ((Dimension2D)object2).getHeight());
            return;
        }
        drawable.setBounds(((RectangularShape)object).getMinX() - ((Dimension2D)object2).getWidth() + (Double)pointND.get(0), ((RectangularShape)object).getMinY(), ((Dimension2D)object2).getWidth(), ((RectangularShape)object).getHeight());
    }

    public List<PointRenderer> getPointRenderers(DataSource iterable) {
        if ((iterable = this.a.get(iterable)) != null) {
            return Collections.unmodifiableList(iterable);
        }
        return Collections.emptyList();
    }

    public void addPointRenderer(DataSource dataSource, PointRenderer pointRenderer) {
        List<PointRenderer> list = this.a.get(dataSource);
        if (list == null) {
            list = new ArrayList<PointRenderer>();
            this.a.put(dataSource, list);
        }
        list.add(pointRenderer);
    }

    public void removePointRenderer(DataSource iterable, PointRenderer pointRenderer) {
        if ((iterable = this.a.get(iterable)) != null) {
            iterable.remove(pointRenderer);
        }
    }

    public void setPointRenderers(DataSource dataSource, List<PointRenderer> list) {
        this.a.put(dataSource, list);
    }

    public void setPointRenderers(DataSource dataSource, PointRenderer pointRendererArray, PointRenderer ... pointRendererArray2) {
        if (pointRendererArray == null) {
            this.setPointRenderers(dataSource, null);
            return;
        }
        ArrayList<PointRenderer> arrayList = new ArrayList<PointRenderer>(pointRendererArray2.length + 1);
        arrayList.add((PointRenderer)pointRendererArray);
        pointRendererArray = pointRendererArray2;
        int n = pointRendererArray2.length;
        for (int i = 0; i < n; ++i) {
            PointRenderer pointRenderer = pointRendererArray[i];
            if (pointRenderer == null) {
                throw new IllegalArgumentException("A PointRenderer for a DataSource cannot be null.");
            }
            arrayList.add(pointRenderer);
        }
        this.setPointRenderers(dataSource, arrayList);
    }

    public List<LineRenderer> getLineRenderers(DataSource iterable) {
        if ((iterable = this.b.get(iterable)) != null) {
            return Collections.unmodifiableList(iterable);
        }
        return Collections.emptyList();
    }

    public void setLineRenderers(DataSource dataSource, List<LineRenderer> list) {
        this.b.put(dataSource, list);
    }

    public void setLineRenderers(DataSource dataSource, LineRenderer lineRendererArray, LineRenderer ... lineRendererArray2) {
        if (lineRendererArray == null) {
            this.setLineRenderers(dataSource, null);
            return;
        }
        ArrayList<LineRenderer> arrayList = new ArrayList<LineRenderer>(lineRendererArray2.length + 1);
        arrayList.add((LineRenderer)lineRendererArray);
        lineRendererArray = lineRendererArray2;
        int n = lineRendererArray2.length;
        for (int i = 0; i < n; ++i) {
            LineRenderer lineRenderer = lineRendererArray[i];
            if (lineRenderer == null) {
                throw new IllegalArgumentException("A LineRenderer for a DataSource cannot be null.");
            }
            arrayList.add(lineRenderer);
        }
        this.setLineRenderers(dataSource, arrayList);
    }

    public List<AreaRenderer> getAreaRenderers(DataSource iterable) {
        if ((iterable = this.c.get(iterable)) != null) {
            return Collections.unmodifiableList(iterable);
        }
        return Collections.emptyList();
    }

    public void setAreaRenderers(DataSource dataSource, List<AreaRenderer> list) {
        this.c.put(dataSource, list);
    }

    public void setAreaRenderers(DataSource dataSource, AreaRenderer areaRendererArray, AreaRenderer ... areaRendererArray2) {
        if (areaRendererArray == null) {
            this.setAreaRenderers(dataSource, null);
            return;
        }
        ArrayList<AreaRenderer> arrayList = new ArrayList<AreaRenderer>(areaRendererArray2.length + 1);
        arrayList.add((AreaRenderer)areaRendererArray);
        areaRendererArray = areaRendererArray2;
        int n = areaRendererArray2.length;
        for (int i = 0; i < n; ++i) {
            AreaRenderer areaRenderer = areaRendererArray[i];
            if (areaRenderer == null) {
                throw new IllegalArgumentException("An AreaRenderer for a DataSource cannot be null.");
            }
            arrayList.add(areaRenderer);
        }
        this.setAreaRenderers(dataSource, arrayList);
    }

    @Override
    public void setAxisRenderer(String string, AxisRenderer axisRenderer) {
        if (axisRenderer != null) {
            if (AXIS_X2.equals(string) || AXIS_Y.equals(string)) {
                axisRenderer.setShapeNormalOrientationClockwise(true);
            }
            if (AXIS_Y.equals(string)) {
                axisRenderer.getLabel().setRotation(90.0);
            }
        }
        super.setAxisRenderer(string, axisRenderer);
    }

    @Override
    public void add(int n, DataSource dataSource, boolean bl) {
        super.add(n, dataSource, bl);
        this.setMapping(dataSource, AXIS_X, AXIS_Y);
        this.autoscaleAxes();
        DefaultPointRenderer2D defaultPointRenderer2D = new DefaultPointRenderer2D();
        this.setPointRenderers(dataSource, defaultPointRenderer2D, new PointRenderer[0]);
        this.setLineRenderers(dataSource, null, new LineRenderer[0]);
        this.setAreaRenderers(dataSource, null, new AreaRenderer[0]);
    }

    @Override
    public Navigator getNavigator() {
        if (this.d == null) {
            this.d = new XYPlotNavigator(this);
        }
        return this.d;
    }

    @Override
    public void draw(DrawingContext drawingContext) {
        super.draw(drawingContext);
        if (!this.e) {
            this.getNavigator().setDefaultState();
            this.e = true;
        }
    }

    @Override
    public void rangeChanged(Axis axis, Number number, Number number2) {
        this.layoutAxes();
    }

    private void readObject(ObjectInputStream object) throws ClassNotFoundException, IOException {
        ((ObjectInputStream)object).defaultReadObject();
        for (String string : this.getAxesNames()) {
            this.getAxis(string).addAxisListener(this);
        }
    }

    public static class XYLegend
    extends SeriesLegend {
        private static final long serialVersionUID = -4629928754001372002L;
        private static final DataSource a = new DummyData(2, Integer.MAX_VALUE, Double.valueOf(0.5));
        private final XYPlot b;

        public XYLegend(XYPlot xYPlot) {
            this.b = xYPlot;
        }

        @Override
        public Drawable getSymbol(Row row) {
            return new AbstractLegend.AbstractSymbol(this, this, row){
                private static final long serialVersionUID = 5744026898590787285L;
                private /* synthetic */ Row a;
                private /* synthetic */ XYLegend b;
                {
                    this.b = xYLegend;
                    this.a = row;
                    super(legend);
                }

                @Override
                public final void draw(DrawingContext drawingContext) {
                    Object object = this.a.getSource();
                    Object object2 = new Row(a, this.a.getIndex());
                    List<PointRenderer> list = this.getBounds();
                    List<LineRenderer> list2 = new Axis(0.0, 1.0);
                    Object object3 = new LinearRenderer2D();
                    object3.setShape(new Line2D.Double(((RectangularShape)((Object)list)).getMinX(), ((RectangularShape)((Object)list)).getCenterY(), ((RectangularShape)((Object)list)).getMaxX(), ((RectangularShape)((Object)list)).getCenterY()));
                    Object object4 = new Axis(0.0, 1.0);
                    LinearRenderer2D linearRenderer2D = new LinearRenderer2D();
                    linearRenderer2D.setShape(new Line2D.Double(((RectangularShape)((Object)list)).getCenterX(), ((RectangularShape)((Object)list)).getMaxY(), ((RectangularShape)((Object)list)).getCenterX(), ((RectangularShape)((Object)list)).getMinY()));
                    object2 = new PointData(Arrays.asList(list2, object4), Arrays.asList(object3, linearRenderer2D), (Row)object2, 0);
                    list2 = new DataPoint((PointData)object2, new PointND((Number[])new Double[]{((RectangularShape)((Object)list)).getMinX(), ((RectangularShape)((Object)list)).getCenterY()}));
                    object3 = new DataPoint((PointData)object2, new PointND((Number[])new Double[]{((RectangularShape)((Object)list)).getCenterX(), ((RectangularShape)((Object)list)).getCenterY()}));
                    list = new DataPoint((PointData)object2, new PointND((Number[])new Double[]{((RectangularShape)((Object)list)).getMaxX(), ((RectangularShape)((Object)list)).getCenterY()}));
                    list = Arrays.asList(list2, object3, list);
                    list2 = null;
                    object4 = this.b.b.getAreaRenderers((DataSource)object);
                    if (!object4.isEmpty()) {
                        list2 = (AreaRenderer)object4.get(0);
                    }
                    if (list2 != null) {
                        object4 = list2.getAreaShape(list);
                        list2 = list2.getArea(list, (Shape)object4);
                        list2.draw(drawingContext);
                    }
                    object4 = null;
                    list2 = this.b.b.getLineRenderers((DataSource)object);
                    if (!list2.isEmpty()) {
                        object4 = (LineRenderer)list2.get(0);
                    }
                    if (object4 != null) {
                        list2 = object4.getLineShape(list);
                        list = object4.getLine(list, (Shape)((Object)list2));
                        list.draw(drawingContext);
                    }
                    list2 = null;
                    list = this.b.b.getPointRenderers((DataSource)object);
                    if (!list.isEmpty()) {
                        list2 = (PointRenderer)list.get(0);
                    }
                    if (list2 != null) {
                        object = drawingContext.getGraphics();
                        list = ((DataPoint)object3).position.getPoint2D();
                        object3 = ((Graphics2D)object).getTransform();
                        ((Graphics2D)object).translate(((Point2D)((Object)list)).getX(), ((Point2D)((Object)list)).getY());
                        list = list2.getPointShape((PointData)object2);
                        object2 = list2.getPoint((PointData)object2, (Shape)((Object)list));
                        object2.draw(drawingContext);
                        ((Graphics2D)object).setTransform((AffineTransform)object3);
                    }
                }
            };
        }
    }

    public static class XYPlotArea2D
    extends PlotArea {
        private static final long serialVersionUID = -3673157774425536428L;
        private final XYPlot a;
        private boolean b;
        private boolean c;
        private Paint d;
        private boolean e;
        private boolean f;
        private Paint g;

        public XYPlotArea2D(XYPlot xYPlot) {
            this.a = xYPlot;
            this.b = true;
            this.c = true;
            this.d = new Color(0.0f, 0.0f, 0.0f, 0.1f);
            this.e = false;
            this.f = false;
            this.g = new Color(0.0f, 0.0f, 0.0f, 0.05f);
        }

        @Override
        public void draw(DrawingContext drawingContext) {
            this.drawBackground(drawingContext);
            this.drawGrid(drawingContext);
            this.drawBorder(drawingContext);
            this.drawPlot(drawingContext);
            this.a.drawAxes(drawingContext);
            this.a.drawLegend(drawingContext);
        }

        protected void drawGrid(DrawingContext object) {
            Object object2;
            Object object3;
            Iterator iterator;
            object = ((DrawingContext)object).getGraphics();
            AffineTransform affineTransform = ((Graphics2D)object).getTransform();
            ((Graphics2D)object).translate(this.getX(), this.getY());
            AffineTransform affineTransform2 = ((Graphics2D)object).getTransform();
            Cloneable cloneable = this.getBounds();
            if (this.isMajorGridX() || this.isMinorGridX()) {
                iterator = this.a.getAxisRenderer(XYPlot.AXIS_X);
                object3 = this.a.getAxis(XYPlot.AXIS_X);
                if (iterator != null && object3 != null && ((Axis)object3).isValid()) {
                    object2 = iterator.getShape();
                    object2 = object2.getBounds2D();
                    iterator = iterator.getTicks((Axis)object3);
                    object3 = new Line2D.Double(-((RectangularShape)object2).getMinX(), -((RectangularShape)object2).getMinY(), -((RectangularShape)object2).getMinX(), ((RectangularShape)cloneable).getHeight() - ((RectangularShape)object2).getMinY());
                    iterator = iterator.iterator();
                    while (iterator.hasNext()) {
                        Point2D point2D;
                        object2 = (Tick)iterator.next();
                        if (((Tick)object2).type == Tick.TickType.MAJOR && !this.isMajorGridX() || ((Tick)object2).type == Tick.TickType.MINOR && !this.isMinorGridX() || (point2D = ((Tick)object2).position.getPoint2D()) == null) continue;
                        Paint paint = this.d;
                        if (((Tick)object2).type == Tick.TickType.MINOR) {
                            paint = this.getMinorGridColor();
                        }
                        ((Graphics2D)object).translate(point2D.getX(), point2D.getY());
                        GraphicsUtils.drawPaintedShape((Graphics2D)object, (Shape)object3, paint, null, null);
                        ((Graphics2D)object).setTransform(affineTransform2);
                    }
                }
            }
            if (this.isMajorGridY() || this.isMinorGridY()) {
                iterator = this.a.getAxis(XYPlot.AXIS_Y);
                object3 = this.a.getAxisRenderer(XYPlot.AXIS_Y);
                if (iterator != null && ((Axis)((Object)iterator)).isValid() && object3 != null) {
                    object2 = object3.getShape();
                    object2 = object2.getBounds2D();
                    iterator = object3.getTicks((Axis)((Object)iterator));
                    object3 = new Line2D.Double(-((RectangularShape)object2).getMinX(), -((RectangularShape)object2).getMinY(), ((RectangularShape)cloneable).getWidth() - ((RectangularShape)object2).getMinX(), -((RectangularShape)object2).getMinY());
                    iterator = iterator.iterator();
                    while (iterator.hasNext()) {
                        boolean bl;
                        object2 = (Tick)iterator.next();
                        boolean bl2 = ((Tick)object2).type == Tick.TickType.MAJOR;
                        boolean bl3 = bl = ((Tick)object2).type == Tick.TickType.MINOR;
                        if (bl2 && !this.isMajorGridY() || bl && !this.isMinorGridY() || (cloneable = ((Tick)object2).position.getPoint2D()) == null) continue;
                        object2 = this.d;
                        if (bl) {
                            object2 = this.getMinorGridColor();
                        }
                        ((Graphics2D)object).translate(((Point2D)cloneable).getX(), ((Point2D)cloneable).getY());
                        GraphicsUtils.drawPaintedShape((Graphics2D)object, (Shape)object3, (Paint)object2, null, null);
                        ((Graphics2D)object).setTransform(affineTransform2);
                    }
                }
            }
            ((Graphics2D)object).setTransform(affineTransform);
        }

        /*
         * Could not resolve type clashes
         */
        @Override
        protected void drawPlot(DrawingContext drawingContext) {
            Graphics2D graphics2D = drawingContext.getGraphics();
            Shape shape = graphics2D.getClip();
            Insets2D insets2D = this.getClippingOffset();
            if (insets2D != null) {
                double d = this.getBaseFont().getSize2D();
                Object object = new Rectangle2D.Double(this.getX() + insets2D.getLeft() * d, this.getY() + insets2D.getTop() * d, this.getWidth() - insets2D.getHorizontal() * d, this.getHeight() - insets2D.getVertical() * d);
                if (shape != null) {
                    Object object2 = new Area(shape);
                    ((Area)object2).intersect(new Area((Shape)object));
                    object = object2;
                }
                graphics2D.setClip((Shape)object);
            }
            AffineTransform affineTransform = graphics2D.getTransform();
            graphics2D.translate(this.getX(), this.getY());
            AffineTransform affineTransform2 = graphics2D.getTransform();
            for (Object object2 : this.a.getVisibleData()) {
                Object object9;
                Object object32;
                Object object4;
                PointND<Double> pointND;
                Object object52;
                Iterator iterator2;
                ArrayList<LineRenderer> arrayList;
                Iterable<Comparable<?>> iterable;
                if (object2.getColumnCount() == 0 || 0 >= object2.getColumnCount() || !object2.isColumnNumeric(0) || 1 >= object2.getColumnCount() || !object2.isColumnNumeric(1)) continue;
                Object object62 = this.a.getMapping((DataSource)object2);
                Object object72 = this.a.getAxis(object62[0]);
                Object object8 = this.a.getAxis(object62[1]);
                if (!((Axis)object72).isValid() || !((Axis)object8).isValid()) continue;
                AxisRenderer axisRenderer = this.a.getAxisRenderer(object62[0]);
                object62 = this.a.getAxisRenderer(object62[1]);
                LinkedList<DataPoint> linkedList = new LinkedList<DataPoint>();
                for (int i = 0; i < object2.getRowCount(); ++i) {
                    iterable = new Row((DataSource)object2, i);
                    arrayList = (Number)((Object)((Row)iterable).get(0));
                    iterator2 = (Number)((Object)((Row)iterable).get(1));
                    object52 = axisRenderer != null ? axisRenderer.getPosition((Axis)object72, (Number)((Object)arrayList), true, false) : new PointND((Number[])new Double[]{0.0, 0.0});
                    PointND<Double> pointND2 = pointND = object62 != null ? object62.getPosition((Axis)object8, (Number)((Object)iterator2), true, false) : new PointND((Number[])new Double[]{0.0, 0.0});
                    if (object52 == null || pointND == null) continue;
                    object4 = new PointND((Number[])new Double[]{(Double)((PointND)object52).get(0), (Double)pointND.get(1)});
                    object32 = new PointData(Arrays.asList(object72, object8), (List<? extends AxisRenderer>)Arrays.asList(axisRenderer, object62), (Row)iterable, 1);
                    object9 = new DataPoint((PointData)object32, (PointND<Double>)object4);
                    linkedList.add((DataPoint)object9);
                }
                ArrayList<PointRenderer> arrayList2 = new ArrayList<PointRenderer>(this.a.getPointRenderers((DataSource)object2));
                Collections.reverse(arrayList2);
                iterable = new ArrayList<AreaRenderer>(this.a.getAreaRenderers((DataSource)object2));
                Collections.reverse(iterable);
                for (Iterator iterator2 : iterable) {
                    object52 = iterator2.getAreaShape(linkedList);
                    pointND = object52;
                    for (Object object32 : arrayList2) {
                        object9 = new ArrayList<Shape>(linkedList.size());
                        for (Object object62 : linkedList) {
                            object72 = object32.getPointShape(((DataPoint)object62).data);
                            object9.add(object72);
                        }
                        pointND = XYPlotArea2D.punch(pointND, linkedList, object9, iterator2.getGap(), iterator2.isGapRounded());
                    }
                    object4 = iterator2.getArea(linkedList, (Shape)((Object)pointND));
                    object4.draw(drawingContext);
                }
                arrayList = new ArrayList<LineRenderer>(this.a.getLineRenderers((DataSource)object2));
                Collections.reverse(arrayList);
                iterator2 = arrayList.iterator();
                while (iterator2.hasNext()) {
                    object52 = (LineRenderer)iterator2.next();
                    pointND = object52.getLineShape(linkedList);
                    object4 = pointND;
                    for (Object object9 : arrayList2) {
                        ArrayList arrayList3 = new ArrayList(linkedList.size());
                        for (Object object72 : linkedList) {
                            object8 = object9.getPointShape(((DataPoint)object72).data);
                            arrayList3.add(object8);
                        }
                        object4 = XYPlotArea2D.punch((Shape)object4, linkedList, arrayList3, object52.getGap(), object52.isGapRounded());
                    }
                    object32 = object52.getLine(linkedList, (Shape)object4);
                    object32.draw(drawingContext);
                }
                if (this.a.getPointRenderers((DataSource)object2).isEmpty()) continue;
                for (Object object52 : linkedList) {
                    pointND = ((DataPoint)object52).position;
                    double d = pointND.get(0);
                    double d2 = pointND.get(1);
                    graphics2D.translate(d, d2);
                    for (Object object72 : this.a.getPointRenderers((DataSource)object2)) {
                        object8 = object72.getPointShape(((DataPoint)object52).data);
                        object72 = object72.getPoint(((DataPoint)object52).data, (Shape)object8);
                        object72.draw(drawingContext);
                    }
                    graphics2D.setTransform(affineTransform2);
                }
                iterator2 = linkedList.iterator();
                while (iterator2.hasNext()) {
                    object52 = (DataPoint)iterator2.next();
                    pointND = ((DataPoint)object52).position;
                    double d = pointND.get(0);
                    double d3 = pointND.get(1);
                    graphics2D.translate(d, d3);
                    for (Object object72 : this.a.getPointRenderers((DataSource)object2)) {
                        object8 = object72.getPointShape(((DataPoint)object52).data);
                        object72 = object72.getValue(((DataPoint)object52).data, (Shape)object8);
                        object72.draw(drawingContext);
                    }
                    graphics2D.setTransform(affineTransform2);
                }
            }
            graphics2D.setTransform(affineTransform);
            if (insets2D != null) {
                graphics2D.setClip(shape);
            }
        }

        protected static Shape punch(Shape shape, List<DataPoint> list, List<Shape> list2, double d, boolean bl) {
            if (!MathUtils.isCalculatable(d) || d == 0.0) {
                return shape;
            }
            shape = new Area(shape);
            for (int i = 0; i < list.size(); ++i) {
                DataPoint dataPoint = list.get(i);
                shape = GeometryUtils.punch((Area)shape, d, bl, dataPoint.position.getPoint2D(), list2.get(i));
            }
            return shape;
        }

        public boolean isMajorGridX() {
            return this.b;
        }

        public void setMajorGridX(boolean bl) {
            this.b = bl;
        }

        public boolean isMajorGridY() {
            return this.c;
        }

        public void setMajorGridY(boolean bl) {
            this.c = bl;
        }

        public Paint getMajorGridColor() {
            return this.d;
        }

        public void setMajorGridColor(Color color) {
            this.d = color;
        }

        public boolean isMinorGridX() {
            return this.e;
        }

        public void setMinorGridX(boolean bl) {
            this.e = bl;
        }

        public boolean isMinorGridY() {
            return this.f;
        }

        public void setMinorGridY(boolean bl) {
            this.f = bl;
        }

        public Paint getMinorGridColor() {
            return this.g;
        }

        public void setMinorGridColor(Color color) {
            this.g = color;
        }
    }

    public static class XYPlotNavigator
    extends PlotNavigator {
        public XYPlotNavigator(XYPlot xYPlot) {
            super((Plot)xYPlot, XYNavigationDirection.ARBITRARY.getAxesNames());
        }

        @Override
        public void setDirection(NavigationDirection navigationDirection) {
            if (navigationDirection == this.getDirection()) {
                return;
            }
            if (!(navigationDirection instanceof XYNavigationDirection)) {
                throw new IllegalArgumentException("Unknown direction.");
            }
            String[] stringArray = ((XYNavigationDirection)navigationDirection).getAxesNames();
            this.setAxes(stringArray);
            super.setDirection(navigationDirection);
        }

        @Override
        protected Number getDimensionValue(String string, PointND<? extends Number> pointND) {
            if (XYPlot.AXIS_Y.equals(string) || XYPlot.AXIS_Y2.equals(string)) {
                return -pointND.get(1).doubleValue();
            }
            return pointND.get(0);
        }

        @Override
        protected int getDimensions() {
            return 2;
        }
    }

    public static enum XYNavigationDirection implements NavigationDirection
    {
        HORIZONTAL("x", "x2"),
        VERTICAL("y", "y2"),
        ARBITRARY("x", "y", "x2", "y2");

        private final String[] a;

        private XYNavigationDirection(String ... stringArray) {
            this.a = stringArray;
        }

        public final String[] getAxesNames() {
            return this.a;
        }
    }
}

