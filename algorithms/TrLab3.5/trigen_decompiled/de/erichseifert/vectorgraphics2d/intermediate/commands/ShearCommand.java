/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.AffineTransformCommand;
import java.awt.geom.AffineTransform;

public class ShearCommand
extends AffineTransformCommand {
    private final double a;
    private final double b;

    public ShearCommand(double d, double d2) {
        super(AffineTransform.getShearInstance(d, d2));
        this.a = d;
        this.b = d2;
    }

    public double getShearX() {
        return this.a;
    }

    public double getShearY() {
        return this.b;
    }

    @Override
    public String toString() {
        return String.format(null, "%s[shearX=%f, shearY=%f, value=%s]", this.getClass().getName(), this.getShearX(), this.getShearY(), this.getValue());
    }
}

