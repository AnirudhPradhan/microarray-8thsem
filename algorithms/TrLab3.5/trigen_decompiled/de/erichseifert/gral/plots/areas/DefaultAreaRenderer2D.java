/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.areas;

import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.plots.DataPoint;
import de.erichseifert.gral.plots.areas.AbstractAreaRenderer;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.MathUtils;
import de.erichseifert.gral.util.PointND;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.List;

public class DefaultAreaRenderer2D
extends AbstractAreaRenderer {
    private static final long serialVersionUID = -202003022764142849L;

    @Override
    public Drawable getArea(List<DataPoint> list, Shape shape) {
        return new AbstractDrawable(this, shape){
            private static final long serialVersionUID = -3659798228877496727L;
            private /* synthetic */ Shape a;
            private /* synthetic */ DefaultAreaRenderer2D b;
            {
                this.b = defaultAreaRenderer2D;
                this.a = shape;
            }

            @Override
            public final void draw(DrawingContext drawingContext) {
                Paint paint = this.b.getColor();
                GraphicsUtils.fillPaintedShape(drawingContext.getGraphics(), this.a, paint, null);
            }
        };
    }

    @Override
    public Shape getAreaShape(List<DataPoint> object) {
        if (object.isEmpty() || object.get(0) == null) {
            return null;
        }
        Serializable serializable = object.get((int)0).data.axes.get(1);
        Object object2 = object.get((int)0).data.axisRenderers.get(1);
        double d = ((Axis)serializable).getMin().doubleValue();
        double d2 = ((Axis)serializable).getMax().doubleValue();
        double d3 = MathUtils.limit(0.0, d, d2);
        PointND<Double> pointND = null;
        if (object2 != null) {
            pointND = object2.getPosition((Axis)serializable, d3, true, false);
        }
        serializable = new Path2D.Double();
        if (pointND == null) {
            return serializable;
        }
        double d4 = pointND.get(1);
        double d5 = 0.0;
        object = object.iterator();
        while (object.hasNext()) {
            object2 = (DataPoint)object.next();
            object2 = ((DataPoint)object2).position.getPoint2D();
            d5 = ((Point2D)object2).getX();
            double d6 = ((Point2D)object2).getY();
            if (((Path2D)((Object)serializable)).getCurrentPoint() == null) {
                ((Path2D)((Object)serializable)).moveTo(d5, d4);
            }
            ((Path2D)((Object)serializable)).lineTo(d5, d6);
        }
        if (((Path2D)((Object)serializable)).getCurrentPoint() != null) {
            ((Path2D)((Object)serializable)).lineTo(d5, d4);
            ((Path2D)((Object)serializable)).closePath();
        }
        return serializable;
    }
}

