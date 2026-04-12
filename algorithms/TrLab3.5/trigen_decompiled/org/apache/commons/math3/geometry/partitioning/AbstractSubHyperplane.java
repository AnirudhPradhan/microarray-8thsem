/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.geometry.partitioning;

import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Transform;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class AbstractSubHyperplane<S extends Space, T extends Space>
implements SubHyperplane<S> {
    private final Hyperplane<S> hyperplane;
    private final Region<T> remainingRegion;

    protected AbstractSubHyperplane(Hyperplane<S> hyperplane, Region<T> remainingRegion) {
        this.hyperplane = hyperplane;
        this.remainingRegion = remainingRegion;
    }

    protected abstract AbstractSubHyperplane<S, T> buildNew(Hyperplane<S> var1, Region<T> var2);

    public AbstractSubHyperplane<S, T> copySelf() {
        return this.buildNew(this.hyperplane.copySelf(), this.remainingRegion);
    }

    @Override
    public Hyperplane<S> getHyperplane() {
        return this.hyperplane;
    }

    public Region<T> getRemainingRegion() {
        return this.remainingRegion;
    }

    @Override
    public double getSize() {
        return this.remainingRegion.getSize();
    }

    public AbstractSubHyperplane<S, T> reunite(SubHyperplane<S> other) {
        AbstractSubHyperplane o = (AbstractSubHyperplane)other;
        return this.buildNew(this.hyperplane, new RegionFactory<T>().union(this.remainingRegion, o.remainingRegion));
    }

    public AbstractSubHyperplane<S, T> applyTransform(Transform<S, T> transform) {
        Hyperplane<S> tHyperplane = transform.apply(this.hyperplane);
        BSPTree<T> tTree = this.recurseTransform(this.remainingRegion.getTree(false), tHyperplane, transform);
        return this.buildNew(tHyperplane, this.remainingRegion.buildNew(tTree));
    }

    private BSPTree<T> recurseTransform(BSPTree<T> node, Hyperplane<S> transformed, Transform<S, T> transform) {
        if (node.getCut() == null) {
            return new BSPTree(node.getAttribute());
        }
        BoundaryAttribute<T> attribute = (BoundaryAttribute<T>)node.getAttribute();
        if (attribute != null) {
            SubHyperplane<T> tPO = attribute.getPlusOutside() == null ? null : transform.apply(attribute.getPlusOutside(), this.hyperplane, transformed);
            SubHyperplane<T> tPI = attribute.getPlusInside() == null ? null : transform.apply(attribute.getPlusInside(), this.hyperplane, transformed);
            attribute = new BoundaryAttribute<T>(tPO, tPI);
        }
        return new BSPTree<T>(transform.apply(node.getCut(), this.hyperplane, transformed), this.recurseTransform(node.getPlus(), transformed, transform), this.recurseTransform(node.getMinus(), transformed, transform), attribute);
    }

    @Override
    public abstract Side side(Hyperplane<S> var1);

    @Override
    public abstract SubHyperplane.SplitSubHyperplane<S> split(Hyperplane<S> var1);

    @Override
    public boolean isEmpty() {
        return this.remainingRegion.isEmpty();
    }
}

