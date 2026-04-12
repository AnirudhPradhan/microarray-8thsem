/*
 * Decompiled with CFR 0.152.
 */
package labutils;

import general.Tricluster;
import java.util.ArrayList;
import java.util.List;
import labutils.Conversions;

public class GeneOntology {
    public static String[] getGeneStrings(List<Tricluster> solutions, String[] datasetGenes) {
        String[] f = new String[solutions.size()];
        int i = 0;
        List<String[]> l = GeneOntology.getAllGeneNames(solutions, datasetGenes);
        for (i = 0; i < f.length; ++i) {
            String[] gn = l.get(i);
            f[i] = Conversions.fromArrayOfStringToString(gn, "\n");
        }
        return f;
    }

    public static List<String[]> getAllGeneNames(List<Tricluster> triclusters, String[] geneNames) {
        ArrayList<String[]> geneLists = new ArrayList<String[]>(triclusters.size());
        for (Tricluster tri : triclusters) {
            String[] geneList = GeneOntology.getGeneNames(tri, geneNames);
            geneLists.add(geneList);
        }
        return geneLists;
    }

    public static String[] getGeneNames(Tricluster tricluster, String[] geneNames) {
        List<Integer> geneList = tricluster.getGenes();
        String[] genes = new String[geneList.size()];
        int i = 0;
        for (Integer genIndex : geneList) {
            genes[i] = geneNames[genIndex];
            ++i;
        }
        return genes;
    }
}

