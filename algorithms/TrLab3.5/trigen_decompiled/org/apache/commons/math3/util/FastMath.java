/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.util;

import java.io.PrintStream;
import org.apache.commons.math3.util.FastMathCalc;
import org.apache.commons.math3.util.FastMathLiteralArrays;
import org.apache.commons.math3.util.Precision;

public class FastMath {
    public static final double PI = Math.PI;
    public static final double E = Math.E;
    static final int EXP_INT_TABLE_MAX_INDEX = 750;
    static final int EXP_INT_TABLE_LEN = 1500;
    static final int LN_MANT_LEN = 1024;
    static final int EXP_FRAC_TABLE_LEN = 1025;
    private static final double LOG_MAX_VALUE = StrictMath.log(Double.MAX_VALUE);
    private static final boolean RECOMPUTE_TABLES_AT_RUNTIME = false;
    private static final double LN_2_A = 0.6931470632553101;
    private static final double LN_2_B = 1.1730463525082348E-7;
    private static final double[][] LN_QUICK_COEF = new double[][]{{1.0, 5.669184079525E-24}, {-0.25, -0.25}, {0.3333333134651184, 1.986821492305628E-8}, {-0.25, -6.663542893624021E-14}, {0.19999998807907104, 1.1921056801463227E-8}, {-0.1666666567325592, -7.800414592973399E-9}, {0.1428571343421936, 5.650007086920087E-9}, {-0.1250253f, -7.44321345601866E-11}, {0.11113807559013367, 9.219544613762692E-9}};
    private static final double[][] LN_HI_PREC_COEF = new double[][]{{1.0, -6.032174644509064E-23}, {-0.25, -0.25}, {0.3333333134651184, 1.9868161777724352E-8}, {-0.2499999701976776, -2.957007209750105E-8}, {0.19999954104423523, 1.5830993332061267E-10}, {-0.1662488f, -2.6033824355191673E-8}};
    private static final int SINE_TABLE_LEN = 14;
    private static final double[] SINE_TABLE_A = new double[]{0.0, 0.1246747374534607, 0.24740394949913025, 0.366272509098053, 0.4794255495071411, 0.5850973129272461, 0.6816387176513672, 0.7675435543060303, 0.8414709568023682, 0.902267575263977, 0.9489846229553223, 0.980893f, 0.9974949359893799, 0.9985313415527344};
    private static final double[] SINE_TABLE_B = new double[]{0.0, -4.068233003401932E-9, 9.755392680573412E-9, 1.9987994582857286E-8, -1.0902938113007961E-8, -3.9986783938944604E-8, 4.23719669792332E-8, -5.207000323380292E-8, 2.800552834259E-8, 1.883511811213715E-8, -3.5997360512765566E-9, 4.116164446561962E-8, 5.0614674548127384E-8, -1.0129027912496858E-9};
    private static final double[] COSINE_TABLE_A = new double[]{1.0, 0.9921976327896118, 0.9689123630523682, 0.9305076599121094, 0.8775825500488281, 0.8109631538391113, 0.7316888570785522, 0.6409968137741089, 0.5403022766113281, 0.4311765432357788, 0.3153223395347595, 0.19454771280288696, 0.0707372f, -0.05417713522911072};
    private static final double[] COSINE_TABLE_B = new double[]{0.0, 3.4439717236742845E-8, 5.865827662008209E-8, -3.7999795083850525E-8, 1.184154459111628E-8, -3.43338934259355E-8, 1.1795268640216787E-8, 4.438921624363781E-8, 2.925681159240093E-8, -2.6437112632041807E-8, 2.2860509143963117E-8, -4.813899778443457E-9, 3.6725170580355583E-9, 2.0217439756338078E-10};
    private static final double[] TANGENT_TABLE_A = new double[]{0.0, 0.1256551444530487, 0.25534194707870483, 0.3936265707015991, 0.5463024377822876, 0.7214844226837158, 0.9315965175628662, 1.1974215507507324, 1.5574076175689697, 2.092571258544922, 3.0095696449279785, 5.041914939880371, 14.101419448852539, -18.430862426757812};
    private static final double[] TANGENT_TABLE_B = new double[]{0.0, -7.877917738262007E-9, -2.5857668567479893E-8, 5.2240336371356666E-9, 5.206150291559893E-8, 1.8307188599677033E-8, -5.7618793749770706E-8, 7.848361555046424E-8, 1.0708593250394448E-7, 1.7827257129423813E-8, 2.893485277253286E-8, 3.1660099222737955E-7, 4.983191803254889E-7, -3.356118100840571E-7};
    private static final long[] RECIP_2PI = new long[]{2935890503282001226L, 9154082963658192752L, 3952090531849364496L, 9193070505571053912L, 7910884519577875640L, 113236205062349959L, 4577762542105553359L, -5034868814120038111L, 4208363204685324176L, 5648769086999809661L, 2819561105158720014L, -4035746434778044925L, -302932621132653753L, -2644281811660520851L, -3183605296591799669L, 6722166367014452318L, -3512299194304650054L, -7278142539171889152L};
    private static final long[] PI_O_4_BITS = new long[]{-3958705157555305932L, -4267615245585081135L};
    private static final double[] EIGHTHS = new double[]{0.0, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0, 1.125, 1.25, 1.375, 1.5, 1.625};
    private static final double[] CBRTTWO = new double[]{0.6299605249474366, 0.7937005259840998, 1.0, 1.2599210498948732, 1.5874010519681994};
    private static final long HEX_40000000 = 0x40000000L;
    private static final long MASK_30BITS = -1073741824L;
    private static final int MASK_NON_SIGN_INT = Integer.MAX_VALUE;
    private static final long MASK_NON_SIGN_LONG = Long.MAX_VALUE;
    private static final double TWO_POWER_52 = 4.503599627370496E15;
    private static final double TWO_POWER_53 = 9.007199254740992E15;
    private static final double F_1_3 = 0.3333333333333333;
    private static final double F_1_5 = 0.2;
    private static final double F_1_7 = 0.14285714285714285;
    private static final double F_1_9 = 0.1111111111111111;
    private static final double F_1_11 = 0.09090909090909091;
    private static final double F_1_13 = 0.07692307692307693;
    private static final double F_1_15 = 0.06666666666666667;
    private static final double F_1_17 = 0.058823529411764705;
    private static final double F_3_4 = 0.75;
    private static final double F_15_16 = 0.9375;
    private static final double F_13_14 = 0.9285714285714286;
    private static final double F_11_12 = 0.9166666666666666;
    private static final double F_9_10 = 0.9;
    private static final double F_7_8 = 0.875;
    private static final double F_5_6 = 0.8333333333333334;
    private static final double F_1_2 = 0.5;
    private static final double F_1_4 = 0.25;

    private FastMath() {
    }

    private static double doubleHighPart(double d) {
        if (d > -Precision.SAFE_MIN && d < Precision.SAFE_MIN) {
            return d;
        }
        long xl = Double.doubleToRawLongBits(d);
        return Double.longBitsToDouble(xl &= 0xFFFFFFFFC0000000L);
    }

    public static double sqrt(double a2) {
        return Math.sqrt(a2);
    }

    public static double cosh(double x) {
        if (x != x) {
            return x;
        }
        if (x > 20.0) {
            if (x >= LOG_MAX_VALUE) {
                double t = FastMath.exp(0.5 * x);
                return 0.5 * t * t;
            }
            return 0.5 * FastMath.exp(x);
        }
        if (x < -20.0) {
            if (x <= -LOG_MAX_VALUE) {
                double t = FastMath.exp(-0.5 * x);
                return 0.5 * t * t;
            }
            return 0.5 * FastMath.exp(-x);
        }
        double[] hiPrec = new double[2];
        if (x < 0.0) {
            x = -x;
        }
        FastMath.exp(x, 0.0, hiPrec);
        double ya = hiPrec[0] + hiPrec[1];
        double yb = -(ya - hiPrec[0] - hiPrec[1]);
        double temp = ya * 1.073741824E9;
        double yaa = ya + temp - temp;
        double yab = ya - yaa;
        double recip = 1.0 / ya;
        temp = recip * 1.073741824E9;
        double recipa = recip + temp - temp;
        double recipb = recip - recipa;
        recipb += (1.0 - yaa * recipa - yaa * recipb - yab * recipa - yab * recipb) * recip;
        recipb += -yb * recip * recip;
        temp = ya + recipa;
        yb += -(temp - ya - recipa);
        ya = temp;
        temp = ya + recipb;
        yb += -(temp - ya - recipb);
        ya = temp;
        double result = ya + yb;
        return result *= 0.5;
    }

    public static double sinh(double x) {
        double result;
        boolean negate = false;
        if (x != x) {
            return x;
        }
        if (x > 20.0) {
            if (x >= LOG_MAX_VALUE) {
                double t = FastMath.exp(0.5 * x);
                return 0.5 * t * t;
            }
            return 0.5 * FastMath.exp(x);
        }
        if (x < -20.0) {
            if (x <= -LOG_MAX_VALUE) {
                double t = FastMath.exp(-0.5 * x);
                return -0.5 * t * t;
            }
            return -0.5 * FastMath.exp(-x);
        }
        if (x == 0.0) {
            return x;
        }
        if (x < 0.0) {
            x = -x;
            negate = true;
        }
        if (x > 0.25) {
            double[] hiPrec = new double[2];
            FastMath.exp(x, 0.0, hiPrec);
            double ya = hiPrec[0] + hiPrec[1];
            double yb = -(ya - hiPrec[0] - hiPrec[1]);
            double temp = ya * 1.073741824E9;
            double yaa = ya + temp - temp;
            double yab = ya - yaa;
            double recip = 1.0 / ya;
            temp = recip * 1.073741824E9;
            double recipa = recip + temp - temp;
            double recipb = recip - recipa;
            recipb += (1.0 - yaa * recipa - yaa * recipb - yab * recipa - yab * recipb) * recip;
            recipb += -yb * recip * recip;
            recipa = -recipa;
            recipb = -recipb;
            temp = ya + recipa;
            yb += -(temp - ya - recipa);
            ya = temp;
            temp = ya + recipb;
            yb += -(temp - ya - recipb);
            ya = temp;
            result = ya + yb;
            result *= 0.5;
        } else {
            double[] hiPrec = new double[2];
            FastMath.expm1(x, hiPrec);
            double ya = hiPrec[0] + hiPrec[1];
            double yb = -(ya - hiPrec[0] - hiPrec[1]);
            double denom = 1.0 + ya;
            double denomr = 1.0 / denom;
            double denomb = -(denom - 1.0 - ya) + yb;
            double ratio = ya * denomr;
            double temp = ratio * 1.073741824E9;
            double ra = ratio + temp - temp;
            double rb = ratio - ra;
            temp = denom * 1.073741824E9;
            double za = denom + temp - temp;
            double zb = denom - za;
            rb += (ya - za * ra - za * rb - zb * ra - zb * rb) * denomr;
            rb += yb * denomr;
            rb += -ya * denomb * denomr * denomr;
            temp = ya + ra;
            yb += -(temp - ya - ra);
            ya = temp;
            temp = ya + rb;
            yb += -(temp - ya - rb);
            ya = temp;
            result = ya + yb;
            result *= 0.5;
        }
        if (negate) {
            result = -result;
        }
        return result;
    }

    public static double tanh(double x) {
        double result;
        boolean negate = false;
        if (x != x) {
            return x;
        }
        if (x > 20.0) {
            return 1.0;
        }
        if (x < -20.0) {
            return -1.0;
        }
        if (x == 0.0) {
            return x;
        }
        if (x < 0.0) {
            x = -x;
            negate = true;
        }
        if (x >= 0.5) {
            double[] hiPrec = new double[2];
            FastMath.exp(x * 2.0, 0.0, hiPrec);
            double ya = hiPrec[0] + hiPrec[1];
            double yb = -(ya - hiPrec[0] - hiPrec[1]);
            double na = -1.0 + ya;
            double nb = -(na + 1.0 - ya);
            double temp = na + yb;
            nb += -(temp - na - yb);
            na = temp;
            double da = 1.0 + ya;
            double db = -(da - 1.0 - ya);
            temp = da + yb;
            db += -(temp - da - yb);
            da = temp;
            temp = da * 1.073741824E9;
            double daa = da + temp - temp;
            double dab = da - daa;
            double ratio = na / da;
            temp = ratio * 1.073741824E9;
            double ratioa = ratio + temp - temp;
            double ratiob = ratio - ratioa;
            ratiob += (na - daa * ratioa - daa * ratiob - dab * ratioa - dab * ratiob) / da;
            ratiob += nb / da;
            result = ratioa + (ratiob += -db * na / da / da);
        } else {
            double[] hiPrec = new double[2];
            FastMath.expm1(x * 2.0, hiPrec);
            double ya = hiPrec[0] + hiPrec[1];
            double yb = -(ya - hiPrec[0] - hiPrec[1]);
            double na = ya;
            double nb = yb;
            double da = 2.0 + ya;
            double db = -(da - 2.0 - ya);
            double temp = da + yb;
            db += -(temp - da - yb);
            da = temp;
            temp = da * 1.073741824E9;
            double daa = da + temp - temp;
            double dab = da - daa;
            double ratio = na / da;
            temp = ratio * 1.073741824E9;
            double ratioa = ratio + temp - temp;
            double ratiob = ratio - ratioa;
            ratiob += (na - daa * ratioa - daa * ratiob - dab * ratioa - dab * ratiob) / da;
            ratiob += nb / da;
            result = ratioa + (ratiob += -db * na / da / da);
        }
        if (negate) {
            result = -result;
        }
        return result;
    }

    public static double acosh(double a2) {
        return FastMath.log(a2 + FastMath.sqrt(a2 * a2 - 1.0));
    }

    public static double asinh(double a2) {
        double absAsinh;
        boolean negative = false;
        if (a2 < 0.0) {
            negative = true;
            a2 = -a2;
        }
        if (a2 > 0.167) {
            absAsinh = FastMath.log(FastMath.sqrt(a2 * a2 + 1.0) + a2);
        } else {
            double a22 = a2 * a2;
            absAsinh = a2 > 0.097 ? a2 * (1.0 - a22 * (0.3333333333333333 - a22 * (0.2 - a22 * (0.14285714285714285 - a22 * (0.1111111111111111 - a22 * (0.09090909090909091 - a22 * (0.07692307692307693 - a22 * (0.06666666666666667 - a22 * 0.058823529411764705 * 0.9375) * 0.9285714285714286) * 0.9166666666666666) * 0.9) * 0.875) * 0.8333333333333334) * 0.75) * 0.5) : (a2 > 0.036 ? a2 * (1.0 - a22 * (0.3333333333333333 - a22 * (0.2 - a22 * (0.14285714285714285 - a22 * (0.1111111111111111 - a22 * (0.09090909090909091 - a22 * 0.07692307692307693 * 0.9166666666666666) * 0.9) * 0.875) * 0.8333333333333334) * 0.75) * 0.5) : (a2 > 0.0036 ? a2 * (1.0 - a22 * (0.3333333333333333 - a22 * (0.2 - a22 * (0.14285714285714285 - a22 * 0.1111111111111111 * 0.875) * 0.8333333333333334) * 0.75) * 0.5) : a2 * (1.0 - a22 * (0.3333333333333333 - a22 * 0.2 * 0.75) * 0.5)));
        }
        return negative ? -absAsinh : absAsinh;
    }

    public static double atanh(double a2) {
        double absAtanh;
        boolean negative = false;
        if (a2 < 0.0) {
            negative = true;
            a2 = -a2;
        }
        if (a2 > 0.15) {
            absAtanh = 0.5 * FastMath.log((1.0 + a2) / (1.0 - a2));
        } else {
            double a22 = a2 * a2;
            absAtanh = a2 > 0.087 ? a2 * (1.0 + a22 * (0.3333333333333333 + a22 * (0.2 + a22 * (0.14285714285714285 + a22 * (0.1111111111111111 + a22 * (0.09090909090909091 + a22 * (0.07692307692307693 + a22 * (0.06666666666666667 + a22 * 0.058823529411764705)))))))) : (a2 > 0.031 ? a2 * (1.0 + a22 * (0.3333333333333333 + a22 * (0.2 + a22 * (0.14285714285714285 + a22 * (0.1111111111111111 + a22 * (0.09090909090909091 + a22 * 0.07692307692307693)))))) : (a2 > 0.003 ? a2 * (1.0 + a22 * (0.3333333333333333 + a22 * (0.2 + a22 * (0.14285714285714285 + a22 * 0.1111111111111111)))) : a2 * (1.0 + a22 * (0.3333333333333333 + a22 * 0.2))));
        }
        return negative ? -absAtanh : absAtanh;
    }

    public static double signum(double a2) {
        return a2 < 0.0 ? -1.0 : (a2 > 0.0 ? 1.0 : a2);
    }

    public static float signum(float a2) {
        return a2 < 0.0f ? -1.0f : (a2 > 0.0f ? 1.0f : a2);
    }

    public static double nextUp(double a2) {
        return FastMath.nextAfter(a2, Double.POSITIVE_INFINITY);
    }

    public static float nextUp(float a2) {
        return FastMath.nextAfter(a2, Double.POSITIVE_INFINITY);
    }

    public static double random() {
        return Math.random();
    }

    public static double exp(double x) {
        return FastMath.exp(x, 0.0, null);
    }

    private static double exp(double x, double extra, double[] hiPrec) {
        double intPartB;
        double intPartA;
        int intVal;
        if (x < 0.0) {
            intVal = (int)(-x);
            if (intVal > 746) {
                if (hiPrec != null) {
                    hiPrec[0] = 0.0;
                    hiPrec[1] = 0.0;
                }
                return 0.0;
            }
            if (intVal > 709) {
                double result = FastMath.exp(x + 40.19140625, extra, hiPrec) / 2.8504009514401178E17;
                if (hiPrec != null) {
                    hiPrec[0] = hiPrec[0] / 2.8504009514401178E17;
                    hiPrec[1] = hiPrec[1] / 2.8504009514401178E17;
                }
                return result;
            }
            if (intVal == 709) {
                double result = FastMath.exp(x + 1.494140625, extra, hiPrec) / 4.455505956692757;
                if (hiPrec != null) {
                    hiPrec[0] = hiPrec[0] / 4.455505956692757;
                    hiPrec[1] = hiPrec[1] / 4.455505956692757;
                }
                return result;
            }
            intPartA = ExpIntTable.EXP_INT_TABLE_A[750 - ++intVal];
            intPartB = ExpIntTable.EXP_INT_TABLE_B[750 - intVal];
            intVal = -intVal;
        } else {
            intVal = (int)x;
            if (intVal > 709) {
                if (hiPrec != null) {
                    hiPrec[0] = Double.POSITIVE_INFINITY;
                    hiPrec[1] = 0.0;
                }
                return Double.POSITIVE_INFINITY;
            }
            intPartA = ExpIntTable.EXP_INT_TABLE_A[750 + intVal];
            intPartB = ExpIntTable.EXP_INT_TABLE_B[750 + intVal];
        }
        int intFrac = (int)((x - (double)intVal) * 1024.0);
        double fracPartA = ExpFracTable.EXP_FRAC_TABLE_A[intFrac];
        double fracPartB = ExpFracTable.EXP_FRAC_TABLE_B[intFrac];
        double epsilon = x - ((double)intVal + (double)intFrac / 1024.0);
        double z = 0.04168701738764507;
        z = z * epsilon + 0.1666666505023083;
        z = z * epsilon + 0.5000000000042687;
        z = z * epsilon + 1.0;
        z = z * epsilon + -3.940510424527919E-20;
        double tempA = intPartA * fracPartA;
        double tempB = intPartA * fracPartB + intPartB * fracPartA + intPartB * fracPartB;
        double tempC = tempB + tempA;
        double result = extra != 0.0 ? tempC * extra * z + tempC * extra + tempC * z + tempB + tempA : tempC * z + tempB + tempA;
        if (hiPrec != null) {
            hiPrec[0] = tempA;
            hiPrec[1] = tempC * extra * z + tempC * extra + tempC * z + tempB;
        }
        return result;
    }

    public static double expm1(double x) {
        return FastMath.expm1(x, null);
    }

    private static double expm1(double x, double[] hiPrecOut) {
        if (x != x || x == 0.0) {
            return x;
        }
        if (x <= -1.0 || x >= 1.0) {
            double[] hiPrec = new double[2];
            FastMath.exp(x, 0.0, hiPrec);
            if (x > 0.0) {
                return -1.0 + hiPrec[0] + hiPrec[1];
            }
            double ra = -1.0 + hiPrec[0];
            double rb = -(ra + 1.0 - hiPrec[0]);
            return ra + (rb += hiPrec[1]);
        }
        boolean negative = false;
        if (x < 0.0) {
            x = -x;
            negative = true;
        }
        int intFrac = (int)(x * 1024.0);
        double tempA = ExpFracTable.EXP_FRAC_TABLE_A[intFrac] - 1.0;
        double tempB = ExpFracTable.EXP_FRAC_TABLE_B[intFrac];
        double temp = tempA + tempB;
        tempB = -(temp - tempA - tempB);
        tempA = temp;
        temp = tempA * 1.073741824E9;
        double baseA = tempA + temp - temp;
        double baseB = tempB + (tempA - baseA);
        double epsilon = x - (double)intFrac / 1024.0;
        double zb = 0.008336750013465571;
        zb = zb * epsilon + 0.041666663879186654;
        zb = zb * epsilon + 0.16666666666745392;
        zb = zb * epsilon + 0.49999999999999994;
        zb *= epsilon;
        double za = epsilon;
        double temp2 = za + (zb *= epsilon);
        zb = -(temp2 - za - zb);
        za = temp2;
        temp2 = za * 1.073741824E9;
        temp2 = za + temp2 - temp2;
        zb += za - temp2;
        za = temp2;
        double ya = za * baseA;
        temp2 = ya + za * baseB;
        double yb = -(temp2 - ya - za * baseB);
        ya = temp2;
        temp2 = ya + zb * baseA;
        yb += -(temp2 - ya - zb * baseA);
        ya = temp2;
        temp2 = ya + zb * baseB;
        yb += -(temp2 - ya - zb * baseB);
        ya = temp2;
        temp2 = ya + baseA;
        yb += -(temp2 - baseA - ya);
        ya = temp2;
        temp2 = ya + za;
        yb += -(temp2 - ya - za);
        ya = temp2;
        temp2 = ya + baseB;
        yb += -(temp2 - ya - baseB);
        ya = temp2;
        temp2 = ya + zb;
        yb += -(temp2 - ya - zb);
        ya = temp2;
        if (negative) {
            double denom = 1.0 + ya;
            double denomr = 1.0 / denom;
            double denomb = -(denom - 1.0 - ya) + yb;
            double ratio = ya * denomr;
            temp2 = ratio * 1.073741824E9;
            double ra = ratio + temp2 - temp2;
            double rb = ratio - ra;
            temp2 = denom * 1.073741824E9;
            za = denom + temp2 - temp2;
            zb = denom - za;
            rb += (ya - za * ra - za * rb - zb * ra - zb * rb) * denomr;
            rb += yb * denomr;
            rb += -ya * denomb * denomr * denomr;
            ya = -ra;
            yb = -rb;
        }
        if (hiPrecOut != null) {
            hiPrecOut[0] = ya;
            hiPrecOut[1] = yb;
        }
        return ya + yb;
    }

    public static double log(double x) {
        return FastMath.log(x, null);
    }

    private static double log(double x, double[] hiPrec) {
        if (x == 0.0) {
            return Double.NEGATIVE_INFINITY;
        }
        long bits = Double.doubleToRawLongBits(x);
        if (((bits & Long.MIN_VALUE) != 0L || x != x) && x != 0.0) {
            if (hiPrec != null) {
                hiPrec[0] = Double.NaN;
            }
            return Double.NaN;
        }
        if (x == Double.POSITIVE_INFINITY) {
            if (hiPrec != null) {
                hiPrec[0] = Double.POSITIVE_INFINITY;
            }
            return Double.POSITIVE_INFINITY;
        }
        int exp = (int)(bits >> 52) - 1023;
        if ((bits & 0x7FF0000000000000L) == 0L) {
            if (x == 0.0) {
                if (hiPrec != null) {
                    hiPrec[0] = Double.NEGATIVE_INFINITY;
                }
                return Double.NEGATIVE_INFINITY;
            }
            bits <<= 1;
            while ((bits & 0x10000000000000L) == 0L) {
                --exp;
                bits <<= 1;
            }
        }
        if ((exp == -1 || exp == 0) && x < 1.01 && x > 0.99 && hiPrec == null) {
            double xa = x - 1.0;
            double xb = xa - x + 1.0;
            double tmp = xa * 1.073741824E9;
            double aa = xa + tmp - tmp;
            double ab = xa - aa;
            xa = aa;
            xb = ab;
            double[] lnCoef_last = LN_QUICK_COEF[LN_QUICK_COEF.length - 1];
            double ya = lnCoef_last[0];
            double yb = lnCoef_last[1];
            for (int i = LN_QUICK_COEF.length - 2; i >= 0; --i) {
                aa = ya * xa;
                ab = ya * xb + yb * xa + yb * xb;
                tmp = aa * 1.073741824E9;
                ya = aa + tmp - tmp;
                yb = aa - ya + ab;
                double[] lnCoef_i = LN_QUICK_COEF[i];
                aa = ya + lnCoef_i[0];
                ab = yb + lnCoef_i[1];
                tmp = aa * 1.073741824E9;
                ya = aa + tmp - tmp;
                yb = aa - ya + ab;
            }
            aa = ya * xa;
            ab = ya * xb + yb * xa + yb * xb;
            tmp = aa * 1.073741824E9;
            ya = aa + tmp - tmp;
            yb = aa - ya + ab;
            return ya + yb;
        }
        double[] lnm = lnMant.LN_MANT[(int)((bits & 0xFFC0000000000L) >> 42)];
        double epsilon = (double)(bits & 0x3FFFFFFFFFFL) / (4.503599627370496E15 + (double)(bits & 0xFFC0000000000L));
        double lnza = 0.0;
        double lnzb = 0.0;
        if (hiPrec != null) {
            double tmp = epsilon * 1.073741824E9;
            double aa = epsilon + tmp - tmp;
            double ab = epsilon - aa;
            double xa = aa;
            double xb = ab;
            double numer = bits & 0x3FFFFFFFFFFL;
            double denom = 4.503599627370496E15 + (double)(bits & 0xFFC0000000000L);
            aa = numer - xa * denom - xb * denom;
            xb += aa / denom;
            double[] lnCoef_last = LN_HI_PREC_COEF[LN_HI_PREC_COEF.length - 1];
            double ya = lnCoef_last[0];
            double yb = lnCoef_last[1];
            for (int i = LN_HI_PREC_COEF.length - 2; i >= 0; --i) {
                aa = ya * xa;
                ab = ya * xb + yb * xa + yb * xb;
                tmp = aa * 1.073741824E9;
                ya = aa + tmp - tmp;
                yb = aa - ya + ab;
                double[] lnCoef_i = LN_HI_PREC_COEF[i];
                aa = ya + lnCoef_i[0];
                ab = yb + lnCoef_i[1];
                tmp = aa * 1.073741824E9;
                ya = aa + tmp - tmp;
                yb = aa - ya + ab;
            }
            aa = ya * xa;
            ab = ya * xb + yb * xa + yb * xb;
            lnza = aa + ab;
            lnzb = -(lnza - aa - ab);
        } else {
            lnza = -0.16624882440418567;
            lnza = lnza * epsilon + 0.19999954120254515;
            lnza = lnza * epsilon + -0.2499999997677497;
            lnza = lnza * epsilon + 0.3333333333332802;
            lnza = lnza * epsilon + -0.5;
            lnza = lnza * epsilon + 1.0;
            lnza *= epsilon;
        }
        double a2 = 0.6931470632553101 * (double)exp;
        double b2 = 0.0;
        double c2 = a2 + lnm[0];
        double d = -(c2 - a2 - lnm[0]);
        a2 = c2;
        b2 += d;
        c2 = a2 + lnza;
        d = -(c2 - a2 - lnza);
        a2 = c2;
        b2 += d;
        c2 = a2 + 1.1730463525082348E-7 * (double)exp;
        d = -(c2 - a2 - 1.1730463525082348E-7 * (double)exp);
        a2 = c2;
        b2 += d;
        c2 = a2 + lnm[1];
        d = -(c2 - a2 - lnm[1]);
        a2 = c2;
        b2 += d;
        c2 = a2 + lnzb;
        d = -(c2 - a2 - lnzb);
        a2 = c2;
        b2 += d;
        if (hiPrec != null) {
            hiPrec[0] = a2;
            hiPrec[1] = b2;
        }
        return a2 + b2;
    }

    public static double log1p(double x) {
        if (x == -1.0) {
            return Double.NEGATIVE_INFINITY;
        }
        if (x == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }
        if (x > 1.0E-6 || x < -1.0E-6) {
            double xpa = 1.0 + x;
            double xpb = -(xpa - 1.0 - x);
            double[] hiPrec = new double[2];
            double lores = FastMath.log(xpa, hiPrec);
            if (Double.isInfinite(lores)) {
                return lores;
            }
            double fx1 = xpb / xpa;
            double epsilon = 0.5 * fx1 + 1.0;
            return epsilon * fx1 + hiPrec[1] + hiPrec[0];
        }
        double y = (x * 0.3333333333333333 - 0.5) * x + 1.0;
        return y * x;
    }

    public static double log10(double x) {
        double[] hiPrec = new double[2];
        double lores = FastMath.log(x, hiPrec);
        if (Double.isInfinite(lores)) {
            return lores;
        }
        double tmp = hiPrec[0] * 1.073741824E9;
        double lna = hiPrec[0] + tmp - tmp;
        double lnb = hiPrec[0] - lna + hiPrec[1];
        double rln10a = 0.4342944622039795;
        double rln10b = 1.9699272335463627E-8;
        return 1.9699272335463627E-8 * lnb + 1.9699272335463627E-8 * lna + 0.4342944622039795 * lnb + 0.4342944622039795 * lna;
    }

    public static double log(double base, double x) {
        return FastMath.log(x) / FastMath.log(base);
    }

    public static double pow(double x, double y) {
        double yb;
        double ya;
        double tmp1;
        double[] lns = new double[2];
        if (y == 0.0) {
            return 1.0;
        }
        if (x != x) {
            return x;
        }
        if (x == 0.0) {
            long bits = Double.doubleToRawLongBits(x);
            if ((bits & Long.MIN_VALUE) != 0L) {
                long yi = (long)y;
                if (y < 0.0 && y == (double)yi && (yi & 1L) == 1L) {
                    return Double.NEGATIVE_INFINITY;
                }
                if (y > 0.0 && y == (double)yi && (yi & 1L) == 1L) {
                    return -0.0;
                }
            }
            if (y < 0.0) {
                return Double.POSITIVE_INFINITY;
            }
            if (y > 0.0) {
                return 0.0;
            }
            return Double.NaN;
        }
        if (x == Double.POSITIVE_INFINITY) {
            if (y != y) {
                return y;
            }
            if (y < 0.0) {
                return 0.0;
            }
            return Double.POSITIVE_INFINITY;
        }
        if (y == Double.POSITIVE_INFINITY) {
            if (x * x == 1.0) {
                return Double.NaN;
            }
            if (x * x > 1.0) {
                return Double.POSITIVE_INFINITY;
            }
            return 0.0;
        }
        if (x == Double.NEGATIVE_INFINITY) {
            if (y != y) {
                return y;
            }
            if (y < 0.0) {
                long yi = (long)y;
                if (y == (double)yi && (yi & 1L) == 1L) {
                    return -0.0;
                }
                return 0.0;
            }
            if (y > 0.0) {
                long yi = (long)y;
                if (y == (double)yi && (yi & 1L) == 1L) {
                    return Double.NEGATIVE_INFINITY;
                }
                return Double.POSITIVE_INFINITY;
            }
        }
        if (y == Double.NEGATIVE_INFINITY) {
            if (x * x == 1.0) {
                return Double.NaN;
            }
            if (x * x < 1.0) {
                return Double.POSITIVE_INFINITY;
            }
            return 0.0;
        }
        if (x < 0.0) {
            if (y >= 9.007199254740992E15 || y <= -9.007199254740992E15) {
                return FastMath.pow(-x, y);
            }
            if (y == (double)((long)y)) {
                return ((long)y & 1L) == 0L ? FastMath.pow(-x, y) : -FastMath.pow(-x, y);
            }
            return Double.NaN;
        }
        if (y < 8.0E298 && y > -8.0E298) {
            tmp1 = y * 1.073741824E9;
            ya = y + tmp1 - tmp1;
            yb = y - ya;
        } else {
            tmp1 = y * 9.313225746154785E-10;
            double tmp2 = tmp1 * 9.313225746154785E-10;
            ya = (tmp1 + tmp2 - tmp1) * 1.073741824E9 * 1.073741824E9;
            yb = y - ya;
        }
        double lores = FastMath.log(x, lns);
        if (Double.isInfinite(lores)) {
            return lores;
        }
        double lna = lns[0];
        double lnb = lns[1];
        double tmp12 = lna * 1.073741824E9;
        double tmp2 = lna + tmp12 - tmp12;
        lnb += lna - tmp2;
        lna = tmp2;
        double aa = lna * ya;
        double ab = lna * yb + lnb * ya + lnb * yb;
        lna = aa + ab;
        lnb = -(lna - aa - ab);
        double z = 0.008333333333333333;
        z = z * lnb + 0.041666666666666664;
        z = z * lnb + 0.16666666666666666;
        z = z * lnb + 0.5;
        z = z * lnb + 1.0;
        double result = FastMath.exp(lna, z *= lnb, null);
        return result;
    }

    public static double pow(double d, int e) {
        if (e == 0) {
            return 1.0;
        }
        if (e < 0) {
            e = -e;
            d = 1.0 / d;
        }
        int splitFactor = 0x8000001;
        double cd = 1.34217729E8 * d;
        double d1High = cd - (cd - d);
        double d1Low = d - d1High;
        double resultHigh = 1.0;
        double resultLow = 0.0;
        double d2p = d;
        double d2pHigh = d1High;
        double d2pLow = d1Low;
        while (e != 0) {
            double tmpLow;
            double tmpHigh;
            if ((e & 1) != 0) {
                tmpHigh = resultHigh * d2p;
                double cRH = 1.34217729E8 * resultHigh;
                double rHH = cRH - (cRH - resultHigh);
                double rHL = resultHigh - rHH;
                tmpLow = rHL * d2pLow - (tmpHigh - rHH * d2pHigh - rHL * d2pHigh - rHH * d2pLow);
                resultHigh = tmpHigh;
                resultLow = resultLow * d2p + tmpLow;
            }
            tmpHigh = d2pHigh * d2p;
            double cD2pH = 1.34217729E8 * d2pHigh;
            double d2pHH = cD2pH - (cD2pH - d2pHigh);
            double d2pHL = d2pHigh - d2pHH;
            tmpLow = d2pHL * d2pLow - (tmpHigh - d2pHH * d2pHigh - d2pHL * d2pHigh - d2pHH * d2pLow);
            double cTmpH = 1.34217729E8 * tmpHigh;
            d2pHigh = cTmpH - (cTmpH - tmpHigh);
            d2pLow = d2pLow * d2p + tmpLow + (tmpHigh - d2pHigh);
            d2p = d2pHigh + d2pLow;
            e >>= 1;
        }
        return resultHigh + resultLow;
    }

    private static double polySine(double x) {
        double x2 = x * x;
        double p = 2.7553817452272217E-6;
        p = p * x2 + -1.9841269659586505E-4;
        p = p * x2 + 0.008333333333329196;
        p = p * x2 + -0.16666666666666666;
        p = p * x2 * x;
        return p;
    }

    private static double polyCosine(double x) {
        double x2 = x * x;
        double p = 2.479773539153719E-5;
        p = p * x2 + -0.0013888888689039883;
        p = p * x2 + 0.041666666666621166;
        p = p * x2 + -0.49999999999999994;
        return p *= x2;
    }

    private static double sinQ(double xa, double xb) {
        int idx = (int)(xa * 8.0 + 0.5);
        double epsilon = xa - EIGHTHS[idx];
        double sintA = SINE_TABLE_A[idx];
        double sintB = SINE_TABLE_B[idx];
        double costA = COSINE_TABLE_A[idx];
        double costB = COSINE_TABLE_B[idx];
        double sinEpsA = epsilon;
        double sinEpsB = FastMath.polySine(epsilon);
        double cosEpsA = 1.0;
        double cosEpsB = FastMath.polyCosine(epsilon);
        double temp = sinEpsA * 1.073741824E9;
        double temp2 = sinEpsA + temp - temp;
        sinEpsB += sinEpsA - temp2;
        sinEpsA = temp2;
        double a2 = 0.0;
        double b2 = 0.0;
        double t = sintA;
        double c2 = a2 + t;
        double d = -(c2 - a2 - t);
        a2 = c2;
        b2 += d;
        t = costA * sinEpsA;
        c2 = a2 + t;
        d = -(c2 - a2 - t);
        a2 = c2;
        b2 += d;
        b2 = b2 + sintA * cosEpsB + costA * sinEpsB;
        b2 = b2 + sintB + costB * sinEpsA + sintB * cosEpsB + costB * sinEpsB;
        if (xb != 0.0) {
            t = ((costA + costB) * (1.0 + cosEpsB) - (sintA + sintB) * (sinEpsA + sinEpsB)) * xb;
            c2 = a2 + t;
            d = -(c2 - a2 - t);
            a2 = c2;
            b2 += d;
        }
        double result = a2 + b2;
        return result;
    }

    private static double cosQ(double xa, double xb) {
        double pi2a = 1.5707963267948966;
        double pi2b = 6.123233995736766E-17;
        double a2 = 1.5707963267948966 - xa;
        double b2 = -(a2 - 1.5707963267948966 + xa);
        return FastMath.sinQ(a2, b2 += 6.123233995736766E-17 - xb);
    }

    private static double tanQ(double xa, double xb, boolean cotanFlag) {
        int idx = (int)(xa * 8.0 + 0.5);
        double epsilon = xa - EIGHTHS[idx];
        double sintA = SINE_TABLE_A[idx];
        double sintB = SINE_TABLE_B[idx];
        double costA = COSINE_TABLE_A[idx];
        double costB = COSINE_TABLE_B[idx];
        double sinEpsA = epsilon;
        double sinEpsB = FastMath.polySine(epsilon);
        double cosEpsA = 1.0;
        double cosEpsB = FastMath.polyCosine(epsilon);
        double temp = sinEpsA * 1.073741824E9;
        double temp2 = sinEpsA + temp - temp;
        sinEpsB += sinEpsA - temp2;
        sinEpsA = temp2;
        double a2 = 0.0;
        double b2 = 0.0;
        double t = sintA;
        double c2 = a2 + t;
        double d = -(c2 - a2 - t);
        a2 = c2;
        b2 += d;
        t = costA * sinEpsA;
        c2 = a2 + t;
        d = -(c2 - a2 - t);
        a2 = c2;
        b2 += d;
        b2 += sintA * cosEpsB + costA * sinEpsB;
        double sina = a2 + (b2 += sintB + costB * sinEpsA + sintB * cosEpsB + costB * sinEpsB);
        double sinb = -(sina - a2 - b2);
        d = 0.0;
        c2 = 0.0;
        b2 = 0.0;
        a2 = 0.0;
        t = costA * 1.0;
        c2 = a2 + t;
        d = -(c2 - a2 - t);
        a2 = c2;
        b2 += d;
        t = -sintA * sinEpsA;
        c2 = a2 + t;
        d = -(c2 - a2 - t);
        a2 = c2;
        b2 += d;
        b2 += costB * 1.0 + costA * cosEpsB + costB * cosEpsB;
        double cosa = a2 + (b2 -= sintB * sinEpsA + sintA * sinEpsB + sintB * sinEpsB);
        double cosb = -(cosa - a2 - b2);
        if (cotanFlag) {
            double tmp = cosa;
            cosa = sina;
            sina = tmp;
            tmp = cosb;
            cosb = sinb;
            sinb = tmp;
        }
        double est = sina / cosa;
        temp = est * 1.073741824E9;
        double esta = est + temp - temp;
        double estb = est - esta;
        temp = cosa * 1.073741824E9;
        double cosaa = cosa + temp - temp;
        double cosab = cosa - cosaa;
        double err = (sina - esta * cosaa - esta * cosab - estb * cosaa - estb * cosab) / cosa;
        err += sinb / cosa;
        err += -sina * cosb / cosa / cosa;
        if (xb != 0.0) {
            double xbadj = xb + est * est * xb;
            if (cotanFlag) {
                xbadj = -xbadj;
            }
            err += xbadj;
        }
        return est + err;
    }

    private static void reducePayneHanek(double x, double[] result) {
        boolean bitsum;
        long shpiB;
        long shpiA;
        long shpi0;
        int idx;
        int shift;
        long inbits = Double.doubleToRawLongBits(x);
        int exponent = (int)(inbits >> 52 & 0x7FFL) - 1023;
        inbits &= 0xFFFFFFFFFFFFFL;
        inbits |= 0x10000000000000L;
        inbits <<= 11;
        if ((shift = ++exponent - ((idx = exponent >> 6) << 6)) != 0) {
            shpi0 = idx == 0 ? 0L : RECIP_2PI[idx - 1] << shift;
            shpi0 |= RECIP_2PI[idx] >>> 64 - shift;
            shpiA = RECIP_2PI[idx] << shift | RECIP_2PI[idx + 1] >>> 64 - shift;
            shpiB = RECIP_2PI[idx + 1] << shift | RECIP_2PI[idx + 2] >>> 64 - shift;
        } else {
            shpi0 = idx == 0 ? 0L : RECIP_2PI[idx - 1];
            shpiA = RECIP_2PI[idx];
            shpiB = RECIP_2PI[idx + 1];
        }
        long a2 = inbits >>> 32;
        long b2 = inbits & 0xFFFFFFFFL;
        long c2 = shpiA >>> 32;
        long d = shpiA & 0xFFFFFFFFL;
        long ac = a2 * c2;
        long bd = b2 * d;
        long bc = b2 * c2;
        long ad = a2 * d;
        long prodB = bd + (ad << 32);
        long prodA = ac + (ad >>> 32);
        boolean bita = (bd & Long.MIN_VALUE) != 0L;
        boolean bitb = (ad & 0x80000000L) != 0L;
        boolean bl = bitsum = (prodB & Long.MIN_VALUE) != 0L;
        if (bita && bitb || (bita || bitb) && !bitsum) {
            ++prodA;
        }
        bita = (prodB & Long.MIN_VALUE) != 0L;
        bitb = (bc & 0x80000000L) != 0L;
        prodA += bc >>> 32;
        boolean bl2 = bitsum = ((prodB += bc << 32) & Long.MIN_VALUE) != 0L;
        if (bita && bitb || (bita || bitb) && !bitsum) {
            ++prodA;
        }
        c2 = shpiB >>> 32;
        d = shpiB & 0xFFFFFFFFL;
        ac = a2 * c2;
        bc = b2 * c2;
        ad = a2 * d;
        bita = (prodB & Long.MIN_VALUE) != 0L;
        bitb = ((ac += bc + ad >>> 32) & Long.MIN_VALUE) != 0L;
        boolean bl3 = bitsum = ((prodB += ac) & Long.MIN_VALUE) != 0L;
        if (bita && bitb || (bita || bitb) && !bitsum) {
            ++prodA;
        }
        c2 = shpi0 >>> 32;
        d = shpi0 & 0xFFFFFFFFL;
        bd = b2 * d;
        bc = b2 * c2;
        ad = a2 * d;
        int intPart = (int)((prodA += bd + (bc + ad << 32)) >>> 62);
        prodA <<= 2;
        prodA |= prodB >>> 62;
        prodB <<= 2;
        a2 = prodA >>> 32;
        b2 = prodA & 0xFFFFFFFFL;
        c2 = PI_O_4_BITS[0] >>> 32;
        d = PI_O_4_BITS[0] & 0xFFFFFFFFL;
        ac = a2 * c2;
        bd = b2 * d;
        bc = b2 * c2;
        ad = a2 * d;
        long prod2B = bd + (ad << 32);
        long prod2A = ac + (ad >>> 32);
        bita = (bd & Long.MIN_VALUE) != 0L;
        bitb = (ad & 0x80000000L) != 0L;
        boolean bl4 = bitsum = (prod2B & Long.MIN_VALUE) != 0L;
        if (bita && bitb || (bita || bitb) && !bitsum) {
            ++prod2A;
        }
        bita = (prod2B & Long.MIN_VALUE) != 0L;
        bitb = (bc & 0x80000000L) != 0L;
        prod2A += bc >>> 32;
        boolean bl5 = bitsum = ((prod2B += bc << 32) & Long.MIN_VALUE) != 0L;
        if (bita && bitb || (bita || bitb) && !bitsum) {
            ++prod2A;
        }
        c2 = PI_O_4_BITS[1] >>> 32;
        d = PI_O_4_BITS[1] & 0xFFFFFFFFL;
        ac = a2 * c2;
        bc = b2 * c2;
        ad = a2 * d;
        bita = (prod2B & Long.MIN_VALUE) != 0L;
        bitb = ((ac += bc + ad >>> 32) & Long.MIN_VALUE) != 0L;
        boolean bl6 = bitsum = ((prod2B += ac) & Long.MIN_VALUE) != 0L;
        if (bita && bitb || (bita || bitb) && !bitsum) {
            ++prod2A;
        }
        a2 = prodB >>> 32;
        b2 = prodB & 0xFFFFFFFFL;
        c2 = PI_O_4_BITS[0] >>> 32;
        d = PI_O_4_BITS[0] & 0xFFFFFFFFL;
        ac = a2 * c2;
        bc = b2 * c2;
        ad = a2 * d;
        bita = (prod2B & Long.MIN_VALUE) != 0L;
        bitb = ((ac += bc + ad >>> 32) & Long.MIN_VALUE) != 0L;
        boolean bl7 = bitsum = ((prod2B += ac) & Long.MIN_VALUE) != 0L;
        if (bita && bitb || (bita || bitb) && !bitsum) {
            ++prod2A;
        }
        double tmpA = (double)(prod2A >>> 12) / 4.503599627370496E15;
        double tmpB = (double)(((prod2A & 0xFFFL) << 40) + (prod2B >>> 24)) / 4.503599627370496E15 / 4.503599627370496E15;
        double sumA = tmpA + tmpB;
        double sumB = -(sumA - tmpA - tmpB);
        result[0] = intPart;
        result[1] = sumA * 2.0;
        result[2] = sumB * 2.0;
    }

    public static double sin(double x) {
        boolean negative = false;
        int quadrant = 0;
        double xb = 0.0;
        double xa = x;
        if (x < 0.0) {
            negative = true;
            xa = -xa;
        }
        if (xa == 0.0) {
            long bits = Double.doubleToRawLongBits(x);
            if (bits < 0L) {
                return -0.0;
            }
            return 0.0;
        }
        if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        if (xa > 3294198.0) {
            double[] reduceResults = new double[3];
            FastMath.reducePayneHanek(xa, reduceResults);
            quadrant = (int)reduceResults[0] & 3;
            xa = reduceResults[1];
            xb = reduceResults[2];
        } else if (xa > 1.5707963267948966) {
            CodyWaite cw = new CodyWaite(xa);
            quadrant = cw.getK() & 3;
            xa = cw.getRemA();
            xb = cw.getRemB();
        }
        if (negative) {
            quadrant ^= 2;
        }
        switch (quadrant) {
            case 0: {
                return FastMath.sinQ(xa, xb);
            }
            case 1: {
                return FastMath.cosQ(xa, xb);
            }
            case 2: {
                return -FastMath.sinQ(xa, xb);
            }
            case 3: {
                return -FastMath.cosQ(xa, xb);
            }
        }
        return Double.NaN;
    }

    public static double cos(double x) {
        int quadrant = 0;
        double xa = x;
        if (x < 0.0) {
            xa = -xa;
        }
        if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        double xb = 0.0;
        if (xa > 3294198.0) {
            double[] reduceResults = new double[3];
            FastMath.reducePayneHanek(xa, reduceResults);
            quadrant = (int)reduceResults[0] & 3;
            xa = reduceResults[1];
            xb = reduceResults[2];
        } else if (xa > 1.5707963267948966) {
            CodyWaite cw = new CodyWaite(xa);
            quadrant = cw.getK() & 3;
            xa = cw.getRemA();
            xb = cw.getRemB();
        }
        switch (quadrant) {
            case 0: {
                return FastMath.cosQ(xa, xb);
            }
            case 1: {
                return -FastMath.sinQ(xa, xb);
            }
            case 2: {
                return -FastMath.cosQ(xa, xb);
            }
            case 3: {
                return FastMath.sinQ(xa, xb);
            }
        }
        return Double.NaN;
    }

    public static double tan(double x) {
        boolean negative = false;
        int quadrant = 0;
        double xa = x;
        if (x < 0.0) {
            negative = true;
            xa = -xa;
        }
        if (xa == 0.0) {
            long bits = Double.doubleToRawLongBits(x);
            if (bits < 0L) {
                return -0.0;
            }
            return 0.0;
        }
        if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        double xb = 0.0;
        if (xa > 3294198.0) {
            double[] reduceResults = new double[3];
            FastMath.reducePayneHanek(xa, reduceResults);
            quadrant = (int)reduceResults[0] & 3;
            xa = reduceResults[1];
            xb = reduceResults[2];
        } else if (xa > 1.5707963267948966) {
            CodyWaite cw = new CodyWaite(xa);
            quadrant = cw.getK() & 3;
            xa = cw.getRemA();
            xb = cw.getRemB();
        }
        if (xa > 1.5) {
            double pi2a = 1.5707963267948966;
            double pi2b = 6.123233995736766E-17;
            double a2 = 1.5707963267948966 - xa;
            double b2 = -(a2 - 1.5707963267948966 + xa);
            xa = a2 + (b2 += 6.123233995736766E-17 - xb);
            xb = -(xa - a2 - b2);
            quadrant ^= 1;
            negative ^= true;
        }
        double result = !(quadrant & true) ? FastMath.tanQ(xa, xb, false) : -FastMath.tanQ(xa, xb, true);
        if (negative) {
            result = -result;
        }
        return result;
    }

    public static double atan(double x) {
        return FastMath.atan(x, 0.0, false);
    }

    private static double atan(double xa, double xb, boolean leftPlane) {
        int idx;
        boolean negate;
        if (xa == 0.0) {
            return leftPlane ? FastMath.copySign(Math.PI, xa) : xa;
        }
        if (xa < 0.0) {
            xa = -xa;
            xb = -xb;
            negate = true;
        } else {
            negate = false;
        }
        if (xa > 1.633123935319537E16) {
            return negate ^ leftPlane ? -1.5707963267948966 : 1.5707963267948966;
        }
        if (xa < 1.0) {
            idx = (int)((-1.7168146928204135 * xa * xa + 8.0) * xa + 0.5);
        } else {
            double oneOverXa = 1.0 / xa;
            idx = (int)(-((-1.7168146928204135 * oneOverXa * oneOverXa + 8.0) * oneOverXa) + 13.07);
        }
        double ttA = TANGENT_TABLE_A[idx];
        double ttB = TANGENT_TABLE_B[idx];
        double epsA = xa - ttA;
        double epsB = -(epsA - xa + ttA);
        double temp = epsA + (epsB += xb - ttB);
        epsB = -(temp - epsA - epsB);
        epsA = temp;
        temp = xa * 1.073741824E9;
        double ya = xa + temp - temp;
        double yb = xb + xa - ya;
        xa = ya;
        xb += yb;
        if (idx == 0) {
            double denom = 1.0 / (1.0 + (xa + xb) * (ttA + ttB));
            ya = epsA * denom;
            yb = epsB * denom;
        } else {
            double temp2 = xa * ttA;
            double za = 1.0 + temp2;
            double zb = -(za - 1.0 - temp2);
            temp2 = xb * ttA + xa * ttB;
            temp = za + temp2;
            zb += -(temp - za - temp2);
            za = temp;
            zb += xb * ttB;
            ya = epsA / za;
            temp = ya * 1.073741824E9;
            double yaa = ya + temp - temp;
            double yab = ya - yaa;
            temp = za * 1.073741824E9;
            double zaa = za + temp - temp;
            double zab = za - zaa;
            yb = (epsA - yaa * zaa - yaa * zab - yab * zaa - yab * zab) / za;
            yb += -epsA * zb / za / za;
            yb += epsB / za;
        }
        epsA = ya;
        epsB = yb;
        double epsA2 = epsA * epsA;
        yb = 0.07490822288864472;
        yb = yb * epsA2 - 0.09088450866185192;
        yb = yb * epsA2 + 0.11111095942313305;
        yb = yb * epsA2 - 0.1428571423679182;
        yb = yb * epsA2 + 0.19999999999923582;
        yb = yb * epsA2 - 0.33333333333333287;
        yb = yb * epsA2 * epsA;
        ya = epsA;
        temp = ya + yb;
        yb = -(temp - ya - yb);
        ya = temp;
        double eighths = EIGHTHS[idx];
        double za = eighths + ya;
        double zb = -(za - eighths - ya);
        temp = za + (yb += epsB / (1.0 + epsA * epsA));
        zb += -(temp - za - yb);
        za = temp;
        double result = za + zb;
        if (leftPlane) {
            double resultb = -(result - za - zb);
            double pia = Math.PI;
            double pib = 1.2246467991473532E-16;
            za = Math.PI - result;
            zb = -(za - Math.PI + result);
            result = za + (zb += 1.2246467991473532E-16 - resultb);
        }
        if (negate ^ leftPlane) {
            result = -result;
        }
        return result;
    }

    public static double atan2(double y, double x) {
        double r;
        if (x != x || y != y) {
            return Double.NaN;
        }
        if (y == 0.0) {
            double result = x * y;
            double invx = 1.0 / x;
            double invy = 1.0 / y;
            if (invx == 0.0) {
                if (x > 0.0) {
                    return y;
                }
                return FastMath.copySign(Math.PI, y);
            }
            if (x < 0.0 || invx < 0.0) {
                if (y < 0.0 || invy < 0.0) {
                    return -Math.PI;
                }
                return Math.PI;
            }
            return result;
        }
        if (y == Double.POSITIVE_INFINITY) {
            if (x == Double.POSITIVE_INFINITY) {
                return 0.7853981633974483;
            }
            if (x == Double.NEGATIVE_INFINITY) {
                return 2.356194490192345;
            }
            return 1.5707963267948966;
        }
        if (y == Double.NEGATIVE_INFINITY) {
            if (x == Double.POSITIVE_INFINITY) {
                return -0.7853981633974483;
            }
            if (x == Double.NEGATIVE_INFINITY) {
                return -2.356194490192345;
            }
            return -1.5707963267948966;
        }
        if (x == Double.POSITIVE_INFINITY) {
            if (y > 0.0 || 1.0 / y > 0.0) {
                return 0.0;
            }
            if (y < 0.0 || 1.0 / y < 0.0) {
                return -0.0;
            }
        }
        if (x == Double.NEGATIVE_INFINITY) {
            if (y > 0.0 || 1.0 / y > 0.0) {
                return Math.PI;
            }
            if (y < 0.0 || 1.0 / y < 0.0) {
                return -Math.PI;
            }
        }
        if (x == 0.0) {
            if (y > 0.0 || 1.0 / y > 0.0) {
                return 1.5707963267948966;
            }
            if (y < 0.0 || 1.0 / y < 0.0) {
                return -1.5707963267948966;
            }
        }
        if (Double.isInfinite(r = y / x)) {
            return FastMath.atan(r, 0.0, x < 0.0);
        }
        double ra = FastMath.doubleHighPart(r);
        double rb = r - ra;
        double xa = FastMath.doubleHighPart(x);
        double xb = x - xa;
        rb += (y - ra * xa - ra * xb - rb * xa - rb * xb) / x;
        double temp = ra + rb;
        rb = -(temp - ra - rb);
        ra = temp;
        if (ra == 0.0) {
            ra = FastMath.copySign(0.0, y);
        }
        double result = FastMath.atan(ra, rb, x < 0.0);
        return result;
    }

    public static double asin(double x) {
        if (x != x) {
            return Double.NaN;
        }
        if (x > 1.0 || x < -1.0) {
            return Double.NaN;
        }
        if (x == 1.0) {
            return 1.5707963267948966;
        }
        if (x == -1.0) {
            return -1.5707963267948966;
        }
        if (x == 0.0) {
            return x;
        }
        double temp = x * 1.073741824E9;
        double xa = x + temp - temp;
        double xb = x - xa;
        double ya = xa * xa;
        double yb = xa * xb * 2.0 + xb * xb;
        ya = -ya;
        yb = -yb;
        double za = 1.0 + ya;
        double zb = -(za - 1.0 - ya);
        temp = za + yb;
        zb += -(temp - za - yb);
        za = temp;
        double y = FastMath.sqrt(za);
        temp = y * 1.073741824E9;
        ya = y + temp - temp;
        yb = y - ya;
        yb += (za - ya * ya - 2.0 * ya * yb - yb * yb) / (2.0 * y);
        double dx = zb / (2.0 * y);
        double r = x / y;
        temp = r * 1.073741824E9;
        double ra = r + temp - temp;
        double rb = r - ra;
        rb += (x - ra * ya - ra * yb - rb * ya - rb * yb) / y;
        temp = ra + (rb += -x * dx / y / y);
        rb = -(temp - ra - rb);
        ra = temp;
        return FastMath.atan(ra, rb, false);
    }

    public static double acos(double x) {
        if (x != x) {
            return Double.NaN;
        }
        if (x > 1.0 || x < -1.0) {
            return Double.NaN;
        }
        if (x == -1.0) {
            return Math.PI;
        }
        if (x == 1.0) {
            return 0.0;
        }
        if (x == 0.0) {
            return 1.5707963267948966;
        }
        double temp = x * 1.073741824E9;
        double xa = x + temp - temp;
        double xb = x - xa;
        double ya = xa * xa;
        double yb = xa * xb * 2.0 + xb * xb;
        ya = -ya;
        yb = -yb;
        double za = 1.0 + ya;
        double zb = -(za - 1.0 - ya);
        temp = za + yb;
        zb += -(temp - za - yb);
        za = temp;
        double y = FastMath.sqrt(za);
        temp = y * 1.073741824E9;
        ya = y + temp - temp;
        yb = y - ya;
        yb += (za - ya * ya - 2.0 * ya * yb - yb * yb) / (2.0 * y);
        yb += zb / (2.0 * y);
        y = ya + yb;
        yb = -(y - ya - yb);
        double r = y / x;
        if (Double.isInfinite(r)) {
            return 1.5707963267948966;
        }
        double ra = FastMath.doubleHighPart(r);
        double rb = r - ra;
        rb += (y - ra * xa - ra * xb - rb * xa - rb * xb) / x;
        temp = ra + (rb += yb / x);
        rb = -(temp - ra - rb);
        ra = temp;
        return FastMath.atan(ra, rb, x < 0.0);
    }

    public static double cbrt(double x) {
        long inbits = Double.doubleToRawLongBits(x);
        int exponent = (int)(inbits >> 52 & 0x7FFL) - 1023;
        boolean subnormal = false;
        if (exponent == -1023) {
            if (x == 0.0) {
                return x;
            }
            subnormal = true;
            inbits = Double.doubleToRawLongBits(x *= 1.8014398509481984E16);
            exponent = (int)(inbits >> 52 & 0x7FFL) - 1023;
        }
        if (exponent == 1024) {
            return x;
        }
        int exp3 = exponent / 3;
        double p2 = Double.longBitsToDouble(inbits & Long.MIN_VALUE | (long)(exp3 + 1023 & 0x7FF) << 52);
        double mant = Double.longBitsToDouble(inbits & 0xFFFFFFFFFFFFFL | 0x3FF0000000000000L);
        double est = -0.010714690733195933;
        est = est * mant + 0.0875862700108075;
        est = est * mant + -0.3058015757857271;
        est = est * mant + 0.7249995199969751;
        est = est * mant + 0.5039018405998233;
        est *= CBRTTWO[exponent % 3 + 2];
        double xs = x / (p2 * p2 * p2);
        est += (xs - est * est * est) / (3.0 * est * est);
        est += (xs - est * est * est) / (3.0 * est * est);
        double temp = est * 1.073741824E9;
        double ya = est + temp - temp;
        double yb = est - ya;
        double za = ya * ya;
        double zb = ya * yb * 2.0 + yb * yb;
        temp = za * 1.073741824E9;
        double temp2 = za + temp - temp;
        zb += za - temp2;
        za = temp2;
        zb = za * yb + ya * zb + zb * yb;
        double na = xs - (za *= ya);
        double nb = -(na - xs + za);
        est += (na + (nb -= zb)) / (3.0 * est * est);
        est *= p2;
        if (subnormal) {
            est *= 3.814697265625E-6;
        }
        return est;
    }

    public static double toRadians(double x) {
        if (Double.isInfinite(x) || x == 0.0) {
            return x;
        }
        double facta = 0.01745329052209854;
        double factb = 1.997844754509471E-9;
        double xa = FastMath.doubleHighPart(x);
        double xb = x - xa;
        double result = xb * 1.997844754509471E-9 + xb * 0.01745329052209854 + xa * 1.997844754509471E-9 + xa * 0.01745329052209854;
        if (result == 0.0) {
            result *= x;
        }
        return result;
    }

    public static double toDegrees(double x) {
        if (Double.isInfinite(x) || x == 0.0) {
            return x;
        }
        double facta = 57.2957763671875;
        double factb = 3.145894820876798E-6;
        double xa = FastMath.doubleHighPart(x);
        double xb = x - xa;
        return xb * 3.145894820876798E-6 + xb * 57.2957763671875 + xa * 3.145894820876798E-6 + xa * 57.2957763671875;
    }

    public static int abs(int x) {
        int i = x >>> 31;
        return (x ^ ~i + 1) + i;
    }

    public static long abs(long x) {
        long l = x >>> 63;
        return (x ^ (l ^ 0xFFFFFFFFFFFFFFFFL) + 1L) + l;
    }

    public static float abs(float x) {
        return Float.intBitsToFloat(Integer.MAX_VALUE & Float.floatToRawIntBits(x));
    }

    public static double abs(double x) {
        return Double.longBitsToDouble(Long.MAX_VALUE & Double.doubleToRawLongBits(x));
    }

    public static double ulp(double x) {
        if (Double.isInfinite(x)) {
            return Double.POSITIVE_INFINITY;
        }
        return FastMath.abs(x - Double.longBitsToDouble(Double.doubleToRawLongBits(x) ^ 1L));
    }

    public static float ulp(float x) {
        if (Float.isInfinite(x)) {
            return Float.POSITIVE_INFINITY;
        }
        return FastMath.abs(x - Float.intBitsToFloat(Float.floatToIntBits(x) ^ 1));
    }

    public static double scalb(double d, int n) {
        if (n > -1023 && n < 1024) {
            return d * Double.longBitsToDouble((long)(n + 1023) << 52);
        }
        if (Double.isNaN(d) || Double.isInfinite(d) || d == 0.0) {
            return d;
        }
        if (n < -2098) {
            return d > 0.0 ? 0.0 : -0.0;
        }
        if (n > 2097) {
            return d > 0.0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        long bits = Double.doubleToRawLongBits(d);
        long sign = bits & Long.MIN_VALUE;
        int exponent = (int)(bits >>> 52) & 0x7FF;
        long mantissa = bits & 0xFFFFFFFFFFFFFL;
        int scaledExponent = exponent + n;
        if (n < 0) {
            if (scaledExponent > 0) {
                return Double.longBitsToDouble(sign | (long)scaledExponent << 52 | mantissa);
            }
            if (scaledExponent > -53) {
                long mostSignificantLostBit = (mantissa |= 0x10000000000000L) & 1L << -scaledExponent;
                mantissa >>>= 1 - scaledExponent;
                if (mostSignificantLostBit != 0L) {
                    ++mantissa;
                }
                return Double.longBitsToDouble(sign | mantissa);
            }
            return sign == 0L ? 0.0 : -0.0;
        }
        if (exponent == 0) {
            while (mantissa >>> 52 != 1L) {
                mantissa <<= 1;
                --scaledExponent;
            }
            mantissa &= 0xFFFFFFFFFFFFFL;
            if (++scaledExponent < 2047) {
                return Double.longBitsToDouble(sign | (long)scaledExponent << 52 | mantissa);
            }
            return sign == 0L ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        if (scaledExponent < 2047) {
            return Double.longBitsToDouble(sign | (long)scaledExponent << 52 | mantissa);
        }
        return sign == 0L ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
    }

    public static float scalb(float f, int n) {
        if (n > -127 && n < 128) {
            return f * Float.intBitsToFloat(n + 127 << 23);
        }
        if (Float.isNaN(f) || Float.isInfinite(f) || f == 0.0f) {
            return f;
        }
        if (n < -277) {
            return f > 0.0f ? 0.0f : -0.0f;
        }
        if (n > 276) {
            return f > 0.0f ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        }
        int bits = Float.floatToIntBits(f);
        int sign = bits & Integer.MIN_VALUE;
        int exponent = bits >>> 23 & 0xFF;
        int mantissa = bits & 0x7FFFFF;
        int scaledExponent = exponent + n;
        if (n < 0) {
            if (scaledExponent > 0) {
                return Float.intBitsToFloat(sign | scaledExponent << 23 | mantissa);
            }
            if (scaledExponent > -24) {
                int mostSignificantLostBit = (mantissa |= 0x800000) & 1 << -scaledExponent;
                mantissa >>>= 1 - scaledExponent;
                if (mostSignificantLostBit != 0) {
                    ++mantissa;
                }
                return Float.intBitsToFloat(sign | mantissa);
            }
            return sign == 0 ? 0.0f : -0.0f;
        }
        if (exponent == 0) {
            while (mantissa >>> 23 != 1) {
                mantissa <<= 1;
                --scaledExponent;
            }
            mantissa &= 0x7FFFFF;
            if (++scaledExponent < 255) {
                return Float.intBitsToFloat(sign | scaledExponent << 23 | mantissa);
            }
            return sign == 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        }
        if (scaledExponent < 255) {
            return Float.intBitsToFloat(sign | scaledExponent << 23 | mantissa);
        }
        return sign == 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
    }

    public static double nextAfter(double d, double direction) {
        long bits;
        long sign;
        if (Double.isNaN(d) || Double.isNaN(direction)) {
            return Double.NaN;
        }
        if (d == direction) {
            return direction;
        }
        if (Double.isInfinite(d)) {
            return d < 0.0 ? -1.7976931348623157E308 : Double.MAX_VALUE;
        }
        if (d == 0.0) {
            return direction < 0.0 ? -4.9E-324 : Double.MIN_VALUE;
        }
        if (direction < d ^ (sign = (bits = Double.doubleToRawLongBits(d)) & Long.MIN_VALUE) == 0L) {
            return Double.longBitsToDouble(sign | (bits & Long.MAX_VALUE) + 1L);
        }
        return Double.longBitsToDouble(sign | (bits & Long.MAX_VALUE) - 1L);
    }

    public static float nextAfter(float f, double direction) {
        int bits;
        int sign;
        if (Double.isNaN(f) || Double.isNaN(direction)) {
            return Float.NaN;
        }
        if ((double)f == direction) {
            return (float)direction;
        }
        if (Float.isInfinite(f)) {
            return f < 0.0f ? -3.4028235E38f : Float.MAX_VALUE;
        }
        if (f == 0.0f) {
            return direction < 0.0 ? -1.4E-45f : Float.MIN_VALUE;
        }
        if (direction < (double)f ^ (sign = (bits = Float.floatToIntBits(f)) & Integer.MIN_VALUE) == 0) {
            return Float.intBitsToFloat(sign | (bits & Integer.MAX_VALUE) + 1);
        }
        return Float.intBitsToFloat(sign | (bits & Integer.MAX_VALUE) - 1);
    }

    public static double floor(double x) {
        if (x != x) {
            return x;
        }
        if (x >= 4.503599627370496E15 || x <= -4.503599627370496E15) {
            return x;
        }
        long y = (long)x;
        if (x < 0.0 && (double)y != x) {
            --y;
        }
        if (y == 0L) {
            return x * (double)y;
        }
        return y;
    }

    public static double ceil(double x) {
        if (x != x) {
            return x;
        }
        double y = FastMath.floor(x);
        if (y == x) {
            return y;
        }
        if ((y += 1.0) == 0.0) {
            return x * y;
        }
        return y;
    }

    public static double rint(double x) {
        double y = FastMath.floor(x);
        double d = x - y;
        if (d > 0.5) {
            if (y == -1.0) {
                return -0.0;
            }
            return y + 1.0;
        }
        if (d < 0.5) {
            return y;
        }
        long z = (long)y;
        return (z & 1L) == 0L ? y : y + 1.0;
    }

    public static long round(double x) {
        return (long)FastMath.floor(x + 0.5);
    }

    public static int round(float x) {
        return (int)FastMath.floor(x + 0.5f);
    }

    public static int min(int a2, int b2) {
        return a2 <= b2 ? a2 : b2;
    }

    public static long min(long a2, long b2) {
        return a2 <= b2 ? a2 : b2;
    }

    public static float min(float a2, float b2) {
        if (a2 > b2) {
            return b2;
        }
        if (a2 < b2) {
            return a2;
        }
        if (a2 != b2) {
            return Float.NaN;
        }
        int bits = Float.floatToRawIntBits(a2);
        if (bits == Integer.MIN_VALUE) {
            return a2;
        }
        return b2;
    }

    public static double min(double a2, double b2) {
        if (a2 > b2) {
            return b2;
        }
        if (a2 < b2) {
            return a2;
        }
        if (a2 != b2) {
            return Double.NaN;
        }
        long bits = Double.doubleToRawLongBits(a2);
        if (bits == Long.MIN_VALUE) {
            return a2;
        }
        return b2;
    }

    public static int max(int a2, int b2) {
        return a2 <= b2 ? b2 : a2;
    }

    public static long max(long a2, long b2) {
        return a2 <= b2 ? b2 : a2;
    }

    public static float max(float a2, float b2) {
        if (a2 > b2) {
            return a2;
        }
        if (a2 < b2) {
            return b2;
        }
        if (a2 != b2) {
            return Float.NaN;
        }
        int bits = Float.floatToRawIntBits(a2);
        if (bits == Integer.MIN_VALUE) {
            return b2;
        }
        return a2;
    }

    public static double max(double a2, double b2) {
        if (a2 > b2) {
            return a2;
        }
        if (a2 < b2) {
            return b2;
        }
        if (a2 != b2) {
            return Double.NaN;
        }
        long bits = Double.doubleToRawLongBits(a2);
        if (bits == Long.MIN_VALUE) {
            return b2;
        }
        return a2;
    }

    public static double hypot(double x, double y) {
        int expY;
        if (Double.isInfinite(x) || Double.isInfinite(y)) {
            return Double.POSITIVE_INFINITY;
        }
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return Double.NaN;
        }
        int expX = FastMath.getExponent(x);
        if (expX > (expY = FastMath.getExponent(y)) + 27) {
            return FastMath.abs(x);
        }
        if (expY > expX + 27) {
            return FastMath.abs(y);
        }
        int middleExp = (expX + expY) / 2;
        double scaledX = FastMath.scalb(x, -middleExp);
        double scaledY = FastMath.scalb(y, -middleExp);
        double scaledH = FastMath.sqrt(scaledX * scaledX + scaledY * scaledY);
        return FastMath.scalb(scaledH, middleExp);
    }

    public static double IEEEremainder(double dividend, double divisor) {
        return StrictMath.IEEEremainder(dividend, divisor);
    }

    public static double copySign(double magnitude, double sign) {
        long s;
        long m = Double.doubleToRawLongBits(magnitude);
        if ((m ^ (s = Double.doubleToRawLongBits(sign))) >= 0L) {
            return magnitude;
        }
        return -magnitude;
    }

    public static float copySign(float magnitude, float sign) {
        int s;
        int m = Float.floatToRawIntBits(magnitude);
        if ((m ^ (s = Float.floatToRawIntBits(sign))) >= 0) {
            return magnitude;
        }
        return -magnitude;
    }

    public static int getExponent(double d) {
        return (int)(Double.doubleToRawLongBits(d) >>> 52 & 0x7FFL) - 1023;
    }

    public static int getExponent(float f) {
        return (Float.floatToRawIntBits(f) >>> 23 & 0xFF) - 127;
    }

    public static void main(String[] a2) {
        PrintStream out = System.out;
        FastMathCalc.printarray(out, "EXP_INT_TABLE_A", 1500, ExpIntTable.EXP_INT_TABLE_A);
        FastMathCalc.printarray(out, "EXP_INT_TABLE_B", 1500, ExpIntTable.EXP_INT_TABLE_B);
        FastMathCalc.printarray(out, "EXP_FRAC_TABLE_A", 1025, ExpFracTable.EXP_FRAC_TABLE_A);
        FastMathCalc.printarray(out, "EXP_FRAC_TABLE_B", 1025, ExpFracTable.EXP_FRAC_TABLE_B);
        FastMathCalc.printarray(out, "LN_MANT", 1024, lnMant.LN_MANT);
        FastMathCalc.printarray(out, "SINE_TABLE_A", 14, SINE_TABLE_A);
        FastMathCalc.printarray(out, "SINE_TABLE_B", 14, SINE_TABLE_B);
        FastMathCalc.printarray(out, "COSINE_TABLE_A", 14, COSINE_TABLE_A);
        FastMathCalc.printarray(out, "COSINE_TABLE_B", 14, COSINE_TABLE_B);
        FastMathCalc.printarray(out, "TANGENT_TABLE_A", 14, TANGENT_TABLE_A);
        FastMathCalc.printarray(out, "TANGENT_TABLE_B", 14, TANGENT_TABLE_B);
    }

    private static class CodyWaite {
        private final int finalK;
        private final double finalRemA;
        private final double finalRemB;

        CodyWaite(double xa) {
            double remB;
            double remA;
            int k = (int)(xa * 0.6366197723675814);
            while (true) {
                double a2 = (double)(-k) * 1.570796251296997;
                remA = xa + a2;
                remB = -(remA - xa - a2);
                a2 = (double)(-k) * 7.549789948768648E-8;
                double b2 = remA;
                remA = a2 + b2;
                remB += -(remA - b2 - a2);
                a2 = (double)(-k) * 6.123233995736766E-17;
                b2 = remA;
                remA = a2 + b2;
                remB += -(remA - b2 - a2);
                if (remA > 0.0) break;
                --k;
            }
            this.finalK = k;
            this.finalRemA = remA;
            this.finalRemB = remB;
        }

        int getK() {
            return this.finalK;
        }

        double getRemA() {
            return this.finalRemA;
        }

        double getRemB() {
            return this.finalRemB;
        }
    }

    private static class lnMant {
        private static final double[][] LN_MANT = FastMathLiteralArrays.loadLnMant();

        private lnMant() {
        }
    }

    private static class ExpFracTable {
        private static final double[] EXP_FRAC_TABLE_A = FastMathLiteralArrays.loadExpFracA();
        private static final double[] EXP_FRAC_TABLE_B = FastMathLiteralArrays.loadExpFracB();

        private ExpFracTable() {
        }
    }

    private static class ExpIntTable {
        private static final double[] EXP_INT_TABLE_A = FastMathLiteralArrays.loadExpIntA();
        private static final double[] EXP_INT_TABLE_B = FastMathLiteralArrays.loadExpIntB();

        private ExpIntTable() {
        }
    }
}

