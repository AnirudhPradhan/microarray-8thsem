/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.geometry.partitioning;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
import org.apache.commons.math3.geometry.partitioning.BoundaryProjection;
import org.apache.commons.math3.geometry.partitioning.BoundaryProjector;
import org.apache.commons.math3.geometry.partitioning.BoundarySizeVisitor;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Transform;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class AbstractRegion<S extends Space, T extends Space>
implements Region<S> {
    private BSPTree<S> tree;
    private final double tolerance;
    private double size;
    private Point<S> barycenter;

    protected AbstractRegion(double tolerance) {
        this.tree = new BSPTree(Boolean.TRUE);
        this.tolerance = tolerance;
    }

    protected AbstractRegion(BSPTree<S> tree, double tolerance) {
        this.tree = tree;
        this.tolerance = tolerance;
    }

    protected AbstractRegion(Collection<SubHyperplane<S>> boundary, double tolerance) {
        this.tolerance = tolerance;
        if (boundary.size() == 0) {
            this.tree = new BSPTree(Boolean.TRUE);
        } else {
            TreeSet<SubHyperplane<S>> ordered = new TreeSet<SubHyperplane<S>>(new Comparator<SubHyperplane<S>>(){

                @Override
                public int compare(SubHyperplane<S> o1, SubHyperplane<S> o2) {
                    double size1 = o1.getSize();
                    double size2 = o2.getSize();
                    return size2 < size1 ? -1 : (o1 == o2 ? 0 : 1);
                }
            });
            ordered.addAll(boundary);
            this.tree = new BSPTree();
            this.insertCuts(this.tree, ordered);
            this.tree.visit(new BSPTreeVisitor<S>(){

                @Override
                public BSPTreeVisitor.Order visitOrder(BSPTree<S> node) {
                    return BSPTreeVisitor.Order.PLUS_SUB_MINUS;
                }

                @Override
                public void visitInternalNode(BSPTree<S> node) {
                }

                @Override
                public void visitLeafNode(BSPTree<S> node) {
                    if (node.getParent() == null || node == node.getParent().getMinus()) {
                        node.setAttribute(Boolean.TRUE);
                    } else {
                        node.setAttribute(Boolean.FALSE);
                    }
                }
            });
        }
    }

    public AbstractRegion(Hyperplane<S>[] hyperplanes, double tolerance) {
        this.tolerance = tolerance;
        if (hyperplanes == null || hyperplanes.length == 0) {
            this.tree = new BSPTree(Boolean.FALSE);
        } else {
            this.tree = hyperplanes[0].wholeSpace().getTree(false);
            BSPTree<S> node = this.tree;
            node.setAttribute(Boolean.TRUE);
            for (Hyperplane<S> hyperplane : hyperplanes) {
                if (!node.insertCut(hyperplane)) continue;
                node.setAttribute(null);
                node.getPlus().setAttribute(Boolean.FALSE);
                node = node.getMinus();
                node.setAttribute(Boolean.TRUE);
            }
        }
    }

    public abstract AbstractRegion<S, T> buildNew(BSPTree<S> var1);

    public double getTolerance() {
        return this.tolerance;
    }

    private void insertCuts(BSPTree<S> node, Collection<SubHyperplane<S>> boundary) {
        Iterator<SubHyperplane<S>> iterator = boundary.iterator();
        Hyperplane<S> inserted = null;
        while (inserted == null && iterator.hasNext()) {
            inserted = iterator.next().getHyperplane();
            if (node.insertCut(inserted.copySelf())) continue;
            inserted = null;
        }
        if (!iterator.hasNext()) {
            return;
        }
        ArrayList<SubHyperplane<S>> plusList = new ArrayList<SubHyperplane<S>>();
        ArrayList<SubHyperplane<S>> minusList = new ArrayList<SubHyperplane<S>>();
        while (iterator.hasNext()) {
            SubHyperplane<S> other = iterator.next();
            switch (other.side(inserted)) {
                case PLUS: {
                    plusList.add(other);
                    break;
                }
                case MINUS: {
                    minusList.add(other);
                    break;
                }
                case BOTH: {
                    SubHyperplane.SplitSubHyperplane<S> split = other.split(inserted);
                    plusList.add(split.getPlus());
                    minusList.add(split.getMinus());
                    break;
                }
            }
        }
        this.insertCuts(node.getPlus(), plusList);
        this.insertCuts(node.getMinus(), minusList);
    }

    public AbstractRegion<S, T> copySelf() {
        return this.buildNew((BSPTree)this.tree.copySelf());
    }

    @Override
    public boolean isEmpty() {
        return this.isEmpty(this.tree);
    }

    @Override
    public boolean isEmpty(BSPTree<S> node) {
        if (node.getCut() == null) {
            return (Boolean)node.getAttribute() == false;
        }
        return this.isEmpty(node.getMinus()) && this.isEmpty(node.getPlus());
    }

    @Override
    public boolean isFull() {
        return this.isFull(this.tree);
    }

    @Override
    public boolean isFull(BSPTree<S> node) {
        if (node.getCut() == null) {
            return (Boolean)node.getAttribute();
        }
        return this.isFull(node.getMinus()) && this.isFull(node.getPlus());
    }

    @Override
    public boolean contains(Region<S> region) {
        return new RegionFactory<S>().difference(region, this).isEmpty();
    }

    @Override
    public BoundaryProjection<S> projectToBoundary(Point<S> point) {
        BoundaryProjector projector = new BoundaryProjector(point);
        this.getTree(true).visit(projector);
        return projector.getProjection();
    }

    @Override
    public Region.Location checkPoint(Vector<S> point) {
        return this.checkPoint((Point<S>)point);
    }

    @Override
    public Region.Location checkPoint(Point<S> point) {
        return this.checkPoint(this.tree, point);
    }

    protected Region.Location checkPoint(BSPTree<S> node, Vector<S> point) {
        return this.checkPoint(node, (Point<S>)point);
    }

    protected Region.Location checkPoint(BSPTree<S> node, Point<S> point) {
        Region.Location plusCode;
        BSPTree<S> cell = node.getCell(point, this.tolerance);
        if (cell.getCut() == null) {
            return (Boolean)cell.getAttribute() != false ? Region.Location.INSIDE : Region.Location.OUTSIDE;
        }
        Region.Location minusCode = this.checkPoint(cell.getMinus(), point);
        return minusCode == (plusCode = this.checkPoint(cell.getPlus(), point)) ? minusCode : Region.Location.BOUNDARY;
    }

    @Override
    public BSPTree<S> getTree(boolean includeBoundaryAttributes) {
        if (includeBoundaryAttributes && this.tree.getCut() != null && this.tree.getAttribute() == null) {
            this.tree.visit(new BoundaryBuilder());
        }
        return this.tree;
    }

    @Override
    public double getBoundarySize() {
        BoundarySizeVisitor visitor = new BoundarySizeVisitor();
        this.getTree(true).visit(visitor);
        return visitor.getSize();
    }

    @Override
    public double getSize() {
        if (this.barycenter == null) {
            this.computeGeometricalProperties();
        }
        return this.size;
    }

    protected void setSize(double size) {
        this.size = size;
    }

    @Override
    public Point<S> getBarycenter() {
        if (this.barycenter == null) {
            this.computeGeometricalProperties();
        }
        return this.barycenter;
    }

    protected void setBarycenter(Vector<S> barycenter) {
        this.setBarycenter((Point<S>)barycenter);
    }

    protected void setBarycenter(Point<S> barycenter) {
        this.barycenter = barycenter;
    }

    protected abstract void computeGeometricalProperties();

    @Override
    public Side side(Hyperplane<S> hyperplane) {
        Sides sides = new Sides();
        this.recurseSides(this.tree, hyperplane.wholeHyperplane(), sides);
        return sides.plusFound() ? (sides.minusFound() ? Side.BOTH : Side.PLUS) : (sides.minusFound() ? Side.MINUS : Side.HYPER);
    }

    private void recurseSides(BSPTree<S> node, SubHyperplane<S> sub, Sides sides) {
        if (node.getCut() == null) {
            if (((Boolean)node.getAttribute()).booleanValue()) {
                sides.rememberPlusFound();
                sides.rememberMinusFound();
            }
            return;
        }
        Hyperplane<S> hyperplane = node.getCut().getHyperplane();
        switch (sub.side(hyperplane)) {
            case PLUS: {
                if (node.getCut().side(sub.getHyperplane()) == Side.PLUS) {
                    if (!this.isEmpty(node.getMinus())) {
                        sides.rememberPlusFound();
                    }
                } else if (!this.isEmpty(node.getMinus())) {
                    sides.rememberMinusFound();
                }
                if (sides.plusFound() && sides.minusFound()) break;
                this.recurseSides(node.getPlus(), sub, sides);
                break;
            }
            case MINUS: {
                if (node.getCut().side(sub.getHyperplane()) == Side.PLUS) {
                    if (!this.isEmpty(node.getPlus())) {
                        sides.rememberPlusFound();
                    }
                } else if (!this.isEmpty(node.getPlus())) {
                    sides.rememberMinusFound();
                }
                if (sides.plusFound() && sides.minusFound()) break;
                this.recurseSides(node.getMinus(), sub, sides);
                break;
            }
            case BOTH: {
                SubHyperplane.SplitSubHyperplane<S> split = sub.split(hyperplane);
                this.recurseSides(node.getPlus(), split.getPlus(), sides);
                if (sides.plusFound() && sides.minusFound()) break;
                this.recurseSides(node.getMinus(), split.getMinus(), sides);
                break;
            }
            default: {
                if (node.getCut().getHyperplane().sameOrientationAs(sub.getHyperplane())) {
                    if (node.getPlus().getCut() != null || ((Boolean)node.getPlus().getAttribute()).booleanValue()) {
                        sides.rememberPlusFound();
                    }
                    if (node.getMinus().getCut() == null && !((Boolean)node.getMinus().getAttribute()).booleanValue()) break;
                    sides.rememberMinusFound();
                    break;
                }
                if (node.getPlus().getCut() != null || ((Boolean)node.getPlus().getAttribute()).booleanValue()) {
                    sides.rememberMinusFound();
                }
                if (node.getMinus().getCut() == null && !((Boolean)node.getMinus().getAttribute()).booleanValue()) break;
                sides.rememberPlusFound();
            }
        }
    }

    @Override
    public SubHyperplane<S> intersection(SubHyperplane<S> sub) {
        return this.recurseIntersection(this.tree, sub);
    }

    private SubHyperplane<S> recurseIntersection(BSPTree<S> node, SubHyperplane<S> sub) {
        if (node.getCut() == null) {
            return (Boolean)node.getAttribute() != false ? sub.copySelf() : null;
        }
        Hyperplane<S> hyperplane = node.getCut().getHyperplane();
        switch (sub.side(hyperplane)) {
            case PLUS: {
                return this.recurseIntersection(node.getPlus(), sub);
            }
            case MINUS: {
                return this.recurseIntersection(node.getMinus(), sub);
            }
            case BOTH: {
                SubHyperplane.SplitSubHyperplane<S> split = sub.split(hyperplane);
                SubHyperplane<S> plus = this.recurseIntersection(node.getPlus(), split.getPlus());
                SubHyperplane<S> minus = this.recurseIntersection(node.getMinus(), split.getMinus());
                if (plus == null) {
                    return minus;
                }
                if (minus == null) {
                    return plus;
                }
                return plus.reunite(minus);
            }
        }
        return this.recurseIntersection(node.getPlus(), this.recurseIntersection(node.getMinus(), sub));
    }

    public AbstractRegion<S, T> applyTransform(Transform<S, T> transform) {
        return this.buildNew((BSPTree)this.recurseTransform(this.getTree(false), transform));
    }

    private BSPTree<S> recurseTransform(BSPTree<S> node, Transform<S, T> transform) {
        if (node.getCut() == null) {
            return new BSPTree(node.getAttribute());
        }
        SubHyperplane<S> sub = node.getCut();
        AbstractSubHyperplane<S, T> tSub = ((AbstractSubHyperplane)sub).applyTransform(transform);
        BoundaryAttribute attribute = (BoundaryAttribute)node.getAttribute();
        if (attribute != null) {
            AbstractSubHyperplane<S, T> tPO = attribute.getPlusOutside() == null ? null : ((AbstractSubHyperplane)attribute.getPlusOutside()).applyTransform(transform);
            AbstractSubHyperplane<S, T> tPI = attribute.getPlusInside() == null ? null : ((AbstractSubHyperplane)attribute.getPlusInside()).applyTransform(transform);
            attribute = new BoundaryAttribute(tPO, tPI);
        }
        return new BSPTree<S>(tSub, this.recurseTransform(node.getPlus(), transform), this.recurseTransform(node.getMinus(), transform), attribute);
    }

    private static final class Sides {
        private boolean plusFound = false;
        private boolean minusFound = false;

        public void rememberPlusFound() {
            this.plusFound = true;
        }

        public boolean plusFound() {
            return this.plusFound;
        }

        public void rememberMinusFound() {
            this.minusFound = true;
        }

        public boolean minusFound() {
            return this.minusFound;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class BoundaryBuilder<S extends Space>
    implements BSPTreeVisitor<S> {
        private BoundaryBuilder() {
        }

        @Override
        public BSPTreeVisitor.Order visitOrder(BSPTree<S> node) {
            return BSPTreeVisitor.Order.PLUS_MINUS_SUB;
        }

        @Override
        public void visitInternalNode(BSPTree<S> node) {
            SubHyperplane[] minusChar;
            SubHyperplane plusOutside = null;
            SubHyperplane plusInside = null;
            SubHyperplane[] plusChar = (SubHyperplane[])Array.newInstance(SubHyperplane.class, 2);
            this.characterize(node.getPlus(), node.getCut().copySelf(), plusChar);
            if (plusChar[0] != null && !plusChar[0].isEmpty()) {
                minusChar = (SubHyperplane[])Array.newInstance(SubHyperplane.class, 2);
                this.characterize(node.getMinus(), plusChar[0], minusChar);
                if (minusChar[1] != null && !minusChar[1].isEmpty()) {
                    plusOutside = minusChar[1];
                }
            }
            if (plusChar[1] != null && !plusChar[1].isEmpty()) {
                minusChar = (SubHyperplane[])Array.newInstance(SubHyperplane.class, 2);
                this.characterize(node.getMinus(), plusChar[1], minusChar);
                if (minusChar[0] != null && !minusChar[0].isEmpty()) {
                    plusInside = minusChar[0];
                }
            }
            node.setAttribute(new BoundaryAttribute(plusOutside, plusInside));
        }

        @Override
        public void visitLeafNode(BSPTree<S> node) {
        }

        private void characterize(BSPTree<S> node, SubHyperplane<S> sub, SubHyperplane<S>[] characterization) {
            if (node.getCut() == null) {
                boolean inside = (Boolean)node.getAttribute();
                if (inside) {
                    characterization[1] = characterization[1] == null ? sub : characterization[1].reunite(sub);
                } else {
                    characterization[0] = characterization[0] == null ? sub : characterization[0].reunite(sub);
                }
            } else {
                Hyperplane<S> hyperplane = node.getCut().getHyperplane();
                switch (sub.side(hyperplane)) {
                    case PLUS: {
                        this.characterize(node.getPlus(), sub, characterization);
                        break;
                    }
                    case MINUS: {
                        this.characterize(node.getMinus(), sub, characterization);
                        break;
                    }
                    case BOTH: {
                        SubHyperplane.SplitSubHyperplane<S> split = sub.split(hyperplane);
                        this.characterize(node.getPlus(), split.getPlus(), characterization);
                        this.characterize(node.getMinus(), split.getMinus(), characterization);
                        break;
                    }
                    default: {
                        throw new MathInternalError();
                    }
                }
            }
        }
    }
}

