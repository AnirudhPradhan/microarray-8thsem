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
import javax.swing.JTextField;
import utils.SystemUtilities;

public class DatasetTypeRadioButtons
extends JPanel {
    private JRadioButton bioRadioButton;
    private JRadioButton comRadioButton;
    private JTextField organismText;

    public DatasetTypeRadioButtons() {
        Properties labels = SystemUtilities.getLabelProperties();
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("62px"), ColumnSpec.decode("86px"), FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("103px")}, new RowSpec[]{FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("26px"), FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC}));
        JLabel lblType = new JLabel(labels.getProperty("dataset_type_tag"));
        lblType.setFont(new Font("Arial", 0, 12));
        this.add((Component)lblType, "1, 2, left, center");
        this.bioRadioButton = new JRadioButton(labels.getProperty("dataset_biological_tag"));
        this.bioRadioButton.setFont(new Font("Arial", 0, 12));
        this.add((Component)this.bioRadioButton, "2, 2, left, center");
        this.comRadioButton = new JRadioButton(labels.getProperty("dataset_common_tag"));
        this.comRadioButton.setFont(new Font("Arial", 0, 12));
        this.add((Component)this.comRadioButton, "4, 2, left, center");
        final JLabel lblOrganism = new JLabel(labels.getProperty("dataset_organism_tag"));
        lblOrganism.setFont(new Font("Arial", 0, 12));
        this.add((Component)lblOrganism, "1, 4, left, center");
        this.organismText = new JTextField();
        this.organismText.setFont(new Font("Arial", 0, 12));
        this.organismText.setColumns(15);
        this.add((Component)this.organismText, "2, 4, 3, 1, left, top");
        this.bioRadioButton.setSelected(true);
        this.comRadioButton.setSelected(false);
        lblOrganism.setVisible(true);
        this.organismText.setVisible(true);
        this.organismText.setText("");
        this.bioRadioButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                DatasetTypeRadioButtons.this.comRadioButton.setSelected(false);
                lblOrganism.setVisible(true);
                DatasetTypeRadioButtons.this.organismText.setVisible(true);
            }
        });
        this.comRadioButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                DatasetTypeRadioButtons.this.bioRadioButton.setSelected(false);
                lblOrganism.setVisible(false);
                DatasetTypeRadioButtons.this.organismText.setVisible(false);
            }
        });
    }

    public char getTypeSelected() {
        int r = 32;
        if (this.bioRadioButton.isSelected()) {
            r = 98;
        } else if (this.comRadioButton.isSelected()) {
            r = 101;
        }
        return (char)r;
    }

    public JTextField getOrganismText() {
        return this.organismText;
    }

    public String getStringReport() {
        String r = "";
        r = "Type = " + this.getTypeSelected();
        if (!this.organismText.getText().equalsIgnoreCase("")) {
            r = r + " , Organism = " + this.organismText.getText();
        }
        return r;
    }
}

