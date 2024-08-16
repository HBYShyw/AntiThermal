package com.android.server.am;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.AlarmManagerInternal;
import com.android.server.LocalServices;
import dalvik.annotation.optimization.NeverCompile;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BroadcastDispatcher {
    private static final String TAG = "BroadcastDispatcher";
    private AlarmManagerInternal mAlarm;
    private IBroadcastDispatcherExt mBroadcastDispatcherExt;
    private final BroadcastConstants mConstants;
    private BroadcastRecord mCurrentBroadcast;
    private final Handler mHandler;
    private final Object mLock;
    private final BroadcastQueueImpl mQueue;
    final SparseIntArray mAlarmUids = new SparseIntArray();
    final AlarmManagerInternal.InFlightListener mAlarmListener = new AlarmManagerInternal.InFlightListener() { // from class: com.android.server.am.BroadcastDispatcher.1
        @Override // com.android.server.AlarmManagerInternal.InFlightListener
        public void broadcastAlarmPending(int i) {
            synchronized (BroadcastDispatcher.this.mLock) {
                int i2 = 0;
                BroadcastDispatcher.this.mAlarmUids.put(i, BroadcastDispatcher.this.mAlarmUids.get(i, 0) + 1);
                int size = BroadcastDispatcher.this.mDeferredBroadcasts.size();
                while (true) {
                    if (i2 >= size) {
                        break;
                    }
                    if (i == ((Deferrals) BroadcastDispatcher.this.mDeferredBroadcasts.get(i2)).uid) {
                        BroadcastDispatcher.this.mAlarmDeferrals.add((Deferrals) BroadcastDispatcher.this.mDeferredBroadcasts.remove(i2));
                        break;
                    }
                    i2++;
                }
            }
        }

        @Override // com.android.server.AlarmManagerInternal.InFlightListener
        public void broadcastAlarmComplete(int i) {
            synchronized (BroadcastDispatcher.this.mLock) {
                int i2 = 0;
                int i3 = BroadcastDispatcher.this.mAlarmUids.get(i, 0) - 1;
                if (i3 >= 0) {
                    BroadcastDispatcher.this.mAlarmUids.put(i, i3);
                } else {
                    Slog.wtf(BroadcastDispatcher.TAG, "Undercount of broadcast alarms in flight for " + i);
                    BroadcastDispatcher.this.mAlarmUids.put(i, 0);
                }
                if (i3 <= 0) {
                    int size = BroadcastDispatcher.this.mAlarmDeferrals.size();
                    while (true) {
                        if (i2 >= size) {
                            break;
                        }
                        if (i == ((Deferrals) BroadcastDispatcher.this.mAlarmDeferrals.get(i2)).uid) {
                            BroadcastDispatcher.insertLocked(BroadcastDispatcher.this.mDeferredBroadcasts, (Deferrals) BroadcastDispatcher.this.mAlarmDeferrals.remove(i2));
                            break;
                        }
                        i2++;
                    }
                }
            }
        }
    };
    final Runnable mScheduleRunnable = new Runnable() { // from class: com.android.server.am.BroadcastDispatcher.2
        @Override // java.lang.Runnable
        public void run() {
            synchronized (BroadcastDispatcher.this.mLock) {
                if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
                    Slog.v(BroadcastDispatcher.TAG, "Deferral recheck of pending broadcasts");
                }
                BroadcastDispatcher.this.mQueue.scheduleBroadcastsLocked();
                BroadcastDispatcher.this.mRecheckScheduled = false;
            }
        }
    };
    private boolean mRecheckScheduled = false;
    private final ArrayList<BroadcastRecord> mOrderedBroadcasts = new ArrayList<>();
    private final ArrayList<Deferrals> mDeferredBroadcasts = new ArrayList<>();
    private final ArrayList<Deferrals> mAlarmDeferrals = new ArrayList<>();
    private final ArrayList<BroadcastRecord> mAlarmQueue = new ArrayList<>();
    private SparseArray<DeferredBootCompletedBroadcastPerUser> mUser2Deferred = new SparseArray<>();
    private BroadcastDispatcherWrapper mWrapper = new BroadcastDispatcherWrapper();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Deferrals {
        int alarmCount;
        final ArrayList<BroadcastRecord> broadcasts = new ArrayList<>();
        long deferUntil;
        long deferredAt;
        long deferredBy;
        final int uid;

        Deferrals(int i, long j, long j2, int i2) {
            this.uid = i;
            this.deferredAt = j;
            this.deferredBy = j2;
            this.deferUntil = j + j2;
            this.alarmCount = i2;
        }

        void add(BroadcastRecord broadcastRecord) {
            this.broadcasts.add(broadcastRecord);
        }

        int size() {
            return this.broadcasts.size();
        }

        boolean isEmpty() {
            return this.broadcasts.isEmpty();
        }

        @NeverCompile
        void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
            Iterator<BroadcastRecord> it = this.broadcasts.iterator();
            while (it.hasNext()) {
                it.next().dumpDebug(protoOutputStream, j);
            }
        }

        @NeverCompile
        void dumpLocked(Dumper dumper) {
            Iterator<BroadcastRecord> it = this.broadcasts.iterator();
            while (it.hasNext()) {
                dumper.dump(it.next());
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Deferrals{uid=");
            sb.append(this.uid);
            sb.append(", deferUntil=");
            sb.append(this.deferUntil);
            sb.append(", #broadcasts=");
            sb.append(this.broadcasts.size());
            sb.append("}");
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class Dumper {
        final String mDumpPackage;
        String mHeading;
        String mLabel;
        int mOrdinal;
        final PrintWriter mPw;
        final String mQueueName;
        final SimpleDateFormat mSdf;
        boolean mPrinted = false;
        boolean mNeedSep = true;

        Dumper(PrintWriter printWriter, String str, String str2, SimpleDateFormat simpleDateFormat) {
            this.mPw = printWriter;
            this.mQueueName = str;
            this.mDumpPackage = str2;
            this.mSdf = simpleDateFormat;
        }

        void setHeading(String str) {
            this.mHeading = str;
            this.mPrinted = false;
        }

        void setLabel(String str) {
            this.mLabel = "  " + str + " " + this.mQueueName + " #";
            this.mOrdinal = 0;
        }

        boolean didPrint() {
            return this.mPrinted;
        }

        @NeverCompile
        void dump(BroadcastRecord broadcastRecord) {
            String str = this.mDumpPackage;
            if (str == null || str.equals(broadcastRecord.callerPackage)) {
                if (!this.mPrinted) {
                    if (this.mNeedSep) {
                        this.mPw.println();
                    }
                    this.mPrinted = true;
                    this.mNeedSep = true;
                    this.mPw.println("  " + this.mHeading + " [" + this.mQueueName + "]:");
                }
                this.mPw.println(this.mLabel + this.mOrdinal + ":");
                this.mOrdinal = this.mOrdinal + 1;
                broadcastRecord.dump(this.mPw, "    ", this.mSdf);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class DeferredBootCompletedBroadcastPerUser {

        @VisibleForTesting
        boolean mBootCompletedBroadcastReceived;

        @VisibleForTesting
        boolean mLockedBootCompletedBroadcastReceived;
        private int mUserId;

        @VisibleForTesting
        SparseBooleanArray mUidReadyForLockedBootCompletedBroadcast = new SparseBooleanArray();

        @VisibleForTesting
        SparseBooleanArray mUidReadyForBootCompletedBroadcast = new SparseBooleanArray();

        @VisibleForTesting
        SparseArray<BroadcastRecord> mDeferredLockedBootCompletedBroadcasts = new SparseArray<>();

        @VisibleForTesting
        SparseArray<BroadcastRecord> mDeferredBootCompletedBroadcasts = new SparseArray<>();

        DeferredBootCompletedBroadcastPerUser(int i) {
            this.mUserId = i;
        }

        public void updateUidReady(int i) {
            if (!this.mLockedBootCompletedBroadcastReceived || this.mDeferredLockedBootCompletedBroadcasts.size() != 0) {
                this.mUidReadyForLockedBootCompletedBroadcast.put(i, true);
            }
            if (this.mBootCompletedBroadcastReceived && this.mDeferredBootCompletedBroadcasts.size() == 0) {
                return;
            }
            this.mUidReadyForBootCompletedBroadcast.put(i, true);
        }

        public void enqueueBootCompletedBroadcasts(String str, SparseArray<BroadcastRecord> sparseArray) {
            if ("android.intent.action.LOCKED_BOOT_COMPLETED".equals(str)) {
                enqueueBootCompletedBroadcasts(sparseArray, this.mDeferredLockedBootCompletedBroadcasts, this.mUidReadyForLockedBootCompletedBroadcast);
                this.mLockedBootCompletedBroadcastReceived = true;
                if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
                    dumpBootCompletedBroadcastRecord(this.mDeferredLockedBootCompletedBroadcasts);
                    return;
                }
                return;
            }
            if ("android.intent.action.BOOT_COMPLETED".equals(str)) {
                enqueueBootCompletedBroadcasts(sparseArray, this.mDeferredBootCompletedBroadcasts, this.mUidReadyForBootCompletedBroadcast);
                this.mBootCompletedBroadcastReceived = true;
                if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
                    dumpBootCompletedBroadcastRecord(this.mDeferredBootCompletedBroadcasts);
                }
            }
        }

        private void enqueueBootCompletedBroadcasts(SparseArray<BroadcastRecord> sparseArray, SparseArray<BroadcastRecord> sparseArray2, SparseBooleanArray sparseBooleanArray) {
            for (int size = sparseBooleanArray.size() - 1; size >= 0; size--) {
                if (sparseArray.indexOfKey(sparseBooleanArray.keyAt(size)) < 0) {
                    sparseBooleanArray.removeAt(size);
                }
            }
            int size2 = sparseArray.size();
            for (int i = 0; i < size2; i++) {
                int keyAt = sparseArray.keyAt(i);
                sparseArray2.put(keyAt, sparseArray.valueAt(i));
                if (sparseBooleanArray.indexOfKey(keyAt) < 0) {
                    sparseBooleanArray.put(keyAt, false);
                }
            }
        }

        public BroadcastRecord dequeueDeferredBootCompletedBroadcast(boolean z) {
            BroadcastRecord dequeueDeferredBootCompletedBroadcast = dequeueDeferredBootCompletedBroadcast(this.mDeferredLockedBootCompletedBroadcasts, this.mUidReadyForLockedBootCompletedBroadcast, z);
            return dequeueDeferredBootCompletedBroadcast == null ? dequeueDeferredBootCompletedBroadcast(this.mDeferredBootCompletedBroadcasts, this.mUidReadyForBootCompletedBroadcast, z) : dequeueDeferredBootCompletedBroadcast;
        }

        private BroadcastRecord dequeueDeferredBootCompletedBroadcast(SparseArray<BroadcastRecord> sparseArray, SparseBooleanArray sparseBooleanArray, boolean z) {
            int size = sparseArray.size();
            for (int i = 0; i < size; i++) {
                int keyAt = sparseArray.keyAt(i);
                if (z || sparseBooleanArray.get(keyAt)) {
                    BroadcastRecord valueAt = sparseArray.valueAt(i);
                    if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
                        Object obj = valueAt.receivers.get(0);
                        if (obj instanceof BroadcastFilter) {
                            if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
                                Slog.i(BroadcastDispatcher.TAG, "getDeferredBootCompletedBroadcast uid:" + keyAt + " BroadcastFilter:" + ((BroadcastFilter) obj) + " broadcast:" + valueAt.intent.getAction());
                            }
                        } else {
                            String str = ((ResolveInfo) obj).activityInfo.applicationInfo.packageName;
                            if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
                                Slog.i(BroadcastDispatcher.TAG, "getDeferredBootCompletedBroadcast uid:" + keyAt + " packageName:" + str + " broadcast:" + valueAt.intent.getAction());
                            }
                        }
                    }
                    sparseArray.removeAt(i);
                    if (sparseArray.size() == 0) {
                        sparseBooleanArray.clear();
                    }
                    return valueAt;
                }
            }
            return null;
        }

        private SparseArray<BroadcastRecord> getDeferredList(String str) {
            if (str.equals("android.intent.action.LOCKED_BOOT_COMPLETED")) {
                return this.mDeferredLockedBootCompletedBroadcasts;
            }
            if (str.equals("android.intent.action.BOOT_COMPLETED")) {
                return this.mDeferredBootCompletedBroadcasts;
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getBootCompletedBroadcastsUidsSize(String str) {
            SparseArray<BroadcastRecord> deferredList = getDeferredList(str);
            if (deferredList != null) {
                return deferredList.size();
            }
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getBootCompletedBroadcastsReceiversSize(String str) {
            SparseArray<BroadcastRecord> deferredList = getDeferredList(str);
            if (deferredList == null) {
                return 0;
            }
            int size = deferredList.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                i += deferredList.valueAt(i2).receivers.size();
            }
            return i;
        }

        @NeverCompile
        public void dump(Dumper dumper, String str) {
            SparseArray<BroadcastRecord> deferredList = getDeferredList(str);
            if (deferredList == null) {
                return;
            }
            int size = deferredList.size();
            for (int i = 0; i < size; i++) {
                dumper.dump(deferredList.valueAt(i));
            }
        }

        @NeverCompile
        public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
            int size = this.mDeferredLockedBootCompletedBroadcasts.size();
            for (int i = 0; i < size; i++) {
                this.mDeferredLockedBootCompletedBroadcasts.valueAt(i).dumpDebug(protoOutputStream, j);
            }
            int size2 = this.mDeferredBootCompletedBroadcasts.size();
            for (int i2 = 0; i2 < size2; i2++) {
                this.mDeferredBootCompletedBroadcasts.valueAt(i2).dumpDebug(protoOutputStream, j);
            }
        }

        @NeverCompile
        private void dumpBootCompletedBroadcastRecord(SparseArray<BroadcastRecord> sparseArray) {
            String str;
            int size = sparseArray.size();
            for (int i = 0; i < size; i++) {
                Object obj = sparseArray.valueAt(i).receivers.get(0);
                if (obj instanceof BroadcastFilter) {
                    str = ((BroadcastFilter) obj).receiverList.app.processName;
                } else {
                    str = ((ResolveInfo) obj).activityInfo.applicationInfo.packageName;
                }
                Slog.i(BroadcastDispatcher.TAG, "uid:" + sparseArray.keyAt(i) + " packageName:" + str + " receivers:" + sparseArray.valueAt(i).receivers.size());
            }
        }
    }

    private DeferredBootCompletedBroadcastPerUser getDeferredPerUser(int i) {
        if (this.mUser2Deferred.contains(i)) {
            return this.mUser2Deferred.get(i);
        }
        DeferredBootCompletedBroadcastPerUser deferredBootCompletedBroadcastPerUser = new DeferredBootCompletedBroadcastPerUser(i);
        this.mUser2Deferred.put(i, deferredBootCompletedBroadcastPerUser);
        return deferredBootCompletedBroadcastPerUser;
    }

    public void updateUidReadyForBootCompletedBroadcastLocked(int i) {
        getDeferredPerUser(UserHandle.getUserId(i)).updateUidReady(i);
    }

    private BroadcastRecord dequeueDeferredBootCompletedBroadcast() {
        boolean z = this.mQueue.mService.mConstants.mDeferBootCompletedBroadcast == 0;
        int size = this.mUser2Deferred.size();
        BroadcastRecord broadcastRecord = null;
        for (int i = 0; i < size; i++) {
            broadcastRecord = this.mUser2Deferred.valueAt(i).dequeueDeferredBootCompletedBroadcast(z);
            if (broadcastRecord != null) {
                break;
            }
        }
        return broadcastRecord;
    }

    public BroadcastDispatcher(BroadcastQueueImpl broadcastQueueImpl, BroadcastConstants broadcastConstants, Handler handler, Object obj) {
        IBroadcastDispatcherExt iBroadcastDispatcherExt = (IBroadcastDispatcherExt) ExtLoader.type(IBroadcastDispatcherExt.class).base(this).create();
        this.mBroadcastDispatcherExt = iBroadcastDispatcherExt;
        this.mQueue = broadcastQueueImpl;
        this.mConstants = broadcastConstants;
        this.mHandler = handler;
        this.mLock = obj;
        iBroadcastDispatcherExt.setBroadcastDispatcher(this);
        this.mBroadcastDispatcherExt.setAMS((ActivityManagerService) obj);
    }

    public void start() {
        AlarmManagerInternal alarmManagerInternal = (AlarmManagerInternal) LocalServices.getService(AlarmManagerInternal.class);
        this.mAlarm = alarmManagerInternal;
        alarmManagerInternal.registerInFlightListener(this.mAlarmListener);
    }

    public boolean isEmpty() {
        boolean z;
        synchronized (this.mLock) {
            z = isIdle() && getBootCompletedBroadcastsUidsSize("android.intent.action.LOCKED_BOOT_COMPLETED") == 0 && getBootCompletedBroadcastsUidsSize("android.intent.action.BOOT_COMPLETED") == 0;
        }
        return z;
    }

    public boolean isIdle() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mCurrentBroadcast == null && this.mOrderedBroadcasts.isEmpty() && this.mAlarmQueue.isEmpty() && isDeferralsListEmpty(this.mDeferredBroadcasts) && isDeferralsListEmpty(this.mAlarmDeferrals);
        }
        return z;
    }

    private static boolean isDeferralsBeyondBarrier(ArrayList<Deferrals> arrayList, long j) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (!isBeyondBarrier(arrayList.get(i).broadcasts, j)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isBeyondBarrier(ArrayList<BroadcastRecord> arrayList, long j) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).enqueueTime <= j) {
                return false;
            }
        }
        return true;
    }

    public boolean isBeyondBarrier(long j) {
        synchronized (this.mLock) {
            BroadcastRecord broadcastRecord = this.mCurrentBroadcast;
            boolean z = false;
            if (broadcastRecord != null && broadcastRecord.enqueueTime <= j) {
                return false;
            }
            if (isBeyondBarrier(this.mOrderedBroadcasts, j) && isBeyondBarrier(this.mAlarmQueue, j) && isDeferralsBeyondBarrier(this.mDeferredBroadcasts, j) && isDeferralsBeyondBarrier(this.mAlarmDeferrals, j)) {
                z = true;
            }
            return z;
        }
    }

    private static boolean isDispatchedInDeferrals(ArrayList<Deferrals> arrayList, Intent intent) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (!isDispatched(arrayList.get(i).broadcasts, intent)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isDispatched(ArrayList<BroadcastRecord> arrayList, Intent intent) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (intent.filterEquals(arrayList.get(i).intent)) {
                return false;
            }
        }
        return true;
    }

    public boolean isDispatched(Intent intent) {
        synchronized (this.mLock) {
            BroadcastRecord broadcastRecord = this.mCurrentBroadcast;
            boolean z = false;
            if (broadcastRecord != null && intent.filterEquals(broadcastRecord.intent)) {
                return false;
            }
            if (isDispatched(this.mOrderedBroadcasts, intent) && isDispatched(this.mAlarmQueue, intent) && isDispatchedInDeferrals(this.mDeferredBroadcasts, intent) && isDispatchedInDeferrals(this.mAlarmDeferrals, intent)) {
                z = true;
            }
            return z;
        }
    }

    private static int pendingInDeferralsList(ArrayList<Deferrals> arrayList) {
        int size = arrayList.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += arrayList.get(i2).size();
        }
        return i;
    }

    private static boolean isDeferralsListEmpty(ArrayList<Deferrals> arrayList) {
        return pendingInDeferralsList(arrayList) == 0;
    }

    public String describeStateLocked() {
        StringBuilder sb = new StringBuilder(128);
        if (this.mCurrentBroadcast != null) {
            sb.append("1 in flight, ");
        }
        sb.append(this.mOrderedBroadcasts.size());
        sb.append(" ordered");
        int size = this.mAlarmQueue.size();
        if (size > 0) {
            sb.append(", ");
            sb.append(size);
            sb.append(" alarms");
        }
        int pendingInDeferralsList = pendingInDeferralsList(this.mAlarmDeferrals);
        if (pendingInDeferralsList > 0) {
            sb.append(", ");
            sb.append(pendingInDeferralsList);
            sb.append(" deferrals in alarm recipients");
        }
        int pendingInDeferralsList2 = pendingInDeferralsList(this.mDeferredBroadcasts);
        if (pendingInDeferralsList2 > 0) {
            sb.append(", ");
            sb.append(pendingInDeferralsList2);
            sb.append(" deferred");
        }
        int bootCompletedBroadcastsUidsSize = getBootCompletedBroadcastsUidsSize("android.intent.action.LOCKED_BOOT_COMPLETED");
        if (bootCompletedBroadcastsUidsSize > 0) {
            sb.append(", ");
            sb.append(bootCompletedBroadcastsUidsSize);
            sb.append(" deferred LOCKED_BOOT_COMPLETED/");
            sb.append(getBootCompletedBroadcastsReceiversSize("android.intent.action.LOCKED_BOOT_COMPLETED"));
            sb.append(" receivers");
        }
        int bootCompletedBroadcastsUidsSize2 = getBootCompletedBroadcastsUidsSize("android.intent.action.BOOT_COMPLETED");
        if (bootCompletedBroadcastsUidsSize2 > 0) {
            sb.append(", ");
            sb.append(bootCompletedBroadcastsUidsSize2);
            sb.append(" deferred BOOT_COMPLETED/");
            sb.append(getBootCompletedBroadcastsReceiversSize("android.intent.action.BOOT_COMPLETED"));
            sb.append(" receivers");
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enqueueOrderedBroadcastLocked(BroadcastRecord broadcastRecord) {
        ArrayList<BroadcastRecord> arrayList;
        if (broadcastRecord.alarm && this.mQueue.mService.mConstants.mPrioritizeAlarmBroadcasts) {
            arrayList = this.mAlarmQueue;
        } else {
            arrayList = this.mOrderedBroadcasts;
        }
        List<Object> list = broadcastRecord.receivers;
        if (list == null || list.isEmpty()) {
            arrayList.add(broadcastRecord);
            return;
        }
        if ("android.intent.action.LOCKED_BOOT_COMPLETED".equals(broadcastRecord.intent.getAction())) {
            ActivityManagerService activityManagerService = this.mQueue.mService;
            getDeferredPerUser(broadcastRecord.userId).enqueueBootCompletedBroadcasts("android.intent.action.LOCKED_BOOT_COMPLETED", broadcastRecord.splitDeferredBootCompletedBroadcastLocked(activityManagerService.mInternal, activityManagerService.mConstants.mDeferBootCompletedBroadcast));
            if (broadcastRecord.receivers.isEmpty()) {
                return;
            }
            this.mOrderedBroadcasts.add(broadcastRecord);
            this.mBroadcastDispatcherExt.adjustQueueIfNecessary(this.mOrderedBroadcasts, broadcastRecord);
            return;
        }
        if ("android.intent.action.BOOT_COMPLETED".equals(broadcastRecord.intent.getAction())) {
            ActivityManagerService activityManagerService2 = this.mQueue.mService;
            getDeferredPerUser(broadcastRecord.userId).enqueueBootCompletedBroadcasts("android.intent.action.BOOT_COMPLETED", broadcastRecord.splitDeferredBootCompletedBroadcastLocked(activityManagerService2.mInternal, activityManagerService2.mConstants.mDeferBootCompletedBroadcast));
            if (broadcastRecord.receivers.isEmpty()) {
                return;
            }
            this.mOrderedBroadcasts.add(broadcastRecord);
            this.mBroadcastDispatcherExt.adjustQueueIfNecessary(this.mOrderedBroadcasts, broadcastRecord);
            return;
        }
        arrayList.add(broadcastRecord);
        this.mBroadcastDispatcherExt.adjustQueueIfNecessary(this.mOrderedBroadcasts, broadcastRecord);
    }

    private int getBootCompletedBroadcastsUidsSize(String str) {
        int size = this.mUser2Deferred.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += this.mUser2Deferred.valueAt(i2).getBootCompletedBroadcastsUidsSize(str);
        }
        return i;
    }

    private int getBootCompletedBroadcastsReceiversSize(String str) {
        int size = this.mUser2Deferred.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            i += this.mUser2Deferred.valueAt(i2).getBootCompletedBroadcastsReceiversSize(str);
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BroadcastRecord replaceBroadcastLocked(BroadcastRecord broadcastRecord, String str) {
        BroadcastRecord replaceBroadcastLocked = replaceBroadcastLocked(this.mOrderedBroadcasts, broadcastRecord, str);
        if (replaceBroadcastLocked == null) {
            replaceBroadcastLocked = replaceBroadcastLocked(this.mAlarmQueue, broadcastRecord, str);
        }
        if (replaceBroadcastLocked == null) {
            replaceBroadcastLocked = replaceDeferredBroadcastLocked(this.mAlarmDeferrals, broadcastRecord, str);
        }
        return replaceBroadcastLocked == null ? replaceDeferredBroadcastLocked(this.mDeferredBroadcasts, broadcastRecord, str) : replaceBroadcastLocked;
    }

    private BroadcastRecord replaceDeferredBroadcastLocked(ArrayList<Deferrals> arrayList, BroadcastRecord broadcastRecord, String str) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            BroadcastRecord replaceBroadcastLocked = replaceBroadcastLocked(arrayList.get(i).broadcasts, broadcastRecord, str);
            if (replaceBroadcastLocked != null) {
                return replaceBroadcastLocked;
            }
        }
        return null;
    }

    private BroadcastRecord replaceBroadcastLocked(ArrayList<BroadcastRecord> arrayList, BroadcastRecord broadcastRecord, String str) {
        Intent intent = broadcastRecord.intent;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            BroadcastRecord broadcastRecord2 = arrayList.get(size);
            if (broadcastRecord2.userId == broadcastRecord.userId && intent.filterEquals(broadcastRecord2.intent)) {
                if (ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                    Slog.v(TAG, "***** Replacing " + str + " [" + this.mQueue.mQueueName + "]: " + intent);
                }
                broadcastRecord.deferred = broadcastRecord2.deferred;
                arrayList.set(size, broadcastRecord);
                return broadcastRecord2;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean cleanupDisabledPackageReceiversLocked(String str, Set<String> set, int i, boolean z) {
        BroadcastRecord broadcastRecord;
        boolean cleanupBroadcastListDisabledReceiversLocked = cleanupBroadcastListDisabledReceiversLocked(this.mOrderedBroadcasts, str, set, i, z);
        if (z || !cleanupBroadcastListDisabledReceiversLocked) {
            cleanupBroadcastListDisabledReceiversLocked = cleanupBroadcastListDisabledReceiversLocked(this.mAlarmQueue, str, set, i, z);
        }
        if (z || !cleanupBroadcastListDisabledReceiversLocked) {
            ArrayList<BroadcastRecord> arrayList = new ArrayList<>();
            int size = this.mUser2Deferred.size();
            for (int i2 = 0; i2 < size; i2++) {
                SparseArray<BroadcastRecord> sparseArray = this.mUser2Deferred.valueAt(i2).mDeferredLockedBootCompletedBroadcasts;
                int size2 = sparseArray.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    arrayList.add(sparseArray.valueAt(i3));
                }
            }
            cleanupBroadcastListDisabledReceiversLocked = cleanupBroadcastListDisabledReceiversLocked(arrayList, str, set, i, z);
        }
        if (z || !cleanupBroadcastListDisabledReceiversLocked) {
            ArrayList<BroadcastRecord> arrayList2 = new ArrayList<>();
            int size3 = this.mUser2Deferred.size();
            for (int i4 = 0; i4 < size3; i4++) {
                SparseArray<BroadcastRecord> sparseArray2 = this.mUser2Deferred.valueAt(i4).mDeferredBootCompletedBroadcasts;
                int size4 = sparseArray2.size();
                for (int i5 = 0; i5 < size4; i5++) {
                    arrayList2.add(sparseArray2.valueAt(i5));
                }
            }
            cleanupBroadcastListDisabledReceiversLocked = cleanupBroadcastListDisabledReceiversLocked(arrayList2, str, set, i, z);
        }
        if (z || !cleanupBroadcastListDisabledReceiversLocked) {
            cleanupBroadcastListDisabledReceiversLocked |= cleanupDeferralsListDisabledReceiversLocked(this.mAlarmDeferrals, str, set, i, z);
        }
        if (z || !cleanupBroadcastListDisabledReceiversLocked) {
            cleanupBroadcastListDisabledReceiversLocked |= cleanupDeferralsListDisabledReceiversLocked(this.mDeferredBroadcasts, str, set, i, z);
        }
        return ((z || !cleanupBroadcastListDisabledReceiversLocked) && (broadcastRecord = this.mCurrentBroadcast) != null) ? cleanupBroadcastListDisabledReceiversLocked | broadcastRecord.cleanupDisabledPackageReceiversLocked(str, set, i, z) : cleanupBroadcastListDisabledReceiversLocked;
    }

    private boolean cleanupDeferralsListDisabledReceiversLocked(ArrayList<Deferrals> arrayList, String str, Set<String> set, int i, boolean z) {
        Iterator<Deferrals> it = arrayList.iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            z2 = cleanupBroadcastListDisabledReceiversLocked(it.next().broadcasts, str, set, i, z);
            if (!z && z2) {
                return true;
            }
        }
        return z2;
    }

    private boolean cleanupBroadcastListDisabledReceiversLocked(ArrayList<BroadcastRecord> arrayList, String str, Set<String> set, int i, boolean z) {
        Iterator<BroadcastRecord> it = arrayList.iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            z2 |= it.next().cleanupDisabledPackageReceiversLocked(str, set, i, z);
            if (!z && z2) {
                return true;
            }
        }
        return z2;
    }

    @NeverCompile
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        BroadcastRecord broadcastRecord = this.mCurrentBroadcast;
        if (broadcastRecord != null) {
            broadcastRecord.dumpDebug(protoOutputStream, j);
        }
        Iterator<Deferrals> it = this.mAlarmDeferrals.iterator();
        while (it.hasNext()) {
            it.next().dumpDebug(protoOutputStream, j);
        }
        Iterator<BroadcastRecord> it2 = this.mOrderedBroadcasts.iterator();
        while (it2.hasNext()) {
            it2.next().dumpDebug(protoOutputStream, j);
        }
        Iterator<BroadcastRecord> it3 = this.mAlarmQueue.iterator();
        while (it3.hasNext()) {
            it3.next().dumpDebug(protoOutputStream, j);
        }
        Iterator<Deferrals> it4 = this.mDeferredBroadcasts.iterator();
        while (it4.hasNext()) {
            it4.next().dumpDebug(protoOutputStream, j);
        }
        int size = this.mUser2Deferred.size();
        for (int i = 0; i < size; i++) {
            this.mUser2Deferred.valueAt(i).dumpDebug(protoOutputStream, j);
        }
    }

    public BroadcastRecord getActiveBroadcastLocked() {
        return this.mCurrentBroadcast;
    }

    public BroadcastRecord getNextBroadcastLocked(long j) {
        BroadcastRecord broadcastRecord = this.mCurrentBroadcast;
        if (broadcastRecord != null) {
            return broadcastRecord;
        }
        BroadcastRecord remove = !this.mAlarmQueue.isEmpty() ? this.mAlarmQueue.remove(0) : null;
        if (remove == null) {
            remove = dequeueDeferredBootCompletedBroadcast();
        }
        if (remove == null && !this.mAlarmDeferrals.isEmpty()) {
            remove = popLocked(this.mAlarmDeferrals);
            if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL && remove != null) {
                Slog.i(TAG, "Next broadcast from alarm targets: " + remove);
            }
        }
        boolean z = !this.mOrderedBroadcasts.isEmpty();
        if (remove == null && !this.mDeferredBroadcasts.isEmpty()) {
            int i = 0;
            while (true) {
                if (i >= this.mDeferredBroadcasts.size()) {
                    break;
                }
                Deferrals deferrals = this.mDeferredBroadcasts.get(i);
                if (j < deferrals.deferUntil && z) {
                    break;
                }
                if (deferrals.broadcasts.size() > 0) {
                    remove = deferrals.broadcasts.remove(0);
                    this.mDeferredBroadcasts.remove(i);
                    long calculateDeferral = calculateDeferral(deferrals.deferredBy);
                    deferrals.deferredBy = calculateDeferral;
                    deferrals.deferUntil += calculateDeferral;
                    insertLocked(this.mDeferredBroadcasts, deferrals);
                    if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
                        Slog.i(TAG, "Next broadcast from deferrals " + remove + ", deferUntil now " + deferrals.deferUntil);
                    }
                } else {
                    i++;
                }
            }
        }
        if (remove == null && z) {
            remove = this.mOrderedBroadcasts.remove(0);
            if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
                Slog.i(TAG, "Next broadcast from main queue: " + remove);
            }
        }
        this.mCurrentBroadcast = remove;
        return remove;
    }

    public void retireBroadcastLocked(BroadcastRecord broadcastRecord) {
        if (broadcastRecord != this.mCurrentBroadcast) {
            Slog.wtf(TAG, "Retiring broadcast " + broadcastRecord + " doesn't match current outgoing " + this.mCurrentBroadcast);
        }
        this.mCurrentBroadcast = null;
    }

    public boolean isDeferringLocked(int i) {
        Deferrals findUidLocked = findUidLocked(i);
        if (findUidLocked == null || !findUidLocked.broadcasts.isEmpty() || SystemClock.uptimeMillis() < findUidLocked.deferUntil) {
            return findUidLocked != null;
        }
        if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
            Slog.i(TAG, "No longer deferring broadcasts to uid " + findUidLocked.uid);
        }
        removeDeferral(findUidLocked);
        return false;
    }

    public void startDeferring(int i) {
        synchronized (this.mLock) {
            Deferrals findUidLocked = findUidLocked(i);
            if (findUidLocked == null) {
                Deferrals deferrals = new Deferrals(i, SystemClock.uptimeMillis(), this.mConstants.DEFERRAL, this.mAlarmUids.get(i, 0));
                if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
                    Slog.i(TAG, "Now deferring broadcasts to " + i + " until " + deferrals.deferUntil);
                }
                if (deferrals.alarmCount == 0) {
                    insertLocked(this.mDeferredBroadcasts, deferrals);
                    scheduleDeferralCheckLocked(true);
                } else {
                    this.mAlarmDeferrals.add(deferrals);
                }
            } else {
                findUidLocked.deferredBy = this.mConstants.DEFERRAL;
                if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
                    Slog.i(TAG, "Uid " + i + " slow again, deferral interval reset to " + findUidLocked.deferredBy);
                }
            }
        }
    }

    public void addDeferredBroadcast(int i, BroadcastRecord broadcastRecord) {
        if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
            Slog.i(TAG, "Enqueuing deferred broadcast " + broadcastRecord);
        }
        synchronized (this.mLock) {
            Deferrals findUidLocked = findUidLocked(i);
            if (findUidLocked == null) {
                Slog.wtf(TAG, "Adding deferred broadcast but not tracking " + i);
            } else if (broadcastRecord == null) {
                Slog.wtf(TAG, "Deferring null broadcast to " + i);
            } else {
                broadcastRecord.deferred = true;
                findUidLocked.add(broadcastRecord);
            }
        }
    }

    public void scheduleDeferralCheckLocked(boolean z) {
        if ((z || !this.mRecheckScheduled) && !this.mDeferredBroadcasts.isEmpty()) {
            Deferrals deferrals = this.mDeferredBroadcasts.get(0);
            if (deferrals.broadcasts.isEmpty()) {
                return;
            }
            this.mHandler.removeCallbacks(this.mScheduleRunnable);
            this.mHandler.postAtTime(this.mScheduleRunnable, deferrals.deferUntil);
            this.mRecheckScheduled = true;
            if (ActivityManagerDebugConfig.DEBUG_BROADCAST_DEFERRAL) {
                Slog.i(TAG, "Scheduling deferred broadcast recheck at " + deferrals.deferUntil);
            }
        }
    }

    public void cancelDeferralsLocked() {
        zeroDeferralTimes(this.mAlarmDeferrals);
        zeroDeferralTimes(this.mDeferredBroadcasts);
    }

    private static void zeroDeferralTimes(ArrayList<Deferrals> arrayList) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            Deferrals deferrals = arrayList.get(i);
            deferrals.deferredBy = 0L;
            deferrals.deferUntil = 0L;
        }
    }

    private Deferrals findUidLocked(int i) {
        Deferrals findUidLocked = findUidLocked(i, this.mDeferredBroadcasts);
        return findUidLocked == null ? findUidLocked(i, this.mAlarmDeferrals) : findUidLocked;
    }

    private boolean removeDeferral(Deferrals deferrals) {
        boolean remove = this.mDeferredBroadcasts.remove(deferrals);
        return !remove ? this.mAlarmDeferrals.remove(deferrals) : remove;
    }

    private static Deferrals findUidLocked(int i, ArrayList<Deferrals> arrayList) {
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            Deferrals deferrals = arrayList.get(i2);
            if (i == deferrals.uid) {
                return deferrals;
            }
        }
        return null;
    }

    private static BroadcastRecord popLocked(ArrayList<Deferrals> arrayList) {
        Deferrals deferrals = arrayList.get(0);
        if (deferrals.broadcasts.isEmpty()) {
            return null;
        }
        return deferrals.broadcasts.remove(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void insertLocked(ArrayList<Deferrals> arrayList, Deferrals deferrals) {
        int size = arrayList.size();
        int i = 0;
        while (i < size && deferrals.deferUntil >= arrayList.get(i).deferUntil) {
            i++;
        }
        arrayList.add(i, deferrals);
    }

    private long calculateDeferral(long j) {
        return Math.max(this.mConstants.DEFERRAL_FLOOR, ((float) j) * r2.DEFERRAL_DECAY_FACTOR);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NeverCompile
    public boolean dumpLocked(PrintWriter printWriter, String str, String str2, SimpleDateFormat simpleDateFormat) {
        Dumper dumper = new Dumper(printWriter, str2, str, simpleDateFormat);
        dumper.setHeading("Currently in flight");
        dumper.setLabel("In-Flight Ordered Broadcast");
        BroadcastRecord broadcastRecord = this.mCurrentBroadcast;
        if (broadcastRecord != null) {
            dumper.dump(broadcastRecord);
        } else {
            printWriter.println("  (null)");
        }
        boolean didPrint = dumper.didPrint() | false;
        dumper.setHeading("Active alarm broadcasts");
        dumper.setLabel("Active Alarm Broadcast");
        Iterator<BroadcastRecord> it = this.mAlarmQueue.iterator();
        while (it.hasNext()) {
            dumper.dump(it.next());
        }
        boolean didPrint2 = didPrint | dumper.didPrint();
        dumper.setHeading("Active ordered broadcasts");
        dumper.setLabel("Active Ordered Broadcast");
        Iterator<Deferrals> it2 = this.mAlarmDeferrals.iterator();
        while (it2.hasNext()) {
            it2.next().dumpLocked(dumper);
        }
        Iterator<BroadcastRecord> it3 = this.mOrderedBroadcasts.iterator();
        while (it3.hasNext()) {
            dumper.dump(it3.next());
        }
        boolean didPrint3 = didPrint2 | dumper.didPrint();
        dumper.setHeading("Deferred ordered broadcasts");
        dumper.setLabel("Deferred Ordered Broadcast");
        Iterator<Deferrals> it4 = this.mDeferredBroadcasts.iterator();
        while (it4.hasNext()) {
            it4.next().dumpLocked(dumper);
        }
        boolean didPrint4 = didPrint3 | dumper.didPrint();
        dumper.setHeading("Deferred LOCKED_BOOT_COMPLETED broadcasts");
        dumper.setLabel("Deferred LOCKED_BOOT_COMPLETED Broadcast");
        int size = this.mUser2Deferred.size();
        for (int i = 0; i < size; i++) {
            this.mUser2Deferred.valueAt(i).dump(dumper, "android.intent.action.LOCKED_BOOT_COMPLETED");
        }
        boolean didPrint5 = didPrint4 | dumper.didPrint();
        dumper.setHeading("Deferred BOOT_COMPLETED broadcasts");
        dumper.setLabel("Deferred BOOT_COMPLETED Broadcast");
        int size2 = this.mUser2Deferred.size();
        for (int i2 = 0; i2 < size2; i2++) {
            this.mUser2Deferred.valueAt(i2).dump(dumper, "android.intent.action.BOOT_COMPLETED");
        }
        return dumper.didPrint() | didPrint5;
    }

    public IBroadcastDispatcherWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class BroadcastDispatcherWrapper implements IBroadcastDispatcherWrapper {
        private BroadcastDispatcherWrapper() {
        }

        @Override // com.android.server.am.IBroadcastDispatcherWrapper
        public IBroadcastDispatcherExt getExtImpl() {
            return BroadcastDispatcher.this.mBroadcastDispatcherExt;
        }

        @Override // com.android.server.am.IBroadcastDispatcherWrapper
        public ArrayList<BroadcastRecord> getOrderedBroadcasts() {
            return BroadcastDispatcher.this.mOrderedBroadcasts;
        }
    }
}
