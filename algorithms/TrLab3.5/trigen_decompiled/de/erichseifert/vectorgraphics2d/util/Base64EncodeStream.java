/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class Base64EncodeStream
extends FilterOutputStream {
    private static final int[] a = new int[]{262144, 4096, 64, 1};
    private static final char[] b = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    private boolean c;
    private final byte[] d = new byte[3];
    private int e;
    private final byte[] f = new byte[4];

    public Base64EncodeStream(OutputStream outputStream) {
        super(outputStream);
    }

    @Override
    public void write(int n) throws IOException {
        if (this.c) {
            return;
        }
        if (this.e == 3) {
            this.a();
            this.e = 0;
        }
        this.d[this.e++] = (byte)n;
    }

    private void a() throws IOException {
        int n;
        if (this.e == 0) {
            return;
        }
        int n2 = this.e;
        byte[] byArray = this.d;
        long l = 0L;
        int n3 = 3 - n2 << 3;
        for (n = n2 - 1; n >= 0; --n) {
            l |= (long)((byArray[n] & 0xFF) << n3);
            n3 += 8;
        }
        long l2 = l;
        long l3 = l2 & 0xFFFFFFFFL;
        int n4 = n = 3 - this.e;
        long l4 = l3;
        Base64EncodeStream base64EncodeStream = this;
        Arrays.fill(base64EncodeStream.f, (byte)61);
        n3 = 4 - n4;
        for (n = 0; n < n3; ++n) {
            base64EncodeStream.f[n] = (byte)b[(int)(l4 / (long)a[n] % 64L)];
        }
        int n5 = 4;
        this.out.write(this.f, 0, n5);
    }

    @Override
    public void close() throws IOException {
        if (this.c) {
            return;
        }
        this.a();
        super.close();
        this.c = true;
    }
}

