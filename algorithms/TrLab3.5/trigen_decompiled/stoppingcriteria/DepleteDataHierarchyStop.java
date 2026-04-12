/*
 * Decompiled with CFR 0.152.
 */
package stoppingcriteria;

import algcore.AlgorithmConfiguration;
import algcore.StoppingCriterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DepleteDataHierarchyStop
implements StoppingCriterion {
    private static final Logger LOG = LoggerFactory.getLogger(DepleteDataHierarchyStop.class);

    @Override
    public boolean checkCriterion() {
        return AlgorithmConfiguration.getInstance().getDataHierarchy().isAvailable();
    }
}

