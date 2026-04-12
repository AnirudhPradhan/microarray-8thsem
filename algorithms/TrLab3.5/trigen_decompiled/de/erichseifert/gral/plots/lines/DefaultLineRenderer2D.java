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

public class DefaultLineRenderer2D
extends AbstractLineRenderer2D {
    private static final long serialVersionUID = -1728830281555843667L;

    @Override
    public Drawable getLine(List<DataPoint> object, Shape shape) {
        object = new AbstractDrawable(this, shape){
            private static final long serialVersionUID = 7995515716470892483L;
            private /* synthetic */ Shape a;
            private /* synthetic */ DefaultLineRenderer2D b;
            {
                this.b = defaultLineRenderer2D;
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
        Path2D.Double double_ = new Path2D.Double(1, 10000);
        object = object.iterator();
        while (object.hasNext()) {
            Object object2 = (DataPoint)object.next();
            object2 = ((DataPoint)object2).position.getPoint2D();
            if (double_.getCurrentPoint() == null) {
                ((Path2D)double_).moveTo(((Point2D)object2).getX(), ((Point2D)object2).getY());
                continue;
            }
            ((Path2D)double_).lineTo(((Point2D)object2).getX(), ((Point2D)object2).getY());
        }
        return this.stroke(double_);
    }
}

