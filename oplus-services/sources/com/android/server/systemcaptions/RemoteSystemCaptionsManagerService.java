package com.android.server.systemcaptions;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class RemoteSystemCaptionsManagerService {
    private static final int MSG_BIND = 1;
    private static final String SERVICE_INTERFACE = "android.service.systemcaptions.SystemCaptionsManagerService";
    private static final String TAG = "RemoteSystemCaptionsManagerService";
    private final ComponentName mComponentName;
    private final Context mContext;
    private final Intent mIntent;

    @GuardedBy({"mLock"})
    private IBinder mService;
    private final int mUserId;
    private final boolean mVerbose;
    private final Object mLock = new Object();
    private final RemoteServiceConnection mServiceConnection = new RemoteServiceConnection();

    @GuardedBy({"mLock"})
    private boolean mBinding = false;

    @GuardedBy({"mLock"})
    private boolean mDestroyed = false;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteSystemCaptionsManagerService(Context context, ComponentName componentName, int i, boolean z) {
        this.mContext = context;
        this.mComponentName = componentName;
        this.mUserId = i;
        this.mVerbose = z;
        this.mIntent = new Intent(SERVICE_INTERFACE).setComponent(componentName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initialize() {
        if (this.mVerbose) {
            Slog.v(TAG, "initialize()");
        }
        scheduleBind();
    }

    public void destroy() {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.systemcaptions.RemoteSystemCaptionsManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((RemoteSystemCaptionsManagerService) obj).handleDestroy();
            }
        }, this));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleDestroy() {
        if (this.mVerbose) {
            Slog.v(TAG, "handleDestroy()");
        }
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                if (this.mVerbose) {
                    Slog.v(TAG, "handleDestroy(): Already destroyed");
                }
            } else {
                this.mDestroyed = true;
                ensureUnboundLocked();
            }
        }
    }

    boolean isDestroyed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mDestroyed;
        }
        return z;
    }

    private void scheduleBind() {
        if (this.mHandler.hasMessages(1)) {
            if (this.mVerbose) {
                Slog.v(TAG, "scheduleBind(): already scheduled");
                return;
            }
            return;
        }
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.systemcaptions.RemoteSystemCaptionsManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((RemoteSystemCaptionsManagerService) obj).handleEnsureBound();
            }
        }, this).setWhat(1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEnsureBound() {
        synchronized (this.mLock) {
            if (this.mService == null && !this.mBinding) {
                if (this.mVerbose) {
                    Slog.v(TAG, "handleEnsureBound(): binding");
                }
                this.mBinding = true;
                if (!this.mContext.bindServiceAsUser(this.mIntent, this.mServiceConnection, 67112961, this.mHandler, new UserHandle(this.mUserId))) {
                    Slog.w(TAG, "Could not bind to " + this.mIntent + " with flags 67112961");
                    this.mBinding = false;
                    this.mService = null;
                }
            }
        }
    }

    @GuardedBy({"mLock"})
    private void ensureUnboundLocked() {
        if (this.mService != null || this.mBinding) {
            this.mBinding = false;
            this.mService = null;
            if (this.mVerbose) {
                Slog.v(TAG, "ensureUnbound(): unbinding");
            }
            this.mContext.unbindService(this.mServiceConnection);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class RemoteServiceConnection implements ServiceConnection {
        private RemoteServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            synchronized (RemoteSystemCaptionsManagerService.this.mLock) {
                if (RemoteSystemCaptionsManagerService.this.mVerbose) {
                    Slog.v(RemoteSystemCaptionsManagerService.TAG, "onServiceConnected()");
                }
                if (!RemoteSystemCaptionsManagerService.this.mDestroyed && RemoteSystemCaptionsManagerService.this.mBinding) {
                    RemoteSystemCaptionsManagerService.this.mBinding = false;
                    RemoteSystemCaptionsManagerService.this.mService = iBinder;
                    return;
                }
                Slog.wtf(RemoteSystemCaptionsManagerService.TAG, "onServiceConnected() dispatched after unbindService");
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            synchronized (RemoteSystemCaptionsManagerService.this.mLock) {
                if (RemoteSystemCaptionsManagerService.this.mVerbose) {
                    Slog.v(RemoteSystemCaptionsManagerService.TAG, "onServiceDisconnected()");
                }
                RemoteSystemCaptionsManagerService.this.mBinding = true;
                RemoteSystemCaptionsManagerService.this.mService = null;
            }
        }
    }
}
