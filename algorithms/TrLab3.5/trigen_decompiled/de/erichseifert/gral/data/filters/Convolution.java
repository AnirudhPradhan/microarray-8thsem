/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data.filters;

import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.filters.Filter;
import de.erichseifert.gral.data.filters.Kernel;
import de.erichseifert.gral.util.DataUtils;
import de.erichseifert.gral.util.MathUtils;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Convolution
extends Filter {
    private static final long serialVersionUID = 7155205321415314271L;
    private final Kernel a;

    public Convolution(DataSource dataSource, Kernel kernel, Filter.Mode mode, int ... nArray) {
        super(dataSource, mode, nArray);
        this.a = kernel;
        this.filter();
    }

    public Kernel getKernel() {
        return this.a;
    }

    @Override
    protected void filter() {
        this.clear();
        for (int i = 0; i < this.getRowCount(); ++i) {
            Double[] doubleArray = new Double[this.getColumnCountFiltered()];
            for (int j = 0; j < doubleArray.length; ++j) {
                double d;
                block6: {
                    int n = this.getIndexOriginal(j);
                    int n2 = i;
                    int n3 = n;
                    Convolution convolution = this;
                    Kernel kernel = convolution.getKernel();
                    if (kernel == null) {
                        Comparable<?> comparable = convolution.getOriginal(n3, n2);
                        d = DataUtils.getValueOrDefault((Number)((Object)comparable), Double.NaN);
                    } else {
                        double d2 = 0.0;
                        for (int k = kernel.getMinIndex(); k <= kernel.getMaxIndex(); ++k) {
                            int n4 = n2 + k;
                            Comparable<?> comparable = convolution.getOriginal(n3, n4);
                            double d3 = DataUtils.getValueOrDefault((Number)((Object)comparable), Double.NaN);
                            if (!MathUtils.isCalculatable(d3)) {
                                d = d3;
                                break block6;
                            }
                            d2 += kernel.get(k) * d3;
                        }
                        d = d2;
                    }
                }
                doubleArray[j] = d;
            }
            this.add(doubleArray);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.dataUpdated(this, new DataChangeEvent[0]);
    }
}

