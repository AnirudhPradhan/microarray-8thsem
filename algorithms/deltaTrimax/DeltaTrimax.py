#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
δ-Trimax triclustering algorithm.

Original author: novalsaputra (https://github.com/novalsaputra/Delta-Trimax)
Modified: fixed deprecated numpy types (np.bool → bool, np.float → float)

Algorithm: 3D extension of Cheng-Church biclustering.
Iteratively removes genes/conditions/timepoints with high Mean Square Residue,
then adds back those that improve coherence. Repeats for n_triclusters.

Data format: D[time, gene, condition]
"""

import numpy as np
import sys
import time


class DeltaTrimax:
    def __init__(self, D):
        self.D = D.copy()
        self.D_asli = D.copy()

    def hitung_MSR(self, gene, kondisi, waktu,
                   g_add=False, k_add=False, w_add=False):
        gene_idx   = np.expand_dims(np.expand_dims(np.nonzero(gene)[0],  axis=0), axis=2)
        waktu_idx  = np.expand_dims(np.expand_dims(np.nonzero(waktu)[0], axis=1), axis=1)
        kondisi_idx = np.expand_dims(np.expand_dims(np.nonzero(kondisi)[0], axis=0), axis=0)

        subarr = self.D[waktu_idx, gene_idx, kondisi_idx]
        self.n_gene    = subarr.shape[1]
        self.n_kondisi = subarr.shape[2]
        self.n_waktu   = subarr.shape[0]

        m_iJK = np.nanmean(np.nanmean(subarr, axis=2), axis=0)
        m_iJK = np.expand_dims(np.expand_dims(m_iJK, axis=0), axis=2)

        m_IjK = np.nanmean(np.nanmean(subarr, axis=0), axis=0)
        m_IjK = np.expand_dims(np.expand_dims(m_IjK, axis=0), axis=1)

        m_IJk = np.nanmean(np.nanmean(subarr, axis=2), axis=1)
        m_IJk = np.expand_dims(np.expand_dims(m_IJk, axis=1), axis=2)

        m_IJK = np.mean(subarr)

        residue = subarr - m_iJK - m_IjK - m_IJk + (2 * m_IJK)
        SR = np.square(residue)
        self.MSR         = np.mean(SR)
        self.MSR_gene    = np.nanmean(np.nanmean(SR, axis=2), axis=0)
        self.MSR_kondisi = np.nanmean(np.nanmean(SR, axis=0), axis=0)
        self.MSR_waktu   = np.nanmean(np.nanmean(SR, axis=2), axis=1)

        if g_add:
            non_gene = np.expand_dims(np.expand_dims(np.nonzero(gene == 0)[0], axis=0), axis=2)
            D_b = self.D.copy()
            D_b[waktu_idx, non_gene, kondisi_idx] = self.D_asli[waktu_idx, non_gene, kondisi_idx]
            subarr_b = D_b[waktu_idx, non_gene, kondisi_idx]
            m_iJK_b = np.nanmean(np.nanmean(subarr_b, axis=2), axis=0)
            m_iJK_b = np.expand_dims(np.expand_dims(m_iJK_b, axis=0), axis=2)
            r = subarr_b - m_iJK_b - m_IjK - m_IJk + (2 * m_IJK)
            sr_b = np.square(r)
            self.MSR_gene_b = np.nanmean(np.nanmean(sr_b, axis=2), axis=0)

        if k_add:
            non_kondisi = np.expand_dims(np.expand_dims(np.nonzero(kondisi == 0)[0], axis=0), axis=0)
            D_b = self.D.copy()
            D_b[waktu_idx, gene_idx, non_kondisi] = self.D_asli[waktu_idx, gene_idx, non_kondisi]
            subarr_b = D_b[waktu_idx, gene_idx, non_kondisi]
            m_IjK_b = np.nanmean(np.nanmean(subarr_b, axis=0), axis=0)
            m_IjK_b = np.expand_dims(np.expand_dims(m_IjK_b, axis=0), axis=1)
            r = subarr_b - m_iJK - m_IjK_b - m_IJk + (2 * m_IJK)
            sr_b = np.square(r)
            self.MSR_kondisi_b = np.nanmean(np.nanmean(sr_b, axis=0), axis=0)

        if w_add:
            non_waktu = np.expand_dims(np.expand_dims(np.nonzero(waktu == 0)[0], axis=1), axis=1)
            D_b = self.D.copy()
            D_b[non_waktu, gene_idx, kondisi_idx] = self.D_asli[non_waktu, gene_idx, kondisi_idx]
            subarr_b = D_b[non_waktu, gene_idx, kondisi_idx]
            m_IJk_b = np.nanmean(np.nanmean(subarr_b, axis=2), axis=1)
            m_IJk_b = np.expand_dims(np.expand_dims(m_IJk_b, axis=1), axis=2)
            r = subarr_b - m_iJK - m_IjK - m_IJk_b + (2 * m_IJK)
            sr_b = np.square(r)
            self.MSR_waktu_b = np.nanmean(np.nanmean(sr_b, axis=2), axis=1)

    def multiple_node_deletion(self, gene, kondisi, waktu):
        self.hitung_MSR(gene, kondisi, waktu)
        while self.MSR > self.delta:
            hapus = 0

            if self.n_gene > self.gene_cutoff:
                gene_dihapus = self.MSR_gene > (self.MSR * self.lamda)
                nonz_idx = gene.nonzero()[0]
                if gene_dihapus.any():
                    hapus = 1
                gene.put(nonz_idx[gene_dihapus], 0)
                self.hitung_MSR(gene, kondisi, waktu)

            if self.n_kondisi > self.kondisi_cutoff:
                kondisi_dihapus = self.MSR_kondisi > (self.MSR * self.lamda)
                nonz_idx = kondisi.nonzero()[0]
                if kondisi_dihapus.any():
                    hapus = 1
                kondisi.put(nonz_idx[kondisi_dihapus], 0)
                self.hitung_MSR(gene, kondisi, waktu)

            if self.n_waktu > self.waktu_cutoff:
                waktu_dihapus = self.MSR_waktu > (self.MSR * self.lamda)
                nonz_idx = waktu.nonzero()[0]
                if waktu_dihapus.any():
                    hapus = 1
                waktu.put(nonz_idx[waktu_dihapus], 0)
                self.hitung_MSR(gene, kondisi, waktu)

            if not hapus:
                break
            self.muldel += 1

        return gene, kondisi, waktu

    def single_node_deletion(self, gene, kondisi, waktu):
        self.hitung_MSR(gene, kondisi, waktu)
        max_iters = gene.sum() + kondisi.sum() + waktu.sum()
        iters = 0
        while self.MSR > self.delta and iters < max_iters:
            gene_max    = np.argmax(self.MSR_gene)
            kondisi_max = np.argmax(self.MSR_kondisi)
            waktu_max   = np.argmax(self.MSR_waktu)

            g_msr = self.MSR_gene[gene_max]
            k_msr = self.MSR_kondisi[kondisi_max]
            w_msr = self.MSR_waktu[waktu_max]

            if g_msr >= k_msr and g_msr >= w_msr and self.n_gene > self.min_genes:
                nonz_idx = gene.nonzero()[0]
                gene.put(nonz_idx[gene_max], 0)
            elif k_msr >= w_msr and self.n_kondisi > self.min_kondisi:
                nonz_idx = kondisi.nonzero()[0]
                kondisi.put(nonz_idx[kondisi_max], 0)
            elif self.n_waktu > self.min_waktu:
                nonz_idx = waktu.nonzero()[0]
                waktu.put(nonz_idx[waktu_max], 0)
            else:
                break  # cannot remove any more dimensions

            self.singdel += 1
            iters += 1
            self.hitung_MSR(gene, kondisi, waktu)

        return gene, kondisi, waktu

    def node_addition(self, gene, kondisi, waktu):
        self.hitung_MSR(gene, kondisi, waktu)
        while True:
            self.hitung_MSR(gene, kondisi, waktu)

            # gene addition
            self.hitung_MSR(gene, kondisi, waktu, g_add=True)
            no_gene_idx = np.nonzero(gene == 0)[0]
            gene_to_add = self.MSR_gene_b < self.MSR
            if gene_to_add.any():
                gene.put(no_gene_idx[gene_to_add], 1)
                g_idx = np.expand_dims(np.expand_dims(no_gene_idx[gene_to_add], axis=0), axis=2)
                k_idx = np.expand_dims(np.expand_dims(np.nonzero(kondisi)[0], axis=0), axis=0)
                w_idx = np.expand_dims(np.expand_dims(np.nonzero(waktu)[0], axis=1), axis=2)
                self.D[w_idx, g_idx, k_idx] = self.D_asli[w_idx, g_idx, k_idx]

            self.hitung_MSR(gene, kondisi, waktu)

            # condition addition
            self.hitung_MSR(gene, kondisi, waktu, k_add=True)
            no_kondisi = np.nonzero(kondisi == 0)[0]
            kondisi_to_add = self.MSR_kondisi_b < self.MSR
            if kondisi_to_add.any():
                kondisi.put(no_kondisi[kondisi_to_add], 1)
                g_idx = np.expand_dims(np.expand_dims(np.nonzero(gene)[0], axis=0), axis=2)
                k_idx = np.expand_dims(np.expand_dims(no_kondisi[kondisi_to_add], axis=0), axis=0)
                w_idx = np.expand_dims(np.expand_dims(np.nonzero(waktu)[0], axis=1), axis=2)
                self.D[w_idx, g_idx, k_idx] = self.D_asli[w_idx, g_idx, k_idx]

            self.hitung_MSR(gene, kondisi, waktu)

            # time addition
            self.hitung_MSR(gene, kondisi, waktu, w_add=True)
            no_waktu = np.nonzero(waktu == 0)[0]
            waktu_to_add = self.MSR_waktu_b < self.MSR
            if waktu_to_add.any():
                waktu.put(no_waktu[waktu_to_add], 1)
                g_idx = np.expand_dims(np.expand_dims(np.nonzero(gene)[0], axis=0), axis=2)
                k_idx = np.expand_dims(np.expand_dims(np.nonzero(kondisi)[0], axis=0), axis=0)
                w_idx = np.expand_dims(np.expand_dims(no_waktu[waktu_to_add], axis=1), axis=2)
                self.D[w_idx, g_idx, k_idx] = self.D_asli[w_idx, g_idx, k_idx]

            self.hitung_MSR(gene, kondisi, waktu)

            if not gene_to_add.any() and not kondisi_to_add.any() and not waktu_to_add.any():
                break

            self.nodeadd += 1

        return gene, kondisi, waktu

    def mask(self, gene, kondisi, waktu):
        g = np.expand_dims(np.expand_dims(gene.nonzero()[0], axis=0), axis=2)
        k = np.expand_dims(np.expand_dims(kondisi.nonzero()[0], axis=0), axis=0)
        w = np.expand_dims(np.expand_dims(waktu.nonzero()[0], axis=1), axis=2)

        shape = (np.count_nonzero(waktu), np.count_nonzero(gene), np.count_nonzero(kondisi))
        if self.mask_mode == 'nan':
            mask_val = np.nan
        else:  # 'random'
            mask_val = np.random.uniform(self.minval, self.maxval, shape)

        self.D[w, g, k] = mask_val

    def fit(
        self,
        delta,
        lamda,
        mask_mode="random",
        n_triclusters=50,
        min_genes=2,
        min_kondisi=2,
        min_waktu=2,
        verbose=False,
    ):
        """
        Find n_triclusters triclusters by iterative deletion+addition+masking.

        Parameters
        ----------
        delta         : MSR threshold — stop deletion when MSR ≤ delta
        lamda         : multiple-deletion multiplier (nodes with MSR > lamda×MSR removed)
        mask_mode     : "random" or "nan"
        n_triclusters : number of triclusters to extract
        min_genes     : minimum gene count per tricluster (safety cutoff for single deletion)
        min_kondisi   : minimum condition count
        min_waktu     : minimum time count
        verbose       : print progress

        Returns
        -------
        list of (gene_frozenset, sample_frozenset, time_frozenset)
        """
        n_waktu, n_gene, n_kondisi = self.D.shape

        self.delta     = delta
        self.lamda     = lamda
        self.mask_mode = mask_mode
        self.minval    = float(np.nanmin(self.D))
        self.maxval    = float(np.nanmax(self.D))

        # multiple deletion cutoffs — only delete if count > cutoff
        self.gene_cutoff    = min_genes
        self.kondisi_cutoff = min_kondisi
        self.waktu_cutoff   = min_waktu

        # single deletion minimum sizes
        self.min_genes   = min_genes
        self.min_kondisi = min_kondisi
        self.min_waktu   = min_waktu

        results = []

        for i in range(1, n_triclusters + 1):
            self.muldel  = 0
            self.singdel = 0
            self.nodeadd = 0

            waktu   = np.ones(n_waktu,   dtype=bool)
            gene    = np.ones(n_gene,    dtype=bool)
            kondisi = np.ones(n_kondisi, dtype=bool)

            if self.mask_mode == "nan":
                gene_means = np.nanmean(np.nanmean(self.D, axis=0), axis=1)
                gene = ~np.isnan(gene_means)

            gene, kondisi, waktu = self.multiple_node_deletion(gene, kondisi, waktu)
            gene, kondisi, waktu = self.single_node_deletion(gene, kondisi, waktu)
            gene, kondisi, waktu = self.node_addition(gene, kondisi, waktu)

            g_count = int(gene.sum())
            k_count = int(kondisi.sum())
            w_count = int(waktu.sum())

            if verbose:
                print(f"  TC {i}: genes={g_count}, conds={k_count}, times={w_count}, MSR={self.MSR:.2f}")

            # Stop if degenerate (single dimension)
            if g_count <= 1 or k_count <= 1 or w_count <= 1:
                if verbose:
                    print(f"  Stopping at tricluster {i} (degenerate)")
                break

            gene_idx    = frozenset(int(x) for x in np.nonzero(gene)[0])
            kondisi_idx = frozenset(int(x) for x in np.nonzero(kondisi)[0])
            waktu_idx   = frozenset(int(x) for x in np.nonzero(waktu)[0])
            results.append((gene_idx, kondisi_idx, waktu_idx))

            self.mask(gene, kondisi, waktu)

        return results
