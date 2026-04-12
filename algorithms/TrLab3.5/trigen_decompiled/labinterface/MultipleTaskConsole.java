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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import labentrypoint.Facade;
import labinterface.MultipleAnalysisExpLevelFrame;
import labinterface.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TextUtilities;

public class MultipleTaskConsole
extends JPanel {
    private static final Logger LOG = LoggerFactory.getLogger(MultipleTaskConsole.class);
    private JTextArea console;
    private MultipleAnalysisTask task;

    public MultipleTaskConsole() {
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

    public void launch(String selectedPath) {
        this.console.setText("");
        this.task = new MultipleAnalysisTask(selectedPath);
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                MultipleTaskConsole.this.setCursor(Cursor.getPredefinedCursor(3));
                MultipleTaskConsole.this.task.execute();
            }
        });
    }

    private class MultipleAnalysisTask
    extends SwingWorker<List<List<Experiment>>, TaskStatus> {
        private String selectedPath;

        public MultipleAnalysisTask(String selectedPath) {
            this.selectedPath = selectedPath;
        }

        @Override
        protected List<List<Experiment>> doInBackground() throws Exception {
            File folder = new File(this.selectedPath);
            File[] files = folder.listFiles();
            ArrayList<List<Experiment>> r = new ArrayList<List<Experiment>>(2);
            LinkedList<Experiment> bioExpGroup = new LinkedList<Experiment>();
            LinkedList<Experiment> comExpGroup = new LinkedList<Experiment>();
            int progress = 0;
            double max = files.length;
            this.publish(new TaskStatus(progress, "Loaded folder: " + this.selectedPath + "\nProcessing files\n\n"));
            try {
                for (int i = 0; i < files.length; ++i) {
                    progress = (int)((double)(i + 1) / max * 100.0);
                    File aux = files[i];
                    if (!this.checkSolFile(aux)) continue;
                    LabOneSolRun wf = new LabOneSolRun();
                    AnalysisResources cSol = wf.loadSolution(aux.getAbsolutePath());
                    File outFolder = new File(aux.getParent());
                    String outName = TextUtilities.getFileNameWithoutExtension(aux.getName());
                    Options opts = new Options(outFolder, outName);
                    this.publish(new TaskStatus(progress, "File: " + aux.getAbsolutePath() + "\nAnalysing Results #" + i + " Name = " + cSol.getExperimentAlias() + "\n"));
                    Experiment exp = Facade.buildAnalysis(cSol, opts, false);
                    if (cSol.getAnalysisType() == 'b') {
                        bioExpGroup.add(exp);
                        continue;
                    }
                    if (cSol.getAnalysisType() != 'c') continue;
                    comExpGroup.add(exp);
                }
                r.add(bioExpGroup);
                r.add(comExpGroup);
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
            catch (WrongOptionsException e1) {
                e1.printStackTrace();
                this.publish(new TaskStatus(progress, e1.getMessage()));
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
            return r;
        }

        @Override
        protected void process(List<TaskStatus> status) {
            TaskStatus st = status.get(status.size() - 1);
            String line = "";
            line = st.getProgress() != 100 ? st.getProgress() + " %  ->  " + st.getDescription() : st.getDescription();
            LOG.debug(line);
            MultipleTaskConsole.this.console.append(line);
        }

        @Override
        protected void done() {
            try {
                List aux = (List)this.get();
                List bioExpGroup = (List)aux.get(0);
                List comExpGroup = (List)aux.get(1);
                String check = "";
                check = check + "\n\nBiological experiments:\n";
                for (Experiment res : bioExpGroup) {
                    check = check + "    Bio Results #" + bioExpGroup.indexOf(res) + ": " + res.getExperimentName() + "\n";
                }
                check = check + "\n\nCommon experiments:\n";
                for (Experiment res : comExpGroup) {
                    check = check + "    Com Results #" + comExpGroup.indexOf(res) + ": " + res.getExperimentName() + "\n";
                }
                check = check + "\nDone";
                LOG.debug("Done");
                MultipleTaskConsole.this.setCursor(Cursor.getPredefinedCursor(0));
                this.publish(new TaskStatus(100, check));
                MultipleAnalysisExpLevelFrame expFrame = new MultipleAnalysisExpLevelFrame(bioExpGroup, comExpGroup);
                expFrame.setVisible(true);
            }
            catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        private boolean checkSolFile(File f) {
            String ext;
            boolean r = false;
            if (!f.isHidden() && f.isFile() && (ext = TextUtilities.getFileExtension(f.getAbsolutePath())).equalsIgnoreCase("sol")) {
                r = true;
            }
            return r;
        }
    }
}

