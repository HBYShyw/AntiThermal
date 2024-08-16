package com.android.server.devicepolicy;

import android.app.AlarmManager;
import android.app.admin.NetworkEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.LongSparseArray;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class NetworkLoggingHandler extends Handler {
    private static final long BATCH_FINALIZATION_TIMEOUT_ALARM_INTERVAL_MS = 1800000;
    private static final long BATCH_FINALIZATION_TIMEOUT_MS = 5400000;

    @VisibleForTesting
    static final int LOG_NETWORK_EVENT_MSG = 1;
    private static final int MAX_BATCHES = 5;
    private static final int MAX_EVENTS_PER_BATCH = 1200;
    static final String NETWORK_EVENT_KEY = "network_event";
    private static final String NETWORK_LOGGING_TIMEOUT_ALARM_TAG = "NetworkLogging.batchTimeout";
    private static final long RETRIEVED_BATCH_DISCARD_DELAY_MS = 300000;
    private final AlarmManager mAlarmManager;
    private final AlarmManager.OnAlarmListener mBatchTimeoutAlarmListener;

    @GuardedBy({"this"})
    private final LongSparseArray<ArrayList<NetworkEvent>> mBatches;

    @GuardedBy({"this"})
    private long mCurrentBatchToken;
    private final DevicePolicyManagerService mDpm;
    private long mId;

    @GuardedBy({"this"})
    private long mLastFinalizationNanos;

    @GuardedBy({"this"})
    private long mLastRetrievedBatchToken;

    @GuardedBy({"this"})
    private ArrayList<NetworkEvent> mNetworkEvents;

    @GuardedBy({"this"})
    private boolean mPaused;
    private int mTargetUserId;
    private static final String TAG = NetworkLoggingHandler.class.getSimpleName();
    private static final long FORCE_FETCH_THROTTLE_NS = TimeUnit.SECONDS.toNanos(10);

    /* JADX INFO: Access modifiers changed from: package-private */
    public NetworkLoggingHandler(Looper looper, DevicePolicyManagerService devicePolicyManagerService, int i) {
        this(looper, devicePolicyManagerService, 0L, i);
    }

    @VisibleForTesting
    NetworkLoggingHandler(Looper looper, DevicePolicyManagerService devicePolicyManagerService, long j, int i) {
        super(looper);
        this.mLastFinalizationNanos = -1L;
        this.mBatchTimeoutAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.android.server.devicepolicy.NetworkLoggingHandler.1
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                Bundle finalizeBatchAndBuildAdminMessageLocked;
                Slog.d(NetworkLoggingHandler.TAG, "Received a batch finalization timeout alarm, finalizing " + NetworkLoggingHandler.this.mNetworkEvents.size() + " pending events.");
                synchronized (NetworkLoggingHandler.this) {
                    finalizeBatchAndBuildAdminMessageLocked = NetworkLoggingHandler.this.finalizeBatchAndBuildAdminMessageLocked();
                }
                if (finalizeBatchAndBuildAdminMessageLocked != null) {
                    NetworkLoggingHandler.this.notifyDeviceOwnerOrProfileOwner(finalizeBatchAndBuildAdminMessageLocked);
                }
            }
        };
        this.mNetworkEvents = new ArrayList<>();
        this.mBatches = new LongSparseArray<>(5);
        this.mPaused = false;
        this.mDpm = devicePolicyManagerService;
        this.mAlarmManager = devicePolicyManagerService.mInjector.getAlarmManager();
        this.mId = j;
        this.mTargetUserId = i;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        Bundle finalizeBatchAndBuildAdminMessageLocked;
        if (message.what == 1) {
            NetworkEvent networkEvent = (NetworkEvent) message.getData().getParcelable(NETWORK_EVENT_KEY, NetworkEvent.class);
            if (networkEvent != null) {
                synchronized (this) {
                    this.mNetworkEvents.add(networkEvent);
                    finalizeBatchAndBuildAdminMessageLocked = this.mNetworkEvents.size() >= MAX_EVENTS_PER_BATCH ? finalizeBatchAndBuildAdminMessageLocked() : null;
                }
                if (finalizeBatchAndBuildAdminMessageLocked != null) {
                    notifyDeviceOwnerOrProfileOwner(finalizeBatchAndBuildAdminMessageLocked);
                    return;
                }
                return;
            }
            return;
        }
        Slog.d(TAG, "NetworkLoggingHandler received an unknown of message.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleBatchFinalization() {
        this.mAlarmManager.setWindow(2, SystemClock.elapsedRealtime() + BATCH_FINALIZATION_TIMEOUT_MS, 1800000L, NETWORK_LOGGING_TIMEOUT_ALARM_TAG, this.mBatchTimeoutAlarmListener, this);
        Slog.d(TAG, "Scheduled a new batch finalization alarm 5400000ms from now.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long forceBatchFinalization() {
        synchronized (this) {
            long nanoTime = (this.mLastFinalizationNanos + FORCE_FETCH_THROTTLE_NS) - System.nanoTime();
            if (nanoTime > 0) {
                return TimeUnit.NANOSECONDS.toMillis(nanoTime) + 1;
            }
            Bundle finalizeBatchAndBuildAdminMessageLocked = finalizeBatchAndBuildAdminMessageLocked();
            if (finalizeBatchAndBuildAdminMessageLocked != null) {
                notifyDeviceOwnerOrProfileOwner(finalizeBatchAndBuildAdminMessageLocked);
            }
            return 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void pause() {
        Slog.d(TAG, "Paused network logging");
        this.mPaused = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resume() {
        Bundle bundle;
        synchronized (this) {
            if (!this.mPaused) {
                Slog.d(TAG, "Attempted to resume network logging, but logging is not paused.");
                return;
            }
            Slog.d(TAG, "Resumed network logging. Current batch=" + this.mCurrentBatchToken + ", LastRetrievedBatch=" + this.mLastRetrievedBatchToken);
            this.mPaused = false;
            if (this.mBatches.size() <= 0 || this.mLastRetrievedBatchToken == this.mCurrentBatchToken) {
                bundle = null;
            } else {
                scheduleBatchFinalization();
                bundle = buildAdminMessageLocked();
            }
            if (bundle != null) {
                notifyDeviceOwnerOrProfileOwner(bundle);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void discardLogs() {
        this.mBatches.clear();
        this.mNetworkEvents = new ArrayList<>();
        Slog.d(TAG, "Discarded all network logs");
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"this"})
    public Bundle finalizeBatchAndBuildAdminMessageLocked() {
        Bundle bundle;
        this.mLastFinalizationNanos = System.nanoTime();
        if (this.mNetworkEvents.size() > 0) {
            Iterator<NetworkEvent> it = this.mNetworkEvents.iterator();
            while (it.hasNext()) {
                it.next().setId(this.mId);
                long j = this.mId;
                if (j == Long.MAX_VALUE) {
                    Slog.i(TAG, "Reached maximum id value; wrapping around ." + this.mCurrentBatchToken);
                    this.mId = 0L;
                } else {
                    this.mId = j + 1;
                }
            }
            if (this.mBatches.size() >= 5) {
                this.mBatches.removeAt(0);
            }
            long j2 = this.mCurrentBatchToken + 1;
            this.mCurrentBatchToken = j2;
            this.mBatches.append(j2, this.mNetworkEvents);
            this.mNetworkEvents = new ArrayList<>();
            if (!this.mPaused) {
                bundle = buildAdminMessageLocked();
                scheduleBatchFinalization();
                return bundle;
            }
        } else {
            Slog.d(TAG, "Was about to finalize the batch, but there were no events to send to the DPC, the batchToken of last available batch: " + this.mCurrentBatchToken);
        }
        bundle = null;
        scheduleBatchFinalization();
        return bundle;
    }

    @GuardedBy({"this"})
    private Bundle buildAdminMessageLocked() {
        Bundle bundle = new Bundle();
        int size = this.mBatches.valueAt(r1.size() - 1).size();
        bundle.putLong("android.app.extra.EXTRA_NETWORK_LOGS_TOKEN", this.mCurrentBatchToken);
        bundle.putInt("android.app.extra.EXTRA_NETWORK_LOGS_COUNT", size);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDeviceOwnerOrProfileOwner(Bundle bundle) {
        if (Thread.holdsLock(this)) {
            Slog.wtfStack(TAG, "Shouldn't be called with NetworkLoggingHandler lock held");
            return;
        }
        Slog.d(TAG, "Sending network logging batch broadcast to device owner or profile owner, batchToken: " + bundle.getLong("android.app.extra.EXTRA_NETWORK_LOGS_TOKEN", -1L));
        this.mDpm.sendDeviceOwnerOrProfileOwnerCommand("android.app.action.NETWORK_LOGS_AVAILABLE", bundle, this.mTargetUserId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized List<NetworkEvent> retrieveFullLogBatch(final long j) {
        int indexOfKey = this.mBatches.indexOfKey(j);
        if (indexOfKey < 0) {
            return null;
        }
        postDelayed(new Runnable() { // from class: com.android.server.devicepolicy.NetworkLoggingHandler$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NetworkLoggingHandler.this.lambda$retrieveFullLogBatch$0(j);
            }
        }, 300000L);
        this.mLastRetrievedBatchToken = j;
        return this.mBatches.valueAt(indexOfKey);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$retrieveFullLogBatch$0(long j) {
        synchronized (this) {
            while (this.mBatches.size() > 0 && this.mBatches.keyAt(0) <= j) {
                this.mBatches.removeAt(0);
            }
        }
    }
}
