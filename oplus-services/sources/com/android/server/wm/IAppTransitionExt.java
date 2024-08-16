package com.android.server.wm;

import android.content.Context;
import android.os.Handler;
import android.util.ArraySet;
import android.view.WindowManager;
import android.view.animation.Animation;
import com.android.internal.policy.TransitionAnimation;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IAppTransitionExt {
    default void appTransitionTimeout(WindowManagerService windowManagerService, DisplayContent displayContent) {
    }

    default Animation checkAndLoadCustomAnimation(String str, int i, boolean z, int i2) {
        return null;
    }

    default Animation createHiddenByKeyguardExit(int i, boolean z, int i2, boolean z2) {
        return null;
    }

    default void hookgoodToGo(DisplayContent displayContent, int i) {
    }

    default boolean isKeyguardGoingAwayTransit(WindowManagerService windowManagerService, DisplayContent displayContent, int i, ArrayList<Integer> arrayList) {
        return false;
    }

    default Animation loadCompactWindowAnimation(WindowManager.LayoutParams layoutParams, int i, boolean z, WindowContainer windowContainer) {
        return null;
    }

    default Animation loadCustomZoomAnimation(int i, WindowContainer windowContainer, Animation animation) {
        return animation;
    }

    default Animation loadFlexibleActivityTransitionAnimation(int i, boolean z, WindowContainer windowContainer, ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2) {
        return null;
    }

    default Animation loadFlexibleTaskTransitionAnimation(int i, boolean z, WindowContainer windowContainer, ArraySet<ActivityRecord> arraySet, ArraySet<ActivityRecord> arraySet2) {
        return null;
    }

    default Animation loadKeyguardUnoccludeAnimation(WindowContainer windowContainer) {
        return null;
    }

    default Animation loadOnePuttTransitionAnimation(int i, boolean z, WindowContainer windowContainer) {
        return null;
    }

    default Animation loadOplusStyleAnimation(WindowManager.LayoutParams layoutParams, int i, boolean z) {
        return null;
    }

    default Animation loadTransitCustomCompactWindowAnimation(WindowManager.LayoutParams layoutParams, int i, boolean z, WindowContainer windowContainer) {
        return null;
    }

    default void postAppTransitionDelayedCallback(Handler handler, int i, RemoteAnimationController remoteAnimationController, DisplayContent displayContent) {
    }

    default void removeAppTransitionDelayedCallback(Handler handler) {
    }

    default Animation updateAnimationForZoom(int i, WindowContainer windowContainer, Animation animation) {
        return animation;
    }

    default void validateKeyguardOcclusion(DisplayContent displayContent) {
    }

    default Animation hookloadAnimationSafely(Context context, boolean z, int i, String str, String str2) {
        return TransitionAnimation.loadAnimationSafely(context, i, str2);
    }

    default boolean canCustomizeAppTransition(WindowManager.LayoutParams layoutParams, int i, boolean z, WindowContainer windowContainer, String str) {
        return windowContainer.canCustomizeAppTransition();
    }
}
