/*
 * Decompiled with CFR 0.152.
 */
package input.laboratory;

import general.Tricluster;
import input.datasets.Common;
import input.laboratory.CommonAnalysisResources;
import java.util.List;

public class ReducedAnalysisResources
extends CommonAnalysisResources {
    private Common dataset;

    public ReducedAnalysisResources(char analysisType, String experimentAlias, List<Tricluster> solutions, Common dataset) {
        super(analysisType, experimentAlias, solutions);
        this.dataset = dataset;
    }

    public Common getDataset() {
        return this.dataset;
    }

    @Override
    public String toString() {
        String r = super.toString();
        r = r + "\nDataset = " + this.dataset.getDatasetName() + " (ID=" + this.dataset.getDatasetID() + ")";
        return r;
    }
}

