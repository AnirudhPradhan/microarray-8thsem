/*
 * Decompiled with CFR 0.152.
 */
package datahierarchies;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmDataset;
import algcore.AlgorithmIndividual;
import algcore.DataHierarchy;
import algcore.TriGen;
import algutils.AlgorithmRandomUtilities;
import algutils.Point;
import general.RandomSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelsGS
implements DataHierarchy {
    private static final Logger LOG = LoggerFactory.getLogger(LevelsGS.class);
    private SortedSet<Point> jerarquia = new TreeSet<Point>();
    private List<Integer> genes = new LinkedList<Integer>();
    private List<Integer> condiciones = new LinkedList<Integer>();
    private boolean disponible;
    private AlgorithmIndividual resto;

    @Override
    public void initialize(int geneSize, int sampleSize, int timeSize) {
        this.initializeDataHierarchy(geneSize, sampleSize);
    }

    @Override
    public void update(AlgorithmIndividual solution) {
        this.updateSet(solution);
        this.updateLists();
        this.updateState();
    }

    @Override
    public boolean isAvailable() {
        return this.disponible;
    }

    @Override
    public ArrayList<ArrayList<Collection<Integer>>> build_gst_coorinates(int n) {
        return null;
    }

    @Override
    public ArrayList<ArrayList<Collection<Integer>>> build_gs_coorinates(int n) {
        ArrayList<ArrayList<Collection<Integer>>> res = new ArrayList<ArrayList<Collection<Integer>>>(n);
        for (int i = 0; i < n; ++i) {
            int gs = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinG(), AlgorithmConfiguration.getInstance().getMaxG());
            int cs = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinC(), AlgorithmConfiguration.getInstance().getMaxC());
            ArrayList<List<Integer>> aux1 = new ArrayList<List<Integer>>(2);
            List<Integer>[] aux2 = this.buildGenesAndSamples(gs, cs);
            aux1.add(aux2[0]);
            aux1.add(aux2[1]);
            res.add(aux1);
        }
        return res;
    }

    @Override
    public ArrayList<Collection<Integer>> build_i_coorinates(int n, char type) {
        return null;
    }

    @Override
    public double getPercentage() {
        double tam = this.jerarquia.size();
        double total = AlgorithmConfiguration.getInstance().getData().getGenSize() * AlgorithmConfiguration.getInstance().getData().getSampleSize();
        double numero = 100.0 * tam / total;
        return numero;
    }

    @Override
    public Map<Integer, Integer> getTimeHierarchy() {
        HashMap<Integer, Integer> res = new HashMap<Integer, Integer>();
        res.put(new Integer(0), new Integer(0));
        return res;
    }

    @Override
    public Map<Integer, Integer> getGenHierarchy() {
        HashMap<Integer, Integer> res = new HashMap<Integer, Integer>();
        res.put(new Integer(0), new Integer(0));
        return res;
    }

    @Override
    public Map<Integer, Integer> getSampleHierarchy() {
        HashMap<Integer, Integer> res = new HashMap<Integer, Integer>();
        res.put(new Integer(0), new Integer(0));
        return res;
    }

    public String toString() {
        String res = "";
        res = res + "genes or rows ( " + this.genes.size() + " ) : " + this.genes + "\n";
        res = res + "conditions or columns ( " + this.condiciones.size() + " ) : " + this.condiciones + "\n";
        Iterator it = this.jerarquia.iterator();
        int i = 1;
        while (it.hasNext()) {
            Point p = (Point)it.next();
            if (i == 20) {
                res = res + "[" + p.getX() + "," + p.getY() + "]\n";
                i = 1;
                continue;
            }
            res = res + "[" + p.getX() + "," + p.getY() + "] ";
            ++i;
        }
        return res;
    }

    private void initializeDataHierarchy(int geneSize, int sampleSize) {
        this.disponible = true;
        for (int i = 0; i < geneSize; ++i) {
            Integer auxi = new Integer(i);
            this.genes.add(auxi);
            for (int j = 0; j < sampleSize; ++j) {
                Integer auxj = new Integer(j);
                Point auxp = new Point(i, j);
                if (!this.condiciones.contains(auxj)) {
                    this.condiciones.add(auxj);
                }
                this.jerarquia.add(auxp);
            }
        }
    }

    private void updateSet(AlgorithmIndividual solution) {
        for (int i : solution.getGenes()) {
            for (int j : solution.getSamples()) {
                Point aux = new Point(i, j);
                this.jerarquia.remove(aux);
            }
        }
    }

    private void updateLists() {
        Iterator it = this.jerarquia.iterator();
        this.genes.clear();
        this.condiciones.clear();
        while (it.hasNext()) {
            Point par = (Point)it.next();
            Integer i = new Integer(par.getX());
            Integer j = new Integer(par.getY());
            if (!this.genes.contains(i)) {
                this.genes.add(i);
            }
            if (this.condiciones.contains(j)) continue;
            this.condiciones.add(j);
        }
    }

    private void updateState() {
        AlgorithmConfiguration PARAM = AlgorithmConfiguration.getInstance();
        int minG = PARAM.getMinG();
        int minC = PARAM.getMinC();
        int tamG = this.genes.size();
        int tamC = this.condiciones.size();
        if (tamG < minG || tamC < minC) {
            this.disponible = false;
        }
        if (!this.disponible) {
            System.out.println("**********************data hierarchy depleted!");
            this.buildRest();
        }
    }

    private void buildRest() {
        AlgorithmConfiguration PARAM = AlgorithmConfiguration.getInstance();
        AlgorithmDataset DATA = PARAM.getData();
        TriGen TRI = TriGen.getInstance();
        LinkedList<Integer> lt = new LinkedList<Integer>();
        for (int i = 0; i < DATA.getTimeSize(); ++i) {
            lt.add(new Integer(i));
        }
        Collections.sort(lt);
        Collections.sort(this.genes);
        Collections.sort(this.condiciones);
        try {
            this.resto = (AlgorithmIndividual)Class.forName(TRI.getIndividualClassName()).newInstance();
            this.resto.initialize(lt, this.genes, this.condiciones);
            this.resto.addEntry("data hierarchy remainder G = " + TRI.getOngoingSolutionIndex() + "\n");
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<Integer>[] buildGenesAndSamples(int geneSize, int sampleSize) {
        AlgorithmRandomUtilities ALEATORIOS = AlgorithmRandomUtilities.getInstance();
        List<Integer>[] res = null;
        int demandadas = geneSize * sampleSize;
        HashSet<Point> elegidos = new HashSet<Point>();
        int faltan = demandadas;
        while (faltan > 0) {
            boolean insertado;
            int c2;
            int ig = ALEATORIOS.getFromInterval(0, this.genes.size() - 1);
            int ic = ALEATORIOS.getFromInterval(0, this.condiciones.size() - 1);
            int g = this.genes.get(ig);
            Point par = new Point(g, c2 = this.condiciones.get(ic).intValue());
            if (!this.jerarquia.contains(par) || !(insertado = elegidos.add(par))) continue;
            --faltan;
        }
        res = this.fromCellToList(elegidos);
        List<Integer> gL = res[0];
        int rG = geneSize - gL.size();
        LOG.debug("rG = " + rG);
        if (rG > 0) {
            RandomSupport rnd = new RandomSupport();
            rnd.emptyMainBag();
            rnd.putMarblesIntoMainBag(this.genes);
            for (int i = 0; i < rG; ++i) {
                boolean enc = false;
                while (!enc) {
                    Integer iG = new Integer(rnd.extractAmarbleFromMainBag());
                    if (gL.contains(iG)) continue;
                    gL.add(iG);
                    enc = true;
                }
            }
            Collections.sort(gL);
        }
        List<Integer> cL = res[1];
        int rC = sampleSize - cL.size();
        LOG.debug("rC = " + rC);
        if (rC > 0) {
            RandomSupport rnd = new RandomSupport();
            rnd.emptyMainBag();
            rnd.putMarblesIntoMainBag(this.condiciones);
            for (int i = 0; i < rC; ++i) {
                boolean enc = false;
                while (!enc) {
                    Integer iC = new Integer(rnd.extractAmarbleFromMainBag());
                    if (cL.contains(iC)) continue;
                    cL.add(iC);
                    enc = true;
                }
            }
            Collections.sort(cL);
        }
        return res;
    }

    private List<Integer>[] fromCellToList(Set<Point> cells) {
        List[] res = new List[2];
        LinkedList<Integer> r1 = new LinkedList<Integer>();
        LinkedList<Integer> r2 = new LinkedList<Integer>();
        if (cells.size() != 0) {
            for (Point par : cells) {
                Integer i = new Integer(par.getX());
                Integer j = new Integer(par.getY());
                if (!r1.contains(i)) {
                    r1.add(i);
                }
                if (r2.contains(j)) continue;
                r2.add(j);
            }
        }
        res[0] = r1;
        res[1] = r2;
        return res;
    }
}

