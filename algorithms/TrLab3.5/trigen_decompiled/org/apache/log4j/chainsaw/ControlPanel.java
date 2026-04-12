/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.chainsaw;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.chainsaw.ExitAction;
import org.apache.log4j.chainsaw.MyTableModel;

class ControlPanel
extends JPanel {
    private static final Logger LOG = Logger.getLogger(ControlPanel.class);

    ControlPanel(final MyTableModel aModel) {
        this.setBorder(BorderFactory.createTitledBorder("Controls: "));
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c2 = new GridBagConstraints();
        this.setLayout(gridbag);
        c2.ipadx = 5;
        c2.ipady = 5;
        c2.gridx = 0;
        c2.anchor = 13;
        c2.gridy = 0;
        JLabel label = new JLabel("Filter Level:");
        gridbag.setConstraints(label, c2);
        this.add(label);
        ++c2.gridy;
        label = new JLabel("Filter Thread:");
        gridbag.setConstraints(label, c2);
        this.add(label);
        ++c2.gridy;
        label = new JLabel("Filter Logger:");
        gridbag.setConstraints(label, c2);
        this.add(label);
        ++c2.gridy;
        label = new JLabel("Filter NDC:");
        gridbag.setConstraints(label, c2);
        this.add(label);
        ++c2.gridy;
        label = new JLabel("Filter Message:");
        gridbag.setConstraints(label, c2);
        this.add(label);
        c2.weightx = 1.0;
        c2.gridx = 1;
        c2.anchor = 17;
        c2.gridy = 0;
        Level[] allPriorities = new Level[]{Level.FATAL, Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG, Level.TRACE};
        final JComboBox<Level> priorities = new JComboBox<Level>(allPriorities);
        Level lowest = allPriorities[allPriorities.length - 1];
        priorities.setSelectedItem(lowest);
        aModel.setPriorityFilter(lowest);
        gridbag.setConstraints(priorities, c2);
        this.add(priorities);
        priorities.setEditable(false);
        priorities.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent aEvent) {
                aModel.setPriorityFilter((Priority)priorities.getSelectedItem());
            }
        });
        c2.fill = 2;
        ++c2.gridy;
        final JTextField threadField = new JTextField("");
        threadField.getDocument().addDocumentListener(new DocumentListener(){

            public void insertUpdate(DocumentEvent aEvent) {
                aModel.setThreadFilter(threadField.getText());
            }

            public void removeUpdate(DocumentEvent aEvente) {
                aModel.setThreadFilter(threadField.getText());
            }

            public void changedUpdate(DocumentEvent aEvent) {
                aModel.setThreadFilter(threadField.getText());
            }
        });
        gridbag.setConstraints(threadField, c2);
        this.add(threadField);
        ++c2.gridy;
        final JTextField catField = new JTextField("");
        catField.getDocument().addDocumentListener(new DocumentListener(){

            public void insertUpdate(DocumentEvent aEvent) {
                aModel.setCategoryFilter(catField.getText());
            }

            public void removeUpdate(DocumentEvent aEvent) {
                aModel.setCategoryFilter(catField.getText());
            }

            public void changedUpdate(DocumentEvent aEvent) {
                aModel.setCategoryFilter(catField.getText());
            }
        });
        gridbag.setConstraints(catField, c2);
        this.add(catField);
        ++c2.gridy;
        final JTextField ndcField = new JTextField("");
        ndcField.getDocument().addDocumentListener(new DocumentListener(){

            public void insertUpdate(DocumentEvent aEvent) {
                aModel.setNDCFilter(ndcField.getText());
            }

            public void removeUpdate(DocumentEvent aEvent) {
                aModel.setNDCFilter(ndcField.getText());
            }

            public void changedUpdate(DocumentEvent aEvent) {
                aModel.setNDCFilter(ndcField.getText());
            }
        });
        gridbag.setConstraints(ndcField, c2);
        this.add(ndcField);
        ++c2.gridy;
        final JTextField msgField = new JTextField("");
        msgField.getDocument().addDocumentListener(new DocumentListener(){

            public void insertUpdate(DocumentEvent aEvent) {
                aModel.setMessageFilter(msgField.getText());
            }

            public void removeUpdate(DocumentEvent aEvent) {
                aModel.setMessageFilter(msgField.getText());
            }

            public void changedUpdate(DocumentEvent aEvent) {
                aModel.setMessageFilter(msgField.getText());
            }
        });
        gridbag.setConstraints(msgField, c2);
        this.add(msgField);
        c2.weightx = 0.0;
        c2.fill = 2;
        c2.anchor = 13;
        c2.gridx = 2;
        c2.gridy = 0;
        JButton exitButton = new JButton("Exit");
        exitButton.setMnemonic('x');
        exitButton.addActionListener(ExitAction.INSTANCE);
        gridbag.setConstraints(exitButton, c2);
        this.add(exitButton);
        ++c2.gridy;
        JButton clearButton = new JButton("Clear");
        clearButton.setMnemonic('c');
        clearButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent aEvent) {
                aModel.clear();
            }
        });
        gridbag.setConstraints(clearButton, c2);
        this.add(clearButton);
        ++c2.gridy;
        final JButton toggleButton = new JButton("Pause");
        toggleButton.setMnemonic('p');
        toggleButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent aEvent) {
                aModel.toggle();
                toggleButton.setText(aModel.isPaused() ? "Resume" : "Pause");
            }
        });
        gridbag.setConstraints(toggleButton, c2);
        this.add(toggleButton);
    }
}

