/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io;

import de.erichseifert.gral.io.IOCapabilities;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class IOCapabilitiesStorage {
    private static final Set<IOCapabilities> a = new HashSet<IOCapabilities>();

    protected IOCapabilitiesStorage() {
    }

    public static Set<IOCapabilities> getCapabilities() {
        return Collections.unmodifiableSet(a);
    }

    protected static void addCapabilities(IOCapabilities iOCapabilities) {
        a.add(iOCapabilities);
    }
}

