/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

public abstract class Command<T> {
    private final T a;

    public Command(T t) {
        this.a = t;
    }

    public T getValue() {
        return this.a;
    }

    public String toString() {
        return String.format(null, "%s[value=%s]", this.getClass().getName(), this.getValue());
    }

    public boolean equals(Object object) {
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        object = (Command)object;
        return this.a == ((Command)object).a || this.a.equals(((Command)object).a);
    }
}

