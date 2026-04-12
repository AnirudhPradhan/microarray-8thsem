/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.layout.FillLayout
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.Shell
 */
package net.miginfocom.examples;

import net.miginfocom.swt.MigLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

public class SwtTest {
    public static void main(String[] stringArray) {
        Display display = new Display();
        Shell shell = new Shell();
        Composite composite = new Composite((Composite)shell, 0x20000000);
        shell.setLayout((Layout)new FillLayout());
        MigLayout migLayout = new MigLayout("debug,wrap 2");
        composite.setLayout((Layout)migLayout);
        Label label = new Label(composite, 64);
        label.setText("This is an even longer label that just goes on and on...");
        label.setLayoutData((Object)"wmin 50");
        Label label2 = new Label(composite, 0);
        label2.setText("Label 2");
        label2 = new Label(composite, 0);
        label2.setText("Label 3");
        label2 = new Label(composite, 0);
        label2.setText("Label 4");
        shell.setSize(300, 300);
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (display.readAndDispatch()) continue;
            display.sleep();
        }
    }
}

