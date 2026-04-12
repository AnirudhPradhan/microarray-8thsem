/*
 * Decompiled with CFR 0.152.
 */
package alginterface;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algcore.DataHierarchy;
import algcore.TriGen;
import algentrypoint.FootBridge;
import algutils.TriGenBuilder;
import general.Parser;
import input.algorithm.Control;
import input.laboratory.AnalysisResources;
import input.laboratory.WrongOptionsException;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import labentrypoint.Facade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SystemUtilities;
import utils.TextUtilities;

public class TriGenGuiTask
extends SwingWorker<List<AlgorithmIndividual>, List<AlgorithmIndividual>> {
    private static final Logger LOG = LoggerFactory.getLogger(TriGenGuiTask.class);
    private Control control;
    private long t1;
    private long t2;
    private JPanel panel;
    private JButton okButton;
    private JButton cancelButton;
    private JTextArea message;

    public TriGenGuiTask(Control control) {
        this.control = control;
        TriGen.getInstance().setTask(this);
    }

    @Override
    protected List<AlgorithmIndividual> doInBackground() throws Exception {
        FootBridge.buildParameters(this.control);
        AlgorithmConfiguration config = AlgorithmConfiguration.getInstance();
        LOG.debug("ID = " + Thread.currentThread().getId());
        LOG.debug("Name = " + Thread.currentThread().getName());
        LOG.info("Run configuration:\n" + config.getReportString());
        LOG.info("Implementation:\n" + this.control.getImplementation() + "\n");
        TriGen trigen = TriGen.getInstance();
        TriGenBuilder.getInstance().buildTriGen(this.control.getImplementation());
        Calendar now1 = Calendar.getInstance();
        LOG.info("Executing algoritm:");
        this.t1 = now1.getTimeInMillis();
        List<AlgorithmIndividual> r = trigen.runAlgorithm();
        return r;
    }

    @Override
    protected void done() {
        if (TriGen.getInstance().isCancelled()) {
            Toolkit.getDefaultToolkit().beep();
            this.okButton.setEnabled(true);
            this.cancelButton.setEnabled(false);
            this.panel.setCursor(Cursor.getPredefinedCursor(0));
            this.message.setText(SystemUtilities.getLabelProperty("cancelled_message"));
        } else {
            Calendar now2 = Calendar.getInstance();
            this.t2 = now2.getTimeInMillis();
            AlgorithmConfiguration config = AlgorithmConfiguration.getInstance();
            String timeString = TextUtilities.getTimeString(this.t1, this.t2);
            List r = null;
            File solDir = null;
            try {
                r = (List)this.get();
                DataHierarchy hierarchy = config.getDataHierarchy();
                AnalysisResources lr = new AnalysisResources(this.control, this.t2 - this.t1);
                lr.loadLegacyDataHierarchy(hierarchy.getGenHierarchy(), hierarchy.getSampleHierarchy(), hierarchy.getTimeHierarchy());
                for (AlgorithmIndividual tri : r) {
                    lr.loadOneSolution(tri.getGenes(), tri.getSamples(), tri.getTimes());
                    lr.loadOneSlog(tri.getRegisterReport(";"));
                }
                LOG.info("");
                LOG.info(timeString);
                LOG.info("Building the solution file");
                solDir = new File(this.control.getOutFolder() + SystemUtilities.getFileSep() + this.control.getOutName());
                solDir.mkdirs();
                Parser.buildSolutionFile(lr, solDir.getAbsolutePath());
                Facade.buildCompleteResultsFiles(lr, solDir.getAbsolutePath());
                LOG.info("DONE!");
            }
            catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (InstantiationException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (WrongOptionsException e) {
                e.printStackTrace();
            }
            catch (SecurityException e) {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            Toolkit.getDefaultToolkit().beep();
            this.okButton.setEnabled(true);
            this.cancelButton.setEnabled(false);
            this.panel.setCursor(Cursor.getPredefinedCursor(0));
            this.message.setText(SystemUtilities.getLabelProperty("completed_task"));
            this.message.append("\n" + SystemUtilities.getLabelProperty("results_message") + solDir.getAbsolutePath());
        }
    }

    public void setProgress(double i) {
        this.setProgress((int)i);
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public void setOkButton(JButton button) {
        this.okButton = button;
    }

    public void setCancelButton(JButton button) {
        this.cancelButton = button;
    }

    public void setMessage(JTextArea message) {
        this.message = message;
    }
}

