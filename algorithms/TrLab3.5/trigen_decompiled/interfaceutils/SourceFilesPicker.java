/*
 * Decompiled with CFR 0.152.
 */
package interfaceutils;

import interfaceutils.PathPicker;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class SourceFilesPicker
extends JPanel {
    private int timePoints;

    public SourceFilesPicker(int timePoints) {
        this.timePoints = timePoints;
        this.setLayout(new BoxLayout(this, 1));
        for (int i = 0; i < this.timePoints; ++i) {
            PathPicker aux = new PathPicker("time " + i);
            this.add(aux);
        }
    }

    public String getSelectedPath(int i) {
        String r = "";
        Component[] comp = this.getComponents();
        r = ((PathPicker)comp[i]).getSelectedPath();
        return r;
    }

    public String[] getAllSelectedPaths() {
        String[] r = new String[this.timePoints];
        Component[] comp = this.getComponents();
        for (int i = 0; i < comp.length; ++i) {
            PathPicker aux = (PathPicker)comp[i];
            r[i] = aux.getSelectedPath();
        }
        return r;
    }

    public String getReportString() {
        String r = "";
        Component[] comp = this.getComponents();
        for (int i = 0; i < comp.length; ++i) {
            PathPicker aux = (PathPicker)comp[i];
            r = i == comp.length - 1 ? r + "T" + i + " path = " + aux.getSelectedPath() : r + "T" + i + " path = " + aux.getSelectedPath() + "\n";
        }
        return r;
    }
}

