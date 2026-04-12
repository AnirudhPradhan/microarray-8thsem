/*
 * Decompiled with CFR 0.152.
 */
package individuals;

import algcore.AlgorithmIndividual;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SetTricluster
extends AlgorithmIndividual {
    @Override
    public void initialize(Collection<Integer> genes, Collection<Integer> samples, Collection<Integer> times) {
        this.genes = this.fromCollectionToSet(genes);
        this.samples = this.fromCollectionToSet(samples);
        this.times = this.fromCollectionToSet(times);
        this.initialState();
    }

    private SortedSet<Integer> fromCollectionToSet(Collection<Integer> component) {
        TreeSet<Integer> r = new TreeSet<Integer>();
        for (Integer coor : component) {
            r.add(new Integer(coor));
        }
        return r;
    }

    @Override
    public int getGene(int geneIndex) {
        Integer[] aux1 = this.genes.toArray(new Integer[0]);
        Integer aux2 = aux1[geneIndex];
        return aux2;
    }

    @Override
    public int getSample(int sampleIndex) {
        Integer[] aux1 = this.samples.toArray(new Integer[0]);
        Integer aux2 = aux1[sampleIndex];
        return aux2;
    }

    @Override
    public int getTime(int timeIndex) {
        Integer[] aux1 = this.times.toArray(new Integer[0]);
        Integer aux2 = aux1[timeIndex];
        return aux2;
    }

    @Override
    public void putGene(int gen) {
        ((Set)this.genes).add(new Integer(gen));
    }

    @Override
    public void putSample(int condicion) {
        ((Set)this.samples).add(new Integer(condicion));
    }

    @Override
    public void putTime(int tiempo) {
        ((Set)this.times).add(new Integer(tiempo));
    }

    @Override
    public void deleteGene(int gen) {
        ((Set)this.genes).remove(new Integer(gen));
    }

    @Override
    public void deleteSample(int condicion) {
        ((Set)this.samples).remove(new Integer(condicion));
    }

    @Override
    public void deleteTime(int tiempo) {
        ((Set)this.times).remove(new Integer(tiempo));
    }
}

