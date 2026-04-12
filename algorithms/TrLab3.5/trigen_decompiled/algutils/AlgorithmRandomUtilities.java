/*
 * Decompiled with CFR 0.152.
 */
package algutils;

import algutils.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.math3.random.RandomDataGenerator;

public class AlgorithmRandomUtilities {
    private static AlgorithmRandomUtilities singleton = new AlgorithmRandomUtilities();
    private RandomDataGenerator random = new RandomDataGenerator();
    private Set<Integer> mainBag;
    private Set<Point> pointsBag;
    private List<Set<Integer>> server = new LinkedList<Set<Integer>>();

    private AlgorithmRandomUtilities() {
    }

    public static AlgorithmRandomUtilities getInstance() {
        return singleton;
    }

    public RandomDataGenerator getRandomObject() {
        return this.random;
    }

    public int newBagOnServer(int bagSize) {
        HashSet<Integer> aux = new HashSet<Integer>();
        for (int i = 0; i < bagSize; ++i) {
            aux.add(new Integer(i));
        }
        this.server.add(aux);
        return this.server.indexOf(aux);
    }

    public void newBag(int bagSize) {
        this.mainBag = new HashSet<Integer>();
        for (int i = 0; i < bagSize; ++i) {
            this.mainBag.add(new Integer(i));
        }
    }

    public void newBag() {
        this.mainBag = new HashSet<Integer>();
    }

    public void newPointsBag() {
        this.pointsBag = new HashSet<Point>();
    }

    public void newPointsBag(Set<Point> marbles) {
        this.pointsBag = new HashSet<Point>(marbles);
    }

    public void putMarbles(Collection<Integer> marbles) {
        this.mainBag.addAll(marbles);
    }

    public void putMarbles(Set<Integer> bag, Collection<Integer> removedMarbles) {
        this.mainBag.addAll(bag);
        this.mainBag.removeAll(removedMarbles);
    }

    public void putMarbles(Set<Integer> bag) {
        this.mainBag.addAll(bag);
    }

    public void putMarbles(Collection<Integer> marbles, int serverSlotIndex) {
        this.server.get(serverSlotIndex).addAll(marbles);
    }

    public void putPointMarbles(Set<Point> bag) {
        this.pointsBag.addAll(bag);
    }

    public int pointBagSize() {
        return this.pointsBag.size();
    }

    public int bagSize() {
        return this.mainBag.size();
    }

    public int bagSize(int serverSlotIndex) {
        return this.server.get(serverSlotIndex).size();
    }

    public void printBag() {
        Iterator<Integer> it = this.mainBag.iterator();
        System.out.print("\n");
        System.out.print("Bag with " + this.mainBag.size() + " marbles :");
        while (it.hasNext()) {
            int bola = it.next();
            System.out.print(bola + "  ");
        }
    }

    public void printBag(int serverSlotIndex) {
        Iterator<Integer> it = this.server.get(serverSlotIndex).iterator();
        System.out.print("\n");
        System.out.print("Bag with " + this.server.get(serverSlotIndex).size() + " marbles [" + serverSlotIndex + "]:");
        while (it.hasNext()) {
            int bola = it.next();
            System.out.print(bola + "  ");
        }
    }

    public int extractAmarble() {
        int res = 0;
        Object[] tirada = this.random.nextSample(this.mainBag, 1);
        Integer bola = (Integer)tirada[0];
        this.mainBag.remove(bola);
        res = bola;
        return res;
    }

    public int extractAmarble(int serverSlotIndex) {
        int res = 0;
        Object[] tirada = this.random.nextSample((Collection)this.server.get(serverSlotIndex), 1);
        Integer bola = (Integer)tirada[0];
        this.server.get(serverSlotIndex).remove(bola);
        res = bola;
        return res;
    }

    public int[] extractNmarbles(int n) {
        int[] res = new int[n];
        Object[] tirada = this.random.nextSample(this.mainBag, n);
        for (int i = 0; i < n; ++i) {
            Integer bola = (Integer)tirada[i];
            this.mainBag.remove(bola);
            res[i] = bola;
        }
        return res;
    }

    public int[] extractNmarbles(int n, int serverSlotIndex) {
        int[] res = new int[n];
        Object[] tirada = this.random.nextSample((Collection)this.server.get(serverSlotIndex), n);
        for (int i = 0; i < n; ++i) {
            Integer bola = (Integer)tirada[i];
            this.server.get(serverSlotIndex).remove(bola);
            res[i] = bola;
        }
        return res;
    }

    public Point[] extractNpointMarbles(int n) {
        Point[] res = new Point[n];
        Object[] tirada = this.random.nextSample(this.pointsBag, n);
        for (int i = 0; i < n; ++i) {
            Point bola = (Point)tirada[i];
            this.pointsBag.remove(bola);
            res[i] = bola;
        }
        return res;
    }

    public double getPercentage() {
        return this.random.nextUniform(0.0, 1.0);
    }

    public int getFromInterval(int lower, int greater) {
        int res = lower;
        if (lower < greater) {
            res = this.random.nextInt(lower, greater);
        }
        return res;
    }

    public double getFromInterval(double lower, double greater) {
        double res = lower;
        if (lower < greater) {
            res = this.random.nextUniform(lower, greater);
        }
        return res;
    }
}

