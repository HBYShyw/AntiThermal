package com.android.server.am;

import android.content.Context;
import android.os.SystemClock;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.am.BaseAppStateDurations;
import com.android.server.am.BaseAppStateEvents;
import com.android.server.am.BaseAppStateEventsTracker;
import com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy;
import com.android.server.am.BaseAppStateTimeEvents;
import com.android.server.am.BaseAppStateTracker;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.LinkedList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class BaseAppStateDurationsTracker<T extends BaseAppStateEventsTracker.BaseAppStateEventsPolicy, U extends BaseAppStateDurations> extends BaseAppStateEventsTracker<T, U> {
    static final boolean DEBUG_BASE_APP_STATE_DURATION_TRACKER = false;

    @GuardedBy({"mLock"})
    final SparseArray<UidStateDurations> mUidStateDurations;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseAppStateDurationsTracker(Context context, AppRestrictionController appRestrictionController, Constructor<? extends BaseAppStateTracker.Injector<T>> constructor, Object obj) {
        super(context, appRestrictionController, constructor, obj);
        this.mUidStateDurations = new SparseArray<>();
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker, com.android.server.am.BaseAppStateTracker
    void onUidProcStateChanged(int i, int i2) {
        synchronized (this.mLock) {
            if (this.mPkgEvents.getMap().indexOfKey(i) < 0) {
                return;
            }
            onUidProcStateChangedUncheckedLocked(i, i2);
            UidStateDurations uidStateDurations = this.mUidStateDurations.get(i);
            if (uidStateDurations == null) {
                uidStateDurations = new UidStateDurations(i, (BaseAppStateEvents.MaxTrackingDurationConfig) this.mInjector.getPolicy());
                this.mUidStateDurations.put(i, uidStateDurations);
            }
            uidStateDurations.addEvent(i2 < 4, SystemClock.elapsedRealtime());
        }
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker, com.android.server.am.BaseAppStateTracker
    void onUidGone(int i) {
        onUidProcStateChanged(i, 20);
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker
    @GuardedBy({"mLock"})
    void trimLocked(long j) {
        super.trimLocked(j);
        for (int size = this.mUidStateDurations.size() - 1; size >= 0; size--) {
            UidStateDurations valueAt = this.mUidStateDurations.valueAt(size);
            valueAt.trim(j);
            if (valueAt.isEmpty()) {
                this.mUidStateDurations.removeAt(size);
            }
        }
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker
    @GuardedBy({"mLock"})
    void onUntrackingUidLocked(int i) {
        this.mUidStateDurations.remove(i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    long getTotalDurations(String str, int i, long j, int i2, boolean z) {
        UidStateDurations uidStateDurations;
        synchronized (this.mLock) {
            BaseAppStateDurations baseAppStateDurations = (BaseAppStateDurations) this.mPkgEvents.get(i, str);
            if (baseAppStateDurations == null) {
                return 0L;
            }
            if (z && (uidStateDurations = this.mUidStateDurations.get(i)) != null && !uidStateDurations.isEmpty()) {
                BaseAppStateDurations baseAppStateDurations2 = (BaseAppStateDurations) createAppStateEvents(baseAppStateDurations);
                baseAppStateDurations2.subtract(uidStateDurations, i2, 0);
                return baseAppStateDurations2.getTotalDurations(j, i2);
            }
            return baseAppStateDurations.getTotalDurations(j, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getTotalDurations(String str, int i, long j, int i2) {
        return getTotalDurations(str, i, j, i2, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getTotalDurations(String str, int i, long j) {
        return getTotalDurations(str, i, j, 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    long getTotalDurations(int i, long j, int i2, boolean z) {
        UidStateDurations uidStateDurations;
        synchronized (this.mLock) {
            BaseAppStateDurations baseAppStateDurations = (BaseAppStateDurations) getUidEventsLocked(i);
            if (baseAppStateDurations == null) {
                return 0L;
            }
            if (z && (uidStateDurations = this.mUidStateDurations.get(i)) != null && !uidStateDurations.isEmpty()) {
                baseAppStateDurations.subtract(uidStateDurations, i2, 0);
            }
            return baseAppStateDurations.getTotalDurations(j, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getTotalDurations(int i, long j, int i2) {
        return getTotalDurations(i, j, i2, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getTotalDurations(int i, long j) {
        return getTotalDurations(i, j, 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    long getTotalDurationsSince(String str, int i, long j, long j2, int i2, boolean z) {
        UidStateDurations uidStateDurations;
        synchronized (this.mLock) {
            BaseAppStateDurations baseAppStateDurations = (BaseAppStateDurations) this.mPkgEvents.get(i, str);
            if (baseAppStateDurations == null) {
                return 0L;
            }
            if (z && (uidStateDurations = this.mUidStateDurations.get(i)) != null && !uidStateDurations.isEmpty()) {
                BaseAppStateDurations baseAppStateDurations2 = (BaseAppStateDurations) createAppStateEvents(baseAppStateDurations);
                baseAppStateDurations2.subtract(uidStateDurations, i2, 0);
                return baseAppStateDurations2.getTotalDurationsSince(j, j2, i2);
            }
            return baseAppStateDurations.getTotalDurationsSince(j, j2, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getTotalDurationsSince(String str, int i, long j, long j2, int i2) {
        return getTotalDurationsSince(str, i, j, j2, i2, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getTotalDurationsSince(String str, int i, long j, long j2) {
        return getTotalDurationsSince(str, i, j, j2, 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    long getTotalDurationsSince(int i, long j, long j2, int i2, boolean z) {
        UidStateDurations uidStateDurations;
        synchronized (this.mLock) {
            BaseAppStateDurations baseAppStateDurations = (BaseAppStateDurations) getUidEventsLocked(i);
            if (baseAppStateDurations == null) {
                return 0L;
            }
            if (z && (uidStateDurations = this.mUidStateDurations.get(i)) != null && !uidStateDurations.isEmpty()) {
                baseAppStateDurations.subtract(uidStateDurations, i2, 0);
            }
            return baseAppStateDurations.getTotalDurationsSince(j, j2, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getTotalDurationsSince(int i, long j, long j2, int i2) {
        return getTotalDurationsSince(i, j, j2, i2, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getTotalDurationsSince(int i, long j, long j2) {
        return getTotalDurationsSince(i, j, j2, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.am.BaseAppStateEventsTracker
    @VisibleForTesting
    public void reset() {
        super.reset();
        synchronized (this.mLock) {
            this.mUidStateDurations.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.am.BaseAppStateEventsTracker
    @GuardedBy({"mLock"})
    public void dumpEventLocked(PrintWriter printWriter, String str, U u, long j) {
        UidStateDurations uidStateDurations = this.mUidStateDurations.get(u.mUid);
        printWriter.print("  " + str);
        printWriter.println("(bg only)");
        if (uidStateDurations == null || uidStateDurations.isEmpty()) {
            u.dump(printWriter, "    " + str, j);
            return;
        }
        BaseAppStateDurations baseAppStateDurations = (BaseAppStateDurations) createAppStateEvents(u);
        baseAppStateDurations.subtract(uidStateDurations, 0);
        baseAppStateDurations.dump(printWriter, "    " + str, j);
        printWriter.print("  " + str);
        printWriter.println("(fg + bg)");
        u.dump(printWriter, "    " + str, j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class SimplePackageDurations extends BaseAppStateDurations<BaseAppStateTimeEvents.BaseTimeEvent> {
        static final int DEFAULT_INDEX = 0;

        @Override // com.android.server.am.BaseAppStateEvents
        String formatEventTypeLabel(int i) {
            return "";
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Multi-variable type inference failed */
        public SimplePackageDurations(int i, String str, BaseAppStateEvents.MaxTrackingDurationConfig maxTrackingDurationConfig) {
            super(i, str, 1, IActivityManagerServiceExt.TAG, maxTrackingDurationConfig);
            this.mEvents[0] = new LinkedList();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public SimplePackageDurations(SimplePackageDurations simplePackageDurations) {
            super(simplePackageDurations);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void addEvent(boolean z, long j) {
            addEvent(z, (boolean) new BaseAppStateTimeEvents.BaseTimeEvent(j), 0);
        }

        long getTotalDurations(long j) {
            return getTotalDurations(j, 0);
        }

        long getTotalDurationsSince(long j, long j2) {
            return getTotalDurationsSince(j, j2, 0);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isActive() {
            return isActive(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class UidStateDurations extends SimplePackageDurations {
        UidStateDurations(int i, BaseAppStateEvents.MaxTrackingDurationConfig maxTrackingDurationConfig) {
            super(i, "", maxTrackingDurationConfig);
        }

        UidStateDurations(UidStateDurations uidStateDurations) {
            super(uidStateDurations);
        }
    }
}
