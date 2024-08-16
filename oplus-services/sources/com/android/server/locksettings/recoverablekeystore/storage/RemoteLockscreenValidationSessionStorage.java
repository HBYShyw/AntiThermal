package com.android.server.locksettings.recoverablekeystore.storage;

import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.security.SecureBox;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RemoteLockscreenValidationSessionStorage {
    private static final long SESSION_TIMEOUT_MILLIS = 600000;
    private static final String TAG = "RemoteLockscreenValidation";

    @VisibleForTesting
    final SparseArray<LockscreenVerificationSession> mSessionsByUserId = new SparseArray<>(0);

    public LockscreenVerificationSession get(int i) {
        LockscreenVerificationSession lockscreenVerificationSession;
        synchronized (this.mSessionsByUserId) {
            lockscreenVerificationSession = this.mSessionsByUserId.get(i);
        }
        return lockscreenVerificationSession;
    }

    public LockscreenVerificationSession startSession(int i) {
        LockscreenVerificationSession lockscreenVerificationSession;
        synchronized (this.mSessionsByUserId) {
            if (this.mSessionsByUserId.get(i) != null) {
                this.mSessionsByUserId.delete(i);
            }
            try {
                lockscreenVerificationSession = new LockscreenVerificationSession(SecureBox.genKeyPair(), SystemClock.elapsedRealtime());
                this.mSessionsByUserId.put(i, lockscreenVerificationSession);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return lockscreenVerificationSession;
    }

    public void finishSession(int i) {
        synchronized (this.mSessionsByUserId) {
            this.mSessionsByUserId.delete(i);
        }
    }

    public Runnable getLockscreenValidationCleanupTask() {
        return new LockscreenValidationCleanupTask();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class LockscreenVerificationSession {
        private final long mElapsedStartTime;
        private final KeyPair mKeyPair;

        LockscreenVerificationSession(KeyPair keyPair, long j) {
            this.mKeyPair = keyPair;
            this.mElapsedStartTime = j;
        }

        public KeyPair getKeyPair() {
            return this.mKeyPair;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long getElapsedStartTimeMillis() {
            return this.mElapsedStartTime;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class LockscreenValidationCleanupTask implements Runnable {
        private LockscreenValidationCleanupTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                synchronized (RemoteLockscreenValidationSessionStorage.this.mSessionsByUserId) {
                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < RemoteLockscreenValidationSessionStorage.this.mSessionsByUserId.size(); i++) {
                        if (SystemClock.elapsedRealtime() - RemoteLockscreenValidationSessionStorage.this.mSessionsByUserId.valueAt(i).getElapsedStartTimeMillis() > 600000) {
                            arrayList.add(Integer.valueOf(RemoteLockscreenValidationSessionStorage.this.mSessionsByUserId.keyAt(i)));
                        }
                    }
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        RemoteLockscreenValidationSessionStorage.this.mSessionsByUserId.delete(((Integer) it.next()).intValue());
                    }
                }
            } catch (Exception e) {
                Log.e(RemoteLockscreenValidationSessionStorage.TAG, "Unexpected exception thrown during LockscreenValidationCleanupTask", e);
            }
        }
    }
}
