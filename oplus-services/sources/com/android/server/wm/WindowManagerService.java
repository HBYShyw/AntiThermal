package com.android.server.wm;

import android.R;
import android.animation.ValueAnimator;
import android.annotation.RequiresPermission;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.ActivityThread;
import android.app.AppOpsManager;
import android.app.IActivityManager;
import android.app.IAssistDataReceiver;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManagerInternal;
import android.content.pm.TestUtilityService;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.configstore.V1_0.OptionalBool;
import android.hardware.configstore.V1_1.ISurfaceFlingerConfigs;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManagerInternal;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.ITheiaManagerExt;
import android.os.InputConstants;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.os.PowerManagerInternal;
import android.os.PowerSaveState;
import android.os.Process;
import android.os.RemoteCallback;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ShellCallback;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.SystemService;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.DeviceConfigInterface;
import android.provider.Settings;
import android.service.vr.IVrManager;
import android.service.vr.IVrStateCallbacks;
import android.sysprop.SurfaceFlingerProperties;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.MergedConfiguration;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.TimeUtils;
import android.util.TypedValue;
import android.util.proto.ProtoOutputStream;
import android.view.Choreographer;
import android.view.ContentRecordingSession;
import android.view.DisplayInfo;
import android.view.IAppTransitionAnimationSpecsFuture;
import android.view.ICrossWindowBlurEnabledListener;
import android.view.IDisplayChangeWindowController;
import android.view.IDisplayFoldListener;
import android.view.IDisplayWindowInsetsController;
import android.view.IDisplayWindowListener;
import android.view.IInputFilter;
import android.view.IOnKeyguardExitResult;
import android.view.IPinnedTaskListener;
import android.view.IRecentsAnimationRunner;
import android.view.IRotationWatcher;
import android.view.IScrollCaptureResponseListener;
import android.view.ISystemGestureExclusionListener;
import android.view.IWallpaperVisibilityListener;
import android.view.IWindow;
import android.view.IWindowId;
import android.view.IWindowManager;
import android.view.IWindowSession;
import android.view.IWindowSessionCallback;
import android.view.InputApplicationHandle;
import android.view.InputChannel;
import android.view.InputWindowHandle;
import android.view.InsetsFrameProvider;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.MagnificationSpec;
import android.view.MotionEvent;
import android.view.RemoteAnimationAdapter;
import android.view.ScrollCaptureResponse;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.TaskTransitionSpec;
import android.view.View;
import android.view.ViewDebug;
import android.view.WindowContentFrameStats;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowManagerPolicyConstants;
import android.view.displayhash.DisplayHash;
import android.view.displayhash.VerifiedDisplayHash;
import android.view.inputmethod.ImeTracker;
import android.window.AddToSurfaceSyncGroupResult;
import android.window.ClientWindowFrames;
import android.window.ISurfaceSyncGroupCompletedListener;
import android.window.ITaskFpsCallback;
import android.window.ScreenCapture;
import android.window.TaskSnapshot;
import android.window.WindowContainerToken;
import android.window.WindowProviderService;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.IResultReceiver;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.policy.IKeyguardLockedStateListener;
import com.android.internal.policy.IShortcutService;
import com.android.internal.policy.KeyInterceptionInfo;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.LatencyTracker;
import com.android.internal.view.WindowManagerPolicyThread;
import com.android.server.AnimationThread;
import com.android.server.DisplayThread;
import com.android.server.FgThread;
import com.android.server.LocalServices;
import com.android.server.LockGuard;
import com.android.server.UiThread;
import com.android.server.Watchdog;
import com.android.server.input.InputManagerService;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.location.settings.SettingsStore$;
import com.android.server.pm.UserManagerInternal;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.power.ShutdownThread;
import com.android.server.utils.PriorityDump;
import com.android.server.wm.AccessibilityController;
import com.android.server.wm.ActivityRecord;
import com.android.server.wm.DisplayAreaPolicy;
import com.android.server.wm.DisplayContent;
import com.android.server.wm.EmbeddedWindowController;
import com.android.server.wm.RecentsAnimationController;
import com.android.server.wm.WindowManagerInternal;
import com.android.server.wm.WindowManagerService;
import com.android.server.wm.WindowState;
import com.android.server.wm.WindowToken;
import dalvik.annotation.optimization.NeverCompile;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WindowManagerService extends IWindowManager.Stub implements Watchdog.Monitor, WindowManagerPolicy.WindowManagerFuncs {
    private static final int ANIMATION_COMPLETED_TIMEOUT_MS = 5000;
    private static final int ANIMATION_DURATION_SCALE = 2;
    private static final int BOOT_ANIMATION_POLL_INTERVAL = 50;
    private static final String BOOT_ANIMATION_SERVICE = "bootanim";
    private static final String DENSITY_OVERRIDE = "ro.config.density_override";
    private static final int INPUT_DEVICES_READY_FOR_SAFE_MODE_DETECTION_TIMEOUT_MILLIS = 1000;
    static final int LAST_ANR_LIFETIME_DURATION_MSECS = 7200000;
    static final int LAYOUT_REPEAT_THRESHOLD = 4;
    static final int LOGTAG_INPUT_FOCUS = 62001;
    static final int MAX_ANIMATION_DURATION = 10000;
    static final boolean PROFILE_ORIENTATION = false;
    private static final String PROPERTY_EMULATOR_CIRCULAR = "ro.emulator.circular";
    private static final String SIZE_OVERRIDE = "ro.config.size_override";
    private static final int SYNC_INPUT_TRANSACTIONS_TIMEOUT_MS = 5000;
    private static final String SYSTEM_DEBUGGABLE = "ro.debuggable";
    private static final String SYSTEM_SECURE = "ro.secure";
    private static final String TAG = "WindowManager";
    private static final int TRACE_MAX_SECTION_NAME_LENGTH = 127;
    private static final int TRANSITION_ANIMATION_SCALE = 1;
    static final int UPDATE_FOCUS_NORMAL = 0;
    static final int UPDATE_FOCUS_PLACING_SURFACES = 2;
    static final int UPDATE_FOCUS_REMOVING_FOCUS = 4;
    static final int UPDATE_FOCUS_WILL_ASSIGN_LAYERS = 1;
    static final int UPDATE_FOCUS_WILL_PLACE_SURFACES = 3;
    static final boolean USE_BLAST_SYNC = true;
    static final int WINDOWS_FREEZING_SCREENS_ACTIVE = 1;
    static final int WINDOWS_FREEZING_SCREENS_NONE = 0;
    static final int WINDOWS_FREEZING_SCREENS_TIMEOUT = 2;
    private static final int WINDOW_ANIMATION_SCALE = 0;
    static final int WINDOW_FREEZE_TIMEOUT_DURATION = 2000;
    final AccessibilityController mAccessibilityController;
    final IActivityManager mActivityManager;
    final WindowManagerInternal.AppTransitionListener mActivityManagerAppTransitionNotifier;
    final boolean mAllowAnimationsInLowPowerMode;
    final boolean mAllowBootMessages;
    boolean mAllowTheaterModeWakeFromLayout;
    final ActivityManagerInternal mAmInternal;
    final Handler mAnimationHandler;
    final ArrayMap<AnimationAdapter, SurfaceAnimator> mAnimationTransferMap;
    private boolean mAnimationsDisabled;
    final WindowAnimator mAnimator;
    private float mAnimatorDurationScaleSetting;
    final AnrController mAnrController;
    final ArrayList<AppFreezeListener> mAppFreezeListeners;
    final AppOpsManager mAppOps;
    int mAppsFreezingScreen;
    final boolean mAssistantOnTopOfDream;
    final ActivityTaskManagerService mAtmService;
    final BlurController mBlurController;
    boolean mBootAnimationStopped;
    long mBootWaitForWindowsStartTime;
    private final BroadcastReceiver mBroadcastReceiver;
    boolean mClientFreezingScreen;
    final WindowManagerConstants mConstants;

    @VisibleForTesting
    final ContentRecordingController mContentRecordingController;
    final Context mContext;
    int mCurrentUserId;
    final ArrayList<WindowState> mDestroySurface;
    boolean mDisableTransitionAnimation;
    private final DisplayAreaPolicy.Provider mDisplayAreaPolicyProvider;
    IDisplayChangeWindowController mDisplayChangeController;
    private final IBinder.DeathRecipient mDisplayChangeControllerDeath;
    boolean mDisplayEnabled;
    long mDisplayFreezeTime;
    boolean mDisplayFrozen;
    private final DisplayHashController mDisplayHashController;
    volatile Map<Integer, Integer> mDisplayImePolicyCache;
    final DisplayManager mDisplayManager;
    final DisplayManagerInternal mDisplayManagerInternal;
    final DisplayWindowListenerController mDisplayNotificationController;
    boolean mDisplayReady;
    final DisplayWindowSettings mDisplayWindowSettings;
    final DisplayWindowSettingsProvider mDisplayWindowSettingsProvider;
    final DragDropController mDragDropController;
    final long mDrawLockTimeoutMillis;
    final EmbeddedWindowController mEmbeddedWindowController;
    EmulatorDisplayOverlay mEmulatorDisplayOverlay;
    private int mEnterAnimId;
    private boolean mEventDispatchingEnabled;
    private int mExitAnimId;
    boolean mFocusMayChange;
    private InputTarget mFocusedInputTarget;
    boolean mForceDesktopModeOnExternalDisplays;
    boolean mForceDisplayEnabled;
    final ArrayList<WindowState> mForceRemoves;
    private int mFrozenDisplayId;
    final WindowManagerGlobalLock mGlobalLock;
    final H mH;
    boolean mHardKeyboardAvailable;
    WindowManagerInternal.OnHardKeyboardStatusChangeListener mHardKeyboardStatusChangeListener;
    private boolean mHasHdrSupport;
    final boolean mHasPermanentDpad;
    private boolean mHasWideColorGamutSupport;
    private ArrayList<WindowState> mHidingNonSystemOverlayWindows;
    final HighRefreshRateDenylist mHighRefreshRateDenylist;
    ImeTargetChangeListener mImeTargetChangeListener;
    final InputManagerService mInputManager;
    final InputManagerCallback mInputManagerCallback;
    final HashMap<IBinder, WindowState> mInputToWindowMap;
    boolean mIsFakeTouchDevice;
    private boolean mIsIgnoreOrientationRequestDisabled;
    boolean mIsPc;
    boolean mIsTouchDevice;
    private final KeyguardDisableHandler mKeyguardDisableHandler;
    String mLastANRState;
    int mLastDisplayFreezeDuration;
    Object mLastFinishedFreezeSource;
    final LatencyTracker mLatencyTracker;
    final LetterboxConfiguration mLetterboxConfiguration;
    final boolean mLimitedAlphaCompositing;
    final int mMaxUiWidth;
    volatile float mMaximumObscuringOpacityForTouch;
    MousePositionTracker mMousePositionTracker;
    private final SparseIntArray mOrientationMapping;

    @VisibleForTesting
    boolean mPerDisplayFocusEnabled;
    final PackageManagerInternal mPmInternal;
    boolean mPointerLocationEnabled;

    @VisibleForTesting
    WindowManagerPolicy mPolicy;
    final PossibleDisplayInfoMapper mPossibleDisplayInfoMapper;
    PowerManager mPowerManager;
    PowerManagerInternal mPowerManagerInternal;
    private final PriorityDump.PriorityDumper mPriorityDumper;
    private RecentsAnimationController mRecentsAnimationController;
    final ArrayList<WindowState> mResizingWindows;
    final RootWindowContainer mRoot;
    final RotationWatcherController mRotationWatcherController;
    boolean mSafeMode;
    private final PowerManager.WakeLock mScreenFrozenLock;
    final ArraySet<Session> mSessions;
    SettingsObserver mSettingsObserver;
    boolean mShowAlertWindowNotifications;
    boolean mShowingBootMessages;

    @VisibleForTesting
    boolean mSkipActivityRelaunchWhenDocking;
    final SnapshotController mSnapshotController;
    final StartingSurfaceController mStartingSurfaceController;
    StrictModeFlash mStrictModeFlash;
    SurfaceAnimationRunner mSurfaceAnimationRunner;
    Function<SurfaceSession, SurfaceControl.Builder> mSurfaceControlFactory;
    private final SurfaceSyncGroupController mSurfaceSyncGroupController;
    boolean mSwitchingUser;
    final BLASTSyncEngine mSyncEngine;
    boolean mSystemBooted;
    boolean mSystemReady;
    final TaskFpsCallbackController mTaskFpsCallbackController;
    final TaskPositioningController mTaskPositioningController;
    final TaskSnapshotController mTaskSnapshotController;
    final TaskSystemBarsListenerController mTaskSystemBarsListenerController;
    TaskTransitionSpec mTaskTransitionSpec;
    private WindowContentFrameStats mTempWindowRenderStats;
    private final TestUtilityService mTestUtilityService;
    final Rect mTmpRect;
    private final SurfaceControl.Transaction mTransaction;
    Supplier<SurfaceControl.Transaction> mTransactionFactory;
    int mTransactionSequence;
    private float mTransitionAnimationScaleSetting;
    final TransitionTracer mTransitionTracer;
    final UserManagerInternal mUmInternal;
    final boolean mUseBLAST;
    private ViewServer mViewServer;
    final ArrayMap<WindowContainer<?>, Message> mWaitingForDrawnCallbacks;
    final WallpaperVisibilityListeners mWallpaperVisibilityListeners;
    Watermark mWatermark;
    private float mWindowAnimationScaleSetting;
    final ArrayList<WindowChangeListener> mWindowChangeListeners;

    @VisibleForTesting
    final WindowContextListenerController mWindowContextListenerController;
    private IWindowManagerServiceExt mWindowManagerServiceExt;
    public IWindowManagerServiceSocExt mWindowManagerServiceSocExt;
    final HashMap<IBinder, WindowState> mWindowMap;
    final WindowSurfacePlacer mWindowPlacerLocked;
    final WindowTracing mWindowTracing;
    boolean mWindowsChanged;
    int mWindowsFreezingScreen;
    int mWindowsInsetsChanged;
    private IWindowManagerServiceWrapper mWrapper;
    static final int MY_PID = Process.myPid();
    static final int MY_UID = Process.myUid();
    public static final String ENABLE_SHELL_TRANSITIONS = "persist.wm.debug.shell_transit";
    public static final boolean sEnableShellTransitions = SystemProperties.getBoolean(ENABLE_SHELL_TRANSITIONS, true);
    static final boolean ENABLE_FIXED_ROTATION_TRANSFORM = SystemProperties.getBoolean("persist.wm.fixed_rotation_transform", true);
    static WindowManagerThreadPriorityBooster sThreadPriorityBooster = new WindowManagerThreadPriorityBooster();
    private final RemoteCallbackList<IKeyguardLockedStateListener> mKeyguardLockedStateListeners = new RemoteCallbackList<>();
    private boolean mDispatchedKeyguardLockedState = false;
    private ITheiaManagerExt mTheiaManagerExt = (ITheiaManagerExt) ExtLoader.type(ITheiaManagerExt.class).create();
    int mVr2dDisplayId = -1;
    boolean mVrModeEnabled = false;
    final Map<IBinder, KeyInterceptionInfo> mKeyInterceptionInfoForToken = Collections.synchronizedMap(new ArrayMap());
    private final IVrStateCallbacks mVrStateCallbacks = new AnonymousClass1();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    interface AppFreezeListener {
        void onAppFreezeTimeout();
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private @interface UpdateAnimationScaleMode {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface WindowChangeListener {
        void focusChanged();

        void windowsChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean excludeWindowTypeFromTapOutTask(int i) {
        return i == 2000 || i == 2012 || i == 2040 || i == 2019 || i == 2020;
    }

    public void endProlongedAnimations() {
    }

    public int getDockedStackSide() {
        return 0;
    }

    public boolean useBLASTSync() {
        return true;
    }

    /* renamed from: com.android.server.wm.WindowManagerService$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    class AnonymousClass1 extends IVrStateCallbacks.Stub {
        AnonymousClass1() {
        }

        public void onVrStateChanged(final boolean z) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService windowManagerService = WindowManagerService.this;
                    windowManagerService.mVrModeEnabled = z;
                    windowManagerService.mRoot.forAllDisplayPolicies(new Consumer() { // from class: com.android.server.wm.WindowManagerService$1$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            ((DisplayPolicy) obj).onVrStateChangedLw(z);
                        }
                    });
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mDisplayChangeController = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public final class SettingsObserver extends ContentObserver {
        private final Uri mAnimationDurationScaleUri;
        private final Uri mDevEnableNonResizableMultiWindowUri;
        private final Uri mDisplayInversionEnabledUri;
        private final Uri mDisplaySettingsPathUri;
        private final Uri mForceDesktopModeOnExternalDisplaysUri;
        private final Uri mForceResizableUri;
        private final Uri mFreeformWindowUri;
        private final Uri mImmersiveModeConfirmationsUri;
        private final Uri mMaximumObscuringOpacityForTouchUri;
        private final Uri mPointerLocationUri;
        private final Uri mPolicyControlUri;
        private final Uri mTransitionAnimationScaleUri;
        private final Uri mWindowAnimationScaleUri;

        public SettingsObserver() {
            super(new Handler());
            Uri uriFor = Settings.Secure.getUriFor("accessibility_display_inversion_enabled");
            this.mDisplayInversionEnabledUri = uriFor;
            Uri uriFor2 = Settings.Global.getUriFor("window_animation_scale");
            this.mWindowAnimationScaleUri = uriFor2;
            Uri uriFor3 = Settings.Global.getUriFor("transition_animation_scale");
            this.mTransitionAnimationScaleUri = uriFor3;
            Uri uriFor4 = Settings.Global.getUriFor("animator_duration_scale");
            this.mAnimationDurationScaleUri = uriFor4;
            Uri uriFor5 = Settings.Secure.getUriFor("immersive_mode_confirmations");
            this.mImmersiveModeConfirmationsUri = uriFor5;
            Uri uriFor6 = Settings.Global.getUriFor("policy_control");
            this.mPolicyControlUri = uriFor6;
            Uri uriFor7 = Settings.System.getUriFor("pointer_location");
            this.mPointerLocationUri = uriFor7;
            Uri uriFor8 = Settings.Global.getUriFor("force_desktop_mode_on_external_displays");
            this.mForceDesktopModeOnExternalDisplaysUri = uriFor8;
            Uri uriFor9 = Settings.Global.getUriFor("enable_freeform_support");
            this.mFreeformWindowUri = uriFor9;
            Uri uriFor10 = Settings.Global.getUriFor("force_resizable_activities");
            this.mForceResizableUri = uriFor10;
            Uri uriFor11 = Settings.Global.getUriFor("enable_non_resizable_multi_window");
            this.mDevEnableNonResizableMultiWindowUri = uriFor11;
            Uri uriFor12 = Settings.Global.getUriFor("wm_display_settings_path");
            this.mDisplaySettingsPathUri = uriFor12;
            Uri uriFor13 = Settings.Global.getUriFor("maximum_obscuring_opacity_for_touch");
            this.mMaximumObscuringOpacityForTouchUri = uriFor13;
            ContentResolver contentResolver = WindowManagerService.this.mContext.getContentResolver();
            contentResolver.registerContentObserver(uriFor, false, this, -1);
            contentResolver.registerContentObserver(uriFor2, false, this, -1);
            contentResolver.registerContentObserver(uriFor3, false, this, -1);
            contentResolver.registerContentObserver(uriFor4, false, this, -1);
            contentResolver.registerContentObserver(uriFor5, false, this, -1);
            contentResolver.registerContentObserver(uriFor6, false, this, -1);
            contentResolver.registerContentObserver(uriFor7, false, this, -1);
            contentResolver.registerContentObserver(uriFor8, false, this, -1);
            contentResolver.registerContentObserver(uriFor9, false, this, -1);
            contentResolver.registerContentObserver(uriFor10, false, this, -1);
            contentResolver.registerContentObserver(uriFor11, false, this, -1);
            contentResolver.registerContentObserver(uriFor12, false, this, -1);
            contentResolver.registerContentObserver(uriFor13, false, this, -1);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            if (uri == null) {
                return;
            }
            int i = 1;
            if (this.mImmersiveModeConfirmationsUri.equals(uri) || this.mPolicyControlUri.equals(uri)) {
                updateSystemUiSettings(true);
                return;
            }
            if (this.mPointerLocationUri.equals(uri)) {
                updatePointerLocation();
                return;
            }
            if (this.mForceDesktopModeOnExternalDisplaysUri.equals(uri)) {
                updateForceDesktopModeOnExternalDisplays();
                return;
            }
            if (this.mFreeformWindowUri.equals(uri)) {
                updateFreeformWindowManagement();
                return;
            }
            if (this.mForceResizableUri.equals(uri)) {
                updateForceResizableTasks();
                return;
            }
            if (this.mDevEnableNonResizableMultiWindowUri.equals(uri)) {
                updateDevEnableNonResizableMultiWindow();
                return;
            }
            if (this.mDisplaySettingsPathUri.equals(uri)) {
                updateDisplaySettingsLocation();
                return;
            }
            if (this.mMaximumObscuringOpacityForTouchUri.equals(uri)) {
                updateMaximumObscuringOpacityForTouch();
                return;
            }
            if (this.mWindowAnimationScaleUri.equals(uri)) {
                i = 0;
            } else if (!this.mTransitionAnimationScaleUri.equals(uri)) {
                if (!this.mAnimationDurationScaleUri.equals(uri)) {
                    return;
                } else {
                    i = 2;
                }
            }
            WindowManagerService.this.mH.sendMessage(WindowManagerService.this.mH.obtainMessage(51, i, 0));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void loadSettings() {
            updateSystemUiSettings(false);
            updatePointerLocation();
            updateMaximumObscuringOpacityForTouch();
        }

        void updateMaximumObscuringOpacityForTouch() {
            ContentResolver contentResolver = WindowManagerService.this.mContext.getContentResolver();
            WindowManagerService.this.mMaximumObscuringOpacityForTouch = Settings.Global.getFloat(contentResolver, "maximum_obscuring_opacity_for_touch", 0.8f);
            if (WindowManagerService.this.mMaximumObscuringOpacityForTouch < 0.0f || WindowManagerService.this.mMaximumObscuringOpacityForTouch > 1.0f) {
                WindowManagerService.this.mMaximumObscuringOpacityForTouch = 0.8f;
            }
        }

        void updateSystemUiSettings(boolean z) {
            boolean z2;
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (z) {
                        z2 = WindowManagerService.this.getDefaultDisplayContentLocked().getDisplayPolicy().onSystemUiSettingsChanged();
                    } else {
                        WindowManagerService windowManagerService = WindowManagerService.this;
                        ImmersiveModeConfirmation.loadSetting(windowManagerService.mCurrentUserId, windowManagerService.mContext);
                        z2 = false;
                    }
                    if (z2) {
                        WindowManagerService.this.mWindowPlacerLocked.requestTraversal();
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        void updatePointerLocation() {
            boolean z = Settings.System.getIntForUser(WindowManagerService.this.mContext.getContentResolver(), "pointer_location", 0, -2) != 0;
            WindowManagerService windowManagerService = WindowManagerService.this;
            if (windowManagerService.mPointerLocationEnabled == z) {
                return;
            }
            windowManagerService.mPointerLocationEnabled = z;
            WindowManagerGlobalLock windowManagerGlobalLock = windowManagerService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.mRoot.forAllDisplayPolicies(new Consumer() { // from class: com.android.server.wm.WindowManagerService$SettingsObserver$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            WindowManagerService.SettingsObserver.this.lambda$updatePointerLocation$0((DisplayPolicy) obj);
                        }
                    });
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updatePointerLocation$0(DisplayPolicy displayPolicy) {
            displayPolicy.setPointerLocationEnabled(WindowManagerService.this.mPointerLocationEnabled);
        }

        void updateForceDesktopModeOnExternalDisplays() {
            boolean z = Settings.Global.getInt(WindowManagerService.this.mContext.getContentResolver(), "force_desktop_mode_on_external_displays", 0) != 0;
            WindowManagerService windowManagerService = WindowManagerService.this;
            if (windowManagerService.mForceDesktopModeOnExternalDisplays == z) {
                return;
            }
            windowManagerService.setForceDesktopModeOnExternalDisplays(z);
        }

        /* JADX WARN: Code restructure failed: missing block: B:4:0x001f, code lost:
        
            if (android.provider.Settings.Global.getInt(r0, "enable_freeform_support", 0) != 0) goto L6;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        void updateFreeformWindowManagement() {
            ContentResolver contentResolver = WindowManagerService.this.mContext.getContentResolver();
            boolean z = WindowManagerService.this.mContext.getPackageManager().hasSystemFeature("android.software.freeform_window_management");
            WindowManagerService windowManagerService = WindowManagerService.this;
            ActivityTaskManagerService activityTaskManagerService = windowManagerService.mAtmService;
            if (activityTaskManagerService.mSupportsFreeformWindowManagement != z) {
                activityTaskManagerService.mSupportsFreeformWindowManagement = z;
                WindowManagerGlobalLock windowManagerGlobalLock = windowManagerService.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        WindowManagerService.this.mRoot.onSettingsRetrieved();
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            }
        }

        void updateForceResizableTasks() {
            WindowManagerService.this.mAtmService.mForceResizableActivities = Settings.Global.getInt(WindowManagerService.this.mContext.getContentResolver(), "force_resizable_activities", 0) != 0;
        }

        void updateDevEnableNonResizableMultiWindow() {
            WindowManagerService.this.mAtmService.mDevEnableNonResizableMultiWindow = Settings.Global.getInt(WindowManagerService.this.mContext.getContentResolver(), "enable_non_resizable_multi_window", 0) != 0;
        }

        void updateDisplaySettingsLocation() {
            String string = Settings.Global.getString(WindowManagerService.this.mContext.getContentResolver(), "wm_display_settings_path");
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.mDisplayWindowSettingsProvider.setBaseSettingsFilePath(string);
                    WindowManagerService.this.mRoot.forAllDisplays(new Consumer() { // from class: com.android.server.wm.WindowManagerService$SettingsObserver$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            WindowManagerService.SettingsObserver.this.lambda$updateDisplaySettingsLocation$1((DisplayContent) obj);
                        }
                    });
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateDisplaySettingsLocation$1(DisplayContent displayContent) {
            WindowManagerService.this.mDisplayWindowSettings.applySettingsToDisplayLocked(displayContent);
            displayContent.reconfigureDisplayLocked();
        }
    }

    public static void boostPriorityForLockedSection() {
        sThreadPriorityBooster.boost();
    }

    public static void resetPriorityAfterLockedSection() {
        sThreadPriorityBooster.reset();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void openSurfaceTransaction() {
        try {
            Trace.traceBegin(32L, "openSurfaceTransaction");
            SurfaceControl.openTransaction();
        } finally {
            Trace.traceEnd(32L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeSurfaceTransaction(String str) {
        try {
            Trace.traceBegin(32L, "closeSurfaceTransaction");
            SurfaceControl.closeTransaction();
            this.mWindowTracing.logState(str);
        } finally {
            Trace.traceEnd(32L);
        }
    }

    public static WindowManagerService main(Context context, InputManagerService inputManagerService, boolean z, WindowManagerPolicy windowManagerPolicy, ActivityTaskManagerService activityTaskManagerService) {
        return main(context, inputManagerService, z, windowManagerPolicy, activityTaskManagerService, new DisplayWindowSettingsProvider(), new Supplier() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda14
            @Override // java.util.function.Supplier
            public final Object get() {
                return new SurfaceControl.Transaction();
            }
        }, new Function() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda15
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return new SurfaceControl.Builder((SurfaceSession) obj);
            }
        });
    }

    @VisibleForTesting
    public static WindowManagerService main(final Context context, final InputManagerService inputManagerService, final boolean z, final WindowManagerPolicy windowManagerPolicy, final ActivityTaskManagerService activityTaskManagerService, final DisplayWindowSettingsProvider displayWindowSettingsProvider, final Supplier<SurfaceControl.Transaction> supplier, final Function<SurfaceSession, SurfaceControl.Builder> function) {
        final WindowManagerService[] windowManagerServiceArr = new WindowManagerService[1];
        DisplayThread.getHandler().runWithScissors(new Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                WindowManagerService.lambda$main$1(windowManagerServiceArr, context, inputManagerService, z, windowManagerPolicy, activityTaskManagerService, displayWindowSettingsProvider, supplier, function);
            }
        }, 0L);
        return windowManagerServiceArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$main$1(WindowManagerService[] windowManagerServiceArr, Context context, InputManagerService inputManagerService, boolean z, WindowManagerPolicy windowManagerPolicy, ActivityTaskManagerService activityTaskManagerService, DisplayWindowSettingsProvider displayWindowSettingsProvider, Supplier supplier, Function function) {
        windowManagerServiceArr[0] = ((IWindowManagerServiceExt) ExtLoader.type(IWindowManagerServiceExt.class).create()).getOplusWindowManagerService(context, inputManagerService, z, windowManagerPolicy, activityTaskManagerService, displayWindowSettingsProvider, supplier, function);
    }

    private void initPolicy() {
        UiThread.getHandler().runWithScissors(new Runnable() { // from class: com.android.server.wm.WindowManagerService.5
            @Override // java.lang.Runnable
            public void run() {
                WindowManagerPolicyThread.set(Thread.currentThread(), Looper.myLooper());
                WindowManagerService windowManagerService = WindowManagerService.this;
                windowManagerService.mPolicy.init(windowManagerService.mContext, windowManagerService);
            }
        }, 0L);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
        new WindowManagerShellCommand(this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
    }

    WindowManagerService(Context context, InputManagerService inputManagerService, boolean z, WindowManagerPolicy windowManagerPolicy, ActivityTaskManagerService activityTaskManagerService, DisplayWindowSettingsProvider displayWindowSettingsProvider, Supplier<SurfaceControl.Transaction> supplier, Function<SurfaceSession, SurfaceControl.Builder> function) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.wm.WindowManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                action.hashCode();
                if (action.equals("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED")) {
                    WindowManagerService.this.mKeyguardDisableHandler.updateKeyguardEnabled(getSendingUserId());
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mPriorityDumper = new PriorityDump.PriorityDumper() { // from class: com.android.server.wm.WindowManagerService.3
            public void dumpCritical(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z2) {
                WindowManagerService.this.doDump(fileDescriptor, printWriter, new String[]{"-a"}, z2);
            }

            public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z2) {
                WindowManagerService.this.doDump(fileDescriptor, printWriter, strArr, z2);
            }
        };
        this.mShowAlertWindowNotifications = true;
        this.mSessions = new ArraySet<>();
        this.mWindowMap = new HashMap<>();
        this.mInputToWindowMap = new HashMap<>();
        this.mResizingWindows = new ArrayList<>();
        this.mDisplayImePolicyCache = Collections.unmodifiableMap(new ArrayMap());
        this.mDestroySurface = new ArrayList<>();
        this.mForceRemoves = new ArrayList<>();
        this.mWaitingForDrawnCallbacks = new ArrayMap<>();
        this.mHidingNonSystemOverlayWindows = new ArrayList<>();
        this.mOrientationMapping = new SparseIntArray();
        this.mTmpRect = new Rect();
        this.mDisplayEnabled = false;
        this.mSystemBooted = false;
        this.mForceDisplayEnabled = false;
        this.mShowingBootMessages = false;
        this.mSystemReady = false;
        this.mBootAnimationStopped = false;
        this.mBootWaitForWindowsStartTime = -1L;
        this.mWindowManagerServiceSocExt = (IWindowManagerServiceSocExt) ExtLoader.type(IWindowManagerServiceSocExt.class).base(this).create();
        this.mWallpaperVisibilityListeners = new WallpaperVisibilityListeners();
        byte b = 0;
        byte b2 = 0;
        this.mDisplayChangeController = null;
        this.mDisplayChangeControllerDeath = new IBinder.DeathRecipient() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda28
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                WindowManagerService.this.lambda$new$0();
            }
        };
        this.mDisplayFrozen = false;
        this.mDisplayFreezeTime = 0L;
        this.mLastDisplayFreezeDuration = 0;
        this.mLastFinishedFreezeSource = null;
        this.mSwitchingUser = false;
        this.mWindowsFreezingScreen = 0;
        this.mClientFreezingScreen = false;
        this.mAppsFreezingScreen = 0;
        this.mWindowsInsetsChanged = 0;
        H h = new H();
        this.mH = h;
        this.mAnimationHandler = new Handler(AnimationThread.getHandler().getLooper());
        this.mMaximumObscuringOpacityForTouch = 0.8f;
        this.mWindowContextListenerController = new WindowContextListenerController();
        this.mContentRecordingController = new ContentRecordingController();
        this.mSurfaceSyncGroupController = new SurfaceSyncGroupController();
        this.mWindowAnimationScaleSetting = 1.0f;
        this.mTransitionAnimationScaleSetting = 1.0f;
        this.mAnimatorDurationScaleSetting = 1.0f;
        this.mAnimationsDisabled = false;
        this.mPointerLocationEnabled = false;
        this.mFrozenDisplayId = -1;
        this.mAnimationTransferMap = new ArrayMap<>();
        this.mWindowChangeListeners = new ArrayList<>();
        this.mWindowsChanged = false;
        this.mActivityManagerAppTransitionNotifier = new WindowManagerInternal.AppTransitionListener() { // from class: com.android.server.wm.WindowManagerService.4
            @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
            public void onAppTransitionCancelledLocked(boolean z2) {
            }

            @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
            public void onAppTransitionFinishedLocked(IBinder iBinder) {
                ActivityRecord activityRecord = WindowManagerService.this.mRoot.getActivityRecord(iBinder);
                if (activityRecord == null) {
                    return;
                }
                if (activityRecord.mLaunchTaskBehind && !WindowManagerService.this.isRecentsAnimationTarget(activityRecord) && !WindowManagerService.this.mWindowManagerServiceExt.isGestureAnimationTarget(activityRecord)) {
                    WindowManagerService.this.mAtmService.mTaskSupervisor.scheduleLaunchTaskBehindComplete(activityRecord.token);
                    activityRecord.mLaunchTaskBehind = false;
                    return;
                }
                activityRecord.updateReportedVisibilityLocked();
                if (!activityRecord.mEnteringAnimation || WindowManagerService.this.isRecentsAnimationTarget(activityRecord)) {
                    return;
                }
                activityRecord.mEnteringAnimation = false;
                if (activityRecord.attachedToProcess()) {
                    try {
                        WindowManagerService.this.mWindowManagerServiceExt.handleAppVisible(activityRecord);
                        activityRecord.app.getThread().scheduleEnterAnimationComplete(activityRecord.token);
                    } catch (RemoteException unused) {
                    }
                }
            }
        };
        this.mAppFreezeListeners = new ArrayList<>();
        this.mInputManagerCallback = new InputManagerCallback(this);
        this.mMousePositionTracker = new MousePositionTracker();
        this.mWrapper = new WindowManagerServiceWrapper();
        this.mWindowManagerServiceExt = (IWindowManagerServiceExt) ExtLoader.type(IWindowManagerServiceExt.class).create();
        LockGuard.installLock(this, 5);
        this.mGlobalLock = activityTaskManagerService.getGlobalLock();
        this.mAtmService = activityTaskManagerService;
        this.mContext = context;
        this.mIsPc = context.getPackageManager().hasSystemFeature("android.hardware.type.pc");
        this.mAllowBootMessages = z;
        this.mLimitedAlphaCompositing = context.getResources().getBoolean(17891808);
        this.mHasPermanentDpad = context.getResources().getBoolean(17891709);
        this.mDrawLockTimeoutMillis = context.getResources().getInteger(R.integer.config_maxResolverActivityColumns);
        this.mAllowAnimationsInLowPowerMode = context.getResources().getBoolean(R.bool.config_allowTheaterModeWakeFromGesture);
        this.mMaxUiWidth = context.getResources().getInteger(R.integer.config_screenBrightnessSettingDefault);
        this.mDisableTransitionAnimation = context.getResources().getBoolean(R.bool.target_honeycomb_needs_options_menu);
        this.mPerDisplayFocusEnabled = context.getResources().getBoolean(R.bool.config_perDisplayFocusEnabled);
        this.mAssistantOnTopOfDream = context.getResources().getBoolean(R.bool.ImsConnectedDefaultValue);
        this.mSkipActivityRelaunchWhenDocking = context.getResources().getBoolean(17891820);
        this.mLetterboxConfiguration = new LetterboxConfiguration(ActivityThread.currentActivityThread().getSystemUiContext());
        this.mInputManager = inputManagerService;
        DisplayManagerInternal displayManagerInternal = (DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class);
        this.mDisplayManagerInternal = displayManagerInternal;
        this.mPossibleDisplayInfoMapper = new PossibleDisplayInfoMapper(displayManagerInternal);
        this.mSurfaceControlFactory = function;
        this.mTransactionFactory = supplier;
        this.mTransaction = supplier.get();
        this.mPolicy = windowManagerPolicy;
        this.mAnimator = new WindowAnimator(this);
        this.mRoot = new RootWindowContainer(this);
        ContentResolver contentResolver = context.getContentResolver();
        this.mUseBLAST = Settings.Global.getInt(contentResolver, "use_blast_adapter_vr", 1) == 1;
        this.mSyncEngine = new BLASTSyncEngine(this);
        this.mWindowPlacerLocked = new WindowSurfacePlacer(this);
        SnapshotController snapshotController = new SnapshotController(this);
        this.mSnapshotController = snapshotController;
        this.mTaskSnapshotController = snapshotController.mTaskSnapshotController;
        this.mWindowTracing = WindowTracing.createDefaultAndStartLooper(this, Choreographer.getInstance());
        this.mTransitionTracer = new TransitionTracer();
        LocalServices.addService(WindowManagerPolicy.class, this.mPolicy);
        this.mDisplayManager = (DisplayManager) context.getSystemService("display");
        this.mKeyguardDisableHandler = KeyguardDisableHandler.create(context, this.mPolicy, h);
        this.mPowerManager = (PowerManager) context.getSystemService("power");
        PowerManagerInternal powerManagerInternal = (PowerManagerInternal) LocalServices.getService(PowerManagerInternal.class);
        this.mPowerManagerInternal = powerManagerInternal;
        if (powerManagerInternal != null) {
            powerManagerInternal.registerLowPowerModeObserver(new PowerManagerInternal.LowPowerModeListener() { // from class: com.android.server.wm.WindowManagerService.6
                public int getServiceType() {
                    return 3;
                }

                public void onLowPowerModeChanged(PowerSaveState powerSaveState) {
                    WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            boolean z2 = powerSaveState.batterySaverEnabled;
                            if (WindowManagerService.this.mAnimationsDisabled != z2) {
                                WindowManagerService windowManagerService = WindowManagerService.this;
                                if (!windowManagerService.mAllowAnimationsInLowPowerMode) {
                                    windowManagerService.mAnimationsDisabled = z2;
                                    WindowManagerService.this.dispatchNewAnimatorScaleLocked(null);
                                }
                            }
                        } catch (Throwable th) {
                            WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                }
            });
            this.mAnimationsDisabled = this.mPowerManagerInternal.getLowPowerState(3).batterySaverEnabled;
        }
        PowerManager.WakeLock newWakeLock = this.mPowerManager.newWakeLock(1, "SCREEN_FROZEN");
        this.mScreenFrozenLock = newWakeLock;
        newWakeLock.setReferenceCounted(false);
        this.mRotationWatcherController = new RotationWatcherController(this);
        this.mDisplayNotificationController = new DisplayWindowListenerController(this);
        this.mTaskSystemBarsListenerController = new TaskSystemBarsListenerController();
        this.mActivityManager = ActivityManager.getService();
        this.mAmInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        this.mUmInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService("appops");
        this.mAppOps = appOpsManager;
        AppOpsManager.OnOpChangedInternalListener onOpChangedInternalListener = new AppOpsManager.OnOpChangedInternalListener() { // from class: com.android.server.wm.WindowManagerService.7
            public void onOpChanged(int i, String str) {
                WindowManagerService.this.updateAppOpsState();
            }
        };
        appOpsManager.startWatchingMode(24, (String) null, (AppOpsManager.OnOpChangedListener) onOpChangedInternalListener);
        appOpsManager.startWatchingMode(45, (String) null, (AppOpsManager.OnOpChangedListener) onOpChangedInternalListener);
        this.mPmInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        this.mTestUtilityService = (TestUtilityService) LocalServices.getService(TestUtilityService.class);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGES_SUSPENDED");
        intentFilter.addAction("android.intent.action.PACKAGES_UNSUSPENDED");
        context.registerReceiverAsUser(new BroadcastReceiver() { // from class: com.android.server.wm.WindowManagerService.8
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String[] stringArrayExtra = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                WindowManagerService.this.updateHiddenWhileSuspendedState(new ArraySet(Arrays.asList(stringArrayExtra)), "android.intent.action.PACKAGES_SUSPENDED".equals(intent.getAction()));
            }
        }, UserHandle.ALL, intentFilter, null, null);
        this.mWindowAnimationScaleSetting = getWindowAnimationScaleSetting();
        this.mTransitionAnimationScaleSetting = getTransitionAnimationScaleSetting();
        setAnimatorDurationScale(getAnimatorDurationScaleSetting());
        this.mForceDesktopModeOnExternalDisplays = Settings.Global.getInt(contentResolver, "force_desktop_mode_on_external_displays", 0) != 0;
        String string = Settings.Global.getString(contentResolver, "wm_display_settings_path");
        this.mDisplayWindowSettingsProvider = displayWindowSettingsProvider;
        if (string != null) {
            displayWindowSettingsProvider.setBaseSettingsFilePath(string);
        }
        this.mDisplayWindowSettings = new DisplayWindowSettings(this, displayWindowSettingsProvider);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
        context.registerReceiverAsUser(broadcastReceiver, UserHandle.ALL, intentFilter2, null, null);
        this.mLatencyTracker = LatencyTracker.getInstance(context);
        this.mSettingsObserver = new SettingsObserver();
        this.mSurfaceAnimationRunner = new SurfaceAnimationRunner(this.mTransactionFactory, this.mPowerManagerInternal);
        this.mAllowTheaterModeWakeFromLayout = context.getResources().getBoolean(R.bool.config_autoPowerModeUseMotionSensor);
        this.mTaskPositioningController = new TaskPositioningController(this);
        this.mDragDropController = new DragDropController(this, h.getLooper());
        this.mHighRefreshRateDenylist = HighRefreshRateDenylist.create(context.getResources());
        WindowManagerConstants windowManagerConstants = new WindowManagerConstants(this, DeviceConfigInterface.REAL);
        this.mConstants = windowManagerConstants;
        windowManagerConstants.start(new HandlerExecutor(h));
        LocalServices.addService(WindowManagerInternal.class, new LocalService());
        LocalServices.addService(ImeTargetVisibilityPolicy.class, new ImeTargetVisibilityPolicyImpl());
        this.mEmbeddedWindowController = new EmbeddedWindowController(activityTaskManagerService);
        this.mDisplayAreaPolicyProvider = DisplayAreaPolicy.Provider.fromResources(context.getResources());
        this.mDisplayHashController = new DisplayHashController(context);
        setGlobalShadowSettings();
        this.mAnrController = new AnrController(this);
        this.mStartingSurfaceController = new StartingSurfaceController(this);
        this.mBlurController = new BlurController(context, this.mPowerManager);
        this.mTaskFpsCallbackController = new TaskFpsCallbackController(context);
        this.mAccessibilityController = new AccessibilityController(this);
        this.mWindowManagerServiceExt.enableDefaultLogIfNeed(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayAreaPolicy.Provider getDisplayAreaPolicyProvider() {
        return this.mDisplayAreaPolicyProvider;
    }

    private void setGlobalShadowSettings() {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(null, com.android.internal.R.styleable.Lighting, 0, 0);
        float dimension = obtainStyledAttributes.getDimension(3, 0.0f);
        float dimension2 = obtainStyledAttributes.getDimension(4, 0.0f);
        float dimension3 = obtainStyledAttributes.getDimension(2, 0.0f);
        float f = obtainStyledAttributes.getFloat(0, 0.0f);
        float f2 = obtainStyledAttributes.getFloat(1, 0.0f);
        obtainStyledAttributes.recycle();
        SurfaceControl.setGlobalShadowSettings(new float[]{0.0f, 0.0f, 0.0f, f}, new float[]{0.0f, 0.0f, 0.0f, f2}, dimension, dimension2, dimension3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getTransitionAnimationScaleSetting() {
        return WindowManager.fixScale(Settings.Global.getFloat(this.mContext.getContentResolver(), "transition_animation_scale", this.mContext.getResources().getFloat(R.dimen.config_pictureInPictureAspectRatioLimitForMinSize)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getAnimatorDurationScaleSetting() {
        return WindowManager.fixScale(Settings.Global.getFloat(this.mContext.getContentResolver(), "animator_duration_scale", this.mAnimatorDurationScaleSetting));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getWindowAnimationScaleSetting() {
        return WindowManager.fixScale(Settings.Global.getFloat(this.mContext.getContentResolver(), "window_animation_scale", this.mWindowAnimationScaleSetting));
    }

    public void onInitReady() {
        initPolicy();
        Watchdog.getInstance().addMonitor(this);
        createWatermark();
        this.mWindowManagerServiceExt.oplusOnInitReady();
        showEmulatorDisplayOverlayIfNeeded();
    }

    public InputManagerCallback getInputManagerCallback() {
        return this.mInputManagerCallback;
    }

    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        try {
            return super.onTransact(i, parcel, parcel2, i2);
        } catch (RuntimeException e) {
            if (!(e instanceof SecurityException) && ProtoLogCache.WM_ERROR_enabled) {
                ProtoLogImpl.wtf(ProtoLogGroup.WM_ERROR, 371641947, 0, "Window Manager Crash %s", new Object[]{String.valueOf(e)});
            }
            throw e;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:158:0x05da, code lost:
    
        if (r1.mOwnerUid != r30) goto L304;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0514 A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0533 A[Catch: all -> 0x0844, TRY_ENTER, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:141:0x05a7 A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:144:0x05b2 A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:165:0x060c A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:173:0x0639 A[Catch: all -> 0x0844, TRY_ENTER, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:176:0x064f  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x0657 A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:182:0x0662 A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:185:0x068f A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:188:0x06b1  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0744 A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:202:0x074c A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:209:0x0767 A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:213:0x077a A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:218:0x079b A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:226:0x07e2 A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:228:0x07e9 A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0805 A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:238:0x081e A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:241:0x0776  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x0702 A[Catch: all -> 0x0844, TryCatch #0 {all -> 0x0844, blocks: (B:114:0x050d, B:116:0x0514, B:118:0x0518, B:119:0x052e, B:122:0x0533, B:124:0x0539, B:126:0x053d, B:127:0x0548, B:131:0x054e, B:133:0x0594, B:137:0x059b, B:141:0x05a7, B:144:0x05b2, B:146:0x05ba, B:148:0x05be, B:149:0x05c8, B:153:0x05cf, B:155:0x05d4, B:157:0x05d8, B:159:0x05ee, B:161:0x05f4, B:163:0x05fe, B:165:0x060c, B:167:0x0610, B:168:0x062c, B:170:0x0632, B:173:0x0639, B:174:0x0646, B:177:0x0653, B:179:0x0657, B:180:0x065c, B:182:0x0662, B:183:0x0667, B:185:0x068f, B:186:0x0694, B:189:0x06b4, B:193:0x06ca, B:195:0x06d1, B:197:0x0733, B:199:0x0744, B:200:0x0746, B:202:0x074c, B:203:0x074e, B:205:0x0752, B:207:0x075a, B:209:0x0767, B:213:0x077a, B:215:0x0783, B:216:0x0792, B:218:0x079b, B:219:0x07a4, B:221:0x07d3, B:224:0x07dc, B:226:0x07e2, B:228:0x07e9, B:229:0x07ec, B:231:0x0805, B:233:0x081a, B:234:0x0824, B:235:0x082a, B:238:0x081e, B:242:0x0758, B:245:0x06eb, B:247:0x06f5, B:253:0x0702, B:256:0x070b, B:257:0x0717, B:259:0x071d, B:260:0x0724, B:262:0x072c, B:265:0x05dc, B:418:0x083f, B:415:0x0834, B:416:0x083b), top: B:13:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0707  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x06b3  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x0652  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x05ec  */
    /* JADX WARN: Type inference failed for: r10v13, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r10v20 */
    /* JADX WARN: Type inference failed for: r10v21 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int addWindow(Session session, IWindow iWindow, WindowManager.LayoutParams layoutParams, int i, int i2, int i3, int i4, InputChannel inputChannel, InsetsState insetsState, InsetsSourceControl.Array array, Rect rect, float[] fArr) {
        int[] iArr;
        WindowState windowState;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        IBinder iBinder;
        int[] iArr2;
        int i10;
        int i11;
        IBinder iBinder2;
        int i12;
        ActivityRecord activityRecord;
        WindowToken windowToken;
        boolean z;
        ActivityRecord activityRecord2;
        WindowState windowState2;
        boolean z2;
        boolean z3;
        ?? r10;
        DisplayContent displayContent;
        boolean z4;
        ActivityRecord activityRecord3;
        boolean z5;
        boolean z6;
        boolean z7;
        int windowType;
        boolean z8;
        WindowToken build;
        array.set((InsetsSourceControl[]) null);
        int[] iArr3 = new int[1];
        boolean z9 = (layoutParams.privateFlags & 1048576) != 0;
        int checkAddPermission = this.mPolicy.checkAddPermission(layoutParams.type, z9, layoutParams.packageName, iArr3);
        if (checkAddPermission != 0) {
            return checkAddPermission;
        }
        if (!this.mWindowManagerServiceExt.canShowInLockDeviceMode(layoutParams.type)) {
            return -6;
        }
        int callingUid = Binder.getCallingUid();
        int callingPid = Binder.getCallingPid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        int i13 = layoutParams.type;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                try {
                    if (!this.mDisplayReady) {
                        throw new IllegalStateException("Display has not been initialialized");
                    }
                    DisplayContent displayContentOrCreate = getDisplayContentOrCreate(i2, layoutParams.token);
                    if (displayContentOrCreate == null) {
                        if (ProtoLogCache.WM_ERROR_enabled) {
                            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -861859917, 1, "Attempted to add window to a display that does not exist: %d. Aborting.", new Object[]{Long.valueOf(i2)});
                        }
                        resetPriorityAfterLockedSection();
                        return -9;
                    }
                    if (!displayContentOrCreate.hasAccess(session.mUid)) {
                        if (ProtoLogCache.WM_ERROR_enabled) {
                            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 435494046, 1, "Attempted to add window to a display for which the application does not have access: %d.  Aborting.", new Object[]{Long.valueOf(displayContentOrCreate.getDisplayId())});
                        }
                        resetPriorityAfterLockedSection();
                        return -9;
                    }
                    boolean z10 = z9;
                    if (this.mWindowMap.containsKey(iWindow.asBinder())) {
                        if (ProtoLogCache.WM_ERROR_enabled) {
                            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -507657818, 0, "Window %s is already added", new Object[]{String.valueOf(iWindow)});
                        }
                        resetPriorityAfterLockedSection();
                        return -5;
                    }
                    if (i13 < 1000 || i13 > 1999) {
                        iArr = iArr3;
                        windowState = null;
                    } else {
                        iArr = iArr3;
                        windowState = windowForClientLocked((Session) null, layoutParams.token, false);
                        if (windowState == null) {
                            if (ProtoLogCache.WM_ERROR_enabled) {
                                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 631792420, 0, "Attempted to add window with token that is not a window: %s.  Aborting.", new Object[]{String.valueOf(layoutParams.token)});
                            }
                            resetPriorityAfterLockedSection();
                            return -2;
                        }
                        int i14 = windowState.mAttrs.type;
                        if (i14 >= 1000 && i14 <= 1999) {
                            if (ProtoLogCache.WM_ERROR_enabled) {
                                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -2072089308, 0, "Attempted to add window with token that is a sub-window: %s.  Aborting.", new Object[]{String.valueOf(layoutParams.token)});
                            }
                            resetPriorityAfterLockedSection();
                            return -2;
                        }
                        if (!this.mWindowManagerServiceExt.canAddSubWindow(this.mContext, layoutParams)) {
                            resetPriorityAfterLockedSection();
                            return -6;
                        }
                    }
                    if (i13 == 2030 && !displayContentOrCreate.isPrivate()) {
                        if (ProtoLogCache.WM_ERROR_enabled) {
                            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -784959154, 0, "Attempted to add private presentation window to a non-private display.  Aborting.", (Object[]) null);
                        }
                        resetPriorityAfterLockedSection();
                        return -8;
                    }
                    if (i13 != 2037 || displayContentOrCreate.getDisplay().isPublicPresentation()) {
                        i5 = i13;
                    } else {
                        if (ProtoLogCache.WM_ERROR_enabled) {
                            i5 = i13;
                            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1670695197, 0, "Attempted to add presentation window to a non-suitable display.  Aborting.", (Object[]) null);
                        } else {
                            i5 = i13;
                        }
                        Slog.d(TAG, "non-suitable  DisplayId : " + displayContentOrCreate.getDisplay().getDisplayId() + " Flags: " + displayContentOrCreate.getDisplay().getFlags());
                        if (!this.mWindowManagerServiceExt.shouldShowPresentation(displayContentOrCreate, layoutParams.packageName)) {
                            resetPriorityAfterLockedSection();
                            return -9;
                        }
                    }
                    int userId = UserHandle.getUserId(session.mUid);
                    if (i3 != userId) {
                        try {
                            i6 = callingPid;
                            this.mAmInternal.handleIncomingUser(i6, callingUid, i3, false, 0, (String) null, (String) null);
                            i7 = i3;
                            i8 = 0;
                            i9 = 1;
                        } catch (Exception unused) {
                            if (ProtoLogCache.WM_ERROR_enabled) {
                                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 315395835, 1, "Trying to add window with invalid user=%d", new Object[]{Long.valueOf(i3)});
                            }
                            resetPriorityAfterLockedSection();
                            return -11;
                        }
                    } else {
                        i6 = callingPid;
                        i8 = 0;
                        i9 = 1;
                        i7 = userId;
                    }
                    int i15 = windowState != null ? i9 : i8;
                    if (i15 != 0) {
                        iBinder = windowState.mAttrs.token;
                        if (iBinder == null) {
                            iBinder = windowState.mToken.token;
                        }
                    } else {
                        iBinder = layoutParams.token;
                    }
                    WindowToken windowToken2 = displayContentOrCreate.getWindowToken(iBinder);
                    int i16 = i15 != 0 ? windowState.mAttrs.type : i5;
                    IBinder iBinder3 = layoutParams.mWindowContextToken;
                    if (windowToken2 == null) {
                        iBinder2 = iBinder3;
                        iArr2 = iArr;
                        WindowState windowState3 = windowState;
                        if (!unprivilegedAppCanCreateTokenWith(windowState, callingUid, i5, i16, layoutParams.token, layoutParams.packageName)) {
                            resetPriorityAfterLockedSection();
                            return -1;
                        }
                        if (i15 != 0) {
                            windowState = windowState3;
                            windowToken = windowState.mToken;
                            i12 = i6;
                            i11 = callingUid;
                            i10 = i5;
                            displayContentOrCreate = displayContentOrCreate;
                            activityRecord2 = null;
                            z = false;
                            ActivityRecord activityRecord4 = activityRecord2;
                            DisplayContent displayContent2 = displayContentOrCreate;
                            int i17 = i10;
                            int i18 = i11;
                            IBinder iBinder4 = iBinder2;
                            int i19 = i12;
                            windowState2 = new WindowState(this, session, iWindow, windowToken, windowState, iArr2[0], layoutParams, i, session.mUid, i7, session.mCanAddInternalSystemWindow);
                            if (windowState2.mDeathRecipient != null) {
                                if (ProtoLogCache.WM_ERROR_enabled) {
                                    ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1770075711, 0, "Adding window client %s that is dead, aborting.", new Object[]{String.valueOf(iWindow.asBinder())});
                                }
                                resetPriorityAfterLockedSection();
                                return -4;
                            }
                            if (windowState2.getDisplayContent() == null) {
                                if (ProtoLogCache.WM_ERROR_enabled) {
                                    ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1720696061, 0, "Adding window to Display that has been removed.", (Object[]) null);
                                }
                                resetPriorityAfterLockedSection();
                                return -9;
                            }
                            DisplayPolicy displayPolicy = displayContent2.getDisplayPolicy();
                            displayPolicy.adjustWindowParamsLw(windowState2, windowState2.mAttrs);
                            layoutParams.flags = sanitizeFlagSlippery(layoutParams.flags, windowState2.getName(), i18, i19);
                            layoutParams.inputFeatures = sanitizeSpyWindow(layoutParams.inputFeatures, windowState2.getName(), i18, i19);
                            windowState2.setRequestedVisibleTypes(i4);
                            displayContent2.getWrapper().getExtImpl().setVendorPreferredRefreshRate(windowState2.getWrapper().getExtImpl(), windowState2);
                            int validateAddingWindowLw = displayPolicy.validateAddingWindowLw(layoutParams, i19, i18);
                            if (validateAddingWindowLw != 0) {
                                resetPriorityAfterLockedSection();
                                return validateAddingWindowLw;
                            }
                            if (inputChannel != null) {
                                z2 = true;
                                r10 = 1;
                                if ((layoutParams.inputFeatures & 1) == 0) {
                                    z3 = true;
                                    if (z3) {
                                        windowState2.openInputChannel(inputChannel);
                                    }
                                    if (i17 != 2005) {
                                        DisplayContent displayContent3 = displayContent2;
                                        if (!displayContent3.canAddToastWindowForUid(i18)) {
                                            if (ProtoLogCache.WM_ERROR_enabled) {
                                                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -883738232, 0, "Adding more than one toast window for UID at a time.", (Object[]) null);
                                            }
                                            resetPriorityAfterLockedSection();
                                            return -5;
                                        }
                                        if (!z && (layoutParams.flags & 8) != 0 && (r1 = displayContent3.mCurrentFocus) != null) {
                                            displayContent = displayContent3;
                                        }
                                        H h = this.mH;
                                        h.sendMessageDelayed(h.obtainMessage(52, windowState2), windowState2.mAttrs.hideTimeoutMilliseconds);
                                        displayContent = displayContent3;
                                    } else {
                                        displayContent = displayContent2;
                                    }
                                    if (!windowState2.isChildWindow() && this.mWindowContextListenerController.hasListener(iBinder4)) {
                                        windowType = this.mWindowContextListenerController.getWindowType(iBinder4);
                                        Bundle options = this.mWindowContextListenerController.getOptions(iBinder4);
                                        if (i17 == windowType) {
                                            if (ProtoLogCache.WM_ERROR_enabled) {
                                                ProtoLogGroup protoLogGroup = ProtoLogGroup.WM_ERROR;
                                                Object[] objArr = new Object[2];
                                                objArr[0] = Long.valueOf(i17);
                                                objArr[r10] = Long.valueOf(windowType);
                                                ProtoLogImpl.w(protoLogGroup, 1252594551, 5, "Window types in WindowContext and LayoutParams.type should match! Type from LayoutParams is %d, but type from WindowContext is %d", objArr);
                                            }
                                            if (!WindowProviderService.isWindowProviderService(options)) {
                                                resetPriorityAfterLockedSection();
                                                return -10;
                                            }
                                        } else {
                                            this.mWindowContextListenerController.registerWindowContainerListener(iBinder4, windowToken, i18, i17, options);
                                        }
                                    }
                                    this.mWindowManagerServiceExt.hookAddWindowBeforeAttach(i18);
                                    int i20 = !this.mUseBLAST ? 8 : 0;
                                    if (displayContent.mCurrentFocus == null) {
                                        displayContent.mWinAddedSinceNullFocus.add(windowState2);
                                    }
                                    if (excludeWindowTypeFromTapOutTask(i17)) {
                                        displayContent.mTapExcludedWindows.add(windowState2);
                                    }
                                    windowState2.attach();
                                    this.mWindowMap.put(iWindow.asBinder(), windowState2);
                                    windowState2.initAppOpsState();
                                    this.mWindowManagerServiceExt.interceptFloatWindow(this, this.mContext, windowState2, this.mAtmService.isKeyguardLocked(i2), true);
                                    if (!ActivityTaskManagerService.LTW_DISABLE) {
                                        this.mWindowManagerServiceExt.addWindow(windowState2);
                                    }
                                    windowState2.setHiddenWhileSuspended(this.mPmInternal.isPackageSuspended(windowState2.getOwningPackage(), UserHandle.getUserId(windowState2.getOwningUid())));
                                    windowState2.setForceHideNonSystemOverlayWindowIfNeeded(this.mHidingNonSystemOverlayWindows.isEmpty() ? r10 : false);
                                    windowState2.mToken.addWindow(windowState2);
                                    displayPolicy.addWindowLw(windowState2, layoutParams);
                                    displayPolicy.setDropInputModePolicy(windowState2, windowState2.mAttrs);
                                    if (i17 != 3 && activityRecord4 != null) {
                                        activityRecord4.attachStartingWindow(windowState2);
                                        if (ProtoLogCache.WM_DEBUG_STARTING_WINDOW_enabled) {
                                            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, 150351993, 0, (String) null, new Object[]{String.valueOf(activityRecord4), String.valueOf(windowState2)});
                                        }
                                    } else {
                                        if (i17 == 2011 || (windowState2.getAttrs().flags & 16) != 0) {
                                            if (i17 != 2012) {
                                                displayContent.computeImeTarget(r10);
                                                z4 = false;
                                            } else if (i17 == 2013) {
                                                displayContent.mWallpaperController.clearLastWallpaperTimeoutTime();
                                                displayContent.pendingLayoutChanges |= 4;
                                            } else if (windowState2.hasWallpaper()) {
                                                displayContent.pendingLayoutChanges |= 4;
                                            } else if (displayContent.mWallpaperController.isBelowWallpaperTarget(windowState2)) {
                                                displayContent.pendingLayoutChanges |= 4;
                                            }
                                        } else if (this.mWindowManagerServiceExt.setInputMethodWindow(displayContent, windowState2)) {
                                            z4 = false;
                                        }
                                        this.mWindowManagerServiceExt.updateBracketPanelWindow(windowState2, r10);
                                        WindowStateAnimator windowStateAnimator = windowState2.mWinAnimator;
                                        windowStateAnimator.mEnterAnimationPending = r10;
                                        windowStateAnimator.mEnteringAnimation = r10;
                                        if (displayPolicy.areSystemBarsForcedConsumedLw()) {
                                            i20 |= 4;
                                        }
                                        if (displayContent.isInTouchMode()) {
                                            i20 |= 1;
                                        }
                                        activityRecord3 = windowState2.mActivityRecord;
                                        if (activityRecord3 != null || activityRecord3.isClientVisible()) {
                                            i20 |= 2;
                                        }
                                        displayContent.getInputMonitor().setUpdateInputWindowsNeededLw();
                                        if (windowState2.canReceiveKeys()) {
                                            z5 = z4;
                                            z6 = false;
                                        } else {
                                            boolean updateFocusedWindowLocked = updateFocusedWindowLocked(r10, false);
                                            if (updateFocusedWindowLocked) {
                                                z6 = updateFocusedWindowLocked;
                                                z5 = false;
                                            } else {
                                                z5 = z4;
                                                z6 = updateFocusedWindowLocked;
                                            }
                                        }
                                        if (z5) {
                                            displayContent.computeImeTarget(r10);
                                            if (windowState2.isImeOverlayLayeringTarget()) {
                                                dispatchImeTargetOverlayVisibilityChanged(iWindow.asBinder(), windowState2.mAttrs.type, windowState2.isVisibleRequestedOrAdding(), false);
                                            }
                                        }
                                        windowState2.getParent().assignChildLayers();
                                        if (z6) {
                                            displayContent.getInputMonitor().setInputFocusLw(displayContent.mCurrentFocus, false);
                                        }
                                        displayContent.getInputMonitor().updateInputWindowsLw(false);
                                        Slog.i(TAG, "addWindow: New client " + iWindow.asBinder() + " : window=" + windowState2);
                                        z7 = (windowState2.isVisibleRequestedOrAdding() || !displayContent.updateOrientation()) ? false : r10;
                                        if (windowState2.providesDisplayDecorInsets()) {
                                            z7 |= displayPolicy.updateDecorInsetsInfo();
                                        }
                                        if (z7) {
                                            displayContent.sendNewConfiguration();
                                        }
                                        displayContent.getInsetsStateController().updateAboveInsetsState(false);
                                        insetsState.set(windowState2.getCompatInsetsState(), (boolean) r10);
                                        getInsetsSourceControls(windowState2, array);
                                        if (!windowState2.mLayoutAttached) {
                                            rect.set(windowState2.getParentWindow().getFrame());
                                            float f = windowState2.mInvGlobalScale;
                                            if (f != 1.0f) {
                                                rect.scale(f);
                                            }
                                        } else {
                                            rect.set(0, 0, -1, -1);
                                        }
                                        fArr[0] = windowState2.getCompatScaleForClient();
                                        resetPriorityAfterLockedSection();
                                        Binder.restoreCallingIdentity(clearCallingIdentity);
                                        return i20;
                                    }
                                    z4 = r10;
                                    this.mWindowManagerServiceExt.updateBracketPanelWindow(windowState2, r10);
                                    WindowStateAnimator windowStateAnimator2 = windowState2.mWinAnimator;
                                    windowStateAnimator2.mEnterAnimationPending = r10;
                                    windowStateAnimator2.mEnteringAnimation = r10;
                                    if (displayPolicy.areSystemBarsForcedConsumedLw()) {
                                    }
                                    if (displayContent.isInTouchMode()) {
                                    }
                                    activityRecord3 = windowState2.mActivityRecord;
                                    if (activityRecord3 != null) {
                                    }
                                    i20 |= 2;
                                    displayContent.getInputMonitor().setUpdateInputWindowsNeededLw();
                                    if (windowState2.canReceiveKeys()) {
                                    }
                                    if (z5) {
                                    }
                                    windowState2.getParent().assignChildLayers();
                                    if (z6) {
                                    }
                                    displayContent.getInputMonitor().updateInputWindowsLw(false);
                                    Slog.i(TAG, "addWindow: New client " + iWindow.asBinder() + " : window=" + windowState2);
                                    if (windowState2.isVisibleRequestedOrAdding()) {
                                    }
                                    if (windowState2.providesDisplayDecorInsets()) {
                                    }
                                    if (z7) {
                                    }
                                    displayContent.getInsetsStateController().updateAboveInsetsState(false);
                                    insetsState.set(windowState2.getCompatInsetsState(), (boolean) r10);
                                    getInsetsSourceControls(windowState2, array);
                                    if (!windowState2.mLayoutAttached) {
                                    }
                                    fArr[0] = windowState2.getCompatScaleForClient();
                                    resetPriorityAfterLockedSection();
                                    Binder.restoreCallingIdentity(clearCallingIdentity);
                                    return i20;
                                }
                            } else {
                                z2 = true;
                            }
                            z3 = false;
                            r10 = z2;
                            if (z3) {
                            }
                            if (i17 != 2005) {
                            }
                            if (!windowState2.isChildWindow()) {
                                windowType = this.mWindowContextListenerController.getWindowType(iBinder4);
                                Bundle options2 = this.mWindowContextListenerController.getOptions(iBinder4);
                                if (i17 == windowType) {
                                }
                            }
                            this.mWindowManagerServiceExt.hookAddWindowBeforeAttach(i18);
                            if (!this.mUseBLAST) {
                            }
                            if (displayContent.mCurrentFocus == null) {
                            }
                            if (excludeWindowTypeFromTapOutTask(i17)) {
                            }
                            windowState2.attach();
                            this.mWindowMap.put(iWindow.asBinder(), windowState2);
                            windowState2.initAppOpsState();
                            this.mWindowManagerServiceExt.interceptFloatWindow(this, this.mContext, windowState2, this.mAtmService.isKeyguardLocked(i2), true);
                            if (!ActivityTaskManagerService.LTW_DISABLE) {
                            }
                            windowState2.setHiddenWhileSuspended(this.mPmInternal.isPackageSuspended(windowState2.getOwningPackage(), UserHandle.getUserId(windowState2.getOwningUid())));
                            windowState2.setForceHideNonSystemOverlayWindowIfNeeded(this.mHidingNonSystemOverlayWindows.isEmpty() ? r10 : false);
                            windowState2.mToken.addWindow(windowState2);
                            displayPolicy.addWindowLw(windowState2, layoutParams);
                            displayPolicy.setDropInputModePolicy(windowState2, windowState2.mAttrs);
                            if (i17 != 3) {
                            }
                            if (i17 == 2011) {
                            }
                            if (i17 != 2012) {
                            }
                        } else {
                            windowState = windowState3;
                            if (this.mWindowContextListenerController.hasListener(iBinder2)) {
                                IBinder iBinder5 = layoutParams.token;
                                if (iBinder5 == null) {
                                    iBinder5 = iBinder2;
                                }
                                i10 = i5;
                                displayContentOrCreate = displayContentOrCreate;
                                z8 = true;
                                build = new WindowToken.Builder(this, iBinder5, i10).setDisplayContent(displayContentOrCreate).setOwnerCanManageAppTokens(session.mCanAddInternalSystemWindow).setRoundedCornerOverlay(z10).setFromClientToken(true).setOptions(this.mWindowContextListenerController.getOptions(iBinder2)).build();
                            } else {
                                i10 = i5;
                                displayContentOrCreate = displayContentOrCreate;
                                z8 = true;
                                IBinder iBinder6 = layoutParams.token;
                                if (iBinder6 == null) {
                                    iBinder6 = iWindow.asBinder();
                                }
                                build = new WindowToken.Builder(this, iBinder6, i10).setDisplayContent(displayContentOrCreate).setOwnerCanManageAppTokens(session.mCanAddInternalSystemWindow).setRoundedCornerOverlay(z10).build();
                            }
                            windowToken = build;
                            iBinder2 = iBinder2;
                            i12 = i6;
                            i11 = callingUid;
                            z = false;
                            activityRecord2 = null;
                            ActivityRecord activityRecord42 = activityRecord2;
                            DisplayContent displayContent22 = displayContentOrCreate;
                            int i172 = i10;
                            int i182 = i11;
                            IBinder iBinder42 = iBinder2;
                            int i192 = i12;
                            windowState2 = new WindowState(this, session, iWindow, windowToken, windowState, iArr2[0], layoutParams, i, session.mUid, i7, session.mCanAddInternalSystemWindow);
                            if (windowState2.mDeathRecipient != null) {
                            }
                        }
                    } else {
                        iArr2 = iArr;
                        i10 = i5;
                        int i21 = i16;
                        if (i21 >= i9 && i21 <= 99) {
                            ActivityRecord asActivityRecord = windowToken2.asActivityRecord();
                            if (asActivityRecord == null) {
                                if (ProtoLogCache.WM_ERROR_enabled) {
                                    ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 246676969, 0, "Attempted to add window with non-application token .%s Aborting.", new Object[]{String.valueOf(windowToken2)});
                                }
                                resetPriorityAfterLockedSection();
                                return -3;
                            }
                            if (asActivityRecord.getParent() == null) {
                                if (ProtoLogCache.WM_ERROR_enabled) {
                                    ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -853226675, 0, "Attempted to add window with exiting application token .%s Aborting.", new Object[]{String.valueOf(windowToken2)});
                                }
                                resetPriorityAfterLockedSection();
                                return -4;
                            }
                            if (i10 == 3) {
                                if (asActivityRecord.mStartingWindow != null) {
                                    if (ProtoLogCache.WM_ERROR_enabled) {
                                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -167822951, 0, "Attempted to add starting window to token with already existing starting window", (Object[]) null);
                                    }
                                    resetPriorityAfterLockedSection();
                                    return -5;
                                }
                                if (asActivityRecord.mStartingData == null) {
                                    if (ProtoLogCache.WM_ERROR_enabled) {
                                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1804245629, 0, "Attempted to add starting window to token but already cleaned", (Object[]) null);
                                    }
                                    resetPriorityAfterLockedSection();
                                    return -5;
                                }
                            }
                            windowToken = windowToken2;
                            activityRecord2 = asActivityRecord;
                            z = false;
                            iBinder2 = iBinder3;
                            i12 = i6;
                            i11 = callingUid;
                        } else {
                            if (i21 == 2011) {
                                if (windowToken2.windowType != 2011) {
                                    if (ProtoLogCache.WM_ERROR_enabled) {
                                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1949279037, 0, "Attempted to add input method window with bad token %s.  Aborting.", new Object[]{String.valueOf(layoutParams.token)});
                                    }
                                    resetPriorityAfterLockedSection();
                                    return -1;
                                }
                            } else if (i21 == 2031) {
                                if (windowToken2.windowType != 2031) {
                                    if (ProtoLogCache.WM_ERROR_enabled) {
                                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1389772804, 0, "Attempted to add voice interaction window with bad token %s.  Aborting.", new Object[]{String.valueOf(layoutParams.token)});
                                    }
                                    resetPriorityAfterLockedSection();
                                    return -1;
                                }
                            } else if (i21 == 2013) {
                                if (windowToken2.windowType != 2013) {
                                    if (ProtoLogCache.WM_ERROR_enabled) {
                                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1915280162, 0, "Attempted to add wallpaper window with bad token %s.  Aborting.", new Object[]{String.valueOf(layoutParams.token)});
                                    }
                                    resetPriorityAfterLockedSection();
                                    return -1;
                                }
                            } else if (i21 == 2032) {
                                if (windowToken2.windowType != 2032) {
                                    if (ProtoLogCache.WM_ERROR_enabled) {
                                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1976930686, 0, "Attempted to add Accessibility overlay window with bad token %s.  Aborting.", new Object[]{String.valueOf(layoutParams.token)});
                                    }
                                    resetPriorityAfterLockedSection();
                                    return -1;
                                }
                            } else if (i10 == 2005) {
                                i11 = callingUid;
                                boolean doesAddToastWindowRequireToken = doesAddToastWindowRequireToken(layoutParams.packageName, i11, windowState);
                                if (doesAddToastWindowRequireToken && windowToken2.windowType != 2005) {
                                    if (ProtoLogCache.WM_ERROR_enabled) {
                                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 662572728, 0, "Attempted to add a toast window with bad token %s.  Aborting.", new Object[]{String.valueOf(layoutParams.token)});
                                    }
                                    resetPriorityAfterLockedSection();
                                    return -1;
                                }
                                windowToken = windowToken2;
                                z = doesAddToastWindowRequireToken;
                                iBinder2 = iBinder3;
                                i12 = i6;
                                activityRecord2 = null;
                            } else {
                                i11 = callingUid;
                                if (i10 == 2035) {
                                    if (windowToken2.windowType != 2035) {
                                        if (ProtoLogCache.WM_ERROR_enabled) {
                                            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1060365734, 0, "Attempted to add QS dialog window with bad token %s.  Aborting.", new Object[]{String.valueOf(layoutParams.token)});
                                        }
                                        resetPriorityAfterLockedSection();
                                        return -1;
                                    }
                                } else if (windowToken2.asActivityRecord() != null) {
                                    if (ProtoLogCache.WM_ERROR_enabled) {
                                        iBinder2 = iBinder3;
                                        i12 = i6;
                                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 372792199, 1, "Non-null activity for system window of rootType=%d", new Object[]{Long.valueOf(i21)});
                                    } else {
                                        iBinder2 = iBinder3;
                                        i12 = i6;
                                    }
                                    activityRecord = null;
                                    layoutParams.token = null;
                                    windowToken2 = new WindowToken.Builder(this, iWindow.asBinder(), i10).setDisplayContent(displayContentOrCreate).setOwnerCanManageAppTokens(session.mCanAddInternalSystemWindow).build();
                                    windowToken = windowToken2;
                                    activityRecord2 = activityRecord;
                                    z = false;
                                    ActivityRecord activityRecord422 = activityRecord2;
                                    DisplayContent displayContent222 = displayContentOrCreate;
                                    int i1722 = i10;
                                    int i1822 = i11;
                                    IBinder iBinder422 = iBinder2;
                                    int i1922 = i12;
                                    windowState2 = new WindowState(this, session, iWindow, windowToken, windowState, iArr2[0], layoutParams, i, session.mUid, i7, session.mCanAddInternalSystemWindow);
                                    if (windowState2.mDeathRecipient != null) {
                                    }
                                }
                                iBinder2 = iBinder3;
                                i12 = i6;
                                activityRecord = null;
                                windowToken = windowToken2;
                                activityRecord2 = activityRecord;
                                z = false;
                                ActivityRecord activityRecord4222 = activityRecord2;
                                DisplayContent displayContent2222 = displayContentOrCreate;
                                int i17222 = i10;
                                int i18222 = i11;
                                IBinder iBinder4222 = iBinder2;
                                int i19222 = i12;
                                windowState2 = new WindowState(this, session, iWindow, windowToken, windowState, iArr2[0], layoutParams, i, session.mUid, i7, session.mCanAddInternalSystemWindow);
                                if (windowState2.mDeathRecipient != null) {
                                }
                            }
                            iBinder2 = iBinder3;
                            i12 = i6;
                            i11 = callingUid;
                            activityRecord = null;
                            windowToken = windowToken2;
                            activityRecord2 = activityRecord;
                            z = false;
                            ActivityRecord activityRecord42222 = activityRecord2;
                            DisplayContent displayContent22222 = displayContentOrCreate;
                            int i172222 = i10;
                            int i182222 = i11;
                            IBinder iBinder42222 = iBinder2;
                            int i192222 = i12;
                            windowState2 = new WindowState(this, session, iWindow, windowToken, windowState, iArr2[0], layoutParams, i, session.mUid, i7, session.mCanAddInternalSystemWindow);
                            if (windowState2.mDeathRecipient != null) {
                            }
                        }
                        ActivityRecord activityRecord422222 = activityRecord2;
                        DisplayContent displayContent222222 = displayContentOrCreate;
                        int i1722222 = i10;
                        int i1822222 = i11;
                        IBinder iBinder422222 = iBinder2;
                        int i1922222 = i12;
                        windowState2 = new WindowState(this, session, iWindow, windowToken, windowState, iArr2[0], layoutParams, i, session.mUid, i7, session.mCanAddInternalSystemWindow);
                        if (windowState2.mDeathRecipient != null) {
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private boolean unprivilegedAppCanCreateTokenWith(WindowState windowState, int i, int i2, int i3, IBinder iBinder, String str) {
        if (i3 >= 1 && i3 <= 99) {
            if (ProtoLogCache.WM_ERROR_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1113134997, 0, "Attempted to add application window with unknown token %s.  Aborting.", new Object[]{String.valueOf(iBinder)});
            }
            return false;
        }
        if (i3 == 2011) {
            if (ProtoLogCache.WM_ERROR_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -2039580386, 0, "Attempted to add input method window with unknown token %s.  Aborting.", new Object[]{String.valueOf(iBinder)});
            }
            return false;
        }
        if (i3 == 2031) {
            if (ProtoLogCache.WM_ERROR_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -914253865, 0, "Attempted to add voice interaction window with unknown token %s.  Aborting.", new Object[]{String.valueOf(iBinder)});
            }
            return false;
        }
        if (i3 == 2013) {
            if (ProtoLogCache.WM_ERROR_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 424524729, 0, "Attempted to add wallpaper window with unknown token %s.  Aborting.", new Object[]{String.valueOf(iBinder)});
            }
            return false;
        }
        if (i3 == 2035) {
            if (ProtoLogCache.WM_ERROR_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 898863925, 0, "Attempted to add QS dialog window with unknown token %s.  Aborting.", new Object[]{String.valueOf(iBinder)});
            }
            return false;
        }
        if (i3 == 2032) {
            if (ProtoLogCache.WM_ERROR_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1042574499, 0, "Attempted to add Accessibility overlay window with unknown token %s.  Aborting.", new Object[]{String.valueOf(iBinder)});
            }
            return false;
        }
        if (i2 != 2005 || !doesAddToastWindowRequireToken(str, i, windowState)) {
            return true;
        }
        if (ProtoLogCache.WM_ERROR_enabled) {
            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1331177619, 0, "Attempted to add a toast window with unknown token %s.  Aborting.", new Object[]{String.valueOf(iBinder)});
        }
        return false;
    }

    private DisplayContent getDisplayContentOrCreate(int i, IBinder iBinder) {
        WindowToken windowToken;
        if (iBinder != null && (windowToken = this.mRoot.getWindowToken(iBinder)) != null) {
            return windowToken.getDisplayContent();
        }
        return this.mRoot.getDisplayContentOrCreate(i);
    }

    private boolean doesAddToastWindowRequireToken(String str, int i, WindowState windowState) {
        if (windowState != null) {
            ActivityRecord activityRecord = windowState.mActivityRecord;
            return activityRecord != null && activityRecord.mTargetSdk >= 26;
        }
        ApplicationInfo applicationInfo = this.mPmInternal.getApplicationInfo(str, 0L, 1000, UserHandle.getUserId(i));
        if (applicationInfo != null && applicationInfo.uid == i) {
            return applicationInfo.targetSdkVersion >= 26;
        }
        throw new SecurityException("Package " + str + " not in UID " + i);
    }

    public void refreshScreenCaptureDisabled() {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("Only system can call refreshScreenCaptureDisabled.");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRoot.refreshSecureSurfaceState();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeWindow(Session session, IWindow iWindow) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowForClientLocked = windowForClientLocked(session, iWindow, false);
                if (windowForClientLocked != null) {
                    windowForClientLocked.removeIfPossible();
                    resetPriorityAfterLockedSection();
                } else {
                    this.mEmbeddedWindowController.remove(iWindow);
                    resetPriorityAfterLockedSection();
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postWindowRemoveCleanupLocked(WindowState windowState) {
        if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, -622997754, 0, (String) null, new Object[]{String.valueOf(windowState)});
        }
        this.mWindowMap.remove(windowState.mClient.asBinder());
        if (!ActivityTaskManagerService.LTW_DISABLE) {
            this.mWindowManagerServiceExt.removeWindow(windowState);
        }
        DisplayContent displayContent = windowState.getDisplayContent();
        displayContent.getDisplayRotation().markForSeamlessRotation(windowState, false);
        windowState.resetAppOpsState();
        if (displayContent.mCurrentFocus == null) {
            displayContent.mWinRemovedSinceNullFocus.add(windowState);
        }
        this.mEmbeddedWindowController.onWindowRemoved(windowState);
        this.mResizingWindows.remove(windowState);
        updateNonSystemOverlayWindowsVisibilityIfNeeded(windowState, false);
        this.mWindowsChanged = true;
        if (ProtoLogCache.WM_DEBUG_WINDOW_MOVEMENT_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_MOVEMENT, -193782861, 0, (String) null, new Object[]{String.valueOf(windowState)});
        }
        DisplayContent displayContent2 = windowState.getDisplayContent();
        if (displayContent2.mInputMethodWindow == windowState) {
            this.mWindowManagerServiceExt.setInputMethodWindow(displayContent2, null);
        }
        WindowToken windowToken = windowState.mToken;
        if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, -1963461591, 0, (String) null, new Object[]{String.valueOf(windowState), String.valueOf(windowToken)});
        }
        if (windowToken.isEmpty() && !windowToken.mPersistOnEmpty) {
            windowToken.removeImmediately();
        }
        ActivityRecord activityRecord = windowState.mActivityRecord;
        if (activityRecord != null) {
            activityRecord.postWindowRemoveStartingWindowCleanup();
        }
        if (windowState.mAttrs.type == 2013) {
            displayContent.mWallpaperController.clearLastWallpaperTimeoutTime();
            displayContent.pendingLayoutChanges |= 4;
        } else if (displayContent.mWallpaperController.isWallpaperTarget(windowState)) {
            displayContent.pendingLayoutChanges |= 4;
        }
        if (!this.mWindowPlacerLocked.isInLayout()) {
            displayContent.assignWindowLayers(true);
            if (getFocusedWindow() == windowState) {
                this.mFocusMayChange = true;
            }
            this.mWindowPlacerLocked.performSurfacePlacement();
            ActivityRecord activityRecord2 = windowState.mActivityRecord;
            if (activityRecord2 != null) {
                activityRecord2.updateReportedVisibilityLocked();
            }
        }
        displayContent.getInputMonitor().updateInputWindowsLw(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHiddenWhileSuspendedState(ArraySet<String> arraySet, boolean z) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRoot.updateHiddenWhileSuspendedState(arraySet, z);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAppOpsState() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRoot.updateAppOpsState();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void logSurface(WindowState windowState, String str, boolean z) {
        String str2 = "  SURFACE " + str + ": " + windowState;
        if (z) {
            logWithStack(TAG, str2);
        } else {
            Slog.i(TAG, str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void logWithStack(String str, String str2) {
        RuntimeException runtimeException;
        if (WindowManagerDebugConfig.SHOW_STACK_CRAWLS) {
            runtimeException = new RuntimeException();
            runtimeException.fillInStackTrace();
        } else {
            runtimeException = null;
        }
        Slog.i(str, str2, runtimeException);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearTouchableRegion(Session session, IWindow iWindow) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    windowForClientLocked(session, iWindow, false).clearClientTouchableRegion();
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInsetsWindow(Session session, IWindow iWindow, int i, Rect rect, Rect rect2, Region region) {
        int callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowState windowForClientLocked = windowForClientLocked(session, iWindow, false);
                    if (windowForClientLocked != null) {
                        if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
                            Slog.d(TAG, "setInsetsWindow " + windowForClientLocked + ", contentInsets=" + windowForClientLocked.mGivenContentInsets + " -> " + rect + ", visibleInsets=" + windowForClientLocked.mGivenVisibleInsets + " -> " + rect2 + ", touchableRegion=" + windowForClientLocked.mGivenTouchableRegion + " -> " + region + ", touchableInsets " + windowForClientLocked.mTouchableInsets + " -> " + i);
                        }
                        windowForClientLocked.mGivenInsetsPending = false;
                        windowForClientLocked.mGivenContentInsets.set(rect);
                        windowForClientLocked.mGivenVisibleInsets.set(rect2);
                        windowForClientLocked.mGivenTouchableRegion.set(region);
                        windowForClientLocked.mTouchableInsets = i;
                        float f = windowForClientLocked.mGlobalScale;
                        if (f != 1.0f) {
                            windowForClientLocked.mGivenContentInsets.scale(f);
                            windowForClientLocked.mGivenVisibleInsets.scale(windowForClientLocked.mGlobalScale);
                            windowForClientLocked.mGivenTouchableRegion.scale(windowForClientLocked.mGlobalScale);
                        }
                        windowForClientLocked.setDisplayLayoutNeeded();
                        windowForClientLocked.updateSourceFrame(windowForClientLocked.getFrame());
                        this.mWindowPlacerLocked.performSurfacePlacement();
                        windowForClientLocked.getDisplayContent().getInputMonitor().updateInputWindowsLw(true);
                        if (this.mAccessibilityController.hasCallbacks()) {
                            this.mAccessibilityController.onSomeWindowResizedOrMovedWithCallingUid(callingUid, windowForClientLocked.getDisplayContent().getDisplayId());
                        }
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void onRectangleOnScreenRequested(IBinder iBinder, Rect rect) {
        WindowState windowState;
        AccessibilityController.AccessibilityControllerInternalImpl accessibilityControllerInternal = AccessibilityController.getAccessibilityControllerInternal(this);
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (accessibilityControllerInternal.hasWindowManagerEventDispatcher() && (windowState = this.mWindowMap.get(iBinder)) != null) {
                    accessibilityControllerInternal.onRectangleOnScreenRequested(windowState.getDisplayId(), rect);
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public IWindowId getWindowId(IBinder iBinder) {
        WindowState.WindowId windowId;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowState = this.mWindowMap.get(iBinder);
                windowId = windowState != null ? windowState.mWindowId : null;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return windowId;
    }

    public void pokeDrawLock(Session session, IBinder iBinder) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowForClientLocked = windowForClientLocked(session, iBinder, false);
                if (windowForClientLocked != null) {
                    windowForClientLocked.pokeDrawLockLw(this.mDrawLockTimeoutMillis);
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    private boolean hasStatusBarPermission(int i, int i2) {
        return this.mContext.checkPermission("android.permission.STATUS_BAR", i, i2) == 0;
    }

    public boolean cancelDraw(Session session, IWindow iWindow) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowForClientLocked = windowForClientLocked(session, iWindow, false);
                if (windowForClientLocked == null) {
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean cancelAndRedraw = windowForClientLocked.cancelAndRedraw();
                resetPriorityAfterLockedSection();
                return cancelAndRedraw;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:327:0x00ff, code lost:
    
        if (r15 == null) goto L359;
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x0101, code lost:
    
        r36 = r7;
        r19 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x0107, code lost:
    
        if (r14.length != r15.length) goto L354;
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x0109, code lost:
    
        r5 = r14.length;
        r7 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x010b, code lost:
    
        if (r7 >= r5) goto L361;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x010d, code lost:
    
        r21 = r5;
        r22 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x011d, code lost:
    
        if (r14[r7].getWindowType() != r15[r7].getWindowType()) goto L357;
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x011f, code lost:
    
        r7 = r7 + 1;
        r5 = r21;
        r14 = r22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x012d, code lost:
    
        throw new java.lang.IllegalArgumentException("Insets override types can not be changed after the window is added.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x012e, code lost:
    
        continue;
     */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0325 A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0333  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x033b A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:121:0x035b A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:124:0x036e A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:127:0x03b4  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x03bb A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:138:0x03d1 A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0419 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:167:0x046a A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:170:0x0474 A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0511 A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:184:0x0526  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x052b A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0537 A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:193:0x0541 A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0556 A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:201:0x0564 A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:204:0x056d A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0575 A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:210:0x057e  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x058c A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0598 A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:221:0x05b8 A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:224:0x05df  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x05e8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0604  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x0609 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:237:0x061d A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:249:0x064b A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:252:0x065a A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:256:0x0606  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x05e1  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x0579  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x0528  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x04a5 A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:285:0x03b6  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x03a8  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x0335  */
    /* JADX WARN: Removed duplicated region for block: B:309:0x0166  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0195 A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x01a3 A[Catch: all -> 0x0665, TryCatch #1 {all -> 0x0665, blocks: (B:8:0x0037, B:10:0x003d, B:14:0x0042, B:16:0x0046, B:17:0x0051, B:19:0x0057, B:22:0x005f, B:24:0x007b, B:26:0x0080, B:28:0x00a5, B:31:0x00ac, B:33:0x00b6, B:35:0x00ba, B:39:0x0145, B:43:0x0169, B:46:0x0172, B:48:0x0178, B:49:0x017e, B:51:0x0184, B:53:0x0188, B:55:0x018e, B:56:0x0191, B:58:0x0195, B:59:0x019e, B:61:0x01a3, B:64:0x01b0, B:66:0x01b3, B:68:0x01bb, B:70:0x01bf, B:76:0x0251, B:78:0x0288, B:79:0x02c5, B:81:0x02d8, B:82:0x02dc, B:84:0x02eb, B:86:0x02ef, B:88:0x02f3, B:90:0x02fb, B:95:0x0309, B:101:0x0317, B:103:0x031b, B:108:0x0325, B:111:0x032e, B:114:0x0336, B:116:0x033b, B:118:0x033f, B:119:0x0346, B:121:0x035b, B:122:0x0367, B:124:0x036e, B:125:0x03ac, B:128:0x03b7, B:130:0x03bb, B:132:0x03bf, B:134:0x03c6, B:138:0x03d1, B:140:0x03d7, B:142:0x03db, B:144:0x03df, B:145:0x03ff, B:147:0x0403, B:148:0x040d, B:152:0x041b, B:154:0x0425, B:158:0x042c, B:160:0x0438, B:161:0x0459, B:162:0x045c, B:165:0x0462, B:167:0x046a, B:170:0x0474, B:173:0x0485, B:175:0x048d, B:178:0x0499, B:180:0x0511, B:182:0x0522, B:186:0x052b, B:188:0x0531, B:190:0x0537, B:191:0x053d, B:193:0x0541, B:194:0x0546, B:196:0x0556, B:198:0x055a, B:199:0x0560, B:201:0x0564, B:202:0x0567, B:204:0x056d, B:205:0x056f, B:207:0x0575, B:212:0x0582, B:215:0x058c, B:216:0x0594, B:218:0x0598, B:219:0x05b4, B:221:0x05b8, B:222:0x05d6, B:225:0x05e2, B:228:0x05ea, B:229:0x05fa, B:234:0x060b, B:237:0x061d, B:240:0x0626, B:242:0x062c, B:244:0x0632, B:245:0x0635, B:246:0x0638, B:247:0x0644, B:249:0x064b, B:252:0x065a, B:253:0x065d, B:260:0x0519, B:265:0x04a5, B:268:0x04b3, B:270:0x04b9, B:271:0x04c7, B:273:0x04cb, B:276:0x04fe, B:279:0x0503, B:280:0x0506, B:281:0x0507, B:293:0x02f7, B:294:0x02a2, B:296:0x01cb, B:298:0x01da, B:299:0x01de, B:301:0x01f8, B:302:0x022d, B:308:0x016d, B:311:0x00c5, B:313:0x00c9, B:315:0x00cd, B:317:0x00d1, B:319:0x00e1, B:325:0x012e, B:328:0x0101, B:330:0x0109, B:332:0x010d, B:334:0x011f, B:336:0x0126, B:337:0x012d, B:340:0x0135, B:341:0x013c, B:345:0x013d, B:346:0x0144, B:348:0x023c, B:349:0x0243, B:350:0x0244, B:351:0x024b, B:355:0x004b, B:275:0x04e1), top: B:7:0x0037, inners: #0, #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int relayoutWindow(Session session, IWindow iWindow, WindowManager.LayoutParams layoutParams, int i, int i2, int i3, int i4, int i5, int i6, ClientWindowFrames clientWindowFrames, MergedConfiguration mergedConfiguration, SurfaceControl surfaceControl, InsetsState insetsState, InsetsSourceControl.Array array, Bundle bundle) {
        int i7;
        int i8;
        int i9;
        boolean z;
        boolean z2;
        DisplayContent displayContent;
        int i10;
        String sb;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        DisplayContent displayContent2;
        int i11;
        boolean z9;
        int i12;
        boolean z10;
        boolean z11;
        ActivityRecord activityRecord;
        boolean updateOrientation;
        ActivityRecord activityRecord2;
        boolean z12;
        ActivityRecord activityRecord3;
        WindowSurfaceController windowSurfaceController;
        InsetsFrameProvider[] insetsFrameProviderArr;
        int i13;
        int copyFrom;
        boolean z13;
        ActivityRecord activityRecord4;
        int i14;
        int i15;
        ActivityRecord activityRecord5;
        int i16;
        if (array != null) {
            array.set((InsetsSourceControl[]) null);
        }
        int callingPid = Binder.getCallingPid();
        int callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowForClientLocked = windowForClientLocked(session, iWindow, false);
                if (windowForClientLocked == null) {
                    resetPriorityAfterLockedSection();
                    return 0;
                }
                int i17 = windowForClientLocked.mRelayoutSeq;
                if (i17 < i5) {
                    windowForClientLocked.mRelayoutSeq = i5;
                } else if (i17 > i5) {
                    resetPriorityAfterLockedSection();
                    return 0;
                }
                int i18 = (!windowForClientLocked.cancelAndRedraw() || windowForClientLocked.mPrepareSyncSeqId > i6) ? 0 : 16;
                int extractConfigInfoAndRealFlags = this.mWindowManagerServiceExt.extractConfigInfoAndRealFlags(this.mWindowManagerServiceExt.extractSwitchPhysicalDisplayFlag(i4, windowForClientLocked), windowForClientLocked);
                DisplayContent displayContent3 = windowForClientLocked.getDisplayContent();
                DisplayPolicy displayPolicy = displayContent3.getDisplayPolicy();
                WindowStateAnimator windowStateAnimator = windowForClientLocked.mWinAnimator;
                if (i3 != 8) {
                    windowForClientLocked.setRequestedSize(i, i2);
                }
                if (layoutParams != null) {
                    displayPolicy.adjustWindowParamsLw(windowForClientLocked, layoutParams);
                    layoutParams.flags = sanitizeFlagSlippery(layoutParams.flags, windowForClientLocked.getName(), callingUid, callingPid);
                    layoutParams.inputFeatures = sanitizeSpyWindow(layoutParams.inputFeatures, windowForClientLocked.getName(), callingUid, callingPid);
                    int i19 = (layoutParams.systemUiVisibility | layoutParams.subtreeSystemUiVisibility) & 134152192;
                    if (i19 != 0 && !hasStatusBarPermission(callingPid, callingUid)) {
                        i19 = 0;
                    }
                    windowForClientLocked.mDisableFlags = i19;
                    WindowManager.LayoutParams layoutParams2 = windowForClientLocked.mAttrs;
                    if (layoutParams2.type != layoutParams.type) {
                        throw new IllegalArgumentException("Window type can not be changed after the window is added.");
                    }
                    InsetsFrameProvider[] insetsFrameProviderArr2 = layoutParams2.providedInsets;
                    if (insetsFrameProviderArr2 == null) {
                        if (layoutParams.providedInsets != null) {
                        }
                        i7 = i18;
                        WindowManager.LayoutParams layoutParams3 = windowForClientLocked.mAttrs;
                        int i20 = layoutParams3.flags ^ layoutParams.flags;
                        i13 = layoutParams3.privateFlags ^ layoutParams.privateFlags;
                        copyFrom = layoutParams3.copyFrom(layoutParams);
                        windowForClientLocked.getWrapper().getExtImpl().setLayoutFullscreenInEmbeddingIfNeed();
                        z13 = (copyFrom & 1) == 0;
                        if (!z13 || (copyFrom & 16384) != 0) {
                            windowForClientLocked.mLayoutNeeded = true;
                        }
                        z = (z13 || !windowForClientLocked.providesDisplayDecorInsets()) ? false : displayPolicy.updateDecorInsetsInfo();
                        activityRecord4 = windowForClientLocked.mActivityRecord;
                        if (activityRecord4 != null && ((i20 & 524288) != 0 || (i20 & 4194304) != 0)) {
                            activityRecord4.checkKeyguardFlagsChanged();
                        }
                        if ((i13 & 524288) != 0) {
                            updateNonSystemOverlayWindowsVisibilityIfNeeded(windowForClientLocked, windowForClientLocked.mWinAnimator.getShown());
                        }
                        if ((131072 & copyFrom) != 0) {
                            windowStateAnimator.setColorSpaceAgnosticLocked((windowForClientLocked.mAttrs.privateFlags & 16777216) != 0);
                        }
                        if (displayContent3.mDwpcHelper.hasController() || (activityRecord5 = windowForClientLocked.mActivityRecord) == null) {
                            i14 = copyFrom;
                        } else {
                            i14 = copyFrom;
                            boolean z14 = windowForClientLocked.mRelayoutCalled;
                            if (!z14 || i20 != 0 || i13 != 0) {
                                if (z14) {
                                    i15 = i20;
                                    i16 = i15;
                                } else {
                                    i15 = i20;
                                    i16 = windowForClientLocked.mAttrs.flags;
                                }
                                if (!z14) {
                                    i13 = windowForClientLocked.mAttrs.privateFlags;
                                }
                                int i21 = i13;
                                DisplayWindowPolicyControllerHelper displayWindowPolicyControllerHelper = displayContent3.mDwpcHelper;
                                ActivityInfo activityInfo = activityRecord5.info;
                                WindowManager.LayoutParams layoutParams4 = windowForClientLocked.mAttrs;
                                if (!displayWindowPolicyControllerHelper.keepActivityOnWindowFlagsChanged(activityInfo, i16, i21, layoutParams4.flags, layoutParams4.privateFlags)) {
                                    H h = this.mH;
                                    h.sendMessage(h.obtainMessage(65, windowForClientLocked.mActivityRecord.getTask()));
                                    Slog.w(TAG, "Activity " + windowForClientLocked.mActivityRecord + " window flag changed, can't remain on display " + displayContent3.getDisplayId());
                                    resetPriorityAfterLockedSection();
                                    return 0;
                                }
                                i8 = i14;
                                i9 = i15;
                            }
                        }
                        i15 = i20;
                        i8 = i14;
                        i9 = i15;
                    }
                    if (insetsFrameProviderArr2 == null || (insetsFrameProviderArr = layoutParams.providedInsets) == null || insetsFrameProviderArr2.length != insetsFrameProviderArr.length) {
                        throw new IllegalArgumentException("Insets amount can not be changed after the window is added.");
                    }
                    int length = insetsFrameProviderArr.length;
                    int i22 = 0;
                    while (i22 < length) {
                        if (!windowForClientLocked.mAttrs.providedInsets[i22].idEquals(layoutParams.providedInsets[i22])) {
                            throw new IllegalArgumentException("Insets ID can not be changed after the window is added.");
                        }
                        InsetsFrameProvider.InsetsSizeOverride[] insetsSizeOverrides = windowForClientLocked.mAttrs.providedInsets[i22].getInsetsSizeOverrides();
                        InsetsFrameProvider.InsetsSizeOverride[] insetsSizeOverrides2 = layoutParams.providedInsets[i22].getInsetsSizeOverrides();
                        if (insetsSizeOverrides == null && insetsSizeOverrides2 == null) {
                            int i23 = i18;
                            int i24 = length;
                            i22++;
                            length = i24;
                            i18 = i23;
                        }
                        throw new IllegalArgumentException("Insets override types can not be changed after the window is added.");
                    }
                    i7 = i18;
                    WindowManager.LayoutParams layoutParams32 = windowForClientLocked.mAttrs;
                    int i202 = layoutParams32.flags ^ layoutParams.flags;
                    i13 = layoutParams32.privateFlags ^ layoutParams.privateFlags;
                    copyFrom = layoutParams32.copyFrom(layoutParams);
                    windowForClientLocked.getWrapper().getExtImpl().setLayoutFullscreenInEmbeddingIfNeed();
                    if ((copyFrom & 1) == 0) {
                    }
                    if (!z13) {
                    }
                    windowForClientLocked.mLayoutNeeded = true;
                    if (z13) {
                    }
                    activityRecord4 = windowForClientLocked.mActivityRecord;
                    if (activityRecord4 != null) {
                        activityRecord4.checkKeyguardFlagsChanged();
                    }
                    if ((i13 & 524288) != 0) {
                    }
                    if ((131072 & copyFrom) != 0) {
                    }
                    if (displayContent3.mDwpcHelper.hasController()) {
                    }
                    i14 = copyFrom;
                    i15 = i202;
                    i8 = i14;
                    i9 = i15;
                } else {
                    i7 = i18;
                    i8 = 0;
                    i9 = 0;
                    z = false;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Relayout ");
                sb2.append(windowForClientLocked);
                sb2.append(": viewVisibility=");
                sb2.append(i3);
                sb2.append(", oldvis=");
                sb2.append(windowForClientLocked.mViewVisibility);
                sb2.append(", req=");
                sb2.append(i);
                sb2.append("x");
                sb2.append(i2);
                if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
                    StringBuilder sb3 = new StringBuilder();
                    z2 = z;
                    sb3.append(" ");
                    sb3.append(windowForClientLocked.mAttrs);
                    sb = sb3.toString();
                    i10 = extractConfigInfoAndRealFlags;
                    displayContent = displayContent3;
                } else {
                    z2 = z;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(", vsysui=");
                    displayContent = displayContent3;
                    i10 = extractConfigInfoAndRealFlags;
                    sb4.append(ViewDebug.flagsToString(View.class, "mSystemUiVisibility", windowForClientLocked.mAttrs.subtreeSystemUiVisibility));
                    sb = sb4.toString();
                }
                sb2.append(sb);
                Slog.v(TAG, sb2.toString());
                this.mWindowManagerServiceExt.isFingerPrintDimLayerRequestHorizontalLayout(windowForClientLocked, i, i2, i3);
                if ((i8 & 128) != 0) {
                    windowStateAnimator.mAlpha = layoutParams.alpha;
                }
                windowForClientLocked.setWindowScale(windowForClientLocked.mRequestedWidth, windowForClientLocked.mRequestedHeight);
                Rect rect = windowForClientLocked.mAttrs.surfaceInsets;
                if (rect.left != 0 || rect.top != 0 || rect.right != 0 || rect.bottom != 0) {
                    windowStateAnimator.setOpaqueLocked(false);
                }
                int i25 = windowForClientLocked.mViewVisibility;
                boolean z15 = (i25 == 4 || i25 == 8) && i3 == 0;
                if ((131080 & i9) == 0 && !z15) {
                    z3 = false;
                    if (i25 == i3 && (i9 & 8) == 0 && windowForClientLocked.mRelayoutCalled) {
                        z4 = false;
                        z5 = (i25 == i3 && windowForClientLocked.hasWallpaper()) | ((1048576 & i9) == 0);
                        if ((i9 & 8192) != 0 && (windowSurfaceController = windowStateAnimator.mSurfaceController) != null) {
                            windowSurfaceController.setSecure(windowForClientLocked.isSecureLocked());
                        }
                        boolean isVisible = windowForClientLocked.isVisible();
                        windowForClientLocked.mRelayoutCalled = true;
                        windowForClientLocked.mInRelayout = true;
                        if (windowForClientLocked.toString().contains(IWindowManagerServiceExt.SPECIAL_HANDLING_WIN)) {
                            windowForClientLocked.getWrapper().getExtImpl().setWindowRelayoutFlag(true);
                        }
                        windowForClientLocked.setViewVisibility(i3);
                        if (ProtoLogCache.WM_DEBUG_SCREEN_ON_enabled) {
                            z6 = z3;
                            z7 = z4;
                        } else {
                            z6 = z3;
                            z7 = z4;
                            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_SCREEN_ON, -754503024, 20, (String) null, new Object[]{String.valueOf(windowForClientLocked), Long.valueOf(i25), Long.valueOf(i3), String.valueOf(new RuntimeException().fillInStackTrace())});
                        }
                        windowForClientLocked.setDisplayLayoutNeeded();
                        windowForClientLocked.mGivenInsetsPending = (i10 & 1) == 0;
                        z8 = i3 != 0 && ((activityRecord3 = windowForClientLocked.mActivityRecord) == null || windowForClientLocked.mAttrs.type == 3 || activityRecord3.isClientVisible());
                        if (z8 && windowStateAnimator.hasSurface() && !windowForClientLocked.mAnimatingExit) {
                            if (WindowManagerDebugConfig.DEBUG_VISIBILITY) {
                                Slog.i(TAG, "Relayout invis " + windowForClientLocked + ": mAnimatingExit=" + windowForClientLocked.mAnimatingExit);
                            }
                            i11 = i7 | 2;
                            if (z5) {
                                displayContent2 = displayContent;
                                displayContent2.mWallpaperController.adjustWallpaperWindows();
                            } else {
                                displayContent2 = displayContent;
                            }
                            tryStartExitingAnimation(windowForClientLocked, windowStateAnimator);
                        } else {
                            displayContent2 = displayContent;
                            i11 = i7;
                        }
                        if (z8 && surfaceControl != null) {
                            this.mWindowManagerServiceExt.clearSavedSurfaceIfNeeded(windowForClientLocked, this.mDestroySurface, 65536, true);
                            try {
                                i11 = createSurfaceControl(surfaceControl, i11, windowForClientLocked, windowStateAnimator);
                            } catch (Exception e) {
                                displayContent2.getInputMonitor().updateInputWindowsLw(true);
                                if (ProtoLogCache.WM_ERROR_enabled) {
                                    ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1750206390, 0, "Exception thrown when creating surface for client %s (%s). %s", new Object[]{String.valueOf(iWindow), String.valueOf(windowForClientLocked.mAttrs.getTitle()), String.valueOf(e)});
                                }
                                Binder.restoreCallingIdentity(clearCallingIdentity);
                                resetPriorityAfterLockedSection();
                                return 0;
                            }
                        }
                        if (!this.mWindowManagerServiceExt.shouldCancelRelayout(windowForClientLocked, i25, i3)) {
                            this.mWindowPlacerLocked.performSurfacePlacement(true);
                        }
                        if (!z8) {
                            Trace.traceBegin(32L, "relayoutWindow: viewVisibility_1");
                            int relayoutVisibleWindow = windowForClientLocked.relayoutVisibleWindow(i11);
                            boolean z16 = (relayoutVisibleWindow & 1) != 0 ? true : z7;
                            z9 = (windowForClientLocked.mAttrs.type == 2011 && this.mWindowManagerServiceExt.setInputMethodWindow(displayContent2, windowForClientLocked)) ? true : z6;
                            windowForClientLocked.adjustStartingWindowFlags();
                            Trace.traceEnd(32L);
                            boolean z17 = z16;
                            i12 = relayoutVisibleWindow;
                            z10 = z17;
                        } else {
                            Trace.traceBegin(32L, "relayoutWindow: viewVisibility_2");
                            windowStateAnimator.mEnterAnimationPending = false;
                            windowStateAnimator.mEnteringAnimation = false;
                            if (surfaceControl != null) {
                                if (i3 == 0 && windowStateAnimator.hasSurface()) {
                                    Trace.traceBegin(32L, "relayoutWindow: getSurface");
                                    windowStateAnimator.mSurfaceController.getSurfaceControl(surfaceControl);
                                    Trace.traceEnd(32L);
                                } else {
                                    if (WindowManagerDebugConfig.DEBUG_VISIBILITY) {
                                        Slog.i(TAG, "Releasing surface in: " + windowForClientLocked);
                                    }
                                    try {
                                        Trace.traceBegin(32L, "wmReleaseOutSurface_" + ((Object) windowForClientLocked.mAttrs.getTitle()));
                                        surfaceControl.release();
                                        Trace.traceEnd(32L);
                                    } finally {
                                        Trace.traceEnd(32L);
                                    }
                                }
                            }
                            Trace.traceEnd(32L);
                            z9 = z6;
                            i12 = i11;
                            z10 = z7;
                        }
                        if ((!z10 || this.mWindowManagerServiceExt.changeFocusForce(windowForClientLocked)) && updateFocusedWindowLocked(0, true)) {
                            z9 = false;
                        }
                        z11 = (i12 & 1) == 0;
                        if (z9) {
                            displayContent2.computeImeTarget(true);
                            if (z11) {
                                displayContent2.assignWindowLayers(false);
                            }
                        }
                        if (z5) {
                            displayContent2.pendingLayoutChanges |= 4;
                        }
                        activityRecord = windowForClientLocked.mActivityRecord;
                        if (activityRecord != null) {
                            displayContent2.mUnknownAppVisibilityController.notifyRelayouted(activityRecord);
                        }
                        Trace.traceBegin(32L, "relayoutWindow: updateOrientation");
                        updateOrientation = z2 | displayContent2.updateOrientation();
                        Trace.traceEnd(32L);
                        if (z11 && windowForClientLocked.mIsWallpaper) {
                            displayContent2.mWallpaperController.updateWallpaperOffset(windowForClientLocked, false);
                        }
                        activityRecord2 = windowForClientLocked.mActivityRecord;
                        if (activityRecord2 != null) {
                            activityRecord2.updateReportedVisibilityLocked();
                        }
                        if (displayPolicy.areSystemBarsForcedConsumedLw()) {
                            i12 |= 8;
                        }
                        if (windowForClientLocked.isGoneForLayout()) {
                            z12 = false;
                            windowForClientLocked.mResizedWhileGone = false;
                        } else {
                            z12 = false;
                        }
                        if (clientWindowFrames != null && mergedConfiguration != null) {
                            windowForClientLocked.fillClientWindowFramesAndConfiguration(clientWindowFrames, mergedConfiguration, z12, z8);
                            windowForClientLocked.onResizeHandled();
                        }
                        if (insetsState != null) {
                            insetsState.set(windowForClientLocked.getCompatInsetsState(), true);
                        }
                        if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
                            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_FOCUS, 95902367, 12, (String) null, new Object[]{String.valueOf(windowForClientLocked), Boolean.valueOf(z10)});
                        }
                        if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
                            Slog.v(TAG, "Relayout complete " + windowForClientLocked + ": outFrames=" + clientWindowFrames);
                        }
                        windowForClientLocked.mInRelayout = false;
                        boolean z18 = windowForClientLocked.isVisible() == isVisible;
                        if (windowForClientLocked.isImeOverlayLayeringTarget() && z18) {
                            dispatchImeTargetOverlayVisibilityChanged(iWindow.asBinder(), windowForClientLocked.mAttrs.type, windowForClientLocked.isVisible(), false);
                        }
                        if ((windowForClientLocked.getDisplayContent().getImeInputTarget() != windowForClientLocked) && z18) {
                            dispatchImeInputTargetVisibilityChanged(windowForClientLocked.mClient.asBinder(), windowForClientLocked.isVisible(), false);
                        }
                        if (bundle != null) {
                            if (windowForClientLocked.useBLASTSync() && i3 == 0 && windowForClientLocked.mSyncSeqId > i6) {
                                r7 = windowForClientLocked.shouldSyncWithBuffers() ? windowForClientLocked.mSyncSeqId : -1;
                                windowForClientLocked.markRedrawForSyncReported();
                            }
                            bundle.putInt("seqid", r7);
                            bundle.putFloat("WindowGlobalScale", windowForClientLocked.mGlobalScale);
                        }
                        this.mWindowManagerServiceExt.addSplitScreenImmersiveFlagIfNeed(windowForClientLocked, bundle);
                        if (updateOrientation) {
                            Trace.traceBegin(32L, "relayoutWindow: postNewConfigurationToHandler");
                            displayContent2.sendNewConfiguration();
                        }
                        if (array != null) {
                            getInsetsSourceControls(windowForClientLocked, array);
                        }
                        resetPriorityAfterLockedSection();
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return i12;
                    }
                    z4 = true;
                    z5 = (i25 == i3 && windowForClientLocked.hasWallpaper()) | ((1048576 & i9) == 0);
                    if ((i9 & 8192) != 0) {
                        windowSurfaceController.setSecure(windowForClientLocked.isSecureLocked());
                    }
                    boolean isVisible2 = windowForClientLocked.isVisible();
                    windowForClientLocked.mRelayoutCalled = true;
                    windowForClientLocked.mInRelayout = true;
                    if (windowForClientLocked.toString().contains(IWindowManagerServiceExt.SPECIAL_HANDLING_WIN)) {
                    }
                    windowForClientLocked.setViewVisibility(i3);
                    if (ProtoLogCache.WM_DEBUG_SCREEN_ON_enabled) {
                    }
                    windowForClientLocked.setDisplayLayoutNeeded();
                    windowForClientLocked.mGivenInsetsPending = (i10 & 1) == 0;
                    if (i3 != 0) {
                    }
                    if (z8) {
                    }
                    displayContent2 = displayContent;
                    i11 = i7;
                    if (z8) {
                        this.mWindowManagerServiceExt.clearSavedSurfaceIfNeeded(windowForClientLocked, this.mDestroySurface, 65536, true);
                        i11 = createSurfaceControl(surfaceControl, i11, windowForClientLocked, windowStateAnimator);
                    }
                    if (!this.mWindowManagerServiceExt.shouldCancelRelayout(windowForClientLocked, i25, i3)) {
                    }
                    if (!z8) {
                    }
                    if (!z10) {
                    }
                    z9 = false;
                    if ((i12 & 1) == 0) {
                    }
                    if (z9) {
                    }
                    if (z5) {
                    }
                    activityRecord = windowForClientLocked.mActivityRecord;
                    if (activityRecord != null) {
                    }
                    Trace.traceBegin(32L, "relayoutWindow: updateOrientation");
                    updateOrientation = z2 | displayContent2.updateOrientation();
                    Trace.traceEnd(32L);
                    if (z11) {
                        displayContent2.mWallpaperController.updateWallpaperOffset(windowForClientLocked, false);
                    }
                    activityRecord2 = windowForClientLocked.mActivityRecord;
                    if (activityRecord2 != null) {
                    }
                    if (displayPolicy.areSystemBarsForcedConsumedLw()) {
                    }
                    if (windowForClientLocked.isGoneForLayout()) {
                    }
                    if (clientWindowFrames != null) {
                        windowForClientLocked.fillClientWindowFramesAndConfiguration(clientWindowFrames, mergedConfiguration, z12, z8);
                        windowForClientLocked.onResizeHandled();
                    }
                    if (insetsState != null) {
                    }
                    if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
                    }
                    if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
                    }
                    windowForClientLocked.mInRelayout = false;
                    if (windowForClientLocked.isVisible() == isVisible2) {
                    }
                    if (windowForClientLocked.isImeOverlayLayeringTarget()) {
                        dispatchImeTargetOverlayVisibilityChanged(iWindow.asBinder(), windowForClientLocked.mAttrs.type, windowForClientLocked.isVisible(), false);
                    }
                    if (windowForClientLocked.getDisplayContent().getImeInputTarget() != windowForClientLocked) {
                        dispatchImeInputTargetVisibilityChanged(windowForClientLocked.mClient.asBinder(), windowForClientLocked.isVisible(), false);
                    }
                    if (bundle != null) {
                    }
                    this.mWindowManagerServiceExt.addSplitScreenImmersiveFlagIfNeed(windowForClientLocked, bundle);
                    if (updateOrientation) {
                    }
                    if (array != null) {
                    }
                    resetPriorityAfterLockedSection();
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return i12;
                }
                z3 = true;
                if (i25 == i3) {
                    z4 = false;
                    z5 = (i25 == i3 && windowForClientLocked.hasWallpaper()) | ((1048576 & i9) == 0);
                    if ((i9 & 8192) != 0) {
                    }
                    boolean isVisible22 = windowForClientLocked.isVisible();
                    windowForClientLocked.mRelayoutCalled = true;
                    windowForClientLocked.mInRelayout = true;
                    if (windowForClientLocked.toString().contains(IWindowManagerServiceExt.SPECIAL_HANDLING_WIN)) {
                    }
                    windowForClientLocked.setViewVisibility(i3);
                    if (ProtoLogCache.WM_DEBUG_SCREEN_ON_enabled) {
                    }
                    windowForClientLocked.setDisplayLayoutNeeded();
                    windowForClientLocked.mGivenInsetsPending = (i10 & 1) == 0;
                    if (i3 != 0) {
                    }
                    if (z8) {
                    }
                    displayContent2 = displayContent;
                    i11 = i7;
                    if (z8) {
                    }
                    if (!this.mWindowManagerServiceExt.shouldCancelRelayout(windowForClientLocked, i25, i3)) {
                    }
                    if (!z8) {
                    }
                    if (!z10) {
                    }
                    z9 = false;
                    if ((i12 & 1) == 0) {
                    }
                    if (z9) {
                    }
                    if (z5) {
                    }
                    activityRecord = windowForClientLocked.mActivityRecord;
                    if (activityRecord != null) {
                    }
                    Trace.traceBegin(32L, "relayoutWindow: updateOrientation");
                    updateOrientation = z2 | displayContent2.updateOrientation();
                    Trace.traceEnd(32L);
                    if (z11) {
                    }
                    activityRecord2 = windowForClientLocked.mActivityRecord;
                    if (activityRecord2 != null) {
                    }
                    if (displayPolicy.areSystemBarsForcedConsumedLw()) {
                    }
                    if (windowForClientLocked.isGoneForLayout()) {
                    }
                    if (clientWindowFrames != null) {
                    }
                    if (insetsState != null) {
                    }
                    if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
                    }
                    if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
                    }
                    windowForClientLocked.mInRelayout = false;
                    if (windowForClientLocked.isVisible() == isVisible22) {
                    }
                    if (windowForClientLocked.isImeOverlayLayeringTarget()) {
                    }
                    if (windowForClientLocked.getDisplayContent().getImeInputTarget() != windowForClientLocked) {
                    }
                    if (bundle != null) {
                    }
                    this.mWindowManagerServiceExt.addSplitScreenImmersiveFlagIfNeed(windowForClientLocked, bundle);
                    if (updateOrientation) {
                    }
                    if (array != null) {
                    }
                    resetPriorityAfterLockedSection();
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return i12;
                }
                z4 = true;
                z5 = (i25 == i3 && windowForClientLocked.hasWallpaper()) | ((1048576 & i9) == 0);
                if ((i9 & 8192) != 0) {
                }
                boolean isVisible222 = windowForClientLocked.isVisible();
                windowForClientLocked.mRelayoutCalled = true;
                windowForClientLocked.mInRelayout = true;
                if (windowForClientLocked.toString().contains(IWindowManagerServiceExt.SPECIAL_HANDLING_WIN)) {
                }
                windowForClientLocked.setViewVisibility(i3);
                if (ProtoLogCache.WM_DEBUG_SCREEN_ON_enabled) {
                }
                windowForClientLocked.setDisplayLayoutNeeded();
                windowForClientLocked.mGivenInsetsPending = (i10 & 1) == 0;
                if (i3 != 0) {
                }
                if (z8) {
                }
                displayContent2 = displayContent;
                i11 = i7;
                if (z8) {
                }
                if (!this.mWindowManagerServiceExt.shouldCancelRelayout(windowForClientLocked, i25, i3)) {
                }
                if (!z8) {
                }
                if (!z10) {
                }
                z9 = false;
                if ((i12 & 1) == 0) {
                }
                if (z9) {
                }
                if (z5) {
                }
                activityRecord = windowForClientLocked.mActivityRecord;
                if (activityRecord != null) {
                }
                Trace.traceBegin(32L, "relayoutWindow: updateOrientation");
                updateOrientation = z2 | displayContent2.updateOrientation();
                Trace.traceEnd(32L);
                if (z11) {
                }
                activityRecord2 = windowForClientLocked.mActivityRecord;
                if (activityRecord2 != null) {
                }
                if (displayPolicy.areSystemBarsForcedConsumedLw()) {
                }
                if (windowForClientLocked.isGoneForLayout()) {
                }
                if (clientWindowFrames != null) {
                }
                if (insetsState != null) {
                }
                if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
                }
                if (WindowManagerDebugConfig.DEBUG_LAYOUT) {
                }
                windowForClientLocked.mInRelayout = false;
                if (windowForClientLocked.isVisible() == isVisible222) {
                }
                if (windowForClientLocked.isImeOverlayLayeringTarget()) {
                }
                if (windowForClientLocked.getDisplayContent().getImeInputTarget() != windowForClientLocked) {
                }
                if (bundle != null) {
                }
                this.mWindowManagerServiceExt.addSplitScreenImmersiveFlagIfNeed(windowForClientLocked, bundle);
                if (updateOrientation) {
                }
                if (array != null) {
                }
                resetPriorityAfterLockedSection();
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return i12;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void getInsetsSourceControls(WindowState windowState, InsetsSourceControl.Array array) {
        InsetsSourceControl[] controlsForDispatch = windowState.getDisplayContent().getInsetsStateController().getControlsForDispatch(windowState);
        if (controlsForDispatch != null) {
            int length = controlsForDispatch.length;
            InsetsSourceControl[] insetsSourceControlArr = new InsetsSourceControl[length];
            for (int i = 0; i < length; i++) {
                if (controlsForDispatch[i] != null) {
                    InsetsSourceControl insetsSourceControl = new InsetsSourceControl(controlsForDispatch[i]);
                    insetsSourceControlArr[i] = insetsSourceControl;
                    insetsSourceControl.setParcelableFlags(1);
                }
            }
            array.set(insetsSourceControlArr);
        }
    }

    private void tryStartExitingAnimation(WindowState windowState, WindowStateAnimator windowStateAnimator) {
        String str;
        int i = windowState.mAttrs.type == 3 ? 5 : 2;
        if (windowState.inTransition()) {
            this.mWindowManagerServiceExt.tryAddActivityToAnimationSourceWhenStartExitingAnimation(windowState);
        }
        if (windowState.isVisible() && windowState.isDisplayed() && windowState.mDisplayContent.okToAnimate()) {
            if (windowStateAnimator.applyAnimationLocked(i, false)) {
                str = "applyAnimation";
            } else if (windowState.isSelfAnimating(0, 16)) {
                str = "selfAnimating";
            } else if (windowState.mTransitionController.isShellTransitionsEnabled()) {
                ActivityRecord activityRecord = windowState.mActivityRecord;
                if (activityRecord != null && activityRecord.inTransition()) {
                    if (this.mWindowManagerServiceExt.checkExitingAnimationRationality(windowState)) {
                        windowState.mTransitionController.mAnimatingExitWindows.add(windowState);
                    }
                    str = "inTransition";
                }
                str = null;
            } else {
                if (windowState.isAnimating(3, 9)) {
                    str = "inLegacyTransition";
                }
                str = null;
            }
            if (str != null) {
                windowState.mAnimatingExit = true;
                if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ANIM, 2075693141, 0, (String) null, new Object[]{str, String.valueOf(windowState)});
                }
            }
        }
        if (!windowState.mAnimatingExit) {
            ActivityRecord activityRecord2 = windowState.mActivityRecord;
            boolean z = activityRecord2 == null || activityRecord2.mAppStopped;
            windowState.mDestroying = true;
            if (this.mWindowManagerServiceExt.shouldWindowSurfaceSaved(windowState, windowState.getDisplayContent())) {
                if (!this.mDestroySurface.contains(windowState)) {
                    this.mDestroySurface.add(windowState);
                }
                if (!this.mWindowManagerServiceExt.isWindowSurfaceSaved(windowState)) {
                    this.mWindowManagerServiceExt.getDestroySavedSurface().add(windowState);
                }
            } else if (!this.mWindowManagerServiceExt.isWindowSurfaceSaved(windowState)) {
                windowState.destroySurface(false, z);
            }
        }
        if (this.mAccessibilityController.hasCallbacks()) {
            this.mAccessibilityController.onWindowTransition(windowState, i);
        }
    }

    private int createSurfaceControl(SurfaceControl surfaceControl, int i, WindowState windowState, WindowStateAnimator windowStateAnimator) {
        if (!windowState.mHasSurface) {
            i |= 2;
        }
        try {
            Trace.traceBegin(32L, "createSurfaceControl");
            WindowSurfaceController createSurfaceLocked = windowStateAnimator.createSurfaceLocked();
            Trace.traceEnd(32L);
            if (createSurfaceLocked != null) {
                createSurfaceLocked.getSurfaceControl(surfaceControl);
                if (ProtoLogCache.WM_SHOW_TRANSACTIONS_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_TRANSACTIONS, -1257821162, 0, (String) null, new Object[]{String.valueOf(surfaceControl)});
                }
            } else {
                if (ProtoLogCache.WM_ERROR_enabled) {
                    ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 704998117, 0, "Failed to create surface control for %s", new Object[]{String.valueOf(windowState)});
                }
                surfaceControl.release();
            }
            return i;
        } catch (Throwable th) {
            Trace.traceEnd(32L);
            throw th;
        }
    }

    public boolean outOfMemoryWindow(Session session, IWindow iWindow) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowState windowForClientLocked = windowForClientLocked(session, iWindow, false);
                    if (windowForClientLocked != null) {
                        boolean reclaimSomeSurfaceMemory = this.mRoot.reclaimSomeSurfaceMemory(windowForClientLocked.mWinAnimator, "from-client", false);
                        resetPriorityAfterLockedSection();
                        return reclaimSomeSurfaceMemory;
                    }
                    resetPriorityAfterLockedSection();
                    return false;
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishDrawingWindow(Session session, IWindow iWindow, SurfaceControl.Transaction transaction, int i) {
        if (transaction != null) {
            transaction.sanitize(Binder.getCallingPid(), Binder.getCallingUid());
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowState windowForClientLocked = windowForClientLocked(session, iWindow, false);
                    this.mWindowManagerServiceExt.setAnimThreadUxIfNeed(true, windowForClientLocked);
                    if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
                        ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 1112047265, 0, (String) null, new Object[]{String.valueOf(windowForClientLocked), String.valueOf(windowForClientLocked != null ? windowForClientLocked.mWinAnimator.drawStateToString() : "null")});
                    }
                    if (windowForClientLocked != null && windowForClientLocked.finishDrawing(transaction, i)) {
                        if (windowForClientLocked.hasWallpaper()) {
                            windowForClientLocked.getDisplayContent().pendingLayoutChanges |= 4;
                        }
                        windowForClientLocked.setDisplayLayoutNeeded();
                        this.mWindowPlacerLocked.requestTraversal();
                    }
                    if (windowForClientLocked == null && transaction != null) {
                        Slog.d(TAG, "finishDrawingWindow force apply postDrawTransaction,client=" + iWindow + "," + session);
                        transaction.apply();
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean checkCallingPermission(String str, String str2) {
        return checkCallingPermission(str, str2, true);
    }

    boolean checkCallingPermission(String str, String str2, boolean z) {
        if (Binder.getCallingPid() == MY_PID || this.mContext.checkCallingPermission(str) == 0) {
            return true;
        }
        if (!z || !ProtoLogCache.WM_ERROR_enabled) {
            return false;
        }
        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1563755163, 20, "Permission Denial: %s from pid=%d, uid=%d requires %s", new Object[]{String.valueOf(str2), Long.valueOf(Binder.getCallingPid()), Long.valueOf(Binder.getCallingUid()), String.valueOf(str)});
        return false;
    }

    public void addWindowToken(IBinder iBinder, int i, int i2, Bundle bundle) {
        if (!checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "addWindowToken()")) {
            throw new SecurityException("Requires MANAGE_APP_TOKENS permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContentOrCreate = getDisplayContentOrCreate(i2, null);
                if (displayContentOrCreate == null) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1208313423, 4, "addWindowToken: Attempted to add token: %s for non-exiting displayId=%d", new Object[]{String.valueOf(iBinder), Long.valueOf(i2)});
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                WindowToken windowToken = displayContentOrCreate.getWindowToken(iBinder);
                if (windowToken == null) {
                    if (i == 2013) {
                        new WallpaperWindowToken(this, iBinder, true, displayContentOrCreate, true, bundle);
                    } else {
                        new WindowToken.Builder(this, iBinder, i).setDisplayContent(displayContentOrCreate).setPersistOnEmpty(true).setOwnerCanManageAppTokens(true).setOptions(bundle).build();
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (ProtoLogCache.WM_ERROR_enabled) {
                    ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 254883724, 16, "addWindowToken: Attempted to add binder token: %s for already created window token: %s displayId=%d", new Object[]{String.valueOf(iBinder), String.valueOf(windowToken), Long.valueOf(i2)});
                }
                resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    public Configuration attachWindowContextToDisplayArea(IBinder iBinder, int i, int i2, Bundle bundle) {
        if (iBinder == null) {
            throw new IllegalArgumentException("clientToken must not be null!");
        }
        boolean checkCallingPermission = checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "attachWindowContextToDisplayArea", false);
        int callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContentOrCreate = this.mRoot.getDisplayContentOrCreate(i2);
                    if (displayContentOrCreate == null) {
                        if (ProtoLogCache.WM_ERROR_enabled) {
                            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 666937535, 1, "attachWindowContextToDisplayArea: trying to attach to a non-existing display:%d", new Object[]{Long.valueOf(i2)});
                        }
                        resetPriorityAfterLockedSection();
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return null;
                    }
                    DisplayArea<?> findAreaForWindowType = displayContentOrCreate.findAreaForWindowType(i, bundle, checkCallingPermission, false);
                    Configuration hookRegisterWindowContainerListener = this.mWindowManagerServiceExt.hookRegisterWindowContainerListener(findAreaForWindowType, this.mWindowContextListenerController, iBinder, callingUid, i, bundle);
                    if (hookRegisterWindowContainerListener != null) {
                        resetPriorityAfterLockedSection();
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return hookRegisterWindowContainerListener;
                    }
                    this.mWindowContextListenerController.registerWindowContainerListener(iBinder, findAreaForWindowType, callingUid, i, bundle, false);
                    Configuration configuration = findAreaForWindowType.getConfiguration();
                    resetPriorityAfterLockedSection();
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return configuration;
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } catch (Throwable th2) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th2;
        }
    }

    /* JADX WARN: Finally extract failed */
    public void attachWindowContextToWindowToken(IBinder iBinder, IBinder iBinder2) {
        boolean checkCallingPermission = checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "attachWindowContextToWindowToken", false);
        int callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowToken windowToken = this.mRoot.getWindowToken(iBinder2);
                    if (windowToken == null) {
                        if (ProtoLogCache.WM_ERROR_enabled) {
                            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1789321832, 0, "Then token:%s is invalid. It might be removed", new Object[]{String.valueOf(iBinder2)});
                        }
                    } else {
                        int windowType = this.mWindowContextListenerController.getWindowType(iBinder);
                        if (windowType == -1) {
                            throw new IllegalArgumentException("The clientToken:" + iBinder + " should have been attached.");
                        }
                        if (windowType != windowToken.windowType) {
                            throw new IllegalArgumentException("The WindowToken's type should match the created WindowContext's type. WindowToken's type is " + windowToken.windowType + ", while WindowContext's is " + windowType);
                        }
                        if (this.mWindowContextListenerController.assertCallerCanModifyListener(iBinder, checkCallingPermission, callingUid)) {
                            this.mWindowContextListenerController.registerWindowContainerListener(iBinder, windowToken, callingUid, windowToken.windowType, windowToken.mOptions);
                            resetPriorityAfterLockedSection();
                            return;
                        }
                    }
                    resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void detachWindowContextFromWindowContainer(IBinder iBinder) {
        boolean checkCallingPermission = checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "detachWindowContextFromWindowContainer", false);
        int callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (this.mWindowContextListenerController.assertCallerCanModifyListener(iBinder, checkCallingPermission, callingUid)) {
                        WindowContainer<?> container = this.mWindowContextListenerController.getContainer(iBinder);
                        this.mWindowContextListenerController.unregisterWindowContainerListener(iBinder);
                        WindowToken asWindowToken = container.asWindowToken();
                        if (asWindowToken != null && asWindowToken.isFromClient()) {
                            removeWindowToken(asWindowToken.token, asWindowToken.getDisplayContent().getDisplayId());
                        }
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX WARN: Finally extract failed */
    public Configuration attachToDisplayContent(IBinder iBinder, int i) {
        if (iBinder == null) {
            throw new IllegalArgumentException("clientToken must not be null!");
        }
        int callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent == null) {
                        if (Binder.getCallingPid() != MY_PID) {
                            throw new WindowManager.InvalidDisplayException("attachToDisplayContent: trying to attach to a non-existing display:" + i);
                        }
                        resetPriorityAfterLockedSection();
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return null;
                    }
                    this.mWindowContextListenerController.registerWindowContainerListener(iBinder, displayContent, callingUid, -1, null, false);
                    Configuration configuration = displayContent.getConfiguration();
                    resetPriorityAfterLockedSection();
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return configuration;
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } catch (Throwable th2) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th2;
        }
    }

    public boolean isWindowToken(IBinder iBinder) {
        boolean z;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                z = this.mRoot.getWindowToken(iBinder) != null;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return z;
    }

    void removeWindowToken(IBinder iBinder, boolean z, boolean z2, int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1739298851, 4, "removeWindowToken: Attempted to remove token: %s for non-exiting displayId=%d", new Object[]{String.valueOf(iBinder), Long.valueOf(i)});
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                WindowToken removeWindowToken = displayContent.removeWindowToken(iBinder, z2);
                if (removeWindowToken == null) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1518495446, 0, "removeWindowToken: Attempted to remove non-existing token: %s", new Object[]{String.valueOf(iBinder)});
                    }
                    resetPriorityAfterLockedSection();
                } else {
                    if (z) {
                        removeWindowToken.removeAllWindowsIfPossible();
                    }
                    displayContent.getInputMonitor().updateInputWindowsLw(true);
                    resetPriorityAfterLockedSection();
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void removeWindowToken(IBinder iBinder, int i) {
        if (!checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "removeWindowToken()")) {
            throw new SecurityException("Requires MANAGE_APP_TOKENS permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            removeWindowToken(iBinder, false, true, i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void moveWindowTokenToDisplay(IBinder iBinder, int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContentOrCreate = this.mRoot.getDisplayContentOrCreate(i);
                if (displayContentOrCreate == null) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 2060978050, 4, "moveWindowTokenToDisplay: Attempted to move token: %s to non-exiting displayId=%d", new Object[]{String.valueOf(iBinder), Long.valueOf(i)});
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                WindowToken windowToken = this.mRoot.getWindowToken(iBinder);
                if (windowToken == null) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1033274509, 0, "moveWindowTokenToDisplay: Attempted to move non-existing token: %s", new Object[]{String.valueOf(iBinder)});
                    }
                    resetPriorityAfterLockedSection();
                } else if (windowToken.getDisplayContent() == displayContentOrCreate) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 781471998, 0, "moveWindowTokenToDisplay: Cannot move to the original display for token: %s", new Object[]{String.valueOf(iBinder)});
                    }
                    resetPriorityAfterLockedSection();
                } else {
                    displayContentOrCreate.reParentWindowToken(windowToken);
                    resetPriorityAfterLockedSection();
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepareAppTransitionNone() {
        if (!checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "prepareAppTransition()")) {
            throw new SecurityException("Requires MANAGE_APP_TOKENS permission");
        }
        getDefaultDisplayContentLocked().prepareAppTransition(0);
    }

    public void overridePendingAppTransitionMultiThumbFuture(IAppTransitionAnimationSpecsFuture iAppTransitionAnimationSpecsFuture, IRemoteCallback iRemoteCallback, boolean z, int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    Slog.w(TAG, "Attempted to call overridePendingAppTransitionMultiThumbFuture for the display " + i + " that does not exist.");
                    resetPriorityAfterLockedSection();
                    return;
                }
                displayContent.mAppTransition.overridePendingAppTransitionMultiThumbFuture(iAppTransitionAnimationSpecsFuture, iRemoteCallback, z);
                resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void overridePendingAppTransitionRemote(RemoteAnimationAdapter remoteAnimationAdapter, int i) {
        if (!checkCallingPermission("android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS", "overridePendingAppTransitionRemote()")) {
            throw new SecurityException("Requires CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    Slog.w(TAG, "Attempted to call overridePendingAppTransitionRemote for the display " + i + " that does not exist.");
                    resetPriorityAfterLockedSection();
                    return;
                }
                remoteAnimationAdapter.setCallingPidUid(Binder.getCallingPid(), Binder.getCallingUid());
                displayContent.mAppTransition.overridePendingAppTransitionRemote(remoteAnimationAdapter);
                resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void executeAppTransition() {
        if (!checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "executeAppTransition()")) {
            throw new SecurityException("Requires MANAGE_APP_TOKENS permission");
        }
        getDefaultDisplayContentLocked().executeAppTransition();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initializeRecentsAnimation(int i, IRecentsAnimationRunner iRecentsAnimationRunner, RecentsAnimationController.RecentsAnimationCallbacks recentsAnimationCallbacks, int i2, SparseBooleanArray sparseBooleanArray, ActivityRecord activityRecord) {
        this.mRecentsAnimationController = new RecentsAnimationController(this, iRecentsAnimationRunner, recentsAnimationCallbacks, i2);
        this.mRoot.getDisplayContent(i2).mAppTransition.updateBooster();
        this.mRecentsAnimationController.initialize(i, sparseBooleanArray, activityRecord);
    }

    @VisibleForTesting
    void setRecentsAnimationController(RecentsAnimationController recentsAnimationController) {
        this.mRecentsAnimationController = recentsAnimationController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RecentsAnimationController getRecentsAnimationController() {
        return this.mRecentsAnimationController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelRecentsAnimation(@RecentsAnimationController.ReorderMode int i, String str) {
        RecentsAnimationController recentsAnimationController = this.mRecentsAnimationController;
        if (recentsAnimationController != null) {
            recentsAnimationController.cancelAnimation(i, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cleanupRecentsAnimation(@RecentsAnimationController.ReorderMode int i) {
        RecentsAnimationController recentsAnimationController = this.mRecentsAnimationController;
        if (recentsAnimationController != null) {
            this.mRecentsAnimationController = null;
            recentsAnimationController.cleanupAnimation(i);
            DisplayContent defaultDisplayContentLocked = getDefaultDisplayContentLocked();
            if (defaultDisplayContentLocked.mAppTransition.isTransitionSet()) {
                defaultDisplayContentLocked.mSkipAppTransitionAnimation = true;
            }
            defaultDisplayContentLocked.forAllWindowContainers(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    WindowManagerService.lambda$cleanupRecentsAnimation$2((WindowContainer) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$cleanupRecentsAnimation$2(WindowContainer windowContainer) {
        if (windowContainer.isAnimating(1, 1)) {
            windowContainer.cancelAnimation();
        }
    }

    boolean isRecentsAnimationTarget(ActivityRecord activityRecord) {
        RecentsAnimationController recentsAnimationController = this.mRecentsAnimationController;
        return recentsAnimationController != null && recentsAnimationController.isTargetApp(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowOpaqueLocked(IBinder iBinder, boolean z) {
        ActivityRecord activityRecord = this.mRoot.getActivityRecord(iBinder);
        if (activityRecord != null) {
            activityRecord.setMainWindowOpaque(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isValidPictureInPictureAspectRatio(DisplayContent displayContent, float f) {
        return displayContent.getPinnedTaskController().isValidPictureInPictureAspectRatio(f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isValidExpandedPictureInPictureAspectRatio(DisplayContent displayContent, float f) {
        return displayContent.getPinnedTaskController().isValidExpandedPictureInPictureAspectRatio(f);
    }

    public void notifyKeyguardTrustedChanged() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mAtmService.mKeyguardController.isKeyguardShowing(0)) {
                    this.mRoot.ensureActivitiesVisible(null, 0, false);
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void screenTurningOff(int i, WindowManagerPolicy.ScreenOffListener screenOffListener) {
        this.mWindowManagerServiceExt.beginHookscreenTurningOff();
        this.mTaskSnapshotController.screenTurningOff(i, screenOffListener);
    }

    public void triggerAnimationFailsafe() {
        this.mH.sendEmptyMessage(60);
    }

    public void onKeyguardShowingAndNotOccludedChanged() {
        this.mH.sendEmptyMessage(61);
        dispatchKeyguardLockedState();
    }

    public void onPowerKeyDown(final boolean z) {
        this.mWindowManagerServiceExt.onPowerKeyDown(z);
        this.mRoot.forAllDisplayPolicies(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((DisplayPolicy) obj).onPowerKeyDown(z);
            }
        });
    }

    public void onUserSwitched() {
        this.mSettingsObserver.updateSystemUiSettings(true);
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRoot.forAllDisplayPolicies(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda21
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((DisplayPolicy) obj).resetSystemBarAttributes();
                    }
                });
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void moveDisplayToTopIfAllowed(int i) {
        moveDisplayToTopInternal(i);
        syncInputTransactions(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveDisplayToTopInternal(int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent != null && this.mRoot.getTopChild() != displayContent) {
                    if (!displayContent.canStealTopFocus()) {
                        if (ProtoLogCache.WM_DEBUG_FOCUS_LIGHT_enabled) {
                            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, 34682671, 5, (String) null, new Object[]{Long.valueOf(i), Long.valueOf(this.mRoot.getTopFocusedDisplayContent().getDisplayId())});
                        }
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    displayContent.getParent().positionChildAt(Integer.MAX_VALUE, displayContent, true);
                }
                resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public boolean isAppTransitionStateIdle() {
        return getDefaultDisplayContentLocked().mAppTransition.isIdle();
    }

    public void startFreezingScreen(int i, int i2) {
        if (!checkCallingPermission("android.permission.FREEZE_SCREEN", "startFreezingScreen()")) {
            throw new SecurityException("Requires FREEZE_SCREEN permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (!this.mClientFreezingScreen) {
                    this.mClientFreezingScreen = true;
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        startFreezingDisplay(i, i2);
                        if (!this.mDisplayFrozen) {
                            this.mClientFreezingScreen = false;
                            resetPriorityAfterLockedSection();
                            return;
                        } else {
                            this.mH.removeMessages(30);
                            this.mH.sendEmptyMessageDelayed(30, 5000L);
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                        }
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
                resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void stopFreezingScreen() {
        if (!checkCallingPermission("android.permission.FREEZE_SCREEN", "stopFreezingScreen()")) {
            throw new SecurityException("Requires FREEZE_SCREEN permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mClientFreezingScreen) {
                    this.mClientFreezingScreen = false;
                    this.mLastFinishedFreezeSource = "client";
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        stopFreezingDisplayLocked();
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    } catch (Throwable th) {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        throw th;
                    }
                }
            } catch (Throwable th2) {
                resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void disableKeyguard(IBinder iBinder, String str, int i) {
        int handleIncomingUser = this.mAmInternal.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, false, 2, "disableKeyguard", (String) null);
        if (this.mContext.checkCallingOrSelfPermission("android.permission.DISABLE_KEYGUARD") != 0) {
            throw new SecurityException("Requires DISABLE_KEYGUARD permission");
        }
        int callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mKeyguardDisableHandler.disableKeyguard(iBinder, str, callingUid, handleIncomingUser);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void reenableKeyguard(IBinder iBinder, int i) {
        int handleIncomingUser = this.mAmInternal.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, false, 2, "reenableKeyguard", (String) null);
        if (this.mContext.checkCallingOrSelfPermission("android.permission.DISABLE_KEYGUARD") != 0) {
            throw new SecurityException("Requires DISABLE_KEYGUARD permission");
        }
        Objects.requireNonNull(iBinder, "token is null");
        int callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mKeyguardDisableHandler.reenableKeyguard(iBinder, callingUid, handleIncomingUser);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void exitKeyguardSecurely(final IOnKeyguardExitResult iOnKeyguardExitResult) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.DISABLE_KEYGUARD") != 0) {
            throw new SecurityException("Requires DISABLE_KEYGUARD permission");
        }
        if (iOnKeyguardExitResult == null) {
            throw new IllegalArgumentException("callback == null");
        }
        this.mPolicy.exitKeyguardSecurely(new WindowManagerPolicy.OnKeyguardExitResult() { // from class: com.android.server.wm.WindowManagerService.9
            public void onKeyguardExitResult(boolean z) {
                try {
                    iOnKeyguardExitResult.onKeyguardExitResult(z);
                } catch (RemoteException unused) {
                }
            }
        });
    }

    public boolean isKeyguardLocked() {
        return this.mPolicy.isKeyguardLocked();
    }

    public boolean isKeyguardShowingAndNotOccluded() {
        return this.mPolicy.isKeyguardShowingAndNotOccluded();
    }

    public boolean isKeyguardSecure(int i) {
        if (i != UserHandle.getCallingUserId() && !checkCallingPermission("android.permission.INTERACT_ACROSS_USERS", "isKeyguardSecure")) {
            throw new SecurityException("Requires INTERACT_ACROSS_USERS permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mPolicy.isKeyguardSecure(i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void dismissKeyguard(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
        if (!checkCallingPermission("android.permission.CONTROL_KEYGUARD", "dismissKeyguard")) {
            throw new SecurityException("Requires CONTROL_KEYGUARD permission");
        }
        if (this.mAtmService.mKeyguardController.isShowingDream()) {
            this.mAtmService.mTaskSupervisor.wakeUp("leaveDream");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mPolicy.dismissKeyguardLw(iKeyguardDismissCallback, charSequence);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    @RequiresPermission("android.permission.SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE")
    public void addKeyguardLockedStateListener(IKeyguardLockedStateListener iKeyguardLockedStateListener) {
        enforceSubscribeToKeyguardLockedStatePermission();
        if (this.mKeyguardLockedStateListeners.register(iKeyguardLockedStateListener)) {
            return;
        }
        Slog.w(TAG, "Failed to register listener: " + iKeyguardLockedStateListener);
    }

    @RequiresPermission("android.permission.SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE")
    public void removeKeyguardLockedStateListener(IKeyguardLockedStateListener iKeyguardLockedStateListener) {
        enforceSubscribeToKeyguardLockedStatePermission();
        this.mKeyguardLockedStateListeners.unregister(iKeyguardLockedStateListener);
    }

    private void enforceSubscribeToKeyguardLockedStatePermission() {
        if (this.mWindowManagerServiceExt.checkOplusWindowPermission(this)) {
            return;
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE", "android.permission.SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE permission required to subscribe to keyguard locked state changes");
    }

    private void dispatchKeyguardLockedState() {
        this.mH.post(new Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                WindowManagerService.this.lambda$dispatchKeyguardLockedState$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchKeyguardLockedState$4() {
        boolean isKeyguardShowing = this.mPolicy.isKeyguardShowing();
        if (this.mDispatchedKeyguardLockedState == isKeyguardShowing) {
            return;
        }
        int beginBroadcast = this.mKeyguardLockedStateListeners.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                this.mKeyguardLockedStateListeners.getBroadcastItem(i).onKeyguardLockedStateChanged(isKeyguardShowing);
            } catch (RemoteException unused) {
            }
        }
        this.mKeyguardLockedStateListeners.finishBroadcast();
        this.mDispatchedKeyguardLockedState = isKeyguardShowing;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchImeTargetOverlayVisibilityChanged(final IBinder iBinder, final int i, final boolean z, final boolean z2) {
        if (this.mImeTargetChangeListener != null) {
            if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                Slog.d(TAG, "onImeTargetOverlayVisibilityChanged, win=" + this.mWindowMap.get(iBinder) + ", type=" + ViewDebug.intToString(WindowManager.LayoutParams.class, "type", i) + "visible=" + z + ", removed=" + z2);
            }
            this.mH.post(new Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    WindowManagerService.this.lambda$dispatchImeTargetOverlayVisibilityChanged$5(iBinder, i, z, z2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchImeTargetOverlayVisibilityChanged$5(IBinder iBinder, int i, boolean z, boolean z2) {
        this.mImeTargetChangeListener.onImeTargetOverlayVisibilityChanged(iBinder, i, z, z2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchImeInputTargetVisibilityChanged(final IBinder iBinder, final boolean z, final boolean z2) {
        if (this.mImeTargetChangeListener != null) {
            if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                Slog.d(TAG, "onImeInputTargetVisibilityChanged, win=" + this.mWindowMap.get(iBinder) + "visible=" + z + ", removed=" + z2);
            }
            this.mH.post(new Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda27
                @Override // java.lang.Runnable
                public final void run() {
                    WindowManagerService.this.lambda$dispatchImeInputTargetVisibilityChanged$6(iBinder, z, z2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchImeInputTargetVisibilityChanged$6(IBinder iBinder, boolean z, boolean z2) {
        this.mImeTargetChangeListener.onImeInputTargetVisibilityChanged(iBinder, z, z2);
    }

    public void setSwitchingUser(boolean z) {
        if (!checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "setSwitchingUser()")) {
            throw new SecurityException("Requires INTERACT_ACROSS_USERS_FULL permission");
        }
        this.mPolicy.setSwitchingUser(z);
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mSwitchingUser = z;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    @RequiresPermission("android.permission.INTERNAL_SYSTEM_WINDOW")
    public void showGlobalActions() {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "showGlobalActions()")) {
            throw new SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        this.mPolicy.showGlobalActions();
    }

    public void closeSystemDialogs(String str) {
        if (this.mAtmService.checkCanCloseSystemDialogs(Binder.getCallingPid(), Binder.getCallingUid(), null)) {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mRoot.closeSystemDialogs(str);
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        }
    }

    public void setAnimationScale(int i, float f) {
        if (!checkCallingPermission("android.permission.SET_ANIMATION_SCALE", "setAnimationScale()")) {
            throw new SecurityException("Requires SET_ANIMATION_SCALE permission");
        }
        float fixScale = WindowManager.fixScale(f);
        if (i == 0) {
            this.mWindowAnimationScaleSetting = fixScale;
        } else if (i == 1) {
            this.mTransitionAnimationScaleSetting = fixScale;
        } else if (i == 2) {
            this.mAnimatorDurationScaleSetting = fixScale;
        }
        this.mH.sendEmptyMessage(14);
    }

    public void setAnimationScales(float[] fArr) {
        if (!checkCallingPermission("android.permission.SET_ANIMATION_SCALE", "setAnimationScale()")) {
            throw new SecurityException("Requires SET_ANIMATION_SCALE permission");
        }
        if (fArr != null) {
            if (fArr.length >= 1) {
                this.mWindowAnimationScaleSetting = WindowManager.fixScale(fArr[0]);
            }
            if (fArr.length >= 2) {
                this.mTransitionAnimationScaleSetting = WindowManager.fixScale(fArr[1]);
            }
            if (fArr.length >= 3) {
                this.mAnimatorDurationScaleSetting = WindowManager.fixScale(fArr[2]);
                dispatchNewAnimatorScaleLocked(null);
            }
        }
        this.mH.sendEmptyMessage(14);
    }

    private void setAnimatorDurationScale(float f) {
        this.mAnimatorDurationScaleSetting = f;
        ValueAnimator.setDurationScale(f);
    }

    private float animationScalesCheck(int i) {
        if (this.mAnimationsDisabled) {
            return 0.0f;
        }
        if (i == 0) {
            return this.mWindowAnimationScaleSetting;
        }
        if (i == 1) {
            return this.mTransitionAnimationScaleSetting;
        }
        if (i != 2) {
            return -1.0f;
        }
        return this.mAnimatorDurationScaleSetting;
    }

    public float getWindowAnimationScaleLocked() {
        return animationScalesCheck(0);
    }

    public float getTransitionAnimationScaleLocked() {
        return animationScalesCheck(1);
    }

    public float getAnimationScale(int i) {
        if (i == 0) {
            return this.mWindowAnimationScaleSetting;
        }
        if (i == 1) {
            return this.mTransitionAnimationScaleSetting;
        }
        if (i != 2) {
            return 0.0f;
        }
        return this.mAnimatorDurationScaleSetting;
    }

    public float[] getAnimationScales() {
        return new float[]{this.mWindowAnimationScaleSetting, this.mTransitionAnimationScaleSetting, this.mAnimatorDurationScaleSetting};
    }

    public float getCurrentAnimatorScale() {
        float f;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                f = this.mAnimationsDisabled ? 0.0f : this.mAnimatorDurationScaleSetting;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchNewAnimatorScaleLocked(Session session) {
        this.mH.obtainMessage(34, session).sendToTarget();
    }

    public void registerPointerEventListener(WindowManagerPolicyConstants.PointerEventListener pointerEventListener, int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent != null) {
                    displayContent.registerPointerEventListener(pointerEventListener);
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void unregisterPointerEventListener(WindowManagerPolicyConstants.PointerEventListener pointerEventListener, int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent != null) {
                    displayContent.unregisterPointerEventListener(pointerEventListener);
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public int getLidState() {
        int switchState = this.mInputManager.getSwitchState(-1, -256, 0);
        if (switchState > 0) {
            return 0;
        }
        return switchState == 0 ? 1 : -1;
    }

    public void lockDeviceNow() {
        lockNow(null);
    }

    public int getCameraLensCoverState() {
        int switchState = this.mInputManager.getSwitchState(-1, -256, 9);
        if (switchState > 0) {
            return 1;
        }
        return switchState == 0 ? 0 : -1;
    }

    public void switchKeyboardLayout(int i, int i2) {
        this.mInputManager.switchKeyboardLayout(i, i2);
    }

    public void shutdown(boolean z) {
        ShutdownThread.shutdown(ActivityThread.currentActivityThread().getSystemUiContext(), "userrequested", z);
    }

    public void reboot(boolean z) {
        ShutdownThread.reboot(ActivityThread.currentActivityThread().getSystemUiContext(), "userrequested", z);
    }

    public void rebootSafeMode(boolean z) {
        ShutdownThread.rebootSafeMode(ActivityThread.currentActivityThread().getSystemUiContext(), z);
    }

    public void setCurrentUser(int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mAtmService.getTransitionController().requestTransitionIfNeeded(1, null);
                this.mCurrentUserId = i;
                this.mPolicy.setCurrentUserLw(i);
                this.mKeyguardDisableHandler.setCurrentUser(i);
                this.mRoot.switchUser(i);
                this.mWindowPlacerLocked.performSurfacePlacement();
                DisplayContent defaultDisplayContentLocked = getDefaultDisplayContentLocked();
                if (this.mDisplayReady) {
                    int forcedDisplayDensityForUserLocked = getForcedDisplayDensityForUserLocked(i);
                    if (forcedDisplayDensityForUserLocked == 0) {
                        forcedDisplayDensityForUserLocked = defaultDisplayContentLocked.getInitialDisplayDensity();
                    }
                    int adjustDensityForUser = this.mWindowManagerServiceExt.adjustDensityForUser(forcedDisplayDensityForUserLocked, i);
                    if (-1 != adjustDensityForUser) {
                        forcedDisplayDensityForUserLocked = adjustDensityForUser;
                    }
                    defaultDisplayContentLocked.setForcedDensity(forcedDisplayDensityForUserLocked, -2);
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUserVisible(int i) {
        return this.mUmInternal.isUserVisible(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getUserAssignedToDisplay(int i) {
        return this.mUmInternal.getUserAssignedToDisplay(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldPlacePrimaryHomeOnDisplay(int i) {
        return shouldPlacePrimaryHomeOnDisplay(i, this.mUmInternal.getUserAssignedToDisplay(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldPlacePrimaryHomeOnDisplay(int i, int i2) {
        return this.mUmInternal.getMainDisplayAssignedToUser(i2) == i;
    }

    public void enableScreenAfterBoot() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (ProtoLogCache.WM_DEBUG_BOOT_enabled) {
                    boolean z = this.mDisplayEnabled;
                    boolean z2 = this.mForceDisplayEnabled;
                    boolean z3 = this.mShowingBootMessages;
                    boolean z4 = this.mSystemBooted;
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_BOOT, -1884933373, 255, (String) null, new Object[]{Boolean.valueOf(z), Boolean.valueOf(z2), Boolean.valueOf(z3), Boolean.valueOf(z4), String.valueOf(new RuntimeException("here").fillInStackTrace())});
                }
                if (this.mSystemBooted) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                this.mSystemBooted = true;
                hideBootMessagesLocked();
                this.mH.sendEmptyMessageDelayed(23, 30000L);
                resetPriorityAfterLockedSection();
                this.mPolicy.systemBooted();
                performEnableScreen();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void enableScreenIfNeeded() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                enableScreenIfNeededLocked();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enableScreenIfNeededLocked() {
        if (ProtoLogCache.WM_DEBUG_BOOT_enabled) {
            boolean z = this.mDisplayEnabled;
            boolean z2 = this.mForceDisplayEnabled;
            boolean z3 = this.mShowingBootMessages;
            boolean z4 = this.mSystemBooted;
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_BOOT, -549028919, 255, (String) null, new Object[]{Boolean.valueOf(z), Boolean.valueOf(z2), Boolean.valueOf(z3), Boolean.valueOf(z4), String.valueOf(new RuntimeException("here").fillInStackTrace())});
        }
        if (this.mDisplayEnabled) {
            return;
        }
        if (this.mSystemBooted || this.mShowingBootMessages) {
            this.mH.sendEmptyMessage(16);
        }
    }

    public void performBootTimeout() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mDisplayEnabled) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (ProtoLogCache.WM_ERROR_enabled) {
                    ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1001904964, 0, "***** BOOT TIMEOUT: forcing display enabled", (Object[]) null);
                }
                this.mForceDisplayEnabled = true;
                resetPriorityAfterLockedSection();
                performEnableScreen();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void onSystemUiStarted() {
        this.mPolicy.onSystemUiStarted();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performEnableScreen() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (ProtoLogCache.WM_DEBUG_BOOT_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_BOOT, -1256520588, 255, (String) null, new Object[]{Boolean.valueOf(this.mDisplayEnabled), Boolean.valueOf(this.mForceDisplayEnabled), Boolean.valueOf(this.mShowingBootMessages), Boolean.valueOf(this.mSystemBooted), String.valueOf(new RuntimeException("here").fillInStackTrace())});
                }
                if (this.mDisplayEnabled) {
                    return;
                }
                if (!this.mSystemBooted && !this.mShowingBootMessages) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (!this.mShowingBootMessages && !this.mPolicy.canDismissBootAnimation()) {
                    Slog.i(TAG, " Waiting for mKeyguardDrawComplete");
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (!this.mForceDisplayEnabled) {
                    if (this.mBootWaitForWindowsStartTime < 0) {
                        this.mBootWaitForWindowsStartTime = SystemClock.elapsedRealtime();
                    }
                    for (int childCount = this.mRoot.getChildCount() - 1; childCount >= 0; childCount--) {
                        if (this.mRoot.getChildAt(childCount).shouldWaitForSystemDecorWindowsOnBoot()) {
                            Slog.i(TAG, " Waiting all existing windows have been drawn");
                            resetPriorityAfterLockedSection();
                            return;
                        }
                    }
                    long elapsedRealtime = SystemClock.elapsedRealtime() - this.mBootWaitForWindowsStartTime;
                    this.mBootWaitForWindowsStartTime = -1L;
                    if (elapsedRealtime > 10 && ProtoLogCache.WM_DEBUG_BOOT_enabled) {
                        ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_BOOT, 544101314, 1, (String) null, new Object[]{Long.valueOf(elapsedRealtime)});
                    }
                }
                if (!this.mBootAnimationStopped) {
                    Trace.asyncTraceBegin(32L, "Stop bootanim", 0);
                    SystemProperties.set("service.bootanim.exit", "1");
                    Slog.i(TAG, " Try to stop boot anim");
                    this.mBootAnimationStopped = true;
                }
                if (!this.mForceDisplayEnabled && !checkBootAnimationCompleteLocked()) {
                    if (ProtoLogCache.WM_DEBUG_BOOT_enabled) {
                        ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_BOOT, 374972436, 0, (String) null, (Object[]) null);
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (!SurfaceControl.bootFinished()) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1323783276, 0, "performEnableScreen: bootFinished() failed.", (Object[]) null);
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                EventLogTags.writeWmBootAnimationDone(SystemClock.uptimeMillis());
                Trace.asyncTraceEnd(32L, "Stop bootanim", 0);
                this.mDisplayEnabled = true;
                if (ProtoLogCache.WM_DEBUG_SCREEN_ON_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_SCREEN_ON, -116086365, 0, (String) null, (Object[]) null);
                }
                this.mInputManagerCallback.setEventDispatchingLw(this.mEventDispatchingEnabled);
                resetPriorityAfterLockedSection();
                try {
                    this.mActivityManager.bootAnimationComplete();
                } catch (RemoteException unused) {
                }
                this.mPolicy.enableScreenAfterBoot();
                updateRotationUnchecked(false, false);
                this.mWindowManagerServiceExt.endHookperformEnableScreen(this, this.mContext);
                WindowManagerGlobalLock windowManagerGlobalLock2 = this.mGlobalLock;
                boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock2) {
                    try {
                        this.mAtmService.getTransitionController().mIsWaitingForDisplayEnabled = false;
                        if (ProtoLogCache.WM_DEBUG_WINDOW_TRANSITIONS_enabled) {
                            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -2088209279, 0, (String) null, (Object[]) null);
                        }
                    } finally {
                    }
                }
                resetPriorityAfterLockedSection();
            } finally {
                resetPriorityAfterLockedSection();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkBootAnimationCompleteLocked() {
        if (SystemService.isRunning(BOOT_ANIMATION_SERVICE)) {
            this.mH.removeMessages(37);
            this.mH.sendEmptyMessageDelayed(37, 50L);
            if (ProtoLogCache.WM_DEBUG_BOOT_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_BOOT, 600140673, 0, (String) null, (Object[]) null);
            }
            return false;
        }
        if (!ProtoLogCache.WM_DEBUG_BOOT_enabled) {
            return true;
        }
        ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_BOOT, 1224307091, 0, (String) null, (Object[]) null);
        return true;
    }

    public void showBootMessage(CharSequence charSequence, boolean z) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                boolean z2 = false;
                if (ProtoLogCache.WM_DEBUG_BOOT_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_BOOT, -874446906, 1020, (String) null, new Object[]{String.valueOf(charSequence), Boolean.valueOf(z), Boolean.valueOf(this.mAllowBootMessages), Boolean.valueOf(this.mShowingBootMessages), Boolean.valueOf(this.mSystemBooted), String.valueOf(new RuntimeException("here").fillInStackTrace())});
                }
                if (!this.mAllowBootMessages) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (!this.mShowingBootMessages) {
                    if (!z) {
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    z2 = true;
                }
                if (this.mSystemBooted) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                this.mShowingBootMessages = true;
                this.mPolicy.showBootMessage(charSequence, z);
                resetPriorityAfterLockedSection();
                if (z2) {
                    performEnableScreen();
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void hideBootMessagesLocked() {
        if (ProtoLogCache.WM_DEBUG_BOOT_enabled) {
            boolean z = this.mDisplayEnabled;
            boolean z2 = this.mForceDisplayEnabled;
            boolean z3 = this.mShowingBootMessages;
            boolean z4 = this.mSystemBooted;
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_BOOT, -1350198040, 255, (String) null, new Object[]{Boolean.valueOf(z), Boolean.valueOf(z2), Boolean.valueOf(z3), Boolean.valueOf(z4), String.valueOf(new RuntimeException("here").fillInStackTrace())});
        }
        if (this.mShowingBootMessages) {
            this.mShowingBootMessages = false;
            this.mPolicy.hideBootMessages();
        }
    }

    public void setInTouchMode(boolean z, int i) {
        int i2;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (this.mPerDisplayFocusEnabled && (displayContent == null || displayContent.isInTouchMode() == z)) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                boolean z2 = displayContent != null && displayContent.hasOwnFocus();
                if (z2 && displayContent.isInTouchMode() == z) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                int callingPid = Binder.getCallingPid();
                int callingUid = Binder.getCallingUid();
                boolean hasTouchModePermission = hasTouchModePermission(callingPid);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (!this.mPerDisplayFocusEnabled && !z2) {
                        int size = this.mRoot.mChildren.size();
                        int i3 = 0;
                        while (i3 < size) {
                            DisplayContent displayContent2 = (DisplayContent) this.mRoot.mChildren.get(i3);
                            if (displayContent2.isInTouchMode() != z && !displayContent2.hasOwnFocus()) {
                                i2 = size;
                                if (this.mInputManager.setInTouchMode(z, callingPid, callingUid, hasTouchModePermission, displayContent2.mDisplayId)) {
                                    displayContent2.setInTouchMode(z);
                                }
                                i3++;
                                size = i2;
                            }
                            i2 = size;
                            i3++;
                            size = i2;
                        }
                        resetPriorityAfterLockedSection();
                    }
                    if (this.mInputManager.setInTouchMode(z, callingPid, callingUid, hasTouchModePermission, i)) {
                        displayContent.setInTouchMode(z);
                    }
                    resetPriorityAfterLockedSection();
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setInTouchModeOnAllDisplays(boolean z) {
        int callingPid = Binder.getCallingPid();
        int callingUid = Binder.getCallingUid();
        boolean hasTouchModePermission = hasTouchModePermission(callingPid);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                for (int i = 0; i < this.mRoot.mChildren.size(); i++) {
                    try {
                        DisplayContent displayContent = (DisplayContent) this.mRoot.mChildren.get(i);
                        if (displayContent.isInTouchMode() != z && this.mInputManager.setInTouchMode(z, callingPid, callingUid, hasTouchModePermission, displayContent.mDisplayId)) {
                            displayContent.setInTouchMode(z);
                        }
                    } catch (Throwable th) {
                        resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean hasTouchModePermission(int i) {
        return this.mAtmService.instrumentationSourceHasPermission(i, "android.permission.MODIFY_TOUCH_MODE_STATE") || checkCallingPermission("android.permission.MODIFY_TOUCH_MODE_STATE", "setInTouchMode()", false);
    }

    public boolean isInTouchMode(int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    boolean z = this.mContext.getResources().getBoolean(R.bool.kg_share_status_area);
                    resetPriorityAfterLockedSection();
                    return z;
                }
                boolean isInTouchMode = displayContent.isInTouchMode();
                resetPriorityAfterLockedSection();
                return isInTouchMode;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void showEmulatorDisplayOverlayIfNeeded() {
        if (this.mContext.getResources().getBoolean(17891912) && SystemProperties.getBoolean(PROPERTY_EMULATOR_CIRCULAR, false) && Build.IS_EMULATOR) {
            H h = this.mH;
            h.sendMessage(h.obtainMessage(36));
        }
    }

    public void showEmulatorDisplayOverlay() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
                    Slog.i(TAG, ">>> showEmulatorDisplayOverlay");
                }
                if (this.mEmulatorDisplayOverlay == null) {
                    this.mEmulatorDisplayOverlay = new EmulatorDisplayOverlay(this.mContext, getDefaultDisplayContentLocked(), (this.mPolicy.getWindowLayerFromTypeLw(2018) * MAX_ANIMATION_DURATION) + 10, this.mTransaction);
                }
                this.mEmulatorDisplayOverlay.setVisibility(true, this.mTransaction);
                this.mTransaction.apply();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void showStrictModeViolation(boolean z) {
        int callingPid = Binder.getCallingPid();
        if (z) {
            H h = this.mH;
            h.sendMessage(h.obtainMessage(25, 1, callingPid));
            H h2 = this.mH;
            h2.sendMessageDelayed(h2.obtainMessage(25, 0, callingPid), 1000L);
            return;
        }
        H h3 = this.mH;
        h3.sendMessage(h3.obtainMessage(25, 0, callingPid));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showStrictModeViolation(int i, int i2) {
        boolean z = i != 0;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            if (z) {
                try {
                    if (!this.mRoot.canShowStrictModeViolation(i2)) {
                        resetPriorityAfterLockedSection();
                        return;
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            if (WindowManagerDebugConfig.SHOW_VERBOSE_TRANSACTIONS) {
                Slog.i(TAG, ">>> showStrictModeViolation");
            }
            if (this.mStrictModeFlash == null) {
                this.mStrictModeFlash = new StrictModeFlash(getDefaultDisplayContentLocked(), this.mTransaction);
            }
            this.mStrictModeFlash.setVisibility(z, this.mTransaction);
            this.mTransaction.apply();
            resetPriorityAfterLockedSection();
        }
    }

    public void setStrictModeVisualIndicatorPreference(String str) {
        SystemProperties.set("persist.sys.strictmode.visual", str);
    }

    public Bitmap screenshotWallpaper() {
        Bitmap screenshotWallpaperLocked;
        if (!checkCallingPermission("android.permission.READ_FRAME_BUFFER", "screenshotWallpaper()")) {
            throw new SecurityException("Requires READ_FRAME_BUFFER permission");
        }
        try {
            Trace.traceBegin(32L, "screenshotWallpaper");
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    screenshotWallpaperLocked = this.mRoot.getDisplayContent(0).mWallpaperController.screenshotWallpaperLocked();
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
            return screenshotWallpaperLocked;
        } finally {
            Trace.traceEnd(32L);
        }
    }

    public SurfaceControl mirrorWallpaperSurface(int i) {
        SurfaceControl mirrorWallpaperSurface;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                mirrorWallpaperSurface = this.mRoot.getDisplayContent(i).mWallpaperController.mirrorWallpaperSurface();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return mirrorWallpaperSurface;
    }

    public boolean requestAssistScreenshot(final IAssistDataReceiver iAssistDataReceiver) {
        final Bitmap screenshotDisplayLocked;
        if (!checkCallingPermission("android.permission.READ_FRAME_BUFFER", "requestAssistScreenshot()")) {
            throw new SecurityException("Requires READ_FRAME_BUFFER permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(0);
                if (displayContent == null) {
                    if (WindowManagerDebugConfig.DEBUG_SCREENSHOT) {
                        Slog.i(TAG, "Screenshot returning null. No Display for displayId=0");
                    }
                    screenshotDisplayLocked = null;
                } else {
                    screenshotDisplayLocked = displayContent.screenshotDisplayLocked();
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        FgThread.getHandler().post(new Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                WindowManagerService.lambda$requestAssistScreenshot$7(iAssistDataReceiver, screenshotDisplayLocked);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$requestAssistScreenshot$7(IAssistDataReceiver iAssistDataReceiver, Bitmap bitmap) {
        try {
            iAssistDataReceiver.onHandleAssistScreenshot(bitmap);
        } catch (RemoteException unused) {
        }
    }

    public TaskSnapshot getTaskSnapshot(int i, int i2, boolean z, boolean z2) {
        return this.mTaskSnapshotController.getSnapshot(i, i2, z2, z);
    }

    public Bitmap captureTaskBitmap(int i, ScreenCapture.LayerCaptureArgs.Builder builder) {
        if (this.mTaskSnapshotController.shouldDisableSnapshots()) {
            return null;
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                Task anyTaskForId = this.mRoot.anyTaskForId(i);
                if (anyTaskForId == null) {
                    resetPriorityAfterLockedSection();
                    return null;
                }
                anyTaskForId.getBounds(this.mTmpRect);
                this.mTmpRect.offsetTo(0, 0);
                ScreenCapture.ScreenshotHardwareBuffer captureLayers = ScreenCapture.captureLayers(builder.setLayer(anyTaskForId.getSurfaceControl()).setSourceCrop(this.mTmpRect).build());
                if (captureLayers == null) {
                    Slog.w(TAG, "Could not get screenshot buffer for taskId: " + i);
                    resetPriorityAfterLockedSection();
                    return null;
                }
                Bitmap asBitmap = captureLayers.asBitmap();
                resetPriorityAfterLockedSection();
                return asBitmap;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void removeObsoleteTaskFiles(ArraySet<Integer> arraySet, int[] iArr) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mTaskSnapshotController.removeObsoleteTaskFiles(arraySet, iArr);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void setFixedToUserRotation(int i, int i2) {
        if (!checkCallingPermission("android.permission.SET_ORIENTATION", "setFixedToUserRotation()")) {
            throw new SecurityException("Requires SET_ORIENTATION permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent == null) {
                        Slog.w(TAG, "Trying to set fixed to user rotation for a missing display.");
                        resetPriorityAfterLockedSection();
                    } else {
                        displayContent.getDisplayRotation().setFixedToUserRotation(i2);
                        resetPriorityAfterLockedSection();
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getFixedToUserRotation(int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    Slog.w(TAG, "Trying to get fixed to user rotation for a missing display.");
                    resetPriorityAfterLockedSection();
                    return -1;
                }
                int fixedToUserRotationMode = displayContent.getDisplayRotation().getFixedToUserRotationMode();
                resetPriorityAfterLockedSection();
                return fixedToUserRotationMode;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setIgnoreOrientationRequest(int i, boolean z) {
        if (!checkCallingPermission("android.permission.SET_ORIENTATION", "setIgnoreOrientationRequest()")) {
            throw new SecurityException("Requires SET_ORIENTATION permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent == null) {
                        Slog.w(TAG, "Trying to setIgnoreOrientationRequest() for a missing display.");
                        resetPriorityAfterLockedSection();
                    } else {
                        displayContent.setIgnoreOrientationRequest(z);
                        resetPriorityAfterLockedSection();
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getIgnoreOrientationRequest(int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    Slog.w(TAG, "Trying to getIgnoreOrientationRequest() for a missing display.");
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean ignoreOrientationRequest = displayContent.getIgnoreOrientationRequest();
                resetPriorityAfterLockedSection();
                return ignoreOrientationRequest;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOrientationRequestPolicy(boolean z, int[] iArr, int[] iArr2) {
        this.mOrientationMapping.clear();
        if (iArr != null && iArr2 != null && iArr.length == iArr2.length) {
            for (int i = 0; i < iArr.length; i++) {
                this.mOrientationMapping.put(iArr[i], iArr2[i]);
            }
        }
        if (z == this.mIsIgnoreOrientationRequestDisabled) {
            return;
        }
        this.mIsIgnoreOrientationRequestDisabled = z;
        for (int childCount = this.mRoot.getChildCount() - 1; childCount >= 0; childCount--) {
            this.mRoot.getChildAt(childCount).onIsIgnoreOrientationRequestDisabledChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int mapOrientationRequest(int i) {
        return !this.mIsIgnoreOrientationRequestDisabled ? i : this.mOrientationMapping.get(i, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isIgnoreOrientationRequestDisabled() {
        return this.mIsIgnoreOrientationRequestDisabled || !this.mLetterboxConfiguration.isIgnoreOrientationRequestAllowed();
    }

    public void freezeRotation(int i) {
        freezeDisplayRotation(0, i);
    }

    public void freezeDisplayRotation(int i, int i2) {
        if (!checkCallingPermission("android.permission.SET_ORIENTATION", "freezeRotation()")) {
            throw new SecurityException("Requires SET_ORIENTATION permission");
        }
        if (i2 < -1 || i2 > 3) {
            throw new IllegalArgumentException("Rotation argument must be -1 or a valid rotation constant.");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent == null) {
                        Slog.w(TAG, "Trying to freeze rotation for a missing display.");
                        resetPriorityAfterLockedSection();
                    } else {
                        displayContent.getDisplayRotation().freezeRotation(i2);
                        resetPriorityAfterLockedSection();
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        updateRotationUnchecked(false, false);
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void thawRotation() {
        thawDisplayRotation(0);
    }

    public void thawDisplayRotation(int i) {
        if (!checkCallingPermission("android.permission.SET_ORIENTATION", "thawRotation()")) {
            throw new SecurityException("Requires SET_ORIENTATION permission");
        }
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -1076978367, 1, (String) null, new Object[]{Long.valueOf(getDefaultDisplayRotation())});
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent == null) {
                        Slog.w(TAG, "Trying to thaw rotation for a missing display.");
                        resetPriorityAfterLockedSection();
                    } else {
                        displayContent.getDisplayRotation().thawRotation();
                        resetPriorityAfterLockedSection();
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        updateRotationUnchecked(false, false);
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean isRotationFrozen() {
        return isDisplayRotationFrozen(0);
    }

    public boolean isDisplayRotationFrozen(int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    Slog.w(TAG, "Trying to check if rotation is frozen on a missing display.");
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean isRotationFrozen = displayContent.getDisplayRotation().isRotationFrozen();
                resetPriorityAfterLockedSection();
                return isRotationFrozen;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDisplayUserRotation(int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    Slog.w(TAG, "Trying to get user rotation of a missing display.");
                    resetPriorityAfterLockedSection();
                    return -1;
                }
                int userRotation = displayContent.getDisplayRotation().getUserRotation();
                resetPriorityAfterLockedSection();
                return userRotation;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void updateRotation(boolean z, boolean z2) {
        updateRotationUnchecked(z, z2);
    }

    private void updateRotationUnchecked(boolean z, boolean z2) {
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -198463978, 15, (String) null, new Object[]{Boolean.valueOf(z), Boolean.valueOf(z2)});
        }
        Trace.traceBegin(32L, "updateRotation");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    int size = this.mRoot.mChildren.size();
                    boolean z3 = false;
                    for (int i = 0; i < size; i++) {
                        DisplayContent displayContent = (DisplayContent) this.mRoot.mChildren.get(i);
                        Trace.traceBegin(32L, "updateRotation: display");
                        boolean updateRotationUnchecked = displayContent.updateRotationUnchecked();
                        Trace.traceEnd(32L);
                        if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("updateRotationUnchecked: rotationChanged = ");
                            sb.append(updateRotationUnchecked);
                            sb.append(", caller:");
                            sb.append(z ? Debug.getCallers(7) : "");
                            Slog.d(TAG, sb.toString());
                        }
                        if (updateRotationUnchecked) {
                            this.mAtmService.getTaskChangeNotificationController().notifyOnActivityRotation(displayContent.mDisplayId);
                        }
                        if (!(updateRotationUnchecked && (displayContent.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange() || displayContent.mTransitionController.isCollecting()))) {
                            if (z2) {
                                displayContent.setLayoutNeeded();
                                z3 = true;
                            }
                            if (updateRotationUnchecked || z) {
                                displayContent.sendNewConfiguration();
                            }
                        }
                    }
                    if (z3) {
                        Trace.traceBegin(32L, "updateRotation: performSurfacePlacement");
                        this.mWindowPlacerLocked.performSurfacePlacement();
                        Trace.traceEnd(32L);
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            Trace.traceEnd(32L);
        }
    }

    public int getDefaultDisplayRotation() {
        int rotation;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                rotation = getDefaultDisplayContentLocked().getRotation();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return rotation;
    }

    public void setDisplayChangeWindowController(IDisplayChangeWindowController iDisplayChangeWindowController) {
        ActivityTaskManagerService.enforceTaskPermission("setDisplayWindowRotationController");
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    IDisplayChangeWindowController iDisplayChangeWindowController2 = this.mDisplayChangeController;
                    if (iDisplayChangeWindowController2 != null) {
                        iDisplayChangeWindowController2.asBinder().unlinkToDeath(this.mDisplayChangeControllerDeath, 0);
                        this.mDisplayChangeController = null;
                    }
                    iDisplayChangeWindowController.asBinder().linkToDeath(this.mDisplayChangeControllerDeath, 0);
                    this.mDisplayChangeController = iDisplayChangeWindowController;
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } catch (RemoteException e) {
            throw new RuntimeException("Unable to set rotation controller", e);
        }
    }

    /* JADX WARN: Finally extract failed */
    public SurfaceControl addShellRoot(int i, IWindow iWindow, int i2) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_APP_TOKENS") != 0) {
            throw new SecurityException("Must hold permission android.permission.MANAGE_APP_TOKENS");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent != null) {
                        SurfaceControl addShellRoot = displayContent.addShellRoot(iWindow, i2);
                        resetPriorityAfterLockedSection();
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        return addShellRoot;
                    }
                    resetPriorityAfterLockedSection();
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return null;
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } catch (Throwable th2) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th2;
        }
    }

    /* JADX WARN: Finally extract failed */
    public void setShellRootAccessibilityWindow(int i, int i2, IWindow iWindow) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_APP_TOKENS") != 0) {
            throw new SecurityException("Must hold permission android.permission.MANAGE_APP_TOKENS");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent != null) {
                        ShellRoot shellRoot = displayContent.mShellRoots.get(i2);
                        if (shellRoot != null) {
                            shellRoot.setAccessibilityWindow(iWindow);
                            resetPriorityAfterLockedSection();
                            return;
                        }
                    }
                    resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setDisplayWindowInsetsController(int i, IDisplayWindowInsetsController iDisplayWindowInsetsController) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_APP_TOKENS") != 0) {
            throw new SecurityException("Must hold permission android.permission.MANAGE_APP_TOKENS");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent != null) {
                        displayContent.setRemoteInsetsController(iDisplayWindowInsetsController);
                        resetPriorityAfterLockedSection();
                    } else {
                        resetPriorityAfterLockedSection();
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void updateDisplayWindowRequestedVisibleTypes(int i, int i2) {
        DisplayContent.RemoteInsetsControlTarget remoteInsetsControlTarget;
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_APP_TOKENS") != 0) {
            throw new SecurityException("Must hold permission android.permission.MANAGE_APP_TOKENS");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent != null && (remoteInsetsControlTarget = displayContent.mRemoteInsetsControlTarget) != null) {
                        remoteInsetsControlTarget.setRequestedVisibleTypes(i2);
                        displayContent.getInsetsStateController().onInsetsModified(displayContent.mRemoteInsetsControlTarget);
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public int watchRotation(IRotationWatcher iRotationWatcher, int i) {
        int rotation;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    throw new IllegalArgumentException("Trying to register rotation event for invalid display: " + i);
                }
                this.mRotationWatcherController.registerDisplayRotationWatcher(iRotationWatcher, i);
                rotation = displayContent.getRotation();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return rotation;
    }

    public void removeRotationWatcher(IRotationWatcher iRotationWatcher) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRotationWatcherController.removeRotationWatcher(iRotationWatcher);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public int registerProposedRotationListener(IBinder iBinder, IRotationWatcher iRotationWatcher) {
        int proposedRotation;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowContainer<?> associatedWindowContainer = this.mRotationWatcherController.getAssociatedWindowContainer(iBinder);
                if (associatedWindowContainer == null) {
                    Slog.w(TAG, "Register rotation listener from non-existing token, uid=" + Binder.getCallingUid());
                    resetPriorityAfterLockedSection();
                    return 0;
                }
                this.mRotationWatcherController.registerProposedRotationListener(iRotationWatcher, iBinder);
                WindowOrientationListener orientationListener = associatedWindowContainer.mDisplayContent.getDisplayRotation().getOrientationListener();
                if (orientationListener != null && (proposedRotation = orientationListener.getProposedRotation()) >= 0) {
                    resetPriorityAfterLockedSection();
                    return proposedRotation;
                }
                int rotation = associatedWindowContainer.getWindowConfiguration().getRotation();
                resetPriorityAfterLockedSection();
                return rotation;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public boolean registerWallpaperVisibilityListener(IWallpaperVisibilityListener iWallpaperVisibilityListener, int i) {
        boolean isWallpaperVisible;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    throw new IllegalArgumentException("Trying to register visibility event for invalid display: " + i);
                }
                this.mWallpaperVisibilityListeners.registerWallpaperVisibilityListener(iWallpaperVisibilityListener, i);
                isWallpaperVisible = displayContent.mWallpaperController.isWallpaperVisible();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return isWallpaperVisible;
    }

    public void unregisterWallpaperVisibilityListener(IWallpaperVisibilityListener iWallpaperVisibilityListener, int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mWallpaperVisibilityListeners.unregisterWallpaperVisibilityListener(iWallpaperVisibilityListener, i);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void registerSystemGestureExclusionListener(ISystemGestureExclusionListener iSystemGestureExclusionListener, int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    throw new IllegalArgumentException("Trying to register visibility event for invalid display: " + i);
                }
                displayContent.registerSystemGestureExclusionListener(iSystemGestureExclusionListener);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void unregisterSystemGestureExclusionListener(ISystemGestureExclusionListener iSystemGestureExclusionListener, int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    throw new IllegalArgumentException("Trying to register visibility event for invalid display: " + i);
                }
                displayContent.unregisterSystemGestureExclusionListener(iSystemGestureExclusionListener);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportSystemGestureExclusionChanged(Session session, IWindow iWindow, List<Rect> list) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowForClientLocked = windowForClientLocked(session, iWindow, true);
                if (windowForClientLocked.setSystemGestureExclusion(list)) {
                    windowForClientLocked.getDisplayContent().updateSystemGestureExclusion();
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportKeepClearAreasChanged(Session session, IWindow iWindow, List<Rect> list, List<Rect> list2) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowForClientLocked = windowForClientLocked(session, iWindow, true);
                if (windowForClientLocked.setKeepClearAreas(list, list2)) {
                    windowForClientLocked.getDisplayContent().updateKeepClearAreas();
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void registerDisplayFoldListener(IDisplayFoldListener iDisplayFoldListener) {
        this.mPolicy.registerDisplayFoldListener(iDisplayFoldListener);
    }

    public void unregisterDisplayFoldListener(IDisplayFoldListener iDisplayFoldListener) {
        this.mPolicy.unregisterDisplayFoldListener(iDisplayFoldListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOverrideFoldedArea(Rect rect) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
            throw new SecurityException("Must hold permission android.permission.WRITE_SECURE_SETTINGS");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mPolicy.setOverrideFoldedArea(rect);
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getFoldedArea() {
        Rect foldedArea;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    foldedArea = this.mPolicy.getFoldedArea();
                } finally {
                }
            }
            resetPriorityAfterLockedSection();
            return foldedArea;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public int[] registerDisplayWindowListener(IDisplayWindowListener iDisplayWindowListener) {
        ActivityTaskManagerService.enforceTaskPermission("registerDisplayWindowListener");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mDisplayNotificationController.registerListener(iDisplayWindowListener);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void unregisterDisplayWindowListener(IDisplayWindowListener iDisplayWindowListener) {
        ActivityTaskManagerService.enforceTaskPermission("unregisterDisplayWindowListener");
        this.mDisplayNotificationController.unregisterListener(iDisplayWindowListener);
    }

    public int getPreferredOptionsPanelGravity(int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    resetPriorityAfterLockedSection();
                    return 81;
                }
                int preferredOptionsPanelGravity = displayContent.getPreferredOptionsPanelGravity();
                resetPriorityAfterLockedSection();
                return preferredOptionsPanelGravity;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public boolean startViewServer(int i) {
        if (isSystemSecure() || !checkCallingPermission("android.permission.DUMP", "startViewServer") || i < 1024) {
            return false;
        }
        ViewServer viewServer = this.mViewServer;
        if (viewServer != null) {
            if (!viewServer.isRunning()) {
                try {
                    return this.mViewServer.start();
                } catch (IOException unused) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1545962566, 0, "View server did not start", (Object[]) null);
                    }
                }
            }
            return false;
        }
        try {
            ViewServer viewServer2 = new ViewServer(this, i);
            this.mViewServer = viewServer2;
            return viewServer2.start();
        } catch (IOException unused2) {
            if (ProtoLogCache.WM_ERROR_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1545962566, 0, "View server did not start", (Object[]) null);
            }
            return false;
        }
    }

    private boolean isSystemSecure() {
        return "1".equals(SystemProperties.get(SYSTEM_SECURE, "1")) && "0".equals(SystemProperties.get(SYSTEM_DEBUGGABLE, "0"));
    }

    public boolean stopViewServer() {
        ViewServer viewServer;
        if (isSystemSecure() || !checkCallingPermission("android.permission.DUMP", "stopViewServer") || (viewServer = this.mViewServer) == null) {
            return false;
        }
        return viewServer.stop();
    }

    public boolean isViewServerRunning() {
        ViewServer viewServer;
        return !isSystemSecure() && checkCallingPermission("android.permission.DUMP", "isViewServerRunning") && (viewServer = this.mViewServer) != null && viewServer.isRunning();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean viewServerListWindows(Socket socket) {
        BufferedWriter bufferedWriter;
        Throwable th;
        boolean z = false;
        if (isSystemSecure()) {
            return false;
        }
        final ArrayList arrayList = new ArrayList();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRoot.forAllWindows(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        arrayList.add((WindowState) obj);
                    }
                }, false);
            } catch (Throwable th2) {
                resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        resetPriorityAfterLockedSection();
        BufferedWriter bufferedWriter2 = null;
        try {
            try {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), 8192);
            } catch (Exception unused) {
            } catch (Throwable th3) {
                bufferedWriter = null;
                th = th3;
            }
            try {
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    WindowState windowState = (WindowState) arrayList.get(i);
                    bufferedWriter.write(Integer.toHexString(System.identityHashCode(windowState)));
                    bufferedWriter.write(32);
                    bufferedWriter.append(windowState.mAttrs.getTitle());
                    bufferedWriter.write(10);
                }
                bufferedWriter.write("DONE.\n");
                bufferedWriter.flush();
                bufferedWriter.close();
                z = true;
            } catch (Exception unused2) {
                bufferedWriter2 = bufferedWriter;
                if (bufferedWriter2 != null) {
                    bufferedWriter2.close();
                }
                return z;
            } catch (Throwable th4) {
                th = th4;
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException unused3) {
                    }
                }
                throw th;
            }
        } catch (IOException unused4) {
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean viewServerGetFocusedWindow(Socket socket) {
        boolean z = false;
        if (isSystemSecure()) {
            return false;
        }
        WindowState focusedWindow = getFocusedWindow();
        BufferedWriter bufferedWriter = null;
        try {
            try {
                BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), 8192);
                if (focusedWindow != null) {
                    try {
                        bufferedWriter2.write(Integer.toHexString(System.identityHashCode(focusedWindow)));
                        bufferedWriter2.write(32);
                        bufferedWriter2.append(focusedWindow.mAttrs.getTitle());
                    } catch (Exception unused) {
                        bufferedWriter = bufferedWriter2;
                        if (bufferedWriter != null) {
                            bufferedWriter.close();
                        }
                        return z;
                    } catch (Throwable th) {
                        th = th;
                        bufferedWriter = bufferedWriter2;
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (IOException unused2) {
                            }
                        }
                        throw th;
                    }
                }
                bufferedWriter2.write(10);
                bufferedWriter2.flush();
                bufferedWriter2.close();
                z = true;
            } catch (Exception unused3) {
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException unused4) {
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00b3 A[Catch: all -> 0x00dd, TRY_LEAVE, TryCatch #7 {all -> 0x00dd, blocks: (B:32:0x00af, B:34:0x00b3), top: B:31:0x00af }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00d4  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00d9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:47:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00ea A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean viewServerWindowCommand(Socket socket, String str, String str2) {
        BufferedWriter bufferedWriter;
        Parcel parcel;
        if (isSystemSecure()) {
            return false;
        }
        Parcel parcel2 = null;
        BufferedWriter bufferedWriter2 = null;
        parcel2 = null;
        try {
            int indexOf = str2.indexOf(32);
            if (indexOf == -1) {
                indexOf = str2.length();
            }
            int parseLong = (int) Long.parseLong(str2.substring(0, indexOf), 16);
            str2 = indexOf < str2.length() ? str2.substring(indexOf + 1) : "";
            WindowState findWindow = findWindow(parseLong);
            if (findWindow == null) {
                return false;
            }
            Parcel obtain = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("android.view.IWindow");
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeInt(1);
                ParcelFileDescriptor.fromSocket(socket).writeToParcel(obtain, 0);
                parcel = Parcel.obtain();
            } catch (Exception e) {
                e = e;
                bufferedWriter = null;
                parcel = null;
            } catch (Throwable th) {
                th = th;
                bufferedWriter = null;
                parcel = null;
            }
            try {
                findWindow.mClient.asBinder().transact(1, obtain, parcel, 0);
                parcel.readException();
                if (!socket.isOutputShutdown()) {
                    BufferedWriter bufferedWriter3 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    try {
                        bufferedWriter3.write("DONE\n");
                        bufferedWriter3.flush();
                        bufferedWriter2 = bufferedWriter3;
                    } catch (Exception e2) {
                        parcel2 = obtain;
                        bufferedWriter = bufferedWriter3;
                        e = e2;
                        try {
                            if (ProtoLogCache.WM_ERROR_enabled) {
                            }
                            if (parcel2 != null) {
                            }
                            if (parcel != null) {
                            }
                            if (bufferedWriter != null) {
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (parcel2 != null) {
                                parcel2.recycle();
                            }
                            if (parcel != null) {
                                parcel.recycle();
                            }
                            if (bufferedWriter != null) {
                                try {
                                    bufferedWriter.close();
                                } catch (IOException unused) {
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        parcel2 = obtain;
                        bufferedWriter = bufferedWriter3;
                        th = th3;
                        if (parcel2 != null) {
                        }
                        if (parcel != null) {
                        }
                        if (bufferedWriter != null) {
                        }
                        throw th;
                    }
                }
                obtain.recycle();
                parcel.recycle();
                if (bufferedWriter2 != null) {
                    try {
                        bufferedWriter2.close();
                    } catch (IOException unused2) {
                    }
                }
                return true;
            } catch (Exception e3) {
                e = e3;
                bufferedWriter = null;
                parcel2 = obtain;
                if (ProtoLogCache.WM_ERROR_enabled) {
                    ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 2086878461, 0, "Could not send command %s with parameters %s. %s", new Object[]{String.valueOf(str), String.valueOf(str2), String.valueOf(e)});
                }
                if (parcel2 != null) {
                    parcel2.recycle();
                }
                if (parcel != null) {
                    parcel.recycle();
                }
                if (bufferedWriter != null) {
                    return false;
                }
                try {
                    bufferedWriter.close();
                    return false;
                } catch (IOException unused3) {
                    return false;
                }
            } catch (Throwable th4) {
                th = th4;
                bufferedWriter = null;
                parcel2 = obtain;
                if (parcel2 != null) {
                }
                if (parcel != null) {
                }
                if (bufferedWriter != null) {
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            bufferedWriter = null;
            parcel = null;
        } catch (Throwable th5) {
            th = th5;
            bufferedWriter = null;
            parcel = null;
        }
    }

    public void addWindowChangeListener(WindowChangeListener windowChangeListener) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mWindowChangeListeners.add(windowChangeListener);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void removeWindowChangeListener(WindowChangeListener windowChangeListener) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mWindowChangeListeners.remove(windowChangeListener);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyWindowsChanged() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mWindowChangeListeners.isEmpty()) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                WindowChangeListener[] windowChangeListenerArr = (WindowChangeListener[]) this.mWindowChangeListeners.toArray(new WindowChangeListener[this.mWindowChangeListeners.size()]);
                resetPriorityAfterLockedSection();
                for (WindowChangeListener windowChangeListener : windowChangeListenerArr) {
                    windowChangeListener.windowsChanged();
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void notifyFocusChanged() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mWindowChangeListeners.isEmpty()) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                WindowChangeListener[] windowChangeListenerArr = (WindowChangeListener[]) this.mWindowChangeListeners.toArray(new WindowChangeListener[this.mWindowChangeListeners.size()]);
                resetPriorityAfterLockedSection();
                for (WindowChangeListener windowChangeListener : windowChangeListenerArr) {
                    windowChangeListener.focusChanged();
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private WindowState findWindow(final int i) {
        WindowState window;
        if (i == -1) {
            return getFocusedWindow();
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                window = this.mRoot.getWindow(new Predicate() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda25
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$findWindow$9;
                        lambda$findWindow$9 = WindowManagerService.lambda$findWindow$9(i, (WindowState) obj);
                        return lambda$findWindow$9;
                    }
                });
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return window;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$findWindow$9(int i, WindowState windowState) {
        return System.identityHashCode(windowState) == i;
    }

    public Configuration computeNewConfiguration(int i) {
        Configuration computeNewConfigurationLocked;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                computeNewConfigurationLocked = computeNewConfigurationLocked(i);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return computeNewConfigurationLocked;
    }

    private Configuration computeNewConfigurationLocked(int i) {
        if (!this.mDisplayReady) {
            return null;
        }
        Configuration configuration = new Configuration();
        this.mRoot.getDisplayContent(i).computeScreenConfiguration(configuration);
        return configuration;
    }

    void notifyHardKeyboardStatusChange() {
        WindowManagerInternal.OnHardKeyboardStatusChangeListener onHardKeyboardStatusChangeListener;
        boolean z;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                onHardKeyboardStatusChangeListener = this.mHardKeyboardStatusChangeListener;
                z = this.mHardKeyboardAvailable;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        if (onHardKeyboardStatusChangeListener != null) {
            onHardKeyboardStatusChangeListener.onHardKeyboardStatusChange(z);
        }
    }

    public void setEventDispatching(boolean z) {
        if (!checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "setEventDispatching()")) {
            throw new SecurityException("Requires MANAGE_APP_TOKENS permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mEventDispatchingEnabled = z;
                if (this.mDisplayEnabled) {
                    this.mInputManagerCallback.setEventDispatchingLw(z);
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public WindowState getFocusedWindow() {
        WindowState focusedWindowLocked;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                focusedWindowLocked = getFocusedWindowLocked();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return focusedWindowLocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowState getFocusedWindowLocked() {
        return this.mRoot.getTopFocusedDisplayContent().mCurrentFocus;
    }

    Task getImeFocusRootTaskLocked() {
        ActivityRecord activityRecord = this.mRoot.getTopFocusedDisplayContent().mFocusedApp;
        if (activityRecord == null || activityRecord.getTask() == null) {
            return null;
        }
        return activityRecord.getTask().getRootTask();
    }

    public boolean detectSafeMode() {
        if (!this.mInputManagerCallback.waitForInputDevicesReady(1000L) && ProtoLogCache.WM_ERROR_enabled) {
            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1774661765, 1, "Devices still not ready after waiting %d milliseconds before attempting to detect safe mode.", new Object[]{1000L});
        }
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "safe_boot_disallowed", 0) != 0) {
            return false;
        }
        int keyCodeState = this.mInputManager.getKeyCodeState(-1, -256, 82);
        int keyCodeState2 = this.mInputManager.getKeyCodeState(-1, -256, 47);
        int keyCodeState3 = this.mInputManager.getKeyCodeState(-1, 513, 23);
        int scanCodeState = this.mInputManager.getScanCodeState(-1, 65540, 272);
        this.mSafeMode = keyCodeState > 0 || keyCodeState2 > 0 || keyCodeState3 > 0 || scanCodeState > 0 || this.mInputManager.getKeyCodeState(-1, -256, 25) > 0;
        try {
            if (SystemProperties.getInt("persist.sys.safemode", 0) != 0 || SystemProperties.getInt("ro.sys.safemode", 0) != 0) {
                this.mSafeMode = true;
                SystemProperties.set("persist.sys.safemode", "");
            }
        } catch (IllegalArgumentException unused) {
        }
        if (this.mSafeMode) {
            if (ProtoLogCache.WM_ERROR_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_ERROR, -1443029505, 85, "SAFE MODE ENABLED (menu=%d s=%d dpad=%d trackball=%d)", new Object[]{Long.valueOf(keyCodeState), Long.valueOf(keyCodeState2), Long.valueOf(keyCodeState3), Long.valueOf(scanCodeState)});
            }
            if (SystemProperties.getInt("ro.sys.safemode", 0) == 0) {
                SystemProperties.set("ro.sys.safemode", "1");
            }
        } else if (ProtoLogCache.WM_ERROR_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_ERROR, 1866772666, 0, "SAFE MODE not enabled", (Object[]) null);
        }
        this.mPolicy.setSafeMode(this.mSafeMode);
        return this.mSafeMode;
    }

    public void displayReady() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mMaxUiWidth > 0) {
                    this.mRoot.forAllDisplays(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda8
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            WindowManagerService.this.lambda$displayReady$10((DisplayContent) obj);
                        }
                    });
                }
                applyForcedPropertiesForDefaultDisplay();
                this.mAnimator.ready();
                this.mDisplayReady = true;
                this.mWindowManagerServiceExt.hookDisplayReady(this, this.mContext);
                this.mRoot.forAllDisplays(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda9
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((DisplayContent) obj).reconfigureDisplayLocked();
                    }
                });
                this.mIsTouchDevice = this.mContext.getPackageManager().hasSystemFeature("android.hardware.touchscreen");
                this.mIsFakeTouchDevice = this.mContext.getPackageManager().hasSystemFeature("android.hardware.faketouch");
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        this.mAtmService.updateConfiguration(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$displayReady$10(DisplayContent displayContent) {
        displayContent.setMaxUiWidth(this.mMaxUiWidth);
    }

    public void systemReady() {
        this.mSystemReady = true;
        this.mPolicy.systemReady();
        this.mRoot.forAllDisplayPolicies(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((DisplayPolicy) obj).systemReady();
            }
        });
        this.mSnapshotController.systemReady();
        this.mHasWideColorGamutSupport = queryWideColorGamutSupport();
        this.mHasHdrSupport = queryHdrSupport();
        Handler handler = UiThread.getHandler();
        final SettingsObserver settingsObserver = this.mSettingsObserver;
        Objects.requireNonNull(settingsObserver);
        handler.post(new Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                WindowManagerService.SettingsObserver.this.loadSettings();
            }
        });
        IVrManager asInterface = IVrManager.Stub.asInterface(ServiceManager.getService("vrmanager"));
        if (asInterface != null) {
            try {
                boolean vrModeState = asInterface.getVrModeState();
                WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
                boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        asInterface.registerListener(this.mVrStateCallbacks);
                        if (vrModeState) {
                            this.mVrModeEnabled = vrModeState;
                            this.mVrStateCallbacks.onVrStateChanged(vrModeState);
                        }
                    } catch (Throwable th) {
                        resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                resetPriorityAfterLockedSection();
            } catch (RemoteException unused) {
            }
        }
        this.mWindowManagerServiceExt.endHookSystemReady();
    }

    private static boolean queryWideColorGamutSupport() {
        Optional has_wide_color_display = SurfaceFlingerProperties.has_wide_color_display();
        if (has_wide_color_display.isPresent()) {
            return ((Boolean) has_wide_color_display.get()).booleanValue();
        }
        try {
            OptionalBool hasWideColorDisplay = ISurfaceFlingerConfigs.getService().hasWideColorDisplay();
            if (hasWideColorDisplay != null) {
                return hasWideColorDisplay.value;
            }
            return false;
        } catch (RemoteException | NoSuchElementException unused) {
            return false;
        }
    }

    private static boolean queryHdrSupport() {
        Optional has_HDR_display = SurfaceFlingerProperties.has_HDR_display();
        if (has_HDR_display.isPresent()) {
            return ((Boolean) has_HDR_display.get()).booleanValue();
        }
        try {
            OptionalBool hasHDRDisplay = ISurfaceFlingerConfigs.getService().hasHDRDisplay();
            if (hasHDRDisplay != null) {
                return hasHDRDisplay.value;
            }
            return false;
        } catch (RemoteException | NoSuchElementException unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputTarget getInputTargetFromToken(IBinder iBinder) {
        WindowState windowState = this.mInputToWindowMap.get(iBinder);
        if (windowState != null) {
            return windowState;
        }
        EmbeddedWindowController.EmbeddedWindow embeddedWindow = this.mEmbeddedWindowController.get(iBinder);
        if (embeddedWindow != null) {
            return embeddedWindow;
        }
        return null;
    }

    InputTarget getInputTargetFromWindowTokenLocked(IBinder iBinder) {
        WindowState windowState = this.mWindowMap.get(iBinder);
        return windowState != null ? windowState : this.mEmbeddedWindowController.getByWindowToken(iBinder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportFocusChanged(IBinder iBinder, IBinder iBinder2) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                InputTarget inputTargetFromToken = getInputTargetFromToken(iBinder);
                InputTarget inputTargetFromToken2 = getInputTargetFromToken(iBinder2);
                if (inputTargetFromToken2 == null && inputTargetFromToken == null) {
                    Slog.v(TAG, "Unknown focus tokens, dropping reportFocusChanged");
                    resetPriorityAfterLockedSection();
                    return;
                }
                this.mFocusedInputTarget = inputTargetFromToken2;
                this.mAccessibilityController.onFocusChanged(inputTargetFromToken, inputTargetFromToken2);
                if (ProtoLogCache.WM_DEBUG_FOCUS_LIGHT_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, 115358443, 0, (String) null, new Object[]{String.valueOf(inputTargetFromToken), String.valueOf(inputTargetFromToken2)});
                }
                resetPriorityAfterLockedSection();
                WindowState windowState = inputTargetFromToken2 != null ? inputTargetFromToken2.getWindowState() : null;
                if (windowState != null && windowState.mInputChannelToken == iBinder2) {
                    this.mAnrController.onFocusChanged(windowState);
                    windowState.reportFocusChangedSerialized(true);
                    notifyFocusChanged();
                }
                WindowState windowState2 = inputTargetFromToken != null ? inputTargetFromToken.getWindowState() : null;
                if (windowState2 == null || windowState2.mInputChannelToken != iBinder) {
                    return;
                }
                windowState2.reportFocusChangedSerialized(false);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public final class H extends Handler {
        public static final int ANIMATION_FAILSAFE = 60;
        public static final int APP_FREEZE_TIMEOUT = 17;
        public static final int BOOT_TIMEOUT = 23;
        public static final int CHECK_IF_BOOT_ANIMATION_FINISHED = 37;
        public static final int CLIENT_FREEZE_TIMEOUT = 30;
        public static final int ENABLE_SCREEN = 16;
        public static final int INSETS_CHANGED = 66;
        public static final int NEW_ANIMATOR_SCALE = 34;
        public static final int NOTIFY_ACTIVITY_DRAWN = 32;
        public static final int ON_POINTER_DOWN_OUTSIDE_FOCUS = 62;
        public static final int PERSIST_ANIMATION_SCALE = 14;
        public static final int RECOMPUTE_FOCUS = 61;
        public static final int REPARENT_TASK_TO_DEFAULT_DISPLAY = 65;
        public static final int REPORT_HARD_KEYBOARD_STATUS_CHANGE = 22;
        public static final int REPORT_WINDOWS_CHANGE = 19;
        public static final int RESET_ANR_MESSAGE = 38;
        public static final int RESTORE_POINTER_ICON = 55;
        public static final int SET_HAS_OVERLAY_UI = 58;
        public static final int SHOW_EMULATOR_DISPLAY_OVERLAY = 36;
        public static final int SHOW_STRICT_MODE_VIOLATION = 25;
        public static final int UNUSED = 0;
        public static final int UPDATE_ANIMATION_SCALE = 51;
        public static final int UPDATE_MULTI_WINDOW_STACKS = 41;
        public static final int WAITING_FOR_DRAWN_TIMEOUT = 24;
        public static final int WALLPAPER_DRAW_PENDING_TIMEOUT = 39;
        public static final int WINDOW_FREEZE_TIMEOUT = 11;
        public static final int WINDOW_HIDE_TIMEOUT = 52;
        public static final int WINDOW_STATE_BLAST_SYNC_TIMEOUT = 64;

        H() {
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:30:0x0056. Please report as an issue. */
        /* JADX WARN: Failed to find 'out' block for switch in B:31:0x0059. Please report as an issue. */
        /* JADX WARN: Failed to find 'out' block for switch in B:32:0x005c. Please report as an issue. */
        /* JADX WARN: Failed to find 'out' block for switch in B:33:0x005f. Please report as an issue. */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Message remove;
            boolean checkBootAnimationCompleteLocked;
            if (WindowManagerDebugConfig.DEBUG_WINDOW_TRACE) {
                Slog.v(WindowManagerService.TAG, "handleMessage: entry what=" + message.what);
            }
            int i = message.what;
            if (i == 11) {
                DisplayContent displayContent = (DisplayContent) message.obj;
                WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
                WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        displayContent.onWindowFreezeTimeout();
                    } finally {
                        WindowManagerService.resetPriorityAfterLockedSection();
                    }
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            } else if (i != 14) {
                if (i == 19) {
                    WindowManagerService windowManagerService = WindowManagerService.this;
                    if (windowManagerService.mWindowsChanged) {
                        WindowManagerGlobalLock windowManagerGlobalLock2 = windowManagerService.mGlobalLock;
                        WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock2) {
                            try {
                                WindowManagerService.this.mWindowsChanged = false;
                            } finally {
                                WindowManagerService.resetPriorityAfterLockedSection();
                            }
                        }
                        WindowManagerService.resetPriorityAfterLockedSection();
                        WindowManagerService.this.notifyWindowsChanged();
                    }
                } else if (i == 30) {
                    WindowManagerGlobalLock windowManagerGlobalLock3 = WindowManagerService.this.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock3) {
                        try {
                            WindowManagerService windowManagerService2 = WindowManagerService.this;
                            if (windowManagerService2.mClientFreezingScreen) {
                                windowManagerService2.mClientFreezingScreen = false;
                                windowManagerService2.mLastFinishedFreezeSource = "client-timeout";
                                windowManagerService2.stopFreezingDisplayLocked();
                            }
                        } finally {
                            WindowManagerService.resetPriorityAfterLockedSection();
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } else if (i == 32) {
                    ActivityRecord activityRecord = (ActivityRecord) message.obj;
                    WindowManagerGlobalLock windowManagerGlobalLock4 = WindowManagerService.this.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock4) {
                        try {
                            if (activityRecord.isAttached()) {
                                activityRecord.getRootTask().notifyActivityDrawnLocked(activityRecord);
                            }
                        } finally {
                            WindowManagerService.resetPriorityAfterLockedSection();
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } else if (i == 34) {
                    float currentAnimatorScale = WindowManagerService.this.getCurrentAnimatorScale();
                    ValueAnimator.setDurationScale(currentAnimatorScale);
                    Session session = (Session) message.obj;
                    if (session != null) {
                        try {
                            session.mCallback.onAnimatorScaleChanged(currentAnimatorScale);
                        } catch (RemoteException unused) {
                        }
                    } else {
                        ArrayList arrayList = new ArrayList();
                        WindowManagerGlobalLock windowManagerGlobalLock5 = WindowManagerService.this.mGlobalLock;
                        WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock5) {
                            for (int i2 = 0; i2 < WindowManagerService.this.mSessions.size(); i2++) {
                                try {
                                    arrayList.add(WindowManagerService.this.mSessions.valueAt(i2).mCallback);
                                } finally {
                                    WindowManagerService.resetPriorityAfterLockedSection();
                                }
                            }
                        }
                        WindowManagerService.resetPriorityAfterLockedSection();
                        for (int i3 = 0; i3 < arrayList.size(); i3++) {
                            try {
                                ((IWindowSessionCallback) arrayList.get(i3)).onAnimatorScaleChanged(currentAnimatorScale);
                            } catch (RemoteException unused2) {
                            }
                        }
                    }
                } else if (i == 41) {
                    WindowManagerGlobalLock windowManagerGlobalLock6 = WindowManagerService.this.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock6) {
                        try {
                            DisplayContent displayContent2 = (DisplayContent) message.obj;
                            if (displayContent2 != null) {
                                displayContent2.adjustForImeIfNeeded();
                            }
                        } finally {
                            WindowManagerService.resetPriorityAfterLockedSection();
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } else if (i == 55) {
                    WindowManagerGlobalLock windowManagerGlobalLock7 = WindowManagerService.this.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock7) {
                        try {
                            WindowManagerService.this.restorePointerIconLocked((DisplayContent) message.obj, message.arg1, message.arg2);
                        } finally {
                            WindowManagerService.resetPriorityAfterLockedSection();
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } else if (i == 58) {
                    WindowManagerService.this.mAmInternal.setHasOverlayUi(message.arg1, message.arg2 == 1);
                } else if (i == 16) {
                    WindowManagerService.this.performEnableScreen();
                } else if (i == 17) {
                    WindowManagerGlobalLock windowManagerGlobalLock8 = WindowManagerService.this.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock8) {
                        try {
                            if (ProtoLogCache.WM_ERROR_enabled) {
                                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -322035974, 0, "App freeze timeout expired.", (Object[]) null);
                            }
                            WindowManagerService windowManagerService3 = WindowManagerService.this;
                            windowManagerService3.mWindowsFreezingScreen = 2;
                            try {
                                for (int size = windowManagerService3.mAppFreezeListeners.size() - 1; size >= 0; size--) {
                                    WindowManagerService.this.mAppFreezeListeners.get(size).onAppFreezeTimeout();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } finally {
                            WindowManagerService.resetPriorityAfterLockedSection();
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } else if (i == 51) {
                    int i4 = message.arg1;
                    if (i4 == 0) {
                        WindowManagerService windowManagerService4 = WindowManagerService.this;
                        windowManagerService4.mWindowAnimationScaleSetting = windowManagerService4.getWindowAnimationScaleSetting();
                    } else if (i4 == 1) {
                        WindowManagerService windowManagerService5 = WindowManagerService.this;
                        windowManagerService5.mTransitionAnimationScaleSetting = windowManagerService5.getTransitionAnimationScaleSetting();
                    } else if (i4 == 2) {
                        WindowManagerService windowManagerService6 = WindowManagerService.this;
                        windowManagerService6.mAnimatorDurationScaleSetting = windowManagerService6.getAnimatorDurationScaleSetting();
                        WindowManagerService.this.dispatchNewAnimatorScaleLocked(null);
                    }
                } else if (i != 52) {
                    switch (i) {
                        case 22:
                            WindowManagerService.this.notifyHardKeyboardStatusChange();
                            break;
                        case 23:
                            WindowManagerService.this.performBootTimeout();
                            break;
                        case 24:
                            WindowManagerService.this.mTheiaManagerExt.sendEvent(260L, SystemClock.uptimeMillis(), 0, 0, 4099L, (String) null);
                            WindowContainer windowContainer = (WindowContainer) message.obj;
                            WindowManagerGlobalLock windowManagerGlobalLock9 = WindowManagerService.this.mGlobalLock;
                            WindowManagerService.boostPriorityForLockedSection();
                            synchronized (windowManagerGlobalLock9) {
                                try {
                                    if (ProtoLogCache.WM_ERROR_enabled) {
                                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1526645239, 0, "Timeout waiting for drawn: undrawn=%s", new Object[]{String.valueOf(windowContainer.mWaitingForDrawn)});
                                    }
                                    if (Trace.isTagEnabled(32L)) {
                                        for (int i5 = 0; i5 < windowContainer.mWaitingForDrawn.size(); i5++) {
                                            WindowManagerService.this.traceEndWaitingForWindowDrawn(windowContainer.mWaitingForDrawn.get(i5));
                                        }
                                    }
                                    WindowManagerService.this.mWindowManagerServiceExt.clearSwitchPhysicalDisplayFlag(windowContainer);
                                    windowContainer.mWaitingForDrawn.clear();
                                    WindowManagerService.this.mWindowManagerServiceExt.clearSkipWaitingForDrawn();
                                    remove = WindowManagerService.this.mWaitingForDrawnCallbacks.remove(windowContainer);
                                } finally {
                                    WindowManagerService.resetPriorityAfterLockedSection();
                                }
                            }
                            WindowManagerService.resetPriorityAfterLockedSection();
                            if (remove != null) {
                                remove.sendToTarget();
                            }
                            WindowManagerService.this.mWindowManagerServiceExt.allWindowsDraw();
                            break;
                        case 25:
                            WindowManagerService.this.showStrictModeViolation(message.arg1, message.arg2);
                            break;
                        default:
                            switch (i) {
                                case 36:
                                    WindowManagerService.this.showEmulatorDisplayOverlay();
                                    break;
                                case 37:
                                    WindowManagerGlobalLock windowManagerGlobalLock10 = WindowManagerService.this.mGlobalLock;
                                    WindowManagerService.boostPriorityForLockedSection();
                                    synchronized (windowManagerGlobalLock10) {
                                        try {
                                            if (ProtoLogCache.WM_DEBUG_BOOT_enabled) {
                                                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_BOOT, 2034780299, 0, (String) null, (Object[]) null);
                                            }
                                            checkBootAnimationCompleteLocked = WindowManagerService.this.checkBootAnimationCompleteLocked();
                                        } finally {
                                            WindowManagerService.resetPriorityAfterLockedSection();
                                        }
                                    }
                                    WindowManagerService.resetPriorityAfterLockedSection();
                                    if (checkBootAnimationCompleteLocked) {
                                        WindowManagerService.this.performEnableScreen();
                                        break;
                                    }
                                    break;
                                case 38:
                                    WindowManagerGlobalLock windowManagerGlobalLock11 = WindowManagerService.this.mGlobalLock;
                                    WindowManagerService.boostPriorityForLockedSection();
                                    synchronized (windowManagerGlobalLock11) {
                                        try {
                                            WindowManagerService windowManagerService7 = WindowManagerService.this;
                                            windowManagerService7.mLastANRState = null;
                                            windowManagerService7.mAtmService.mLastANRState = null;
                                        } finally {
                                            WindowManagerService.resetPriorityAfterLockedSection();
                                        }
                                    }
                                    WindowManagerService.resetPriorityAfterLockedSection();
                                    break;
                                case 39:
                                    WindowManagerGlobalLock windowManagerGlobalLock12 = WindowManagerService.this.mGlobalLock;
                                    WindowManagerService.boostPriorityForLockedSection();
                                    synchronized (windowManagerGlobalLock12) {
                                        try {
                                            WallpaperController wallpaperController = (WallpaperController) message.obj;
                                            if (wallpaperController != null && wallpaperController.processWallpaperDrawPendingTimeout()) {
                                                WindowManagerService.this.mWindowPlacerLocked.performSurfacePlacement();
                                            }
                                        } finally {
                                            WindowManagerService.resetPriorityAfterLockedSection();
                                        }
                                    }
                                    WindowManagerService.resetPriorityAfterLockedSection();
                                    break;
                                default:
                                    switch (i) {
                                        case 60:
                                            WindowManagerGlobalLock windowManagerGlobalLock13 = WindowManagerService.this.mGlobalLock;
                                            WindowManagerService.boostPriorityForLockedSection();
                                            synchronized (windowManagerGlobalLock13) {
                                                try {
                                                    if (WindowManagerService.this.mRecentsAnimationController != null) {
                                                        WindowManagerService.this.mRecentsAnimationController.scheduleFailsafe();
                                                    }
                                                } finally {
                                                    WindowManagerService.resetPriorityAfterLockedSection();
                                                }
                                            }
                                            WindowManagerService.resetPriorityAfterLockedSection();
                                            break;
                                        case 61:
                                            WindowManagerGlobalLock windowManagerGlobalLock14 = WindowManagerService.this.mGlobalLock;
                                            WindowManagerService.boostPriorityForLockedSection();
                                            synchronized (windowManagerGlobalLock14) {
                                                try {
                                                    WindowManagerService.this.updateFocusedWindowLocked(0, true);
                                                } finally {
                                                    WindowManagerService.resetPriorityAfterLockedSection();
                                                }
                                            }
                                            WindowManagerService.resetPriorityAfterLockedSection();
                                            break;
                                        case 62:
                                            WindowManagerGlobalLock windowManagerGlobalLock15 = WindowManagerService.this.mGlobalLock;
                                            WindowManagerService.boostPriorityForLockedSection();
                                            synchronized (windowManagerGlobalLock15) {
                                                try {
                                                    IBinder iBinder = (IBinder) message.obj;
                                                    WindowManagerService windowManagerService8 = WindowManagerService.this;
                                                    windowManagerService8.onPointerDownOutsideFocusLocked(windowManagerService8.getInputTargetFromToken(iBinder));
                                                } finally {
                                                    WindowManagerService.resetPriorityAfterLockedSection();
                                                }
                                            }
                                            WindowManagerService.resetPriorityAfterLockedSection();
                                            break;
                                        default:
                                            switch (i) {
                                                case 64:
                                                    WindowManagerGlobalLock windowManagerGlobalLock16 = WindowManagerService.this.mGlobalLock;
                                                    WindowManagerService.boostPriorityForLockedSection();
                                                    synchronized (windowManagerGlobalLock16) {
                                                        try {
                                                            WindowState windowState = (WindowState) message.obj;
                                                            Slog.i(WindowManagerService.TAG, "Blast sync timeout: " + windowState);
                                                            windowState.immediatelyNotifyBlastSync();
                                                        } finally {
                                                            WindowManagerService.resetPriorityAfterLockedSection();
                                                        }
                                                    }
                                                    WindowManagerService.resetPriorityAfterLockedSection();
                                                    break;
                                                case 65:
                                                    WindowManagerGlobalLock windowManagerGlobalLock17 = WindowManagerService.this.mGlobalLock;
                                                    WindowManagerService.boostPriorityForLockedSection();
                                                    synchronized (windowManagerGlobalLock17) {
                                                        try {
                                                            Task task = (Task) message.obj;
                                                            task.reparent(WindowManagerService.this.mRoot.getDefaultTaskDisplayArea(), true);
                                                            task.resumeNextFocusAfterReparent();
                                                        } finally {
                                                        }
                                                    }
                                                    WindowManagerService.resetPriorityAfterLockedSection();
                                                    break;
                                                case 66:
                                                    WindowManagerGlobalLock windowManagerGlobalLock18 = WindowManagerService.this.mGlobalLock;
                                                    WindowManagerService.boostPriorityForLockedSection();
                                                    synchronized (windowManagerGlobalLock18) {
                                                        try {
                                                            WindowManagerService windowManagerService9 = WindowManagerService.this;
                                                            if (windowManagerService9.mWindowsInsetsChanged > 0) {
                                                                windowManagerService9.mWindowsInsetsChanged = 0;
                                                                windowManagerService9.mWindowPlacerLocked.performSurfacePlacement();
                                                            }
                                                        } finally {
                                                        }
                                                    }
                                                    WindowManagerService.resetPriorityAfterLockedSection();
                                                    break;
                                                default:
                                                    WindowManagerService.this.mWindowManagerServiceExt.handleOplusMessage(message);
                                                    break;
                                            }
                                    }
                            }
                    }
                } else {
                    WindowState windowState2 = (WindowState) message.obj;
                    WindowManagerGlobalLock windowManagerGlobalLock19 = WindowManagerService.this.mGlobalLock;
                    WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock19) {
                        try {
                            windowState2.mAttrs.flags &= -129;
                            windowState2.hidePermanentlyLw();
                            windowState2.setDisplayLayoutNeeded();
                            WindowManagerService.this.mWindowPlacerLocked.performSurfacePlacement();
                        } finally {
                            WindowManagerService.resetPriorityAfterLockedSection();
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                }
            } else {
                Settings.Global.putFloat(WindowManagerService.this.mContext.getContentResolver(), "window_animation_scale", WindowManagerService.this.mWindowAnimationScaleSetting);
                Settings.Global.putFloat(WindowManagerService.this.mContext.getContentResolver(), "transition_animation_scale", WindowManagerService.this.mTransitionAnimationScaleSetting);
                Settings.Global.putFloat(WindowManagerService.this.mContext.getContentResolver(), "animator_duration_scale", WindowManagerService.this.mAnimatorDurationScaleSetting);
            }
            if (WindowManagerDebugConfig.DEBUG_WINDOW_TRACE) {
                Slog.v(WindowManagerService.TAG, "handleMessage: exit");
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void sendNewMessageDelayed(int i, Object obj, long j) {
            removeMessages(i, obj);
            sendMessageDelayed(obtainMessage(i, obj), j);
        }
    }

    public IWindowSession openSession(IWindowSessionCallback iWindowSessionCallback) {
        return new Session(this, iWindowSessionCallback);
    }

    public boolean useBLAST() {
        return this.mUseBLAST;
    }

    public void getInitialDisplaySize(int i, Point point) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent != null && displayContent.hasAccess(Binder.getCallingUid())) {
                    point.x = displayContent.mInitialDisplayWidth;
                    point.y = displayContent.mInitialDisplayHeight;
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void getBaseDisplaySize(int i, Point point) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent != null && displayContent.hasAccess(Binder.getCallingUid())) {
                    point.x = displayContent.mBaseDisplayWidth;
                    point.y = displayContent.mBaseDisplayHeight;
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void setForcedDisplaySize(int i, int i2, int i3) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
            throw new SecurityException("Must hold permission android.permission.WRITE_SECURE_SETTINGS");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent != null) {
                        displayContent.setForcedSize(i2, i3);
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setForcedDisplayScalingMode(int i, int i2) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
            throw new SecurityException("Must hold permission android.permission.WRITE_SECURE_SETTINGS");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent != null) {
                        displayContent.setForcedScalingMode(i2);
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSandboxDisplayApis(int i, boolean z) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
            throw new SecurityException("Must hold permission android.permission.WRITE_SECURE_SETTINGS");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent != null) {
                        displayContent.setSandboxDisplayApis(z);
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00a4  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00a6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean applyForcedPropertiesForDefaultDisplay() {
        boolean z;
        int forcedDisplayDensityForUserLocked;
        int indexOf;
        int i;
        int i2;
        DisplayContent defaultDisplayContentLocked = getDefaultDisplayContentLocked();
        String string = Settings.Global.getString(this.mContext.getContentResolver(), "display_size_forced");
        if (string == null || string.length() == 0) {
            string = SystemProperties.get(SIZE_OVERRIDE, (String) null);
        }
        if (string != null && string.length() > 0 && (indexOf = string.indexOf(44)) > 0 && string.lastIndexOf(44) == indexOf) {
            try {
                Point validForcedSize = defaultDisplayContentLocked.getValidForcedSize(Integer.parseInt(string.substring(0, indexOf)), Integer.parseInt(string.substring(indexOf + 1)));
                i = validForcedSize.x;
                i2 = validForcedSize.y;
            } catch (NumberFormatException unused) {
            }
            if (defaultDisplayContentLocked.mBaseDisplayWidth != i || defaultDisplayContentLocked.mBaseDisplayHeight != i2) {
                if (ProtoLogCache.WM_ERROR_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_ERROR, 1115417974, 5, "FORCED DISPLAY SIZE: %dx%d", new Object[]{Long.valueOf(i), Long.valueOf(i2)});
                }
                defaultDisplayContentLocked.updateBaseDisplayMetrics(i, i2, defaultDisplayContentLocked.mBaseDisplayDensity, defaultDisplayContentLocked.mBaseDisplayPhysicalXDpi, defaultDisplayContentLocked.mBaseDisplayPhysicalYDpi);
                z = true;
                forcedDisplayDensityForUserLocked = getForcedDisplayDensityForUserLocked(this.mCurrentUserId);
                if (forcedDisplayDensityForUserLocked != 0 && forcedDisplayDensityForUserLocked != defaultDisplayContentLocked.mBaseDisplayDensity) {
                    defaultDisplayContentLocked.mBaseDisplayDensity = forcedDisplayDensityForUserLocked;
                    z = true;
                }
                if (defaultDisplayContentLocked.mDisplayScalingDisabled != (Settings.Global.getInt(this.mContext.getContentResolver(), "display_scaling_force", 0) == 0)) {
                    return z;
                }
                if (ProtoLogCache.WM_ERROR_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_ERROR, 954470154, 0, "FORCED DISPLAY SCALING DISABLED", (Object[]) null);
                }
                defaultDisplayContentLocked.mDisplayScalingDisabled = true;
                return true;
            }
        }
        z = false;
        forcedDisplayDensityForUserLocked = getForcedDisplayDensityForUserLocked(this.mCurrentUserId);
        if (forcedDisplayDensityForUserLocked != 0) {
            defaultDisplayContentLocked.mBaseDisplayDensity = forcedDisplayDensityForUserLocked;
            z = true;
        }
        if (defaultDisplayContentLocked.mDisplayScalingDisabled != (Settings.Global.getInt(this.mContext.getContentResolver(), "display_scaling_force", 0) == 0)) {
        }
    }

    public void clearForcedDisplaySize(int i) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
            throw new SecurityException("Must hold permission android.permission.WRITE_SECURE_SETTINGS");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent != null) {
                        int i2 = displayContent.mInitialDisplayWidth;
                        int i3 = displayContent.mInitialDisplayHeight;
                        float f = displayContent.mInitialPhysicalXDpi;
                        displayContent.setForcedSize(i2, i3, f, f);
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public int getInitialDisplayDensity(int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent != null && displayContent.hasAccess(Binder.getCallingUid())) {
                    int initialDisplayDensity = displayContent.getInitialDisplayDensity();
                    resetPriorityAfterLockedSection();
                    return initialDisplayDensity;
                }
                DisplayInfo displayInfo = this.mDisplayManagerInternal.getDisplayInfo(i);
                if (displayInfo == null || !displayInfo.hasAccess(Binder.getCallingUid())) {
                    resetPriorityAfterLockedSection();
                    return -1;
                }
                int i2 = displayInfo.logicalDensityDpi;
                resetPriorityAfterLockedSection();
                return i2;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public int getBaseDisplayDensity(int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null || !displayContent.hasAccess(Binder.getCallingUid())) {
                    resetPriorityAfterLockedSection();
                    return -1;
                }
                int i2 = displayContent.mBaseDisplayDensity;
                resetPriorityAfterLockedSection();
                return i2;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public int getDisplayIdByUniqueId(String str) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(str);
                if (displayContent == null || !displayContent.hasAccess(Binder.getCallingUid())) {
                    resetPriorityAfterLockedSection();
                    return -1;
                }
                int i = displayContent.mDisplayId;
                resetPriorityAfterLockedSection();
                return i;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setForcedDisplayDensityForUser(int i, int i2, int i3) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
            throw new SecurityException("Must hold permission android.permission.WRITE_SECURE_SETTINGS");
        }
        int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i3, false, true, "setForcedDisplayDensityForUser", null);
        Slog.i(TAG, "setForcedDisplayDensityForUser  density:" + i2 + " pid:" + Binder.getCallingPid() + " uid:" + Binder.getCallingUid() + " caller:" + Debug.getCallers(5));
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent != null) {
                        displayContent.setForcedDensity(i2, handleIncomingUser);
                        this.mWindowManagerServiceExt.onSetDensityForUser(i2, i3);
                        DisplayInfo needForceSetDensityDisplayInfo = this.mWindowManagerServiceExt.getNeedForceSetDensityDisplayInfo(this, i, -1);
                        if (displayContent.isDefaultDisplay && needForceSetDensityDisplayInfo != null) {
                            this.mDisplayWindowSettings.setForcedDensity(needForceSetDensityDisplayInfo, i2, i3);
                        }
                    } else {
                        DisplayInfo displayInfo = this.mDisplayManagerInternal.getDisplayInfo(i);
                        if (displayInfo != null) {
                            this.mDisplayWindowSettings.setForcedDensity(displayInfo, i2, i3);
                        }
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void clearForcedDisplayDensityForUser(int i, int i2) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
            throw new SecurityException("Must hold permission android.permission.WRITE_SECURE_SETTINGS");
        }
        int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i2, false, true, "clearForcedDisplayDensityForUser", null);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent != null) {
                        displayContent.setForcedDensity(displayContent.getInitialDisplayDensity(), handleIncomingUser);
                        this.mWindowManagerServiceExt.onSetDensityForUser(displayContent.mInitialDisplayDensity, i2);
                        DisplayInfo needForceSetDensityDisplayInfo = this.mWindowManagerServiceExt.getNeedForceSetDensityDisplayInfo(this, i, -1);
                        if (displayContent.isDefaultDisplay && needForceSetDensityDisplayInfo != null) {
                            this.mDisplayWindowSettings.setForcedDensity(needForceSetDensityDisplayInfo, displayContent.mInitialDisplayDensity);
                        }
                    } else {
                        DisplayInfo displayInfo = this.mDisplayManagerInternal.getDisplayInfo(i);
                        if (displayInfo != null) {
                            this.mDisplayWindowSettings.setForcedDensity(displayInfo, displayInfo.logicalDensityDpi, i2);
                        }
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getForcedDisplayDensityForUserLocked(int i) {
        String stringForUser = Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "display_density_forced", i);
        if (stringForUser == null || stringForUser.length() == 0) {
            stringForUser = SystemProperties.get(DENSITY_OVERRIDE, (String) null);
        }
        if (stringForUser == null || stringForUser.length() <= 0) {
            return 0;
        }
        try {
            return Integer.parseInt(stringForUser);
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    public void startWindowTrace() {
        this.mWindowTracing.startTrace(null);
    }

    public void stopWindowTrace() {
        this.mWindowTracing.stopTrace(null);
    }

    public void saveWindowTraceToFile() {
        this.mWindowTracing.saveForBugreport(null);
    }

    public boolean isWindowTraceEnabled() {
        return this.mWindowTracing.isEnabled();
    }

    public void startTransitionTrace() {
        this.mTransitionTracer.startTrace(null);
    }

    public void stopTransitionTrace() {
        this.mTransitionTracer.stopTrace(null);
    }

    public boolean isTransitionTraceEnabled() {
        return this.mTransitionTracer.isActiveTracingEnabled();
    }

    public boolean registerCrossWindowBlurEnabledListener(ICrossWindowBlurEnabledListener iCrossWindowBlurEnabledListener) {
        return this.mBlurController.registerCrossWindowBlurEnabledListener(iCrossWindowBlurEnabledListener);
    }

    public void unregisterCrossWindowBlurEnabledListener(ICrossWindowBlurEnabledListener iCrossWindowBlurEnabledListener) {
        this.mBlurController.unregisterCrossWindowBlurEnabledListener(iCrossWindowBlurEnabledListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final WindowState windowForClientLocked(Session session, IWindow iWindow, boolean z) {
        return windowForClientLocked(session, iWindow.asBinder(), z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final WindowState windowForClientLocked(Session session, IBinder iBinder, boolean z) {
        WindowState windowState = this.mWindowMap.get(iBinder);
        if (WindowManagerDebugConfig.DEBUG) {
            Slog.v(TAG, "Looking up client " + iBinder + ": " + windowState);
        }
        if (windowState == null) {
            if (z) {
                throw new IllegalArgumentException("Requested window " + iBinder + " does not exist");
            }
            if (ProtoLogCache.WM_ERROR_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -2101985723, 0, "Failed looking up window session=%s callers=%s", new Object[]{String.valueOf(session), String.valueOf(Debug.getCallers(3))});
            }
            return null;
        }
        if (session == null || windowState.mSession == session) {
            return windowState;
        }
        if (z) {
            throw new IllegalArgumentException("Requested window " + iBinder + " is in session " + windowState.mSession + ", not " + session);
        }
        if (ProtoLogCache.WM_ERROR_enabled) {
            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -2101985723, 0, "Failed looking up window session=%s callers=%s", new Object[]{String.valueOf(session), String.valueOf(Debug.getCallers(3))});
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void makeWindowFreezingScreenIfNeededLocked(WindowState windowState) {
        int i = this.mFrozenDisplayId;
        if (i == -1 || i != windowState.getDisplayId() || this.mWindowsFreezingScreen == 2) {
            return;
        }
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -1632122349, 0, (String) null, new Object[]{String.valueOf(windowState)});
        }
        if (windowState.isVisibleRequested()) {
            windowState.setOrientationChanging(true);
        }
        if (!this.mWindowManagerServiceExt.shouldSkipUnFreezeCheck(windowState)) {
            this.mRoot.mOrientationChangeComplete = false;
        }
        if (this.mWindowsFreezingScreen != 0 || this.mWindowManagerServiceExt.shouldSkipUnFreezeCheck(windowState)) {
            return;
        }
        this.mWindowsFreezingScreen = 1;
        this.mH.sendNewMessageDelayed(11, windowState.getDisplayContent(), 2000L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkDrawnWindowsLocked() {
        if (this.mWaitingForDrawnCallbacks.isEmpty()) {
            return;
        }
        for (int size = this.mWaitingForDrawnCallbacks.size() - 1; size >= 0; size--) {
            WindowContainer<?> keyAt = this.mWaitingForDrawnCallbacks.keyAt(size);
            boolean z = !keyAt.mWaitingForDrawn.isEmpty();
            int size2 = keyAt.mWaitingForDrawn.size();
            while (true) {
                size2--;
                if (size2 < 0) {
                    break;
                }
                WindowState windowState = keyAt.mWaitingForDrawn.get(size2);
                if (!ProtoLogGroup.WM_DEBUG_SCREEN_ON.isLogToLogcat()) {
                    Slog.i(TAG, String.format("Waiting for drawn %s: removed=%b visible=%b mHasSurface=%b drawState=%d", windowState, Boolean.valueOf(windowState.mRemoved), Boolean.valueOf(windowState.isVisible()), Boolean.valueOf(windowState.mHasSurface), Integer.valueOf(windowState.mWinAnimator.mDrawState)));
                } else if (ProtoLogCache.WM_DEBUG_SCREEN_ON_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_SCREEN_ON, 892244061, 508, (String) null, new Object[]{String.valueOf(windowState), Boolean.valueOf(windowState.mRemoved), Boolean.valueOf(windowState.isVisible()), Boolean.valueOf(windowState.mHasSurface), Long.valueOf(windowState.mWinAnimator.mDrawState)});
                }
                if (this.mWindowManagerServiceExt.dontWaitDrawForCompactWindow(windowState) || this.mWindowManagerServiceExt.dontWaitDrawForFlexibleWindow(windowState)) {
                    keyAt.mWaitingForDrawn.remove(windowState);
                } else if (!this.mWindowManagerServiceExt.shouldSkipCheckWindowDrawn(windowState)) {
                    if (windowState.mRemoved || !windowState.mHasSurface || !windowState.isVisibleByPolicy()) {
                        if (ProtoLogCache.WM_DEBUG_SCREEN_ON_enabled) {
                            ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_SCREEN_ON, 463993897, 0, (String) null, new Object[]{String.valueOf(windowState)});
                        }
                        keyAt.mWaitingForDrawn.remove(windowState);
                        if (Trace.isTagEnabled(32L)) {
                            traceEndWaitingForWindowDrawn(windowState);
                        }
                    } else if (windowState.hasDrawn()) {
                        if (ProtoLogCache.WM_DEBUG_SCREEN_ON_enabled) {
                            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_SCREEN_ON, 1401700824, 0, (String) null, new Object[]{String.valueOf(windowState)});
                        }
                        keyAt.mWaitingForDrawn.remove(windowState);
                        if (Trace.isTagEnabled(32L)) {
                            traceEndWaitingForWindowDrawn(windowState);
                        }
                    }
                }
            }
            if (keyAt.mWaitingForDrawn.isEmpty() && z) {
                if (!ProtoLogGroup.WM_DEBUG_SCREEN_ON.isLogToLogcat()) {
                    Slog.d(TAG, "All windows drawn!");
                } else if (ProtoLogCache.WM_DEBUG_SCREEN_ON_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_SCREEN_ON, 665256544, 0, (String) null, (Object[]) null);
                }
                this.mH.removeMessages(24, keyAt);
                this.mWaitingForDrawnCallbacks.removeAt(size).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void traceStartWaitingForWindowDrawn(WindowState windowState) {
        String str = "waitForAllWindowsDrawn#" + ((Object) windowState.getWindowTag());
        Trace.asyncTraceBegin(32L, str.substring(0, Math.min(127, str.length())), 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void traceEndWaitingForWindowDrawn(WindowState windowState) {
        String str = "waitForAllWindowsDrawn#" + ((Object) windowState.getWindowTag());
        Trace.asyncTraceEnd(32L, str.substring(0, Math.min(127, str.length())), 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void requestTraversal() {
        this.mWindowPlacerLocked.requestTraversal();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleAnimationLocked() {
        this.mAnimator.scheduleAnimation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateFocusedWindowLocked(int i, boolean z) {
        Trace.traceBegin(32L, "wmUpdateFocus");
        boolean updateFocusedWindowLocked = this.mRoot.updateFocusedWindowLocked(i, z);
        Trace.traceEnd(32L);
        return updateFocusedWindowLocked;
    }

    void startFreezingDisplay(int i, int i2) {
        startFreezingDisplay(i, i2, getDefaultDisplayContentLocked());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startFreezingDisplay(int i, int i2, DisplayContent displayContent) {
        startFreezingDisplay(i, i2, displayContent, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x008d, code lost:
    
        if (r7.getDisplayRotation() != null) goto L39;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void startFreezingDisplay(int i, int i2, DisplayContent displayContent, int i3) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (!this.mDisplayFrozen && displayContent != null && displayContent.getDisplayRotation() != null && (!displayContent.getDisplayRotation().isRotatingSeamlessly() || this.mClientFreezingScreen)) {
                    if (displayContent.isReady() && displayContent.getDisplayPolicy().isScreenOnFully() && displayContent.getDisplayInfo().state != 1 && displayContent.okToAnimate()) {
                        if (displayContent.getDisplayId() == 2020) {
                            resetPriorityAfterLockedSection();
                            return;
                        }
                        Slog.i(TAG, "startFreezingDisplay display " + displayContent.getDisplayId());
                        this.mWindowManagerServiceExt.notifySysWindowRotation(WindowManagerService.class, null);
                        Trace.traceBegin(32L, "WMS.doStartFreezingDisplay");
                        doStartFreezingDisplay(i, i2, displayContent, i3);
                        Trace.traceEnd(32L);
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                Slog.w(TAG, "startFreezingDisplay displayContent " + displayContent);
                resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void doStartFreezingDisplay(int i, int i2, DisplayContent displayContent, int i3) {
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ORIENTATION, 9803449, 5, (String) null, new Object[]{Long.valueOf(i), Long.valueOf(i2), String.valueOf(Debug.getCallers(8))});
        }
        this.mScreenFrozenLock.acquire();
        this.mAtmService.startLaunchPowerMode(2);
        this.mDisplayFrozen = true;
        this.mDisplayFreezeTime = SystemClock.elapsedRealtime();
        this.mLastFinishedFreezeSource = null;
        this.mFrozenDisplayId = displayContent.getDisplayId();
        this.mInputManagerCallback.freezeInputDispatchingLw();
        if (displayContent.mAppTransition.isTransitionSet()) {
            displayContent.mAppTransition.freeze();
        }
        this.mLatencyTracker.onActionStart(6);
        this.mWindowManagerServiceSocExt.hookStartFreezingDisplay();
        this.mExitAnimId = i;
        this.mEnterAnimId = i2;
        this.mWindowManagerServiceExt.pokeDynamicVsyncAnimation(3500, "FreezingDisplay");
        displayContent.updateDisplayInfo();
        if (i3 == -1) {
            i3 = displayContent.getDisplayInfo().rotation;
        }
        displayContent.setRotationAnimation(new ScreenRotationAnimation(displayContent, i3));
        this.mWindowManagerServiceExt.checkScreenFreezingTimeOut(true);
        cancelRecentsAnimation(0, "freeze");
        if (WindowManagerDebugConfig.DEBUG_ANIM) {
            Slog.d(TAG, "cancelRecentsAnimation by FREEZE");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopFreezingDisplayLocked() {
        int i;
        boolean z;
        boolean z2;
        if (this.mDisplayFrozen) {
            DisplayContent displayContent = this.mRoot.getDisplayContent(this.mFrozenDisplayId);
            if (displayContent != null) {
                i = displayContent.mOpeningApps.size();
                z = displayContent.mWaitingForConfig;
                z2 = displayContent.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange();
            } else {
                i = 0;
                z = false;
                z2 = false;
            }
            if ((z || z2 || this.mAppsFreezingScreen > 0 || this.mWindowsFreezingScreen == 1 || this.mClientFreezingScreen || i > 0 || this.mWindowManagerServiceExt.shouldwaitingForFolded()) && !this.mWindowManagerServiceExt.shouldForceStopFreezingScreen()) {
                if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                    ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1360176455, 1887, (String) null, new Object[]{Boolean.valueOf(z), Boolean.valueOf(z2), Long.valueOf(this.mAppsFreezingScreen), Long.valueOf(this.mWindowsFreezingScreen), Boolean.valueOf(this.mClientFreezingScreen), Long.valueOf(i)});
                    return;
                }
                return;
            }
            Trace.traceBegin(32L, "WMS.doStopFreezingDisplayLocked-" + this.mLastFinishedFreezeSource);
            doStopFreezingDisplayLocked(displayContent);
            Trace.traceEnd(32L);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x010f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void doStopFreezingDisplayLocked(DisplayContent displayContent) {
        boolean z;
        boolean z2;
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ORIENTATION, 355720268, 0, (String) null, (Object[]) null);
        }
        this.mWindowManagerServiceExt.speedWallpaperShowIfNeeded(displayContent);
        this.mFrozenDisplayId = -1;
        this.mDisplayFrozen = false;
        this.mInputManagerCallback.thawInputDispatchingLw();
        this.mLastDisplayFreezeDuration = (int) (SystemClock.elapsedRealtime() - this.mDisplayFreezeTime);
        StringBuilder sb = new StringBuilder(128);
        sb.append("Screen frozen for ");
        TimeUtils.formatDuration(this.mLastDisplayFreezeDuration, sb);
        if (this.mLastFinishedFreezeSource != null) {
            sb.append(" due to ");
            sb.append(this.mLastFinishedFreezeSource);
        }
        if (ProtoLogCache.WM_ERROR_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_ERROR, -583031528, 0, "%s", new Object[]{String.valueOf(sb.toString())});
        }
        this.mH.removeMessages(17);
        this.mH.removeMessages(30);
        this.mWindowManagerServiceExt.checkScreenFreezingTimeOut(false);
        ScreenRotationAnimation rotationAnimation = displayContent == null ? null : displayContent.getRotationAnimation();
        if (rotationAnimation != null && rotationAnimation.hasScreenshot()) {
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1634557978, 0, (String) null, (Object[]) null);
            }
            DisplayInfo displayInfo = displayContent.getDisplayInfo();
            if (!displayContent.getDisplayRotation().validateRotationAnimation(this.mExitAnimId, this.mEnterAnimId, false)) {
                this.mEnterAnimId = 0;
                this.mExitAnimId = 0;
            }
            if (rotationAnimation.dismiss(this.mTransaction, 10000L, getTransitionAnimationScaleLocked(), displayInfo.logicalWidth, displayInfo.logicalHeight, this.mExitAnimId, this.mEnterAnimId)) {
                this.mTransaction.apply();
                z = false;
                this.mWindowManagerServiceExt.onStopFreezingDisplayLocked();
                z2 = displayContent == null && displayContent.updateOrientation();
                this.mWindowManagerServiceExt.handleUiModeChanged();
                this.mScreenFrozenLock.release();
                if (z && displayContent != null) {
                    if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                        ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_ORIENTATION, -783405930, 0, (String) null, (Object[]) null);
                    }
                    z2 |= displayContent.updateRotationUnchecked();
                }
                if (z2) {
                    displayContent.sendNewConfiguration();
                }
                this.mAtmService.endLaunchPowerMode(2);
                this.mLatencyTracker.onActionEnd(6);
                this.mWindowManagerServiceSocExt.hookStopFreezingDisplayLocked();
                this.mWindowManagerServiceExt.endHookstopFreezingDisplayLocked(sb.toString());
            }
            rotationAnimation.kill();
            displayContent.setRotationAnimation(null);
            this.mWindowManagerServiceExt.setFrozenByUserSwitching(false);
        } else {
            if (rotationAnimation != null) {
                rotationAnimation.kill();
                displayContent.setRotationAnimation(null);
            }
            this.mWindowManagerServiceExt.setFrozenByUserSwitching(false);
        }
        z = true;
        this.mWindowManagerServiceExt.onStopFreezingDisplayLocked();
        if (displayContent == null) {
        }
        this.mWindowManagerServiceExt.handleUiModeChanged();
        this.mScreenFrozenLock.release();
        if (z) {
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            }
            z2 |= displayContent.updateRotationUnchecked();
        }
        if (z2) {
        }
        this.mAtmService.endLaunchPowerMode(2);
        this.mLatencyTracker.onActionEnd(6);
        this.mWindowManagerServiceSocExt.hookStopFreezingDisplayLocked();
        this.mWindowManagerServiceExt.endHookstopFreezingDisplayLocked(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getPropertyInt(String[] strArr, int i, int i2, int i3, DisplayMetrics displayMetrics) {
        String str;
        if (i < strArr.length && (str = strArr[i]) != null && str.length() > 0) {
            try {
                return Integer.parseInt(str);
            } catch (Exception unused) {
            }
        }
        return i2 == 0 ? i3 : (int) TypedValue.applyDimension(i2, i3, displayMetrics);
    }

    public void showCustomizeWatermark(boolean z) {
        this.mWindowManagerServiceExt.showCustomizeWatermark(z, this.mContext, getDefaultDisplayContentLocked(), this.mTransaction);
    }

    void createWatermark() {
        FileInputStream fileInputStream;
        DataInputStream dataInputStream;
        String[] split;
        if (this.mWatermark != null) {
            return;
        }
        DataInputStream dataInputStream2 = null;
        try {
            try {
                fileInputStream = new FileInputStream(new File("/system/etc/setup.conf"));
                try {
                    dataInputStream = new DataInputStream(fileInputStream);
                } catch (FileNotFoundException unused) {
                } catch (IOException unused2) {
                } catch (Throwable th) {
                    th = th;
                }
            } catch (FileNotFoundException unused3) {
                fileInputStream = null;
            } catch (IOException unused4) {
                fileInputStream = null;
            } catch (Throwable th2) {
                th = th2;
                fileInputStream = null;
            }
            try {
                String readLine = dataInputStream.readLine();
                if (readLine != null && (split = readLine.split("%")) != null && split.length > 0) {
                    DisplayContent defaultDisplayContentLocked = getDefaultDisplayContentLocked();
                    this.mWatermark = new Watermark(defaultDisplayContentLocked, defaultDisplayContentLocked.mRealDisplayMetrics, split, this.mTransaction);
                    this.mTransaction.apply();
                }
                dataInputStream.close();
            } catch (FileNotFoundException unused5) {
                dataInputStream2 = dataInputStream;
                if (dataInputStream2 == null) {
                    if (fileInputStream == null) {
                        return;
                    }
                    fileInputStream.close();
                }
                dataInputStream2.close();
            } catch (IOException unused6) {
                dataInputStream2 = dataInputStream;
                if (dataInputStream2 == null) {
                    if (fileInputStream == null) {
                        return;
                    }
                    fileInputStream.close();
                }
                dataInputStream2.close();
            } catch (Throwable th3) {
                th = th3;
                dataInputStream2 = dataInputStream;
                if (dataInputStream2 != null) {
                    dataInputStream2.close();
                } else {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    throw th;
                }
                throw th;
            }
        } catch (IOException unused7) {
        }
    }

    public void setRecentsVisibility(boolean z) {
        if (!checkCallingPermission("android.permission.STATUS_BAR", "setRecentsVisibility()")) {
            throw new SecurityException("Requires STATUS_BAR permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mPolicy.setRecentsVisibilityLw(z);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void hideTransientBars(int i) {
        if (!checkCallingPermission("android.permission.STATUS_BAR", "hideTransientBars()")) {
            throw new SecurityException("Requires STATUS_BAR permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent != null) {
                    displayContent.getInsetsPolicy().hideTransient();
                } else {
                    Slog.w(TAG, "hideTransientBars with invalid displayId=" + i);
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void updateStaticPrivacyIndicatorBounds(int i, Rect[] rectArr) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent != null) {
                    displayContent.updatePrivacyIndicatorBounds(rectArr);
                } else {
                    Slog.w(TAG, "updateStaticPrivacyIndicatorBounds with invalid displayId=" + i);
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void setNavBarVirtualKeyHapticFeedbackEnabled(boolean z) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.STATUS_BAR") != 0) {
            throw new SecurityException("Caller does not hold permission android.permission.STATUS_BAR");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mPolicy.setNavBarVirtualKeyHapticFeedbackEnabledLw(z);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void createInputConsumer(IBinder iBinder, String str, int i, InputChannel inputChannel) {
        if (!this.mAtmService.isCallerRecents(Binder.getCallingUid()) && this.mContext.checkCallingOrSelfPermission("android.permission.INPUT_CONSUMER") != 0) {
            throw new SecurityException("createInputConsumer requires INPUT_CONSUMER permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent != null) {
                    displayContent.getInputMonitor().createInputConsumer(iBinder, str, inputChannel, Binder.getCallingPid(), Binder.getCallingUserHandle());
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public boolean destroyInputConsumer(String str, int i) {
        if (!this.mAtmService.isCallerRecents(Binder.getCallingUid()) && this.mContext.checkCallingOrSelfPermission("android.permission.INPUT_CONSUMER") != 0) {
            throw new SecurityException("destroyInputConsumer requires INPUT_CONSUMER permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean destroyInputConsumer = displayContent.getInputMonitor().destroyInputConsumer(str);
                resetPriorityAfterLockedSection();
                return destroyInputConsumer;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public Region getCurrentImeTouchRegion() {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.RESTRICTED_VR_ACCESS") != 0) {
            throw new SecurityException("getCurrentImeTouchRegion is restricted to VR services");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                Region region = new Region();
                for (int size = this.mRoot.mChildren.size() - 1; size >= 0; size--) {
                    WindowState windowState = ((DisplayContent) this.mRoot.mChildren.get(size)).mInputMethodWindow;
                    if (windowState != null) {
                        windowState.getTouchableRegion(region);
                        resetPriorityAfterLockedSection();
                        return region;
                    }
                }
                resetPriorityAfterLockedSection();
                return region;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public boolean hasNavigationBar(int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean hasNavigationBar = displayContent.getDisplayPolicy().hasNavigationBar();
                resetPriorityAfterLockedSection();
                return hasNavigationBar;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void lockNow(Bundle bundle) {
        Slog.d(TAG, "lockNow with options called by uid: " + Binder.getCallingUid() + " pid: " + Binder.getCallingPid() + ",call" + Debug.getCallers(3));
        this.mPolicy.lockNow(bundle);
    }

    public void showRecentApps() {
        this.mPolicy.showRecentApps();
    }

    public boolean isSafeModeEnabled() {
        return this.mSafeMode;
    }

    public boolean clearWindowContentFrameStats(IBinder iBinder) {
        if (!checkCallingPermission("android.permission.FRAME_STATS", "clearWindowContentFrameStats()")) {
            throw new SecurityException("Requires FRAME_STATS permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowState = this.mWindowMap.get(iBinder);
                if (windowState == null) {
                    resetPriorityAfterLockedSection();
                    return false;
                }
                WindowSurfaceController windowSurfaceController = windowState.mWinAnimator.mSurfaceController;
                if (windowSurfaceController == null) {
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean clearWindowContentFrameStats = windowSurfaceController.clearWindowContentFrameStats();
                resetPriorityAfterLockedSection();
                return clearWindowContentFrameStats;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public WindowContentFrameStats getWindowContentFrameStats(IBinder iBinder) {
        if (!checkCallingPermission("android.permission.FRAME_STATS", "getWindowContentFrameStats()")) {
            throw new SecurityException("Requires FRAME_STATS permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowState = this.mWindowMap.get(iBinder);
                if (windowState == null) {
                    resetPriorityAfterLockedSection();
                    return null;
                }
                WindowSurfaceController windowSurfaceController = windowState.mWinAnimator.mSurfaceController;
                if (windowSurfaceController == null) {
                    resetPriorityAfterLockedSection();
                    return null;
                }
                if (this.mTempWindowRenderStats == null) {
                    this.mTempWindowRenderStats = new WindowContentFrameStats();
                }
                WindowContentFrameStats windowContentFrameStats = this.mTempWindowRenderStats;
                if (windowSurfaceController.getWindowContentFrameStats(windowContentFrameStats)) {
                    resetPriorityAfterLockedSection();
                    return windowContentFrameStats;
                }
                resetPriorityAfterLockedSection();
                return null;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void dumpPolicyLocked(PrintWriter printWriter, String[] strArr) {
        printWriter.println("WINDOW MANAGER POLICY STATE (dumpsys window policy)");
        this.mPolicy.dump("    ", printWriter, strArr);
    }

    private void dumpAnimatorLocked(PrintWriter printWriter, boolean z) {
        printWriter.println("WINDOW MANAGER ANIMATOR STATE (dumpsys window animator)");
        this.mAnimator.dumpLocked(printWriter, "    ", z);
    }

    private void dumpTokensLocked(PrintWriter printWriter, boolean z) {
        printWriter.println("WINDOW MANAGER TOKENS (dumpsys window tokens)");
        this.mRoot.dumpTokens(printWriter, z);
    }

    private void dumpHighRefreshRateBlacklist(PrintWriter printWriter) {
        printWriter.println("WINDOW MANAGER HIGH REFRESH RATE BLACKLIST (dumpsys window refresh)");
        this.mHighRefreshRateDenylist.dump(printWriter);
    }

    private void dumpTraceStatus(PrintWriter printWriter) {
        printWriter.println("WINDOW MANAGER TRACE (dumpsys window trace)");
        printWriter.print(this.mWindowTracing.getStatus() + "\n");
    }

    private void dumpLogStatus(PrintWriter printWriter) {
        printWriter.println("WINDOW MANAGER LOGGING (dumpsys window logging)");
        printWriter.println(ProtoLogImpl.getSingleInstance().getStatus());
    }

    private void dumpSessionsLocked(PrintWriter printWriter) {
        printWriter.println("WINDOW MANAGER SESSIONS (dumpsys window sessions)");
        for (int i = 0; i < this.mSessions.size(); i++) {
            Session valueAt = this.mSessions.valueAt(i);
            printWriter.print("  Session ");
            printWriter.print(valueAt);
            printWriter.println(':');
            valueAt.dump(printWriter, "    ");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebugLocked(ProtoOutputStream protoOutputStream, int i) {
        this.mPolicy.dumpDebug(protoOutputStream, 1146756268033L);
        this.mRoot.dumpDebug(protoOutputStream, 1146756268034L, i);
        DisplayContent topFocusedDisplayContent = this.mRoot.getTopFocusedDisplayContent();
        WindowState windowState = topFocusedDisplayContent.mCurrentFocus;
        if (windowState != null) {
            windowState.writeIdentifierToProto(protoOutputStream, 1146756268035L);
        }
        ActivityRecord activityRecord = topFocusedDisplayContent.mFocusedApp;
        if (activityRecord != null) {
            activityRecord.writeNameToProto(protoOutputStream, 1138166333444L);
        }
        WindowState currentInputMethodWindow = this.mRoot.getCurrentInputMethodWindow();
        if (currentInputMethodWindow != null) {
            currentInputMethodWindow.writeIdentifierToProto(protoOutputStream, 1146756268037L);
        }
        protoOutputStream.write(1133871366150L, this.mDisplayFrozen);
        protoOutputStream.write(1120986464265L, topFocusedDisplayContent.getDisplayId());
        protoOutputStream.write(1133871366154L, this.mHardKeyboardAvailable);
        protoOutputStream.write(1133871366155L, true);
        this.mAtmService.mBackNavigationController.dumpDebug(protoOutputStream, 1146756268044L);
    }

    private void dumpWindowsLocked(PrintWriter printWriter, boolean z, ArrayList<WindowState> arrayList) {
        printWriter.println("WINDOW MANAGER WINDOWS (dumpsys window windows)");
        dumpWindowsNoHeaderLocked(printWriter, z, arrayList);
    }

    private void dumpWindowsNoHeaderLocked(final PrintWriter printWriter, boolean z, ArrayList<WindowState> arrayList) {
        this.mRoot.dumpWindowsNoHeader(printWriter, z, arrayList);
        if (!this.mHidingNonSystemOverlayWindows.isEmpty()) {
            printWriter.println();
            printWriter.println("  Hiding System Alert Windows:");
            for (int size = this.mHidingNonSystemOverlayWindows.size() - 1; size >= 0; size--) {
                WindowState windowState = this.mHidingNonSystemOverlayWindows.get(size);
                printWriter.print("  #");
                printWriter.print(size);
                printWriter.print(' ');
                printWriter.print(windowState);
                if (z) {
                    printWriter.println(":");
                    windowState.dump(printWriter, "    ", true);
                } else {
                    printWriter.println();
                }
            }
        }
        ArrayList<WindowState> arrayList2 = this.mForceRemoves;
        if (arrayList2 != null && !arrayList2.isEmpty()) {
            printWriter.println();
            printWriter.println("  Windows force removing:");
            for (int size2 = this.mForceRemoves.size() - 1; size2 >= 0; size2--) {
                WindowState windowState2 = this.mForceRemoves.get(size2);
                printWriter.print("  Removing #");
                printWriter.print(size2);
                printWriter.print(' ');
                printWriter.print(windowState2);
                if (z) {
                    printWriter.println(":");
                    windowState2.dump(printWriter, "    ", true);
                } else {
                    printWriter.println();
                }
            }
        }
        if (!this.mDestroySurface.isEmpty()) {
            printWriter.println();
            printWriter.println("  Windows waiting to destroy their surface:");
            for (int size3 = this.mDestroySurface.size() - 1; size3 >= 0; size3--) {
                WindowState windowState3 = this.mDestroySurface.get(size3);
                if (arrayList == null || arrayList.contains(windowState3)) {
                    printWriter.print("  Destroy #");
                    printWriter.print(size3);
                    printWriter.print(' ');
                    printWriter.print(windowState3);
                    if (z) {
                        printWriter.println(":");
                        windowState3.dump(printWriter, "    ", true);
                    } else {
                        printWriter.println();
                    }
                }
            }
        }
        if (!this.mResizingWindows.isEmpty()) {
            printWriter.println();
            printWriter.println("  Windows waiting to resize:");
            for (int size4 = this.mResizingWindows.size() - 1; size4 >= 0; size4--) {
                WindowState windowState4 = this.mResizingWindows.get(size4);
                if (arrayList == null || arrayList.contains(windowState4)) {
                    printWriter.print("  Resizing #");
                    printWriter.print(size4);
                    printWriter.print(' ');
                    printWriter.print(windowState4);
                    if (z) {
                        printWriter.println(":");
                        windowState4.dump(printWriter, "    ", true);
                    } else {
                        printWriter.println();
                    }
                }
            }
        }
        if (!this.mWaitingForDrawnCallbacks.isEmpty()) {
            printWriter.println();
            printWriter.println("  Clients waiting for these windows to be drawn:");
            this.mWaitingForDrawnCallbacks.forEach(new BiConsumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda23
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    WindowManagerService.lambda$dumpWindowsNoHeaderLocked$11(printWriter, (WindowContainer) obj, (Message) obj2);
                }
            });
        }
        printWriter.println();
        printWriter.print("  mGlobalConfiguration=");
        printWriter.println(this.mRoot.getConfiguration());
        printWriter.print("  mHasPermanentDpad=");
        printWriter.println(this.mHasPermanentDpad);
        this.mRoot.dumpTopFocusedDisplayId(printWriter);
        this.mRoot.forAllDisplays(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda24
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WindowManagerService.lambda$dumpWindowsNoHeaderLocked$12(printWriter, (DisplayContent) obj);
            }
        });
        printWriter.print("  mBlurEnabled=");
        printWriter.println(this.mBlurController.getBlurEnabled());
        printWriter.print("  mLastDisplayFreezeDuration=");
        TimeUtils.formatDuration(this.mLastDisplayFreezeDuration, printWriter);
        if (this.mLastFinishedFreezeSource != null) {
            printWriter.print(" due to ");
            printWriter.print(this.mLastFinishedFreezeSource);
        }
        printWriter.println();
        this.mInputManagerCallback.dump(printWriter, "  ");
        this.mSnapshotController.dump(printWriter, " ");
        dumpAccessibilityController(printWriter, false);
        if (z) {
            Object currentInputMethodWindow = this.mRoot.getCurrentInputMethodWindow();
            if (currentInputMethodWindow != null) {
                printWriter.print("  mInputMethodWindow=");
                printWriter.println(currentInputMethodWindow);
            }
            this.mWindowPlacerLocked.dump(printWriter, "  ");
            printWriter.print("  mSystemBooted=");
            printWriter.print(this.mSystemBooted);
            printWriter.print(" mDisplayEnabled=");
            printWriter.println(this.mDisplayEnabled);
            this.mRoot.dumpLayoutNeededDisplayIds(printWriter);
            printWriter.print("  mTransactionSequence=");
            printWriter.println(this.mTransactionSequence);
            printWriter.print("  mDisplayFrozen=");
            printWriter.print(this.mDisplayFrozen);
            printWriter.print(" windows=");
            printWriter.print(this.mWindowsFreezingScreen);
            printWriter.print(" client=");
            printWriter.print(this.mClientFreezingScreen);
            printWriter.print(" apps=");
            printWriter.println(this.mAppsFreezingScreen);
            DisplayContent defaultDisplayContentLocked = getDefaultDisplayContentLocked();
            printWriter.print("  mRotation=");
            printWriter.println(defaultDisplayContentLocked.getRotation());
            printWriter.print("  mLastOrientation=");
            printWriter.println(defaultDisplayContentLocked.getLastOrientation());
            printWriter.print("  mWaitingForConfig=");
            printWriter.println(defaultDisplayContentLocked.mWaitingForConfig);
            this.mRotationWatcherController.dump(printWriter);
            printWriter.print("  Animation settings: disabled=");
            printWriter.print(this.mAnimationsDisabled);
            printWriter.print(" window=");
            printWriter.print(this.mWindowAnimationScaleSetting);
            printWriter.print(" transition=");
            printWriter.print(this.mTransitionAnimationScaleSetting);
            printWriter.print(" animator=");
            printWriter.println(this.mAnimatorDurationScaleSetting);
            if (this.mRecentsAnimationController != null) {
                printWriter.print("  mRecentsAnimationController=");
                printWriter.println(this.mRecentsAnimationController);
                this.mRecentsAnimationController.dump(printWriter, "    ");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dumpWindowsNoHeaderLocked$11(PrintWriter printWriter, WindowContainer windowContainer, Message message) {
        printWriter.print("  WindowContainer ");
        printWriter.println(windowContainer.getName());
        for (int size = windowContainer.mWaitingForDrawn.size() - 1; size >= 0; size--) {
            WindowState windowState = windowContainer.mWaitingForDrawn.get(size);
            printWriter.print("  Waiting #");
            printWriter.print(size);
            printWriter.print(' ');
            printWriter.print(windowState);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dumpWindowsNoHeaderLocked$12(PrintWriter printWriter, DisplayContent displayContent) {
        int displayId = displayContent.getDisplayId();
        InsetsControlTarget imeTarget = displayContent.getImeTarget(0);
        InputTarget imeInputTarget = displayContent.getImeInputTarget();
        InsetsControlTarget imeTarget2 = displayContent.getImeTarget(2);
        if (imeTarget != null) {
            printWriter.print("  imeLayeringTarget in display# ");
            printWriter.print(displayId);
            printWriter.print(' ');
            printWriter.println(imeTarget);
        }
        if (imeInputTarget != null) {
            printWriter.print("  imeInputTarget in display# ");
            printWriter.print(displayId);
            printWriter.print(' ');
            printWriter.println(imeInputTarget);
        }
        if (imeTarget2 != null) {
            printWriter.print("  imeControlTarget in display# ");
            printWriter.print(displayId);
            printWriter.print(' ');
            printWriter.println(imeTarget2);
        }
        printWriter.print("  Minimum task size of display#");
        printWriter.print(displayId);
        printWriter.print(' ');
        printWriter.print(displayContent.mMinSizeOfResizeableTaskDp);
        printWriter.print("  displayConfiguration=");
        printWriter.println(displayContent.getConfiguration());
    }

    private void dumpAccessibilityController(PrintWriter printWriter, boolean z) {
        boolean hasCallbacks = this.mAccessibilityController.hasCallbacks();
        if (hasCallbacks || z) {
            if (!hasCallbacks) {
                printWriter.println("AccessibilityController doesn't have callbacks, but printing it anways:");
            } else {
                printWriter.println("AccessibilityController:");
            }
            this.mAccessibilityController.dump(printWriter, "  ");
        }
    }

    private void dumpAccessibilityLocked(PrintWriter printWriter) {
        dumpAccessibilityController(printWriter, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean dumpWindows(PrintWriter printWriter, String str, boolean z) {
        final ArrayList<WindowState> arrayList = new ArrayList<>();
        if ("apps".equals(str) || ActivityTaskManagerService.DUMP_VISIBLE_ACTIVITIES.equals(str) || "visible-apps".equals(str)) {
            final boolean contains = str.contains("apps");
            final boolean contains2 = str.contains(ActivityTaskManagerService.DUMP_VISIBLE_ACTIVITIES);
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                if (contains) {
                    try {
                        this.mRoot.dumpDisplayContents(printWriter);
                    } finally {
                        resetPriorityAfterLockedSection();
                    }
                }
                this.mRoot.forAllWindows(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda16
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        WindowManagerService.lambda$dumpWindows$13(contains2, contains, arrayList, (WindowState) obj);
                    }
                }, true);
            }
            resetPriorityAfterLockedSection();
        } else {
            WindowManagerGlobalLock windowManagerGlobalLock2 = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock2) {
                try {
                    this.mRoot.getWindowsByName(arrayList, str);
                } finally {
                }
            }
            resetPriorityAfterLockedSection();
        }
        if (arrayList.isEmpty()) {
            return false;
        }
        WindowManagerGlobalLock windowManagerGlobalLock3 = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock3) {
            try {
                dumpWindowsLocked(printWriter, z, arrayList);
            } finally {
            }
        }
        resetPriorityAfterLockedSection();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dumpWindows$13(boolean z, boolean z2, ArrayList arrayList, WindowState windowState) {
        if (!z || windowState.isVisible()) {
            if (z2 && windowState.mActivityRecord == null) {
                return;
            }
            arrayList.add(windowState);
        }
    }

    private void dumpLastANRLocked(PrintWriter printWriter) {
        printWriter.println("WINDOW MANAGER LAST ANR (dumpsys window lastanr)");
        String str = this.mLastANRState;
        if (str == null) {
            printWriter.println("  <no ANR has occurred since boot>");
        } else {
            printWriter.println(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void saveANRStateLocked(ActivityRecord activityRecord, WindowState windowState, String str) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter fastPrintWriter = new FastPrintWriter(stringWriter, false, 1024);
        fastPrintWriter.println("  ANR time: " + DateFormat.getDateTimeInstance().format(new Date()));
        if (activityRecord != null) {
            fastPrintWriter.println("  Application at fault: " + activityRecord.stringName);
        }
        if (windowState != null) {
            fastPrintWriter.println("  Window at fault: " + ((Object) windowState.mAttrs.getTitle()));
        }
        if (str != null) {
            fastPrintWriter.println("  Reason: " + str);
        }
        for (int childCount = this.mRoot.getChildCount() - 1; childCount >= 0; childCount--) {
            DisplayContent childAt = this.mRoot.getChildAt(childCount);
            int displayId = childAt.getDisplayId();
            if (!childAt.mWinAddedSinceNullFocus.isEmpty()) {
                fastPrintWriter.println("  Windows added in display #" + displayId + " since null focus: " + childAt.mWinAddedSinceNullFocus);
            }
            if (!childAt.mWinRemovedSinceNullFocus.isEmpty()) {
                fastPrintWriter.println("  Windows removed in display #" + displayId + " since null focus: " + childAt.mWinRemovedSinceNullFocus);
            }
        }
        fastPrintWriter.println();
        dumpWindowsNoHeaderLocked(fastPrintWriter, true, null);
        fastPrintWriter.println();
        fastPrintWriter.println("Last ANR continued");
        this.mRoot.dumpDisplayContents(fastPrintWriter);
        fastPrintWriter.close();
        this.mLastANRState = stringWriter.toString();
        this.mH.removeMessages(38);
        this.mH.sendEmptyMessageDelayed(38, 7200000L);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        PriorityDump.dump(this.mPriorityDumper, fileDescriptor, printWriter, strArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00cb, code lost:
    
        r2 = new android.util.proto.ProtoOutputStream(r16);
        r3 = r15.mGlobalLock;
        boostPriorityForLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00d7, code lost:
    
        monitor-enter(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00d8, code lost:
    
        dumpDebugLocked(r2, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00db, code lost:
    
        monitor-exit(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00dc, code lost:
    
        resetPriorityAfterLockedSection();
        r2.flush();
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00e2, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00e3, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00e8, code lost:
    
        throw r0;
     */
    @NeverCompile
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void doDump(FileDescriptor fileDescriptor, final PrintWriter printWriter, String[] strArr, boolean z) {
        String str;
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            int i = 0;
            boolean z2 = false;
            while (i < strArr.length && (str = strArr[i]) != null && str.length() > 0 && str.charAt(0) == '-') {
                i++;
                if ("-a".equals(str)) {
                    z2 = true;
                } else {
                    if ("-h".equals(str)) {
                        printWriter.println("Window manager dump options:");
                        printWriter.println("  [-a] [-h] [cmd] ...");
                        printWriter.println("  cmd may be one of:");
                        printWriter.println("    l[astanr]: last ANR information");
                        printWriter.println("    p[policy]: policy state");
                        printWriter.println("    a[animator]: animator state");
                        printWriter.println("    s[essions]: active sessions");
                        printWriter.println("    surfaces: active surfaces (debugging enabled only)");
                        printWriter.println("    d[isplays]: active display contents");
                        printWriter.println("    t[okens]: token list");
                        printWriter.println("    w[indows]: window list");
                        printWriter.println("    a11y[accessibility]: accessibility-related state");
                        printWriter.println("    package-config: installed packages having app-specific config");
                        printWriter.println("    trace: print trace status and write Winscope trace to file");
                        printWriter.println("  cmd may also be a NAME to dump windows.  NAME may");
                        printWriter.println("    be a partial substring in a window name, a");
                        printWriter.println("    Window hex object identifier, or");
                        printWriter.println("    \"all\" for all windows, or");
                        printWriter.println("    \"visible\" for the visible windows.");
                        printWriter.println("    \"visible-apps\" for the visible app windows.");
                        printWriter.println("  -a: include all available server state.");
                        printWriter.println("  --proto: output dump in protocol buffer format.");
                        return;
                    }
                    printWriter.println("Unknown argument: " + str + "; use -h for help");
                }
            }
            if (i < strArr.length) {
                String str2 = strArr[i];
                int i2 = i + 1;
                if (this.mWindowManagerServiceExt.doDump(this, str2, fileDescriptor, printWriter, strArr, i2)) {
                    return;
                }
                if (ActivityTaskManagerService.DUMP_LASTANR_CMD.equals(str2) || "l".equals(str2)) {
                    WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            dumpLastANRLocked(printWriter);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("policy".equals(str2) || "p".equals(str2)) {
                    WindowManagerGlobalLock windowManagerGlobalLock2 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock2) {
                        try {
                            dumpPolicyLocked(printWriter, strArr);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("animator".equals(str2) || ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD.equals(str2)) {
                    WindowManagerGlobalLock windowManagerGlobalLock3 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock3) {
                        try {
                            dumpAnimatorLocked(printWriter, true);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("sessions".equals(str2) || "s".equals(str2)) {
                    WindowManagerGlobalLock windowManagerGlobalLock4 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock4) {
                        try {
                            dumpSessionsLocked(printWriter);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("displays".equals(str2) || "d".equals(str2)) {
                    WindowManagerGlobalLock windowManagerGlobalLock5 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock5) {
                        try {
                            this.mRoot.dumpDisplayContents(printWriter);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("tokens".equals(str2) || "t".equals(str2)) {
                    WindowManagerGlobalLock windowManagerGlobalLock6 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock6) {
                        try {
                            dumpTokensLocked(printWriter, true);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("windows".equals(str2) || "w".equals(str2)) {
                    WindowManagerGlobalLock windowManagerGlobalLock7 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock7) {
                        try {
                            dumpWindowsLocked(printWriter, true, null);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("accessibility".equals(str2) || "a11y".equals(str2)) {
                    WindowManagerGlobalLock windowManagerGlobalLock8 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock8) {
                        try {
                            dumpAccessibilityLocked(printWriter);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("all".equals(str2)) {
                    WindowManagerGlobalLock windowManagerGlobalLock9 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock9) {
                        try {
                            dumpWindowsLocked(printWriter, true, null);
                        } finally {
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (ActivityTaskManagerService.DUMP_CONTAINERS_CMD.equals(str2)) {
                    WindowManagerGlobalLock windowManagerGlobalLock10 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock10) {
                        try {
                            this.mRoot.dumpChildrenNames(printWriter, " ");
                            printWriter.println(" ");
                            this.mRoot.forAllWindows(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda10
                                @Override // java.util.function.Consumer
                                public final void accept(Object obj) {
                                    printWriter.println((WindowState) obj);
                                }
                            }, true);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("trace".equals(str2)) {
                    dumpTraceStatus(printWriter);
                    return;
                }
                if ("logging".equals(str2)) {
                    dumpLogStatus(printWriter);
                    return;
                }
                if ("refresh".equals(str2)) {
                    dumpHighRefreshRateBlacklist(printWriter);
                    return;
                }
                if ("constants".equals(str2)) {
                    this.mConstants.dump(printWriter);
                    return;
                }
                if ("splitscreen".equals(str2)) {
                    this.mWindowManagerServiceExt.dump(printWriter, strArr);
                    return;
                }
                if ("package-config".equals(str2)) {
                    this.mAtmService.dumpInstalledPackagesConfig(printWriter);
                    return;
                }
                if (this.mWindowManagerServiceExt.doDumpWindows(printWriter, str2, strArr, i2, z2)) {
                    return;
                }
                printWriter.println("Bad window command, or no windows match: " + str2);
                printWriter.println("Use -h for help.");
                return;
            }
            WindowManagerGlobalLock windowManagerGlobalLock11 = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock11) {
                try {
                    printWriter.println();
                    if (z2) {
                        printWriter.println("-------------------------------------------------------------------------------");
                    }
                    dumpLastANRLocked(printWriter);
                    printWriter.println();
                    if (z2) {
                        printWriter.println("-------------------------------------------------------------------------------");
                    }
                    dumpPolicyLocked(printWriter, strArr);
                    printWriter.println();
                    if (z2) {
                        printWriter.println("-------------------------------------------------------------------------------");
                    }
                    dumpAnimatorLocked(printWriter, z2);
                    printWriter.println();
                    if (z2) {
                        printWriter.println("-------------------------------------------------------------------------------");
                    }
                    dumpSessionsLocked(printWriter);
                    printWriter.println();
                    if (z2) {
                        printWriter.println("-------------------------------------------------------------------------------");
                    }
                    if (z2) {
                        printWriter.println("-------------------------------------------------------------------------------");
                    }
                    this.mRoot.dumpDisplayContents(printWriter);
                    printWriter.println();
                    if (z2) {
                        printWriter.println("-------------------------------------------------------------------------------");
                    }
                    dumpTokensLocked(printWriter, z2);
                    printWriter.println();
                    if (z2) {
                        printWriter.println("-------------------------------------------------------------------------------");
                    }
                    dumpWindowsLocked(printWriter, z2, null);
                    if (z2) {
                        printWriter.println("-------------------------------------------------------------------------------");
                    }
                    dumpTraceStatus(printWriter);
                    if (z2) {
                        printWriter.println("-------------------------------------------------------------------------------");
                    }
                    dumpLogStatus(printWriter);
                    if (z2) {
                        printWriter.println("-------------------------------------------------------------------------------");
                    }
                    dumpHighRefreshRateBlacklist(printWriter);
                    if (z2) {
                        printWriter.println("-------------------------------------------------------------------------------");
                    }
                    this.mAtmService.dumpInstalledPackagesConfig(printWriter);
                    if (z2) {
                        printWriter.println("-------------------------------------------------------------------------------");
                    }
                    this.mConstants.dump(printWriter);
                } finally {
                    resetPriorityAfterLockedSection();
                }
            }
            resetPriorityAfterLockedSection();
        }
    }

    public void monitor() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayContent getDefaultDisplayContentLocked() {
        return this.mRoot.getDisplayContent(0);
    }

    public void onOverlayChanged() {
        this.mH.post(new Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                WindowManagerService.this.lambda$onOverlayChanged$16();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onOverlayChanged$16() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mAtmService.deferWindowLayout();
                this.mRoot.forAllDisplays(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda20
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        WindowManagerService.lambda$onOverlayChanged$15((DisplayContent) obj);
                    }
                });
                this.mAtmService.continueWindowLayout();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onOverlayChanged$15(DisplayContent displayContent) {
        displayContent.getDisplayPolicy().onOverlayChanged();
    }

    public Object getWindowManagerLock() {
        return this.mGlobalLock;
    }

    void setForceDesktopModeOnExternalDisplays(boolean z) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mForceDesktopModeOnExternalDisplays = z;
                this.mRoot.updateDisplayImePolicyCache();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    @VisibleForTesting
    void setIsPc(boolean z) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mIsPc = z;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int dipToPixel(int i, DisplayMetrics displayMetrics) {
        return (int) TypedValue.applyDimension(1, i, displayMetrics);
    }

    public void registerPinnedTaskListener(int i, IPinnedTaskListener iPinnedTaskListener) {
        if (checkCallingPermission("android.permission.REGISTER_WINDOW_MANAGER_LISTENERS", "registerPinnedTaskListener()") && this.mAtmService.mSupportsPictureInPicture) {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mRoot.getDisplayContent(i).getPinnedTaskController().registerPinnedTaskListener(iPinnedTaskListener);
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        }
    }

    public void requestAppKeyboardShortcuts(IResultReceiver iResultReceiver, int i) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.REGISTER_WINDOW_MANAGER_LISTENERS", "requestAppKeyboardShortcuts");
        try {
            WindowState focusedWindow = getFocusedWindow();
            if (focusedWindow == null || focusedWindow.mClient == null) {
                return;
            }
            getFocusedWindow().mClient.requestAppKeyboardShortcuts(iResultReceiver, i);
        } catch (RemoteException unused) {
        }
    }

    public void getStableInsets(int i, Rect rect) throws RemoteException {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                getStableInsetsLocked(i, rect);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    void getStableInsetsLocked(int i, Rect rect) {
        rect.setEmpty();
        DisplayContent displayContent = this.mRoot.getDisplayContent(i);
        if (displayContent != null) {
            DisplayInfo displayInfo = displayContent.getDisplayInfo();
            rect.set(displayContent.getDisplayPolicy().getDecorInsetsInfo(displayInfo.rotation, displayInfo.logicalWidth, displayInfo.logicalHeight).mConfigInsets);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class MousePositionTracker implements WindowManagerPolicyConstants.PointerEventListener {
        private boolean mLatestEventWasMouse;
        private float mLatestMouseX;
        private float mLatestMouseY;
        private int mPointerDisplayId;

        private MousePositionTracker() {
            this.mPointerDisplayId = -1;
        }

        boolean updatePosition(int i, float f, float f2) {
            synchronized (this) {
                this.mLatestEventWasMouse = true;
                if (i != this.mPointerDisplayId) {
                    return false;
                }
                this.mLatestMouseX = f;
                this.mLatestMouseY = f2;
                return true;
            }
        }

        void setPointerDisplayId(int i) {
            synchronized (this) {
                this.mPointerDisplayId = i;
            }
        }

        public void onPointerEvent(MotionEvent motionEvent) {
            if (motionEvent.isFromSource(8194)) {
                updatePosition(motionEvent.getDisplayId(), motionEvent.getRawX(), motionEvent.getRawY());
            } else {
                synchronized (this) {
                    this.mLatestEventWasMouse = false;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updatePointerIcon(IWindow iWindow) {
        synchronized (this.mMousePositionTracker) {
            if (this.mMousePositionTracker.mLatestEventWasMouse) {
                float f = this.mMousePositionTracker.mLatestMouseX;
                float f2 = this.mMousePositionTracker.mLatestMouseY;
                int i = this.mMousePositionTracker.mPointerDisplayId;
                WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
                boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        if (this.mDragDropController.dragDropActiveLocked()) {
                            resetPriorityAfterLockedSection();
                            return;
                        }
                        WindowState windowForClientLocked = windowForClientLocked((Session) null, iWindow, false);
                        if (windowForClientLocked == null) {
                            if (ProtoLogCache.WM_ERROR_enabled) {
                                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1325649102, 0, "Bad requesting window %s", new Object[]{String.valueOf(iWindow)});
                            }
                            resetPriorityAfterLockedSection();
                            return;
                        }
                        DisplayContent displayContent = windowForClientLocked.getDisplayContent();
                        if (displayContent == null) {
                            resetPriorityAfterLockedSection();
                            return;
                        }
                        if (i != displayContent.getDisplayId()) {
                            resetPriorityAfterLockedSection();
                            return;
                        }
                        WindowState touchableWinAtPointLocked = displayContent.getTouchableWinAtPointLocked(f, f2);
                        if (touchableWinAtPointLocked != windowForClientLocked) {
                            resetPriorityAfterLockedSection();
                            return;
                        }
                        try {
                            touchableWinAtPointLocked.mClient.updatePointerIcon(touchableWinAtPointLocked.translateToWindowX(f), touchableWinAtPointLocked.translateToWindowY(f2));
                        } catch (RemoteException unused) {
                            if (ProtoLogCache.WM_ERROR_enabled) {
                                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -393505149, 0, "unable to update pointer icon", (Object[]) null);
                            }
                        }
                        resetPriorityAfterLockedSection();
                    } catch (Throwable th) {
                        resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restorePointerIconLocked(DisplayContent displayContent, float f, float f2) {
        if (this.mMousePositionTracker.updatePosition(displayContent.getDisplayId(), f, f2)) {
            WindowState touchableWinAtPointLocked = displayContent.getTouchableWinAtPointLocked(f, f2);
            if (touchableWinAtPointLocked != null) {
                try {
                    touchableWinAtPointLocked.mClient.updatePointerIcon(touchableWinAtPointLocked.translateToWindowX(f), touchableWinAtPointLocked.translateToWindowY(f2));
                    return;
                } catch (RemoteException unused) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1423418408, 0, "unable to restore pointer icon", (Object[]) null);
                        return;
                    }
                    return;
                }
            }
            ((InputManager) this.mContext.getSystemService(InputManager.class)).setPointerIconType(1000);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMousePointerDisplayId(int i) {
        this.mMousePositionTracker.setPointerDisplayId(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateTapExcludeRegion(IWindow iWindow, Region region) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowForClientLocked = windowForClientLocked((Session) null, iWindow, false);
                if (windowForClientLocked == null) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1325649102, 0, "Bad requesting window %s", new Object[]{String.valueOf(iWindow)});
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                windowForClientLocked.updateTapExcludeRegion(region);
                resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void requestScrollCapture(int i, IBinder iBinder, int i2, IScrollCaptureResponseListener iScrollCaptureResponseListener) {
        ScrollCaptureResponse.Builder builder;
        WindowManagerGlobalLock windowManagerGlobalLock;
        if (!checkCallingPermission("android.permission.READ_FRAME_BUFFER", "requestScrollCapture()")) {
            throw new SecurityException("Requires READ_FRAME_BUFFER permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                builder = new ScrollCaptureResponse.Builder();
                windowManagerGlobalLock = this.mGlobalLock;
                boostPriorityForLockedSection();
            } catch (RemoteException e) {
                if (ProtoLogCache.WM_ERROR_enabled) {
                    ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1046922686, 0, "requestScrollCapture: caught exception dispatching callback: %s", new Object[]{String.valueOf(e)});
                }
            }
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    if (displayContent == null) {
                        if (ProtoLogCache.WM_ERROR_enabled) {
                            ProtoLogImpl.e(ProtoLogGroup.WM_ERROR, 646981048, 1, "Invalid displayId for requestScrollCapture: %d", new Object[]{Long.valueOf(i)});
                        }
                        builder.setDescription(String.format("bad displayId: %d", Integer.valueOf(i)));
                        iScrollCaptureResponseListener.onScrollCaptureResponse(builder.build());
                    } else {
                        WindowState findScrollCaptureTargetWindow = displayContent.findScrollCaptureTargetWindow(iBinder != null ? windowForClientLocked((Session) null, iBinder, false) : null, i2);
                        if (findScrollCaptureTargetWindow == null) {
                            builder.setDescription("findScrollCaptureTargetWindow returned null");
                            iScrollCaptureResponseListener.onScrollCaptureResponse(builder.build());
                        } else {
                            try {
                                findScrollCaptureTargetWindow.mClient.requestScrollCapture(iScrollCaptureResponseListener);
                            } catch (RemoteException e2) {
                                if (ProtoLogCache.WM_ERROR_enabled) {
                                    ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1517908912, 0, "requestScrollCapture: caught exception dispatching to window.token=%s", new Object[]{String.valueOf(findScrollCaptureTargetWindow.mClient.asBinder())});
                                }
                                builder.setWindowTitle(findScrollCaptureTargetWindow.getName());
                                builder.setPackageName(findScrollCaptureTargetWindow.getOwningPackage());
                                builder.setDescription(String.format("caught exception: %s", e2));
                                iScrollCaptureResponseListener.onScrollCaptureResponse(builder.build());
                            }
                            resetPriorityAfterLockedSection();
                            return;
                        }
                    }
                    resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public int getWindowingMode(int i) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "getWindowingMode()")) {
            throw new SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 51628177, 1, "Attempted to get windowing mode of a display that does not exist: %d", new Object[]{Long.valueOf(i)});
                    }
                    resetPriorityAfterLockedSection();
                    return 0;
                }
                int windowingModeLocked = this.mDisplayWindowSettings.getWindowingModeLocked(displayContent);
                resetPriorityAfterLockedSection();
                return windowingModeLocked;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setWindowingMode(int i, int i2) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "setWindowingMode()")) {
            throw new SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContentOrCreate = getDisplayContentOrCreate(i, null);
                    if (displayContentOrCreate == null) {
                        if (ProtoLogCache.WM_ERROR_enabled) {
                            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -1838803135, 1, "Attempted to set windowing mode to a display that does not exist: %d", new Object[]{Long.valueOf(i)});
                        }
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    int windowingMode = displayContentOrCreate.getWindowingMode();
                    this.mDisplayWindowSettings.setWindowingModeLocked(displayContentOrCreate, i2);
                    displayContentOrCreate.reconfigureDisplayLocked();
                    if (windowingMode != displayContentOrCreate.getWindowingMode()) {
                        displayContentOrCreate.sendNewConfiguration();
                        displayContentOrCreate.executeAppTransition();
                    }
                    resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @WindowManager.RemoveContentMode
    public int getRemoveContentMode(int i) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "getRemoveContentMode()")) {
            throw new SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -496681057, 1, "Attempted to get remove mode of a display that does not exist: %d", new Object[]{Long.valueOf(i)});
                    }
                    resetPriorityAfterLockedSection();
                    return 0;
                }
                int removeContentModeLocked = this.mDisplayWindowSettings.getRemoveContentModeLocked(displayContent);
                resetPriorityAfterLockedSection();
                return removeContentModeLocked;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setRemoveContentMode(int i, @WindowManager.RemoveContentMode int i2) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "setRemoveContentMode()")) {
            throw new SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContentOrCreate = getDisplayContentOrCreate(i, null);
                    if (displayContentOrCreate == null) {
                        if (ProtoLogCache.WM_ERROR_enabled) {
                            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 288485303, 1, "Attempted to set remove mode to a display that does not exist: %d", new Object[]{Long.valueOf(i)});
                        }
                        resetPriorityAfterLockedSection();
                    } else {
                        this.mDisplayWindowSettings.setRemoveContentModeLocked(displayContentOrCreate, i2);
                        displayContentOrCreate.reconfigureDisplayLocked();
                        resetPriorityAfterLockedSection();
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean shouldShowWithInsecureKeyguard(int i) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "shouldShowWithInsecureKeyguard()")) {
            throw new SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1434383382, 1, "Attempted to get flag of a display that does not exist: %d", new Object[]{Long.valueOf(i)});
                    }
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean shouldShowWithInsecureKeyguardLocked = this.mDisplayWindowSettings.shouldShowWithInsecureKeyguardLocked(displayContent);
                resetPriorityAfterLockedSection();
                return shouldShowWithInsecureKeyguardLocked;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setShouldShowWithInsecureKeyguard(int i, boolean z) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "setShouldShowWithInsecureKeyguard()")) {
            throw new SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContentOrCreate = getDisplayContentOrCreate(i, null);
                    if (displayContentOrCreate == null) {
                        if (ProtoLogCache.WM_ERROR_enabled) {
                            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1521476038, 1, "Attempted to set flag to a display that does not exist: %d", new Object[]{Long.valueOf(i)});
                        }
                        resetPriorityAfterLockedSection();
                    } else {
                        this.mDisplayWindowSettings.setShouldShowWithInsecureKeyguardLocked(displayContentOrCreate, z);
                        displayContentOrCreate.reconfigureDisplayLocked();
                        resetPriorityAfterLockedSection();
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean shouldShowSystemDecors(int i) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "shouldShowSystemDecors()")) {
            throw new SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 11060725, 1, "Attempted to get system decors flag of a display that does not exist: %d", new Object[]{Long.valueOf(i)});
                    }
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean supportsSystemDecorations = displayContent.supportsSystemDecorations();
                resetPriorityAfterLockedSection();
                return supportsSystemDecorations;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setShouldShowSystemDecors(int i, boolean z) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "setShouldShowSystemDecors()")) {
            throw new SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContentOrCreate = getDisplayContentOrCreate(i, null);
                    if (displayContentOrCreate == null) {
                        if (ProtoLogCache.WM_ERROR_enabled) {
                            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -386552155, 1, "Attempted to set system decors flag to a display that does not exist: %d", new Object[]{Long.valueOf(i)});
                        }
                        resetPriorityAfterLockedSection();
                    } else {
                        if (!displayContentOrCreate.isTrusted()) {
                            throw new SecurityException("Attempted to set system decors flag to an untrusted virtual display: " + i);
                        }
                        this.mDisplayWindowSettings.setShouldShowSystemDecorsLocked(displayContentOrCreate, z);
                        displayContentOrCreate.reconfigureDisplayLocked();
                        resetPriorityAfterLockedSection();
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @WindowManager.DisplayImePolicy
    public int getDisplayImePolicy(int i) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "getDisplayImePolicy()")) {
            throw new SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        Map<Integer, Integer> map = this.mDisplayImePolicyCache;
        if (!map.containsKey(Integer.valueOf(i))) {
            if (ProtoLogCache.WM_ERROR_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 1100065297, 1, "Attempted to get IME policy of a display that does not exist: %d", new Object[]{Long.valueOf(i)});
            }
            return 1;
        }
        return map.get(Integer.valueOf(i)).intValue();
    }

    public void setDisplayImePolicy(int i, @WindowManager.DisplayImePolicy int i2) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "setDisplayImePolicy()")) {
            throw new SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContentOrCreate = getDisplayContentOrCreate(i, null);
                    if (displayContentOrCreate == null) {
                        if (ProtoLogCache.WM_ERROR_enabled) {
                            ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, -292790591, 1, "Attempted to set IME policy to a display that does not exist: %d", new Object[]{Long.valueOf(i)});
                        }
                        resetPriorityAfterLockedSection();
                    } else {
                        if (!displayContentOrCreate.isTrusted()) {
                            throw new SecurityException("Attempted to set IME policy to an untrusted virtual display: " + i);
                        }
                        this.mDisplayWindowSettings.setDisplayImePolicy(displayContentOrCreate, i2);
                        displayContentOrCreate.reconfigureDisplayLocked();
                        resetPriorityAfterLockedSection();
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void registerShortcutKey(long j, IShortcutService iShortcutService) throws RemoteException {
        if (!checkCallingPermission("android.permission.REGISTER_WINDOW_MANAGER_LISTENERS", "registerShortcutKey")) {
            throw new SecurityException("Requires REGISTER_WINDOW_MANAGER_LISTENERS permission");
        }
        this.mPolicy.registerShortcutKey(j, iShortcutService);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public final class LocalService extends WindowManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public WindowManagerInternal.AccessibilityControllerInternal getAccessibilityController() {
            return AccessibilityController.getAccessibilityControllerInternal(WindowManagerService.this);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void clearSnapshotCache() {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.mTaskSnapshotController.clearSnapshotCache();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void requestTraversalFromDisplayManager() {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.requestTraversal();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void onDisplayManagerReceivedDeviceState(final int i) {
            WindowManagerService.this.mH.post(new Runnable() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    WindowManagerService.LocalService.this.lambda$onDisplayManagerReceivedDeviceState$0(i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDisplayManagerReceivedDeviceState$0(int i) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.mRoot.onDisplayManagerReceivedDeviceState(i);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setMagnificationSpec(int i, MagnificationSpec magnificationSpec) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (WindowManagerService.this.mAccessibilityController.hasCallbacks()) {
                        WindowManagerService.this.mAccessibilityController.setMagnificationSpec(i, magnificationSpec);
                    } else {
                        throw new IllegalStateException("Magnification callbacks not set!");
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setForceShowMagnifiableBounds(int i, boolean z) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (WindowManagerService.this.mAccessibilityController.hasCallbacks()) {
                        WindowManagerService.this.mAccessibilityController.setForceShowMagnifiableBounds(i, z);
                    } else {
                        throw new IllegalStateException("Magnification callbacks not set!");
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void getMagnificationRegion(int i, Region region) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (WindowManagerService.this.mAccessibilityController.hasCallbacks()) {
                        WindowManagerService.this.mAccessibilityController.getMagnificationRegion(i, region);
                    } else {
                        throw new IllegalStateException("Magnification callbacks not set!");
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean setMagnificationCallbacks(int i, WindowManagerInternal.MagnificationCallbacks magnificationCallbacks) {
            boolean magnificationCallbacks2;
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    magnificationCallbacks2 = WindowManagerService.this.mAccessibilityController.setMagnificationCallbacks(i, magnificationCallbacks);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return magnificationCallbacks2;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setWindowsForAccessibilityCallback(int i, WindowManagerInternal.WindowsForAccessibilityCallback windowsForAccessibilityCallback) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.mAccessibilityController.setWindowsForAccessibilityCallback(i, windowsForAccessibilityCallback);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setInputFilter(IInputFilter iInputFilter) {
            WindowManagerService.this.mInputManager.setInputFilter(iInputFilter);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public IBinder getFocusedWindowToken() {
            IBinder focusedWindowToken;
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    focusedWindowToken = WindowManagerService.this.mAccessibilityController.getFocusedWindowToken();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return focusedWindowToken;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public IBinder getFocusedWindowTokenFromWindowStates() {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowState focusedWindowLocked = WindowManagerService.this.getFocusedWindowLocked();
                    if (focusedWindowLocked == null) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    IBinder asBinder = focusedWindowLocked.mClient.asBinder();
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return asBinder;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void moveDisplayToTopIfAllowed(int i) {
            WindowManagerService.this.moveDisplayToTopIfAllowed(i);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void requestWindowFocus(IBinder iBinder) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.onPointerDownOutsideFocusLocked(WindowManagerService.this.getInputTargetFromWindowTokenLocked(iBinder));
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isKeyguardLocked() {
            return WindowManagerService.this.isKeyguardLocked();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isKeyguardShowingAndNotOccluded() {
            return WindowManagerService.this.isKeyguardShowingAndNotOccluded();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isKeyguardSecure(int i) {
            return WindowManagerService.this.mPolicy.isKeyguardSecure(i);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void showGlobalActions() {
            WindowManagerService.this.showGlobalActions();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void getWindowFrame(IBinder iBinder, Rect rect) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowState windowState = WindowManagerService.this.mWindowMap.get(iBinder);
                    if (windowState != null) {
                        rect.set(windowState.getFrame());
                    } else {
                        rect.setEmpty();
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public Pair<Matrix, MagnificationSpec> getWindowTransformationMatrixAndMagnificationSpec(IBinder iBinder) {
            return WindowManagerService.this.mAccessibilityController.getWindowTransformationMatrixAndMagnificationSpec(iBinder);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void waitForAllWindowsDrawn(Message message, long j, int i) {
            WindowContainer<?> displayContent;
            boolean z;
            Objects.requireNonNull(message.getTarget());
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    displayContent = i == -1 ? WindowManagerService.this.mRoot : WindowManagerService.this.mRoot.getDisplayContent(i);
                } finally {
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            if (displayContent == null) {
                message.sendToTarget();
                return;
            }
            WindowManagerGlobalLock windowManagerGlobalLock2 = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock2) {
                try {
                    displayContent.waitForAllWindowsDrawn();
                    WindowManagerService.this.mWindowPlacerLocked.requestTraversal();
                    WindowManagerService.this.mH.removeMessages(24, displayContent);
                    if (displayContent.mWaitingForDrawn.isEmpty()) {
                        WindowManagerService.this.mWindowManagerServiceExt.clearSkipWaitingForDrawn();
                        z = true;
                    } else {
                        if (Trace.isTagEnabled(32L)) {
                            for (int i2 = 0; i2 < displayContent.mWaitingForDrawn.size(); i2++) {
                                WindowManagerService.this.traceStartWaitingForWindowDrawn(displayContent.mWaitingForDrawn.get(i2));
                            }
                        }
                        WindowManagerService.this.mWaitingForDrawnCallbacks.put(displayContent, message);
                        WindowManagerService.this.mH.sendNewMessageDelayed(24, displayContent, j);
                        WindowManagerService.this.checkDrawnWindowsLocked();
                        z = false;
                    }
                } finally {
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            Slog.d(WindowManagerService.TAG, "waitForAllWindowsDrawn displayId=" + i + "," + z + ",message=" + message);
            if (z) {
                message.sendToTarget();
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setForcedDisplaySize(int i, int i2, int i3) {
            WindowManagerService.this.setForcedDisplaySize(i, i2, i3);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void clearForcedDisplaySize(int i) {
            WindowManagerService.this.clearForcedDisplaySize(i);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void addWindowToken(IBinder iBinder, int i, int i2, Bundle bundle) {
            WindowManagerService.this.addWindowToken(iBinder, i, i2, bundle);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void removeWindowToken(IBinder iBinder, boolean z, boolean z2, int i) {
            WindowManagerService.this.removeWindowToken(iBinder, z, z2, i);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void moveWindowTokenToDisplay(IBinder iBinder, int i) {
            WindowManagerService.this.moveWindowTokenToDisplay(iBinder, i);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void registerAppTransitionListener(WindowManagerInternal.AppTransitionListener appTransitionListener) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.getDefaultDisplayContentLocked().mAppTransition.registerListenerLocked(appTransitionListener);
                    WindowManagerService.this.mAtmService.getTransitionController().registerLegacyListener(appTransitionListener);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void registerTaskSystemBarsListener(WindowManagerInternal.TaskSystemBarsListener taskSystemBarsListener) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.mTaskSystemBarsListenerController.registerListener(taskSystemBarsListener);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void unregisterTaskSystemBarsListener(WindowManagerInternal.TaskSystemBarsListener taskSystemBarsListener) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.mTaskSystemBarsListenerController.unregisterListener(taskSystemBarsListener);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void registerKeyguardExitAnimationStartListener(WindowManagerInternal.KeyguardExitAnimationStartListener keyguardExitAnimationStartListener) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.getDefaultDisplayContentLocked().mAppTransition.registerKeygaurdExitAnimationStartListener(keyguardExitAnimationStartListener);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void reportPasswordChanged(int i) {
            WindowManagerService.this.mKeyguardDisableHandler.updateKeyguardEnabled(i);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public int getInputMethodWindowVisibleHeight(int i) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = WindowManagerService.this.mRoot.getDisplayContent(i);
                    if (displayContent == null) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return 0;
                    }
                    int inputMethodWindowVisibleHeight = displayContent.getInputMethodWindowVisibleHeight();
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return inputMethodWindowVisibleHeight;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setDismissImeOnBackKeyPressed(boolean z) {
            WindowManagerService.this.mPolicy.setDismissImeOnBackKeyPressed(z);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void updateInputMethodTargetWindow(IBinder iBinder, IBinder iBinder2) {
            if (WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                Slog.w(WindowManagerService.TAG, "updateInputMethodTargetWindow: imeToken=" + iBinder + " imeTargetWindowToken=" + iBinder2);
            }
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    InputTarget inputTargetFromWindowTokenLocked = WindowManagerService.this.getInputTargetFromWindowTokenLocked(iBinder2);
                    if (inputTargetFromWindowTokenLocked != null) {
                        inputTargetFromWindowTokenLocked.getDisplayContent().updateImeInputAndControlTarget(inputTargetFromWindowTokenLocked);
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isHardKeyboardAvailable() {
            boolean z;
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    z = WindowManagerService.this.mHardKeyboardAvailable;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return z;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setOnHardKeyboardStatusChangeListener(WindowManagerInternal.OnHardKeyboardStatusChangeListener onHardKeyboardStatusChangeListener) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.mHardKeyboardStatusChangeListener = onHardKeyboardStatusChangeListener;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void computeWindowsForAccessibility(int i) {
            WindowManagerService.this.mAccessibilityController.performComputeChangedWindowsNot(i, true);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setVr2dDisplayId(int i) {
            if (WindowManagerDebugConfig.DEBUG_DISPLAY) {
                Slog.d(WindowManagerService.TAG, "setVr2dDisplayId called for: " + i);
            }
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.mVr2dDisplayId = i;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void registerDragDropControllerCallback(WindowManagerInternal.IDragDropCallback iDragDropCallback) {
            WindowManagerService.this.mDragDropController.registerCallback(iDragDropCallback);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void lockNow() {
            Slog.d(WindowManagerService.TAG, "lockNow called by uid: " + Binder.getCallingUid() + " pid: " + Binder.getCallingPid());
            WindowManagerService.this.lockNow(null);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public int getWindowOwnerUserId(IBinder iBinder) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowState windowState = WindowManagerService.this.mWindowMap.get(iBinder);
                    if (windowState == null) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return -10000;
                    }
                    int i = windowState.mShowUserId;
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return i;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setWallpaperShowWhenLocked(IBinder iBinder, boolean z) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowToken windowToken = WindowManagerService.this.mRoot.getWindowToken(iBinder);
                    if (windowToken != null && windowToken.asWallpaperToken() != null) {
                        windowToken.asWallpaperToken().setShowWhenLocked(z);
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (ProtoLogCache.WM_ERROR_enabled) {
                        ProtoLogImpl.w(ProtoLogGroup.WM_ERROR, 2043434284, 0, "setWallpaperShowWhenLocked: non-existent wallpaper token: %s", new Object[]{String.valueOf(iBinder)});
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isUidFocused(int i) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    for (int childCount = WindowManagerService.this.mRoot.getChildCount() - 1; childCount >= 0; childCount--) {
                        WindowState windowState = WindowManagerService.this.mRoot.getChildAt(childCount).mCurrentFocus;
                        if (windowState != null && i == windowState.getOwningUid()) {
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return true;
                        }
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public int hasInputMethodClientFocus(IBinder iBinder, int i, int i2, int i3) {
            if (i3 == -1) {
                return -3;
            }
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent topFocusedDisplayContent = WindowManagerService.this.mRoot.getTopFocusedDisplayContent();
                    InputTarget inputTargetFromWindowTokenLocked = WindowManagerService.this.getInputTargetFromWindowTokenLocked(iBinder);
                    if (inputTargetFromWindowTokenLocked == null) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return -1;
                    }
                    int displayId = inputTargetFromWindowTokenLocked.getDisplayContent().getDisplayId();
                    if (displayId != i3) {
                        Slog.e(WindowManagerService.TAG, "isInputMethodClientFocus: display ID mismatch. from client: " + i3 + " from window: " + displayId);
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return -2;
                    }
                    if (topFocusedDisplayContent != null && topFocusedDisplayContent.getDisplayId() == i3 && topFocusedDisplayContent.hasAccess(i)) {
                        if (inputTargetFromWindowTokenLocked.isInputMethodClientFocus(i, i2)) {
                            if (WindowManagerService.this.mWindowManagerServiceExt.isIMETargetWindowHasFocus(inputTargetFromWindowTokenLocked)) {
                                WindowManagerService.resetPriorityAfterLockedSection();
                                return 0;
                            }
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return -1;
                        }
                        WindowState windowState = topFocusedDisplayContent.mCurrentFocus;
                        if (windowState != null) {
                            Session session = windowState.mSession;
                            if (session.mUid == i && session.mPid == i2) {
                                int i4 = windowState.canBeImeTarget() ? 0 : -1;
                                WindowManagerService.resetPriorityAfterLockedSection();
                                return i4;
                            }
                        }
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return -1;
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return -3;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void showImePostLayout(IBinder iBinder, ImeTracker.Token token) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    InputTarget inputTargetFromWindowTokenLocked = WindowManagerService.this.getInputTargetFromWindowTokenLocked(iBinder);
                    if (ProtoLogGroup.WM_DEBUG_ADD_REMOVE.isLogToLogcat()) {
                        Slog.i(WindowManagerService.TAG, "showImePostLayout:" + inputTargetFromWindowTokenLocked + " Caller=" + Debug.getCallers(1));
                    }
                    if (inputTargetFromWindowTokenLocked == null) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    Trace.asyncTraceBegin(32L, "WMS.showImePostLayout", 0);
                    InsetsControlTarget imeControlTarget = inputTargetFromWindowTokenLocked.getImeControlTarget();
                    WindowState window = imeControlTarget.getWindow();
                    (window != null ? window.getDisplayContent() : WindowManagerService.this.getDefaultDisplayContentLocked()).getInsetsStateController().getImeSourceProvider().scheduleShowImePostLayout(imeControlTarget, token);
                    WindowManagerService.resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void hideIme(IBinder iBinder, int i, ImeTracker.Token token) {
            Trace.traceBegin(32L, "WMS.hideIme");
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowState windowState = WindowManagerService.this.mWindowMap.get(iBinder);
                    if (ProtoLogCache.WM_DEBUG_IME_enabled) {
                        ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_IME, 95216706, 0, (String) null, new Object[]{String.valueOf(windowState)});
                    }
                    DisplayContent displayContent = WindowManagerService.this.mRoot.getDisplayContent(i);
                    if (windowState != null) {
                        WindowState window = windowState.getImeControlTarget().getWindow();
                        if (window != null) {
                            displayContent = window.getDisplayContent();
                        }
                        displayContent.getInsetsStateController().getImeSourceProvider().abortShowImePostLayout();
                    }
                    if (displayContent != null && displayContent.mRemoteInsetsControlTarget != null) {
                        InsetsControlTarget imeTarget = displayContent.getImeTarget(2);
                        DisplayContent.RemoteInsetsControlTarget remoteInsetsControlTarget = displayContent.mRemoteInsetsControlTarget;
                        if (imeTarget != remoteInsetsControlTarget && (remoteInsetsControlTarget.getRequestedVisibleTypes() & WindowInsets.Type.ime()) != 0) {
                            Slog.d(WindowManagerService.TAG, "update mRemoteInsetsControlTarget visible force when hideIme");
                            displayContent.mRemoteInsetsControlTarget.setRequestedVisibleTypes(WindowInsets.Type.defaultVisible());
                        }
                    }
                    if (displayContent != null && displayContent.getImeTarget(2) != null) {
                        ImeTracker.forLogging().onProgress(token, 20);
                        if (ProtoLogCache.WM_DEBUG_IME_enabled) {
                            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_IME, -547111355, 0, (String) null, new Object[]{String.valueOf(displayContent.getImeTarget(2))});
                        }
                        displayContent.getImeTarget(2).hideInsets(WindowInsets.Type.ime(), true, token);
                    } else {
                        ImeTracker.forLogging().onFailed(token, 20);
                    }
                    if (displayContent != null) {
                        displayContent.getInsetsStateController().getImeSourceProvider().setImeShowing(false);
                        displayContent.forAllActivities(new Consumer() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda5
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                ((ActivityRecord) obj).mImeInsetsFrozenUntilStartInput = false;
                            }
                        });
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            Trace.traceEnd(32L);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isUidAllowedOnDisplay(int i, int i2) {
            boolean z = true;
            if (i == 0) {
                return true;
            }
            if (i == -1) {
                return false;
            }
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = WindowManagerService.this.mRoot.getDisplayContent(i);
                    if (displayContent == null || !displayContent.hasAccess(i2)) {
                        z = false;
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return z;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public int getDisplayIdForWindow(IBinder iBinder) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowState windowState = WindowManagerService.this.mWindowMap.get(iBinder);
                    if (windowState == null) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return -1;
                    }
                    int displayId = windowState.getDisplayContent().getDisplayId();
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return displayId;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public int getTopFocusedDisplayId() {
            int displayId;
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    displayId = WindowManagerService.this.mRoot.getTopFocusedDisplayContent().getDisplayId();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return displayId;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public Context getTopFocusedDisplayUiContext() {
            Context displayUiContext;
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    displayUiContext = WindowManagerService.this.mRoot.getTopFocusedDisplayContent().getDisplayUiContext();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return displayUiContext;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean shouldShowSystemDecorOnDisplay(int i) {
            boolean shouldShowSystemDecors;
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    shouldShowSystemDecors = WindowManagerService.this.shouldShowSystemDecors(i);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return shouldShowSystemDecors;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        @WindowManager.DisplayImePolicy
        public int getDisplayImePolicy(int i) {
            return WindowManagerService.this.getDisplayImePolicy(i);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void addRefreshRateRangeForPackage(final String str, final float f, final float f2) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.mRoot.forAllDisplays(new Consumer() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            WindowManagerService.LocalService.lambda$addRefreshRateRangeForPackage$2(str, f, f2, (DisplayContent) obj);
                        }
                    });
                    WindowManagerService.this.mRoot.forAllDisplays(new Consumer() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda2
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            WindowManagerService.LocalService.lambda$addRefreshRateRangeForPackage$3(str, f, f2, (DisplayContent) obj);
                        }
                    });
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$addRefreshRateRangeForPackage$2(String str, float f, float f2, DisplayContent displayContent) {
            displayContent.getDisplayPolicy().getRefreshRatePolicy().addRefreshRateRangeForPackage(str, f, f2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$addRefreshRateRangeForPackage$3(String str, float f, float f2, DisplayContent displayContent) {
            displayContent.getWrapper().getExtImpl().addRefreshRateRangeForPackage(str, f, f2);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void removeRefreshRateRangeForPackage(final String str) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.mRoot.forAllDisplays(new Consumer() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda3
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            WindowManagerService.LocalService.lambda$removeRefreshRateRangeForPackage$4(str, (DisplayContent) obj);
                        }
                    });
                    WindowManagerService.this.mRoot.forAllDisplays(new Consumer() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda4
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            WindowManagerService.LocalService.lambda$removeRefreshRateRangeForPackage$5(str, (DisplayContent) obj);
                        }
                    });
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$removeRefreshRateRangeForPackage$4(String str, DisplayContent displayContent) {
            displayContent.getDisplayPolicy().getRefreshRatePolicy().removeRefreshRateRangeForPackage(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$removeRefreshRateRangeForPackage$5(String str, DisplayContent displayContent) {
            displayContent.getWrapper().getExtImpl().removeRefreshRateRangeForPackage(str);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isTouchOrFaketouchDevice() {
            boolean z;
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService windowManagerService = WindowManagerService.this;
                    if (windowManagerService.mIsTouchDevice && !windowManagerService.mIsFakeTouchDevice) {
                        throw new IllegalStateException("touchscreen supported device must report faketouch.");
                    }
                    z = windowManagerService.mIsFakeTouchDevice;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return z;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public KeyInterceptionInfo getKeyInterceptionInfoFromToken(IBinder iBinder) {
            return WindowManagerService.this.mKeyInterceptionInfoForToken.get(iBinder);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setAccessibilityIdToSurfaceMetadata(IBinder iBinder, int i) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowState windowState = WindowManagerService.this.mWindowMap.get(iBinder);
                    if (windowState == null) {
                        Slog.w(WindowManagerService.TAG, "Cannot find window which accessibility connection is added to");
                        WindowManagerService.resetPriorityAfterLockedSection();
                    } else {
                        WindowManagerService.this.mTransaction.setMetadata(windowState.mSurfaceControl, 5, i).apply();
                        WindowManagerService.resetPriorityAfterLockedSection();
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public String getWindowName(IBinder iBinder) {
            String name;
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowState windowState = WindowManagerService.this.mWindowMap.get(iBinder);
                    name = windowState != null ? windowState.getName() : null;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return name;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public WindowManagerInternal.ImeTargetInfo onToggleImeRequested(boolean z, IBinder iBinder, IBinder iBinder2, int i) {
            String name;
            String name2;
            String str;
            String str2;
            String str3;
            String str4;
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowState windowState = WindowManagerService.this.mWindowMap.get(iBinder);
                    name = windowState != null ? windowState.getName() : "null";
                    WindowState windowState2 = WindowManagerService.this.mWindowMap.get(iBinder2);
                    name2 = windowState2 != null ? windowState2.getName() : "null";
                    DisplayContent displayContent = WindowManagerService.this.mRoot.getDisplayContent(i);
                    if (displayContent != null) {
                        InsetsControlTarget imeTarget = displayContent.getImeTarget(2);
                        if (imeTarget != null) {
                            WindowState asWindowOrNull = InsetsControlTarget.asWindowOrNull(imeTarget);
                            str4 = asWindowOrNull != null ? asWindowOrNull.getName() : imeTarget.toString();
                        } else {
                            str4 = "null";
                        }
                        InsetsControlTarget imeTarget2 = displayContent.getImeTarget(0);
                        String name3 = imeTarget2 != null ? imeTarget2.getWindow().getName() : "null";
                        SurfaceControl surfaceControl = displayContent.mInputMethodSurfaceParent;
                        String surfaceControl2 = surfaceControl != null ? surfaceControl.toString() : "null";
                        if (z) {
                            displayContent.onShowImeRequested();
                        }
                        str = str4;
                        str2 = name3;
                        str3 = surfaceControl2;
                    } else {
                        str = "no-display";
                        str2 = str;
                        str3 = str2;
                    }
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            return new WindowManagerInternal.ImeTargetInfo(name, name2, str, str2, str3);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean shouldRestoreImeVisibility(IBinder iBinder) {
            return WindowManagerService.this.shouldRestoreImeVisibility(iBinder);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void addTrustedTaskOverlay(int i, SurfaceControlViewHost.SurfacePackage surfacePackage) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    Task rootTask = WindowManagerService.this.mRoot.getRootTask(i);
                    if (rootTask == null) {
                        throw new IllegalArgumentException("no task with taskId" + i);
                    }
                    rootTask.addTrustedOverlay(surfacePackage, rootTask.getTopVisibleAppMainWindow());
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void removeTrustedTaskOverlay(int i, SurfaceControlViewHost.SurfacePackage surfacePackage) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    Task rootTask = WindowManagerService.this.mRoot.getRootTask(i);
                    if (rootTask == null) {
                        throw new IllegalArgumentException("no task with taskId" + i);
                    }
                    rootTask.removeTrustedOverlay(surfacePackage);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public SurfaceControl getHandwritingSurfaceForDisplay(int i) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = WindowManagerService.this.mRoot.getDisplayContent(i);
                    if (displayContent == null) {
                        Slog.e(WindowManagerService.TAG, "Failed to create a handwriting surface on display: " + i + " - DisplayContent not found.");
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    SurfaceControl inputOverlayLayer = displayContent.getInputOverlayLayer();
                    if (inputOverlayLayer == null) {
                        Slog.e(WindowManagerService.TAG, "Failed to create a gesture monitor on display: " + i + " - Input overlay layer is not initialized.");
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    SurfaceControl build = WindowManagerService.this.makeSurfaceBuilder(displayContent.getSession()).setContainerLayer().setName("IME Handwriting Surface").setCallsite("getHandwritingSurfaceForDisplay").setParent(inputOverlayLayer).build();
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return build;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isPointInsideWindow(IBinder iBinder, int i, float f, float f2) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowState windowState = WindowManagerService.this.mWindowMap.get(iBinder);
                    if (windowState != null && windowState.getDisplayId() == i) {
                        boolean contains = windowState.getBounds().contains((int) f, (int) f2);
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return contains;
                    }
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean setContentRecordingSession(ContentRecordingSession contentRecordingSession) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                if (contentRecordingSession != null) {
                    try {
                        if (contentRecordingSession.getContentToRecord() == 1) {
                            WindowContainerToken taskWindowContainerTokenForLaunchCookie = WindowManagerService.this.getTaskWindowContainerTokenForLaunchCookie(contentRecordingSession.getTokenToRecord());
                            if (taskWindowContainerTokenForLaunchCookie == null) {
                                Slog.w(WindowManagerService.TAG, "Handling a new recording session; unable to find the WindowContainerToken");
                                WindowManagerService.resetPriorityAfterLockedSection();
                                return false;
                            }
                            contentRecordingSession.setTokenToRecord(taskWindowContainerTokenForLaunchCookie.asBinder());
                            WindowManagerService windowManagerService = WindowManagerService.this;
                            windowManagerService.mContentRecordingController.setContentRecordingSessionLocked(contentRecordingSession, windowManagerService);
                            WindowManagerService.resetPriorityAfterLockedSection();
                            return true;
                        }
                    } catch (Throwable th) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                WindowManagerService windowManagerService2 = WindowManagerService.this;
                windowManagerService2.mContentRecordingController.setContentRecordingSessionLocked(contentRecordingSession, windowManagerService2);
                WindowManagerService.resetPriorityAfterLockedSection();
                return true;
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public SurfaceControl getA11yOverlayLayer(int i) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = WindowManagerService.this.mRoot.getDisplayContent(i);
                    if (displayContent == null) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    SurfaceControl a11yOverlayLayer = displayContent.getA11yOverlayLayer();
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return a11yOverlayLayer;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean keepSimultaneousDisplay() {
            WindowState window;
            DisplayContent displayContent = WindowManagerService.this.mRoot.getDisplayContent(1);
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    window = displayContent.getWindow(new Predicate() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            boolean lambda$keepSimultaneousDisplay$6;
                            lambda$keepSimultaneousDisplay$6 = WindowManagerService.LocalService.lambda$keepSimultaneousDisplay$6((WindowState) obj);
                            return lambda$keepSimultaneousDisplay$6;
                        }
                    });
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
            ActivityRecord activityRecord = displayContent.topRunningActivity();
            if (activityRecord != null) {
                Slog.d(WindowManagerService.TAG, "win : " + window + " topActivityRecord " + activityRecord + "  nowVisible : " + activityRecord.nowVisible + " TurnScreenOnFlag = " + activityRecord.getTurnScreenOnFlag());
            }
            return window != null || (activityRecord != null && activityRecord.nowVisible && activityRecord.getTurnScreenOnFlag());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$keepSimultaneousDisplay$6(WindowState windowState) {
            return windowState.isVisible() && windowState.getDisplayId() == 1 && (windowState.mAttrs.flags & 2097152) != 0;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setInputMethodTargetChangeListener(ImeTargetChangeListener imeTargetChangeListener) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowManagerService.this.mImeTargetChangeListener = imeTargetChangeListener;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private final class ImeTargetVisibilityPolicyImpl extends ImeTargetVisibilityPolicy {
        private ImeTargetVisibilityPolicyImpl() {
        }

        @Override // com.android.server.wm.ImeTargetVisibilityPolicy
        public boolean showImeScreenshot(IBinder iBinder, int i) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    WindowState windowState = WindowManagerService.this.mWindowMap.get(iBinder);
                    if (windowState == null) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    DisplayContent displayContent = WindowManagerService.this.mRoot.getDisplayContent(i);
                    if (displayContent == null) {
                        Slog.w(WindowManagerService.TAG, "Invalid displayId:" + i + ", fail to show ime screenshot");
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    displayContent.showImeScreenshot(windowState);
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return true;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.ImeTargetVisibilityPolicy
        public boolean removeImeScreenshot(int i) {
            WindowManagerGlobalLock windowManagerGlobalLock = WindowManagerService.this.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContent = WindowManagerService.this.mRoot.getDisplayContent(i);
                    if (displayContent == null) {
                        Slog.w(WindowManagerService.TAG, "Invalid displayId:" + i + ", fail to remove ime screenshot");
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    displayContent.removeImeSurfaceImmediately();
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return true;
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerAppFreezeListener(AppFreezeListener appFreezeListener) {
        if (this.mAppFreezeListeners.contains(appFreezeListener)) {
            return;
        }
        this.mAppFreezeListeners.add(appFreezeListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterAppFreezeListener(AppFreezeListener appFreezeListener) {
        this.mAppFreezeListeners.remove(appFreezeListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void inSurfaceTransaction(Runnable runnable) {
        SurfaceControl.openTransaction();
        try {
            runnable.run();
        } finally {
            SurfaceControl.closeTransaction();
        }
    }

    public void disableNonVrUi(boolean z) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            boolean z2 = !z;
            try {
                if (z2 == this.mShowAlertWindowNotifications) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                this.mShowAlertWindowNotifications = z2;
                for (int size = this.mSessions.size() - 1; size >= 0; size--) {
                    this.mSessions.valueAt(size).setShowingAlertWindowNotificationAllowed(this.mShowAlertWindowNotifications);
                }
                resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasWideColorGamutSupport() {
        return this.mHasWideColorGamutSupport && SystemProperties.getInt("persist.sys.sf.native_mode", 0) != 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasHdrSupport() {
        return this.mHasHdrSupport && hasWideColorGamutSupport();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateNonSystemOverlayWindowsVisibilityIfNeeded(WindowState windowState, boolean z) {
        if (windowState.hideNonSystemOverlayWindowsWhenVisible() || this.mHidingNonSystemOverlayWindows.contains(windowState)) {
            boolean z2 = !this.mHidingNonSystemOverlayWindows.isEmpty();
            if (z && windowState.hideNonSystemOverlayWindowsWhenVisible()) {
                if (!this.mHidingNonSystemOverlayWindows.contains(windowState)) {
                    this.mHidingNonSystemOverlayWindows.add(windowState);
                }
            } else {
                this.mHidingNonSystemOverlayWindows.remove(windowState);
            }
            final boolean z3 = !this.mHidingNonSystemOverlayWindows.isEmpty();
            if (z2 == z3) {
                return;
            }
            this.mRoot.forAllWindows(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda22
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((WindowState) obj).setForceHideNonSystemOverlayWindowIfNeeded(z3);
                }
            }, false);
        }
    }

    public void applyMagnificationSpecLocked(int i, MagnificationSpec magnificationSpec) {
        DisplayContent displayContent = this.mRoot.getDisplayContent(i);
        if (displayContent != null) {
            displayContent.applyMagnificationSpec(magnificationSpec);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl.Builder makeSurfaceBuilder(SurfaceSession surfaceSession) {
        return this.mSurfaceControlFactory.apply(surfaceSession);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLockTaskStateChanged(final int i) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRoot.forAllDisplayPolicies(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((DisplayPolicy) obj).onLockTaskStateChangedLw(i);
                    }
                });
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void syncInputTransactions(boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (z) {
                try {
                    waitForAnimationsToComplete();
                } catch (InterruptedException e) {
                    Slog.e(TAG, "Exception thrown while waiting for window infos to be reported", e);
                }
            }
            final SurfaceControl.Transaction transaction = this.mTransactionFactory.get();
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mWindowPlacerLocked.performSurfacePlacementIfScheduled();
                    this.mRoot.forAllDisplays(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda5
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            WindowManagerService.lambda$syncInputTransactions$19(transaction, (DisplayContent) obj);
                        }
                    });
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            transaction.addWindowInfosReportedListener(new SettingsStore$.ExternalSyntheticLambda1(countDownLatch)).apply();
            countDownLatch.await(5000L, TimeUnit.MILLISECONDS);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$syncInputTransactions$19(SurfaceControl.Transaction transaction, DisplayContent displayContent) {
        displayContent.getInputMonitor().updateInputWindowsImmediately(transaction);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(12:7|(1:45)(1:11)|12|(2:18|(5:37|38|39|41|42)(2:22|23))|44|(1:20)|37|38|39|41|42|5) */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void waitForAnimationsToComplete() {
        boolean z;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mAnimator.mNotifyWhenNoAnimation = true;
                long j = 5000;
                boolean z2 = false;
                while (j > 0) {
                    z2 = !this.mAtmService.getTransitionController().isShellTransitionsEnabled() && this.mRoot.forAllActivities(new Transition$ChangeInfo$$ExternalSyntheticLambda0());
                    if (!this.mAnimator.isAnimationScheduled() && !this.mRoot.isAnimating(5, -1) && !z2) {
                        z = false;
                        if (z && !this.mAtmService.getTransitionController().inTransition()) {
                            break;
                        }
                        long currentTimeMillis = System.currentTimeMillis();
                        this.mGlobalLock.wait(j);
                        j -= System.currentTimeMillis() - currentTimeMillis;
                    }
                    z = true;
                    if (z) {
                    }
                    long currentTimeMillis2 = System.currentTimeMillis();
                    this.mGlobalLock.wait(j);
                    j -= System.currentTimeMillis() - currentTimeMillis2;
                }
                this.mAnimator.mNotifyWhenNoAnimation = false;
                WindowContainer animatingContainer = this.mRoot.getAnimatingContainer(5, -1);
                if (this.mAnimator.isAnimationScheduled() || animatingContainer != null || z2) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Timed out waiting for animations to complete, animatingContainer=");
                    sb.append(animatingContainer);
                    sb.append(" animationType=");
                    sb.append(SurfaceAnimator.animationTypeToString(animatingContainer != null ? animatingContainer.mSurfaceAnimator.getAnimationType() : 0));
                    sb.append(" animateStarting=");
                    sb.append(z2);
                    Slog.w(TAG, sb.toString());
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAnimationFinished() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mGlobalLock.notifyAll();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPointerDownOutsideFocusLocked(InputTarget inputTarget) {
        Task task;
        if (inputTarget == null || !inputTarget.receiveFocusFromTapOutside()) {
            return;
        }
        RecentsAnimationController recentsAnimationController = this.mRecentsAnimationController;
        if (recentsAnimationController == null || recentsAnimationController.getTargetAppMainWindow() != inputTarget) {
            WindowState windowState = inputTarget.getWindowState();
            if (windowState == null || (task = windowState.getTask()) == null || !windowState.mTransitionController.isTransientHide(task)) {
                if (ProtoLogCache.WM_DEBUG_FOCUS_LIGHT_enabled) {
                    ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -561092364, 0, (String) null, new Object[]{String.valueOf(inputTarget)});
                }
                if (!this.mWindowManagerServiceExt.isHomePageOfSettingsTaskFragment(inputTarget, this.mFocusedInputTarget)) {
                    InputTarget inputTarget2 = this.mFocusedInputTarget;
                    if (inputTarget2 != inputTarget && inputTarget2 != null) {
                        inputTarget2.handleTapOutsideFocusOutsideSelf();
                    }
                    this.mAtmService.mTaskSupervisor.mUserLeaving = true;
                    inputTarget.handleTapOutsideFocusInsideSelf();
                    this.mAtmService.mTaskSupervisor.mUserLeaving = false;
                }
                this.mWindowManagerServiceExt.cpuFrequencyBoostIfNeed(inputTarget.getActivityRecord());
                this.mWindowManagerServiceExt.handleCompactWindowTouchFocusChange(inputTarget.getWindowState());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void handleTaskFocusChange(Task task, ActivityRecord activityRecord) {
        if (task == null) {
            return;
        }
        if (task.isActivityTypeHome()) {
            TaskDisplayArea displayArea = task.getDisplayArea();
            WindowState focusedWindow = getFocusedWindow();
            if (focusedWindow != null && displayArea != null && focusedWindow.isDescendantOf(displayArea) && this.mWindowManagerServiceExt.currentFoucusWindowModeNotZoomMode(focusedWindow.getWindowingMode()) && !this.mWindowManagerServiceExt.isFocusChangeWithNonFlexible(focusedWindow)) {
                return;
            }
        }
        if (this.mWindowManagerServiceExt.ignoreFingerprintWindow(this.mContext, task)) {
            Slog.d(TAG, "split screen mode, FP is shown, igonre focus change.");
            return;
        }
        if (this.mWindowManagerServiceExt.isActivityTypeMultiSearch(task)) {
            Slog.d(TAG, "multisearch task igonre focus change.");
        } else if (task.getWrapper().getExtImpl().isTaskCanvas() && !this.mWindowManagerServiceExt.isNoCanvasActivity(activityRecord)) {
            Slog.d(TAG, "canvas task igonre focus change.");
        } else {
            this.mAtmService.setFocusedTask(task.mTaskId, activityRecord);
        }
    }

    @VisibleForTesting
    WindowContainerToken getTaskWindowContainerTokenForLaunchCookie(final IBinder iBinder) {
        ActivityRecord activity = this.mRoot.getActivity(new Predicate() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getTaskWindowContainerTokenForLaunchCookie$20;
                lambda$getTaskWindowContainerTokenForLaunchCookie$20 = WindowManagerService.lambda$getTaskWindowContainerTokenForLaunchCookie$20(iBinder, (ActivityRecord) obj);
                return lambda$getTaskWindowContainerTokenForLaunchCookie$20;
            }
        });
        if (activity == null) {
            Slog.w(TAG, "Unable to find the activity for this launch cookie");
            return null;
        }
        if (activity.getTask() == null) {
            Slog.w(TAG, "Unable to find the task for this launch cookie");
            return null;
        }
        WindowContainerToken windowContainerToken = activity.getTask().mRemoteToken.toWindowContainerToken();
        if (windowContainerToken != null) {
            return windowContainerToken;
        }
        Slog.w(TAG, "Unable to find the WindowContainerToken for " + activity.getName());
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getTaskWindowContainerTokenForLaunchCookie$20(IBinder iBinder, ActivityRecord activityRecord) {
        return activityRecord.mLaunchCookie == iBinder;
    }

    private int sanitizeFlagSlippery(int i, String str, int i2, int i3) {
        if ((536870912 & i) == 0 || this.mContext.checkPermission("android.permission.ALLOW_SLIPPERY_TOUCHES", i3, i2) == 0) {
            return i;
        }
        Slog.w(TAG, "Removing FLAG_SLIPPERY from '" + str + "' because it doesn't have ALLOW_SLIPPERY_TOUCHES permission");
        return (-536870913) & i;
    }

    private int sanitizeSpyWindow(int i, String str, int i2, int i3) {
        if ((i & 4) == 0 || this.mContext.checkPermission("android.permission.MONITOR_INPUT", i3, i2) == 0) {
            return i;
        }
        throw new IllegalArgumentException("Cannot use INPUT_FEATURE_SPY from '" + str + "' because it doesn't the have MONITOR_INPUT permission");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void grantInputChannel(Session session, int i, int i2, int i3, SurfaceControl surfaceControl, IWindow iWindow, IBinder iBinder, int i4, int i5, int i6, int i7, IBinder iBinder2, IBinder iBinder3, String str, InputChannel inputChannel) {
        InputChannel openInputChannel;
        InputApplicationHandle applicationHandle;
        String embeddedWindow;
        int i8;
        int sanitizeWindowType = sanitizeWindowType(session, i3, iBinder2, i7);
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                EmbeddedWindowController.EmbeddedWindow embeddedWindow2 = new EmbeddedWindowController.EmbeddedWindow(session, this, iWindow, this.mInputToWindowMap.get(iBinder), i, i2, sanitizeWindowType, i3, iBinder3, str, (i4 & 8) == 0);
                openInputChannel = embeddedWindow2.openInputChannel();
                if (this.mWindowManagerServiceExt.isSecondaryhomePackageName(embeddedWindow2)) {
                    embeddedWindow2.setIsFocusable(false);
                }
                this.mEmbeddedWindowController.add(openInputChannel.getToken(), embeddedWindow2);
                applicationHandle = embeddedWindow2.getApplicationHandle();
                embeddedWindow = embeddedWindow2.toString();
                i8 = (i3 != 1 || embeddedWindow2.getWindowState() == null || (embeddedWindow2.getWindowState().mAttrs.flags & 1048576) == 0) ? i4 : i4 | 1048576;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        updateInputChannel(openInputChannel.getToken(), i, i2, i3, surfaceControl, embeddedWindow, applicationHandle, i8, i5, i6, sanitizeWindowType, null, iWindow);
        openInputChannel.copyTo(inputChannel);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean transferEmbeddedTouchFocusToHost(IWindow iWindow) {
        IBinder asBinder = iWindow.asBinder();
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                EmbeddedWindowController.EmbeddedWindow byWindowToken = this.mEmbeddedWindowController.getByWindowToken(asBinder);
                if (byWindowToken == null) {
                    Slog.w(TAG, "Attempt to transfer touch focus from non-existent embedded window");
                    resetPriorityAfterLockedSection();
                    return false;
                }
                WindowState windowState = byWindowToken.getWindowState();
                if (windowState == null) {
                    Slog.w(TAG, "Attempt to transfer touch focus from embedded window with no associated host");
                    resetPriorityAfterLockedSection();
                    return false;
                }
                IBinder inputChannelToken = byWindowToken.getInputChannelToken();
                if (inputChannelToken == null) {
                    Slog.w(TAG, "Attempt to transfer touch focus from embedded window with no input channel");
                    resetPriorityAfterLockedSection();
                    return false;
                }
                IBinder iBinder = windowState.mInputChannelToken;
                if (iBinder == null) {
                    Slog.w(TAG, "Attempt to transfer touch focus to a host window with no input channel");
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean transferTouchFocus = this.mInputManager.transferTouchFocus(inputChannelToken, iBinder);
                resetPriorityAfterLockedSection();
                return transferTouchFocus;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void updateInputChannel(IBinder iBinder, int i, int i2, int i3, SurfaceControl surfaceControl, String str, InputApplicationHandle inputApplicationHandle, int i4, int i5, int i6, int i7, Region region, IWindow iWindow) {
        InputWindowHandle inputWindowHandle = new InputWindowHandle(inputApplicationHandle, i3);
        inputWindowHandle.token = iBinder;
        inputWindowHandle.setWindowToken(iWindow);
        inputWindowHandle.name = str;
        int sanitizeFlagSlippery = sanitizeFlagSlippery(i4, str, i, i2);
        int sanitizeSpyWindow = sanitizeSpyWindow(i6, str, i, i2);
        int i8 = (536870936 & sanitizeFlagSlippery) | 32;
        inputWindowHandle.layoutParamsType = i7;
        inputWindowHandle.layoutParamsFlags = i8;
        int inputConfigFromWindowParams = InputConfigAdapter.getInputConfigFromWindowParams(i7, i8, sanitizeSpyWindow);
        inputWindowHandle.inputConfig = inputConfigFromWindowParams;
        if ((sanitizeFlagSlippery & 8) != 0) {
            inputWindowHandle.inputConfig = inputConfigFromWindowParams | 4;
        }
        if (i3 == 1 && (sanitizeFlagSlippery & 1048576) != 0) {
            inputWindowHandle.inputConfig |= 32;
        }
        if ((536870912 & i5) != 0) {
            inputWindowHandle.inputConfig |= 256;
        }
        inputWindowHandle.dispatchingTimeoutMillis = InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
        inputWindowHandle.ownerUid = i;
        inputWindowHandle.ownerPid = i2;
        if (region == null) {
            inputWindowHandle.replaceTouchableRegionWithCrop((SurfaceControl) null);
        } else {
            inputWindowHandle.touchableRegion.set(region);
            inputWindowHandle.replaceTouchableRegionWithCrop = false;
            if (this.mContext.checkPermission("android.permission.MANAGE_ACTIVITY_TASKS", i2, i) != 0) {
                inputWindowHandle.setTouchableRegionCrop(surfaceControl);
            }
        }
        SurfaceControl.Transaction transaction = this.mTransactionFactory.get();
        transaction.setInputWindowInfo(surfaceControl, inputWindowHandle);
        transaction.apply();
        transaction.close();
        surfaceControl.release();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateInputChannel(IBinder iBinder, int i, SurfaceControl surfaceControl, int i2, int i3, int i4, Region region) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                EmbeddedWindowController.EmbeddedWindow embeddedWindow = this.mEmbeddedWindowController.get(iBinder);
                if (embeddedWindow == null) {
                    Slog.e(TAG, "Couldn't find window for provided channelToken.");
                    resetPriorityAfterLockedSection();
                    return;
                }
                String embeddedWindow2 = embeddedWindow.toString();
                InputApplicationHandle applicationHandle = embeddedWindow.getApplicationHandle();
                if (!this.mWindowManagerServiceExt.isSecondaryhomePackageName(embeddedWindow)) {
                    embeddedWindow.setIsFocusable((i2 & 8) == 0);
                }
                resetPriorityAfterLockedSection();
                updateInputChannel(iBinder, embeddedWindow.mOwnerUid, embeddedWindow.mOwnerPid, i, surfaceControl, embeddedWindow2, applicationHandle, i2, i3, i4, embeddedWindow.mWindowType, region, embeddedWindow.mClient);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0054 A[Catch: all -> 0x0058, TRY_ENTER, TryCatch #2 {all -> 0x0058, blocks: (B:11:0x002e, B:19:0x0045, B:21:0x004a, B:27:0x0054, B:29:0x005c, B:30:0x005f), top: B:4:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x005c A[Catch: all -> 0x0058, TryCatch #2 {all -> 0x0058, blocks: (B:11:0x002e, B:19:0x0045, B:21:0x004a, B:27:0x0054, B:29:0x005c, B:30:0x005f), top: B:4:0x0010 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isLayerTracing() {
        Parcel parcel;
        if (!checkCallingPermission("android.permission.DUMP", "isLayerTracing()")) {
            throw new SecurityException("Requires DUMP permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        Parcel parcel2 = null;
        try {
            try {
                try {
                    IBinder service = ServiceManager.getService("SurfaceFlinger");
                    if (service != null) {
                        parcel = Parcel.obtain();
                        try {
                            parcel2 = Parcel.obtain();
                            parcel2.writeInterfaceToken("android.ui.ISurfaceComposer");
                            service.transact(1026, parcel2, parcel, 0);
                            boolean readBoolean = parcel.readBoolean();
                            parcel2.recycle();
                            parcel.recycle();
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                            return readBoolean;
                        } catch (RemoteException unused) {
                            Slog.e(TAG, "Failed to get layer tracing");
                            if (parcel2 != null) {
                                parcel2.recycle();
                            }
                            if (parcel != null) {
                                parcel.recycle();
                            }
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                            return false;
                        }
                    }
                } catch (RemoteException unused2) {
                    parcel = null;
                } catch (Throwable th) {
                    th = th;
                    if (0 != 0) {
                    }
                    if (0 != 0) {
                    }
                    throw th;
                }
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return false;
            } catch (Throwable th2) {
                th = th2;
                if (0 != 0) {
                    parcel2.recycle();
                }
                if (0 != 0) {
                    parcel2.recycle();
                }
                throw th;
            }
        } catch (Throwable th3) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th3;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0045, code lost:
    
        if (r5 == null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x004b, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0036, code lost:
    
        r5.recycle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0034, code lost:
    
        if (r5 != null) goto L18;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setLayerTracing(boolean z) {
        Parcel parcel;
        Throwable th;
        if (!checkCallingPermission("android.permission.DUMP", "setLayerTracing()")) {
            throw new SecurityException("Requires DUMP permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        Parcel parcel2 = null;
        try {
            try {
                try {
                    IBinder service = ServiceManager.getService("SurfaceFlinger");
                    if (service != null) {
                        parcel = Parcel.obtain();
                        try {
                            parcel.writeInterfaceToken("android.ui.ISurfaceComposer");
                            parcel.writeInt(z ? 1 : 0);
                            service.transact(1025, parcel, null, 0);
                            parcel2 = parcel;
                        } catch (RemoteException unused) {
                            parcel2 = parcel;
                            Slog.e(TAG, "Failed to set layer tracing");
                        } catch (Throwable th2) {
                            th = th2;
                            if (parcel != null) {
                                parcel.recycle();
                            }
                            throw th;
                        }
                    }
                } catch (Throwable th3) {
                    parcel = parcel2;
                    th = th3;
                }
            } catch (RemoteException unused2) {
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0040, code lost:
    
        if (r5 == null) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0046, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0031, code lost:
    
        r5.recycle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x002f, code lost:
    
        if (r5 != null) goto L14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setLayerTracingFlags(int i) {
        Parcel parcel;
        Throwable th;
        if (!checkCallingPermission("android.permission.DUMP", "setLayerTracingFlags")) {
            throw new SecurityException("Requires DUMP permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        Parcel parcel2 = null;
        try {
            try {
                try {
                    IBinder service = ServiceManager.getService("SurfaceFlinger");
                    if (service != null) {
                        parcel = Parcel.obtain();
                        try {
                            parcel.writeInterfaceToken("android.ui.ISurfaceComposer");
                            parcel.writeInt(i);
                            service.transact(1033, parcel, null, 0);
                            parcel2 = parcel;
                        } catch (RemoteException unused) {
                            parcel2 = parcel;
                            Slog.e(TAG, "Failed to set layer tracing flags");
                        } catch (Throwable th2) {
                            th = th2;
                            if (parcel != null) {
                                parcel.recycle();
                            }
                            throw th;
                        }
                    }
                } catch (RemoteException unused2) {
                }
            } catch (Throwable th3) {
                parcel = null;
                th = th3;
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0045, code lost:
    
        if (r5 == null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x004b, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0036, code lost:
    
        r5.recycle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0034, code lost:
    
        if (r5 != null) goto L18;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setActiveTransactionTracing(boolean z) {
        Parcel parcel;
        Throwable th;
        if (!checkCallingPermission("android.permission.DUMP", "setActiveTransactionTracing()")) {
            throw new SecurityException("Requires DUMP permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        Parcel parcel2 = null;
        try {
            try {
                try {
                    IBinder service = ServiceManager.getService("SurfaceFlinger");
                    if (service != null) {
                        parcel = Parcel.obtain();
                        try {
                            parcel.writeInterfaceToken("android.ui.ISurfaceComposer");
                            parcel.writeInt(z ? 1 : 0);
                            service.transact(1041, parcel, null, 0);
                            parcel2 = parcel;
                        } catch (RemoteException unused) {
                            parcel2 = parcel;
                            Slog.e(TAG, "Failed to set transaction tracing");
                        } catch (Throwable th2) {
                            th = th2;
                            if (parcel != null) {
                                parcel.recycle();
                            }
                            throw th;
                        }
                    }
                } catch (Throwable th3) {
                    parcel = parcel2;
                    th = th3;
                }
            } catch (RemoteException unused2) {
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean mirrorDisplay(int i, SurfaceControl surfaceControl) {
        if (!checkCallingPermission("android.permission.READ_FRAME_BUFFER", "mirrorDisplay()")) {
            throw new SecurityException("Requires READ_FRAME_BUFFER permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    Slog.e(TAG, "Invalid displayId " + i + " for mirrorDisplay");
                    resetPriorityAfterLockedSection();
                    return false;
                }
                SurfaceControl windowingLayer = displayContent.getWindowingLayer();
                resetPriorityAfterLockedSection();
                SurfaceControl mirrorSurface = SurfaceControl.mirrorSurface(windowingLayer);
                surfaceControl.copyFrom(mirrorSurface, "WMS.mirrorDisplay");
                mirrorSurface.release();
                return true;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public boolean getWindowInsets(int i, IBinder iBinder, InsetsState insetsState) {
        boolean areSystemBarsForcedConsumedLw;
        WindowState findMainWindow;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    DisplayContent displayContentOrCreate = getDisplayContentOrCreate(i, iBinder);
                    if (displayContentOrCreate == null) {
                        throw new WindowManager.InvalidDisplayException("Display#" + i + "could not be found!");
                    }
                    WindowToken windowToken = displayContentOrCreate.getWindowToken(iBinder);
                    displayContentOrCreate.getInsetsPolicy().getInsetsForWindowMetrics(windowToken, insetsState);
                    if ((windowToken instanceof ActivityRecord) && windowToken.getWindowingMode() == 120 && (findMainWindow = ((ActivityRecord) windowToken).findMainWindow()) != null) {
                        findMainWindow.getWrapper().getExtImpl().hookGetCompatInsetsState(insetsState);
                    }
                    areSystemBarsForcedConsumedLw = displayContentOrCreate.getDisplayPolicy().areSystemBarsForcedConsumedLw();
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
            return areSystemBarsForcedConsumedLw;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX WARN: Finally extract failed */
    public List<DisplayInfo> getPossibleDisplayInfo(int i) {
        List<DisplayInfo> possibleDisplayInfos;
        int callingUid = Binder.getCallingUid();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (!this.mAtmService.isCallerRecents(callingUid)) {
                        Slog.e(TAG, "Unable to verify uid for getPossibleDisplayInfo on uid " + callingUid);
                        possibleDisplayInfos = new ArrayList<>();
                    } else {
                        possibleDisplayInfos = this.mPossibleDisplayInfoMapper.getPossibleDisplayInfos(i);
                    }
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
            return possibleDisplayInfos;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<DisplayInfo> getPossibleDisplayInfoLocked(int i) {
        return this.mPossibleDisplayInfoMapper.getPossibleDisplayInfos(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void grantEmbeddedWindowFocus(Session session, IBinder iBinder, boolean z) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                EmbeddedWindowController.EmbeddedWindow byFocusToken = this.mEmbeddedWindowController.getByFocusToken(iBinder);
                if (byFocusToken == null) {
                    Slog.e(TAG, "Embedded window not found");
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (byFocusToken.mSession != session) {
                    Slog.e(TAG, "Window not in session:" + session);
                    resetPriorityAfterLockedSection();
                    return;
                }
                IBinder inputChannelToken = byFocusToken.getInputChannelToken();
                if (inputChannelToken == null) {
                    Slog.e(TAG, "Focus token found but input channel token not found");
                    resetPriorityAfterLockedSection();
                    return;
                }
                SurfaceControl.Transaction transaction = this.mTransactionFactory.get();
                int i = byFocusToken.mDisplayId;
                if (z) {
                    transaction.setFocusedWindow(inputChannelToken, byFocusToken.toString(), i).apply();
                    EventLog.writeEvent(LOGTAG_INPUT_FOCUS, "Focus request " + byFocusToken, "reason=grantEmbeddedWindowFocus(true)");
                } else {
                    DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                    WindowState findFocusedWindow = displayContent == null ? null : displayContent.findFocusedWindow();
                    if (findFocusedWindow == null) {
                        transaction.setFocusedWindow(null, null, i).apply();
                        if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
                            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_FOCUS, 958338552, 0, (String) null, new Object[]{String.valueOf(byFocusToken)});
                        }
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    transaction.setFocusedWindow(findFocusedWindow.mInputChannelToken, findFocusedWindow.getName(), i).apply();
                    EventLog.writeEvent(LOGTAG_INPUT_FOCUS, "Focus request " + findFocusedWindow, "reason=grantEmbeddedWindowFocus(false)");
                }
                if (!ProtoLogGroup.WM_DEBUG_FOCUS.isLogToLogcat()) {
                    Slog.d(TAG, "grantEmbeddedWindowFocus embeddedWindow=" + byFocusToken.toString() + ",grantFocus=" + z + ",inputToken=" + inputChannelToken);
                }
                if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_FOCUS, -2107721178, 0, (String) null, new Object[]{String.valueOf(byFocusToken), String.valueOf(z)});
                }
                resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void grantEmbeddedWindowFocus(Session session, IWindow iWindow, IBinder iBinder, boolean z) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowForClientLocked = windowForClientLocked(session, iWindow, false);
                if (windowForClientLocked == null) {
                    Slog.e(TAG, "Host window not found");
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (windowForClientLocked.mInputChannel == null) {
                    Slog.e(TAG, "Host window does not have an input channel");
                    resetPriorityAfterLockedSection();
                    return;
                }
                EmbeddedWindowController.EmbeddedWindow byFocusToken = this.mEmbeddedWindowController.getByFocusToken(iBinder);
                if (byFocusToken == null) {
                    Slog.e(TAG, "Embedded window not found");
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (byFocusToken.mHostWindowState != windowForClientLocked) {
                    Slog.e(TAG, "Embedded window does not belong to the host");
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (z) {
                    windowForClientLocked.mInputWindowHandle.setFocusTransferTarget(byFocusToken.getInputChannelToken());
                    EventLog.writeEvent(LOGTAG_INPUT_FOCUS, "Transfer focus request " + byFocusToken, "reason=grantEmbeddedWindowFocus(true)");
                } else {
                    windowForClientLocked.mInputWindowHandle.setFocusTransferTarget(null);
                    EventLog.writeEvent(LOGTAG_INPUT_FOCUS, "Transfer focus request " + windowForClientLocked, "reason=grantEmbeddedWindowFocus(false)");
                }
                DisplayContent displayContent = this.mRoot.getDisplayContent(windowForClientLocked.getDisplayId());
                if (displayContent != null) {
                    displayContent.getInputMonitor().updateInputWindowsLw(true);
                }
                if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_FOCUS, -2107721178, 0, (String) null, new Object[]{String.valueOf(byFocusToken), String.valueOf(z)});
                }
                resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void holdLock(IBinder iBinder, int i) {
        this.mTestUtilityService.verifyHoldLockToken(iBinder);
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                SystemClock.sleep(i);
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public String[] getSupportedDisplayHashAlgorithms() {
        return this.mDisplayHashController.getSupportedHashAlgorithms();
    }

    public VerifiedDisplayHash verifyDisplayHash(DisplayHash displayHash) {
        return this.mDisplayHashController.verifyDisplayHash(displayHash);
    }

    public void setDisplayHashThrottlingEnabled(boolean z) {
        if (!checkCallingPermission("android.permission.READ_FRAME_BUFFER", "setDisplayHashThrottle()")) {
            throw new SecurityException("Requires READ_FRAME_BUFFER permission");
        }
        this.mDisplayHashController.setDisplayHashThrottlingEnabled(z);
    }

    public boolean isTaskSnapshotSupported() {
        boolean z;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                z = !this.mTaskSnapshotController.shouldDisableSnapshots();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void generateDisplayHash(Session session, IWindow iWindow, Rect rect, String str, RemoteCallback remoteCallback) {
        Rect rect2 = new Rect(rect);
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowForClientLocked = windowForClientLocked(session, iWindow, false);
                if (windowForClientLocked == null) {
                    Slog.w(TAG, "Failed to generate DisplayHash. Invalid window");
                    this.mDisplayHashController.sendDisplayHashError(remoteCallback, -3);
                    resetPriorityAfterLockedSection();
                    return;
                }
                ActivityRecord activityRecord = windowForClientLocked.mActivityRecord;
                if (activityRecord != null && activityRecord.isState(ActivityRecord.State.RESUMED)) {
                    DisplayContent displayContent = windowForClientLocked.getDisplayContent();
                    if (displayContent == null) {
                        Slog.w(TAG, "Failed to generate DisplayHash. Window is not on a display");
                        this.mDisplayHashController.sendDisplayHashError(remoteCallback, -4);
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    SurfaceControl surfaceControl = displayContent.getSurfaceControl();
                    this.mDisplayHashController.calculateDisplayHashBoundsLocked(windowForClientLocked, rect, rect2);
                    if (rect2.isEmpty()) {
                        Slog.w(TAG, "Failed to generate DisplayHash. Bounds are not on screen");
                        this.mDisplayHashController.sendDisplayHashError(remoteCallback, -4);
                        resetPriorityAfterLockedSection();
                        return;
                    } else {
                        resetPriorityAfterLockedSection();
                        int i = session.mUid;
                        this.mDisplayHashController.generateDisplayHash(new ScreenCapture.LayerCaptureArgs.Builder(surfaceControl).setUid(i).setSourceCrop(rect2), rect, str, i, remoteCallback);
                        return;
                    }
                }
                this.mDisplayHashController.sendDisplayHashError(remoteCallback, -3);
                resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    boolean shouldRestoreImeVisibility(IBinder iBinder) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                WindowState windowState = this.mWindowMap.get(iBinder);
                if (windowState == null) {
                    resetPriorityAfterLockedSection();
                    return false;
                }
                Task task = windowState.getTask();
                if (task == null) {
                    resetPriorityAfterLockedSection();
                    return false;
                }
                ActivityRecord activityRecord = windowState.mActivityRecord;
                if (activityRecord != null && activityRecord.mLastImeShown) {
                    resetPriorityAfterLockedSection();
                    return true;
                }
                resetPriorityAfterLockedSection();
                TaskSnapshot taskSnapshot = getTaskSnapshot(task.mTaskId, task.mUserId, false, false);
                boolean z = taskSnapshot != null && taskSnapshot.hasImeSurface();
                if (ProtoLogGroup.WM_DEBUG_ADD_REMOVE.isLogToLogcat()) {
                    Slog.i(TAG, "shouldRestoreImeVisibility mHasImeSurface:" + z);
                }
                return taskSnapshot != null && taskSnapshot.hasImeSurface();
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public int getImeDisplayId() {
        int displayId;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent topFocusedDisplayContent = this.mRoot.getTopFocusedDisplayContent();
                displayId = topFocusedDisplayContent.getImePolicy() == 0 ? topFocusedDisplayContent.getDisplayId() : 0;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return displayId;
    }

    public void setTaskSnapshotEnabled(boolean z) {
        this.mTaskSnapshotController.setSnapshotEnabled(z);
    }

    public void setTaskTransitionSpec(TaskTransitionSpec taskTransitionSpec) {
        if (!checkCallingPermission("android.permission.MANAGE_ACTIVITY_TASKS", "setTaskTransitionSpec()")) {
            throw new SecurityException("Requires MANAGE_ACTIVITY_TASKS permission");
        }
        this.mTaskTransitionSpec = taskTransitionSpec;
    }

    public void clearTaskTransitionSpec() {
        if (!checkCallingPermission("android.permission.MANAGE_ACTIVITY_TASKS", "clearTaskTransitionSpec()")) {
            throw new SecurityException("Requires MANAGE_ACTIVITY_TASKS permission");
        }
        this.mTaskTransitionSpec = null;
    }

    @RequiresPermission("android.permission.ACCESS_FPS_COUNTER")
    public void registerTaskFpsCallback(int i, ITaskFpsCallback iTaskFpsCallback) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.ACCESS_FPS_COUNTER") != 0) {
            throw new SecurityException("Access denied to process: " + Binder.getCallingPid() + ", must have permission android.permission.ACCESS_FPS_COUNTER");
        }
        if (this.mRoot.anyTaskForId(i) == null) {
            throw new IllegalArgumentException("no task with taskId: " + i);
        }
        this.mTaskFpsCallbackController.registerListener(i, iTaskFpsCallback);
    }

    @RequiresPermission("android.permission.ACCESS_FPS_COUNTER")
    public void unregisterTaskFpsCallback(ITaskFpsCallback iTaskFpsCallback) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.ACCESS_FPS_COUNTER") != 0) {
            throw new SecurityException("Access denied to process: " + Binder.getCallingPid() + ", must have permission android.permission.ACCESS_FPS_COUNTER");
        }
        this.mTaskFpsCallbackController.lambda$registerListener$0(iTaskFpsCallback);
    }

    public Bitmap snapshotTaskForRecents(int i) {
        TaskSnapshot captureSnapshot;
        if (!checkCallingPermission("android.permission.READ_FRAME_BUFFER", "snapshotTaskForRecents()")) {
            throw new SecurityException("Requires READ_FRAME_BUFFER permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    Task anyTaskForId = this.mRoot.anyTaskForId(i, 1);
                    if (anyTaskForId == null) {
                        throw new IllegalArgumentException("Failed to find matching task for taskId=" + i);
                    }
                    captureSnapshot = this.mTaskSnapshotController.captureSnapshot(anyTaskForId, false);
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
            if (captureSnapshot == null || captureSnapshot.getHardwareBuffer() == null) {
                return null;
            }
            return Bitmap.wrapHardwareBuffer(captureSnapshot.getHardwareBuffer(), captureSnapshot.getColorSpace());
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setRecentsAppBehindSystemBars(boolean z) {
        if (!checkCallingPermission("android.permission.START_TASKS_FROM_RECENTS", "setRecentsAppBehindSystemBars()")) {
            throw new SecurityException("Requires START_TASKS_FROM_RECENTS permission");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    Task task = this.mRoot.getTask(new Predicate() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda1
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            boolean lambda$setRecentsAppBehindSystemBars$21;
                            lambda$setRecentsAppBehindSystemBars$21 = WindowManagerService.lambda$setRecentsAppBehindSystemBars$21((Task) obj);
                            return lambda$setRecentsAppBehindSystemBars$21;
                        }
                    });
                    if (task != null) {
                        task.getTask().setCanAffectSystemUiFlags(z);
                        this.mWindowPlacerLocked.requestTraversal();
                    }
                    InputMethodManagerInternal.get().maybeFinishStylusHandwriting();
                } catch (Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$setRecentsAppBehindSystemBars$21(Task task) {
        return task.isActivityTypeHomeOrRecents() && task.getTopVisibleActivity() != null;
    }

    public int getLetterboxBackgroundColorInArgb() {
        return this.mLetterboxConfiguration.getLetterboxBackgroundColor().toArgb();
    }

    public boolean isLetterboxBackgroundMultiColored() {
        int letterboxBackgroundType = this.mLetterboxConfiguration.getLetterboxBackgroundType();
        if (letterboxBackgroundType == 0) {
            return false;
        }
        if (letterboxBackgroundType == 1 || letterboxBackgroundType == 2 || letterboxBackgroundType == 3) {
            return true;
        }
        throw new AssertionError("Unexpected letterbox background type: " + letterboxBackgroundType);
    }

    public void captureDisplay(int i, ScreenCapture.CaptureArgs captureArgs, ScreenCapture.ScreenCaptureListener screenCaptureListener) {
        Slog.d(TAG, "captureDisplay");
        if (!checkCallingPermission("android.permission.READ_FRAME_BUFFER", "captureDisplay()")) {
            throw new SecurityException("Requires READ_FRAME_BUFFER permission");
        }
        ScreenCapture.LayerCaptureArgs captureArgs2 = getCaptureArgs(i, captureArgs);
        ScreenCapture.captureLayers(captureArgs2, screenCaptureListener);
        if (Binder.getCallingUid() != 1000) {
            captureArgs2.release();
        }
    }

    @VisibleForTesting
    ScreenCapture.LayerCaptureArgs getCaptureArgs(int i, ScreenCapture.CaptureArgs captureArgs) {
        SurfaceControl surfaceControl;
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    throw new IllegalArgumentException("Trying to screenshot and invalid display: " + i);
                }
                surfaceControl = displayContent.getSurfaceControl();
                if (captureArgs == null) {
                    captureArgs = new ScreenCapture.CaptureArgs.Builder().build();
                }
                if (captureArgs.mSourceCrop.isEmpty()) {
                    displayContent.getBounds(this.mTmpRect);
                    this.mTmpRect.offsetTo(0, 0);
                } else {
                    this.mTmpRect.set(captureArgs.mSourceCrop);
                }
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return new ScreenCapture.LayerCaptureArgs.Builder(surfaceControl, captureArgs).setSourceCrop(this.mTmpRect).build();
    }

    public boolean isGlobalKey(int i) {
        return this.mPolicy.isGlobalKey(i);
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0019, code lost:
    
        if (r7 != r3.getWindowType()) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int sanitizeWindowType(Session session, int i, IBinder iBinder, int i2) {
        boolean z = true;
        if (i2 == 2032 && iBinder != null) {
            WindowToken windowToken = this.mRoot.getDisplayContent(i).getWindowToken(iBinder);
            if (windowToken != null) {
            }
            z = false;
        } else if (!session.mCanAddInternalSystemWindow && i2 != 0) {
            Slog.w(TAG, "Requires INTERNAL_SYSTEM_WINDOW permission if assign type to input. New type will be 0.");
            z = false;
        }
        if (z) {
            return i2;
        }
        return 0;
    }

    public boolean addToSurfaceSyncGroup(IBinder iBinder, boolean z, ISurfaceSyncGroupCompletedListener iSurfaceSyncGroupCompletedListener, AddToSurfaceSyncGroupResult addToSurfaceSyncGroupResult) {
        return this.mSurfaceSyncGroupController.addToSyncGroup(iBinder, z, iSurfaceSyncGroupCompletedListener, addToSurfaceSyncGroupResult);
    }

    public void markSurfaceSyncGroupReady(IBinder iBinder) {
        this.mSurfaceSyncGroupController.markSyncGroupReady(iBinder);
    }

    public List<ComponentName> notifyScreenshotListeners(int i) {
        if (!checkCallingPermission("android.permission.STATUS_BAR_SERVICE", "notifyScreenshotListeners()")) {
            throw new SecurityException("Requires STATUS_BAR_SERVICE permission");
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mRoot.getDisplayContent(i);
                if (displayContent == null) {
                    ArrayList arrayList = new ArrayList();
                    resetPriorityAfterLockedSection();
                    return arrayList;
                }
                final ArraySet arraySet = new ArraySet();
                displayContent.forAllActivities(new Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda17
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        WindowManagerService.lambda$notifyScreenshotListeners$22(arraySet, (ActivityRecord) obj);
                    }
                }, true);
                List<ComponentName> copyOf = List.copyOf(arraySet);
                resetPriorityAfterLockedSection();
                return copyOf;
            } catch (Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$notifyScreenshotListeners$22(ArraySet arraySet, ActivityRecord activityRecord) {
        if (!arraySet.contains(activityRecord.mActivityComponent) && activityRecord.isVisible() && activityRecord.isRegisteredForScreenCaptureCallback()) {
            activityRecord.reportScreenCaptured();
            arraySet.add(activityRecord.mActivityComponent);
        }
    }

    public IWindowManagerServiceWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class WindowManagerServiceWrapper implements IWindowManagerServiceWrapper {
        private WindowManagerServiceWrapper() {
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public WindowState getFocusedWindow() {
            return WindowManagerService.this.getFocusedWindow();
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public int getForcedDisplayDensityForUserLocked(int i) {
            return WindowManagerService.this.getForcedDisplayDensityForUserLocked(i);
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public void updateAppOpsState() {
            WindowManagerService.this.updateAppOpsState();
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public boolean dumpWindows(PrintWriter printWriter, String str, boolean z) {
            return WindowManagerService.this.dumpWindows(printWriter, str, z);
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public void setWindowAnimationScaleSetting(float f) {
            WindowManagerService.this.mWindowAnimationScaleSetting = f;
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public void setTransitionAnimationScaleSetting(float f) {
            WindowManagerService.this.mTransitionAnimationScaleSetting = f;
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public IWindowManagerServiceExt getExtImpl() {
            return WindowManagerService.this.mWindowManagerServiceExt;
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public void transferTouchFocus(IBinder iBinder, IBinder iBinder2) {
            WindowManagerService.this.mInputManager.transferTouchFocus(iBinder, iBinder2);
        }
    }
}
