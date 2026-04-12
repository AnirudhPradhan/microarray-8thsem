/*
 * Decompiled with CFR 0.152.
 */
package individuals;

import algcore.AlgorithmIndividual;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoorTricluster
extends AlgorithmIndividual {
    private static final Logger LOG = LoggerFactory.getLogger(CoorTricluster.class);
    private List<Integer> lgenes;
    private List<Integer> lsamples;
    private List<Integer> ltimes;

    @Override
    public void initialize(Collection<Integer> genes, Collection<Integer> samples, Collection<Integer> times) {
        this.genes = this.fromCollectionToList(genes);
        this.samples = this.fromCollectionToList(samples);
        this.times = this.fromCollectionToList(times);
        this.lgenes = (List)this.genes;
        this.lsamples = (List)this.samples;
        this.ltimes = (List)this.times;
        this.initialState();
    }

    private List<Integer> fromCollectionToList(Collection<Integer> component) {
        LinkedList<Integer> r = new LinkedList<Integer>();
        for (Integer coor : component) {
            r.add(new Integer(coor));
        }
        Collections.sort(r);
        return r;
    }

    @Override
    public int getGene(int geneIndex) {
        return this.lgenes.get(geneIndex);
    }

    @Override
    public int getSample(int sampleIndex) {
        return this.lsamples.get(sampleIndex);
    }

    @Override
    public int getTime(int timeIndex) {
        return this.ltimes.get(timeIndex);
    }

    @Override
    public void putGene(int gene) {
        this.lgenes.add(new Integer(gene));
        Collections.sort(this.lgenes);
    }

    @Override
    public void putSample(int sample) {
        this.lsamples.add(new Integer(sample));
        Collections.sort(this.lsamples);
    }

    @Override
    public void putTime(int time) {
        this.ltimes.add(new Integer(time));
        Collections.sort(this.ltimes);
    }

    @Override
    public void deleteGene(int gene) {
        this.lgenes.remove(new Integer(gene));
    }

    @Override
    public void deleteSample(int sample) {
        this.lsamples.remove(new Integer(sample));
    }

    @Override
    public void deleteTime(int time) {
        this.ltimes.remove(new Integer(time));
    }

    @Override
    public String toString() {
        String r = "";
        r = "[" + this.genes.size() + "," + this.samples.size() + "," + this.times.size() + "]";
        return r;
    }
}

