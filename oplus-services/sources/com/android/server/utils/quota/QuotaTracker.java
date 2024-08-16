package com.android.server.utils.quota;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.Slog;
import android.util.SparseArrayMap;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.BackgroundThread;
import com.android.server.FgThread;
import com.android.server.LocalServices;
import com.android.server.SystemServiceManager;
import com.android.server.utils.AlarmQueue;
import com.android.server.utils.quota.QuotaTracker;
import java.util.function.Predicate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class QuotaTracker {
    private static final String ALARM_TAG_QUOTA_CHECK = "*" + QuotaTracker.class.getSimpleName() + ".quota_check*";
    private static final boolean DEBUG = false;

    @VisibleForTesting
    static final long MAX_WINDOW_SIZE_MS = 2592000000L;

    @VisibleForTesting
    static final long MIN_WINDOW_SIZE_MS = 20000;
    private static final String TAG = "QuotaTracker";
    private final AlarmManager mAlarmManager;
    private final BroadcastReceiver mBroadcastReceiver;
    final Categorizer mCategorizer;
    protected final Context mContext;

    @GuardedBy({"mLock"})
    private final InQuotaAlarmQueue mInQuotaAlarmQueue;
    protected final Injector mInjector;

    @GuardedBy({"mLock"})
    private boolean mIsQuotaFree;
    final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final ArraySet<QuotaChangeListener> mQuotaChangeListeners = new ArraySet<>();

    @GuardedBy({"mLock"})
    private final SparseArrayMap<String, Boolean> mFreeQuota = new SparseArrayMap<>();

    @GuardedBy({"mLock"})
    private boolean mIsEnabled = true;

    @GuardedBy({"mLock"})
    abstract void dropEverythingLocked();

    abstract Handler getHandler();

    @GuardedBy({"mLock"})
    abstract long getInQuotaTimeElapsedLocked(int i, String str, String str2);

    @GuardedBy({"mLock"})
    abstract void handleRemovedAppLocked(int i, String str);

    @GuardedBy({"mLock"})
    abstract void handleRemovedUserLocked(int i);

    @GuardedBy({"mLock"})
    abstract boolean isWithinQuotaLocked(int i, String str, String str2);

    @GuardedBy({"mLock"})
    abstract void maybeUpdateAllQuotaStatusLocked();

    abstract void maybeUpdateQuotaStatus(int i, String str, String str2);

    @GuardedBy({"mLock"})
    abstract void onQuotaFreeChangedLocked(int i, String str, boolean z);

    @GuardedBy({"mLock"})
    abstract void onQuotaFreeChangedLocked(boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Injector {
        /* JADX INFO: Access modifiers changed from: package-private */
        public long getElapsedRealtime() {
            return SystemClock.elapsedRealtime();
        }

        boolean isAlarmManagerReady() {
            return ((SystemServiceManager) LocalServices.getService(SystemServiceManager.class)).isBootCompleted();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public QuotaTracker(Context context, Categorizer categorizer, Injector injector) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.utils.quota.QuotaTracker.1
            private String getPackageName(Intent intent) {
                Uri data = intent.getData();
                if (data != null) {
                    return data.getSchemeSpecificPart();
                }
                return null;
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent == null || intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                    return;
                }
                String action = intent.getAction();
                if (action == null) {
                    Slog.e(QuotaTracker.TAG, "Received intent with null action");
                    return;
                }
                if (action.equals("android.intent.action.USER_REMOVED")) {
                    int intExtra = intent.getIntExtra("android.intent.extra.user_handle", 0);
                    synchronized (QuotaTracker.this.mLock) {
                        QuotaTracker.this.onUserRemovedLocked(intExtra);
                    }
                    return;
                }
                if (action.equals("android.intent.action.PACKAGE_FULLY_REMOVED")) {
                    int intExtra2 = intent.getIntExtra("android.intent.extra.UID", -1);
                    synchronized (QuotaTracker.this.mLock) {
                        QuotaTracker.this.onAppRemovedLocked(UserHandle.getUserId(intExtra2), getPackageName(intent));
                    }
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mCategorizer = categorizer;
        this.mContext = context;
        this.mInjector = injector;
        this.mAlarmManager = (AlarmManager) context.getSystemService(AlarmManager.class);
        this.mInQuotaAlarmQueue = new InQuotaAlarmQueue(context, FgThread.getHandler().getLooper());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_FULLY_REMOVED");
        intentFilter.addDataScheme("package");
        context.registerReceiverAsUser(broadcastReceiver, UserHandle.ALL, intentFilter, null, BackgroundThread.getHandler());
        context.registerReceiverAsUser(broadcastReceiver, UserHandle.ALL, new IntentFilter("android.intent.action.USER_REMOVED"), null, BackgroundThread.getHandler());
    }

    public void clear() {
        synchronized (this.mLock) {
            this.mInQuotaAlarmQueue.removeAllAlarms();
            this.mFreeQuota.clear();
            dropEverythingLocked();
        }
    }

    public boolean isWithinQuota(int i, String str, String str2) {
        boolean isWithinQuotaLocked;
        synchronized (this.mLock) {
            isWithinQuotaLocked = isWithinQuotaLocked(i, str, str2);
        }
        return isWithinQuotaLocked;
    }

    public void setQuotaFree(int i, String str, boolean z) {
        synchronized (this.mLock) {
            if (((Boolean) this.mFreeQuota.getOrDefault(i, str, Boolean.FALSE)).booleanValue() != z) {
                this.mFreeQuota.add(i, str, Boolean.valueOf(z));
                onQuotaFreeChangedLocked(i, str, z);
            }
        }
    }

    public void setQuotaFree(boolean z) {
        synchronized (this.mLock) {
            if (this.mIsQuotaFree == z) {
                return;
            }
            this.mIsQuotaFree = z;
            if (this.mIsEnabled) {
                onQuotaFreeChangedLocked(z);
                scheduleQuotaCheck();
            }
        }
    }

    public void registerQuotaChangeListener(QuotaChangeListener quotaChangeListener) {
        synchronized (this.mLock) {
            if (this.mQuotaChangeListeners.add(quotaChangeListener) && this.mQuotaChangeListeners.size() == 1) {
                scheduleQuotaCheck();
            }
        }
    }

    public void unregisterQuotaChangeListener(QuotaChangeListener quotaChangeListener) {
        synchronized (this.mLock) {
            this.mQuotaChangeListeners.remove(quotaChangeListener);
        }
    }

    public void setEnabled(boolean z) {
        synchronized (this.mLock) {
            if (this.mIsEnabled == z) {
                return;
            }
            this.mIsEnabled = z;
            if (!z) {
                clear();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean isEnabledLocked() {
        return this.mIsEnabled;
    }

    @GuardedBy({"mLock"})
    boolean isQuotaFreeLocked() {
        return this.mIsQuotaFree;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean isQuotaFreeLocked(int i, String str) {
        return this.mIsQuotaFree || ((Boolean) this.mFreeQuota.getOrDefault(i, str, Boolean.FALSE)).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public boolean isIndividualQuotaFreeLocked(int i, String str) {
        return ((Boolean) this.mFreeQuota.getOrDefault(i, str, Boolean.FALSE)).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleAlarm(final int i, final long j, final String str, final AlarmManager.OnAlarmListener onAlarmListener) {
        FgThread.getHandler().post(new Runnable() { // from class: com.android.server.utils.quota.QuotaTracker$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                QuotaTracker.this.lambda$scheduleAlarm$0(i, j, str, onAlarmListener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleAlarm$0(int i, long j, String str, AlarmManager.OnAlarmListener onAlarmListener) {
        if (this.mInjector.isAlarmManagerReady()) {
            this.mAlarmManager.set(i, j, str, onAlarmListener, getHandler());
        } else {
            Slog.w(TAG, "Alarm not scheduled because boot isn't completed");
        }
    }

    void cancelAlarm(final AlarmManager.OnAlarmListener onAlarmListener) {
        FgThread.getHandler().post(new Runnable() { // from class: com.android.server.utils.quota.QuotaTracker$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                QuotaTracker.this.lambda$cancelAlarm$1(onAlarmListener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelAlarm$1(AlarmManager.OnAlarmListener onAlarmListener) {
        if (this.mInjector.isAlarmManagerReady()) {
            this.mAlarmManager.cancel(onAlarmListener);
        } else {
            Slog.w(TAG, "Alarm not cancelled because boot isn't completed");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleQuotaCheck() {
        BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.server.utils.quota.QuotaTracker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                QuotaTracker.this.lambda$scheduleQuotaCheck$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleQuotaCheck$2() {
        synchronized (this.mLock) {
            if (this.mQuotaChangeListeners.size() > 0) {
                maybeUpdateAllQuotaStatusLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void onAppRemovedLocked(int i, String str) {
        if (str == null) {
            Slog.wtf(TAG, "Told app removed but given null package name.");
            return;
        }
        this.mInQuotaAlarmQueue.removeAlarms(i, str);
        this.mFreeQuota.delete(i, str);
        handleRemovedAppLocked(i, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void onUserRemovedLocked(int i) {
        this.mInQuotaAlarmQueue.removeAlarmsForUserId(i);
        this.mFreeQuota.delete(i);
        handleRemovedUserLocked(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postQuotaStatusChanged(final int i, final String str, final String str2) {
        BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.server.utils.quota.QuotaTracker$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                QuotaTracker.this.lambda$postQuotaStatusChanged$3(i, str, str2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postQuotaStatusChanged$3(int i, String str, String str2) {
        QuotaChangeListener[] quotaChangeListenerArr;
        synchronized (this.mLock) {
            ArraySet<QuotaChangeListener> arraySet = this.mQuotaChangeListeners;
            quotaChangeListenerArr = (QuotaChangeListener[]) arraySet.toArray(new QuotaChangeListener[arraySet.size()]);
        }
        for (QuotaChangeListener quotaChangeListener : quotaChangeListenerArr) {
            quotaChangeListener.onQuotaStateChanged(i, str, str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    @VisibleForTesting
    public void maybeScheduleStartAlarmLocked(int i, String str, String str2) {
        if (this.mQuotaChangeListeners.size() == 0) {
            return;
        }
        Uptc.string(i, str, str2);
        if (isWithinQuota(i, str, str2)) {
            this.mInQuotaAlarmQueue.removeAlarmForKey(new Uptc(i, str, str2));
            maybeUpdateQuotaStatus(i, str, str2);
        } else {
            this.mInQuotaAlarmQueue.addAlarm(new Uptc(i, str, str2), getInQuotaTimeElapsedLocked(i, str, str2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void cancelScheduledStartAlarmLocked(int i, String str, String str2) {
        this.mInQuotaAlarmQueue.removeAlarmForKey(new Uptc(i, str, str2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class InQuotaAlarmQueue extends AlarmQueue<Uptc> {
        private InQuotaAlarmQueue(Context context, Looper looper) {
            super(context, looper, QuotaTracker.ALARM_TAG_QUOTA_CHECK, "In quota", false, 0L);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.utils.AlarmQueue
        public boolean isForUser(Uptc uptc, int i) {
            return i == uptc.userId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$removeAlarms$0(int i, String str, Uptc uptc) {
            return i == uptc.userId && str.equals(uptc.packageName);
        }

        void removeAlarms(final int i, final String str) {
            removeAlarmsIf(new Predicate() { // from class: com.android.server.utils.quota.QuotaTracker$InQuotaAlarmQueue$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$removeAlarms$0;
                    lambda$removeAlarms$0 = QuotaTracker.InQuotaAlarmQueue.lambda$removeAlarms$0(i, str, (Uptc) obj);
                    return lambda$removeAlarms$0;
                }
            });
        }

        @Override // com.android.server.utils.AlarmQueue
        protected void processExpiredAlarms(ArraySet<Uptc> arraySet) {
            for (int i = 0; i < arraySet.size(); i++) {
                final Uptc valueAt = arraySet.valueAt(i);
                QuotaTracker.this.getHandler().post(new Runnable() { // from class: com.android.server.utils.quota.QuotaTracker$InQuotaAlarmQueue$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        QuotaTracker.InQuotaAlarmQueue.this.lambda$processExpiredAlarms$1(valueAt);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processExpiredAlarms$1(Uptc uptc) {
            QuotaTracker.this.maybeUpdateQuotaStatus(uptc.userId, uptc.packageName, uptc.tag);
        }
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("QuotaTracker:");
        indentingPrintWriter.increaseIndent();
        synchronized (this.mLock) {
            indentingPrintWriter.println("Is enabled: " + this.mIsEnabled);
            indentingPrintWriter.println("Is global quota free: " + this.mIsQuotaFree);
            indentingPrintWriter.println("Current elapsed time: " + this.mInjector.getElapsedRealtime());
            indentingPrintWriter.println();
            indentingPrintWriter.println();
            this.mInQuotaAlarmQueue.dump(indentingPrintWriter);
            indentingPrintWriter.println();
            indentingPrintWriter.println("Per-app free quota:");
            indentingPrintWriter.increaseIndent();
            for (int i = 0; i < this.mFreeQuota.numMaps(); i++) {
                int keyAt = this.mFreeQuota.keyAt(i);
                for (int i2 = 0; i2 < this.mFreeQuota.numElementsForKey(keyAt); i2++) {
                    String str = (String) this.mFreeQuota.keyAt(i, i2);
                    indentingPrintWriter.print(Uptc.string(keyAt, str, null));
                    indentingPrintWriter.print(": ");
                    indentingPrintWriter.println(this.mFreeQuota.get(keyAt, str));
                }
            }
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.decreaseIndent();
    }

    public void dump(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        synchronized (this.mLock) {
            protoOutputStream.write(1133871366145L, this.mIsEnabled);
            protoOutputStream.write(1133871366146L, this.mIsQuotaFree);
            protoOutputStream.write(1112396529667L, this.mInjector.getElapsedRealtime());
        }
        protoOutputStream.end(start);
    }
}
