/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.eps;

import de.erichseifert.vectorgraphics2d.GraphicsState;
import de.erichseifert.vectorgraphics2d.SizedDocument;
import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import de.erichseifert.vectorgraphics2d.intermediate.commands.CreateCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DisposeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DrawImageCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DrawShapeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DrawStringCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.FillShapeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.RotateCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.ScaleCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetClipCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetColorCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetCompositeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetFontCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetPaintCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetStrokeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetTransformCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.ShearCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.TransformCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.TranslateCommand;
import de.erichseifert.vectorgraphics2d.util.ASCII85EncodeStream;
import de.erichseifert.vectorgraphics2d.util.AlphaToMaskOp;
import de.erichseifert.vectorgraphics2d.util.DataUtils;
import de.erichseifert.vectorgraphics2d.util.FlateEncodeStream;
import de.erichseifert.vectorgraphics2d.util.GraphicsUtils;
import de.erichseifert.vectorgraphics2d.util.ImageDataStream;
import de.erichseifert.vectorgraphics2d.util.LineWrapOutputStream;
import de.erichseifert.vectorgraphics2d.util.PageSize;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EPSDocument
extends SizedDocument {
    private static final Pattern a = Pattern.compile("(.{1,255})(\\s+|$)");
    private static final Map<Integer, Integer> b = DataUtils.map(new Integer[]{0, 1, 2}, new Integer[]{0, 1, 2});
    private static final Map<Integer, Integer> c = DataUtils.map(new Integer[]{0, 1, 2}, new Integer[]{0, 1, 2});
    private final List<String> d = new LinkedList<String>();

    public EPSDocument(PageSize object) {
        super((PageSize)object);
        object = this;
        double d = ((SizedDocument)object).getPageSize().x * 2.834645669291339;
        double d2 = ((SizedDocument)object).getPageSize().y * 2.834645669291339;
        double d3 = ((SizedDocument)object).getPageSize().width * 2.834645669291339;
        double d4 = ((SizedDocument)object).getPageSize().height * 2.834645669291339;
        ((EPSDocument)object).d.addAll(Arrays.asList("%!PS-Adobe-3.0 EPSF-3.0", "%%BoundingBox: " + (int)Math.floor(d) + " " + (int)Math.floor(d2) + " " + (int)Math.ceil(d + d3) + " " + (int)Math.ceil(d2 + d4), "%%HiResBoundingBox: " + d + " " + d2 + " " + (d + d3) + " " + (d2 + d4), "%%LanguageLevel: 3", "%%Pages: 1", "%%EndComments", "%%Page: 1 1", "/M /moveto load def", "/L /lineto load def", "/C /curveto load def", "/Z /closepath load def", "/RL /rlineto load def", "/rgb /setrgbcolor load def", "/cmyk /setcmykcolor load def", "/rect { /height exch def /width exch def /y exch def /x exch def x y M width 0 RL 0 height RL width neg 0 RL } bind def", "/ellipse { /endangle exch def /startangle exch def /ry exch def /rx exch def /y exch def /x exch def /savematrix matrix currentmatrix def x y translate rx ry scale 0 0 1 startangle endangle arcn savematrix setmatrix } bind def", "/imgdict { /datastream exch def /hasdata exch def /decodeScale exch def /bits exch def /bands exch def /imgheight exch def /imgwidth exch def << /ImageType 1 /Width imgwidth /Height imgheight /BitsPerComponent bits /Decode [bands {0 decodeScale} repeat] /ImageMatrix [imgwidth 0 0 imgheight 0 0] hasdata { /DataSource datastream } if >> } bind def", "/latinize { /fontName exch def /fontNameNew exch def fontName findfont 0 dict copy begin /Encoding ISOLatin1Encoding def fontNameNew /FontName def currentdict end dup /FID undef fontNameNew exch definefont pop } bind def", EPSDocument.a(GraphicsState.DEFAULT_FONT), "gsave", "clipsave", "/DeviceRGB setcolorspace", "0 " + d4 + " translate", "2.834645669291339 -2.834645669291339 scale", "/basematrix matrix currentmatrix def"));
    }

    @Override
    public void write(OutputStream closeable) throws IOException {
        closeable = new OutputStreamWriter((OutputStream)closeable, "ISO-8859-1");
        for (String string : this.d) {
            if (string == null) continue;
            Matcher matcher = a.matcher(string);
            boolean bl = false;
            while (matcher.find()) {
                bl = true;
                String string2 = matcher.group();
                ((OutputStreamWriter)closeable).write(string2, 0, string2.length());
                ((OutputStreamWriter)closeable).append("\n");
            }
            if (bl) continue;
            System.err.println("Unable to divide eps element into lines: " + string);
        }
        ((OutputStreamWriter)closeable).append("%%EOF");
        ((OutputStreamWriter)closeable).flush();
    }

    @Override
    public void handle(Command<?> object) {
        if (object instanceof SetClipCommand) {
            object = (SetClipCommand)object;
            Shape shape = (Shape)((Command)object).getValue();
            this.d.add("cliprestore");
            if (shape != null) {
                this.d.add(EPSDocument.a(shape) + " clip");
            }
            return;
        }
        if (object instanceof SetColorCommand) {
            String string;
            object = (SetColorCommand)object;
            if (((Color)(object = (Color)((Command)object).getValue())).getColorSpace().getType() == 9) {
                float[] fArray = ((Color)object).getComponents(null);
                string = String.format(null, "%f %f %f %f cmyk", Float.valueOf(fArray[0]), Float.valueOf(fArray[1]), Float.valueOf(fArray[2]), Float.valueOf(fArray[3]));
            } else {
                string = String.format(null, "%f %f %f rgb", Float.valueOf((float)((Color)object).getRed() / 255.0f), Float.valueOf((float)((Color)object).getGreen() / 255.0f), Float.valueOf((float)((Color)object).getBlue() / 255.0f));
            }
            this.d.add(string);
            return;
        }
        if (object instanceof SetCompositeCommand) {
            object = (SetCompositeCommand)object;
            this.d.add("% composite not yet implemented: " + ((Command)object).getValue());
            return;
        }
        if (object instanceof SetFontCommand) {
            object = (SetFontCommand)object;
            this.d.add(EPSDocument.a((Font)((Command)object).getValue()));
            return;
        }
        if (object instanceof SetPaintCommand) {
            object = (SetPaintCommand)object;
            this.d.add("% paint not yet implemented: " + ((Command)object).getValue());
            return;
        }
        if (object instanceof SetStrokeCommand) {
            object = (SetStrokeCommand)object;
            object = (Stroke)((Command)object).getValue();
            StringBuilder stringBuilder = new StringBuilder();
            if (object instanceof BasicStroke) {
                BasicStroke basicStroke = (BasicStroke)object;
                stringBuilder.append(basicStroke.getLineWidth()).append(" setlinewidth ").append(c.get(basicStroke.getLineJoin())).append(" setlinejoin ").append(b.get(basicStroke.getEndCap())).append(" setlinecap [").append(DataUtils.join(" ", basicStroke.getDashArray())).append("] ").append(basicStroke.getDashPhase()).append(" setdash");
            } else {
                stringBuilder.append("% Custom strokes aren't supported at the moment");
            }
            this.d.add(stringBuilder.toString());
            return;
        }
        if (object instanceof SetTransformCommand) {
            object = (SetTransformCommand)object;
            StringBuilder stringBuilder = new StringBuilder();
            double[] dArray = new double[6];
            ((AffineTransform)((Command)object).getValue()).getMatrix(dArray);
            stringBuilder.append("basematrix setmatrix [").append(DataUtils.join(" ", dArray)).append("] concat");
            this.d.add(stringBuilder.toString());
            return;
        }
        if (object instanceof RotateCommand) {
            object = (RotateCommand)object;
            StringBuilder stringBuilder = new StringBuilder();
            double d = ((RotateCommand)object).getCenterX();
            double d2 = ((RotateCommand)object).getCenterY();
            boolean bl = d != 0.0 || d2 != 0.0;
            if (bl) {
                stringBuilder.append(d).append(" ").append(d2).append(" translate ");
            }
            stringBuilder.append(Math.toDegrees(((RotateCommand)object).getTheta())).append(" rotate");
            if (bl) {
                stringBuilder.append(" ");
                stringBuilder.append(-d).append(" ").append(-d2).append(" translate");
            }
            this.d.add(stringBuilder.toString());
            return;
        }
        if (object instanceof ScaleCommand) {
            object = (ScaleCommand)object;
            this.d.add(DataUtils.format(((ScaleCommand)object).getScaleX()) + " " + DataUtils.format(((ScaleCommand)object).getScaleY()) + " scale");
            return;
        }
        if (object instanceof ShearCommand) {
            object = (ShearCommand)object;
            this.d.add("[1 " + DataUtils.format(((ShearCommand)object).getShearY()) + " " + DataUtils.format(((ShearCommand)object).getShearX()) + " 1 0 0] concat");
            return;
        }
        if (object instanceof TransformCommand) {
            object = (TransformCommand)object;
            StringBuilder stringBuilder = new StringBuilder();
            double[] dArray = new double[6];
            ((AffineTransform)((Command)object).getValue()).getMatrix(dArray);
            stringBuilder.append("[").append(DataUtils.join(" ", dArray)).append("] concat");
            this.d.add(stringBuilder.toString());
            return;
        }
        if (object instanceof TranslateCommand) {
            object = (TranslateCommand)object;
            this.d.add(String.valueOf(((TranslateCommand)object).getDeltaX()) + " " + ((TranslateCommand)object).getDeltaY() + " translate");
            return;
        }
        if (object instanceof DrawImageCommand) {
            object = (DrawImageCommand)object;
            double d = ((DrawImageCommand)object).getHeight();
            double d3 = ((DrawImageCommand)object).getWidth();
            double d4 = ((DrawImageCommand)object).getY();
            double d5 = ((DrawImageCommand)object).getX();
            int n = ((DrawImageCommand)object).getImageHeight();
            int n2 = ((DrawImageCommand)object).getImageWidth();
            object = (Image)((Command)object).getValue();
            CharSequence charSequence = new StringBuilder();
            object = GraphicsUtils.toBufferedImage((Image)object);
            int n3 = ((BufferedImage)object).getSampleModel().getNumBands();
            int n4 = DataUtils.max(((BufferedImage)object).getSampleModel().getSampleSize());
            n4 = (int)(Math.ceil((double)n4 / 8.0) * 8.0);
            if (n3 > 3) {
                n3 = 3;
            }
            charSequence.append("gsave\n");
            if (d5 != 0.0 || d4 != 0.0) {
                charSequence.append(d5).append(" ").append(d4).append(" translate\n");
            }
            if (d3 != 1.0 || d != 1.0) {
                charSequence.append(d3).append(" ").append(d).append(" scale\n");
            }
            int n5 = 1;
            if (((BufferedImage)object).getColorModel().hasAlpha()) {
                charSequence.append("<< /ImageType 3 /InterleaveType 1 /MaskDict ").append(n2).append(" ").append(n).append(" 1").append(" ").append(n4).append(" 1").append(" false").append(" 0").append(" imgdict /DataDict ").append(n2).append(" ").append(n).append(" ").append(n3).append(" ").append(n4).append(" 1").append(" true").append(" currentfile /ASCII85Decode filter << /BitsPerComponent ").append(n4).append(" >> /FlateDecode filter ").append("imgdict >> image").append("\n");
                object = new AlphaToMaskOp(true).filter((BufferedImage)object, null);
                EPSDocument.a((BufferedImage)object, charSequence);
            } else {
                if (n3 == 1) {
                    charSequence.append("/DeviceGray setcolorspace\n");
                }
                if (((BufferedImage)object).getType() == 12) {
                    n5 = 255;
                }
                charSequence.append(n2).append(" ").append(n).append(" ").append(n3).append(" ").append(n4).append(" ").append(n5).append(" true").append(" currentfile /ASCII85Decode filter << /BitsPerComponent ").append(n4).append(" >> /FlateDecode filter ").append("imgdict image").append("\n");
                EPSDocument.a((BufferedImage)object, charSequence);
            }
            charSequence.append("grestore");
            charSequence = charSequence.toString();
            this.d.add((String)charSequence);
            return;
        }
        if (object instanceof DrawShapeCommand) {
            object = (DrawShapeCommand)object;
            this.d.add(EPSDocument.a((Shape)((Command)object).getValue()) + " stroke");
            return;
        }
        if (object instanceof DrawStringCommand) {
            object = (DrawStringCommand)object;
            double d = ((DrawStringCommand)object).getY();
            double d6 = ((DrawStringCommand)object).getX();
            object = (String)((Command)object).getValue();
            StringBuilder stringBuilder = new StringBuilder("gsave 1 -1 scale ").append(d6).append(" ").append(-d).append(" M ");
            Object object2 = object;
            StringBuilder stringBuilder2 = new StringBuilder();
            object2 = ((String)object2).replaceAll("\\\\", "\\\\\\\\").replaceAll("\t", "\\\\t").replaceAll("\b", "\\\\b").replaceAll("\f", "\\\\f").replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)").replaceAll("[\r\n]", "");
            stringBuilder2.append("(").append((String)object2).append(")");
            this.d.add(stringBuilder.append((Object)stringBuilder2).append(" show grestore").toString());
            return;
        }
        if (object instanceof FillShapeCommand) {
            object = (FillShapeCommand)object;
            this.d.add(EPSDocument.a((Shape)((Command)object).getValue()) + " fill");
            return;
        }
        if (object instanceof CreateCommand) {
            this.d.add("gsave");
            return;
        }
        if (object instanceof DisposeCommand) {
            this.d.add("grestore");
        }
    }

    private static String a(Shape object) {
        StringBuilder stringBuilder;
        block10: {
            block13: {
                double d;
                double d2;
                block14: {
                    block12: {
                        block11: {
                            block9: {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("newpath ");
                                if (!(object instanceof Line2D)) break block9;
                                object = (Line2D)object;
                                stringBuilder.append(((Line2D)object).getX1()).append(" ").append(((Line2D)object).getY1()).append(" M ").append(((Line2D)object).getX2()).append(" ").append(((Line2D)object).getY2()).append(" L");
                                break block10;
                            }
                            if (!(object instanceof Rectangle2D)) break block11;
                            object = (Rectangle2D)object;
                            stringBuilder.append(((RectangularShape)object).getX()).append(" ").append(((RectangularShape)object).getY()).append(" ").append(((RectangularShape)object).getWidth()).append(" ").append(((RectangularShape)object).getHeight()).append(" rect Z");
                            break block10;
                        }
                        if (!(object instanceof Ellipse2D)) break block12;
                        object = (Ellipse2D)object;
                        double d3 = ((RectangularShape)object).getX() + ((RectangularShape)object).getWidth() / 2.0;
                        double d4 = ((RectangularShape)object).getY() + ((RectangularShape)object).getHeight() / 2.0;
                        double d5 = ((RectangularShape)object).getWidth() / 2.0;
                        double d6 = ((RectangularShape)object).getHeight() / 2.0;
                        stringBuilder.append(d3).append(" ").append(d4).append(" ").append(d5).append(" ").append(d6).append(" 360.0").append(" 0.0").append(" ellipse Z");
                        break block10;
                    }
                    if (!(object instanceof Arc2D)) break block13;
                    object = (Arc2D)object;
                    d2 = ((RectangularShape)object).getX() + ((RectangularShape)object).getWidth() / 2.0;
                    d = ((RectangularShape)object).getY() + ((RectangularShape)object).getHeight() / 2.0;
                    double d7 = ((RectangularShape)object).getWidth() / 2.0;
                    double d8 = ((RectangularShape)object).getHeight() / 2.0;
                    double d9 = -((Arc2D)object).getAngleStart();
                    double d10 = -(((Arc2D)object).getAngleStart() + ((Arc2D)object).getAngleExtent());
                    stringBuilder.append(d2).append(" ").append(d).append(" ").append(d7).append(" ").append(d8).append(" ").append(d9).append(" ").append(d10).append(" ellipse");
                    if (((Arc2D)object).getArcType() != 1) break block14;
                    stringBuilder.append(" Z");
                    break block10;
                }
                if (((Arc2D)object).getArcType() != 2) break block10;
                stringBuilder.append(" ").append(d2).append(" ").append(d).append(" L Z");
                break block10;
            }
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
                        stringBuilder.append(dArray[0]).append(" ").append(dArray[1]).append(" M");
                        dArray2[0] = dArray[0];
                        dArray2[1] = dArray[1];
                        break;
                    }
                    case 1: {
                        stringBuilder.append(dArray[0]).append(" ").append(dArray[1]).append(" L");
                        dArray2[0] = dArray[0];
                        dArray2[1] = dArray[1];
                        break;
                    }
                    case 3: {
                        stringBuilder.append(dArray[0]).append(" ").append(dArray[1]).append(" ").append(dArray[2]).append(" ").append(dArray[3]).append(" ").append(dArray[4]).append(" ").append(dArray[5]).append(" C");
                        dArray2[0] = dArray[4];
                        dArray2[1] = dArray[5];
                        break;
                    }
                    case 2: {
                        double d = dArray2[0] + 0.6666666666666666 * (dArray[0] - dArray2[0]);
                        double d11 = dArray2[1] + 0.6666666666666666 * (dArray[1] - dArray2[1]);
                        double d12 = dArray[0] + 0.3333333333333333 * (dArray[2] - dArray[0]);
                        double d13 = dArray[1] + 0.3333333333333333 * (dArray[3] - dArray[1]);
                        double d14 = dArray[2];
                        double d15 = dArray[3];
                        stringBuilder.append(d).append(" ").append(d11).append(" ").append(d12).append(" ").append(d13).append(" ").append(d14).append(" ").append(d15).append(" C");
                        dArray2[0] = d14;
                        dArray2[1] = d15;
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
        }
        return stringBuilder.toString();
    }

    private static void a(BufferedImage object, StringBuilder stringBuilder) {
        object = new ImageDataStream((BufferedImage)object, ImageDataStream.Interleaving.SAMPLE);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FlateEncodeStream flateEncodeStream = new FlateEncodeStream(new ASCII85EncodeStream(new LineWrapOutputStream(byteArrayOutputStream, 80)));
        try {
            DataUtils.transfer((InputStream)object, flateEncodeStream, 1024);
            ((OutputStream)flateEncodeStream).close();
            object = byteArrayOutputStream.toString("ISO-8859-1");
            stringBuilder.append((String)object).append("\n");
            return;
        }
        catch (IOException iOException) {
            object = iOException;
            iOException.printStackTrace();
            return;
        }
    }

    private static String a(Font font) {
        StringBuilder stringBuilder = new StringBuilder();
        font = GraphicsUtils.getPhysicalFont(font);
        String string = font.getPSName();
        string = string + "Lat";
        stringBuilder.append("/").append(string).append(" /").append(font.getPSName()).append(" latinize ");
        stringBuilder.append("/").append(string).append(" ").append(font.getSize2D()).append(" selectfont");
        return stringBuilder.toString();
    }
}

