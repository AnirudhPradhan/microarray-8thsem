/*
 * Decompiled with CFR 0.152.
 */
package labinterface;

public class TaskStatus {
    private int progress;
    private String description;

    public TaskStatus(int progress, String description) {
        this.progress = progress;
        this.description = description;
    }

    public int getProgress() {
        return this.progress;
    }

    public String getDescription() {
        return this.description;
    }
}

