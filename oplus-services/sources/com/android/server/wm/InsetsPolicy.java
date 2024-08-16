package com.android.server.wm;

import android.app.WindowConfiguration;
import android.content.res.CompatibilityInfo;
import android.graphics.Rect;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Choreographer;
import android.view.InsetsAnimationControlCallbacks;
import android.view.InsetsAnimationControlImpl;
import android.view.InsetsAnimationControlRunner;
import android.view.InsetsController;
import android.view.InsetsFrameProvider;
import android.view.InsetsSource;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.InternalInsetsAnimationController;
import android.view.SurfaceControl;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.WindowInsetsAnimationControlListener;
import android.view.WindowManager;
import android.view.inputmethod.ImeTracker;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.DisplayThread;
import com.android.server.statusbar.StatusBarManagerInternal;
import com.android.server.wm.InsetsPolicy;
import java.util.Locale;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class InsetsPolicy {
    public static final int CONTROLLABLE_TYPES = (WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars()) | WindowInsets.Type.ime();
    private static final String TAG = "WindowManager";
    private boolean mAnimatingShown;
    private final DisplayContent mDisplayContent;
    private WindowState mFocusedWin;
    private final boolean mHideNavBarForKeyboard;
    private final DisplayPolicy mPolicy;
    private int mShowingTransientTypes;
    private final InsetsStateController mStateController;
    public IInsetsPolicyExt insetsPolicyExt = (IInsetsPolicyExt) ExtLoader.type(IInsetsPolicyExt.class).base(this).create();
    private final InsetsControlTarget mDummyControlTarget = new InsetsControlTarget() { // from class: com.android.server.wm.InsetsPolicy.1
        @Override // com.android.server.wm.InsetsControlTarget
        public void notifyInsetsControlChanged() {
            SurfaceControl leash;
            InsetsSourceControl[] controlsForDispatch = InsetsPolicy.this.mStateController.getControlsForDispatch(this);
            if (controlsForDispatch == null) {
                return;
            }
            boolean z = false;
            for (InsetsSourceControl insetsSourceControl : controlsForDispatch) {
                if (!InsetsPolicy.this.isTransient(insetsSourceControl.getType()) && (leash = insetsSourceControl.getLeash()) != null) {
                    InsetsPolicy.this.mDisplayContent.getPendingTransaction().setAlpha(leash, (insetsSourceControl.getType() & WindowInsets.Type.defaultVisible()) != 0 ? 1.0f : 0.0f);
                    z = true;
                }
            }
            if (z) {
                InsetsPolicy.this.mDisplayContent.scheduleAnimation();
            }
        }
    };
    private final BarWindow mStatusBar = new BarWindow(1);
    private final BarWindow mNavBar = new BarWindow(2);
    private final float[] mTmpFloat9 = new float[9];

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsPolicy(InsetsStateController insetsStateController, DisplayContent displayContent) {
        this.mStateController = insetsStateController;
        this.mDisplayContent = displayContent;
        DisplayPolicy displayPolicy = displayContent.getDisplayPolicy();
        this.mPolicy = displayPolicy;
        this.mHideNavBarForKeyboard = displayPolicy.getContext().getResources().getBoolean(17891714);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateBarControlTarget(WindowState windowState) {
        InsetsControlTarget statusControlTarget;
        if (this.mFocusedWin != windowState) {
            abortTransient();
        }
        this.mFocusedWin = windowState;
        InsetsControlTarget statusControlTarget2 = getStatusControlTarget(windowState, false);
        InsetsControlTarget navControlTarget = getNavControlTarget(windowState, false);
        WindowState notificationShade = this.mPolicy.getNotificationShade();
        WindowState topFullscreenOpaqueWindow = this.mPolicy.getTopFullscreenOpaqueWindow();
        InsetsStateController insetsStateController = this.mStateController;
        InsetsControlTarget insetsControlTarget = null;
        if (statusControlTarget2 == this.mDummyControlTarget) {
            statusControlTarget = getStatusControlTarget(windowState, true);
        } else {
            statusControlTarget = statusControlTarget2 == notificationShade ? getStatusControlTarget(topFullscreenOpaqueWindow, true) : null;
        }
        if (navControlTarget == this.mDummyControlTarget) {
            insetsControlTarget = getNavControlTarget(windowState, true);
        } else if (navControlTarget == notificationShade) {
            insetsControlTarget = getNavControlTarget(topFullscreenOpaqueWindow, true);
        }
        insetsStateController.onBarControlTargetChanged(statusControlTarget2, statusControlTarget, navControlTarget, insetsControlTarget);
        this.mStatusBar.updateVisibility(statusControlTarget2, WindowInsets.Type.statusBars());
        this.mNavBar.updateVisibility(navControlTarget, WindowInsets.Type.navigationBars());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasHiddenSources(int i) {
        InsetsState rawInsetsState = this.mStateController.getRawInsetsState();
        for (int sourceSize = rawInsetsState.sourceSize() - 1; sourceSize >= 0; sourceSize--) {
            InsetsSource sourceAt = rawInsetsState.sourceAt(sourceSize);
            if ((sourceAt.getType() & i) != 0 && !sourceAt.getFrame().isEmpty() && !sourceAt.isVisible()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void showTransient(int i, boolean z) {
        if (this.insetsPolicyExt.showTransient()) {
            Slog.d(TAG, "Zen mode, abort showTransient");
            return;
        }
        int i2 = this.mShowingTransientTypes;
        InsetsState rawInsetsState = this.mStateController.getRawInsetsState();
        for (int sourceSize = rawInsetsState.sourceSize() - 1; sourceSize >= 0; sourceSize--) {
            InsetsSource sourceAt = rawInsetsState.sourceAt(sourceSize);
            if (!sourceAt.isVisible()) {
                int type = sourceAt.getType();
                if ((sourceAt.getType() & i) != 0) {
                    i2 |= type;
                }
            }
        }
        Slog.d(TAG, "request show transient bar, mShowingTransientTypes = " + this.mShowingTransientTypes + " showingTransientTypes = " + i2);
        if (this.mShowingTransientTypes != i2) {
            this.mShowingTransientTypes = i2;
            StatusBarManagerInternal statusBarManagerInternal = this.mPolicy.getStatusBarManagerInternal();
            if (statusBarManagerInternal != null) {
                statusBarManagerInternal.showTransient(this.mDisplayContent.getDisplayId(), i2, z);
            }
            updateBarControlTarget(this.mFocusedWin);
            dispatchTransientSystemBarsVisibilityChanged(this.mFocusedWin, (i2 & (WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars())) != 0, z);
            this.mDisplayContent.mWmService.mAnimator.getChoreographer().postFrameCallback(new Choreographer.FrameCallback() { // from class: com.android.server.wm.InsetsPolicy$$ExternalSyntheticLambda0
                @Override // android.view.Choreographer.FrameCallback
                public final void doFrame(long j) {
                    InsetsPolicy.this.lambda$showTransient$0(j);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showTransient$0(long j) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mDisplayContent.mWmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                startAnimation(true, null);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void hideTransient() {
        if (this.mShowingTransientTypes == 0) {
            return;
        }
        dispatchTransientSystemBarsVisibilityChanged(this.mFocusedWin, false, false);
        startAnimation(false, new Runnable() { // from class: com.android.server.wm.InsetsPolicy$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                InsetsPolicy.this.lambda$hideTransient$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideTransient$1() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mDisplayContent.mWmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                SparseArray<InsetsSourceProvider> sourceProviders = this.mStateController.getSourceProviders();
                for (int size = sourceProviders.size() - 1; size >= 0; size--) {
                    InsetsSourceProvider valueAt = sourceProviders.valueAt(size);
                    if (isTransient(valueAt.getSource().getType())) {
                        valueAt.setClientVisible(false);
                    }
                }
                this.mShowingTransientTypes = 0;
                updateBarControlTarget(this.mFocusedWin);
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    boolean isTransient(int i) {
        return (this.mShowingTransientTypes & i) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsState adjustInsetsForWindow(WindowState windowState, InsetsState insetsState, boolean z) {
        InsetsState adjustVisibilityForTransientTypes = !z ? adjustVisibilityForTransientTypes(insetsState) : insetsState;
        InsetsState adjustVisibilityForIme = adjustVisibilityForIme(windowState, adjustVisibilityForTransientTypes, adjustVisibilityForTransientTypes == insetsState);
        return adjustInsetsForRoundedCorners(windowState.mToken, adjustVisibilityForIme, adjustVisibilityForIme == insetsState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsState adjustInsetsForWindow(WindowState windowState, InsetsState insetsState) {
        return adjustInsetsForWindow(windowState, insetsState, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getInsetsForWindowMetrics(WindowToken windowToken, InsetsState insetsState) {
        InsetsState rawInsetsState;
        if (windowToken != null && windowToken.isFixedRotationTransforming()) {
            rawInsetsState = windowToken.getFixedRotationTransformInsetsState();
        } else {
            rawInsetsState = this.mStateController.getRawInsetsState();
        }
        insetsState.set(rawInsetsState, true);
        for (int sourceSize = insetsState.sourceSize() - 1; sourceSize >= 0; sourceSize--) {
            InsetsSource sourceAt = insetsState.sourceAt(sourceSize);
            if (isTransient(sourceAt.getType())) {
                sourceAt.setVisible(false);
            }
        }
        adjustInsetsForRoundedCorners(windowToken, insetsState, false);
        if (windowToken == null || !windowToken.hasSizeCompatBounds()) {
            return;
        }
        insetsState.scale(1.0f / windowToken.getCompatScale());
        if (windowToken instanceof ActivityRecord) {
            float compatScaleInOplusCompatMode = ((ActivityRecord) windowToken).getWrapper().getExtImpl().getCompatScaleInOplusCompatMode();
            if (compatScaleInOplusCompatMode != 1.0f) {
                insetsState.scale(1.0f / compatScaleInOplusCompatMode);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsState enforceInsetsPolicyForTarget(WindowManager.LayoutParams layoutParams, @WindowConfiguration.WindowingMode int i, boolean z, InsetsState insetsState) {
        InsetsState insetsState2;
        if (layoutParams.type == 2011) {
            insetsState2 = new InsetsState(insetsState);
            insetsState2.removeSource(InsetsSource.ID_IME);
        } else {
            InsetsFrameProvider[] insetsFrameProviderArr = layoutParams.providedInsets;
            if (insetsFrameProviderArr != null) {
                InsetsState insetsState3 = insetsState;
                for (InsetsFrameProvider insetsFrameProvider : insetsFrameProviderArr) {
                    if ((insetsFrameProvider.getType() & WindowInsets.Type.systemBars()) != 0) {
                        if (insetsState3 == insetsState) {
                            insetsState3 = new InsetsState(insetsState3);
                        }
                        if (insetsState3.peekSource(insetsFrameProvider.getId()) != null) {
                            insetsState3.removeSource(insetsFrameProvider.getId());
                        } else {
                            insetsState3.getWrapper().getExtImpl().removeSourceByType(insetsFrameProvider.getType());
                        }
                    }
                }
                insetsState2 = insetsState3;
            } else {
                insetsState2 = insetsState;
            }
        }
        if (!layoutParams.isFullscreen() || layoutParams.getFitInsetsTypes() != 0) {
            if (insetsState2 == insetsState) {
                insetsState2 = new InsetsState(insetsState);
            }
            for (int sourceSize = insetsState2.sourceSize() - 1; sourceSize >= 0; sourceSize--) {
                if (insetsState2.sourceAt(sourceSize).getType() == WindowInsets.Type.captionBar()) {
                    insetsState2.removeSourceAt(sourceSize);
                }
            }
        }
        SparseArray<InsetsSourceProvider> sourceProviders = this.mStateController.getSourceProviders();
        int i2 = layoutParams.type;
        for (int size = sourceProviders.size() - 1; size >= 0; size--) {
            InsetsSourceProvider valueAt = sourceProviders.valueAt(size);
            if (valueAt.overridesFrame(i2)) {
                if (insetsState2 == insetsState) {
                    insetsState2 = new InsetsState(insetsState2);
                }
                InsetsSource insetsSource = new InsetsSource(valueAt.getSource());
                insetsSource.setFrame(valueAt.getOverriddenFrame(i2));
                insetsState2.addSource(insetsSource);
            }
        }
        if (WindowConfiguration.isFloating(i) || (i == 6 && z)) {
            int captionBar = WindowInsets.Type.captionBar();
            if (i != 2) {
                captionBar |= WindowInsets.Type.ime();
            }
            InsetsState insetsState4 = new InsetsState();
            insetsState4.set(insetsState2, captionBar);
            insetsState2 = insetsState4;
        }
        if (this.insetsPolicyExt.hasFoldRemapDisplayDisableFeature()) {
            this.insetsPolicyExt.removeSource(insetsState2, this.mDisplayContent);
        }
        return insetsState2;
    }

    private InsetsState adjustVisibilityForTransientTypes(InsetsState insetsState) {
        InsetsState insetsState2 = insetsState;
        for (int sourceSize = insetsState.sourceSize() - 1; sourceSize >= 0; sourceSize--) {
            InsetsSource sourceAt = insetsState2.sourceAt(sourceSize);
            if (isTransient(sourceAt.getType()) && sourceAt.isVisible()) {
                if (insetsState2 == insetsState) {
                    insetsState2 = new InsetsState(insetsState);
                }
                InsetsSource insetsSource = new InsetsSource(sourceAt);
                insetsSource.setVisible(false);
                insetsState2.addSource(insetsSource);
            }
        }
        return insetsState2;
    }

    private InsetsState adjustVisibilityForIme(WindowState windowState, InsetsState insetsState, boolean z) {
        InsetsSource peekSource;
        if (windowState.mIsImWindow) {
            boolean z2 = !this.mHideNavBarForKeyboard;
            InsetsState insetsState2 = insetsState;
            for (int sourceSize = insetsState.sourceSize() - 1; sourceSize >= 0; sourceSize--) {
                InsetsSource sourceAt = insetsState.sourceAt(sourceSize);
                if (sourceAt.getType() == WindowInsets.Type.navigationBars() && sourceAt.isVisible() != z2) {
                    if (insetsState2 == insetsState && z) {
                        insetsState2 = new InsetsState(insetsState);
                    }
                    InsetsSource insetsSource = new InsetsSource(sourceAt);
                    insetsSource.setVisible(z2);
                    insetsState2.addSource(insetsSource);
                }
            }
            return insetsState2;
        }
        ActivityRecord activityRecord = windowState.mActivityRecord;
        if (activityRecord != null && activityRecord.mImeInsetsFrozenUntilStartInput && (peekSource = insetsState.peekSource(InsetsSource.ID_IME)) != null) {
            boolean isRequestedVisible = windowState.isRequestedVisible(WindowInsets.Type.ime());
            if (z) {
                insetsState = new InsetsState(insetsState);
            }
            InsetsSource insetsSource2 = new InsetsSource(peekSource);
            insetsSource2.setVisible(isRequestedVisible);
            insetsState.addSource(insetsSource2);
        }
        return insetsState;
    }

    private InsetsState adjustInsetsForRoundedCorners(WindowToken windowToken, InsetsState insetsState, boolean z) {
        if (windowToken != null) {
            ActivityRecord asActivityRecord = windowToken.asActivityRecord();
            Task task = asActivityRecord != null ? asActivityRecord.getTask() : null;
            if (task != null && !task.getWindowConfiguration().tasksAreFloating()) {
                if (z) {
                    insetsState = new InsetsState(insetsState);
                }
                insetsState.setRoundedCornerFrame(task.getBounds());
            }
        }
        return insetsState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onInsetsModified(InsetsControlTarget insetsControlTarget) {
        this.mStateController.onInsetsModified(insetsControlTarget);
        checkAbortTransient(insetsControlTarget);
        updateBarControlTarget(this.mFocusedWin);
    }

    private void checkAbortTransient(InsetsControlTarget insetsControlTarget) {
        if (this.mShowingTransientTypes == 0) {
            return;
        }
        int requestedVisibleTypes = (insetsControlTarget.getRequestedVisibleTypes() & this.mStateController.getFakeControllingTypes(insetsControlTarget)) | (this.mStateController.getImeSourceProvider().isClientVisible() ? WindowInsets.Type.navigationBars() : 0);
        this.mShowingTransientTypes &= ~requestedVisibleTypes;
        if (requestedVisibleTypes != 0) {
            this.mDisplayContent.setLayoutNeeded();
            this.mDisplayContent.mWmService.requestTraversal();
            StatusBarManagerInternal statusBarManagerInternal = this.mPolicy.getStatusBarManagerInternal();
            if (statusBarManagerInternal != null) {
                statusBarManagerInternal.abortTransient(this.mDisplayContent.getDisplayId(), requestedVisibleTypes);
            }
        }
    }

    private void abortTransient() {
        if (this.mShowingTransientTypes == 0) {
            return;
        }
        StatusBarManagerInternal statusBarManagerInternal = this.mPolicy.getStatusBarManagerInternal();
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.abortTransient(this.mDisplayContent.getDisplayId(), this.mShowingTransientTypes);
        }
        this.mShowingTransientTypes = 0;
        this.mDisplayContent.setLayoutNeeded();
        this.mDisplayContent.mWmService.requestTraversal();
        dispatchTransientSystemBarsVisibilityChanged(this.mFocusedWin, false, false);
    }

    private InsetsControlTarget getStatusControlTarget(WindowState windowState, boolean z) {
        if (!z && isTransient(WindowInsets.Type.statusBars())) {
            return this.mDummyControlTarget;
        }
        WindowState notificationShade = this.mPolicy.getNotificationShade();
        if (windowState == notificationShade) {
            return windowState;
        }
        if (remoteInsetsControllerControlsSystemBars(windowState)) {
            ActivityRecord activityRecord = windowState.mActivityRecord;
            this.mDisplayContent.mRemoteInsetsControlTarget.topFocusedWindowChanged(activityRecord != null ? activityRecord.mActivityComponent : null, windowState.getRequestedVisibleTypes());
            return this.mDisplayContent.mRemoteInsetsControlTarget;
        }
        if (this.mPolicy.areSystemBarsForcedShownLw() && this.insetsPolicyExt.shouldForceShowStatusBar(this.mDisplayContent)) {
            return null;
        }
        if (forceShowsStatusBarTransiently() && !z) {
            return this.mDummyControlTarget;
        }
        if (this.mDisplayContent.mWmService.mAtmService.getWrapper().getFlexibleExtImpl().isInPocketStudio(this.mDisplayContent.getDisplayId())) {
            return this.insetsPolicyExt.getContainerWindow(windowState, this.mDisplayContent, true);
        }
        WindowState statusControlTargetInSplit = this.insetsPolicyExt.getStatusControlTargetInSplit(windowState);
        return (statusControlTargetInSplit == null || this.insetsPolicyExt.shouldForceShowStatusBar(this.mDisplayContent)) ? ((!canBeTopFullscreenOpaqueWindow(windowState) && this.mPolicy.topAppHidesSystemBar(WindowInsets.Type.statusBars()) && (notificationShade == null || !notificationShade.canReceiveKeys())) || this.insetsPolicyExt.isWindowingZoomMode(windowState) || this.insetsPolicyExt.isFlexibleTaskIgnoreSysBar(windowState)) ? this.mPolicy.getTopFullscreenOpaqueWindow() : windowState : statusControlTargetInSplit;
    }

    private static boolean canBeTopFullscreenOpaqueWindow(WindowState windowState) {
        int i;
        return (windowState != null && (i = windowState.mAttrs.type) >= 1 && i <= 99) && windowState.mAttrs.isFullscreen() && !windowState.isFullyTransparent() && !windowState.inMultiWindowMode();
    }

    private InsetsControlTarget getNavControlTarget(WindowState windowState, boolean z) {
        WindowState topFullscreenOpaqueWindow;
        InsetsSourceProvider controllableInsetProvider;
        ActivityRecord activityRecord;
        WindowState windowState2 = this.mDisplayContent.mInputMethodWindow;
        if (windowState2 != null && windowState2.isVisible() && !this.mHideNavBarForKeyboard) {
            return null;
        }
        if (!z && isTransient(WindowInsets.Type.navigationBars())) {
            return this.mDummyControlTarget;
        }
        if (windowState == this.mPolicy.getNotificationShade()) {
            if (windowState == null || windowState.getDisplayContent() == null || !windowState.getDisplayContent().isKeyguardGoingAway() || (activityRecord = this.mDisplayContent.mFocusedApp) == null) {
                return windowState;
            }
            WindowState findMainWindow = activityRecord.findMainWindow(false);
            if (findMainWindow == null) {
                Slog.d(TAG, "getNavControlTarget skip focus on NotificationShade for going away, appWin==null");
            }
            return findMainWindow;
        }
        if (windowState != null && (controllableInsetProvider = windowState.getControllableInsetProvider()) != null && controllableInsetProvider.getSource().getType() == WindowInsets.Type.navigationBars()) {
            return windowState;
        }
        if (forcesShowingNavigationBars(windowState)) {
            return null;
        }
        if (remoteInsetsControllerControlsSystemBars(windowState)) {
            ActivityRecord activityRecord2 = windowState.mActivityRecord;
            this.mDisplayContent.mRemoteInsetsControlTarget.topFocusedWindowChanged(activityRecord2 != null ? activityRecord2.mActivityComponent : null, windowState.getRequestedVisibleTypes());
            return this.mDisplayContent.mRemoteInsetsControlTarget;
        }
        if (this.mPolicy.areSystemBarsForcedShownLw()) {
            return null;
        }
        if (forceShowsNavigationBarTransiently() && !z) {
            return this.mDummyControlTarget;
        }
        if (!this.mPolicy.topAppHidesSystemBar(WindowInsets.Type.navigationBars()) && this.insetsPolicyExt.isWindowingZoomMode(windowState) && (topFullscreenOpaqueWindow = this.mPolicy.getTopFullscreenOpaqueWindow()) != null) {
            return topFullscreenOpaqueWindow;
        }
        if (this.insetsPolicyExt.shouldTopFullOpqWinForceCtrlNavBar(windowState)) {
            return this.mPolicy.getTopFullscreenOpaqueWindow();
        }
        return this.mDisplayContent.mWmService.mAtmService.getWrapper().getFlexibleExtImpl().isInPocketStudio(this.mDisplayContent.getDisplayId()) ? this.insetsPolicyExt.getContainerWindow(windowState, this.mDisplayContent, false) : windowState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean forcesShowingNavigationBars(WindowState windowState) {
        return this.mPolicy.isForceShowNavigationBarEnabled() && windowState != null && windowState.getActivityType() == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean remoteInsetsControllerControlsSystemBars(WindowState windowState) {
        DisplayContent displayContent;
        return windowState != null && this.mPolicy.isRemoteInsetsControllerControllingSystemBars() && (displayContent = this.mDisplayContent) != null && displayContent.mRemoteInsetsControlTarget != null && windowState.getAttrs().type >= 1 && windowState.getAttrs().type <= 99;
    }

    private boolean forceShowsStatusBarTransiently() {
        WindowState statusBar = this.mPolicy.getStatusBar();
        return (statusBar == null || (statusBar.mAttrs.privateFlags & 2048) == 0) ? false : true;
    }

    private boolean forceShowsNavigationBarTransiently() {
        WindowState notificationShade = this.mPolicy.getNotificationShade();
        return (notificationShade == null || (notificationShade.mAttrs.privateFlags & 8388608) == 0) ? false : true;
    }

    @VisibleForTesting
    void startAnimation(boolean z, Runnable runnable) {
        SparseArray<InsetsSourceControl> sparseArray = new SparseArray<>();
        InsetsSourceControl[] controlsForDispatch = this.mStateController.getControlsForDispatch(this.mDummyControlTarget);
        if (controlsForDispatch == null) {
            if (runnable != null) {
                DisplayThread.getHandler().post(runnable);
                return;
            }
            return;
        }
        int i = 0;
        for (InsetsSourceControl insetsSourceControl : controlsForDispatch) {
            if (isTransient(insetsSourceControl.getType()) && insetsSourceControl.getLeash() != null) {
                i |= insetsSourceControl.getType();
                sparseArray.put(insetsSourceControl.getId(), new InsetsSourceControl(insetsSourceControl));
            }
        }
        controlAnimationUnchecked(i, sparseArray, z, runnable);
    }

    private void controlAnimationUnchecked(int i, SparseArray<InsetsSourceControl> sparseArray, boolean z, Runnable runnable) {
        new InsetsPolicyAnimationControlListener(z, runnable, i).mControlCallbacks.controlAnimationUnchecked(i, sparseArray, z);
    }

    private void dispatchTransientSystemBarsVisibilityChanged(WindowState windowState, boolean z, boolean z2) {
        Task task;
        if (windowState == null || (task = windowState.getTask()) == null) {
            return;
        }
        int i = task.mTaskId;
        if (i != -1) {
            this.mDisplayContent.mWmService.mTaskSystemBarsListenerController.dispatchTransientSystemBarVisibilityChanged(i, z, z2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class BarWindow {
        private final int mId;
        private int mState = 0;

        BarWindow(int i) {
            this.mId = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateVisibility(InsetsControlTarget insetsControlTarget, int i) {
            setVisible(insetsControlTarget == null || insetsControlTarget.isRequestedVisible(i));
        }

        private void setVisible(boolean z) {
            int i = z ? 0 : 2;
            if (this.mState != i) {
                Slog.d(InsetsPolicy.TAG, String.format(Locale.getDefault(), "BarWindow--setVisible: id=%d, oldState=%d, state=%d", Integer.valueOf(this.mId), Integer.valueOf(this.mState), Integer.valueOf(i)));
                this.mState = i;
                StatusBarManagerInternal statusBarManagerInternal = InsetsPolicy.this.mPolicy.getStatusBarManagerInternal();
                if (statusBarManagerInternal != null) {
                    statusBarManagerInternal.setWindowState(InsetsPolicy.this.mDisplayContent.getDisplayId(), this.mId, i);
                    InsetsPolicy.this.mPolicy.getWrapper().getExtImpl().notifyWindowStateChanged(this.mId, i, InsetsPolicy.this.mDisplayContent.getDisplayId());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class InsetsPolicyAnimationControlListener extends InsetsController.InternalAnimationControlListener {
        InsetsPolicyAnimationControlCallbacks mControlCallbacks;
        Runnable mFinishCallback;

        InsetsPolicyAnimationControlListener(boolean z, Runnable runnable, int i) {
            super(z, false, i, 2, false, 0, (WindowInsetsAnimationControlListener) null, (ImeTracker.InputMethodJankContext) null);
            this.mFinishCallback = runnable;
            this.mControlCallbacks = new InsetsPolicyAnimationControlCallbacks(this);
        }

        protected void onAnimationFinish() {
            super.onAnimationFinish();
            if (this.mFinishCallback != null) {
                DisplayThread.getHandler().post(this.mFinishCallback);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public class InsetsPolicyAnimationControlCallbacks implements InsetsAnimationControlCallbacks {
            private InsetsAnimationControlImpl mAnimationControl = null;
            private InsetsPolicyAnimationControlListener mListener;

            public void notifyFinished(InsetsAnimationControlRunner insetsAnimationControlRunner, boolean z) {
            }

            public void reportPerceptible(int i, boolean z) {
            }

            public <T extends InsetsAnimationControlRunner & InternalInsetsAnimationController> void startAnimation(T t, WindowInsetsAnimationControlListener windowInsetsAnimationControlListener, int i, WindowInsetsAnimation windowInsetsAnimation, WindowInsetsAnimation.Bounds bounds) {
            }

            InsetsPolicyAnimationControlCallbacks(InsetsPolicyAnimationControlListener insetsPolicyAnimationControlListener) {
                this.mListener = insetsPolicyAnimationControlListener;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void controlAnimationUnchecked(final int i, SparseArray<InsetsSourceControl> sparseArray, boolean z) {
                if (i == 0) {
                    return;
                }
                InsetsPolicy.this.mAnimatingShown = z;
                InsetsState insetsState = InsetsPolicy.this.mFocusedWin.getInsetsState();
                InsetsController.InternalAnimationControlListener internalAnimationControlListener = this.mListener;
                this.mAnimationControl = new InsetsAnimationControlImpl(sparseArray, (Rect) null, insetsState, internalAnimationControlListener, i, this, internalAnimationControlListener.getDurationMs(), InsetsPolicyAnimationControlListener.this.getInsetsInterpolator(), !z ? 1 : 0, !z ? 1 : 0, (CompatibilityInfo.Translator) null, (ImeTracker.Token) null);
                SurfaceAnimationThread.getHandler().post(new Runnable() { // from class: com.android.server.wm.InsetsPolicy$InsetsPolicyAnimationControlListener$InsetsPolicyAnimationControlCallbacks$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        InsetsPolicy.InsetsPolicyAnimationControlListener.InsetsPolicyAnimationControlCallbacks.this.lambda$controlAnimationUnchecked$0(i);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$controlAnimationUnchecked$0(int i) {
                this.mListener.onReady(this.mAnimationControl, i);
            }

            public void scheduleApplyChangeInsets(InsetsAnimationControlRunner insetsAnimationControlRunner) {
                if (this.mAnimationControl.applyChangeInsets((InsetsState) null)) {
                    this.mAnimationControl.finish(InsetsPolicy.this.mAnimatingShown);
                }
            }

            public void applySurfaceParams(SyncRtSurfaceTransactionApplier.SurfaceParams... surfaceParamsArr) {
                SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                for (int length = surfaceParamsArr.length - 1; length >= 0; length--) {
                    SyncRtSurfaceTransactionApplier.applyParams(transaction, surfaceParamsArr[length], InsetsPolicy.this.mTmpFloat9);
                }
                transaction.apply();
                transaction.close();
            }

            public void releaseSurfaceControlFromRt(SurfaceControl surfaceControl) {
                surfaceControl.release();
            }
        }
    }
}
