/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.VectorGraphicsEncoder;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.Chart;

public class XChartPanel<T extends Chart>
extends JPanel {
    private final T chart;
    private final Dimension preferredSize;
    private String saveAsString = "Save As...";

    public XChartPanel(T chart) {
        this.chart = chart;
        this.preferredSize = new Dimension(((Chart)chart).getWidth(), ((Chart)chart).getHeight());
        this.addMouseListener(new PopUpMenuClickListener());
        KeyStroke ctrlS = KeyStroke.getKeyStroke(83, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        this.getInputMap(2).put(ctrlS, "save");
        this.getActionMap().put("save", new SaveAction());
    }

    public void setSaveAsString(String saveAsString) {
        this.saveAsString = saveAsString;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g.create();
        ((Chart)this.chart).paint(g2d, this.getWidth(), this.getHeight());
        g2d.dispose();
    }

    public T getChart() {
        return this.chart;
    }

    @Override
    public Dimension getPreferredSize() {
        return this.preferredSize;
    }

    private void showSaveAsDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new SuffixSaveFilter("jpg"));
        SuffixSaveFilter pngFileFilter = new SuffixSaveFilter("png");
        fileChooser.addChoosableFileFilter(pngFileFilter);
        fileChooser.addChoosableFileFilter(new SuffixSaveFilter("bmp"));
        fileChooser.addChoosableFileFilter(new SuffixSaveFilter("gif"));
        try {
            Class.forName("de.erichseifert.vectorgraphics2d.VectorGraphics2D");
            fileChooser.addChoosableFileFilter(new SuffixSaveFilter("svg"));
            fileChooser.addChoosableFileFilter(new SuffixSaveFilter("eps"));
            fileChooser.addChoosableFileFilter(new SuffixSaveFilter("pdf"));
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(pngFileFilter);
        if (fileChooser.showSaveDialog(null) == 0 && fileChooser.getSelectedFile() != null) {
            File theFileToSave = fileChooser.getSelectedFile();
            try {
                if (fileChooser.getFileFilter() == null) {
                    BitmapEncoder.saveBitmap(this.chart, theFileToSave.getCanonicalPath().toString(), BitmapEncoder.BitmapFormat.PNG);
                } else if (fileChooser.getFileFilter().getDescription().equals("*.jpg,*.JPG")) {
                    BitmapEncoder.saveJPGWithQuality(this.chart, BitmapEncoder.addFileExtension(theFileToSave.getCanonicalPath().toString(), BitmapEncoder.BitmapFormat.JPG), 1.0f);
                } else if (fileChooser.getFileFilter().getDescription().equals("*.png,*.PNG")) {
                    BitmapEncoder.saveBitmap(this.chart, theFileToSave.getCanonicalPath().toString(), BitmapEncoder.BitmapFormat.PNG);
                } else if (fileChooser.getFileFilter().getDescription().equals("*.bmp,*.BMP")) {
                    BitmapEncoder.saveBitmap(this.chart, theFileToSave.getCanonicalPath().toString(), BitmapEncoder.BitmapFormat.BMP);
                } else if (fileChooser.getFileFilter().getDescription().equals("*.gif,*.GIF")) {
                    BitmapEncoder.saveBitmap(this.chart, theFileToSave.getCanonicalPath().toString(), BitmapEncoder.BitmapFormat.GIF);
                } else if (fileChooser.getFileFilter().getDescription().equals("*.svg,*.SVG")) {
                    VectorGraphicsEncoder.saveVectorGraphic(this.chart, theFileToSave.getCanonicalPath().toString(), VectorGraphicsEncoder.VectorGraphicsFormat.SVG);
                } else if (fileChooser.getFileFilter().getDescription().equals("*.eps,*.EPS")) {
                    VectorGraphicsEncoder.saveVectorGraphic(this.chart, theFileToSave.getCanonicalPath().toString(), VectorGraphicsEncoder.VectorGraphicsFormat.EPS);
                } else if (fileChooser.getFileFilter().getDescription().equals("*.pdf,*.PDF")) {
                    VectorGraphicsEncoder.saveVectorGraphic(this.chart, theFileToSave.getCanonicalPath().toString(), VectorGraphicsEncoder.VectorGraphicsFormat.PDF);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Series_AxesChart updateSeries(String seriesName, List<?> newXData, List<? extends Number> newYData, List<? extends Number> newErrorBarData) {
        Map seriesMap = ((Chart)this.chart).getSeriesMap();
        Series_AxesChart series = (Series_AxesChart)seriesMap.get(seriesName);
        if (series == null) {
            throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
        }
        if (newXData == null) {
            ArrayList<Integer> generatedXData = new ArrayList<Integer>();
            for (int i = 1; i <= newYData.size(); ++i) {
                generatedXData.add(i);
            }
            series.replaceData(generatedXData, newYData, newErrorBarData);
        } else {
            series.replaceData(newXData, newYData, newErrorBarData);
        }
        this.revalidate();
        this.repaint();
        return series;
    }

    private class XChartPanelPopupMenu
    extends JPopupMenu {
        JMenuItem saveAsMenuItem;

        public XChartPanelPopupMenu() {
            this.saveAsMenuItem = new JMenuItem(XChartPanel.this.saveAsString);
            this.saveAsMenuItem.addMouseListener(new MouseListener(){

                @Override
                public void mouseReleased(MouseEvent e) {
                    XChartPanel.this.showSaveAsDialog();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                }
            });
            this.add(this.saveAsMenuItem);
        }
    }

    private class PopUpMenuClickListener
    extends MouseAdapter {
        private PopUpMenuClickListener() {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                this.doPop(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                this.doPop(e);
            }
        }

        private void doPop(MouseEvent e) {
            XChartPanelPopupMenu menu = new XChartPanelPopupMenu();
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private class SuffixSaveFilter
    extends FileFilter {
        private final String suffix;

        public SuffixSaveFilter(String suffix) {
            this.suffix = suffix;
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String s = f.getName();
            return s.endsWith("." + this.suffix) || s.endsWith("." + this.suffix.toUpperCase());
        }

        @Override
        public String getDescription() {
            return "*." + this.suffix + ",*." + this.suffix.toUpperCase();
        }
    }

    private class SaveAction
    extends AbstractAction {
        public SaveAction() {
            super("save");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            XChartPanel.this.showSaveAsDialog();
        }
    }
}

