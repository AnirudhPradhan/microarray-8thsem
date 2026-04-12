/*
 * Decompiled with CFR 0.152.
 */
package analysis.biological;

import analysis.biological.GoTerm;
import java.util.Comparator;

public class PAtermOrder
implements Comparator<GoTerm> {
    @Override
    public int compare(GoTerm o1, GoTerm o2) {
        double p2;
        double p1;
        double pa2;
        int r = 0;
        double pa1 = o1.getpAdjusted();
        r = Double.compare(pa1, pa2 = o2.getpAdjusted());
        if (r == 0 && (r = Double.compare(p1 = o1.getP(), p2 = o2.getP())) == 0) {
            int ps1 = o1.getPs();
            int ps2 = o2.getPs();
            r = Integer.compare(ps1, ps2);
        }
        return r;
    }
}

