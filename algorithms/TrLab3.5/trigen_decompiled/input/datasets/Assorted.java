/*
 * Decompiled with CFR 0.152.
 */
package input.datasets;

import input.datasets.Real;

public class Assorted
extends Real {
    private static final long serialVersionUID = 2136629192023387798L;

    public Assorted(String datasetID, String datasetName, String datasetType, String geneSize, String sampleSize, String timeSize, String[] paths, String sep, String defMinG, String defMaxG, String defMinC, String defMaxC, String defMinT, String defMaxT, String gNamesPath, String sNamesPath, String tNamesPath) {
        super(datasetID, datasetName, datasetType, geneSize, sampleSize, timeSize, paths, sep, defMinG, defMaxG, defMinC, defMaxC, defMinT, defMaxT, gNamesPath, sNamesPath, tNamesPath);
    }
}

