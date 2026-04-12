/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import analysis.Experiment;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import labinterface.BioExperimentLevelFrame;
import labinterface.CommonExperimentLevelFrame;
import labinterface.ExpCompGraphFrame;
import labinterface.ExpCompTableModel;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipleAnalysisExpLevelFrame
extends JFrame {
    private static final Logger LOG = LoggerFactory.getLogger(MultipleAnalysisExpLevelFrame.class);
    private JPanel contentPane;
    private List<Experiment> biological;
    private List<Experiment> common;
    private List<Experiment> allExperiments;

    public MultipleAnalysisExpLevelFrame(List<Experiment> bio, List<Experiment> com) {
        this.biological = bio;
        this.common = com;
        this.allExperiments = new LinkedList<Experiment>();
        this.allExperiments.addAll(this.biological);
        this.allExperiments.addAll(this.common);
        this.setDefaultCloseOperation(2);
        this.setBounds(50, 50, 1400, 500);
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.contentPane);
        this.contentPane.setLayout(new MigLayout("", "[]10[]10[]", "[][]"));
        JTable bioTable = new JTable(new ExpCompTableModel(this.biological));
        bioTable.setAutoResizeMode(4);
        bioTable.setFont(new Font("Monospaced", 0, 10));
        bioTable.setFillsViewportHeight(true);
        bioTable.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable)me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                int index = table.convertRowIndexToModel(row);
                if (me.getClickCount() == 2) {
                    BioExperimentLevelFrame bioExpFrame = new BioExperimentLevelFrame((Experiment)MultipleAnalysisExpLevelFrame.this.biological.get(index));
                    bioExpFrame.setVisible(true);
                }
            }
        });
        JTable comTable = new JTable(new ExpCompTableModel(this.common));
        comTable.setAutoResizeMode(4);
        comTable.setFont(new Font("Monospaced", 0, 10));
        comTable.setFillsViewportHeight(true);
        comTable.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable)me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                int index = table.convertRowIndexToModel(row);
                if (me.getClickCount() == 2) {
                    CommonExperimentLevelFrame comExpFrame = new CommonExperimentLevelFrame((Experiment)MultipleAnalysisExpLevelFrame.this.common.get(index));
                    comExpFrame.setVisible(true);
                }
            }
        });
        JTable allTable = new JTable(new ExpCompTableModel(this.allExperiments));
        allTable.setAutoResizeMode(4);
        allTable.setFont(new Font("Monospaced", 0, 10));
        allTable.setFillsViewportHeight(true);
        allTable.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable)me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                int index = table.convertRowIndexToModel(row);
                if (me.getClickCount() == 2) {
                    Experiment e = (Experiment)MultipleAnalysisExpLevelFrame.this.allExperiments.get(index);
                    if (e.getAnalysisType() == 'b') {
                        BioExperimentLevelFrame bioExpFrame = new BioExperimentLevelFrame(e);
                        bioExpFrame.setVisible(true);
                    } else if (e.getAnalysisType() == 'c') {
                        CommonExperimentLevelFrame comExpFrame = new CommonExperimentLevelFrame(e);
                        comExpFrame.setVisible(true);
                    }
                }
            }
        });
        JButton bioButton = new JButton("Biological Summary");
        bioButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ExpCompGraphFrame gr = new ExpCompGraphFrame(MultipleAnalysisExpLevelFrame.this.biological);
                gr.setVisible(true);
            }
        });
        JButton comButton = new JButton("Common Summary");
        comButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ExpCompGraphFrame gr = new ExpCompGraphFrame(MultipleAnalysisExpLevelFrame.this.common);
                gr.setVisible(true);
            }
        });
        JButton allButton = new JButton("Complete Summary");
        allButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ExpCompGraphFrame gr = new ExpCompGraphFrame(MultipleAnalysisExpLevelFrame.this.allExperiments);
                gr.setVisible(true);
            }
        });
        this.contentPane.add((Component)bioButton, "cell 0 0");
        this.contentPane.add((Component)comButton, "cell 1 0");
        this.contentPane.add((Component)allButton, "cell 2 0");
        bioTable.setAutoCreateRowSorter(true);
        JScrollPane bioTScroll = new JScrollPane(bioTable);
        this.contentPane.add((Component)bioTScroll, "cell 0 1");
        comTable.setAutoCreateRowSorter(true);
        JScrollPane comTScroll = new JScrollPane(comTable);
        this.contentPane.add((Component)comTScroll, "cell 1 1");
        allTable.setAutoCreateRowSorter(true);
        JScrollPane allTScroll = new JScrollPane(allTable);
        this.contentPane.add((Component)allTScroll, "cell 2 1");
    }
}

