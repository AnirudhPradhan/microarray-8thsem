/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.geometry.partitioning;

import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RegionFactory<S extends Space> {
    private final NodesCleaner nodeCleaner = new NodesCleaner();

    public Region<S> buildConvex(Hyperplane<S> ... hyperplanes) {
        if (hyperplanes == null || hyperplanes.length == 0) {
            return null;
        }
        Region<S> region = hyperplanes[0].wholeSpace();
        BSPTree<S> node = region.getTree(false);
        node.setAttribute(Boolean.TRUE);
        for (Hyperplane<S> hyperplane : hyperplanes) {
            if (!node.insertCut(hyperplane)) continue;
            node.setAttribute(null);
            node.getPlus().setAttribute(Boolean.FALSE);
            node = node.getMinus();
            node.setAttribute(Boolean.TRUE);
        }
        return region;
    }

    public Region<S> union(Region<S> region1, Region<S> region2) {
        BSPTree<S> tree = region1.getTree(false).merge(region2.getTree(false), new UnionMerger());
        tree.visit(this.nodeCleaner);
        return region1.buildNew(tree);
    }

    public Region<S> intersection(Region<S> region1, Region<S> region2) {
        BSPTree<S> tree = region1.getTree(false).merge(region2.getTree(false), new IntersectionMerger());
        tree.visit(this.nodeCleaner);
        return region1.buildNew(tree);
    }

    public Region<S> xor(Region<S> region1, Region<S> region2) {
        BSPTree<S> tree = region1.getTree(false).merge(region2.getTree(false), new XorMerger());
        tree.visit(this.nodeCleaner);
        return region1.buildNew(tree);
    }

    public Region<S> difference(Region<S> region1, Region<S> region2) {
        BSPTree<S> tree = region1.getTree(false).merge(region2.getTree(false), new DifferenceMerger());
        tree.visit(this.nodeCleaner);
        return region1.buildNew(tree);
    }

    public Region<S> getComplement(Region<S> region) {
        return region.buildNew(this.recurseComplement(region.getTree(false)));
    }

    private BSPTree<S> recurseComplement(BSPTree<S> node) {
        if (node.getCut() == null) {
            return new BSPTree((Boolean)node.getAttribute() != false ? Boolean.FALSE : Boolean.TRUE);
        }
        BoundaryAttribute attribute = (BoundaryAttribute)node.getAttribute();
        if (attribute != null) {
            SubHyperplane plusOutside = attribute.getPlusInside() == null ? null : attribute.getPlusInside().copySelf();
            SubHyperplane plusInside = attribute.getPlusOutside() == null ? null : attribute.getPlusOutside().copySelf();
            attribute = new BoundaryAttribute(plusOutside, plusInside);
        }
        return new BSPTree<S>(node.getCut().copySelf(), this.recurseComplement(node.getPlus()), this.recurseComplement(node.getMinus()), attribute);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private class NodesCleaner
    implements BSPTreeVisitor<S> {
        private NodesCleaner() {
        }

        @Override
        public BSPTreeVisitor.Order visitOrder(BSPTree<S> node) {
            return BSPTreeVisitor.Order.PLUS_SUB_MINUS;
        }

        @Override
        public void visitInternalNode(BSPTree<S> node) {
            node.setAttribute(null);
        }

        @Override
        public void visitLeafNode(BSPTree<S> node) {
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private class DifferenceMerger
    implements BSPTree.LeafMerger<S> {
        private DifferenceMerger() {
        }

        @Override
        public BSPTree<S> merge(BSPTree<S> leaf, BSPTree<S> tree, BSPTree<S> parentTree, boolean isPlusChild, boolean leafFromInstance) {
            if (((Boolean)leaf.getAttribute()).booleanValue()) {
                BSPTree argTree = RegionFactory.this.recurseComplement(leafFromInstance ? tree : leaf);
                argTree.insertInTree(parentTree, isPlusChild);
                return argTree;
            }
            BSPTree instanceTree = leafFromInstance ? leaf : tree;
            instanceTree.insertInTree(parentTree, isPlusChild);
            return instanceTree;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private class XorMerger
    implements BSPTree.LeafMerger<S> {
        private XorMerger() {
        }

        @Override
        public BSPTree<S> merge(BSPTree<S> leaf, BSPTree<S> tree, BSPTree<S> parentTree, boolean isPlusChild, boolean leafFromInstance) {
            BSPTree t = tree;
            if (((Boolean)leaf.getAttribute()).booleanValue()) {
                t = RegionFactory.this.recurseComplement(t);
            }
            t.insertInTree(parentTree, isPlusChild);
            return t;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private class IntersectionMerger
    implements BSPTree.LeafMerger<S> {
        private IntersectionMerger() {
        }

        @Override
        public BSPTree<S> merge(BSPTree<S> leaf, BSPTree<S> tree, BSPTree<S> parentTree, boolean isPlusChild, boolean leafFromInstance) {
            if (((Boolean)leaf.getAttribute()).booleanValue()) {
                tree.insertInTree(parentTree, isPlusChild);
                return tree;
            }
            leaf.insertInTree(parentTree, isPlusChild);
            return leaf;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private class UnionMerger
    implements BSPTree.LeafMerger<S> {
        private UnionMerger() {
        }

        @Override
        public BSPTree<S> merge(BSPTree<S> leaf, BSPTree<S> tree, BSPTree<S> parentTree, boolean isPlusChild, boolean leafFromInstance) {
            if (((Boolean)leaf.getAttribute()).booleanValue()) {
                leaf.insertInTree(parentTree, isPlusChild);
                return leaf;
            }
            tree.insertInTree(parentTree, isPlusChild);
            return tree;
        }
    }
}

