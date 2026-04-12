/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.svg;

import de.erichseifert.vectorgraphics2d.GraphicsState;
import de.erichseifert.vectorgraphics2d.SizedDocument;
import de.erichseifert.vectorgraphics2d.VectorHints;
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
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetCompositeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetFontCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetHintCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetPaintCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetStrokeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetTransformCommand;
import de.erichseifert.vectorgraphics2d.util.Base64EncodeStream;
import de.erichseifert.vectorgraphics2d.util.DataUtils;
import de.erichseifert.vectorgraphics2d.util.GraphicsUtils;
import de.erichseifert.vectorgraphics2d.util.PageSize;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SVGDocument
extends SizedDocument {
    private final Stack<GraphicsState> a = new Stack();
    private final Document b;
    private final Element c;
    private Element d;
    private boolean e;
    private Element f;
    private final Map<Integer, Element> g;
    private static final Map<Integer, String> h = DataUtils.map(new Integer[]{0, 1, 2}, new String[]{"butt", "round", "square"});
    private static final Map<Integer, String> i = DataUtils.map(new Integer[]{0, 1, 2}, new String[]{"miter", "round", "bevel"});

    public SVGDocument(PageSize object) {
        super((PageSize)object);
        this.a.push(new GraphicsState());
        this.g = new HashMap<Integer, Element>();
        object = DocumentBuilderFactory.newInstance();
        ((DocumentBuilderFactory)object).setValidating(false);
        try {
            object = ((DocumentBuilderFactory)object).newDocumentBuilder();
        }
        catch (ParserConfigurationException parserConfigurationException) {
            throw new IllegalStateException("Could not create XML builder.");
        }
        object = ((DocumentBuilder)object).getDOMImplementation();
        DocumentType documentType = object.createDocumentType("svg", "-//W3C//DTD SVG 1.1//EN", "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");
        this.b = object.createDocument("http://www.w3.org/2000/svg", "svg", documentType);
        try {
            this.b.setXmlStandalone(false);
        }
        catch (AbstractMethodError abstractMethodError) {
            System.err.println("Your XML parser does not support standalone XML documents.");
        }
        this.c = this.b.getDocumentElement();
        object = this;
        double d = ((SizedDocument)object).getPageSize().x;
        double d2 = ((SizedDocument)object).getPageSize().y;
        double d3 = ((SizedDocument)object).getPageSize().width;
        double d4 = ((SizedDocument)object).getPageSize().height;
        ((SVGDocument)object).c.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        ((SVGDocument)object).c.setAttribute("version", "1.1");
        ((SVGDocument)object).c.setAttribute("x", DataUtils.format(d / 2.834645669291339) + "mm");
        ((SVGDocument)object).c.setAttribute("y", DataUtils.format(d2 / 2.834645669291339) + "mm");
        ((SVGDocument)object).c.setAttribute("width", DataUtils.format(d3 / 2.834645669291339) + "mm");
        ((SVGDocument)object).c.setAttribute("height", DataUtils.format(d4 / 2.834645669291339) + "mm");
        ((SVGDocument)object).c.setAttribute("viewBox", DataUtils.join(" ", new double[]{d, d2, d3, d4}));
        this.d = this.c;
    }

    private GraphicsState a() {
        return this.a.peek();
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        Object object = TransformerFactory.newInstance();
        try {
            object = ((TransformerFactory)object).newTransformer();
            ((Transformer)object).setOutputProperty("indent", "yes");
            ((Transformer)object).setOutputProperty("standalone", "no");
            ((Transformer)object).setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            ((Transformer)object).setOutputProperty("encoding", "UTF-8");
            ((Transformer)object).setOutputProperty("doctype-public", this.b.getDoctype().getPublicId());
            ((Transformer)object).setOutputProperty("doctype-system", this.b.getDoctype().getSystemId());
            ((Transformer)object).transform(new DOMSource(this.b), new StreamResult(outputStream));
            return;
        }
        catch (TransformerConfigurationException transformerConfigurationException) {
            throw new IOException(transformerConfigurationException.getMessage());
        }
        catch (TransformerException transformerException) {
            throw new IOException(transformerException.getMessage());
        }
    }

    public String toString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            this.write(byteArrayOutputStream);
            return byteArrayOutputStream.toString("UTF-8");
        }
        catch (IOException iOException) {
            return "";
        }
    }

    private void a(Element element) {
        this.d.appendChild(element);
        if (!this.e && this.d != this.c) {
            this.c.appendChild(this.d);
            this.e = true;
        }
    }

    @Override
    public void handle(Command<?> object) {
        if (object instanceof Group) {
            boolean bl;
            Object object2;
            Object object3;
            Object object4;
            Object object5;
            Object object6;
            Object object7;
            Object object8;
            Object object9;
            block34: {
                object = (Group)object;
                object9 = (List)((Command)object).getValue();
                object8 = this;
                object7 = object9.iterator();
                while (object7.hasNext()) {
                    object6 = (Command)object7.next();
                    object5 = ((SVGDocument)object8).a();
                    if (object6 instanceof SetBackgroundCommand) {
                        object4 = (SetBackgroundCommand)object6;
                        ((GraphicsState)object5).setBackground((Color)((Command)object4).getValue());
                        continue;
                    }
                    if (object6 instanceof SetClipCommand) {
                        object4 = (SetClipCommand)object6;
                        ((GraphicsState)object5).setClip((Shape)((Command)object4).getValue());
                        continue;
                    }
                    if (object6 instanceof SetColorCommand) {
                        object4 = (SetColorCommand)object6;
                        ((GraphicsState)object5).setColor((Color)((Command)object4).getValue());
                        continue;
                    }
                    if (object6 instanceof SetCompositeCommand) {
                        object4 = (SetCompositeCommand)object6;
                        ((GraphicsState)object5).setComposite((Composite)((Command)object4).getValue());
                        continue;
                    }
                    if (object6 instanceof SetFontCommand) {
                        object4 = (SetFontCommand)object6;
                        ((GraphicsState)object5).setFont((Font)((Command)object4).getValue());
                        continue;
                    }
                    if (object6 instanceof SetPaintCommand) {
                        object4 = (SetPaintCommand)object6;
                        ((GraphicsState)object5).setPaint((Paint)((Command)object4).getValue());
                        continue;
                    }
                    if (object6 instanceof SetStrokeCommand) {
                        object4 = (SetStrokeCommand)object6;
                        ((GraphicsState)object5).setStroke((Stroke)((Command)object4).getValue());
                        continue;
                    }
                    if (object6 instanceof SetTransformCommand) {
                        object4 = (SetTransformCommand)object6;
                        ((GraphicsState)object5).setTransform((AffineTransform)((Command)object4).getValue());
                        continue;
                    }
                    if (object6 instanceof AffineTransformCommand) {
                        object4 = (AffineTransformCommand)object6;
                        object3 = ((GraphicsState)object5).getTransform();
                        object2 = (AffineTransform)((Command)object4).getValue();
                        ((AffineTransform)object3).concatenate((AffineTransform)object2);
                        ((GraphicsState)object5).setTransform((AffineTransform)object3);
                        continue;
                    }
                    if (object6 instanceof SetHintCommand) {
                        object4 = (SetHintCommand)object6;
                        ((GraphicsState)object5).getHints().put(((SetHintCommand)object4).getKey(), ((Command)object4).getValue());
                        continue;
                    }
                    if (object6 instanceof CreateCommand) {
                        try {
                            ((SVGDocument)object8).a.push((GraphicsState)super.a().clone());
                        }
                        catch (CloneNotSupportedException cloneNotSupportedException) {
                            object4 = cloneNotSupportedException;
                            cloneNotSupportedException.printStackTrace();
                        }
                        continue;
                    }
                    if (!(object6 instanceof DisposeCommand)) continue;
                    ((SVGDocument)object8).a.pop();
                }
                object8 = (List)((Command)object).getValue();
                object7 = object8.iterator();
                while (object7.hasNext()) {
                    object6 = (Command)object7.next();
                    if (!(object6 instanceof SetClipCommand) && !(object6 instanceof SetTransformCommand) && !(object6 instanceof AffineTransformCommand)) continue;
                    bl = true;
                    break block34;
                }
                bl = false;
            }
            if (bl) {
                object8 = this;
                this.d = ((SVGDocument)object8).b.createElement("g");
                ((SVGDocument)object8).e = false;
                object9 = super.a().getClip();
                if (object9 != GraphicsState.DEFAULT_CLIP) {
                    Object object10;
                    object4 = object9;
                    object5 = object8;
                    object3 = ((SVGDocument)object5).g.get(object4.hashCode());
                    if (object3 != null) {
                        object10 = object3;
                    } else {
                        if (((SVGDocument)object5).f == null) {
                            ((SVGDocument)object5).f = ((SVGDocument)object5).b.createElement("defs");
                            ((SVGDocument)object5).c.insertBefore(((SVGDocument)object5).f, ((SVGDocument)object5).c.getFirstChild());
                        }
                        object3 = ((SVGDocument)object5).b.createElement("clipPath");
                        object3.setAttribute("id", "clip" + object4.hashCode());
                        object2 = super.a((Shape)object4);
                        object2.removeAttribute("style");
                        object3.appendChild((Node)object2);
                        ((SVGDocument)object5).f.appendChild((Node)object3);
                        ((SVGDocument)object5).g.put(object4.hashCode(), (Element)object3);
                        object10 = object3;
                    }
                    object7 = object10;
                    object6 = "url(#" + object7.getAttribute("id") + ")";
                    ((SVGDocument)object8).d.setAttribute("clip-path", (String)object6);
                }
                if (!GraphicsState.DEFAULT_TRANSFORM.equals(object7 = super.a().getTransform())) {
                    Element element = ((SVGDocument)object8).d;
                    object5 = object7;
                    object4 = new StringBuilder();
                    if (AffineTransform.getTranslateInstance(((AffineTransform)object5).getTranslateX(), ((AffineTransform)object5).getTranslateY()).equals(object5)) {
                        ((StringBuilder)object4).append("translate(").append(DataUtils.format(((AffineTransform)object5).getTranslateX())).append(" ").append(DataUtils.format(((AffineTransform)object5).getTranslateY())).append(")");
                    } else {
                        object3 = new double[6];
                        ((AffineTransform)object5).getMatrix((double[])object3);
                        ((StringBuilder)object4).append("matrix(").append(DataUtils.join(" ", (double[])object3)).append(")");
                    }
                    element.setAttribute("transform", ((StringBuilder)object4).toString());
                }
            }
            return;
        }
        if (object instanceof DrawImageCommand) {
            boolean bl;
            object = (DrawImageCommand)object;
            double d = ((DrawImageCommand)object).getHeight();
            double d2 = ((DrawImageCommand)object).getWidth();
            double d3 = ((DrawImageCommand)object).getY();
            double d4 = ((DrawImageCommand)object).getX();
            Image image = (Image)((Command)object).getValue();
            SVGDocument sVGDocument = this;
            object = sVGDocument.b.createElement("image");
            object.setAttribute("x", DataUtils.format(d4));
            object.setAttribute("y", DataUtils.format(d3));
            object.setAttribute("width", DataUtils.format(d2));
            object.setAttribute("height", DataUtils.format(d));
            object.setAttribute("preserveAspectRatio", "none");
            boolean bl2 = bl = sVGDocument.a().getHints().get(VectorHints.KEY_EXPORT) == VectorHints.VALUE_EXPORT_SIZE;
            Object object11 = image;
            object11 = GraphicsUtils.toBufferedImage((Image)object11);
            Object object12 = SVGDocument.a((BufferedImage)object11, "png");
            if (!GraphicsUtils.usesAlpha((Image)object11) && bl2 && ((String)(object11 = SVGDocument.a((BufferedImage)object11, "jpeg"))).length() > 0 && ((String)object11).length() < ((String)object12).length()) {
                object12 = object11;
            }
            object.setAttribute("xlink:href", (String)object12);
            this.a((Element)object);
            return;
        }
        if (object instanceof DrawShapeCommand) {
            object = (DrawShapeCommand)object;
            object = this.a((Shape)((Command)object).getValue());
            object.setAttribute("style", this.a(false));
            this.a((Element)object);
            return;
        }
        if (object instanceof DrawStringCommand) {
            object = (DrawStringCommand)object;
            double d = ((DrawStringCommand)object).getY();
            double d5 = ((DrawStringCommand)object).getX();
            Object object13 = (String)((Command)object).getValue();
            SVGDocument sVGDocument = this;
            Element element = sVGDocument.b.createElement("text");
            element.appendChild(sVGDocument.b.createTextNode((String)object13));
            element.setAttribute("x", DataUtils.format(d5));
            element.setAttribute("y", DataUtils.format(d));
            object = element;
            object13 = this.a().getFont();
            sVGDocument = this;
            String string = sVGDocument.a(true);
            if (!GraphicsState.DEFAULT_FONT.equals(object13)) {
                StringBuilder stringBuilder = new StringBuilder().append(string);
                Object object14 = object13;
                StringBuilder stringBuilder2 = new StringBuilder();
                if (!GraphicsState.DEFAULT_FONT.getFamily().equals(((Font)object14).getFamily())) {
                    String string2 = GraphicsUtils.getPhysicalFont((Font)object14).getFamily();
                    stringBuilder2.append("font-family:\"").append(string2).append("\";");
                }
                if (((Font)object14).getSize2D() != GraphicsState.DEFAULT_FONT.getSize2D()) {
                    stringBuilder2.append("font-size:").append(DataUtils.format(Float.valueOf(((Font)object14).getSize2D()))).append("px;");
                }
                if ((((Font)object14).getStyle() & 2) != 0) {
                    stringBuilder2.append("font-style:italic;");
                }
                if ((((Font)object14).getStyle() & 1) != 0) {
                    stringBuilder2.append("font-weight:bold;");
                }
                string = stringBuilder.append(stringBuilder2.toString()).toString();
            }
            object.setAttribute("style", string);
            this.a((Element)object);
            return;
        }
        if (object instanceof FillShapeCommand) {
            object = (FillShapeCommand)object;
            object = this.a((Shape)((Command)object).getValue());
            object.setAttribute("style", this.a(true));
            this.a((Element)object);
        }
    }

    private String a(boolean bl) {
        String string;
        StringBuilder stringBuilder = new StringBuilder();
        Color color = this.a().getColor();
        Object object = color;
        if (color.getColorSpace().getType() == 9) {
            float[] fArray = ((Color)object).getComponents(null);
            string = String.format(null, "rgb(%d,%d,%d) icc-color(Generic-CMYK-profile,%f,%f,%f,%f)", ((Color)object).getRed(), ((Color)object).getGreen(), ((Color)object).getBlue(), Float.valueOf(fArray[0]), Float.valueOf(fArray[1]), Float.valueOf(fArray[2]), Float.valueOf(fArray[3]));
        } else {
            string = String.format(null, "rgb(%d,%d,%d)", ((Color)object).getRed(), ((Color)object).getGreen(), ((Color)object).getBlue());
        }
        object = string;
        double d = (double)color.getAlpha() / 255.0;
        if (bl) {
            SVGDocument.a(stringBuilder, "fill", object);
            if (color.getAlpha() < 255) {
                SVGDocument.a(stringBuilder, "fill-opacity", d);
            }
        } else {
            SVGDocument.a(stringBuilder, "fill", "none");
        }
        if (!bl) {
            Stroke stroke;
            SVGDocument.a(stringBuilder, "stroke", object);
            if (color.getAlpha() < 255) {
                SVGDocument.a(stringBuilder, "stroke-opacity", d);
            }
            if ((stroke = this.a().getStroke()) instanceof BasicStroke) {
                if (((BasicStroke)(stroke = (BasicStroke)stroke)).getLineWidth() != 1.0f) {
                    SVGDocument.a(stringBuilder, "stroke-width", Float.valueOf(((BasicStroke)stroke).getLineWidth()));
                }
                if (((BasicStroke)stroke).getMiterLimit() != 4.0f) {
                    SVGDocument.a(stringBuilder, "stroke-miterlimit", Float.valueOf(((BasicStroke)stroke).getMiterLimit()));
                }
                if (((BasicStroke)stroke).getEndCap() != 0) {
                    SVGDocument.a(stringBuilder, "stroke-linecap", h.get(((BasicStroke)stroke).getEndCap()));
                }
                if (((BasicStroke)stroke).getLineJoin() != 0) {
                    SVGDocument.a(stringBuilder, "stroke-linejoin", i.get(((BasicStroke)stroke).getLineJoin()));
                }
                if (((BasicStroke)stroke).getDashArray() != null) {
                    SVGDocument.a(stringBuilder, "stroke-dasharray", DataUtils.join(",", ((BasicStroke)stroke).getDashArray()));
                    if (((BasicStroke)stroke).getDashPhase() != 0.0f) {
                        SVGDocument.a(stringBuilder, "stroke-dashoffset", Float.valueOf(((BasicStroke)stroke).getDashPhase()));
                    }
                }
            }
        } else {
            SVGDocument.a(stringBuilder, "stroke", "none");
        }
        return stringBuilder.toString();
    }

    private static void a(StringBuilder stringBuilder, String string, Object object) {
        stringBuilder.append(string).append(":").append(DataUtils.format(object)).append(";");
    }

    private static String a(BufferedImage object, String string) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64EncodeStream base64EncodeStream = new Base64EncodeStream(byteArrayOutputStream);
        try {
            ImageIO.write((RenderedImage)object, string, base64EncodeStream);
            base64EncodeStream.close();
            object = byteArrayOutputStream.toString("ISO-8859-1");
            return String.format("data:image/%s;base64,%s", string, object);
        }
        catch (IOException iOException) {
            return "";
        }
    }

    private Element a(Shape object) {
        Element element;
        if (object instanceof Line2D) {
            object = (Line2D)object;
            element = this.b.createElement("line");
            element.setAttribute("x1", DataUtils.format(((Line2D)object).getX1()));
            element.setAttribute("y1", DataUtils.format(((Line2D)object).getY1()));
            element.setAttribute("x2", DataUtils.format(((Line2D)object).getX2()));
            element.setAttribute("y2", DataUtils.format(((Line2D)object).getY2()));
        } else if (object instanceof Rectangle2D) {
            object = (Rectangle2D)object;
            element = this.b.createElement("rect");
            element.setAttribute("x", DataUtils.format(((RectangularShape)object).getX()));
            element.setAttribute("y", DataUtils.format(((RectangularShape)object).getY()));
            element.setAttribute("width", DataUtils.format(((RectangularShape)object).getWidth()));
            element.setAttribute("height", DataUtils.format(((RectangularShape)object).getHeight()));
        } else if (object instanceof RoundRectangle2D) {
            object = (RoundRectangle2D)object;
            element = this.b.createElement("rect");
            element.setAttribute("x", DataUtils.format(((RectangularShape)object).getX()));
            element.setAttribute("y", DataUtils.format(((RectangularShape)object).getY()));
            element.setAttribute("width", DataUtils.format(((RectangularShape)object).getWidth()));
            element.setAttribute("height", DataUtils.format(((RectangularShape)object).getHeight()));
            element.setAttribute("rx", DataUtils.format(((RoundRectangle2D)object).getArcWidth() / 2.0));
            element.setAttribute("ry", DataUtils.format(((RoundRectangle2D)object).getArcHeight() / 2.0));
        } else if (object instanceof Ellipse2D) {
            object = (Ellipse2D)object;
            element = this.b.createElement("ellipse");
            element.setAttribute("cx", DataUtils.format(((RectangularShape)object).getCenterX()));
            element.setAttribute("cy", DataUtils.format(((RectangularShape)object).getCenterY()));
            element.setAttribute("rx", DataUtils.format(((RectangularShape)object).getWidth() / 2.0));
            element.setAttribute("ry", DataUtils.format(((RectangularShape)object).getHeight() / 2.0));
        } else {
            element = this.b.createElement("path");
            StringBuilder stringBuilder = new StringBuilder();
            object = object.getPathIterator(null);
            double[] dArray = new double[6];
            int n = 0;
            while (!object.isDone()) {
                if (n > 0) {
                    stringBuilder.append(" ");
                }
                int n2 = object.currentSegment(dArray);
                switch (n2) {
                    case 0: {
                        stringBuilder.append("M").append(dArray[0]).append(",").append(dArray[1]);
                        break;
                    }
                    case 1: {
                        stringBuilder.append("L").append(dArray[0]).append(",").append(dArray[1]);
                        break;
                    }
                    case 3: {
                        stringBuilder.append("C").append(dArray[0]).append(",").append(dArray[1]).append(" ").append(dArray[2]).append(",").append(dArray[3]).append(" ").append(dArray[4]).append(",").append(dArray[5]);
                        break;
                    }
                    case 2: {
                        stringBuilder.append("Q").append(dArray[0]).append(",").append(dArray[1]).append(" ").append(dArray[2]).append(",").append(dArray[3]);
                        break;
                    }
                    case 4: {
                        stringBuilder.append("Z");
                        break;
                    }
                    default: {
                        throw new IllegalStateException("Unknown path operation.");
                    }
                }
                ++n;
                object.next();
            }
            element.setAttribute("d", stringBuilder.toString());
        }
        return element;
    }
}

