/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.navigation;

import de.erichseifert.gral.navigation.NavigationDirection;
import de.erichseifert.gral.navigation.NavigationListener;
import de.erichseifert.gral.util.PointND;

public interface Navigator
extends NavigationListener {
    public boolean isZoomable();

    public void setZoomable(boolean var1);

    public double getZoom();

    public void setZoom(double var1);

    public void zoomIn();

    public void zoomOut();

    public void zoomAt(double var1, PointND<? extends Number> var3);

    public void zoomInAt(PointND<? extends Number> var1);

    public void zoomOutAt(PointND<? extends Number> var1);

    public boolean isPannable();

    public void setPannable(boolean var1);

    public PointND<? extends Number> getCenter();

    public void setCenter(PointND<? extends Number> var1);

    public void pan(PointND<? extends Number> var1);

    public void setDefaultState();

    public void reset();

    public double getZoomFactor();

    public void setZoomFactor(double var1);

    public double getZoomMin();

    public void setZoomMin(double var1);

    public double getZoomMax();

    public void setZoomMax(double var1);

    public void addNavigationListener(NavigationListener var1);

    public void removeNavigationListener(NavigationListener var1);

    public NavigationDirection getDirection();

    public void setDirection(NavigationDirection var1);

    public void connect(Navigator var1);

    public void disconnect(Navigator var1);
}

