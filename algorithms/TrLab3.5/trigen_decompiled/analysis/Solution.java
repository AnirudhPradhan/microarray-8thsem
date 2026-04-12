/*
 * Decompiled with CFR 0.152.
 */
package analysis;

import analysis.biological.GoLevels;
import analysis.biological.GoSlot;
import analysis.biological.GoStudy;
import general.Tricluster;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution
implements Comparable<Solution> {
    private int index;
    private String name;
    private Tricluster tricluster;
    private Map<String, Double> variables;
    private GoLevels goLevels;
    private GoStudy goStudy;

    public Solution(int index, Tricluster tricluster) {
        this.index = index;
        this.tricluster = tricluster;
        this.variables = new HashMap<String, Double>();
        this.intializeVariables();
        this.name = "TRI_{" + this.index + "}";
    }

    private void intializeVariables() {
        double init = -1.0;
        this.variables.put("grq", new Double(init));
        this.variables.put("peq", new Double(init));
        this.variables.put("spq", new Double(init));
        this.variables.put("bioq", new Double(init));
        this.variables.put("bioqn", new Double(init));
        this.variables.put("triqn", new Double(init));
        this.variables.put("triq", new Double(init));
    }

    public int getIndex() {
        return this.index;
    }

    public Tricluster getTricluster() {
        return this.tricluster;
    }

    public String getName() {
        return this.name;
    }

    public double getValue(String variable) {
        return this.variables.get(variable);
    }

    public double putValue(String variable, double value) {
        return this.variables.put(variable, new Double(value));
    }

    public void setGoLevels(GoLevels goLevels) {
        this.goLevels = goLevels;
    }

    public List<GoSlot> getGoSlots(String type) {
        List<GoSlot> slots = null;
        switch (type) {
            case "pa": {
                slots = this.goLevels.getPa();
                break;
            }
            case "p": {
                slots = this.goLevels.getP();
            }
        }
        return slots;
    }

    @Override
    public int compareTo(Solution o) {
        int i1 = this.index;
        int i2 = o.getIndex();
        return Integer.compare(i1, i2);
    }

    public GoStudy getGoStudy() {
        return this.goStudy;
    }

    public void setGoStudy(GoStudy goStudy) {
        this.goStudy = goStudy;
    }
}

