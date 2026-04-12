/*
 * Decompiled with CFR 0.152.
 */
package interfaceutils;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import utils.SystemUtilities;

public class PathPicker
extends JPanel {
    private JLabel label;
    private JButton explore_button;
    private JFileChooser fileChooser;
    private boolean onlyDir;
    private JTextField selectedPath_f;

    public PathPicker(String tag) {
        this.build(false, tag);
    }

    public PathPicker(boolean onlyDir, String tag) {
        this.build(onlyDir, tag);
    }

    public void build(boolean onlyDir, String tag) {
        this.onlyDir = onlyDir;
        this.setLayout(new FlowLayout(1, 7, 7));
        this.setPreferredSize(new Dimension(532, 40));
        if (!tag.equalsIgnoreCase("")) {
            this.label = new JLabel(tag);
            this.label.setFont(new Font("Arial", 0, 12));
            this.add(this.label);
        }
        this.selectedPath_f = new JTextField();
        this.selectedPath_f.setFont(new Font("Arial", 0, 12));
        this.selectedPath_f.setBounds(97, 15, 584, 28);
        this.add(this.selectedPath_f);
        this.selectedPath_f.setColumns(40);
        this.selectedPath_f.setText(System.getProperty("user.home"));
        this.explore_button = new JButton(SystemUtilities.getLabelProperty("explore_button"));
        this.explore_button.setFont(new Font("Arial", 0, 12));
        this.explore_button.setBounds(693, 16, 117, 29);
        this.add(this.explore_button);
        this.fileChooser = new JFileChooser(System.getProperty("user.home"));
        this.fileChooser.setVisible(false);
        this.explore_button.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                int choice;
                PathPicker.this.fileChooser.setVisible(true);
                if (PathPicker.this.onlyDir) {
                    PathPicker.this.fileChooser.setFileSelectionMode(1);
                }
                if ((choice = PathPicker.this.fileChooser.showOpenDialog(PathPicker.this.explore_button)) == 0) {
                    PathPicker.this.selectedPath_f.setText(PathPicker.this.fileChooser.getSelectedFile().getPath());
                }
            }
        });
    }

    public void setPath(String nPath) {
        this.selectedPath_f.setText(nPath);
    }

    public String getSelectedPath() {
        return this.selectedPath_f.getText();
    }

    @Override
    public void setSize(int with, int lenght) {
        this.setMaximumSize(new Dimension(with, lenght));
        this.setMinimumSize(new Dimension(with, lenght));
    }
}

