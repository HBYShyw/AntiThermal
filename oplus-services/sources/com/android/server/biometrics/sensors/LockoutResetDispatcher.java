package com.android.server.biometrics.sensors;

import android.content.Context;
import android.hardware.biometrics.IBiometricServiceLockoutResetCallback;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class LockoutResetDispatcher implements IBinder.DeathRecipient {
    private static final String TAG = "LockoutResetTracker";

    @VisibleForTesting
    final CopyOnWriteArrayList<ClientCallback> mClientCallbacks = new CopyOnWriteArrayList<>();
    private final Context mContext;

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static class ClientCallback {
        private static final long WAKELOCK_TIMEOUT_MS = 2000;
        private final IBiometricServiceLockoutResetCallback mCallback;
        private final String mOpPackageName;
        private final PowerManager.WakeLock mWakeLock;

        ClientCallback(Context context, IBiometricServiceLockoutResetCallback iBiometricServiceLockoutResetCallback, String str) {
            PowerManager powerManager = (PowerManager) context.getSystemService(PowerManager.class);
            this.mOpPackageName = str;
            this.mCallback = iBiometricServiceLockoutResetCallback;
            this.mWakeLock = powerManager.newWakeLock(1, "LockoutResetMonitor:SendLockoutReset");
        }

        void sendLockoutReset(int i) {
            if (this.mCallback != null) {
                try {
                    this.mWakeLock.acquire(WAKELOCK_TIMEOUT_MS);
                    this.mCallback.onLockoutReset(i, new IRemoteCallback.Stub() { // from class: com.android.server.biometrics.sensors.LockoutResetDispatcher.ClientCallback.1
                        public void sendResult(Bundle bundle) {
                            ClientCallback.this.releaseWakelock();
                        }
                    });
                } catch (RemoteException e) {
                    Slog.w(LockoutResetDispatcher.TAG, "Failed to invoke onLockoutReset: ", e);
                    releaseWakelock();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void releaseWakelock() {
            if (this.mWakeLock.isHeld()) {
                this.mWakeLock.release();
            }
        }
    }

    public LockoutResetDispatcher(Context context) {
        this.mContext = context;
    }

    public void addCallback(IBiometricServiceLockoutResetCallback iBiometricServiceLockoutResetCallback, String str) {
        if (iBiometricServiceLockoutResetCallback == null) {
            Slog.w(TAG, "Callback from : " + str + " is null");
            return;
        }
        this.mClientCallbacks.add(new ClientCallback(this.mContext, iBiometricServiceLockoutResetCallback, str));
        try {
            iBiometricServiceLockoutResetCallback.asBinder().linkToDeath(this, 0);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to link to death", e);
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied(IBinder iBinder) {
        Slog.e(TAG, "Callback binder died: " + iBinder);
        int i = 0;
        while (i < this.mClientCallbacks.size()) {
            if (this.mClientCallbacks.get(i).mCallback.asBinder().equals(iBinder)) {
                Slog.e(TAG, "Removing dead callback for: " + this.mClientCallbacks.get(i).mOpPackageName);
                this.mClientCallbacks.get(i).releaseWakelock();
                CopyOnWriteArrayList<ClientCallback> copyOnWriteArrayList = this.mClientCallbacks;
                copyOnWriteArrayList.remove(copyOnWriteArrayList.get(i));
                i--;
            }
            i++;
        }
    }

    public void notifyLockoutResetCallbacks(int i) {
        Iterator<ClientCallback> it = this.mClientCallbacks.iterator();
        while (it.hasNext()) {
            it.next().sendLockoutReset(i);
        }
    }
}
