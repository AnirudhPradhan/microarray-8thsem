/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots;

import de.erichseifert.gral.navigation.AbstractNavigator;
import de.erichseifert.gral.navigation.NavigationEvent;
import de.erichseifert.gral.plots.Plot;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.util.MathUtils;
import de.erichseifert.gral.util.PointND;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class PlotNavigator
extends AbstractNavigator {
    private final Plot a;
    private final Map<String, NavigationInfo> b;
    private final List<String> c = new LinkedList<String>();

    public PlotNavigator(Plot plot, List<String> list) {
        this.b = new HashMap<String, NavigationInfo>();
        this.a = plot;
        this.setAxes(list);
    }

    public PlotNavigator(Plot plot, String ... stringArray) {
        this(plot, Arrays.asList(stringArray));
    }

    private void a() {
        for (String string : this.getAxes()) {
            Object object;
            Object object2 = this.getInfo(string);
            if (object2 == null || (object = this.getPlot().getAxisRenderer(string)) == null) continue;
            Axis axis = this.getPlot().getAxis(string);
            double d = object.worldToView(axis, ((NavigationInfo)object2).getMinOriginal(), true);
            double d2 = object.worldToView(axis, ((NavigationInfo)object2).getMaxOriginal(), true);
            double d3 = d2 - d;
            double d4 = ((NavigationInfo)object2).getZoom();
            double d5 = d3 / d4;
            double d6 = object.worldToView(axis, ((NavigationInfo)object2).getCenter(), true);
            object2 = object.viewToWorld(axis, d6 - d5 * 0.5, true);
            object = object.viewToWorld(axis, d6 + d5 * 0.5, true);
            axis.setRange((Number)object2, (Number)object);
        }
    }

    protected Plot getPlot() {
        return this.a;
    }

    @Override
    public double getZoom() {
        double d = 0.0;
        int n = 0;
        for (String string : this.getAxes()) {
            NavigationInfo object = this.getInfo(string);
            if (object == null || !MathUtils.isCalculatable(object.getZoom())) continue;
            d += object.getZoom();
            ++n;
        }
        return d / (double)n;
    }

    @Override
    public void setZoom(double d) {
        if (!this.isZoomable() || d <= 0.0 || !MathUtils.isCalculatable(d)) {
            return;
        }
        double d2 = this.getZoom();
        if (d2 == (d = MathUtils.limit(d, this.getZoomMin(), this.getZoomMax()))) {
            return;
        }
        for (String string : this.getAxes()) {
            NavigationInfo object = this.getInfo(string);
            if (object == null) continue;
            object.setZoom(d);
        }
        NavigationEvent<Double> navigationEvent = new NavigationEvent<Double>(this, d2, d);
        this.fireZoomChanged(navigationEvent);
        this.a();
    }

    @Override
    public PointND<? extends Number> getCenter() {
        Object object = this.getAxes();
        Number[] numberArray = new Double[object.size()];
        int n = 0;
        object = object.iterator();
        while (object.hasNext()) {
            Object object2 = (String)object.next();
            if ((object2 = this.getInfo((String)object2)) != null) {
                double d = ((NavigationInfo)object2).getCenter();
                numberArray[n] = d;
            }
            ++n;
        }
        return new PointND(numberArray);
    }

    @Override
    public void setCenter(PointND<? extends Number> pointND) {
        if (!this.isPannable()) {
            return;
        }
        PointND<? extends Number> pointND2 = this.getCenter();
        if (pointND2.equals(pointND)) {
            return;
        }
        NavigationEvent<PointND<? extends Number>> navigationEvent = this.getAxes();
        int n = 0;
        navigationEvent = navigationEvent.iterator();
        while (navigationEvent.hasNext()) {
            Object object = (String)navigationEvent.next();
            if ((object = this.getInfo((String)object)) != null) {
                Number number = pointND.get(n);
                ((NavigationInfo)object).setCenter(number.doubleValue());
            }
            ++n;
        }
        navigationEvent = new NavigationEvent<PointND<? extends Number>>(this, pointND2, pointND);
        this.fireCenterChanged(navigationEvent);
        this.a();
    }

    @Override
    public void pan(PointND<? extends Number> pointND) {
        if (!this.isPannable()) {
            return;
        }
        PointND<? extends Number> pointND2 = this.getCenter();
        Number[] numberArray = new Double[pointND2.getDimensions()];
        int n = 0;
        for (String string : this.getAxes()) {
            NavigationInfo navigationInfo = this.getInfo(string);
            if (navigationInfo != null) {
                double d = this.getDimensionValue(string, pointND).doubleValue();
                AxisRenderer axisRenderer = this.getPlot().getAxisRenderer(string);
                if (axisRenderer != null) {
                    boolean bl = axisRenderer.isShapeDirectionSwapped();
                    if (bl) {
                        d = -d;
                    }
                    Axis axis = this.getPlot().getAxis(string);
                    double d2 = axisRenderer.worldToView(axis, navigationInfo.getCenter(), true);
                    Number number = axisRenderer.viewToWorld(axis, d2 - d, true);
                    navigationInfo.setCenter(number.doubleValue());
                    numberArray[n] = number.doubleValue();
                }
            }
            ++n;
        }
        PointND pointND3 = new PointND(numberArray);
        NavigationEvent<Object> navigationEvent = new NavigationEvent<Object>(this, pointND2, pointND3);
        this.fireCenterChanged(navigationEvent);
        this.a();
    }

    @Override
    public void setDefaultState() {
        this.b.clear();
        for (String string : this.getAxes()) {
            Object object = this.getPlot().getAxis(string);
            if (object == null) continue;
            Number number = 0.0;
            AxisRenderer axisRenderer = this.getPlot().getAxisRenderer(string);
            if (axisRenderer != null && ((Axis)object).isValid()) {
                double d = axisRenderer.worldToView((Axis)object, ((Axis)object).getMin(), false);
                double d2 = axisRenderer.worldToView((Axis)object, ((Axis)object).getMax(), false);
                if (MathUtils.isCalculatable(d) && MathUtils.isCalculatable(d2)) {
                    number = axisRenderer.viewToWorld((Axis)object, (d + d2) / 2.0, false);
                }
            }
            object = new NavigationInfo(((Axis)object).getMin(), ((Axis)object).getMax(), (double)number);
            this.b.put(string, (NavigationInfo)object);
        }
    }

    @Override
    public void reset() {
        NavigationEvent<Object> navigationEvent;
        double d = this.getZoom();
        PointND<? extends Number> pointND = this.getCenter();
        Object object = this.getAxes();
        Number[] numberArray = new Double[pointND.getDimensions()];
        int n = 0;
        object = object.iterator();
        while (object.hasNext()) {
            navigationEvent = (String)object.next();
            if ((navigationEvent = this.getInfo((String)((Object)navigationEvent))) != null) {
                double d2 = ((NavigationInfo)((Object)navigationEvent)).getCenterOriginal();
                numberArray[n] = d2;
                ((NavigationInfo)((Object)navigationEvent)).setCenter(d2);
                ((NavigationInfo)((Object)navigationEvent)).setZoom(1.0);
            }
            ++n;
        }
        object = new PointND(numberArray);
        navigationEvent = new NavigationEvent<Object>(this, pointND, object);
        this.fireCenterChanged(navigationEvent);
        navigationEvent = new NavigationEvent<Double>(this, d, 1.0);
        this.fireZoomChanged(navigationEvent);
        this.a();
    }

    protected NavigationInfo getInfo(String string) {
        return this.b.get(string);
    }

    protected List<String> getAxes() {
        return Collections.unmodifiableList(this.c);
    }

    protected void setAxes(List<String> list) {
        this.c.clear();
        this.c.addAll(list);
        this.setDefaultState();
    }

    protected void setAxes(String ... stringArray) {
        this.setAxes(Arrays.asList(stringArray));
    }

    protected abstract int getDimensions();

    protected abstract Number getDimensionValue(String var1, PointND<? extends Number> var2);

    protected static final class NavigationInfo {
        private final Number a;
        private final Number b;
        private final double c;
        private double d;
        private double e;

        public NavigationInfo(Number number, Number number2, double d) {
            this.a = number;
            this.b = number2;
            this.d = this.c = d;
            this.e = 1.0;
        }

        public final Number getMinOriginal() {
            return this.a;
        }

        public final Number getMaxOriginal() {
            return this.b;
        }

        public final double getCenterOriginal() {
            return this.c;
        }

        public final double getCenter() {
            return this.d;
        }

        public final void setCenter(double d) {
            this.d = d;
        }

        public final double getZoom() {
            return this.e;
        }

        public final void setZoom(double d) {
            this.e = d;
        }
    }
}

