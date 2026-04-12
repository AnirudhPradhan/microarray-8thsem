/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics;

import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.MathUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

public class Label
extends AbstractDrawable {
    private static final long serialVersionUID = 374045708533704103L;
    private String a;
    private double b;
    private double c;
    private Font d;
    private double e;
    private Paint f;
    private double g;
    private boolean h;
    private Paint i;
    private transient Shape j;
    private transient Shape k;

    public Label() {
        this("");
    }

    public Label(String string) {
        this.a = string;
        this.b = 0.5;
        this.c = 0.5;
        this.d = Font.decode(null);
        this.e = 0.0;
        this.f = Color.BLACK;
        this.g = 0.5;
        this.h = false;
    }

    @Override
    public void draw(DrawingContext object) {
        Object object2;
        boolean bl = this.isWordWrapEnabled();
        Shape shape = this.getCachedOutline(bl);
        if (shape == null) {
            return;
        }
        Object object3 = shape.getBounds2D();
        double d = this.getRotation();
        if (MathUtils.isCalculatable(d) && d != 0.0) {
            object2 = AffineTransform.getRotateInstance(Math.toRadians(-d), ((RectangularShape)object3).getCenterX(), ((RectangularShape)object3).getCenterY());
            shape = ((AffineTransform)object2).createTransformedShape(shape);
            object3 = shape.getBounds2D();
        }
        object2 = ((DrawingContext)object).getGraphics();
        object = ((Graphics2D)object2).getTransform();
        Paint paint = this.getBackground();
        if (paint != null) {
            GraphicsUtils.fillPaintedShape((Graphics2D)object2, this.getBounds(), paint, null);
        }
        double d2 = this.getX() - ((RectangularShape)object3).getX();
        double d3 = this.getY() - ((RectangularShape)object3).getY();
        double d4 = this.getAlignmentX();
        double d5 = this.getAlignmentY();
        ((Graphics2D)object2).translate(d2 += d4 * (this.getWidth() - ((RectangularShape)object3).getWidth()), d3 += d5 * (this.getHeight() - ((RectangularShape)object3).getHeight()));
        object3 = this.getColor();
        GraphicsUtils.fillPaintedShape((Graphics2D)object2, shape, (Paint)object3, null);
        ((Graphics2D)object2).setTransform((AffineTransform)object);
    }

    @Override
    public Dimension2D getPreferredSize() {
        Dimension2D dimension2D = super.getPreferredSize();
        if (this.getCachedOutline(false) != null) {
            Shape shape = this.getTextRectangle();
            Cloneable cloneable = shape.getBounds2D();
            double d = this.getRotation();
            if (MathUtils.isCalculatable(d) && d != 0.0) {
                cloneable = AffineTransform.getRotateInstance(Math.toRadians(-d), cloneable.getCenterX(), cloneable.getCenterY());
                shape = ((AffineTransform)cloneable).createTransformedShape(shape);
            }
            dimension2D.setSize(shape.getBounds2D().getWidth(), shape.getBounds2D().getHeight());
        }
        return dimension2D;
    }

    protected Shape getOutline(boolean bl) {
        double d;
        Font font = this.getFont();
        float f = 0.0f;
        if (bl) {
            d = Math.toRadians(this.getRotation());
            f = (float)(Math.abs(Math.cos(d)) * this.getWidth() + Math.abs(Math.sin(d)) * this.getHeight());
        }
        d = this.getTextAlignment();
        Shape shape = GraphicsUtils.getOutline(this.getText(), font, f, d);
        return shape;
    }

    protected Shape getCachedOutline(boolean bl) {
        if (!this.isValid() && this.getText() != null && !this.getText().isEmpty()) {
            this.j = this.getOutline(true);
            this.k = this.getOutline(false);
        }
        if (bl) {
            return this.j;
        }
        return this.k;
    }

    public Rectangle2D getTextRectangle() {
        return this.getCachedOutline(false).getBounds();
    }

    public String getText() {
        return this.a;
    }

    public void setText(String string) {
        this.a = string;
        this.invalidate();
    }

    protected void invalidate() {
        this.j = null;
        this.k = null;
    }

    protected boolean isValid() {
        boolean bl = this.isWordWrapEnabled();
        if (bl) {
            return this.j != null;
        }
        return this.k != null;
    }

    @Override
    public void setBounds(double d, double d2, double d3, double d4) {
        double d5 = this.getWidth();
        double d6 = this.getHeight();
        super.setBounds(d, d2, d3, d4);
        if (d3 != d5 || d4 != d6) {
            this.invalidate();
        }
    }

    public double getAlignmentX() {
        return this.b;
    }

    public void setAlignmentX(double d) {
        this.b = d;
    }

    public double getAlignmentY() {
        return this.c;
    }

    public void setAlignmentY(double d) {
        this.c = d;
    }

    public Font getFont() {
        return this.d;
    }

    public void setFont(Font font) {
        this.d = font;
        this.invalidate();
    }

    public double getRotation() {
        return this.e;
    }

    public void setRotation(double d) {
        this.e = d;
        this.invalidate();
    }

    public Paint getColor() {
        return this.f;
    }

    public void setColor(Paint paint) {
        this.f = paint;
    }

    public double getTextAlignment() {
        return this.g;
    }

    public void setTextAlignment(double d) {
        this.g = d;
        this.invalidate();
    }

    public boolean isWordWrapEnabled() {
        return this.h;
    }

    public void setWordWrapEnabled(boolean bl) {
        this.h = bl;
        this.invalidate();
    }

    public Paint getBackground() {
        return this.i;
    }

    public void setBackground(Paint paint) {
        this.i = paint;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Label)) {
            return false;
        }
        object = (Label)object;
        return (this.getText() == null && ((Label)object).getText() == null || this.getText().equals(((Label)object).getText())) && this.getAlignmentX() == ((Label)object).getAlignmentX() && this.getAlignmentY() == ((Label)object).getAlignmentY() && (this.getFont() == null && ((Label)object).getFont() == null || this.getFont().equals(((Label)object).getFont())) && this.getRotation() == ((Label)object).getRotation() && (this.getColor() == null && ((Label)object).getColor() == null || this.getColor().equals(((Label)object).getColor())) && this.getTextAlignment() == ((Label)object).getTextAlignment() && this.isWordWrapEnabled() == ((Label)object).isWordWrapEnabled() && (this.getBackground() == null && ((Label)object).getBackground() == null || this.getBackground().equals(((Label)object).getBackground()));
    }
}

