"""
TriGen paper synthetic dataset generator.
Replicates Section 4.1: 1000 genes × 10 conditions × 5 time points, with planted areas a and b.

Reference: Gutiérrez-Avilés et al. Neurocomputing 132 (2014) 42–53
"""

import numpy as np
from pathlib import Path

# Per-condition value ranges (paper Section 4.1)
CONDITION_RANGES = [
    (1, 15),
    (7, 35),
    (60, 75),
    (0, 25),
    (30, 100),
    (71, 135),
    (160, 375),
    (5, 30),
    (25, 40),
    (10, 30),
]

# Area a: Gl=20, Cl=5, Tl=3, all values=1
AREA_A = {"genes": 20, "conditions": 5, "times": 3, "value": 1}

# Area b: Gl=30, Cl=4, Tl=4, ascending at t=0,1 and descending at t=2,3
# Levels per condition: [1,15], [60,75], [5,30], [160,375]
AREA_B_LEVELS = [(1, 15), (60, 75), (5, 30), (160, 375)]


def generate_tricgen_synthetic(seed: int = 42) -> tuple[np.ndarray, list[dict]]:
    """
    Generate synthetic dataset as in TriGen paper Section 4.1.
    Returns: tensor (1000, 10, 5), ground_truth list
    """
    rng = np.random.default_rng(seed)
    n_genes, n_conds, n_times = 1000, 10, 5

    # Background: random per condition range
    tensor = np.zeros((n_genes, n_conds, n_times), dtype=float)
    for c in range(n_conds):
        low, high = CONDITION_RANGES[c]
        tensor[:, c, :] = rng.uniform(low, high, size=(n_genes, n_times))

    ground_truth = []

    # Plant Area a: Gl=20, Cl=5, Tl=3, all values=1
    # Use fixed positions to ensure reproducibility
    ga, ca, ta = AREA_A["genes"], AREA_A["conditions"], AREA_A["times"]
    ia0, ia1 = 0, ga
    ja0, ja1 = 0, ca
    ka0, ka1 = 0, ta
    tensor[ia0:ia1, ja0:ja1, ka0:ka1] = AREA_A["value"]
    ground_truth.append({"name": "area_a", "genes": (ia0, ia1), "conditions": (ja0, ja1), "times": (ka0, ka1)})

    # Plant Area b: Gl=30, Cl=4, Tl=4
    # Ascending at t=0,1; descending at t=2,3 at different levels [1,15], [60,75], [5,30], [160,375]
    gb, cb, tb = 30, 4, 4
    ib0, ib1 = ga, ga + gb  # place after area a
    jb0, jb1 = 4, 8  # conditions 4-7
    kb0, kb1 = 0, 4  # times 0-3

    for jj, j in enumerate(range(jb0, jb1)):
        low, high = AREA_B_LEVELS[jj]
        # t=0,1 ascending; t=2,3 descending
        vals_01 = np.linspace(low, high, gb * 2).reshape(gb, 2)
        vals_23 = np.linspace(high, low, gb * 2).reshape(gb, 2)
        for kk in range(gb):
            tensor[ib0 + kk, j, kb0] = vals_01[kk, 0]
            tensor[ib0 + kk, j, kb0 + 1] = vals_01[kk, 1]
            tensor[ib0 + kk, j, kb0 + 2] = vals_23[kk, 0]
            tensor[ib0 + kk, j, kb0 + 3] = vals_23[kk, 1]
    ground_truth.append({"name": "area_b", "genes": (ib0, ib1), "conditions": (jb0, jb1), "times": (kb0, kb1)})

    return tensor, ground_truth


def main():
    from config import DATA_DIR

    base_dir = Path(__file__).parent
    data_dir = base_dir / DATA_DIR
    data_dir.mkdir(exist_ok=True)

    print("=== TriGen Synthetic Dataset (Section 4.1) ===\n")
    tensor, gt = generate_tricgen_synthetic()
    print(f"Shape: {tensor.shape} (genes × conditions × time)")
    print(f"Planted: area_a (20×5×3), area_b (30×4×4)")

    out = data_dir / "tricgen_synthetic_tensor.npz"
    # Store bounds as arrays for easy loading (area_a: ia0,ia1,ja0,ja1,ka0,ka1)
    bounds = np.array([
        [gt[0]["genes"][0], gt[0]["genes"][1], gt[0]["conditions"][0], gt[0]["conditions"][1], gt[0]["times"][0], gt[0]["times"][1]],
        [gt[1]["genes"][0], gt[1]["genes"][1], gt[1]["conditions"][0], gt[1]["conditions"][1], gt[1]["times"][0], gt[1]["times"][1]],
    ])
    np.savez_compressed(
        out,
        tensor=tensor,
        area_a_bounds=bounds[0],
        area_b_bounds=bounds[1],
        condition_ranges=np.array(CONDITION_RANGES),
    )
    print(f"\nSaved to {out}")
    print("=== Done ===")


if __name__ == "__main__":
    main()
