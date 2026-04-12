/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;

public class DrawStringCommand
extends Command<String> {
    private final double a;
    private final double b;

    public DrawStringCommand(String string, double d, double d2) {
        super(string);
        this.a = d;
        this.b = d2;
    }

    public double getX() {
        return this.a;
    }

    public double getY() {
        return this.b;
    }

    @Override
    public String toString() {
        return String.format(null, "%s[value=%s, x=%f, y=%f]", this.getClass().getName(), this.getValue(), this.getX(), this.getY());
    }
}

