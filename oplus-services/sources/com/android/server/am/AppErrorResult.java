package com.android.server.am;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AppErrorResult {
    boolean mHasResult = false;
    int mResult;

    public void set(int i) {
        synchronized (this) {
            this.mHasResult = true;
            this.mResult = i;
            notifyAll();
        }
    }

    public int get() {
        synchronized (this) {
            while (!this.mHasResult) {
                try {
                    wait();
                } catch (InterruptedException unused) {
                }
            }
        }
        return this.mResult;
    }
}
