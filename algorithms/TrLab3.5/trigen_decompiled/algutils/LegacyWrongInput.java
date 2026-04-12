/*
 * Decompiled with CFR 0.152.
 */
package algutils;

public class LegacyWrongInput
extends Exception {
    private String mensaje;

    public LegacyWrongInput(String message) {
        this.mensaje = message;
    }

    public String getMenssage() {
        return this.mensaje;
    }
}

