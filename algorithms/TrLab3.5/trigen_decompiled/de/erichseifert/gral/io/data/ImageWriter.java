/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io.data;

import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.io.IOCapabilities;
import de.erichseifert.gral.io.data.AbstractDataWriter;
import de.erichseifert.gral.util.MathUtils;
import de.erichseifert.gral.util.Messages;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.imageio.ImageIO;

public class ImageWriter
extends AbstractDataWriter {
    public ImageWriter(String string) {
        super(string);
        this.setDefault("factor", 1.0);
        this.setDefault("offset", 0.0);
    }

    @Override
    public void write(DataSource dataSource, OutputStream outputStream) throws IOException {
        Comparable<?> comparable;
        int n = dataSource.getColumnCount();
        int n2 = dataSource.getRowCount();
        double d = ((Number)this.getSetting("factor")).doubleValue();
        double d2 = ((Number)this.getSetting("offset")).doubleValue();
        byte[] byArray = new byte[n * n2];
        int n3 = 0;
        for (int i = 0; i < n2; ++i) {
            for (int j = 0; j < n; ++j) {
                comparable = dataSource.get(j, i);
                if (!(comparable instanceof Number)) continue;
                comparable = (Number)((Object)comparable);
                double d3 = ((Number)((Object)comparable)).doubleValue() * d + d2;
                byte by = (byte)Math.round(MathUtils.limit(d3, 0.0, 255.0));
                byArray[n3++] = by;
            }
        }
        BufferedImage bufferedImage = new BufferedImage(n, n2, 10);
        bufferedImage.getRaster().setDataElements(0, 0, n, n2, byArray);
        Iterator<javax.imageio.ImageWriter> iterator = ImageIO.getImageWritersByMIMEType(this.getMimeType());
        try {
            comparable = iterator.next();
            ((javax.imageio.ImageWriter)((Object)comparable)).setOutput(ImageIO.createImageOutputStream(outputStream));
            ((javax.imageio.ImageWriter)((Object)comparable)).write(bufferedImage);
            return;
        }
        catch (NoSuchElementException noSuchElementException) {
            throw new IOException(MessageFormat.format("No writer found for MIME type {0}.", this.getMimeType()));
        }
    }

    static {
        ImageWriter.addCapabilities(new IOCapabilities("BMP", Messages.getString("ImageIO.bmpDescription"), "image/bmp", new String[]{"bmp", "dib"}));
        ImageWriter.addCapabilities(new IOCapabilities("GIF", Messages.getString("ImageIO.gifDescription"), "image/gif", new String[]{"gif"}));
        ImageWriter.addCapabilities(new IOCapabilities("JPEG/JFIF", Messages.getString("ImageIO.jpegDescription"), "image/jpeg", new String[]{"jpg", "jpeg", "jpe", "jif", "jfif", "jfi"}));
        ImageWriter.addCapabilities(new IOCapabilities("PNG", Messages.getString("ImageIO.pngDescription"), "image/png", new String[]{"png"}));
        ImageWriter.addCapabilities(new IOCapabilities("WBMP", Messages.getString("ImageIO.wbmpDescription"), "image/vnd.wap.wbmp", new String[]{"wbmp"}));
    }
}

