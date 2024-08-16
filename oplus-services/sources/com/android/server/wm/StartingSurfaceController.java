package com.android.server.wm;

import android.app.ActivityOptions;
import android.app.compat.CompatChanges;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.os.UserHandle;
import android.util.Slog;
import android.window.TaskSnapshot;
import java.util.ArrayList;
import java.util.function.Supplier;
import vendor.oplus.hardware.charger.ChargerErrorCode;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class StartingSurfaceController {
    private static final long ALLOW_COPY_SOLID_COLOR_VIEW = 205907456;
    private static final String TAG = "WindowManager";
    private final ArrayList<DeferringStartingWindowRecord> mDeferringAddStartActivities = new ArrayList<>();
    private boolean mDeferringAddStartingWindow;
    boolean mInitNewTask;
    boolean mInitProcessRunning;
    boolean mInitTaskSwitch;
    private final WindowManagerService mService;
    private final SplashScreenExceptionList mSplashScreenExceptionsList;

    public StartingSurfaceController(WindowManagerService windowManagerService) {
        this.mService = windowManagerService;
        this.mSplashScreenExceptionsList = new SplashScreenExceptionList(windowManagerService.mContext.getMainExecutor());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StartingSurface createSplashScreenStartingSurface(ActivityRecord activityRecord, int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                Task task = activityRecord.getTask();
                if (task == null || !this.mService.mAtmService.mTaskOrganizerController.addStartingWindow(task, activityRecord, i, null)) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                StartingSurface startingSurface = new StartingSurface(task);
                WindowManagerService.resetPriorityAfterLockedSection();
                return startingSurface;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isExceptionApp(String str, int i, Supplier<ApplicationInfo> supplier) {
        return this.mSplashScreenExceptionsList.isException(str, i, supplier);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:0:?, code lost:
    
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x000f, code lost:
    
        if (r8 == 1) goto L11;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v13 */
    /* JADX WARN: Type inference failed for: r0v14 */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v23 */
    /* JADX WARN: Type inference failed for: r0v24 */
    /* JADX WARN: Type inference failed for: r0v3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int makeStartingWindowTypeParameter(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, int i, String str, int i2) {
        ?? r0;
        int i3;
        if (z2) {
            r0 = (z ? 1 : 0) | 2;
        }
        if (z3) {
            r0 = (r0 == true ? 1 : 0) | 4;
        }
        if (z4) {
            r0 = (r0 == true ? 1 : 0) | '\b';
        }
        if (!z5) {
            i3 = r0;
        }
        i3 = (r0 == true ? 1 : 0) | 16;
        if (z6) {
            i3 = (i3 == true ? 1 : 0) | 32;
        }
        if (z7) {
            i3 = (i3 == true ? 1 : 0) | ChargerErrorCode.ERR_FILE_FAILURE_ACCESS;
        }
        if (z8) {
            i3 = (i3 == true ? 1 : 0) | 64;
        }
        return (i == 2 && CompatChanges.isChangeEnabled(ALLOW_COPY_SOLID_COLOR_VIEW, str, UserHandle.of(i2))) ? i3 | 128 : i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StartingSurface createTaskSnapshotSurface(ActivityRecord activityRecord, TaskSnapshot taskSnapshot) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                Task task = activityRecord.getTask();
                if (task == null) {
                    Slog.w(TAG, "TaskSnapshotSurface.create: Failed to find task for activity=" + activityRecord);
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                ActivityRecord topFullscreenActivity = activityRecord.getTask().getTopFullscreenActivity();
                if (topFullscreenActivity == null) {
                    Slog.w(TAG, "TaskSnapshotSurface.create: Failed to find top fullscreen for task=" + task);
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                WindowState topFullscreenOpaqueWindow = topFullscreenActivity.getTopFullscreenOpaqueWindow();
                if (topFullscreenOpaqueWindow == null) {
                    Slog.w(TAG, "TaskSnapshotSurface.create: no opaque window in " + topFullscreenActivity);
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                DisplayRotation displayRotation = activityRecord.getDisplayContent().getDisplayRotation();
                boolean z = (taskSnapshot.getRotation() == displayRotation.mLandscapeRotation || taskSnapshot.getRotation() == displayRotation.mSeascapeRotation) && ActivityInfo.isFixedOrientationPortrait(topFullscreenOpaqueWindow.getConfiguration().orientation);
                if (activityRecord.mDisplayContent.getRotation() != taskSnapshot.getRotation() || z) {
                    activityRecord.mDisplayContent.handleTopActivityLaunchingInDifferentOrientation(activityRecord, false);
                }
                this.mService.mAtmService.mTaskOrganizerController.addStartingWindow(task, activityRecord, 0, taskSnapshot);
                StartingSurface startingSurface = new StartingSurface(task);
                WindowManagerService.resetPriorityAfterLockedSection();
                return startingSurface;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class DeferringStartingWindowRecord {
        final ActivityRecord mDeferring;
        final ActivityRecord mPrev;
        final ActivityRecord mSource;

        DeferringStartingWindowRecord(ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityRecord activityRecord3) {
            this.mDeferring = activityRecord;
            this.mPrev = activityRecord2;
            this.mSource = activityRecord3;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void showStartingWindow(ActivityRecord activityRecord, ActivityRecord activityRecord2, boolean z, boolean z2, ActivityRecord activityRecord3) {
        if (this.mDeferringAddStartingWindow) {
            addDeferringRecord(activityRecord, activityRecord2, z, z2, activityRecord3);
        } else {
            activityRecord.showStartingWindow(activityRecord2, z, z2, true, activityRecord3);
        }
    }

    private void addDeferringRecord(ActivityRecord activityRecord, ActivityRecord activityRecord2, boolean z, boolean z2, ActivityRecord activityRecord3) {
        if (this.mDeferringAddStartActivities.isEmpty()) {
            this.mInitProcessRunning = activityRecord.isProcessRunning();
            this.mInitNewTask = z;
            this.mInitTaskSwitch = z2;
        }
        this.mDeferringAddStartActivities.add(new DeferringStartingWindowRecord(activityRecord, activityRecord2, activityRecord3));
    }

    private void showStartingWindowFromDeferringActivities(ActivityOptions activityOptions) {
        for (int size = this.mDeferringAddStartActivities.size() - 1; size >= 0; size--) {
            DeferringStartingWindowRecord deferringStartingWindowRecord = this.mDeferringAddStartActivities.get(size);
            if (deferringStartingWindowRecord.mDeferring.getTask() == null) {
                Slog.e(TAG, "No task exists: " + deferringStartingWindowRecord.mDeferring.shortComponentName + " parent: " + deferringStartingWindowRecord.mDeferring.getParent());
            } else {
                deferringStartingWindowRecord.mDeferring.showStartingWindow(deferringStartingWindowRecord.mPrev, this.mInitNewTask, this.mInitTaskSwitch, this.mInitProcessRunning, true, deferringStartingWindowRecord.mSource, activityOptions);
                if (deferringStartingWindowRecord.mDeferring.mStartingData != null) {
                    break;
                }
            }
        }
        this.mDeferringAddStartActivities.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void beginDeferAddStartingWindow() {
        this.mDeferringAddStartingWindow = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void endDeferAddStartingWindow(ActivityOptions activityOptions) {
        this.mDeferringAddStartingWindow = false;
        showStartingWindowFromDeferringActivities(activityOptions);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public final class StartingSurface {
        private final Task mTask;

        StartingSurface(Task task) {
            this.mTask = task;
        }

        public void remove(boolean z) {
            WindowManagerGlobalLock windowManagerGlobalLock = StartingSurfaceController.this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    StartingSurfaceController.this.mService.mAtmService.mTaskOrganizerController.removeStartingWindow(this.mTask, z);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }
    }
}
