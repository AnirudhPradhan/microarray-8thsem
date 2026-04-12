/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.navigation;

import de.erichseifert.gral.navigation.NavigationEvent;
import de.erichseifert.gral.util.PointND;

public interface NavigationListener {
    public void centerChanged(NavigationEvent<PointND<? extends Number>> var1);

    public void zoomChanged(NavigationEvent<Double> var1);
}

