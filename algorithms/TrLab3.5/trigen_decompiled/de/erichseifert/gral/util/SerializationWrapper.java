/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import java.io.Serializable;

public interface SerializationWrapper<T>
extends Serializable {
    public T unwrap();
}

