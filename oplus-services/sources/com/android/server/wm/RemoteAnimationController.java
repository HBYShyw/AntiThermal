package com.android.server.wm;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.FastPrintWriter;
import com.android.server.wm.SurfaceAnimator;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class RemoteAnimationController implements IBinder.DeathRecipient {
    private static final String TAG = "RemoteAnimationController";
    private static final long TIMEOUT_MS = 10000;
    private boolean mCanceled;
    private final DisplayContent mDisplayContent;
    private FinishedCallback mFinishedCallback;
    private final Handler mHandler;
    private final boolean mIsActivityEmbedding;
    private boolean mIsFinishing;
    private boolean mLinkedToDeathOfRunner;
    private Runnable mOnRemoteAnimationReady;
    private final RemoteAnimationAdapter mRemoteAnimationAdapter;
    private final WindowManagerService mService;
    private final ArrayList<RemoteAnimationRecord> mPendingAnimations = new ArrayList<>();
    private final ArrayList<WallpaperAnimationAdapter> mPendingWallpaperAnimations = new ArrayList<>();

    @VisibleForTesting
    final ArrayList<NonAppWindowAnimationAdapter> mPendingNonAppAnimations = new ArrayList<>();
    private final Runnable mTimeoutRunnable = new Runnable() { // from class: com.android.server.wm.RemoteAnimationController$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            RemoteAnimationController.this.lambda$new$0();
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        cancelAnimation("timeoutRunnable");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteAnimationController(WindowManagerService windowManagerService, DisplayContent displayContent, RemoteAnimationAdapter remoteAnimationAdapter, Handler handler, boolean z) {
        this.mService = windowManagerService;
        this.mDisplayContent = displayContent;
        this.mRemoteAnimationAdapter = remoteAnimationAdapter;
        this.mHandler = handler;
        this.mIsActivityEmbedding = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteAnimationRecord createRemoteAnimationRecord(WindowContainer windowContainer, Point point, Rect rect, Rect rect2, Rect rect3, boolean z) {
        return createRemoteAnimationRecord(windowContainer, point, rect, rect2, rect3, z, rect3 != null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteAnimationRecord createRemoteAnimationRecord(WindowContainer windowContainer, Point point, Rect rect, Rect rect2, Rect rect3, boolean z, boolean z2) {
        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 2022422429, 0, (String) null, new Object[]{String.valueOf(windowContainer)});
        }
        RemoteAnimationRecord remoteAnimationRecord = new RemoteAnimationRecord(windowContainer, point, rect, rect2, rect3, z, z2);
        this.mPendingAnimations.add(remoteAnimationRecord);
        return remoteAnimationRecord;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOnRemoteAnimationReady(Runnable runnable) {
        this.mOnRemoteAnimationReady = runnable;
    }

    public boolean isFromActivityEmbedding() {
        return this.mIsActivityEmbedding;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void goodToGo(final int i) {
        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 873914452, 0, (String) null, (Object[]) null);
        }
        if (this.mCanceled) {
            if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 269976641, 0, (String) null, (Object[]) null);
            }
            onAnimationFinished();
            invokeAnimationCancelled("already_cancelled");
            return;
        }
        this.mHandler.postDelayed(this.mTimeoutRunnable, this.mService.getCurrentAnimatorScale() * 10000.0f);
        this.mFinishedCallback = new FinishedCallback(this);
        final RemoteAnimationTarget[] createAppAnimations = createAppAnimations();
        if (createAppAnimations.length == 0 && !AppTransition.isKeyguardOccludeTransitOld(i)) {
            if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -1777196134, 1, (String) null, new Object[]{Long.valueOf(this.mPendingAnimations.size())});
            }
            onAnimationFinished();
            invokeAnimationCancelled("no_app_targets");
            return;
        }
        Runnable runnable = this.mOnRemoteAnimationReady;
        if (runnable != null) {
            runnable.run();
            this.mOnRemoteAnimationReady = null;
        }
        final RemoteAnimationTarget[] createWallpaperAnimations = createWallpaperAnimations();
        final RemoteAnimationTarget[] createNonAppWindowAnimations = createNonAppWindowAnimations(i);
        this.mService.mAnimator.addAfterPrepareSurfacesRunnable(new Runnable() { // from class: com.android.server.wm.RemoteAnimationController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                RemoteAnimationController.this.lambda$goodToGo$1(i, createAppAnimations, createWallpaperAnimations, createNonAppWindowAnimations);
            }
        });
        setRunningRemoteAnimation(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$goodToGo$1(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3) {
        try {
            linkToDeathOfRunner();
            Slog.d(TAG, "goodToGo onAnimationStart transit=" + AppTransition.appTransitionOldToString(i) + ", apps=:" + remoteAnimationTargetArr.length + ", wallpapers=" + remoteAnimationTargetArr2.length + ", nonApps=" + remoteAnimationTargetArr3.length + ", animation:" + this);
            if (AppTransition.isKeyguardOccludeTransitOld(i)) {
                EventLogTags.writeWmSetKeyguardOccluded(i == 23 ? 0 : 1, 1, i, "onAnimationStart");
            }
            this.mRemoteAnimationAdapter.getRunner().onAnimationStart(i, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, this.mFinishedCallback);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to start remote animation", e);
            onAnimationFinished();
        }
        if (ProtoLogImpl.isEnabled(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS)) {
            if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -2012562539, 0, (String) null, (Object[]) null);
            }
            writeStartDebugStatement();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelAnimation(String str) {
        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 1891501279, 0, (String) null, new Object[]{String.valueOf(str)});
        }
        synchronized (this.mService.getWindowManagerLock()) {
            if (this.mCanceled) {
                return;
            }
            this.mCanceled = true;
            onAnimationFinished();
            invokeAnimationCancelled(str);
        }
    }

    private void writeStartDebugStatement() {
        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 1964565370, 0, (String) null, (Object[]) null);
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter fastPrintWriter = new FastPrintWriter(stringWriter);
        for (int size = this.mPendingAnimations.size() - 1; size >= 0; size--) {
            this.mPendingAnimations.get(size).mAdapter.dump(fastPrintWriter, "");
        }
        fastPrintWriter.close();
        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 835814848, 0, (String) null, new Object[]{String.valueOf(stringWriter.toString())});
        }
    }

    private RemoteAnimationTarget[] createAppAnimations() {
        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -653156702, 0, (String) null, (Object[]) null);
        }
        ArrayList arrayList = new ArrayList();
        for (int size = this.mPendingAnimations.size() - 1; size >= 0; size--) {
            if (size < this.mPendingAnimations.size()) {
                RemoteAnimationRecord remoteAnimationRecord = this.mPendingAnimations.get(size);
                WindowContainer windowContainer = remoteAnimationRecord.mWindowContainer;
                ActivityRecord asActivityRecord = windowContainer != null ? windowContainer.asActivityRecord() : null;
                if (asActivityRecord == null || asActivityRecord.getTask() == null || !asActivityRecord.getTask().getWrapper().getExtImpl().getLaunchedFromMultiSearch()) {
                    RemoteAnimationTarget createRemoteAnimationTarget = remoteAnimationRecord.createRemoteAnimationTarget();
                    if (createRemoteAnimationTarget != null) {
                        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
                            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -1248645819, 0, (String) null, new Object[]{String.valueOf(remoteAnimationRecord.mWindowContainer)});
                        }
                        arrayList.add(createRemoteAnimationTarget);
                    } else {
                        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
                            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 638429464, 0, (String) null, new Object[]{String.valueOf(remoteAnimationRecord.mWindowContainer)});
                        }
                        RemoteAnimationAdapterWrapper remoteAnimationAdapterWrapper = remoteAnimationRecord.mAdapter;
                        if (remoteAnimationAdapterWrapper != null && remoteAnimationAdapterWrapper.mCapturedFinishCallback != null) {
                            remoteAnimationRecord.mAdapter.mCapturedFinishCallback.onAnimationFinished(remoteAnimationRecord.mAdapter.mAnimationType, remoteAnimationRecord.mAdapter);
                        }
                        RemoteAnimationAdapterWrapper remoteAnimationAdapterWrapper2 = remoteAnimationRecord.mThumbnailAdapter;
                        if (remoteAnimationAdapterWrapper2 != null && remoteAnimationAdapterWrapper2.mCapturedFinishCallback != null) {
                            remoteAnimationRecord.mThumbnailAdapter.mCapturedFinishCallback.onAnimationFinished(remoteAnimationRecord.mThumbnailAdapter.mAnimationType, remoteAnimationRecord.mThumbnailAdapter);
                        }
                        this.mPendingAnimations.remove(remoteAnimationRecord);
                    }
                }
            }
        }
        return (RemoteAnimationTarget[]) arrayList.toArray(new RemoteAnimationTarget[arrayList.size()]);
    }

    private RemoteAnimationTarget[] createWallpaperAnimations() {
        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 594260577, 0, (String) null, (Object[]) null);
        }
        return WallpaperAnimationAdapter.startWallpaperAnimations(this.mDisplayContent, this.mRemoteAnimationAdapter.getDuration(), this.mRemoteAnimationAdapter.getStatusBarTransitionDelay(), new Consumer() { // from class: com.android.server.wm.RemoteAnimationController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                RemoteAnimationController.this.lambda$createWallpaperAnimations$2((WallpaperAnimationAdapter) obj);
            }
        }, this.mPendingWallpaperAnimations);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createWallpaperAnimations$2(WallpaperAnimationAdapter wallpaperAnimationAdapter) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mPendingWallpaperAnimations.remove(wallpaperAnimationAdapter);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    private RemoteAnimationTarget[] createNonAppWindowAnimations(int i) {
        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -1834214907, 0, (String) null, (Object[]) null);
        }
        return NonAppWindowAnimationAdapter.startNonAppWindowAnimations(this.mService, this.mDisplayContent, i, this.mRemoteAnimationAdapter.getDuration(), this.mRemoteAnimationAdapter.getStatusBarTransitionDelay(), this.mPendingNonAppAnimations);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAnimationFinished() {
        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -1497837552, 1, (String) null, new Object[]{Long.valueOf(this.mPendingAnimations.size())});
        }
        this.mHandler.removeCallbacks(this.mTimeoutRunnable);
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mIsFinishing = true;
                unlinkToDeathOfRunner();
                releaseFinishedCallback();
                this.mService.openSurfaceTransaction();
                try {
                    try {
                        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
                            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 557227556, 0, (String) null, (Object[]) null);
                        }
                        for (int size = this.mPendingAnimations.size() - 1; size >= 0; size--) {
                            if (size < this.mPendingAnimations.size()) {
                                RemoteAnimationRecord remoteAnimationRecord = this.mPendingAnimations.get(size);
                                RemoteAnimationAdapterWrapper remoteAnimationAdapterWrapper = remoteAnimationRecord.mAdapter;
                                if (remoteAnimationAdapterWrapper != null && remoteAnimationAdapterWrapper.mCapturedFinishCallback != null) {
                                    remoteAnimationRecord.mAdapter.mCapturedFinishCallback.onAnimationFinished(remoteAnimationRecord.mAdapter.mAnimationType, remoteAnimationRecord.mAdapter);
                                }
                                RemoteAnimationAdapterWrapper remoteAnimationAdapterWrapper2 = remoteAnimationRecord.mThumbnailAdapter;
                                if (remoteAnimationAdapterWrapper2 != null && remoteAnimationAdapterWrapper2.mCapturedFinishCallback != null) {
                                    remoteAnimationRecord.mThumbnailAdapter.mCapturedFinishCallback.onAnimationFinished(remoteAnimationRecord.mThumbnailAdapter.mAnimationType, remoteAnimationRecord.mThumbnailAdapter);
                                }
                                this.mPendingAnimations.remove(remoteAnimationRecord);
                                if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
                                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 972354148, 0, (String) null, new Object[]{String.valueOf(remoteAnimationRecord.mWindowContainer)});
                                }
                            }
                        }
                        for (int size2 = this.mPendingWallpaperAnimations.size() - 1; size2 >= 0; size2--) {
                            WallpaperAnimationAdapter wallpaperAnimationAdapter = this.mPendingWallpaperAnimations.get(size2);
                            wallpaperAnimationAdapter.getLeashFinishedCallback().onAnimationFinished(wallpaperAnimationAdapter.getLastAnimationType(), wallpaperAnimationAdapter);
                            this.mPendingWallpaperAnimations.remove(size2);
                            if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
                                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -853404763, 0, (String) null, new Object[]{String.valueOf(wallpaperAnimationAdapter.getToken())});
                            }
                        }
                        for (int size3 = this.mPendingNonAppAnimations.size() - 1; size3 >= 0; size3--) {
                            NonAppWindowAnimationAdapter nonAppWindowAnimationAdapter = this.mPendingNonAppAnimations.get(size3);
                            nonAppWindowAnimationAdapter.getLeashFinishedCallback().onAnimationFinished(nonAppWindowAnimationAdapter.getLastAnimationType(), nonAppWindowAnimationAdapter);
                            this.mPendingNonAppAnimations.remove(size3);
                            if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
                                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 1931178855, 0, (String) null, new Object[]{String.valueOf(nonAppWindowAnimationAdapter.getWindowContainer())});
                            }
                        }
                        this.mDisplayContent.mAppTransition.getWrapper().getExtImpl().validateKeyguardOcclusion(this.mDisplayContent);
                        this.mService.closeSurfaceTransaction("RemoteAnimationController#finished");
                        this.mIsFinishing = false;
                        this.mDisplayContent.forAllActivities(new Consumer() { // from class: com.android.server.wm.RemoteAnimationController$$ExternalSyntheticLambda2
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                ((ActivityRecord) obj).setDropInputForAnimation(false);
                            }
                        });
                    } catch (Exception e) {
                        Slog.e(TAG, "Failed to finish remote animation", e);
                        throw e;
                    }
                } catch (Throwable th) {
                    this.mService.closeSurfaceTransaction("RemoteAnimationController#finished");
                    this.mIsFinishing = false;
                    throw th;
                }
            } catch (Throwable th2) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        setRunningRemoteAnimation(false);
        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 248210157, 0, (String) null, (Object[]) null);
        }
    }

    private void invokeAnimationCancelled(String str) {
        boolean isKeyguardOccluded = this.mDisplayContent.isKeyguardOccluded();
        try {
            EventLogTags.writeWmSetKeyguardOccluded(isKeyguardOccluded ? 1 : 0, 0, 0, "onAnimationCancelled");
            Slog.d(TAG, "cancelAnimation: reason=" + str + ", isKeyguardOccluded=" + isKeyguardOccluded + ", animation:" + this + ", Callers=" + Debug.getCallers(5));
            this.mRemoteAnimationAdapter.getRunner().onAnimationCancelled();
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to notify cancel", e);
        }
        this.mOnRemoteAnimationReady = null;
    }

    private void releaseFinishedCallback() {
        FinishedCallback finishedCallback = this.mFinishedCallback;
        if (finishedCallback != null) {
            finishedCallback.release();
            this.mFinishedCallback = null;
        }
    }

    private void setRunningRemoteAnimation(boolean z) {
        int callingPid = this.mRemoteAnimationAdapter.getCallingPid();
        int callingUid = this.mRemoteAnimationAdapter.getCallingUid();
        if (callingPid == 0) {
            throw new RuntimeException("Calling pid of remote animation was null");
        }
        WindowProcessController processController = this.mService.mAtmService.getProcessController(callingPid, callingUid);
        if (processController == null) {
            Slog.w(TAG, "Unable to find process with pid=" + callingPid + " uid=" + callingUid);
            return;
        }
        processController.setRunningRemoteAnimation(z);
    }

    private void linkToDeathOfRunner() throws RemoteException {
        if (this.mLinkedToDeathOfRunner) {
            return;
        }
        this.mRemoteAnimationAdapter.getRunner().asBinder().linkToDeath(this, 0);
        this.mLinkedToDeathOfRunner = true;
    }

    private void unlinkToDeathOfRunner() {
        if (this.mLinkedToDeathOfRunner) {
            this.mRemoteAnimationAdapter.getRunner().asBinder().unlinkToDeath(this, 0);
            this.mLinkedToDeathOfRunner = false;
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        cancelAnimation("binderDied");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class FinishedCallback extends IRemoteAnimationFinishedCallback.Stub {
        RemoteAnimationController mOuter;

        FinishedCallback(RemoteAnimationController remoteAnimationController) {
            this.mOuter = remoteAnimationController;
        }

        public void onAnimationFinished() throws RemoteException {
            Slog.d(RemoteAnimationController.TAG, "app-onAnimationFinished(): mOuter=" + this.mOuter);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                RemoteAnimationController remoteAnimationController = this.mOuter;
                if (remoteAnimationController != null) {
                    remoteAnimationController.onAnimationFinished();
                    this.mOuter = null;
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        void release() {
            if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -2109864870, 0, (String) null, new Object[]{String.valueOf(this.mOuter)});
            }
            this.mOuter = null;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class RemoteAnimationRecord {
        RemoteAnimationAdapterWrapper mAdapter;
        int mBackdropColor = 0;
        private int mMode = 2;
        final boolean mShowBackdrop;
        final Rect mStartBounds;
        RemoteAnimationTarget mTarget;
        RemoteAnimationAdapterWrapper mThumbnailAdapter;
        final WindowContainer mWindowContainer;

        RemoteAnimationRecord(WindowContainer windowContainer, Point point, Rect rect, Rect rect2, Rect rect3, boolean z, boolean z2) {
            this.mThumbnailAdapter = null;
            this.mWindowContainer = windowContainer;
            this.mShowBackdrop = z;
            if (rect3 != null) {
                Rect rect4 = new Rect(rect3);
                this.mStartBounds = rect4;
                this.mAdapter = new RemoteAnimationAdapterWrapper(this, point, rect, rect2, rect4, z);
                if (z2 && RemoteAnimationController.this.mRemoteAnimationAdapter.getChangeNeedsSnapshot()) {
                    Rect rect5 = new Rect(rect3);
                    rect5.offsetTo(0, 0);
                    this.mThumbnailAdapter = new RemoteAnimationAdapterWrapper(this, new Point(0, 0), rect5, rect3, new Rect(), z);
                    return;
                }
                return;
            }
            this.mAdapter = new RemoteAnimationAdapterWrapper(this, point, rect, rect2, new Rect(), z);
            this.mStartBounds = null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setBackDropColor(int i) {
            this.mBackdropColor = i;
        }

        RemoteAnimationTarget createRemoteAnimationTarget() {
            RemoteAnimationAdapterWrapper remoteAnimationAdapterWrapper = this.mAdapter;
            if (remoteAnimationAdapterWrapper == null || remoteAnimationAdapterWrapper.mCapturedFinishCallback == null || this.mAdapter.mCapturedLeash == null) {
                return null;
            }
            RemoteAnimationTarget createRemoteAnimationTarget = this.mWindowContainer.createRemoteAnimationTarget(this);
            this.mTarget = createRemoteAnimationTarget;
            return createRemoteAnimationTarget;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setMode(int i) {
            this.mMode = i;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getMode() {
            return this.mMode;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean hasAnimatingParent() {
            for (int size = RemoteAnimationController.this.mDisplayContent.mChangingContainers.size() - 1; size >= 0; size--) {
                if (this.mWindowContainer.isDescendantOf(RemoteAnimationController.this.mDisplayContent.mChangingContainers.valueAt(size))) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class RemoteAnimationAdapterWrapper implements AnimationAdapter {
        private int mAnimationType;
        private SurfaceAnimator.OnAnimationFinishedCallback mCapturedFinishCallback;
        SurfaceControl mCapturedLeash;
        final Rect mEndBounds;
        final Rect mLocalBounds;
        final Point mPosition;
        private final RemoteAnimationRecord mRecord;
        final boolean mShowBackdrop;
        final Rect mStartBounds;

        @Override // com.android.server.wm.AnimationAdapter
        public boolean getShowWallpaper() {
            return false;
        }

        RemoteAnimationAdapterWrapper(RemoteAnimationRecord remoteAnimationRecord, Point point, Rect rect, Rect rect2, Rect rect3, boolean z) {
            Point point2 = new Point();
            this.mPosition = point2;
            Rect rect4 = new Rect();
            this.mEndBounds = rect4;
            Rect rect5 = new Rect();
            this.mStartBounds = rect5;
            this.mRecord = remoteAnimationRecord;
            point2.set(point.x, point.y);
            this.mLocalBounds = rect;
            rect4.set(rect2);
            rect5.set(rect3);
            this.mShowBackdrop = z;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public int getBackgroundColor() {
            return this.mRecord.mBackdropColor;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public boolean getShowBackground() {
            return this.mShowBackdrop;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void startAnimation(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, int i, SurfaceAnimator.OnAnimationFinishedCallback onAnimationFinishedCallback) {
            if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -1596995693, 0, (String) null, (Object[]) null);
            }
            if (this.mRecord.mWindowContainer.mSurfaceAnimator.getWrapper().getExtImpl().isReuseLeash()) {
                this.mCapturedLeash = surfaceControl;
                this.mCapturedFinishCallback = onAnimationFinishedCallback;
                this.mAnimationType = i;
                return;
            }
            if (this.mStartBounds.isEmpty()) {
                Point point = this.mPosition;
                transaction.setPosition(surfaceControl, point.x, point.y);
                transaction.setWindowCrop(surfaceControl, this.mEndBounds.width(), this.mEndBounds.height());
                WindowContainer windowContainer = this.mRecord.mWindowContainer;
                if (!windowContainer.mWindowContainerExt.shouldCropAnimationLeashInEmbedding(windowContainer)) {
                    transaction.setWindowCrop(surfaceControl, 0, 0);
                }
            } else {
                int i2 = this.mPosition.x + this.mStartBounds.left;
                Rect rect = this.mEndBounds;
                transaction.setPosition(surfaceControl, i2 - rect.left, (r0.y + r2.top) - rect.top);
                transaction.setWindowCrop(surfaceControl, this.mStartBounds.width(), this.mStartBounds.height());
            }
            this.mCapturedLeash = surfaceControl;
            this.mCapturedFinishCallback = onAnimationFinishedCallback;
            this.mAnimationType = i;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void onAnimationCancelled(SurfaceControl surfaceControl) {
            if (RemoteAnimationController.this.mIsFinishing) {
                return;
            }
            RemoteAnimationRecord remoteAnimationRecord = this.mRecord;
            if (remoteAnimationRecord.mAdapter == this) {
                remoteAnimationRecord.mAdapter = null;
            } else {
                remoteAnimationRecord.mThumbnailAdapter = null;
            }
            if (remoteAnimationRecord.mAdapter == null && remoteAnimationRecord.mThumbnailAdapter == null) {
                WindowManagerGlobalLock windowManagerGlobalLock = RemoteAnimationController.this.mService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        RemoteAnimationController.this.mPendingAnimations.remove(this.mRecord);
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            }
            boolean z = true;
            int size = RemoteAnimationController.this.mPendingAnimations.size() - 1;
            while (true) {
                if (size >= 0) {
                    RemoteAnimationRecord remoteAnimationRecord2 = (RemoteAnimationRecord) RemoteAnimationController.this.mPendingAnimations.get(size);
                    RemoteAnimationRecord remoteAnimationRecord3 = this.mRecord;
                    if (remoteAnimationRecord2 != remoteAnimationRecord3 && remoteAnimationRecord2.mWindowContainer == remoteAnimationRecord3.mWindowContainer) {
                        break;
                    } else {
                        size--;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            if (RemoteAnimationController.this.mPendingAnimations.isEmpty() || !(this.mRecord.mWindowContainer.isActivityTypeHome() || z)) {
                RemoteAnimationController.this.cancelAnimation("allAppAnimationsCanceled");
            }
        }

        @Override // com.android.server.wm.AnimationAdapter
        public long getDurationHint() {
            return RemoteAnimationController.this.mRemoteAnimationAdapter.getDuration();
        }

        @Override // com.android.server.wm.AnimationAdapter
        public long getStatusBarTransitionsStartTime() {
            return SystemClock.uptimeMillis() + RemoteAnimationController.this.mRemoteAnimationAdapter.getStatusBarTransitionDelay();
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void dump(PrintWriter printWriter, String str) {
            printWriter.print(str);
            printWriter.print("container=");
            printWriter.println(this.mRecord.mWindowContainer);
            if (this.mRecord.mTarget != null) {
                printWriter.print(str);
                printWriter.println("Target:");
                this.mRecord.mTarget.dump(printWriter, str + "  ");
                return;
            }
            printWriter.print(str);
            printWriter.println("Target: null");
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void dumpDebug(ProtoOutputStream protoOutputStream) {
            long start = protoOutputStream.start(1146756268034L);
            RemoteAnimationTarget remoteAnimationTarget = this.mRecord.mTarget;
            if (remoteAnimationTarget != null) {
                remoteAnimationTarget.dumpDebug(protoOutputStream, 1146756268033L);
            }
            protoOutputStream.end(start);
        }
    }
}
