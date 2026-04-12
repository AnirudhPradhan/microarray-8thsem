/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import org.knowm.xchart.internal.chartpart.Chart;

public final class BitmapEncoder {
    private BitmapEncoder() {
    }

    public static String addFileExtension(String fileName, BitmapFormat bitmapFormat) {
        String fileNameWithFileExtension = fileName;
        String newFileExtension = "." + bitmapFormat.toString().toLowerCase();
        if (fileName.length() <= newFileExtension.length() || !fileName.substring(fileName.length() - newFileExtension.length(), fileName.length()).equalsIgnoreCase(newFileExtension)) {
            fileNameWithFileExtension = fileName + newFileExtension;
        }
        return fileNameWithFileExtension;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void saveBitmap(Chart chart, String fileName, BitmapFormat bitmapFormat) throws IOException {
        BufferedImage bufferedImage = BitmapEncoder.getBufferedImage(chart);
        FileOutputStream out = new FileOutputStream(BitmapEncoder.addFileExtension(fileName, bitmapFormat));
        try {
            ImageIO.write((RenderedImage)bufferedImage, bitmapFormat.toString().toLowerCase(), out);
        }
        finally {
            ((OutputStream)out).close();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void saveBitmapWithDPI(Chart chart, String fileName, BitmapFormat bitmapFormat, int DPI) throws IOException {
        double scaleFactor = (double)DPI / 72.0;
        BufferedImage bufferedImage = new BufferedImage((int)((double)chart.getWidth() * scaleFactor), (int)((double)chart.getHeight() * scaleFactor), 1);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        AffineTransform at = graphics2D.getTransform();
        at.scale(scaleFactor, scaleFactor);
        graphics2D.setTransform(at);
        chart.paint(graphics2D, chart.getWidth(), chart.getHeight());
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(bitmapFormat.toString().toLowerCase());
        if (writers.hasNext()) {
            ImageWriter writer = writers.next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(1);
            IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, iwp);
            if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
                throw new IllegalArgumentException("It is not possible to set the DPI on a bitmap with " + (Object)((Object)bitmapFormat) + " format!! Try another format.");
            }
            BitmapEncoder.setDPI(metadata, DPI);
            File file = new File(BitmapEncoder.addFileExtension(fileName, bitmapFormat));
            FileImageOutputStream output = new FileImageOutputStream(file);
            writer.setOutput(output);
            IIOImage image = new IIOImage(bufferedImage, null, metadata);
            try {
                writer.write(null, image, iwp);
                writer.dispose();
            }
            finally {
                output.close();
            }
        }
    }

    private static void setDPI(IIOMetadata metadata, int DPI) throws IIOInvalidTreeException {
        double dotsPerMilli = 1.0 * (double)DPI / 10.0 / 2.54;
        IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
        horiz.setAttribute("value", Double.toString(dotsPerMilli));
        IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
        vert.setAttribute("value", Double.toString(dotsPerMilli));
        IIOMetadataNode dim = new IIOMetadataNode("Dimension");
        dim.appendChild(horiz);
        dim.appendChild(vert);
        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
        root.appendChild(dim);
        metadata.mergeTree("javax_imageio_1.0", root);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void saveJPGWithQuality(Chart chart, String fileName, float quality) throws FileNotFoundException, IOException {
        BufferedImage bufferedImage = BitmapEncoder.getBufferedImage(chart);
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter writer = iter.next();
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(2);
        iwp.setCompressionQuality(quality);
        File file = new File(fileName);
        FileImageOutputStream output = new FileImageOutputStream(file);
        try {
            writer.setOutput(output);
            IIOImage image = new IIOImage(bufferedImage, null, null);
            writer.write(null, image, iwp);
            writer.dispose();
        }
        finally {
            output.close();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] getBitmapBytes(Chart chart, BitmapFormat bitmapFormat) throws IOException {
        BufferedImage bufferedImage = BitmapEncoder.getBufferedImage(chart);
        byte[] imageInBytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage)bufferedImage, bitmapFormat.toString().toLowerCase(), baos);
        try {
            baos.flush();
            imageInBytes = baos.toByteArray();
        }
        finally {
            baos.close();
        }
        return imageInBytes;
    }

    public static BufferedImage getBufferedImage(Chart chart) {
        BufferedImage bufferedImage = new BufferedImage(chart.getWidth(), chart.getHeight(), 1);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        chart.paint(graphics2D);
        return bufferedImage;
    }

    public static enum BitmapFormat {
        PNG,
        JPG,
        BMP,
        GIF;

    }
}

