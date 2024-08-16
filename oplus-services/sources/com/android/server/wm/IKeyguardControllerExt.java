package com.android.server.wm;

import android.content.Context;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IKeyguardControllerExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface IStaticExt {
        default void setAppLayoutChanges(boolean z, boolean z2, DisplayContent displayContent, ActivityRecord activityRecord, int i) {
        }
    }

    default boolean checkKeyguardVisibility(ActivityRecord activityRecord, KeyguardController keyguardController) {
        return false;
    }

    default boolean dismissKeyguard(Context context, ActivityRecord activityRecord, boolean z) {
        return false;
    }

    default void enableOrientationListenerWhenKeyguradGoingAway(DisplayContent displayContent, int i) {
    }

    default int getKeyguardGoingAwayFlags() {
        return 0;
    }

    default boolean ifSkipTransition(int i) {
        return false;
    }

    default void keyguardGoingAway(int i) {
    }

    default boolean skipAcquireSleepToken(int i) {
        return false;
    }

    default boolean skipShowWallpaper(int i, RootWindowContainer rootWindowContainer) {
        return false;
    }

    default void updateKeyguardExitAnimStateIfNeeded(boolean z, int i) {
    }
}
