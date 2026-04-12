/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.examples;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

public class Example01 {
    private static JPanel createPanel() {
        JPanel jPanel = new JPanel(new MigLayout());
        jPanel.add(new JLabel("First Name"));
        jPanel.add(new JTextField(10));
        jPanel.add((Component)new JLabel("Surname"), "gap unrelated");
        jPanel.add((Component)new JTextField(10), "wrap");
        jPanel.add(new JLabel("Address"));
        jPanel.add((Component)new JTextField(), "span, grow");
        return jPanel;
    }

    public static void main(String[] stringArray) {
        SwingUtilities.invokeLater(new Runnable(){

            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                JFrame jFrame = new JFrame("Example 01");
                jFrame.getContentPane().add(Example01.createPanel());
                jFrame.pack();
                jFrame.setDefaultCloseOperation(3);
                jFrame.setVisible(true);
            }
        });
    }
}

