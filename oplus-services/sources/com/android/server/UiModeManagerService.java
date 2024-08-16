package com.android.server;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AlarmManager;
import android.app.IApplicationThread;
import android.app.IOnProjectionStateChangedListener;
import android.app.IUiModeManager;
import android.app.IUiModeManagerCallback;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.StatusBarManager;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManagerInternal;
import android.os.PowerSaveState;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ShellCallback;
import android.os.ShellCommand;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.dreams.Sandman;
import android.service.vr.IVrManager;
import android.service.vr.IVrStateCallbacks;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.DisableCarModeActivity;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.server.SystemService;
import com.android.server.UiModeManagerService;
import com.android.server.am.HostingRecord;
import com.android.server.twilight.TwilightListener;
import com.android.server.twilight.TwilightManager;
import com.android.server.twilight.TwilightState;
import com.android.server.wm.ActivityTaskManagerInternal;
import com.android.server.wm.WindowManagerInternal;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class UiModeManagerService extends SystemService {
    private static final boolean ENABLE_LAUNCH_DESK_DOCK_APP = true;
    private static final boolean LOG;

    @VisibleForTesting
    public static final Set<Integer> SUPPORTED_NIGHT_MODE_CUSTOM_TYPES;
    private static final String SYSTEM_PROPERTY_DEVICE_THEME = "persist.sys.theme";
    private static final String TAG = UiModeManager.class.getSimpleName();
    private final LocalTime DEFAULT_CUSTOM_NIGHT_END_TIME;
    private final LocalTime DEFAULT_CUSTOM_NIGHT_START_TIME;
    private ActivityTaskManagerInternal mActivityTaskManager;
    private AlarmManager mAlarmManager;
    private final BroadcastReceiver mBatteryReceiver;
    private boolean mCar;
    private int mCarModeEnableFlags;
    private boolean mCarModeEnabled;
    private boolean mCarModeKeepsScreenOn;
    private Map<Integer, String> mCarModePackagePriority;
    private boolean mCharging;
    private boolean mComputedNightMode;
    private Configuration mConfiguration;
    private final ContentObserver mContrastObserver;

    @GuardedBy({"mLock"})
    private final SparseArray<Float> mContrasts;
    int mCurUiMode;
    private int mCurrentUser;
    private LocalTime mCustomAutoNightModeEndMilliseconds;
    private LocalTime mCustomAutoNightModeStartMilliseconds;
    private final AlarmManager.OnAlarmListener mCustomTimeListener;
    private final ContentObserver mDarkThemeObserver;
    private int mDefaultUiModeType;
    private boolean mDeskModeKeepsScreenOn;
    private final BroadcastReceiver mDockModeReceiver;
    private int mDockState;
    private boolean mDreamsDisabledByAmbientModeSuppression;
    private boolean mEnableCarDockLaunch;
    private final Handler mHandler;
    private boolean mHoldingConfiguration;
    private final Injector mInjector;
    private KeyguardManager mKeyguardManager;
    private boolean mLastBedtimeRequestedNightMode;
    private int mLastBroadcastState;
    private PowerManagerInternal mLocalPowerManager;
    private final LocalService mLocalService;
    public final Object mLock;
    private int mNightMode;
    private int mNightModeCustomType;
    private boolean mNightModeLocked;
    private NotificationManager mNotificationManager;
    private final BroadcastReceiver mOnScreenOffHandler;
    private final BroadcastReceiver mOnShutdown;
    private final BroadcastReceiver mOnTimeChangedHandler;
    private boolean mOverrideNightModeOff;
    private boolean mOverrideNightModeOn;
    private int mOverrideNightModeUser;
    private PowerManager mPowerManager;
    private boolean mPowerSave;

    @GuardedBy({"mLock"})
    private SparseArray<List<ProjectionHolder>> mProjectionHolders;

    @GuardedBy({"mLock"})
    private SparseArray<RemoteCallbackList<IOnProjectionStateChangedListener>> mProjectionListeners;
    private final BroadcastReceiver mResultReceiver;
    private final IUiModeManager.Stub mService;
    private int mSetUiMode;
    private final BroadcastReceiver mSettingsRestored;
    private boolean mSetupWizardComplete;
    private final ContentObserver mSetupWizardObserver;
    private boolean mStartDreamImmediatelyOnDock;
    private StatusBarManager mStatusBarManager;
    boolean mSystemReady;
    private boolean mTelevision;
    private final TwilightListener mTwilightListener;
    private TwilightManager mTwilightManager;
    private boolean mUiModeLocked;

    @GuardedBy({"mLock"})
    private final SparseArray<RemoteCallbackList<IUiModeManagerCallback>> mUiModeManagerCallbacks;
    private IUiModeManagerServiceWrapper mUiModemsWrapper;
    private IUiModeManagerServiceExt mUmssExt;
    private boolean mVrHeadset;
    private final IVrStateCallbacks mVrStateCallbacks;
    private boolean mWaitForScreenOff;
    private PowerManager.WakeLock mWakeLock;
    private boolean mWatch;
    private WindowManagerInternal mWindowManager;

    private static boolean isDeskDockState(int i) {
        return i == 1 || i == 3 || i == 4;
    }

    static {
        LOG = SystemProperties.getBoolean("persist.sys.assert.panic", false) && "0".equals(SystemProperties.get("persist.sys.agingtest", "0"));
        SUPPORTED_NIGHT_MODE_CUSTOM_TYPES = new ArraySet(new Integer[]{0, 1});
    }

    public UiModeManagerService(Context context) {
        this(context, false, null, new Injector());
    }

    @VisibleForTesting
    protected UiModeManagerService(Context context, boolean z, TwilightManager twilightManager, Injector injector) {
        super(context);
        this.mLock = new Object();
        this.mDockState = 0;
        this.mLastBroadcastState = 0;
        this.mNightMode = 1;
        this.mNightModeCustomType = -1;
        LocalTime of = LocalTime.of(22, 0);
        this.DEFAULT_CUSTOM_NIGHT_START_TIME = of;
        LocalTime of2 = LocalTime.of(6, 0);
        this.DEFAULT_CUSTOM_NIGHT_END_TIME = of2;
        this.mCustomAutoNightModeStartMilliseconds = of;
        this.mCustomAutoNightModeEndMilliseconds = of2;
        this.mCarModePackagePriority = new HashMap();
        this.mCarModeEnabled = false;
        this.mCharging = false;
        this.mPowerSave = false;
        this.mWaitForScreenOff = false;
        this.mLastBedtimeRequestedNightMode = false;
        this.mStartDreamImmediatelyOnDock = true;
        this.mDreamsDisabledByAmbientModeSuppression = false;
        this.mEnableCarDockLaunch = true;
        this.mUiModeLocked = false;
        this.mNightModeLocked = false;
        this.mCurUiMode = 0;
        this.mSetUiMode = 0;
        this.mHoldingConfiguration = false;
        this.mConfiguration = new Configuration();
        Handler handler = new Handler();
        this.mHandler = handler;
        this.mOverrideNightModeUser = 0;
        this.mLocalService = new LocalService();
        this.mUiModeManagerCallbacks = new SparseArray<>();
        this.mUmssExt = (IUiModeManagerServiceExt) ExtLoader.type(IUiModeManagerServiceExt.class).create();
        this.mContrasts = new SparseArray<>();
        this.mResultReceiver = new BroadcastReceiver() { // from class: com.android.server.UiModeManagerService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (getResultCode() != -1) {
                    if (UiModeManagerService.LOG) {
                        Slog.v(UiModeManagerService.TAG, "Handling broadcast result for action " + intent.getAction() + ": canceled: " + getResultCode());
                        return;
                    }
                    return;
                }
                int intExtra = intent.getIntExtra("enableFlags", 0);
                int intExtra2 = intent.getIntExtra("disableFlags", 0);
                synchronized (UiModeManagerService.this.mLock) {
                    UiModeManagerService.this.updateAfterBroadcastLocked(intent.getAction(), intExtra, intExtra2);
                }
            }
        };
        this.mDockModeReceiver = new BroadcastReceiver() { // from class: com.android.server.UiModeManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                UiModeManagerService.this.updateDockState(intent.getIntExtra("android.intent.extra.DOCK_STATE", 0));
            }
        };
        this.mBatteryReceiver = new BroadcastReceiver() { // from class: com.android.server.UiModeManagerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                action.hashCode();
                if (action.equals("android.intent.action.BATTERY_CHANGED")) {
                    UiModeManagerService.this.mCharging = intent.getIntExtra("plugged", 0) != 0;
                }
                synchronized (UiModeManagerService.this.mLock) {
                    UiModeManagerService uiModeManagerService = UiModeManagerService.this;
                    if (uiModeManagerService.mSystemReady) {
                        uiModeManagerService.updateLocked(0, 0);
                    }
                }
            }
        };
        this.mTwilightListener = new TwilightListener() { // from class: com.android.server.UiModeManagerService.4
            public void onTwilightStateChanged(TwilightState twilightState) {
                synchronized (UiModeManagerService.this.mLock) {
                    if (UiModeManagerService.this.mNightMode == 0) {
                        UiModeManagerService uiModeManagerService = UiModeManagerService.this;
                        if (uiModeManagerService.mSystemReady) {
                            if (uiModeManagerService.shouldApplyAutomaticChangesImmediately()) {
                                if (UiModeManagerService.LOG) {
                                    Slog.d(UiModeManagerService.TAG, "onTwilightStateChanged updateLocked now-->" + LocalTime.now().toString());
                                }
                                UiModeManagerService.this.updateLocked(0, 0);
                            } else {
                                if (UiModeManagerService.LOG) {
                                    Slog.d(UiModeManagerService.TAG, "onTwilightStateChanged wait screen off-->" + LocalTime.now().toString());
                                }
                                UiModeManagerService.this.registerScreenOffEventLocked();
                            }
                        }
                    }
                }
            }
        };
        this.mOnScreenOffHandler = new BroadcastReceiver() { // from class: com.android.server.UiModeManagerService.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                synchronized (UiModeManagerService.this.mLock) {
                    if (UiModeManagerService.LOG) {
                        Slog.d(UiModeManagerService.TAG, "screenOff receiver-->" + LocalTime.now().toString());
                    }
                    UiModeManagerService.this.unregisterScreenOffEventLocked();
                    UiModeManagerService.this.updateLocked(0, 0);
                }
            }
        };
        this.mOnTimeChangedHandler = new BroadcastReceiver() { // from class: com.android.server.UiModeManagerService.6
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                synchronized (UiModeManagerService.this.mLock) {
                    if (UiModeManagerService.LOG) {
                        Slog.d(UiModeManagerService.TAG, "TimeChanged-->" + LocalTime.now().toString());
                    }
                    UiModeManagerService.this.updateCustomTimeLocked();
                }
            }
        };
        this.mCustomTimeListener = new AlarmManager.OnAlarmListener() { // from class: com.android.server.UiModeManagerService$$ExternalSyntheticLambda2
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                UiModeManagerService.this.lambda$new$0();
            }
        };
        this.mVrStateCallbacks = new IVrStateCallbacks.Stub() { // from class: com.android.server.UiModeManagerService.7
            public void onVrStateChanged(boolean z2) {
                synchronized (UiModeManagerService.this.mLock) {
                    UiModeManagerService.this.mVrHeadset = z2;
                    UiModeManagerService uiModeManagerService = UiModeManagerService.this;
                    if (uiModeManagerService.mSystemReady) {
                        uiModeManagerService.updateLocked(0, 0);
                    }
                }
            }
        };
        this.mSetupWizardObserver = new ContentObserver(handler) { // from class: com.android.server.UiModeManagerService.8
            @Override // android.database.ContentObserver
            public void onChange(boolean z2, Uri uri) {
                synchronized (UiModeManagerService.this.mLock) {
                    if (UiModeManagerService.this.setupWizardCompleteForCurrentUser() && !z2) {
                        UiModeManagerService.this.mSetupWizardComplete = true;
                        UiModeManagerService.this.getContext().getContentResolver().unregisterContentObserver(UiModeManagerService.this.mSetupWizardObserver);
                        Context context2 = UiModeManagerService.this.getContext();
                        UiModeManagerService.this.updateNightModeFromSettingsLocked(context2, context2.getResources(), UserHandle.getCallingUserId());
                        UiModeManagerService.this.updateLocked(0, 0);
                        try {
                            ActivityManager.getService().forceStopPackage("com.android.settings", 0);
                            Slog.d(UiModeManagerService.TAG, "setupWizardComplete");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        this.mDarkThemeObserver = new ContentObserver(handler) { // from class: com.android.server.UiModeManagerService.9
            @Override // android.database.ContentObserver
            public void onChange(boolean z2, Uri uri) {
                UiModeManagerService.this.updateSystemProperties();
            }
        };
        this.mContrastObserver = new AnonymousClass10(handler);
        this.mOnShutdown = new BroadcastReceiver() { // from class: com.android.server.UiModeManagerService.11
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (UiModeManagerService.this.mNightMode == 0) {
                    UiModeManagerService uiModeManagerService = UiModeManagerService.this;
                    uiModeManagerService.persistComputedNightMode(uiModeManagerService.mCurrentUser);
                }
            }
        };
        this.mSettingsRestored = new BroadcastReceiver() { // from class: com.android.server.UiModeManagerService.12
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (Arrays.asList("ui_night_mode", "dark_theme_custom_start_time", "dark_theme_custom_end_time").contains(intent.getExtras().getCharSequence("setting_name"))) {
                    synchronized (UiModeManagerService.this.mLock) {
                        UiModeManagerService.this.updateNightModeFromSettingsLocked(context2, context2.getResources(), UserHandle.getCallingUserId());
                        UiModeManagerService.this.updateConfigurationLocked();
                    }
                }
            }
        };
        this.mService = new AnonymousClass13();
        this.mUiModemsWrapper = new UiModeManagerServiceWrapper();
        this.mConfiguration.setToDefaults();
        this.mSetupWizardComplete = z;
        this.mTwilightManager = twilightManager;
        this.mInjector = injector;
        this.mUmssExt.init(context, this);
    }

    private static Intent buildHomeIntent(String str) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory(str);
        intent.setFlags(270532608);
        return intent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        synchronized (this.mLock) {
            if (LOG) {
                Slog.d(TAG, "customTime alarm-->" + LocalTime.now().toString());
            }
            updateCustomTimeLocked();
        }
    }

    /* renamed from: com.android.server.UiModeManagerService$10, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class AnonymousClass10 extends ContentObserver {
        AnonymousClass10(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            synchronized (UiModeManagerService.this.mLock) {
                if (UiModeManagerService.this.updateContrastLocked()) {
                    final float contrastLocked = UiModeManagerService.this.getContrastLocked();
                    ((RemoteCallbackList) UiModeManagerService.this.mUiModeManagerCallbacks.get(UiModeManagerService.this.mCurrentUser, new RemoteCallbackList())).broadcast(FunctionalUtils.ignoreRemoteException(new FunctionalUtils.RemoteExceptionIgnoringConsumer() { // from class: com.android.server.UiModeManagerService$10$$ExternalSyntheticLambda0
                        public final void acceptOrThrow(Object obj) {
                            ((IUiModeManagerCallback) obj).notifyContrastChanged(contrastLocked);
                        }
                    }));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSystemProperties() {
        int intForUser = Settings.Secure.getIntForUser(getContext().getContentResolver(), "ui_night_mode", this.mNightMode, 0);
        if (intForUser == 0 || intForUser == 3) {
            intForUser = 2;
        }
        SystemProperties.set(SYSTEM_PROPERTY_DEVICE_THEME, Integer.toString(intForUser));
    }

    @VisibleForTesting
    void setStartDreamImmediatelyOnDock(boolean z) {
        this.mStartDreamImmediatelyOnDock = z;
    }

    @VisibleForTesting
    void setDreamsDisabledByAmbientModeSuppression(boolean z) {
        this.mDreamsDisabledByAmbientModeSuppression = z;
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2) {
        this.mCurrentUser = targetUser2.getUserIdentifier();
        if (this.mNightMode == 0) {
            persistComputedNightMode(targetUser.getUserIdentifier());
        }
        getContext().getContentResolver().unregisterContentObserver(this.mSetupWizardObserver);
        verifySetupWizardCompleted();
        unregisterScreenOffEventLocked();
        synchronized (this.mLock) {
            updateNightModeFromSettingsLocked(getContext(), getContext().getResources(), targetUser2.getUserIdentifier());
            updateLocked(0, 0);
            this.mUmssExt.persistNightModeStatistics(getContext(), this.mCurrentUser);
        }
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        if (i == 500) {
            synchronized (this.mLock) {
                Context context = getContext();
                boolean z = true;
                this.mSystemReady = true;
                this.mKeyguardManager = (KeyguardManager) context.getSystemService(KeyguardManager.class);
                PowerManager powerManager = (PowerManager) context.getSystemService("power");
                this.mPowerManager = powerManager;
                this.mWakeLock = powerManager.newWakeLock(26, TAG);
                this.mWindowManager = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
                this.mActivityTaskManager = (ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class);
                this.mAlarmManager = (AlarmManager) getContext().getSystemService(HostingRecord.TRIGGER_TYPE_ALARM);
                TwilightManager twilightManager = (TwilightManager) getLocalService(TwilightManager.class);
                if (twilightManager != null) {
                    this.mTwilightManager = twilightManager;
                }
                this.mLocalPowerManager = (PowerManagerInternal) LocalServices.getService(PowerManagerInternal.class);
                initPowerSave();
                if (this.mDockState != 2) {
                    z = false;
                }
                this.mCarModeEnabled = z;
                registerVrStateListener();
                context.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("ui_night_mode"), false, this.mDarkThemeObserver, 0);
                context.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("contrast_level"), false, this.mContrastObserver, -1);
                context.registerReceiver(this.mDockModeReceiver, new IntentFilter("android.intent.action.DOCK_EVENT"));
                IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
                intentFilter.addCategory("oplusBrEx@android.intent.action.BATTERY_CHANGED@BATTERYSTATE=CHARGING_CHANGED");
                context.registerReceiver(this.mBatteryReceiver, intentFilter);
                context.registerReceiver(this.mSettingsRestored, new IntentFilter("android.os.action.SETTING_RESTORED"));
                context.registerReceiver(this.mOnShutdown, new IntentFilter("android.intent.action.ACTION_SHUTDOWN"));
                this.mUmssExt.darkModeRegisterThermalProtect(this);
                this.mUmssExt.darkModeRegisterShutdownReceiver(this, this.mHandler);
                updateConfigurationLocked();
                applyConfigurationExternallyLocked();
            }
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        final Context context = getContext();
        verifySetupWizardCompleted();
        final Resources resources = context.getResources();
        this.mStartDreamImmediatelyOnDock = resources.getBoolean(17891834);
        this.mDreamsDisabledByAmbientModeSuppression = resources.getBoolean(17891638);
        this.mNightMode = resources.getInteger(R.integer.config_dockedStackDividerSnapMode);
        this.mDefaultUiModeType = resources.getInteger(R.integer.config_dynamicPowerSavingsDefaultDisableThreshold);
        this.mCarModeKeepsScreenOn = resources.getInteger(R.integer.config_defaultNightMode) == 1;
        this.mDeskModeKeepsScreenOn = resources.getInteger(R.integer.config_fingerprintMaxTemplatesPerUser) == 1;
        this.mEnableCarDockLaunch = resources.getBoolean(17891658);
        this.mUiModeLocked = resources.getBoolean(17891747);
        this.mNightModeLocked = resources.getBoolean(17891746);
        PackageManager packageManager = context.getPackageManager();
        this.mTelevision = packageManager.hasSystemFeature("android.hardware.type.television") || packageManager.hasSystemFeature("android.software.leanback");
        this.mCar = packageManager.hasSystemFeature("android.hardware.type.automotive");
        this.mWatch = packageManager.hasSystemFeature("android.hardware.type.watch");
        SystemServerInitThreadPool.submit(new Runnable() { // from class: com.android.server.UiModeManagerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                UiModeManagerService.this.lambda$onStart$1(context, resources);
            }
        }, TAG + ".onStart");
        publishBinderService("uimode", this.mService);
        publishLocalService(UiModeManagerInternal.class, this.mLocalService);
        this.mUmssExt.darkModeOnStartInit(context, this);
        this.mUmssExt.persistNightModeStatistics(getContext(), UserHandle.getCallingUserId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$1(Context context, Resources resources) {
        synchronized (this.mLock) {
            TwilightManager twilightManager = (TwilightManager) getLocalService(TwilightManager.class);
            if (twilightManager != null) {
                this.mTwilightManager = twilightManager;
            }
            updateNightModeFromSettingsLocked(context, resources, UserHandle.getCallingUserId());
            updateSystemProperties();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void persistComputedNightMode(int i) {
        Settings.Secure.putIntForUser(getContext().getContentResolver(), "ui_night_mode_last_computed", this.mComputedNightMode ? 1 : 0, i);
    }

    private void initPowerSave() {
        if (this.mUmssExt.darkModeShouldHideSaveMode()) {
            return;
        }
        this.mPowerSave = this.mLocalPowerManager.getLowPowerState(16).batterySaverEnabled;
        this.mLocalPowerManager.registerLowPowerModeObserver(16, new Consumer() { // from class: com.android.server.UiModeManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                UiModeManagerService.this.lambda$initPowerSave$2((PowerSaveState) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initPowerSave$2(PowerSaveState powerSaveState) {
        synchronized (this.mLock) {
            boolean z = this.mPowerSave;
            boolean z2 = powerSaveState.batterySaverEnabled;
            if (z == z2) {
                return;
            }
            this.mPowerSave = z2;
            if (this.mSystemReady) {
                updateLocked(0, 0);
            }
        }
    }

    @VisibleForTesting
    protected IUiModeManager getService() {
        return this.mService;
    }

    @VisibleForTesting
    protected Configuration getConfiguration() {
        return this.mConfiguration;
    }

    private void verifySetupWizardCompleted() {
        Context context = getContext();
        int callingUserId = UserHandle.getCallingUserId();
        if (!setupWizardCompleteForCurrentUser()) {
            this.mSetupWizardComplete = false;
            context.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("user_setup_complete"), false, this.mSetupWizardObserver, callingUserId);
        } else {
            this.mSetupWizardComplete = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setupWizardCompleteForCurrentUser() {
        return Settings.Secure.getIntForUser(getContext().getContentResolver(), "user_setup_complete", 0, UserHandle.getCallingUserId()) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCustomTimeLocked() {
        if (this.mNightMode != 3) {
            return;
        }
        if (shouldApplyAutomaticChangesImmediately()) {
            if (LOG) {
                Slog.d(TAG, "updateCustomTimeLocked updateLocked now-->" + LocalTime.now().toString());
            }
            updateLocked(0, 0);
        } else {
            if (LOG) {
                Slog.d(TAG, "updateCustomTimeLocked wait screen off-->" + LocalTime.now().toString());
            }
            registerScreenOffEventLocked();
        }
        scheduleNextCustomTimeListener();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNightModeFromSettingsLocked(Context context, Resources resources, int i) {
        if (this.mCarModeEnabled || this.mCar || !this.mSetupWizardComplete) {
            return;
        }
        this.mNightMode = Settings.Secure.getIntForUser(context.getContentResolver(), "ui_night_mode", resources.getInteger(R.integer.config_dockedStackDividerSnapMode), i);
        this.mNightModeCustomType = Settings.Secure.getIntForUser(context.getContentResolver(), "ui_night_mode_custom_type", -1, i);
        this.mOverrideNightModeOn = Settings.Secure.getIntForUser(context.getContentResolver(), "ui_night_mode_override_on", 0, i) != 0;
        this.mOverrideNightModeOff = Settings.Secure.getIntForUser(context.getContentResolver(), "ui_night_mode_override_off", 0, i) != 0;
        this.mCustomAutoNightModeStartMilliseconds = LocalTime.ofNanoOfDay(Settings.Secure.getLongForUser(context.getContentResolver(), "dark_theme_custom_start_time", this.DEFAULT_CUSTOM_NIGHT_START_TIME.toNanoOfDay() / 1000, i) * 1000);
        this.mCustomAutoNightModeEndMilliseconds = LocalTime.ofNanoOfDay(Settings.Secure.getLongForUser(context.getContentResolver(), "dark_theme_custom_end_time", this.DEFAULT_CUSTOM_NIGHT_END_TIME.toNanoOfDay() / 1000, i) * 1000);
        if (this.mNightMode == 0) {
            this.mComputedNightMode = Settings.Secure.getIntForUser(context.getContentResolver(), "ui_night_mode_last_computed", 0, i) != 0;
        }
        this.mUmssExt.darkModeInitSettings(getContext());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long toMilliSeconds(LocalTime localTime) {
        return localTime.toNanoOfDay() / 1000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static LocalTime fromMilliseconds(long j) {
        return LocalTime.ofNanoOfDay(j * 1000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerScreenOffEventLocked() {
        if (this.mPowerSave) {
            return;
        }
        this.mWaitForScreenOff = true;
        getContext().registerReceiver(this.mOnScreenOffHandler, new IntentFilter("android.intent.action.SCREEN_OFF"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelCustomAlarm() {
        this.mAlarmManager.cancel(this.mCustomTimeListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterScreenOffEventLocked() {
        this.mWaitForScreenOff = false;
        try {
            getContext().unregisterReceiver(this.mOnScreenOffHandler);
        } catch (IllegalArgumentException unused) {
        }
    }

    private void registerTimeChangeEvent() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        getContext().registerReceiver(this.mOnTimeChangedHandler, intentFilter);
    }

    private void unregisterTimeChangeEvent() {
        try {
            getContext().unregisterReceiver(this.mOnTimeChangedHandler);
        } catch (IllegalArgumentException unused) {
        }
    }

    /* renamed from: com.android.server.UiModeManagerService$13, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class AnonymousClass13 extends IUiModeManager.Stub {
        AnonymousClass13() {
        }

        public void addCallback(IUiModeManagerCallback iUiModeManagerCallback) {
            int callingUserId = UserHandle.getCallingUserId();
            synchronized (UiModeManagerService.this.mLock) {
                if (!UiModeManagerService.this.mUiModeManagerCallbacks.contains(callingUserId)) {
                    UiModeManagerService.this.mUiModeManagerCallbacks.put(callingUserId, new RemoteCallbackList());
                }
                ((RemoteCallbackList) UiModeManagerService.this.mUiModeManagerCallbacks.get(callingUserId)).register(iUiModeManagerCallback);
            }
        }

        public void enableCarMode(int i, int i2, String str) {
            if (isUiModeLocked()) {
                Slog.e(UiModeManagerService.TAG, "enableCarMode while UI mode is locked");
                return;
            }
            if (i2 != 0 && UiModeManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.ENTER_CAR_MODE_PRIORITIZED") != 0) {
                throw new SecurityException("Enabling car mode with a priority requires permission ENTER_CAR_MODE_PRIORITIZED");
            }
            if (!(UiModeManagerService.this.mInjector.getCallingUid() == 2000)) {
                UiModeManagerService.this.assertLegit(str);
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (UiModeManagerService.this.mLock) {
                    UiModeManagerService.this.setCarModeLocked(true, i, i2, str);
                    UiModeManagerService uiModeManagerService = UiModeManagerService.this;
                    if (uiModeManagerService.mSystemReady) {
                        uiModeManagerService.updateLocked(i, 0);
                    }
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void disableCarMode(int i) {
            disableCarModeByCallingPackage(i, null);
        }

        public void disableCarModeByCallingPackage(int i, final String str) {
            if (isUiModeLocked()) {
                Slog.e(UiModeManagerService.TAG, "disableCarMode while UI mode is locked");
                return;
            }
            int callingUid = UiModeManagerService.this.mInjector.getCallingUid();
            boolean z = callingUid == 1000;
            boolean z2 = callingUid == 2000;
            if (!z && !z2) {
                UiModeManagerService.this.assertLegit(str);
            }
            int i2 = z ? i : i & (-3);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (UiModeManagerService.this.mLock) {
                    UiModeManagerService.this.setCarModeLocked(false, i2, ((Integer) UiModeManagerService.this.mCarModePackagePriority.entrySet().stream().filter(new Predicate() { // from class: com.android.server.UiModeManagerService$13$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            boolean lambda$disableCarModeByCallingPackage$0;
                            lambda$disableCarModeByCallingPackage$0 = UiModeManagerService.AnonymousClass13.lambda$disableCarModeByCallingPackage$0(str, (Map.Entry) obj);
                            return lambda$disableCarModeByCallingPackage$0;
                        }
                    }).findFirst().map(new UiModeManagerService$13$$ExternalSyntheticLambda1()).orElse(0)).intValue(), str);
                    UiModeManagerService uiModeManagerService = UiModeManagerService.this;
                    if (uiModeManagerService.mSystemReady) {
                        uiModeManagerService.updateLocked(0, i);
                    }
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$disableCarModeByCallingPackage$0(String str, Map.Entry entry) {
            return ((String) entry.getValue()).equals(str);
        }

        public int getCurrentModeType() {
            int i;
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (UiModeManagerService.this.mLock) {
                    i = UiModeManagerService.this.mCurUiMode & 15;
                }
                return i;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setNightMode(int i) {
            setNightModeInternal(i, i == 3 ? 0 : -1);
        }

        private void setNightModeInternal(int i, int i2) {
            if (isNightModeLocked() && UiModeManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.MODIFY_DAY_NIGHT_MODE") != 0) {
                Slog.e(UiModeManagerService.TAG, "Night mode locked, requires MODIFY_DAY_NIGHT_MODE permission");
                return;
            }
            if (i != 0 && i != 1 && i != 2) {
                if (i == 3) {
                    if (!UiModeManagerService.SUPPORTED_NIGHT_MODE_CUSTOM_TYPES.contains(Integer.valueOf(i2))) {
                        throw new IllegalArgumentException("Can't set the custom type to " + i2);
                    }
                } else {
                    throw new IllegalArgumentException("Unknown mode: " + i);
                }
            }
            int callingUserId = UserHandle.getCallingUserId();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (UiModeManagerService.this.mLock) {
                    if (UiModeManagerService.LOG) {
                        Slog.d(UiModeManagerService.TAG, "userId:" + callingUserId + "-->pid:" + Binder.getCallingPid() + "-->setNightMode:" + i + "-->oldMode:" + UiModeManagerService.this.mNightMode);
                    }
                    UiModeManagerService.this.mUmssExt.upCommonStatistics(UiModeManagerService.this.getContext(), callingUserId, UiModeManagerService.this.mNightMode, i);
                    if (UiModeManagerService.this.mNightMode != i || UiModeManagerService.this.mNightModeCustomType != i2) {
                        if (UiModeManagerService.this.mNightMode == 0 || UiModeManagerService.this.mNightMode == 3) {
                            UiModeManagerService.this.unregisterScreenOffEventLocked();
                            UiModeManagerService.this.cancelCustomAlarm();
                        }
                        UiModeManagerService uiModeManagerService = UiModeManagerService.this;
                        if (i != 3) {
                            i2 = -1;
                        }
                        uiModeManagerService.mNightModeCustomType = i2;
                        UiModeManagerService.this.mNightMode = i;
                        UiModeManagerService.this.resetNightModeOverrideLocked();
                        UiModeManagerService.this.persistNightMode(callingUserId);
                        if ((UiModeManagerService.this.mNightMode != 0 && UiModeManagerService.this.mNightMode != 3) || UiModeManagerService.this.shouldApplyAutomaticChangesImmediately()) {
                            UiModeManagerService.this.unregisterScreenOffEventLocked();
                            UiModeManagerService.this.updateLocked(0, 0);
                        } else {
                            UiModeManagerService.this.registerScreenOffEventLocked();
                        }
                    }
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int getNightMode() {
            int i;
            synchronized (UiModeManagerService.this.mLock) {
                i = UiModeManagerService.this.mNightMode;
            }
            return i;
        }

        public void setNightModeCustomType(int i) {
            if (UiModeManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.MODIFY_DAY_NIGHT_MODE") != 0) {
                throw new SecurityException("setNightModeCustomType requires MODIFY_DAY_NIGHT_MODE permission");
            }
            setNightModeInternal(3, i);
        }

        public int getNightModeCustomType() {
            int i;
            if (UiModeManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.MODIFY_DAY_NIGHT_MODE") != 0) {
                throw new SecurityException("getNightModeCustomType requires MODIFY_DAY_NIGHT_MODE permission");
            }
            synchronized (UiModeManagerService.this.mLock) {
                i = UiModeManagerService.this.mNightModeCustomType;
            }
            return i;
        }

        public void setApplicationNightMode(int i) {
            if (i != 0 && i != 1 && i != 2 && i != 3) {
                throw new IllegalArgumentException("Unknown mode: " + i);
            }
            int i2 = i != 1 ? i != 2 ? 0 : 32 : 16;
            ActivityTaskManagerInternal.PackageConfigurationUpdater createPackageConfigurationUpdater = UiModeManagerService.this.mActivityTaskManager.createPackageConfigurationUpdater();
            createPackageConfigurationUpdater.setNightMode(i2);
            createPackageConfigurationUpdater.commit();
        }

        public boolean isUiModeLocked() {
            boolean z;
            synchronized (UiModeManagerService.this.mLock) {
                z = UiModeManagerService.this.mUiModeLocked;
            }
            return z;
        }

        public boolean isNightModeLocked() {
            boolean z;
            synchronized (UiModeManagerService.this.mLock) {
                z = UiModeManagerService.this.mNightModeLocked;
            }
            return z;
        }

        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
            new Shell(UiModeManagerService.this.mService).exec(UiModeManagerService.this.mService, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }

        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(UiModeManagerService.this.getContext(), UiModeManagerService.TAG, printWriter)) {
                UiModeManagerService.this.dumpImpl(printWriter);
            }
        }

        public boolean setNightModeActivatedForCustomMode(int i, boolean z) {
            return setNightModeActivatedForModeInternal(i, z);
        }

        public boolean setNightModeActivated(boolean z) {
            return setNightModeActivatedForModeInternal(UiModeManagerService.this.mNightModeCustomType, z);
        }

        private boolean setNightModeActivatedForModeInternal(int i, boolean z) {
            if (UiModeManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.MODIFY_DAY_NIGHT_MODE") != 0) {
                Slog.e(UiModeManagerService.TAG, "Night mode locked, requires MODIFY_DAY_NIGHT_MODE permission");
                return false;
            }
            if (Binder.getCallingUserHandle().getIdentifier() != UiModeManagerService.this.mCurrentUser && UiModeManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS") != 0) {
                Slog.e(UiModeManagerService.TAG, "Target user is not current user, INTERACT_ACROSS_USERS permission is required");
                return false;
            }
            if (i == 1) {
                UiModeManagerService.this.mLastBedtimeRequestedNightMode = z;
            }
            if (i != UiModeManagerService.this.mNightModeCustomType) {
                return false;
            }
            synchronized (UiModeManagerService.this.mLock) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (UiModeManagerService.LOG) {
                        Slog.d(UiModeManagerService.TAG, "pid:" + Binder.getCallingPid() + "-->setNightModeActivated-->" + z);
                    }
                    if (UiModeManagerService.this.mNightMode != 0 && UiModeManagerService.this.mNightMode != 3) {
                        if (UiModeManagerService.this.mNightMode == 1 && z) {
                            UiModeManagerService.this.mUmssExt.upCommonStatistics(UiModeManagerService.this.getContext(), UiModeManagerService.this.mCurrentUser, UiModeManagerService.this.mNightMode, 2);
                            UiModeManagerService.this.mNightMode = 2;
                        } else if (UiModeManagerService.this.mNightMode == 2 && !z) {
                            UiModeManagerService.this.mUmssExt.upCommonStatistics(UiModeManagerService.this.getContext(), UiModeManagerService.this.mCurrentUser, UiModeManagerService.this.mNightMode, 1);
                            UiModeManagerService.this.mNightMode = 1;
                        }
                        UiModeManagerService.this.updateConfigurationLocked();
                        UiModeManagerService.this.applyConfigurationExternallyLocked();
                        UiModeManagerService uiModeManagerService = UiModeManagerService.this;
                        uiModeManagerService.persistNightMode(uiModeManagerService.mCurrentUser);
                    }
                    UiModeManagerService.this.unregisterScreenOffEventLocked();
                    UiModeManagerService.this.mOverrideNightModeOff = z ? false : true;
                    UiModeManagerService.this.mOverrideNightModeOn = z;
                    UiModeManagerService uiModeManagerService2 = UiModeManagerService.this;
                    uiModeManagerService2.mOverrideNightModeUser = uiModeManagerService2.mCurrentUser;
                    UiModeManagerService uiModeManagerService3 = UiModeManagerService.this;
                    uiModeManagerService3.persistNightModeOverrides(uiModeManagerService3.mCurrentUser);
                    UiModeManagerService.this.updateConfigurationLocked();
                    UiModeManagerService.this.applyConfigurationExternallyLocked();
                    UiModeManagerService uiModeManagerService4 = UiModeManagerService.this;
                    uiModeManagerService4.persistNightMode(uiModeManagerService4.mCurrentUser);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
            return true;
        }

        public long getCustomNightModeStart() {
            return UiModeManagerService.this.mCustomAutoNightModeStartMilliseconds.toNanoOfDay() / 1000;
        }

        public void setCustomNightModeStart(long j) {
            LocalTime ofNanoOfDay;
            if (isNightModeLocked() && UiModeManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.MODIFY_DAY_NIGHT_MODE") != 0) {
                Slog.e(UiModeManagerService.TAG, "Set custom time start, requires MODIFY_DAY_NIGHT_MODE permission");
                return;
            }
            int callingUserId = UserHandle.getCallingUserId();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                try {
                    ofNanoOfDay = LocalTime.ofNanoOfDay(j * 1000);
                } catch (DateTimeException unused) {
                    UiModeManagerService.this.unregisterScreenOffEventLocked();
                }
                if (ofNanoOfDay == null) {
                    return;
                }
                UiModeManagerService.this.mCustomAutoNightModeStartMilliseconds = ofNanoOfDay;
                if (UiModeManagerService.LOG) {
                    Slog.d(UiModeManagerService.TAG, "pid:" + Binder.getCallingPid() + "-->setCustomNightModeStart-->" + UiModeManagerService.this.mCustomAutoNightModeStartMilliseconds);
                }
                UiModeManagerService.this.persistNightMode(callingUserId);
                UiModeManagerService.this.onCustomTimeUpdated(callingUserId);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public long getCustomNightModeEnd() {
            return UiModeManagerService.this.mCustomAutoNightModeEndMilliseconds.toNanoOfDay() / 1000;
        }

        public void setCustomNightModeEnd(long j) {
            LocalTime ofNanoOfDay;
            if (isNightModeLocked() && UiModeManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.MODIFY_DAY_NIGHT_MODE") != 0) {
                Slog.e(UiModeManagerService.TAG, "Set custom time end, requires MODIFY_DAY_NIGHT_MODE permission");
                return;
            }
            int callingUserId = UserHandle.getCallingUserId();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                try {
                    ofNanoOfDay = LocalTime.ofNanoOfDay(j * 1000);
                } catch (DateTimeException unused) {
                    UiModeManagerService.this.unregisterScreenOffEventLocked();
                }
                if (ofNanoOfDay == null) {
                    return;
                }
                UiModeManagerService.this.mCustomAutoNightModeEndMilliseconds = ofNanoOfDay;
                if (UiModeManagerService.LOG) {
                    Slog.d(UiModeManagerService.TAG, "pid:" + Binder.getCallingPid() + "-->setCustomNightModeEnd-->" + UiModeManagerService.this.mCustomAutoNightModeEndMilliseconds);
                }
                UiModeManagerService.this.onCustomTimeUpdated(callingUserId);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public boolean requestProjection(IBinder iBinder, int i, String str) {
            UiModeManagerService.this.assertLegit(str);
            UiModeManagerService.assertSingleProjectionType(i);
            UiModeManagerService.this.enforceProjectionTypePermissions(i);
            synchronized (UiModeManagerService.this.mLock) {
                if (UiModeManagerService.this.mProjectionHolders == null) {
                    UiModeManagerService.this.mProjectionHolders = new SparseArray(1);
                }
                if (!UiModeManagerService.this.mProjectionHolders.contains(i)) {
                    UiModeManagerService.this.mProjectionHolders.put(i, new ArrayList(1));
                }
                List list = (List) UiModeManagerService.this.mProjectionHolders.get(i);
                for (int i2 = 0; i2 < list.size(); i2++) {
                    if (str.equals(((ProjectionHolder) list.get(i2)).mPackageName)) {
                        return true;
                    }
                }
                if (i == 1 && !list.isEmpty()) {
                    return false;
                }
                final UiModeManagerService uiModeManagerService = UiModeManagerService.this;
                ProjectionHolder projectionHolder = new ProjectionHolder(str, i, iBinder, new ProjectionHolder.ProjectionReleaser() { // from class: com.android.server.UiModeManagerService$13$$ExternalSyntheticLambda2
                    @Override // com.android.server.UiModeManagerService.ProjectionHolder.ProjectionReleaser
                    public final boolean release(int i3, String str2) {
                        boolean releaseProjectionUnchecked;
                        releaseProjectionUnchecked = UiModeManagerService.this.releaseProjectionUnchecked(i3, str2);
                        return releaseProjectionUnchecked;
                    }
                });
                if (!projectionHolder.linkToDeath()) {
                    return false;
                }
                list.add(projectionHolder);
                Slog.d(UiModeManagerService.TAG, "Package " + str + " set projection type " + i + ".");
                UiModeManagerService.this.onProjectionStateChangedLocked(i);
                return true;
            }
        }

        public boolean releaseProjection(int i, String str) {
            UiModeManagerService.this.assertLegit(str);
            UiModeManagerService.assertSingleProjectionType(i);
            UiModeManagerService.this.enforceProjectionTypePermissions(i);
            return UiModeManagerService.this.releaseProjectionUnchecked(i, str);
        }

        public int getActiveProjectionTypes() {
            int i;
            UiModeManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.READ_PROJECTION_STATE", "getActiveProjectionTypes");
            synchronized (UiModeManagerService.this.mLock) {
                i = 0;
                if (UiModeManagerService.this.mProjectionHolders != null) {
                    int i2 = 0;
                    while (i < UiModeManagerService.this.mProjectionHolders.size()) {
                        if (!((List) UiModeManagerService.this.mProjectionHolders.valueAt(i)).isEmpty()) {
                            i2 |= UiModeManagerService.this.mProjectionHolders.keyAt(i);
                        }
                        i++;
                    }
                    i = i2;
                }
            }
            return i;
        }

        public List<String> getProjectingPackages(int i) {
            ArrayList arrayList;
            UiModeManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.READ_PROJECTION_STATE", "getProjectionState");
            synchronized (UiModeManagerService.this.mLock) {
                arrayList = new ArrayList();
                UiModeManagerService.this.populateWithRelevantActivePackageNames(i, arrayList);
            }
            return arrayList;
        }

        public void addOnProjectionStateChangedListener(IOnProjectionStateChangedListener iOnProjectionStateChangedListener, int i) {
            UiModeManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.READ_PROJECTION_STATE", "addOnProjectionStateChangedListener");
            if (i == 0) {
                return;
            }
            synchronized (UiModeManagerService.this.mLock) {
                if (UiModeManagerService.this.mProjectionListeners == null) {
                    UiModeManagerService.this.mProjectionListeners = new SparseArray(1);
                }
                if (!UiModeManagerService.this.mProjectionListeners.contains(i)) {
                    UiModeManagerService.this.mProjectionListeners.put(i, new RemoteCallbackList());
                }
                if (((RemoteCallbackList) UiModeManagerService.this.mProjectionListeners.get(i)).register(iOnProjectionStateChangedListener)) {
                    ArrayList arrayList = new ArrayList();
                    int populateWithRelevantActivePackageNames = UiModeManagerService.this.populateWithRelevantActivePackageNames(i, arrayList);
                    if (!arrayList.isEmpty()) {
                        try {
                            iOnProjectionStateChangedListener.onProjectionStateChanged(populateWithRelevantActivePackageNames, arrayList);
                        } catch (RemoteException unused) {
                            Slog.w(UiModeManagerService.TAG, "Failed a call to onProjectionStateChanged() during listener registration.");
                        }
                    }
                }
            }
        }

        public void removeOnProjectionStateChangedListener(IOnProjectionStateChangedListener iOnProjectionStateChangedListener) {
            UiModeManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.READ_PROJECTION_STATE", "removeOnProjectionStateChangedListener");
            synchronized (UiModeManagerService.this.mLock) {
                if (UiModeManagerService.this.mProjectionListeners != null) {
                    for (int i = 0; i < UiModeManagerService.this.mProjectionListeners.size(); i++) {
                        ((RemoteCallbackList) UiModeManagerService.this.mProjectionListeners.valueAt(i)).unregister(iOnProjectionStateChangedListener);
                    }
                }
            }
        }

        public float getContrast() {
            float contrastLocked;
            synchronized (UiModeManagerService.this.mLock) {
                contrastLocked = UiModeManagerService.this.getContrastLocked();
            }
            return contrastLocked;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceProjectionTypePermissions(int i) {
        if ((i & 1) != 0) {
            getContext().enforceCallingPermission("android.permission.TOGGLE_AUTOMOTIVE_PROJECTION", "toggleProjection");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void assertSingleProjectionType(int i) {
        boolean z = ((i + (-1)) & i) == 0;
        if (i == 0 || !z) {
            throw new IllegalArgumentException("Must specify exactly one projection type.");
        }
    }

    private static List<String> toPackageNameList(Collection<ProjectionHolder> collection) {
        ArrayList arrayList = new ArrayList();
        Iterator<ProjectionHolder> it = collection.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().mPackageName);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public int populateWithRelevantActivePackageNames(int i, List<String> list) {
        list.clear();
        if (this.mProjectionHolders == null) {
            return 0;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.mProjectionHolders.size(); i3++) {
            int keyAt = this.mProjectionHolders.keyAt(i3);
            List<ProjectionHolder> valueAt = this.mProjectionHolders.valueAt(i3);
            if ((i & keyAt) != 0 && list.addAll(toPackageNameList(valueAt))) {
                i2 |= keyAt;
            }
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean releaseProjectionUnchecked(int i, String str) {
        boolean z;
        List<ProjectionHolder> list;
        synchronized (this.mLock) {
            SparseArray<List<ProjectionHolder>> sparseArray = this.mProjectionHolders;
            z = false;
            if (sparseArray != null && (list = sparseArray.get(i)) != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    ProjectionHolder projectionHolder = list.get(size);
                    if (str.equals(projectionHolder.mPackageName)) {
                        projectionHolder.unlinkToDeath();
                        Slog.d(TAG, "Projection type " + i + " released by " + str + ".");
                        list.remove(size);
                        z = true;
                    }
                }
            }
            if (z) {
                onProjectionStateChangedLocked(i);
            } else {
                Slog.w(TAG, str + " tried to release projection type " + i + " but was not set by that package.");
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public float getContrastLocked() {
        if (!this.mContrasts.contains(this.mCurrentUser)) {
            updateContrastLocked();
        }
        return this.mContrasts.get(this.mCurrentUser).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public boolean updateContrastLocked() {
        float floatForUser = Settings.Secure.getFloatForUser(getContext().getContentResolver(), "contrast_level", 0.0f, this.mCurrentUser);
        if (Math.abs(this.mContrasts.get(this.mCurrentUser, Float.valueOf(Float.MAX_VALUE)).floatValue() - floatForUser) < 1.0E-10d) {
            return false;
        }
        this.mContrasts.put(this.mCurrentUser, Float.valueOf(floatForUser));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class ProjectionHolder implements IBinder.DeathRecipient {
        private final IBinder mBinder;
        private final String mPackageName;
        private final ProjectionReleaser mProjectionReleaser;
        private final int mProjectionType;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public interface ProjectionReleaser {
            boolean release(int i, String str);
        }

        private ProjectionHolder(String str, int i, IBinder iBinder, ProjectionReleaser projectionReleaser) {
            this.mPackageName = str;
            this.mProjectionType = i;
            this.mBinder = iBinder;
            this.mProjectionReleaser = projectionReleaser;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean linkToDeath() {
            try {
                this.mBinder.linkToDeath(this, 0);
                return true;
            } catch (RemoteException e) {
                Slog.e(UiModeManagerService.TAG, "linkToDeath failed for projection requester: " + this.mPackageName + ".", e);
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unlinkToDeath() {
            this.mBinder.unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Slog.w(UiModeManagerService.TAG, "Projection holder " + this.mPackageName + " died. Releasing projection type " + this.mProjectionType + ".");
            this.mProjectionReleaser.release(this.mProjectionType, this.mPackageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertLegit(String str) {
        if (doesPackageHaveCallingUid(str)) {
            return;
        }
        throw new SecurityException("Caller claimed bogus packageName: " + str + ".");
    }

    private boolean doesPackageHaveCallingUid(String str) {
        int callingUid = this.mInjector.getCallingUid();
        int userId = UserHandle.getUserId(callingUid);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return getContext().getPackageManager().getPackageUidAsUser(str, userId) == callingUid;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void onProjectionStateChangedLocked(int i) {
        if (this.mProjectionListeners == null) {
            return;
        }
        for (int i2 = 0; i2 < this.mProjectionListeners.size(); i2++) {
            int keyAt = this.mProjectionListeners.keyAt(i2);
            if ((i & keyAt) != 0) {
                RemoteCallbackList<IOnProjectionStateChangedListener> valueAt = this.mProjectionListeners.valueAt(i2);
                ArrayList arrayList = new ArrayList();
                int populateWithRelevantActivePackageNames = populateWithRelevantActivePackageNames(keyAt, arrayList);
                int beginBroadcast = valueAt.beginBroadcast();
                for (int i3 = 0; i3 < beginBroadcast; i3++) {
                    try {
                        valueAt.getBroadcastItem(i3).onProjectionStateChanged(populateWithRelevantActivePackageNames, arrayList);
                    } catch (RemoteException unused) {
                        Slog.w(TAG, "Failed a call to onProjectionStateChanged().");
                    }
                }
                valueAt.finishBroadcast();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCustomTimeUpdated(int i) {
        persistNightMode(i);
        if (this.mNightMode != 3) {
            return;
        }
        if (shouldApplyAutomaticChangesImmediately()) {
            unregisterScreenOffEventLocked();
            updateLocked(0, 0);
            if (LOG) {
                Slog.d(TAG, "onCustomTimeUpdated updateLocked now");
                return;
            }
            return;
        }
        registerScreenOffEventLocked();
        if (LOG) {
            Slog.d(TAG, "onCustomTimeUpdated wait screen off");
        }
    }

    void dumpImpl(PrintWriter printWriter) {
        synchronized (this.mLock) {
            printWriter.println("Current UI Mode Service state:");
            printWriter.print("  mDockState=");
            printWriter.print(this.mDockState);
            printWriter.print(" mLastBroadcastState=");
            printWriter.println(this.mLastBroadcastState);
            printWriter.print(" mStartDreamImmediatelyOnDock=");
            printWriter.print(this.mStartDreamImmediatelyOnDock);
            printWriter.print("  mNightMode=");
            printWriter.print(this.mNightMode);
            printWriter.print(" (");
            printWriter.print(Shell.nightModeToStr(this.mNightMode, this.mNightModeCustomType));
            printWriter.print(") ");
            printWriter.print(" mOverrideOn/Off=");
            printWriter.print(this.mOverrideNightModeOn);
            printWriter.print("/");
            printWriter.print(this.mOverrideNightModeOff);
            printWriter.print(" mNightModeLocked=");
            printWriter.println(this.mNightModeLocked);
            printWriter.print("  mCarModeEnabled=");
            printWriter.print(this.mCarModeEnabled);
            printWriter.print(" (carModeApps=");
            for (Map.Entry<Integer, String> entry : this.mCarModePackagePriority.entrySet()) {
                printWriter.print(entry.getKey());
                printWriter.print(":");
                printWriter.print(entry.getValue());
                printWriter.print(" ");
            }
            printWriter.println("");
            printWriter.print(" waitScreenOff=");
            printWriter.print(this.mWaitForScreenOff);
            printWriter.print(" mComputedNightMode=");
            printWriter.print(this.mComputedNightMode);
            printWriter.print(" customStart=");
            printWriter.print(this.mCustomAutoNightModeStartMilliseconds);
            printWriter.print(" customEnd");
            printWriter.print(this.mCustomAutoNightModeEndMilliseconds);
            printWriter.print(" mCarModeEnableFlags=");
            printWriter.print(this.mCarModeEnableFlags);
            printWriter.print(" mEnableCarDockLaunch=");
            printWriter.println(this.mEnableCarDockLaunch);
            printWriter.print("  mCurUiMode=0x");
            printWriter.print(Integer.toHexString(this.mCurUiMode));
            printWriter.print(" mUiModeLocked=");
            printWriter.print(this.mUiModeLocked);
            printWriter.print(" mSetUiMode=0x");
            printWriter.println(Integer.toHexString(this.mSetUiMode));
            printWriter.print("  mHoldingConfiguration=");
            printWriter.print(this.mHoldingConfiguration);
            printWriter.print(" mSystemReady=");
            printWriter.println(this.mSystemReady);
            if (this.mTwilightManager != null) {
                printWriter.print("  mTwilightService.getLastTwilightState()=");
                printWriter.println(this.mTwilightManager.getLastTwilightState());
                this.mUmssExt.darkModeDumpUiModeManagerServiceMessage(printWriter, this.mTwilightManager);
            }
        }
    }

    void setCarModeLocked(boolean z, int i, int i2, String str) {
        if (z) {
            enableCarMode(i2, str);
        } else {
            disableCarMode(i, i2, str);
        }
        boolean isCarModeEnabled = isCarModeEnabled();
        if (this.mCarModeEnabled != isCarModeEnabled) {
            this.mCarModeEnabled = isCarModeEnabled;
            if (!isCarModeEnabled) {
                Context context = getContext();
                updateNightModeFromSettingsLocked(context, context.getResources(), UserHandle.getCallingUserId());
            }
        }
        this.mCarModeEnableFlags = i;
    }

    private void disableCarMode(int i, int i2, String str) {
        boolean z = true;
        boolean z2 = (i & 2) != 0;
        boolean contains = this.mCarModePackagePriority.keySet().contains(Integer.valueOf(i2));
        if (!(i2 == 0) && ((!contains || !this.mCarModePackagePriority.get(Integer.valueOf(i2)).equals(str)) && !z2)) {
            z = false;
        }
        if (z) {
            Slog.d(TAG, "disableCarMode: disabling, priority=" + i2 + ", packageName=" + str);
            if (z2) {
                ArraySet<Map.Entry> arraySet = new ArraySet(this.mCarModePackagePriority.entrySet());
                this.mCarModePackagePriority.clear();
                for (Map.Entry entry : arraySet) {
                    notifyCarModeDisabled(((Integer) entry.getKey()).intValue(), (String) entry.getValue());
                }
                return;
            }
            this.mCarModePackagePriority.remove(Integer.valueOf(i2));
            notifyCarModeDisabled(i2, str);
        }
    }

    private void enableCarMode(int i, String str) {
        boolean containsKey = this.mCarModePackagePriority.containsKey(Integer.valueOf(i));
        boolean containsValue = this.mCarModePackagePriority.containsValue(str);
        if (!containsKey && !containsValue) {
            Slog.d(TAG, "enableCarMode: enabled at priority=" + i + ", packageName=" + str);
            this.mCarModePackagePriority.put(Integer.valueOf(i), str);
            notifyCarModeEnabled(i, str);
            return;
        }
        Slog.d(TAG, "enableCarMode: car mode at priority " + i + " already enabled.");
    }

    private void notifyCarModeEnabled(int i, String str) {
        Intent intent = new Intent("android.app.action.ENTER_CAR_MODE_PRIORITIZED");
        intent.putExtra("android.app.extra.CALLING_PACKAGE", str);
        intent.putExtra("android.app.extra.PRIORITY", i);
        getContext().sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.HANDLE_CAR_MODE_CHANGES");
    }

    private void notifyCarModeDisabled(int i, String str) {
        Intent intent = new Intent("android.app.action.EXIT_CAR_MODE_PRIORITIZED");
        intent.putExtra("android.app.extra.CALLING_PACKAGE", str);
        intent.putExtra("android.app.extra.PRIORITY", i);
        getContext().sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.HANDLE_CAR_MODE_CHANGES");
    }

    private boolean isCarModeEnabled() {
        return this.mCarModePackagePriority.size() > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDockState(int i) {
        synchronized (this.mLock) {
            if (i != this.mDockState) {
                this.mDockState = i;
                setCarModeLocked(i == 2, 0, 0, "");
                if (this.mSystemReady) {
                    updateLocked(1, 0);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void persistNightMode(int i) {
        if (this.mCarModeEnabled || this.mCar) {
            return;
        }
        Settings.Secure.putIntForUser(getContext().getContentResolver(), "ui_night_mode", this.mNightMode, i);
        Settings.Secure.putLongForUser(getContext().getContentResolver(), "ui_night_mode_custom_type", this.mNightModeCustomType, i);
        Settings.Secure.putLongForUser(getContext().getContentResolver(), "dark_theme_custom_start_time", this.mCustomAutoNightModeStartMilliseconds.toNanoOfDay() / 1000, i);
        Settings.Secure.putLongForUser(getContext().getContentResolver(), "dark_theme_custom_end_time", this.mCustomAutoNightModeEndMilliseconds.toNanoOfDay() / 1000, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void persistNightModeOverrides(int i) {
        if (this.mCarModeEnabled || this.mCar) {
            return;
        }
        Settings.Secure.putIntForUser(getContext().getContentResolver(), "ui_night_mode_override_on", this.mOverrideNightModeOn ? 1 : 0, i);
        Settings.Secure.putIntForUser(getContext().getContentResolver(), "ui_night_mode_override_off", this.mOverrideNightModeOff ? 1 : 0, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfigurationLocked() {
        int i = this.mDefaultUiModeType;
        if (!this.mUiModeLocked) {
            if (this.mTelevision) {
                i = 4;
            } else if (this.mWatch) {
                i = 6;
            } else if (this.mCarModeEnabled) {
                i = 3;
            } else if (isDeskDockState(this.mDockState)) {
                i = 2;
            } else if (this.mVrHeadset) {
                i = 7;
            }
        }
        int i2 = this.mNightMode;
        if (i2 == 2 || i2 == 1) {
            updateComputedNightModeLocked(i2 == 2);
        }
        if (this.mNightMode == 0) {
            boolean z = this.mComputedNightMode;
            TwilightManager twilightManager = this.mTwilightManager;
            if (twilightManager != null) {
                twilightManager.registerListener(this.mTwilightListener, this.mHandler);
                TwilightState lastTwilightState = this.mTwilightManager.getLastTwilightState();
                boolean isNight = lastTwilightState == null ? this.mComputedNightMode : lastTwilightState.isNight();
                if (LOG) {
                    Slog.d(TAG, "updateConfigurationLocked-->lastState: " + lastTwilightState + " mComputedNightMode: " + this.mComputedNightMode + " activateNightMode: " + isNight + " time: " + LocalTime.now().toString());
                }
                z = isNight;
            }
            updateComputedNightModeLocked(z);
        } else {
            TwilightManager twilightManager2 = this.mTwilightManager;
            if (twilightManager2 != null) {
                twilightManager2.unregisterListener(this.mTwilightListener);
            }
        }
        if (this.mNightMode == 3) {
            if (this.mNightModeCustomType == 1) {
                updateComputedNightModeLocked(this.mLastBedtimeRequestedNightMode);
            } else {
                registerTimeChangeEvent();
                boolean computeCustomNightMode = computeCustomNightMode();
                updateComputedNightModeLocked(computeCustomNightMode);
                if (LOG) {
                    Slog.d(TAG, "updateConfigurationLocked-->activate: " + computeCustomNightMode + " time: " + LocalTime.now().toString());
                }
                scheduleNextCustomTimeListener();
            }
        } else {
            unregisterTimeChangeEvent();
        }
        int computedUiModeConfiguration = (!this.mPowerSave || this.mCarModeEnabled || this.mCar) ? getComputedUiModeConfiguration(i) : (i & (-17)) | 32;
        if (LOG) {
            Slog.d(TAG, "updateConfigurationLocked: mDockState=" + this.mDockState + "; mCarMode=" + this.mCarModeEnabled + "; mNightMode=" + this.mNightMode + "; mNightModeCustomType=" + this.mNightModeCustomType + "; uiMode=" + computedUiModeConfiguration);
        }
        this.mCurUiMode = computedUiModeConfiguration;
        if (this.mHoldingConfiguration) {
            return;
        }
        if (!this.mWaitForScreenOff || this.mPowerSave) {
            this.mConfiguration.uiMode = computedUiModeConfiguration;
        }
    }

    private int getComputedUiModeConfiguration(int i) {
        if (this.mUmssExt.darkModeIsSuperSaveMode()) {
            return this.mUmssExt.darkModeGetSuperSaveUiMode(i);
        }
        boolean z = this.mComputedNightMode;
        return (z ? -17 : -33) & (i | (z ? 32 : 16));
    }

    private boolean computeCustomNightMode() {
        return TimeUtils.isTimeBetween(LocalTime.now(), this.mCustomAutoNightModeStartMilliseconds, this.mCustomAutoNightModeEndMilliseconds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyConfigurationExternallyLocked() {
        if (this.mSetUiMode != this.mConfiguration.uiMode) {
            if (LOG) {
                Slog.d(TAG, "change uiMode to-->" + this.mConfiguration.uiMode);
            }
            this.mUmssExt.darkModeNightModeChange(this, this.mConfiguration.uiMode, this.mSetUiMode);
            this.mUmssExt.fontUpdateConfigurationInUIMode(getContext(), this.mConfiguration, -2);
            int i = this.mConfiguration.uiMode;
            this.mSetUiMode = i;
            this.mUmssExt.notifyFlingerUiMode(i);
            this.mWindowManager.clearSnapshotCache();
            this.mUmssExt.darkModeSetValueForState(getContext(), -2, this.mSetUiMode);
            try {
                ActivityTaskManager.getService().updateConfiguration(this.mConfiguration);
            } catch (RemoteException e) {
                Slog.w(TAG, "Failure communicating with activity manager", e);
            } catch (SecurityException e2) {
                Slog.e(TAG, "Activity does not have the ", e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldApplyAutomaticChangesImmediately() {
        return this.mCar || !this.mPowerManager.isInteractive() || this.mNightModeCustomType == 1;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [java.time.ZonedDateTime] */
    private void scheduleNextCustomTimeListener() {
        LocalDateTime dateTimeAfter;
        cancelCustomAlarm();
        LocalDateTime now = LocalDateTime.now();
        if (computeCustomNightMode()) {
            dateTimeAfter = getDateTimeAfter(this.mCustomAutoNightModeEndMilliseconds, now);
        } else {
            dateTimeAfter = getDateTimeAfter(this.mCustomAutoNightModeStartMilliseconds, now);
        }
        long epochMilli = dateTimeAfter.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        AlarmManager alarmManager = this.mAlarmManager;
        String str = TAG;
        alarmManager.setExact(1, epochMilli, str, this.mCustomTimeListener, null);
        if (LOG) {
            Slog.d(str, "next customTime alarm-->" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(epochMilli)));
        }
    }

    private LocalDateTime getDateTimeAfter(LocalTime localTime, LocalDateTime localDateTime) {
        LocalDateTime of = LocalDateTime.of(localDateTime.toLocalDate(), localTime);
        return of.isBefore(localDateTime) ? of.plusDays(1L) : of;
    }

    void updateLocked(int i, int i2) {
        String str;
        int i3 = this.mLastBroadcastState;
        String str2 = null;
        if (i3 == 2) {
            adjustStatusBarCarModeLocked();
            str = UiModeManager.ACTION_EXIT_CAR_MODE;
        } else {
            str = isDeskDockState(i3) ? UiModeManager.ACTION_EXIT_DESK_MODE : null;
        }
        boolean z = false;
        if (this.mCarModeEnabled) {
            if (this.mLastBroadcastState != 2) {
                adjustStatusBarCarModeLocked();
                if (str != null) {
                    sendForegroundBroadcastToAllUsers(str);
                }
                this.mLastBroadcastState = 2;
                str = UiModeManager.ACTION_ENTER_CAR_MODE;
            }
            str = null;
        } else if (isDeskDockState(this.mDockState)) {
            if (!isDeskDockState(this.mLastBroadcastState)) {
                if (str != null) {
                    sendForegroundBroadcastToAllUsers(str);
                }
                this.mLastBroadcastState = this.mDockState;
                str = UiModeManager.ACTION_ENTER_DESK_MODE;
            }
            str = null;
        } else {
            this.mLastBroadcastState = 0;
        }
        if (str != null) {
            if (LOG) {
                Slog.v(TAG, String.format("updateLocked: preparing broadcast: action=%s enable=0x%08x disable=0x%08x", str, Integer.valueOf(i), Integer.valueOf(i2)));
            }
            Intent intent = new Intent(str);
            intent.putExtra("enableFlags", i);
            intent.putExtra("disableFlags", i2);
            intent.addFlags(AudioFormat.EVRC);
            getContext().sendOrderedBroadcastAsUser(intent, UserHandle.CURRENT, null, this.mResultReceiver, null, -1, null, null);
            this.mHoldingConfiguration = true;
            updateConfigurationLocked();
        } else {
            if (this.mCarModeEnabled) {
                if (this.mEnableCarDockLaunch && (i & 1) != 0) {
                    str2 = "android.intent.category.CAR_DOCK";
                }
            } else if (isDeskDockState(this.mDockState)) {
                if ((i & 1) != 0) {
                    str2 = "android.intent.category.DESK_DOCK";
                }
            } else if ((i2 & 1) != 0) {
                str2 = "android.intent.category.HOME";
            }
            if (LOG) {
                Slog.v(TAG, "updateLocked: null action, mDockState=" + this.mDockState + ", category=" + str2);
            }
            sendConfigurationAndStartDreamOrDockAppLocked(str2);
        }
        if (this.mCharging && ((this.mCarModeEnabled && this.mCarModeKeepsScreenOn && (this.mCarModeEnableFlags & 2) == 0) || (this.mCurUiMode == 2 && this.mDeskModeKeepsScreenOn))) {
            z = true;
        }
        if (z != this.mWakeLock.isHeld()) {
            if (z) {
                this.mWakeLock.acquire();
            } else {
                this.mWakeLock.release();
            }
        }
    }

    private void sendForegroundBroadcastToAllUsers(String str) {
        getContext().sendBroadcastAsUser(new Intent(str).addFlags(AudioFormat.EVRC), UserHandle.ALL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAfterBroadcastLocked(String str, int i, int i2) {
        String str2;
        if (UiModeManager.ACTION_ENTER_CAR_MODE.equals(str)) {
            if (this.mEnableCarDockLaunch && (i & 1) != 0) {
                str2 = "android.intent.category.CAR_DOCK";
            }
            str2 = null;
        } else if (UiModeManager.ACTION_ENTER_DESK_MODE.equals(str)) {
            if ((i & 1) != 0) {
                str2 = "android.intent.category.DESK_DOCK";
            }
            str2 = null;
        } else {
            if ((i2 & 1) != 0) {
                str2 = "android.intent.category.HOME";
            }
            str2 = null;
        }
        if (LOG) {
            Slog.v(TAG, String.format("Handling broadcast result for action %s: enable=0x%08x, disable=0x%08x, category=%s", str, Integer.valueOf(i), Integer.valueOf(i2), str2));
        }
        sendConfigurationAndStartDreamOrDockAppLocked(str2);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0098 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:32:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void sendConfigurationAndStartDreamOrDockAppLocked(String str) {
        boolean z;
        Intent intent;
        int startActivityWithConfig;
        this.mHoldingConfiguration = false;
        updateConfigurationLocked();
        if (str != null) {
            Intent buildHomeIntent = buildHomeIntent(str);
            if (Sandman.shouldStartDockApp(getContext(), buildHomeIntent)) {
                try {
                    intent = buildHomeIntent;
                    try {
                        startActivityWithConfig = ActivityTaskManager.getService().startActivityWithConfig((IApplicationThread) null, getContext().getBasePackageName(), getContext().getAttributionTag(), buildHomeIntent, (String) null, (IBinder) null, (String) null, 0, 0, this.mConfiguration, (Bundle) null, -2);
                    } catch (RemoteException e) {
                        e = e;
                        Slog.e(TAG, "Could not start dock app: " + intent, e);
                        z = false;
                        applyConfigurationExternallyLocked();
                        if (this.mDreamsDisabledByAmbientModeSuppression) {
                        }
                        if (str == null) {
                        }
                    }
                } catch (RemoteException e2) {
                    e = e2;
                    intent = buildHomeIntent;
                }
                if (ActivityManager.isStartResultSuccessful(startActivityWithConfig)) {
                    z = true;
                    applyConfigurationExternallyLocked();
                    boolean z2 = !this.mDreamsDisabledByAmbientModeSuppression && this.mLocalPowerManager.isAmbientDisplaySuppressed();
                    if (str == null || z || z2) {
                        return;
                    }
                    if (this.mStartDreamImmediatelyOnDock || this.mWindowManager.isKeyguardShowingAndNotOccluded() || !this.mPowerManager.isInteractive()) {
                        this.mInjector.startDreamWhenDockedIfAppropriate(getContext());
                        return;
                    }
                    return;
                }
                if (startActivityWithConfig != -91) {
                    Slog.e(TAG, "Could not start dock app: " + intent + ", startActivityWithConfig result " + startActivityWithConfig);
                }
            }
        }
        z = false;
        applyConfigurationExternallyLocked();
        if (this.mDreamsDisabledByAmbientModeSuppression) {
        }
        if (str == null) {
        }
    }

    private void adjustStatusBarCarModeLocked() {
        Context context = getContext();
        if (this.mStatusBarManager == null) {
            this.mStatusBarManager = (StatusBarManager) context.getSystemService("statusbar");
        }
        StatusBarManager statusBarManager = this.mStatusBarManager;
        if (statusBarManager != null) {
            statusBarManager.disable(this.mCarModeEnabled ? 524288 : 0);
        }
        if (this.mNotificationManager == null) {
            this.mNotificationManager = (NotificationManager) context.getSystemService("notification");
        }
        NotificationManager notificationManager = this.mNotificationManager;
        if (notificationManager != null) {
            if (this.mCarModeEnabled) {
                this.mNotificationManager.notifyAsUser(null, 10, new Notification.Builder(context, SystemNotificationChannels.CAR_MODE).setSmallIcon(R.drawable.sym_keyboard_feedback_delete).setDefaults(4).setOngoing(true).setWhen(0L).setColor(context.getColor(R.color.system_notification_accent_color)).setContentTitle(context.getString(R.string.config_batterymeterPowersavePath)).setContentText(context.getString(R.string.config_batterymeterPerimeterPath)).setContentIntent(PendingIntent.getActivityAsUser(context, 0, new Intent(context, (Class<?>) DisableCarModeActivity.class), 33554432, null, UserHandle.CURRENT)).build(), UserHandle.ALL);
            } else {
                notificationManager.cancelAsUser(null, 10, UserHandle.ALL);
            }
        }
    }

    private void updateComputedNightModeLocked(boolean z) {
        TwilightManager twilightManager;
        boolean darkModeGetAutoFirst = this.mUmssExt.darkModeGetAutoFirst();
        this.mComputedNightMode = z;
        int i = this.mNightMode;
        if (i == 2 || i == 1) {
            return;
        }
        if (this.mOverrideNightModeOn && !z) {
            this.mComputedNightMode = true;
            return;
        }
        if (this.mOverrideNightModeOff && z) {
            this.mComputedNightMode = false;
            return;
        }
        this.mComputedNightMode = this.mUmssExt.darkModeOverrideComputedNightMode(i, darkModeGetAutoFirst, z);
        if (this.mNightMode == 0 && ((twilightManager = this.mTwilightManager) == null || twilightManager.getLastTwilightState() == null)) {
            return;
        }
        resetNightModeOverrideLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean resetNightModeOverrideLocked() {
        if (!this.mOverrideNightModeOff && !this.mOverrideNightModeOn) {
            return false;
        }
        this.mOverrideNightModeOff = false;
        this.mOverrideNightModeOn = false;
        persistNightModeOverrides(this.mOverrideNightModeUser);
        this.mOverrideNightModeUser = 0;
        return true;
    }

    private void registerVrStateListener() {
        IVrManager asInterface = IVrManager.Stub.asInterface(ServiceManager.getService("vrmanager"));
        if (asInterface != null) {
            try {
                asInterface.registerListener(this.mVrStateCallbacks);
            } catch (RemoteException e) {
                Slog.e(TAG, "Failed to register VR mode state listener: " + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Shell extends ShellCommand {
        public static final String NIGHT_MODE_STR_AUTO = "auto";
        public static final String NIGHT_MODE_STR_CUSTOM_BEDTIME = "custom_bedtime";
        public static final String NIGHT_MODE_STR_CUSTOM_SCHEDULE = "custom_schedule";
        public static final String NIGHT_MODE_STR_NO = "no";
        public static final String NIGHT_MODE_STR_UNKNOWN = "unknown";
        public static final String NIGHT_MODE_STR_YES = "yes";
        private final IUiModeManager mInterface;

        /* JADX INFO: Access modifiers changed from: private */
        public static String nightModeToStr(int i, int i2) {
            return i != 0 ? i != 1 ? i != 2 ? i != 3 ? "unknown" : i2 == 0 ? NIGHT_MODE_STR_CUSTOM_SCHEDULE : i2 == 1 ? NIGHT_MODE_STR_CUSTOM_BEDTIME : "unknown" : NIGHT_MODE_STR_YES : NIGHT_MODE_STR_NO : NIGHT_MODE_STR_AUTO;
        }

        Shell(IUiModeManager iUiModeManager) {
            this.mInterface = iUiModeManager;
        }

        public void onHelp() {
            PrintWriter outPrintWriter = getOutPrintWriter();
            outPrintWriter.println("UiModeManager service (uimode) commands:");
            outPrintWriter.println("  help");
            outPrintWriter.println("    Print this help text.");
            outPrintWriter.println("  night [yes|no|auto|custom_schedule|custom_bedtime]");
            outPrintWriter.println("    Set or read night mode.");
            outPrintWriter.println("  car [yes|no]");
            outPrintWriter.println("    Set or read car mode.");
            outPrintWriter.println("  time [start|end] <ISO time>");
            outPrintWriter.println("    Set custom start/end schedule time (night mode must be set to custom to apply).");
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x0041  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0054 A[Catch: RemoteException -> 0x0059, TRY_LEAVE, TryCatch #0 {RemoteException -> 0x0059, blocks: (B:7:0x0008, B:18:0x0045, B:20:0x004a, B:22:0x004f, B:24:0x0054, B:26:0x001e, B:29:0x0029, B:32:0x0034), top: B:6:0x0008 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public int onCommand(String str) {
            char c;
            if (str == null) {
                return handleDefaultCommands(str);
            }
            try {
                int hashCode = str.hashCode();
                if (hashCode == 98260) {
                    if (str.equals("car")) {
                        c = 1;
                        if (c != 0) {
                        }
                    }
                    c = 65535;
                    if (c != 0) {
                    }
                } else if (hashCode != 3560141) {
                    if (hashCode == 104817688 && str.equals("night")) {
                        c = 0;
                        if (c != 0) {
                            return handleNightMode();
                        }
                        if (c == 1) {
                            return handleCarMode();
                        }
                        if (c == 2) {
                            return handleCustomTime();
                        }
                        return handleDefaultCommands(str);
                    }
                    c = 65535;
                    if (c != 0) {
                    }
                } else {
                    if (str.equals("time")) {
                        c = 2;
                        if (c != 0) {
                        }
                    }
                    c = 65535;
                    if (c != 0) {
                    }
                }
            } catch (RemoteException e) {
                getErrPrintWriter().println("Remote exception: " + e);
                return -1;
            }
        }

        private int handleCustomTime() throws RemoteException {
            String nextArg = getNextArg();
            if (nextArg == null) {
                printCustomTime();
                return 0;
            }
            if (nextArg.equals("end")) {
                this.mInterface.setCustomNightModeEnd(UiModeManagerService.toMilliSeconds(LocalTime.parse(getNextArg())));
                return 0;
            }
            if (nextArg.equals("start")) {
                this.mInterface.setCustomNightModeStart(UiModeManagerService.toMilliSeconds(LocalTime.parse(getNextArg())));
                return 0;
            }
            getErrPrintWriter().println("command must be in [start|end]");
            return -1;
        }

        private void printCustomTime() throws RemoteException {
            getOutPrintWriter().println("start " + UiModeManagerService.fromMilliseconds(this.mInterface.getCustomNightModeStart()).toString());
            getOutPrintWriter().println("end " + UiModeManagerService.fromMilliseconds(this.mInterface.getCustomNightModeEnd()).toString());
        }

        private int handleNightMode() throws RemoteException {
            PrintWriter errPrintWriter = getErrPrintWriter();
            String nextArg = getNextArg();
            if (nextArg == null) {
                printCurrentNightMode();
                return 0;
            }
            int strToNightMode = strToNightMode(nextArg);
            int strToNightModeCustomType = strToNightModeCustomType(nextArg);
            if (strToNightMode >= 0) {
                this.mInterface.setNightMode(strToNightMode);
                if (strToNightMode == 3) {
                    this.mInterface.setNightModeCustomType(strToNightModeCustomType);
                }
                printCurrentNightMode();
                return 0;
            }
            errPrintWriter.println("Error: mode must be 'yes', 'no', or 'auto', or 'custom_schedule', or 'custom_bedtime'");
            return -1;
        }

        private void printCurrentNightMode() throws RemoteException {
            getOutPrintWriter().println("Night mode: " + nightModeToStr(this.mInterface.getNightMode(), this.mInterface.getNightModeCustomType()));
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        private static int strToNightMode(String str) {
            char c;
            str.hashCode();
            switch (str.hashCode()) {
                case -757868544:
                    if (str.equals(NIGHT_MODE_STR_CUSTOM_BEDTIME)) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 3521:
                    if (str.equals(NIGHT_MODE_STR_NO)) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 119527:
                    if (str.equals(NIGHT_MODE_STR_YES)) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 3005871:
                    if (str.equals(NIGHT_MODE_STR_AUTO)) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 164399013:
                    if (str.equals(NIGHT_MODE_STR_CUSTOM_SCHEDULE)) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                case 4:
                    return 3;
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return 0;
                default:
                    return -1;
            }
        }

        private static int strToNightModeCustomType(String str) {
            str.hashCode();
            if (str.equals(NIGHT_MODE_STR_CUSTOM_BEDTIME)) {
                return 1;
            }
            return !str.equals(NIGHT_MODE_STR_CUSTOM_SCHEDULE) ? -1 : 0;
        }

        private int handleCarMode() throws RemoteException {
            PrintWriter errPrintWriter = getErrPrintWriter();
            String nextArg = getNextArg();
            if (nextArg == null) {
                printCurrentCarMode();
                return 0;
            }
            if (nextArg.equals(NIGHT_MODE_STR_YES)) {
                this.mInterface.enableCarMode(0, 0, "");
                printCurrentCarMode();
                return 0;
            }
            if (nextArg.equals(NIGHT_MODE_STR_NO)) {
                this.mInterface.disableCarMode(0);
                printCurrentCarMode();
                return 0;
            }
            errPrintWriter.println("Error: mode must be 'yes', or 'no'");
            return -1;
        }

        private void printCurrentCarMode() throws RemoteException {
            PrintWriter outPrintWriter = getOutPrintWriter();
            int currentModeType = this.mInterface.getCurrentModeType();
            StringBuilder sb = new StringBuilder();
            sb.append("Car mode: ");
            sb.append(currentModeType == 3 ? NIGHT_MODE_STR_YES : NIGHT_MODE_STR_NO);
            outPrintWriter.println(sb.toString());
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class LocalService extends UiModeManagerInternal {
        public LocalService() {
        }

        @Override // com.android.server.UiModeManagerInternal
        public boolean isNightMode() {
            boolean z;
            synchronized (UiModeManagerService.this.mLock) {
                z = (UiModeManagerService.this.mConfiguration.uiMode & 32) != 0;
                if (UiModeManagerService.LOG) {
                    Slog.d(UiModeManagerService.TAG, "LocalService.isNightMode(): mNightMode=" + UiModeManagerService.this.mNightMode + "; mComputedNightMode=" + UiModeManagerService.this.mComputedNightMode + "; uiMode=" + UiModeManagerService.this.mConfiguration.uiMode + "; isIt=" + z);
                }
            }
            return z;
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector {
        public int getCallingUid() {
            return Binder.getCallingUid();
        }

        public void startDreamWhenDockedIfAppropriate(Context context) {
            Sandman.startDreamWhenDockedIfAppropriate(context);
        }
    }

    public IUiModeManagerServiceWrapper getWrapper() {
        return this.mUiModemsWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class UiModeManagerServiceWrapper implements IUiModeManagerServiceWrapper {
        private UiModeManagerServiceWrapper() {
        }

        @Override // com.android.server.IUiModeManagerServiceWrapper
        public void unregisterScreenOffEvent() {
            UiModeManagerService.this.unregisterScreenOffEventLocked();
        }
    }
}
