/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.examples;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;

public class Example02 {
    private static JPanel createPanel() {
        JPanel jPanel = new JPanel(new MigLayout());
        jPanel.add((Component)Example02.createLabel("West Panel"), "dock west");
        jPanel.add((Component)Example02.createLabel("North 1 Panel"), "dock north");
        jPanel.add((Component)Example02.createLabel("North 2 Panel"), "dock north");
        jPanel.add((Component)Example02.createLabel("South Panel"), "dock south");
        jPanel.add((Component)Example02.createLabel("East Panel"), "dock east");
        jPanel.add((Component)Example02.createLabel("Center Panel"), "grow, push");
        return jPanel;
    }

    private static JLabel createLabel(String string) {
        JLabel jLabel = new JLabel(string);
        jLabel.setHorizontalAlignment(0);
        jLabel.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(5, 10, 5, 10)));
        return jLabel;
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
                JFrame jFrame = new JFrame("Example 02");
                jFrame.getContentPane().add(Example02.createPanel());
                jFrame.pack();
                jFrame.setDefaultCloseOperation(3);
                jFrame.setVisible(true);
            }
        });
    }
}

