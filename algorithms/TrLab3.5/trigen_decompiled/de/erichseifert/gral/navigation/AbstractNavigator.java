/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.navigation;

import de.erichseifert.gral.navigation.NavigationDirection;
import de.erichseifert.gral.navigation.NavigationEvent;
import de.erichseifert.gral.navigation.NavigationListener;
import de.erichseifert.gral.navigation.Navigator;
import de.erichseifert.gral.util.PointND;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractNavigator
implements Navigator {
    public static final double DEFAULT_ZOOM_FACTOR = 1.25;
    public static final double DEFAULT_ZOOM_MIN = 0.01;
    public static final double DEFAULT_ZOOM_MAX = 100.0;
    private final Set<NavigationListener> a = new HashSet<NavigationListener>();
    private double b = 1.25;
    private double c = 0.01;
    private double d = 100.0;
    private boolean e = true;
    private boolean f = true;
    private NavigationDirection g;

    @Override
    public boolean isZoomable() {
        return this.e;
    }

    @Override
    public void setZoomable(boolean bl) {
        this.e = bl;
    }

    @Override
    public void zoomIn() {
        this.zoomInAt(null);
    }

    @Override
    public void zoomOut() {
        this.zoomOutAt(null);
    }

    @Override
    public void zoomAt(double d, PointND<? extends Number> pointND) {
        if (!this.isZoomable()) {
            return;
        }
        boolean bl = this.isPannable() && pointND != null;
        PointND<? extends Number> pointND2 = null;
        if (bl) {
            pointND2 = this.getCenter();
            this.setCenter(pointND);
        }
        this.setZoom(d);
        if (bl) {
            this.setCenter(pointND2);
        }
    }

    @Override
    public void zoomInAt(PointND<? extends Number> pointND) {
        double d = this.getZoom();
        this.zoomAt(d * this.getZoomFactor(), pointND);
    }

    @Override
    public void zoomOutAt(PointND<? extends Number> pointND) {
        double d = this.getZoom();
        this.zoomAt(d / this.getZoomFactor(), pointND);
    }

    @Override
    public boolean isPannable() {
        return this.f;
    }

    @Override
    public void setPannable(boolean bl) {
        this.f = bl;
    }

    @Override
    public double getZoomFactor() {
        return this.b;
    }

    @Override
    public void setZoomFactor(double d) {
        this.b = d;
    }

    @Override
    public double getZoomMin() {
        return this.c;
    }

    @Override
    public void setZoomMin(double d) {
        this.c = d;
    }

    @Override
    public double getZoomMax() {
        return this.d;
    }

    @Override
    public void setZoomMax(double d) {
        this.d = d;
    }

    @Override
    public void addNavigationListener(NavigationListener navigationListener) {
        this.a.add(navigationListener);
    }

    @Override
    public void removeNavigationListener(NavigationListener navigationListener) {
        this.a.remove(navigationListener);
    }

    @Override
    public NavigationDirection getDirection() {
        return this.g;
    }

    @Override
    public void setDirection(NavigationDirection navigationDirection) {
        this.g = navigationDirection;
    }

    @Override
    public void connect(Navigator navigator) {
        if (navigator != null && navigator != this) {
            this.addNavigationListener(navigator);
            navigator.addNavigationListener(this);
        }
    }

    @Override
    public void disconnect(Navigator navigator) {
        if (navigator != null && navigator != this) {
            this.removeNavigationListener(navigator);
            navigator.removeNavigationListener(this);
        }
    }

    @Override
    public void centerChanged(NavigationEvent<PointND<? extends Number>> navigationEvent) {
        if (navigationEvent.getSource() != this) {
            this.setCenter(navigationEvent.getValueNew());
        }
    }

    @Override
    public void zoomChanged(NavigationEvent<Double> navigationEvent) {
        if (navigationEvent.getSource() != this) {
            this.setZoom(navigationEvent.getValueNew());
        }
    }

    protected void fireCenterChanged(NavigationEvent<PointND<? extends Number>> navigationEvent) {
        for (NavigationListener navigationListener : this.a) {
            navigationListener.centerChanged(navigationEvent);
        }
    }

    protected void fireZoomChanged(NavigationEvent<Double> navigationEvent) {
        for (NavigationListener navigationListener : this.a) {
            navigationListener.zoomChanged(navigationEvent);
        }
    }
}

