// Source reconstruction of BIOQ.computeOneSolution with empty-list guard
// Add at start: if (goStudies == null || goStudies.isEmpty()) return;

package analysis.biological;

import analysis.Solution;
import java.util.List;

public class BIOQPatcher {
    // Patched logic - call this instead to avoid crash
    public static boolean shouldSkip(List<?> goStudies, Solution solution) {
        return goStudies == null || goStudies.isEmpty();
    }
}
