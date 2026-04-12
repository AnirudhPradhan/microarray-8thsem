/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.AffineTransformCommand;
import java.awt.geom.AffineTransform;

public class ScaleCommand
extends AffineTransformCommand {
    private final double a;
    private final double b;

    public ScaleCommand(double d, double d2) {
        super(AffineTransform.getScaleInstance(d, d2));
        this.a = d;
        this.b = d2;
    }

    public double getScaleX() {
        return this.a;
    }

    public double getScaleY() {
        return this.b;
    }

    @Override
    public String toString() {
        return String.format(null, "%s[scaleX=%f, scaleY=%f, value=%s]", this.getClass().getName(), this.getScaleX(), this.getScaleY(), this.getValue());
    }
}

