/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.BoundSize;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.Grid;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.LayoutCallback;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.layout.UnitValue;
import net.miginfocom.swing.SwingComponentWrapper;
import net.miginfocom.swing.SwingContainerWrapper;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class MigLayout
implements LayoutManager2,
Externalizable {
    private final Map<Component, Object> scrConstrMap = new IdentityHashMap<Component, Object>(8);
    private Object layoutConstraints = "";
    private Object colConstraints = "";
    private Object rowConstraints = "";
    private transient ContainerWrapper cacheParentW = null;
    private final transient Map<ComponentWrapper, CC> ccMap = new HashMap<ComponentWrapper, CC>(8);
    private transient Timer debugTimer = null;
    private transient LC lc = null;
    private transient AC colSpecs = null;
    private transient AC rowSpecs = null;
    private transient Grid grid = null;
    private transient int lastModCount = PlatformDefaults.getModCount();
    private transient int lastHash = -1;
    private transient Dimension lastInvalidSize = null;
    private transient boolean lastWasInvalid = false;
    private transient Dimension lastParentSize = null;
    private transient ArrayList<LayoutCallback> callbackList = null;
    private transient boolean dirty = true;
    private long lastSize = 0L;

    public MigLayout() {
        this("", "", "");
    }

    public MigLayout(String string) {
        this(string, "", "");
    }

    public MigLayout(String string, String string2) {
        this(string, string2, "");
    }

    public MigLayout(String string, String string2, String string3) {
        this.setLayoutConstraints(string);
        this.setColumnConstraints(string2);
        this.setRowConstraints(string3);
    }

    public MigLayout(LC lC) {
        this(lC, null, null);
    }

    public MigLayout(LC lC, AC aC) {
        this(lC, aC, null);
    }

    public MigLayout(LC lC, AC aC, AC aC2) {
        this.setLayoutConstraints(lC);
        this.setColumnConstraints(aC);
        this.setRowConstraints(aC2);
    }

    public Object getLayoutConstraints() {
        return this.layoutConstraints;
    }

    public void setLayoutConstraints(Object object) {
        if (object == null || object instanceof String) {
            object = ConstraintParser.prepare((String)object);
            this.lc = ConstraintParser.parseLayoutConstraint((String)object);
        } else if (object instanceof LC) {
            this.lc = (LC)object;
        } else {
            throw new IllegalArgumentException("Illegal constraint type: " + object.getClass().toString());
        }
        this.layoutConstraints = object;
        this.dirty = true;
    }

    public Object getColumnConstraints() {
        return this.colConstraints;
    }

    public void setColumnConstraints(Object object) {
        if (object == null || object instanceof String) {
            object = ConstraintParser.prepare((String)object);
            this.colSpecs = ConstraintParser.parseColumnConstraints((String)object);
        } else if (object instanceof AC) {
            this.colSpecs = (AC)object;
        } else {
            throw new IllegalArgumentException("Illegal constraint type: " + object.getClass().toString());
        }
        this.colConstraints = object;
        this.dirty = true;
    }

    public Object getRowConstraints() {
        return this.rowConstraints;
    }

    public void setRowConstraints(Object object) {
        if (object == null || object instanceof String) {
            object = ConstraintParser.prepare((String)object);
            this.rowSpecs = ConstraintParser.parseRowConstraints((String)object);
        } else if (object instanceof AC) {
            this.rowSpecs = (AC)object;
        } else {
            throw new IllegalArgumentException("Illegal constraint type: " + object.getClass().toString());
        }
        this.rowConstraints = object;
        this.dirty = true;
    }

    public Map<Component, Object> getConstraintMap() {
        return new IdentityHashMap<Component, Object>(this.scrConstrMap);
    }

    public void setConstraintMap(Map<Component, Object> map) {
        this.scrConstrMap.clear();
        this.ccMap.clear();
        for (Map.Entry<Component, Object> entry : map.entrySet()) {
            this.setComponentConstraintsImpl(entry.getKey(), entry.getValue(), true);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Object getComponentConstraints(Component component) {
        Object object = component.getParent().getTreeLock();
        synchronized (object) {
            return this.scrConstrMap.get(component);
        }
    }

    public void setComponentConstraints(Component component, Object object) {
        this.setComponentConstraintsImpl(component, object, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void setComponentConstraintsImpl(Component component, Object object, boolean bl) {
        Container container = component.getParent();
        Object object2 = container != null ? container.getTreeLock() : new Object();
        synchronized (object2) {
            if (!bl && !this.scrConstrMap.containsKey(component)) {
                throw new IllegalArgumentException("Component must already be added to parent!");
            }
            SwingComponentWrapper swingComponentWrapper = new SwingComponentWrapper(component);
            if (object == null || object instanceof String) {
                String string = ConstraintParser.prepare((String)object);
                this.scrConstrMap.put(component, object);
                this.ccMap.put(swingComponentWrapper, ConstraintParser.parseComponentConstraint(string));
            } else if (object instanceof CC) {
                this.scrConstrMap.put(component, object);
                this.ccMap.put(swingComponentWrapper, (CC)object);
            } else {
                throw new IllegalArgumentException("Constraint must be String or ComponentConstraint: " + object.getClass().toString());
            }
            this.dirty = true;
        }
    }

    public boolean isManagingComponent(Component component) {
        return this.scrConstrMap.containsKey(component);
    }

    public void addLayoutCallback(LayoutCallback layoutCallback) {
        if (layoutCallback == null) {
            throw new NullPointerException();
        }
        if (this.callbackList == null) {
            this.callbackList = new ArrayList(1);
        }
        this.callbackList.add(layoutCallback);
    }

    public void removeLayoutCallback(LayoutCallback layoutCallback) {
        if (this.callbackList != null) {
            this.callbackList.remove(layoutCallback);
        }
    }

    private synchronized void setDebug(ComponentWrapper componentWrapper, boolean bl) {
        if (bl && (this.debugTimer == null || this.debugTimer.getDelay() != this.getDebugMillis())) {
            ContainerWrapper containerWrapper;
            if (this.debugTimer != null) {
                this.debugTimer.stop();
            }
            final Component component = (containerWrapper = componentWrapper.getParent()) != null ? (Component)containerWrapper.getComponent() : null;
            this.debugTimer = new Timer(this.getDebugMillis(), new MyDebugRepaintListener());
            if (component != null) {
                SwingUtilities.invokeLater(new Runnable(){

                    public void run() {
                        Container container = component.getParent();
                        if (container != null) {
                            if (container instanceof JComponent) {
                                ((JComponent)container).revalidate();
                            } else {
                                component.invalidate();
                                container.validate();
                            }
                        }
                    }
                });
            }
            this.debugTimer.setInitialDelay(100);
            this.debugTimer.start();
        } else if (!bl && this.debugTimer != null) {
            this.debugTimer.stop();
            this.debugTimer = null;
        }
    }

    private boolean getDebug() {
        return this.debugTimer != null;
    }

    private int getDebugMillis() {
        int n = LayoutUtil.getGlobalDebugMillis();
        return n > 0 ? n : this.lc.getDebugMillis();
    }

    private void checkCache(Container container) {
        int n;
        if (container == null) {
            return;
        }
        if (this.dirty) {
            this.grid = null;
        }
        if (this.lastModCount != (n = PlatformDefaults.getModCount())) {
            this.grid = null;
            this.lastModCount = n;
        }
        if (!container.isValid()) {
            if (!this.lastWasInvalid) {
                this.lastWasInvalid = true;
                int n2 = 0;
                boolean bl = false;
                for (ComponentWrapper componentWrapper : this.ccMap.keySet()) {
                    Object object = componentWrapper.getComponent();
                    if (object instanceof JTextArea || object instanceof JEditorPane) {
                        bl = true;
                    }
                    n2 += componentWrapper.getLayoutHashCode();
                }
                if (bl) {
                    this.resetLastInvalidOnParent(container);
                }
                if (n2 != this.lastHash) {
                    this.grid = null;
                    this.lastHash = n2;
                }
                Dimension dimension = container.getSize();
                if (this.lastInvalidSize == null || !this.lastInvalidSize.equals(dimension)) {
                    if (this.grid != null) {
                        this.grid.invalidateContainerSize();
                    }
                    this.lastInvalidSize = dimension;
                }
            }
        } else {
            this.lastWasInvalid = false;
        }
        ContainerWrapper containerWrapper = this.checkParent(container);
        this.setDebug(containerWrapper, this.getDebugMillis() > 0);
        if (this.grid == null) {
            this.grid = new Grid(containerWrapper, this.lc, this.rowSpecs, this.colSpecs, this.ccMap, this.callbackList);
        }
        this.dirty = false;
    }

    private void resetLastInvalidOnParent(Container container) {
        while (container != null) {
            LayoutManager layoutManager = container.getLayout();
            if (layoutManager instanceof MigLayout) {
                ((MigLayout)layoutManager).lastWasInvalid = false;
            }
            container = container.getParent();
        }
    }

    private ContainerWrapper checkParent(Container container) {
        if (container == null) {
            return null;
        }
        if (this.cacheParentW == null || this.cacheParentW.getComponent() != container) {
            this.cacheParentW = new SwingContainerWrapper(container);
        }
        return this.cacheParentW;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void layoutContainer(Container container) {
        Object object = container.getTreeLock();
        synchronized (object) {
            long l;
            this.checkCache(container);
            Insets insets = container.getInsets();
            int[] nArray = new int[]{insets.left, insets.top, container.getWidth() - insets.left - insets.right, container.getHeight() - insets.top - insets.bottom};
            if (this.grid.layout(nArray, this.lc.getAlignX(), this.lc.getAlignY(), this.getDebug(), true)) {
                this.grid = null;
                this.checkCache(container);
                this.grid.layout(nArray, this.lc.getAlignX(), this.lc.getAlignY(), this.getDebug(), false);
            }
            if (this.lastSize != (l = (long)this.grid.getHeight()[1] + ((long)this.grid.getWidth()[1] << 32))) {
                this.lastSize = l;
                final ContainerWrapper containerWrapper = this.checkParent(container);
                Window window = (Window)SwingUtilities.getAncestorOfClass(Window.class, (Component)containerWrapper.getComponent());
                if (window != null) {
                    if (window.isVisible()) {
                        SwingUtilities.invokeLater(new Runnable(){

                            public void run() {
                                MigLayout.this.adjustWindowSize(containerWrapper);
                            }
                        });
                    } else {
                        this.adjustWindowSize(containerWrapper);
                    }
                }
            }
            this.lastInvalidSize = null;
        }
    }

    private void adjustWindowSize(ContainerWrapper containerWrapper) {
        BoundSize boundSize = this.lc.getPackWidth();
        BoundSize boundSize2 = this.lc.getPackHeight();
        if (boundSize == null && boundSize2 == null) {
            return;
        }
        Window window = (Window)SwingUtilities.getAncestorOfClass(Window.class, (Component)containerWrapper.getComponent());
        if (window == null) {
            return;
        }
        Dimension dimension = window.getPreferredSize();
        int n = this.constrain(this.checkParent(window), window.getWidth(), dimension.width, boundSize);
        int n2 = this.constrain(this.checkParent(window), window.getHeight(), dimension.height, boundSize2);
        int n3 = Math.round((float)window.getX() - (float)(n - window.getWidth()) * (1.0f - this.lc.getPackWidthAlign()));
        int n4 = Math.round((float)window.getY() - (float)(n2 - window.getHeight()) * (1.0f - this.lc.getPackHeightAlign()));
        window.setBounds(n3, n4, n, n2);
    }

    private int constrain(ContainerWrapper containerWrapper, int n, int n2, BoundSize boundSize) {
        if (boundSize == null) {
            return n;
        }
        int n3 = n;
        UnitValue unitValue = boundSize.getPreferred();
        if (unitValue != null) {
            n3 = unitValue.getPixels(n2, containerWrapper, containerWrapper);
        }
        n3 = boundSize.constrain(n3, n2, containerWrapper);
        return boundSize.getGapPush() ? Math.max(n, n3) : n3;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Dimension minimumLayoutSize(Container container) {
        Object object = container.getTreeLock();
        synchronized (object) {
            return this.getSizeImpl(container, 0);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Dimension preferredLayoutSize(Container container) {
        Object object = container.getTreeLock();
        synchronized (object) {
            if (this.lastParentSize == null || !container.getSize().equals(this.lastParentSize)) {
                for (ComponentWrapper componentWrapper : this.ccMap.keySet()) {
                    Component component = (Component)componentWrapper.getComponent();
                    if (!(component instanceof JTextArea) && !(component instanceof JEditorPane) && (!(component instanceof JComponent) || !Boolean.TRUE.equals(((JComponent)component).getClientProperty("migLayout.dynamicAspectRatio")))) continue;
                    this.layoutContainer(container);
                    break;
                }
            }
            this.lastParentSize = container.getSize();
            return this.getSizeImpl(container, 1);
        }
    }

    @Override
    public Dimension maximumLayoutSize(Container container) {
        return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    }

    private Dimension getSizeImpl(Container container, int n) {
        this.checkCache(container);
        Insets insets = container.getInsets();
        int n2 = LayoutUtil.getSizeSafe(this.grid != null ? this.grid.getWidth() : null, n) + insets.left + insets.right;
        int n3 = LayoutUtil.getSizeSafe(this.grid != null ? this.grid.getHeight() : null, n) + insets.top + insets.bottom;
        return new Dimension(n2, n3);
    }

    @Override
    public float getLayoutAlignmentX(Container container) {
        return this.lc != null && this.lc.getAlignX() != null ? (float)this.lc.getAlignX().getPixels(1.0f, this.checkParent(container), null) : 0.0f;
    }

    @Override
    public float getLayoutAlignmentY(Container container) {
        return this.lc != null && this.lc.getAlignY() != null ? (float)this.lc.getAlignY().getPixels(1.0f, this.checkParent(container), null) : 0.0f;
    }

    @Override
    public void addLayoutComponent(String string, Component component) {
        this.addLayoutComponent(component, string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addLayoutComponent(Component component, Object object) {
        Object object2 = component.getParent().getTreeLock();
        synchronized (object2) {
            this.setComponentConstraintsImpl(component, object, true);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void removeLayoutComponent(Component component) {
        Object object = component.getParent().getTreeLock();
        synchronized (object) {
            this.scrConstrMap.remove(component);
            this.ccMap.remove(new SwingComponentWrapper(component));
        }
    }

    @Override
    public void invalidateLayout(Container container) {
        this.dirty = true;
    }

    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInput));
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        if (this.getClass() == MigLayout.class) {
            LayoutUtil.writeAsXML(objectOutput, this);
        }
    }

    private class MyDebugRepaintListener
    implements ActionListener {
        private MyDebugRepaintListener() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Component component;
            if (MigLayout.this.grid != null && (component = (Component)MigLayout.this.grid.getContainer().getComponent()).isShowing()) {
                MigLayout.this.grid.paintDebug();
                return;
            }
            MigLayout.this.debugTimer.stop();
            MigLayout.this.debugTimer = null;
        }
    }
}

