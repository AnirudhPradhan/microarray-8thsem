/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.lines;

import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.plots.DataPoint;
import de.erichseifert.gral.plots.lines.AbstractLineRenderer2D;
import de.erichseifert.gral.util.GraphicsUtils;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

public class SmoothLineRenderer2D
extends AbstractLineRenderer2D {
    private static final long serialVersionUID = -6390029474886495264L;
    private Number a = 1.0;

    @Override
    public Drawable getLine(List<DataPoint> object, Shape shape) {
        object = new AbstractDrawable(this, shape){
            private static final long serialVersionUID = 3641589240264518755L;
            private /* synthetic */ Shape a;
            private /* synthetic */ SmoothLineRenderer2D b;
            {
                this.b = smoothLineRenderer2D;
                this.a = shape;
            }

            @Override
            public final void draw(DrawingContext drawingContext) {
                Paint paint = this.b.getColor();
                GraphicsUtils.fillPaintedShape(drawingContext.getGraphics(), this.a, paint, null);
            }
        };
        return object;
    }

    @Override
    public Shape getLineShape(List<DataPoint> object) {
        double d = this.getSmoothness().doubleValue();
        Path2D.Double double_ = new Path2D.Double();
        Point2D point2D = null;
        Point2D point2D2 = null;
        Point2D point2D3 = null;
        Point2D point2D4 = null;
        Point2D.Double double_2 = new Point2D.Double();
        Point2D.Double double_3 = new Point2D.Double();
        object = object.iterator();
        while (object.hasNext()) {
            DataPoint dataPoint = (DataPoint)object.next();
            if (dataPoint == null) continue;
            point2D4 = dataPoint.position.getPoint2D();
            SmoothLineRenderer2D.a(double_, point2D, point2D2, point2D3, point2D4, double_2, double_3, d);
            point2D = point2D2;
            point2D2 = point2D3;
            point2D3 = point2D4;
        }
        SmoothLineRenderer2D.a(double_, point2D, point2D2, point2D3, point2D4, double_2, double_3, d);
        return this.stroke(double_);
    }

    private static void a(Path2D path2D, Point2D point2D, Point2D point2D2, Point2D point2D3, Point2D point2D4, Point2D point2D5, Point2D point2D6, double d) {
        if (point2D2 == null) {
            return;
        }
        if (path2D.getCurrentPoint() == null) {
            path2D.moveTo(point2D2.getX(), point2D2.getY());
        }
        if (point2D3 == null) {
            return;
        }
        double d2 = d;
        Point2D point2D7 = point2D6;
        Point2D point2D8 = point2D5;
        Point2D point2D9 = point2D4;
        point2D4 = point2D3;
        if (point2D == null) {
            point2D = point2D2;
        }
        if (point2D9 == null) {
            point2D9 = point2D4;
        }
        Point2D.Double double_ = new Point2D.Double((point2D.getX() + point2D2.getX()) / 2.0, (point2D.getY() + point2D2.getY()) / 2.0);
        Point2D.Double double_2 = new Point2D.Double((point2D2.getX() + point2D4.getX()) / 2.0, (point2D2.getY() + point2D4.getY()) / 2.0);
        Point2D.Double double_3 = new Point2D.Double((point2D4.getX() + point2D9.getX()) / 2.0, (point2D4.getY() + point2D9.getY()) / 2.0);
        double d3 = point2D2.distance(point2D);
        double d4 = point2D4.distance(point2D2);
        double d5 = point2D9.distance(point2D4);
        double d6 = d3 / (d3 + d4);
        double d7 = d4 / (d4 + d5);
        point2D = new Point2D.Double(((Point2D)double_).getX() + (((Point2D)double_2).getX() - ((Point2D)double_).getX()) * d6, ((Point2D)double_).getY() + (((Point2D)double_2).getY() - ((Point2D)double_).getY()) * d6);
        point2D9 = new Point2D.Double(((Point2D)double_2).getX() + (((Point2D)double_3).getX() - ((Point2D)double_2).getX()) * d7, ((Point2D)double_2).getY() + (((Point2D)double_3).getY() - ((Point2D)double_2).getY()) * d7);
        point2D8.setLocation(point2D.getX() + (((Point2D)double_2).getX() - point2D.getX()) * d2 + point2D2.getX() - point2D.getX(), point2D.getY() + (((Point2D)double_2).getY() - point2D.getY()) * d2 + point2D2.getY() - point2D.getY());
        point2D7.setLocation(point2D9.getX() + (((Point2D)double_2).getX() - point2D9.getX()) * d2 + point2D4.getX() - point2D9.getX(), point2D9.getY() + (((Point2D)double_2).getY() - point2D9.getY()) * d2 + point2D4.getY() - point2D9.getY());
        path2D.curveTo(point2D5.getX(), point2D5.getY(), point2D6.getX(), point2D6.getY(), point2D3.getX(), point2D3.getY());
    }

    public Number getSmoothness() {
        return this.a;
    }

    public void setSmoothness(Number number) {
        this.a = number;
    }
}

