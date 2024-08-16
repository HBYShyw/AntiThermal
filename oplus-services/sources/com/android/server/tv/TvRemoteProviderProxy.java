package com.android.server.tv;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.tv.ITvRemoteProvider;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.util.Slog;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class TvRemoteProviderProxy implements ServiceConnection {
    protected static final String SERVICE_INTERFACE = "com.android.media.tv.remoteprovider.TvRemoteProvider";
    private boolean mBound;
    private final ComponentName mComponentName;
    private boolean mConnected;
    private final Context mContext;
    private final Object mLock;
    private boolean mRunning;
    private final int mUid;
    private final int mUserId;
    private static final String TAG = "TvRemoteProviderProxy";
    private static final boolean DEBUG = Log.isLoggable(TAG, 2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public TvRemoteProviderProxy(Context context, Object obj, ComponentName componentName, int i, int i2) {
        this.mContext = context;
        this.mLock = obj;
        this.mComponentName = componentName;
        this.mUserId = i;
        this.mUid = i2;
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + "Proxy");
        printWriter.println(str + "  mUserId=" + this.mUserId);
        printWriter.println(str + "  mRunning=" + this.mRunning);
        printWriter.println(str + "  mBound=" + this.mBound);
        printWriter.println(str + "  mConnected=" + this.mConnected);
    }

    public boolean hasComponentName(String str, String str2) {
        return this.mComponentName.getPackageName().equals(str) && this.mComponentName.getClassName().equals(str2);
    }

    public void start() {
        if (this.mRunning) {
            return;
        }
        if (DEBUG) {
            Slog.d(TAG, this + ": Starting");
        }
        this.mRunning = true;
        bind();
    }

    public void stop() {
        if (this.mRunning) {
            if (DEBUG) {
                Slog.d(TAG, this + ": Stopping");
            }
            this.mRunning = false;
            unbind();
        }
    }

    public void rebindIfDisconnected() {
        if (!this.mRunning || this.mConnected) {
            return;
        }
        unbind();
        bind();
    }

    private void bind() {
        if (this.mBound) {
            return;
        }
        boolean z = DEBUG;
        if (z) {
            Slog.d(TAG, this + ": Binding");
        }
        Intent intent = new Intent(SERVICE_INTERFACE);
        intent.setComponent(this.mComponentName);
        try {
            boolean bindServiceAsUser = this.mContext.bindServiceAsUser(intent, this, 67108865, new UserHandle(this.mUserId));
            this.mBound = bindServiceAsUser;
            if (!z || bindServiceAsUser) {
                return;
            }
            Slog.d(TAG, this + ": Bind failed");
        } catch (SecurityException e) {
            if (DEBUG) {
                Slog.d(TAG, this + ": Bind failed", e);
            }
        }
    }

    private void unbind() {
        if (this.mBound) {
            if (DEBUG) {
                Slog.d(TAG, this + ": Unbinding");
            }
            this.mBound = false;
            this.mContext.unbindService(this);
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (DEBUG) {
            Slog.d(TAG, this + ": onServiceConnected()");
        }
        this.mConnected = true;
        ITvRemoteProvider asInterface = ITvRemoteProvider.Stub.asInterface(iBinder);
        if (asInterface == null) {
            Slog.e(TAG, this + ": Invalid binder");
            return;
        }
        try {
            asInterface.setRemoteServiceInputSink(new TvRemoteServiceInput(this.mLock, asInterface));
        } catch (RemoteException unused) {
            Slog.e(TAG, this + ": Failed remote call to setRemoteServiceInputSink");
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        this.mConnected = false;
        if (DEBUG) {
            Slog.d(TAG, this + ": onServiceDisconnected()");
        }
    }
}
