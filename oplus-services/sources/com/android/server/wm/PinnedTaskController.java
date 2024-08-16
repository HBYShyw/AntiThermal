package com.android.server.wm;

import android.R;
import android.app.PictureInPictureParams;
import android.content.ComponentName;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.util.RotationUtils;
import android.util.Slog;
import android.view.IPinnedTaskListener;
import android.view.SurfaceControl;
import android.window.PictureInPictureSurfaceTransaction;
import com.android.server.wm.utils.LogUtil;
import java.io.PrintWriter;
import java.util.function.Predicate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class PinnedTaskController {
    private static final int DEFER_ORIENTATION_CHANGE_TIMEOUT_MS = 1000;
    private static final String TAG = "WindowManager";
    private boolean mDeferOrientationChanging;
    private Rect mDestRotatedBounds;
    private final DisplayContent mDisplayContent;
    private boolean mFreezingTaskConfig;
    private int mImeHeight;
    private boolean mIsImeShowing;
    private float mMaxAspectRatio;
    private float mMinAspectRatio;
    private IPinnedTaskListener mPinnedTaskListener;
    private PictureInPictureSurfaceTransaction mPipTransaction;
    private final WindowManagerService mService;
    private final PinnedTaskListenerDeathHandler mPinnedTaskListenerDeathHandler = new PinnedTaskListenerDeathHandler();
    private final Runnable mDeferOrientationTimeoutRunnable = new Runnable() { // from class: com.android.server.wm.PinnedTaskController$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            PinnedTaskController.this.lambda$new$0();
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class PinnedTaskListenerDeathHandler implements IBinder.DeathRecipient {
        private PinnedTaskListenerDeathHandler() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            WindowManagerGlobalLock windowManagerGlobalLock = PinnedTaskController.this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    PinnedTaskController.this.mPinnedTaskListener = null;
                    PinnedTaskController.this.mFreezingTaskConfig = false;
                    PinnedTaskController.this.mDeferOrientationTimeoutRunnable.run();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PinnedTaskController(WindowManagerService windowManagerService, DisplayContent displayContent) {
        this.mService = windowManagerService;
        this.mDisplayContent = displayContent;
        reloadResources();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mDeferOrientationChanging) {
                    continueOrientationChange();
                    this.mService.mWindowPlacerLocked.requestTraversal();
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPostDisplayConfigurationChanged() {
        reloadResources();
        this.mFreezingTaskConfig = false;
    }

    private void reloadResources() {
        Resources resources = this.mService.mContext.getResources();
        this.mMinAspectRatio = resources.getFloat(R.dimen.conversation_badge_side_margin);
        this.mMaxAspectRatio = resources.getFloat(R.dimen.conversation_avatar_size_group_expanded);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerPinnedTaskListener(IPinnedTaskListener iPinnedTaskListener) {
        try {
            iPinnedTaskListener.asBinder().linkToDeath(this.mPinnedTaskListenerDeathHandler, 0);
            this.mPinnedTaskListener = iPinnedTaskListener;
            notifyImeVisibilityChanged(this.mIsImeShowing, this.mImeHeight);
            notifyMovementBoundsChanged(false);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to register pinned task listener", e);
        }
    }

    public boolean isValidPictureInPictureAspectRatio(float f) {
        return Float.compare(this.mMinAspectRatio, f) <= 0 && Float.compare(f, this.mMaxAspectRatio) <= 0;
    }

    public boolean isValidExpandedPictureInPictureAspectRatio(float f) {
        return Float.compare(this.mMinAspectRatio, f) > 0 || Float.compare(f, this.mMaxAspectRatio) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deferOrientationChangeForEnteringPipFromFullScreenIfNeeded() {
        if (this.mDisplayContent.getWrapper().getExtImpl().skipDeferOrientationChangeForEnteringPipFromFullScreen()) {
            return;
        }
        ActivityRecord activity = this.mDisplayContent.getActivity(new Predicate() { // from class: com.android.server.wm.PinnedTaskController$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$deferOrientationChangeForEnteringPipFromFullScreenIfNeeded$1;
                lambda$deferOrientationChangeForEnteringPipFromFullScreenIfNeeded$1 = PinnedTaskController.lambda$deferOrientationChangeForEnteringPipFromFullScreenIfNeeded$1((ActivityRecord) obj);
                return lambda$deferOrientationChangeForEnteringPipFromFullScreenIfNeeded$1;
            }
        });
        LogUtil.d(TAG, "topFullscreen = " + activity + ", the top activity determines the display orientation of the PiP window");
        if (activity == null || activity.hasFixedRotationTransform()) {
            return;
        }
        int rotationForActivityInDifferentOrientation = this.mDisplayContent.rotationForActivityInDifferentOrientation(activity);
        if (rotationForActivityInDifferentOrientation == -1) {
            LogUtil.d(TAG, "Orientation has not changed");
            return;
        }
        this.mDisplayContent.setFixedRotationLaunchingApp(activity, rotationForActivityInDifferentOrientation);
        this.mDeferOrientationChanging = true;
        this.mService.mH.removeCallbacks(this.mDeferOrientationTimeoutRunnable);
        this.mService.mH.postDelayed(this.mDeferOrientationTimeoutRunnable, (int) (Math.max(1.0f, this.mService.getCurrentAnimatorScale()) * 1000.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$deferOrientationChangeForEnteringPipFromFullScreenIfNeeded$1(ActivityRecord activityRecord) {
        return activityRecord.providesOrientation() && !activityRecord.getTask().inMultiWindowMode() && activityRecord.getWindowingMode() != 100 && (activityRecord.getWrapper().getExtImpl() == null || !activityRecord.getWrapper().getExtImpl().isCompactWindowingMode(activityRecord.getWindowingMode()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldDeferOrientationChange() {
        return this.mDeferOrientationChanging;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setEnterPipBounds(Rect rect) {
        if (this.mDeferOrientationChanging) {
            this.mFreezingTaskConfig = true;
            this.mDestRotatedBounds = new Rect(rect);
            if (this.mDisplayContent.mTransitionController.isShellTransitionsEnabled()) {
                return;
            }
            continueOrientationChange();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setEnterPipTransaction(PictureInPictureSurfaceTransaction pictureInPictureSurfaceTransaction) {
        this.mFreezingTaskConfig = true;
        this.mPipTransaction = pictureInPictureSurfaceTransaction;
    }

    private void continueOrientationChange() {
        this.mDeferOrientationChanging = false;
        this.mService.mH.removeCallbacks(this.mDeferOrientationTimeoutRunnable);
        WindowContainer lastOrientationSource = this.mDisplayContent.getLastOrientationSource();
        if (lastOrientationSource == null || lastOrientationSource.isAppTransitioning()) {
            return;
        }
        this.mDisplayContent.continueUpdateOrientationForDiffOrienLaunchingApp();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startSeamlessRotationIfNeeded(SurfaceControl.Transaction transaction, int i, int i2) {
        TaskDisplayArea defaultTaskDisplayArea;
        Task rootPinnedTask;
        Rect rect = this.mDestRotatedBounds;
        PictureInPictureSurfaceTransaction pictureInPictureSurfaceTransaction = this.mPipTransaction;
        boolean z = pictureInPictureSurfaceTransaction == null || pictureInPictureSurfaceTransaction.mPosition == null;
        if ((rect == null && z) || (rootPinnedTask = (defaultTaskDisplayArea = this.mDisplayContent.getDefaultTaskDisplayArea()).getRootPinnedTask()) == null) {
            return;
        }
        Rect rect2 = null;
        this.mDestRotatedBounds = null;
        this.mPipTransaction = null;
        Rect bounds = defaultTaskDisplayArea.getBounds();
        if (!z) {
            PointF pointF = pictureInPictureSurfaceTransaction.mPosition;
            float f = pointF.x;
            float f2 = pointF.y;
            Matrix matrix = pictureInPictureSurfaceTransaction.getMatrix();
            float f3 = pictureInPictureSurfaceTransaction.mRotation;
            if (f3 == 90.0f) {
                PointF pointF2 = pictureInPictureSurfaceTransaction.mPosition;
                f = pointF2.y;
                f2 = bounds.right - pointF2.x;
                matrix.postRotate(-90.0f);
            } else if (f3 == -90.0f) {
                float f4 = bounds.bottom;
                PointF pointF3 = pictureInPictureSurfaceTransaction.mPosition;
                f = f4 - pointF3.y;
                f2 = pointF3.x;
                matrix.postRotate(90.0f);
            }
            matrix.postTranslate(f, f2);
            SurfaceControl surfaceControl = rootPinnedTask.getSurfaceControl();
            transaction.setMatrix(surfaceControl, matrix, new float[9]);
            if (pictureInPictureSurfaceTransaction.hasCornerRadiusSet()) {
                transaction.setCornerRadius(surfaceControl, pictureInPictureSurfaceTransaction.mCornerRadius);
            }
            Slog.i(TAG, "Seamless rotation PiP tx=" + pictureInPictureSurfaceTransaction + " pos=" + f + "," + f2);
            return;
        }
        PictureInPictureParams pictureInPictureParams = rootPinnedTask.getPictureInPictureParams();
        if (pictureInPictureParams != null && pictureInPictureParams.hasSourceBoundsHint()) {
            rect2 = pictureInPictureParams.getSourceRectHint();
        }
        Slog.i(TAG, "Seamless rotation PiP bounds=" + rect + " hintRect=" + rect2);
        int deltaRotation = RotationUtils.deltaRotation(i, i2);
        if (rect2 != null && deltaRotation == 3 && rootPinnedTask.getDisplayCutoutInsets() != null) {
            Rect rect3 = RotationUtils.rotateInsets(Insets.of(rootPinnedTask.getDisplayCutoutInsets()), RotationUtils.deltaRotation(i2, i)).toRect();
            rect2.offset(rect3.left, rect3.top);
        }
        if (rect2 == null || !bounds.contains(rect2)) {
            rect2 = bounds;
        }
        int width = rect2.width();
        int height = rect2.height();
        float width2 = width <= height ? rect.width() / width : rect.height() / height;
        int i3 = (int) (((rect2.left - bounds.left) * width2) + 0.5f);
        int i4 = (int) (((rect2.top - bounds.top) * width2) + 0.5f);
        Matrix matrix2 = new Matrix();
        matrix2.setScale(width2, width2);
        matrix2.postTranslate(rect.left - i3, rect.top - i4);
        transaction.setMatrix(rootPinnedTask.getSurfaceControl(), matrix2, new float[9]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFreezingTaskConfig(Task task) {
        return this.mFreezingTaskConfig && task == this.mDisplayContent.getDefaultTaskDisplayArea().getRootPinnedTask();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCancelFixedRotationTransform() {
        this.mFreezingTaskConfig = false;
        this.mDeferOrientationChanging = false;
        this.mDestRotatedBounds = null;
        this.mPipTransaction = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onActivityHidden(ComponentName componentName) {
        IPinnedTaskListener iPinnedTaskListener = this.mPinnedTaskListener;
        if (iPinnedTaskListener == null) {
            return;
        }
        try {
            iPinnedTaskListener.onActivityHidden(componentName);
        } catch (RemoteException e) {
            Slog.e(TAG, "Error delivering reset reentry fraction event.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAdjustedForIme(boolean z, int i) {
        boolean z2 = z && i > 0;
        if (!z2) {
            i = 0;
        }
        if (z2 == this.mIsImeShowing && i == this.mImeHeight) {
            return;
        }
        this.mIsImeShowing = z2;
        this.mImeHeight = i;
        notifyImeVisibilityChanged(z2, i);
        notifyMovementBoundsChanged(true);
    }

    private void notifyImeVisibilityChanged(boolean z, int i) {
        IPinnedTaskListener iPinnedTaskListener = this.mPinnedTaskListener;
        if (iPinnedTaskListener != null) {
            try {
                iPinnedTaskListener.onImeVisibilityChanged(z, i);
            } catch (RemoteException e) {
                Slog.e(TAG, "Error delivering bounds changed event.", e);
            }
        }
    }

    private void notifyMovementBoundsChanged(boolean z) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                IPinnedTaskListener iPinnedTaskListener = this.mPinnedTaskListener;
                if (iPinnedTaskListener == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                try {
                    iPinnedTaskListener.onMovementBoundsChanged(z);
                } catch (RemoteException e) {
                    Slog.e(TAG, "Error delivering actions changed event.", e);
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(String str, PrintWriter printWriter) {
        printWriter.println(str + "PinnedTaskController");
        if (this.mDeferOrientationChanging) {
            printWriter.println(str + "  mDeferOrientationChanging=true");
        }
        if (this.mFreezingTaskConfig) {
            printWriter.println(str + "  mFreezingTaskConfig=true");
        }
        if (this.mDestRotatedBounds != null) {
            printWriter.println(str + "  mPendingBounds=" + this.mDestRotatedBounds);
        }
        if (this.mPipTransaction != null) {
            printWriter.println(str + "  mPipTransaction=" + this.mPipTransaction);
        }
        printWriter.println(str + "  mIsImeShowing=" + this.mIsImeShowing);
        printWriter.println(str + "  mImeHeight=" + this.mImeHeight);
        printWriter.println(str + "  mMinAspectRatio=" + this.mMinAspectRatio);
        printWriter.println(str + "  mMaxAspectRatio=" + this.mMaxAspectRatio);
    }
}
