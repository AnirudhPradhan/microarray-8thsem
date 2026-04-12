/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.text.DecimalFormat;
import java.util.Map;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.PlotContent_;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;

public class PlotContent_Pie<ST extends Styler, S extends Series>
extends PlotContent_ {
    PieStyler stylerPie;
    DecimalFormat df = new DecimalFormat("#.0");

    protected PlotContent_Pie(Chart<PieStyler, PieSeries> chart) {
        super(chart);
        this.stylerPie = chart.getStyler();
    }

    @Override
    public void doPaint(Graphics2D g) {
        double pieFillPercentage = this.stylerPie.getPlotContentSize();
        double halfBorderPercentage = (1.0 - pieFillPercentage) / 2.0;
        double width = this.stylerPie.isCircular() ? Math.min(this.getBounds().getWidth(), this.getBounds().getHeight()) : this.getBounds().getWidth();
        double height = this.stylerPie.isCircular() ? Math.min(this.getBounds().getWidth(), this.getBounds().getHeight()) : this.getBounds().getHeight();
        Rectangle2D.Double pieBounds = new Rectangle2D.Double(this.getBounds().getX() + this.getBounds().getWidth() / 2.0 - width / 2.0 + halfBorderPercentage * width, this.getBounds().getY() + this.getBounds().getHeight() / 2.0 - height / 2.0 + halfBorderPercentage * height, width * pieFillPercentage, height * pieFillPercentage);
        double total = 0.0;
        Map map = this.chart.getSeriesMap();
        for (PieSeries series : map.values()) {
            total += series.getValue().doubleValue();
        }
        double startAngle = this.stylerPie.getStartAngleInDegrees() + 90.0;
        map = this.chart.getSeriesMap();
        for (PieSeries series : map.values()) {
            Number y = series.getValue();
            double arcAngle = y.doubleValue() * 360.0 / total;
            g.setColor(series.getFillColor());
            if (PieSeries.PieSeriesRenderStyle.Pie == series.getChartPieSeriesRenderStyle()) {
                g.fill(new Arc2D.Double(((RectangularShape)pieBounds).getX(), ((RectangularShape)pieBounds).getY(), ((RectangularShape)pieBounds).getWidth(), ((RectangularShape)pieBounds).getHeight(), startAngle, arcAngle, 2));
                g.setColor(this.stylerPie.getPlotBackgroundColor());
                g.draw(new Arc2D.Double(((RectangularShape)pieBounds).getX(), ((RectangularShape)pieBounds).getY(), ((RectangularShape)pieBounds).getWidth(), ((RectangularShape)pieBounds).getHeight(), startAngle, arcAngle, 2));
            } else {
                Shape donutSlice = this.getDonutSliceShape(pieBounds, this.stylerPie.getDonutThickness(), startAngle, arcAngle);
                g.fill(donutSlice);
                g.setColor(this.stylerPie.getPlotBackgroundColor());
                g.draw(donutSlice);
            }
            if (this.stylerPie.hasAnnotations().booleanValue()) {
                String annotation = "";
                if (this.stylerPie.getAnnotationType() == PieStyler.AnnotationType.Label) {
                    annotation = series.getName();
                } else if (this.stylerPie.getAnnotationType() == PieStyler.AnnotationType.LabelAndPercentage) {
                    double percentage = y.doubleValue() / total * 100.0;
                    annotation = series.getName() + " (" + this.df.format(percentage) + "%)";
                } else if (this.stylerPie.getAnnotationType() == PieStyler.AnnotationType.Percentage) {
                    double percentage = y.doubleValue() / total * 100.0;
                    annotation = this.df.format(percentage) + "%";
                }
                TextLayout textLayout = new TextLayout(annotation, this.stylerPie.getAnnotationsFont(), new FontRenderContext(null, true, false));
                Rectangle2D annotationRectangle = textLayout.getBounds();
                double xCenter = ((RectangularShape)pieBounds).getX() + ((RectangularShape)pieBounds).getWidth() / 2.0 - annotationRectangle.getWidth() / 2.0;
                double yCenter = ((RectangularShape)pieBounds).getY() + ((RectangularShape)pieBounds).getHeight() / 2.0 + annotationRectangle.getHeight() / 2.0;
                double angle = arcAngle + startAngle - arcAngle / 2.0;
                double xOffset = xCenter + Math.cos(Math.toRadians(angle)) * (((RectangularShape)pieBounds).getWidth() / 2.0 * this.stylerPie.getAnnotationDistance());
                double yOffset = yCenter - Math.sin(Math.toRadians(angle)) * (((RectangularShape)pieBounds).getHeight() / 2.0 * this.stylerPie.getAnnotationDistance());
                Shape shape = textLayout.getOutline(null);
                Rectangle2D annotationBounds = shape.getBounds2D();
                double annotationWidth = annotationBounds.getWidth();
                double annotationHeight = annotationBounds.getHeight();
                double xOffset1 = xCenter + Math.cos(Math.toRadians(startAngle)) * (((RectangularShape)pieBounds).getWidth() / 2.0 * this.stylerPie.getAnnotationDistance());
                double yOffset1 = yCenter - Math.sin(Math.toRadians(startAngle)) * (((RectangularShape)pieBounds).getHeight() / 2.0 * this.stylerPie.getAnnotationDistance());
                double xOffset2 = xCenter + Math.cos(Math.toRadians(arcAngle + startAngle)) * (((RectangularShape)pieBounds).getWidth() / 2.0 * this.stylerPie.getAnnotationDistance());
                double yOffset2 = yCenter - Math.sin(Math.toRadians(arcAngle + startAngle)) * (((RectangularShape)pieBounds).getHeight() / 2.0 * this.stylerPie.getAnnotationDistance());
                double xDiff = Math.abs(xOffset1 - xOffset2);
                double yDiff = Math.abs(yOffset1 - yOffset2);
                boolean annotationWillFit = false;
                if (xDiff >= yDiff) {
                    if (annotationWidth < xDiff) {
                        annotationWillFit = true;
                    }
                } else if (xDiff <= yDiff && annotationHeight < yDiff) {
                    annotationWillFit = true;
                }
                if (this.stylerPie.isDrawAllAnnotations() || annotationWillFit) {
                    g.setColor(this.stylerPie.getChartFontColor());
                    g.setFont(this.stylerPie.getAnnotationsFont());
                    AffineTransform orig = g.getTransform();
                    AffineTransform at = new AffineTransform();
                    if (this.stylerPie.getAnnotationDistance() <= 1.0) {
                        at.translate(xOffset, yOffset);
                    } else {
                        xCenter = ((RectangularShape)pieBounds).getX() + ((RectangularShape)pieBounds).getWidth() / 2.0;
                        yCenter = ((RectangularShape)pieBounds).getY() + ((RectangularShape)pieBounds).getHeight() / 2.0;
                        double endPoint = 3.0 - this.stylerPie.getAnnotationDistance();
                        double xOffsetStart = xCenter + Math.cos(Math.toRadians(angle)) * (((RectangularShape)pieBounds).getWidth() / 2.01);
                        double xOffsetEnd = xCenter + Math.cos(Math.toRadians(angle)) * (((RectangularShape)pieBounds).getWidth() / endPoint);
                        double yOffsetStart = yCenter - Math.sin(Math.toRadians(angle)) * (((RectangularShape)pieBounds).getHeight() / 2.01);
                        double yOffsetEnd = yCenter - Math.sin(Math.toRadians(angle)) * (((RectangularShape)pieBounds).getHeight() / endPoint);
                        g.setStroke(new BasicStroke(2.0f, 0, 0));
                        Line2D.Double line = new Line2D.Double(xOffsetStart, yOffsetStart, xOffsetEnd, yOffsetEnd);
                        g.draw(line);
                        at.translate(xOffset - Math.sin(Math.toRadians(angle - 90.0)) * annotationWidth / 2.0 + 3.0, yOffset);
                    }
                    g.transform(at);
                    g.fill(shape);
                    g.setTransform(orig);
                }
            }
            startAngle += arcAngle;
        }
    }

    private Shape getDonutSliceShape(Rectangle2D pieBounds, double thickness, double start, double extent) {
        GeneralPath generalPath = new GeneralPath();
        GeneralPath dummy = new GeneralPath();
        Arc2D.Double outer = new Arc2D.Double(pieBounds.getX(), pieBounds.getY(), pieBounds.getWidth(), pieBounds.getHeight(), start, extent, 0);
        Arc2D.Double inner = new Arc2D.Double(pieBounds.getX() + pieBounds.getWidth() * (thickness /= 2.0), pieBounds.getY() + pieBounds.getHeight() * thickness, pieBounds.getWidth() - 2.0 * pieBounds.getWidth() * thickness, pieBounds.getHeight() - 2.0 * pieBounds.getHeight() * thickness, start + extent, -extent, 0);
        generalPath.append(outer, false);
        dummy.append(new Arc2D.Double(pieBounds.getX() + pieBounds.getWidth() * thickness, pieBounds.getY() + pieBounds.getHeight() * thickness, pieBounds.getWidth() - 2.0 * pieBounds.getWidth() * thickness, pieBounds.getHeight() - 2.0 * pieBounds.getHeight() * thickness, start, extent, 0), false);
        Point2D point = dummy.getCurrentPoint();
        if (point != null) {
            generalPath.lineTo(point.getX(), point.getY());
        }
        generalPath.append(inner, false);
        dummy.append(new Arc2D.Double(pieBounds.getX(), pieBounds.getY(), pieBounds.getWidth(), pieBounds.getHeight(), start + extent, -extent, 0), false);
        point = dummy.getCurrentPoint();
        generalPath.lineTo(point.getX(), point.getY());
        return generalPath;
    }
}

