/*
 * Decompiled with CFR 0.152.
 */
package analysis.biological;

import analysis.biological.GoStudy;
import analysis.biological.GoTerm;
import general.Tricluster;
import input.datasets.Biological;
import input.laboratory.Options;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import labutils.GeneOntology;
import utils.InTextFile;
import utils.OutTextFile;
import utils.SystemUtilities;
import utils.TextUtilities;

public class BiologicalAnalysis {
    private String currentCommand;
    private String appPath;
    private String mtc;
    private String calculation;
    private String dot;
    private String filter;
    private boolean ignore;
    private boolean annotation;
    private String resamplingsteps;
    private String go;
    private String association;
    private String population;
    private String outdir;
    private String studyset;
    private File namesFolder;
    private File tablesFolder;
    private File bufferFile;
    private String[] currentGeneNames;

    public BiologicalAnalysis(Biological resources, Options options) throws IOException {
        this.mtc = options.getGoMtc();
        this.calculation = options.getGoCalculation();
        this.dot = options.getGoDot();
        this.filter = options.getGoFilter();
        this.ignore = options.isGoIgnore();
        this.annotation = options.isGoAnnotation();
        this.resamplingsteps = options.getGOrsteps();
        String outName = options.getOutName();
        this.namesFolder = new File(options.getOutFolder(), "temp-" + outName + "-GOnames");
        this.namesFolder.mkdir();
        this.tablesFolder = new File(options.getOutFolder(), "temp-" + outName + "-GOtables");
        this.tablesFolder.mkdir();
        this.bufferFile = new File(options.getOutFolder(), "temp-" + outName + "_buffer.txt");
        this.studyset = this.namesFolder.getAbsolutePath();
        this.outdir = this.tablesFolder.getAbsolutePath();
        String org = resources.getOrganism().toLowerCase();
        this.appPath = SystemUtilities.getGOappPath();
        this.go = SystemUtilities.getGOtermsPath();
        this.association = SystemUtilities.getGOassociationPath(org);
        this.population = resources.getGenesPath();
        this.currentGeneNames = resources.getGeneNames();
        this.currentCommand = "";
    }

    public List<GoStudy> getAnalysis(List<Tricluster> triclusters, boolean persistent) throws IOException, InterruptedException {
        ArrayList<GoStudy> gostudies = new ArrayList<GoStudy>();
        List<String[]> tags = GeneOntology.getAllGeneNames(triclusters, this.currentGeneNames);
        this.createStudySets(tags);
        this.launchGOApp();
        File[] tableFiles = this.tablesFolder.listFiles();
        Arrays.sort(tableFiles, new FileNameComparator());
        for (int i = 0; i < tableFiles.length; ++i) {
            File table = tableFiles[i];
            InTextFile f = new InTextFile(table);
            GoStudy st = new GoStudy("sol_" + (i + 1));
            int j = 0;
            for (String line : f) {
                List<String> ls;
                if (j != 0 && (ls = TextUtilities.splitElements(line, "\t")).size() > 1) {
                    if (j == 1) {
                        int popTotal = Integer.parseInt(ls.get(1));
                        int studyTotal = Integer.parseInt(ls.get(3));
                        st.setPopTotal(popTotal);
                        st.setStudyTotal(studyTotal);
                    }
                    String id = ls.get(0);
                    String name = ls.get(8).substring(1, ls.get(8).length() - 1);
                    double p = Double.parseDouble(ls.get(5));
                    double pAdjusted = Double.parseDouble(ls.get(6));
                    double pMin = Double.parseDouble(ls.get(7));
                    int popTerm = Integer.parseInt(ls.get(2));
                    int studyTerm = Integer.parseInt(ls.get(4));
                    GoTerm gt = new GoTerm(id, name, p, pAdjusted, pMin, popTerm, studyTerm);
                    st.addGoTerm(gt);
                }
                ++j;
            }
            f.close();
            gostudies.add(st);
        }
        if (!persistent) {
            this.deleteFoler(this.namesFolder);
            this.deleteFoler(this.tablesFolder);
            this.bufferFile.delete();
        }
        return gostudies;
    }

    public String getAppPath() {
        return this.appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public String getMtc() {
        return this.mtc;
    }

    public void setMtc(String mtc) {
        this.mtc = mtc;
    }

    public String getCalculation() {
        return this.calculation;
    }

    public void setCalculation(String calculation) {
        this.calculation = calculation;
    }

    public String getDot() {
        return this.dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }

    public String getFilter() {
        return this.filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public boolean getIgnore() {
        return this.ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean getAnnotation() {
        return this.annotation;
    }

    public void setAnnotation(boolean annotation) {
        this.annotation = annotation;
    }

    public String getResamplingsteps() {
        return this.resamplingsteps;
    }

    public void setResamplingsteps(String resamplingsteps) {
        this.resamplingsteps = resamplingsteps;
    }

    private void createStudySets(List<String[]> geneNames) throws FileNotFoundException {
        int index = 1;
        for (String[] geneList : geneNames) {
            String filePath = this.namesFolder.getAbsolutePath() + "/genes_" + index + ".txt";
            OutTextFile f = new OutTextFile(filePath);
            for (int i = 0; i < geneList.length; ++i) {
                f.println(geneList[i]);
            }
            f.close();
            ++index;
        }
    }

    private void launchGOApp() throws IOException, InterruptedException {
        this.runCurrentCommand();
    }

    private void runCurrentCommand() throws IOException, InterruptedException {
        java.util.List<String> cmd = new java.util.ArrayList<String>();
        cmd.add("java");
        cmd.add("-jar");
        cmd.add(this.appPath);
        cmd.add("-g"); cmd.add(this.go);
        cmd.add("-a"); cmd.add(this.association);
        cmd.add("-p"); cmd.add(this.population);
        cmd.add("-s"); cmd.add(this.studyset);
        cmd.add("-o"); cmd.add(this.outdir);
        cmd.add("-m"); cmd.add(this.mtc);
        cmd.add("-c"); cmd.add(this.calculation);
        if (this.filter != null && !this.filter.equalsIgnoreCase("")) {
            cmd.add("-f"); cmd.add(this.filter);
        }
        if (this.ignore) {
            cmd.add("-i");
        }
        if (this.annotation) {
            cmd.add("-n");
        }
        if (this.resamplingsteps != null && !this.resamplingsteps.equalsIgnoreCase("")) {
            cmd.add("-r"); cmd.add(this.resamplingsteps);
        }
        File logFile = new File(this.outdir).getParentFile() != null ? new File(new File(this.outdir).getParentFile(), "ontologizer_debug.log") : this.bufferFile;
        try {
            OutTextFile log = new OutTextFile(logFile);
            log.print("Ontologizer command: " + String.join(" ", cmd) + "\n");
            log.close();
        } catch (Exception e) { }
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        OutTextFile buffer = new OutTextFile(this.bufferFile);
        try {
            Process p = pb.start();
            InputStream stdout = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdout);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder allOutput = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.print(line + "\n");
                allOutput.append(line).append("\n");
            }
            int exitCode = p.waitFor();
            buffer.close();
            try {
                OutTextFile log = new OutTextFile(logFile);
                log.print("Exit code: " + exitCode + "\n");
                log.print(allOutput.toString());
                log.close();
            } catch (Exception e) { }
        } catch (Throwable t) {
            buffer.print("Error: " + t.getMessage());
            try {
                OutTextFile log = new OutTextFile(logFile);
                log.print("Exception: " + t.toString() + "\n");
                log.close();
            } catch (Exception e) { }
            t.printStackTrace();
        }
    }

    private void deleteFoler(File folder) {
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; ++i) {
            if (files[i].isDirectory()) {
                this.deleteFoler(files[i]);
            }
            files[i].delete();
        }
        folder.delete();
    }

    private class FileNameComparator
    implements Comparator<File> {
        private FileNameComparator() {
        }

        @Override
        public int compare(File f1, File f2) {
            int i1 = TextUtilities.getIndexFromGOfileName(f1.getName());
            int i2 = TextUtilities.getIndexFromGOfileName(f2.getName());
            return Integer.compare(i1, i2);
        }
    }
}

