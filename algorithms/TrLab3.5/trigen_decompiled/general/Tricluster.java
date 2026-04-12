/*
 * Decompiled with CFR 0.152.
 */
package general;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tricluster {
    private static final Logger LOG = LoggerFactory.getLogger(Tricluster.class);
    private List<Integer> genes;
    private List<Integer> samples;
    private List<Integer> times;

    public Tricluster() {
    }

    public Tricluster(List<String> genes, List<String> samples, List<String> times) {
        this.genes = this.buildCoordinates(genes);
        this.samples = this.buildCoordinates(samples);
        this.times = this.buildCoordinates(times);
    }

    public void loadCoordinates(List<Integer> genes, List<Integer> samples, List<Integer> times) {
        this.genes = genes;
        this.samples = samples;
        this.times = times;
    }

    public List<Integer> getGenes() {
        return this.genes;
    }

    public List<Integer> getSamples() {
        return this.samples;
    }

    public List<Integer> getTimes() {
        return this.times;
    }

    public String toString() {
        String r = "";
        r = "[" + this.genes.size() + "," + this.samples.size() + "," + this.times.size() + "] " + "{" + this.genes.get(0) + "--" + this.genes.get(this.genes.size() - 1) + "} " + "{" + this.samples.get(0) + "--" + this.samples.get(this.samples.size() - 1) + "} " + "{" + this.times.get(0) + "--" + this.times.get(this.times.size() - 1) + "}";
        return r;
    }

    public String completeToString() {
        String r = "";
        r = "[" + this.genes.size() + "," + this.samples.size() + "," + this.times.size() + "]\n" + "Genes: " + this.genes + "\n" + "Samples: " + this.samples + "\n" + "Times: " + this.times;
        return r;
    }

    private List<Integer> buildCoordinates(List<String> stringCoordinates) {
        ArrayList<Integer> r = new ArrayList<Integer>(stringCoordinates.size());
        for (String it : stringCoordinates) {
            r.add(new Integer(it));
        }
        return r;
    }
}

