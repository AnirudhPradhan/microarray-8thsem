/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

import general.Tricluster;
import java.util.List;
import org.knowm.xchart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriGraphs {
    private static final Logger LOG = LoggerFactory.getLogger(TriGraphs.class);
    private Tricluster tricluster;
    private List<XYChart> GCTcharts;
    private List<XYChart> GTCcharts;
    private List<XYChart> TGCcharts;

    public TriGraphs(Tricluster tri) {
        this.tricluster = tri;
    }

    public List<XYChart> getGCTcharts() {
        return this.GCTcharts;
    }

    public void setGCTcharts(List<XYChart> gCTcharts) {
        this.GCTcharts = gCTcharts;
    }

    public List<XYChart> getGTCcharts() {
        return this.GTCcharts;
    }

    public void setGTCcharts(List<XYChart> gTCcharts) {
        this.GTCcharts = gTCcharts;
    }

    public List<XYChart> getTGCcharts() {
        return this.TGCcharts;
    }

    public void setTGCcharts(List<XYChart> tGCcharts) {
        this.TGCcharts = tGCcharts;
    }

    public Tricluster getTricluster() {
        return this.tricluster;
    }
}

