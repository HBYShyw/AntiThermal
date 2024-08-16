package com.android.server.wm;

import android.content.ComponentName;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface ITransitionControllerExt {
    default void addNextAppTransitionRequests(int i) {
    }

    default boolean canAssignLayers(WindowContainer windowContainer) {
        return false;
    }

    default void finishTransition(Transition transition) {
    }

    default void hookSetBinderUxFlag(boolean z) {
    }

    default void initFoldScreenBlackCoverStrategy() {
    }

    default boolean isRemoteAnimationPlaying(boolean z) {
        return false;
    }

    default void notifySysWindowRotation(Class cls, ComponentName componentName, TransitionRequestInfo.DisplayChange displayChange) {
    }

    default void requestStartTransition(TransitionRequestInfo transitionRequestInfo, Transition transition) {
    }

    default void setOverrideAnimation(TransitionInfo.AnimationOptions animationOptions, Transition transition) {
    }

    default boolean skipRequestCloseTransitionIfNeeded(WindowContainer<?> windowContainer) {
        return false;
    }

    default boolean skipUpdateWallpaperVisibility(boolean z, DisplayContent displayContent) {
        return false;
    }

    default void validateKeyguardOcclusion(DisplayContent displayContent) {
    }

    default ArrayList<Integer> getNextAppTransitionRequests() {
        return new ArrayList<>();
    }
}
