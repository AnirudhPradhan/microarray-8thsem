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
import labinterface.SingleTaskConsole;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleAnalysisApp {
    private static final Logger LOG = LoggerFactory.getLogger(SingleAnalysisApp.class);
    public JFrame frame;
    private JTextField txtLaboratoryApp;
    private PathPicker inputExperiment;
    private JButton btnAnalyse;
    private SingleTaskConsole console;

    public SingleAnalysisApp() {
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
        this.txtLaboratoryApp.setText("TrLab - Single Analysis");
        this.txtLaboratoryApp.setColumns(10);
        this.frame.getContentPane().add((Component)this.txtLaboratoryApp, "cell 0 0");
        this.inputExperiment = new PathPicker("Select a solution file");
        this.inputExperiment.setBorder(null);
        this.frame.getContentPane().add((Component)this.inputExperiment, "cell 0 1");
        this.btnAnalyse = new JButton("Analyse");
        this.btnAnalyse.setFont(new Font("Arial", 0, 12));
        this.inputExperiment.add(this.btnAnalyse);
        this.console = new SingleTaskConsole();
        JScrollPane scrollMessage = new JScrollPane(this.console);
        this.frame.getContentPane().add((Component)scrollMessage, "cell 0 3");
        this.btnAnalyse.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String path = SingleAnalysisApp.this.inputExperiment.getSelectedPath();
                if (path == null || path.trim().isEmpty()) {
                    SingleAnalysisApp.this.console.showMessage("Please enter or select a .sol file path (use Explore button).");
                    return;
                }
                if (!path.trim().toLowerCase().endsWith(".sol")) {
                    SingleAnalysisApp.this.console.showMessage("File must be a .sol file. Use Explore to select a solution file.");
                    return;
                }
                SingleAnalysisApp.this.console.launch(path.trim());
            }
        });
    }
}

