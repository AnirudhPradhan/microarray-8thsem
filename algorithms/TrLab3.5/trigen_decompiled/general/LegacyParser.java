/*
 * Decompiled with CFR 0.152.
 */
package general;

import general.Tricluster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.InTextFile;
import utils.OutTextFile;
import utils.TextUtilities;

public class LegacyParser {
    private static final Logger LOG = LoggerFactory.getLogger(LegacyParser.class);

    public static List<Tricluster> parse(String path) {
        ArrayList<Tricluster> r = new ArrayList<Tricluster>();
        try {
            InTextFile f = new InTextFile(path);
            Iterator<String> it = f.iterator();
            while (it.hasNext()) {
                String next1 = it.next();
                List<String> vg = TextUtilities.splitElements(next1, ";");
                String next2 = it.next();
                List<String> vc = TextUtilities.splitElements(next2, ";");
                String next3 = it.next();
                List<String> vt = TextUtilities.splitElements(next3, ";");
                Tricluster n = new Tricluster(vg, vc, vt);
                r.add(n);
                it.next();
            }
            f.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        r.trimToSize();
        return r;
    }

    public static List<Tricluster> parse(File expFile) {
        return LegacyParser.parse(expFile.getAbsolutePath());
    }

    public static void buildLegacy(File file, List<Tricluster> triclusters) throws FileNotFoundException {
        OutTextFile f = new OutTextFile(file);
        for (Tricluster tri : triclusters) {
            String str = LegacyParser.getNormalizedString(tri);
            f.print(str + "\n;;\n");
        }
        f.close();
    }

    /*
     * WARNING - void declaration
     */
    private static String getNormalizedString(Tricluster tricluster) {
        void var8_11;
        String res = "";
        List<Integer> genes = tricluster.getGenes();
        List<Integer> samples = tricluster.getSamples();
        List<Integer> times = tricluster.getTimes();
        String sg = "";
        int i = 1;
        for (Integer n : genes) {
            int g = n;
            sg = i < genes.size() ? sg + g + ";" : sg + g;
            ++i;
        }
        String sc = "";
        i = 1;
        for (Integer ic : samples) {
            int c2 = ic;
            sc = i < samples.size() ? sc + c2 + ";" : sc + c2;
            ++i;
        }
        String string = "";
        i = 1;
        for (Integer it : times) {
            int t = it;
            if (i < times.size()) {
                String string2 = (String)var8_11 + t + ";";
            } else {
                String string3 = (String)var8_11 + t;
            }
            ++i;
        }
        res = res + sg + "\n" + sc + "\n" + (String)var8_11;
        return res;
    }
}

