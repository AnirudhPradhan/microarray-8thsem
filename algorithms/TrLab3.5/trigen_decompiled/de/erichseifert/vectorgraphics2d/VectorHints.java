/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d;

import java.awt.RenderingHints;
import java.util.HashSet;
import java.util.Set;

public abstract class VectorHints {
    public static final Key KEY_EXPORT = new Key(0, "Vector export mode");
    public static final Object VALUE_EXPORT_READABILITY = new Value(KEY_EXPORT, 0, "Maximize readability for humans");
    public static final Object VALUE_EXPORT_QUALITY = new Value(KEY_EXPORT, 1, "Maximize render quality");
    public static final Object VALUE_EXPORT_SIZE = new Value(KEY_EXPORT, 2, "Minimize data size");
    public static final Key KEY_TEXT = new Key(1, "Text export mode");
    public static final Object VALUE_TEXT_DEFAULT = new Value(KEY_TEXT, 0, "Keep text");
    public static final Object VALUE_TEXT_VECTOR = new Value(KEY_TEXT, 1, "Convert text to vector shapes");

    protected VectorHints() {
        throw new UnsupportedOperationException();
    }

    public static class Value {
        private static final Set<String> a = new HashSet<String>();
        private final Key b;
        private final int c;
        private final String d;

        private static synchronized void a(Value value) {
            String string = value.getId();
            if (a.contains(string)) {
                throw new ExceptionInInitializerError("Duplicate index: " + value.getIndex());
            }
            a.add(string);
        }

        public Value(Key key, int n, String string) {
            this.b = key;
            this.c = n;
            this.d = string;
            Value.a(this);
        }

        public boolean isCompatibleKey(RenderingHints.Key key) {
            return this.b == key;
        }

        public int getIndex() {
            return this.c;
        }

        public String getId() {
            return this.b.getIndex() + ":" + this.getIndex();
        }

        public String toString() {
            return this.d;
        }
    }

    public static class Key
    extends RenderingHints.Key {
        private final String a;

        public Key(int n, String string) {
            super(n);
            this.a = string;
        }

        public int getIndex() {
            return this.intKey();
        }

        @Override
        public boolean isCompatibleValue(Object object) {
            return object instanceof Value && ((Value)object).isCompatibleKey(this);
        }

        public String toString() {
            return this.a;
        }
    }
}

