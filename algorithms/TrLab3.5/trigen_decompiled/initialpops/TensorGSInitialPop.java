/*
 * Decompiled with CFR 0.152.
 */
package initialpops;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algcore.InitialPop;
import algcore.TriGen;
import initialpops.InitialPopStrategy;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import strinitialpop.HierarchycalStrategy;
import strinitialpop.Tensor2DStrategy;

public class TensorGSInitialPop
implements InitialPop {
    private static final Logger LOG = LoggerFactory.getLogger(TensorGSInitialPop.class);
    private InitialPopStrategy tensor2D = new Tensor2DStrategy();
    private InitialPopStrategy hierarchy = new HierarchycalStrategy();

    @Override
    public List<AlgorithmIndividual> produceInitialPop() {
        int random_tensors = (int)(AlgorithmConfiguration.getInstance().getAle() * (float)AlgorithmConfiguration.getInstance().getI());
        int guided_tensors = AlgorithmConfiguration.getInstance().getI() - random_tensors;
        List<AlgorithmIndividual> initialPop = this.tensor2D.generateIndividuals(random_tensors, TriGen.getInstance().getIndividualClassName());
        List<AlgorithmIndividual> hierarchyPop = this.hierarchy.generateIndividuals(guided_tensors, TriGen.getInstance().getIndividualClassName());
        initialPop.addAll(hierarchyPop);
        int rest = AlgorithmConfiguration.getInstance().getI() - initialPop.size();
        if (rest > 0) {
            initialPop.addAll(this.tensor2D.generateIndividuals(rest, TriGen.getInstance().getIndividualClassName()));
        }
        return initialPop;
    }
}

