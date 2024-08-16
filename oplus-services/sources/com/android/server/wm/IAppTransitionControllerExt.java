package com.android.server.wm;

import android.util.ArraySet;
import java.util.ArrayList;
import java.util.LinkedList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IAppTransitionControllerExt {
    public static final int TRANSIT_OLD_PUTT_TASK = 100;

    default boolean addSiblingToAnimationTargets(WindowContainer windowContainer, int i, boolean z) {
        return false;
    }

    default boolean applyAnimationForLauncherIfNeed(DisplayContent displayContent, ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2, int i) {
        return false;
    }

    default boolean applyAnimations(ArraySet<WindowContainer> arraySet, ArraySet<ActivityRecord> arraySet2) {
        return false;
    }

    default boolean applyAnimations(ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2, int i) {
        return false;
    }

    default boolean changeCanPromote(boolean z, boolean z2, WindowContainer windowContainer, ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2) {
        return false;
    }

    default void collectWcs(ArraySet<WindowContainer> arraySet, ArraySet<WindowContainer> arraySet2) {
    }

    default boolean getParallelWindowAnimationTargets(WindowContainer windowContainer, WindowContainer windowContainer2, ArrayList<WindowContainer> arrayList, LinkedList<WindowContainer> linkedList, boolean z, int i, boolean z2) {
        return false;
    }

    default void handleAppTransitionReady(int i) {
    }

    default boolean isAllInSplitOpening(ArraySet<? extends WindowContainer> arraySet) {
        return true;
    }

    default boolean isCompactWindowingMode(int i) {
        return false;
    }

    default boolean isDragToSplitState() {
        return false;
    }

    default boolean isGoodToGoWhenEnterCompactWindowApp(ActivityRecord activityRecord) {
        return false;
    }

    default boolean isGoodToGoWhenStartTasks(ActivityRecord activityRecord) {
        return true;
    }

    default boolean isNeedApplyAnimationForLauncher() {
        return false;
    }

    default boolean isNeedIgnoreFlexibleSplitStartupAnimation(ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2) {
        return false;
    }

    default boolean isNeedOverrideWithTaskFragmentRemoteAnimation(Task task) {
        return true;
    }

    default boolean isPrimaryActivityCloseInCompactWindow(ArraySet<ActivityRecord> arraySet) {
        return false;
    }

    default boolean isTransferStartingWindow(ActivityRecord activityRecord) {
        return false;
    }

    default void onAppTransitionReady(DisplayContent displayContent) {
    }

    default void overrideTaskFragmentAnimationIfNeed(DisplayContent displayContent, Task task, ActivityRecord activityRecord) {
    }

    default boolean overrideWithRemoteAnimationIfNeed(DisplayContent displayContent, int i, ArraySet<Integer> arraySet, ActivityRecord activityRecord) {
        return false;
    }

    default boolean overrideWithSplitScreenRemoteAnimationIfNeed(DisplayContent displayContent, int i, ArraySet<Integer> arraySet) {
        return false;
    }

    default boolean shouldDoPuttTransition(ArraySet<ActivityRecord> arraySet) {
        return false;
    }

    default boolean skipAppTransitionAnimation() {
        return false;
    }

    default boolean skipCheckOtherAncestors(boolean z, WindowContainer windowContainer, WindowContainer windowContainer2, ArraySet<ActivityRecord> arraySet) {
        return false;
    }

    default void startKeyguardExitOnKeyguardIfNeeded(int i, int i2, DisplayContent displayContent) {
    }

    default boolean transitionGoodToGo(ActivityRecord activityRecord, ArraySet<ActivityRecord> arraySet) {
        return false;
    }
}
