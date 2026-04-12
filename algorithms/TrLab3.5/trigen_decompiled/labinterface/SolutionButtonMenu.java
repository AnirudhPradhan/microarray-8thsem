/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolutionButtonMenu
extends JPanel {
    private static final Logger LOG = LoggerFactory.getLogger(SolutionButtonMenu.class);
    private JPanel solutionPanel;

    public SolutionButtonMenu(JPanel solPanel, int champIndex) {
        this.setBorder(new LineBorder(SystemColor.textHighlight, 3, true));
        this.solutionPanel = solPanel;
        this.setLayout(new BoxLayout(this, 1));
        for (int i = 0; i < this.solutionPanel.getComponentCount(); ++i) {
            final JButton button = new JButton("Solution " + (i + 1));
            button.setFocusPainted(false);
            button.setFont(new Font("Monospaced", 0, 10));
            int w = 100;
            int l = 30;
            button.setMinimumSize(new Dimension(w, l));
            button.setMaximumSize(new Dimension(w, l));
            if (i == champIndex - 1) {
                button.setForeground(SystemColor.ORANGE);
            }
            this.add(button);
            button.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    JPanel panel = (JPanel)button.getParent();
                    int index = panel.getComponentZOrder(button);
                    SolutionButtonMenu.this.showSolution(index);
                }
            });
        }
        this.validate();
        this.setVisible(true);
        this.showSolution(0);
    }

    public void showSolution(int index) {
        Component[] cmp = this.solutionPanel.getComponents();
        for (int i = 0; i < cmp.length; ++i) {
            cmp[i].setVisible(false);
            JButton button = (JButton)this.getComponent(i);
            button.setEnabled(true);
        }
        cmp[index].setVisible(true);
        ((JButton)this.getComponent(index)).setEnabled(false);
    }
}

