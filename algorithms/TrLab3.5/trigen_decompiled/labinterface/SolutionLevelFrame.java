/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import analysis.Experiment;
import analysis.Solution;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import labinterface.BioSolutionPanel;
import labinterface.CommonSolutionPanel;
import labinterface.SolutionButtonMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolutionLevelFrame
extends JFrame {
    private static final Logger LOG = LoggerFactory.getLogger(SolutionLevelFrame.class);
    private JPanel contentPane;
    private JPanel expDigestPanel;
    private JPanel solutionPanel;
    private SolutionButtonMenu buttonMenu;
    private Experiment experiment;

    public SolutionLevelFrame(Experiment exp) {
        this.experiment = exp;
        this.initializeGUI();
    }

    private void initializeGUI() {
        this.setDefaultCloseOperation(2);
        int w = 1500;
        int h = 800;
        this.setBounds(5, 0, w, h);
        this.setMaximumSize(new Dimension(w, h));
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.contentPane.setLayout(new BorderLayout(0, 0));
        this.contentPane.setMaximumSize(new Dimension(w, h));
        JScrollPane scrollPane = new JScrollPane(this.contentPane);
        scrollPane.setMaximumSize(new Dimension(w, h));
        this.getContentPane().add(scrollPane);
        this.expDigestPanel = new JPanel();
        this.contentPane.add((Component)this.expDigestPanel, "North");
        int unitW = 300;
        int unitL = 400;
        this.solutionPanel = new JPanel();
        this.solutionPanel.setMaximumSize(new Dimension(unitW, unitL));
        this.contentPane.add((Component)this.solutionPanel, "Center");
        List<Solution> sols = this.experiment.getSolutions();
        for (Solution s : sols) {
            JPanel sp;
            if (this.experiment.getAnalysisType() == 'b') {
                sp = new BioSolutionPanel(s, this.experiment.getDataset());
                this.solutionPanel.add(sp);
                continue;
            }
            if (this.experiment.getAnalysisType() != 'c') continue;
            sp = new CommonSolutionPanel(s, this.experiment.getDataset());
            this.solutionPanel.add(sp);
        }
        this.buttonMenu = new SolutionButtonMenu(this.solutionPanel, ((Solution)this.experiment.getValue("bestsolution")).getIndex());
        this.contentPane.add((Component)this.buttonMenu, "West");
    }

    public void displaySolution(int i) {
        this.setVisible(true);
        this.buttonMenu.showSolution(i);
    }
}

