/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j.helpers;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.helpers.FormattingTuple;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class MessageFormatter {
    static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    public static final FormattingTuple format(String messagePattern, Object arg) {
        return MessageFormatter.arrayFormat(messagePattern, new Object[]{arg});
    }

    public static final FormattingTuple format(String messagePattern, Object arg1, Object arg2) {
        return MessageFormatter.arrayFormat(messagePattern, new Object[]{arg1, arg2});
    }

    static final Throwable getThrowableCandidate(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }
        Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable)lastEntry;
        }
        return null;
    }

    public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray) {
        int L;
        Throwable throwableCandidate = MessageFormatter.getThrowableCandidate(argArray);
        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwableCandidate);
        }
        if (argArray == null) {
            return new FormattingTuple(messagePattern);
        }
        int i = 0;
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
        for (L = 0; L < argArray.length; ++L) {
            int j = messagePattern.indexOf(DELIM_STR, i);
            if (j == -1) {
                if (i == 0) {
                    return new FormattingTuple(messagePattern, argArray, throwableCandidate);
                }
                sbuf.append(messagePattern.substring(i, messagePattern.length()));
                return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
            }
            if (MessageFormatter.isEscapedDelimeter(messagePattern, j)) {
                if (!MessageFormatter.isDoubleEscaped(messagePattern, j)) {
                    --L;
                    sbuf.append(messagePattern.substring(i, j - 1));
                    sbuf.append('{');
                    i = j + 1;
                    continue;
                }
                sbuf.append(messagePattern.substring(i, j - 1));
                MessageFormatter.deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                i = j + 2;
                continue;
            }
            sbuf.append(messagePattern.substring(i, j));
            MessageFormatter.deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
            i = j + 2;
        }
        sbuf.append(messagePattern.substring(i, messagePattern.length()));
        if (L < argArray.length - 1) {
            return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
        }
        return new FormattingTuple(sbuf.toString(), argArray, null);
    }

    static final boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex == 0) {
            return false;
        }
        char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        return potentialEscape == '\\';
    }

    static final boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        return delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\';
    }

    private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
        if (o == null) {
            sbuf.append("null");
            return;
        }
        if (!o.getClass().isArray()) {
            MessageFormatter.safeObjectAppend(sbuf, o);
        } else if (o instanceof boolean[]) {
            MessageFormatter.booleanArrayAppend(sbuf, (boolean[])o);
        } else if (o instanceof byte[]) {
            MessageFormatter.byteArrayAppend(sbuf, (byte[])o);
        } else if (o instanceof char[]) {
            MessageFormatter.charArrayAppend(sbuf, (char[])o);
        } else if (o instanceof short[]) {
            MessageFormatter.shortArrayAppend(sbuf, (short[])o);
        } else if (o instanceof int[]) {
            MessageFormatter.intArrayAppend(sbuf, (int[])o);
        } else if (o instanceof long[]) {
            MessageFormatter.longArrayAppend(sbuf, (long[])o);
        } else if (o instanceof float[]) {
            MessageFormatter.floatArrayAppend(sbuf, (float[])o);
        } else if (o instanceof double[]) {
            MessageFormatter.doubleArrayAppend(sbuf, (double[])o);
        } else {
            MessageFormatter.objectArrayAppend(sbuf, (Object[])o, seenMap);
        }
    }

    private static void safeObjectAppend(StringBuilder sbuf, Object o) {
        try {
            String oAsString = o.toString();
            sbuf.append(oAsString);
        }
        catch (Throwable t) {
            System.err.println("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + "]");
            t.printStackTrace();
            sbuf.append("[FAILED toString()]");
        }
    }

    private static void objectArrayAppend(StringBuilder sbuf, Object[] a2, Map<Object[], Object> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a2)) {
            seenMap.put(a2, null);
            int len = a2.length;
            for (int i = 0; i < len; ++i) {
                MessageFormatter.deeplyAppendParameter(sbuf, a2[i], seenMap);
                if (i == len - 1) continue;
                sbuf.append(", ");
            }
            seenMap.remove(a2);
        } else {
            sbuf.append("...");
        }
        sbuf.append(']');
    }

    private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a2[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void byteArrayAppend(StringBuilder sbuf, byte[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a2[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void charArrayAppend(StringBuilder sbuf, char[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a2[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void shortArrayAppend(StringBuilder sbuf, short[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a2[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void intArrayAppend(StringBuilder sbuf, int[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a2[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void longArrayAppend(StringBuilder sbuf, long[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a2[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void floatArrayAppend(StringBuilder sbuf, float[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a2[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }

    private static void doubleArrayAppend(StringBuilder sbuf, double[] a2) {
        sbuf.append('[');
        int len = a2.length;
        for (int i = 0; i < len; ++i) {
            sbuf.append(a2[i]);
            if (i == len - 1) continue;
            sbuf.append(", ");
        }
        sbuf.append(']');
    }
}

