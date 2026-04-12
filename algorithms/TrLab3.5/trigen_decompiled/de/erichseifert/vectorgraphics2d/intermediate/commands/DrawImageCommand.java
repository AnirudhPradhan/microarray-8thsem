/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import java.awt.Image;

public class DrawImageCommand
extends Command<Image> {
    private final int a;
    private final int b;
    private final double c;
    private final double d;
    private final double e;
    private final double f;

    public DrawImageCommand(Image image, int n, int n2, double d, double d2, double d3, double d4) {
        super(image);
        this.a = n;
        this.b = n2;
        this.c = d;
        this.d = d2;
        this.e = d3;
        this.f = d4;
    }

    public int getImageWidth() {
        return this.a;
    }

    public int getImageHeight() {
        return this.b;
    }

    public double getX() {
        return this.c;
    }

    public double getY() {
        return this.d;
    }

    public double getWidth() {
        return this.e;
    }

    public double getHeight() {
        return this.f;
    }

    @Override
    public String toString() {
        return String.format(null, "%s[value=%s, imageWidth=%d, imageHeight=%d, x=%f, y=%f, width=%f, height=%f]", this.getClass().getName(), this.getValue(), this.getImageWidth(), this.getImageHeight(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}

