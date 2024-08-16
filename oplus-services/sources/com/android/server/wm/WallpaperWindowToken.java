package com.android.server.wm;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.animation.Animation;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import java.util.function.Consumer;
import vendor.oplus.hardware.charger.ChargerErrorCode;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WallpaperWindowToken extends WindowToken {
    private static final String TAG = "WindowManager";
    private boolean mShowWhenLocked;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public WallpaperWindowToken asWallpaperToken() {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean fillsParent() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public boolean onChildVisibleRequestedChanged(WindowContainer windowContainer) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean showWallpaper() {
        return false;
    }

    WallpaperWindowToken(WindowManagerService windowManagerService, IBinder iBinder, boolean z, DisplayContent displayContent, boolean z2) {
        this(windowManagerService, iBinder, z, displayContent, z2, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WallpaperWindowToken(WindowManagerService windowManagerService, IBinder iBinder, boolean z, DisplayContent displayContent, boolean z2, Bundle bundle) {
        super(windowManagerService, iBinder, 2013, z, displayContent, z2, false, false, bundle);
        this.mShowWhenLocked = false;
        displayContent.mWallpaperController.addWallpaperToken(this);
        setWindowingMode(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowToken
    public void setExiting(boolean z) {
        super.setExiting(z);
        this.mDisplayContent.mWallpaperController.removeWallpaperToken(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setShowWhenLocked(boolean z) {
        boolean z2 = this.mDisplayContent.mWallpaperController.mIsLockscreenLiveWallpaperEnabled;
        if (!z2) {
            this.mShowWhenLocked = true;
        } else {
            if (z == this.mShowWhenLocked) {
                return;
            }
            this.mShowWhenLocked = z;
            if (z2) {
                getParent().positionChildAt(z ? ChargerErrorCode.ERR_FILE_FAILURE_ACCESS : Integer.MAX_VALUE, this, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canShowWhenLocked() {
        return this.mShowWhenLocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendWindowWallpaperCommand(String str, int i, int i2, int i3, Bundle bundle, boolean z) {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            try {
                ((WindowState) this.mChildren.get(size)).mClient.dispatchWallpaperCommand(str, i, i2, i3, bundle, z);
                z = false;
            } catch (RemoteException unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateWallpaperOffset(boolean z) {
        WallpaperController wallpaperController = this.mDisplayContent.mWallpaperController;
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            if (wallpaperController.updateWallpaperOffset((WindowState) this.mChildren.get(size), z)) {
                z = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startAnimation(Animation animation) {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            ((WindowState) this.mChildren.get(size)).startAnimation(animation);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateWallpaperWindowsInTransition(boolean z) {
        if (this.mTransitionController.isCollecting() && this.mVisibleRequested != z) {
            this.waitingToShow = true;
        }
        updateWallpaperWindows(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateWallpaperWindows(boolean z) {
        if (this.mVisibleRequested != z || isVisibleRequested() != z) {
            if (ProtoLogCache.WM_DEBUG_WALLPAPER_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_WALLPAPER, 733466617, 12, (String) null, new Object[]{String.valueOf(this.token), Boolean.valueOf(z)});
            }
            setVisibility(z);
        }
        WindowState wallpaperTarget = this.mDisplayContent.mWallpaperController.getWallpaperTarget();
        if (z && wallpaperTarget != null) {
            RecentsAnimationController recentsAnimationController = this.mWmService.getRecentsAnimationController();
            if (recentsAnimationController != null && recentsAnimationController.isAnimatingTask(wallpaperTarget.getTask())) {
                recentsAnimationController.linkFixedRotationTransformIfNeeded(this);
            } else {
                ActivityRecord activityRecord = wallpaperTarget.mActivityRecord;
                if ((activityRecord == null || activityRecord.isVisibleRequested()) && wallpaperTarget.mToken.hasFixedRotationTransform()) {
                    linkFixedRotationTransform(wallpaperTarget.mToken);
                }
            }
        }
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            return;
        }
        setVisible(z);
    }

    private void setVisible(boolean z) {
        boolean isClientVisible = isClientVisible();
        setClientVisible(z);
        if (!z || isClientVisible) {
            return;
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            ((WindowState) this.mChildren.get(size)).requestUpdateWallpaperIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setVisibility(boolean z) {
        if (this.mVisibleRequested != z) {
            this.mTransitionController.collect(this);
            setVisibleRequested(z);
        }
        if ((z || !(this.mTransitionController.inTransition() || getDisplayContent().mAppTransition.isRunning())) && !this.mTransitionController.mTransitionControllerExt.skipUpdateWallpaperVisibility(z, getDisplayContent())) {
            commitVisibility(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void commitVisibility(boolean z) {
        if (z == isVisible() || this.waitingToShow) {
            return;
        }
        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 3593205, 60, (String) null, new Object[]{String.valueOf(this), Boolean.valueOf(isVisible()), Boolean.valueOf(this.mVisibleRequested)});
        }
        setVisibleRequested(z);
        setVisible(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasVisibleNotDrawnWallpaper() {
        if (!isVisible()) {
            return false;
        }
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            WindowState windowState = (WindowState) this.mChildren.get(size);
            if (!windowState.isDrawn() && windowState.isVisible()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void forAllWallpaperWindows(Consumer<WallpaperWindowToken> consumer) {
        consumer.accept(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public boolean setVisibleRequested(boolean z) {
        if (!super.setVisibleRequested(z)) {
            return false;
        }
        setInsetsFrozen(!z);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean isVisible() {
        return isClientVisible();
    }

    @Override // com.android.server.wm.WindowToken
    public String toString() {
        if (this.stringName == null) {
            this.stringName = "WallpaperWindowToken{" + Integer.toHexString(System.identityHashCode(this)) + " token=" + this.token + '}';
        }
        return this.stringName;
    }
}
