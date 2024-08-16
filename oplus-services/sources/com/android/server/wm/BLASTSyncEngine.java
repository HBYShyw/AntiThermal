package com.android.server.wm;

import android.os.Debug;
import android.os.Handler;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.ArraySet;
import android.util.Slog;
import android.view.SurfaceControl;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.server.SystemServerInitThreadPool$;
import com.android.server.wm.BLASTSyncEngine;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class BLASTSyncEngine {
    public static final int METHOD_BLAST = 1;
    public static final int METHOD_NONE = 0;
    public static final int METHOD_UNDEFINED = -1;
    private static final boolean PANIC_DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final String TAG = "BLASTSyncEngine";
    private final ArrayList<SyncGroup> mActiveSyncs;
    private IBLASTSyncEngineExt mBLASTSyncEngineExt;
    private final Handler mHandler;
    private int mNextSyncId;
    private final ArrayList<Runnable> mOnIdleListeners;
    private final ArrayList<PendingSyncSet> mPendingSyncSets;
    private final ArrayList<SyncGroup> mTmpFinishQueue;
    private final ArrayList<SyncGroup> mTmpFringe;
    private final WindowManagerService mWm;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface TransactionReadyListener {
        void onTransactionReady(int i, SurfaceControl.Transaction transaction);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class PendingSyncSet {
        private Runnable mApplySync;
        private Runnable mStartSync;

        private PendingSyncSet() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class SyncGroup {
        private static final ArrayList<SyncGroup> NO_DEPENDENCIES = new ArrayList<>();
        ArrayList<SyncGroup> mDependencies;
        boolean mIgnoreIndirectMembers;
        final TransactionReadyListener mListener;
        final Runnable mOnTimeout;
        private SurfaceControl.Transaction mOrphanTransaction;
        boolean mReady;
        final ArraySet<WindowContainer> mRootMembers;
        final int mSyncId;
        int mSyncMethod;
        private String mTraceName;

        private SyncGroup(TransactionReadyListener transactionReadyListener, int i, String str) {
            this.mSyncMethod = 1;
            this.mReady = false;
            this.mRootMembers = new ArraySet<>();
            this.mOrphanTransaction = null;
            this.mIgnoreIndirectMembers = false;
            this.mDependencies = NO_DEPENDENCIES;
            this.mSyncId = i;
            this.mListener = transactionReadyListener;
            this.mOnTimeout = new Runnable() { // from class: com.android.server.wm.BLASTSyncEngine$SyncGroup$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    BLASTSyncEngine.SyncGroup.this.lambda$new$0();
                }
            };
            if (Trace.isTagEnabled(32L)) {
                String str2 = str + "SyncGroupReady";
                this.mTraceName = str2;
                Trace.asyncTraceBegin(32L, str2, i);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            Slog.w(BLASTSyncEngine.TAG, "Sync group " + this.mSyncId + " timeout");
            WindowManagerGlobalLock windowManagerGlobalLock = BLASTSyncEngine.this.mWm.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    onTimeout();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public SurfaceControl.Transaction getOrphanTransaction() {
            if (this.mOrphanTransaction == null) {
                this.mOrphanTransaction = BLASTSyncEngine.this.mWm.mTransactionFactory.get();
            }
            return this.mOrphanTransaction;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isIgnoring(WindowContainer windowContainer) {
            return this.mIgnoreIndirectMembers && windowContainer.asWindowState() == null && windowContainer.mSyncGroup != this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean tryFinish() {
            if (!this.mReady) {
                return false;
            }
            if (ProtoLogCache.WM_DEBUG_SYNC_ENGINE_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, 966569777, 1, (String) null, new Object[]{Long.valueOf(this.mSyncId), String.valueOf(this.mRootMembers)});
            }
            if (!this.mDependencies.isEmpty()) {
                if (ProtoLogCache.WM_DEBUG_SYNC_ENGINE_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, 1820873642, 1, (String) null, new Object[]{Long.valueOf(this.mSyncId), String.valueOf(this.mDependencies)});
                }
                return false;
            }
            if (BLASTSyncEngine.this.mBLASTSyncEngineExt.tryFinishAheadIfNeed(this.mSyncId, this, this.mRootMembers)) {
                finishNow();
                return true;
            }
            for (int size = this.mRootMembers.size() - 1; size >= 0; size--) {
                WindowContainer valueAt = this.mRootMembers.valueAt(size);
                if (!valueAt.isSyncFinished(this)) {
                    if (ProtoLogCache.WM_DEBUG_SYNC_ENGINE_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, -230587670, 1, (String) null, new Object[]{Long.valueOf(this.mSyncId), String.valueOf(valueAt)});
                    }
                    return false;
                }
                if (BLASTSyncEngine.this.mBLASTSyncEngineExt.onSurfacePlacement(this.mSyncId, valueAt, this.mRootMembers)) {
                    return false;
                }
            }
            finishNow();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void finishNow() {
            String str = this.mTraceName;
            if (str != null) {
                Trace.asyncTraceEnd(32L, str, this.mSyncId);
            }
            if (ProtoLogCache.WM_DEBUG_SYNC_ENGINE_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, -1905191109, 1, (String) null, new Object[]{Long.valueOf(this.mSyncId)});
            }
            SurfaceControl.Transaction transaction = BLASTSyncEngine.this.mWm.mTransactionFactory.get();
            SurfaceControl.Transaction transaction2 = this.mOrphanTransaction;
            if (transaction2 != null) {
                transaction.merge(transaction2);
            }
            Iterator<WindowContainer> it = this.mRootMembers.iterator();
            while (it.hasNext()) {
                it.next().finishSync(transaction, this, false);
            }
            ArraySet<WindowContainer> arraySet = new ArraySet<>();
            Iterator<WindowContainer> it2 = this.mRootMembers.iterator();
            while (it2.hasNext()) {
                it2.next().waitForSyncTransactionCommit(arraySet);
            }
            final C1CommitCallback c1CommitCallback = new C1CommitCallback(arraySet, transaction);
            transaction.addTransactionCommittedListener(new SystemServerInitThreadPool$.ExternalSyntheticLambda1(), new SurfaceControl.TransactionCommittedListener() { // from class: com.android.server.wm.BLASTSyncEngine$SyncGroup$$ExternalSyntheticLambda0
                @Override // android.view.SurfaceControl.TransactionCommittedListener
                public final void onTransactionCommitted() {
                    BLASTSyncEngine.SyncGroup.lambda$finishNow$1(BLASTSyncEngine.SyncGroup.C1CommitCallback.this);
                }
            });
            BLASTSyncEngine.this.mHandler.postDelayed(c1CommitCallback, 5000L);
            Trace.traceBegin(32L, "onTransactionReady");
            this.mListener.onTransactionReady(this.mSyncId, transaction);
            Trace.traceEnd(32L);
            BLASTSyncEngine.this.mActiveSyncs.remove(this);
            BLASTSyncEngine.this.mHandler.removeCallbacks(this.mOnTimeout);
            if (BLASTSyncEngine.this.mActiveSyncs.size() == 0 && !BLASTSyncEngine.this.mPendingSyncSets.isEmpty()) {
                if (ProtoLogCache.WM_DEBUG_SYNC_ENGINE_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, 1730300180, 0, (String) null, (Object[]) null);
                }
                final PendingSyncSet pendingSyncSet = (PendingSyncSet) BLASTSyncEngine.this.mPendingSyncSets.remove(0);
                pendingSyncSet.mStartSync.run();
                if (BLASTSyncEngine.this.mActiveSyncs.size() == 0) {
                    throw new IllegalStateException("Pending Sync Set didn't start a sync.");
                }
                BLASTSyncEngine.this.mHandler.post(new Runnable() { // from class: com.android.server.wm.BLASTSyncEngine$SyncGroup$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        BLASTSyncEngine.SyncGroup.this.lambda$finishNow$2(pendingSyncSet);
                    }
                });
            }
            for (int size = BLASTSyncEngine.this.mOnIdleListeners.size() - 1; size >= 0 && BLASTSyncEngine.this.mActiveSyncs.size() <= 0; size--) {
                ((Runnable) BLASTSyncEngine.this.mOnIdleListeners.get(size)).run();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.android.server.wm.BLASTSyncEngine$SyncGroup$1CommitCallback, reason: invalid class name */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public class C1CommitCallback implements Runnable {
            boolean ran = false;
            final /* synthetic */ SurfaceControl.Transaction val$merged;
            final /* synthetic */ ArraySet val$wcAwaitingCommit;

            C1CommitCallback(ArraySet arraySet, SurfaceControl.Transaction transaction) {
                this.val$wcAwaitingCommit = arraySet;
                this.val$merged = transaction;
            }

            public void onCommitted(SurfaceControl.Transaction transaction) {
                WindowManagerGlobalLock windowManagerGlobalLock = BLASTSyncEngine.this.mWm.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        if (this.ran) {
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                        BLASTSyncEngine.this.mHandler.removeCallbacks(this);
                        this.ran = true;
                        Iterator it = this.val$wcAwaitingCommit.iterator();
                        while (it.hasNext()) {
                            ((WindowContainer) it.next()).onSyncTransactionCommitted(transaction);
                        }
                        transaction.apply();
                        this.val$wcAwaitingCommit.clear();
                        WindowManagerService.resetPriorityAfterLockedSection();
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
            }

            @Override // java.lang.Runnable
            public void run() {
                Trace.traceBegin(32L, "onTransactionCommitTimeout");
                Slog.e(BLASTSyncEngine.TAG, "WM sent Transaction to organized, but never received commit callback. Application ANR likely to follow.");
                Trace.traceEnd(32L);
                WindowManagerGlobalLock windowManagerGlobalLock = BLASTSyncEngine.this.mWm.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        SurfaceControl.Transaction transaction = this.val$merged;
                        if (transaction.mNativeObject == 0) {
                            transaction = BLASTSyncEngine.this.mWm.mTransactionFactory.get();
                        }
                        onCommitted(transaction);
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$finishNow$1(C1CommitCallback c1CommitCallback) {
            c1CommitCallback.onCommitted(new SurfaceControl.Transaction());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$finishNow$2(PendingSyncSet pendingSyncSet) {
            WindowManagerGlobalLock windowManagerGlobalLock = BLASTSyncEngine.this.mWm.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    pendingSyncSet.mApplySync.run();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean setReady(boolean z) {
            if (this.mReady == z) {
                return false;
            }
            if (ProtoLogCache.WM_DEBUG_SYNC_ENGINE_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, -874087484, 13, (String) null, new Object[]{Long.valueOf(this.mSyncId), Boolean.valueOf(z)});
            }
            this.mReady = z;
            if (!z && BLASTSyncEngine.PANIC_DEBUG) {
                Slog.e(BLASTSyncEngine.TAG, "setReady " + z + " debug " + Debug.getCallers(10));
            }
            if (!z) {
                return true;
            }
            BLASTSyncEngine.this.mWm.mWindowPlacerLocked.requestTraversal();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addToSync(WindowContainer windowContainer) {
            if (this.mRootMembers.contains(windowContainer)) {
                return;
            }
            if (ProtoLogCache.WM_DEBUG_SYNC_ENGINE_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, -1973119651, 1, (String) null, new Object[]{Long.valueOf(this.mSyncId), String.valueOf(windowContainer)});
            }
            SyncGroup syncGroup = windowContainer.getSyncGroup();
            if (syncGroup != null && syncGroup != this && !syncGroup.isIgnoring(windowContainer)) {
                Slog.w(BLASTSyncEngine.TAG, "SyncGroup " + this.mSyncId + " conflicts with " + syncGroup.mSyncId + ": Making " + this.mSyncId + " depend on " + syncGroup.mSyncId + ", wc=" + windowContainer);
                if (BLASTSyncEngine.this.mBLASTSyncEngineExt.skipAddToSync(windowContainer, this, syncGroup)) {
                    return;
                }
                if (!this.mDependencies.contains(syncGroup)) {
                    if (syncGroup.dependsOn(this)) {
                        Slog.w(BLASTSyncEngine.TAG, " Detected dependency cycle between " + this.mSyncId + " and " + syncGroup.mSyncId + ": Moving " + windowContainer + " to " + this.mSyncId);
                        SyncGroup syncGroup2 = windowContainer.mSyncGroup;
                        if (syncGroup2 == null) {
                            windowContainer.setSyncGroup(this);
                        } else {
                            syncGroup2.mRootMembers.remove(windowContainer);
                            this.mRootMembers.add(windowContainer);
                            windowContainer.mSyncGroup = this;
                        }
                    } else {
                        if (this.mDependencies == NO_DEPENDENCIES) {
                            this.mDependencies = new ArrayList<>();
                        }
                        this.mDependencies.add(syncGroup);
                    }
                }
            } else {
                this.mRootMembers.add(windowContainer);
                windowContainer.setSyncGroup(this);
            }
            windowContainer.prepareSync();
            if (this.mReady) {
                BLASTSyncEngine.this.mWm.mWindowPlacerLocked.requestTraversal();
            }
        }

        private boolean dependsOn(SyncGroup syncGroup) {
            if (this.mDependencies.isEmpty()) {
                return false;
            }
            ArrayList arrayList = BLASTSyncEngine.this.mTmpFringe;
            arrayList.clear();
            arrayList.add(this);
            for (int i = 0; i < arrayList.size(); i++) {
                SyncGroup syncGroup2 = (SyncGroup) arrayList.get(i);
                if (syncGroup2 == syncGroup) {
                    arrayList.clear();
                    return true;
                }
                for (int i2 = 0; i2 < syncGroup2.mDependencies.size(); i2++) {
                    if (!arrayList.contains(syncGroup2.mDependencies.get(i2))) {
                        arrayList.add(syncGroup2.mDependencies.get(i2));
                    }
                }
            }
            arrayList.clear();
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void onCancelSync(WindowContainer windowContainer) {
            this.mRootMembers.remove(windowContainer);
        }

        private void onTimeout() {
            if (BLASTSyncEngine.this.mActiveSyncs.contains(this)) {
                boolean z = true;
                for (int size = this.mRootMembers.size() - 1; size >= 0; size--) {
                    WindowContainer valueAt = this.mRootMembers.valueAt(size);
                    if (valueAt != null && !valueAt.isSyncFinished(this)) {
                        Slog.i(BLASTSyncEngine.TAG, "Unfinished container: " + valueAt);
                        BLASTSyncEngine.this.mBLASTSyncEngineExt.logOutUnfinishedcontainerInfo(this, valueAt);
                        z = false;
                    }
                }
                int size2 = this.mDependencies.size() - 1;
                while (size2 >= 0) {
                    Slog.i(BLASTSyncEngine.TAG, "Unfinished dependency: " + this.mDependencies.get(size2).mSyncId);
                    size2 += -1;
                    z = false;
                }
                if (z && !this.mReady) {
                    Slog.w(BLASTSyncEngine.TAG, "Sync group " + this.mSyncId + " timed-out because not ready. If you see this, please file a bug.");
                }
                BLASTSyncEngine.this.mBLASTSyncEngineExt.onTimeout(BLASTSyncEngine.this.mWm, this);
                finishNow();
                BLASTSyncEngine.this.removeFromDependencies(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BLASTSyncEngine(WindowManagerService windowManagerService) {
        this(windowManagerService, windowManagerService.mH);
    }

    @VisibleForTesting
    BLASTSyncEngine(WindowManagerService windowManagerService, Handler handler) {
        this.mBLASTSyncEngineExt = (IBLASTSyncEngineExt) ExtLoader.type(IBLASTSyncEngineExt.class).base(this).create();
        this.mNextSyncId = 0;
        this.mActiveSyncs = new ArrayList<>();
        this.mPendingSyncSets = new ArrayList<>();
        this.mOnIdleListeners = new ArrayList<>();
        this.mTmpFinishQueue = new ArrayList<>();
        this.mTmpFringe = new ArrayList<>();
        this.mWm = windowManagerService;
        this.mHandler = handler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SyncGroup prepareSyncSet(TransactionReadyListener transactionReadyListener, String str) {
        int i = this.mNextSyncId;
        this.mNextSyncId = i + 1;
        return new SyncGroup(transactionReadyListener, i, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int startSyncSet(TransactionReadyListener transactionReadyListener, long j, String str, boolean z) {
        SyncGroup prepareSyncSet = prepareSyncSet(transactionReadyListener, str);
        startSyncSet(prepareSyncSet, j, z);
        return prepareSyncSet.mSyncId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startSyncSet(SyncGroup syncGroup) {
        startSyncSet(syncGroup, 5000L, false);
    }

    void startSyncSet(SyncGroup syncGroup, long j, boolean z) {
        boolean z2 = this.mActiveSyncs.size() > 0;
        if (!z && z2) {
            Slog.e(TAG, "SyncGroup " + syncGroup.mSyncId + ": Started when there is other active SyncGroup");
        }
        this.mActiveSyncs.add(syncGroup);
        syncGroup.mIgnoreIndirectMembers = z;
        if (ProtoLogCache.WM_DEBUG_SYNC_ENGINE_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, -1828118576, 1, (String) null, new Object[]{Long.valueOf(syncGroup.mSyncId), (z && z2) ? "(in parallel) " : "", String.valueOf(syncGroup.mListener)});
        }
        scheduleTimeout(syncGroup, j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SyncGroup getSyncSet(int i) {
        for (int i2 = 0; i2 < this.mActiveSyncs.size(); i2++) {
            if (this.mActiveSyncs.get(i2).mSyncId == i) {
                return this.mActiveSyncs.get(i2);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasActiveSync() {
        return this.mActiveSyncs.size() != 0;
    }

    @VisibleForTesting
    void scheduleTimeout(SyncGroup syncGroup, long j) {
        this.mHandler.postDelayed(syncGroup.mOnTimeout, j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addToSyncSet(int i, WindowContainer windowContainer) {
        getSyncGroup(i).addToSync(windowContainer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSyncMethod(int i, int i2) {
        SyncGroup syncGroup = getSyncGroup(i);
        if (!syncGroup.mRootMembers.isEmpty()) {
            throw new IllegalStateException("Not allow to change sync method after adding group member, id=" + i);
        }
        syncGroup.mSyncMethod = i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setReady(int i, boolean z) {
        return getSyncGroup(i).setReady(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setReady(int i) {
        setReady(i, true);
    }

    boolean isReady(int i) {
        return getSyncGroup(i).mReady;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void abort(int i) {
        SyncGroup syncGroup = getSyncGroup(i);
        syncGroup.finishNow();
        removeFromDependencies(syncGroup);
    }

    private SyncGroup getSyncGroup(int i) {
        SyncGroup syncSet = getSyncSet(i);
        if (syncSet != null) {
            return syncSet;
        }
        throw new IllegalStateException("SyncGroup is not started yet id=" + i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFromDependencies(SyncGroup syncGroup) {
        boolean z = false;
        for (int i = 0; i < this.mActiveSyncs.size(); i++) {
            SyncGroup syncGroup2 = this.mActiveSyncs.get(i);
            if (syncGroup2.mDependencies.remove(syncGroup) && syncGroup2.mDependencies.isEmpty()) {
                z = true;
            }
        }
        if (z) {
            this.mWm.mWindowPlacerLocked.requestTraversal();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSurfacePlacement() {
        if (this.mActiveSyncs.isEmpty()) {
            return;
        }
        this.mTmpFinishQueue.addAll(this.mActiveSyncs);
        int size = ((this.mActiveSyncs.size() + 1) * this.mActiveSyncs.size()) / 2;
        while (!this.mTmpFinishQueue.isEmpty()) {
            if (size <= 0) {
                Slog.e(TAG, "Trying to finish more syncs than theoretically possible. This should never happen. Most likely a dependency cycle wasn't detected.");
            }
            size--;
            SyncGroup remove = this.mTmpFinishQueue.remove(0);
            int indexOf = this.mActiveSyncs.indexOf(remove);
            if (indexOf >= 0 && remove.tryFinish()) {
                int i = 0;
                for (int i2 = 0; i2 < this.mActiveSyncs.size(); i2++) {
                    SyncGroup syncGroup = this.mActiveSyncs.get(i2);
                    if (syncGroup.mDependencies.remove(remove) && i2 < indexOf && syncGroup.mDependencies.isEmpty()) {
                        this.mTmpFinishQueue.add(i, this.mActiveSyncs.get(i2));
                        i++;
                    }
                }
            }
        }
    }

    void tryFinishForTest(int i) {
        getSyncSet(i).tryFinish();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void queueSyncSet(Runnable runnable, Runnable runnable2) {
        PendingSyncSet pendingSyncSet = new PendingSyncSet();
        pendingSyncSet.mStartSync = runnable;
        pendingSyncSet.mApplySync = runnable2;
        this.mPendingSyncSets.add(pendingSyncSet);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasPendingSyncSets() {
        return !this.mPendingSyncSets.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addOnIdleListener(Runnable runnable) {
        this.mOnIdleListeners.add(runnable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str, boolean z) {
        this.mBLASTSyncEngineExt.dump(printWriter, str, this, this.mActiveSyncs);
    }
}
