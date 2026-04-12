/*
 * Decompiled with CFR 0.152.
 */
package selections;

import algcore.AlgorithmIndividual;
import algcore.Selection;
import algcore.TriGen;
import algutils.AlgorithmRandomUtilities;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TournamentSelection
implements Selection {
    @Override
    public List<AlgorithmIndividual> select(int numberOfSelections, List<AlgorithmIndividual> population) {
        TriGen trigen = TriGen.getInstance();
        AlgorithmRandomUtilities randomSupport = AlgorithmRandomUtilities.getInstance();
        LinkedList<AlgorithmIndividual> res = new LinkedList<AlgorithmIndividual>();
        int popSize = population.size();
        int numberOfGroups = 3;
        if (popSize <= 2) {
            numberOfGroups = 2;
        }
        List[] gruposPrimerIntento = new List[numberOfGroups];
        for (int i = 0; i < gruposPrimerIntento.length; ++i) {
            gruposPrimerIntento[i] = new LinkedList();
        }
        for (AlgorithmIndividual tri : population) {
            int grupo = randomSupport.getFromInterval(0, gruposPrimerIntento.length - 1);
            gruposPrimerIntento[grupo].add(tri);
        }
        LinkedList<List> grupos = new LinkedList<List>();
        for (int i = 0; i < gruposPrimerIntento.length; ++i) {
            if (gruposPrimerIntento[i].size() == 0) continue;
            grupos.add(gruposPrimerIntento[i]);
        }
        Iterator itg1 = grupos.iterator();
        while (itg1.hasNext()) {
            Collections.sort((List)itg1.next());
        }
        for (List grupo : grupos) {
            AlgorithmIndividual primero = (AlgorithmIndividual)grupo.get(0);
            res.add(primero);
            primero.addEntry("selected [" + (trigen.getOngoingGenerationIndex() + 1) + "]");
            grupo.remove(primero);
            --numberOfSelections;
        }
        for (int i = 0; i < numberOfSelections; ++i) {
            int NumeroGrupo = randomSupport.getFromInterval(0, grupos.size() - 1);
            List grupo = (List)grupos.get(NumeroGrupo);
            while (grupo.size() == 0) {
                NumeroGrupo = randomSupport.getFromInterval(0, grupos.size() - 1);
                grupo = (List)grupos.get(NumeroGrupo);
            }
            AlgorithmIndividual tri = (AlgorithmIndividual)grupo.get(0);
            res.add(tri);
            tri.addEntry("selected [" + (trigen.getOngoingGenerationIndex() + 1) + "]");
            grupo.remove(tri);
        }
        return res;
    }
}

