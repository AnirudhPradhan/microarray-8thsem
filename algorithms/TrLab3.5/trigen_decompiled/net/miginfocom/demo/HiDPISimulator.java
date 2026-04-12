/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jvnet.substance.SubstanceLookAndFeel
 *  org.jvnet.substance.fonts.FontPolicy
 *  org.jvnet.substance.fonts.SubstanceFontUtilities
 *  org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel
 */
package net.miginfocom.demo;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import net.miginfocom.demo.HiDPIDemoPanel;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swing.MigLayout;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.fonts.FontPolicy;
import org.jvnet.substance.fonts.SubstanceFontUtilities;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

public class HiDPISimulator {
    static final String SYSTEM_LAF_NAME = "System";
    static final String SUBSTANCE_LAF_NAME = "Substance";
    static final String OCEAN_LAF_NAME = "Ocean";
    static final String NUMBUS_LAF_NAME = "Nimbus (Soon..)";
    static JFrame APP_GUI_FRAME;
    static HiDPIDemoPanel HiDPIDEMO_PANEL;
    static JPanel SIM_PANEL;
    static JPanel MIRROR_PANEL;
    static JScrollPane MAIN_SCROLL;
    static JTextArea TEXT_AREA;
    static boolean SCALE_LAF;
    static boolean SCALE_FONTS;
    static boolean SCALE_LAYOUT;
    static boolean PAINT_GHOSTED;
    static BufferedImage GUI_BUF;
    static BufferedImage ORIG_GUI_BUF;
    static int CUR_DPI;
    static HashMap<String, Font> ORIG_DEFAULTS;

    private static JPanel createScaleMirror() {
        return new JPanel(new MigLayout()){

            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                if (GUI_BUF != null) {
                    Graphics2D graphics2D = (Graphics2D)graphics.create();
                    double d = this.getToolkit().getScreenResolution();
                    AffineTransform affineTransform = graphics2D.getTransform();
                    graphics2D.scale(d / (double)CUR_DPI, d / (double)CUR_DPI);
                    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    graphics2D.drawImage((Image)GUI_BUF, 0, 0, null);
                    graphics2D.setTransform(affineTransform);
                    if (ORIG_GUI_BUF != null && PAINT_GHOSTED) {
                        graphics2D.setComposite(AlphaComposite.getInstance(3, 0.2f));
                        graphics2D.drawImage((Image)ORIG_GUI_BUF, 0, 0, null);
                    }
                    graphics2D.dispose();
                }
            }

            public Dimension getPreferredSize() {
                return ORIG_GUI_BUF != null ? new Dimension(ORIG_GUI_BUF.getWidth(), ORIG_GUI_BUF.getHeight()) : new Dimension(100, 100);
            }

            public Dimension getMinimumSize() {
                return this.getPreferredSize();
            }
        };
    }

    private static JPanel createSimulator() {
        final JRadioButton jRadioButton = new JRadioButton("UIManager Font Substitution", true);
        final JRadioButton jRadioButton2 = new JRadioButton("Native Look&Feel Scaling", false);
        JRadioButton jRadioButton3 = new JRadioButton("No Scaling", false);
        final JRadioButton jRadioButton4 = new JRadioButton("Native MigLayout Gap Scaling", true);
        JRadioButton jRadioButton5 = new JRadioButton("No Gap Scaling", false);
        final JComboBox<String> jComboBox = new JComboBox<String>(new String[]{SYSTEM_LAF_NAME, SUBSTANCE_LAF_NAME, OCEAN_LAF_NAME, NUMBUS_LAF_NAME});
        ButtonGroup buttonGroup = new ButtonGroup();
        ButtonGroup buttonGroup2 = new ButtonGroup();
        final JCheckBox jCheckBox = new JCheckBox("Overlay \"Optimal\" HiDPI Result");
        jRadioButton2.setEnabled(false);
        buttonGroup.add(jRadioButton);
        buttonGroup.add(jRadioButton2);
        buttonGroup.add(jRadioButton3);
        buttonGroup2.add(jRadioButton4);
        buttonGroup2.add(jRadioButton5);
        Vector<String> vector = new Vector<String>();
        for (float f = 0.5f; f < 2.01f; f += 0.1f) {
            vector.add(Math.round((float)PlatformDefaults.getDefaultDPI() * f) + " DPI (" + Math.round(f * 100.0f + 0.499f) + "%)");
        }
        final JComboBox jComboBox2 = new JComboBox(vector);
        jComboBox2.setSelectedIndex(5);
        JPanel jPanel = new JPanel(new MigLayout("alignx center, insets 10px, flowy", "[]", "[]3px[]0px[]"));
        JLabel jLabel = new JLabel("Look & Feel:");
        JLabel jLabel2 = new JLabel("Simulated DPI:");
        JLabel jLabel3 = new JLabel("Component/Text Scaling:");
        JLabel jLabel4 = new JLabel("LayoutManager Scaling:");
        JLabel jLabel5 = new JLabel("Visual Aids:");
        jPanel.add((Component)jLabel, "");
        jPanel.add(jComboBox, "wrap");
        jPanel.add((Component)jLabel2, "");
        jPanel.add(jComboBox2, "wrap");
        jPanel.add((Component)jLabel3, "");
        jPanel.add((Component)jRadioButton, "");
        jPanel.add((Component)jRadioButton2, "");
        jPanel.add((Component)jRadioButton3, "wrap");
        jPanel.add((Component)jLabel4, "");
        jPanel.add((Component)jRadioButton4, "");
        jPanel.add((Component)jRadioButton5, "wrap");
        jPanel.add((Component)jLabel5, "");
        jPanel.add((Component)jCheckBox, "");
        HiDPISimulator.lockFont(jComboBox2, jRadioButton, jRadioButton2, jRadioButton4, jRadioButton3, jComboBox, jCheckBox, jPanel, jLabel, jLabel2, jRadioButton5, jLabel3, jLabel4, jLabel5);
        jComboBox.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                GUI_BUF = null;
                try {
                    Object object = jComboBox.getSelectedItem();
                    jComboBox2.setSelectedIndex(5);
                    if (object.equals(HiDPISimulator.SYSTEM_LAF_NAME)) {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } else if (object.equals(HiDPISimulator.SUBSTANCE_LAF_NAME)) {
                        UIManager.setLookAndFeel((LookAndFeel)new SubstanceBusinessBlackSteelLookAndFeel());
                    } else if (object.equals(HiDPISimulator.OCEAN_LAF_NAME)) {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    } else {
                        JOptionPane.showMessageDialog(APP_GUI_FRAME, "Nimbus will be included as soon as it is ready!");
                    }
                    if (ORIG_DEFAULTS != null) {
                        for (String string : ORIG_DEFAULTS.keySet()) {
                            UIManager.put(string, null);
                        }
                    }
                    ORIG_DEFAULTS = null;
                    if (UIManager.getLookAndFeel().getName().toLowerCase().contains("windows")) {
                        UIManager.put("TextArea.font", UIManager.getFont("TextField.font"));
                    } else {
                        UIManager.put("TextArea.font", null);
                    }
                    SwingUtilities.updateComponentTreeUI(APP_GUI_FRAME);
                    MAIN_SCROLL.setBorder(null);
                    if (object.equals(HiDPISimulator.SYSTEM_LAF_NAME)) {
                        if (jRadioButton2.isSelected()) {
                            jRadioButton.setSelected(true);
                        }
                        jRadioButton2.setEnabled(false);
                    } else if (object.equals(HiDPISimulator.SUBSTANCE_LAF_NAME)) {
                        jRadioButton2.setEnabled(true);
                    } else if (object.equals(HiDPISimulator.OCEAN_LAF_NAME)) {
                        if (jRadioButton2.isSelected()) {
                            jRadioButton.setSelected(true);
                        }
                        jRadioButton2.setEnabled(false);
                    }
                    HiDPISimulator.setDPI(CUR_DPI);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        jCheckBox.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                GUI_BUF = null;
                PAINT_GHOSTED = jCheckBox.isSelected();
                APP_GUI_FRAME.repaint();
            }
        });
        jRadioButton4.addItemListener(new ItemListener(){

            public void itemStateChanged(ItemEvent itemEvent) {
                GUI_BUF = null;
                SCALE_LAYOUT = jRadioButton4.isSelected();
                HiDPISimulator.setDPI(CUR_DPI);
            }
        });
        ItemListener itemListener = new ItemListener(){

            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange() == 1) {
                    GUI_BUF = null;
                    SCALE_LAF = jRadioButton2.isSelected();
                    SCALE_FONTS = jRadioButton.isSelected();
                    HiDPISimulator.setDPI(CUR_DPI);
                }
            }
        };
        jRadioButton2.addItemListener(itemListener);
        jRadioButton.addItemListener(itemListener);
        jRadioButton3.addItemListener(itemListener);
        jComboBox2.addItemListener(new ItemListener(){

            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange() == 1) {
                    GUI_BUF = null;
                    CUR_DPI = Integer.parseInt(jComboBox2.getSelectedItem().toString().substring(0, 3).trim());
                    HiDPISimulator.setDPI(CUR_DPI);
                }
            }
        });
        return jPanel;
    }

    private static void lockFont(Component ... componentArray) {
        for (Component component : componentArray) {
            Font font = component.getFont();
            component.setFont(font.deriveFont((float)font.getSize()));
        }
    }

    private static void revalidateGUI() {
        APP_GUI_FRAME.getContentPane().invalidate();
        APP_GUI_FRAME.repaint();
    }

    private static synchronized void setDPI(int n) {
        Object object;
        Set<Map.Entry<String, Font>> set;
        float f;
        float f2 = (float)n / (float)Toolkit.getDefaultToolkit().getScreenResolution();
        TEXT_AREA.setSize(0, 0);
        PlatformDefaults.setHorizontalScaleFactor(Float.valueOf(0.1f));
        PlatformDefaults.setHorizontalScaleFactor(SCALE_LAYOUT ? Float.valueOf(f2) : null);
        PlatformDefaults.setVerticalScaleFactor(SCALE_LAYOUT ? Float.valueOf(f2) : null);
        float f3 = f = SCALE_FONTS ? (float)n / (float)Toolkit.getDefaultToolkit().getScreenResolution() : 1.0f;
        if (ORIG_DEFAULTS == null) {
            ORIG_DEFAULTS = new HashMap();
            set = new HashSet(UIManager.getLookAndFeelDefaults().keySet());
            Iterator<Map.Entry<String, Font>> iterator = set.iterator();
            while (iterator.hasNext()) {
                String object2 = iterator.next().toString();
                object = UIManager.get(object2);
                if (!(object instanceof Font)) continue;
                ORIG_DEFAULTS.put(object2, (Font)object);
            }
        }
        set = ORIG_DEFAULTS.entrySet();
        for (Map.Entry<String, Font> entry : set) {
            object = entry.getValue();
            if (!SCALE_LAF) {
                UIManager.put(entry.getKey(), new FontUIResource(((Font)object).deriveFont((float)((Font)object).getSize() * f)));
                continue;
            }
            UIManager.put(entry.getKey(), null);
        }
        if (SCALE_LAF) {
            HiDPISimulator.scaleSubstanceLAF(f2);
        } else if (UIManager.getLookAndFeel().getName().toLowerCase().contains("substance")) {
            HiDPISimulator.scaleSubstanceLAF(1.0f);
        }
        SwingUtilities.updateComponentTreeUI(HiDPIDEMO_PANEL);
        HiDPISimulator.revalidateGUI();
    }

    private static void scaleSubstanceLAF(float f) {
        SubstanceLookAndFeel.setFontPolicy((FontPolicy)SubstanceFontUtilities.getScaledFontPolicy((float)f));
        try {
            UIManager.setLookAndFeel((LookAndFeel)new SubstanceBusinessBlackSteelLookAndFeel());
        }
        catch (Exception exception) {
            // empty catch block
        }
        SwingUtilities.updateComponentTreeUI(APP_GUI_FRAME);
        MAIN_SCROLL.setBorder(null);
    }

    public static void main(String[] stringArray) {
        try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "HiDPI Simulator");
        }
        catch (Exception exception) {
            // empty catch block
        }
        PlatformDefaults.setDefaultHorizontalUnit(1);
        PlatformDefaults.setDefaultVerticalUnit(2);
        SwingUtilities.invokeLater(new Runnable(){

            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (UIManager.getLookAndFeel().getName().toLowerCase().contains("windows")) {
                    UIManager.put("TextArea.font", UIManager.getFont("TextField.font"));
                }
                APP_GUI_FRAME = new JFrame("Resolution Independence Simulator");
                JPanel jPanel = new JPanel(new MigLayout("fill, insets 0px, nocache"));
                JPanel jPanel2 = new JPanel(new MigLayout("fill, insets 0px, nocache")){

                    public void paintComponent(Graphics graphics) {
                        Graphics2D graphics2D = (Graphics2D)graphics.create();
                        graphics2D.setPaint(new GradientPaint(0.0f, 0.0f, new Color(20, 20, 30), 0.0f, this.getHeight(), new Color(90, 90, 110), false));
                        graphics2D.fillRect(0, 0, this.getWidth(), this.getHeight());
                        graphics2D.setFont(graphics2D.getFont().deriveFont(1, 13.0f));
                        graphics2D.setPaint(Color.WHITE);
                        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        graphics2D.drawString("Left panel shows the scaled version. Right side shows how this would look on a HiDPI screen. It should look the same as the original panel!", 10, 19);
                        graphics2D.dispose();
                    }
                };
                HiDPIDEMO_PANEL = new HiDPIDemoPanel();
                SIM_PANEL = HiDPISimulator.createSimulator();
                MIRROR_PANEL = HiDPISimulator.createScaleMirror();
                MAIN_SCROLL = new JScrollPane(jPanel2);
                MAIN_SCROLL.setBorder(null);
                jPanel2.add((Component)HiDPIDEMO_PANEL, "align center center, split, span, width pref!");
                jPanel2.add((Component)MIRROR_PANEL, "id mirror, gap 20px!, width pref!");
                jPanel.add((Component)SIM_PANEL, "dock south");
                jPanel.add((Component)MAIN_SCROLL, "dock center");
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                APP_GUI_FRAME.setContentPane(jPanel);
                APP_GUI_FRAME.setSize(Math.min(1240, dimension.width), Math.min(950, dimension.height - 30));
                APP_GUI_FRAME.setDefaultCloseOperation(3);
                APP_GUI_FRAME.setLocationRelativeTo(null);
                APP_GUI_FRAME.setVisible(true);
            }
        });
    }

    static {
        SCALE_LAF = false;
        SCALE_FONTS = true;
        SCALE_LAYOUT = true;
        PAINT_GHOSTED = false;
        GUI_BUF = null;
        ORIG_GUI_BUF = null;
        CUR_DPI = PlatformDefaults.getDefaultDPI();
    }
}

