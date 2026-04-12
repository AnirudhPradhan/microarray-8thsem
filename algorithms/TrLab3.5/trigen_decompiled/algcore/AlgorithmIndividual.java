/*
 * Decompiled with CFR 0.152.
 */
package algcore;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import utils.TextUtilities;

public abstract class AlgorithmIndividual
implements Comparable<AlgorithmIndividual> {
    protected Collection<Integer> genes;
    protected Collection<Integer> samples;
    protected Collection<Integer> times;
    protected double fitness;
    protected double overlapping;
    protected double sizes;
    protected double fitnessFunction;
    protected boolean evaluated;
    protected boolean altered;
    protected List<String> register;

    public abstract void initialize(Collection<Integer> var1, Collection<Integer> var2, Collection<Integer> var3);

    public abstract int getGene(int var1);

    public abstract int getSample(int var1);

    public abstract int getTime(int var1);

    public abstract void putGene(int var1);

    public abstract void putSample(int var1);

    public abstract void putTime(int var1);

    public abstract void deleteGene(int var1);

    public abstract void deleteSample(int var1);

    public abstract void deleteTime(int var1);

    @Override
    public int compareTo(AlgorithmIndividual o) {
        double fe1 = this.fitnessFunction;
        double fe2 = o.getFitnessFunctionValue();
        return Double.compare(fe1, fe2);
    }

    public boolean canSkipEvaluation() {
        boolean res = false;
        if (this.evaluated && !this.altered) {
            res = true;
        }
        return res;
    }

    protected void initialState() {
        this.evaluated = false;
        this.altered = false;
        this.fitness = 0.0;
        this.overlapping = 0.0;
        this.sizes = 0.0;
        this.fitnessFunction = 0.0;
        this.register = new LinkedList<String>();
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }

    public void setAltered(boolean altered) {
        this.altered = altered;
    }

    public String completeToString() {
        String r = "";
        DecimalFormat f = TextUtilities.getDecimalFormat('.', "0.000");
        r = "[" + this.genes.size() + "," + this.samples.size() + "," + this.times.size() + "] " + " evaluated?=" + this.evaluated + " altered?=" + this.altered + " , FF = " + f.format(this.fitnessFunction) + " [" + f.format(this.fitness) + "," + f.format(this.sizes) + "," + f.format(this.overlapping) + "]" + "\n" + "Genes: " + this.genes + "\n" + "Samples: " + this.samples + "\n" + "Times: " + this.times + "\n" + this.getRegisterReport("\n");
        return r;
    }

    public String toString() {
        String r = "";
        DecimalFormat f = TextUtilities.getDecimalFormat('.', "0.000");
        r = "[" + this.genes.size() + "," + this.samples.size() + "," + this.times.size() + "] " + "{" + this.register.get(0) + "-" + this.register.get(this.register.size() - 1) + "}" + " evaluated?=" + this.evaluated + " altered?=" + this.altered + " , FF = " + f.format(this.fitnessFunction) + " [" + f.format(this.fitness) + "," + f.format(this.sizes) + "," + f.format(this.overlapping) + "]";
        return r;
    }

    public Collection<Integer> getGenes() {
        return this.genes;
    }

    public Collection<Integer> getSamples() {
        return this.samples;
    }

    public Collection<Integer> getTimes() {
        return this.times;
    }

    public int getGeneSize() {
        return this.genes.size();
    }

    public int getSampleSize() {
        return this.samples.size();
    }

    public int getTimeSize() {
        return this.times.size();
    }

    public double getFitnessFunctionValue() {
        return this.fitnessFunction;
    }

    public double getFitnessValue() {
        return this.fitness;
    }

    public double getOverlappingValue() {
        return this.overlapping;
    }

    public double getSizeValue() {
        return this.sizes;
    }

    public List<String> getRegister() {
        return this.register;
    }

    public String getRegisterReport(String sep) {
        String r = "";
        for (String s : this.register) {
            r = r + s + sep;
        }
        return r;
    }

    public void setFitnessFunctionValue(double fitnessFunctionValue) {
        this.fitnessFunction = fitnessFunctionValue;
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitness = fitnessValue;
    }

    public void setOverlappingValue(double overlappingValue) {
        this.overlapping = overlappingValue;
    }

    public void setSizeValue(double sizeValue) {
        this.sizes = sizeValue;
    }

    public void addEntry(String entry) {
        this.register.add(entry);
    }
}

