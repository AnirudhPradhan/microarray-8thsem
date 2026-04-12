/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.IDEUtil;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swing.MigLayout;

public class SwingDemo
extends JFrame {
    public static final int INITIAL_INDEX = 0;
    private static final boolean DEBUG = false;
    private static final boolean OPAQUE = false;
    private static final String[][] panels = new String[][]{{"Welcome", "\n\n         \"MigLayout makes complex layouts easy and normal layouts one-liners.\""}, {"Quick Start", "This is an example of how to build a common dialog type. Note that there are no special components, nested panels or absolute references to cell positions. If you look at the source code you will see that the layout code is very simple to understand."}, {"Plain", "A simple example on how simple it is to create normal forms. No builders needed since the whole layout manager works like a builder."}, {"Alignments", "Shows how the alignment of components are specified. At the top/left is the alignment for the column/row. The components have no alignments specified.\n\nNote that baseline alignment will be interpreted as 'center' before JDK 6."}, {"Cell Alignments", "Shows how components are aligned when both column/row alignments and component constraints are specified. At the top/left are the alignment for the column/row and the text on the buttons is the component constraint that will override the column/row alignment if it is an alignment.\n\nNote that baseline alignment will be interpreted as 'center' before JDK 6."}, {"Basic Sizes", "A simple example that shows how to use the column or row min/preferred/max size to set the sizes of the contained components and also an example that shows how to do this directly in the component constraints."}, {"Growing", "A simple example that shows how to use the growx and growy constraint to set the sizes and how they should grow to fit the available size. Both the column/row and the component grow/shrink constraints can be set, but the components will always be confined to the space given by its column/row."}, {"Grow Shrink", "Demonstrates the very flexible grow and shrink constraints that can be set on a component.\nComponents can be divided into grow/shrink groups and also have grow/shrink weight within each of those groups.\n\nBy default components shrink to their inherent (or specified) minimum size, but they don't grow."}, {"Span", "This example shows the powerful spanning and splitting that can be specified in the component constraints. With spanning any number of cells can be merged with the additional option to split that space for more than one component. This makes layouts very flexible and reduces the number of times you will need nested panels to very few."}, {"Flow Direction", "Shows the different flow directions. Flow direction for the layout specifies if the next cell will be in the x or y dimension. Note that it can be a different flow direction in the slit cell (the middle cell is slit in two). Wrap is set to 3 for all panels."}, {"Grouping", "Sizes for both components and columns/rows can be grouped so they get the same size. For instance buttons in a button bar can be given a size-group so that they will all get the same minimum and preferred size (the largest within the group). Size-groups can be set for the width, height or both."}, {"Units", "Demonstrates the basic units that are understood by MigLayout. These units can be extended by the user by adding one or more UnitConverter(s)."}, {"Component Sizes", "Minimum, preferred and maximum component sizes can be overridden in the component constraints using any unit type. The format to do this is short and simple to understand. You simply specify the min, preferred and max sizes with a colon between.\n\nAbove are some examples of this. An exclamation mark means that the value will be used for all sizes."}, {"Bound Sizes", "Shows how to create columns that are stable between tabs using minimum sizes."}, {"Cell Position", "Even though MigLayout has automatic grid flow you can still specify the cell position explicitly. You can even combine absolute (x, y) and flow (skip, wrap and newline) constraints to build your layout."}, {"Orientation", "MigLayout supports not only right-to-left orientation, but also bottom-to-top. You can even set the flow direction so that the flow is vertical instead of horizontal. It will automatically pick up if right-to-left is to be used depending on the ComponentWrapper, but it can also be manually set for every layout."}, {"Absolute Position", "Demonstrates the option to place any number of components using absolute coordinates. This can be just the position (if min/preferred size) using \"x y p p\" format orthe bounds using the \"x1 y1 x2 y2\" format. Any unit can be used and percent is relative to the parent.\nAbsolute components will not disturb the flow or occupy cells in the grid. Absolute positioned components will be taken into account when calculating the container's preferred size."}, {"Component Links", "Components can be linked to any side of any other component. It can be a forward, backward or cyclic link references, as long as it is stable and won't continue to change value over many iterations.Links are referencing the ID of another component. The ID can be overridden by the component's constrains or is provided by the ComponentWrapper. For instance it will use the component's 'name' on Swing.\nSince the links can be combined with any expression (such as 'butt1.x+10' or 'max(button.x, 200)' the links are very customizable."}, {"Docking", "Docking components can be added around the grid. The docked component will get the whole width/height on the docked side by default, however this can be overridden. When all docked components are laid out, whatever space is left will be available for the normal grid laid out components. Docked components does not in any way affect the flow in the grid.\n\nSince the docking runs in the same code path as the normal layout code the same properties can be specified for the docking components. You can for instance set the sizes and alignment or link other components to their docked component's bounds."}, {"Button Bars", "Button order is very customizable and are by default different on the supported platforms. E.g. Gaps, button order and minimum button size are properties that are 'per platform'. MigLayout picks up the current platform automatically and adjusts the button order and minimum button size accordingly, all without using a button builder or any other special code construct."}, {"Visual Bounds", "Human perceptible bounds may not be the same as the mathematical bounds for the component. This is for instance the case if there is a drop shadow painted by the component's border. MigLayout can compensate for this in a simple way. Note the top middle tab-component, it is not aligned visually correct on Windows XP. For the second tab the bounds are corrected automatically on Windows XP."}, {"Debug", "Demonstrates the non-intrusive way to get visual debugging aid. There is no need to use a special DebugPanel or anything that will need code changes. The user can simply turn on debug on the layout manager by using the \"debug\" constraint and it will continuously repaint the panel with debug information on top. This means you don't have to change your code to debug!"}, {"Layout Showdown", "This is an implementation of the Layout Showdown posted on java.net by John O'Conner. The first tab is a pure implemenetation of the showdown that follows all the rules. The second tab is a slightly fixed version that follows some improved layout guidelines.The source code is for both the first and for the fixed version. Note the simplification of the code for the fixed version. Writing better layouts with MiG Layout is reasier that writing bad.\n\nReference: http://weblogs.java.net/blog/joconner/archive/2006/10/more_informatio.html"}, {"API Constraints1", "This dialog shows the constraint API added to v2.0. It works the same way as the string constraints but with chained method calls. See the source code for details."}, {"API Constraints2", "This dialog shows the constraint API added to v2.0. It works the same way as the string constraints but with chained method calls. See the source code for details."}};
    private int lastIndex = -10;
    private JPanel contentPanel = new JPanel(new MigLayout("wrap", "[]unrel[grow]", "[grow][pref]"));
    private JTabbedPane layoutPickerTabPane = new JTabbedPane();
    private JList pickerList = new JList(new DefaultListModel());
    private JTabbedPane southTabPane = new JTabbedPane();
    private JScrollPane descrTextAreaScroll = this.createTextAreaScroll("", 5, 80, true);
    private JTextArea descrTextArea = (JTextArea)this.descrTextAreaScroll.getViewport().getView();
    private JScrollPane sourceTextAreaScroll = null;
    private JTextArea sourceTextArea = null;
    private JPanel layoutDisplayPanel = new JPanel(new BorderLayout(0, 0));
    private static boolean buttonOpaque = true;
    private static boolean contentAreaFilled = true;
    private JFrame sourceFrame = null;
    private JTextArea sourceFrameTextArea = null;
    private static int benchRuns = 0;
    private static long startupMillis = 0L;
    private static long timeToShowMillis = 0L;
    private static long benchRunTime = 0L;
    private static String benchOutFileName = null;
    private static boolean append = false;
    private static long lastRunTimeStart = 0L;
    private static StringBuffer runTimeSB = null;
    private final ToolTipListener toolTipListener = new ToolTipListener();
    private final ConstraintListener constraintListener = new ConstraintListener();
    private static final Font BUTT_FONT = new Font("monospaced", 0, 12);
    static final Color LABEL_COLOR = new Color(0, 70, 213);

    public static void main(String[] stringArray) {
        try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MiGLayout Swing Demo");
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        startupMillis = System.currentTimeMillis();
        String string = UIManager.getSystemLookAndFeelClassName();
        if (stringArray.length > 0) {
            for (int i = 0; i < stringArray.length; ++i) {
                String string2 = stringArray[i].trim();
                if (string2.startsWith("-laf")) {
                    string = string2.substring(4);
                    continue;
                }
                if (string2.startsWith("-bench")) {
                    benchRuns = 10;
                    try {
                        benchRuns = Integer.parseInt(string2.substring(6));
                    }
                    catch (Exception exception) {}
                    continue;
                }
                if (string2.startsWith("-bout")) {
                    benchOutFileName = string2.substring(5);
                    continue;
                }
                if (string2.startsWith("-append")) {
                    append = true;
                    continue;
                }
                if (string2.startsWith("-verbose")) {
                    runTimeSB = new StringBuffer(256);
                    continue;
                }
                if (string2.equals("-steel")) {
                    string = "javax.swing.plaf.metal.MetalLookAndFeel";
                    System.setProperty("swing.metalTheme", "steel");
                    continue;
                }
                if (string2.equals("-ocean")) {
                    string = "javax.swing.plaf.metal.MetalLookAndFeel";
                    continue;
                }
                System.out.println("Usage: [-laf[look_&_feel_class_name]] [-bench[#_of_runs]] [-bout[benchmark_results_filename]] [-append] [-steel] [-ocean]\n -laf    Set the Application Look&Feel. (Look and feel must be in Classpath)\n -bench  Run demo as benchmark. Run count can be appended. 10 is default.\n -bout   Benchmark results output filename.\n -append Appends the result to the -bout file.\n -verbose Print the times of every run.\n -steel  Sets the old Steel theme for Sun's Metal look&feel.\n -ocean  Sets the Ocean theme for Sun's Metal look&feel.\n\nExamples:\n java -jar swingdemoapp.jar -bench -boutC:/bench.txt -append\n java -jar swingdemoapp.jar -ocean -bench20\n java -cp c:\\looks-2.0.4.jar;.\\swingdemoapp.jar net.miginfocom.demo.SwingDemo -lafcom.jgoodies.looks.plastic.PlasticLookAndFeel -bench20 -boutC:/bench.txt");
                System.exit(0);
            }
        }
        if (benchRuns == 0) {
            LayoutUtil.setDesignTime(null, true);
        }
        if (string.endsWith("WindowsLookAndFeel")) {
            buttonOpaque = false;
        }
        if (string.endsWith("AquaLookAndFeel")) {
            contentAreaFilled = false;
        }
        final String string3 = string;
        SwingUtilities.invokeLater(new Runnable(){

            public void run() {
                try {
                    UIManager.setLookAndFeel(string3);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                new SwingDemo();
            }
        });
    }

    public SwingDemo() {
        super("MigLayout Swing Demo v2.5 - Mig Layout v" + LayoutUtil.getVersion());
        if (benchRuns == 0) {
            this.sourceTextAreaScroll = this.createTextAreaScroll("", 5, 80, true);
            this.sourceTextArea = (JTextArea)this.sourceTextAreaScroll.getViewport().getView();
        }
        this.contentPanel.add((Component)this.layoutPickerTabPane, "spany,grow");
        this.contentPanel.add((Component)this.layoutDisplayPanel, "grow");
        this.contentPanel.add((Component)this.southTabPane, "growx");
        this.setContentPane(this.contentPanel);
        this.pickerList.setOpaque(false);
        ((DefaultListCellRenderer)this.pickerList.getCellRenderer()).setOpaque(false);
        this.pickerList.setSelectionForeground(new Color(0, 0, 220));
        this.pickerList.setBackground(null);
        this.pickerList.setBorder(new EmptyBorder(2, 5, 0, 4));
        this.pickerList.setFont(this.pickerList.getFont().deriveFont(1));
        this.layoutPickerTabPane.addTab("Example Browser", this.pickerList);
        this.descrTextAreaScroll.setBorder(null);
        this.descrTextAreaScroll.setOpaque(false);
        this.descrTextAreaScroll.getViewport().setOpaque(false);
        this.descrTextArea.setOpaque(false);
        this.descrTextArea.setEditable(false);
        this.descrTextArea.setBorder(new EmptyBorder(0, 4, 0, 4));
        this.southTabPane.addTab("Description", this.descrTextAreaScroll);
        if (this.sourceTextArea != null) {
            this.sourceTextAreaScroll.setBorder(null);
            this.sourceTextAreaScroll.setOpaque(false);
            this.sourceTextAreaScroll.getViewport().setOpaque(false);
            this.sourceTextAreaScroll.setHorizontalScrollBarPolicy(30);
            this.sourceTextArea.setOpaque(false);
            this.sourceTextArea.setLineWrap(false);
            this.sourceTextArea.setWrapStyleWord(false);
            this.sourceTextArea.setEditable(false);
            this.sourceTextArea.setBorder(new EmptyBorder(0, 4, 0, 4));
            this.sourceTextArea.setFont(new Font("monospaced", 0, 11));
            this.southTabPane.addTab("Source Code", this.sourceTextAreaScroll);
            this.southTabPane.addMouseListener(new MouseAdapter(){

                public void mouseClicked(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        SwingDemo.this.showSourceInFrame();
                    }
                }
            });
        }
        for (int i = 0; i < panels.length; ++i) {
            ((DefaultListModel)this.pickerList.getModel()).addElement(panels[i][0]);
        }
        try {
            if (UIManager.getLookAndFeel().getID().equals("Aqua")) {
                this.setSize(1000, 750);
            } else {
                this.setSize(900, 650);
            }
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(3);
            this.setVisible(true);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            System.exit(1);
        }
        this.pickerList.addListSelectionListener(new ListSelectionListener(){

            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                int n = SwingDemo.this.pickerList.getSelectedIndex();
                if (n == -1 || SwingDemo.this.lastIndex == n) {
                    return;
                }
                SwingDemo.this.lastIndex = n;
                String string = "create" + panels[n][0].replace(' ', '_');
                SwingDemo.this.layoutDisplayPanel.removeAll();
                try {
                    SwingDemo.this.pickerList.requestFocusInWindow();
                    JComponent jComponent = (JComponent)SwingDemo.class.getMethod(string, new Class[0]).invoke((Object)SwingDemo.this, new Object[0]);
                    SwingDemo.this.layoutDisplayPanel.add(jComponent);
                    SwingDemo.this.descrTextArea.setText(panels[n][1]);
                    SwingDemo.this.descrTextArea.setCaretPosition(0);
                    SwingDemo.this.contentPanel.revalidate();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                SwingDemo.this.southTabPane.setSelectedIndex(0);
            }
        });
        this.pickerList.requestFocusInWindow();
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        if (benchRuns > 0) {
            this.doBenchmark();
        } else {
            this.pickerList.setSelectedIndex(0);
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher(){

                public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                    if (keyEvent.getID() == 401 && keyEvent.getKeyCode() == 66 && (keyEvent.getModifiersEx() & 0x80) > 0) {
                        startupMillis = System.currentTimeMillis();
                        timeToShowMillis = System.currentTimeMillis() - startupMillis;
                        benchRuns = 1;
                        SwingDemo.this.doBenchmark();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void doBenchmark() {
        Thread thread = new Thread(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            public void run() {
                for (int i = 0; i < benchRuns; ++i) {
                    lastRunTimeStart = System.currentTimeMillis();
                    int n = SwingDemo.this.pickerList.getModel().getSize();
                    for (int j = 0; j < n; ++j) {
                        if (benchRuns > 0 && panels[j][0].equals("Visual Bounds")) continue;
                        final int n2 = j;
                        try {
                            SwingUtilities.invokeAndWait(new Runnable(){

                                public void run() {
                                    SwingDemo.this.pickerList.setSelectedIndex(n2);
                                    Toolkit.getDefaultToolkit().sync();
                                }
                            });
                        }
                        catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        Component[] componentArray = SwingDemo.this.layoutDisplayPanel.getComponents();
                        for (int k = 0; k < componentArray.length; ++k) {
                            if (!(componentArray[k] instanceof JTabbedPane)) continue;
                            final JTabbedPane jTabbedPane = (JTabbedPane)componentArray[k];
                            int n3 = jTabbedPane.getTabCount();
                            for (int i2 = 0; i2 < n3; ++i2) {
                                final int n4 = i2;
                                try {
                                    SwingUtilities.invokeAndWait(new Runnable(){

                                        public void run() {
                                            jTabbedPane.setSelectedIndex(n4);
                                            Toolkit.getDefaultToolkit().sync();
                                            if (timeToShowMillis == 0L) {
                                                timeToShowMillis = System.currentTimeMillis() - startupMillis;
                                            }
                                        }
                                    });
                                    continue;
                                }
                                catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            }
                        }
                    }
                    if (runTimeSB == null) continue;
                    runTimeSB.append("Run ").append(i).append(": ");
                    runTimeSB.append(System.currentTimeMillis() - lastRunTimeStart).append(" millis.\n");
                }
                benchRunTime = System.currentTimeMillis() - startupMillis - timeToShowMillis;
                String string = "Java Version:       " + System.getProperty("java.version") + "\n" + "Look & Feel:        " + UIManager.getLookAndFeel().getDescription() + "\n" + "Time to Show:       " + timeToShowMillis + " millis.\n" + (runTimeSB != null ? runTimeSB.toString() : "") + "Benchmark Run Time: " + benchRunTime + " millis.\n" + "Average Run Time:   " + benchRunTime / (long)benchRuns + " millis (" + benchRuns + " runs).\n\n";
                if (benchOutFileName == null) {
                    JOptionPane.showMessageDialog(SwingDemo.this, string, "Results", 1);
                } else {
                    FileWriter fileWriter = null;
                    try {
                        fileWriter = new FileWriter(benchOutFileName, append);
                        fileWriter.write(string);
                    }
                    catch (IOException iOException) {
                        iOException.printStackTrace();
                    }
                    finally {
                        if (fileWriter != null) {
                            try {
                                fileWriter.close();
                            }
                            catch (IOException iOException) {}
                        }
                    }
                }
                System.out.println(string);
            }
        };
        thread.start();
    }

    private void setSource(String string) {
        if (benchRuns > 0 || this.sourceTextArea == null) {
            return;
        }
        if (string.length() > 0) {
            string = string.replaceAll("\t\t", "");
            string = "DOUBLE CLICK TAB TO SHOW SOURCE IN SEPARATE WINDOW!\n===================================================\n\n" + string;
        }
        this.sourceTextArea.setText(string);
        this.sourceTextArea.setCaretPosition(0);
        if (this.sourceFrame != null && this.sourceFrame.isVisible()) {
            this.sourceFrameTextArea.setText(string.length() > 105 ? string.substring(105) : "No Source Code Available!");
            this.sourceFrameTextArea.setCaretPosition(0);
        }
    }

    private void showSourceInFrame() {
        if (this.sourceTextArea == null) {
            return;
        }
        JScrollPane jScrollPane = this.createTextAreaScroll("", 5, 80, true);
        this.sourceFrameTextArea = (JTextArea)jScrollPane.getViewport().getView();
        jScrollPane.setBorder(null);
        jScrollPane.setHorizontalScrollBarPolicy(30);
        this.sourceFrameTextArea.setLineWrap(false);
        this.sourceFrameTextArea.setWrapStyleWord(false);
        this.sourceFrameTextArea.setEditable(false);
        this.sourceFrameTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.sourceFrameTextArea.setFont(new Font("monospaced", 0, 12));
        String string = this.sourceTextArea.getText();
        this.sourceFrameTextArea.setText(string.length() > 105 ? string.substring(105) : "No Source Code Available!");
        this.sourceFrameTextArea.setCaretPosition(0);
        this.sourceFrame = new JFrame("Source Code");
        this.sourceFrame.getContentPane().add((Component)jScrollPane, "Center");
        this.sourceFrame.setSize(700, 800);
        this.sourceFrame.setLocationRelativeTo(this);
        this.sourceFrame.setVisible(true);
    }

    public JComponent createTest() {
        JPanel jPanel = new JPanel();
        MigLayout migLayout = new MigLayout();
        jPanel.setLayout(migLayout);
        return jPanel;
    }

    public JComponent createAPI_Constraints1() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        LC lC = new LC().fill().wrap();
        AC aC = new AC().align("right", 1).fill(2, 4).grow(100.0f, 2, 4).align("right", 3).gap("15", 2);
        AC aC2 = new AC().align("top", 7).gap("15!", 6).grow(100.0f, 8);
        JPanel jPanel = this.createTabPanel(new MigLayout(lC, aC, aC2));
        JScrollPane jScrollPane = new JScrollPane(new JList<String>(new String[]{"Mouse, Mickey"}));
        jPanel.add((Component)jScrollPane, new CC().spanY().growY().minWidth("150").gapX(null, "10"));
        jPanel.add(new JLabel("Last Name"));
        jPanel.add(new JTextField());
        jPanel.add(new JLabel("First Name"));
        jPanel.add((Component)new JTextField(), new CC().wrap().alignX("right"));
        jPanel.add(new JLabel("Phone"));
        jPanel.add(new JTextField());
        jPanel.add(new JLabel("Email"));
        jPanel.add(new JTextField());
        jPanel.add(new JLabel("Address 1"));
        jPanel.add((Component)new JTextField(), new CC().spanX().growX());
        jPanel.add(new JLabel("Address 2"));
        jPanel.add((Component)new JTextField(), new CC().spanX().growX());
        jPanel.add(new JLabel("City"));
        jPanel.add((Component)new JTextField(), new CC().wrap());
        jPanel.add(new JLabel("State"));
        jPanel.add(new JTextField());
        jPanel.add(new JLabel("Postal Code"));
        jPanel.add((Component)new JTextField(10), new CC().spanX(2).growX(0.0f));
        jPanel.add(new JLabel("Country"));
        jPanel.add((Component)new JTextField(), new CC().wrap());
        jPanel.add((Component)new JButton("New"), new CC().spanX(5).split(5).tag("other"));
        jPanel.add((Component)new JButton("Delete"), new CC().tag("other"));
        jPanel.add((Component)new JButton("Edit"), new CC().tag("other"));
        jPanel.add((Component)new JButton("Save"), new CC().tag("other"));
        jPanel.add((Component)new JButton("Cancel"), new CC().tag("cancel"));
        jTabbedPane.addTab("Layout Showdown (improved)", jPanel);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\nLC layC = new LC().fill().wrap();\nAC colC = new AC().align(\"right\", 1).fill(2, 4).grow(100, 2, 4).align(\"right\", 3).gap(\"15\", 2);\nAC rowC = new AC().align(\"top\", 7).gap(\"15!\", 6).grow(100, 8);\n\nJPanel panel = createTabPanel(new MigLayout(layC, colC, rowC));    // Makes the background gradient\n\n// References to text fields not stored to reduce code clutter.\n\nJScrollPane list2 = new JScrollPane(new JList(new String[] {\"Mouse, Mickey\"}));\npanel.add(list2,                     new CC().spanY().growY().minWidth(\"150\").gapX(null, \"10\"));\n\npanel.add(new JLabel(\"Last Name\"));\npanel.add(new JTextField());\npanel.add(new JLabel(\"First Name\"));\npanel.add(new JTextField(),          new CC().wrap().alignX(\"right\"));\npanel.add(new JLabel(\"Phone\"));\npanel.add(new JTextField());\npanel.add(new JLabel(\"Email\"));\npanel.add(new JTextField());\npanel.add(new JLabel(\"Address 1\"));\npanel.add(new JTextField(),          new CC().spanX().growX());\npanel.add(new JLabel(\"Address 2\"));\npanel.add(new JTextField(),          new CC().spanX().growX());\npanel.add(new JLabel(\"City\"));\npanel.add(new JTextField(),          new CC().wrap());\npanel.add(new JLabel(\"State\"));\npanel.add(new JTextField());\npanel.add(new JLabel(\"Postal Code\"));\npanel.add(new JTextField(10),        new CC().spanX(2).growX(0));\npanel.add(new JLabel(\"Country\"));\npanel.add(new JTextField(),          new CC().wrap());\n\npanel.add(new JButton(\"New\"),        new CC().spanX(5).split(5).tag(\"other\"));\npanel.add(new JButton(\"Delete\"),     new CC().tag(\"other\"));\npanel.add(new JButton(\"Edit\"),       new CC().tag(\"other\"));\npanel.add(new JButton(\"Save\"),       new CC().tag(\"other\"));\npanel.add(new JButton(\"Cancel\"),     new CC().tag(\"cancel\"));\n\ntabbedPane.addTab(\"Layout Showdown (improved)\", panel);");
        return jTabbedPane;
    }

    public JComponent createAPI_Constraints2() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        LC lC = new LC().fill().wrap();
        AC aC = new AC().align("right", 0).fill(1, 3).grow(100.0f, 1, 3).align("right", 2).gap("15", 1);
        AC aC2 = new AC().index(6).gap("15!").align("top").grow(100.0f, 8);
        JPanel jPanel = this.createTabPanel(new MigLayout(lC, aC, aC2));
        jPanel.add(new JLabel("Last Name"));
        jPanel.add(new JTextField());
        jPanel.add(new JLabel("First Name"));
        jPanel.add((Component)new JTextField(), new CC().wrap());
        jPanel.add(new JLabel("Phone"));
        jPanel.add(new JTextField());
        jPanel.add(new JLabel("Email"));
        jPanel.add(new JTextField());
        jPanel.add(new JLabel("Address 1"));
        jPanel.add((Component)new JTextField(), new CC().spanX().growX());
        jPanel.add(new JLabel("Address 2"));
        jPanel.add((Component)new JTextField(), new CC().spanX().growX());
        jPanel.add(new JLabel("City"));
        jPanel.add((Component)new JTextField(), new CC().wrap());
        jPanel.add(new JLabel("State"));
        jPanel.add(new JTextField());
        jPanel.add(new JLabel("Postal Code"));
        jPanel.add((Component)new JTextField(10), new CC().spanX(2).growX(0.0f));
        jPanel.add(new JLabel("Country"));
        jPanel.add((Component)new JTextField(), new CC().wrap());
        jPanel.add((Component)this.createButton("New"), new CC().spanX(5).split(5).tag("other"));
        jPanel.add((Component)this.createButton("Delete"), new CC().tag("other"));
        jPanel.add((Component)this.createButton("Edit"), new CC().tag("other"));
        jPanel.add((Component)this.createButton("Save"), new CC().tag("other"));
        jPanel.add((Component)this.createButton("Cancel"), new CC().tag("cancel"));
        jTabbedPane.addTab("Layout Showdown (improved)", jPanel);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\nLC layC = new LC().fill().wrap();\nAC colC = new AC().align(\"right\", 0).fill(1, 3).grow(100, 1, 3).align(\"right\", 2).gap(\"15\", 1);\nAC rowC = new AC().index(6).gap(\"15!\").align(\"top\").grow(100, 8);\n\nJPanel panel = createTabPanel(new MigLayout(layC, colC, rowC));    // Makes the background gradient\n\n// References to text fields not stored to reduce code clutter.\n\npanel.add(new JLabel(\"Last Name\"));\npanel.add(new JTextField());\npanel.add(new JLabel(\"First Name\"));\npanel.add(new JTextField(),          new CC().wrap());\npanel.add(new JLabel(\"Phone\"));\npanel.add(new JTextField());\npanel.add(new JLabel(\"Email\"));\npanel.add(new JTextField());\npanel.add(new JLabel(\"Address 1\"));\npanel.add(new JTextField(),          new CC().spanX().growX());\npanel.add(new JLabel(\"Address 2\"));\npanel.add(new JTextField(),          new CC().spanX().growX());\npanel.add(new JLabel(\"City\"));\npanel.add(new JTextField(),          new CC().wrap());\npanel.add(new JLabel(\"State\"));\npanel.add(new JTextField());\npanel.add(new JLabel(\"Postal Code\"));\npanel.add(new JTextField(10),        new CC().spanX(2).growX(0));\npanel.add(new JLabel(\"Country\"));\npanel.add(new JTextField(),          new CC().wrap());\n\npanel.add(createButton(\"New\"),        new CC().spanX(5).split(5).tag(\"other\"));\npanel.add(createButton(\"Delete\"),     new CC().tag(\"other\"));\npanel.add(createButton(\"Edit\"),       new CC().tag(\"other\"));\npanel.add(createButton(\"Save\"),       new CC().tag(\"other\"));\npanel.add(createButton(\"Cancel\"),     new CC().tag(\"cancel\"));\n\ntabbedPane.addTab(\"Layout Showdown (improved)\", panel);");
        return jTabbedPane;
    }

    public JComponent createLayout_Showdown() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        JPanel jPanel = this.createTabPanel(new MigLayout("", "[]15[][grow,fill]15[grow]"));
        JScrollPane jScrollPane = new JScrollPane(new JList<String>(new String[]{"Mouse, Mickey"}));
        jPanel.add((Component)jScrollPane, "spany, growy, wmin 150");
        jPanel.add(new JLabel("Last Name"));
        jPanel.add(new JTextField());
        jPanel.add((Component)new JLabel("First Name"), "split");
        jPanel.add((Component)new JTextField(), "growx, wrap");
        jPanel.add(new JLabel("Phone"));
        jPanel.add(new JTextField());
        jPanel.add((Component)new JLabel("Email"), "split");
        jPanel.add((Component)new JTextField(), "growx, wrap");
        jPanel.add(new JLabel("Address 1"));
        jPanel.add((Component)new JTextField(), "span, growx");
        jPanel.add(new JLabel("Address 2"));
        jPanel.add((Component)new JTextField(), "span, growx");
        jPanel.add(new JLabel("City"));
        jPanel.add((Component)new JTextField(), "wrap");
        jPanel.add(new JLabel("State"));
        jPanel.add(new JTextField());
        jPanel.add((Component)new JLabel("Postal Code"), "split");
        jPanel.add((Component)new JTextField(), "growx, wrap");
        jPanel.add(new JLabel("Country"));
        jPanel.add((Component)new JTextField(), "wrap 15");
        jPanel.add((Component)this.createButton("New"), "span, split, align left");
        jPanel.add((Component)this.createButton("Delete"), "");
        jPanel.add((Component)this.createButton("Edit"), "");
        jPanel.add((Component)this.createButton("Save"), "");
        jPanel.add((Component)this.createButton("Cancel"), "wrap push");
        jTabbedPane.addTab("Layout Showdown (pure)", jPanel);
        JPanel jPanel2 = this.createTabPanel(new MigLayout("", "[]15[][grow,fill]15[][grow,fill]"));
        JScrollPane jScrollPane2 = new JScrollPane(new JList<String>(new String[]{"Mouse, Mickey"}));
        jPanel2.add((Component)jScrollPane2, "spany, growy, wmin 150");
        jPanel2.add(new JLabel("Last Name"));
        jPanel2.add(new JTextField());
        jPanel2.add(new JLabel("First Name"));
        jPanel2.add((Component)new JTextField(), "wrap");
        jPanel2.add(new JLabel("Phone"));
        jPanel2.add(new JTextField());
        jPanel2.add(new JLabel("Email"));
        jPanel2.add((Component)new JTextField(), "wrap");
        jPanel2.add(new JLabel("Address 1"));
        jPanel2.add((Component)new JTextField(), "span");
        jPanel2.add(new JLabel("Address 2"));
        jPanel2.add((Component)new JTextField(), "span");
        jPanel2.add(new JLabel("City"));
        jPanel2.add((Component)new JTextField(), "wrap");
        jPanel2.add(new JLabel("State"));
        jPanel2.add(new JTextField());
        jPanel2.add(new JLabel("Postal Code"));
        jPanel2.add((Component)new JTextField(10), "growx 0, wrap");
        jPanel2.add(new JLabel("Country"));
        jPanel2.add((Component)new JTextField(), "wrap 15");
        jPanel2.add((Component)this.createButton("New"), "tag other, span, split");
        jPanel2.add((Component)this.createButton("Delete"), "tag other");
        jPanel2.add((Component)this.createButton("Edit"), "tag other");
        jPanel2.add((Component)this.createButton("Save"), "tag other");
        jPanel2.add((Component)this.createButton("Cancel"), "tag cancel, wrap push");
        jTabbedPane.addTab("Layout Showdown (improved)", jPanel2);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\nJPanel p = createTabPanel(new MigLayout(\"\", \"[]15[][grow,fill]15[grow]\"));\n\nJScrollPane list1 = new JScrollPane(new JList(new String[] {\"Mouse, Mickey\"}));\n\np.add(list1,                     \"spany, growy, wmin 150\");\np.add(new JLabel(\"Last Name\"));\np.add(new JTextField());\np.add(new JLabel(\"First Name\"),  \"split\");  // split divides the cell\np.add(new JTextField(),          \"growx, wrap\");\np.add(new JLabel(\"Phone\"));\np.add(new JTextField());\np.add(new JLabel(\"Email\"),       \"split\");\np.add(new JTextField(),          \"growx, wrap\");\np.add(new JLabel(\"Address 1\"));\np.add(new JTextField(),          \"span, growx\"); // span merges cells\np.add(new JLabel(\"Address 2\"));\np.add(new JTextField(),          \"span, growx\");\np.add(new JLabel(\"City\"));\np.add(new JTextField(),          \"wrap\"); // wrap continues on next line\np.add(new JLabel(\"State\"));\np.add(new JTextField());\np.add(new JLabel(\"Postal Code\"), \"split\");\np.add(new JTextField(),          \"growx, wrap\");\np.add(new JLabel(\"Country\"));\np.add(new JTextField(),          \"wrap 15\");\n\np.add(createButton(\"New\"),        \"span, split, align left\");\np.add(createButton(\"Delete\"),     \"\");\np.add(createButton(\"Edit\"),       \"\");\np.add(createButton(\"Save\"),       \"\");\np.add(createButton(\"Cancel\"),     \"wrap push\");\n\ntabbedPane.addTab(\"Layout Showdown (pure)\", p);\n\t\t// Fixed version *******************************************\nJPanel p2 = createTabPanel(new MigLayout(\"\", \"[]15[][grow,fill]15[][grow,fill]\"));    // Makes the background gradient\n\n// References to text fields not stored to reduce code clutter.\n\nJScrollPane list2 = new JScrollPane(new JList(new String[] {\"Mouse, Mickey\"}));\np2.add(list2,                     \"spany, growy, wmin 150\");\np2.add(new JLabel(\"Last Name\"));\np2.add(new JTextField());\np2.add(new JLabel(\"First Name\"));\np2.add(new JTextField(),          \"wrap\");\np2.add(new JLabel(\"Phone\"));\np2.add(new JTextField());\np2.add(new JLabel(\"Email\"));\np2.add(new JTextField(),          \"wrap\");\np2.add(new JLabel(\"Address 1\"));\np2.add(new JTextField(),          \"span\");\np2.add(new JLabel(\"Address 2\"));\np2.add(new JTextField(),          \"span\");\np2.add(new JLabel(\"City\"));\np2.add(new JTextField(),          \"wrap\");\np2.add(new JLabel(\"State\"));\np2.add(new JTextField());\np2.add(new JLabel(\"Postal Code\"));\np2.add(new JTextField(10),        \"growx 0, wrap\");\np2.add(new JLabel(\"Country\"));\np2.add(new JTextField(),          \"wrap 15\");\n\np2.add(createButton(\"New\"),        \"tag other, span, split\");\np2.add(createButton(\"Delete\"),     \"tag other\");\np2.add(createButton(\"Edit\"),       \"tag other\");\np2.add(createButton(\"Save\"),       \"tag other\");\np2.add(createButton(\"Cancel\"),     \"tag cancel, wrap push\");\n\ntabbedPane.addTab(\"Layout Showdown (improved)\", p2);");
        return jTabbedPane;
    }

    public JComponent createWelcome() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        MigLayout migLayout = new MigLayout("ins 20, fill", "", "[grow]unrel[]");
        JPanel jPanel = this.createTabPanel(migLayout);
        String string = "MigLayout's main purpose is to make layouts for SWT and Swing, and possibly other frameworks, much more powerful and a lot easier to create, especially for manual coding.\n\nThe motto is: \"MigLayout makes complex layouts easy and normal layouts one-liners.\"\n\nThe layout engine is very flexible and advanced, something that is needed to make it simple to use yet handle almost all layout use-cases.\n\nMigLayout can handle all layouts that the commonly used Swing Layout Managers can handle and this with a lot of extra features. It also incorporates most, if not all, of the open source alternatives FormLayout's and TableLayout's functionality.\n\n\nThanks to Karsten Lentzsch of JGoodies.com for allowing the reuse of the main demo application layout and for his inspiring talks that led to this layout Manager.\n\n\nMikael Grev\nMiG InfoCom AB\nmiglayout@miginfocom.com";
        JTextArea jTextArea = new JTextArea(string);
        jTextArea.setEditable(false);
        jTextArea.setSize(400, 400);
        if (PlatformDefaults.getCurrentPlatform() == 0) {
            jTextArea.setFont(new Font("tahoma", 1, 11));
        }
        jTextArea.setOpaque(false);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setLineWrap(true);
        JLabel jLabel = new JLabel("You can Right Click any Component or Container to change the constraints for it!");
        jLabel.setForeground(new Color(200, 0, 0));
        jPanel.add((Component)jTextArea, "wmin 500, ay top, grow, push, wrap");
        jPanel.add((Component)jLabel, "growx");
        jLabel.setFont(jLabel.getFont().deriveFont(1));
        jTabbedPane.addTab("Welcome", jPanel);
        this.setSource("");
        return jTabbedPane;
    }

    public JComponent createVisual_Bounds() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        JPanel jPanel = this.createTabPanel(new MigLayout("fill, ins 3, novisualpadding"));
        jPanel.setBorder(new LineBorder(Color.BLACK));
        JTabbedPane jTabbedPane2 = new JTabbedPane();
        JPanel jPanel2 = new JPanel();
        jPanel2.setBackground(Color.WHITE);
        jTabbedPane2.addTab("Demo Tab", jPanel2);
        jPanel.add((Component)this.createTextArea("A JTextArea", 1, 100), "grow, aligny bottom, wmin 100");
        jPanel.add((Component)jTabbedPane2, "grow, aligny bottom");
        jPanel.add((Component)this.createTextField("A JTextField", 100), "grow, aligny bottom, wmin 100");
        jPanel.add((Component)this.createTextArea("A JTextArea", 1, 100), "newline,grow, aligny bottom, wmin 100");
        jPanel.add((Component)this.createTextArea("A JTextArea", 1, 100), "grow, aligny bottom, wmin 100");
        jPanel.add((Component)this.createTextArea("A JTextArea", 1, 100), "grow, aligny bottom, wmin 100");
        JPanel jPanel3 = this.createTabPanel(new MigLayout("center,center,fill,ins 3"));
        jPanel3.setBorder(new LineBorder(Color.BLACK));
        JTabbedPane jTabbedPane3 = new JTabbedPane();
        JPanel jPanel4 = new JPanel();
        jPanel4.setBackground(Color.WHITE);
        jTabbedPane3.addTab("Demo Tab", jPanel4);
        jPanel3.add((Component)this.createTextArea("A JTextArea", 1, 100), "grow, aligny bottom, wmin 100");
        jPanel3.add((Component)jTabbedPane3, "grow, aligny bottom");
        jPanel3.add((Component)this.createTextField("A JTextField", 100), "grow, aligny bottom, wmin 100");
        jPanel3.add((Component)this.createTextArea("A JTextArea", 1, 100), "newline,grow, aligny bottom, wmin 100");
        jPanel3.add((Component)this.createTextArea("A JTextArea", 1, 100), "grow, aligny bottom, wmin 100");
        jPanel3.add((Component)this.createTextArea("A JTextArea", 1, 100), "grow, aligny bottom, wmin 100");
        jTabbedPane.addTab("Visual Bounds (Not Corrected)", jPanel);
        jTabbedPane.addTab("Visual Bounds (Corrected on XP)", jPanel3);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\n// \"NON\"-corrected bounds\nJPanel p1 = createTabPanel(new MigLayout(\"fill, ins 3, novisualpadding\"));\np1.setBorder(new LineBorder(Color.BLACK));\n\nJTabbedPane demoPane2 = new JTabbedPane();\nJPanel demoPanel2 = new JPanel();\ndemoPanel2.setBackground(Color.WHITE);\ndemoPane2.addTab(\"Demo Tab\", demoPanel2);\n\np1.add(createTextArea(\"A JTextArea\", 1, 100), \"grow, aligny bottom, wmin 100\");\np1.add(demoPane2, \"grow, aligny bottom\");\np1.add(createTextField(\"A JTextField\", 100), \"grow, aligny bottom, wmin 100\");\np1.add(createTextArea(\"A JTextArea\", 1, 100), \"newline,grow, aligny bottom, wmin 100\");\np1.add(createTextArea(\"A JTextArea\", 1, 100), \"grow, aligny bottom, wmin 100\");\np1.add(createTextArea(\"A JTextArea\", 1, 100), \"grow, aligny bottom, wmin 100\");\n\nJPanel p2 = createTabPanel(new MigLayout(\"center,center,fill,ins 3\"));\np2.setBorder(new LineBorder(Color.BLACK));\n\nJTabbedPane demoPane = new JTabbedPane();\nJPanel demoPanel = new JPanel();\ndemoPanel.setBackground(Color.WHITE);\ndemoPane.addTab(\"Demo Tab\", demoPanel);\n\np2.add(createTextArea(\"A JTextArea\", 1, 100), \"grow, aligny bottom, wmin 100\");\np2.add(demoPane, \"grow, aligny bottom\");\np2.add(createTextField(\"A JTextField\", 100), \"grow, aligny bottom, wmin 100\");\np2.add(createTextArea(\"A JTextArea\", 1, 100), \"newline,grow, aligny bottom, wmin 100\");\np2.add(createTextArea(\"A JTextArea\", 1, 100), \"grow, aligny bottom, wmin 100\");\np2.add(createTextArea(\"A JTextArea\", 1, 100), \"grow, aligny bottom, wmin 100\");\n\ntabbedPane.addTab(\"Visual Bounds (Not Corrected)\", p1);\ntabbedPane.addTab(\"Visual Bounds (Corrected on XP)\", p2);");
        return jTabbedPane;
    }

    public JComponent createDocking() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        JPanel jPanel = this.createTabPanel(new MigLayout("fill"));
        jPanel.add((Component)this.createPanel("1. North"), "north");
        jPanel.add((Component)this.createPanel("2. West"), "west");
        jPanel.add((Component)this.createPanel("3. East"), "east");
        jPanel.add((Component)this.createPanel("4. South"), "south");
        Object[][] objectArray = new String[20][6];
        for (int i = 0; i < objectArray.length; ++i) {
            objectArray[i] = new String[6];
            for (int j = 0; j < objectArray[i].length; ++j) {
                objectArray[i][j] = "Cell " + (i + 1) + ", " + (j + 1);
            }
        }
        JTable jTable = new JTable(objectArray, new String[]{"Column 1", "Column 2", "Column 3", "Column 4", "Column 5", "Column 6"});
        jPanel.add((Component)new JScrollPane(jTable), "grow");
        JPanel jPanel2 = this.createTabPanel(new MigLayout("fill", "[c]", ""));
        jPanel2.add((Component)this.createPanel("1. North"), "north");
        jPanel2.add((Component)this.createPanel("2. North"), "north");
        jPanel2.add((Component)this.createPanel("3. West"), "west");
        jPanel2.add((Component)this.createPanel("4. West"), "west");
        jPanel2.add((Component)this.createPanel("5. South"), "south");
        jPanel2.add((Component)this.createPanel("6. East"), "east");
        jPanel2.add(this.createButton("7. Normal"));
        jPanel2.add(this.createButton("8. Normal"));
        jPanel2.add(this.createButton("9. Normal"));
        JPanel jPanel3 = this.createTabPanel(new MigLayout());
        jPanel3.add((Component)this.createPanel("1. North"), "north");
        jPanel3.add((Component)this.createPanel("2. South"), "south");
        jPanel3.add((Component)this.createPanel("3. West"), "west");
        jPanel3.add((Component)this.createPanel("4. East"), "east");
        jPanel3.add(this.createButton("5. Normal"));
        JPanel jPanel4 = this.createTabPanel(new MigLayout());
        jPanel4.add((Component)this.createPanel("1. North"), "north");
        jPanel4.add((Component)this.createPanel("2. North"), "north");
        jPanel4.add((Component)this.createPanel("3. West"), "west");
        jPanel4.add((Component)this.createPanel("4. West"), "west");
        jPanel4.add((Component)this.createPanel("5. South"), "south");
        jPanel4.add((Component)this.createPanel("6. East"), "east");
        jPanel4.add(this.createButton("7. Normal"));
        jPanel4.add(this.createButton("8. Normal"));
        jPanel4.add(this.createButton("9. Normal"));
        JPanel jPanel5 = this.createTabPanel(new MigLayout("fillx", "[c]", ""));
        jPanel5.add((Component)this.createPanel("1. North"), "north");
        jPanel5.add((Component)this.createPanel("2. North"), "north");
        jPanel5.add((Component)this.createPanel("3. West"), "west");
        jPanel5.add((Component)this.createPanel("4. West"), "west");
        jPanel5.add((Component)this.createPanel("5. South"), "south");
        jPanel5.add((Component)this.createPanel("6. East"), "east");
        jPanel5.add(this.createButton("7. Normal"));
        jPanel5.add(this.createButton("8. Normal"));
        jPanel5.add(this.createButton("9. Normal"));
        JPanel jPanel6 = this.createTabPanel(new MigLayout("fill", "", ""));
        Random random = new Random();
        String[] stringArray = new String[]{"north", "east", "south", "west"};
        for (int i = 0; i < 20; ++i) {
            int n = random.nextInt(4);
            jPanel6.add((Component)this.createPanel(i + 1 + " " + stringArray[n]), stringArray[n]);
        }
        jPanel6.add((Component)this.createPanel("I'm in the Center!"), "dock center");
        jTabbedPane.addTab("Docking 1 (fill)", jPanel);
        jTabbedPane.addTab("Docking 2 (fill)", jPanel2);
        jTabbedPane.addTab("Docking 3", jPanel3);
        jTabbedPane.addTab("Docking 4", jPanel4);
        jTabbedPane.addTab("Docking 5 (fillx)", jPanel5);
        jTabbedPane.addTab("Random Docking", new JScrollPane(jPanel6));
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\nJPanel p1 = createTabPanel(new MigLayout(\"fill\"));\n\np1.add(createPanel(\"1. North\"), \"north\");\np1.add(createPanel(\"2. West\"), \"west\");\np1.add(createPanel(\"3. East\"), \"east\");\np1.add(createPanel(\"4. South\"), \"south\");\n\nString[][] data = new String[20][6];\nfor (int r = 0; r < data.length; r++) {\n\tdata[r] = new String[6];\n\tfor (int c = 0; c < data[r].length; c++)\n\t\tdata[r][c] = \"Cell \" + (r + 1) + \", \" + (c + 1);\n}\nJTable table = new JTable(data, new String[] {\"Column 1\", \"Column 2\", \"Column 3\", \"Column 4\", \"Column 5\", \"Column 6\"});\np1.add(new JScrollPane(table), \"grow\");\n\nJPanel p2 = createTabPanel(new MigLayout(\"fill\", \"[c]\", \"\"));\n\np2.add(createPanel(\"1. North\"), \"north\");\np2.add(createPanel(\"2. North\"), \"north\");\np2.add(createPanel(\"3. West\"), \"west\");\np2.add(createPanel(\"4. West\"), \"west\");\np2.add(createPanel(\"5. South\"), \"south\");\np2.add(createPanel(\"6. East\"), \"east\");\np2.add(createButton(\"7. Normal\"));\np2.add(createButton(\"8. Normal\"));\np2.add(createButton(\"9. Normal\"));\n\nJPanel p3 = createTabPanel(new MigLayout());\n\np3.add(createPanel(\"1. North\"), \"north\");\np3.add(createPanel(\"2. South\"), \"south\");\np3.add(createPanel(\"3. West\"), \"west\");\np3.add(createPanel(\"4. East\"), \"east\");\np3.add(createButton(\"5. Normal\"));\n\nJPanel p4 = createTabPanel(new MigLayout());\n\np4.add(createPanel(\"1. North\"), \"north\");\np4.add(createPanel(\"2. North\"), \"north\");\np4.add(createPanel(\"3. West\"), \"west\");\np4.add(createPanel(\"4. West\"), \"west\");\np4.add(createPanel(\"5. South\"), \"south\");\np4.add(createPanel(\"6. East\"), \"east\");\np4.add(createButton(\"7. Normal\"));\np4.add(createButton(\"8. Normal\"));\np4.add(createButton(\"9. Normal\"));\n\nJPanel p5 = createTabPanel(new MigLayout(\"fillx\", \"[c]\", \"\"));\n\np5.add(createPanel(\"1. North\"), \"north\");\np5.add(createPanel(\"2. North\"), \"north\");\np5.add(createPanel(\"3. West\"), \"west\");\np5.add(createPanel(\"4. West\"), \"west\");\np5.add(createPanel(\"5. South\"), \"south\");\np5.add(createPanel(\"6. East\"), \"east\");\np5.add(createButton(\"7. Normal\"));\np5.add(createButton(\"8. Normal\"));\np5.add(createButton(\"9. Normal\"));\n\nJPanel p6 = createTabPanel(new MigLayout(\"fill\", \"\", \"\"));\n\nRandom rand = new Random();\nString[] sides = {\"north\", \"east\", \"south\", \"west\"};\nfor (int i = 0; i < 20; i++) {\n\tint side = rand.nextInt(4);\n\tp6.add(createPanel((i + 1) + \" \" + sides[side]), sides[side]);\n}\np6.add(createButton(\"I'm in the middle!\"), \"grow\");\n\ntabbedPane.addTab(\"Docking 1 (fill)\", p1);\ntabbedPane.addTab(\"Docking 2 (fill)\", p2);\ntabbedPane.addTab(\"Docking 3\", p3);\ntabbedPane.addTab(\"Docking 4\", p4);\ntabbedPane.addTab(\"Docking 5 (fillx)\", p5);\ntabbedPane.addTab(\"Docking Spiral\", new JScrollPane(p6));");
        return jTabbedPane;
    }

    public JComponent createAbsolute_Position() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        final JPanel jPanel = this.createTabPanel(new MigLayout());
        jPanel.add((Component)this.createButton(), "pos 0.5al 0al");
        jPanel.add((Component)this.createButton(), "pos 1al 0al");
        jPanel.add((Component)this.createButton(), "pos 0.5al 0.5al");
        jPanel.add((Component)this.createButton(), "pos 5in 45lp");
        jPanel.add((Component)this.createButton(), "pos 0.5al 0.5al");
        jPanel.add((Component)this.createButton(), "pos 0.5al 1.0al");
        jPanel.add((Component)this.createButton(), "pos 1al .25al");
        jPanel.add((Component)this.createButton(), "pos visual.x2-pref visual.y2-pref");
        jPanel.add((Component)this.createButton(), "pos 1al -1in");
        jPanel.add((Component)this.createButton(), "pos 100 100");
        jPanel.add((Component)this.createButton(), "pos (10+(20*3lp)) 200");
        jPanel.add((Component)this.createButton("Drag Window! (pos 500-container.xpos 500-container.ypos)"), "pos 500-container.xpos 500-container.ypos");
        JPanel jPanel2 = this.createTabPanel(new MigLayout());
        String string = "pos (visual.x+visual.w*0.1) visual.y2-40 (visual.x2-visual.w*0.1) visual.y2";
        JLabel jLabel = this.createLabel(string, 0);
        jLabel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        jLabel.setBackground(new Color(200, 200, 255, benchRuns == 0 ? 70 : 255));
        jLabel.setOpaque(true);
        jLabel.setFont(jLabel.getFont().deriveFont(1));
        jPanel2.add((Component)jLabel, string);
        jPanel2.add((Component)this.createButton(), "pos 0 0 container.x2 n");
        jPanel2.add((Component)this.createButton(), "pos visual.x 40 visual.x2 70");
        jPanel2.add((Component)this.createButton(), "pos visual.x 100 visual.x2 p");
        jPanel2.add((Component)this.createButton(), "pos 0.1al 0.4al n (visual.y2 - 10)");
        jPanel2.add((Component)this.createButton(), "pos 0.9al 0.4al n visual.y2-10");
        jPanel2.add((Component)this.createButton(), "pos 0.5al 0.5al, pad 3 0 -3 0");
        jPanel2.add((Component)this.createButton(), "pos n n 50% 50%");
        jPanel2.add((Component)this.createButton(), "pos 50% 50% n n");
        jPanel2.add((Component)this.createButton(), "pos 50% n n 50%");
        jPanel2.add((Component)this.createButton(), "pos n 50% 50% n");
        jTabbedPane.addTab("X Y Positions", jPanel);
        jTabbedPane.addTab("X1 Y1 X2 Y2 Bounds", jPanel2);
        if (benchRuns == 0) {
            final JPanel jPanel3 = this.createTabPanel(new MigLayout("align c c, ins 0"));
            final JButton jButton = this.createButton("Press me!!");
            jPanel3.add(jButton);
            jButton.addActionListener(new ActionListener(){

                public void actionPerformed(ActionEvent actionEvent) {
                    jButton.setEnabled(false);
                    final JPanel jPanel = new JPanel(new MigLayout("align c c,fill")){

                        public void paint(Graphics graphics) {
                            graphics.setColor(this.getBackground());
                            graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
                            super.paint(graphics);
                        }
                    };
                    jPanel.setOpaque(false);
                    SwingDemo.this.configureActiveComponet(jPanel);
                    final JLabel jLabel = SwingDemo.this.createLabel("You don't need a GlassPane to be cool!");
                    jLabel.setFont(jLabel.getFont().deriveFont(30.0f));
                    jLabel.setForeground(new Color(255, 255, 255, 0));
                    jPanel.add((Component)jLabel, "align 50% 30%");
                    jPanel3.add(jPanel, "pos visual.x visual.y visual.x2 visual.y2", 0);
                    final long l = System.nanoTime();
                    final long l2 = l + 500000000L;
                    jPanel3.revalidate();
                    final Timer timer = new Timer(25, null);
                    timer.addActionListener(new ActionListener(){

                        public void actionPerformed(ActionEvent actionEvent) {
                            long l3 = System.nanoTime();
                            int n = (int)((double)(l3 - l) / (double)(l2 - l) * 300.0);
                            if (n < 150) {
                                jPanel.setBackground(new Color(100, 100, 100, n));
                            }
                            if (n > 150 && n < 405) {
                                jLabel.setForeground(new Color(255, 255, 255, n - 150));
                                jPanel.repaint();
                            }
                            if (n > 405) {
                                timer.stop();
                            }
                        }
                    });
                    timer.start();
                }
            });
            jTabbedPane.addTab("GlassPane Substitute", jPanel3);
            this.addComponentListener(new ComponentAdapter(){

                public void componentMoved(ComponentEvent componentEvent) {
                    if (jPanel.isDisplayable()) {
                        jPanel.revalidate();
                    } else {
                        SwingDemo.this.removeComponentListener(this);
                    }
                }
            });
        }
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\n// Pos tab\nfinal JPanel posPanel = createTabPanel(new MigLayout());\n\nposPanel.add(createButton(), \"pos 0.5al 0al\");\nposPanel.add(createButton(), \"pos 1al 0al\");\nposPanel.add(createButton(), \"pos 0.5al 0.5al\");\nposPanel.add(createButton(), \"pos 5in 45lp\");\nposPanel.add(createButton(), \"pos 0.5al 0.5al\");\nposPanel.add(createButton(), \"pos 0.5al 1.0al\");\nposPanel.add(createButton(), \"pos 1al .25al\");\nposPanel.add(createButton(), \"pos visual.x2-pref visual.y2-pref\");\nposPanel.add(createButton(), \"pos 1al -1in\");\nposPanel.add(createButton(), \"pos 100 100\");\nposPanel.add(createButton(), \"pos (10+(20*3lp)) 200\");\nposPanel.add(createButton(\"Drag Window! (pos 500-container.xpos 500-container.ypos)\"),\n                            \"pos 500-container.xpos 500-container.ypos\");\n\n// Bounds tab\nJPanel boundsPanel = createTabPanel(new MigLayout());\n\nString constr = \"pos (visual.x+visual.w*0.1) visual.y2-40 (visual.x2-visual.w*0.1) visual.y2\";\nJLabel southLabel = createLabel(constr, SwingConstants.CENTER);\nsouthLabel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));\nsouthLabel.setBackground(new Color(200, 200, 255, 70));\nsouthLabel.setOpaque(true);\nsouthLabel.setFont(southLabel.getFont().deriveFont(Font.BOLD));\nboundsPanel.add(southLabel, constr);\n\nboundsPanel.add(createButton(), \"pos 0 0 container.x2 n\");\nboundsPanel.add(createButton(), \"pos visual.x 40 visual.x2 70\");\nboundsPanel.add(createButton(), \"pos visual.x 100 visual.x2 p\");\nboundsPanel.add(createButton(), \"pos 0.1al 0.4al n visual.y2-10\");\nboundsPanel.add(createButton(), \"pos 0.9al 0.4al n visual.y2-10\");\nboundsPanel.add(createButton(), \"pos 0.5al 0.5al, pad 3 0 -3 0\");\nboundsPanel.add(createButton(), \"pos n n 50% 50%\");\nboundsPanel.add(createButton(), \"pos 50% 50% n n\");\nboundsPanel.add(createButton(), \"pos 50% n n 50%\");\nboundsPanel.add(createButton(), \"pos n 50% 50% n\");\n\n// Glass pane tab\nfinal JPanel glassPanel = createTabPanel(new MigLayout(\"align c c, ins 0\"));\nfinal JButton butt = createButton(\"Press me!!\");\nglassPanel.add(butt);\n\nbutt.addActionListener(new ActionListener()\t\t{\n\tpublic void actionPerformed(ActionEvent e)\n\t{\n\t\tbutt.setEnabled(false);\n\t\tfinal JPanel bg = new JPanel(new MigLayout(\"align c c,fill\")) {\n\t\t\tpublic void paint(Graphics g)\n\t\t\t{\n\t\t\t\tg.setColor(getBackground());\n\t\t\t\tg.fillRect(0, 0, getWidth(), getHeight());\n\t\t\t\tsuper.paint(g);\n\t\t\t}\n\t\t};\n\t\tbg.setOpaque(OPAQUE);\n\t\tconfigureActiveComponet(bg);\n\n\t\tfinal JLabel label = createLabel(\"You don't need a GlassPane to be cool!\");\n\t\tlabel.setFont(label.getFont().deriveFont(30f));\n\t\tlabel.setForeground(new Color(255, 255, 255, 0));\n\t\tbg.add(label, \"align 50% 30%\");\n\n\t\tglassPanel.add(bg, \"pos visual.x visual.y visual.x2 visual.y2\", 0);\n\t\tfinal long startTime = System.nanoTime();\n\t\tfinal long endTime = startTime + 500000000L;\n\n\t\tglassPanel.revalidate();\n\n\t\tfinal javax.swing.Timer timer = new Timer(25, null);\n\n\t\ttimer.addActionListener(new ActionListener() {\n\t\t\tpublic void actionPerformed(ActionEvent e)\n\t\t\t{\n\t\t\t\tlong now = System.nanoTime();\n\t\t\t\tint alpha = (int) (((now - startTime) / (double) (endTime - startTime)) * 300);\n\t\t\t\tif (alpha < 150)\n\t\t\t\t\tbg.setBackground(new Color(100, 100, 100, alpha));\n\n\t\t\t\tif (alpha > 150 && alpha < 405) {\n\t\t\t\t\tlabel.setForeground(new Color(255, 255, 255, (alpha - 150)));\n\t\t\t\t\tbg.repaint();\n\t\t\t\t}\n\t\t\t\tif (alpha > 405)\n\t\t\t\t\ttimer.stop();\n\t\t\t}\n\t\t});\n\t\ttimer.start();\n\t}\n});\n\ntabbedPane.addTab(\"X Y Positions\", posPanel);\ntabbedPane.addTab(\"X1 Y1 X2 Y2 Bounds\", boundsPanel);\ntabbedPane.addTab(\"GlassPane Substitute\", glassPanel);\n\naddComponentListener(new ComponentAdapter() {\n\tpublic void componentMoved(ComponentEvent e) {\n\t\tif (posPanel.isDisplayable()) {\n\t\t\tposPanel.revalidate();\n\t\t} else {\n\t\t\tremoveComponentListener(this);\n\t\t}\n\t}\n});");
        return jTabbedPane;
    }

    public JComponent createComponent_Links() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        JPanel jPanel = this.createTabPanel(new MigLayout());
        JButton jButton = this.createButton("Mini");
        jButton.setMargin(new Insets(0, 1, 0, 1));
        jPanel.add((Component)jButton, "pos null ta.y ta.x2 null");
        jPanel.add((Component)this.createTextArea("Components, Please Link to Me!\nMy ID is: 'ta'", 3, 30), "id ta, pos 0.5al 0.5al");
        jPanel.add((Component)this.createButton(), "id b1,pos ta.x2 ta.y2");
        jPanel.add((Component)this.createButton(), "pos b1.x2+rel b1.y visual.x2 null");
        jPanel.add((Component)this.createButton(), "pos ta.x2+rel ta.y visual.x2 null");
        jPanel.add((Component)this.createButton(), "pos null ta.y+(ta.h-pref)/2 ta.x-rel null");
        jPanel.add((Component)this.createButton(), "pos ta.x ta.y2+100 ta.x2 null");
        jPanel.add((Component)this.createCheck("pos (ta.x+indent) (ta.y2+rel)"), "pos (ta.x+indent) (ta.y2+rel)");
        JPanel jPanel2 = this.createTabPanel(new MigLayout());
        JButton jButton2 = this.createButton("Bounds Externally Set!");
        jButton2.setBounds(250, 130, 200, 40);
        jPanel2.add((Component)jButton2, "id ext, external");
        jPanel2.add((Component)this.createButton(), "pos ext.x2 ext.y2");
        jPanel2.add((Component)this.createButton(), "pos null null ext.x ext.y");
        JPanel jPanel3 = this.createTabPanel(new MigLayout());
        jPanel3.add((Component)this.createButton(), "id b1, endgroupx g1, pos 200 200");
        jPanel3.add((Component)this.createButton(), "id b2, endgroupx g1, pos (b1.x+2ind) (b1.y2+rel)");
        jPanel3.add((Component)this.createButton(), "id b3, endgroupx g1, pos (b1.x+4ind) (b2.y2+rel)");
        jPanel3.add((Component)this.createButton(), "id b4, endgroupx g1, pos (b1.x+6ind) (b3.y2+rel)");
        JPanel jPanel4 = this.createTabPanel(new MigLayout());
        jPanel4.add((Component)this.createButton(), "id grp1.b1, pos n 0.5al 50% n");
        jPanel4.add((Component)this.createButton(), "id grp1.b2, pos 50% 0.5al n n");
        jPanel4.add((Component)this.createButton(), "id grp1.b3, pos 0.5al n n b1.y");
        jPanel4.add((Component)this.createButton(), "id grp1.b4, pos 0.5al b1.y2 n n");
        jPanel4.add((Component)this.createButton(), "pos n grp1.y2 grp1.x n");
        jPanel4.add((Component)this.createButton(), "pos n n grp1.x grp1.y");
        jPanel4.add((Component)this.createButton(), "pos grp1.x2 n n grp1.y");
        jPanel4.add((Component)this.createButton(), "pos grp1.x2 grp1.y2");
        JPanel jPanel5 = new JPanel(null);
        jPanel5.setBackground(new Color(200, 200, 255));
        jPanel4.add((Component)jPanel5, "pos grp1.x grp1.y grp1.x2 grp1.y2");
        jTabbedPane.addTab("Component Links", jPanel);
        jTabbedPane.addTab("External Components", jPanel2);
        jTabbedPane.addTab("End Grouping", jPanel3);
        jTabbedPane.addTab("Group Bounds", jPanel4);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\nJPanel linksPanel = createTabPanel(new MigLayout());\n\n// Links tab\nJButton mini = createButton(\"Mini\");\nmini.setMargin(new Insets(0, 1, 0, 1));\nlinksPanel.add(mini, \"pos null ta.y ta.x2 null\");\nlinksPanel.add(createTextArea(\"Components, Please Link to Me!\\nMy ID is: 'ta'\", 3, 30), \"id ta, pos 0.5al 0.5al\");\nlinksPanel.add(createButton(), \"id b1,pos ta.x2 ta.y2\");\nlinksPanel.add(createButton(), \"pos b1.x2+rel b1.y visual.x2 null\");\nlinksPanel.add(createButton(), \"pos ta.x2+rel ta.y visual.x2 null\");\nlinksPanel.add(createButton(), \"pos null ta.y+(ta.h-pref)/2 ta.x-rel null\");\nlinksPanel.add(createButton(), \"pos ta.x ta.y2+100 ta.x2 null\");\nlinksPanel.add(createCheck(\"pos (ta.x+indent) (ta.y2+rel)\"),\n                           \"pos (ta.x+indent) (ta.y2+rel)\");\n\n// External tab\nJPanel externalPanel = createTabPanel(new MigLayout());\n\nJButton extButt = createButton(\"Bounds Externally Set!\");\nextButt.setBounds(250, 130, 200, 40);\nexternalPanel.add(extButt, \"id ext, external\");\nexternalPanel.add(createButton(), \"pos ext.x2 ext.y2\");\nexternalPanel.add(createButton(), \"pos null null ext.x ext.y\");\n\n// Start/End Group tab\nJPanel egPanel = createTabPanel(new MigLayout());\n\negPanel.add(createButton(), \"id b1, endgroupx g1, pos 200 200\");\negPanel.add(createButton(), \"id b2, endgroupx g1, pos (b1.x+2ind) (b1.y2+rel)\");\negPanel.add(createButton(), \"id b3, endgroupx g1, pos (b1.x+4ind) (b2.y2+rel)\");\negPanel.add(createButton(), \"id b4, endgroupx g1, pos (b1.x+6ind) (b3.y2+rel)\");\n\n// Group Bounds tab\nJPanel gpPanel = createTabPanel(new MigLayout());\n\ngpPanel.add(createButton(), \"id grp1.b1, pos n 0.5al 50% n\");\ngpPanel.add(createButton(), \"id grp1.b2, pos 50% 0.5al n n\");\ngpPanel.add(createButton(), \"id grp1.b3, pos 0.5al n n b1.y\");\ngpPanel.add(createButton(), \"id grp1.b4, pos 0.5al b1.y2 n n\");\n\ngpPanel.add(createButton(), \"pos n grp1.y2 grp1.x n\");\ngpPanel.add(createButton(), \"pos n n grp1.x grp1.y\");\ngpPanel.add(createButton(), \"pos grp1.x2 n n grp1.y\");\ngpPanel.add(createButton(), \"pos grp1.x2 grp1.y2\");\n\nJPanel boundsPanel = new JPanel(null);\nboundsPanel.setBackground(new Color(200, 200, 255));\ngpPanel.add(boundsPanel, \"pos grp1.x grp1.y grp1.x2 grp1.y2\");\n\n\ntabbedPane.addTab(\"Component Links\", linksPanel);\ntabbedPane.addTab(\"External Components\", externalPanel);\ntabbedPane.addTab(\"End Grouping\", egPanel);\ntabbedPane.addTab(\"Group Bounds\", gpPanel);");
        return jTabbedPane;
    }

    public JComponent createFlow_Direction() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.addTab("Layout: flowx, Cell: flowx", this.createFlowPanel("", "flowx"));
        jTabbedPane.addTab("Layout: flowx, Cell: flowy", this.createFlowPanel("", "flowy"));
        jTabbedPane.addTab("Layout: flowy, Cell: flowx", this.createFlowPanel("flowy", "flowx"));
        jTabbedPane.addTab("Layout: flowy, Cell: flowy", this.createFlowPanel("flowy", "flowy"));
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\ntabbedPane.addTab(\"Layout: flowx, Cell: flowx\", createFlowPanel(\"\", \"flowx\"));\ntabbedPane.addTab(\"Layout: flowx, Cell: flowy\", createFlowPanel(\"\", \"flowy\"));\ntabbedPane.addTab(\"Layout: flowy, Cell: flowx\", createFlowPanel(\"flowy\", \"flowx\"));\ntabbedPane.addTab(\"Layout: flowy, Cell: flowy\", createFlowPanel(\"flowy\", \"flowy\"));\n\tpublic JPanel createFlowPanel(String gridFlow, String cellFlow)\n\t{\nMigLayout lm = new MigLayout(\"center, wrap 3,\" + gridFlow,\n                             \"[110,fill]\",\n                             \"[110,fill]\");\nJPanel panel = createTabPanel(lm);\n\nfor (int i = 0; i < 9; i++) {\n\tJButton b = createButton(\"\" + (i + 1));\n\tb.setFont(b.getFont().deriveFont(20f));\n\tpanel.add(b, cellFlow);\n}\n\nJButton b = createButton(\"5:2\");\nb.setFont(b.getFont().deriveFont(20f));\npanel.add(b, cellFlow + \",cell 1 1\");\n\nreturn panel;\n\t}");
        return jTabbedPane;
    }

    public JPanel createFlowPanel(String string, String string2) {
        MigLayout migLayout = new MigLayout("center, wrap 3," + string, "[110,fill]", "[110,fill]");
        JPanel jPanel = this.createTabPanel(migLayout);
        Font font = jPanel.getFont().deriveFont(20.0f);
        for (int i = 0; i < 9; ++i) {
            JComponent jComponent = this.createPanel("" + (i + 1));
            jComponent.setFont(font);
            jPanel.add((Component)jComponent, string2);
        }
        JComponent jComponent = this.createPanel("5:2");
        jComponent.setFont(font);
        jPanel.add((Component)jComponent, string2 + ",cell 1 1");
        return jPanel;
    }

    public JComponent createDebug() {
        return this.createPlainImpl(true);
    }

    public JComponent createButton_Bars() {
        MigLayout migLayout = new MigLayout("ins 0 0 15lp 0", "[grow]", "[grow][baseline,nogrid]");
        final JPanel jPanel = new JPanel(migLayout);
        final JLabel jLabel = this.createLabel("");
        jLabel.setFont(jLabel.getFont().deriveFont(1));
        JTabbedPane jTabbedPane = new JTabbedPane();
        JToggleButton jToggleButton = this.createToggleButton("Windows");
        JToggleButton jToggleButton2 = this.createToggleButton("Mac OS X");
        JButton jButton = this.createButton("Help");
        if (benchRuns == 0) {
            jToggleButton.addActionListener(new ActionListener(){

                public void actionPerformed(ActionEvent actionEvent) {
                    PlatformDefaults.setPlatform(0);
                    jLabel.setText("'" + PlatformDefaults.getButtonOrder() + "'");
                    ((JPanel)((JFrame)Frame.getFrames()[0]).getContentPane()).revalidate();
                }
            });
            jToggleButton2.addActionListener(new ActionListener(){

                public void actionPerformed(ActionEvent actionEvent) {
                    PlatformDefaults.setPlatform(1);
                    jLabel.setText(PlatformDefaults.getButtonOrder());
                    ((JPanel)((JFrame)Frame.getFrames()[0]).getContentPane()).revalidate();
                }
            });
            jButton.addActionListener(new ActionListener(){

                public void actionPerformed(ActionEvent actionEvent) {
                    JOptionPane.showMessageDialog(jPanel, "See JavaDoc for PlatformConverter.getButtonBarOrder(..) for details on the format string.");
                }
            });
        }
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(jToggleButton);
        buttonGroup.add(jToggleButton2);
        if (benchRuns == 0) {
            if (PlatformDefaults.getCurrentPlatform() == 1) {
                jToggleButton2.doClick();
            } else {
                jToggleButton.doClick();
            }
        }
        jTabbedPane.addTab("Buttons", this.createButtonBarsPanel("help", false));
        jTabbedPane.addTab("Buttons with Help2", this.createButtonBarsPanel("help2", false));
        jTabbedPane.addTab("Buttons (Same width)", this.createButtonBarsPanel("help", true));
        jPanel.add((Component)jTabbedPane, "grow,wrap");
        jPanel.add(this.createLabel("Button Order:"));
        jPanel.add((Component)jLabel, "growx");
        jPanel.add(jToggleButton);
        jPanel.add(jToggleButton2);
        jPanel.add((Component)jButton, "gapbefore unrel");
        this.setSource("MigLayout lm = new MigLayout(\"ins 0 0 15lp 0\",\n                                  \"[grow]\",\n                                  \"[grow][baseline,nogrid,gap unrelated]\");\n\nfinal JPanel mainPanel = new JPanel(lm);\nfinal JLabel formatLabel = createLabel(\"\");\nformatLabel.setFont(formatLabel.getFont().deriveFont(Font.BOLD));\nJTabbedPane tabbedPane = new JTabbedPane();\n\nJToggleButton winButt = createToggleButton(\"Windows\");\n\nwinButt.addActionListener(new ActionListener() {\n\tpublic void actionPerformed(ActionEvent e) {\n\t\tPlatformDefaults.setPlatform(PlatformDefaults.WINDOWS_XP);\n\t\tformatLabel.setText(\"'\" + PlatformDefaults.getButtonOrder() + \"'\");\n\t\tSwingUtilities.updateComponentTreeUI(mainPanel);\n\t}\n});\n\nJToggleButton macButt = createToggleButton(\"Mac OS X\");\nmacButt.addActionListener(new ActionListener() {\n\tpublic void actionPerformed(ActionEvent e) {\n\t\tPlatformDefaults.setPlatform(PlatformDefaults.MAC_OSX);\n\t\tformatLabel.setText(PlatformDefaults.getButtonOrder());\n\t\tSwingUtilities.updateComponentTreeUI(mainPanel);\n\t}\n});\n\nJButton helpButt = createButton(\"Help\");\nhelpButt.addActionListener(new ActionListener() {\n\tpublic void actionPerformed(ActionEvent e) {\n\t\tJOptionPane.showMessageDialog(mainPanel, \"See JavaDoc for PlatformConverter.getButtonBarOrder(..) for details on the format string.\");\n\t}\n});\n\nButtonGroup bg = new ButtonGroup();\nbg.add(winButt);\nbg.add(macButt);\nwinButt.doClick();\n\ntabbedPane.addTab(\"Buttons\", createButtonBarsPanel(\"help\", false));\ntabbedPane.addTab(\"Buttons with Help2\", createButtonBarsPanel(\"help2\", false));\ntabbedPane.addTab(\"Buttons (Same width)\", createButtonBarsPanel(\"help\", true));\n\nmainPanel.add(tabbedPane, \"grow,wrap\");\n\nmainPanel.add(createLabel(\"Button Order:\"));\nmainPanel.add(formatLabel, \"growx\");\nmainPanel.add(winButt);\nmainPanel.add(macButt);\nmainPanel.add(helpButt, \"gapbefore unrel\");");
        return jPanel;
    }

    private JComponent createButtonBarsPanel(String string, boolean bl) {
        MigLayout migLayout = new MigLayout("nogrid, fillx, aligny 100%, gapy unrel");
        JPanel jPanel = this.createTabPanel(migLayout);
        String[][] stringArrayArray = new String[][]{{"OK"}, {"No", "Yes"}, {"Help", "Close"}, {"OK", "Help"}, {"OK", "Cancel", "Help"}, {"OK", "Cancel", "Apply", "Help"}, {"No", "Yes", "Cancel"}, {"Help", "< Back", "Forward >", "Cancel"}, {"Print...", "Cancel", "Help"}};
        for (int i = 0; i < stringArrayArray.length; ++i) {
            for (int j = 0; j < stringArrayArray[i].length; ++j) {
                String string2;
                String string3 = string2 = stringArrayArray[i][j];
                if (string2.equals("Help")) {
                    string3 = string;
                } else if (string2.equals("< Back")) {
                    string3 = "back";
                } else if (string2.equals("Close")) {
                    string3 = "cancel";
                } else if (string2.equals("Forward >")) {
                    string3 = "next";
                } else if (string2.equals("Print...")) {
                    string3 = "other";
                }
                String string4 = j == stringArrayArray[i].length - 1 ? ",wrap" : "";
                String string5 = bl ? "sgx " + i + "," : "";
                jPanel.add((Component)this.createButton(string2), string5 + "tag " + string3 + string4);
            }
        }
        return jPanel;
    }

    public JComponent createOrientation() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        MigLayout migLayout = new MigLayout("flowy", "[grow,fill]", "[]20[]20[]20[]");
        JPanel jPanel = this.createTabPanel(migLayout);
        MigLayout migLayout2 = new MigLayout("", "[trailing][grow,fill]", "");
        JPanel jPanel2 = this.createTabPanel(migLayout2);
        this.addSeparator(jPanel2, "Default Orientation");
        jPanel2.add(this.createLabel("Level of Trust"));
        jPanel2.add((Component)this.createTextField(""), "span,growx");
        jPanel2.add(this.createLabel("Radar Presentation"));
        jPanel2.add(this.createTextField(""));
        jPanel2.add(this.createTextField(""));
        MigLayout migLayout3 = new MigLayout("rtl,ttb", "[trailing][grow,fill]", "");
        JPanel jPanel3 = this.createTabPanel(migLayout3);
        this.addSeparator(jPanel3, "Right to Left");
        jPanel3.add(this.createLabel("Level of Trust"));
        jPanel3.add((Component)this.createTextField(""), "span,growx");
        jPanel3.add(this.createLabel("Radar Presentation"));
        jPanel3.add(this.createTextField(""));
        jPanel3.add(this.createTextField(""));
        MigLayout migLayout4 = new MigLayout("rtl,btt", "[trailing][grow,fill]", "");
        JPanel jPanel4 = this.createTabPanel(migLayout4);
        this.addSeparator(jPanel4, "Right to Left, Bottom to Top");
        jPanel4.add(this.createLabel("Level of Trust"));
        jPanel4.add((Component)this.createTextField(""), "span,growx");
        jPanel4.add(this.createLabel("Radar Presentation"));
        jPanel4.add(this.createTextField(""));
        jPanel4.add(this.createTextField(""));
        MigLayout migLayout5 = new MigLayout("ltr,btt", "[trailing][grow,fill]", "");
        JPanel jPanel5 = this.createTabPanel(migLayout5);
        this.addSeparator(jPanel5, "Left to Right, Bottom to Top");
        jPanel5.add(this.createLabel("Level of Trust"));
        jPanel5.add((Component)this.createTextField(""), "span,growx");
        jPanel5.add(this.createLabel("Radar Presentation"));
        jPanel5.add(this.createTextField(""));
        jPanel5.add(this.createTextField(""));
        jPanel.add(jPanel2);
        jPanel.add(jPanel3);
        jPanel.add(jPanel4);
        jPanel.add(jPanel5);
        jTabbedPane.addTab("Orientation", jPanel);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\nMigLayout lm = new MigLayout(\"flowy\", \"[grow,fill]\", \"[]0[]15lp[]0[]\");\nJPanel mainPanel = createTabPanel(lm);\n\n// Default orientation\nMigLayout defLM = new MigLayout(\"\", \"[][grow,fill]\", \"\");\n\nJPanel defPanel = createTabPanel(defLM);\naddSeparator(defPanel, \"Default Orientation\");\ndefPanel.add(createLabel(\"Level\"));\ndefPanel.add(createTextField(\"\"), \"span,growx\");\ndefPanel.add(createLabel(\"Radar\"));\ndefPanel.add(createTextField(\"\"));\ndefPanel.add(createTextField(\"\"));\n\n// Right-to-left, Top-to-bottom\nMigLayout rtlLM = new MigLayout(\"rtl,ttb\",\n                                \"[][grow,fill]\",\n                                \"\");\nJPanel rtlPanel = createTabPanel(rtlLM);\naddSeparator(rtlPanel, \"Right to Left\");\nrtlPanel.add(createLabel(\"Level\"));\nrtlPanel.add(createTextField(\"\"), \"span,growx\");\nrtlPanel.add(createLabel(\"Radar\"));\nrtlPanel.add(createTextField(\"\"));\nrtlPanel.add(createTextField(\"\"));\n\n// Right-to-left, Bottom-to-top\nMigLayout rtlbLM = new MigLayout(\"rtl,btt\",\n                                      \"[][grow,fill]\",\n                                      \"\");\nJPanel rtlbPanel = createTabPanel(rtlbLM);\naddSeparator(rtlbPanel, \"Right to Left, Bottom to Top\");\nrtlbPanel.add(createLabel(\"Level\"));\nrtlbPanel.add(createTextField(\"\"), \"span,growx\");\nrtlbPanel.add(createLabel(\"Radar\"));\nrtlbPanel.add(createTextField(\"\"));\nrtlbPanel.add(createTextField(\"\"));\n\n// Left-to-right, Bottom-to-top\nMigLayout ltrbLM = new MigLayout(\"ltr,btt\",\n                                      \"[][grow,fill]\",\n                                      \"\");\nJPanel ltrbPanel = createTabPanel(ltrbLM);\naddSeparator(ltrbPanel, \"Left to Right, Bottom to Top\");\nltrbPanel.add(createLabel(\"Level\"));\nltrbPanel.add(createTextField(\"\"), \"span,growx\");\nltrbPanel.add(createLabel(\"Radar\"));\nltrbPanel.add(createTextField(\"\"));\nltrbPanel.add(createTextField(\"\"));\n\nmainPanel.add(defPanel);\nmainPanel.add(rtlPanel);\nmainPanel.add(rtlbPanel);\nmainPanel.add(ltrbPanel);\n\ntabbedPane.addTab(\"Orientation\", mainPanel);");
        return jTabbedPane;
    }

    public JComponent createCell_Position() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        MigLayout migLayout = new MigLayout("", "[100:pref,fill]", "[100:pref,fill]");
        JPanel jPanel = this.createTabPanel(migLayout);
        jPanel.add((Component)this.createButton(), "cell 0 0");
        jPanel.add((Component)this.createButton(), "cell 2 0");
        jPanel.add((Component)this.createButton(), "cell 3 0");
        jPanel.add((Component)this.createButton(), "cell 1 1");
        jPanel.add((Component)this.createButton(), "cell 0 2");
        jPanel.add((Component)this.createButton(), "cell 2 2");
        jPanel.add((Component)this.createButton(), "cell 2 2");
        MigLayout migLayout2 = new MigLayout("wrap", "[100:pref,fill][100:pref,fill][100:pref,fill][100:pref,fill]", "[100:pref,fill]");
        JPanel jPanel2 = this.createTabPanel(migLayout2);
        jPanel2.add(this.createButton());
        jPanel2.add((Component)this.createButton(), "skip");
        jPanel2.add(this.createButton());
        jPanel2.add((Component)this.createButton(), "skip,wrap");
        jPanel2.add(this.createButton());
        jPanel2.add((Component)this.createButton(), "skip,split");
        jPanel2.add(this.createButton());
        MigLayout migLayout3 = new MigLayout("", "[100:pref,fill]", "[100:pref,fill]");
        JPanel jPanel3 = this.createTabPanel(migLayout3);
        jPanel3.add(this.createButton());
        jPanel3.add((Component)this.createButton(), "skip");
        jPanel3.add((Component)this.createButton(), "wrap");
        jPanel3.add((Component)this.createButton(), "skip,wrap");
        jPanel3.add(this.createButton());
        jPanel3.add((Component)this.createButton(), "skip,split");
        jPanel3.add(this.createButton());
        MigLayout migLayout4 = new MigLayout("", "[100:pref,fill]", "[100:pref,fill]");
        JPanel jPanel4 = this.createTabPanel(migLayout4);
        jPanel4.add(this.createButton());
        jPanel4.add((Component)this.createButton(), "cell 2 0");
        jPanel4.add(this.createButton());
        jPanel4.add((Component)this.createButton(), "cell 1 1,wrap");
        jPanel4.add(this.createButton());
        jPanel4.add((Component)this.createButton(), "cell 2 2,split");
        jPanel4.add(this.createButton());
        jTabbedPane.addTab("Absolute", jPanel);
        jTabbedPane.addTab("Relative + Wrap", jPanel2);
        jTabbedPane.addTab("Relative", jPanel3);
        jTabbedPane.addTab("Mixed", jPanel4);
        this.setSource("\t\tJTabbedPane tabbedPane = new JTabbedPane();\n\n\t\t// Absolute grid position\n\t\tMigLayout absLM = new MigLayout(\"\",\n\t\t                                \"[100:pref,fill]\",\n\t\t                                \"[100:pref,fill]\");\n\t\tJPanel absPanel = createTabPanel(absLM);\n\t\tabsPanel.add(createPanel(), \"cell 0 0\");\n\t\tabsPanel.add(createPanel(), \"cell 2 0\");\n\t\tabsPanel.add(createPanel(), \"cell 3 0\");\n\t\tabsPanel.add(createPanel(), \"cell 1 1\");\n\t\tabsPanel.add(createPanel(), \"cell 0 2\");\n\t\tabsPanel.add(createPanel(), \"cell 2 2\");\n\t\tabsPanel.add(createPanel(), \"cell 2 2\");\n\n\n\t\t// Relative grid position with wrap\n\t\tMigLayout relAwLM = new MigLayout(\"wrap\",\n\t\t                                       \"[100:pref,fill][100:pref,fill][100:pref,fill][100:pref,fill]\",\n\t\t                                       \"[100:pref,fill]\");\n\t\tJPanel relAwPanel = createTabPanel(relAwLM);\n\t\trelAwPanel.add(createPanel());\n\t\trelAwPanel.add(createPanel(), \"skip\");\n\t\trelAwPanel.add(createPanel());\n\t\trelAwPanel.add(createPanel(), \"skip,wrap\");\n\t\trelAwPanel.add(createPanel());\n\t\trelAwPanel.add(createPanel(), \"skip,split\");\n\t\trelAwPanel.add(createPanel());\n\n\n\t\t// Relative grid position with manual wrap\n\t\tMigLayout relWLM = new MigLayout(\"\",\n\t\t                                      \"[100:pref,fill]\",\n\t\t                                      \"[100:pref,fill]\");\n\t\tJPanel relWPanel = createTabPanel(relWLM);\n\t\trelWPanel.add(createPanel());\n\t\trelWPanel.add(createPanel(), \"skip\");\n\t\trelWPanel.add(createPanel(), \"wrap\");\n\t\trelWPanel.add(createPanel(), \"skip,wrap\");\n\t\trelWPanel.add(createPanel());\n\t\trelWPanel.add(createPanel(), \"skip,split\");\n\n\t\trelWPanel.add(createPanel());\n\n\n\t\t// Mixed relative and absolute grid position\n\t\tMigLayout mixLM = new MigLayout(\"\",\n\t\t                                \"[100:pref,fill]\",\n\t\t                                \"[100:pref,fill]\");\n\t\tJPanel mixPanel = createTabPanel(mixLM);\n\t\tmixPanel.add(createPanel());\n\t\tmixPanel.add(createPanel(), \"cell 2 0\");\n\t\tmixPanel.add(createPanel());\n\t\tmixPanel.add(createPanel(), \"cell 1 1,wrap\");\n\t\tmixPanel.add(createPanel());\n\t\tmixPanel.add(createPanel(), \"cell 2 2,split\");\n\t\tmixPanel.add(createPanel());\n\n\t\ttabbedPane.addTab(\"Absolute\", absPanel);\n\t\ttabbedPane.addTab(\"Relative + Wrap\", relAwPanel);\n\t\ttabbedPane.addTab(\"Relative\", relWPanel);\n\t\ttabbedPane.addTab(\"Mixed\", mixPanel);");
        return jTabbedPane;
    }

    public JComponent createPlain() {
        return this.createPlainImpl(false);
    }

    private JComponent createPlainImpl(boolean bl) {
        JTabbedPane jTabbedPane = new JTabbedPane();
        MigLayout migLayout = new MigLayout(bl && benchRuns == 0 ? "debug, inset 20" : "ins 20", "[para]0[][100lp, fill][60lp][95lp, fill]", "");
        JPanel jPanel = this.createTabPanel(migLayout);
        this.addSeparator(jPanel, "Manufacturer");
        jPanel.add((Component)this.createLabel("Company"), "skip");
        jPanel.add((Component)this.createTextField(""), "span, growx");
        jPanel.add((Component)this.createLabel("Contact"), "skip");
        jPanel.add((Component)this.createTextField(""), "span, growx");
        jPanel.add((Component)this.createLabel("Order No"), "skip");
        jPanel.add((Component)this.createTextField(15), "wrap para");
        this.addSeparator(jPanel, "Inspector");
        jPanel.add((Component)this.createLabel("Name"), "skip");
        jPanel.add((Component)this.createTextField(""), "span, growx");
        jPanel.add((Component)this.createLabel("Reference No"), "skip");
        jPanel.add((Component)this.createTextField(""), "wrap");
        jPanel.add((Component)this.createLabel("Status"), "skip");
        jPanel.add((Component)this.createCombo(new String[]{"In Progress", "Finnished", "Released"}), "wrap para");
        this.addSeparator(jPanel, "Ship");
        jPanel.add((Component)this.createLabel("Shipyard"), "skip");
        jPanel.add((Component)this.createTextField(""), "span, growx");
        jPanel.add((Component)this.createLabel("Register No"), "skip");
        jPanel.add(this.createTextField(""));
        jPanel.add((Component)this.createLabel("Hull No"), "right");
        jPanel.add((Component)this.createTextField(15), "wrap");
        jPanel.add((Component)this.createLabel("Project StructureType"), "skip");
        jPanel.add(this.createCombo(new String[]{"New Building", "Convention", "Repair"}));
        if (bl) {
            jPanel.add((Component)this.createLabel("Red is cell bounds. Blue is component bounds."), "newline,ax left,span,gaptop 40,");
        }
        jTabbedPane.addTab("Plain", jPanel);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\nMigLayout lm = new MigLayout((debug && benchRuns == 0 ? \"debug, inset 20\" : \"ins 20\"), \"[para]0[][100lp, fill][60lp][95lp, fill]\", \"\");\nJPanel panel = createTabPanel(lm);\n\naddSeparator(panel, \"Manufacturer\");\n\npanel.add(createLabel(\"Company\"),   \"skip\");\npanel.add(createTextField(\"\"),      \"span, growx\");\npanel.add(createLabel(\"Contact\"),   \"skip\");\npanel.add(createTextField(\"\"),      \"span, growx\");\npanel.add(createLabel(\"Order No\"),  \"skip\");\npanel.add(createTextField(15),      \"wrap para\");\n\naddSeparator(panel, \"Inspector\");\n\npanel.add(createLabel(\"Name\"),         \"skip\");\npanel.add(createTextField(\"\"),         \"span, growx\");\npanel.add(createLabel(\"Reference No\"), \"skip\");\npanel.add(createTextField(\"\"),         \"wrap\");\npanel.add(createLabel(\"Status\"),       \"skip\");\npanel.add(createCombo(new String[] {\"In Progress\", \"Finnished\", \"Released\"}), \"wrap para\");\n\naddSeparator(panel, \"Ship\");\n\npanel.add(createLabel(\"Shipyard\"),     \"skip\");\npanel.add(createTextField(\"\"),         \"span, growx\");\npanel.add(createLabel(\"Register No\"),  \"skip\");\npanel.add(createTextField(\"\"));\npanel.add(createLabel(\"Hull No\"),      \"right\");\npanel.add(createTextField(15), \"wrap\");\npanel.add(createLabel(\"Project StructureType\"), \"skip\");\npanel.add(createCombo(new String[] {\"New Building\", \"Convention\", \"Repair\"}));\n\nif (debug)\n\tpanel.add(createLabel(\"Red is cell bounds. Blue is component bounds.\"), \"newline,ax left,span,gaptop 40,\");\n\ntabbedPane.addTab(\"Plain\", panel);");
        return jTabbedPane;
    }

    public JComponent createBound_Sizes() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        for (int i = 0; i < 2; ++i) {
            String string = i == 0 ? "[right][300]" : "[right, 100lp:pref][300]";
            MigLayout migLayout = new MigLayout("wrap", string, "");
            JPanel jPanel = this.createTabPanel(migLayout);
            jPanel.add(this.createLabel("File Number:"));
            jPanel.add((Component)this.createTextField(""), "growx");
            jPanel.add(this.createLabel("RFQ Number:"));
            jPanel.add((Component)this.createTextField(""), "growx");
            jPanel.add(this.createLabel("Entry Date:"));
            jPanel.add(this.createTextField(6));
            jPanel.add(this.createLabel("Sales Person:"));
            jPanel.add((Component)this.createTextField(""), "growx");
            MigLayout migLayout2 = new MigLayout("wrap", string, "");
            JPanel jPanel2 = this.createTabPanel(migLayout2);
            jPanel2.add(this.createLabel("Shipper:"));
            jPanel2.add((Component)this.createTextField(6), "split 2");
            jPanel2.add((Component)this.createTextField(""), "growx");
            jPanel2.add(this.createLabel("Consignee:"));
            jPanel2.add((Component)this.createTextField(6), "split 2");
            jPanel2.add((Component)this.createTextField(""), "growx");
            jPanel2.add(this.createLabel("Departure:"));
            jPanel2.add((Component)this.createTextField(6), "split 2");
            jPanel2.add((Component)this.createTextField(""), "growx");
            jPanel2.add(this.createLabel("Destination:"));
            jPanel2.add((Component)this.createTextField(6), "split 2");
            jPanel2.add((Component)this.createTextField(""), "growx");
            jTabbedPane.addTab(i == 0 ? "Jumping 1" : "Stable 1", jPanel);
            jTabbedPane.addTab(i == 0 ? "Jumping 2" : "Stable 2", jPanel2);
        }
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\nfor (int i = 0; i < 2; i++) {   // Jumping for 0 and Stable for 1\n\tString colConstr = i == 0 ? \"[right][300]\" : \"[right, 100lp:pref][300]\";\n\n\tMigLayout LM1 = new MigLayout(\"wrap\", colConstr, \"\");\n\tJPanel panel1 = createTabPanel(LM1);\n\tpanel1.add(createLabel(\"File Number:\"));\n\tpanel1.add(createTextField(\"\"), \"growx\");\n\tpanel1.add(createLabel(\"RFQ Number:\"));\n\tpanel1.add(createTextField(\"\"), \"growx\");\n\tpanel1.add(createLabel(\"Entry Date:\"));\n\tpanel1.add(createTextField(6));\n\tpanel1.add(createLabel(\"Sales Person:\"));\n\tpanel1.add(createTextField(\"\"), \"growx\");\n\n\tMigLayout LM2 = new MigLayout(\"wrap\", colConstr, \"\");\n\tJPanel panel2 = createTabPanel(LM2);\n\tpanel2.add(createLabel(\"Shipper:\"));\n\tpanel2.add(createTextField(6), \"split 2\");\n\tpanel2.add(createTextField(\"\"), \"growx\");\n\tpanel2.add(createLabel(\"Consignee:\"));\n\tpanel2.add(createTextField(6), \"split 2\");\n\tpanel2.add(createTextField(\"\"), \"growx\");\n\tpanel2.add(createLabel(\"Departure:\"));\n\tpanel2.add(createTextField(6), \"split 2\");\n\tpanel2.add(createTextField(\"\"), \"growx\");\n\tpanel2.add(createLabel(\"Destination:\"));\n\tpanel2.add(createTextField(6), \"split 2\");\n\tpanel2.add(createTextField(\"\"), \"growx\");\n\n\ttabbedPane.addTab(i == 0 ? \"Jumping 1\" : \"Stable 2\", panel1);\n\ttabbedPane.addTab(i == 0 ? \"Jumping 2\" : \"Stable 2\", panel2);\n}");
        return jTabbedPane;
    }

    public JComponent createComponent_Sizes() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        MigLayout migLayout = new MigLayout("wrap", "[right][0:pref,grow]", "");
        JPanel jPanel = this.createTabPanel(migLayout);
        JScrollPane jScrollPane = this.createTextAreaScroll("Use slider to see how the components grow and shrink depending on the constraints set on them.", 0, 0, false);
        jScrollPane.setOpaque(false);
        jScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        ((JTextArea)jScrollPane.getViewport().getView()).setOpaque(false);
        jScrollPane.getViewport().setOpaque(false);
        JSplitPane jSplitPane = new JSplitPane(1, true, jPanel, jScrollPane);
        jSplitPane.setOpaque(false);
        jSplitPane.setBorder(null);
        jPanel.add(this.createLabel("\"\""));
        jPanel.add(this.createTextField(""));
        jPanel.add(this.createLabel("\"min!\""));
        jPanel.add((Component)this.createTextField("3", 3), "width min!");
        jPanel.add(this.createLabel("\"pref!\""));
        jPanel.add((Component)this.createTextField("3", 3), "width pref!");
        jPanel.add(this.createLabel("\"min:pref\""));
        jPanel.add((Component)this.createTextField("8", 8), "width min:pref");
        jPanel.add(this.createLabel("\"min:100:150\""));
        jPanel.add((Component)this.createTextField("8", 8), "width min:100:150");
        jPanel.add(this.createLabel("\"min:100:150, growx\""));
        jPanel.add((Component)this.createTextField("8", 8), "width min:100:150, growx");
        jPanel.add(this.createLabel("\"min:100, growx\""));
        jPanel.add((Component)this.createTextField("8", 8), "width min:100, growx");
        jPanel.add(this.createLabel("\"40!\""));
        jPanel.add((Component)this.createTextField("8", 8), "width 40!");
        jPanel.add(this.createLabel("\"40:40:40\""));
        jPanel.add((Component)this.createTextField("8", 8), "width 40:40:40");
        jTabbedPane.addTab("Component Sizes", jSplitPane);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\n\t\tMigLayout LM = new MigLayout(\"wrap\", \"[right][0:pref,grow]\", \"\");\n\n\t\tJPanel panel = createTabPanel(LM);\n\t\tJScrollPane descrText = createTextAreaScroll(\"Use slider to see how the components grow and shrink depending on the constraints set on them.\", 0, 0, false);\n\n\t\tdescrText.setOpaque(OPAQUE);\n\t\tdescrText.setBorder(new EmptyBorder(10, 10, 10, 10));\n\t\t((JTextArea) descrText.getViewport().getView()).setOpaque(OPAQUE);\n\t\tdescrText.getViewport().setOpaque(OPAQUE);\n\n\t\tJSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, panel, descrText);\n\t\tsplitPane.setOpaque(OPAQUE);\n\t\tsplitPane.setBorder(null);\n\n\t\tpanel.add(createLabel(\"\\\"\\\"\"));\n\t\tpanel.add(createTextField(\"\"));\n\t\tpanel.add(createLabel(\"\\\"min!\\\"\"));\n\t\tpanel.add(createTextField(\"3\", 3), \"width min!\");\n\t\tpanel.add(createLabel(\"\\\"pref!\\\"\"));\n\t\tpanel.add(createTextField(\"3\", 3), \"width pref!\");\n\t\tpanel.add(createLabel(\"\\\"min:pref\\\"\"));\n\t\tpanel.add(createTextField(\"8\", 8), \"width min:pref\");\n\t\tpanel.add(createLabel(\"\\\"min:100:150\\\"\"));\n\t\tpanel.add(createTextField(\"8\", 8), \"width min:100:150\");\n\t\tpanel.add(createLabel(\"\\\"min:100:150, growx\\\"\"));\n\t\tpanel.add(createTextField(\"8\", 8), \"width min:100:150, growx\");\n\t\tpanel.add(createLabel(\"\\\"min:100, growx\\\"\"));\n\t\tpanel.add(createTextField(\"8\", 8), \"width min:100, growx\");\n\t\tpanel.add(createLabel(\"\\\"40!\\\"\"));\n\t\tpanel.add(createTextField(\"8\", 8), \"width 40!\");\n\t\tpanel.add(createLabel(\"\\\"40:40:40\\\"\"));\n\t\tpanel.add(createTextField(\"8\", 8), \"width 40:40:40\");\n\n\t\ttabbedPane.addTab(\"Component Sizes\", splitPane);");
        return jTabbedPane;
    }

    public JComponent createCell_Alignments() {
        String[] stringArray;
        JTabbedPane jTabbedPane = new JTabbedPane();
        MigLayout migLayout = new MigLayout("wrap", "[grow,left][grow,center][grow,right][grow,fill,center]", "[]unrel[][]");
        JPanel jPanel = this.createTabPanel(migLayout);
        String[] stringArray2 = new String[]{"", "growx", "growx 0", "left", "center", "right", "leading", "trailing"};
        jPanel.add((Component)this.createLabel("[left]"), "c");
        jPanel.add((Component)this.createLabel("[center]"), "c");
        jPanel.add((Component)this.createLabel("[right]"), "c");
        jPanel.add((Component)this.createLabel("[fill,center]"), "c, growx 0");
        for (int i = 0; i < stringArray2.length; ++i) {
            for (int j = 0; j < 4; ++j) {
                stringArray = stringArray2[i].length() > 0 ? stringArray2[i] : "default";
                jPanel.add((Component)this.createButton((String)stringArray), stringArray2[i]);
            }
        }
        MigLayout migLayout2 = new MigLayout("wrap,flowy", "[right][]", "[grow,top][grow,center][grow,bottom][grow,fill,bottom][grow,fill,baseline]");
        JPanel jPanel2 = this.createTabPanel(migLayout2);
        stringArray = new String[]{"", "growy", "growy 0", "top", "aligny center", "bottom"};
        jPanel2.add((Component)this.createLabel("[top]"), "aligny center");
        jPanel2.add((Component)this.createLabel("[center]"), "aligny center");
        jPanel2.add((Component)this.createLabel("[bottom]"), "aligny center");
        jPanel2.add((Component)this.createLabel("[fill, bottom]"), "aligny center, growy 0");
        jPanel2.add((Component)this.createLabel("[fill, baseline]"), "aligny center");
        for (int i = 0; i < stringArray.length; ++i) {
            for (int j = 0; j < 5; ++j) {
                String string = stringArray[i].length() > 0 ? stringArray[i] : "default";
                JButton jButton = this.createButton(string);
                if (j == 4 && i <= 1) {
                    jButton.setFont(new Font("sansserif", 0, 16 + i * 5));
                }
                jPanel2.add((Component)jButton, stringArray[i]);
            }
        }
        jTabbedPane.addTab("Horizontal", jPanel);
        jTabbedPane.addTab("Vertical", jPanel2);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\n// Horizontal\nMigLayout hLM = new MigLayout(\"wrap\",\n                              \"[grow,left][grow,center][grow,right][grow,fill,center]\",\n                              \"[]unrel[][]\");\nJPanel hPanel = createTabPanel(hLM);\nString[] sizes = new String[] {\"\", \"growx\", \"growx 0\", \"left\", \"center\", \"right\", \"leading\", \"trailing\"};\nhPanel.add(createLabel(\"[left]\"), \"c\");\nhPanel.add(createLabel(\"[center]\"), \"c\");\nhPanel.add(createLabel(\"[right]\"), \"c\");\nhPanel.add(createLabel(\"[fill,center]\"), \"c, growx 0\");\n\nfor (int r = 0; r < sizes.length; r++) {\n\tfor (int c = 0; c < 4; c++) {\n\t\tString text = sizes[r].length() > 0 ? sizes[r] : \"default\";\n\t\thPanel.add(createButton(text), sizes[r]);\n\t}\n}\n\n// Vertical\nMigLayout vLM = new MigLayout(\"wrap,flowy\",\n                                   \"[right][]\",\n                                   \"[grow,top][grow,center][grow,bottom][grow,fill,bottom][grow,fill,baseline]\");\nJPanel vPanel = createTabPanel(vLM);\nString[] vSizes = new String[] {\"\", \"growy\", \"growy 0\", \"top\", \"center\", \"bottom\"};\nvPanel.add(createLabel(\"[top]\"), \"center\");\nvPanel.add(createLabel(\"[center]\"), \"center\");\nvPanel.add(createLabel(\"[bottom]\"), \"center\");\nvPanel.add(createLabel(\"[fill, bottom]\"), \"center, growy 0\");\nvPanel.add(createLabel(\"[fill, baseline]\"), \"center\");\n\nfor (int c = 0; c < vSizes.length; c++) {\n\tfor (int r = 0; r < 5; r++) {\n\t\tString text = vSizes[c].length() > 0 ? vSizes[c] : \"default\";\n\t\tJButton b = createButton(text);\n\t\tif (r == 4 && c <= 1)\n\t\t\tb.setFont(new Font(\"sansserif\", Font.PLAIN, 16 + c * 5));\n\t\tvPanel.add(b, vSizes[c]);\n\t}\n}\n\ntabbedPane.addTab(\"Horizontal\", hPanel);\ntabbedPane.addTab(\"Vertical\", vPanel);");
        return jTabbedPane;
    }

    public JComponent createUnits() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        MigLayout migLayout = new MigLayout("wrap,nocache", "[right][]", "");
        JPanel jPanel = this.createTabPanel(migLayout);
        String[] stringArray = new String[]{"72pt", "25.4mm", "2.54cm", "1in", "72px", "96px", "120px", "25%", "20sp"};
        for (int i = 0; i < stringArray.length; ++i) {
            jPanel.add(this.createLabel(stringArray[i]));
            jPanel.add((Component)this.createTextField(""), "width " + stringArray[i] + "!");
        }
        MigLayout migLayout2 = new MigLayout("nocache", "[right][][]", "");
        JPanel jPanel2 = this.createTabPanel(migLayout2);
        jPanel2.add(this.createLabel("9 cols"));
        jPanel2.add(this.createTextField(9));
        String[] stringArray2 = new String[]{"75lp", "75px", "88px", "100px"};
        jPanel2.add((Component)this.createLabel("Width of createTextField(9)"), "wrap");
        for (int i = 0; i < stringArray2.length; ++i) {
            jPanel2.add(this.createLabel(stringArray2[i]));
            jPanel2.add((Component)this.createTextField(""), "width " + stringArray2[i] + "!, wrap");
        }
        MigLayout migLayout3 = new MigLayout("wrap,flowy,nocache", "[c]", "[top][top]");
        JPanel jPanel3 = this.createTabPanel(migLayout3);
        String[] stringArray3 = new String[]{"72pt", "25.4mm", "2.54cm", "1in", "72px", "96px", "120px", "25%", "20sp"};
        for (int i = 0; i < stringArray.length; ++i) {
            jPanel3.add(this.createLabel(stringArray3[i]));
            jPanel3.add((Component)this.createTextArea("", 0, 5), "width 50!, height " + stringArray3[i] + "!");
        }
        MigLayout migLayout4 = new MigLayout("wrap,flowy,nocache", "[c]", "[top][top]40px[top][top]");
        JPanel jPanel4 = this.createTabPanel(migLayout4);
        jPanel4.add(this.createLabel("4 rows"));
        jPanel4.add((Component)this.createTextArea("", 4, 5), "width 50!");
        jPanel4.add(this.createLabel("field"));
        jPanel4.add(this.createTextField(5));
        String[] stringArray4 = new String[]{"63lp", "57px", "63px", "68px", "25%"};
        String[] stringArray5 = new String[]{"21lp", "21px", "23px", "24px", "10%"};
        for (int i = 0; i < stringArray4.length; ++i) {
            jPanel4.add(this.createLabel(stringArray4[i]));
            jPanel4.add((Component)this.createTextArea("", 1, 5), "width 50!, height " + stringArray4[i] + "!");
            jPanel4.add(this.createLabel(stringArray5[i]));
            jPanel4.add((Component)this.createTextField(5), "height " + stringArray5[i] + "!");
        }
        jPanel4.add((Component)this.createLabel("button"), "skip 2");
        jPanel4.add(this.createButton("..."));
        jTabbedPane.addTab("Horizontal", jPanel);
        jTabbedPane.addTab("Horizontal LP", jPanel2);
        jTabbedPane.addTab("Vertical", jPanel3);
        jTabbedPane.addTab("Vertical LP", jPanel4);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\n// Horizontal\nMigLayout hLM = new MigLayout(\"wrap,nocache\",\n\t\t\t\t\t\t\t  \"[right][]\",\n\t\t\t\t\t\t\t  \"\");\nJPanel hPanel = createTabPanel(hLM);\nString[] sizes = new String[] {\"72pt\", \"25.4mm\", \"2.54cm\", \"1in\", \"72px\", \"96px\", \"120px\", \"25%\", \"20sp\"};\nfor (int i = 0; i < sizes.length; i++) {\n\thPanel.add(createLabel(sizes[i]));\n\thPanel.add(createTextField(\"\"), \"width \" + sizes[i] + \"!\");\n}\n\n// Horizontal lp\nMigLayout hlpLM = new MigLayout(\"nocache\", \"[right][][]\", \"\");\nJPanel hlpPanel = createTabPanel(hlpLM);\nhlpPanel.add(createLabel(\"9 cols\"));\nhlpPanel.add(createTextField(9));\nString[] lpSizes = new String[] {\"75lp\", \"75px\", \"88px\", \"100px\"};\nhlpPanel.add(createLabel(\"Width of createTextField(9)\"), \"wrap\");\nfor (int i = 0; i < lpSizes.length; i++) {\n\thlpPanel.add(createLabel(lpSizes[i]));\n\thlpPanel.add(createTextField(\"\"), \"width \" + lpSizes[i] + \"!, wrap\");\n}\n\n// Vertical\nMigLayout vLM = new MigLayout(\"wrap,flowy,nocache\",\n\t\t\t\t\t\t\t  \"[c]\",\n\t\t\t\t\t\t\t  \"[top][top]\");\nJPanel vPanel = createTabPanel(vLM);\nString[] vSizes = new String[] {\"72pt\", \"25.4mm\", \"2.54cm\", \"1in\", \"72px\", \"96px\", \"120px\", \"25%\", \"20sp\"};\nfor (int i = 0; i < sizes.length; i++) {\n\tvPanel.add(createLabel(vSizes[i]));\n\tvPanel.add(createTextArea(\"\", 0, 5), \"width 50!, height \" + vSizes[i] + \"!\");\n}\n\n// Vertical lp\nMigLayout vlpLM = new MigLayout(\"wrap,flowy,nocache\",\n\t\t\t\t\t\t\t\t\"[c]\",\n\t\t\t\t\t\t\t\t\"[top][top]40px[top][top]\");\nJPanel vlpPanel = createTabPanel(vlpLM);\nvlpPanel.add(createLabel(\"4 rows\"));\nvlpPanel.add(createTextArea(\"\", 4, 5), \"width 50!\");\nvlpPanel.add(createLabel(\"field\"));\nvlpPanel.add(createTextField(5));\n\nString[] vlpSizes1 = new String[] {\"63lp\", \"57px\", \"63px\", \"68px\", \"25%\"};\nString[] vlpSizes2 = new String[] {\"21lp\", \"21px\", \"23px\", \"24px\", \"10%\"};\nfor (int i = 0; i < vlpSizes1.length; i++) {\n\tvlpPanel.add(createLabel(vlpSizes1[i]));\n\tvlpPanel.add(createTextArea(\"\", 1, 5), \"width 50!, height \" + vlpSizes1[i] + \"!\");\n\tvlpPanel.add(createLabel(vlpSizes2[i]));\n\tvlpPanel.add(createTextField(5), \"height \" + vlpSizes2[i] + \"!\");\n}\n\nvlpPanel.add(createLabel(\"button\"), \"skip 2\");\nvlpPanel.add(createButton(\"...\"));\n\ntabbedPane.addTab(\"Horizontal\", hPanel);\ntabbedPane.addTab(\"Horizontal LP\", hlpPanel);\ntabbedPane.addTab(\"Vertical\", vPanel);\ntabbedPane.addTab(\"Vertical LP\", vlpPanel);");
        return jTabbedPane;
    }

    public JComponent createGrouping() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        MigLayout migLayout = new MigLayout("", "[]push[][][]", "");
        JPanel jPanel = this.createTabPanel(migLayout);
        jPanel.add(this.createButton("Help"));
        jPanel.add((Component)this.createButton("< Back"), "");
        jPanel.add((Component)this.createButton("Forward >"), "gap push");
        jPanel.add((Component)this.createButton("Apply"), "gap unrel");
        jPanel.add((Component)this.createButton("Cancel"), "gap unrel");
        MigLayout migLayout2 = new MigLayout("nogrid, fillx");
        JPanel jPanel2 = this.createTabPanel(migLayout2);
        jPanel2.add((Component)this.createButton("Help"), "sg");
        jPanel2.add((Component)this.createButton("< Back"), "sg,gap push");
        jPanel2.add((Component)this.createButton("Forward >"), "sg");
        jPanel2.add((Component)this.createButton("Apply"), "sg,gap unrel");
        jPanel2.add((Component)this.createButton("Cancel"), "sg,gap unrel");
        MigLayout migLayout3 = new MigLayout("", "[sg,fill]push[sg,fill][sg,fill]unrel[sg,fill]unrel[sg,fill]", "");
        JPanel jPanel3 = this.createTabPanel(migLayout3);
        jPanel3.add(this.createButton("Help"));
        jPanel3.add(this.createButton("< Back"));
        jPanel3.add(this.createButton("Forward >"));
        jPanel3.add(this.createButton("Apply"));
        jPanel3.add(this.createButton("Cancel"));
        MigLayout migLayout4 = new MigLayout();
        JPanel jPanel4 = this.createTabPanel(migLayout4);
        jPanel4.add(this.createLabel("File Number:"));
        jPanel4.add((Component)this.createTextField(30), "wrap");
        jPanel4.add(this.createLabel("BL/MBL number:"));
        jPanel4.add((Component)this.createTextField(7), "split 2");
        jPanel4.add((Component)this.createTextField(7), "wrap");
        jPanel4.add(this.createLabel("Entry Date:"));
        jPanel4.add((Component)this.createTextField(7), "wrap");
        jPanel4.add(this.createLabel("RFQ Number:"));
        jPanel4.add((Component)this.createTextField(30), "wrap");
        jPanel4.add(this.createLabel("Goods:"));
        jPanel4.add((Component)this.createCheck("Dangerous"), "wrap");
        jPanel4.add(this.createLabel("Shipper:"));
        jPanel4.add((Component)this.createTextField(30), "wrap");
        jPanel4.add(this.createLabel("Customer:"));
        jPanel4.add((Component)this.createTextField(""), "split 2,growx");
        jPanel4.add((Component)this.createButton("..."), "width 60px:pref,wrap");
        jPanel4.add(this.createLabel("Port of Loading:"));
        jPanel4.add((Component)this.createTextField(30), "wrap");
        jPanel4.add(this.createLabel("Destination:"));
        jPanel4.add((Component)this.createTextField(30), "wrap");
        MigLayout migLayout5 = new MigLayout("", "[]", "[sg]");
        JPanel jPanel5 = this.createTabPanel(migLayout5);
        jPanel5.add(this.createLabel("File Number:"));
        jPanel5.add((Component)this.createTextField(30), "wrap");
        jPanel5.add(this.createLabel("BL/MBL number:"));
        jPanel5.add((Component)this.createTextField(7), "split 2");
        jPanel5.add((Component)this.createTextField(7), "wrap");
        jPanel5.add(this.createLabel("Entry Date:"));
        jPanel5.add((Component)this.createTextField(7), "wrap");
        jPanel5.add(this.createLabel("RFQ Number:"));
        jPanel5.add((Component)this.createTextField(30), "wrap");
        jPanel5.add(this.createLabel("Goods:"));
        jPanel5.add((Component)this.createCheck("Dangerous"), "wrap");
        jPanel5.add(this.createLabel("Shipper:"));
        jPanel5.add((Component)this.createTextField(30), "wrap");
        jPanel5.add(this.createLabel("Customer:"));
        jPanel5.add((Component)this.createTextField(""), "split 2,growx");
        jPanel5.add((Component)this.createButton("..."), "width 50px:pref,wrap");
        jPanel5.add(this.createLabel("Port of Loading:"));
        jPanel5.add((Component)this.createTextField(30), "wrap");
        jPanel5.add(this.createLabel("Destination:"));
        jPanel5.add((Component)this.createTextField(30), "wrap");
        jTabbedPane.addTab("Ungrouped", jPanel);
        jTabbedPane.addTab("Grouped (Components)", jPanel2);
        jTabbedPane.addTab("Grouped (Columns)", jPanel3);
        jTabbedPane.addTab("Ungrouped Rows", jPanel4);
        jTabbedPane.addTab("Grouped Rows", jPanel5);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\n// Ungrouped\nMigLayout ugM = new MigLayout(\"\", \"[]push[][][]\", \"\");\nJPanel ugPanel = createTabPanel(ugM);\nugPanel.add(createButton(\"Help\"));\nugPanel.add(createButton(\"< Back\"), \"\");\nugPanel.add(createButton(\"Forward >\"), \"gap push\");\nugPanel.add(createButton(\"Apply\"), \"gap unrel\");\nugPanel.add(createButton(\"Cancel\"), \"gap unrel\");\n\n// Grouped Components\nMigLayout gM = new MigLayout(\"nogrid, fillx\");\nJPanel gPanel = createTabPanel(gM);\ngPanel.add(createButton(\"Help\"), \"sg\");\ngPanel.add(createButton(\"< Back\"), \"sg,gap push\");\ngPanel.add(createButton(\"Forward >\"), \"sg\");\ngPanel.add(createButton(\"Apply\"), \"sg,gap unrel\");\ngPanel.add(createButton(\"Cancel\"), \"sg,gap unrel\");\n\n// Grouped Columns\nMigLayout gcM = new MigLayout(\"\", \"[sg,fill]push[sg,fill][sg,fill]unrel[sg,fill]unrel[sg,fill]\", \"\");\nJPanel gcPanel = createTabPanel(gcM);\ngcPanel.add(createButton(\"Help\"));\ngcPanel.add(createButton(\"< Back\"));\ngcPanel.add(createButton(\"Forward >\"));\ngcPanel.add(createButton(\"Apply\"));\ngcPanel.add(createButton(\"Cancel\"));\n\n// Ungrouped Rows\nMigLayout ugrM = new MigLayout();     // no \"sg\" is the only difference to next panel\nJPanel ugrPanel = createTabPanel(ugrM);\nugrPanel.add(createLabel(\"File Number:\"));\nugrPanel.add(createTextField(30), \"wrap\");\nugrPanel.add(createLabel(\"BL/MBL number:\"));\nugrPanel.add(createTextField(7), \"split 2\");\nugrPanel.add(createTextField(7), \"wrap\");\nugrPanel.add(createLabel(\"Entry Date:\"));\nugrPanel.add(createTextField(7), \"wrap\");\nugrPanel.add(createLabel(\"RFQ Number:\"));\nugrPanel.add(createTextField(30), \"wrap\");\nugrPanel.add(createLabel(\"Goods:\"));\nugrPanel.add(createCheck(\"Dangerous\"), \"wrap\");\nugrPanel.add(createLabel(\"Shipper:\"));\nugrPanel.add(createTextField(30), \"wrap\");\nugrPanel.add(createLabel(\"Customer:\"));\nugrPanel.add(createTextField(\"\"), \"split 2,growx\");\nugrPanel.add(createButton(\"...\"), \"width 60px:pref,wrap\");\nugrPanel.add(createLabel(\"Port of Loading:\"));\nugrPanel.add(createTextField(30), \"wrap\");\nugrPanel.add(createLabel(\"Destination:\"));\nugrPanel.add(createTextField(30), \"wrap\");\n\n// Grouped Rows\nMigLayout grM = new MigLayout(\"\", \"[]\", \"[sg]\");    // \"sg\" is the only difference to previous panel\nJPanel grPanel = createTabPanel(grM);\ngrPanel.add(createLabel(\"File Number:\"));\ngrPanel.add(createTextField(30),\"wrap\");\ngrPanel.add(createLabel(\"BL/MBL number:\"));\ngrPanel.add(createTextField(7),\"split 2\");\ngrPanel.add(createTextField(7), \"wrap\");\ngrPanel.add(createLabel(\"Entry Date:\"));\ngrPanel.add(createTextField(7), \"wrap\");\ngrPanel.add(createLabel(\"RFQ Number:\"));\ngrPanel.add(createTextField(30), \"wrap\");\ngrPanel.add(createLabel(\"Goods:\"));\ngrPanel.add(createCheck(\"Dangerous\"), \"wrap\");\ngrPanel.add(createLabel(\"Shipper:\"));\ngrPanel.add(createTextField(30), \"wrap\");\ngrPanel.add(createLabel(\"Customer:\"));\ngrPanel.add(createTextField(\"\"), \"split 2,growx\");\ngrPanel.add(createButton(\"...\"), \"width 50px:pref,wrap\");\ngrPanel.add(createLabel(\"Port of Loading:\"));\ngrPanel.add(createTextField(30), \"wrap\");\ngrPanel.add(createLabel(\"Destination:\"));\ngrPanel.add(createTextField(30), \"wrap\");\n\ntabbedPane.addTab(\"Ungrouped\", ugPanel);\ntabbedPane.addTab(\"Grouped (Components)\", gPanel);\ntabbedPane.addTab(\"Grouped (Columns)\", gcPanel);\ntabbedPane.addTab(\"Ungrouped Rows\", ugrPanel);\ntabbedPane.addTab(\"Grouped Rows\", grPanel);");
        return jTabbedPane;
    }

    public JComponent createSpan() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        MigLayout migLayout = new MigLayout("nocache", "[fill][25%!,fill][105lp!,fill][100px!,fill]", "[]15[][]");
        JPanel jPanel = this.createTabPanel(migLayout);
        jPanel.add(this.createTextField("Col1 [ ]"));
        jPanel.add(this.createTextField("Col2 [25%!]"));
        jPanel.add(this.createTextField("Col3 [105lp!]"));
        jPanel.add((Component)this.createTextField("Col4 [100px!]"), "wrap");
        jPanel.add(this.createLabel("Full Name:"));
        jPanel.add((Component)this.createTextField("span, growx", 40), "span,growx");
        jPanel.add(this.createLabel("Phone:"));
        jPanel.add((Component)this.createTextField(5), "span 3, split 5");
        jPanel.add(this.createTextField(7));
        jPanel.add(this.createTextField(7));
        jPanel.add(this.createTextField(9));
        jPanel.add((Component)this.createLabel("(span 3, split 4)"), "wrap");
        jPanel.add(this.createLabel("Zip/City:"));
        jPanel.add(this.createTextField(5));
        jPanel.add((Component)this.createTextField("span 2, growx", 5), "span 2,growx");
        MigLayout migLayout2 = new MigLayout("wrap", "[225lp]para[225lp]", "[]3[]unrel[]3[]unrel[]3[]");
        JPanel jPanel2 = this.createTabPanel(migLayout2);
        jPanel2.add(this.createLabel("Name"));
        jPanel2.add(this.createLabel("Notes"));
        jPanel2.add((Component)this.createTextField("growx"), "growx");
        jPanel2.add((Component)this.createTextArea("spany,grow", 5, 20), "spany,grow");
        jPanel2.add(this.createLabel("Phone"));
        jPanel2.add((Component)this.createTextField("growx"), "growx");
        jPanel2.add(this.createLabel("Fax"));
        jPanel2.add((Component)this.createTextField("growx"), "growx");
        jTabbedPane.addTab("Column Span/Split", jPanel);
        jTabbedPane.addTab("Row Span", jPanel2);
        this.setSource("\t\tJTabbedPane tabbedPane = new JTabbedPane();\n\n\t\t// Horizontal span\n\t\tMigLayout colLM = new MigLayout(\"\",\n\t\t                                     \"[fill][25%!,fill][105lp!,fill][100px!,fill]\",\n\t\t                                     \"[]15[][]\");\n\t\tJPanel colPanel = createTabPanel(colLM);\n\t\tcolPanel.add(createTextField(\"Col1 [ ]\"));\n\t\tcolPanel.add(createTextField(\"Col2 [25%!]\"));\n\t\tcolPanel.add(createTextField(\"Col3 [105lp!]\"));\n\t\tcolPanel.add(createTextField(\"Col4 [100px!]\"), \"wrap\");\n\n\t\tcolPanel.add(createLabel(\"Full Name:\"));\n\t\tcolPanel.add(createTextField(\"span, growx\", 40), \"span,growx\");\n\n\t\tcolPanel.add(createLabel(\"Phone:\"));\n\t\tcolPanel.add(createTextField(5), \"span 3, split 5\");\n\t\tcolPanel.add(createTextField(7));\n\t\tcolPanel.add(createTextField(7));\n\t\tcolPanel.add(createTextField(9));\n\t\tcolPanel.add(createLabel(\"(span 3, split 4)\"), \"wrap\");\n\n\t\tcolPanel.add(createLabel(\"Zip/City:\"));\n\t\tcolPanel.add(createTextField(5));\n\t\tcolPanel.add(createTextField(\"span 2, growx\", 5), \"span 2,growx\");\n\n\t\t// Vertical span\n\t\tMigLayout rowLM = new MigLayout(\"wrap\",\n\t\t                                     \"[225lp]para[225lp]\",\n\t\t                                     \"[]3[]unrel[]3[]unrel[]3[]\");\n\t\tJPanel rowPanel = createTabPanel(rowLM);\n\t\trowPanel.add(createLabel(\"Name\"));\n\t\trowPanel.add(createLabel(\"Notes\"));\n\t\trowPanel.add(createTextField(\"growx\"), \"growx\");\n\t\trowPanel.add(createTextArea(\"spany,grow\", 5, 20), \"spany,grow\");\n\t\trowPanel.add(createLabel(\"Phone\"));\n\t\trowPanel.add(createTextField(\"growx\"), \"growx\");\n\t\trowPanel.add(createLabel(\"Fax\"));\n\t\trowPanel.add(createTextField(\"growx\"), \"growx\");\n\n\t\ttabbedPane.addTab(\"Column Span/Split\", colPanel);\n\t\ttabbedPane.addTab(\"Row Span\", rowPanel);");
        return jTabbedPane;
    }

    public JComponent createGrowing() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        MigLayout migLayout = new MigLayout("", "[pref!][grow,fill]", "[]15[]");
        JPanel jPanel = this.createTabPanel(migLayout);
        jPanel.add(this.createLabel("Fixed"));
        jPanel.add((Component)this.createLabel("Gets all extra space"), "wrap");
        jPanel.add(this.createTextField(5));
        jPanel.add(this.createTextField(5));
        MigLayout migLayout2 = new MigLayout("", "[pref!][grow,fill]", "[]15[]");
        JPanel jPanel2 = this.createTabPanel(migLayout2);
        jPanel2.add(this.createLabel("Fixed"));
        jPanel2.add(this.createLabel("Gets half of extra space"));
        jPanel2.add((Component)this.createLabel("Gets half of extra space"), "wrap");
        jPanel2.add(this.createTextField(5));
        jPanel2.add(this.createTextField(5));
        jPanel2.add(this.createTextField(5));
        MigLayout migLayout3 = new MigLayout("", "[pref!][0:0,grow 25,fill][0:0,grow 75,fill]", "[]15[]");
        JPanel jPanel3 = this.createTabPanel(migLayout3);
        jPanel3.add(this.createLabel("Fixed"));
        jPanel3.add((Component)this.createLabel("Gets 25% of extra space"), "");
        jPanel3.add((Component)this.createLabel("Gets 75% of extra space"), "wrap");
        jPanel3.add(this.createTextField(5));
        jPanel3.add(this.createTextField(5));
        jPanel3.add(this.createTextField(5));
        MigLayout migLayout4 = new MigLayout("", "[0:0,grow 33,fill][0:0,grow 67,fill]", "[]15[]");
        JPanel jPanel4 = this.createTabPanel(migLayout4);
        jPanel4.add((Component)this.createLabel("Gets 33% of extra space"), "");
        jPanel4.add((Component)this.createLabel("Gets 67% of extra space"), "wrap");
        jPanel4.add(this.createTextField(5));
        jPanel4.add(this.createTextField(5));
        MigLayout migLayout5 = new MigLayout("flowy", "[]15[]", "[][c,pref!][c,grow 25,fill][c,grow 75,fill]");
        JPanel jPanel5 = this.createTabPanel(migLayout5);
        jPanel5.add((Component)this.createLabel("Fixed"), "skip");
        jPanel5.add(this.createLabel("Gets 25% of extra space"));
        jPanel5.add((Component)this.createLabel("Gets 75% of extra space"), "wrap");
        jPanel5.add(this.createLabel("new JTextArea(4, 30)"));
        jPanel5.add(this.createTextAreaScroll("", 4, 30, false));
        jPanel5.add(this.createTextAreaScroll("", 4, 30, false));
        jPanel5.add(this.createTextAreaScroll("", 4, 30, false));
        MigLayout migLayout6 = new MigLayout("flowy", "[]15[]", "[][c,grow 33,fill][c,grow 67,fill]");
        JPanel jPanel6 = this.createTabPanel(migLayout6);
        jPanel6.add((Component)this.createLabel("Gets 33% of extra space"), "skip");
        jPanel6.add((Component)this.createLabel("Gets 67% of extra space"), "wrap");
        jPanel6.add(this.createLabel("new JTextArea(4, 30)"));
        jPanel6.add(this.createTextAreaScroll("", 4, 30, false));
        jPanel6.add(this.createTextAreaScroll("", 4, 30, false));
        jTabbedPane.addTab("All", jPanel);
        jTabbedPane.addTab("Half", jPanel2);
        jTabbedPane.addTab("Percent 1", jPanel3);
        jTabbedPane.addTab("Percent 2", jPanel4);
        jTabbedPane.addTab("Vertical 1", jPanel5);
        jTabbedPane.addTab("Vertical 2", jPanel6);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\n// All tab\nMigLayout allLM = new MigLayout(\"\",\n                                \"[pref!][grow,fill]\",\n                                \"[]15[]\");\nJPanel allTab = createTabPanel(allLM);\nallTab.add(createLabel(\"Fixed\"));\nallTab.add(createLabel(\"Gets all extra space\"), \"wrap\");\nallTab.add(createTextField(5));\nallTab.add(createTextField(5));\n\n// Half tab\nMigLayout halfLM = new MigLayout(\"\",\n                                 \"[pref!][grow,fill]\",\n                                 \"[]15[]\");\nJPanel halfTab = createTabPanel(halfLM);\nhalfTab.add(createLabel(\"Fixed\"));\nhalfTab.add(createLabel(\"Gets half of extra space\"));\nhalfTab.add(createLabel(\"Gets half of extra space\"), \"wrap\");\nhalfTab.add(createTextField(5));\nhalfTab.add(createTextField(5));\nhalfTab.add(createTextField(5));\n\n// Percent 1 tab\nMigLayout p1LM = new MigLayout(\"\",\n                               \"[pref!][0:0,grow 25,fill][0:0,grow 75,fill]\",\n                               \"[]15[]\");\nJPanel p1Tab = createTabPanel(p1LM);\np1Tab.add(createLabel(\"Fixed\"));\np1Tab.add(createLabel(\"Gets 25% of extra space\"), \"\");\np1Tab.add(createLabel(\"Gets 75% of extra space\"), \"wrap\");\np1Tab.add(createTextField(5));\np1Tab.add(createTextField(5));\np1Tab.add(createTextField(5));\n\n// Percent 2 tab\nMigLayout p2LM = new MigLayout(\"\",\n                               \"[0:0,grow 33,fill][0:0,grow 67,fill]\",\n                               \"[]15[]\");\nJPanel p2Tab = createTabPanel(p2LM);\np2Tab.add(createLabel(\"Gets 33% of extra space\"), \"\");\np2Tab.add(createLabel(\"Gets 67% of extra space\"), \"wrap\");\np2Tab.add(createTextField(5));\np2Tab.add(createTextField(5));\n\n// Vertical 1 tab\nMigLayout v1LM = new MigLayout(\"flowy\",\n                               \"[]15[]\",\n                               \"[][c,pref!][c,grow 25,fill][c,grow 75,fill]\");\nJPanel v1Tab = createTabPanel(v1LM);\nv1Tab.add(createLabel(\"Fixed\"), \"skip\");\nv1Tab.add(createLabel(\"Gets 25% of extra space\"));\nv1Tab.add(createLabel(\"Gets 75% of extra space\"), \"wrap\");\nv1Tab.add(createLabel(\"new JTextArea(4, 30)\"));\nv1Tab.add(createTextAreaScroll(\"\", 4, 30, false));\nv1Tab.add(createTextAreaScroll(\"\", 4, 30, false));\nv1Tab.add(createTextAreaScroll(\"\", 4, 30, false));\n\n// Vertical 2 tab\nMigLayout v2LM = new MigLayout(\"flowy\",\n                               \"[]15[]\",\n                               \"[][c,grow 33,fill][c,grow 67,fill]\");\nJPanel v2Tab = createTabPanel(v2LM);\nv2Tab.add(createLabel(\"Gets 33% of extra space\"), \"skip\");\nv2Tab.add(createLabel(\"Gets 67% of extra space\"), \"wrap\");\nv2Tab.add(createLabel(\"new JTextArea(4, 30)\"));\nv2Tab.add(createTextAreaScroll(\"\", 4, 30, false));\nv2Tab.add(createTextAreaScroll(\"\", 4, 30, false));\n\ntabbedPane.addTab(\"All\", allTab);\ntabbedPane.addTab(\"Half\", halfTab);\ntabbedPane.addTab(\"Percent 1\", p1Tab);\ntabbedPane.addTab(\"Percent 2\", p2Tab);\ntabbedPane.addTab(\"Vertical 1\", v1Tab);\ntabbedPane.addTab(\"Vertical 2\", v2Tab);");
        return jTabbedPane;
    }

    public JComponent createBasic_Sizes() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        MigLayout migLayout = new MigLayout("", "[]15[75px]25[min]25[]", "[]15");
        JPanel jPanel = this.createTabPanel(migLayout);
        jPanel.add((Component)this.createLabel("75px"), "skip");
        jPanel.add(this.createLabel("Min"));
        jPanel.add((Component)this.createLabel("Pref"), "wrap");
        jPanel.add(this.createLabel("new TextField(15)"));
        jPanel.add(this.createTextField(15));
        jPanel.add(this.createTextField(15));
        jPanel.add(this.createTextField(15));
        MigLayout migLayout2 = new MigLayout("flowy,wrap", "[]15[]", "[]15[c,45px]15[c,min]15[c,pref]");
        JPanel jPanel2 = this.createTabPanel(migLayout2);
        jPanel2.add((Component)this.createLabel("45px"), "skip");
        jPanel2.add(this.createLabel("Min"));
        jPanel2.add(this.createLabel("Pref"));
        jPanel2.add(this.createLabel("new JTextArea(10, 40)"));
        jPanel2.add(this.createTextArea("", 10, 40));
        jPanel2.add(this.createTextArea("", 10, 40));
        jPanel2.add(this.createTextArea("", 10, 40));
        MigLayout migLayout3 = new MigLayout("flowy,wrap", "[]15[]", "[]15[baseline]15[baseline]15[baseline]");
        JPanel jPanel3 = this.createTabPanel(migLayout3);
        jPanel3.add((Component)this.createLabel("45px"), "skip");
        jPanel3.add(this.createLabel("Min"));
        jPanel3.add(this.createLabel("Pref"));
        jPanel3.add(this.createLabel("new JTextArea(10, 40)"));
        jPanel3.add((Component)this.createTextArea("", 10, 40), "height 45");
        jPanel3.add((Component)this.createTextArea("", 10, 40), "height min");
        jPanel3.add((Component)this.createTextArea("", 10, 40), "height pref");
        jTabbedPane.addTab("Horizontal - Column size set", jPanel);
        jTabbedPane.addTab("Vertical - Row sized", jPanel2);
        jTabbedPane.addTab("Vertical - Component sized + Baseline", jPanel3);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\n// Horizontal tab\nMigLayout horLM = new MigLayout(\"\",\n                                     \"[]15[75px]25[min]25[]\",\n                                     \"[]15\");\nJPanel horTab = createTabPanel(horLM);\nhorTab.add(createLabel(\"75px\"), \"skip\");\nhorTab.add(createLabel(\"Min\"));\nhorTab.add(createLabel(\"Pref\"), \"wrap\");\n\nhorTab.add(createLabel(\"new TextField(15)\"));\nhorTab.add(createTextField(15));\nhorTab.add(createTextField(15));\nhorTab.add(createTextField(15));\n\n// Vertical tab 1\nMigLayout verLM = new MigLayout(\"flowy,wrap\",\n                                     \"[]15[]\",\n                                     \"[]15[c,45px]15[c,min]15[c,pref]\");\nJPanel verTab = createTabPanel(verLM);\nverTab.add(createLabel(\"45px\"), \"skip\");\nverTab.add(createLabel(\"Min\"));\nverTab.add(createLabel(\"Pref\"));\n\nverTab.add(createLabel(\"new JTextArea(10, 40)\"));\nverTab.add(createTextArea(\"\", 10, 40));\nverTab.add(createTextArea(\"\", 10, 40));\nverTab.add(createTextArea(\"\", 10, 40));\n\n// Componentsized/Baseline 2\nMigLayout verLM2 = new MigLayout(\"flowy,wrap\",\n                                 \"[]15[]\",\n                                 \"[]15[baseline]15[baseline]15[baseline]\");\nJPanel verTab2 = createTabPanel(verLM2);\nverTab2.add(createLabel(\"45px\"), \"skip\");\nverTab2.add(createLabel(\"Min\"));\nverTab2.add(createLabel(\"Pref\"));\n\nverTab2.add(createLabel(\"new JTextArea(10, 40)\"));\nverTab2.add(createTextArea(\"\", 10, 40), \"height 45\");\nverTab2.add(createTextArea(\"\", 10, 40), \"height min\");\nverTab2.add(createTextArea(\"\", 10, 40), \"height pref\");\n\ntabbedPane.addTab(\"Horizontal - Column size set\", horTab);\ntabbedPane.addTab(\"Vertical - Row sized\", verTab);\ntabbedPane.addTab(\"Vertical - Component sized + Baseline\", verTab2);");
        return jTabbedPane;
    }

    public JComponent createAlignments() {
        int n;
        String[] stringArray;
        int n2;
        JTabbedPane jTabbedPane = new JTabbedPane();
        MigLayout migLayout = new MigLayout("wrap", "[label]15[left]15[center]15[right]15[fill]15[]", "[]15[]");
        String[] stringArray2 = new String[]{"[label]", "[left]", "[center]", "[right]", "[fill]", "[] (Default)"};
        JPanel jPanel = this.createTabPanel(migLayout);
        String[] stringArray3 = new String[]{"First Name", "Phone Number", "Facsmile", "Email", "Address", "Other"};
        for (n2 = 0; n2 < stringArray2.length; ++n2) {
            jPanel.add(this.createLabel(stringArray2[n2]));
        }
        for (n2 = 0; n2 < stringArray2.length; ++n2) {
            for (int i = 0; i < stringArray3.length; ++i) {
                jPanel.add(i == 0 ? this.createLabel(stringArray3[n2] + ":") : this.createButton(stringArray3[n2]));
            }
        }
        MigLayout migLayout2 = new MigLayout("wrap,flowy", "[]unrel[]rel[]", "[top]15[center]15[bottom]15[fill]15[fill,baseline]15[baseline]15[]");
        String[] stringArray4 = new String[]{"[top]", "[center]", "[bottom]", "[fill]", "[fill,baseline]", "[baseline]", "[] (Default)"};
        JPanel jPanel2 = this.createTabPanel(migLayout2);
        if (benchRuns == 0) {
            String[] stringArray5 = new String[2];
            stringArray5[0] = "<html>One</html>";
            stringArray = stringArray5;
            stringArray5[1] = "<html>One<br>Two</html>";
        } else {
            String[] stringArray6 = new String[2];
            stringArray6[0] = "One";
            stringArray = stringArray6;
            stringArray6[1] = "One/Two";
        }
        String[] stringArray7 = stringArray;
        for (n = 0; n < stringArray4.length; ++n) {
            jPanel2.add(this.createLabel(stringArray4[n]));
        }
        for (n = 0; n < stringArray7.length; ++n) {
            for (int i = 0; i < stringArray4.length; ++i) {
                jPanel2.add(this.createButton(stringArray7[n]));
            }
        }
        for (n = 0; n < stringArray4.length; ++n) {
            jPanel2.add(this.createTextField("JTextFied"));
        }
        for (n = 0; n < stringArray4.length; ++n) {
            jPanel2.add(this.createTextArea("JTextArea", 1, 8));
        }
        for (n = 0; n < stringArray4.length; ++n) {
            jPanel2.add(this.createTextArea("JTextArea\nwith two lines", 1, 10));
        }
        for (n = 0; n < stringArray4.length; ++n) {
            jPanel2.add(this.createTextAreaScroll("Scrolling JTextArea\nwith two lines", 1, 15, true));
        }
        jTabbedPane.addTab("Horizontal", jPanel);
        jTabbedPane.addTab("Vertical", jPanel2);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\n// Horizontal tab\nMigLayout horLM = new MigLayout(\"wrap\",\n                                     \"[left]15[center]15[right]15[fill]15[]\",\n                                     \"rel[]rel\");\n\nString[] horLabels = new String[] {\"[left]\", \"[center]\", \"[right]\", \"[fill]\", \"[] (Default)\"};\nJPanel horTab = createTabPanel(horLM);\nString[] horNames = new String[] {\"First Name\", \"Phone Number\", \"Facsmile\", \"Email\", \"Address\"};\nfor (int c = 0; c < horLabels.length; c++)\n\thorTab.add(createLabel(horLabels[c]));\n\nfor (int r = 0; r < horLabels.length; r++) {\n\tfor (int c = 0; c < horNames.length; c++)\n\t\thorTab.add(createButton(horNames[r]));\n}\n\n// Vertical tab\nMigLayout verLM = new MigLayout(\"wrap,flowy\",\n                                \"[]unrel[]rel[]\",\n                                \"[top]15[center]15[bottom]15[fill]15[fill,baseline]15[baseline]15[]\");\n\nString[] verLabels = new String[] {\"[top]\", \"[center]\", \"[bottom]\", \"[fill]\", \"[fill,baseline]\", \"[baseline]\", \"[] (Default)\"};\nJPanel verTab = createTabPanel(verLM);\n\nString[] verNames = new String[] {\"<html>One</html>\", \"<html>One<br>Two</html>\"};\nfor (int c = 0; c < verLabels.length; c++)\n\tverTab.add(createLabel(verLabels[c]));\n\nfor (int r = 0; r < verNames.length; r++) {\n\tfor (int c = 0; c < verLabels.length; c++)\n\t\tverTab.add(createButton(verNames[r]));\n}\n\nfor (int c = 0; c < verLabels.length; c++)\n\tverTab.add(createTextField(\"JTextFied\"));\n\nfor (int c = 0; c < verLabels.length; c++)\n\tverTab.add(createTextArea(\"JTextArea\", 1, 8));\n\nfor (int c = 0; c < verLabels.length; c++)\n\tverTab.add(createTextArea(\"JTextArea\\nwith two lines\", 1, 10));\n\nfor (int c = 0; c < verLabels.length; c++)\n\tverTab.add(createTextAreaScroll(\"Scrolling JTextArea\\nwith two lines\", 1, 15, true));\n\ntabbedPane.addTab(\"Horizontal\", horTab);\ntabbedPane.addTab(\"Vertical\", verTab);");
        return jTabbedPane;
    }

    public JComponent createQuick_Start() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        JPanel jPanel = this.createTabPanel(new MigLayout("inset 20"));
        this.addSeparator(jPanel, "General");
        jPanel.add((Component)this.createLabel("Company"), "gap para");
        jPanel.add((Component)this.createTextField(""), "span, growx");
        jPanel.add((Component)this.createLabel("Contact"), "gap para");
        jPanel.add((Component)this.createTextField(""), "span, growx, wrap para");
        this.addSeparator(jPanel, "Propeller");
        jPanel.add((Component)this.createLabel("PTI/kW"), "gap para");
        jPanel.add(this.createTextField(10));
        jPanel.add((Component)this.createLabel("Power/kW"), "gap para");
        jPanel.add((Component)this.createTextField(10), "wrap");
        jPanel.add((Component)this.createLabel("R/mm"), "gap para");
        jPanel.add(this.createTextField(10));
        jPanel.add((Component)this.createLabel("D/mm"), "gap para");
        jPanel.add(this.createTextField(10));
        jTabbedPane.addTab("Quick Start", jPanel);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\nJPanel p = createTabPanel(new MigLayout());\n\naddSeparator(p, \"General\");\n\np.add(createLabel(\"Company\"), \"gap para\");\np.add(createTextField(\"\"),    \"span, growx, wrap\");\np.add(createLabel(\"Contact\"), \"gap para\");\np.add(createTextField(\"\"),    \"span, growx, wrap para\");\n\naddSeparator(p, \"Propeller\");\n\np.add(createLabel(\"PTI/kW\"),  \"gap para\");\np.add(createTextField(10));\np.add(createLabel(\"Power/kW\"),\"gap para\");\np.add(createTextField(10),    \"wrap\");\np.add(createLabel(\"R/mm\"),    \"gap para\");\np.add(createTextField(10));\np.add(createLabel(\"D/mm\"),    \"gap para\");\np.add(createTextField(10));\n\ntabbedPane.addTab(\"Quick Start\", p);");
        return jTabbedPane;
    }

    public JComponent createGrow_Shrink() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        MigLayout migLayout = new MigLayout("nogrid");
        JPanel jPanel = this.createTabPanel(migLayout);
        JScrollPane jScrollPane = this.createTextAreaScroll("Use the slider to see how the components shrink depending on the constraints set on them.\n\n'shp' means Shrink Priority. Lower values will be shrunk before higer ones and the default value is 100.\n\n'shrink' means Shrink Weight. Lower values relative to other's means they will shrink less when space is scarse. Shrink Weight is only relative to components with the same Shrink Priority. Default Shrink Weight is 100.\n\nThe component's minimum size will always be honored.", 0, 0, true);
        jScrollPane.setOpaque(false);
        jScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        ((JTextArea)jScrollPane.getViewport().getView()).setOpaque(false);
        jScrollPane.getViewport().setOpaque(false);
        JSplitPane jSplitPane = new JSplitPane(1, true, jPanel, jScrollPane);
        jSplitPane.setOpaque(false);
        jSplitPane.setBorder(null);
        jPanel.add((Component)this.createTextField("shp 110", 12), "shp 110");
        jPanel.add((Component)this.createTextField("Default (100)", 12), "");
        jPanel.add((Component)this.createTextField("shp 90", 12), "shp 90");
        jPanel.add((Component)this.createTextField("shrink 25", 20), "newline,shrink 25");
        jPanel.add((Component)this.createTextField("shrink 75", 20), "shrink 75");
        jPanel.add((Component)this.createTextField("Default", 20), "newline");
        jPanel.add((Component)this.createTextField("Default", 20), "");
        jPanel.add((Component)this.createTextField("shrink 0", 40), "newline,shrink 0");
        jPanel.add((Component)this.createTextField("shp 110", 12), "newline,shp 110");
        jPanel.add((Component)this.createTextField("shp 100,shrink 25", 12), "shp 100,shrink 25");
        jPanel.add((Component)this.createTextField("shp 100,shrink 75", 12), "shp 100,shrink 75");
        jTabbedPane.addTab("Shrink", jSplitPane);
        MigLayout migLayout2 = new MigLayout("nogrid", "[grow]", "");
        JPanel jPanel2 = this.createTabPanel(migLayout2);
        JScrollPane jScrollPane2 = this.createTextAreaScroll("'gp' means Grow Priority. Higher values will be grown before lower ones and the default value is 100.\n\n'grow' means Grow Weight. Higher values relative to other's means they will grow more when space is up for takes. Grow Weight is only relative to components with the same Grow Priority. Default Grow Weight is 0 which means components will normally not grow. \n\nNote that the buttons in the first and last row have max width set to 170 to emphasize Grow Priority.\n\nThe component's maximum size will always be honored.", 0, 0, true);
        jScrollPane2.setOpaque(false);
        jScrollPane2.setBorder(new EmptyBorder(10, 10, 10, 10));
        ((JTextArea)jScrollPane2.getViewport().getView()).setOpaque(false);
        jScrollPane2.getViewport().setOpaque(false);
        JSplitPane jSplitPane2 = new JSplitPane(1, true, jPanel2, jScrollPane2);
        jSplitPane2.setOpaque(false);
        jSplitPane2.setBorder(null);
        jPanel2.add((Component)this.createButton("gp 110,grow"), "gp 110,grow,wmax 170");
        jPanel2.add((Component)this.createButton("Default (100),grow"), "grow,wmax 170");
        jPanel2.add((Component)this.createButton("gp 90,grow"), "gp 90,grow,wmax 170");
        jPanel2.add((Component)this.createButton("Default Button"), "newline");
        jPanel2.add((Component)this.createButton("growx"), "newline,growx,wrap");
        jPanel2.add((Component)this.createButton("gp 110,grow"), "gp 110,grow,wmax 170");
        jPanel2.add((Component)this.createButton("gp 100,grow 25"), "gp 100,grow 25,wmax 170");
        jPanel2.add((Component)this.createButton("gp 100,grow 75"), "gp 100,grow 75,wmax 170");
        jTabbedPane.addTab("Grow", jSplitPane2);
        this.setSource("JTabbedPane tabbedPane = new JTabbedPane();\n\n// shrink tab\nMigLayout slm = new MigLayout(\"nogrid\");\nJPanel sPanel = createTabPanel(slm);\n\nJScrollPane sDescrText = createTextAreaScroll(\"Use the slider to see how the components shrink depending on the constraints set on them.\\n\\n'shp' means Shrink Priority. \" +\n                                              \"Lower values will be shrunk before higer ones and the default value is 100.\\n\\n'shrink' means Shrink Weight. \" +\n                                              \"Lower values relative to other's means they will shrink less when space is scarse. \" +\n                                              \"Shrink Weight is only relative to components with the same Shrink Priority. Default Shrink Weight is 100.\\n\\n\" +\n                                              \"The component's minimum size will always be honored.\", 0, 0, true);\n\nsDescrText.setOpaque(OPAQUE);\nsDescrText.setBorder(new EmptyBorder(10, 10, 10, 10));\n((JTextArea) sDescrText.getViewport().getView()).setOpaque(OPAQUE);\nsDescrText.getViewport().setOpaque(OPAQUE);\n\nJSplitPane sSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, sPanel, sDescrText);\nsSplitPane.setOpaque(OPAQUE);\nsSplitPane.setBorder(null);\n\nsPanel.add(createTextField(\"shp 110\", 12), \"shp 110\");\nsPanel.add(createTextField(\"Default (100)\", 12), \"\");\nsPanel.add(createTextField(\"shp 90\", 12), \"shp 90\");\n\nsPanel.add(createTextField(\"shrink 25\", 20), \"newline,shrink 25\");\nsPanel.add(createTextField(\"shrink 75\", 20), \"shrink 75\");\n\nsPanel.add(createTextField(\"Default\", 20), \"newline\");\nsPanel.add(createTextField(\"Default\", 20), \"\");\n\nsPanel.add(createTextField(\"shrink 0\", 40), \"newline,shrink 0\");\n\nsPanel.add(createTextField(\"shp 110\", 12), \"newline,shp 110\");\nsPanel.add(createTextField(\"shp 100,shrink 25\", 12), \"shp 100,shrink 25\");\nsPanel.add(createTextField(\"shp 100,shrink 75\", 12), \"shp 100,shrink 75\");\ntabbedPane.addTab(\"Shrink\", sSplitPane);\n\n// Grow tab\nMigLayout glm = new MigLayout(\"nogrid\", \"[grow]\", \"\");\nJPanel gPanel = createTabPanel(glm);\n\nJScrollPane gDescrText = createTextAreaScroll(\"'gp' means Grow Priority. \" +\n                                              \"Higher values will be grown before lower ones and the default value is 100.\\n\\n'grow' means Grow Weight. \" +\n                                              \"Higher values relative to other's means they will grow more when space is up for takes. \" +\n                                              \"Grow Weight is only relative to components with the same Grow Priority. Default Grow Weight is 0 which means \" +\n                                              \"components will normally not grow. \\n\\nNote that the buttons in the first and last row have max width set to 170 to \" +\n                                              \"emphasize Grow Priority.\\n\\nThe component's maximum size will always be honored.\", 0, 0, true);\n\ngDescrText.setOpaque(OPAQUE);\ngDescrText.setBorder(new EmptyBorder(10, 10, 10, 10));\n((JTextArea) gDescrText.getViewport().getView()).setOpaque(OPAQUE);\ngDescrText.getViewport().setOpaque(OPAQUE);\n\nJSplitPane gSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, gPanel, gDescrText);\ngSplitPane.setOpaque(OPAQUE);\ngSplitPane.setBorder(null);\n\ngPanel.add(createButton(\"gp 110,grow\"), \"gp 110,grow,wmax 170\");\ngPanel.add(createButton(\"Default (100),grow\"), \"grow,wmax 170\");\ngPanel.add(createButton(\"gp 90,grow\"), \"gp 90,grow,wmax 170\");\n\ngPanel.add(createButton(\"Default Button\"), \"newline\");\n\ngPanel.add(createButton(\"growx\"), \"newline,growx,wrap\");\n\ngPanel.add(createButton(\"gp 110,grow\"), \"gp 110,grow,wmax 170\");\ngPanel.add(createButton(\"gp 100,grow 25\"), \"gp 100,grow 25,wmax 170\");\ngPanel.add(createButton(\"gp 100,grow 75\"), \"gp 100,grow 75,wmax 170\");\ntabbedPane.addTab(\"Grow\", gSplitPane);");
        return jTabbedPane;
    }

    public JComponent createPlainApi() {
        JTabbedPane jTabbedPane = new JTabbedPane();
        MigLayout migLayout = new MigLayout(new LC(), null, null);
        JPanel jPanel = this.createTabPanel(migLayout);
        this.addSeparator(jPanel, "Manufacturer");
        jPanel.add(this.createLabel("Company"));
        jPanel.add((Component)this.createTextField(""), "span,growx");
        jPanel.add(this.createLabel("Contact"));
        jPanel.add((Component)this.createTextField(""), "span,growx");
        jPanel.add(this.createLabel("Order No"));
        jPanel.add((Component)this.createTextField(15), "wrap");
        this.addSeparator(jPanel, "Inspector");
        jPanel.add(this.createLabel("Name"));
        jPanel.add((Component)this.createTextField(""), "span,growx");
        jPanel.add(this.createLabel("Reference No"));
        jPanel.add((Component)this.createTextField(""), "wrap");
        jPanel.add(this.createLabel("Status"));
        jPanel.add((Component)this.createCombo(new String[]{"In Progress", "Finnished", "Released"}), "wrap");
        this.addSeparator(jPanel, "Ship");
        jPanel.add(this.createLabel("Shipyard"));
        jPanel.add((Component)this.createTextField(""), "span,growx");
        jPanel.add(this.createLabel("Register No"));
        jPanel.add(this.createTextField(""));
        jPanel.add((Component)this.createLabel("Hull No"), "right");
        jPanel.add((Component)this.createTextField(15), "wrap");
        jPanel.add(this.createLabel("Project StructureType"));
        jPanel.add(this.createCombo(new String[]{"New Building", "Convention", "Repair"}));
        jTabbedPane.addTab("Plain", jPanel);
        return jTabbedPane;
    }

    private JLabel createLabel(String string) {
        return this.createLabel(string, 10);
    }

    private JLabel createLabel(String string, int n) {
        JLabel jLabel = new JLabel(string, n);
        this.configureActiveComponet(jLabel);
        return jLabel;
    }

    public JComboBox createCombo(String[] stringArray) {
        JComboBox<String> jComboBox = new JComboBox<String>(stringArray);
        if (PlatformDefaults.getCurrentPlatform() == 1) {
            jComboBox.setOpaque(false);
        }
        return jComboBox;
    }

    private JTextField createTextField(int n) {
        return this.createTextField("", n);
    }

    private JTextField createTextField(String string) {
        return this.createTextField(string, 0);
    }

    private JTextField createTextField(String string, int n) {
        JTextField jTextField = new JTextField(string, n);
        this.configureActiveComponet(jTextField);
        return jTextField;
    }

    private JButton createButton() {
        return this.createButton("");
    }

    private JButton createButton(String string) {
        return this.createButton(string, false);
    }

    private JButton createButton(String string, boolean bl) {
        JButton jButton = new JButton(string){

            public void addNotify() {
                super.addNotify();
                if (benchRuns == 0) {
                    if (this.getText().length() == 0) {
                        String string = (String)((MigLayout)this.getParent().getLayout()).getComponentConstraints(this);
                        this.setText(string != null && string.length() > 0 ? string : "<Empty>");
                    }
                } else {
                    this.setText("Benchmark Version");
                }
            }
        };
        if (bl) {
            jButton.setFont(jButton.getFont().deriveFont(1));
        }
        this.configureActiveComponet(jButton);
        jButton.setOpaque(buttonOpaque);
        jButton.setContentAreaFilled(contentAreaFilled);
        return jButton;
    }

    private JToggleButton createToggleButton(String string) {
        JToggleButton jToggleButton = new JToggleButton(string);
        jToggleButton.setOpaque(buttonOpaque);
        return jToggleButton;
    }

    private JCheckBox createCheck(String string) {
        JCheckBox jCheckBox = new JCheckBox(string);
        this.configureActiveComponet(jCheckBox);
        jCheckBox.setOpaque(false);
        return jCheckBox;
    }

    private JPanel createTabPanel(LayoutManager layoutManager) {
        JPanel jPanel = new JPanel(layoutManager);
        this.configureActiveComponet(jPanel);
        jPanel.setOpaque(false);
        return jPanel;
    }

    private JComponent createPanel() {
        return this.createPanel("");
    }

    private JComponent createPanel(String string) {
        JLabel jLabel = new JLabel(string, 0){

            public void addNotify() {
                super.addNotify();
                if (benchRuns == 0 && this.getText().length() == 0) {
                    String string = (String)((MigLayout)this.getParent().getLayout()).getComponentConstraints(this);
                    this.setText(string != null && string.length() > 0 ? string : "<Empty>");
                }
            }
        };
        jLabel.setBorder(new EtchedBorder());
        jLabel.setOpaque(true);
        this.configureActiveComponet(jLabel);
        return jLabel;
    }

    private JTextArea createTextArea(String string, int n, int n2) {
        JTextArea jTextArea = new JTextArea(string, n, n2);
        jTextArea.setBorder(UIManager.getBorder("TextField.border"));
        jTextArea.setFont(UIManager.getFont("TextField.font"));
        jTextArea.setWrapStyleWord(true);
        jTextArea.setLineWrap(true);
        this.configureActiveComponet(jTextArea);
        return jTextArea;
    }

    private JScrollPane createTextAreaScroll(String string, int n, int n2, boolean bl) {
        JTextArea jTextArea = new JTextArea(string, n, n2);
        jTextArea.setFont(UIManager.getFont("TextField.font"));
        jTextArea.setWrapStyleWord(true);
        jTextArea.setLineWrap(true);
        JScrollPane jScrollPane = new JScrollPane(jTextArea, bl ? 20 : 21, 31);
        return jScrollPane;
    }

    private JComponent configureActiveComponet(JComponent jComponent) {
        if (benchRuns == 0) {
            jComponent.addMouseMotionListener(this.toolTipListener);
            jComponent.addMouseListener(this.constraintListener);
        }
        return jComponent;
    }

    private void addSeparator(JPanel jPanel, String string) {
        JLabel jLabel = this.createLabel(string);
        jLabel.setForeground(LABEL_COLOR);
        jPanel.add((Component)jLabel, "gapbottom 1, span, split 2, aligny center");
        jPanel.add((Component)this.configureActiveComponet(new JSeparator()), "gapleft rel, growx");
    }

    private static class ConstraintsDialog
    extends JDialog
    implements ActionListener,
    KeyEventDispatcher {
        private static final Color ERROR_COLOR = new Color(255, 180, 180);
        private final JPanel mainPanel = new JPanel(new MigLayout("fillx,flowy,ins dialog", "[fill]", "2[]2"));
        final JTextField layoutConstrTF;
        final JTextField rowsConstrTF;
        final JTextField colsConstrTF;
        final JTextField componentConstrTF;
        private final JButton okButt = new JButton("OK");
        private final JButton cancelButt = new JButton("Cancel");
        private boolean okPressed = false;

        public ConstraintsDialog(Frame frame, String string, String string2, String string3, String string4) {
            super(frame, string4 != null ? "Edit Component Constraints" : "Edit Container Constraints", true);
            this.layoutConstrTF = this.createConstraintField(string);
            this.rowsConstrTF = this.createConstraintField(string2);
            this.colsConstrTF = this.createConstraintField(string3);
            this.componentConstrTF = this.createConstraintField(string4);
            if (this.componentConstrTF != null) {
                this.mainPanel.add(new JLabel("Component Constraints"));
                this.mainPanel.add(this.componentConstrTF);
            }
            if (this.layoutConstrTF != null) {
                this.mainPanel.add(new JLabel("Layout Constraints"));
                this.mainPanel.add(this.layoutConstrTF);
            }
            if (this.colsConstrTF != null) {
                this.mainPanel.add((Component)new JLabel("Column Constraints"), "gaptop unrel");
                this.mainPanel.add(this.colsConstrTF);
            }
            if (this.rowsConstrTF != null) {
                this.mainPanel.add((Component)new JLabel("Row Constraints"), "gaptop unrel");
                this.mainPanel.add(this.rowsConstrTF);
            }
            this.mainPanel.add((Component)this.okButt, "tag ok,split,flowx,gaptop 15");
            this.mainPanel.add((Component)this.cancelButt, "tag cancel,gaptop 15");
            this.setContentPane(this.mainPanel);
            this.okButt.addActionListener(this);
            this.cancelButt.addActionListener(this);
        }

        public void addNotify() {
            super.addNotify();
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        }

        public void removeNotify() {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
            super.removeNotify();
        }

        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == 27) {
                this.dispose();
            }
            return false;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (actionEvent.getSource() == this.okButt) {
                this.okPressed = true;
            }
            this.dispose();
        }

        private JTextField createConstraintField(String string) {
            if (string == null) {
                return null;
            }
            final JTextField jTextField = new JTextField(string, 50);
            jTextField.setFont(new Font("monospaced", 0, 12));
            jTextField.addKeyListener(new KeyAdapter(){

                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == 10) {
                        ConstraintsDialog.this.okButt.doClick();
                        return;
                    }
                    Timer timer = new Timer(50, new ActionListener(){

                        public void actionPerformed(ActionEvent actionEvent) {
                            String string = jTextField.getText();
                            try {
                                if (jTextField == ConstraintsDialog.this.layoutConstrTF) {
                                    ConstraintParser.parseLayoutConstraint(string);
                                } else if (jTextField == ConstraintsDialog.this.rowsConstrTF) {
                                    ConstraintParser.parseRowConstraints(string);
                                } else if (jTextField == ConstraintsDialog.this.colsConstrTF) {
                                    ConstraintParser.parseColumnConstraints(string);
                                } else if (jTextField == ConstraintsDialog.this.componentConstrTF) {
                                    ConstraintParser.parseComponentConstraint(string);
                                }
                                jTextField.setBackground(Color.WHITE);
                                ConstraintsDialog.this.okButt.setEnabled(true);
                            }
                            catch (Exception exception) {
                                jTextField.setBackground(ERROR_COLOR);
                                ConstraintsDialog.this.okButt.setEnabled(false);
                            }
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            });
            return jTextField;
        }

        private boolean showDialog() {
            this.setVisible(true);
            return this.okPressed;
        }
    }

    private static class ToolTipListener
    extends MouseMotionAdapter {
        private ToolTipListener() {
        }

        public void mouseMoved(MouseEvent mouseEvent) {
            Object object;
            JComponent jComponent = (JComponent)mouseEvent.getSource();
            LayoutManager layoutManager = jComponent.getParent().getLayout();
            if (layoutManager instanceof MigLayout && (object = ((MigLayout)layoutManager).getComponentConstraints(jComponent)) instanceof String) {
                jComponent.setToolTipText(object != null ? "\"" + object + "\"" : "null");
            }
        }
    }

    private class ConstraintListener
    extends MouseAdapter {
        private ConstraintListener() {
        }

        public void mousePressed(MouseEvent mouseEvent) {
            if (mouseEvent.isPopupTrigger()) {
                this.react(mouseEvent);
            }
        }

        public void mouseReleased(MouseEvent mouseEvent) {
            if (mouseEvent.isPopupTrigger()) {
                this.react(mouseEvent);
            }
        }

        public void react(MouseEvent mouseEvent) {
            JComponent jComponent = (JComponent)mouseEvent.getSource();
            LayoutManager layoutManager = jComponent.getParent().getLayout();
            if (!(layoutManager instanceof MigLayout)) {
                layoutManager = jComponent.getLayout();
            }
            if (layoutManager instanceof MigLayout) {
                Object object;
                MigLayout migLayout = (MigLayout)layoutManager;
                boolean bl = migLayout.isManagingComponent(jComponent);
                Object object2 = object = bl ? migLayout.getComponentConstraints(jComponent) : null;
                if (bl && object == null) {
                    object = "";
                }
                Object object3 = bl ? null : migLayout.getRowConstraints();
                Object object4 = bl ? null : migLayout.getColumnConstraints();
                Object object5 = bl ? null : migLayout.getLayoutConstraints();
                ConstraintsDialog constraintsDialog = new ConstraintsDialog(SwingDemo.this, object5 instanceof LC ? IDEUtil.getConstraintString((LC)object5, false) : (String)object5, object3 instanceof AC ? IDEUtil.getConstraintString((AC)object3, false, false) : (String)object3, object4 instanceof AC ? IDEUtil.getConstraintString((AC)object4, false, false) : (String)object4, object instanceof CC ? IDEUtil.getConstraintString((CC)object, false) : (String)object);
                constraintsDialog.pack();
                constraintsDialog.setLocationRelativeTo(jComponent);
                if (constraintsDialog.showDialog()) {
                    try {
                        if (bl) {
                            String string = constraintsDialog.componentConstrTF.getText().trim();
                            migLayout.setComponentConstraints(jComponent, string);
                            if (jComponent instanceof JButton) {
                                jComponent.setFont(BUTT_FONT);
                                ((JButton)jComponent).setText(string.length() == 0 ? "<Empty>" : string);
                            }
                        } else {
                            migLayout.setLayoutConstraints(constraintsDialog.layoutConstrTF.getText());
                            migLayout.setRowConstraints(constraintsDialog.rowsConstrTF.getText());
                            migLayout.setColumnConstraints(constraintsDialog.colsConstrTF.getText());
                        }
                    }
                    catch (Exception exception) {
                        StringWriter stringWriter = new StringWriter();
                        exception.printStackTrace(new PrintWriter(stringWriter));
                        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(jComponent), stringWriter.toString(), "Error parsing Constraint!", 0);
                        return;
                    }
                    jComponent.invalidate();
                    jComponent.getParent().validate();
                }
            }
        }
    }
}

