package com.oplus.wrapper.os;

/* loaded from: classes.dex */
public class ProcessCpuTracker {
    private final com.android.internal.os.ProcessCpuTracker mProcessCpuTracker;

    public ProcessCpuTracker(boolean includeThreads) {
        this.mProcessCpuTracker = new com.android.internal.os.ProcessCpuTracker(includeThreads);
    }

    public void update() {
        this.mProcessCpuTracker.update();
    }

    public String printCurrentState(long now) {
        return this.mProcessCpuTracker.printCurrentState(now);
    }

    public String printCurrentLoad() {
        return this.mProcessCpuTracker.printCurrentLoad();
    }

    public int getLastIrqTime() {
        return this.mProcessCpuTracker.getLastIrqTime();
    }

    public int getLastSystemTime() {
        return this.mProcessCpuTracker.getLastSystemTime();
    }

    public int getLastUserTime() {
        return this.mProcessCpuTracker.getLastUserTime();
    }

    public float getTotalCpuPercent() {
        return this.mProcessCpuTracker.getTotalCpuPercent();
    }

    public void init() {
        this.mProcessCpuTracker.init();
    }
}
