package com.android.server.wm;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.LoadedApk;
import android.app.ResourcesManager;
import android.common.OplusFeatureCache;
import android.content.Context;
import android.content.Intent;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.ArraySet;
import android.util.PrintWriterPrinter;
import android.util.Slog;
import android.util.SparseArray;
import android.view.DisplayInfo;
import android.view.InsetsFlags;
import android.view.InsetsFrameProvider;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.Surface;
import android.view.ViewDebug;
import android.view.ViewRootImpl;
import android.view.WindowInsets;
import android.view.WindowLayout;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.window.ClientWindowFrames;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.ForceShowNavBarSettingsObserver;
import com.android.internal.policy.GestureNavigationSettingsObserver;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.statusbar.LetterboxDetails;
import com.android.internal.util.ScreenshotHelper;
import com.android.internal.util.ScreenshotRequest;
import com.android.internal.util.function.TriFunction;
import com.android.internal.view.AppearanceRegion;
import com.android.internal.widget.PointerLocationView;
import com.android.server.LocalServices;
import com.android.server.OplusIoThread;
import com.android.server.UiThread;
import com.android.server.input.InputManagerService;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.statusbar.StatusBarManagerInternal;
import com.android.server.wallpaper.WallpaperManagerInternal;
import com.android.server.wm.DisplayPolicy;
import com.android.server.wm.SystemGesturesPointerEventListener;
import com.android.server.wm.WindowManagerInternal;
import com.android.server.zenmode.IZenModeManagerExt;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;
import vendor.oplus.hardware.charger.ChargerErrorCode;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DisplayPolicy {
    static final int ANIMATION_NONE = -1;
    static final int ANIMATION_STYLEABLE = 0;
    private static final int INSETS_OVERRIDE_INDEX_INVALID = -1;
    private static final int MSG_DISABLE_POINTER_LOCATION = 5;
    private static final int MSG_ENABLE_POINTER_LOCATION = 4;
    private static final int NAV_BAR_FORCE_TRANSPARENT = 2;
    private static final int NAV_BAR_OPAQUE_WHEN_FREEFORM_OR_DOCKED = 0;
    private static final int NAV_BAR_TRANSLUCENT_WHEN_FREEFORM_OPAQUE_OTHERWISE = 1;
    private static final long PANIC_GESTURE_EXPIRATION = 30000;
    private static final String TAG = "WindowManager";
    private final AccessibilityManager mAccessibilityManager;
    private boolean mAllowLockscreenWhenOn;
    private final WindowManagerInternal.AppTransitionListener mAppTransitionListener;
    private volatile boolean mAwake;
    private int mBottomGestureAdditionalInset;
    private WindowState mBottomGestureHost;
    private DecorInsets.Cache mCachedDecorInsets;
    private boolean mCanSystemBarsBeShownByUser;
    private final boolean mCarDockEnablesAccelerometer;
    private final Context mContext;
    private Resources mCurrentUserResources;
    final DecorInsets mDecorInsets;
    private final boolean mDeskDockEnablesAccelerometer;
    private final DisplayContent mDisplayContent;
    private boolean mDreamingLockscreen;
    private String mFocusedApp;
    private WindowState mFocusedWindow;
    private boolean mForceConsumeSystemBars;
    private final ForceShowNavBarSettingsObserver mForceShowNavBarSettingsObserver;
    private boolean mForceShowNavigationBarEnabled;
    private boolean mForceShowSystemBars;
    private final GestureNavigationSettingsObserver mGestureNavigationSettingsObserver;
    private final Handler mHandler;
    private volatile boolean mHasNavigationBar;
    private volatile boolean mHasStatusBar;
    private volatile boolean mHdmiPlugged;
    private final ImmersiveModeConfirmation mImmersiveModeConfirmation;
    private boolean mIsFreeformWindowOverlappingWithNavBar;
    private boolean mIsImmersiveMode;
    private volatile boolean mKeyguardDrawComplete;
    private int mLastAppearance;
    private int mLastBehavior;
    private int mLastDisableFlags;
    private WindowState mLastFocusedWindow;
    private LetterboxDetails[] mLastLetterboxDetails;
    private boolean mLastShowingDream;
    private AppearanceRegion[] mLastStatusBarAppearanceRegions;
    private WindowState mLeftGestureHost;
    private int mLeftGestureInset;
    private final Object mLock;
    private WindowState mNavBarBackgroundWindow;
    private WindowState mNavBarColorWindowCandidate;
    private volatile boolean mNavigationBarAlwaysShowOnSideGesture;
    private volatile boolean mNavigationBarCanMove;
    private volatile WindowState mNotificationShade;
    private long mPendingPanicGestureUptime;
    private volatile boolean mPersistentVrModeEnabled;
    private PointerLocationView mPointerLocationView;
    private RefreshRatePolicy mRefreshRatePolicy;
    private boolean mRemoteInsetsControllerControlsSystemBars;
    private WindowState mRightGestureHost;
    private int mRightGestureInset;
    private volatile boolean mScreenOnEarly;
    private volatile boolean mScreenOnFully;
    private volatile WindowManagerPolicy.ScreenOnListener mScreenOnListener;
    private final ScreenshotHelper mScreenshotHelper;
    private final WindowManagerService mService;
    private boolean mShouldAttachNavBarToAppDuringTransition;
    private boolean mShowingDream;
    private StatusBarManagerInternal mStatusBarManagerInternal;
    private SystemGesturesPointerEventListener mSystemGestures;
    private WindowState mSystemUiControllingWindow;
    private WindowState mTopFullscreenOpaqueWindowState;
    private WindowState mTopGestureHost;
    private boolean mTopIsFullscreen;
    private final Context mUiContext;
    private volatile boolean mWindowManagerDrawComplete;
    private static final int SHOW_TYPES_FOR_SWIPE = WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars();
    private static final int SHOW_TYPES_FOR_PANIC = WindowInsets.Type.navigationBars();
    private static final boolean USE_CACHED_INSETS_FOR_DISPLAY_SWITCH = SystemProperties.getBoolean("persist.wm.debug.cached_insets_switch", true);
    private static final Rect sTmpRect = new Rect();
    private static final Rect sTmpRect2 = new Rect();
    private static final Rect sTmpDisplayCutoutSafe = new Rect();
    private static final ClientWindowFrames sTmpClientFrames = new ClientWindowFrames();
    private static final boolean IS_AGING_VERSION = "1".equals(SystemProperties.get("persist.sys.agingtest", ""));
    private static final boolean IS_HIGHTEMP_VERSION = "hightempaging".equals(SystemProperties.get("ro.oplus.image.my_engineering.type", ""));
    public IZenModeManagerExt mZenModeManagerExt = (IZenModeManagerExt) ExtLoader.type(IZenModeManagerExt.class).create();
    private final Object mServiceAcquireLock = new Object();
    private volatile int mLidState = -1;
    private volatile int mDockMode = 0;
    private WindowState mStatusBar = null;
    private WindowState mNavigationBar = null;
    private int mNavigationBarPosition = 4;
    private final ArraySet<WindowState> mInsetsSourceWindowsExceptIme = new ArraySet<>();
    private final ArraySet<ActivityRecord> mSystemBarColorApps = new ArraySet<>();
    private final ArraySet<ActivityRecord> mRelaunchingSystemBarColorApps = new ArraySet<>();
    private final ArrayList<AppearanceRegion> mStatusBarAppearanceRegionList = new ArrayList<>();
    private final ArrayList<WindowState> mStatusBarBackgroundWindows = new ArrayList<>();
    private final ArrayList<LetterboxDetails> mLetterboxDetails = new ArrayList<>();
    private int mLastRequestedVisibleTypes = WindowInsets.Type.defaultVisible();
    private final Rect mStatusBarColorCheckedBounds = new Rect();
    private final Rect mStatusBarBackgroundCheckedBounds = new Rect();
    private boolean mLastFocusIsFullscreen = false;
    private final WindowLayout mWindowLayout = new WindowLayout();
    private int mNavBarOpacityMode = 0;
    private final Runnable mHiddenNavPanic = new Runnable() { // from class: com.android.server.wm.DisplayPolicy.3
        @Override // java.lang.Runnable
        public void run() {
            synchronized (DisplayPolicy.this.mLock) {
                if (DisplayPolicy.this.mService.mPolicy.isUserSetupComplete()) {
                    DisplayPolicy.this.mPendingPanicGestureUptime = SystemClock.uptimeMillis();
                    DisplayPolicy.this.updateSystemBarAttributes();
                }
            }
        }
    };
    private DisplayPolicyWrapper mDisplayPolicyWrapper = new DisplayPolicyWrapper();

    private int clearNavBarOpaqueFlag(int i) {
        return i & (-3);
    }

    private static boolean isNavBarEmpty(int i) {
        return (i & 23068672) == 23068672;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StatusBarManagerInternal getStatusBarManagerInternal() {
        StatusBarManagerInternal statusBarManagerInternal;
        synchronized (this.mServiceAcquireLock) {
            if (this.mStatusBarManagerInternal == null) {
                this.mStatusBarManagerInternal = (StatusBarManagerInternal) LocalServices.getService(StatusBarManagerInternal.class);
            }
            statusBarManagerInternal = this.mStatusBarManagerInternal;
        }
        return statusBarManagerInternal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class PolicyHandler extends Handler {
        PolicyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 4) {
                DisplayPolicy.this.mDisplayPolicyWrapper.getExtImpl().getOplusUIHandler(this).post(new Runnable() { // from class: com.android.server.wm.DisplayPolicy$PolicyHandler$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        DisplayPolicy.PolicyHandler.this.lambda$handleMessage$0();
                    }
                });
            } else {
                if (i != 5) {
                    return;
                }
                DisplayPolicy.this.mDisplayPolicyWrapper.getExtImpl().getOplusUIHandler(this).post(new Runnable() { // from class: com.android.server.wm.DisplayPolicy$PolicyHandler$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        DisplayPolicy.PolicyHandler.this.lambda$handleMessage$1();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$0() {
            DisplayPolicy.this.enablePointerLocation();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$1() {
            DisplayPolicy.this.disablePointerLocation();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayPolicy(WindowManagerService windowManagerService, DisplayContent displayContent) {
        this.mService = windowManagerService;
        Context createDisplayContext = displayContent.isDefaultDisplay ? windowManagerService.mContext : windowManagerService.mContext.createDisplayContext(displayContent.getDisplay());
        this.mContext = createDisplayContext;
        Context uiContext = displayContent.isDefaultDisplay ? windowManagerService.mAtmService.getUiContext() : windowManagerService.mAtmService.mSystemThread.getSystemUiContext(displayContent.getDisplayId());
        this.mUiContext = uiContext;
        this.mDisplayContent = displayContent;
        this.mDecorInsets = new DecorInsets(displayContent);
        this.mLock = windowManagerService.getWindowManagerLock();
        int displayId = displayContent.getDisplayId();
        Resources resources = createDisplayContext.getResources();
        this.mCarDockEnablesAccelerometer = resources.getBoolean(R.bool.config_disableTransitionAnimation);
        this.mDeskDockEnablesAccelerometer = resources.getBoolean(R.bool.preferences_prefer_dual_pane);
        this.mCanSystemBarsBeShownByUser = !resources.getBoolean(R.bool.action_bar_embed_tabs) || resources.getBoolean(17891793);
        this.mAccessibilityManager = (AccessibilityManager) createDisplayContext.getSystemService("accessibility");
        if (!displayContent.isDefaultDisplay) {
            this.mAwake = true;
            this.mScreenOnEarly = true;
            this.mScreenOnFully = true;
        }
        this.mDisplayPolicyWrapper.getSocExtImpl().loadConfig();
        Looper looper = UiThread.getHandler().getLooper();
        this.mDisplayPolicyWrapper.getExtImpl().initOplusDisplayPolicy(this);
        Handler createPolicyHandler = this.mDisplayPolicyWrapper.getExtImpl().createPolicyHandler(looper, new PolicyHandler(looper));
        this.mHandler = createPolicyHandler;
        this.mDisplayPolicyWrapper.getExtImpl().initOplusDisplayPolicyEx(windowManagerService, this);
        if (!ViewRootImpl.CLIENT_TRANSIENT) {
            SystemGesturesPointerEventListener systemGesturesPointerEventListener = new SystemGesturesPointerEventListener(uiContext, createPolicyHandler, new AnonymousClass1(displayId));
            this.mSystemGestures = systemGesturesPointerEventListener;
            displayContent.registerPointerEventListener(systemGesturesPointerEventListener);
        }
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(displayId);
        this.mAppTransitionListener = anonymousClass2;
        displayContent.mAppTransition.registerListenerLocked(anonymousClass2);
        displayContent.mTransitionController.registerLegacyListener(anonymousClass2);
        this.mImmersiveModeConfirmation = new ImmersiveModeConfirmation(createDisplayContext, looper, windowManagerService.mVrModeEnabled, this.mCanSystemBarsBeShownByUser);
        this.mScreenshotHelper = displayContent.isDefaultDisplay ? new ScreenshotHelper(createDisplayContext) : null;
        if (displayContent.isDefaultDisplay) {
            this.mHasStatusBar = true;
            this.mHasNavigationBar = createDisplayContext.getResources().getBoolean(17891814);
            String str = SystemProperties.get("qemu.hw.mainkeys");
            if ("1".equals(str)) {
                this.mHasNavigationBar = false;
            } else if ("0".equals(str)) {
                this.mHasNavigationBar = true;
            }
        } else {
            this.mHasStatusBar = false;
            this.mHasNavigationBar = displayContent.supportsSystemDecorations();
        }
        this.mRefreshRatePolicy = new RefreshRatePolicy(windowManagerService, displayContent.getDisplayInfo(), windowManagerService.mHighRefreshRateDenylist);
        final GestureNavigationSettingsObserver gestureNavigationSettingsObserver = new GestureNavigationSettingsObserver(createPolicyHandler, createDisplayContext, new Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPolicy.this.lambda$new$0();
            }
        });
        this.mGestureNavigationSettingsObserver = gestureNavigationSettingsObserver;
        Objects.requireNonNull(gestureNavigationSettingsObserver);
        createPolicyHandler.post(new Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                gestureNavigationSettingsObserver.register();
            }
        });
        final ForceShowNavBarSettingsObserver forceShowNavBarSettingsObserver = new ForceShowNavBarSettingsObserver(createPolicyHandler, createDisplayContext);
        this.mForceShowNavBarSettingsObserver = forceShowNavBarSettingsObserver;
        forceShowNavBarSettingsObserver.setOnChangeRunnable(new Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPolicy.this.updateForceShowNavBarSettings();
            }
        });
        this.mForceShowNavigationBarEnabled = forceShowNavBarSettingsObserver.isEnabled();
        Objects.requireNonNull(forceShowNavBarSettingsObserver);
        createPolicyHandler.post(new Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                forceShowNavBarSettingsObserver.register();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.wm.DisplayPolicy$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class AnonymousClass1 implements SystemGesturesPointerEventListener.Callbacks {
        private static final long MOUSE_GESTURE_DELAY_MS = 500;
        final /* synthetic */ int val$displayId;
        private Runnable mOnSwipeFromLeft = new Runnable() { // from class: com.android.server.wm.DisplayPolicy$1$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPolicy.AnonymousClass1.this.onSwipeFromLeft();
            }
        };
        private Runnable mOnSwipeFromTop = new Runnable() { // from class: com.android.server.wm.DisplayPolicy$1$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPolicy.AnonymousClass1.this.onSwipeFromTop();
            }
        };
        private Runnable mOnSwipeFromRight = new Runnable() { // from class: com.android.server.wm.DisplayPolicy$1$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPolicy.AnonymousClass1.this.onSwipeFromRight();
            }
        };
        private Runnable mOnSwipeFromBottom = new Runnable() { // from class: com.android.server.wm.DisplayPolicy$1$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPolicy.AnonymousClass1.this.onSwipeFromBottom();
            }
        };

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onDebug() {
        }

        AnonymousClass1(int i) {
            this.val$displayId = i;
        }

        private Insets getControllableInsets(WindowState windowState) {
            if (windowState == null) {
                return Insets.NONE;
            }
            InsetsSourceProvider controllableInsetProvider = windowState.getControllableInsetProvider();
            if (controllableInsetProvider == null) {
                return Insets.NONE;
            }
            return controllableInsetProvider.getSource().calculateInsets(windowState.getBounds(), true);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onSwipeFromTop() {
            synchronized (DisplayPolicy.this.mLock) {
                DisplayPolicy displayPolicy = DisplayPolicy.this;
                displayPolicy.requestTransientBars(displayPolicy.mTopGestureHost, getControllableInsets(DisplayPolicy.this.mTopGestureHost).top > 0);
            }
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onSwipeFromBottom() {
            synchronized (DisplayPolicy.this.mLock) {
                DisplayPolicy displayPolicy = DisplayPolicy.this;
                displayPolicy.requestTransientBars(displayPolicy.mBottomGestureHost, getControllableInsets(DisplayPolicy.this.mBottomGestureHost).bottom > 0);
            }
        }

        private boolean allowsSideSwipe(Region region) {
            return (!DisplayPolicy.this.mNavigationBarAlwaysShowOnSideGesture || DisplayPolicy.this.mSystemGestures.currentGestureStartedInRegion(region) || DisplayPolicy.this.mDisplayPolicyWrapper.getExtImpl().isInPocketStudio(this.val$displayId)) ? false : true;
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onSwipeFromRight() {
            Region obtain = Region.obtain();
            synchronized (DisplayPolicy.this.mLock) {
                DisplayPolicy.this.mDisplayContent.calculateSystemGestureExclusion(obtain, null);
                boolean z = getControllableInsets(DisplayPolicy.this.mRightGestureHost).right > 0;
                if (z || allowsSideSwipe(obtain)) {
                    DisplayPolicy displayPolicy = DisplayPolicy.this;
                    displayPolicy.requestTransientBars(displayPolicy.mRightGestureHost, z);
                }
            }
            obtain.recycle();
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onSwipeFromLeft() {
            Region obtain = Region.obtain();
            synchronized (DisplayPolicy.this.mLock) {
                DisplayPolicy.this.mDisplayContent.calculateSystemGestureExclusion(obtain, null);
                boolean z = getControllableInsets(DisplayPolicy.this.mLeftGestureHost).left > 0;
                if (z || allowsSideSwipe(obtain)) {
                    DisplayPolicy displayPolicy = DisplayPolicy.this;
                    displayPolicy.requestTransientBars(displayPolicy.mLeftGestureHost, z);
                }
                ((IZoomWindowManagerExt) ExtLoader.type(IZoomWindowManagerExt.class).create()).gestureSwipeFromBottom();
            }
            obtain.recycle();
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onFling(int i) {
            if (DisplayPolicy.this.mService.mPowerManagerInternal != null) {
                DisplayPolicy.this.mService.mPowerManagerInternal.setPowerBoost(0, i);
            }
            if (i > 1000) {
                DisplayPolicy.this.mDisplayPolicyWrapper.getExtImpl().pokeDynamicVsyncAnimation(i + 1000, "OnFling");
            }
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onVerticalFling(final int i) {
            OplusIoThread.getHandler().post(new Runnable() { // from class: com.android.server.wm.DisplayPolicy$1$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayPolicy.AnonymousClass1.this.lambda$onVerticalFling$0(i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onVerticalFling$0(int i) {
            DisplayPolicy.this.mDisplayPolicyWrapper.getSocExtImpl().hookOnVerticalFling(i);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onHorizontalFling(final int i) {
            OplusIoThread.getHandler().post(new Runnable() { // from class: com.android.server.wm.DisplayPolicy$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayPolicy.AnonymousClass1.this.lambda$onHorizontalFling$1(i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onHorizontalFling$1(int i) {
            DisplayPolicy.this.mDisplayPolicyWrapper.getSocExtImpl().hookOnHorizontalFling(i);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onScroll(final boolean z) {
            OplusIoThread.getHandler().post(new Runnable() { // from class: com.android.server.wm.DisplayPolicy$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayPolicy.AnonymousClass1.this.lambda$onScroll$2(z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onScroll$2(boolean z) {
            DisplayPolicy.this.mDisplayPolicyWrapper.getSocExtImpl().hookOnScroll(z);
        }

        private WindowOrientationListener getOrientationListener() {
            DisplayRotation displayRotation = DisplayPolicy.this.mDisplayContent.getDisplayRotation();
            if (displayRotation != null) {
                return displayRotation.getOrientationListener();
            }
            return null;
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onDown() {
            WindowOrientationListener orientationListener = getOrientationListener();
            if (orientationListener != null) {
                orientationListener.onTouchStart();
            }
            DisplayPolicy.this.mDisplayPolicyWrapper.getSocExtImpl().hookOnDown();
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onUpOrCancel() {
            WindowOrientationListener orientationListener = getOrientationListener();
            if (orientationListener != null) {
                orientationListener.onTouchEnd();
            }
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseHoverAtLeft() {
            DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromLeft);
            DisplayPolicy.this.mHandler.postDelayed(this.mOnSwipeFromLeft, MOUSE_GESTURE_DELAY_MS);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseHoverAtTop() {
            DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromTop);
            DisplayPolicy.this.mHandler.postDelayed(this.mOnSwipeFromTop, MOUSE_GESTURE_DELAY_MS);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseHoverAtRight() {
            DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromRight);
            DisplayPolicy.this.mHandler.postDelayed(this.mOnSwipeFromRight, MOUSE_GESTURE_DELAY_MS);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseHoverAtBottom() {
            DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromBottom);
            DisplayPolicy.this.mHandler.postDelayed(this.mOnSwipeFromBottom, MOUSE_GESTURE_DELAY_MS);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseLeaveFromLeft() {
            DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromLeft);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseLeaveFromTop() {
            DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromTop);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseLeaveFromRight() {
            DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromRight);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseLeaveFromBottom() {
            DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromBottom);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.wm.DisplayPolicy$2, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class AnonymousClass2 extends WindowManagerInternal.AppTransitionListener {
        private Runnable mAppTransitionCancelled;
        private Runnable mAppTransitionFinished;
        private Runnable mAppTransitionPending;
        final /* synthetic */ int val$displayId;

        AnonymousClass2(final int i) {
            this.val$displayId = i;
            this.mAppTransitionPending = new Runnable() { // from class: com.android.server.wm.DisplayPolicy$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayPolicy.AnonymousClass2.this.lambda$$0(i);
                }
            };
            this.mAppTransitionCancelled = new Runnable() { // from class: com.android.server.wm.DisplayPolicy$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayPolicy.AnonymousClass2.this.lambda$$1(i);
                }
            };
            this.mAppTransitionFinished = new Runnable() { // from class: com.android.server.wm.DisplayPolicy$2$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayPolicy.AnonymousClass2.this.lambda$$2(i);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$$0(int i) {
            StatusBarManagerInternal statusBarManagerInternal = DisplayPolicy.this.getStatusBarManagerInternal();
            if (statusBarManagerInternal != null) {
                statusBarManagerInternal.appTransitionPending(i);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$$1(int i) {
            StatusBarManagerInternal statusBarManagerInternal = DisplayPolicy.this.getStatusBarManagerInternal();
            if (statusBarManagerInternal != null) {
                statusBarManagerInternal.appTransitionCancelled(i);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$$2(int i) {
            StatusBarManagerInternal statusBarManagerInternal = DisplayPolicy.this.getStatusBarManagerInternal();
            if (statusBarManagerInternal != null) {
                statusBarManagerInternal.appTransitionFinished(i);
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionPendingLocked() {
            DisplayPolicy.this.mHandler.post(this.mAppTransitionPending);
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public int onAppTransitionStartingLocked(final long j, final long j2) {
            DisplayPolicy.this.mHandler.post(new Runnable() { // from class: com.android.server.wm.DisplayPolicy$2$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayPolicy.AnonymousClass2.this.lambda$onAppTransitionStartingLocked$3(j, j2);
                }
            });
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAppTransitionStartingLocked$3(long j, long j2) {
            StatusBarManagerInternal statusBarManagerInternal = DisplayPolicy.this.getStatusBarManagerInternal();
            if (statusBarManagerInternal != null) {
                statusBarManagerInternal.appTransitionStarting(DisplayPolicy.this.mContext.getDisplayId(), j, j2);
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionCancelledLocked(boolean z) {
            DisplayPolicy.this.mHandler.post(this.mAppTransitionCancelled);
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionFinishedLocked(IBinder iBinder) {
            DisplayPolicy.this.mHandler.post(this.mAppTransitionFinished);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        synchronized (this.mLock) {
            onConfigurationChanged();
            if (!ViewRootImpl.CLIENT_TRANSIENT) {
                this.mSystemGestures.onConfigurationChanged();
            }
            this.mDisplayContent.updateSystemGestureExclusion();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateForceShowNavBarSettings() {
        synchronized (this.mLock) {
            this.mForceShowNavigationBarEnabled = this.mForceShowNavBarSettingsObserver.isEnabled();
            updateSystemBarAttributes();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void systemReady() {
        if (!ViewRootImpl.CLIENT_TRANSIENT) {
            this.mSystemGestures.systemReady();
        }
        if (this.mService.mPointerLocationEnabled) {
            setPointerLocationEnabled(true);
        }
    }

    private int getDisplayId() {
        return this.mDisplayContent.getDisplayId();
    }

    public void setHdmiPlugged(boolean z) {
        setHdmiPlugged(z, false);
    }

    public void setHdmiPlugged(boolean z, boolean z2) {
        if (z2 || this.mHdmiPlugged != z) {
            this.mHdmiPlugged = z;
            this.mService.updateRotation(true, true);
            Intent intent = new Intent("android.intent.action.HDMI_PLUGGED");
            intent.addFlags(67108864);
            intent.putExtra("state", z);
            this.mContext.sendStickyBroadcastAsUser(intent, UserHandle.ALL);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isHdmiPlugged() {
        return this.mHdmiPlugged;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCarDockEnablesAccelerometer() {
        return this.mCarDockEnablesAccelerometer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDeskDockEnablesAccelerometer() {
        return this.mDeskDockEnablesAccelerometer;
    }

    public void setPersistentVrModeEnabled(boolean z) {
        this.mPersistentVrModeEnabled = z;
    }

    public boolean isPersistentVrModeEnabled() {
        return this.mPersistentVrModeEnabled;
    }

    public void setDockMode(int i) {
        this.mDockMode = i;
    }

    public int getDockMode() {
        return this.mDockMode;
    }

    public boolean hasNavigationBar() {
        return this.mHasNavigationBar;
    }

    public boolean hasStatusBar() {
        return this.mHasStatusBar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasSideGestures() {
        return this.mHasNavigationBar && (this.mLeftGestureInset > 0 || this.mRightGestureInset > 0);
    }

    public boolean navigationBarCanMove() {
        return this.mNavigationBarCanMove;
    }

    public void setLidState(int i) {
        this.mLidState = i;
    }

    public int getLidState() {
        return this.mLidState;
    }

    public void setAwake(boolean z) {
        synchronized (this.mLock) {
            if (z == this.mAwake) {
                return;
            }
            this.mAwake = z;
            if (this.mDisplayContent.isDefaultDisplay) {
                this.mService.mAtmService.mKeyguardController.updateDeferTransitionForAod(this.mAwake);
            }
        }
    }

    public boolean isAwake() {
        return this.mAwake;
    }

    public boolean isScreenOnEarly() {
        return this.mScreenOnEarly;
    }

    public boolean isScreenOnFully() {
        return this.mScreenOnFully;
    }

    public boolean isKeyguardDrawComplete() {
        return this.mKeyguardDrawComplete;
    }

    public boolean isWindowManagerDrawComplete() {
        return this.mWindowManagerDrawComplete;
    }

    public boolean isForceShowNavigationBarEnabled() {
        return this.mForceShowNavigationBarEnabled;
    }

    public WindowManagerPolicy.ScreenOnListener getScreenOnListener() {
        return this.mScreenOnListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRemoteInsetsControllerControllingSystemBars() {
        return this.mRemoteInsetsControllerControlsSystemBars;
    }

    @VisibleForTesting
    void setRemoteInsetsControllerControlsSystemBars(boolean z) {
        this.mRemoteInsetsControllerControlsSystemBars = z;
    }

    public void screenTurnedOn(WindowManagerPolicy.ScreenOnListener screenOnListener) {
        Slog.d(TAG, "screenTurnedOn  " + this.mDisplayContent);
        synchronized (this.mLock) {
            this.mScreenOnEarly = true;
            this.mScreenOnFully = false;
            this.mKeyguardDrawComplete = false;
            this.mWindowManagerDrawComplete = false;
            this.mScreenOnListener = screenOnListener;
        }
    }

    public void screenTurnedOff() {
        synchronized (this.mLock) {
            this.mScreenOnEarly = false;
            this.mScreenOnFully = false;
            this.mKeyguardDrawComplete = false;
            this.mWindowManagerDrawComplete = false;
            this.mScreenOnListener = null;
        }
        Slog.d(TAG, "screenTurnedOff  " + this.mDisplayContent);
    }

    public boolean finishKeyguardDrawn() {
        synchronized (this.mLock) {
            if (this.mScreenOnEarly && !this.mKeyguardDrawComplete) {
                this.mKeyguardDrawComplete = true;
                this.mWindowManagerDrawComplete = false;
                return true;
            }
            Slog.d(TAG, "finishKeyguardDrawn  mScreenOnEarly:" + this.mScreenOnEarly + "," + this.mDisplayContent);
            return false;
        }
    }

    public boolean finishWindowsDrawn() {
        synchronized (this.mLock) {
            if (this.mScreenOnEarly && !this.mWindowManagerDrawComplete) {
                this.mWindowManagerDrawComplete = true;
                return true;
            }
            Slog.d(TAG, "finishWindowsDrawn  mScreenOnEarly:" + this.mScreenOnEarly + "," + this.mDisplayContent);
            return false;
        }
    }

    public boolean finishScreenTurningOn() {
        synchronized (this.mLock) {
            if (ProtoLogCache.WM_DEBUG_SCREEN_ON_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_SCREEN_ON, 1865125884, 1023, (String) null, new Object[]{Boolean.valueOf(this.mAwake), Boolean.valueOf(this.mScreenOnEarly), Boolean.valueOf(this.mScreenOnFully), Boolean.valueOf(this.mKeyguardDrawComplete), Boolean.valueOf(this.mWindowManagerDrawComplete)});
            }
            if (!this.mScreenOnFully && this.mScreenOnEarly && this.mWindowManagerDrawComplete && (!this.mAwake || this.mKeyguardDrawComplete || getDisplayId() != 0)) {
                if (!ProtoLogGroup.WM_DEBUG_SCREEN_ON.isLogToLogcat()) {
                    Slog.i(TAG, "Finished screen turning on..." + this.mDisplayContent.getDisplayId());
                } else if (ProtoLogCache.WM_DEBUG_SCREEN_ON_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_SCREEN_ON, 1140424002, 0, (String) null, (Object[]) null);
                }
                this.mScreenOnListener = null;
                this.mScreenOnFully = true;
                this.mDisplayPolicyWrapper.getExtImpl().finishScreenTurningOn();
                return true;
            }
            Slog.d(TAG, "finishScreenTurningOn  " + this.mScreenOnFully + "," + this.mScreenOnEarly + "," + this.mWindowManagerDrawComplete + "," + this.mDisplayContent);
            return false;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0013, code lost:
    
        if (r0 != 2006) goto L34;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void adjustWindowParamsLw(WindowState windowState, WindowManager.LayoutParams layoutParams) {
        ActivityRecord activityRecord;
        int i = layoutParams.type;
        if (i != 1) {
            if (i != 2013) {
                if (i != 2015) {
                    if (i == 2005) {
                        long j = layoutParams.hideTimeoutMilliseconds;
                        if (j < 0 || j > 4100) {
                            layoutParams.hideTimeoutMilliseconds = 4100L;
                        }
                        layoutParams.hideTimeoutMilliseconds = this.mAccessibilityManager.getRecommendedTimeoutMillis((int) layoutParams.hideTimeoutMilliseconds, 2);
                        layoutParams.flags |= 16;
                    }
                }
                layoutParams.flags = (layoutParams.flags | 24) & (-262145);
            } else {
                layoutParams.layoutInDisplayCutoutMode = 3;
            }
        } else if (layoutParams.isFullscreen() && (activityRecord = windowState.mActivityRecord) != null && activityRecord.fillsParent() && (windowState.mAttrs.privateFlags & 32768) != 0 && layoutParams.getFitInsetsTypes() != 0) {
            throw new IllegalArgumentException("Illegal attributes: Main activity window that isn't translucent trying to fit insets: " + layoutParams.getFitInsetsTypes() + " attrs=" + layoutParams);
        }
        if (WindowManager.LayoutParams.isSystemAlertWindowType(layoutParams.type)) {
            float f = this.mService.mMaximumObscuringOpacityForTouch;
            if (layoutParams.alpha > f && (layoutParams.flags & 16) != 0 && !windowState.getWrapper().getExtImpl().isOplusTrustedWindow(layoutParams) && (layoutParams.privateFlags & 536870912) == 0) {
                Slog.w(TAG, String.format("App %s has a system alert window (type = %d) with FLAG_NOT_TOUCHABLE and LayoutParams.alpha = %.2f > %.2f, setting alpha to %.2f to let touches pass through (if this is isn't desirable, remove flag FLAG_NOT_TOUCHABLE).", layoutParams.packageName, Integer.valueOf(layoutParams.type), Float.valueOf(layoutParams.alpha), Float.valueOf(f), Float.valueOf(f)));
                layoutParams.alpha = f;
                windowState.mWinAnimator.mAlpha = f;
            }
        }
        if (!windowState.mSession.mCanSetUnrestrictedGestureExclusion) {
            layoutParams.privateFlags &= -33;
        }
        InsetsSourceProvider controllableInsetProvider = windowState.getControllableInsetProvider();
        if (controllableInsetProvider != null && controllableInsetProvider.getSource().insetsRoundedCornerFrame() != layoutParams.insetsRoundedCornerFrame) {
            controllableInsetProvider.getSource().setInsetsRoundedCornerFrame(layoutParams.insetsRoundedCornerFrame);
        }
        this.mDisplayPolicyWrapper.getExtImpl().adjustWindowParamsLw(windowState, layoutParams);
    }

    public void setDropInputModePolicy(WindowState windowState, WindowManager.LayoutParams layoutParams) {
        if (layoutParams.type == 2005 && (layoutParams.privateFlags & 536870912) == 0) {
            this.mService.mTransactionFactory.get().setDropInputMode(windowState.getSurfaceControl(), 1).apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x003d, code lost:
    
        if (r0 != 2041) goto L46;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int validateAddingWindowLw(WindowManager.LayoutParams layoutParams, int i, int i2) {
        if ((layoutParams.privateFlags & 536870912) != 0) {
            this.mContext.enforcePermission("android.permission.INTERNAL_SYSTEM_WINDOW", i, i2, "DisplayPolicy");
        }
        if ((layoutParams.privateFlags & ChargerErrorCode.ERR_FILE_FAILURE_ACCESS) != 0) {
            ActivityTaskManagerService.enforceTaskPermission("DisplayPolicy");
        }
        int i3 = layoutParams.type;
        if (i3 == 2000) {
            this.mContext.enforcePermission("android.permission.STATUS_BAR_SERVICE", i, i2, "DisplayPolicy");
            WindowState windowState = this.mStatusBar;
            if (windowState != null && windowState.isAlive()) {
                return -7;
            }
        } else {
            if (i3 == 2014) {
                return -10;
            }
            if (i3 != 2017) {
                if (i3 == 2019) {
                    this.mContext.enforcePermission("android.permission.STATUS_BAR_SERVICE", i, i2, "DisplayPolicy");
                    WindowState windowState2 = this.mNavigationBar;
                    if (windowState2 != null && windowState2.isAlive()) {
                        return -7;
                    }
                } else if (i3 != 2024) {
                    if (i3 != 2033) {
                        if (i3 == 2040) {
                            this.mContext.enforcePermission("android.permission.STATUS_BAR_SERVICE", i, i2, "DisplayPolicy");
                            if (this.mNotificationShade != null && this.mNotificationShade.isAlive()) {
                                return -7;
                            }
                        }
                    }
                } else if (!this.mService.mAtmService.isCallerRecents(i2)) {
                    this.mContext.enforcePermission("android.permission.STATUS_BAR_SERVICE", i, i2, "DisplayPolicy");
                }
            }
            this.mContext.enforcePermission("android.permission.STATUS_BAR_SERVICE", i, i2, "DisplayPolicy");
        }
        if (layoutParams.providedInsets == null || this.mService.mAtmService.isCallerRecents(i2)) {
            return 0;
        }
        this.mContext.enforcePermission("android.permission.STATUS_BAR_SERVICE", i, i2, "DisplayPolicy");
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addWindowLw(WindowState windowState, WindowManager.LayoutParams layoutParams) {
        SparseArray<TriFunction<DisplayFrames, WindowContainer, Rect, Integer>> sparseArray;
        int i = layoutParams.type;
        if (i == 2000) {
            this.mStatusBar = windowState;
        } else if (i == 2019) {
            this.mNavigationBar = windowState;
        } else if (i == 2024) {
            this.mDisplayPolicyWrapper.getExtImpl().addTaskBar(windowState, true);
        } else if (i == 2040) {
            this.mNotificationShade = windowState;
        }
        InsetsFrameProvider[] insetsFrameProviderArr = layoutParams.providedInsets;
        if (insetsFrameProviderArr != null) {
            for (int length = insetsFrameProviderArr.length - 1; length >= 0; length--) {
                InsetsFrameProvider insetsFrameProvider = layoutParams.providedInsets[length];
                TriFunction<DisplayFrames, WindowContainer, Rect, Integer> frameProvider = getFrameProvider(windowState, length, -1);
                InsetsFrameProvider.InsetsSizeOverride[] insetsSizeOverrides = insetsFrameProvider.getInsetsSizeOverrides();
                if (insetsSizeOverrides != null) {
                    sparseArray = new SparseArray<>();
                    for (int length2 = insetsSizeOverrides.length - 1; length2 >= 0; length2--) {
                        sparseArray.put(insetsSizeOverrides[length2].getWindowType(), getFrameProvider(windowState, length, length2));
                    }
                } else {
                    sparseArray = null;
                }
                this.mDisplayContent.getInsetsStateController().getOrCreateSourceProvider(insetsFrameProvider.getId(), insetsFrameProvider.getType()).setWindowContainer(windowState, frameProvider, sparseArray);
                this.mInsetsSourceWindowsExceptIme.add(windowState);
            }
        }
    }

    private static TriFunction<DisplayFrames, WindowContainer, Rect, Integer> getFrameProvider(final WindowState windowState, final int i, final int i2) {
        return new TriFunction() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda2
            public final Object apply(Object obj, Object obj2, Object obj3) {
                Integer lambda$getFrameProvider$1;
                lambda$getFrameProvider$1 = DisplayPolicy.lambda$getFrameProvider$1(WindowState.this, i, i2, (DisplayFrames) obj, (WindowContainer) obj2, (Rect) obj3);
                return lambda$getFrameProvider$1;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$getFrameProvider$1(WindowState windowState, int i, int i2, DisplayFrames displayFrames, WindowContainer windowContainer, Rect rect) {
        Insets insetsSize;
        WindowManager.LayoutParams forRotation = windowState.mAttrs.forRotation(displayFrames.mRotation);
        InsetsFrameProvider insetsFrameProvider = forRotation.providedInsets[i];
        Rect rect2 = displayFrames.mUnrestricted;
        Rect rect3 = displayFrames.mDisplayCutoutSafe;
        int source = insetsFrameProvider.getSource();
        boolean z = false;
        if (source == 0) {
            rect.set(rect2);
        } else if (source == 1) {
            rect.set(windowContainer.getBounds());
        } else if (source != 2) {
            if (source == 3) {
                rect.set(insetsFrameProvider.getArbitraryRectangle());
            }
        } else if ((forRotation.privateFlags & 4096) != 0) {
            z = true;
        }
        if (i2 == -1) {
            insetsSize = insetsFrameProvider.getInsetsSize();
        } else {
            insetsSize = insetsFrameProvider.getInsetsSizeOverrides()[i2].getInsetsSize();
        }
        if (insetsFrameProvider.getMinimalInsetsSizeInDisplayCutoutSafe() != null) {
            sTmpRect2.set(rect);
        }
        calculateInsetsFrame(rect, insetsSize);
        if (z && insetsSize != null) {
            WindowLayout.extendFrameByCutout(rect3, rect2, rect, sTmpRect);
        }
        if (insetsFrameProvider.getMinimalInsetsSizeInDisplayCutoutSafe() != null) {
            Rect rect4 = sTmpRect2;
            calculateInsetsFrame(rect4, insetsFrameProvider.getMinimalInsetsSizeInDisplayCutoutSafe());
            WindowLayout.extendFrameByCutout(rect3, rect2, rect4, sTmpRect);
            if (rect4.contains(rect)) {
                rect.set(rect4);
            }
        }
        DisplayContent displayContent = windowState.getDisplayContent();
        if (displayContent != null && displayContent.getDisplayPolicy() != null) {
            displayContent.getDisplayPolicy().mDisplayPolicyWrapper.getExtImpl().updateFrameProvider(displayFrames, windowState, rect, forRotation, insetsFrameProvider);
        } else if (displayContent == null) {
            if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
                Slog.d(TAG, "getFrameProvider() DisplayContent is null");
            }
        } else if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
            Slog.d(TAG, "getFrameProvider() DisplayPolicy is null");
        }
        return Integer.valueOf(insetsFrameProvider.getFlags());
    }

    private static void calculateInsetsFrame(Rect rect, Insets insets) {
        if (insets == null) {
            return;
        }
        int i = insets.left;
        if (i != 0) {
            rect.right = rect.left + i;
            return;
        }
        int i2 = insets.top;
        if (i2 != 0) {
            rect.bottom = rect.top + i2;
            return;
        }
        int i3 = insets.right;
        if (i3 != 0) {
            rect.left = rect.right - i3;
            return;
        }
        int i4 = insets.bottom;
        if (i4 != 0) {
            rect.top = rect.bottom - i4;
        } else {
            rect.setEmpty();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TriFunction<DisplayFrames, WindowContainer, Rect, Integer> getImeSourceFrameProvider() {
        return new TriFunction() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda13
            public final Object apply(Object obj, Object obj2, Object obj3) {
                Integer lambda$getImeSourceFrameProvider$2;
                lambda$getImeSourceFrameProvider$2 = DisplayPolicy.this.lambda$getImeSourceFrameProvider$2((DisplayFrames) obj, (WindowContainer) obj2, (Rect) obj3);
                return lambda$getImeSourceFrameProvider$2;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$getImeSourceFrameProvider$2(DisplayFrames displayFrames, WindowContainer windowContainer, Rect rect) {
        WindowState asWindowState = windowContainer.asWindowState();
        if (asWindowState == null) {
            throw new IllegalArgumentException("IME insets must be provided by a window.");
        }
        if (this.mNavigationBar != null && navigationBarPosition(displayFrames.mRotation) == 4) {
            Rect rect2 = sTmpRect;
            rect2.set(rect);
            if (this.mDisplayPolicyWrapper.getExtImpl().updateImeSourceFrame(asWindowState, rect)) {
                return 0;
            }
            rect2.intersectUnchecked(this.mNavigationBar.getFrame());
            rect.inset(asWindowState.mGivenContentInsets);
            rect.union(rect2);
        } else {
            rect.inset(asWindowState.mGivenContentInsets);
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeWindowLw(WindowState windowState) {
        if (this.mStatusBar == windowState) {
            this.mStatusBar = null;
        } else if (this.mNavigationBar == windowState) {
            this.mNavigationBar = null;
        } else if (this.mNotificationShade == windowState) {
            this.mNotificationShade = null;
        }
        if (this.mDisplayPolicyWrapper.getExtImpl().getTaskBar() == windowState) {
            this.mDisplayPolicyWrapper.getExtImpl().addTaskBar(windowState, false);
        }
        if (this.mLastFocusedWindow == windowState) {
            this.mLastFocusedWindow = null;
        }
        if (windowState.hasInsetsSourceProvider()) {
            SparseArray insetsSourceProviders = windowState.getInsetsSourceProviders();
            InsetsStateController insetsStateController = this.mDisplayContent.getInsetsStateController();
            for (int size = insetsSourceProviders.size() - 1; size >= 0; size--) {
                InsetsSourceProvider insetsSourceProvider = (InsetsSourceProvider) insetsSourceProviders.valueAt(size);
                insetsSourceProvider.setWindowContainer(null, null, null);
                insetsStateController.removeSourceProvider(insetsSourceProvider.getSource().getId());
            }
        }
        this.mInsetsSourceWindowsExceptIme.remove(windowState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState getStatusBar() {
        return this.mStatusBar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState getNotificationShade() {
        return this.mNotificationShade;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState getNavigationBar() {
        return this.mNavigationBar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isImmersiveMode() {
        return this.mIsImmersiveMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int selectAnimation(WindowState windowState, int i) {
        if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ANIM, 341360111, 4, (String) null, new Object[]{String.valueOf(windowState), Long.valueOf(i)});
        }
        if (i != 5 || !windowState.hasAppShownWindows()) {
            return 0;
        }
        if (windowState.isActivityTypeHome()) {
            return -1;
        }
        if (!ProtoLogCache.WM_DEBUG_ANIM_enabled) {
            return R.anim.app_starting_exit;
        }
        ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ANIM, -1303628829, 0, (String) null, (Object[]) null);
        return R.anim.app_starting_exit;
    }

    public boolean areSystemBarsForcedConsumedLw() {
        return this.mForceConsumeSystemBars;
    }

    public boolean areSystemBarsForcedShownLw() {
        return this.mForceShowSystemBars;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void simulateLayoutDisplay(DisplayFrames displayFrames) {
        sTmpClientFrames.attachedFrame = null;
        for (int size = this.mInsetsSourceWindowsExceptIme.size() - 1; size >= 0; size--) {
            WindowState valueAt = this.mInsetsSourceWindowsExceptIme.valueAt(size);
            WindowLayout windowLayout = this.mWindowLayout;
            WindowManager.LayoutParams forRotation = valueAt.mAttrs.forRotation(displayFrames.mRotation);
            InsetsState insetsState = displayFrames.mInsetsState;
            Rect rect = displayFrames.mDisplayCutoutSafe;
            Rect rect2 = displayFrames.mUnrestricted;
            int windowingMode = valueAt.getWindowingMode();
            int requestedVisibleTypes = valueAt.getRequestedVisibleTypes();
            float f = valueAt.mGlobalScale;
            ClientWindowFrames clientWindowFrames = sTmpClientFrames;
            windowLayout.computeFrames(forRotation, insetsState, rect, rect2, windowingMode, -1, -1, requestedVisibleTypes, f, clientWindowFrames);
            if (!this.mDisplayPolicyWrapper.getExtImpl().checkWindowForSimulateLayoutDisplay(valueAt)) {
                clientWindowFrames.frame.setEmpty();
            }
            SparseArray insetsSourceProviders = valueAt.getInsetsSourceProviders();
            InsetsState insetsState2 = displayFrames.mInsetsState;
            for (int size2 = insetsSourceProviders.size() - 1; size2 >= 0; size2--) {
                insetsState2.addSource(((InsetsSourceProvider) insetsSourceProviders.valueAt(size2)).createSimulatedSource(displayFrames, sTmpClientFrames.frame));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDisplayInfoChanged(DisplayInfo displayInfo) {
        if (ViewRootImpl.CLIENT_TRANSIENT) {
            return;
        }
        this.mSystemGestures.onDisplayInfoChanged(displayInfo);
    }

    public void layoutWindowLw(WindowState windowState, WindowState windowState2, DisplayFrames displayFrames) {
        if (windowState.skipLayout()) {
            return;
        }
        DisplayFrames displayFrames2 = windowState.getDisplayFrames(displayFrames);
        WindowManager.LayoutParams forRotation = windowState.mAttrs.forRotation(displayFrames2.mRotation);
        windowState.getWrapper().getExtImpl().updateAttrsBeforeCompute(forRotation);
        ClientWindowFrames clientWindowFrames = sTmpClientFrames;
        clientWindowFrames.attachedFrame = windowState2 != null ? windowState2.getFrame() : null;
        boolean z = forRotation == windowState.mAttrs;
        int i = z ? windowState.mRequestedWidth : -1;
        int i2 = z ? windowState.mRequestedHeight : -1;
        this.mDisplayPolicyWrapper.getExtImpl().layoutInFullScreen(windowState, null);
        this.mWindowLayout.computeFrames(forRotation, windowState.getInsetsState(), displayFrames2.mDisplayCutoutSafe, windowState.getBounds(), windowState.getWindowingMode(), i, i2, windowState.getRequestedVisibleTypes(), windowState.mGlobalScale, clientWindowFrames);
        IOplusCarModeManager iOplusCarModeManager = IOplusCarModeManager.DEFAULT;
        ((IOplusCarModeManager) OplusFeatureCache.get(iOplusCarModeManager)).layoutCarDockBar(this.mDisplayContent, displayFrames2);
        ((IOplusCarModeManager) OplusFeatureCache.get(iOplusCarModeManager)).adjustWindowFrameForCarDockBarInsets(this.mDisplayContent, windowState, clientWindowFrames);
        if (this.mDisplayPolicyWrapper.getExtImpl().restrictFullScreenActivityRectInCompactWindow(windowState, forRotation.flags, forRotation.type, forRotation.subtreeSystemUiVisibility | forRotation.systemUiVisibility)) {
            DisplayContent defaultDisplayContentLocked = this.mService.getDefaultDisplayContentLocked();
            Rect rect = new Rect();
            defaultDisplayContentLocked.getStableRect(rect);
            clientWindowFrames.frame.bottom = rect.bottom;
        }
        this.mService.mAtmService.getWrapper().getFlexibleExtImpl().adjustInputMethodTargetFrame(this.mDisplayContent, windowState, clientWindowFrames);
        windowState.setFrames(clientWindowFrames, windowState.mRequestedWidth, windowState.mRequestedHeight);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState getTopFullscreenOpaqueWindow() {
        return this.mTopFullscreenOpaqueWindowState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTopLayoutFullscreen() {
        return this.mTopIsFullscreen;
    }

    public void beginPostLayoutPolicyLw() {
        this.mLeftGestureHost = null;
        this.mTopGestureHost = null;
        this.mRightGestureHost = null;
        this.mBottomGestureHost = null;
        this.mTopFullscreenOpaqueWindowState = null;
        this.mNavBarColorWindowCandidate = null;
        this.mNavBarBackgroundWindow = null;
        this.mStatusBarAppearanceRegionList.clear();
        this.mLetterboxDetails.clear();
        this.mStatusBarBackgroundWindows.clear();
        this.mStatusBarColorCheckedBounds.setEmpty();
        this.mStatusBarBackgroundCheckedBounds.setEmpty();
        this.mSystemBarColorApps.clear();
        this.mAllowLockscreenWhenOn = false;
        this.mShowingDream = false;
        this.mIsFreeformWindowOverlappingWithNavBar = false;
    }

    public void applyPostLayoutPolicyLw(WindowState windowState, WindowManager.LayoutParams layoutParams, WindowState windowState2, WindowState windowState3) {
        LetterboxDetails letterboxDetails;
        if (layoutParams.type == 2019) {
            this.mNavigationBarPosition = navigationBarPosition(this.mDisplayContent.mDisplayFrames.mRotation);
        }
        boolean canAffectSystemUiFlags = windowState.canAffectSystemUiFlags();
        if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
            Slog.i(TAG, "Win " + windowState + ": affectsSystemUi=" + canAffectSystemUiFlags);
        }
        applyKeyguardPolicy(windowState, windowState3);
        if (!this.mIsFreeformWindowOverlappingWithNavBar && windowState.inFreeformWindowingMode() && windowState.mActivityRecord != null && isOverlappingWithNavBar(windowState)) {
            this.mIsFreeformWindowOverlappingWithNavBar = true;
        }
        boolean z = false;
        if (windowState.hasInsetsSourceProvider()) {
            SparseArray insetsSourceProviders = windowState.getInsetsSourceProviders();
            Rect bounds = windowState.getBounds();
            for (int size = insetsSourceProviders.size() - 1; size >= 0; size--) {
                InsetsSource source = ((InsetsSourceProvider) insetsSourceProviders.valueAt(size)).getSource();
                if ((source.getType() & (WindowInsets.Type.systemGestures() | WindowInsets.Type.mandatorySystemGestures())) != 0 && (this.mLeftGestureHost == null || this.mTopGestureHost == null || this.mRightGestureHost == null || this.mBottomGestureHost == null)) {
                    Insets calculateInsets = source.calculateInsets(bounds, false);
                    if (this.mLeftGestureHost == null && calculateInsets.left > 0) {
                        this.mLeftGestureHost = windowState;
                    }
                    if (this.mTopGestureHost == null && calculateInsets.top > 0) {
                        this.mTopGestureHost = windowState;
                    }
                    if (this.mRightGestureHost == null && calculateInsets.right > 0) {
                        this.mRightGestureHost = windowState;
                    }
                    if (this.mBottomGestureHost == null && calculateInsets.bottom > 0) {
                        this.mBottomGestureHost = windowState;
                    }
                }
            }
        }
        if (canAffectSystemUiFlags) {
            int i = layoutParams.type;
            if (i >= 1 && i < 2000) {
                z = true;
            }
            boolean isSpecialAppWindow = this.mDisplayPolicyWrapper.getExtImpl().isSpecialAppWindow(z, layoutParams);
            if (this.mTopFullscreenOpaqueWindowState == null) {
                int i2 = layoutParams.flags;
                if (windowState.isDreamWindow() && (!this.mDreamingLockscreen || (windowState.isVisible() && windowState.hasDrawn()))) {
                    this.mShowingDream = true;
                    isSpecialAppWindow = true;
                }
                if (isSpecialAppWindow && windowState2 == null && layoutParams.isFullscreen() && (i2 & 1) != 0) {
                    this.mAllowLockscreenWhenOn = true;
                }
            }
            if ((isSpecialAppWindow && windowState2 == null && layoutParams.isFullscreen() && this.mDisplayPolicyWrapper.getExtImpl().canBeTopFullscreenOpqWin(windowState)) || layoutParams.type == 2031) {
                if (this.mTopFullscreenOpaqueWindowState == null && !this.mDisplayPolicyWrapper.getExtImpl().judgeWindowModeZoom(windowState) && !this.mDisplayPolicyWrapper.getExtImpl().isFlexibleTaskIgnoreSysBar(windowState)) {
                    this.mTopFullscreenOpaqueWindowState = windowState;
                    this.mDisplayPolicyWrapper.getExtImpl().onTopFullscreenOpaqueWindowUpdated(this, this.mTopFullscreenOpaqueWindowState);
                }
                if (this.mStatusBar != null) {
                    Rect rect = sTmpRect;
                    if ((rect.setIntersect(windowState.getFrame(), this.mStatusBar.getFrame()) || this.mDisplayPolicyWrapper.getExtImpl().intersectInCompactWindow(windowState, this.mStatusBar.getFrame(), rect)) && !this.mStatusBarBackgroundCheckedBounds.contains(rect) && !this.mDisplayPolicyWrapper.getExtImpl().judgeWindowModeZoom(windowState)) {
                        this.mStatusBarBackgroundWindows.add(windowState);
                        this.mStatusBarBackgroundCheckedBounds.union(rect);
                        if (!this.mStatusBarColorCheckedBounds.contains(rect)) {
                            this.mStatusBarAppearanceRegionList.add(new AppearanceRegion(windowState.mAttrs.insetsFlags.appearance & 8, new Rect(windowState.getFrame())));
                            this.mStatusBarColorCheckedBounds.union(rect);
                            addSystemBarColorApp(windowState);
                        }
                    }
                }
                if (isOverlappingWithNavBar(windowState)) {
                    if (this.mNavBarColorWindowCandidate == null) {
                        this.mNavBarColorWindowCandidate = windowState;
                        addSystemBarColorApp(windowState);
                    }
                    if (this.mNavBarBackgroundWindow == null) {
                        this.mNavBarBackgroundWindow = windowState;
                    }
                }
                ActivityRecord activityRecord = windowState.getActivityRecord();
                if (activityRecord == null || (letterboxDetails = activityRecord.mLetterboxUiController.getLetterboxDetails()) == null) {
                    return;
                }
                this.mLetterboxDetails.add(letterboxDetails);
                return;
            }
            if (windowState.isDimming() && !this.mDisplayPolicyWrapper.getExtImpl().judgeWindowModeZoom(windowState)) {
                WindowState windowState4 = this.mStatusBar;
                if (windowState4 != null && addStatusBarAppearanceRegionsForDimmingWindow(windowState.mAttrs.insetsFlags.appearance & 8, windowState4.getFrame(), windowState.getBounds(), windowState.getFrame())) {
                    addSystemBarColorApp(windowState);
                }
                if (isOverlappingWithNavBar(windowState) && this.mNavBarColorWindowCandidate == null) {
                    this.mNavBarColorWindowCandidate = windowState;
                    return;
                }
                return;
            }
            if (isSpecialAppWindow && windowState2 == null && this.mNavBarColorWindowCandidate == null && windowState.getFrame().contains(getBarContentFrameForWindow(windowState, WindowInsets.Type.navigationBars()))) {
                this.mNavBarColorWindowCandidate = windowState;
                addSystemBarColorApp(windowState);
            }
        }
    }

    private boolean addStatusBarAppearanceRegionsForDimmingWindow(int i, Rect rect, Rect rect2, Rect rect3) {
        Rect rect4 = sTmpRect;
        if (!rect4.setIntersect(rect2, rect) || this.mStatusBarColorCheckedBounds.contains(rect4)) {
            return false;
        }
        if (i != 0) {
            Rect rect5 = sTmpRect2;
            if (rect5.setIntersect(rect3, rect)) {
                this.mStatusBarAppearanceRegionList.add(new AppearanceRegion(i, new Rect(rect3)));
                if (!rect4.equals(rect5) && rect4.height() == rect5.height()) {
                    if (rect4.left != rect5.left) {
                        this.mStatusBarAppearanceRegionList.add(new AppearanceRegion(0, new Rect(rect2.left, rect2.top, rect5.left, rect2.bottom)));
                    }
                    if (rect4.right != rect5.right) {
                        this.mStatusBarAppearanceRegionList.add(new AppearanceRegion(0, new Rect(rect5.right, rect2.top, rect2.right, rect2.bottom)));
                    }
                }
                this.mStatusBarColorCheckedBounds.union(rect4);
                return true;
            }
        }
        this.mStatusBarAppearanceRegionList.add(new AppearanceRegion(0, new Rect(rect2)));
        this.mStatusBarColorCheckedBounds.union(rect4);
        return true;
    }

    private void addSystemBarColorApp(WindowState windowState) {
        ActivityRecord activityRecord = windowState.mActivityRecord;
        if (activityRecord != null) {
            this.mSystemBarColorApps.add(activityRecord);
        }
    }

    public void finishPostLayoutPolicyLw() {
        if (!this.mShowingDream) {
            this.mDreamingLockscreen = this.mService.mPolicy.isKeyguardShowingAndNotOccluded();
        }
        updateSystemBarAttributes();
        boolean z = this.mShowingDream;
        if (z != this.mLastShowingDream) {
            this.mLastShowingDream = z;
            this.mDisplayContent.notifyKeyguardFlagsChanged();
        }
        this.mService.mPolicy.setAllowLockscreenWhenOn(getDisplayId(), this.mAllowLockscreenWhenOn);
    }

    private void applyKeyguardPolicy(WindowState windowState, WindowState windowState2) {
        if (windowState.canBeHiddenByKeyguard()) {
            boolean shouldBeHiddenByKeyguard = shouldBeHiddenByKeyguard(windowState, windowState2);
            if (windowState.mIsImWindow) {
                this.mDisplayContent.getInsetsStateController().getImeSourceProvider().setFrozen(shouldBeHiddenByKeyguard);
            }
            if (shouldBeHiddenByKeyguard) {
                windowState.hide(false, true);
            } else {
                windowState.show(false, true);
            }
        }
    }

    private boolean shouldBeHiddenByKeyguard(WindowState windowState, WindowState windowState2) {
        boolean z = false;
        if (windowState.mIsImWindow && (this.mDisplayContent.isAodShowing() || (this.mDisplayContent.isDefaultDisplay && !this.mWindowManagerDrawComplete))) {
            return true;
        }
        if (!this.mDisplayContent.isDefaultDisplay || !isKeyguardShowing()) {
            return false;
        }
        if (windowState2 != null && windowState2.isVisible() && windowState.mIsImWindow && (windowState2.canShowWhenLocked() || !windowState2.canBeHiddenByKeyguard())) {
            return false;
        }
        if (isKeyguardOccluded() && (windowState.canShowWhenLocked() || (windowState.mAttrs.privateFlags & 256) != 0)) {
            z = true;
        }
        return !z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean topAppHidesSystemBar(int i) {
        if (this.mDisplayPolicyWrapper.getExtImpl().isSplitTaskVisible(this.mDisplayContent)) {
            return (i == WindowInsets.Type.navigationBars() && this.mTopFullscreenOpaqueWindowState == null) ? false : true;
        }
        if (this.mTopFullscreenOpaqueWindowState == null || this.mForceShowSystemBars) {
            return false;
        }
        if (this.mDisplayPolicyWrapper.getExtImpl().updateSpecialSystemBar(this.mTopFullscreenOpaqueWindowState.getAttrs())) {
            return this.mTopIsFullscreen;
        }
        return !this.mTopFullscreenOpaqueWindowState.isRequestedVisible(i);
    }

    public void switchUser() {
        updateCurrentUserResources();
        updateForceShowNavBarSettings();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onOverlayChanged() {
        updateCurrentUserResources();
        this.mDisplayContent.updateDisplayInfo();
        onConfigurationChanged();
        if (ViewRootImpl.CLIENT_TRANSIENT) {
            return;
        }
        this.mSystemGestures.onConfigurationChanged();
    }

    public void onConfigurationChanged() {
        Resources currentUserResources = getCurrentUserResources();
        this.mNavBarOpacityMode = currentUserResources.getInteger(R.integer.config_storageManagerDaystoRetainDefault);
        this.mLeftGestureInset = this.mGestureNavigationSettingsObserver.getLeftSensitivity(currentUserResources);
        this.mRightGestureInset = this.mGestureNavigationSettingsObserver.getRightSensitivity(currentUserResources);
        this.mNavigationBarAlwaysShowOnSideGesture = currentUserResources.getBoolean(17891763);
        this.mRemoteInsetsControllerControlsSystemBars = currentUserResources.getBoolean(R.bool.action_bar_embed_tabs);
        updateConfigurationAndScreenSizeDependentBehaviors();
        boolean z = currentUserResources.getBoolean(R.bool.config_batterymeterDualTone);
        if (this.mShouldAttachNavBarToAppDuringTransition != z) {
            this.mShouldAttachNavBarToAppDuringTransition = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateConfigurationAndScreenSizeDependentBehaviors() {
        Resources currentUserResources = getCurrentUserResources();
        DisplayContent displayContent = this.mDisplayContent;
        this.mNavigationBarCanMove = displayContent.mBaseDisplayWidth != displayContent.mBaseDisplayHeight && currentUserResources.getBoolean(17891764);
        if (this.mDisplayContent.getDisplayRotation() == null) {
            Slog.w(TAG, "DisplayRotation is null of display:" + this.mDisplayContent);
            return;
        }
        this.mDisplayContent.getDisplayRotation().updateUserDependentConfiguration(currentUserResources);
    }

    private void updateCurrentUserResources() {
        int currentUserId = this.mService.mAmInternal.getCurrentUserId();
        Context systemUiContext = getSystemUiContext();
        if (currentUserId == 0) {
            this.mCurrentUserResources = systemUiContext.getResources();
        } else {
            LoadedApk packageInfo = ActivityThread.currentActivityThread().getPackageInfo(systemUiContext.getPackageName(), (CompatibilityInfo) null, 0, currentUserId);
            this.mCurrentUserResources = ResourcesManager.getInstance().getResources(systemUiContext.getWindowContextToken(), packageInfo.getResDir(), (String[]) null, packageInfo.getOverlayDirs(), packageInfo.getOverlayPaths(), packageInfo.getApplicationInfo().sharedLibraryFiles, Integer.valueOf(this.mDisplayContent.getDisplayId()), (Configuration) null, systemUiContext.getResources().getCompatibilityInfo(), (ClassLoader) null, (List) null);
        }
    }

    @VisibleForTesting
    Resources getCurrentUserResources() {
        if (this.mCurrentUserResources == null) {
            updateCurrentUserResources();
        }
        return this.mCurrentUserResources;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public Context getContext() {
        return this.mContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Context getSystemUiContext() {
        return this.mUiContext;
    }

    @VisibleForTesting
    void setCanSystemBarsBeShownByUser(boolean z) {
        this.mCanSystemBarsBeShownByUser = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyDisplayReady() {
        this.mHandler.post(new Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPolicy.this.lambda$notifyDisplayReady$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyDisplayReady$3() {
        int displayId = getDisplayId();
        StatusBarManagerInternal statusBarManagerInternal = getStatusBarManagerInternal();
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.onDisplayReady(displayId);
        }
        WallpaperManagerInternal wallpaperManagerInternal = (WallpaperManagerInternal) LocalServices.getService(WallpaperManagerInternal.class);
        if (wallpaperManagerInternal != null) {
            wallpaperManagerInternal.onDisplayReady(displayId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNavigationBarFrameHeight(int i) {
        WindowState windowState = this.mNavigationBar;
        if (windowState == null) {
            return 0;
        }
        return windowState.mAttrs.forRotation(i).height;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getWindowCornerRadius() {
        if (this.mDisplayContent.getDisplay().getType() == 1) {
            return ScreenDecorationsUtils.getWindowCornerRadius(this.mContext);
        }
        return 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isShowingDreamLw() {
        return this.mShowingDream;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class DecorInsets {
        private final DisplayContent mDisplayContent;
        private final Info[] mInfoForRotation;
        final Info mTmpInfo = new Info();
        static final int DECOR_TYPES = WindowInsets.Type.displayCutout() | WindowInsets.Type.navigationBars();
        static final int CONFIG_TYPES = WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars();

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public static class Info {
            int mLastInsetsSourceCount;
            final Rect mNonDecorInsets = new Rect();
            final Rect mConfigInsets = new Rect();
            final Rect mNonDecorFrame = new Rect();
            final Rect mConfigFrame = new Rect();
            private boolean mNeedUpdate = true;

            Info() {
            }

            void update(DisplayContent displayContent, int i, int i2, int i3) {
                DisplayFrames displayFrames = new DisplayFrames();
                displayContent.updateDisplayFrames(displayFrames, i, i2, i3);
                displayContent.getDisplayPolicy().simulateLayoutDisplay(displayFrames);
                InsetsState insetsState = displayFrames.mInsetsState;
                Rect displayFrame = insetsState.getDisplayFrame();
                Insets calculateInsets = insetsState.calculateInsets(displayFrame, DecorInsets.DECOR_TYPES, true);
                Insets calculateInsets2 = insetsState.calculateInsets(displayFrame, WindowInsets.Type.statusBars(), true);
                this.mNonDecorInsets.set(calculateInsets.left, calculateInsets.top, calculateInsets.right, calculateInsets.bottom);
                this.mConfigInsets.set(Math.max(calculateInsets2.left, calculateInsets.left), Math.max(calculateInsets2.top, calculateInsets.top), Math.max(calculateInsets2.right, calculateInsets.right), Math.max(calculateInsets2.bottom, calculateInsets.bottom));
                this.mNonDecorFrame.set(displayFrame);
                this.mNonDecorFrame.inset(this.mNonDecorInsets);
                this.mConfigFrame.set(displayFrame);
                this.mConfigFrame.inset(this.mConfigInsets);
                this.mLastInsetsSourceCount = displayContent.getDisplayPolicy().mInsetsSourceWindowsExceptIme.size();
                this.mNeedUpdate = false;
            }

            void set(Info info) {
                this.mNonDecorInsets.set(info.mNonDecorInsets);
                this.mConfigInsets.set(info.mConfigInsets);
                this.mNonDecorFrame.set(info.mNonDecorFrame);
                this.mConfigFrame.set(info.mConfigFrame);
                this.mLastInsetsSourceCount = info.mLastInsetsSourceCount;
                this.mNeedUpdate = false;
            }

            public String toString() {
                StringBuilder sb = new StringBuilder(32);
                return "{nonDecorInsets=" + this.mNonDecorInsets.toShortString(sb) + ", configInsets=" + this.mConfigInsets.toShortString(sb) + ", nonDecorFrame=" + this.mNonDecorFrame.toShortString(sb) + ", configFrame=" + this.mConfigFrame.toShortString(sb) + '}';
            }
        }

        DecorInsets(DisplayContent displayContent) {
            Info[] infoArr = new Info[4];
            this.mInfoForRotation = infoArr;
            this.mDisplayContent = displayContent;
            for (int length = infoArr.length - 1; length >= 0; length--) {
                this.mInfoForRotation[length] = new Info();
            }
        }

        Info get(int i, int i2, int i3) {
            Info info = this.mInfoForRotation[i];
            if (info.mNeedUpdate) {
                info.update(this.mDisplayContent, i, i2, i3);
            }
            return info;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void invalidate() {
            for (Info info : this.mInfoForRotation) {
                info.mNeedUpdate = true;
            }
        }

        void setTo(DecorInsets decorInsets) {
            for (int length = this.mInfoForRotation.length - 1; length >= 0; length--) {
                this.mInfoForRotation[length].set(decorInsets.mInfoForRotation[length]);
            }
        }

        void dump(String str, PrintWriter printWriter) {
            int i = 0;
            while (true) {
                Info[] infoArr = this.mInfoForRotation;
                if (i >= infoArr.length) {
                    return;
                }
                printWriter.println(str + Surface.rotationToString(i) + "=" + infoArr[i]);
                i++;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public static class Cache {
            static final int ID_UPDATING_CONFIG = -1;
            boolean mActive;
            final DecorInsets mDecorInsets;
            int mPreserveId;

            Cache(DisplayContent displayContent) {
                this.mDecorInsets = new DecorInsets(displayContent);
            }

            boolean canPreserve() {
                return this.mPreserveId == -1 || this.mDecorInsets.mDisplayContent.mTransitionController.inTransition(this.mPreserveId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateDecorInsetsInfo() {
        if (shouldKeepCurrentDecorInsets()) {
            return false;
        }
        DisplayContent displayContent = this.mDisplayContent;
        DisplayFrames displayFrames = displayContent.mDisplayFrames;
        int i = displayFrames.mRotation;
        int i2 = displayFrames.mWidth;
        int i3 = displayFrames.mHeight;
        DecorInsets.Info info = this.mDecorInsets.mTmpInfo;
        info.update(displayContent, i, i2, i3);
        DecorInsets.Info decorInsetsInfo = getDecorInsetsInfo(i, i2, i3);
        if (info.mConfigFrame.equals(decorInsetsInfo.mConfigFrame)) {
            if (info.mLastInsetsSourceCount != decorInsetsInfo.mLastInsetsSourceCount) {
                for (int length = this.mDecorInsets.mInfoForRotation.length - 1; length >= 0; length--) {
                    if (length != i) {
                        boolean z = (length + i) % 2 == 1;
                        this.mDecorInsets.mInfoForRotation[length].update(this.mDisplayContent, length, z ? i3 : i2, z ? i2 : i3);
                    }
                }
                this.mDecorInsets.mInfoForRotation[i].set(info);
            }
            return false;
        }
        DecorInsets.Cache cache = this.mCachedDecorInsets;
        if (cache != null && !cache.canPreserve() && !this.mDisplayContent.isSleeping()) {
            this.mCachedDecorInsets = null;
        }
        this.mDecorInsets.invalidate();
        this.mDecorInsets.mInfoForRotation[i].set(info);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DecorInsets.Info getDecorInsetsInfo(int i, int i2, int i3) {
        return this.mDecorInsets.get(i, i2, i3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldKeepCurrentDecorInsets() {
        DecorInsets.Cache cache = this.mCachedDecorInsets;
        return cache != null && cache.mActive && cache.canPreserve();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void physicalDisplayChanged() {
        if (USE_CACHED_INSETS_FOR_DISPLAY_SWITCH) {
            updateCachedDecorInsets();
        }
    }

    @VisibleForTesting
    void updateCachedDecorInsets() {
        DecorInsets decorInsets;
        if (this.mCachedDecorInsets == null) {
            this.mCachedDecorInsets = new DecorInsets.Cache(this.mDisplayContent);
            decorInsets = null;
        } else {
            decorInsets = new DecorInsets(this.mDisplayContent);
            decorInsets.setTo(this.mCachedDecorInsets.mDecorInsets);
        }
        DecorInsets.Cache cache = this.mCachedDecorInsets;
        cache.mPreserveId = -1;
        cache.mDecorInsets.setTo(this.mDecorInsets);
        if (decorInsets != null) {
            this.mDecorInsets.setTo(decorInsets);
            this.mCachedDecorInsets.mActive = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void physicalDisplayUpdated() {
        if (this.mCachedDecorInsets == null) {
            return;
        }
        if (!this.mDisplayContent.mTransitionController.isCollecting()) {
            this.mCachedDecorInsets = null;
            return;
        }
        this.mCachedDecorInsets.mPreserveId = this.mDisplayContent.mTransitionController.getCollectingTransitionId();
        this.mDisplayContent.mTransitionController.mStateValidators.add(new Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPolicy.this.lambda$physicalDisplayUpdated$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$physicalDisplayUpdated$4() {
        if (this.mDisplayContent.isSleeping() || !updateDecorInsetsInfo()) {
            return;
        }
        Slog.d(TAG, "Insets changed after display switch transition");
        this.mDisplayContent.sendNewConfiguration();
    }

    int navigationBarPosition(int i) {
        if (this.mNavigationBar == null) {
            return -1;
        }
        if (this.mDisplayPolicyWrapper.getExtImpl().adjustNavigationBarToBottom(this.mDisplayContent)) {
            return 4;
        }
        int i2 = this.mNavigationBar.mAttrs.forRotation(i).gravity;
        if (i2 != 3) {
            return i2 != 5 ? 4 : 2;
        }
        return 1;
    }

    public void focusChangedLw(WindowState windowState, WindowState windowState2) {
        this.mFocusedWindow = windowState2;
        this.mLastFocusedWindow = windowState;
        if (this.mDisplayContent.isDefaultDisplay) {
            this.mService.mPolicy.onDefaultDisplayFocusChangedLw(windowState2);
        }
        updateSystemBarAttributes();
    }

    @VisibleForTesting
    void requestTransientBars(WindowState windowState, boolean z) {
        if (ViewRootImpl.CLIENT_TRANSIENT) {
            return;
        }
        if (windowState == null || !this.mService.mPolicy.isUserSetupComplete()) {
            Slog.d(TAG, "request show transient bar, swipeTarget = " + windowState);
            return;
        }
        if (!this.mCanSystemBarsBeShownByUser) {
            Slog.d(TAG, "Remote insets controller disallows showing system bars - ignoring request");
            return;
        }
        if ((windowState == this.mNavigationBar || windowState == this.mDisplayPolicyWrapper.getExtImpl().getTaskBar()) && this.mDisplayPolicyWrapper.getExtImpl().isDisableExpendNavBar()) {
            Slog.d(TAG, "NAVIGATIONBAR GESTURE Mode Not showing Navigation bar");
            return;
        }
        if (windowState == this.mStatusBar && this.mDisplayPolicyWrapper.getExtImpl().isDisableExpendStatusBar()) {
            Slog.d(TAG, "NAVIGATIONBAR GESTURE Mode Not showing status bar");
            return;
        }
        if (this.mZenModeManagerExt.isZenModeOn()) {
            Slog.d(TAG, "Zen mode, not showing bar");
            return;
        }
        InsetsSourceProvider controllableInsetProvider = windowState.getControllableInsetProvider();
        InsetsControlTarget controlTarget = controllableInsetProvider != null ? controllableInsetProvider.getControlTarget() : null;
        if (controlTarget == null || controlTarget == getNotificationShade()) {
            Slog.d(TAG, "request show transient bar, provider = " + controllableInsetProvider + " controlTarget = " + controlTarget);
            return;
        }
        WindowState window = controlTarget.getWindow();
        if (window != null && window.isActivityTypeDream()) {
            Slog.d(TAG, "request show transient bar, is activity type dream");
            return;
        }
        int statusBars = (WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars()) & controlTarget.getRequestedVisibleTypes();
        InsetsSourceProvider controllableInsetProvider2 = windowState.getControllableInsetProvider();
        if (controllableInsetProvider2 != null && controllableInsetProvider2.getSource().getType() == WindowInsets.Type.navigationBars() && (WindowInsets.Type.navigationBars() & statusBars) != 0) {
            controlTarget.showInsets(WindowInsets.Type.navigationBars(), false, null);
            Slog.d(TAG, "request show transient bar, navigationBar already visible");
            return;
        }
        if (controlTarget.canShowTransient()) {
            this.mDisplayContent.getInsetsPolicy().showTransient(SHOW_TYPES_FOR_SWIPE, z);
            controlTarget.showInsets(statusBars, false, null);
        } else {
            controlTarget.showInsets(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars(), false, null);
            if (windowState == this.mStatusBar && this.mNotificationShade != null && this.mNotificationShade.mViewVisibility != 0 && !this.mStatusBar.transferTouch()) {
                Slog.i(TAG, "Could not transfer touch to the status bar");
            }
        }
        this.mImmersiveModeConfirmation.confirmCurrentPrompt();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isKeyguardShowing() {
        return this.mService.mPolicy.isKeyguardShowing();
    }

    private boolean isKeyguardOccluded() {
        return this.mService.mPolicy.isKeyguardOccluded();
    }

    InsetsPolicy getInsetsPolicy() {
        return this.mDisplayContent.getInsetsPolicy();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addRelaunchingApp(ActivityRecord activityRecord) {
        if (!this.mSystemBarColorApps.contains(activityRecord) || activityRecord.hasStartingWindow()) {
            return;
        }
        this.mRelaunchingSystemBarColorApps.add(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeRelaunchingApp(ActivityRecord activityRecord) {
        if (this.mRelaunchingSystemBarColorApps.remove(activityRecord) && this.mRelaunchingSystemBarColorApps.isEmpty()) {
            updateSystemBarAttributes();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetSystemBarAttributes() {
        this.mLastDisableFlags = 0;
        updateSystemBarAttributes();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateSystemBarAttributes() {
        WindowState windowState;
        if (this.mFocusedWindow == null && (windowState = this.mTopFullscreenOpaqueWindowState) != null && (windowState.mAttrs.flags & 8) != 0) {
            this.mDisplayPolicyWrapper.getExtImpl().shouldNoFocusWindowUpdateSystemBarAttributes(this.mTopFullscreenOpaqueWindowState);
        }
        WindowState windowState2 = this.mFocusedWindow;
        if (windowState2 == null) {
            windowState2 = this.mTopFullscreenOpaqueWindowState;
        }
        if (windowState2 == null) {
            return;
        }
        if (windowState2.getAttrs().token == this.mImmersiveModeConfirmation.getWindowToken()) {
            if (this.mNotificationShade != null && this.mNotificationShade.canReceiveKeys()) {
                windowState2 = this.mNotificationShade;
            } else {
                WindowState windowState3 = this.mLastFocusedWindow;
                if (windowState3 != null && windowState3.canReceiveKeys()) {
                    windowState2 = this.mLastFocusedWindow;
                } else {
                    windowState2 = this.mTopFullscreenOpaqueWindowState;
                }
            }
            if (windowState2 == null) {
                return;
            }
        }
        if (this.mDisplayPolicyWrapper.getExtImpl().skipSystemUiVisibility(windowState2.getAttrs()) || this.mDisplayPolicyWrapper.getExtImpl().isDreamWindow(windowState2.isDreamWindow())) {
            return;
        }
        this.mSystemUiControllingWindow = windowState2;
        final int displayId = getDisplayId();
        final int disableFlags = windowState2.getDisableFlags();
        int updateSystemBarsLw = updateSystemBarsLw(windowState2, disableFlags);
        if (this.mRelaunchingSystemBarColorApps.isEmpty()) {
            WindowState chooseNavigationColorWindowLw = chooseNavigationColorWindowLw(this.mNavBarColorWindowCandidate, this.mDisplayContent.mInputMethodWindow, this.mNavigationBarPosition);
            boolean z = true;
            boolean z2 = chooseNavigationColorWindowLw != null && chooseNavigationColorWindowLw == this.mDisplayContent.mInputMethodWindow;
            final int updateLightNavigationBarLw = updateLightNavigationBarLw(windowState2.mAttrs.insetsFlags.appearance, chooseNavigationColorWindowLw) | updateSystemBarsLw;
            final int i = (topAppHidesSystemBar(WindowInsets.Type.navigationBars()) ? this.mTopFullscreenOpaqueWindowState : windowState2).mAttrs.insetsFlags.behavior;
            final String str = windowState2.mAttrs.packageName;
            boolean z3 = (windowState2.isRequestedVisible(WindowInsets.Type.statusBars()) && windowState2.isRequestedVisible(WindowInsets.Type.navigationBars())) ? false : true;
            final AppearanceRegion[] appearanceRegionArr = new AppearanceRegion[this.mStatusBarAppearanceRegionList.size()];
            this.mStatusBarAppearanceRegionList.toArray(appearanceRegionArr);
            if (this.mLastDisableFlags != disableFlags) {
                this.mLastDisableFlags = disableFlags;
                final String windowState4 = windowState2.toString();
                callStatusBarSafely(new Consumer() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((StatusBarManagerInternal) obj).setDisableFlags(displayId, disableFlags, windowState4);
                    }
                });
            }
            final int requestedVisibleTypes = windowState2.getRequestedVisibleTypes();
            final LetterboxDetails[] letterboxDetailsArr = new LetterboxDetails[this.mLetterboxDetails.size()];
            this.mLetterboxDetails.toArray(letterboxDetailsArr);
            if (this.mLastAppearance == updateLightNavigationBarLw && this.mLastBehavior == i && this.mLastRequestedVisibleTypes == requestedVisibleTypes && Objects.equals(this.mFocusedApp, str) && this.mLastFocusIsFullscreen == z3 && Arrays.equals(this.mLastStatusBarAppearanceRegions, appearanceRegionArr) && Arrays.equals(this.mLastLetterboxDetails, letterboxDetailsArr)) {
                return;
            }
            if (this.mDisplayContent.isDefaultDisplay && this.mLastFocusIsFullscreen != z3 && ((this.mLastAppearance ^ updateLightNavigationBarLw) & 4) != 0) {
                InputManagerService inputManagerService = this.mService.mInputManager;
                if (!z3 && (updateLightNavigationBarLw & 4) == 0) {
                    z = false;
                }
                inputManagerService.setSystemUiLightsOut(z);
            }
            this.mLastAppearance = updateLightNavigationBarLw;
            this.mLastBehavior = i;
            this.mLastRequestedVisibleTypes = requestedVisibleTypes;
            this.mFocusedApp = str;
            this.mLastFocusIsFullscreen = z3;
            this.mLastStatusBarAppearanceRegions = appearanceRegionArr;
            this.mLastLetterboxDetails = letterboxDetailsArr;
            final boolean z4 = z2;
            callStatusBarSafely(new Consumer() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((StatusBarManagerInternal) obj).onSystemBarAttributesChanged(displayId, updateLightNavigationBarLw, appearanceRegionArr, z4, i, requestedVisibleTypes, str, letterboxDetailsArr);
                }
            });
            this.mDisplayPolicyWrapper.getExtImpl().updateTaskBarAppearanceIfNeed(this.mTopFullscreenOpaqueWindowState);
        }
    }

    private void callStatusBarSafely(final Consumer<StatusBarManagerInternal> consumer) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                DisplayPolicy.this.lambda$callStatusBarSafely$7(consumer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$callStatusBarSafely$7(Consumer consumer) {
        StatusBarManagerInternal statusBarManagerInternal = getStatusBarManagerInternal();
        if (statusBarManagerInternal != null) {
            consumer.accept(statusBarManagerInternal);
        }
    }

    @VisibleForTesting
    static WindowState chooseNavigationColorWindowLw(WindowState windowState, WindowState windowState2, int i) {
        return !(windowState2 != null && windowState2.isVisible() && i == 4 && (windowState2.mAttrs.flags & ChargerErrorCode.ERR_FILE_FAILURE_ACCESS) != 0) ? windowState : (windowState == null || !windowState.isDimming() || WindowManager.LayoutParams.mayUseInputMethod(windowState.mAttrs.flags)) ? windowState2 : windowState;
    }

    @VisibleForTesting
    int updateLightNavigationBarLw(int i, WindowState windowState) {
        return (windowState == null || !isLightBarAllowed(windowState, WindowInsets.Type.navigationBars())) ? i & (-17) : (i & (-17)) | (windowState.mAttrs.insetsFlags.appearance & 16);
    }

    private int updateSystemBarsLw(WindowState windowState, int i) {
        StatusBarManagerInternal statusBarManagerInternal;
        TaskDisplayArea defaultTaskDisplayArea = this.mDisplayContent.getDefaultTaskDisplayArea();
        boolean z = false;
        boolean z2 = defaultTaskDisplayArea.getRootTask(new Predicate() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$updateSystemBarsLw$8;
                lambda$updateSystemBarsLw$8 = DisplayPolicy.this.lambda$updateSystemBarsLw$8((Task) obj);
                return lambda$updateSystemBarsLw$8;
            }
        }) != null;
        boolean isRootTaskVisible = defaultTaskDisplayArea.isRootTaskVisible(5);
        boolean z3 = z2 || isRootTaskVisible;
        this.mForceShowSystemBars = z3;
        this.mForceConsumeSystemBars = z3 || getInsetsPolicy().remoteInsetsControllerControlsSystemBars(windowState) || getInsetsPolicy().forcesShowingNavigationBars(windowState);
        this.mDisplayContent.getInsetsPolicy().updateBarControlTarget(windowState);
        boolean z4 = topAppHidesSystemBar(WindowInsets.Type.statusBars());
        if (getStatusBar() != null && (statusBarManagerInternal = getStatusBarManagerInternal()) != null) {
            statusBarManagerInternal.setTopAppHidesStatusBar(z4);
        }
        this.mTopIsFullscreen = z4 && (this.mNotificationShade == null || !this.mNotificationShade.isVisible());
        int configureNavBarOpacity = configureNavBarOpacity(configureStatusBarOpacity(3), z2, isRootTaskVisible);
        if (this.mDisplayPolicyWrapper.getExtImpl().opaqueNavBar(this.mTopFullscreenOpaqueWindowState)) {
            configureNavBarOpacity |= 2;
        }
        boolean z5 = this.mIsImmersiveMode;
        boolean isImmersiveMode = isImmersiveMode(windowState);
        if (z5 != isImmersiveMode) {
            this.mIsImmersiveMode = isImmersiveMode;
            RootDisplayArea rootDisplayArea = windowState.getRootDisplayArea();
            this.mImmersiveModeConfirmation.immersiveModeChangedLw(rootDisplayArea == null ? -1 : rootDisplayArea.mFeatureId, isImmersiveMode, this.mService.mPolicy.isUserSetupComplete(), isNavBarEmpty(i));
        }
        boolean z6 = !windowState.isRequestedVisible(WindowInsets.Type.navigationBars());
        long uptimeMillis = SystemClock.uptimeMillis();
        long j = this.mPendingPanicGestureUptime;
        if (j != 0 && uptimeMillis - j <= PANIC_GESTURE_EXPIRATION) {
            z = true;
        }
        DisplayPolicy displayPolicy = this.mService.getDefaultDisplayContentLocked().getDisplayPolicy();
        if (z && z6 && isImmersiveMode && displayPolicy.isKeyguardDrawComplete()) {
            this.mPendingPanicGestureUptime = 0L;
            if (!isNavBarEmpty(i)) {
                this.mDisplayContent.getInsetsPolicy().showTransient(SHOW_TYPES_FOR_PANIC, true);
            }
        }
        return configureNavBarOpacity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateSystemBarsLw$8(Task task) {
        return task.isVisible() && !task.getWrapper().getExtImpl().getLaunchedFromMultiSearch() && this.mDisplayPolicyWrapper.getExtImpl().isNeedForceShowSystemBarsWhenSplit() && task.getTopLeafTask().getWindowingMode() == 6 && task.getTopLeafTask().getAdjacentTask() != null;
    }

    private static boolean isLightBarAllowed(WindowState windowState, int i) {
        if (windowState == null) {
            return false;
        }
        return intersectsAnyInsets(windowState.getFrame(), windowState.getInsetsState(), i);
    }

    private Rect getBarContentFrameForWindow(WindowState windowState, int i) {
        DisplayFrames displayFrames = windowState.getDisplayFrames(this.mDisplayContent.mDisplayFrames);
        InsetsState insetsState = displayFrames.mInsetsState;
        Rect rect = displayFrames.mUnrestricted;
        Rect rect2 = sTmpDisplayCutoutSafe;
        Insets waterfallInsets = insetsState.getDisplayCutout().getWaterfallInsets();
        Rect rect3 = new Rect();
        Rect rect4 = sTmpRect;
        rect2.set(displayFrames.mDisplayCutoutSafe);
        for (int sourceSize = insetsState.sourceSize() - 1; sourceSize >= 0; sourceSize--) {
            InsetsSource sourceAt = insetsState.sourceAt(sourceSize);
            if (sourceAt.getType() == i) {
                if (i == WindowInsets.Type.statusBars()) {
                    rect2.set(displayFrames.mDisplayCutoutSafe);
                    Insets calculateInsets = sourceAt.calculateInsets(rect, true);
                    if (calculateInsets.left > 0) {
                        int i2 = rect.left;
                        rect2.left = Math.max(waterfallInsets.left + i2, i2);
                    } else if (calculateInsets.top > 0) {
                        int i3 = rect.top;
                        rect2.top = Math.max(waterfallInsets.top + i3, i3);
                    } else if (calculateInsets.right > 0) {
                        int i4 = rect.right;
                        rect2.right = Math.max(i4 - waterfallInsets.right, i4);
                    } else if (calculateInsets.bottom > 0) {
                        int i5 = rect.bottom;
                        rect2.bottom = Math.max(i5 - waterfallInsets.bottom, i5);
                    }
                }
                rect4.set(sourceAt.getFrame());
                rect4.intersect(rect2);
                rect3.union(rect4);
            }
        }
        return rect3;
    }

    @VisibleForTesting
    boolean isFullyTransparentAllowed(WindowState windowState, int i) {
        if (windowState == null) {
            return true;
        }
        return windowState.isFullyTransparentBarAllowed(getBarContentFrameForWindow(windowState, i));
    }

    private boolean drawsBarBackground(WindowState windowState) {
        if (windowState == null) {
            return true;
        }
        return ((windowState.getAttrs().privateFlags & 32768) != 0) || ((windowState.getAttrs().flags & ChargerErrorCode.ERR_FILE_FAILURE_ACCESS) != 0);
    }

    private int configureStatusBarOpacity(int i) {
        boolean z = true;
        boolean z2 = true;
        for (int size = this.mStatusBarBackgroundWindows.size() - 1; size >= 0; size--) {
            WindowState windowState = this.mStatusBarBackgroundWindows.get(size);
            z &= drawsBarBackground(windowState);
            z2 &= isFullyTransparentAllowed(windowState, WindowInsets.Type.statusBars());
        }
        if (z && !this.mDisplayPolicyWrapper.getExtImpl().makeStatusBarOpaque(this.mDisplayContent)) {
            i &= -2;
        }
        return !z2 ? i | 32 : i;
    }

    private int configureNavBarOpacity(int i, boolean z, boolean z2) {
        boolean drawsBarBackground = drawsBarBackground(this.mNavBarBackgroundWindow);
        int i2 = this.mNavBarOpacityMode;
        if (i2 == 2) {
            if (drawsBarBackground) {
                i = clearNavBarOpaqueFlag(i);
            }
        } else if (i2 == 0) {
            if (z || z2 || this.mDisplayPolicyWrapper.getExtImpl().isSplitTaskVisible(this.mDisplayContent)) {
                if (this.mIsFreeformWindowOverlappingWithNavBar || this.mDisplayPolicyWrapper.getExtImpl().isMinimized(this.mDisplayContent) || this.mDisplayPolicyWrapper.getExtImpl().isWaitForExitSplit()) {
                    i = clearNavBarOpaqueFlag(i);
                }
            } else if (drawsBarBackground) {
                i = clearNavBarOpaqueFlag(i);
            }
        } else if (i2 == 1 && z2) {
            i = clearNavBarOpaqueFlag(i);
        }
        return !isFullyTransparentAllowed(this.mNavBarBackgroundWindow, WindowInsets.Type.navigationBars()) ? i | 64 : i;
    }

    private boolean isImmersiveMode(WindowState windowState) {
        if (windowState == null || windowState == getNotificationShade() || windowState.isActivityTypeDream()) {
            return false;
        }
        return getInsetsPolicy().hasHiddenSources(WindowInsets.Type.navigationBars());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPowerKeyDown(boolean z) {
        if (this.mImmersiveModeConfirmation.onPowerKeyDown(z, SystemClock.elapsedRealtime(), isImmersiveMode(this.mSystemUiControllingWindow), isNavBarEmpty(this.mLastDisableFlags))) {
            this.mHandler.post(this.mHiddenNavPanic);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onVrStateChangedLw(boolean z) {
        this.mImmersiveModeConfirmation.onVrStateChangedLw(z);
    }

    public void onLockTaskStateChangedLw(int i) {
        this.mImmersiveModeConfirmation.onLockTaskModeChangedLw(i);
    }

    public void onUserActivityEventTouch() {
        if (this.mAwake) {
            return;
        }
        WindowState windowState = this.mNotificationShade;
        this.mService.mAtmService.setProcessAnimatingWhileDozing(windowState != null ? windowState.getProcess() : null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onSystemUiSettingsChanged() {
        return this.mImmersiveModeConfirmation.onSettingChanged(this.mService.mCurrentUserId);
    }

    public void takeScreenshot(int i, int i2) {
        if (this.mScreenshotHelper != null) {
            this.mScreenshotHelper.takeScreenshot(new ScreenshotRequest.Builder(i, i2).build(), this.mHandler, (Consumer) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RefreshRatePolicy getRefreshRatePolicy() {
        return this.mRefreshRatePolicy;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(String str, PrintWriter printWriter) {
        printWriter.print(str);
        printWriter.println("DisplayPolicy");
        String str2 = str + "  ";
        String str3 = str2 + "  ";
        printWriter.print(str2);
        printWriter.print("mCarDockEnablesAccelerometer=");
        printWriter.print(this.mCarDockEnablesAccelerometer);
        printWriter.print(" mDeskDockEnablesAccelerometer=");
        printWriter.println(this.mDeskDockEnablesAccelerometer);
        printWriter.print(str2);
        printWriter.print("mDockMode=");
        printWriter.print(Intent.dockStateToString(this.mDockMode));
        printWriter.print(" mLidState=");
        printWriter.println(WindowManagerPolicy.WindowManagerFuncs.lidStateToString(this.mLidState));
        printWriter.print(str2);
        printWriter.print("mAwake=");
        printWriter.print(this.mAwake);
        printWriter.print(" mScreenOnEarly=");
        printWriter.print(this.mScreenOnEarly);
        printWriter.print(" mScreenOnFully=");
        printWriter.println(this.mScreenOnFully);
        printWriter.print(str2);
        printWriter.print("mKeyguardDrawComplete=");
        printWriter.print(this.mKeyguardDrawComplete);
        printWriter.print(" mWindowManagerDrawComplete=");
        printWriter.println(this.mWindowManagerDrawComplete);
        printWriter.print(str2);
        printWriter.print("mHdmiPlugged=");
        printWriter.println(this.mHdmiPlugged);
        if (this.mLastDisableFlags != 0) {
            printWriter.print(str2);
            printWriter.print("mLastDisableFlags=0x");
            printWriter.println(Integer.toHexString(this.mLastDisableFlags));
        }
        if (this.mLastAppearance != 0) {
            printWriter.print(str2);
            printWriter.print("mLastAppearance=");
            printWriter.println(ViewDebug.flagsToString(InsetsFlags.class, "appearance", this.mLastAppearance));
        }
        if (this.mLastBehavior != 0) {
            printWriter.print(str2);
            printWriter.print("mLastBehavior=");
            printWriter.println(ViewDebug.flagsToString(InsetsFlags.class, "behavior", this.mLastBehavior));
        }
        printWriter.print(str2);
        printWriter.print("mShowingDream=");
        printWriter.print(this.mShowingDream);
        printWriter.print(" mDreamingLockscreen=");
        printWriter.println(this.mDreamingLockscreen);
        if (this.mStatusBar != null) {
            printWriter.print(str2);
            printWriter.print("mStatusBar=");
            printWriter.println(this.mStatusBar);
        }
        if (this.mNotificationShade != null) {
            printWriter.print(str2);
            printWriter.print("mExpandedPanel=");
            printWriter.println(this.mNotificationShade);
        }
        printWriter.print(str2);
        printWriter.print("isKeyguardShowing=");
        printWriter.println(isKeyguardShowing());
        if (this.mNavigationBar != null) {
            printWriter.print(str2);
            printWriter.print("mNavigationBar=");
            printWriter.println(this.mNavigationBar);
            printWriter.print(str2);
            printWriter.print("mNavBarOpacityMode=");
            printWriter.println(this.mNavBarOpacityMode);
            printWriter.print(str2);
            printWriter.print("mNavigationBarCanMove=");
            printWriter.println(this.mNavigationBarCanMove);
            printWriter.print(str2);
            printWriter.print("mNavigationBarPosition=");
            printWriter.println(this.mNavigationBarPosition);
        }
        if (this.mLeftGestureHost != null) {
            printWriter.print(str2);
            printWriter.print("mLeftGestureHost=");
            printWriter.println(this.mLeftGestureHost);
        }
        if (this.mTopGestureHost != null) {
            printWriter.print(str2);
            printWriter.print("mTopGestureHost=");
            printWriter.println(this.mTopGestureHost);
        }
        if (this.mRightGestureHost != null) {
            printWriter.print(str2);
            printWriter.print("mRightGestureHost=");
            printWriter.println(this.mRightGestureHost);
        }
        if (this.mBottomGestureHost != null) {
            printWriter.print(str2);
            printWriter.print("mBottomGestureHost=");
            printWriter.println(this.mBottomGestureHost);
        }
        if (this.mFocusedWindow != null) {
            printWriter.print(str2);
            printWriter.print("mFocusedWindow=");
            printWriter.println(this.mFocusedWindow);
        }
        if (this.mTopFullscreenOpaqueWindowState != null) {
            printWriter.print(str2);
            printWriter.print("mTopFullscreenOpaqueWindowState=");
            printWriter.println(this.mTopFullscreenOpaqueWindowState);
        }
        if (!this.mSystemBarColorApps.isEmpty()) {
            printWriter.print(str2);
            printWriter.print("mSystemBarColorApps=");
            printWriter.println(this.mSystemBarColorApps);
        }
        if (!this.mRelaunchingSystemBarColorApps.isEmpty()) {
            printWriter.print(str2);
            printWriter.print("mRelaunchingSystemBarColorApps=");
            printWriter.println(this.mRelaunchingSystemBarColorApps);
        }
        if (this.mNavBarColorWindowCandidate != null) {
            printWriter.print(str2);
            printWriter.print("mNavBarColorWindowCandidate=");
            printWriter.println(this.mNavBarColorWindowCandidate);
        }
        if (this.mNavBarBackgroundWindow != null) {
            printWriter.print(str2);
            printWriter.print("mNavBarBackgroundWindow=");
            printWriter.println(this.mNavBarBackgroundWindow);
        }
        if (this.mLastStatusBarAppearanceRegions != null) {
            printWriter.print(str2);
            printWriter.println("mLastStatusBarAppearanceRegions=");
            for (int length = this.mLastStatusBarAppearanceRegions.length - 1; length >= 0; length--) {
                printWriter.print(str3);
                printWriter.println(this.mLastStatusBarAppearanceRegions[length]);
            }
        }
        if (this.mLastLetterboxDetails != null) {
            printWriter.print(str2);
            printWriter.println("mLastLetterboxDetails=");
            for (int length2 = this.mLastLetterboxDetails.length - 1; length2 >= 0; length2--) {
                printWriter.print(str3);
                printWriter.println(this.mLastLetterboxDetails[length2]);
            }
        }
        if (!this.mStatusBarBackgroundWindows.isEmpty()) {
            printWriter.print(str2);
            printWriter.println("mStatusBarBackgroundWindows=");
            for (int size = this.mStatusBarBackgroundWindows.size() - 1; size >= 0; size--) {
                WindowState windowState = this.mStatusBarBackgroundWindows.get(size);
                printWriter.print(str3);
                printWriter.println(windowState);
            }
        }
        printWriter.print(str2);
        printWriter.print("mTopIsFullscreen=");
        printWriter.println(this.mTopIsFullscreen);
        printWriter.print(str2);
        printWriter.print("mForceShowNavigationBarEnabled=");
        printWriter.print(this.mForceShowNavigationBarEnabled);
        printWriter.print(" mAllowLockscreenWhenOn=");
        printWriter.println(this.mAllowLockscreenWhenOn);
        printWriter.print(str2);
        printWriter.print("mRemoteInsetsControllerControlsSystemBars=");
        printWriter.println(this.mRemoteInsetsControllerControlsSystemBars);
        printWriter.print(str2);
        printWriter.println("mDecorInsetsInfo:");
        this.mDecorInsets.dump(str3, printWriter);
        if (this.mCachedDecorInsets != null) {
            printWriter.print(str2);
            printWriter.println("mCachedDecorInsets:");
            this.mCachedDecorInsets.mDecorInsets.dump(str3, printWriter);
        }
        if (!ViewRootImpl.CLIENT_TRANSIENT) {
            this.mSystemGestures.dump(printWriter, str2);
        }
        if (IS_HIGHTEMP_VERSION || IS_AGING_VERSION) {
            return;
        }
        printWriter.print(str2);
        printWriter.println("Looper state:");
        this.mHandler.getLooper().dump(new PrintWriterPrinter(printWriter), str2 + "  ");
    }

    private boolean supportsPointerLocation() {
        if (this.mDisplayContent.getWrapper().getExtImpl().isTwoScreenShown()) {
            Slog.d(TAG, "supportsPointerLocation false as Two Screen Shown");
            return false;
        }
        DisplayContent displayContent = this.mDisplayContent;
        return displayContent.isDefaultDisplay || !displayContent.isPrivate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPointerLocationEnabled(boolean z) {
        if (supportsPointerLocation()) {
            this.mHandler.sendEmptyMessage(z ? 4 : 5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enablePointerLocation() {
        if (this.mPointerLocationView != null) {
            return;
        }
        if (this.mDisplayContent.getWrapper().getExtImpl().interceptPointerLocationEnable(this.mDisplayContent)) {
            Slog.d(TAG, "interceptPointerLocationEnable");
            return;
        }
        PointerLocationView pointerLocationView = new PointerLocationView(this.mContext);
        this.mPointerLocationView = pointerLocationView;
        pointerLocationView.setPrintCoords(false);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = 2015;
        layoutParams.flags = 280;
        layoutParams.privateFlags |= 16;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.layoutInDisplayCutoutMode = 3;
        if (ActivityManager.isHighEndGfx()) {
            layoutParams.flags |= 16777216;
            layoutParams.privateFlags |= 2;
        }
        layoutParams.format = -3;
        layoutParams.setTitle("PointerLocation - display " + getDisplayId());
        layoutParams.inputFeatures = layoutParams.inputFeatures | 1;
        ((WindowManager) this.mContext.getSystemService(WindowManager.class)).addView(this.mPointerLocationView, layoutParams);
        this.mDisplayContent.registerPointerEventListener(this.mPointerLocationView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disablePointerLocation() {
        if (this.mPointerLocationView == null) {
            return;
        }
        if (!this.mDisplayContent.isRemoved()) {
            this.mDisplayContent.unregisterPointerEventListener(this.mPointerLocationView);
        }
        ((WindowManager) this.mContext.getSystemService(WindowManager.class)).removeView(this.mPointerLocationView);
        this.mPointerLocationView = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isWindowExcludedFromContent(WindowState windowState) {
        PointerLocationView pointerLocationView;
        if (windowState != null && (pointerLocationView = this.mPointerLocationView) != null) {
            try {
                return windowState.mClient == pointerLocationView.getWindowToken();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void release() {
        this.mDisplayContent.mTransitionController.unregisterLegacyListener(this.mAppTransitionListener);
        Handler handler = this.mHandler;
        final GestureNavigationSettingsObserver gestureNavigationSettingsObserver = this.mGestureNavigationSettingsObserver;
        Objects.requireNonNull(gestureNavigationSettingsObserver);
        handler.post(new Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                gestureNavigationSettingsObserver.unregister();
            }
        });
        Handler handler2 = this.mHandler;
        final ForceShowNavBarSettingsObserver forceShowNavBarSettingsObserver = this.mForceShowNavBarSettingsObserver;
        Objects.requireNonNull(forceShowNavBarSettingsObserver);
        handler2.post(new Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                forceShowNavBarSettingsObserver.unregister();
            }
        });
        this.mImmersiveModeConfirmation.release();
        if (this.mService.mPointerLocationEnabled) {
            setPointerLocationEnabled(false);
        }
    }

    @VisibleForTesting
    static boolean isOverlappingWithNavBar(WindowState windowState) {
        if (windowState.isVisible()) {
            return intersectsAnyInsets(windowState.isDimming() ? windowState.getBounds() : windowState.getFrame(), windowState.getInsetsState(), WindowInsets.Type.navigationBars());
        }
        return false;
    }

    private static boolean intersectsAnyInsets(Rect rect, InsetsState insetsState, int i) {
        for (int sourceSize = insetsState.sourceSize() - 1; sourceSize >= 0; sourceSize--) {
            InsetsSource sourceAt = insetsState.sourceAt(sourceSize);
            if ((sourceAt.getType() & i) != 0 && sourceAt.isVisible() && Rect.intersects(rect, sourceAt.getFrame())) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldAttachNavBarToAppDuringTransition() {
        return this.mShouldAttachNavBarToAppDuringTransition && this.mNavigationBar != null;
    }

    public IDisplayPolicyWrapper getWrapper() {
        return this.mDisplayPolicyWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class DisplayPolicyWrapper implements IDisplayPolicyWrapper {
        private IDisplayPolicyExt mDisplayPolicyExt;
        private IDisplayPolicySocExt mDisplayPolicySocExt;

        private DisplayPolicyWrapper() {
            this.mDisplayPolicyExt = (IDisplayPolicyExt) ExtLoader.type(IDisplayPolicyExt.class).base(DisplayPolicy.this).create();
            this.mDisplayPolicySocExt = (IDisplayPolicySocExt) ExtLoader.type(IDisplayPolicySocExt.class).base(DisplayPolicy.this).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IDisplayPolicySocExt getSocExtImpl() {
            return this.mDisplayPolicySocExt;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public IDisplayPolicyExt getExtImpl() {
            return this.mDisplayPolicyExt;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public ScreenshotHelper getScreenshotHelper() {
            return DisplayPolicy.this.mScreenshotHelper;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public Object getServiceAcquireLock() {
            return DisplayPolicy.this.mServiceAcquireLock;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public Handler getHandler() {
            return DisplayPolicy.this.mHandler;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public WindowManagerService getWindowManagerService() {
            return DisplayPolicy.this.mService;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public WindowState getFocusedWindow() {
            return DisplayPolicy.this.mFocusedWindow;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public WindowLayout getWindowLayout() {
            return DisplayPolicy.this.mWindowLayout;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public ClientWindowFrames getTmpClientFrames() {
            return DisplayPolicy.sTmpClientFrames;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public DisplayContent getDisplayContent() {
            return DisplayPolicy.this.mDisplayContent;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public void setBottomGestureAdditionalInset(int i) {
            DisplayPolicy.this.mBottomGestureAdditionalInset = i;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public int getBottomGestureAdditionalInset() {
            return DisplayPolicy.this.mBottomGestureAdditionalInset;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public int getNavBarFrameHeight(int i) {
            return DisplayPolicy.this.getNavigationBarFrameHeight(i);
        }
    }
}
