/*
 * Decompiled with CFR 0.152.
 */
package analysis.biological;

import analysis.biological.GoSlot;
import java.util.List;

public class GoLevels {
    private List<GoSlot> pa;
    private List<GoSlot> p;

    public List<GoSlot> getPa() {
        return this.pa;
    }

    public void setPa(List<GoSlot> pa) {
        this.pa = pa;
    }

    public List<GoSlot> getP() {
        return this.p;
    }

    public void setP(List<GoSlot> p) {
        this.p = p;
    }

    public List<GoSlot> getSlotsByType(String type) {
        List<GoSlot> slots = null;
        switch (type) {
            case "pa": {
                slots = this.pa;
                break;
            }
            case "p": {
                slots = this.p;
            }
        }
        return slots;
    }

    public String report(String type) {
        String r = "";
        List<GoSlot> slots = null;
        switch (type) {
            case "pa": {
                slots = this.pa;
                break;
            }
            case "p": {
                slots = this.p;
            }
        }
        for (GoSlot s : slots) {
            r = r + s + "\n";
        }
        return r;
    }

    public String toString() {
        return "pa = " + this.pa.size() + " , p =" + this.p.size();
    }
}

