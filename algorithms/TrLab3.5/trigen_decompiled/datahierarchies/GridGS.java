/*
 * Decompiled with CFR 0.152.
 */
package datahierarchies;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algcore.DataHierarchy;
import algutils.AlgorithmRandomUtilities;
import algutils.Point;
import algutils.TriclusterUtilities;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GridGS
implements DataHierarchy {
    private static final Logger LOG = LoggerFactory.getLogger(GridGS.class);
    private Set<Point> grid = new HashSet<Point>();
    private Map<Integer, Integer> times = new HashMap<Integer, Integer>();
    private int x_Size;
    private int y_Size;
    private int t_Size;
    private int visited_points;

    @Override
    public void initialize(int geneSize, int sampleSize, int timeSize) {
        this.x_Size = sampleSize;
        this.y_Size = geneSize;
        this.t_Size = timeSize;
        this.visited_points = 0;
        for (int x = 0; x < this.x_Size; ++x) {
            for (int y = 0; y < this.y_Size; ++y) {
                this.grid.add(new Point(x, y));
            }
        }
        for (int i = 0; i < this.t_Size; ++i) {
            this.times.put(new Integer(i), new Integer(0));
        }
    }

    @Override
    public void update(AlgorithmIndividual solution) {
        for (Integer x : solution.getSamples()) {
            for (Integer y : solution.getGenes()) {
                if (!this.grid.remove(new Point(x, y))) continue;
                ++this.visited_points;
            }
        }
        if (this.grid.isEmpty()) {
            for (int x = 0; x < this.x_Size; ++x) {
                for (int y = 0; y < this.y_Size; ++y) {
                    this.grid.add(new Point(x, y));
                }
            }
        }
        for (Integer t : solution.getTimes()) {
            this.times.put(t, new Integer(this.times.get(t) + 1));
        }
    }

    @Override
    public boolean isAvailable() {
        return this.visited_points < this.x_Size * this.y_Size;
    }

    @Override
    public ArrayList<ArrayList<Collection<Integer>>> build_gst_coorinates(int n) {
        LOG.debug("\n>>>>>GridGS.build_gst_coorinates\n");
        ArrayList<ArrayList<Collection<Integer>>> res = new ArrayList<ArrayList<Collection<Integer>>>(n);
        int nmarbles = n;
        if (this.grid.size() < n) {
            nmarbles = this.grid.size();
        }
        AlgorithmRandomUtilities.getInstance().newPointsBag(this.grid);
        Point[] centers = AlgorithmRandomUtilities.getInstance().extractNpointMarbles(nmarbles);
        for (int i = 0; i < centers.length; ++i) {
            ArrayList<List<Object>> components = new ArrayList<List<Object>>(3);
            LinkedList<Integer> x = new LinkedList<Integer>();
            LinkedList<Integer> y = new LinkedList<Integer>();
            LOG.debug("\nCenter: " + centers[i] + "\n");
            int x_center = centers[i].getX();
            int y_center = centers[i].getY();
            int tamX = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinC(), AlgorithmConfiguration.getInstance().getMaxC());
            int tamY = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinG(), AlgorithmConfiguration.getInstance().getMaxG());
            int[] radios = TriclusterUtilities.getInstance().getTensor2DLimits(y_center, x_center, tamY, tamX, AlgorithmConfiguration.getInstance().getData().getGenSize(), AlgorithmConfiguration.getInstance().getData().getSampleSize());
            for (int iy = radios[0]; iy <= radios[1]; ++iy) {
                y.add(new Integer(iy));
            }
            for (int ix = radios[2]; ix <= radios[3]; ++ix) {
                x.add(new Integer(ix));
            }
            components.add(y);
            components.add(x);
            components.add(this.buildHierarchicalList(AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinT(), AlgorithmConfiguration.getInstance().getMaxT())));
            LOG.debug("x: " + x + " y:" + y + " t:" + components.get(2) + "\n");
            res.add(components);
        }
        LOG.debug("\n<<<<<GridGS.build_gst_coorinates\n\n");
        return res;
    }

    @Override
    public ArrayList<ArrayList<Collection<Integer>>> build_gs_coorinates(int n) {
        ArrayList<ArrayList<Collection<Integer>>> res = new ArrayList<ArrayList<Collection<Integer>>>(n);
        int nmarbles = n;
        if (this.grid.size() < n) {
            nmarbles = this.grid.size();
        }
        AlgorithmRandomUtilities.getInstance().newPointsBag(this.grid);
        Point[] centers = AlgorithmRandomUtilities.getInstance().extractNpointMarbles(nmarbles);
        for (int i = 0; i < centers.length; ++i) {
            ArrayList components = new ArrayList(2);
            LinkedList<Integer> x = new LinkedList<Integer>();
            LinkedList<Integer> y = new LinkedList<Integer>();
            LOG.debug("Center: " + centers[i]);
            int x_center = centers[i].getX();
            int y_center = centers[i].getY();
            int tamX = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinC(), AlgorithmConfiguration.getInstance().getMaxC());
            int tamY = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinG(), AlgorithmConfiguration.getInstance().getMaxG());
            int[] radios = TriclusterUtilities.getInstance().getTensor2DLimits(y_center, x_center, tamY, tamX, AlgorithmConfiguration.getInstance().getData().getGenSize(), AlgorithmConfiguration.getInstance().getData().getSampleSize());
            for (int iy = radios[0]; iy <= radios[1]; ++iy) {
                y.add(new Integer(iy));
            }
            for (int ix = radios[2]; ix <= radios[3]; ++ix) {
                x.add(new Integer(ix));
            }
            components.add(y);
            components.add(x);
            res.add(components);
        }
        return res;
    }

    @Override
    public ArrayList<Collection<Integer>> build_i_coorinates(int n, char type) {
        ArrayList<Collection<Integer>> res = new ArrayList<Collection<Integer>>(n);
        if (type == 'g' || type == 's' || type == 'c') {
            int nmarbles = n;
            if (this.grid.size() < n) {
                nmarbles = this.grid.size();
            }
            AlgorithmRandomUtilities.getInstance().newPointsBag(this.grid);
            Point[] centers = AlgorithmRandomUtilities.getInstance().extractNpointMarbles(nmarbles);
            for (int i = 0; i < centers.length; ++i) {
                int x_center = centers[i].getX();
                int y_center = centers[i].getY();
                int tamX = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinC(), AlgorithmConfiguration.getInstance().getMaxC());
                int tamY = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinG(), AlgorithmConfiguration.getInstance().getMaxG());
                int[] radios = TriclusterUtilities.getInstance().getTensor2DLimits(y_center, x_center, tamY, tamX, AlgorithmConfiguration.getInstance().getData().getGenSize(), AlgorithmConfiguration.getInstance().getData().getSampleSize());
                if (type == 'g') {
                    LinkedList<Integer> y = new LinkedList<Integer>();
                    for (int iy = radios[0]; iy <= radios[1]; ++iy) {
                        y.add(new Integer(iy));
                    }
                    res.add(y);
                    continue;
                }
                if (type != 's' && type != 'c') continue;
                LinkedList<Integer> x = new LinkedList<Integer>();
                for (int ix = radios[2]; ix <= radios[3]; ++ix) {
                    x.add(new Integer(ix));
                }
                res.add(x);
            }
        } else if (type == 't') {
            res.add(this.buildHierarchicalList(AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinT(), AlgorithmConfiguration.getInstance().getMaxT())));
        }
        return res;
    }

    @Override
    public double getPercentage() {
        double s = this.grid.size();
        double total = this.x_Size * this.y_Size;
        return 100.0 * s / total;
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
        String r = "";
        for (int i = 0; i < this.y_Size; ++i) {
            for (int j = 0; j < this.x_Size; ++j) {
                Point p = new Point(j, i);
                r = this.grid.contains(p) ? r + "0" : r + "1";
                r = j == this.x_Size - 1 ? r + "\n" : r + " , ";
            }
        }
        return r;
    }

    private List<Integer> buildHierarchicalList(int size) {
        LinkedList<Integer> res = new LinkedList<Integer>();
        int faltan = size;
        int nivel = 0;
        while (faltan > 0) {
            LinkedList<Integer> lista_coordenadas_en_el_nivel = new LinkedList<Integer>();
            for (Map.Entry<Integer, Integer> entry : this.times.entrySet()) {
                if (entry.getValue() != nivel) continue;
                lista_coordenadas_en_el_nivel.add(new Integer(entry.getKey()));
            }
            int tam = lista_coordenadas_en_el_nivel.size();
            if (tam < faltan) {
                faltan -= tam;
                res.addAll(lista_coordenadas_en_el_nivel);
            } else if (tam == faltan) {
                faltan = 0;
                res.addAll(lista_coordenadas_en_el_nivel);
            } else {
                AlgorithmRandomUtilities.getInstance().newBag();
                AlgorithmRandomUtilities.getInstance().putMarbles(lista_coordenadas_en_el_nivel);
                int[] timeMarbles = AlgorithmRandomUtilities.getInstance().extractNmarbles(faltan);
                for (int i = 0; i < faltan; ++i) {
                    res.add(new Integer(timeMarbles[i]));
                }
                faltan = 0;
            }
            ++nivel;
        }
        return res;
    }
}

