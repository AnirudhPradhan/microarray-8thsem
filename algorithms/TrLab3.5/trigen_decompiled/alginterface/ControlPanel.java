/*
 * Decompiled with CFR 0.152.
 */
package alginterface;

import alginterface.AdvancedPanel;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import general.DatasetModelServer;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SystemUtilities;

public class ControlPanel
extends JPanel {
    private static final Logger LOG = LoggerFactory.getLogger(ControlPanel.class);
    private JSpinner n_value;
    private JSpinner g_value;
    private JSpinner i_value;
    private JSpinner ale_value;
    private JSpinner sel_value;
    private JSpinner mut_value;
    private JSpinner wf_value;
    private JSpinner wg_value;
    private JSpinner wc_value;
    private JSpinner wt_value;
    private JSpinner wog_value;
    private JSpinner woc_value;
    private JSpinner wot_value;
    private JComboBox<String> fitness_combo;
    private JComboBox<String> dataset_combo;
    private String[] dataset_items = DatasetModelServer.getInstance().getDatasetItems();
    private String[] fit_items = DatasetModelServer.getInstance().getFitnessItems();

    public ControlPanel() {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("max(7dlu;pref)"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(37dlu;pref)"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(79dlu;default)"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC}, new RowSpec[]{FormSpecs.PREF_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.PREF_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC}));
        this.init_labels();
        this.init_values();
    }

    public JComboBox<String> getDatasetCombo() {
        return this.dataset_combo;
    }

    private void init_values() {
        Properties def = SystemUtilities.getAlgorithmProperties();
        Properties gui = SystemUtilities.getGuiProperties();
        SpinnerNumberModel n_model = new SpinnerNumberModel(new Integer(def.getProperty("N")), new Integer(gui.getProperty("n_min")), new Integer(gui.getProperty("n_max")), new Integer(gui.getProperty("n_step")));
        this.n_value = new JSpinner(n_model);
        this.add((Component)this.n_value, "5, 3, fill, fill");
        SpinnerNumberModel g_model = new SpinnerNumberModel(new Integer(def.getProperty("G")), new Integer(gui.getProperty("g_min")), new Integer(gui.getProperty("g_max")), new Integer(gui.getProperty("g_step")));
        this.g_value = new JSpinner(g_model);
        this.add((Component)this.g_value, "5, 5, fill, fill");
        SpinnerNumberModel i_model = new SpinnerNumberModel(new Integer(def.getProperty("I")), new Integer(gui.getProperty("i_min")), new Integer(gui.getProperty("i_max")), new Integer(gui.getProperty("i_step")));
        this.i_value = new JSpinner(i_model);
        this.add((Component)this.i_value, "5, 7, fill, fill");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        double ale_default = Double.parseDouble(def.getProperty("Ale"));
        SpinnerNumberModel ale_model = new SpinnerNumberModel(new Double(ale_default), new Double(gui.getProperty("ale_min")), new Double(gui.getProperty("ale_max")), new Double(gui.getProperty("ale_step")));
        this.ale_value = new JSpinner(ale_model);
        JSpinner.NumberEditor ale_editor = (JSpinner.NumberEditor)this.ale_value.getEditor();
        DecimalFormat ale_format = ale_editor.getFormat();
        ale_format.setDecimalFormatSymbols(symbols);
        this.ale_value.setValue(-0.1);
        this.ale_value.setValue(ale_default);
        this.add((Component)this.ale_value, "5, 9, fill, fill");
        double sel_default = Double.parseDouble(def.getProperty("Sel"));
        SpinnerNumberModel sel_model = new SpinnerNumberModel(new Double(sel_default), new Double(gui.getProperty("sel_min")), new Double(gui.getProperty("sel_max")), new Double(gui.getProperty("sel_step")));
        this.sel_value = new JSpinner(sel_model);
        JSpinner.NumberEditor sel_editor = (JSpinner.NumberEditor)this.sel_value.getEditor();
        DecimalFormat sel_format = sel_editor.getFormat();
        sel_format.setDecimalFormatSymbols(symbols);
        this.sel_value.setValue(-0.1);
        this.sel_value.setValue(sel_default);
        this.add((Component)this.sel_value, "5, 11, fill, fill");
        double mut_default = Double.parseDouble(def.getProperty("Mut"));
        SpinnerNumberModel mut_model = new SpinnerNumberModel(new Double(mut_default), new Double(gui.getProperty("mut_min")), new Double(gui.getProperty("mut_max")), new Double(gui.getProperty("mut_step")));
        this.mut_value = new JSpinner(mut_model);
        JSpinner.NumberEditor mut_editor = (JSpinner.NumberEditor)this.mut_value.getEditor();
        DecimalFormat mut_format = mut_editor.getFormat();
        mut_format.setDecimalFormatSymbols(symbols);
        this.mut_value.setValue(-0.1);
        this.mut_value.setValue(mut_default);
        this.add((Component)this.mut_value, "5, 13, fill, fill");
        double wf_default = Double.parseDouble(def.getProperty("Wf"));
        SpinnerNumberModel wf_model = new SpinnerNumberModel(new Double(wf_default), new Double(gui.getProperty("wf_min")), new Double(gui.getProperty("wf_max")), new Double(gui.getProperty("wf_step")));
        this.wf_value = new JSpinner(wf_model);
        JSpinner.NumberEditor wf_editor = (JSpinner.NumberEditor)this.wf_value.getEditor();
        DecimalFormat wf_format = wf_editor.getFormat();
        wf_format.setDecimalFormatSymbols(symbols);
        this.wf_value.setValue(-0.1);
        this.wf_value.setValue(wf_default);
        this.add((Component)this.wf_value, "5, 15, fill, fill");
        double wg_default = Double.parseDouble(def.getProperty("Wg"));
        SpinnerNumberModel wg_model = new SpinnerNumberModel(new Double(wg_default), new Double(gui.getProperty("wg_min")), new Double(gui.getProperty("wg_max")), new Double(gui.getProperty("wg_step")));
        this.wg_value = new JSpinner(wg_model);
        JSpinner.NumberEditor wg_editor = (JSpinner.NumberEditor)this.wg_value.getEditor();
        DecimalFormat wg_format = wg_editor.getFormat();
        wg_format.setDecimalFormatSymbols(symbols);
        this.wg_value.setValue(-0.1);
        this.wg_value.setValue(wg_default);
        this.add((Component)this.wg_value, "5, 17, fill, fill");
        double wc_default = Double.parseDouble(def.getProperty("Wc"));
        SpinnerNumberModel wc_model = new SpinnerNumberModel(new Double(wc_default), new Double(gui.getProperty("wc_min")), new Double(gui.getProperty("wc_max")), new Double(gui.getProperty("wc_step")));
        this.wc_value = new JSpinner(wc_model);
        JSpinner.NumberEditor wc_editor = (JSpinner.NumberEditor)this.wc_value.getEditor();
        DecimalFormat wc_format = wc_editor.getFormat();
        wc_format.setDecimalFormatSymbols(symbols);
        this.wc_value.setValue(-0.1);
        this.wc_value.setValue(wc_default);
        this.add((Component)this.wc_value, "5, 19, fill, fill");
        double wt_default = Double.parseDouble(def.getProperty("Wt"));
        SpinnerNumberModel wt_model = new SpinnerNumberModel(new Double(wt_default), new Double(gui.getProperty("wt_min")), new Double(gui.getProperty("wt_max")), new Double(gui.getProperty("wt_step")));
        this.wt_value = new JSpinner(wt_model);
        JSpinner.NumberEditor wt_editor = (JSpinner.NumberEditor)this.wt_value.getEditor();
        DecimalFormat wt_format = wt_editor.getFormat();
        wt_format.setDecimalFormatSymbols(symbols);
        this.wt_value.setValue(-0.1);
        this.wt_value.setValue(wt_default);
        this.add((Component)this.wt_value, "5, 21, fill, fill");
        double wog_default = Double.parseDouble(def.getProperty("WOg"));
        SpinnerNumberModel wog_model = new SpinnerNumberModel(new Double(wog_default), new Double(gui.getProperty("wog_min")), new Double(gui.getProperty("wog_max")), new Double(gui.getProperty("wog_step")));
        this.wog_value = new JSpinner(wog_model);
        JSpinner.NumberEditor wog_editor = (JSpinner.NumberEditor)this.wog_value.getEditor();
        DecimalFormat wog_format = wog_editor.getFormat();
        wog_format.setDecimalFormatSymbols(symbols);
        this.wog_value.setValue(-0.1);
        this.wog_value.setValue(wog_default);
        this.add((Component)this.wog_value, "5, 23, fill, fill");
        double woc_default = Double.parseDouble(def.getProperty("WOc"));
        SpinnerNumberModel woc_model = new SpinnerNumberModel(new Double(woc_default), new Double(gui.getProperty("woc_min")), new Double(gui.getProperty("woc_max")), new Double(gui.getProperty("woc_step")));
        this.woc_value = new JSpinner(woc_model);
        JSpinner.NumberEditor woc_editor = (JSpinner.NumberEditor)this.woc_value.getEditor();
        DecimalFormat woc_format = woc_editor.getFormat();
        woc_format.setDecimalFormatSymbols(symbols);
        this.woc_value.setValue(-0.1);
        this.woc_value.setValue(woc_default);
        this.add((Component)this.woc_value, "5, 25, fill, fill");
        double wot_default = Double.parseDouble(def.getProperty("WOc"));
        SpinnerNumberModel wot_model = new SpinnerNumberModel(new Double(wot_default), new Double(gui.getProperty("wot_min")), new Double(gui.getProperty("wot_max")), new Double(gui.getProperty("wot_step")));
        this.wot_value = new JSpinner(wot_model);
        JSpinner.NumberEditor wot_editor = (JSpinner.NumberEditor)this.wot_value.getEditor();
        DecimalFormat wot_format = wot_editor.getFormat();
        wot_format.setDecimalFormatSymbols(symbols);
        this.wot_value.setValue(-0.1);
        this.wot_value.setValue(wot_default);
        this.add((Component)this.wot_value, "5, 27, fill, fill");
        this.buildCombos(def);
    }

    public void updateDatasetCombo() {
        this.dataset_items = DatasetModelServer.getInstance().getDatasetItems();
        this.dataset_combo.setModel(new DefaultComboBoxModel<String>(this.dataset_items));
    }

    private void buildCombos(Properties def) {
        String dataset_default = def.getProperty("dataset");
        this.dataset_combo = new JComboBox<String>(this.dataset_items);
        this.dataset_combo.setSelectedItem(dataset_default);
        this.dataset_combo.setFont(new Font("Arial", 0, 12));
        this.add(this.dataset_combo, "11, 3, fill, fill");
        this.dataset_combo.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                JComponent combo = (JComponent)e.getSource();
                JRootPane root = combo.getRootPane();
                JFrame parent = (JFrame)root.getParent();
                Container containter = parent.getContentPane();
                JTabbedPane tab = (JTabbedPane)containter.getComponent(1);
                AdvancedPanel adv_panel = (AdvancedPanel)tab.getComponent(1);
                adv_panel.updateMaxMin((String)ControlPanel.this.dataset_combo.getSelectedItem());
                LOG.debug("name = " + adv_panel.toString());
                LOG.debug("name = " + adv_panel.getName());
            }
        });
        String fitness_default = def.getProperty("fitness");
        this.fitness_combo = new JComboBox<String>(this.fit_items);
        this.fitness_combo.setSelectedItem(fitness_default);
        this.fitness_combo.setFont(new Font("Arial", 0, 12));
        this.add(this.fitness_combo, "11, 5, fill, fill");
    }

    private void init_labels() {
        JLabel n_label = new JLabel("N");
        n_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)n_label, "3, 3, center, fill");
        JLabel g_label = new JLabel("G");
        g_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)g_label, "3, 5, center, fill");
        JLabel i_label = new JLabel("I");
        i_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)i_label, "3, 7, center, fill");
        JLabel ale_label = new JLabel("Ale");
        ale_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)ale_label, "3, 9, center, fill");
        JLabel sel_label = new JLabel("Sel");
        sel_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)sel_label, "3, 11, center, fill");
        JLabel mut_label = new JLabel("Mut");
        mut_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)mut_label, "3, 13, center, fill");
        JLabel wf_label = new JLabel("Wf");
        wf_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)wf_label, "3, 15, center, fill");
        JLabel wg_label = new JLabel("Wg");
        wg_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)wg_label, "3, 17, center, fill");
        JLabel wc_label = new JLabel("Wc");
        wc_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)wc_label, "3, 19, center, fill");
        JLabel wt_label = new JLabel("Wt");
        wt_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)wt_label, "3, 21, center, fill");
        JLabel wog_label = new JLabel("WOg");
        wog_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)wog_label, "3, 23, center, fill");
        JLabel woc_label = new JLabel("WOc");
        woc_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)woc_label, "3, 25, center, fill");
        JLabel wot_label = new JLabel("WOt");
        wot_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)wot_label, "3, 27, center, fill");
        JLabel dataset_label = new JLabel("Dataset");
        dataset_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)dataset_label, "9, 3, center, fill");
        JLabel fitnes_label = new JLabel("Fitness");
        fitnes_label.setFont(new Font("Arial", 0, 12));
        this.add((Component)fitnes_label, "9, 5, center, fill");
    }

    public int getN_value() {
        return (Integer)this.n_value.getValue();
    }

    public int getG_value() {
        return (Integer)this.g_value.getValue();
    }

    public int getI_value() {
        return (Integer)this.i_value.getValue();
    }

    public double getAle_value() {
        return (Double)this.ale_value.getValue();
    }

    public double getSel_value() {
        return (Double)this.sel_value.getValue();
    }

    public double getMut_value() {
        return (Double)this.mut_value.getValue();
    }

    public double getWf_value() {
        return (Double)this.wf_value.getValue();
    }

    public double getWg_value() {
        return (Double)this.wg_value.getValue();
    }

    public double getWc_value() {
        return (Double)this.wc_value.getValue();
    }

    public double getWt_value() {
        return (Double)this.wt_value.getValue();
    }

    public double getWog_value() {
        return (Double)this.wog_value.getValue();
    }

    public double getWoc_value() {
        return (Double)this.woc_value.getValue();
    }

    public double getWot_value() {
        return (Double)this.wot_value.getValue();
    }

    public String getFitness_combo() {
        return (String)this.fitness_combo.getSelectedItem();
    }

    public String getDataset_combo() {
        return (String)this.dataset_combo.getSelectedItem();
    }

    public void setValues(Properties values) {
        this.n_value.setValue(new Integer(values.getProperty("N")));
        this.g_value.setValue(new Integer(values.getProperty("G")));
        this.i_value.setValue(new Integer(values.getProperty("I")));
        this.ale_value.setValue(new Double(values.getProperty("Ale")));
        this.sel_value.setValue(new Double(values.getProperty("Sel")));
        this.mut_value.setValue(new Double(values.getProperty("Mut")));
        this.wf_value.setValue(new Double(values.getProperty("Wf")));
        this.wg_value.setValue(new Double(values.getProperty("Wg")));
        this.wc_value.setValue(new Double(values.getProperty("Wc")));
        this.wt_value.setValue(new Double(values.getProperty("Wt")));
        this.wog_value.setValue(new Double(values.getProperty("WOg")));
        this.woc_value.setValue(new Double(values.getProperty("WOc")));
        this.wot_value.setValue(new Double(values.getProperty("WOt")));
        this.fitness_combo.setSelectedItem(values.getProperty("fitness"));
        this.dataset_combo.setSelectedItem(values.getProperty("dataset"));
    }
}

