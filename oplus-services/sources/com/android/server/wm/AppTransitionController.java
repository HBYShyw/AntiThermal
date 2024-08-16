package com.android.server.wm;

import android.graphics.Rect;
import android.os.Trace;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Pair;
import android.util.Slog;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationDefinition;
import android.view.WindowManager;
import android.window.ITaskFragmentOrganizer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;
import vendor.oplus.hardware.charger.ChargerErrorCode;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class AppTransitionController {
    private static final String TAG = "WindowManager";
    private static final int TYPE_ACTIVITY = 1;
    private static final int TYPE_NONE = 0;
    private static final int TYPE_TASK = 3;
    private static final int TYPE_TASK_FRAGMENT = 2;
    public static IAppTransitionControllerExt sAppTransControllerExt = (IAppTransitionControllerExt) ExtLoader.type(IAppTransitionControllerExt.class).create();
    private final DisplayContent mDisplayContent;
    private final WindowManagerService mService;
    private final WallpaperController mWallpaperControllerLocked;
    private RemoteAnimationDefinition mRemoteAnimationDefinition = null;
    private final ArrayMap<WindowContainer, Integer> mTempTransitionReasons = new ArrayMap<>();
    private final ArrayList<WindowContainer> mTempTransitionWindows = new ArrayList<>();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface TransitContainerType {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppTransitionController(WindowManagerService windowManagerService, DisplayContent displayContent) {
        this.mService = windowManagerService;
        this.mDisplayContent = displayContent;
        this.mWallpaperControllerLocked = displayContent.mWallpaperController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerRemoteAnimations(RemoteAnimationDefinition remoteAnimationDefinition) {
        this.mRemoteAnimationDefinition = remoteAnimationDefinition;
    }

    private WindowState getOldWallpaper() {
        WindowState wallpaperTarget = this.mWallpaperControllerLocked.getWallpaperTarget();
        int firstAppTransition = this.mDisplayContent.mAppTransition.getFirstAppTransition();
        DisplayContent displayContent = this.mDisplayContent;
        boolean z = true;
        ArraySet<WindowContainer> animationTargets = getAnimationTargets(displayContent.mOpeningApps, displayContent.mClosingApps, true);
        if (wallpaperTarget == null || (!wallpaperTarget.hasWallpaper() && ((firstAppTransition != 1 && firstAppTransition != 3) || animationTargets.isEmpty() || animationTargets.valueAt(0).asTask() == null || !this.mWallpaperControllerLocked.isWallpaperVisible()))) {
            z = false;
        }
        if (this.mWallpaperControllerLocked.isWallpaperTargetAnimating() || !z) {
            return null;
        }
        return wallpaperTarget;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleAppTransitionReady() {
        ArraySet<ActivityRecord> arraySet;
        ArraySet<ActivityRecord> arraySet2;
        this.mTempTransitionReasons.clear();
        if (transitionGoodToGo(this.mDisplayContent.mOpeningApps, this.mTempTransitionReasons) && transitionGoodToGo(this.mDisplayContent.mChangingContainers, this.mTempTransitionReasons) && transitionGoodToGoForTaskFragments()) {
            if (!this.mDisplayContent.mOpeningApps.stream().anyMatch(new Predicate() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda6
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((ActivityRecord) obj).isActivityTypeRecents();
                }
            })) {
                ArraySet arraySet3 = new ArraySet();
                arraySet3.addAll((ArraySet) this.mDisplayContent.mOpeningApps);
                arraySet3.addAll((ArraySet) this.mDisplayContent.mChangingContainers);
                int i = 0;
                boolean z = false;
                while (true) {
                    if (i >= arraySet3.size()) {
                        break;
                    }
                    ActivityRecord appFromContainer = getAppFromContainer((WindowContainer) arraySet3.valueAt(i));
                    if (appFromContainer != null) {
                        if (!appFromContainer.isAnimating(2, 8)) {
                            z = false;
                            break;
                        }
                        z = true;
                    }
                    i++;
                }
                if (z) {
                    if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 323235828, 0, (String) null, (Object[]) null);
                        return;
                    }
                    return;
                }
            }
            Trace.traceBegin(32L, "AppTransitionReady");
            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 255692476, 0, (String) null, (Object[]) null);
            }
            this.mDisplayContent.forAllWindows(new Consumer() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((WindowState) obj).cleanupAnimatingExitWindow();
                }
            }, true);
            sAppTransControllerExt.onAppTransitionReady(this.mDisplayContent);
            DisplayContent displayContent = this.mDisplayContent;
            AppTransition appTransition = displayContent.mAppTransition;
            displayContent.mNoAnimationNotifyOnTransitionFinished.clear();
            appTransition.removeAppTransitionTimeoutCallbacks();
            DisplayContent displayContent2 = this.mDisplayContent;
            displayContent2.mWallpaperMayChange = false;
            int size = displayContent2.mOpeningApps.size();
            for (int i2 = 0; i2 < size; i2++) {
                ((ActivityRecord) this.mDisplayContent.mOpeningApps.valueAtUnchecked(i2)).clearAnimatingFlags();
            }
            int size2 = this.mDisplayContent.mChangingContainers.size();
            for (int i3 = 0; i3 < size2; i3++) {
                ActivityRecord appFromContainer2 = getAppFromContainer((WindowContainer) this.mDisplayContent.mChangingContainers.valueAtUnchecked(i3));
                if (appFromContainer2 != null) {
                    appFromContainer2.clearAnimatingFlags();
                }
            }
            this.mWallpaperControllerLocked.adjustWallpaperWindowsForAppTransitionIfNeeded(this.mDisplayContent.mOpeningApps);
            DisplayContent displayContent3 = this.mDisplayContent;
            ArraySet<ActivityRecord> arraySet4 = displayContent3.mOpeningApps;
            ArraySet<ActivityRecord> arraySet5 = displayContent3.mClosingApps;
            if (displayContent3.mAtmService.mBackNavigationController.isMonitoringTransition()) {
                arraySet = new ArraySet<>(this.mDisplayContent.mOpeningApps);
                ArraySet<ActivityRecord> arraySet6 = new ArraySet<>(this.mDisplayContent.mClosingApps);
                if (this.mDisplayContent.mAtmService.mBackNavigationController.removeIfContainsBackAnimationTargets(arraySet, arraySet6)) {
                    this.mDisplayContent.mAtmService.mBackNavigationController.clearBackAnimations();
                }
                arraySet2 = arraySet6;
            } else {
                arraySet = arraySet4;
                arraySet2 = arraySet5;
            }
            DisplayContent displayContent4 = this.mDisplayContent;
            int transitCompatType = getTransitCompatType(displayContent4.mAppTransition, arraySet, arraySet2, displayContent4.mChangingContainers, this.mWallpaperControllerLocked.getWallpaperTarget(), getOldWallpaper(), this.mDisplayContent.mSkipAppTransitionAnimation);
            if (!sAppTransControllerExt.skipAppTransitionAnimation()) {
                this.mDisplayContent.mSkipAppTransitionAnimation = false;
            }
            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -240296576, 1, (String) null, new Object[]{Long.valueOf(this.mDisplayContent.mDisplayId), String.valueOf(appTransition.toString()), String.valueOf(arraySet), String.valueOf(arraySet2), String.valueOf(AppTransition.appTransitionOldToString(transitCompatType))});
            }
            ArraySet<Integer> collectActivityTypes = collectActivityTypes(arraySet, arraySet2, this.mDisplayContent.mChangingContainers);
            ArraySet<ActivityRecord> arraySet7 = arraySet2;
            ActivityRecord findAnimLayoutParamsToken = findAnimLayoutParamsToken(transitCompatType, collectActivityTypes, arraySet, arraySet2, this.mDisplayContent.mChangingContainers);
            ActivityRecord topApp = getTopApp(arraySet, false);
            ActivityRecord topApp2 = getTopApp(arraySet7, false);
            ActivityRecord topApp3 = getTopApp(this.mDisplayContent.mChangingContainers, false);
            WindowManager.LayoutParams animLp = getAnimLp(findAnimLayoutParamsToken);
            if (!sAppTransControllerExt.overrideWithRemoteAnimationIfNeed(this.mDisplayContent, transitCompatType, collectActivityTypes, topApp2) && !overrideWithTaskFragmentRemoteAnimation(transitCompatType, collectActivityTypes, findAnimLayoutParamsToken)) {
                unfreezeEmbeddedChangingWindows();
                overrideWithRemoteAnimationIfSet(findAnimLayoutParamsToken, transitCompatType, collectActivityTypes);
            }
            boolean z2 = containsVoiceInteraction(this.mDisplayContent.mClosingApps) || containsVoiceInteraction(this.mDisplayContent.mOpeningApps);
            this.mService.mSurfaceAnimationRunner.deferStartingAnimations();
            try {
                applyAnimations(arraySet, arraySet7, transitCompatType, animLp, z2);
                handleClosingApps();
                handleOpeningApps();
                handleChangingApps(transitCompatType);
                handleClosingChangingContainers();
                appTransition.setLastAppTransition(transitCompatType, topApp, topApp2, topApp3);
                appTransition.getTransitFlags();
                int goodToGo = appTransition.goodToGo(transitCompatType, topApp);
                appTransition.postAnimationCallback();
                sAppTransControllerExt.handleAppTransitionReady(transitCompatType);
                this.mService.mSnapshotController.onTransitionStarting(this.mDisplayContent);
                this.mDisplayContent.mOpeningApps.clear();
                this.mDisplayContent.mClosingApps.clear();
                this.mDisplayContent.mChangingContainers.clear();
                this.mDisplayContent.mUnknownAppVisibilityController.clear();
                this.mDisplayContent.mClosingChangingContainers.clear();
                this.mDisplayContent.setLayoutNeeded();
                this.mDisplayContent.computeImeTarget(true);
                this.mService.mAtmService.mTaskSupervisor.getActivityMetricsLogger().notifyTransitionStarting(this.mTempTransitionReasons);
                Trace.traceEnd(32L);
                DisplayContent displayContent5 = this.mDisplayContent;
                displayContent5.pendingLayoutChanges = goodToGo | 1 | 2 | displayContent5.pendingLayoutChanges;
            } finally {
                appTransition.clear();
                this.mService.mSurfaceAnimationRunner.continueStartingAnimations();
                if (sAppTransControllerExt.skipAppTransitionAnimation()) {
                    this.mDisplayContent.mSkipAppTransitionAnimation = false;
                }
            }
        }
    }

    static int getTransitCompatType(AppTransition appTransition, ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2, ArraySet<WindowContainer> arraySet3, WindowState windowState, WindowState windowState2, boolean z) {
        ActivityRecord topApp = getTopApp(arraySet, false);
        ActivityRecord topApp2 = getTopApp(arraySet2, true);
        boolean z2 = canBeWallpaperTarget(arraySet) && windowState != null;
        boolean z3 = canBeWallpaperTarget(arraySet2) && windowState != null;
        int keyguardTransition = appTransition.getKeyguardTransition();
        if (keyguardTransition == 7) {
            return z2 ? 21 : 20;
        }
        if (keyguardTransition == 8) {
            if (arraySet2.isEmpty()) {
                return (arraySet.isEmpty() || arraySet.valueAt(0).getActivityType() != 5) ? 22 : 33;
            }
            return 6;
        }
        if (keyguardTransition == 9) {
            return 23;
        }
        if (topApp != null && topApp.getActivityType() == 5) {
            return 31;
        }
        if (topApp2 != null && topApp2.getActivityType() == 5) {
            return 32;
        }
        if (z) {
            return -1;
        }
        int transitFlags = appTransition.getTransitFlags();
        int firstAppTransition = appTransition.getFirstAppTransition();
        if (appTransition.containsTransitRequest(6) && !arraySet3.isEmpty()) {
            int transitContainerType = getTransitContainerType(arraySet3.valueAt(0));
            if (transitContainerType == 2) {
                return 30;
            }
            if (transitContainerType == 3) {
                return 27;
            }
            throw new IllegalStateException("TRANSIT_CHANGE with unrecognized changing type=" + transitContainerType);
        }
        if ((transitFlags & 16) != 0) {
            return 26;
        }
        if (firstAppTransition == 0) {
            return 0;
        }
        if (AppTransition.isNormalTransit(firstAppTransition)) {
            boolean z4 = !arraySet.isEmpty();
            boolean z5 = true;
            for (int size = arraySet.size() - 1; size >= 0; size--) {
                ActivityRecord valueAt = arraySet.valueAt(size);
                if (!valueAt.isVisible()) {
                    if (valueAt.fillsParent()) {
                        z4 = false;
                        z5 = false;
                    } else {
                        z5 = false;
                    }
                }
            }
            boolean z6 = !arraySet2.isEmpty();
            int size2 = arraySet2.size() - 1;
            while (true) {
                if (size2 < 0) {
                    break;
                }
                if (arraySet2.valueAt(size2).fillsParent()) {
                    z6 = false;
                    break;
                }
                size2--;
            }
            if (z6 && z5) {
                return 25;
            }
            if (z4 && arraySet2.isEmpty()) {
                return 24;
            }
        }
        if (sAppTransControllerExt.shouldDoPuttTransition(arraySet)) {
            return 100;
        }
        if (z3 && z2) {
            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 100936473, 0, (String) null, (Object[]) null);
            }
            if (firstAppTransition == 1) {
                return 14;
            }
            if (firstAppTransition == 2) {
                return 15;
            }
            if (firstAppTransition == 3) {
                return 14;
            }
            if (firstAppTransition == 4) {
                return 15;
            }
        } else {
            if (windowState2 != null && !arraySet.isEmpty() && !arraySet.contains(windowState2.mActivityRecord) && arraySet2.contains(windowState2.mActivityRecord) && topApp2 == windowState2.mActivityRecord) {
                return 12;
            }
            if (windowState != null && windowState.isVisible() && arraySet.contains(windowState.mActivityRecord) && topApp == windowState.mActivityRecord) {
                return 13;
            }
        }
        ArraySet<WindowContainer> animationTargets = getAnimationTargets(arraySet, arraySet2, true);
        ArraySet<WindowContainer> animationTargets2 = getAnimationTargets(arraySet, arraySet2, false);
        WindowContainer valueAt2 = !animationTargets.isEmpty() ? animationTargets.valueAt(0) : null;
        WindowContainer valueAt3 = animationTargets2.isEmpty() ? null : animationTargets2.valueAt(0);
        int transitContainerType2 = getTransitContainerType(valueAt2);
        int transitContainerType3 = getTransitContainerType(valueAt3);
        if (appTransition.containsTransitRequest(3) && transitContainerType2 == 3) {
            return (topApp == null || !topApp.isActivityTypeHome()) ? 10 : 11;
        }
        if (appTransition.containsTransitRequest(4) && transitContainerType3 == 3) {
            return 11;
        }
        if (appTransition.containsTransitRequest(1) && !sAppTransControllerExt.isPrimaryActivityCloseInCompactWindow(arraySet2)) {
            if (transitContainerType2 == 3) {
                return (appTransition.getTransitFlags() & 32) != 0 ? 16 : 8;
            }
            if (transitContainerType2 == 1) {
                return 6;
            }
            if (transitContainerType2 == 2) {
                return 28;
            }
        }
        if (appTransition.containsTransitRequest(2)) {
            if (transitContainerType3 == 3) {
                return 9;
            }
            if (transitContainerType3 == 2) {
                return 29;
            }
            if (transitContainerType3 == 1) {
                for (int size3 = arraySet2.size() - 1; size3 >= 0; size3--) {
                    if (arraySet2.valueAt(size3).visibleIgnoringKeyguard) {
                        return 7;
                    }
                }
                return -1;
            }
        }
        return (!appTransition.containsTransitRequest(5) || animationTargets.isEmpty() || arraySet.isEmpty()) ? 0 : 18;
    }

    private static int getTransitContainerType(WindowContainer<?> windowContainer) {
        if (windowContainer == null) {
            return 0;
        }
        if (windowContainer.asTask() != null) {
            return 3;
        }
        if (windowContainer.asTaskFragment() != null) {
            return 2;
        }
        return windowContainer.asActivityRecord() != null ? 1 : 0;
    }

    private static WindowManager.LayoutParams getAnimLp(ActivityRecord activityRecord) {
        WindowState findMainWindow = activityRecord != null ? activityRecord.findMainWindow() : null;
        if (findMainWindow != null) {
            return findMainWindow.mAttrs;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteAnimationAdapter getRemoteAnimationOverride(WindowContainer windowContainer, int i, ArraySet<Integer> arraySet) {
        RemoteAnimationDefinition remoteAnimationDefinition;
        if (windowContainer != null && (remoteAnimationDefinition = windowContainer.getRemoteAnimationDefinition()) != null) {
            RemoteAnimationAdapter adapter = remoteAnimationDefinition.getAdapter(i, arraySet);
            if (adapter != null && AppTransition.isKeyguardOccludeTransitOld(i)) {
                Slog.e(TAG, "getRemoteAnimationOverride skip container RemoteAnimation,transit=" + i + ",container=" + windowContainer);
            } else if (adapter != null) {
                return adapter;
            }
        }
        RemoteAnimationDefinition remoteAnimationDefinition2 = this.mRemoteAnimationDefinition;
        if (remoteAnimationDefinition2 != null) {
            return remoteAnimationDefinition2.getAdapter(i, arraySet);
        }
        return null;
    }

    private void unfreezeEmbeddedChangingWindows() {
        ArraySet<WindowContainer> arraySet = this.mDisplayContent.mChangingContainers;
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            WindowContainer valueAt = arraySet.valueAt(size);
            if (valueAt.isEmbedded()) {
                valueAt.mSurfaceFreezer.unfreeze(valueAt.getSyncTransaction());
            }
        }
    }

    private boolean transitionMayContainNonAppWindows(int i) {
        return NonAppWindowAnimationAdapter.shouldStartNonAppWindowAnimationsForKeyguardExit(i) || NonAppWindowAnimationAdapter.shouldAttachNavBarToApp(this.mService, this.mDisplayContent, i) || WallpaperAnimationAdapter.shouldStartWallpaperAnimation(this.mDisplayContent);
    }

    private boolean transitionContainsTaskFragmentWithBoundsOverride() {
        boolean z = true;
        for (int size = this.mDisplayContent.mChangingContainers.size() - 1; size >= 0; size--) {
            if (this.mDisplayContent.mChangingContainers.valueAt(size).isEmbedded()) {
                return true;
            }
        }
        this.mTempTransitionWindows.clear();
        this.mTempTransitionWindows.addAll(this.mDisplayContent.mClosingApps);
        this.mTempTransitionWindows.addAll(this.mDisplayContent.mOpeningApps);
        int size2 = this.mTempTransitionWindows.size() - 1;
        while (true) {
            if (size2 < 0) {
                z = false;
                break;
            }
            TaskFragment taskFragment = this.mTempTransitionWindows.get(size2).asActivityRecord().getTaskFragment();
            if (taskFragment != null && taskFragment.isEmbeddedWithBoundsOverride()) {
                break;
            }
            size2--;
        }
        this.mTempTransitionWindows.clear();
        return z;
    }

    private Task findParentTaskForAllEmbeddedWindows() {
        Task task;
        this.mTempTransitionWindows.clear();
        this.mTempTransitionWindows.addAll(this.mDisplayContent.mClosingApps);
        this.mTempTransitionWindows.addAll(this.mDisplayContent.mOpeningApps);
        this.mTempTransitionWindows.addAll(this.mDisplayContent.mChangingContainers);
        int size = this.mTempTransitionWindows.size() - 1;
        Task task2 = null;
        Task task3 = null;
        while (true) {
            if (size >= 0) {
                ActivityRecord appFromContainer = getAppFromContainer(this.mTempTransitionWindows.get(size));
                if (appFromContainer == null || (task = appFromContainer.getTask()) == null || task.inPinnedWindowingMode() || ((task3 != null && task3 != task) || task.getRootActivity() == null || (appFromContainer.getUid() != task.effectiveUid && !appFromContainer.isEmbedded()))) {
                    break;
                }
                size--;
                task3 = task;
            } else {
                task2 = task3;
                break;
            }
        }
        this.mTempTransitionWindows.clear();
        return task2;
    }

    private ITaskFragmentOrganizer findTaskFragmentOrganizer(Task task) {
        if (task == null) {
            return null;
        }
        final ITaskFragmentOrganizer[] iTaskFragmentOrganizerArr = new ITaskFragmentOrganizer[1];
        if (!task.forAllLeafTaskFragments(new Predicate() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$findTaskFragmentOrganizer$0;
                lambda$findTaskFragmentOrganizer$0 = AppTransitionController.lambda$findTaskFragmentOrganizer$0(iTaskFragmentOrganizerArr, (TaskFragment) obj);
                return lambda$findTaskFragmentOrganizer$0;
            }
        })) {
            return iTaskFragmentOrganizerArr[0];
        }
        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
            ProtoLogImpl.e(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 1805116444, 0, (String) null, (Object[]) null);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$findTaskFragmentOrganizer$0(ITaskFragmentOrganizer[] iTaskFragmentOrganizerArr, TaskFragment taskFragment) {
        ITaskFragmentOrganizer taskFragmentOrganizer = taskFragment.getTaskFragmentOrganizer();
        if (taskFragmentOrganizer == null) {
            return false;
        }
        ITaskFragmentOrganizer iTaskFragmentOrganizer = iTaskFragmentOrganizerArr[0];
        if (iTaskFragmentOrganizer != null && !iTaskFragmentOrganizer.asBinder().equals(taskFragmentOrganizer.asBinder())) {
            return true;
        }
        iTaskFragmentOrganizerArr[0] = taskFragmentOrganizer;
        return false;
    }

    private boolean overrideWithTaskFragmentRemoteAnimation(int i, ArraySet<Integer> arraySet, ActivityRecord activityRecord) {
        if (transitionMayContainNonAppWindows(i) || !transitionContainsTaskFragmentWithBoundsOverride()) {
            return false;
        }
        final Task findParentTaskForAllEmbeddedWindows = findParentTaskForAllEmbeddedWindows();
        ITaskFragmentOrganizer findTaskFragmentOrganizer = findTaskFragmentOrganizer(findParentTaskForAllEmbeddedWindows);
        RemoteAnimationDefinition remoteAnimationDefinition = findTaskFragmentOrganizer != null ? this.mDisplayContent.mAtmService.mTaskFragmentOrganizerController.getRemoteAnimationDefinition(findTaskFragmentOrganizer) : null;
        RemoteAnimationAdapter adapter = remoteAnimationDefinition != null ? remoteAnimationDefinition.getAdapter(i, arraySet) : null;
        if (adapter == null) {
            return false;
        }
        if (AppTransition.isKeyguardOccludeTransitOld(i)) {
            Slog.e(TAG, "Override with TaskFragment remote animation for transit=" + AppTransition.appTransitionOldToString(i) + ",task=" + findParentTaskForAllEmbeddedWindows);
        }
        this.mDisplayContent.mAppTransition.overridePendingAppTransitionRemote(adapter, false, true);
        sAppTransControllerExt.overrideTaskFragmentAnimationIfNeed(this.mDisplayContent, findParentTaskForAllEmbeddedWindows, activityRecord);
        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -702650156, 0, (String) null, new Object[]{String.valueOf(AppTransition.appTransitionOldToString(i))});
        }
        boolean z = !findParentTaskForAllEmbeddedWindows.isFullyTrustedEmbedding(this.mDisplayContent.mAtmService.mTaskFragmentOrganizerController.getTaskFragmentOrganizerUid(findTaskFragmentOrganizer));
        RemoteAnimationController remoteAnimationController = this.mDisplayContent.mAppTransition.getRemoteAnimationController();
        if (z && remoteAnimationController != null) {
            remoteAnimationController.setOnRemoteAnimationReady(new Runnable() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    AppTransitionController.lambda$overrideWithTaskFragmentRemoteAnimation$2(Task.this);
                }
            });
            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 2100457473, 1, (String) null, new Object[]{Long.valueOf(findParentTaskForAllEmbeddedWindows.mTaskId)});
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$overrideWithTaskFragmentRemoteAnimation$2(Task task) {
        task.forAllActivities(new Consumer() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ActivityRecord) obj).setDropInputForAnimation(true);
            }
        });
    }

    private void overrideWithRemoteAnimationIfSet(ActivityRecord activityRecord, int i, ArraySet<Integer> arraySet) {
        if (AppTransition.isKeyguardOccludeTransitOld(i) && this.mDisplayContent.mAppTransition.getRemoteAnimationController() != null) {
            Slog.e(TAG, "overrideWithRemoteAnimationIfSet RemoteAnimationController not null !! ,transit=" + AppTransition.appTransitionOldToString(i));
        }
        RemoteAnimationAdapter remoteAnimationAdapter = null;
        if (i != 26) {
            if (AppTransition.isKeyguardGoingAwayTransitOld(i)) {
                RemoteAnimationDefinition remoteAnimationDefinition = this.mRemoteAnimationDefinition;
                if (remoteAnimationDefinition != null) {
                    remoteAnimationAdapter = remoteAnimationDefinition.getAdapter(i, arraySet);
                }
            } else if (this.mDisplayContent.mAppTransition.getRemoteAnimationController() == null) {
                remoteAnimationAdapter = getRemoteAnimationOverride(activityRecord, i, arraySet);
            }
        }
        if (remoteAnimationAdapter != null) {
            this.mDisplayContent.mAppTransition.overridePendingAppTransitionRemote(remoteAnimationAdapter);
        }
    }

    static Task findRootTaskFromContainer(WindowContainer windowContainer) {
        return windowContainer.asTaskFragment() != null ? windowContainer.asTaskFragment().getRootTask() : windowContainer.asActivityRecord().getRootTask();
    }

    static ActivityRecord getAppFromContainer(WindowContainer windowContainer) {
        return windowContainer.asTaskFragment() != null ? windowContainer.asTaskFragment().getTopNonFinishingActivity() : windowContainer.asActivityRecord();
    }

    private ActivityRecord findAnimLayoutParamsToken(final int i, final ArraySet<Integer> arraySet, ArraySet<ActivityRecord> arraySet2, ArraySet<ActivityRecord> arraySet3, ArraySet<WindowContainer> arraySet4) {
        ActivityRecord lookForHighestTokenWithFilter = lookForHighestTokenWithFilter(arraySet3, arraySet2, arraySet4, new Predicate() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$findAnimLayoutParamsToken$3;
                lambda$findAnimLayoutParamsToken$3 = AppTransitionController.lambda$findAnimLayoutParamsToken$3(i, arraySet, (ActivityRecord) obj);
                return lambda$findAnimLayoutParamsToken$3;
            }
        });
        if (lookForHighestTokenWithFilter != null) {
            return lookForHighestTokenWithFilter;
        }
        ActivityRecord lookForHighestTokenWithFilter2 = lookForHighestTokenWithFilter(arraySet3, arraySet2, arraySet4, new Predicate() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$findAnimLayoutParamsToken$4;
                lambda$findAnimLayoutParamsToken$4 = AppTransitionController.lambda$findAnimLayoutParamsToken$4((ActivityRecord) obj);
                return lambda$findAnimLayoutParamsToken$4;
            }
        });
        return lookForHighestTokenWithFilter2 != null ? lookForHighestTokenWithFilter2 : lookForHighestTokenWithFilter(arraySet3, arraySet2, arraySet4, new Predicate() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$findAnimLayoutParamsToken$5;
                lambda$findAnimLayoutParamsToken$5 = AppTransitionController.lambda$findAnimLayoutParamsToken$5((ActivityRecord) obj);
                return lambda$findAnimLayoutParamsToken$5;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$findAnimLayoutParamsToken$3(int i, ArraySet arraySet, ActivityRecord activityRecord) {
        return activityRecord.getRemoteAnimationDefinition() != null && activityRecord.getRemoteAnimationDefinition().hasTransition(i, arraySet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$findAnimLayoutParamsToken$4(ActivityRecord activityRecord) {
        return activityRecord.fillsParent() && activityRecord.findMainWindow() != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$findAnimLayoutParamsToken$5(ActivityRecord activityRecord) {
        return activityRecord.findMainWindow() != null;
    }

    private static ArraySet<Integer> collectActivityTypes(ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2, ArraySet<WindowContainer> arraySet3) {
        ArraySet<Integer> arraySet4 = new ArraySet<>();
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            arraySet4.add(Integer.valueOf(arraySet.valueAt(size).getActivityType()));
        }
        for (int size2 = arraySet2.size() - 1; size2 >= 0; size2--) {
            arraySet4.add(Integer.valueOf(arraySet2.valueAt(size2).getActivityType()));
        }
        for (int size3 = arraySet3.size() - 1; size3 >= 0; size3--) {
            arraySet4.add(Integer.valueOf(arraySet3.valueAt(size3).getActivityType()));
        }
        return arraySet4;
    }

    private static ActivityRecord lookForHighestTokenWithFilter(ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2, ArraySet<WindowContainer> arraySet3, Predicate<ActivityRecord> predicate) {
        WindowContainer valueAt;
        int size = arraySet.size();
        int size2 = arraySet2.size() + size;
        int size3 = arraySet3.size() + size2;
        int i = ChargerErrorCode.ERR_FILE_FAILURE_ACCESS;
        ActivityRecord activityRecord = null;
        for (int i2 = 0; i2 < size3; i2++) {
            if (i2 < size) {
                valueAt = arraySet.valueAt(i2);
            } else if (i2 < size2) {
                valueAt = arraySet2.valueAt(i2 - size);
            } else {
                valueAt = arraySet3.valueAt(i2 - size2);
            }
            int prefixOrderIndex = valueAt.getPrefixOrderIndex();
            ActivityRecord appFromContainer = getAppFromContainer(valueAt);
            if (appFromContainer != null && predicate.test(appFromContainer) && prefixOrderIndex > i) {
                activityRecord = appFromContainer;
                i = prefixOrderIndex;
            }
        }
        return activityRecord;
    }

    private boolean containsVoiceInteraction(ArraySet<ActivityRecord> arraySet) {
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            if (arraySet.valueAt(size).mVoiceInteraction) {
                return true;
            }
        }
        return false;
    }

    private void applyAnimations(ArraySet<WindowContainer> arraySet, ArraySet<ActivityRecord> arraySet2, int i, boolean z, WindowManager.LayoutParams layoutParams, boolean z2) {
        int size = arraySet.size();
        for (int i2 = 0; i2 < size; i2++) {
            WindowContainer valueAt = arraySet.valueAt(i2);
            ArrayList<WindowContainer> arrayList = new ArrayList<>();
            for (int i3 = 0; i3 < arraySet2.size(); i3++) {
                ActivityRecord valueAt2 = arraySet2.valueAt(i3);
                if (valueAt2.isDescendantOf(valueAt)) {
                    arrayList.add(valueAt2);
                }
            }
            valueAt.applyAnimation(layoutParams, i, z, z2, arrayList);
        }
    }

    @VisibleForTesting
    static ArraySet<WindowContainer> getAnimationTargets(ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2, boolean z) {
        return getAnimationTargets(arraySet, arraySet2, z, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isTaskViewTask(WindowContainer windowContainer) {
        if ((windowContainer instanceof Task) && ((Task) windowContainer).mRemoveWithTaskOrganizer) {
            return true;
        }
        WindowContainer parent = windowContainer.getParent();
        return parent != null && (parent instanceof Task) && ((Task) parent).mRemoveWithTaskOrganizer;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @VisibleForTesting
    static ArraySet<WindowContainer> getAnimationTargets(ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2, boolean z, int i) {
        Object obj;
        ArraySet arraySet3;
        ArraySet<ActivityRecord> arraySet4;
        int i2;
        WindowContainer windowContainer;
        WindowContainer windowContainer2;
        ArraySet<WindowContainer> arraySet5;
        LinkedList<WindowContainer> linkedList;
        ArrayList<WindowContainer> arrayList;
        ArraySet<ActivityRecord> arraySet6;
        int i3;
        ArraySet<WindowContainer> arraySet7;
        ArraySet<ActivityRecord> arraySet8;
        WindowContainer windowContainer3;
        ArrayList<WindowContainer> arrayList2;
        LinkedList<WindowContainer> linkedList2 = new LinkedList<>();
        ArraySet<ActivityRecord> arraySet9 = z ? arraySet : arraySet2;
        int i4 = 0;
        int i5 = 0;
        while (true) {
            obj = null;
            if (i5 >= arraySet9.size()) {
                break;
            }
            ActivityRecord valueAt = arraySet9.valueAt(i5);
            if (valueAt.shouldApplyAnimation(z)) {
                linkedList2.add(valueAt);
                if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 1967975839, 60, (String) null, new Object[]{String.valueOf(valueAt), Boolean.valueOf(valueAt.isVisible()), Boolean.FALSE});
                }
            }
            i5++;
        }
        ArraySet<ActivityRecord> arraySet10 = z ? arraySet2 : arraySet;
        ArraySet arraySet11 = new ArraySet();
        for (int i6 = 0; i6 < arraySet10.size(); i6++) {
            for (ActivityRecord valueAt2 = arraySet10.valueAt(i6); valueAt2 != null; valueAt2 = valueAt2.getParent()) {
                arraySet11.add(valueAt2);
            }
        }
        ArraySet<WindowContainer> arraySet12 = new ArraySet<>();
        ArrayList<WindowContainer> arrayList3 = new ArrayList<>();
        if (z && sAppTransControllerExt.isNeedApplyAnimationForLauncher()) {
            Slog.d(TAG, "getAnimationTargets abort for openingApps:" + arraySet);
            return arraySet12;
        }
        if (sAppTransControllerExt.isNeedIgnoreFlexibleSplitStartupAnimation(arraySet, arraySet2)) {
            Slog.d(TAG, "getAnimationTargets abort for FlexibleSplitStartup:" + arraySet);
            return arraySet12;
        }
        while (!linkedList2.isEmpty()) {
            WindowContainer removeFirst = linkedList2.removeFirst();
            WindowContainer parent = removeFirst.getParent();
            arrayList3.clear();
            arrayList3.add(removeFirst);
            if (!isTaskViewTask(removeFirst)) {
                if (parent == null || !parent.canCreateRemoteAnimationTarget() || ((removeFirst.asTask() != null && removeFirst.asTask().mInRemoveTask) || parent.isChangingAppTransition())) {
                    arraySet3 = arraySet11;
                    arraySet4 = arraySet10;
                    i2 = i4;
                    windowContainer = parent;
                    windowContainer2 = removeFirst;
                    arraySet5 = arraySet12;
                    linkedList = linkedList2;
                    arrayList = arrayList3;
                    arraySet6 = arraySet9;
                    i3 = i2;
                } else {
                    int i7 = (removeFirst.asTask() == null || removeFirst.asTask().getAdjacentTask() == null) ? (sAppTransControllerExt.skipCheckOtherAncestors(z, removeFirst, parent, arraySet10) || !arraySet11.contains(parent)) ? 1 : i4 : i4;
                    if (sAppTransControllerExt.isCompactWindowingMode(removeFirst.getWindowingMode())) {
                        windowContainer3 = parent;
                        windowContainer2 = removeFirst;
                        arraySet8 = arraySet9;
                        arrayList2 = arrayList3;
                        arraySet5 = arraySet12;
                        i7 = sAppTransControllerExt.getParallelWindowAnimationTargets(removeFirst, parent, arrayList3, linkedList2, z, i, i7) ? 1 : 0;
                    } else {
                        arraySet8 = arraySet9;
                        windowContainer3 = parent;
                        windowContainer2 = removeFirst;
                        arrayList2 = arrayList3;
                        arraySet5 = arraySet12;
                        int i8 = i4;
                        while (i8 < windowContainer3.getChildCount()) {
                            WindowContainer windowContainer4 = windowContainer3;
                            WindowContainer childAt = windowContainer4.getChildAt(i8);
                            if (linkedList2.remove(childAt)) {
                                if (!isTaskViewTask(childAt)) {
                                    arrayList2.add(childAt);
                                }
                            } else if (childAt != windowContainer2 && childAt.isVisible()) {
                                if (sAppTransControllerExt.addSiblingToAnimationTargets(childAt, i, z)) {
                                    arrayList2.add(childAt);
                                }
                                i7 = i4;
                                i8++;
                                windowContainer3 = windowContainer4;
                            }
                            i8++;
                            windowContainer3 = windowContainer4;
                        }
                    }
                    windowContainer = windowContainer3;
                    arraySet3 = arraySet11;
                    arraySet4 = arraySet10;
                    i2 = i4;
                    arrayList = arrayList2;
                    arraySet6 = arraySet8;
                    linkedList = linkedList2;
                    i3 = sAppTransControllerExt.changeCanPromote(i7, z, windowContainer2, arraySet, arraySet2);
                }
                if (i3 != 0) {
                    Task asTask = windowContainer.asTask();
                    if (asTask == null || !asTask.getWrapper().getExtImpl().getLaunchedFromMultiSearch()) {
                        arraySet7 = arraySet5;
                        if (windowContainer instanceof Task) {
                            arraySet7.add(windowContainer);
                        } else {
                            linkedList.add(windowContainer);
                        }
                    } else if (z) {
                        arraySet10 = arraySet4;
                        arraySet11 = arraySet3;
                        linkedList2 = linkedList;
                        i4 = i2;
                        arraySet9 = arraySet6;
                        arrayList3 = arrayList;
                        arraySet12 = arraySet5;
                        obj = null;
                    } else {
                        arraySet7 = arraySet5;
                        arraySet7.add(windowContainer2);
                    }
                } else {
                    arraySet7 = arraySet5;
                    arraySet7.addAll(arrayList);
                }
                arraySet10 = arraySet4;
                linkedList2 = linkedList;
                i4 = i2;
                arraySet9 = arraySet6;
                arrayList3 = arrayList;
                obj = null;
                arraySet12 = arraySet7;
                arraySet11 = arraySet3;
            }
        }
        int i9 = i4;
        ArraySet<ActivityRecord> arraySet13 = arraySet9;
        ArraySet<WindowContainer> arraySet14 = arraySet12;
        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 1460759282, i9, (String) null, new Object[]{String.valueOf(arraySet13), String.valueOf(arraySet14)});
        }
        return arraySet14;
    }

    private void applyAnimations(ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2, int i, WindowManager.LayoutParams layoutParams, boolean z) {
        RecentsAnimationController recentsAnimationController = this.mService.getRecentsAnimationController();
        if (i == -1 || (arraySet.isEmpty() && arraySet2.isEmpty())) {
            if (recentsAnimationController != null) {
                recentsAnimationController.sendTasksAppeared();
                return;
            }
            return;
        }
        if (AppTransition.isActivityTransitOld(i)) {
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < arraySet2.size(); i2++) {
                ActivityRecord valueAt = arraySet2.valueAt(i2);
                if (valueAt.areBoundsLetterboxed()) {
                    arrayList.add(new Pair(valueAt, valueAt.getLetterboxInsets()));
                }
            }
            for (int i3 = 0; i3 < arraySet.size(); i3++) {
                ActivityRecord valueAt2 = arraySet.valueAt(i3);
                if (valueAt2.areBoundsLetterboxed()) {
                    Rect letterboxInsets = valueAt2.getLetterboxInsets();
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        Pair pair = (Pair) it.next();
                        if (letterboxInsets.equals((Rect) pair.second)) {
                            ActivityRecord activityRecord = (ActivityRecord) pair.first;
                            valueAt2.setNeedsLetterboxedAnimation(true);
                            activityRecord.setNeedsLetterboxedAnimation(true);
                        }
                    }
                }
            }
        }
        ArraySet<WindowContainer> animationTargets = getAnimationTargets(arraySet, arraySet2, true, i);
        ArraySet<WindowContainer> animationTargets2 = getAnimationTargets(arraySet, arraySet2, false, i);
        sAppTransControllerExt.collectWcs(animationTargets, animationTargets2);
        applyAnimations(animationTargets, arraySet, i, true, layoutParams, z);
        if (sAppTransControllerExt.applyAnimations(animationTargets, arraySet)) {
            return;
        }
        applyAnimations(animationTargets2, arraySet2, i, false, layoutParams, z);
        if (recentsAnimationController != null) {
            recentsAnimationController.sendTasksAppeared();
        }
        for (int i4 = 0; i4 < arraySet.size(); i4++) {
            ((ActivityRecord) arraySet.valueAtUnchecked(i4)).mOverrideTaskTransition = false;
        }
        for (int i5 = 0; i5 < arraySet2.size(); i5++) {
            ((ActivityRecord) arraySet2.valueAtUnchecked(i5)).mOverrideTaskTransition = false;
        }
        AccessibilityController accessibilityController = this.mDisplayContent.mWmService.mAccessibilityController;
        if (accessibilityController.hasCallbacks()) {
            accessibilityController.onAppWindowTransition(this.mDisplayContent.getDisplayId(), i);
        }
    }

    private void handleOpeningApps() {
        ArraySet<ActivityRecord> arraySet = this.mDisplayContent.mOpeningApps;
        int size = arraySet.size();
        for (int i = 0; i < size; i++) {
            if (i >= arraySet.size()) {
                Slog.d(TAG, "handleOpeningApps IndexOutOfBoundsE this = " + this);
                return;
            }
            ActivityRecord valueAt = arraySet.valueAt(i);
            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 1628345525, 0, (String) null, new Object[]{String.valueOf(valueAt)});
            }
            valueAt.commitVisibility(true, false);
            WindowContainer animatingContainer = valueAt.getAnimatingContainer(2, 1);
            if (animatingContainer == null || !animatingContainer.getAnimationSources().contains(valueAt)) {
                this.mDisplayContent.mNoAnimationNotifyOnTransitionFinished.add(valueAt.token);
            }
            valueAt.updateReportedVisibilityLocked();
            valueAt.waitingToShow = false;
            if (WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
                Slog.i(TAG, ">>> OPEN TRANSACTION handleAppTransitionReady()");
            }
            this.mService.openSurfaceTransaction();
            try {
                valueAt.showAllWindowsLocked();
                if (this.mDisplayContent.mAppTransition.isNextAppTransitionThumbnailUp()) {
                    valueAt.attachThumbnailAnimation();
                } else if (this.mDisplayContent.mAppTransition.isNextAppTransitionOpenCrossProfileApps()) {
                    valueAt.attachCrossProfileAppsThumbnailAnimation();
                }
            } finally {
                this.mService.closeSurfaceTransaction("handleAppTransitionReady");
                if (WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
                    Slog.i(TAG, "<<< CLOSE TRANSACTION handleAppTransitionReady()");
                }
            }
        }
    }

    private void handleClosingApps() {
        ArraySet<ActivityRecord> arraySet = this.mDisplayContent.mClosingApps;
        int size = arraySet.size();
        for (int i = 0; i < size; i++) {
            if (i >= arraySet.size()) {
                Slog.d(TAG, "handleClosingApps IndexOutOfBoundsE this = " + this);
                return;
            }
            ActivityRecord valueAt = arraySet.valueAt(i);
            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 794570322, 0, (String) null, new Object[]{String.valueOf(valueAt)});
            }
            valueAt.commitVisibility(false, false);
            valueAt.updateReportedVisibilityLocked();
            valueAt.allDrawn = true;
            WindowState windowState = valueAt.mStartingWindow;
            if (windowState != null && !windowState.mAnimatingExit) {
                valueAt.removeStartingWindow();
            }
            if (this.mDisplayContent.mAppTransition.isNextAppTransitionThumbnailDown()) {
                valueAt.attachThumbnailAnimation();
            }
        }
    }

    private void handleClosingChangingContainers() {
        ArrayMap<WindowContainer, Rect> arrayMap = this.mDisplayContent.mClosingChangingContainers;
        while (!arrayMap.isEmpty()) {
            WindowContainer keyAt = arrayMap.keyAt(0);
            arrayMap.remove(keyAt);
            TaskFragment asTaskFragment = keyAt.asTaskFragment();
            if (asTaskFragment != null) {
                asTaskFragment.updateOrganizedTaskFragmentSurface();
            }
        }
    }

    private void handleChangingApps(int i) {
        ArraySet<WindowContainer> arraySet = this.mDisplayContent.mChangingContainers;
        int size = arraySet.size();
        for (int i2 = 0; i2 < size; i2++) {
            WindowContainer valueAt = arraySet.valueAt(i2);
            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 186668272, 0, (String) null, new Object[]{String.valueOf(valueAt)});
            }
            valueAt.applyAnimation(null, i, true, false, null);
        }
    }

    private boolean transitionGoodToGo(ArraySet<? extends WindowContainer> arraySet, ArrayMap<WindowContainer, Integer> arrayMap) {
        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 2045641491, 61, (String) null, new Object[]{Long.valueOf(arraySet.size()), Boolean.valueOf(this.mService.mDisplayFrozen), Boolean.valueOf(this.mDisplayContent.mAppTransition.isTimeout())});
        }
        if (this.mDisplayContent.mAppTransition.isTimeout()) {
            return true;
        }
        if (!sAppTransControllerExt.isAllInSplitOpening(arraySet)) {
            return false;
        }
        ScreenRotationAnimation rotationAnimation = this.mService.mRoot.getDisplayContent(0).getRotationAnimation();
        if (rotationAnimation != null && rotationAnimation.isAnimating() && this.mDisplayContent.getDisplayRotation().needsUpdate()) {
            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 628276090, 0, (String) null, (Object[]) null);
            }
            return false;
        }
        for (int i = 0; i < arraySet.size(); i++) {
            ActivityRecord appFromContainer = getAppFromContainer(arraySet.valueAt(i));
            if (appFromContainer != null) {
                if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 289967521, 1020, (String) null, new Object[]{String.valueOf(appFromContainer), Boolean.valueOf(appFromContainer.allDrawn), Boolean.valueOf(appFromContainer.isStartingWindowDisplayed()), Boolean.valueOf(appFromContainer.startingMoved), Boolean.valueOf(appFromContainer.isRelaunching()), String.valueOf(appFromContainer.mStartingWindow)});
                }
                boolean z = appFromContainer.allDrawn && !appFromContainer.isRelaunching();
                if (sAppTransControllerExt.isTransferStartingWindow(appFromContainer)) {
                    return false;
                }
                if (!z && ((!appFromContainer.isStartingWindowDisplayed() || !sAppTransControllerExt.isGoodToGoWhenStartTasks(appFromContainer)) && !appFromContainer.startingMoved && !sAppTransControllerExt.transitionGoodToGo(appFromContainer, this.mDisplayContent.mOpeningApps) && !sAppTransControllerExt.isGoodToGoWhenEnterCompactWindowApp(appFromContainer))) {
                    return false;
                }
                if (z) {
                    arrayMap.put(appFromContainer, 2);
                } else {
                    arrayMap.put(appFromContainer, Integer.valueOf(appFromContainer.mStartingData instanceof SplashScreenStartingData ? 1 : 4));
                }
            }
        }
        if (this.mDisplayContent.mAppTransition.isFetchingAppTransitionsSpecs()) {
            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -1009117329, 0, (String) null, (Object[]) null);
            }
            return false;
        }
        if (this.mDisplayContent.mUnknownAppVisibilityController.allResolved()) {
            return !this.mWallpaperControllerLocked.isWallpaperVisible() || this.mWallpaperControllerLocked.wallpaperTransitionReady();
        }
        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -379068494, 0, (String) null, new Object[]{String.valueOf(this.mDisplayContent.mUnknownAppVisibilityController.getDebugMessage())});
        }
        return false;
    }

    private boolean transitionGoodToGoForTaskFragments() {
        if (this.mDisplayContent.mAppTransition.isTimeout()) {
            return true;
        }
        ArraySet arraySet = new ArraySet();
        for (int size = this.mDisplayContent.mOpeningApps.size() - 1; size >= 0; size--) {
            arraySet.add(this.mDisplayContent.mOpeningApps.valueAt(size).getRootTask());
        }
        for (int size2 = this.mDisplayContent.mClosingApps.size() - 1; size2 >= 0; size2--) {
            arraySet.add(this.mDisplayContent.mClosingApps.valueAt(size2).getRootTask());
        }
        for (int size3 = this.mDisplayContent.mChangingContainers.size() - 1; size3 >= 0; size3--) {
            arraySet.add(findRootTaskFromContainer(this.mDisplayContent.mChangingContainers.valueAt(size3)));
        }
        for (int size4 = arraySet.size() - 1; size4 >= 0; size4--) {
            Task task = (Task) arraySet.valueAt(size4);
            if (task != null && task.forAllLeafTaskFragments(new Predicate() { // from class: com.android.server.wm.AppTransitionController$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$transitionGoodToGoForTaskFragments$6;
                    lambda$transitionGoodToGoForTaskFragments$6 = AppTransitionController.lambda$transitionGoodToGoForTaskFragments$6((TaskFragment) obj);
                    return lambda$transitionGoodToGoForTaskFragments$6;
                }
            })) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$transitionGoodToGoForTaskFragments$6(TaskFragment taskFragment) {
        if (taskFragment.isReadyToTransit()) {
            return false;
        }
        if (!ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
            return true;
        }
        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -1501564055, 0, (String) null, new Object[]{String.valueOf(taskFragment)});
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isTransitWithinTask(int i, Task task) {
        if (task == null || !this.mDisplayContent.mChangingContainers.isEmpty()) {
            return false;
        }
        if (i != 6 && i != 7 && i != 18) {
            return false;
        }
        Iterator<ActivityRecord> it = this.mDisplayContent.mOpeningApps.iterator();
        while (it.hasNext()) {
            if (it.next().getTask() != task) {
                return false;
            }
        }
        Iterator<ActivityRecord> it2 = this.mDisplayContent.mClosingApps.iterator();
        while (it2.hasNext()) {
            if (it2.next().getTask() != task) {
                return false;
            }
        }
        return true;
    }

    private static boolean canBeWallpaperTarget(ArraySet<ActivityRecord> arraySet) {
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            if (arraySet.valueAt(size).windowsCanBeWallpaperTarget()) {
                return true;
            }
        }
        return false;
    }

    private static ActivityRecord getTopApp(ArraySet<? extends WindowContainer> arraySet, boolean z) {
        int prefixOrderIndex;
        int i = ChargerErrorCode.ERR_FILE_FAILURE_ACCESS;
        ActivityRecord activityRecord = null;
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            ActivityRecord appFromContainer = getAppFromContainer(arraySet.valueAt(size));
            if (appFromContainer != null && ((!z || appFromContainer.isVisible()) && (prefixOrderIndex = appFromContainer.getPrefixOrderIndex()) > i)) {
                activityRecord = appFromContainer;
                i = prefixOrderIndex;
            }
        }
        return activityRecord;
    }
}
