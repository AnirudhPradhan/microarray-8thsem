/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.custom.StyledText
 *  org.eclipse.swt.graphics.Color
 *  org.eclipse.swt.graphics.Device
 *  org.eclipse.swt.graphics.Drawable
 *  org.eclipse.swt.graphics.FontMetrics
 *  org.eclipse.swt.graphics.GC
 *  org.eclipse.swt.graphics.Point
 *  org.eclipse.swt.graphics.Rectangle
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Canvas
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Control
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.List
 *  org.eclipse.swt.widgets.ProgressBar
 *  org.eclipse.swt.widgets.Scrollable
 *  org.eclipse.swt.widgets.Slider
 *  org.eclipse.swt.widgets.Spinner
 *  org.eclipse.swt.widgets.Table
 *  org.eclipse.swt.widgets.Text
 */
package net.miginfocom.swt;

import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swt.SwtContainerWrapper;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

public class SwtComponentWrapper
implements ComponentWrapper {
    private static Color DB_COMP_OUTLINE = new Color((Device)Display.getCurrent(), 0, 0, 200);
    private static boolean vp = false;
    private static boolean mz = false;
    private final Control c;
    private int compType = -1;

    public SwtComponentWrapper(Control control) {
        this.c = control;
    }

    public final int getBaseline(int n, int n2) {
        return -1;
    }

    public final Object getComponent() {
        return this.c;
    }

    public final float getPixelUnitFactor(boolean bl) {
        switch (PlatformDefaults.getLogicalPixelBase()) {
            case 100: {
                GC gC = new GC((Drawable)this.c);
                FontMetrics fontMetrics = gC.getFontMetrics();
                float f = bl ? (float)fontMetrics.getAverageCharWidth() / 5.0f : (float)fontMetrics.getHeight() / 13.0f;
                gC.dispose();
                return f;
            }
            case 101: {
                Float f;
                Float f2 = f = bl ? PlatformDefaults.getHorizontalScaleFactor() : PlatformDefaults.getVerticalScaleFactor();
                if (f != null) {
                    return f.floatValue();
                }
                return (float)(bl ? this.getHorizontalScreenDPI() : this.getVerticalScreenDPI()) / (float)PlatformDefaults.getDefaultDPI();
            }
        }
        return 1.0f;
    }

    public final int getX() {
        return this.c.getLocation().x;
    }

    public final int getY() {
        return this.c.getLocation().y;
    }

    public final int getWidth() {
        return this.c.getSize().x;
    }

    public final int getHeight() {
        return this.c.getSize().y;
    }

    public final int getScreenLocationX() {
        return this.c.toDisplay((int)0, (int)0).x;
    }

    public final int getScreenLocationY() {
        return this.c.toDisplay((int)0, (int)0).y;
    }

    public final int getMinimumHeight(int n) {
        return mz ? 0 : this.computeSize((boolean)false, (int)n).y;
    }

    public final int getMinimumWidth(int n) {
        return mz ? 0 : this.computeSize((boolean)true, (int)n).x;
    }

    public final int getPreferredHeight(int n) {
        return this.computeSize((boolean)false, (int)n).y;
    }

    public final int getPreferredWidth(int n) {
        return this.computeSize((boolean)true, (int)n).x;
    }

    public final int getMaximumHeight(int n) {
        return Short.MAX_VALUE;
    }

    public final int getMaximumWidth(int n) {
        return Short.MAX_VALUE;
    }

    private Point computeSize(boolean bl, int n) {
        int n2;
        int n3 = bl ? -1 : n;
        int n4 = n2 = !bl ? -1 : n;
        if (n3 != -1 || n2 != -1) {
            int n5 = 0;
            if (this.c instanceof Scrollable) {
                Rectangle rectangle = ((Scrollable)this.c).computeTrim(0, 0, 0, 0);
                n5 = bl ? rectangle.width : rectangle.height;
            } else {
                n5 = this.c.getBorderWidth() << 1;
            }
            if (n3 == -1) {
                n2 = Math.max(0, n2 - n5);
            } else {
                n3 = Math.max(0, n3 - n5);
            }
        }
        return this.c.computeSize(n3, n2);
    }

    public final ContainerWrapper getParent() {
        return new SwtContainerWrapper(this.c.getParent());
    }

    public int getHorizontalScreenDPI() {
        return this.c.getDisplay().getDPI().x;
    }

    public int getVerticalScreenDPI() {
        return this.c.getDisplay().getDPI().y;
    }

    public final int getScreenWidth() {
        return this.c.getDisplay().getBounds().width;
    }

    public final int getScreenHeight() {
        return this.c.getDisplay().getBounds().height;
    }

    public final boolean hasBaseline() {
        return false;
    }

    public final String getLinkId() {
        return null;
    }

    public final void setBounds(int n, int n2, int n3, int n4) {
        this.c.setBounds(n, n2, n3, n4);
    }

    public boolean isVisible() {
        return this.c.getVisible();
    }

    public final int[] getVisualPadding() {
        return null;
    }

    public static boolean isUseVisualPadding() {
        return vp;
    }

    public static void setUseVisualPadding(boolean bl) {
        vp = bl;
    }

    public static boolean isMinimumSizeZero() {
        return mz;
    }

    public static void setMinimumSizeZero(boolean bl) {
        mz = bl;
    }

    public int getLayoutHashCode() {
        String string;
        if (this.c.isDisposed()) {
            return -1;
        }
        Point point = this.c.getSize();
        Point point2 = this.c.computeSize(-1, -1, false);
        int n = point2.x + (point2.y << 12) + (point.x << 22) + (point.y << 16);
        if (this.c.isVisible()) {
            n |= 0x2000000;
        }
        if ((string = this.getLinkId()) != null) {
            n += string.hashCode();
        }
        return n;
    }

    public final void paintDebugOutline() {
        if (this.c.isDisposed()) {
            return;
        }
        GC gC = new GC((Drawable)this.c);
        gC.setLineJoin(1);
        gC.setLineCap(3);
        gC.setLineStyle(3);
        gC.setForeground(DB_COMP_OUTLINE);
        gC.drawRectangle(0, 0, this.getWidth() - 1, this.getHeight() - 1);
        gC.dispose();
    }

    public int getComponetType(boolean bl) {
        if (this.compType == -1) {
            this.compType = this.checkType();
        }
        return this.compType;
    }

    private int checkType() {
        int n = this.c.getStyle();
        if (this.c instanceof Text || this.c instanceof StyledText) {
            return (n & 2) > 0 ? 4 : 3;
        }
        if (this.c instanceof Label) {
            return (n & 2) > 0 ? 18 : 2;
        }
        if (this.c instanceof Button) {
            if ((n & 0x20) > 0 || (n & 0x10) > 0) {
                return 16;
            }
            return 5;
        }
        if (this.c instanceof Canvas) {
            return 10;
        }
        if (this.c instanceof List) {
            return 6;
        }
        if (this.c instanceof Table) {
            return 7;
        }
        if (this.c instanceof Spinner) {
            return 13;
        }
        if (this.c instanceof ProgressBar) {
            return 14;
        }
        if (this.c instanceof Slider) {
            return 12;
        }
        if (this.c instanceof Composite) {
            return 1;
        }
        return 0;
    }

    public final int hashCode() {
        return this.c.hashCode();
    }

    public final boolean equals(Object object) {
        if (object == null || !(object instanceof ComponentWrapper)) {
            return false;
        }
        return this.getComponent().equals(((ComponentWrapper)object).getComponent());
    }
}

