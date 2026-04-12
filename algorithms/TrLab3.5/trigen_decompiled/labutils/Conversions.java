/*
 * Decompiled with CFR 0.152.
 */
package labutils;

import analysis.Solution;
import general.Tricluster;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Conversions {
    private static final Logger LOG = LoggerFactory.getLogger(Conversions.class);

    public static double[] fromListOfDoubleToArray(List<Double> list) {
        double[] r = new double[list.size()];
        int index = 0;
        for (Double number : list) {
            r[index] = number;
            ++index;
        }
        return r;
    }

    public static int[] fromListOfIntegerToArray(List<Integer> list) {
        int[] r = new int[list.size()];
        int index = 0;
        for (Integer number : list) {
            r[index] = number;
            ++index;
        }
        return r;
    }

    public static String fromArrayOfStringToString(String[] array, String sep) {
        String r = "";
        for (int i = 0; i < array.length; ++i) {
            r = i == array.length - 1 ? r + array[i] : r + array[i] + sep;
        }
        return r;
    }

    public static List<Tricluster> fromSolutionsToTriclusters(List<Solution> solutions) {
        ArrayList<Tricluster> r = new ArrayList<Tricluster>(solutions.size());
        for (Solution s : solutions) {
            r.add(s.getTricluster());
        }
        return r;
    }

    public static double[] fromListIntegerToArrayDouble(Collection<Integer> list) {
        double[] r = new double[list.size()];
        int i = 0;
        for (Integer n : list) {
            r[i] = n.doubleValue();
            ++i;
        }
        return r;
    }
}

