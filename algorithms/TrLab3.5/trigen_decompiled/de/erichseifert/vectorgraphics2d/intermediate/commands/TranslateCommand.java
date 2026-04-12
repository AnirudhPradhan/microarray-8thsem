/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.AffineTransformCommand;
import java.awt.geom.AffineTransform;

public class TranslateCommand
extends AffineTransformCommand {
    private final double a;
    private final double b;

    public TranslateCommand(double d, double d2) {
        super(AffineTransform.getTranslateInstance(d, d2));
        this.a = d;
        this.b = d2;
    }

    public double getDeltaX() {
        return this.a;
    }

    public double getDeltaY() {
        return this.b;
    }

    @Override
    public String toString() {
        return String.format(null, "%s[deltaX=%f, deltaY=%f, value=%s]", this.getClass().getName(), this.getDeltaX(), this.getDeltaY(), this.getValue());
    }
}

