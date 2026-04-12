/*
 * Decompiled with CFR 0.152.
 */
package strfitness;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmDataset;
import algcore.AlgorithmIndividual;
import algutils.FitnessUtilities;
import algutils.TriclusterUtilities;
import fitnessfunctions.FitnessStrategy;
import java.util.List;
import labutils.Conversions;
import org.apache.commons.math3.stat.StatUtils;

public class StrMsl
implements FitnessStrategy {
    @Override
    public double calculate(AlgorithmIndividual individual) {
        double res = 0.0;
        AlgorithmDataset dataset = AlgorithmConfiguration.getInstance().getData();
        List<double[][][]> transformaciones = TriclusterUtilities.getInstance().original(individual, dataset);
        double[][][] angulosGC = FitnessUtilities.getInstance().buildAnglesCubeT1(transformaciones.get(0));
        List<Double> diferenciasGC = FitnessUtilities.getInstance().buildTableOfDifferencies(angulosGC);
        double[][][] angulosGT = FitnessUtilities.getInstance().buildAnglesCubeT1(transformaciones.get(1));
        List<Double> diferenciasGT = FitnessUtilities.getInstance().buildTableOfDifferencies(angulosGT);
        double[][][] angulosTG = FitnessUtilities.getInstance().buildAnglesCubeT1(transformaciones.get(2));
        List<Double> diferenciasTG = FitnessUtilities.getInstance().buildTableOfDifferencies(angulosTG);
        double[] views = new double[]{StatUtils.mean(Conversions.fromListOfDoubleToArray(diferenciasGC)), StatUtils.mean(Conversions.fromListOfDoubleToArray(diferenciasGT)), StatUtils.mean(Conversions.fromListOfDoubleToArray(diferenciasTG))};
        res = StatUtils.mean(views);
        return res;
    }
}

