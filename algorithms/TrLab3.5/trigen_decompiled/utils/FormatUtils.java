/*
 * Decompiled with CFR 0.152.
 */
package utils;

import general.Tricluster;
import java.text.Format;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TextUtilities;

public class FormatUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FormatUtils.class);

    public static String[] getExcelStrings(List<Tricluster> solutions, double[][][] dataset) {
        return FormatUtils.getExcelStings(solutions, dataset, TextUtilities.getDecimalFormat('.', "0.####"));
    }

    public static String[] getExcelStings(List<Tricluster> solutions, double[][][] dataset, Format format) {
        String[] f = new String[solutions.size()];
        int i = 0;
        for (Tricluster sol : solutions) {
            int j;
            int j2;
            String str = "";
            List<Integer> times = sol.getTimes();
            List<Integer> genes = sol.getGenes();
            List<Integer> samples = sol.getSamples();
            int nt = times.size();
            int ng = genes.size();
            int nc = samples.size();
            str = str + ";";
            for (int t : times) {
                for (j2 = 0; j2 < nc; ++j2) {
                    str = j2 == nc - 1 ? str + "t" + t + ";;" : str + "t" + t + ";";
                }
            }
            str = str + "\n";
            str = str + ";";
            Iterator<Integer> ittimes1 = times.iterator();
            while (ittimes1.hasNext()) {
                ittimes1.next();
                for (j2 = 0; j2 < nc; ++j2) {
                    int c2 = samples.get(j2);
                    str = j2 == nc - 1 ? str + "c" + c2 + ";;" : str + "c" + c2 + ";";
                }
            }
            str = str + "\n";
            for (int g = 0; g < ng; ++g) {
                for (int t = 0; t < nt; ++t) {
                    for (int c3 = -1; c3 < nc; ++c3) {
                        int cg = genes.get(g);
                        if (c3 != -1) {
                            boolean cond1 = c3 == nc - 1;
                            boolean cond2 = t == nt - 1;
                            int ct = times.get(t);
                            int cc = samples.get(c3);
                            String cad = format.format(dataset[cg][cc][ct]);
                            cad = !cond1 && !cond2 ? cad + ";" : (cond1 && cond2 ? cad + "\n" : cad + ";");
                            str = str + cad;
                            continue;
                        }
                        str = str + "g" + cg + ";";
                    }
                }
            }
            str = str + "\n\n\n\n";
            str = str + ";";
            for (int c2 : samples) {
                for (j = 0; j < nt; ++j) {
                    str = j == nt - 1 ? str + "c" + c2 + ";;" : str + "c" + c2 + ";";
                }
            }
            str = str + "\n";
            str = str + ";";
            Iterator<Integer> itsamples1 = samples.iterator();
            while (itsamples1.hasNext()) {
                itsamples1.next();
                for (j = 0; j < nt; ++j) {
                    int t = times.get(j);
                    str = j == nt - 1 ? str + "t" + t + ";;" : str + "t" + t + ";";
                }
            }
            str = str + "\n";
            for (int g = 0; g < ng; ++g) {
                for (int c4 = 0; c4 < nc; ++c4) {
                    for (int t = -1; t < nt; ++t) {
                        int cg = genes.get(g);
                        if (t != -1) {
                            boolean cond1 = t == nt - 1;
                            boolean cond2 = c4 == nc - 1;
                            int ct = times.get(t);
                            int cc = samples.get(c4);
                            String cad = format.format(dataset[cg][cc][ct]);
                            cad = !cond1 && !cond2 ? cad + ";" : (cond1 && cond2 ? cad + "\n" : cad + ";");
                            str = str + cad;
                            continue;
                        }
                        str = str + "g" + cg + ";";
                    }
                }
            }
            f[i] = str;
            ++i;
        }
        return f;
    }

    public static String[] getRstrings(List<Tricluster> solutions, double[][][] dataset) {
        return FormatUtils.getRstrings(solutions, dataset, TextUtilities.getDecimalFormat('.', "0.####"));
    }

    public static String[] getRstrings(List<Tricluster> solutions, double[][][] dataset, Format format) {
        String[] f = new String[solutions.size()];
        int i = 0;
        for (Tricluster sol : solutions) {
            String str = "";
            List<Integer> times = sol.getTimes();
            List<Integer> genes = sol.getGenes();
            List<Integer> samples = sol.getSamples();
            str = str + "t;s;g;el\n";
            for (int v_t : times) {
                for (int v_c : samples) {
                    for (int v_g : genes) {
                        double value = dataset[v_g][v_c][v_t];
                        str = str + v_t + ";" + v_c + ";" + v_g + ";" + format.format(value) + "\n";
                    }
                }
            }
            f[i] = str;
            ++i;
        }
        return f;
    }
}

