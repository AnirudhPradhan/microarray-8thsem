/*
 * Decompiled with CFR 0.152.
 */
package input.laboratory;

import general.Tricluster;
import input.datasets.Biological;
import java.util.List;

public class OPTsolBatch {
    private List<Tricluster> triclusters;
    private Biological dataset;

    public OPTsolBatch(List<Tricluster> triclusters, Biological dataset) {
        this.triclusters = triclusters;
        this.dataset = dataset;
    }

    public List<Tricluster> getTriclusters() {
        return this.triclusters;
    }

    public Biological getDataset() {
        return this.dataset;
    }
}

