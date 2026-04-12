/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d;

import de.erichseifert.vectorgraphics2d.ProcessingPipeline;
import de.erichseifert.vectorgraphics2d.Processor;
import de.erichseifert.vectorgraphics2d.eps.EPSProcessor;
import java.awt.BasicStroke;
import java.awt.Color;

public class EPSGraphics2D
extends ProcessingPipeline {
    private final Processor a = new EPSProcessor();

    public EPSGraphics2D(double d, double d2, double d3, double d4) {
        super(d, d2, d3, d4);
        this.setColor(Color.BLACK);
        this.setStroke(new BasicStroke(1.0f, 0, 0, 10.0f, null, 0.0f));
    }

    @Override
    protected Processor getProcessor() {
        return this.a;
    }
}

