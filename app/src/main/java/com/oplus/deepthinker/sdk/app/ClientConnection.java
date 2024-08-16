package com.oplus.deepthinker.sdk.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import i6.IDeepThinkerBridge;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes.dex */
public class ClientConnection {
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECTED = 3;
    private static final int STATE_INIT = 0;
    private static final String TAG = "ClientConnection";
    private final Context mContext;
    private CountDownLatch mCountDownLatch;
    private volatile IDeepThinkerBridge mDeepThinkerBridge;
    private final Executor mExecutor;
    private final Handler mHandler;
    private boolean mIsSystemUser;
    private volatile IBinder mRemote;
    private UserHandle mTargetUser;
    private static final Object LOCK = new Object();
    private static final ReadWriteLock BINDER_LOCK = new ReentrantReadWriteLock();
    private final WeakHashMap<ServiceStateObserver, Object> mStateObservers = new WeakHashMap<>();
    private final ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.oplus.deepthinker.sdk.app.ClientConnection.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onServiceConnected ");
            sb2.append(iBinder != null);
            SDKLog.i(ClientConnection.TAG, sb2.toString());
            if (iBinder == null) {
                return;
            }
            ClientConnection.BINDER_LOCK.writeLock().lock();
            try {
                try {
                    ClientConnection.this.mRemote = iBinder;
                    ClientConnection.this.mDeepThinkerBridge = IDeepThinkerBridge.a.z(iBinder);
                    iBinder.linkToDeath(ClientConnection.this.mDeathRecipient, 0);
                    if (ClientConnection.this.mDeepThinkerBridge != null) {
                        ClientConnection.this.mState = 2;
                    } else {
                        ClientConnection.this.mState = 3;
                    }
                    ClientConnection.BINDER_LOCK.writeLock().unlock();
                    if (ClientConnection.this.mCountDownLatch == null) {
                        return;
                    }
                } catch (RemoteException e10) {
                    SDKLog.e(ClientConnection.TAG, "onServiceConnected: ", e10);
                    if (ClientConnection.this.mDeepThinkerBridge != null) {
                        ClientConnection.this.mState = 2;
                    } else {
                        ClientConnection.this.mState = 3;
                    }
                    ClientConnection.BINDER_LOCK.writeLock().unlock();
                    if (ClientConnection.this.mCountDownLatch == null) {
                        return;
                    }
                }
                ClientConnection.this.mCountDownLatch.countDown();
            } catch (Throwable th) {
                if (ClientConnection.this.mDeepThinkerBridge != null) {
                    ClientConnection.this.mState = 2;
                } else {
                    ClientConnection.this.mState = 3;
                }
                ClientConnection.BINDER_LOCK.writeLock().unlock();
                if (ClientConnection.this.mCountDownLatch != null) {
                    ClientConnection.this.mCountDownLatch.countDown();
                }
                throw th;
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            SDKLog.i(ClientConnection.TAG, "onServiceDisconnected");
            ClientConnection.this.release();
            ClientConnection.this.serviceDied();
        }
    };
    private final IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.deepthinker.sdk.app.a
        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            ClientConnection.this.lambda$new$0();
        }
    };
    private volatile int mState = 0;

    public ClientConnection(Context context, Executor executor, Handler handler) {
        this.mContext = context;
        this.mExecutor = executor;
        this.mHandler = handler;
        initIsSystemUser();
    }

    private boolean bindService(Intent intent, ServiceConnection serviceConnection) {
        if (this.mIsSystemUser) {
            UserHandle userHandle = this.mTargetUser;
            if (userHandle == null) {
                userHandle = Process.myUserHandle();
            }
            try {
                Boolean bool = (Boolean) this.mContext.getClass().getMethod("bindServiceAsUser", Intent.class, ServiceConnection.class, Integer.TYPE, Handler.class, UserHandle.class).invoke(this.mContext, intent, serviceConnection, 1, this.mHandler, userHandle);
                if (bool != null) {
                    return bool.booleanValue();
                }
            } catch (Exception e10) {
                SDKLog.e(TAG, "bindService: bindServiceAsUser", e10);
            }
            return true;
        }
        if (isQ()) {
            return this.mContext.bindService(intent, 1, this.mExecutor, serviceConnection);
        }
        return this.mContext.bindService(intent, serviceConnection, 1);
    }

    private boolean bindServiceByPlatform() {
        return bindService(IOplusDeepThinkerManager.getServiceIntent(), this.mServiceConnection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0022  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void deliveryOnServiceDied() {
        ServiceStateObserver[] serviceStateObserverArr = new ServiceStateObserver[0];
        synchronized (LOCK) {
            try {
                serviceStateObserverArr = (ServiceStateObserver[]) this.mStateObservers.keySet().toArray(new ServiceStateObserver[0]);
            } finally {
                while (r0 < r5) {
                }
            }
        }
        for (ServiceStateObserver serviceStateObserver : serviceStateObserverArr) {
            if (serviceStateObserver != null) {
                serviceStateObserver.onServiceDied();
            }
        }
    }

    private void initIsSystemUser() {
        if (Process.myUid() == 1000) {
            this.mIsSystemUser = true;
            return;
        }
        try {
            this.mIsSystemUser = "android.uid.system".equals(this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).sharedUserId);
        } catch (Exception e10) {
            SDKLog.w(TAG, "initIsSystemUser " + e10);
        }
    }

    private boolean isOnMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    private boolean isQ() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        SDKLog.i(TAG, "binderDied");
        serviceDied();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v9, types: [java.util.concurrent.locks.Lock] */
    public /* synthetic */ void lambda$tryConnect$1(CountDownLatch countDownLatch) {
        SDKLog.d(TAG, "tryConnect: start connect on async thread.");
        if (!isQ() && isOnMainThread()) {
            SDKLog.w(TAG, "tryConnect: bind service on main thread, ignore ileagal usage.");
            return;
        }
        ReadWriteLock readWriteLock = BINDER_LOCK;
        readWriteLock.writeLock().lock();
        if (this.mState == 2) {
            try {
                SDKLog.w(TAG, "tryConnect: Already connected, do not reconnect again.");
                readWriteLock.writeLock().unlock();
                return;
            } finally {
            }
        }
        if (this.mState == 1) {
            try {
                SDKLog.w(TAG, "tryConnect: Already do connecting, do not reconnect again.");
                readWriteLock.writeLock().unlock();
                CountDownLatch countDownLatch2 = this.mCountDownLatch;
                if (countDownLatch2 != null) {
                    try {
                        countDownLatch2.await(2L, TimeUnit.SECONDS);
                        return;
                    } catch (InterruptedException e10) {
                        SDKLog.e(TAG, "tryConnect: wait to be connected error: ", e10);
                        return;
                    }
                }
                return;
            } finally {
            }
        }
        try {
            this.mState = 1;
            this.mCountDownLatch = new CountDownLatch(1);
            readWriteLock.writeLock().unlock();
            SDKLog.d(TAG, "tryConnect: start bind service");
            try {
                if (bindServiceByPlatform()) {
                    try {
                        this.mCountDownLatch.await(2L, TimeUnit.SECONDS);
                        readWriteLock.writeLock().lock();
                        try {
                            if (this.mState != 2) {
                                this.mState = 3;
                            }
                        } finally {
                        }
                    } catch (InterruptedException e11) {
                        SDKLog.e(TAG, "tryConnect: connectBinderPoolService error: ", e11);
                        readWriteLock = BINDER_LOCK;
                        readWriteLock.writeLock().lock();
                        try {
                            if (this.mState != 2) {
                                this.mState = 3;
                            }
                        } finally {
                        }
                    }
                    this = readWriteLock.writeLock();
                    this.unlock();
                } else {
                    readWriteLock.writeLock().lock();
                    try {
                        this.mState = 3;
                        readWriteLock.writeLock().unlock();
                        CountDownLatch countDownLatch3 = this.mCountDownLatch;
                        if (countDownLatch3 != null) {
                            countDownLatch3.countDown();
                        }
                        SDKLog.w(TAG, "tryConnect: Bind Algorithm Service Failed!");
                    } finally {
                    }
                }
                SDKLog.d(TAG, "tryConnect: end connect on async thread.");
                countDownLatch.countDown();
            } catch (Throwable th) {
                ReadWriteLock readWriteLock2 = BINDER_LOCK;
                readWriteLock2.writeLock().lock();
                try {
                    if (this.mState != 2) {
                        this.mState = 3;
                    }
                    readWriteLock2.writeLock().unlock();
                    throw th;
                } finally {
                }
            }
        } finally {
        }
    }

    private void onServiceDied() {
        if (isOnMainThread()) {
            this.mExecutor.execute(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.b
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
    /* JADX WARN: Finally extract failed */
    public void serviceDied() {
        ReadWriteLock readWriteLock = BINDER_LOCK;
        readWriteLock.writeLock().lock();
        if (this.mState == 3) {
            try {
                SDKLog.i(TAG, "serviceDied: already disconnected.");
                readWriteLock.writeLock().unlock();
                return;
            } catch (Throwable th) {
                BINDER_LOCK.writeLock().unlock();
                throw th;
            }
        }
        try {
            try {
                this.mDeepThinkerBridge.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
                this.mState = 3;
                this.mDeepThinkerBridge = null;
            } catch (Exception e10) {
                SDKLog.e(TAG, "serviceDied: ", e10);
                this.mState = 3;
                this.mDeepThinkerBridge = null;
                readWriteLock = BINDER_LOCK;
            }
            readWriteLock.writeLock().unlock();
            onServiceDied();
        } catch (Throwable th2) {
            this.mState = 3;
            this.mDeepThinkerBridge = null;
            BINDER_LOCK.writeLock().unlock();
            onServiceDied();
            throw th2;
        }
    }

    private void tryConnect() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        this.mExecutor.execute(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.c
            @Override // java.lang.Runnable
            public final void run() {
                ClientConnection.this.lambda$tryConnect$1(countDownLatch);
            }
        });
        boolean isOnMainThread = isOnMainThread();
        if (!isQ() && isOnMainThread) {
            SDKLog.w(TAG, "tryConnect: end. On Main Thread, reutrn directly.");
            return;
        }
        try {
            countDownLatch.await(2L, TimeUnit.SECONDS);
        } catch (InterruptedException e10) {
            SDKLog.e(TAG, "tryConnect: interrupted.", e10);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("tryConnect: end. On ");
        sb2.append(isOnMainThread ? "Main" : "Async");
        sb2.append(" Thread, connect ");
        sb2.append(this.mState == 2 ? "success." : "timeout.");
        SDKLog.i(TAG, sb2.toString());
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0031  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public IDeepThinkerBridge getDeepThinkerBridge() {
        boolean z10;
        boolean z11;
        SDKLog.i(TAG, "getDeepThinkerBridge start");
        if (this.mDeepThinkerBridge == null) {
            ReadWriteLock readWriteLock = BINDER_LOCK;
            readWriteLock.readLock().lock();
            try {
                if (this.mState != 2 && this.mState != 1) {
                    z10 = false;
                    z11 = this.mDeepThinkerBridge == null;
                    readWriteLock.readLock().unlock();
                    if (z11) {
                        if (z10) {
                            try {
                                CountDownLatch countDownLatch = this.mCountDownLatch;
                                if (countDownLatch != null) {
                                    countDownLatch.await(2L, TimeUnit.SECONDS);
                                }
                            } catch (Exception e10) {
                                SDKLog.w(TAG, "tryConnect: " + e10);
                            }
                        } else {
                            tryConnect();
                        }
                    }
                }
                z10 = true;
                if (this.mDeepThinkerBridge == null) {
                }
                readWriteLock.readLock().unlock();
                if (z11) {
                }
            } catch (Throwable th) {
                BINDER_LOCK.readLock().unlock();
                throw th;
            }
        }
        IDeepThinkerBridge iDeepThinkerBridge = this.mDeepThinkerBridge;
        SDKLog.i(TAG, "getDeepThinkerBridge end");
        return iDeepThinkerBridge;
    }

    public int getServiceState() {
        return this.mState;
    }

    public UserHandle getTargetUser() {
        return this.mTargetUser;
    }

    public void registerServiceStateObserver(ServiceStateObserver serviceStateObserver) {
        synchronized (LOCK) {
            this.mStateObservers.putIfAbsent(serviceStateObserver, null);
        }
    }

    public void release() {
        IBinder iBinder = this.mRemote;
        if (iBinder != null) {
            try {
                iBinder.unlinkToDeath(this.mDeathRecipient, 0);
            } catch (NoSuchElementException unused) {
            }
        }
    }

    public void setTargetUser(UserHandle userHandle) {
        this.mTargetUser = userHandle;
    }
}
