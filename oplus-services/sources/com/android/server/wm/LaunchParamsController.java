package com.android.server.wm;

import android.app.ActivityOptions;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import com.android.server.wm.ActivityStarter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class LaunchParamsController {
    private final LaunchParamsPersister mPersister;
    private final ActivityTaskManagerService mService;
    private final List<LaunchParamsModifier> mModifiers = new ArrayList();
    private final LaunchParams mTmpParams = new LaunchParams();
    private final LaunchParams mTmpCurrent = new LaunchParams();
    private final LaunchParams mTmpResult = new LaunchParams();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface LaunchParamsModifier {
        public static final int PHASE_BOUNDS = 3;
        public static final int PHASE_DISPLAY = 0;
        public static final int PHASE_DISPLAY_AREA = 2;
        public static final int PHASE_WINDOWING_MODE = 1;
        public static final int RESULT_CONTINUE = 2;
        public static final int RESULT_DONE = 1;
        public static final int RESULT_SKIP = 0;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public @interface Phase {
        }

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public @interface Result {
        }

        int onCalculate(Task task, ActivityInfo.WindowLayout windowLayout, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions, ActivityStarter.Request request, int i, LaunchParams launchParams, LaunchParams launchParams2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LaunchParamsController(ActivityTaskManagerService activityTaskManagerService, LaunchParamsPersister launchParamsPersister) {
        this.mService = activityTaskManagerService;
        this.mPersister = launchParamsPersister;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerDefaultModifiers(ActivityTaskSupervisor activityTaskSupervisor) {
        registerModifier(new TaskLaunchParamsModifier(activityTaskSupervisor));
        if (DesktopModeLaunchParamsModifier.DESKTOP_MODE_SUPPORTED) {
            registerModifier(new DesktopModeLaunchParamsModifier());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void calculate(Task task, ActivityInfo.WindowLayout windowLayout, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions, ActivityStarter.Request request, int i, LaunchParams launchParams) {
        TaskDisplayArea finalPreferredTaskDisplayArea;
        launchParams.reset();
        if (task != null || activityRecord != null) {
            this.mPersister.getLaunchParams(task, activityRecord, launchParams);
        }
        for (int size = this.mModifiers.size() - 1; size >= 0; size--) {
            this.mTmpCurrent.set(launchParams);
            this.mTmpResult.reset();
            int onCalculate = this.mModifiers.get(size).onCalculate(task, windowLayout, activityRecord, activityRecord2, activityOptions, request, i, this.mTmpCurrent, this.mTmpResult);
            if (onCalculate == 1) {
                launchParams.set(this.mTmpResult);
                return;
            } else {
                if (onCalculate == 2) {
                    launchParams.set(this.mTmpResult);
                }
            }
        }
        if (activityRecord != null && activityRecord.requestedVrComponent != null) {
            launchParams.mPreferredTaskDisplayArea = this.mService.mRootWindowContainer.getDefaultTaskDisplayArea();
        } else {
            ActivityTaskManagerService activityTaskManagerService = this.mService;
            int i2 = activityTaskManagerService.mVr2dDisplayId;
            if (i2 != -1) {
                launchParams.mPreferredTaskDisplayArea = activityTaskManagerService.mRootWindowContainer.getDisplayContent(i2).getDefaultTaskDisplayArea();
            }
        }
        if (ActivityTaskManagerService.LTW_DISABLE || !this.mService.getWrapper().getExtImpl().getRemoteTaskManager().isDisplaySwitchDetected() || (finalPreferredTaskDisplayArea = this.mService.getWrapper().getExtImpl().getRemoteTaskManager().getFinalPreferredTaskDisplayArea()) == null) {
            return;
        }
        launchParams.mPreferredTaskDisplayArea = finalPreferredTaskDisplayArea;
    }

    boolean layoutTask(Task task, ActivityInfo.WindowLayout windowLayout) {
        return layoutTask(task, windowLayout, null, null, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean layoutTask(Task task, ActivityInfo.WindowLayout windowLayout, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions) {
        calculate(task, windowLayout, activityRecord, activityRecord2, activityOptions, null, 3, this.mTmpParams);
        if (this.mTmpParams.isEmpty()) {
            return false;
        }
        this.mService.deferWindowLayout();
        try {
            if (!this.mTmpParams.mBounds.isEmpty()) {
                if (!task.getRootTask().inMultiWindowMode() && !task.getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
                    task.setLastNonFullscreenBounds(this.mTmpParams.mBounds);
                }
                task.setBounds(this.mTmpParams.mBounds);
                this.mService.continueWindowLayout();
                return true;
            }
            return false;
        } finally {
            this.mService.continueWindowLayout();
        }
    }

    void registerModifier(LaunchParamsModifier launchParamsModifier) {
        if (this.mModifiers.contains(launchParamsModifier)) {
            return;
        }
        this.mModifiers.add(launchParamsModifier);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class LaunchParams {
        final Rect mBounds = new Rect();
        TaskDisplayArea mPreferredTaskDisplayArea;
        int mWindowingMode;

        /* JADX INFO: Access modifiers changed from: package-private */
        public void reset() {
            this.mBounds.setEmpty();
            this.mPreferredTaskDisplayArea = null;
            this.mWindowingMode = 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void set(LaunchParams launchParams) {
            this.mBounds.set(launchParams.mBounds);
            this.mPreferredTaskDisplayArea = launchParams.mPreferredTaskDisplayArea;
            this.mWindowingMode = launchParams.mWindowingMode;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isEmpty() {
            return this.mBounds.isEmpty() && this.mPreferredTaskDisplayArea == null && this.mWindowingMode == 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean hasWindowingMode() {
            return this.mWindowingMode != 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean hasPreferredTaskDisplayArea() {
            return this.mPreferredTaskDisplayArea != null;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            LaunchParams launchParams = (LaunchParams) obj;
            if (this.mPreferredTaskDisplayArea != launchParams.mPreferredTaskDisplayArea || this.mWindowingMode != launchParams.mWindowingMode) {
                return false;
            }
            Rect rect = this.mBounds;
            return rect != null ? rect.equals(launchParams.mBounds) : launchParams.mBounds == null;
        }

        public int hashCode() {
            Rect rect = this.mBounds;
            int hashCode = (rect != null ? rect.hashCode() : 0) * 31;
            TaskDisplayArea taskDisplayArea = this.mPreferredTaskDisplayArea;
            return ((hashCode + (taskDisplayArea != null ? taskDisplayArea.hashCode() : 0)) * 31) + this.mWindowingMode;
        }
    }
}
