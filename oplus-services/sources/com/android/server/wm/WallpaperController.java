package com.android.server.wm;

import android.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.ArraySet;
import android.util.MathUtils;
import android.util.Slog;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.SurfaceControl;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.window.ScreenCapture;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.ToBooleanFunction;
import com.android.server.wm.WindowManagerService;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;
import vendor.oplus.hardware.charger.ChargerErrorCode;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WallpaperController {
    private static final String TAG = "WindowManager";
    private static final int WALLPAPER_DRAW_NORMAL = 0;
    private static final int WALLPAPER_DRAW_PENDING = 1;
    private static final long WALLPAPER_DRAW_PENDING_TIMEOUT_DURATION = 500;
    private static final int WALLPAPER_DRAW_TIMEOUT = 2;
    private static final long WALLPAPER_TIMEOUT = 150;
    private static final long WALLPAPER_TIMEOUT_RECOVERY = 10000;
    private DisplayContent mDisplayContent;
    final boolean mIsLockscreenLiveWallpaperEnabled;
    private long mLastWallpaperTimeoutTime;
    private final float mMaxWallpaperScale;
    private WindowManagerService mService;
    private boolean mShouldOffsetWallpaperCenter;
    private boolean mShouldUpdateZoom;
    private WindowState mWaitingOnWallpaper;
    private final ArrayList<WallpaperWindowToken> mWallpaperTokens = new ArrayList<>();
    private WindowState mWallpaperTarget = null;
    private WindowState mPrevWallpaperTarget = null;
    private float mLastWallpaperX = -1.0f;
    private float mLastWallpaperY = -1.0f;
    private float mLastWallpaperXStep = -1.0f;
    private float mLastWallpaperYStep = -1.0f;
    private float mLastWallpaperZoomOut = 0.0f;
    private int mLastWallpaperDisplayOffsetX = ChargerErrorCode.ERR_FILE_FAILURE_ACCESS;
    private int mLastWallpaperDisplayOffsetY = ChargerErrorCode.ERR_FILE_FAILURE_ACCESS;
    private boolean mLastFrozen = false;
    private int mWallpaperDrawState = 0;
    private Point mLargestDisplaySize = null;
    public IWallpaperControllerExt mWallpaperControllerExt = (IWallpaperControllerExt) ExtLoader.type(IWallpaperControllerExt.class).base(this).create();
    private final FindWallpaperTargetResult mFindResults = new FindWallpaperTargetResult();
    private final Consumer<WindowState> mFindWallpapers = new Consumer() { // from class: com.android.server.wm.WallpaperController$$ExternalSyntheticLambda0
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            WallpaperController.this.lambda$new$0((WindowState) obj);
        }
    };
    private final ToBooleanFunction<WindowState> mFindWallpaperTargetFunction = new ToBooleanFunction() { // from class: com.android.server.wm.WallpaperController$$ExternalSyntheticLambda1
        public final boolean apply(Object obj) {
            boolean lambda$new$1;
            lambda$new$1 = WallpaperController.this.lambda$new$1((WindowState) obj);
            return lambda$new$1;
        }
    };
    private Consumer<WindowState> mComputeMaxZoomOutFunction = new Consumer() { // from class: com.android.server.wm.WallpaperController$$ExternalSyntheticLambda2
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            WallpaperController.this.lambda$new$2((WindowState) obj);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateWallpaperWindowsTarget$3(WindowState windowState, WindowState windowState2) {
        return windowState2 == windowState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(WindowState windowState) {
        if (windowState.mAttrs.type == 2013) {
            WallpaperWindowToken asWallpaperToken = windowState.mToken.asWallpaperToken();
            if (asWallpaperToken.canShowWhenLocked() && !this.mFindResults.hasTopShowWhenLockedWallpaper()) {
                this.mFindResults.setTopShowWhenLockedWallpaper(windowState);
            } else {
                if (asWallpaperToken.canShowWhenLocked() || this.mFindResults.hasTopHideWhenLockedWallpaper()) {
                    return;
                }
                this.mFindResults.setTopHideWhenLockedWallpaper(windowState);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$1(WindowState windowState) {
        ActivityRecord activityRecord;
        boolean isShellTransitionsEnabled = windowState.mTransitionController.isShellTransitionsEnabled();
        if (!isShellTransitionsEnabled) {
            ActivityRecord activityRecord2 = windowState.mActivityRecord;
            if (activityRecord2 != null && !activityRecord2.isVisible() && !windowState.mActivityRecord.isAnimating(3)) {
                if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                    Slog.v(TAG, "Skipping hidden and not animating token: " + windowState);
                }
                return false;
            }
        } else {
            ActivityRecord activityRecord3 = windowState.mActivityRecord;
            if (activityRecord3 != null && !activityRecord3.isVisibleRequested() && !activityRecord3.isVisible()) {
                return false;
            }
        }
        if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
            Slog.v(TAG, "Win " + windowState + ": isOnScreen=" + windowState.isOnScreen() + " mDrawState=" + windowState.mWinAnimator.mDrawState + " mSurfaceShown=" + windowState.mWinAnimator.getShown());
        }
        ActivityRecord activityRecord4 = windowState.mActivityRecord;
        WindowContainer animatingContainer = activityRecord4 != null ? activityRecord4.getAnimatingContainer() : null;
        if (!isShellTransitionsEnabled && animatingContainer != null && animatingContainer.isAnimating(3) && AppTransition.isKeyguardGoingAwayTransitOld(animatingContainer.mTransit) && (animatingContainer.mTransitFlags & 4) != 0) {
            this.mFindResults.setUseTopWallpaperAsTarget(true);
        }
        if (this.mService.mPolicy.isKeyguardLocked() && windowState.canShowWhenLocked() && (this.mService.mPolicy.isKeyguardOccluded() || (!isShellTransitionsEnabled ? this.mService.mPolicy.isKeyguardUnoccluding() : windowState.inTransition()))) {
            this.mFindResults.mNeedsShowWhenLockedWallpaper = (isFullscreen(windowState.mAttrs) && ((activityRecord = windowState.mActivityRecord) == null || activityRecord.fillsParent())) ? false : true;
        }
        boolean z = windowState.hasWallpaper() || (animatingContainer != null && animatingContainer.getAnimation() != null && animatingContainer.getAnimation().getShowWallpaper());
        if (isRecentsTransitionTarget(windowState) || isBackNavigationTarget(windowState)) {
            if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                Slog.v(TAG, "Found recents animation wallpaper target: " + windowState);
            }
            this.mFindResults.setWallpaperTarget(windowState);
            return true;
        }
        if (this.mService.getWrapper().getExtImpl().isGestureAnimationWapaperTarget(windowState)) {
            if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                Slog.v(TAG, "Found gesture animation wallpaper target: " + windowState);
            }
            this.mFindResults.setWallpaperTarget(windowState);
            return true;
        }
        if (!z || !windowState.isOnScreen()) {
            return false;
        }
        if (this.mWallpaperTarget != windowState && !windowState.isDrawFinishedLw()) {
            return false;
        }
        if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
            Slog.v(TAG, "Found wallpaper target: " + windowState);
        }
        this.mFindResults.setWallpaperTarget(windowState);
        if (windowState == this.mWallpaperTarget && windowState.isAnimating(3) && WindowManagerDebugConfig.DEBUG_WALLPAPER) {
            Slog.v(TAG, "Win " + windowState + ": token animating, looking behind.");
        }
        this.mFindResults.setIsWallpaperTargetForLetterbox(windowState.hasWallpaperForLetterboxBackground());
        return windowState.mActivityRecord != null;
    }

    private boolean isRecentsTransitionTarget(WindowState windowState) {
        if (windowState.mTransitionController.isShellTransitionsEnabled()) {
            return windowState.mActivityRecord != null && windowState.mAttrs.type == 1 && this.mDisplayContent.isKeyguardLocked() && windowState.mTransitionController.isTransientHide(windowState.getTask());
        }
        RecentsAnimationController recentsAnimationController = this.mService.getRecentsAnimationController();
        return recentsAnimationController != null && recentsAnimationController.isWallpaperVisible(windowState);
    }

    private boolean isBackNavigationTarget(WindowState windowState) {
        return this.mService.mAtmService.mBackNavigationController.isWallpaperVisible(windowState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(WindowState windowState) {
        if (windowState.mIsWallpaper || Float.compare(windowState.mWallpaperZoomOut, this.mLastWallpaperZoomOut) <= 0) {
            return;
        }
        this.mLastWallpaperZoomOut = windowState.mWallpaperZoomOut;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WallpaperController(WindowManagerService windowManagerService, DisplayContent displayContent) {
        this.mService = windowManagerService;
        this.mDisplayContent = displayContent;
        Resources resources = windowManagerService.mContext.getResources();
        this.mMaxWallpaperScale = resources.getFloat(R.dimen.datepicker_header_height);
        this.mShouldOffsetWallpaperCenter = resources.getBoolean(17891774);
        this.mWallpaperControllerExt.handleWallpaperCreated(displayContent);
        this.mIsLockscreenLiveWallpaperEnabled = SystemProperties.getBoolean("persist.wm.debug.lockscreen_live_wallpaper", false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetLargestDisplay(Display display) {
        if (display == null || display.getType() != 1) {
            return;
        }
        this.mLargestDisplaySize = null;
    }

    @VisibleForTesting
    void setShouldOffsetWallpaperCenter(boolean z) {
        this.mShouldOffsetWallpaperCenter = z;
    }

    private Point findLargestDisplaySize() {
        if (!this.mShouldOffsetWallpaperCenter) {
            return null;
        }
        Point point = new Point();
        List<DisplayInfo> possibleDisplayInfoLocked = this.mService.getPossibleDisplayInfoLocked(0);
        for (int i = 0; i < possibleDisplayInfoLocked.size(); i++) {
            DisplayInfo displayInfo = possibleDisplayInfoLocked.get(i);
            if (displayInfo.type == 1 && Math.max(displayInfo.logicalWidth, displayInfo.logicalHeight) > Math.max(point.x, point.y)) {
                point.set(displayInfo.logicalWidth, displayInfo.logicalHeight);
            }
        }
        return point;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState getWallpaperTarget() {
        return this.mWallpaperTarget;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isWallpaperTarget(WindowState windowState) {
        return windowState == this.mWallpaperTarget;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isBelowWallpaperTarget(WindowState windowState) {
        WindowState windowState2 = this.mWallpaperTarget;
        return windowState2 != null && windowState2.mLayer >= windowState.mBaseLayer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isWallpaperVisible() {
        for (int size = this.mWallpaperTokens.size() - 1; size >= 0; size--) {
            if (this.mWallpaperTokens.get(size).isVisible()) {
                return true;
            }
        }
        return false;
    }

    void startWallpaperAnimation(Animation animation) {
        for (int size = this.mWallpaperTokens.size() - 1; size >= 0; size--) {
            this.mWallpaperTokens.get(size).startAnimation(animation);
        }
    }

    private boolean shouldWallpaperBeVisible(WindowState windowState) {
        if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
            Slog.v(TAG, "Wallpaper vis: target " + windowState + " prev=" + this.mPrevWallpaperTarget);
        }
        return (windowState == null && this.mPrevWallpaperTarget == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isWallpaperTargetAnimating() {
        ActivityRecord activityRecord;
        WindowState windowState = this.mWallpaperTarget;
        return windowState != null && windowState.isAnimating(3) && ((activityRecord = this.mWallpaperTarget.mActivityRecord) == null || !activityRecord.isWaitingForTransitionStart());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateWallpaperVisibility() {
        boolean shouldWallpaperBeVisible = shouldWallpaperBeVisible(this.mWallpaperTarget);
        for (int size = this.mWallpaperTokens.size() - 1; size >= 0; size--) {
            this.mWallpaperTokens.get(size).setVisibility(shouldWallpaperBeVisible);
        }
    }

    public void showWallpaperInTransition(boolean z) {
        updateWallpaperWindowsTarget(this.mFindResults);
        if (!this.mFindResults.hasTopShowWhenLockedWallpaper()) {
            Slog.w(TAG, "There is no wallpaper for the lock screen");
            return;
        }
        FindWallpaperTargetResult findWallpaperTargetResult = this.mFindResults;
        FindWallpaperTargetResult.TopWallpaper topWallpaper = findWallpaperTargetResult.mTopWallpaper;
        WindowState windowState = topWallpaper.mTopHideWhenLockedWallpaper;
        WindowState windowState2 = topWallpaper.mTopShowWhenLockedWallpaper;
        if (!findWallpaperTargetResult.hasTopHideWhenLockedWallpaper()) {
            windowState2.mToken.asWallpaperToken().updateWallpaperWindows(this.mWallpaperTarget != null);
        } else {
            windowState.mToken.asWallpaperToken().updateWallpaperWindowsInTransition(z);
            windowState2.mToken.asWallpaperToken().updateWallpaperWindowsInTransition(!z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void hideDeferredWallpapersIfNeededLegacy() {
        for (int size = this.mWallpaperTokens.size() - 1; size >= 0; size--) {
            WallpaperWindowToken wallpaperWindowToken = this.mWallpaperTokens.get(size);
            if (!wallpaperWindowToken.isVisibleRequested()) {
                wallpaperWindowToken.commitVisibility(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void hideWallpapers(WindowState windowState) {
        WindowState windowState2 = this.mWallpaperTarget;
        if ((windowState2 != null && (windowState2 != windowState || this.mPrevWallpaperTarget != null)) || this.mWallpaperControllerExt.skipHideSecondaryWallpaper(this.mService, this.mDisplayContent) || this.mFindResults.useTopWallpaperAsTarget) {
            return;
        }
        for (int size = this.mWallpaperTokens.size() - 1; size >= 0; size--) {
            WallpaperWindowToken wallpaperWindowToken = this.mWallpaperTokens.get(size);
            wallpaperWindowToken.setVisibility(false);
            if (ProtoLogImpl.isEnabled(ProtoLogGroup.WM_DEBUG_WALLPAPER) && wallpaperWindowToken.isVisible() && ProtoLogCache.WM_DEBUG_WALLPAPER_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_WALLPAPER, 1984843251, 0, (String) null, new Object[]{String.valueOf(wallpaperWindowToken), String.valueOf(windowState), String.valueOf(this.mWallpaperTarget), String.valueOf(this.mPrevWallpaperTarget), String.valueOf(Debug.getCallers(5))});
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateWallpaperOffset(WindowState windowState, boolean z) {
        boolean z2;
        boolean z3;
        Rect parentFrame = windowState.getParentFrame();
        Rect frame = windowState.getFrame();
        int width = frame.width() - parentFrame.width();
        int height = frame.height() - parentFrame.height();
        if ((windowState.mAttrs.flags & 16384) != 0 && Math.abs(width) > 1 && Math.abs(height) > 1) {
            Slog.d(TAG, "Skip wallpaper offset with inconsistent orientation, bounds=" + parentFrame + " frame=" + frame);
            return false;
        }
        float f = windowState.isRtl() ? 1.0f : 0.0f;
        float f2 = this.mLastWallpaperX;
        if (f2 >= 0.0f) {
            f = f2;
        }
        float checkWallpaperOffsetX = this.mWallpaperControllerExt.checkWallpaperOffsetX(this.mService.mContext, windowState, z, f);
        float f3 = this.mLastWallpaperXStep;
        if (f3 < 0.0f) {
            f3 = -1.0f;
        }
        int displayWidthOffset = getDisplayWidthOffset(width, parentFrame, windowState.isRtl());
        int i = width - displayWidthOffset;
        int i2 = i > 0 ? -((int) ((i * checkWallpaperOffsetX) + 0.5f)) : 0;
        int i3 = this.mLastWallpaperDisplayOffsetX;
        if (i3 != Integer.MIN_VALUE) {
            i2 += i3;
        } else if (!windowState.isRtl()) {
            i2 -= displayWidthOffset;
        }
        if (windowState.mWallpaperX == checkWallpaperOffsetX && windowState.mWallpaperXStep == f3) {
            z2 = false;
        } else {
            windowState.mWallpaperX = checkWallpaperOffsetX;
            windowState.mWallpaperXStep = f3;
            z2 = true;
        }
        float f4 = this.mLastWallpaperY;
        if (f4 < 0.0f) {
            f4 = 0.5f;
        }
        float f5 = this.mLastWallpaperYStep;
        float f6 = f5 >= 0.0f ? f5 : -1.0f;
        int i4 = height > 0 ? -((int) ((height * f4) + 0.5f)) : 0;
        if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
            Slog.d(TAG, "updateWallpaperOffset diffHeight =" + height + ", offset = " + i4 + ", wallpaperFrame = " + frame + ", lastWallpaperBounds = " + parentFrame);
        }
        int i5 = this.mLastWallpaperDisplayOffsetY;
        if (i5 != Integer.MIN_VALUE) {
            i4 += i5;
        }
        if (windowState.mWallpaperY != f4 || windowState.mWallpaperYStep != f6) {
            windowState.mWallpaperY = f4;
            windowState.mWallpaperYStep = f6;
            z2 = true;
        }
        if (Float.compare(windowState.mWallpaperZoomOut, this.mLastWallpaperZoomOut) != 0) {
            windowState.mWallpaperZoomOut = this.mLastWallpaperZoomOut;
            z3 = true;
        } else {
            z3 = z2;
        }
        boolean wallpaperOffset = windowState.setWallpaperOffset(i2, i4, windowState.mShouldScaleWallpaper ? zoomOutToScale(windowState.mWallpaperZoomOut) : 1.0f);
        if (z3 && (windowState.mAttrs.privateFlags & 4) != 0) {
            try {
                if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                    Slog.v(TAG, "Report new wp offset " + windowState + " x=" + windowState.mWallpaperX + " y=" + windowState.mWallpaperY + " zoom=" + windowState.mWallpaperZoomOut);
                }
                if (z) {
                    this.mWaitingOnWallpaper = windowState;
                }
                windowState.mClient.dispatchWallpaperOffsets(windowState.mWallpaperX, windowState.mWallpaperY, windowState.mWallpaperXStep, windowState.mWallpaperYStep, windowState.mWallpaperZoomOut, z);
                if (z && this.mWaitingOnWallpaper != null) {
                    long uptimeMillis = SystemClock.uptimeMillis();
                    if (this.mLastWallpaperTimeoutTime + WALLPAPER_TIMEOUT_RECOVERY < uptimeMillis) {
                        try {
                            if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                                Slog.v(TAG, "Waiting for offset complete...");
                            }
                            this.mService.mGlobalLock.wait(WALLPAPER_TIMEOUT);
                        } catch (InterruptedException unused) {
                        }
                        if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                            Slog.v(TAG, "Offset complete!");
                        }
                        if (WALLPAPER_TIMEOUT + uptimeMillis < SystemClock.uptimeMillis()) {
                            Slog.i(TAG, "Timeout waiting for wallpaper to offset: " + windowState);
                            this.mLastWallpaperTimeoutTime = uptimeMillis;
                        }
                    }
                    this.mWaitingOnWallpaper = null;
                }
            } catch (RemoteException unused2) {
            }
        }
        return wallpaperOffset;
    }

    private int getDisplayWidthOffset(int i, Rect rect, boolean z) {
        int width;
        if (!this.mShouldOffsetWallpaperCenter) {
            return 0;
        }
        if (this.mLargestDisplaySize == null) {
            this.mLargestDisplaySize = findLargestDisplaySize();
        }
        if (this.mLargestDisplaySize == null || this.mLargestDisplaySize.x == (width = rect.width()) || rect.width() >= rect.height()) {
            return 0;
        }
        float height = rect.height();
        Point point = this.mLargestDisplaySize;
        int round = Math.round(point.x * (height / point.y));
        if (z) {
            return round - ((width + round) / 2);
        }
        return Math.min(round - width, i) / 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowWallpaperPosition(WindowState windowState, float f, float f2, float f3, float f4) {
        if (windowState.mWallpaperX == f && windowState.mWallpaperY == f2) {
            return;
        }
        windowState.mWallpaperX = f;
        windowState.mWallpaperY = f2;
        windowState.mWallpaperXStep = f3;
        windowState.mWallpaperYStep = f4;
        updateWallpaperOffsetLocked(windowState, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWallpaperZoomOut(WindowState windowState, float f) {
        if (Float.compare(windowState.mWallpaperZoomOut, f) != 0) {
            windowState.mWallpaperZoomOut = f;
            this.mShouldUpdateZoom = true;
            updateWallpaperOffsetLocked(windowState, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setShouldZoomOutWallpaper(WindowState windowState, boolean z) {
        if (z != windowState.mShouldScaleWallpaper) {
            windowState.mShouldScaleWallpaper = z;
            updateWallpaperOffsetLocked(windowState, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowWallpaperDisplayOffset(WindowState windowState, int i, int i2) {
        if (windowState.mWallpaperDisplayOffsetX == i && windowState.mWallpaperDisplayOffsetY == i2) {
            return;
        }
        windowState.mWallpaperDisplayOffsetX = i;
        windowState.mWallpaperDisplayOffsetY = i2;
        updateWallpaperOffsetLocked(windowState, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle sendWindowWallpaperCommand(WindowState windowState, String str, int i, int i2, int i3, Bundle bundle, boolean z) {
        if ((windowState != this.mWallpaperTarget && windowState != this.mPrevWallpaperTarget) || this.mWallpaperControllerExt.sendWindowWallpaperCommand(windowState, str, bundle, z)) {
            return null;
        }
        sendWindowWallpaperCommand(str, i, i2, i3, bundle, z);
        return null;
    }

    private void sendWindowWallpaperCommand(String str, int i, int i2, int i3, Bundle bundle, boolean z) {
        for (int size = this.mWallpaperTokens.size() - 1; size >= 0; size--) {
            this.mWallpaperTokens.get(size).sendWindowWallpaperCommand(str, i, i2, i3, bundle, z);
        }
    }

    private void updateWallpaperOffsetLocked(WindowState windowState, boolean z) {
        WindowState windowState2 = this.mWallpaperTarget;
        if (windowState2 == null && windowState.mToken.isVisible() && windowState.mTransitionController.inTransition()) {
            windowState2 = windowState;
        }
        if (windowState2 != null) {
            float f = windowState2.mWallpaperX;
            if (f >= 0.0f) {
                this.mLastWallpaperX = f;
            } else {
                float f2 = windowState.mWallpaperX;
                if (f2 >= 0.0f) {
                    this.mLastWallpaperX = f2;
                }
            }
            float f3 = windowState2.mWallpaperY;
            if (f3 >= 0.0f) {
                this.mLastWallpaperY = f3;
            } else {
                float f4 = windowState.mWallpaperY;
                if (f4 >= 0.0f) {
                    this.mLastWallpaperY = f4;
                }
            }
            computeLastWallpaperZoomOut();
            int i = windowState2.mWallpaperDisplayOffsetX;
            if (i != Integer.MIN_VALUE) {
                this.mLastWallpaperDisplayOffsetX = i;
            } else {
                int i2 = windowState.mWallpaperDisplayOffsetX;
                if (i2 != Integer.MIN_VALUE) {
                    this.mLastWallpaperDisplayOffsetX = i2;
                }
            }
            int i3 = windowState2.mWallpaperDisplayOffsetY;
            if (i3 != Integer.MIN_VALUE) {
                this.mLastWallpaperDisplayOffsetY = i3;
            } else {
                int i4 = windowState.mWallpaperDisplayOffsetY;
                if (i4 != Integer.MIN_VALUE) {
                    this.mLastWallpaperDisplayOffsetY = i4;
                }
            }
            float f5 = windowState2.mWallpaperXStep;
            if (f5 >= 0.0f) {
                this.mLastWallpaperXStep = f5;
            } else {
                float f6 = windowState.mWallpaperXStep;
                if (f6 >= 0.0f) {
                    this.mLastWallpaperXStep = f6;
                }
            }
            float f7 = windowState2.mWallpaperYStep;
            if (f7 >= 0.0f) {
                this.mLastWallpaperYStep = f7;
            } else {
                float f8 = windowState.mWallpaperYStep;
                if (f8 >= 0.0f) {
                    this.mLastWallpaperYStep = f8;
                }
            }
        }
        for (int size = this.mWallpaperTokens.size() - 1; size >= 0; size--) {
            this.mWallpaperTokens.get(size).updateWallpaperOffset(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearLastWallpaperTimeoutTime() {
        this.mLastWallpaperTimeoutTime = 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void wallpaperCommandComplete(IBinder iBinder) {
        WindowState windowState = this.mWaitingOnWallpaper;
        if (windowState == null || windowState.mClient.asBinder() != iBinder) {
            return;
        }
        this.mWaitingOnWallpaper = null;
        this.mService.mGlobalLock.notifyAll();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void wallpaperOffsetsComplete(IBinder iBinder) {
        WindowState windowState = this.mWaitingOnWallpaper;
        if (windowState == null || windowState.mClient.asBinder() != iBinder) {
            return;
        }
        this.mWaitingOnWallpaper = null;
        this.mService.mGlobalLock.notifyAll();
    }

    private void findWallpaperTarget() {
        this.mFindResults.reset();
        if (this.mService.mAtmService.mSupportsFreeformWindowManagement && this.mDisplayContent.getDefaultTaskDisplayArea().isRootTaskVisible(5)) {
            this.mFindResults.setUseTopWallpaperAsTarget(true);
        }
        this.mDisplayContent.forAllWindows(this.mFindWallpapers, true);
        this.mDisplayContent.forAllWindows(this.mFindWallpaperTargetFunction, true);
        FindWallpaperTargetResult findWallpaperTargetResult = this.mFindResults;
        if (findWallpaperTargetResult.mNeedsShowWhenLockedWallpaper) {
            findWallpaperTargetResult.setUseTopWallpaperAsTarget(true);
        }
        FindWallpaperTargetResult findWallpaperTargetResult2 = this.mFindResults;
        if (findWallpaperTargetResult2.wallpaperTarget == null && findWallpaperTargetResult2.useTopWallpaperAsTarget) {
            findWallpaperTargetResult2.setWallpaperTarget(findWallpaperTargetResult2.getTopWallpaper(this.mDisplayContent.isKeyguardLocked()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<WindowState> getAllTopWallpapers() {
        ArrayList arrayList = new ArrayList(2);
        if (this.mFindResults.hasTopShowWhenLockedWallpaper()) {
            arrayList.add(this.mFindResults.mTopWallpaper.mTopShowWhenLockedWallpaper);
        }
        if (this.mFindResults.hasTopHideWhenLockedWallpaper()) {
            arrayList.add(this.mFindResults.mTopWallpaper.mTopHideWhenLockedWallpaper);
        }
        return arrayList;
    }

    private boolean isFullscreen(WindowManager.LayoutParams layoutParams) {
        return layoutParams.x == 0 && layoutParams.y == 0 && layoutParams.width == -1 && layoutParams.height == -1;
    }

    private void updateWallpaperWindowsTarget(FindWallpaperTargetResult findWallpaperTargetResult) {
        WindowState windowState;
        WindowState windowState2 = findWallpaperTargetResult.wallpaperTarget;
        if (this.mWallpaperTarget == windowState2 || ((windowState = this.mPrevWallpaperTarget) != null && windowState == windowState2)) {
            WindowState windowState3 = this.mPrevWallpaperTarget;
            if (windowState3 == null || windowState3.isAnimatingLw()) {
                return;
            }
            if (ProtoLogCache.WM_DEBUG_WALLPAPER_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WALLPAPER, -1478175541, 0, (String) null, (Object[]) null);
            }
            this.mPrevWallpaperTarget = null;
            this.mWallpaperTarget = windowState2;
            return;
        }
        if (ProtoLogCache.WM_DEBUG_WALLPAPER_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WALLPAPER, 114070759, 0, (String) null, new Object[]{String.valueOf(windowState2), String.valueOf(this.mWallpaperTarget), String.valueOf(Debug.getCallers(5))});
        }
        this.mPrevWallpaperTarget = null;
        final WindowState windowState4 = this.mWallpaperTarget;
        this.mWallpaperTarget = windowState2;
        if (windowState4 == null && windowState2 != null) {
            updateWallpaperOffsetLocked(windowState2, false);
        }
        if (windowState2 == null || windowState4 == null) {
            return;
        }
        boolean isAnimatingLw = windowState4.isAnimatingLw();
        boolean isAnimatingLw2 = windowState2.isAnimatingLw();
        if (ProtoLogCache.WM_DEBUG_WALLPAPER_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WALLPAPER, -275077723, 0, (String) null, new Object[]{String.valueOf(isAnimatingLw2), String.valueOf(isAnimatingLw)});
        }
        if (isAnimatingLw2 && isAnimatingLw && this.mDisplayContent.getWindow(new Predicate() { // from class: com.android.server.wm.WallpaperController$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$updateWallpaperWindowsTarget$3;
                lambda$updateWallpaperWindowsTarget$3 = WallpaperController.lambda$updateWallpaperWindowsTarget$3(WindowState.this, (WindowState) obj);
                return lambda$updateWallpaperWindowsTarget$3;
            }
        }) != null) {
            ActivityRecord activityRecord = windowState2.mActivityRecord;
            boolean z = (activityRecord == null || activityRecord.isVisibleRequested()) ? false : true;
            ActivityRecord activityRecord2 = windowState4.mActivityRecord;
            boolean z2 = (activityRecord2 == null || activityRecord2.isVisibleRequested()) ? false : true;
            if (ProtoLogCache.WM_DEBUG_WALLPAPER_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WALLPAPER, -360208282, 204, (String) null, new Object[]{String.valueOf(windowState4), Boolean.valueOf(z2), String.valueOf(windowState2), Boolean.valueOf(z)});
            }
            this.mPrevWallpaperTarget = windowState4;
            if (z && !z2) {
                if (ProtoLogCache.WM_DEBUG_WALLPAPER_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WALLPAPER, 1178653181, 0, (String) null, (Object[]) null);
                }
                this.mWallpaperTarget = windowState4;
            } else if (z == z2 && !this.mDisplayContent.mOpeningApps.contains(windowState2.mActivityRecord) && (this.mDisplayContent.mOpeningApps.contains(windowState4.mActivityRecord) || this.mDisplayContent.mClosingApps.contains(windowState4.mActivityRecord))) {
                this.mWallpaperTarget = windowState4;
            }
            findWallpaperTargetResult.setWallpaperTarget(windowState2);
        }
    }

    private void updateWallpaperTokens(boolean z, boolean z2) {
        WindowState topWallpaper = this.mFindResults.getTopWallpaper(z2);
        WallpaperWindowToken asWallpaperToken = topWallpaper == null ? null : topWallpaper.mToken.asWallpaperToken();
        for (int size = this.mWallpaperTokens.size() - 1; size >= 0; size--) {
            WallpaperWindowToken wallpaperWindowToken = this.mWallpaperTokens.get(size);
            if (!wallpaperWindowToken.hasChild()) {
                Slog.d(TAG, "token have not add any child");
            } else {
                wallpaperWindowToken.updateWallpaperWindows(z && wallpaperWindowToken == asWallpaperToken);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void adjustWallpaperWindows() {
        this.mDisplayContent.mWallpaperMayChange = false;
        findWallpaperTarget();
        updateWallpaperWindowsTarget(this.mFindResults);
        boolean z = this.mWallpaperTarget != null;
        if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
            Slog.v(TAG, "Wallpaper visibility: " + z + " at display " + this.mDisplayContent.getDisplayId());
        }
        if (z) {
            WindowState windowState = this.mWallpaperTarget;
            float f = windowState.mWallpaperX;
            if (f >= 0.0f) {
                this.mLastWallpaperX = f;
                this.mLastWallpaperXStep = windowState.mWallpaperXStep;
            }
            computeLastWallpaperZoomOut();
            WindowState windowState2 = this.mWallpaperTarget;
            float f2 = windowState2.mWallpaperY;
            if (f2 >= 0.0f) {
                this.mLastWallpaperY = f2;
                this.mLastWallpaperYStep = windowState2.mWallpaperYStep;
            }
            int i = windowState2.mWallpaperDisplayOffsetX;
            if (i != Integer.MIN_VALUE) {
                this.mLastWallpaperDisplayOffsetX = i;
            }
            int i2 = windowState2.mWallpaperDisplayOffsetY;
            if (i2 != Integer.MIN_VALUE) {
                this.mLastWallpaperDisplayOffsetY = i2;
            }
        }
        this.mWallpaperControllerExt.dispatchWallpaperWindowsTarget(this.mWallpaperTarget, this.mDisplayContent, z);
        if (!this.mDisplayContent.isKeyguardGoingAway() || !this.mIsLockscreenLiveWallpaperEnabled) {
            updateWallpaperTokens(z, this.mDisplayContent.isKeyguardLocked());
        }
        if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
            Slog.v(TAG, "adjustWallpaperWindows: wallpaper visibility " + z + ", lock visibility " + this.mDisplayContent.isKeyguardLocked());
        }
        if (z) {
            boolean z2 = this.mLastFrozen;
            boolean z3 = this.mFindResults.isWallpaperTargetForLetterbox;
            if (z2 != z3) {
                this.mLastFrozen = z3;
                sendWindowWallpaperCommand(z3 ? "android.wallpaper.freeze" : "android.wallpaper.unfreeze", 0, 0, 0, null, false);
            }
        }
        if (ProtoLogCache.WM_DEBUG_WALLPAPER_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_WALLPAPER, -304728471, 0, (String) null, new Object[]{String.valueOf(this.mWallpaperTarget), String.valueOf(this.mPrevWallpaperTarget)});
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean processWallpaperDrawPendingTimeout() {
        if (this.mWallpaperDrawState != 1) {
            return false;
        }
        this.mWallpaperDrawState = 2;
        if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
            Slog.v(TAG, "*** WALLPAPER DRAW TIMEOUT");
        }
        if (this.mService.getRecentsAnimationController() != null) {
            this.mService.getRecentsAnimationController().startAnimation();
        }
        this.mService.mAtmService.mBackNavigationController.startAnimation();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean wallpaperTransitionReady() {
        boolean z;
        boolean z2 = true;
        int size = this.mWallpaperTokens.size() - 1;
        while (true) {
            if (size < 0) {
                z = true;
                break;
            }
            if (this.mWallpaperTokens.get(size).hasVisibleNotDrawnWallpaper()) {
                int i = this.mWallpaperDrawState;
                z = i == 2;
                if (i == 0) {
                    this.mWallpaperDrawState = 1;
                    this.mService.mH.removeMessages(39, this);
                    WindowManagerService.H h = this.mService.mH;
                    h.sendMessageDelayed(h.obtainMessage(39, this), WALLPAPER_DRAW_PENDING_TIMEOUT_DURATION);
                }
                if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                    Slog.v(TAG, "Wallpaper should be visible but has not been drawn yet. mWallpaperDrawState=" + this.mWallpaperDrawState);
                }
                z2 = false;
            } else {
                size--;
            }
        }
        if (z2) {
            this.mWallpaperDrawState = 0;
            this.mService.mH.removeMessages(39, this);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void adjustWallpaperWindowsForAppTransitionIfNeeded(ArraySet<ActivityRecord> arraySet) {
        boolean z = true;
        if ((this.mDisplayContent.pendingLayoutChanges & 4) == 0) {
            int size = arraySet.size() - 1;
            while (true) {
                if (size < 0) {
                    z = false;
                    break;
                } else if (arraySet.valueAt(size).windowsCanBeWallpaperTarget()) {
                    break;
                } else {
                    size--;
                }
            }
        }
        if (z) {
            adjustWallpaperWindows();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addWallpaperToken(WallpaperWindowToken wallpaperWindowToken) {
        this.mWallpaperTokens.add(wallpaperWindowToken);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeWallpaperToken(WallpaperWindowToken wallpaperWindowToken) {
        this.mWallpaperTokens.remove(wallpaperWindowToken);
        this.mWallpaperControllerExt.removeWallpaperWindows();
    }

    @VisibleForTesting
    boolean canScreenshotWallpaper() {
        return canScreenshotWallpaper(getTopVisibleWallpaper());
    }

    private boolean canScreenshotWallpaper(WindowState windowState) {
        if (!this.mService.mPolicy.isScreenOn()) {
            if (WindowManagerDebugConfig.DEBUG_SCREENSHOT) {
                Slog.i(TAG, "Attempted to take screenshot while display was off.");
            }
            return false;
        }
        if (windowState != null) {
            return true;
        }
        if (WindowManagerDebugConfig.DEBUG_SCREENSHOT) {
            Slog.i(TAG, "No visible wallpaper to screenshot");
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bitmap screenshotWallpaperLocked() {
        WindowState topVisibleWallpaper = getTopVisibleWallpaper();
        if (!canScreenshotWallpaper(topVisibleWallpaper)) {
            return null;
        }
        Rect bounds = topVisibleWallpaper.getBounds();
        bounds.offsetTo(0, 0);
        ScreenCapture.ScreenshotHardwareBuffer captureLayers = ScreenCapture.captureLayers(topVisibleWallpaper.getSurfaceControl(), bounds, 1.0f);
        if (captureLayers == null) {
            Slog.w(TAG, "Failed to screenshot wallpaper");
            return null;
        }
        return Bitmap.wrapHardwareBuffer(captureLayers.getHardwareBuffer(), captureLayers.getColorSpace());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl mirrorWallpaperSurface() {
        WindowState topVisibleWallpaper = getTopVisibleWallpaper();
        if (topVisibleWallpaper != null) {
            return SurfaceControl.mirrorSurface(topVisibleWallpaper.getSurfaceControl());
        }
        return null;
    }

    WindowState getTopVisibleWallpaper() {
        for (int size = this.mWallpaperTokens.size() - 1; size >= 0; size--) {
            WallpaperWindowToken wallpaperWindowToken = this.mWallpaperTokens.get(size);
            for (int childCount = wallpaperWindowToken.getChildCount() - 1; childCount >= 0; childCount--) {
                WindowState childAt = wallpaperWindowToken.getChildAt(childCount);
                if (childAt.mWinAnimator.getShown() && childAt.mWinAnimator.mLastAlpha > 0.0f) {
                    return childAt;
                }
            }
        }
        return null;
    }

    private void computeLastWallpaperZoomOut() {
        if (this.mShouldUpdateZoom) {
            this.mLastWallpaperZoomOut = 0.0f;
            this.mDisplayContent.forAllWindows(this.mComputeMaxZoomOutFunction, true);
            this.mShouldUpdateZoom = false;
        }
    }

    private float zoomOutToScale(float f) {
        return MathUtils.lerp(1.0f, this.mMaxWallpaperScale, 1.0f - f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.print("displayId=");
        printWriter.println(this.mDisplayContent.getDisplayId());
        printWriter.print(str);
        printWriter.print("mWallpaperTarget=");
        printWriter.println(this.mWallpaperTarget);
        if (this.mPrevWallpaperTarget != null) {
            printWriter.print(str);
            printWriter.print("mPrevWallpaperTarget=");
            printWriter.println(this.mPrevWallpaperTarget);
        }
        printWriter.print(str);
        printWriter.print("mLastWallpaperX=");
        printWriter.print(this.mLastWallpaperX);
        printWriter.print(" mLastWallpaperY=");
        printWriter.println(this.mLastWallpaperY);
        if (this.mLastWallpaperDisplayOffsetX == Integer.MIN_VALUE && this.mLastWallpaperDisplayOffsetY == Integer.MIN_VALUE) {
            return;
        }
        printWriter.print(str);
        printWriter.print("mLastWallpaperDisplayOffsetX=");
        printWriter.print(this.mLastWallpaperDisplayOffsetX);
        printWriter.print(" mLastWallpaperDisplayOffsetY=");
        printWriter.println(this.mLastWallpaperDisplayOffsetY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class FindWallpaperTargetResult {
        boolean isWallpaperTargetForLetterbox;
        boolean mNeedsShowWhenLockedWallpaper;
        TopWallpaper mTopWallpaper;
        boolean useTopWallpaperAsTarget;
        WindowState wallpaperTarget;

        private FindWallpaperTargetResult() {
            this.mTopWallpaper = new TopWallpaper();
            this.useTopWallpaperAsTarget = false;
            this.wallpaperTarget = null;
            this.isWallpaperTargetForLetterbox = false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public static final class TopWallpaper {
            WindowState mTopHideWhenLockedWallpaper = null;
            WindowState mTopShowWhenLockedWallpaper = null;

            TopWallpaper() {
            }

            void reset() {
                this.mTopHideWhenLockedWallpaper = null;
                this.mTopShowWhenLockedWallpaper = null;
            }
        }

        void setTopHideWhenLockedWallpaper(WindowState windowState) {
            if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                Slog.v(WallpaperController.TAG, "setTopHideWhenLockedWallpaper " + windowState);
            }
            this.mTopWallpaper.mTopHideWhenLockedWallpaper = windowState;
        }

        void setTopShowWhenLockedWallpaper(WindowState windowState) {
            if (WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                Slog.v(WallpaperController.TAG, "setTopShowWhenLockedWallpaper " + windowState);
            }
            this.mTopWallpaper.mTopShowWhenLockedWallpaper = windowState;
        }

        boolean hasTopHideWhenLockedWallpaper() {
            return this.mTopWallpaper.mTopHideWhenLockedWallpaper != null;
        }

        boolean hasTopShowWhenLockedWallpaper() {
            return this.mTopWallpaper.mTopShowWhenLockedWallpaper != null;
        }

        WindowState getTopWallpaper(boolean z) {
            if (!z && hasTopHideWhenLockedWallpaper()) {
                return this.mTopWallpaper.mTopHideWhenLockedWallpaper;
            }
            return this.mTopWallpaper.mTopShowWhenLockedWallpaper;
        }

        void setWallpaperTarget(WindowState windowState) {
            this.wallpaperTarget = windowState;
        }

        void setUseTopWallpaperAsTarget(boolean z) {
            this.useTopWallpaperAsTarget = z;
        }

        void setIsWallpaperTargetForLetterbox(boolean z) {
            this.isWallpaperTargetForLetterbox = z;
        }

        void reset() {
            this.mTopWallpaper.reset();
            this.mNeedsShowWhenLockedWallpaper = false;
            this.wallpaperTarget = null;
            this.useTopWallpaperAsTarget = false;
            this.isWallpaperTargetForLetterbox = false;
        }
    }
}
