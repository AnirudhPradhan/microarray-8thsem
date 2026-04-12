/*
 * Decompiled with CFR 0.152.
 */
package strfitness;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algcore.TriGen;
import fitnessfunctions.FitnessStrategy;
import java.util.Collection;
import java.util.List;

public class StrMatching
implements FitnessStrategy {
    @Override
    public double calculate(AlgorithmIndividual individual) {
        TriGen TRI = TriGen.getInstance();
        AlgorithmConfiguration PARAM = AlgorithmConfiguration.getInstance();
        double indice = 0.0;
        Collection<Integer> tiempos = individual.getTimes();
        Collection<Integer> genes = individual.getGenes();
        Collection<Integer> condiciones = individual.getSamples();
        double numero_tiempos = 0.0;
        double numero_genes = 0.0;
        double numero_condiciones = 0.0;
        List<AlgorithmIndividual> resultados = TRI.getSolutions();
        for (AlgorithmIndividual resultado : resultados) {
            for (Integer componente : resultado.getTimes()) {
                if (tiempos.contains(componente)) continue;
                numero_tiempos += 1.0;
            }
            for (Integer componente : resultado.getGenes()) {
                if (genes.contains(componente)) continue;
                numero_genes += 1.0;
            }
            for (Integer componente : resultado.getSamples()) {
                if (condiciones.contains(componente)) continue;
                numero_condiciones += 1.0;
            }
        }
        double nSolsEncontradas = TRI.getSolutions().size();
        indice = 0.0;
        if (nSolsEncontradas != 0.0) {
            double wog = PARAM.getWog();
            double woc = PARAM.getWoc();
            double wot = PARAM.getWot();
            double nGr = numero_genes;
            double nCr = numero_condiciones;
            double nTr = numero_tiempos;
            double nGind = individual.getGeneSize();
            double nCind = individual.getSampleSize();
            double nTind = individual.getTimeSize();
            double denG = nGind * nSolsEncontradas;
            double denC = nCind * nSolsEncontradas;
            double denT = nTind * nSolsEncontradas;
            double den = wog + woc + wot;
            if (den != 0.0) {
                double numerador = wog * (nGr / denG) + woc * (nCr / denC) + wot * (nTr / denT);
                indice = numerador / den;
            }
        }
        return indice;
    }

    public double[] getAmount(AlgorithmIndividual individuo) {
        TriGen TRI = TriGen.getInstance();
        Collection<Integer> tiempos = individuo.getTimes();
        Collection<Integer> genes = individuo.getGenes();
        Collection<Integer> condiciones = individuo.getSamples();
        double numero_tiempos = 0.0;
        double numero_genes = 0.0;
        double numero_condiciones = 0.0;
        List<AlgorithmIndividual> resultados = TRI.getSolutions();
        for (AlgorithmIndividual resultado : resultados) {
            for (Integer componente : resultado.getTimes()) {
                if (tiempos.contains(componente)) continue;
                numero_tiempos += 1.0;
            }
            for (Integer componente : resultado.getGenes()) {
                if (genes.contains(componente)) continue;
                numero_genes += 1.0;
            }
            for (Integer componente : resultado.getSamples()) {
                if (condiciones.contains(componente)) continue;
                numero_condiciones += 1.0;
            }
        }
        double[] r = new double[3];
        double nSolsEncontradas = TRI.getSolutions().size();
        r[0] = 0.0;
        r[1] = 0.0;
        r[2] = 0.0;
        if (nSolsEncontradas != 0.0) {
            double nGr = numero_genes;
            double nCr = numero_condiciones;
            double nTr = numero_tiempos;
            double nGind = individuo.getGeneSize();
            double nCind = individuo.getSampleSize();
            double nTind = individuo.getTimeSize();
            double denG = nGind * nSolsEncontradas;
            double denC = nCind * nSolsEncontradas;
            double denT = nTind * nSolsEncontradas;
            r[0] = nGr / denG;
            r[1] = nCr / denC;
            r[2] = nTr / denT;
        }
        return r;
    }
}

