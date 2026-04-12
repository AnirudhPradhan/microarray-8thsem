/*
 * Decompiled with CFR 0.152.
 */
package general;

import general.GST;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.math3.random.RandomDataGenerator;

public class RandomSupport {
    private RandomDataGenerator random = new RandomDataGenerator();
    private Set<Integer> mainBag;
    private List<Set<Integer>> bagServer = new LinkedList<Set<Integer>>();

    public RandomDataGenerator getRandomGenerator() {
        return this.random;
    }

    public void setSeed() {
        this.random.reSeed();
    }

    public int newBagInServer(int size) {
        HashSet<Integer> aux = new HashSet<Integer>();
        for (int i = 0; i < size; ++i) {
            aux.add(new Integer(i));
        }
        this.bagServer.add(aux);
        return this.bagServer.indexOf(aux);
    }

    public void emptyAndFillMainBag(int size) {
        this.mainBag = new HashSet<Integer>();
        for (int i = 0; i < size; ++i) {
            this.mainBag.add(new Integer(i));
        }
    }

    public void emptyMainBag() {
        this.mainBag = new HashSet<Integer>();
    }

    public void putMarblesIntoMainBag(Collection<Integer> marbles) {
        this.mainBag.addAll(marbles);
    }

    public void putWholeBagAndRemoveMarblesIntoMainBag(Set<Integer> bag, Collection<Integer> marbles) {
        this.mainBag.addAll(bag);
        this.mainBag.removeAll(marbles);
    }

    public void putWholeBagIntoMainBag(Set<Integer> bag) {
        this.mainBag.addAll(bag);
    }

    public void putMarblesIntoServerSlot(Collection<Integer> marbles, int slot) {
        this.bagServer.get(slot).addAll(marbles);
    }

    public int sizeOfMainBag() {
        return this.mainBag.size();
    }

    public int sizeOfServerSlot(int slot) {
        return this.bagServer.get(slot).size();
    }

    public String toString() {
        String r = "Bag of " + this.mainBag.size() + " marbles:\n";
        for (Integer marble : this.mainBag) {
            r = r + marble + "  \n";
        }
        return r;
    }

    public String getServerSlotString(int slot) {
        Set<Integer> bag = this.bagServer.get(slot);
        String r = "Bag of " + bag.size() + " marbles [" + slot + "]:\n";
        for (Integer marble : bag) {
            r = r + marble + "  \n";
        }
        return r;
    }

    public int extractAmarbleFromMainBag() {
        int r = 0;
        Object[] shot = this.random.nextSample(this.mainBag, 1);
        Integer marble = (Integer)shot[0];
        this.mainBag.remove(marble);
        r = marble;
        return r;
    }

    public int extractAmarbleFromServerSlot(int slot) {
        int r = 0;
        Object[] shot = this.random.nextSample((Collection)this.bagServer.get(slot), 1);
        Integer marble = (Integer)shot[0];
        this.bagServer.get(slot).remove(marble);
        r = marble;
        return r;
    }

    public int[] extractNmarblesFromMainBag(int n) {
        int[] r = new int[n];
        Object[] shot = this.random.nextSample(this.mainBag, n);
        for (int i = 0; i < n; ++i) {
            Integer marble = (Integer)shot[i];
            this.mainBag.remove(marble);
            r[i] = marble;
        }
        return r;
    }

    public int[] extractNmarblesFromServerSlot(int n, int slot) {
        int[] r = new int[n];
        Object[] shot = this.random.nextSample((Collection)this.bagServer.get(slot), n);
        for (int i = 0; i < n; ++i) {
            Integer marble = (Integer)shot[i];
            this.bagServer.get(slot).remove(marble);
            r[i] = marble;
        }
        return r;
    }

    public double getPercentage() {
        return this.random.nextUniform(0.0, 1.0);
    }

    public int getNumberFromInterval(int lower, int upper) {
        int r = lower;
        if (lower < upper) {
            r = this.random.nextInt(lower, upper);
        }
        return r;
    }

    public double getNumberFromInterval(double lower, double upper) {
        double r = lower;
        if (lower < upper) {
            r = this.random.nextUniform(lower, upper);
        }
        return r;
    }

    public double getCriptograpghicNumber(double lower, double upper) {
        double r = lower;
        if (lower < upper) {
            long auxlower = (long)lower;
            long auxupper = (long)upper;
            r = this.random.nextSecureLong(auxlower, auxupper);
        }
        return r;
    }

    public GST extractOneGSTfromSet(Set<GST> st) {
        Object[] shot = this.random.nextSample(st, 1);
        return (GST)shot[0];
    }
}

