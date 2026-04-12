/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import analysis.Experiment;
import input.algorithm.InvalidImplementationException;
import input.algorithm.WrongContolException;
import input.laboratory.AnalysisResources;
import input.laboratory.Options;
import input.laboratory.WrongOptionsException;
import input.workflows.LabOneSolRun;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import labentrypoint.Facade;
import labinterface.BioExperimentLevelFrame;
import labinterface.CommonExperimentLevelFrame;
import labinterface.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TextUtilities;

public class SingleTaskConsole
extends JPanel {
    private static final Logger LOG = LoggerFactory.getLogger(SingleTaskConsole.class);
    private JTextArea console;
    private SingleAnalysisTask task;

    public SingleTaskConsole() {
        super(new BorderLayout());
        this.setUI();
    }

    private void setUI() {
        this.console = new JTextArea();
        this.console.setBackground(SystemColor.window);
        this.console.setForeground(Color.BLUE);
        this.console.setFont(new Font("Arial", 3, 14));
        this.console.setVisible(true);
        JScrollPane spanel = new JScrollPane(this.console);
        spanel.setPreferredSize(new Dimension(200, 800));
        this.add((Component)spanel, "Center");
    }

    public void showMessage(String msg) {
        this.console.setText("");
        this.console.append(msg + "\n");
    }

    public void launch(String selectedPath) {
        this.console.setText("");
        this.console.append("Starting analysis of: " + selectedPath + "\n");
        this.console.paintImmediately(this.console.getVisibleRect());
        this.task = new SingleAnalysisTask(selectedPath);
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                SingleTaskConsole.this.setCursor(Cursor.getPredefinedCursor(3));
                SingleTaskConsole.this.task.execute();
            }
        });
    }

    private class SingleAnalysisTask
    extends SwingWorker<Experiment, TaskStatus> {
        private String selectedPath;

        public SingleAnalysisTask(String selectedPath) {
            this.selectedPath = selectedPath;
        }

        @Override
        protected Experiment doInBackground() throws Exception {
            int progress = 0;
            LabOneSolRun wf = new LabOneSolRun();
            Experiment r = null;
            try {
                if (this.checkSolFile(this.selectedPath)) {
                    AnalysisResources currentSolutions = wf.loadSolution(this.selectedPath);
                    this.publish(new TaskStatus(progress, "\n[" + this.selectedPath + "] \nSolution loaded \nBuilding default options..."));
                    File aux = new File(this.selectedPath);
                    File outFolder = new File(aux.getParent());
                    String outName = TextUtilities.getFileNameWithoutExtension(aux.getName());
                    Options opts = new Options(outFolder, outName);
                    LOG.info("Loaded: " + this.selectedPath);
                    this.publish(new TaskStatus(progress, "\n[" + this.selectedPath + "] \nDefault options built\nComputing experiment analysis..."));
                    r = Facade.buildAnalysis(currentSolutions, opts, false);
                    this.publish(new TaskStatus(progress, "\n[" + this.selectedPath + "] \nExperiment analysis done\nShowing up the results..."));
                } else {
                    this.publish(new TaskStatus(progress, "\n[" + this.selectedPath + "] \nThis is not an experiment file"));
                }
            }
            catch (InstantiationException e1) {
                e1.printStackTrace();
                this.publish(new TaskStatus(progress, e1.getMessage()));
            }
            catch (IllegalAccessException e1) {
                e1.printStackTrace();
                this.publish(new TaskStatus(progress, e1.getMessage()));
            }
            catch (ClassNotFoundException e1) {
                e1.printStackTrace();
                this.publish(new TaskStatus(progress, e1.getMessage()));
            }
            catch (InterruptedException e1) {
                e1.printStackTrace();
                this.publish(new TaskStatus(progress, e1.getMessage()));
            }
            catch (WrongOptionsException e1) {
                e1.printStackTrace();
                this.publish(new TaskStatus(progress, e1.getMessage()));
            }
            catch (IOException e1) {
                e1.printStackTrace();
                this.publish(new TaskStatus(progress, e1.getMessage()));
            }
            catch (WrongContolException e1) {
                e1.printStackTrace();
                this.publish(new TaskStatus(progress, e1.getMessage()));
            }
            catch (InvalidImplementationException e1) {
                e1.printStackTrace();
                this.publish(new TaskStatus(progress, e1.getMessage()));
            }
            catch (Exception e1) {
                e1.printStackTrace();
                String msg = e1.getMessage();
                if (msg == null) msg = e1.getClass().getName();
                this.publish(new TaskStatus(progress, "\nError: " + msg));
            }
            return r;
        }

        @Override
        protected void process(List<TaskStatus> status) {
            TaskStatus st = status.get(status.size() - 1);
            String line = st.getDescription();
            LOG.debug(line);
            SingleTaskConsole.this.console.append(line);
        }

        @Override
        protected void done() {
            SingleTaskConsole.this.setCursor(Cursor.getPredefinedCursor(0));
            try {
                Experiment exp = (Experiment)this.get();
                if (exp != null) {
                    if (exp.getAnalysisType() == 'b') {
                        LOG.debug("" + exp.getAnalysisType());
                        SingleTaskConsole.this.console.append("\nAnalysis type: biological\n");
                        BioExperimentLevelFrame exp_l = new BioExperimentLevelFrame(exp);
                        exp_l.setVisible(true);
                        exp_l.toFront();
                        exp_l.requestFocus();
                    } else if (exp.getAnalysisType() == 'c') {
                        LOG.debug("" + exp.getAnalysisType());
                        SingleTaskConsole.this.console.append("\nAnalysis type: common\n");
                        CommonExperimentLevelFrame exp_l = new CommonExperimentLevelFrame(exp);
                        exp_l.setVisible(true);
                        exp_l.toFront();
                        exp_l.requestFocus();
                    }
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (ExecutionException e) {
                Throwable cause = e.getCause();
                String err = cause != null ? cause.getMessage() : e.getMessage();
                if (err == null) err = cause != null ? cause.getClass().getName() : e.getClass().getName();
                SingleTaskConsole.this.console.append("\nError: " + err + "\n");
                e.printStackTrace();
            }
            catch (Exception e) {
                SingleTaskConsole.this.console.append("\nError showing results: " + (e.getMessage() != null ? e.getMessage() : e.getClass().getName()) + "\n");
                e.printStackTrace();
            }
        }

        private boolean checkSolFile(String path) {
            boolean r = false;
            String ext = TextUtilities.getFileExtension(path);
            if (ext.equalsIgnoreCase("sol")) {
                r = true;
            }
            return r;
        }
    }
}

