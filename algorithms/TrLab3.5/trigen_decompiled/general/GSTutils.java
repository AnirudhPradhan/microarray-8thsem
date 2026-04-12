/*
 * Decompiled with CFR 0.152.
 */
package general;

import general.GST;
import general.Tricluster;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GSTutils {
    public static Set<Integer> getComponentSet(Collection<Integer> component) {
        HashSet<Integer> set = new HashSet<Integer>();
        for (Integer num : component) {
            set.add(new Integer(num));
        }
        return set;
    }

    public static int componentUnionCount(Set<Integer> s1, Set<Integer> s2) {
        HashSet<Integer> union = new HashSet<Integer>(s1);
        union.addAll(s2);
        return union.size();
    }

    public static int componentIntersectionCount(Set<Integer> s1, Set<Integer> s2) {
        HashSet<Integer> intersection = new HashSet<Integer>(s1);
        intersection.retainAll(s2);
        return intersection.size();
    }

    public static int cellUnionCount(List<Tricluster> group) {
        TreeSet<GST> union = new TreeSet<GST>();
        for (Tricluster tri : group) {
            union.addAll(GSTutils.getCells(tri));
        }
        return union.size();
    }

    public static int cellUnionCount(Set<GST> s1, Set<GST> s2) {
        TreeSet<GST> union = new TreeSet<GST>(s1);
        union.addAll(s2);
        return union.size();
    }

    public static int cellIntersectionCount(List<Tricluster> group) {
        TreeSet intersection = new TreeSet();
        for (Tricluster tri : group) {
            intersection.retainAll(GSTutils.getCells(tri));
        }
        return intersection.size();
    }

    public static int cellIntersectionCount(Set<GST> s1, Set<GST> s2) {
        TreeSet<GST> intersection = new TreeSet<GST>(s1);
        intersection.retainAll(s2);
        return intersection.size();
    }

    public static Set<GST> cellUnionSet(List<Tricluster> triclusters, Tricluster ... excluding) {
        TreeSet<GST> set = new TreeSet<GST>();
        for (Tricluster tri : triclusters) {
            if (GSTutils.isIntoExcluding(tri, excluding)) continue;
            set.addAll(GSTutils.getCells(tri));
        }
        return set;
    }

    private static boolean isIntoExcluding(Tricluster tri, Tricluster[] excluding) {
        boolean enc = false;
        for (int i = 0; i < excluding.length && !enc; ++i) {
            if (excluding[i] != tri) continue;
            enc = true;
        }
        return enc;
    }

    public static Set<GST> getCells(Tricluster tri) {
        TreeSet<GST> set = new TreeSet<GST>();
        List<Integer> genes = tri.getGenes();
        List<Integer> samples = tri.getSamples();
        List<Integer> times = tri.getTimes();
        for (Integer ig : genes) {
            for (Integer is : samples) {
                for (Integer it : times) {
                    GST n = new GST(ig, is, it);
                    set.add(n);
                }
            }
        }
        return set;
    }
}

