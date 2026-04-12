/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.geometry.euclidean.threed;

import java.awt.geom.AffineTransform;
import java.util.Collection;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.threed.Euclidean3D;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.SubPlane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.apache.commons.math3.geometry.partitioning.BoundaryAttribute;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Transform;
import org.apache.commons.math3.util.FastMath;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class PolyhedronsSet
extends AbstractRegion<Euclidean3D, Euclidean2D> {
    private static final double DEFAULT_TOLERANCE = 1.0E-10;

    public PolyhedronsSet(double tolerance) {
        super(tolerance);
    }

    public PolyhedronsSet(BSPTree<Euclidean3D> tree, double tolerance) {
        super(tree, tolerance);
    }

    public PolyhedronsSet(Collection<SubHyperplane<Euclidean3D>> boundary, double tolerance) {
        super(boundary, tolerance);
    }

    public PolyhedronsSet(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax, double tolerance) {
        super(PolyhedronsSet.buildBoundary(xMin, xMax, yMin, yMax, zMin, zMax, tolerance), tolerance);
    }

    @Deprecated
    public PolyhedronsSet() {
        this(1.0E-10);
    }

    @Deprecated
    public PolyhedronsSet(BSPTree<Euclidean3D> tree) {
        this(tree, 1.0E-10);
    }

    @Deprecated
    public PolyhedronsSet(Collection<SubHyperplane<Euclidean3D>> boundary) {
        this(boundary, 1.0E-10);
    }

    @Deprecated
    public PolyhedronsSet(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        this(xMin, xMax, yMin, yMax, zMin, zMax, 1.0E-10);
    }

    private static BSPTree<Euclidean3D> buildBoundary(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax, double tolerance) {
        if (xMin >= xMax - tolerance || yMin >= yMax - tolerance || zMin >= zMax - tolerance) {
            return new BSPTree<Euclidean3D>(Boolean.FALSE);
        }
        Plane pxMin = new Plane(new Vector3D(xMin, 0.0, 0.0), Vector3D.MINUS_I, tolerance);
        Plane pxMax = new Plane(new Vector3D(xMax, 0.0, 0.0), Vector3D.PLUS_I, tolerance);
        Plane pyMin = new Plane(new Vector3D(0.0, yMin, 0.0), Vector3D.MINUS_J, tolerance);
        Plane pyMax = new Plane(new Vector3D(0.0, yMax, 0.0), Vector3D.PLUS_J, tolerance);
        Plane pzMin = new Plane(new Vector3D(0.0, 0.0, zMin), Vector3D.MINUS_K, tolerance);
        Plane pzMax = new Plane(new Vector3D(0.0, 0.0, zMax), Vector3D.PLUS_K, tolerance);
        Region boundary = new RegionFactory().buildConvex(pxMin, pxMax, pyMin, pyMax, pzMin, pzMax);
        return boundary.getTree(false);
    }

    public PolyhedronsSet buildNew(BSPTree<Euclidean3D> tree) {
        return new PolyhedronsSet(tree, this.getTolerance());
    }

    @Override
    protected void computeGeometricalProperties() {
        this.getTree(true).visit(new FacetsContributionVisitor());
        if (this.getSize() < 0.0) {
            this.setSize(Double.POSITIVE_INFINITY);
            this.setBarycenter(Vector3D.NaN);
        } else {
            this.setSize(this.getSize() / 3.0);
            this.setBarycenter(new Vector3D(1.0 / (4.0 * this.getSize()), (Vector3D)this.getBarycenter()));
        }
    }

    public SubHyperplane<Euclidean3D> firstIntersection(Vector3D point, Line line) {
        return this.recurseFirstIntersection(this.getTree(true), point, line);
    }

    private SubHyperplane<Euclidean3D> recurseFirstIntersection(BSPTree<Euclidean3D> node, Vector3D point, Line line) {
        SubHyperplane<Euclidean3D> facet;
        Vector3D hit3D;
        SubHyperplane<Euclidean3D> facet2;
        BSPTree<Euclidean3D> far;
        BSPTree<Euclidean3D> near;
        boolean in;
        SubHyperplane<Euclidean3D> cut = node.getCut();
        if (cut == null) {
            return null;
        }
        BSPTree<Euclidean3D> minus = node.getMinus();
        BSPTree<Euclidean3D> plus = node.getPlus();
        Plane plane = (Plane)cut.getHyperplane();
        double offset = plane.getOffset(point);
        boolean bl = in = FastMath.abs(offset) < 1.0E-10;
        if (offset < 0.0) {
            near = minus;
            far = plus;
        } else {
            near = plus;
            far = minus;
        }
        if (in && (facet2 = this.boundaryFacet(point, node)) != null) {
            return facet2;
        }
        SubHyperplane<Euclidean3D> crossed = this.recurseFirstIntersection(near, point, line);
        if (crossed != null) {
            return crossed;
        }
        if (!in && (hit3D = plane.intersection(line)) != null && (facet = this.boundaryFacet(hit3D, node)) != null) {
            return facet;
        }
        return this.recurseFirstIntersection(far, point, line);
    }

    private SubHyperplane<Euclidean3D> boundaryFacet(Vector3D point, BSPTree<Euclidean3D> node) {
        Point point2D = ((Plane)node.getCut().getHyperplane()).toSubSpace((Point)point);
        BoundaryAttribute attribute = (BoundaryAttribute)node.getAttribute();
        if (attribute.getPlusOutside() != null && ((SubPlane)attribute.getPlusOutside()).getRemainingRegion().checkPoint(point2D) == Region.Location.INSIDE) {
            return attribute.getPlusOutside();
        }
        if (attribute.getPlusInside() != null && ((SubPlane)attribute.getPlusInside()).getRemainingRegion().checkPoint(point2D) == Region.Location.INSIDE) {
            return attribute.getPlusInside();
        }
        return null;
    }

    public PolyhedronsSet rotate(Vector3D center, Rotation rotation) {
        return (PolyhedronsSet)this.applyTransform(new RotationTransform(center, rotation));
    }

    public PolyhedronsSet translate(Vector3D translation) {
        return (PolyhedronsSet)this.applyTransform(new TranslationTransform(translation));
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class TranslationTransform
    implements Transform<Euclidean3D, Euclidean2D> {
        private Vector3D translation;
        private Plane cachedOriginal;
        private Transform<Euclidean2D, Euclidean1D> cachedTransform;

        public TranslationTransform(Vector3D translation) {
            this.translation = translation;
        }

        public Vector3D apply(Point<Euclidean3D> point) {
            return new Vector3D(1.0, (Vector3D)point, 1.0, this.translation);
        }

        public Plane apply(Hyperplane<Euclidean3D> hyperplane) {
            return ((Plane)hyperplane).translate(this.translation);
        }

        @Override
        public SubHyperplane<Euclidean2D> apply(SubHyperplane<Euclidean2D> sub, Hyperplane<Euclidean3D> original, Hyperplane<Euclidean3D> transformed) {
            if (original != this.cachedOriginal) {
                Plane oPlane = (Plane)original;
                Plane tPlane = (Plane)transformed;
                Point shift = tPlane.toSubSpace(this.apply((Point)oPlane.getOrigin()));
                AffineTransform at = AffineTransform.getTranslateInstance(((Vector2D)shift).getX(), ((Vector2D)shift).getY());
                this.cachedOriginal = (Plane)original;
                this.cachedTransform = org.apache.commons.math3.geometry.euclidean.twod.Line.getTransform(at);
            }
            return ((SubLine)sub).applyTransform(this.cachedTransform);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class RotationTransform
    implements Transform<Euclidean3D, Euclidean2D> {
        private Vector3D center;
        private Rotation rotation;
        private Plane cachedOriginal;
        private Transform<Euclidean2D, Euclidean1D> cachedTransform;

        public RotationTransform(Vector3D center, Rotation rotation) {
            this.center = center;
            this.rotation = rotation;
        }

        public Vector3D apply(Point<Euclidean3D> point) {
            Vector delta = ((Vector3D)point).subtract((Vector)this.center);
            return new Vector3D(1.0, this.center, 1.0, this.rotation.applyTo((Vector3D)delta));
        }

        public Plane apply(Hyperplane<Euclidean3D> hyperplane) {
            return ((Plane)hyperplane).rotate(this.center, this.rotation);
        }

        @Override
        public SubHyperplane<Euclidean2D> apply(SubHyperplane<Euclidean2D> sub, Hyperplane<Euclidean3D> original, Hyperplane<Euclidean3D> transformed) {
            if (original != this.cachedOriginal) {
                Plane oPlane = (Plane)original;
                Plane tPlane = (Plane)transformed;
                Vector3D p00 = oPlane.getOrigin();
                Point p10 = oPlane.toSpace((Point)new Vector2D(1.0, 0.0));
                Point p01 = oPlane.toSpace((Point)new Vector2D(0.0, 1.0));
                Point tP00 = tPlane.toSubSpace(this.apply((Point)p00));
                Point tP10 = tPlane.toSubSpace(this.apply(p10));
                Point tP01 = tPlane.toSubSpace(this.apply(p01));
                AffineTransform at = new AffineTransform(((Vector2D)tP10).getX() - ((Vector2D)tP00).getX(), ((Vector2D)tP10).getY() - ((Vector2D)tP00).getY(), ((Vector2D)tP01).getX() - ((Vector2D)tP00).getX(), ((Vector2D)tP01).getY() - ((Vector2D)tP00).getY(), ((Vector2D)tP00).getX(), ((Vector2D)tP00).getY());
                this.cachedOriginal = (Plane)original;
                this.cachedTransform = org.apache.commons.math3.geometry.euclidean.twod.Line.getTransform(at);
            }
            return ((SubLine)sub).applyTransform(this.cachedTransform);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private class FacetsContributionVisitor
    implements BSPTreeVisitor<Euclidean3D> {
        public FacetsContributionVisitor() {
            PolyhedronsSet.this.setSize(0.0);
            PolyhedronsSet.this.setBarycenter(new Vector3D(0.0, 0.0, 0.0));
        }

        @Override
        public BSPTreeVisitor.Order visitOrder(BSPTree<Euclidean3D> node) {
            return BSPTreeVisitor.Order.MINUS_SUB_PLUS;
        }

        @Override
        public void visitInternalNode(BSPTree<Euclidean3D> node) {
            BoundaryAttribute attribute = (BoundaryAttribute)node.getAttribute();
            if (attribute.getPlusOutside() != null) {
                this.addContribution(attribute.getPlusOutside(), false);
            }
            if (attribute.getPlusInside() != null) {
                this.addContribution(attribute.getPlusInside(), true);
            }
        }

        @Override
        public void visitLeafNode(BSPTree<Euclidean3D> node) {
        }

        private void addContribution(SubHyperplane<Euclidean3D> facet, boolean reversed) {
            Region polygon = ((SubPlane)facet).getRemainingRegion();
            double area = polygon.getSize();
            if (Double.isInfinite(area)) {
                PolyhedronsSet.this.setSize(Double.POSITIVE_INFINITY);
                PolyhedronsSet.this.setBarycenter(Vector3D.NaN);
            } else {
                Plane plane = (Plane)facet.getHyperplane();
                Point facetB = plane.toSpace(polygon.getBarycenter());
                double scaled = area * ((Vector3D)facetB).dotProduct(plane.getNormal());
                if (reversed) {
                    scaled = -scaled;
                }
                PolyhedronsSet.this.setSize(PolyhedronsSet.this.getSize() + scaled);
                PolyhedronsSet.this.setBarycenter(new Vector3D(1.0, (Vector3D)PolyhedronsSet.this.getBarycenter(), scaled, (Vector3D)facetB));
            }
        }
    }
}

