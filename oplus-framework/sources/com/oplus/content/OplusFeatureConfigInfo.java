package com.oplus.content;

/* loaded from: classes.dex */
public class OplusFeatureConfigInfo {
    public String name;
    public int priority;

    public OplusFeatureConfigInfo() {
    }

    public OplusFeatureConfigInfo(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public String toString() {
        return "OplusFeatureConfigInfo{name='" + this.name + "', priority=" + this.priority + '}';
    }
}
