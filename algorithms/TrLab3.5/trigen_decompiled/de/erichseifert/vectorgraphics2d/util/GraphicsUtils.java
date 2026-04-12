/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.util;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.color.ColorSpace;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.PriorityQueue;
import javax.swing.ImageIcon;

public abstract class GraphicsUtils {
    private static final FontRenderContext a = new FontRenderContext(null, false, true);
    private static final a b = new a(0);

    protected GraphicsUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean hasAlpha(Image object) {
        if (object instanceof BufferedImage) {
            object = (BufferedImage)object;
            object = ((BufferedImage)object).getColorModel();
        } else {
            object = new PixelGrabber((Image)object, 0, 0, 1, 1, false);
            try {
                ((PixelGrabber)object).grabPixels();
            }
            catch (InterruptedException interruptedException) {
                return false;
            }
            object = ((PixelGrabber)object).getColorModel();
        }
        return ((ColorModel)object).hasAlpha();
    }

    public static boolean usesAlpha(Image object) {
        if (object == null) {
            return false;
        }
        object = GraphicsUtils.toBufferedImage((Image)object);
        if ((object = ((BufferedImage)object).getAlphaRaster()) == null) {
            return false;
        }
        object = ((Raster)object).getDataBuffer();
        for (int i = 0; i < ((DataBuffer)object).getSize(); ++i) {
            int n = ((DataBuffer)object).getElem(i);
            if (n >= 255) continue;
            return true;
        }
        return false;
    }

    public static BufferedImage toBufferedImage(RenderedImage renderedImage) {
        if (renderedImage instanceof BufferedImage) {
            return (BufferedImage)renderedImage;
        }
        ColorModel colorModel = renderedImage.getColorModel();
        WritableRaster writableRaster = colorModel.createCompatibleWritableRaster(renderedImage.getWidth(), renderedImage.getHeight());
        boolean bl = colorModel.isAlphaPremultiplied();
        Hashtable<String, Object> hashtable = null;
        if (renderedImage.getPropertyNames() != null) {
            hashtable = new Hashtable<String, Object>();
            for (String string : renderedImage.getPropertyNames()) {
                hashtable.put(string, renderedImage.getProperty(string));
            }
        }
        BufferedImage bufferedImage = new BufferedImage(colorModel, writableRaster, bl, hashtable);
        renderedImage.copyData(writableRaster);
        return bufferedImage;
    }

    public static BufferedImage toBufferedImage(Image image) {
        int n;
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
        image = new ImageIcon(image).getImage();
        boolean bl = GraphicsUtils.hasAlpha(image);
        Object object = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            n = 1;
            if (bl) {
                n = 3;
            }
            object = ((GraphicsEnvironment)object).getDefaultScreenDevice();
            object = ((GraphicsDevice)object).getDefaultConfiguration();
            object = ((GraphicsConfiguration)object).createCompatibleImage(image.getWidth(null), image.getHeight(null), n);
        }
        catch (HeadlessException headlessException) {
            object = null;
        }
        if (object == null) {
            n = 1;
            if (bl) {
                n = 2;
            }
            object = new BufferedImage(image.getWidth(null), image.getHeight(null), n);
        }
        Graphics2D graphics2D = ((BufferedImage)object).createGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();
        return object;
    }

    public static Shape clone(Shape shape) {
        Shape shape2;
        if (shape == null) {
            return null;
        }
        if (shape instanceof Line2D) {
            shape2 = shape instanceof Line2D.Float ? new Line2D.Float() : new Line2D.Double();
            ((Line2D)shape2).setLine((Line2D)shape);
        } else if (shape instanceof Rectangle) {
            shape2 = new Rectangle((Rectangle)shape);
        } else if (shape instanceof Rectangle2D) {
            shape2 = shape instanceof Rectangle2D.Float ? new Rectangle2D.Float() : new Rectangle2D.Double();
            ((Rectangle2D)shape2).setRect((Rectangle2D)shape);
        } else if (shape instanceof RoundRectangle2D) {
            shape2 = shape instanceof RoundRectangle2D.Float ? new RoundRectangle2D.Float() : new RoundRectangle2D.Double();
            ((RoundRectangle2D)shape2).setRoundRect((RoundRectangle2D)shape);
        } else if (shape instanceof Ellipse2D) {
            shape2 = shape instanceof Ellipse2D.Float ? new Ellipse2D.Float() : new Ellipse2D.Double();
            ((Ellipse2D)shape2).setFrame(((Ellipse2D)shape).getFrame());
        } else if (shape instanceof Arc2D) {
            shape2 = shape instanceof Arc2D.Float ? new Arc2D.Float() : new Arc2D.Double();
            ((Arc2D)shape2).setArc((Arc2D)shape);
        } else if (shape instanceof Polygon) {
            shape = (Polygon)shape;
            shape2 = new Polygon(((Polygon)shape).xpoints, ((Polygon)shape).ypoints, ((Polygon)shape).npoints);
        } else if (shape instanceof CubicCurve2D) {
            shape2 = shape instanceof CubicCurve2D.Float ? new CubicCurve2D.Float() : new CubicCurve2D.Double();
            ((CubicCurve2D)shape2).setCurve((CubicCurve2D)shape);
        } else if (shape instanceof QuadCurve2D) {
            shape2 = shape instanceof QuadCurve2D.Float ? new QuadCurve2D.Float() : new QuadCurve2D.Double();
            ((QuadCurve2D)shape2).setCurve((QuadCurve2D)shape);
        } else {
            shape2 = shape instanceof Path2D.Float ? new Path2D.Float(shape) : new Path2D.Double(shape);
        }
        return shape2;
    }

    private static boolean a(String string) {
        return "Dialog".equals(string) || "DialogInput".equals(string) || "SansSerif".equals(string) || "Serif".equals(string) || "Monospaced".equals(string);
    }

    public static Font getPhysicalFont(Font font, String string) {
        Object object = font.getFamily();
        if (!GraphicsUtils.a((String)object)) {
            return font;
        }
        object = new TextLayout(string, font, a);
        PriorityQueue<Font> priorityQueue = new PriorityQueue<Font>(1, b);
        Font[] fontArray = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        Font[] fontArray2 = fontArray;
        fontArray2 = fontArray;
        int n = fontArray.length;
        for (int i = 0; i < n; ++i) {
            Font font2 = fontArray2[i];
            Object object2 = font2.getFamily();
            if (GraphicsUtils.a((String)object2) || !((TextLayout)(object2 = new TextLayout(string, font2 = font2.deriveFont(font.getStyle(), font.getSize2D()), a))).getBounds().equals(((TextLayout)object).getBounds()) || ((TextLayout)object2).getAscent() != ((TextLayout)object).getAscent() || ((TextLayout)object2).getDescent() != ((TextLayout)object).getDescent() || ((TextLayout)object2).getLeading() != ((TextLayout)object).getLeading() || ((TextLayout)object2).getAdvance() != ((TextLayout)object).getAdvance() || ((TextLayout)object2).getVisibleAdvance() != ((TextLayout)object).getVisibleAdvance()) continue;
            priorityQueue.add(font2);
        }
        if (priorityQueue.isEmpty()) {
            return font;
        }
        return (Font)priorityQueue.poll();
    }

    public static Font getPhysicalFont(Font font) {
        return GraphicsUtils.getPhysicalFont(font, "Falsches \u00dcben von Xylophonmusik qu\u00e4lt jeden gr\u00f6\u00dferen Zwerg");
    }

    public static BufferedImage getAlphaImage(BufferedImage bufferedImage) {
        WritableRaster writableRaster = bufferedImage.getAlphaRaster();
        int n = bufferedImage.getWidth();
        int n2 = bufferedImage.getHeight();
        Object object = ColorSpace.getInstance(1003);
        int[] nArray = new int[]{8};
        object = new ComponentColorModel((ColorSpace)object, nArray, false, true, 1, 0);
        Object object2 = ((ColorModel)object).createCompatibleWritableRaster(n, n2);
        object2 = new BufferedImage((ColorModel)object, (WritableRaster)object2, false, null);
        int[] nArray2 = new int[bufferedImage.getWidth() * writableRaster.getNumBands()];
        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            writableRaster.getPixels(0, i, bufferedImage.getWidth(), 1, nArray2);
            if (bufferedImage.getTransparency() == 2) {
                for (int j = 0; j < nArray2.length; ++j) {
                    if (nArray2[j] <= 0) continue;
                    nArray2[j] = 255;
                }
            }
            ((BufferedImage)object2).getRaster().setPixels(0, i, bufferedImage.getWidth(), 1, nArray2);
        }
        return object2;
    }

    public static boolean equals(Shape object, Shape object2) {
        object = object.getPathIterator(null);
        object2 = object2.getPathIterator(null);
        if (object.getWindingRule() != object2.getWindingRule()) {
            return false;
        }
        double[] dArray = new double[6];
        double[] dArray2 = new double[6];
        while (!object.isDone()) {
            int n;
            int n2 = object.currentSegment(dArray);
            if (n2 != (n = object2.currentSegment(dArray2))) {
                return false;
            }
            for (n2 = 0; n2 < 6; ++n2) {
                if (dArray[n2] == dArray2[n2]) continue;
                return false;
            }
            object.next();
            object2.next();
        }
        return object2.isDone();
    }

    private static final class a
    implements Comparator<Font> {
        private static final int[] a = new int[]{0, 2, 1, 3};

        private a() {
        }

        @Override
        public final /* synthetic */ int compare(Object object, Object object2) {
            if ((object = (Font)object) == (object2 = (Font)object2)) {
                return 0;
            }
            HashSet<String> hashSet = new HashSet<String>();
            HashSet<String> hashSet2 = new HashSet<String>();
            int[] nArray = a;
            for (int i = 0; i < 4; ++i) {
                int n = nArray[i];
                hashSet.add(((Font)object).deriveFont(n).getPSName());
                hashSet2.add(((Font)object2).deriveFont(n).getPSName());
            }
            if (hashSet.size() < hashSet2.size()) {
                return 1;
            }
            if (hashSet.size() > hashSet2.size()) {
                return -1;
            }
            return ((Font)object).getName().compareTo(((Font)object2).getName());
        }

        /* synthetic */ a(byte by) {
            this();
        }
    }
}

