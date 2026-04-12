/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.pdf;

import de.erichseifert.vectorgraphics2d.GraphicsState;
import de.erichseifert.vectorgraphics2d.SizedDocument;
import de.erichseifert.vectorgraphics2d.intermediate.commands.AffineTransformCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import de.erichseifert.vectorgraphics2d.intermediate.commands.CreateCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DisposeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DrawImageCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DrawShapeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DrawStringCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.FillShapeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.Group;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetBackgroundCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetClipCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetColorCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetFontCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetHintCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetPaintCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetStrokeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetTransformCommand;
import de.erichseifert.vectorgraphics2d.pdf.PDFObject;
import de.erichseifert.vectorgraphics2d.pdf.Payload;
import de.erichseifert.vectorgraphics2d.pdf.Resources;
import de.erichseifert.vectorgraphics2d.pdf.SizePayload;
import de.erichseifert.vectorgraphics2d.util.DataUtils;
import de.erichseifert.vectorgraphics2d.util.FlateEncodeStream;
import de.erichseifert.vectorgraphics2d.util.FormattingWriter;
import de.erichseifert.vectorgraphics2d.util.GraphicsUtils;
import de.erichseifert.vectorgraphics2d.util.ImageDataStream;
import de.erichseifert.vectorgraphics2d.util.PageSize;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class PDFDocument
extends SizedDocument {
    private static final Map<Integer, Integer> a = DataUtils.map(new Integer[]{0, 1, 2}, new Integer[]{0, 1, 2});
    private static final Map<Integer, Integer> b = DataUtils.map(new Integer[]{0, 1, 2}, new Integer[]{0, 1, 2});
    private final List<PDFObject> c;
    private int d;
    private final Map<PDFObject, Long> e;
    private PDFObject f;
    private Resources g;
    private final Map<Integer, PDFObject> h;
    private final Stack<GraphicsState> i;
    private boolean j;
    private final boolean k;

    public PDFDocument(PageSize object, boolean bl) {
        super((PageSize)object);
        this.k = bl;
        this.i = new Stack();
        this.i.push(new GraphicsState());
        this.c = new LinkedList<PDFObject>();
        this.d = 1;
        this.e = new HashMap<PDFObject, Long>();
        this.h = new HashMap<Integer, PDFObject>();
        object = this;
        Object object2 = DataUtils.map(new String[]{"Type"}, new Object[]{"Catalog"});
        Object object3 = super.a((Map<String, Object>)object2, null);
        Object object4 = new LinkedList<Object>();
        object2 = DataUtils.map(new String[]{"Type", "Kids", "Count"}, new Object[]{"Pages", object4, 1});
        object2 = super.a((Map<String, Object>)object2, null);
        ((PDFObject)object3).dict.put("Pages", object2);
        double d = ((SizedDocument)object).getPageSize().x * 2.834645669291339;
        double d2 = ((SizedDocument)object).getPageSize().y * 2.834645669291339;
        double d3 = ((SizedDocument)object).getPageSize().width * 2.834645669291339;
        double d4 = ((SizedDocument)object).getPageSize().height * 2.834645669291339;
        object2 = DataUtils.map(new String[]{"Type", "Parent", "MediaBox"}, new Object[]{"Page", object2, new double[]{d, d2, d3, d4}});
        object2 = super.a((Map<String, Object>)object2, null);
        object4.add(object2);
        object3 = new Payload(true);
        ((PDFDocument)object).f = super.a(null, (Payload)object3);
        ((PDFObject)object2).dict.put("Contents", ((PDFDocument)object).f);
        if (((PDFDocument)object).k) {
            ((Payload)object3).addFilter(FlateEncodeStream.class);
            ((PDFDocument)object).f.dict.put("Filter", new Object[]{"FlateDecode"});
        }
        try {
            ((OutputStream)object3).write(DataUtils.join("", new Object[]{"q", "\n", PDFDocument.a(super.a().getColor()), "\n", 2.834645669291339, " 0 0 ", -2.834645669291339, " 0 ", d4, " cm", "\n"}).getBytes("ISO-8859-1"));
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        object4 = new SizePayload(((PDFDocument)object).f, "ISO-8859-1", false);
        object4 = super.a(null, (Payload)object4);
        ((PDFDocument)object).f.dict.put("Length", object4);
        ((PDFDocument)object).g = new Resources(((PDFDocument)object).d++, 0);
        ((PDFDocument)object).c.add(((PDFDocument)object).g);
        ((PDFObject)object2).dict.put("Resources", ((PDFDocument)object).g);
        object2 = super.a().getFont();
        object = ((PDFDocument)object).g.getId((Font)object2);
        float f = ((Font)object2).getSize2D();
        object4 = new StringBuilder();
        ((StringBuilder)object4).append("/").append((String)object).append(" ").append(f).append(" Tf\n");
        try {
            ((OutputStream)object3).write(((StringBuilder)object4).toString().getBytes("ISO-8859-1"));
            return;
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    private GraphicsState a() {
        return this.i.peek();
    }

    private PDFObject a(Map<String, Object> object, Payload payload) {
        int n = this.d++;
        object = new PDFObject(n, 0, (Map<String, Object>)object, payload);
        this.c.add((PDFObject)object);
        return object;
    }

    private PDFObject a(Image image) {
        image = GraphicsUtils.toBufferedImage(image);
        int n = ((BufferedImage)image).getWidth();
        int n2 = ((BufferedImage)image).getHeight();
        int n3 = DataUtils.max(((BufferedImage)image).getSampleModel().getSampleSize());
        int n4 = ((BufferedImage)image).getSampleModel().getNumBands();
        String string = n4 == 1 ? "DeviceGray" : "DeviceRGB";
        Payload payload = new Payload(true);
        String[] stringArray = new String[]{};
        if (this.k) {
            payload.addFilter(FlateEncodeStream.class);
            stringArray = new String[]{"FlateDecode"};
        }
        ImageDataStream imageDataStream = new ImageDataStream((BufferedImage)image, ImageDataStream.Interleaving.WITHOUT_ALPHA);
        try {
            DataUtils.transfer(imageDataStream, payload, 1024);
            payload.close();
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        int n5 = payload.getBytes().length;
        Object object = DataUtils.map(new String[]{"Type", "Subtype", "Width", "Height", "ColorSpace", "BitsPerComponent", "Length", "Filter"}, new Object[]{"XObject", "Image", n, n2, string, n3, n5, stringArray});
        object = this.a((Map<String, Object>)object, payload);
        boolean bl = ((BufferedImage)image).getColorModel().hasAlpha();
        n2 = bl ? 1 : 0;
        if (bl) {
            image = GraphicsUtils.getAlphaImage((BufferedImage)image);
            PDFObject pDFObject = this.a(image);
            boolean bl2 = ((BufferedImage)image).getSampleModel().getSampleSize(0) == 1;
            if (bl2) {
                pDFObject.dict.put("ImageMask", true);
                pDFObject.dict.remove("ColorSpace");
                ((PDFObject)object).dict.put("Mask", pDFObject);
            } else {
                ((PDFObject)object).dict.put("SMask", pDFObject);
            }
        }
        return object;
    }

    @Override
    public void write(OutputStream closeable) throws IOException {
        closeable = new FormattingWriter((OutputStream)closeable, "ISO-8859-1", "\n");
        ((FormattingWriter)closeable).writeln("%PDF-1.4");
        for (PDFObject pDFObject : this.c) {
            this.e.put(pDFObject, ((FormattingWriter)closeable).tell());
            ((FormattingWriter)closeable).writeln(PDFDocument.toString(pDFObject));
            ((FormattingWriter)closeable).flush();
        }
        long l = ((FormattingWriter)closeable).tell();
        ((FormattingWriter)closeable).writeln("xref");
        ((FormattingWriter)closeable).write(0).write(" ").writeln(this.c.size() + 1);
        ((FormattingWriter)closeable).format("%010d %05d f ", 0, 65535).writeln();
        for (PDFObject pDFObject : this.c) {
            ((FormattingWriter)closeable).format("%010d %05d n ", this.e.get(pDFObject), 0).writeln();
        }
        ((FormattingWriter)closeable).flush();
        ((FormattingWriter)closeable).writeln("trailer");
        ((FormattingWriter)closeable).writeln(PDFDocument.a(DataUtils.map(new String[]{"Size", "Root"}, new Object[]{this.c.size() + 1, this.c.get(0)})));
        ((FormattingWriter)closeable).writeln("startxref");
        ((FormattingWriter)closeable).writeln(l);
        ((FormattingWriter)closeable).writeln("%%EOF");
        ((FormattingWriter)closeable).flush();
    }

    public static String toString(PDFObject pDFObject) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pDFObject.id).append(" ").append(pDFObject.version).append(" obj\n");
        if (!pDFObject.dict.isEmpty()) {
            stringBuilder.append(PDFDocument.a(pDFObject.dict)).append("\n");
        }
        if (pDFObject.payload != null) {
            String string;
            try {
                string = new String(pDFObject.payload.getBytes(), "ISO-8859-1");
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                string = "";
            }
            if (string.length() > 0) {
                if (pDFObject.payload.isStream()) {
                    stringBuilder.append("stream\n");
                }
                stringBuilder.append(string);
                if (pDFObject.payload.isStream()) {
                    stringBuilder.append("endstream");
                }
                stringBuilder.append("\n");
            }
        }
        stringBuilder.append("endobj");
        return stringBuilder.toString();
    }

    /*
     * WARNING - void declaration
     */
    private static String a(Object object2) {
        void var0_1;
        while (true) {
            if (var0_1 instanceof String) {
                return "/" + var0_1.toString();
            }
            if (var0_1 instanceof float[]) {
                List<Float> list = DataUtils.asList((float[])var0_1);
                continue;
            }
            if (var0_1 instanceof double[]) {
                List<Double> list = DataUtils.asList((double[])var0_1);
                continue;
            }
            if (!(var0_1 instanceof Object[])) break;
            List<Object> list = Arrays.asList((Object[])var0_1);
        }
        if (var0_1 instanceof List) {
            List list = (List)var0_1;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            int n = 0;
            for (Object e : list) {
                if (n++ > 0) {
                    stringBuilder.append(" ");
                }
                stringBuilder.append(PDFDocument.a(e));
            }
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
        if (var0_1 instanceof Map) {
            Map map = (Map)var0_1;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<<\n");
            for (Map.Entry entry : map.entrySet()) {
                String string = entry.getKey().toString();
                stringBuilder.append(PDFDocument.a(string)).append(" ");
                Object v = entry.getValue();
                stringBuilder.append(PDFDocument.a(v)).append("\n");
            }
            stringBuilder.append(">>");
            return stringBuilder.toString();
        }
        if (var0_1 instanceof PDFObject) {
            PDFObject pDFObject = (PDFObject)var0_1;
            return String.valueOf(pDFObject.id) + " " + pDFObject.version + " R";
        }
        return DataUtils.format(var0_1);
    }

    @Override
    public void handle(Command<?> object) {
        Object object2 = "";
        if (object instanceof Group) {
            Object object3;
            Object object4;
            Object object5;
            Object object6;
            Object object7;
            object = (Group)object;
            Object object8 = (List)((Command)object).getValue();
            object = this;
            Iterator iterator = object8.iterator();
            while (iterator.hasNext()) {
                Object object9;
                object7 = (Command)iterator.next();
                if (object7 instanceof SetHintCommand) {
                    object9 = (SetHintCommand)object7;
                    super.a().getHints().put(((SetHintCommand)object9).getKey(), ((Command)object9).getValue());
                    continue;
                }
                if (object7 instanceof SetBackgroundCommand) {
                    object9 = (SetBackgroundCommand)object7;
                    super.a().setBackground((Color)((Command)object9).getValue());
                    continue;
                }
                if (object7 instanceof SetColorCommand) {
                    object9 = (SetColorCommand)object7;
                    super.a().setColor((Color)((Command)object9).getValue());
                    continue;
                }
                if (object7 instanceof SetPaintCommand) {
                    object9 = (SetPaintCommand)object7;
                    super.a().setPaint((Paint)((Command)object9).getValue());
                    continue;
                }
                if (object7 instanceof SetStrokeCommand) {
                    object9 = (SetStrokeCommand)object7;
                    super.a().setStroke((Stroke)((Command)object9).getValue());
                    continue;
                }
                if (object7 instanceof SetFontCommand) {
                    object9 = (SetFontCommand)object7;
                    super.a().setFont((Font)((Command)object9).getValue());
                    continue;
                }
                if (object7 instanceof SetTransformCommand) {
                    throw new UnsupportedOperationException("The PDF format has no means of setting the transformation matrix.");
                }
                if (object7 instanceof AffineTransformCommand) {
                    object9 = (AffineTransformCommand)object7;
                    object6 = super.a().getTransform();
                    object5 = (AffineTransform)((Command)object9).getValue();
                    ((AffineTransform)object6).concatenate((AffineTransform)object5);
                    super.a().setTransform((AffineTransform)object6);
                    continue;
                }
                if (object7 instanceof SetClipCommand) {
                    object9 = (SetClipCommand)object7;
                    super.a().setClip((Shape)((Command)object9).getValue());
                    continue;
                }
                if (object7 instanceof CreateCommand) {
                    try {
                        ((PDFDocument)object).i.push((GraphicsState)super.a().clone());
                    }
                    catch (CloneNotSupportedException cloneNotSupportedException) {
                        object9 = cloneNotSupportedException;
                        cloneNotSupportedException.printStackTrace();
                    }
                    continue;
                }
                if (!(object7 instanceof DisposeCommand)) continue;
                ((PDFDocument)object).i.pop();
            }
            boolean bl = !this.j;
            object8 = this.g;
            object = this.a();
            object7 = new StringBuilder();
            if (!bl) {
                ((StringBuilder)object7).append("Q\n");
            }
            ((StringBuilder)object7).append("q\n");
            if (!((GraphicsState)object).getColor().equals(GraphicsState.DEFAULT_COLOR)) {
                if (((GraphicsState)object).getColor().getAlpha() != GraphicsState.DEFAULT_COLOR.getAlpha()) {
                    double d = (double)((GraphicsState)object).getColor().getAlpha() / 255.0;
                    object5 = ((Resources)object8).getId(d);
                    ((StringBuilder)object7).append("/").append((String)object5).append(" gs\n");
                }
                ((StringBuilder)object7).append(PDFDocument.a(((GraphicsState)object).getColor())).append("\n");
            }
            if (!((GraphicsState)object).getTransform().equals(GraphicsState.DEFAULT_TRANSFORM)) {
                object4 = ((GraphicsState)object).getTransform();
                object3 = new double[6];
                ((AffineTransform)object4).getMatrix((double[])object3);
                ((StringBuilder)object7).append(DataUtils.join(" ", (double[])object3)).append(" cm\n");
            }
            if (!((GraphicsState)object).getStroke().equals(GraphicsState.DEFAULT_STROKE)) {
                object4 = ((GraphicsState)object).getStroke();
                object3 = new StringBuilder();
                if (object4 instanceof BasicStroke) {
                    object2 = (BasicStroke)GraphicsState.DEFAULT_STROKE;
                    BasicStroke basicStroke = (BasicStroke)object4;
                    if (basicStroke.getLineWidth() != ((BasicStroke)object2).getLineWidth()) {
                        ((StringBuilder)object3).append(PDFDocument.a(Float.valueOf(basicStroke.getLineWidth()))).append(" w\n");
                    }
                    if (basicStroke.getLineJoin() == 0 && basicStroke.getMiterLimit() != ((BasicStroke)object2).getMiterLimit()) {
                        ((StringBuilder)object3).append(PDFDocument.a(Float.valueOf(basicStroke.getMiterLimit()))).append(" M\n");
                    }
                    if (basicStroke.getLineJoin() != ((BasicStroke)object2).getLineJoin()) {
                        ((StringBuilder)object3).append(PDFDocument.a(b.get(basicStroke.getLineJoin()))).append(" j\n");
                    }
                    if (basicStroke.getEndCap() != ((BasicStroke)object2).getEndCap()) {
                        ((StringBuilder)object3).append(PDFDocument.a(a.get(basicStroke.getEndCap()))).append(" J\n");
                    }
                    if (basicStroke.getDashArray() != ((BasicStroke)object2).getDashArray()) {
                        if (basicStroke.getDashArray() != null) {
                            ((StringBuilder)object3).append(PDFDocument.a(basicStroke.getDashArray())).append(" ").append(PDFDocument.a(Float.valueOf(basicStroke.getDashPhase()))).append(" d\n");
                        } else {
                            ((StringBuilder)object3).append("\n[] 0 d").append("\n");
                        }
                    }
                }
                ((StringBuilder)object7).append(((StringBuilder)object3).toString()).append("\n");
            }
            if (((GraphicsState)object).getClip() != GraphicsState.DEFAULT_CLIP) {
                ((StringBuilder)object7).append(PDFDocument.a(((GraphicsState)object).getClip())).append(" W n\n");
            }
            if (!((GraphicsState)object).getFont().equals(GraphicsState.DEFAULT_FONT)) {
                Font font = ((GraphicsState)object).getFont();
                object6 = ((Resources)object8).getId(font);
                float f = font.getSize2D();
                ((StringBuilder)object7).append("/").append((String)object6).append(" ").append(f).append(" Tf\n");
            }
            object2 = DataUtils.stripTrailing(((StringBuilder)object7).toString(), "\n");
            this.j = true;
        } else if (object instanceof DrawShapeCommand) {
            object = (DrawShapeCommand)object;
            object2 = PDFDocument.a((Shape)((Command)object).getValue()) + " S";
        } else if (object instanceof FillShapeCommand) {
            object = (FillShapeCommand)object;
            object2 = PDFDocument.a((Shape)((Command)object).getValue()) + " f";
        } else if (object instanceof DrawStringCommand) {
            object = (DrawStringCommand)object;
            double d = ((DrawStringCommand)object).getY();
            double d2 = ((DrawStringCommand)object).getX();
            object = (String)((Command)object).getValue();
            StringBuilder stringBuilder = new StringBuilder("q 1 0 0 -1 ").append(d2).append(" ").append(d).append(" cm BT ");
            Object object10 = object;
            StringBuilder stringBuilder2 = new StringBuilder();
            object10 = ((String)object10).replaceAll("\\\\", "\\\\\\\\").replaceAll("\t", "\\\\t").replaceAll("\b", "\\\\b").replaceAll("\f", "\\\\f").replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)").replaceAll("[\r\n]", "");
            stringBuilder2.append("(").append((String)object10).append(")");
            object2 = stringBuilder.append((Object)stringBuilder2).append(" Tj ET Q").toString();
        } else if (object instanceof DrawImageCommand) {
            object2 = (Image)((Command)(object = (DrawImageCommand)object)).getValue();
            Object object11 = this.h.get(object2.hashCode());
            if (object11 == null) {
                object11 = this.a((Image)object2);
                this.h.put(object2.hashCode(), (PDFObject)object11);
            }
            object2 = this.g;
            double d = ((DrawImageCommand)object).getHeight();
            double d3 = ((DrawImageCommand)object).getWidth();
            double d4 = ((DrawImageCommand)object).getY();
            double d5 = ((DrawImageCommand)object).getX();
            object = object11;
            object11 = ((Resources)object2).getId((PDFObject)object);
            object2 = "q " + d3 + " 0 0 " + d + " " + d5 + " " + d4 + " cm 1 0 0 -1 0 1 cm /" + (String)object11 + " Do Q";
        }
        try {
            object = this.f.payload;
            ((OutputStream)object).write(((String)object2).getBytes("ISO-8859-1"));
            ((OutputStream)object).write("\n".getBytes("ISO-8859-1"));
            return;
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    private static String a(Color object) {
        if (((Color)object).getColorSpace().getType() == 9) {
            Object object2 = ((Color)object).getComponents(null);
            String string = PDFDocument.a(Float.valueOf(object2[0]));
            object = PDFDocument.a(Float.valueOf(object2[1]));
            String string2 = PDFDocument.a(Float.valueOf(object2[2]));
            object2 = PDFDocument.a(Float.valueOf(object2[3]));
            return string + " " + (String)object + " " + string2 + " " + (String)object2 + " k " + string + " " + (String)object + " " + string2 + " " + (String)object2 + " K";
        }
        String string = PDFDocument.a((double)((Color)object).getRed() / 255.0);
        String string3 = PDFDocument.a((double)((Color)object).getGreen() / 255.0);
        object = PDFDocument.a((double)((Color)object).getBlue() / 255.0);
        return string + " " + string3 + " " + (String)object + " rg " + string + " " + string3 + " " + (String)object + " RG";
    }

    private static String a(Shape object) {
        StringBuilder stringBuilder = new StringBuilder();
        object = object.getPathIterator(null);
        double[] dArray = new double[6];
        double[] dArray2 = new double[2];
        int n = 0;
        while (!object.isDone()) {
            if (n > 0) {
                stringBuilder.append(" ");
            }
            int n2 = object.currentSegment(dArray);
            switch (n2) {
                case 0: {
                    stringBuilder.append(PDFDocument.a(dArray[0])).append(" ").append(PDFDocument.a(dArray[1])).append(" m");
                    dArray2[0] = dArray[0];
                    dArray2[1] = dArray[1];
                    break;
                }
                case 1: {
                    stringBuilder.append(PDFDocument.a(dArray[0])).append(" ").append(PDFDocument.a(dArray[1])).append(" l");
                    dArray2[0] = dArray[0];
                    dArray2[1] = dArray[1];
                    break;
                }
                case 3: {
                    stringBuilder.append(PDFDocument.a(dArray[0])).append(" ").append(PDFDocument.a(dArray[1])).append(" ").append(PDFDocument.a(dArray[2])).append(" ").append(PDFDocument.a(dArray[3])).append(" ").append(PDFDocument.a(dArray[4])).append(" ").append(PDFDocument.a(dArray[5])).append(" c");
                    dArray2[0] = dArray[4];
                    dArray2[1] = dArray[5];
                    break;
                }
                case 2: {
                    double d = dArray2[0] + 0.6666666666666666 * (dArray[0] - dArray2[0]);
                    double d2 = dArray2[1] + 0.6666666666666666 * (dArray[1] - dArray2[1]);
                    double d3 = dArray[0] + 0.3333333333333333 * (dArray[2] - dArray[0]);
                    double d4 = dArray[1] + 0.3333333333333333 * (dArray[3] - dArray[1]);
                    double d5 = dArray[2];
                    double d6 = dArray[3];
                    stringBuilder.append(PDFDocument.a(d)).append(" ").append(PDFDocument.a(d2)).append(" ").append(PDFDocument.a(d3)).append(" ").append(PDFDocument.a(d4)).append(" ").append(PDFDocument.a(d5)).append(" ").append(PDFDocument.a(d6)).append(" c");
                    dArray2[0] = d5;
                    dArray2[1] = d6;
                    break;
                }
                case 4: {
                    stringBuilder.append("h");
                    break;
                }
                default: {
                    throw new IllegalStateException("Unknown path operation.");
                }
            }
            ++n;
            object.next();
        }
        return stringBuilder.toString();
    }

    @Override
    public void close() {
        try {
            String string = "Q\n";
            if (this.j) {
                string = string + "Q\n";
            }
            Payload payload = this.f.payload;
            payload.write(string.getBytes("ISO-8859-1"));
            payload.close();
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        super.close();
    }

    public boolean isCompressed() {
        return this.k;
    }
}

