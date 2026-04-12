/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots;

import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.SerializationUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class PlotArea
extends AbstractDrawable {
    private static final long serialVersionUID = 2745982325709470005L;
    private Font a = null;
    private Paint b = Color.WHITE;
    private transient Stroke c = new BasicStroke(1.0f);
    private Paint d = Color.BLACK;
    private Insets2D e = new Insets2D.Double(0.0);

    protected void drawBackground(DrawingContext drawingContext) {
        Paint paint = this.getBackground();
        if (paint != null) {
            GraphicsUtils.fillPaintedShape(drawingContext.getGraphics(), this.getBounds(), paint, null);
        }
    }

    protected void drawBorder(DrawingContext drawingContext) {
        Stroke stroke = this.getBorderStroke();
        if (stroke != null) {
            Paint paint = this.getBorderColor();
            GraphicsUtils.drawPaintedShape(drawingContext.getGraphics(), this.getBounds(), paint, null, stroke);
        }
    }

    protected abstract void drawPlot(DrawingContext var1);

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.c = (Stroke)SerializationUtils.unwrap((Serializable)objectInputStream.readObject());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws ClassNotFoundException, IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(SerializationUtils.wrap(this.c));
    }

    public Font getBaseFont() {
        return this.a;
    }

    public void setBaseFont(Font font) {
        this.a = font;
    }

    public Paint getBackground() {
        return this.b;
    }

    public void setBackground(Paint paint) {
        this.b = paint;
    }

    public Stroke getBorderStroke() {
        return this.c;
    }

    public void setBorderStroke(Stroke stroke) {
        this.c = stroke;
    }

    public Paint getBorderColor() {
        return this.d;
    }

    public void setBorderColor(Paint paint) {
        this.d = paint;
    }

    public Insets2D getClippingOffset() {
        return this.e;
    }

    public void setClippingArea(Insets2D insets2D) {
        this.e = insets2D;
    }
}

