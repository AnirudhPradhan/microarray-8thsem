/*
 * Decompiled with CFR 0.152.
 */
package stoppingcriteria;

import algcore.AlgorithmConfiguration;
import algcore.StoppingCriterion;
import algcore.TriGen;

public class FindSolutionsStop
implements StoppingCriterion {
    @Override
    public boolean checkCriterion() {
        TriGen TRI = TriGen.getInstance();
        AlgorithmConfiguration PARAM = AlgorithmConfiguration.getInstance();
        boolean res = TRI.getOngoingSolutionIndex() < PARAM.getN();
        return res;
    }
}

