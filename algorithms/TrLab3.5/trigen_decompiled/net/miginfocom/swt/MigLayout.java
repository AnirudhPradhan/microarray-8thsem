/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.graphics.Point
 *  org.eclipse.swt.graphics.Rectangle
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Control
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Layout
 */
package net.miginfocom.swt;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.Grid;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.LayoutCallback;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swt.SwtComponentWrapper;
import net.miginfocom.swt.SwtContainerWrapper;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class MigLayout
extends Layout
implements Externalizable {
    private final Map<Control, Object> scrConstrMap = new IdentityHashMap<Control, Object>(8);
    private Object layoutConstraints = "";
    private Object colConstraints = "";
    private Object rowConstraints = "";
    private transient ContainerWrapper cacheParentW = null;
    private final transient Map<ComponentWrapper, CC> ccMap = new HashMap<ComponentWrapper, CC>(8);
    private transient LC lc = null;
    private transient AC colSpecs = null;
    private transient AC rowSpecs = null;
    private transient Grid grid = null;
    private transient Timer debugTimer = null;
    private transient long curDelay = -1L;
    private transient int lastModCount = PlatformDefaults.getModCount();
    private transient int lastHash = -1;
    private transient ArrayList<LayoutCallback> callbackList = null;

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
        this.grid = null;
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
        this.grid = null;
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
        this.grid = null;
    }

    public Map<Control, Object> getConstraintMap() {
        return new IdentityHashMap<Control, Object>(this.scrConstrMap);
    }

    public void setConstraintMap(Map<Control, Object> map) {
        this.scrConstrMap.clear();
        this.ccMap.clear();
        for (Map.Entry<Control, Object> entry : map.entrySet()) {
            this.setComponentConstraintsImpl(entry.getKey(), entry.getValue(), true);
        }
    }

    private void setComponentConstraintsImpl(Control control, Object object, boolean bl) {
        if (!bl && !this.scrConstrMap.containsKey(control)) {
            throw new IllegalArgumentException("Component must already be added to parent!");
        }
        SwtComponentWrapper swtComponentWrapper = new SwtComponentWrapper(control);
        if (object == null || object instanceof String) {
            String string = ConstraintParser.prepare((String)object);
            this.scrConstrMap.put(control, object);
            this.ccMap.put(swtComponentWrapper, ConstraintParser.parseComponentConstraint(string));
        } else if (object instanceof CC) {
            this.scrConstrMap.put(control, object);
            this.ccMap.put(swtComponentWrapper, (CC)object);
        } else {
            throw new IllegalArgumentException("Constraint must be String or ComponentConstraint: " + object.getClass().toString());
        }
        this.grid = null;
    }

    public boolean isManagingComponent(Control control) {
        return this.scrConstrMap.containsKey(control);
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
        if (bl && (this.debugTimer == null || this.curDelay != (long)this.getDebugMillis())) {
            Composite composite;
            if (this.debugTimer != null) {
                this.debugTimer.cancel();
            }
            this.debugTimer = new Timer(true);
            this.curDelay = this.getDebugMillis();
            this.debugTimer.schedule((TimerTask)new MyDebugRepaintTask(this), this.curDelay, this.curDelay);
            ContainerWrapper containerWrapper = componentWrapper.getParent();
            Composite composite2 = composite = containerWrapper != null ? (Composite)containerWrapper.getComponent() : null;
            if (composite != null) {
                composite.layout();
            }
        } else if (!bl && this.debugTimer != null) {
            this.debugTimer.cancel();
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

    private void checkCache(Composite composite) {
        if (composite == null) {
            return;
        }
        this.checkConstrMap(composite);
        ContainerWrapper containerWrapper = this.checkParent(composite);
        int n = PlatformDefaults.getModCount();
        if (this.lastModCount != n) {
            this.grid = null;
            this.lastModCount = n;
        }
        int n2 = composite.getSize().hashCode();
        Iterator<ComponentWrapper> iterator = this.ccMap.keySet().iterator();
        while (iterator.hasNext()) {
            n2 += iterator.next().getLayoutHashCode();
        }
        if (n2 != this.lastHash) {
            this.grid = null;
            this.lastHash = n2;
        }
        this.setDebug(containerWrapper, this.getDebugMillis() > 0);
        if (this.grid == null) {
            this.grid = new Grid(containerWrapper, this.lc, this.rowSpecs, this.colSpecs, this.ccMap, this.callbackList);
        }
    }

    private boolean checkConstrMap(Composite composite) {
        Control control;
        int n;
        boolean bl;
        Control[] controlArray = composite.getChildren();
        boolean bl2 = bl = controlArray.length != this.scrConstrMap.size();
        if (!bl) {
            for (n = 0; n < controlArray.length; ++n) {
                control = controlArray[n];
                if (this.scrConstrMap.get(control) == control.getLayoutData()) continue;
                bl = true;
                break;
            }
        }
        if (bl) {
            this.scrConstrMap.clear();
            for (n = 0; n < controlArray.length; ++n) {
                control = controlArray[n];
                this.setComponentConstraintsImpl(control, control.getLayoutData(), true);
            }
        }
        return bl;
    }

    private ContainerWrapper checkParent(Composite composite) {
        if (composite == null) {
            return null;
        }
        if (this.cacheParentW == null || this.cacheParentW.getComponent() != composite) {
            this.cacheParentW = new SwtContainerWrapper(composite);
        }
        return this.cacheParentW;
    }

    public float getLayoutAlignmentX(Composite composite) {
        return this.lc != null && this.lc.getAlignX() != null ? (float)this.lc.getAlignX().getPixels(1.0f, this.checkParent(composite), null) : 0.0f;
    }

    public float getLayoutAlignmentY(Composite composite) {
        return this.lc != null && this.lc.getAlignY() != null ? (float)this.lc.getAlignY().getPixels(1.0f, this.checkParent(composite), null) : 0.0f;
    }

    protected Point computeSize(Composite composite, int n, int n2, boolean bl) {
        this.checkCache(composite);
        int n3 = LayoutUtil.getSizeSafe(this.grid != null ? this.grid.getWidth() : null, 1);
        int n4 = LayoutUtil.getSizeSafe(this.grid != null ? this.grid.getHeight() : null, 1);
        return new Point(n3, n4);
    }

    protected void layout(Composite composite, boolean bl) {
        this.checkCache(composite);
        Rectangle rectangle = composite.getClientArea();
        int[] nArray = new int[]{rectangle.x, rectangle.y, rectangle.width, rectangle.height};
        boolean bl2 = this.grid.layout(nArray, this.lc.getAlignX(), this.lc.getAlignY(), this.getDebug(), true);
        if (bl2) {
            this.grid = null;
            this.checkCache(composite);
            this.grid.layout(nArray, this.lc.getAlignX(), this.lc.getAlignY(), this.getDebug(), false);
        }
    }

    protected boolean flushCache(Control control) {
        this.grid = null;
        return true;
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

    static {
        if (PlatformDefaults.getPlatform() == 1) {
            PlatformDefaults.setDefaultDPI(72);
        }
    }

    private static class MyDebugRepaintTask
    extends TimerTask {
        private final WeakReference<MigLayout> layoutRef;

        private MyDebugRepaintTask(MigLayout migLayout) {
            this.layoutRef = new WeakReference<MigLayout>(migLayout);
        }

        public void run() {
            final MigLayout migLayout = (MigLayout)this.layoutRef.get();
            if (migLayout != null && migLayout.grid != null) {
                Display.getDefault().asyncExec(new Runnable(){

                    public void run() {
                        if (migLayout.grid != null) {
                            migLayout.grid.paintDebug();
                        }
                    }
                });
            }
        }
    }
}

