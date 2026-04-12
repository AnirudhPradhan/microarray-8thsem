/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.axes;

import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.Dimension2D;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.axes.Tick;
import de.erichseifert.gral.util.GeometryUtils;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.MathUtils;
import de.erichseifert.gral.util.PointND;
import de.erichseifert.gral.util.SerializationUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractAxisRenderer2D
implements AxisRenderer,
Serializable {
    private static final long serialVersionUID = 5623525683845512624L;
    private Line2D[] a;
    private Point2D[] b;
    private double[] c;
    private double[] d;
    private Number e = 0.0;
    private Shape f = new Line2D.Double(0.0, 0.0, 1.0, 0.0);
    private boolean g;
    private boolean h;
    private Paint i;
    private transient Stroke j;
    private boolean k = false;
    private boolean l;
    private Number m;
    private boolean n;
    private double o;
    private transient Stroke p;
    private double q;
    private Font r;
    private Paint s;
    private boolean t;
    private Format u;
    private double v;
    private boolean w;
    private double x;
    private boolean y;
    private int z;
    private double A;
    private transient Stroke B;
    private double C;
    private Paint D;
    private final Map<Double, String> E;
    private Label F;
    private double G;

    public AbstractAxisRenderer2D() {
        this.evaluateShape(this.f);
        this.g = true;
        this.h = false;
        this.j = new BasicStroke();
        this.i = Color.BLACK;
        this.l = true;
        this.m = 0.0;
        this.n = false;
        this.o = 1.0;
        this.p = new BasicStroke();
        this.q = 0.5;
        this.r = Font.decode(null);
        this.s = Color.BLACK;
        this.t = true;
        this.u = NumberFormat.getInstance();
        this.v = 1.0;
        this.w = true;
        this.x = 0.0;
        this.E = new HashMap<Double, String>();
        this.y = true;
        this.z = 1;
        this.A = 0.5;
        this.B = new BasicStroke();
        this.C = 0.5;
        this.D = Color.BLACK;
        this.F = new Label();
        this.G = 1.0;
    }

    @Override
    public Drawable getRendererComponent(Axis serializable) {
        serializable = new AbstractDrawable(this, (Axis)serializable){
            private static final long serialVersionUID = 3605211198378801694L;
            private /* synthetic */ Axis a;
            private /* synthetic */ AbstractAxisRenderer2D b;
            {
                this.b = abstractAxisRenderer2D;
                this.a = axis;
            }

            @Override
            public final void draw(DrawingContext drawingContext) {
                Label label;
                Object object;
                Object object2;
                double d;
                double d2;
                if (this.b.a == null || this.b.a.length == 0) {
                    return;
                }
                AbstractAxisRenderer2D abstractAxisRenderer2D = this.b;
                Graphics2D graphics2D = drawingContext.getGraphics();
                AffineTransform affineTransform = graphics2D.getTransform();
                graphics2D.translate(this.getX(), this.getY());
                Stroke stroke = graphics2D.getStroke();
                Paint paint = graphics2D.getPaint();
                Paint paint2 = abstractAxisRenderer2D.getShapeColor();
                Stroke stroke2 = abstractAxisRenderer2D.getShapeStroke();
                boolean bl = abstractAxisRenderer2D.isShapeVisible();
                if (bl) {
                    Shape shape = abstractAxisRenderer2D.getShape();
                    GraphicsUtils.drawPaintedShape(graphics2D, shape, paint2, null, stroke2);
                }
                double d3 = abstractAxisRenderer2D.getTickFont().getSize2D();
                boolean bl2 = abstractAxisRenderer2D.isTicksVisible();
                boolean bl3 = abstractAxisRenderer2D.isMinorTicksVisible();
                if (bl2 || bl2 && bl3) {
                    Object object3 = this.b.getTicks(this.a);
                    boolean bl4 = abstractAxisRenderer2D.isTickLabelsVisible();
                    boolean bl5 = abstractAxisRenderer2D.isTickLabelsOutside();
                    d2 = abstractAxisRenderer2D.getTickLabelRotation();
                    d = abstractAxisRenderer2D.getTickLabelDistanceAbsolute();
                    Line2D.Double double_ = new Line2D.Double();
                    Iterator<Tick> iterator = object3.iterator();
                    while (iterator.hasNext()) {
                        double d4;
                        double d5;
                        Tick tick = iterator.next();
                        if (tick.position == null || tick.normal == null) continue;
                        Point2D point2D = tick.position.getPoint2D();
                        Point2D point2D2 = tick.normal.getPoint2D();
                        if (Tick.TickType.MINOR.equals((Object)tick.type)) {
                            d5 = abstractAxisRenderer2D.getTickMinorLengthAbsolute();
                            d4 = abstractAxisRenderer2D.getMinorTickAlignment();
                            object2 = abstractAxisRenderer2D.getMinorTickColor();
                            object = abstractAxisRenderer2D.getMinorTickStroke();
                        } else {
                            d5 = this.b.getTickLengthAbsolute();
                            d4 = abstractAxisRenderer2D.getTickAlignment();
                            object2 = abstractAxisRenderer2D.getTickColor();
                            object = abstractAxisRenderer2D.getTickStroke();
                        }
                        double d6 = d5 * d4;
                        double d7 = d5 * (1.0 - d4);
                        if (bl2 && tick.type == Tick.TickType.MAJOR || tick.type == Tick.TickType.CUSTOM || bl3 && tick.type == Tick.TickType.MINOR) {
                            ((Line2D)double_).setLine(point2D.getX() - point2D2.getX() * d6, point2D.getY() - point2D2.getY() * d6, point2D.getX() + point2D2.getX() * d7, point2D.getY() + point2D2.getY() * d7);
                            GraphicsUtils.drawPaintedShape(graphics2D, double_, (Paint)object2, null, (Stroke)object);
                        }
                        if (!bl4 || tick.type != Tick.TickType.MAJOR && tick.type != Tick.TickType.CUSTOM || (object3 = tick.label) == null || ((String)object3).trim().isEmpty()) continue;
                        object3 = new Label((String)object3);
                        ((Label)object3).setFont(abstractAxisRenderer2D.getTickFont());
                        ((Label)object3).setColor((Paint)object2);
                        double d8 = d7 + d;
                        1.a((Label)object3, point2D, point2D2, d8, bl5, d2);
                        ((Label)object3).draw(drawingContext);
                    }
                }
                if ((label = abstractAxisRenderer2D.getLabel()) != null && !label.getText().trim().isEmpty()) {
                    double d9 = this.b.getTickLengthAbsolute();
                    d2 = abstractAxisRenderer2D.getTickAlignment();
                    d = d9 * (1.0 - d2);
                    double d10 = abstractAxisRenderer2D.getTickLabelDistanceAbsolute();
                    double d11 = abstractAxisRenderer2D.getLabelDistance() * d3;
                    double d12 = d + d10 + d3 + d11;
                    double d13 = (this.a.getMin().doubleValue() + this.a.getMax().doubleValue()) * 0.5;
                    boolean bl6 = abstractAxisRenderer2D.isTickLabelsOutside();
                    object2 = this.b.getPosition(this.a, d13, false, true);
                    object = this.b.getNormal(this.a, d13, false, true);
                    if (object2 != null && object != null) {
                        1.a(label, ((PointND)object2).getPoint2D(), ((PointND)object).getPoint2D(), d12, bl6, label.getRotation());
                        label.draw(drawingContext);
                    }
                }
                graphics2D.setPaint(paint);
                graphics2D.setStroke(stroke);
                graphics2D.setTransform(affineTransform);
            }

            private static void a(Label label, Point2D point2D, Point2D list, double d, boolean bl, double d2) {
                Rectangle2D rectangle2D = label.getTextRectangle();
                Shape shape = new Rectangle2D.Double(0.0, 0.0, rectangle2D.getWidth() + d * 2.0, rectangle2D.getHeight() + d * 2.0);
                Rectangle2D rectangle2D2 = shape.getBounds2D();
                label.setRotation(d2);
                if (d2 % 360.0 != 0.0) {
                    shape = AffineTransform.getRotateInstance(Math.toRadians(-d2), rectangle2D2.getCenterX(), rectangle2D2.getCenterY()).createTransformedShape(shape);
                }
                rectangle2D2 = shape.getBounds2D();
                double d3 = rectangle2D2.getHeight() * rectangle2D2.getHeight() + rectangle2D2.getWidth() * rectangle2D2.getWidth();
                double d4 = (bl ? -1.0 : 1.0) * d3;
                list = GeometryUtils.intersection(rectangle2D2, (Shape)new Line2D.Double(rectangle2D2.getCenterX(), rectangle2D2.getCenterY(), rectangle2D2.getCenterX() + d4 * ((Point2D)((Object)list)).getX(), rectangle2D2.getCenterY() + d4 * ((Point2D)((Object)list)).getY()));
                if (!list.isEmpty()) {
                    list = (Point2D)list.get(0);
                    double d5 = ((Point2D)((Object)list)).getX() - rectangle2D2.getCenterX();
                    double d6 = ((Point2D)((Object)list)).getY() - rectangle2D2.getCenterY();
                    double d7 = point2D.getX() - d5 - rectangle2D.getWidth() / 2.0;
                    double d8 = point2D.getY() - d6 - rectangle2D.getHeight() / 2.0;
                    label.setBounds(d7, d8, rectangle2D.getWidth(), rectangle2D.getHeight());
                }
            }

            @Override
            public final Dimension2D getPreferredSize() {
                AbstractAxisRenderer2D abstractAxisRenderer2D = this.b;
                double d = abstractAxisRenderer2D.getTickFont().getSize2D();
                double d2 = this.b.getTickLengthAbsolute();
                double d3 = abstractAxisRenderer2D.getTickAlignment();
                double d4 = d2 * (1.0 - d3);
                double d5 = abstractAxisRenderer2D.getTickLabelDistanceAbsolute() + d4;
                double d6 = d + d5 + d4;
                return new Dimension2D.Double(d6, d6);
            }
        };
        return serializable;
    }

    @Override
    public List<Tick> getTicks(Axis axis) {
        double d;
        Number number;
        LinkedList<Tick> linkedList = new LinkedList<Tick>();
        if (!axis.isValid()) {
            return linkedList;
        }
        double d2 = axis.getMin().doubleValue();
        double d3 = axis.getMax().doubleValue();
        HashSet<Double> hashSet = new HashSet<Double>();
        this.createTicksCustom(linkedList, axis, d2, d3, hashSet);
        boolean bl = this.isTicksAutoSpaced();
        if (!(bl || (number = this.getTickSpacing()) != null && !((d = number.doubleValue()) <= 0.0) && MathUtils.isCalculatable(d))) {
            bl = true;
        }
        this.createTicks(linkedList, axis, d2, d3, hashSet, bl);
        return linkedList;
    }

    protected double getTickLengthAbsolute() {
        double d = this.getTickFont().getSize2D();
        return this.getTickLength() * d;
    }

    protected double getTickMinorLengthAbsolute() {
        double d = this.getTickFont().getSize2D();
        return this.getMinorTickLength() * d;
    }

    protected double getTickLabelDistanceAbsolute() {
        double d = this.getTickFont().getSize2D();
        return this.getTickLabelDistance() * d;
    }

    protected abstract void createTicks(List<Tick> var1, Axis var2, double var3, double var5, Set<Double> var7, boolean var8);

    protected void createTicksCustom(List<Tick> list, Axis axis, double d, double d2, Set<Double> set) {
        Map<Double, String> map = this.getCustomTicks();
        if (map != null) {
            for (Number number : map.keySet()) {
                double d3 = number.doubleValue();
                if (d3 < d || d3 > d2) continue;
                Tick tick = this.getTick(Tick.TickType.CUSTOM, axis, d3);
                list.add(tick);
                set.add(d3);
            }
        }
    }

    protected Tick getTick(Tick.TickType tickType, Axis serializable, double d) {
        PointND<Double> pointND = this.getPosition((Axis)serializable, d, false, false);
        serializable = this.getNormal((Axis)serializable, d, false, false);
        Map<Double, String> map = this.getCustomTicks();
        String string = map != null && map.containsKey(d) ? (String)map.get(d) : ((map = this.getTickLabelFormat()) != null ? ((Format)((Object)map)).format(d) : String.valueOf(d));
        map = new Tick(tickType, pointND, (PointND<Double>)serializable, null, null, string);
        return map;
    }

    @Override
    public PointND<Double> getNormal(Axis axis, Number number, boolean bl, boolean bl2) {
        double d = bl2 ? (number.doubleValue() - axis.getMin().doubleValue()) / axis.getRange() * this.getShapeLength() : this.worldToView(axis, number, bl);
        int n = MathUtils.binarySearchFloor(this.d, d);
        if (n < 0 || n >= this.a.length) {
            return null;
        }
        n = MathUtils.limit(n, 0, this.b.length - 1);
        boolean bl3 = this.isShapeNormalOrientationClockwise();
        double d2 = bl3 ? 1.0 : -1.0;
        PointND pointND = new PointND((Number[])new Double[]{d2 * this.b[n].getX(), d2 * this.b[n].getY()});
        return pointND;
    }

    protected double getShapeLength() {
        if (this.d == null || this.d.length == 0) {
            return 0.0;
        }
        return this.d[this.d.length - 1];
    }

    @Override
    public PointND<Double> getPosition(Axis axis, Number object, boolean bl, boolean bl2) {
        double d;
        if (this.a == null || this.a.length == 0 || object == null) {
            return null;
        }
        double d2 = axis.getPosition((Number)object).doubleValue();
        if (!bl) {
            d2 = MathUtils.limit(d2, 0.0, 1.0);
        }
        if (Double.isNaN(d = bl2 ? d2 * this.getShapeLength() : this.worldToView(axis, (Number)object, bl))) {
            return null;
        }
        if (d == Double.NEGATIVE_INFINITY) {
            d = 0.0;
        } else if (d == Double.POSITIVE_INFINITY) {
            d = 1.0;
        }
        int n = d2 <= 0.0 ? 0 : (d2 >= 1.0 ? this.a.length - 1 : MathUtils.binarySearchFloor(this.d, d));
        if (n < 0 || n >= this.a.length) {
            return null;
        }
        object = this.a[n];
        double d3 = this.c[n];
        double d4 = this.d[n];
        double d5 = (d - d4) / d3;
        double d6 = ((Line2D)object).getX1() + (((Line2D)object).getX2() - ((Line2D)object).getX1()) * d5;
        double d7 = ((Line2D)object).getY1() + (((Line2D)object).getY2() - ((Line2D)object).getY1()) * d5;
        return new PointND((Number[])new Double[]{d6, d7});
    }

    protected final void evaluateShape(Shape shape) {
        boolean bl = this.isShapeDirectionSwapped();
        this.a = GeometryUtils.shapeToLines(shape, bl);
        this.c = new double[this.a.length];
        this.d = new double[this.a.length + 1];
        this.b = new Point2D[this.a.length];
        if (this.a.length == 0) {
            return;
        }
        for (int i = 0; i < this.a.length; ++i) {
            double d;
            Line2D line2D = this.a[i];
            this.c[i] = d = line2D.getP1().distance(line2D.getP2());
            this.d[i + 1] = this.d[i] + d;
            this.b[i] = new Point2D.Double((line2D.getY2() - line2D.getY1()) / d, -(line2D.getX2() - line2D.getX1()) / d);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.j = (Stroke)SerializationUtils.unwrap((Serializable)objectInputStream.readObject());
        this.p = (Stroke)SerializationUtils.unwrap((Serializable)objectInputStream.readObject());
        this.B = (Stroke)SerializationUtils.unwrap((Serializable)objectInputStream.readObject());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(SerializationUtils.wrap(this.j));
        objectOutputStream.writeObject(SerializationUtils.wrap(this.p));
        objectOutputStream.writeObject(SerializationUtils.wrap(this.B));
    }

    @Override
    public Number getIntersection() {
        return this.e;
    }

    @Override
    public void setIntersection(Number number) {
        this.e = number;
    }

    @Override
    public Shape getShape() {
        return this.f;
    }

    @Override
    public void setShape(Shape shape) {
        this.f = shape;
        this.evaluateShape(shape);
    }

    @Override
    public boolean isShapeVisible() {
        return this.g;
    }

    @Override
    public void setShapeVisible(boolean bl) {
        this.g = bl;
    }

    @Override
    public boolean isShapeNormalOrientationClockwise() {
        return this.h;
    }

    @Override
    public void setShapeNormalOrientationClockwise(boolean bl) {
        this.h = bl;
    }

    @Override
    public Paint getShapeColor() {
        return this.i;
    }

    @Override
    public void setShapeColor(Paint paint) {
        this.i = paint;
    }

    @Override
    public Stroke getShapeStroke() {
        return this.j;
    }

    @Override
    public void setShapeStroke(Stroke stroke) {
        this.j = stroke;
    }

    @Override
    public boolean isShapeDirectionSwapped() {
        return this.k;
    }

    @Override
    public void setShapeDirectionSwapped(boolean bl) {
        this.k = bl;
    }

    @Override
    public boolean isTicksVisible() {
        return this.l;
    }

    @Override
    public void setTicksVisible(boolean bl) {
        this.l = bl;
    }

    @Override
    public Number getTickSpacing() {
        return this.m;
    }

    @Override
    public void setTickSpacing(Number number) {
        this.m = number;
    }

    @Override
    public boolean isTicksAutoSpaced() {
        return this.n;
    }

    @Override
    public void setTicksAutoSpaced(boolean bl) {
        this.n = bl;
    }

    @Override
    public double getTickLength() {
        return this.o;
    }

    @Override
    public void setTickLength(double d) {
        this.o = d;
    }

    @Override
    public Stroke getTickStroke() {
        return this.p;
    }

    @Override
    public void setTickStroke(Stroke stroke) {
        this.p = stroke;
    }

    @Override
    public double getTickAlignment() {
        return this.q;
    }

    @Override
    public void setTickAlignment(double d) {
        this.q = d;
    }

    @Override
    public Font getTickFont() {
        return this.r;
    }

    @Override
    public void setTickFont(Font font) {
        this.r = font;
    }

    @Override
    public Paint getTickColor() {
        return this.s;
    }

    @Override
    public void setTickColor(Paint paint) {
        this.s = paint;
    }

    @Override
    public boolean isTickLabelsVisible() {
        return this.t;
    }

    @Override
    public void setTickLabelsVisible(boolean bl) {
        this.t = bl;
    }

    @Override
    public Format getTickLabelFormat() {
        return this.u;
    }

    @Override
    public void setTickLabelFormat(Format format) {
        this.u = format;
    }

    @Override
    public double getTickLabelDistance() {
        return this.v;
    }

    @Override
    public void setTickLabelDistance(double d) {
        this.v = d;
    }

    @Override
    public boolean isTickLabelsOutside() {
        return this.w;
    }

    @Override
    public void setTickLabelsOutside(boolean bl) {
        this.w = bl;
    }

    @Override
    public double getTickLabelRotation() {
        return this.x;
    }

    @Override
    public void setTickLabelRotation(double d) {
        this.x = d;
    }

    @Override
    public boolean isMinorTicksVisible() {
        return this.y;
    }

    @Override
    public void setMinorTicksVisible(boolean bl) {
        this.y = bl;
    }

    @Override
    public int getMinorTicksCount() {
        return this.z;
    }

    @Override
    public void setMinorTicksCount(int n) {
        this.z = n;
    }

    @Override
    public double getMinorTickLength() {
        return this.A;
    }

    @Override
    public void setMinorTickLength(double d) {
        this.A = d;
    }

    @Override
    public Stroke getMinorTickStroke() {
        return this.B;
    }

    @Override
    public void setMinorTickStroke(Stroke stroke) {
        this.B = stroke;
    }

    @Override
    public double getMinorTickAlignment() {
        return this.C;
    }

    @Override
    public void setMinorTickAlignment(double d) {
        this.C = d;
    }

    @Override
    public Paint getMinorTickColor() {
        return this.D;
    }

    @Override
    public void setMinorTickColor(Paint paint) {
        this.D = paint;
    }

    @Override
    public Map<Double, String> getCustomTicks() {
        return Collections.unmodifiableMap(this.E);
    }

    @Override
    public void setCustomTicks(Map<Double, String> map) {
        this.E.clear();
        this.E.putAll(map);
    }

    @Override
    public Label getLabel() {
        return this.F;
    }

    @Override
    public void setLabel(Label label) {
        this.F = label;
    }

    @Override
    public double getLabelDistance() {
        return this.G;
    }

    @Override
    public void setLabelDistance(double d) {
        this.G = d;
    }
}

