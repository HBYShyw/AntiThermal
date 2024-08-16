package com.android.server.wm;

import android.content.res.Configuration;
import android.view.WindowInsets;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class DisplayRotationImmersiveAppCompatPolicy {
    private final DisplayContent mDisplayContent;
    private final DisplayRotation mDisplayRotation;
    private final LetterboxConfiguration mLetterboxConfiguration;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DisplayRotationImmersiveAppCompatPolicy createIfNeeded(LetterboxConfiguration letterboxConfiguration, DisplayRotation displayRotation, DisplayContent displayContent) {
        if (letterboxConfiguration.isDisplayRotationImmersiveAppCompatPolicyEnabled(false)) {
            return new DisplayRotationImmersiveAppCompatPolicy(letterboxConfiguration, displayRotation, displayContent);
        }
        return null;
    }

    private DisplayRotationImmersiveAppCompatPolicy(LetterboxConfiguration letterboxConfiguration, DisplayRotation displayRotation, DisplayContent displayContent) {
        this.mDisplayRotation = displayRotation;
        this.mLetterboxConfiguration = letterboxConfiguration;
        this.mDisplayContent = displayContent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRotationLockEnforced(int i) {
        boolean isRotationLockEnforcedLocked;
        if (!this.mLetterboxConfiguration.isDisplayRotationImmersiveAppCompatPolicyEnabled(true)) {
            return false;
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mDisplayContent.mWmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                isRotationLockEnforcedLocked = isRotationLockEnforcedLocked(i);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
        return isRotationLockEnforcedLocked;
    }

    private boolean isRotationLockEnforcedLocked(int i) {
        ActivityRecord activityRecord;
        return (!this.mDisplayContent.getIgnoreOrientationRequest() || (activityRecord = this.mDisplayContent.topRunningActivity()) == null || !hasRequestedToHideStatusAndNavBars(activityRecord) || activityRecord.getTask() == null || activityRecord.getTask().getWindowingMode() != 1 || activityRecord.areBoundsLetterboxed() || activityRecord.getRequestedConfigurationOrientation() == 0 || activityRecord.getRequestedConfigurationOrientation() == surfaceRotationToConfigurationOrientation(i)) ? false : true;
    }

    private boolean hasRequestedToHideStatusAndNavBars(ActivityRecord activityRecord) {
        WindowState findMainWindow = activityRecord.findMainWindow();
        return findMainWindow != null && (findMainWindow.getRequestedVisibleTypes() & (WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars())) == 0;
    }

    @Configuration.Orientation
    private int surfaceRotationToConfigurationOrientation(int i) {
        if (this.mDisplayRotation.isAnyPortrait(i)) {
            return 1;
        }
        return this.mDisplayRotation.isLandscapeOrSeascape(i) ? 2 : 0;
    }
}
