/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io.data;

import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.io.IOCapabilities;
import de.erichseifert.gral.io.data.AbstractDataReader;
import de.erichseifert.gral.util.Messages;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class ImageReader
extends AbstractDataReader {
    public ImageReader(String string) {
        super(string);
        this.setDefault("factor", 1.0);
        this.setDefault("offset", 0.0);
    }

    @Override
    public DataSource read(InputStream object, Class<? extends Comparable<?>> ... classArray) throws IOException {
        object = ImageIO.read((InputStream)object);
        int n = ((BufferedImage)object).getWidth();
        int n2 = ((BufferedImage)object).getHeight();
        Object object2 = new Class[n];
        Arrays.fill((Object[])object2, Double.class);
        object2 = new DataTable((Class<? extends Comparable<?>>)object2);
        double d = ((Number)this.getSetting("factor")).doubleValue();
        double d2 = ((Number)this.getSetting("offset")).doubleValue();
        int[] nArray = new int[n];
        Double[] doubleArray = new Double[n];
        for (int i = 0; i < n2; ++i) {
            ((BufferedImage)object).getRGB(0, i, n, 1, nArray, 0, 0);
            for (int j = 0; j < n; ++j) {
                double d3 = nArray[j] >> 16 & 0xFF;
                doubleArray[j] = d3 * d + d2;
            }
            ((DataTable)object2).add(doubleArray);
        }
        return object2;
    }

    static {
        ImageReader.addCapabilities(new IOCapabilities("BMP", Messages.getString("ImageIO.bmpDescription"), "image/bmp", new String[]{"bmp", "dib"}));
        ImageReader.addCapabilities(new IOCapabilities("GIF", Messages.getString("ImageIO.gifDescription"), "image/gif", new String[]{"gif"}));
        ImageReader.addCapabilities(new IOCapabilities("JPEG/JFIF", Messages.getString("ImageIO.jpegDescription"), "image/jpeg", new String[]{"jpg", "jpeg", "jpe", "jif", "jfif", "jfi"}));
        ImageReader.addCapabilities(new IOCapabilities("PNG", Messages.getString("ImageIO.pngDescription"), "image/png", new String[]{"png"}));
        ImageReader.addCapabilities(new IOCapabilities("WBMP", Messages.getString("ImageIO.wbmpDescription"), "image/vnd.wap.wbmp", new String[]{"wbmp"}));
    }
}

