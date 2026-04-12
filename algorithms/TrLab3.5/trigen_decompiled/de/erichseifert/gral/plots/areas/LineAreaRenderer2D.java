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
import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.List;

public class LineAreaRenderer2D
extends AbstractAreaRenderer {
    private static final long serialVersionUID = -8396097579938931392L;
    private Stroke a = new BasicStroke(1.0f);

    @Override
    public Drawable getArea(List<DataPoint> list, Shape shape) {
        return new AbstractDrawable(this, shape){
            private static final long serialVersionUID = 5492321759151727458L;
            private /* synthetic */ Shape a;
            private /* synthetic */ LineAreaRenderer2D b;
            {
                this.b = lineAreaRenderer2D;
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
        double d4 = 0.0;
        if (object2 != null) {
            d4 = object2.getPosition((Axis)serializable, d3, true, false).get(1);
        }
        serializable = new Path2D.Double();
        object = object.iterator();
        while (object.hasNext()) {
            object2 = (DataPoint)object.next();
            object2 = ((DataPoint)object2).position.getPoint2D();
            double d5 = ((Point2D)object2).getX();
            double d6 = ((Point2D)object2).getY();
            ((Path2D)((Object)serializable)).moveTo(d5, d6);
            ((Path2D)((Object)serializable)).lineTo(d5, d4);
        }
        object = this.getStroke();
        return object.createStrokedShape((Shape)((Object)serializable));
    }

    public Stroke getStroke() {
        return this.a;
    }

    public void setStroke(Stroke stroke) {
        this.a = stroke;
    }
}

