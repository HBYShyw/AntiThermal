package com.oplus.deepthinker.platform.server;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.UserHandle;
import com.oplus.deepthinker.OplusDeepThinkerManagerDecor;
import com.oplus.deepthinker.sdk.common.utils.SDKLog;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes.dex */
public class ClientConnection {
    private static final String ACTION = "deepthinker.intent.action.BIND_INTERFACE_SERVER";
    private static final int COUNT = 1;
    private static final String PKG = "com.oplus.deepthinker";
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECTED = 3;
    private static final int STATE_INIT = 0;
    private static final String TAG = "ClientConnection";
    private static final String THREAD_NAME = "deepthinker-ct";
    private static final int WAIT_TIME_OUT_SECONDS = 2;
    private final Context mContext;
    private CountDownLatch mCountDownLatch;
    private final Executor mExecutor;
    private Handler mHandler;
    private volatile FrameworkInvokeDelegate mInvokeDelegate;
    private boolean mIsSystemUser;
    private UserHandle mMyUser;
    private final ReadWriteLock BINDER_LOCK = new ReentrantReadWriteLock();
    private volatile int mState = 0;
    private final ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.oplus.deepthinker.platform.server.ClientConnection.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            SDKLog.i(ClientConnection.TAG, "onServiceConnected " + (service != null));
            if (service == null) {
                return;
            }
            ClientConnection.this.BINDER_LOCK.writeLock().lock();
            try {
                try {
                    ClientConnection.this.mInvokeDelegate = new FrameworkInvokeDelegate(service);
                    service.linkToDeath(ClientConnection.this.mDeathRecipient, 0);
                    if (ClientConnection.this.mInvokeDelegate != null) {
                        ClientConnection.this.mState = 2;
                    } else {
                        ClientConnection.this.mState = 3;
                    }
                    ClientConnection.this.BINDER_LOCK.writeLock().unlock();
                    if (ClientConnection.this.mCountDownLatch == null) {
                        return;
                    }
                } catch (Exception e) {
                    SDKLog.e(ClientConnection.TAG, "onServiceConnected: ", e);
                    if (ClientConnection.this.mInvokeDelegate != null) {
                        ClientConnection.this.mState = 2;
                    } else {
                        ClientConnection.this.mState = 3;
                    }
                    ClientConnection.this.BINDER_LOCK.writeLock().unlock();
                    if (ClientConnection.this.mCountDownLatch == null) {
                        return;
                    }
                }
                ClientConnection.this.mCountDownLatch.countDown();
            } catch (Throwable th) {
                if (ClientConnection.this.mInvokeDelegate != null) {
                    ClientConnection.this.mState = 2;
                } else {
                    ClientConnection.this.mState = 3;
                }
                ClientConnection.this.BINDER_LOCK.writeLock().unlock();
                if (ClientConnection.this.mCountDownLatch != null) {
                    ClientConnection.this.mCountDownLatch.countDown();
                }
                throw th;
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            SDKLog.i(ClientConnection.TAG, "onServiceDisconnected");
            ClientConnection.this.serviceDied();
        }
    };
    private final IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.deepthinker.platform.server.ClientConnection.2
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            SDKLog.i(ClientConnection.TAG, "binderDied");
            ClientConnection.this.serviceDied();
        }
    };

    public ClientConnection(Context context, Executor executor) {
        this.mContext = context.getApplicationContext();
        this.mExecutor = executor;
        initIsSystemUser();
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x002a  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0038  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public FrameworkInvokeDelegate getInvokeDelegate() {
        boolean z;
        boolean needConn;
        SDKLog.i(TAG, "getInvokeDelegate start");
        if (this.mInvokeDelegate == null) {
            this.BINDER_LOCK.readLock().lock();
            try {
                if (this.mState != 2 && this.mState != 1) {
                    z = false;
                    boolean needWait = z;
                    needConn = this.mInvokeDelegate == null;
                    if (needConn) {
                        if (needWait) {
                            try {
                                CountDownLatch countDownLatch = this.mCountDownLatch;
                                if (countDownLatch != null) {
                                    countDownLatch.await(2L, TimeUnit.SECONDS);
                                }
                            } catch (Exception e) {
                                SDKLog.w(TAG, "tryConnect: " + e);
                            }
                        } else {
                            tryConnect();
                        }
                    }
                }
                z = true;
                boolean needWait2 = z;
                needConn = this.mInvokeDelegate == null;
                if (needConn) {
                }
            } finally {
                this.BINDER_LOCK.readLock().unlock();
            }
        }
        FrameworkInvokeDelegate delegate = this.mInvokeDelegate;
        SDKLog.i(TAG, "getInvokeDelegate end");
        return delegate;
    }

    private void initIsSystemUser() {
        if (Process.myUid() == 1000) {
            this.mIsSystemUser = true;
            return;
        }
        try {
            PackageInfo info = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0);
            this.mIsSystemUser = "android.uid.system".equals(info.sharedUserId);
        } catch (Exception e) {
            SDKLog.w(TAG, "initIsSystemUser " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void serviceDied() {
        this.BINDER_LOCK.writeLock().lock();
        if (this.mState == 3) {
            try {
                SDKLog.d(TAG, "serviceDied: already disconnected.");
                return;
            } finally {
                this.BINDER_LOCK.writeLock().unlock();
            }
        }
        try {
            try {
                this.mInvokeDelegate.getRemote().unlinkToDeath(this.mDeathRecipient, 0);
            } catch (Exception e) {
                SDKLog.e(TAG, "serviceDied: ", e);
            }
        } finally {
            this.mState = 3;
            this.mInvokeDelegate = null;
            this.BINDER_LOCK.writeLock().unlock();
            onServiceDied();
        }
    }

    private void onServiceDied() {
        if (isOnMainThread()) {
            this.mExecutor.execute(new Runnable() { // from class: com.oplus.deepthinker.platform.server.ClientConnection$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ClientConnection.this.deliveryOnServiceDied();
                }
            });
        } else {
            deliveryOnServiceDied();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deliveryOnServiceDied() {
        OplusDeepThinkerManagerDecor.getInstance(this.mContext).onServiceDied();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isOnMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    private void tryConnect() {
        Thread thread = new Thread(new Runnable() { // from class: com.oplus.deepthinker.platform.server.ClientConnection.3
            /* JADX WARN: Code restructure failed: missing block: B:55:0x011f, code lost:
            
                if (r7.this$0.mState == 2) goto L36;
             */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void run() {
                SDKLog.d(ClientConnection.TAG, "tryConnect: start connect on async thread.");
                if (!ClientConnection.this.isQ() && ClientConnection.this.isOnMainThread()) {
                    SDKLog.w(ClientConnection.TAG, "tryConnect: bind service on main thread, ignore ileagal usage.");
                    return;
                }
                ClientConnection.this.BINDER_LOCK.writeLock().lock();
                if (ClientConnection.this.mState == 2) {
                    try {
                        SDKLog.w(ClientConnection.TAG, "tryConnect: Already connected, do not reconnect again.");
                        return;
                    } finally {
                    }
                }
                if (ClientConnection.this.mState == 1) {
                    try {
                        SDKLog.w(ClientConnection.TAG, "tryConnect: Already do connecting, do not reconnect again.");
                        ClientConnection.this.BINDER_LOCK.writeLock().unlock();
                        if (ClientConnection.this.mCountDownLatch != null) {
                            try {
                                ClientConnection.this.mCountDownLatch.await(2L, TimeUnit.SECONDS);
                                return;
                            } catch (InterruptedException e) {
                                SDKLog.e(ClientConnection.TAG, "tryConnect: wait to be connected error: ", e);
                                return;
                            }
                        }
                        return;
                    } finally {
                    }
                }
                try {
                    ClientConnection.this.mState = 1;
                    ClientConnection.this.mCountDownLatch = new CountDownLatch(1);
                    ClientConnection.this.BINDER_LOCK.writeLock().unlock();
                    SDKLog.d(ClientConnection.TAG, "tryConnect: start bind service");
                    if (ClientConnection.this.bindServiceByPlatform()) {
                        try {
                            try {
                                ClientConnection.this.mCountDownLatch.await(2L, TimeUnit.SECONDS);
                            } catch (InterruptedException e2) {
                                SDKLog.e(ClientConnection.TAG, "tryConnect: connectBinderPoolService error: ", e2);
                                ClientConnection.this.BINDER_LOCK.writeLock().lock();
                            }
                        } finally {
                            ClientConnection.this.BINDER_LOCK.writeLock().lock();
                            if (ClientConnection.this.mState != 2) {
                                ClientConnection.this.mState = 3;
                            }
                        }
                    } else {
                        ClientConnection.this.BINDER_LOCK.writeLock().lock();
                        try {
                            ClientConnection.this.mState = 3;
                            ClientConnection.this.BINDER_LOCK.writeLock().unlock();
                            if (ClientConnection.this.mCountDownLatch != null) {
                                ClientConnection.this.mCountDownLatch.countDown();
                            }
                            SDKLog.w(ClientConnection.TAG, "tryConnect: Bind Algorithm Service Failed!");
                        } finally {
                        }
                    }
                    SDKLog.d(ClientConnection.TAG, "tryConnect: end connect on async thread.");
                } finally {
                }
            }
        }, THREAD_NAME);
        thread.start();
        boolean onMain = isOnMainThread();
        if (isQ() || !onMain) {
            try {
                thread.join(2000L);
            } catch (InterruptedException e) {
                SDKLog.e(TAG, "tryConnect: interrupted.", e);
            }
            SDKLog.i(TAG, "tryConnect: end. On " + (onMain ? "Main" : "Async") + " Thread, connect " + (this.mState == 2 ? "success." : "timeout."));
            return;
        }
        SDKLog.w(TAG, "tryConnect: end. On Main Thread, reutrn directly.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean bindServiceByPlatform() {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.setPackage("com.oplus.deepthinker");
        return bindService(intent, this.mServiceConnection);
    }

    private boolean bindService(Intent intent, ServiceConnection connection) {
        if (this.mIsSystemUser) {
            if (this.mMyUser == null) {
                this.mMyUser = Process.myUserHandle();
            }
            if (this.mHandler == null) {
                try {
                    HandlerThread thread = new HandlerThread("deepthinker_sdk_inner");
                    thread.start();
                    this.mHandler = new Handler(thread.getLooper());
                } catch (Exception e) {
                    SDKLog.e(TAG, "bindService: on init handle", e);
                    return true;
                }
            }
            try {
                Boolean result = (Boolean) this.mContext.getClass().getMethod("bindServiceAsUser", Intent.class, ServiceConnection.class, Integer.TYPE, Handler.class, UserHandle.class).invoke(this.mContext, intent, connection, 1, this.mHandler, this.mMyUser);
                if (result != null) {
                    return result.booleanValue();
                }
            } catch (Exception e2) {
                SDKLog.e(TAG, "bindService: bindServiceAsUser", e2);
            }
            return true;
        }
        if (isQ()) {
            return this.mContext.bindService(intent, 1, this.mExecutor, connection);
        }
        return this.mContext.bindService(intent, connection, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isQ() {
        return true;
    }
}
