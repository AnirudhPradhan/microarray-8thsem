/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.AffineTransformCommand;
import java.awt.geom.AffineTransform;

public class RotateCommand
extends AffineTransformCommand {
    private final double a;
    private final double b;
    private final double c;

    public RotateCommand(double d, double d2, double d3) {
        super(AffineTransform.getRotateInstance(d, d2, d3));
        this.a = d;
        this.b = d2;
        this.c = d3;
    }

    public double getTheta() {
        return this.a;
    }

    public double getCenterX() {
        return this.b;
    }

    public double getCenterY() {
        return this.c;
    }

    @Override
    public String toString() {
        return String.format(null, "%s[theta=%f, centerX=%f, centerY=%f, value=%s]", this.getClass().getName(), this.getTheta(), this.getCenterX(), this.getCenterY(), this.getValue());
    }
}

