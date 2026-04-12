/*
 * Decompiled with CFR 0.152.
 */
package input.datasets;

import input.datasets.Real;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Biological
extends Real {
    private static final long serialVersionUID = 8700182723979926824L;
    private static final Logger LOG = LoggerFactory.getLogger(Biological.class);
    private String organism;
    private String genesPath;

    public Biological(String datasetID, String datasetName, String datasetType, String geneSize, String sampleSize, String timeSize, String[] paths, String sep, String defMinG, String defMaxG, String defMinC, String defMaxC, String defMinT, String defMaxT, String gNamesPath, String sNamesPath, String tNamesPath, String organism) {
        super(datasetID, datasetName, datasetType, geneSize, sampleSize, timeSize, paths, sep, defMinG, defMaxG, defMinC, defMaxC, defMinT, defMaxT, gNamesPath, sNamesPath, tNamesPath);
        this.organism = organism;
        this.genesPath = gNamesPath;
    }

    public String getOrganism() {
        return this.organism;
    }

    public String getGenesPath() {
        return this.genesPath;
    }

    @Override
    public String toString() {
        String r = super.toString();
        r = r + "\nOrganism = " + this.organism + "\nGenes path = " + this.genesPath;
        return r;
    }
}

