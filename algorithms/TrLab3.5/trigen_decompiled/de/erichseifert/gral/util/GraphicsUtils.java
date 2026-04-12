/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import de.erichseifert.gral.util.MathUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class GraphicsUtils {
    private static final FontRenderContext a = new FontRenderContext(null, true, true);
    private static final double[] b = new double[]{0.964221, 1.0, 0.825211};
    private static final double c = 4.0 * b[0] / (b[0] + 15.0 * b[1] + 3.0 * b[2]);
    private static final double d = 9.0 * b[1] / (b[0] + 15.0 * b[1] + 3.0 * b[2]);
    private static final double[] e = new double[]{0.436052025, 0.385081593, 0.143087414, 0.222491598, 0.71688606, 0.060621486, 0.013929122, 0.097097002, 0.71418547};
    private static final double[] f = new double[]{3.1338561, -1.6168667, -0.4906146, -0.9787684, 1.9161415, 0.033454, 0.0719453, -0.2289914, 1.4052427};

    protected GraphicsUtils() {
        throw new UnsupportedOperationException();
    }

    public static Shape getOutline(String object, Font object2, float f, double d) {
        TextLayout textLayout;
        boolean bl = true;
        if (f <= 0.0f) {
            bl = false;
            f = Float.MAX_VALUE;
        }
        Object object3 = new AttributedString((String)object);
        ((AttributedString)object3).addAttribute(TextAttribute.FONT, object2);
        object2 = ((AttributedString)object3).getIterator();
        object2 = new LineBreakMeasurer((AttributedCharacterIterator)object2, a);
        object3 = new LinkedList();
        while (((LineBreakMeasurer)object2).getPosition() < ((String)object).length()) {
            int n = ((LineBreakMeasurer)object2).nextOffset(f);
            int n2 = ((String)object).indexOf(10, ((LineBreakMeasurer)object2).getPosition()) + 1;
            int n3 = n;
            if (n2 > 0 && n2 < n) {
                n3 = n2;
            }
            textLayout = ((LineBreakMeasurer)object2).nextLayout(f, n3, false);
            object3.add(textLayout);
        }
        if (!bl) {
            float f2 = 0.0f;
            Iterator iterator = object3.iterator();
            while (iterator.hasNext()) {
                TextLayout textLayout2 = (TextLayout)iterator.next();
                f2 = Math.max(textLayout2.getAdvance(), f2);
            }
            f = f2;
        }
        AffineTransform affineTransform = new AffineTransform();
        Object object4 = null;
        Iterator iterator = object3.iterator();
        while (iterator.hasNext()) {
            textLayout = (TextLayout)iterator.next();
            double d2 = d * (double)(f - textLayout.getAdvance());
            affineTransform.translate(d2, textLayout.getAscent());
            object = new Area(textLayout.getOutline(affineTransform));
            if (object4 == null) {
                object4 = object;
            } else {
                ((Area)object4).add((Area)object);
            }
            affineTransform.translate(-d2, textLayout.getDescent() + textLayout.getLeading());
        }
        return object4;
    }

    public static void fillPaintedShape(Graphics2D graphics2D, Shape shape, Paint object, Rectangle2D rectangle2D) {
        if (shape == null) {
            return;
        }
        if (rectangle2D == null) {
            rectangle2D = shape.getBounds2D();
        }
        AffineTransform affineTransform = graphics2D.getTransform();
        graphics2D.translate(rectangle2D.getX(), rectangle2D.getY());
        graphics2D.scale(rectangle2D.getWidth(), rectangle2D.getHeight());
        Paint paint = null;
        if (object != null) {
            paint = graphics2D.getPaint();
            graphics2D.setPaint((Paint)object);
        }
        object = AffineTransform.getScaleInstance(1.0 / rectangle2D.getWidth(), 1.0 / rectangle2D.getHeight());
        ((AffineTransform)object).translate(-rectangle2D.getX(), -rectangle2D.getY());
        graphics2D.fill(((AffineTransform)object).createTransformedShape(shape));
        if (paint != null) {
            graphics2D.setPaint(paint);
        }
        graphics2D.setTransform(affineTransform);
    }

    public static void drawPaintedShape(Graphics2D graphics2D, Shape shape, Paint paint, Rectangle2D rectangle2D, Stroke stroke) {
        if (shape == null) {
            return;
        }
        if (stroke == null) {
            stroke = graphics2D.getStroke();
        }
        shape = stroke.createStrokedShape(shape);
        GraphicsUtils.fillPaintedShape(graphics2D, shape, paint, rectangle2D);
    }

    public static double[] rgb2xyz(double[] dArray, double[] dArray2) {
        int n;
        if (dArray2 == null) {
            dArray2 = new double[dArray.length];
        }
        double[] dArray3 = new double[dArray.length];
        for (n = 0; n < dArray.length; ++n) {
            dArray3[n] = dArray[n] <= 0.04045 ? dArray[n] / 12.92 : Math.pow((dArray[n] + 0.055) / 1.055, 2.4);
        }
        for (n = 0; n < dArray2.length; ++n) {
            dArray2[n] = e[n * 3] * dArray3[0] + e[n * 3 + 1] * dArray3[1] + e[n * 3 + 2] * dArray3[2];
        }
        return dArray2;
    }

    public static double[] luv2xyz(double[] dArray, double[] dArray2) {
        if (dArray2 == null) {
            dArray2 = new double[dArray.length];
        }
        if (dArray[0] > 8.0) {
            dArray2[1] = (dArray[0] + 16.0) / 116.0;
            dArray2[1] = dArray2[1] * dArray2[1] * dArray2[1];
        } else {
            dArray2[1] = dArray[0] / 903.2962962962963;
        }
        double d = dArray[0] != 0.0 || dArray[1] != 0.0 ? (52.0 * dArray[0] / (dArray[1] + 13.0 * dArray[0] * c) - 1.0) / 3.0 : 0.0;
        double d2 = -5.0 * dArray2[1];
        double d3 = dArray[0] != 0.0 || dArray[2] != 0.0 ? dArray2[1] * (39.0 * dArray[0] / (dArray[2] + 13.0 * dArray[0] * GraphicsUtils.d) - 5.0) : 0.0;
        dArray2[0] = !MathUtils.almostEqual(d, -0.3333333333333333, 1.0E-15) ? (d3 - d2) / (d - -0.3333333333333333) : 0.0;
        dArray2[2] = dArray2[0] * d + d2;
        return dArray2;
    }

    public static double[] xyz2rgb(double[] dArray, double[] dArray2) {
        int n;
        if (dArray2 == null) {
            dArray2 = new double[dArray.length];
        }
        for (n = 0; n < dArray.length; ++n) {
            dArray2[n] = f[n * 3] * dArray[0] + f[n * 3 + 1] * dArray[1] + f[n * 3 + 2] * dArray[2];
        }
        for (n = 0; n < dArray2.length; ++n) {
            dArray2[n] = dArray2[n] <= 0.0031308 ? 12.92 * dArray2[n] : 1.055 * Math.pow(dArray2[n], 0.4166666666666667) - 0.055;
        }
        return dArray2;
    }

    public static double[] xyz2luv(double[] dArray, double[] dArray2) {
        double d = dArray[0] + 15.0 * dArray[1] + 3.0 * dArray[2];
        if (d == 0.0) {
            d = 1.0;
        }
        double d2 = 4.0 * dArray[0] / d;
        double d3 = 9.0 * dArray[1] / d;
        double d4 = dArray[1] / b[1];
        double d5 = 4.0 * b[0] / (b[0] + 15.0 * b[1] + 3.0 * b[2]);
        double d6 = 9.0 * b[1] / (b[0] + 15.0 * b[1] + 3.0 * b[2]);
        if (dArray2 == null) {
            dArray2 = new double[dArray.length];
        }
        dArray2[0] = d4 > 0.008856451679035631 ? 116.0 * Math.pow(d4, 0.3333333333333333) - 16.0 : d4 * 903.2962962962963;
        dArray2[1] = 13.0 * dArray2[0] * (d2 - d5);
        dArray2[2] = 13.0 * dArray2[0] * (d3 - d6);
        return dArray2;
    }

    public static double[] rgb2luv(double[] dArray, double[] dArray2) {
        dArray = GraphicsUtils.rgb2xyz(dArray, null);
        return GraphicsUtils.xyz2luv(dArray, dArray2);
    }

    public static double[] luv2rgb(double[] dArray, double[] dArray2) {
        dArray = GraphicsUtils.luv2xyz(dArray, null);
        return GraphicsUtils.xyz2rgb(dArray, dArray2);
    }

    public static Color blend(Color color, Color color2, double d) {
        double d2 = MathUtils.limit(d, 0.0, 1.0);
        double d3 = 1.0 - d2;
        int n = (int)Math.round(d3 * (double)color.getRed() + d2 * (double)color2.getRed());
        int n2 = (int)Math.round(d3 * (double)color.getGreen() + d2 * (double)color2.getGreen());
        int n3 = (int)Math.round(d3 * (double)color.getBlue() + d2 * (double)color2.getBlue());
        int n4 = (int)Math.round(d3 * (double)color.getAlpha() + d2 * (double)color2.getAlpha());
        return new Color(n, n2, n3, n4);
    }

    public static Color deriveWithAlpha(Color color, int n) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
    }

    public static Color deriveDarker(Color color) {
        return GraphicsUtils.deriveWithAlpha(GraphicsUtils.blend(color, Color.BLACK, 0.5), color.getAlpha());
    }

    public static Color deriveBrighter(Color color) {
        return GraphicsUtils.deriveWithAlpha(GraphicsUtils.blend(color, Color.WHITE, 0.5), color.getAlpha());
    }
}

