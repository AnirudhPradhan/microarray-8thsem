/*
 * Decompiled with CFR 0.152.
 */
package datasetinterface;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import utils.SystemUtilities;

public class SepCharRadioButtons
extends JPanel {
    private JRadioButton semicolonRadioButton;
    private JRadioButton commaRadioButton;
    private JRadioButton tabRadioButton;
    private JRadioButton blankRadioButton;

    public SepCharRadioButtons() {
        Properties labels = SystemUtilities.getLabelProperties();
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("121px"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC}, new RowSpec[]{FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("26px")}));
        JLabel lblType = new JLabel(labels.getProperty("dataset_sep_tag"));
        lblType.setFont(new Font("Arial", 0, 12));
        this.add((Component)lblType, "1, 2, left, center");
        this.semicolonRadioButton = new JRadioButton(labels.getProperty("dataset_semicolon_tag"));
        this.semicolonRadioButton.setFont(new Font("Arial", 0, 12));
        this.add((Component)this.semicolonRadioButton, "3, 2, left, center");
        this.semicolonRadioButton.setSelected(false);
        this.commaRadioButton = new JRadioButton(labels.getProperty("dataset_comma_tag"));
        this.commaRadioButton.setFont(new Font("Arial", 0, 12));
        this.add((Component)this.commaRadioButton, "5, 2, left, center");
        this.tabRadioButton = new JRadioButton(labels.getProperty("dataset_tab_tag"));
        this.tabRadioButton.setFont(new Font("Arial", 0, 12));
        this.add((Component)this.tabRadioButton, "7, 2, left, center");
        this.blankRadioButton = new JRadioButton(labels.getProperty("dataset_blank_tag"));
        this.blankRadioButton.setFont(new Font("Arial", 0, 12));
        this.add((Component)this.blankRadioButton, "9, 2, left, center");
        this.semicolonRadioButton.setSelected(true);
        this.commaRadioButton.setSelected(false);
        this.tabRadioButton.setSelected(false);
        this.blankRadioButton.setSelected(false);
        this.semicolonRadioButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                SepCharRadioButtons.this.commaRadioButton.setSelected(false);
                SepCharRadioButtons.this.tabRadioButton.setSelected(false);
                SepCharRadioButtons.this.blankRadioButton.setSelected(false);
            }
        });
        this.commaRadioButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                SepCharRadioButtons.this.semicolonRadioButton.setSelected(false);
                SepCharRadioButtons.this.tabRadioButton.setSelected(false);
                SepCharRadioButtons.this.blankRadioButton.setSelected(false);
            }
        });
        this.tabRadioButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                SepCharRadioButtons.this.semicolonRadioButton.setSelected(false);
                SepCharRadioButtons.this.commaRadioButton.setSelected(false);
                SepCharRadioButtons.this.blankRadioButton.setSelected(false);
            }
        });
        this.blankRadioButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                SepCharRadioButtons.this.semicolonRadioButton.setSelected(false);
                SepCharRadioButtons.this.commaRadioButton.setSelected(false);
                SepCharRadioButtons.this.tabRadioButton.setSelected(false);
            }
        });
    }

    public String getSelectedSep() {
        String r = "";
        if (this.semicolonRadioButton.isSelected()) {
            r = ";";
        } else if (this.commaRadioButton.isSelected()) {
            r = ",";
        } else if (this.tabRadioButton.isSelected()) {
            r = "\t";
        } else if (this.blankRadioButton.isSelected()) {
            r = " ";
        }
        return r;
    }
}

