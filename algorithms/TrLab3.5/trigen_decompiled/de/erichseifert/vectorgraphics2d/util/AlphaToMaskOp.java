/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.util;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

public class AlphaToMaskOp
implements BufferedImageOp {
    private final boolean a;

    public AlphaToMaskOp(boolean bl) {
        this.a = bl;
    }

    public AlphaToMaskOp() {
        this(false);
    }

    public boolean isInverted() {
        return this.a;
    }

    @Override
    public BufferedImage filter(BufferedImage object, BufferedImage bufferedImage) {
        ColorModel colorModel = ((BufferedImage)object).getColorModel();
        if (bufferedImage == null) {
            bufferedImage = this.createCompatibleDestImage((BufferedImage)object, colorModel);
        } else {
            if (bufferedImage.getWidth() != ((BufferedImage)object).getWidth() || bufferedImage.getHeight() != ((BufferedImage)object).getHeight()) {
                throw new IllegalArgumentException("Source and destination images have different dimensions.");
            }
            if (bufferedImage.getColorModel() != colorModel) {
                throw new IllegalArgumentException("Color models don't match.");
            }
        }
        if (colorModel.hasAlpha()) {
            object = ((BufferedImage)object).getRaster();
            WritableRaster writableRaster = bufferedImage.getRaster();
            for (int i = 0; i < ((Raster)object).getHeight(); ++i) {
                for (int j = 0; j < ((Raster)object).getWidth(); ++j) {
                    int n = colorModel.getRGB(((Raster)object).getDataElements(j, i, null));
                    int n2 = n >>> 24;
                    n = n2 >= 127 && !this.isInverted() || n2 < 127 && this.isInverted() ? (n |= 0xFF000000) : (n &= 0xFFFFFF);
                    writableRaster.setDataElements(j, i, colorModel.getDataElements(n, null));
                }
            }
        }
        return bufferedImage;
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage bufferedImage) {
        Rectangle2D.Double double_ = new Rectangle2D.Double();
        ((Rectangle2D)double_).setRect(bufferedImage.getRaster().getBounds());
        return double_;
    }

    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage bufferedImage, ColorModel colorModel) {
        if (colorModel == null) {
            colorModel = bufferedImage.getColorModel();
        }
        WritableRaster writableRaster = colorModel.createCompatibleWritableRaster(bufferedImage.getWidth(), bufferedImage.getHeight());
        boolean bl = colorModel.isAlphaPremultiplied();
        Hashtable<String, Object> hashtable = null;
        if (bufferedImage.getPropertyNames() != null) {
            hashtable = new Hashtable<String, Object>();
            for (String string : bufferedImage.getPropertyNames()) {
                hashtable.put(string, bufferedImage.getProperty(string));
            }
        }
        BufferedImage bufferedImage2 = new BufferedImage(colorModel, writableRaster, bl, hashtable);
        bufferedImage.copyData(writableRaster);
        return bufferedImage2;
    }

    @Override
    public Point2D getPoint2D(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = new Point2D.Double();
        }
        point2D2.setLocation(point2D);
        return point2D2;
    }

    @Override
    public RenderingHints getRenderingHints() {
        return null;
    }
}

