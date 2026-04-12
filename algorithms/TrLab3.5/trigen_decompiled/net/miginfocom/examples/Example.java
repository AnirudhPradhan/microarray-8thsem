/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.Table
 *  org.eclipse.swt.widgets.TableItem
 */
package net.miginfocom.examples;

import net.miginfocom.swt.MigLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class Example {
    protected void buildControls(Composite composite) {
        composite.setLayout((Layout)new MigLayout("inset 0", "[fill, grow]", "[fill, grow]"));
        Table table = new Table(composite, 2816);
        table.setLayoutData((Object)"id table, hmin 100, wmin 300");
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        Label label = new Label(composite, 2048);
        label.setText("Label Text");
        label.moveAbove(null);
        label.setLayoutData((Object)"pos table.x table.y");
        for (int i = 0; i < 10; ++i) {
            TableItem tableItem = new TableItem(table, 0);
            tableItem.setText("item #" + i);
        }
    }

    public static void main(String[] stringArray) {
        Display display = new Display();
        Shell shell = new Shell(display);
        new Example().buildControls((Composite)shell);
        shell.open();
        while (!shell.isDisposed()) {
            if (display.readAndDispatch()) continue;
            display.sleep();
        }
        display.dispose();
    }
}

