/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d;

import de.erichseifert.vectorgraphics2d.ProcessingPipeline;
import de.erichseifert.vectorgraphics2d.Processor;
import de.erichseifert.vectorgraphics2d.svg.SVGProcessor;
import java.awt.Color;

public class SVGGraphics2D
extends ProcessingPipeline {
    private final Processor a = new SVGProcessor();

    public SVGGraphics2D(double d, double d2, double d3, double d4) {
        super(d, d2, d3, d4);
        this.setColor(Color.BLACK);
    }

    @Override
    protected Processor getProcessor() {
        return this.a;
    }
}

