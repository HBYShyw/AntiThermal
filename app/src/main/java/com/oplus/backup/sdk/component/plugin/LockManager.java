package com.oplus.backup.sdk.component.plugin;

import android.util.Log;
import java.util.HashMap;

/* loaded from: classes.dex */
public class LockManager {
    private static final String TAG = "LockManager";
    private static LockManager sInstance;
    private volatile HashMap<String, BooleanLock> mLockMap = new HashMap<>();

    /* loaded from: classes.dex */
    public static class BooleanLock {
        private final String mKey;
        private boolean mIsOK = false;
        private Object mResult = null;

        public BooleanLock(String str) {
            this.mKey = str;
        }

        public String getKey() {
            return this.mKey;
        }

        public Object getResult() {
            return this.mResult;
        }

        public int hashCode() {
            return this.mKey.hashCode();
        }

        public boolean isOK() {
            return this.mIsOK;
        }

        public void setOK(boolean z10) {
            this.mIsOK = z10;
        }

        public void setResult(Object obj) {
            this.mResult = obj;
        }
    }

    private LockManager() {
    }

    public static LockManager getInstance() {
        if (sInstance == null) {
            synchronized (LockManager.class) {
                if (sInstance == null) {
                    sInstance = new LockManager();
                }
            }
        }
        return sInstance;
    }

    public BooleanLock applyLock(String... strArr) {
        BooleanLock booleanLock;
        synchronized (this.mLockMap) {
            StringBuilder sb2 = new StringBuilder();
            for (String str : strArr) {
                sb2.append(str);
                sb2.append("|");
            }
            String sb3 = sb2.toString();
            while (this.mLockMap.containsKey(sb3)) {
                Log.w(TAG, "applyLock the same lock is used, waitting for release. Lock: " + this.mLockMap.get(sb3));
                try {
                    this.mLockMap.wait(1000L);
                } catch (InterruptedException e10) {
                    Log.e(TAG, "applyLock, e =" + e10.getMessage());
                }
            }
            booleanLock = new BooleanLock(sb3);
            this.mLockMap.put(sb3, booleanLock);
        }
        return booleanLock;
    }

    public BooleanLock getLock(String... strArr) {
        BooleanLock booleanLock;
        synchronized (this.mLockMap) {
            StringBuilder sb2 = new StringBuilder();
            for (String str : strArr) {
                sb2.append(str);
                sb2.append("|");
            }
            booleanLock = this.mLockMap.get(sb2.toString());
        }
        return booleanLock;
    }

    public void notifyLock(BooleanLock booleanLock) {
        if (booleanLock != null) {
            synchronized (booleanLock) {
                booleanLock.setOK(true);
                booleanLock.notify();
            }
        }
    }

    public Object waitResult(BooleanLock booleanLock) {
        if (booleanLock == null) {
            return null;
        }
        synchronized (booleanLock) {
            while (!booleanLock.isOK()) {
                try {
                    booleanLock.wait(100L);
                } catch (InterruptedException e10) {
                    Log.e(TAG, "waitResult, e =" + e10.getMessage());
                }
            }
        }
        Object result = booleanLock.getResult();
        synchronized (this.mLockMap) {
            this.mLockMap.remove(booleanLock.getKey());
            this.mLockMap.notify();
            Log.i(TAG, "waitResult over, release lock: " + booleanLock);
        }
        return result;
    }
}
