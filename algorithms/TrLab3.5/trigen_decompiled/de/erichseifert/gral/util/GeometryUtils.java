/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import de.erichseifert.gral.util.MathUtils;
import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class GeometryUtils {
    public static final double EPSILON = 1.0E-5;
    public static final double EPSILON_SQ = 1.0000000000000002E-10;

    private GeometryUtils() {
        throw new UnsupportedOperationException();
    }

    public static Line2D[] shapeToLines(Shape object, boolean bl) {
        ArrayDeque<Cloneable> arrayDeque = new ArrayDeque<Cloneable>();
        object = new FlatteningPathIterator(object.getPathIterator(null), 0.5);
        double[] dArray = new double[6];
        double[] dArray2 = new double[6];
        while (!object.isDone()) {
            Cloneable cloneable;
            Cloneable cloneable2;
            int n = object.currentSegment(dArray);
            if (n == 1 || n == 4) {
                if (!bl) {
                    cloneable2 = new Line2D.Double(dArray2[0], dArray2[1], dArray[0], dArray[1]);
                    arrayDeque.addLast(cloneable2);
                } else {
                    cloneable2 = new Line2D.Double(dArray[0], dArray[1], dArray2[0], dArray2[1]);
                    arrayDeque.addFirst(cloneable2);
                }
            }
            if (n == 4 && !arrayDeque.isEmpty() && !((Point2D)(cloneable2 = ((Line2D)arrayDeque.getFirst()).getP1())).equals(cloneable = ((Line2D)arrayDeque.getLast()).getP2())) {
                if (!bl) {
                    cloneable = new Line2D.Double(dArray[0], dArray[1], ((Point2D)cloneable2).getX(), ((Point2D)cloneable2).getY());
                    arrayDeque.addLast(cloneable);
                } else {
                    cloneable = new Line2D.Double(((Point2D)cloneable2).getX(), ((Point2D)cloneable2).getY(), dArray[0], dArray[1]);
                    arrayDeque.addFirst(cloneable);
                }
            }
            System.arraycopy(dArray, 0, dArray2, 0, 6);
            object.next();
        }
        Line2D[] line2DArray = new Line2D[arrayDeque.size()];
        arrayDeque.toArray(line2DArray);
        return line2DArray;
    }

    public static List<Point2D> intersection(Shape line2DArray, Shape line2DArray2) {
        ArrayList<Point2D> arrayList = new ArrayList<Point2D>(2);
        line2DArray = GeometryUtils.shapeToLines((Shape)line2DArray, false);
        line2DArray2 = GeometryUtils.shapeToLines((Shape)line2DArray2, false);
        for (Line2D line2D : line2DArray) {
            Line2D[] line2DArray3 = line2DArray2;
            int n = line2DArray2.length;
            for (int i = 0; i < n; ++i) {
                Cloneable cloneable = line2DArray3[i];
                if ((cloneable = GeometryUtils.intersection(line2D, cloneable)) == null) continue;
                arrayList.add((Point2D)cloneable);
            }
        }
        return arrayList;
    }

    public static Point2D intersection(Line2D cloneable, Line2D cloneable2) {
        double d;
        double d2;
        Point2D point2D = ((Line2D)cloneable).getP1();
        cloneable = new Point2D.Double(((Line2D)cloneable).getX2() - point2D.getX(), ((Line2D)cloneable).getY2() - point2D.getY());
        Point2D point2D2 = ((Line2D)cloneable2).getP1();
        cloneable2 = new Point2D.Double(((Line2D)cloneable2).getX2() - point2D2.getX(), ((Line2D)cloneable2).getY2() - point2D2.getY());
        point2D2 = new Point2D.Double(point2D2.getX() - point2D.getX(), point2D2.getY() - point2D.getY());
        double d3 = ((Point2D)cloneable).getX() * ((Point2D)cloneable2).getY() - ((Point2D)cloneable).getY() * ((Point2D)cloneable2).getX();
        double d4 = d3 * d3;
        if (d4 > (d2 = ((Point2D)cloneable).distanceSq(0.0, 0.0)) * 1.0000000000000002E-10 * (d = ((Point2D)cloneable2).distanceSq(0.0, 0.0))) {
            double d5 = (point2D2.getX() * ((Point2D)cloneable2).getY() - point2D2.getY() * ((Point2D)cloneable2).getX()) / d3;
            if (d5 < 0.0 || d5 > 1.0) {
                return null;
            }
            double d6 = (point2D2.getX() * ((Point2D)cloneable).getY() - point2D2.getY() * ((Point2D)cloneable).getX()) / d3;
            if (d6 < 0.0 || d6 > 1.0) {
                return null;
            }
            return new Point2D.Double(point2D.getX() + d5 * ((Point2D)cloneable).getX(), point2D.getY() + d5 * ((Point2D)cloneable).getY());
        }
        return null;
    }

    public static Area grow(Shape shape, double d) {
        return GeometryUtils.grow(shape, d, 0, 10.0f);
    }

    public static Area grow(Shape shape, double d, int n, float f) {
        Area area = new Area(shape);
        if (MathUtils.almostEqual(d, 0.0, 1.0E-5)) {
            return area;
        }
        BasicStroke basicStroke = new BasicStroke((float)Math.abs(d * 2.0), 2, n, f);
        shape = new Area(basicStroke.createStrokedShape(shape));
        if (d > 0.0) {
            area.add((Area)shape);
        } else {
            area.subtract((Area)shape);
        }
        return area;
    }

    public static Area punch(Area area, double d, boolean bl, Point2D cloneable, Shape shape) {
        if (d <= 1.0E-10 || cloneable == null || shape == null) {
            return area;
        }
        cloneable = AffineTransform.getTranslateInstance(((Point2D)cloneable).getX(), ((Point2D)cloneable).getY());
        bl = bl;
        Area area2 = GeometryUtils.grow(((AffineTransform)cloneable).createTransformedShape(shape), d, bl ? 1 : 0, 10.0f);
        area.subtract(area2);
        return area;
    }

    public static List<PathSegment> getSegments(Shape object) {
        object = object.getPathIterator(null);
        Serializable serializable = null;
        Point2D.Double double_ = null;
        double[] dArray = new double[6];
        LinkedList<PathSegment> linkedList = new LinkedList<PathSegment>();
        while (!object.isDone()) {
            int n = object.currentSegment(dArray);
            if (n == 0 || n == 1) {
                double_ = new Point2D.Double(dArray[0], dArray[1]);
            } else if (n == 2) {
                double_ = new Point2D.Double(dArray[2], dArray[3]);
            } else if (n == 3) {
                double_ = new Point2D.Double(dArray[4], dArray[5]);
            }
            serializable = new PathSegment(n, (Point2D)((Object)serializable), double_, dArray);
            linkedList.add((PathSegment)serializable);
            serializable = double_;
            object.next();
        }
        return linkedList;
    }

    public static Shape getShape(List<PathSegment> object, boolean bl) {
        if (bl) {
            Path2D.Double double_ = new Path2D.Double(1, object.size());
            object = object.iterator();
            while (object.hasNext()) {
                PathSegment pathSegment = (PathSegment)object.next();
                double[] dArray = pathSegment.coords;
                if (pathSegment.type == 0) {
                    double_.moveTo(dArray[0], dArray[1]);
                    continue;
                }
                if (pathSegment.type == 1) {
                    double_.lineTo(dArray[0], dArray[1]);
                    continue;
                }
                if (pathSegment.type == 2) {
                    double_.quadTo(dArray[0], dArray[1], dArray[2], dArray[3]);
                    continue;
                }
                if (pathSegment.type == 3) {
                    double_.curveTo(dArray[0], dArray[1], dArray[2], dArray[3], dArray[4], dArray[5]);
                    continue;
                }
                if (pathSegment.type != 4) continue;
                double_.closePath();
            }
            return double_;
        }
        return GeometryUtils.a(object);
    }

    private static Shape a(List<PathSegment> object) {
        Path2D.Float float_ = new Path2D.Float(1, object.size());
        object = object.iterator();
        while (object.hasNext()) {
            PathSegment pathSegment = (PathSegment)object.next();
            float[] fArray = new float[pathSegment.coords.length];
            for (int i = 0; i < fArray.length; ++i) {
                fArray[i] = (float)pathSegment.coords[i];
            }
            if (pathSegment.type == 0) {
                float_.moveTo(fArray[0], fArray[1]);
                continue;
            }
            if (pathSegment.type == 1) {
                float_.lineTo(fArray[0], fArray[1]);
                continue;
            }
            if (pathSegment.type == 2) {
                float_.quadTo(fArray[0], fArray[1], fArray[2], fArray[3]);
                continue;
            }
            if (pathSegment.type == 3) {
                float_.curveTo(fArray[0], fArray[1], fArray[2], fArray[3], fArray[4], fArray[5]);
                continue;
            }
            if (pathSegment.type != 4) continue;
            float_.closePath();
        }
        return float_;
    }

    public static Shape reverse(Shape listIterator) {
        listIterator = GeometryUtils.getSegments((Shape)((Object)listIterator));
        boolean bl = false;
        Path2D.Double double_ = new Path2D.Double(1, listIterator.size());
        listIterator = listIterator.listIterator(listIterator.size());
        while (listIterator.hasPrevious()) {
            PathSegment pathSegment = (PathSegment)listIterator.previous();
            if (pathSegment.type == 4) {
                bl = true;
                continue;
            }
            if (double_.getCurrentPoint() == null) {
                ((Path2D)double_).moveTo(pathSegment.end.getX(), pathSegment.end.getY());
            }
            if (pathSegment.type == 1) {
                ((Path2D)double_).lineTo(pathSegment.start.getX(), pathSegment.start.getY());
                continue;
            }
            if (pathSegment.type == 2) {
                ((Path2D)double_).quadTo(pathSegment.coords[0], pathSegment.coords[1], pathSegment.start.getX(), pathSegment.start.getY());
                continue;
            }
            if (pathSegment.type == 3) {
                ((Path2D)double_).curveTo(pathSegment.coords[2], pathSegment.coords[3], pathSegment.coords[0], pathSegment.coords[1], pathSegment.start.getX(), pathSegment.start.getY());
                continue;
            }
            if (pathSegment.type != 0 || !bl) continue;
            double_.closePath();
            bl = false;
        }
        return double_;
    }

    public static final class PathSegment
    implements Serializable {
        private static final long serialVersionUID = 526444553637955799L;
        public final int type;
        public final Point2D start;
        public final Point2D end;
        public final double[] coords;

        public PathSegment(int n, Point2D point2D, Point2D point2D2, double[] dArray) {
            this.type = n;
            this.start = point2D;
            this.end = point2D2;
            this.coords = new double[6];
            System.arraycopy(dArray, 0, this.coords, 0, 6);
        }
    }
}

