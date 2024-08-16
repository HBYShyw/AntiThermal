package com.android.server.wm;

import android.content.Context;
import android.graphics.Rect;
import android.os.Debug;
import android.os.Trace;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.server.policy.WindowManagerPolicy;
import java.io.PrintWriter;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WindowStateAnimator {
    static final int COMMIT_DRAW_PENDING = 2;
    static final int DRAW_PENDING = 1;
    static final int HAS_DRAWN = 4;
    static final int NO_SURFACE = 0;
    static final int PRESERVED_SURFACE_LAYER = 1;
    static final int READY_TO_SHOW = 3;
    static final int ROOT_TASK_CLIP_AFTER_ANIM = 0;
    static final int ROOT_TASK_CLIP_NONE = 1;
    static final String TAG = "WindowManager";
    boolean mAnimationIsEntrance;
    final WindowAnimator mAnimator;
    int mAttrType;
    final Context mContext;
    int mDrawState;
    boolean mEnterAnimationPending;
    boolean mEnteringAnimation;
    final boolean mIsWallpaper;
    boolean mLastHidden;
    final WindowManagerPolicy mPolicy;
    final WindowManagerService mService;
    final Session mSession;
    WindowSurfaceController mSurfaceController;
    private final WallpaperController mWallpaperControllerLocked;
    final WindowState mWin;
    float mShownAlpha = 0.0f;
    float mAlpha = 0.0f;
    float mLastAlpha = 0.0f;
    private final Rect mSystemDecorRect = new Rect();
    public IWindowStateAnimatorExt mStateAnimatorExt = (IWindowStateAnimatorExt) ExtLoader.type(IWindowStateAnimatorExt.class).base(this).create();

    /* JADX INFO: Access modifiers changed from: package-private */
    public String drawStateToString() {
        return drawStateToString(this.mDrawState);
    }

    String drawStateToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? Integer.toString(this.mDrawState) : "HAS_DRAWN" : "READY_TO_SHOW" : "COMMIT_DRAW_PENDING" : "DRAW_PENDING" : "NO_SURFACE";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void printWindowState(int i, int i2, WindowState windowState, String str) {
        if (i != i2) {
            Slog.i(TAG, windowState + " state from " + drawStateToString(i) + " to " + drawStateToString(i2) + "; reason: " + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowStateAnimator(WindowState windowState) {
        WindowManagerService windowManagerService = windowState.mWmService;
        this.mService = windowManagerService;
        this.mAnimator = windowManagerService.mAnimator;
        this.mPolicy = windowManagerService.mPolicy;
        this.mContext = windowManagerService.mContext;
        this.mWin = windowState;
        this.mSession = windowState.mSession;
        this.mAttrType = windowState.mAttrs.type;
        this.mIsWallpaper = windowState.mIsWallpaper;
        this.mWallpaperControllerLocked = windowState.getDisplayContent().mWallpaperController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAnimationFinished() {
        if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
            String valueOf = String.valueOf(this);
            WindowState windowState = this.mWin;
            boolean z = windowState.mAnimatingExit;
            ActivityRecord activityRecord = windowState.mActivityRecord;
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ANIM, 1810209625, 60, (String) null, new Object[]{valueOf, Boolean.valueOf(z), Boolean.valueOf(activityRecord != null && activityRecord.reportedVisible)});
        }
        this.mWin.checkPolicyVisibilityChange();
        DisplayContent displayContent = this.mWin.getDisplayContent();
        int i = this.mAttrType;
        if ((i == 2000 || i == 2040) && this.mWin.isVisibleByPolicy()) {
            displayContent.setLayoutNeeded();
        }
        this.mWin.onExitAnimationDone();
        displayContent.pendingLayoutChanges |= 8;
        if (displayContent.mWallpaperController.isWallpaperTarget(this.mWin)) {
            displayContent.pendingLayoutChanges |= 4;
        }
        if (WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
            this.mService.mWindowPlacerLocked.debugLayoutRepeats("WindowStateAnimator", displayContent.pendingLayoutChanges);
        }
        ActivityRecord activityRecord2 = this.mWin.mActivityRecord;
        if (activityRecord2 != null) {
            activityRecord2.updateReportedVisibilityLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void hide(SurfaceControl.Transaction transaction, String str) {
        if (this.mLastHidden) {
            return;
        }
        this.mLastHidden = true;
        WindowSurfaceController windowSurfaceController = this.mSurfaceController;
        if (windowSurfaceController != null) {
            windowSurfaceController.hide(transaction, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean finishDrawingLocked(SurfaceControl.Transaction transaction) {
        WindowState windowState = this.mWin;
        boolean z = false;
        boolean z2 = windowState.mAttrs.type == 3;
        if (z2 && ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, -344488673, 0, (String) null, new Object[]{String.valueOf(windowState), String.valueOf(drawStateToString())});
        }
        if (this.mDrawState == 1) {
            if (ProtoLogCache.WM_DEBUG_DRAW_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_DRAW, -993378225, 0, (String) null, new Object[]{String.valueOf(this.mWin), String.valueOf(this.mSurfaceController)});
            }
            if (z2 && ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, 829434921, 0, (String) null, new Object[]{String.valueOf(this.mWin)});
            }
            printWindowState(this.mDrawState, 2, this.mWin, "finishDrawingLocked");
            this.mDrawState = 2;
            z = true;
        }
        if (transaction != null) {
            this.mWin.getSyncTransaction().merge(transaction);
            z = true;
        }
        if (this.mWin.getDisplayContent().mWaitingForDrawn.contains(this.mWin) || this.mService.mRoot.mWaitingForDrawn.contains(this.mWin)) {
            return true;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean commitFinishDrawingLocked() {
        if (WindowManagerDebugConfig.DEBUG_STARTING_WINDOW_VERBOSE && this.mWin.mAttrs.type == 3) {
            Slog.i(TAG, "commitFinishDrawingLocked: " + this.mWin + " cur mDrawState=" + drawStateToString());
        }
        int i = this.mDrawState;
        if ((i != 2 && i != 3) || this.mWin.getWrapper().getExtImpl().syncEmbeddedWindowDrawStateIfNeeded(this.mWin)) {
            return false;
        }
        if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ANIM, -203358733, 0, (String) null, new Object[]{String.valueOf(this.mSurfaceController)});
        }
        printWindowState(this.mDrawState, 3, this.mWin, "commitFinishDrawingLocked");
        this.mDrawState = 3;
        ActivityRecord activityRecord = this.mWin.mActivityRecord;
        if (activityRecord == null || activityRecord.canShowWindows() || this.mWin.mAttrs.type == 3) {
            return this.mWin.performShowLocked();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetDrawState() {
        printWindowState(this.mDrawState, 1, this.mWin, "resetDrawState");
        this.mDrawState = 1;
        ActivityRecord activityRecord = this.mWin.mActivityRecord;
        if (activityRecord == null || activityRecord.isAnimating(1)) {
            return;
        }
        this.mWin.mActivityRecord.clearAllDrawn();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowSurfaceController createSurfaceLocked() {
        WindowSurfaceController windowSurfaceController;
        WindowState windowState = this.mWin;
        WindowSurfaceController windowSurfaceController2 = this.mSurfaceController;
        if (windowSurfaceController2 != null) {
            return windowSurfaceController2;
        }
        windowState.setHasSurface(false);
        if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ANIM, 1335791109, 0, (String) null, new Object[]{String.valueOf(this)});
        }
        resetDrawState();
        this.mService.makeWindowFreezingScreenIfNeededLocked(windowState);
        WindowManager.LayoutParams layoutParams = windowState.mAttrs;
        int i = windowState.isSecureLocked() ? 132 : 4;
        if (ActivityTaskManagerService.LTW_DISABLE ? (this.mWin.mAttrs.privateFlags & 1048576) != 0 : !((this.mWin.mAttrs.privateFlags & 1048576) == 0 && layoutParams.type != 2099)) {
            i |= 64;
        }
        int i2 = i;
        if (WindowManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.v(TAG, "Creating surface in session " + this.mSession.mSurfaceSession + " window " + this + " format=" + layoutParams.format + " flags=" + i2);
        }
        try {
            try {
                try {
                    WindowSurfaceController windowSurfaceController3 = new WindowSurfaceController(layoutParams.getTitle().toString(), (layoutParams.flags & 16777216) != 0 ? -3 : layoutParams.format, i2, this, layoutParams.type);
                    this.mSurfaceController = windowSurfaceController3;
                    windowSurfaceController3.setColorSpaceAgnostic(windowState.getPendingTransaction(), (layoutParams.privateFlags & 16777216) != 0);
                    this.mStateAnimatorExt.addStartingBackColorLayerIfNeed(this.mWin);
                    windowState.setHasSurface(true);
                    windowState.mInputWindowHandle.forceChange();
                    if (ProtoLogCache.WM_SHOW_SURFACE_ALLOC_enabled) {
                        try {
                            ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, 745391677, 336, (String) null, new Object[]{String.valueOf(this.mSurfaceController), String.valueOf(this.mSession.mSurfaceSession), Long.valueOf(this.mSession.mPid), Long.valueOf(layoutParams.format), Long.valueOf(i2), String.valueOf(this)});
                        } catch (Surface.OutOfResourcesException unused) {
                            windowSurfaceController = null;
                            Slog.w(TAG, "OutOfResourcesException creating surface");
                            this.mService.mRoot.reclaimSomeSurfaceMemory(this, "create", true);
                            this.mDrawState = 0;
                            return windowSurfaceController;
                        }
                    }
                    if (WindowManagerDebugConfig.DEBUG) {
                        Slog.v(TAG, "Got surface: " + this.mSurfaceController + ", set left=" + windowState.getFrame().left + " top=" + windowState.getFrame().top);
                    }
                    if (WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
                        Slog.i(TAG, ">>> OPEN TRANSACTION createSurfaceLocked");
                        WindowManagerService.logSurface(windowState, "CREATE pos=(" + windowState.getFrame().left + "," + windowState.getFrame().top + ") HIDE", false);
                    }
                    this.mLastHidden = true;
                    if (WindowManagerDebugConfig.DEBUG) {
                        Slog.v(TAG, "Created surface " + this);
                    }
                    return this.mSurfaceController;
                } catch (Surface.OutOfResourcesException unused2) {
                    windowSurfaceController = null;
                }
            } catch (Exception e) {
                Slog.e(TAG, "Exception creating surface (parent dead?)", e);
                this.mDrawState = 0;
                return null;
            }
        } catch (Surface.OutOfResourcesException unused3) {
            windowSurfaceController = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasSurface() {
        WindowSurfaceController windowSurfaceController = this.mSurfaceController;
        return windowSurfaceController != null && windowSurfaceController.hasSurface();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroySurfaceLocked(SurfaceControl.Transaction transaction) {
        if (this.mSurfaceController == null) {
            return;
        }
        this.mWin.mHidden = true;
        try {
            if (WindowManagerDebugConfig.DEBUG_VISIBILITY) {
                WindowManagerService.logWithStack(TAG, "Window " + this + " destroying surface " + this.mSurfaceController + ", session " + this.mSession);
            }
            if (ProtoLogCache.WM_SHOW_SURFACE_ALLOC_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, -1391944764, 0, (String) null, new Object[]{String.valueOf(this.mWin), String.valueOf(new RuntimeException().fillInStackTrace())});
            }
            destroySurface(transaction);
            this.mWallpaperControllerLocked.hideWallpapers(this.mWin);
        } catch (RuntimeException e) {
            Slog.w(TAG, "Exception thrown when destroying Window " + this + " surface " + this.mSurfaceController + " session " + this.mSession + ": " + e.toString());
        }
        this.mWin.setHasSurface(false);
        WindowSurfaceController windowSurfaceController = this.mSurfaceController;
        if (windowSurfaceController != null) {
            windowSurfaceController.setShown(false);
        }
        this.mSurfaceController = null;
        printWindowState(this.mDrawState, 0, this.mWin, "destroySurfaceLocked");
        this.mDrawState = 0;
    }

    void computeShownFrameLocked() {
        if ((this.mIsWallpaper && this.mService.mRoot.mWallpaperActionPending) || this.mWin.isDragResizeChanged()) {
            return;
        }
        if (WindowManagerDebugConfig.DEBUG) {
            Slog.v(TAG, "computeShownFrameLocked: " + this + " not attached, mAlpha=" + this.mAlpha);
        }
        this.mShownAlpha = this.mAlpha;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepareSurfaceLocked(SurfaceControl.Transaction transaction) {
        WindowState windowState = this.mWin;
        if (!hasSurface()) {
            if (windowState.getOrientationChanging() && windowState.isGoneForLayout()) {
                if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1756082882, 0, (String) null, new Object[]{String.valueOf(windowState)});
                }
                windowState.setOrientationChanging(false);
                return;
            }
            return;
        }
        computeShownFrameLocked();
        if ((!windowState.isOnScreen() && !this.mStateAnimatorExt.prepareSurfaceLocked(windowState)) || this.mStateAnimatorExt.hideForUnFolded(windowState)) {
            hide(transaction, "prepareSurfaceLocked");
            this.mWallpaperControllerLocked.hideWallpapers(windowState);
            if (windowState.getOrientationChanging() && windowState.isGoneForLayout()) {
                windowState.setOrientationChanging(false);
                if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1756082882, 0, (String) null, new Object[]{String.valueOf(windowState)});
                }
            }
        } else {
            float f = this.mLastAlpha;
            float f2 = this.mShownAlpha;
            if (f != f2 || this.mLastHidden) {
                this.mLastAlpha = f2;
                if (ProtoLogCache.WM_SHOW_TRANSACTIONS_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_TRANSACTIONS, -1906387645, 168, (String) null, new Object[]{String.valueOf(this.mSurfaceController), Double.valueOf(this.mShownAlpha), Double.valueOf(windowState.mHScale), Double.valueOf(windowState.mVScale), String.valueOf(windowState)});
                }
                if (this.mSurfaceController.prepareToShowInTransaction(transaction, this.mShownAlpha) && this.mDrawState == 4 && this.mLastHidden && !this.mStateAnimatorExt.hideForUnFolded(windowState)) {
                    this.mSurfaceController.showRobustly(transaction);
                    this.mLastHidden = false;
                    DisplayContent displayContent = windowState.getDisplayContent();
                    if (!displayContent.getLastHasContent()) {
                        displayContent.pendingLayoutChanges |= 8;
                        if (WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
                            this.mService.mWindowPlacerLocked.debugLayoutRepeats("showSurfaceRobustlyLocked " + windowState, displayContent.pendingLayoutChanges);
                        }
                    }
                }
            }
        }
        if (windowState.getOrientationChanging()) {
            if (!windowState.isDrawn()) {
                if (windowState.mDisplayContent.shouldSyncRotationChange(windowState) && this.mStateAnimatorExt.waitDrawingCompleted(windowState, this.mContext)) {
                    windowState.mWmService.mRoot.mOrientationChangeComplete = false;
                    this.mAnimator.mLastWindowFreezeSource = windowState;
                }
                if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -1130891072, 0, (String) null, new Object[]{String.valueOf(windowState)});
                    return;
                }
                return;
            }
            windowState.setOrientationChanging(false);
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 916191774, 0, (String) null, new Object[]{String.valueOf(windowState)});
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOpaqueLocked(boolean z) {
        WindowSurfaceController windowSurfaceController = this.mSurfaceController;
        if (windowSurfaceController == null) {
            return;
        }
        windowSurfaceController.setOpaque(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSecureLocked(boolean z) {
        WindowSurfaceController windowSurfaceController = this.mSurfaceController;
        if (windowSurfaceController == null) {
            return;
        }
        windowSurfaceController.setSecure(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setColorSpaceAgnosticLocked(boolean z) {
        WindowSurfaceController windowSurfaceController = this.mSurfaceController;
        if (windowSurfaceController == null) {
            return;
        }
        windowSurfaceController.setColorSpaceAgnostic(this.mWin.getPendingTransaction(), z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyEnterAnimationLocked() {
        int i;
        if (this.mEnterAnimationPending) {
            this.mEnterAnimationPending = false;
            i = 1;
        } else {
            i = 3;
        }
        if (this.mAttrType != 1 && !this.mIsWallpaper) {
            applyAnimationLocked(i, true);
        }
        if (this.mService.mAccessibilityController.hasCallbacks()) {
            this.mService.mAccessibilityController.onWindowTransition(this.mWin, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00ad  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00f8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean applyAnimationLocked(int i, boolean z) {
        Animation loadAnimationAttr;
        int i2 = 1;
        if (this.mWin.isAnimating() && this.mAnimationIsEntrance == z) {
            return true;
        }
        WindowState windowState = this.mWin;
        if (windowState.mAttrs.type == 2011) {
            windowState.getDisplayContent().adjustForImeIfNeeded();
            if (z) {
                this.mWin.setDisplayLayoutNeeded();
                this.mService.mWindowPlacerLocked.requestTraversal();
            }
        }
        WindowState windowState2 = this.mWin;
        if (windowState2.mControllableInsetProvider != null) {
            return false;
        }
        if (windowState2.mToken.okToAnimate()) {
            if (this.mStateAnimatorExt.setStartingWindowExitAnimation(i, this.mWin)) {
                Trace.traceEnd(32L);
                return true;
            }
            int selectAnimation = this.mWin.getDisplayContent().getDisplayPolicy().selectAnimation(this.mWin, i);
            int i3 = -1;
            if (selectAnimation != 0) {
                if (selectAnimation != -1) {
                    Trace.traceBegin(32L, "WSA#loadAnimation");
                    loadAnimationAttr = AnimationUtils.loadAnimation(this.mContext, selectAnimation);
                    Trace.traceEnd(32L);
                    if (this.mStateAnimatorExt.skipWindowAnimationIfNeed(i, z, this.mWin)) {
                        loadAnimationAttr = null;
                    }
                    if (ProtoLogImpl.isEnabled(ProtoLogGroup.WM_DEBUG_ANIM) && ProtoLogCache.WM_DEBUG_ANIM_enabled) {
                        long j = selectAnimation;
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ANIM, -1318478129, 13588, (String) null, new Object[]{String.valueOf(this), Long.valueOf(j), Long.valueOf(i3), String.valueOf(loadAnimationAttr), Long.valueOf(i), Long.valueOf(this.mAttrType), Boolean.valueOf(z), String.valueOf(Debug.getCallers(20))});
                    }
                    if (loadAnimationAttr != null) {
                        Trace.traceBegin(32L, "WSA#startAnimation");
                        this.mStateAnimatorExt.skipWindowAnimation(z, this.mWin, loadAnimationAttr);
                        this.mStateAnimatorExt.adjustMultiSearchAnimation(z, this.mWin, loadAnimationAttr);
                        this.mWin.startAnimation(loadAnimationAttr);
                        Trace.traceEnd(32L);
                        this.mAnimationIsEntrance = z;
                    }
                }
                loadAnimationAttr = null;
                if (this.mStateAnimatorExt.skipWindowAnimationIfNeed(i, z, this.mWin)) {
                }
                if (ProtoLogImpl.isEnabled(ProtoLogGroup.WM_DEBUG_ANIM)) {
                    long j2 = selectAnimation;
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ANIM, -1318478129, 13588, (String) null, new Object[]{String.valueOf(this), Long.valueOf(j2), Long.valueOf(i3), String.valueOf(loadAnimationAttr), Long.valueOf(i), Long.valueOf(this.mAttrType), Boolean.valueOf(z), String.valueOf(Debug.getCallers(20))});
                }
                if (loadAnimationAttr != null) {
                }
            } else {
                if (i == 1) {
                    i2 = 0;
                } else if (i != 2) {
                    i2 = 3;
                    if (i == 3) {
                        i2 = 2;
                    } else if (i != 4) {
                        i2 = -1;
                    }
                }
                if (i2 >= 0) {
                    i3 = i2;
                    loadAnimationAttr = this.mWin.getDisplayContent().mAppTransition.loadAnimationAttr(this.mWin.mAttrs, i2, 0);
                    if (this.mStateAnimatorExt.skipWindowAnimationIfNeed(i, z, this.mWin)) {
                    }
                    if (ProtoLogImpl.isEnabled(ProtoLogGroup.WM_DEBUG_ANIM)) {
                    }
                    if (loadAnimationAttr != null) {
                    }
                } else {
                    i3 = i2;
                    loadAnimationAttr = null;
                    if (this.mStateAnimatorExt.skipWindowAnimationIfNeed(i, z, this.mWin)) {
                    }
                    if (ProtoLogImpl.isEnabled(ProtoLogGroup.WM_DEBUG_ANIM)) {
                    }
                    if (loadAnimationAttr != null) {
                    }
                }
            }
        } else {
            this.mWin.cancelAnimation();
        }
        return this.mWin.isAnimating(0, 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        WindowSurfaceController windowSurfaceController = this.mSurfaceController;
        if (windowSurfaceController != null) {
            windowSurfaceController.dumpDebug(protoOutputStream, 1146756268034L);
        }
        protoOutputStream.write(1159641169923L, this.mDrawState);
        this.mSystemDecorRect.dumpDebug(protoOutputStream, 1146756268036L);
        protoOutputStream.end(start);
    }

    public void dump(PrintWriter printWriter, String str, boolean z) {
        if (this.mAnimationIsEntrance) {
            printWriter.print(str);
            printWriter.print(" mAnimationIsEntrance=");
            printWriter.print(this.mAnimationIsEntrance);
        }
        WindowSurfaceController windowSurfaceController = this.mSurfaceController;
        if (windowSurfaceController != null) {
            windowSurfaceController.dump(printWriter, str, z);
        }
        if (z) {
            printWriter.print(str);
            printWriter.print("mDrawState=");
            printWriter.print(drawStateToString());
            printWriter.print(str);
            printWriter.print(" mLastHidden=");
            printWriter.println(this.mLastHidden);
            printWriter.print(str);
            printWriter.print("mEnterAnimationPending=" + this.mEnterAnimationPending);
            printWriter.print(str);
            printWriter.print("mSystemDecorRect=");
            this.mSystemDecorRect.printShortString(printWriter);
            printWriter.println();
        }
        if (this.mShownAlpha != 1.0f || this.mAlpha != 1.0f || this.mLastAlpha != 1.0f) {
            printWriter.print(str);
            printWriter.print("mShownAlpha=");
            printWriter.print(this.mShownAlpha);
            printWriter.print(" mAlpha=");
            printWriter.print(this.mAlpha);
            printWriter.print(" mLastAlpha=");
            printWriter.println(this.mLastAlpha);
        }
        if (this.mWin.mGlobalScale != 1.0f) {
            printWriter.print(str);
            printWriter.print("mGlobalScale=");
            printWriter.print(this.mWin.mGlobalScale);
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("WindowStateAnimator{");
        stringBuffer.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuffer.append(' ');
        stringBuffer.append(this.mWin.mAttrs.getTitle());
        stringBuffer.append('}');
        return stringBuffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getShown() {
        WindowSurfaceController windowSurfaceController = this.mSurfaceController;
        if (windowSurfaceController != null) {
            return windowSurfaceController.getShown();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroySurface(SurfaceControl.Transaction transaction) {
        try {
            try {
                this.mStateAnimatorExt.destoryCompactDimmer(this.mWin);
                WindowSurfaceController windowSurfaceController = this.mSurfaceController;
                if (windowSurfaceController != null) {
                    windowSurfaceController.destroy(transaction);
                }
            } catch (RuntimeException e) {
                Slog.w(TAG, "Exception thrown when destroying surface " + this + " surface " + this.mSurfaceController + " session " + this.mSession + ": " + e);
            }
        } finally {
            this.mWin.setHasSurface(false);
            this.mSurfaceController = null;
            printWindowState(this.mDrawState, 0, this.mWin, "destroySurface");
            this.mDrawState = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl getSurfaceControl() {
        if (hasSurface()) {
            return this.mSurfaceController.mSurfaceControl;
        }
        return null;
    }
}
