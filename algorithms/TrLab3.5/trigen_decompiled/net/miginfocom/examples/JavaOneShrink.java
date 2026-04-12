/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.examples;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;

public class JavaOneShrink {
    private static JComponent createPanel(String ... stringArray) {
        JPanel jPanel = new JPanel(new MigLayout("nogrid"));
        jPanel.add((Component)JavaOneShrink.createTextArea(stringArray[0].replace(", ", "\n    ")), stringArray[0] + ", w 200");
        jPanel.add((Component)JavaOneShrink.createTextArea(stringArray[1].replace(", ", "\n    ")), stringArray[1] + ", w 200");
        jPanel.add((Component)JavaOneShrink.createTextArea(stringArray[2].replace(", ", "\n    ")), stringArray[2] + ", w 200");
        jPanel.add((Component)JavaOneShrink.createTextArea(stringArray[3].replace(", ", "\n    ")), stringArray[3] + ", w 200");
        JSplitPane jSplitPane = new JSplitPane(1, true, jPanel, new JPanel());
        jSplitPane.setOpaque(true);
        jSplitPane.setBorder(null);
        return jSplitPane;
    }

    private static JComponent createTextArea(String string) {
        JTextArea jTextArea = new JTextArea("\n\n    " + string, 6, 20);
        jTextArea.setBorder(new LineBorder(new Color(200, 200, 200)));
        jTextArea.setFont(new Font("Helvetica", 1, 20));
        jTextArea.setMinimumSize(new Dimension(20, 20));
        jTextArea.setFocusable(false);
        return jTextArea;
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
                JFrame jFrame = new JFrame("JavaOne Shrink Demo");
                Container container = jFrame.getContentPane();
                container.setLayout(new MigLayout("wrap 1"));
                container.add(JavaOneShrink.createPanel(new String[]{"", "", "", ""}));
                container.add(JavaOneShrink.createPanel(new String[]{"shrinkprio 1", "shrinkprio 1", "shrinkprio 2", "shrinkprio 3"}));
                container.add(JavaOneShrink.createPanel(new String[]{"shrink 25", "shrink 50", "shrink 75", "shrink 100"}));
                container.add(JavaOneShrink.createPanel(new String[]{"shrinkprio 1, shrink 50", "shrinkprio 1, shrink 100", "shrinkprio 2, shrink 50", "shrinkprio 2, shrink 100"}));
                jFrame.pack();
                jFrame.setDefaultCloseOperation(3);
                jFrame.setLocationRelativeTo(null);
                jFrame.setVisible(true);
            }
        });
    }
}

