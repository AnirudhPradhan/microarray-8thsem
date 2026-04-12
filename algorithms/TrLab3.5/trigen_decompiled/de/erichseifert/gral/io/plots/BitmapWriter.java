/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io.plots;

import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.io.IOCapabilities;
import de.erichseifert.gral.io.IOCapabilitiesStorage;
import de.erichseifert.gral.io.plots.DrawableWriter;
import de.erichseifert.gral.util.Messages;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

public class BitmapWriter
extends IOCapabilitiesStorage
implements DrawableWriter {
    private final String a;
    private final int b;

    protected BitmapWriter(String string) {
        boolean bl;
        this.a = string;
        boolean bl2 = "image/png".equals(string);
        boolean bl3 = !"image/vnd.wap.wbmp".equals(string);
        boolean bl4 = bl = !"image/vnd.wap.wbmp".equals(string);
        if (bl3) {
            if (bl2) {
                this.b = 2;
                return;
            }
            this.b = 1;
            return;
        }
        if (bl) {
            this.b = 10;
            return;
        }
        this.b = 12;
    }

    @Override
    public void write(Drawable drawable, OutputStream outputStream, double d, double d2) throws IOException {
        this.write(drawable, outputStream, 0.0, 0.0, d, d2);
    }

    @Override
    public void write(Drawable drawable, OutputStream closeable, double d, double d2, double d3, double d4) throws IOException {
        BufferedImage bufferedImage = new BufferedImage((int)Math.ceil(d3), (int)Math.ceil(d4), this.b);
        Object object = bufferedImage.createGraphics();
        ((Graphics2D)object).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D)object).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        ((Graphics2D)object).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        ((Graphics2D)object).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        object = new DrawingContext((Graphics2D)object);
        Object object2 = ImageIO.getImageWritersByMIMEType(this.getMimeType());
        if (object2.hasNext()) {
            object2 = object2.next();
            closeable = ImageIO.createImageOutputStream(closeable);
            ((ImageWriter)object2).setOutput(closeable);
            Rectangle2D rectangle2D = drawable.getBounds();
            drawable.setBounds(d, d2, d3, d4);
            try {
                drawable.draw((DrawingContext)object);
                ((ImageWriter)object2).write(bufferedImage);
                return;
            }
            finally {
                drawable.setBounds(rectangle2D);
                closeable.close();
            }
        }
    }

    @Override
    public String getMimeType() {
        return this.a;
    }

    static {
        BitmapWriter.addCapabilities(new IOCapabilities("BMP", Messages.getString("ImageIO.bmpDescription"), "image/bmp", new String[]{"bmp", "dib"}));
        BitmapWriter.addCapabilities(new IOCapabilities("GIF", Messages.getString("ImageIO.gifDescription"), "image/gif", new String[]{"gif"}));
        BitmapWriter.addCapabilities(new IOCapabilities("JPEG/JFIF", Messages.getString("ImageIO.jpegDescription"), "image/jpeg", new String[]{"jpg", "jpeg", "jpe", "jif", "jfif", "jfi"}));
        BitmapWriter.addCapabilities(new IOCapabilities("PNG", Messages.getString("ImageIO.pngDescription"), "image/png", new String[]{"png"}));
        BitmapWriter.addCapabilities(new IOCapabilities("WBMP", Messages.getString("ImageIO.wbmpDescription"), "image/vnd.wap.wbmp", new String[]{"wbmp"}));
    }
}

