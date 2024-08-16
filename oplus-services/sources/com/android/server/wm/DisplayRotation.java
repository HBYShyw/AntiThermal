package com.android.server.wm;

import android.R;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Debug;
import android.os.Handler;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.ArraySet;
import android.util.RotationUtils;
import android.util.Slog;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import android.view.DisplayAddress;
import android.view.DisplayInfo;
import android.view.Surface;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.server.LocalServices;
import com.android.server.UiThread;
import com.android.server.display.IMirageDisplayManagerExt;
import com.android.server.policy.PhoneWindowManager;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.statusbar.StatusBarManagerInternal;
import com.android.server.wm.DeviceStateController;
import com.android.server.wm.DisplayRotation;
import com.android.server.wm.RemoteDisplayChangeController;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DisplayRotation {
    private static final int ALLOW_ALL_ROTATIONS_DISABLED = 0;
    private static final int ALLOW_ALL_ROTATIONS_ENABLED = 1;
    private static final int ALLOW_ALL_ROTATIONS_UNDEFINED = -1;
    private static final int CAMERA_ROTATION_DISABLED = 0;
    private static final int CAMERA_ROTATION_ENABLED = 1;
    private static final int FOLDING_RECOMPUTE_CONFIG_DELAY_MS = 800;
    private static final int ROTATION_UNDEFINED = -1;
    private static final String TAG = "WindowManager";
    public final boolean isDefaultDisplay;
    private int mAllowAllRotations;
    private final boolean mAllowRotationResolver;
    private boolean mAllowSeamlessRotationDespiteNavBarMoving;
    private int mCameraRotationMode;
    private final int mCarDockRotation;
    private final DisplayRotationImmersiveAppCompatPolicy mCompatPolicyForImmersiveApps;
    private final Context mContext;
    private int mCurrentAppOrientation;

    @VisibleForTesting
    final Runnable mDefaultDisplayRotationChangedCallback;
    private boolean mDefaultFixedToUserRotation;
    private int mDeferredRotationPauseCount;
    private int mDemoHdmiRotation;
    private boolean mDemoHdmiRotationLock;
    private int mDemoRotation;
    private boolean mDemoRotationLock;
    private final int mDeskDockRotation;
    private final DeviceStateController mDeviceStateController;
    private final DisplayContent mDisplayContent;
    private final DisplayPolicy mDisplayPolicy;
    private final DisplayRotationCoordinator mDisplayRotationCoordinator;
    private IDisplayRotationExt mDisplayRotationExt;
    public IDisplayRotationSocExt mDisplayRotationSocExt;
    private final DisplayWindowSettings mDisplayWindowSettings;
    private int mFixedToUserRotation;
    final FoldController mFoldController;

    @VisibleForTesting
    int mLandscapeRotation;
    private int mLastOrientation;
    int mLastSensorRotation;
    private final int mLidOpenRotation;
    private final Object mLock;
    private OrientationListener mOrientationListener;

    @VisibleForTesting
    int mPortraitRotation;
    private boolean mRotatingSeamlessly;
    private int mRotation;
    private int mRotationChoiceShownToUserForConfirmation;
    private final RotationHistory mRotationHistory;
    private int mSeamlessRotationCount;

    @VisibleForTesting
    int mSeascapeRotation;
    private final WindowManagerService mService;
    private SettingsObserver mSettingsObserver;
    private int mShowRotationSuggestions;
    private StatusBarManagerInternal mStatusBarManagerInternal;
    private final boolean mSupportAutoRotation;
    private final RotationAnimationPair mTmpRotationAnim;
    private final int mUndockedHdmiRotation;

    @VisibleForTesting
    int mUpsideDownRotation;
    private int mUserRotation;
    private int mUserRotationMode;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private @interface AllowAllRotations {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class RotationAnimationPair {
        int mEnter;
        int mExit;

        private RotationAnimationPair() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayRotation(WindowManagerService windowManagerService, DisplayContent displayContent, DisplayAddress displayAddress, DeviceStateController deviceStateController, DisplayRotationCoordinator displayRotationCoordinator) {
        this(windowManagerService, displayContent, displayAddress, displayContent.getDisplayPolicy(), windowManagerService.mDisplayWindowSettings, windowManagerService.mContext, windowManagerService.getWindowManagerLock(), deviceStateController, displayRotationCoordinator);
    }

    @VisibleForTesting
    DisplayRotation(WindowManagerService windowManagerService, DisplayContent displayContent, DisplayAddress displayAddress, DisplayPolicy displayPolicy, DisplayWindowSettings displayWindowSettings, Context context, Object obj, DeviceStateController deviceStateController, DisplayRotationCoordinator displayRotationCoordinator) {
        this.mDisplayRotationExt = (IDisplayRotationExt) ExtLoader.type(IDisplayRotationExt.class).base(this).create();
        this.mDisplayRotationSocExt = (IDisplayRotationSocExt) ExtLoader.type(IDisplayRotationSocExt.class).base(this).create();
        this.mTmpRotationAnim = new RotationAnimationPair();
        this.mRotationHistory = new RotationHistory();
        this.mCurrentAppOrientation = -1;
        this.mLastOrientation = -1;
        this.mLastSensorRotation = -1;
        this.mRotationChoiceShownToUserForConfirmation = -1;
        this.mAllowAllRotations = -1;
        this.mUserRotationMode = 0;
        this.mUserRotation = 0;
        this.mCameraRotationMode = 0;
        this.mFixedToUserRotation = 0;
        this.mService = windowManagerService;
        this.mDisplayContent = displayContent;
        this.mDisplayPolicy = displayPolicy;
        this.mDisplayWindowSettings = displayWindowSettings;
        this.mContext = context;
        this.mLock = obj;
        this.mDeviceStateController = deviceStateController;
        boolean z = displayContent.isDefaultDisplay;
        this.isDefaultDisplay = z;
        this.mCompatPolicyForImmersiveApps = initImmersiveAppCompatPolicy(windowManagerService, displayContent);
        boolean z2 = context.getResources().getBoolean(17891841);
        this.mSupportAutoRotation = z2;
        this.mAllowRotationResolver = context.getResources().getBoolean(R.bool.config_allowTheaterModeWakeFromPowerKey);
        this.mLidOpenRotation = readRotation(R.integer.config_notificationsBatteryLowARGB);
        this.mCarDockRotation = readRotation(R.integer.config_defaultNotificationLedOff);
        this.mDeskDockRotation = readRotation(R.integer.config_globalActionsKeyTimeout);
        this.mUndockedHdmiRotation = readRotation(R.integer.leanback_setup_translation_backward_out_content_duration);
        int readDefaultDisplayRotation = readDefaultDisplayRotation(displayAddress);
        this.mRotation = readDefaultDisplayRotation;
        this.mDisplayRotationCoordinator = displayRotationCoordinator;
        if (z) {
            displayRotationCoordinator.setDefaultDisplayDefaultRotation(readDefaultDisplayRotation);
        }
        Runnable runnable = new Runnable() { // from class: com.android.server.wm.DisplayRotation$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                DisplayRotation.this.updateRotationAndSendNewConfigIfChanged();
            }
        };
        this.mDefaultDisplayRotationChangedCallback = runnable;
        if (DisplayRotationCoordinator.isSecondaryInternalDisplay(displayContent) && deviceStateController.shouldMatchBuiltInDisplayOrientationToReverseDefaultDisplay()) {
            displayRotationCoordinator.setDefaultDisplayRotationChangedCallback(runnable);
        }
        if (z) {
            Handler handler = UiThread.getHandler();
            OrientationListener orientationListener = new OrientationListener(context, handler, readDefaultDisplayRotation);
            this.mOrientationListener = orientationListener;
            orientationListener.setCurrentRotation(this.mRotation);
            SettingsObserver settingsObserver = new SettingsObserver(handler);
            this.mSettingsObserver = settingsObserver;
            settingsObserver.observe();
            if (z2 && context.getResources().getBoolean(17891914)) {
                this.mFoldController = new FoldController();
            } else {
                this.mFoldController = null;
            }
        } else {
            if (this.mDisplayRotationExt.isSecondDisplay(displayContent)) {
                Handler handler2 = UiThread.getHandler();
                OrientationListener orientationListener2 = new OrientationListener(context, handler2, readDefaultDisplayRotation);
                this.mOrientationListener = orientationListener2;
                orientationListener2.setCurrentRotation(this.mRotation);
                SettingsObserver settingsObserver2 = new SettingsObserver(handler2);
                this.mSettingsObserver = settingsObserver2;
                settingsObserver2.observe();
                this.mDisplayRotationExt.registerFoldStateListener(context, handler2, obj);
            }
            this.mFoldController = null;
        }
        if (z) {
            int i = SystemProperties.getInt("ro.panel.pad_orientation", 0) / 90;
            this.mRotation = i;
            this.mUserRotation = i;
            this.mDisplayRotationSocExt.hookRegisterWifiDisplay(context, windowManagerService);
        }
        int mirageInitialRotation = this.mDisplayRotationExt.getMirageInitialRotation(displayContent.getDisplayId());
        if (mirageInitialRotation != -1) {
            Slog.d(TAG, "Set init rotation " + mirageInitialRotation + " for display " + displayContent.mDisplayId);
            this.mRotation = mirageInitialRotation;
        }
    }

    @VisibleForTesting
    DisplayRotationImmersiveAppCompatPolicy initImmersiveAppCompatPolicy(WindowManagerService windowManagerService, DisplayContent displayContent) {
        return DisplayRotationImmersiveAppCompatPolicy.createIfNeeded(windowManagerService.mLetterboxConfiguration, this, displayContent);
    }

    private int readDefaultDisplayRotation(DisplayAddress displayAddress) {
        if (!(displayAddress instanceof DisplayAddress.Physical)) {
            return 0;
        }
        String str = SystemProperties.get("ro.bootanim.set_orientation_" + ((DisplayAddress.Physical) displayAddress).getPhysicalDisplayId(), "ORIENTATION_0");
        if (str.equals("ORIENTATION_90")) {
            return 1;
        }
        if (str.equals("ORIENTATION_180")) {
            return 2;
        }
        return str.equals("ORIENTATION_270") ? 3 : 0;
    }

    private int readRotation(int i) {
        try {
            int integer = this.mContext.getResources().getInteger(i);
            if (integer == 0) {
                return 0;
            }
            if (integer == 90) {
                return 1;
            }
            if (integer != 180) {
                return integer != 270 ? -1 : 3;
            }
            return 2;
        } catch (Resources.NotFoundException unused) {
            return -1;
        }
    }

    @VisibleForTesting
    boolean useDefaultSettingsProvider() {
        return this.isDefaultDisplay;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateUserDependentConfiguration(Resources resources) {
        this.mAllowSeamlessRotationDespiteNavBarMoving = resources.getBoolean(R.bool.config_allowTheaterModeWakeFromUnplug);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void configure(int i, int i2) {
        Resources resources = this.mContext.getResources();
        if (i > i2) {
            this.mLandscapeRotation = 0;
            this.mSeascapeRotation = 2;
            if (resources.getBoolean(17891798)) {
                this.mPortraitRotation = 1;
                this.mUpsideDownRotation = 3;
            } else {
                this.mPortraitRotation = 3;
                this.mUpsideDownRotation = 1;
            }
        } else {
            this.mPortraitRotation = 0;
            this.mUpsideDownRotation = 2;
            if (resources.getBoolean(17891798)) {
                this.mLandscapeRotation = 3;
                this.mSeascapeRotation = 1;
            } else {
                this.mLandscapeRotation = 1;
                this.mSeascapeRotation = 3;
            }
        }
        if ("portrait".equals(SystemProperties.get("persist.demo.hdmirotation"))) {
            this.mDemoHdmiRotation = this.mPortraitRotation;
        } else {
            this.mDemoHdmiRotation = this.mLandscapeRotation;
        }
        this.mDemoHdmiRotationLock = SystemProperties.getBoolean("persist.demo.hdmirotationlock", false);
        if ("portrait".equals(SystemProperties.get("persist.demo.remoterotation"))) {
            this.mDemoRotation = this.mPortraitRotation;
        } else {
            this.mDemoRotation = this.mLandscapeRotation;
        }
        this.mDemoRotationLock = SystemProperties.getBoolean("persist.demo.rotationlock", false);
        this.mDefaultFixedToUserRotation = (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive") || this.mContext.getPackageManager().hasSystemFeature("android.software.leanback") || this.mService.mIsPc || this.mDisplayContent.forceDesktopMode()) && !"true".equals(SystemProperties.get("config.override_forced_orient"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyCurrentRotation(int i) {
        this.mRotationHistory.addRecord(this, i);
        OrientationListener orientationListener = this.mOrientationListener;
        if (orientationListener != null) {
            orientationListener.setCurrentRotation(i);
        }
    }

    @VisibleForTesting
    void setRotation(int i) {
        this.mRotation = i;
        Slog.w(TAG, "setRotation = " + i + "; callers:" + Debug.getCallers(30));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRotation() {
        return this.mRotation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLastOrientation() {
        return this.mLastOrientation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateOrientation(int i, boolean z) {
        if (i == this.mLastOrientation && !z) {
            if (!this.mDisplayRotationExt.checkForceUpdate(this.mDisplayContent)) {
                return false;
            }
            Slog.d(TAG, "still update rotation even if orientation no changed.");
        }
        Slog.d(TAG, "updateOrientation: mLastOrientation = " + ActivityInfo.screenOrientationToString(this.mLastOrientation) + "(" + this.mLastOrientation + ") newOrientation = " + ActivityInfo.screenOrientationToString(i) + "(" + i + ") forceUpdate = " + z + " LastOrientationSource = " + this.mDisplayContent.getLastOrientationSource());
        this.mLastOrientation = i;
        if (i != this.mCurrentAppOrientation) {
            this.mCurrentAppOrientation = i;
            if (this.isDefaultDisplay) {
                updateOrientationListenerLw();
            }
        }
        return updateRotationUnchecked(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateRotationAndSendNewConfigIfChanged() {
        boolean updateRotationUnchecked = updateRotationUnchecked(false);
        if (updateRotationUnchecked) {
            this.mDisplayContent.sendNewConfiguration();
        }
        return updateRotationUnchecked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateRotationUnchecked(boolean z) {
        String str;
        TransitionRequestInfo.DisplayChange displayChange;
        int displayId = this.mDisplayContent.getDisplayId();
        if (this.mDisplayRotationExt.hasMaskAnimation()) {
            Slog.v(TAG, "Deferring rotation, mask animation in progress.");
            return false;
        }
        if (!z) {
            if (this.mDeferredRotationPauseCount > 0) {
                if (!ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                    Slog.d(TAG, "Deferring rotation, rotation is paused.");
                } else if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1497304204, 0, (String) null, (Object[]) null);
                }
                return false;
            }
            if (this.mDisplayContent.inTransition() && this.mDisplayContent.getDisplayPolicy().isScreenOnFully() && !this.mDisplayContent.mTransitionController.useShellTransitionsRotation()) {
                if (!ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                    Slog.d(TAG, "Deferring rotation, animation in progress.");
                } else if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 292904800, 0, (String) null, (Object[]) null);
                }
                Slog.d(TAG, "animation is in process, force update in next orientation update.");
                this.mDisplayRotationExt.setForceUpdateRotation(true);
                return false;
            }
            if (this.mService.mDisplayFrozen && !this.mDisplayRotationExt.enableRequestOrientationWhenDeviceFolding(this.mDisplayContent)) {
                if (!ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                    Slog.d(TAG, "Deferring rotation, still finishing previous rotation");
                } else if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 1947239194, 0, (String) null, (Object[]) null);
                }
                Slog.d(TAG, "previous rotation is finishing, force update in next orientation update.");
                this.mDisplayRotationExt.setForceUpdateRotation(true);
                return false;
            }
            if (this.mDisplayContent.mFixedRotationTransitionListener.shouldDeferRotation()) {
                this.mLastOrientation = -2;
                if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                    Slog.d(TAG, "updateRotationUnchecked :ignore rotation, recents animation running");
                }
                return false;
            }
        }
        if (this.mService.getWrapper().getExtImpl().isRotationLockForBootAnimation()) {
            Slog.v(TAG, "Do not rotation when shutdown");
            return false;
        }
        if (!this.mService.mDisplayEnabled) {
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -1117599386, 0, (String) null, (Object[]) null);
            }
            return false;
        }
        int i = this.mRotation;
        int i2 = this.mLastOrientation;
        int rotationForOrientation = rotationForOrientation(i2, i);
        FoldController foldController = this.mFoldController;
        if (foldController != null && foldController.shouldRevertOverriddenRotation()) {
            int revertOverriddenRotation = this.mFoldController.revertOverriddenRotation();
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -1043981272, 0, (String) null, new Object[]{String.valueOf(Surface.rotationToString(revertOverriddenRotation)), String.valueOf(Surface.rotationToString(i)), String.valueOf(Surface.rotationToString(rotationForOrientation))});
            }
            rotationForOrientation = revertOverriddenRotation;
        }
        if (DisplayRotationCoordinator.isSecondaryInternalDisplay(this.mDisplayContent) && this.mDeviceStateController.shouldMatchBuiltInDisplayOrientationToReverseDefaultDisplay() && !this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent)) {
            rotationForOrientation = RotationUtils.reverseRotationDirectionAroundZAxis(this.mDisplayRotationCoordinator.getDefaultDisplayCurrentRotation());
        }
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            String valueOf = String.valueOf(Surface.rotationToString(rotationForOrientation));
            String valueOf2 = String.valueOf(ActivityInfo.screenOrientationToString(i2));
            str = TAG;
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -1263316010, 4372, (String) null, new Object[]{valueOf, Long.valueOf(rotationForOrientation), Long.valueOf(displayId), valueOf2, Long.valueOf(i2), String.valueOf(Surface.rotationToString(i)), Long.valueOf(i)});
        } else {
            str = TAG;
        }
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            displayChange = null;
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -766059044, 273, (String) null, new Object[]{Long.valueOf(displayId), String.valueOf(ActivityInfo.screenOrientationToString(i2)), Long.valueOf(i2), String.valueOf(Surface.rotationToString(rotationForOrientation)), Long.valueOf(rotationForOrientation)});
        } else {
            displayChange = null;
        }
        if (i == rotationForOrientation) {
            Slog.d(str, "rotation(" + Surface.rotationToString(rotationForOrientation) + ") no changed.");
            this.mDisplayRotationExt.setSensorRotationChanged(this.mDisplayContent, false);
            return false;
        }
        String str2 = str;
        if (this.isDefaultDisplay) {
            this.mDisplayRotationCoordinator.onDefaultDisplayRotationChanged(rotationForOrientation);
        }
        RecentsAnimationController recentsAnimationController = this.mService.getRecentsAnimationController();
        if (!this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent) && recentsAnimationController != null) {
            recentsAnimationController.cancelAnimationForDisplayChange();
        }
        if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
            Slog.d(str2, "updateRotationUnchecked :rotation changed by orientationSource:" + this.mDisplayContent.getLastOrientationSource() + ", caller:" + Debug.getCallers(30));
        }
        if (this.mDisplayRotationExt.stopRotationInGame(this.mDisplayContent.getLastOrientationSource())) {
            Slog.d(str2, "updateRotationUnchecked: ignore rotation under GameSpace control");
            return false;
        }
        if (this.mDisplayRotationExt.stopRotationInPutt(this.mDisplayContent.getLastOrientationSource(), displayId)) {
            Slog.d(str2, "updateRotationUnchecked: ignore rotation under one putt");
            return false;
        }
        Slog.i(str2, "Display id " + displayId + " rotation changed to " + Surface.rotationToString(rotationForOrientation) + " (" + rotationForOrientation + ") from " + Surface.rotationToString(i) + " (" + i + "), lastOrientation = " + ActivityInfo.screenOrientationToString(i2) + " (" + i2 + ") lastOrientationSource = " + this.mDisplayContent.getLastOrientationSource());
        this.mRotation = rotationForOrientation;
        this.mDisplayRotationExt.updateRotation(rotationForOrientation, this.mDisplayContent);
        this.mDisplayContent.setLayoutNeeded();
        DisplayContent displayContent = this.mDisplayContent;
        displayContent.mWaitingForConfig = true;
        if (displayContent.mTransitionController.isShellTransitionsEnabled()) {
            boolean isCollecting = this.mDisplayContent.mTransitionController.isCollecting();
            this.mDisplayContent.requestChangeTransitionIfNeeded(536870912, isCollecting ? displayChange : new TransitionRequestInfo.DisplayChange(this.mDisplayContent.getDisplayId(), i, this.mRotation));
            if (!isCollecting) {
                return true;
            }
            startRemoteRotation(i, this.mRotation);
            return true;
        }
        WindowManagerService windowManagerService = this.mService;
        windowManagerService.mWindowsFreezingScreen = 1;
        windowManagerService.mH.sendNewMessageDelayed(11, this.mDisplayContent, 2000L);
        if (shouldRotateSeamlessly(i, rotationForOrientation, z)) {
            prepareSeamlessRotation();
        } else {
            prepareNormalRotationAnimation();
        }
        startRemoteRotation(i, this.mRotation);
        return true;
    }

    private void startRemoteRotation(int i, final int i2) {
        if (this.mDisplayContent.getWrapper().getExtImpl().isActivityPreloadDisplay(this.mDisplayContent)) {
            Slog.v("OAPM", "startRemoteRotation : temp return! ");
            return;
        }
        if (WindowManagerDebugConfig.DEBUG_ANIM) {
            Slog.v(TAG, "Start remote rotation : fromRotation : " + i + "; toRotation : " + i2 + ";callers:" + Debug.getCallers(20));
        }
        this.mDisplayContent.mRemoteDisplayChangeController.performRemoteDisplayChange(i, i2, null, new RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback() { // from class: com.android.server.wm.DisplayRotation$$ExternalSyntheticLambda0
            @Override // com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback
            public final void onContinueRemoteDisplayChange(WindowContainerTransaction windowContainerTransaction) {
                DisplayRotation.this.lambda$startRemoteRotation$0(i2, windowContainerTransaction);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: continueRotation, reason: merged with bridge method [inline-methods] */
    public void lambda$startRemoteRotation$0(int i, WindowContainerTransaction windowContainerTransaction) {
        if (WindowManagerDebugConfig.DEBUG_ANIM) {
            Slog.v(TAG, "Continue rotation : targetRotation : " + i + "; mRotation : " + this.mRotation + "; callers:" + Debug.getCallers(10));
        }
        if (i != this.mRotation) {
            if (this.mDisplayContent.mWaitingForConfig) {
                Slog.e(TAG, "continueRotation  mWaitingForConfig is true,targetRotation=" + i + " mRotation=" + this.mRotation + ",callers:" + Debug.getCallers(3));
                return;
            }
            return;
        }
        if (this.mDisplayContent.mTransitionController.isShellTransitionsEnabled()) {
            if (!this.mDisplayContent.mTransitionController.isCollecting()) {
                Slog.e(TAG, "Trying to continue rotation outside a transition");
            }
            DisplayContent displayContent = this.mDisplayContent;
            displayContent.mTransitionController.collect(displayContent);
        }
        this.mService.mAtmService.deferWindowLayout();
        try {
            this.mDisplayContent.sendNewConfiguration();
            if (windowContainerTransaction != null) {
                this.mService.mAtmService.mWindowOrganizerController.applyTransaction(windowContainerTransaction);
            }
            this.mDisplayRotationExt.continueRotation();
        } finally {
            this.mService.mAtmService.continueWindowLayout();
        }
    }

    void prepareNormalRotationAnimation() {
        cancelSeamlessRotation();
        RotationAnimationPair selectRotationAnimation = selectRotationAnimation();
        this.mService.startFreezingDisplay(selectRotationAnimation.mExit, selectRotationAnimation.mEnter, this.mDisplayContent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelSeamlessRotation() {
        if (this.mRotatingSeamlessly) {
            this.mDisplayContent.forAllWindows(new Consumer() { // from class: com.android.server.wm.DisplayRotation$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DisplayRotation.lambda$cancelSeamlessRotation$1((WindowState) obj);
                }
            }, true);
            this.mSeamlessRotationCount = 0;
            this.mRotatingSeamlessly = false;
            this.mDisplayContent.finishAsyncRotationIfPossible();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$cancelSeamlessRotation$1(WindowState windowState) {
        if (windowState.mSeamlesslyRotated) {
            windowState.cancelSeamlessRotation();
            windowState.mSeamlesslyRotated = false;
        }
    }

    private void prepareSeamlessRotation() {
        this.mSeamlessRotationCount = 0;
        this.mRotatingSeamlessly = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRotatingSeamlessly() {
        return this.mRotatingSeamlessly;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasSeamlessRotatingWindow() {
        return this.mSeamlessRotationCount > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean shouldRotateSeamlessly(int i, int i2, boolean z) {
        if (this.mDisplayContent.hasTopFixedRotationLaunchingApp()) {
            return true;
        }
        WindowState topFullscreenOpaqueWindow = this.mDisplayPolicy.getTopFullscreenOpaqueWindow();
        if (topFullscreenOpaqueWindow != null) {
            DisplayContent displayContent = this.mDisplayContent;
            if (topFullscreenOpaqueWindow == displayContent.mCurrentFocus) {
                if (displayContent.getWrapper().getExtImpl().isMirageDisplay()) {
                    return true;
                }
                if (topFullscreenOpaqueWindow.getAttrs().rotationAnimation != 3 || topFullscreenOpaqueWindow.inMultiWindowMode() || topFullscreenOpaqueWindow.isAnimatingLw() || (topFullscreenOpaqueWindow.getTask() != null && topFullscreenOpaqueWindow.getTask().getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0]))) {
                    return false;
                }
                if (!canRotateSeamlessly(i, i2)) {
                    return this.mDisplayRotationExt.forceSeamlesslyRotated(topFullscreenOpaqueWindow, "upside down rotation");
                }
                ActivityRecord activityRecord = topFullscreenOpaqueWindow.mActivityRecord;
                if (activityRecord == null || activityRecord.matchParentBounds()) {
                    return (this.mDisplayContent.getDefaultTaskDisplayArea().hasPinnedTask() || this.mDisplayContent.hasAlertWindowSurfaces()) ? this.mDisplayRotationExt.forceSeamlesslyRotated(topFullscreenOpaqueWindow, "PIP or System Alert windows") : z || this.mDisplayContent.getWindow(new Predicate() { // from class: com.android.server.wm.DisplayRotation$$ExternalSyntheticLambda1
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            boolean z2;
                            z2 = ((WindowState) obj).mSeamlesslyRotated;
                            return z2;
                        }
                    }) == null || this.mDisplayRotationExt.forceSeamlesslyRotated(topFullscreenOpaqueWindow, "waiting for last seamless");
                }
                return false;
            }
        }
        if (this.mDisplayRotationExt.forceSeamlesslyRotated(topFullscreenOpaqueWindow, "camera launch from keyguard")) {
            return true;
        }
        if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
            Slog.e(TAG, "not allow seamless rotate, for fullscreen win:" + topFullscreenOpaqueWindow + " not match current focus:" + this.mDisplayContent.mCurrentFocus);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canRotateSeamlessly(int i, int i2) {
        if (this.mAllowSeamlessRotationDespiteNavBarMoving || this.mDisplayPolicy.navigationBarCanMove()) {
            return true;
        }
        return (i == 2 || i2 == 2) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void markForSeamlessRotation(WindowState windowState, boolean z) {
        if (z == windowState.mSeamlesslyRotated || windowState.mForceSeamlesslyRotate) {
            return;
        }
        windowState.mSeamlesslyRotated = z;
        if (z) {
            this.mSeamlessRotationCount++;
        } else {
            this.mSeamlessRotationCount--;
        }
        if (this.mSeamlessRotationCount == 0) {
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ORIENTATION, -576070986, 0, (String) null, (Object[]) null);
            }
            this.mRotatingSeamlessly = false;
            this.mDisplayContent.finishAsyncRotationIfPossible();
            updateRotationAndSendNewConfigIfChanged();
        }
    }

    private RotationAnimationPair selectRotationAnimation() {
        boolean z = !(this.mDisplayPolicy.isScreenOnFully() && this.mService.mPolicy.okToAnimate(false)) && this.mDisplayContent.isDefaultDisplay;
        WindowState topFullscreenOpaqueWindow = this.mDisplayPolicy.getTopFullscreenOpaqueWindow();
        if (ProtoLogCache.WM_DEBUG_ANIM_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_ANIM, 2019765997, 52, (String) null, new Object[]{String.valueOf(topFullscreenOpaqueWindow), Long.valueOf(topFullscreenOpaqueWindow == null ? 0L : topFullscreenOpaqueWindow.getAttrs().rotationAnimation), Boolean.valueOf(z)});
        }
        if (z) {
            RotationAnimationPair rotationAnimationPair = this.mTmpRotationAnim;
            rotationAnimationPair.mExit = R.anim.push_up_in;
            rotationAnimationPair.mEnter = R.anim.push_down_out_no_alpha;
            return rotationAnimationPair;
        }
        if (topFullscreenOpaqueWindow != null) {
            int rotationAnimationHint = topFullscreenOpaqueWindow.getRotationAnimationHint();
            if (rotationAnimationHint < 0 && this.mDisplayPolicy.isTopLayoutFullscreen()) {
                rotationAnimationHint = topFullscreenOpaqueWindow.getAttrs().rotationAnimation;
            }
            if (rotationAnimationHint != 1) {
                if (rotationAnimationHint == 2) {
                    RotationAnimationPair rotationAnimationPair2 = this.mTmpRotationAnim;
                    rotationAnimationPair2.mExit = R.anim.push_up_in;
                    rotationAnimationPair2.mEnter = R.anim.push_down_out_no_alpha;
                } else if (rotationAnimationHint != 3) {
                    RotationAnimationPair rotationAnimationPair3 = this.mTmpRotationAnim;
                    rotationAnimationPair3.mEnter = 0;
                    rotationAnimationPair3.mExit = 0;
                }
            }
            RotationAnimationPair rotationAnimationPair4 = this.mTmpRotationAnim;
            rotationAnimationPair4.mExit = R.anim.push_up_out;
            rotationAnimationPair4.mEnter = R.anim.push_down_out_no_alpha;
        } else {
            RotationAnimationPair rotationAnimationPair5 = this.mTmpRotationAnim;
            rotationAnimationPair5.mEnter = 0;
            rotationAnimationPair5.mExit = 0;
        }
        return this.mTmpRotationAnim;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean validateRotationAnimation(int i, int i2, boolean z) {
        switch (i) {
            case R.anim.push_up_in:
            case R.anim.push_up_out:
                if (z) {
                    return false;
                }
                RotationAnimationPair selectRotationAnimation = selectRotationAnimation();
                return i == selectRotationAnimation.mExit && i2 == selectRotationAnimation.mEnter;
            default:
                return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restoreSettings(int i, int i2, int i3) {
        this.mFixedToUserRotation = i3;
        if (this.isDefaultDisplay || this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent)) {
            if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                Slog.e(TAG, "restoreSettings return DisplayId = " + this.mDisplayContent.getDisplayId());
                return;
            }
            return;
        }
        if (i != 0 && i != 1) {
            Slog.w(TAG, "Trying to restore an invalid user rotation mode " + i + " for " + this.mDisplayContent);
            i = 0;
        }
        if (i2 < 0 || i2 > 3) {
            Slog.w(TAG, "Trying to restore an invalid user rotation " + i2 + " for " + this.mDisplayContent);
            i2 = 0;
        }
        this.mUserRotationMode = i;
        this.mUserRotation = i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFixedToUserRotation(int i) {
        if (this.mFixedToUserRotation == i) {
            return;
        }
        this.mFixedToUserRotation = i;
        this.mDisplayWindowSettings.setFixedToUserRotation(this.mDisplayContent, i);
        DisplayContent displayContent = this.mDisplayContent;
        ActivityRecord activityRecord = displayContent.mFocusedApp;
        if (activityRecord != null) {
            displayContent.onLastFocusedTaskDisplayAreaChanged(activityRecord.getDisplayArea());
        }
        this.mDisplayContent.updateOrientation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void setUserRotation(int i, int i2) {
        boolean z;
        this.mRotationChoiceShownToUserForConfirmation = -1;
        if (useDefaultSettingsProvider()) {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            Settings.System.putIntForUser(contentResolver, "accelerometer_rotation", i != 1 ? 1 : 0, -2);
            Settings.System.putIntForUser(contentResolver, "user_rotation", i2, -2);
            return;
        }
        if (this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent)) {
            Settings.System.putIntForUser(this.mContext.getContentResolver(), "accelerometer_rotation_secondary", i == 1 ? 0 : 1, -2);
        }
        if (this.mUserRotationMode != i) {
            this.mUserRotationMode = i;
            if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                Slog.e(TAG, "setUserRotation DisplayId = " + this.mDisplayContent.getDisplayId() + " mUserRotationMode = " + this.mUserRotationMode);
            }
            z = true;
        } else {
            z = false;
        }
        if (this.mUserRotation != i2) {
            this.mUserRotation = i2;
            z = true;
        }
        this.mDisplayWindowSettings.setUserRotation(this.mDisplayContent, i, i2);
        if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
            Slog.w(TAG, "setUserRotation rotation " + i2 + "; userRotationMode = " + i + ";changed = " + z + " for " + this.mDisplayContent + "; callers:" + Debug.getCallers(10));
        }
        if (z) {
            this.mService.updateRotation(true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void freezeRotation(int i) {
        if (this.mDeviceStateController.shouldReverseRotationDirectionAroundZAxis(this.mDisplayContent)) {
            i = RotationUtils.reverseRotationDirectionAroundZAxis(i);
        }
        if (i == -1) {
            i = this.mRotation;
        }
        setUserRotation(1, this.mDisplayRotationExt.modifyFreezeRotationWhenDeviceFolding(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void thawRotation() {
        setUserRotation(0, this.mUserRotation);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRotationFrozen() {
        return !this.isDefaultDisplay ? this.mUserRotationMode == 1 : Settings.System.getIntForUser(this.mContext.getContentResolver(), "accelerometer_rotation", 0, -2) == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFixedToUserRotation() {
        int i = this.mFixedToUserRotation;
        if (i == 1) {
            return false;
        }
        if (i != 2) {
            return this.mDefaultFixedToUserRotation;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getFixedToUserRotationMode() {
        return this.mFixedToUserRotation;
    }

    public int getLandscapeRotation() {
        return this.mLandscapeRotation;
    }

    public int getSeascapeRotation() {
        return this.mSeascapeRotation;
    }

    public int getPortraitRotation() {
        return this.mPortraitRotation;
    }

    public int getUpsideDownRotation() {
        return this.mUpsideDownRotation;
    }

    public int getCurrentAppOrientation() {
        return this.mCurrentAppOrientation;
    }

    public DisplayPolicy getDisplayPolicy() {
        return this.mDisplayPolicy;
    }

    public WindowOrientationListener getOrientationListener() {
        return this.mOrientationListener;
    }

    public int getUserRotation() {
        return this.mUserRotation;
    }

    public int getUserRotationMode() {
        return this.mUserRotationMode;
    }

    public void updateOrientationListener() {
        synchronized (this.mLock) {
            updateOrientationListenerLw();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pause() {
        int i = this.mDeferredRotationPauseCount + 1;
        this.mDeferredRotationPauseCount = i;
        if (i > 0) {
            Slog.w(TAG, "pause, mDeferredRotationPauseCount = " + this.mDeferredRotationPauseCount, new Throwable());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resume() {
        int i = this.mDeferredRotationPauseCount;
        if (i <= 0) {
            return;
        }
        int i2 = i - 1;
        this.mDeferredRotationPauseCount = i2;
        if (i2 == 0) {
            Slog.w(TAG, "resume, mDeferredRotationPauseCount = " + this.mDeferredRotationPauseCount, new Throwable());
            updateRotationAndSendNewConfigIfChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOrientationListenerLw() {
        OrientationListener orientationListener = this.mOrientationListener;
        if (orientationListener == null || !orientationListener.canDetectOrientation()) {
            return;
        }
        boolean isScreenOnEarly = this.mDisplayPolicy.isScreenOnEarly();
        boolean isAwake = this.mDisplayPolicy.isAwake();
        boolean isKeyguardDrawComplete = this.mDisplayPolicy.isKeyguardDrawComplete();
        boolean isWindowManagerDrawComplete = this.mDisplayPolicy.isWindowManagerDrawComplete();
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -1868124841, 4063, (String) null, new Object[]{Boolean.valueOf(isScreenOnEarly), Boolean.valueOf(isAwake), Long.valueOf(this.mCurrentAppOrientation), Boolean.valueOf(this.mOrientationListener.mEnabled), Boolean.valueOf(isKeyguardDrawComplete), Boolean.valueOf(isWindowManagerDrawComplete)});
        }
        boolean z = true;
        if (isScreenOnEarly && ((this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent) || ((isAwake || this.mOrientationListener.shouldStayEnabledWhileDreaming()) && isKeyguardDrawComplete && isWindowManagerDrawComplete)) && needSensorRunning())) {
            OrientationListener orientationListener2 = this.mOrientationListener;
            if (!orientationListener2.mEnabled) {
                orientationListener2.enable();
                this.mDisplayRotationExt.updateOrientationSensorRunningState(true);
            }
            z = false;
        }
        if (z) {
            this.mOrientationListener.disable();
            this.mDisplayRotationExt.updateOrientationSensorRunningState(false);
        }
    }

    private boolean needSensorRunning() {
        int i;
        if (this.mDisplayRotationExt.shouldFreezeScreenOrientation() || isFixedToUserRotation()) {
            return false;
        }
        FoldController foldController = this.mFoldController;
        if (foldController != null && foldController.shouldDisableRotationSensor()) {
            return false;
        }
        if (this.mSupportAutoRotation && ((i = this.mCurrentAppOrientation) == 4 || i == 10 || i == 7 || i == 6)) {
            return true;
        }
        int dockMode = this.mDisplayPolicy.getDockMode();
        if ((this.mDisplayPolicy.isCarDockEnablesAccelerometer() && dockMode == 2) || (this.mDisplayPolicy.isDeskDockEnablesAccelerometer() && (dockMode == 1 || dockMode == 3 || dockMode == 4))) {
            return true;
        }
        if (this.mUserRotationMode == 1) {
            return this.mSupportAutoRotation && this.mShowRotationSuggestions == 1;
        }
        return this.mSupportAutoRotation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean needsUpdate() {
        int i = this.mRotation;
        return i != rotationForOrientation(this.mLastOrientation, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetAllowAllRotations() {
        this.mAllowAllRotations = -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x02ae, code lost:
    
        if (r17 != 13) goto L150;
     */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int rotationForOrientation(int i, int i2) {
        int i3;
        int i4;
        int suggestRotationForBracketMode;
        int suggestRotationForBracketMode2;
        int shouldSuggestEnterBracketMode;
        if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 202263690, 1092, (String) null, new Object[]{String.valueOf(ActivityInfo.screenOrientationToString(i)), Long.valueOf(i), String.valueOf(Surface.rotationToString(i2)), Long.valueOf(i2), String.valueOf(Surface.rotationToString(this.mUserRotation)), Long.valueOf(this.mUserRotation), this.mUserRotationMode == 1 ? "USER_ROTATION_LOCKED" : ""});
        }
        int fixedRotationForOrientation = this.mDisplayRotationExt.getFixedRotationForOrientation(i, this.mDisplayContent, i2);
        if (fixedRotationForOrientation != -1) {
            if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                Slog.d(TAG, "rotationForOrientation fixedRotation = " + Surface.rotationToString(fixedRotationForOrientation));
            }
            return fixedRotationForOrientation;
        }
        int mirageFixedRotation = this.mDisplayRotationExt.getMirageFixedRotation(this.mDisplayContent.getDisplayId());
        if (mirageFixedRotation != -1) {
            if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                Slog.d(TAG, "rotationForOrientation mirageFixedRotation = " + Surface.rotationToString(fixedRotationForOrientation));
            }
            return mirageFixedRotation;
        }
        if (isFixedToUserRotation()) {
            if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                Slog.d(TAG, "rotationForOrientation: isFixedToUserRotation, mUserRotation = " + this.mUserRotation);
            }
            return this.mUserRotation;
        }
        OrientationListener orientationListener = this.mOrientationListener;
        int proposedRotation = orientationListener != null ? orientationListener.getProposedRotation() : -1;
        FoldController foldController = this.mFoldController;
        if (foldController != null && foldController.shouldIgnoreSensorRotation()) {
            proposedRotation = -1;
        }
        if (this.mDeviceStateController.shouldReverseRotationDirectionAroundZAxis(this.mDisplayContent)) {
            proposedRotation = RotationUtils.reverseRotationDirectionAroundZAxis(proposedRotation);
        }
        if (this.mDisplayRotationExt.shouldKeepSensorRotationInFixRotation(this.mDisplayContent, i, proposedRotation, this.mLastSensorRotation)) {
            proposedRotation = this.mLastSensorRotation;
        }
        this.mLastSensorRotation = proposedRotation;
        int hookUpdateSensorRotation = this.mDisplayRotationExt.hookUpdateSensorRotation(proposedRotation, this.mDisplayContent);
        if (hookUpdateSensorRotation < 0) {
            hookUpdateSensorRotation = i2;
        }
        if (this.mDisplayContent.getWrapper().getExtImpl().isMirageDisplay() && !((IMirageDisplayManagerExt) ExtLoader.type(IMirageDisplayManagerExt.class).create()).isMirageCarMode(this.mDisplayContent.getDisplayId())) {
            hookUpdateSensorRotation = this.mDisplayRotationExt.getMirageDisplaySensorRotation(this.mDisplayContent.getDisplayId());
        }
        if (WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
            Slog.d(TAG, "rotationForOrientation sensorRotation = " + hookUpdateSensorRotation + " displayId = " + this.mDisplayContent.getDisplayId() + "; " + Debug.getCallers(8));
        }
        int lidState = this.mDisplayPolicy.getLidState();
        int dockMode = this.mDisplayPolicy.getDockMode();
        boolean isHdmiPlugged = this.mDisplayPolicy.isHdmiPlugged();
        boolean isCarDockEnablesAccelerometer = this.mDisplayPolicy.isCarDockEnablesAccelerometer();
        boolean isDeskDockEnablesAccelerometer = this.mDisplayPolicy.isDeskDockEnablesAccelerometer();
        if (!this.isDefaultDisplay && !this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent) && !this.mDisplayContent.getWrapper().getExtImpl().isMirageDisplay()) {
            hookUpdateSensorRotation = this.mUserRotation;
        } else if (lidState == 1 && (i4 = this.mLidOpenRotation) >= 0) {
            hookUpdateSensorRotation = i4;
        } else if (dockMode != 2 || (!isCarDockEnablesAccelerometer && this.mCarDockRotation < 0)) {
            if ((dockMode == 1 || dockMode == 3 || dockMode == 4) && !((!isDeskDockEnablesAccelerometer && this.mDeskDockRotation < 0) || i == 14 || i == 5)) {
                if (!isDeskDockEnablesAccelerometer) {
                    hookUpdateSensorRotation = this.mDeskDockRotation;
                }
            } else if ((isHdmiPlugged || this.mDisplayRotationSocExt.hookIsWifiDisplayConnected()) && this.mDemoHdmiRotationLock) {
                hookUpdateSensorRotation = this.mDemoHdmiRotation;
            } else if (this.mDisplayRotationSocExt.hookIsWifiDisplayConnected() && this.mDisplayRotationSocExt.hookGetWifiDisplayRotation() > -1) {
                hookUpdateSensorRotation = this.mDisplayRotationSocExt.hookGetWifiDisplayRotation();
            } else if (isHdmiPlugged && dockMode == 0 && (i3 = this.mUndockedHdmiRotation) >= 0) {
                hookUpdateSensorRotation = i3;
            } else if (this.mDemoRotationLock) {
                hookUpdateSensorRotation = this.mDemoRotation;
            } else if (this.mDisplayPolicy.isPersistentVrModeEnabled()) {
                hookUpdateSensorRotation = this.mPortraitRotation;
            } else {
                if (i != 14) {
                    if (this.mSupportAutoRotation) {
                        if (((this.mUserRotationMode == 0 || isTabletopAutoRotateOverrideEnabled()) && (i == 2 || i == -1 || i == 11 || i == 12 || i == 13)) || i == 4 || i == 10 || i == 6 || i == 7) {
                            if (this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent)) {
                                hookUpdateSensorRotation = this.mDisplayRotationExt.resolvePreferredRotationInSecondary(hookUpdateSensorRotation, i2, i);
                            } else if (hookUpdateSensorRotation == 2) {
                                if (!this.mDisplayRotationExt.isForceAllowAllOrientation(this.mDisplayContent)) {
                                    if (getAllowAllRotations() != this.mDisplayRotationExt.blockAllowAllRotationsInTable(1, this.mDisplayContent)) {
                                        if (i != 10) {
                                        }
                                    }
                                }
                            }
                        } else if (this.mUserRotationMode == 1 && i != 5 && i != 0 && i != 1 && i != 8 && i != 9) {
                            hookUpdateSensorRotation = this.mDisplayRotationExt.hookLockedRotation(this.mUserRotation, this.mDisplayContent);
                        }
                    }
                    hookUpdateSensorRotation = -1;
                }
                hookUpdateSensorRotation = i2;
            }
        } else if (!isCarDockEnablesAccelerometer) {
            hookUpdateSensorRotation = this.mCarDockRotation;
        }
        if (i == 0) {
            return isLandscapeOrSeascape(hookUpdateSensorRotation) ? hookUpdateSensorRotation : this.mLandscapeRotation;
        }
        if (i == 1) {
            return (this.mDisplayContent.getDisplayId() != 0 || (suggestRotationForBracketMode = this.mDisplayRotationExt.getSuggestRotationForBracketMode()) == -1) ? isAnyPortrait(hookUpdateSensorRotation) ? hookUpdateSensorRotation : this.mDisplayRotationExt.hookActivityOrientation(i, this.mPortraitRotation, this.mDisplayContent) : suggestRotationForBracketMode;
        }
        if (i != 11) {
            if (i != 12) {
                switch (i) {
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        int adjustRotationForReverseLandscape = this.mDisplayRotationExt.adjustRotationForReverseLandscape(this.mDisplayContent, this.mSeascapeRotation);
                        return adjustRotationForReverseLandscape != -1 ? adjustRotationForReverseLandscape : isLandscapeOrSeascape(hookUpdateSensorRotation) ? hookUpdateSensorRotation : this.mSeascapeRotation;
                    case 9:
                        if (this.mDisplayContent.getDisplayId() == 0 && (suggestRotationForBracketMode2 = this.mDisplayRotationExt.getSuggestRotationForBracketMode()) != -1) {
                            return suggestRotationForBracketMode2;
                        }
                        DisplayInfo displayInfo = this.mDisplayContent.getDisplayInfo();
                        if (displayInfo == null || displayInfo.displayId == 0 || displayInfo.type != 1) {
                            return isAnyPortrait(hookUpdateSensorRotation) ? hookUpdateSensorRotation : this.mDisplayRotationExt.hookActivityOrientation(i, this.mUpsideDownRotation, this.mDisplayContent);
                        }
                        Slog.d(TAG, "mDisplayContent: " + this.mDisplayContent);
                        return this.mUpsideDownRotation;
                    default:
                        if (this.mDisplayContent.getDisplayId() == 0 && (shouldSuggestEnterBracketMode = this.mDisplayRotationExt.shouldSuggestEnterBracketMode()) != -1) {
                            return shouldSuggestEnterBracketMode;
                        }
                        if (hookUpdateSensorRotation >= 0) {
                            return this.mDisplayRotationExt.forceLauncherRotate(hookUpdateSensorRotation, this.mDisplayContent.getLastOrientationSource());
                        }
                        return 0;
                }
            }
            return isAnyPortrait(hookUpdateSensorRotation) ? hookUpdateSensorRotation : isAnyPortrait(i2) ? i2 : this.mPortraitRotation;
        }
        return isLandscapeOrSeascape(hookUpdateSensorRotation) ? hookUpdateSensorRotation : isLandscapeOrSeascape(i2) ? i2 : this.mLandscapeRotation;
    }

    private int getAllowAllRotations() {
        if (this.mAllowAllRotations == -1) {
            this.mAllowAllRotations = this.mContext.getResources().getBoolean(R.bool.config_allowTheaterModeWakeFromDock) ? 1 : 0;
        }
        return this.mAllowAllRotations;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLandscapeOrSeascape(int i) {
        return i == this.mLandscapeRotation || i == this.mSeascapeRotation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAnyPortrait(int i) {
        return i == this.mPortraitRotation || i == this.mUpsideDownRotation;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValidRotationChoice(int i) {
        int i2 = this.mCurrentAppOrientation;
        if (i2 != -1 && i2 != 2) {
            switch (i2) {
                case 11:
                    return isLandscapeOrSeascape(i);
                case 12:
                    return i == this.mPortraitRotation;
                case 13:
                    return i >= 0;
                default:
                    return false;
            }
        }
        if (getAllowAllRotations() == 1) {
            return i >= 0;
        }
        return this.mDisplayRotationExt.isValidRotationChoice(i, this.mUpsideDownRotation);
    }

    private boolean isTabletopAutoRotateOverrideEnabled() {
        FoldController foldController = this.mFoldController;
        return foldController != null && foldController.overrideFrozenRotation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRotationChoiceAllowed(int i) {
        int dockMode;
        DisplayRotationImmersiveAppCompatPolicy displayRotationImmersiveAppCompatPolicy = this.mCompatPolicyForImmersiveApps;
        if (!(displayRotationImmersiveAppCompatPolicy != null && displayRotationImmersiveAppCompatPolicy.isRotationLockEnforced(i)) && this.mUserRotationMode != 1) {
            return false;
        }
        FoldController foldController = this.mFoldController;
        if ((foldController != null && foldController.shouldIgnoreSensorRotation()) || isTabletopAutoRotateOverrideEnabled() || isFixedToUserRotation()) {
            return false;
        }
        if ((this.mDisplayPolicy.getLidState() == 1 && this.mLidOpenRotation >= 0) || (dockMode = this.mDisplayPolicy.getDockMode()) == 2) {
            return false;
        }
        boolean isDeskDockEnablesAccelerometer = this.mDisplayPolicy.isDeskDockEnablesAccelerometer();
        if ((dockMode == 1 || dockMode == 3 || dockMode == 4) && !isDeskDockEnablesAccelerometer) {
            return false;
        }
        boolean isHdmiPlugged = this.mDisplayPolicy.isHdmiPlugged();
        if (isHdmiPlugged && this.mDemoHdmiRotationLock) {
            return false;
        }
        if ((isHdmiPlugged && dockMode == 0 && this.mUndockedHdmiRotation >= 0) || this.mDemoRotationLock || this.mDisplayPolicy.isPersistentVrModeEnabled() || !this.mSupportAutoRotation) {
            return false;
        }
        int i2 = this.mCurrentAppOrientation;
        if (i2 != -1 && i2 != 2) {
            switch (i2) {
                case 11:
                case 12:
                case 13:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendProposedRotationChangeToStatusBarInternal(int i, boolean z) {
        if (this.mStatusBarManagerInternal == null) {
            this.mStatusBarManagerInternal = (StatusBarManagerInternal) LocalServices.getService(StatusBarManagerInternal.class);
        }
        StatusBarManagerInternal statusBarManagerInternal = this.mStatusBarManagerInternal;
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.onProposedRotationChanged(i, z);
        }
    }

    void dispatchProposedRotation(int i) {
        if (this.mService.mRotationWatcherController.hasProposedRotationListeners()) {
            synchronized (this.mLock) {
                this.mService.mRotationWatcherController.dispatchProposedRotation(this.mDisplayContent, i);
            }
        }
    }

    private static String allowAllRotationsToString(int i) {
        return i != -1 ? i != 0 ? i != 1 ? Integer.toString(i) : "true" : "false" : "unknown";
    }

    public void onUserSwitch() {
        SettingsObserver settingsObserver = this.mSettingsObserver;
        if (settingsObserver != null) {
            settingsObserver.onChange(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDisplayRemoved() {
        removeDefaultDisplayRotationChangedCallback();
        FoldController foldController = this.mFoldController;
        if (foldController != null) {
            foldController.onDisplayRemoved();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x004b, code lost:
    
        if (android.provider.Settings.System.getIntForUser(r0, "accelerometer_rotation", 0, -2) != 0) goto L23;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean updateSettings() {
        boolean z;
        boolean z2;
        boolean z3;
        int i;
        ContentResolver contentResolver = this.mContext.getContentResolver();
        synchronized (this.mLock) {
            z = true;
            int intForUser = ActivityManager.isLowRamDeviceStatic() ? 0 : Settings.Secure.getIntForUser(contentResolver, "show_rotation_suggestions", 1, -2);
            if (this.mShowRotationSuggestions != intForUser) {
                this.mShowRotationSuggestions = intForUser;
                z2 = true;
            } else {
                z2 = false;
            }
            int intForUser2 = Settings.System.getIntForUser(contentResolver, "user_rotation", 0, -2);
            if (this.mUserRotation != intForUser2) {
                this.mUserRotation = intForUser2;
                z3 = true;
            } else {
                z3 = false;
            }
            if (this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent)) {
                if (Settings.System.getIntForUser(contentResolver, "accelerometer_rotation_secondary", 0, -2) != 0) {
                    i = 0;
                }
                i = 1;
            }
            if (this.mUserRotationMode != i) {
                this.mUserRotationMode = i;
                z2 = true;
                z3 = true;
            }
            if (z2) {
                updateOrientationListenerLw();
            }
            int intForUser3 = Settings.Secure.getIntForUser(contentResolver, "camera_autorotate", 0, -2);
            if (this.mCameraRotationMode != intForUser3) {
                this.mCameraRotationMode = intForUser3;
            } else {
                z = z3;
            }
        }
        return z;
    }

    void removeDefaultDisplayRotationChangedCallback() {
        if (DisplayRotationCoordinator.isSecondaryInternalDisplay(this.mDisplayContent)) {
            this.mDisplayRotationCoordinator.removeDefaultDisplayRotationChangedCallback();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSetRequestedOrientation() {
        int i;
        if (this.mCompatPolicyForImmersiveApps == null || (i = this.mRotationChoiceShownToUserForConfirmation) == -1) {
            return;
        }
        this.mOrientationListener.onProposedRotationChanged(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(String str, PrintWriter printWriter) {
        printWriter.println(str + "DisplayRotation");
        printWriter.println(str + "  mCurrentAppOrientation=" + ActivityInfo.screenOrientationToString(this.mCurrentAppOrientation));
        printWriter.println(str + "  mLastOrientation=" + this.mLastOrientation);
        printWriter.print(str + "  mRotation=" + this.mRotation);
        StringBuilder sb = new StringBuilder();
        sb.append(" mDeferredRotationPauseCount=");
        sb.append(this.mDeferredRotationPauseCount);
        printWriter.println(sb.toString());
        printWriter.print(str + "  mLandscapeRotation=" + Surface.rotationToString(this.mLandscapeRotation));
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" mSeascapeRotation=");
        sb2.append(Surface.rotationToString(this.mSeascapeRotation));
        printWriter.println(sb2.toString());
        printWriter.print(str + "  mPortraitRotation=" + Surface.rotationToString(this.mPortraitRotation));
        StringBuilder sb3 = new StringBuilder();
        sb3.append(" mUpsideDownRotation=");
        sb3.append(Surface.rotationToString(this.mUpsideDownRotation));
        printWriter.println(sb3.toString());
        printWriter.println(str + "  mSupportAutoRotation=" + this.mSupportAutoRotation);
        printWriter.println(str + "  mSeamlessRotationCount=" + this.mSeamlessRotationCount);
        OrientationListener orientationListener = this.mOrientationListener;
        if (orientationListener != null) {
            orientationListener.dump(printWriter, str + "  ");
        }
        printWriter.println();
        printWriter.print(str + "  mCarDockRotation=" + Surface.rotationToString(this.mCarDockRotation));
        StringBuilder sb4 = new StringBuilder();
        sb4.append(" mDeskDockRotation=");
        sb4.append(Surface.rotationToString(this.mDeskDockRotation));
        printWriter.println(sb4.toString());
        printWriter.print(str + "  mUserRotationMode=" + WindowManagerPolicy.userRotationModeToString(this.mUserRotationMode));
        StringBuilder sb5 = new StringBuilder();
        sb5.append(" mUserRotation=");
        sb5.append(Surface.rotationToString(this.mUserRotation));
        printWriter.print(sb5.toString());
        printWriter.print(" mCameraRotationMode=" + this.mCameraRotationMode);
        printWriter.println(" mAllowAllRotations=" + allowAllRotationsToString(this.mAllowAllRotations));
        printWriter.print(str + "  mDemoHdmiRotation=" + Surface.rotationToString(this.mDemoHdmiRotation));
        StringBuilder sb6 = new StringBuilder();
        sb6.append(" mDemoHdmiRotationLock=");
        sb6.append(this.mDemoHdmiRotationLock);
        printWriter.print(sb6.toString());
        printWriter.println(" mUndockedHdmiRotation=" + Surface.rotationToString(this.mUndockedHdmiRotation));
        printWriter.println(str + "  mLidOpenRotation=" + Surface.rotationToString(this.mLidOpenRotation));
        printWriter.println(str + "  mFixedToUserRotation=" + isFixedToUserRotation());
        if (this.mFoldController != null) {
            printWriter.println(str + "FoldController");
            printWriter.println(str + "  mPauseAutorotationDuringUnfolding=" + this.mFoldController.mPauseAutorotationDuringUnfolding);
            printWriter.println(str + "  mShouldDisableRotationSensor=" + this.mFoldController.mShouldDisableRotationSensor);
            printWriter.println(str + "  mShouldIgnoreSensorRotation=" + this.mFoldController.mShouldIgnoreSensorRotation);
            printWriter.println(str + "  mLastDisplaySwitchTime=" + this.mFoldController.mLastDisplaySwitchTime);
            printWriter.println(str + "  mLastHingeAngleEventTime=" + this.mFoldController.mLastHingeAngleEventTime);
            printWriter.println(str + "  mDeviceState=" + this.mFoldController.mDeviceState);
        }
        if (this.mRotationHistory.mRecords.isEmpty()) {
            return;
        }
        printWriter.println();
        printWriter.println(str + "  RotationHistory");
        String str2 = "    " + str;
        Iterator<RotationHistory.Record> it = this.mRotationHistory.mRecords.iterator();
        while (it.hasNext()) {
            it.next().dump(str2, printWriter);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1120986464257L, getRotation());
        protoOutputStream.write(1133871366146L, isRotationFrozen());
        protoOutputStream.write(1120986464259L, getUserRotation());
        protoOutputStream.write(1120986464260L, this.mFixedToUserRotation);
        protoOutputStream.write(1120986464261L, this.mLastOrientation);
        protoOutputStream.write(1133871366150L, isFixedToUserRotation());
        protoOutputStream.end(start);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDeviceInPosture(DeviceStateController.DeviceState deviceState, boolean z) {
        FoldController foldController = this.mFoldController;
        if (foldController == null) {
            return false;
        }
        return foldController.isDeviceInPosture(deviceState, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDisplaySeparatingHinge() {
        FoldController foldController = this.mFoldController;
        return foldController != null && foldController.isSeparatingHinge();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void foldStateChanged(DeviceStateController.DeviceState deviceState) {
        if (this.mFoldController != null) {
            synchronized (this.mLock) {
                this.mFoldController.foldStateChanged(deviceState);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void physicalDisplayChanged() {
        FoldController foldController = this.mFoldController;
        if (foldController != null) {
            foldController.onPhysicalDisplayChanged();
        }
    }

    @VisibleForTesting
    long uptimeMillis() {
        return SystemClock.uptimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class FoldController {
        private final Runnable mActivityBoundsUpdateCallback;
        private int mDisplaySwitchRotationBlockTimeMs;
        private int mHingeAngleRotationBlockTimeMs;
        private SensorEventListener mHingeAngleSensorEventListener;
        private final boolean mIsDisplayAlwaysSeparatingHinge;
        private int mMaxHingeAngle;
        private final boolean mPauseAutorotationDuringUnfolding;
        private SensorManager mSensorManager;
        private boolean mShouldDisableRotationSensor;
        private boolean mShouldIgnoreSensorRotation;
        private int mHalfFoldSavedRotation = -1;
        private DeviceStateController.DeviceState mDeviceState = DeviceStateController.DeviceState.UNKNOWN;
        private long mLastHingeAngleEventTime = 0;
        private long mLastDisplaySwitchTime = 0;
        private boolean mInHalfFoldTransition = false;
        private final Set<Integer> mTabletopRotations = new ArraySet();

        FoldController() {
            int[] intArray = DisplayRotation.this.mContext.getResources().getIntArray(R.array.config_ephemeralResolverPackage);
            if (intArray != null) {
                for (int i : intArray) {
                    if (i == 0) {
                        this.mTabletopRotations.add(0);
                    } else if (i == 90) {
                        this.mTabletopRotations.add(1);
                    } else if (i == 180) {
                        this.mTabletopRotations.add(2);
                    } else if (i == 270) {
                        this.mTabletopRotations.add(3);
                    } else if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                        ProtoLogImpl.e(ProtoLogGroup.WM_DEBUG_ORIENTATION, -637815408, 1, (String) null, new Object[]{Long.valueOf(i)});
                    }
                }
            } else if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_ORIENTATION, 939638078, 0, (String) null, (Object[]) null);
            }
            this.mIsDisplayAlwaysSeparatingHinge = DisplayRotation.this.mContext.getResources().getBoolean(17891722);
            this.mActivityBoundsUpdateCallback = new AnonymousClass1(DisplayRotation.this);
            boolean z = DisplayRotation.this.mContext.getResources().getBoolean(17891915);
            this.mPauseAutorotationDuringUnfolding = z;
            if (z) {
                this.mDisplaySwitchRotationBlockTimeMs = DisplayRotation.this.mContext.getResources().getInteger(R.integer.dock_enter_exit_duration);
                this.mHingeAngleRotationBlockTimeMs = DisplayRotation.this.mContext.getResources().getInteger(R.integer.kg_carousel_angle);
                this.mMaxHingeAngle = DisplayRotation.this.mContext.getResources().getInteger(R.integer.kg_glowpad_rotation_offset);
                registerSensorManager();
            }
        }

        /* renamed from: com.android.server.wm.DisplayRotation$FoldController$1, reason: invalid class name */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ DisplayRotation val$this$0;

            AnonymousClass1(DisplayRotation displayRotation) {
                this.val$this$0 = displayRotation;
            }

            @Override // java.lang.Runnable
            public void run() {
                ActivityRecord activityRecord;
                if (FoldController.this.mDeviceState == DeviceStateController.DeviceState.OPEN || FoldController.this.mDeviceState == DeviceStateController.DeviceState.HALF_FOLDED) {
                    synchronized (DisplayRotation.this.mLock) {
                        Task task = DisplayRotation.this.mDisplayContent.getTask(new Predicate() { // from class: com.android.server.wm.DisplayRotation$FoldController$1$$ExternalSyntheticLambda0
                            @Override // java.util.function.Predicate
                            public final boolean test(Object obj) {
                                boolean lambda$run$0;
                                lambda$run$0 = DisplayRotation.FoldController.AnonymousClass1.lambda$run$0((Task) obj);
                                return lambda$run$0;
                            }
                        });
                        if (task != null && (activityRecord = task.topRunningActivity()) != null) {
                            activityRecord.recomputeConfiguration();
                        }
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static /* synthetic */ boolean lambda$run$0(Task task) {
                return task.getWindowingMode() == 1;
            }
        }

        private void registerSensorManager() {
            Sensor defaultSensor;
            SensorManager sensorManager = (SensorManager) DisplayRotation.this.mContext.getSystemService(SensorManager.class);
            this.mSensorManager = sensorManager;
            if (sensorManager == null || (defaultSensor = sensorManager.getDefaultSensor(36)) == null) {
                return;
            }
            SensorEventListener sensorEventListener = new SensorEventListener() { // from class: com.android.server.wm.DisplayRotation.FoldController.2
                @Override // android.hardware.SensorEventListener
                public void onAccuracyChanged(Sensor sensor, int i) {
                }

                @Override // android.hardware.SensorEventListener
                public void onSensorChanged(SensorEvent sensorEvent) {
                    FoldController.this.onHingeAngleChanged(sensorEvent.values[0]);
                }
            };
            this.mHingeAngleSensorEventListener = sensorEventListener;
            this.mSensorManager.registerListener(sensorEventListener, defaultSensor, 0, DisplayRotation.this.getHandler());
        }

        void onDisplayRemoved() {
            SensorEventListener sensorEventListener;
            SensorManager sensorManager = this.mSensorManager;
            if (sensorManager == null || (sensorEventListener = this.mHingeAngleSensorEventListener) == null) {
                return;
            }
            sensorManager.unregisterListener(sensorEventListener);
        }

        boolean isDeviceInPosture(DeviceStateController.DeviceState deviceState, boolean z) {
            DeviceStateController.DeviceState deviceState2 = this.mDeviceState;
            if (deviceState != deviceState2) {
                return false;
            }
            return deviceState2 != DeviceStateController.DeviceState.HALF_FOLDED || z == this.mTabletopRotations.contains(Integer.valueOf(DisplayRotation.this.mRotation));
        }

        DeviceStateController.DeviceState getFoldState() {
            return this.mDeviceState;
        }

        boolean isSeparatingHinge() {
            DeviceStateController.DeviceState deviceState = this.mDeviceState;
            return deviceState == DeviceStateController.DeviceState.HALF_FOLDED || (deviceState == DeviceStateController.DeviceState.OPEN && this.mIsDisplayAlwaysSeparatingHinge);
        }

        boolean overrideFrozenRotation() {
            return this.mDeviceState == DeviceStateController.DeviceState.HALF_FOLDED;
        }

        boolean shouldRevertOverriddenRotation() {
            return this.mDeviceState == DeviceStateController.DeviceState.OPEN && !this.mShouldIgnoreSensorRotation && this.mInHalfFoldTransition && DisplayRotation.this.mDisplayContent.getRotationReversionController().isOverrideActive(2) && DisplayRotation.this.mUserRotationMode == 1;
        }

        int revertOverriddenRotation() {
            int i = this.mHalfFoldSavedRotation;
            this.mHalfFoldSavedRotation = -1;
            DisplayRotation.this.mDisplayContent.getRotationReversionController().revertOverride(2);
            this.mInHalfFoldTransition = false;
            return i;
        }

        void foldStateChanged(DeviceStateController.DeviceState deviceState) {
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                long displayId = DisplayRotation.this.mDisplayContent.getDisplayId();
                String valueOf = String.valueOf(deviceState.name());
                long j = this.mHalfFoldSavedRotation;
                long j2 = DisplayRotation.this.mUserRotation;
                DisplayRotation displayRotation = DisplayRotation.this;
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 2066210760, 5457, (String) null, new Object[]{Long.valueOf(displayId), valueOf, Long.valueOf(j), Long.valueOf(j2), Long.valueOf(displayRotation.mLastSensorRotation), Long.valueOf(displayRotation.mLastOrientation), Long.valueOf(DisplayRotation.this.mRotation)});
            }
            DeviceStateController.DeviceState deviceState2 = this.mDeviceState;
            if (deviceState2 == DeviceStateController.DeviceState.UNKNOWN) {
                this.mDeviceState = deviceState;
                return;
            }
            DeviceStateController.DeviceState deviceState3 = DeviceStateController.DeviceState.HALF_FOLDED;
            if (deviceState == deviceState3 && deviceState2 != deviceState3) {
                DisplayRotation.this.mDisplayContent.getRotationReversionController().beforeOverrideApplied(2);
                this.mHalfFoldSavedRotation = DisplayRotation.this.mRotation;
                this.mDeviceState = deviceState;
                DisplayRotation.this.mService.updateRotation(false, false);
            } else {
                this.mInHalfFoldTransition = true;
                this.mDeviceState = deviceState;
                DisplayRotation.this.mService.updateRotation(false, false);
            }
            UiThread.getHandler().removeCallbacks(this.mActivityBoundsUpdateCallback);
            UiThread.getHandler().postDelayed(this.mActivityBoundsUpdateCallback, 800L);
        }

        boolean shouldIgnoreSensorRotation() {
            return this.mShouldIgnoreSensorRotation;
        }

        boolean shouldDisableRotationSensor() {
            return this.mShouldDisableRotationSensor;
        }

        private void updateSensorRotationBlockIfNeeded() {
            long uptimeMillis = DisplayRotation.this.uptimeMillis();
            boolean z = uptimeMillis - this.mLastDisplaySwitchTime < ((long) this.mDisplaySwitchRotationBlockTimeMs) || uptimeMillis - this.mLastHingeAngleEventTime < ((long) this.mHingeAngleRotationBlockTimeMs);
            if (z != this.mShouldIgnoreSensorRotation) {
                this.mShouldIgnoreSensorRotation = z;
                if (z) {
                    return;
                }
                if (this.mShouldDisableRotationSensor) {
                    this.mShouldDisableRotationSensor = false;
                    DisplayRotation.this.updateOrientationListenerLw();
                } else {
                    DisplayRotation.this.updateRotationAndSendNewConfigIfChanged();
                }
            }
        }

        void onPhysicalDisplayChanged() {
            if (this.mPauseAutorotationDuringUnfolding) {
                this.mLastDisplaySwitchTime = DisplayRotation.this.uptimeMillis();
                DeviceStateController.DeviceState deviceState = this.mDeviceState;
                if (deviceState == DeviceStateController.DeviceState.OPEN || deviceState == DeviceStateController.DeviceState.HALF_FOLDED) {
                    this.mShouldDisableRotationSensor = true;
                    DisplayRotation.this.updateOrientationListenerLw();
                }
                updateSensorRotationBlockIfNeeded();
                DisplayRotation.this.getHandler().postDelayed(new Runnable() { // from class: com.android.server.wm.DisplayRotation$FoldController$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        DisplayRotation.FoldController.this.lambda$onPhysicalDisplayChanged$0();
                    }
                }, this.mDisplaySwitchRotationBlockTimeMs);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPhysicalDisplayChanged$0() {
            synchronized (DisplayRotation.this.mLock) {
                updateSensorRotationBlockIfNeeded();
            }
        }

        void onHingeAngleChanged(float f) {
            if (f < this.mMaxHingeAngle) {
                this.mLastHingeAngleEventTime = DisplayRotation.this.uptimeMillis();
                updateSensorRotationBlockIfNeeded();
                DisplayRotation.this.getHandler().postDelayed(new Runnable() { // from class: com.android.server.wm.DisplayRotation$FoldController$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        DisplayRotation.FoldController.this.lambda$onHingeAngleChanged$1();
                    }
                }, this.mHingeAngleRotationBlockTimeMs);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onHingeAngleChanged$1() {
            synchronized (DisplayRotation.this.mLock) {
                updateSensorRotationBlockIfNeeded();
            }
        }
    }

    @VisibleForTesting
    Handler getHandler() {
        return this.mService.mH;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class OrientationListener extends WindowOrientationListener implements Runnable {
        transient boolean mEnabled;

        OrientationListener(Context context, Handler handler, int i) {
            super(context, handler, i);
        }

        @Override // com.android.server.wm.WindowOrientationListener
        public boolean isKeyguardShowingAndNotOccluded() {
            return DisplayRotation.this.mService.isKeyguardShowingAndNotOccluded();
        }

        @Override // com.android.server.wm.WindowOrientationListener
        public boolean isRotationResolverEnabled() {
            return DisplayRotation.this.mAllowRotationResolver && DisplayRotation.this.mUserRotationMode == 0 && DisplayRotation.this.mCameraRotationMode == 1 && !DisplayRotation.this.mService.mPowerManager.isPowerSaveMode();
        }

        @Override // com.android.server.wm.WindowOrientationListener
        public void onProposedRotationChanged(int i) {
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, 2128917433, 1, (String) null, new Object[]{Long.valueOf(i)});
            }
            DisplayRotation.this.mDisplayRotationExt.onProposedRotationChanged(i, DisplayRotation.this.mUserRotationMode, DisplayRotation.this.mDisplayContent);
            if (this.mEnabled && DisplayRotation.this.mRotation != i && DisplayRotation.this.mUserRotationMode != 1) {
                DisplayRotation.this.mDisplayRotationExt.setSensorRotationChanged(DisplayRotation.this.mDisplayContent, true);
            }
            DisplayRotation.this.mService.mPowerManagerInternal.setPowerBoost(0, 0);
            DisplayRotation.this.dispatchProposedRotation(i);
            if (DisplayRotation.this.isRotationChoiceAllowed(i)) {
                if (DisplayRotation.this.mDisplayRotationExt.skipSendProposedRotationChangeToStatusBar(DisplayRotation.this.mCurrentAppOrientation, DisplayRotation.this.mDisplayContent)) {
                    return;
                }
                DisplayRotation.this.mRotationChoiceShownToUserForConfirmation = i;
                DisplayRotation.this.sendProposedRotationChangeToStatusBarInternal(i, DisplayRotation.this.isValidRotationChoice(i));
                return;
            }
            DisplayRotation.this.mRotationChoiceShownToUserForConfirmation = -1;
            DisplayRotation.this.mService.updateRotation(false, false);
            if (DisplayRotation.this.mService.mPolicy instanceof PhoneWindowManager) {
                DisplayRotation.this.mService.mPolicy.getWrapper().getExtImpl().sendWindowDrawCompleteMsg(DisplayRotation.this.mDisplayContent.getDisplayId());
            }
        }

        @Override // com.android.server.wm.WindowOrientationListener
        public void enable() {
            this.mEnabled = true;
            getHandler().post(this);
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -1568331821, 0, (String) null, (Object[]) null);
            }
        }

        @Override // com.android.server.wm.WindowOrientationListener
        public void disable() {
            this.mEnabled = false;
            getHandler().post(this);
            if (ProtoLogCache.WM_DEBUG_ORIENTATION_enabled) {
                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ORIENTATION, -439951996, 0, (String) null, (Object[]) null);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mEnabled) {
                super.enable();
            } else {
                super.disable();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            ContentResolver contentResolver = DisplayRotation.this.mContext.getContentResolver();
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("show_rotation_suggestions"), false, this, -1);
            contentResolver.registerContentObserver(Settings.System.getUriFor("accelerometer_rotation"), false, this, -1);
            contentResolver.registerContentObserver(Settings.System.getUriFor("user_rotation"), false, this, -1);
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("camera_autorotate"), false, this, -1);
            if (DisplayRotation.this.mDisplayRotationExt.isSecondDisplay(DisplayRotation.this.mDisplayContent)) {
                contentResolver.registerContentObserver(Settings.System.getUriFor("accelerometer_rotation_secondary"), false, this, -1);
            }
            DisplayRotation.this.updateSettings();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            if (DisplayRotation.this.updateSettings()) {
                DisplayRotation.this.mDisplayRotationExt.forceUpdateRotationForCanvas(true);
                DisplayRotation.this.mService.updateRotation(true, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class RotationHistory {
        private static final int MAX_SIZE = 8;
        private static final int NO_FOLD_CONTROLLER = -2;
        final ArrayDeque<Record> mRecords;

        private RotationHistory() {
            this.mRecords = new ArrayDeque<>(8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        public static class Record {
            final DeviceStateController.DeviceState mDeviceState;
            final String mDisplayRotationCompatPolicySummary;
            final int mFromRotation;
            final int mHalfFoldSavedRotation;
            final boolean mIgnoreOrientationRequest;
            final boolean mInHalfFoldTransition;
            final String mLastOrientationSource;
            final String mNonDefaultRequestingTaskDisplayArea;
            final boolean[] mRotationReversionSlots;
            final int mSensorRotation;
            final int mSourceOrientation;
            final long mTimestamp = System.currentTimeMillis();
            final int mToRotation;
            final int mUserRotation;
            final int mUserRotationMode;

            Record(DisplayRotation displayRotation, int i, int i2) {
                String displayArea;
                int overrideOrientation;
                this.mFromRotation = i;
                this.mToRotation = i2;
                this.mUserRotation = displayRotation.mUserRotation;
                this.mUserRotationMode = displayRotation.mUserRotationMode;
                OrientationListener orientationListener = displayRotation.mOrientationListener;
                this.mSensorRotation = (orientationListener == null || !orientationListener.mEnabled) ? -2 : displayRotation.mLastSensorRotation;
                DisplayContent displayContent = displayRotation.mDisplayContent;
                this.mIgnoreOrientationRequest = displayContent.getIgnoreOrientationRequest();
                TaskDisplayArea orientationRequestingTaskDisplayArea = displayContent.getOrientationRequestingTaskDisplayArea();
                if (orientationRequestingTaskDisplayArea == null) {
                    displayArea = "none";
                } else {
                    displayArea = orientationRequestingTaskDisplayArea != displayContent.getDefaultTaskDisplayArea() ? orientationRequestingTaskDisplayArea.toString() : null;
                }
                this.mNonDefaultRequestingTaskDisplayArea = displayArea;
                WindowContainer lastOrientationSource = displayContent.getLastOrientationSource();
                if (lastOrientationSource != null) {
                    this.mLastOrientationSource = lastOrientationSource.toString();
                    WindowState asWindowState = lastOrientationSource.asWindowState();
                    if (asWindowState != null) {
                        overrideOrientation = asWindowState.mAttrs.screenOrientation;
                    } else {
                        overrideOrientation = lastOrientationSource.getOverrideOrientation();
                    }
                    this.mSourceOrientation = overrideOrientation;
                } else {
                    this.mLastOrientationSource = null;
                    this.mSourceOrientation = -2;
                }
                FoldController foldController = displayRotation.mFoldController;
                if (foldController != null) {
                    this.mHalfFoldSavedRotation = foldController.mHalfFoldSavedRotation;
                    this.mInHalfFoldTransition = displayRotation.mFoldController.mInHalfFoldTransition;
                    this.mDeviceState = displayRotation.mFoldController.mDeviceState;
                } else {
                    this.mHalfFoldSavedRotation = -2;
                    this.mInHalfFoldTransition = false;
                    this.mDeviceState = DeviceStateController.DeviceState.UNKNOWN;
                }
                DisplayRotationCompatPolicy displayRotationCompatPolicy = displayContent.mDisplayRotationCompatPolicy;
                this.mDisplayRotationCompatPolicySummary = displayRotationCompatPolicy != null ? displayRotationCompatPolicy.getSummaryForDisplayRotationHistoryRecord() : null;
                this.mRotationReversionSlots = displayRotation.mDisplayContent.getRotationReversionController().getSlotsCopy();
            }

            void dump(String str, PrintWriter printWriter) {
                printWriter.println(str + TimeUtils.logTimeOfDay(this.mTimestamp) + " " + Surface.rotationToString(this.mFromRotation) + " to " + Surface.rotationToString(this.mToRotation));
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append("  source=");
                sb.append(this.mLastOrientationSource);
                sb.append(" ");
                sb.append(ActivityInfo.screenOrientationToString(this.mSourceOrientation));
                printWriter.println(sb.toString());
                printWriter.println(str + "  mode=" + WindowManagerPolicy.userRotationModeToString(this.mUserRotationMode) + " user=" + Surface.rotationToString(this.mUserRotation) + " sensor=" + Surface.rotationToString(this.mSensorRotation));
                if (this.mIgnoreOrientationRequest) {
                    printWriter.println(str + "  ignoreRequest=true");
                }
                if (this.mNonDefaultRequestingTaskDisplayArea != null) {
                    printWriter.println(str + "  requestingTda=" + this.mNonDefaultRequestingTaskDisplayArea);
                }
                if (this.mHalfFoldSavedRotation != -2) {
                    printWriter.println(str + " halfFoldSavedRotation=" + this.mHalfFoldSavedRotation + " mInHalfFoldTransition=" + this.mInHalfFoldTransition + " mFoldState=" + this.mDeviceState);
                }
                if (this.mDisplayRotationCompatPolicySummary != null) {
                    printWriter.println(str + this.mDisplayRotationCompatPolicySummary);
                }
                if (this.mRotationReversionSlots != null) {
                    printWriter.println(str + " reversionSlots= NOSENSOR " + this.mRotationReversionSlots[0] + ", CAMERA " + this.mRotationReversionSlots[1] + " HALF_FOLD " + this.mRotationReversionSlots[2]);
                }
            }
        }

        void addRecord(DisplayRotation displayRotation, int i) {
            if (this.mRecords.size() >= 8) {
                this.mRecords.removeFirst();
            }
            this.mRecords.addLast(new Record(displayRotation, displayRotation.mDisplayContent.getWindowConfiguration().getRotation(), i));
        }
    }
}
