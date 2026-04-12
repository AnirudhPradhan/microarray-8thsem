/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Control
 */
package net.miginfocom.swt;

import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.swt.SwtComponentWrapper;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public final class SwtContainerWrapper
extends SwtComponentWrapper
implements ContainerWrapper {
    public SwtContainerWrapper(Composite composite) {
        super((Control)composite);
    }

    public ComponentWrapper[] getComponents() {
        Composite composite = (Composite)this.getComponent();
        Control[] controlArray = composite.getChildren();
        ComponentWrapper[] componentWrapperArray = new ComponentWrapper[controlArray.length];
        for (int i = 0; i < componentWrapperArray.length; ++i) {
            componentWrapperArray[i] = new SwtComponentWrapper(controlArray[i]);
        }
        return componentWrapperArray;
    }

    public int getComponentCount() {
        return ((Composite)this.getComponent()).getChildren().length;
    }

    public Object getLayout() {
        return ((Composite)this.getComponent()).getLayout();
    }

    public final boolean isLeftToRight() {
        return (((Composite)this.getComponent()).getStyle() & 0x2000000) > 0;
    }

    public final void paintDebugCell(int n, int n2, int n3, int n4) {
    }

    public int getComponetType(boolean bl) {
        return 1;
    }

    public int getLayoutHashCode() {
        int n = super.getLayoutHashCode();
        if (this.isLeftToRight()) {
            n |= 0x4000000;
        }
        return n;
    }
}

