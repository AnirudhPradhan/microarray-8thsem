/*
 * Decompiled with CFR 0.152.
 */
package alginterface;

import alginterface.AdvancedPanel;
import alginterface.ControlPanel;
import alginterface.GuiLauncher;
import alginterface.LaunchTriGen;
import alginterface.LoadExperimentMenu;
import alginterface.SaveExperimentMenu;
import alginterface.SetWorkspaceMenu;
import input.algorithm.Control;
import input.algorithm.InvalidImplementationException;
import input.algorithm.WrongContolException;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SystemUtilities;

public class AlgApp {
    private static final Logger LOG = LoggerFactory.getLogger(AlgApp.class);
    public JFrame frame;
    private ControlPanel control_panel;
    private AdvancedPanel advanced_panel;
    private SetWorkspaceMenu setWorkspace;
    private SaveExperimentMenu savExperiment;
    private LoadExperimentMenu loadExperiment;
    private JTextField expName_v;
    private JComboBox<String> dataset_combo;
    private LaunchTriGen launchTrigen;

    public AlgApp() {
        this.initialize();
    }

    private void initialize() {
        this.frame = new JFrame();
        this.frame.getContentPane().setFont(new Font("Arial", 0, 12));
        this.frame.setBounds(100, 100, 848, 700);
        this.frame.setDefaultCloseOperation(2);
        this.frame.getContentPane().setLayout(null);
        this.expName_v = new JTextField();
        this.expName_v.setBounds(251, 601, 152, 28);
        this.frame.getContentPane().add(this.expName_v);
        this.expName_v.setColumns(10);
        this.expName_v.setText(SystemUtilities.getLabelProperty("exp_name_default"));
        this.setWorkspace = new SetWorkspaceMenu();
        this.setWorkspace.setVisible(false);
        this.savExperiment = new SaveExperimentMenu(this.expName_v.getText());
        this.savExperiment.setVisible(false);
        this.loadExperiment = new LoadExperimentMenu();
        this.loadExperiment.setVisible(false);
        JTabbedPane tabbedPane = new JTabbedPane(1);
        tabbedPane.setBounds(0, 90, 842, 487);
        this.frame.getContentPane().add(tabbedPane);
        this.control_panel = new ControlPanel();
        tabbedPane.addTab(SystemUtilities.getLabelProperty("control_tab_tag"), null, this.control_panel, null);
        this.dataset_combo = this.control_panel.getDatasetCombo();
        this.advanced_panel = new AdvancedPanel(this.dataset_combo);
        tabbedPane.addTab(SystemUtilities.getLabelProperty("advanced_tab_tag"), null, this.advanced_panel, null);
        JMenuBar menu_bar = new JMenuBar();
        menu_bar.setBorder(new LineBorder(SystemColor.BLACK, 1, true));
        menu_bar.setBounds(0, 0, 174, 22);
        this.frame.getContentPane().add(menu_bar);
        JMenu load_menu = new JMenu(SystemUtilities.getLabelProperty("load_menu_tag"));
        load_menu.setFont(new Font("Arial", 1, 14));
        menu_bar.add(load_menu);
        JMenuItem mntmLoadExperiment = new JMenuItem(SystemUtilities.getLabelProperty("load_experiment_menu_tag"));
        mntmLoadExperiment.setFont(new Font("Arial", 1, 14));
        load_menu.add(mntmLoadExperiment);
        mntmLoadExperiment.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                AlgApp.this.loadExperiment.resetText();
                AlgApp.this.loadExperiment.setExpName(AlgApp.this.expName_v);
                AlgApp.this.loadExperiment.setWorkspaceMenu(AlgApp.this.setWorkspace);
                AlgApp.this.loadExperiment.setControlPanel(AlgApp.this.control_panel);
                AlgApp.this.loadExperiment.setAdvancedPanel(AlgApp.this.advanced_panel);
                AlgApp.this.loadExperiment.setVisible(true);
            }
        });
        JMenu mnSave = new JMenu(SystemUtilities.getLabelProperty("save_menu_tag"));
        mnSave.setFont(new Font("Arial", 1, 14));
        menu_bar.add(mnSave);
        JMenuItem mntmNewMenuItem = new JMenuItem(SystemUtilities.getLabelProperty("save_experiment_menu_tag"));
        mntmNewMenuItem.setFont(new Font("Arial", 1, 14));
        mnSave.add(mntmNewMenuItem);
        mntmNewMenuItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                AlgApp.this.savExperiment.resetText();
                AlgApp.this.savExperiment.setExpName(AlgApp.this.expName_v.getText());
                AlgApp.this.savExperiment.setControl_panel(AlgApp.this.control_panel);
                AlgApp.this.savExperiment.setAdvanced_panel(AlgApp.this.advanced_panel);
                AlgApp.this.savExperiment.setWorkspacePath(AlgApp.this.setWorkspace.getSelectedPath());
                AlgApp.this.savExperiment.setVisible(true);
            }
        });
        JMenu mnSettings = new JMenu(SystemUtilities.getLabelProperty("settings_menu_tag"));
        mnSettings.setFont(new Font("Arial", 1, 14));
        menu_bar.add(mnSettings);
        JMenuItem mntmSetWorkspace = new JMenuItem(SystemUtilities.getLabelProperty("set_workspace_menu_tag"));
        mntmSetWorkspace.setFont(new Font("Arial", 1, 14));
        mnSettings.add(mntmSetWorkspace);
        mntmSetWorkspace.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                AlgApp.this.setWorkspace.setVisible(true);
            }
        });
        menu_bar.validate();
        JButton launch_button = new JButton(SystemUtilities.getLabelProperty("launch_button"));
        launch_button.setBounds(529, 602, 117, 29);
        this.frame.getContentPane().add(launch_button);
        JLabel expName_l = new JLabel(SystemUtilities.getLabelProperty("experimet_name_tag"));
        expName_l.setBounds(48, 607, 198, 16);
        this.frame.getContentPane().add(expName_l);
        JTextPane txtpnTrlabAlgapp = new JTextPane();
        txtpnTrlabAlgapp.setText("TrLab - TriGen");
        txtpnTrlabAlgapp.setPreferredSize(new Dimension(100, 100));
        txtpnTrlabAlgapp.setBorder(null);
        txtpnTrlabAlgapp.setForeground(SystemColor.controlHighlight);
        txtpnTrlabAlgapp.setBackground(SystemColor.window);
        txtpnTrlabAlgapp.setEditable(false);
        txtpnTrlabAlgapp.setFont(new Font("Arial", 3, 30));
        txtpnTrlabAlgapp.setBounds(310, 20, 225, 40);
        this.frame.getContentPane().add(txtpnTrlabAlgapp);
        this.launchTrigen = new LaunchTriGen();
        launch_button.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Control control = null;
                try {
                    control = GuiLauncher.buidGuiControl(AlgApp.this.control_panel, AlgApp.this.advanced_panel, AlgApp.this.setWorkspace.getSelectedPath(), AlgApp.this.expName_v.getText());
                }
                catch (InvalidImplementationException | WrongContolException e1) {
                    e1.printStackTrace();
                }
                AlgApp.this.launchTrigen.setVisible(true);
                AlgApp.this.launchTrigen.launch(control);
            }
        });
    }
}

