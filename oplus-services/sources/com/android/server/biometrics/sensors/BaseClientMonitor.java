package com.android.server.biometrics.sensors;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import java.util.NoSuchElementException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class BaseClientMonitor implements IBinder.DeathRecipient {
    protected static final boolean DEBUG = true;
    private static final String TAG = "BaseClientMonitor";
    private static int sCount;
    private final BiometricContext mBiometricContext;
    private final Context mContext;
    private final int mCookie;
    private ClientMonitorCallbackConverter mListener;
    private final BiometricLogger mLogger;
    private final String mOwner;
    private long mRequestId;
    private final int mSensorId;
    private final int mSequentialId;
    private final int mTargetUserId;
    private IBinder mToken;
    public IBaseClientMonitorExt mBaseClientMonitorExt = (IBaseClientMonitorExt) ExtLoader.type(IBaseClientMonitorExt.class).base(this).create();
    private boolean mAlreadyDone = false;
    protected ClientMonitorCallback mCallback = new ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.BaseClientMonitor.1
        @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
        public void onClientStarted(BaseClientMonitor baseClientMonitor) {
            Slog.e(BaseClientMonitor.TAG, "mCallback onClientStarted: called before set (should not happen)");
        }

        @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
        public void onClientFinished(BaseClientMonitor baseClientMonitor, boolean z) {
            Slog.e(BaseClientMonitor.TAG, "mCallback onClientFinished: called before set (should not happen)");
        }
    };

    public abstract int getProtoEnum();

    public boolean interruptsPrecedingClients() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isCryptoOperation() {
        return false;
    }

    public boolean isInterruptable() {
        return false;
    }

    protected ClientMonitorCallback wrapCallbackForStart(ClientMonitorCallback clientMonitorCallback) {
        return clientMonitorCallback;
    }

    public BaseClientMonitor(Context context, IBinder iBinder, ClientMonitorCallbackConverter clientMonitorCallbackConverter, int i, String str, int i2, int i3, BiometricLogger biometricLogger, BiometricContext biometricContext) {
        int i4 = sCount;
        sCount = i4 + 1;
        this.mSequentialId = i4;
        this.mContext = context;
        this.mToken = iBinder;
        this.mRequestId = -1L;
        this.mListener = clientMonitorCallbackConverter;
        this.mTargetUserId = i;
        this.mOwner = str;
        this.mCookie = i2;
        this.mSensorId = i3;
        this.mLogger = biometricLogger;
        this.mBiometricContext = biometricContext;
        if (iBinder != null) {
            try {
                iBinder.linkToDeath(this, 0);
            } catch (RemoteException e) {
                Slog.w(TAG, "caught remote exception in linkToDeath: ", e);
            }
        }
    }

    public void waitForCookie(ClientMonitorCallback clientMonitorCallback) {
        this.mCallback = clientMonitorCallback;
    }

    public void start(ClientMonitorCallback clientMonitorCallback) {
        ClientMonitorCallback wrapCallbackForStart = wrapCallbackForStart(clientMonitorCallback);
        this.mCallback = wrapCallbackForStart;
        wrapCallbackForStart.onClientStarted(this);
    }

    public void setIsAlreadyDone(boolean z) {
        this.mAlreadyDone = z;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void destroy() {
        this.mAlreadyDone = true;
        IBinder iBinder = this.mToken;
        if (iBinder != null) {
            try {
                iBinder.unlinkToDeath(this, 0);
            } catch (NoSuchElementException unused) {
                Slog.e(TAG, "destroy(): " + this + ":", new Exception("here"));
            }
            this.mToken = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void markAlreadyDone() {
        Slog.d(TAG, "marking operation as done: " + this);
        this.mAlreadyDone = true;
    }

    public boolean isAlreadyDone() {
        return this.mAlreadyDone;
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        binderDiedInternal(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void binderDiedInternal(boolean z) {
        Slog.e(TAG, "Binder died, operation: " + this);
        if (this.mAlreadyDone) {
            Slog.w(TAG, "Binder died but client is finished, ignoring");
            return;
        }
        if (isInterruptable()) {
            Slog.e(TAG, "Binder died, cancelling client");
            cancel();
        }
        this.mToken = null;
        if (z) {
            this.mListener = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BiometricContext getBiometricContext() {
        return this.mBiometricContext;
    }

    public BiometricLogger getLogger() {
        return this.mLogger;
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final String getOwnerString() {
        return this.mOwner;
    }

    public final ClientMonitorCallbackConverter getListener() {
        return this.mListener;
    }

    public int getTargetUserId() {
        return this.mTargetUserId;
    }

    public final IBinder getToken() {
        return this.mToken;
    }

    public int getSensorId() {
        return this.mSensorId;
    }

    public int getCookie() {
        return this.mCookie;
    }

    public long getRequestId() {
        return this.mRequestId;
    }

    public boolean hasRequestId() {
        return this.mRequestId > 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setRequestId(long j) {
        if (j <= 0) {
            throw new IllegalArgumentException("request id must be positive");
        }
        this.mRequestId = j;
    }

    @VisibleForTesting
    public ClientMonitorCallback getCallback() {
        return this.mCallback;
    }

    public String toString() {
        return "{[" + this.mSequentialId + "] " + getClass().getName() + ", proto=" + getProtoEnum() + ", owner=" + getOwnerString() + ", cookie=" + getCookie() + ", requestId=" + getRequestId() + ", userId=" + getTargetUserId() + "}";
    }

    public void cancel() {
        cancelWithoutStarting(this.mCallback);
    }

    public void cancelWithoutStarting(ClientMonitorCallback clientMonitorCallback) {
        Slog.d(TAG, "cancelWithoutStarting: " + this);
        try {
            ClientMonitorCallbackConverter listener = getListener();
            if (listener != null) {
                listener.onError(getSensorId(), getCookie(), 5, 0);
            }
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to invoke sendError", e);
        }
        clientMonitorCallback.onClientFinished(this, true);
    }
}
