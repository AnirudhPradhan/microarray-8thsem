/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d;

import de.erichseifert.vectorgraphics2d.util.GraphicsUtils;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

public class GraphicsState
implements Cloneable {
    public static final Color DEFAULT_BACKGROUND = Color.BLACK;
    public static final Color DEFAULT_COLOR = Color.WHITE;
    public static final Shape DEFAULT_CLIP = null;
    public static final Composite DEFAULT_COMPOSITE = AlphaComposite.SrcOver;
    public static final Font DEFAULT_FONT = Font.decode(null);
    public static final Color DEFAULT_PAINT = DEFAULT_COLOR;
    public static final Stroke DEFAULT_STROKE = new BasicStroke();
    public static final AffineTransform DEFAULT_TRANSFORM = new AffineTransform();
    public static final Color DEFAULT_XOR_MODE = Color.BLACK;
    private RenderingHints a = new RenderingHints(null);
    private Color b = DEFAULT_BACKGROUND;
    private Color c = DEFAULT_COLOR;
    private Shape d = DEFAULT_CLIP;
    private Composite e = DEFAULT_COMPOSITE;
    private Font f = DEFAULT_FONT;
    private Paint g = DEFAULT_PAINT;
    private Stroke h = DEFAULT_STROKE;
    private AffineTransform i = new AffineTransform(DEFAULT_TRANSFORM);
    private Color j = DEFAULT_XOR_MODE;

    public Object clone() throws CloneNotSupportedException {
        GraphicsState graphicsState = (GraphicsState)super.clone();
        ((GraphicsState)super.clone()).a = (RenderingHints)this.a.clone();
        graphicsState.d = GraphicsUtils.clone(this.d);
        graphicsState.i = new AffineTransform(this.i);
        return graphicsState;
    }

    private static Shape a(Shape shape, AffineTransform affineTransform) {
        boolean bl;
        if (shape == null) {
            return null;
        }
        if (affineTransform == null || affineTransform.isIdentity()) {
            return GraphicsUtils.clone(shape);
        }
        boolean bl2 = shape instanceof Rectangle2D;
        boolean bl3 = bl = (affineTransform.getType() & 0x30) == 0;
        if (bl2 && bl) {
            shape = (Rectangle2D)shape;
            double[] dArray = new double[]{((RectangularShape)shape).getMinX(), ((RectangularShape)shape).getMinY(), ((RectangularShape)shape).getMaxX(), ((RectangularShape)shape).getMaxY()};
            affineTransform.transform(dArray, 0, dArray, 0, 2);
            shape = new Rectangle2D.Double();
            ((RectangularShape)shape).setFrameFromDiagonal(dArray[0], dArray[1], dArray[2], dArray[3]);
            return shape;
        }
        return affineTransform.createTransformedShape(shape);
    }

    private static Shape b(Shape shape, AffineTransform affineTransform) {
        if (shape == null) {
            return null;
        }
        try {
            affineTransform = affineTransform.createInverse();
            return GraphicsState.a(shape, affineTransform);
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            return null;
        }
    }

    public Shape transformShape(Shape shape) {
        return GraphicsState.a(shape, this.i);
    }

    public Shape untransformShape(Shape shape) {
        return GraphicsState.b(shape, this.i);
    }

    public RenderingHints getHints() {
        return this.a;
    }

    public Color getBackground() {
        return this.b;
    }

    public void setBackground(Color color) {
        this.b = color;
    }

    public Color getColor() {
        return this.c;
    }

    public void setColor(Color color) {
        this.c = color;
    }

    public Shape getClip() {
        return this.untransformShape(this.d);
    }

    public void setClip(Shape shape) {
        this.d = this.transformShape(shape);
    }

    public Composite getComposite() {
        return this.e;
    }

    public void setComposite(Composite composite) {
        this.e = composite;
    }

    public Font getFont() {
        return this.f;
    }

    public void setFont(Font font) {
        this.f = font;
    }

    public Paint getPaint() {
        return this.g;
    }

    public void setPaint(Paint paint) {
        this.g = paint;
    }

    public Stroke getStroke() {
        return this.h;
    }

    public void setStroke(Stroke stroke) {
        this.h = stroke;
    }

    public AffineTransform getTransform() {
        return new AffineTransform(this.i);
    }

    public void setTransform(AffineTransform affineTransform) {
        this.i.setTransform(affineTransform);
    }

    public Color getXorMode() {
        return this.j;
    }

    public void setXorMode(Color color) {
        this.j = color;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof GraphicsState)) {
            return false;
        }
        object = (GraphicsState)object;
        return this.a.equals(((GraphicsState)object).a) && this.b.equals(((GraphicsState)object).b) && this.c.equals(((GraphicsState)object).c) && this.e.equals(((GraphicsState)object).e) && this.f.equals(((GraphicsState)object).f) && this.g.equals(((GraphicsState)object).g) && this.h.equals(((GraphicsState)object).h) && this.i.equals(((GraphicsState)object).i) && this.j.equals(((GraphicsState)object).j) && (this.d != null && ((GraphicsState)object).d != null || this.d == ((GraphicsState)object).d) && (this.d == null || this.d.equals(((GraphicsState)object).d));
    }

    public boolean isDefault() {
        return this.a.isEmpty() && this.b.equals(DEFAULT_BACKGROUND) && this.c.equals(DEFAULT_COLOR) && this.e.equals(DEFAULT_COMPOSITE) && this.f.equals(DEFAULT_FONT) && this.g.equals(DEFAULT_PAINT) && this.h.equals(DEFAULT_STROKE) && this.i.equals(DEFAULT_TRANSFORM) && this.j.equals(DEFAULT_XOR_MODE) && this.d == DEFAULT_CLIP;
    }
}

