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
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class VectorWriter
extends IOCapabilitiesStorage
implements DrawableWriter {
    private static final Map<String, Class<?>> a;
    private final String b;
    private final Class<? extends Graphics2D> c;

    protected VectorWriter(String string) {
        Class<?> clazz;
        this.b = string;
        try {
            clazz = a.get(string);
        }
        catch (ClassCastException classCastException) {
            clazz = null;
        }
        this.c = clazz;
        if (this.c == null) {
            throw new IllegalArgumentException(MessageFormat.format("Unsupported file format: {0}", string));
        }
    }

    @Override
    public void write(Drawable drawable, OutputStream outputStream, double d, double d2) throws IOException {
        this.write(drawable, outputStream, 0.0, 0.0, d, d2);
    }

    @Override
    public void write(Drawable drawable, OutputStream outputStream, double d, double d2, double d3, double d4) throws IOException {
        try {
            Object object = this.c.getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE);
            object = ((Constructor)object).newInstance(d, d2, d3, d4);
            Rectangle2D rectangle2D = drawable.getBounds();
            drawable.setBounds(d, d2, d3, d4);
            Object object2 = new DrawingContext((Graphics2D)object, DrawingContext.Quality.QUALITY, DrawingContext.Target.VECTOR);
            drawable.draw((DrawingContext)object2);
            object2 = (byte[])this.c.getMethod("getBytes", new Class[0]).invoke(object, new Object[0]);
            outputStream.write((byte[])object2);
            drawable.setBounds(rectangle2D);
            return;
        }
        catch (SecurityException securityException) {
            throw new IllegalStateException(securityException);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new IllegalStateException(noSuchMethodException);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalStateException(illegalArgumentException);
        }
        catch (InstantiationException instantiationException) {
            throw new IllegalStateException(instantiationException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new IllegalStateException(illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new IllegalStateException(invocationTargetException);
        }
    }

    @Override
    public String getMimeType() {
        return this.b;
    }

    static {
        Class<?> clazz;
        a = new HashMap();
        try {
            clazz = Class.forName("de.erichseifert.vectorgraphics2d.EPSGraphics2D");
            VectorWriter.addCapabilities(new IOCapabilities("EPS", Messages.getString("ImageIO.epsDescription"), "application/postscript", new String[]{"eps", "epsf", "epsi"}));
            a.put("application/postscript", clazz);
        }
        catch (ClassNotFoundException classNotFoundException) {}
        try {
            clazz = Class.forName("de.erichseifert.vectorgraphics2d.PDFGraphics2D");
            VectorWriter.addCapabilities(new IOCapabilities("PDF", Messages.getString("ImageIO.pdfDescription"), "application/pdf", new String[]{"pdf"}));
            a.put("application/pdf", clazz);
        }
        catch (ClassNotFoundException classNotFoundException) {}
        try {
            clazz = Class.forName("de.erichseifert.vectorgraphics2d.SVGGraphics2D");
            VectorWriter.addCapabilities(new IOCapabilities("SVG", Messages.getString("ImageIO.svgDescription"), "image/svg+xml", new String[]{"svg", "svgz"}));
            a.put("image/svg+xml", clazz);
        }
        catch (ClassNotFoundException classNotFoundException) {}
    }
}

