package com.android.server.utils.quota;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.ArrayMap;
import android.util.IndentingPrintWriter;
import android.util.LongArrayQueue;
import android.util.Slog;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.job.controllers.JobStatus;
import com.android.server.usage.UnixCalendar;
import com.android.server.utils.quota.CountQuotaTracker;
import com.android.server.utils.quota.QuotaTracker;
import com.android.server.utils.quota.UptcMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class CountQuotaTracker extends QuotaTracker {
    private static final String ALARM_TAG_CLEANUP = "*" + CountQuotaTracker.class.getSimpleName() + ".cleanup*";
    private static final boolean DEBUG = false;
    private static final int MSG_CLEAN_UP_EVENTS = 1;
    private static final String TAG = "CountQuotaTracker";

    @GuardedBy({"mLock"})
    private final ArrayMap<Category, Long> mCategoryCountWindowSizesMs;
    private Function<Void, ExecutionStats> mCreateExecutionStats;
    private Function<Void, LongArrayQueue> mCreateLongArrayQueue;
    private final DeleteEventTimesFunctor mDeleteOldEventTimesFunctor;
    private final EarliestEventTimeFunctor mEarliestEventTimeFunctor;

    @GuardedBy({"mLock"})
    private final AlarmManager.OnAlarmListener mEventCleanupAlarmListener;

    @GuardedBy({"mLock"})
    private final UptcMap<LongArrayQueue> mEventTimes;

    @GuardedBy({"mLock"})
    private final UptcMap<ExecutionStats> mExecutionStatsCache;
    private final Handler mHandler;
    private boolean mHasCleanUpEvents;

    @GuardedBy({"mLock"})
    private final ArrayMap<Category, Integer> mMaxCategoryCounts;

    @GuardedBy({"mLock"})
    private long mMaxPeriodMs;

    @GuardedBy({"mLock"})
    private long mNextCleanupTimeElapsed;

    @Override // com.android.server.utils.quota.QuotaTracker
    @GuardedBy({"mLock"})
    void onQuotaFreeChangedLocked(boolean z) {
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ void dump(IndentingPrintWriter indentingPrintWriter) {
        super.dump(indentingPrintWriter);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ boolean isWithinQuota(int i, String str, String str2) {
        return super.isWithinQuota(i, str, str2);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ void registerQuotaChangeListener(QuotaChangeListener quotaChangeListener) {
        super.registerQuotaChangeListener(quotaChangeListener);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ void setEnabled(boolean z) {
        super.setEnabled(z);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ void setQuotaFree(int i, String str, boolean z) {
        super.setQuotaFree(i, str, z);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ void setQuotaFree(boolean z) {
        super.setQuotaFree(z);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ void unregisterQuotaChangeListener(QuotaChangeListener quotaChangeListener) {
        super.unregisterQuotaChangeListener(quotaChangeListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ExecutionStats {
        public int countInWindow;
        public int countLimit;
        public long expirationTimeElapsed;
        public long inQuotaTimeElapsed;
        public long windowSizeMs;

        ExecutionStats() {
        }

        public String toString() {
            return "expirationTime=" + this.expirationTimeElapsed + ", windowSizeMs=" + this.windowSizeMs + ", countLimit=" + this.countLimit + ", countInWindow=" + this.countInWindow + ", inQuotaTime=" + this.inQuotaTimeElapsed;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ExecutionStats)) {
                return false;
            }
            ExecutionStats executionStats = (ExecutionStats) obj;
            return this.expirationTimeElapsed == executionStats.expirationTimeElapsed && this.windowSizeMs == executionStats.windowSizeMs && this.countLimit == executionStats.countLimit && this.countInWindow == executionStats.countInWindow && this.inQuotaTimeElapsed == executionStats.inQuotaTimeElapsed;
        }

        public int hashCode() {
            return ((((((((0 + Long.hashCode(this.expirationTimeElapsed)) * 31) + Long.hashCode(this.windowSizeMs)) * 31) + this.countLimit) * 31) + this.countInWindow) * 31) + Long.hashCode(this.inQuotaTimeElapsed);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    public CountQuotaTracker(Context context, Categorizer categorizer) {
        this(context, categorizer, new QuotaTracker.Injector());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public CountQuotaTracker(Context context, Categorizer categorizer, QuotaTracker.Injector injector) {
        super(context, categorizer, injector);
        this.mEventTimes = new UptcMap<>();
        this.mExecutionStatsCache = new UptcMap<>();
        this.mNextCleanupTimeElapsed = 0L;
        this.mEventCleanupAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda5
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                CountQuotaTracker.this.lambda$new$0();
            }
        };
        this.mCategoryCountWindowSizesMs = new ArrayMap<>();
        this.mMaxCategoryCounts = new ArrayMap<>();
        this.mMaxPeriodMs = 0L;
        this.mHasCleanUpEvents = true;
        this.mEarliestEventTimeFunctor = new EarliestEventTimeFunctor();
        this.mDeleteOldEventTimesFunctor = new DeleteEventTimesFunctor();
        this.mCreateLongArrayQueue = new Function() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                LongArrayQueue lambda$new$4;
                lambda$new$4 = CountQuotaTracker.lambda$new$4((Void) obj);
                return lambda$new$4;
            }
        };
        this.mCreateExecutionStats = new Function() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                CountQuotaTracker.ExecutionStats lambda$new$5;
                lambda$new$5 = CountQuotaTracker.lambda$new$5((Void) obj);
                return lambda$new$5;
            }
        };
        this.mHandler = new CqtHandler(context.getMainLooper());
    }

    public boolean noteEvent(int i, String str, String str2) {
        synchronized (this.mLock) {
            if (isEnabledLocked() && !isQuotaFreeLocked(i, str)) {
                long elapsedRealtime = this.mInjector.getElapsedRealtime();
                LongArrayQueue orCreate = this.mEventTimes.getOrCreate(i, str, str2, this.mCreateLongArrayQueue);
                orCreate.addLast(elapsedRealtime);
                ExecutionStats executionStatsLocked = getExecutionStatsLocked(i, str, str2);
                executionStatsLocked.countInWindow++;
                executionStatsLocked.expirationTimeElapsed = Math.min(executionStatsLocked.expirationTimeElapsed, executionStatsLocked.windowSizeMs + elapsedRealtime);
                int i2 = executionStatsLocked.countInWindow;
                int i3 = executionStatsLocked.countLimit;
                if (i2 == i3) {
                    long j = elapsedRealtime - executionStatsLocked.windowSizeMs;
                    while (orCreate.size() > 0 && orCreate.peekFirst() < j) {
                        orCreate.removeFirst();
                    }
                    executionStatsLocked.inQuotaTimeElapsed = orCreate.peekFirst() + executionStatsLocked.windowSizeMs;
                    postQuotaStatusChanged(i, str, str2);
                } else if (i3 > 9 && i2 == (i3 * 4) / 5) {
                    Slog.w(TAG, Uptc.string(i, str, str2) + " has reached 80% of it's count limit of " + executionStatsLocked.countLimit);
                }
                maybeScheduleCleanupAlarmLocked();
                return isWithinQuotaLocked(executionStatsLocked);
            }
            return true;
        }
    }

    public void setCountLimit(Category category, int i, long j) {
        if (i < 0 || j < 0) {
            throw new IllegalArgumentException("Limit and window size must be nonnegative.");
        }
        synchronized (this.mLock) {
            Integer put = this.mMaxCategoryCounts.put(category, Integer.valueOf(i));
            long max = Math.max(20000L, Math.min(j, UnixCalendar.MONTH_IN_MILLIS));
            Long put2 = this.mCategoryCountWindowSizesMs.put(category, Long.valueOf(max));
            if (put == null || put2 == null || put.intValue() != i || put2.longValue() != max) {
                this.mDeleteOldEventTimesFunctor.updateMaxPeriod();
                this.mMaxPeriodMs = this.mDeleteOldEventTimesFunctor.mMaxPeriodMs;
                invalidateAllExecutionStatsLocked();
                scheduleQuotaCheck();
            }
        }
    }

    public int getLimit(Category category) {
        int intValue;
        synchronized (this.mLock) {
            Integer num = this.mMaxCategoryCounts.get(category);
            if (num == null) {
                throw new IllegalArgumentException("Limit for " + category + " not defined");
            }
            intValue = num.intValue();
        }
        return intValue;
    }

    public long getWindowSizeMs(Category category) {
        long longValue;
        synchronized (this.mLock) {
            Long l = this.mCategoryCountWindowSizesMs.get(category);
            if (l == null) {
                throw new IllegalArgumentException("Limit for " + category + " not defined");
            }
            longValue = l.longValue();
        }
        return longValue;
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    @GuardedBy({"mLock"})
    void dropEverythingLocked() {
        this.mExecutionStatsCache.clear();
        this.mEventTimes.clear();
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    @GuardedBy({"mLock"})
    Handler getHandler() {
        return this.mHandler;
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    @GuardedBy({"mLock"})
    long getInQuotaTimeElapsedLocked(int i, String str, String str2) {
        return getExecutionStatsLocked(i, str, str2).inQuotaTimeElapsed;
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    @GuardedBy({"mLock"})
    void handleRemovedAppLocked(int i, String str) {
        if (str == null) {
            Slog.wtf(TAG, "Told app removed but given null package name.");
        } else {
            this.mEventTimes.delete(i, str);
            this.mExecutionStatsCache.delete(i, str);
        }
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    @GuardedBy({"mLock"})
    void handleRemovedUserLocked(int i) {
        this.mEventTimes.delete(i);
        this.mExecutionStatsCache.delete(i);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    @GuardedBy({"mLock"})
    boolean isWithinQuotaLocked(int i, String str, String str2) {
        if (isEnabledLocked() && !isQuotaFreeLocked(i, str)) {
            return isWithinQuotaLocked(getExecutionStatsLocked(i, str, str2));
        }
        return true;
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    @GuardedBy({"mLock"})
    void maybeUpdateAllQuotaStatusLocked() {
        final UptcMap uptcMap = new UptcMap();
        this.mEventTimes.forEach(new UptcMap.UptcDataConsumer() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda8
            @Override // com.android.server.utils.quota.UptcMap.UptcDataConsumer
            public final void accept(int i, String str, String str2, Object obj) {
                CountQuotaTracker.this.lambda$maybeUpdateAllQuotaStatusLocked$1(uptcMap, i, str, str2, (LongArrayQueue) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeUpdateAllQuotaStatusLocked$1(UptcMap uptcMap, int i, String str, String str2, LongArrayQueue longArrayQueue) {
        if (uptcMap.contains(i, str, str2)) {
            return;
        }
        maybeUpdateStatusForUptcLocked(i, str, str2);
        uptcMap.add(i, str, str2, Boolean.TRUE);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    void maybeUpdateQuotaStatus(int i, String str, String str2) {
        synchronized (this.mLock) {
            maybeUpdateStatusForUptcLocked(i, str, str2);
        }
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    @GuardedBy({"mLock"})
    void onQuotaFreeChangedLocked(int i, String str, boolean z) {
        maybeUpdateStatusForPkgLocked(i, str);
    }

    @GuardedBy({"mLock"})
    private boolean isWithinQuotaLocked(ExecutionStats executionStats) {
        return isUnderCountQuotaLocked(executionStats);
    }

    @GuardedBy({"mLock"})
    private boolean isUnderCountQuotaLocked(ExecutionStats executionStats) {
        return executionStats.countInWindow < executionStats.countLimit;
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    ExecutionStats getExecutionStatsLocked(int i, String str, String str2) {
        return getExecutionStatsLocked(i, str, str2, true);
    }

    @GuardedBy({"mLock"})
    private ExecutionStats getExecutionStatsLocked(int i, String str, String str2, boolean z) {
        ExecutionStats orCreate = this.mExecutionStatsCache.getOrCreate(i, str, str2, this.mCreateExecutionStats);
        if (z) {
            Category category = this.mCategorizer.getCategory(i, str, str2);
            long longValue = this.mCategoryCountWindowSizesMs.getOrDefault(category, Long.valueOf(JobStatus.NO_LATEST_RUNTIME)).longValue();
            int intValue = this.mMaxCategoryCounts.getOrDefault(category, Integer.MAX_VALUE).intValue();
            if (orCreate.expirationTimeElapsed <= this.mInjector.getElapsedRealtime() || orCreate.windowSizeMs != longValue || orCreate.countLimit != intValue) {
                orCreate.windowSizeMs = longValue;
                orCreate.countLimit = intValue;
                updateExecutionStatsLocked(i, str, str2, orCreate);
            }
        }
        return orCreate;
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    void updateExecutionStatsLocked(int i, String str, String str2, ExecutionStats executionStats) {
        executionStats.countInWindow = 0;
        if (executionStats.countLimit == 0) {
            executionStats.inQuotaTimeElapsed = JobStatus.NO_LATEST_RUNTIME;
        } else {
            executionStats.inQuotaTimeElapsed = 0L;
        }
        long elapsedRealtime = this.mInjector.getElapsedRealtime();
        executionStats.expirationTimeElapsed = this.mMaxPeriodMs + elapsedRealtime;
        LongArrayQueue longArrayQueue = this.mEventTimes.get(i, str, str2);
        if (longArrayQueue == null) {
            return;
        }
        long j = JobStatus.NO_LATEST_RUNTIME - elapsedRealtime;
        long j2 = elapsedRealtime - executionStats.windowSizeMs;
        for (int size = longArrayQueue.size() - 1; size >= 0; size--) {
            long j3 = longArrayQueue.get(size);
            if (j3 < j2) {
                break;
            }
            executionStats.countInWindow++;
            j = Math.min(j, j3 - j2);
            if (executionStats.countInWindow >= executionStats.countLimit) {
                executionStats.inQuotaTimeElapsed = Math.max(executionStats.inQuotaTimeElapsed, j3 + executionStats.windowSizeMs);
            }
        }
        executionStats.expirationTimeElapsed = elapsedRealtime + j;
    }

    @GuardedBy({"mLock"})
    private void invalidateAllExecutionStatsLocked() {
        final long elapsedRealtime = this.mInjector.getElapsedRealtime();
        this.mExecutionStatsCache.forEach(new Consumer() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CountQuotaTracker.lambda$invalidateAllExecutionStatsLocked$2(elapsedRealtime, (CountQuotaTracker.ExecutionStats) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$invalidateAllExecutionStatsLocked$2(long j, ExecutionStats executionStats) {
        if (executionStats != null) {
            executionStats.expirationTimeElapsed = j;
        }
    }

    @GuardedBy({"mLock"})
    private void invalidateAllExecutionStatsLocked(int i, String str) {
        ArrayMap<String, ExecutionStats> arrayMap = this.mExecutionStatsCache.get(i, str);
        if (arrayMap != null) {
            long elapsedRealtime = this.mInjector.getElapsedRealtime();
            int size = arrayMap.size();
            for (int i2 = 0; i2 < size; i2++) {
                ExecutionStats valueAt = arrayMap.valueAt(i2);
                if (valueAt != null) {
                    valueAt.expirationTimeElapsed = elapsedRealtime;
                }
            }
        }
    }

    @GuardedBy({"mLock"})
    private void invalidateExecutionStatsLocked(int i, String str, String str2) {
        ExecutionStats executionStats = this.mExecutionStatsCache.get(i, str, str2);
        if (executionStats != null) {
            executionStats.expirationTimeElapsed = this.mInjector.getElapsedRealtime();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class EarliestEventTimeFunctor implements Consumer<LongArrayQueue> {
        long earliestTimeElapsed;

        private EarliestEventTimeFunctor() {
            this.earliestTimeElapsed = JobStatus.NO_LATEST_RUNTIME;
        }

        @Override // java.util.function.Consumer
        public void accept(LongArrayQueue longArrayQueue) {
            if (longArrayQueue == null || longArrayQueue.size() <= 0) {
                return;
            }
            this.earliestTimeElapsed = Math.min(this.earliestTimeElapsed, longArrayQueue.get(0));
        }

        void reset() {
            this.earliestTimeElapsed = JobStatus.NO_LATEST_RUNTIME;
        }
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    void maybeScheduleCleanupAlarmLocked() {
        if (this.mNextCleanupTimeElapsed <= this.mInjector.getElapsedRealtime() && this.mHasCleanUpEvents) {
            this.mHasCleanUpEvents = false;
            this.mEarliestEventTimeFunctor.reset();
            this.mEventTimes.forEach(this.mEarliestEventTimeFunctor);
            long j = this.mEarliestEventTimeFunctor.earliestTimeElapsed;
            if (j == JobStatus.NO_LATEST_RUNTIME) {
                return;
            }
            long j2 = j + this.mMaxPeriodMs;
            if (j2 - this.mNextCleanupTimeElapsed <= 600000) {
                j2 += 600000;
            }
            long j3 = j2;
            this.mNextCleanupTimeElapsed = j3;
            scheduleAlarm(3, j3, ALARM_TAG_CLEANUP, this.mEventCleanupAlarmListener);
        }
    }

    @GuardedBy({"mLock"})
    private boolean maybeUpdateStatusForPkgLocked(final int i, final String str) {
        final UptcMap uptcMap = new UptcMap();
        if (!this.mEventTimes.contains(i, str)) {
            return false;
        }
        ArrayMap<String, LongArrayQueue> arrayMap = this.mEventTimes.get(i, str);
        if (arrayMap == null) {
            Slog.wtf(TAG, "Events map was null even though mEventTimes said it contained " + Uptc.string(i, str, null));
            return false;
        }
        final boolean[] zArr = {false};
        arrayMap.forEach(new BiConsumer() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                CountQuotaTracker.this.lambda$maybeUpdateStatusForPkgLocked$3(uptcMap, i, str, zArr, (String) obj, (LongArrayQueue) obj2);
            }
        });
        return zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeUpdateStatusForPkgLocked$3(UptcMap uptcMap, int i, String str, boolean[] zArr, String str2, LongArrayQueue longArrayQueue) {
        if (uptcMap.contains(i, str, str2)) {
            return;
        }
        zArr[0] = maybeUpdateStatusForUptcLocked(i, str, str2) | zArr[0];
        uptcMap.add(i, str, str2, Boolean.TRUE);
    }

    @GuardedBy({"mLock"})
    private boolean maybeUpdateStatusForUptcLocked(int i, String str, String str2) {
        boolean isWithinQuotaLocked = isWithinQuotaLocked(getExecutionStatsLocked(i, str, str2, false));
        boolean isWithinQuotaLocked2 = (!isEnabledLocked() || isQuotaFreeLocked(i, str)) ? true : isWithinQuotaLocked(getExecutionStatsLocked(i, str, str2, true));
        if (!isWithinQuotaLocked2) {
            maybeScheduleStartAlarmLocked(i, str, str2);
        } else {
            cancelScheduledStartAlarmLocked(i, str, str2);
        }
        if (isWithinQuotaLocked == isWithinQuotaLocked2) {
            return false;
        }
        postQuotaStatusChanged(i, str, str2);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class DeleteEventTimesFunctor implements Consumer<LongArrayQueue> {
        private long mMaxPeriodMs;

        private DeleteEventTimesFunctor() {
        }

        @Override // java.util.function.Consumer
        public void accept(LongArrayQueue longArrayQueue) {
            if (longArrayQueue != null) {
                while (longArrayQueue.size() > 0 && longArrayQueue.peekFirst() <= CountQuotaTracker.this.mInjector.getElapsedRealtime() - this.mMaxPeriodMs) {
                    longArrayQueue.removeFirst();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateMaxPeriod() {
            long j = 0;
            for (int size = CountQuotaTracker.this.mCategoryCountWindowSizesMs.size() - 1; size >= 0; size--) {
                j = Long.max(j, ((Long) CountQuotaTracker.this.mCategoryCountWindowSizesMs.valueAt(size)).longValue());
            }
            this.mMaxPeriodMs = j;
        }
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    void deleteObsoleteEventsLocked() {
        this.mEventTimes.forEach(this.mDeleteOldEventTimesFunctor);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class CqtHandler extends Handler {
        CqtHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            synchronized (CountQuotaTracker.this.mLock) {
                if (message.what == 1) {
                    CountQuotaTracker.this.deleteObsoleteEventsLocked();
                    CountQuotaTracker.this.mHasCleanUpEvents = true;
                    CountQuotaTracker.this.maybeScheduleCleanupAlarmLocked();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ LongArrayQueue lambda$new$4(Void r0) {
        return new LongArrayQueue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ExecutionStats lambda$new$5(Void r0) {
        return new ExecutionStats();
    }

    @VisibleForTesting
    LongArrayQueue getEvents(int i, String str, String str2) {
        return this.mEventTimes.get(i, str, str2);
    }

    public void dump(final com.android.internal.util.IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.print(TAG);
        indentingPrintWriter.println(":");
        indentingPrintWriter.increaseIndent();
        synchronized (this.mLock) {
            super.dump((IndentingPrintWriter) indentingPrintWriter);
            indentingPrintWriter.println();
            indentingPrintWriter.println("Instantaneous events:");
            indentingPrintWriter.increaseIndent();
            this.mEventTimes.forEach(new UptcMap.UptcDataConsumer() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda2
                @Override // com.android.server.utils.quota.UptcMap.UptcDataConsumer
                public final void accept(int i, String str, String str2, Object obj) {
                    CountQuotaTracker.lambda$dump$6(indentingPrintWriter, i, str, str2, (LongArrayQueue) obj);
                }
            });
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            indentingPrintWriter.println("Cached execution stats:");
            indentingPrintWriter.increaseIndent();
            this.mExecutionStatsCache.forEach(new UptcMap.UptcDataConsumer() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda3
                @Override // com.android.server.utils.quota.UptcMap.UptcDataConsumer
                public final void accept(int i, String str, String str2, Object obj) {
                    CountQuotaTracker.lambda$dump$7(indentingPrintWriter, i, str, str2, (CountQuotaTracker.ExecutionStats) obj);
                }
            });
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            indentingPrintWriter.println("Limits:");
            indentingPrintWriter.increaseIndent();
            int size = this.mCategoryCountWindowSizesMs.size();
            for (int i = 0; i < size; i++) {
                Category keyAt = this.mCategoryCountWindowSizesMs.keyAt(i);
                indentingPrintWriter.print(keyAt);
                indentingPrintWriter.print(": ");
                indentingPrintWriter.print(this.mMaxCategoryCounts.get(keyAt));
                indentingPrintWriter.print(" events in ");
                indentingPrintWriter.println(TimeUtils.formatDuration(this.mCategoryCountWindowSizesMs.get(keyAt).longValue()));
            }
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$6(com.android.internal.util.IndentingPrintWriter indentingPrintWriter, int i, String str, String str2, LongArrayQueue longArrayQueue) {
        if (longArrayQueue.size() > 0) {
            indentingPrintWriter.print(Uptc.string(i, str, str2));
            indentingPrintWriter.println(":");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.print(longArrayQueue.get(0));
            for (int i2 = 1; i2 < longArrayQueue.size(); i2++) {
                indentingPrintWriter.print(", ");
                indentingPrintWriter.print(longArrayQueue.get(i2));
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$7(com.android.internal.util.IndentingPrintWriter indentingPrintWriter, int i, String str, String str2, ExecutionStats executionStats) {
        if (executionStats != null) {
            indentingPrintWriter.print(Uptc.string(i, str, str2));
            indentingPrintWriter.println(":");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println(executionStats);
            indentingPrintWriter.decreaseIndent();
        }
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public void dump(final ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        synchronized (this.mLock) {
            super.dump(protoOutputStream, 1146756268033L);
            for (int i = 0; i < this.mCategoryCountWindowSizesMs.size(); i++) {
                Category keyAt = this.mCategoryCountWindowSizesMs.keyAt(i);
                long start2 = protoOutputStream.start(2246267895810L);
                keyAt.dumpDebug(protoOutputStream, 1146756268033L);
                protoOutputStream.write(1120986464258L, this.mMaxCategoryCounts.get(keyAt).intValue());
                protoOutputStream.write(1112396529667L, this.mCategoryCountWindowSizesMs.get(keyAt).longValue());
                protoOutputStream.end(start2);
            }
            this.mExecutionStatsCache.forEach(new UptcMap.UptcDataConsumer() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda4
                @Override // com.android.server.utils.quota.UptcMap.UptcDataConsumer
                public final void accept(int i2, String str, String str2, Object obj) {
                    CountQuotaTracker.this.lambda$dump$8(protoOutputStream, i2, str, str2, (CountQuotaTracker.ExecutionStats) obj);
                }
            });
            protoOutputStream.end(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dump$8(ProtoOutputStream protoOutputStream, int i, String str, String str2, ExecutionStats executionStats) {
        boolean isIndividualQuotaFreeLocked = isIndividualQuotaFreeLocked(i, str);
        long start = protoOutputStream.start(2246267895811L);
        new Uptc(i, str, str2).dumpDebug(protoOutputStream, 1146756268033L);
        protoOutputStream.write(1133871366146L, isIndividualQuotaFreeLocked);
        LongArrayQueue longArrayQueue = this.mEventTimes.get(i, str, str2);
        if (longArrayQueue != null) {
            for (int size = longArrayQueue.size() - 1; size >= 0; size--) {
                long start2 = protoOutputStream.start(2246267895811L);
                protoOutputStream.write(1112396529665L, longArrayQueue.get(size));
                protoOutputStream.end(start2);
            }
        }
        long start3 = protoOutputStream.start(2246267895812L);
        protoOutputStream.write(1112396529665L, executionStats.expirationTimeElapsed);
        protoOutputStream.write(1112396529666L, executionStats.windowSizeMs);
        protoOutputStream.write(1120986464259L, executionStats.countLimit);
        protoOutputStream.write(1120986464260L, executionStats.countInWindow);
        protoOutputStream.write(1112396529669L, executionStats.inQuotaTimeElapsed);
        protoOutputStream.end(start3);
        protoOutputStream.end(start);
    }
}
