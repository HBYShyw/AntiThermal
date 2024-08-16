package com.android.server.wm;

import android.R;
import android.app.ActivityManagerInternal;
import android.app.WindowConfiguration;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.HardwareBuffer;
import android.hardware.display.DisplayManagerInternal;
import android.metrics.LogMaker;
import android.os.Bundle;
import android.os.Debug;
import android.os.HandlerExecutor;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.WorkSource;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.DisplayMetrics;
import android.util.DisplayUtils;
import android.util.EventLog;
import android.util.IntArray;
import android.util.Pair;
import android.util.RotationUtils;
import android.util.Size;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.util.proto.ProtoOutputStream;
import android.view.ContentRecordingSession;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.DisplayInfo;
import android.view.DisplayShape;
import android.view.IDisplayWindowInsetsController;
import android.view.ISystemGestureExclusionListener;
import android.view.IWindow;
import android.view.InputDevice;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.MagnificationSpec;
import android.view.MotionEvent;
import android.view.PrivacyIndicatorBounds;
import android.view.RemoteAnimationDefinition;
import android.view.RoundedCorners;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowManagerPolicyConstants;
import android.view.inputmethod.ImeTracker;
import android.window.IDisplayAreaOrganizer;
import android.window.ScreenCapture;
import android.window.TransitionRequestInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.ToBooleanFunction;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.display.IMirageDisplayManagerExt;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.wm.ActivityRecord;
import com.android.server.wm.ActivityTaskManagerInternal;
import com.android.server.wm.ActivityTaskManagerService;
import com.android.server.wm.BLASTSyncEngine;
import com.android.server.wm.DeviceStateController;
import com.android.server.wm.DisplayArea;
import com.android.server.wm.DisplayPolicy;
import com.android.server.wm.IDisplayContentExt;
import com.android.server.wm.RootWindowContainer;
import com.android.server.wm.WindowManagerInternal;
import com.android.server.wm.WindowManagerService;
import com.android.server.wm.utils.RegionUtils;
import com.android.server.wm.utils.RotationCache;
import com.android.server.wm.utils.WmDisplayCutout;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DisplayContent extends RootDisplayArea implements WindowManagerPolicy.DisplayContentInfo {
    private static final long FIXED_ROTATION_HIDE_ANIMATION_DEBOUNCE_DELAY_MS = 250;
    static final int FORCE_SCALING_MODE_AUTO = 0;
    static final int FORCE_SCALING_MODE_DISABLED = 1;
    static final int IME_TARGET_CONTROL = 2;
    static final int IME_TARGET_LAYERING = 0;
    static final float INVALID_DPI = 0.0f;
    private static final String TAG = "WindowManager";

    @VisibleForTesting
    boolean isDefaultDisplay;
    private SurfaceControl mA11yOverlayLayer;
    private Set<ActivityRecord> mActiveSizeCompatActivities;
    final ArrayList<RootWindowContainer.SleepToken> mAllSleepTokens;
    final AppTransition mAppTransition;
    final AppTransitionController mAppTransitionController;
    private final Consumer<WindowState> mApplyPostLayoutPolicy;
    private final Consumer<WindowState> mApplySurfaceChangesTransaction;
    private AsyncRotationController mAsyncRotationController;
    final ActivityTaskManagerService mAtmService;
    DisplayCutout mBaseDisplayCutout;
    int mBaseDisplayDensity;
    int mBaseDisplayHeight;
    float mBaseDisplayPhysicalXDpi;
    float mBaseDisplayPhysicalYDpi;
    int mBaseDisplayWidth;
    RoundedCorners mBaseRoundedCorners;
    final ArraySet<WindowContainer> mChangingContainers;

    @VisibleForTesting
    final float mCloseToSquareMaxAspectRatio;
    final ArraySet<ActivityRecord> mClosingApps;
    final ArrayMap<WindowContainer, Rect> mClosingChangingContainers;
    private final DisplayMetrics mCompatDisplayMetrics;
    float mCompatibleScreenScale;
    private final Predicate<WindowState> mComputeImeTargetPredicate;
    private ContentRecorder mContentRecorder;
    WindowState mCurrentFocus;
    private int mCurrentOverrideConfigurationChanges;
    PrivacyIndicatorBounds mCurrentPrivacyIndicatorBounds;
    String mCurrentUniqueDisplayId;
    private int mDeferUpdateImeTargetCount;
    private boolean mDeferredRemoval;
    final Consumer<DeviceStateController.DeviceState> mDeviceStateConsumer;

    @VisibleForTesting
    final DeviceStateController mDeviceStateController;
    final Display mDisplay;
    private IntArray mDisplayAccessUIDs;

    @VisibleForTesting
    DisplayAreaPolicy mDisplayAreaPolicy;
    private IDisplayContentExt mDisplayContentExt;
    private DisplayContentWrapper mDisplayContentWrapper;
    private final RotationCache<DisplayCutout, WmDisplayCutout> mDisplayCutoutCache;
    DisplayFrames mDisplayFrames;
    final int mDisplayId;
    private final DisplayInfo mDisplayInfo;
    private final DisplayMetrics mDisplayMetrics;
    private final DisplayPolicy mDisplayPolicy;
    private boolean mDisplayReady;
    private final DisplayRotation mDisplayRotation;
    final DisplayRotationCompatPolicy mDisplayRotationCompatPolicy;
    boolean mDisplayScalingDisabled;
    private final RotationCache<DisplayShape, DisplayShape> mDisplayShapeCache;
    private final PhysicalDisplaySwitchTransitionLauncher mDisplaySwitchTransitionLauncher;
    boolean mDontMoveToTop;
    DisplayWindowPolicyControllerHelper mDwpcHelper;
    private final ToBooleanFunction<WindowState> mFindFocusedWindow;
    private ActivityRecord mFixedRotationLaunchingApp;
    final FixedRotationTransitionListener mFixedRotationTransitionListener;
    ActivityRecord mFocusedApp;
    private PowerManager.WakeLock mHoldScreenWakeLock;
    private WindowState mHoldScreenWindow;
    boolean mIgnoreDisplayCutout;
    private InsetsControlTarget mImeControlTarget;
    private InputTarget mImeInputTarget;
    private WindowState mImeLayeringTarget;
    ImeScreenshot mImeScreenshot;
    private Pair<IBinder, WindowContainerListener> mImeTargetTokenListenerPair;
    private final ImeContainer mImeWindowsContainer;
    private boolean mInEnsureActivitiesVisible;
    private boolean mInTouchMode;
    DisplayCutout mInitialDisplayCutout;
    int mInitialDisplayDensity;
    int mInitialDisplayHeight;
    DisplayShape mInitialDisplayShape;
    int mInitialDisplayWidth;
    float mInitialPhysicalXDpi;
    float mInitialPhysicalYDpi;
    RoundedCorners mInitialRoundedCorners;

    @VisibleForTesting
    SurfaceControl mInputMethodSurfaceParent;
    WindowState mInputMethodWindow;
    private InputMonitor mInputMonitor;
    private SurfaceControl mInputOverlayLayer;
    private final InsetsPolicy mInsetsPolicy;
    private final InsetsStateController mInsetsStateController;
    boolean mIsDensityForced;
    boolean mIsSizeForced;
    private boolean mLastHasContent;
    private InputTarget mLastImeInputTarget;
    private WindowState mLastWakeLockHoldingWindow;
    private WindowState mLastWakeLockObscuringWindow;
    private boolean mLastWallpaperVisible;
    private boolean mLayoutNeeded;
    int mLayoutSeq;
    private MagnificationSpec mMagnificationSpec;
    private int mMaxUiWidth;
    private MetricsLogger mMetricsLogger;
    int mMinSizeOfResizeableTaskDp;
    final List<IBinder> mNoAnimationNotifyOnTransitionFinished;
    private INonStaticDisplayContentExt mNonStaticExt;
    private WindowState mObscuringWindow;
    private final ActivityTaskManagerInternal.SleepTokenAcquirer mOffTokenAcquirer;
    final ArraySet<ActivityRecord> mOpeningApps;
    private TaskDisplayArea mOrientationRequestingTaskDisplayArea;
    private SurfaceControl mOverlayLayer;
    private final Consumer<WindowState> mPerformLayout;
    private final Consumer<WindowState> mPerformLayoutAttached;
    private Point mPhysicalDisplaySize;
    final PinnedTaskController mPinnedTaskController;
    private final PointerEventDispatcher mPointerEventDispatcher;
    private final RotationCache<PrivacyIndicatorBounds, PrivacyIndicatorBounds> mPrivacyIndicatorBoundsCache;
    final DisplayMetrics mRealDisplayMetrics;
    final RemoteDisplayChangeController mRemoteDisplayChangeController;
    RemoteInsetsControlTarget mRemoteInsetsControlTarget;
    private final IBinder.DeathRecipient mRemoteInsetsDeath;
    private boolean mRemoved;
    private boolean mRemoving;
    private Set<Rect> mRestrictedKeepClearAreas;
    private RootWindowContainer mRootWindowContainer;
    private final DisplayRotationReversionController mRotationReversionController;
    private final RotationCache<RoundedCorners, RoundedCorners> mRoundedCornerCache;
    private boolean mSandboxDisplayApis;
    private final Consumer<WindowState> mScheduleToastTimeout;
    private ScreenRotationAnimation mScreenRotationAnimation;
    private final SurfaceSession mSession;
    final SparseArray<ShellRoot> mShellRoots;
    boolean mSkipAppTransitionAnimation;
    private boolean mSleeping;
    private final Region mSystemGestureExclusion;
    private int mSystemGestureExclusionLimit;
    private final RemoteCallbackList<ISystemGestureExclusionListener> mSystemGestureExclusionListeners;
    private final Region mSystemGestureExclusionUnrestricted;
    private boolean mSystemGestureExclusionWasRestricted;
    private final Rect mSystemGestureFrameLeft;
    private final Rect mSystemGestureFrameRight;

    @VisibleForTesting
    final TaskTapPointerEventListener mTapDetector;
    final ArraySet<WindowState> mTapExcludeProvidingWindows;
    final ArrayList<WindowState> mTapExcludedWindows;
    private final Configuration mTempConfig;
    final WindowManagerPolicyConstants.PointerEventListener mTheiaPanicDetector;
    private final ApplySurfaceChangesTransactionState mTmpApplySurfaceChangesTransactionState;
    private final Configuration mTmpConfiguration;
    private final DisplayMetrics mTmpDisplayMetrics;
    private Point mTmpDisplaySize;
    private WindowState mTmpHoldScreenWindow;
    private boolean mTmpInitial;
    private final Rect mTmpRect;
    private final Rect mTmpRect2;
    private final Region mTmpRegion;
    private final TaskForResizePointSearchResult mTmpTaskForResizePointSearchResult;
    private final LinkedList<ActivityRecord> mTmpUpdateAllDrawn;
    private WindowState mTmpWindow;
    private final HashMap<IBinder, WindowToken> mTokenMap;
    private Region mTouchExcludeRegion;
    final UnknownAppVisibilityController mUnknownAppVisibilityController;
    private Set<Rect> mUnrestrictedKeepClearAreas;
    private boolean mUpdateImeRequestedWhileDeferred;
    private boolean mUpdateImeTarget;
    private final Consumer<WindowState> mUpdateWindowsForAnimator;
    boolean mWaitingForConfig;
    WallpaperController mWallpaperController;
    boolean mWallpaperMayChange;
    final ArrayList<WindowState> mWinAddedSinceNullFocus;
    final ArrayList<WindowState> mWinRemovedSinceNullFocus;
    private final float mWindowCornerRadius;
    private SurfaceControl mWindowingLayer;
    int pendingLayoutChanges;
    private static final InsetsState.OnTraverseCallbacks COPY_SOURCE_VISIBILITY = new InsetsState.OnTraverseCallbacks() { // from class: com.android.server.wm.DisplayContent.1
        public void onIdMatch(InsetsSource insetsSource, InsetsSource insetsSource2) {
            insetsSource.setVisible(insetsSource2.isVisible());
        }
    };
    private static IDisplayContentExt.IGestureStaticExt mStaticDisplayContentExt = (IDisplayContentExt.IGestureStaticExt) ExtLoader.type(IDisplayContentExt.IGestureStaticExt.class).create();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface ForceScalingMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    @interface InputMethodTarget {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean alwaysCreateRootTask(int i, int i2) {
        return ((i2 == 1 || i2 == 3) && (i == 1 || i == 5 || i == 100 || i == 2 || i == 6)) || i == 120;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getTopRootTask$18(Task task) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$removeImeSurfaceByTarget$28(WindowState windowState, Object obj) {
        return obj == windowState;
    }

    @Override // com.android.server.wm.WindowContainer
    DisplayContent asDisplayContent() {
        return this;
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    boolean fillsParent() {
        return true;
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    long getProtoFieldId() {
        return 1146756268035L;
    }

    @Override // com.android.server.wm.WindowContainer
    int getRelativeDisplayRotation() {
        return 0;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isVisible() {
        return true;
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.ConfigurationContainer
    public boolean providesMaxBounds() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRemoteInsetsControlTarget = null;
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(WindowState windowState) {
        WindowStateAnimator windowStateAnimator = windowState.mWinAnimator;
        ActivityRecord activityRecord = windowState.mActivityRecord;
        if (windowStateAnimator.mDrawState == 3) {
            if ((activityRecord == null || activityRecord.canShowWindows()) && windowState.performShowLocked()) {
                int i = this.pendingLayoutChanges | 8;
                this.pendingLayoutChanges = i;
                if (WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
                    this.mWmService.mWindowPlacerLocked.debugLayoutRepeats("updateWindowsAndWallpaperLocked 5", i);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(WindowState windowState) {
        int i = this.mTmpWindow.mOwnerUid;
        WindowManagerService.H h = this.mWmService.mH;
        if (windowState.mAttrs.type == 2005 && windowState.mOwnerUid == i && !h.hasMessages(52, windowState)) {
            h.sendMessageDelayed(h.obtainMessage(52, windowState), windowState.mAttrs.hideTimeoutMilliseconds);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$3(WindowState windowState) {
        WindowState windowState2;
        WindowState windowState3;
        ActivityRecord activityRecord = this.mFocusedApp;
        if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_FOCUS, -1142279614, 52, (String) null, new Object[]{String.valueOf(windowState), Long.valueOf(windowState.mAttrs.flags), Boolean.valueOf(windowState.canReceiveKeys()), String.valueOf(windowState.canReceiveKeysReason(false))});
        }
        if (!windowState.canReceiveKeys()) {
            return false;
        }
        if (windowState.mIsImWindow && windowState.isChildWindow() && ((windowState3 = this.mImeLayeringTarget) == null || !windowState3.isRequestedVisible(WindowInsets.Type.ime()))) {
            return false;
        }
        if (windowState.mAttrs.type == 2012 && ((windowState2 = this.mImeLayeringTarget) == null || (!windowState2.isRequestedVisible(WindowInsets.Type.ime()) && (!this.mImeLayeringTarget.isVisibleRequested() || (!this.mTransitionController.isShellTransitionsEnabled() ? !this.mImeLayeringTarget.isAnimating(3, 1) : !this.mImeLayeringTarget.inTransition()))))) {
            return false;
        }
        ActivityRecord activityRecord2 = windowState.mActivityRecord;
        if (activityRecord == null) {
            if (ProtoLogCache.WM_DEBUG_FOCUS_LIGHT_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -87705714, 0, (String) null, new Object[]{String.valueOf(windowState)});
            }
            this.mTmpWindow = windowState;
            return true;
        }
        if (!activityRecord.windowsAreFocusable()) {
            if (ProtoLogCache.WM_DEBUG_FOCUS_LIGHT_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, 1430336882, 0, (String) null, new Object[]{String.valueOf(windowState)});
            }
            this.mTmpWindow = windowState;
            return true;
        }
        if (activityRecord2 != null && windowState.mAttrs.type != 3) {
            if (activityRecord.compareTo((WindowContainer) activityRecord2) > 0) {
                if (ProtoLogCache.WM_DEBUG_FOCUS_LIGHT_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -809771899, 0, (String) null, new Object[]{String.valueOf(activityRecord)});
                }
                this.mTmpWindow = null;
                return true;
            }
            if (this.mDisplayContentExt.setFocusedAppToNormalWindow(activityRecord, windowState)) {
                return false;
            }
            if (activityRecord.getTask() != null && !activityRecord.getTask().getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0]) && windowState.getTask() != null && windowState.getTask().getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
                return false;
            }
            TaskFragment taskFragment = activityRecord2.getTaskFragment();
            if (taskFragment != null && taskFragment.isEmbedded() && activityRecord2.getTask() == activityRecord.getTask() && activityRecord2.getTaskFragment() != activityRecord.getTaskFragment()) {
                return false;
            }
        }
        if (ProtoLogCache.WM_DEBUG_FOCUS_LIGHT_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -415865166, 0, (String) null, new Object[]{String.valueOf(windowState)});
        }
        this.mTmpWindow = windowState;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(WindowState windowState) {
        if (windowState.mLayoutAttached) {
            return;
        }
        boolean isGoneForLayout = windowState.isGoneForLayout();
        if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
            Slog.v(TAG, "1ST PASS " + windowState + ": gone=" + isGoneForLayout + " mHaveFrame=" + windowState.mHaveFrame + " config reported=" + windowState.isLastConfigReportedToClient());
            ActivityRecord activityRecord = windowState.mActivityRecord;
            if (isGoneForLayout) {
                StringBuilder sb = new StringBuilder();
                sb.append("  GONE: mViewVisibility=");
                sb.append(windowState.mViewVisibility);
                sb.append(" mRelayoutCalled=");
                sb.append(windowState.mRelayoutCalled);
                sb.append(" visible=");
                sb.append(windowState.mToken.isVisible());
                sb.append(" visibleRequested=");
                sb.append(activityRecord != null && activityRecord.isVisibleRequested());
                sb.append(" parentHidden=");
                sb.append(windowState.isParentWindowHidden());
                Slog.v(TAG, sb.toString());
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("  VIS: mViewVisibility=");
                sb2.append(windowState.mViewVisibility);
                sb2.append(" mRelayoutCalled=");
                sb2.append(windowState.mRelayoutCalled);
                sb2.append(" visible=");
                sb2.append(windowState.mToken.isVisible());
                sb2.append(" visibleRequested=");
                sb2.append(activityRecord != null && activityRecord.isVisibleRequested());
                sb2.append(" parentHidden=");
                sb2.append(windowState.isParentWindowHidden());
                Slog.v(TAG, sb2.toString());
            }
        }
        if (isGoneForLayout && windowState.mHaveFrame && !windowState.mLayoutNeeded) {
            return;
        }
        this.mDisplayContentExt.hookPerformLayout(this, windowState, this.mCurrentFocus);
        if (this.mTmpInitial) {
            windowState.resetContentChanged();
        }
        windowState.mSurfacePlacementNeeded = true;
        windowState.mLayoutNeeded = false;
        boolean z = !windowState.isLaidOut();
        getDisplayPolicy().layoutWindowLw(windowState, null, this.mDisplayFrames);
        windowState.mLayoutSeq = this.mLayoutSeq;
        if (z && !windowState.mHasSurface) {
            if (!windowState.getFrame().isEmpty()) {
                windowState.updateLastFrames();
            }
            windowState.onResizeHandled();
        }
        if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
            Slog.v(TAG, "  LAYOUT: mFrame=" + windowState.getFrame() + " mParentFrame=" + windowState.getParentFrame() + " mDisplayFrame=" + windowState.getDisplayFrame());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(WindowState windowState) {
        this.mDisplayContentExt.mayAddFloatingWindow(windowState);
        if (windowState.mLayoutAttached) {
            if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
                Slog.v(TAG, "2ND PASS " + windowState + " mHaveFrame=" + windowState.mHaveFrame + " mViewVisibility=" + windowState.mViewVisibility + " mRelayoutCalled=" + windowState.mRelayoutCalled);
            }
            if ((windowState.mViewVisibility == 8 || !windowState.mRelayoutCalled) && windowState.mHaveFrame && !windowState.mLayoutNeeded) {
                return;
            }
            if (this.mTmpInitial) {
                windowState.resetContentChanged();
            }
            windowState.mSurfacePlacementNeeded = true;
            windowState.mLayoutNeeded = false;
            getDisplayPolicy().layoutWindowLw(windowState, windowState.getParentWindow(), this.mDisplayFrames);
            windowState.mLayoutSeq = this.mLayoutSeq;
            if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
                Slog.v(TAG, " LAYOUT: mFrame=" + windowState.getFrame() + " mParentFrame=" + windowState.getParentFrame() + " mDisplayFrame=" + windowState.getDisplayFrame());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$6(WindowState windowState) {
        if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD && this.mUpdateImeTarget) {
            Slog.i(TAG, "Checking window @" + windowState + " fl=0x" + Integer.toHexString(windowState.mAttrs.flags));
        }
        return windowState.canBeImeTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$7(WindowState windowState) {
        getDisplayPolicy().applyPostLayoutPolicyLw(windowState, windowState.mAttrs, windowState.getParentWindow(), this.mImeLayeringTarget);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8(WindowState windowState) {
        WindowManagerService windowManagerService = this.mWmService;
        WindowSurfacePlacer windowSurfacePlacer = windowManagerService.mWindowPlacerLocked;
        boolean z = windowState.mObscured;
        boolean z2 = this.mTmpApplySurfaceChangesTransactionState.obscured;
        boolean z3 = z != z2;
        RootWindowContainer rootWindowContainer = windowManagerService.mRoot;
        windowState.mObscured = z2;
        if (!z2) {
            boolean isDisplayed = windowState.isDisplayed();
            if (isDisplayed && windowState.isObscuringDisplay()) {
                this.mObscuringWindow = windowState;
                this.mTmpApplySurfaceChangesTransactionState.obscured = true;
            }
            ApplySurfaceChangesTransactionState applySurfaceChangesTransactionState = this.mTmpApplySurfaceChangesTransactionState;
            boolean handleNotObscuredLocked = rootWindowContainer.handleNotObscuredLocked(windowState, applySurfaceChangesTransactionState.obscured, applySurfaceChangesTransactionState.syswin);
            if (!this.mTmpApplySurfaceChangesTransactionState.displayHasContent && !getDisplayPolicy().isWindowExcludedFromContent(windowState)) {
                ApplySurfaceChangesTransactionState applySurfaceChangesTransactionState2 = this.mTmpApplySurfaceChangesTransactionState;
                applySurfaceChangesTransactionState2.displayHasContent = handleNotObscuredLocked | applySurfaceChangesTransactionState2.displayHasContent;
            }
            if (windowState.mHasSurface && isDisplayed) {
                if ((windowState.mAttrs.flags & 128) != 0) {
                    this.mTmpHoldScreenWindow = windowState;
                } else if (windowState == this.mLastWakeLockHoldingWindow && ProtoLogCache.WM_DEBUG_KEEP_SCREEN_ON_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, 191486492, 0, (String) null, new Object[]{String.valueOf(windowState), String.valueOf(Debug.getCallers(10))});
                }
                int i = windowState.mAttrs.type;
                if (i == 2008 || i == 2010 || (i == 2040 && this.mWmService.mPolicy.isKeyguardShowing())) {
                    this.mTmpApplySurfaceChangesTransactionState.syswin = true;
                }
                ApplySurfaceChangesTransactionState applySurfaceChangesTransactionState3 = this.mTmpApplySurfaceChangesTransactionState;
                if (applySurfaceChangesTransactionState3.preferredRefreshRate == INVALID_DPI) {
                    float f = windowState.mAttrs.preferredRefreshRate;
                    if (f != INVALID_DPI) {
                        applySurfaceChangesTransactionState3.preferredRefreshRate = f;
                    }
                }
                applySurfaceChangesTransactionState3.preferMinimalPostProcessing = applySurfaceChangesTransactionState3.preferMinimalPostProcessing | windowState.mAttrs.preferMinimalPostProcessing;
                applySurfaceChangesTransactionState3.disableHdrConversion |= !r8.isHdrConversionEnabled();
                int preferredModeId = getDisplayPolicy().getRefreshRatePolicy().getPreferredModeId(windowState);
                if (windowState.getWindowingMode() != 2) {
                    ApplySurfaceChangesTransactionState applySurfaceChangesTransactionState4 = this.mTmpApplySurfaceChangesTransactionState;
                    if (applySurfaceChangesTransactionState4.preferredModeId == 0 && preferredModeId != 0) {
                        applySurfaceChangesTransactionState4.preferredModeId = preferredModeId;
                    }
                }
                float preferredMinRefreshRate = getDisplayPolicy().getRefreshRatePolicy().getPreferredMinRefreshRate(windowState);
                ApplySurfaceChangesTransactionState applySurfaceChangesTransactionState5 = this.mTmpApplySurfaceChangesTransactionState;
                if (applySurfaceChangesTransactionState5.preferredMinRefreshRate == INVALID_DPI && preferredMinRefreshRate != INVALID_DPI) {
                    applySurfaceChangesTransactionState5.preferredMinRefreshRate = preferredMinRefreshRate;
                }
                float preferredMaxRefreshRate = getDisplayPolicy().getRefreshRatePolicy().getPreferredMaxRefreshRate(windowState);
                ApplySurfaceChangesTransactionState applySurfaceChangesTransactionState6 = this.mTmpApplySurfaceChangesTransactionState;
                if (applySurfaceChangesTransactionState6.preferredMaxRefreshRate == INVALID_DPI && preferredMaxRefreshRate != INVALID_DPI) {
                    applySurfaceChangesTransactionState6.preferredMaxRefreshRate = preferredMaxRefreshRate;
                }
                this.mDisplayContentExt.applyPreferredMode(windowState.getWrapper().getExtImpl(), windowState, windowState == getDisplayPolicy().getTopFullscreenOpaqueWindow(), preferredModeId, preferredMaxRefreshRate);
            }
        }
        if (z3 && windowState.isVisible() && this.mWallpaperController.isWallpaperTarget(windowState)) {
            this.mWallpaperController.updateWallpaperVisibility();
        }
        windowState.handleWindowMovedIfNeeded();
        WindowStateAnimator windowStateAnimator = windowState.mWinAnimator;
        windowState.resetContentChanged();
        if (windowState.mHasSurface) {
            boolean commitFinishDrawingLocked = windowStateAnimator.commitFinishDrawingLocked();
            if (this.isDefaultDisplay && commitFinishDrawingLocked && windowState.hasWallpaper()) {
                if (ProtoLogCache.WM_DEBUG_WALLPAPER_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WALLPAPER, 422634333, 0, (String) null, new Object[]{String.valueOf(windowState)});
                }
                this.mWallpaperMayChange = true;
                int i2 = this.pendingLayoutChanges | 4;
                this.pendingLayoutChanges = i2;
                if (WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
                    windowSurfacePlacer.debugLayoutRepeats("wallpaper and commitFinishDrawingLocked true", i2);
                }
            }
        }
        ActivityRecord activityRecord = windowState.mActivityRecord;
        if (activityRecord != null && activityRecord.isVisibleRequested()) {
            activityRecord.updateLetterboxSurface(windowState);
            if (activityRecord.updateDrawnWindowStates(windowState) && !this.mTmpUpdateAllDrawn.contains(activityRecord)) {
                this.mTmpUpdateAllDrawn.add(activityRecord);
            }
        }
        windowState.updateResizingWindowIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayContent(Display display, RootWindowContainer rootWindowContainer, DeviceStateController deviceStateController) {
        super(rootWindowContainer.mWindowManager, "DisplayContent", 0);
        this.mMinSizeOfResizeableTaskDp = -1;
        this.mImeWindowsContainer = new ImeContainer(this.mWmService);
        this.mMaxUiWidth = 0;
        this.mSkipAppTransitionAnimation = false;
        this.mOpeningApps = new ArraySet<>();
        this.mClosingApps = new ArraySet<>();
        this.mChangingContainers = new ArraySet<>();
        this.mClosingChangingContainers = new ArrayMap<>();
        this.mNoAnimationNotifyOnTransitionFinished = new ArrayList();
        this.mTokenMap = new HashMap<>();
        this.mInitialDisplayWidth = 0;
        this.mInitialDisplayHeight = 0;
        this.mInitialPhysicalXDpi = INVALID_DPI;
        this.mInitialPhysicalYDpi = INVALID_DPI;
        this.mInitialDisplayDensity = 0;
        this.mDisplayCutoutCache = new RotationCache<>(new RotationCache.RotationDependentComputation() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda3
            @Override // com.android.server.wm.utils.RotationCache.RotationDependentComputation
            public final Object compute(Object obj, int i) {
                WmDisplayCutout calculateDisplayCutoutForRotationUncached;
                calculateDisplayCutoutForRotationUncached = DisplayContent.this.calculateDisplayCutoutForRotationUncached((DisplayCutout) obj, i);
                return calculateDisplayCutoutForRotationUncached;
            }
        });
        this.mRoundedCornerCache = new RotationCache<>(new RotationCache.RotationDependentComputation() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda8
            @Override // com.android.server.wm.utils.RotationCache.RotationDependentComputation
            public final Object compute(Object obj, int i) {
                RoundedCorners calculateRoundedCornersForRotationUncached;
                calculateRoundedCornersForRotationUncached = DisplayContent.this.calculateRoundedCornersForRotationUncached((RoundedCorners) obj, i);
                return calculateRoundedCornersForRotationUncached;
            }
        });
        this.mCurrentPrivacyIndicatorBounds = new PrivacyIndicatorBounds();
        this.mPrivacyIndicatorBoundsCache = new RotationCache<>(new RotationCache.RotationDependentComputation() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda9
            @Override // com.android.server.wm.utils.RotationCache.RotationDependentComputation
            public final Object compute(Object obj, int i) {
                PrivacyIndicatorBounds calculatePrivacyIndicatorBoundsForRotationUncached;
                calculatePrivacyIndicatorBoundsForRotationUncached = DisplayContent.this.calculatePrivacyIndicatorBoundsForRotationUncached((PrivacyIndicatorBounds) obj, i);
                return calculatePrivacyIndicatorBoundsForRotationUncached;
            }
        });
        this.mDisplayShapeCache = new RotationCache<>(new RotationCache.RotationDependentComputation() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda10
            @Override // com.android.server.wm.utils.RotationCache.RotationDependentComputation
            public final Object compute(Object obj, int i) {
                DisplayShape calculateDisplayShapeForRotationUncached;
                calculateDisplayShapeForRotationUncached = DisplayContent.this.calculateDisplayShapeForRotationUncached((DisplayShape) obj, i);
                return calculateDisplayShapeForRotationUncached;
            }
        });
        this.mBaseDisplayWidth = 0;
        this.mBaseDisplayHeight = 0;
        this.mIsSizeForced = false;
        this.mSandboxDisplayApis = true;
        this.mBaseDisplayDensity = 0;
        this.mIsDensityForced = false;
        this.mBaseDisplayPhysicalXDpi = INVALID_DPI;
        this.mBaseDisplayPhysicalYDpi = INVALID_DPI;
        DisplayInfo displayInfo = new DisplayInfo();
        this.mDisplayInfo = displayInfo;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mDisplayMetrics = displayMetrics;
        this.mSystemGestureExclusionListeners = new RemoteCallbackList<>();
        this.mSystemGestureExclusion = new Region();
        this.mSystemGestureExclusionWasRestricted = false;
        this.mSystemGestureExclusionUnrestricted = new Region();
        this.mSystemGestureFrameLeft = new Rect();
        this.mSystemGestureFrameRight = new Rect();
        this.mRestrictedKeepClearAreas = new ArraySet();
        this.mUnrestrictedKeepClearAreas = new ArraySet();
        this.mRealDisplayMetrics = new DisplayMetrics();
        this.mTmpDisplayMetrics = new DisplayMetrics();
        this.mCompatDisplayMetrics = new DisplayMetrics();
        this.mLastWallpaperVisible = false;
        this.mTouchExcludeRegion = new Region();
        this.mTmpRect = new Rect();
        this.mTmpRect2 = new Rect();
        this.mTmpRegion = new Region();
        this.mTmpConfiguration = new Configuration();
        this.mTapExcludedWindows = new ArrayList<>();
        this.mTapExcludeProvidingWindows = new ArraySet<>();
        this.mTmpUpdateAllDrawn = new LinkedList<>();
        this.mTmpTaskForResizePointSearchResult = new TaskForResizePointSearchResult();
        this.mTmpApplySurfaceChangesTransactionState = new ApplySurfaceChangesTransactionState();
        this.mDisplayReady = false;
        this.mWallpaperMayChange = false;
        this.mSession = new SurfaceSession();
        this.mCurrentFocus = null;
        this.mFocusedApp = null;
        this.mOrientationRequestingTaskDisplayArea = null;
        FixedRotationTransitionListener fixedRotationTransitionListener = new FixedRotationTransitionListener();
        this.mFixedRotationTransitionListener = fixedRotationTransitionListener;
        this.mWinAddedSinceNullFocus = new ArrayList<>();
        this.mWinRemovedSinceNullFocus = new ArrayList<>();
        this.mLayoutSeq = 0;
        this.mDisplayContentWrapper = new DisplayContentWrapper();
        this.mDisplayContentExt = (IDisplayContentExt) ExtLoader.type(IDisplayContentExt.class).base(this).create();
        this.mNonStaticExt = (INonStaticDisplayContentExt) ExtLoader.type(INonStaticDisplayContentExt.class).base(this).create();
        this.mShellRoots = new SparseArray<>();
        this.mRemoteInsetsControlTarget = null;
        this.mRemoteInsetsDeath = new IBinder.DeathRecipient() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda11
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                DisplayContent.this.lambda$new$0();
            }
        };
        this.mDisplayAccessUIDs = new IntArray();
        this.mAllSleepTokens = new ArrayList<>();
        this.mActiveSizeCompatActivities = new ArraySet();
        this.mTmpDisplaySize = new Point();
        this.mTempConfig = new Configuration();
        this.mInEnsureActivitiesVisible = false;
        WindowManagerPolicyConstants.PointerEventListener pointerEventListener = new WindowManagerPolicyConstants.PointerEventListener() { // from class: com.android.server.wm.DisplayContent.2
            public void onPointerEvent(MotionEvent motionEvent) {
                int i;
                if (motionEvent.getActionMasked() == 0) {
                    WindowState windowState = DisplayContent.this.mInputMethodWindow;
                    if (windowState == null || !windowState.isVisible()) {
                        DisplayContent displayContent = DisplayContent.this;
                        WindowState windowState2 = displayContent.mCurrentFocus;
                        if (windowState2 == null || !((i = windowState2.mWinAnimator.mDrawState) == 3 || i == 4)) {
                            displayContent.mDisplayContentExt.onPointerEventForTheia(motionEvent);
                        }
                    }
                }
            }
        };
        this.mTheiaPanicDetector = pointerEventListener;
        this.mUpdateWindowsForAnimator = new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.this.lambda$new$1((WindowState) obj);
            }
        };
        this.mScheduleToastTimeout = new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.this.lambda$new$2((WindowState) obj);
            }
        };
        this.mFindFocusedWindow = new ToBooleanFunction() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda14
            public final boolean apply(Object obj) {
                boolean lambda$new$3;
                lambda$new$3 = DisplayContent.this.lambda$new$3((WindowState) obj);
                return lambda$new$3;
            }
        };
        this.mPerformLayout = new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.this.lambda$new$4((WindowState) obj);
            }
        };
        this.mPerformLayoutAttached = new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.this.lambda$new$5((WindowState) obj);
            }
        };
        this.mComputeImeTargetPredicate = new Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$new$6;
                lambda$new$6 = DisplayContent.this.lambda$new$6((WindowState) obj);
                return lambda$new$6;
            }
        };
        this.mApplyPostLayoutPolicy = new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.this.lambda$new$7((WindowState) obj);
            }
        };
        this.mApplySurfaceChangesTransaction = new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.this.lambda$new$8((WindowState) obj);
            }
        };
        if (this.mWmService.mRoot.getDisplayContent(display.getDisplayId()) != null) {
            throw new IllegalArgumentException("Display with ID=" + display.getDisplayId() + " already exists=" + this.mWmService.mRoot.getDisplayContent(display.getDisplayId()) + " new=" + display);
        }
        this.mRootWindowContainer = rootWindowContainer;
        this.mAtmService = this.mWmService.mAtmService;
        this.mDisplay = display;
        int displayId = display.getDisplayId();
        this.mDisplayId = displayId;
        this.mCurrentUniqueDisplayId = display.getUniqueId();
        this.mOffTokenAcquirer = this.mRootWindowContainer.mDisplayOffTokenAcquirer;
        WallpaperController wallpaperController = new WallpaperController(this.mWmService, this);
        this.mWallpaperController = wallpaperController;
        wallpaperController.resetLargestDisplay(display);
        display.getDisplayInfo(displayInfo);
        display.getMetrics(displayMetrics);
        this.mSystemGestureExclusionLimit = (this.mDisplayContentExt.adjustConstantSystemGestureExclusionLimitDp(this.mWmService.mConstants.mSystemGestureExclusionLimitDp, this) * displayMetrics.densityDpi) / 160;
        this.isDefaultDisplay = displayId == 0;
        InsetsStateController insetsStateController = new InsetsStateController(this);
        this.mInsetsStateController = insetsStateController;
        initializeDisplayBaseInfo();
        this.mDisplayFrames = new DisplayFrames(insetsStateController.getRawInsetsState(), displayInfo, calculateDisplayCutoutForRotation(displayInfo.rotation), calculateRoundedCornersForRotation(displayInfo.rotation), calculatePrivacyIndicatorBoundsForRotation(displayInfo.rotation), calculateDisplayShapeForRotation(displayInfo.rotation));
        PowerManager.WakeLock newWakeLock = this.mWmService.mPowerManager.newWakeLock(536870922, "WindowManager/displayId:" + displayId, displayId);
        this.mHoldScreenWakeLock = newWakeLock;
        newWakeLock.setReferenceCounted(false);
        WindowManagerService windowManagerService = this.mWmService;
        AppTransition appTransition = new AppTransition(windowManagerService.mContext, windowManagerService, this);
        this.mAppTransition = appTransition;
        appTransition.registerListenerLocked(this.mWmService.mActivityManagerAppTransitionNotifier);
        appTransition.registerListenerLocked(fixedRotationTransitionListener);
        this.mAppTransitionController = new AppTransitionController(this.mWmService, this);
        this.mTransitionController.registerLegacyListener(fixedRotationTransitionListener);
        this.mUnknownAppVisibilityController = new UnknownAppVisibilityController(this.mWmService, this);
        this.mDisplaySwitchTransitionLauncher = new PhysicalDisplaySwitchTransitionLauncher(this, this.mTransitionController);
        this.mRemoteDisplayChangeController = new RemoteDisplayChangeController(this.mWmService, displayId);
        this.mPointerEventDispatcher = new PointerEventDispatcher(this.mWmService.mInputManager.monitorInput("PointerEventDispatcher" + displayId, displayId));
        TaskTapPointerEventListener taskTapPointerEventListener = new TaskTapPointerEventListener(this.mWmService, this);
        this.mTapDetector = taskTapPointerEventListener;
        registerPointerEventListener(taskTapPointerEventListener);
        registerPointerEventListener(this.mWmService.mMousePositionTracker);
        if (this.mWmService.mAtmService.getRecentTasks() != null) {
            registerPointerEventListener(this.mWmService.mAtmService.getRecentTasks().getInputListener());
        }
        this.mDeviceStateController = deviceStateController;
        if (!this.mDisplayContentExt.isCommercialVersion()) {
            registerPointerEventListener(pointerEventListener);
        }
        DisplayPolicy createDisplayPolicy = this.mDisplayContentExt.createDisplayPolicy(this.mWmService, this);
        this.mDisplayPolicy = createDisplayPolicy;
        this.mDisplayRotation = new DisplayRotation(this.mWmService, this, displayInfo.address, deviceStateController, rootWindowContainer.getDisplayRotationCoordinator());
        Consumer<DeviceStateController.DeviceState> consumer = new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.this.lambda$new$9((DeviceStateController.DeviceState) obj);
            }
        };
        this.mDeviceStateConsumer = consumer;
        deviceStateController.registerDeviceStateCallback(consumer, new HandlerExecutor(this.mWmService.mH));
        this.mCloseToSquareMaxAspectRatio = this.mWmService.mContext.getResources().getFloat(R.dimen.config_prefDialogWidth);
        if (this.isDefaultDisplay) {
            this.mWmService.mPolicy.setDefaultDisplay(this);
        }
        this.mDisplayContentExt.setSecondDefaultDisplay(this, this.mWmService);
        if (this.mWmService.mDisplayReady) {
            createDisplayPolicy.onConfigurationChanged();
        }
        if (this.mWmService.mSystemReady) {
            createDisplayPolicy.systemReady();
        }
        this.mWindowCornerRadius = createDisplayPolicy.getWindowCornerRadius();
        this.mPinnedTaskController = new PinnedTaskController(this.mWmService, this);
        SurfaceControl.Transaction pendingTransaction = getPendingTransaction();
        configureSurfaces(pendingTransaction);
        pendingTransaction.apply();
        onDisplayChanged(this);
        updateDisplayAreaOrganizers();
        this.mDisplayRotationCompatPolicy = this.mWmService.mLetterboxConfiguration.isCameraCompatTreatmentEnabled(false) ? new DisplayRotationCompatPolicy(this) : null;
        this.mRotationReversionController = new DisplayRotationReversionController(this);
        this.mInputMonitor = new InputMonitor(this.mWmService, this);
        this.mInsetsPolicy = new InsetsPolicy(insetsStateController, this);
        this.mMinSizeOfResizeableTaskDp = getMinimalTaskSizeDp();
        if (WindowManagerDebugConfig.DEBUG_DISPLAY) {
            Slog.v(TAG, "Creating display=" + display);
        }
        setWindowingMode(1);
        this.mWmService.mDisplayWindowSettings.applySettingsToDisplayLocked(this);
        boolean z = this.mWmService.mContext.getResources().getBoolean(R.bool.kg_share_status_area);
        this.mInTouchMode = z;
        this.mWmService.mInputManager.setInTouchMode(z, WindowManagerService.MY_PID, WindowManagerService.MY_UID, true, displayId);
        this.mDisplayContentExt.initOplusRefreshRatePolicy(new Object[]{this.mWmService.mContext, this, displayInfo});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$9(DeviceStateController.DeviceState deviceState) {
        this.mDisplaySwitchTransitionLauncher.foldStateChanged(deviceState);
        this.mDisplayRotation.foldStateChanged(deviceState);
    }

    private void beginHoldScreenUpdate() {
        this.mTmpHoldScreenWindow = null;
        this.mObscuringWindow = null;
    }

    private void finishHoldScreenUpdate() {
        WindowState windowState = this.mTmpHoldScreenWindow;
        boolean z = windowState != null;
        if (z && windowState != this.mHoldScreenWindow) {
            PowerManager.WakeLock wakeLock = this.mHoldScreenWakeLock;
            Session session = this.mTmpHoldScreenWindow.mSession;
            wakeLock.setWorkSource(new WorkSource(session.mUid, session.mPackageName));
        }
        this.mHoldScreenWindow = this.mTmpHoldScreenWindow;
        this.mTmpHoldScreenWindow = null;
        if (z != this.mHoldScreenWakeLock.isHeld()) {
            if (z) {
                if (ProtoLogCache.WM_DEBUG_KEEP_SCREEN_ON_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, -384639879, 0, (String) null, new Object[]{String.valueOf(this.mHoldScreenWindow)});
                }
                this.mLastWakeLockHoldingWindow = this.mHoldScreenWindow;
                this.mLastWakeLockObscuringWindow = null;
                this.mHoldScreenWakeLock.acquire();
                return;
            }
            if (ProtoLogCache.WM_DEBUG_KEEP_SCREEN_ON_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, 782864973, 0, (String) null, new Object[]{String.valueOf(this.mObscuringWindow)});
            }
            this.mLastWakeLockHoldingWindow = null;
            this.mLastWakeLockObscuringWindow = this.mObscuringWindow;
            this.mHoldScreenWakeLock.release();
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void migrateToNewSurfaceControl(SurfaceControl.Transaction transaction) {
        transaction.remove(this.mSurfaceControl);
        this.mLastSurfacePosition.set(0, 0);
        this.mLastDeltaRotation = 0;
        configureSurfaces(transaction);
        for (int i = 0; i < this.mChildren.size(); i++) {
            SurfaceControl surfaceControl = ((DisplayArea) this.mChildren.get(i)).getSurfaceControl();
            if (surfaceControl != null) {
                transaction.reparent(surfaceControl, this.mSurfaceControl);
            }
        }
        scheduleAnimation();
    }

    private void configureSurfaces(SurfaceControl.Transaction transaction) {
        SurfaceControl.Builder callsite = this.mWmService.makeSurfaceBuilder(this.mSession).setOpaque(true).setContainerLayer().setCallsite("DisplayContent");
        this.mSurfaceControl = callsite.setName(getName()).setContainerLayer().build();
        if (this.mDisplayAreaPolicy == null) {
            this.mDisplayAreaPolicy = this.mWmService.getDisplayAreaPolicyProvider().instantiate(this.mWmService, this, this, this.mImeWindowsContainer);
        }
        List<DisplayArea<? extends WindowContainer>> displayAreas = this.mDisplayAreaPolicy.getDisplayAreas(4);
        DisplayArea<? extends WindowContainer> displayArea = displayAreas.size() == 1 ? displayAreas.get(0) : null;
        if (displayArea != null && displayArea.getParent() == this) {
            SurfaceControl surfaceControl = displayArea.mSurfaceControl;
            this.mWindowingLayer = surfaceControl;
            transaction.reparent(surfaceControl, this.mSurfaceControl);
        } else {
            this.mWindowingLayer = this.mSurfaceControl;
            SurfaceControl build = callsite.setName("RootWrapper").build();
            this.mSurfaceControl = build;
            transaction.reparent(this.mWindowingLayer, build).show(this.mWindowingLayer);
        }
        SurfaceControl surfaceControl2 = this.mOverlayLayer;
        if (surfaceControl2 == null) {
            this.mOverlayLayer = callsite.setName("Display Overlays").setParent(this.mSurfaceControl).build();
        } else {
            transaction.reparent(surfaceControl2, this.mSurfaceControl);
        }
        SurfaceControl surfaceControl3 = this.mInputOverlayLayer;
        if (surfaceControl3 == null) {
            this.mInputOverlayLayer = callsite.setName("Input Overlays").setParent(this.mSurfaceControl).build();
        } else {
            transaction.reparent(surfaceControl3, this.mSurfaceControl);
        }
        SurfaceControl surfaceControl4 = this.mA11yOverlayLayer;
        if (surfaceControl4 == null) {
            this.mA11yOverlayLayer = callsite.setName("Accessibility Overlays").setParent(this.mSurfaceControl).build();
        } else {
            transaction.reparent(surfaceControl4, this.mSurfaceControl);
        }
        transaction.setLayer(this.mSurfaceControl, 0).setLayerStack(this.mSurfaceControl, this.mDisplayId).show(this.mSurfaceControl).setLayer(this.mOverlayLayer, Integer.MAX_VALUE).show(this.mOverlayLayer).setLayer(this.mInputOverlayLayer, 2147483646).show(this.mInputOverlayLayer).setLayer(this.mA11yOverlayLayer, 2147483645).show(this.mA11yOverlayLayer);
        this.mDisplayContentExt.initOplusRefreshRatePolicy(new Object[]{this.mWmService.mContext, this, this.mDisplayInfo});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayRotationReversionController getRotationReversionController() {
        return this.mRotationReversionController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isReady() {
        return this.mWmService.mDisplayReady && this.mDisplayReady;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setInTouchMode(boolean z) {
        if (this.mInTouchMode == z) {
            return false;
        }
        this.mInTouchMode = z;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInTouchMode() {
        return this.mInTouchMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDisplayId() {
        return this.mDisplayId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getWindowCornerRadius() {
        return this.mWindowCornerRadius;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowToken getWindowToken(IBinder iBinder) {
        return this.mTokenMap.get(iBinder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord getActivityRecord(IBinder iBinder) {
        WindowToken windowToken = getWindowToken(iBinder);
        if (windowToken == null) {
            return null;
        }
        return windowToken.asActivityRecord();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addWindowToken(IBinder iBinder, WindowToken windowToken) {
        DisplayContent windowTokenDisplay = this.mWmService.mRoot.getWindowTokenDisplay(windowToken);
        if (windowTokenDisplay != null) {
            throw new IllegalArgumentException("Can't map token=" + windowToken + " to display=" + getName() + " already mapped to display=" + windowTokenDisplay + " tokens=" + windowTokenDisplay.mTokenMap);
        }
        if (iBinder == null) {
            throw new IllegalArgumentException("Can't map token=" + windowToken + " to display=" + getName() + " binder is null");
        }
        if (windowToken == null) {
            throw new IllegalArgumentException("Can't map null token to display=" + getName() + " binder=" + iBinder);
        }
        this.mTokenMap.put(iBinder, windowToken);
        if (windowToken.asActivityRecord() == null) {
            windowToken.mDisplayContent = this;
            findAreaForToken(windowToken).asTokens().addChild(windowToken);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowToken removeWindowToken(IBinder iBinder, boolean z) {
        WindowToken remove = this.mTokenMap.remove(iBinder);
        if (remove != null && remove.asActivityRecord() == null) {
            remove.setExiting(z);
        }
        return remove;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl addShellRoot(IWindow iWindow, int i) {
        ShellRoot shellRoot = this.mShellRoots.get(i);
        if (shellRoot != null) {
            if (shellRoot.getClient() == iWindow) {
                return shellRoot.getSurfaceControl();
            }
            shellRoot.clear();
            this.mShellRoots.remove(i);
        }
        ShellRoot shellRoot2 = new ShellRoot(iWindow, this, i);
        SurfaceControl surfaceControl = shellRoot2.getSurfaceControl();
        if (surfaceControl == null) {
            shellRoot2.clear();
            return null;
        }
        this.mShellRoots.put(i, shellRoot2);
        return new SurfaceControl(surfaceControl, "DisplayContent.addShellRoot");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeShellRoot(int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ShellRoot shellRoot = this.mShellRoots.get(i);
                if (shellRoot == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                shellRoot.clear();
                this.mShellRoots.remove(i);
                WindowManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRemoteInsetsController(IDisplayWindowInsetsController iDisplayWindowInsetsController) {
        RemoteInsetsControlTarget remoteInsetsControlTarget = this.mRemoteInsetsControlTarget;
        if (remoteInsetsControlTarget != null) {
            remoteInsetsControlTarget.mRemoteInsetsController.asBinder().unlinkToDeath(this.mRemoteInsetsDeath, 0);
            this.mRemoteInsetsControlTarget = null;
        }
        if (iDisplayWindowInsetsController != null) {
            try {
                iDisplayWindowInsetsController.asBinder().linkToDeath(this.mRemoteInsetsDeath, 0);
                this.mRemoteInsetsControlTarget = new RemoteInsetsControlTarget(iDisplayWindowInsetsController);
            } catch (RemoteException unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reParentWindowToken(WindowToken windowToken) {
        DisplayContent displayContent = windowToken.getDisplayContent();
        if (displayContent == this) {
            return;
        }
        if (displayContent != null && displayContent.mTokenMap.remove(windowToken.token) != null && windowToken.asActivityRecord() == null) {
            windowToken.getParent().removeChild(windowToken);
        }
        addWindowToken(windowToken.token, windowToken);
        if (this.mWmService.mAccessibilityController.hasCallbacks()) {
            this.mWmService.mAccessibilityController.onSomeWindowResizedOrMoved(displayContent != null ? displayContent.getDisplayId() : -1, getDisplayId());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeAppToken(IBinder iBinder) {
        WindowToken removeWindowToken = removeWindowToken(iBinder, true);
        if (removeWindowToken == null) {
            Slog.w(TAG, "removeAppToken: Attempted to remove non-existing token: " + iBinder);
            return;
        }
        ActivityRecord asActivityRecord = removeWindowToken.asActivityRecord();
        if (asActivityRecord == null) {
            Slog.w(TAG, "Attempted to remove non-App token: " + iBinder + " token=" + removeWindowToken);
            return;
        }
        asActivityRecord.onRemovedFromDisplay();
        if (asActivityRecord == this.mFixedRotationLaunchingApp) {
            asActivityRecord.finishFixedRotationTransform();
            setFixedRotationLaunchingAppUnchecked(null);
        }
    }

    public Display getDisplay() {
        return this.mDisplay;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayInfo getDisplayInfo() {
        return this.mDisplayInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayMetrics getDisplayMetrics() {
        return this.mDisplayMetrics;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayPolicy getDisplayPolicy() {
        return this.mDisplayPolicy;
    }

    public DisplayRotation getDisplayRotation() {
        return this.mDisplayRotation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsStateController getInsetsStateController() {
        return this.mInsetsStateController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsPolicy getInsetsPolicy() {
        return this.mInsetsPolicy;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRotation() {
        return this.mDisplayRotation.getRotation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLastOrientation() {
        return this.mDisplayRotation.getLastOrientation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerRemoteAnimations(RemoteAnimationDefinition remoteAnimationDefinition) {
        this.mAppTransitionController.registerRemoteAnimations(remoteAnimationDefinition);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reconfigureDisplayLocked() {
        boolean updateOrientation;
        if (isReady()) {
            configureDisplayPolicy();
            setLayoutNeeded();
            if (this.isDefaultDisplay && this.mDisplayContentExt.waitingPhysicalDisplayChanged(this)) {
                Slog.d(TAG, "force update orientation when device folding.");
                updateOrientation = updateOrientation(true);
            } else {
                updateOrientation = updateOrientation();
            }
            Configuration configuration = getConfiguration();
            this.mTmpConfiguration.setTo(configuration);
            computeScreenConfiguration(this.mTmpConfiguration);
            int diff = configuration.diff(this.mTmpConfiguration);
            if (updateOrientation | (diff != 0)) {
                this.mWaitingForConfig = true;
                if (this.mTransitionController.isShellTransitionsEnabled()) {
                    TransitionRequestInfo.DisplayChange displayChange = this.mTransitionController.isCollecting() ? null : new TransitionRequestInfo.DisplayChange(this.mDisplayId);
                    if (displayChange != null) {
                        displayChange.setStartAbsBounds(configuration.windowConfiguration.getBounds());
                        displayChange.setEndAbsBounds(this.mTmpConfiguration.windowConfiguration.getBounds());
                    }
                    requestChangeTransitionIfNeeded(diff, displayChange);
                } else if (this.mLastHasContent) {
                    this.mWmService.startFreezingDisplay(0, 0, this);
                }
                forAllActivities(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda52
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DisplayContent.lambda$reconfigureDisplayLocked$10((ActivityRecord) obj);
                    }
                });
                sendNewConfiguration();
                getWrapper().getExtImpl().scheduleFoldableDeviceDisplaySwitch(this.isDefaultDisplay, this.mAtmService, this.mWmService, this.mDisplayInfo.address);
            }
            this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$reconfigureDisplayLocked$10(ActivityRecord activityRecord) {
        if (activityRecord.getWindowingMode() == 1) {
            activityRecord.clearSizeCompatModeIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendNewConfiguration() {
        if (isReady() && !this.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange()) {
            if (updateDisplayOverrideConfigurationLocked()) {
                if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                    Slog.d(TAG, "sendNewConfiguration: finished configUpdated.");
                    return;
                }
                return;
            }
            clearFixedRotationLaunchingApp();
            if (this.mWaitingForConfig) {
                this.mWaitingForConfig = false;
                this.mWmService.mLastFinishedFreezeSource = "config-unchanged";
                setLayoutNeeded();
                this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
            }
        }
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    boolean onDescendantOrientationChanged(WindowContainer windowContainer) {
        Configuration updateOrientation = updateOrientation(windowContainer, false);
        boolean handlesOrientationChangeFromDescendant = handlesOrientationChangeFromDescendant(windowContainer != null ? windowContainer.getOverrideOrientation() : -2);
        if (updateOrientation == null) {
            return handlesOrientationChangeFromDescendant;
        }
        if (handlesOrientationChangeFromDescendant && (windowContainer instanceof ActivityRecord)) {
            ActivityRecord activityRecord = (ActivityRecord) windowContainer;
            boolean updateDisplayOverrideConfigurationLocked = updateDisplayOverrideConfigurationLocked(updateOrientation, activityRecord, false, null);
            activityRecord.frozenBeforeDestroy = true;
            if (!updateDisplayOverrideConfigurationLocked) {
                this.mRootWindowContainer.resumeFocusedTasksTopActivities();
            }
        } else {
            updateDisplayOverrideConfigurationLocked(updateOrientation, null, false, null);
        }
        return handlesOrientationChangeFromDescendant;
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    boolean handlesOrientationChangeFromDescendant(int i) {
        return (shouldIgnoreOrientationRequest(i) || getDisplayRotation().isFixedToUserRotation()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateOrientation() {
        return updateOrientation(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Configuration updateOrientation(WindowContainer<?> windowContainer, boolean z) {
        ActivityRecord asActivityRecord;
        if (!this.mDisplayReady || !updateOrientation(z)) {
            return null;
        }
        if (windowContainer != null && !this.mWmService.mRoot.mOrientationChangeComplete && (asActivityRecord = windowContainer.asActivityRecord()) != null && asActivityRecord.mayFreezeScreenLocked()) {
            asActivityRecord.startFreezingScreen();
        }
        Configuration configuration = new Configuration();
        computeScreenConfiguration(configuration);
        return configuration;
    }

    private int getMinimalTaskSizeDp() {
        Resources resources = this.mAtmService.mContext.createConfigurationContext(getConfiguration()).getResources();
        TypedValue typedValue = new TypedValue();
        resources.getValue(R.dimen.floating_toolbar_menu_image_button_width, typedValue, true);
        int i = typedValue.data;
        int i2 = (i >> 0) & 15;
        if (typedValue.type != 5 || i2 != 1) {
            throw new IllegalArgumentException("Resource ID #0x" + Integer.toHexString(R.dimen.floating_toolbar_menu_image_button_width) + " is not in valid type or unit");
        }
        return (int) TypedValue.complexToFloat(i);
    }

    private boolean updateOrientation(boolean z) {
        WindowContainer windowContainer = this.mLastOrientationSource;
        int orientation = getOrientation();
        WindowContainer lastOrientationSource = getLastOrientationSource();
        if (lastOrientationSource != windowContainer && this.mRotationReversionController.isRotationReversionEnabled()) {
            this.mRotationReversionController.updateForNoSensorOverride();
        }
        ActivityRecord asActivityRecord = lastOrientationSource != null ? lastOrientationSource.asActivityRecord() : null;
        if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
            Slog.d(TAG, "updateOrientation: orientation = " + ActivityInfo.screenOrientationToString(orientation) + "(" + orientation + ") is request by orientationSource = " + lastOrientationSource);
        }
        if (asActivityRecord != null) {
            if (this.mDisplayContentExt.shouldFixOrientationForSplashScreen(asActivityRecord)) {
                return false;
            }
            Task task = asActivityRecord.getTask();
            if (task != null && orientation != task.mLastReportedRequestedOrientation) {
                task.mLastReportedRequestedOrientation = orientation;
                this.mAtmService.getTaskChangeNotificationController().notifyTaskRequestedOrientationChanged(task.mTaskId, orientation);
            }
            ActivityRecord activityRecord = !asActivityRecord.isVisibleRequested() ? topRunningActivity() : asActivityRecord;
            if (!this.mDisplayContentExt.waitingPhysicalDisplayChanged(this) && activityRecord != null && handleTopActivityLaunchingInDifferentOrientation(activityRecord, asActivityRecord, true)) {
                this.mDisplayContentExt.checkSetFixedRotationLaunchingApp(this, activityRecord);
                Slog.d(TAG, "updateOrientation: stop for launching different orientation app. topCandidate = " + activityRecord + " hasFixedRotationTransform = " + activityRecord.hasFixedRotationTransform() + " rotation = " + activityRecord.getWindowConfiguration().getRotation() + " r = " + asActivityRecord + " hasFixedRotationTransform = " + asActivityRecord.hasFixedRotationTransform() + " rotation = " + asActivityRecord.getWindowConfiguration().getRotation() + " curRotation = " + getRotation());
                return false;
            }
        }
        if (this.mDisplayContentExt.shouldBlockUpdateOrientationDuringFixedRotation(lastOrientationSource, this)) {
            Slog.d(TAG, "updateOrientation: stop for fingerprint window during fixed rotation or waiting remote animation.");
            return false;
        }
        return this.mDisplayRotation.updateOrientation(orientation, z);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isSyncFinished(BLASTSyncEngine.SyncGroup syncGroup) {
        return !this.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int rotationForActivityInDifferentOrientation(ActivityRecord activityRecord) {
        int rotation;
        int rotationForOrientation;
        ActivityRecord activity;
        if (this.mTransitionController.useShellTransitionsRotation()) {
            return -1;
        }
        int overrideOrientation = activityRecord.getOverrideOrientation();
        if (WindowManagerService.ENABLE_FIXED_ROTATION_TRANSFORM && !shouldIgnoreOrientationRequest(overrideOrientation)) {
            if (overrideOrientation == 3 && (activity = getActivity(new Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda28
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean canDefineOrientationForActivitiesAbove;
                    canDefineOrientationForActivitiesAbove = ((ActivityRecord) obj).canDefineOrientationForActivitiesAbove();
                    return canDefineOrientationForActivitiesAbove;
                }
            }, activityRecord, false, true)) != null) {
                activityRecord = activity;
            }
            if (!activityRecord.inMultiWindowMode() && activityRecord.getRequestedConfigurationOrientation(true) != getConfiguration().orientation) {
                if ((activityRecord.getTask() == null || !activityRecord.getTask().getWrapper().getExtImpl().shouldSkipRotationForFlexibleWindow()) && (rotationForOrientation = this.mDisplayRotation.rotationForOrientation(this.mDisplayContentExt.getFixedScreenOrientation(activityRecord, activityRecord.getRequestedOrientation()), (rotation = getRotation()))) != rotation) {
                    return rotationForOrientation;
                }
                return -1;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean handleTopActivityLaunchingInDifferentOrientation(ActivityRecord activityRecord, boolean z) {
        return handleTopActivityLaunchingInDifferentOrientation(activityRecord, activityRecord, z);
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x0091, code lost:
    
        if (r4.getTask().mInResumeTopActivity == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0080, code lost:
    
        if (r3.mDisplayContentExt.suggestUseFixedRotationAnimation(r3, r4) != false) goto L46;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean handleTopActivityLaunchingInDifferentOrientation(ActivityRecord activityRecord, ActivityRecord activityRecord2, boolean z) {
        int rotationForActivityInDifferentOrientation;
        if (!WindowManagerService.ENABLE_FIXED_ROTATION_TRANSFORM || this.mDisplayContentExt.shouldReviseScreenOrientationForApp(activityRecord) || activityRecord.isFinishingFixedRotationTransform()) {
            return false;
        }
        if (activityRecord.hasFixedRotationTransform()) {
            if (!this.mDisplayContentExt.shouldExitFixedRotation(this, activityRecord)) {
                return true;
            }
            clearFixedRotationLaunchingApp();
            return false;
        }
        if (this.mDisplayContentExt.shouldPuttFixedRotation(this, activityRecord)) {
            return true;
        }
        if (this.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange()) {
            return false;
        }
        if ((!activityRecord.occludesParent() && this.mDisplayContentExt.doRotationAnimation(activityRecord)) || (activityRecord.isReportedDrawn() && activityRecord.isVisible())) {
            return false;
        }
        if (z) {
            if (this.mTransitionController.isShellTransitionsEnabled()) {
                if (!this.mTransitionController.isCollecting(activityRecord)) {
                    return false;
                }
            } else {
                if (this.mAppTransition.isTransitionSet()) {
                    if (!this.mOpeningApps.contains(activityRecord)) {
                    }
                }
                return false;
            }
            if (activityRecord.isState(ActivityRecord.State.RESUMED)) {
            }
        } else if (activityRecord != topRunningActivity()) {
            return false;
        }
        if ((this.mLastWallpaperVisible && activityRecord.windowsCanBeWallpaperTarget() && this.mFixedRotationTransitionListener.mAnimatingRecents == null && !this.mTransitionController.isTransientLaunch(activityRecord)) || (rotationForActivityInDifferentOrientation = rotationForActivityInDifferentOrientation(activityRecord2)) == -1 || !activityRecord.getDisplayArea().matchParentBounds() || this.mDisplayContentExt.dontDoFixedRotatinAnimation(this, activityRecord) || !this.mDisplayContentExt.handleNonOccludesParentActiviy(this, activityRecord) || !this.mDisplayContentExt.shouldSetFixedRotationForTargetLaunchingApp(activityRecord2, activityRecord)) {
            return false;
        }
        setFixedRotationLaunchingApp(activityRecord, rotationForActivityInDifferentOrientation);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean mayImeShowOnLaunchingActivity(ActivityRecord activityRecord) {
        int i;
        StartingData startingData;
        WindowState findMainWindow = activityRecord.findMainWindow(false);
        if (findMainWindow == null || (i = findMainWindow.mAttrs.softInputMode & 15) == 2 || i == 3) {
            return false;
        }
        if (activityRecord.getWindow(new Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda49
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$mayImeShowOnLaunchingActivity$12;
                lambda$mayImeShowOnLaunchingActivity$12 = DisplayContent.lambda$mayImeShowOnLaunchingActivity$12((WindowState) obj);
                return lambda$mayImeShowOnLaunchingActivity$12;
            }
        }) != null) {
            return activityRecord.mLastImeShown || ((startingData = activityRecord.mStartingData) != null && startingData.hasImeSurface());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$mayImeShowOnLaunchingActivity$12(WindowState windowState) {
        return WindowManager.LayoutParams.mayUseInputMethod(windowState.mAttrs.flags);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasTopFixedRotationLaunchingApp() {
        ActivityRecord activityRecord = this.mFixedRotationLaunchingApp;
        return (activityRecord == null || activityRecord == this.mFixedRotationTransitionListener.mAnimatingRecents) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFixedRotationLaunchingApp(ActivityRecord activityRecord) {
        return this.mFixedRotationLaunchingApp == activityRecord;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public AsyncRotationController getAsyncRotationController() {
        return this.mAsyncRotationController;
    }

    void setFixedRotationLaunchingAppUnchecked(ActivityRecord activityRecord) {
        setFixedRotationLaunchingAppUnchecked(activityRecord, -1);
    }

    void setFixedRotationLaunchingAppUnchecked(ActivityRecord activityRecord, int i) {
        ActivityRecord activityRecord2 = this.mFixedRotationLaunchingApp;
        if (activityRecord2 == null && activityRecord != null) {
            this.mWmService.mDisplayNotificationController.dispatchFixedRotationStarted(this, i);
            startAsyncRotation(activityRecord == this.mFixedRotationTransitionListener.mAnimatingRecents || this.mTransitionController.isTransientLaunch(activityRecord));
        } else if (activityRecord2 != null && activityRecord == null) {
            this.mWmService.mDisplayNotificationController.dispatchFixedRotationFinished(this);
            if (!this.mTransitionController.isCollecting(this)) {
                finishAsyncRotationIfPossible();
            }
        }
        this.mDisplayContentExt.positionAnimation(this.mFixedRotationLaunchingApp, activityRecord);
        this.mFixedRotationLaunchingApp = activityRecord;
        this.mDisplayContentExt.setFixedRotationForScreenshot(activityRecord, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFixedRotationLaunchingApp(ActivityRecord activityRecord, int i) {
        ActivityRecord activityRecord2 = this.mFixedRotationLaunchingApp;
        if (activityRecord2 == activityRecord && activityRecord.getWindowConfiguration().getRotation() == i) {
            return;
        }
        if (activityRecord2 != null && activityRecord2.getWindowConfiguration().getRotation() == i && this.mDisplayContentExt.isAnimating(activityRecord2, this.mDisplayRotation)) {
            activityRecord.linkFixedRotationTransform(activityRecord2);
            if (activityRecord != this.mFixedRotationTransitionListener.mAnimatingRecents) {
                setFixedRotationLaunchingAppUnchecked(activityRecord, i);
                return;
            }
            return;
        }
        if (!activityRecord.hasFixedRotationTransform()) {
            startFixedRotationTransform(activityRecord, i);
        }
        setFixedRotationLaunchingAppUnchecked(activityRecord, i);
        if (activityRecord2 != null) {
            activityRecord2.finishFixedRotationTransform();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void continueUpdateOrientationForDiffOrienLaunchingApp() {
        if (this.mFixedRotationLaunchingApp == null || this.mPinnedTaskController.shouldDeferOrientationChange()) {
            return;
        }
        if (this.mDisplayRotation.updateOrientation(getOrientation(), false)) {
            sendNewConfiguration();
        } else {
            if (this.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange()) {
                return;
            }
            clearFixedRotationLaunchingApp();
        }
    }

    private void clearFixedRotationLaunchingApp() {
        ActivityRecord activityRecord = this.mFixedRotationLaunchingApp;
        if (activityRecord == null) {
            return;
        }
        activityRecord.finishFixedRotationTransform();
        setFixedRotationLaunchingAppUnchecked(null);
    }

    private void startFixedRotationTransform(WindowToken windowToken, int i) {
        this.mTmpConfiguration.unset();
        DisplayInfo computeScreenConfiguration = computeScreenConfiguration(this.mTmpConfiguration, i);
        windowToken.applyFixedRotationTransform(computeScreenConfiguration, new DisplayFrames(new InsetsState(), computeScreenConfiguration, calculateDisplayCutoutForRotation(i), calculateRoundedCornersForRotation(i), calculatePrivacyIndicatorBoundsForRotation(i), calculateDisplayShapeForRotation(i)), this.mTmpConfiguration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void rotateInDifferentOrientationIfNeeded(ActivityRecord activityRecord) {
        int rotationForActivityInDifferentOrientation;
        if (this.mDisplayContentExt.shouldReviseScreenOrientationForApp(activityRecord) || (rotationForActivityInDifferentOrientation = rotationForActivityInDifferentOrientation(activityRecord)) == -1) {
            return;
        }
        startFixedRotationTransform(activityRecord, rotationForActivityInDifferentOrientation);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRotationChanging() {
        return this.mDisplayRotation.getRotation() != getWindowConfiguration().getRotation();
    }

    private void startAsyncRotationIfNeeded() {
        if (isRotationChanging()) {
            startAsyncRotation(false);
        }
    }

    private boolean startAsyncRotation(boolean z) {
        if (z) {
            this.mWmService.mH.postDelayed(new Runnable() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda43
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayContent.this.lambda$startAsyncRotation$13();
                }
            }, FIXED_ROTATION_HIDE_ANIMATION_DEBOUNCE_DELAY_MS);
            return false;
        }
        if (this.mAsyncRotationController != null) {
            return false;
        }
        this.mAsyncRotationController = new AsyncRotationController(this);
        this.mDisplayContentExt.setRotationChange(this, true);
        this.mAsyncRotationController.start();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startAsyncRotation$13() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mFixedRotationLaunchingApp != null && startAsyncRotation(false)) {
                    getPendingTransaction().apply();
                }
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishAsyncRotationIfPossible() {
        AsyncRotationController asyncRotationController = this.mAsyncRotationController;
        if (asyncRotationController == null || this.mDisplayRotation.hasSeamlessRotatingWindow()) {
            return;
        }
        asyncRotationController.completeAll();
        this.mDisplayContentExt.setRotationChange(this, false);
        this.mAsyncRotationController = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishAsyncRotation(WindowToken windowToken) {
        AsyncRotationController asyncRotationController = this.mAsyncRotationController;
        if (asyncRotationController == null || !asyncRotationController.completeRotation(windowToken)) {
            return;
        }
        this.mDisplayContentExt.setRotationChange(this, false);
        this.mAsyncRotationController = null;
        this.mDisplayContentExt.requestTraversalWhenAsyncRotationFinished();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldSyncRotationChange(WindowState windowState) {
        AsyncRotationController asyncRotationController = this.mAsyncRotationController;
        return asyncRotationController == null || !asyncRotationController.isAsync(windowState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyInsetsChanged(Consumer<WindowState> consumer) {
        InsetsState fixedRotationTransformInsetsState;
        ActivityRecord activityRecord = this.mFixedRotationLaunchingApp;
        if (activityRecord != null && (fixedRotationTransformInsetsState = activityRecord.getFixedRotationTransformInsetsState()) != null) {
            InsetsState.traverse(fixedRotationTransformInsetsState, this.mInsetsStateController.getRawInsetsState(), COPY_SOURCE_VISIBILITY);
        }
        forAllWindows(consumer, true);
        RemoteInsetsControlTarget remoteInsetsControlTarget = this.mRemoteInsetsControlTarget;
        if (remoteInsetsControlTarget != null) {
            remoteInsetsControlTarget.notifyInsetsChanged();
        }
        if (this.mWmService.mAccessibilityController.hasCallbacks()) {
            InsetsControlTarget insetsControlTarget = this.mImeControlTarget;
            this.mWmService.mAccessibilityController.updateImeVisibilityIfNeeded(this.mDisplayId, insetsControlTarget != null && insetsControlTarget.isRequestedVisible(WindowInsets.Type.ime()));
        }
        this.mDisplayContentExt.notifyInsetsChangedLw();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateRotationUnchecked() {
        if (!this.mTransitionController.isShellTransitionsEnabled()) {
            if (this.mDisplayContentExt.updateRotationUnchecked((hasTopFixedRotationLaunchingApp() && this.mFixedRotationLaunchingApp.isFixedRotationTransforming() && this.mFixedRotationLaunchingApp.hasAnimatingFixedRotationTransition()) || this.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange())) {
                Slog.d(TAG, "updateRotationUnchecked: stop for fixed rotation or waiting remote rotation.");
                return false;
            }
        }
        if (this.mDisplayContentExt.hasGestureAnimationController()) {
            Slog.d(TAG, "block update orientation during gesture animation when updateRotationUnchecked");
            return false;
        }
        return this.mDisplayRotation.updateRotationUnchecked(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canShowTasksInHostDeviceRecents() {
        DisplayWindowPolicyControllerHelper displayWindowPolicyControllerHelper = this.mDwpcHelper;
        if (displayWindowPolicyControllerHelper == null) {
            return true;
        }
        return displayWindowPolicyControllerHelper.canShowTasksInHostDeviceRecents();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: applyRotation, reason: merged with bridge method [inline-methods] */
    public void lambda$applyRotationAndFinishFixedRotation$44(final int i, final int i2) {
        this.mDisplayContentExt.applyRotation(i, i2, this.mDisplayId);
        this.mDisplayRotation.applyCurrentRotation(i2);
        final boolean z = false;
        boolean z2 = this.mTransitionController.getTransitionPlayer() != null;
        if (this.mDisplayRotation.isRotatingSeamlessly() && !z2) {
            z = true;
        }
        final SurfaceControl.Transaction syncTransaction = z2 ? getSyncTransaction() : getPendingTransaction();
        ScreenRotationAnimation rotationAnimation = z ? null : getRotationAnimation();
        updateDisplayAndOrientation(null);
        if (rotationAnimation != null && rotationAnimation.hasScreenshot()) {
            rotationAnimation.setRotation(syncTransaction, i2);
        }
        if (!z2) {
            forAllWindows(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda31
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DisplayContent.lambda$applyRotation$14(syncTransaction, i, i2, z, (WindowState) obj);
                }
            }, true);
            this.mPinnedTaskController.startSeamlessRotationIfNeeded(syncTransaction, i, i2);
            if (!this.mDisplayRotation.hasSeamlessRotatingWindow()) {
                this.mDisplayRotation.cancelSeamlessRotation();
            }
        }
        this.mWmService.mDisplayManagerInternal.performTraversal(syncTransaction);
        if (z2) {
            getPendingTransaction().setFixedTransformHint(this.mSurfaceControl, i2);
            syncTransaction.unsetFixedTransformHint(this.mSurfaceControl);
        }
        scheduleAnimation();
        this.mWmService.mRotationWatcherController.dispatchDisplayRotationChange(this.mDisplayId, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$applyRotation$14(SurfaceControl.Transaction transaction, int i, int i2, boolean z, WindowState windowState) {
        windowState.seamlesslyRotateIfAllowed(transaction, i, i2, z);
        if (z || !windowState.mHasSurface) {
            return;
        }
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 2083556954, 0, (String) null, new Object[]{String.valueOf(windowState)});
        }
        windowState.setOrientationChanging(true);
    }

    void configureDisplayPolicy() {
        this.mRootWindowContainer.updateDisplayImePolicyCache();
        this.mDisplayPolicy.updateConfigurationAndScreenSizeDependentBehaviors();
        this.mDisplayRotation.configure(this.mBaseDisplayWidth, this.mBaseDisplayHeight);
    }

    private DisplayInfo updateDisplayAndOrientation(Configuration configuration) {
        int rotation = getRotation();
        boolean shouldDisplayRotated = this.mDisplayContentExt.shouldDisplayRotated(rotation, this.mDisplayInfo.name, this);
        int i = shouldDisplayRotated ? this.mBaseDisplayHeight : this.mBaseDisplayWidth;
        int i2 = shouldDisplayRotated ? this.mBaseDisplayWidth : this.mBaseDisplayHeight;
        DisplayCutout calculateDisplayCutoutForRotation = calculateDisplayCutoutForRotation(rotation);
        RoundedCorners calculateRoundedCornersForRotation = calculateRoundedCornersForRotation(rotation);
        DisplayShape calculateDisplayShapeForRotation = calculateDisplayShapeForRotation(rotation);
        Rect rect = this.mDisplayPolicy.getDecorInsetsInfo(rotation, i, i2).mNonDecorFrame;
        DisplayInfo displayInfo = this.mDisplayInfo;
        displayInfo.rotation = rotation;
        displayInfo.logicalWidth = i;
        displayInfo.logicalHeight = i2;
        displayInfo.logicalDensityDpi = this.mBaseDisplayDensity;
        displayInfo.physicalXDpi = this.mBaseDisplayPhysicalXDpi;
        displayInfo.physicalYDpi = this.mBaseDisplayPhysicalYDpi;
        displayInfo.appWidth = rect.width();
        this.mDisplayInfo.appHeight = rect.height();
        if (this.isDefaultDisplay) {
            this.mDisplayInfo.getLogicalMetrics(this.mRealDisplayMetrics, CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO, (Configuration) null);
        }
        DisplayInfo displayInfo2 = this.mDisplayInfo;
        if (calculateDisplayCutoutForRotation.isEmpty()) {
            calculateDisplayCutoutForRotation = null;
        }
        displayInfo2.displayCutout = calculateDisplayCutoutForRotation;
        DisplayInfo displayInfo3 = this.mDisplayInfo;
        displayInfo3.roundedCorners = calculateRoundedCornersForRotation;
        displayInfo3.displayShape = calculateDisplayShapeForRotation;
        displayInfo3.getAppMetrics(this.mDisplayMetrics);
        if (this.mDisplayScalingDisabled) {
            this.mDisplayInfo.flags |= 1073741824;
        } else {
            this.mDisplayInfo.flags &= -1073741825;
        }
        computeSizeRanges(this.mDisplayInfo, shouldDisplayRotated, i, i2, this.mDisplayMetrics.density, configuration);
        this.mWmService.mDisplayManagerInternal.setDisplayInfoOverrideFromWindowManager(this.mDisplayId, this.mDisplayInfo);
        if (this.isDefaultDisplay) {
            this.mCompatibleScreenScale = CompatibilityInfo.computeCompatibleScaling(this.mDisplayMetrics, this.mCompatDisplayMetrics);
        }
        onDisplayInfoChanged();
        return this.mDisplayInfo;
    }

    DisplayCutout calculateDisplayCutoutForRotation(int i) {
        if (this.mDisplayContentExt.isReturnNoCutoutForFullScreenDisplay(i, this)) {
            return DisplayCutout.NO_CUTOUT;
        }
        return this.mDisplayCutoutCache.getOrCompute(this.mIsSizeForced ? this.mBaseDisplayCutout : this.mInitialDisplayCutout, this.mDisplayContentExt.correctRotationParam(i)).getDisplayCutout();
    }

    static WmDisplayCutout calculateDisplayCutoutForRotationAndDisplaySizeUncached(DisplayCutout displayCutout, int i, int i2, int i3) {
        if (displayCutout == null || displayCutout == DisplayCutout.NO_CUTOUT) {
            return WmDisplayCutout.NO_CUTOUT;
        }
        if (i2 == i3) {
            Slog.w(TAG, "Ignore cutout because display size is square: " + i2);
            return WmDisplayCutout.NO_CUTOUT;
        }
        if (i == 0) {
            return WmDisplayCutout.computeSafeInsets(displayCutout, i2, i3);
        }
        DisplayCutout rotated = displayCutout.getRotated(i2, i3, 0, i);
        boolean z = i == 1 || i == 3;
        int i4 = z ? i3 : i2;
        if (!z) {
            i2 = i3;
        }
        return new WmDisplayCutout(rotated, new Size(i4, i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public WmDisplayCutout calculateDisplayCutoutForRotationUncached(DisplayCutout displayCutout, int i) {
        boolean z = this.mIsSizeForced;
        return calculateDisplayCutoutForRotationAndDisplaySizeUncached(displayCutout, i, z ? this.mBaseDisplayWidth : this.mInitialDisplayWidth, z ? this.mBaseDisplayHeight : this.mInitialDisplayHeight);
    }

    RoundedCorners calculateRoundedCornersForRotation(int i) {
        return this.mRoundedCornerCache.getOrCompute(this.mIsSizeForced ? this.mBaseRoundedCorners : this.mInitialRoundedCorners, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RoundedCorners calculateRoundedCornersForRotationUncached(RoundedCorners roundedCorners, int i) {
        if (roundedCorners == null || roundedCorners == RoundedCorners.NO_ROUNDED_CORNERS) {
            return RoundedCorners.NO_ROUNDED_CORNERS;
        }
        if (i == 0) {
            return roundedCorners;
        }
        boolean z = this.mIsSizeForced;
        return roundedCorners.rotate(i, z ? this.mBaseDisplayWidth : this.mInitialDisplayWidth, z ? this.mBaseDisplayHeight : this.mInitialDisplayHeight);
    }

    PrivacyIndicatorBounds calculatePrivacyIndicatorBoundsForRotation(int i) {
        return this.mPrivacyIndicatorBoundsCache.getOrCompute(this.mCurrentPrivacyIndicatorBounds, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PrivacyIndicatorBounds calculatePrivacyIndicatorBoundsForRotationUncached(PrivacyIndicatorBounds privacyIndicatorBounds, int i) {
        if (privacyIndicatorBounds == null) {
            return new PrivacyIndicatorBounds(new Rect[4], i);
        }
        return privacyIndicatorBounds.rotate(i);
    }

    DisplayShape calculateDisplayShapeForRotation(int i) {
        return this.mDisplayShapeCache.getOrCompute(this.mInitialDisplayShape, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DisplayShape calculateDisplayShapeForRotationUncached(DisplayShape displayShape, int i) {
        if (displayShape == null) {
            return DisplayShape.NONE;
        }
        return i == 0 ? displayShape : displayShape.setRotation(i);
    }

    DisplayInfo computeScreenConfiguration(Configuration configuration, int i) {
        boolean z = i == 1 || i == 3;
        int i2 = z ? this.mBaseDisplayHeight : this.mBaseDisplayWidth;
        int i3 = z ? this.mBaseDisplayWidth : this.mBaseDisplayHeight;
        configuration.windowConfiguration.setMaxBounds(0, 0, i2, i3);
        WindowConfiguration windowConfiguration = configuration.windowConfiguration;
        windowConfiguration.setBounds(windowConfiguration.getMaxBounds());
        computeScreenAppConfiguration(configuration, i2, i3, i);
        DisplayInfo displayInfo = new DisplayInfo(this.mDisplayInfo);
        displayInfo.rotation = i;
        displayInfo.logicalWidth = i2;
        displayInfo.logicalHeight = i3;
        Rect appBounds = configuration.windowConfiguration.getAppBounds();
        displayInfo.appWidth = appBounds.width();
        displayInfo.appHeight = appBounds.height();
        DisplayCutout calculateDisplayCutoutForRotation = calculateDisplayCutoutForRotation(i);
        if (calculateDisplayCutoutForRotation.isEmpty()) {
            calculateDisplayCutoutForRotation = null;
        }
        displayInfo.displayCutout = calculateDisplayCutoutForRotation;
        computeSizeRanges(displayInfo, z, i2, i3, this.mDisplayMetrics.density, configuration);
        if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
            Slog.d(TAG, "computeScreenConfiguration: AppBounds:" + appBounds + ", rotation:" + displayInfo.rotation);
        }
        return displayInfo;
    }

    private void computeScreenAppConfiguration(Configuration configuration, int i, int i2, int i3) {
        DisplayPolicy.DecorInsets.Info decorInsetsInfo = this.mDisplayPolicy.getDecorInsetsInfo(i3, i, i2);
        configuration.windowConfiguration.setAppBounds(decorInsetsInfo.mNonDecorFrame);
        configuration.windowConfiguration.setRotation(i3);
        boolean z = true;
        configuration.orientation = i <= i2 ? 1 : 2;
        float f = this.mDisplayMetrics.density;
        configuration.screenWidthDp = (int) ((decorInsetsInfo.mConfigFrame.width() / f) + 0.5f);
        int height = (int) ((decorInsetsInfo.mConfigFrame.height() / f) + 0.5f);
        configuration.screenHeightDp = height;
        float f2 = configuration.screenWidthDp;
        float f3 = this.mCompatibleScreenScale;
        configuration.compatScreenWidthDp = (int) (f2 / f3);
        configuration.compatScreenHeightDp = (int) (height / f3);
        configuration.screenLayout = WindowContainer.computeScreenLayout(Configuration.resetScreenLayout(configuration.screenLayout), configuration.screenWidthDp, configuration.screenHeightDp);
        if (i3 != 1 && i3 != 3) {
            z = false;
        }
        configuration.compatSmallestScreenWidthDp = computeCompatSmallestWidth(z, i, i2);
        configuration.windowConfiguration.setDisplayRotation(i3);
        this.mDisplayContentExt.adjustScreenConfigurationForCarLink(this.mDisplayContent, configuration, f);
        if (f == INVALID_DPI) {
            Slog.i(TAG, "computeScreenAppConfiguration  dw:" + i + " dh:" + i2 + " rotation:" + i3 + " configFrameWidth:" + decorInsetsInfo.mConfigFrame.width() + " configFrameHeight:" + decorInsetsInfo.mConfigFrame.height() + " density:" + f + " outConfig:" + configuration.toString() + " " + Debug.getCallers(5));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00e3 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void computeScreenConfiguration(Configuration configuration) {
        DisplayInfo updateDisplayAndOrientation = updateDisplayAndOrientation(configuration);
        int i = updateDisplayAndOrientation.logicalWidth;
        int i2 = updateDisplayAndOrientation.logicalHeight;
        this.mTmpRect.set(0, 0, i, i2);
        configuration.windowConfiguration.setBounds(this.mTmpRect);
        configuration.windowConfiguration.setMaxBounds(this.mTmpRect);
        configuration.windowConfiguration.setWindowingMode(getWindowingMode());
        configuration.windowConfiguration.setDisplayWindowingMode(getWindowingMode());
        computeScreenAppConfiguration(configuration, i, i2, updateDisplayAndOrientation.rotation);
        configuration.screenLayout = (configuration.screenLayout & (-769)) | ((updateDisplayAndOrientation.flags & 16) != 0 ? 512 : 256);
        configuration.densityDpi = updateDisplayAndOrientation.logicalDensityDpi;
        configuration.colorMode = ((updateDisplayAndOrientation.isWideColorGamut() && this.mWmService.hasWideColorGamutSupport()) ? 2 : 1) | ((updateDisplayAndOrientation.isHdr() && this.mWmService.hasHdrSupport()) ? 8 : 4);
        configuration.touchscreen = 1;
        configuration.keyboard = 1;
        configuration.navigation = 1;
        InputDevice[] inputDevices = this.mWmService.mInputManager.getInputDevices();
        int length = inputDevices != null ? inputDevices.length : 0;
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < length; i5++) {
            InputDevice inputDevice = inputDevices[i5];
            if (!inputDevice.isVirtual() && this.mWmService.mInputManager.canDispatchToDisplay(inputDevice.getId(), this.mDisplayId)) {
                int sources = inputDevice.getSources();
                int i6 = inputDevice.isExternal() ? 2 : 1;
                if (!this.mWmService.mIsTouchDevice) {
                    configuration.touchscreen = 1;
                } else if ((sources & 4098) == 4098) {
                    configuration.touchscreen = 3;
                }
                if ((sources & 65540) == 65540) {
                    configuration.navigation = 3;
                } else {
                    if ((sources & 513) == 513 && configuration.navigation == 1) {
                        configuration.navigation = 2;
                    }
                    if (inputDevice.getKeyboardType() != 2) {
                        configuration.keyboard = 2;
                        i4 |= i6;
                    }
                }
                i3 |= i6;
                if (inputDevice.getKeyboardType() != 2) {
                }
            }
        }
        if (configuration.navigation == 1 && this.mWmService.mHasPermanentDpad) {
            configuration.navigation = 2;
            i3 |= 1;
        }
        boolean z = configuration.keyboard != 1;
        WindowManagerService windowManagerService = this.mWmService;
        if (z != windowManagerService.mHardKeyboardAvailable) {
            windowManagerService.mHardKeyboardAvailable = z;
            windowManagerService.mH.removeMessages(22);
            this.mWmService.mH.sendEmptyMessage(22);
        }
        this.mDisplayPolicy.updateConfigurationAndScreenSizeDependentBehaviors();
        configuration.keyboardHidden = 1;
        configuration.hardKeyboardHidden = 1;
        configuration.navigationHidden = 1;
        this.mWmService.mPolicy.adjustConfigurationLw(configuration, i4, i3);
    }

    private int computeCompatSmallestWidth(boolean z, int i, int i2) {
        this.mTmpDisplayMetrics.setTo(this.mDisplayMetrics);
        DisplayMetrics displayMetrics = this.mTmpDisplayMetrics;
        if (!z) {
            i2 = i;
            i = i2;
        }
        return reduceCompatConfigWidthSize(reduceCompatConfigWidthSize(reduceCompatConfigWidthSize(reduceCompatConfigWidthSize(0, 0, displayMetrics, i2, i), 1, displayMetrics, i, i2), 2, displayMetrics, i2, i), 3, displayMetrics, i, i2);
    }

    private int reduceCompatConfigWidthSize(int i, int i2, DisplayMetrics displayMetrics, int i3, int i4) {
        Rect rect = this.mDisplayPolicy.getDecorInsetsInfo(i2, i3, i4).mNonDecorFrame;
        displayMetrics.noncompatWidthPixels = rect.width();
        displayMetrics.noncompatHeightPixels = rect.height();
        int computeCompatibleScaling = (int) (((displayMetrics.noncompatWidthPixels / CompatibilityInfo.computeCompatibleScaling(displayMetrics, (DisplayMetrics) null)) / displayMetrics.density) + 0.5f);
        return (i == 0 || computeCompatibleScaling < i) ? computeCompatibleScaling : i;
    }

    private void computeSizeRanges(DisplayInfo displayInfo, boolean z, int i, int i2, float f, Configuration configuration) {
        if (z) {
            i2 = i;
            i = i2;
        }
        displayInfo.smallestNominalAppWidth = 1073741824;
        displayInfo.smallestNominalAppHeight = 1073741824;
        displayInfo.largestNominalAppWidth = 0;
        displayInfo.largestNominalAppHeight = 0;
        adjustDisplaySizeRanges(displayInfo, 0, i, i2);
        adjustDisplaySizeRanges(displayInfo, 1, i2, i);
        adjustDisplaySizeRanges(displayInfo, 2, i, i2);
        adjustDisplaySizeRanges(displayInfo, 3, i2, i);
        if (configuration == null) {
            return;
        }
        configuration.smallestScreenWidthDp = (int) ((displayInfo.smallestNominalAppWidth / f) + 0.5f);
    }

    private void adjustDisplaySizeRanges(DisplayInfo displayInfo, int i, int i2, int i3) {
        DisplayPolicy.DecorInsets.Info decorInsetsInfo = this.mDisplayPolicy.getDecorInsetsInfo(i, i2, i3);
        int width = decorInsetsInfo.mConfigFrame.width();
        int height = decorInsetsInfo.mConfigFrame.height();
        if (width < displayInfo.smallestNominalAppWidth) {
            displayInfo.smallestNominalAppWidth = width;
        }
        if (width > displayInfo.largestNominalAppWidth) {
            displayInfo.largestNominalAppWidth = width;
        }
        if (height < displayInfo.smallestNominalAppHeight) {
            displayInfo.smallestNominalAppHeight = height;
        }
        if (height > displayInfo.largestNominalAppHeight) {
            displayInfo.largestNominalAppHeight = height;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPreferredOptionsPanelGravity() {
        int rotation = getRotation();
        if (this.mInitialDisplayWidth < this.mInitialDisplayHeight) {
            if (rotation != 1) {
                return rotation != 3 ? 81 : 8388691;
            }
            return 85;
        }
        if (rotation == 1) {
            return 81;
        }
        if (rotation != 2) {
            return rotation != 3 ? 85 : 81;
        }
        return 8388691;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PinnedTaskController getPinnedTaskController() {
        return this.mPinnedTaskController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasAccess(int i) {
        return this.mDisplay.hasAccess(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPrivate() {
        return (this.mDisplay.getFlags() & 4) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTrusted() {
        return this.mDisplay.isTrusted();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getRootTask(final int i, final int i2) {
        return (Task) getItemFromTaskDisplayAreas(new Function() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda47
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Task lambda$getRootTask$15;
                lambda$getRootTask$15 = DisplayContent.lambda$getRootTask$15(i, i2, (TaskDisplayArea) obj);
                return lambda$getRootTask$15;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Task lambda$getRootTask$15(int i, int i2, TaskDisplayArea taskDisplayArea) {
        return taskDisplayArea.getRootTask(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getRootTask$16(int i, Task task) {
        return task.getRootTaskId() == i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getRootTask(final int i) {
        return getRootTask(new Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda26
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getRootTask$16;
                lambda$getRootTask$16 = DisplayContent.lambda$getRootTask$16(i, (Task) obj);
                return lambda$getRootTask$16;
            }
        });
    }

    int getRootTaskCount() {
        final int[] iArr = new int[1];
        forAllRootTasks(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda37
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.lambda$getRootTaskCount$17(iArr, (Task) obj);
            }
        });
        return iArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getRootTaskCount$17(int[] iArr, Task task) {
        iArr[0] = iArr[0] + 1;
    }

    Task getTopRootTask() {
        return getRootTask(new Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda23
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getTopRootTask$18;
                lambda$getTopRootTask$18 = DisplayContent.lambda$getTopRootTask$18((Task) obj);
                return lambda$getTopRootTask$18;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCurrentOverrideConfigurationChanges() {
        return this.mCurrentOverrideConfigurationChanges;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getInitialDisplayDensity() {
        int i;
        int i2 = this.mInitialDisplayDensity;
        int i3 = this.mMaxUiWidth;
        return (i3 <= 0 || (i = this.mInitialDisplayWidth) <= i3) ? i2 : (int) ((i2 * i3) / i);
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(Configuration configuration) {
        int i = getConfiguration().orientation;
        super.onConfigurationChanged(configuration);
        DisplayPolicy displayPolicy = this.mDisplayPolicy;
        if (displayPolicy != null) {
            displayPolicy.onConfigurationChanged();
            this.mPinnedTaskController.onPostDisplayConfigurationChanged();
        }
        this.mDisplayContentExt.updateRotation(this.mDisplayContent, true);
        updateImeParent();
        ContentRecorder contentRecorder = this.mContentRecorder;
        if (contentRecorder != null) {
            contentRecorder.onConfigurationChanged(i);
        }
        if (i != getConfiguration().orientation) {
            getMetricsLogger().write(new LogMaker(1659).setSubtype(getConfiguration().orientation).addTaggedData(1660, Integer.valueOf(getDisplayId())));
            this.mDisplayContentExt.onConfigurationChanged(configuration);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isVisibleRequested() {
        return (!isVisible() || this.mRemoved || this.mRemoving) ? false : true;
    }

    @Override // com.android.server.wm.WindowContainer
    void onAppTransitionDone() {
        super.onAppTransitionDone();
        this.mWmService.mWindowsChanged = true;
        ActivityRecord activityRecord = this.mFixedRotationLaunchingApp;
        if (activityRecord != null && !activityRecord.isVisibleRequested() && !this.mFixedRotationLaunchingApp.isVisible() && !this.mDisplayRotation.isRotatingSeamlessly()) {
            clearFixedRotationLaunchingApp();
        }
        this.mDisplayContentExt.onAppTransitionDone();
    }

    @Override // com.android.server.wm.ConfigurationContainer
    void setDisplayWindowingMode(int i) {
        setWindowingMode(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean forAllImeWindows(ToBooleanFunction<WindowState> toBooleanFunction, boolean z) {
        return this.mImeWindowsContainer.forAllWindowForce(toBooleanFunction, z);
    }

    @Override // com.android.server.wm.WindowContainer
    int getOrientation() {
        int orientation;
        if (((IMirageDisplayManagerExt) ExtLoader.type(IMirageDisplayManagerExt.class).create()).isMirageCarMode(this.mDisplayId)) {
            if (!ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                return 2;
            }
            Slog.d(TAG, "getOrientation MirageCarMode user");
            return 2;
        }
        int splitRequestedOrientation = this.mDisplayContentExt.getSplitRequestedOrientation();
        if (splitRequestedOrientation != -2) {
            if (ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                Slog.d(TAG, "getOrientation splitRequestedOrientation " + splitRequestedOrientation);
            }
            return splitRequestedOrientation;
        }
        WindowManagerService windowManagerService = this.mWmService;
        if (windowManagerService.mDisplayFrozen && windowManagerService.mPolicy.isKeyguardLocked()) {
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -1710206702, 5, (String) null, new Object[]{Long.valueOf(this.mDisplayId), Long.valueOf(getLastOrientation())});
            }
            return getLastOrientation();
        }
        DisplayRotationCompatPolicy displayRotationCompatPolicy = this.mDisplayRotationCompatPolicy;
        if (displayRotationCompatPolicy != null && (orientation = displayRotationCompatPolicy.getOrientation()) != -1) {
            this.mLastOrientationSource = null;
            return orientation;
        }
        int orientation2 = super.getOrientation();
        if (!handlesOrientationChangeFromDescendant(orientation2)) {
            ActivityRecord activityRecord = topRunningActivity(true);
            if (activityRecord != null && activityRecord.mLetterboxUiController.shouldUseDisplayLandscapeNaturalOrientation()) {
                if (!ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                    return 0;
                }
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1015746067, 21, (String) null, new Object[]{Long.valueOf(this.mDisplayId), Long.valueOf(orientation2), 0L, String.valueOf(activityRecord)});
                return 0;
            }
            this.mLastOrientationSource = null;
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1877863087, 21, (String) null, new Object[]{Long.valueOf(this.mDisplayId), Long.valueOf(orientation2), -1L});
            }
            return -1;
        }
        if (orientation2 == -2) {
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -1480772131, 5, (String) null, new Object[]{-1L, Long.valueOf(this.mDisplayId)});
            }
            return -1;
        }
        WindowContainer lastOrientationSource = getLastOrientationSource();
        int fixedScreenOrientation = this.mDisplayContentExt.getFixedScreenOrientation(lastOrientationSource, orientation2);
        if (fixedScreenOrientation == orientation2) {
            return orientation2;
        }
        if (ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
            Slog.d(TAG, "getOrientation " + lastOrientationSource + " origin " + orientation2 + " newOrientation " + fixedScreenOrientation);
        }
        return fixedScreenOrientation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateDisplayInfo() {
        updateBaseDisplayMetricsIfNeeded();
        this.mDisplay.getDisplayInfo(this.mDisplayInfo);
        this.mDisplay.getMetrics(this.mDisplayMetrics);
        onDisplayInfoChanged();
        onDisplayChanged(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updatePrivacyIndicatorBounds(Rect[] rectArr) {
        PrivacyIndicatorBounds privacyIndicatorBounds = this.mCurrentPrivacyIndicatorBounds;
        PrivacyIndicatorBounds updateStaticBounds = privacyIndicatorBounds.updateStaticBounds(rectArr);
        this.mCurrentPrivacyIndicatorBounds = updateStaticBounds;
        if (Objects.equals(privacyIndicatorBounds, updateStaticBounds)) {
            return;
        }
        updateDisplayFrames(true);
    }

    void onDisplayInfoChanged() {
        updateDisplayFrames(false);
        this.mMinSizeOfResizeableTaskDp = getMinimalTaskSizeDp();
        InputMonitor inputMonitor = this.mInputMonitor;
        DisplayInfo displayInfo = this.mDisplayInfo;
        inputMonitor.layoutInputConsumers(displayInfo.logicalWidth, displayInfo.logicalHeight);
        this.mDisplayPolicy.onDisplayInfoChanged(this.mDisplayInfo);
    }

    private void updateDisplayFrames(boolean z) {
        DisplayFrames displayFrames = this.mDisplayFrames;
        DisplayInfo displayInfo = this.mDisplayInfo;
        if (updateDisplayFrames(displayFrames, displayInfo.rotation, displayInfo.logicalWidth, displayInfo.logicalHeight)) {
            this.mInsetsStateController.onDisplayFramesUpdated(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateDisplayFrames(DisplayFrames displayFrames, int i, int i2, int i3) {
        return displayFrames.update(i, i2, i3, calculateDisplayCutoutForRotation(i), calculateRoundedCornersForRotation(i), calculatePrivacyIndicatorBoundsForRotation(i), calculateDisplayShapeForRotation(i));
    }

    @Override // com.android.server.wm.WindowContainer
    void onDisplayChanged(DisplayContent displayContent) {
        super.onDisplayChanged(displayContent);
        updateSystemGestureExclusionLimit();
        this.mDisplayContentExt.hookOnDisplayChanged(displayContent);
        updateKeepClearAreas();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateSystemGestureExclusionLimit() {
        this.mSystemGestureExclusionLimit = (this.mDisplayContentExt.adjustConstantSystemGestureExclusionLimitDp(this.mWmService.mConstants.mSystemGestureExclusionLimitDp, this) * this.mDisplayMetrics.densityDpi) / 160;
        updateSystemGestureExclusion();
    }

    void initializeDisplayBaseInfo() {
        DisplayManagerInternal displayManagerInternal = this.mWmService.mDisplayManagerInternal;
        if (displayManagerInternal != null) {
            DisplayInfo displayInfo = displayManagerInternal.getDisplayInfo(this.mDisplayId);
            if (displayInfo != null) {
                this.mDisplayInfo.copyFrom(displayInfo);
            }
            this.mDwpcHelper = new DisplayWindowPolicyControllerHelper(this);
        }
        DisplayInfo displayInfo2 = this.mDisplayInfo;
        updateBaseDisplayMetrics(displayInfo2.logicalWidth, displayInfo2.logicalHeight, displayInfo2.logicalDensityDpi, displayInfo2.physicalXDpi, displayInfo2.physicalYDpi);
        DisplayInfo displayInfo3 = this.mDisplayInfo;
        this.mInitialDisplayWidth = displayInfo3.logicalWidth;
        this.mInitialDisplayHeight = displayInfo3.logicalHeight;
        this.mInitialDisplayDensity = displayInfo3.logicalDensityDpi;
        this.mInitialPhysicalXDpi = displayInfo3.physicalXDpi;
        this.mInitialPhysicalYDpi = displayInfo3.physicalYDpi;
        this.mInitialDisplayCutout = displayInfo3.displayCutout;
        this.mInitialRoundedCorners = displayInfo3.roundedCorners;
        this.mCurrentPrivacyIndicatorBounds = new PrivacyIndicatorBounds(new Rect[4], this.mDisplayInfo.rotation);
        DisplayInfo displayInfo4 = this.mDisplayInfo;
        this.mInitialDisplayShape = displayInfo4.displayShape;
        Display.Mode maximumResolutionDisplayMode = DisplayUtils.getMaximumResolutionDisplayMode(displayInfo4.supportedModes);
        this.mPhysicalDisplaySize = new Point(maximumResolutionDisplayMode == null ? this.mInitialDisplayWidth : maximumResolutionDisplayMode.getPhysicalWidth(), maximumResolutionDisplayMode == null ? this.mInitialDisplayHeight : maximumResolutionDisplayMode.getPhysicalHeight());
    }

    private void updateBaseDisplayMetricsIfNeeded() {
        int i;
        RoundedCorners roundedCorners;
        String str;
        DisplayCutout displayCutout;
        DisplayShape displayShape;
        float f;
        this.mWmService.mDisplayManagerInternal.getNonOverrideDisplayInfo(this.mDisplayId, this.mDisplayInfo);
        int rotation = getRotation();
        DisplayInfo displayInfo = this.mDisplayInfo;
        int i2 = displayInfo.rotation;
        boolean z = i2 == 1 || i2 == 3;
        int i3 = z ? displayInfo.logicalHeight : displayInfo.logicalWidth;
        int i4 = z ? displayInfo.logicalWidth : displayInfo.logicalHeight;
        int i5 = displayInfo.logicalDensityDpi;
        float f2 = displayInfo.physicalXDpi;
        float f3 = displayInfo.physicalYDpi;
        DisplayCutout displayCutout2 = this.mIgnoreDisplayCutout ? DisplayCutout.NO_CUTOUT : displayInfo.displayCutout;
        String str2 = displayInfo.uniqueId;
        RoundedCorners roundedCorners2 = displayInfo.roundedCorners;
        DisplayShape displayShape2 = displayInfo.displayShape;
        boolean z2 = (this.mInitialDisplayWidth == i3 && this.mInitialDisplayHeight == i4 && this.mInitialDisplayDensity == i5 && this.mInitialPhysicalXDpi == f2 && this.mInitialPhysicalYDpi == f3 && Objects.equals(this.mInitialDisplayCutout, displayCutout2) && Objects.equals(this.mInitialRoundedCorners, roundedCorners2) && Objects.equals(this.mInitialDisplayShape, displayShape2)) ? false : true;
        boolean z3 = !str2.equals(this.mCurrentUniqueDisplayId);
        if (z2 || z3) {
            if (z3) {
                this.mWmService.mDisplayWindowSettings.applySettingsToDisplayLocked(this, false);
                roundedCorners = roundedCorners2;
                str = str2;
                i = rotation;
                displayCutout = displayCutout2;
                this.mDisplaySwitchTransitionLauncher.requestDisplaySwitchTransitionIfNeeded(this.mDisplayId, this.mInitialDisplayWidth, this.mInitialDisplayHeight, i3, i4);
                this.mDisplayRotation.physicalDisplayChanged();
                this.mDisplayPolicy.physicalDisplayChanged();
            } else {
                i = rotation;
                roundedCorners = roundedCorners2;
                str = str2;
                displayCutout = displayCutout2;
            }
            boolean z4 = this.mIsSizeForced;
            int i6 = z4 ? this.mBaseDisplayWidth : i3;
            int i7 = z4 ? this.mBaseDisplayHeight : i4;
            int i8 = this.mIsDensityForced ? this.mBaseDisplayDensity : i5;
            float f4 = z4 ? this.mBaseDisplayPhysicalXDpi : f2;
            if (z4) {
                f = this.mBaseDisplayPhysicalYDpi;
                displayShape = displayShape2;
            } else {
                displayShape = displayShape2;
                f = f3;
            }
            DisplayShape displayShape3 = displayShape;
            int i9 = i6;
            String str3 = str;
            int i10 = i7;
            RoundedCorners roundedCorners3 = roundedCorners;
            updateBaseDisplayMetrics(i9, i10, i8, f4, f);
            configureDisplayPolicy();
            if (z3) {
                this.mWmService.mDisplayWindowSettings.applyRotationSettingsToDisplayLocked(this);
                this.mDisplayContentExt.physicalDisplayChanged(this);
            }
            this.mInitialDisplayWidth = i3;
            this.mInitialDisplayHeight = i4;
            this.mInitialDisplayDensity = i5;
            this.mInitialPhysicalXDpi = f2;
            this.mInitialPhysicalYDpi = f3;
            this.mInitialDisplayCutout = displayCutout;
            this.mInitialRoundedCorners = roundedCorners3;
            this.mInitialDisplayShape = displayShape3;
            this.mCurrentUniqueDisplayId = str3;
            reconfigureDisplayLocked();
            if (z3) {
                this.mDisplayContentExt.physicalDisplayChangedAfterConfig(this);
                Slog.d(TAG, "physicalDisplayChange to " + str3 + " size:" + i3 + "x" + i4);
                this.mDisplayPolicy.physicalDisplayUpdated();
                this.mDisplaySwitchTransitionLauncher.onDisplayUpdated(i, getRotation(), getDisplayAreaInfo());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMaxUiWidth(int i) {
        if (WindowManagerDebugConfig.DEBUG_DISPLAY) {
            Slog.v(TAG, "Setting max ui width:" + i + " on display:" + getDisplayId());
        }
        this.mMaxUiWidth = i;
        updateBaseDisplayMetrics(this.mBaseDisplayWidth, this.mBaseDisplayHeight, this.mBaseDisplayDensity, this.mBaseDisplayPhysicalXDpi, this.mBaseDisplayPhysicalYDpi);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateBaseDisplayMetrics(int i, int i2, int i3, float f, float f2) {
        int i4;
        this.mBaseDisplayWidth = i;
        this.mBaseDisplayHeight = i2;
        this.mBaseDisplayDensity = i3;
        this.mBaseDisplayPhysicalXDpi = f;
        this.mBaseDisplayPhysicalYDpi = f2;
        if (this.mIsSizeForced) {
            this.mBaseDisplayCutout = loadDisplayCutout(i, i2);
            this.mBaseRoundedCorners = loadRoundedCorners(i, i2);
        }
        int i5 = this.mMaxUiWidth;
        if (i5 > 0 && (i4 = this.mBaseDisplayWidth) > i5) {
            float f3 = i5 / i4;
            this.mBaseDisplayHeight = (int) (this.mBaseDisplayHeight * f3);
            this.mBaseDisplayWidth = i5;
            this.mBaseDisplayPhysicalXDpi *= f3;
            this.mBaseDisplayPhysicalYDpi *= f3;
            if (!this.mIsDensityForced) {
                this.mBaseDisplayDensity = (int) (this.mBaseDisplayDensity * f3);
            }
            if (WindowManagerDebugConfig.DEBUG_DISPLAY) {
                Slog.v(TAG, "Applying config restraints:" + this.mBaseDisplayWidth + "x" + this.mBaseDisplayHeight + " on display:" + getDisplayId());
            }
        }
        if (!this.mDisplayReady || this.mDisplayPolicy.shouldKeepCurrentDecorInsets()) {
            return;
        }
        this.mDisplayPolicy.mDecorInsets.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForcedDensity(int i, int i2) {
        this.mIsDensityForced = i != getInitialDisplayDensity();
        boolean z = i2 == -2;
        if (this.mWmService.mCurrentUserId == i2 || z) {
            this.mBaseDisplayDensity = i;
            reconfigureDisplayLocked();
        }
        if (z) {
            return;
        }
        if (i == getInitialDisplayDensity()) {
            i = 0;
        }
        this.mWmService.mDisplayWindowSettings.setForcedDensity(getDisplayInfo(), i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForcedScalingMode(int i) {
        if (i != 1) {
            i = 0;
        }
        this.mDisplayScalingDisabled = i != 0;
        StringBuilder sb = new StringBuilder();
        sb.append("Using display scaling mode: ");
        sb.append(this.mDisplayScalingDisabled ? "off" : "auto");
        Slog.i(TAG, sb.toString());
        reconfigureDisplayLocked();
        this.mWmService.mDisplayWindowSettings.setForcedScalingMode(this, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForcedSize(int i, int i2) {
        setForcedSize(i, i2, INVALID_DPI, INVALID_DPI);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForcedSize(int i, int i2, float f, float f2) {
        int i3 = this.mMaxUiWidth;
        if (i3 > 0 && i > i3) {
            i2 = (int) (i2 * (i3 / i));
            i = i3;
        }
        int i4 = 0;
        boolean z = (this.mInitialDisplayWidth == i && this.mInitialDisplayHeight == i2) ? false : true;
        this.mIsSizeForced = z;
        if (z) {
            Point validForcedSize = getValidForcedSize(i, i2);
            int i5 = validForcedSize.x;
            i2 = validForcedSize.y;
            i = i5;
        }
        Slog.i(TAG, "Using new display size: " + i + "x" + i2);
        int i6 = this.mBaseDisplayDensity;
        if (f == INVALID_DPI) {
            f = this.mBaseDisplayPhysicalXDpi;
        }
        float f3 = f;
        if (f2 == INVALID_DPI) {
            f2 = this.mBaseDisplayPhysicalYDpi;
        }
        updateBaseDisplayMetrics(i, i2, i6, f3, f2);
        reconfigureDisplayLocked();
        if (this.mIsSizeForced) {
            i4 = i;
        } else {
            i2 = 0;
        }
        this.mWmService.mDisplayWindowSettings.setForcedSize(this, i4, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Point getValidForcedSize(int i, int i2) {
        int max = Math.max(this.mInitialDisplayWidth, this.mInitialDisplayHeight) * 3;
        return new Point(Math.min(Math.max(i, 200), max), Math.min(Math.max(i2, 200), max));
    }

    DisplayCutout loadDisplayCutout(int i, int i2) {
        DisplayPolicy displayPolicy = this.mDisplayPolicy;
        if (displayPolicy == null || this.mInitialDisplayCutout == null) {
            return null;
        }
        Resources resources = displayPolicy.getSystemUiContext().getResources();
        String str = this.mDisplayInfo.uniqueId;
        Point point = this.mPhysicalDisplaySize;
        return DisplayCutout.fromResourcesRectApproximation(resources, str, point.x, point.y, i, i2);
    }

    RoundedCorners loadRoundedCorners(int i, int i2) {
        DisplayPolicy displayPolicy = this.mDisplayPolicy;
        if (displayPolicy == null || this.mInitialRoundedCorners == null) {
            return null;
        }
        Resources resources = displayPolicy.getSystemUiContext().getResources();
        String str = this.mDisplayInfo.uniqueId;
        Point point = this.mPhysicalDisplaySize;
        return RoundedCorners.fromResources(resources, str, point.x, point.y, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.DisplayArea
    public void getStableRect(Rect rect) {
        InsetsState rawInsetsState = this.mDisplayContent.getInsetsStateController().getRawInsetsState();
        rect.set(rawInsetsState.getDisplayFrame());
        rect.inset(rawInsetsState.calculateInsets(rect, WindowInsets.Type.systemBars(), true));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskDisplayArea getDefaultTaskDisplayArea() {
        return this.mDisplayAreaPolicy.getDefaultTaskDisplayArea();
    }

    @VisibleForTesting
    void updateDisplayAreaOrganizers() {
        if (isTrusted()) {
            forAllDisplayAreas(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DisplayContent.this.lambda$updateDisplayAreaOrganizers$19((DisplayArea) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDisplayAreaOrganizers$19(DisplayArea displayArea) {
        IDisplayAreaOrganizer organizerByFeature;
        if (displayArea.isOrganized() || (organizerByFeature = this.mAtmService.mWindowOrganizerController.mDisplayAreaOrganizerController.getOrganizerByFeature(displayArea.mFeatureId)) == null) {
            return;
        }
        displayArea.setOrganizer(organizerByFeature);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task findTaskForResizePoint(final int i, final int i2) {
        final int dipToPixel = WindowManagerService.dipToPixel(30, this.mDisplayMetrics);
        return (Task) getItemFromTaskDisplayAreas(new Function() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda36
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Task lambda$findTaskForResizePoint$20;
                lambda$findTaskForResizePoint$20 = DisplayContent.this.lambda$findTaskForResizePoint$20(i, i2, dipToPixel, (TaskDisplayArea) obj);
                return lambda$findTaskForResizePoint$20;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Task lambda$findTaskForResizePoint$20(int i, int i2, int i3, TaskDisplayArea taskDisplayArea) {
        return this.mTmpTaskForResizePointSearchResult.process(taskDisplayArea, i, i2, i3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateTouchExcludeRegion() {
        ActivityRecord activityRecord = this.mFocusedApp;
        final Task task = activityRecord != null ? activityRecord.getTask() : null;
        if (task == null) {
            this.mTouchExcludeRegion.setEmpty();
        } else {
            Region region = this.mTouchExcludeRegion;
            DisplayInfo displayInfo = this.mDisplayInfo;
            region.set(0, 0, displayInfo.logicalWidth, displayInfo.logicalHeight);
            final int dipToPixel = WindowManagerService.dipToPixel(30, this.mDisplayMetrics);
            this.mTmpRect.setEmpty();
            this.mTmpRect2.setEmpty();
            forAllTasks(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda50
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DisplayContent.this.lambda$updateTouchExcludeRegion$21(task, dipToPixel, (Task) obj);
                }
            });
            if (!this.mTmpRect2.isEmpty()) {
                this.mTouchExcludeRegion.op(this.mTmpRect2, Region.Op.UNION);
            }
        }
        WindowState windowState = this.mInputMethodWindow;
        if (windowState != null && windowState.isVisible()) {
            this.mInputMethodWindow.getTouchableRegion(this.mTmpRegion);
            this.mTouchExcludeRegion.op(this.mTmpRegion, Region.Op.UNION);
        }
        for (int size = this.mTapExcludedWindows.size() - 1; size >= 0; size--) {
            WindowState windowState2 = this.mTapExcludedWindows.get(size);
            if (windowState2.isVisible()) {
                windowState2.getTouchableRegion(this.mTmpRegion);
                this.mTouchExcludeRegion.op(this.mTmpRegion, Region.Op.UNION);
            }
        }
        amendWindowTapExcludeRegion(this.mTouchExcludeRegion);
        this.mDisplayContentExt.updateWindowTapExcludeRegion(this, this.mTouchExcludeRegion);
        this.mTapDetector.setTouchExcludeRegion(this.mTouchExcludeRegion);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: processTaskForTouchExcludeRegion, reason: merged with bridge method [inline-methods] */
    public void lambda$updateTouchExcludeRegion$21(Task task, Task task2, int i) {
        ActivityRecord topVisibleActivity = task.getTopVisibleActivity();
        if (topVisibleActivity == null || !topVisibleActivity.hasContentToDisplay()) {
            return;
        }
        if (task.isActivityTypeHome() && task.isVisible() && task.isResizeable()) {
            task.getDisplayArea().getBounds(this.mTmpRect);
        } else {
            task.getDimBounds(this.mTmpRect);
        }
        this.mDisplayContentExt.getScaleBound(task, this.mTmpRect);
        if (task == task2) {
            this.mTmpRect2.set(this.mTmpRect);
        }
        boolean inFreeformWindowingMode = task.inFreeformWindowingMode();
        if (task != task2 || inFreeformWindowingMode) {
            if (inFreeformWindowingMode) {
                int i2 = -i;
                this.mTmpRect.inset(i2, i2);
                this.mTmpRect.inset(getInsetsStateController().getRawInsetsState().calculateInsets(this.mTmpRect, WindowInsets.Type.systemBars() | WindowInsets.Type.ime(), false));
            }
            this.mTouchExcludeRegion.op(this.mTmpRect, Region.Op.DIFFERENCE);
        }
    }

    private void amendWindowTapExcludeRegion(Region region) {
        Region obtain = Region.obtain();
        for (int size = this.mTapExcludeProvidingWindows.size() - 1; size >= 0; size--) {
            this.mTapExcludeProvidingWindows.valueAt(size).getTapExcludeRegion(obtain);
            region.op(obtain, Region.Op.UNION);
        }
        obtain.recycle();
    }

    @Override // com.android.server.wm.WindowContainer
    void switchUser(int i) {
        super.switchUser(i);
        this.mWmService.mWindowsChanged = true;
        this.mDisplayPolicy.switchUser();
    }

    private boolean shouldDeferRemoval() {
        return isAnimating(3) || this.mTransitionController.isTransitionOnDisplay(this);
    }

    @Override // com.android.server.wm.WindowContainer
    void removeIfPossible() {
        if (shouldDeferRemoval()) {
            this.mDeferredRemoval = true;
        } else {
            removeImmediately();
        }
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    void removeImmediately() {
        this.mDeferredRemoval = false;
        try {
            this.mOpeningApps.clear();
            this.mClosingApps.clear();
            this.mChangingContainers.clear();
            this.mUnknownAppVisibilityController.clear();
            this.mAppTransition.removeAppTransitionTimeoutCallbacks();
            this.mTransitionController.unregisterLegacyListener(this.mFixedRotationTransitionListener);
            handleAnimatingStoppedAndTransition();
            this.mWmService.stopFreezingDisplayLocked();
            this.mDeviceStateController.unregisterDeviceStateCallback(this.mDeviceStateConsumer);
            super.removeImmediately();
            if (WindowManagerDebugConfig.DEBUG_DISPLAY) {
                Slog.v(TAG, "Removing display=" + this);
            }
            this.mPointerEventDispatcher.dispose();
            setRotationAnimation(null);
            setRemoteInsetsController(null);
            this.mOverlayLayer.release();
            this.mInputOverlayLayer.release();
            this.mA11yOverlayLayer.release();
            this.mWindowingLayer.release();
            this.mInputMonitor.onDisplayRemoved();
            this.mWmService.mDisplayNotificationController.dispatchDisplayRemoved(this);
            this.mDisplayRotation.onDisplayRemoved();
            this.mWmService.mAccessibilityController.onDisplayRemoved(this.mDisplayId);
            this.mRootWindowContainer.mTaskSupervisor.getKeyguardController().onDisplayRemoved(this.mDisplayId);
            this.mWallpaperController.resetLargestDisplay(this.mDisplay);
            this.mDisplayReady = false;
            getPendingTransaction().apply();
            this.mWmService.mWindowPlacerLocked.requestTraversal();
            DisplayRotationCompatPolicy displayRotationCompatPolicy = this.mDisplayRotationCompatPolicy;
            if (displayRotationCompatPolicy != null) {
                displayRotationCompatPolicy.dispose();
            }
        } catch (Throwable th) {
            this.mDisplayReady = false;
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean handleCompleteDeferredRemoval() {
        boolean z = super.handleCompleteDeferredRemoval() || shouldDeferRemoval();
        if (z || !this.mDeferredRemoval) {
            return z;
        }
        removeImmediately();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void adjustForImeIfNeeded() {
        WindowState windowState = this.mInputMethodWindow;
        boolean z = windowState != null && windowState.isVisible() && windowState.isDisplayed();
        int inputMethodWindowVisibleHeight = getInputMethodWindowVisibleHeight();
        this.mPinnedTaskController.setAdjustedForIme(z, inputMethodWindowVisibleHeight);
        IDisplayContentExt iDisplayContentExt = this.mDisplayContentExt;
        InputTarget inputTarget = this.mImeInputTarget;
        iDisplayContentExt.hookAdjustForImeIfNeeded(windowState, z, inputMethodWindowVisibleHeight, inputTarget == null ? null : inputTarget.getWindowState(), this.mCurrentFocus);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getInputMethodWindowVisibleHeight() {
        InsetsState rawInsetsState = getInsetsStateController().getRawInsetsState();
        InsetsSource peekSource = rawInsetsState.peekSource(InsetsSource.ID_IME);
        if (peekSource == null || !peekSource.isVisible()) {
            this.mDisplayContentExt.notifyIMELayoutChanged(false, 0, 0);
            return 0;
        }
        Rect visibleFrame = peekSource.getVisibleFrame() != null ? peekSource.getVisibleFrame() : peekSource.getFrame();
        Rect rect = this.mTmpRect;
        rect.set(rawInsetsState.getDisplayFrame());
        rect.inset(rawInsetsState.calculateInsets(rect, WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout(), false));
        this.mDisplayContentExt.notifyIMELayoutChanged(true, visibleFrame.top, rect.bottom);
        return rect.bottom - visibleFrame.top;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void rotateBounds(int i, int i2, Rect rect) {
        getBounds(this.mTmpRect, i);
        RotationUtils.rotateBounds(rect, this.mTmpRect, i, i2);
    }

    public void setRotationAnimation(ScreenRotationAnimation screenRotationAnimation) {
        ScreenRotationAnimation screenRotationAnimation2 = this.mScreenRotationAnimation;
        this.mScreenRotationAnimation = screenRotationAnimation;
        if (screenRotationAnimation2 != null) {
            screenRotationAnimation2.kill();
        }
        if (screenRotationAnimation == null || !screenRotationAnimation.hasScreenshot()) {
            return;
        }
        startAsyncRotationIfNeeded();
    }

    public ScreenRotationAnimation getRotationAnimation() {
        return this.mScreenRotationAnimation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void requestChangeTransitionIfNeeded(int i, TransitionRequestInfo.DisplayChange displayChange) {
        if (this.mLastHasContent) {
            if (this.mDisplayContentExt.skipTransitionAnimationIfNeed(i, displayChange, this.mTransitionController, this.mFocusedApp)) {
                Slog.d(TAG, "requestChangeTransitionIfNeeded skip");
                return;
            }
            TransitionController transitionController = this.mTransitionController;
            if (transitionController.isCollecting()) {
                if (displayChange != null) {
                    throw new IllegalArgumentException("Provided displayChange for non-new transition");
                }
                if (transitionController.isCollecting(this)) {
                    return;
                }
                transitionController.collect(this);
                startAsyncRotationIfNeeded();
                if (this.mFixedRotationLaunchingApp != null) {
                    setSeamlessTransitionForFixedRotation(transitionController.getCollectingTransition());
                    return;
                }
                return;
            }
            Transition requestTransitionIfNeeded = transitionController.requestTransitionIfNeeded(6, 0, this, this, null, displayChange);
            if (requestTransitionIfNeeded != null) {
                this.mAtmService.startLaunchPowerMode(2);
                if (this.mFixedRotationLaunchingApp != null) {
                    setSeamlessTransitionForFixedRotation(requestTransitionIfNeeded);
                } else if (isRotationChanging()) {
                    if (displayChange != null && this.mDisplayRotation.shouldRotateSeamlessly(displayChange.getStartRotation(), displayChange.getEndRotation(), false)) {
                        if (this.mDisplayContentExt.requestSeamlessExplicit()) {
                            requestTransitionIfNeeded.setSeamlessRotation(this);
                        } else {
                            requestTransitionIfNeeded.onSeamlessRotating(this);
                        }
                    }
                    this.mWmService.mLatencyTracker.onActionStart(6);
                    transitionController.mTransitionMetricsReporter.associate(requestTransitionIfNeeded.getToken(), new LongConsumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda2
                        @Override // java.util.function.LongConsumer
                        public final void accept(long j) {
                            DisplayContent.this.lambda$requestChangeTransitionIfNeeded$22(j);
                        }
                    });
                    startAsyncRotation(false);
                }
                requestTransitionIfNeeded.setKnownConfigChanges(this, i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestChangeTransitionIfNeeded$22(long j) {
        this.mWmService.mLatencyTracker.onActionEnd(6);
    }

    private void setSeamlessTransitionForFixedRotation(Transition transition) {
        transition.setSeamlessRotation(this);
        AsyncRotationController asyncRotationController = this.mAsyncRotationController;
        if (asyncRotationController != null) {
            asyncRotationController.keepAppearanceInPreviousRotation();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean inTransition() {
        return this.mScreenRotationAnimation != null || super.inTransition();
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j, int i) {
        if (i != 2 || isVisible()) {
            long start = protoOutputStream.start(j);
            super.dumpDebug(protoOutputStream, 1146756268053L, i);
            protoOutputStream.write(1120986464258L, this.mDisplayId);
            protoOutputStream.write(1120986464265L, this.mBaseDisplayDensity);
            this.mDisplayInfo.dumpDebug(protoOutputStream, 1146756268042L);
            this.mDisplayRotation.dumpDebug(protoOutputStream, 1146756268065L);
            ScreenRotationAnimation rotationAnimation = getRotationAnimation();
            if (rotationAnimation != null) {
                rotationAnimation.dumpDebug(protoOutputStream, 1146756268044L);
            }
            this.mDisplayFrames.dumpDebug(protoOutputStream, 1146756268045L);
            protoOutputStream.write(1120986464295L, this.mMinSizeOfResizeableTaskDp);
            if (this.mTransitionController.isShellTransitionsEnabled()) {
                this.mTransitionController.dumpDebugLegacy(protoOutputStream, 1146756268048L);
            } else {
                this.mAppTransition.dumpDebug(protoOutputStream, 1146756268048L);
            }
            ActivityRecord activityRecord = this.mFocusedApp;
            if (activityRecord != null) {
                activityRecord.writeNameToProto(protoOutputStream, 1138166333455L);
            }
            for (int size = this.mOpeningApps.size() - 1; size >= 0; size--) {
                this.mOpeningApps.valueAt(size).writeIdentifierToProto(protoOutputStream, 2246267895825L);
            }
            for (int size2 = this.mClosingApps.size() - 1; size2 >= 0; size2--) {
                this.mClosingApps.valueAt(size2).writeIdentifierToProto(protoOutputStream, 2246267895826L);
            }
            Task focusedRootTask = getFocusedRootTask();
            if (focusedRootTask != null) {
                protoOutputStream.write(1120986464279L, focusedRootTask.getRootTaskId());
                ActivityRecord focusedActivity = focusedRootTask.getDisplayArea().getFocusedActivity();
                if (focusedActivity != null) {
                    focusedActivity.writeIdentifierToProto(protoOutputStream, 1146756268056L);
                }
            } else {
                protoOutputStream.write(1120986464279L, -1);
            }
            protoOutputStream.write(1133871366170L, isReady());
            protoOutputStream.write(1133871366180L, isSleeping());
            for (int i2 = 0; i2 < this.mAllSleepTokens.size(); i2++) {
                this.mAllSleepTokens.get(i2).writeTagToProto(protoOutputStream, 2237677961253L);
            }
            WindowState windowState = this.mImeLayeringTarget;
            if (windowState != null) {
                windowState.dumpDebug(protoOutputStream, 1146756268059L, i);
            }
            InputTarget inputTarget = this.mImeInputTarget;
            if (inputTarget != null) {
                inputTarget.dumpProto(protoOutputStream, 1146756268060L, i);
            }
            InsetsControlTarget insetsControlTarget = this.mImeControlTarget;
            if (insetsControlTarget != null && insetsControlTarget.getWindow() != null) {
                this.mImeControlTarget.getWindow().dumpDebug(protoOutputStream, 1146756268061L, i);
            }
            WindowState windowState2 = this.mCurrentFocus;
            if (windowState2 != null) {
                windowState2.dumpDebug(protoOutputStream, 1146756268062L, i);
            }
            InsetsStateController insetsStateController = this.mInsetsStateController;
            if (insetsStateController != null) {
                insetsStateController.dumpDebug(protoOutputStream, i);
            }
            protoOutputStream.write(1120986464290L, getImePolicy());
            Iterator<Rect> it = getKeepClearAreas().iterator();
            while (it.hasNext()) {
                it.next().dumpDebug(protoOutputStream, 2246267895846L);
            }
            protoOutputStream.end(start);
        }
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    public void dump(final PrintWriter printWriter, final String str, final boolean z) {
        printWriter.print(str);
        StringBuilder sb = new StringBuilder();
        sb.append("Display: mDisplayId=");
        sb.append(this.mDisplayId);
        sb.append(isOrganized() ? " (organized)" : "");
        printWriter.println(sb.toString());
        String str2 = "  " + str;
        printWriter.print(str2);
        printWriter.print("init=");
        printWriter.print(this.mInitialDisplayWidth);
        printWriter.print("x");
        printWriter.print(this.mInitialDisplayHeight);
        printWriter.print(" ");
        printWriter.print(this.mInitialDisplayDensity);
        printWriter.print("dpi");
        printWriter.print(" mMinSizeOfResizeableTaskDp=");
        printWriter.print(this.mMinSizeOfResizeableTaskDp);
        if (this.mInitialDisplayWidth != this.mBaseDisplayWidth || this.mInitialDisplayHeight != this.mBaseDisplayHeight || this.mInitialDisplayDensity != this.mBaseDisplayDensity) {
            printWriter.print(" base=");
            printWriter.print(this.mBaseDisplayWidth);
            printWriter.print("x");
            printWriter.print(this.mBaseDisplayHeight);
            printWriter.print(" ");
            printWriter.print(this.mBaseDisplayDensity);
            printWriter.print("dpi");
        }
        if (this.mDisplayScalingDisabled) {
            printWriter.println(" noscale");
        }
        printWriter.print(" cur=");
        printWriter.print(this.mDisplayInfo.logicalWidth);
        printWriter.print("x");
        printWriter.print(this.mDisplayInfo.logicalHeight);
        printWriter.print(" app=");
        printWriter.print(this.mDisplayInfo.appWidth);
        printWriter.print("x");
        printWriter.print(this.mDisplayInfo.appHeight);
        printWriter.print(" rng=");
        printWriter.print(this.mDisplayInfo.smallestNominalAppWidth);
        printWriter.print("x");
        printWriter.print(this.mDisplayInfo.smallestNominalAppHeight);
        printWriter.print("-");
        printWriter.print(this.mDisplayInfo.largestNominalAppWidth);
        printWriter.print("x");
        printWriter.println(this.mDisplayInfo.largestNominalAppHeight);
        printWriter.print("mIsDensityForced=");
        printWriter.println(this.mIsDensityForced);
        printWriter.print(str2 + "deferred=" + this.mDeferredRemoval + " mLayoutNeeded=" + this.mLayoutNeeded);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" mTouchExcludeRegion=");
        sb2.append(this.mTouchExcludeRegion);
        printWriter.println(sb2.toString());
        printWriter.println();
        super.dump(printWriter, str, z);
        printWriter.print(str);
        printWriter.print("mLayoutSeq=");
        printWriter.println(this.mLayoutSeq);
        printWriter.print("  mCurrentFocus=");
        printWriter.println(this.mCurrentFocus);
        printWriter.print("  mFocusedApp=");
        printWriter.println(this.mFocusedApp);
        if (this.mFixedRotationLaunchingApp != null) {
            printWriter.println("  mFixedRotationLaunchingApp=" + this.mFixedRotationLaunchingApp);
        }
        printWriter.println();
        printWriter.print(str + "mHoldScreenWindow=");
        printWriter.print(this.mHoldScreenWindow);
        printWriter.println();
        printWriter.print(str + "mObscuringWindow=");
        printWriter.print(this.mObscuringWindow);
        printWriter.println();
        printWriter.print(str + "mLastWakeLockHoldingWindow=");
        printWriter.print(this.mLastWakeLockHoldingWindow);
        printWriter.println();
        printWriter.print(str + "mLastWakeLockObscuringWindow=");
        printWriter.println(this.mLastWakeLockObscuringWindow);
        printWriter.println();
        this.mWallpaperController.dump(printWriter, "  ");
        if (this.mSystemGestureExclusionListeners.getRegisteredCallbackCount() > 0) {
            printWriter.println();
            printWriter.print("  mSystemGestureExclusion=");
            printWriter.println(this.mSystemGestureExclusion);
        }
        Set<Rect> keepClearAreas = getKeepClearAreas();
        if (!keepClearAreas.isEmpty()) {
            printWriter.println();
            printWriter.print("  keepClearAreas=");
            printWriter.println(keepClearAreas);
        }
        printWriter.println();
        printWriter.println(str + "Display areas in top down Z order:");
        dumpChildDisplayArea(printWriter, str2, z);
        printWriter.println();
        printWriter.println(str + "Task display areas in top down Z order:");
        forAllTaskDisplayAreas(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda30
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.lambda$dump$23(printWriter, str, z, (TaskDisplayArea) obj);
            }
        });
        printWriter.println();
        ScreenRotationAnimation rotationAnimation = getRotationAnimation();
        if (rotationAnimation != null) {
            printWriter.println("  mScreenRotationAnimation:");
            rotationAnimation.printTo(str2, printWriter);
        } else if (z) {
            printWriter.println("  no ScreenRotationAnimation ");
        }
        printWriter.println();
        Task rootHomeTask = getDefaultTaskDisplayArea().getRootHomeTask();
        if (rootHomeTask != null) {
            printWriter.println(str + "rootHomeTask=" + rootHomeTask.getName());
        }
        Task rootPinnedTask = getDefaultTaskDisplayArea().getRootPinnedTask();
        if (rootPinnedTask != null) {
            printWriter.println(str + "rootPinnedTask=" + rootPinnedTask.getName());
        }
        Task rootTask = getDefaultTaskDisplayArea().getRootTask(0, 3);
        if (rootTask != null) {
            printWriter.println(str + "rootRecentsTask=" + rootTask.getName());
        }
        Task rootTask2 = getRootTask(0, 5);
        if (rootTask2 != null) {
            printWriter.println(str + "rootDreamTask=" + rootTask2.getName());
        }
        printWriter.println();
        this.mPinnedTaskController.dump(str, printWriter);
        printWriter.println();
        this.mDisplayFrames.dump(str, printWriter);
        printWriter.println();
        this.mDisplayPolicy.dump(str, printWriter);
        printWriter.println();
        this.mDisplayRotation.dump(str, printWriter);
        printWriter.println();
        this.mInputMonitor.dump(printWriter, "  ");
        printWriter.println();
        this.mInsetsStateController.dump(str, printWriter);
        this.mDwpcHelper.dump(str, printWriter);
        printWriter.println();
        this.mAppTransition.dump(printWriter, str2);
        AsyncRotationController asyncRotationController = this.mAsyncRotationController;
        if (asyncRotationController != null) {
            asyncRotationController.dump(printWriter, str2);
        }
        if (this.mAllSleepTokens.size() > 0) {
            printWriter.println(str + " mAllSleepTokens:");
            for (int i = 0; i < this.mAllSleepTokens.size(); i++) {
                printWriter.println(str + "  " + this.mAllSleepTokens.get(i));
            }
        }
        printWriter.println(str + "isWaitingForRemoteDisplayChange=" + this.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange());
        this.mTransitionController.dump(printWriter, str2, z);
        Object flexibleActivityImeAnimationState = this.mDisplayContentExt.getFlexibleActivityImeAnimationState();
        if (flexibleActivityImeAnimationState != null) {
            printWriter.println(str + "flexibleActivityImeAnimationState=" + flexibleActivityImeAnimationState);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$23(PrintWriter printWriter, String str, boolean z, TaskDisplayArea taskDisplayArea) {
        taskDisplayArea.dump(printWriter, str + "  ", z);
    }

    @Override // com.android.server.wm.DisplayArea
    public String toString() {
        return "Display{#" + this.mDisplayId + " state=" + Display.stateToString(this.mDisplayInfo.state) + " size=" + this.mDisplayInfo.logicalWidth + "x" + this.mDisplayInfo.logicalHeight + " " + Surface.rotationToString(this.mDisplayInfo.rotation) + "}";
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.ConfigurationContainer
    String getName() {
        return "Display " + this.mDisplayId + " name=\"" + this.mDisplayInfo.name + "\"";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState getTouchableWinAtPointLocked(float f, float f2) {
        final int i = (int) f;
        final int i2 = (int) f2;
        return getWindow(new Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda35
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getTouchableWinAtPointLocked$24;
                lambda$getTouchableWinAtPointLocked$24 = DisplayContent.this.lambda$getTouchableWinAtPointLocked$24(i, i2, (WindowState) obj);
                return lambda$getTouchableWinAtPointLocked$24;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getTouchableWinAtPointLocked$24(int i, int i2, WindowState windowState) {
        int i3 = windowState.mAttrs.flags;
        if (!windowState.isVisible() || (i3 & 16) != 0) {
            return false;
        }
        windowState.getVisibleBounds(this.mTmpRect);
        if (!this.mTmpRect.contains(i, i2)) {
            return false;
        }
        windowState.getTouchableRegion(this.mTmpRegion);
        return this.mTmpRegion.contains(i, i2) || (i3 & 40) == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canAddToastWindowForUid(final int i) {
        return getWindow(new Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda45
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$canAddToastWindowForUid$25;
                lambda$canAddToastWindowForUid$25 = DisplayContent.lambda$canAddToastWindowForUid$25(i, (WindowState) obj);
                return lambda$canAddToastWindowForUid$25;
            }
        }) != null || getWindow(new Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda46
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$canAddToastWindowForUid$26;
                lambda$canAddToastWindowForUid$26 = DisplayContent.lambda$canAddToastWindowForUid$26(i, (WindowState) obj);
                return lambda$canAddToastWindowForUid$26;
            }
        }) == null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$canAddToastWindowForUid$25(int i, WindowState windowState) {
        return windowState.mOwnerUid == i && windowState.isFocused();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$canAddToastWindowForUid$26(int i, WindowState windowState) {
        return windowState.mAttrs.type == 2005 && windowState.mOwnerUid == i && !windowState.mPermanentlyHidden && !windowState.mWindowRemovalAllowed;
    }

    void scheduleToastWindowsTimeoutIfNeededLocked(WindowState windowState, WindowState windowState2) {
        if (windowState != null) {
            if (windowState2 == null || windowState2.mOwnerUid != windowState.mOwnerUid) {
                this.mTmpWindow = windowState;
                forAllWindows(this.mScheduleToastTimeout, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canStealTopFocus() {
        return (this.mDisplayInfo.flags & 4096) == 0;
    }

    WindowState findFocusedWindowIfNeeded(int i) {
        if (hasOwnFocus() || i == -1 || ((IMirageDisplayManagerExt) ExtLoader.type(IMirageDisplayManagerExt.class).create()).isMirageDisplayEnabled() || this.mDisplayContentExt.isActivityPreloadDisplay(this)) {
            return findFocusedWindow();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState findFocusedWindow() {
        this.mTmpWindow = null;
        forAllWindows(this.mFindFocusedWindow, true);
        WindowState windowState = this.mTmpWindow;
        if (windowState != null) {
            return windowState;
        }
        if (ProtoLogCache.WM_DEBUG_FOCUS_LIGHT_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, 620519522, 1, (String) null, new Object[]{Long.valueOf(getDisplayId())});
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateFocusedWindowLocked(int i, boolean z, int i2) {
        boolean z2;
        ActivityRecord activityRecord;
        WindowState windowState = this.mCurrentFocus;
        if (windowState != null && this.mTransitionController.shouldKeepFocus(windowState) && (activityRecord = this.mFocusedApp) != null && this.mCurrentFocus.isDescendantOf(activityRecord) && this.mCurrentFocus.isVisible() && this.mCurrentFocus.isFocusable()) {
            Slog.w(TAG, "Current transition prevents automatic focus change, currentFocus:" + this.mCurrentFocus + " focusedApp:" + this.mFocusedApp);
            if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_FOCUS, -464564167, 0, (String) null, (Object[]) null);
            }
            return false;
        }
        WindowState findFocusedWindowIfNeeded = findFocusedWindowIfNeeded(i2);
        if (this.mCurrentFocus == findFocusedWindowIfNeeded) {
            this.mDisplayContentExt.hookForComplexScene(findFocusedWindowIfNeeded, z);
            return false;
        }
        this.mDisplayContentExt.beginHookUpdateFocusedWindowLocked(this, findFocusedWindowIfNeeded, this.mWmService);
        if (this.mInputMethodWindow != null) {
            z2 = this.mImeLayeringTarget != computeImeTarget(true);
            if (i != 1 && i != 3) {
                assignWindowLayers(false);
            }
            if (z2) {
                this.mWmService.mWindowsChanged = true;
                setLayoutNeeded();
                findFocusedWindowIfNeeded = findFocusedWindowIfNeeded(i2);
            }
        } else {
            z2 = false;
        }
        if (!ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT.isLogToLogcat()) {
            Slog.v(TAG, "Changing focus from " + this.mCurrentFocus + " to " + findFocusedWindowIfNeeded + ",diplayid=" + getDisplayId());
        } else if (ProtoLogCache.WM_DEBUG_FOCUS_LIGHT_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, 872933199, 16, (String) null, new Object[]{String.valueOf(this.mCurrentFocus), String.valueOf(findFocusedWindowIfNeeded), Long.valueOf(getDisplayId()), String.valueOf(Debug.getCallers(4))});
        }
        WindowState windowState2 = this.mCurrentFocus;
        this.mCurrentFocus = findFocusedWindowIfNeeded;
        if (findFocusedWindowIfNeeded != null) {
            this.mWinAddedSinceNullFocus.clear();
            this.mWinRemovedSinceNullFocus.clear();
            if (findFocusedWindowIfNeeded.canReceiveKeys()) {
                findFocusedWindowIfNeeded.mToken.paused = false;
            }
        }
        this.mDisplayContentExt.disableStatusBarForSystem(this.mWmService, findFocusedWindowIfNeeded);
        getDisplayPolicy().focusChangedLw(windowState2, findFocusedWindowIfNeeded);
        this.mAtmService.mBackNavigationController.onFocusChanged(findFocusedWindowIfNeeded);
        if (z2 && windowState2 != this.mInputMethodWindow) {
            if (i == 2) {
                performLayout(true, z);
            } else if (i == 3) {
                assignWindowLayers(false);
            }
        }
        if (i != 1) {
            getInputMonitor().setInputFocusLw(findFocusedWindowIfNeeded, z);
        }
        adjustForImeIfNeeded();
        updateKeepClearAreas();
        scheduleToastWindowsTimeoutIfNeededLocked(windowState2, findFocusedWindowIfNeeded);
        if (i == 2) {
            this.pendingLayoutChanges |= 8;
        }
        if (this.mWmService.mAccessibilityController.hasCallbacks()) {
            this.mWmService.mH.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda29
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DisplayContent.this.updateAccessibilityOnWindowFocusChanged((AccessibilityController) obj);
                }
            }, this.mWmService.mAccessibilityController));
        }
        this.mDisplayContentExt.endHookUpdateFocusedWindowLocked(this, this.mWmService, i2);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateAccessibilityOnWindowFocusChanged(AccessibilityController accessibilityController) {
        accessibilityController.onWindowFocusChangedNot(getDisplayId());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setFocusedApp(ActivityRecord activityRecord) {
        if (activityRecord != null) {
            DisplayContent displayContent = activityRecord.getDisplayContent();
            if (displayContent != this) {
                if (getWrapper().getNonStaticExtImpl().isPuttDisplay()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(activityRecord);
                    sb.append(" is not on ");
                    sb.append(getName());
                    sb.append(" but ");
                    sb.append(displayContent != null ? displayContent.getName() : "none");
                    StringBuilder sb2 = new StringBuilder(sb.toString());
                    for (WindowContainer parent = activityRecord.getParent(); parent != null; parent = parent.getParent()) {
                        sb2.append(" p: " + parent.toString());
                    }
                    Slog.e(TAG, sb2.toString());
                    return false;
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append(activityRecord);
                sb3.append(" is not on ");
                sb3.append(getName());
                sb3.append(" but ");
                sb3.append(displayContent != null ? displayContent.getName() : "none");
                throw new IllegalStateException(sb3.toString());
            }
            onLastFocusedTaskDisplayAreaChanged(activityRecord.getDisplayArea());
        }
        if (this.mFocusedApp == activityRecord) {
            return false;
        }
        if (ProtoLogCache.WM_DEBUG_FOCUS_LIGHT_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -639217716, 4, (String) null, new Object[]{String.valueOf(activityRecord), Long.valueOf(getDisplayId()), String.valueOf(Debug.getCallers(4))});
        }
        ActivityRecord activityRecord2 = this.mFocusedApp;
        Task task = activityRecord2 != null ? activityRecord2.getTask() : null;
        Task task2 = activityRecord != null ? activityRecord.getTask() : null;
        this.mDisplayContentExt.savedSurface(this.mWmService);
        this.mFocusedApp = activityRecord;
        this.mWmService.getWrapper().getExtImpl().cpuFrequencyBoostIfNeed(activityRecord);
        if (task != task2) {
            if (task != null) {
                task.onAppFocusChanged(false);
            }
            if (task2 != null) {
                task2.onAppFocusChanged(true);
            }
        }
        this.mDisplayContentExt.onDisplayFocusedAppChanged(this, activityRecord);
        getInputMonitor().setFocusedAppLw(activityRecord);
        updateTouchExcludeRegion();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onRunningActivityChanged() {
        this.mDwpcHelper.onRunningActivityChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLastFocusedTaskDisplayAreaChanged(TaskDisplayArea taskDisplayArea) {
        this.mOrientationRequestingTaskDisplayArea = taskDisplayArea;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TaskDisplayArea getOrientationRequestingTaskDisplayArea() {
        return this.mOrientationRequestingTaskDisplayArea;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void assignWindowLayers(boolean z) {
        Trace.traceBegin(32L, "assignWindowLayers");
        assignChildLayers(getSyncTransaction());
        if (z) {
            setLayoutNeeded();
        }
        scheduleAnimation();
        Trace.traceEnd(32L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean destroyLeakedSurfaces() {
        this.mTmpWindow = null;
        final SurfaceControl.Transaction transaction = this.mWmService.mTransactionFactory.get();
        forAllWindows(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.this.lambda$destroyLeakedSurfaces$27(transaction, (WindowState) obj);
            }
        }, false);
        transaction.apply();
        return this.mTmpWindow != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$destroyLeakedSurfaces$27(SurfaceControl.Transaction transaction, WindowState windowState) {
        WindowStateAnimator windowStateAnimator = windowState.mWinAnimator;
        if (windowStateAnimator.mSurfaceController == null) {
            return;
        }
        if (!this.mWmService.mSessions.contains(windowStateAnimator.mSession)) {
            Slog.w(TAG, "LEAKED SURFACE (session doesn't exist): " + windowState + " surface=" + windowStateAnimator.mSurfaceController + " token=" + windowState.mToken + " pid=" + windowState.mSession.mPid + " uid=" + windowState.mSession.mUid);
            windowStateAnimator.destroySurface(transaction);
            this.mWmService.mForceRemoves.add(windowState);
            this.mTmpWindow = windowState;
            return;
        }
        ActivityRecord activityRecord = windowState.mActivityRecord;
        if (activityRecord == null || activityRecord.isClientVisible()) {
            return;
        }
        Slog.w(TAG, "LEAKED SURFACE (app token hidden): " + windowState + " surface=" + windowStateAnimator.mSurfaceController + " token=" + windowState.mActivityRecord);
        if (ProtoLogCache.WM_SHOW_TRANSACTIONS_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_TRANSACTIONS, -1938839202, 0, (String) null, new Object[]{String.valueOf(windowState)});
        }
        windowStateAnimator.destroySurface(transaction);
        this.mTmpWindow = windowState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasAlertWindowSurfaces() {
        for (int size = this.mWmService.mSessions.size() - 1; size >= 0; size--) {
            if (this.mWmService.mSessions.valueAt(size).hasAlertWindowSurfaces(this)) {
                return true;
            }
        }
        return false;
    }

    void setInputMethodWindowLocked(WindowState windowState) {
        this.mDisplayContentExt.setInputMethodWindow(this.mInputMethodWindow, windowState);
        this.mInputMethodWindow = windowState;
        if (windowState != null) {
            this.mAtmService.onImeWindowSetOnDisplayArea(windowState.mSession.mPid, this.mImeWindowsContainer);
        }
        this.mInsetsStateController.getImeSourceProvider().setWindowContainer(windowState, this.mDisplayPolicy.getImeSourceFrameProvider(), null);
        computeImeTarget(true);
        updateImeControlTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState computeImeTarget(boolean z) {
        if (this.mInputMethodWindow == null) {
            if (z) {
                if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                    Slog.w(TAG, "Moving IM target from " + this.mImeLayeringTarget + " to null since mInputMethodWindow is null");
                }
                setImeLayeringTargetInner(null);
            }
            return null;
        }
        WindowState windowState = this.mImeLayeringTarget;
        if (!canUpdateImeTarget()) {
            if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                Slog.w(TAG, "Defer updating IME target");
            }
            this.mUpdateImeRequestedWhileDeferred = true;
            return windowState;
        }
        this.mUpdateImeTarget = z;
        WindowState window = getWindow(this.mComputeImeTargetPredicate);
        if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD && z) {
            Slog.v(TAG, "Proposed new IME target: " + window + " for display: " + getDisplayId());
        }
        if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
            Slog.v(TAG, "Desired input method target=" + window + " updateImeTarget=" + z);
        }
        String str = "";
        if (window == null) {
            if (z) {
                if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Moving IM target from ");
                    sb.append(windowState);
                    sb.append(" to null.");
                    if (WindowManagerDebugConfig.SHOW_STACK_CRAWLS) {
                        str = " Callers=" + Debug.getCallers(4);
                    }
                    sb.append(str);
                    Slog.w(TAG, sb.toString());
                }
                setImeLayeringTargetInner(null);
            }
            return null;
        }
        if (z) {
            if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Moving IM target from ");
                sb2.append(windowState);
                sb2.append(" to ");
                sb2.append(window);
                if (WindowManagerDebugConfig.SHOW_STACK_CRAWLS) {
                    str = " Callers=" + Debug.getCallers(4);
                }
                sb2.append(str);
                Slog.w(TAG, sb2.toString());
            }
            setImeLayeringTargetInner(window);
        }
        return window;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void computeImeTargetIfNeeded(ActivityRecord activityRecord) {
        WindowState windowState = this.mImeLayeringTarget;
        if (windowState == null || windowState.mActivityRecord != activityRecord) {
            return;
        }
        computeImeTarget(true);
    }

    private boolean isImeControlledByApp() {
        InputTarget inputTarget = this.mImeInputTarget;
        return inputTarget != null && inputTarget.shouldControlIme();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldImeAttachedToApp() {
        WindowState windowState;
        if (this.mImeWindowsContainer.isOrganized()) {
            return false;
        }
        if ((this.mMagnificationSpec == null) && isImeControlledByApp() && (windowState = this.mImeLayeringTarget) != null && windowState.mActivityRecord != null && windowState.getWindowingMode() == 1 && this.mImeLayeringTarget.matchesDisplayAreaBounds()) {
            return this.mImeLayeringTarget.mActivityRecord.getTask() == null || !this.mImeLayeringTarget.mActivityRecord.getTask().getWrapper().getExtImpl().isTaskEmbedded();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isImeAttachedToApp() {
        SurfaceControl surfaceControl;
        return shouldImeAttachedToApp() && (surfaceControl = this.mInputMethodSurfaceParent) != null && surfaceControl.isSameSurface(this.mImeLayeringTarget.mActivityRecord.getSurfaceControl());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsControlTarget getImeHostOrFallback(WindowState windowState) {
        return (windowState == null || windowState.getDisplayContent().getImePolicy() != 0) ? getImeFallback() : windowState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsControlTarget getImeFallback() {
        DisplayContent defaultDisplayContentLocked = this.mWmService.getDefaultDisplayContentLocked();
        WindowState statusBar = defaultDisplayContentLocked.getDisplayPolicy().getStatusBar();
        return statusBar != null ? statusBar : defaultDisplayContentLocked.mRemoteInsetsControlTarget;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsControlTarget getImeTarget(int i) {
        if (i == 0) {
            return this.mImeLayeringTarget;
        }
        if (i != 2) {
            return null;
        }
        return this.mImeControlTarget;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputTarget getImeInputTarget() {
        return this.mImeInputTarget;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WindowManager.DisplayImePolicy
    public int getImePolicy() {
        if (((IMirageDisplayManagerExt) ExtLoader.type(IMirageDisplayManagerExt.class).create()).isMirageDisplay(getDisplayId()) || getWrapper().getNonStaticExtImpl().isPuttDisplay() || this.mDisplayContentExt.isSupportedIMEOnSecondDisplay(this.mDisplayContent)) {
            return 0;
        }
        if (!isTrusted()) {
            return 1;
        }
        int imePolicyLocked = this.mWmService.mDisplayWindowSettings.getImePolicyLocked(this);
        if (imePolicyLocked == 1 && forceDesktopMode()) {
            return 0;
        }
        return imePolicyLocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean forceDesktopMode() {
        return (!this.mWmService.mForceDesktopModeOnExternalDisplays || this.isDefaultDisplay || isPrivate()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onShowImeRequested() {
        ActivityRecord activityRecord;
        WindowState windowState = this.mInputMethodWindow;
        if (windowState == null || (activityRecord = this.mFixedRotationLaunchingApp) == null) {
            return;
        }
        windowState.mToken.linkFixedRotationTransform(activityRecord);
        AsyncRotationController asyncRotationController = this.mAsyncRotationController;
        if (asyncRotationController != null) {
            asyncRotationController.hideImeImmediately();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void setImeLayeringTarget(WindowState windowState) {
        this.mImeLayeringTarget = windowState;
    }

    private void setImeLayeringTargetInner(WindowState windowState) {
        RootDisplayArea rootDisplayArea;
        WindowState windowState2 = this.mImeLayeringTarget;
        if (windowState == windowState2 && this.mLastImeInputTarget == this.mImeInputTarget) {
            return;
        }
        InputTarget inputTarget = this.mImeInputTarget;
        this.mLastImeInputTarget = inputTarget;
        if (windowState2 != null && windowState2 == inputTarget) {
            boolean z = windowState2.mAnimatingExit && windowState2.mAttrs.type != 1 && windowState2.isSelfAnimating(0, 16);
            if (this.mImeLayeringTarget.inTransitionSelfOrParent() || z) {
                showImeScreenshot();
            }
        }
        this.mDisplayContentExt.removeImeSurfaceImmediately(this, windowState);
        if (ProtoLogCache.WM_DEBUG_IME_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_IME, 2119122320, 0, (String) null, new Object[]{String.valueOf(windowState)});
        }
        boolean z2 = windowState != this.mImeLayeringTarget;
        this.mImeLayeringTarget = windowState;
        if (windowState != null && !this.mImeWindowsContainer.isOrganized() && (rootDisplayArea = windowState.getRootDisplayArea()) != null && rootDisplayArea != this.mImeWindowsContainer.getRootDisplayArea() && rootDisplayArea.placeImeContainer(this.mImeWindowsContainer)) {
            WindowState windowState3 = this.mInputMethodWindow;
            if (windowState3 != null) {
                windowState3.hide(false, false);
            }
            z2 = true;
        }
        assignWindowLayers(true);
        InsetsStateController insetsStateController = this.mInsetsStateController;
        insetsStateController.updateAboveInsetsState(insetsStateController.getRawInsetsState().isSourceOrDefaultVisible(InsetsSource.ID_IME, WindowInsets.Type.ime()));
        updateImeControlTarget(z2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void setImeInputTarget(InputTarget inputTarget) {
        final WindowState windowState;
        Pair<IBinder, WindowContainerListener> pair = this.mImeTargetTokenListenerPair;
        if (pair != null) {
            WindowToken windowToken = this.mTokenMap.get(pair.first);
            if (windowToken != null) {
                windowToken.unregisterWindowContainerListener((WindowContainerListener) this.mImeTargetTokenListenerPair.second);
            }
            this.mImeTargetTokenListenerPair = null;
        }
        this.mImeInputTarget = inputTarget;
        if (inputTarget != null && (windowState = inputTarget.getWindowState()) != null) {
            Pair<IBinder, WindowContainerListener> pair2 = new Pair<>(windowState.mToken.token, new WindowContainerListener() { // from class: com.android.server.wm.DisplayContent.3
                @Override // com.android.server.wm.WindowContainerListener
                public void onVisibleRequestedChanged(boolean z) {
                    WindowManagerService windowManagerService = DisplayContent.this.mWmService;
                    IBinder asBinder = windowState.mClient.asBinder();
                    ActivityRecord activityRecord = windowState.mActivityRecord;
                    windowManagerService.dispatchImeInputTargetVisibilityChanged(asBinder, z, activityRecord != null && activityRecord.finishing);
                }
            });
            this.mImeTargetTokenListenerPair = pair2;
            windowState.mToken.registerWindowContainerListener((WindowContainerListener) pair2.second);
            this.mWmService.dispatchImeInputTargetVisibilityChanged(windowState.mClient.asBinder(), windowState.isVisible(), false);
        }
        if (refreshImeSecureFlag(getPendingTransaction())) {
            this.mWmService.requestTraversal();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean refreshImeSecureFlag(SurfaceControl.Transaction transaction) {
        InputTarget inputTarget = this.mImeInputTarget;
        return this.mImeWindowsContainer.setCanScreenshot(transaction, inputTarget == null || inputTarget.canScreenshotIme());
    }

    @VisibleForTesting
    void setImeControlTarget(InsetsControlTarget insetsControlTarget) {
        this.mImeControlTarget = insetsControlTarget;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class ImeScreenshot {
        private SurfaceControl mImeSurface;
        private Point mImeSurfacePosition;
        private WindowState mImeTarget;
        private SurfaceControl.Builder mSurfaceBuilder;

        ImeScreenshot(SurfaceControl.Builder builder, WindowState windowState) {
            this.mSurfaceBuilder = builder;
            this.mImeTarget = windowState;
        }

        WindowState getImeTarget() {
            return this.mImeTarget;
        }

        @VisibleForTesting
        SurfaceControl getImeScreenshotSurface() {
            return this.mImeSurface;
        }

        private SurfaceControl createImeSurface(ScreenCapture.ScreenshotHardwareBuffer screenshotHardwareBuffer, SurfaceControl.Transaction transaction) {
            SurfaceControl surfaceControl;
            HardwareBuffer hardwareBuffer = screenshotHardwareBuffer.getHardwareBuffer();
            if (ProtoLogCache.WM_DEBUG_IME_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_IME, -1777010776, 0, (String) null, new Object[]{String.valueOf(this.mImeTarget), String.valueOf(hardwareBuffer.getWidth()), String.valueOf(hardwareBuffer.getHeight())});
            }
            WindowState windowState = this.mImeTarget.getDisplayContent().mInputMethodWindow;
            WindowState windowState2 = this.mImeTarget;
            ActivityRecord activityRecord = windowState2.mActivityRecord;
            if (windowState2.mAttrs.type == 1) {
                surfaceControl = activityRecord.getSurfaceControl();
            } else {
                surfaceControl = windowState2.getSurfaceControl();
            }
            SurfaceControl build = this.mSurfaceBuilder.setName("IME-snapshot-surface").setBLASTLayer().setFormat(hardwareBuffer.getFormat()).setParent(surfaceControl).setCallsite("DisplayContent.attachAndShowImeScreenshotOnTarget").build();
            InputMonitor.setTrustedOverlayInputInfo(build, transaction, windowState.getDisplayId(), "IME-snapshot-surface");
            transaction.setBuffer(build, hardwareBuffer);
            transaction.setColorSpace(activityRecord.mSurfaceControl, ColorSpace.get(ColorSpace.Named.SRGB));
            transaction.setLayer(build, 1);
            Point point = new Point(windowState.getFrame().left, windowState.getFrame().top);
            if (surfaceControl == activityRecord.getSurfaceControl()) {
                transaction.setPosition(build, point.x, point.y);
            } else {
                point.offset(-this.mImeTarget.getFrame().left, -this.mImeTarget.getFrame().top);
                Rect rect = this.mImeTarget.mAttrs.surfaceInsets;
                point.offset(rect.left, rect.top);
                transaction.setPosition(build, point.x, point.y);
            }
            this.mImeSurfacePosition = point;
            if (ProtoLogCache.WM_DEBUG_IME_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_IME, -1814361639, 5, (String) null, new Object[]{Long.valueOf(point.x), Long.valueOf(point.y)});
            }
            return build;
        }

        private void removeImeSurface(SurfaceControl.Transaction transaction) {
            if (this.mImeSurface != null) {
                if (ProtoLogCache.WM_DEBUG_IME_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_IME, -2111539867, 0, (String) null, new Object[]{String.valueOf(Debug.getCallers(6))});
                }
                transaction.remove(this.mImeSurface);
                this.mImeSurface = null;
            }
            if (ImeTracker.DEBUG_IME_VISIBILITY) {
                EventLog.writeEvent(EventLogTags.IMF_REMOVE_IME_SCREENSHOT, this.mImeTarget.toString());
            }
        }

        void attachAndShow(SurfaceControl.Transaction transaction) {
            DisplayContent displayContent = this.mImeTarget.getDisplayContent();
            Task task = this.mImeTarget.getTask();
            SurfaceControl surfaceControl = this.mImeSurface;
            boolean z = (surfaceControl != null && surfaceControl.getWidth() == displayContent.mInputMethodWindow.getFrame().width() && this.mImeSurface.getHeight() == displayContent.mInputMethodWindow.getFrame().height()) ? false : true;
            if (task != null && !task.isActivityTypeHomeOrRecents()) {
                ScreenCapture.ScreenshotHardwareBuffer snapshotImeFromAttachedTask = z ? displayContent.mWmService.mTaskSnapshotController.snapshotImeFromAttachedTask(task) : null;
                if (snapshotImeFromAttachedTask != null) {
                    removeImeSurface(transaction);
                    this.mImeSurface = createImeSurface(snapshotImeFromAttachedTask, transaction);
                }
            }
            SurfaceControl surfaceControl2 = this.mImeSurface;
            boolean z2 = surfaceControl2 != null && surfaceControl2.isValid();
            if (!z2 || !displayContent.getInsetsStateController().getImeSourceProvider().isImeShowing()) {
                if (z2) {
                    return;
                }
                removeImeSurface(transaction);
            } else {
                if (ProtoLogCache.WM_DEBUG_IME_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_IME, -57750640, 0, (String) null, new Object[]{String.valueOf(this.mImeTarget), String.valueOf(Debug.getCallers(6))});
                }
                transaction.show(this.mImeSurface);
                if (ImeTracker.DEBUG_IME_VISIBILITY) {
                    EventLog.writeEvent(EventLogTags.IMF_SHOW_IME_SCREENSHOT, this.mImeTarget.toString(), Integer.valueOf(displayContent.mInputMethodWindow.mTransitFlags), this.mImeSurfacePosition.toString());
                }
            }
        }

        void detach(SurfaceControl.Transaction transaction) {
            removeImeSurface(transaction);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(64);
            sb.append("ImeScreenshot{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" imeTarget=" + this.mImeTarget);
            sb.append(" surface=" + this.mImeSurface);
            sb.append('}');
            return sb.toString();
        }
    }

    private void attachImeScreenshotOnTargetIfNeeded() {
        WindowState windowState;
        if (shouldImeAttachedToApp() && this.mWmService.mPolicy.isScreenOn() && (windowState = this.mInputMethodWindow) != null && windowState.isVisible() && !this.mInputMethodWindow.isSecureLocked()) {
            attachImeScreenshotOnTarget(this.mImeLayeringTarget);
        }
    }

    private void attachImeScreenshotOnTarget(WindowState windowState) {
        attachImeScreenshotOnTarget(windowState, false);
    }

    private void attachImeScreenshotOnTarget(WindowState windowState, boolean z) {
        SurfaceControl.Transaction pendingTransaction = getPendingTransaction();
        removeImeSurfaceImmediately();
        ImeScreenshot imeScreenshot = new ImeScreenshot(this.mWmService.mSurfaceControlFactory.apply(null), windowState);
        this.mImeScreenshot = imeScreenshot;
        imeScreenshot.attachAndShow(pendingTransaction);
        this.mDisplayContentExt.setLastImeLayeringTarget(this.mImeLayeringTarget);
        WindowState windowState2 = this.mInputMethodWindow;
        if (windowState2 == null || !z) {
            return;
        }
        windowState2.hide(false, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void showImeScreenshot() {
        attachImeScreenshotOnTargetIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void showImeScreenshot(WindowState windowState) {
        attachImeScreenshotOnTarget(windowState, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeImeSurfaceByTarget(WindowContainer windowContainer) {
        if (this.mImeScreenshot == null || windowContainer == null) {
            return;
        }
        if (windowContainer.asWindowState() == null || windowContainer.asWindowState().mAttrs.type != 3) {
            final WindowState imeTarget = this.mImeScreenshot.getImeTarget();
            if (windowContainer == imeTarget || windowContainer.getWindow(new Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda27
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$removeImeSurfaceByTarget$28;
                    lambda$removeImeSurfaceByTarget$28 = DisplayContent.lambda$removeImeSurfaceByTarget$28(WindowState.this, obj);
                    return lambda$removeImeSurfaceByTarget$28;
                }
            }) != null) {
                removeImeSurfaceImmediately();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeImeSurfaceImmediately() {
        ImeScreenshot imeScreenshot = this.mImeScreenshot;
        if (imeScreenshot != null) {
            imeScreenshot.detach(getSyncTransaction());
            this.mImeScreenshot = null;
            this.mDisplayContentExt.setLastImeLayeringTarget(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateImeInputAndControlTarget(InputTarget inputTarget) {
        SurfaceControl surfaceControl;
        InputTarget inputTarget2 = this.mImeInputTarget;
        if (inputTarget2 != inputTarget) {
            boolean z = false;
            if (ProtoLogCache.WM_DEBUG_IME_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_IME, -322743468, 0, (String) null, new Object[]{String.valueOf(inputTarget)});
            }
            InputTarget inputTarget3 = this.mImeInputTarget;
            if (inputTarget == inputTarget3) {
                if (this.mDisplayContentExt.updateImeTarget(inputTarget3 != null ? inputTarget3.getWindowState() : null)) {
                    return;
                }
            }
            setImeInputTarget(inputTarget);
            InsetsStateController insetsStateController = this.mInsetsStateController;
            insetsStateController.updateAboveInsetsState(insetsStateController.getRawInsetsState().isSourceOrDefaultVisible(InsetsSource.ID_IME, WindowInsets.Type.ime()));
            if (this.mImeControlTarget == this.mRemoteInsetsControlTarget && (surfaceControl = this.mInputMethodSurfaceParent) != null && !surfaceControl.isSameSurface(this.mImeWindowsContainer.getParent().mSurfaceControl)) {
                z = true;
            }
            updateImeControlTarget(z);
            return;
        }
        if (inputTarget2 == null || inputTarget2.getWindowState() == this.mImeLayeringTarget) {
            return;
        }
        InsetsStateController insetsStateController2 = this.mInsetsStateController;
        insetsStateController2.updateAboveInsetsState(insetsStateController2.getRawInsetsState().isSourceOrDefaultVisible(InsetsSource.ID_IME, WindowInsets.Type.ime()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onImeInsetsClientVisibilityUpdate() {
        final boolean[] zArr = new boolean[1];
        InputTarget inputTarget = this.mImeInputTarget;
        ActivityRecord activityRecord = inputTarget != null ? inputTarget.getActivityRecord() : null;
        if ((this.mImeInputTarget != this.mLastImeInputTarget) || (activityRecord != null && activityRecord.isVisibleRequested() && activityRecord.mImeInsetsFrozenUntilStartInput)) {
            forAllActivities(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DisplayContent.lambda$onImeInsetsClientVisibilityUpdate$29(zArr, (ActivityRecord) obj);
                }
            });
        }
        return zArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onImeInsetsClientVisibilityUpdate$29(boolean[] zArr, ActivityRecord activityRecord) {
        if (activityRecord.mImeInsetsFrozenUntilStartInput && activityRecord.isVisibleRequested()) {
            activityRecord.mImeInsetsFrozenUntilStartInput = false;
            zArr[0] = true;
        }
    }

    void updateImeControlTarget() {
        updateImeControlTarget(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateImeControlTarget(boolean z) {
        InsetsControlTarget insetsControlTarget = this.mImeControlTarget;
        InsetsControlTarget computeImeControlTarget = computeImeControlTarget();
        this.mImeControlTarget = computeImeControlTarget;
        this.mInsetsStateController.onImeControlTargetChanged(computeImeControlTarget);
        if ((insetsControlTarget != this.mImeControlTarget) || z) {
            updateImeParent();
        }
        WindowState asWindowOrNull = InsetsControlTarget.asWindowOrNull(this.mImeControlTarget);
        final IBinder asBinder = asWindowOrNull != null ? asWindowOrNull.mClient.asBinder() : null;
        this.mWmService.mH.post(new Runnable() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                DisplayContent.lambda$updateImeControlTarget$30(asBinder);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateImeControlTarget$30(IBinder iBinder) {
        InputMethodManagerInternal.get().reportImeControl(iBinder);
    }

    void updateImeParent() {
        if (this.mImeWindowsContainer.isOrganized()) {
            if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                Slog.i(TAG, "ImeContainer is organized. Skip updateImeParent.");
            }
            this.mInputMethodSurfaceParent = null;
            return;
        }
        SurfaceControl computeImeParent = computeImeParent();
        if (computeImeParent == null || computeImeParent == this.mInputMethodSurfaceParent) {
            return;
        }
        this.mInputMethodSurfaceParent = computeImeParent;
        getSyncTransaction().reparent(this.mImeWindowsContainer.mSurfaceControl, computeImeParent);
        if (ImeTracker.DEBUG_IME_VISIBILITY) {
            EventLog.writeEvent(EventLogTags.IMF_UPDATE_IME_PARENT, computeImeParent.toString());
        }
        assignRelativeLayerForIme(getSyncTransaction(), true);
        scheduleAnimation();
        this.mWmService.mH.post(new Runnable() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                DisplayContent.lambda$updateImeParent$31();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateImeParent$31() {
        InputMethodManagerInternal.get().onImeParentChanged();
    }

    @VisibleForTesting
    InsetsControlTarget computeImeControlTarget() {
        InputTarget inputTarget = this.mImeInputTarget;
        if (inputTarget == null) {
            return null;
        }
        WindowState windowState = inputTarget.getWindowState();
        return ((isImeControlledByApp() || this.mRemoteInsetsControlTarget == null) && getImeHostOrFallback(windowState) != this.mRemoteInsetsControlTarget) ? windowState : this.mRemoteInsetsControlTarget;
    }

    @VisibleForTesting
    SurfaceControl computeImeParent() {
        if (!ImeTargetVisibilityPolicy.canComputeImeParent(this.mImeLayeringTarget, this.mImeInputTarget) && !this.mDisplayContentExt.HasZoomWindowAboveImeInputTarget(this.mImeLayeringTarget, this.mImeInputTarget, this.mImeWindowsContainer.getParent())) {
            return null;
        }
        if (shouldImeAttachedToApp()) {
            return this.mImeLayeringTarget.mActivityRecord.getSurfaceControl();
        }
        if (this.mImeWindowsContainer.getParent() != null) {
            return this.mImeWindowsContainer.getParent().getSurfaceControl();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLayoutNeeded() {
        if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
            Slog.w(TAG, "setLayoutNeeded: callers=" + Debug.getCallers(3));
        }
        this.mLayoutNeeded = true;
    }

    private void clearLayoutNeeded() {
        if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
            Slog.w(TAG, "clearLayoutNeeded: callers=" + Debug.getCallers(3));
        }
        this.mLayoutNeeded = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLayoutNeeded() {
        return this.mLayoutNeeded;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpTokens(PrintWriter printWriter, boolean z) {
        if (this.mTokenMap.isEmpty()) {
            return;
        }
        printWriter.println("  Display #" + this.mDisplayId);
        printWriter.println("    mInTouchMode=" + this.mInTouchMode);
        for (WindowToken windowToken : this.mTokenMap.values()) {
            printWriter.print("  ");
            printWriter.print(windowToken);
            if (z) {
                printWriter.println(':');
                windowToken.dump(printWriter, "    ", z);
            } else {
                printWriter.println();
            }
        }
        if (!this.mOpeningApps.isEmpty() || !this.mClosingApps.isEmpty() || !this.mChangingContainers.isEmpty()) {
            printWriter.println();
            if (this.mOpeningApps.size() > 0) {
                printWriter.print("  mOpeningApps=");
                printWriter.println(this.mOpeningApps);
            }
            if (this.mClosingApps.size() > 0) {
                printWriter.print("  mClosingApps=");
                printWriter.println(this.mClosingApps);
            }
            if (this.mChangingContainers.size() > 0) {
                printWriter.print("  mChangingApps=");
                printWriter.println(this.mChangingContainers);
            }
        }
        this.mUnknownAppVisibilityController.dump(printWriter, "  ");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpWindowAnimators(final PrintWriter printWriter, final String str) {
        final int[] iArr = new int[1];
        forAllWindows(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda59
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.lambda$dumpWindowAnimators$32(printWriter, str, iArr, (WindowState) obj);
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dumpWindowAnimators$32(PrintWriter printWriter, String str, int[] iArr, WindowState windowState) {
        printWriter.println(str + "Window #" + iArr[0] + ": " + windowState.mWinAnimator);
        iArr[0] = iArr[0] + 1;
    }

    void startKeyguardExitOnNonAppWindows(final boolean z, final boolean z2, final boolean z3) {
        final WindowManagerPolicy windowManagerPolicy = this.mWmService.mPolicy;
        forAllWindows(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda21
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.this.lambda$startKeyguardExitOnNonAppWindows$33(z, z2, z3, windowManagerPolicy, (WindowState) obj);
            }
        }, true);
        for (int size = this.mShellRoots.size() - 1; size >= 0; size--) {
            this.mShellRoots.valueAt(size).startAnimation(windowManagerPolicy.createHiddenByKeyguardExit(z, z2, z3));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startKeyguardExitOnNonAppWindows$33(boolean z, boolean z2, boolean z3, WindowManagerPolicy windowManagerPolicy, WindowState windowState) {
        if (windowState.mActivityRecord == null && windowState.canBeHiddenByKeyguard() && windowState.wouldBeVisibleIfPolicyIgnored() && !windowState.isVisible() && !this.mDisplayContentExt.startKeyguardExitOnNonAppWindows(windowState, z, z2, z3)) {
            windowState.startAnimation(windowManagerPolicy.createHiddenByKeyguardExit(z, z2, z3));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldWaitForSystemDecorWindowsOnBoot() {
        if ((!this.isDefaultDisplay && !supportsSystemDecorations()) || this.mDisplayContentExt.shouldNotWaitForDisplayOnBoot(this)) {
            return false;
        }
        final SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        sparseBooleanArray.put(2040, true);
        WindowState window = getWindow(new Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda51
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$shouldWaitForSystemDecorWindowsOnBoot$34;
                lambda$shouldWaitForSystemDecorWindowsOnBoot$34 = DisplayContent.this.lambda$shouldWaitForSystemDecorWindowsOnBoot$34(sparseBooleanArray, (WindowState) obj);
                return lambda$shouldWaitForSystemDecorWindowsOnBoot$34;
            }
        });
        if (window != null) {
            this.mDisplayContentExt.debugForBootTime(TAG, window);
            return true;
        }
        boolean z = this.mWmService.mContext.getResources().getBoolean(17891682) && this.mWmService.mContext.getResources().getBoolean(R.bool.config_windowNoTitleDefault);
        boolean z2 = sparseBooleanArray.get(2021);
        boolean z3 = sparseBooleanArray.get(1);
        boolean z4 = sparseBooleanArray.get(2013);
        boolean z5 = sparseBooleanArray.get(2040);
        if (ProtoLogCache.WM_DEBUG_SCREEN_ON_enabled) {
            WindowManagerService windowManagerService = this.mWmService;
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_SCREEN_ON, -635082269, 16383, (String) null, new Object[]{Boolean.valueOf(windowManagerService.mSystemBooted), Boolean.valueOf(windowManagerService.mShowingBootMessages), Boolean.valueOf(z2), Boolean.valueOf(z3), Boolean.valueOf(z4), Boolean.valueOf(z), Boolean.valueOf(z5)});
        }
        boolean z6 = this.mWmService.mSystemBooted;
        if (z6 || z2) {
            return z6 && (!(z3 || z5) || (z && !z4));
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$shouldWaitForSystemDecorWindowsOnBoot$34(SparseBooleanArray sparseBooleanArray, WindowState windowState) {
        boolean z = windowState.isVisible() && !windowState.mObscured;
        boolean isDrawn = windowState.isDrawn();
        if (z && !isDrawn) {
            if (ProtoLogCache.WM_DEBUG_BOOT_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_BOOT, -381475323, 1, (String) null, new Object[]{Long.valueOf(windowState.mAttrs.type)});
            }
            return true;
        }
        if (isDrawn) {
            int i = windowState.mAttrs.type;
            if (i == 1 || i == 2013 || i == 2021) {
                sparseBooleanArray.put(i, true);
            } else if (i == 2040) {
                sparseBooleanArray.put(2040, this.mWmService.mPolicy.isKeyguardDrawnLw());
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateWindowsForAnimator() {
        forAllWindows(this.mUpdateWindowsForAnimator, true);
        AsyncRotationController asyncRotationController = this.mAsyncRotationController;
        if (asyncRotationController != null) {
            asyncRotationController.updateTargetWindows();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInputMethodClientFocus(int i, int i2) {
        WindowState computeImeTarget = computeImeTarget(false);
        if (computeImeTarget == null) {
            return false;
        }
        if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
            Slog.i(TAG, "Desired input method target: " + computeImeTarget);
            Slog.i(TAG, "Current focus: " + this.mCurrentFocus + " displayId=" + this.mDisplayId);
        }
        if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
            Slog.i(TAG, "IM target uid/pid: " + computeImeTarget.mSession.mUid + "/" + computeImeTarget.mSession.mPid);
            Slog.i(TAG, "Requesting client uid/pid: " + i + "/" + i2);
        }
        Session session = computeImeTarget.mSession;
        return session.mUid == i && session.mPid == i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$hasSecureWindowOnScreen$35(WindowState windowState) {
        return windowState.isOnScreen() && windowState.isSecureLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasSecureWindowOnScreen() {
        return getWindow(new Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda48
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$hasSecureWindowOnScreen$35;
                lambda$hasSecureWindowOnScreen$35 = DisplayContent.lambda$hasSecureWindowOnScreen$35((WindowState) obj);
                return lambda$hasSecureWindowOnScreen$35;
            }
        }) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onWindowFreezeTimeout() {
        Slog.w(TAG, "Window freeze timeout expired.");
        this.mWmService.mWindowsFreezingScreen = 2;
        forAllWindows(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda40
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.this.lambda$onWindowFreezeTimeout$36((WindowState) obj);
            }
        }, true);
        if (this.mWaitingForConfig) {
            this.mWaitingForConfig = false;
            this.mWmService.mLastFinishedFreezeSource = "config-wait-timeout";
            Slog.w(TAG, "onWindowFreezeTimeout set mWaitingForConfig to false");
        }
        this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onWindowFreezeTimeout$36(WindowState windowState) {
        if (windowState.getOrientationChanging()) {
            windowState.orientationChangeTimedOut();
            windowState.mLastFreezeDuration = (int) (SystemClock.elapsedRealtime() - this.mWmService.mDisplayFreezeTime);
            Slog.w(TAG, "Force clearing orientation change: " + windowState);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onWindowAnimationFinished(WindowContainer windowContainer, int i) {
        if (this.mImeScreenshot != null && ProtoLogCache.WM_DEBUG_IME_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_IME, -658964693, 0, (String) null, new Object[]{String.valueOf(windowContainer), String.valueOf(SurfaceAnimator.animationTypeToString(i)), String.valueOf(this.mImeScreenshot), String.valueOf(this.mImeScreenshot.getImeTarget())});
        }
        if ((i & 25) != 0) {
            removeImeSurfaceByTarget(windowContainer);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applySurfaceChangesTransaction() {
        WindowSurfacePlacer windowSurfacePlacer = this.mWmService.mWindowPlacerLocked;
        beginHoldScreenUpdate();
        this.mTmpUpdateAllDrawn.clear();
        if (WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
            windowSurfacePlacer.debugLayoutRepeats("On entry to LockedInner", this.pendingLayoutChanges);
        }
        if ((this.pendingLayoutChanges & 4) != 0) {
            this.mWallpaperController.adjustWallpaperWindows();
        }
        if ((this.pendingLayoutChanges & 2) != 0) {
            if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
                Slog.v(TAG, "Computing new config from layout");
            }
            if (updateOrientation()) {
                setLayoutNeeded();
                sendNewConfiguration();
            }
        }
        if ((this.pendingLayoutChanges & 1) != 0) {
            setLayoutNeeded();
        }
        performLayout(true, false);
        this.pendingLayoutChanges = 0;
        Trace.traceBegin(32L, "applyPostLayoutPolicy");
        try {
            this.mDisplayPolicy.beginPostLayoutPolicyLw();
            forAllWindows(this.mApplyPostLayoutPolicy, true);
            this.mDisplayPolicy.finishPostLayoutPolicyLw();
            Trace.traceEnd(32L);
            this.mInsetsStateController.onPostLayout();
            this.mTmpApplySurfaceChangesTransactionState.reset();
            Trace.traceBegin(32L, "applyWindowSurfaceChanges");
            try {
                forAllWindows(this.mApplySurfaceChangesTransaction, true);
                Trace.traceEnd(32L);
                prepareSurfaces();
                WallpaperController wallpaperController = this.mWallpaperController;
                wallpaperController.mWallpaperControllerExt.dispatchWallpaperWindowsTarget(wallpaperController.getWallpaperTarget(), this, this.mWallpaperController.getWallpaperTarget() != null);
                Trace.traceBegin(32L, "refreshRate");
                ApplySurfaceChangesTransactionState applySurfaceChangesTransactionState = this.mTmpApplySurfaceChangesTransactionState;
                applySurfaceChangesTransactionState.preferredModeId = this.mDisplayContentExt.getPreferredModeId(applySurfaceChangesTransactionState.preferredRefreshRate, applySurfaceChangesTransactionState.preferredModeId);
                ApplySurfaceChangesTransactionState applySurfaceChangesTransactionState2 = this.mTmpApplySurfaceChangesTransactionState;
                applySurfaceChangesTransactionState2.preferredMinRefreshRate = this.mDisplayContentExt.getPreferredMinRefreshRate(applySurfaceChangesTransactionState2.preferredMinRefreshRate);
                ApplySurfaceChangesTransactionState applySurfaceChangesTransactionState3 = this.mTmpApplySurfaceChangesTransactionState;
                applySurfaceChangesTransactionState3.preferredMaxRefreshRate = this.mDisplayContentExt.getPreferredMaxRefreshRate(applySurfaceChangesTransactionState3.preferredMaxRefreshRate);
                Trace.traceEnd(32L);
                this.mInsetsStateController.getImeSourceProvider().checkShowImePostLayout();
                this.mLastHasContent = this.mTmpApplySurfaceChangesTransactionState.displayHasContent;
                if (!inTransition() && !this.mDisplayRotation.isRotatingSeamlessly()) {
                    Trace.traceBegin(32L, "setDisplayProperties");
                    DisplayManagerInternal displayManagerInternal = this.mWmService.mDisplayManagerInternal;
                    int i = this.mDisplayId;
                    boolean z = this.mLastHasContent;
                    ApplySurfaceChangesTransactionState applySurfaceChangesTransactionState4 = this.mTmpApplySurfaceChangesTransactionState;
                    displayManagerInternal.setDisplayProperties(i, z, applySurfaceChangesTransactionState4.preferredRefreshRate, applySurfaceChangesTransactionState4.preferredModeId, applySurfaceChangesTransactionState4.preferredMinRefreshRate, applySurfaceChangesTransactionState4.preferredMaxRefreshRate, applySurfaceChangesTransactionState4.preferMinimalPostProcessing, applySurfaceChangesTransactionState4.disableHdrConversion, true);
                    Trace.traceEnd(32L);
                }
                updateRecording();
                Trace.traceBegin(32L, "notifyWallpaperVisibilityChanged");
                boolean isWallpaperVisible = this.mWallpaperController.isWallpaperVisible();
                if (isWallpaperVisible != this.mLastWallpaperVisible) {
                    this.mLastWallpaperVisible = isWallpaperVisible;
                    this.mDisplayContentExt.dispatchWallpaperVisibility(isWallpaperVisible);
                    this.mWmService.mWallpaperVisibilityListeners.notifyWallpaperVisibilityChanged(this);
                }
                Trace.traceEnd(32L);
                Trace.traceBegin(32L, "mTmpUpdateAllDrawn:" + this.mTmpUpdateAllDrawn.isEmpty());
                while (!this.mTmpUpdateAllDrawn.isEmpty()) {
                    this.mTmpUpdateAllDrawn.removeLast().updateAllDrawn();
                }
                finishHoldScreenUpdate();
            } finally {
            }
        } finally {
        }
    }

    private void getBounds(Rect rect, int i) {
        getBounds(rect);
        int deltaRotation = RotationUtils.deltaRotation(this.mDisplayInfo.rotation, i);
        if (deltaRotation == 1 || deltaRotation == 3) {
            rect.set(0, 0, rect.height(), rect.width());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getNaturalOrientation() {
        return this.mBaseDisplayWidth < this.mBaseDisplayHeight ? 1 : 2;
    }

    void performLayout(boolean z, boolean z2) {
        Trace.traceBegin(32L, "performLayout");
        try {
            performLayoutNoTrace(z, z2);
        } finally {
            Trace.traceEnd(32L);
        }
    }

    private void performLayoutNoTrace(boolean z, boolean z2) {
        if (isLayoutNeeded()) {
            clearLayoutNeeded();
            if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
                Slog.v(TAG, "-------------------------------------");
                Slog.v(TAG, "performLayout: dw=" + this.mDisplayInfo.logicalWidth + " dh=" + this.mDisplayInfo.logicalHeight);
            }
            this.mDisplayContentExt.performLayoutNoTrace(this.mDisplayPolicy, this.mDisplayFrames, getConfiguration().uiMode);
            int i = this.mLayoutSeq + 1;
            if (i < 0) {
                i = 0;
            }
            this.mLayoutSeq = i;
            this.mTmpInitial = z;
            forAllWindows(this.mPerformLayout, true);
            forAllWindows(this.mPerformLayoutAttached, true);
            this.mDisplayContentExt.onFindFocusedWindow();
            this.mInputMonitor.setUpdateInputWindowsNeededLw();
            if (z2) {
                this.mInputMonitor.updateInputWindowsLw(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bitmap screenshotDisplayLocked() {
        if (!this.mWmService.mPolicy.isScreenOn()) {
            if (WindowManagerDebugConfig.DEBUG_SCREENSHOT) {
                Slog.i(TAG, "Attempted to take screenshot while display was off.");
            }
            return null;
        }
        ScreenCapture.SynchronousScreenCaptureListener createSyncCaptureListener = ScreenCapture.createSyncCaptureListener();
        getBounds(this.mTmpRect);
        this.mTmpRect.offsetTo(0, 0);
        ScreenCapture.captureLayers(new ScreenCapture.LayerCaptureArgs.Builder(getSurfaceControl()).setSourceCrop(this.mTmpRect).build(), createSyncCaptureListener);
        ScreenCapture.ScreenshotHardwareBuffer buffer = createSyncCaptureListener.getBuffer();
        Bitmap asBitmap = buffer != null ? buffer.asBitmap() : null;
        if (asBitmap == null) {
            Slog.w(TAG, "Failed to take screenshot");
        }
        return asBitmap;
    }

    @Override // com.android.server.wm.WindowContainer
    void onDescendantOverrideConfigurationChanged() {
        setLayoutNeeded();
        this.mWmService.requestTraversal();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean okToDisplay() {
        return okToDisplay(false, false);
    }

    boolean okToDisplay(boolean z, boolean z2) {
        if (this.mDisplayId != 0) {
            return this.mDisplayInfo.state == 2;
        }
        WindowManagerService windowManagerService = this.mWmService;
        return (!windowManagerService.mDisplayFrozen || z) && windowManagerService.mDisplayEnabled && (z2 || windowManagerService.mPolicy.isScreenOn());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean okToAnimate(boolean z, boolean z2) {
        return okToDisplay(z, z2) && (this.mDisplayId != 0 || this.mWmService.mPolicy.okToAnimate(z2)) && (z || this.mDisplayPolicy.isScreenOnFully());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class TaskForResizePointSearchResult implements Predicate<Task> {
        private int delta;
        private Rect mTmpRect = new Rect();
        private Task taskForResize;
        private int x;
        private int y;

        TaskForResizePointSearchResult() {
        }

        Task process(WindowContainer windowContainer, int i, int i2, int i3) {
            this.taskForResize = null;
            this.x = i;
            this.y = i2;
            this.delta = i3;
            this.mTmpRect.setEmpty();
            windowContainer.forAllTasks(this);
            return this.taskForResize;
        }

        @Override // java.util.function.Predicate
        public boolean test(Task task) {
            if (!task.getRootTask().getWindowConfiguration().canResizeTask() || task.getWindowingMode() == 1 || task.isOrganized()) {
                return true;
            }
            task.getDimBounds(this.mTmpRect);
            Rect rect = this.mTmpRect;
            int i = this.delta;
            rect.inset(-i, -i);
            if (!this.mTmpRect.contains(this.x, this.y)) {
                return false;
            }
            Rect rect2 = this.mTmpRect;
            int i2 = this.delta;
            rect2.inset(i2, i2);
            if (!this.mTmpRect.contains(this.x, this.y)) {
                this.taskForResize = task;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class ApplySurfaceChangesTransactionState {
        public boolean disableHdrConversion;
        public boolean displayHasContent;
        public boolean obscured;
        public boolean preferMinimalPostProcessing;
        public float preferredMaxRefreshRate;
        public float preferredMinRefreshRate;
        public int preferredModeId;
        public float preferredRefreshRate;
        public boolean syswin;

        private ApplySurfaceChangesTransactionState() {
        }

        void reset() {
            this.displayHasContent = false;
            this.obscured = false;
            this.syswin = false;
            this.preferMinimalPostProcessing = false;
            this.preferredRefreshRate = DisplayContent.INVALID_DPI;
            this.preferredModeId = 0;
            this.preferredMinRefreshRate = DisplayContent.INVALID_DPI;
            this.preferredMaxRefreshRate = DisplayContent.INVALID_DPI;
            this.disableHdrConversion = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class ImeContainer extends DisplayArea.Tokens {
        boolean mNeedsLayer;

        ImeContainer(WindowManagerService windowManagerService) {
            super(windowManagerService, DisplayArea.Type.ABOVE_TASKS, "ImeContainer", 8);
            this.mNeedsLayer = false;
        }

        public void setNeedsLayer() {
            this.mNeedsLayer = true;
        }

        @Override // com.android.server.wm.DisplayArea.Tokens, com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
        int getOrientation(int i) {
            if (shouldIgnoreOrientationRequest(i)) {
                return -2;
            }
            return i;
        }

        @Override // com.android.server.wm.WindowContainer
        void updateAboveInsetsState(InsetsState insetsState, SparseArray<InsetsSource> sparseArray, ArraySet<WindowState> arraySet) {
            if (skipImeWindowsDuringTraversal(this.mDisplayContent)) {
                return;
            }
            super.updateAboveInsetsState(insetsState, sparseArray, arraySet);
        }

        @Override // com.android.server.wm.WindowContainer
        boolean forAllWindows(ToBooleanFunction<WindowState> toBooleanFunction, boolean z) {
            if (skipImeWindowsDuringTraversal(this.mDisplayContent)) {
                return false;
            }
            return super.forAllWindows(toBooleanFunction, z);
        }

        private static boolean skipImeWindowsDuringTraversal(DisplayContent displayContent) {
            return (displayContent.mImeLayeringTarget == null || displayContent.mWmService.mDisplayFrozen) ? false : true;
        }

        boolean forAllWindowForce(ToBooleanFunction<WindowState> toBooleanFunction, boolean z) {
            return super.forAllWindows(toBooleanFunction, z);
        }

        @Override // com.android.server.wm.WindowContainer
        void assignLayer(SurfaceControl.Transaction transaction, int i) {
            if (this.mNeedsLayer) {
                super.assignLayer(transaction, i);
                this.mNeedsLayer = false;
            }
        }

        @Override // com.android.server.wm.WindowContainer
        void assignRelativeLayer(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, int i, boolean z) {
            if (this.mNeedsLayer) {
                super.assignRelativeLayer(transaction, surfaceControl, i, z);
                this.mNeedsLayer = false;
            }
        }

        @Override // com.android.server.wm.DisplayArea
        void setOrganizer(IDisplayAreaOrganizer iDisplayAreaOrganizer, boolean z) {
            super.setOrganizer(iDisplayAreaOrganizer, z);
            this.mDisplayContent.updateImeParent();
            if (iDisplayAreaOrganizer != null) {
                SurfaceControl parentSurfaceControl = getParentSurfaceControl();
                SurfaceControl surfaceControl = this.mSurfaceControl;
                if (surfaceControl != null && parentSurfaceControl != null) {
                    if (ProtoLogCache.WM_DEBUG_IME_enabled) {
                        ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_IME, 1175495463, 0, (String) null, new Object[]{String.valueOf(parentSurfaceControl)});
                    }
                    getPendingTransaction().reparent(this.mSurfaceControl, parentSurfaceControl);
                    return;
                }
                if (ProtoLogCache.WM_DEBUG_IME_enabled) {
                    ProtoLogImpl.e(ProtoLogGroup.WM_DEBUG_IME, -81121442, 0, (String) null, new Object[]{String.valueOf(surfaceControl), String.valueOf(parentSurfaceControl)});
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public SurfaceSession getSession() {
        return this.mSession;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public SurfaceControl.Builder makeChildSurface(WindowContainer windowContainer) {
        SurfaceControl.Builder containerLayer = this.mWmService.makeSurfaceBuilder(windowContainer != null ? windowContainer.getSession() : getSession()).setContainerLayer();
        return windowContainer == null ? containerLayer : containerLayer.setName(windowContainer.getName()).setParent(this.mSurfaceControl);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl.Builder makeOverlay() {
        return this.mWmService.makeSurfaceBuilder(this.mSession).setParent(getOverlayLayer());
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public SurfaceControl.Builder makeAnimationLeash() {
        return this.mWmService.makeSurfaceBuilder(this.mSession).setParent(this.mSurfaceControl).setContainerLayer();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reparentToOverlay(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        transaction.reparent(surfaceControl, getOverlayLayer());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyMagnificationSpec(MagnificationSpec magnificationSpec) {
        if (magnificationSpec.scale != 1.0d) {
            this.mMagnificationSpec = magnificationSpec;
        } else {
            this.mMagnificationSpec = null;
        }
        updateImeParent();
        if (magnificationSpec.scale != 1.0d) {
            applyMagnificationSpec(getPendingTransaction(), magnificationSpec);
        } else {
            clearMagnificationSpec(getPendingTransaction());
        }
        getPendingTransaction().apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reapplyMagnificationSpec() {
        if (this.mMagnificationSpec != null) {
            applyMagnificationSpec(getPendingTransaction(), this.mMagnificationSpec);
        }
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    void onParentChanged(ConfigurationContainer configurationContainer, ConfigurationContainer configurationContainer2) {
        if (isReady()) {
            return;
        }
        this.mDisplayReady = true;
        DisplayManagerInternal displayManagerInternal = this.mWmService.mDisplayManagerInternal;
        if (displayManagerInternal != null) {
            displayManagerInternal.setDisplayInfoOverrideFromWindowManager(this.mDisplayId, getDisplayInfo());
            configureDisplayPolicy();
        }
        reconfigureDisplayLocked();
        onRequestedOverrideConfigurationChanged(getRequestedOverrideConfiguration());
        this.mWmService.mDisplayNotificationController.dispatchDisplayAdded(this);
        this.mWmService.mWindowContextListenerController.registerWindowContainerListener(getDisplayUiContext().getWindowContextToken(), this, 1000, -1, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    public void assignChildLayers(SurfaceControl.Transaction transaction) {
        assignRelativeLayerForIme(transaction, false);
        super.assignChildLayers(transaction);
    }

    private void assignRelativeLayerForIme(SurfaceControl.Transaction transaction, boolean z) {
        ActivityRecord activityRecord;
        ActivityRecord activityRecord2;
        if (this.mImeWindowsContainer.isOrganized()) {
            if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                Slog.i(TAG, "ImeContainer is organized. Skip assignRelativeLayerForIme.");
                return;
            }
            return;
        }
        this.mImeWindowsContainer.setNeedsLayer();
        WindowState windowState = this.mImeLayeringTarget;
        boolean imeTargetIsMainWindow = this.mDisplayContentExt.imeTargetIsMainWindow(windowState);
        if (windowState != null && ((activityRecord = windowState.mActivityRecord) == null || !activityRecord.hasStartingWindow() || !imeTargetIsMainWindow)) {
            InsetsControlTarget insetsControlTarget = this.mImeControlTarget;
            if ((windowState.getSurfaceControl() == null || windowState.mToken != ((insetsControlTarget == null || insetsControlTarget.getWindow() == null) ? null : this.mImeControlTarget.getWindow().mToken) || windowState.inMultiWindowMode() || this.mDisplayContentExt.isZoomWindowMode(windowState) || ((activityRecord2 = windowState.mActivityRecord) != null && activityRecord2.getTask().getWrapper().getExtImpl().isTaskEmbedded()) || this.mDisplayContentExt.isFlexibleTaskAndAdjustIme(windowState)) ? false : true) {
                this.mImeWindowsContainer.assignRelativeLayer(transaction, windowState.getSurfaceControl(), 1, z);
                return;
            }
        }
        SurfaceControl surfaceControl = this.mInputMethodSurfaceParent;
        if (surfaceControl != null) {
            this.mImeWindowsContainer.assignRelativeLayer(transaction, surfaceControl, 1, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void assignRelativeLayerForImeTargetChild(SurfaceControl.Transaction transaction, WindowContainer windowContainer) {
        windowContainer.assignRelativeLayer(transaction, this.mImeWindowsContainer.getSurfaceControl(), 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.DisplayArea.Dimmable, com.android.server.wm.WindowContainer
    public void prepareSurfaces() {
        Trace.traceBegin(32L, "prepareSurfaces");
        try {
            SurfaceControl.Transaction pendingTransaction = getPendingTransaction();
            this.mDisplayContentExt.hookPrepareSurfaces(this, pendingTransaction);
            super.prepareSurfaces();
            SurfaceControl.mergeToGlobalTransaction(pendingTransaction);
        } finally {
            Trace.traceEnd(32L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deferUpdateImeTarget() {
        int i = this.mDeferUpdateImeTargetCount;
        if (i == 0) {
            this.mUpdateImeRequestedWhileDeferred = false;
        }
        this.mDeferUpdateImeTargetCount = i + 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void continueUpdateImeTarget() {
        int i = this.mDeferUpdateImeTargetCount;
        if (i == 0) {
            return;
        }
        int i2 = i - 1;
        this.mDeferUpdateImeTargetCount = i2;
        if (i2 == 0 && this.mUpdateImeRequestedWhileDeferred) {
            computeImeTarget(true);
        }
    }

    private boolean canUpdateImeTarget() {
        return this.mDeferUpdateImeTargetCount == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputMonitor getInputMonitor() {
        return this.mInputMonitor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getLastHasContent() {
        return this.mLastHasContent;
    }

    @VisibleForTesting
    void setLastHasContent() {
        this.mLastHasContent = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerPointerEventListener(WindowManagerPolicyConstants.PointerEventListener pointerEventListener) {
        this.mPointerEventDispatcher.registerInputEventListener(pointerEventListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterPointerEventListener(WindowManagerPolicyConstants.PointerEventListener pointerEventListener) {
        this.mPointerEventDispatcher.unregisterInputEventListener(pointerEventListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void transferAppTransitionFrom(DisplayContent displayContent) {
        if (this.mAppTransition.transferFrom(displayContent.mAppTransition) && okToAnimate()) {
            this.mSkipAppTransitionAnimation = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public void prepareAppTransition(int i) {
        prepareAppTransition(i, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public void prepareAppTransition(int i, int i2) {
        if (!this.mAppTransition.prepareAppTransition(i, i2) || !okToAnimate() || i == 0 || this.mDisplayContentExt.skipAppTransitionAnimation()) {
            return;
        }
        this.mDisplayContent.mSkipAppTransitionAnimation = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void requestTransitionAndLegacyPrepare(int i, int i2) {
        requestTransitionAndLegacyPrepare(i, i2, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void requestTransitionAndLegacyPrepare(int i, int i2, WindowContainer windowContainer) {
        prepareAppTransition(i, i2);
        this.mTransitionController.requestTransitionIfNeeded(i, i2, windowContainer, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void executeAppTransition() {
        if (this.mAtmService.getWrapper().getFlexibleExtImpl().isEmbbeddingTaskAnimating()) {
            return;
        }
        this.mTransitionController.setReady(this);
        if (this.mAppTransition.isTransitionSet()) {
            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 1166381079, 4, (String) null, new Object[]{String.valueOf(this.mAppTransition), Long.valueOf(this.mDisplayId), String.valueOf(Debug.getCallers(5))});
            }
            this.mAppTransition.setReady();
            this.mWmService.mWindowPlacerLocked.requestTraversal();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleAnimatingStoppedAndTransition() {
        this.mAppTransition.setIdle();
        for (int size = this.mNoAnimationNotifyOnTransitionFinished.size() - 1; size >= 0; size--) {
            this.mAppTransition.notifyAppTransitionFinishedLocked(this.mNoAnimationNotifyOnTransitionFinished.get(size));
        }
        this.mNoAnimationNotifyOnTransitionFinished.clear();
        this.mWallpaperController.hideDeferredWallpapersIfNeededLegacy();
        onAppTransitionDone();
        if (ProtoLogCache.WM_DEBUG_WALLPAPER_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WALLPAPER, -182877285, 0, (String) null, (Object[]) null);
        }
        computeImeTarget(true);
        this.mWallpaperMayChange = true;
        this.mWmService.mFocusMayChange = true;
        this.pendingLayoutChanges |= 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isNextTransitionForward() {
        if (!this.mTransitionController.isShellTransitionsEnabled()) {
            return this.mAppTransition.containsTransitRequest(1) || this.mAppTransition.containsTransitRequest(3);
        }
        int collectingTransitionType = this.mTransitionController.getCollectingTransitionType();
        return collectingTransitionType == 1 || collectingTransitionType == 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean supportsSystemDecorations() {
        return (this.mWmService.mDisplayWindowSettings.shouldShowSystemDecorsLocked(this) || (this.mDisplay.getFlags() & 64) != 0 || forceDesktopMode() || this.mDisplayContentExt.supportDesktopModeOnExternalDisplays(this)) && this.mDisplayId != this.mWmService.mVr2dDisplayId && isTrusted();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl getWindowingLayer() {
        return this.mWindowingLayer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayArea.Tokens getImeContainer() {
        return this.mImeWindowsContainer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl getOverlayLayer() {
        return this.mOverlayLayer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl getInputOverlayLayer() {
        return this.mInputOverlayLayer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl getA11yOverlayLayer() {
        return this.mA11yOverlayLayer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl[] findRoundedCornerOverlays() {
        ArrayList arrayList = new ArrayList();
        for (WindowToken windowToken : this.mTokenMap.values()) {
            if (windowToken.mRoundedCornerOverlay && windowToken.isVisible()) {
                arrayList.add(windowToken.mSurfaceControl);
            }
        }
        return (SurfaceControl[]) arrayList.toArray(new SurfaceControl[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateSystemGestureExclusion() {
        if (this.mSystemGestureExclusionListeners.getRegisteredCallbackCount() == 0) {
            return false;
        }
        Region obtain = Region.obtain();
        this.mSystemGestureExclusionWasRestricted = calculateSystemGestureExclusion(obtain, this.mSystemGestureExclusionUnrestricted);
        try {
            if (this.mSystemGestureExclusion.equals(obtain)) {
                return false;
            }
            this.mSystemGestureExclusion.set(obtain);
            Region region = this.mSystemGestureExclusionWasRestricted ? this.mSystemGestureExclusionUnrestricted : null;
            for (int beginBroadcast = this.mSystemGestureExclusionListeners.beginBroadcast() - 1; beginBroadcast >= 0; beginBroadcast--) {
                try {
                    this.mSystemGestureExclusionListeners.getBroadcastItem(beginBroadcast).onSystemGestureExclusionChanged(this.mDisplayId, obtain, region);
                } catch (RemoteException e) {
                    Slog.e(TAG, "Failed to notify SystemGestureExclusionListener", e);
                }
            }
            this.mSystemGestureExclusionListeners.finishBroadcast();
            return true;
        } finally {
            obtain.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean calculateSystemGestureExclusion(final Region region, final Region region2) {
        region.setEmpty();
        if (region2 != null) {
            region2.setEmpty();
        }
        final Region obtain = Region.obtain();
        DisplayFrames displayFrames = this.mDisplayFrames;
        obtain.set(0, 0, displayFrames.mWidth, displayFrames.mHeight);
        InsetsState rawInsetsState = this.mInsetsStateController.getRawInsetsState();
        Rect displayFrame = rawInsetsState.getDisplayFrame();
        Insets calculateInsets = rawInsetsState.calculateInsets(displayFrame, WindowInsets.Type.systemGestures(), false);
        Rect rect = this.mSystemGestureFrameLeft;
        int i = displayFrame.left;
        rect.set(i, displayFrame.top, calculateInsets.left + i, displayFrame.bottom);
        Rect rect2 = this.mSystemGestureFrameRight;
        int i2 = displayFrame.right;
        rect2.set(i2 - calculateInsets.right, displayFrame.top, i2, displayFrame.bottom);
        final Region obtain2 = Region.obtain();
        final Region obtain3 = Region.obtain();
        int i3 = this.mSystemGestureExclusionLimit;
        final int[] iArr = {i3, i3};
        final RecentsAnimationController recentsAnimationController = this.mWmService.getRecentsAnimationController();
        forAllWindows(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda32
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.this.lambda$calculateSystemGestureExclusion$37(recentsAnimationController, obtain, obtain2, obtain3, iArr, region, region2, (WindowState) obj);
            }
        }, true);
        obtain3.recycle();
        obtain2.recycle();
        obtain.recycle();
        int i4 = iArr[0];
        int i5 = this.mSystemGestureExclusionLimit;
        return i4 < i5 || iArr[1] < i5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$calculateSystemGestureExclusion$37(RecentsAnimationController recentsAnimationController, Region region, Region region2, Region region3, int[] iArr, Region region4, Region region5, WindowState windowState) {
        boolean z = recentsAnimationController != null && recentsAnimationController.shouldApplyInputConsumer(windowState.getActivityRecord());
        if (!windowState.canReceiveTouchInput() || !windowState.isVisible() || (windowState.getAttrs().flags & 16) != 0 || region.isEmpty() || z || windowState.getWrapper().getExtImpl().checkIfWindowingModeZoom(windowState.getWindowingMode())) {
            return;
        }
        windowState.getEffectiveTouchableRegion(region2);
        region2.op(region, Region.Op.INTERSECT);
        if (windowState.isImplicitlyExcludingAllSystemGestures()) {
            region3.set(region2);
        } else {
            RegionUtils.rectListToRegion(windowState.getSystemGestureExclusion(), region3);
            region3.scale(windowState.mGlobalScale);
            Rect rect = windowState.getWindowFrames().mFrame;
            region3.translate(rect.left, rect.top);
            region3.op(region2, Region.Op.INTERSECT);
        }
        if (needsGestureExclusionRestrictions(windowState, false)) {
            iArr[0] = addToGlobalAndConsumeLimit(region3, region4, this.mSystemGestureFrameLeft, iArr[0], windowState, 0);
            iArr[1] = addToGlobalAndConsumeLimit(region3, region4, this.mSystemGestureFrameRight, iArr[1], windowState, 1);
            Region obtain = Region.obtain(region3);
            obtain.op(this.mSystemGestureFrameLeft, Region.Op.DIFFERENCE);
            obtain.op(this.mSystemGestureFrameRight, Region.Op.DIFFERENCE);
            region4.op(obtain, Region.Op.UNION);
            obtain.recycle();
        } else {
            if (needsGestureExclusionRestrictions(windowState, true)) {
                addToGlobalAndConsumeLimit(region3, region4, this.mSystemGestureFrameLeft, Integer.MAX_VALUE, windowState, 0);
                addToGlobalAndConsumeLimit(region3, region4, this.mSystemGestureFrameRight, Integer.MAX_VALUE, windowState, 1);
            }
            region4.op(region3, Region.Op.UNION);
        }
        if (region5 != null) {
            region5.op(region3, Region.Op.UNION);
        }
        region.op(region2, Region.Op.DIFFERENCE);
    }

    private static boolean needsGestureExclusionRestrictions(WindowState windowState, boolean z) {
        WindowManager.LayoutParams layoutParams = windowState.mAttrs;
        int i = layoutParams.type;
        return (!(!windowState.isRequestedVisible(WindowInsets.Type.navigationBars()) && windowState.mAttrs.insetsFlags.behavior == 2) || z) && i != 2011 && i != 2040 && windowState.getActivityType() != 2 && (layoutParams.privateFlags & 32) == 0 && mStaticDisplayContentExt.isWinHasGestureExclusionRestrictions(windowState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean logsGestureExclusionRestrictions(WindowState windowState) {
        WindowManager.LayoutParams attrs;
        int i;
        return windowState.mWmService.mConstants.mSystemGestureExclusionLogDebounceTimeoutMillis > 0 && (i = (attrs = windowState.getAttrs()).type) != 2013 && i != 3 && i != 2019 && (attrs.flags & 16) == 0 && needsGestureExclusionRestrictions(windowState, true) && windowState.getDisplayContent().mDisplayPolicy.hasSideGestures();
    }

    private static int addToGlobalAndConsumeLimit(Region region, final Region region2, Rect rect, int i, WindowState windowState, int i2) {
        Region obtain = Region.obtain(region);
        obtain.op(rect, Region.Op.INTERSECT);
        final int[] iArr = {i};
        final int[] iArr2 = {0};
        RegionUtils.forEachRectReverse(obtain, new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda41
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.lambda$addToGlobalAndConsumeLimit$38(iArr, iArr2, region2, (Rect) obj);
            }
        });
        windowState.setLastExclusionHeights(i2, iArr2[0], i - iArr[0]);
        obtain.recycle();
        return iArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$addToGlobalAndConsumeLimit$38(int[] iArr, int[] iArr2, Region region, Rect rect) {
        if (iArr[0] <= 0) {
            return;
        }
        int height = rect.height();
        iArr2[0] = iArr2[0] + height;
        int i = iArr[0];
        if (height > i) {
            rect.top = rect.bottom - i;
        }
        iArr[0] = i - height;
        region.op(rect, Region.Op.UNION);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerSystemGestureExclusionListener(ISystemGestureExclusionListener iSystemGestureExclusionListener) {
        this.mSystemGestureExclusionListeners.register(iSystemGestureExclusionListener);
        if (this.mSystemGestureExclusionListeners.getRegisteredCallbackCount() == 1 ? updateSystemGestureExclusion() : false) {
            return;
        }
        try {
            iSystemGestureExclusionListener.onSystemGestureExclusionChanged(this.mDisplayId, this.mSystemGestureExclusion, this.mSystemGestureExclusionWasRestricted ? this.mSystemGestureExclusionUnrestricted : null);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to notify SystemGestureExclusionListener during register", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterSystemGestureExclusionListener(ISystemGestureExclusionListener iSystemGestureExclusionListener) {
        this.mSystemGestureExclusionListeners.unregister(iSystemGestureExclusionListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateKeepClearAreas() {
        ArraySet arraySet = new ArraySet();
        ArraySet arraySet2 = new ArraySet();
        getKeepClearAreas(arraySet, arraySet2);
        if (this.mRestrictedKeepClearAreas.equals(arraySet) && this.mUnrestrictedKeepClearAreas.equals(arraySet2)) {
            return;
        }
        this.mRestrictedKeepClearAreas = arraySet;
        this.mUnrestrictedKeepClearAreas = arraySet2;
        this.mWmService.mDisplayNotificationController.dispatchKeepClearAreasChanged(this, arraySet, arraySet2);
    }

    void getKeepClearAreas(final Set<Rect> set, final Set<Rect> set2) {
        final Matrix matrix = new Matrix();
        final float[] fArr = new float[9];
        final RecentsAnimationController recentsAnimationController = this.mWmService.getRecentsAnimationController();
        forAllWindows(new ToBooleanFunction() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda38
            public final boolean apply(Object obj) {
                boolean lambda$getKeepClearAreas$40;
                lambda$getKeepClearAreas$40 = DisplayContent.lambda$getKeepClearAreas$40(RecentsAnimationController.this, set, set2, matrix, fArr, (WindowState) obj);
                return lambda$getKeepClearAreas$40;
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getKeepClearAreas$40(RecentsAnimationController recentsAnimationController, Set set, final Set set2, Matrix matrix, float[] fArr, WindowState windowState) {
        if (recentsAnimationController != null && recentsAnimationController.shouldApplyInputConsumer(windowState.getActivityRecord())) {
            return false;
        }
        if (windowState.isVisible() && !windowState.inPinnedWindowingMode()) {
            windowState.getKeepClearAreas(set, set2, matrix, fArr);
            if (windowState.mIsImWindow) {
                Region obtain = Region.obtain();
                windowState.getEffectiveTouchableRegion(obtain);
                RegionUtils.forEachRect(obtain, new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda55
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        set2.add((Rect) obj);
                    }
                });
            }
        }
        return windowState.getWindowType() == 1 && windowState.getWindowingMode() == 1;
    }

    Set<Rect> getKeepClearAreas() {
        ArraySet arraySet = new ArraySet();
        getKeepClearAreas(arraySet, arraySet);
        return arraySet;
    }

    protected MetricsLogger getMetricsLogger() {
        if (this.mMetricsLogger == null) {
            this.mMetricsLogger = new MetricsLogger();
        }
        return this.mMetricsLogger;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDisplayChanged() {
        int i = this.mDisplayInfo.state;
        updateDisplayInfo();
        int displayId = this.mDisplay.getDisplayId();
        int i2 = this.mDisplayInfo.state;
        if (displayId != 0) {
            if (this.mDisplayContentExt.isSecondDisplay(this)) {
                if (i2 == 2) {
                    this.mOffTokenAcquirer.release(this.mDisplayId);
                } else {
                    this.mOffTokenAcquirer.acquire(this.mDisplayId);
                }
            } else if (i2 == 1) {
                this.mOffTokenAcquirer.acquire(this.mDisplayId);
            } else if (i2 == 2) {
                this.mOffTokenAcquirer.release(this.mDisplayId);
            }
            if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -1883484959, 5, "Content Recording: Display %d state is now (%d), so update recording?", new Object[]{Long.valueOf(this.mDisplayId), Long.valueOf(i2)});
            }
            if (i != i2) {
                updateRecording();
            }
        }
        this.mWallpaperController.resetLargestDisplay(this.mDisplay);
        if (Display.isSuspendedState(i) && !Display.isSuspendedState(i2) && i2 != 0) {
            this.mWmService.mWindowContextListenerController.dispatchPendingConfigurationIfNeeded(this.mDisplayId);
        }
        this.mDisplayContentExt.triggerIntoComapct();
        IDisplayContentExt iDisplayContentExt = this.mDisplayContentExt;
        DisplayInfo displayInfo = this.mDisplayInfo;
        iDisplayContentExt.physicalDisplayChangedForFlexibleWindow(displayId, displayInfo.logicalWidth, displayInfo.logicalHeight);
        if (i2 == 2) {
            this.mDisplayContentExt.displayChangeToOn();
        }
        this.mWmService.requestTraversal();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getFocusedRootTask() {
        return (Task) getItemFromTaskDisplayAreas(new Function() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda56
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((TaskDisplayArea) obj).getFocusedRootTask();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeRootTasksInWindowingModes(final int... iArr) {
        if (iArr == null || iArr.length == 0) {
            return;
        }
        final ArrayList arrayList = new ArrayList();
        forAllRootTasks(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda57
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.lambda$removeRootTasksInWindowingModes$41(iArr, arrayList, (Task) obj);
            }
        });
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            this.mRootWindowContainer.mTaskSupervisor.removeRootTask((Task) arrayList.get(size));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$removeRootTasksInWindowingModes$41(int[] iArr, ArrayList arrayList, Task task) {
        for (int i : iArr) {
            if (!task.mCreatedByOrganizer && task.getWindowingMode() == i && task.isActivityTypeStandardOrUndefined()) {
                arrayList.add(task);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeRootTasksWithActivityTypes(final int... iArr) {
        if (iArr == null || iArr.length == 0) {
            return;
        }
        final ArrayList arrayList = new ArrayList();
        forAllRootTasks(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda34
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.lambda$removeRootTasksWithActivityTypes$42(iArr, arrayList, (Task) obj);
            }
        });
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            this.mRootWindowContainer.mTaskSupervisor.removeRootTask((Task) arrayList.get(size));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$removeRootTasksWithActivityTypes$42(int[] iArr, ArrayList arrayList, Task task) {
        for (int i : iArr) {
            if (task.mCreatedByOrganizer) {
                for (int childCount = task.getChildCount() - 1; childCount >= 0; childCount--) {
                    Task task2 = (Task) task.getChildAt(childCount);
                    if (task2.getActivityType() == i) {
                        arrayList.add(task2);
                    }
                }
            } else if (task.getActivityType() == i) {
                arrayList.add(task);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord topRunningActivity() {
        return topRunningActivity(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityRecord topRunningActivity(final boolean z) {
        return (ActivityRecord) getItemFromTaskDisplayAreas(new Function() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda58
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                ActivityRecord lambda$topRunningActivity$43;
                lambda$topRunningActivity$43 = DisplayContent.lambda$topRunningActivity$43(z, (TaskDisplayArea) obj);
                return lambda$topRunningActivity$43;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ActivityRecord lambda$topRunningActivity$43(boolean z, TaskDisplayArea taskDisplayArea) {
        return taskDisplayArea.topRunningActivity(z);
    }

    boolean updateDisplayOverrideConfigurationLocked() {
        RecentsAnimationController recentsAnimationController = this.mWmService.getRecentsAnimationController();
        if (recentsAnimationController != null && this.mDisplayContentExt.shouldCancelRecentAnimation(this)) {
            recentsAnimationController.cancelAnimationForDisplayChange();
        }
        Configuration configuration = new Configuration();
        computeScreenConfiguration(configuration);
        this.mAtmService.mH.sendMessage(PooledLambda.obtainMessage(new ActivityTaskManagerService$$ExternalSyntheticLambda14(), this.mAtmService.mAmInternal, Integer.valueOf(this.mDisplayId)));
        Settings.System.clearConfiguration(configuration);
        updateDisplayOverrideConfigurationLocked(configuration, null, false, this.mAtmService.mTmpUpdateConfigurationResult);
        return this.mAtmService.mTmpUpdateConfigurationResult.changes != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateDisplayOverrideConfigurationLocked(Configuration configuration, ActivityRecord activityRecord, boolean z, ActivityTaskManagerService.UpdateConfigurationResult updateConfigurationResult) {
        this.mAtmService.deferWindowLayout();
        int i = 0;
        if (configuration != null) {
            try {
                if (this.mDisplayId == 0) {
                    i = this.mAtmService.updateGlobalConfigurationLocked(configuration, false, false, -10000);
                } else {
                    i = performDisplayOverrideConfigUpdate(configuration);
                }
            } catch (Throwable th) {
                this.mAtmService.continueWindowLayout();
                throw th;
            }
        }
        boolean ensureConfigAndVisibilityAfterUpdate = !z ? this.mAtmService.ensureConfigAndVisibilityAfterUpdate(activityRecord, i) : true;
        this.mAtmService.continueWindowLayout();
        if (updateConfigurationResult != null) {
            updateConfigurationResult.changes = i;
            updateConfigurationResult.activityRelaunched = !ensureConfigAndVisibilityAfterUpdate;
        }
        return ensureConfigAndVisibilityAfterUpdate;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int performDisplayOverrideConfigUpdate(Configuration configuration) {
        this.mTempConfig.setTo(getRequestedOverrideConfiguration());
        int updateFrom = this.mTempConfig.updateFrom(configuration);
        if (updateFrom != 0) {
            Slog.i(TAG, "Override config changes=" + Integer.toHexString(updateFrom) + " " + this.mTempConfig + " for displayId=" + this.mDisplayId);
            if (isReady() && this.mTransitionController.isShellTransitionsEnabled()) {
                requestChangeTransitionIfNeeded(updateFrom, null);
            }
            onRequestedOverrideConfigurationChanged(this.mTempConfig);
            if (((updateFrom & 4096) != 0) && this.mDisplayId == 0) {
                this.mAtmService.mAppWarnings.onDensityChanged();
                this.mAtmService.mH.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda33
                    public final void accept(Object obj, Object obj2, Object obj3) {
                        ((ActivityManagerInternal) obj).killAllBackgroundProcessesExcept(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
                    }
                }, this.mAtmService.mAmInternal, 24, 6));
            }
            this.mWmService.mDisplayNotificationController.dispatchDisplayChanged(this, getConfiguration());
        }
        return updateFrom;
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onRequestedOverrideConfigurationChanged(Configuration configuration) {
        Configuration requestedOverrideConfiguration = getRequestedOverrideConfiguration();
        int rotation = requestedOverrideConfiguration.windowConfiguration.getRotation();
        int rotation2 = configuration.windowConfiguration.getRotation();
        if (rotation != -1 && rotation2 != -1 && rotation != rotation2) {
            applyRotationAndFinishFixedRotation(rotation, rotation2);
        }
        this.mCurrentOverrideConfigurationChanges = requestedOverrideConfiguration.diff(configuration);
        this.mNonStaticExt.updateRequestedOverrideConfiguration(configuration);
        super.onRequestedOverrideConfigurationChanged(configuration);
        this.mCurrentOverrideConfigurationChanges = 0;
        if (this.mWaitingForConfig) {
            this.mWaitingForConfig = false;
            this.mWmService.mLastFinishedFreezeSource = "new-config";
        }
        this.mAtmService.addWindowLayoutReasons(1);
    }

    @Override // com.android.server.wm.WindowContainer
    void onResize() {
        super.onResize();
        if (this.mWmService.mAccessibilityController.hasCallbacks()) {
            this.mWmService.mAccessibilityController.onDisplaySizeChanged(this);
        }
    }

    private void applyRotationAndFinishFixedRotation(final int i, final int i2) {
        ActivityRecord activityRecord = this.mFixedRotationLaunchingApp;
        if (activityRecord == null) {
            lambda$applyRotationAndFinishFixedRotation$44(i, i2);
        } else {
            activityRecord.finishFixedRotationTransform(new Runnable() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda44
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayContent.this.lambda$applyRotationAndFinishFixedRotation$44(i, i2);
                }
            });
            setFixedRotationLaunchingAppUnchecked(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleActivitySizeCompatModeIfNeeded(ActivityRecord activityRecord) {
        Task organizedTask = activityRecord.getOrganizedTask();
        if (organizedTask == null) {
            this.mActiveSizeCompatActivities.remove(activityRecord);
            return;
        }
        if (activityRecord.isState(ActivityRecord.State.RESUMED) && (activityRecord.inSizeCompatMode() || activityRecord.getWrapper().getExtImpl().inOplusCompatMode())) {
            if (this.mActiveSizeCompatActivities.add(activityRecord)) {
                organizedTask.onSizeCompatActivityChanged();
            }
        } else if (this.mActiveSizeCompatActivities.remove(activityRecord)) {
            organizedTask.onSizeCompatActivityChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUidPresent(int i) {
        Predicate<ActivityRecord> obtainPredicate = PooledLambda.obtainPredicate(new DisplayContent$$ExternalSyntheticLambda24(), PooledLambda.__(ActivityRecord.class), Integer.valueOf(i));
        boolean z = this.mDisplayContent.getActivity(obtainPredicate) != null;
        obtainPredicate.recycle();
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRemoved() {
        return this.mRemoved;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRemoving() {
        return this.mRemoving;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remove() {
        this.mRemoving = true;
        this.mRootWindowContainer.mTaskSupervisor.beginDeferResume();
        try {
            Task task = (Task) reduceOnAllTaskDisplayAreas(new BiFunction() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda19
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    Task lambda$remove$45;
                    lambda$remove$45 = DisplayContent.lambda$remove$45((TaskDisplayArea) obj, (Task) obj2);
                    return lambda$remove$45;
                }
            }, null, false);
            this.mRootWindowContainer.mTaskSupervisor.endDeferResume();
            this.mRemoved = true;
            ContentRecorder contentRecorder = this.mContentRecorder;
            if (contentRecorder != null) {
                contentRecorder.stopRecording();
            }
            if (task != null) {
                task.resumeNextFocusAfterReparent();
            }
            releaseSelfIfNeeded();
            this.mDisplayPolicy.release();
            if (this.mAllSleepTokens.isEmpty()) {
                return;
            }
            this.mAllSleepTokens.forEach(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda20
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DisplayContent.this.lambda$remove$46((RootWindowContainer.SleepToken) obj);
                }
            });
            this.mAllSleepTokens.clear();
            this.mAtmService.updateSleepIfNeededLocked();
        } catch (Throwable th) {
            this.mRootWindowContainer.mTaskSupervisor.endDeferResume();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Task lambda$remove$45(TaskDisplayArea taskDisplayArea, Task task) {
        Task remove = taskDisplayArea.remove();
        return remove != null ? remove : task;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$remove$46(RootWindowContainer.SleepToken sleepToken) {
        this.mRootWindowContainer.mSleepTokens.remove(sleepToken.mHashKey);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void releaseSelfIfNeeded() {
        if (this.mRemoved) {
            if (!forAllRootTasks(new Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda53
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$releaseSelfIfNeeded$47;
                    lambda$releaseSelfIfNeeded$47 = DisplayContent.lambda$releaseSelfIfNeeded$47((Task) obj);
                    return lambda$releaseSelfIfNeeded$47;
                }
            }) && getRootTaskCount() > 0) {
                forAllRootTasks(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda54
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((Task) obj).removeIfPossible("releaseSelfIfNeeded");
                    }
                });
            } else if (getTopRootTask() == null) {
                removeIfPossible();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$releaseSelfIfNeeded$47(Task task) {
        return !task.isActivityTypeHome() || task.hasChild();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IntArray getPresentUIDs() {
        this.mDisplayAccessUIDs.clear();
        this.mDisplayContent.forAllActivities(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda22
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.this.lambda$getPresentUIDs$49((ActivityRecord) obj);
            }
        });
        return this.mDisplayAccessUIDs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getPresentUIDs$49(ActivityRecord activityRecord) {
        this.mDisplayAccessUIDs.add(activityRecord.getUid());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean shouldDestroyContentOnRemove() {
        return this.mDisplay.getRemoveMode() == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldSleep() {
        return (getRootTaskCount() == 0 || !this.mAllSleepTokens.isEmpty()) && this.mAtmService.mRunningVoice == null && this.mDisplayContentExt.onGoingToSleep(getDisplayId());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void ensureActivitiesVisible(final ActivityRecord activityRecord, final int i, final boolean z, final boolean z2) {
        if (this.mInEnsureActivitiesVisible) {
            return;
        }
        this.mInEnsureActivitiesVisible = true;
        this.mAtmService.mTaskSupervisor.beginActivityVisibilityUpdate();
        try {
            forAllRootTasks(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda42
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((Task) obj).ensureActivitiesVisible(ActivityRecord.this, i, z, z2);
                }
            });
            if (this.mTransitionController.useShellTransitionsRotation() && this.mTransitionController.isCollecting() && this.mWallpaperController.getWallpaperTarget() != null) {
                this.mWallpaperController.adjustWallpaperWindows();
            }
        } finally {
            this.mAtmService.mTaskSupervisor.endActivityVisibilityUpdate();
            this.mInEnsureActivitiesVisible = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSleeping() {
        return this.mSleeping;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIsSleeping(boolean z) {
        Slog.d(TAG, " setIsSleeping from  " + this.mSleeping + " to " + z + " this=" + this);
        this.mSleeping = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyKeyguardFlagsChanged() {
        if (isKeyguardLocked()) {
            boolean isTransitionSet = this.mAppTransition.isTransitionSet();
            if (!isTransitionSet) {
                prepareAppTransition(0);
            }
            this.mRootWindowContainer.ensureActivitiesVisible(null, 0, false);
            if (isTransitionSet) {
                return;
            }
            executeAppTransition();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canShowWithInsecureKeyguard() {
        return (this.mDisplay.getFlags() & 32) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isKeyguardLocked() {
        return this.mRootWindowContainer.mTaskSupervisor.getKeyguardController().isKeyguardLocked(this.mDisplayId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isKeyguardGoingAway() {
        return this.mRootWindowContainer.mTaskSupervisor.getKeyguardController().isKeyguardGoingAway(this.mDisplayId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isKeyguardAlwaysUnlocked() {
        return (this.mDisplayInfo.flags & 512) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAodShowing() {
        return this.mRootWindowContainer.mTaskSupervisor.getKeyguardController().isAodShowing(this.mDisplayId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasOwnFocus() {
        return this.mWmService.mPerDisplayFocusEnabled || (this.mDisplayInfo.flags & 2048) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isKeyguardOccluded() {
        return this.mRootWindowContainer.mTaskSupervisor.getKeyguardController().isDisplayOccluded(this.mDisplayId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task getTaskOccludingKeyguard() {
        KeyguardController keyguardController = this.mRootWindowContainer.mTaskSupervisor.getKeyguardController();
        if (keyguardController.getTopOccludingActivity(this.mDisplayId) != null) {
            return keyguardController.getTopOccludingActivity(this.mDisplayId).getRootTask();
        }
        if (keyguardController.getDismissKeyguardActivity(this.mDisplayId) != null) {
            return keyguardController.getDismissKeyguardActivity(this.mDisplayId).getRootTask();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$removeAllTasks$51(Task task) {
        task.getRootTask().removeChild(task, "removeAllTasks");
    }

    @VisibleForTesting
    void removeAllTasks() {
        forAllTasks(new Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda39
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayContent.lambda$removeAllTasks$51((Task) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Context getDisplayUiContext() {
        return this.mDisplayPolicy.getSystemUiContext();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.DisplayArea
    public boolean setIgnoreOrientationRequest(boolean z) {
        if (this.mSetIgnoreOrientationRequest == z) {
            return false;
        }
        boolean ignoreOrientationRequest = super.setIgnoreOrientationRequest(z);
        this.mWmService.mDisplayWindowSettings.setIgnoreOrientationRequest(this, this.mSetIgnoreOrientationRequest);
        return ignoreOrientationRequest;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onIsIgnoreOrientationRequestDisabledChanged() {
        ActivityRecord activityRecord = this.mFocusedApp;
        if (activityRecord != null) {
            onLastFocusedTaskDisplayAreaChanged(activityRecord.getDisplayArea());
        }
        if (this.mSetIgnoreOrientationRequest) {
            updateOrientation();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState findScrollCaptureTargetWindow(WindowState windowState, int i) {
        return getWindow(new Predicate<WindowState>(windowState, i) { // from class: com.android.server.wm.DisplayContent.4
            boolean behindTopWindow;
            final /* synthetic */ WindowState val$searchBehind;
            final /* synthetic */ int val$taskId;

            {
                this.val$searchBehind = windowState;
                this.val$taskId = i;
                this.behindTopWindow = windowState == null;
            }

            @Override // java.util.function.Predicate
            public boolean test(WindowState windowState2) {
                if (!this.behindTopWindow) {
                    if (windowState2 == this.val$searchBehind) {
                        this.behindTopWindow = true;
                    }
                    return false;
                }
                if (this.val$taskId == -1) {
                    if (!windowState2.canReceiveKeys()) {
                        return false;
                    }
                } else {
                    Task task = windowState2.getTask();
                    if (task == null || !task.isTaskId(this.val$taskId)) {
                        return false;
                    }
                }
                return !windowState2.isSecureLocked();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSandboxDisplayApis(boolean z) {
        this.mSandboxDisplayApis = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean sandboxDisplayApis() {
        return this.mSandboxDisplayApis;
    }

    private ContentRecorder getContentRecorder() {
        if (this.mContentRecorder == null) {
            this.mContentRecorder = new ContentRecorder(this);
        }
        return this.mContentRecorder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void pauseRecording() {
        ContentRecorder contentRecorder = this.mContentRecorder;
        if (contentRecorder != null) {
            contentRecorder.pauseRecording();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setContentRecordingSession(ContentRecordingSession contentRecordingSession) {
        getContentRecorder().setContentRecordingSession(contentRecordingSession);
    }

    boolean setDisplayMirroring() {
        int displayIdToMirror = this.mWmService.mDisplayManagerInternal.getDisplayIdToMirror(this.mDisplayId);
        if (displayIdToMirror == -1) {
            return false;
        }
        int i = this.mDisplayId;
        if (displayIdToMirror == i) {
            if (i != 0 && ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 1145016093, 1, "Content Recording: Attempting to mirror self on %d", new Object[]{Long.valueOf(displayIdToMirror)});
            }
            return false;
        }
        DisplayContent displayContentOrCreate = this.mRootWindowContainer.getDisplayContentOrCreate(displayIdToMirror);
        if (displayContentOrCreate == null && this.mDisplayId == 0) {
            if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -2072029833, 1, "Content Recording: Found no matching mirror display for id=%d for DEFAULT_DISPLAY. Nothing to mirror.", new Object[]{Long.valueOf(displayIdToMirror)});
            }
            return false;
        }
        if (displayContentOrCreate == null) {
            displayContentOrCreate = this.mRootWindowContainer.getDefaultDisplay();
            if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 801521566, 5, "Content Recording: Attempting to mirror %d from %d but no DisplayContent associated. Changing to mirror default display.", new Object[]{Long.valueOf(displayIdToMirror), Long.valueOf(this.mDisplayId)});
            }
        }
        if (this.mDisplayContentExt.isFoldExternalScreen(this.mRootWindowContainer, this.mDisplayId)) {
            return false;
        }
        setContentRecordingSession(ContentRecordingSession.createDisplaySession(displayContentOrCreate.getDisplayId()).setVirtualDisplayId(this.mDisplayId));
        if (ProtoLogCache.WM_DEBUG_CONTENT_RECORDING_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -1885450608, 5, "Content Recording: Successfully created a ContentRecordingSession for displayId=%d to mirror content from displayId=%d", new Object[]{Long.valueOf(this.mDisplayId), Long.valueOf(displayIdToMirror)});
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateRecording() {
        ContentRecorder contentRecorder = this.mContentRecorder;
        if ((contentRecorder == null || !contentRecorder.isContentRecordingSessionSet()) && !setDisplayMirroring()) {
            return;
        }
        if (ActivityTaskManagerService.LTW_DISABLE || !this.mContentRecorder.mContentRecorderExt.updateMirroringIfSurfaceSizeChanged()) {
            this.mContentRecorder.updateRecording();
        }
    }

    boolean isCurrentlyRecording() {
        ContentRecorder contentRecorder = this.mContentRecorder;
        return contentRecorder != null && contentRecorder.isCurrentlyRecording();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class FixedRotationTransitionListener extends WindowManagerInternal.AppTransitionListener {
        private ActivityRecord mAnimatingRecents;
        private boolean mRecentsWillBeTop;

        FixedRotationTransitionListener() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void onStartRecentsAnimation(ActivityRecord activityRecord) {
            ActivityRecord activityRecord2;
            this.mAnimatingRecents = activityRecord;
            if (activityRecord.isVisible() && (activityRecord2 = DisplayContent.this.mFocusedApp) != null && !activityRecord2.occludesParent()) {
                Slog.d(DisplayContent.TAG, "onStartRecentsAnimation: focused app( " + DisplayContent.this.mFocusedApp + " ) not occludes parent.");
                return;
            }
            DisplayContent.this.rotateInDifferentOrientationIfNeeded(activityRecord);
            if (activityRecord.hasFixedRotationTransform()) {
                DisplayContent.this.setFixedRotationLaunchingApp(activityRecord, activityRecord.getWindowConfiguration().getRotation());
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void onFinishRecentsAnimation() {
            ActivityRecord activityRecord = this.mAnimatingRecents;
            boolean z = this.mRecentsWillBeTop;
            this.mAnimatingRecents = null;
            this.mRecentsWillBeTop = false;
            if (z) {
                return;
            }
            if (activityRecord != null && activityRecord == DisplayContent.this.mFixedRotationLaunchingApp && activityRecord.isVisible() && activityRecord != DisplayContent.this.topRunningActivity()) {
                DisplayContent.this.setFixedRotationLaunchingAppUnchecked(null);
            } else {
                DisplayContent.this.continueUpdateOrientationForDiffOrienLaunchingApp();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void notifyRecentsWillBeTop() {
            this.mRecentsWillBeTop = true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean shouldDeferRotation() {
            ActivityRecord activityRecord = null;
            if (DisplayContent.this.mTransitionController.isShellTransitionsEnabled()) {
                ActivityRecord activityRecord2 = DisplayContent.this.mFixedRotationLaunchingApp;
                if (activityRecord2 != null && DisplayContent.this.mTransitionController.isTransientLaunch(activityRecord2)) {
                    activityRecord = activityRecord2;
                }
            } else if (this.mAnimatingRecents != null && !DisplayContent.this.hasTopFixedRotationLaunchingApp()) {
                activityRecord = this.mAnimatingRecents;
            }
            if (activityRecord == null || activityRecord.getRequestedConfigurationOrientation(true) == 0) {
                return false;
            }
            return DisplayContent.this.mWmService.mPolicy.okToAnimate(false);
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionFinishedLocked(IBinder iBinder) {
            ActivityRecord activityRecord;
            ActivityRecord activityRecord2 = DisplayContent.this.getActivityRecord(iBinder);
            if (activityRecord2 == null || activityRecord2 == (activityRecord = this.mAnimatingRecents)) {
                return;
            }
            if (activityRecord == null || !this.mRecentsWillBeTop || (activityRecord2.getWindowingMode() == 100 && DisplayContent.this.mFixedRotationLaunchingApp != null && DisplayContent.this.mFixedRotationLaunchingApp.hasFixedRotationTransform(activityRecord2))) {
                if (DisplayContent.this.mFixedRotationLaunchingApp == null) {
                    activityRecord2.finishFixedRotationTransform();
                    return;
                }
                if (DisplayContent.this.mFixedRotationLaunchingApp.hasFixedRotationTransform(activityRecord2)) {
                    if (DisplayContent.this.mFixedRotationLaunchingApp.hasAnimatingFixedRotationTransition()) {
                        return;
                    }
                } else {
                    Task task = activityRecord2.getTask();
                    if (task == null || task != DisplayContent.this.mFixedRotationLaunchingApp.getTask() || task.getActivity(new Predicate() { // from class: com.android.server.wm.DisplayContent$FixedRotationTransitionListener$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            return ((ActivityRecord) obj).isInTransition();
                        }
                    }) != null) {
                        return;
                    }
                }
                DisplayContent.this.continueUpdateOrientationForDiffOrienLaunchingApp();
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionCancelledLocked(boolean z) {
            if (DisplayContent.this.mTransitionController.isShellTransitionsEnabled()) {
                return;
            }
            DisplayContent.this.continueUpdateOrientationForDiffOrienLaunchingApp();
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionTimeoutLocked() {
            DisplayContent.this.continueUpdateOrientationForDiffOrienLaunchingApp();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class RemoteInsetsControlTarget implements InsetsControlTarget {
        private final boolean mCanShowTransient;
        private final IDisplayWindowInsetsController mRemoteInsetsController;
        private int mRequestedVisibleTypes = WindowInsets.Type.defaultVisible();

        RemoteInsetsControlTarget(IDisplayWindowInsetsController iDisplayWindowInsetsController) {
            this.mRemoteInsetsController = iDisplayWindowInsetsController;
            this.mCanShowTransient = DisplayContent.this.mWmService.mContext.getResources().getBoolean(17891793);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void topFocusedWindowChanged(ComponentName componentName, int i) {
            try {
                this.mRemoteInsetsController.topFocusedWindowChanged(componentName, i);
            } catch (RemoteException e) {
                Slog.w(DisplayContent.TAG, "Failed to deliver package in top focused window change", e);
            }
        }

        void notifyInsetsChanged() {
            try {
                this.mRemoteInsetsController.insetsChanged(DisplayContent.this.getInsetsStateController().getRawInsetsState());
            } catch (RemoteException e) {
                Slog.w(DisplayContent.TAG, "Failed to deliver inset state change", e);
            }
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public void notifyInsetsControlChanged() {
            InsetsStateController insetsStateController = DisplayContent.this.getInsetsStateController();
            try {
                this.mRemoteInsetsController.insetsControlChanged(insetsStateController.getRawInsetsState(), insetsStateController.getControlsForDispatch(this));
            } catch (RemoteException e) {
                Slog.w(DisplayContent.TAG, "Failed to deliver inset control state change", e);
            }
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public void showInsets(int i, boolean z, ImeTracker.Token token) {
            try {
                ImeTracker.forLogging().onProgress(token, 23);
                this.mRemoteInsetsController.showInsets(i, z, token);
            } catch (RemoteException e) {
                Slog.w(DisplayContent.TAG, "Failed to deliver showInsets", e);
                ImeTracker.forLogging().onFailed(token, 23);
            }
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public void hideInsets(int i, boolean z, ImeTracker.Token token) {
            try {
                ImeTracker.forLogging().onProgress(token, 24);
                this.mRemoteInsetsController.hideInsets(i, z, token);
            } catch (RemoteException e) {
                Slog.w(DisplayContent.TAG, "Failed to deliver hideInsets", e);
                ImeTracker.forLogging().onFailed(token, 24);
            }
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public boolean canShowTransient() {
            return this.mCanShowTransient;
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public boolean isRequestedVisible(int i) {
            return ((WindowInsets.Type.ime() & i) != 0 && DisplayContent.this.getInsetsStateController().getImeSourceProvider().isImeShowing()) || (this.mRequestedVisibleTypes & i) != 0;
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public int getRequestedVisibleTypes() {
            return this.mRequestedVisibleTypes;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setRequestedVisibleTypes(int i) {
            if (this.mRequestedVisibleTypes != i) {
                this.mRequestedVisibleTypes = i;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MagnificationSpec getMagnificationSpec() {
        return this.mMagnificationSpec;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayArea findAreaForWindowType(int i, Bundle bundle, boolean z, boolean z2) {
        if (i >= 1 && i <= 99) {
            return this.mDisplayAreaPolicy.getTaskDisplayArea(bundle);
        }
        if (i == 2011 || i == 2012) {
            return getImeContainer();
        }
        return this.mDisplayAreaPolicy.findAreaForWindowType(i, bundle, z, z2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayArea findAreaForToken(WindowToken windowToken) {
        return findAreaForWindowType(windowToken.getWindowType(), windowToken.mOptions, windowToken.mOwnerCanManageAppTokens, windowToken.mRoundedCornerOverlay);
    }

    public IDisplayContentWrapper getWrapper() {
        return this.mDisplayContentWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class DisplayContentWrapper implements IDisplayContentWrapper {
        private DisplayContentWrapper() {
        }

        @Override // com.android.server.wm.IDisplayContentWrapper
        public IDisplayContentExt getExtImpl() {
            return DisplayContent.this.mDisplayContentExt;
        }

        @Override // com.android.server.wm.IDisplayContentWrapper
        public INonStaticDisplayContentExt getNonStaticExtImpl() {
            return DisplayContent.this.mNonStaticExt;
        }

        @Override // com.android.server.wm.IDisplayContentWrapper
        public ActivityRecord getFixedRotationLaunchingApp() {
            return DisplayContent.this.mFixedRotationLaunchingApp;
        }
    }
}
