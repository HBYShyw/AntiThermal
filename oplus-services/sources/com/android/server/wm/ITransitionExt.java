package com.android.server.wm;

import android.util.ArrayMap;
import android.util.ArraySet;
import android.view.SurfaceControl;
import android.window.RemoteTransition;
import android.window.TransitionInfo;
import android.window.WindowContainerTransaction;
import com.android.server.wm.Transition;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ITransitionExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface IStaticExt {
        default boolean canPromote(Transition.ChangeInfo changeInfo, ArrayMap<WindowContainer, Transition.ChangeInfo> arrayMap) {
            return true;
        }

        default boolean dontPromoteWhenReparent(Transition.ChangeInfo changeInfo, ArrayMap<WindowContainer, Transition.ChangeInfo> arrayMap) {
            return true;
        }

        default void filterAnimTargetIfNeed(ArrayList<Transition.ChangeInfo> arrayList, ArrayMap<WindowContainer, Transition.ChangeInfo> arrayMap) {
        }

        default boolean forceSeamlesslyRotated(DisplayContent displayContent, String str) {
            return false;
        }

        default void setWindowCropForTransitionIfNeed(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, WindowContainer windowContainer) {
        }

        default boolean skipCurrentOrAdjustChange(int i, int i2, TransitionInfo.Change change, Transition.ChangeInfo changeInfo, ArrayList<Transition.ChangeInfo> arrayList, WindowContainer windowContainer) {
            return false;
        }

        default void updateAnimTargetIfNeed(ArraySet<WindowContainer> arraySet, ArrayList<Transition.ChangeInfo> arrayList, ArrayMap<WindowContainer, Transition.ChangeInfo> arrayMap) {
        }
    }

    default void addFlag(int i) {
    }

    default void addHandledInfo(WindowContainerTransaction windowContainerTransaction) {
    }

    default boolean addTaskToTransientHideTasks(Task task, Task task2) {
        return false;
    }

    default int adjustLayerZOrder(WindowContainer windowContainer, int i) {
        return i;
    }

    default void buildFinishTransaction(SurfaceControl.Transaction transaction, TransitionInfo transitionInfo, WindowContainer windowContainer, SurfaceControl surfaceControl) {
    }

    default boolean checkIfNeedRecordSnapshot(int i) {
        return true;
    }

    default void finishTransition(Transition transition, ArraySet<WindowContainer> arraySet) {
    }

    default SurfaceControl.Transaction fixFinishTransaction(SurfaceControl.Transaction transaction, TransitionInfo transitionInfo) {
        return transaction;
    }

    default int fixTransitType(int i, TransitionController transitionController) {
        return i;
    }

    default boolean forceVisibleAtTransitionEnd(Transition transition, ActivityRecord activityRecord) {
        return false;
    }

    default int getFeedBackFlags() {
        return 0;
    }

    default void hideDeferredWallpapersIfNeeded(WindowContainer<?> windowContainer, ActivityRecord activityRecord, TransitionController transitionController) {
    }

    default void hookSetBinderUxFlag(int i, int i2) {
    }

    default boolean isRemoteTransitionRequested() {
        return false;
    }

    default void onTransactionReady(Transition transition, ArrayList<Transition.ChangeInfo> arrayList, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction) {
    }

    default void recordTaskSnapShot(TaskSnapshotController taskSnapshotController, Task task) {
    }

    default void setAnimThreadUxIfNeed(boolean z) {
    }

    default void setRemoteTransitionRequested(RemoteTransition remoteTransition) {
    }

    default boolean shouldSkipCollect(Transition transition, WindowContainer windowContainer, ArrayMap<WindowContainer, Transition.ChangeInfo> arrayMap, ArraySet<WindowContainer> arraySet) {
        return false;
    }

    default int updateFlag(int i, DisplayContent displayContent) {
        return i;
    }
}
