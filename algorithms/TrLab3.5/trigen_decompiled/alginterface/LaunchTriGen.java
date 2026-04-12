/*
 * Decompiled with CFR 0.152.
 */
package alginterface;

import algcore.TriGen;
import alginterface.TriGenGuiTask;
import input.algorithm.Control;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SystemUtilities;

public class LaunchTriGen
extends JDialog
implements PropertyChangeListener {
    private static final Logger LOG = LoggerFactory.getLogger(LaunchTriGen.class);
    private final JPanel contentPanel = new JPanel();
    private JProgressBar progressBar;
    private JButton okButton;
    private JButton cancelButton;
    private JTextArea message;
    private Control currentControl;
    private TriGenGuiTask task;

    public static void main(String[] args) {
        try {
            LaunchTriGen dialog = new LaunchTriGen();
            dialog.setDefaultCloseOperation(2);
            dialog.setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LaunchTriGen() {
        this.setDefaultCloseOperation(2);
        this.setBounds(100, 100, 469, 144);
        this.getContentPane().setLayout(new BorderLayout());
        this.contentPanel.setLayout(new FlowLayout());
        this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.getContentPane().add((Component)this.contentPanel, "Center");
        this.cancelButton = new JButton(SystemUtilities.getLabelProperty("cancel_button"));
        this.cancelButton.setFont(new Font("Arial", 0, 12));
        this.contentPanel.add(this.cancelButton);
        this.okButton = new JButton(SystemUtilities.getLabelProperty("ok_button"));
        this.okButton.setFont(new Font("Arial", 0, 12));
        this.okButton.setActionCommand("start");
        this.contentPanel.add(this.okButton);
        this.progressBar = new JProgressBar(0, 100);
        this.progressBar.setFont(new Font("Arial", 1, 12));
        this.progressBar.setValue(0);
        this.progressBar.setStringPainted(true);
        this.contentPanel.add(this.progressBar);
        this.getContentPane().add((Component)this.contentPanel, "Center");
        this.message = new JTextArea(2, 35);
        this.message.setBackground(SystemColor.window);
        this.contentPanel.add(this.message);
        this.message.setForeground(Color.BLUE);
        this.message.setEditable(false);
        this.message.setFont(new Font("Arial", 2, 12));
        this.message.setMargin(new Insets(5, 5, 5, 5));
        this.contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.okButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                LaunchTriGen.this.dispose();
            }
        });
        this.cancelButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                TriGen.getInstance().cancel();
            }
        });
        this.setVisible(false);
    }

    public void launch(Control control) {
        this.currentControl = control;
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                LaunchTriGen.this.launchAlgorithm();
            }
        });
    }

    public void launchAlgorithm() {
        this.okButton.setEnabled(false);
        this.message.setText("");
        this.cancelButton.setEnabled(true);
        this.setCursor(Cursor.getPredefinedCursor(3));
        this.task = new TriGenGuiTask(this.currentControl);
        this.task.setPanel(this.contentPanel);
        this.task.setOkButton(this.okButton);
        this.task.setCancelButton(this.cancelButton);
        this.task.setMessage(this.message);
        this.task.addPropertyChangeListener(this);
        this.task.execute();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer)evt.getNewValue();
            this.progressBar.setValue(progress);
        }
    }
}

