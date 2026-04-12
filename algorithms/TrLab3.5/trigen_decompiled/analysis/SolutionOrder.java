/*
 * Decompiled with CFR 0.152.
 */
package analysis;

import analysis.Solution;
import java.util.Comparator;

public class SolutionOrder
implements Comparator<Solution> {
    private String parameter;
    private String order;

    public SolutionOrder(String parameter, String order) {
        this.parameter = parameter;
        this.order = order;
    }

    @Override
    public int compare(Solution o1, Solution o2) {
        double v1 = o1.getValue(this.parameter);
        double v2 = o2.getValue(this.parameter);
        int r = Double.compare(v1, v2);
        if (this.order.equalsIgnoreCase("g-l")) {
            r *= -1;
        }
        return r;
    }
}

