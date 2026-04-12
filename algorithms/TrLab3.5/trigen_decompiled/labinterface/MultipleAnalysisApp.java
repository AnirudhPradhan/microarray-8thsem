/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import interfaceutils.PathPicker;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import labinterface.MultipleTaskConsole;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipleAnalysisApp {
    private static final Logger LOG = LoggerFactory.getLogger(MultipleAnalysisApp.class);
    public JFrame frame;
    private JTextField txtLaboratoryApp;
    private MultipleTaskConsole console;
    private PathPicker inputFolder;
    private JButton btnAnalyse;

    public MultipleAnalysisApp() {
        this.initialize();
    }

    private void initialize() {
        this.frame = new JFrame();
        this.frame.setBounds(100, 100, 770, 700);
        this.frame.setDefaultCloseOperation(2);
        this.frame.getContentPane().setLayout(new MigLayout("", "[800.00,fill]", "[fill][50.00,fill][]"));
        this.txtLaboratoryApp = new JTextField();
        this.txtLaboratoryApp.setPreferredSize(new Dimension(50, 50));
        this.txtLaboratoryApp.setBorder(null);
        this.txtLaboratoryApp.setForeground(SystemColor.controlHighlight);
        this.txtLaboratoryApp.setHorizontalAlignment(0);
        this.txtLaboratoryApp.setBackground(SystemColor.window);
        this.txtLaboratoryApp.setEditable(false);
        this.txtLaboratoryApp.setFont(new Font("Arial", 3, 30));
        this.txtLaboratoryApp.setText("TrLab - Multiple Analysis");
        this.txtLaboratoryApp.setColumns(10);
        this.frame.getContentPane().add((Component)this.txtLaboratoryApp, "cell 0 0");
        this.inputFolder = new PathPicker(true, "Select a results folder");
        this.inputFolder.setBorder(null);
        this.frame.getContentPane().add((Component)this.inputFolder, "cell 0 1");
        this.btnAnalyse = new JButton("Analyse");
        this.btnAnalyse.setMaximumSize(new Dimension(70, 40));
        this.btnAnalyse.setFont(new Font("Arial", 0, 12));
        this.inputFolder.add(this.btnAnalyse);
        this.console = new MultipleTaskConsole();
        JScrollPane scrollMessage = new JScrollPane(this.console);
        this.frame.getContentPane().add((Component)scrollMessage, "cell 0 3");
        this.btnAnalyse.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                MultipleAnalysisApp.this.console.launch(MultipleAnalysisApp.this.inputFolder.getSelectedPath());
            }
        });
    }
}

