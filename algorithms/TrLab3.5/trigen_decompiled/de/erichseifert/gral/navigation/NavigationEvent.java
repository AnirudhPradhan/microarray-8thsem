/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.navigation;

import de.erichseifert.gral.navigation.Navigator;

public class NavigationEvent<T> {
    private final Navigator a;
    private final T b;
    private final T c;

    public NavigationEvent(Navigator navigator, T t, T t2) {
        this.a = navigator;
        this.b = t;
        this.c = t2;
    }

    public Navigator getSource() {
        return this.a;
    }

    public T getValueOld() {
        return this.b;
    }

    public T getValueNew() {
        return this.c;
    }
}

