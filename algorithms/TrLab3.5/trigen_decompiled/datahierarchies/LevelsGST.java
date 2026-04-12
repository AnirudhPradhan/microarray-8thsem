/*
 * Decompiled with CFR 0.152.
 */
package datahierarchies;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algcore.DataHierarchy;
import algutils.AlgorithmRandomUtilities;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LevelsGST
implements DataHierarchy {
    private Map<Integer, Integer> jerarquia_tiempos = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> jerarquia_genes = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> jerarquia_condiciones = new HashMap<Integer, Integer>();

    @Override
    public ArrayList<ArrayList<Collection<Integer>>> build_gst_coorinates(int n) {
        ArrayList<ArrayList<Collection<Integer>>> res = new ArrayList<ArrayList<Collection<Integer>>>(n);
        for (int i = 0; i < n; ++i) {
            int gs = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinG(), AlgorithmConfiguration.getInstance().getMaxG());
            int cs = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinC(), AlgorithmConfiguration.getInstance().getMaxC());
            int ts = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinT(), AlgorithmConfiguration.getInstance().getMaxT());
            List<Integer> gl = this.getHierarchicalListOfGenes(gs);
            List<Integer> cl = this.getHierarchicalListOfSamples(cs);
            List<Integer> tl = this.getHierarchicalListOfTimes(ts);
            ArrayList<List<Integer>> aux1 = new ArrayList<List<Integer>>(3);
            aux1.add(gl);
            aux1.add(cl);
            aux1.add(tl);
            res.add(aux1);
        }
        return res;
    }

    @Override
    public ArrayList<ArrayList<Collection<Integer>>> build_gs_coorinates(int n) {
        ArrayList<ArrayList<Collection<Integer>>> res = new ArrayList<ArrayList<Collection<Integer>>>(n);
        for (int i = 0; i < n; ++i) {
            int gs = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinG(), AlgorithmConfiguration.getInstance().getMaxG());
            int cs = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinC(), AlgorithmConfiguration.getInstance().getMaxC());
            ArrayList<List<Integer>> aux1 = new ArrayList<List<Integer>>(2);
            List<Integer> gl = this.getHierarchicalListOfGenes(gs);
            List<Integer> cl = this.getHierarchicalListOfSamples(cs);
            aux1.add(gl);
            aux1.add(cl);
            res.add(aux1);
        }
        return res;
    }

    @Override
    public ArrayList<Collection<Integer>> build_i_coorinates(int n, char type) {
        ArrayList<Collection<Integer>> res = new ArrayList<Collection<Integer>>(n);
        for (int i = 0; i < n; ++i) {
            List<Integer> l = null;
            if (type == 'g') {
                int gs = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinG(), AlgorithmConfiguration.getInstance().getMaxG());
                l = this.getHierarchicalListOfGenes(gs);
            } else if (type == 's') {
                int cs = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinC(), AlgorithmConfiguration.getInstance().getMaxC());
                l = this.getHierarchicalListOfSamples(cs);
            } else if (type == 't') {
                int ts = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinT(), AlgorithmConfiguration.getInstance().getMaxT());
                l = this.getHierarchicalListOfTimes(ts);
            }
            res.add(l);
        }
        return res;
    }

    @Override
    public void initialize(int geneSize, int sampleSize, int timeSize) {
        int i;
        for (i = 0; i < timeSize; ++i) {
            this.jerarquia_tiempos.put(new Integer(i), new Integer(0));
        }
        for (i = 0; i < geneSize; ++i) {
            this.jerarquia_genes.put(new Integer(i), new Integer(0));
        }
        for (i = 0; i < sampleSize; ++i) {
            this.jerarquia_condiciones.put(new Integer(i), new Integer(0));
        }
    }

    @Override
    public boolean isAvailable() {
        boolean enc = false;
        Set<Map.Entry<Integer, Integer>> c2 = this.jerarquia_condiciones.entrySet();
        for (Map.Entry<Integer, Integer> ec : c2) {
            if (ec.getValue() != 0) continue;
            enc = true;
        }
        if (!enc) {
            Set<Map.Entry<Integer, Integer>> t = this.jerarquia_tiempos.entrySet();
            for (Map.Entry<Integer, Integer> et : t) {
                if (et.getValue() != 0) continue;
                enc = true;
            }
        }
        if (!enc) {
            Set<Map.Entry<Integer, Integer>> g = this.jerarquia_genes.entrySet();
            for (Map.Entry<Integer, Integer> eg : g) {
                if (eg.getValue() != 0) continue;
                enc = true;
            }
        }
        return enc;
    }

    @Override
    public void update(AlgorithmIndividual solution) {
        for (Integer t : solution.getTimes()) {
            this.jerarquia_tiempos.put(t, new Integer(this.jerarquia_tiempos.get(t) + 1));
        }
        for (Integer g : solution.getGenes()) {
            this.jerarquia_genes.put(g, new Integer(this.jerarquia_genes.get(g) + 1));
        }
        for (Integer c2 : solution.getSamples()) {
            this.jerarquia_condiciones.put(c2, new Integer(this.jerarquia_condiciones.get(c2) + 1));
        }
    }

    @Override
    public double getPercentage() {
        AlgorithmConfiguration param = AlgorithmConfiguration.getInstance();
        int ng = param.getData().getGenSize();
        int nc = param.getData().getSampleSize();
        int nt = param.getData().getTimeSize();
        Set<Map.Entry<Integer, Integer>> g = this.jerarquia_genes.entrySet();
        double gcount = 0.0;
        for (Map.Entry<Integer, Integer> eg : g) {
            if (eg.getValue() != 0) continue;
            gcount += 1.0;
        }
        Set<Map.Entry<Integer, Integer>> c2 = this.jerarquia_condiciones.entrySet();
        double ccount = 0.0;
        for (Map.Entry<Integer, Integer> ec : c2) {
            if (ec.getValue() != 0) continue;
            ccount += 1.0;
        }
        Set<Map.Entry<Integer, Integer>> t = this.jerarquia_tiempos.entrySet();
        double tcount = 0.0;
        for (Map.Entry<Integer, Integer> et : t) {
            if (et.getValue() != 0) continue;
            tcount += 1.0;
        }
        double v = (gcount + ccount + tcount) / (double)(ng + nc + nt) * 100.0;
        return v;
    }

    @Override
    public Map<Integer, Integer> getTimeHierarchy() {
        return this.jerarquia_tiempos;
    }

    @Override
    public Map<Integer, Integer> getGenHierarchy() {
        return this.jerarquia_genes;
    }

    @Override
    public Map<Integer, Integer> getSampleHierarchy() {
        return this.jerarquia_condiciones;
    }

    private List<Integer> getHierarchicalListOfTimes(int timeSize) {
        return this.buildHierarchicalList(timeSize, this.jerarquia_tiempos);
    }

    private List<Integer> getHierarchicalListOfGenes(int geneSize) {
        return this.buildHierarchicalList(geneSize, this.jerarquia_genes);
    }

    private List<Integer> getHierarchicalListOfSamples(int sampleSize) {
        return this.buildHierarchicalList(sampleSize, this.jerarquia_condiciones);
    }

    private List<Integer> buildHierarchicalList(int number, Map<Integer, Integer> hierarchy) {
        LinkedList<Integer> l = new LinkedList<Integer>();
        int faltan = number;
        int nivel = 0;
        while (faltan > 0) {
            faltan = this.fillListWithNcoordinatesFromLevel(l, faltan, nivel, hierarchy);
            ++nivel;
        }
        return l;
    }

    private int fillListWithNcoordinatesFromLevel(List<Integer> l, int n, int level, Map<Integer, Integer> hierarchy) {
        AlgorithmRandomUtilities ALEATORIOS = AlgorithmRandomUtilities.getInstance();
        int faltan = 0;
        List<Integer> lista_coordenadas_en_el_nivel = this.fillListWithCoordinatesFromLevel(level, hierarchy);
        int tam = lista_coordenadas_en_el_nivel.size();
        if (tam < n) {
            faltan = n - tam;
            l.addAll(lista_coordenadas_en_el_nivel);
        } else if (tam == n) {
            faltan = 0;
            l.addAll(lista_coordenadas_en_el_nivel);
        } else {
            for (int i = 0; i < n; ++i) {
                int indice = ALEATORIOS.getFromInterval(0, tam - 1);
                Integer coordenada = lista_coordenadas_en_el_nivel.get(indice);
                while (l.contains(coordenada)) {
                    indice = ALEATORIOS.getFromInterval(0, tam - 1);
                    coordenada = lista_coordenadas_en_el_nivel.get(indice);
                }
                l.add(coordenada);
            }
        }
        return faltan;
    }

    private List<Integer> fillListWithCoordinatesFromLevel(int level, Map<Integer, Integer> hierarchy) {
        LinkedList<Integer> l = new LinkedList<Integer>();
        Set<Map.Entry<Integer, Integer>> conjunto = hierarchy.entrySet();
        for (Map.Entry<Integer, Integer> entry : conjunto) {
            int valor = entry.getValue();
            if (valor != level) continue;
            int key = entry.getKey();
            Integer coordenada = new Integer(key);
            l.add(coordenada);
        }
        return l;
    }
}

