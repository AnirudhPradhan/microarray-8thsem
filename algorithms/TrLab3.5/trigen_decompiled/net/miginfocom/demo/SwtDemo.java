/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.custom.SashForm
 *  org.eclipse.swt.custom.StyledText
 *  org.eclipse.swt.events.ControlAdapter
 *  org.eclipse.swt.events.ControlEvent
 *  org.eclipse.swt.events.ControlListener
 *  org.eclipse.swt.events.SelectionAdapter
 *  org.eclipse.swt.events.SelectionEvent
 *  org.eclipse.swt.events.SelectionListener
 *  org.eclipse.swt.graphics.Color
 *  org.eclipse.swt.graphics.Device
 *  org.eclipse.swt.graphics.Font
 *  org.eclipse.swt.graphics.FontData
 *  org.eclipse.swt.layout.FillLayout
 *  org.eclipse.swt.widgets.Button
 *  org.eclipse.swt.widgets.Combo
 *  org.eclipse.swt.widgets.Composite
 *  org.eclipse.swt.widgets.Control
 *  org.eclipse.swt.widgets.Display
 *  org.eclipse.swt.widgets.Event
 *  org.eclipse.swt.widgets.Label
 *  org.eclipse.swt.widgets.Layout
 *  org.eclipse.swt.widgets.List
 *  org.eclipse.swt.widgets.Listener
 *  org.eclipse.swt.widgets.MessageBox
 *  org.eclipse.swt.widgets.Shell
 *  org.eclipse.swt.widgets.TabFolder
 *  org.eclipse.swt.widgets.TabItem
 *  org.eclipse.swt.widgets.Table
 *  org.eclipse.swt.widgets.TableColumn
 *  org.eclipse.swt.widgets.TableItem
 *  org.eclipse.swt.widgets.Text
 */
package net.miginfocom.demo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swt.MigLayout;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class SwtDemo {
    public static final int SELECTED_INDEX = 0;
    private static final String[][] panels = new String[][]{{"Welcome", "\n\n         \"MigLayout makes complex layouts easy and normal layouts one-liners.\""}, {"Quick Start", "This is an example of how to build a common dialog type. Note that there are no special components, nested panels or absolute references to cell positions. If you look at the source code you will see that the layout code is very simple to understand."}, {"Plain", "A simple example on how simple it is to create normal forms. No builders needed since the whole layout manager works like a builder."}, {"Alignments", "Shows how the alignment of components are specified. At the top/left is the alignment for the column/row. The components have no alignments specified.\n\nNote that baseline alignment will be interpreted as 'center' before JDK 6."}, {"Cell Alignments", "Shows how components are aligned when both column/row alignments and component constraints are specified. At the top/left are the alignment for the column/row and the text on the buttons is the component constraint that will override the column/row alignment if it is an alignment.\n\nNote that baseline alignment will be interpreted as 'center' before JDK 6."}, {"Basic Sizes", "A simple example that shows how to use the column or row min/preferred/max size to set the sizes of the contained components and also an example that shows how to do this directly in the component constraints."}, {"Growing", "A simple example that shows how to use the growx and growy constraint to set the sizes and how they should grow to fit the available size. Both the column/row and the component grow/shrink constraints can be set, but the components will always be confined to the space given by its column/row."}, {"Grow Shrink", "Demonstrates the very flexible grow and shrink constraints that can be set on a component.\nComponents can be divided into grow/shrink groups and also have grow/shrink weight within each of those groups.\n\nBy default components shrink to their inherent (or specified) minimum size, but they don't grow."}, {"Span", "This example shows the powerful spanning and splitting that can be specified in the component constraints. With spanning any number of cells can be merged with the additional option to split that space for more than one component. This makes layouts very flexible and reduces the number of times you will need nested panels to very few."}, {"Flow Direction", "Shows the different flow directions. Flow direction for the layout specifies if the next cell will be in the x or y dimension. Note that it can be a different flow direction in the slit cell (the middle cell is slit in two). Wrap is set to 3 for all panels."}, {"Grouping", "Sizes for both components and columns/rows can be grouped so they get the same size. For instance buttons in a button bar can be given a size-group so that they will all get the same minimum and preferred size (the largest within the group). Size-groups can be set for the width, height or both."}, {"Units", "Demonstrates the basic units that are understood by MigLayout. These units can be extended by the user by adding one or more UnitConverter(s)."}, {"Component Sizes", "Minimum, preferred and maximum component sizes can be overridden in the component constraints using any unit type. The format to do this is short and simple to understand. You simply specify the min, preferred and max sizes with a colon between.\n\nAbove are some examples of this. An exclamation mark means that the value will be used for all sizes."}, {"Bound Sizes", "Shows how to create columns that are stable between tabs using minimum sizes."}, {"Cell Position", "Even though MigLayout has automatic grid flow you can still specify the cell position explicitly. You can even combine absolute (x, y) and flow (skip, wrap and newline) constraints to build your layout."}, {"Orientation", "MigLayout supports not only right-to-left orientation, but also bottom-to-top. You can even set the flow direction so that the flow is vertical instead of horizontal. It will automatically pick up if right-to-left is to be used depending on the ComponentWrapper, but it can also be manually set for every layout."}, {"Absolute Position", "Demonstrates the option to place any number of components using absolute coordinates. This can be just the position (if min/preferred size) using \"x y p p\" format orthe bounds using the \"x1 y1 x2 y2\" format. Any unit can be used and percent is relative to the parent.\nAbsolute components will not disturb the flow or occupy cells in the grid. Absolute positioned components will be taken into account when calculating the container's preferred size."}, {"Component Links", "Components can be linked to any side of any other component. It can be a forward, backward or cyclic link references, as long as it is stable and won't continue to change value over many iterations.Links are referencing the ID of another component. The ID can be overridden by the component's constrains or is provided by the ComponentWrapper. For instance it will use the component's 'name' on Swing.\nSince the links can be combined with any expression (such as 'butt1.x+10' or 'max(button.x, 200)' the links are very customizable."}, {"Docking", "Docking components can be added around the grid. The docked component will get the whole width/height on the docked side by default, however this can be overridden. When all docked components are laid out, whatever space is left will be available for the normal grid laid out components. Docked components does not in any way affect the flow in the grid.\n\nSince the docking runs in the same code path as the normal layout code the same properties can be specified for the docking components. You can for instance set the sizes and alignment or link other components to their docked component's bounds."}, {"Button Bars", "Button order is very customizable and are by default different on the supported platforms. E.g. Gaps, button order and minimum button size are properties that are 'per platform'. MigLayout picks up the current platform automatically and adjusts the button order and minimum button size accordingly, all without using a button builder or any other special code construct."}, {"Debug", "Demonstrates the non-intrusive way to get visual debugging aid. There is no need to use a special DebugPanel or anything that will need code changes. The user can simply turn on debug on the layout manager by using the \u00ecdebug\u00ee constraint and it will continuously repaint the panel with debug information on top. This means you don't have to change your code to debug!"}, {"Layout Showdown", "This is an implementation of the Layout Showdown posted on java.net by John O'Conner. The first tab is a pure implemenetation of the showdown that follows all the rules. The second tab is a slightly fixed version that follows some improved layout guidelines.The source code is for bothe the first and for the fixed version. Note the simplification of the code for the fixed version. Writing better layouts with MiG Layout is reasier that writing bad.\n\nReference: http://weblogs.java.net/blog/joconner/archive/2006/10/more_informatio.html"}, {"API Constraints1", "This dialog shows the constraint API added to v2.0. It works the same way as the string constraints but with chained method calls. See the source code for details."}, {"API Constraints2", "This dialog shows the constraint API added to v2.0. It works the same way as the string constraints but with chained method calls. See the source code for details."}};
    private static int DOUBLE_BUFFER = 0;
    private static int benchRuns = 0;
    private static long startupMillis = 0L;
    private static long timeToShowMillis = 0L;
    private static long benchRunTime = 0L;
    private static String benchOutFileName = null;
    private static boolean append = false;
    private static long lastRunTimeStart = 0L;
    private static StringBuffer runTimeSB = null;
    private static Display display = null;
    final List pickerList;
    final Composite layoutDisplayPanel;
    final StyledText descrTextArea;
    private static Control[] comps = null;
    private static Control[] tabs = null;

    public static void main(String[] stringArray) {
        startupMillis = System.currentTimeMillis();
        if (stringArray.length > 0) {
            for (int i = 0; i < stringArray.length; ++i) {
                String string = stringArray[i].trim();
                if (string.startsWith("-bench")) {
                    benchRuns = 10;
                    try {
                        if (string.length() <= 6) continue;
                        benchRuns = Integer.parseInt(string.substring(6));
                    }
                    catch (Exception exception) {}
                    continue;
                }
                if (string.startsWith("-bout")) {
                    benchOutFileName = string.substring(5);
                    continue;
                }
                if (string.startsWith("-append")) {
                    append = true;
                    continue;
                }
                if (string.startsWith("-verbose")) {
                    runTimeSB = new StringBuffer(256);
                    continue;
                }
                System.out.println("Usage: [-bench[#_of_runs]] [-bout[benchmark_results_filename]] [-append]\n -bench Run demo as benchmark. Run count can be appended. 10 is default.\n -bout  Benchmark results output filename.\n -append Appends the result to the \"-bout\" file.\n -verbose Print the times of every run.\n\nExamples:\n java -jar swtdemoapp.jar -bench -boutC:/bench.txt -append\n java -jar swtdemoapp.jar -bench20\nNOTE! swt-win32-3232.dll must be in the current directory!");
                System.exit(0);
            }
        }
        if (benchRuns == 0) {
            LayoutUtil.setDesignTime(null, true);
        }
        new SwtDemo();
    }

    public SwtDemo() {
        display = new Display();
        Shell shell = new Shell();
        shell.setLayout((Layout)new MigLayout("wrap", "[]u[grow,fill]", "[grow,fill][pref!]"));
        shell.setText("MigLayout SWT Demo v2.5 - Mig Layout v" + LayoutUtil.getVersion());
        TabFolder tabFolder = new TabFolder((Composite)shell, DOUBLE_BUFFER);
        tabFolder.setLayoutData((Object)"spany,grow");
        this.pickerList = new List((Composite)tabFolder, 4 | DOUBLE_BUFFER);
        this.pickerList.setBackground(tabFolder.getBackground());
        this.deriveFont((Control)this.pickerList, 1, -1);
        TabItem tabItem = new TabItem(tabFolder, DOUBLE_BUFFER);
        tabItem.setControl((Control)this.pickerList);
        tabItem.setText("Example Browser");
        for (int i = 0; i < panels.length; ++i) {
            this.pickerList.add(panels[i][0]);
        }
        this.layoutDisplayPanel = new Composite((Composite)shell, DOUBLE_BUFFER);
        this.layoutDisplayPanel.setLayout((Layout)new MigLayout("fill, insets 0"));
        TabFolder tabFolder2 = new TabFolder((Composite)shell, DOUBLE_BUFFER);
        tabFolder2.setLayoutData((Object)"growx,hmin 120,w 500:500");
        this.descrTextArea = this.createTextArea(tabFolder2, "", "", 66);
        this.descrTextArea.setBackground(tabFolder2.getBackground());
        tabItem = new TabItem(tabFolder2, DOUBLE_BUFFER);
        tabItem.setControl((Control)this.descrTextArea);
        tabItem.setText("Description");
        this.pickerList.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent selectionEvent) {
                SwtDemo.this.dispatchSelection();
            }
        });
        shell.setSize(900, 650);
        shell.open();
        shell.layout();
        if (benchRuns > 0) {
            this.doBenchmark();
        } else {
            this.pickerList.select(0);
            this.dispatchSelection();
            display.addFilter(1, new Listener(){

                public void handleEvent(Event event) {
                    if (event.character == 'b') {
                        startupMillis = System.currentTimeMillis();
                        timeToShowMillis = System.currentTimeMillis() - startupMillis;
                        benchRuns = 1;
                        SwtDemo.this.doBenchmark();
                    }
                }
            });
        }
        System.out.println(Display.getCurrent().getDPI());
        while (!shell.isDisposed()) {
            if (display.readAndDispatch()) continue;
            display.sleep();
        }
        display.dispose();
    }

    private void doBenchmark() {
        final int n = this.pickerList.getItemCount();
        Thread thread = new Thread(){

            public void run() {
                for (int i = 0; i < benchRuns; ++i) {
                    lastRunTimeStart = System.currentTimeMillis();
                    int n4 = i;
                    for (int j = 0; j < n; ++j) {
                        final int n2 = j;
                        try {
                            display.syncExec(new Runnable(){

                                public void run() {
                                    SwtDemo.this.pickerList.setSelection(n2);
                                    SwtDemo.this.dispatchSelection();
                                }
                            });
                        }
                        catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        display.syncExec(new Runnable(){

                            public void run() {
                                SwtDemo.access$702(SwtDemo.this.layoutDisplayPanel.getChildren());
                            }
                        });
                        for (int k = 0; k < comps.length; ++k) {
                            if (!(comps[k] instanceof TabFolder)) continue;
                            final TabFolder tabFolder = (TabFolder)comps[k];
                            display.syncExec(new Runnable(){

                                public void run() {
                                    SwtDemo.access$802(tabFolder.getTabList());
                                }
                            });
                            for (int i2 = 0; i2 < tabs.length; ++i2) {
                                final int n3 = i2;
                                try {
                                    display.syncExec(new Runnable(){

                                        public void run() {
                                            tabFolder.setSelection(n3);
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
                    runTimeSB.append("Run ").append(n4).append(": ");
                    runTimeSB.append(System.currentTimeMillis() - lastRunTimeStart).append(" millis.\n");
                }
                benchRunTime = System.currentTimeMillis() - startupMillis - timeToShowMillis;
                final String string = "Java Version:       " + System.getProperty("java.version") + "\n" + "Time to Show:       " + timeToShowMillis + " millis.\n" + (runTimeSB != null ? runTimeSB.toString() : "") + "Benchmark Run Time: " + benchRunTime + " millis.\n" + "Average Run Time:   " + benchRunTime / (long)benchRuns + " millis (" + benchRuns + " runs).\n\n";
                display.syncExec(new Runnable(){

                    /*
                     * WARNING - Removed try catching itself - possible behaviour change.
                     */
                    public void run() {
                        if (benchOutFileName == null) {
                            MessageBox messageBox = new MessageBox(display.getActiveShell(), 34);
                            messageBox.setText("Results");
                            messageBox.setMessage(string);
                            messageBox.open();
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
                    }
                });
                System.out.println(string);
                if (benchOutFileName != null) {
                    System.exit(0);
                }
            }
        };
        thread.start();
    }

    private void dispatchSelection() {
        int n = this.pickerList.getSelectionIndex();
        if (n == -1) {
            return;
        }
        String string = "create" + panels[n][0].replace(' ', '_');
        Control[] controlArray = this.layoutDisplayPanel.getChildren();
        for (int i = 0; i < controlArray.length; ++i) {
            controlArray[i].dispose();
        }
        try {
            Control control = (Control)SwtDemo.class.getMethod(string, Composite.class).invoke((Object)this, this.layoutDisplayPanel);
            control.setLayoutData((Object)"grow, wmin 500");
            this.descrTextArea.setText(panels[n][1]);
            this.layoutDisplayPanel.layout();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Control createTest(Composite composite) {
        Composite composite2 = new Composite(composite, 0);
        composite2.setLayout((Layout)new MigLayout("debug", "[right][grow]", ""));
        Button button = new Button(composite2, 8);
        button.setText("New");
        button.setLayoutData((Object)"span 2, align left, split, sgx button");
        button = new Button(composite2, 8);
        button.setText("Edit");
        button.setLayoutData((Object)"sgx button");
        button = new Button(composite2, 8);
        button.setText("Cancel");
        button.setLayoutData((Object)"sgx button");
        button = new Button(composite2, 8);
        button.setText("Save");
        button.setLayoutData((Object)"sgx button, wrap");
        new Label(composite2, 0).setText("Name");
        Text text = new Text(composite2, 2048);
        text.setLayoutData((Object)"sgy control, pushx, growx, wrap");
        new Label(composite2, 0).setText("Sex");
        Combo combo = new Combo(composite2, 4);
        combo.setLayoutData((Object)"sgy control, w 50!, wrap");
        combo.setItems(new String[]{"M", "F", "-"});
        return composite2;
    }

    public Control createWelcome(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        TabItem tabItem = this.createTabPanel(tabFolder, "Welcome", new MigLayout());
        MigLayout migLayout = new MigLayout("ins 20, fill");
        Composite composite2 = this.createPanel(tabFolder, migLayout);
        tabItem.setControl((Control)composite2);
        String string = "MigLayout's main purpose is to make layouts for SWT and Swing, and possibly other frameworks, much more powerful and a lot easier to create, especially for manual coding.\n\nThe motto is: \"MigLayout makes complex layouts easy and normal layouts one-liners.\"\n\nThe layout engine is very flexible and advanced, something that is needed to make it simple to use yet handle almost all layout use-cases.\n\nMigLayout can handle all layouts that the commonly used Swing Layout Managers can handle and this with a lot of extra features. It also incorporates most, if not all, of the open source alternatives FormLayout's and TableLayout's functionality.\n\n\nThanks to Karsten Lentzsch from JGoodies.com for allowing the reuse of the main demo application layout and for his inspiring talks that led to this layout Manager.\n\n\nMikael Grev\nMiG InfoCom AB\nmiglayout@miginfocom.com";
        StyledText styledText = this.createTextArea(composite2, string, "w 500:500, ay top, grow, push", 0);
        styledText.setBackground(composite2.getBackground());
        styledText.setBackgroundMode(0);
        return tabFolder;
    }

    public Composite createAPI_Constraints1(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        LC lC = new LC().fill().wrap();
        AC aC = new AC().align("right", 0).fill(1, 3).grow(100.0f, 1, 3).align("right", 2).gap("15", 1);
        AC aC2 = new AC().align("top", 7).gap("15!", 6).grow(100.0f, 8);
        TabItem tabItem = this.createTabPanel(tabFolder, "Layout Showdown (improved)", new MigLayout(lC, aC, aC2));
        this.createList(tabItem, "Mouse, Mickey", new CC().dockWest().minWidth("150").gapX(null, "10"));
        this.createLabel(tabItem, "Last Name", "");
        this.createTextField(tabItem, "", "");
        this.createLabel(tabItem, "First Name", "");
        this.createTextField(tabItem, "", new CC().wrap());
        this.createLabel(tabItem, "Phone", "");
        this.createTextField(tabItem, "", "");
        this.createLabel(tabItem, "Email", "");
        this.createTextField(tabItem, "", "");
        this.createLabel(tabItem, "Address 1", "");
        this.createTextField(tabItem, "", new CC().spanX().growX());
        this.createLabel(tabItem, "Address 2", "");
        this.createTextField(tabItem, "", new CC().spanX().growX());
        this.createLabel(tabItem, "City", "");
        this.createTextField(tabItem, "", new CC().wrap());
        this.createLabel(tabItem, "State", "");
        this.createTextField(tabItem, "", "");
        this.createLabel(tabItem, "Postal Code", "");
        this.createTextField(tabItem, "", new CC().spanX(2).growX(0.0f));
        this.createLabel(tabItem, "Country", "");
        this.createTextField(tabItem, "", new CC().wrap());
        this.createButton(tabItem, "New", new CC().spanX(5).split(5).tag("other"));
        this.createButton(tabItem, "Delete", new CC().tag("other"));
        this.createButton(tabItem, "Edit", new CC().tag("other"));
        this.createButton(tabItem, "Save", new CC().tag("other"));
        this.createButton(tabItem, "Cancel", new CC().tag("cancel"));
        return tabFolder;
    }

    public Composite createAPI_Constraints2(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        LC lC = new LC().fill().wrap();
        AC aC = new AC().align("right", 0).fill(1, 3).grow(100.0f, 1, 3).align("right", 2).gap("15", 1);
        AC aC2 = new AC().index(6).gap("15!").align("top").grow(100.0f, 8);
        TabItem tabItem = this.createTabPanel(tabFolder, "Layout Showdown (improved)", new MigLayout(lC, aC, aC2));
        this.createLabel(tabItem, "Last Name", "");
        this.createTextField(tabItem, "", "");
        this.createLabel(tabItem, "First Name", "");
        this.createTextField(tabItem, "", new CC().wrap());
        this.createLabel(tabItem, "Phone", "");
        this.createTextField(tabItem, "", "");
        this.createLabel(tabItem, "Email", "");
        this.createTextField(tabItem, "", "");
        this.createLabel(tabItem, "Address 1", "");
        this.createTextField(tabItem, "", new CC().spanX().growX());
        this.createLabel(tabItem, "Address 2", "");
        this.createTextField(tabItem, "", new CC().spanX().growX());
        this.createLabel(tabItem, "City", "");
        this.createTextField(tabItem, "", new CC().wrap());
        this.createLabel(tabItem, "State", "");
        this.createTextField(tabItem, "", "");
        this.createLabel(tabItem, "Postal Code", "");
        this.createTextField(tabItem, "", new CC().spanX(2).growX(0.0f));
        this.createLabel(tabItem, "Country", "");
        this.createTextField(tabItem, "", new CC().wrap());
        this.createButton(tabItem, "New", new CC().spanX(5).split(5).tag("other"));
        this.createButton(tabItem, "Delete", new CC().tag("other"));
        this.createButton(tabItem, "Edit", new CC().tag("other"));
        this.createButton(tabItem, "Save", new CC().tag("other"));
        this.createButton(tabItem, "Cancel", new CC().tag("cancel"));
        return tabFolder;
    }

    public Composite createLayout_Showdown(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        TabItem tabItem = this.createTabPanel(tabFolder, "Layout Showdown (pure)", new MigLayout("", "[]15[][grow,fill]15[grow]"));
        this.createList(tabItem, "Mouse, Mickey", "spany, growy, wmin 150");
        this.createLabel(tabItem, "Last Name", "");
        this.createTextField(tabItem, "", "");
        this.createLabel(tabItem, "First Name", "split");
        this.createTextField(tabItem, "", "growx, wrap");
        this.createLabel(tabItem, "Phone", "");
        this.createTextField(tabItem, "", "");
        this.createLabel(tabItem, "Email", "split");
        this.createTextField(tabItem, "", "growx, wrap");
        this.createLabel(tabItem, "Address 1", "");
        this.createTextField(tabItem, "", "span, growx");
        this.createLabel(tabItem, "Address 2", "");
        this.createTextField(tabItem, "", "span, growx");
        this.createLabel(tabItem, "City", "");
        this.createTextField(tabItem, "", "wrap");
        this.createLabel(tabItem, "State", "");
        this.createTextField(tabItem, "", "");
        this.createLabel(tabItem, "Postal Code", "split");
        this.createTextField(tabItem, "", "growx, wrap");
        this.createLabel(tabItem, "Country", "");
        this.createTextField(tabItem, "", "wrap 15");
        this.createButton(tabItem, "New", "span, split, align right");
        this.createButton(tabItem, "Delete", "");
        this.createButton(tabItem, "Edit", "");
        this.createButton(tabItem, "Save", "");
        this.createButton(tabItem, "Cancel", "wrap push");
        TabItem tabItem2 = this.createTabPanel(tabFolder, "Layout Showdown (improved)", new MigLayout("", "[]15[][grow,fill]15[][grow,fill]"));
        this.createList(tabItem2, "Mouse, Mickey", "spany, growy, wmin 150");
        this.createLabel(tabItem2, "Last Name", "");
        this.createTextField(tabItem2, "", "");
        this.createLabel(tabItem2, "First Name", "");
        this.createTextField(tabItem2, "", "wrap");
        this.createLabel(tabItem2, "Phone", "");
        this.createTextField(tabItem2, "", "");
        this.createLabel(tabItem2, "Email", "");
        this.createTextField(tabItem2, "", "wrap");
        this.createLabel(tabItem2, "Address 1", "");
        this.createTextField(tabItem2, "", "span");
        this.createLabel(tabItem2, "Address 2", "");
        this.createTextField(tabItem2, "", "span");
        this.createLabel(tabItem2, "City", "");
        this.createTextField(tabItem2, "", "wrap");
        this.createLabel(tabItem2, "State", "");
        this.createTextField(tabItem2, "", "");
        this.createLabel(tabItem2, "Postal Code", "");
        this.createTextField(tabItem2, "", "width 50, grow 0, wrap");
        this.createLabel(tabItem2, "Country", "");
        this.createTextField(tabItem2, "", "wrap 15");
        this.createButton(tabItem2, "New", "tag other, span, split");
        this.createButton(tabItem2, "Delete", "tag other");
        this.createButton(tabItem2, "Edit", "tag other");
        this.createButton(tabItem2, "Save", "tag other");
        this.createButton(tabItem2, "Cancel", "tag cancel, wrap push");
        return tabFolder;
    }

    public Composite createDocking(Composite composite) {
        TabItem tabItem;
        TableColumn tableColumn;
        int n;
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        tabFolder.setLayoutData((Object)"grow");
        TabItem tabItem2 = this.createTabPanel(tabFolder, "Docking 1", new MigLayout("fill"));
        this.createPanel((Object)tabItem2, "1. North", "north");
        this.createPanel((Object)tabItem2, "2. West", "west");
        this.createPanel((Object)tabItem2, "3. East", "east");
        this.createPanel((Object)tabItem2, "4. South", "south");
        Table table = new Table(this.getComposite(tabItem2), DOUBLE_BUFFER);
        for (n = 0; n < 5; ++n) {
            tableColumn = new TableColumn(table, 16897);
            tableColumn.setText("Column " + (n + 1));
            tableColumn.setWidth(100);
        }
        for (n = 0; n < 15; ++n) {
            tableColumn = new TableItem(table, 0);
            tabItem = new String[6];
            for (int i = 0; i < ((String[])tabItem).length; ++i) {
                tabItem[i] = "Cell " + (n + 1) + ", " + (i + 1);
            }
            tableColumn.setText((String[])tabItem);
        }
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setLayoutData((Object)"grow");
        TabItem tabItem3 = this.createTabPanel(tabFolder, "Docking 2 (fill)", new MigLayout("fill", "[c]", ""));
        this.createPanel((Object)tabItem3, "1. North", "north");
        this.createPanel((Object)tabItem3, "2. North", "north");
        this.createPanel((Object)tabItem3, "3. West", "west");
        this.createPanel((Object)tabItem3, "4. West", "west");
        this.createPanel((Object)tabItem3, "5. South", "south");
        this.createPanel((Object)tabItem3, "6. East", "east");
        this.createButton(tabItem3, "7. Normal", "");
        this.createButton(tabItem3, "8. Normal", "");
        this.createButton(tabItem3, "9. Normal", "");
        tableColumn = this.createTabPanel(tabFolder, "Docking 3", new MigLayout());
        this.createPanel((Object)tableColumn, "1. North", "north");
        this.createPanel((Object)tableColumn, "2. South", "south");
        this.createPanel((Object)tableColumn, "3. West", "west");
        this.createPanel((Object)tableColumn, "4. East", "east");
        this.createButton(tableColumn, "5. Normal", "");
        tabItem = this.createTabPanel(tabFolder, "Docking 4", new MigLayout());
        this.createPanel((Object)tabItem, "1. North", "north");
        this.createPanel((Object)tabItem, "2. North", "north");
        this.createPanel((Object)tabItem, "3. West", "west");
        this.createPanel((Object)tabItem, "4. West", "west");
        this.createPanel((Object)tabItem, "5. South", "south");
        this.createPanel((Object)tabItem, "6. East", "east");
        this.createButton(tabItem, "7. Normal", "");
        this.createButton(tabItem, "8. Normal", "");
        this.createButton(tabItem, "9. Normal", "");
        TabItem tabItem4 = this.createTabPanel(tabFolder, "Docking 5 (fillx)", new MigLayout("fillx", "[c]", ""));
        this.createPanel((Object)tabItem4, "1. North", "north");
        this.createPanel((Object)tabItem4, "2. North", "north");
        this.createPanel((Object)tabItem4, "3. West", "west");
        this.createPanel((Object)tabItem4, "4. West", "west");
        this.createPanel((Object)tabItem4, "5. South", "south");
        this.createPanel((Object)tabItem4, "6. East", "east");
        this.createButton(tabItem4, "7. Normal", "");
        this.createButton(tabItem4, "8. Normal", "");
        this.createButton(tabItem4, "9. Normal", "");
        TabItem tabItem5 = this.createTabPanel(tabFolder, "Random Docking", new MigLayout("fill"));
        String[] stringArray = new String[]{"north", "east", "south", "west"};
        Random random = new Random();
        for (int i = 0; i < 20; ++i) {
            int n2 = random.nextInt(4);
            this.createPanel((Object)tabItem5, i + 1 + " " + stringArray[n2], stringArray[n2]);
        }
        this.createPanel((Object)tabItem5, "I'm in the Center!", "grow");
        return tabFolder;
    }

    public Control createAbsolute_Position(final Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        TabItem tabItem = this.createTabPanel(tabFolder, "X Y Positions", (Layout)new FillLayout());
        final Composite composite2 = this.createPanel(tabItem, new MigLayout());
        this.createButton(composite2, "pos 0.5al 0al", null);
        this.createButton(composite2, "pos 1al 0al", null);
        this.createButton(composite2, "pos 0.5al 0.5al", null);
        this.createButton(composite2, "pos 5in 45lp", null);
        this.createButton(composite2, "pos 0.5al 0.5al", null);
        this.createButton(composite2, "pos 0.5al 1al", null);
        this.createButton(composite2, "pos 1al .25al", null);
        this.createButton(composite2, "pos visual.x2-pref visual.y2-pref", null);
        this.createButton(composite2, "pos 1al -1in", null);
        this.createButton(composite2, "pos 100 100", null);
        this.createButton(composite2, "pos (10+(20*3lp)) 200", null);
        this.createButton(composite2, "Drag Window! (pos 500-container.xpos 500-container.ypos)", "pos 500-container.xpos 500-container.ypos");
        TabItem tabItem2 = this.createTabPanel(tabFolder, "X1 Y1 X2 Y2 Bounds", (Layout)new FillLayout());
        Composite composite3 = this.createPanel(tabItem2, new MigLayout());
        Label label = this.createLabel(composite3, "pos (visual.x+visual.w*0.1) visual.y2-40 (visual.x2-visual.w*0.1) visual.y2", null, 0x1000800);
        label.setBackground(new Color((Device)display, 200, 200, 255));
        this.deriveFont((Control)label, 1, 10);
        this.createButton(composite3, "pos 0 0 container.x2 n", null);
        this.createButton(composite3, "pos visual.x 40 visual.x2 70", null);
        this.createButton(composite3, "pos visual.x 100 visual.x2 p", null);
        this.createButton(composite3, "pos 0.1al 0.4al n visual.y2-10", null);
        this.createButton(composite3, "pos 0.9al 0.4al n visual.y2-10", null);
        this.createButton(composite3, "pos 0.5al 0.5al, pad 3 0 -3 0", null);
        this.createButton(composite3, "pos n n 50% 50%", null);
        this.createButton(composite3, "pos 50% 50% n n", null);
        this.createButton(composite3, "pos 50% n n 50%", null);
        this.createButton(composite3, "pos n 50% 50% n", null);
        composite.getShell().addControlListener((ControlListener)new ControlAdapter(){

            public void controlMoved(ControlEvent controlEvent) {
                if (!composite2.isDisposed()) {
                    composite2.layout();
                } else {
                    composite.getShell().removeControlListener((ControlListener)this);
                }
            }
        });
        return tabFolder;
    }

    public Control createComponent_Links(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        TabItem tabItem = this.createTabPanel(tabFolder, "Component Links", new MigLayout());
        this.createButton(tabItem, "Mini", "pos null ta.y ta.x2 null, pad 3 0 -3 0");
        this.createTextArea(tabItem, "Components, Please Link to Me!\nMy ID is: 'ta'", "id ta, pos 0.5al 0.5al, w 300");
        this.createButton(tabItem, "id b1,pos ta.x2 ta.y2", null);
        this.createButton(tabItem, "pos b1.x2+rel b1.y visual.x2 null", null);
        this.createCheck(tabItem, "pos (ta.x+indent) (ta.y2+rel)", null);
        this.createButton(tabItem, "pos ta.x2+rel ta.y visual.x2 null", null);
        this.createButton(tabItem, "pos null ta.y+(ta.h-pref)/2 ta.x-rel null", null);
        this.createButton(tabItem, "pos ta.x ta.y2+100 ta.x2 null", null);
        TabItem tabItem2 = this.createTabPanel(tabFolder, "External Components", new MigLayout());
        Button button = this.createButton(tabItem2, "Bounds Externally Set!", "id ext, external");
        button.setBounds(250, 130, 200, 40);
        this.createButton(tabItem2, "pos ext.x2 ext.y2", "pos ext.x2 ext.y2");
        this.createButton(tabItem2, "pos null null ext.x ext.y", "pos null null ext.x ext.y");
        TabItem tabItem3 = this.createTabPanel(tabFolder, "End Grouping", (Layout)new FillLayout());
        Composite composite2 = this.createPanel(tabItem3, new MigLayout());
        this.createButton(composite2, "id b1, endgroupx g1, pos 200 200", null);
        this.createButton(composite2, "id b2, endgroupx g1, pos (b1.x+2ind) (b1.y2+rel)", null);
        this.createButton(composite2, "id b3, endgroupx g1, pos (b1.x+4ind) (b2.y2+rel)", null);
        this.createButton(composite2, "id b4, endgroupx g1, pos (b1.x+6ind) (b3.y2+rel)", null);
        TabItem tabItem4 = this.createTabPanel(tabFolder, "Group Bounds", (Layout)new FillLayout());
        Composite composite3 = this.createPanel(tabItem4, new MigLayout());
        this.createButton(composite3, "id grp1.b1, pos n 0.5al 50% n", null);
        this.createButton(composite3, "id grp1.b2, pos 50% 0.5al n n", null);
        this.createButton(composite3, "id grp1.b3, pos 0.5al n n b1.y", null);
        this.createButton(composite3, "id grp1.b4, pos 0.5al b1.y2 n n", null);
        this.createButton(composite3, "pos n grp1.y2 grp1.x n", null);
        this.createButton(composite3, "pos n n grp1.x grp1.y", null);
        this.createButton(composite3, "pos grp1.x2 n n grp1.y", null);
        this.createButton(composite3, "pos grp1.x2 grp1.y2", null);
        Composite composite4 = this.createPanel(composite3, null);
        composite4.setLayoutData((Object)"pos grp1.x grp1.y grp1.x2 grp1.y2");
        composite4.setBackground(new Color((Device)display, 200, 200, 255));
        return tabFolder;
    }

    public Control createFlow_Direction(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        this.createFlowPanel(tabFolder, "Layout: flowx, Cell: flowx", "", "flowx");
        this.createFlowPanel(tabFolder, "Layout: flowx, Cell: flowy", "", "flowy");
        this.createFlowPanel(tabFolder, "Layout: flowy, Cell: flowx", "flowy", "flowx");
        this.createFlowPanel(tabFolder, "Layout: flowy, Cell: flowy", "flowy", "flowy");
        return tabFolder;
    }

    private TabItem createFlowPanel(TabFolder tabFolder, String string, String string2, String string3) {
        Composite composite;
        MigLayout migLayout = new MigLayout("center, wrap 3," + string2, "[110,fill]", "[110,fill]");
        TabItem tabItem = this.createTabPanel(tabFolder, string, migLayout);
        for (int i = 0; i < 9; ++i) {
            composite = this.createPanel((Object)tabItem, "" + (i + 1), string3);
            Font font = this.deriveFont((Control)composite, -1, 20);
            composite.getChildren()[0].setFont(font);
        }
        Composite composite2 = this.createPanel((Object)tabItem, "5:2", string3 + ",cell 1 1");
        composite = this.deriveFont((Control)composite2, -1, 20);
        composite2.getChildren()[0].setFont((Font)composite);
        return tabItem;
    }

    public Control createDebug(Composite composite) {
        return this.createPlainImpl(composite, true);
    }

    public Control createButton_Bars(final Composite composite) {
        MigLayout migLayout = new MigLayout("ins 0 0 15lp 0", "[grow]", "[grow]u[baseline,nogrid]");
        final Composite composite2 = new Composite(composite, DOUBLE_BUFFER);
        composite2.setLayout((Layout)migLayout);
        TabFolder tabFolder = new TabFolder(composite2, DOUBLE_BUFFER);
        tabFolder.setLayoutData((Object)"grow, wrap");
        this.createButtonBarsPanel(tabFolder, "Buttons", "help", false);
        this.createButtonBarsPanel(tabFolder, "Buttons with Help2", "help2", false);
        this.createButtonBarsPanel(tabFolder, "Buttons (Same width)", "help", true);
        this.createLabel(composite2, "Button Order:", "");
        final Label label = this.createLabel(composite2, "", "growx");
        this.deriveFont((Control)label, 1, -1);
        final Button button = this.createToggleButton(composite2, "Windows", "wmin button");
        final Button button2 = this.createToggleButton(composite2, "Mac OS X", "wmin button");
        button.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent selectionEvent) {
                if (button.getSelection()) {
                    PlatformDefaults.setPlatform(0);
                    label.setText("'" + PlatformDefaults.getButtonOrder() + "'");
                    button2.setSelection(false);
                    composite2.layout();
                }
            }
        });
        button2.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent selectionEvent) {
                if (button2.getSelection()) {
                    PlatformDefaults.setPlatform(1);
                    label.setText("'" + PlatformDefaults.getButtonOrder() + "'");
                    button.setSelection(false);
                    composite2.layout();
                }
            }
        });
        Button button3 = this.createButton(composite2, "Help", "gap unrel,wmin button");
        button3.addSelectionListener((SelectionListener)new SelectionAdapter(){

            public void widgetSelected(SelectionEvent selectionEvent) {
                MessageBox messageBox = new MessageBox(composite.getShell());
                messageBox.setMessage("See JavaDoc for PlatformConverter.getButtonBarOrder(..) for details on the format string.");
                messageBox.open();
            }
        });
        (PlatformDefaults.getPlatform() == 0 ? button : button2).setSelection(true);
        return composite2;
    }

    private TabItem createButtonBarsPanel(TabFolder tabFolder, String string, String string2, boolean bl) {
        MigLayout migLayout = new MigLayout("nogrid, fillx, aligny 100%, gapy unrel");
        TabItem tabItem = this.createTabPanel(tabFolder, string, migLayout);
        String[][] stringArrayArray = new String[][]{{"No", "Yes"}, {"Help", "Close"}, {"OK", "Help"}, {"OK", "Cancel", "Help"}, {"OK", "Cancel", "Apply", "Help"}, {"No", "Yes", "Cancel"}, {"Help", "< Move Back", "Move Forward >", "Cancel"}, {"Print...", "Cancel", "Help"}};
        for (int i = 0; i < stringArrayArray.length; ++i) {
            for (int j = 0; j < stringArrayArray[i].length; ++j) {
                String string3;
                String string4 = string3 = stringArrayArray[i][j];
                if (string3.equals("Help")) {
                    string4 = string2;
                } else if (string3.equals("< Move Back")) {
                    string4 = "back";
                } else if (string3.equals("Close")) {
                    string4 = "cancel";
                } else if (string3.equals("Move Forward >")) {
                    string4 = "next";
                } else if (string3.equals("Print...")) {
                    string4 = "other";
                }
                String string5 = j == stringArrayArray[i].length - 1 ? ",wrap" : "";
                String string6 = bl ? "sgx " + i + "," : "";
                this.createButton(tabItem, string3, string6 + "tag " + string4 + string5);
            }
        }
        return tabItem;
    }

    public Control createOrientation(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        MigLayout migLayout = new MigLayout("flowy", "[grow,fill]", "[]0[]15lp[]0[]");
        TabItem tabItem = this.createTabPanel(tabFolder, "Orientation", migLayout);
        MigLayout migLayout2 = new MigLayout("", "[][grow,fill]", "");
        Composite composite2 = this.createPanel(tabItem, migLayout2);
        this.addSeparator(composite2, "Default Orientation");
        this.createLabel(composite2, "Level", "");
        this.createTextField(composite2, "", "span,growx");
        this.createLabel(composite2, "Radar", "");
        this.createTextField(composite2, "", "");
        this.createTextField(composite2, "", "");
        MigLayout migLayout3 = new MigLayout("rtl,ttb", "[][grow,fill]", "");
        Composite composite3 = this.createPanel(tabItem, migLayout3);
        this.addSeparator(composite3, "Right to Left");
        this.createLabel(composite3, "Level", "");
        this.createTextField(composite3, "", "span,growx");
        this.createLabel(composite3, "Radar", "");
        this.createTextField(composite3, "", "");
        this.createTextField(composite3, "", "");
        MigLayout migLayout4 = new MigLayout("rtl,btt", "[][grow,fill]", "");
        Composite composite4 = this.createPanel(tabItem, migLayout4);
        this.addSeparator(composite4, "Right to Left, Bottom to Top");
        this.createLabel(composite4, "Level", "");
        this.createTextField(composite4, "", "span,growx");
        this.createLabel(composite4, "Radar", "");
        this.createTextField(composite4, "", "");
        this.createTextField(composite4, "", "");
        MigLayout migLayout5 = new MigLayout("ltr,btt", "[][grow,fill]", "");
        Composite composite5 = this.createPanel(tabItem, migLayout5);
        this.addSeparator(composite5, "Left to Right, Bottom to Top");
        this.createLabel(composite5, "Level", "");
        this.createTextField(composite5, "", "span,growx");
        this.createLabel(composite5, "Radar", "");
        this.createTextField(composite5, "", "");
        this.createTextField(composite5, "", "");
        return tabFolder;
    }

    public Control createCell_Position(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        MigLayout migLayout = new MigLayout("", "[100:pref,fill]", "[100:pref,fill]");
        TabItem tabItem = this.createTabPanel(tabFolder, "Absolute", migLayout);
        this.createPanel((Object)tabItem, "cell 0 0", null);
        this.createPanel((Object)tabItem, "cell 2 0", null);
        this.createPanel((Object)tabItem, "cell 3 0", null);
        this.createPanel((Object)tabItem, "cell 1 1", null);
        this.createPanel((Object)tabItem, "cell 0 2", null);
        this.createPanel((Object)tabItem, "cell 2 2", null);
        this.createPanel((Object)tabItem, "cell 2 2", null);
        MigLayout migLayout2 = new MigLayout("wrap", "[100:pref,fill][100:pref,fill][100:pref,fill][100:pref,fill]", "[100:pref,fill]");
        TabItem tabItem2 = this.createTabPanel(tabFolder, "Relative + Wrap", migLayout2);
        this.createPanel((Object)tabItem2, "", null);
        this.createPanel((Object)tabItem2, "skip", null);
        this.createPanel((Object)tabItem2, "", null);
        this.createPanel((Object)tabItem2, "skip,wrap", null);
        this.createPanel((Object)tabItem2, "", null);
        this.createPanel((Object)tabItem2, "skip,split", null);
        this.createPanel((Object)tabItem2, "", null);
        MigLayout migLayout3 = new MigLayout("", "[100:pref,fill]", "[100:pref,fill]");
        TabItem tabItem3 = this.createTabPanel(tabFolder, "Relative", migLayout3);
        this.createPanel((Object)tabItem3, "", null);
        this.createPanel((Object)tabItem3, "skip", null);
        this.createPanel((Object)tabItem3, "wrap", null);
        this.createPanel((Object)tabItem3, "skip,wrap", null);
        this.createPanel((Object)tabItem3, "", null);
        this.createPanel((Object)tabItem3, "skip,split", null);
        this.createPanel((Object)tabItem3, "", null);
        MigLayout migLayout4 = new MigLayout("", "[100:pref,fill]", "[100:pref,fill]");
        TabItem tabItem4 = this.createTabPanel(tabFolder, "Mixed", migLayout4);
        this.createPanel((Object)tabItem4, "", null);
        this.createPanel((Object)tabItem4, "cell 2 0", null);
        this.createPanel((Object)tabItem4, "", null);
        this.createPanel((Object)tabItem4, "cell 1 1,wrap", null);
        this.createPanel((Object)tabItem4, "", null);
        this.createPanel((Object)tabItem4, "cell 2 2,split", null);
        this.createPanel((Object)tabItem4, "", null);
        return tabFolder;
    }

    public Control createPlain(Composite composite) {
        return this.createPlainImpl(composite, false);
    }

    private Control createPlainImpl(Composite composite, boolean bl) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        MigLayout migLayout = new MigLayout(bl && benchRuns == 0 ? "debug" : "", "[r][100lp, fill][60lp][95lp, fill]", "");
        TabItem tabItem = this.createTabPanel(tabFolder, "Plain", migLayout);
        this.addSeparator(tabItem, "Manufacturer");
        this.createLabel(tabItem, "Company", "");
        this.createTextField(tabItem, "", "span,growx");
        this.createLabel(tabItem, "Contact", "");
        this.createTextField(tabItem, "", "span,growx");
        this.createLabel(tabItem, "Order No", "");
        this.createTextField(tabItem, "", "wmin 15*6,wrap");
        this.addSeparator(tabItem, "Inspector");
        this.createLabel(tabItem, "Name", "");
        this.createTextField(tabItem, "", "span,growx");
        this.createLabel(tabItem, "Reference No", "");
        this.createTextField(tabItem, "", "wrap");
        this.createLabel(tabItem, "Status", "");
        this.createCombo(tabItem, new String[]{"In Progress", "Finnished", "Released"}, "wrap");
        this.addSeparator(tabItem, "Ship");
        this.createLabel(tabItem, "Shipyard", "");
        this.createTextField(tabItem, "", "span,growx");
        this.createLabel(tabItem, "Register No", "");
        this.createTextField(tabItem, "", "");
        this.createLabel(tabItem, "Hull No", "right");
        this.createTextField(tabItem, "", "wmin 15*6,wrap");
        this.createLabel(tabItem, "Project StructureType", "");
        this.createCombo(tabItem, new String[]{"New Building", "Convention", "Repair"}, "wrap");
        if (bl) {
            this.createLabel(tabItem, "Blue is component bounds. Cell bounds (red) can not be shown in SWT", "newline,ax left,span,gaptop 40");
        }
        return tabFolder;
    }

    public Control createBound_Sizes(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        for (int i = 0; i < 2; ++i) {
            String string = i == 0 ? "[right][300]" : "[right, 150lp:pref][300]";
            MigLayout migLayout = new MigLayout("wrap", string, "");
            TabItem tabItem = this.createTabPanel(tabFolder, i == 0 ? "Jumping 1" : "Stable 1", migLayout);
            this.createLabel(tabItem, "File Number:", "");
            this.createTextField(tabItem, "", "growx");
            this.createLabel(tabItem, "RFQ Number:", "");
            this.createTextField(tabItem, "", "growx");
            this.createLabel(tabItem, "Entry Date:", "");
            this.createTextField(tabItem, "        ", "wmin 6*6");
            this.createLabel(tabItem, "Sales Person:", "");
            this.createTextField(tabItem, "", "growx");
            MigLayout migLayout2 = new MigLayout("wrap", string, "");
            TabItem tabItem2 = this.createTabPanel(tabFolder, i == 0 ? "Jumping 2" : "Stable 2", migLayout2);
            this.createLabel(tabItem2, "Shipper:", "");
            this.createTextField(tabItem2, "        ", "split 2");
            this.createTextField(tabItem2, "", "growx");
            this.createLabel(tabItem2, "Consignee:", "");
            this.createTextField(tabItem2, "        ", "split 2");
            this.createTextField(tabItem2, "", "growx");
            this.createLabel(tabItem2, "Departure:", "");
            this.createTextField(tabItem2, "        ", "split 2");
            this.createTextField(tabItem2, "", "growx");
            this.createLabel(tabItem2, "Destination:", "");
            this.createTextField(tabItem2, "        ", "split 2");
            this.createTextField(tabItem2, "", "growx");
        }
        return tabFolder;
    }

    public Control createComponent_Sizes(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        MigLayout migLayout = new MigLayout("wrap", "[right][0:pref,grow]", "");
        TabItem tabItem = this.createTabPanel(tabFolder, "Component Sizes", (Layout)new FillLayout());
        SashForm sashForm = new SashForm(this.getComposite(tabItem), 65792);
        sashForm.setBackground(new Color((Device)display, 255, 255, 255));
        sashForm.setBackgroundMode(2);
        Composite composite2 = this.createPanel((Object)sashForm, migLayout, 2048);
        this.createTextArea(sashForm, "Use slider to see how the components grow and shrink depending on the constraints set on them.", "");
        this.createLabel(composite2, "", "");
        this.createTextField(composite2, "8       ", "");
        this.createLabel(composite2, "width min!", null);
        this.createTextField(composite2, "3  ", "width min!");
        this.createLabel(composite2, "width pref!", "");
        this.createTextField(composite2, "3  ", "width pref!");
        this.createLabel(composite2, "width min:pref", null);
        this.createTextField(composite2, "8       ", "width min:pref");
        this.createLabel(composite2, "width min:100:150", null);
        this.createTextField(composite2, "8       ", "width min:100:150");
        this.createLabel(composite2, "width min:100:150, growx", null);
        this.createTextField(composite2, "8       ", "width min:100:150, growx");
        this.createLabel(composite2, "width min:100, growx", null);
        this.createTextField(composite2, "8       ", "width min:100, growx");
        this.createLabel(composite2, "width 40!", null);
        this.createTextField(composite2, "8       ", "width 40!");
        this.createLabel(composite2, "width 40:40:40", null);
        this.createTextField(composite2, "8       ", "width 40:40:40");
        return tabFolder;
    }

    public Control createCell_Alignments(Composite composite) {
        String[] stringArray;
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        MigLayout migLayout = new MigLayout("wrap", "[grow,left][grow,center][grow,right][grow,fill,center]", "[]unrel[][]");
        TabItem tabItem = this.createTabPanel(tabFolder, "Horizontal", migLayout);
        String[] stringArray2 = new String[]{"", "growx", "growx 0", "left", "center", "right", "leading", "trailing"};
        this.createLabel(tabItem, "[left]", "c");
        this.createLabel(tabItem, "[center]", "c");
        this.createLabel(tabItem, "[right]", "c");
        this.createLabel(tabItem, "[fill,center]", "c, growx 0");
        for (int i = 0; i < stringArray2.length; ++i) {
            for (int j = 0; j < 4; ++j) {
                stringArray = stringArray2[i].length() > 0 ? stringArray2[i] : "default";
                this.createButton(tabItem, (String)stringArray, stringArray2[i]);
            }
        }
        MigLayout migLayout2 = new MigLayout("wrap,flowy", "[right][]", "[grow,top][grow,center][grow,bottom][grow,fill,bottom][grow,fill,baseline]");
        TabItem tabItem2 = this.createTabPanel(tabFolder, "Vertical", migLayout2);
        stringArray = new String[]{"", "growy", "growy 0", "top", "center", "bottom"};
        this.createLabel(tabItem2, "[top]", "center");
        this.createLabel(tabItem2, "[center]", "center");
        this.createLabel(tabItem2, "[bottom]", "center");
        this.createLabel(tabItem2, "[fill, bottom]", "center, growy 0");
        this.createLabel(tabItem2, "[fill, baseline]", "center");
        for (int i = 0; i < stringArray.length; ++i) {
            for (int j = 0; j < 5; ++j) {
                String string = stringArray[i].length() > 0 ? stringArray[i] : "default";
                this.createButton(tabItem2, string, stringArray[i]);
            }
        }
        return tabFolder;
    }

    public Control createUnits(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        MigLayout migLayout = new MigLayout("wrap", "[right][]", "");
        TabItem tabItem = this.createTabPanel(tabFolder, "Horizontal", migLayout);
        String[] stringArray = new String[]{"72pt", "25.4mm", "2.54cm", "1in", "72px", "96px", "120px", "25%", "30sp"};
        for (int i = 0; i < stringArray.length; ++i) {
            this.createLabel(tabItem, stringArray[i], "");
            this.createTextField(tabItem, "", "width " + stringArray[i] + "");
        }
        MigLayout migLayout2 = new MigLayout("", "[right][][]", "");
        TabItem tabItem2 = this.createTabPanel(tabFolder, "Horizontal LP", migLayout2);
        this.createLabel(tabItem2, "9 cols", "");
        this.createTextField(tabItem2, "", "wmin 9*6");
        String[] stringArray2 = new String[]{"75lp", "75px", "88px", "100px"};
        this.createLabel(tabItem2, "", "wrap");
        for (int i = 0; i < stringArray2.length; ++i) {
            this.createLabel(tabItem2, stringArray2[i], "");
            this.createTextField(tabItem2, "", "width " + stringArray2[i] + ", wrap");
        }
        MigLayout migLayout3 = new MigLayout("wrap,flowy", "[c]", "[top][top]");
        TabItem tabItem3 = this.createTabPanel(tabFolder, "Vertical", migLayout3);
        String[] stringArray3 = new String[]{"72pt", "25.4mm", "2.54cm", "1in", "72px", "96px", "120px", "25%", "30sp"};
        for (int i = 0; i < stringArray.length; ++i) {
            this.createLabel(tabItem3, stringArray3[i], "");
            this.createTextArea(tabItem3, "", "width 50!, height " + stringArray3[i] + "");
        }
        MigLayout migLayout4 = new MigLayout("wrap,flowy", "[c]", "[top][top]40px[top][top]");
        TabItem tabItem4 = this.createTabPanel(tabFolder, "Vertical LP", migLayout4);
        this.createLabel(tabItem4, "4 rows", "");
        this.createTextArea(tabItem4, "\n\n\n\n", "width 50!");
        this.createLabel(tabItem4, "field", "");
        this.createTextField(tabItem4, "", "wmin 5*9");
        String[] stringArray4 = new String[]{"63lp", "57px", "63px", "68px", "25%"};
        String[] stringArray5 = new String[]{"21lp", "21px", "23px", "24px", "10%"};
        for (int i = 0; i < stringArray4.length; ++i) {
            this.createLabel(tabItem4, stringArray4[i], "");
            this.createTextArea(tabItem4, "", "width 50!, height " + stringArray4[i] + "");
            this.createLabel(tabItem4, stringArray5[i], "");
            this.createTextField(tabItem4, "", "height " + stringArray5[i] + "!,wmin 5*6");
        }
        this.createLabel(tabItem4, "button", "skip 2");
        this.createButton(tabItem4, "...", "");
        return tabFolder;
    }

    public Control createGrouping(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        MigLayout migLayout = new MigLayout("", "[]push[][][]", "");
        TabItem tabItem = this.createTabPanel(tabFolder, "Ungrouped", migLayout);
        this.createButton(tabItem, "Help", "");
        this.createButton(tabItem, "< Back", "gap push");
        this.createButton(tabItem, "Forward >", "");
        this.createButton(tabItem, "Apply", "gap unrel");
        this.createButton(tabItem, "Cancel", "gap unrel");
        MigLayout migLayout2 = new MigLayout("nogrid, fillx");
        TabItem tabItem2 = this.createTabPanel(tabFolder, "Grouped (Components)", migLayout2);
        this.createButton(tabItem2, "Help", "sg");
        this.createButton(tabItem2, "< Back", "sg, gap push");
        this.createButton(tabItem2, "Forward >", "sg");
        this.createButton(tabItem2, "Apply", "sg, gap unrel");
        this.createButton(tabItem2, "Cancel", "sg, gap unrel");
        MigLayout migLayout3 = new MigLayout("", "[sg,fill]push[sg,fill][sg,fill]unrel[sg,fill]unrel[sg,fill]", "");
        TabItem tabItem3 = this.createTabPanel(tabFolder, "Grouped (Columns)", migLayout3);
        this.createButton(tabItem3, "Help", "");
        this.createButton(tabItem3, "< Back", "");
        this.createButton(tabItem3, "Forward >", "");
        this.createButton(tabItem3, "Apply", "");
        this.createButton(tabItem3, "Cancel", "");
        MigLayout migLayout4 = new MigLayout();
        TabItem tabItem4 = this.createTabPanel(tabFolder, "Ungrouped Rows", migLayout4);
        this.createLabel(tabItem4, "File Number:", "");
        this.createTextField(tabItem4, "30                            ", "wrap");
        this.createLabel(tabItem4, "BL/MBL number:", "");
        this.createTextField(tabItem4, "7      ", "split 2");
        this.createTextField(tabItem4, "7      ", "wrap");
        this.createLabel(tabItem4, "Entry Date:", "");
        this.createTextField(tabItem4, "7      ", "wrap");
        this.createLabel(tabItem4, "RFQ Number:", "");
        this.createTextField(tabItem4, "30                            ", "wrap");
        this.createLabel(tabItem4, "Goods:", "");
        this.createCheck(tabItem4, "Dangerous", "wrap");
        this.createLabel(tabItem4, "Shipper:", "");
        this.createTextField(tabItem4, "30                            ", "wrap");
        this.createLabel(tabItem4, "Customer:", "");
        this.createTextField(tabItem4, "", "split 2,growx");
        this.createButton(tabItem4, "...", "width 60px:pref,wrap");
        this.createLabel(tabItem4, "Port of Loading:", "");
        this.createTextField(tabItem4, "30                            ", "wrap");
        this.createLabel(tabItem4, "Destination:", "");
        this.createTextField(tabItem4, "30                            ", "wrap");
        MigLayout migLayout5 = new MigLayout("", "[]", "[sg]");
        TabItem tabItem5 = this.createTabPanel(tabFolder, "Grouped Rows", migLayout5);
        this.createLabel(tabItem5, "File Number:", "");
        this.createTextField(tabItem5, "30                            ", "wrap");
        this.createLabel(tabItem5, "BL/MBL number:", "");
        this.createTextField(tabItem5, "7      ", "split 2");
        this.createTextField(tabItem5, "7      ", "wrap");
        this.createLabel(tabItem5, "Entry Date:", "");
        this.createTextField(tabItem5, "7      ", "wrap");
        this.createLabel(tabItem5, "RFQ Number:", "");
        this.createTextField(tabItem5, "30                            ", "wrap");
        this.createLabel(tabItem5, "Goods:", "");
        this.createCheck(tabItem5, "Dangerous", "wrap");
        this.createLabel(tabItem5, "Shipper:", "");
        this.createTextField(tabItem5, "30                            ", "wrap");
        this.createLabel(tabItem5, "Customer:", "");
        this.createTextField(tabItem5, "", "split 2,growx");
        this.createButton(tabItem5, "...", "width 50px:pref,wrap");
        this.createLabel(tabItem5, "Port of Loading:", "");
        this.createTextField(tabItem5, "30                            ", "wrap");
        this.createLabel(tabItem5, "Destination:", "");
        this.createTextField(tabItem5, "30                            ", "wrap");
        return tabFolder;
    }

    public Control createSpan(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        MigLayout migLayout = new MigLayout("", "[fill][25%,fill][105lp!,fill][150px!,fill]", "[]15[][]");
        TabItem tabItem = this.createTabPanel(tabFolder, "Column Span/Split", migLayout);
        this.createTextField(tabItem, "Col1 [ ]", "");
        this.createTextField(tabItem, "Col2 [25%]", "");
        this.createTextField(tabItem, "Col3 [105lp!]", "");
        this.createTextField(tabItem, "Col4 [150px!]", "wrap");
        this.createLabel(tabItem, "Full Name:", "");
        this.createTextField(tabItem, "span, growx                              ", "span,growx");
        this.createLabel(tabItem, "Phone:", "");
        this.createTextField(tabItem, "   ", "span 3, split 5");
        this.createTextField(tabItem, "     ", null);
        this.createTextField(tabItem, "     ", null);
        this.createTextField(tabItem, "       ", null);
        this.createLabel(tabItem, "(span 3, split 4)", "wrap");
        this.createLabel(tabItem, "Zip/City:", "");
        this.createTextField(tabItem, "     ", "");
        this.createTextField(tabItem, "span 2, growx", null);
        MigLayout migLayout2 = new MigLayout("wrap", "[225lp]para[225lp]", "[]3[]unrel[]3[]unrel[]3[]");
        TabItem tabItem2 = this.createTabPanel(tabFolder, "Row Span", migLayout2);
        this.createLabel(tabItem2, "Name", "");
        this.createLabel(tabItem2, "Notes", "");
        this.createTextField(tabItem2, "growx", null);
        this.createTextArea(tabItem2, "spany,grow          ", "spany,grow,hmin 13*5");
        this.createLabel(tabItem2, "Phone", "");
        this.createTextField(tabItem2, "growx", null);
        this.createLabel(tabItem2, "Fax", "");
        this.createTextField(tabItem2, "growx", null);
        return tabFolder;
    }

    public Control createGrowing(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        MigLayout migLayout = new MigLayout("", "[pref!][grow,fill]", "[]15[]");
        TabItem tabItem = this.createTabPanel(tabFolder, "All", migLayout);
        this.createLabel(tabItem, "Fixed", "");
        this.createLabel(tabItem, "Gets all extra space", "wrap");
        this.createTextField(tabItem, "     ", "");
        this.createTextField(tabItem, "     ", "");
        MigLayout migLayout2 = new MigLayout("", "[pref!][grow,fill]", "[]15[]");
        TabItem tabItem2 = this.createTabPanel(tabFolder, "Half", migLayout2);
        this.createLabel(tabItem2, "Fixed", "");
        this.createLabel(tabItem2, "Gets half of extra space", "");
        this.createLabel(tabItem2, "Gets half of extra space", "wrap");
        this.createTextField(tabItem2, "     ", "");
        this.createTextField(tabItem2, "     ", "");
        this.createTextField(tabItem2, "     ", "");
        MigLayout migLayout3 = new MigLayout("", "[pref!][0:0,grow 25,fill][0:0,grow 75,fill]", "[]15[]");
        TabItem tabItem3 = this.createTabPanel(tabFolder, "Percent 1", migLayout3);
        this.createLabel(tabItem3, "Fixed", "");
        this.createLabel(tabItem3, "Gets 25% of extra space", "");
        this.createLabel(tabItem3, "Gets 75% of extra space", "wrap");
        this.createTextField(tabItem3, "     ", "");
        this.createTextField(tabItem3, "     ", "");
        this.createTextField(tabItem3, "     ", "");
        MigLayout migLayout4 = new MigLayout("", "[0:0,grow 33,fill][0:0,grow 67,fill]", "[]15[]");
        TabItem tabItem4 = this.createTabPanel(tabFolder, "Percent 2", migLayout4);
        this.createLabel(tabItem4, "Gets 33% of extra space", "");
        this.createLabel(tabItem4, "Gets 67% of extra space", "wrap");
        this.createTextField(tabItem4, "     ", "");
        this.createTextField(tabItem4, "     ", "");
        MigLayout migLayout5 = new MigLayout("flowy", "[]15[]", "[][c,pref!][c,grow 25,fill][c,grow 75,fill]");
        TabItem tabItem5 = this.createTabPanel(tabFolder, "Vertical 1", migLayout5);
        this.createLabel(tabItem5, "Fixed", "skip");
        this.createLabel(tabItem5, "Gets 25% of extra space", "");
        this.createLabel(tabItem5, "Gets 75% of extra space", "wrap");
        this.createLabel(tabItem5, "new Text(SWT.MULTI | SWT.WRAP | SWT.BORDER)", "");
        this.createTextArea(tabItem5, "", "hmin 4*13");
        this.createTextArea(tabItem5, "", "hmin 4*13");
        this.createTextArea(tabItem5, "", "hmin 4*13");
        MigLayout migLayout6 = new MigLayout("flowy", "[]15[]", "[][c,grow 33,fill][c,grow 67,fill]");
        TabItem tabItem6 = this.createTabPanel(tabFolder, "Vertical 2", migLayout6);
        this.createLabel(tabItem6, "Gets 33% of extra space", "skip");
        this.createLabel(tabItem6, "Gets 67% of extra space", "wrap");
        this.createLabel(tabItem6, "new Text(SWT.MULTI | SWT.WRAP | SWT.BORDER)", "");
        this.createTextArea(tabItem6, "", "hmin 4*13");
        this.createTextArea(tabItem6, "", "hmin 4*13");
        return tabFolder;
    }

    public Control createBasic_Sizes(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        MigLayout migLayout = new MigLayout("", "[]15[75px]25[min]25[]", "[]15");
        TabItem tabItem = this.createTabPanel(tabFolder, "Horizontal - Column size set", migLayout);
        this.createLabel(tabItem, "75px", "skip");
        this.createLabel(tabItem, "Min", "");
        this.createLabel(tabItem, "Pref", "wrap");
        this.createLabel(tabItem, "new Text(15)", "");
        this.createTextField(tabItem, "               ", "wmin 10");
        this.createTextField(tabItem, "               ", "wmin 10");
        this.createTextField(tabItem, "               ", "wmin 10");
        MigLayout migLayout2 = new MigLayout("flowy,wrap", "[]15[]", "[]15[c,45:45]15[c,min]15[c,pref]");
        TabItem tabItem2 = this.createTabPanel(tabFolder, "\"Vertical - Row sized\"", migLayout2);
        this.createLabel(tabItem2, "45px", "skip");
        this.createLabel(tabItem2, "Min", "");
        this.createLabel(tabItem2, "Pref", "");
        this.createLabel(tabItem2, "new Text(SWT.MULTI)", "");
        this.createTextArea(tabItem2, "", "");
        this.createTextArea(tabItem2, "", "");
        this.createTextArea(tabItem2, "", "");
        MigLayout migLayout3 = new MigLayout("flowy,wrap", "[]15[]", "[]15[baseline]15[baseline]15[baseline]");
        TabItem tabItem3 = this.createTabPanel(tabFolder, "\"Vertical - Component sized + Baseline\"", migLayout3);
        this.createLabel(tabItem3, "45px", "skip");
        this.createLabel(tabItem3, "Min", "");
        this.createLabel(tabItem3, "Pref", "");
        this.createLabel(tabItem3, "new Text(SWT.MULTI)", "");
        this.createTextArea(tabItem3, "", "height 45");
        this.createTextArea(tabItem3, "", "height min");
        this.createTextArea(tabItem3, "", "height pref");
        return tabFolder;
    }

    public Control createAlignments(Composite composite) {
        int n;
        int n2;
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        MigLayout migLayout = new MigLayout("wrap", "[label]15[left]15[center]15[right]15[fill]15[]", "[]15[]");
        String[] stringArray = new String[]{"[label]", "[left]", "[center]", "[right]", "[fill]", "[] (Default)"};
        TabItem tabItem = this.createTabPanel(tabFolder, "Horizontal", migLayout);
        String[] stringArray2 = new String[]{"First Name", "Phone Number", "Facsmile", "Email", "Address", "Other"};
        for (n2 = 0; n2 < stringArray.length; ++n2) {
            this.createLabel(tabItem, stringArray[n2], "");
        }
        for (n2 = 0; n2 < stringArray.length; ++n2) {
            for (int i = 0; i < stringArray2.length; ++i) {
                if (i == 0) {
                    this.createLabel(tabItem, stringArray2[n2] + ":", "");
                    continue;
                }
                this.createButton(tabItem, stringArray2[n2], "");
            }
        }
        MigLayout migLayout2 = new MigLayout("wrap,flowy", "[]unrel[]rel[]", "[top]15[center]15[bottom]15[fill]15[fill,baseline]15[baseline]15[]");
        String[] stringArray3 = new String[]{"[top]", "[center]", "[bottom]", "[fill]", "[fill,baseline]", "[baseline]", "[] (Default)"};
        TabItem tabItem2 = this.createTabPanel(tabFolder, "Vertical", migLayout2);
        for (n = 0; n < stringArray3.length; ++n) {
            this.createLabel(tabItem2, stringArray3[n], "");
        }
        for (n = 0; n < stringArray3.length; ++n) {
            this.createButton(tabItem2, "A Button", "");
        }
        for (n = 0; n < stringArray3.length; ++n) {
            this.createTextField(tabItem2, "JTextFied", "");
        }
        for (n = 0; n < stringArray3.length; ++n) {
            this.createTextArea(tabItem2, "Text    ", "");
        }
        for (n = 0; n < stringArray3.length; ++n) {
            this.createTextArea(tabItem2, "Text\nwith two lines", "");
        }
        for (n = 0; n < stringArray3.length; ++n) {
            this.createTextArea(tabItem2, "Scrolling Text\nwith two lines", "");
        }
        return tabFolder;
    }

    public Control createQuick_Start(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        MigLayout migLayout = new MigLayout("wrap", "[right][fill,sizegroup]unrel[right][fill,sizegroup]", "");
        TabItem tabItem = this.createTabPanel(tabFolder, "Quick Start", migLayout);
        this.addSeparator(tabItem, "General");
        this.createLabel(tabItem, "Company", "gap indent");
        this.createTextField(tabItem, "", "span,growx");
        this.createLabel(tabItem, "Contact", "gap indent");
        this.createTextField(tabItem, "", "span,growx");
        this.addSeparator(tabItem, "Propeller");
        this.createLabel(tabItem, "PTI/kW", "gap indent");
        this.createTextField(tabItem, "", "wmin 130");
        this.createLabel(tabItem, "Power/kW", "gap indent");
        this.createTextField(tabItem, "", "wmin 130");
        this.createLabel(tabItem, "R/mm", "gap indent");
        this.createTextField(tabItem, "", "wmin 130");
        this.createLabel(tabItem, "D/mm", "gap indent");
        this.createTextField(tabItem, "", "wmin 130");
        return tabFolder;
    }

    public Control createGrow_Shrink(Composite composite) {
        TabFolder tabFolder = new TabFolder(composite, DOUBLE_BUFFER);
        MigLayout migLayout = new MigLayout("nogrid");
        TabItem tabItem = this.createTabPanel(tabFolder, "Shrink", (Layout)new FillLayout());
        SashForm sashForm = new SashForm(this.getComposite(tabItem), 65792);
        sashForm.setBackground(new Color((Device)display, 255, 255, 255));
        sashForm.setBackgroundMode(2);
        Composite composite2 = this.createPanel((Object)sashForm, migLayout, 2048);
        composite2.setLayoutData((Object)"wmin 100");
        this.createTextField(composite2, "shp 110", "shp 110,w 10:130");
        this.createTextField(composite2, "Default (100)", "w 10:130");
        this.createTextField(composite2, "shp 90", "shp 90,w 10:130");
        this.createTextField(composite2, "shrink 25", "newline,shrink 25,w 10:130");
        this.createTextField(composite2, "shrink 75", "shrink 75,w 10:130");
        this.createTextField(composite2, "Default", "newline, w 10:130");
        this.createTextField(composite2, "Default", "w 10:130");
        this.createTextField(composite2, "shrink 0", "newline,shrink 0,w 10:130");
        this.createTextField(composite2, "shp 110", "newline,shp 110,w 10:130");
        this.createTextField(composite2, "shp 100,shrink 25", "shp 100,shrink 25,w 10:130");
        this.createTextField(composite2, "shp 100,shrink 75", "shp 100,shrink 75,w 10:130");
        this.createTextArea(sashForm, "Use the slider to see how the components shrink depending on the constraints set on them.\n\n'shp' means Shrink Priority. Lower values will be shrunk before higer ones and the default value is 100.\n\n'shrink' means Shrink Weight. Lower values relative to other's means they will shrink less when space is scarse. Shrink Weight is only relative to components with the same Shrink Priority. Default Shrink Weight is 100.\n\nThe component's minimum size will always be honored.\n\nFor SWT, which doesn't have a component notion of minimum, preferred or maximum size, those sizes are set explicitly to minimum 10 and preferred 130 pixels.", "");
        TabItem tabItem2 = this.createTabPanel(tabFolder, "Grow", (Layout)new FillLayout());
        SashForm sashForm2 = new SashForm(this.getComposite(tabItem2), 65792);
        sashForm2.setBackground(new Color((Device)display, 255, 255, 255));
        sashForm2.setBackgroundMode(2);
        Composite composite3 = this.createPanel((Object)sashForm2, new MigLayout("nogrid", "[grow]"), 2048);
        composite3.setLayoutData((Object)"wmin 100");
        this.createButton(composite3, "gp 110, grow", "gp 110, grow, wmax 170");
        this.createButton(composite3, "Default (100), grow", "grow, wmax 170");
        this.createButton(composite3, "gp 90, grow", "gp 90, grow, wmax 170");
        this.createButton(composite3, "Default Button", "newline");
        this.createButton(composite3, "growx", "newline,growx,wrap");
        this.createButton(composite3, "gp 110, grow", "gp 110, grow, wmax 170");
        this.createButton(composite3, "gp 100, grow 25", "gp 100, grow 25, wmax 170");
        this.createButton(composite3, "gp 100, grow 75", "gp 100, grow 75, wmax 170");
        this.createTextArea(sashForm2, "'gp' means Grow Priority. Lower values will be grown before higher ones and the default value is 100.\n\n'grow' means Grow Weight. Higher values relative to other's means they will grow more when space is up for takes. Grow Weight is only relative to components with the same Grow Priority. Default Grow Weight is 0 which means components will normally not grow. \n\nNote that the buttons in the first and last row have max width set to 170 to emphasize Grow Priority.\n\nThe component's maximum size will always be honored.", "");
        return tabFolder;
    }

    private Combo createCombo(Object object, String[] stringArray, Object object2) {
        Combo combo = new Combo(this.getComposite(object), 4);
        for (int i = 0; i < stringArray.length; ++i) {
            combo.add(stringArray[i]);
        }
        combo.setLayoutData(object2);
        return combo;
    }

    private Label createLabel(Object object, String string, Object object2) {
        return this.createLabel(object, string, object2, 16384);
    }

    private Label createLabel(Object object, String string, Object object2, int n) {
        Label label = new Label(this.getComposite(object), n | DOUBLE_BUFFER);
        label.setText(string);
        label.setLayoutData(object2 != null ? object2 : string);
        return label;
    }

    private Text createTextField(Object object, String string, Object object2) {
        Text text = new Text(this.getComposite(object), 0x804 | DOUBLE_BUFFER);
        text.setText(string);
        text.setLayoutData(object2 != null ? object2 : string);
        return text;
    }

    private Button createButton(Object object, String string, Object object2) {
        return this.createButton(this.getComposite(object), string, object2, false);
    }

    private Button createButton(Object object, String string, Object object2, boolean bl) {
        Button button = new Button(this.getComposite(object), 0x40008 | DOUBLE_BUFFER);
        button.setText(string.length() == 0 ? "\"\"" : string);
        button.setLayoutData(object2 != null ? object2 : string);
        return button;
    }

    private Composite createPanel(Object object, String string, Object object2) {
        Color color = new Color((Device)display.getActiveShell().getDisplay(), 255, 255, 255);
        Composite composite = new Composite(this.getComposite(object), DOUBLE_BUFFER | 0x800);
        composite.setLayout((Layout)new MigLayout("fill"));
        composite.setBackground(color);
        composite.setLayoutData(object2 != null ? object2 : string);
        string = string.length() == 0 ? "\"\"" : string;
        Label label = this.createLabel(composite, string, "grow", 0x1040000);
        label.setBackground(color);
        return composite;
    }

    private TabItem createTabPanel(TabFolder tabFolder, String string, Layout layout) {
        Composite composite = new Composite((Composite)tabFolder, DOUBLE_BUFFER);
        TabItem tabItem = new TabItem(tabFolder, DOUBLE_BUFFER);
        tabItem.setControl((Control)composite);
        tabItem.setText(string);
        if (layout != null) {
            composite.setLayout(layout);
        }
        return tabItem;
    }

    private Composite createPanel(Object object, Layout layout) {
        return this.createPanel(object, layout, 0);
    }

    private Composite createPanel(Object object, Layout layout, int n) {
        Composite composite = new Composite(this.getComposite(object), DOUBLE_BUFFER | n);
        composite.setLayout(layout);
        return composite;
    }

    private Button createToggleButton(Object object, String string, Object object2) {
        Button button = new Button(this.getComposite(object), 2 | DOUBLE_BUFFER);
        button.setText(string.length() == 0 ? "\"\"" : string);
        button.setLayoutData(object2 != null ? object2 : string);
        return button;
    }

    private Button createCheck(Object object, String string, Object object2) {
        Button button = new Button(this.getComposite(object), 0x20 | DOUBLE_BUFFER);
        button.setText(string);
        button.setLayoutData(object2 != null ? object2 : string);
        return button;
    }

    private List createList(Object object, String string, Object object2) {
        List list = new List(this.getComposite(object), DOUBLE_BUFFER | 0x800);
        list.add(string);
        list.setLayoutData(object2);
        return list;
    }

    private StyledText createTextArea(Object object, String string, String string2) {
        return this.createTextArea(object, string, string2, 0x842 | DOUBLE_BUFFER);
    }

    private StyledText createTextArea(Object object, String string, String string2, int n) {
        StyledText styledText = new StyledText(this.getComposite(object), 0x42 | n | DOUBLE_BUFFER);
        styledText.setText(string);
        styledText.setLayoutData((Object)(string2 != null ? string2 : string));
        return styledText;
    }

    public Composite getComposite(Object object) {
        if (object instanceof Control) {
            return (Composite)object;
        }
        return (Composite)((TabItem)object).getControl();
    }

    private Font deriveFont(Control control, int n, int n2) {
        Font font = control.getFont();
        FontData fontData = font.getFontData()[0];
        if (n != -1) {
            fontData.setStyle(n);
        }
        if (n2 != -1) {
            fontData.setHeight(n2);
        }
        Font font2 = new Font((Device)display, fontData);
        control.setFont(font2);
        return font2;
    }

    private void addSeparator(Object object, String string) {
        Label label = this.createLabel(object, string, "gaptop para, span, split 2");
        label.setForeground(new Color((Device)display, 0, 70, 213));
        Label label2 = new Label(this.getComposite(object), 0x102 | DOUBLE_BUFFER);
        label2.setLayoutData((Object)"gapleft rel, gaptop para, growx");
    }

    static /* synthetic */ Control[] access$702(Control[] controlArray) {
        comps = controlArray;
        return controlArray;
    }

    static /* synthetic */ Control[] access$802(Control[] controlArray) {
        tabs = controlArray;
        return controlArray;
    }
}

