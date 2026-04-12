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
import strinitialpop.HierarchycalStrategy;
import strinitialpop.RandomStrategy;

public class BasicInitialPop
implements InitialPop {
    private InitialPopStrategy totalmente_aleatoria = new RandomStrategy();
    private InitialPopStrategy jerarquica = new HierarchycalStrategy();

    @Override
    public List<AlgorithmIndividual> produceInitialPop() {
        List<AlgorithmIndividual> l = null;
        if (TriGen.getInstance().getOngoingSolutionIndex() == 0) {
            l = this.totalmente_aleatoria.generateIndividuals(AlgorithmConfiguration.getInstance().getI(), TriGen.getInstance().getIndividualClassName());
        } else {
            int n_aleatorio = (int)(AlgorithmConfiguration.getInstance().getAle() * (float)AlgorithmConfiguration.getInstance().getI());
            int n_jerarquico = AlgorithmConfiguration.getInstance().getI() - n_aleatorio;
            l = this.totalmente_aleatoria.generateIndividuals(n_aleatorio, TriGen.getInstance().getIndividualClassName());
            List<AlgorithmIndividual> aux = this.jerarquica.generateIndividuals(n_jerarquico, TriGen.getInstance().getIndividualClassName());
            l.addAll(aux);
        }
        return l;
    }
}

