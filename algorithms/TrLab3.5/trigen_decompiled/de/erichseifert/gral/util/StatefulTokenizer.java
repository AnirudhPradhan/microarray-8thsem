/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StatefulTokenizer {
    protected static final String INITIAL_STATE = "";
    private final Set<Object> a = new HashSet<Object>();
    private final Set<Object> b = new HashSet<Object>();
    private final Map<String, Rule[]> c = new HashMap<String, Rule[]>();

    protected StatefulTokenizer() {
    }

    protected void addJoinedType(Object object) {
        this.a.add(object);
    }

    protected void addIgnoredType(Object object) {
        this.b.add(object);
    }

    protected void putRules(Rule ... ruleArray) {
        this.putRules(INITIAL_STATE, ruleArray);
    }

    protected void putRules(String string, Rule ... ruleArray) {
        this.c.put(string, ruleArray);
    }

    public List<Token> tokenize(String string) {
        LinkedList<Token> linkedList = new LinkedList<Token>();
        Stack<String> stack = new Stack<String>();
        stack.push(INITIAL_STATE);
        int n = 0;
        Token token = null;
        block0: while (n < string.length() && !stack.isEmpty()) {
            Rule[] ruleArray = (Rule[])stack.peek();
            Rule[] ruleArray2 = this.c.get(ruleArray);
            ruleArray = ruleArray2;
            ruleArray = ruleArray2;
            int n2 = ruleArray2.length;
            for (int i = 0; i < n2; ++i) {
                Rule rule = ruleArray[i];
                Token token2 = rule.getToken(string, n);
                if (token2 == null) continue;
                if (token != null && token.c.equals(token2.c) && this.a.contains(token.c)) {
                    token.append(token2);
                } else {
                    if (token != null && !this.b.contains(token.c)) {
                        linkedList.add(token);
                    }
                    token = token2;
                }
                n = token2.b;
                if ("#pop".equals(rule.c)) {
                    stack.pop();
                    continue block0;
                }
                if (rule.c == null) continue block0;
                stack.push(rule.c);
                continue block0;
            }
        }
        if (token != null && !this.b.contains(token.c)) {
            linkedList.add(token);
        }
        return linkedList;
    }

    protected static class Rule {
        private final Pattern a;
        private final Object b;
        private final String c;

        public Rule(String string, Object object, String string2) {
            this.a = Pattern.compile(string);
            this.b = object;
            this.c = string2;
        }

        public Rule(String string, Object object) {
            this(string, object, null);
        }

        public Token getToken(String object, int n) {
            Matcher matcher = this.a.matcher((CharSequence)object);
            matcher.region(n, ((String)object).length());
            if (!matcher.lookingAt()) {
                return null;
            }
            object = matcher.groupCount() > 0 ? matcher.group(1) : matcher.group();
            object = new Token(matcher.start(), matcher.end(), this.b, (String)object);
            return object;
        }
    }

    public static class Token {
        private final int a;
        private int b;
        private final Object c;
        private final StringBuilder d = new StringBuilder();

        public Token(int n, int n2, Object object, String string) {
            this.a = n;
            this.b = n2;
            this.c = object;
            this.d.append(string);
        }

        public void append(Token token) {
            this.d.append((CharSequence)token.d);
            this.b = token.b;
        }

        public int getStart() {
            return this.a;
        }

        public int getEnd() {
            return this.b;
        }

        public Object getType() {
            return this.c;
        }

        public String getContent() {
            return this.d.toString();
        }

        public String toString() {
            return String.format("%s[start=%d, end=%d, type=%s, content=\"%s\"]", this.getClass().getSimpleName(), this.getStart(), this.getEnd(), this.getType(), this.getContent());
        }
    }
}

