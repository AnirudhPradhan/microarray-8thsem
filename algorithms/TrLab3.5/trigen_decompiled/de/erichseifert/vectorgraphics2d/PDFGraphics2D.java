/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d;

import de.erichseifert.vectorgraphics2d.ProcessingPipeline;
import de.erichseifert.vectorgraphics2d.Processor;
import de.erichseifert.vectorgraphics2d.pdf.PDFProcessor;
import java.awt.BasicStroke;
import java.awt.Color;

public class PDFGraphics2D
extends ProcessingPipeline {
    private final PDFProcessor a;

    public PDFGraphics2D(double d, double d2, double d3, double d4) {
        this(d, d2, d3, d4, false);
    }

    public PDFGraphics2D(double d, double d2, double d3, double d4, boolean bl) {
        super(d, d2, d3, d4);
        this.a = new PDFProcessor(bl);
        this.setColor(Color.BLACK);
        this.setStroke(new BasicStroke(1.0f, 0, 0, 10.0f, null, 0.0f));
    }

    @Override
    protected Processor getProcessor() {
        return this.a;
    }

    public boolean isCompressed() {
        return this.a.isCompressed();
    }
}

