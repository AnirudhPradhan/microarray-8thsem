/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.points;

import de.erichseifert.gral.graphics.Location;
import de.erichseifert.gral.plots.colors.ColorMapper;
import de.erichseifert.gral.plots.colors.SingleColor;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.util.SerializationUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.Format;

public abstract class AbstractPointRenderer
implements PointRenderer,
Serializable {
    private static final long serialVersionUID = -408976260196287753L;
    private Shape a = new Rectangle2D.Double(-2.5, -2.5, 5.0, 5.0);
    private ColorMapper b = new SingleColor(Color.BLACK);
    private boolean c = false;
    private int d = 1;
    private Format e;
    private Location f = Location.CENTER;
    private double g = 0.5;
    private double h = 0.5;
    private double i = 0.0;
    private double j = 1.0;
    private ColorMapper k = new SingleColor(Color.BLACK);
    private Font l = Font.decode(null);
    private boolean m = false;
    private int n = 2;
    private int o = 3;
    private ColorMapper p = new SingleColor(Color.BLACK);
    private Shape q = new Line2D.Double(-2.0, 0.0, 2.0, 0.0);
    private transient Stroke r = new BasicStroke(1.0f);

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.r = (Stroke)SerializationUtils.unwrap((Serializable)objectInputStream.readObject());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws ClassNotFoundException, IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(SerializationUtils.wrap(this.r));
    }

    @Override
    public Shape getShape() {
        return this.a;
    }

    @Override
    public void setShape(Shape shape) {
        this.a = shape;
    }

    @Override
    public ColorMapper getColor() {
        return this.b;
    }

    @Override
    public void setColor(ColorMapper colorMapper) {
        this.b = colorMapper;
    }

    @Override
    public void setColor(Paint paint) {
        this.setColor(new SingleColor(paint));
    }

    @Override
    public boolean isValueVisible() {
        return this.c;
    }

    @Override
    public void setValueVisible(boolean bl) {
        this.c = bl;
    }

    @Override
    public int getValueColumn() {
        return this.d;
    }

    @Override
    public void setValueColumn(int n) {
        this.d = n;
    }

    @Override
    public Format getValueFormat() {
        return this.e;
    }

    @Override
    public void setValueFormat(Format format) {
        this.e = format;
    }

    @Override
    public Location getValueLocation() {
        return this.f;
    }

    @Override
    public void setValueLocation(Location location) {
        this.f = location;
    }

    @Override
    public double getValueAlignmentX() {
        return this.g;
    }

    @Override
    public void setValueAlignmentX(double d) {
        this.g = d;
    }

    @Override
    public double getValueAlignmentY() {
        return this.h;
    }

    @Override
    public void setValueAlignmentY(double d) {
        this.h = d;
    }

    @Override
    public double getValueRotation() {
        return this.i;
    }

    @Override
    public void setValueRotation(double d) {
        this.i = d;
    }

    @Override
    public double getValueDistance() {
        return this.j;
    }

    @Override
    public void setValueDistance(double d) {
        this.j = d;
    }

    @Override
    public ColorMapper getValueColor() {
        return this.k;
    }

    @Override
    public void setValueColor(ColorMapper colorMapper) {
        this.k = colorMapper;
    }

    @Override
    public void setValueColor(Paint paint) {
        this.setValueColor(new SingleColor(paint));
    }

    @Override
    public Font getValueFont() {
        return this.l;
    }

    @Override
    public void setValueFont(Font font) {
        this.l = font;
    }

    @Override
    public boolean isErrorVisible() {
        return this.m;
    }

    @Override
    public void setErrorVisible(boolean bl) {
        this.m = bl;
    }

    @Override
    public int getErrorColumnTop() {
        return this.n;
    }

    @Override
    public void setErrorColumnTop(int n) {
        this.n = n;
    }

    @Override
    public int getErrorColumnBottom() {
        return this.o;
    }

    @Override
    public void setErrorColumnBottom(int n) {
        this.o = n;
    }

    @Override
    public ColorMapper getErrorColor() {
        return this.p;
    }

    @Override
    public void setErrorColor(ColorMapper colorMapper) {
        this.p = colorMapper;
    }

    @Override
    public void setErrorColor(Paint paint) {
        this.setErrorColor(new SingleColor(paint));
    }

    @Override
    public Shape getErrorShape() {
        return this.q;
    }

    @Override
    public void setErrorShape(Shape shape) {
        this.q = shape;
    }

    @Override
    public Stroke getErrorStroke() {
        return this.r;
    }

    @Override
    public void setErrorStroke(Stroke stroke) {
        this.r = stroke;
    }
}

