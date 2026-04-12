/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class ASCII85EncodeStream
extends FilterOutputStream {
    private static final int[] a = new int[]{52200625, 614125, 7225, 85, 1};
    private static final char[] b = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstu".toCharArray();
    private boolean c;
    private final byte[] d;
    private int e;
    private final byte[] f;
    private boolean g;
    private final byte[] h;
    private final byte[] i;

    public ASCII85EncodeStream(OutputStream outputStream, String string, String string2) {
        super(outputStream);
        this.f = string != null ? string.getBytes() : "".getBytes();
        this.h = string2 != null ? string2.getBytes() : "".getBytes();
        this.d = new byte[4];
        this.i = new byte[5];
    }

    public ASCII85EncodeStream(OutputStream outputStream) {
        this(outputStream, "", "~>");
    }

    @Override
    public void write(int n) throws IOException {
        if (this.c) {
            return;
        }
        if (!this.g) {
            this.out.write(this.f);
            this.g = true;
        }
        if (this.e == 4) {
            this.a();
            this.e = 0;
        }
        this.d[this.e++] = (byte)n;
    }

    private void a() throws IOException {
        int n;
        int n2;
        int n3;
        if (this.e == 0) {
            return;
        }
        int n4 = this.e;
        byte[] byArray = this.d;
        long l = 0L;
        for (n3 = 0; n3 < 4 && n3 < n4; ++n3) {
            l |= (long)((byArray[n3] & 0xFF) << (3 - n3 << 3));
        }
        long l2 = l;
        long l3 = l2 & 0xFFFFFFFFL;
        int n5 = n2 = 4 - this.e;
        long l4 = l3;
        ASCII85EncodeStream aSCII85EncodeStream = this;
        Arrays.fill(aSCII85EncodeStream.i, (byte)0);
        if (l4 == 0L && n5 == 0) {
            aSCII85EncodeStream.i[0] = 122;
            n = 1;
        } else {
            n3 = 5 - n5;
            for (int i = 0; i < n3; ++i) {
                aSCII85EncodeStream.i[i] = (byte)b[(int)(l4 / (long)a[i] % 85L)];
            }
            n = n3;
        }
        int n6 = n;
        this.out.write(this.i, 0, n6);
    }

    @Override
    public void close() throws IOException {
        if (this.c) {
            return;
        }
        this.a();
        this.out.write(this.h);
        super.close();
        this.c = true;
    }
}

