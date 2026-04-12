/*
 * Decompiled with CFR 0.152.
 */
package fitnessfunctions;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algcore.FitnessFunction;
import fitnessfunctions.FitnessStrategy;
import strfitness.StrLslts;
import strfitness.StrMatching;
import strfitness.StrSizes;

public class Lslts
implements FitnessFunction {
    private static final double HIGHEST_LSL_MULTI = Math.PI * 2;
    private FitnessStrategy tamanyos = new StrSizes();
    private FitnessStrategy solapamiento = new StrMatching();
    private FitnessStrategy lslts = new StrLslts();

    @Override
    public void evaluate(AlgorithmIndividual individual) {
        AlgorithmConfiguration config = AlgorithmConfiguration.getInstance();
        double[] aux1 = ((StrSizes)this.tamanyos).getAmount(individual);
        double[] aux2 = ((StrMatching)this.solapamiento).getAmount(individual);
        double eval_base = this.lslts.calculate(individual);
        double maximoValor = Math.PI * 2;
        double neval = eval_base / maximoValor;
        double wog = config.getWog();
        double woc = config.getWoc();
        double wot = config.getWot();
        double wg = config.getWg();
        double wc = config.getWc();
        double wt = config.getWt();
        double prg = aux1[0];
        double prc = aux1[1];
        double prt = aux1[2];
        double sg = aux2[0];
        double sc = aux2[1];
        double st = aux2[2];
        double wf = config.getWf();
        double cprg = 1.0 - prg;
        double cprc = 1.0 - prc;
        double cprt = 1.0 - prt;
        double fitness = (wf * neval + wog * sg + woc * sc + wot * st + cprg * wg + cprc * wc + cprt * wt) / (wf + wog + woc + wot + wg + wc + wt);
        individual.setFitnessValue(neval);
        individual.setOverlappingValue(wog * sg + woc * sc + wot * st);
        individual.setSizeValue(cprg * wg + cprc * wc + cprt * wt);
        individual.setFitnessFunctionValue(fitness);
    }
}

