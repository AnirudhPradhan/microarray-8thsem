/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.ui;

import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawingContext;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Dimension2D;
import javax.swing.JPanel;

public class DrawablePanel
extends JPanel {
    private static final long serialVersionUID = 1036506991203257170L;
    private final Drawable a;
    private boolean b;

    public DrawablePanel(Drawable drawable) {
        this.a = drawable;
        this.setOpaque(false);
        this.b = true;
    }

    public Drawable getDrawable() {
        return this.a;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (this.isVisible()) {
            graphics = (Graphics2D)graphics;
            if (this.isAntialiased()) {
                ((Graphics2D)graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            this.getDrawable().draw(new DrawingContext((Graphics2D)graphics));
        }
    }

    @Override
    public void setBounds(Rectangle rectangle) {
        super.setBounds(rectangle);
        this.getDrawable().setBounds(rectangle);
    }

    @Override
    public void setBounds(int n, int n2, int n3, int n4) {
        super.setBounds(n, n2, n3, n4);
        this.getDrawable().setBounds(0.0, 0.0, n3, n4);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dimension = super.getPreferredSize();
        Dimension2D dimension2D = this.getDrawable().getPreferredSize();
        dimension.setSize(dimension2D);
        return dimension;
    }

    @Override
    public Dimension getMinimumSize() {
        return super.getPreferredSize();
    }

    public boolean isAntialiased() {
        return this.b;
    }

    public void setAntialiased(boolean bl) {
        this.b = bl;
    }
}

