package com.android.server.policy;

import android.R;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.ActivityTaskManager;
import android.app.AppOpsManager;
import android.app.IActivityTaskManager;
import android.app.IApplicationThread;
import android.app.IUiModeManager;
import android.app.NotificationManager;
import android.app.ProfilerInfo;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.hardware.SensorPrivacyManager;
import android.hardware.devicestate.DeviceStateManagerInternal;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManagerInternal;
import android.hardware.hdmi.HdmiAudioSystemClient;
import android.hardware.hdmi.HdmiControlManager;
import android.hardware.hdmi.HdmiPlaybackClient;
import android.media.AudioManagerInternal;
import android.media.AudioSystem;
import android.media.IAudioService;
import android.media.session.MediaSessionLegacyHelper;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.DeviceIdleManager;
import android.os.FactoryTest;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManagerInternal;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UEventObserver;
import android.os.UserHandle;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.service.dreams.DreamManagerInternal;
import android.service.dreams.IDreamManager;
import android.service.vr.IPersistentVrStateCallbacks;
import android.telecom.TelecomManager;
import android.util.FeatureFlagUtils;
import android.util.Log;
import android.util.MathUtils;
import android.util.MutableBoolean;
import android.util.PrintWriterPrinter;
import android.util.Slog;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import android.view.Display;
import android.view.IDisplayFoldListener;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManagerPolicyConstants;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.autofill.AutofillManagerInternal;
import android.widget.Toast;
import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.internal.accessibility.util.AccessibilityUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.os.RoSystemProperties;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.policy.IShortcutService;
import com.android.internal.policy.KeyInterceptionInfo;
import com.android.internal.policy.LogDecelerateInterpolator;
import com.android.internal.policy.PhoneWindow;
import com.android.internal.policy.TransitionAnimation;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.ArrayUtils;
import com.android.internal.widget.LockPatternUtils;
import com.android.server.AccessibilityManagerInternal;
import com.android.server.ExtconStateObserver;
import com.android.server.ExtconUEventObserver;
import com.android.server.GestureLauncherService;
import com.android.server.LocalServices;
import com.android.server.SystemServiceManager;
import com.android.server.UiThread;
import com.android.server.display.BrightnessUtils;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.input.InputManagerInternal;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.location.interfaces.IOplusLocationStatistics;
import com.android.server.pm.UserManagerInternal;
import com.android.server.policy.KeyCombinationManager;
import com.android.server.policy.PhoneWindowManager;
import com.android.server.policy.SingleKeyGestureDetector;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.policy.keyguard.KeyguardServiceDelegate;
import com.android.server.policy.keyguard.KeyguardStateMonitor;
import com.android.server.statusbar.StatusBarManagerInternal;
import com.android.server.usb.descriptors.UsbACInterface;
import com.android.server.usb.descriptors.UsbDescriptor;
import com.android.server.vr.VrManagerInternal;
import com.android.server.wallpaper.WallpaperManagerInternal;
import com.android.server.wm.ActivityTaskManagerInternal;
import com.android.server.wm.DisplayPolicy;
import com.android.server.wm.DisplayRotation;
import com.android.server.wm.WindowManagerInternal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PhoneWindowManager implements WindowManagerPolicy {
    private static final String ACTION_VOICE_ASSIST_RETAIL = "android.intent.action.VOICE_ASSIST_RETAIL";
    private static final int BRIGHTNESS_STEPS = 10;
    private static final long BUGREPORT_TV_GESTURE_TIMEOUT_MILLIS = 1000;
    static boolean DEBUG_INPUT = false;
    static boolean DEBUG_KEYGUARD = false;
    static boolean DEBUG_WAKEUP = false;
    static final int DOUBLE_PRESS_PRIMARY_NOTHING = 0;
    static final int DOUBLE_PRESS_PRIMARY_SWITCH_RECENT_APP = 1;
    static final int DOUBLE_TAP_HOME_NOTHING = 0;
    static final int DOUBLE_TAP_HOME_PIP_MENU = 2;
    static final int DOUBLE_TAP_HOME_RECENT_SYSTEM_UI = 1;
    static final boolean ENABLE_DESK_DOCK_HOME_CAPTURE = false;
    static final boolean ENABLE_VR_HEADSET_HOME_CAPTURE = true;
    private static final float KEYGUARD_SCREENSHOT_CHORD_DELAY_MULTIPLIER = 2.5f;
    static final int LAST_LONG_PRESS_HOME_BEHAVIOR = 3;
    static final int LONG_PRESS_BACK_GO_TO_VOICE_ASSIST = 1;
    static final int LONG_PRESS_BACK_NOTHING = 0;
    static final int LONG_PRESS_HOME_ALL_APPS = 1;
    static final int LONG_PRESS_HOME_ASSIST = 2;
    static final int LONG_PRESS_HOME_NOTHING = 0;
    static final int LONG_PRESS_HOME_NOTIFICATION_PANEL = 3;
    static final int LONG_PRESS_POWER_ASSISTANT = 5;
    static final int LONG_PRESS_POWER_GLOBAL_ACTIONS = 1;
    static final int LONG_PRESS_POWER_GO_TO_VOICE_ASSIST = 4;
    static final int LONG_PRESS_POWER_NOTHING = 0;
    static final int LONG_PRESS_POWER_SHUT_OFF = 2;
    static final int LONG_PRESS_POWER_SHUT_OFF_NO_CONFIRM = 3;
    static final int LONG_PRESS_PRIMARY_LAUNCH_VOICE_ASSISTANT = 1;
    static final int LONG_PRESS_PRIMARY_NOTHING = 0;
    private static final int MSG_ACCESSIBILITY_SHORTCUT = 17;
    private static final int MSG_ACCESSIBILITY_TV = 19;
    private static final int MSG_BUGREPORT_TV = 18;
    private static final int MSG_DISPATCH_BACK_KEY_TO_AUTOFILL = 20;
    private static final int MSG_DISPATCH_MEDIA_KEY_REPEAT_WITH_WAKE_LOCK = 4;
    private static final int MSG_DISPATCH_MEDIA_KEY_WITH_WAKE_LOCK = 3;
    private static final int MSG_DISPATCH_SHOW_GLOBAL_ACTIONS = 10;
    private static final int MSG_DISPATCH_SHOW_RECENTS = 9;
    private static final int MSG_HANDLE_ALL_APPS = 22;
    private static final int MSG_HIDE_BOOT_MESSAGE = 11;
    private static final int MSG_KEYGUARD_DRAWN_COMPLETE = 5;
    private static final int MSG_KEYGUARD_DRAWN_TIMEOUT = 6;
    private static final int MSG_LAUNCH_ASSIST = 23;
    private static final int MSG_LAUNCH_VOICE_ASSIST_WITH_WAKE_LOCK = 12;
    private static final int MSG_RINGER_TOGGLE_CHORD = 24;
    private static final int MSG_SCREENSHOT_CHORD = 16;
    private static final int MSG_SHOW_PICTURE_IN_PICTURE_MENU = 15;
    private static final int MSG_SWITCH_KEYBOARD_LAYOUT = 25;
    private static final int MSG_SYSTEM_KEY_PRESS = 21;
    private static final int MSG_WINDOW_MANAGER_DRAWN_COMPLETE = 7;
    static final int MULTI_PRESS_POWER_BRIGHTNESS_BOOST = 2;
    static final int MULTI_PRESS_POWER_LAUNCH_TARGET_ACTIVITY = 3;
    static final int MULTI_PRESS_POWER_NOTHING = 0;
    static final int MULTI_PRESS_POWER_THEATER_MODE = 1;
    static final int PENDING_KEY_NULL = -1;
    private static final int POWER_BUTTON_SUPPRESSION_DELAY_DEFAULT_MILLIS = 800;
    static final int POWER_VOLUME_UP_BEHAVIOR_GLOBAL_ACTIONS = 2;
    static final int POWER_VOLUME_UP_BEHAVIOR_MUTE = 1;
    static final int POWER_VOLUME_UP_BEHAVIOR_NOTHING = 0;
    static final int SEARCH_BEHAVIOR_DEFAULT_SEARCH = 0;
    static final int SEARCH_BEHAVIOR_TARGET_ACTIVITY = 1;
    static final int SHORT_PRESS_POWER_CLOSE_IME_OR_GO_HOME = 5;
    static final int SHORT_PRESS_POWER_DREAM_OR_SLEEP = 7;
    static final int SHORT_PRESS_POWER_GO_HOME = 4;
    static final int SHORT_PRESS_POWER_GO_TO_SLEEP = 1;
    static final int SHORT_PRESS_POWER_LOCK_OR_SLEEP = 6;
    static final int SHORT_PRESS_POWER_NOTHING = 0;
    static final int SHORT_PRESS_POWER_REALLY_GO_TO_SLEEP = 2;
    static final int SHORT_PRESS_POWER_REALLY_GO_TO_SLEEP_AND_GO_HOME = 3;
    static final int SHORT_PRESS_PRIMARY_LAUNCH_ALL_APPS = 1;
    static final int SHORT_PRESS_PRIMARY_NOTHING = 0;
    static final int SHORT_PRESS_SLEEP_GO_TO_SLEEP = 0;
    static final int SHORT_PRESS_SLEEP_GO_TO_SLEEP_AND_GO_HOME = 1;
    static final int SHORT_PRESS_WINDOW_NOTHING = 0;
    static final int SHORT_PRESS_WINDOW_PICTURE_IN_PICTURE = 1;
    static final boolean SHOW_SPLASH_SCREENS = true;
    public static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";
    public static final String SYSTEM_DIALOG_REASON_GESTURE_NAV = "gestureNav";
    public static final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
    public static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    public static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    public static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    public static final String SYSTEM_DIALOG_REASON_SCREENSHOT = "screenshot";
    static final String TAG = "WindowManager";
    private static final String TALKBACK_LABEL = "TalkBack";
    public static final int TOAST_WINDOW_ANIM_BUFFER = 600;
    public static final int TOAST_WINDOW_TIMEOUT = 4100;
    public static final String TRACE_WAIT_FOR_ALL_WINDOWS_DRAWN_METHOD = "waitForAllWindowsDrawn";
    static final int TRIPLE_PRESS_PRIMARY_NOTHING = 0;
    static final int TRIPLE_PRESS_PRIMARY_TOGGLE_ACCESSIBILITY = 1;
    static final int VERY_LONG_PRESS_POWER_GLOBAL_ACTIONS = 1;
    static final int VERY_LONG_PRESS_POWER_NOTHING = 0;
    static final int WAITING_FOR_DRAWN_TIMEOUT = 1000;
    static boolean localLOGV = false;
    AccessibilityManager mAccessibilityManager;
    AccessibilityManagerInternal mAccessibilityManagerInternal;
    private AccessibilityShortcutController mAccessibilityShortcutController;
    ActivityManagerInternal mActivityManagerInternal;
    ActivityTaskManagerInternal mActivityTaskManagerInternal;
    boolean mAllowStartActivityForLongPressOnPowerDuringSetup;
    private boolean mAllowTheaterModeWakeFromCameraLens;
    private boolean mAllowTheaterModeWakeFromKey;
    private boolean mAllowTheaterModeWakeFromLidSwitch;
    private boolean mAllowTheaterModeWakeFromMotion;
    private boolean mAllowTheaterModeWakeFromMotionWhenNotDreaming;
    private boolean mAllowTheaterModeWakeFromPowerKey;
    private boolean mAllowTheaterModeWakeFromWakeGesture;
    AppOpsManager mAppOpsManager;
    AudioManagerInternal mAudioManagerInternal;
    AutofillManagerInternal mAutofillManagerInternal;
    volatile boolean mBackKeyHandled;
    volatile boolean mBootAnimationDismissable;
    boolean mBootMessageNeedsHiding;
    PowerManager.WakeLock mBroadcastWakeLock;
    BurnInProtectionHelper mBurnInProtectionHelper;
    volatile boolean mCameraGestureTriggered;
    volatile boolean mCameraGestureTriggeredDuringGoingToSleep;
    Intent mCarDockIntent;
    Context mContext;
    private int mCurrentUserId;
    Display mDefaultDisplay;
    DisplayPolicy mDefaultDisplayPolicy;
    DisplayRotation mDefaultDisplayRotation;
    Intent mDeskDockIntent;
    volatile boolean mDeviceGoingToSleep;
    private volatile boolean mDismissImeOnBackKeyPressed;
    private DisplayFoldController mDisplayFoldController;
    DisplayManager mDisplayManager;
    DisplayManagerInternal mDisplayManagerInternal;
    int mDoublePressOnPowerBehavior;
    int mDoublePressOnStemPrimaryBehavior;
    private int mDoubleTapOnHomeBehavior;
    DreamManagerInternal mDreamManagerInternal;
    volatile boolean mEndCallKeyHandled;
    int mEndcallBehavior;
    private GestureLauncherService mGestureLauncherService;
    private GlobalActions mGlobalActions;
    private Supplier<GlobalActions> mGlobalActionsFactory;
    private GlobalKeyManager mGlobalKeyManager;
    private boolean mGoToSleepOnButtonPressTheaterMode;
    private boolean mHandleVolumeKeysInWM;
    public Handler mHandler;
    boolean mHapticTextHandleEnabled;
    private boolean mHasFeatureAuto;
    private boolean mHasFeatureHdmiCec;
    private boolean mHasFeatureLeanback;
    private boolean mHasFeatureWatch;
    boolean mHaveBuiltInKeyboard;
    boolean mHavePendingMediaKeyRepeatWithWakeLock;
    HdmiControl mHdmiControl;
    Intent mHomeIntent;
    int mIncallBackBehavior;
    int mIncallPowerBehavior;
    InputManagerInternal mInputManagerInternal;
    private KeyCombinationManager mKeyCombinationManager;
    private boolean mKeyguardBound;
    public KeyguardServiceDelegate mKeyguardDelegate;
    private boolean mKeyguardDrawnOnce;
    private boolean mKeyguardOccludedChanged;
    int mLidKeyboardAccessibility;
    int mLidNavigationAccessibility;
    boolean mLockAfterAppTransitionFinished;
    LockPatternUtils mLockPatternUtils;
    int mLockScreenTimeout;
    boolean mLockScreenTimerActive;
    MetricsLogger mLogger;
    int mLongPressOnBackBehavior;
    private int mLongPressOnHomeBehavior;
    long mLongPressOnPowerAssistantTimeoutMs;
    int mLongPressOnPowerBehavior;
    int mLongPressOnStemPrimaryBehavior;
    ModifierShortcutManager mModifierShortcutManager;
    PackageManager mPackageManager;
    boolean mPendingCapsLockToggle;
    private boolean mPendingKeyguardOccluded;
    boolean mPendingMetaAction;
    volatile boolean mPictureInPictureVisible;
    ComponentName mPowerDoublePressTargetActivity;
    volatile boolean mPowerKeyHandled;
    PowerManager.WakeLock mPowerKeyWakeLock;
    PowerManager mPowerManager;
    PowerManagerInternal mPowerManagerInternal;
    int mPowerVolUpBehavior;
    boolean mPreloadedRecentApps;
    int mRecentAppsHeldModifiers;
    volatile boolean mRecentsVisible;
    volatile boolean mRequestedOrSleepingDefaultDisplay;
    boolean mSafeMode;
    long[] mSafeModeEnabledVibePattern;
    private ActivityTaskManagerInternal.SleepTokenAcquirer mScreenOffSleepTokenAcquirer;
    int mSearchKeyBehavior;
    ComponentName mSearchKeyTargetActivity;
    SearchManager mSearchManager;
    SensorPrivacyManager mSensorPrivacyManager;
    SettingsObserver mSettingsObserver;
    int mShortPressOnPowerBehavior;
    int mShortPressOnSleepBehavior;
    int mShortPressOnStemPrimaryBehavior;
    int mShortPressOnWindowBehavior;
    SideFpsEventHandler mSideFpsEventHandler;
    private SingleKeyGestureDetector mSingleKeyGestureDetector;
    StatusBarManagerInternal mStatusBarManagerInternal;
    IStatusBarService mStatusBarService;
    private boolean mSupportLongPressPowerWhenNonInteractive;
    boolean mSystemBooted;
    boolean mSystemNavigationKeysEnabled;
    boolean mSystemReady;
    int mTriplePressOnPowerBehavior;
    int mTriplePressOnStemPrimaryBehavior;
    int mUiMode;
    IUiModeManager mUiModeManager;
    boolean mUseTvRouting;
    UserManagerInternal mUserManagerInternal;
    int mVeryLongPressOnPowerBehavior;
    Vibrator mVibrator;
    Intent mVrHeadsetHomeIntent;
    volatile VrManagerInternal mVrManagerInternal;
    boolean mWakeGestureEnabledSetting;
    MyWakeGestureListener mWakeGestureListener;
    boolean mWakeOnAssistKeyPress;
    boolean mWakeOnBackKeyPress;
    boolean mWakeOnDpadKeyPress;
    long mWakeUpToLastStateTimeout;
    private WallpaperManagerInternal mWallpaperManagerInternal;
    WindowManagerPolicy.WindowManagerFuncs mWindowManagerFuncs;
    WindowManagerInternal mWindowManagerInternal;
    private static final VibrationAttributes TOUCH_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(18);
    private static final VibrationAttributes PHYSICAL_EMULATION_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(34);
    private static final VibrationAttributes HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(50);
    private static boolean LTW_DISABLE = SystemProperties.getBoolean("persist.sys.ltw.disable", false);
    private static final int[] WINDOW_TYPES_WHERE_HOME_DOESNT_WORK = {2003, 2010};
    private final Object mLock = new Object();
    private final SparseArray<WindowManagerPolicy.ScreenOnListener> mScreenOnListeners = new SparseArray<>();
    final Object mServiceAcquireLock = new Object();
    boolean mEnableShiftMenuBugReports = false;
    private boolean mEnableCarDockHomeCapture = true;
    final KeyguardServiceDelegate.DrawnListener mKeyguardDrawnCallback = new KeyguardServiceDelegate.DrawnListener() { // from class: com.android.server.policy.PhoneWindowManager.1
        @Override // com.android.server.policy.keyguard.KeyguardServiceDelegate.DrawnListener
        public void onDrawn() {
            Slog.d(PhoneWindowManager.TAG, "mKeyguardDelegate.ShowListener.onDrawn.");
            PhoneWindowManager.this.mHandler.sendEmptyMessage(5);
        }
    };
    volatile boolean mNavBarVirtualKeyHapticFeedbackEnabled = true;
    volatile int mPendingWakeKey = -1;
    int mCameraLensCoverState = -1;
    boolean mStylusButtonsEnabled = true;
    boolean mHasSoftInput = false;
    private HashSet<Integer> mAllowLockscreenWhenOnDisplays = new HashSet<>();
    int mRingerToggleChord = 0;
    private final SparseArray<KeyCharacterMap.FallbackAction> mFallbackActions = new SparseArray<>();
    private final LogDecelerateInterpolator mLogDecelerateInterpolator = new LogDecelerateInterpolator(100, 0);
    private volatile int mTopFocusedDisplayId = -1;
    private int mPowerButtonSuppressionDelayMillis = 800;
    private boolean mLockNowPending = false;
    private int mKeyguardDrawnTimeout = 1000;
    private UEventObserver mHDMIObserver = new UEventObserver() { // from class: com.android.server.policy.PhoneWindowManager.2
        public void onUEvent(UEventObserver.UEvent uEvent) {
            PhoneWindowManager.this.mDefaultDisplayPolicy.setHdmiPlugged("1".equals(uEvent.get("SWITCH_STATE")));
        }
    };
    final IPersistentVrStateCallbacks mPersistentVrModeListener = new IPersistentVrStateCallbacks.Stub() { // from class: com.android.server.policy.PhoneWindowManager.3
        public void onPersistentVrStateChanged(boolean z) {
            PhoneWindowManager.this.mDefaultDisplayPolicy.setPersistentVrModeEnabled(z);
        }
    };
    public IPhoneWindowManagerSocExt mPhoneWindowManagerSocExt = (IPhoneWindowManagerSocExt) ExtLoader.type(IPhoneWindowManagerSocExt.class).base(this).create();
    private final Runnable mEndCallLongPress = new Runnable() { // from class: com.android.server.policy.PhoneWindowManager.4
        @Override // java.lang.Runnable
        public void run() {
            PhoneWindowManager.this.mEndCallKeyHandled = true;
            PhoneWindowManager.this.performHapticFeedback(0, false, "End Call - Long Press - Show Global Actions");
            PhoneWindowManager.this.showGlobalActionsInternal();
        }
    };
    private final SparseArray<DisplayHomeButtonHandler> mDisplayHomeButtonHandlers = new SparseArray<>();
    BroadcastReceiver mDockReceiver = new BroadcastReceiver() { // from class: com.android.server.policy.PhoneWindowManager.14
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.DOCK_EVENT".equals(intent.getAction())) {
                PhoneWindowManager.this.mDefaultDisplayPolicy.setDockMode(intent.getIntExtra("android.intent.extra.DOCK_STATE", 0));
            } else {
                try {
                    IUiModeManager asInterface = IUiModeManager.Stub.asInterface(ServiceManager.getService("uimode"));
                    PhoneWindowManager.this.mUiMode = asInterface.getCurrentModeType();
                } catch (RemoteException unused) {
                }
            }
            PhoneWindowManager.this.updateRotation(true);
            PhoneWindowManager.this.mDefaultDisplayRotation.updateOrientationListener();
        }
    };
    BroadcastReceiver mMultiuserReceiver = new BroadcastReceiver() { // from class: com.android.server.policy.PhoneWindowManager.15
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                PhoneWindowManager.this.mSettingsObserver.onChange(false);
                PhoneWindowManager.this.mDefaultDisplayRotation.onUserSwitch();
                PhoneWindowManager.this.mWindowManagerFuncs.onUserSwitched();
            }
        }
    };
    ProgressDialog mBootMsgDialog = null;
    final ScreenLockTimeout mScreenLockTimeout = new ScreenLockTimeout();
    private IPhoneWindowManagerWrapper mWrapper = new PhoneWindowManagerWrapper();
    protected IPhoneWindowManagerExt mPhoneWindowManagerExt = (IPhoneWindowManagerExt) ExtLoader.type(IPhoneWindowManagerExt.class).base(this).create();

    private static String incallBackBehaviorToString(int i) {
        return (i & 1) != 0 ? "hangup" : "<nothing>";
    }

    private static String incallPowerBehaviorToString(int i) {
        return (i & 2) != 0 ? "hangup" : "sleep";
    }

    private static boolean isValidGlobalKey(int i) {
        return (i == 26 || i == 223 || i == 224) ? false : true;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void finishedWakingUpGlobal(int i) {
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void keepScreenOnStartedLw() {
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void startedWakingUpGlobal(int i) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class PolicyHandler extends Handler {
        private PolicyHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 3:
                    PhoneWindowManager.this.dispatchMediaKeyWithWakeLock((KeyEvent) message.obj);
                    return;
                case 4:
                    PhoneWindowManager.this.dispatchMediaKeyRepeatWithWakeLock((KeyEvent) message.obj);
                    return;
                case 5:
                    Slog.w(PhoneWindowManager.TAG, "Setting mKeyguardDrawComplete");
                    PhoneWindowManager.this.finishKeyguardDrawn();
                    return;
                case 6:
                    Slog.w(PhoneWindowManager.TAG, "Keyguard drawn timeout. Setting mKeyguardDrawComplete");
                    PhoneWindowManager.this.finishKeyguardDrawn();
                    return;
                case 7:
                    int i = message.arg1;
                    Slog.w(PhoneWindowManager.TAG, "All windows drawn on display " + i);
                    Trace.asyncTraceEnd(32L, PhoneWindowManager.TRACE_WAIT_FOR_ALL_WINDOWS_DRAWN_METHOD, i);
                    PhoneWindowManager.this.finishWindowsDrawn(i);
                    return;
                case 8:
                case 13:
                case 14:
                default:
                    return;
                case 9:
                    PhoneWindowManager.this.showRecentApps(false);
                    return;
                case 10:
                    PhoneWindowManager.this.showGlobalActionsInternal();
                    return;
                case 11:
                    PhoneWindowManager.this.handleHideBootMessage();
                    return;
                case 12:
                    PhoneWindowManager.this.launchVoiceAssistWithWakeLock();
                    return;
                case 15:
                    PhoneWindowManager.this.showPictureInPictureMenuInternal();
                    return;
                case 16:
                    PhoneWindowManager.this.handleScreenShot(message.arg1);
                    return;
                case 17:
                    PhoneWindowManager.this.accessibilityShortcutActivated();
                    return;
                case 18:
                    PhoneWindowManager.this.requestBugreportForTv();
                    return;
                case 19:
                    if (PhoneWindowManager.this.mAccessibilityShortcutController.isAccessibilityShortcutAvailable(false)) {
                        PhoneWindowManager.this.accessibilityShortcutActivated();
                        return;
                    }
                    return;
                case 20:
                    PhoneWindowManager.this.mAutofillManagerInternal.onBackKeyPressed();
                    return;
                case PhoneWindowManager.MSG_SYSTEM_KEY_PRESS /* 21 */:
                    PhoneWindowManager.this.sendSystemKeyToStatusBar((KeyEvent) message.obj);
                    return;
                case PhoneWindowManager.MSG_HANDLE_ALL_APPS /* 22 */:
                    PhoneWindowManager.this.launchAllAppsAction();
                    return;
                case 23:
                    PhoneWindowManager.this.launchAssistAction(null, message.arg1, ((Long) message.obj).longValue(), 7, 0);
                    return;
                case PhoneWindowManager.MSG_RINGER_TOGGLE_CHORD /* 24 */:
                    if (PhoneWindowManager.this.mPhoneWindowManagerExt.getInputExtension().interceptRingerChordGesture()) {
                        return;
                    }
                    PhoneWindowManager.this.handleRingerChordGesture();
                    return;
                case PhoneWindowManager.MSG_SWITCH_KEYBOARD_LAYOUT /* 25 */:
                    PhoneWindowManager.this.handleSwitchKeyboardLayout(message.arg1, message.arg2);
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            ContentResolver contentResolver = PhoneWindowManager.this.mContext.getContentResolver();
            contentResolver.registerContentObserver(Settings.System.getUriFor("end_button_behavior"), false, this, -1);
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("incall_power_button_behavior"), false, this, -1);
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("incall_back_button_behavior"), false, this, -1);
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("wake_gesture_enabled"), false, this, -1);
            contentResolver.registerContentObserver(Settings.System.getUriFor("screen_off_timeout"), false, this, -1);
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("default_input_method"), false, this, -1);
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("volume_hush_gesture"), false, this, -1);
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("system_navigation_keys_enabled"), false, this, -1);
            contentResolver.registerContentObserver(Settings.Global.getUriFor("power_button_long_press"), false, this, -1);
            contentResolver.registerContentObserver(Settings.Global.getUriFor("power_button_long_press_duration_ms"), false, this, -1);
            contentResolver.registerContentObserver(Settings.Global.getUriFor("power_button_very_long_press"), false, this, -1);
            contentResolver.registerContentObserver(Settings.Global.getUriFor("key_chord_power_volume_up"), false, this, -1);
            contentResolver.registerContentObserver(Settings.Global.getUriFor("power_button_suppression_delay_after_gesture_wake"), false, this, -1);
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("stylus_buttons_enabled"), false, this, -1);
            PhoneWindowManager.this.updateSettings();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            PhoneWindowManager.this.updateSettings();
            PhoneWindowManager.this.updateRotation(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class MyWakeGestureListener extends WakeGestureListener {
        MyWakeGestureListener(Context context, Handler handler) {
            super(context, handler);
        }

        @Override // com.android.server.policy.WakeGestureListener
        public void onWakeUp() {
            synchronized (PhoneWindowManager.this.mLock) {
                if (PhoneWindowManager.this.shouldEnableWakeGestureLp()) {
                    PhoneWindowManager.this.performHapticFeedback(1, false, "Wake Up");
                    PhoneWindowManager.this.wakeUp(SystemClock.uptimeMillis(), PhoneWindowManager.this.mAllowTheaterModeWakeFromWakeGesture, 4, "android.policy:GESTURE");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRingerChordGesture() {
        if (this.mRingerToggleChord == 0) {
            return;
        }
        getAudioManagerInternal();
        this.mAudioManagerInternal.silenceRingerModeInternal("volume_hush");
        Settings.Secure.putInt(this.mContext.getContentResolver(), "hush_gesture_used", 1);
        this.mLogger.action(1440, this.mRingerToggleChord);
    }

    IStatusBarService getStatusBarService() {
        IStatusBarService iStatusBarService;
        synchronized (this.mServiceAcquireLock) {
            if (this.mStatusBarService == null) {
                this.mStatusBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
            }
            iStatusBarService = this.mStatusBarService;
        }
        return iStatusBarService;
    }

    StatusBarManagerInternal getStatusBarManagerInternal() {
        StatusBarManagerInternal statusBarManagerInternal;
        synchronized (this.mServiceAcquireLock) {
            if (this.mStatusBarManagerInternal == null) {
                this.mStatusBarManagerInternal = (StatusBarManagerInternal) LocalServices.getService(StatusBarManagerInternal.class);
            }
            statusBarManagerInternal = this.mStatusBarManagerInternal;
        }
        return statusBarManagerInternal;
    }

    AudioManagerInternal getAudioManagerInternal() {
        AudioManagerInternal audioManagerInternal;
        synchronized (this.mServiceAcquireLock) {
            if (this.mAudioManagerInternal == null) {
                this.mAudioManagerInternal = (AudioManagerInternal) LocalServices.getService(AudioManagerInternal.class);
            }
            audioManagerInternal = this.mAudioManagerInternal;
        }
        return audioManagerInternal;
    }

    AccessibilityManagerInternal getAccessibilityManagerInternal() {
        AccessibilityManagerInternal accessibilityManagerInternal;
        synchronized (this.mServiceAcquireLock) {
            if (this.mAccessibilityManagerInternal == null) {
                this.mAccessibilityManagerInternal = (AccessibilityManagerInternal) LocalServices.getService(AccessibilityManagerInternal.class);
            }
            accessibilityManagerInternal = this.mAccessibilityManagerInternal;
        }
        return accessibilityManagerInternal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean backKeyPress() {
        TelecomManager telecommService;
        this.mLogger.count("key_back_press", 1);
        boolean z = this.mBackKeyHandled;
        if (this.mHasFeatureWatch && (telecommService = getTelecommService()) != null) {
            if (telecommService.isRinging()) {
                telecommService.silenceRinger();
                return false;
            }
            if ((1 & this.mIncallBackBehavior) != 0 && telecommService.isInCall()) {
                return telecommService.endCall();
            }
        }
        if (this.mAutofillManagerInternal != null) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(20));
        }
        return z;
    }

    private void interceptPowerKeyDown(KeyEvent keyEvent, boolean z) {
        if (!this.mPowerKeyWakeLock.isHeld()) {
            this.mPowerKeyWakeLock.acquire();
            this.mPhoneWindowManagerExt.hookForInputLogV("interceptPowerKeyDown mPowerKeyWakeLock acquired");
        }
        this.mWindowManagerFuncs.onPowerKeyDown(z);
        this.mPhoneWindowManagerExt.onPwkPressed();
        getTelecommService();
        boolean interceptPowerKeyForTelephone = this.mPhoneWindowManagerExt.getInputExtension().interceptPowerKeyForTelephone(keyEvent, z);
        this.mPhoneWindowManagerExt.getInputExtension().interceptPowerKeyForAlarm();
        boolean interceptPowerKeyDown = this.mPowerManagerInternal.interceptPowerKeyDown(keyEvent);
        sendSystemKeyToStatusBarAsync(keyEvent);
        boolean z2 = false;
        this.mPowerKeyHandled = this.mPowerKeyHandled || interceptPowerKeyForTelephone || interceptPowerKeyDown || this.mKeyCombinationManager.isPowerKeyIntercepted();
        this.mPhoneWindowManagerExt.getInputExtension().interceptPowerKeyDown(keyEvent, z);
        if (!z && !this.mPhoneWindowManagerExt.interceptPowerKeyDown()) {
            z2 = true;
        }
        if (this.mPowerKeyHandled) {
            this.mPhoneWindowManagerExt.notePowerkeyProcessEvent("interceptPowerKeyDown keyhandled", true, true);
            if (this.mSingleKeyGestureDetector.isKeyIntercepted(26)) {
                Slog.d(TAG, "Skip power key gesture for other policy has handled it.");
                this.mSingleKeyGestureDetector.reset();
            }
        } else if (!z && z2) {
            wakeUpFromPowerKey(keyEvent.getDownTime());
        }
        this.mPhoneWindowManagerExt.startHwShutdownDectect();
    }

    private void interceptPowerKeyUp(KeyEvent keyEvent, boolean z) {
        boolean z2 = z || this.mPowerKeyHandled || this.mPhoneWindowManagerExt.getInputExtension().getSpeechLongPressHandle();
        this.mPhoneWindowManagerExt.getInputExtension().interceptPowerKeyUp(z2);
        this.mPhoneWindowManagerExt.clearHwShutdownDectect();
        this.mPhoneWindowManagerExt.onPwkReleased();
        this.mPhoneWindowManagerExt.interceptPowerKeyUp(z2, this.mWindowManagerFuncs);
        if (!z2 && (keyEvent.getFlags() & 128) == 0) {
            Handler handler = this.mHandler;
            final WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs = this.mWindowManagerFuncs;
            Objects.requireNonNull(windowManagerFuncs);
            handler.post(new Runnable() { // from class: com.android.server.policy.PhoneWindowManager$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    WindowManagerPolicy.WindowManagerFuncs.this.triggerAnimationFailsafe();
                }
            });
        }
        finishPowerKeyPress();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishPowerKeyPress() {
        this.mPowerKeyHandled = false;
        if (this.mPowerKeyWakeLock.isHeld()) {
            this.mPowerKeyWakeLock.release();
            this.mPhoneWindowManagerExt.hookForInputLogV("interceptPowerKeyDown mPowerKeyWakeLock released");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void powerPress(final long j, int i, boolean z) {
        if (i == 1) {
            this.mSideFpsEventHandler.notifyPowerPressed();
        }
        if (this.mDefaultDisplayPolicy.isScreenOnEarly() && !this.mDefaultDisplayPolicy.isScreenOnFully()) {
            Slog.i(TAG, "Suppressed redundant power key press while already in the process of turning the screen on.");
            this.mPhoneWindowManagerExt.notePowerkeyProcessEvent("Screen turning on ignore powerpress", false, true);
            return;
        }
        boolean isDisplaysOnLocked = this.mPhoneWindowManagerExt.isDisplaysOnLocked(this.mDefaultDisplay);
        Slog.d(TAG, "powerPress: eventTime=" + j + " interactive=" + isDisplaysOnLocked + " count=" + i + " beganFromNonInteractive=" + z + " mShortPressOnPowerBehavior=" + this.mShortPressOnPowerBehavior);
        if (i == 2) {
            powerMultiPressAction(j, isDisplaysOnLocked, this.mDoublePressOnPowerBehavior);
            return;
        }
        if (i == 3) {
            powerMultiPressAction(j, isDisplaysOnLocked, this.mTriplePressOnPowerBehavior);
            return;
        }
        if (i > 3 && i <= getMaxMultiPressPowerCount()) {
            Slog.d(TAG, "No behavior defined for power press count " + i);
            return;
        }
        if (i == 1 && isDisplaysOnLocked && !z) {
            if (this.mSideFpsEventHandler.shouldConsumeSinglePress(j)) {
                Slog.i(TAG, "Suppressing power key because the user is interacting with the fingerprint sensor");
                return;
            }
            if (!LTW_DISABLE && this.mPhoneWindowManagerExt.getBlackScreenWindowManagerPowerKeyState()) {
                Slog.d(TAG, "intercept power key down event success!");
                return;
            }
            switch (this.mShortPressOnPowerBehavior) {
                case 1:
                    sleepDefaultDisplayFromPowerButton(j, 0);
                    return;
                case 2:
                    sleepDefaultDisplayFromPowerButton(j, 1);
                    return;
                case 3:
                    if (sleepDefaultDisplayFromPowerButton(j, 1)) {
                        launchHomeFromHotKey(0);
                        return;
                    }
                    return;
                case 4:
                    shortPressPowerGoHome();
                    return;
                case 5:
                    if (this.mDismissImeOnBackKeyPressed) {
                        InputMethodManagerInternal.get().hideCurrentInputMethod(17);
                        return;
                    } else {
                        shortPressPowerGoHome();
                        return;
                    }
                case 6:
                    KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
                    if (keyguardServiceDelegate == null || !keyguardServiceDelegate.hasKeyguard() || !this.mKeyguardDelegate.isSecure(this.mCurrentUserId) || keyguardOn()) {
                        sleepDefaultDisplayFromPowerButton(j, 0);
                        return;
                    } else {
                        lockNow(null);
                        return;
                    }
                case 7:
                    attemptToDreamFromShortPowerButtonPress(true, new Runnable() { // from class: com.android.server.policy.PhoneWindowManager$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            PhoneWindowManager.this.lambda$powerPress$0(j);
                        }
                    });
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$powerPress$0(long j) {
        sleepDefaultDisplayFromPowerButton(j, 0);
    }

    private void attemptToDreamFromShortPowerButtonPress(boolean z, Runnable runnable) {
        if (this.mShortPressOnPowerBehavior != 7) {
            runnable.run();
            return;
        }
        DreamManagerInternal dreamManagerInternal = getDreamManagerInternal();
        if (dreamManagerInternal == null || !dreamManagerInternal.canStartDreaming(z)) {
            Slog.d(TAG, "Can't start dreaming when attempting to dream from short power press (isScreenOn=" + z + ")");
            runnable.run();
            return;
        }
        synchronized (this.mLock) {
            this.mLockAfterAppTransitionFinished = this.mLockPatternUtils.getPowerButtonInstantlyLocks(this.mCurrentUserId);
        }
        dreamManagerInternal.requestDream();
    }

    private boolean sleepDefaultDisplayFromPowerButton(long j, int i) {
        int i2;
        PowerManager.WakeData lastWakeup = this.mPowerManagerInternal.getLastWakeup();
        if (lastWakeup != null && ((i2 = lastWakeup.wakeReason) == 4 || i2 == 16 || i2 == 17)) {
            long uptimeMillis = SystemClock.uptimeMillis();
            int i3 = this.mPowerButtonSuppressionDelayMillis;
            if (i3 > 0 && uptimeMillis < lastWakeup.wakeTime + i3) {
                Slog.i(TAG, "Sleep from power button suppressed. Time since gesture: " + (uptimeMillis - lastWakeup.wakeTime) + "ms");
                this.mPhoneWindowManagerExt.notePowerkeyProcessEvent("Ignored goToSleepFromPowerButton for gesture wakeup", false, true);
                return false;
            }
        }
        if (this.mPhoneWindowManagerExt.isSleepByPowerButtonDisabled() && isScreenOn()) {
            Slog.i(TAG, "Sleep from power button suppressed due to customize disabled!");
            return false;
        }
        this.mPhoneWindowManagerExt.notePowerkeyProcessStagePoint("POWERKEY_goToSleepFromPowerButton");
        sleepDefaultDisplay(j, 4, i);
        return true;
    }

    private void sleepDefaultDisplay(long j, int i, int i2) {
        this.mRequestedOrSleepingDefaultDisplay = true;
        this.mPhoneWindowManagerExt.keyEventSpendTimeEventLog(j);
        this.mPowerManager.goToSleep(j, i, i2);
    }

    private void shortPressPowerGoHome() {
        launchHomeFromHotKey(0, true, false);
        if (isKeyguardShowingAndNotOccluded()) {
            this.mKeyguardDelegate.onShortPowerPressedGoHome();
        }
    }

    private void powerMultiPressAction(long j, boolean z, int i) {
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    return;
                }
                launchTargetActivityOnMultiPressPower();
                return;
            } else {
                Slog.i(TAG, "Starting brightness boost.");
                if (!z) {
                    wakeUpFromPowerKey(j);
                }
                this.mPowerManager.boostScreenBrightness(j);
                return;
            }
        }
        if (!isUserSetupComplete()) {
            Slog.i(TAG, "Ignoring toggling theater mode - device not setup.");
            return;
        }
        if (isTheaterModeEnabled()) {
            Slog.i(TAG, "Toggling theater mode off.");
            Settings.Global.putInt(this.mContext.getContentResolver(), "theater_mode_on", 0);
            if (z) {
                return;
            }
            wakeUpFromPowerKey(j);
            return;
        }
        Slog.i(TAG, "Toggling theater mode on.");
        Settings.Global.putInt(this.mContext.getContentResolver(), "theater_mode_on", 1);
        if (this.mGoToSleepOnButtonPressTheaterMode && z) {
            sleepDefaultDisplay(j, 4, 0);
        }
    }

    private void launchTargetActivityOnMultiPressPower() {
        if (DEBUG_INPUT) {
            Slog.d(TAG, "Executing the double press power action.");
        }
        if (this.mPowerDoublePressTargetActivity != null) {
            Intent intent = new Intent();
            intent.setComponent(this.mPowerDoublePressTargetActivity);
            boolean z = false;
            if (this.mContext.getPackageManager().resolveActivity(intent, 0) != null) {
                KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
                if (keyguardServiceDelegate != null && keyguardServiceDelegate.isShowing()) {
                    z = true;
                }
                intent.addFlags(270532608);
                if (!z) {
                    startActivityAsUser(intent, UserHandle.CURRENT_OR_SELF);
                    return;
                } else {
                    this.mKeyguardDelegate.dismissKeyguardToLaunch(intent);
                    return;
                }
            }
            Slog.e(TAG, "Could not resolve activity with : " + this.mPowerDoublePressTargetActivity.flattenToString() + " name.");
        }
    }

    private int getLidBehavior() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "lid_behavior", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMaxMultiPressPowerCount() {
        if (this.mHasFeatureWatch && GestureLauncherService.isEmergencyGestureSettingEnabled(this.mContext, ActivityManager.getCurrentUser())) {
            return 5;
        }
        if (this.mTriplePressOnPowerBehavior != 0) {
            return 3;
        }
        return this.mDoublePressOnPowerBehavior != 0 ? 2 : 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void powerLongPress(long j) {
        int resolvedLongPressOnPowerBehavior = getResolvedLongPressOnPowerBehavior();
        Slog.d(TAG, "powerLongPress: eventTime=" + j + " behavior =" + resolvedLongPressOnPowerBehavior + " mLongPressOnPowerBehavior=" + this.mLongPressOnPowerBehavior);
        if (resolvedLongPressOnPowerBehavior != 1) {
            if (resolvedLongPressOnPowerBehavior == 2 || resolvedLongPressOnPowerBehavior == 3) {
                this.mPowerKeyHandled = true;
                if (!ActivityManager.isUserAMonkey()) {
                    performHapticFeedback(10003, false, "Power - Long Press - Shut Off");
                    sendCloseSystemWindows(SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS);
                    this.mWindowManagerFuncs.shutdown(resolvedLongPressOnPowerBehavior == 2);
                }
            } else if (resolvedLongPressOnPowerBehavior == 4) {
                this.mPowerKeyHandled = true;
                performHapticFeedback(10003, false, "Power - Long Press - Go To Voice Assist");
                launchVoiceAssist(this.mAllowStartActivityForLongPressOnPowerDuringSetup);
            } else if (resolvedLongPressOnPowerBehavior == 5) {
                this.mPowerKeyHandled = true;
                performHapticFeedback(10002, false, "Power - Long Press - Go To Assistant");
                launchAssistAction(null, Integer.MIN_VALUE, j, 6, 1);
            }
        } else {
            this.mPowerKeyHandled = true;
            if (!this.mPhoneWindowManagerExt.getInputExtension().interceptLongPowerPress()) {
                showGlobalActions();
            }
        }
        if (this.mPowerKeyHandled) {
            this.mPhoneWindowManagerExt.notePowerkeyProcessEvent("powerLongPress handled behavior is " + resolvedLongPressOnPowerBehavior, true, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void powerVeryLongPress() {
        Slog.v(TAG, " powerVeryLongPress b=" + this.mVeryLongPressOnPowerBehavior);
        if (this.mVeryLongPressOnPowerBehavior != 1) {
            return;
        }
        this.mPowerKeyHandled = true;
        performHapticFeedback(10003, false, "Power - Very Long Press - Show Global Actions");
        showGlobalActions();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void backLongPress() {
        this.mBackKeyHandled = true;
        if (this.mLongPressOnBackBehavior != 1) {
            return;
        }
        launchVoiceAssist(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void accessibilityShortcutActivated() {
        this.mAccessibilityShortcutController.performAccessibilityShortcut();
    }

    private void sleepPress() {
        if (this.mShortPressOnSleepBehavior == 1) {
            launchHomeFromHotKey(0, false, true);
        }
    }

    private void sleepRelease(long j) {
        int i = this.mShortPressOnSleepBehavior;
        if (i == 0 || i == 1) {
            Slog.i(TAG, "sleepRelease() calling goToSleep(GO_TO_SLEEP_REASON_SLEEP_BUTTON)");
            sleepDefaultDisplay(j, 6, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getResolvedLongPressOnPowerBehavior() {
        if (FactoryTest.isLongPressOnPowerOffEnabled()) {
            return 3;
        }
        if (this.mLongPressOnPowerBehavior == 5 && !isDeviceProvisioned()) {
            return 1;
        }
        if (this.mLongPressOnPowerBehavior != 4 || isLongPressToAssistantEnabled(this.mContext)) {
            return this.mLongPressOnPowerBehavior;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stemPrimaryPress(int i) {
        if (DEBUG_INPUT) {
            Slog.d(TAG, "stemPrimaryPress: " + i);
        }
        if (i == 3) {
            stemPrimaryTriplePressAction(this.mTriplePressOnStemPrimaryBehavior);
        } else if (i == 2) {
            stemPrimaryDoublePressAction(this.mDoublePressOnStemPrimaryBehavior);
        } else if (i == 1) {
            stemPrimarySinglePressAction(this.mShortPressOnStemPrimaryBehavior);
        }
    }

    private void stemPrimarySinglePressAction(int i) {
        if (i != 1) {
            return;
        }
        if (DEBUG_INPUT) {
            Slog.d(TAG, "Executing stem primary short press action behavior.");
        }
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (!(keyguardServiceDelegate != null && keyguardServiceDelegate.isShowing())) {
            Intent intent = new Intent("android.intent.action.ALL_APPS");
            intent.addFlags(270532608);
            startActivityAsUser(intent, UserHandle.CURRENT_OR_SELF);
            return;
        }
        this.mKeyguardDelegate.onSystemKeyPressed(264);
    }

    private void stemPrimaryDoublePressAction(int i) {
        if (i != 1) {
            return;
        }
        if (DEBUG_INPUT) {
            Slog.d(TAG, "Executing stem primary double press action behavior.");
        }
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate == null ? false : keyguardServiceDelegate.isShowing()) {
            return;
        }
        switchRecentTask();
    }

    private void stemPrimaryTriplePressAction(int i) {
        if (i != 1) {
            return;
        }
        if (DEBUG_INPUT) {
            Slog.d(TAG, "Executing stem primary triple press action behavior.");
        }
        toggleTalkBack();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stemPrimaryLongPress() {
        if (DEBUG_INPUT) {
            Slog.d(TAG, "Executing stem primary long press action behavior.");
        }
        if (this.mLongPressOnStemPrimaryBehavior != 1) {
            return;
        }
        launchVoiceAssist(false);
    }

    private void toggleTalkBack() {
        ComponentName talkbackComponent = getTalkbackComponent();
        if (talkbackComponent == null) {
            return;
        }
        AccessibilityUtils.setAccessibilityServiceState(this.mContext, talkbackComponent, !AccessibilityUtils.getEnabledServicesFromSettings(this.mContext, this.mCurrentUserId).contains(talkbackComponent));
    }

    private ComponentName getTalkbackComponent() {
        Iterator<AccessibilityServiceInfo> it = ((AccessibilityManager) this.mContext.getSystemService(AccessibilityManager.class)).getInstalledAccessibilityServiceList().iterator();
        while (it.hasNext()) {
            ServiceInfo serviceInfo = it.next().getResolveInfo().serviceInfo;
            if (isTalkback(serviceInfo)) {
                return new ComponentName(serviceInfo.packageName, serviceInfo.name);
            }
        }
        return null;
    }

    private boolean isTalkback(ServiceInfo serviceInfo) {
        return serviceInfo.loadLabel(this.mPackageManager).toString().equals(TALKBACK_LABEL);
    }

    private void switchRecentTask() {
        ActivityManager.RecentTaskInfo mostRecentTaskFromBackground = this.mActivityTaskManagerInternal.getMostRecentTaskFromBackground();
        if (mostRecentTaskFromBackground == null) {
            if (DEBUG_INPUT) {
                Slog.w(TAG, "No recent task available! Show watch face.");
            }
            goHome();
            return;
        }
        if (DEBUG_INPUT) {
            Slog.d(TAG, "Starting task from recents. id=" + mostRecentTaskFromBackground.id + ", persistentId=" + mostRecentTaskFromBackground.persistentId + ", topActivity=" + mostRecentTaskFromBackground.topActivity + ", baseIntent=" + mostRecentTaskFromBackground.baseIntent);
        }
        try {
            ActivityManager.getService().startActivityFromRecents(mostRecentTaskFromBackground.persistentId, (Bundle) null);
        } catch (RemoteException | IllegalArgumentException e) {
            Slog.e(TAG, "Failed to start task " + mostRecentTaskFromBackground.persistentId + " from recents", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMaxMultiPressStemPrimaryCount() {
        if (this.mTriplePressOnStemPrimaryBehavior == 1 && Settings.System.getIntForUser(this.mContext.getContentResolver(), "wear_accessibility_gesture_enabled", 0, -2) == 1) {
            return 3;
        }
        return this.mDoublePressOnStemPrimaryBehavior != 0 ? 2 : 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasLongPressOnPowerBehavior() {
        return getResolvedLongPressOnPowerBehavior() != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasVeryLongPressOnPowerBehavior() {
        return this.mVeryLongPressOnPowerBehavior != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasLongPressOnBackBehavior() {
        return this.mLongPressOnBackBehavior != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasLongPressOnStemPrimaryBehavior() {
        return this.mLongPressOnStemPrimaryBehavior != 0;
    }

    private boolean hasStemPrimaryBehavior() {
        return getMaxMultiPressStemPrimaryCount() > 1 || hasLongPressOnStemPrimaryBehavior() || this.mShortPressOnStemPrimaryBehavior != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void interceptScreenshotChord(int i, long j) {
        this.mHandler.removeMessages(16);
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(16, i, 0), j);
        this.mPhoneWindowManagerExt.interceptScreenshotChord();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void interceptAccessibilityShortcutChord() {
        this.mHandler.removeMessages(17);
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(17), getAccessibilityShortcutTimeout());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void interceptRingerToggleChord() {
        this.mHandler.removeMessages(MSG_RINGER_TOGGLE_CHORD);
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(MSG_RINGER_TOGGLE_CHORD), getRingerToggleChordDelay());
    }

    private long getAccessibilityShortcutTimeout() {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(this.mContext);
        boolean z = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_shortcut_dialog_shown", 0, this.mCurrentUserId) != 0;
        boolean z2 = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "skip_accessibility_shortcut_dialog_timeout_restriction", 0, this.mCurrentUserId) != 0;
        if (z || z2) {
            return viewConfiguration.getAccessibilityShortcutKeyTimeoutAfterConfirmation();
        }
        return viewConfiguration.getAccessibilityShortcutKeyTimeout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getScreenshotChordLongPressDelay() {
        if (this.mPhoneWindowManagerExt.isCustomize()) {
            return this.mPhoneWindowManagerExt.hookScreenshotChordLongPressDelay();
        }
        return this.mKeyguardDelegate.isShowing() ? ((float) r0) * KEYGUARD_SCREENSHOT_CHORD_DELAY_MULTIPLIER : DeviceConfig.getLong("systemui", "screenshot_keychord_delay", ViewConfiguration.get(this.mContext).getScreenshotChordKeyTimeout());
    }

    private long getRingerToggleChordDelay() {
        return ViewConfiguration.getTapTimeout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelPendingScreenshotChordAction() {
        this.mHandler.removeMessages(16);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelPendingAccessibilityShortcutAction() {
        this.mHandler.removeMessages(17);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelPendingRingerToggleChordAction() {
        this.mHandler.removeMessages(MSG_RINGER_TOGGLE_CHORD);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScreenShot(@WindowManager.ScreenshotSource int i) {
        this.mDefaultDisplayPolicy.takeScreenshot(1, i);
        this.mPhoneWindowManagerExt.getTpInfo("ctl.start", "gettpinfo");
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void showGlobalActions() {
        this.mHandler.removeMessages(10);
        this.mHandler.sendEmptyMessage(10);
    }

    void showGlobalActionsInternal() {
        if (this.mGlobalActions == null) {
            this.mGlobalActions = this.mGlobalActionsFactory.get();
        }
        this.mGlobalActions.showDialog(isKeyguardShowingAndNotOccluded(), isDeviceProvisioned());
        this.mPowerManager.userActivity(SystemClock.uptimeMillis(), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelGlobalActionsAction() {
        this.mHandler.removeMessages(10);
    }

    boolean isDeviceProvisioned() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isUserSetupComplete() {
        boolean isAutoUserSetupComplete;
        boolean z = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "user_setup_complete", 0, -2) != 0;
        if (this.mHasFeatureLeanback) {
            isAutoUserSetupComplete = isTvUserSetupComplete();
        } else {
            if (!this.mHasFeatureAuto) {
                return z;
            }
            isAutoUserSetupComplete = isAutoUserSetupComplete();
        }
        return z & isAutoUserSetupComplete;
    }

    private boolean isAutoUserSetupComplete() {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "android.car.SETUP_WIZARD_IN_PROGRESS", 0, -2) == 0;
    }

    private boolean isTvUserSetupComplete() {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "tv_user_setup_complete", 0, -2) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleShortPressOnHome(int i) {
        HdmiControl hdmiControl = getHdmiControl();
        if (hdmiControl != null) {
            hdmiControl.turnOnTv();
        }
        DreamManagerInternal dreamManagerInternal = getDreamManagerInternal();
        if (dreamManagerInternal != null && dreamManagerInternal.isDreaming()) {
            this.mDreamManagerInternal.stopDream(false, "short press on home");
            Log.i(TAG, "Handle short press on home, when there is a dream running.");
        } else {
            launchHomeFromHotKey(i);
        }
    }

    private HdmiControl getHdmiControl() {
        if (this.mHdmiControl == null) {
            if (!this.mHasFeatureHdmiCec) {
                return null;
            }
            HdmiControlManager hdmiControlManager = (HdmiControlManager) this.mContext.getSystemService("hdmi_control");
            this.mHdmiControl = new HdmiControl(hdmiControlManager != null ? hdmiControlManager.getPlaybackClient() : null);
        }
        return this.mHdmiControl;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class HdmiControl {
        private final HdmiPlaybackClient mClient;

        private HdmiControl(HdmiPlaybackClient hdmiPlaybackClient) {
            this.mClient = hdmiPlaybackClient;
        }

        public void turnOnTv() {
            HdmiPlaybackClient hdmiPlaybackClient = this.mClient;
            if (hdmiPlaybackClient == null) {
                return;
            }
            hdmiPlaybackClient.oneTouchPlay(new HdmiPlaybackClient.OneTouchPlayCallback() { // from class: com.android.server.policy.PhoneWindowManager.HdmiControl.1
                public void onComplete(int i) {
                    if (i != 0) {
                        Log.w(PhoneWindowManager.TAG, "One touch play failed: " + i);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchAllAppsAction() {
        Intent intent = new Intent("android.intent.action.ALL_APPS");
        if (this.mHasFeatureLeanback) {
            Intent intent2 = new Intent("android.intent.action.MAIN");
            intent2.addCategory("android.intent.category.HOME");
            ResolveInfo resolveActivityAsUser = this.mPackageManager.resolveActivityAsUser(intent2, 1048576, this.mCurrentUserId);
            if (resolveActivityAsUser != null) {
                intent.setPackage(resolveActivityAsUser.activityInfo.packageName);
            }
        }
        startActivityAsUser(intent, UserHandle.CURRENT);
    }

    private void launchAllAppsViaA11y() {
        getAccessibilityManagerInternal().performSystemAction(14);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleNotificationPanel() {
        IStatusBarService statusBarService = getStatusBarService();
        if (!isUserSetupComplete() || statusBarService == null) {
            return;
        }
        try {
            statusBarService.togglePanel();
        } catch (RemoteException unused) {
        }
    }

    private void showSystemSettings() {
        startActivityAsUser(new Intent("android.settings.SETTINGS"), UserHandle.CURRENT_OR_SELF);
    }

    private void showPictureInPictureMenu(KeyEvent keyEvent) {
        if (DEBUG_INPUT) {
            Log.d(TAG, "showPictureInPictureMenu event=" + keyEvent);
        }
        this.mHandler.removeMessages(15);
        Message obtainMessage = this.mHandler.obtainMessage(15);
        obtainMessage.setAsynchronous(true);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPictureInPictureMenuInternal() {
        StatusBarManagerInternal statusBarManagerInternal = getStatusBarManagerInternal();
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.showPictureInPictureMenu();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class DisplayHomeButtonHandler {
        private final int mDisplayId;
        private boolean mHomeConsumed;
        private boolean mHomeDoubleTapPending;
        private final Runnable mHomeDoubleTapTimeoutRunnable = new Runnable() { // from class: com.android.server.policy.PhoneWindowManager.DisplayHomeButtonHandler.1
            @Override // java.lang.Runnable
            public void run() {
                if (DisplayHomeButtonHandler.this.mHomeDoubleTapPending) {
                    DisplayHomeButtonHandler.this.mHomeDoubleTapPending = false;
                    DisplayHomeButtonHandler displayHomeButtonHandler = DisplayHomeButtonHandler.this;
                    PhoneWindowManager.this.handleShortPressOnHome(displayHomeButtonHandler.mDisplayId);
                }
            }
        };
        private boolean mHomePressed;

        DisplayHomeButtonHandler(int i) {
            this.mDisplayId = i;
        }

        int handleHomeButton(IBinder iBinder, final KeyEvent keyEvent) {
            boolean keyguardOn = PhoneWindowManager.this.keyguardOn();
            int repeatCount = keyEvent.getRepeatCount();
            boolean z = keyEvent.getAction() == 0;
            boolean isCanceled = keyEvent.isCanceled();
            if (PhoneWindowManager.DEBUG_INPUT) {
                Log.d(PhoneWindowManager.TAG, String.format("handleHomeButton in display#%d mHomePressed = %b", Integer.valueOf(this.mDisplayId), Boolean.valueOf(this.mHomePressed)));
            }
            if (!z) {
                if (this.mDisplayId == 0) {
                    PhoneWindowManager.this.cancelPreloadRecentApps();
                }
                this.mHomePressed = false;
                if (this.mHomeConsumed) {
                    this.mHomeConsumed = false;
                    return -1;
                }
                if (isCanceled) {
                    Log.i(PhoneWindowManager.TAG, "Ignoring HOME; event canceled.");
                    return -1;
                }
                if (PhoneWindowManager.this.mDoubleTapOnHomeBehavior != 0 && (PhoneWindowManager.this.mDoubleTapOnHomeBehavior != 2 || PhoneWindowManager.this.mPictureInPictureVisible)) {
                    PhoneWindowManager.this.mHandler.removeCallbacks(this.mHomeDoubleTapTimeoutRunnable);
                    this.mHomeDoubleTapPending = true;
                    PhoneWindowManager.this.mHandler.postDelayed(this.mHomeDoubleTapTimeoutRunnable, ViewConfiguration.getDoubleTapTimeout());
                    return -1;
                }
                PhoneWindowManager.this.mHandler.post(new Runnable() { // from class: com.android.server.policy.PhoneWindowManager$DisplayHomeButtonHandler$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhoneWindowManager.DisplayHomeButtonHandler.this.lambda$handleHomeButton$0();
                    }
                });
                return -1;
            }
            KeyInterceptionInfo keyInterceptionInfoFromToken = PhoneWindowManager.this.mWindowManagerInternal.getKeyInterceptionInfoFromToken(iBinder);
            if (keyInterceptionInfoFromToken != null) {
                int i = keyInterceptionInfoFromToken.layoutParamsType;
                if (i == 2009 || (i == 2040 && PhoneWindowManager.this.isKeyguardShowing())) {
                    return 0;
                }
                for (int i2 : PhoneWindowManager.WINDOW_TYPES_WHERE_HOME_DOESNT_WORK) {
                    if (keyInterceptionInfoFromToken.layoutParamsType == i2) {
                        return -1;
                    }
                }
            }
            if (repeatCount == 0) {
                this.mHomePressed = true;
                if (this.mHomeDoubleTapPending) {
                    this.mHomeDoubleTapPending = false;
                    PhoneWindowManager.this.mHandler.removeCallbacks(this.mHomeDoubleTapTimeoutRunnable);
                    PhoneWindowManager.this.mHandler.post(new Runnable() { // from class: com.android.server.policy.PhoneWindowManager$DisplayHomeButtonHandler$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            PhoneWindowManager.DisplayHomeButtonHandler.this.handleDoubleTapOnHome();
                        }
                    });
                } else if (PhoneWindowManager.this.mDoubleTapOnHomeBehavior == 1 && this.mDisplayId == 0) {
                    PhoneWindowManager.this.preloadRecentApps();
                }
            } else if ((keyEvent.getFlags() & 128) != 0 && !keyguardOn) {
                PhoneWindowManager.this.mHandler.post(new Runnable() { // from class: com.android.server.policy.PhoneWindowManager$DisplayHomeButtonHandler$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhoneWindowManager.DisplayHomeButtonHandler.this.lambda$handleHomeButton$1(keyEvent);
                    }
                });
            }
            return -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleHomeButton$0() {
            PhoneWindowManager.this.handleShortPressOnHome(this.mDisplayId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleHomeButton$1(KeyEvent keyEvent) {
            handleLongPressOnHome(keyEvent.getDeviceId(), keyEvent.getEventTime());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleDoubleTapOnHome() {
            if (this.mHomeConsumed) {
                return;
            }
            int i = PhoneWindowManager.this.mDoubleTapOnHomeBehavior;
            if (i == 1) {
                this.mHomeConsumed = true;
                PhoneWindowManager.this.toggleRecentApps();
            } else if (i == 2) {
                this.mHomeConsumed = true;
                PhoneWindowManager.this.showPictureInPictureMenuInternal();
            } else {
                Log.w(PhoneWindowManager.TAG, "No action or undefined behavior for double tap home: " + PhoneWindowManager.this.mDoubleTapOnHomeBehavior);
            }
        }

        private void handleLongPressOnHome(int i, long j) {
            if (this.mHomeConsumed || PhoneWindowManager.this.mLongPressOnHomeBehavior == 0 || PhoneWindowManager.this.mPhoneWindowManagerExt.getInputExtension().interceptLongHomePress()) {
                return;
            }
            this.mHomeConsumed = true;
            int i2 = PhoneWindowManager.this.mLongPressOnHomeBehavior;
            if (i2 == 1) {
                PhoneWindowManager.this.launchAllAppsAction();
                return;
            }
            if (i2 == 2) {
                PhoneWindowManager.this.launchAssistAction(null, i, j, 5, 1);
                return;
            }
            if (i2 == 3) {
                PhoneWindowManager.this.toggleNotificationPanel();
                return;
            }
            Log.w(PhoneWindowManager.TAG, "Undefined long press on home behavior: " + PhoneWindowManager.this.mLongPressOnHomeBehavior);
        }

        public String toString() {
            return String.format("mDisplayId = %d, mHomePressed = %b", Integer.valueOf(this.mDisplayId), Boolean.valueOf(this.mHomePressed));
        }
    }

    private boolean isRoundWindow() {
        return this.mContext.getResources().getConfiguration().isScreenRound();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setDefaultDisplay(WindowManagerPolicy.DisplayContentInfo displayContentInfo) {
        this.mDefaultDisplay = displayContentInfo.getDisplay();
        DisplayRotation displayRotation = displayContentInfo.getDisplayRotation();
        this.mDefaultDisplayRotation = displayRotation;
        DisplayPolicy displayPolicy = displayRotation.getDisplayPolicy();
        this.mDefaultDisplayPolicy = displayPolicy;
        this.mPhoneWindowManagerSocExt.hookSetDefaultDisplay(displayPolicy);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Injector {
        private final Context mContext;
        private final WindowManagerPolicy.WindowManagerFuncs mWindowManagerFuncs;

        Injector(Context context, WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs) {
            this.mContext = context;
            this.mWindowManagerFuncs = windowManagerFuncs;
        }

        Context getContext() {
            return this.mContext;
        }

        WindowManagerPolicy.WindowManagerFuncs getWindowManagerFuncs() {
            return this.mWindowManagerFuncs;
        }

        AccessibilityShortcutController getAccessibilityShortcutController(Context context, Handler handler, int i) {
            return new AccessibilityShortcutController(context, handler, i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ GlobalActions lambda$getGlobalActionsFactory$0() {
            return new GlobalActions(this.mContext, this.mWindowManagerFuncs);
        }

        Supplier<GlobalActions> getGlobalActionsFactory() {
            return new Supplier() { // from class: com.android.server.policy.PhoneWindowManager$Injector$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final Object get() {
                    GlobalActions lambda$getGlobalActionsFactory$0;
                    lambda$getGlobalActionsFactory$0 = PhoneWindowManager.Injector.this.lambda$getGlobalActionsFactory$0();
                    return lambda$getGlobalActionsFactory$0;
                }
            };
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setSecondDefaultDisplay(WindowManagerPolicy.DisplayContentInfo displayContentInfo) {
        this.mPhoneWindowManagerExt.setSecondDefaultDisplay(displayContentInfo);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setDisplayEnable(boolean z, boolean z2) {
        this.mPhoneWindowManagerExt.setDisplayEnable(z, z2);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean getDisplayEnable(boolean z) {
        return this.mPhoneWindowManagerExt.getDisplayEnable(z);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void init(Context context, WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs) {
        init(new Injector(context, windowManagerFuncs));
    }

    @VisibleForTesting
    void init(Injector injector) {
        int integer;
        int i;
        int i2;
        int i3;
        int i4;
        this.mContext = injector.getContext();
        this.mWindowManagerFuncs = injector.getWindowManagerFuncs();
        this.mWindowManagerInternal = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
        this.mActivityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        this.mActivityTaskManagerInternal = (ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class);
        this.mInputManagerInternal = (InputManagerInternal) LocalServices.getService(InputManagerInternal.class);
        this.mDreamManagerInternal = (DreamManagerInternal) LocalServices.getService(DreamManagerInternal.class);
        this.mPowerManagerInternal = (PowerManagerInternal) LocalServices.getService(PowerManagerInternal.class);
        this.mAppOpsManager = (AppOpsManager) this.mContext.getSystemService(AppOpsManager.class);
        this.mSensorPrivacyManager = (SensorPrivacyManager) this.mContext.getSystemService(SensorPrivacyManager.class);
        this.mDisplayManager = (DisplayManager) this.mContext.getSystemService(DisplayManager.class);
        this.mDisplayManagerInternal = (DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class);
        this.mUserManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        PackageManager packageManager = this.mContext.getPackageManager();
        this.mPackageManager = packageManager;
        this.mHasFeatureWatch = packageManager.hasSystemFeature("android.hardware.type.watch");
        this.mHasFeatureLeanback = this.mPackageManager.hasSystemFeature("android.software.leanback");
        this.mHasFeatureAuto = this.mPackageManager.hasSystemFeature("android.hardware.type.automotive");
        this.mHasFeatureHdmiCec = this.mPackageManager.hasSystemFeature("android.hardware.hdmi.cec");
        this.mAccessibilityShortcutController = injector.getAccessibilityShortcutController(this.mContext, new Handler(), this.mCurrentUserId);
        this.mGlobalActionsFactory = injector.getGlobalActionsFactory();
        this.mLockPatternUtils = new LockPatternUtils(this.mContext);
        this.mLogger = new MetricsLogger();
        this.mScreenOffSleepTokenAcquirer = this.mActivityTaskManagerInternal.createSleepTokenAcquirer("ScreenOff");
        Resources resources = this.mContext.getResources();
        this.mWakeOnDpadKeyPress = resources.getBoolean(17891908);
        this.mWakeOnAssistKeyPress = resources.getBoolean(17891906);
        this.mWakeOnBackKeyPress = resources.getBoolean(17891907);
        boolean z = this.mContext.getResources().getBoolean(17891657);
        boolean z2 = SystemProperties.getBoolean("persist.debug.force_burn_in", false);
        if (z || z2) {
            if (z2) {
                integer = isRoundWindow() ? 6 : -1;
                i = -8;
                i3 = -8;
                i2 = 8;
                i4 = -4;
            } else {
                Resources resources2 = this.mContext.getResources();
                int integer2 = resources2.getInteger(R.integer.config_debugSystemServerPssThresholdBytes);
                int integer3 = resources2.getInteger(R.integer.config_datause_polling_period_sec);
                int integer4 = resources2.getInteger(R.integer.config_defaultDisplayDefaultColorMode);
                int integer5 = resources2.getInteger(R.integer.config_datause_throttle_kbitsps);
                integer = resources2.getInteger(R.integer.config_datause_threshold_bytes);
                i = integer2;
                i2 = integer3;
                i3 = integer4;
                i4 = integer5;
            }
            this.mBurnInProtectionHelper = new BurnInProtectionHelper(this.mContext, i, i2, i3, i4, integer);
        }
        PolicyHandler policyHandler = new PolicyHandler();
        this.mHandler = policyHandler;
        this.mWakeGestureListener = new MyWakeGestureListener(this.mContext, policyHandler);
        SettingsObserver settingsObserver = new SettingsObserver(this.mHandler);
        this.mSettingsObserver = settingsObserver;
        settingsObserver.observe();
        this.mModifierShortcutManager = new ModifierShortcutManager(this.mContext);
        this.mUiMode = this.mContext.getResources().getInteger(R.integer.config_dynamicPowerSavingsDefaultDisableThreshold);
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        this.mHomeIntent = intent;
        intent.addCategory("android.intent.category.HOME");
        this.mHomeIntent.addFlags(270532608);
        this.mEnableCarDockHomeCapture = this.mContext.getResources().getBoolean(17891658);
        Intent intent2 = new Intent("android.intent.action.MAIN", (Uri) null);
        this.mCarDockIntent = intent2;
        intent2.addCategory("android.intent.category.CAR_DOCK");
        this.mCarDockIntent.addFlags(270532608);
        Intent intent3 = new Intent("android.intent.action.MAIN", (Uri) null);
        this.mDeskDockIntent = intent3;
        intent3.addCategory("android.intent.category.DESK_DOCK");
        this.mDeskDockIntent.addFlags(270532608);
        Intent intent4 = new Intent("android.intent.action.MAIN", (Uri) null);
        this.mVrHeadsetHomeIntent = intent4;
        intent4.addCategory("android.intent.category.VR_HOME");
        this.mVrHeadsetHomeIntent.addFlags(270532608);
        PowerManager powerManager = (PowerManager) this.mContext.getSystemService("power");
        this.mPowerManager = powerManager;
        this.mBroadcastWakeLock = powerManager.newWakeLock(1, "PhoneWindowManager.mBroadcastWakeLock");
        this.mPowerKeyWakeLock = this.mPowerManager.newWakeLock(1, "PhoneWindowManager.mPowerKeyWakeLock");
        this.mEnableShiftMenuBugReports = "1".equals(SystemProperties.get("ro.debuggable"));
        this.mLidKeyboardAccessibility = this.mContext.getResources().getInteger(R.integer.config_notificationsBatteryLedOff);
        this.mLidNavigationAccessibility = this.mContext.getResources().getInteger(R.integer.config_notificationsBatteryLedOn);
        boolean z3 = this.mContext.getResources().getBoolean(R.bool.config_annoy_dianne);
        this.mAllowTheaterModeWakeFromKey = z3;
        this.mAllowTheaterModeWakeFromPowerKey = z3 || this.mContext.getResources().getBoolean(R.bool.config_autoPowerModePreferWristTilt);
        this.mAllowTheaterModeWakeFromMotion = this.mContext.getResources().getBoolean(R.bool.config_assistantOnTopOfDream);
        this.mAllowTheaterModeWakeFromMotionWhenNotDreaming = this.mContext.getResources().getBoolean(R.bool.config_autoBrightnessResetAmbientLuxAfterWarmUp);
        this.mAllowTheaterModeWakeFromCameraLens = this.mContext.getResources().getBoolean(R.bool.config_allow_ussd_over_ims);
        this.mAllowTheaterModeWakeFromLidSwitch = this.mContext.getResources().getBoolean(R.bool.config_apfDrop802_3Frames);
        this.mAllowTheaterModeWakeFromWakeGesture = this.mContext.getResources().getBoolean(R.bool.config_animateScreenLights);
        this.mGoToSleepOnButtonPressTheaterMode = this.mContext.getResources().getBoolean(17891703);
        this.mSupportLongPressPowerWhenNonInteractive = this.mContext.getResources().getBoolean(17891844);
        this.mLongPressOnBackBehavior = this.mContext.getResources().getInteger(R.integer.config_ntpPollingIntervalShorter);
        this.mShortPressOnPowerBehavior = this.mContext.getResources().getInteger(R.integer.timepicker_title_visibility);
        this.mLongPressOnPowerBehavior = this.mContext.getResources().getInteger(R.integer.config_ntpTimeout);
        this.mLongPressOnPowerAssistantTimeoutMs = this.mContext.getResources().getInteger(R.integer.config_num_physical_slots);
        this.mVeryLongPressOnPowerBehavior = this.mContext.getResources().getInteger(R.integer.leanback_setup_translation_forward_in_content_duration);
        this.mDoublePressOnPowerBehavior = this.mContext.getResources().getInteger(R.integer.config_lowBatteryWarningLevel);
        this.mPowerDoublePressTargetActivity = ComponentName.unflattenFromString(this.mContext.getResources().getString(R.string.config_wallpaperManagerServiceName));
        this.mTriplePressOnPowerBehavior = this.mContext.getResources().getInteger(R.integer.leanback_setup_base_animation_duration);
        this.mShortPressOnSleepBehavior = this.mContext.getResources().getInteger(R.integer.default_data_warning_level_mb);
        this.mAllowStartActivityForLongPressOnPowerDuringSetup = this.mContext.getResources().getBoolean(R.bool.config_allowTheaterModeWakeFromWindowLayout);
        this.mHapticTextHandleEnabled = this.mContext.getResources().getBoolean(17891665);
        this.mUseTvRouting = AudioSystem.getPlatformType(this.mContext) == 2;
        this.mHandleVolumeKeysInWM = this.mContext.getResources().getBoolean(17891708);
        this.mWakeUpToLastStateTimeout = this.mContext.getResources().getInteger(R.integer.time_picker_mode);
        this.mSearchKeyBehavior = this.mContext.getResources().getInteger(R.integer.time_picker_mode);
        this.mSearchKeyTargetActivity = ComponentName.unflattenFromString(this.mContext.getResources().getString(R.string.default_sms_application));
        readConfigurationDependentBehaviors();
        this.mDisplayFoldController = DisplayFoldController.create(this.mContext, 0);
        this.mAccessibilityManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UiModeManager.ACTION_ENTER_CAR_MODE);
        intentFilter.addAction(UiModeManager.ACTION_EXIT_CAR_MODE);
        intentFilter.addAction(UiModeManager.ACTION_ENTER_DESK_MODE);
        intentFilter.addAction(UiModeManager.ACTION_EXIT_DESK_MODE);
        intentFilter.addAction("android.intent.action.DOCK_EVENT");
        Intent registerReceiver = this.mContext.registerReceiver(this.mDockReceiver, intentFilter);
        if (registerReceiver != null) {
            this.mDefaultDisplayPolicy.setDockMode(registerReceiver.getIntExtra("android.intent.extra.DOCK_STATE", 0));
        }
        this.mContext.registerReceiver(this.mMultiuserReceiver, new IntentFilter("android.intent.action.USER_SWITCHED"));
        this.mVibrator = (Vibrator) this.mContext.getSystemService("vibrator");
        this.mSafeModeEnabledVibePattern = getLongIntArray(this.mContext.getResources(), R.array.vendor_disallowed_apps_managed_user);
        this.mGlobalKeyManager = new GlobalKeyManager(this.mContext);
        initializeHdmiState();
        if (!this.mPowerManager.isInteractive()) {
            startedGoingToSleep(0, 2);
            finishedGoingToSleep(0, 2);
        }
        this.mWindowManagerInternal.registerAppTransitionListener(new WindowManagerInternal.AppTransitionListener() { // from class: com.android.server.policy.PhoneWindowManager.5
            public int onAppTransitionStartingLocked(long j, long j2) {
                return PhoneWindowManager.this.handleTransitionForKeyguardLw(false, false);
            }

            public void onAppTransitionCancelledLocked(boolean z4) {
                PhoneWindowManager.this.handleTransitionForKeyguardLw(z4, true);
                synchronized (PhoneWindowManager.this.mLock) {
                    PhoneWindowManager.this.mLockAfterAppTransitionFinished = false;
                }
            }

            public void onAppTransitionFinishedLocked(IBinder iBinder) {
                synchronized (PhoneWindowManager.this.mLock) {
                    PhoneWindowManager phoneWindowManager = PhoneWindowManager.this;
                    if (phoneWindowManager.mLockAfterAppTransitionFinished) {
                        phoneWindowManager.mLockAfterAppTransitionFinished = false;
                        phoneWindowManager.lockNow(null);
                    }
                }
            }
        });
        this.mKeyguardDrawnTimeout = this.mContext.getResources().getInteger(R.integer.config_nightDisplayColorTemperatureDefault);
        this.mKeyguardDelegate = new KeyguardServiceDelegate(this.mContext, new KeyguardStateMonitor.StateCallback() { // from class: com.android.server.policy.PhoneWindowManager.6
            @Override // com.android.server.policy.keyguard.KeyguardStateMonitor.StateCallback
            public void onTrustedChanged() {
                PhoneWindowManager.this.mWindowManagerFuncs.notifyKeyguardTrustedChanged();
            }

            @Override // com.android.server.policy.keyguard.KeyguardStateMonitor.StateCallback
            public void onShowingChanged() {
                PhoneWindowManager.this.mWindowManagerFuncs.onKeyguardShowingAndNotOccludedChanged();
            }
        });
        initKeyCombinationRules();
        initSingleKeyGestureRules();
        this.mPhoneWindowManagerExt.hookForInit();
        this.mSideFpsEventHandler = new SideFpsEventHandler(this.mContext, this.mHandler, this.mPowerManager);
    }

    private void initKeyCombinationRules() {
        this.mKeyCombinationManager = new KeyCombinationManager(this.mHandler);
        boolean z = this.mContext.getResources().getBoolean(17891677);
        Slog.d(TAG, "initKeyCombinationRules screenshotChordEnabled:" + z);
        int i = MSG_SWITCH_KEYBOARD_LAYOUT;
        int i2 = 26;
        if (z) {
            this.mKeyCombinationManager.addRule(new KeyCombinationManager.TwoKeysCombinationRule(i, i2) { // from class: com.android.server.policy.PhoneWindowManager.7
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                long getKeyInterceptDelayMs() {
                    return 0L;
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                public void execute() {
                    PhoneWindowManager.this.mPowerKeyHandled = true;
                    PhoneWindowManager phoneWindowManager = PhoneWindowManager.this;
                    phoneWindowManager.interceptScreenshotChord(1, phoneWindowManager.getScreenshotChordLongPressDelay());
                    PhoneWindowManager.this.mPhoneWindowManagerExt.onScreenShotKeyPressedOnTheiaMonitor();
                    PhoneWindowManager.this.mPhoneWindowManagerExt.sendBroadcastForCombinationKeyGrabSystrace();
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                public void cancel() {
                    PhoneWindowManager.this.cancelPendingScreenshotChordAction();
                }
            });
            if (this.mHasFeatureWatch) {
                this.mKeyCombinationManager.addRule(new KeyCombinationManager.TwoKeysCombinationRule(i2, 264) { // from class: com.android.server.policy.PhoneWindowManager.8
                    /* JADX INFO: Access modifiers changed from: package-private */
                    @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                    public void execute() {
                        PhoneWindowManager.this.mPowerKeyHandled = true;
                        PhoneWindowManager phoneWindowManager = PhoneWindowManager.this;
                        phoneWindowManager.interceptScreenshotChord(1, phoneWindowManager.getScreenshotChordLongPressDelay());
                    }

                    /* JADX INFO: Access modifiers changed from: package-private */
                    @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                    public void cancel() {
                        PhoneWindowManager.this.cancelPendingScreenshotChordAction();
                    }
                });
            }
        }
        KeyCombinationManager keyCombinationManager = this.mKeyCombinationManager;
        int i3 = MSG_RINGER_TOGGLE_CHORD;
        keyCombinationManager.addRule(new KeyCombinationManager.TwoKeysCombinationRule(i, i3) { // from class: com.android.server.policy.PhoneWindowManager.9
            @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
            boolean preCondition() {
                return PhoneWindowManager.this.mAccessibilityShortcutController.isAccessibilityShortcutAvailable(PhoneWindowManager.this.isKeyguardLocked());
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
            public void execute() {
                PhoneWindowManager.this.interceptAccessibilityShortcutChord();
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
            public void cancel() {
                PhoneWindowManager.this.cancelPendingAccessibilityShortcutAction();
            }
        });
        this.mKeyCombinationManager.addRule(new KeyCombinationManager.TwoKeysCombinationRule(i3, i2) { // from class: com.android.server.policy.PhoneWindowManager.10
            @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
            boolean preCondition() {
                PhoneWindowManager phoneWindowManager = PhoneWindowManager.this;
                return (phoneWindowManager.mPowerVolUpBehavior == 1 && phoneWindowManager.mRingerToggleChord == 0) ? false : true;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
            public void execute() {
                PhoneWindowManager phoneWindowManager = PhoneWindowManager.this;
                int i4 = phoneWindowManager.mPowerVolUpBehavior;
                if (i4 == 0 || i4 == 1) {
                    phoneWindowManager.interceptRingerToggleChord();
                    PhoneWindowManager.this.mPowerKeyHandled = true;
                } else {
                    if (i4 != 2) {
                        return;
                    }
                    phoneWindowManager.performHapticFeedback(10003, false, "Power + Volume Up - Global Actions");
                    PhoneWindowManager.this.showGlobalActions();
                    PhoneWindowManager.this.mPowerKeyHandled = true;
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
            public void cancel() {
                PhoneWindowManager phoneWindowManager = PhoneWindowManager.this;
                int i4 = phoneWindowManager.mPowerVolUpBehavior;
                if (i4 == 1) {
                    phoneWindowManager.cancelPendingRingerToggleChordAction();
                } else {
                    if (i4 != 2) {
                        return;
                    }
                    phoneWindowManager.cancelGlobalActionsAction();
                }
            }
        });
        if (this.mHasFeatureLeanback) {
            int i4 = 4;
            this.mKeyCombinationManager.addRule(new KeyCombinationManager.TwoKeysCombinationRule(i4, 20) { // from class: com.android.server.policy.PhoneWindowManager.11
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                long getKeyInterceptDelayMs() {
                    return 0L;
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                public void execute() {
                    PhoneWindowManager.this.mBackKeyHandled = true;
                    PhoneWindowManager.this.interceptAccessibilityGestureTv();
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                public void cancel() {
                    PhoneWindowManager.this.cancelAccessibilityGestureTv();
                }
            });
            this.mKeyCombinationManager.addRule(new KeyCombinationManager.TwoKeysCombinationRule(23, i4) { // from class: com.android.server.policy.PhoneWindowManager.12
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                long getKeyInterceptDelayMs() {
                    return 0L;
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                public void execute() {
                    PhoneWindowManager.this.mBackKeyHandled = true;
                    PhoneWindowManager.this.interceptBugreportGestureTv();
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                public void cancel() {
                    PhoneWindowManager.this.cancelBugreportGestureTv();
                }
            });
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class PowerKeyRule extends SingleKeyGestureDetector.SingleKeyRule {
        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        public /* bridge */ /* synthetic */ boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        PowerKeyRule() {
            super(26);
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        boolean supportLongPress() {
            return PhoneWindowManager.this.hasLongPressOnPowerBehavior();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        boolean supportVeryLongPress() {
            return PhoneWindowManager.this.hasVeryLongPressOnPowerBehavior();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        int getMaxMultiPressCount() {
            return PhoneWindowManager.this.getMaxMultiPressPowerCount();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onPress(long j) {
            PhoneWindowManager.this.mPhoneWindowManagerExt.getInputExtension().powerPress(j, PhoneWindowManager.this.mSingleKeyGestureDetector.beganFromNonInteractive(), 1);
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        long getLongPressTimeoutMs() {
            if (PhoneWindowManager.this.getResolvedLongPressOnPowerBehavior() == 5) {
                return PhoneWindowManager.this.mLongPressOnPowerAssistantTimeoutMs;
            }
            return super.getLongPressTimeoutMs();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onLongPress(long j) {
            if (PhoneWindowManager.this.mSingleKeyGestureDetector.beganFromNonInteractive() && !PhoneWindowManager.this.mSupportLongPressPowerWhenNonInteractive) {
                Slog.v(PhoneWindowManager.TAG, "Not support long press power when device is not interactive.");
            } else {
                PhoneWindowManager.this.powerLongPress(j);
            }
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onVeryLongPress(long j) {
            PhoneWindowManager.this.mActivityManagerInternal.prepareForPossibleShutdown();
            PhoneWindowManager.this.powerVeryLongPress();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onMultiPress(long j, int i) {
            PhoneWindowManager.this.mPhoneWindowManagerExt.getInputExtension().powerPress(j, PhoneWindowManager.this.mSingleKeyGestureDetector.beganFromNonInteractive(), i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class BackKeyRule extends SingleKeyGestureDetector.SingleKeyRule {
        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        int getMaxMultiPressCount() {
            return 1;
        }

        BackKeyRule() {
            super(4);
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        boolean supportLongPress() {
            return PhoneWindowManager.this.hasLongPressOnBackBehavior();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onPress(long j) {
            PhoneWindowManager phoneWindowManager = PhoneWindowManager.this;
            phoneWindowManager.mBackKeyHandled = PhoneWindowManager.this.backKeyPress() | phoneWindowManager.mBackKeyHandled;
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onLongPress(long j) {
            PhoneWindowManager.this.backLongPress();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class StemPrimaryKeyRule extends SingleKeyGestureDetector.SingleKeyRule {
        StemPrimaryKeyRule() {
            super(264);
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        boolean supportLongPress() {
            return PhoneWindowManager.this.hasLongPressOnStemPrimaryBehavior();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        int getMaxMultiPressCount() {
            return PhoneWindowManager.this.getMaxMultiPressStemPrimaryCount();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onPress(long j) {
            PhoneWindowManager.this.stemPrimaryPress(1);
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onLongPress(long j) {
            PhoneWindowManager.this.stemPrimaryLongPress();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onMultiPress(long j, int i) {
            PhoneWindowManager.this.stemPrimaryPress(i);
        }
    }

    private void initSingleKeyGestureRules() {
        this.mSingleKeyGestureDetector = SingleKeyGestureDetector.get(this.mContext);
        if (hasLongPressOnBackBehavior()) {
            this.mSingleKeyGestureDetector.addRule(new BackKeyRule());
        }
        if (hasStemPrimaryBehavior()) {
            this.mSingleKeyGestureDetector.addRule(new StemPrimaryKeyRule());
        }
    }

    private void readConfigurationDependentBehaviors() {
        Resources resources = this.mContext.getResources();
        this.mLongPressOnHomeBehavior = resources.getInteger(R.integer.config_ntpRetry);
        int updateConfigurationDependentBehaviors = this.mPhoneWindowManagerExt.getInputExtension().updateConfigurationDependentBehaviors(this.mLongPressOnHomeBehavior);
        this.mLongPressOnHomeBehavior = updateConfigurationDependentBehaviors;
        if (updateConfigurationDependentBehaviors < 0 || updateConfigurationDependentBehaviors > 3) {
            this.mLongPressOnHomeBehavior = 0;
        }
        int integer = resources.getInteger(R.integer.config_lowMemoryKillerMinFreeKbytesAdjust);
        this.mDoubleTapOnHomeBehavior = integer;
        if (integer < 0 || integer > 2) {
            this.mDoubleTapOnHomeBehavior = 0;
        }
        this.mShortPressOnWindowBehavior = 0;
        if (this.mPackageManager.hasSystemFeature("android.software.picture_in_picture")) {
            this.mShortPressOnWindowBehavior = 1;
        }
        this.mShortPressOnStemPrimaryBehavior = this.mContext.getResources().getInteger(R.integer.disabled_alpha_animation_duration);
        this.mLongPressOnStemPrimaryBehavior = this.mContext.getResources().getInteger(R.integer.config_overrideHasPermanentMenuKey);
        this.mDoublePressOnStemPrimaryBehavior = this.mContext.getResources().getInteger(R.integer.config_lowMemoryKillerMinFreeKbytesAbsolute);
        this.mTriplePressOnStemPrimaryBehavior = this.mContext.getResources().getInteger(R.integer.leanback_setup_translation_backward_out_content_delay);
    }

    public void updateSettings() {
        boolean z;
        ContentResolver contentResolver = this.mContext.getContentResolver();
        synchronized (this.mLock) {
            this.mEndcallBehavior = Settings.System.getIntForUser(contentResolver, "end_button_behavior", 2, -2);
            this.mIncallPowerBehavior = Settings.Secure.getIntForUser(contentResolver, "incall_power_button_behavior", 1, -2);
            this.mIncallBackBehavior = Settings.Secure.getIntForUser(contentResolver, "incall_back_button_behavior", 0, -2);
            this.mSystemNavigationKeysEnabled = Settings.Secure.getIntForUser(contentResolver, "system_navigation_keys_enabled", 0, -2) == 1;
            this.mRingerToggleChord = Settings.Secure.getIntForUser(contentResolver, "volume_hush_gesture", 0, -2);
            this.mPowerButtonSuppressionDelayMillis = Settings.Global.getInt(contentResolver, "power_button_suppression_delay_after_gesture_wake", 800);
            if (!this.mContext.getResources().getBoolean(17891903)) {
                this.mRingerToggleChord = 0;
            }
            boolean z2 = Settings.Secure.getIntForUser(contentResolver, "wake_gesture_enabled", 0, -2) != 0;
            if (this.mWakeGestureEnabledSetting != z2) {
                this.mWakeGestureEnabledSetting = z2;
                updateWakeGestureListenerLp();
            }
            this.mLockScreenTimeout = Settings.System.getIntForUser(contentResolver, "screen_off_timeout", 0, -2);
            String stringForUser = Settings.Secure.getStringForUser(contentResolver, "default_input_method", -2);
            boolean z3 = stringForUser != null && stringForUser.length() > 0;
            if (this.mHasSoftInput == z3 || !this.mPhoneWindowManagerExt.isTargetUserUnlocked(this.mCurrentUserId)) {
                z = false;
            } else {
                this.mHasSoftInput = z3;
                z = true;
            }
            int i = Settings.Global.getInt(contentResolver, "power_button_long_press", this.mContext.getResources().getInteger(R.integer.config_ntpTimeout));
            int i2 = Settings.Global.getInt(contentResolver, "power_button_very_long_press", this.mContext.getResources().getInteger(R.integer.leanback_setup_translation_forward_in_content_duration));
            if (this.mLongPressOnPowerBehavior != i || this.mVeryLongPressOnPowerBehavior != i2) {
                this.mLongPressOnPowerBehavior = i;
                this.mVeryLongPressOnPowerBehavior = i2;
            }
            this.mLongPressOnPowerAssistantTimeoutMs = Settings.Global.getLong(this.mContext.getContentResolver(), "power_button_long_press_duration_ms", this.mContext.getResources().getInteger(R.integer.config_num_physical_slots));
            this.mPowerVolUpBehavior = Settings.Global.getInt(contentResolver, "key_chord_power_volume_up", this.mContext.getResources().getInteger(R.integer.config_networkWakeupPacketMask));
            boolean z4 = Settings.Secure.getIntForUser(contentResolver, "stylus_buttons_enabled", 1, -2) == 1;
            this.mStylusButtonsEnabled = z4;
            this.mInputManagerInternal.setStylusButtonMotionEventsEnabled(z4);
        }
        if (z) {
            updateRotation(true);
        }
    }

    private DreamManagerInternal getDreamManagerInternal() {
        if (this.mDreamManagerInternal == null) {
            this.mDreamManagerInternal = (DreamManagerInternal) LocalServices.getService(DreamManagerInternal.class);
        }
        return this.mDreamManagerInternal;
    }

    private void updateWakeGestureListenerLp() {
        if (shouldEnableWakeGestureLp()) {
            this.mWakeGestureListener.requestWakeUpTrigger();
        } else {
            this.mWakeGestureListener.cancelWakeUpTrigger();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldEnableWakeGestureLp() {
        return this.mWakeGestureEnabledSetting && !this.mDefaultDisplayPolicy.isAwake() && !(getLidBehavior() == 1 && this.mDefaultDisplayPolicy.getLidState() == 0) && this.mWakeGestureListener.isSupported();
    }

    /* JADX WARN: Removed duplicated region for block: B:69:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:71:? A[RETURN, SYNTHETIC] */
    @Override // com.android.server.policy.WindowManagerPolicy
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int checkAddPermission(int i, boolean z, String str, int[] iArr) {
        ApplicationInfo applicationInfo;
        int noteOpNoThrow;
        if (z && this.mContext.checkCallingOrSelfPermission("android.permission.INTERNAL_SYSTEM_WINDOW") != 0) {
            return -8;
        }
        iArr[0] = -1;
        if ((i < 1 || i > 99) && ((i < 1000 || i > 1999) && (i < 2000 || i > 2999))) {
            return -10;
        }
        if (i < 2000 || i > 2999) {
            return 0;
        }
        if (!WindowManager.LayoutParams.isSystemAlertWindowType(i)) {
            if (i == 2005) {
                iArr[0] = 45;
                return 0;
            }
            if (i != 2011 && i != 2013 && i != 2024 && i != 2035 && i != 2037) {
                switch (i) {
                    case 2030:
                    case 2031:
                    case 2032:
                        break;
                    default:
                        return this.mContext.checkCallingOrSelfPermission("android.permission.INTERNAL_SYSTEM_WINDOW") == 0 ? 0 : -8;
                }
            }
            return 0;
        }
        iArr[0] = MSG_RINGER_TOGGLE_CHORD;
        int callingUid = Binder.getCallingUid();
        if (UserHandle.getAppId(callingUid) == 1000) {
            return 0;
        }
        try {
            applicationInfo = this.mPackageManager.getApplicationInfoAsUser(str, 0, UserHandle.getUserId(callingUid));
        } catch (PackageManager.NameNotFoundException unused) {
            applicationInfo = null;
            if (applicationInfo != null) {
            }
            if (this.mContext.checkCallingOrSelfPermission("android.permission.INTERNAL_SYSTEM_WINDOW") != 0) {
            }
        }
        if (applicationInfo != null || (i != 2038 && applicationInfo.targetSdkVersion >= 26)) {
            return this.mContext.checkCallingOrSelfPermission("android.permission.INTERNAL_SYSTEM_WINDOW") != 0 ? 0 : -8;
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.SYSTEM_APPLICATION_OVERLAY") == 0 || (noteOpNoThrow = this.mAppOpsManager.noteOpNoThrow(iArr[0], callingUid, str, (String) null, "check-add")) == 0 || noteOpNoThrow == 1) {
            return 0;
        }
        return noteOpNoThrow != 2 ? this.mContext.checkCallingOrSelfPermission("android.permission.SYSTEM_ALERT_WINDOW") == 0 ? 0 : -8 : applicationInfo.targetSdkVersion < 23 ? 0 : -8;
    }

    void readLidState() {
        this.mDefaultDisplayPolicy.setLidState(this.mWindowManagerFuncs.getLidState());
    }

    private void readCameraLensCoverState() {
        this.mCameraLensCoverState = this.mWindowManagerFuncs.getCameraLensCoverState();
    }

    private boolean isHidden(int i) {
        int lidState = this.mDefaultDisplayPolicy.getLidState();
        return i != 1 ? i == 2 && lidState == 1 : lidState == 0;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void adjustConfigurationLw(Configuration configuration, int i, int i2) {
        this.mHaveBuiltInKeyboard = (i & 1) != 0;
        readConfigurationDependentBehaviors();
        readLidState();
        if (configuration.keyboard == 1 || (i == 1 && isHidden(this.mLidKeyboardAccessibility))) {
            configuration.hardKeyboardHidden = 2;
            if (!this.mHasSoftInput) {
                configuration.keyboardHidden = 2;
            }
        }
        if (configuration.navigation == 1 || (i2 == 1 && isHidden(this.mLidNavigationAccessibility))) {
            configuration.navigationHidden = 2;
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardHostWindow(WindowManager.LayoutParams layoutParams) {
        return layoutParams.type == 2040;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public Animation createHiddenByKeyguardExit(boolean z, boolean z2, boolean z3) {
        return TransitionAnimation.createHiddenByKeyguardExit(this.mContext, this.mLogDecelerateInterpolator, z, z2, z3);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public Animation createKeyguardWallpaperExit(boolean z) {
        if (z) {
            return null;
        }
        return AnimationUtils.loadAnimation(this.mContext, R.anim.input_method_fancy_exit);
    }

    private static void awakenDreams() {
        IDreamManager dreamManager = getDreamManager();
        if (dreamManager != null) {
            try {
                dreamManager.awaken();
            } catch (RemoteException unused) {
            }
        }
    }

    static IDreamManager getDreamManager() {
        return IDreamManager.Stub.asInterface(ServiceManager.checkService("dreams"));
    }

    TelecomManager getTelecommService() {
        return (TelecomManager) this.mContext.getSystemService("telecom");
    }

    NotificationManager getNotificationService() {
        return (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
    }

    static IAudioService getAudioService() {
        IAudioService asInterface = IAudioService.Stub.asInterface(ServiceManager.checkService("audio"));
        if (asInterface == null) {
            Log.w(TAG, "Unable to find IAudioService interface.");
        }
        return asInterface;
    }

    boolean keyguardOn() {
        return isKeyguardShowingAndNotOccluded() || inKeyguardRestrictedKeyInputMode();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:37:0x00c7. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:253:0x03be  */
    @Override // com.android.server.policy.WindowManagerPolicy
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public long interceptKeyBeforeDispatching(IBinder iBinder, KeyEvent keyEvent, int i) {
        int handleHomeShortcuts;
        InputDevice device;
        boolean keyguardOn = keyguardOn();
        int keyCode = keyEvent.getKeyCode();
        int repeatCount = keyEvent.getRepeatCount();
        int metaState = keyEvent.getMetaState();
        int flags = keyEvent.getFlags();
        boolean z = keyEvent.getAction() == 0;
        boolean isCanceled = keyEvent.isCanceled();
        int displayId = keyEvent.getDisplayId();
        this.mPhoneWindowManagerExt.onSpecialKeyPressedOnTheiaMonitor(keyEvent);
        if (DEBUG_INPUT) {
            Log.d(TAG, "interceptKeyTi keyCode=" + keyCode + " down=" + z + " repeatCount=" + repeatCount + " keyguardOn=" + keyguardOn + " canceled=" + isCanceled);
        }
        if (this.mKeyCombinationManager.isKeyConsumed(keyEvent)) {
            return -1L;
        }
        if ((flags & 1024) == 0) {
            long uptimeMillis = SystemClock.uptimeMillis();
            long keyInterceptTimeout = this.mKeyCombinationManager.getKeyInterceptTimeout(keyCode);
            if (uptimeMillis < keyInterceptTimeout) {
                return keyInterceptTimeout - uptimeMillis;
            }
        }
        if (this.mPendingMetaAction && !KeyEvent.isMetaKey(keyCode)) {
            this.mPendingMetaAction = false;
        }
        if (this.mPendingCapsLockToggle && !KeyEvent.isMetaKey(keyCode) && !KeyEvent.isAltKey(keyCode)) {
            this.mPendingCapsLockToggle = false;
        }
        if (isUserSetupComplete() && !keyguardOn && this.mModifierShortcutManager.interceptKey(keyEvent)) {
            dismissKeyboardShortcutsMenu();
            this.mPendingMetaAction = false;
            this.mPendingCapsLockToggle = false;
            return -1L;
        }
        switch (keyCode) {
            case 3:
                handleHomeShortcuts = handleHomeShortcuts(displayId, iBinder, keyEvent);
                return handleHomeShortcuts;
            case 19:
                if (z && keyEvent.isMetaPressed() && keyEvent.isCtrlPressed() && repeatCount == 0) {
                    StatusBarManagerInternal statusBarManagerInternal = getStatusBarManagerInternal();
                    if (statusBarManagerInternal != null) {
                        statusBarManagerInternal.goToFullscreenFromSplit();
                    }
                    return -1L;
                }
                return ((isValidGlobalKey(keyCode) || !this.mGlobalKeyManager.handleGlobalKey(this.mContext, keyCode, keyEvent)) && (65536 & metaState) == 0) ? 0L : -1L;
            case MSG_SYSTEM_KEY_PRESS /* 21 */:
                if (z && keyEvent.isMetaPressed() && keyEvent.isCtrlPressed() && repeatCount == 0) {
                    enterStageSplitFromRunningApp(true);
                    return -1L;
                }
                if (isValidGlobalKey(keyCode)) {
                    break;
                }
            case MSG_HANDLE_ALL_APPS /* 22 */:
                if (z && keyEvent.isMetaPressed() && keyEvent.isCtrlPressed() && repeatCount == 0) {
                    enterStageSplitFromRunningApp(false);
                    return -1L;
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case MSG_RINGER_TOGGLE_CHORD /* 24 */:
            case MSG_SWITCH_KEYBOARD_LAYOUT /* 25 */:
            case 164:
                if (this.mUseTvRouting || this.mHandleVolumeKeysInWM) {
                    dispatchDirectAudioEvent(keyEvent);
                    return -1L;
                }
                if (this.mDefaultDisplayPolicy.isPersistentVrModeEnabled() && (device = keyEvent.getDevice()) != null && !device.isExternal()) {
                    return -1L;
                }
                if (!this.mPowerManager.isInteractive() && this.mPhoneWindowManagerExt.skipVolumeKeyIfNeeded()) {
                    return -1L;
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case HdmiCecKeycode.CEC_KEYCODE_NUMBER_ENTRY_MODE /* 29 */:
                if (z && keyEvent.isMetaPressed()) {
                    launchAssistAction("android.intent.extra.ASSIST_INPUT_HINT_KEYBOARD", keyEvent.getDeviceId(), keyEvent.getEventTime(), 0, -1);
                    return -1L;
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case 36:
            case HdmiCecKeycode.CEC_KEYCODE_VOLUME_DOWN /* 66 */:
                if (keyEvent.isMetaPressed()) {
                    handleHomeShortcuts = handleHomeShortcuts(displayId, iBinder, keyEvent);
                    return handleHomeShortcuts;
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case HdmiCecKeycode.CEC_KEYCODE_NUMBERS_5 /* 37 */:
                if (z && keyEvent.isMetaPressed()) {
                    showSystemSettings();
                    return -1L;
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case HdmiCecKeycode.CEC_KEYCODE_DOT /* 42 */:
                if (z && keyEvent.isMetaPressed()) {
                    if (keyEvent.isCtrlPressed()) {
                        sendSystemKeyToStatusBarAsync(keyEvent);
                    } else {
                        toggleNotificationPanel();
                    }
                    return -1L;
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case HdmiCecKeycode.CEC_KEYCODE_NEXT_FAVORITE /* 47 */:
                if (z && keyEvent.isMetaPressed() && keyEvent.isCtrlPressed() && repeatCount == 0) {
                    interceptScreenshotChord(2, 0L);
                    return -1L;
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case 48:
                if (z && keyEvent.isMetaPressed()) {
                    toggleTaskbar();
                    return -1L;
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case 57:
            case 58:
                if (z) {
                    if (keyEvent.isMetaPressed()) {
                        this.mPendingCapsLockToggle = true;
                        this.mPendingMetaAction = false;
                    } else {
                        this.mPendingCapsLockToggle = false;
                    }
                } else {
                    int i2 = this.mRecentAppsHeldModifiers;
                    if (i2 != 0 && (i2 & metaState) == 0) {
                        this.mRecentAppsHeldModifiers = 0;
                        hideRecentApps(true, false);
                        return -1L;
                    }
                    if (this.mPendingCapsLockToggle) {
                        this.mInputManagerInternal.toggleCapsLock(keyEvent.getDeviceId());
                        this.mPendingCapsLockToggle = false;
                        return -1L;
                    }
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case 61:
                if (z && keyEvent.isMetaPressed()) {
                    if (!keyguardOn && isUserSetupComplete()) {
                        showRecentApps(false);
                        return -1L;
                    }
                } else if (z && repeatCount == 0 && this.mRecentAppsHeldModifiers == 0 && !keyguardOn && isUserSetupComplete()) {
                    int modifiers = keyEvent.getModifiers() & (-194);
                    if (KeyEvent.metaStateHasModifiers(modifiers, 2)) {
                        this.mRecentAppsHeldModifiers = modifiers;
                        showRecentApps(true);
                        return -1L;
                    }
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case 62:
                if ((458752 & metaState) == 0) {
                    return 0L;
                }
                if (z && repeatCount == 0) {
                    sendSwitchKeyboardLayout(keyEvent, (metaState & HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_STEP_PLUS) != 0 ? -1 : 1);
                    return -1L;
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case HdmiCecKeycode.CEC_KEYCODE_BACKWARD /* 76 */:
                if (z && repeatCount == 0 && keyEvent.isMetaPressed() && !keyguardOn) {
                    toggleKeyboardShortcutsMenu(keyEvent.getDeviceId());
                    return -1L;
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case HdmiCecKeycode.CEC_KEYCODE_VIDEO_ON_DEMAND /* 82 */:
                if (z && repeatCount == 0 && this.mEnableShiftMenuBugReports && (metaState & 1) == 1) {
                    this.mContext.sendOrderedBroadcastAsUser(new Intent("android.intent.action.BUG_REPORT"), UserHandle.CURRENT, null, null, null, 0, null, null);
                    return -1L;
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case HdmiCecKeycode.CEC_KEYCODE_ELECTRONIC_PROGRAM_GUIDE /* 83 */:
                if (!z) {
                    toggleNotificationPanel();
                }
                return -1L;
            case HdmiCecKeycode.CEC_KEYCODE_TIMER_PROGRAMMING /* 84 */:
                if (z && repeatCount == 0 && !keyguardOn() && this.mSearchKeyBehavior == 1) {
                    launchTargetSearchActivity();
                    return -1L;
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case HdmiCecKeycode.CEC_KEYCODE_F5 /* 117 */:
            case HdmiCecKeycode.CEC_KEYCODE_DATA /* 118 */:
                if (z) {
                    if (keyEvent.isAltPressed()) {
                        this.mPendingCapsLockToggle = true;
                        this.mPendingMetaAction = false;
                    } else {
                        this.mPendingCapsLockToggle = false;
                        this.mPendingMetaAction = true;
                    }
                } else if (this.mPendingCapsLockToggle) {
                    this.mInputManagerInternal.toggleCapsLock(keyEvent.getDeviceId());
                    this.mPendingCapsLockToggle = false;
                } else if (this.mPendingMetaAction) {
                    if (!isCanceled) {
                        launchAssistAction("android.intent.extra.ASSIST_INPUT_HINT_KEYBOARD", keyEvent.getDeviceId(), keyEvent.getEventTime(), 0, 1);
                    }
                    this.mPendingMetaAction = false;
                }
                return -1L;
            case 187:
                this.mPhoneWindowManagerExt.hookForInputLogV("KEYCODE_APP_SWITCH keyguardOn : " + keyguardOn);
                if (!keyguardOn) {
                    if (z && repeatCount == 0) {
                        preloadRecentApps();
                    } else if (!z) {
                        toggleRecentApps();
                    }
                }
                return -1L;
            case 204:
                if (z && repeatCount == 0) {
                    sendSwitchKeyboardLayout(keyEvent, (metaState & HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_STEP_PLUS) != 0 ? -1 : 1);
                    return -1L;
                }
                if (isValidGlobalKey(keyCode)) {
                }
            case 219:
                Slog.wtf(TAG, "KEYCODE_ASSIST should be handled in interceptKeyBeforeQueueing");
                return -1L;
            case UsbDescriptor.CLASSID_DIAGNOSTIC /* 220 */:
            case 221:
                if (z) {
                    int i3 = keyCode == 221 ? 1 : -1;
                    if (Settings.System.getIntForUser(this.mContext.getContentResolver(), "screen_brightness_mode", 0, -3) != 0) {
                        Settings.System.putIntForUser(this.mContext.getContentResolver(), "screen_brightness_mode", 0, -3);
                    }
                    if (displayId < 0) {
                        displayId = 0;
                    }
                    this.mDisplayManager.setBrightness(displayId, MathUtils.constrain(BrightnessUtils.convertGammaToLinear(MathUtils.constrain(BrightnessUtils.convertLinearToGamma(this.mDisplayManager.getBrightness(displayId)) + (i3 * 0.1f), 0.0f, 1.0f)), this.mPowerManager.getBrightnessConstraint(0), this.mPowerManager.getBrightnessConstraint(1)));
                    this.mPhoneWindowManagerExt.adjustBrightnessUpOrDownEvent(10, i3);
                    startActivityAsUser(new Intent("com.android.intent.action.SHOW_BRIGHTNESS_DIALOG"), UserHandle.CURRENT_OR_SELF);
                }
                return -1L;
            case 231:
                Slog.wtf(TAG, "KEYCODE_VOICE_ASSIST should be handled in interceptKeyBeforeQueueing");
                return -1L;
            case 284:
                if (!z) {
                    this.mHandler.removeMessages(MSG_HANDLE_ALL_APPS);
                    Message obtainMessage = this.mHandler.obtainMessage(MSG_HANDLE_ALL_APPS);
                    obtainMessage.setAsynchronous(true);
                    obtainMessage.sendToTarget();
                }
                return -1L;
            case 289:
            case 290:
            case 291:
            case 292:
            case 293:
            case 294:
            case 295:
            case 296:
            case 297:
            case 298:
            case 299:
            case 300:
            case IOplusLocationStatistics.GNSS_STRATEGY_OF_ACTIVITY_STOP /* 301 */:
            case IOplusLocationStatistics.GNSS_STRATEGY_OF_GPS_IN_DOOR /* 302 */:
            case 303:
            case 304:
                Slog.wtf(TAG, "KEYCODE_APP_X should be handled in interceptKeyBeforeQueueing");
                return -1L;
            case 305:
                if (z) {
                    this.mInputManagerInternal.decrementKeyboardBacklight(keyEvent.getDeviceId());
                }
                return -1L;
            case 306:
                if (z) {
                    this.mInputManagerInternal.incrementKeyboardBacklight(keyEvent.getDeviceId());
                }
                return -1L;
            case 307:
                return -1L;
            case 308:
            case 309:
            case 310:
            case 311:
                Slog.wtf(TAG, "KEYCODE_STYLUS_BUTTON_* should be handled in interceptKeyBeforeQueueing");
                return -1L;
            case 312:
                if (z && repeatCount == 0) {
                    showRecentApps(false);
                }
                return -1L;
            default:
                if (isValidGlobalKey(keyCode)) {
                }
        }
    }

    private int handleHomeShortcuts(int i, IBinder iBinder, KeyEvent keyEvent) {
        DisplayHomeButtonHandler displayHomeButtonHandler = this.mDisplayHomeButtonHandlers.get(i);
        if (displayHomeButtonHandler == null) {
            displayHomeButtonHandler = new DisplayHomeButtonHandler(i);
            this.mDisplayHomeButtonHandlers.put(i, displayHomeButtonHandler);
        }
        return displayHomeButtonHandler.handleHomeButton(iBinder, keyEvent);
    }

    private void toggleMicrophoneMuteFromKey() {
        if (this.mSensorPrivacyManager.supportsSensorToggle(1, 1)) {
            boolean isSensorPrivacyEnabled = this.mSensorPrivacyManager.isSensorPrivacyEnabled(1, 1);
            this.mSensorPrivacyManager.setSensorPrivacy(1, !isSensorPrivacyEnabled);
            Toast.makeText(this.mContext, UiThread.get().getLooper(), this.mContext.getString(isSensorPrivacyEnabled ? R.string.permdesc_sdcardWrite : R.string.permdesc_sdcardRead), 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void interceptBugreportGestureTv() {
        this.mHandler.removeMessages(18);
        Message obtain = Message.obtain(this.mHandler, 18);
        obtain.setAsynchronous(true);
        this.mHandler.sendMessageDelayed(obtain, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelBugreportGestureTv() {
        this.mHandler.removeMessages(18);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void interceptAccessibilityGestureTv() {
        this.mHandler.removeMessages(19);
        Message obtain = Message.obtain(this.mHandler, 19);
        obtain.setAsynchronous(true);
        this.mHandler.sendMessageDelayed(obtain, getAccessibilityShortcutTimeout());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAccessibilityGestureTv() {
        this.mHandler.removeMessages(19);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestBugreportForTv() {
        if ("1".equals(SystemProperties.get("ro.debuggable")) || Settings.Global.getInt(this.mContext.getContentResolver(), "development_settings_enabled", 0) == 1) {
            try {
                if (ActivityManager.getService().launchBugReportHandlerApp()) {
                    return;
                }
                ActivityManager.getService().requestInteractiveBugReport();
            } catch (RemoteException e) {
                Slog.e(TAG, "Error taking bugreport", e);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0148  */
    @Override // com.android.server.policy.WindowManagerPolicy
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public KeyEvent dispatchUnhandledKey(IBinder iBinder, KeyEvent keyEvent, int i) {
        KeyEvent keyEvent2;
        KeyCharacterMap.FallbackAction fallbackAction;
        if (DEBUG_INPUT) {
            KeyInterceptionInfo keyInterceptionInfoFromToken = this.mWindowManagerInternal.getKeyInterceptionInfoFromToken(iBinder);
            Slog.d(TAG, "Unhandled key: inputToken=" + iBinder + ", title=" + (keyInterceptionInfoFromToken == null ? "<unknown>" : keyInterceptionInfoFromToken.windowTitle) + ", action=" + keyEvent.getAction() + ", flags=" + keyEvent.getFlags() + ", keyCode=" + keyEvent.getKeyCode() + ", scanCode=" + keyEvent.getScanCode() + ", metaState=" + keyEvent.getMetaState() + ", repeatCount=" + keyEvent.getRepeatCount() + ", policyFlags=" + i);
        }
        if (interceptUnhandledKey(keyEvent)) {
            return null;
        }
        if ((keyEvent.getFlags() & 1024) == 0) {
            KeyCharacterMap keyCharacterMap = keyEvent.getKeyCharacterMap();
            int keyCode = keyEvent.getKeyCode();
            int metaState = keyEvent.getMetaState();
            boolean z = keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0;
            if (z) {
                fallbackAction = keyCharacterMap.getFallbackAction(keyCode, metaState);
            } else {
                fallbackAction = this.mFallbackActions.get(keyCode);
            }
            if (fallbackAction != null) {
                if (DEBUG_INPUT) {
                    Slog.d(TAG, "Fallback: keyCode=" + fallbackAction.keyCode + " metaState=" + Integer.toHexString(fallbackAction.metaState));
                }
                keyEvent2 = KeyEvent.obtain(keyEvent.getDownTime(), keyEvent.getEventTime(), keyEvent.getAction(), fallbackAction.keyCode, keyEvent.getRepeatCount(), fallbackAction.metaState, keyEvent.getDeviceId(), keyEvent.getScanCode(), keyEvent.getFlags() | 1024, keyEvent.getSource(), keyEvent.getDisplayId(), null);
                if (!interceptFallback(iBinder, keyEvent2, i)) {
                    keyEvent2.recycle();
                    keyEvent2 = null;
                }
                if (z) {
                    this.mFallbackActions.put(keyCode, fallbackAction);
                } else if (keyEvent.getAction() == 1) {
                    this.mFallbackActions.remove(keyCode);
                    fallbackAction.recycle();
                }
                if (DEBUG_INPUT) {
                    if (keyEvent2 == null) {
                        Slog.d(TAG, "No fallback.");
                    } else {
                        Slog.d(TAG, "Performing fallback: " + keyEvent2);
                    }
                }
                return keyEvent2;
            }
        }
        keyEvent2 = null;
        if (DEBUG_INPUT) {
        }
        return keyEvent2;
    }

    private boolean interceptUnhandledKey(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        int repeatCount = keyEvent.getRepeatCount();
        boolean z = keyEvent.getAction() == 0;
        int modifiers = keyEvent.getModifiers();
        if (keyCode != 54) {
            if (keyCode != 62) {
                if (keyCode == 111) {
                    if (z && repeatCount == 0) {
                        this.mContext.closeSystemDialogs();
                    }
                    return true;
                }
                if (keyCode == 120) {
                    if (z && repeatCount == 0) {
                        interceptScreenshotChord(2, 0L);
                    }
                    return true;
                }
            } else if (z && repeatCount == 0 && KeyEvent.metaStateHasModifiers(modifiers & (-194), 4096)) {
                sendSwitchKeyboardLayout(keyEvent, (modifiers & HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_STEP_PLUS) != 0 ? -1 : 1);
                return true;
            }
        } else if (z && KeyEvent.metaStateHasModifiers(modifiers, UsbACInterface.FORMAT_II_AC3) && this.mAccessibilityShortcutController.isAccessibilityShortcutAvailable(isKeyguardLocked())) {
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(17));
            return true;
        }
        return false;
    }

    private void sendSwitchKeyboardLayout(KeyEvent keyEvent, int i) {
        this.mHandler.obtainMessage(MSG_SWITCH_KEYBOARD_LAYOUT, keyEvent.getDeviceId(), i).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSwitchKeyboardLayout(int i, int i2) {
        if (FeatureFlagUtils.isEnabled(this.mContext, "settings_new_keyboard_ui")) {
            InputMethodManagerInternal.get().switchKeyboardLayout(i2);
        } else {
            this.mWindowManagerFuncs.switchKeyboardLayout(i, i2);
        }
    }

    private boolean interceptFallback(IBinder iBinder, KeyEvent keyEvent, int i) {
        return ((interceptKeyBeforeQueueing(keyEvent, i) & 1) == 0 || interceptKeyBeforeDispatching(iBinder, keyEvent, i) != 0 || interceptUnhandledKey(keyEvent)) ? false : true;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setTopFocusedDisplay(int i) {
        this.mTopFocusedDisplayId = i;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void registerDisplayFoldListener(IDisplayFoldListener iDisplayFoldListener) {
        DisplayFoldController displayFoldController = this.mDisplayFoldController;
        if (displayFoldController != null) {
            displayFoldController.registerDisplayFoldListener(iDisplayFoldListener);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void unregisterDisplayFoldListener(IDisplayFoldListener iDisplayFoldListener) {
        DisplayFoldController displayFoldController = this.mDisplayFoldController;
        if (displayFoldController != null) {
            displayFoldController.unregisterDisplayFoldListener(iDisplayFoldListener);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setOverrideFoldedArea(Rect rect) {
        DisplayFoldController displayFoldController = this.mDisplayFoldController;
        if (displayFoldController != null) {
            displayFoldController.setOverrideFoldedArea(rect);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public Rect getFoldedArea() {
        DisplayFoldController displayFoldController = this.mDisplayFoldController;
        if (displayFoldController != null) {
            return displayFoldController.getFoldedArea();
        }
        return new Rect();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void onDefaultDisplayFocusChangedLw(WindowManagerPolicy.WindowState windowState) {
        DisplayFoldController displayFoldController = this.mDisplayFoldController;
        if (displayFoldController != null) {
            displayFoldController.onDefaultDisplayFocusChanged(windowState != null ? windowState.getOwningPackage() : null);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void registerShortcutKey(long j, IShortcutService iShortcutService) throws RemoteException {
        synchronized (this.mLock) {
            this.mModifierShortcutManager.registerShortcutKey(j, iShortcutService);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void onKeyguardOccludedChangedLw(boolean z) {
        if (this.mKeyguardDelegate != null) {
            this.mPendingKeyguardOccluded = z;
            this.mKeyguardOccludedChanged = true;
            return;
        }
        if (this.mKeyguardOccludedChanged && this.mPendingKeyguardOccluded != z) {
            Slog.d(TAG, "force set mPendingKeyguardOccluded from " + this.mPendingKeyguardOccluded + " to " + z);
            this.mPendingKeyguardOccluded = z;
        }
        setKeyguardOccludedLw(z);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public int applyKeyguardOcclusionChange() {
        if (this.mPhoneWindowManagerExt.applyKeyguardOcclusionChange(this.mKeyguardOccludedChanged)) {
            return 0;
        }
        if (DEBUG_KEYGUARD) {
            Slog.d(TAG, "transition/occluded commit occluded=" + this.mPendingKeyguardOccluded + " changed=" + this.mKeyguardOccludedChanged);
        }
        return setKeyguardOccludedLw(this.mPendingKeyguardOccluded) ? 5 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int handleTransitionForKeyguardLw(boolean z, boolean z2) {
        int applyKeyguardOcclusionChange;
        if (z2 && (applyKeyguardOcclusionChange = applyKeyguardOcclusionChange()) != 0) {
            return applyKeyguardOcclusionChange;
        }
        if (!z) {
            return 0;
        }
        if (DEBUG_KEYGUARD) {
            Slog.d(TAG, "Starting keyguard exit animation");
        }
        startKeyguardExitAnimation(SystemClock.uptimeMillis());
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchAssistAction(String str, int i, long j, int i2, int i3) {
        sendCloseSystemWindows(SYSTEM_DIALOG_REASON_ASSIST);
        if (isUserSetupComplete()) {
            Bundle bundle = new Bundle();
            if (i > Integer.MIN_VALUE) {
                bundle.putInt("android.intent.extra.ASSIST_INPUT_DEVICE_ID", i);
            }
            if (str != null) {
                bundle.putBoolean(str, true);
            }
            bundle.putLong("android.intent.extra.TIME", j);
            bundle.putInt("invocation_type", i2);
            this.mPhoneWindowManagerExt.getInputExtension().handleAssistLaunchMode(i3, bundle);
            ((SearchManager) this.mContext.createContextAsUser(UserHandle.of(this.mCurrentUserId), 0).getSystemService("search")).launchAssist(bundle);
        }
    }

    private void launchVoiceAssist(boolean z) {
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (!(keyguardServiceDelegate != null && keyguardServiceDelegate.isShowing())) {
            if (this.mHasFeatureWatch && isInRetailMode()) {
                launchRetailVoiceAssist(z);
                return;
            } else {
                startVoiceAssistIntent(z);
                return;
            }
        }
        this.mKeyguardDelegate.dismissKeyguardToLaunch(new Intent("android.intent.action.VOICE_ASSIST"));
    }

    private void launchRetailVoiceAssist(boolean z) {
        Intent intent = new Intent(ACTION_VOICE_ASSIST_RETAIL);
        ResolveInfo resolveActivity = this.mContext.getPackageManager().resolveActivity(intent, 0);
        if (resolveActivity != null) {
            ActivityInfo activityInfo = resolveActivity.activityInfo;
            intent.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
            startActivityAsUser(intent, null, UserHandle.CURRENT_OR_SELF, z);
        } else {
            Slog.w(TAG, "Couldn't find an app to process android.intent.action.VOICE_ASSIST_RETAIL. Fall back to start android.intent.action.VOICE_ASSIST");
            startVoiceAssistIntent(z);
        }
    }

    private void startVoiceAssistIntent(boolean z) {
        startActivityAsUser(new Intent("android.intent.action.VOICE_ASSIST"), null, UserHandle.CURRENT_OR_SELF, z);
    }

    private boolean isInRetailMode() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "device_demo_mode", 0) == 1;
    }

    private void startActivityAsUser(Intent intent, UserHandle userHandle) {
        startActivityAsUser(intent, null, userHandle);
    }

    private void startActivityAsUser(Intent intent, Bundle bundle, UserHandle userHandle) {
        startActivityAsUser(intent, bundle, userHandle, false);
    }

    private void startActivityAsUser(Intent intent, Bundle bundle, UserHandle userHandle, boolean z) {
        if (z || isUserSetupComplete()) {
            this.mContext.startActivityAsUser(intent, bundle, userHandle);
            return;
        }
        Slog.i(TAG, "Not starting activity because user setup is in progress: " + intent);
    }

    private SearchManager getSearchManager() {
        if (this.mSearchManager == null) {
            this.mSearchManager = (SearchManager) this.mContext.getSystemService("search");
        }
        return this.mSearchManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void preloadRecentApps() {
        this.mPreloadedRecentApps = true;
        StatusBarManagerInternal statusBarManagerInternal = getStatusBarManagerInternal();
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.preloadRecentApps();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelPreloadRecentApps() {
        if (this.mPreloadedRecentApps) {
            this.mPreloadedRecentApps = false;
            StatusBarManagerInternal statusBarManagerInternal = getStatusBarManagerInternal();
            if (statusBarManagerInternal != null) {
                statusBarManagerInternal.cancelPreloadRecentApps();
            }
        }
    }

    private void toggleTaskbar() {
        StatusBarManagerInternal statusBarManagerInternal = getStatusBarManagerInternal();
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.toggleTaskbar();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleRecentApps() {
        this.mPreloadedRecentApps = false;
        StatusBarManagerInternal statusBarManagerInternal = getStatusBarManagerInternal();
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.toggleRecentApps();
            this.mPhoneWindowManagerExt.onRecentClicked();
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void showRecentApps() {
        this.mHandler.removeMessages(9);
        this.mHandler.obtainMessage(9).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showRecentApps(boolean z) {
        this.mPreloadedRecentApps = false;
        StatusBarManagerInternal statusBarManagerInternal = getStatusBarManagerInternal();
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.showRecentApps(z);
        }
    }

    private void toggleKeyboardShortcutsMenu(int i) {
        StatusBarManagerInternal statusBarManagerInternal = getStatusBarManagerInternal();
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.toggleKeyboardShortcutsMenu(i);
        }
    }

    private void dismissKeyboardShortcutsMenu() {
        StatusBarManagerInternal statusBarManagerInternal = getStatusBarManagerInternal();
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.dismissKeyboardShortcutsMenu();
        }
    }

    private void hideRecentApps(boolean z, boolean z2) {
        this.mPreloadedRecentApps = false;
        StatusBarManagerInternal statusBarManagerInternal = getStatusBarManagerInternal();
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.hideRecentApps(z, z2);
        }
    }

    private void enterStageSplitFromRunningApp(boolean z) {
        StatusBarManagerInternal statusBarManagerInternal = getStatusBarManagerInternal();
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.enterStageSplitFromRunningApp(z);
        }
    }

    void launchHomeFromHotKey(int i) {
        launchHomeFromHotKey(i, true, true);
    }

    void launchHomeFromHotKey(final int i, final boolean z, boolean z2) {
        this.mPhoneWindowManagerExt.hookForInputLogV("launchHomeFromHotKey displayId=" + i + ", awaken=" + z + ", respect=" + z2 + ", recentsvisible=" + this.mRecentsVisible);
        if (z2) {
            if (isKeyguardShowingAndNotOccluded()) {
                Log.i(TAG, "Don't launch home because the Keyguard is showing, isKeyguardShowingAndNotOccluded=" + isKeyguardShowingAndNotOccluded());
                return;
            }
            if (!isKeyguardOccluded() && this.mKeyguardDelegate.isInputRestricted()) {
                this.mKeyguardDelegate.verifyUnlock(new WindowManagerPolicy.OnKeyguardExitResult() { // from class: com.android.server.policy.PhoneWindowManager.13
                    @Override // com.android.server.policy.WindowManagerPolicy.OnKeyguardExitResult
                    public void onKeyguardExitResult(boolean z3) {
                        if (z3) {
                            long clearCallingIdentity = Binder.clearCallingIdentity();
                            try {
                                PhoneWindowManager.this.startDockOrHome(i, true, z);
                            } finally {
                                Binder.restoreCallingIdentity(clearCallingIdentity);
                            }
                        }
                    }
                });
                Log.i(TAG, "now is in keyguard restricted mode , must unlock before launching home.");
                return;
            }
        }
        if (this.mRecentsVisible) {
            try {
                ActivityManager.getService().stopAppSwitches();
            } catch (RemoteException unused) {
            }
            if (z) {
                awakenDreams();
            }
            hideRecentApps(false, true);
            return;
        }
        startDockOrHome(i, true, z);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setRecentsVisibilityLw(boolean z) {
        this.mRecentsVisible = z;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setPipVisibilityLw(boolean z) {
        this.mPictureInPictureVisible = z;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setNavBarVirtualKeyHapticFeedbackEnabledLw(boolean z) {
        this.mNavBarVirtualKeyHapticFeedbackEnabled = z;
    }

    private boolean setKeyguardOccludedLw(boolean z) {
        Slog.d(TAG, "setKeyguardOccludedLw occluded=" + z + "," + isKeyguardOccluded() + "," + this.mKeyguardOccludedChanged);
        this.mKeyguardOccludedChanged = false;
        this.mPendingKeyguardOccluded = z;
        this.mKeyguardDelegate.setOccluded(z, true);
        return this.mKeyguardDelegate.isShowing();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void notifyLidSwitchChanged(long j, boolean z) {
        if (z == this.mDefaultDisplayPolicy.getLidState()) {
            return;
        }
        this.mDefaultDisplayPolicy.setLidState(z ? 1 : 0);
        applyLidSwitchState();
        updateRotation(true);
        if (z) {
            wakeUp(SystemClock.uptimeMillis(), this.mAllowTheaterModeWakeFromLidSwitch, 9, "android.policy:LID");
        } else if (getLidBehavior() != 1) {
            this.mPowerManager.userActivity(SystemClock.uptimeMillis(), false);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void notifyCameraLensCoverSwitchChanged(long j, boolean z) {
        Intent intent;
        if (this.mCameraLensCoverState != z && this.mContext.getResources().getBoolean(17891733)) {
            if (this.mCameraLensCoverState == 1 && !z) {
                KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
                if (keyguardServiceDelegate == null ? false : keyguardServiceDelegate.isShowing()) {
                    intent = new Intent("android.media.action.STILL_IMAGE_CAMERA_SECURE");
                } else {
                    intent = new Intent("android.media.action.STILL_IMAGE_CAMERA");
                }
                wakeUp(j / 1000000, this.mAllowTheaterModeWakeFromCameraLens, 5, "android.policy:CAMERA_COVER");
                startActivityAsUser(intent, UserHandle.CURRENT_OR_SELF);
            }
            this.mCameraLensCoverState = z ? 1 : 0;
        }
    }

    void initializeHdmiState() {
        int allowThreadDiskReadsMask = StrictMode.allowThreadDiskReadsMask();
        try {
            initializeHdmiStateInternal();
        } finally {
            StrictMode.setThreadPolicyMask(allowThreadDiskReadsMask);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0078, code lost:
    
        if (r1 == null) goto L41;
     */
    /* JADX WARN: Removed duplicated region for block: B:33:0x007f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void initializeHdmiStateInternal() {
        NumberFormatException e;
        IOException e2;
        this.mPhoneWindowManagerSocExt.hookInitializeHdmiStateInternal();
        UEventObserver uEventObserver = null;
        boolean z = false;
        if (new File("/sys/devices/virtual/switch/hdmi/state").exists()) {
            UEventObserver uEventObserver2 = this.mHDMIObserver;
            uEventObserver2.startObserving("DEVPATH=/devices/virtual/switch/hdmi");
            try {
                try {
                    uEventObserver2 = new FileReader("/sys/class/switch/hdmi/state");
                } catch (IOException e3) {
                    e2 = e3;
                    uEventObserver2 = null;
                } catch (NumberFormatException e4) {
                    e = e4;
                    uEventObserver2 = null;
                } catch (Throwable th) {
                    th = th;
                    if (uEventObserver != null) {
                    }
                    throw th;
                }
                try {
                    char[] cArr = new char[15];
                    int read = uEventObserver2.read(cArr);
                    if (read > 1) {
                        if (Integer.parseInt(new String(cArr, 0, read - 1)) != 0) {
                            z = true;
                        }
                    }
                } catch (IOException e5) {
                    e2 = e5;
                    Slog.w(TAG, "Couldn't read hdmi state from /sys/class/switch/hdmi/state: " + e2);
                } catch (NumberFormatException e6) {
                    e = e6;
                    Slog.w(TAG, "Couldn't read hdmi state from /sys/class/switch/hdmi/state: " + e);
                    if (uEventObserver2 != null) {
                        uEventObserver2.close();
                    }
                    this.mDefaultDisplayPolicy.setHdmiPlugged(z, true);
                }
                try {
                    uEventObserver2.close();
                } catch (IOException unused) {
                }
            } catch (Throwable th2) {
                th = th2;
                uEventObserver = uEventObserver2;
                if (uEventObserver != null) {
                    try {
                        uEventObserver.close();
                    } catch (IOException unused2) {
                    }
                }
                throw th;
            }
        } else {
            List extconInfoForTypes = ExtconUEventObserver.ExtconInfo.getExtconInfoForTypes(new String[]{"HDMI"});
            if (!extconInfoForTypes.isEmpty()) {
                HdmiVideoExtconUEventObserver hdmiVideoExtconUEventObserver = new HdmiVideoExtconUEventObserver();
                z = hdmiVideoExtconUEventObserver.init((ExtconUEventObserver.ExtconInfo) extconInfoForTypes.get(0));
                this.mHDMIObserver = hdmiVideoExtconUEventObserver;
            } else if (localLOGV) {
                Slog.v(TAG, "Not observing HDMI plug state because HDMI was not found.");
            }
        }
        this.mDefaultDisplayPolicy.setHdmiPlugged(z, true);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:267:0x0433, code lost:
    
        if (r30.mBackKeyHandled != false) goto L168;
     */
    /* JADX WARN: Removed duplicated region for block: B:273:0x018a  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0125  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0168  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x017d  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0188  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x018d  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01a8 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01a9  */
    @Override // com.android.server.policy.WindowManagerPolicy
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int interceptKeyBeforeQueueing(KeyEvent keyEvent, int i) {
        int i2;
        TelecomManager telecommService;
        int i3;
        int i4;
        boolean z;
        HdmiControl hdmiControl;
        int keyCode = keyEvent.getKeyCode();
        boolean z2 = keyEvent.getAction() == 0;
        boolean z3 = (i & 1) != 0 || keyEvent.isWakeKey();
        this.mPhoneWindowManagerExt.onPowerKeyPressedOnTheiaMonitor(keyEvent);
        if (!this.mSystemBooted) {
            if (z2 && (keyCode == 26 || keyCode == 177)) {
                wakeUpFromPowerKey(keyEvent.getDownTime());
            } else {
                if (!z2 || ((!z3 && keyCode != 224) || !isWakeKeyWhenScreenOff(keyCode))) {
                    z = false;
                    if (z && (hdmiControl = getHdmiControl()) != null) {
                        hdmiControl.turnOnTv();
                    }
                    return 0;
                }
                wakeUpFromWakeKey(keyEvent);
            }
            z = true;
            if (z) {
                hdmiControl.turnOnTv();
            }
            return 0;
        }
        boolean z4 = (536870912 & i) != 0;
        boolean isCanceled = keyEvent.isCanceled();
        int displayId = keyEvent.getDisplayId();
        boolean z5 = (16777216 & i) != 0;
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        Log.d(TAG, "interceptKeyTq keycode=" + keyCode + " interactive=" + z4 + " keyguardActive=" + (keyguardServiceDelegate != null && (!z4 ? !keyguardServiceDelegate.isShowing() : !isKeyguardShowingAndNotOccluded())) + " policyFlags=" + Integer.toHexString(i));
        if (z4 || (z5 && !z3)) {
            if (z4) {
                int i5 = (keyCode != this.mPendingWakeKey || z2) ? 1 : 0;
                this.mPendingWakeKey = -1;
                i2 = i5;
                z3 = false;
                if (!isValidGlobalKey(keyCode) && this.mGlobalKeyManager.shouldHandleGlobalKey(keyCode)) {
                    if (!z4 && z3 && z2 && this.mGlobalKeyManager.shouldDispatchFromNonInteractive(keyCode)) {
                        this.mGlobalKeyManager.setBeganFromNonInteractive();
                        this.mPendingWakeKey = -1;
                        i4 = 1;
                    } else {
                        i4 = i2;
                    }
                    if (z3) {
                        wakeUpFromWakeKey(keyEvent);
                    }
                    return i4;
                }
                HdmiControlManager hdmiControlManager = getHdmiControlManager();
                if (keyCode != 177 && this.mHasFeatureLeanback && (hdmiControlManager == null || !hdmiControlManager.shouldHandleTvPowerKey())) {
                    return interceptKeyBeforeQueueing(KeyEvent.obtain(keyEvent.getDownTime(), keyEvent.getEventTime(), keyEvent.getAction(), 26, keyEvent.getRepeatCount(), keyEvent.getMetaState(), keyEvent.getDeviceId(), keyEvent.getScanCode(), keyEvent.getFlags(), keyEvent.getSource(), keyEvent.getDisplayId(), null), i);
                }
                boolean z6 = !z4 && this.mPhoneWindowManagerExt.isDisplaysOnLocked(this.mDefaultDisplay);
                if ((keyEvent.getFlags() & 1024) == 0) {
                    handleKeyGesture(keyEvent, z6);
                }
                boolean z7 = (z2 || (i & 2) == 0 || (((keyEvent.getFlags() & 64) == 0) && !this.mNavBarVirtualKeyHapticFeedbackEnabled) || keyEvent.getRepeatCount() != 0) ? false : true;
                if (!this.mPhoneWindowManagerExt.interceptKeyEventForAppShareModeIfNeed(keyEvent)) {
                    return 0;
                }
                if (keyCode != 3) {
                    if (keyCode != 4) {
                        if (keyCode != 5) {
                            if (keyCode != 6) {
                                if (keyCode != 79 && keyCode != 130) {
                                    if (keyCode != 164) {
                                        if (keyCode != 171) {
                                            if (keyCode != 177) {
                                                if (keyCode != 187) {
                                                    if (keyCode == 219) {
                                                        boolean z8 = keyEvent.getRepeatCount() > 0;
                                                        if (z2 && !z8) {
                                                            Message obtainMessage = this.mHandler.obtainMessage(23, keyEvent.getDeviceId(), 0, Long.valueOf(keyEvent.getEventTime()));
                                                            this.mPhoneWindowManagerExt.getInputExtension().setLaunchModeInBundleWithDefault(obtainMessage);
                                                            obtainMessage.setAsynchronous(true);
                                                            obtainMessage.sendToTarget();
                                                        }
                                                    } else if (keyCode != 231) {
                                                        if (keyCode == 276) {
                                                            i2 &= -2;
                                                            if (!z2) {
                                                                this.mPowerManagerInternal.setUserInactiveOverrideFromWindowManager();
                                                            }
                                                        } else if (keyCode != 987) {
                                                            if (keyCode != 126 && keyCode != 127) {
                                                                switch (keyCode) {
                                                                    case MSG_RINGER_TOGGLE_CHORD /* 24 */:
                                                                    case MSG_SWITCH_KEYBOARD_LAYOUT /* 25 */:
                                                                        break;
                                                                    case 26:
                                                                        EventLogTags.writeInterceptPower(KeyEvent.actionToString(keyEvent.getAction()), this.mPowerKeyHandled ? 1 : 0, this.mSingleKeyGestureDetector.getKeyPressCounter(26));
                                                                        i2 &= -2;
                                                                        if (z2) {
                                                                            interceptPowerKeyDown(keyEvent, z6);
                                                                            break;
                                                                        } else {
                                                                            interceptPowerKeyUp(keyEvent, isCanceled);
                                                                            break;
                                                                        }
                                                                    default:
                                                                        switch (keyCode) {
                                                                            case HdmiCecKeycode.CEC_KEYCODE_INITIAL_CONFIGURATION /* 85 */:
                                                                            case HdmiCecKeycode.CEC_KEYCODE_SELECT_BROADCAST_TYPE /* 86 */:
                                                                            case HdmiCecKeycode.CEC_KEYCODE_SELECT_SOUND_PRESENTATION /* 87 */:
                                                                            case 88:
                                                                            case 89:
                                                                            case 90:
                                                                                break;
                                                                            case 91:
                                                                                i2 &= -2;
                                                                                if (z2 && keyEvent.getRepeatCount() == 0) {
                                                                                    toggleMicrophoneMuteFromKey();
                                                                                    break;
                                                                                }
                                                                                break;
                                                                            default:
                                                                                switch (keyCode) {
                                                                                    case 222:
                                                                                        break;
                                                                                    case 223:
                                                                                        i2 &= -2;
                                                                                        if (!this.mPowerManager.isInteractive()) {
                                                                                            z7 = false;
                                                                                        }
                                                                                        if (z2) {
                                                                                            sleepPress();
                                                                                            break;
                                                                                        } else {
                                                                                            sleepRelease(keyEvent.getEventTime());
                                                                                            break;
                                                                                        }
                                                                                    case UsbDescriptor.CLASSID_WIRELESS /* 224 */:
                                                                                        i2 &= -2;
                                                                                        z3 = true;
                                                                                        break;
                                                                                    default:
                                                                                        switch (keyCode) {
                                                                                            case 280:
                                                                                            case 281:
                                                                                            case 282:
                                                                                            case 283:
                                                                                                i2 &= -2;
                                                                                                interceptSystemNavigationKey(keyEvent);
                                                                                                break;
                                                                                            default:
                                                                                                switch (keyCode) {
                                                                                                    default:
                                                                                                        switch (keyCode) {
                                                                                                            case 308:
                                                                                                            case 309:
                                                                                                            case 310:
                                                                                                            case 311:
                                                                                                                if (z2 && this.mStylusButtonsEnabled) {
                                                                                                                    sendSystemKeyToStatusBarAsync(keyEvent);
                                                                                                                }
                                                                                                                break;
                                                                                                            default:
                                                                                                                switch (keyCode) {
                                                                                                                }
                                                                                                        }
                                                                                                    case 289:
                                                                                                    case 290:
                                                                                                    case 291:
                                                                                                    case 292:
                                                                                                    case 293:
                                                                                                    case 294:
                                                                                                    case 295:
                                                                                                    case 296:
                                                                                                    case 297:
                                                                                                    case 298:
                                                                                                    case 299:
                                                                                                    case 300:
                                                                                                    case IOplusLocationStatistics.GNSS_STRATEGY_OF_ACTIVITY_STOP /* 301 */:
                                                                                                    case IOplusLocationStatistics.GNSS_STRATEGY_OF_GPS_IN_DOOR /* 302 */:
                                                                                                    case 303:
                                                                                                    case 304:
                                                                                                        i2 &= -2;
                                                                                                        break;
                                                                                                }
                                                                                        }
                                                                                }
                                                                        }
                                                                }
                                                            }
                                                        }
                                                    } else if (!z2) {
                                                        this.mBroadcastWakeLock.acquire();
                                                        Message obtainMessage2 = this.mHandler.obtainMessage(12);
                                                        obtainMessage2.setAsynchronous(true);
                                                        obtainMessage2.sendToTarget();
                                                    }
                                                    i2 &= -2;
                                                }
                                                z7 = this.mPhoneWindowManagerExt.getInputExtension().interceptAppSwitchEventBeforeQueueing(keyEvent, z7);
                                            } else {
                                                i2 &= -2;
                                                if (z2 && hdmiControlManager != null) {
                                                    hdmiControlManager.toggleAndFollowTvPower();
                                                }
                                            }
                                            z3 = false;
                                        } else if (this.mShortPressOnWindowBehavior == 1 && this.mPictureInPictureVisible) {
                                            if (!z2) {
                                                showPictureInPictureMenu(keyEvent);
                                            }
                                            i2 &= -2;
                                        }
                                    }
                                    this.mPhoneWindowManagerExt.hookForInputLogV("interceptKeyBeforeQueueing volume key : " + i2 + " | " + this.mUseTvRouting + " | " + this.mHandleVolumeKeysInWM);
                                    if (z2) {
                                        sendSystemKeyToStatusBarAsync(keyEvent);
                                        NotificationManager notificationService = getNotificationService();
                                        if (notificationService != null && !this.mHandleVolumeKeysInWM) {
                                            notificationService.silenceNotificationSound();
                                        }
                                        TelecomManager telecommService2 = getTelecommService();
                                        if (telecommService2 != null && !this.mHandleVolumeKeysInWM && telecommService2.isRinging()) {
                                            Log.i(TAG, "interceptKeyBeforeQueueing: VOLUME key-down while ringing: Silence ringer!");
                                            telecommService2.silenceRinger();
                                            i2 &= -2;
                                        } else {
                                            try {
                                                i3 = getAudioService().getMode();
                                            } catch (Exception e) {
                                                Log.e(TAG, "Error getting AudioService in interceptKeyBeforeQueueing.", e);
                                                i3 = 0;
                                            }
                                            if (((telecommService2 != null && telecommService2.isInCall()) || i3 == 3) && (i2 & 1) == 0) {
                                                MediaSessionLegacyHelper.getHelper(this.mContext).sendVolumeKeyEvent(keyEvent, Integer.MIN_VALUE, false);
                                            }
                                        }
                                    }
                                    if (this.mUseTvRouting || this.mHandleVolumeKeysInWM) {
                                        i2 |= 1;
                                    } else if ((i2 & 1) == 0) {
                                        MediaSessionLegacyHelper.getHelper(this.mContext).sendVolumeKeyEvent(keyEvent, Integer.MIN_VALUE, true);
                                    }
                                }
                                if (MediaSessionLegacyHelper.getHelper(this.mContext).isGlobalPriorityActive()) {
                                    i2 &= -2;
                                }
                                if ((i2 & 1) == 0) {
                                    this.mBroadcastWakeLock.acquire();
                                    Message obtainMessage3 = this.mHandler.obtainMessage(3, new KeyEvent(keyEvent));
                                    obtainMessage3.setAsynchronous(true);
                                    obtainMessage3.sendToTarget();
                                }
                            } else {
                                i2 &= -2;
                                if (z2) {
                                    TelecomManager telecommService3 = getTelecommService();
                                    boolean endCall = telecommService3 != null ? telecommService3.endCall() : false;
                                    if (z4 && !endCall) {
                                        this.mEndCallKeyHandled = false;
                                        this.mHandler.postDelayed(this.mEndCallLongPress, ViewConfiguration.get(this.mContext).getDeviceGlobalActionKeyTimeout());
                                    } else {
                                        this.mEndCallKeyHandled = true;
                                    }
                                } else if (!this.mEndCallKeyHandled) {
                                    this.mHandler.removeCallbacks(this.mEndCallLongPress);
                                    if (!isCanceled && (((this.mEndcallBehavior & 1) == 0 || !goHome()) && (this.mEndcallBehavior & 2) != 0)) {
                                        sleepDefaultDisplay(keyEvent.getEventTime(), 4, 0);
                                        z3 = false;
                                    }
                                }
                            }
                        } else if (z2 && (telecommService = getTelecommService()) != null && telecommService.isRinging()) {
                            Log.i(TAG, "interceptKeyBeforeQueueing: CALL key-down while ringing: Answer the call!");
                            telecommService.acceptRingingCall();
                            i2 &= -2;
                        }
                    } else if (z2) {
                        this.mBackKeyHandled = false;
                    } else if (!hasLongPressOnBackBehavior()) {
                        this.mBackKeyHandled |= backKeyPress();
                    }
                } else if (z2 && keyEvent.getDownTime() - keyEvent.getEventTime() == keyCode) {
                    z7 = false;
                }
                if (z7) {
                    performHapticFeedback(1, false, "Virtual Key - Press");
                }
                if (z3) {
                    wakeUpFromWakeKey(keyEvent);
                }
                if ((i2 & 1) != 0 && displayId != -1 && displayId != this.mTopFocusedDisplayId) {
                    Log.i(TAG, "Attempting to move non-focused display " + displayId + " to top because a key is targeting it");
                    this.mWindowManagerFuncs.moveDisplayToTopIfAllowed(displayId);
                }
                return i2;
            }
            z3 = false;
            i2 = 1;
            if (!isValidGlobalKey(keyCode)) {
            }
            HdmiControlManager hdmiControlManager2 = getHdmiControlManager();
            if (keyCode != 177) {
            }
            if (z4) {
            }
            if ((keyEvent.getFlags() & 1024) == 0) {
            }
            if (z2) {
            }
            if (!this.mPhoneWindowManagerExt.interceptKeyEventForAppShareModeIfNeed(keyEvent)) {
            }
        } else if (shouldDispatchInputWhenNonInteractive(displayId, keyCode)) {
            this.mPendingWakeKey = -1;
            i2 = 1;
            if (!isValidGlobalKey(keyCode)) {
            }
            HdmiControlManager hdmiControlManager22 = getHdmiControlManager();
            if (keyCode != 177) {
            }
            if (z4) {
            }
            if ((keyEvent.getFlags() & 1024) == 0) {
            }
            if (z2) {
            }
            if (!this.mPhoneWindowManagerExt.interceptKeyEventForAppShareModeIfNeed(keyEvent)) {
            }
        } else {
            if (z3 && (!z2 || !isWakeKeyWhenScreenOff(keyCode))) {
                z3 = false;
            }
            if (z3 && z2) {
                this.mPendingWakeKey = keyCode;
            }
            i2 = 0;
            if (!isValidGlobalKey(keyCode)) {
            }
            HdmiControlManager hdmiControlManager222 = getHdmiControlManager();
            if (keyCode != 177) {
            }
            if (z4) {
            }
            if ((keyEvent.getFlags() & 1024) == 0) {
            }
            if (z2) {
            }
            if (!this.mPhoneWindowManagerExt.interceptKeyEventForAppShareModeIfNeed(keyEvent)) {
            }
        }
    }

    private void handleKeyGesture(KeyEvent keyEvent, boolean z) {
        if (this.mKeyCombinationManager.interceptKey(keyEvent, z)) {
            this.mSingleKeyGestureDetector.reset();
            return;
        }
        if (keyEvent.getKeyCode() == 26 && keyEvent.getAction() == 0) {
            this.mPowerKeyHandled = handleCameraGesture(keyEvent, z);
            if (this.mPowerKeyHandled) {
                this.mSingleKeyGestureDetector.reset();
                return;
            }
        }
        this.mSingleKeyGestureDetector.interceptKey(keyEvent, z);
    }

    private boolean handleCameraGesture(KeyEvent keyEvent, boolean z) {
        if (!this.mPhoneWindowManagerExt.getInputExtension().isCameraGestureEnabled() || this.mGestureLauncherService == null) {
            return false;
        }
        this.mCameraGestureTriggered = false;
        MutableBoolean mutableBoolean = new MutableBoolean(false);
        boolean interceptPowerKeyDown = this.mGestureLauncherService.interceptPowerKeyDown(keyEvent, z, mutableBoolean);
        if (!mutableBoolean.value) {
            return interceptPowerKeyDown;
        }
        this.mCameraGestureTriggered = true;
        if (this.mRequestedOrSleepingDefaultDisplay) {
            this.mCameraGestureTriggeredDuringGoingToSleep = true;
            wakeUp(SystemClock.uptimeMillis(), this.mAllowTheaterModeWakeFromPowerKey, 5, "android.policy:CAMERA_GESTURE_PREVENT_LOCK");
        }
        return true;
    }

    private void interceptSystemNavigationKey(KeyEvent keyEvent) {
        if (keyEvent.getAction() == 1) {
            if (!(this.mAccessibilityManager.isEnabled() && this.mAccessibilityManager.sendFingerprintGesture(keyEvent.getKeyCode())) && this.mSystemNavigationKeysEnabled) {
                sendSystemKeyToStatusBarAsync(keyEvent);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSystemKeyToStatusBar(KeyEvent keyEvent) {
        IStatusBarService statusBarService = getStatusBarService();
        if (statusBarService != null) {
            try {
                statusBarService.handleSystemKey(keyEvent);
            } catch (RemoteException unused) {
            }
        }
    }

    private void sendSystemKeyToStatusBarAsync(KeyEvent keyEvent) {
        Message obtainMessage = this.mHandler.obtainMessage(MSG_SYSTEM_KEY_PRESS, keyEvent);
        obtainMessage.setAsynchronous(true);
        this.mHandler.sendMessage(obtainMessage);
    }

    private boolean isWakeKeyWhenScreenOff(int i) {
        if (i == 4) {
            return this.mWakeOnBackKeyPress;
        }
        if (i != 219) {
            switch (i) {
                case 19:
                case 20:
                case MSG_SYSTEM_KEY_PRESS /* 21 */:
                case MSG_HANDLE_ALL_APPS /* 22 */:
                case 23:
                    return this.mWakeOnDpadKeyPress;
                default:
                    switch (i) {
                        case 308:
                        case 309:
                        case 310:
                        case 311:
                            return this.mStylusButtonsEnabled;
                        default:
                            return true;
                    }
            }
        }
        return this.mWakeOnAssistKeyPress;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public int interceptMotionBeforeQueueingNonInteractive(int i, long j, int i2) {
        int i3 = i2 & 1;
        if (i3 != 0 && wakeUp(j / 1000000, this.mAllowTheaterModeWakeFromMotion, 7, "android.policy:MOTION")) {
            return 0;
        }
        if (shouldDispatchInputWhenNonInteractive(i, 0)) {
            return 1;
        }
        if (isTheaterModeEnabled() && i3 != 0) {
            wakeUp(j / 1000000, this.mAllowTheaterModeWakeFromMotionWhenNotDreaming, 7, "android.policy:MOTION");
        }
        return 0;
    }

    private boolean shouldDispatchInputWhenNonInteractive(int i, int i2) {
        Display display;
        IDreamManager dreamManager;
        boolean z = i == 0 || i == -1;
        if (z) {
            display = this.mDefaultDisplay;
        } else {
            display = this.mDisplayManager.getDisplay(i);
        }
        boolean z2 = display == null || display.getState() == 1;
        this.mPhoneWindowManagerExt.hookForInputLogV("shouldDispatchInputWhenNonInteractive : " + z2 + " | " + z + " | " + this.mHasFeatureWatch);
        if (z2 && !this.mHasFeatureWatch) {
            return false;
        }
        if (isKeyguardShowingAndNotOccluded() && !z2) {
            return true;
        }
        if ((!this.mHasFeatureWatch || (i2 != 4 && i2 != 264 && i2 != 265 && i2 != 266 && i2 != 267)) && z && (dreamManager = getDreamManager()) != null) {
            try {
                if (dreamManager.isDreaming()) {
                    this.mPhoneWindowManagerExt.hookForInputLogV("shouldDispatchInputWhenNonInteractive dreaming!");
                    return true;
                }
            } catch (RemoteException e) {
                Slog.e(TAG, "RemoteException when checking if dreaming", e);
            }
        }
        return false;
    }

    private void dispatchDirectAudioEvent(KeyEvent keyEvent) {
        HdmiAudioSystemClient audioSystemClient;
        HdmiControlManager hdmiControlManager = getHdmiControlManager();
        if (hdmiControlManager != null && !hdmiControlManager.getSystemAudioMode() && shouldCecAudioDeviceForwardVolumeKeysSystemAudioModeOff() && (audioSystemClient = hdmiControlManager.getAudioSystemClient()) != null) {
            audioSystemClient.sendKeyEvent(keyEvent.getKeyCode(), keyEvent.getAction() == 0);
            return;
        }
        try {
            getAudioService().handleVolumeKey(keyEvent, this.mUseTvRouting, this.mContext.getOpPackageName(), TAG);
        } catch (Exception e) {
            Log.e(TAG, "Error dispatching volume key in handleVolumeKey for event:" + keyEvent, e);
        }
    }

    private HdmiControlManager getHdmiControlManager() {
        if (this.mHasFeatureHdmiCec) {
            return (HdmiControlManager) this.mContext.getSystemService(HdmiControlManager.class);
        }
        return null;
    }

    private boolean shouldCecAudioDeviceForwardVolumeKeysSystemAudioModeOff() {
        return RoSystemProperties.CEC_AUDIO_DEVICE_FORWARD_VOLUME_KEYS_SYSTEM_AUDIO_MODE_OFF;
    }

    void dispatchMediaKeyWithWakeLock(KeyEvent keyEvent) {
        if (DEBUG_INPUT) {
            Slog.d(TAG, "dispatchMediaKeyWithWakeLock: " + keyEvent);
        }
        if (this.mHavePendingMediaKeyRepeatWithWakeLock) {
            if (DEBUG_INPUT) {
                Slog.d(TAG, "dispatchMediaKeyWithWakeLock: canceled repeat");
            }
            this.mHandler.removeMessages(4);
            this.mHavePendingMediaKeyRepeatWithWakeLock = false;
            this.mBroadcastWakeLock.release();
        }
        dispatchMediaKeyWithWakeLockToAudioService(keyEvent);
        if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
            this.mHavePendingMediaKeyRepeatWithWakeLock = true;
            Message obtainMessage = this.mHandler.obtainMessage(4, keyEvent);
            obtainMessage.setAsynchronous(true);
            this.mHandler.sendMessageDelayed(obtainMessage, ViewConfiguration.getKeyRepeatTimeout());
            return;
        }
        this.mBroadcastWakeLock.release();
    }

    void dispatchMediaKeyRepeatWithWakeLock(KeyEvent keyEvent) {
        this.mHavePendingMediaKeyRepeatWithWakeLock = false;
        KeyEvent changeTimeRepeat = KeyEvent.changeTimeRepeat(keyEvent, SystemClock.uptimeMillis(), 1, keyEvent.getFlags() | 128);
        if (DEBUG_INPUT) {
            Slog.d(TAG, "dispatchMediaKeyRepeatWithWakeLock: " + changeTimeRepeat);
        }
        dispatchMediaKeyWithWakeLockToAudioService(changeTimeRepeat);
        this.mBroadcastWakeLock.release();
    }

    void dispatchMediaKeyWithWakeLockToAudioService(KeyEvent keyEvent) {
        if (this.mActivityManagerInternal.isSystemReady()) {
            MediaSessionLegacyHelper.getHelper(this.mContext).sendMediaButtonEvent(keyEvent, true);
        }
    }

    void launchVoiceAssistWithWakeLock() {
        Intent intent;
        sendCloseSystemWindows(SYSTEM_DIALOG_REASON_ASSIST);
        if (!keyguardOn()) {
            intent = new Intent("android.speech.action.WEB_SEARCH");
        } else {
            DeviceIdleManager deviceIdleManager = (DeviceIdleManager) this.mContext.getSystemService(DeviceIdleManager.class);
            if (deviceIdleManager != null) {
                deviceIdleManager.endIdle("voice-search");
            }
            intent = new Intent("android.speech.action.VOICE_SEARCH_HANDS_FREE");
            intent.putExtra("android.speech.extras.EXTRA_SECURE", true);
        }
        startActivityAsUser(intent, UserHandle.CURRENT_OR_SELF);
        this.mBroadcastWakeLock.release();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void startedGoingToSleepGlobal(int i) {
        this.mDeviceGoingToSleep = true;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void finishedGoingToSleepGlobal(int i) {
        this.mDeviceGoingToSleep = false;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void startedGoingToSleep(int i, int i2) {
        if (DEBUG_WAKEUP) {
            Slog.i(TAG, "Started going to sleep... (groupId=" + i + " why=" + WindowManagerPolicyConstants.offReasonToString(WindowManagerPolicyConstants.translateSleepReasonToOffReason(i2)) + ")");
        }
        if (i != 0) {
            return;
        }
        this.mRequestedOrSleepingDefaultDisplay = true;
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate != null) {
            keyguardServiceDelegate.onStartedGoingToSleep(i2);
            this.mPhoneWindowManagerExt.startedGoingToSleep();
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void finishedGoingToSleep(int i, int i2) {
        if (i != 0) {
            return;
        }
        EventLogTags.writeScreenToggled(0);
        if (DEBUG_WAKEUP) {
            Slog.i(TAG, "Finished going to sleep... (groupId=" + i + " why=" + WindowManagerPolicyConstants.offReasonToString(WindowManagerPolicyConstants.translateSleepReasonToOffReason(i2)) + ")");
        }
        MetricsLogger.histogram(this.mContext, "screen_timeout", this.mLockScreenTimeout / 1000);
        this.mRequestedOrSleepingDefaultDisplay = false;
        this.mDefaultDisplayPolicy.setAwake(false);
        synchronized (this.mLock) {
            updateWakeGestureListenerLp();
            updateLockScreenTimeout();
        }
        this.mDefaultDisplayRotation.updateOrientationListener();
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate != null) {
            keyguardServiceDelegate.onFinishedGoingToSleep(i2, this.mCameraGestureTriggeredDuringGoingToSleep);
        }
        DisplayFoldController displayFoldController = this.mDisplayFoldController;
        if (displayFoldController != null) {
            displayFoldController.finishedGoingToSleep();
        }
        this.mCameraGestureTriggeredDuringGoingToSleep = false;
        this.mCameraGestureTriggered = false;
        this.mPhoneWindowManagerExt.resetDeviceFolded();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void startedWakingUp(int i, int i2) {
        if (DEBUG_WAKEUP) {
            Slog.i(TAG, "Started waking up... (groupId=" + i + " why=" + WindowManagerPolicyConstants.onReasonToString(WindowManagerPolicyConstants.translateWakeReasonToOnReason(i2)) + ")");
        }
        if (i != 0) {
            return;
        }
        EventLogTags.writeScreenToggled(1);
        this.mDefaultDisplayPolicy.setAwake(true);
        synchronized (this.mLock) {
            updateWakeGestureListenerLp();
            updateLockScreenTimeout();
        }
        this.mDefaultDisplayRotation.updateOrientationListener();
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate != null) {
            keyguardServiceDelegate.onStartedWakingUp(i2, this.mCameraGestureTriggered);
        }
        this.mCameraGestureTriggered = false;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void finishedWakingUp(int i, int i2) {
        if (DEBUG_WAKEUP) {
            Slog.i(TAG, "Finished waking up... (groupId=" + i + " why=" + WindowManagerPolicyConstants.onReasonToString(WindowManagerPolicyConstants.translateWakeReasonToOnReason(i2)) + ")");
        }
        if (i != 0) {
            return;
        }
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate != null) {
            keyguardServiceDelegate.onFinishedWakingUp();
        }
        DisplayFoldController displayFoldController = this.mDisplayFoldController;
        if (displayFoldController != null) {
            displayFoldController.finishedWakingUp();
        }
    }

    private boolean shouldWakeUpWithHomeIntent() {
        if (this.mWakeUpToLastStateTimeout <= 0) {
            return false;
        }
        long j = this.mPowerManagerInternal.getLastWakeup().sleepDurationRealtime;
        if (DEBUG_WAKEUP) {
            Log.i(TAG, "shouldWakeUpWithHomeIntent: sleepDurationRealtime= " + j + " mWakeUpToLastStateTimeout= " + this.mWakeUpToLastStateTimeout);
        }
        return j > this.mWakeUpToLastStateTimeout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wakeUpFromPowerKey(long j) {
        this.mPhoneWindowManagerExt.notePowerkeyProcessStagePoint("POWERKEY_wakeUpFromPowerKey");
        if (this.mPhoneWindowManagerExt.isPowerButtonFpSensor()) {
            this.mPhoneWindowManagerExt.notifyPowerKeyPressed("android.policy:POWER");
        }
        if (wakeUp(j, this.mAllowTheaterModeWakeFromPowerKey, 1, "android.policy:POWER") && shouldWakeUpWithHomeIntent()) {
            startDockOrHome(0, false, true, PowerManager.wakeReasonToString(1));
        }
    }

    private void wakeUpFromWakeKey(KeyEvent keyEvent) {
        if (wakeUp(keyEvent.getEventTime(), this.mAllowTheaterModeWakeFromKey, 6, "android.policy:KEY") && shouldWakeUpWithHomeIntent() && keyEvent.getKeyCode() == 3) {
            startDockOrHome(0, true, true, PowerManager.wakeReasonToString(6));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean wakeUp(long j, boolean z, int i, String str) {
        boolean isTheaterModeEnabled = isTheaterModeEnabled();
        this.mPhoneWindowManagerExt.hookForInputLogV("wakeup wakeTime=" + j + ",wakeInTheaterMode=" + z + ",details=" + str + ",theaterModeEnabled=" + isTheaterModeEnabled);
        if (!z && isTheaterModeEnabled) {
            this.mPhoneWindowManagerExt.notePowerkeyProcessEvent("theatermode enable when wakeup", true, false);
            return false;
        }
        if (isTheaterModeEnabled) {
            Settings.Global.putInt(this.mContext.getContentResolver(), "theater_mode_on", 0);
        }
        this.mPhoneWindowManagerExt.keyEventSpendTimeEventLog(j);
        this.mPowerManager.wakeUp(j, i, str);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishKeyguardDrawn() {
        if (this.mDefaultDisplayPolicy.finishKeyguardDrawn()) {
            this.mPhoneWindowManagerExt.setSwitchingTrackerKeyguardOndrawnEventLog();
            synchronized (this.mLock) {
                if (this.mKeyguardDelegate != null) {
                    this.mHandler.removeMessages(6);
                }
            }
            Trace.asyncTraceBegin(32L, TRACE_WAIT_FOR_ALL_WINDOWS_DRAWN_METHOD, -1);
            this.mWindowManagerInternal.waitForAllWindowsDrawn(this.mHandler.obtainMessage(7, -1, 0), 1000L, -1);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void screenTurnedOff(int i, boolean z) {
        if (DEBUG_WAKEUP) {
            Slog.i(TAG, "Display" + i + " turned off...");
        }
        if (i == 0) {
            updateScreenOffSleepToken(true, z);
            this.mRequestedOrSleepingDefaultDisplay = false;
            this.mDefaultDisplayPolicy.screenTurnedOff();
            synchronized (this.mLock) {
                KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
                if (keyguardServiceDelegate != null) {
                    keyguardServiceDelegate.onScreenTurnedOff();
                }
            }
            this.mPhoneWindowManagerExt.updateOrientationListenerAsyncIfNeeded(this.mDefaultDisplayRotation);
            reportScreenStateToVrManager(false);
        }
        this.mPhoneWindowManagerExt.screenTurnedOff(i);
        this.mPhoneWindowManagerExt.updateOrientationListener(i);
    }

    private long getKeyguardDrawnTimeout() {
        if (((SystemServiceManager) LocalServices.getService(SystemServiceManager.class)).isBootCompleted()) {
            return this.mKeyguardDrawnTimeout;
        }
        return 5000L;
    }

    private WallpaperManagerInternal getWallpaperManagerInternal() {
        if (this.mWallpaperManagerInternal == null) {
            this.mWallpaperManagerInternal = (WallpaperManagerInternal) LocalServices.getService(WallpaperManagerInternal.class);
        }
        return this.mWallpaperManagerInternal;
    }

    private void reportScreenTurningOnToWallpaper(int i) {
        WallpaperManagerInternal wallpaperManagerInternal = getWallpaperManagerInternal();
        if (wallpaperManagerInternal != null) {
            wallpaperManagerInternal.onScreenTurningOn(i);
        }
    }

    private void reportScreenTurnedOnToWallpaper(int i) {
        WallpaperManagerInternal wallpaperManagerInternal = getWallpaperManagerInternal();
        if (wallpaperManagerInternal != null) {
            wallpaperManagerInternal.onScreenTurnedOn(i);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void screenTurningOn(int i, WindowManagerPolicy.ScreenOnListener screenOnListener) {
        if (DEBUG_WAKEUP) {
            Slog.i(TAG, "Display " + i + " turning on...");
        }
        reportScreenTurningOnToWallpaper(i);
        if (i == 0) {
            Trace.asyncTraceBegin(32L, "screenTurningOn", 0);
            this.mPhoneWindowManagerExt.setSwitchingTrackerScreenTurningOnEventLog(true);
            updateScreenOffSleepToken(false, false);
            this.mDefaultDisplayPolicy.screenTurnedOn(screenOnListener);
            this.mBootAnimationDismissable = false;
            synchronized (this.mLock) {
                KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
                if (keyguardServiceDelegate != null && keyguardServiceDelegate.hasKeyguard()) {
                    this.mHandler.removeMessages(6);
                    this.mHandler.sendEmptyMessageDelayed(6, getKeyguardDrawnTimeout());
                    this.mKeyguardDelegate.onScreenTurningOn(this.mKeyguardDrawnCallback);
                } else {
                    if (DEBUG_WAKEUP) {
                        Slog.d(TAG, "null mKeyguardDelegate: setting mKeyguardDrawComplete.");
                    }
                    this.mHandler.sendEmptyMessage(5);
                }
            }
            return;
        }
        this.mScreenOnListeners.put(i, screenOnListener);
        this.mPhoneWindowManagerExt.screenTurnedOn(i, screenOnListener);
        Trace.asyncTraceBegin(32L, TRACE_WAIT_FOR_ALL_WINDOWS_DRAWN_METHOD, i);
        this.mWindowManagerInternal.waitForAllWindowsDrawn(this.mHandler.obtainMessage(7, i, 0), 1000L, i);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void screenTurnedOn(int i) {
        if (DEBUG_WAKEUP) {
            Slog.i(TAG, "Display " + i + " turned on...");
        }
        reportScreenTurnedOnToWallpaper(i);
        if (i != 0) {
            return;
        }
        synchronized (this.mLock) {
            KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
            if (keyguardServiceDelegate != null) {
                keyguardServiceDelegate.onScreenTurnedOn();
            }
        }
        reportScreenStateToVrManager(true);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void screenTurningOff(int i, WindowManagerPolicy.ScreenOffListener screenOffListener) {
        this.mWindowManagerFuncs.screenTurningOff(i, screenOffListener);
        if (i != 0) {
            return;
        }
        this.mRequestedOrSleepingDefaultDisplay = true;
        synchronized (this.mLock) {
            KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
            if (keyguardServiceDelegate != null) {
                keyguardServiceDelegate.onScreenTurningOff();
            }
        }
    }

    private void reportScreenStateToVrManager(boolean z) {
        if (this.mVrManagerInternal == null) {
            return;
        }
        this.mVrManagerInternal.onScreenStateChanged(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishWindowsDrawn(int i) {
        if (i != 0 && i != -1) {
            WindowManagerPolicy.ScreenOnListener screenOnListener = (WindowManagerPolicy.ScreenOnListener) this.mScreenOnListeners.removeReturnOld(i);
            if (screenOnListener != null) {
                screenOnListener.onScreenOn();
            }
            if (this.mPhoneWindowManagerExt.finishWindowsDrawn(i)) {
                this.mPhoneWindowManagerExt.updateOrientationListener(i);
                this.mPhoneWindowManagerExt.finishScreenTurningOn(i);
                return;
            }
            return;
        }
        this.mPhoneWindowManagerExt.finishWindowsDrawn(i);
        if (this.mDefaultDisplayPolicy.finishWindowsDrawn()) {
            finishScreenTurningOn();
        }
    }

    private void finishScreenTurningOn() {
        this.mPhoneWindowManagerExt.updateOrientationListenerAsyncIfNeeded(this.mDefaultDisplayRotation);
        WindowManagerPolicy.ScreenOnListener screenOnListener = this.mDefaultDisplayPolicy.getScreenOnListener();
        if (this.mDefaultDisplayPolicy.finishScreenTurningOn()) {
            Trace.asyncTraceEnd(32L, "screenTurningOn", 0);
            this.mPhoneWindowManagerExt.setSwitchingTrackerScreenTurningOnEventLog(false);
            enableScreen(screenOnListener, true);
        }
    }

    private void enableScreen(WindowManagerPolicy.ScreenOnListener screenOnListener, boolean z) {
        boolean z2;
        boolean isAwake = this.mDefaultDisplayPolicy.isAwake();
        synchronized (this.mLock) {
            z2 = false;
            if (!this.mKeyguardDrawnOnce && isAwake) {
                this.mKeyguardDrawnOnce = true;
                if (this.mBootMessageNeedsHiding) {
                    this.mBootMessageNeedsHiding = false;
                    hideBootMessages();
                }
                z2 = true;
            }
        }
        if (z && screenOnListener != null) {
            screenOnListener.onScreenOn();
        }
        if (z2) {
            this.mWindowManagerFuncs.enableScreenIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHideBootMessage() {
        synchronized (this.mLock) {
            if (!this.mKeyguardDrawnOnce) {
                this.mBootMessageNeedsHiding = true;
            } else if (this.mBootMsgDialog != null) {
                if (DEBUG_WAKEUP) {
                    Slog.d(TAG, "handleHideBootMessage: dismissing");
                }
                this.mBootMsgDialog.dismiss();
                this.mBootMsgDialog = null;
            }
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isScreenOn() {
        return this.mDefaultDisplayPolicy.isScreenOnEarly();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean okToAnimate(boolean z) {
        return (z || isScreenOn()) && !this.mDeviceGoingToSleep;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void enableKeyguard(boolean z) {
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate != null) {
            keyguardServiceDelegate.setKeyguardEnabled(z);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void exitKeyguardSecurely(WindowManagerPolicy.OnKeyguardExitResult onKeyguardExitResult) {
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate != null) {
            keyguardServiceDelegate.verifyUnlock(onKeyguardExitResult);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardShowing() {
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate == null) {
            return false;
        }
        return keyguardServiceDelegate.isShowing();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardShowingAndNotOccluded() {
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        return (keyguardServiceDelegate == null || !keyguardServiceDelegate.isShowing() || isKeyguardOccluded()) ? false : true;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardTrustedLw() {
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate == null) {
            return false;
        }
        return keyguardServiceDelegate.isTrusted();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardLocked() {
        return keyguardOn();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardSecure(int i) {
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate == null) {
            return false;
        }
        return keyguardServiceDelegate.isSecure(i);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardOccluded() {
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate == null) {
            return false;
        }
        return keyguardServiceDelegate.isOccluded();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean inKeyguardRestrictedKeyInputMode() {
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate == null) {
            return false;
        }
        return keyguardServiceDelegate.isInputRestricted();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardUnoccluding() {
        return keyguardOn() && !this.mWindowManagerFuncs.isAppTransitionStateIdle();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void dismissKeyguardLw(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate != null && keyguardServiceDelegate.isShowing()) {
            if (DEBUG_KEYGUARD) {
                Slog.d(TAG, "PWM.dismissKeyguardLw");
            }
            this.mKeyguardDelegate.dismiss(iKeyguardDismissCallback, charSequence);
        } else if (iKeyguardDismissCallback != null) {
            try {
                iKeyguardDismissCallback.onDismissError();
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to call callback", e);
            }
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardDrawnLw() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mKeyguardDrawnOnce;
        }
        return z;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void startKeyguardExitAnimation(long j) {
        if (this.mKeyguardDelegate != null) {
            if (DEBUG_KEYGUARD) {
                Slog.d(TAG, "PWM.startKeyguardExitAnimation");
            }
            this.mKeyguardDelegate.startKeyguardExitAnimation(j);
        }
    }

    void sendCloseSystemWindows() {
        PhoneWindow.sendCloseSystemWindows(this.mContext, (String) null);
    }

    void sendCloseSystemWindows(String str) {
        PhoneWindow.sendCloseSystemWindows(this.mContext, str);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setSafeMode(boolean z) {
        this.mSafeMode = z;
        if (z) {
            performHapticFeedback(10001, true, "Safe Mode Enabled");
        }
    }

    static long[] getLongIntArray(Resources resources, int i) {
        return ArrayUtils.convertToLongArray(resources.getIntArray(i));
    }

    private void bindKeyguard() {
        synchronized (this.mLock) {
            if (this.mKeyguardBound) {
                return;
            }
            this.mKeyguardBound = true;
            this.mKeyguardDelegate.bindService(this.mContext);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void onSystemUiStarted() {
        bindKeyguard();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void systemReady() {
        this.mKeyguardDelegate.onSystemReady();
        this.mVrManagerInternal = (VrManagerInternal) LocalServices.getService(VrManagerInternal.class);
        if (this.mVrManagerInternal != null) {
            this.mVrManagerInternal.addPersistentVrModeStateListener(this.mPersistentVrModeListener);
        }
        readCameraLensCoverState();
        updateUiMode();
        this.mDefaultDisplayRotation.updateOrientationListener();
        synchronized (this.mLock) {
            this.mSystemReady = true;
            this.mHandler.post(new Runnable() { // from class: com.android.server.policy.PhoneWindowManager.16
                @Override // java.lang.Runnable
                public void run() {
                    PhoneWindowManager.this.updateSettings();
                }
            });
            if (this.mSystemBooted) {
                this.mKeyguardDelegate.onBootCompleted();
            }
        }
        this.mAutofillManagerInternal = (AutofillManagerInternal) LocalServices.getService(AutofillManagerInternal.class);
        this.mGestureLauncherService = (GestureLauncherService) LocalServices.getService(GestureLauncherService.class);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void systemBooted() {
        bindKeyguard();
        synchronized (this.mLock) {
            this.mSystemBooted = true;
            if (this.mSystemReady) {
                this.mKeyguardDelegate.onBootCompleted();
            }
        }
        this.mSideFpsEventHandler.onFingerprintSensorReady();
        startedWakingUp(0, 0);
        finishedWakingUp(0, 0);
        boolean z = this.mDisplayManager.getDisplay(0).getState() == 2;
        boolean z2 = this.mDefaultDisplayPolicy.getScreenOnListener() != null;
        if (z || z2) {
            screenTurningOn(0, this.mDefaultDisplayPolicy.getScreenOnListener());
            screenTurnedOn(0);
        } else {
            this.mBootAnimationDismissable = true;
            enableScreen(null, false);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean canDismissBootAnimation() {
        return this.mDefaultDisplayPolicy.isKeyguardDrawComplete() || this.mBootAnimationDismissable;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void showBootMessage(final CharSequence charSequence, boolean z) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.policy.PhoneWindowManager.17
            @Override // java.lang.Runnable
            public void run() {
                PhoneWindowManager phoneWindowManager = PhoneWindowManager.this;
                if (phoneWindowManager.mBootMsgDialog == null) {
                    PhoneWindowManager.this.mBootMsgDialog = new ProgressDialog(PhoneWindowManager.this.mContext, phoneWindowManager.mPackageManager.hasSystemFeature("android.software.leanback") ? R.style.Theme.Holo.Light.Dialog.Presentation : 0) { // from class: com.android.server.policy.PhoneWindowManager.17.1
                        @Override // android.app.Dialog, android.view.Window.Callback
                        public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
                            return true;
                        }

                        @Override // android.app.Dialog, android.view.Window.Callback
                        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                            return true;
                        }

                        @Override // android.app.Dialog, android.view.Window.Callback
                        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
                            return true;
                        }

                        @Override // android.app.Dialog, android.view.Window.Callback
                        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
                            return true;
                        }

                        @Override // android.app.Dialog, android.view.Window.Callback
                        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                            return true;
                        }

                        @Override // android.app.Dialog, android.view.Window.Callback
                        public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
                            return true;
                        }
                    };
                    if (PhoneWindowManager.this.mPackageManager.isDeviceUpgrading()) {
                        PhoneWindowManager.this.mBootMsgDialog.setTitle(R.string.autofill_card_ignored_re);
                    } else {
                        PhoneWindowManager.this.mBootMsgDialog.setTitle(R.string.autofill_address_type_use_my_re);
                    }
                    PhoneWindowManager.this.mBootMsgDialog.setProgressStyle(0);
                    PhoneWindowManager.this.mBootMsgDialog.setIndeterminate(true);
                    PhoneWindowManager.this.mBootMsgDialog.getWindow().setType(2021);
                    PhoneWindowManager.this.mBootMsgDialog.getWindow().addFlags(258);
                    PhoneWindowManager.this.mBootMsgDialog.getWindow().setDimAmount(1.0f);
                    WindowManager.LayoutParams attributes = PhoneWindowManager.this.mBootMsgDialog.getWindow().getAttributes();
                    attributes.screenOrientation = 5;
                    attributes.setFitInsetsTypes(0);
                    PhoneWindowManager.this.mBootMsgDialog.getWindow().setAttributes(attributes);
                    PhoneWindowManager.this.mBootMsgDialog.setCancelable(false);
                    PhoneWindowManager.this.mBootMsgDialog.show();
                }
                PhoneWindowManager.this.mBootMsgDialog.setMessage(charSequence);
            }
        });
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void hideBootMessages() {
        this.mHandler.sendEmptyMessage(11);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void userActivity(int i, int i2) {
        if (i == 0 && i2 == 2) {
            this.mDefaultDisplayPolicy.onUserActivityEventTouch();
        }
        synchronized (this.mScreenLockTimeout) {
            if (this.mLockScreenTimerActive) {
                this.mHandler.removeCallbacks(this.mScreenLockTimeout);
                this.mHandler.postDelayed(this.mScreenLockTimeout, this.mLockScreenTimeout);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class ScreenLockTimeout implements Runnable {
        Bundle options;

        ScreenLockTimeout() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (this) {
                Log.v(PhoneWindowManager.TAG, "mScreenLockTimeout activating keyguard");
                KeyguardServiceDelegate keyguardServiceDelegate = PhoneWindowManager.this.mKeyguardDelegate;
                if (keyguardServiceDelegate != null) {
                    keyguardServiceDelegate.doKeyguardTimeout(this.options);
                }
                PhoneWindowManager phoneWindowManager = PhoneWindowManager.this;
                phoneWindowManager.mLockScreenTimerActive = false;
                phoneWindowManager.mLockNowPending = false;
                this.options = null;
            }
        }

        public void setLockOptions(Bundle bundle) {
            this.options = bundle;
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void lockNow(Bundle bundle) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
        this.mHandler.removeCallbacks(this.mScreenLockTimeout);
        if (bundle != null) {
            this.mScreenLockTimeout.setLockOptions(bundle);
        }
        Slog.d(TAG, "lockNow  options=" + bundle + ",mLockScreenTimerActive=" + this.mLockScreenTimerActive);
        this.mHandler.post(this.mScreenLockTimeout);
        synchronized (this.mScreenLockTimeout) {
            this.mLockNowPending = true;
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setAllowLockscreenWhenOn(int i, boolean z) {
        if (z) {
            this.mAllowLockscreenWhenOnDisplays.add(Integer.valueOf(i));
        } else {
            this.mAllowLockscreenWhenOnDisplays.remove(Integer.valueOf(i));
        }
        updateLockScreenTimeout();
    }

    private void updateLockScreenTimeout() {
        KeyguardServiceDelegate keyguardServiceDelegate;
        synchronized (this.mScreenLockTimeout) {
            if (this.mLockNowPending) {
                Log.w(TAG, "lockNow pending, ignore updating lockscreen timeout");
                return;
            }
            boolean z = !this.mAllowLockscreenWhenOnDisplays.isEmpty() && this.mDefaultDisplayPolicy.isAwake() && (keyguardServiceDelegate = this.mKeyguardDelegate) != null && keyguardServiceDelegate.isSecure(this.mCurrentUserId);
            if (this.mLockScreenTimerActive != z) {
                if (z) {
                    Log.v(TAG, "setting lockscreen timer");
                    this.mHandler.removeCallbacks(this.mScreenLockTimeout);
                    this.mHandler.postDelayed(this.mScreenLockTimeout, this.mLockScreenTimeout);
                } else {
                    Log.v(TAG, "clearing lockscreen timer");
                    this.mHandler.removeCallbacks(this.mScreenLockTimeout);
                }
                this.mLockScreenTimerActive = z;
            }
        }
    }

    private void updateScreenOffSleepToken(boolean z, boolean z2) {
        if (z) {
            this.mScreenOffSleepTokenAcquirer.acquire(0, z2);
        } else {
            this.mScreenOffSleepTokenAcquirer.release(0);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void enableScreenAfterBoot() {
        readLidState();
        applyLidSwitchState();
        ((DeviceStateManagerInternal) LocalServices.getService(DeviceStateManagerInternal.class)).enableDeviceStateAfterBoot(true);
        updateRotation(true);
    }

    private void applyLidSwitchState() {
        if (this.mDefaultDisplayPolicy.getLidState() == 0) {
            int lidBehavior = getLidBehavior();
            if (lidBehavior == 1) {
                sleepDefaultDisplay(SystemClock.uptimeMillis(), 3, 1);
            } else if (lidBehavior == 2) {
                this.mWindowManagerFuncs.lockDeviceNow();
            }
        }
        synchronized (this.mLock) {
            updateWakeGestureListenerLp();
        }
    }

    void updateUiMode() {
        if (this.mUiModeManager == null) {
            this.mUiModeManager = IUiModeManager.Stub.asInterface(ServiceManager.getService("uimode"));
        }
        try {
            this.mUiMode = this.mUiModeManager.getCurrentModeType();
        } catch (RemoteException unused) {
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public int getUiMode() {
        return this.mUiMode;
    }

    void updateRotation(boolean z) {
        this.mWindowManagerFuncs.updateRotation(z, false);
    }

    Intent createHomeDockIntent() {
        Intent intent;
        Bundle bundle;
        int i = this.mUiMode;
        if (i == 3) {
            if (this.mEnableCarDockHomeCapture) {
                intent = this.mCarDockIntent;
            }
            intent = null;
        } else {
            if (i != 2) {
                if (i == 6) {
                    int dockMode = this.mDefaultDisplayPolicy.getDockMode();
                    if (dockMode == 1 || dockMode == 4 || dockMode == 3) {
                        intent = this.mDeskDockIntent;
                    }
                } else if (i == 7) {
                    intent = this.mVrHeadsetHomeIntent;
                }
            }
            intent = null;
        }
        if (intent == null) {
            return null;
        }
        ResolveInfo resolveActivityAsUser = this.mPackageManager.resolveActivityAsUser(intent, 65664, this.mCurrentUserId);
        ActivityInfo activityInfo = resolveActivityAsUser != null ? resolveActivityAsUser.activityInfo : null;
        if (activityInfo == null || (bundle = activityInfo.metaData) == null || !bundle.getBoolean("android.dock_home")) {
            return null;
        }
        Intent intent2 = new Intent(intent);
        intent2.setClassName(activityInfo.packageName, activityInfo.name);
        return intent2;
    }

    void startDockOrHome(int i, boolean z, boolean z2, String str) {
        try {
            ActivityManager.getService().stopAppSwitches();
        } catch (RemoteException unused) {
        }
        sendCloseSystemWindows(SYSTEM_DIALOG_REASON_HOME_KEY);
        if (z2) {
            awakenDreams();
        }
        if (!this.mHasFeatureAuto && !isUserSetupComplete()) {
            Slog.i(TAG, "Not going home because user setup is in progress.");
            return;
        }
        Intent createHomeDockIntent = createHomeDockIntent();
        if (createHomeDockIntent != null) {
            if (z) {
                try {
                    createHomeDockIntent.putExtra("android.intent.extra.FROM_HOME_KEY", z);
                } catch (ActivityNotFoundException unused2) {
                }
            }
            startActivityAsUser(createHomeDockIntent, UserHandle.CURRENT);
            return;
        }
        if (DEBUG_WAKEUP) {
            Log.d(TAG, "startDockOrHome: startReason= " + str);
        }
        this.mActivityTaskManagerInternal.startHomeOnDisplay(this.mUserManagerInternal.getUserAssignedToDisplay(i), str, i, true, z);
    }

    void startDockOrHome(int i, boolean z, boolean z2) {
        startDockOrHome(i, z, z2, "startDockOrHome");
    }

    boolean goHome() {
        IActivityTaskManager service;
        String opPackageName;
        String attributionTag;
        Intent intent;
        if (!isUserSetupComplete()) {
            Slog.i(TAG, "Not going home because user setup is in progress.");
            return false;
        }
        try {
            if (SystemProperties.getInt("persist.sys.uts-test-mode", 0) == 1) {
                Log.d(TAG, "UTS-TEST-MODE");
            } else {
                ActivityManager.getService().stopAppSwitches();
                sendCloseSystemWindows();
                Intent createHomeDockIntent = createHomeDockIntent();
                if (createHomeDockIntent != null && ActivityTaskManager.getService().startActivityAsUser((IApplicationThread) null, this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), createHomeDockIntent, createHomeDockIntent.resolveTypeIfNeeded(this.mContext.getContentResolver()), (IBinder) null, (String) null, 0, 1, (ProfilerInfo) null, (Bundle) null, -2) == 1) {
                    return false;
                }
            }
            service = ActivityTaskManager.getService();
            opPackageName = this.mContext.getOpPackageName();
            attributionTag = this.mContext.getAttributionTag();
            intent = this.mHomeIntent;
        } catch (RemoteException unused) {
        }
        return service.startActivityAsUser((IApplicationThread) null, opPackageName, attributionTag, intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), (IBinder) null, (String) null, 0, 1, (ProfilerInfo) null, (Bundle) null, -2) != 1;
    }

    private boolean isTheaterModeEnabled() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "theater_mode_on", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean performHapticFeedback(int i, boolean z, String str) {
        return performHapticFeedback(Process.myUid(), this.mContext.getOpPackageName(), i, z, str);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isGlobalKey(int i) {
        return this.mGlobalKeyManager.shouldHandleGlobalKey(i);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean performHapticFeedback(int i, String str, int i2, boolean z, String str2) {
        VibrationEffect vibrationEffect;
        if (!this.mVibrator.hasVibrator() || (vibrationEffect = getVibrationEffect(i2)) == null) {
            return false;
        }
        VibrationAttributes vibrationAttributes = getVibrationAttributes(i2);
        if (z) {
            vibrationAttributes = new VibrationAttributes.Builder(vibrationAttributes).setFlags(2).build();
        }
        this.mVibrator.vibrate(i, str, vibrationEffect, str2, vibrationAttributes);
        return true;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x000c. Please report as an issue. */
    private VibrationEffect getVibrationEffect(int i) {
        if (i != 0) {
            if (i != 1) {
                switch (i) {
                    case 3:
                    case 5:
                    case 12:
                    case 15:
                    case 16:
                    case 19:
                    case 20:
                        break;
                    case 4:
                    case 27:
                        return VibrationEffect.get(MSG_SYSTEM_KEY_PRESS);
                    case 6:
                    case 13:
                    case 18:
                    case 23:
                    case 26:
                        return VibrationEffect.get(2);
                    case 7:
                    case 8:
                    case 10:
                    case 11:
                        return VibrationEffect.get(2, false);
                    case 9:
                        if (!this.mHapticTextHandleEnabled) {
                            return null;
                        }
                        return VibrationEffect.get(MSG_SYSTEM_KEY_PRESS);
                    case 14:
                    case MSG_SWITCH_KEYBOARD_LAYOUT /* 25 */:
                        break;
                    case 17:
                        return VibrationEffect.get(1);
                    case MSG_SYSTEM_KEY_PRESS /* 21 */:
                        return getScaledPrimitiveOrElseEffect(7, 0.5f, 2);
                    case MSG_HANDLE_ALL_APPS /* 22 */:
                        return getScaledPrimitiveOrElseEffect(8, 0.2f, MSG_SYSTEM_KEY_PRESS);
                    case MSG_RINGER_TOGGLE_CHORD /* 24 */:
                        return getScaledPrimitiveOrElseEffect(7, 0.4f, MSG_SYSTEM_KEY_PRESS);
                    default:
                        switch (i) {
                            case 10001:
                                long[] jArr = this.mSafeModeEnabledVibePattern;
                                if (jArr.length == 0) {
                                    return null;
                                }
                                if (jArr.length == 1) {
                                    return VibrationEffect.createOneShot(jArr[0], -1);
                                }
                                return VibrationEffect.createWaveform(jArr, -1);
                            case 10002:
                                if (this.mVibrator.areAllPrimitivesSupported(4, 7)) {
                                    return VibrationEffect.startComposition().addPrimitive(4, 0.25f).addPrimitive(7, 1.0f, 50).compose();
                                }
                                return VibrationEffect.get(5);
                            case 10003:
                                break;
                            default:
                                return null;
                        }
                }
            }
            return VibrationEffect.get(0);
        }
        return VibrationEffect.get(5);
    }

    private VibrationEffect getScaledPrimitiveOrElseEffect(int i, float f, int i2) {
        if (this.mVibrator.areAllPrimitivesSupported(i)) {
            return VibrationEffect.startComposition().addPrimitive(i, f).compose();
        }
        return VibrationEffect.get(i2);
    }

    private VibrationAttributes getVibrationAttributes(int i) {
        if (i == 14 || i == 15) {
            return PHYSICAL_EMULATION_VIBRATION_ATTRIBUTES;
        }
        if (i != 10002 && i != 10003) {
            switch (i) {
                case 18:
                case 19:
                case 20:
                    break;
                default:
                    return TOUCH_VIBRATION_ATTRIBUTES;
            }
        }
        return HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void keepScreenOnStoppedLw() {
        if (isKeyguardShowingAndNotOccluded()) {
            this.mPowerManager.userActivity(SystemClock.uptimeMillis(), false);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean hasNavigationBar() {
        return this.mDefaultDisplayPolicy.hasNavigationBar();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setDismissImeOnBackKeyPressed(boolean z) {
        this.mDismissImeOnBackKeyPressed = z;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setCurrentUserLw(int i) {
        this.mCurrentUserId = i;
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate != null) {
            keyguardServiceDelegate.setCurrentUser(i);
        }
        AccessibilityShortcutController accessibilityShortcutController = this.mAccessibilityShortcutController;
        if (accessibilityShortcutController != null) {
            accessibilityShortcutController.setCurrentUser(i);
        }
        StatusBarManagerInternal statusBarManagerInternal = getStatusBarManagerInternal();
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.setCurrentUser(i);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setSwitchingUser(boolean z) {
        this.mKeyguardDelegate.setSwitchingUser(z);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1159641169922L, this.mDefaultDisplayRotation.getUserRotationMode());
        protoOutputStream.write(1159641169923L, this.mDefaultDisplayRotation.getUserRotation());
        protoOutputStream.write(1159641169924L, this.mDefaultDisplayRotation.getCurrentAppOrientation());
        protoOutputStream.write(1133871366149L, this.mDefaultDisplayPolicy.isScreenOnFully());
        protoOutputStream.write(1133871366150L, this.mDefaultDisplayPolicy.isKeyguardDrawComplete());
        protoOutputStream.write(1133871366151L, this.mDefaultDisplayPolicy.isWindowManagerDrawComplete());
        protoOutputStream.write(1133871366156L, isKeyguardOccluded());
        protoOutputStream.write(1133871366157L, this.mKeyguardOccludedChanged);
        protoOutputStream.write(1133871366158L, this.mPendingKeyguardOccluded);
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate != null) {
            keyguardServiceDelegate.dumpDebug(protoOutputStream, 1146756268052L);
        }
        protoOutputStream.end(start);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void dump(String str, PrintWriter printWriter, String[] strArr) {
        printWriter.print(str);
        printWriter.print("mSafeMode=");
        printWriter.print(this.mSafeMode);
        printWriter.print(" mSystemReady=");
        printWriter.print(this.mSystemReady);
        printWriter.print(" mSystemBooted=");
        printWriter.println(this.mSystemBooted);
        printWriter.print(str);
        printWriter.print("mCameraLensCoverState=");
        printWriter.println(WindowManagerPolicy.WindowManagerFuncs.cameraLensStateToString(this.mCameraLensCoverState));
        printWriter.print(str);
        printWriter.print("mWakeGestureEnabledSetting=");
        printWriter.println(this.mWakeGestureEnabledSetting);
        printWriter.print(str);
        printWriter.print("mUiMode=");
        printWriter.print(Configuration.uiModeToString(this.mUiMode));
        printWriter.print("mEnableCarDockHomeCapture=");
        printWriter.println(this.mEnableCarDockHomeCapture);
        printWriter.print(str);
        printWriter.print("mLidKeyboardAccessibility=");
        printWriter.print(this.mLidKeyboardAccessibility);
        printWriter.print(" mLidNavigationAccessibility=");
        printWriter.print(this.mLidNavigationAccessibility);
        printWriter.print(" getLidBehavior=");
        printWriter.println(lidBehaviorToString(getLidBehavior()));
        printWriter.print(str);
        printWriter.print("mLongPressOnBackBehavior=");
        printWriter.println(longPressOnBackBehaviorToString(this.mLongPressOnBackBehavior));
        printWriter.print(str);
        printWriter.print("mLongPressOnHomeBehavior=");
        printWriter.println(longPressOnHomeBehaviorToString(this.mLongPressOnHomeBehavior));
        printWriter.print(str);
        printWriter.print("mDoubleTapOnHomeBehavior=");
        printWriter.println(doubleTapOnHomeBehaviorToString(this.mDoubleTapOnHomeBehavior));
        printWriter.print(str);
        printWriter.print("mShortPressOnPowerBehavior=");
        printWriter.println(shortPressOnPowerBehaviorToString(this.mShortPressOnPowerBehavior));
        printWriter.print(str);
        printWriter.print("mLongPressOnPowerBehavior=");
        printWriter.println(longPressOnPowerBehaviorToString(this.mLongPressOnPowerBehavior));
        printWriter.print(str);
        printWriter.print("mLongPressOnPowerAssistantTimeoutMs=");
        printWriter.println(this.mLongPressOnPowerAssistantTimeoutMs);
        printWriter.print(str);
        printWriter.print("mVeryLongPressOnPowerBehavior=");
        printWriter.println(veryLongPressOnPowerBehaviorToString(this.mVeryLongPressOnPowerBehavior));
        printWriter.print(str);
        printWriter.print("mDoublePressOnPowerBehavior=");
        printWriter.println(multiPressOnPowerBehaviorToString(this.mDoublePressOnPowerBehavior));
        printWriter.print(str);
        printWriter.print("mTriplePressOnPowerBehavior=");
        printWriter.println(multiPressOnPowerBehaviorToString(this.mTriplePressOnPowerBehavior));
        printWriter.print(str);
        printWriter.print("mPowerVolUpBehavior=");
        printWriter.println(powerVolumeUpBehaviorToString(this.mPowerVolUpBehavior));
        printWriter.print(str);
        printWriter.print("mShortPressOnSleepBehavior=");
        printWriter.println(shortPressOnSleepBehaviorToString(this.mShortPressOnSleepBehavior));
        printWriter.print(str);
        printWriter.print("mShortPressOnWindowBehavior=");
        printWriter.println(shortPressOnWindowBehaviorToString(this.mShortPressOnWindowBehavior));
        printWriter.print(str);
        printWriter.print("mShortPressOnStemPrimaryBehavior=");
        printWriter.println(shortPressOnStemPrimaryBehaviorToString(this.mShortPressOnStemPrimaryBehavior));
        printWriter.print(str);
        printWriter.print("mDoublePressOnStemPrimaryBehavior=");
        printWriter.println(doublePressOnStemPrimaryBehaviorToString(this.mDoublePressOnStemPrimaryBehavior));
        printWriter.print(str);
        printWriter.print("mTriplePressOnStemPrimaryBehavior=");
        printWriter.println(triplePressOnStemPrimaryBehaviorToString(this.mTriplePressOnStemPrimaryBehavior));
        printWriter.print(str);
        printWriter.print("mLongPressOnStemPrimaryBehavior=");
        printWriter.println(longPressOnStemPrimaryBehaviorToString(this.mLongPressOnStemPrimaryBehavior));
        printWriter.print(str);
        printWriter.print("mAllowStartActivityForLongPressOnPowerDuringSetup=");
        printWriter.println(this.mAllowStartActivityForLongPressOnPowerDuringSetup);
        printWriter.print(str);
        printWriter.print("mHasSoftInput=");
        printWriter.print(this.mHasSoftInput);
        printWriter.print(" mHapticTextHandleEnabled=");
        printWriter.println(this.mHapticTextHandleEnabled);
        printWriter.print(str);
        printWriter.print("mDismissImeOnBackKeyPressed=");
        printWriter.print(this.mDismissImeOnBackKeyPressed);
        printWriter.print(" mIncallPowerBehavior=");
        printWriter.println(incallPowerBehaviorToString(this.mIncallPowerBehavior));
        printWriter.print(str);
        printWriter.print("mIncallBackBehavior=");
        printWriter.print(incallBackBehaviorToString(this.mIncallBackBehavior));
        printWriter.print(" mEndcallBehavior=");
        printWriter.println(endcallBehaviorToString(this.mEndcallBehavior));
        printWriter.print(str);
        printWriter.print("mDisplayHomeButtonHandlers=");
        for (int i = 0; i < this.mDisplayHomeButtonHandlers.size(); i++) {
            printWriter.println(this.mDisplayHomeButtonHandlers.get(this.mDisplayHomeButtonHandlers.keyAt(i)));
        }
        printWriter.print(str);
        printWriter.print("mKeyguardOccluded=");
        printWriter.print(isKeyguardOccluded());
        printWriter.print(" mKeyguardOccludedChanged=");
        printWriter.print(this.mKeyguardOccludedChanged);
        printWriter.print(" mPendingKeyguardOccluded=");
        printWriter.println(this.mPendingKeyguardOccluded);
        printWriter.print(str);
        printWriter.print("mAllowLockscreenWhenOnDisplays=");
        printWriter.print(!this.mAllowLockscreenWhenOnDisplays.isEmpty());
        printWriter.print(" mLockScreenTimeout=");
        printWriter.print(this.mLockScreenTimeout);
        printWriter.print(" mLockScreenTimerActive=");
        printWriter.println(this.mLockScreenTimerActive);
        this.mGlobalKeyManager.dump(str, printWriter);
        this.mKeyCombinationManager.dump(str, printWriter);
        this.mSingleKeyGestureDetector.dump(str, printWriter);
        MyWakeGestureListener myWakeGestureListener = this.mWakeGestureListener;
        if (myWakeGestureListener != null) {
            myWakeGestureListener.dump(printWriter, str);
        }
        BurnInProtectionHelper burnInProtectionHelper = this.mBurnInProtectionHelper;
        if (burnInProtectionHelper != null) {
            burnInProtectionHelper.dump(str, printWriter);
        }
        KeyguardServiceDelegate keyguardServiceDelegate = this.mKeyguardDelegate;
        if (keyguardServiceDelegate != null) {
            keyguardServiceDelegate.dump(str, printWriter);
        }
        printWriter.print(str);
        printWriter.println("Looper state:");
        this.mHandler.getLooper().dump(new PrintWriterPrinter(printWriter), str + "  ");
    }

    private static String endcallBehaviorToString(int i) {
        StringBuilder sb = new StringBuilder();
        if ((i & 1) != 0) {
            sb.append("home|");
        }
        if ((i & 2) != 0) {
            sb.append("sleep|");
        }
        int length = sb.length();
        return length == 0 ? "<nothing>" : sb.substring(0, length - 1);
    }

    private static String longPressOnBackBehaviorToString(int i) {
        return i != 0 ? i != 1 ? Integer.toString(i) : "LONG_PRESS_BACK_GO_TO_VOICE_ASSIST" : "LONG_PRESS_BACK_NOTHING";
    }

    private static String longPressOnHomeBehaviorToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? Integer.toString(i) : "LONG_PRESS_HOME_NOTIFICATION_PANEL" : "LONG_PRESS_HOME_ASSIST" : "LONG_PRESS_HOME_ALL_APPS" : "LONG_PRESS_HOME_NOTHING";
    }

    private static String doubleTapOnHomeBehaviorToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? Integer.toString(i) : "DOUBLE_TAP_HOME_PIP_MENU" : "DOUBLE_TAP_HOME_RECENT_SYSTEM_UI" : "DOUBLE_TAP_HOME_NOTHING";
    }

    private static String shortPressOnPowerBehaviorToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? Integer.toString(i) : "SHORT_PRESS_POWER_CLOSE_IME_OR_GO_HOME" : "SHORT_PRESS_POWER_GO_HOME" : "SHORT_PRESS_POWER_REALLY_GO_TO_SLEEP_AND_GO_HOME" : "SHORT_PRESS_POWER_REALLY_GO_TO_SLEEP" : "SHORT_PRESS_POWER_GO_TO_SLEEP" : "SHORT_PRESS_POWER_NOTHING";
    }

    private static String longPressOnPowerBehaviorToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? Integer.toString(i) : "LONG_PRESS_POWER_ASSISTANT" : "LONG_PRESS_POWER_GO_TO_VOICE_ASSIST" : "LONG_PRESS_POWER_SHUT_OFF_NO_CONFIRM" : "LONG_PRESS_POWER_SHUT_OFF" : "LONG_PRESS_POWER_GLOBAL_ACTIONS" : "LONG_PRESS_POWER_NOTHING";
    }

    private static String veryLongPressOnPowerBehaviorToString(int i) {
        return i != 0 ? i != 1 ? Integer.toString(i) : "VERY_LONG_PRESS_POWER_GLOBAL_ACTIONS" : "VERY_LONG_PRESS_POWER_NOTHING";
    }

    private static String powerVolumeUpBehaviorToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? Integer.toString(i) : "POWER_VOLUME_UP_BEHAVIOR_GLOBAL_ACTIONS" : "POWER_VOLUME_UP_BEHAVIOR_MUTE" : "POWER_VOLUME_UP_BEHAVIOR_NOTHING";
    }

    private static String multiPressOnPowerBehaviorToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? Integer.toString(i) : "MULTI_PRESS_POWER_LAUNCH_TARGET_ACTIVITY" : "MULTI_PRESS_POWER_BRIGHTNESS_BOOST" : "MULTI_PRESS_POWER_THEATER_MODE" : "MULTI_PRESS_POWER_NOTHING";
    }

    private static String shortPressOnSleepBehaviorToString(int i) {
        return i != 0 ? i != 1 ? Integer.toString(i) : "SHORT_PRESS_SLEEP_GO_TO_SLEEP_AND_GO_HOME" : "SHORT_PRESS_SLEEP_GO_TO_SLEEP";
    }

    private static String shortPressOnWindowBehaviorToString(int i) {
        return i != 0 ? i != 1 ? Integer.toString(i) : "SHORT_PRESS_WINDOW_PICTURE_IN_PICTURE" : "SHORT_PRESS_WINDOW_NOTHING";
    }

    private static String shortPressOnStemPrimaryBehaviorToString(int i) {
        return i != 0 ? i != 1 ? Integer.toString(i) : "SHORT_PRESS_PRIMARY_LAUNCH_ALL_APPS" : "SHORT_PRESS_PRIMARY_NOTHING";
    }

    private static String doublePressOnStemPrimaryBehaviorToString(int i) {
        return i != 0 ? i != 1 ? Integer.toString(i) : "DOUBLE_PRESS_PRIMARY_SWITCH_RECENT_APP" : "DOUBLE_PRESS_PRIMARY_NOTHING";
    }

    private static String triplePressOnStemPrimaryBehaviorToString(int i) {
        return i != 0 ? i != 1 ? Integer.toString(i) : "TRIPLE_PRESS_PRIMARY_TOGGLE_ACCESSIBILITY" : "TRIPLE_PRESS_PRIMARY_NOTHING";
    }

    private static String longPressOnStemPrimaryBehaviorToString(int i) {
        return i != 0 ? i != 1 ? Integer.toString(i) : "LONG_PRESS_PRIMARY_LAUNCH_VOICE_ASSISTANT" : "LONG_PRESS_PRIMARY_NOTHING";
    }

    private static String lidBehaviorToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? Integer.toString(i) : "LID_BEHAVIOR_LOCK" : "LID_BEHAVIOR_SLEEP" : "LID_BEHAVIOR_NONE";
    }

    public static boolean isLongPressToAssistantEnabled(Context context) {
        int intForUser = Settings.System.getIntForUser(context.getContentResolver(), "clockwork_long_press_to_assistant_enabled", 1, -2);
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "longPressToAssistant = " + intForUser);
        }
        return intForUser == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class HdmiVideoExtconUEventObserver extends ExtconStateObserver<Boolean> {
        private static final String HDMI_EXIST = "HDMI=1";
        private static final String NAME = "hdmi";

        private HdmiVideoExtconUEventObserver() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean init(ExtconUEventObserver.ExtconInfo extconInfo) {
            boolean z;
            try {
                z = ((Boolean) parseStateFromFile(extconInfo)).booleanValue();
            } catch (FileNotFoundException e) {
                Slog.w(PhoneWindowManager.TAG, extconInfo.getStatePath() + " not found while attempting to determine initial state", e);
                z = false;
                startObserving(extconInfo);
                return z;
            } catch (IOException e2) {
                Slog.e(PhoneWindowManager.TAG, "Error reading " + extconInfo.getStatePath() + " while attempting to determine initial state", e2);
                z = false;
                startObserving(extconInfo);
                return z;
            }
            startObserving(extconInfo);
            return z;
        }

        public void updateState(ExtconUEventObserver.ExtconInfo extconInfo, String str, Boolean bool) {
            PhoneWindowManager.this.mDefaultDisplayPolicy.setHdmiPlugged(bool.booleanValue());
        }

        /* renamed from: parseState, reason: merged with bridge method [inline-methods] */
        public Boolean m2541parseState(ExtconUEventObserver.ExtconInfo extconInfo, String str) {
            return Boolean.valueOf(str.contains(HDMI_EXIST));
        }
    }

    private void launchTargetSearchActivity() {
        Intent intent;
        if (this.mSearchKeyTargetActivity != null) {
            intent = new Intent();
            intent.setComponent(this.mSearchKeyTargetActivity);
        } else {
            intent = new Intent("android.intent.action.WEB_SEARCH");
        }
        intent.addFlags(270532608);
        try {
            startActivityAsUser(intent, UserHandle.CURRENT_OR_SELF);
        } catch (ActivityNotFoundException unused) {
            Slog.e(TAG, "Could not resolve activity with : " + intent.getComponent().flattenToString() + " name.");
        }
    }

    public IPhoneWindowManagerWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class PhoneWindowManagerWrapper implements IPhoneWindowManagerWrapper {
        private PhoneWindowManagerWrapper() {
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public IPhoneWindowManagerExt getExtImpl() {
            return PhoneWindowManager.this.mPhoneWindowManagerExt;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void setlocalLOGV(boolean z) {
            PhoneWindowManager.localLOGV = z;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void setDebugInput(boolean z) {
            PhoneWindowManager.DEBUG_INPUT = z;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void setDebugKeyguard(boolean z) {
            PhoneWindowManager.DEBUG_KEYGUARD = z;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void setDebugWakeup(boolean z) {
            PhoneWindowManager.DEBUG_WAKEUP = z;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public boolean performHapticFeedback(int i, boolean z, String str) {
            return PhoneWindowManager.this.performHapticFeedback(i, z, str);
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public KeyCombinationManager getKeyCombinationManager() {
            return PhoneWindowManager.this.mKeyCombinationManager;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void interceptRingerToggleChord() {
            PhoneWindowManager.this.interceptRingerToggleChord();
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void cancelPendingRingerToggleChordAction() {
            PhoneWindowManager.this.cancelPendingRingerToggleChordAction();
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void cancelGlobalActionsAction() {
            PhoneWindowManager.this.cancelGlobalActionsAction();
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public SingleKeyGestureDetector getSingleKeyGestureDetector() {
            return PhoneWindowManager.this.mSingleKeyGestureDetector;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void launchAssistAction(String str, int i, long j, int i2, int i3) {
            PhoneWindowManager.this.launchAssistAction(str, i, j, i2, i3);
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void finishPowerKeyPress() {
            PhoneWindowManager.this.finishPowerKeyPress();
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void cancelPreloadRecentApps() {
            PhoneWindowManager.this.cancelPreloadRecentApps();
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public Object getLock() {
            return PhoneWindowManager.this.mLock;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void powerPress(long j, int i, boolean z) {
            PhoneWindowManager.this.powerPress(j, i, z);
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void wakeUpFromPowerKey(long j) {
            PhoneWindowManager.this.wakeUpFromPowerKey(j);
        }
    }
}
