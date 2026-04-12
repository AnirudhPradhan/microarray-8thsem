/*
 * Decompiled with CFR 0.152.
 */
package alginterface;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SystemUtilities;

public class SetWorkspaceMenu
extends JDialog {
    private static final Logger LOG = LoggerFactory.getLogger(SetWorkspaceMenu.class);
    private final JPanel contentPanel = new JPanel();
    private JTextField selectedPath_f;
    private String selectedPath_v;

    public static void main(String[] args) {
        try {
            SetWorkspaceMenu dialog = new SetWorkspaceMenu();
            dialog.setDefaultCloseOperation(2);
            dialog.setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPaths(String nPath) {
        this.selectedPath_f.setText(nPath);
        this.selectedPath_v = nPath;
    }

    public String getSelectedPath() {
        return this.selectedPath_v;
    }

    public SetWorkspaceMenu() {
        this.setBounds(100, 100, 852, 166);
        this.getContentPane().setLayout(null);
        this.contentPanel.setBounds(0, 0, 0, 0);
        this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.getContentPane().add(this.contentPanel);
        this.contentPanel.setLayout(null);
        JLabel lblWorkspace = new JLabel(SystemUtilities.getLabelProperty("workspace_tag"));
        lblWorkspace.setFont(new Font("Arial", 0, 12));
        lblWorkspace.setBounds(17, 21, 68, 16);
        this.getContentPane().add(lblWorkspace);
        JPanel buttonPane = new JPanel();
        buttonPane.setBounds(67, 64, 450, 47);
        buttonPane.setLayout(new FlowLayout(2));
        this.getContentPane().add(buttonPane);
        JButton okButton = new JButton(SystemUtilities.getLabelProperty("ok_button"));
        okButton.setFont(new Font("Arial", 0, 12));
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        this.getRootPane().setDefaultButton(okButton);
        okButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                SetWorkspaceMenu.this.selectedPath_v = SetWorkspaceMenu.this.selectedPath_f.getText();
                JButton button = (JButton)e.getSource();
                JRootPane root = button.getRootPane();
                JDialog dialog = (JDialog)root.getParent();
                dialog.dispose();
                LOG.debug("Selected path : " + SetWorkspaceMenu.this.selectedPath_v);
            }
        });
        JButton cancelButton = new JButton(SystemUtilities.getLabelProperty("cancel_button"));
        cancelButton.setFont(new Font("Arial", 0, 12));
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
        cancelButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton)e.getSource();
                JRootPane root = button.getRootPane();
                JDialog dialog = (JDialog)root.getParent();
                dialog.dispose();
                LOG.debug("Selected path : " + SetWorkspaceMenu.this.selectedPath_v);
            }
        });
        this.selectedPath_f = new JTextField();
        this.selectedPath_f.setFont(new Font("Arial", 0, 12));
        this.selectedPath_f.setBounds(97, 15, 584, 28);
        this.getContentPane().add(this.selectedPath_f);
        this.selectedPath_f.setColumns(10);
        this.selectedPath_f.setText(System.getProperty("user.home"));
        this.selectedPath_v = this.selectedPath_f.getText();
        final JButton explore_button = new JButton(SystemUtilities.getLabelProperty("explore_button"));
        explore_button.setFont(new Font("Arial", 0, 12));
        explore_button.setBounds(693, 16, 117, 29);
        this.getContentPane().add(explore_button);
        explore_button.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
                fileChooser.setFileSelectionMode(1);
                int choice = fileChooser.showOpenDialog(explore_button);
                if (choice == 0) {
                    SetWorkspaceMenu.this.selectedPath_f.setText(fileChooser.getSelectedFile().getPath());
                }
            }
        });
    }
}

