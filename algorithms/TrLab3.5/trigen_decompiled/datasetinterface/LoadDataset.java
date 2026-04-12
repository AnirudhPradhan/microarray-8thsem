/*
 * Decompiled with CFR 0.152.
 */
package datasetinterface;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import datasetinterface.DatasetTypeRadioButtons;
import datasetinterface.SepCharRadioButtons;
import general.DatasetModelServer;
import interfaceutils.PathPicker;
import interfaceutils.SourceFilesPicker;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ReadTable;
import utils.SystemUtilities;
import utils.TextUtilities;

public class LoadDataset
extends JDialog {
    private static final Logger LOG = LoggerFactory.getLogger(LoadDataset.class);
    private final JScrollPane scrollPanel;
    private final JPanel contentPanel;
    private int currentCard;
    private JPanel panel_1;
    private JPanel panel_2;
    private JPanel panel_3;
    private JPanel panel_4;
    private int timeSize;
    private JTextField datasetNameError;
    private JTextPane summary;
    private JTextField nameValue;
    private JTextField descriptionValue;
    private JSpinner timePointsValue;
    private DatasetTypeRadioButtons datasetType;
    private PathPicker genesFile;
    private PathPicker samplesFile;
    private PathPicker timesFile;
    private SepCharRadioButtons sepRadio;
    private SourceFilesPicker sourceFiles;
    private int geneSize;
    private int sampleSize;
    private JSpinner maxG_v;
    private JSpinner maxC_v;
    private JSpinner maxT_v;
    private JSpinner minG_v;
    private JSpinner minC_v;
    private JSpinner minT_v;
    private String id;

    public LoadDataset() {
        this.setDefaultCloseOperation(2);
        this.setBounds(100, 100, 672, 493);
        this.contentPanel = new JPanel();
        this.contentPanel.setLayout(new CardLayout(0, 0));
        this.scrollPanel = new JScrollPane(this.contentPanel);
        this.getContentPane().add((Component)this.scrollPanel, "Center");
        this.getContentPane().add(this.scrollPanel);
        this.initFirstCard();
        this.initSecondCard();
        this.initThirdCard();
        this.initFourthCard();
        this.initButtonPanel();
        CardLayout c2 = (CardLayout)this.contentPanel.getLayout();
        c2.first(this.contentPanel);
    }

    private void initButtonPanel() {
        final CardLayout c2 = (CardLayout)this.contentPanel.getLayout();
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(2));
        this.getContentPane().add((Component)buttonPane, "South");
        final JButton btnPrevious = new JButton("Previous");
        btnPrevious.setFont(new Font("Arial", 0, 12));
        buttonPane.add(btnPrevious);
        final JButton nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", 0, 12));
        buttonPane.add(nextButton);
        this.getRootPane().setDefaultButton(nextButton);
        final JButton btnLoad = new JButton("Load");
        buttonPane.add(btnLoad);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", 0, 12));
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
        this.currentCard = 1;
        nextButton.setVisible(true);
        btnPrevious.setVisible(false);
        btnLoad.setVisible(false);
        LOG.debug("INIT: currentCard = " + this.currentCard + " Config = card 1");
        nextButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (LoadDataset.this.currentCard == 1) {
                    LoadDataset.this.datasetNameError.setText("");
                    DatasetModelServer dataserver = DatasetModelServer.getInstance();
                    boolean repeated = dataserver.isRepeated(LoadDataset.this.nameValue.getText());
                    boolean blank = LoadDataset.this.nameValue.getText().equalsIgnoreCase("");
                    if (!repeated && !blank) {
                        LoadDataset.this.panel_2.remove(LoadDataset.this.panel_2.getComponentCount() - 1);
                        LoadDataset.this.timeSize = (Integer)LoadDataset.this.timePointsValue.getValue();
                        LoadDataset.this.sourceFiles = new SourceFilesPicker(LoadDataset.this.timeSize);
                        GridBagConstraints gbc_sourceFiles = new GridBagConstraints();
                        gbc_sourceFiles.gridwidth = 2;
                        gbc_sourceFiles.anchor = 17;
                        gbc_sourceFiles.insets = new Insets(0, 0, 5, 5);
                        gbc_sourceFiles.gridx = 0;
                        gbc_sourceFiles.gridy = 4;
                        LoadDataset.this.panel_2.add((Component)LoadDataset.this.sourceFiles, gbc_sourceFiles);
                        c2.next(LoadDataset.this.contentPanel);
                        LoadDataset.this.currentCard++;
                        nextButton.setVisible(true);
                        btnPrevious.setVisible(true);
                        btnLoad.setVisible(false);
                        LOG.debug("currentCard = " + LoadDataset.this.currentCard + " Config = card 2");
                    } else {
                        LoadDataset.this.datasetNameError.setText(SystemUtilities.getLabelProperty("dataset_name_error_text"));
                    }
                } else {
                    c2.next(LoadDataset.this.contentPanel);
                    LoadDataset.this.currentCard++;
                    if (LoadDataset.this.currentCard == 3) {
                        LoadDataset.this.checkDatasetSize();
                        nextButton.setVisible(true);
                        btnPrevious.setVisible(true);
                        btnLoad.setVisible(false);
                        LOG.debug("currentCard = " + LoadDataset.this.currentCard + " Config = card 3");
                    } else if (LoadDataset.this.currentCard == 4) {
                        LoadDataset.this.printSummary();
                        nextButton.setVisible(false);
                        btnPrevious.setVisible(true);
                        btnLoad.setVisible(true);
                        LOG.debug("currentCard = " + LoadDataset.this.currentCard + " Config = card 4");
                    }
                }
            }
        });
        btnPrevious.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                c2.previous(LoadDataset.this.contentPanel);
                LoadDataset.this.currentCard--;
                String aux = "currentCard = " + LoadDataset.this.currentCard;
                if (LoadDataset.this.currentCard == 1) {
                    nextButton.setVisible(true);
                    btnPrevious.setVisible(false);
                    btnLoad.setVisible(false);
                    aux = aux + " Config = card 1";
                } else if (LoadDataset.this.currentCard == 2) {
                    nextButton.setVisible(true);
                    btnPrevious.setVisible(true);
                    btnLoad.setVisible(false);
                    aux = aux + " Config = card 2";
                } else if (LoadDataset.this.currentCard == 3) {
                    nextButton.setVisible(true);
                    btnPrevious.setVisible(true);
                    btnLoad.setVisible(false);
                    aux = aux + " Config = card 3";
                } else if (LoadDataset.this.currentCard == 4) {
                    nextButton.setVisible(false);
                    btnPrevious.setVisible(true);
                    btnLoad.setVisible(true);
                    aux = aux + " Config = card 4";
                }
                LOG.debug(aux);
            }
        });
        cancelButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                LoadDataset.this.dispose();
            }
        });
        btnLoad.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                LoadDataset.this.loadDatasetResources();
            }
        });
    }

    private void initFourthCard() {
        this.panel_4 = new JPanel();
        FlowLayout flowLayout = (FlowLayout)this.panel_4.getLayout();
        flowLayout.setAlignment(0);
        this.panel_4.setForeground(Color.BLUE);
        this.contentPanel.add((Component)this.panel_4, "name_1465892505241567000");
        this.summary = new JTextPane();
        this.summary.setBackground(SystemColor.window);
        this.summary.setForeground(Color.BLUE);
        this.summary.setFont(new Font("Arial", 3, 12));
        this.summary.setEditable(false);
        this.panel_4.add(this.summary);
    }

    private void initFirstCard() {
        Properties gui = SystemUtilities.getGuiProperties();
        Properties labels = SystemUtilities.getLabelProperties();
        this.panel_1 = new JPanel();
        this.contentPanel.add((Component)this.panel_1, "name_1465550427543843000");
        this.panel_1.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("191px"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(24dlu;default)"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow")}, new RowSpec[]{FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow")}));
        JLabel lblCard = new JLabel(labels.getProperty("dataset_name_tag"));
        lblCard.setFont(new Font("Arial", 0, 12));
        this.panel_1.add((Component)lblCard, "1, 3, right, center");
        this.nameValue = new JTextField();
        this.nameValue.setFont(new Font("Arial", 0, 12));
        this.panel_1.add((Component)this.nameValue, "4, 3, fill, default");
        this.nameValue.setColumns(10);
        this.datasetNameError = new JTextField();
        this.datasetNameError.setBackground(SystemColor.window);
        this.datasetNameError.setFont(new Font("Arial", 3, 12));
        this.datasetNameError.setForeground(Color.RED);
        this.panel_1.add((Component)this.datasetNameError, "8, 3, fill, default");
        this.datasetNameError.setColumns(10);
        this.datasetNameError.setBorder(null);
        this.datasetNameError.setEditable(false);
        JLabel lblDescription = new JLabel(labels.getProperty("dataset_description_tag"));
        lblDescription.setFont(new Font("Arial", 0, 12));
        this.panel_1.add((Component)lblDescription, "1, 5, right, center");
        this.descriptionValue = new JTextField();
        this.descriptionValue.setFont(new Font("Arial", 0, 12));
        this.panel_1.add((Component)this.descriptionValue, "4, 5, fill, default");
        this.descriptionValue.setColumns(10);
        SpinnerNumberModel times_model = new SpinnerNumberModel(new Integer(gui.getProperty("dataset_times_def")), new Integer(gui.getProperty("dataset_times_min")), new Integer(gui.getProperty("dataset_times_max")), new Integer(gui.getProperty("dataset_times_step")));
        JLabel lblNumberOfTime = new JLabel(labels.getProperty("dataset_time_points_tag"));
        lblNumberOfTime.setFont(new Font("Arial", 0, 12));
        this.panel_1.add((Component)lblNumberOfTime, "1, 7, right, center");
        this.timePointsValue = new JSpinner(times_model);
        this.timePointsValue.setFont(new Font("Arial", 0, 12));
        this.panel_1.add((Component)this.timePointsValue, "4, 7, default, center");
        this.datasetType = new DatasetTypeRadioButtons();
        this.panel_1.add((Component)this.datasetType, "4, 9, fill, fill");
    }

    private void initSecondCard() {
        this.panel_2 = new JPanel();
        this.contentPanel.add((Component)this.panel_2, "name_1465550427551281000");
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{100, 210, 67, 100, 0};
        gbl_panel.rowHeights = new int[]{30, 30, 30, 30, 0, 0};
        gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        this.panel_2.setLayout(gbl_panel);
        this.genesFile = new PathPicker("Genes file");
        GridBagConstraints gbc_genesFile = new GridBagConstraints();
        gbc_genesFile.anchor = 13;
        gbc_genesFile.insets = new Insets(0, 0, 5, 5);
        gbc_genesFile.gridx = 0;
        gbc_genesFile.gridy = 0;
        this.panel_2.add((Component)this.genesFile, gbc_genesFile);
        this.samplesFile = new PathPicker("Samples file");
        GridBagConstraints gbc_samplesFile = new GridBagConstraints();
        gbc_samplesFile.anchor = 13;
        gbc_samplesFile.insets = new Insets(0, 0, 5, 5);
        gbc_samplesFile.gridx = 0;
        gbc_samplesFile.gridy = 1;
        this.panel_2.add((Component)this.samplesFile, gbc_samplesFile);
        this.timesFile = new PathPicker("Times file");
        GridBagConstraints gbc_timesFile = new GridBagConstraints();
        gbc_timesFile.anchor = 13;
        gbc_timesFile.insets = new Insets(0, 0, 5, 5);
        gbc_timesFile.gridx = 0;
        gbc_timesFile.gridy = 2;
        this.panel_2.add((Component)this.timesFile, gbc_timesFile);
        this.sepRadio = new SepCharRadioButtons();
        GridBagConstraints gbc_sepRadio = new GridBagConstraints();
        gbc_sepRadio.gridwidth = 2;
        gbc_sepRadio.anchor = 17;
        gbc_sepRadio.insets = new Insets(0, 0, 5, 5);
        gbc_sepRadio.gridx = 0;
        gbc_sepRadio.gridy = 3;
        this.panel_2.add((Component)this.sepRadio, gbc_sepRadio);
        this.timeSize = (Integer)this.timePointsValue.getValue();
        this.sourceFiles = new SourceFilesPicker(this.timeSize);
        GridBagConstraints gbc_sourceFiles = new GridBagConstraints();
        gbc_sourceFiles.gridwidth = 2;
        gbc_sourceFiles.anchor = 17;
        gbc_sourceFiles.insets = new Insets(0, 0, 0, 5);
        gbc_sourceFiles.gridx = 0;
        gbc_sourceFiles.gridy = 4;
        this.panel_2.add((Component)this.sourceFiles, gbc_sourceFiles);
    }

    private void initThirdCard() {
        this.panel_3 = new JPanel();
        this.contentPanel.add((Component)this.panel_3, "name_1465550674496678000");
        GridBagLayout gbl_panel_3 = new GridBagLayout();
        gbl_panel_3.columnWidths = new int[]{35, 85, 0, 75, 0};
        gbl_panel_3.rowHeights = new int[]{14, 14, 14, 14, 14, 14, 0};
        gbl_panel_3.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        this.panel_3.setLayout(gbl_panel_3);
        JLabel lblMinG = new JLabel("min G");
        lblMinG.setFont(new Font("Arial", 0, 12));
        GridBagConstraints gbc_lblMinG = new GridBagConstraints();
        gbc_lblMinG.insets = new Insets(0, 0, 5, 5);
        gbc_lblMinG.gridx = 0;
        gbc_lblMinG.gridy = 0;
        this.panel_3.add((Component)lblMinG, gbc_lblMinG);
        this.minG_v = new JSpinner();
        this.minG_v.setFont(new Font("Arial", 0, 12));
        GridBagConstraints gbc_spinner_3 = new GridBagConstraints();
        gbc_spinner_3.fill = 2;
        gbc_spinner_3.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_3.gridx = 1;
        gbc_spinner_3.gridy = 0;
        this.panel_3.add((Component)this.minG_v, gbc_spinner_3);
        JLabel lblCard_1 = new JLabel("max G");
        lblCard_1.setFont(new Font("Arial", 0, 12));
        GridBagConstraints gbc_lblCard_1 = new GridBagConstraints();
        gbc_lblCard_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblCard_1.gridx = 2;
        gbc_lblCard_1.gridy = 0;
        this.panel_3.add((Component)lblCard_1, gbc_lblCard_1);
        this.maxG_v = new JSpinner();
        this.maxG_v.setFont(new Font("Arial", 0, 12));
        GridBagConstraints gbc_spinner = new GridBagConstraints();
        gbc_spinner.fill = 2;
        gbc_spinner.insets = new Insets(0, 0, 5, 0);
        gbc_spinner.gridx = 3;
        gbc_spinner.gridy = 0;
        this.panel_3.add((Component)this.maxG_v, gbc_spinner);
        JLabel lblMinC = new JLabel("min C");
        lblMinC.setFont(new Font("Arial", 0, 12));
        GridBagConstraints gbc_lblMinC = new GridBagConstraints();
        gbc_lblMinC.insets = new Insets(0, 0, 5, 5);
        gbc_lblMinC.gridx = 0;
        gbc_lblMinC.gridy = 1;
        this.panel_3.add((Component)lblMinC, gbc_lblMinC);
        this.minC_v = new JSpinner();
        this.minC_v.setFont(new Font("Arial", 0, 12));
        GridBagConstraints gbc_spinner_4 = new GridBagConstraints();
        gbc_spinner_4.fill = 2;
        gbc_spinner_4.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_4.gridx = 1;
        gbc_spinner_4.gridy = 1;
        this.panel_3.add((Component)this.minC_v, gbc_spinner_4);
        JLabel lblMaxC = new JLabel("max C");
        lblMaxC.setFont(new Font("Arial", 0, 12));
        GridBagConstraints gbc_lblMaxC = new GridBagConstraints();
        gbc_lblMaxC.insets = new Insets(0, 0, 5, 5);
        gbc_lblMaxC.gridx = 2;
        gbc_lblMaxC.gridy = 1;
        this.panel_3.add((Component)lblMaxC, gbc_lblMaxC);
        this.maxC_v = new JSpinner();
        this.maxC_v.setFont(new Font("Arial", 0, 12));
        GridBagConstraints gbc_spinner_1 = new GridBagConstraints();
        gbc_spinner_1.fill = 2;
        gbc_spinner_1.insets = new Insets(0, 0, 5, 0);
        gbc_spinner_1.gridx = 3;
        gbc_spinner_1.gridy = 1;
        this.panel_3.add((Component)this.maxC_v, gbc_spinner_1);
        JLabel lblMinT = new JLabel("min T");
        lblMinT.setFont(new Font("Arial", 0, 12));
        GridBagConstraints gbc_lblMinT = new GridBagConstraints();
        gbc_lblMinT.insets = new Insets(0, 0, 5, 5);
        gbc_lblMinT.gridx = 0;
        gbc_lblMinT.gridy = 2;
        this.panel_3.add((Component)lblMinT, gbc_lblMinT);
        this.minT_v = new JSpinner();
        this.minT_v.setFont(new Font("Arial", 0, 12));
        GridBagConstraints gbc_spinner_5 = new GridBagConstraints();
        gbc_spinner_5.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_5.fill = 2;
        gbc_spinner_5.gridx = 1;
        gbc_spinner_5.gridy = 2;
        this.panel_3.add((Component)this.minT_v, gbc_spinner_5);
        JLabel lblMaxT = new JLabel("max T");
        lblMaxT.setFont(new Font("Arial", 0, 12));
        GridBagConstraints gbc_lblMaxT = new GridBagConstraints();
        gbc_lblMaxT.insets = new Insets(0, 0, 5, 5);
        gbc_lblMaxT.gridx = 2;
        gbc_lblMaxT.gridy = 2;
        this.panel_3.add((Component)lblMaxT, gbc_lblMaxT);
        this.maxT_v = new JSpinner();
        this.maxT_v.setFont(new Font("Arial", 0, 12));
        GridBagConstraints gbc_spinner_2 = new GridBagConstraints();
        gbc_spinner_2.fill = 2;
        gbc_spinner_2.insets = new Insets(0, 0, 5, 0);
        gbc_spinner_2.gridx = 3;
        gbc_spinner_2.gridy = 2;
        this.panel_3.add((Component)this.maxT_v, gbc_spinner_2);
    }

    private void checkDatasetSize() {
        String path0 = this.sourceFiles.getSelectedPath(0);
        String[] paths = this.sourceFiles.getAllSelectedPaths();
        for (int i = 0; i < paths.length; ++i) {
            LOG.debug("path " + i + " = " + paths[i]);
        }
        try {
            ReadTable path0_table = new ReadTable(path0, this.sepRadio.getSelectedSep());
            List<List<String>> values = path0_table.getTable();
            this.geneSize = values.size();
            this.sampleSize = values.get(0).size();
            LOG.debug("sizes = [ " + this.geneSize + " , " + this.sampleSize + " , " + this.timeSize + " ]");
            if (this.geneSize > 1000) {
                this.maxG_v.setValue(new Integer(500));
            } else {
                this.maxG_v.setValue(new Integer(this.geneSize));
            }
            this.maxC_v.setValue(new Integer(this.sampleSize));
            this.maxT_v.setValue(new Integer(this.timeSize));
            if (this.geneSize < 20) {
                this.minG_v.setValue(new Integer(3));
            } else {
                this.minG_v.setValue(new Integer(20));
            }
            this.minC_v.setValue(new Integer(2));
            this.minT_v.setValue(new Integer(3));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printSummary() {
        this.id = DatasetModelServer.getInstance().getAnewID();
        String sum = "ID = " + this.id + "\n" + "Name = " + this.nameValue.getText() + "\n" + "Description = " + this.descriptionValue.getText() + "\n" + this.datasetType.getStringReport() + "\n" + "Gene size = " + this.geneSize + "\n" + "Sample size = " + this.sampleSize + "\n" + "Time size = " + this.timeSize + "\n" + "Genes path = " + this.genesFile.getSelectedPath() + "\n" + "Samples path = " + this.samplesFile.getSelectedPath() + "\n" + "Times path = " + this.timesFile.getSelectedPath() + "\n" + "Separator = " + this.sepRadio.getSelectedSep() + "\n" + this.sourceFiles.getReportString() + "\n" + "Min G = " + this.minG_v.getValue() + " , " + "Max G = " + this.maxG_v.getValue() + "\n" + "Min C = " + this.minC_v.getValue() + " , " + "Max C = " + this.maxC_v.getValue() + "\n" + "Min T = " + this.minT_v.getValue() + " , " + "Max T = " + this.maxT_v.getValue();
        this.summary.setText(sum);
    }

    private void loadDatasetResources() {
        DatasetModelServer server = DatasetModelServer.getInstance();
        String[] sourcePaths = this.sourceFiles.getAllSelectedPaths();
        String[] sourceNames = new String[sourcePaths.length];
        for (int i = 0; i < sourcePaths.length; ++i) {
            sourceNames[i] = TextUtilities.getFileName(sourcePaths[i]);
        }
        String genePath = this.genesFile.getSelectedPath();
        String geneName = TextUtilities.getFileName(genePath);
        String samplePath = this.samplesFile.getSelectedPath();
        String sampleName = TextUtilities.getFileName(samplePath);
        String timePath = this.timesFile.getSelectedPath();
        String timeName = TextUtilities.getFileName(timePath);
        try {
            server.writeNewDataset(this.id, this.nameValue.getText(), this.datasetType.getTypeSelected(), this.geneSize, this.sampleSize, this.timeSize, (Integer)this.minG_v.getValue(), (Integer)this.maxG_v.getValue(), (Integer)this.minC_v.getValue(), (Integer)this.maxC_v.getValue(), (Integer)this.minT_v.getValue(), (Integer)this.maxT_v.getValue(), this.datasetType.getOrganismText().getText(), this.descriptionValue.getText(), this.sepRadio.getSelectedSep(), sourceNames, geneName, sampleName, timeName);
        }
        catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        catch (TransformerException e) {
            e.printStackTrace();
        }
        LOG.debug("New dataset written in Xml");
        String dirP = TextUtilities.appendToPath(SystemUtilities.getResourcesFolderPath(), this.id);
        Path dirPath = Paths.get(dirP, new String[0]);
        LOG.debug("Build directory");
        LOG.debug(dirP);
        try {
            Files.createDirectory(dirPath, new FileAttribute[0]);
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        LOG.debug("Copying data files");
        for (int i = 0; i < sourcePaths.length; ++i) {
            Path source = Paths.get(sourcePaths[i], new String[0]);
            Path target = Paths.get(dirP, sourceNames[i]);
            try {
                Files.copy(source, target, new CopyOption[0]);
                continue;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        LOG.debug("data copied");
        Path sourceGenes = Paths.get(genePath, new String[0]);
        Path targetGenes = Paths.get(dirP, geneName);
        Path sourceSamples = Paths.get(samplePath, new String[0]);
        Path targetSamples = Paths.get(dirP, sampleName);
        Path sourceTimes = Paths.get(timePath, new String[0]);
        Path targetTimes = Paths.get(dirP, timeName);
        LOG.debug("Copying gene file");
        try {
            Files.copy(sourceGenes, targetGenes, new CopyOption[0]);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        LOG.debug("Copied");
        LOG.debug("Copying sample file");
        try {
            Files.copy(sourceSamples, targetSamples, new CopyOption[0]);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        LOG.debug("Copied");
        LOG.debug("Copying time file");
        try {
            Files.copy(sourceTimes, targetTimes, new CopyOption[0]);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        LOG.debug("Copied");
        LOG.debug("Dataset loaded!!");
        JOptionPane.showMessageDialog(null, "The dataset has been loaded!!", "", -1);
        DatasetModelServer.getInstance().updateDatasetServer();
        this.dispose();
    }
}

