/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.swing.SwingComponentWrapper;

public final class SwingContainerWrapper
extends SwingComponentWrapper
implements ContainerWrapper {
    private static final Color DB_CELL_OUTLINE = new Color(255, 0, 0);

    public SwingContainerWrapper(Container container) {
        super(container);
    }

    public ComponentWrapper[] getComponents() {
        Container container = (Container)this.getComponent();
        ComponentWrapper[] componentWrapperArray = new ComponentWrapper[container.getComponentCount()];
        for (int i = 0; i < componentWrapperArray.length; ++i) {
            componentWrapperArray[i] = new SwingComponentWrapper(container.getComponent(i));
        }
        return componentWrapperArray;
    }

    public int getComponentCount() {
        return ((Container)this.getComponent()).getComponentCount();
    }

    public Object getLayout() {
        return ((Container)this.getComponent()).getLayout();
    }

    public final boolean isLeftToRight() {
        return ((Container)this.getComponent()).getComponentOrientation().isLeftToRight();
    }

    public final void paintDebugCell(int n, int n2, int n3, int n4) {
        Component component = (Component)this.getComponent();
        if (!component.isShowing()) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D)component.getGraphics();
        if (graphics2D == null) {
            return;
        }
        graphics2D.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, new float[]{2.0f, 3.0f}, 0.0f));
        graphics2D.setPaint(DB_CELL_OUTLINE);
        graphics2D.drawRect(n, n2, n3 - 1, n4 - 1);
    }

    public int getComponetType(boolean bl) {
        return 1;
    }

    public int getLayoutHashCode() {
        int n = super.getLayoutHashCode();
        if (this.isLeftToRight()) {
            n += 416343;
        }
        return 0;
    }
}

