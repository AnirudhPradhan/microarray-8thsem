/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.points;

import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointData;
import de.erichseifert.gral.util.GraphicsUtils;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.RectangularShape;
import java.text.Format;
import java.text.NumberFormat;

public class LabelPointRenderer
extends DefaultPointRenderer2D {
    private static final long serialVersionUID = -2612520977245369774L;
    private int a = 1;
    private Format b = NumberFormat.getInstance();
    private Font c = Font.decode(null);
    private double d = 0.5;
    private double e = 0.5;

    public int getColumn() {
        return this.a;
    }

    public void setColumn(int n) {
        this.a = n;
    }

    public Format getFormat() {
        return this.b;
    }

    public void setFormat(Format format) {
        this.b = format;
    }

    public Font getFont() {
        return this.c;
    }

    public void setFont(Font font) {
        this.c = font;
    }

    public double getAlignmentX() {
        return this.d;
    }

    public void setAlignmentX(double d) {
        this.d = d;
    }

    public double getAlignmentY() {
        return this.e;
    }

    public void setAlignmentY(double d) {
        this.e = d;
    }

    @Override
    public Shape getPointShape(PointData object) {
        object = ((PointData)object).row;
        int n = this.getColumn();
        if (n >= ((Row)object).size()) {
            return null;
        }
        if ((object = ((Row)object).get(n)) == null) {
            return null;
        }
        Cloneable cloneable = this.getFormat();
        Font font = this.getFont();
        object = ((Format)cloneable).format(object);
        double d = this.getAlignmentX();
        object = GraphicsUtils.getOutline((String)object, font, 0.0f, d);
        double d2 = this.getAlignmentX();
        double d3 = this.getAlignmentY();
        cloneable = object.getBounds2D();
        cloneable = AffineTransform.getTranslateInstance(-d2 * ((RectangularShape)cloneable).getWidth(), d3 * ((RectangularShape)cloneable).getHeight());
        object = ((AffineTransform)cloneable).createTransformedShape((Shape)object);
        return object;
    }
}

