/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.demo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import net.miginfocom.demo.HiDPISimulator;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swing.MigLayout;

class HiDPIDemoPanel
extends JPanel {
    public HiDPIDemoPanel() {
        super(new MigLayout());
        JLabel jLabel = new JLabel("A Small Label:");
        JTextField jTextField = new JTextField(10);
        JButton jButton = new JButton("Cancel");
        JButton jButton2 = new JButton("OK");
        JButton jButton3 = new JButton("Help");
        JList jList = new JList();
        JLabel jLabel2 = new JLabel("Label:");
        JTextField jTextField2 = new JTextField(10);
        JLabel jLabel3 = new JLabel("This is another section");
        JSeparator jSeparator = new JSeparator();
        JTextArea jTextArea = new JTextArea("Some general text that takes place, doesn't offend anyone and fills some pixels.", 3, 30);
        JLabel jLabel4 = new JLabel("Some Text Area");
        JLabel jLabel5 = new JLabel("Some List:");
        JComboBox<String> jComboBox = new JComboBox<String>();
        JCheckBox jCheckBox = new JCheckBox("Orange");
        JScrollBar jScrollBar = new JScrollBar(1);
        JScrollBar jScrollBar2 = new JScrollBar(0, 30, 40, 0, 100);
        JRadioButton jRadioButton = new JRadioButton("Apple");
        JProgressBar jProgressBar = new JProgressBar();
        jProgressBar.setValue(50);
        JSpinner jSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 100, 1));
        JTree jTree = new JTree();
        jTree.setOpaque(false);
        jTree.setEnabled(false);
        jList.setModel(new AbstractListModel(){
            String[] strings = new String[]{"Donald Duck", "Mickey Mouse", "Pluto", "Cartman"};

            public int getSize() {
                return this.strings.length;
            }

            public Object getElementAt(int n) {
                return this.strings[n];
            }
        });
        jList.setVisibleRowCount(4);
        jList.setBorder(new LineBorder(Color.GRAY));
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setBorder(new LineBorder(Color.GRAY));
        jComboBox.setModel(new DefaultComboBoxModel<String>(new String[]{"Text in ComboBox"}));
        jCheckBox.setMargin(new Insets(0, 0, 0, 0));
        this.add((Component)jLabel, "split, span");
        this.add((Component)jTextField, "");
        this.add((Component)jLabel2, "gap unrelated");
        this.add((Component)jTextField2, "wrap");
        this.add((Component)jLabel3, "split, span");
        this.add((Component)jSeparator, "growx, span, gap 2, wrap unrelated");
        this.add((Component)jLabel4, "wrap 2");
        this.add((Component)jTextArea, "span, wmin 150, wrap unrelated");
        this.add((Component)jLabel5, "wrap 2");
        this.add(jList, "split, span");
        this.add((Component)jScrollBar, "growy");
        this.add((Component)jProgressBar, "width 80!");
        this.add((Component)jTree, "wrap unrelated");
        this.add((Component)jScrollBar2, "split, span, growx");
        this.add((Component)jSpinner, "wrap unrelated");
        this.add(jComboBox, "span, split");
        this.add((Component)jRadioButton, "");
        this.add((Component)jCheckBox, "wrap unrelated");
        this.add((Component)jButton3, "split, span, tag help2");
        this.add((Component)jButton2, "tag ok");
        this.add((Component)jButton, "tag cancel");
        HiDPISimulator.TEXT_AREA = jTextArea;
    }

    public void paint(Graphics graphics) {
        if (HiDPISimulator.GUI_BUF == null) {
            BufferedImage bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), 2);
            Graphics2D graphics2D = bufferedImage.createGraphics();
            super.paint(graphics2D);
            graphics2D.dispose();
            graphics.drawImage(bufferedImage, 0, 0, null);
            HiDPISimulator.GUI_BUF = bufferedImage;
            if (HiDPISimulator.CUR_DPI == PlatformDefaults.getDefaultDPI()) {
                HiDPISimulator.ORIG_GUI_BUF = bufferedImage;
            }
            SwingUtilities.invokeLater(new Runnable(){

                public void run() {
                    HiDPISimulator.MIRROR_PANEL.revalidate();
                    HiDPISimulator.MIRROR_PANEL.repaint();
                }
            });
        } else {
            super.paint(graphics);
        }
    }
}

