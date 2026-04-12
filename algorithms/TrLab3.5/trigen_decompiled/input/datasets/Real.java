/*
 * Decompiled with CFR 0.152.
 */
package input.datasets;

import input.datasets.Common;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.InTextFile;

public class Real
extends Common {
    private static final long serialVersionUID = 5056243918948791779L;
    private static final Logger LOG = LoggerFactory.getLogger(Real.class);
    private String[] geneNames;
    private String[] sampleNames;
    private String[] timeNames;

    public Real(String datasetID, String datasetName, String datasetType, String geneSize, String sampleSize, String timeSize, String[] paths, String sep, String defMinG, String defMaxG, String defMinC, String defMaxC, String defMinT, String defMaxT, String gNamesPath, String sNamesPath, String tNamesPath) {
        super(datasetID, datasetName, datasetType, geneSize, sampleSize, timeSize, paths, sep, defMinG, defMaxG, defMinC, defMaxC, defMinT, defMaxT);
        this.geneNames = this.buindNamesFromFile(gNamesPath, 'g');
        this.sampleNames = this.buindNamesFromFile(sNamesPath, 's');
        this.timeNames = this.buindNamesFromFile(tNamesPath, 't');
    }

    public String[] getGeneNames() {
        return this.geneNames;
    }

    public String[] getSampleNames() {
        return this.sampleNames;
    }

    public String[] getTimeNames() {
        return this.timeNames;
    }

    @Override
    public String toString() {
        String r = super.toString();
        r = r + "\nFirst Gene Name = " + this.geneNames[0] + "\nLast Gene Name = " + this.geneNames[this.getGeneSize() - 1] + "\nFirst Sample Name = " + this.sampleNames[0] + "\nLast Sample Name = " + this.sampleNames[this.getSampleSize() - 1] + "\nFirst Time Name = " + this.timeNames[0] + "\nLast Time Name = " + this.timeNames[this.getTimeSize() - 1];
        return r;
    }

    private String[] buindNamesFromFile(String path, char ty) {
        int size = 0;
        if (ty == 'g') {
            size = this.getGeneSize();
        } else if (ty == 's') {
            size = this.getSampleSize();
        } else if (ty == 't') {
            size = this.getTimeSize();
        }
        String[] r = new String[size];
        try {
            InTextFile f = new InTextFile(path);
            int i = 0;
            for (String it : f) {
                r[i] = it.trim();
                ++i;
            }
            f.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }
}

