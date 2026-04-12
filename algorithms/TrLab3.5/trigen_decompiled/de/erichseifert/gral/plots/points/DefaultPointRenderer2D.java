/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.points;

import de.erichseifert.gral.data.DataAccessor;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawableContainer;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.graphics.layout.OuterEdgeLayout;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.points.AbstractPointRenderer;
import de.erichseifert.gral.plots.points.PointData;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.MathUtils;
import de.erichseifert.gral.util.PointND;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;

public class DefaultPointRenderer2D
extends AbstractPointRenderer {
    private static final long serialVersionUID = -895832597380598383L;

    @Override
    public Drawable getPoint(PointData object, Shape shape) {
        object = new AbstractDrawable(this, (PointData)object, shape){
            private static final long serialVersionUID = 1915778739867091906L;
            private /* synthetic */ PointData a;
            private /* synthetic */ Shape b;
            private /* synthetic */ DefaultPointRenderer2D c;
            {
                this.c = defaultPointRenderer2D;
                this.a = pointData;
                this.b = shape;
            }

            @Override
            public final void draw(DrawingContext drawingContext) {
                DefaultPointRenderer2D defaultPointRenderer2D = this.c;
                Axis axis = this.a.axes.get(1);
                AxisRenderer axisRenderer = this.a.axisRenderers.get(1);
                Row row = this.a.row;
                int n = this.a.col;
                Object object = this.c.getColor();
                object = object.get(row.getIndex());
                GraphicsUtils.fillPaintedShape(drawingContext.getGraphics(), this.b, (Paint)object, null);
                if (defaultPointRenderer2D.isErrorVisible()) {
                    int n2 = defaultPointRenderer2D.getErrorColumnTop();
                    int n3 = defaultPointRenderer2D.getErrorColumnBottom();
                    this.c.drawErrorBars(drawingContext, this.b, row, n, n2, n3, axis, axisRenderer);
                }
            }
        };
        return object;
    }

    protected void drawValueLabel(DrawingContext drawingContext, Shape shape, Row object, int n) {
        Object object2 = ((Row)object).get(n);
        Object object3 = this.getValueFormat();
        if (object3 == null && ((Row)object).isColumnNumeric(n)) {
            object3 = NumberFormat.getInstance();
        }
        Object object4 = object3 != null ? object3.format(object2) : object2.toString();
        object2 = this.getValueColor();
        object = object2.get(((DataAccessor)object).getIndex());
        object2 = this.getValueFont();
        double d = ((Font)object2).getSize2D();
        object3 = this.getValueLocation();
        double d2 = this.getValueAlignmentX();
        double d3 = this.getValueAlignmentY();
        double d4 = this.getValueRotation();
        double d5 = this.getValueDistance();
        d5 = MathUtils.isCalculatable(d5) ? (d5 *= d) : 0.0;
        object4 = new Label((String)object4);
        ((Label)object4).setAlignmentX(d2);
        ((Label)object4).setAlignmentY(d3);
        ((Label)object4).setRotation(d4);
        ((Label)object4).setColor((Paint)object);
        ((Label)object4).setFont((Font)object2);
        shape = shape.getBounds2D();
        object = new DrawableContainer(new OuterEdgeLayout(d5));
        ((DrawableContainer)object).add((Drawable)object4, object3);
        ((DrawableContainer)object).setBounds((Rectangle2D)shape);
        ((DrawableContainer)object).draw(drawingContext);
    }

    protected void drawErrorBars(DrawingContext object, Shape object2, Row object3, int n, int n2, int n3, Axis axis, AxisRenderer axisRenderer) {
        if (axisRenderer == null) {
            return;
        }
        if (n2 < 0 || n2 >= ((Row)object3).size() || !((Row)object3).isColumnNumeric(n2) || n3 < 0 || n3 >= ((Row)object3).size() || !((Row)object3).isColumnNumeric(n3)) {
            return;
        }
        object2 = (Number)((Object)((Row)object3).get(n));
        Object object4 = (Number)((Object)((Row)object3).get(n2));
        Number number = (Number)((Object)((Row)object3).get(n3));
        if (!(MathUtils.isCalculatable((Number)object2) && MathUtils.isCalculatable((Number)object4) && MathUtils.isCalculatable(number))) {
            return;
        }
        object = ((DrawingContext)object).getGraphics();
        AffineTransform affineTransform = ((Graphics2D)object).getTransform();
        PointND<Double> pointND = axisRenderer.getPosition(axis, (Number)object2, true, false);
        object4 = axisRenderer.getPosition(axis, ((Number)object2).doubleValue() + ((Number)object4).doubleValue(), true, false);
        object2 = axisRenderer.getPosition(axis, ((Number)object2).doubleValue() - number.doubleValue(), true, false);
        if (pointND == null || object4 == null || object2 == null) {
            return;
        }
        double d = pointND.get(1);
        double d2 = (Double)((PointND)object4).get(1) - d;
        double d3 = ((PointND)object2).get(1) - d;
        object2 = new Line2D.Double(0.0, d2, 0.0, d3);
        object4 = this.getErrorColor();
        object3 = object4.get(((DataAccessor)object3).getIndex());
        object4 = this.getErrorStroke();
        GraphicsUtils.drawPaintedShape((Graphics2D)object, object2, (Paint)object3, null, (Stroke)object4);
        object2 = this.getErrorShape();
        ((Graphics2D)object).translate(0.0, d2);
        object4 = new BasicStroke(1.0f);
        GraphicsUtils.drawPaintedShape((Graphics2D)object, (Shape)object2, (Paint)object3, null, (Stroke)object4);
        ((Graphics2D)object).setTransform(affineTransform);
        ((Graphics2D)object).translate(0.0, d3);
        GraphicsUtils.drawPaintedShape((Graphics2D)object, (Shape)object2, (Paint)object3, null, (Stroke)object4);
        ((Graphics2D)object).setTransform(affineTransform);
    }

    @Override
    public Shape getPointShape(PointData pointData) {
        return this.getShape();
    }

    @Override
    public Drawable getValue(PointData object, Shape shape) {
        object = new AbstractDrawable(this, (PointData)object, shape){
            private static final long serialVersionUID = -2568531344817590175L;
            private /* synthetic */ PointData a;
            private /* synthetic */ Shape b;
            private /* synthetic */ DefaultPointRenderer2D c;
            {
                this.c = defaultPointRenderer2D;
                this.a = pointData;
                this.b = shape;
            }

            @Override
            public final void draw(DrawingContext drawingContext) {
                DefaultPointRenderer2D defaultPointRenderer2D = this.c;
                Row row = this.a.row;
                if (defaultPointRenderer2D.isValueVisible()) {
                    int n = defaultPointRenderer2D.getValueColumn();
                    this.c.drawValueLabel(drawingContext, this.b, row, n);
                }
            }
        };
        return object;
    }
}

