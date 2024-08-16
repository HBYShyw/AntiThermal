package com.android.server.wm;

import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.window.AddToSurfaceSyncGroupResult;
import android.window.ISurfaceSyncGroupCompletedListener;
import android.window.ITransactionReadyCallback;
import android.window.SurfaceSyncGroup;
import com.android.internal.annotations.GuardedBy;
import com.android.server.SystemServerInitThreadPool$;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
class SurfaceSyncGroupController {
    private static final String TAG = "SurfaceSyncGroupController";
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final ArrayMap<IBinder, SurfaceSyncGroupData> mSurfaceSyncGroups = new ArrayMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean addToSyncGroup(IBinder iBinder, boolean z, final ISurfaceSyncGroupCompletedListener iSurfaceSyncGroupCompletedListener, AddToSurfaceSyncGroupResult addToSurfaceSyncGroupResult) {
        SurfaceSyncGroup surfaceSyncGroup;
        synchronized (this.mLock) {
            SurfaceSyncGroupData surfaceSyncGroupData = this.mSurfaceSyncGroups.get(iBinder);
            if (surfaceSyncGroupData == null) {
                surfaceSyncGroup = new SurfaceSyncGroup("SurfaceSyncGroupController-" + iBinder.hashCode());
                if (iSurfaceSyncGroupCompletedListener != null) {
                    surfaceSyncGroup.addSyncCompleteCallback(new SystemServerInitThreadPool$.ExternalSyntheticLambda1(), new Runnable() { // from class: com.android.server.wm.SurfaceSyncGroupController$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            SurfaceSyncGroupController.lambda$addToSyncGroup$0(iSurfaceSyncGroupCompletedListener);
                        }
                    });
                }
                this.mSurfaceSyncGroups.put(iBinder, new SurfaceSyncGroupData(Binder.getCallingUid(), surfaceSyncGroup));
            } else {
                surfaceSyncGroup = surfaceSyncGroupData.mSurfaceSyncGroup;
            }
        }
        ITransactionReadyCallback createTransactionReadyCallback = surfaceSyncGroup.createTransactionReadyCallback(z);
        if (createTransactionReadyCallback == null) {
            return false;
        }
        addToSurfaceSyncGroupResult.mParentSyncGroup = surfaceSyncGroup.mISurfaceSyncGroup;
        addToSurfaceSyncGroupResult.mTransactionReadyCallback = createTransactionReadyCallback;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$addToSyncGroup$0(ISurfaceSyncGroupCompletedListener iSurfaceSyncGroupCompletedListener) {
        try {
            iSurfaceSyncGroupCompletedListener.onSurfaceSyncGroupComplete();
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void markSyncGroupReady(IBinder iBinder) {
        SurfaceSyncGroup surfaceSyncGroup;
        synchronized (this.mLock) {
            SurfaceSyncGroupData surfaceSyncGroupData = this.mSurfaceSyncGroups.get(iBinder);
            if (surfaceSyncGroupData == null) {
                throw new IllegalArgumentException("SurfaceSyncGroup Token has not been set up or has already been marked as ready");
            }
            if (surfaceSyncGroupData.mOwningUid != Binder.getCallingUid()) {
                throw new IllegalArgumentException("Only process that created the SurfaceSyncGroup can call markSyncGroupReady");
            }
            surfaceSyncGroup = surfaceSyncGroupData.mSurfaceSyncGroup;
            this.mSurfaceSyncGroups.remove(iBinder);
        }
        surfaceSyncGroup.markSyncReady();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private static class SurfaceSyncGroupData {
        final int mOwningUid;
        final SurfaceSyncGroup mSurfaceSyncGroup;

        private SurfaceSyncGroupData(int i, SurfaceSyncGroup surfaceSyncGroup) {
            this.mOwningUid = i;
            this.mSurfaceSyncGroup = surfaceSyncGroup;
        }
    }
}
