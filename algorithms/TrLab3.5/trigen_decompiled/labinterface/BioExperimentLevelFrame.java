/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import analysis.Experiment;
import analysis.Solution;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import labinterface.BioExpTableModel;
import labinterface.GraphicsBuilder;
import labinterface.SolutionLevelFrame;
import net.miginfocom.swing.MigLayout;
import org.knowm.xchart.BubbleChart;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.internal.chartpart.Chart;
import utils.TextUtilities;

public class BioExperimentLevelFrame
extends JFrame {
    private static final int W = 1500;
    private static final int H = 800;
    private Experiment experiment;
    private JPanel contentPane;
    private SolutionLevelFrame solutionLevelFrame;

    public BioExperimentLevelFrame(Experiment experiment) {
        this.experiment = experiment;
        this.initializeGUI();
        this.buildSolutionLevelFrame();
        this.buildSummaryGraphs();
    }

    private void buildSolutionLevelFrame() {
        this.solutionLevelFrame = new SolutionLevelFrame(this.experiment);
        this.solutionLevelFrame.setVisible(false);
    }

    private void buildSummaryGraphs() {
        List<Solution> solutions = this.experiment.getSolutions();
        JTable triqTable = new JTable(new BioExpTableModel(solutions));
        triqTable.setAutoCreateRowSorter(true);
        triqTable.setAutoResizeMode(4);
        triqTable.setFont(new Font("Monospaced", 0, 14));
        triqTable.setFillsViewportHeight(true);
        triqTable.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable)me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                int index = table.convertRowIndexToModel(row);
                if (me.getClickCount() == 2) {
                    BioExperimentLevelFrame.this.solutionLevelFrame.displaySolution(index);
                }
            }
        });
        JScrollPane tScroll = new JScrollPane(triqTable);
        tScroll.validate();
        tScroll.setMaximumSize(new Dimension(550, 340));
        JTextPane expTag = new JTextPane();
        DecimalFormat format = TextUtilities.getDecimalFormat('.');
        String expName = this.experiment.getExperimentName();
        String datasetName = this.experiment.getDatasetName();
        String bestSolution = ((Solution)this.experiment.getValue("bestsolution")).getName();
        double bestTriq = (Double)this.experiment.getValue("besttriq");
        double mean = (Double)this.experiment.getValue("mean");
        double stdev = (Double)this.experiment.getValue("stdev");
        String aux = "Experiment : " + expName + " , Dataset : " + datasetName + "\n\nBest solution " + bestSolution + " ( TRIQ = " + format.format(bestTriq) + " ) \n\nMean TRIQ = " + format.format(mean) + " , Stdev TRIQ = " + format.format(stdev);
        expTag.setText(aux);
        expTag.setBackground(SystemColor.window);
        expTag.setForeground(SystemColor.controlHighlight);
        expTag.setFont(new Font("Arial", 3, 15));
        expTag.setEditable(false);
        expTag.setBorder(null);
        JButton solLevelButton = new JButton("Solution Level Menu");
        solLevelButton.setFont(new Font("Arial", 3, 14));
        solLevelButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                BioExperimentLevelFrame.this.solutionLevelFrame.setVisible(true);
            }
        });
        GraphicsBuilder gr = GraphicsBuilder.getInstance();
        List<CategoryChart> lc = gr.buildSummaryGraphs(solutions, this.experiment.getAnalysisType());
        XChartPanel<Chart> p1 = new XChartPanel<Chart>(lc.get(0));
        XChartPanel<Chart> p2 = new XChartPanel<Chart>(lc.get(1));
        XChartPanel<Chart> p3 = new XChartPanel<Chart>(lc.get(2));
        XChartPanel<Chart> p4 = new XChartPanel<Chart>(lc.get(3));
        XChartPanel<Chart> p5 = new XChartPanel<Chart>(lc.get(4));
        List<BubbleChart> ls = gr.buildSummaryBubbleCharts(solutions);
        XChartPanel<Chart> p10 = new XChartPanel<Chart>(ls.get(0));
        XChartPanel<Chart> p11 = new XChartPanel<Chart>(ls.get(1));
        this.contentPane.add(p1, "cell 0 0");
        this.contentPane.add(p10, "cell 1 0,align left");
        this.contentPane.add(p11, "cell 2 0,align left");
        this.contentPane.add(p2, "cell 0 1");
        this.contentPane.add((Component)expTag, "cell 1 1,align left,gaptop 10");
        this.contentPane.add((Component)solLevelButton, "cell 2 1,align left,gaptop 10");
        this.contentPane.add(p3, "cell 0 2");
        this.contentPane.add((Component)tScroll, "span 2 4,align left");
        this.contentPane.add(p4, "cell 0 3");
        this.contentPane.add(p5, "cell 0 4");
    }

    private void initializeGUI() {
        this.setDefaultCloseOperation(2);
        this.setBounds(5, 0, 1500, 800);
        this.setMaximumSize(new Dimension(1500, 800));
        this.contentPane = new JPanel();
        this.contentPane.setBackground(SystemColor.window);
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        JScrollPane scrollPane = new JScrollPane(this.contentPane);
        this.getContentPane().add(scrollPane);
        this.contentPane.setLayout(new MigLayout("", "[]80[]10[]", "[][][][][]"));
    }
}

