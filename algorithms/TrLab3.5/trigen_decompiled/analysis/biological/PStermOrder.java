/*
 * Decompiled with CFR 0.152.
 */
package analysis.biological;

import analysis.biological.GoTerm;
import java.util.Comparator;

public class PStermOrder
implements Comparator<GoTerm> {
    @Override
    public int compare(GoTerm o1, GoTerm o2) {
        double pa2;
        double pa1;
        int ps2;
        int r = 0;
        int ps1 = o1.getPs();
        r = Integer.compare(ps1, ps2 = o2.getPs());
        if (r == 0 && (r = Double.compare(pa1 = o1.getpAdjusted(), pa2 = o2.getpAdjusted())) == 0) {
            double p1 = o1.getP();
            double p2 = o2.getP();
            r = Double.compare(p1, p2);
        }
        return r;
    }
}

