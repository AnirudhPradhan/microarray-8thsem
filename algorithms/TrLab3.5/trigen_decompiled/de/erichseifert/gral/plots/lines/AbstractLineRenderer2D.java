/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.lines;

import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.util.SerializationUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class AbstractLineRenderer2D
implements LineRenderer,
Serializable {
    private static final long serialVersionUID = -4172505541305453796L;
    private transient Stroke a = new BasicStroke(1.5f);
    private double b = 0.0;
    private boolean c = false;
    private Paint d = Color.BLACK;

    protected Shape stroke(Shape shape) {
        if (shape == null) {
            return null;
        }
        Stroke stroke = this.getStroke();
        shape = stroke.createStrokedShape(shape);
        return shape;
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.a = (Stroke)SerializationUtils.unwrap((Serializable)objectInputStream.readObject());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws ClassNotFoundException, IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(SerializationUtils.wrap(this.a));
    }

    @Override
    public Stroke getStroke() {
        return this.a;
    }

    @Override
    public void setStroke(Stroke stroke) {
        this.a = stroke;
    }

    @Override
    public double getGap() {
        return this.b;
    }

    @Override
    public void setGap(double d) {
        this.b = d;
    }

    @Override
    public boolean isGapRounded() {
        return this.c;
    }

    @Override
    public void setGapRounded(boolean bl) {
        this.c = bl;
    }

    @Override
    public Paint getColor() {
        return this.d;
    }

    @Override
    public void setColor(Paint paint) {
        this.d = paint;
    }
}

