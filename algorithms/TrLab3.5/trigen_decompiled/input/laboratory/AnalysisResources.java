/*
 * Decompiled with CFR 0.152.
 */
package input.laboratory;

import general.Tricluster;
import input.algorithm.Control;
import input.laboratory.CommonAnalysisResources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import utils.TextUtilities;
import utils.WorkFlowUtilities;

public class AnalysisResources
extends CommonAnalysisResources {
    private long execTime;
    private Control control;
    private Map<Integer, Integer> ghie;
    private Map<Integer, Integer> chie;
    private Map<Integer, Integer> thie;
    private List<String> slogs;

    public AnalysisResources(char analysisType, String experimentAlias, List<Tricluster> solutions, Control control, long execTime, Map<Integer, Integer> gh, Map<Integer, Integer> ch, Map<Integer, Integer> th) {
        super(analysisType, experimentAlias, solutions);
        this.assignments(control, execTime, gh, ch, th);
    }

    public AnalysisResources(char analysisType, String experimentAlias, List<Tricluster> solutions, Control control, long execTime, Map<Integer, Integer> gh, Map<Integer, Integer> ch, Map<Integer, Integer> th, List<String> slogs) {
        super(analysisType, experimentAlias, solutions);
        this.assignments(control, execTime, gh, ch, th, slogs);
    }

    private void assignments(Control control, long execTime, Map<Integer, Integer> gh, Map<Integer, Integer> ch, Map<Integer, Integer> th) {
        ArrayList<String> slogs = new ArrayList<String>();
        slogs.add("none");
        this.assignments(control, execTime, gh, ch, th, slogs);
    }

    private void assignments(Control control, long execTime, Map<Integer, Integer> gh, Map<Integer, Integer> ch, Map<Integer, Integer> th, List<String> slogs) {
        this.control = control;
        this.execTime = execTime;
        this.ghie = gh;
        this.chie = ch;
        this.thie = th;
        this.slogs = slogs;
    }

    public AnalysisResources(Control control, long execTime) {
        super(WorkFlowUtilities.getExperimentTypeFromDataset(control.getDataset()), control.getOutName());
        this.control = control;
        this.execTime = execTime;
        this.slogs = new ArrayList<String>(control.getN());
    }

    public AnalysisResources(char analysisType, String experimentAlias, Control control, long execTime) {
        super(analysisType, experimentAlias);
        this.control = control;
        this.execTime = execTime;
        this.slogs = new ArrayList<String>(control.getN());
    }

    public void loadLegacyDataHierarchy(Map<Integer, Integer> oldGh, Map<Integer, Integer> oldCh, Map<Integer, Integer> oldTh) {
        this.ghie = this.changeDataHierarcchy(oldGh);
        this.chie = this.changeDataHierarcchy(oldCh);
        this.thie = this.changeDataHierarcchy(oldTh);
    }

    public void loadOneSlog(String log) {
        this.slogs.add(log);
    }

    public Control getControl() {
        return this.control;
    }

    public long getExecTime() {
        return this.execTime;
    }

    public Map<Integer, Integer> getGhie() {
        return this.ghie;
    }

    public Map<Integer, Integer> getChie() {
        return this.chie;
    }

    public Map<Integer, Integer> getThie() {
        return this.thie;
    }

    public List<String> getSlogs() {
        return this.slogs;
    }

    @Override
    public String toString() {
        String r = super.toString();
        r = r + "\nExecution time = " + TextUtilities.getTimeString(this.execTime) + "\nControl" + this.control + "\nGene data hierarcy = " + this.getDataHiearchyString(this.ghie) + "\nCondition data hierachy = " + this.getDataHiearchyString(this.chie) + "\nTime data hierarchy = " + this.getDataHiearchyString(this.thie) + "\nFirst slog = " + this.slogs.get(0) + "\nLast slog = " + this.slogs.get(this.slogs.size() - 1);
        return r;
    }

    private String getDataHiearchyString(Map<Integer, Integer> hierarchy) {
        String r = "";
        Set<Integer> auxlevels = hierarchy.keySet();
        ArrayList<Integer> auxLlevels = new ArrayList<Integer>(auxlevels);
        Collections.sort(auxLlevels);
        int iaux = 0;
        for (Integer auxL : auxLlevels) {
            Integer count = hierarchy.get(auxL);
            String sufix = "";
            if (iaux != auxLlevels.size() - 1) {
                sufix = " , ";
            }
            r = r + "Level " + auxL + ": " + count + sufix;
            ++iaux;
        }
        return r;
    }

    private Map<Integer, Integer> changeDataHierarcchy(Map<Integer, Integer> oldHierarchy) {
        HashMap<Integer, Integer> newHierarchy = new HashMap<Integer, Integer>();
        HashMap aux = new HashMap();
        HashSet<Integer> levels = new HashSet<Integer>(oldHierarchy.values());
        Set<Map.Entry<Integer, Integer>> entryset = oldHierarchy.entrySet();
        for (Integer level : levels) {
            int levelValue = level;
            ArrayList<Integer> coordinates = new ArrayList<Integer>();
            for (Map.Entry<Integer, Integer> en : entryset) {
                int enKey = en.getKey();
                int enValue = en.getValue();
                if (levelValue != enValue) continue;
                coordinates.add(new Integer(enKey));
            }
            Collections.sort(coordinates);
            aux.put(new Integer(levelValue), coordinates);
        }
        Set auxlevels = aux.keySet();
        ArrayList auxLlevels = new ArrayList(auxlevels);
        Collections.sort(auxLlevels);
        for (Integer auxL : auxLlevels) {
            List co = (List)aux.get(auxL);
            newHierarchy.put(new Integer(auxL), new Integer(co.size()));
        }
        return newHierarchy;
    }
}

