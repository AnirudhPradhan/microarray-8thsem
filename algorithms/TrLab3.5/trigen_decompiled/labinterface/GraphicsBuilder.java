/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import analysis.Experiment;
import analysis.Solution;
import analysis.biological.GoInterval;
import analysis.biological.GoSignificance;
import analysis.biological.GoSlot;
import general.Tricluster;
import input.datasets.Common;
import input.datasets.Real;
import input.laboratory.AnalysisResources;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import labinterface.GuiSolution;
import labinterface.TriGraphs;
import labutils.Conversions;
import org.knowm.xchart.BubbleChart;
import org.knowm.xchart.BubbleChartBuilder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.BubbleStyler;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.XYStyler;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TextUtilities;

public class GraphicsBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(GraphicsBuilder.class);
    private static final double CONT_EXP_1 = 0.98;
    private static final int W_EXP_1 = 350;
    private static final int H_EXP_1 = 145;
    private static final double CONT_EXP_2 = 0.98;
    private static final int W_EXP_2 = 350;
    private static final int H_EXP_2 = 145;
    private static final double CONT_EXP_COMP_1 = 0.98;
    private static final int W_EXP_COMP_1 = 1000;
    private static final int H_EXP_COMP_1 = 700;
    private static final double CONT_EXP_COMP_2 = 0.85;
    private static final int W_EXP_COMP_2 = 1000;
    private static final int H_EXP_COMP_2 = 800;
    private static final double CONT_SUMMARY = 0.98;
    private static final int W_SUMMARY = 550;
    private static final int H_SUMMARY = 145;
    private static final double CONT_SIG = 0.8;
    private static final int W_SIG = 580;
    private static final int H_SIG = 300;
    private static final double CONT_GCT = 0.7;
    private static final int W_GCT = 420;
    private static final int H_GCT = 170;
    private static final double CONT_GTC = 0.7;
    private static final int W_GTC = 420;
    private static final int H_GTC = 170;
    private static final double CONT_TGC = 0.7;
    private static final int W_TGC = 400;
    private static final int H_TGC = 170;
    private static GraphicsBuilder singleton = new GraphicsBuilder();

    private GraphicsBuilder() {
    }

    public static GraphicsBuilder getInstance() {
        return singleton;
    }

    public List<BubbleChart> buildSummaryBubbleCharts(List<Solution> solutions) {
        ArrayList<BubbleChart> charts = new ArrayList<BubbleChart>(2);
        double[] grq = new double[solutions.size()];
        double[] bioq = new double[solutions.size()];
        double[] peq = new double[solutions.size()];
        double[] spq = new double[solutions.size()];
        double[] triq = new double[solutions.size()];
        int i = 0;
        double shift = 10.0;
        for (Solution sol : solutions) {
            grq[i] = sol.getValue("grq");
            bioq[i] = sol.getValue("bioq");
            peq[i] = sol.getValue("peq");
            spq[i] = sol.getValue("spq");
            triq[i] = sol.getValue("triq") * shift;
            ++i;
        }
        BubbleChart chart1 = this.getBubbleChart("BIOQ vs GRQ volume TRIQ", 350, 145, "bioq", "grq", 0.98, Color.GREEN);
        chart1.addSeries("bioq-grq", bioq, grq, triq);
        charts.add(chart1);
        BubbleChart chart2 = this.getBubbleChart("PEQ vs SPQ volume TRIQ", 350, 145, "peq", "spq", 0.98, Color.cyan);
        chart2.addSeries("peq-spq", peq, spq, triq);
        charts.add(chart2);
        return charts;
    }

    public List<XYChart> buildSummaryScatterCharts(List<Solution> solutions) {
        ArrayList<XYChart> charts = new ArrayList<XYChart>(2);
        double[] grq = new double[solutions.size()];
        double[] peq = new double[solutions.size()];
        double[] spq = new double[solutions.size()];
        double[] triq = new double[solutions.size()];
        int i = 0;
        double shift = 1.0;
        for (Solution sol : solutions) {
            grq[i] = sol.getValue("grq");
            peq[i] = sol.getValue("peq");
            spq[i] = sol.getValue("spq");
            triq[i] = sol.getValue("triq") * shift;
            ++i;
        }
        XYChart chart1 = this.getScatterChart("TRIQ-GRQ distribution", 350, 145, "triq", "grq", 0.98, Color.ORANGE);
        chart1.addSeries("triq-grq", triq, grq);
        charts.add(chart1);
        XYChart chart2 = this.getScatterChart("PEQ-SPQ distribution", 350, 145, "peq", "spq", 0.98, Color.GREEN);
        chart2.addSeries("peq-spq", peq, spq);
        charts.add(chart2);
        return charts;
    }

    public List<CategoryChart> buildCompCategoryCharts(List<Experiment> experiments) {
        ArrayList<CategoryChart> charts = new ArrayList<CategoryChart>(3);
        ArrayList<String> xAxis = new ArrayList<String>(experiments.size());
        ArrayList<Double> meanValues = new ArrayList<Double>(experiments.size());
        ArrayList<Double> stdevValues = new ArrayList<Double>(experiments.size());
        ArrayList<Double> bestTriqValues = new ArrayList<Double>(experiments.size());
        for (Experiment e : experiments) {
            xAxis.add(e.getExperimentName());
            meanValues.add((Double)e.getValue("mean"));
            stdevValues.add((Double)e.getValue("stdev"));
            bestTriqValues.add((Double)e.getValue("besttriq"));
        }
        CategoryChart meanChart = this.getCategoryChart("MEAN", 1000, 700, "", Color.ORANGE, 90);
        meanChart.addSeries("mean", xAxis, meanValues);
        charts.add(meanChart);
        CategoryChart stdevChart = this.getCategoryChart("STDEV", 1000, 700, "", Color.MAGENTA, 90);
        stdevChart.addSeries("stdev", xAxis, stdevValues);
        charts.add(stdevChart);
        CategoryChart bestTriqChart = this.getCategoryChart("BEST TRIQ", 1000, 700, "", Color.CYAN, 90);
        bestTriqChart.addSeries("triq values", xAxis, bestTriqValues);
        charts.add(bestTriqChart);
        return charts;
    }

    public BubbleChart buildBubbleChart(List<Experiment> experiments) {
        BubbleChart chart = this.getBubbleChart("SUMMARY", 1000, 800, "mean triq", "stdev", 0.85, Color.GREEN);
        double[] x = new double[experiments.size()];
        double[] y = new double[experiments.size()];
        double[] bubble = new double[experiments.size()];
        int i = 0;
        double bubbleShift = 10.0;
        for (Experiment e : experiments) {
            x[i] = (Double)e.getValue("mean");
            y[i] = (Double)e.getValue("stdev");
            bubble[i] = (Double)e.getValue("besttriq") * bubbleShift;
            ++i;
        }
        chart.addSeries("Summary", x, y, bubble);
        return chart;
    }

    public List<CategoryChart> buildSummaryGraphs(List<Solution> solutions, char analisysType) {
        ArrayList<CategoryChart> charts = new ArrayList<CategoryChart>(4);
        ArrayList<String> xAxis = new ArrayList<String>(solutions.size());
        ArrayList<Double> triqValues = new ArrayList<Double>(solutions.size());
        ArrayList<Double> bioqValues = null;
        if (analisysType == 'b') {
            bioqValues = new ArrayList<Double>(solutions.size());
        }
        ArrayList<Double> grqValues = new ArrayList<Double>(solutions.size());
        ArrayList<Double> peqValues = new ArrayList<Double>(solutions.size());
        ArrayList<Double> spqValues = new ArrayList<Double>(solutions.size());
        double shift = 1.0;
        for (Solution sol : solutions) {
            xAxis.add("" + sol.getIndex());
            triqValues.add(new Double(sol.getValue("triq") * shift));
            if (analisysType == 'b') {
                bioqValues.add(new Double(sol.getValue("bioq") * shift));
            }
            grqValues.add(new Double(sol.getValue("grq") * shift));
            peqValues.add(new Double(sol.getValue("peq") * shift));
            spqValues.add(new Double(sol.getValue("spq") * shift));
        }
        CategoryChart triqChart = this.getCategoryChart("TRIQ values", 550, 145, "solutions", Color.BLUE);
        triqChart.addSeries("triq values", xAxis, triqValues);
        charts.add(triqChart);
        if (analisysType == 'b') {
            CategoryChart bioqChart = this.getCategoryChart("BIOQ values", 550, 145, "solutions", Color.GREEN);
            bioqChart.addSeries("bioq values", xAxis, bioqValues);
            charts.add(bioqChart);
        }
        CategoryChart qrqChart = this.getCategoryChart("GRQ values", 550, 145, "solutions", Color.ORANGE);
        qrqChart.addSeries("qrq values", xAxis, grqValues);
        charts.add(qrqChart);
        CategoryChart peqChart = this.getCategoryChart("PEQ values", 550, 145, "solutions", Color.CYAN);
        peqChart.addSeries("peq values", xAxis, peqValues);
        charts.add(peqChart);
        CategoryChart spqChart = this.getCategoryChart("SPQ values", 550, 145, "solutions", Color.MAGENTA);
        spqChart.addSeries("spq values", xAxis, spqValues);
        charts.add(spqChart);
        return charts;
    }

    public List<CategoryChart> buildSignificanceSolutionGraph(List<GoSlot> significance, int solIndex) {
        ArrayList<CategoryChart> charts = new ArrayList<CategoryChart>(2);
        List<GoInterval> intervals = GoSignificance.getInstance().getINTERVALS();
        ArrayList<String> xAxis = new ArrayList<String>(intervals.size());
        for (GoInterval in : intervals) {
            xAxis.add("" + in.getLevel());
        }
        ArrayList<Double> concentrationValues = new ArrayList<Double>(significance.size());
        ArrayList<Integer> itemValues = new ArrayList<Integer>(significance.size());
        for (GoSlot sl : significance) {
            concentrationValues.add(new Double(sl.getConcentration()));
            itemValues.add(new Integer(sl.getCount()));
        }
        Collections.reverse(xAxis);
        Collections.reverse(concentrationValues);
        Collections.reverse(itemValues);
        LOG.debug(((Object)xAxis).toString());
        LOG.debug(((Object)concentrationValues).toString());
        LOG.debug(((Object)itemValues).toString());
        CategoryChart concentrationChart = this.getCategoryChart("Solution " + solIndex + " Biological Significance - Concentration", 580, 300, "levels", "term concentration", Color.BLUE, false, 0.0, 1.0);
        concentrationChart.addSeries("term concentration", xAxis, concentrationValues);
        CategoryChart itemChart = this.getCategoryChart("Solution " + solIndex + " Biological Significance - Item Count", 580, 300, "levels", "item count", Color.GREEN, true, 0.0, -1.0);
        itemChart.addSeries("item count", xAxis, itemValues);
        charts.add(concentrationChart);
        charts.add(itemChart);
        return charts;
    }

    public TriGraphs buildTriclusterGraphs(Tricluster tricluster, Common dataset) {
        TriGraphs guiTri = new TriGraphs(tricluster);
        LOG.debug("g-c-t");
        guiTri.setGCTcharts(this.buildChartsCathegory(guiTri.getTricluster(), dataset, "g-c-t"));
        LOG.debug("g-t-c");
        guiTri.setGTCcharts(this.buildChartsCathegory(guiTri.getTricluster(), dataset, "g-t-c"));
        LOG.debug("t-g-c");
        guiTri.setTGCcharts(this.buildChartsTGC(guiTri.getTricluster(), dataset));
        return guiTri;
    }

    public List<TriGraphs> buildGuiSolutions(List<Tricluster> triclusters, Common dataset) {
        ArrayList<TriGraphs> r = new ArrayList<TriGraphs>(triclusters.size());
        int i = 0;
        for (Tricluster tri : triclusters) {
            TriGraphs guiTri = new TriGraphs(tri);
            LOG.debug("Tricluster " + i);
            LOG.debug("g-c-t");
            guiTri.setGCTcharts(this.buildChartsCathegory(guiTri.getTricluster(), dataset, "g-c-t"));
            LOG.debug("g-t-c");
            guiTri.setGTCcharts(this.buildChartsCathegory(guiTri.getTricluster(), dataset, "g-t-c"));
            LOG.debug("t-g-c");
            guiTri.setTGCcharts(this.buildChartsTGC(guiTri.getTricluster(), dataset));
            r.add(guiTri);
            ++i;
        }
        return r;
    }

    public List<GuiSolution> buildGuiSolutions(AnalysisResources currentSolutions) {
        List<Tricluster> sols = currentSolutions.getSolutions();
        Common dataset = currentSolutions.getControl().getDataset();
        ArrayList<GuiSolution> guiSolutions = new ArrayList<GuiSolution>(sols.size());
        int i = 0;
        for (Tricluster tri : sols) {
            TriGraphs guiTri = new TriGraphs(tri);
            LOG.debug("Tricluster " + i);
            LOG.debug("g-c-t");
            guiTri.setGCTcharts(this.buildChartsCathegory(guiTri.getTricluster(), dataset, "g-c-t"));
            LOG.debug("g-t-c");
            guiTri.setGTCcharts(this.buildChartsCathegory(guiTri.getTricluster(), dataset, "g-t-c"));
            LOG.debug("t-g-c");
            guiTri.setTGCcharts(this.buildChartsTGC(guiTri.getTricluster(), dataset));
            guiSolutions.add(new GuiSolution(guiTri));
        }
        return guiSolutions;
    }

    private List<XYChart> buildChartsCathegory(Tricluster tri, Common dataset, String config) {
        double[][][] data = dataset.getDataset();
        List<Integer> xCoor = null;
        List<Integer> oCoor = null;
        List<Integer> pCoor = null;
        int nx = 0;
        int no = 0;
        int np = 0;
        String[] panelLabels = null;
        String[] outputLabels = null;
        String panelPrefix = "";
        String xAxisTittle = "genes";
        String yAxisTittle = "expression level";
        int w = 0;
        int l = 0;
        double csize = 0.0;
        switch (config) {
            case "g-c-t": {
                panelPrefix = "GCT";
                panelLabels = ((Real)dataset).getTimeNames();
                outputLabels = ((Real)dataset).getSampleNames();
                xCoor = tri.getGenes();
                oCoor = tri.getSamples();
                pCoor = tri.getTimes();
                nx = xCoor.size();
                no = oCoor.size();
                np = pCoor.size();
                w = 420;
                l = 170;
                csize = 0.7;
                break;
            }
            case "g-t-c": {
                panelPrefix = "GTC";
                panelLabels = ((Real)dataset).getSampleNames();
                outputLabels = ((Real)dataset).getTimeNames();
                xCoor = tri.getGenes();
                oCoor = tri.getTimes();
                pCoor = tri.getSamples();
                nx = xCoor.size();
                no = oCoor.size();
                np = pCoor.size();
                w = 420;
                l = 170;
                csize = 0.7;
            }
        }
        ArrayList<XYChart> charts = new ArrayList<XYChart>(np);
        double[] xAxis = new double[nx];
        for (int i = 0; i < nx; ++i) {
            xAxis[i] = i + 1;
        }
        ArrayList<double[]> series = null;
        for (Integer c_ip : pCoor) {
            int ip = c_ip;
            series = new ArrayList<double[]>(no);
            for (Integer c_io : oCoor) {
                int io = c_io;
                double[] serie = new double[nx];
                int i = 0;
                for (Integer c_ix : xCoor) {
                    int ix = c_ix;
                    switch (config) {
                        case "g-c-t": {
                            serie[i] = data[ix][io][ip];
                            break;
                        }
                        case "g-t-c": {
                            serie[i] = data[ix][ip][io];
                        }
                    }
                    ++i;
                }
                series.add(serie);
            }
            String debug = "\n";
            XYChart chart = this.getXYchart(panelPrefix + " - " + panelLabels[ip], w, l, xAxisTittle, yAxisTittle, csize, false, false);
            ((XYStyler)chart.getStyler()).setXAxisTicksVisible(false);
            int sn = 0;
            for (double[] serie : series) {
                debug = debug + "Panel " + panelLabels[ip] + " Serie " + sn + " : " + TextUtilities.vectorOfDoubleToString(serie, 0) + "\n";
                XYSeries s = chart.addSeries(outputLabels[sn], xAxis, serie);
                s.setMarker(SeriesMarkers.NONE);
                ++sn;
            }
            charts.add(chart);
        }
        return charts;
    }

    private List<XYChart> buildChartsTGC(Tricluster tri, Common dataset) {
        double[][][] data = dataset.getDataset();
        String[] panelLabels = ((Real)dataset).getSampleNames();
        List<Integer> xCoor = null;
        List<Integer> oCoor = null;
        List<Integer> pCoor = null;
        int nx = 0;
        int no = 0;
        int np = 0;
        String xAxisTittle = "time points";
        String panelPrefix = "TGC";
        xCoor = tri.getTimes();
        oCoor = tri.getGenes();
        pCoor = tri.getSamples();
        nx = xCoor.size();
        no = oCoor.size();
        np = pCoor.size();
        ArrayList<XYChart> charts = new ArrayList<XYChart>(np);
        double[] xAxis = Conversions.fromListIntegerToArrayDouble(xCoor);
        ArrayList<double[]> series = null;
        for (Integer c_ip : pCoor) {
            int ip = c_ip;
            series = new ArrayList<double[]>(no);
            for (Integer c_io : oCoor) {
                int io = c_io;
                double[] serie = new double[nx];
                int i = 0;
                for (Integer c_ix : xCoor) {
                    int ix = c_ix;
                    serie[i] = data[io][ip][ix];
                    ++i;
                }
                series.add(serie);
            }
            String debug = "\n";
            XYChart chart = this.getXYchart(panelPrefix + " - " + panelLabels[ip], 400, 170, xAxisTittle, "expresion Level", 0.7, false, true);
            int sn = 0;
            for (double[] serie : series) {
                debug = debug + "Panel " + panelLabels[ip] + " Serie " + sn + " : " + TextUtilities.vectorOfDoubleToString(serie, 0) + "\n";
                XYSeries s = chart.addSeries("gen " + sn, xAxis, serie);
                s.setMarker(SeriesMarkers.NONE);
                ++sn;
            }
            charts.add(chart);
        }
        return charts;
    }

    private XYChart getXYchart(String title, int width, int height, String xAxisTitle, String yAxisTitle, double plotContentSize, boolean legend, boolean xAxisTicksVisible) {
        return this.getXYchart(title, width, height, xAxisTitle, yAxisTitle, SystemColor.window, Color.BLACK, 1, false, plotContentSize, legend, 0, new Font("Monospaced", 0, 8), xAxisTicksVisible);
    }

    private XYChart getXYchart(String title, int width, int height, String xAxisTitle, String yAxisTitle, Color backgorundColor, Color borderColor, int charPadding, boolean annotations, double plotContentSize, boolean legend, int xAxisRotation, Font axisLabelFont, boolean xAxisTicksVisible) {
        XYChart chart = new XYChartBuilder().build();
        this.configureCommons(chart, title, width, height, xAxisTitle, yAxisTitle, backgorundColor, borderColor, charPadding, annotations, plotContentSize, legend);
        this.configureXY(chart, xAxisRotation, axisLabelFont, xAxisTicksVisible);
        return chart;
    }

    private void configureXY(XYChart chart, int xAxisRotation, Font axisLabelFont, boolean xAxisTicksVisible) {
        ((XYStyler)chart.getStyler()).setXAxisLabelRotation(xAxisRotation);
        ((XYStyler)chart.getStyler()).setAxisTickLabelsFont(axisLabelFont);
        ((XYStyler)chart.getStyler()).setXAxisTicksVisible(xAxisTicksVisible);
    }

    private BubbleChart getBubbleChart(String title, int width, int height, String xAxisTitle, String yAxisTitle, double plotContentSize, Color seriesColor) {
        BubbleChart chart = new BubbleChartBuilder().build();
        chart.setWidth(width);
        chart.setHeight(height);
        chart.setTitle(title);
        chart.setXAxisTitle(xAxisTitle);
        chart.setYAxisTitle(yAxisTitle);
        ((BubbleStyler)chart.getStyler()).setChartBackgroundColor(SystemColor.window);
        ((BubbleStyler)chart.getStyler()).setPlotBorderColor(Color.BLACK);
        ((BubbleStyler)chart.getStyler()).setChartPadding(1);
        ((BubbleStyler)chart.getStyler()).setHasAnnotations(true);
        ((BubbleStyler)chart.getStyler()).setPlotContentSize(plotContentSize);
        ((BubbleStyler)chart.getStyler()).setLegendVisible(false);
        Color[] sc = new Color[]{seriesColor};
        ((BubbleStyler)chart.getStyler()).setSeriesColors(sc);
        ((BubbleStyler)chart.getStyler()).setAxisTickLabelsFont(new Font("Monospaced", 0, 8));
        ((BubbleStyler)chart.getStyler()).setXAxisLabelRotation(0);
        ((BubbleStyler)chart.getStyler()).setYAxisMax(1.0);
        ((BubbleStyler)chart.getStyler()).setYAxisMin(0.0);
        ((BubbleStyler)chart.getStyler()).setXAxisMax(1.0);
        ((BubbleStyler)chart.getStyler()).setXAxisMin(0.0);
        return chart;
    }

    private CategoryChart getCategoryChart(String title, int width, int height, String xAxisTitle, String yAxisTitle, Color seriesColor, boolean annotations, double yMin, double yMax) {
        return this.getCategoryChart(title, width, height, xAxisTitle, yAxisTitle, SystemColor.window, Color.BLACK, 1, annotations, 0.8, false, seriesColor, 0.4, new Font("Monospaced", 0, 8), 90, yMin, yMax);
    }

    private CategoryChart getCategoryChart(String title, int width, int height, String xAxisTitle, Color seriesColor, int xAxisLabelRotation) {
        return this.getCategoryChart(title, width, height, xAxisTitle, "", SystemColor.window, Color.BLACK, 1, false, 0.98, false, seriesColor, 0.4, new Font("Monospaced", 0, 8), xAxisLabelRotation, 0.0, 1.0);
    }

    private CategoryChart getCategoryChart(String title, int width, int height, String xAxisTitle, Color seriesColor) {
        return this.getCategoryChart(title, width, height, xAxisTitle, "", SystemColor.window, Color.BLACK, 1, false, 0.98, false, seriesColor, 0.4, new Font("Monospaced", 0, 8), 0, 0.0, 1.0);
    }

    private CategoryChart getCategoryChart(String title, int width, int height, String xAxisTitle, String yAxisTitle, Color backgorundColor, Color borderColor, int charPadding, boolean annotations, double plotContentSize, boolean legend, Color seriesColor, double aviableSpaceFill, Font axisLabelFont, int xAxisLabelRotation, double yMin, double yMax) {
        CategoryChart chart = new CategoryChartBuilder().build();
        this.configureCommons(chart, title, width, height, xAxisTitle, yAxisTitle, backgorundColor, borderColor, charPadding, annotations, plotContentSize, legend);
        this.configureCategory(chart, seriesColor, aviableSpaceFill, axisLabelFont, xAxisLabelRotation, yMin, yMax);
        return chart;
    }

    private void configureCommons(Chart chart, String title, int width, int height, String xAxisTitle, String yAxisTitle, Color backgorundColor, Color borderColor, int charPadding, boolean annotations, double plotContentSize, boolean legend) {
        chart.setWidth(width);
        chart.setHeight(height);
        chart.setXAxisTitle(xAxisTitle);
        chart.setTitle(title);
        ((Styler)chart.getStyler()).setChartBackgroundColor(backgorundColor);
        ((Styler)chart.getStyler()).setPlotBorderColor(borderColor);
        ((Styler)chart.getStyler()).setChartPadding(charPadding);
        ((Styler)chart.getStyler()).setHasAnnotations(annotations);
        ((Styler)chart.getStyler()).setPlotContentSize(plotContentSize);
        ((Styler)chart.getStyler()).setLegendVisible(legend);
        chart.setYAxisTitle(yAxisTitle);
    }

    private void configureCategory(CategoryChart chart, Color seriesColor, double aviableSpaceFill, Font axisLabelFont, int xAxisLabelRotation, double yMin, double yMax) {
        Color[] sc = new Color[]{seriesColor};
        ((CategoryStyler)chart.getStyler()).setSeriesColors(sc);
        ((CategoryStyler)chart.getStyler()).setAvailableSpaceFill(aviableSpaceFill);
        ((CategoryStyler)chart.getStyler()).setAxisTickLabelsFont(axisLabelFont);
        ((CategoryStyler)chart.getStyler()).setXAxisLabelRotation(xAxisLabelRotation);
        if (yMax != -1.0) {
            ((CategoryStyler)chart.getStyler()).setYAxisMax(yMax);
        }
        if (yMin != -1.0) {
            ((CategoryStyler)chart.getStyler()).setYAxisMin(yMin);
        }
    }

    private XYChart getScatterChart(String title, int width, int height, String xAxisTitle, String yAxisTitle, double plotContentSize, Color seriesColor) {
        XYChart chart = new XYChartBuilder().build();
        ((XYStyler)chart.getStyler()).setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.setWidth(width);
        chart.setHeight(height);
        chart.setTitle(title);
        chart.setXAxisTitle(xAxisTitle);
        chart.setYAxisTitle(yAxisTitle);
        ((XYStyler)chart.getStyler()).setChartBackgroundColor(SystemColor.window);
        ((XYStyler)chart.getStyler()).setPlotBorderColor(Color.BLACK);
        ((XYStyler)chart.getStyler()).setChartPadding(1);
        ((XYStyler)chart.getStyler()).setHasAnnotations(true);
        ((XYStyler)chart.getStyler()).setPlotContentSize(plotContentSize);
        ((XYStyler)chart.getStyler()).setLegendVisible(false);
        Color[] sc = new Color[]{seriesColor};
        ((XYStyler)chart.getStyler()).setSeriesColors(sc);
        ((XYStyler)chart.getStyler()).setAxisTickLabelsFont(new Font("Monospaced", 0, 8));
        ((XYStyler)chart.getStyler()).setXAxisLabelRotation(0);
        ((XYStyler)chart.getStyler()).setYAxisMax(1.0);
        ((XYStyler)chart.getStyler()).setYAxisMin(0.0);
        ((XYStyler)chart.getStyler()).setXAxisMax(1.0);
        ((XYStyler)chart.getStyler()).setXAxisMin(0.0);
        return chart;
    }
}

