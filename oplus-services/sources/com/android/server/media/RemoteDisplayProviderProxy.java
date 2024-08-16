package com.android.server.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.IRemoteDisplayCallback;
import android.media.IRemoteDisplayProvider;
import android.media.RemoteDisplayState;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.util.Slog;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class RemoteDisplayProviderProxy implements ServiceConnection {
    private Connection mActiveConnection;
    private boolean mBound;
    private final ComponentName mComponentName;
    private boolean mConnectionReady;
    private final Context mContext;
    private int mDiscoveryMode;
    private RemoteDisplayState mDisplayState;
    private Callback mDisplayStateCallback;
    private final Runnable mDisplayStateChanged = new Runnable() { // from class: com.android.server.media.RemoteDisplayProviderProxy.1
        @Override // java.lang.Runnable
        public void run() {
            RemoteDisplayProviderProxy.this.mScheduledDisplayStateChangedCallback = false;
            if (RemoteDisplayProviderProxy.this.mDisplayStateCallback != null) {
                Callback callback = RemoteDisplayProviderProxy.this.mDisplayStateCallback;
                RemoteDisplayProviderProxy remoteDisplayProviderProxy = RemoteDisplayProviderProxy.this;
                callback.onDisplayStateChanged(remoteDisplayProviderProxy, remoteDisplayProviderProxy.mDisplayState);
            }
        }
    };
    private final Handler mHandler = new Handler();
    private boolean mRunning;
    private boolean mScheduledDisplayStateChangedCallback;
    private String mSelectedDisplayId;
    private final int mUserId;
    private static final String TAG = "RemoteDisplayProvider";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Callback {
        void onDisplayStateChanged(RemoteDisplayProviderProxy remoteDisplayProviderProxy, RemoteDisplayState remoteDisplayState);
    }

    public RemoteDisplayProviderProxy(Context context, ComponentName componentName, int i) {
        this.mContext = context;
        this.mComponentName = componentName;
        this.mUserId = i;
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + "Proxy");
        printWriter.println(str + "  mUserId=" + this.mUserId);
        printWriter.println(str + "  mRunning=" + this.mRunning);
        printWriter.println(str + "  mBound=" + this.mBound);
        printWriter.println(str + "  mActiveConnection=" + this.mActiveConnection);
        printWriter.println(str + "  mConnectionReady=" + this.mConnectionReady);
        printWriter.println(str + "  mDiscoveryMode=" + this.mDiscoveryMode);
        printWriter.println(str + "  mSelectedDisplayId=" + this.mSelectedDisplayId);
        printWriter.println(str + "  mDisplayState=" + this.mDisplayState);
    }

    public void setCallback(Callback callback) {
        this.mDisplayStateCallback = callback;
    }

    public RemoteDisplayState getDisplayState() {
        return this.mDisplayState;
    }

    public void setDiscoveryMode(int i) {
        if (this.mDiscoveryMode != i) {
            this.mDiscoveryMode = i;
            if (this.mConnectionReady) {
                this.mActiveConnection.setDiscoveryMode(i);
            }
            updateBinding();
        }
    }

    public void setSelectedDisplay(String str) {
        String str2;
        if (Objects.equals(this.mSelectedDisplayId, str)) {
            return;
        }
        if (this.mConnectionReady && (str2 = this.mSelectedDisplayId) != null) {
            this.mActiveConnection.disconnect(str2);
        }
        this.mSelectedDisplayId = str;
        if (this.mConnectionReady && str != null) {
            this.mActiveConnection.connect(str);
        }
        updateBinding();
    }

    public void setDisplayVolume(int i) {
        String str;
        if (!this.mConnectionReady || (str = this.mSelectedDisplayId) == null) {
            return;
        }
        this.mActiveConnection.setVolume(str, i);
    }

    public void adjustDisplayVolume(int i) {
        String str;
        if (!this.mConnectionReady || (str = this.mSelectedDisplayId) == null) {
            return;
        }
        this.mActiveConnection.adjustVolume(str, i);
    }

    public boolean hasComponentName(String str, String str2) {
        return this.mComponentName.getPackageName().equals(str) && this.mComponentName.getClassName().equals(str2);
    }

    public String getFlattenedComponentName() {
        return this.mComponentName.flattenToShortString();
    }

    public void start() {
        if (this.mRunning) {
            return;
        }
        if (DEBUG) {
            Slog.d(TAG, this + ": Starting");
        }
        this.mRunning = true;
        updateBinding();
    }

    public void stop() {
        if (this.mRunning) {
            if (DEBUG) {
                Slog.d(TAG, this + ": Stopping");
            }
            this.mRunning = false;
            updateBinding();
        }
    }

    public void rebindIfDisconnected() {
        if (this.mActiveConnection == null && shouldBind()) {
            unbind();
            bind();
        }
    }

    private void updateBinding() {
        if (shouldBind()) {
            bind();
        } else {
            unbind();
        }
    }

    private boolean shouldBind() {
        if (this.mRunning) {
            return (this.mDiscoveryMode == 0 && this.mSelectedDisplayId == null) ? false : true;
        }
        return false;
    }

    private void bind() {
        if (this.mBound) {
            return;
        }
        boolean z = DEBUG;
        if (z) {
            Slog.d(TAG, this + ": Binding");
        }
        Intent intent = new Intent("com.android.media.remotedisplay.RemoteDisplayProvider");
        intent.setComponent(this.mComponentName);
        try {
            boolean bindServiceAsUser = this.mContext.bindServiceAsUser(intent, this, 67108865, new UserHandle(this.mUserId));
            this.mBound = bindServiceAsUser;
            if (bindServiceAsUser || !z) {
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
            disconnect();
            this.mContext.unbindService(this);
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        boolean z = DEBUG;
        if (z) {
            Slog.d(TAG, this + ": Connected");
        }
        if (this.mBound) {
            disconnect();
            IRemoteDisplayProvider asInterface = IRemoteDisplayProvider.Stub.asInterface(iBinder);
            if (asInterface != null) {
                Connection connection = new Connection(asInterface);
                if (connection.register()) {
                    this.mActiveConnection = connection;
                    return;
                } else {
                    if (z) {
                        Slog.d(TAG, this + ": Registration failed");
                        return;
                    }
                    return;
                }
            }
            Slog.e(TAG, this + ": Service returned invalid remote display provider binder");
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        if (DEBUG) {
            Slog.d(TAG, this + ": Service disconnected");
        }
        disconnect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConnectionReady(Connection connection) {
        Connection connection2 = this.mActiveConnection;
        if (connection2 == connection) {
            this.mConnectionReady = true;
            int i = this.mDiscoveryMode;
            if (i != 0) {
                connection2.setDiscoveryMode(i);
            }
            String str = this.mSelectedDisplayId;
            if (str != null) {
                this.mActiveConnection.connect(str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConnectionDied(Connection connection) {
        if (this.mActiveConnection == connection) {
            if (DEBUG) {
                Slog.d(TAG, this + ": Service connection died");
            }
            disconnect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDisplayStateChanged(Connection connection, RemoteDisplayState remoteDisplayState) {
        if (this.mActiveConnection == connection) {
            if (DEBUG) {
                Slog.d(TAG, this + ": State changed, state=" + remoteDisplayState);
            }
            setDisplayState(remoteDisplayState);
        }
    }

    private void disconnect() {
        Connection connection = this.mActiveConnection;
        if (connection != null) {
            String str = this.mSelectedDisplayId;
            if (str != null) {
                connection.disconnect(str);
            }
            this.mConnectionReady = false;
            this.mActiveConnection.dispose();
            this.mActiveConnection = null;
            setDisplayState(null);
        }
    }

    private void setDisplayState(RemoteDisplayState remoteDisplayState) {
        if (Objects.equals(this.mDisplayState, remoteDisplayState)) {
            return;
        }
        this.mDisplayState = remoteDisplayState;
        if (this.mScheduledDisplayStateChangedCallback) {
            return;
        }
        this.mScheduledDisplayStateChangedCallback = true;
        this.mHandler.post(this.mDisplayStateChanged);
    }

    public String toString() {
        return "Service connection " + this.mComponentName.flattenToShortString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class Connection implements IBinder.DeathRecipient {
        private final ProviderCallback mCallback = new ProviderCallback(this);
        private final IRemoteDisplayProvider mProvider;

        public Connection(IRemoteDisplayProvider iRemoteDisplayProvider) {
            this.mProvider = iRemoteDisplayProvider;
        }

        public boolean register() {
            try {
                this.mProvider.asBinder().linkToDeath(this, 0);
                this.mProvider.setCallback(this.mCallback);
                RemoteDisplayProviderProxy.this.mHandler.post(new Runnable() { // from class: com.android.server.media.RemoteDisplayProviderProxy.Connection.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Connection connection = Connection.this;
                        RemoteDisplayProviderProxy.this.onConnectionReady(connection);
                    }
                });
                return true;
            } catch (RemoteException unused) {
                binderDied();
                return false;
            }
        }

        public void dispose() {
            this.mProvider.asBinder().unlinkToDeath(this, 0);
            this.mCallback.dispose();
        }

        public void setDiscoveryMode(int i) {
            try {
                this.mProvider.setDiscoveryMode(i);
            } catch (RemoteException e) {
                Slog.e(RemoteDisplayProviderProxy.TAG, "Failed to deliver request to set discovery mode.", e);
            }
        }

        public void connect(String str) {
            try {
                this.mProvider.connect(str);
            } catch (RemoteException e) {
                Slog.e(RemoteDisplayProviderProxy.TAG, "Failed to deliver request to connect to display.", e);
            }
        }

        public void disconnect(String str) {
            try {
                this.mProvider.disconnect(str);
            } catch (RemoteException e) {
                Slog.e(RemoteDisplayProviderProxy.TAG, "Failed to deliver request to disconnect from display.", e);
            }
        }

        public void setVolume(String str, int i) {
            try {
                this.mProvider.setVolume(str, i);
            } catch (RemoteException e) {
                Slog.e(RemoteDisplayProviderProxy.TAG, "Failed to deliver request to set display volume.", e);
            }
        }

        public void adjustVolume(String str, int i) {
            try {
                this.mProvider.adjustVolume(str, i);
            } catch (RemoteException e) {
                Slog.e(RemoteDisplayProviderProxy.TAG, "Failed to deliver request to adjust display volume.", e);
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            RemoteDisplayProviderProxy.this.mHandler.post(new Runnable() { // from class: com.android.server.media.RemoteDisplayProviderProxy.Connection.2
                @Override // java.lang.Runnable
                public void run() {
                    Connection connection = Connection.this;
                    RemoteDisplayProviderProxy.this.onConnectionDied(connection);
                }
            });
        }

        void postStateChanged(final RemoteDisplayState remoteDisplayState) {
            RemoteDisplayProviderProxy.this.mHandler.post(new Runnable() { // from class: com.android.server.media.RemoteDisplayProviderProxy.Connection.3
                @Override // java.lang.Runnable
                public void run() {
                    Connection connection = Connection.this;
                    RemoteDisplayProviderProxy.this.onDisplayStateChanged(connection, remoteDisplayState);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ProviderCallback extends IRemoteDisplayCallback.Stub {
        private final WeakReference<Connection> mConnectionRef;

        public ProviderCallback(Connection connection) {
            this.mConnectionRef = new WeakReference<>(connection);
        }

        public void dispose() {
            this.mConnectionRef.clear();
        }

        public void onStateChanged(RemoteDisplayState remoteDisplayState) throws RemoteException {
            Connection connection = this.mConnectionRef.get();
            if (connection != null) {
                connection.postStateChanged(remoteDisplayState);
            }
        }
    }
}
