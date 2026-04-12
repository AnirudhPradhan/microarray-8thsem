/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j;

import org.slf4j.IMarkerFactory;
import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.helpers.Util;
import org.slf4j.impl.StaticMarkerBinder;

public class MarkerFactory {
    static IMarkerFactory markerFactory;

    private MarkerFactory() {
    }

    public static Marker getMarker(String name) {
        return markerFactory.getMarker(name);
    }

    public static Marker getDetachedMarker(String name) {
        return markerFactory.getDetachedMarker(name);
    }

    public static IMarkerFactory getIMarkerFactory() {
        return markerFactory;
    }

    static {
        try {
            markerFactory = StaticMarkerBinder.SINGLETON.getMarkerFactory();
        }
        catch (NoClassDefFoundError e) {
            markerFactory = new BasicMarkerFactory();
        }
        catch (Exception e) {
            Util.report("Unexpected failure while binding MarkerFactory", e);
        }
    }
}

