package com.android.server.pm;

import android.os.Binder;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class KeySetHandle extends Binder {
    private final long mId;
    private int mRefCount;

    /* JADX INFO: Access modifiers changed from: protected */
    public KeySetHandle(long j) {
        this.mId = j;
        this.mRefCount = 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public KeySetHandle(long j, int i) {
        this.mId = j;
        this.mRefCount = i;
    }

    public long getId() {
        return this.mId;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getRefCountLPr() {
        return this.mRefCount;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setRefCountLPw(int i) {
        this.mRefCount = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void incrRefCountLPw() {
        this.mRefCount++;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int decrRefCountLPw() {
        int i = this.mRefCount - 1;
        this.mRefCount = i;
        return i;
    }
}
