/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.swing;

import java.awt.BasicStroke;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.List;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swing.SwingContainerWrapper;

public class SwingComponentWrapper
implements ComponentWrapper {
    private static boolean maxSet = false;
    private static boolean vp = true;
    private static final Color DB_COMP_OUTLINE = new Color(0, 0, 200);
    private final Component c;
    private int compType = -1;
    private Boolean bl = null;
    private boolean prefCalled = false;
    private static final IdentityHashMap<FontMetrics, Point2D.Float> FM_MAP = new IdentityHashMap(4);
    private static final Font SUBST_FONT = new Font("sansserif", 0, 11);
    private static Method BL_METHOD = null;
    private static Method BL_RES_METHOD = null;
    private static Method IMS_METHOD;

    public SwingComponentWrapper(Component component) {
        this.c = component;
    }

    public final int getBaseline(int n, int n2) {
        if (BL_METHOD == null) {
            return -1;
        }
        try {
            Object[] objectArray = new Object[]{new Integer(n < 0 ? this.c.getWidth() : n), new Integer(n2 < 0 ? this.c.getHeight() : n2)};
            return (Integer)BL_METHOD.invoke((Object)this.c, objectArray);
        }
        catch (Exception exception) {
            return -1;
        }
    }

    public final Object getComponent() {
        return this.c;
    }

    public final float getPixelUnitFactor(boolean bl) {
        switch (PlatformDefaults.getLogicalPixelBase()) {
            case 100: {
                Font font = this.c.getFont();
                FontMetrics fontMetrics = this.c.getFontMetrics(font != null ? font : SUBST_FONT);
                Point2D.Float float_ = FM_MAP.get(fontMetrics);
                if (float_ == null) {
                    Rectangle2D rectangle2D = fontMetrics.getStringBounds("X", this.c.getGraphics());
                    float_ = new Point2D.Float((float)rectangle2D.getWidth() / 6.0f, (float)rectangle2D.getHeight() / 13.277344f);
                    FM_MAP.put(fontMetrics, float_);
                }
                return bl ? float_.x : float_.y;
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
        return this.c.getX();
    }

    public final int getY() {
        return this.c.getY();
    }

    public final int getHeight() {
        return this.c.getHeight();
    }

    public final int getWidth() {
        return this.c.getWidth();
    }

    public final int getScreenLocationX() {
        Point point = new Point();
        SwingUtilities.convertPointToScreen(point, this.c);
        return point.x;
    }

    public final int getScreenLocationY() {
        Point point = new Point();
        SwingUtilities.convertPointToScreen(point, this.c);
        return point.y;
    }

    public final int getMinimumHeight(int n) {
        if (!this.prefCalled) {
            this.c.getPreferredSize();
            this.prefCalled = true;
        }
        return this.c.getMinimumSize().height;
    }

    public final int getMinimumWidth(int n) {
        if (!this.prefCalled) {
            this.c.getPreferredSize();
            this.prefCalled = true;
        }
        return this.c.getMinimumSize().width;
    }

    public final int getPreferredHeight(int n) {
        if (this.c.getWidth() == 0 && this.c.getHeight() == 0 && n != -1) {
            this.c.setBounds(this.c.getX(), this.c.getY(), n, 1);
        }
        return this.c.getPreferredSize().height;
    }

    public final int getPreferredWidth(int n) {
        if (this.c.getWidth() == 0 && this.c.getHeight() == 0 && n != -1) {
            this.c.setBounds(this.c.getX(), this.c.getY(), 1, n);
        }
        return this.c.getPreferredSize().width;
    }

    public final int getMaximumHeight(int n) {
        if (!this.isMaxSet(this.c)) {
            return Short.MAX_VALUE;
        }
        return this.c.getMaximumSize().height;
    }

    public final int getMaximumWidth(int n) {
        if (!this.isMaxSet(this.c)) {
            return Short.MAX_VALUE;
        }
        return this.c.getMaximumSize().width;
    }

    private boolean isMaxSet(Component component) {
        if (IMS_METHOD != null) {
            try {
                return (Boolean)IMS_METHOD.invoke((Object)component, (Object[])null);
            }
            catch (Exception exception) {
                IMS_METHOD = null;
            }
        }
        return SwingComponentWrapper.isMaxSizeSetOn1_4();
    }

    public final ContainerWrapper getParent() {
        Container container = this.c.getParent();
        return container != null ? new SwingContainerWrapper(container) : null;
    }

    public final int getHorizontalScreenDPI() {
        return PlatformDefaults.getDefaultDPI();
    }

    public final int getVerticalScreenDPI() {
        return PlatformDefaults.getDefaultDPI();
    }

    public final int getScreenWidth() {
        try {
            return this.c.getToolkit().getScreenSize().width;
        }
        catch (HeadlessException headlessException) {
            return 1024;
        }
    }

    public final int getScreenHeight() {
        try {
            return this.c.getToolkit().getScreenSize().height;
        }
        catch (HeadlessException headlessException) {
            return 768;
        }
    }

    public final boolean hasBaseline() {
        if (this.bl == null) {
            try {
                if (BL_RES_METHOD == null || BL_RES_METHOD.invoke((Object)this.c, new Object[0]).toString().equals("OTHER")) {
                    this.bl = Boolean.FALSE;
                } else {
                    Dimension dimension = this.c.getMinimumSize();
                    this.bl = new Boolean(this.getBaseline(dimension.width, dimension.height) > -1);
                }
            }
            catch (Throwable throwable) {
                this.bl = Boolean.FALSE;
            }
        }
        return this.bl;
    }

    public final String getLinkId() {
        return this.c.getName();
    }

    public final void setBounds(int n, int n2, int n3, int n4) {
        this.c.setBounds(n, n2, n3, n4);
    }

    public boolean isVisible() {
        return this.c.isVisible();
    }

    public final int[] getVisualPadding() {
        if (vp && this.c instanceof JTabbedPane && UIManager.getLookAndFeel().getClass().getName().endsWith("WindowsLookAndFeel")) {
            return new int[]{-1, 0, 2, 2};
        }
        return null;
    }

    public static boolean isMaxSizeSetOn1_4() {
        return maxSet;
    }

    public static void setMaxSizeSetOn1_4(boolean bl) {
        maxSet = bl;
    }

    public static boolean isVisualPaddingEnabled() {
        return vp;
    }

    public static void setVisualPaddingEnabled(boolean bl) {
        vp = bl;
    }

    public final void paintDebugOutline() {
        if (!this.c.isShowing()) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D)this.c.getGraphics();
        if (graphics2D == null) {
            return;
        }
        graphics2D.setPaint(DB_COMP_OUTLINE);
        graphics2D.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, new float[]{2.0f, 4.0f}, 0.0f));
        graphics2D.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
    }

    public int getComponetType(boolean bl) {
        if (this.compType == -1) {
            this.compType = this.checkType(bl);
        }
        return this.compType;
    }

    public int getLayoutHashCode() {
        String string;
        Dimension dimension = this.c.getMaximumSize();
        int n = dimension.width + (dimension.height << 5);
        dimension = this.c.getPreferredSize();
        n += (dimension.width << 10) + (dimension.height << 15);
        dimension = this.c.getMinimumSize();
        n += (dimension.width << 20) + (dimension.height << 25);
        if (this.c.isVisible()) {
            n += 1324511;
        }
        if ((string = this.getLinkId()) != null) {
            n += string.hashCode();
        }
        return n;
    }

    private int checkType(boolean bl) {
        Component component = this.c;
        if (bl) {
            if (component instanceof JScrollPane) {
                component = ((JScrollPane)component).getViewport().getView();
            } else if (component instanceof ScrollPane) {
                component = ((ScrollPane)component).getComponent(0);
            }
        }
        if (component instanceof JTextField || component instanceof TextField) {
            return 3;
        }
        if (component instanceof JLabel || component instanceof Label) {
            return 2;
        }
        if (component instanceof JToggleButton || component instanceof Checkbox) {
            return 16;
        }
        if (component instanceof AbstractButton || component instanceof Button) {
            return 5;
        }
        if (component instanceof JComboBox || component instanceof Choice) {
            return 2;
        }
        if (component instanceof JTextComponent || component instanceof TextComponent) {
            return 4;
        }
        if (component instanceof JPanel || component instanceof Canvas) {
            return 10;
        }
        if (component instanceof JList || component instanceof List) {
            return 6;
        }
        if (component instanceof JTable) {
            return 7;
        }
        if (component instanceof JSeparator) {
            return 18;
        }
        if (component instanceof JSpinner) {
            return 13;
        }
        if (component instanceof JProgressBar) {
            return 14;
        }
        if (component instanceof JSlider) {
            return 12;
        }
        if (component instanceof JScrollPane) {
            return 8;
        }
        if (component instanceof JScrollBar || component instanceof Scrollbar) {
            return 17;
        }
        if (component instanceof Container) {
            return 1;
        }
        return 0;
    }

    public final int hashCode() {
        return this.getComponent().hashCode();
    }

    public final boolean equals(Object object) {
        if (!(object instanceof ComponentWrapper)) {
            return false;
        }
        return this.getComponent().equals(((ComponentWrapper)object).getComponent());
    }

    static {
        try {
            BL_METHOD = Component.class.getDeclaredMethod("getBaseline", Integer.TYPE, Integer.TYPE);
            BL_RES_METHOD = Component.class.getDeclaredMethod("getBaselineResizeBehavior", new Class[0]);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        IMS_METHOD = null;
        try {
            IMS_METHOD = Component.class.getDeclaredMethod("isMaximumSizeSet", null);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }
}

