/*
 * Decompiled with CFR 0.152.
 */
package algcore;

import algcore.AlgorithmIndividual;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public interface DataHierarchy {
    public ArrayList<ArrayList<Collection<Integer>>> build_gst_coorinates(int var1);

    public ArrayList<ArrayList<Collection<Integer>>> build_gs_coorinates(int var1);

    public ArrayList<Collection<Integer>> build_i_coorinates(int var1, char var2);

    public void initialize(int var1, int var2, int var3);

    public void update(AlgorithmIndividual var1);

    public boolean isAvailable();

    public double getPercentage();

    public Map<Integer, Integer> getGenHierarchy();

    public Map<Integer, Integer> getSampleHierarchy();

    public Map<Integer, Integer> getTimeHierarchy();
}

