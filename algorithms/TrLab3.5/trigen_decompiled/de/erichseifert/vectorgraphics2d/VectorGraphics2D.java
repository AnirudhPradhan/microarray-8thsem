/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d;

import de.erichseifert.vectorgraphics2d.GraphicsState;
import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import de.erichseifert.vectorgraphics2d.intermediate.commands.CreateCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DisposeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DrawImageCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DrawShapeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DrawStringCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.FillShapeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.RotateCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.ScaleCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetBackgroundCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetClipCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetColorCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetCompositeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetFontCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetHintCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetPaintCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetStrokeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetTransformCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetXORModeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.ShearCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.TransformCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.TranslateCommand;
import de.erichseifert.vectorgraphics2d.util.GraphicsUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VectorGraphics2D
extends Graphics2D
implements Cloneable {
    private final List<Command<?>> a = new LinkedList();
    private final GraphicsConfiguration b;
    private final FontRenderContext c;
    private boolean d;
    private GraphicsState e;
    private Graphics2D f;

    public VectorGraphics2D() {
        this.a(new CreateCommand(this));
        Object object = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (!((GraphicsEnvironment)object).isHeadlessInstance()) {
            object = ((GraphicsEnvironment)object).getDefaultScreenDevice();
            this.b = ((GraphicsDevice)object).getDefaultConfiguration();
        } else {
            this.b = null;
        }
        this.c = new FontRenderContext(null, false, true);
        this.e = new GraphicsState();
        object = new BufferedImage(200, 250, 2);
        this.f = (Graphics2D)((BufferedImage)object).getGraphics();
        this.f.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public Object clone() throws CloneNotSupportedException {
        try {
            VectorGraphics2D vectorGraphics2D = (VectorGraphics2D)super.clone();
            ((VectorGraphics2D)super.clone()).e = (GraphicsState)this.e.clone();
            return vectorGraphics2D;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    @Override
    public void addRenderingHints(Map<?, ?> object) {
        if (this.isDisposed()) {
            return;
        }
        for (Map.Entry entry : object.entrySet()) {
            this.setRenderingHint((RenderingHints.Key)entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clip(Shape shape) {
        this.f.clip(shape);
        Shape shape2 = this.getClip();
        Shape shape3 = this.getClip();
        if (shape3 != null && shape != null) {
            Shape shape4;
            Shape shape5 = shape3;
            shape3 = shape;
            shape = shape5;
            if (shape5 instanceof Rectangle2D && shape3 instanceof Rectangle2D) {
                shape = (Rectangle2D)shape;
                shape3 = (Rectangle2D)shape3;
                double d = Math.max(((RectangularShape)shape).getMinX(), ((RectangularShape)shape3).getMinX());
                double d2 = Math.max(((RectangularShape)shape).getMinY(), ((RectangularShape)shape3).getMinY());
                double d3 = Math.min(((RectangularShape)shape).getMaxX(), ((RectangularShape)shape3).getMaxX());
                double d4 = Math.min(((RectangularShape)shape).getMaxY(), ((RectangularShape)shape3).getMaxY());
                shape = new Rectangle2D.Double();
                if (d3 < d || d4 < d2) {
                    ((RectangularShape)shape).setFrameFromDiagonal(0.0, 0.0, 0.0, 0.0);
                } else {
                    ((RectangularShape)shape).setFrameFromDiagonal(d, d2, d3, d4);
                }
                shape4 = shape;
            } else {
                shape = new Area(shape);
                ((Area)shape).intersect(new Area(shape3));
                shape4 = shape;
            }
            shape = shape4;
        }
        this.setClip(shape);
        shape3 = this.getClip();
        if ((shape3 == null || this.f.getClip() == null) && shape3 != this.f.getClip()) {
            System.err.println("clip() validation failed: clip(" + shape2 + ", " + shape + ") => " + shape3 + " != " + this.f.getClip());
        }
        if (shape3 != null && !GraphicsUtils.equals(shape3, this.f.getClip())) {
            System.err.println("clip() validation failed: clip(" + shape2 + ", " + shape + ") => " + shape3 + " != " + this.f.getClip());
        }
    }

    @Override
    public void draw(Shape shape) {
        if (this.isDisposed() || shape == null) {
            return;
        }
        this.a(new DrawShapeCommand(shape));
        this.f.draw(shape);
    }

    @Override
    public void drawGlyphVector(GlyphVector object, float f, float f2) {
        object = ((GlyphVector)object).getOutline(f, f2);
        this.draw((Shape)object);
    }

    @Override
    public boolean drawImage(Image object, AffineTransform object2, ImageObserver object3) {
        object3 = object2;
        object2 = object;
        object = this;
        object = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR.equals(object = (Integer)((VectorGraphics2D)object).getRenderingHint(RenderingHints.KEY_INTERPOLATION)) ? Integer.valueOf(1) : (RenderingHints.VALUE_INTERPOLATION_BILINEAR.equals(object) ? Integer.valueOf(2) : Integer.valueOf(3));
        object = new AffineTransformOp((AffineTransform)object3, (Integer)object);
        object2 = GraphicsUtils.toBufferedImage((Image)object2);
        object = ((AffineTransformOp)object).filter((BufferedImage)object2, null);
        return this.drawImage((Image)object, ((BufferedImage)object).getMinX(), ((BufferedImage)object).getMinY(), ((BufferedImage)object).getWidth(), ((BufferedImage)object).getHeight(), null, null);
    }

    @Override
    public void drawImage(BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, int n, int n2) {
        if (bufferedImageOp != null) {
            bufferedImage = bufferedImageOp.filter(bufferedImage, null);
        }
        this.drawImage(bufferedImage, n, n2, bufferedImage.getWidth(), bufferedImage.getHeight(), null, null);
    }

    @Override
    public void drawRenderableImage(RenderableImage renderableImage, AffineTransform affineTransform) {
        this.drawRenderedImage(renderableImage.createDefaultRendering(), affineTransform);
    }

    @Override
    public void drawRenderedImage(RenderedImage renderedImage, AffineTransform affineTransform) {
        renderedImage = GraphicsUtils.toBufferedImage(renderedImage);
        this.drawImage((Image)((Object)renderedImage), affineTransform, null);
    }

    @Override
    public void drawString(String string, int n, int n2) {
        this.drawString(string, (float)n, (float)n2);
    }

    @Override
    public void drawString(String string, float f, float f2) {
        if (this.isDisposed() || string == null || string.trim().length() == 0) {
            return;
        }
        this.a(new DrawStringCommand(string, f, f2));
        this.f.drawString(string, f, f2);
    }

    @Override
    public void drawString(AttributedCharacterIterator attributedCharacterIterator, int n, int n2) {
        this.drawString(attributedCharacterIterator, (float)n, (float)n2);
    }

    @Override
    public void drawString(AttributedCharacterIterator attributedCharacterIterator, float f, float f2) {
        StringBuilder stringBuilder = new StringBuilder();
        char c2 = attributedCharacterIterator.first();
        while (c2 != '\uffff') {
            stringBuilder.append(c2);
            c2 = attributedCharacterIterator.next();
        }
        this.drawString(stringBuilder.toString(), f, f2);
    }

    @Override
    public void fill(Shape shape) {
        if (this.isDisposed() || shape == null) {
            return;
        }
        this.a(new FillShapeCommand(shape));
        this.f.fill(shape);
    }

    @Override
    public Color getBackground() {
        return this.e.getBackground();
    }

    @Override
    public Composite getComposite() {
        return this.e.getComposite();
    }

    @Override
    public GraphicsConfiguration getDeviceConfiguration() {
        return this.b;
    }

    @Override
    public FontRenderContext getFontRenderContext() {
        return this.c;
    }

    @Override
    public Paint getPaint() {
        return this.e.getPaint();
    }

    @Override
    public Object getRenderingHint(RenderingHints.Key key) {
        if (RenderingHints.KEY_ANTIALIASING.equals(key)) {
            return RenderingHints.VALUE_ANTIALIAS_OFF;
        }
        if (RenderingHints.KEY_TEXT_ANTIALIASING.equals(key)) {
            return RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
        }
        if (RenderingHints.KEY_FRACTIONALMETRICS.equals(key)) {
            return RenderingHints.VALUE_FRACTIONALMETRICS_ON;
        }
        return this.e.getHints().get(key);
    }

    @Override
    public RenderingHints getRenderingHints() {
        return (RenderingHints)this.e.getHints().clone();
    }

    @Override
    public Stroke getStroke() {
        return this.e.getStroke();
    }

    @Override
    public boolean hit(Rectangle rectangle, Shape shape, boolean bl) {
        boolean bl2;
        boolean bl3;
        Shape shape2 = shape;
        if (bl) {
            shape2 = this.getStroke().createStrokedShape(shape2);
        }
        if ((bl3 = (shape2 = this.e.transformShape(shape2)).intersects(rectangle)) != (bl2 = this.f.hit(rectangle, shape, bl))) {
            System.err.println("setClip() validation failed");
        }
        return bl3;
    }

    @Override
    public void setBackground(Color color) {
        if (this.isDisposed() || color == null || this.getColor().equals(color)) {
            return;
        }
        this.a(new SetBackgroundCommand(color));
        this.e.setBackground(color);
        this.f.setBackground(color);
        if (!this.getBackground().equals(this.f.getBackground())) {
            System.err.println("setBackground() validation failed");
        }
    }

    @Override
    public void setComposite(Composite composite) {
        if (this.isDisposed()) {
            return;
        }
        if (composite == null) {
            throw new IllegalArgumentException("Cannot set a null composite.");
        }
        this.a(new SetCompositeCommand(composite));
        this.e.setComposite(composite);
        this.f.setComposite(composite);
        if (!this.getComposite().equals(this.f.getComposite())) {
            System.err.println("setComposite() validation failed");
        }
    }

    @Override
    public void setPaint(Paint paint) {
        if (this.isDisposed() || paint == null) {
            return;
        }
        if (paint instanceof Color) {
            this.setColor((Color)paint);
            return;
        }
        if (this.getPaint().equals(paint)) {
            return;
        }
        this.a(new SetPaintCommand(paint));
        this.e.setPaint(paint);
        this.f.setPaint(paint);
        if (!this.getPaint().equals(this.f.getPaint())) {
            System.err.println("setPaint() validation failed");
        }
    }

    @Override
    public void setRenderingHint(RenderingHints.Key key, Object object) {
        if (this.isDisposed()) {
            return;
        }
        this.e.getHints().put(key, object);
        this.a(new SetHintCommand(key, object));
    }

    @Override
    public void setRenderingHints(Map<?, ?> object) {
        if (this.isDisposed()) {
            return;
        }
        this.e.getHints().clear();
        for (Map.Entry entry : object.entrySet()) {
            this.setRenderingHint((RenderingHints.Key)entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void setStroke(Stroke stroke) {
        if (this.isDisposed()) {
            return;
        }
        if (stroke == null) {
            throw new IllegalArgumentException("Cannot set a null stroke.");
        }
        this.a(new SetStrokeCommand(stroke));
        this.e.setStroke(stroke);
        this.f.setStroke(stroke);
        if (!this.getStroke().equals(this.f.getStroke())) {
            System.err.println("setStroke() validation failed");
        }
    }

    @Override
    public AffineTransform getTransform() {
        return new AffineTransform(this.e.getTransform());
    }

    @Override
    public void setTransform(AffineTransform affineTransform) {
        if (this.isDisposed() || affineTransform == null || this.e.getTransform().equals(affineTransform)) {
            return;
        }
        this.a(new SetTransformCommand(affineTransform));
        this.e.setTransform(affineTransform);
        this.f.setTransform(affineTransform);
        if (!this.getTransform().equals(this.f.getTransform())) {
            System.err.println("setTransform() validation failed");
        }
    }

    @Override
    public void shear(double d, double d2) {
        if (d == 0.0 && d2 == 0.0) {
            return;
        }
        AffineTransform affineTransform = this.getTransform();
        affineTransform.shear(d, d2);
        this.a(new ShearCommand(d, d2));
        this.e.setTransform(affineTransform);
        this.f.shear(d, d2);
        if (!this.getTransform().equals(this.f.getTransform())) {
            System.err.println("shear() validation failed");
        }
    }

    @Override
    public void transform(AffineTransform affineTransform) {
        if (affineTransform.isIdentity()) {
            return;
        }
        AffineTransform affineTransform2 = this.getTransform();
        affineTransform2.concatenate(affineTransform);
        this.a(new TransformCommand(affineTransform));
        this.e.setTransform(affineTransform2);
        this.f.transform(affineTransform);
        if (!this.getTransform().equals(this.f.getTransform())) {
            System.err.println("transform() validation failed");
        }
    }

    @Override
    public void translate(int n, int n2) {
        this.translate((double)n, (double)n2);
    }

    @Override
    public void translate(double d, double d2) {
        if (d == 0.0 && d2 == 0.0) {
            return;
        }
        AffineTransform affineTransform = this.getTransform();
        affineTransform.translate(d, d2);
        this.a(new TranslateCommand(d, d2));
        this.e.setTransform(affineTransform);
        this.f.translate(d, d2);
        if (!this.getTransform().equals(this.f.getTransform())) {
            System.err.println("translate() validation failed");
        }
    }

    @Override
    public void rotate(double d) {
        this.rotate(d, 0.0, 0.0);
    }

    @Override
    public void rotate(double d, double d2, double d3) {
        if (d == 0.0) {
            return;
        }
        AffineTransform affineTransform = this.getTransform();
        if (d2 == 0.0 && d3 == 0.0) {
            affineTransform.rotate(d);
        } else {
            affineTransform.rotate(d, d2, d3);
        }
        this.a(new RotateCommand(d, d2, d3));
        this.e.setTransform(affineTransform);
        if (d2 == 0.0 && d3 == 0.0) {
            this.f.rotate(d);
            if (!this.getTransform().equals(this.f.getTransform())) {
                System.err.println("rotate(theta) validation failed");
                return;
            }
        } else {
            this.f.rotate(d, d2, d3);
            if (!this.getTransform().equals(this.f.getTransform())) {
                System.err.println("rotate(theta,x,y) validation failed");
            }
        }
    }

    @Override
    public void scale(double d, double d2) {
        if (d == 1.0 && d2 == 1.0) {
            return;
        }
        AffineTransform affineTransform = this.getTransform();
        affineTransform.scale(d, d2);
        this.a(new ScaleCommand(d, d2));
        this.e.setTransform(affineTransform);
        this.f.scale(d, d2);
        if (!this.getTransform().equals(this.f.getTransform())) {
            System.err.println("scale() validation failed");
        }
    }

    @Override
    public void clearRect(int n, int n2, int n3, int n4) {
        Color color = this.getColor();
        this.setColor(this.getBackground());
        this.fillRect(n, n2, n3, n4);
        this.setColor(color);
    }

    @Override
    public void clipRect(int n, int n2, int n3, int n4) {
        this.clip(new Rectangle(n, n2, n3, n4));
    }

    @Override
    public void copyArea(int n, int n2, int n3, int n4, int n5, int n6) {
    }

    @Override
    public Graphics create() {
        if (this.isDisposed()) {
            return null;
        }
        VectorGraphics2D vectorGraphics2D = null;
        try {
            vectorGraphics2D = (VectorGraphics2D)this.clone();
            this.a(new CreateCommand(vectorGraphics2D));
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            CloneNotSupportedException cloneNotSupportedException2 = cloneNotSupportedException;
            cloneNotSupportedException.printStackTrace();
        }
        if (vectorGraphics2D != null) {
            vectorGraphics2D.f = (Graphics2D)this.f.create();
        }
        return vectorGraphics2D;
    }

    @Override
    public void dispose() {
        if (this.isDisposed()) {
            return;
        }
        this.a(new DisposeCommand(this));
        this.d = true;
        this.f.dispose();
    }

    @Override
    public void drawArc(int n, int n2, int n3, int n4, int n5, int n6) {
        this.draw(new Arc2D.Double(n, n2, n3, n4, n5, n6, 0));
    }

    @Override
    public boolean drawImage(Image image, int n, int n2, ImageObserver imageObserver) {
        return this.drawImage(image, n, n2, image.getWidth(imageObserver), image.getHeight(imageObserver), null, imageObserver);
    }

    @Override
    public boolean drawImage(Image image, int n, int n2, Color color, ImageObserver imageObserver) {
        return this.drawImage(image, n, n2, image.getWidth(imageObserver), image.getHeight(imageObserver), color, imageObserver);
    }

    @Override
    public boolean drawImage(Image image, int n, int n2, int n3, int n4, ImageObserver imageObserver) {
        return this.drawImage(image, n, n2, n3, n4, null, imageObserver);
    }

    @Override
    public boolean drawImage(Image image, int n, int n2, int n3, int n4, Color color, ImageObserver imageObserver) {
        if (this.isDisposed() || image == null) {
            return true;
        }
        int n5 = image.getWidth(imageObserver);
        int n6 = image.getHeight(imageObserver);
        Rectangle rectangle = new Rectangle(n, n2, n3, n4);
        if (color != null) {
            Color color2 = this.getColor();
            this.setColor(color);
            this.fill(rectangle);
            this.setColor(color2);
        }
        this.a(new DrawImageCommand(image, n5, n6, n, n2, n3, n4));
        this.f.drawImage(image, n, n2, n3, n4, color, imageObserver);
        return true;
    }

    @Override
    public boolean drawImage(Image image, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, ImageObserver imageObserver) {
        return this.drawImage(image, n, n2, n3, n4, n5, n6, n7, n8, null, imageObserver);
    }

    @Override
    public boolean drawImage(Image image, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, Color color, ImageObserver imageObserver) {
        if (image == null) {
            return true;
        }
        int n9 = Math.min(n5, n7);
        int n10 = Math.min(n6, n8);
        n5 = Math.abs(n7 - n5);
        n6 = Math.abs(n8 - n6);
        n7 = Math.min(n, n3);
        n8 = Math.min(n2, n4);
        n = Math.abs(n3 - n);
        n2 = Math.abs(n4 - n2);
        image = GraphicsUtils.toBufferedImage(image);
        image = ((BufferedImage)image).getSubimage(n9, n10, n5, n6);
        return this.drawImage(image, n7, n8, n, n2, color, imageObserver);
    }

    @Override
    public void drawLine(int n, int n2, int n3, int n4) {
        this.draw(new Line2D.Double(n, n2, n3, n4));
    }

    @Override
    public void drawOval(int n, int n2, int n3, int n4) {
        this.draw(new Ellipse2D.Double(n, n2, n3, n4));
    }

    @Override
    public void drawPolygon(Polygon polygon) {
        this.draw(polygon);
    }

    @Override
    public void drawPolygon(int[] nArray, int[] nArray2, int n) {
        this.draw(new Polygon(nArray, nArray2, n));
    }

    @Override
    public void drawPolyline(int[] nArray, int[] nArray2, int n) {
        Path2D.Float float_ = new Path2D.Float();
        for (int i = 0; i < n; ++i) {
            if (i > 0) {
                ((Path2D)float_).lineTo(nArray[i], nArray2[i]);
                continue;
            }
            ((Path2D)float_).moveTo(nArray[i], nArray2[i]);
        }
        this.draw(float_);
    }

    @Override
    public void drawRect(int n, int n2, int n3, int n4) {
        this.draw(new Rectangle(n, n2, n3, n4));
    }

    @Override
    public void drawRoundRect(int n, int n2, int n3, int n4, int n5, int n6) {
        this.draw(new RoundRectangle2D.Double(n, n2, n3, n4, n5, n6));
    }

    @Override
    public void fillArc(int n, int n2, int n3, int n4, int n5, int n6) {
        this.fill(new Arc2D.Double(n, n2, n3, n4, n5, n6, 2));
    }

    @Override
    public void fillOval(int n, int n2, int n3, int n4) {
        this.fill(new Ellipse2D.Double(n, n2, n3, n4));
    }

    @Override
    public void fillPolygon(Polygon polygon) {
        this.fill(polygon);
    }

    @Override
    public void fillPolygon(int[] nArray, int[] nArray2, int n) {
        this.fill(new Polygon(nArray, nArray2, n));
    }

    @Override
    public void fillRect(int n, int n2, int n3, int n4) {
        this.fill(new Rectangle(n, n2, n3, n4));
    }

    @Override
    public void fillRoundRect(int n, int n2, int n3, int n4, int n5, int n6) {
        this.fill(new RoundRectangle2D.Double(n, n2, n3, n4, n5, n6));
    }

    @Override
    public Shape getClip() {
        return this.e.getClip();
    }

    @Override
    public Rectangle getClipBounds() {
        if (this.getClip() == null) {
            return null;
        }
        return this.getClip().getBounds();
    }

    @Override
    public Color getColor() {
        return this.e.getColor();
    }

    @Override
    public Font getFont() {
        return this.e.getFont();
    }

    @Override
    public FontMetrics getFontMetrics(Font object) {
        object = new BufferedImage(1, 1, 3);
        object = ((BufferedImage)object).getGraphics();
        FontMetrics fontMetrics = ((Graphics)object).getFontMetrics(this.getFont());
        ((Graphics)object).dispose();
        return fontMetrics;
    }

    @Override
    public void setClip(Shape shape) {
        if (this.isDisposed()) {
            return;
        }
        this.a(new SetClipCommand(shape));
        this.e.setClip(shape);
        this.f.setClip(shape);
        if (this.getClip() == null) {
            if (this.f.getClip() != null) {
                System.err.printf("setClip() validation failed: clip=null, validation=%s\n", this.f.getClip());
                return;
            }
        } else if (!GraphicsUtils.equals(this.getClip(), this.f.getClip())) {
            System.err.printf("setClip() validation failed: clip=%s, validation=%s\n", this.getClip(), this.f.getClip());
        }
    }

    @Override
    public void setClip(int n, int n2, int n3, int n4) {
        this.setClip(new Rectangle(n, n2, n3, n4));
    }

    @Override
    public void setColor(Color color) {
        if (this.isDisposed() || color == null || this.getColor().equals(color)) {
            return;
        }
        this.a(new SetColorCommand(color));
        this.e.setColor(color);
        this.e.setPaint(color);
        this.f.setColor(color);
        if (!this.getColor().equals(this.f.getColor())) {
            System.err.println("setColor() validation failed");
        }
    }

    @Override
    public void setFont(Font font) {
        if (this.isDisposed() || font != null && this.getFont().equals(font)) {
            return;
        }
        this.a(new SetFontCommand(font));
        this.e.setFont(font);
        this.f.setFont(font);
        if (!this.getFont().equals(this.f.getFont())) {
            System.err.println("setFont() validation failed");
        }
    }

    @Override
    public void setPaintMode() {
        this.setComposite(AlphaComposite.SrcOver);
        this.f.setPaintMode();
    }

    public Color getXORMode() {
        return this.e.getXorMode();
    }

    @Override
    public void setXORMode(Color color) {
        if (this.isDisposed() || color == null) {
            return;
        }
        this.a(new SetXORModeCommand(color));
        this.e.setXorMode(color);
        this.f.setXORMode(color);
    }

    private void a(Command<?> command) {
        this.a.add(command);
    }

    protected Iterable<Command<?>> getCommands() {
        return this.a;
    }

    protected boolean isDisposed() {
        return this.d;
    }
}

