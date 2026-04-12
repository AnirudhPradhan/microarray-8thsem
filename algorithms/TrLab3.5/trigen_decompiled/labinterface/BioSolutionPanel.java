/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import analysis.Solution;
import analysis.biological.GoSlot;
import input.datasets.Common;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumnModel;
import labinterface.GoTableModel;
import labinterface.GraphicsBuilder;
import labinterface.PvalueRender;
import labinterface.TriGraphs;
import net.miginfocom.swing.MigLayout;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TextUtilities;

public class BioSolutionPanel
extends JPanel {
    private static final Logger LOG = LoggerFactory.getLogger(BioSolutionPanel.class);
    private JTextField solTag;
    private JTabbedPane solutionAnalysisTab;
    private JPanel graphicAnalysisPanel;
    private JPanel bioAnalysisPanel;
    private Solution solution;
    private Common dataset;

    public BioSolutionPanel(Solution sol, Common data) {
        this.solution = sol;
        this.dataset = data;
        this.initializeGUI();
        this.buildGraphPanels();
        this.buildBioPanels();
    }

    private void buildBioPanels() {
        this.bioAnalysisPanel.setLayout(new MigLayout("", "[fill]40[]", "[fill][fill]"));
        if (this.solution.getGoStudy() == null) {
            JTextField noData = new JTextField("No Gene Ontology data available (GO associations not configured).");
            noData.setEditable(false);
            noData.setBackground(SystemColor.window);
            this.bioAnalysisPanel.add(noData, "span");
            return;
        }
        JTable goTable = new JTable(new GoTableModel(this.solution.getGoStudy()));
        goTable.setAutoCreateRowSorter(true);
        goTable.setAutoResizeMode(0);
        TableColumnModel columnModel = goTable.getColumnModel();
        columnModel.getColumn(0).setMinWidth(90);
        columnModel.getColumn(1).setMinWidth(320);
        columnModel.getColumn(2).setMinWidth(110);
        columnModel.getColumn(3).setMinWidth(110);
        columnModel.getColumn(2).setCellRenderer(PvalueRender.getInstance());
        columnModel.getColumn(3).setCellRenderer(PvalueRender.getInstance());
        JScrollPane tScroll = new JScrollPane(goTable);
        tScroll.setMinimumSize(new Dimension(550, 500));
        List<GoSlot> sig = this.solution.getGoSlots("pa");
        if (sig == null || sig.isEmpty()) {
            JTextField noSig = new JTextField("No significance data available.");
            noSig.setEditable(false);
            noSig.setBackground(SystemColor.window);
            this.bioAnalysisPanel.add((Component)tScroll, "cell 0 0");
            this.bioAnalysisPanel.add(noSig, "cell 0 1");
            return;
        }
        GraphicsBuilder gr = GraphicsBuilder.getInstance();
        List<CategoryChart> charts = gr.buildSignificanceSolutionGraph(sig, this.solution.getIndex());
        XChartPanel<Chart> pnlConcentrationChart = new XChartPanel<Chart>(charts.get(0));
        XChartPanel<Chart> pnlItemChart = new XChartPanel<Chart>(charts.get(1));
        this.bioAnalysisPanel.add(pnlConcentrationChart, "cell 0 0");
        this.bioAnalysisPanel.add((Component)tScroll, "span 1 2");
        this.bioAnalysisPanel.add(pnlItemChart, "cell 0 1");
    }

    private void initializeGUI() {
        this.setLayout(new BorderLayout());
        JPanel digestPanel = new JPanel();
        this.add((Component)digestPanel, "North");
        this.solTag = new JTextField();
        DecimalFormat format = TextUtilities.getDecimalFormat('.');
        double triq = this.solution.getValue("triq");
        double bioq = this.solution.getValue("bioq");
        double peq = this.solution.getValue("peq");
        double spq = this.solution.getValue("spq");
        String aux = "Solution " + this.solution.getIndex() + " , TRIQ = " + format.format(triq) + " , BIOQ = " + format.format(bioq) + " , PEQ = " + format.format(peq) + " , SPQ = " + format.format(spq);
        this.solTag.setText(aux);
        this.solTag.setBackground(SystemColor.window);
        this.solTag.setForeground(SystemColor.controlHighlight);
        this.solTag.setFont(new Font("Arial", 3, 20));
        this.solTag.setEditable(false);
        this.solTag.setBorder(null);
        digestPanel.add(this.solTag);
        this.solTag.setColumns(40);
        this.solutionAnalysisTab = new JTabbedPane(1);
        this.add((Component)this.solutionAnalysisTab, "Center");
        this.graphicAnalysisPanel = new JPanel();
        this.solutionAnalysisTab.addTab("Graphical Analysis", null, this.graphicAnalysisPanel, null);
        this.bioAnalysisPanel = new JPanel();
        this.solutionAnalysisTab.addTab("Biological Analysis", null, this.bioAnalysisPanel, null);
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

