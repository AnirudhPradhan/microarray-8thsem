/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io.data;

import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.io.IOCapabilities;
import de.erichseifert.gral.io.data.AbstractDataReader;
import de.erichseifert.gral.util.Messages;
import de.erichseifert.gral.util.StatefulTokenizer;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CSVReader
extends AbstractDataReader {
    public static final String SEPARATOR_CHAR = "separator";

    public CSVReader(String string) {
        super(string);
        if ("text/tab-separated-values".equals(string)) {
            this.setDefault(SEPARATOR_CHAR, Character.valueOf('\t'));
            return;
        }
        this.setDefault(SEPARATOR_CHAR, Character.valueOf(','));
    }

    @Override
    public DataSource read(InputStream object, Class<? extends Comparable<?>> ... classArray) throws IOException {
        Object object2;
        int n;
        object = new Scanner((InputStream)object).useDelimiter("\\Z");
        object = ((Scanner)object).next();
        Object object3 = (Character)this.getSetting(SEPARATOR_CHAR);
        object3 = new b(((Character)object3).charValue());
        if (((StatefulTokenizer.Token)(object3 = (object = ((StatefulTokenizer)object3).tokenize((String)object)).get(object.size() - 1))).getType() != a.d) {
            object3 = new StatefulTokenizer.Token(((StatefulTokenizer.Token)object3).getEnd(), ((StatefulTokenizer.Token)object3).getEnd(), (Object)a.d, "");
            object.add((StatefulTokenizer.Token)object3);
        }
        object3 = new HashMap();
        Object object4 = classArray;
        int n2 = classArray.length;
        for (n = 0; n < n2; ++n) {
            Class<Comparable<?>> clazz = object4[n];
            if (object3.containsKey(clazz) || (object2 = CSVReader.a(clazz)) == null) continue;
            object3.put(clazz, object2);
        }
        object4 = new DataTable(classArray);
        LinkedList<Comparable> linkedList = new LinkedList<Comparable>();
        n = 0;
        int n3 = 0;
        object2 = "";
        object = object.iterator();
        while (object.hasNext()) {
            Comparable comparable;
            StatefulTokenizer.Token token;
            block11: {
                token = (StatefulTokenizer.Token)object.next();
                if (token.getType() == a.b || token.getType() == a.a) {
                    object2 = (String)object2 + token.getContent();
                    continue;
                }
                if (token.getType() != a.e && token.getType() != a.d) continue;
                if (n3 >= classArray.length) {
                    throw new IllegalArgumentException(MessageFormat.format("Too many columns in line {0,number,integer}: got {1,number,integer}, but expected {2,number,integer}.", n + 1, n3 + 1, classArray.length));
                }
                Class<Comparable<?>> clazz = classArray[n3];
                Method method = (Method)object3.get(clazz);
                comparable = null;
                try {
                    comparable = (Comparable)method.invoke(null, ((String)object2).trim());
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    throw new RuntimeException(MessageFormat.format("Could not invoke method for parsing data type {0} in column {1,number,integer}.", classArray[n3].getSimpleName(), n3));
                }
                catch (IllegalAccessException illegalAccessException) {
                    throw new RuntimeException(MessageFormat.format("Could not access method for parsing data type {0} in column {1,number,integer}.", classArray[n3].getSimpleName(), n3));
                }
                catch (InvocationTargetException invocationTargetException) {
                    if (((String)object2).isEmpty()) break block11;
                    throw new IOException(MessageFormat.format("Type mismatch in line {0,number,integer}, column {1,number,integer}: got \"{2}\", but expected {3} value.", n + 1, n3 + 1, object2, clazz.getSimpleName()));
                }
            }
            linkedList.add(comparable);
            ++n3;
            if (token.getType() == a.d) {
                if (linkedList.size() < classArray.length) {
                    throw new IllegalArgumentException(MessageFormat.format("Not enough columns in line {0,number,integer}: got {1,number,integer}, but expected {2,number,integer}.", n + 1, linkedList.size(), classArray.length));
                }
                ((DataTable)object4).add(linkedList);
                ++n;
                linkedList.clear();
                n3 = 0;
            }
            object2 = "";
        }
        return object4;
    }

    private static Method a(Class<?> clazz) {
        Method method = null;
        if (String.class.isAssignableFrom(clazz)) {
            try {
                method = String.class.getMethod("valueOf", Object.class);
            }
            catch (NoSuchMethodException noSuchMethodException) {}
        } else {
            for (Method method2 : clazz.getMethods()) {
                Class<?>[] classArray;
                boolean bl = method2.toString().contains("static");
                if (!bl || !(bl = (classArray = method2.getParameterTypes()).length == 1 && String.class.equals(classArray[0])) || !method2.getName().startsWith("parse" + clazz.getSimpleName().substring(0, 3))) continue;
                method = method2;
            }
        }
        return method;
    }

    static {
        CSVReader.addCapabilities(new IOCapabilities("CSV", Messages.getString("DataIO.csvDescription"), "text/csv", new String[]{"csv", "txt"}));
        CSVReader.addCapabilities(new IOCapabilities("TSV", Messages.getString("DataIO.tsvDescription"), "text/tab-separated-values", new String[]{"tsv", "tab", "txt"}));
    }

    private static final class b
    extends StatefulTokenizer {
        public b(char c2) {
            this.addJoinedType((Object)a.b);
            this.addIgnoredType((Object)a.c);
            this.putRules(new StatefulTokenizer.Rule("\n|\r\n|\r", (Object)a.d), new StatefulTokenizer.Rule(Pattern.quote(String.valueOf(c2)), (Object)a.e), new StatefulTokenizer.Rule("\"", (Object)a.c, "quoted"), new StatefulTokenizer.Rule("[ \t]+", (Object)a.a), new StatefulTokenizer.Rule(".", (Object)a.b));
            this.putRules("quoted", new StatefulTokenizer.Rule("(\")\"", (Object)a.b), new StatefulTokenizer.Rule("\"", (Object)a.c, "#pop"), new StatefulTokenizer.Rule(".", (Object)a.b));
        }
    }

    private static enum a {
        a,
        b,
        c,
        d,
        e;

    }
}

