/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import labinterface.TriGraphs;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

public class GuiSolution
extends JTabbedPane {
    TriGraphs tricluster;
    private JPanel gctPanel;
    private JPanel gtcPpanel;
    private JPanel tgcPanel;

    public GuiSolution(TriGraphs tri) {
        super(1);
        XChartPanel<XYChart> pnlChart;
        this.tricluster = tri;
        this.setBounds(100, 100, 523, 457);
        int with = 400;
        int heigh = 200;
        this.gctPanel = new JPanel();
        this.gctPanel.setLayout(new BoxLayout(this.gctPanel, 1));
        for (XYChart c2 : this.tricluster.getGCTcharts()) {
            pnlChart = new XChartPanel<XYChart>(c2);
            pnlChart.setMinimumSize(new Dimension(with, heigh));
            pnlChart.setMaximumSize(new Dimension(with, heigh));
            this.gctPanel.add(pnlChart);
            this.gctPanel.validate();
        }
        this.add((Component)this.gctPanel, "GCT view");
        this.gtcPpanel = new JPanel();
        this.gtcPpanel.setLayout(new BoxLayout(this.gtcPpanel, 1));
        for (XYChart c2 : this.tricluster.getGTCcharts()) {
            pnlChart = new XChartPanel<XYChart>(c2);
            pnlChart.setMinimumSize(new Dimension(with, heigh));
            pnlChart.setMaximumSize(new Dimension(with, heigh));
            this.gtcPpanel.add(pnlChart);
            this.gtcPpanel.validate();
        }
        this.add((Component)this.gtcPpanel, "GTC view");
        this.tgcPanel = new JPanel();
        this.tgcPanel.setLayout(new BoxLayout(this.tgcPanel, 1));
        for (XYChart c2 : this.tricluster.getTGCcharts()) {
            pnlChart = new XChartPanel<XYChart>(c2);
            pnlChart.setMinimumSize(new Dimension(with, heigh));
            pnlChart.setMaximumSize(new Dimension(with, heigh));
            this.tgcPanel.add(pnlChart);
            this.tgcPanel.validate();
        }
        this.add((Component)this.tgcPanel, "TGC view");
        this.setVisible(false);
    }
}

