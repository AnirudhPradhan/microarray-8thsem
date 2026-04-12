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

public class StrLslts
implements FitnessStrategy {
    @Override
    public double calculate(AlgorithmIndividual individual) {
        AlgorithmDataset dataset = AlgorithmConfiguration.getInstance().getData();
        double[][][] individualData = TriclusterUtilities.getInstance().buildGTCView(individual, dataset);
        double[][] slopesAngles = FitnessUtilities.getInstance().buildTableOfAngles(individualData);
        List<Double> delta = FitnessUtilities.getInstance().buildTableOfDifferencies(slopesAngles);
        return StatUtils.mean(Conversions.fromListOfDoubleToArray(delta));
    }
}

