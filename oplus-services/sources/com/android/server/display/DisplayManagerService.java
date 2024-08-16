package com.android.server.display;

import android.R;
import android.annotation.RequiresPermission;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.AppOpsManager;
import android.app.compat.CompatChanges;
import android.companion.virtual.IVirtualDevice;
import android.companion.virtual.VirtualDeviceManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ParceledListSlice;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.graphics.ColorSpace;
import android.graphics.Point;
import android.hardware.OverlayProperties;
import android.hardware.SensorManager;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.devicestate.DeviceStateManagerInternal;
import android.hardware.display.AmbientBrightnessDayStats;
import android.hardware.display.BrightnessChangeEvent;
import android.hardware.display.BrightnessConfiguration;
import android.hardware.display.BrightnessInfo;
import android.hardware.display.Curve;
import android.hardware.display.DisplayManagerGlobal;
import android.hardware.display.DisplayManagerInternal;
import android.hardware.display.DisplayViewport;
import android.hardware.display.DisplayedContentSample;
import android.hardware.display.DisplayedContentSamplingAttributes;
import android.hardware.display.HdrConversionMode;
import android.hardware.display.IDisplayManager;
import android.hardware.display.IDisplayManagerCallback;
import android.hardware.display.IVirtualDisplayCallback;
import android.hardware.display.VirtualDisplayConfig;
import android.hardware.display.WifiDisplayStatus;
import android.hardware.graphics.common.DisplayDecorationSupport;
import android.hardware.input.HostUsiVersion;
import android.media.projection.IMediaProjection;
import android.media.projection.IMediaProjectionManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ShellCallback;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.EventLog;
import android.util.IntArray;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.Spline;
import android.view.ContentRecordingSession;
import android.view.Display;
import android.view.DisplayEventReceiver;
import android.view.DisplayInfo;
import android.view.Surface;
import android.view.SurfaceControl;
import android.window.DisplayWindowPolicyController;
import android.window.ScreenCapture;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.display.BrightnessSynchronizer;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.AnimationThread;
import com.android.server.DisplayThread;
import com.android.server.LocalServices;
import com.android.server.OplusServiceFactory;
import com.android.server.SystemService;
import com.android.server.UiThread;
import com.android.server.companion.virtual.VirtualDeviceManagerInternal;
import com.android.server.display.DisplayAdapter;
import com.android.server.display.DisplayDeviceConfig;
import com.android.server.display.DisplayManagerService;
import com.android.server.display.LogicalDisplayMapper;
import com.android.server.display.mode.DisplayModeDirector;
import com.android.server.display.utils.SensorUtils;
import com.android.server.input.InputManagerInternal;
import com.android.server.policy.DeviceStateProviderImpl;
import com.android.server.wm.SurfaceAnimationThread;
import com.android.server.wm.WindowManagerInternal;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DisplayManagerService extends SystemService {
    static final long DISPLAY_MODE_RETURNS_PHYSICAL_REFRESH_RATE = 170503758;
    private static final String FORCE_WIFI_DISPLAY_ENABLE = "persist.debug.wfd.enable";
    private static final int MSG_DELIVER_DISPLAY_EVENT = 3;
    private static final int MSG_DELIVER_DISPLAY_EVENT_FRAME_RATE_OVERRIDE = 7;
    private static final int MSG_DELIVER_DISPLAY_GROUP_EVENT = 8;
    private static final int MSG_LOAD_BRIGHTNESS_CONFIGURATIONS = 6;
    private static final int MSG_RECEIVED_DEVICE_STATE = 9;
    private static final int MSG_REGISTER_ADDITIONAL_DISPLAY_ADAPTERS = 2;
    private static final int MSG_REGISTER_DEFAULT_DISPLAY_ADAPTERS = 1;
    private static final int MSG_REQUEST_TRAVERSAL = 4;
    private static final int MSG_UPDATE_VIEWPORT = 5;
    private static final String PROP_DEFAULT_DISPLAY_TOP_INSET = "persist.sys.displayinset.top";
    private static final String TAG = "DisplayManagerService";
    private static final float THRESHOLD_FOR_REFRESH_RATES_DIVISORS = 9.0E-4f;
    private static final long WAIT_FOR_DEFAULT_DISPLAY_TIMEOUT = 10000;
    private ActivityManager mActivityManager;
    private ActivityManagerInternal mActivityManagerInternal;

    @GuardedBy({"mSyncRoot"})
    private boolean mAreUserDisabledHdrTypesAllowed;
    private boolean mBootCompleted;
    private final BrightnessSynchronizer mBrightnessSynchronizer;
    private BrightnessTracker mBrightnessTracker;
    public final SparseArray<CallbackRecord> mCallbacks;
    private final Context mContext;
    private int mCurrentUserId;
    public IOplusDisplayManagerServiceEx mDMSEx;
    private boolean mDebugBrightness;
    private final int mDefaultDisplayDefaultColorMode;
    private int mDefaultDisplayTopInset;
    private DeviceStateManagerInternal mDeviceStateManager;
    private final SparseArray<IntArray> mDisplayAccessUIDs;
    private final ArrayList<DisplayAdapter> mDisplayAdapters;
    private final DisplayBlanker mDisplayBlanker;

    @GuardedBy({"mSyncRoot"})
    private final SparseArray<BrightnessPair> mDisplayBrightnesses;
    private final DisplayDeviceRepository mDisplayDeviceRepo;
    private final CopyOnWriteArrayList<DisplayManagerInternal.DisplayGroupListener> mDisplayGroupListeners;
    private final DisplayModeDirector mDisplayModeDirector;
    private DisplayManagerInternal.DisplayPowerCallbacks mDisplayPowerCallbacks;
    private final SparseArray<DisplayPowerController> mDisplayPowerControllers;

    @GuardedBy({"mSyncRoot"})
    private final SparseIntArray mDisplayStates;
    private final CopyOnWriteArrayList<DisplayManagerInternal.DisplayTransactionListener> mDisplayTransactionListeners;
    final SparseArray<Pair<IVirtualDevice, DisplayWindowPolicyController>> mDisplayWindowPolicyControllers;
    public IDisplayManagerServiceExt mDmsExt;
    private DisplayManagerServiceWrapper mDmsWrapper;

    @GuardedBy({"mSyncDump"})
    private boolean mDumpInProgress;
    private final DisplayManagerHandler mHandler;

    @GuardedBy({"mSyncRoot"})
    private HdrConversionMode mHdrConversionMode;
    private final HighBrightnessModeMetadataMapper mHighBrightnessModeMetadataMapper;
    private final BroadcastReceiver mIdleModeReceiver;
    private final Injector mInjector;
    private InputManagerInternal mInputManagerInternal;
    private boolean mIsDocked;
    private boolean mIsDreaming;
    private volatile boolean mIsHdrOutputControlEnabled;
    private final LogicalDisplayMapper mLogicalDisplayMapper;

    @GuardedBy({"mSyncRoot"})
    private boolean mMinimalPostProcessingAllowed;
    private final Curve mMinimumBrightnessCurve;
    private final Spline mMinimumBrightnessSpline;
    private final OverlayProperties mOverlayProperties;
    private HdrConversionMode mOverrideHdrConversionMode;

    @GuardedBy({"mPendingCallbackSelfLocked"})
    public final SparseArray<SparseArray<PendingCallback>> mPendingCallbackSelfLocked;
    private boolean mPendingTraversal;
    private boolean mPendingTraversalCompleted;
    private final PersistentDataStore mPersistentDataStore;
    private Handler mPowerHandler;
    private IMediaProjectionManager mProjectionService;
    public boolean mSafeMode;
    private SensorManager mSensorManager;
    private SettingsObserver mSettingsObserver;
    private Point mStableDisplaySize;
    private int[] mSupportedHdrOutputType;
    private final SyncRoot mSyncDump;
    private final SyncRoot mSyncRoot;

    @GuardedBy({"mSyncRoot"})
    private int mSystemPreferredHdrOutputType;
    private boolean mSystemReady;
    private final ArrayList<CallbackRecord> mTempCallbacks;
    private final ArrayList<DisplayViewport> mTempViewports;
    private final Handler mUiHandler;
    private UidImportanceListener mUidImportanceListener;

    @GuardedBy({"mSyncRoot"})
    private int[] mUserDisabledHdrTypes;
    private Display.Mode mUserPreferredMode;

    @GuardedBy({"mSyncRoot"})
    private final ArrayList<DisplayViewport> mViewports;
    private VirtualDisplayAdapter mVirtualDisplayAdapter;
    private final ColorSpace mWideColorSpace;
    private WifiDisplayAdapter mWifiDisplayAdapter;
    private int mWifiDisplayScanRequestCount;
    private WindowManagerInternal mWindowManagerInternal;
    private float requestedMaxRefreshRate;
    private static final boolean PANIC_DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static boolean DEBUG = true;
    private static final int[] EMPTY_ARRAY = new int[0];
    private static final HdrConversionMode HDR_CONVERSION_MODE_UNSUPPORTED = new HdrConversionMode(0);

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Clock {
        long uptimeMillis();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class SyncRoot {
    }

    public DisplayManagerService(Context context) {
        this(context, new Injector());
    }

    @VisibleForTesting
    DisplayManagerService(Context context, Injector injector) {
        super(context);
        this.mUidImportanceListener = new UidImportanceListener();
        this.mUserDisabledHdrTypes = new int[0];
        this.mAreUserDisabledHdrTypesAllowed = true;
        this.mHdrConversionMode = null;
        this.mOverrideHdrConversionMode = null;
        this.mSystemPreferredHdrOutputType = -1;
        SyncRoot syncRoot = new SyncRoot();
        this.mSyncRoot = syncRoot;
        this.mCallbacks = new SparseArray<>();
        this.mDisplayWindowPolicyControllers = new SparseArray<>();
        this.mHighBrightnessModeMetadataMapper = new HighBrightnessModeMetadataMapper();
        this.mDisplayAdapters = new ArrayList<>();
        this.mDisplayTransactionListeners = new CopyOnWriteArrayList<>();
        this.mDisplayGroupListeners = new CopyOnWriteArrayList<>();
        this.mDisplayPowerControllers = new SparseArray<>();
        this.mDisplayBlanker = new DisplayBlanker() { // from class: com.android.server.display.DisplayManagerService.1
            @Override // com.android.server.display.DisplayBlanker
            public synchronized void requestDisplayState(int i, int i2, float f, float f2) {
                boolean z;
                boolean z2;
                boolean z3;
                synchronized (DisplayManagerService.this.mSyncRoot) {
                    int indexOfKey = DisplayManagerService.this.mDisplayStates.indexOfKey(i);
                    if (indexOfKey > -1) {
                        boolean z4 = i2 != DisplayManagerService.this.mDisplayStates.valueAt(indexOfKey);
                        if (z4) {
                            int size = DisplayManagerService.this.mDisplayStates.size();
                            z = true;
                            z2 = true;
                            int i3 = 0;
                            while (i3 < size) {
                                int valueAt = i3 == indexOfKey ? i2 : DisplayManagerService.this.mDisplayStates.valueAt(i3);
                                if (valueAt != 1) {
                                    DisplayManagerService displayManagerService = DisplayManagerService.this;
                                    z2 = displayManagerService.mDmsExt.onDisplayStateChange(i2, valueAt, displayManagerService.mDisplayStates.keyAt(i3), DisplayManagerService.this.mLogicalDisplayMapper);
                                }
                                if (Display.isActiveState(valueAt)) {
                                    z = false;
                                }
                                if (!z2 && !z) {
                                    break;
                                } else {
                                    i3++;
                                }
                            }
                        } else {
                            z = true;
                            z2 = true;
                        }
                        z3 = z4;
                    } else {
                        z = true;
                        z2 = true;
                        z3 = false;
                    }
                }
                if (i2 == 1) {
                    DisplayManagerService.this.requestDisplayStateInternal(i, i2, f, f2);
                }
                if (z3) {
                    DisplayManagerService.this.mDisplayPowerCallbacks.onDisplayStateChange(z, z2);
                }
                if (i2 != 1) {
                    DisplayManagerService.this.requestDisplayStateInternal(i, i2, f, f2);
                }
            }
        };
        this.mDisplayStates = new SparseIntArray();
        this.mDisplayBrightnesses = new SparseArray<>();
        this.mStableDisplaySize = new Point();
        this.mViewports = new ArrayList<>();
        PersistentDataStore persistentDataStore = new PersistentDataStore();
        this.mPersistentDataStore = persistentDataStore;
        this.mTempCallbacks = new ArrayList<>();
        this.mPendingCallbackSelfLocked = new SparseArray<>();
        this.mTempViewports = new ArrayList<>();
        this.mDisplayAccessUIDs = new SparseArray<>();
        this.mBootCompleted = false;
        this.mDebugBrightness = false;
        this.mIdleModeReceiver = new BroadcastReceiver() { // from class: com.android.server.display.DisplayManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("android.intent.action.DOCK_EVENT".equals(intent.getAction())) {
                    int intExtra = intent.getIntExtra("android.intent.extra.DOCK_STATE", 0);
                    DisplayManagerService.this.mIsDocked = intExtra == 1 || intExtra == 3 || intExtra == 4;
                }
                if ("android.intent.action.DREAMING_STARTED".equals(intent.getAction())) {
                    DisplayManagerService.this.mIsDreaming = true;
                } else if ("android.intent.action.DREAMING_STOPPED".equals(intent.getAction())) {
                    DisplayManagerService.this.mIsDreaming = false;
                }
                DisplayManagerService displayManagerService = DisplayManagerService.this;
                displayManagerService.setDockedAndIdleEnabled(displayManagerService.mIsDocked && DisplayManagerService.this.mIsDreaming, 0);
            }
        };
        this.mSyncDump = new SyncRoot();
        this.mDmsExt = (IDisplayManagerServiceExt) ExtLoader.type(IDisplayManagerServiceExt.class).base(this).create();
        this.mDMSEx = null;
        this.mPendingTraversalCompleted = true;
        this.mDmsWrapper = new DisplayManagerServiceWrapper();
        this.mInjector = injector;
        this.mContext = context;
        DisplayManagerHandler displayManagerHandler = new DisplayManagerHandler(DisplayThread.get().getLooper());
        this.mHandler = displayManagerHandler;
        Handler handler = UiThread.getHandler();
        this.mUiHandler = handler;
        DisplayDeviceRepository displayDeviceRepository = new DisplayDeviceRepository(syncRoot, persistentDataStore);
        this.mDisplayDeviceRepo = displayDeviceRepository;
        LogicalDisplayMapper logicalDisplayMapper = new LogicalDisplayMapper(context, displayDeviceRepository, new LogicalDisplayListener(), syncRoot, displayManagerHandler);
        this.mLogicalDisplayMapper = logicalDisplayMapper;
        this.mDisplayModeDirector = new DisplayModeDirector(context, displayManagerHandler);
        this.mBrightnessSynchronizer = new BrightnessSynchronizer(context);
        Resources resources = context.getResources();
        this.mDefaultDisplayDefaultColorMode = context.getResources().getInteger(R.integer.config_displayWhiteBalanceColorTemperatureFilterHorizon);
        this.mDefaultDisplayTopInset = SystemProperties.getInt(PROP_DEFAULT_DISPLAY_TOP_INSET, -1);
        float[] floatArray = getFloatArray(resources.obtainTypedArray(R.array.disallowed_apps_managed_device));
        float[] floatArray2 = getFloatArray(resources.obtainTypedArray(R.array.disallowed_apps_managed_profile));
        this.mMinimumBrightnessCurve = new Curve(floatArray, floatArray2);
        this.mMinimumBrightnessSpline = Spline.createSpline(floatArray, floatArray2);
        this.mCurrentUserId = 0;
        this.mWideColorSpace = SurfaceControl.getCompositionColorSpaces()[1];
        this.mOverlayProperties = SurfaceControl.getOverlaySupport();
        this.mSystemReady = false;
        this.mDumpInProgress = false;
        this.mDmsExt.init(context);
        this.mDmsExt.setUiHandler(handler);
        this.mDmsExt.setLogicalDisplayMapper(logicalDisplayMapper);
        this.mDMSEx = (IOplusDisplayManagerServiceEx) OplusServiceFactory.getInstance().getFeature(IOplusDisplayManagerServiceEx.DEFAULT, new Object[]{getContext(), this});
    }

    public void setupSchedulerPolicies() {
        Process.setThreadGroupAndCpuset(DisplayThread.get().getThreadId(), 5);
        Process.setThreadGroupAndCpuset(AnimationThread.get().getThreadId(), 5);
        Process.setThreadGroupAndCpuset(SurfaceAnimationThread.get().getThreadId(), 5);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        synchronized (this.mSyncRoot) {
            this.mPersistentDataStore.loadIfNeeded();
            loadStableDisplayValuesLocked();
        }
        this.mHandler.sendEmptyMessage(1);
        DisplayManagerGlobal.invalidateLocalDisplayInfoCaches();
        IDisplayManager.Stub binderService = new BinderService();
        this.mDmsExt.onStart(binderService);
        publishBinderService("display", binderService, true);
        publishLocalService(DisplayManagerInternal.class, new LocalService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        if (i == 100) {
            synchronized (this.mSyncRoot) {
                long uptimeMillis = SystemClock.uptimeMillis() + this.mInjector.getDefaultDisplayDelayTimeout();
                while (true) {
                    if (this.mLogicalDisplayMapper.getDisplayLocked(0) != null && this.mVirtualDisplayAdapter != null) {
                    }
                    long uptimeMillis2 = uptimeMillis - SystemClock.uptimeMillis();
                    if (uptimeMillis2 <= 0) {
                        throw new RuntimeException("Timeout waiting for default display to be initialized. DefaultDisplay=" + this.mLogicalDisplayMapper.getDisplayLocked(0) + ", mVirtualDisplayAdapter=" + this.mVirtualDisplayAdapter);
                    }
                    if (DEBUG) {
                        Slog.d(TAG, "waitForDefaultDisplay: waiting, timeout=" + uptimeMillis2);
                    }
                    try {
                        this.mSyncRoot.wait(uptimeMillis2);
                    } catch (InterruptedException unused) {
                    }
                }
            }
            break;
        }
        if (i == 1000) {
            synchronized (this.mSyncRoot) {
                this.mBootCompleted = true;
                for (int i2 = 0; i2 < this.mDisplayPowerControllers.size(); i2++) {
                    this.mDisplayPowerControllers.valueAt(i2).onBootCompleted();
                }
            }
            this.mDisplayModeDirector.onBootCompleted();
            this.mLogicalDisplayMapper.onBootCompleted();
        }
        this.mDmsExt.onBootComplete(i, this.mDisplayPowerControllers.get(0), this.mSyncRoot);
        DeviceStateProviderImpl.sExtImpl.onBootPhase(i);
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2) {
        final int userIdentifier = targetUser2.getUserIdentifier();
        final int userSerialNumber = getUserManager().getUserSerialNumber(userIdentifier);
        synchronized (this.mSyncRoot) {
            final boolean z = this.mCurrentUserId != userIdentifier;
            if (z) {
                this.mCurrentUserId = userIdentifier;
            }
            this.mLogicalDisplayMapper.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DisplayManagerService.this.lambda$onUserSwitching$0(z, userSerialNumber, userIdentifier, (LogicalDisplay) obj);
                }
            });
            handleSettingsChange();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserSwitching$0(boolean z, int i, int i2, LogicalDisplay logicalDisplay) {
        DisplayPowerController displayPowerController;
        if (logicalDisplay.getDisplayInfoLocked().type == 1 && (displayPowerController = this.mDisplayPowerControllers.get(logicalDisplay.getDisplayIdLocked())) != null) {
            if (z) {
                getBrightnessConfigForDisplayWithPdsFallbackLocked(logicalDisplay.getPrimaryDisplayDeviceLocked().getUniqueId(), i);
                Slog.w(TAG, "disconnect WFD when switch user by feature");
                disconnectWifiDisplayInternal();
            }
            displayPowerController.onSwitchUser(i2);
        }
    }

    @SuppressLint({"AndroidFrameworkRequiresPermission"})
    public void windowManagerAndInputReady() {
        synchronized (this.mSyncRoot) {
            this.mWindowManagerInternal = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
            this.mInputManagerInternal = (InputManagerInternal) LocalServices.getService(InputManagerInternal.class);
            this.mActivityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
            ActivityManager activityManager = (ActivityManager) this.mContext.getSystemService(ActivityManager.class);
            this.mActivityManager = activityManager;
            activityManager.addOnUidImportanceListener(this.mUidImportanceListener, 400);
            this.mDeviceStateManager = (DeviceStateManagerInternal) LocalServices.getService(DeviceStateManagerInternal.class);
            ((DeviceStateManager) this.mContext.getSystemService(DeviceStateManager.class)).registerCallback(new HandlerExecutor(this.mHandler), new DeviceStateListener());
            scheduleTraversalLocked(false);
        }
    }

    public void systemReady(boolean z) {
        synchronized (this.mSyncRoot) {
            this.mSafeMode = z;
            this.mSystemReady = true;
            this.mIsHdrOutputControlEnabled = isDeviceConfigHdrOutputControlEnabled();
            DeviceConfig.addOnPropertiesChangedListener("display_manager", BackgroundThread.getExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda8
                public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                    DisplayManagerService.this.lambda$systemReady$1(properties);
                }
            });
            recordTopInsetLocked(this.mLogicalDisplayMapper.getDisplayLocked(0));
            updateSettingsLocked();
            updateUserDisabledHdrTypesFromSettingsLocked();
            updateUserPreferredDisplayModeSettingsLocked();
            if (this.mIsHdrOutputControlEnabled) {
                updateHdrConversionModeSettingsLocked();
            }
        }
        this.mDisplayModeDirector.setDesiredDisplayModeSpecsListener(new DesiredDisplayModeSpecsObserver());
        this.mDisplayModeDirector.start(this.mSensorManager);
        this.mHandler.sendEmptyMessage(2);
        this.mSettingsObserver = new SettingsObserver();
        this.mBrightnessSynchronizer.startSynchronizing();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.DREAMING_STARTED");
        intentFilter.addAction("android.intent.action.DREAMING_STOPPED");
        intentFilter.addAction("android.intent.action.DOCK_EVENT");
        this.mContext.registerReceiver(this.mIdleModeReceiver, intentFilter);
        this.mDmsExt.onSystemReady();
        this.mDMSEx.systemReady();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$1(DeviceConfig.Properties properties) {
        this.mIsHdrOutputControlEnabled = isDeviceConfigHdrOutputControlEnabled();
    }

    private boolean isDeviceConfigHdrOutputControlEnabled() {
        return DeviceConfig.getBoolean("display_manager", "enable_hdr_output_control", true);
    }

    @VisibleForTesting
    Handler getDisplayHandler() {
        return this.mHandler;
    }

    @VisibleForTesting
    DisplayDeviceRepository getDisplayDeviceRepository() {
        return this.mDisplayDeviceRepo;
    }

    @VisibleForTesting
    boolean isMinimalPostProcessingAllowed() {
        boolean z;
        synchronized (this.mSyncRoot) {
            z = this.mMinimalPostProcessingAllowed;
        }
        return z;
    }

    @VisibleForTesting
    void setMinimalPostProcessingAllowed(boolean z) {
        synchronized (this.mSyncRoot) {
            this.mMinimalPostProcessingAllowed = z;
        }
    }

    private void loadStableDisplayValuesLocked() {
        int i;
        Point stableDisplaySize = this.mPersistentDataStore.getStableDisplaySize();
        int i2 = stableDisplaySize.x;
        if (i2 > 0 && (i = stableDisplaySize.y) > 0) {
            this.mStableDisplaySize.set(i2, i);
            return;
        }
        Resources resources = this.mContext.getResources();
        int integer = resources.getInteger(R.integer.leanback_setup_alpha_backward_out_content_delay);
        int integer2 = resources.getInteger(R.integer.leanback_setup_alpha_backward_in_content_duration);
        if (integer <= 0 || integer2 <= 0) {
            return;
        }
        setStableDisplaySizeLocked(integer, integer2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Point getStableDisplaySizeInternal() {
        int i;
        Point point = new Point();
        synchronized (this.mSyncRoot) {
            Point point2 = this.mStableDisplaySize;
            int i2 = point2.x;
            if (i2 > 0 && (i = point2.y) > 0) {
                point.set(i2, i);
            }
        }
        return point;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerDisplayTransactionListenerInternal(DisplayManagerInternal.DisplayTransactionListener displayTransactionListener) {
        this.mDisplayTransactionListeners.add(displayTransactionListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterDisplayTransactionListenerInternal(DisplayManagerInternal.DisplayTransactionListener displayTransactionListener) {
        this.mDisplayTransactionListeners.remove(displayTransactionListener);
    }

    @VisibleForTesting
    void setDisplayInfoOverrideFromWindowManagerInternal(int i, DisplayInfo displayInfo) {
        synchronized (this.mSyncRoot) {
            LogicalDisplay displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(i);
            if (displayLocked != null && displayLocked.setDisplayInfoOverrideFromWindowManagerLocked(displayInfo)) {
                handleLogicalDisplayChangedLocked(displayLocked);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getNonOverrideDisplayInfoInternal(int i, DisplayInfo displayInfo) {
        synchronized (this.mSyncRoot) {
            LogicalDisplay displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(i);
            if (displayLocked != null) {
                displayLocked.getNonOverrideDisplayInfoLocked(displayInfo);
            }
        }
    }

    @VisibleForTesting
    void performTraversalInternal(SurfaceControl.Transaction transaction) {
        synchronized (this.mSyncRoot) {
            if (this.mPendingTraversal || !this.mPendingTraversalCompleted) {
                this.mPendingTraversal = false;
                Slog.d(TAG, "performTraversalInternal pendingTraversal");
                performTraversalLocked(transaction);
                Iterator<DisplayManagerInternal.DisplayTransactionListener> it = this.mDisplayTransactionListeners.iterator();
                while (it.hasNext()) {
                    it.next().onDisplayTransaction(transaction);
                }
            }
        }
    }

    private float clampBrightness(int i, float f) {
        if (i == 1) {
            f = -1.0f;
        } else if (f != -1.0f && f < 0.0f) {
            f = Float.NaN;
        }
        return (i == 1 || i == 3 || i == 4 || f == -1.0f) ? f : this.mDmsExt.oplusAdjustBrightness(f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestDisplayStateInternal(int i, int i2, float f, float f2) {
        if (i2 == 0) {
            i2 = 2;
        }
        float clampBrightness = clampBrightness(i2, f);
        float clampBrightness2 = clampBrightness(i2, f2);
        synchronized (this.mSyncRoot) {
            int indexOfKey = this.mDisplayStates.indexOfKey(i);
            BrightnessPair valueAt = indexOfKey < 0 ? null : this.mDisplayBrightnesses.valueAt(indexOfKey);
            if (indexOfKey >= 0 && (this.mDisplayStates.valueAt(indexOfKey) != i2 || !BrightnessSynchronizer.floatEquals(valueAt.brightness, clampBrightness) || !BrightnessSynchronizer.floatEquals(valueAt.sdrBrightness, clampBrightness2))) {
                if (Trace.isTagEnabled(131072L)) {
                    Trace.asyncTraceForTrackBegin(131072L, "requestDisplayStateInternal:" + i, Display.stateToString(i2) + ", brightness=" + clampBrightness + ", sdrBrightness=" + clampBrightness2, i);
                }
                this.mDisplayStates.setValueAt(indexOfKey, i2);
                valueAt.brightness = clampBrightness;
                valueAt.sdrBrightness = clampBrightness2;
                if (Trace.isTagEnabled(131072L)) {
                    Trace.asyncTraceForTrackEnd(131072L, "requestDisplayStateInternal:" + i, i);
                }
                LogicalDisplay displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(i);
                Runnable updateDisplayStateLocked = displayLocked != null ? updateDisplayStateLocked(displayLocked.getPrimaryDisplayDeviceLocked()) : null;
                this.mDmsExt.enterDCMode(this.mWindowManagerInternal, clampBrightness);
                DisplayPowerController displayPowerController = this.mDisplayPowerControllers.get(0);
                if (displayPowerController != null) {
                    displayPowerController.updateFpsIfNeeded(clampBrightness);
                }
                if (updateDisplayStateLocked != null) {
                    updateDisplayStateLocked.run();
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class UidImportanceListener implements ActivityManager.OnUidImportanceListener {
        private UidImportanceListener() {
        }

        public void onUidImportance(int i, int i2) {
            synchronized (DisplayManagerService.this.mPendingCallbackSelfLocked) {
                try {
                    if (i2 >= 1000) {
                        Slog.d(DisplayManagerService.TAG, "Drop pending events for gone uid " + i);
                        DisplayManagerService.this.mPendingCallbackSelfLocked.delete(i);
                        return;
                    }
                    if (i2 >= 400) {
                        return;
                    }
                    SparseArray<PendingCallback> sparseArray = DisplayManagerService.this.mPendingCallbackSelfLocked.get(i);
                    if (sparseArray == null) {
                        return;
                    }
                    if (DisplayManagerService.DEBUG) {
                        Slog.d(DisplayManagerService.TAG, "Uid " + i + " becomes " + i2);
                    }
                    for (int size = sparseArray.size() - 1; size >= 0; size--) {
                        int keyAt = sparseArray.keyAt(size);
                        sparseArray.get(keyAt).sendPendingDisplayEvent();
                        sparseArray.delete(keyAt);
                    }
                    DisplayManagerService.this.mPendingCallbackSelfLocked.delete(i);
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class SettingsObserver extends ContentObserver {
        SettingsObserver() {
            super(DisplayManagerService.this.mHandler);
            DisplayManagerService.this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("minimal_post_processing_allowed"), false, this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            DisplayManagerService.this.handleSettingsChange();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSettingsChange() {
        synchronized (this.mSyncRoot) {
            updateSettingsLocked();
            scheduleTraversalLocked(false);
        }
    }

    private void updateSettingsLocked() {
        setMinimalPostProcessingAllowed(Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "minimal_post_processing_allowed", 1, -2) != 0);
    }

    private void updateUserDisabledHdrTypesFromSettingsLocked() {
        this.mAreUserDisabledHdrTypesAllowed = Settings.Global.getInt(this.mContext.getContentResolver(), "are_user_disabled_hdr_formats_allowed", 1) != 0;
        String string = Settings.Global.getString(this.mContext.getContentResolver(), "user_disabled_hdr_formats");
        if (string != null) {
            try {
                String[] split = TextUtils.split(string, ",");
                this.mUserDisabledHdrTypes = new int[split.length];
                for (int i = 0; i < split.length; i++) {
                    this.mUserDisabledHdrTypes[i] = Integer.parseInt(split[i]);
                }
                if (this.mAreUserDisabledHdrTypesAllowed) {
                    return;
                }
                this.mLogicalDisplayMapper.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda5
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DisplayManagerService.this.lambda$updateUserDisabledHdrTypesFromSettingsLocked$2((LogicalDisplay) obj);
                    }
                });
                return;
            } catch (NumberFormatException e) {
                Slog.e(TAG, "Failed to parse USER_DISABLED_HDR_FORMATS. Clearing the setting.", e);
                clearUserDisabledHdrTypesLocked();
                return;
            }
        }
        clearUserDisabledHdrTypesLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateUserDisabledHdrTypesFromSettingsLocked$2(LogicalDisplay logicalDisplay) {
        logicalDisplay.setUserDisabledHdrTypes(this.mUserDisabledHdrTypes);
        handleLogicalDisplayChangedLocked(logicalDisplay);
    }

    private void clearUserDisabledHdrTypesLocked() {
        synchronized (this.mSyncRoot) {
            this.mUserDisabledHdrTypes = new int[0];
            Settings.Global.putString(this.mContext.getContentResolver(), "user_disabled_hdr_formats", "");
        }
    }

    private void updateUserPreferredDisplayModeSettingsLocked() {
        final Display.Mode mode = new Display.Mode(Settings.Global.getInt(this.mContext.getContentResolver(), "user_preferred_resolution_width", -1), Settings.Global.getInt(this.mContext.getContentResolver(), "user_preferred_resolution_height", -1), Settings.Global.getFloat(this.mContext.getContentResolver(), "user_preferred_refresh_rate", 0.0f));
        Display.Mode mode2 = isResolutionAndRefreshRateValid(mode) ? mode : null;
        this.mUserPreferredMode = mode2;
        if (mode2 != null) {
            this.mDisplayDeviceRepo.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((DisplayDevice) obj).setUserPreferredDisplayModeLocked(mode);
                }
            });
        } else {
            this.mLogicalDisplayMapper.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DisplayManagerService.this.lambda$updateUserPreferredDisplayModeSettingsLocked$4((LogicalDisplay) obj);
                }
            });
        }
    }

    private DisplayInfo getDisplayInfoForFrameRateOverride(DisplayEventReceiver.FrameRateOverride[] frameRateOverrideArr, DisplayInfo displayInfo, int i) {
        float f = displayInfo.renderFrameRate;
        int length = frameRateOverrideArr.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            DisplayEventReceiver.FrameRateOverride frameRateOverride = frameRateOverrideArr[i2];
            if (frameRateOverride.uid == i) {
                f = frameRateOverride.frameRateHz;
                break;
            }
            i2++;
        }
        if (f == 0.0f) {
            return displayInfo;
        }
        boolean z = i < 10000 || CompatChanges.isChangeEnabled(DISPLAY_MODE_RETURNS_PHYSICAL_REFRESH_RATE, i);
        Display.Mode mode = displayInfo.getMode();
        float refreshRate = mode.getRefreshRate() / f;
        float round = Math.round(refreshRate);
        if (Math.abs(refreshRate - round) > THRESHOLD_FOR_REFRESH_RATES_DIVISORS) {
            return displayInfo;
        }
        float refreshRate2 = mode.getRefreshRate() / round;
        DisplayInfo displayInfo2 = new DisplayInfo();
        displayInfo2.copyFrom(displayInfo);
        for (Display.Mode mode2 : displayInfo.supportedModes) {
            if (mode2.equalsExceptRefreshRate(mode) && mode2.getRefreshRate() >= refreshRate2 - THRESHOLD_FOR_REFRESH_RATES_DIVISORS && mode2.getRefreshRate() <= refreshRate2 + THRESHOLD_FOR_REFRESH_RATES_DIVISORS) {
                if (this.mDebugBrightness) {
                    Slog.d(TAG, "found matching modeId " + mode2.getModeId());
                }
                displayInfo2.refreshRateOverride = mode2.getRefreshRate();
                if (!z) {
                    displayInfo2.modeId = mode2.getModeId();
                }
                return displayInfo2;
            }
        }
        displayInfo2.refreshRateOverride = refreshRate2;
        if (!z) {
            Display.Mode[] modeArr = displayInfo.supportedModes;
            Display.Mode[] modeArr2 = (Display.Mode[]) Arrays.copyOf(modeArr, modeArr.length + 1);
            displayInfo2.supportedModes = modeArr2;
            modeArr2[modeArr2.length - 1] = new Display.Mode(255, mode.getPhysicalWidth(), mode.getPhysicalHeight(), displayInfo2.refreshRateOverride, new float[0], mode.getSupportedHdrTypes());
            Display.Mode[] modeArr3 = displayInfo2.supportedModes;
            displayInfo2.modeId = modeArr3[modeArr3.length - 1].getModeId();
        }
        return displayInfo2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DisplayInfo getDisplayInfoInternal(int i, int i2) {
        synchronized (this.mSyncRoot) {
            LogicalDisplay displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(i);
            if (displayLocked != null) {
                DisplayInfo displayInfoLocked = displayLocked.getDisplayInfoLocked();
                if (displayInfoLocked != null && displayInfoLocked.name != null) {
                    DisplayInfo displayInfoForFrameRateOverride = getDisplayInfoForFrameRateOverride(displayLocked.getFrameRateOverrides(), displayInfoLocked, i2);
                    if (displayInfoForFrameRateOverride.hasAccess(i2) || isUidPresentOnDisplayInternal(i2, i)) {
                        return this.mDmsExt.getZoomModeDisplayInfo(this.mDmsExt.getBacklightTypeDisplayInfo(displayInfoForFrameRateOverride, i), i, i2);
                    }
                }
                return null;
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerCallbackInternal(IDisplayManagerCallback iDisplayManagerCallback, int i, int i2, long j) {
        synchronized (this.mSyncRoot) {
            CallbackRecord callbackRecord = this.mCallbacks.get(i);
            if (callbackRecord != null) {
                callbackRecord.updateEventsMask(j);
                return;
            }
            CallbackRecord callbackRecord2 = new CallbackRecord(i, i2, iDisplayManagerCallback, j);
            try {
                iDisplayManagerCallback.asBinder().linkToDeath(callbackRecord2, 0);
                this.mCallbacks.put(i, callbackRecord2);
                this.mDmsExt.addProxyBinder(iDisplayManagerCallback.asBinder(), i2, i);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCallbackDied(CallbackRecord callbackRecord) {
        synchronized (this.mSyncRoot) {
            this.mCallbacks.remove(callbackRecord.mPid);
            stopWifiDisplayScanLocked(callbackRecord);
        }
        this.mDmsExt.removeProxyBinder(callbackRecord.mCallback.asBinder(), callbackRecord.mUid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startWifiDisplayScanInternal(int i) {
        synchronized (this.mSyncRoot) {
            CallbackRecord callbackRecord = this.mCallbacks.get(i);
            if (callbackRecord == null) {
                throw new IllegalStateException("The calling process has not registered an IDisplayManagerCallback.");
            }
            startWifiDisplayScanLocked(callbackRecord);
        }
    }

    private void startWifiDisplayScanLocked(CallbackRecord callbackRecord) {
        WifiDisplayAdapter wifiDisplayAdapter;
        if (callbackRecord.mWifiDisplayScanRequested) {
            return;
        }
        callbackRecord.mWifiDisplayScanRequested = true;
        int i = this.mWifiDisplayScanRequestCount;
        this.mWifiDisplayScanRequestCount = i + 1;
        if (i != 0 || (wifiDisplayAdapter = this.mWifiDisplayAdapter) == null) {
            return;
        }
        wifiDisplayAdapter.requestStartScanLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopWifiDisplayScanInternal(int i) {
        synchronized (this.mSyncRoot) {
            CallbackRecord callbackRecord = this.mCallbacks.get(i);
            if (callbackRecord == null) {
                throw new IllegalStateException("The calling process has not registered an IDisplayManagerCallback.");
            }
            stopWifiDisplayScanLocked(callbackRecord);
        }
    }

    private void stopWifiDisplayScanLocked(CallbackRecord callbackRecord) {
        if (callbackRecord.mWifiDisplayScanRequested) {
            callbackRecord.mWifiDisplayScanRequested = false;
            int i = this.mWifiDisplayScanRequestCount - 1;
            this.mWifiDisplayScanRequestCount = i;
            if (i == 0) {
                WifiDisplayAdapter wifiDisplayAdapter = this.mWifiDisplayAdapter;
                if (wifiDisplayAdapter != null) {
                    wifiDisplayAdapter.requestStopScanLocked();
                    return;
                }
                return;
            }
            if (i < 0) {
                Slog.wtf(TAG, "mWifiDisplayScanRequestCount became negative: " + this.mWifiDisplayScanRequestCount);
                this.mWifiDisplayScanRequestCount = 0;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectWifiDisplayInternal(String str) {
        synchronized (this.mSyncRoot) {
            WifiDisplayAdapter wifiDisplayAdapter = this.mWifiDisplayAdapter;
            if (wifiDisplayAdapter != null) {
                wifiDisplayAdapter.requestConnectLocked(str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pauseWifiDisplayInternal() {
        synchronized (this.mSyncRoot) {
            WifiDisplayAdapter wifiDisplayAdapter = this.mWifiDisplayAdapter;
            if (wifiDisplayAdapter != null) {
                wifiDisplayAdapter.requestPauseLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resumeWifiDisplayInternal() {
        synchronized (this.mSyncRoot) {
            WifiDisplayAdapter wifiDisplayAdapter = this.mWifiDisplayAdapter;
            if (wifiDisplayAdapter != null) {
                wifiDisplayAdapter.requestResumeLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disconnectWifiDisplayInternal() {
        synchronized (this.mSyncRoot) {
            WifiDisplayAdapter wifiDisplayAdapter = this.mWifiDisplayAdapter;
            if (wifiDisplayAdapter != null) {
                wifiDisplayAdapter.requestDisconnectLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renameWifiDisplayInternal(String str, String str2) {
        synchronized (this.mSyncRoot) {
            WifiDisplayAdapter wifiDisplayAdapter = this.mWifiDisplayAdapter;
            if (wifiDisplayAdapter != null) {
                wifiDisplayAdapter.requestRenameLocked(str, str2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forgetWifiDisplayInternal(String str) {
        synchronized (this.mSyncRoot) {
            WifiDisplayAdapter wifiDisplayAdapter = this.mWifiDisplayAdapter;
            if (wifiDisplayAdapter != null) {
                wifiDisplayAdapter.requestForgetLocked(str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public WifiDisplayStatus getWifiDisplayStatusInternal() {
        synchronized (this.mSyncRoot) {
            WifiDisplayAdapter wifiDisplayAdapter = this.mWifiDisplayAdapter;
            if (wifiDisplayAdapter != null) {
                return wifiDisplayAdapter.getWifiDisplayStatusLocked();
            }
            return new WifiDisplayStatus();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUserDisabledHdrTypesInternal(final int[] iArr) {
        synchronized (this.mSyncRoot) {
            if (iArr == null) {
                Slog.e(TAG, "Null is not an expected argument to setUserDisabledHdrTypesInternal");
                return;
            }
            if (!isSubsetOf(Display.HdrCapabilities.HDR_TYPES, iArr)) {
                Slog.e(TAG, "userDisabledHdrTypes contains unexpected types");
                return;
            }
            Arrays.sort(iArr);
            if (Arrays.equals(this.mUserDisabledHdrTypes, iArr)) {
                return;
            }
            Settings.Global.putString(this.mContext.getContentResolver(), "user_disabled_hdr_formats", iArr.length != 0 ? TextUtils.join(",", Arrays.stream(iArr).boxed().toArray()) : "");
            this.mUserDisabledHdrTypes = iArr;
            if (!this.mAreUserDisabledHdrTypesAllowed) {
                this.mLogicalDisplayMapper.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda14
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DisplayManagerService.this.lambda$setUserDisabledHdrTypesInternal$5(iArr, (LogicalDisplay) obj);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setUserDisabledHdrTypesInternal$5(int[] iArr, LogicalDisplay logicalDisplay) {
        logicalDisplay.setUserDisabledHdrTypes(iArr);
        handleLogicalDisplayChangedLocked(logicalDisplay);
    }

    private boolean isSubsetOf(int[] iArr, int[] iArr2) {
        for (int i : iArr2) {
            if (Arrays.binarySearch(iArr, i) < 0) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAreUserDisabledHdrTypesAllowedInternal(boolean z) {
        synchronized (this.mSyncRoot) {
            if (this.mAreUserDisabledHdrTypesAllowed == z) {
                return;
            }
            this.mAreUserDisabledHdrTypesAllowed = z;
            if (this.mUserDisabledHdrTypes.length == 0) {
                return;
            }
            Settings.Global.putInt(this.mContext.getContentResolver(), "are_user_disabled_hdr_formats_allowed", z ? 1 : 0);
            final int[] iArr = new int[0];
            if (!this.mAreUserDisabledHdrTypesAllowed) {
                iArr = this.mUserDisabledHdrTypes;
            }
            this.mLogicalDisplayMapper.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DisplayManagerService.this.lambda$setAreUserDisabledHdrTypesAllowedInternal$6(iArr, (LogicalDisplay) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setAreUserDisabledHdrTypesAllowedInternal$6(int[] iArr, LogicalDisplay logicalDisplay) {
        logicalDisplay.setUserDisabledHdrTypes(iArr);
        handleLogicalDisplayChangedLocked(logicalDisplay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestColorModeInternal(int i, int i2) {
        synchronized (this.mSyncRoot) {
            LogicalDisplay displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(i);
            if (displayLocked != null && displayLocked.getRequestedColorModeLocked() != i2) {
                displayLocked.setRequestedColorModeLocked(i2);
                scheduleTraversalLocked(false);
            }
        }
    }

    private boolean validatePackageName(int i, String str) {
        String[] packagesForUid;
        if (i == 0) {
            return true;
        }
        if (str != null && (packagesForUid = this.mContext.getPackageManager().getPackagesForUid(i)) != null) {
            for (String str2 : packagesForUid) {
                if (str2.equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canProjectVideo(IMediaProjection iMediaProjection) {
        if (iMediaProjection != null) {
            try {
                if (iMediaProjection.canProjectVideo()) {
                    return true;
                }
            } catch (RemoteException e) {
                Slog.e(TAG, "Unable to query projection service for permissions", e);
            }
        }
        if (checkCallingPermission("android.permission.CAPTURE_VIDEO_OUTPUT", "canProjectVideo()")) {
            return true;
        }
        return canProjectSecureVideo(iMediaProjection);
    }

    private boolean canProjectSecureVideo(IMediaProjection iMediaProjection) {
        if (iMediaProjection != null) {
            try {
                if (iMediaProjection.canProjectSecureVideo()) {
                    return true;
                }
            } catch (RemoteException e) {
                Slog.e(TAG, "Unable to query projection service for permissions", e);
            }
        }
        return checkCallingPermission("android.permission.CAPTURE_SECURE_VIDEO_OUTPUT", "canProjectSecureVideo()");
    }

    private boolean checkCallingPermission(String str, String str2) {
        if (this.mContext.checkCallingPermission(str) == 0) {
            return true;
        }
        Slog.w(TAG, "Permission Denial: " + str2 + " from pid=" + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid() + " requires " + str);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int createVirtualDisplayInternal(VirtualDisplayConfig virtualDisplayConfig, IVirtualDisplayCallback iVirtualDisplayCallback, IMediaProjection iMediaProjection, IVirtualDevice iVirtualDevice, DisplayWindowPolicyController displayWindowPolicyController, String str) {
        boolean z;
        boolean z2;
        int createVirtualDisplayLocked;
        int callingUid = Binder.getCallingUid();
        if (callingUid != 1000 && "OplusPuttDisplay".equals(virtualDisplayConfig.getName())) {
            throw new SecurityException("has no permission to use OplusPuttDisplay");
        }
        if (!validatePackageName(callingUid, str)) {
            throw new SecurityException("packageName must match the calling uid");
        }
        if (iVirtualDisplayCallback == null) {
            throw new IllegalArgumentException("appToken must not be null");
        }
        if (virtualDisplayConfig == null) {
            throw new IllegalArgumentException("virtualDisplayConfig must not be null");
        }
        Surface surface = virtualDisplayConfig.getSurface();
        int flags = virtualDisplayConfig.getFlags();
        if (iVirtualDevice != null) {
            try {
                if (!((VirtualDeviceManager) this.mContext.getSystemService(VirtualDeviceManager.class)).isValidVirtualDeviceId(iVirtualDevice.getDeviceId())) {
                    throw new SecurityException("Invalid virtual device");
                }
                flags |= ((VirtualDeviceManagerInternal) getLocalService(VirtualDeviceManagerInternal.class)).getBaseVirtualDisplayFlags(iVirtualDevice);
            } catch (RemoteException unused) {
                throw new SecurityException("Unable to validate virtual device");
            }
        }
        if (surface != null && surface.isSingleBuffered()) {
            throw new IllegalArgumentException("Surface can't be single-buffered");
        }
        if ((flags & 1) != 0) {
            flags |= 16;
            if ((flags & 32) != 0) {
                throw new IllegalArgumentException("Public display must not be marked as SHOW_WHEN_LOCKED_INSECURE");
            }
        }
        if ((flags & 8) != 0) {
            flags &= -17;
        }
        if ((flags & 16) != 0) {
            flags &= -2049;
        }
        if ((flags & 2048) == 0 && iVirtualDevice != null) {
            flags |= 32768;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (iMediaProjection != null) {
                try {
                    if (!getProjectionService().isCurrentProjection(iMediaProjection)) {
                        throw new SecurityException("Cannot create VirtualDisplay with non-current MediaProjection");
                    }
                    if (iMediaProjection.isValid()) {
                        z = false;
                    } else {
                        Slog.w(TAG, "Reusing token: create virtual display for app reusing token");
                        getProjectionService().requestConsentForInvalidProjection(iMediaProjection);
                        z = true;
                    }
                    flags = iMediaProjection.applyVirtualDisplayFlags(flags);
                    z2 = z;
                } catch (RemoteException e) {
                    throw new SecurityException("Unable to validate media projection or flags", e);
                }
            } else {
                z2 = false;
            }
            if (callingUid != 1000 && (flags & 16) != 0 && !canProjectVideo(iMediaProjection)) {
                throw new SecurityException("Requires CAPTURE_VIDEO_OUTPUT or CAPTURE_SECURE_VIDEO_OUTPUT permission, or an appropriate MediaProjection token in order to create a screen sharing virtual display.");
            }
            if (callingUid != 1000 && (flags & 4) != 0 && !canProjectSecureVideo(iMediaProjection)) {
                throw new SecurityException("Requires CAPTURE_SECURE_VIDEO_OUTPUT or an appropriate MediaProjection token to create a secure virtual display.");
            }
            if (callingUid != 1000 && (flags & 1024) != 0 && !checkCallingPermission("android.permission.ADD_TRUSTED_DISPLAY", "createVirtualDisplay()")) {
                EventLog.writeEvent(1397638484, "162627132", Integer.valueOf(callingUid), "Attempt to create a trusted display without holding permission!");
                throw new SecurityException("Requires ADD_TRUSTED_DISPLAY permission to create a trusted virtual display.");
            }
            if (callingUid != 1000 && (flags & 2048) != 0 && iVirtualDevice == null && !checkCallingPermission("android.permission.ADD_TRUSTED_DISPLAY", "createVirtualDisplay()")) {
                throw new SecurityException("Requires ADD_TRUSTED_DISPLAY permission to create a virtual display which is not in the default DisplayGroup.");
            }
            if ((flags & 4096) != 0 && callingUid != 1000 && !checkCallingPermission("android.permission.ADD_ALWAYS_UNLOCKED_DISPLAY", "createVirtualDisplay()")) {
                throw new SecurityException("Requires ADD_ALWAYS_UNLOCKED_DISPLAY permission to create an always unlocked virtual display.");
            }
            if ((flags & 1024) == 0) {
                flags &= -513;
            }
            int i = flags;
            if ((i & FrameworkStatsLog.APP_STANDBY_BUCKET_CHANGED__MAIN_REASON__MAIN_FORCED_BY_SYSTEM) == 512 && !checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "createVirtualDisplay()")) {
                throw new SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
            }
            clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (this.mSyncRoot) {
                    createVirtualDisplayLocked = createVirtualDisplayLocked(iVirtualDisplayCallback, iMediaProjection, callingUid, str, iVirtualDevice, surface, i, virtualDisplayConfig);
                    if (createVirtualDisplayLocked != -1 && iVirtualDevice != null && displayWindowPolicyController != null) {
                        this.mDisplayWindowPolicyControllers.put(createVirtualDisplayLocked, Pair.create(iVirtualDevice, displayWindowPolicyController));
                    }
                }
                ContentRecordingSession contentRecordingSession = null;
                if (iMediaProjection != null) {
                    try {
                        IBinder launchCookie = iMediaProjection.getLaunchCookie();
                        if (launchCookie == null) {
                            contentRecordingSession = ContentRecordingSession.createDisplaySession(virtualDisplayConfig.getDisplayIdToMirror());
                        } else {
                            contentRecordingSession = ContentRecordingSession.createTaskSession(launchCookie);
                        }
                    } catch (RemoteException e2) {
                        Slog.e(TAG, "Unable to retrieve the projection's launch cookie", e2);
                    }
                }
                if (((iMediaProjection == null && (i & 16) == 0) ? false : true) && createVirtualDisplayLocked != -1 && contentRecordingSession != null) {
                    contentRecordingSession.setVirtualDisplayId(createVirtualDisplayLocked);
                    contentRecordingSession.setWaitingForConsent(z2);
                    try {
                        if (!getProjectionService().setContentRecordingSession(contentRecordingSession, iMediaProjection)) {
                            releaseVirtualDisplayInternal(iVirtualDisplayCallback.asBinder());
                            return -1;
                        }
                        if (iMediaProjection != null) {
                            iMediaProjection.notifyVirtualDisplayCreated(createVirtualDisplayLocked);
                        }
                    } catch (RemoteException e3) {
                        Slog.e(TAG, "Unable to tell MediaProjectionManagerService to set the content recording session", e3);
                    }
                }
                return createVirtualDisplayLocked;
            } finally {
            }
        } finally {
        }
    }

    private int createVirtualDisplayLocked(IVirtualDisplayCallback iVirtualDisplayCallback, IMediaProjection iMediaProjection, int i, String str, IVirtualDevice iVirtualDevice, Surface surface, int i2, VirtualDisplayConfig virtualDisplayConfig) {
        VirtualDisplayAdapter virtualDisplayAdapter = this.mVirtualDisplayAdapter;
        if (virtualDisplayAdapter == null) {
            Slog.w(TAG, "Rejecting request to create private virtual display because the virtual display adapter is not available.");
            return -1;
        }
        DisplayDevice createVirtualDisplayLocked = virtualDisplayAdapter.createVirtualDisplayLocked(iVirtualDisplayCallback, iMediaProjection, i, str, surface, i2, virtualDisplayConfig);
        if (createVirtualDisplayLocked == null) {
            return -1;
        }
        if ((i2 & 32768) != 0) {
            if (iVirtualDevice != null) {
                try {
                    this.mLogicalDisplayMapper.associateDisplayDeviceWithVirtualDevice(createVirtualDisplayLocked, iVirtualDevice.getDeviceId());
                } catch (RemoteException e) {
                    e.rethrowFromSystemServer();
                }
            } else {
                Slog.i(TAG, "Display created with VIRTUAL_DISPLAY_FLAG_DEVICE_DISPLAY_GROUP set, but no virtual device. The display will not be added to a device display group.");
            }
        }
        this.mDisplayDeviceRepo.onDisplayDeviceEvent(createVirtualDisplayLocked, 1);
        LogicalDisplay displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(createVirtualDisplayLocked);
        if (displayLocked != null) {
            return displayLocked.getDisplayIdLocked();
        }
        Slog.w(TAG, "Rejecting request to create virtual display because the logical display was not created.");
        this.mVirtualDisplayAdapter.releaseVirtualDisplayLocked(iVirtualDisplayCallback.asBinder());
        this.mDisplayDeviceRepo.onDisplayDeviceEvent(createVirtualDisplayLocked, 3);
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resizeVirtualDisplayInternal(IBinder iBinder, int i, int i2, int i3) {
        synchronized (this.mSyncRoot) {
            VirtualDisplayAdapter virtualDisplayAdapter = this.mVirtualDisplayAdapter;
            if (virtualDisplayAdapter == null) {
                return;
            }
            virtualDisplayAdapter.resizeVirtualDisplayLocked(iBinder, i, i2, i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVirtualDisplaySurfaceInternal(IBinder iBinder, Surface surface) {
        synchronized (this.mSyncRoot) {
            VirtualDisplayAdapter virtualDisplayAdapter = this.mVirtualDisplayAdapter;
            if (virtualDisplayAdapter == null) {
                return;
            }
            virtualDisplayAdapter.setVirtualDisplaySurfaceLocked(iBinder, surface);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseVirtualDisplayInternal(IBinder iBinder) {
        synchronized (this.mSyncRoot) {
            VirtualDisplayAdapter virtualDisplayAdapter = this.mVirtualDisplayAdapter;
            if (virtualDisplayAdapter == null) {
                return;
            }
            DisplayDevice releaseVirtualDisplayLocked = virtualDisplayAdapter.releaseVirtualDisplayLocked(iBinder);
            if (releaseVirtualDisplayLocked != null) {
                this.mDisplayDeviceRepo.onDisplayDeviceEvent(releaseVirtualDisplayLocked, 3);
            }
            if (releaseVirtualDisplayLocked == null) {
                this.mDmsExt.hookUpdateScreenRecorderState(-1, "all", false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVirtualDisplayStateInternal(IBinder iBinder, boolean z) {
        synchronized (this.mSyncRoot) {
            VirtualDisplayAdapter virtualDisplayAdapter = this.mVirtualDisplayAdapter;
            if (virtualDisplayAdapter == null) {
                return;
            }
            virtualDisplayAdapter.setVirtualDisplayStateLocked(iBinder, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerDefaultDisplayAdapters() {
        synchronized (this.mSyncRoot) {
            registerDisplayAdapterLocked(this.mInjector.getLocalDisplayAdapter(this.mSyncRoot, this.mContext, this.mHandler, this.mDisplayDeviceRepo));
            VirtualDisplayAdapter virtualDisplayAdapter = this.mInjector.getVirtualDisplayAdapter(this.mSyncRoot, this.mContext, this.mHandler, this.mDisplayDeviceRepo);
            this.mVirtualDisplayAdapter = virtualDisplayAdapter;
            if (virtualDisplayAdapter != null) {
                registerDisplayAdapterLocked(virtualDisplayAdapter);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerAdditionalDisplayAdapters() {
        synchronized (this.mSyncRoot) {
            if (shouldRegisterNonEssentialDisplayAdaptersLocked()) {
                registerOverlayDisplayAdapterLocked();
                registerWifiDisplayAdapterLocked();
            }
        }
    }

    private void registerOverlayDisplayAdapterLocked() {
        registerDisplayAdapterLocked(new OverlayDisplayAdapter(this.mSyncRoot, this.mContext, this.mHandler, this.mDisplayDeviceRepo, this.mUiHandler));
        this.mDmsExt.setActivityPreloadDisplayAdapter(this.mDisplayAdapters);
    }

    private void registerWifiDisplayAdapterLocked() {
        if (this.mContext.getResources().getBoolean(17891684) || SystemProperties.getInt(FORCE_WIFI_DISPLAY_ENABLE, -1) == 1 || (Build.isMtkPlatform() && SystemProperties.get("ro.vendor.mtk_wfd_support").equals("1"))) {
            WifiDisplayAdapter wifiDisplayAdapter = new WifiDisplayAdapter(this.mSyncRoot, this.mContext, this.mHandler, this.mDisplayDeviceRepo, this.mPersistentDataStore);
            this.mWifiDisplayAdapter = wifiDisplayAdapter;
            registerDisplayAdapterLocked(wifiDisplayAdapter);
        }
    }

    private boolean shouldRegisterNonEssentialDisplayAdaptersLocked() {
        return !this.mSafeMode;
    }

    private void registerDisplayAdapterLocked(DisplayAdapter displayAdapter) {
        this.mDisplayAdapters.add(displayAdapter);
        displayAdapter.registerLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplayAddedLocked(LogicalDisplay logicalDisplay) {
        DisplayDevice primaryDisplayDeviceLocked = logicalDisplay.getPrimaryDisplayDeviceLocked();
        int displayIdLocked = logicalDisplay.getDisplayIdLocked();
        boolean z = displayIdLocked == 0;
        configureColorModeLocked(logicalDisplay, primaryDisplayDeviceLocked);
        if (!this.mAreUserDisabledHdrTypesAllowed) {
            logicalDisplay.setUserDisabledHdrTypes(this.mUserDisabledHdrTypes);
        }
        if (z) {
            notifyDefaultDisplayDeviceUpdated(logicalDisplay);
            recordStableDisplayStatsIfNeededLocked(logicalDisplay);
            recordTopInsetLocked(logicalDisplay);
        }
        Display.Mode mode = this.mUserPreferredMode;
        if (mode != null) {
            primaryDisplayDeviceLocked.setUserPreferredDisplayModeLocked(mode);
        } else {
            lambda$updateUserPreferredDisplayModeSettingsLocked$4(logicalDisplay);
        }
        addDisplayPowerControllerLocked(logicalDisplay);
        if (primaryDisplayDeviceLocked.getDisplayDeviceInfoLocked().type == 1) {
            this.mDisplayStates.append(displayIdLocked, 0);
        } else {
            this.mDisplayStates.append(displayIdLocked, 2);
        }
        float f = logicalDisplay.getDisplayInfoLocked().brightnessDefault;
        this.mDisplayBrightnesses.append(displayIdLocked, new BrightnessPair(f, f));
        DisplayManagerGlobal.invalidateLocalDisplayInfoCaches();
        if (z) {
            this.mSyncRoot.notifyAll();
        }
        sendDisplayEventLocked(logicalDisplay, 1);
        this.mDmsExt.handleLogicalDisplayAddedLocked(logicalDisplay);
        Runnable updateDisplayStateLocked = updateDisplayStateLocked(primaryDisplayDeviceLocked);
        if (updateDisplayStateLocked != null) {
            updateDisplayStateLocked.run();
        }
        scheduleTraversalLocked(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplayChangedLocked(LogicalDisplay logicalDisplay) {
        updateViewportPowerStateLocked(logicalDisplay);
        int displayIdLocked = logicalDisplay.getDisplayIdLocked();
        if (displayIdLocked == 0) {
            recordTopInsetLocked(logicalDisplay);
        }
        sendDisplayEventLocked(logicalDisplay, 2);
        scheduleTraversalLocked(false);
        this.mPersistentDataStore.saveIfNeeded();
        this.mDmsExt.handleLogicalDisplayChangedLocked(logicalDisplay);
        DisplayPowerController displayPowerController = this.mDisplayPowerControllers.get(displayIdLocked);
        if (displayPowerController != null) {
            int leadDisplayIdLocked = logicalDisplay.getLeadDisplayIdLocked();
            updateDisplayPowerControllerLeaderLocked(displayPowerController, leadDisplayIdLocked);
            HighBrightnessModeMetadata highBrightnessModeMetadataLocked = this.mHighBrightnessModeMetadataMapper.getHighBrightnessModeMetadataLocked(logicalDisplay);
            if (highBrightnessModeMetadataLocked != null) {
                displayPowerController.onDisplayChanged(highBrightnessModeMetadataLocked, leadDisplayIdLocked);
            }
        }
    }

    private void updateDisplayPowerControllerLeaderLocked(DisplayPowerControllerInterface displayPowerControllerInterface, int i) {
        displayPowerControllerInterface.getLeadDisplayId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplayFrameRateOverridesChangedLocked(LogicalDisplay logicalDisplay) {
        sendDisplayEventFrameRateOverrideLocked(logicalDisplay.getDisplayIdLocked());
        scheduleTraversalLocked(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplayRemovedLocked(LogicalDisplay logicalDisplay) {
        final IVirtualDevice iVirtualDevice;
        this.mDmsExt.handleLogicalDisplayRemovedLocked(logicalDisplay);
        final int displayIdLocked = logicalDisplay.getDisplayIdLocked();
        DisplayPowerController displayPowerController = (DisplayPowerController) this.mDisplayPowerControllers.removeReturnOld(displayIdLocked);
        if (displayPowerController != null) {
            updateDisplayPowerControllerLeaderLocked(displayPowerController, -1);
            displayPowerController.stop();
        }
        this.mDisplayStates.delete(displayIdLocked);
        this.mDisplayBrightnesses.delete(displayIdLocked);
        DisplayManagerGlobal.invalidateLocalDisplayInfoCaches();
        sendDisplayEventLocked(logicalDisplay, 3);
        scheduleTraversalLocked(false);
        if (!this.mDisplayWindowPolicyControllers.contains(displayIdLocked) || (iVirtualDevice = (IVirtualDevice) ((Pair) this.mDisplayWindowPolicyControllers.removeReturnOld(displayIdLocked)).first) == null) {
            return;
        }
        this.mHandler.post(new Runnable() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                DisplayManagerService.this.lambda$handleLogicalDisplayRemovedLocked$7(iVirtualDevice, displayIdLocked);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleLogicalDisplayRemovedLocked$7(IVirtualDevice iVirtualDevice, int i) {
        ((VirtualDeviceManagerInternal) getLocalService(VirtualDeviceManagerInternal.class)).onVirtualDisplayRemoved(iVirtualDevice, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplaySwappedLocked(LogicalDisplay logicalDisplay) {
        this.mDmsExt.handleLogicalDisplaySwappedLocked(logicalDisplay);
        handleLogicalDisplayChangedLocked(logicalDisplay);
        if (logicalDisplay.getDisplayIdLocked() == 0) {
            notifyDefaultDisplayDeviceUpdated(logicalDisplay);
        }
        this.mHandler.sendEmptyMessage(6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplayHdrSdrRatioChangedLocked(LogicalDisplay logicalDisplay) {
        sendDisplayEventLocked(logicalDisplay, 5);
    }

    private void notifyDefaultDisplayDeviceUpdated(LogicalDisplay logicalDisplay) {
        this.mDisplayModeDirector.defaultDisplayDeviceUpdated(logicalDisplay.getPrimaryDisplayDeviceLocked().mDisplayDeviceConfig);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplayDeviceStateTransitionLocked(LogicalDisplay logicalDisplay) {
        DisplayPowerController displayPowerController = this.mDisplayPowerControllers.get(logicalDisplay.getDisplayIdLocked());
        if (displayPowerController != null) {
            int leadDisplayIdLocked = logicalDisplay.getLeadDisplayIdLocked();
            updateDisplayPowerControllerLeaderLocked(displayPowerController, leadDisplayIdLocked);
            HighBrightnessModeMetadata highBrightnessModeMetadataLocked = this.mHighBrightnessModeMetadataMapper.getHighBrightnessModeMetadataLocked(logicalDisplay);
            if (highBrightnessModeMetadataLocked != null) {
                displayPowerController.onDisplayChanged(highBrightnessModeMetadataLocked, leadDisplayIdLocked);
            }
        }
        this.mDmsExt.handleLogicalDisplayDeviceStateTransitionLocked(logicalDisplay);
    }

    private Runnable updateDisplayStateLocked(DisplayDevice displayDevice) {
        LogicalDisplay displayLocked;
        int displayIdLocked;
        int i;
        if ((displayDevice.getDisplayDeviceInfoLocked().flags & 32) != 0 || (displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(displayDevice)) == null || (i = this.mDisplayStates.get((displayIdLocked = displayLocked.getDisplayIdLocked()))) == 0) {
            return null;
        }
        BrightnessPair brightnessPair = this.mDisplayBrightnesses.get(displayIdLocked);
        return displayDevice.requestDisplayStateLocked(i, brightnessPair.brightness, brightnessPair.sdrBrightness);
    }

    private void configureColorModeLocked(LogicalDisplay logicalDisplay, DisplayDevice displayDevice) {
        if (logicalDisplay.getPrimaryDisplayDeviceLocked() == displayDevice) {
            int colorMode = this.mPersistentDataStore.getColorMode(displayDevice);
            if (colorMode == -1) {
                colorMode = logicalDisplay.getDisplayIdLocked() == 0 ? this.mDefaultDisplayDefaultColorMode : 0;
            }
            logicalDisplay.setRequestedColorModeLocked(colorMode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: configurePreferredDisplayModeLocked, reason: merged with bridge method [inline-methods] */
    public void lambda$updateUserPreferredDisplayModeSettingsLocked$4(LogicalDisplay logicalDisplay) {
        DisplayDevice primaryDisplayDeviceLocked = logicalDisplay.getPrimaryDisplayDeviceLocked();
        Point userPreferredResolution = this.mPersistentDataStore.getUserPreferredResolution(primaryDisplayDeviceLocked);
        float userPreferredRefreshRate = this.mPersistentDataStore.getUserPreferredRefreshRate(primaryDisplayDeviceLocked);
        if ((userPreferredResolution == null && Float.isNaN(userPreferredRefreshRate)) || (userPreferredResolution.equals(0, 0) && userPreferredRefreshRate == 0.0f)) {
            Display.Mode systemPreferredDisplayModeLocked = primaryDisplayDeviceLocked.getSystemPreferredDisplayModeLocked();
            if (systemPreferredDisplayModeLocked == null) {
                return;
            }
            storeModeInPersistentDataStoreLocked(logicalDisplay.getDisplayIdLocked(), systemPreferredDisplayModeLocked.getPhysicalWidth(), systemPreferredDisplayModeLocked.getPhysicalHeight(), systemPreferredDisplayModeLocked.getRefreshRate());
            primaryDisplayDeviceLocked.setUserPreferredDisplayModeLocked(systemPreferredDisplayModeLocked);
            return;
        }
        Display.Mode.Builder builder = new Display.Mode.Builder();
        builder.setResolution(userPreferredResolution.x, userPreferredResolution.y);
        if (!Float.isNaN(userPreferredRefreshRate)) {
            builder.setRefreshRate(userPreferredRefreshRate);
        }
        primaryDisplayDeviceLocked.setUserPreferredDisplayModeLocked(builder.build());
    }

    @GuardedBy({"mSyncRoot"})
    private void storeHdrConversionModeLocked(HdrConversionMode hdrConversionMode) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "hdr_conversion_mode", hdrConversionMode.getConversionMode());
        Settings.Global.putInt(this.mContext.getContentResolver(), "hdr_force_conversion_type", hdrConversionMode.getConversionMode() == 3 ? hdrConversionMode.getPreferredHdrOutputType() : -1);
    }

    @GuardedBy({"mSyncRoot"})
    void updateHdrConversionModeSettingsLocked() {
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "hdr_conversion_mode", 2);
        HdrConversionMode hdrConversionMode = new HdrConversionMode(i, i == 3 ? Settings.Global.getInt(this.mContext.getContentResolver(), "hdr_force_conversion_type", 1) : -1);
        this.mHdrConversionMode = hdrConversionMode;
        setHdrConversionModeInternal(hdrConversionMode);
    }

    private void recordStableDisplayStatsIfNeededLocked(LogicalDisplay logicalDisplay) {
        Point point = this.mStableDisplaySize;
        if (point.x > 0 || point.y > 0) {
            return;
        }
        DisplayInfo displayInfoLocked = logicalDisplay.getDisplayInfoLocked();
        setStableDisplaySizeLocked(displayInfoLocked.getNaturalWidth(), displayInfoLocked.getNaturalHeight());
    }

    private void recordTopInsetLocked(LogicalDisplay logicalDisplay) {
        int i;
        if (!this.mSystemReady || logicalDisplay == null || (i = logicalDisplay.getInsets().top) == this.mDefaultDisplayTopInset) {
            return;
        }
        this.mDefaultDisplayTopInset = i;
        SystemProperties.set(PROP_DEFAULT_DISPLAY_TOP_INSET, Integer.toString(i));
    }

    private void setStableDisplaySizeLocked(int i, int i2) {
        Point point = new Point(i, i2);
        this.mStableDisplaySize = point;
        try {
            this.mPersistentDataStore.setStableDisplaySize(point);
        } finally {
            this.mPersistentDataStore.saveIfNeeded();
        }
    }

    @VisibleForTesting
    Curve getMinimumBrightnessCurveInternal() {
        return this.mMinimumBrightnessCurve;
    }

    int getPreferredWideGamutColorSpaceIdInternal() {
        return this.mWideColorSpace.getId();
    }

    OverlayProperties getOverlaySupportInternal() {
        return this.mOverlayProperties;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x001c, code lost:
    
        r2 = -1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setUserPreferredDisplayModeInternal(int i, Display.Mode mode) {
        synchronized (this.mSyncRoot) {
            if (mode != null) {
                try {
                    if (!isResolutionAndRefreshRateValid(mode) && i == -1) {
                        throw new IllegalArgumentException("width, height and refresh rate of mode should be greater than 0 when setting the global user preferred display mode.");
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            int physicalHeight = mode.getPhysicalHeight();
            int physicalWidth = mode == null ? -1 : mode.getPhysicalWidth();
            float refreshRate = mode == null ? 0.0f : mode.getRefreshRate();
            Slog.d(TAG, "setUserPreferredDisplayMode id=" + i + " mode=" + mode + " pid=" + Binder.getCallingPid() + " uid=" + Binder.getCallingUid());
            storeModeInPersistentDataStoreLocked(i, physicalWidth, physicalHeight, refreshRate);
            if (i != -1) {
                setUserPreferredModeForDisplayLocked(i, mode);
            } else {
                this.mUserPreferredMode = mode;
                storeModeInGlobalSettingsLocked(physicalWidth, physicalHeight, refreshRate, mode);
            }
        }
    }

    private void storeModeInPersistentDataStoreLocked(int i, int i2, int i3, float f) {
        DisplayDevice deviceForDisplayLocked = getDeviceForDisplayLocked(i);
        if (deviceForDisplayLocked == null) {
            return;
        }
        try {
            this.mPersistentDataStore.setUserPreferredResolution(deviceForDisplayLocked, i2, i3);
            this.mPersistentDataStore.setUserPreferredRefreshRate(deviceForDisplayLocked, f);
        } finally {
            this.mPersistentDataStore.saveIfNeeded();
        }
    }

    private void setUserPreferredModeForDisplayLocked(int i, Display.Mode mode) {
        DisplayDevice deviceForDisplayLocked = getDeviceForDisplayLocked(i);
        if (deviceForDisplayLocked == null) {
            return;
        }
        deviceForDisplayLocked.setUserPreferredDisplayModeLocked(mode);
    }

    private void storeModeInGlobalSettingsLocked(int i, int i2, float f, final Display.Mode mode) {
        Settings.Global.putFloat(this.mContext.getContentResolver(), "user_preferred_refresh_rate", f);
        Settings.Global.putInt(this.mContext.getContentResolver(), "user_preferred_resolution_height", i2);
        Settings.Global.putInt(this.mContext.getContentResolver(), "user_preferred_resolution_width", i);
        this.mDisplayDeviceRepo.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((DisplayDevice) obj).setUserPreferredDisplayModeLocked(mode);
            }
        });
    }

    @GuardedBy({"mSyncRoot"})
    private int[] getEnabledAutoHdrTypesLocked() {
        boolean z;
        IntArray intArray = new IntArray();
        for (int i : getSupportedHdrOutputTypesInternal()) {
            int[] iArr = this.mUserDisabledHdrTypes;
            int length = iArr.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    z = false;
                    break;
                }
                if (i == iArr[i2]) {
                    z = true;
                    break;
                }
                i2++;
            }
            if (!z) {
                intArray.add(i);
            }
        }
        return intArray.toArray();
    }

    @GuardedBy({"mSyncRoot"})
    private boolean hdrConversionIntroducesLatencyLocked() {
        int preferredHdrOutputType = getHdrConversionModeSettingInternal().getPreferredHdrOutputType();
        if (preferredHdrOutputType != -1) {
            return ArrayUtils.contains(this.mInjector.getHdrOutputTypesWithLatency(), preferredHdrOutputType);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Display.Mode getUserPreferredDisplayModeInternal(int i) {
        synchronized (this.mSyncRoot) {
            if (i == -1) {
                return this.mUserPreferredMode;
            }
            DisplayDevice deviceForDisplayLocked = getDeviceForDisplayLocked(i);
            if (deviceForDisplayLocked == null) {
                return null;
            }
            return deviceForDisplayLocked.getUserPreferredDisplayModeLocked();
        }
    }

    Display.Mode getSystemPreferredDisplayModeInternal(int i) {
        synchronized (this.mSyncRoot) {
            DisplayDevice deviceForDisplayLocked = getDeviceForDisplayLocked(i);
            if (deviceForDisplayLocked == null) {
                return null;
            }
            return deviceForDisplayLocked.getSystemPreferredDisplayModeLocked();
        }
    }

    void setHdrConversionModeInternal(HdrConversionMode hdrConversionMode) {
        if (this.mInjector.getHdrOutputConversionSupport()) {
            synchronized (this.mSyncRoot) {
                if (hdrConversionMode.getConversionMode() == 2 && hdrConversionMode.getPreferredHdrOutputType() != -1) {
                    throw new IllegalArgumentException("preferredHdrOutputType must not be set if the conversion mode is HDR_CONVERSION_SYSTEM");
                }
                this.mHdrConversionMode = hdrConversionMode;
                storeHdrConversionModeLocked(hdrConversionMode);
                int[] iArr = null;
                int[] enabledAutoHdrTypesLocked = hdrConversionMode.getConversionMode() == 2 ? getEnabledAutoHdrTypesLocked() : null;
                int conversionMode = hdrConversionMode.getConversionMode();
                int preferredHdrOutputType = hdrConversionMode.getPreferredHdrOutputType();
                HdrConversionMode hdrConversionMode2 = this.mOverrideHdrConversionMode;
                if (hdrConversionMode2 == null) {
                    if (conversionMode == 3 && preferredHdrOutputType == -1) {
                        conversionMode = 1;
                    }
                    iArr = enabledAutoHdrTypesLocked;
                } else {
                    conversionMode = hdrConversionMode2.getConversionMode();
                    preferredHdrOutputType = this.mOverrideHdrConversionMode.getPreferredHdrOutputType();
                }
                this.mSystemPreferredHdrOutputType = this.mInjector.setHdrConversionMode(conversionMode, preferredHdrOutputType, iArr);
            }
        }
    }

    HdrConversionMode getHdrConversionModeSettingInternal() {
        if (!this.mInjector.getHdrOutputConversionSupport()) {
            return HDR_CONVERSION_MODE_UNSUPPORTED;
        }
        synchronized (this.mSyncRoot) {
            HdrConversionMode hdrConversionMode = this.mHdrConversionMode;
            return hdrConversionMode != null ? hdrConversionMode : new HdrConversionMode(2);
        }
    }

    HdrConversionMode getHdrConversionModeInternal() {
        if (!this.mInjector.getHdrOutputConversionSupport()) {
            return HDR_CONVERSION_MODE_UNSUPPORTED;
        }
        synchronized (this.mSyncRoot) {
            HdrConversionMode hdrConversionMode = this.mOverrideHdrConversionMode;
            if (hdrConversionMode == null) {
                hdrConversionMode = this.mHdrConversionMode;
            }
            if (hdrConversionMode != null && hdrConversionMode.getConversionMode() != 2) {
                return hdrConversionMode;
            }
            return new HdrConversionMode(2, this.mSystemPreferredHdrOutputType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getSupportedHdrOutputTypesInternal() {
        if (this.mSupportedHdrOutputType == null) {
            this.mSupportedHdrOutputType = this.mInjector.getSupportedHdrOutputTypes();
        }
        return this.mSupportedHdrOutputType;
    }

    void setShouldAlwaysRespectAppRequestedModeInternal(boolean z) {
        this.mDisplayModeDirector.setShouldAlwaysRespectAppRequestedMode(z);
    }

    boolean shouldAlwaysRespectAppRequestedModeInternal() {
        return this.mDisplayModeDirector.shouldAlwaysRespectAppRequestedMode();
    }

    void setRefreshRateSwitchingTypeInternal(int i) {
        this.mDisplayModeDirector.setModeSwitchingType(i);
    }

    int getRefreshRateSwitchingTypeInternal() {
        return this.mDisplayModeDirector.getModeSwitchingType();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DisplayDecorationSupport getDisplayDecorationSupportInternal(int i) {
        IBinder displayToken = getDisplayToken(i);
        if (displayToken == null) {
            return null;
        }
        return SurfaceControl.getDisplayDecorationSupport(displayToken);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBrightnessConfigurationForDisplayInternal(BrightnessConfiguration brightnessConfiguration, String str, int i, String str2) {
        validateBrightnessConfiguration(brightnessConfiguration);
        int userSerialNumber = getUserManager().getUserSerialNumber(i);
        synchronized (this.mSyncRoot) {
            try {
                DisplayDevice byUniqueIdLocked = this.mDisplayDeviceRepo.getByUniqueIdLocked(str);
                if (byUniqueIdLocked == null) {
                    return;
                }
                if (this.mLogicalDisplayMapper.getDisplayLocked(byUniqueIdLocked) != null && this.mLogicalDisplayMapper.getDisplayLocked(byUniqueIdLocked).getDisplayInfoLocked().type == 1 && brightnessConfiguration != null) {
                    FrameworkStatsLog.write(FrameworkStatsLog.BRIGHTNESS_CONFIGURATION_UPDATED, (float[]) brightnessConfiguration.getCurve().first, (float[]) brightnessConfiguration.getCurve().second, str);
                }
                this.mPersistentDataStore.setBrightnessConfigurationForDisplayLocked(brightnessConfiguration, byUniqueIdLocked, userSerialNumber, str2);
                this.mPersistentDataStore.saveIfNeeded();
                if (i != this.mCurrentUserId) {
                    return;
                }
                DisplayPowerController dpcFromUniqueIdLocked = getDpcFromUniqueIdLocked(str);
                if (dpcFromUniqueIdLocked != null) {
                    dpcFromUniqueIdLocked.setBrightnessConfiguration(brightnessConfiguration, true);
                }
            } finally {
                this.mPersistentDataStore.saveIfNeeded();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DisplayPowerController getDpcFromUniqueIdLocked(String str) {
        LogicalDisplay displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(this.mDisplayDeviceRepo.getByUniqueIdLocked(str));
        if (displayLocked == null) {
            return null;
        }
        return this.mDisplayPowerControllers.get(displayLocked.getDisplayIdLocked());
    }

    @VisibleForTesting
    void validateBrightnessConfiguration(BrightnessConfiguration brightnessConfiguration) {
        if (brightnessConfiguration != null && isBrightnessConfigurationTooDark(brightnessConfiguration)) {
            throw new IllegalArgumentException("brightness curve is too dark");
        }
    }

    private boolean isBrightnessConfigurationTooDark(BrightnessConfiguration brightnessConfiguration) {
        Pair curve = brightnessConfiguration.getCurve();
        float[] fArr = (float[]) curve.first;
        float[] fArr2 = (float[]) curve.second;
        for (int i = 0; i < fArr.length; i++) {
            if (fArr2[i] < this.mMinimumBrightnessSpline.interpolate(fArr[i])) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadBrightnessConfigurations() {
        final int userSerialNumber = getUserManager().getUserSerialNumber(this.mContext.getUserId());
        synchronized (this.mSyncRoot) {
            this.mLogicalDisplayMapper.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DisplayManagerService.this.lambda$loadBrightnessConfigurations$9(userSerialNumber, (LogicalDisplay) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadBrightnessConfigurations$9(int i, LogicalDisplay logicalDisplay) {
        DisplayPowerController displayPowerController;
        BrightnessConfiguration brightnessConfigForDisplayWithPdsFallbackLocked = getBrightnessConfigForDisplayWithPdsFallbackLocked(logicalDisplay.getPrimaryDisplayDeviceLocked().getUniqueId(), i);
        if (brightnessConfigForDisplayWithPdsFallbackLocked == null || (displayPowerController = this.mDisplayPowerControllers.get(logicalDisplay.getDisplayIdLocked())) == null) {
            return;
        }
        displayPowerController.setBrightnessConfiguration(brightnessConfigForDisplayWithPdsFallbackLocked, false);
    }

    private void performTraversalLocked(final SurfaceControl.Transaction transaction) {
        clearViewportsLocked();
        this.mLogicalDisplayMapper.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayManagerService.this.lambda$performTraversalLocked$10(transaction, (LogicalDisplay) obj);
            }
        }, true);
        if (this.mInputManagerInternal != null) {
            this.mHandler.sendEmptyMessage(5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performTraversalLocked$10(SurfaceControl.Transaction transaction, LogicalDisplay logicalDisplay) {
        DisplayDevice primaryDisplayDeviceLocked = logicalDisplay.getPrimaryDisplayDeviceLocked();
        if (primaryDisplayDeviceLocked != null) {
            configureDisplayLocked(transaction, primaryDisplayDeviceLocked);
            primaryDisplayDeviceLocked.performTraversalLocked(transaction);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00a2 A[Catch: all -> 0x00e0, TryCatch #0 {, blocks: (B:4:0x000e, B:6:0x0016, B:9:0x0018, B:11:0x0020, B:13:0x0024, B:14:0x004a, B:18:0x0057, B:20:0x0061, B:21:0x0086, B:25:0x009c, B:27:0x00a2, B:29:0x00a7, B:32:0x00ae, B:33:0x00b1, B:35:0x00b5, B:37:0x00b7, B:41:0x00bf, B:42:0x00de, B:47:0x00d5, B:49:0x0066), top: B:3:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00ae A[Catch: all -> 0x00e0, TryCatch #0 {, blocks: (B:4:0x000e, B:6:0x0016, B:9:0x0018, B:11:0x0020, B:13:0x0024, B:14:0x004a, B:18:0x0057, B:20:0x0061, B:21:0x0086, B:25:0x009c, B:27:0x00a2, B:29:0x00a7, B:32:0x00ae, B:33:0x00b1, B:35:0x00b5, B:37:0x00b7, B:41:0x00bf, B:42:0x00de, B:47:0x00d5, B:49:0x0066), top: B:3:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00b5 A[Catch: all -> 0x00e0, DONT_GENERATE, TryCatch #0 {, blocks: (B:4:0x000e, B:6:0x0016, B:9:0x0018, B:11:0x0020, B:13:0x0024, B:14:0x004a, B:18:0x0057, B:20:0x0061, B:21:0x0086, B:25:0x009c, B:27:0x00a2, B:29:0x00a7, B:32:0x00ae, B:33:0x00b1, B:35:0x00b5, B:37:0x00b7, B:41:0x00bf, B:42:0x00de, B:47:0x00d5, B:49:0x0066), top: B:3:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00b7 A[Catch: all -> 0x00e0, TryCatch #0 {, blocks: (B:4:0x000e, B:6:0x0016, B:9:0x0018, B:11:0x0020, B:13:0x0024, B:14:0x004a, B:18:0x0057, B:20:0x0061, B:21:0x0086, B:25:0x009c, B:27:0x00a2, B:29:0x00a7, B:32:0x00ae, B:33:0x00b1, B:35:0x00b5, B:37:0x00b7, B:41:0x00bf, B:42:0x00de, B:47:0x00d5, B:49:0x0066), top: B:3:0x000e }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void setDisplayPropertiesInternal(int i, boolean z, float f, int i2, float f2, float f3, boolean z2, boolean z3, boolean z4) {
        boolean z5;
        int i3;
        boolean z6;
        HdrConversionMode hdrConversionMode;
        this.requestedMaxRefreshRate = f3;
        synchronized (this.mSyncRoot) {
            LogicalDisplay displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(i);
            if (displayLocked == null) {
                return;
            }
            if (displayLocked.hasContentLocked() != z) {
                if (DEBUG) {
                    Slog.d(TAG, "Display " + i + " hasContent flag changed: hasContent=" + z + ", inTraversal=" + z4);
                }
                displayLocked.setHasContentLocked(z);
                z5 = true;
            } else {
                z5 = false;
            }
            if (i2 == 0 && f != 0.0f) {
                Display.Mode findDefaultModeByRefreshRate = displayLocked.getDisplayInfoLocked().findDefaultModeByRefreshRate(f);
                if (findDefaultModeByRefreshRate != null) {
                    i3 = findDefaultModeByRefreshRate.getModeId();
                    this.mDisplayModeDirector.getAppRequestObserver().setAppRequest(i, i3, f2, f3);
                    z6 = !isMinimalPostProcessingAllowed() && z2;
                    if (displayLocked.getRequestedMinimalPostProcessingLocked() != z6) {
                        displayLocked.setRequestedMinimalPostProcessingLocked(z6);
                        r9 = z6 ? hdrConversionIntroducesLatencyLocked() : false;
                        z5 = true;
                    }
                    if (z5) {
                        scheduleTraversalLocked(z4);
                    }
                    hdrConversionMode = this.mHdrConversionMode;
                    if (hdrConversionMode != null) {
                        return;
                    }
                    HdrConversionMode hdrConversionMode2 = this.mOverrideHdrConversionMode;
                    if (hdrConversionMode2 == null && (z3 || r9)) {
                        this.mOverrideHdrConversionMode = new HdrConversionMode(1);
                        setHdrConversionModeInternal(this.mHdrConversionMode);
                        handleLogicalDisplayChangedLocked(displayLocked);
                    } else if (hdrConversionMode2 != null && !z3 && !r9) {
                        this.mOverrideHdrConversionMode = null;
                        setHdrConversionModeInternal(hdrConversionMode);
                        handleLogicalDisplayChangedLocked(displayLocked);
                    }
                    return;
                }
                Slog.e(TAG, "Couldn't find a mode for the requestedRefreshRate: " + f + " on Display: " + i);
            }
            i3 = i2;
            this.mDisplayModeDirector.getAppRequestObserver().setAppRequest(i, i3, f2, f3);
            if (isMinimalPostProcessingAllowed()) {
            }
            if (displayLocked.getRequestedMinimalPostProcessingLocked() != z6) {
            }
            if (z5) {
            }
            hdrConversionMode = this.mHdrConversionMode;
            if (hdrConversionMode != null) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisplayOffsetsInternal(int i, int i2, int i3) {
        synchronized (this.mSyncRoot) {
            LogicalDisplay displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(i);
            if (displayLocked == null) {
                return;
            }
            if (displayLocked.getDisplayOffsetXLocked() != i2 || displayLocked.getDisplayOffsetYLocked() != i3) {
                if (DEBUG) {
                    Slog.d(TAG, "Display " + i + " burn-in offset set to (" + i2 + ", " + i3 + ")");
                }
                displayLocked.setDisplayOffsetsLocked(i2, i3);
                scheduleTraversalLocked(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisplayScalingDisabledInternal(int i, boolean z) {
        synchronized (this.mSyncRoot) {
            LogicalDisplay displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(i);
            if (displayLocked == null) {
                return;
            }
            if (displayLocked.isDisplayScalingDisabled() != z) {
                if (DEBUG) {
                    Slog.d(TAG, "Display " + i + " content scaling disabled = " + z);
                }
                displayLocked.setDisplayScalingDisabledLocked(z);
                scheduleTraversalLocked(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisplayAccessUIDsInternal(SparseArray<IntArray> sparseArray) {
        synchronized (this.mSyncRoot) {
            this.mDisplayAccessUIDs.clear();
            for (int size = sparseArray.size() - 1; size >= 0; size--) {
                this.mDisplayAccessUIDs.append(sparseArray.keyAt(size), sparseArray.valueAt(size));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUidPresentOnDisplayInternal(int i, int i2) {
        boolean z;
        synchronized (this.mSyncRoot) {
            IntArray intArray = this.mDisplayAccessUIDs.get(i2);
            z = (intArray == null || intArray.indexOf(i) == -1) ? false : true;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IBinder getDisplayToken(int i) {
        DisplayDevice primaryDisplayDeviceLocked;
        synchronized (this.mSyncRoot) {
            LogicalDisplay displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(i);
            if (displayLocked == null || (primaryDisplayDeviceLocked = displayLocked.getPrimaryDisplayDeviceLocked()) == null) {
                return null;
            }
            return primaryDisplayDeviceLocked.getDisplayTokenLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ScreenCapture.ScreenshotHardwareBuffer systemScreenshotInternal(int i) {
        synchronized (this.mSyncRoot) {
            IBinder displayToken = getDisplayToken(i);
            if (displayToken == null) {
                return null;
            }
            LogicalDisplay displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(i);
            if (displayLocked == null) {
                return null;
            }
            DisplayInfo displayInfoLocked = displayLocked.getDisplayInfoLocked();
            return ScreenCapture.captureDisplay(new ScreenCapture.DisplayCaptureArgs.Builder(displayToken).setSize(displayInfoLocked.getNaturalWidth(), displayInfoLocked.getNaturalHeight()).setCaptureSecureLayers(true).setAllowProtected(true).build());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ScreenCapture.ScreenshotHardwareBuffer userScreenshotInternal(int i) {
        synchronized (this.mSyncRoot) {
            IBinder displayToken = getDisplayToken(i);
            if (displayToken == null) {
                return null;
            }
            return ScreenCapture.captureDisplay(new ScreenCapture.DisplayCaptureArgs.Builder(displayToken).build());
        }
    }

    @VisibleForTesting
    DisplayedContentSamplingAttributes getDisplayedContentSamplingAttributesInternal(int i) {
        IBinder displayToken = getDisplayToken(i);
        if (displayToken == null) {
            return null;
        }
        return SurfaceControl.getDisplayedContentSamplingAttributes(displayToken);
    }

    @VisibleForTesting
    boolean setDisplayedContentSamplingEnabledInternal(int i, boolean z, int i2, int i3) {
        IBinder displayToken = getDisplayToken(i);
        if (displayToken == null) {
            return false;
        }
        return SurfaceControl.setDisplayedContentSamplingEnabled(displayToken, z, i2, i3);
    }

    @VisibleForTesting
    DisplayedContentSample getDisplayedContentSampleInternal(int i, long j, long j2) {
        IBinder displayToken = getDisplayToken(i);
        if (displayToken == null) {
            return null;
        }
        return SurfaceControl.getDisplayedContentSample(displayToken, j, j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetBrightnessConfigurations() {
        this.mPersistentDataStore.setBrightnessConfigurationForUser(null, this.mContext.getUserId(), this.mContext.getPackageName());
        this.mLogicalDisplayMapper.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayManagerService.this.lambda$resetBrightnessConfigurations$11((LogicalDisplay) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resetBrightnessConfigurations$11(LogicalDisplay logicalDisplay) {
        if (logicalDisplay.getDisplayInfoLocked().type != 1) {
            return;
        }
        setBrightnessConfigurationForDisplayInternal(null, logicalDisplay.getPrimaryDisplayDeviceLocked().getUniqueId(), this.mContext.getUserId(), this.mContext.getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAutoBrightnessLoggingEnabled(boolean z) {
        synchronized (this.mSyncRoot) {
            DisplayPowerController displayPowerController = this.mDisplayPowerControllers.get(0);
            if (displayPowerController != null) {
                displayPowerController.setAutoBrightnessLoggingEnabled(z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDisplayWhiteBalanceLoggingEnabled(boolean z) {
        synchronized (this.mSyncRoot) {
            DisplayPowerController displayPowerController = this.mDisplayPowerControllers.get(0);
            if (displayPowerController != null) {
                displayPowerController.setDisplayWhiteBalanceLoggingEnabled(z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDisplayModeDirectorLoggingEnabled(boolean z) {
        synchronized (this.mSyncRoot) {
            this.mDisplayModeDirector.setLoggingEnabled(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Display.Mode getActiveDisplayModeAtStart(int i) {
        synchronized (this.mSyncRoot) {
            DisplayDevice deviceForDisplayLocked = getDeviceForDisplayLocked(i);
            if (deviceForDisplayLocked == null) {
                return null;
            }
            return deviceForDisplayLocked.getActiveDisplayModeAtStartLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAmbientColorTemperatureOverride(float f) {
        synchronized (this.mSyncRoot) {
            DisplayPowerController displayPowerController = this.mDisplayPowerControllers.get(0);
            if (displayPowerController != null) {
                displayPowerController.setAmbientColorTemperatureOverride(f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDockedAndIdleEnabled(boolean z, int i) {
        synchronized (this.mSyncRoot) {
            DisplayPowerController displayPowerController = this.mDisplayPowerControllers.get(i);
            if (displayPowerController != null) {
                displayPowerController.setAutomaticScreenBrightnessMode(z);
            }
        }
    }

    private void clearViewportsLocked() {
        this.mViewports.clear();
    }

    private Optional<Integer> getViewportType(DisplayDeviceInfo displayDeviceInfo) {
        int i = displayDeviceInfo.touch;
        if (i == 1) {
            return Optional.of(1);
        }
        if (i == 2) {
            return Optional.of(2);
        }
        if (i == 3 && !TextUtils.isEmpty(displayDeviceInfo.uniqueId)) {
            return Optional.of(3);
        }
        if (DEBUG) {
            Slog.w(TAG, "Display " + displayDeviceInfo + " does not support input device matching.");
        }
        return Optional.empty();
    }

    private void configureDisplayLocked(SurfaceControl.Transaction transaction, DisplayDevice displayDevice) {
        LogicalDisplay displayLocked;
        DisplayDeviceInfo displayDeviceInfoLocked = displayDevice.getDisplayDeviceInfoLocked();
        LogicalDisplay displayLocked2 = this.mLogicalDisplayMapper.getDisplayLocked(displayDevice, true);
        int bondDisplayIdLocked = ((IMirageDisplayManagerExt) ExtLoader.type(IMirageDisplayManagerExt.class).create()).getBondDisplayIdLocked(displayLocked2 != null ? displayLocked2.getDisplayIdLocked() : -1, displayDeviceInfoLocked, displayLocked2 != null ? displayLocked2.hasContentLocked() : false);
        if (bondDisplayIdLocked != -1 && (displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(bondDisplayIdLocked)) != null) {
            displayLocked2 = displayLocked;
        }
        if (displayLocked2 == null) {
            Slog.w(TAG, "Missing logical display to use for physical display device: " + displayDevice.getDisplayDeviceInfoLocked());
            return;
        }
        if (PANIC_DEBUG) {
            Slog.d(TAG, "configureDisplayLocked state=" + Display.stateToString(displayDeviceInfoLocked.state) + " logicalDisplay=" + displayLocked2.toStringMini());
        }
        displayLocked2.configureDisplayLocked(transaction, displayDevice, displayDeviceInfoLocked.state == 1);
        Optional<Integer> viewportType = getViewportType(displayDeviceInfoLocked);
        if (viewportType.isPresent()) {
            populateViewportLocked(viewportType.get().intValue(), displayLocked2.getDisplayIdLocked(), displayDevice, displayDeviceInfoLocked);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSpecBrightnessInternal(int i, String str, int i2) {
        synchronized (this.mSyncRoot) {
            this.mDmsExt.setSpecBrightness(i, str, i2);
        }
    }

    private DisplayViewport getViewportLocked(int i, String str) {
        if (i != 1 && i != 2 && i != 3) {
            Slog.wtf(TAG, "Cannot call getViewportByTypeLocked for type " + DisplayViewport.typeToString(i));
            return null;
        }
        int size = this.mViewports.size();
        for (int i2 = 0; i2 < size; i2++) {
            DisplayViewport displayViewport = this.mViewports.get(i2);
            if (displayViewport.type == i && str.equals(displayViewport.uniqueId)) {
                return displayViewport;
            }
        }
        DisplayViewport displayViewport2 = new DisplayViewport();
        displayViewport2.type = i;
        displayViewport2.uniqueId = str;
        this.mViewports.add(displayViewport2);
        return displayViewport2;
    }

    private void populateViewportLocked(int i, int i2, DisplayDevice displayDevice, DisplayDeviceInfo displayDeviceInfo) {
        DisplayViewport viewportLocked = getViewportLocked(i, displayDeviceInfo.uniqueId);
        displayDevice.populateViewportLocked(viewportLocked);
        viewportLocked.valid = true;
        viewportLocked.displayId = i2;
        viewportLocked.isActive = Display.isActiveState(displayDeviceInfo.state);
    }

    private void updateViewportPowerStateLocked(LogicalDisplay logicalDisplay) {
        DisplayDeviceInfo displayDeviceInfoLocked = logicalDisplay.getPrimaryDisplayDeviceLocked().getDisplayDeviceInfoLocked();
        Optional<Integer> viewportType = getViewportType(displayDeviceInfoLocked);
        if (viewportType.isPresent()) {
            Iterator<DisplayViewport> it = this.mViewports.iterator();
            while (it.hasNext()) {
                DisplayViewport next = it.next();
                if (next.type == viewportType.get().intValue() && displayDeviceInfoLocked.uniqueId.equals(next.uniqueId)) {
                    next.isActive = Display.isActiveState(displayDeviceInfoLocked.state);
                }
            }
            if (this.mInputManagerInternal != null) {
                this.mHandler.sendEmptyMessage(5);
            }
        }
    }

    private void sendDisplayEventLocked(LogicalDisplay logicalDisplay, int i) {
        if (logicalDisplay.isEnabledLocked()) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(3, logicalDisplay.getDisplayIdLocked(), i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendDisplayGroupEvent(int i, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(8, i, i2));
    }

    private void sendDisplayEventFrameRateOverrideLocked(int i) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(7, i, 2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleTraversalLocked(boolean z) {
        if (this.mPendingTraversal || this.mWindowManagerInternal == null) {
            return;
        }
        this.mDmsExt.scheduleTraversalLocked(z);
        if (!z) {
            this.mPendingTraversalCompleted = false;
        }
        this.mPendingTraversal = true;
        if (z) {
            return;
        }
        this.mHandler.sendEmptyMessage(4);
    }

    private boolean isUidCached(int i) {
        ActivityManagerInternal activityManagerInternal = this.mActivityManagerInternal;
        return activityManagerInternal != null && ActivityManager.RunningAppProcessInfo.procStateToImportance(activityManagerInternal.getUidProcessState(i)) >= 400;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deliverDisplayEvent(int i, ArraySet<Integer> arraySet, int i2) {
        if (i2 != 4 || PANIC_DEBUG) {
            Slog.d(TAG, "Delivering display event: displayId=" + i + ", event=" + i2);
        }
        synchronized (this.mSyncRoot) {
            int size = this.mCallbacks.size();
            this.mTempCallbacks.clear();
            for (int i3 = 0; i3 < size; i3++) {
                if (arraySet == null || arraySet.contains(Integer.valueOf(this.mCallbacks.valueAt(i3).mUid))) {
                    this.mTempCallbacks.add(this.mCallbacks.valueAt(i3));
                }
            }
        }
        for (int i4 = 0; i4 < this.mTempCallbacks.size(); i4++) {
            CallbackRecord callbackRecord = this.mTempCallbacks.get(i4);
            int i5 = callbackRecord.mUid;
            int i6 = callbackRecord.mPid;
            if (isUidCached(i5)) {
                synchronized (this.mPendingCallbackSelfLocked) {
                    SparseArray<PendingCallback> sparseArray = this.mPendingCallbackSelfLocked.get(i5);
                    if (sparseArray == null) {
                        SparseArray<PendingCallback> sparseArray2 = new SparseArray<>();
                        sparseArray2.put(i6, new PendingCallback(callbackRecord, i, i2));
                        this.mPendingCallbackSelfLocked.put(i5, sparseArray2);
                    } else {
                        PendingCallback pendingCallback = sparseArray.get(i6);
                        if (pendingCallback == null) {
                            sparseArray.put(i6, new PendingCallback(callbackRecord, i, i2));
                        } else {
                            pendingCallback.addDisplayEvent(i, i2);
                        }
                    }
                }
            } else {
                callbackRecord.notifyDisplayEventAsync(i, i2);
            }
        }
        this.mTempCallbacks.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deliverDisplayGroupEvent(int i, int i2) {
        if (DEBUG) {
            Slog.d(TAG, "Delivering display group event: groupId=" + i + ", event=" + i2);
        }
        if (i2 == 1) {
            Iterator<DisplayManagerInternal.DisplayGroupListener> it = this.mDisplayGroupListeners.iterator();
            while (it.hasNext()) {
                it.next().onDisplayGroupAdded(i);
            }
        } else if (i2 == 2) {
            Iterator<DisplayManagerInternal.DisplayGroupListener> it2 = this.mDisplayGroupListeners.iterator();
            while (it2.hasNext()) {
                it2.next().onDisplayGroupChanged(i);
            }
        } else {
            if (i2 != 3) {
                return;
            }
            Iterator<DisplayManagerInternal.DisplayGroupListener> it3 = this.mDisplayGroupListeners.iterator();
            while (it3.hasNext()) {
                it3.next().onDisplayGroupRemoved(i);
            }
        }
    }

    private IMediaProjectionManager getProjectionService() {
        if (this.mProjectionService == null) {
            this.mProjectionService = this.mInjector.getProjectionService();
        }
        return this.mProjectionService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UserManager getUserManager() {
        return (UserManager) this.mContext.getSystemService(UserManager.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpInternal(final PrintWriter printWriter, String[] strArr) {
        BrightnessTracker brightnessTracker;
        synchronized (this.mSyncDump) {
            if (this.mDumpInProgress) {
                printWriter.println("One dump is in service already.");
                return;
            }
            this.mDumpInProgress = true;
            printWriter.println("DISPLAY MANAGER (dumpsys display)");
            synchronized (this.mSyncRoot) {
                brightnessTracker = this.mBrightnessTracker;
                printWriter.println("  mSafeMode=" + this.mSafeMode);
                printWriter.println("  mPendingTraversal=" + this.mPendingTraversal);
                printWriter.println("  mViewports=" + this.mViewports);
                printWriter.println("  mDefaultDisplayDefaultColorMode=" + this.mDefaultDisplayDefaultColorMode);
                printWriter.println("  mWifiDisplayScanRequestCount=" + this.mWifiDisplayScanRequestCount);
                printWriter.println("  mStableDisplaySize=" + this.mStableDisplaySize);
                printWriter.println("  mMinimumBrightnessCurve=" + this.mMinimumBrightnessCurve);
                if (this.mUserPreferredMode != null) {
                    printWriter.println(" mUserPreferredMode=" + this.mUserPreferredMode);
                }
                printWriter.println();
                if (!this.mAreUserDisabledHdrTypesAllowed) {
                    printWriter.println("  mUserDisabledHdrTypes: size=" + this.mUserDisabledHdrTypes.length);
                    int[] iArr = this.mUserDisabledHdrTypes;
                    int length = iArr.length;
                    for (int i = 0; i < length; i++) {
                        printWriter.println("  " + iArr[i]);
                    }
                }
                if (this.mHdrConversionMode != null) {
                    printWriter.println("  mHdrConversionMode=" + this.mHdrConversionMode);
                }
                printWriter.println();
                int size = this.mDisplayStates.size();
                printWriter.println("Display States: size=" + size);
                for (int i2 = 0; i2 < size; i2++) {
                    int keyAt = this.mDisplayStates.keyAt(i2);
                    int valueAt = this.mDisplayStates.valueAt(i2);
                    BrightnessPair valueAt2 = this.mDisplayBrightnesses.valueAt(i2);
                    printWriter.println("  Display Id=" + keyAt);
                    printWriter.println("  Display State=" + Display.stateToString(valueAt));
                    printWriter.println("  Display Brightness=" + valueAt2.brightness);
                    printWriter.println("  Display SdrBrightness=" + valueAt2.sdrBrightness);
                }
                final PrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "    ");
                indentingPrintWriter.increaseIndent();
                printWriter.println();
                printWriter.println("Display Adapters: size=" + this.mDisplayAdapters.size());
                Iterator<DisplayAdapter> it = this.mDisplayAdapters.iterator();
                while (it.hasNext()) {
                    DisplayAdapter next = it.next();
                    printWriter.println("  " + next.getName());
                    next.dumpLocked(indentingPrintWriter);
                }
                printWriter.println();
                printWriter.println("Display Devices: size=" + this.mDisplayDeviceRepo.sizeLocked());
                this.mDisplayDeviceRepo.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda4
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DisplayManagerService.lambda$dumpInternal$12(printWriter, indentingPrintWriter, (DisplayDevice) obj);
                    }
                });
                printWriter.println();
                this.mLogicalDisplayMapper.dumpLocked(printWriter);
                int size2 = this.mCallbacks.size();
                printWriter.println();
                printWriter.println("Callbacks: size=" + size2);
                for (int i3 = 0; i3 < size2; i3++) {
                    CallbackRecord valueAt3 = this.mCallbacks.valueAt(i3);
                    printWriter.println("  " + i3 + ": mPid=" + valueAt3.mPid + ", mWifiDisplayScanRequested=" + valueAt3.mWifiDisplayScanRequested);
                }
                int size3 = this.mDisplayPowerControllers.size();
                printWriter.println();
                printWriter.println("Display Power Controllers: size=" + size3);
                for (int i4 = 0; i4 < size3; i4++) {
                    this.mDisplayPowerControllers.valueAt(i4).dump(printWriter);
                }
                printWriter.println();
                this.mPersistentDataStore.dump(printWriter);
                int size4 = this.mDisplayWindowPolicyControllers.size();
                printWriter.println();
                printWriter.println("Display Window Policy Controllers: size=" + size4);
                for (int i5 = 0; i5 < size4; i5++) {
                    printWriter.print("Display " + this.mDisplayWindowPolicyControllers.keyAt(i5) + ":");
                    ((DisplayWindowPolicyController) this.mDisplayWindowPolicyControllers.valueAt(i5).second).dump("  ", printWriter);
                }
            }
            if (brightnessTracker != null) {
                printWriter.println();
                brightnessTracker.dump(printWriter);
            }
            printWriter.println();
            this.mDisplayModeDirector.dump(printWriter);
            this.mBrightnessSynchronizer.dump(printWriter);
            synchronized (this.mSyncDump) {
                this.mDumpInProgress = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dumpInternal$12(PrintWriter printWriter, IndentingPrintWriter indentingPrintWriter, DisplayDevice displayDevice) {
        printWriter.println("  " + displayDevice.getDisplayDeviceInfoLocked());
        displayDevice.dumpLocked(indentingPrintWriter);
    }

    private static float[] getFloatArray(TypedArray typedArray) {
        int length = typedArray.length();
        float[] fArr = new float[length];
        for (int i = 0; i < length; i++) {
            fArr[i] = typedArray.getFloat(i, Float.NaN);
        }
        typedArray.recycle();
        return fArr;
    }

    private static boolean isResolutionAndRefreshRateValid(Display.Mode mode) {
        return mode.getPhysicalWidth() > 0 && mode.getPhysicalHeight() > 0 && mode.getRefreshRate() > 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector {
        long getDefaultDisplayDelayTimeout() {
            return 10000L;
        }

        Injector() {
        }

        VirtualDisplayAdapter getVirtualDisplayAdapter(SyncRoot syncRoot, Context context, Handler handler, DisplayAdapter.Listener listener) {
            return new VirtualDisplayAdapter(syncRoot, context, handler, listener);
        }

        LocalDisplayAdapter getLocalDisplayAdapter(SyncRoot syncRoot, Context context, Handler handler, DisplayAdapter.Listener listener) {
            return new LocalDisplayAdapter(syncRoot, context, handler, listener);
        }

        int setHdrConversionMode(int i, int i2, int[] iArr) {
            return DisplayControl.setHdrConversionMode(i, i2, iArr);
        }

        int[] getSupportedHdrOutputTypes() {
            return DisplayControl.getSupportedHdrOutputTypes();
        }

        int[] getHdrOutputTypesWithLatency() {
            return DisplayControl.getHdrOutputTypesWithLatency();
        }

        boolean getHdrOutputConversionSupport() {
            return DisplayControl.getHdrOutputConversionSupport();
        }

        IMediaProjectionManager getProjectionService() {
            return IMediaProjectionManager.Stub.asInterface(ServiceManager.getService("media_projection"));
        }
    }

    @VisibleForTesting
    DisplayDeviceInfo getDisplayDeviceInfoInternal(int i) {
        synchronized (this.mSyncRoot) {
            LogicalDisplay displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(i);
            if (displayLocked == null) {
                return null;
            }
            return displayLocked.getPrimaryDisplayDeviceLocked().getDisplayDeviceInfoLocked();
        }
    }

    @VisibleForTesting
    Surface getVirtualDisplaySurfaceInternal(IBinder iBinder) {
        synchronized (this.mSyncRoot) {
            VirtualDisplayAdapter virtualDisplayAdapter = this.mVirtualDisplayAdapter;
            if (virtualDisplayAdapter == null) {
                return null;
            }
            return virtualDisplayAdapter.getVirtualDisplaySurfaceLocked(iBinder);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initializeDisplayPowerControllersLocked() {
        this.mLogicalDisplayMapper.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayManagerService.this.addDisplayPowerControllerLocked((LogicalDisplay) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    @RequiresPermission("android.permission.READ_DEVICE_CONFIG")
    public void addDisplayPowerControllerLocked(final LogicalDisplay logicalDisplay) {
        DisplayPowerController displayPowerController;
        if (this.mPowerHandler == null) {
            return;
        }
        DisplayDeviceInfo displayDeviceInfoLocked = logicalDisplay.getPrimaryDisplayDeviceLocked().getDisplayDeviceInfoLocked();
        int i = displayDeviceInfoLocked != null ? displayDeviceInfoLocked.type : 0;
        if (i != 1) {
            if (i == 0) {
                Slog.e(TAG, "maybe display init error display=" + logicalDisplay);
                return;
            }
            return;
        }
        if (this.mBrightnessTracker == null && logicalDisplay.getDisplayIdLocked() == 0) {
            this.mBrightnessTracker = new BrightnessTracker(this.mContext, null);
        }
        BrightnessSetting brightnessSetting = new BrightnessSetting(this.mPersistentDataStore, logicalDisplay, this.mSyncRoot);
        HighBrightnessModeMetadata highBrightnessModeMetadataLocked = this.mHighBrightnessModeMetadataMapper.getHighBrightnessModeMetadataLocked(logicalDisplay);
        if (highBrightnessModeMetadataLocked == null) {
            Slog.wtf(TAG, "High Brightness Mode Metadata is null in DisplayManagerService for display: " + logicalDisplay.getDisplayIdLocked());
            return;
        }
        if (DeviceConfig.getBoolean("display_manager", "use_newly_structured_display_power_controller", true)) {
            displayPowerController = new DisplayPowerController(this.mContext, null, this.mDisplayPowerCallbacks, this.mPowerHandler, this.mSensorManager, this.mDisplayBlanker, logicalDisplay, this.mBrightnessTracker, brightnessSetting, new Runnable() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayManagerService.this.lambda$addDisplayPowerControllerLocked$13(logicalDisplay);
                }
            }, highBrightnessModeMetadataLocked, this.mBootCompleted);
        } else {
            displayPowerController = new DisplayPowerController(this.mContext, null, this.mDisplayPowerCallbacks, this.mPowerHandler, this.mSensorManager, this.mDisplayBlanker, logicalDisplay, this.mBrightnessTracker, brightnessSetting, new Runnable() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayManagerService.this.lambda$addDisplayPowerControllerLocked$14(logicalDisplay);
                }
            }, highBrightnessModeMetadataLocked, this.mBootCompleted);
        }
        this.mDisplayPowerControllers.append(logicalDisplay.getDisplayIdLocked(), displayPowerController);
        displayPowerController.getWrapper().setLogicalDisplayMapper(this.mLogicalDisplayMapper);
        this.mDmsExt.addDisplayPowerControllerLocked(logicalDisplay, displayPowerController);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleBrightnessChange, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$addDisplayPowerControllerLocked$14(LogicalDisplay logicalDisplay) {
        synchronized (this.mSyncRoot) {
            sendDisplayEventLocked(logicalDisplay, 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DisplayDevice getDeviceForDisplayLocked(int i) {
        LogicalDisplay displayLocked = this.mLogicalDisplayMapper.getDisplayLocked(i);
        if (displayLocked == null) {
            return null;
        }
        return displayLocked.getPrimaryDisplayDeviceLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BrightnessConfiguration getBrightnessConfigForDisplayWithPdsFallbackLocked(String str, int i) {
        BrightnessConfiguration brightnessConfigurationForDisplayLocked = this.mPersistentDataStore.getBrightnessConfigurationForDisplayLocked(str, i);
        return brightnessConfigurationForDisplayLocked == null ? this.mPersistentDataStore.getBrightnessConfiguration(i) : brightnessConfigurationForDisplayLocked;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DisplayManagerHandler extends Handler {
        public DisplayManagerHandler(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            boolean z;
            switch (message.what) {
                case 1:
                    DisplayManagerService.this.registerDefaultDisplayAdapters();
                    return;
                case 2:
                    DisplayManagerService.this.registerAdditionalDisplayAdapters();
                    return;
                case 3:
                    DisplayManagerService.this.deliverDisplayEvent(message.arg1, null, message.arg2);
                    return;
                case 4:
                    Slog.d(DisplayManagerService.TAG, "MSG_REQUEST_TRAVERSAL pendingCompleted=" + DisplayManagerService.this.mPendingTraversalCompleted);
                    DisplayManagerService.this.mPendingTraversalCompleted = true;
                    DisplayManagerService.this.mWindowManagerInternal.requestTraversalFromDisplayManager();
                    return;
                case 5:
                    synchronized (DisplayManagerService.this.mSyncRoot) {
                        z = !DisplayManagerService.this.mTempViewports.equals(DisplayManagerService.this.mViewports);
                        if (z) {
                            DisplayManagerService.this.mTempViewports.clear();
                            Iterator it = DisplayManagerService.this.mViewports.iterator();
                            while (it.hasNext()) {
                                DisplayManagerService.this.mTempViewports.add(((DisplayViewport) it.next()).makeCopy());
                            }
                        }
                    }
                    if (z) {
                        DisplayManagerService.this.mInputManagerInternal.setDisplayViewports(DisplayManagerService.this.mTempViewports);
                        return;
                    }
                    return;
                case 6:
                    DisplayManagerService.this.loadBrightnessConfigurations();
                    return;
                case 7:
                    synchronized (DisplayManagerService.this.mSyncRoot) {
                        LogicalDisplay displayLocked = DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(message.arg1);
                        if (displayLocked != null) {
                            ArraySet<Integer> pendingFrameRateOverrideUids = displayLocked.getPendingFrameRateOverrideUids();
                            displayLocked.clearPendingFrameRateOverrideUids();
                            DisplayManagerService.this.deliverDisplayEvent(message.arg1, pendingFrameRateOverrideUids, message.arg2);
                        }
                    }
                    return;
                case 8:
                    DisplayManagerService.this.deliverDisplayGroupEvent(message.arg1, message.arg2);
                    return;
                case 9:
                    DisplayManagerService.this.mWindowManagerInternal.onDisplayManagerReceivedDeviceState(message.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class LogicalDisplayListener implements LogicalDisplayMapper.Listener {
        private LogicalDisplayListener() {
        }

        @Override // com.android.server.display.LogicalDisplayMapper.Listener
        public void onLogicalDisplayEventLocked(LogicalDisplay logicalDisplay, int i) {
            switch (i) {
                case 1:
                    DisplayManagerService.this.handleLogicalDisplayAddedLocked(logicalDisplay);
                    return;
                case 2:
                    DisplayManagerService.this.handleLogicalDisplayChangedLocked(logicalDisplay);
                    return;
                case 3:
                    DisplayManagerService.this.handleLogicalDisplayRemovedLocked(logicalDisplay);
                    return;
                case 4:
                    DisplayManagerService.this.handleLogicalDisplaySwappedLocked(logicalDisplay);
                    return;
                case 5:
                    DisplayManagerService.this.handleLogicalDisplayFrameRateOverridesChangedLocked(logicalDisplay);
                    return;
                case 6:
                    DisplayManagerService.this.handleLogicalDisplayDeviceStateTransitionLocked(logicalDisplay);
                    return;
                case 7:
                    DisplayManagerService.this.handleLogicalDisplayHdrSdrRatioChangedLocked(logicalDisplay);
                    return;
                default:
                    return;
            }
        }

        @Override // com.android.server.display.LogicalDisplayMapper.Listener
        public void onDisplayGroupEventLocked(int i, int i2) {
            DisplayManagerService.this.sendDisplayGroupEvent(i, i2);
        }

        @Override // com.android.server.display.LogicalDisplayMapper.Listener
        public void onTraversalRequested() {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                DisplayManagerService.this.scheduleTraversalLocked(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class CallbackRecord implements IBinder.DeathRecipient {
        private final IDisplayManagerCallback mCallback;
        private AtomicLong mEventsMask;
        public final int mPid;
        public final int mUid;
        public boolean mWifiDisplayScanRequested;

        CallbackRecord(int i, int i2, IDisplayManagerCallback iDisplayManagerCallback, long j) {
            this.mPid = i;
            this.mUid = i2;
            this.mCallback = iDisplayManagerCallback;
            this.mEventsMask = new AtomicLong(j);
        }

        public void updateEventsMask(long j) {
            this.mEventsMask.set(j);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            if (DisplayManagerService.DEBUG) {
                Slog.d(DisplayManagerService.TAG, "Display listener for pid " + this.mPid + " died.");
            }
            DisplayManagerService.this.onCallbackDied(this);
        }

        public boolean notifyDisplayEventAsync(int i, int i2) {
            if (!shouldSendEvent(i2)) {
                return true;
            }
            try {
                this.mCallback.onDisplayEvent(i, i2);
                return true;
            } catch (RemoteException e) {
                Slog.w(DisplayManagerService.TAG, "Failed to notify process " + this.mPid + " that displays changed, assuming it died.", e);
                binderDied();
                return false;
            }
        }

        private boolean shouldSendEvent(int i) {
            long j = this.mEventsMask.get();
            if (i == 1) {
                return (j & 1) != 0;
            }
            if (i == 2) {
                return (j & 4) != 0;
            }
            if (i == 3) {
                return (j & 2) != 0;
            }
            if (i == 4) {
                return (j & 8) != 0;
            }
            if (i == 5) {
                return (j & 16) != 0;
            }
            Slog.e(DisplayManagerService.TAG, "Unknown display event " + i);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class PendingCallback {
        private final CallbackRecord mCallbackRecord;
        private final ArrayList<Pair<Integer, Integer>> mDisplayEvents;

        PendingCallback(CallbackRecord callbackRecord, int i, int i2) {
            this.mCallbackRecord = callbackRecord;
            ArrayList<Pair<Integer, Integer>> arrayList = new ArrayList<>();
            this.mDisplayEvents = arrayList;
            arrayList.add(new Pair<>(Integer.valueOf(i), Integer.valueOf(i2)));
        }

        public void addDisplayEvent(int i, int i2) {
            Pair<Integer, Integer> pair = this.mDisplayEvents.get(r0.size() - 1);
            if (((Integer) pair.first).intValue() == i && ((Integer) pair.second).intValue() == i2) {
                Slog.d(DisplayManagerService.TAG, "Ignore redundant display event " + i + "/" + i2 + " to " + this.mCallbackRecord.mUid + "/" + this.mCallbackRecord.mPid);
                return;
            }
            this.mDisplayEvents.add(new Pair<>(Integer.valueOf(i), Integer.valueOf(i2)));
        }

        public void sendPendingDisplayEvent() {
            int i = 0;
            while (true) {
                if (i >= this.mDisplayEvents.size()) {
                    break;
                }
                Pair<Integer, Integer> pair = this.mDisplayEvents.get(i);
                if (DisplayManagerService.DEBUG) {
                    Slog.d(DisplayManagerService.TAG, "Send pending display event #" + i + " " + pair.first + "/" + pair.second + " to " + this.mCallbackRecord.mUid + "/" + this.mCallbackRecord.mPid);
                }
                if (!this.mCallbackRecord.notifyDisplayEventAsync(((Integer) pair.first).intValue(), ((Integer) pair.second).intValue())) {
                    Slog.d(DisplayManagerService.TAG, "Drop pending events for dead process " + this.mCallbackRecord.mPid);
                    break;
                }
                i++;
            }
            this.mDisplayEvents.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class BinderService extends IDisplayManager.Stub {
        BinderService() {
        }

        public DisplayInfo getDisplayInfo(int i) {
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.getDisplayInfoInternal(i, callingUid);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int[] getDisplayIds(boolean z) {
            int[] displayIdsLocked;
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (DisplayManagerService.this.mSyncRoot) {
                    displayIdsLocked = DisplayManagerService.this.mLogicalDisplayMapper.getDisplayIdsLocked(callingUid, z);
                }
                return displayIdsLocked;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public boolean isUidPresentOnDisplay(int i, int i2) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.isUidPresentOnDisplayInternal(i, i2);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public Point getStableDisplaySize() {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.getStableDisplaySizeInternal();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void registerCallback(IDisplayManagerCallback iDisplayManagerCallback) {
            registerCallbackWithEventMask(iDisplayManagerCallback, 7L);
        }

        public void registerCallbackWithEventMask(IDisplayManagerCallback iDisplayManagerCallback, long j) {
            if (iDisplayManagerCallback == null) {
                throw new IllegalArgumentException("listener must not be null");
            }
            int callingPid = Binder.getCallingPid();
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.registerCallbackInternal(iDisplayManagerCallback, callingPid, callingUid, j);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void startWifiDisplayScan() {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY", "Permission required to start wifi display scans");
            int callingPid = Binder.getCallingPid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.startWifiDisplayScanInternal(callingPid);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void stopWifiDisplayScan() {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY", "Permission required to stop wifi display scans");
            int callingPid = Binder.getCallingPid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.stopWifiDisplayScanInternal(callingPid);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void connectWifiDisplay(String str) {
            if (str == null) {
                throw new IllegalArgumentException("address must not be null");
            }
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY", "Permission required to connect to a wifi display");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.connectWifiDisplayInternal(str);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void disconnectWifiDisplay() {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.disconnectWifiDisplayInternal();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void renameWifiDisplay(String str, String str2) {
            if (str == null) {
                throw new IllegalArgumentException("address must not be null");
            }
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY", "Permission required to rename to a wifi display");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.renameWifiDisplayInternal(str, str2);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void forgetWifiDisplay(String str) {
            if (str == null) {
                throw new IllegalArgumentException("address must not be null");
            }
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY", "Permission required to forget to a wifi display");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.forgetWifiDisplayInternal(str);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void pauseWifiDisplay() {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY", "Permission required to pause a wifi display session");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.pauseWifiDisplayInternal();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void resumeWifiDisplay() {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY", "Permission required to resume a wifi display session");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.resumeWifiDisplayInternal();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public WifiDisplayStatus getWifiDisplayStatus() {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.getWifiDisplayStatusInternal();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setUserDisabledHdrTypes(int[] iArr) {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS", "Permission required to write the user settings.");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.setUserDisabledHdrTypesInternal(iArr);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void overrideHdrTypes(int i, int[] iArr) {
            IBinder displayToken;
            synchronized (DisplayManagerService.this.mSyncRoot) {
                displayToken = DisplayManagerService.this.getDisplayToken(i);
                if (displayToken == null) {
                    throw new IllegalArgumentException("Invalid display: " + i);
                }
            }
            DisplayControl.overrideHdrTypes(displayToken, iArr);
        }

        public void setAreUserDisabledHdrTypesAllowed(boolean z) {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS", "Permission required to write the user settings.");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.setAreUserDisabledHdrTypesAllowedInternal(z);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public boolean areUserDisabledHdrTypesAllowed() {
            boolean z;
            synchronized (DisplayManagerService.this.mSyncRoot) {
                z = DisplayManagerService.this.mAreUserDisabledHdrTypesAllowed;
            }
            return z;
        }

        public int[] getUserDisabledHdrTypes() {
            int[] iArr;
            synchronized (DisplayManagerService.this.mSyncRoot) {
                iArr = DisplayManagerService.this.mUserDisabledHdrTypes;
            }
            return iArr;
        }

        public void requestColorMode(int i, int i2) {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_DISPLAY_COLOR_MODE", "Permission required to change the display color mode");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.requestColorModeInternal(i, i2);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int createVirtualDisplay(VirtualDisplayConfig virtualDisplayConfig, IVirtualDisplayCallback iVirtualDisplayCallback, IMediaProjection iMediaProjection, String str) {
            return DisplayManagerService.this.createVirtualDisplayInternal(virtualDisplayConfig, iVirtualDisplayCallback, iMediaProjection, null, null, str);
        }

        public void resizeVirtualDisplay(IVirtualDisplayCallback iVirtualDisplayCallback, int i, int i2, int i3) {
            if (i <= 0 || i2 <= 0 || i3 <= 0) {
                throw new IllegalArgumentException("width, height, and densityDpi must be greater than 0");
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.resizeVirtualDisplayInternal(iVirtualDisplayCallback.asBinder(), i, i2, i3);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setVirtualDisplaySurface(IVirtualDisplayCallback iVirtualDisplayCallback, Surface surface) {
            if (surface != null && surface.isSingleBuffered()) {
                throw new IllegalArgumentException("Surface can't be single-buffered");
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.setVirtualDisplaySurfaceInternal(iVirtualDisplayCallback.asBinder(), surface);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void releaseVirtualDisplay(IVirtualDisplayCallback iVirtualDisplayCallback) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.releaseVirtualDisplayInternal(iVirtualDisplayCallback.asBinder());
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setVirtualDisplayState(IVirtualDisplayCallback iVirtualDisplayCallback, boolean z) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.setVirtualDisplayStateInternal(iVirtualDisplayCallback.asBinder(), z);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(DisplayManagerService.this.mContext, DisplayManagerService.TAG, printWriter) && !DisplayManagerService.this.mDmsExt.dynamicallyConfigDebug(printWriter, strArr)) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    DisplayManagerService.this.dumpInternal(printWriter, strArr);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public ParceledListSlice<BrightnessChangeEvent> getBrightnessEvents(String str) {
            ParceledListSlice<BrightnessChangeEvent> brightnessEvents;
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.BRIGHTNESS_SLIDER_USAGE", "Permission to read brightness events.");
            int callingUid = Binder.getCallingUid();
            int noteOp = ((AppOpsManager) DisplayManagerService.this.mContext.getSystemService(AppOpsManager.class)).noteOp(43, callingUid, str);
            boolean z = true;
            if (noteOp != 3 ? noteOp != 0 : DisplayManagerService.this.mContext.checkCallingPermission("android.permission.PACKAGE_USAGE_STATS") != 0) {
                z = false;
            }
            int userId = UserHandle.getUserId(callingUid);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (DisplayManagerService.this.mSyncRoot) {
                    brightnessEvents = ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(0)).getBrightnessEvents(userId, z);
                }
                return brightnessEvents;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public ParceledListSlice<AmbientBrightnessDayStats> getAmbientBrightnessStats() {
            ParceledListSlice<AmbientBrightnessDayStats> ambientBrightnessStats;
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_AMBIENT_LIGHT_STATS", "Permission required to to access ambient light stats.");
            int userId = UserHandle.getUserId(Binder.getCallingUid());
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (DisplayManagerService.this.mSyncRoot) {
                    ambientBrightnessStats = ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(0)).getAmbientBrightnessStats(userId);
                }
                return ambientBrightnessStats;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setBrightnessConfigurationForUser(final BrightnessConfiguration brightnessConfiguration, final int i, final String str) {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_DISPLAY_BRIGHTNESS", "Permission required to change the display's brightness configuration");
            if (i != UserHandle.getCallingUserId()) {
                DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", "Permission required to change the display brightness configuration of another user");
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (DisplayManagerService.this.mSyncRoot) {
                    DisplayManagerService.this.mLogicalDisplayMapper.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$BinderService$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            DisplayManagerService.BinderService.this.lambda$setBrightnessConfigurationForUser$0(brightnessConfiguration, i, str, (LogicalDisplay) obj);
                        }
                    });
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setBrightnessConfigurationForUser$0(BrightnessConfiguration brightnessConfiguration, int i, String str, LogicalDisplay logicalDisplay) {
            if (logicalDisplay.getDisplayInfoLocked().type != 1) {
                return;
            }
            DisplayManagerService.this.setBrightnessConfigurationForDisplayInternal(brightnessConfiguration, logicalDisplay.getPrimaryDisplayDeviceLocked().getUniqueId(), i, str);
        }

        public void setBrightnessConfigurationForDisplay(BrightnessConfiguration brightnessConfiguration, String str, int i, String str2) {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_DISPLAY_BRIGHTNESS", "Permission required to change the display's brightness configuration");
            if (i != UserHandle.getCallingUserId()) {
                DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", "Permission required to change the display brightness configuration of another user");
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.setBrightnessConfigurationForDisplayInternal(brightnessConfiguration, str, i, str2);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public BrightnessConfiguration getBrightnessConfigurationForDisplay(String str, int i) {
            BrightnessConfiguration brightnessConfigForDisplayWithPdsFallbackLocked;
            DisplayPowerController dpcFromUniqueIdLocked;
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_DISPLAY_BRIGHTNESS", "Permission required to read the display's brightness configuration");
            if (i != UserHandle.getCallingUserId()) {
                DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", "Permission required to read the display brightness configuration of another user");
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            int userSerialNumber = DisplayManagerService.this.getUserManager().getUserSerialNumber(i);
            try {
                synchronized (DisplayManagerService.this.mSyncRoot) {
                    brightnessConfigForDisplayWithPdsFallbackLocked = DisplayManagerService.this.getBrightnessConfigForDisplayWithPdsFallbackLocked(str, userSerialNumber);
                    if (brightnessConfigForDisplayWithPdsFallbackLocked == null && (dpcFromUniqueIdLocked = DisplayManagerService.this.getDpcFromUniqueIdLocked(str)) != null) {
                        brightnessConfigForDisplayWithPdsFallbackLocked = dpcFromUniqueIdLocked.getDefaultBrightnessConfiguration();
                    }
                }
                return brightnessConfigForDisplayWithPdsFallbackLocked;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public BrightnessConfiguration getBrightnessConfigurationForUser(int i) {
            String uniqueId;
            synchronized (DisplayManagerService.this.mSyncRoot) {
                uniqueId = DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(0).getPrimaryDisplayDeviceLocked().getUniqueId();
            }
            return getBrightnessConfigurationForDisplay(uniqueId, i);
        }

        public BrightnessConfiguration getDefaultBrightnessConfiguration() {
            BrightnessConfiguration defaultBrightnessConfiguration;
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_DISPLAY_BRIGHTNESS", "Permission required to read the display's default brightness configuration");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (DisplayManagerService.this.mSyncRoot) {
                    defaultBrightnessConfiguration = ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(0)).getDefaultBrightnessConfiguration();
                }
                return defaultBrightnessConfiguration;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public BrightnessInfo getBrightnessInfo(int i) {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_DISPLAY_BRIGHTNESS", "Permission required to read the display's brightness info.");
            DisplayManagerService.this.mDmsExt.setBrightnessInfoUid(Binder.getCallingUid());
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (DisplayManagerService.this.mSyncRoot) {
                    LogicalDisplay displayLocked = DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(i, false);
                    if (displayLocked != null && displayLocked.isEnabledLocked()) {
                        DisplayPowerController displayPowerController = (DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(i);
                        if (displayPowerController == null) {
                            return null;
                        }
                        return displayPowerController.getBrightnessInfo();
                    }
                    return null;
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public boolean isMinimalPostProcessingRequested(int i) {
            boolean requestedMinimalPostProcessingLocked;
            synchronized (DisplayManagerService.this.mSyncRoot) {
                requestedMinimalPostProcessingLocked = DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(i).getRequestedMinimalPostProcessingLocked();
            }
            return requestedMinimalPostProcessingLocked;
        }

        public void setTemporaryBrightness(int i, float f) {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_DISPLAY_BRIGHTNESS", "Permission required to set the display's brightness");
            if (DisplayManagerService.PANIC_DEBUG) {
                Slog.d(DisplayManagerService.TAG, "setTemporaryBrightness=" + String.format("id=%d,brightness=%f,calling(%d,%d)", Integer.valueOf(i), Float.valueOf(f), Integer.valueOf(Binder.getCallingPid()), Integer.valueOf(Binder.getCallingUid())));
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (DisplayManagerService.this.mSyncRoot) {
                    ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(i)).setTemporaryBrightness(f);
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setBrightness(int i, float f) {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_DISPLAY_BRIGHTNESS", "Permission required to set the display's brightness");
            DisplayManagerService.this.mDmsExt.setBrightnessUid(Binder.getCallingUid());
            if (!DisplayManagerService.isValidBrightness(f) && !DisplayManagerService.this.mDmsExt.setBrightnessByAccessibility(Binder.getCallingUid())) {
                Slog.w(DisplayManagerService.TAG, "Attempted to set invalid brightness" + f);
                return;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (DisplayManagerService.this.mSyncRoot) {
                    DisplayPowerController displayPowerController = (DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(i);
                    if (displayPowerController != null) {
                        displayPowerController.setBrightness(f);
                    }
                    DisplayManagerService.this.mPersistentDataStore.saveIfNeeded();
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public float getBrightness(int i) {
            float screenBrightnessSetting;
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_DISPLAY_BRIGHTNESS", "Permission required to set the display's brightness");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (DisplayManagerService.this.mSyncRoot) {
                    DisplayPowerController displayPowerController = (DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(i);
                    screenBrightnessSetting = displayPowerController != null ? displayPowerController.getScreenBrightnessSetting() : Float.NaN;
                }
                return screenBrightnessSetting;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setTemporaryAutoBrightnessAdjustment(float f) {
            int callingUid = Binder.getCallingUid();
            int callingPid = Binder.getCallingPid();
            if (DisplayManagerService.PANIC_DEBUG) {
                Slog.d(DisplayManagerService.TAG, "setTemporaryAutoBrightnessAdjustment: adjustment = " + String.format("%f,calling(%d,%d)", Float.valueOf(f), Integer.valueOf(callingPid), Integer.valueOf(callingUid)));
            }
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_DISPLAY_BRIGHTNESS", "Permission required to set the display's auto brightness adjustment");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (DisplayManagerService.this.mSyncRoot) {
                    DisplayManagerService.this.mDmsExt.setTemporaryAutoBrightnessAdjustment(f);
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
            new DisplayManagerShellCommand(DisplayManagerService.this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }

        public Curve getMinimumBrightnessCurve() {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.getMinimumBrightnessCurveInternal();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int getPreferredWideGamutColorSpaceId() {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.getPreferredWideGamutColorSpaceIdInternal();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setUserPreferredDisplayMode(int i, Display.Mode mode) {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_USER_PREFERRED_DISPLAY_MODE", "Permission required to set the user preferred display mode.");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.setUserPreferredDisplayModeInternal(i, mode);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public Display.Mode getUserPreferredDisplayMode(int i) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.getUserPreferredDisplayModeInternal(i);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public Display.Mode getSystemPreferredDisplayMode(int i) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.getSystemPreferredDisplayModeInternal(i);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setHdrConversionMode(HdrConversionMode hdrConversionMode) {
            if (DisplayManagerService.this.mIsHdrOutputControlEnabled) {
                DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_HDR_CONVERSION_MODE", "Permission required to set the HDR conversion mode.");
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    DisplayManagerService.this.setHdrConversionModeInternal(hdrConversionMode);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public HdrConversionMode getHdrConversionModeSetting() {
            if (!DisplayManagerService.this.mIsHdrOutputControlEnabled) {
                return DisplayManagerService.HDR_CONVERSION_MODE_UNSUPPORTED;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.getHdrConversionModeSettingInternal();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public HdrConversionMode getHdrConversionMode() {
            if (!DisplayManagerService.this.mIsHdrOutputControlEnabled) {
                return DisplayManagerService.HDR_CONVERSION_MODE_UNSUPPORTED;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.getHdrConversionModeInternal();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int[] getSupportedHdrOutputTypes() {
            if (!DisplayManagerService.this.mIsHdrOutputControlEnabled) {
                return DisplayManagerService.EMPTY_ARRAY;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.getSupportedHdrOutputTypesInternal();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setShouldAlwaysRespectAppRequestedMode(boolean z) {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.OVERRIDE_DISPLAY_MODE_REQUESTS", "Permission required to override display mode requests.");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.setShouldAlwaysRespectAppRequestedModeInternal(z);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public boolean shouldAlwaysRespectAppRequestedMode() {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.OVERRIDE_DISPLAY_MODE_REQUESTS", "Permission required to override display mode requests.");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.shouldAlwaysRespectAppRequestedModeInternal();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setRefreshRateSwitchingType(int i) {
            DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_REFRESH_RATE_SWITCHING_TYPE", "Permission required to modify refresh rate switching type.");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DisplayManagerService.this.setRefreshRateSwitchingTypeInternal(i);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int getRefreshRateSwitchingType() {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.getRefreshRateSwitchingTypeInternal();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public DisplayDecorationSupport getDisplayDecorationSupport(int i) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.getDisplayDecorationSupportInternal(i);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setDisplayIdToMirror(IBinder iBinder, int i) {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                LogicalDisplay displayLocked = DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(i);
                if (DisplayManagerService.this.mVirtualDisplayAdapter != null) {
                    VirtualDisplayAdapter virtualDisplayAdapter = DisplayManagerService.this.mVirtualDisplayAdapter;
                    if (displayLocked == null) {
                        i = -1;
                    }
                    virtualDisplayAdapter.setDisplayIdToMirror(iBinder, i);
                }
            }
        }

        public OverlayProperties getOverlaySupport() {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DisplayManagerService.this.getOverlaySupportInternal();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setSpecBrightness(int i, String str, int i2) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                Slog.d(DisplayManagerService.TAG, "setSpecBrightness gear = " + i + " reason = " + str + " rate = " + i2);
                DisplayManagerService.this.setSpecBrightnessInternal(i, str, i2);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (super.onTransact(i, parcel, parcel2, i2)) {
                return true;
            }
            IOplusDisplayManagerServiceEx iOplusDisplayManagerServiceEx = DisplayManagerService.this.mDMSEx;
            return iOplusDisplayManagerServiceEx != null && iOplusDisplayManagerServiceEx.onTransact(i, parcel, parcel2, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidBrightness(float f) {
        return !Float.isNaN(f) && f >= 0.0f && f <= 1.0f;
    }

    private static boolean isValidResolution(Point point) {
        return point != null && point.x > 0 && point.y > 0;
    }

    private static boolean isValidRefreshRate(float f) {
        return !Float.isNaN(f) && f > 0.0f;
    }

    @VisibleForTesting
    void overrideSensorManager(SensorManager sensorManager) {
        synchronized (this.mSyncRoot) {
            this.mSensorManager = sensorManager;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class LocalService extends DisplayManagerInternal {
        LocalService() {
        }

        public void initPowerManagement(DisplayManagerInternal.DisplayPowerCallbacks displayPowerCallbacks, Handler handler, SensorManager sensorManager) {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                DisplayManagerService.this.mDisplayPowerCallbacks = displayPowerCallbacks;
                DisplayManagerService.this.mSensorManager = sensorManager;
                DisplayManagerService.this.mPowerHandler = handler;
                DisplayManagerService.this.initializeDisplayPowerControllersLocked();
                DisplayManagerService.this.mLogicalDisplayMapper.setPowerHandler(handler);
            }
            DisplayManagerService.this.mHandler.sendEmptyMessage(6);
        }

        public int createVirtualDisplay(VirtualDisplayConfig virtualDisplayConfig, IVirtualDisplayCallback iVirtualDisplayCallback, IVirtualDevice iVirtualDevice, DisplayWindowPolicyController displayWindowPolicyController, String str) {
            return DisplayManagerService.this.createVirtualDisplayInternal(virtualDisplayConfig, iVirtualDisplayCallback, null, iVirtualDevice, displayWindowPolicyController, str);
        }

        public boolean requestPowerState(int i, DisplayManagerInternal.DisplayPowerRequest displayPowerRequest, boolean z) {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                if (DisplayManagerService.this.mLogicalDisplayMapper.getDisplayGroupLocked(i) == null) {
                    return true;
                }
                DisplayManagerService displayManagerService = DisplayManagerService.this;
                return displayManagerService.mDmsExt.requestPowerState(displayManagerService.mLogicalDisplayMapper, i, displayPowerRequest, z) & true;
            }
        }

        public boolean isProximitySensorAvailable() {
            boolean isProximitySensorAvailable;
            synchronized (DisplayManagerService.this.mSyncRoot) {
                isProximitySensorAvailable = ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(0)).isProximitySensorAvailable();
            }
            return isProximitySensorAvailable;
        }

        public void registerDisplayGroupListener(DisplayManagerInternal.DisplayGroupListener displayGroupListener) {
            DisplayManagerService.this.mDisplayGroupListeners.add(displayGroupListener);
        }

        public void unregisterDisplayGroupListener(DisplayManagerInternal.DisplayGroupListener displayGroupListener) {
            DisplayManagerService.this.mDisplayGroupListeners.remove(displayGroupListener);
        }

        public ScreenCapture.ScreenshotHardwareBuffer systemScreenshot(int i) {
            return DisplayManagerService.this.systemScreenshotInternal(i);
        }

        public ScreenCapture.ScreenshotHardwareBuffer userScreenshot(int i) {
            return DisplayManagerService.this.userScreenshotInternal(i);
        }

        public DisplayInfo getDisplayInfo(int i) {
            return DisplayManagerService.this.getDisplayInfoInternal(i, Process.myUid());
        }

        public Set<DisplayInfo> getPossibleDisplayInfo(int i) {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                ArraySet arraySet = new ArraySet();
                if (DisplayManagerService.this.mDeviceStateManager == null) {
                    Slog.w(DisplayManagerService.TAG, "Can't get supported states since DeviceStateManager not ready");
                    return arraySet;
                }
                for (int i2 : DisplayManagerService.this.mDeviceStateManager.getSupportedStateIdentifiers()) {
                    DisplayInfo displayInfoForStateLocked = DisplayManagerService.this.mLogicalDisplayMapper.getDisplayInfoForStateLocked(i2, i);
                    if (displayInfoForStateLocked != null) {
                        arraySet.add(displayInfoForStateLocked);
                    }
                }
                return arraySet;
            }
        }

        public Point getDisplayPosition(int i) {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                LogicalDisplay displayLocked = DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(i);
                if (displayLocked == null) {
                    return null;
                }
                return displayLocked.getDisplayPosition();
            }
        }

        public void registerDisplayTransactionListener(DisplayManagerInternal.DisplayTransactionListener displayTransactionListener) {
            if (displayTransactionListener == null) {
                throw new IllegalArgumentException("listener must not be null");
            }
            DisplayManagerService.this.registerDisplayTransactionListenerInternal(displayTransactionListener);
        }

        public void unregisterDisplayTransactionListener(DisplayManagerInternal.DisplayTransactionListener displayTransactionListener) {
            if (displayTransactionListener == null) {
                throw new IllegalArgumentException("listener must not be null");
            }
            DisplayManagerService.this.unregisterDisplayTransactionListenerInternal(displayTransactionListener);
        }

        public void setDisplayInfoOverrideFromWindowManager(int i, DisplayInfo displayInfo) {
            DisplayManagerService.this.setDisplayInfoOverrideFromWindowManagerInternal(i, displayInfo);
        }

        public void getNonOverrideDisplayInfo(int i, DisplayInfo displayInfo) {
            DisplayManagerService.this.getNonOverrideDisplayInfoInternal(i, displayInfo);
        }

        public void performTraversal(SurfaceControl.Transaction transaction) {
            DisplayManagerService.this.performTraversalInternal(transaction);
        }

        public void setDisplayProperties(int i, boolean z, float f, int i2, float f2, float f3, boolean z2, boolean z3, boolean z4) {
            DisplayManagerService.this.setDisplayPropertiesInternal(i, z, f, i2, f2, f3, z2, z3, z4);
        }

        public void setDisplayOffsets(int i, int i2, int i3) {
            DisplayManagerService.this.setDisplayOffsetsInternal(i, i2, i3);
        }

        public void setDisplayScalingDisabled(int i, boolean z) {
            DisplayManagerService.this.setDisplayScalingDisabledInternal(i, z);
        }

        public void setDisplayAccessUIDs(SparseArray<IntArray> sparseArray) {
            DisplayManagerService.this.setDisplayAccessUIDsInternal(sparseArray);
        }

        public void persistBrightnessTrackerState() {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(0)).persistBrightnessTrackerState();
            }
        }

        public void onOverlayChanged() {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                DisplayManagerService.this.mDisplayDeviceRepo.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$LocalService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((DisplayDevice) obj).onOverlayChangedLocked();
                    }
                });
            }
        }

        public DisplayedContentSamplingAttributes getDisplayedContentSamplingAttributes(int i) {
            return DisplayManagerService.this.getDisplayedContentSamplingAttributesInternal(i);
        }

        public boolean setDisplayedContentSamplingEnabled(int i, boolean z, int i2, int i3) {
            return DisplayManagerService.this.setDisplayedContentSamplingEnabledInternal(i, z, i2, i3);
        }

        public DisplayedContentSample getDisplayedContentSample(int i, long j, long j2) {
            return DisplayManagerService.this.getDisplayedContentSampleInternal(i, j, j2);
        }

        public void ignoreProximitySensorUntilChanged() {
            ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(0)).ignoreProximitySensorUntilChanged();
        }

        public int getRefreshRateSwitchingType() {
            return DisplayManagerService.this.getRefreshRateSwitchingTypeInternal();
        }

        public void notifyRefreshRatePolicyChange() {
            DisplayManagerService.this.scheduleTraversalLocked(false);
        }

        public void blockScreenOnByBiometrics(String str) {
            try {
                int size = DisplayManagerService.this.mDisplayStates.size();
                for (int i = 0; i < size; i++) {
                    int keyAt = DisplayManagerService.this.mDisplayStates.keyAt(i);
                    if (DisplayManagerService.this.mDisplayPowerControllers != null && DisplayManagerService.this.mDisplayPowerControllers.get(keyAt) != null && ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(keyAt)).mDpcExt != null) {
                        ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(keyAt)).mDpcExt.blockScreenOnByBiometrics(str);
                        Slog.i(DisplayManagerService.TAG, "blockScreenOnByBiometrics displayId = " + keyAt);
                    }
                }
            } catch (Exception e) {
                Slog.e(DisplayManagerService.TAG, "blockScreenOnByBiometrics", e);
            }
        }

        public void unblockScreenOnByBiometrics(String str) {
            try {
                int size = DisplayManagerService.this.mDisplayStates.size();
                for (int i = 0; i < size; i++) {
                    int keyAt = DisplayManagerService.this.mDisplayStates.keyAt(i);
                    if (DisplayManagerService.this.mDisplayPowerControllers != null && DisplayManagerService.this.mDisplayPowerControllers.get(keyAt) != null && ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(keyAt)).mDpcExt != null) {
                        ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(keyAt)).mDpcExt.unblockScreenOnByBiometrics(str);
                        Slog.i(DisplayManagerService.TAG, "unblockScreenOnByBiometrics displayId = " + keyAt);
                    }
                }
            } catch (Exception e) {
                Slog.e(DisplayManagerService.TAG, "unblockScreenOnByBiometrics", e);
            }
        }

        public boolean hasBiometricsBlockedReason(String str) {
            return ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(0)).mDpcExt.hasBiometricsBlockedReason(str);
        }

        public boolean isBlockDisplayByBiometrics() {
            return ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(0)).mDpcExt.isBlockDisplayByBiometrics();
        }

        public boolean isBlockScreenOnByBiometrics() {
            return ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(0)).mDpcExt.isBlockScreenOnByBiometrics();
        }

        public int getScreenState() {
            return ((DisplayPowerController) DisplayManagerService.this.mDisplayPowerControllers.get(0)).mDpcExt.getScreenState();
        }

        public SurfaceControl.RefreshRateRange getRefreshRateForDisplayAndSensor(int i, String str, String str2) {
            SensorManager sensorManager;
            synchronized (DisplayManagerService.this.mSyncRoot) {
                sensorManager = DisplayManagerService.this.mSensorManager;
            }
            if (sensorManager == null || SensorUtils.findSensor(sensorManager, str2, str, 0) == null) {
                return null;
            }
            synchronized (DisplayManagerService.this.mSyncRoot) {
                LogicalDisplay displayLocked = DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(i);
                if (displayLocked == null) {
                    return null;
                }
                DisplayDevice primaryDisplayDeviceLocked = displayLocked.getPrimaryDisplayDeviceLocked();
                if (primaryDisplayDeviceLocked == null) {
                    return null;
                }
                DisplayDeviceConfig.SensorData proximitySensor = primaryDisplayDeviceLocked.getDisplayDeviceConfig().getProximitySensor();
                if (proximitySensor == null || !proximitySensor.matches(str, str2)) {
                    return null;
                }
                return new SurfaceControl.RefreshRateRange(proximitySensor.minRefreshRate, proximitySensor.maxRefreshRate);
            }
        }

        public List<DisplayManagerInternal.RefreshRateLimitation> getRefreshRateLimitations(int i) {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                DisplayDevice deviceForDisplayLocked = DisplayManagerService.this.getDeviceForDisplayLocked(i);
                if (deviceForDisplayLocked == null) {
                    return null;
                }
                return deviceForDisplayLocked.getDisplayDeviceConfig().getRefreshRateLimitations();
            }
        }

        public void setWindowManagerMirroring(int i, boolean z) {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                DisplayDevice deviceForDisplayLocked = DisplayManagerService.this.getDeviceForDisplayLocked(i);
                if (deviceForDisplayLocked != null) {
                    deviceForDisplayLocked.setWindowManagerMirroringLocked(z);
                }
            }
        }

        public Point getDisplaySurfaceDefaultSize(int i) {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                DisplayDevice deviceForDisplayLocked = DisplayManagerService.this.getDeviceForDisplayLocked(i);
                if (deviceForDisplayLocked == null) {
                    return null;
                }
                return deviceForDisplayLocked.getDisplaySurfaceDefaultSizeLocked();
            }
        }

        public void onEarlyInteractivityChange(boolean z) {
            DisplayManagerService.this.mLogicalDisplayMapper.onEarlyInteractivityChange(z);
        }

        public DisplayWindowPolicyController getDisplayWindowPolicyController(int i) {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                if (!DisplayManagerService.this.mDisplayWindowPolicyControllers.contains(i)) {
                    return null;
                }
                return (DisplayWindowPolicyController) DisplayManagerService.this.mDisplayWindowPolicyControllers.get(i).second;
            }
        }

        public int getDisplayIdToMirror(int i) {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                LogicalDisplay displayLocked = DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(i);
                if (displayLocked == null) {
                    return -1;
                }
                if (DisplayManagerService.this.mLogicalDisplayMapper.isRemapDisabledSecondaryDisplayId(i)) {
                    return -1;
                }
                if (((IMirageDisplayManagerExt) ExtLoader.type(IMirageDisplayManagerExt.class).create()).isMirageDisplay(i)) {
                    return -1;
                }
                DisplayDevice primaryDisplayDeviceLocked = displayLocked.getPrimaryDisplayDeviceLocked();
                int i2 = 0;
                if (!((primaryDisplayDeviceLocked.getDisplayDeviceInfoLocked().flags & 128) != 0) && !primaryDisplayDeviceLocked.isWindowManagerMirroringLocked()) {
                    int displayIdToMirrorLocked = primaryDisplayDeviceLocked.getDisplayIdToMirrorLocked();
                    if (DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(displayIdToMirrorLocked) != null) {
                        i2 = displayIdToMirrorLocked;
                    }
                    return i2;
                }
                return -1;
            }
        }

        public SurfaceControl.DisplayPrimaries getDisplayNativePrimaries(int i) {
            IBinder displayToken;
            synchronized (DisplayManagerService.this.mSyncRoot) {
                displayToken = DisplayManagerService.this.getDisplayToken(i);
                if (displayToken == null) {
                    throw new IllegalArgumentException("Invalid displayId=" + i);
                }
            }
            return SurfaceControl.getDisplayNativePrimaries(displayToken);
        }

        public HostUsiVersion getHostUsiVersion(int i) {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                LogicalDisplay displayLocked = DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(i);
                if (displayLocked == null) {
                    return null;
                }
                return displayLocked.getPrimaryDisplayDeviceLocked().getDisplayDeviceConfig().getHostUsiVersion();
            }
        }

        public IntArray getDisplayGroupIds() {
            final ArraySet arraySet = new ArraySet();
            final IntArray intArray = new IntArray();
            synchronized (DisplayManagerService.this.mSyncRoot) {
                DisplayManagerService.this.mLogicalDisplayMapper.forEachLocked(new Consumer() { // from class: com.android.server.display.DisplayManagerService$LocalService$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DisplayManagerService.LocalService.this.lambda$getDisplayGroupIds$0(arraySet, intArray, (LogicalDisplay) obj);
                    }
                });
            }
            return intArray;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getDisplayGroupIds$0(Set set, IntArray intArray, LogicalDisplay logicalDisplay) {
            int displayGroupIdFromDisplayIdLocked = DisplayManagerService.this.mLogicalDisplayMapper.getDisplayGroupIdFromDisplayIdLocked(logicalDisplay.getDisplayIdLocked());
            if (set.contains(Integer.valueOf(displayGroupIdFromDisplayIdLocked))) {
                return;
            }
            set.add(Integer.valueOf(displayGroupIdFromDisplayIdLocked));
            intArray.add(displayGroupIdFromDisplayIdLocked);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class DesiredDisplayModeSpecsObserver implements DisplayModeDirector.DesiredDisplayModeSpecsListener {
        private final Consumer<LogicalDisplay> mSpecsChangedConsumer = new Consumer() { // from class: com.android.server.display.DisplayManagerService$DesiredDisplayModeSpecsObserver$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DisplayManagerService.DesiredDisplayModeSpecsObserver.this.lambda$new$0((LogicalDisplay) obj);
            }
        };

        @GuardedBy({"mSyncRoot"})
        private boolean mChanged = false;

        DesiredDisplayModeSpecsObserver() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(LogicalDisplay logicalDisplay) {
            int displayIdLocked = logicalDisplay.getDisplayIdLocked();
            DisplayModeDirector.DesiredDisplayModeSpecs desiredDisplayModeSpecs = DisplayManagerService.this.mDisplayModeDirector.getDesiredDisplayModeSpecs(displayIdLocked);
            DisplayModeDirector.DesiredDisplayModeSpecs desiredDisplayModeSpecsLocked = logicalDisplay.getDesiredDisplayModeSpecsLocked();
            if (DisplayManagerService.this.mDmsExt.isBoostDisplayRefreshRateForAnim()) {
                desiredDisplayModeSpecs.primary.render.max = DisplayManagerService.this.requestedMaxRefreshRate;
            }
            if (DisplayManagerService.DEBUG) {
                Slog.i(DisplayManagerService.TAG, "Comparing display specs: " + desiredDisplayModeSpecs + ", existing: " + desiredDisplayModeSpecsLocked);
            }
            if (desiredDisplayModeSpecs.equals(desiredDisplayModeSpecsLocked)) {
                return;
            }
            logicalDisplay.setDesiredDisplayModeSpecsLocked(desiredDisplayModeSpecs);
            DisplayManagerService.this.mDmsExt.notifyDisplayModeSpecsChanged(displayIdLocked, desiredDisplayModeSpecs.primary.render.max);
            this.mChanged = true;
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecsListener
        public void onDesiredDisplayModeSpecsChanged() {
            synchronized (DisplayManagerService.this.mSyncRoot) {
                this.mChanged = false;
                DisplayManagerService.this.mLogicalDisplayMapper.forEachLocked(this.mSpecsChangedConsumer, false);
                if (this.mChanged) {
                    DisplayManagerService.this.scheduleTraversalLocked(false);
                    this.mChanged = false;
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class DeviceStateListener implements DeviceStateManager.DeviceStateCallback {
        private int mBaseState = -1;

        DeviceStateListener() {
        }

        public void onStateChanged(int i) {
            boolean z = i != this.mBaseState;
            synchronized (DisplayManagerService.this.mSyncRoot) {
                Message obtainMessage = DisplayManagerService.this.mHandler.obtainMessage(9);
                obtainMessage.arg1 = i;
                DisplayManagerService.this.mHandler.sendMessage(obtainMessage);
                DisplayManagerService.this.mLogicalDisplayMapper.setDeviceStateLocked(i, z);
            }
        }

        public void onBaseStateChanged(int i) {
            this.mBaseState = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class BrightnessPair {
        public float brightness;
        public float sdrBrightness;

        BrightnessPair(float f, float f2) {
            this.brightness = f;
            this.sdrBrightness = f2;
        }
    }

    public DisplayManagerServiceWrapper getWrapper() {
        return this.mDmsWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class DisplayManagerServiceWrapper {
        public DisplayManagerServiceWrapper() {
        }

        public SparseArray<DisplayPowerController> getDisplayPowerControllers() {
            return DisplayManagerService.this.mDisplayPowerControllers;
        }
    }
}
