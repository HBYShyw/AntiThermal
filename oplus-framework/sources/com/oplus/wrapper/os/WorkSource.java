package com.oplus.wrapper.os;

/* loaded from: classes.dex */
public class WorkSource {
    private android.os.WorkSource mWorkSource;

    public WorkSource(android.os.WorkSource workSource) {
        this.mWorkSource = workSource;
    }

    public String getPackageName(int index) {
        return this.mWorkSource.getPackageName(index);
    }

    public int getUid(int index) {
        return this.mWorkSource.getUid(index);
    }

    public int size() {
        return this.mWorkSource.size();
    }
}
