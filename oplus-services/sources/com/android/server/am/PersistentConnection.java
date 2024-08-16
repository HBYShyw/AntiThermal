package com.android.server.am;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class PersistentConnection<T> {
    private static final boolean DEBUG = false;

    @GuardedBy({"mLock"})
    private boolean mBound;
    private final ComponentName mComponentName;
    private final Context mContext;
    private final Handler mHandler;

    @GuardedBy({"mLock"})
    private boolean mIsConnected;

    @GuardedBy({"mLock"})
    private long mLastConnectedTime;
    private long mNextBackoffMs;

    @GuardedBy({"mLock"})
    private int mNumBindingDied;

    @GuardedBy({"mLock"})
    private int mNumConnected;

    @GuardedBy({"mLock"})
    private int mNumDisconnected;
    private final double mRebindBackoffIncrease;
    private final long mRebindBackoffMs;
    private final long mRebindMaxBackoffMs;

    @GuardedBy({"mLock"})
    private boolean mRebindScheduled;
    private long mReconnectTime;
    private final long mResetBackoffDelay;

    @GuardedBy({"mLock"})
    private T mService;

    @GuardedBy({"mLock"})
    private boolean mShouldBeBound;
    private final String mTag;
    private final int mUserId;
    private final Object mLock = new Object();
    private final ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.android.server.am.PersistentConnection.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            synchronized (PersistentConnection.this.mLock) {
                if (!PersistentConnection.this.mBound) {
                    Log.w(PersistentConnection.this.mTag, "Connected: " + PersistentConnection.this.mComponentName.flattenToShortString() + " u" + PersistentConnection.this.mUserId + " but not bound, ignore.");
                    return;
                }
                Log.i(PersistentConnection.this.mTag, "Connected: " + PersistentConnection.this.mComponentName.flattenToShortString() + " u" + PersistentConnection.this.mUserId);
                PersistentConnection persistentConnection = PersistentConnection.this;
                persistentConnection.mNumConnected = persistentConnection.mNumConnected + 1;
                PersistentConnection.this.mIsConnected = true;
                PersistentConnection persistentConnection2 = PersistentConnection.this;
                persistentConnection2.mLastConnectedTime = persistentConnection2.injectUptimeMillis();
                PersistentConnection persistentConnection3 = PersistentConnection.this;
                persistentConnection3.mService = persistentConnection3.asInterface(iBinder);
                PersistentConnection.this.scheduleStableCheckLocked();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            synchronized (PersistentConnection.this.mLock) {
                Log.i(PersistentConnection.this.mTag, "Disconnected: " + PersistentConnection.this.mComponentName.flattenToShortString() + " u" + PersistentConnection.this.mUserId);
                PersistentConnection persistentConnection = PersistentConnection.this;
                persistentConnection.mNumDisconnected = persistentConnection.mNumDisconnected + 1;
                PersistentConnection.this.cleanUpConnectionLocked();
            }
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName componentName) {
            synchronized (PersistentConnection.this.mLock) {
                if (!PersistentConnection.this.mBound) {
                    Log.w(PersistentConnection.this.mTag, "Binding died: " + PersistentConnection.this.mComponentName.flattenToShortString() + " u" + PersistentConnection.this.mUserId + " but not bound, ignore.");
                    return;
                }
                Log.w(PersistentConnection.this.mTag, "Binding died: " + PersistentConnection.this.mComponentName.flattenToShortString() + " u" + PersistentConnection.this.mUserId);
                PersistentConnection persistentConnection = PersistentConnection.this;
                persistentConnection.mNumBindingDied = persistentConnection.mNumBindingDied + 1;
                PersistentConnection.this.scheduleRebindLocked();
            }
        }
    };
    private final Runnable mBindForBackoffRunnable = new Runnable() { // from class: com.android.server.am.PersistentConnection$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            PersistentConnection.this.lambda$new$0();
        }
    };
    private final Runnable mStableCheck = new Runnable() { // from class: com.android.server.am.PersistentConnection$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            PersistentConnection.this.stableConnectionCheck();
        }
    };

    protected abstract T asInterface(IBinder iBinder);

    protected abstract int getBindFlags();

    public PersistentConnection(String str, Context context, Handler handler, int i, ComponentName componentName, long j, double d, long j2, long j3) {
        this.mTag = str;
        this.mContext = context;
        this.mHandler = handler;
        this.mUserId = i;
        this.mComponentName = componentName;
        long j4 = j * 1000;
        this.mRebindBackoffMs = j4;
        this.mRebindBackoffIncrease = d;
        this.mRebindMaxBackoffMs = j2 * 1000;
        this.mResetBackoffDelay = j3 * 1000;
        this.mNextBackoffMs = j4;
    }

    public final ComponentName getComponentName() {
        return this.mComponentName;
    }

    public final int getUserId() {
        return this.mUserId;
    }

    public final boolean isBound() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mBound;
        }
        return z;
    }

    public final boolean isRebindScheduled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mRebindScheduled;
        }
        return z;
    }

    public final boolean isConnected() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsConnected;
        }
        return z;
    }

    public final T getServiceBinder() {
        T t;
        synchronized (this.mLock) {
            t = this.mService;
        }
        return t;
    }

    public final void bind() {
        synchronized (this.mLock) {
            this.mShouldBeBound = true;
            bindInnerLocked(true);
        }
    }

    public long getNextBackoffMs() {
        long j;
        synchronized (this.mLock) {
            j = this.mNextBackoffMs;
        }
        return j;
    }

    public int getNumConnected() {
        int i;
        synchronized (this.mLock) {
            i = this.mNumConnected;
        }
        return i;
    }

    public int getNumDisconnected() {
        int i;
        synchronized (this.mLock) {
            i = this.mNumDisconnected;
        }
        return i;
    }

    public int getNumBindingDied() {
        int i;
        synchronized (this.mLock) {
            i = this.mNumBindingDied;
        }
        return i;
    }

    @GuardedBy({"mLock"})
    private void resetBackoffLocked() {
        long j = this.mNextBackoffMs;
        long j2 = this.mRebindBackoffMs;
        if (j != j2) {
            this.mNextBackoffMs = j2;
            Log.i(this.mTag, "Backoff reset to " + this.mNextBackoffMs);
        }
    }

    @GuardedBy({"mLock"})
    public final void bindInnerLocked(boolean z) {
        unscheduleRebindLocked();
        if (this.mBound) {
            return;
        }
        this.mBound = true;
        unscheduleStableCheckLocked();
        if (z) {
            resetBackoffLocked();
        }
        Intent component = new Intent().setComponent(this.mComponentName);
        if (this.mContext.bindServiceAsUser(component, this.mServiceConnection, getBindFlags() | 1, this.mHandler, UserHandle.of(this.mUserId))) {
            return;
        }
        Log.e(this.mTag, "Binding: " + component.getComponent() + " u" + this.mUserId + " failed.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: bindForBackoff, reason: merged with bridge method [inline-methods] */
    public final void lambda$new$0() {
        synchronized (this.mLock) {
            if (this.mShouldBeBound) {
                bindInnerLocked(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void cleanUpConnectionLocked() {
        this.mIsConnected = false;
        this.mService = null;
        unscheduleStableCheckLocked();
    }

    public final void unbind() {
        synchronized (this.mLock) {
            this.mShouldBeBound = false;
            unbindLocked();
            unscheduleStableCheckLocked();
        }
    }

    @GuardedBy({"mLock"})
    private final void unbindLocked() {
        unscheduleRebindLocked();
        if (this.mBound) {
            Log.i(this.mTag, "Stopping: " + this.mComponentName.flattenToShortString() + " u" + this.mUserId);
            this.mBound = false;
            this.mContext.unbindService(this.mServiceConnection);
            cleanUpConnectionLocked();
        }
    }

    @GuardedBy({"mLock"})
    void unscheduleRebindLocked() {
        injectRemoveCallbacks(this.mBindForBackoffRunnable);
        this.mRebindScheduled = false;
    }

    @GuardedBy({"mLock"})
    void scheduleRebindLocked() {
        unbindLocked();
        if (this.mRebindScheduled) {
            return;
        }
        Log.i(this.mTag, "Scheduling to reconnect in " + this.mNextBackoffMs + " ms (uptime)");
        long injectUptimeMillis = injectUptimeMillis() + this.mNextBackoffMs;
        this.mReconnectTime = injectUptimeMillis;
        injectPostAtTime(this.mBindForBackoffRunnable, injectUptimeMillis);
        this.mNextBackoffMs = Math.min(this.mRebindMaxBackoffMs, (long) (((double) this.mNextBackoffMs) * this.mRebindBackoffIncrease));
        this.mRebindScheduled = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stableConnectionCheck() {
        synchronized (this.mLock) {
            long injectUptimeMillis = (this.mLastConnectedTime + this.mResetBackoffDelay) - injectUptimeMillis();
            if (this.mBound && this.mIsConnected && injectUptimeMillis <= 0) {
                resetBackoffLocked();
            }
        }
    }

    @GuardedBy({"mLock"})
    private void unscheduleStableCheckLocked() {
        injectRemoveCallbacks(this.mStableCheck);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void scheduleStableCheckLocked() {
        unscheduleStableCheckLocked();
        injectPostAtTime(this.mStableCheck, injectUptimeMillis() + this.mResetBackoffDelay);
    }

    public void dump(String str, PrintWriter printWriter) {
        synchronized (this.mLock) {
            printWriter.print(str);
            printWriter.print(this.mComponentName.flattenToShortString());
            printWriter.print(" u");
            printWriter.print(this.mUserId);
            printWriter.print(this.mBound ? " [bound]" : " [not bound]");
            printWriter.print(this.mIsConnected ? " [connected]" : " [not connected]");
            if (this.mRebindScheduled) {
                printWriter.print(" reconnect in ");
                TimeUtils.formatDuration(this.mReconnectTime - injectUptimeMillis(), printWriter);
            }
            printWriter.println();
            printWriter.print(str);
            printWriter.print("  Next backoff(sec): ");
            printWriter.print(this.mNextBackoffMs / 1000);
            printWriter.println();
            printWriter.print(str);
            printWriter.print("  Connected: ");
            printWriter.print(this.mNumConnected);
            printWriter.print("  Disconnected: ");
            printWriter.print(this.mNumDisconnected);
            printWriter.print("  Died: ");
            printWriter.print(this.mNumBindingDied);
            if (this.mIsConnected) {
                printWriter.print("  Duration: ");
                TimeUtils.formatDuration(injectUptimeMillis() - this.mLastConnectedTime, printWriter);
            }
            printWriter.println();
        }
    }

    @VisibleForTesting
    void injectRemoveCallbacks(Runnable runnable) {
        this.mHandler.removeCallbacks(runnable);
    }

    @VisibleForTesting
    void injectPostAtTime(Runnable runnable, long j) {
        this.mHandler.postAtTime(runnable, j);
    }

    @VisibleForTesting
    long injectUptimeMillis() {
        return SystemClock.uptimeMillis();
    }

    @VisibleForTesting
    long getNextBackoffMsForTest() {
        return this.mNextBackoffMs;
    }

    @VisibleForTesting
    long getReconnectTimeForTest() {
        return this.mReconnectTime;
    }

    @VisibleForTesting
    ServiceConnection getServiceConnectionForTest() {
        return this.mServiceConnection;
    }

    @VisibleForTesting
    Runnable getBindForBackoffRunnableForTest() {
        return this.mBindForBackoffRunnable;
    }

    @VisibleForTesting
    Runnable getStableCheckRunnableForTest() {
        return this.mStableCheck;
    }

    @VisibleForTesting
    boolean shouldBeBoundForTest() {
        return this.mShouldBeBound;
    }
}
