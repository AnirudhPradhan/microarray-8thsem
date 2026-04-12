/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import analysis.Experiment;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import labinterface.GraphicsBuilder;
import net.miginfocom.swing.MigLayout;
import org.knowm.xchart.BubbleChart;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.internal.chartpart.Chart;

public class ExpCompGraphFrame
extends JFrame {
    private JPanel contentPane;
    private List<Experiment> experiments;

    public ExpCompGraphFrame(List<Experiment> experiments) {
        this.experiments = experiments;
        this.setDefaultCloseOperation(2);
        this.setBounds(20, 20, 1400, 420);
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.contentPane);
        this.contentPane.setLayout(new MigLayout("", "[fill]10[fill]10[fill]40[fill]", "[fill]"));
        this.buildCharts();
    }

    private void buildCharts() {
        GraphicsBuilder gr = GraphicsBuilder.getInstance();
        List<CategoryChart> lc = gr.buildCompCategoryCharts(this.experiments);
        XChartPanel<Chart> p1 = new XChartPanel<Chart>(lc.get(0));
        XChartPanel<Chart> p2 = new XChartPanel<Chart>(lc.get(1));
        XChartPanel<Chart> p3 = new XChartPanel<Chart>(lc.get(2));
        BubbleChart c1 = gr.buildBubbleChart(this.experiments);
        XChartPanel<BubbleChart> p4 = new XChartPanel<BubbleChart>(c1);
        this.contentPane.add(p1, "cell 0 0");
        this.contentPane.add(p2, "cell 1 0");
        this.contentPane.add(p3, "cell 2 0");
        this.contentPane.add(p4, "cell 3 0");
    }
}

