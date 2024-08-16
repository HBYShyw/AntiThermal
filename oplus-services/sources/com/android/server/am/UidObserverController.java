package com.android.server.am;

import android.app.ActivityManager;
import android.app.IUidObserver;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Slog;
import android.util.SparseIntArray;
import android.util.proto.ProtoOutputStream;
import android.util.proto.ProtoUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UidObserverController {
    private static final int SLOW_UID_OBSERVER_THRESHOLD_MS = 20;
    private static final boolean VALIDATE_UID_STATES = true;
    private final Handler mHandler;

    @GuardedBy({"mLock"})
    private int mUidChangeDispatchCount;
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    final RemoteCallbackList<IUidObserver> mUidObservers = new RemoteCallbackList<>();

    @GuardedBy({"mLock"})
    private final ArrayList<ChangeRecord> mPendingUidChanges = new ArrayList<>();

    @GuardedBy({"mLock"})
    private final ArrayList<ChangeRecord> mAvailUidChanges = new ArrayList<>();
    private ChangeRecord[] mActiveUidChanges = new ChangeRecord[5];
    private final Runnable mDispatchRunnable = new Runnable() { // from class: com.android.server.am.UidObserverController$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            UidObserverController.this.dispatchUidsChanged();
        }
    };
    private final ActiveUids mValidateUids = new ActiveUids(null, false);

    @VisibleForTesting
    static int mergeWithPendingChange(int i, int i2) {
        if ((i & 6) == 0) {
            i |= i2 & 6;
        }
        if ((i & 24) == 0) {
            i |= i2 & 24;
        }
        if ((i & 1) != 0) {
            i &= -13;
        }
        if ((i2 & 32) != 0) {
            i |= 32;
        }
        if ((i2 & Integer.MIN_VALUE) != 0) {
            i |= Integer.MIN_VALUE;
        }
        return (i2 & 64) != 0 ? i | 64 : i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UidObserverController(Handler handler) {
        this.mHandler = handler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder register(IUidObserver iUidObserver, int i, int i2, String str, int i3, int[] iArr) {
        Binder binder = new Binder("UidObserver-" + str + "-" + UUID.randomUUID().toString());
        synchronized (this.mLock) {
            this.mUidObservers.register(iUidObserver, new UidObserverRegistration(i3, str, i, i2, ActivityManager.checkUidPermission("android.permission.INTERACT_ACROSS_USERS_FULL", i3) == 0, iArr, binder));
        }
        return binder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregister(IUidObserver iUidObserver) {
        synchronized (this.mLock) {
            this.mUidObservers.unregister(iUidObserver);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void addUidToObserver(IBinder iBinder, int i) {
        this.mHandler.sendMessage(Message.obtain(this.mHandler, 80, i, 0, iBinder));
    }

    public final void addUidToObserverImpl(IBinder iBinder, int i) {
        int beginBroadcast = this.mUidObservers.beginBroadcast();
        while (true) {
            int i2 = beginBroadcast - 1;
            if (beginBroadcast <= 0) {
                break;
            }
            UidObserverRegistration uidObserverRegistration = (UidObserverRegistration) this.mUidObservers.getBroadcastCookie(i2);
            if (uidObserverRegistration.getToken().equals(iBinder)) {
                uidObserverRegistration.addUid(i);
                break;
            } else {
                if (i2 == 0) {
                    Slog.e(ActivityManagerService.TAG_UID_OBSERVERS, "Unable to find UidObserver by token");
                }
                beginBroadcast = i2;
            }
        }
        this.mUidObservers.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void removeUidFromObserver(IBinder iBinder, int i) {
        this.mHandler.sendMessage(Message.obtain(this.mHandler, 81, i, 0, iBinder));
    }

    public final void removeUidFromObserverImpl(IBinder iBinder, int i) {
        int beginBroadcast = this.mUidObservers.beginBroadcast();
        while (true) {
            int i2 = beginBroadcast - 1;
            if (beginBroadcast <= 0) {
                break;
            }
            UidObserverRegistration uidObserverRegistration = (UidObserverRegistration) this.mUidObservers.getBroadcastCookie(i2);
            if (uidObserverRegistration.getToken().equals(iBinder)) {
                uidObserverRegistration.removeUid(i);
                break;
            } else {
                if (i2 == 0) {
                    Slog.e(ActivityManagerService.TAG_UID_OBSERVERS, "Unable to find UidObserver by token");
                }
                beginBroadcast = i2;
            }
        }
        this.mUidObservers.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int enqueueUidChange(ChangeRecord changeRecord, int i, int i2, int i3, int i4, long j, int i5, boolean z) {
        synchronized (this.mLock) {
            if (this.mPendingUidChanges.size() == 0) {
                if (ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                    Slog.i(ActivityManagerService.TAG_UID_OBSERVERS, "*** Enqueueing dispatch uid changed!");
                }
                this.mHandler.post(this.mDispatchRunnable);
            }
            if (changeRecord == null) {
                changeRecord = getOrCreateChangeRecordLocked();
            }
            if (!changeRecord.isPending) {
                changeRecord.isPending = true;
                this.mPendingUidChanges.add(changeRecord);
            } else {
                i2 = mergeWithPendingChange(i2, changeRecord.change);
            }
            changeRecord.uid = i;
            changeRecord.change = i2;
            changeRecord.procState = i3;
            changeRecord.procAdj = i4;
            changeRecord.procStateSeq = j;
            changeRecord.capability = i5;
            changeRecord.ephemeral = z;
        }
        return i2;
    }

    ArrayList<ChangeRecord> getPendingUidChangesForTest() {
        return this.mPendingUidChanges;
    }

    ActiveUids getValidateUidsForTest() {
        return this.mValidateUids;
    }

    Runnable getDispatchRunnableForTest() {
        return this.mDispatchRunnable;
    }

    @GuardedBy({"mLock"})
    private ChangeRecord getOrCreateChangeRecordLocked() {
        ChangeRecord changeRecord;
        int size = this.mAvailUidChanges.size();
        if (size > 0) {
            changeRecord = this.mAvailUidChanges.remove(size - 1);
            if (ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                Slog.i(ActivityManagerService.TAG_UID_OBSERVERS, "Retrieving available item: " + changeRecord);
            }
        } else {
            changeRecord = new ChangeRecord();
            if (ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                Slog.i(ActivityManagerService.TAG_UID_OBSERVERS, "Allocating new item: " + changeRecord);
            }
        }
        return changeRecord;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void dispatchUidsChanged() {
        int size;
        synchronized (this.mLock) {
            size = this.mPendingUidChanges.size();
            if (this.mActiveUidChanges.length < size) {
                this.mActiveUidChanges = new ChangeRecord[size];
            }
            for (int i = 0; i < size; i++) {
                ChangeRecord changeRecord = this.mPendingUidChanges.get(i);
                this.mActiveUidChanges[i] = getOrCreateChangeRecordLocked();
                changeRecord.copyTo(this.mActiveUidChanges[i]);
                changeRecord.isPending = false;
            }
            this.mPendingUidChanges.clear();
            if (ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                Slog.i(ActivityManagerService.TAG_UID_OBSERVERS, "*** Delivering " + size + " uid changes");
            }
            this.mUidChangeDispatchCount += size;
        }
        int beginBroadcast = this.mUidObservers.beginBroadcast();
        while (true) {
            int i2 = beginBroadcast - 1;
            if (beginBroadcast <= 0) {
                break;
            }
            dispatchUidsChangedForObserver(this.mUidObservers.getBroadcastItem(i2), (UidObserverRegistration) this.mUidObservers.getBroadcastCookie(i2), size);
            beginBroadcast = i2;
        }
        this.mUidObservers.finishBroadcast();
        if (this.mUidObservers.getRegisteredCallbackCount() > 0) {
            for (int i3 = 0; i3 < size; i3++) {
                ChangeRecord changeRecord2 = this.mActiveUidChanges[i3];
                if ((changeRecord2.change & 1) != 0) {
                    this.mValidateUids.remove(changeRecord2.uid);
                } else {
                    UidRecord uidRecord = this.mValidateUids.get(changeRecord2.uid);
                    if (uidRecord == null) {
                        uidRecord = new UidRecord(changeRecord2.uid, null);
                        this.mValidateUids.put(changeRecord2.uid, uidRecord);
                    }
                    int i4 = changeRecord2.change;
                    if ((i4 & 2) != 0) {
                        uidRecord.setIdle(true);
                    } else if ((i4 & 4) != 0) {
                        uidRecord.setIdle(false);
                    }
                    uidRecord.setSetProcState(changeRecord2.procState);
                    uidRecord.setCurProcState(changeRecord2.procState);
                    uidRecord.setSetCapability(changeRecord2.capability);
                    uidRecord.setCurCapability(changeRecord2.capability);
                }
            }
        }
        synchronized (this.mLock) {
            for (int i5 = 0; i5 < size; i5++) {
                ChangeRecord changeRecord3 = this.mActiveUidChanges[i5];
                changeRecord3.isPending = false;
                this.mAvailUidChanges.add(changeRecord3);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:90:0x0176, code lost:
    
        if (r13.procState != 20) goto L87;
     */
    /* JADX WARN: Removed duplicated region for block: B:100:0x018d A[Catch: RemoteException -> 0x0211, TryCatch #0 {RemoteException -> 0x0211, blocks: (B:9:0x0011, B:14:0x0025, B:16:0x0035, B:22:0x0042, B:28:0x004f, B:34:0x005e, B:36:0x0066, B:38:0x006a, B:39:0x0082, B:40:0x00b5, B:42:0x00bd, B:44:0x00c1, B:46:0x00c5, B:47:0x00dd, B:48:0x00e3, B:50:0x00e7, B:52:0x00eb, B:53:0x0101, B:54:0x0106, B:56:0x010c, B:58:0x0114, B:60:0x0118, B:61:0x0130, B:62:0x0137, B:64:0x013b, B:66:0x01f8, B:68:0x0202, B:70:0x0206, B:73:0x0145, B:75:0x014c, B:77:0x0152, B:79:0x015d, B:82:0x0166, B:89:0x0174, B:92:0x017b, B:94:0x0183, B:97:0x018a, B:100:0x018d, B:102:0x0191, B:103:0x01b9, B:105:0x01bd, B:106:0x01c4, B:107:0x01e5, B:109:0x01ed, B:111:0x01f1, B:114:0x008a, B:116:0x008e, B:118:0x0096, B:120:0x009a, B:121:0x00b0), top: B:8:0x0011 }] */
    /* JADX WARN: Removed duplicated region for block: B:109:0x01ed A[Catch: RemoteException -> 0x0211, TryCatch #0 {RemoteException -> 0x0211, blocks: (B:9:0x0011, B:14:0x0025, B:16:0x0035, B:22:0x0042, B:28:0x004f, B:34:0x005e, B:36:0x0066, B:38:0x006a, B:39:0x0082, B:40:0x00b5, B:42:0x00bd, B:44:0x00c1, B:46:0x00c5, B:47:0x00dd, B:48:0x00e3, B:50:0x00e7, B:52:0x00eb, B:53:0x0101, B:54:0x0106, B:56:0x010c, B:58:0x0114, B:60:0x0118, B:61:0x0130, B:62:0x0137, B:64:0x013b, B:66:0x01f8, B:68:0x0202, B:70:0x0206, B:73:0x0145, B:75:0x014c, B:77:0x0152, B:79:0x015d, B:82:0x0166, B:89:0x0174, B:92:0x017b, B:94:0x0183, B:97:0x018a, B:100:0x018d, B:102:0x0191, B:103:0x01b9, B:105:0x01bd, B:106:0x01c4, B:107:0x01e5, B:109:0x01ed, B:111:0x01f1, B:114:0x008a, B:116:0x008e, B:118:0x0096, B:120:0x009a, B:121:0x00b0), top: B:8:0x0011 }] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0183 A[Catch: RemoteException -> 0x0211, TryCatch #0 {RemoteException -> 0x0211, blocks: (B:9:0x0011, B:14:0x0025, B:16:0x0035, B:22:0x0042, B:28:0x004f, B:34:0x005e, B:36:0x0066, B:38:0x006a, B:39:0x0082, B:40:0x00b5, B:42:0x00bd, B:44:0x00c1, B:46:0x00c5, B:47:0x00dd, B:48:0x00e3, B:50:0x00e7, B:52:0x00eb, B:53:0x0101, B:54:0x0106, B:56:0x010c, B:58:0x0114, B:60:0x0118, B:61:0x0130, B:62:0x0137, B:64:0x013b, B:66:0x01f8, B:68:0x0202, B:70:0x0206, B:73:0x0145, B:75:0x014c, B:77:0x0152, B:79:0x015d, B:82:0x0166, B:89:0x0174, B:92:0x017b, B:94:0x0183, B:97:0x018a, B:100:0x018d, B:102:0x0191, B:103:0x01b9, B:105:0x01bd, B:106:0x01c4, B:107:0x01e5, B:109:0x01ed, B:111:0x01f1, B:114:0x008a, B:116:0x008e, B:118:0x0096, B:120:0x009a, B:121:0x00b0), top: B:8:0x0011 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void dispatchUidsChangedForObserver(IUidObserver iUidObserver, UidObserverRegistration uidObserverRegistration, int i) {
        boolean z;
        int i2;
        int i3;
        if (iUidObserver == null) {
            return;
        }
        boolean z2 = false;
        int i4 = 0;
        while (i4 < i) {
            try {
                ChangeRecord changeRecord = this.mActiveUidChanges[i4];
                long uptimeMillis = SystemClock.uptimeMillis();
                int i5 = changeRecord.change;
                if (uidObserverRegistration.isWatchingUid(changeRecord.uid) && ((UserHandle.getUserId(changeRecord.uid) == UserHandle.getUserId(uidObserverRegistration.mUid) || uidObserverRegistration.mCanInteractAcrossUsers) && ((i5 != Integer.MIN_VALUE || (uidObserverRegistration.mWhich & 1) != 0) && (i5 != 64 || (uidObserverRegistration.mWhich & 64) != 0)))) {
                    if ((i5 & 2) != 0) {
                        if ((uidObserverRegistration.mWhich & 4) != 0) {
                            if (ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                                Slog.i(ActivityManagerService.TAG_UID_OBSERVERS, "UID idle uid=" + changeRecord.uid);
                            }
                            iUidObserver.onUidIdle(changeRecord.uid, changeRecord.ephemeral);
                        }
                    } else if ((i5 & 4) != 0 && (uidObserverRegistration.mWhich & 8) != 0) {
                        if (ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                            Slog.i(ActivityManagerService.TAG_UID_OBSERVERS, "UID active uid=" + changeRecord.uid);
                        }
                        iUidObserver.onUidActive(changeRecord.uid);
                    }
                    if ((uidObserverRegistration.mWhich & 16) != 0) {
                        if ((i5 & 8) != 0) {
                            if (ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                                Slog.i(ActivityManagerService.TAG_UID_OBSERVERS, "UID cached uid=" + changeRecord.uid);
                            }
                            iUidObserver.onUidCachedChanged(changeRecord.uid, true);
                        } else if ((i5 & 16) != 0) {
                            if (ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                                Slog.i(ActivityManagerService.TAG_UID_OBSERVERS, "UID active uid=" + changeRecord.uid);
                            }
                            iUidObserver.onUidCachedChanged(changeRecord.uid, z2);
                        }
                    }
                    if ((i5 & 1) != 0) {
                        if ((uidObserverRegistration.mWhich & 2) != 0) {
                            if (ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                                Slog.i(ActivityManagerService.TAG_UID_OBSERVERS, "UID gone uid=" + changeRecord.uid);
                            }
                            iUidObserver.onUidGone(changeRecord.uid, changeRecord.ephemeral);
                        }
                        SparseIntArray sparseIntArray = uidObserverRegistration.mLastProcStates;
                        if (sparseIntArray != null) {
                            sparseIntArray.delete(changeRecord.uid);
                        }
                        i2 = 20;
                    } else {
                        if ((uidObserverRegistration.mWhich & 1) != 0) {
                            if (uidObserverRegistration.mCutpoint >= 0) {
                                int i6 = uidObserverRegistration.mLastProcStates.get(changeRecord.uid, -1);
                                if (i6 != -1) {
                                    if ((i6 <= uidObserverRegistration.mCutpoint ? true : z2) != (changeRecord.procState <= uidObserverRegistration.mCutpoint ? true : z2)) {
                                    }
                                }
                                if ((uidObserverRegistration.mWhich & 32) != 0) {
                                    z |= (i5 & 32) != 0 ? true : z2;
                                }
                                if (z) {
                                    if (ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                                        Slog.i(ActivityManagerService.TAG_UID_OBSERVERS, "UID CHANGED uid=" + changeRecord.uid + ": " + changeRecord.procState + ": " + changeRecord.capability);
                                    }
                                    SparseIntArray sparseIntArray2 = uidObserverRegistration.mLastProcStates;
                                    if (sparseIntArray2 != null) {
                                        sparseIntArray2.put(changeRecord.uid, changeRecord.procState);
                                    }
                                    i2 = 20;
                                    i3 = i5;
                                    iUidObserver.onUidStateChanged(changeRecord.uid, changeRecord.procState, changeRecord.procStateSeq, changeRecord.capability);
                                } else {
                                    i2 = 20;
                                    i3 = i5;
                                }
                                if ((uidObserverRegistration.mWhich & 64) != 0 && (i3 & 64) != 0) {
                                    iUidObserver.onUidProcAdjChanged(changeRecord.uid, changeRecord.procAdj);
                                }
                            }
                            z = true;
                            if ((uidObserverRegistration.mWhich & 32) != 0) {
                            }
                            if (z) {
                            }
                            if ((uidObserverRegistration.mWhich & 64) != 0) {
                                iUidObserver.onUidProcAdjChanged(changeRecord.uid, changeRecord.procAdj);
                            }
                        }
                        z = z2;
                        if ((uidObserverRegistration.mWhich & 32) != 0) {
                        }
                        if (z) {
                        }
                        if ((uidObserverRegistration.mWhich & 64) != 0) {
                        }
                    }
                    int uptimeMillis2 = (int) (SystemClock.uptimeMillis() - uptimeMillis);
                    if (uidObserverRegistration.mMaxDispatchTime < uptimeMillis2) {
                        uidObserverRegistration.mMaxDispatchTime = uptimeMillis2;
                    }
                    if (uptimeMillis2 >= i2) {
                        uidObserverRegistration.mSlowDispatchCount++;
                    }
                }
                i4++;
                z2 = false;
            } catch (RemoteException unused) {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UidRecord getValidateUidRecord(int i) {
        return this.mValidateUids.get(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        synchronized (this.mLock) {
            int registeredCallbackCount = this.mUidObservers.getRegisteredCallbackCount();
            boolean z = false;
            for (int i = 0; i < registeredCallbackCount; i++) {
                UidObserverRegistration uidObserverRegistration = (UidObserverRegistration) this.mUidObservers.getRegisteredCallbackCookie(i);
                if (str == null || str.equals(uidObserverRegistration.mPkg)) {
                    if (!z) {
                        printWriter.println("  mUidObservers:");
                        z = true;
                    }
                    uidObserverRegistration.dump(printWriter, this.mUidObservers.getRegisteredCallbackItem(i));
                }
            }
            if (str == null) {
                printWriter.println();
                printWriter.print("  mUidChangeDispatchCount=");
                printWriter.print(this.mUidChangeDispatchCount);
                printWriter.println();
                printWriter.println("  Slow UID dispatches:");
                for (int i2 = 0; i2 < registeredCallbackCount; i2++) {
                    UidObserverRegistration uidObserverRegistration2 = (UidObserverRegistration) this.mUidObservers.getRegisteredCallbackCookie(i2);
                    printWriter.print("    ");
                    printWriter.print(this.mUidObservers.getRegisteredCallbackItem(i2).getClass().getTypeName());
                    printWriter.print(": ");
                    printWriter.print(uidObserverRegistration2.mSlowDispatchCount);
                    printWriter.print(" / Max ");
                    printWriter.print(uidObserverRegistration2.mMaxDispatchTime);
                    printWriter.println("ms");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, String str) {
        synchronized (this.mLock) {
            int registeredCallbackCount = this.mUidObservers.getRegisteredCallbackCount();
            for (int i = 0; i < registeredCallbackCount; i++) {
                UidObserverRegistration uidObserverRegistration = (UidObserverRegistration) this.mUidObservers.getRegisteredCallbackCookie(i);
                if (str == null || str.equals(uidObserverRegistration.mPkg)) {
                    uidObserverRegistration.dumpDebug(protoOutputStream, 2246267895831L);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean dumpValidateUids(PrintWriter printWriter, String str, int i, String str2, boolean z) {
        return this.mValidateUids.dump(printWriter, str, i, str2, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpValidateUidsProto(ProtoOutputStream protoOutputStream, String str, int i, long j) {
        this.mValidateUids.dumpProto(protoOutputStream, str, i, j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class ChangeRecord {
        public int capability;
        public int change;
        public boolean ephemeral;
        public boolean isPending;
        public int procAdj;
        public int procState;
        public long procStateSeq;
        public int uid;

        void copyTo(ChangeRecord changeRecord) {
            changeRecord.isPending = this.isPending;
            changeRecord.uid = this.uid;
            changeRecord.change = this.change;
            changeRecord.procState = this.procState;
            changeRecord.procAdj = this.procAdj;
            changeRecord.capability = this.capability;
            changeRecord.ephemeral = this.ephemeral;
            changeRecord.procStateSeq = this.procStateSeq;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class UidObserverRegistration {
        private static final int[] ORIG_ENUMS = {4, 8, 2, 1, 32, 64};
        private static final int[] PROTO_ENUMS = {3, 4, 2, 1, 6, 7};
        private final boolean mCanInteractAcrossUsers;
        private final int mCutpoint;
        final SparseIntArray mLastProcStates;
        int mMaxDispatchTime;
        private final String mPkg;
        int mSlowDispatchCount;
        private final IBinder mToken;
        private final int mUid;
        private int[] mUids;
        private final int mWhich;

        UidObserverRegistration(int i, String str, int i2, int i3, boolean z, int[] iArr, IBinder iBinder) {
            this.mUid = i;
            this.mPkg = str;
            this.mWhich = i2;
            this.mCutpoint = i3;
            this.mCanInteractAcrossUsers = z;
            if (iArr != null) {
                int[] iArr2 = (int[]) iArr.clone();
                this.mUids = iArr2;
                Arrays.sort(iArr2);
            } else {
                this.mUids = null;
            }
            this.mToken = iBinder;
            this.mLastProcStates = i3 >= 0 ? new SparseIntArray() : null;
        }

        boolean isWatchingUid(int i) {
            int[] iArr = this.mUids;
            return iArr == null || Arrays.binarySearch(iArr, i) >= 0;
        }

        void addUid(int i) {
            int[] iArr = this.mUids;
            if (iArr == null) {
                return;
            }
            this.mUids = new int[iArr.length + 1];
            boolean z = false;
            for (int i2 = 0; i2 < iArr.length; i2++) {
                if (!z) {
                    int i3 = iArr[i2];
                    if (i3 < i) {
                        this.mUids[i2] = i3;
                    } else {
                        if (i3 == i) {
                            this.mUids = iArr;
                            return;
                        }
                        int[] iArr2 = this.mUids;
                        iArr2[i2] = i;
                        iArr2[i2 + 1] = iArr[i2];
                        z = true;
                    }
                } else {
                    this.mUids[i2 + 1] = iArr[i2];
                }
            }
            if (z) {
                return;
            }
            this.mUids[iArr.length] = i;
        }

        void removeUid(int i) {
            int[] iArr = this.mUids;
            if (iArr == null || iArr.length == 0) {
                return;
            }
            this.mUids = new int[iArr.length - 1];
            boolean z = false;
            for (int i2 = 0; i2 < iArr.length; i2++) {
                if (!z) {
                    int i3 = iArr[i2];
                    if (i3 == i) {
                        z = true;
                    } else {
                        if (i2 == iArr.length - 1) {
                            this.mUids = iArr;
                            return;
                        }
                        this.mUids[i2] = i3;
                    }
                } else {
                    this.mUids[i2 - 1] = iArr[i2];
                }
            }
        }

        IBinder getToken() {
            return this.mToken;
        }

        void dump(PrintWriter printWriter, IUidObserver iUidObserver) {
            printWriter.print("    ");
            UserHandle.formatUid(printWriter, this.mUid);
            printWriter.print(" ");
            printWriter.print(this.mPkg);
            printWriter.print(" ");
            printWriter.print(iUidObserver.getClass().getTypeName());
            printWriter.print(":");
            if ((this.mWhich & 4) != 0) {
                printWriter.print(" IDLE");
            }
            if ((this.mWhich & 8) != 0) {
                printWriter.print(" ACT");
            }
            if ((this.mWhich & 2) != 0) {
                printWriter.print(" GONE");
            }
            if ((this.mWhich & 32) != 0) {
                printWriter.print(" CAP");
            }
            if ((this.mWhich & 1) != 0) {
                printWriter.print(" STATE");
                printWriter.print(" (cut=");
                printWriter.print(this.mCutpoint);
                printWriter.print(")");
            }
            printWriter.println();
            SparseIntArray sparseIntArray = this.mLastProcStates;
            if (sparseIntArray != null) {
                int size = sparseIntArray.size();
                for (int i = 0; i < size; i++) {
                    printWriter.print("      Last ");
                    UserHandle.formatUid(printWriter, this.mLastProcStates.keyAt(i));
                    printWriter.print(": ");
                    printWriter.println(this.mLastProcStates.valueAt(i));
                }
            }
        }

        void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
            long start = protoOutputStream.start(j);
            protoOutputStream.write(1120986464257L, this.mUid);
            protoOutputStream.write(1138166333442L, this.mPkg);
            ProtoUtils.writeBitWiseFlagsToProtoEnum(protoOutputStream, 2259152797699L, this.mWhich, ORIG_ENUMS, PROTO_ENUMS);
            protoOutputStream.write(1120986464260L, this.mCutpoint);
            SparseIntArray sparseIntArray = this.mLastProcStates;
            if (sparseIntArray != null) {
                int size = sparseIntArray.size();
                for (int i = 0; i < size; i++) {
                    long start2 = protoOutputStream.start(2246267895813L);
                    protoOutputStream.write(1120986464257L, this.mLastProcStates.keyAt(i));
                    protoOutputStream.write(1120986464258L, this.mLastProcStates.valueAt(i));
                    protoOutputStream.end(start2);
                }
            }
            protoOutputStream.end(start);
        }
    }
}
