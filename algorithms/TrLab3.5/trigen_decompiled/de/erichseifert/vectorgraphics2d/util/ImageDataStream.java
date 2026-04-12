/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.util;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

public class ImageDataStream
extends InputStream {
    private final BufferedImage a;
    private final int b;
    private final int c;
    private final Interleaving d;
    private final Raster e;
    private final boolean f;
    private final Queue<Integer> g;
    private final int[] h;
    private final int[] i;
    private int j;
    private int k;

    public ImageDataStream(BufferedImage bufferedImage, Interleaving interleaving) {
        this.a = bufferedImage;
        this.d = interleaving;
        this.b = bufferedImage.getWidth();
        this.c = bufferedImage.getHeight();
        this.j = -1;
        this.k = 0;
        WritableRaster writableRaster = bufferedImage.getAlphaRaster();
        this.e = interleaving == Interleaving.ALPHA_ONLY ? writableRaster : bufferedImage.getRaster();
        this.f = writableRaster == null;
        this.g = new LinkedList<Integer>();
        this.h = new int[this.e.getNumBands()];
        this.i = this.e.getSampleModel().getSampleSize();
    }

    public BufferedImage getImage() {
        return this.a;
    }

    public Interleaving getInterleaving() {
        return this.d;
    }

    @Override
    public int read() throws IOException {
        boolean bl;
        if (!this.g.isEmpty()) {
            return this.g.poll();
        }
        ImageDataStream imageDataStream = this;
        if (imageDataStream.d == Interleaving.SAMPLE || imageDataStream.d == Interleaving.WITHOUT_ALPHA) {
            ++imageDataStream.j;
            if (imageDataStream.j >= imageDataStream.b) {
                imageDataStream.j = 0;
                ++imageDataStream.k;
            }
        }
        if (imageDataStream.j < 0 || imageDataStream.j >= imageDataStream.b || imageDataStream.k < 0 || imageDataStream.k >= imageDataStream.c) {
            bl = false;
        } else {
            imageDataStream.e.getPixel(imageDataStream.j, imageDataStream.k, imageDataStream.h);
            bl = true;
        }
        if (!bl) {
            return -1;
        }
        int n = this.h.length;
        if (this.d == Interleaving.WITHOUT_ALPHA || this.d == Interleaving.ALPHA_ONLY) {
            if (this.d == Interleaving.WITHOUT_ALPHA && !this.f) {
                --n;
            }
            for (int i = 0; i < n; ++i) {
                this.a(i);
            }
        } else if (this.f) {
            for (int i = 0; i < n; ++i) {
                this.a(i);
            }
        } else {
            for (int i = 0; i < n; ++i) {
                if (i == 0) {
                    this.a(n - 1);
                    continue;
                }
                this.a(i - 1);
            }
        }
        if (!this.g.isEmpty()) {
            return this.g.poll();
        }
        return -1;
    }

    private void a(int n) {
        if (this.i[n] < 8) {
            int n2 = this.h[n] & 0xFF;
            this.g.offer(n2);
            return;
        }
        int n3 = this.i[n] / 8;
        --n3;
        while (n3 >= 0) {
            int n4 = this.h[n] >> (n3 << 3) & 0xFF;
            this.g.offer(n4);
            --n3;
        }
    }

    public static enum Interleaving {
        SAMPLE,
        ROW,
        WITHOUT_ALPHA,
        ALPHA_ONLY;

    }
}

