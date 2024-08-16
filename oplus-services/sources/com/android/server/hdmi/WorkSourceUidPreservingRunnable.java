package com.android.server.hdmi;

import android.os.Binder;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WorkSourceUidPreservingRunnable implements Runnable {
    private Runnable mRunnable;
    private int mUid = Binder.getCallingWorkSourceUid();

    public WorkSourceUidPreservingRunnable(Runnable runnable) {
        this.mRunnable = runnable;
    }

    @Override // java.lang.Runnable
    public void run() {
        long callingWorkSourceUid = Binder.setCallingWorkSourceUid(this.mUid);
        try {
            this.mRunnable.run();
        } finally {
            Binder.restoreCallingWorkSource(callingWorkSourceUid);
        }
    }
}
