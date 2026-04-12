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
import strinitialpop.EarthQuakeStrategy;
import strinitialpop.RandomStrategy;

public class EarthQuakeInitialPop
implements InitialPop {
    private InitialPopStrategy jerarquicaTerremoto = new EarthQuakeStrategy();
    private InitialPopStrategy totalmente_aleatoria = new RandomStrategy();

    @Override
    public List<AlgorithmIndividual> produceInitialPop() {
        float pAleatorio = AlgorithmConfiguration.getInstance().getAle();
        List<AlgorithmIndividual> l = null;
        int totales = AlgorithmConfiguration.getInstance().getI();
        boolean disponible = AlgorithmConfiguration.getInstance().getDataHierarchy().isAvailable();
        if (disponible) {
            int aleatorio = (int)(pAleatorio * (float)totales);
            int jerarquicos = totales - aleatorio;
            l = this.jerarquicaTerremoto.generateIndividuals(jerarquicos, TriGen.getInstance().getIndividualClassName());
            List<AlgorithmIndividual> aux = this.totalmente_aleatoria.generateIndividuals(aleatorio, TriGen.getInstance().getIndividualClassName());
            l.addAll(aux);
        } else {
            l = this.totalmente_aleatoria.generateIndividuals(totales, TriGen.getInstance().getIndividualClassName());
        }
        return l;
    }
}

