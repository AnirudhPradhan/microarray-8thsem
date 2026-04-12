/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.examples;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;

public class TestGap {
    public static void main(String[] stringArray) throws Exception {
        JFrame jFrame = new JFrame();
        jFrame.setLayout(new MigLayout("debug"));
        JTable jTable = new JTable(10, 10);
        jFrame.add(new JScrollPane(jTable));
        jFrame.pack();
        jFrame.setDefaultCloseOperation(2);
        jFrame.setVisible(true);
    }
}

