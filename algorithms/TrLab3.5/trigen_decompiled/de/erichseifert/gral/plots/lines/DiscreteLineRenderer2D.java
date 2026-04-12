/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.lines;

import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.graphics.Orientation;
import de.erichseifert.gral.plots.DataPoint;
import de.erichseifert.gral.plots.lines.AbstractLineRenderer2D;
import de.erichseifert.gral.util.GraphicsUtils;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

public class DiscreteLineRenderer2D
extends AbstractLineRenderer2D {
    private static final long serialVersionUID = 4648286099838467355L;
    private Orientation a = Orientation.HORIZONTAL;
    private Number b = 0.5;

    @Override
    public Drawable getLine(List<DataPoint> object, Shape shape) {
        object = new AbstractDrawable(this, shape){
            private static final long serialVersionUID = -1686744943386843195L;
            private /* synthetic */ Shape a;
            private /* synthetic */ DiscreteLineRenderer2D b;
            {
                this.b = discreteLineRenderer2D;
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
        Orientation orientation = this.getAscentDirection();
        double d = this.getAscendingPoint().doubleValue();
        Path2D.Double double_ = new Path2D.Double();
        object = object.iterator();
        while (object.hasNext()) {
            double d2;
            Object object2 = (DataPoint)object.next();
            object2 = ((DataPoint)object2).position.getPoint2D();
            if (double_.getCurrentPoint() == null) {
                ((Path2D)double_).moveTo(((Point2D)object2).getX(), ((Point2D)object2).getY());
                continue;
            }
            Point2D point2D = double_.getCurrentPoint();
            if (orientation == Orientation.HORIZONTAL) {
                d2 = point2D.getX() + (((Point2D)object2).getX() - point2D.getX()) * d;
                ((Path2D)double_).lineTo(d2, point2D.getY());
                ((Path2D)double_).lineTo(d2, ((Point2D)object2).getY());
            } else {
                d2 = point2D.getY() + (((Point2D)object2).getY() - point2D.getY()) * d;
                ((Path2D)double_).lineTo(point2D.getX(), d2);
                ((Path2D)double_).lineTo(((Point2D)object2).getX(), d2);
            }
            ((Path2D)double_).lineTo(((Point2D)object2).getX(), ((Point2D)object2).getY());
        }
        return this.stroke(double_);
    }

    public Orientation getAscentDirection() {
        return this.a;
    }

    public void setAscentDirection(Orientation orientation) {
        this.a = orientation;
    }

    public Number getAscendingPoint() {
        return this.b;
    }

    public void setAscendingPoint(Number number) {
        this.b = number;
    }
}

