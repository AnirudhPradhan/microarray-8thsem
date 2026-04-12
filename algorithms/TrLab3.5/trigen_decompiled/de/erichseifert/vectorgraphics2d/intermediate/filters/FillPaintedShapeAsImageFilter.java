/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.filters;

import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DisposeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DrawImageCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.FillShapeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetPaintCommand;
import de.erichseifert.vectorgraphics2d.intermediate.filters.Filter;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class FillPaintedShapeAsImageFilter
extends Filter {
    private SetPaintCommand a;

    public FillPaintedShapeAsImageFilter(Iterable<Command<?>> iterable) {
        super(iterable);
    }

    @Override
    public Command<?> next() {
        Object object = super.next();
        if (object instanceof SetPaintCommand) {
            this.a = (SetPaintCommand)object;
        } else if (object instanceof DisposeCommand) {
            this.a = null;
        }
        return object;
    }

    @Override
    protected List<Command<?>> filter(Command<?> object) {
        if (this.a != null && object instanceof FillShapeCommand) {
            FillShapeCommand fillShapeCommand = (FillShapeCommand)object;
            object = fillShapeCommand;
            SetPaintCommand setPaintCommand = this.a;
            object = fillShapeCommand;
            object = (Shape)fillShapeCommand.getValue();
            Rectangle2D rectangle2D = object.getBounds2D();
            double d = rectangle2D.getX();
            double d2 = rectangle2D.getY();
            double d3 = rectangle2D.getWidth();
            double d4 = rectangle2D.getHeight();
            int n = (int)Math.round(d3);
            int n2 = (int)Math.round(d4);
            BufferedImage bufferedImage = new BufferedImage(n, n2, 2);
            Graphics2D graphics2D = (Graphics2D)bufferedImage.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.scale((double)n / d3, (double)n2 / d4);
            graphics2D.translate(-rectangle2D.getX(), -rectangle2D.getY());
            graphics2D.setPaint((Paint)setPaintCommand.getValue());
            graphics2D.fill((Shape)object);
            graphics2D.dispose();
            object = new DrawImageCommand(bufferedImage, n, n2, d, d2, d3, d4);
            return Arrays.asList(object);
        }
        return Arrays.asList(object);
    }
}

