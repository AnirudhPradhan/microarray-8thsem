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
import strinitialpop.TensorStrategy;

public class TensorInitialPop
implements InitialPop {
    private InitialPopStrategy aleatoria = new RandomStrategy();
    private InitialPopStrategy jerarquica = new HierarchycalStrategy();
    private InitialPopStrategy tensores = new TensorStrategy();

    @Override
    public List<AlgorithmIndividual> produceInitialPop() {
        List<AlgorithmIndividual> l = null;
        if (TriGen.getInstance().getOngoingSolutionIndex() == 0) {
            int aleatorias = AlgorithmConfiguration.getInstance().getI() / 2;
            int cubos = AlgorithmConfiguration.getInstance().getI() - aleatorias;
            l = this.aleatoria.generateIndividuals(aleatorias, TriGen.getInstance().getIndividualClassName());
            List<AlgorithmIndividual> aux = this.tensores.generateIndividuals(cubos, TriGen.getInstance().getIndividualClassName());
            l.addAll(aux);
        } else {
            int n_aleatorio = (int)(AlgorithmConfiguration.getInstance().getAle() * (float)AlgorithmConfiguration.getInstance().getI());
            int aleatorias = n_aleatorio / 2;
            int cubos = n_aleatorio - aleatorias;
            int n_jerarquico = AlgorithmConfiguration.getInstance().getI() - n_aleatorio;
            l = this.aleatoria.generateIndividuals(aleatorias, TriGen.getInstance().getIndividualClassName());
            List<AlgorithmIndividual> aux = this.tensores.generateIndividuals(cubos, TriGen.getInstance().getIndividualClassName());
            l.addAll(aux);
            List<AlgorithmIndividual> aux1 = this.jerarquica.generateIndividuals(n_jerarquico, TriGen.getInstance().getIndividualClassName());
            l.addAll(aux1);
        }
        return l;
    }
}

