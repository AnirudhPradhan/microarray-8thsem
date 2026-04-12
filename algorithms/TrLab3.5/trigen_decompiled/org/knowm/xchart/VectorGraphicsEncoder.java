/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import de.erichseifert.vectorgraphics2d.EPSGraphics2D;
import de.erichseifert.vectorgraphics2d.PDFGraphics2D;
import de.erichseifert.vectorgraphics2d.ProcessingPipeline;
import de.erichseifert.vectorgraphics2d.SVGGraphics2D;
import java.io.FileOutputStream;
import java.io.IOException;
import org.knowm.xchart.internal.chartpart.Chart;

public final class VectorGraphicsEncoder {
    private VectorGraphicsEncoder() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void saveVectorGraphic(Chart chart, String fileName, VectorGraphicsFormat vectorGraphicsFormat) throws IOException {
        ProcessingPipeline g = null;
        switch (vectorGraphicsFormat) {
            case EPS: {
                g = new EPSGraphics2D(0.0, 0.0, chart.getWidth(), chart.getHeight());
                break;
            }
            case PDF: {
                g = new PDFGraphics2D(0.0, 0.0, chart.getWidth(), chart.getHeight());
                break;
            }
            case SVG: {
                g = new SVGGraphics2D(0.0, 0.0, chart.getWidth(), chart.getHeight());
                break;
            }
        }
        chart.paint(g, chart.getWidth(), chart.getHeight());
        FileOutputStream file = new FileOutputStream(fileName + "." + vectorGraphicsFormat.toString().toLowerCase());
        try {
            file.write(g.getBytes());
        }
        finally {
            file.close();
        }
    }

    public static enum VectorGraphicsFormat {
        EPS,
        PDF,
        SVG;

    }
}

