/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.filters;

import de.erichseifert.vectorgraphics2d.intermediate.commands.AffineTransformCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import de.erichseifert.vectorgraphics2d.intermediate.commands.CreateCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.DisposeCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetTransformCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.TransformCommand;
import de.erichseifert.vectorgraphics2d.intermediate.filters.Filter;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class AbsoluteToRelativeTransformsFilter
extends Filter {
    private Stack<AffineTransform> a = new Stack();

    public AbsoluteToRelativeTransformsFilter(Iterable<Command<?>> iterable) {
        super(iterable);
    }

    @Override
    public Command<?> next() {
        Object object = super.next();
        if (object instanceof AffineTransformCommand) {
            AffineTransformCommand affineTransformCommand = (AffineTransformCommand)object;
            this.a().concatenate((AffineTransform)affineTransformCommand.getValue());
        } else if (object instanceof CreateCommand) {
            AffineTransform affineTransform = this.a.isEmpty() ? new AffineTransform() : new AffineTransform(this.a());
            this.a.push(affineTransform);
        } else if (object instanceof DisposeCommand) {
            this.a.pop();
        }
        return object;
    }

    @Override
    protected List<Command<?>> filter(Command<?> object) {
        if (object instanceof SetTransformCommand) {
            Object object2;
            object = (SetTransformCommand)object;
            object = (AffineTransform)((Command)object).getValue();
            AffineTransform affineTransform = new AffineTransform();
            try {
                object2 = this.a().createInverse();
                affineTransform.concatenate((AffineTransform)object2);
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                object2 = noninvertibleTransformException;
                noninvertibleTransformException.printStackTrace();
            }
            affineTransform.concatenate((AffineTransform)object);
            object2 = new TransformCommand(affineTransform);
            return Arrays.asList(object2);
        }
        return Arrays.asList(object);
    }

    private AffineTransform a() {
        return this.a.peek();
    }
}

