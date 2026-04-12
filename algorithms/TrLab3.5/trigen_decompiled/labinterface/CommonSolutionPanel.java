/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import analysis.Solution;
import input.datasets.Common;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTextField;
import labinterface.GraphicsBuilder;
import labinterface.TriGraphs;
import net.miginfocom.swing.MigLayout;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TextUtilities;

public class CommonSolutionPanel
extends JPanel {
    private static final Logger LOG = LoggerFactory.getLogger(CommonSolutionPanel.class);
    private JTextField solTag;
    private JPanel graphicAnalysisPanel;
    private Solution solution;
    private Common dataset;

    public CommonSolutionPanel(Solution sol, Common data) {
        this.solution = sol;
        this.dataset = data;
        this.initializeGUI();
        this.buildGraphPanels();
    }

    private void initializeGUI() {
        this.setLayout(new BorderLayout());
        JPanel digestPanel = new JPanel();
        this.add((Component)digestPanel, "North");
        this.solTag = new JTextField();
        DecimalFormat format = TextUtilities.getDecimalFormat('.');
        double triq = this.solution.getValue("triq");
        double peq = this.solution.getValue("peq");
        double spq = this.solution.getValue("spq");
        String aux = "Solution " + this.solution.getIndex() + " , TRIQ = " + format.format(triq) + " , PEQ = " + format.format(peq) + " , SPQ = " + format.format(spq);
        this.solTag.setText(aux);
        this.solTag.setBackground(SystemColor.window);
        this.solTag.setForeground(SystemColor.controlHighlight);
        this.solTag.setFont(new Font("Arial", 3, 20));
        this.solTag.setEditable(false);
        this.solTag.setBorder(null);
        digestPanel.add(this.solTag);
        this.solTag.setColumns(40);
        this.graphicAnalysisPanel = new JPanel();
        this.add((Component)this.graphicAnalysisPanel, "Center");
        this.setVisible(false);
    }

    private void buildGraphPanels() {
        String loc;
        XChartPanel<XYChart> pnlChart;
        int i;
        GraphicsBuilder gr = GraphicsBuilder.getInstance();
        TriGraphs graphs = gr.buildTriclusterGraphs(this.solution.getTricluster(), this.dataset);
        List<Integer> sizes = Arrays.asList(graphs.getGCTcharts().size(), graphs.getGTCcharts().size(), graphs.getTGCcharts().size());
        int rows = (Integer)sizes.stream().max(Comparator.naturalOrder()).get();
        String rc = "";
        for (i = 0; i < rows; ++i) {
            rc = rc + "[]";
        }
        this.graphicAnalysisPanel.setLayout(new MigLayout("", "[][][]", rc));
        i = 0;
        for (XYChart c2 : graphs.getGCTcharts()) {
            pnlChart = new XChartPanel<XYChart>(c2);
            loc = "cell 0 " + i;
            this.graphicAnalysisPanel.add(pnlChart, loc);
            ++i;
        }
        i = 0;
        for (XYChart c2 : graphs.getGTCcharts()) {
            pnlChart = new XChartPanel<XYChart>(c2);
            loc = "cell 1 " + i;
            this.graphicAnalysisPanel.add(pnlChart, loc);
            ++i;
        }
        i = 0;
        for (XYChart c2 : graphs.getTGCcharts()) {
            pnlChart = new XChartPanel<XYChart>(c2);
            loc = "cell 2 " + i;
            this.graphicAnalysisPanel.add(pnlChart, loc);
            ++i;
        }
    }
}

