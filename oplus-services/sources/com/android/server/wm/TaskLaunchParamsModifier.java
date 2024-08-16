package com.android.server.wm;

import android.app.ActivityOptions;
import android.app.WindowConfiguration;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.util.Size;
import android.window.WindowContainerToken;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.wm.ActivityStarter;
import com.android.server.wm.LaunchParamsController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class TaskLaunchParamsModifier implements LaunchParamsController.LaunchParamsModifier {
    private static final int BOUNDS_CONFLICT_THRESHOLD = 4;
    private static final int CASCADING_OFFSET_DP = 75;
    private static final boolean DEBUG = false;
    private static final int EPSILON = 2;
    private static final int MINIMAL_STEP = 1;
    private static final int STEP_DENOMINATOR = 16;
    private static final String TAG = "ActivityTaskManager";
    private StringBuilder mLogBuilder;
    private final ActivityTaskSupervisor mSupervisor;
    private TaskDisplayArea mTmpDisplayArea;
    private final Rect mTmpBounds = new Rect();
    private final Rect mTmpStableBounds = new Rect();
    private final int[] mTmpDirections = new int[2];
    private ITaskLaunchParamsModifierExt mModifierExt = (ITaskLaunchParamsModifierExt) ExtLoader.type(ITaskLaunchParamsModifierExt.class).base(this).create();

    private void appendLog(String str) {
    }

    private int convertOrientationToScreenOrientation(int i) {
        if (i != 1) {
            return i != 2 ? -1 : 0;
        }
        return 1;
    }

    private void initLogBuilder(Task task, ActivityRecord activityRecord) {
    }

    private void outputLog() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskLaunchParamsModifier(ActivityTaskSupervisor activityTaskSupervisor) {
        this.mSupervisor = activityTaskSupervisor;
    }

    @Override // com.android.server.wm.LaunchParamsController.LaunchParamsModifier
    public int onCalculate(Task task, ActivityInfo.WindowLayout windowLayout, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions, ActivityStarter.Request request, int i, LaunchParamsController.LaunchParams launchParams, LaunchParamsController.LaunchParams launchParams2) {
        initLogBuilder(task, activityRecord);
        int calculate = calculate(task, windowLayout, activityRecord, activityRecord2, activityOptions, request, i, launchParams, launchParams2);
        outputLog();
        return calculate;
    }

    /* JADX WARN: Removed duplicated region for block: B:135:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0299  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x012f  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0138 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0165  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x017b  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01db  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01e3 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01e5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int calculate(Task task, ActivityInfo.WindowLayout windowLayout, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityOptions activityOptions, ActivityStarter.Request request, int i, LaunchParamsController.LaunchParams launchParams, LaunchParamsController.LaunchParams launchParams2) {
        ActivityRecord activityRecord3;
        boolean z;
        boolean z2;
        int i2;
        boolean z3;
        int i3;
        int i4;
        DisplayContent displayContent;
        TaskDisplayArea taskDisplayArea;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        TaskDisplayArea taskDisplayArea2;
        TaskDisplayArea taskDisplayArea3;
        int i10;
        if (task != null) {
            activityRecord3 = task.getRootActivity() == null ? activityRecord : task.getRootActivity();
        } else {
            activityRecord3 = activityRecord;
        }
        if (activityRecord3 == null) {
            return 0;
        }
        TaskDisplayArea preferredLaunchTaskDisplayArea = getPreferredLaunchTaskDisplayArea(task, activityOptions, activityRecord2, launchParams, activityRecord, request);
        if (!ActivityTaskManagerService.LTW_DISABLE) {
            preferredLaunchTaskDisplayArea = this.mSupervisor.mService.getWrapper().getExtImpl().getRemoteTaskManager().queryPreferredDisplayArea(task, preferredLaunchTaskDisplayArea, activityRecord.intent, activityRecord2, activityRecord, activityOptions);
        }
        TaskDisplayArea taskDisplayArea4 = preferredLaunchTaskDisplayArea;
        launchParams2.mPreferredTaskDisplayArea = taskDisplayArea4;
        DisplayContent displayContent2 = taskDisplayArea4.mDisplayContent;
        if (i == 0) {
            return 2;
        }
        int launchWindowingMode = activityOptions != null ? activityOptions.getLaunchWindowingMode() : 0;
        if (launchWindowingMode == 0 && canInheritWindowingModeFromSource(displayContent2, taskDisplayArea4, activityRecord2)) {
            launchWindowingMode = activityRecord2.getTask().getWindowingMode();
        }
        if (launchWindowingMode == 0 && task != null && task.getTaskDisplayArea() == taskDisplayArea4 && !task.getRootTask().mReparentLeafTaskIfRelaunch) {
            launchWindowingMode = task.getWindowingMode();
        }
        boolean canCalculateBoundsForFullscreenTask = canCalculateBoundsForFullscreenTask(taskDisplayArea4, launchWindowingMode);
        boolean canApplyFreeformWindowPolicy = canApplyFreeformWindowPolicy(taskDisplayArea4, launchWindowingMode);
        boolean z4 = windowLayout != null && (canApplyFreeformWindowPolicy || canCalculateBoundsForFullscreenTask);
        if (this.mSupervisor.canUseActivityOptionsLaunchBounds(activityOptions) && (canApplyFreeformWindowPolicy || canApplyPipWindowPolicy(launchWindowingMode) || canCalculateBoundsForFullscreenTask)) {
            if (launchWindowingMode == 0 && canApplyFreeformWindowPolicy) {
                launchWindowingMode = 5;
            }
            launchParams2.mBounds.set(activityOptions.getLaunchBounds());
        } else {
            if (z4) {
                this.mTmpBounds.set(launchParams.mBounds);
                getLayoutBounds(taskDisplayArea4, activityRecord3, windowLayout, this.mTmpBounds);
                if (!this.mTmpBounds.isEmpty()) {
                    if (canApplyFreeformWindowPolicy) {
                        launchWindowingMode = 5;
                    }
                    launchParams2.mBounds.set(this.mTmpBounds);
                    z = true;
                    z2 = true;
                    if (task != null) {
                        i2 = 0;
                        if (task.getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0]) && activityOptions != null && activityOptions.getLaunchBounds() != null) {
                            launchParams2.mBounds.set(activityOptions.getLaunchBounds());
                            z3 = true;
                            if (launchParams.isEmpty() && !z3 && ((taskDisplayArea3 = launchParams.mPreferredTaskDisplayArea) == null || taskDisplayArea3.getDisplayId() == displayContent2.getDisplayId())) {
                                if (launchParams.hasWindowingMode() && taskDisplayArea4.inFreeformWindowingMode()) {
                                    launchWindowingMode = launchParams.mWindowingMode;
                                    if (launchWindowingMode != 5) {
                                        i10 = 1;
                                        if (launchParams.mBounds.isEmpty()) {
                                            launchParams2.mBounds.set(launchParams.mBounds);
                                            i3 = 1;
                                        } else {
                                            i3 = i10;
                                        }
                                    }
                                }
                                i10 = i2;
                                if (launchParams.mBounds.isEmpty()) {
                                }
                            } else {
                                i3 = i2;
                            }
                            if (taskDisplayArea4.inFreeformWindowingMode() || launchWindowingMode == 2 || activityRecord3.isResizeable()) {
                                i4 = i2;
                                displayContent = displayContent2;
                                taskDisplayArea = taskDisplayArea4;
                                i5 = 1;
                                i6 = launchWindowingMode;
                            } else if (shouldLaunchUnresizableAppInFreeform(activityRecord3, taskDisplayArea4, activityOptions)) {
                                if (launchParams2.mBounds.isEmpty()) {
                                    i5 = 1;
                                    i4 = i2;
                                    i6 = 5;
                                    displayContent = displayContent2;
                                    taskDisplayArea = taskDisplayArea4;
                                    getTaskBounds(activityRecord3, taskDisplayArea4, windowLayout, 5, z3, launchParams2.mBounds);
                                    i7 = 1;
                                    launchParams2.mWindowingMode = i6 == taskDisplayArea.getWindowingMode() ? i4 : i6;
                                    if (i == i5) {
                                        return 2;
                                    }
                                    final int windowingMode = i6 != 0 ? i6 : taskDisplayArea.getWindowingMode();
                                    if (activityOptions == null || (activityOptions.getLaunchTaskDisplayArea() == null && activityOptions.getLaunchTaskDisplayAreaFeatureId() == -1)) {
                                        final int resolveActivityType = this.mSupervisor.mRootWindowContainer.resolveActivityType(activityRecord3, activityOptions, task);
                                        displayContent.forAllTaskDisplayAreas(new Predicate() { // from class: com.android.server.wm.TaskLaunchParamsModifier$$ExternalSyntheticLambda1
                                            @Override // java.util.function.Predicate
                                            public final boolean test(Object obj) {
                                                boolean lambda$calculate$0;
                                                lambda$calculate$0 = TaskLaunchParamsModifier.this.lambda$calculate$0(windowingMode, resolveActivityType, (TaskDisplayArea) obj);
                                                return lambda$calculate$0;
                                            }
                                        });
                                        TaskDisplayArea taskDisplayArea5 = this.mTmpDisplayArea;
                                        if (taskDisplayArea5 != null) {
                                            TaskDisplayArea taskDisplayArea6 = taskDisplayArea;
                                            if (taskDisplayArea5 != taskDisplayArea6) {
                                                launchParams2.mWindowingMode = i6 == taskDisplayArea5.getWindowingMode() ? i4 : i6;
                                                if (z2) {
                                                    launchParams2.mBounds.setEmpty();
                                                    getLayoutBounds(this.mTmpDisplayArea, activityRecord3, windowLayout, launchParams2.mBounds);
                                                    z3 = !launchParams2.mBounds.isEmpty();
                                                } else if (i7 != 0) {
                                                    launchParams2.mBounds.setEmpty();
                                                    i8 = windowingMode;
                                                    taskDisplayArea = taskDisplayArea6;
                                                    i9 = 2;
                                                    getTaskBounds(activityRecord3, this.mTmpDisplayArea, windowLayout, i6, z3, launchParams2.mBounds);
                                                    taskDisplayArea2 = this.mTmpDisplayArea;
                                                    if (taskDisplayArea2 == null) {
                                                        this.mTmpDisplayArea = null;
                                                        appendLog("overridden-display-area=[" + WindowConfiguration.activityTypeToString(resolveActivityType) + ", " + WindowConfiguration.windowingModeToString(i8) + ", " + taskDisplayArea2 + "]");
                                                    } else {
                                                        taskDisplayArea2 = taskDisplayArea;
                                                    }
                                                }
                                            }
                                            i8 = windowingMode;
                                            taskDisplayArea = taskDisplayArea6;
                                        } else {
                                            i8 = windowingMode;
                                        }
                                        i9 = 2;
                                        taskDisplayArea2 = this.mTmpDisplayArea;
                                        if (taskDisplayArea2 == null) {
                                        }
                                    } else {
                                        taskDisplayArea2 = taskDisplayArea;
                                        i8 = windowingMode;
                                        i9 = 2;
                                    }
                                    appendLog("display-area=" + taskDisplayArea2);
                                    launchParams2.mPreferredTaskDisplayArea = taskDisplayArea2;
                                    if (i == i9) {
                                        return i9;
                                    }
                                    if (i3 == 0) {
                                        if (activityRecord2 != null && activityRecord2.inFreeformWindowingMode() && i8 == 5 && launchParams2.mBounds.isEmpty() && activityRecord2.getDisplayArea() == taskDisplayArea2) {
                                            cascadeBounds(activityRecord2.getConfiguration().windowConfiguration.getBounds(), taskDisplayArea2, launchParams2.mBounds);
                                        }
                                        getTaskBounds(activityRecord3, taskDisplayArea2, windowLayout, i8, z3, launchParams2.mBounds);
                                    } else if (i8 == 5) {
                                        if (launchParams.mPreferredTaskDisplayArea != taskDisplayArea2) {
                                            adjustBoundsToFitInDisplayArea(taskDisplayArea2, windowLayout, launchParams2.mBounds);
                                        }
                                        adjustBoundsToAvoidConflictInDisplayArea(taskDisplayArea2, launchParams2.mBounds);
                                    }
                                    return i9;
                                }
                                i4 = i2;
                                displayContent = displayContent2;
                                taskDisplayArea = taskDisplayArea4;
                                i5 = 1;
                                i6 = 5;
                            } else {
                                i4 = i2;
                                displayContent = displayContent2;
                                taskDisplayArea = taskDisplayArea4;
                                i5 = 1;
                                launchParams2.mBounds.setEmpty();
                                i6 = 1;
                            }
                            i7 = i4;
                            launchParams2.mWindowingMode = i6 == taskDisplayArea.getWindowingMode() ? i4 : i6;
                            if (i == i5) {
                            }
                        }
                    } else {
                        i2 = 0;
                    }
                    z3 = z;
                    if (launchParams.isEmpty()) {
                    }
                    i3 = i2;
                    if (taskDisplayArea4.inFreeformWindowingMode()) {
                    }
                    i4 = i2;
                    displayContent = displayContent2;
                    taskDisplayArea = taskDisplayArea4;
                    i5 = 1;
                    i6 = launchWindowingMode;
                    i7 = i4;
                    launchParams2.mWindowingMode = i6 == taskDisplayArea.getWindowingMode() ? i4 : i6;
                    if (i == i5) {
                    }
                }
            } else if (launchWindowingMode == 6 && activityOptions != null && activityOptions.getLaunchBounds() != null) {
                launchParams2.mBounds.set(activityOptions.getLaunchBounds());
            }
            z = false;
            z2 = false;
            if (task != null) {
            }
            z3 = z;
            if (launchParams.isEmpty()) {
            }
            i3 = i2;
            if (taskDisplayArea4.inFreeformWindowingMode()) {
            }
            i4 = i2;
            displayContent = displayContent2;
            taskDisplayArea = taskDisplayArea4;
            i5 = 1;
            i6 = launchWindowingMode;
            i7 = i4;
            launchParams2.mWindowingMode = i6 == taskDisplayArea.getWindowingMode() ? i4 : i6;
            if (i == i5) {
            }
        }
        z = true;
        z2 = false;
        if (task != null) {
        }
        z3 = z;
        if (launchParams.isEmpty()) {
        }
        i3 = i2;
        if (taskDisplayArea4.inFreeformWindowingMode()) {
        }
        i4 = i2;
        displayContent = displayContent2;
        taskDisplayArea = taskDisplayArea4;
        i5 = 1;
        i6 = launchWindowingMode;
        i7 = i4;
        launchParams2.mWindowingMode = i6 == taskDisplayArea.getWindowingMode() ? i4 : i6;
        if (i == i5) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$calculate$0(int i, int i2, TaskDisplayArea taskDisplayArea) {
        if (taskDisplayArea.getLaunchRootTask(i, i2, null, null, 0) == null) {
            return false;
        }
        this.mTmpDisplayArea = taskDisplayArea;
        return true;
    }

    private TaskDisplayArea getPreferredLaunchTaskDisplayArea(Task task, ActivityOptions activityOptions, ActivityRecord activityRecord, LaunchParamsController.LaunchParams launchParams, ActivityRecord activityRecord2, ActivityStarter.Request request) {
        DisplayContent displayContent;
        final int launchTaskDisplayAreaFeatureId;
        WindowContainerToken launchTaskDisplayArea = activityOptions != null ? activityOptions.getLaunchTaskDisplayArea() : null;
        TaskDisplayArea taskDisplayArea = launchTaskDisplayArea != null ? (TaskDisplayArea) WindowContainer.fromBinder(launchTaskDisplayArea.asBinder()) : null;
        if (taskDisplayArea == null && activityOptions != null && (launchTaskDisplayAreaFeatureId = activityOptions.getLaunchTaskDisplayAreaFeatureId()) != -1) {
            DisplayContent displayContent2 = this.mSupervisor.mRootWindowContainer.getDisplayContent(activityOptions.getLaunchDisplayId() == -1 ? 0 : activityOptions.getLaunchDisplayId());
            if (displayContent2 != null) {
                taskDisplayArea = (TaskDisplayArea) displayContent2.getItemFromTaskDisplayAreas(new Function() { // from class: com.android.server.wm.TaskLaunchParamsModifier$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        TaskDisplayArea lambda$getPreferredLaunchTaskDisplayArea$1;
                        lambda$getPreferredLaunchTaskDisplayArea$1 = TaskLaunchParamsModifier.lambda$getPreferredLaunchTaskDisplayArea$1(launchTaskDisplayAreaFeatureId, (TaskDisplayArea) obj);
                        return lambda$getPreferredLaunchTaskDisplayArea$1;
                    }
                });
            }
        }
        if (taskDisplayArea == null) {
            int launchDisplayId = activityOptions != null ? activityOptions.getLaunchDisplayId() : -1;
            if (launchDisplayId != -1 && (displayContent = this.mSupervisor.mRootWindowContainer.getDisplayContent(launchDisplayId)) != null) {
                taskDisplayArea = displayContent.getDefaultTaskDisplayArea();
            }
        }
        if (taskDisplayArea == null && activityRecord != null && activityRecord.noDisplay && (taskDisplayArea = activityRecord.mHandoverTaskDisplayArea) == null) {
            DisplayContent displayContent3 = this.mSupervisor.mRootWindowContainer.getDisplayContent(activityRecord.mHandoverLaunchDisplayId);
            if (displayContent3 != null) {
                taskDisplayArea = displayContent3.getDefaultTaskDisplayArea();
            }
        }
        if (taskDisplayArea == null && activityRecord != null) {
            taskDisplayArea = activityRecord.getDisplayArea();
        }
        TaskDisplayArea modifierTaskDisplayAreaIfNeed = this.mModifierExt.modifierTaskDisplayAreaIfNeed(activityRecord2, activityOptions, this.mModifierExt.modifierTaskDisplayAreaIfNeed(this.mSupervisor, taskDisplayArea, task, activityRecord), task, activityRecord);
        Task rootTask = (modifierTaskDisplayAreaIfNeed != null || task == null) ? null : task.getRootTask();
        if (rootTask != null) {
            modifierTaskDisplayAreaIfNeed = rootTask.getDisplayArea();
        }
        TaskDisplayArea modifierTaskDisplayAreaIfNeed2 = this.mModifierExt.modifierTaskDisplayAreaIfNeed(this.mSupervisor, modifierTaskDisplayAreaIfNeed, activityRecord2, false, rootTask);
        if (modifierTaskDisplayAreaIfNeed2 == null && activityOptions != null) {
            DisplayContent displayContent4 = this.mSupervisor.mRootWindowContainer.getDisplayContent(activityOptions.getCallerDisplayId());
            if (displayContent4 != null) {
                modifierTaskDisplayAreaIfNeed2 = displayContent4.getDefaultTaskDisplayArea();
            }
        }
        if (modifierTaskDisplayAreaIfNeed2 == null) {
            modifierTaskDisplayAreaIfNeed2 = launchParams.mPreferredTaskDisplayArea;
        }
        if (modifierTaskDisplayAreaIfNeed2 != null && !this.mSupervisor.mService.mSupportsMultiDisplay && modifierTaskDisplayAreaIfNeed2.getDisplayId() != 0) {
            modifierTaskDisplayAreaIfNeed2 = this.mSupervisor.mRootWindowContainer.getDefaultTaskDisplayArea();
        }
        if (modifierTaskDisplayAreaIfNeed2 != null && activityRecord2.isActivityTypeHome() && !this.mSupervisor.mRootWindowContainer.canStartHomeOnDisplayArea(activityRecord2.info, modifierTaskDisplayAreaIfNeed2, false)) {
            modifierTaskDisplayAreaIfNeed2 = this.mSupervisor.mRootWindowContainer.getDefaultTaskDisplayArea();
        }
        TaskDisplayArea modifierTaskDisplayAreaIfNeed3 = this.mModifierExt.modifierTaskDisplayAreaIfNeed(this.mSupervisor, modifierTaskDisplayAreaIfNeed2, activityRecord2);
        return modifierTaskDisplayAreaIfNeed3 != null ? modifierTaskDisplayAreaIfNeed3 : getFallbackDisplayAreaForActivity(activityRecord2, request);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ TaskDisplayArea lambda$getPreferredLaunchTaskDisplayArea$1(int i, TaskDisplayArea taskDisplayArea) {
        if (taskDisplayArea.mFeatureId == i) {
            return taskDisplayArea;
        }
        return null;
    }

    private TaskDisplayArea getFallbackDisplayAreaForActivity(ActivityRecord activityRecord, ActivityStarter.Request request) {
        WindowProcessController processController = this.mSupervisor.mService.getProcessController(activityRecord.launchedFromPid, activityRecord.launchedFromUid);
        TaskDisplayArea topActivityDisplayArea = processController == null ? null : processController.getTopActivityDisplayArea();
        if (topActivityDisplayArea != null) {
            return topActivityDisplayArea;
        }
        WindowProcessController processController2 = this.mSupervisor.mService.getProcessController(activityRecord.getProcessName(), activityRecord.getUid());
        TaskDisplayArea modifierTaskDisplayAreaIfNeed = this.mModifierExt.modifierTaskDisplayAreaIfNeed(this.mSupervisor, processController2 == null ? null : processController2.getTopActivityDisplayArea(), activityRecord, true, (Task) null);
        if (modifierTaskDisplayAreaIfNeed != null) {
            return modifierTaskDisplayAreaIfNeed;
        }
        WindowProcessController processController3 = request == null ? null : this.mSupervisor.mService.getProcessController(request.realCallingPid, request.realCallingUid);
        TaskDisplayArea topActivityDisplayArea2 = processController3 != null ? processController3.getTopActivityDisplayArea() : null;
        return topActivityDisplayArea2 != null ? topActivityDisplayArea2 : this.mSupervisor.mRootWindowContainer.getDefaultTaskDisplayArea();
    }

    private boolean canInheritWindowingModeFromSource(DisplayContent displayContent, TaskDisplayArea taskDisplayArea, ActivityRecord activityRecord) {
        if (activityRecord == null || taskDisplayArea.inFreeformWindowingMode()) {
            return false;
        }
        int windowingMode = activityRecord.getWindowingMode();
        return (windowingMode == 1 || windowingMode == 5) && displayContent.getDisplayId() == activityRecord.getDisplayId();
    }

    private boolean canCalculateBoundsForFullscreenTask(TaskDisplayArea taskDisplayArea, int i) {
        return this.mSupervisor.mService.mSupportsFreeformWindowManagement && ((taskDisplayArea.getWindowingMode() == 1 && i == 0) || i == 1);
    }

    private boolean canApplyFreeformWindowPolicy(TaskDisplayArea taskDisplayArea, int i) {
        return this.mSupervisor.mService.mSupportsFreeformWindowManagement && ((taskDisplayArea.inFreeformWindowingMode() && i == 0) || i == 5);
    }

    private boolean canApplyPipWindowPolicy(int i) {
        return this.mSupervisor.mService.mSupportsPictureInPicture && i == 2;
    }

    private void getLayoutBounds(TaskDisplayArea taskDisplayArea, ActivityRecord activityRecord, ActivityInfo.WindowLayout windowLayout, Rect rect) {
        int i;
        int i2;
        int i3 = windowLayout.gravity;
        int i4 = i3 & 112;
        int i5 = i3 & 7;
        if (!windowLayout.hasSpecifiedSize() && i4 == 0 && i5 == 0) {
            rect.setEmpty();
            return;
        }
        Rect rect2 = this.mTmpStableBounds;
        taskDisplayArea.getStableRect(rect2);
        int width = rect2.width();
        int height = rect2.height();
        float f = 1.0f;
        if (!windowLayout.hasSpecifiedSize()) {
            if (!rect.isEmpty()) {
                i = rect.width();
                i2 = rect.height();
            } else {
                getTaskBounds(activityRecord, taskDisplayArea, windowLayout, 5, false, rect);
                i = rect.width();
                i2 = rect.height();
            }
        } else {
            i = windowLayout.width;
            if (i <= 0 || i >= width) {
                float f2 = windowLayout.widthFraction;
                i = (f2 <= 0.0f || f2 >= 1.0f) ? width : (int) (width * f2);
            }
            i2 = windowLayout.height;
            if (i2 <= 0 || i2 >= height) {
                float f3 = windowLayout.heightFraction;
                i2 = (f3 <= 0.0f || f3 >= 1.0f) ? height : (int) (height * f3);
            }
        }
        float f4 = i5 != 3 ? i5 != 5 ? 0.5f : 1.0f : 0.0f;
        if (i4 == 48) {
            f = 0.0f;
        } else if (i4 != 80) {
            f = 0.5f;
        }
        rect.set(0, 0, i, i2);
        rect.offset(rect2.left, rect2.top);
        rect.offset((int) (f4 * (width - i)), (int) (f * (height - i2)));
    }

    private boolean shouldLaunchUnresizableAppInFreeform(ActivityRecord activityRecord, TaskDisplayArea taskDisplayArea, ActivityOptions activityOptions) {
        if ((activityOptions == null || activityOptions.getLaunchWindowingMode() != 1) && activityRecord.supportsFreeformInDisplayArea(taskDisplayArea) && !activityRecord.isResizeable()) {
            int orientationFromBounds = orientationFromBounds(taskDisplayArea.getBounds());
            int resolveOrientation = resolveOrientation(activityRecord, taskDisplayArea, taskDisplayArea.getBounds());
            if (taskDisplayArea.getWindowingMode() == 5 && orientationFromBounds != resolveOrientation) {
                return true;
            }
        }
        return false;
    }

    private int resolveOrientation(ActivityRecord activityRecord) {
        int i = activityRecord.info.screenOrientation;
        if (i != 0) {
            if (i == 1) {
                return 1;
            }
            if (i != 11) {
                if (i == 12) {
                    return 1;
                }
                if (i != 14) {
                    switch (i) {
                        case 5:
                            break;
                        case 6:
                        case 8:
                            break;
                        case 7:
                        case 9:
                            return 1;
                        default:
                            return -1;
                    }
                }
                return 14;
            }
        }
        return 0;
    }

    private void cascadeBounds(Rect rect, TaskDisplayArea taskDisplayArea, Rect rect2) {
        rect2.set(rect);
        int i = (int) (((taskDisplayArea.getConfiguration().densityDpi / 160.0f) * 75.0f) + 0.5f);
        taskDisplayArea.getBounds(this.mTmpBounds);
        rect2.offset(Math.min(i, Math.max(0, this.mTmpBounds.right - rect.right)), Math.min(i, Math.max(0, this.mTmpBounds.bottom - rect.bottom)));
    }

    private void getTaskBounds(ActivityRecord activityRecord, TaskDisplayArea taskDisplayArea, ActivityInfo.WindowLayout windowLayout, int i, boolean z, Rect rect) {
        if (i == 5 || i == 1) {
            int resolveOrientation = resolveOrientation(activityRecord, taskDisplayArea, rect);
            if (resolveOrientation != 1 && resolveOrientation != 0) {
                throw new IllegalStateException("Orientation must be one of portrait or landscape, but it's " + ActivityInfo.screenOrientationToString(resolveOrientation));
            }
            taskDisplayArea.getStableRect(this.mTmpStableBounds);
            Size defaultFreeformSize = LaunchParamsUtil.getDefaultFreeformSize(activityRecord, taskDisplayArea, windowLayout, resolveOrientation, this.mTmpStableBounds);
            this.mTmpBounds.set(0, 0, defaultFreeformSize.getWidth(), defaultFreeformSize.getHeight());
            if (z || sizeMatches(rect, this.mTmpBounds)) {
                if (resolveOrientation != orientationFromBounds(rect)) {
                    LaunchParamsUtil.centerBounds(taskDisplayArea, rect.height(), rect.width(), rect);
                }
            } else {
                adjustBoundsToFitInDisplayArea(taskDisplayArea, windowLayout, this.mTmpBounds);
                rect.setEmpty();
                LaunchParamsUtil.centerBounds(taskDisplayArea, this.mTmpBounds.width(), this.mTmpBounds.height(), rect);
            }
            adjustBoundsToAvoidConflictInDisplayArea(taskDisplayArea, rect);
        }
    }

    private int resolveOrientation(ActivityRecord activityRecord, TaskDisplayArea taskDisplayArea, Rect rect) {
        int orientationFromBounds;
        int resolveOrientation = resolveOrientation(activityRecord);
        if (resolveOrientation == 14) {
            if (rect.isEmpty()) {
                orientationFromBounds = convertOrientationToScreenOrientation(taskDisplayArea.getConfiguration().orientation);
            } else {
                orientationFromBounds = orientationFromBounds(rect);
            }
            resolveOrientation = orientationFromBounds;
        }
        if (resolveOrientation == -1) {
            return rect.isEmpty() ? 1 : orientationFromBounds(rect);
        }
        return resolveOrientation;
    }

    private void adjustBoundsToFitInDisplayArea(TaskDisplayArea taskDisplayArea, ActivityInfo.WindowLayout windowLayout, Rect rect) {
        LaunchParamsUtil.adjustBoundsToFitInDisplayArea(taskDisplayArea, this.mSupervisor.mRootWindowContainer.getConfiguration().getLayoutDirection(), windowLayout, rect);
    }

    private void adjustBoundsToAvoidConflictInDisplayArea(TaskDisplayArea taskDisplayArea, Rect rect) {
        final ArrayList arrayList = new ArrayList();
        taskDisplayArea.forAllRootTasks(new Consumer() { // from class: com.android.server.wm.TaskLaunchParamsModifier$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TaskLaunchParamsModifier.lambda$adjustBoundsToAvoidConflictInDisplayArea$2(arrayList, (Task) obj);
            }
        }, false);
        adjustBoundsToAvoidConflict(taskDisplayArea.getBounds(), arrayList, rect);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$adjustBoundsToAvoidConflictInDisplayArea$2(List list, Task task) {
        if (task.inFreeformWindowingMode()) {
            for (int i = 0; i < task.getChildCount(); i++) {
                list.add(task.getChildAt(i).getBounds());
            }
        }
    }

    @VisibleForTesting
    void adjustBoundsToAvoidConflict(Rect rect, List<Rect> list, Rect rect2) {
        if (rect.contains(rect2) && boundsConflict(list, rect2)) {
            calculateCandidateShiftDirections(rect, rect2);
            for (int i : this.mTmpDirections) {
                if (i == 0) {
                    return;
                }
                this.mTmpBounds.set(rect2);
                while (boundsConflict(list, this.mTmpBounds) && rect.contains(this.mTmpBounds)) {
                    shiftBounds(i, rect, this.mTmpBounds);
                }
                if (!boundsConflict(list, this.mTmpBounds) && rect.contains(this.mTmpBounds)) {
                    rect2.set(this.mTmpBounds);
                    return;
                }
            }
        }
    }

    private void calculateCandidateShiftDirections(Rect rect, Rect rect2) {
        int i = 0;
        while (true) {
            int[] iArr = this.mTmpDirections;
            if (i >= iArr.length) {
                break;
            }
            iArr[i] = 0;
            i++;
        }
        int i2 = rect.left;
        int i3 = rect.right;
        int i4 = ((i2 * 2) + i3) / 3;
        int i5 = (i2 + (i3 * 2)) / 3;
        int centerX = rect2.centerX();
        if (centerX < i4) {
            this.mTmpDirections[0] = 5;
            return;
        }
        if (centerX > i5) {
            this.mTmpDirections[0] = 3;
            return;
        }
        int i6 = rect.top;
        int i7 = rect.bottom;
        int i8 = ((i6 * 2) + i7) / 3;
        int i9 = (i6 + (i7 * 2)) / 3;
        int centerY = rect2.centerY();
        if (centerY < i8 || centerY > i9) {
            int[] iArr2 = this.mTmpDirections;
            iArr2[0] = 5;
            iArr2[1] = 3;
        } else {
            int[] iArr3 = this.mTmpDirections;
            iArr3[0] = 85;
            iArr3[1] = 51;
        }
    }

    private boolean boundsConflict(List<Rect> list, Rect rect) {
        Iterator<Rect> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                return false;
            }
            Rect next = it.next();
            boolean z = Math.abs(next.left - rect.left) < 4;
            boolean z2 = Math.abs(next.top - rect.top) < 4;
            boolean z3 = Math.abs(next.right - rect.right) < 4;
            boolean z4 = Math.abs(next.bottom - rect.bottom) < 4;
            if ((!z || !z2) && ((!z || !z4) && ((!z3 || !z2) && (!z3 || !z4)))) {
            }
        }
        return true;
    }

    private void shiftBounds(int i, Rect rect, Rect rect2) {
        int i2;
        int i3 = i & 7;
        int i4 = 0;
        if (i3 == 3) {
            i2 = -Math.max(1, rect.width() / 16);
        } else {
            i2 = i3 != 5 ? 0 : Math.max(1, rect.width() / 16);
        }
        int i5 = i & 112;
        if (i5 == 48) {
            i4 = -Math.max(1, rect.height() / 16);
        } else if (i5 == 80) {
            i4 = Math.max(1, rect.height() / 16);
        }
        rect2.offset(i2, i4);
    }

    private static int orientationFromBounds(Rect rect) {
        return rect.width() > rect.height() ? 0 : 1;
    }

    private static boolean sizeMatches(Rect rect, Rect rect2) {
        return Math.abs(rect2.width() - rect.width()) < 2 && Math.abs(rect2.height() - rect.height()) < 2;
    }
}
