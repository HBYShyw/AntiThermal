package com.android.server.accessibility;

import android.R;
import android.accessibilityservice.AccessibilityGestureEvent;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.AccessibilityTrace;
import android.accessibilityservice.IAccessibilityServiceClient;
import android.accessibilityservice.MagnificationConfig;
import android.annotation.RequiresPermission;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManagerInternal;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.database.ContentObserver;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.IFingerprintService;
import android.media.AudioManagerInternal;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ShellCallback;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.provider.SettingsStringUtil;
import android.safetycenter.SafetyCenterManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.IntArray;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.IWindow;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MagnificationSpec;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.WindowInfo;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityInteractionClient;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowAttributes;
import android.view.accessibility.AccessibilityWindowInfo;
import android.view.accessibility.IAccessibilityInteractionConnection;
import android.view.accessibility.IAccessibilityManager;
import android.view.accessibility.IAccessibilityManagerClient;
import android.view.accessibility.IWindowMagnificationConnection;
import android.view.inputmethod.EditorInfo;
import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.internal.accessibility.dialog.AccessibilityButtonChooserActivity;
import com.android.internal.accessibility.dialog.AccessibilityShortcutChooserActivity;
import com.android.internal.accessibility.util.AccessibilityStatsLogUtils;
import com.android.internal.accessibility.util.AccessibilityUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.content.PackageMonitor;
import com.android.internal.inputmethod.IAccessibilityInputMethodSession;
import com.android.internal.inputmethod.IRemoteAccessibilityInputConnection;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.internal.util.IntPair;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.AccessibilityManagerInternal;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.accessibility.AbstractAccessibilityServiceConnection;
import com.android.server.accessibility.AccessibilityManagerService;
import com.android.server.accessibility.AccessibilitySecurityPolicy;
import com.android.server.accessibility.AccessibilityUserState;
import com.android.server.accessibility.AccessibilityWindowManager;
import com.android.server.accessibility.PolicyWarningUIController;
import com.android.server.accessibility.ProxyManager;
import com.android.server.accessibility.SystemActionPerformer;
import com.android.server.accessibility.magnification.MagnificationController;
import com.android.server.accessibility.magnification.MagnificationProcessor;
import com.android.server.accessibility.magnification.MagnificationScaleProvider;
import com.android.server.accessibility.magnification.WindowMagnificationManager;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.pm.UserManagerInternal;
import com.android.server.utils.Slogf;
import com.android.server.wm.ActivityTaskManagerInternal;
import com.android.server.wm.WindowManagerInternal;
import com.android.settingslib.RestrictedLockUtils;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AccessibilityManagerService extends IAccessibilityManager.Stub implements AbstractAccessibilityServiceConnection.SystemSupport, AccessibilityUserState.ServiceInfoChangeListener, AccessibilityWindowManager.AccessibilityEventSender, AccessibilitySecurityPolicy.AccessibilityUserManager, SystemActionPerformer.SystemActionsChangedListener, SystemActionPerformer.DisplayUpdateCallBack, ProxyManager.SystemSupport {
    private static final char COMPONENT_NAME_SEPARATOR = ':';
    private static final boolean DEBUG = false;
    private static final String FUNCTION_REGISTER_UI_TEST_AUTOMATION_SERVICE = "registerUiTestAutomationService";
    private static final String GET_WINDOW_TOKEN = "getWindowToken";
    public static final int INVALID_SERVICE_ID = -1;
    private static final String LOG_TAG = "AccessibilityManagerService";
    public static final int MAGNIFICATION_GESTURE_HANDLER_ID = 0;
    private static final int POSTPONE_WINDOW_STATE_CHANGED_EVENT_TIMEOUT_MILLIS = 500;
    private static final String SET_PIP_ACTION_REPLACEMENT = "setPictureInPictureActionReplacingConnection";
    private static final int WAIT_FOR_USER_STATE_FULLY_INITIALIZED_MILLIS = 3000;
    private static final int WAIT_INPUT_FILTER_INSTALL_TIMEOUT_MS = 1000;
    private final AccessibilityDisplayListener mA11yDisplayListener;
    private SparseArray<SurfaceControl> mA11yOverlayLayers;
    private final AccessibilityWindowManager mA11yWindowManager;
    private final ActivityTaskManagerInternal mActivityTaskManagerService;
    private final CaptioningManagerImpl mCaptioningManagerImpl;
    private final Context mContext;

    @GuardedBy({"mLock"})
    private int mCurrentUserId;
    EditorInfo mEditorInfo;
    private AlertDialog mEnableTouchExplorationDialog;
    private FingerprintGestureDispatcher mFingerprintGestureDispatcher;
    private final FlashNotificationsController mFlashNotificationsController;
    private final RemoteCallbackList<IAccessibilityManagerClient> mGlobalClients;
    private boolean mHasInputFilter;
    private boolean mInitialized;
    private boolean mInputBound;
    private AccessibilityInputFilter mInputFilter;
    private boolean mInputFilterInstalled;
    boolean mInputSessionRequested;
    private InteractionBridge mInteractionBridge;
    private boolean mIsAccessibilityButtonShown;
    private KeyEventDispatcher mKeyEventDispatcher;
    private final Object mLock;
    private final MagnificationController mMagnificationController;
    private final MagnificationProcessor mMagnificationProcessor;
    private final Handler mMainHandler;
    private SparseArray<MotionEventInjector> mMotionEventInjectors;
    private final PackageManager mPackageManager;
    private final PowerManager mPowerManager;
    private final ProxyManager mProxyManager;

    @GuardedBy({"mLock"})
    private int mRealCurrentUserId;
    IRemoteAccessibilityInputConnection mRemoteInputConnection;
    boolean mRestarting;
    private final AccessibilitySecurityPolicy mSecurityPolicy;
    private final List<SendWindowStateChangedEventRunnable> mSendWindowStateChangedEventRunnables;
    private IAccessibilityManagerServiceExt mServiceExt;
    private final TextUtils.SimpleStringSplitter mStringColonSplitter;
    private SystemActionPerformer mSystemActionPerformer;
    private final List<AccessibilityServiceInfo> mTempAccessibilityServiceInfoList;
    private final Set<ComponentName> mTempComponentNameSet;
    private final IntArray mTempIntArray;
    private Point mTempPoint;
    private final Rect mTempRect;
    private final Rect mTempRect1;
    private final AccessibilityTraceManager mTraceManager;
    private final UiAutomationManager mUiAutomationManager;
    private final UserManagerInternal mUmi;

    @VisibleForTesting
    final SparseArray<AccessibilityUserState> mUserStates;

    @GuardedBy({"mLock"})
    private final SparseBooleanArray mVisibleBgUserIds;
    private final WindowManagerInternal mWindowManagerService;
    private static final int OWN_PROCESS_ID = Process.myPid();
    private static int sIdCounter = 1;

    public static /* synthetic */ String lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$23(String str) {
        return str;
    }

    public static /* synthetic */ String lambda$readAccessibilityButtonTargetsLocked$16(String str) {
        return str;
    }

    public static /* synthetic */ String lambda$readAccessibilityShortcutKeySettingLocked$15(String str) {
        return str;
    }

    public static /* synthetic */ String lambda$removeShortcutTargetForUnboundServiceLocked$24(String str) {
        return str;
    }

    public static /* synthetic */ String lambda$removeShortcutTargetForUnboundServiceLocked$25(String str) {
        return str;
    }

    public static /* synthetic */ String lambda$restoreAccessibilityButtonTargetsLocked$3(String str) {
        return str;
    }

    public static /* synthetic */ String lambda$restoreAccessibilityButtonTargetsLocked$4(String str) {
        return str;
    }

    public static /* synthetic */ String lambda$restoreAccessibilityButtonTargetsLocked$5(String str) {
        return str;
    }

    public static /* synthetic */ String lambda$restoreLegacyDisplayMagnificationNavBarIfNeededLocked$1(String str) {
        return str;
    }

    public static /* synthetic */ String lambda$restoreLegacyDisplayMagnificationNavBarIfNeededLocked$2(String str) {
        return str;
    }

    public static /* synthetic */ String lambda$updateAccessibilityButtonTargetsLocked$20(String str) {
        return str;
    }

    public static /* synthetic */ String lambda$updateAccessibilityShortcutKeyTargetsLocked$18(String str) {
        return str;
    }

    public AccessibilityUserState getCurrentUserStateLocked() {
        return getUserStateLocked(this.mCurrentUserId);
    }

    public void changeMagnificationMode(int i, int i2) {
        synchronized (this.mLock) {
            if (i == 0) {
                persistMagnificationModeSettingsLocked(i2);
            } else {
                AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
                if (i2 != currentUserStateLocked.getMagnificationModeLocked(i)) {
                    currentUserStateLocked.setMagnificationModeLocked(i, i2);
                    updateMagnificationModeChangeSettingsLocked(currentUserStateLocked, i);
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static final class LocalServiceImpl extends AccessibilityManagerInternal {
        private final AccessibilityManagerService mService;

        LocalServiceImpl(AccessibilityManagerService accessibilityManagerService) {
            this.mService = accessibilityManagerService;
        }

        @Override // com.android.server.AccessibilityManagerInternal
        public void setImeSessionEnabled(SparseArray<IAccessibilityInputMethodSession> sparseArray, boolean z) {
            this.mService.scheduleSetImeSessionEnabled(sparseArray, z);
        }

        @Override // com.android.server.AccessibilityManagerInternal
        public void unbindInput() {
            this.mService.scheduleUnbindInput();
        }

        @Override // com.android.server.AccessibilityManagerInternal
        public void bindInput() {
            this.mService.scheduleBindInput();
        }

        @Override // com.android.server.AccessibilityManagerInternal
        public void createImeSession(ArraySet<Integer> arraySet) {
            this.mService.scheduleCreateImeSession(arraySet);
        }

        @Override // com.android.server.AccessibilityManagerInternal
        public void startInput(IRemoteAccessibilityInputConnection iRemoteAccessibilityInputConnection, EditorInfo editorInfo, boolean z) {
            this.mService.scheduleStartInput(iRemoteAccessibilityInputConnection, editorInfo, z);
        }

        @Override // com.android.server.AccessibilityManagerInternal
        public void performSystemAction(int i) {
            this.mService.getSystemActionPerformer().performSystemAction(i);
        }

        @Override // com.android.server.AccessibilityManagerInternal
        public boolean isTouchExplorationEnabled(int i) {
            boolean isTouchExplorationEnabledLocked;
            synchronized (this.mService.mLock) {
                isTouchExplorationEnabledLocked = this.mService.getUserStateLocked(i).isTouchExplorationEnabledLocked();
            }
            return isTouchExplorationEnabledLocked;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Lifecycle extends SystemService {
        private final AccessibilityManagerService mService;

        public Lifecycle(Context context) {
            super(context);
            this.mService = new AccessibilityManagerService(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            LocalServices.addService(AccessibilityManagerInternal.class, new LocalServiceImpl(this.mService));
            publishBinderService("accessibility", this.mService);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int i) {
            this.mService.onBootPhase(i);
        }
    }

    @VisibleForTesting
    AccessibilityManagerService(Context context, Handler handler, PackageManager packageManager, AccessibilitySecurityPolicy accessibilitySecurityPolicy, SystemActionPerformer systemActionPerformer, AccessibilityWindowManager accessibilityWindowManager, AccessibilityDisplayListener accessibilityDisplayListener, MagnificationController magnificationController, AccessibilityInputFilter accessibilityInputFilter, ProxyManager proxyManager) {
        Object obj = new Object();
        this.mLock = obj;
        this.mStringColonSplitter = new TextUtils.SimpleStringSplitter(COMPONENT_NAME_SEPARATOR);
        this.mTempRect = new Rect();
        this.mTempRect1 = new Rect();
        this.mTempComponentNameSet = new HashSet();
        this.mTempAccessibilityServiceInfoList = new ArrayList();
        this.mTempIntArray = new IntArray(0);
        this.mGlobalClients = new RemoteCallbackList<>();
        this.mUserStates = new SparseArray<>();
        this.mUiAutomationManager = new UiAutomationManager(obj);
        this.mSendWindowStateChangedEventRunnables = new ArrayList();
        this.mCurrentUserId = 0;
        this.mRealCurrentUserId = -2;
        this.mServiceExt = (IAccessibilityManagerServiceExt) ExtLoader.type(IAccessibilityManagerServiceExt.class).create();
        this.mTempPoint = new Point();
        this.mA11yOverlayLayers = new SparseArray<>();
        this.mContext = context;
        this.mPowerManager = (PowerManager) context.getSystemService("power");
        WindowManagerInternal windowManagerInternal = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
        this.mWindowManagerService = windowManagerInternal;
        this.mTraceManager = AccessibilityTraceManager.getInstance(windowManagerInternal.getAccessibilityController(), this, obj);
        this.mMainHandler = handler;
        this.mActivityTaskManagerService = (ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class);
        this.mPackageManager = packageManager;
        this.mSecurityPolicy = accessibilitySecurityPolicy;
        this.mSystemActionPerformer = systemActionPerformer;
        this.mA11yWindowManager = accessibilityWindowManager;
        this.mA11yDisplayListener = accessibilityDisplayListener;
        this.mMagnificationController = magnificationController;
        this.mMagnificationProcessor = new MagnificationProcessor(magnificationController);
        this.mCaptioningManagerImpl = new CaptioningManagerImpl(context);
        this.mProxyManager = proxyManager;
        if (accessibilityInputFilter != null) {
            this.mInputFilter = accessibilityInputFilter;
            this.mHasInputFilter = true;
        }
        this.mFlashNotificationsController = new FlashNotificationsController(context);
        this.mUmi = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        this.mVisibleBgUserIds = null;
        init();
    }

    public AccessibilityManagerService(Context context) {
        Object obj = new Object();
        this.mLock = obj;
        this.mStringColonSplitter = new TextUtils.SimpleStringSplitter(COMPONENT_NAME_SEPARATOR);
        this.mTempRect = new Rect();
        this.mTempRect1 = new Rect();
        this.mTempComponentNameSet = new HashSet();
        this.mTempAccessibilityServiceInfoList = new ArrayList();
        this.mTempIntArray = new IntArray(0);
        this.mGlobalClients = new RemoteCallbackList<>();
        this.mUserStates = new SparseArray<>();
        UiAutomationManager uiAutomationManager = new UiAutomationManager(obj);
        this.mUiAutomationManager = uiAutomationManager;
        this.mSendWindowStateChangedEventRunnables = new ArrayList();
        this.mCurrentUserId = 0;
        this.mRealCurrentUserId = -2;
        this.mServiceExt = (IAccessibilityManagerServiceExt) ExtLoader.type(IAccessibilityManagerServiceExt.class).create();
        this.mTempPoint = new Point();
        this.mA11yOverlayLayers = new SparseArray<>();
        this.mContext = context;
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        WindowManagerInternal windowManagerInternal = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
        this.mWindowManagerService = windowManagerInternal;
        AccessibilityTraceManager accessibilityTraceManager = AccessibilityTraceManager.getInstance(windowManagerInternal.getAccessibilityController(), this, obj);
        this.mTraceManager = accessibilityTraceManager;
        MainHandler mainHandler = new MainHandler(context.getMainLooper());
        this.mMainHandler = mainHandler;
        this.mActivityTaskManagerService = (ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class);
        this.mPackageManager = context.getPackageManager();
        AccessibilitySecurityPolicy accessibilitySecurityPolicy = new AccessibilitySecurityPolicy(new PolicyWarningUIController(mainHandler, context, new PolicyWarningUIController.NotificationController(context)), context, this, (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class));
        this.mSecurityPolicy = accessibilitySecurityPolicy;
        AccessibilityWindowManager accessibilityWindowManager = new AccessibilityWindowManager(obj, mainHandler, windowManagerInternal, this, accessibilitySecurityPolicy, this, accessibilityTraceManager);
        this.mA11yWindowManager = accessibilityWindowManager;
        this.mA11yDisplayListener = new AccessibilityDisplayListener(context, mainHandler);
        MagnificationController magnificationController = new MagnificationController(this, obj, context, new MagnificationScaleProvider(context), Executors.newSingleThreadExecutor());
        this.mMagnificationController = magnificationController;
        this.mMagnificationProcessor = new MagnificationProcessor(magnificationController);
        this.mCaptioningManagerImpl = new CaptioningManagerImpl(context);
        this.mProxyManager = new ProxyManager(obj, accessibilityWindowManager, context, mainHandler, uiAutomationManager, this);
        this.mFlashNotificationsController = new FlashNotificationsController(context);
        UserManagerInternal userManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        this.mUmi = userManagerInternal;
        if (UserManager.isVisibleBackgroundUsersEnabled()) {
            this.mVisibleBgUserIds = new SparseBooleanArray();
            userManagerInternal.addUserVisibilityListener(new UserManagerInternal.UserVisibilityListener() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda31
                public final void onUserVisibilityChanged(int i, boolean z) {
                    AccessibilityManagerService.this.lambda$new$0(i, z);
                }
            });
        } else {
            this.mVisibleBgUserIds = null;
        }
        init();
    }

    private void init() {
        this.mSecurityPolicy.setAccessibilityWindowManager(this.mA11yWindowManager);
        registerBroadcastReceivers();
        new AccessibilityContentObserver(this.mMainHandler).register(this.mContext.getContentResolver());
        disableAccessibilityMenuToMigrateIfNeeded();
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport, com.android.server.accessibility.AccessibilitySecurityPolicy.AccessibilityUserManager
    public int getCurrentUserIdLocked() {
        return this.mCurrentUserId;
    }

    @Override // com.android.server.accessibility.AccessibilitySecurityPolicy.AccessibilityUserManager
    @GuardedBy({"mLock"})
    public SparseBooleanArray getVisibleUserIdsLocked() {
        return this.mVisibleBgUserIds;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public boolean isAccessibilityButtonShown() {
        return this.mIsAccessibilityButtonShown;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public Pair<float[], MagnificationSpec> getWindowTransformationMatrixAndMagnificationSpec(int i) {
        WindowInfo findWindowInfoByIdLocked;
        IBinder windowTokenForUserAndWindowIdLocked;
        synchronized (this.mLock) {
            findWindowInfoByIdLocked = this.mA11yWindowManager.findWindowInfoByIdLocked(i);
        }
        if (findWindowInfoByIdLocked != null) {
            MagnificationSpec magnificationSpec = new MagnificationSpec();
            magnificationSpec.setTo(findWindowInfoByIdLocked.mMagnificationSpec);
            return new Pair<>(findWindowInfoByIdLocked.mTransformMatrix, magnificationSpec);
        }
        synchronized (this.mLock) {
            windowTokenForUserAndWindowIdLocked = this.mA11yWindowManager.getWindowTokenForUserAndWindowIdLocked(this.mCurrentUserId, i);
        }
        Pair windowTransformationMatrixAndMagnificationSpec = this.mWindowManagerService.getWindowTransformationMatrixAndMagnificationSpec(windowTokenForUserAndWindowIdLocked);
        float[] fArr = new float[9];
        Matrix matrix = (Matrix) windowTransformationMatrixAndMagnificationSpec.first;
        MagnificationSpec magnificationSpec2 = (MagnificationSpec) windowTransformationMatrixAndMagnificationSpec.second;
        if (!magnificationSpec2.isNop()) {
            float f = magnificationSpec2.scale;
            matrix.postScale(f, f);
            matrix.postTranslate(magnificationSpec2.offsetX, magnificationSpec2.offsetY);
        }
        matrix.getValues(fArr);
        return new Pair<>(fArr, (MagnificationSpec) windowTransformationMatrixAndMagnificationSpec.second);
    }

    public IAccessibilityManager.WindowTransformationSpec getWindowTransformationSpec(int i) {
        IAccessibilityManager.WindowTransformationSpec windowTransformationSpec = new IAccessibilityManager.WindowTransformationSpec();
        Pair<float[], MagnificationSpec> windowTransformationMatrixAndMagnificationSpec = getWindowTransformationMatrixAndMagnificationSpec(i);
        windowTransformationSpec.transformationMatrix = (float[]) windowTransformationMatrixAndMagnificationSpec.first;
        windowTransformationSpec.magnificationSpec = (MagnificationSpec) windowTransformationMatrixAndMagnificationSpec.second;
        return windowTransformationSpec;
    }

    @Override // com.android.server.accessibility.AccessibilityUserState.ServiceInfoChangeListener
    public void onServiceInfoChangedLocked(AccessibilityUserState accessibilityUserState) {
        this.mSecurityPolicy.onBoundServicesChangedLocked(accessibilityUserState.mUserId, accessibilityUserState.mBoundServices);
        scheduleNotifyClientsOfServicesStateChangeLocked(accessibilityUserState);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public FingerprintGestureDispatcher getFingerprintGestureDispatcher() {
        return this.mFingerprintGestureDispatcher;
    }

    public void onInputFilterInstalled(boolean z) {
        synchronized (this.mLock) {
            this.mInputFilterInstalled = z;
            this.mLock.notifyAll();
        }
    }

    public void onBootPhase(int i) {
        if (i == 500 && this.mPackageManager.hasSystemFeature("android.software.app_widgets")) {
            this.mSecurityPolicy.setAppWidgetManager((AppWidgetManagerInternal) LocalServices.getService(AppWidgetManagerInternal.class));
        }
        if (i == 600) {
            setNonA11yToolNotificationToMatchSafetyCenter();
        }
    }

    public void setNonA11yToolNotificationToMatchSafetyCenter() {
        boolean z = !((SafetyCenterManager) this.mContext.getSystemService(SafetyCenterManager.class)).isSafetyCenterEnabled();
        synchronized (this.mLock) {
            this.mSecurityPolicy.setSendingNonA11yToolNotificationLocked(z);
        }
    }

    public AccessibilityUserState getCurrentUserState() {
        AccessibilityUserState currentUserStateLocked;
        synchronized (this.mLock) {
            currentUserStateLocked = getCurrentUserStateLocked();
        }
        return currentUserStateLocked;
    }

    private AccessibilityUserState getUserState(int i) {
        AccessibilityUserState userStateLocked;
        synchronized (this.mLock) {
            userStateLocked = getUserStateLocked(i);
        }
        return userStateLocked;
    }

    public AccessibilityUserState getUserStateLocked(int i) {
        AccessibilityUserState accessibilityUserState = this.mUserStates.get(i);
        if (accessibilityUserState != null) {
            return accessibilityUserState;
        }
        AccessibilityUserState accessibilityUserState2 = new AccessibilityUserState(i, this.mContext, this);
        this.mUserStates.put(i, accessibilityUserState2);
        return accessibilityUserState2;
    }

    public boolean getBindInstantServiceAllowed(int i) {
        boolean bindInstantServiceAllowedLocked;
        synchronized (this.mLock) {
            bindInstantServiceAllowedLocked = getUserStateLocked(i).getBindInstantServiceAllowedLocked();
        }
        return bindInstantServiceAllowedLocked;
    }

    public void setBindInstantServiceAllowed(int i, boolean z) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_BIND_INSTANT_SERVICE", "setBindInstantServiceAllowed");
        synchronized (this.mLock) {
            AccessibilityUserState userStateLocked = getUserStateLocked(i);
            if (z != userStateLocked.getBindInstantServiceAllowedLocked()) {
                userStateLocked.setBindInstantServiceAllowedLocked(z);
                onUserStateChangedLocked(userStateLocked);
            }
        }
    }

    public void onSomePackagesChangedLocked() {
        AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
        currentUserStateLocked.mInstalledServices.clear();
        if (readConfigurationForUserStateLocked(currentUserStateLocked)) {
            onUserStateChangedLocked(currentUserStateLocked);
        }
    }

    /* renamed from: com.android.server.accessibility.AccessibilityManagerService$1 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass1 extends PackageMonitor {
        AnonymousClass1() {
        }

        public void onPackageAdded(String str, int i) {
            if (AccessibilityManagerService.this.mTraceManager.isA11yTracingEnabledForTypes(32768L)) {
                AccessibilityManagerService.this.mTraceManager.logTrace("AccessibilityManagerService.PM.onPackageAdded", 32768L, "packageName=" + str + ";uid=" + i);
            }
            synchronized (AccessibilityManagerService.this.mLock) {
                if (getChangingUserId() == AccessibilityManagerService.this.mCurrentUserId && AccessibilityManagerService.this.mCurrentUserId != 0) {
                    AccessibilityManagerService.this.onSomePackagesChangedLocked();
                }
            }
        }

        public void onSomePackagesChanged() {
            if (AccessibilityManagerService.this.mTraceManager.isA11yTracingEnabledForTypes(32768L)) {
                AccessibilityManagerService.this.mTraceManager.logTrace("AccessibilityManagerService.PM.onSomePackagesChanged", 32768L);
            }
            synchronized (AccessibilityManagerService.this.mLock) {
                if (getChangingUserId() != AccessibilityManagerService.this.mCurrentUserId) {
                    return;
                }
                AccessibilityManagerService.this.onSomePackagesChangedLocked();
            }
        }

        public void onPackageUpdateFinished(final String str, int i) {
            boolean z;
            if (AccessibilityManagerService.this.mTraceManager.isA11yTracingEnabledForTypes(32768L)) {
                AccessibilityManagerService.this.mTraceManager.logTrace("AccessibilityManagerService.PM.onPackageUpdateFinished", 32768L, "packageName=" + str + ";uid=" + i);
            }
            synchronized (AccessibilityManagerService.this.mLock) {
                int changingUserId = getChangingUserId();
                if (changingUserId != AccessibilityManagerService.this.mCurrentUserId) {
                    return;
                }
                AccessibilityUserState userStateLocked = AccessibilityManagerService.this.getUserStateLocked(changingUserId);
                if (!userStateLocked.getBindingServicesLocked().removeIf(new Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$1$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$onPackageUpdateFinished$0;
                        lambda$onPackageUpdateFinished$0 = AccessibilityManagerService.AnonymousClass1.lambda$onPackageUpdateFinished$0(str, (ComponentName) obj);
                        return lambda$onPackageUpdateFinished$0;
                    }
                }) && !userStateLocked.mCrashedServices.removeIf(new Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$1$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$onPackageUpdateFinished$1;
                        lambda$onPackageUpdateFinished$1 = AccessibilityManagerService.AnonymousClass1.lambda$onPackageUpdateFinished$1(str, (ComponentName) obj);
                        return lambda$onPackageUpdateFinished$1;
                    }
                })) {
                    z = false;
                    userStateLocked.mInstalledServices.clear();
                    boolean readConfigurationForUserStateLocked = AccessibilityManagerService.this.readConfigurationForUserStateLocked(userStateLocked);
                    if (!z || readConfigurationForUserStateLocked) {
                        AccessibilityManagerService.this.onUserStateChangedLocked(userStateLocked);
                    }
                    AccessibilityManagerService.this.migrateAccessibilityButtonSettingsIfNecessaryLocked(userStateLocked, str, 0);
                }
                z = true;
                userStateLocked.mInstalledServices.clear();
                boolean readConfigurationForUserStateLocked2 = AccessibilityManagerService.this.readConfigurationForUserStateLocked(userStateLocked);
                if (!z) {
                }
                AccessibilityManagerService.this.onUserStateChangedLocked(userStateLocked);
                AccessibilityManagerService.this.migrateAccessibilityButtonSettingsIfNecessaryLocked(userStateLocked, str, 0);
            }
        }

        public static /* synthetic */ boolean lambda$onPackageUpdateFinished$0(String str, ComponentName componentName) {
            return componentName != null && componentName.getPackageName().equals(str);
        }

        public static /* synthetic */ boolean lambda$onPackageUpdateFinished$1(String str, ComponentName componentName) {
            return componentName != null && componentName.getPackageName().equals(str);
        }

        public void onPackageRemoved(final String str, int i) {
            if (AccessibilityManagerService.this.mTraceManager.isA11yTracingEnabledForTypes(32768L)) {
                AccessibilityManagerService.this.mTraceManager.logTrace("AccessibilityManagerService.PM.onPackageRemoved", 32768L, "packageName=" + str + ";uid=" + i);
            }
            synchronized (AccessibilityManagerService.this.mLock) {
                int changingUserId = getChangingUserId();
                if (changingUserId != AccessibilityManagerService.this.mCurrentUserId) {
                    return;
                }
                AccessibilityUserState userStateLocked = AccessibilityManagerService.this.getUserStateLocked(changingUserId);
                Predicate<? super ComponentName> predicate = new Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$1$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$onPackageRemoved$2;
                        lambda$onPackageRemoved$2 = AccessibilityManagerService.AnonymousClass1.lambda$onPackageRemoved$2(str, (ComponentName) obj);
                        return lambda$onPackageRemoved$2;
                    }
                };
                userStateLocked.mBindingServices.removeIf(predicate);
                userStateLocked.mCrashedServices.removeIf(predicate);
                Iterator<ComponentName> it = userStateLocked.mEnabledServices.iterator();
                boolean z = false;
                while (it.hasNext()) {
                    ComponentName next = it.next();
                    if (next.getPackageName().equals(str)) {
                        it.remove();
                        userStateLocked.mTouchExplorationGrantedServices.remove(next);
                        z = true;
                    }
                }
                if (z) {
                    AccessibilityManagerService.this.persistComponentNamesToSettingLocked("enabled_accessibility_services", userStateLocked.mEnabledServices, changingUserId);
                    AccessibilityManagerService.this.persistComponentNamesToSettingLocked("touch_exploration_granted_accessibility_services", userStateLocked.mTouchExplorationGrantedServices, changingUserId);
                    AccessibilityManagerService.this.onUserStateChangedLocked(userStateLocked);
                }
            }
        }

        public static /* synthetic */ boolean lambda$onPackageRemoved$2(String str, ComponentName componentName) {
            return componentName != null && componentName.getPackageName().equals(str);
        }

        public boolean onHandleForceStop(Intent intent, String[] strArr, int i, boolean z) {
            if (AccessibilityManagerService.this.mTraceManager.isA11yTracingEnabledForTypes(32768L)) {
                AccessibilityManagerService.this.mTraceManager.logTrace("AccessibilityManagerService.PM.onHandleForceStop", 32768L, "intent=" + intent + ";packages=" + Arrays.toString(strArr) + ";uid=" + i + ";doit=" + z);
            }
            synchronized (AccessibilityManagerService.this.mLock) {
                int changingUserId = getChangingUserId();
                if (changingUserId != AccessibilityManagerService.this.mCurrentUserId) {
                    return false;
                }
                AccessibilityUserState userStateLocked = AccessibilityManagerService.this.getUserStateLocked(changingUserId);
                Iterator<ComponentName> it = userStateLocked.mEnabledServices.iterator();
                while (it.hasNext()) {
                    ComponentName next = it.next();
                    String packageName = next.getPackageName();
                    for (String str : strArr) {
                        if (packageName.equals(str)) {
                            if (!z) {
                                return true;
                            }
                            it.remove();
                            userStateLocked.getBindingServicesLocked().remove(next);
                            userStateLocked.getCrashedServicesLocked().remove(next);
                            AccessibilityManagerService.this.persistComponentNamesToSettingLocked("enabled_accessibility_services", userStateLocked.mEnabledServices, changingUserId);
                            AccessibilityManagerService.this.onUserStateChangedLocked(userStateLocked);
                        }
                    }
                }
                return false;
            }
        }
    }

    private void registerBroadcastReceivers() {
        this.mServiceExt.hookPackageMonitorRegister(this.mContext, new AnonymousClass1());
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        if (packageManagerInternal != null) {
            packageManagerInternal.getPackageList(new PackageManagerInternal.PackageListObserver() { // from class: com.android.server.accessibility.AccessibilityManagerService.2
                AnonymousClass2() {
                }

                @Override // android.content.pm.PackageManagerInternal.PackageListObserver
                public void onPackageAdded(String str, int i) {
                    int userId = UserHandle.getUserId(i);
                    synchronized (AccessibilityManagerService.this.mLock) {
                        if (userId == AccessibilityManagerService.this.mCurrentUserId) {
                            AccessibilityManagerService.this.onSomePackagesChangedLocked();
                        }
                    }
                }
            });
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        intentFilter.addAction("android.intent.action.USER_REMOVED");
        intentFilter.addAction("android.os.action.SETTING_RESTORED");
        this.mContext.registerReceiverAsUser(new BroadcastReceiver() { // from class: com.android.server.accessibility.AccessibilityManagerService.3
            AnonymousClass3() {
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (AccessibilityManagerService.this.mTraceManager.isA11yTracingEnabledForTypes(65536L)) {
                    AccessibilityManagerService.this.mTraceManager.logTrace("AccessibilityManagerService.BR.onReceive", 65536L, "context=" + context + ";intent=" + intent);
                }
                String action = intent.getAction();
                if ("android.intent.action.USER_SWITCHED".equals(action)) {
                    AccessibilityManagerService.this.switchUser(intent.getIntExtra("android.intent.extra.user_handle", 0));
                    return;
                }
                if ("android.intent.action.USER_UNLOCKED".equals(action)) {
                    AccessibilityManagerService.this.unlockUser(intent.getIntExtra("android.intent.extra.user_handle", 0));
                    return;
                }
                if ("android.intent.action.USER_REMOVED".equals(action)) {
                    AccessibilityManagerService.this.removeUser(intent.getIntExtra("android.intent.extra.user_handle", 0));
                    return;
                }
                if ("android.os.action.SETTING_RESTORED".equals(action)) {
                    String stringExtra = intent.getStringExtra("setting_name");
                    if ("enabled_accessibility_services".equals(stringExtra)) {
                        synchronized (AccessibilityManagerService.this.mLock) {
                            AccessibilityManagerService.this.restoreEnabledAccessibilityServicesLocked(intent.getStringExtra("previous_value"), intent.getStringExtra("new_value"), intent.getIntExtra("restored_from_sdk_int", 0));
                        }
                    } else if ("accessibility_display_magnification_navbar_enabled".equals(stringExtra)) {
                        synchronized (AccessibilityManagerService.this.mLock) {
                            AccessibilityManagerService.this.restoreLegacyDisplayMagnificationNavBarIfNeededLocked(intent.getStringExtra("new_value"), intent.getIntExtra("restored_from_sdk_int", 0));
                        }
                    } else if ("accessibility_button_targets".equals(stringExtra)) {
                        synchronized (AccessibilityManagerService.this.mLock) {
                            AccessibilityManagerService.this.restoreAccessibilityButtonTargetsLocked(intent.getStringExtra("previous_value"), intent.getStringExtra("new_value"));
                        }
                    }
                }
            }
        }, UserHandle.ALL, intentFilter, null, null);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.safetycenter.action.SAFETY_CENTER_ENABLED_CHANGED");
        this.mContext.registerReceiverAsUser(new BroadcastReceiver() { // from class: com.android.server.accessibility.AccessibilityManagerService.4
            AnonymousClass4() {
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                AccessibilityManagerService.this.setNonA11yToolNotificationToMatchSafetyCenter();
            }
        }, UserHandle.ALL, intentFilter2, null, this.mMainHandler, 2);
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.accessibility.AccessibilityManagerService.5
            AnonymousClass5() {
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                AccessibilityManagerService.this.mProxyManager.clearConnections(intent.getIntExtra("android.companion.virtual.extra.VIRTUAL_DEVICE_ID", 0));
            }
        }, new IntentFilter("android.companion.virtual.action.VIRTUAL_DEVICE_REMOVED"), 4);
    }

    /* renamed from: com.android.server.accessibility.AccessibilityManagerService$2 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass2 implements PackageManagerInternal.PackageListObserver {
        AnonymousClass2() {
        }

        @Override // android.content.pm.PackageManagerInternal.PackageListObserver
        public void onPackageAdded(String str, int i) {
            int userId = UserHandle.getUserId(i);
            synchronized (AccessibilityManagerService.this.mLock) {
                if (userId == AccessibilityManagerService.this.mCurrentUserId) {
                    AccessibilityManagerService.this.onSomePackagesChangedLocked();
                }
            }
        }
    }

    /* renamed from: com.android.server.accessibility.AccessibilityManagerService$3 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass3 extends BroadcastReceiver {
        AnonymousClass3() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (AccessibilityManagerService.this.mTraceManager.isA11yTracingEnabledForTypes(65536L)) {
                AccessibilityManagerService.this.mTraceManager.logTrace("AccessibilityManagerService.BR.onReceive", 65536L, "context=" + context + ";intent=" + intent);
            }
            String action = intent.getAction();
            if ("android.intent.action.USER_SWITCHED".equals(action)) {
                AccessibilityManagerService.this.switchUser(intent.getIntExtra("android.intent.extra.user_handle", 0));
                return;
            }
            if ("android.intent.action.USER_UNLOCKED".equals(action)) {
                AccessibilityManagerService.this.unlockUser(intent.getIntExtra("android.intent.extra.user_handle", 0));
                return;
            }
            if ("android.intent.action.USER_REMOVED".equals(action)) {
                AccessibilityManagerService.this.removeUser(intent.getIntExtra("android.intent.extra.user_handle", 0));
                return;
            }
            if ("android.os.action.SETTING_RESTORED".equals(action)) {
                String stringExtra = intent.getStringExtra("setting_name");
                if ("enabled_accessibility_services".equals(stringExtra)) {
                    synchronized (AccessibilityManagerService.this.mLock) {
                        AccessibilityManagerService.this.restoreEnabledAccessibilityServicesLocked(intent.getStringExtra("previous_value"), intent.getStringExtra("new_value"), intent.getIntExtra("restored_from_sdk_int", 0));
                    }
                } else if ("accessibility_display_magnification_navbar_enabled".equals(stringExtra)) {
                    synchronized (AccessibilityManagerService.this.mLock) {
                        AccessibilityManagerService.this.restoreLegacyDisplayMagnificationNavBarIfNeededLocked(intent.getStringExtra("new_value"), intent.getIntExtra("restored_from_sdk_int", 0));
                    }
                } else if ("accessibility_button_targets".equals(stringExtra)) {
                    synchronized (AccessibilityManagerService.this.mLock) {
                        AccessibilityManagerService.this.restoreAccessibilityButtonTargetsLocked(intent.getStringExtra("previous_value"), intent.getStringExtra("new_value"));
                    }
                }
            }
        }
    }

    /* renamed from: com.android.server.accessibility.AccessibilityManagerService$4 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass4 extends BroadcastReceiver {
        AnonymousClass4() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            AccessibilityManagerService.this.setNonA11yToolNotificationToMatchSafetyCenter();
        }
    }

    /* renamed from: com.android.server.accessibility.AccessibilityManagerService$5 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass5 extends BroadcastReceiver {
        AnonymousClass5() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            AccessibilityManagerService.this.mProxyManager.clearConnections(intent.getIntExtra("android.companion.virtual.extra.VIRTUAL_DEVICE_ID", 0));
        }
    }

    private void disableAccessibilityMenuToMigrateIfNeeded() {
        int i;
        synchronized (this.mLock) {
            i = this.mCurrentUserId;
        }
        ComponentName accessibilityMenuComponentToMigrate = AccessibilityUtils.getAccessibilityMenuComponentToMigrate(this.mPackageManager, i);
        if (accessibilityMenuComponentToMigrate != null) {
            this.mPackageManager.setComponentEnabledSetting(accessibilityMenuComponentToMigrate, 2, 1);
        }
    }

    public void restoreLegacyDisplayMagnificationNavBarIfNeededLocked(String str, int i) {
        if (i >= 30) {
            return;
        }
        try {
            boolean z = Integer.parseInt(str) == 1;
            AccessibilityUserState userStateLocked = getUserStateLocked(0);
            ArraySet arraySet = new ArraySet();
            readColonDelimitedSettingToSet("accessibility_button_targets", userStateLocked.mUserId, new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda15
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    String lambda$restoreLegacyDisplayMagnificationNavBarIfNeededLocked$1;
                    lambda$restoreLegacyDisplayMagnificationNavBarIfNeededLocked$1 = AccessibilityManagerService.lambda$restoreLegacyDisplayMagnificationNavBarIfNeededLocked$1((String) obj);
                    return lambda$restoreLegacyDisplayMagnificationNavBarIfNeededLocked$1;
                }
            }, arraySet);
            if (arraySet.contains("com.android.server.accessibility.MagnificationController") == z) {
                return;
            }
            if (z) {
                arraySet.add("com.android.server.accessibility.MagnificationController");
            } else {
                arraySet.remove("com.android.server.accessibility.MagnificationController");
            }
            persistColonDelimitedSetToSettingLocked("accessibility_button_targets", userStateLocked.mUserId, arraySet, new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda16
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    String lambda$restoreLegacyDisplayMagnificationNavBarIfNeededLocked$2;
                    lambda$restoreLegacyDisplayMagnificationNavBarIfNeededLocked$2 = AccessibilityManagerService.lambda$restoreLegacyDisplayMagnificationNavBarIfNeededLocked$2((String) obj);
                    return lambda$restoreLegacyDisplayMagnificationNavBarIfNeededLocked$2;
                }
            });
            readAccessibilityButtonTargetsLocked(userStateLocked);
            onUserStateChangedLocked(userStateLocked);
        } catch (NumberFormatException e) {
            Slog.w(LOG_TAG, "number format is incorrect" + e);
        }
    }

    public long addClient(IAccessibilityManagerClient iAccessibilityManagerClient, int i) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.addClient", 4L, "callback=" + iAccessibilityManagerClient + ";userId=" + i);
        }
        this.mServiceExt.addProxyBinder(iAccessibilityManagerClient.asBinder(), iAccessibilityManagerClient, Binder.getCallingUid(), Binder.getCallingPid());
        synchronized (this.mLock) {
            int resolveCallingUserIdEnforcingPermissionsLocked = this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(i);
            AccessibilityUserState userStateLocked = getUserStateLocked(resolveCallingUserIdEnforcingPermissionsLocked);
            int firstDeviceIdForUidLocked = this.mProxyManager.getFirstDeviceIdForUidLocked(Binder.getCallingUid());
            Client client = new Client(iAccessibilityManagerClient, Binder.getCallingUid(), userStateLocked, firstDeviceIdForUidLocked);
            if (this.mSecurityPolicy.isCallerInteractingAcrossUsers(i)) {
                if (this.mProxyManager.isProxyedDeviceId(firstDeviceIdForUidLocked)) {
                    return IntPair.of(this.mProxyManager.getStateLocked(firstDeviceIdForUidLocked), client.mLastSentRelevantEventTypes);
                }
                this.mGlobalClients.register(iAccessibilityManagerClient, client);
            } else {
                if (this.mProxyManager.isProxyedDeviceId(firstDeviceIdForUidLocked)) {
                    return IntPair.of(this.mProxyManager.getStateLocked(firstDeviceIdForUidLocked), client.mLastSentRelevantEventTypes);
                }
                userStateLocked.mUserClients.register(iAccessibilityManagerClient, client);
            }
            return IntPair.of(resolveCallingUserIdEnforcingPermissionsLocked == this.mCurrentUserId ? getClientStateLocked(userStateLocked) : 0, client.mLastSentRelevantEventTypes);
        }
    }

    public boolean removeClient(IAccessibilityManagerClient iAccessibilityManagerClient, int i) {
        this.mServiceExt.removeProxyBinder(iAccessibilityManagerClient.asBinder(), iAccessibilityManagerClient);
        synchronized (this.mLock) {
            AccessibilityUserState userStateLocked = getUserStateLocked(this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(i));
            if (this.mSecurityPolicy.isCallerInteractingAcrossUsers(i)) {
                return this.mGlobalClients.unregister(iAccessibilityManagerClient);
            }
            return userStateLocked.mUserClients.unregister(iAccessibilityManagerClient);
        }
    }

    public void sendAccessibilityEvent(AccessibilityEvent accessibilityEvent, int i) {
        int resolveCallingUserIdEnforcingPermissionsLocked;
        boolean z;
        boolean z2;
        AccessibilityWindowInfo pictureInPictureWindowLocked;
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.sendAccessibilityEvent", 4L, "event=" + accessibilityEvent + ";userId=" + i);
        }
        synchronized (this.mLock) {
            if (accessibilityEvent.getWindowId() == -3 && (pictureInPictureWindowLocked = this.mA11yWindowManager.getPictureInPictureWindowLocked()) != null) {
                accessibilityEvent.setWindowId(pictureInPictureWindowLocked.getId());
            }
            resolveCallingUserIdEnforcingPermissionsLocked = this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(i);
            accessibilityEvent.setPackageName(this.mSecurityPolicy.resolveValidReportedPackageLocked(accessibilityEvent.getPackageName(), UserHandle.getCallingAppId(), resolveCallingUserIdEnforcingPermissionsLocked, IAccessibilityManager.Stub.getCallingPid()));
            int i2 = this.mCurrentUserId;
            z = true;
            if (resolveCallingUserIdEnforcingPermissionsLocked == i2) {
                if (this.mSecurityPolicy.canDispatchAccessibilityEventLocked(i2, accessibilityEvent)) {
                    this.mA11yWindowManager.updateActiveAndAccessibilityFocusedWindowLocked(this.mCurrentUserId, accessibilityEvent.getWindowId(), accessibilityEvent.getSourceNodeId(), accessibilityEvent.getEventType(), accessibilityEvent.getAction());
                    this.mSecurityPolicy.updateEventSourceLocked(accessibilityEvent);
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (this.mHasInputFilter && this.mInputFilter != null) {
                    this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda8
                        @Override // java.util.function.BiConsumer
                        public final void accept(Object obj, Object obj2) {
                            ((AccessibilityManagerService) obj).sendAccessibilityEventToInputFilter((AccessibilityEvent) obj2);
                        }
                    }, this, AccessibilityEvent.obtain(accessibilityEvent)));
                }
            } else {
                z2 = false;
            }
        }
        if (z2) {
            int displayId = accessibilityEvent.getDisplayId();
            synchronized (this.mLock) {
                int windowId = accessibilityEvent.getWindowId();
                if (windowId != -1 && displayId == -1) {
                    displayId = this.mA11yWindowManager.getDisplayIdByUserIdAndWindowIdLocked(resolveCallingUserIdEnforcingPermissionsLocked, windowId);
                    accessibilityEvent.setDisplayId(displayId);
                }
                if (accessibilityEvent.getEventType() != 32 || displayId == -1 || !this.mA11yWindowManager.isTrackingWindowsLocked(displayId)) {
                    z = false;
                }
            }
            if (z) {
                if (this.mTraceManager.isA11yTracingEnabledForTypes(512L)) {
                    this.mTraceManager.logTrace("WindowManagerInternal.computeWindowsForAccessibility", 512L, "display=" + displayId);
                }
                ((WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class)).computeWindowsForAccessibility(displayId);
                if (postponeWindowStateEvent(accessibilityEvent)) {
                    return;
                }
            }
            synchronized (this.mLock) {
                dispatchAccessibilityEventLocked(accessibilityEvent);
            }
        }
        if (OWN_PROCESS_ID != Binder.getCallingPid()) {
            accessibilityEvent.recycle();
        }
    }

    public void dispatchAccessibilityEventLocked(AccessibilityEvent accessibilityEvent) {
        if (this.mProxyManager.isProxyedDisplay(accessibilityEvent.getDisplayId())) {
            this.mProxyManager.sendAccessibilityEventLocked(accessibilityEvent);
        } else {
            notifyAccessibilityServicesDelayedLocked(accessibilityEvent, false);
            notifyAccessibilityServicesDelayedLocked(accessibilityEvent, true);
        }
        this.mUiAutomationManager.sendAccessibilityEventLocked(accessibilityEvent);
    }

    public void sendAccessibilityEventToInputFilter(AccessibilityEvent accessibilityEvent) {
        AccessibilityInputFilter accessibilityInputFilter;
        synchronized (this.mLock) {
            if (this.mHasInputFilter && (accessibilityInputFilter = this.mInputFilter) != null) {
                accessibilityInputFilter.notifyAccessibilityEvent(accessibilityEvent);
            }
        }
        accessibilityEvent.recycle();
    }

    public void registerSystemAction(RemoteAction remoteAction, int i) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.registerSystemAction", 4L, "action=" + remoteAction + ";actionId=" + i);
        }
        this.mSecurityPolicy.enforceCallingOrSelfPermission("android.permission.MANAGE_ACCESSIBILITY");
        getSystemActionPerformer().registerSystemAction(i, remoteAction);
    }

    public void unregisterSystemAction(int i) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.unregisterSystemAction", 4L, "actionId=" + i);
        }
        this.mSecurityPolicy.enforceCallingOrSelfPermission("android.permission.MANAGE_ACCESSIBILITY");
        getSystemActionPerformer().unregisterSystemAction(i);
    }

    public SystemActionPerformer getSystemActionPerformer() {
        if (this.mSystemActionPerformer == null) {
            this.mSystemActionPerformer = new SystemActionPerformer(this.mContext, this.mWindowManagerService, null, this, this);
        }
        return this.mSystemActionPerformer;
    }

    public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(int i) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getInstalledAccessibilityServiceList", 4L, "userId=" + i);
        }
        synchronized (this.mLock) {
            int firstDeviceIdForUidLocked = this.mProxyManager.getFirstDeviceIdForUidLocked(Binder.getCallingUid());
            if (this.mProxyManager.isProxyedDeviceId(firstDeviceIdForUidLocked)) {
                return this.mProxyManager.getInstalledAndEnabledServiceInfosLocked(-1, firstDeviceIdForUidLocked);
            }
            int resolveCallingUserIdEnforcingPermissionsLocked = this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(i);
            ArrayList arrayList = new ArrayList(this.mServiceExt.getAccessibilityServiceAfterCheckCustomizeWhiteList(this.mContext, getUserStateLocked(resolveCallingUserIdEnforcingPermissionsLocked).mInstalledServices));
            if (Binder.getCallingPid() == OWN_PROCESS_ID) {
                return arrayList;
            }
            PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
            int callingUid = Binder.getCallingUid();
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                if (packageManagerInternal.filterAppAccess(((AccessibilityServiceInfo) arrayList.get(size)).getComponentName().getPackageName(), callingUid, resolveCallingUserIdEnforcingPermissionsLocked)) {
                    arrayList.remove(size);
                }
            }
            return arrayList;
        }
    }

    public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int i, int i2) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getEnabledAccessibilityServiceList", 4L, "feedbackType=" + i + ";userId=" + i2);
        }
        synchronized (this.mLock) {
            int firstDeviceIdForUidLocked = this.mProxyManager.getFirstDeviceIdForUidLocked(Binder.getCallingUid());
            if (this.mProxyManager.isProxyedDeviceId(firstDeviceIdForUidLocked)) {
                return this.mProxyManager.getInstalledAndEnabledServiceInfosLocked(i, firstDeviceIdForUidLocked);
            }
            AccessibilityUserState userStateLocked = getUserStateLocked(this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(i2));
            if (this.mUiAutomationManager.suppressingAccessibilityServicesLocked()) {
                return Collections.emptyList();
            }
            ArrayList<AccessibilityServiceConnection> arrayList = userStateLocked.mBoundServices;
            int size = arrayList.size();
            ArrayList arrayList2 = new ArrayList(size);
            for (int i3 = 0; i3 < size; i3++) {
                AccessibilityServiceConnection accessibilityServiceConnection = arrayList.get(i3);
                if ((accessibilityServiceConnection.mFeedbackType & i) != 0 || i == -1) {
                    arrayList2.add(accessibilityServiceConnection.getServiceInfo());
                }
            }
            return arrayList2;
        }
    }

    public void interrupt(int i) {
        ArrayList arrayList;
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.interrupt", 4L, "userId=" + i);
        }
        synchronized (this.mLock) {
            int resolveCallingUserIdEnforcingPermissionsLocked = this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(i);
            if (resolveCallingUserIdEnforcingPermissionsLocked != this.mCurrentUserId) {
                return;
            }
            int firstDeviceIdForUidLocked = this.mProxyManager.getFirstDeviceIdForUidLocked(Binder.getCallingUid());
            if (this.mProxyManager.isProxyedDeviceId(firstDeviceIdForUidLocked)) {
                arrayList = new ArrayList();
                this.mProxyManager.addServiceInterfacesLocked(arrayList, firstDeviceIdForUidLocked);
            } else {
                ArrayList<AccessibilityServiceConnection> arrayList2 = getUserStateLocked(resolveCallingUserIdEnforcingPermissionsLocked).mBoundServices;
                ArrayList arrayList3 = new ArrayList(arrayList2.size());
                for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                    AccessibilityServiceConnection accessibilityServiceConnection = arrayList2.get(i2);
                    IBinder iBinder = accessibilityServiceConnection.mService;
                    IAccessibilityServiceClient iAccessibilityServiceClient = accessibilityServiceConnection.mServiceInterface;
                    if (iBinder != null && iAccessibilityServiceClient != null) {
                        arrayList3.add(iAccessibilityServiceClient);
                    }
                }
                arrayList = arrayList3;
            }
            int size = arrayList.size();
            for (int i3 = 0; i3 < size; i3++) {
                try {
                    if (this.mTraceManager.isA11yTracingEnabledForTypes(2L)) {
                        this.mTraceManager.logTrace("AccessibilityManagerService.IAccessibilityServiceClient.onInterrupt", 2L);
                    }
                    ((IAccessibilityServiceClient) arrayList.get(i3)).onInterrupt();
                } catch (RemoteException e) {
                    Slog.e(LOG_TAG, "Error sending interrupt request to " + arrayList.get(i3), e);
                }
            }
        }
    }

    public int addAccessibilityInteractionConnection(IWindow iWindow, IBinder iBinder, IAccessibilityInteractionConnection iAccessibilityInteractionConnection, String str, int i) throws RemoteException {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.addAccessibilityInteractionConnection", 4L, "windowToken=" + iWindow + "leashToken=" + iBinder + ";connection=" + iAccessibilityInteractionConnection + "; packageName=" + str + ";userId=" + i);
        }
        return this.mA11yWindowManager.addAccessibilityInteractionConnection(iWindow, iBinder, iAccessibilityInteractionConnection, str, i);
    }

    public void removeAccessibilityInteractionConnection(IWindow iWindow) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.removeAccessibilityInteractionConnection", 4L, "window=" + iWindow);
        }
        this.mA11yWindowManager.removeAccessibilityInteractionConnection(iWindow);
    }

    public void setPictureInPictureActionReplacingConnection(IAccessibilityInteractionConnection iAccessibilityInteractionConnection) throws RemoteException {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.setPictureInPictureActionReplacingConnection", 4L, "connection=" + iAccessibilityInteractionConnection);
        }
        this.mSecurityPolicy.enforceCallingPermission("android.permission.MODIFY_ACCESSIBILITY_DATA", SET_PIP_ACTION_REPLACEMENT);
        this.mA11yWindowManager.setPictureInPictureActionReplacingConnection(iAccessibilityInteractionConnection);
    }

    public void registerUiTestAutomationService(IBinder iBinder, IAccessibilityServiceClient iAccessibilityServiceClient, AccessibilityServiceInfo accessibilityServiceInfo, int i, int i2) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.registerUiTestAutomationService", 4L, "owner=" + iBinder + ";serviceClient=" + iAccessibilityServiceClient + ";accessibilityServiceInfo=" + accessibilityServiceInfo + ";flags=" + i2);
        }
        this.mSecurityPolicy.enforceCallingPermission("android.permission.RETRIEVE_WINDOW_CONTENT", FUNCTION_REGISTER_UI_TEST_AUTOMATION_SERVICE);
        synchronized (this.mLock) {
            changeCurrentUserForTestAutomationIfNeededLocked(i);
            UiAutomationManager uiAutomationManager = this.mUiAutomationManager;
            Context context = this.mContext;
            int i3 = sIdCounter;
            sIdCounter = i3 + 1;
            uiAutomationManager.registerUiTestAutomationServiceLocked(iBinder, iAccessibilityServiceClient, context, accessibilityServiceInfo, i3, this.mMainHandler, this.mSecurityPolicy, this, getTraceManager(), this.mWindowManagerService, getSystemActionPerformer(), this.mA11yWindowManager, i2);
            onUserStateChangedLocked(getCurrentUserStateLocked());
        }
    }

    public void unregisterUiTestAutomationService(IAccessibilityServiceClient iAccessibilityServiceClient) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.unregisterUiTestAutomationService", 4L, "serviceClient=" + iAccessibilityServiceClient);
        }
        synchronized (this.mLock) {
            this.mUiAutomationManager.unregisterUiTestAutomationServiceLocked(iAccessibilityServiceClient);
            restoreCurrentUserAfterTestAutomationIfNeededLocked();
        }
    }

    @GuardedBy({"mLock"})
    private void changeCurrentUserForTestAutomationIfNeededLocked(int i) {
        SparseBooleanArray sparseBooleanArray = this.mVisibleBgUserIds;
        if (sparseBooleanArray == null) {
            Slogf.d(LOG_TAG, "changeCurrentUserForTestAutomationIfNeededLocked(%d): ignoring because device doesn't support visible background users", new Object[]{Integer.valueOf(i)});
            return;
        }
        if (!sparseBooleanArray.get(i)) {
            Slogf.wtf(LOG_TAG, "changeCurrentUserForTestAutomationIfNeededLocked(): cannot change current user to %d as it's not visible (mVisibleUsers=%s)", new Object[]{Integer.valueOf(i), this.mVisibleBgUserIds});
            return;
        }
        int i2 = this.mCurrentUserId;
        if (i2 == i) {
            Slogf.d(LOG_TAG, "changeCurrentUserForTestAutomationIfNeededLocked(): NOT changing current user for test automation purposes as it is already %d", new Object[]{Integer.valueOf(i2)});
            return;
        }
        Slogf.i(LOG_TAG, "changeCurrentUserForTestAutomationIfNeededLocked(): changing current user from %d to %d for test automation purposes", new Object[]{Integer.valueOf(i2), Integer.valueOf(i)});
        this.mRealCurrentUserId = this.mCurrentUserId;
        switchUser(i);
    }

    @GuardedBy({"mLock"})
    private void restoreCurrentUserAfterTestAutomationIfNeededLocked() {
        if (this.mVisibleBgUserIds == null) {
            Slogf.d(LOG_TAG, "restoreCurrentUserForTestAutomationIfNeededLocked(): ignoring because device doesn't support visible background users");
            return;
        }
        int i = this.mRealCurrentUserId;
        if (i == -2) {
            Slogf.d(LOG_TAG, "restoreCurrentUserForTestAutomationIfNeededLocked(): ignoring because mRealCurrentUserId is already USER_CURRENT");
            return;
        }
        Slogf.i(LOG_TAG, "restoreCurrentUserForTestAutomationIfNeededLocked(): restoring current user to %d after using %d for test automation purposes", new Object[]{Integer.valueOf(i), Integer.valueOf(this.mCurrentUserId)});
        int i2 = this.mRealCurrentUserId;
        this.mRealCurrentUserId = -2;
        switchUser(i2);
    }

    public IBinder getWindowToken(int i, int i2) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getWindowToken", 4L, "windowId=" + i + ";userId=" + i2);
        }
        this.mSecurityPolicy.enforceCallingPermission("android.permission.RETRIEVE_WINDOW_TOKEN", GET_WINDOW_TOKEN);
        synchronized (this.mLock) {
            if (this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(i2) != this.mCurrentUserId) {
                return null;
            }
            AccessibilityWindowInfo findA11yWindowInfoByIdLocked = this.mA11yWindowManager.findA11yWindowInfoByIdLocked(i);
            if (findA11yWindowInfoByIdLocked == null) {
                return null;
            }
            return this.mA11yWindowManager.getWindowTokenForUserAndWindowIdLocked(i2, findA11yWindowInfoByIdLocked.getId());
        }
    }

    public void notifyAccessibilityButtonClicked(int i, String str) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.notifyAccessibilityButtonClicked", 4L, "displayId=" + i + ";targetName=" + str);
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.STATUS_BAR_SERVICE") != 0) {
            throw new SecurityException("Caller does not hold permission android.permission.STATUS_BAR_SERVICE");
        }
        if (str == null) {
            synchronized (this.mLock) {
                str = getCurrentUserStateLocked().getTargetAssignedToAccessibilityButton();
            }
        }
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new AccessibilityManagerService$$ExternalSyntheticLambda1(), this, Integer.valueOf(i), 0, str));
    }

    public void notifyAccessibilityButtonVisibilityChanged(boolean z) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.notifyAccessibilityButtonVisibilityChanged", 4L, "shown=" + z);
        }
        this.mSecurityPolicy.enforceCallingOrSelfPermission("android.permission.STATUS_BAR_SERVICE");
        synchronized (this.mLock) {
            notifyAccessibilityButtonVisibilityChangedLocked(z);
        }
    }

    public boolean onGesture(AccessibilityGestureEvent accessibilityGestureEvent) {
        boolean notifyGestureLocked;
        synchronized (this.mLock) {
            notifyGestureLocked = notifyGestureLocked(accessibilityGestureEvent, false);
            if (!notifyGestureLocked) {
                notifyGestureLocked = notifyGestureLocked(accessibilityGestureEvent, true);
            }
        }
        return notifyGestureLocked;
    }

    public boolean sendMotionEventToListeningServices(MotionEvent motionEvent) {
        return scheduleNotifyMotionEvent(MotionEvent.obtain(motionEvent));
    }

    public boolean onTouchStateChanged(int i, int i2) {
        return scheduleNotifyTouchState(i, i2);
    }

    @Override // com.android.server.accessibility.SystemActionPerformer.SystemActionsChangedListener
    public void onSystemActionsChanged() {
        synchronized (this.mLock) {
            notifySystemActionsChangedLocked(getCurrentUserStateLocked());
        }
    }

    @Override // com.android.server.accessibility.SystemActionPerformer.DisplayUpdateCallBack
    public void moveNonProxyTopFocusedDisplayToTopIfNeeded() {
        this.mA11yWindowManager.moveNonProxyTopFocusedDisplayToTopIfNeeded();
    }

    @Override // com.android.server.accessibility.SystemActionPerformer.DisplayUpdateCallBack
    public int getLastNonProxyTopFocusedDisplayId() {
        return this.mA11yWindowManager.getLastNonProxyTopFocusedDisplayId();
    }

    @VisibleForTesting
    void notifySystemActionsChangedLocked(AccessibilityUserState accessibilityUserState) {
        for (int size = accessibilityUserState.mBoundServices.size() - 1; size >= 0; size--) {
            accessibilityUserState.mBoundServices.get(size).notifySystemActionsChangedLocked();
        }
    }

    @VisibleForTesting
    public boolean notifyKeyEvent(KeyEvent keyEvent, int i) {
        synchronized (this.mLock) {
            ArrayList<AccessibilityServiceConnection> arrayList = getCurrentUserStateLocked().mBoundServices;
            if (arrayList.isEmpty()) {
                return false;
            }
            return getKeyEventDispatcher().notifyKeyEventLocked(keyEvent, i, arrayList);
        }
    }

    public void notifyMagnificationChanged(int i, Region region, MagnificationConfig magnificationConfig) {
        synchronized (this.mLock) {
            notifyClearAccessibilityCacheLocked();
            notifyMagnificationChangedLocked(i, region, magnificationConfig);
        }
    }

    public void setMotionEventInjectors(SparseArray<MotionEventInjector> sparseArray) {
        synchronized (this.mLock) {
            this.mMotionEventInjectors = sparseArray;
            this.mLock.notifyAll();
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public MotionEventInjector getMotionEventInjectorForDisplayLocked(int i) {
        long uptimeMillis = SystemClock.uptimeMillis() + 1000;
        while (this.mMotionEventInjectors == null && SystemClock.uptimeMillis() < uptimeMillis) {
            try {
                this.mLock.wait(uptimeMillis - SystemClock.uptimeMillis());
            } catch (InterruptedException unused) {
            }
        }
        SparseArray<MotionEventInjector> sparseArray = this.mMotionEventInjectors;
        if (sparseArray == null) {
            Slog.e(LOG_TAG, "MotionEventInjector installation timed out");
            return null;
        }
        return sparseArray.get(i);
    }

    public boolean getAccessibilityFocusClickPointInScreen(Point point) {
        return getInteractionBridge().getAccessibilityFocusClickPointInScreenNotLocked(point);
    }

    public boolean performActionOnAccessibilityFocusedItem(AccessibilityNodeInfo.AccessibilityAction accessibilityAction) {
        return getInteractionBridge().performActionOnAccessibilityFocusedItemNotLocked(accessibilityAction);
    }

    public boolean accessibilityFocusOnlyInActiveWindow() {
        boolean accessibilityFocusOnlyInActiveWindowLocked;
        synchronized (this.mLock) {
            accessibilityFocusOnlyInActiveWindowLocked = this.mA11yWindowManager.accessibilityFocusOnlyInActiveWindowLocked();
        }
        return accessibilityFocusOnlyInActiveWindowLocked;
    }

    boolean getWindowBounds(int i, Rect rect) {
        IBinder windowToken;
        synchronized (this.mLock) {
            windowToken = getWindowToken(i, this.mCurrentUserId);
        }
        if (this.mTraceManager.isA11yTracingEnabledForTypes(512L)) {
            this.mTraceManager.logTrace("WindowManagerInternal.getWindowFrame", 512L, "token=" + windowToken + ";outBounds=" + rect);
        }
        this.mWindowManagerService.getWindowFrame(windowToken, rect);
        return !rect.isEmpty();
    }

    public int getActiveWindowId() {
        return this.mA11yWindowManager.getActiveWindowId(this.mCurrentUserId);
    }

    public void onTouchInteractionStart() {
        this.mA11yWindowManager.onTouchInteractionStart();
    }

    public void onTouchInteractionEnd() {
        this.mA11yWindowManager.onTouchInteractionEnd();
    }

    public void switchUser(int i) {
        this.mMagnificationController.updateUserIdIfNeeded(i);
        synchronized (this.mLock) {
            if (this.mCurrentUserId == i && this.mInitialized) {
                return;
            }
            AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
            currentUserStateLocked.onSwitchToAnotherUserLocked();
            if (currentUserStateLocked.mUserClients.getRegisteredCallbackCount() > 0) {
                this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda17
                    public final void accept(Object obj, Object obj2, Object obj3) {
                        ((AccessibilityManagerService) obj).sendStateToClients(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
                    }
                }, this, 0, Integer.valueOf(currentUserStateLocked.mUserId)));
            }
            boolean z = true;
            if (((UserManager) this.mContext.getSystemService("user")).getUsers().size() <= 1) {
                z = false;
            }
            this.mCurrentUserId = i;
            AccessibilityUserState currentUserStateLocked2 = getCurrentUserStateLocked();
            readConfigurationForUserStateLocked(currentUserStateLocked2);
            this.mSecurityPolicy.onSwitchUserLocked(this.mCurrentUserId, currentUserStateLocked2.mEnabledServices);
            onUserStateChangedLocked(currentUserStateLocked2);
            migrateAccessibilityButtonSettingsIfNecessaryLocked(currentUserStateLocked2, null, 0);
            if (z) {
                this.mMainHandler.sendMessageDelayed(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda18
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((AccessibilityManagerService) obj).announceNewUserIfNeeded();
                    }
                }, this), 3000L);
            }
        }
    }

    public void announceNewUserIfNeeded() {
        synchronized (this.mLock) {
            if (getCurrentUserStateLocked().isHandlingAccessibilityEventsLocked()) {
                String string = this.mContext.getString(17041811, ((UserManager) this.mContext.getSystemService("user")).getUserInfo(this.mCurrentUserId).name);
                AccessibilityEvent obtain = AccessibilityEvent.obtain(16384);
                obtain.getText().add(string);
                sendAccessibilityEventLocked(obtain, this.mCurrentUserId);
            }
        }
    }

    public void unlockUser(int i) {
        synchronized (this.mLock) {
            int resolveProfileParentLocked = this.mSecurityPolicy.resolveProfileParentLocked(i);
            int i2 = this.mCurrentUserId;
            if (resolveProfileParentLocked == i2) {
                onUserStateChangedLocked(getUserStateLocked(i2));
            }
        }
    }

    public void removeUser(int i) {
        synchronized (this.mLock) {
            this.mUserStates.remove(i);
        }
        getMagnificationController().onUserRemoved(i);
    }

    void restoreEnabledAccessibilityServicesLocked(String str, String str2, int i) {
        readComponentNamesFromStringLocked(str, this.mTempComponentNameSet, false);
        readComponentNamesFromStringLocked(str2, this.mTempComponentNameSet, true);
        AccessibilityUserState userStateLocked = getUserStateLocked(0);
        userStateLocked.mEnabledServices.clear();
        userStateLocked.mEnabledServices.addAll(this.mTempComponentNameSet);
        persistComponentNamesToSettingLocked("enabled_accessibility_services", userStateLocked.mEnabledServices, 0);
        onUserStateChangedLocked(userStateLocked);
        migrateAccessibilityButtonSettingsIfNecessaryLocked(userStateLocked, null, i);
    }

    void restoreAccessibilityButtonTargetsLocked(String str, String str2) {
        ArraySet arraySet = new ArraySet();
        readColonDelimitedStringToSet(str, new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda11
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String lambda$restoreAccessibilityButtonTargetsLocked$3;
                lambda$restoreAccessibilityButtonTargetsLocked$3 = AccessibilityManagerService.lambda$restoreAccessibilityButtonTargetsLocked$3((String) obj);
                return lambda$restoreAccessibilityButtonTargetsLocked$3;
            }
        }, arraySet, false);
        readColonDelimitedStringToSet(str2, new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda12
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String lambda$restoreAccessibilityButtonTargetsLocked$4;
                lambda$restoreAccessibilityButtonTargetsLocked$4 = AccessibilityManagerService.lambda$restoreAccessibilityButtonTargetsLocked$4((String) obj);
                return lambda$restoreAccessibilityButtonTargetsLocked$4;
            }
        }, arraySet, true);
        AccessibilityUserState userStateLocked = getUserStateLocked(0);
        userStateLocked.mAccessibilityButtonTargets.clear();
        userStateLocked.mAccessibilityButtonTargets.addAll((Collection<? extends String>) arraySet);
        persistColonDelimitedSetToSettingLocked("accessibility_button_targets", 0, userStateLocked.mAccessibilityButtonTargets, new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda13
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String lambda$restoreAccessibilityButtonTargetsLocked$5;
                lambda$restoreAccessibilityButtonTargetsLocked$5 = AccessibilityManagerService.lambda$restoreAccessibilityButtonTargetsLocked$5((String) obj);
                return lambda$restoreAccessibilityButtonTargetsLocked$5;
            }
        });
        scheduleNotifyClientsOfServicesStateChangeLocked(userStateLocked);
        onUserStateChangedLocked(userStateLocked);
    }

    private int getClientStateLocked(AccessibilityUserState accessibilityUserState) {
        return accessibilityUserState.getClientStateLocked(this.mUiAutomationManager.isUiAutomationRunningLocked(), this.mTraceManager.getTraceStateForAccessibilityManagerClientState());
    }

    public InteractionBridge getInteractionBridge() {
        InteractionBridge interactionBridge;
        synchronized (this.mLock) {
            if (this.mInteractionBridge == null) {
                this.mInteractionBridge = new InteractionBridge();
            }
            interactionBridge = this.mInteractionBridge;
        }
        return interactionBridge;
    }

    private boolean notifyGestureLocked(AccessibilityGestureEvent accessibilityGestureEvent, boolean z) {
        AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
        for (int size = currentUserStateLocked.mBoundServices.size() - 1; size >= 0; size--) {
            AccessibilityServiceConnection accessibilityServiceConnection = currentUserStateLocked.mBoundServices.get(size);
            if (accessibilityServiceConnection.mRequestTouchExplorationMode && accessibilityServiceConnection.mIsDefault == z) {
                accessibilityServiceConnection.notifyGesture(accessibilityGestureEvent);
                return true;
            }
        }
        return false;
    }

    private boolean scheduleNotifyMotionEvent(MotionEvent motionEvent) {
        boolean z;
        int displayId = motionEvent.getDisplayId();
        synchronized (this.mLock) {
            AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
            z = false;
            for (int size = currentUserStateLocked.mBoundServices.size() - 1; size >= 0; size--) {
                AccessibilityServiceConnection accessibilityServiceConnection = currentUserStateLocked.mBoundServices.get(size);
                if (accessibilityServiceConnection.wantsGenericMotionEvent(motionEvent) || (motionEvent.isFromSource(4098) && accessibilityServiceConnection.isServiceDetectsGesturesEnabled(displayId))) {
                    accessibilityServiceConnection.notifyMotionEvent(motionEvent);
                    z = true;
                }
            }
        }
        return z;
    }

    private boolean scheduleNotifyTouchState(int i, int i2) {
        boolean z;
        synchronized (this.mLock) {
            AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
            z = false;
            for (int size = currentUserStateLocked.mBoundServices.size() - 1; size >= 0; size--) {
                AccessibilityServiceConnection accessibilityServiceConnection = currentUserStateLocked.mBoundServices.get(size);
                if (accessibilityServiceConnection.isServiceDetectsGesturesEnabled(i)) {
                    accessibilityServiceConnection.notifyTouchState(i, i2);
                    z = true;
                }
            }
        }
        return z;
    }

    @Override // com.android.server.accessibility.ProxyManager.SystemSupport
    public void notifyClearAccessibilityCacheLocked() {
        AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
        for (int size = currentUserStateLocked.mBoundServices.size() - 1; size >= 0; size--) {
            currentUserStateLocked.mBoundServices.get(size).notifyClearAccessibilityNodeInfoCache();
        }
        this.mProxyManager.clearCacheLocked();
    }

    private void notifyMagnificationChangedLocked(int i, Region region, MagnificationConfig magnificationConfig) {
        AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
        for (int size = currentUserStateLocked.mBoundServices.size() - 1; size >= 0; size--) {
            currentUserStateLocked.mBoundServices.get(size).notifyMagnificationChangedLocked(i, region, magnificationConfig);
        }
    }

    private void sendAccessibilityButtonToInputFilter(int i) {
        AccessibilityInputFilter accessibilityInputFilter;
        synchronized (this.mLock) {
            if (this.mHasInputFilter && (accessibilityInputFilter = this.mInputFilter) != null) {
                accessibilityInputFilter.notifyAccessibilityButtonClicked(i);
            }
        }
    }

    private void showAccessibilityTargetsSelection(int i, int i2) {
        String name;
        Intent intent = new Intent("com.android.internal.intent.action.CHOOSE_ACCESSIBILITY_BUTTON");
        if (i2 == 1) {
            name = AccessibilityShortcutChooserActivity.class.getName();
        } else {
            name = AccessibilityButtonChooserActivity.class.getName();
        }
        intent.setClassName("android", name);
        intent.addFlags(268468224);
        intent.setComponent(this.mServiceExt.replaceOplusUiIntent(this.mContext, i2, intent.getComponent()));
        this.mContext.startActivityAsUser(intent, ActivityOptions.makeBasic().setLaunchDisplayId(i).toBundle(), UserHandle.of(this.mCurrentUserId));
    }

    private void launchShortcutTargetActivity(int i, ComponentName componentName) {
        Intent intent = new Intent();
        Bundle bundle = ActivityOptions.makeBasic().setLaunchDisplayId(i).toBundle();
        intent.setComponent(componentName);
        intent.addFlags(AudioFormat.EVRC);
        try {
            this.mContext.startActivityAsUser(intent, bundle, UserHandle.of(this.mCurrentUserId));
        } catch (ActivityNotFoundException unused) {
        }
    }

    private void launchAccessibilitySubSettings(int i, ComponentName componentName) {
        Intent intent = new Intent("android.settings.ACCESSIBILITY_DETAILS_SETTINGS");
        Bundle bundle = ActivityOptions.makeBasic().setLaunchDisplayId(i).toBundle();
        intent.addFlags(AudioFormat.AAC_ADIF);
        intent.putExtra("android.intent.extra.COMPONENT_NAME", componentName.flattenToString());
        try {
            this.mContext.startActivityAsUser(intent, bundle, UserHandle.of(this.mCurrentUserId));
        } catch (ActivityNotFoundException unused) {
        }
    }

    private void notifyAccessibilityButtonVisibilityChangedLocked(boolean z) {
        AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
        this.mIsAccessibilityButtonShown = z;
        for (int size = currentUserStateLocked.mBoundServices.size() - 1; size >= 0; size--) {
            AccessibilityServiceConnection accessibilityServiceConnection = currentUserStateLocked.mBoundServices.get(size);
            if (accessibilityServiceConnection.mRequestAccessibilityButton) {
                accessibilityServiceConnection.notifyAccessibilityButtonAvailabilityChangedLocked(accessibilityServiceConnection.isAccessibilityButtonAvailableLocked(currentUserStateLocked));
            }
        }
    }

    private boolean readInstalledAccessibilityServiceLocked(AccessibilityUserState accessibilityUserState) {
        this.mTempAccessibilityServiceInfoList.clear();
        List<ResolveInfo> queryIntentServicesAsUser = this.mPackageManager.queryIntentServicesAsUser(new Intent("android.accessibilityservice.AccessibilityService"), accessibilityUserState.getBindInstantServiceAllowedLocked() ? 9207940 : 819332, this.mCurrentUserId);
        if (this.mServiceExt.checkIfInstalledServicesNotChange(queryIntentServicesAsUser, accessibilityUserState, this.mSecurityPolicy)) {
            Slog.i(LOG_TAG, "installed services not changed, just return");
            return false;
        }
        int size = queryIntentServicesAsUser.size();
        for (int i = 0; i < size; i++) {
            ResolveInfo resolveInfo = queryIntentServicesAsUser.get(i);
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (this.mSecurityPolicy.canRegisterService(serviceInfo)) {
                try {
                    AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo(resolveInfo, this.mContext);
                    if (!accessibilityServiceInfo.isWithinParcelableSize()) {
                        Slog.e(LOG_TAG, "Skipping service " + accessibilityServiceInfo.getResolveInfo().getComponentInfo() + " because service info size is larger than safe parcelable limits.");
                    } else {
                        if (accessibilityUserState.mCrashedServices.contains(serviceInfo.getComponentName())) {
                            accessibilityServiceInfo.crashed = true;
                        }
                        this.mTempAccessibilityServiceInfoList.add(accessibilityServiceInfo);
                    }
                } catch (IOException | XmlPullParserException e) {
                    Slog.e(LOG_TAG, "Error while initializing AccessibilityServiceInfo", e);
                }
            }
        }
        if (!this.mTempAccessibilityServiceInfoList.equals(accessibilityUserState.mInstalledServices)) {
            accessibilityUserState.mInstalledServices.clear();
            accessibilityUserState.mInstalledServices.addAll(this.mTempAccessibilityServiceInfoList);
            this.mTempAccessibilityServiceInfoList.clear();
            return true;
        }
        this.mTempAccessibilityServiceInfoList.clear();
        return false;
    }

    private boolean readInstalledAccessibilityShortcutLocked(AccessibilityUserState accessibilityUserState) {
        List installedAccessibilityShortcutListAsUser = AccessibilityManager.getInstance(this.mContext).getInstalledAccessibilityShortcutListAsUser(this.mContext, this.mCurrentUserId);
        if (installedAccessibilityShortcutListAsUser.equals(accessibilityUserState.mInstalledShortcuts)) {
            return false;
        }
        accessibilityUserState.mInstalledShortcuts.clear();
        accessibilityUserState.mInstalledShortcuts.addAll(installedAccessibilityShortcutListAsUser);
        return true;
    }

    public boolean readEnabledAccessibilityServicesLocked(AccessibilityUserState accessibilityUserState) {
        this.mTempComponentNameSet.clear();
        readComponentNamesFromSettingLocked("enabled_accessibility_services", accessibilityUserState.mUserId, this.mTempComponentNameSet);
        if (!this.mTempComponentNameSet.equals(accessibilityUserState.mEnabledServices)) {
            accessibilityUserState.mEnabledServices.clear();
            accessibilityUserState.mEnabledServices.addAll(this.mTempComponentNameSet);
            this.mTempComponentNameSet.clear();
            return true;
        }
        this.mTempComponentNameSet.clear();
        return false;
    }

    public boolean readTouchExplorationGrantedAccessibilityServicesLocked(AccessibilityUserState accessibilityUserState) {
        this.mTempComponentNameSet.clear();
        readComponentNamesFromSettingLocked("touch_exploration_granted_accessibility_services", accessibilityUserState.mUserId, this.mTempComponentNameSet);
        if (!this.mTempComponentNameSet.equals(accessibilityUserState.mTouchExplorationGrantedServices)) {
            accessibilityUserState.mTouchExplorationGrantedServices.clear();
            accessibilityUserState.mTouchExplorationGrantedServices.addAll(this.mTempComponentNameSet);
            this.mTempComponentNameSet.clear();
            return true;
        }
        this.mTempComponentNameSet.clear();
        return false;
    }

    private void notifyAccessibilityServicesDelayedLocked(AccessibilityEvent accessibilityEvent, boolean z) {
        try {
            AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
            int size = currentUserStateLocked.mBoundServices.size();
            for (int i = 0; i < size; i++) {
                AccessibilityServiceConnection accessibilityServiceConnection = currentUserStateLocked.mBoundServices.get(i);
                if (accessibilityServiceConnection.mIsDefault == z) {
                    accessibilityServiceConnection.notifyAccessibilityEvent(accessibilityEvent);
                }
            }
        } catch (IndexOutOfBoundsException unused) {
        }
    }

    private void updateRelevantEventsLocked(final AccessibilityUserState accessibilityUserState) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(2L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.updateRelevantEventsLocked", 2L, "userState=" + accessibilityUserState);
        }
        this.mMainHandler.post(new Runnable() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                AccessibilityManagerService.this.lambda$updateRelevantEventsLocked$7(accessibilityUserState);
            }
        });
    }

    public /* synthetic */ void lambda$updateRelevantEventsLocked$7(final AccessibilityUserState accessibilityUserState) {
        broadcastToClients(accessibilityUserState, FunctionalUtils.ignoreRemoteException(new FunctionalUtils.RemoteExceptionIgnoringConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda6
            public final void acceptOrThrow(Object obj) {
                AccessibilityManagerService.this.lambda$updateRelevantEventsLocked$6(accessibilityUserState, (AccessibilityManagerService.Client) obj);
            }
        }));
    }

    public /* synthetic */ void lambda$updateRelevantEventsLocked$6(AccessibilityUserState accessibilityUserState, Client client) throws RemoteException {
        synchronized (this.mLock) {
            int computeRelevantEventTypesLocked = computeRelevantEventTypesLocked(accessibilityUserState, client);
            if (!this.mProxyManager.isProxyedDeviceId(client.mDeviceId) && client.mLastSentRelevantEventTypes != computeRelevantEventTypesLocked) {
                client.mLastSentRelevantEventTypes = computeRelevantEventTypesLocked;
                client.mCallback.setRelevantEventTypes(computeRelevantEventTypesLocked);
            }
        }
    }

    public int computeRelevantEventTypesLocked(AccessibilityUserState accessibilityUserState, Client client) {
        int size = accessibilityUserState.mBoundServices.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            AccessibilityServiceConnection accessibilityServiceConnection = accessibilityUserState.mBoundServices.get(i2);
            i |= isClientInPackageAllowlist(accessibilityServiceConnection.getServiceInfo(), client) ? accessibilityServiceConnection.getRelevantEventTypes() : 0;
        }
        return i | (isClientInPackageAllowlist(this.mUiAutomationManager.getServiceInfo(), client) ? this.mUiAutomationManager.getRelevantEventTypes() : 0);
    }

    public void updateMagnificationModeChangeSettingsLocked(AccessibilityUserState accessibilityUserState, int i) {
        if (accessibilityUserState.mUserId == this.mCurrentUserId && !fallBackMagnificationModeSettingsLocked(accessibilityUserState, i)) {
            this.mMagnificationController.transitionMagnificationModeLocked(i, accessibilityUserState.getMagnificationModeLocked(i), new MagnificationController.TransitionCallBack() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda14
                @Override // com.android.server.accessibility.magnification.MagnificationController.TransitionCallBack
                public final void onResult(int i2, boolean z) {
                    AccessibilityManagerService.this.onMagnificationTransitionEndedLocked(i2, z);
                }
            });
        }
    }

    public void onMagnificationTransitionEndedLocked(int i, boolean z) {
        AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
        int magnificationModeLocked = currentUserStateLocked.getMagnificationModeLocked(i) ^ 3;
        if (!z && magnificationModeLocked != 0) {
            currentUserStateLocked.setMagnificationModeLocked(i, magnificationModeLocked);
            if (i == 0) {
                persistMagnificationModeSettingsLocked(magnificationModeLocked);
                return;
            }
            return;
        }
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda41
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((AccessibilityManagerService) obj).notifyRefreshMagnificationModeToInputFilter(((Integer) obj2).intValue());
            }
        }, this, Integer.valueOf(i)));
    }

    public void notifyRefreshMagnificationModeToInputFilter(int i) {
        synchronized (this.mLock) {
            if (this.mHasInputFilter) {
                ArrayList<Display> validDisplayList = getValidDisplayList();
                for (int i2 = 0; i2 < validDisplayList.size(); i2++) {
                    Display display = validDisplayList.get(i2);
                    if (display != null && display.getDisplayId() == i) {
                        this.mInputFilter.refreshMagnificationMode(display);
                        return;
                    }
                }
            }
        }
    }

    public static boolean isClientInPackageAllowlist(AccessibilityServiceInfo accessibilityServiceInfo, Client client) {
        if (accessibilityServiceInfo == null) {
            return false;
        }
        String[] strArr = client.mPackageNames;
        boolean isEmpty = ArrayUtils.isEmpty(accessibilityServiceInfo.packageNames);
        if (isEmpty || strArr == null) {
            return isEmpty;
        }
        for (String str : strArr) {
            if (ArrayUtils.contains(accessibilityServiceInfo.packageNames, str)) {
                return true;
            }
        }
        return isEmpty;
    }

    private void broadcastToClients(AccessibilityUserState accessibilityUserState, Consumer<Client> consumer) {
        this.mGlobalClients.broadcastForEachCookie(consumer);
        accessibilityUserState.mUserClients.broadcastForEachCookie(consumer);
    }

    private void readComponentNamesFromSettingLocked(String str, int i, Set<ComponentName> set) {
        readColonDelimitedSettingToSet(str, i, new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda44
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                ComponentName unflattenFromString;
                unflattenFromString = ComponentName.unflattenFromString((String) obj);
                return unflattenFromString;
            }
        }, set);
    }

    private void readComponentNamesFromStringLocked(String str, Set<ComponentName> set, boolean z) {
        readColonDelimitedStringToSet(str, new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                ComponentName unflattenFromString;
                unflattenFromString = ComponentName.unflattenFromString((String) obj);
                return unflattenFromString;
            }
        }, set, z);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void persistComponentNamesToSettingLocked(String str, Set<ComponentName> set, int i) {
        persistColonDelimitedSetToSettingLocked(str, i, set, new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda25
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String flattenToShortString;
                flattenToShortString = ((ComponentName) obj).flattenToShortString();
                return flattenToShortString;
            }
        });
    }

    private <T> void readColonDelimitedSettingToSet(String str, int i, Function<String, T> function, Set<T> set) {
        readColonDelimitedStringToSet(Settings.Secure.getStringForUser(this.mContext.getContentResolver(), str, i), function, set, false);
    }

    private <T> void readColonDelimitedStringToSet(String str, Function<String, T> function, Set<T> set, boolean z) {
        T apply;
        if (!z) {
            set.clear();
        }
        if (TextUtils.isEmpty(str)) {
            return;
        }
        TextUtils.SimpleStringSplitter simpleStringSplitter = this.mStringColonSplitter;
        simpleStringSplitter.setString(str);
        while (simpleStringSplitter.hasNext()) {
            String next = simpleStringSplitter.next();
            if (!TextUtils.isEmpty(next) && (apply = function.apply(next)) != null) {
                set.add(apply);
            }
        }
    }

    private <T> void persistColonDelimitedSetToSettingLocked(String str, int i, Set<T> set, Function<T, String> function) {
        String str2;
        StringBuilder sb = new StringBuilder();
        Iterator<T> it = set.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            T next = it.next();
            str2 = next != null ? function.apply(next) : null;
            if (!TextUtils.isEmpty(str2)) {
                if (sb.length() > 0) {
                    sb.append(COMPONENT_NAME_SEPARATOR);
                }
                sb.append(str2);
            }
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String sb2 = sb.toString();
            ContentResolver contentResolver = this.mContext.getContentResolver();
            if (!TextUtils.isEmpty(sb2)) {
                str2 = sb2;
            }
            Settings.Secure.putStringForUser(contentResolver, str, str2, i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x00fe, code lost:
    
        if (r15.mBoundServices.contains(r0) != false) goto L88;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateServicesLocked(AccessibilityUserState accessibilityUserState) {
        int i;
        int i2;
        Map<ComponentName, AccessibilityServiceConnection> map;
        AccessibilityUserState accessibilityUserState2;
        AccessibilityManagerService accessibilityManagerService;
        AccessibilityUserState accessibilityUserState3;
        AccessibilityManagerService accessibilityManagerService2 = this;
        AccessibilityUserState accessibilityUserState4 = accessibilityUserState;
        Map<ComponentName, AccessibilityServiceConnection> map2 = accessibilityUserState4.mComponentNameToServiceMap;
        boolean isUserUnlockingOrUnlocked = accessibilityManagerService2.mUmi.isUserUnlockingOrUnlocked(accessibilityUserState4.mUserId);
        accessibilityManagerService2.mTempComponentNameSet.clear();
        int size = accessibilityUserState4.mInstalledServices.size();
        int i3 = 0;
        while (i3 < size) {
            AccessibilityServiceInfo accessibilityServiceInfo = accessibilityUserState4.mInstalledServices.get(i3);
            ComponentName unflattenFromString = ComponentName.unflattenFromString(accessibilityServiceInfo.getId());
            accessibilityManagerService2.mTempComponentNameSet.add(unflattenFromString);
            AccessibilityServiceConnection accessibilityServiceConnection = map2.get(unflattenFromString);
            if (!isUserUnlockingOrUnlocked && !accessibilityServiceInfo.isDirectBootAware()) {
                Slog.d(LOG_TAG, "Ignoring non-encryption-aware service " + unflattenFromString);
            } else if (!accessibilityUserState.getBindingServicesLocked().contains(unflattenFromString) && !accessibilityUserState.getCrashedServicesLocked().contains(unflattenFromString)) {
                if (!accessibilityUserState4.mEnabledServices.contains(unflattenFromString) || accessibilityManagerService2.mUiAutomationManager.suppressingAccessibilityServicesLocked()) {
                    i = i3;
                    i2 = size;
                    map = map2;
                    accessibilityUserState2 = accessibilityUserState4;
                    if (accessibilityServiceConnection != null) {
                        accessibilityServiceConnection.unbindLocked();
                        accessibilityManagerService = this;
                        accessibilityUserState3 = accessibilityUserState2;
                        accessibilityManagerService.removeShortcutTargetForUnboundServiceLocked(accessibilityUserState3, accessibilityServiceConnection);
                        i3 = i + 1;
                        accessibilityManagerService2 = accessibilityManagerService;
                        accessibilityUserState4 = accessibilityUserState3;
                        map2 = map;
                        size = i2;
                    }
                } else if (!accessibilityManagerService2.isAccessibilityTargetAllowed(unflattenFromString.getPackageName(), accessibilityServiceInfo.getResolveInfo().serviceInfo.applicationInfo.uid, accessibilityUserState4.mUserId)) {
                    Slog.d(LOG_TAG, "Skipping enabling service disallowed by device admin policy: " + unflattenFromString);
                    accessibilityManagerService2.disableAccessibilityServiceLocked(unflattenFromString, accessibilityUserState4.mUserId);
                } else {
                    if (accessibilityServiceConnection == null) {
                        Context context = accessibilityManagerService2.mContext;
                        int i4 = sIdCounter;
                        sIdCounter = i4 + 1;
                        Handler handler = accessibilityManagerService2.mMainHandler;
                        Object obj = accessibilityManagerService2.mLock;
                        AccessibilitySecurityPolicy accessibilitySecurityPolicy = accessibilityManagerService2.mSecurityPolicy;
                        AccessibilityTraceManager traceManager = getTraceManager();
                        WindowManagerInternal windowManagerInternal = accessibilityManagerService2.mWindowManagerService;
                        SystemActionPerformer systemActionPerformer = getSystemActionPerformer();
                        AccessibilityWindowManager accessibilityWindowManager = accessibilityManagerService2.mA11yWindowManager;
                        ActivityTaskManagerInternal activityTaskManagerInternal = accessibilityManagerService2.mActivityTaskManagerService;
                        i = i3;
                        i2 = size;
                        map = map2;
                        accessibilityUserState2 = accessibilityUserState4;
                        accessibilityServiceConnection = new AccessibilityServiceConnection(accessibilityUserState, context, unflattenFromString, accessibilityServiceInfo, i4, handler, obj, accessibilitySecurityPolicy, this, traceManager, windowManagerInternal, systemActionPerformer, accessibilityWindowManager, activityTaskManagerInternal);
                    } else {
                        i = i3;
                        i2 = size;
                        map = map2;
                        accessibilityUserState2 = accessibilityUserState4;
                    }
                    accessibilityServiceConnection.bindLocked();
                }
                accessibilityManagerService = this;
                accessibilityUserState3 = accessibilityUserState2;
                i3 = i + 1;
                accessibilityManagerService2 = accessibilityManagerService;
                accessibilityUserState4 = accessibilityUserState3;
                map2 = map;
                size = i2;
            }
            i = i3;
            i2 = size;
            map = map2;
            accessibilityUserState3 = accessibilityUserState4;
            accessibilityManagerService = accessibilityManagerService2;
            i3 = i + 1;
            accessibilityManagerService2 = accessibilityManagerService;
            accessibilityUserState4 = accessibilityUserState3;
            map2 = map;
            size = i2;
        }
        AccessibilityUserState accessibilityUserState5 = accessibilityUserState4;
        AccessibilityManagerService accessibilityManagerService3 = accessibilityManagerService2;
        int size2 = accessibilityUserState5.mBoundServices.size();
        accessibilityManagerService3.mTempIntArray.clear();
        for (int i5 = 0; i5 < size2; i5++) {
            ResolveInfo resolveInfo = accessibilityUserState5.mBoundServices.get(i5).mAccessibilityServiceInfo.getResolveInfo();
            if (resolveInfo != null) {
                accessibilityManagerService3.mTempIntArray.add(resolveInfo.serviceInfo.applicationInfo.uid);
            }
        }
        AudioManagerInternal audioManagerInternal = (AudioManagerInternal) LocalServices.getService(AudioManagerInternal.class);
        if (audioManagerInternal != null) {
            audioManagerInternal.setAccessibilityServiceUids(accessibilityManagerService3.mTempIntArray);
        }
        accessibilityManagerService3.mActivityTaskManagerService.setAccessibilityServiceUids(accessibilityManagerService3.mTempIntArray);
        if (accessibilityUserState5.mEnabledServices.removeIf(new Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda42
            @Override // java.util.function.Predicate
            public final boolean test(Object obj2) {
                boolean lambda$updateServicesLocked$11;
                lambda$updateServicesLocked$11 = AccessibilityManagerService.this.lambda$updateServicesLocked$11((ComponentName) obj2);
                return lambda$updateServicesLocked$11;
            }
        }) || accessibilityUserState5.mTouchExplorationGrantedServices.removeIf(new Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda43
            @Override // java.util.function.Predicate
            public final boolean test(Object obj2) {
                boolean lambda$updateServicesLocked$12;
                lambda$updateServicesLocked$12 = AccessibilityManagerService.this.lambda$updateServicesLocked$12((ComponentName) obj2);
                return lambda$updateServicesLocked$12;
            }
        })) {
            accessibilityManagerService3.persistComponentNamesToSettingLocked("enabled_accessibility_services", accessibilityUserState5.mEnabledServices, accessibilityUserState5.mUserId);
            accessibilityManagerService3.persistComponentNamesToSettingLocked("touch_exploration_granted_accessibility_services", accessibilityUserState5.mTouchExplorationGrantedServices, accessibilityUserState5.mUserId);
        }
        accessibilityManagerService3.mTempComponentNameSet.clear();
        updateAccessibilityEnabledSettingLocked(accessibilityUserState);
    }

    public /* synthetic */ boolean lambda$updateServicesLocked$11(ComponentName componentName) {
        return !this.mTempComponentNameSet.contains(componentName);
    }

    public /* synthetic */ boolean lambda$updateServicesLocked$12(ComponentName componentName) {
        return !this.mTempComponentNameSet.contains(componentName);
    }

    public void scheduleUpdateClientsIfNeededLocked(AccessibilityUserState accessibilityUserState) {
        scheduleUpdateClientsIfNeededLocked(accessibilityUserState, false);
    }

    void scheduleUpdateClientsIfNeededLocked(AccessibilityUserState accessibilityUserState, boolean z) {
        int clientStateLocked = getClientStateLocked(accessibilityUserState);
        if (accessibilityUserState.getLastSentClientStateLocked() != clientStateLocked || z) {
            if (this.mGlobalClients.getRegisteredCallbackCount() > 0 || accessibilityUserState.mUserClients.getRegisteredCallbackCount() > 0) {
                accessibilityUserState.setLastSentClientStateLocked(clientStateLocked);
                this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda56
                    public final void accept(Object obj, Object obj2, Object obj3) {
                        ((AccessibilityManagerService) obj).sendStateToAllClients(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
                    }
                }, this, Integer.valueOf(clientStateLocked), Integer.valueOf(accessibilityUserState.mUserId)));
            }
        }
    }

    public void sendStateToAllClients(int i, int i2) {
        sendStateToClients(i, this.mGlobalClients);
        sendStateToClients(i, i2);
    }

    public void sendStateToClients(int i, int i2) {
        sendStateToClients(i, getUserState(i2).mUserClients);
    }

    private void sendStateToClients(final int i, RemoteCallbackList<IAccessibilityManagerClient> remoteCallbackList) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(8L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.sendStateToClients", 8L, "clientState=" + i);
        }
        remoteCallbackList.broadcastForEachCookie(FunctionalUtils.ignoreRemoteException(new FunctionalUtils.RemoteExceptionIgnoringConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda35
            public final void acceptOrThrow(Object obj) {
                AccessibilityManagerService.this.lambda$sendStateToClients$13(i, obj);
            }
        }));
    }

    public /* synthetic */ void lambda$sendStateToClients$13(int i, Object obj) throws RemoteException {
        Client client = (Client) obj;
        if (this.mProxyManager.isProxyedDeviceId(client.mDeviceId)) {
            return;
        }
        client.mCallback.setState(i);
    }

    private void scheduleNotifyClientsOfServicesStateChangeLocked(AccessibilityUserState accessibilityUserState) {
        updateRecommendedUiTimeoutLocked(accessibilityUserState);
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda30
            public final void accept(Object obj, Object obj2, Object obj3) {
                ((AccessibilityManagerService) obj).sendServicesStateChanged((RemoteCallbackList) obj2, ((Long) obj3).longValue());
            }
        }, this, accessibilityUserState.mUserClients, Long.valueOf(getRecommendedTimeoutMillisLocked(accessibilityUserState))));
    }

    public void sendServicesStateChanged(RemoteCallbackList<IAccessibilityManagerClient> remoteCallbackList, long j) {
        notifyClientsOfServicesStateChange(this.mGlobalClients, j);
        notifyClientsOfServicesStateChange(remoteCallbackList, j);
    }

    private void notifyClientsOfServicesStateChange(RemoteCallbackList<IAccessibilityManagerClient> remoteCallbackList, final long j) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(8L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.notifyClientsOfServicesStateChange", 8L, "uiTimeout=" + j);
        }
        remoteCallbackList.broadcastForEachCookie(FunctionalUtils.ignoreRemoteException(new FunctionalUtils.RemoteExceptionIgnoringConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda26
            public final void acceptOrThrow(Object obj) {
                AccessibilityManagerService.this.lambda$notifyClientsOfServicesStateChange$14(j, obj);
            }
        }));
    }

    public /* synthetic */ void lambda$notifyClientsOfServicesStateChange$14(long j, Object obj) throws RemoteException {
        Client client = (Client) obj;
        if (this.mProxyManager.isProxyedDeviceId(client.mDeviceId)) {
            return;
        }
        client.mCallback.notifyServicesStateChanged(j);
    }

    private void scheduleUpdateInputFilter(AccessibilityUserState accessibilityUserState) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda36
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((AccessibilityManagerService) obj).updateInputFilter((AccessibilityUserState) obj2);
            }
        }, this, accessibilityUserState));
    }

    private void scheduleUpdateFingerprintGestureHandling(AccessibilityUserState accessibilityUserState) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda19
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((AccessibilityManagerService) obj).updateFingerprintGestureHandling((AccessibilityUserState) obj2);
            }
        }, this, accessibilityUserState));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void updateInputFilter(AccessibilityUserState accessibilityUserState) {
        boolean z;
        AccessibilityInputFilter accessibilityInputFilter;
        if (this.mUiAutomationManager.suppressingAccessibilityServicesLocked()) {
            return;
        }
        synchronized (this.mLock) {
            boolean isDisplayMagnificationEnabledLocked = accessibilityUserState.isDisplayMagnificationEnabledLocked();
            boolean z2 = isDisplayMagnificationEnabledLocked;
            if (accessibilityUserState.isShortcutMagnificationEnabledLocked()) {
                z2 = (isDisplayMagnificationEnabledLocked ? 1 : 0) | '@';
            }
            boolean z3 = z2;
            if (userHasMagnificationServicesLocked(accessibilityUserState)) {
                z3 = (z2 ? 1 : 0) | ' ';
            }
            boolean z4 = z3;
            if (accessibilityUserState.isHandlingAccessibilityEventsLocked()) {
                z4 = z3;
                if (accessibilityUserState.isTouchExplorationEnabledLocked()) {
                    boolean z5 = (z3 ? 1 : 0) | 2;
                    boolean z6 = z5;
                    if (accessibilityUserState.isServiceHandlesDoubleTapEnabledLocked()) {
                        z6 = (z5 ? 1 : 0) | 128;
                    }
                    boolean z7 = z6;
                    if (accessibilityUserState.isMultiFingerGesturesEnabledLocked()) {
                        z7 = (z6 ? 1 : 0) | 256;
                    }
                    z4 = z7;
                    if (accessibilityUserState.isTwoFingerPassthroughEnabledLocked()) {
                        z4 = (z7 ? 1 : 0) | 512;
                    }
                }
            }
            boolean z8 = z4;
            if (accessibilityUserState.isFilterKeyEventsEnabledLocked()) {
                z8 = (z4 ? 1 : 0) | 4;
            }
            boolean z9 = z8;
            if (accessibilityUserState.isSendMotionEventsEnabled()) {
                z9 = (z8 ? 1 : 0) | 1024;
            }
            boolean z10 = z9;
            if (accessibilityUserState.isAutoclickEnabledLocked()) {
                z10 = (z9 ? 1 : 0) | '\b';
            }
            int i = z10;
            if (accessibilityUserState.isPerformGesturesEnabledLocked()) {
                i = (z10 ? 1 : 0) | 16;
            }
            Iterator<AccessibilityServiceConnection> it = accessibilityUserState.mBoundServices.iterator();
            z = false;
            int i2 = 0;
            while (it.hasNext()) {
                i2 |= it.next().mGenericMotionEventSources;
            }
            if (i2 != 0) {
                i = (i == true ? 1 : 0) | 2048;
            }
            accessibilityInputFilter = null;
            if (i != 0) {
                if (!this.mHasInputFilter) {
                    this.mHasInputFilter = true;
                    if (this.mInputFilter == null) {
                        this.mInputFilter = new AccessibilityInputFilter(this.mContext, this);
                    }
                    accessibilityInputFilter = this.mInputFilter;
                    z = true;
                }
                this.mInputFilter.setUserAndEnabledFeatures(accessibilityUserState.mUserId, i);
                this.mInputFilter.setCombinedGenericMotionEventSources(i2);
            } else if (this.mHasInputFilter) {
                this.mHasInputFilter = false;
                this.mInputFilter.setUserAndEnabledFeatures(accessibilityUserState.mUserId, 0);
                this.mInputFilter.resetServiceDetectsGestures();
                if (accessibilityUserState.isTouchExplorationEnabledLocked()) {
                    Iterator<Display> it2 = getValidDisplayList().iterator();
                    while (it2.hasNext()) {
                        int displayId = it2.next().getDisplayId();
                        this.mInputFilter.setServiceDetectsGesturesEnabled(displayId, accessibilityUserState.isServiceDetectsGesturesEnabled(displayId));
                    }
                }
                z = true;
            }
        }
        if (z) {
            if (this.mTraceManager.isA11yTracingEnabledForTypes(4608L)) {
                this.mTraceManager.logTrace("WindowManagerInternal.setInputFilter", 4608L, "inputFilter=" + accessibilityInputFilter);
            }
            this.mWindowManagerService.setInputFilter(accessibilityInputFilter);
            this.mProxyManager.setAccessibilityInputFilter(accessibilityInputFilter);
        }
    }

    public void showEnableTouchExplorationDialog(AccessibilityServiceConnection accessibilityServiceConnection) {
        synchronized (this.mLock) {
            String charSequence = accessibilityServiceConnection.getServiceInfo().getResolveInfo().loadLabel(this.mContext.getPackageManager()).toString();
            AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
            if (currentUserStateLocked.isTouchExplorationEnabledLocked()) {
                return;
            }
            AlertDialog alertDialog = this.mEnableTouchExplorationDialog;
            if (alertDialog == null || !alertDialog.isShowing()) {
                AlertDialog create = new AlertDialog.Builder(this.mContext).setIconAttribute(R.attr.alertDialogIcon).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.server.accessibility.AccessibilityManagerService.7
                    final /* synthetic */ AccessibilityServiceConnection val$service;
                    final /* synthetic */ AccessibilityUserState val$userState;

                    AnonymousClass7(AccessibilityUserState currentUserStateLocked2, AccessibilityServiceConnection accessibilityServiceConnection2) {
                        r2 = currentUserStateLocked2;
                        r3 = accessibilityServiceConnection2;
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        r2.mTouchExplorationGrantedServices.add(r3.mComponentName);
                        AccessibilityManagerService accessibilityManagerService = AccessibilityManagerService.this;
                        AccessibilityUserState accessibilityUserState = r2;
                        accessibilityManagerService.persistComponentNamesToSettingLocked("touch_exploration_granted_accessibility_services", accessibilityUserState.mTouchExplorationGrantedServices, accessibilityUserState.mUserId);
                        r2.setTouchExplorationEnabledLocked(true);
                        long clearCallingIdentity = Binder.clearCallingIdentity();
                        try {
                            Settings.Secure.putIntForUser(AccessibilityManagerService.this.mContext.getContentResolver(), "touch_exploration_enabled", 1, r2.mUserId);
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                            AccessibilityManagerService.this.onUserStateChangedLocked(r2);
                        } catch (Throwable th) {
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                            throw th;
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.server.accessibility.AccessibilityManagerService.6
                    AnonymousClass6() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setTitle(R.string.global_action_logout).setMessage(this.mContext.getString(R.string.global_action_lockdown, charSequence)).create();
                this.mEnableTouchExplorationDialog = create;
                create.getWindow().setType(2003);
                this.mEnableTouchExplorationDialog.getWindow().getAttributes().privateFlags |= 16;
                this.mEnableTouchExplorationDialog.setCanceledOnTouchOutside(true);
                this.mEnableTouchExplorationDialog.show();
            }
        }
    }

    /* renamed from: com.android.server.accessibility.AccessibilityManagerService$7 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass7 implements DialogInterface.OnClickListener {
        final /* synthetic */ AccessibilityServiceConnection val$service;
        final /* synthetic */ AccessibilityUserState val$userState;

        AnonymousClass7(AccessibilityUserState currentUserStateLocked2, AccessibilityServiceConnection accessibilityServiceConnection2) {
            r2 = currentUserStateLocked2;
            r3 = accessibilityServiceConnection2;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            r2.mTouchExplorationGrantedServices.add(r3.mComponentName);
            AccessibilityManagerService accessibilityManagerService = AccessibilityManagerService.this;
            AccessibilityUserState accessibilityUserState = r2;
            accessibilityManagerService.persistComponentNamesToSettingLocked("touch_exploration_granted_accessibility_services", accessibilityUserState.mTouchExplorationGrantedServices, accessibilityUserState.mUserId);
            r2.setTouchExplorationEnabledLocked(true);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                Settings.Secure.putIntForUser(AccessibilityManagerService.this.mContext.getContentResolver(), "touch_exploration_enabled", 1, r2.mUserId);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                AccessibilityManagerService.this.onUserStateChangedLocked(r2);
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
        }
    }

    /* renamed from: com.android.server.accessibility.AccessibilityManagerService$6 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass6 implements DialogInterface.OnClickListener {
        AnonymousClass6() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    /* renamed from: onUserVisibilityChanged */
    public void lambda$new$0(int i, boolean z) {
        synchronized (this.mLock) {
            if (z) {
                this.mVisibleBgUserIds.put(i, z);
            } else {
                this.mVisibleBgUserIds.delete(i);
            }
        }
    }

    public void onUserStateChangedLocked(AccessibilityUserState accessibilityUserState) {
        onUserStateChangedLocked(accessibilityUserState, false);
    }

    private void onUserStateChangedLocked(AccessibilityUserState accessibilityUserState, boolean z) {
        this.mInitialized = true;
        updateLegacyCapabilitiesLocked(accessibilityUserState);
        updateServicesLocked(accessibilityUserState);
        updateWindowsForAccessibilityCallbackLocked(accessibilityUserState);
        updateFilterKeyEventsLocked(accessibilityUserState);
        updateTouchExplorationLocked(accessibilityUserState);
        updatePerformGesturesLocked(accessibilityUserState);
        updateMagnificationLocked(accessibilityUserState);
        scheduleUpdateFingerprintGestureHandling(accessibilityUserState);
        scheduleUpdateInputFilter(accessibilityUserState);
        updateRelevantEventsLocked(accessibilityUserState);
        scheduleUpdateClientsIfNeededLocked(accessibilityUserState, z);
        updateAccessibilityShortcutKeyTargetsLocked(accessibilityUserState);
        updateAccessibilityButtonTargetsLocked(accessibilityUserState);
        updateMagnificationCapabilitiesSettingsChangeLocked(accessibilityUserState);
        updateMagnificationModeChangeSettingsForAllDisplaysLocked(accessibilityUserState);
        updateFocusAppearanceDataLocked(accessibilityUserState);
    }

    private void updateMagnificationModeChangeSettingsForAllDisplaysLocked(AccessibilityUserState accessibilityUserState) {
        ArrayList<Display> validDisplayList = getValidDisplayList();
        for (int i = 0; i < validDisplayList.size(); i++) {
            updateMagnificationModeChangeSettingsLocked(accessibilityUserState, validDisplayList.get(i).getDisplayId());
        }
    }

    public void updateWindowsForAccessibilityCallbackLocked(AccessibilityUserState accessibilityUserState) {
        boolean z = this.mUiAutomationManager.canRetrieveInteractiveWindowsLocked() || this.mProxyManager.canRetrieveInteractiveWindowsLocked();
        ArrayList<AccessibilityServiceConnection> arrayList = accessibilityUserState.mBoundServices;
        int size = arrayList.size();
        for (int i = 0; !z && i < size; i++) {
            if (arrayList.get(i).canRetrieveInteractiveWindowsLocked()) {
                accessibilityUserState.setAccessibilityFocusOnlyInActiveWindow(false);
                z = true;
            }
        }
        accessibilityUserState.setAccessibilityFocusOnlyInActiveWindow(true);
        ArrayList<Display> validDisplayList = getValidDisplayList();
        for (int i2 = 0; i2 < validDisplayList.size(); i2++) {
            Display display = validDisplayList.get(i2);
            if (display != null) {
                if (z) {
                    this.mA11yWindowManager.startTrackingWindows(display.getDisplayId(), this.mProxyManager.isProxyedDisplay(display.getDisplayId()));
                } else {
                    this.mA11yWindowManager.stopTrackingWindows(display.getDisplayId());
                }
            }
        }
    }

    private void updateLegacyCapabilitiesLocked(AccessibilityUserState accessibilityUserState) {
        int size = accessibilityUserState.mInstalledServices.size();
        for (int i = 0; i < size; i++) {
            AccessibilityServiceInfo accessibilityServiceInfo = accessibilityUserState.mInstalledServices.get(i);
            ResolveInfo resolveInfo = accessibilityServiceInfo.getResolveInfo();
            if ((accessibilityServiceInfo.getCapabilities() & 2) == 0 && resolveInfo.serviceInfo.applicationInfo.targetSdkVersion <= 17) {
                ServiceInfo serviceInfo = resolveInfo.serviceInfo;
                if (accessibilityUserState.mTouchExplorationGrantedServices.contains(new ComponentName(serviceInfo.packageName, serviceInfo.name))) {
                    accessibilityServiceInfo.setCapabilities(accessibilityServiceInfo.getCapabilities() | 2);
                }
            }
        }
    }

    private void updatePerformGesturesLocked(AccessibilityUserState accessibilityUserState) {
        int size = accessibilityUserState.mBoundServices.size();
        for (int i = 0; i < size; i++) {
            if ((accessibilityUserState.mBoundServices.get(i).getCapabilities() & 32) != 0) {
                accessibilityUserState.setPerformGesturesEnabledLocked(true);
                return;
            }
        }
        accessibilityUserState.setPerformGesturesEnabledLocked(false);
    }

    private void updateFilterKeyEventsLocked(AccessibilityUserState accessibilityUserState) {
        int size = accessibilityUserState.mBoundServices.size();
        for (int i = 0; i < size; i++) {
            AccessibilityServiceConnection accessibilityServiceConnection = accessibilityUserState.mBoundServices.get(i);
            if (accessibilityServiceConnection.mRequestFilterKeyEvents && (accessibilityServiceConnection.getCapabilities() & 8) != 0) {
                accessibilityUserState.setFilterKeyEventsEnabledLocked(true);
                return;
            }
        }
        accessibilityUserState.setFilterKeyEventsEnabledLocked(false);
    }

    public boolean readConfigurationForUserStateLocked(AccessibilityUserState accessibilityUserState) {
        return readAlwaysOnMagnificationLocked(accessibilityUserState) | readInstalledAccessibilityServiceLocked(accessibilityUserState) | readInstalledAccessibilityShortcutLocked(accessibilityUserState) | readEnabledAccessibilityServicesLocked(accessibilityUserState) | readTouchExplorationGrantedAccessibilityServicesLocked(accessibilityUserState) | readTouchExplorationEnabledSettingLocked(accessibilityUserState) | readHighTextContrastEnabledSettingLocked(accessibilityUserState) | readAudioDescriptionEnabledSettingLocked(accessibilityUserState) | readMagnificationEnabledSettingsLocked(accessibilityUserState) | readAutoclickEnabledSettingLocked(accessibilityUserState) | readAccessibilityShortcutKeySettingLocked(accessibilityUserState) | readAccessibilityButtonTargetsLocked(accessibilityUserState) | readAccessibilityButtonTargetComponentLocked(accessibilityUserState) | readUserRecommendedUiTimeoutSettingsLocked(accessibilityUserState) | readMagnificationModeForDefaultDisplayLocked(accessibilityUserState) | readMagnificationCapabilitiesLocked(accessibilityUserState) | readMagnificationFollowTypingLocked(accessibilityUserState);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v3, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r1v5 */
    private void updateAccessibilityEnabledSettingLocked(AccessibilityUserState accessibilityUserState) {
        ?? r1 = (this.mUiAutomationManager.isUiAutomationRunningLocked() || accessibilityUserState.isHandlingAccessibilityEventsLocked()) ? 1 : 0;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_enabled", 0, accessibilityUserState.mUserId) != r1) {
                Slog.i(LOG_TAG, "updateAccessibilityEnabledSettingLocked:" + ((boolean) r1));
                Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "accessibility_enabled", r1, accessibilityUserState.mUserId);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean readTouchExplorationEnabledSettingLocked(AccessibilityUserState accessibilityUserState) {
        boolean z = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "touch_exploration_enabled", 0, accessibilityUserState.mUserId) == 1;
        if (z == accessibilityUserState.isTouchExplorationEnabledLocked()) {
            return false;
        }
        accessibilityUserState.setTouchExplorationEnabledLocked(z);
        return true;
    }

    public boolean readMagnificationEnabledSettingsLocked(AccessibilityUserState accessibilityUserState) {
        boolean z = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_display_magnification_enabled", 0, accessibilityUserState.mUserId) == 1;
        if (z == accessibilityUserState.isDisplayMagnificationEnabledLocked()) {
            return false;
        }
        accessibilityUserState.setDisplayMagnificationEnabledLocked(z);
        return true;
    }

    public boolean readAutoclickEnabledSettingLocked(AccessibilityUserState accessibilityUserState) {
        boolean z = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_autoclick_enabled", 0, accessibilityUserState.mUserId) == 1;
        if (z == accessibilityUserState.isAutoclickEnabledLocked()) {
            return false;
        }
        accessibilityUserState.setAutoclickEnabledLocked(z);
        return true;
    }

    public boolean readHighTextContrastEnabledSettingLocked(AccessibilityUserState accessibilityUserState) {
        boolean z = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "high_text_contrast_enabled", 0, accessibilityUserState.mUserId) == 1;
        if (z == accessibilityUserState.isTextHighContrastEnabledLocked()) {
            return false;
        }
        accessibilityUserState.setTextHighContrastEnabledLocked(z);
        return true;
    }

    public boolean readAudioDescriptionEnabledSettingLocked(AccessibilityUserState accessibilityUserState) {
        boolean z = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "enabled_accessibility_audio_description_by_default", 0, accessibilityUserState.mUserId) == 1;
        if (z == accessibilityUserState.isAudioDescriptionByDefaultEnabledLocked()) {
            return false;
        }
        accessibilityUserState.setAudioDescriptionByDefaultEnabledLocked(z);
        return true;
    }

    private void updateTouchExplorationLocked(AccessibilityUserState accessibilityUserState) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean isTouchExplorationEnabledLocked = this.mUiAutomationManager.isTouchExplorationEnabledLocked();
        int size = accessibilityUserState.mBoundServices.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                z = false;
                z2 = false;
                z3 = false;
                z4 = false;
                break;
            }
            AccessibilityServiceConnection accessibilityServiceConnection = accessibilityUserState.mBoundServices.get(i);
            if (canRequestAndRequestsTouchExplorationLocked(accessibilityServiceConnection, accessibilityUserState)) {
                boolean isServiceHandlesDoubleTapEnabled = accessibilityServiceConnection.isServiceHandlesDoubleTapEnabled();
                boolean isMultiFingerGesturesEnabled = accessibilityServiceConnection.isMultiFingerGesturesEnabled();
                boolean isTwoFingerPassthroughEnabled = accessibilityServiceConnection.isTwoFingerPassthroughEnabled();
                z4 = accessibilityServiceConnection.isSendMotionEventsEnabled();
                z3 = isTwoFingerPassthroughEnabled;
                z2 = isMultiFingerGesturesEnabled;
                z = isServiceHandlesDoubleTapEnabled;
                isTouchExplorationEnabledLocked = true;
                break;
            }
            i++;
        }
        if (isTouchExplorationEnabledLocked != accessibilityUserState.isTouchExplorationEnabledLocked()) {
            accessibilityUserState.setTouchExplorationEnabledLocked(isTouchExplorationEnabledLocked);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "touch_exploration_enabled", isTouchExplorationEnabledLocked ? 1 : 0, accessibilityUserState.mUserId);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        accessibilityUserState.resetServiceDetectsGestures();
        ArrayList<Display> validDisplayList = getValidDisplayList();
        Iterator<AccessibilityServiceConnection> it = accessibilityUserState.mBoundServices.iterator();
        while (it.hasNext()) {
            AccessibilityServiceConnection next = it.next();
            Iterator<Display> it2 = validDisplayList.iterator();
            while (it2.hasNext()) {
                int displayId = it2.next().getDisplayId();
                if (next.isServiceDetectsGesturesEnabled(displayId)) {
                    accessibilityUserState.setServiceDetectsGesturesEnabled(displayId, true);
                }
            }
        }
        accessibilityUserState.setServiceHandlesDoubleTapLocked(z);
        accessibilityUserState.setMultiFingerGesturesLocked(z2);
        accessibilityUserState.setTwoFingerPassthroughLocked(z3);
        accessibilityUserState.setSendMotionEventsEnabled(z4);
    }

    public boolean readAccessibilityShortcutKeySettingLocked(AccessibilityUserState accessibilityUserState) {
        String stringForUser = Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "accessibility_shortcut_target_service", accessibilityUserState.mUserId);
        ArraySet arraySet = new ArraySet();
        readColonDelimitedStringToSet(stringForUser, new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda40
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String lambda$readAccessibilityShortcutKeySettingLocked$15;
                lambda$readAccessibilityShortcutKeySettingLocked$15 = AccessibilityManagerService.lambda$readAccessibilityShortcutKeySettingLocked$15((String) obj);
                return lambda$readAccessibilityShortcutKeySettingLocked$15;
            }
        }, arraySet, false);
        if (stringForUser == null) {
            String string = this.mContext.getString(R.string.config_inputEventCompatProcessorOverrideClassName);
            if (!TextUtils.isEmpty(string)) {
                arraySet.add(string);
            }
        }
        Set shortcutTargetsLocked = accessibilityUserState.getShortcutTargetsLocked(1);
        if (arraySet.equals(shortcutTargetsLocked)) {
            return false;
        }
        shortcutTargetsLocked.clear();
        shortcutTargetsLocked.addAll(arraySet);
        scheduleNotifyClientsOfServicesStateChangeLocked(accessibilityUserState);
        return true;
    }

    public boolean readAccessibilityButtonTargetsLocked(AccessibilityUserState accessibilityUserState) {
        ArraySet arraySet = new ArraySet();
        readColonDelimitedSettingToSet("accessibility_button_targets", accessibilityUserState.mUserId, new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda24
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String lambda$readAccessibilityButtonTargetsLocked$16;
                lambda$readAccessibilityButtonTargetsLocked$16 = AccessibilityManagerService.lambda$readAccessibilityButtonTargetsLocked$16((String) obj);
                return lambda$readAccessibilityButtonTargetsLocked$16;
            }
        }, arraySet);
        Set shortcutTargetsLocked = accessibilityUserState.getShortcutTargetsLocked(0);
        if (arraySet.equals(shortcutTargetsLocked)) {
            return false;
        }
        shortcutTargetsLocked.clear();
        shortcutTargetsLocked.addAll(arraySet);
        scheduleNotifyClientsOfServicesStateChangeLocked(accessibilityUserState);
        return true;
    }

    public boolean readAccessibilityButtonTargetComponentLocked(AccessibilityUserState accessibilityUserState) {
        String stringForUser = Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "accessibility_button_target_component", accessibilityUserState.mUserId);
        if (TextUtils.isEmpty(stringForUser)) {
            if (accessibilityUserState.getTargetAssignedToAccessibilityButton() == null) {
                return false;
            }
            accessibilityUserState.setTargetAssignedToAccessibilityButton(null);
            return true;
        }
        if (stringForUser.equals(accessibilityUserState.getTargetAssignedToAccessibilityButton())) {
            return false;
        }
        accessibilityUserState.setTargetAssignedToAccessibilityButton(stringForUser);
        return true;
    }

    public boolean readUserRecommendedUiTimeoutSettingsLocked(AccessibilityUserState accessibilityUserState) {
        int intForUser = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_non_interactive_ui_timeout_ms", 0, accessibilityUserState.mUserId);
        int intForUser2 = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_interactive_ui_timeout_ms", 0, accessibilityUserState.mUserId);
        this.mProxyManager.updateTimeoutsIfNeeded(intForUser, intForUser2);
        if (intForUser == accessibilityUserState.getUserNonInteractiveUiTimeoutLocked() && intForUser2 == accessibilityUserState.getUserInteractiveUiTimeoutLocked()) {
            return false;
        }
        accessibilityUserState.setUserNonInteractiveUiTimeoutLocked(intForUser);
        accessibilityUserState.setUserInteractiveUiTimeoutLocked(intForUser2);
        scheduleNotifyClientsOfServicesStateChangeLocked(accessibilityUserState);
        return true;
    }

    private void updateAccessibilityShortcutKeyTargetsLocked(final AccessibilityUserState accessibilityUserState) {
        ArraySet<String> shortcutTargetsLocked = accessibilityUserState.getShortcutTargetsLocked(1);
        int size = shortcutTargetsLocked.size();
        if (size == 0) {
            return;
        }
        shortcutTargetsLocked.removeIf(new Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda54
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$updateAccessibilityShortcutKeyTargetsLocked$17;
                lambda$updateAccessibilityShortcutKeyTargetsLocked$17 = AccessibilityManagerService.lambda$updateAccessibilityShortcutKeyTargetsLocked$17(AccessibilityUserState.this, (String) obj);
                return lambda$updateAccessibilityShortcutKeyTargetsLocked$17;
            }
        });
        if (size == shortcutTargetsLocked.size()) {
            return;
        }
        persistColonDelimitedSetToSettingLocked("accessibility_shortcut_target_service", accessibilityUserState.mUserId, shortcutTargetsLocked, new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda55
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String lambda$updateAccessibilityShortcutKeyTargetsLocked$18;
                lambda$updateAccessibilityShortcutKeyTargetsLocked$18 = AccessibilityManagerService.lambda$updateAccessibilityShortcutKeyTargetsLocked$18((String) obj);
                return lambda$updateAccessibilityShortcutKeyTargetsLocked$18;
            }
        });
        scheduleNotifyClientsOfServicesStateChangeLocked(accessibilityUserState);
    }

    public static /* synthetic */ boolean lambda$updateAccessibilityShortcutKeyTargetsLocked$17(AccessibilityUserState accessibilityUserState, String str) {
        return !accessibilityUserState.isShortcutTargetInstalledLocked(str);
    }

    private boolean canRequestAndRequestsTouchExplorationLocked(AccessibilityServiceConnection accessibilityServiceConnection, AccessibilityUserState accessibilityUserState) {
        if (accessibilityServiceConnection.canReceiveEventsLocked() && accessibilityServiceConnection.mRequestTouchExplorationMode) {
            if (accessibilityServiceConnection.getServiceInfo().getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion <= 17) {
                if (accessibilityUserState.mTouchExplorationGrantedServices.contains(accessibilityServiceConnection.mComponentName)) {
                    return true;
                }
                AlertDialog alertDialog = this.mEnableTouchExplorationDialog;
                if (alertDialog == null || !alertDialog.isShowing()) {
                    this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda46
                        @Override // java.util.function.BiConsumer
                        public final void accept(Object obj, Object obj2) {
                            ((AccessibilityManagerService) obj).showEnableTouchExplorationDialog((AccessibilityServiceConnection) obj2);
                        }
                    }, this, accessibilityServiceConnection));
                }
            } else if ((accessibilityServiceConnection.getCapabilities() & 2) != 0) {
                return true;
            }
        }
        return false;
    }

    public void updateMagnificationLocked(AccessibilityUserState accessibilityUserState) {
        if (accessibilityUserState.mUserId != this.mCurrentUserId) {
            return;
        }
        if (this.mUiAutomationManager.suppressingAccessibilityServicesLocked() && this.mMagnificationController.isFullScreenMagnificationControllerInitialized()) {
            getMagnificationController().getFullScreenMagnificationController().unregisterAll();
            return;
        }
        ArrayList<Display> validDisplayList = getValidDisplayList();
        int i = 0;
        if (accessibilityUserState.isDisplayMagnificationEnabledLocked() || accessibilityUserState.isShortcutMagnificationEnabledLocked()) {
            while (i < validDisplayList.size()) {
                getMagnificationController().getFullScreenMagnificationController().register(validDisplayList.get(i).getDisplayId());
                i++;
            }
            return;
        }
        while (i < validDisplayList.size()) {
            int displayId = validDisplayList.get(i).getDisplayId();
            if (userHasListeningMagnificationServicesLocked(accessibilityUserState, displayId)) {
                getMagnificationController().getFullScreenMagnificationController().register(displayId);
            } else if (this.mMagnificationController.isFullScreenMagnificationControllerInitialized()) {
                getMagnificationController().getFullScreenMagnificationController().unregister(displayId);
            }
            i++;
        }
    }

    private void updateWindowMagnificationConnectionIfNeeded(AccessibilityUserState accessibilityUserState) {
        if (this.mMagnificationController.supportWindowMagnification()) {
            boolean z = true;
            if (((!accessibilityUserState.isShortcutMagnificationEnabledLocked() && !accessibilityUserState.isDisplayMagnificationEnabledLocked()) || accessibilityUserState.getMagnificationCapabilitiesLocked() == 1) && !userHasMagnificationServicesLocked(accessibilityUserState)) {
                z = false;
            }
            getWindowMagnificationMgr().requestConnection(z);
        }
    }

    private boolean userHasMagnificationServicesLocked(AccessibilityUserState accessibilityUserState) {
        ArrayList<AccessibilityServiceConnection> arrayList = accessibilityUserState.mBoundServices;
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (this.mSecurityPolicy.canControlMagnification(arrayList.get(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean userHasListeningMagnificationServicesLocked(AccessibilityUserState accessibilityUserState, int i) {
        ArrayList<AccessibilityServiceConnection> arrayList = accessibilityUserState.mBoundServices;
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            AccessibilityServiceConnection accessibilityServiceConnection = arrayList.get(i2);
            if (this.mSecurityPolicy.canControlMagnification(accessibilityServiceConnection) && accessibilityServiceConnection.isMagnificationCallbackEnabled(i)) {
                return true;
            }
        }
        return false;
    }

    public void updateFingerprintGestureHandling(AccessibilityUserState accessibilityUserState) {
        ArrayList<AccessibilityServiceConnection> arrayList;
        synchronized (this.mLock) {
            arrayList = accessibilityUserState.mBoundServices;
            if (this.mFingerprintGestureDispatcher == null && this.mPackageManager.hasSystemFeature("android.hardware.fingerprint")) {
                int size = arrayList.size();
                int i = 0;
                while (true) {
                    if (i >= size) {
                        break;
                    }
                    if (arrayList.get(i).isCapturingFingerprintGestures()) {
                        long clearCallingIdentity = Binder.clearCallingIdentity();
                        try {
                            IFingerprintService asInterface = IFingerprintService.Stub.asInterface(ServiceManager.getService("fingerprint"));
                            if (asInterface != null) {
                                this.mFingerprintGestureDispatcher = new FingerprintGestureDispatcher(asInterface, this.mContext.getResources(), this.mLock);
                                break;
                            }
                        } finally {
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                        }
                    }
                    i++;
                }
            }
        }
        FingerprintGestureDispatcher fingerprintGestureDispatcher = this.mFingerprintGestureDispatcher;
        if (fingerprintGestureDispatcher != null) {
            fingerprintGestureDispatcher.updateClientList(arrayList);
        }
    }

    private void updateAccessibilityButtonTargetsLocked(final AccessibilityUserState accessibilityUserState) {
        for (int size = accessibilityUserState.mBoundServices.size() - 1; size >= 0; size--) {
            AccessibilityServiceConnection accessibilityServiceConnection = accessibilityUserState.mBoundServices.get(size);
            if (accessibilityServiceConnection.mRequestAccessibilityButton) {
                accessibilityServiceConnection.notifyAccessibilityButtonAvailabilityChangedLocked(accessibilityServiceConnection.isAccessibilityButtonAvailableLocked(accessibilityUserState));
            }
        }
        ArraySet<String> shortcutTargetsLocked = accessibilityUserState.getShortcutTargetsLocked(0);
        int size2 = shortcutTargetsLocked.size();
        if (size2 == 0) {
            return;
        }
        shortcutTargetsLocked.removeIf(new Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$updateAccessibilityButtonTargetsLocked$19;
                lambda$updateAccessibilityButtonTargetsLocked$19 = AccessibilityManagerService.lambda$updateAccessibilityButtonTargetsLocked$19(AccessibilityUserState.this, (String) obj);
                return lambda$updateAccessibilityButtonTargetsLocked$19;
            }
        });
        if (size2 == shortcutTargetsLocked.size()) {
            return;
        }
        persistColonDelimitedSetToSettingLocked("accessibility_button_targets", accessibilityUserState.mUserId, shortcutTargetsLocked, new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String lambda$updateAccessibilityButtonTargetsLocked$20;
                lambda$updateAccessibilityButtonTargetsLocked$20 = AccessibilityManagerService.lambda$updateAccessibilityButtonTargetsLocked$20((String) obj);
                return lambda$updateAccessibilityButtonTargetsLocked$20;
            }
        });
        scheduleNotifyClientsOfServicesStateChangeLocked(accessibilityUserState);
    }

    public static /* synthetic */ boolean lambda$updateAccessibilityButtonTargetsLocked$19(AccessibilityUserState accessibilityUserState, String str) {
        return !accessibilityUserState.isShortcutTargetInstalledLocked(str);
    }

    public void migrateAccessibilityButtonSettingsIfNecessaryLocked(final AccessibilityUserState accessibilityUserState, final String str, int i) {
        if (i > 29) {
            return;
        }
        final ArraySet<String> shortcutTargetsLocked = accessibilityUserState.getShortcutTargetsLocked(0);
        int size = shortcutTargetsLocked.size();
        shortcutTargetsLocked.removeIf(new Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda48
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$21;
                lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$21 = AccessibilityManagerService.lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$21(str, accessibilityUserState, (String) obj);
                return lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$21;
            }
        });
        boolean z = size != shortcutTargetsLocked.size();
        int size2 = shortcutTargetsLocked.size();
        final ArraySet<String> shortcutTargetsLocked2 = accessibilityUserState.getShortcutTargetsLocked(1);
        accessibilityUserState.mEnabledServices.forEach(new Consumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda49
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AccessibilityManagerService.lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$22(str, accessibilityUserState, shortcutTargetsLocked, shortcutTargetsLocked2, (ComponentName) obj);
            }
        });
        if (!z && !(size2 != shortcutTargetsLocked.size())) {
            return;
        }
        persistColonDelimitedSetToSettingLocked("accessibility_button_targets", accessibilityUserState.mUserId, shortcutTargetsLocked, new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda50
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$23;
                lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$23 = AccessibilityManagerService.lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$23((String) obj);
                return lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$23;
            }
        });
        scheduleNotifyClientsOfServicesStateChangeLocked(accessibilityUserState);
    }

    public static /* synthetic */ boolean lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$21(String str, AccessibilityUserState accessibilityUserState, String str2) {
        ComponentName unflattenFromString;
        AccessibilityServiceInfo installedServiceInfoLocked;
        if ((str != null && str2 != null && !str2.contains(str)) || (unflattenFromString = ComponentName.unflattenFromString(str2)) == null || (installedServiceInfoLocked = accessibilityUserState.getInstalledServiceInfoLocked(unflattenFromString)) == null) {
            return false;
        }
        if (installedServiceInfoLocked.getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion <= 29) {
            Slog.v(LOG_TAG, "Legacy service " + unflattenFromString + " should not in the button");
            return true;
        }
        if (!((installedServiceInfoLocked.flags & 256) != 0) || accessibilityUserState.mEnabledServices.contains(unflattenFromString)) {
            return false;
        }
        Slog.v(LOG_TAG, "Service requesting a11y button and be assigned to the button" + unflattenFromString + " should be enabled state");
        return true;
    }

    public static /* synthetic */ void lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$22(String str, AccessibilityUserState accessibilityUserState, Set set, Set set2, ComponentName componentName) {
        AccessibilityServiceInfo installedServiceInfoLocked;
        if ((str == null || componentName == null || str.equals(componentName.getPackageName())) && (installedServiceInfoLocked = accessibilityUserState.getInstalledServiceInfoLocked(componentName)) != null) {
            boolean z = (installedServiceInfoLocked.flags & 256) != 0;
            if (installedServiceInfoLocked.getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion <= 29 || !z) {
                return;
            }
            String flattenToString = componentName.flattenToString();
            if (TextUtils.isEmpty(flattenToString) || AccessibilityUserState.doesShortcutTargetsStringContain(set, flattenToString) || AccessibilityUserState.doesShortcutTargetsStringContain(set2, flattenToString)) {
                return;
            }
            Slog.v(LOG_TAG, "A enabled service requesting a11y button " + componentName + " should be assign to the button or shortcut.");
            set.add(flattenToString);
        }
    }

    private void removeShortcutTargetForUnboundServiceLocked(AccessibilityUserState accessibilityUserState, AccessibilityServiceConnection accessibilityServiceConnection) {
        if (!accessibilityServiceConnection.mRequestAccessibilityButton || accessibilityServiceConnection.getServiceInfo().getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion <= 29) {
            return;
        }
        ComponentName componentName = accessibilityServiceConnection.getComponentName();
        if (accessibilityUserState.removeShortcutTargetLocked(1, componentName)) {
            persistColonDelimitedSetToSettingLocked("accessibility_shortcut_target_service", accessibilityUserState.mUserId, accessibilityUserState.getShortcutTargetsLocked(1), new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda28
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    String lambda$removeShortcutTargetForUnboundServiceLocked$24;
                    lambda$removeShortcutTargetForUnboundServiceLocked$24 = AccessibilityManagerService.lambda$removeShortcutTargetForUnboundServiceLocked$24((String) obj);
                    return lambda$removeShortcutTargetForUnboundServiceLocked$24;
                }
            });
        }
        if (accessibilityUserState.removeShortcutTargetLocked(0, componentName)) {
            persistColonDelimitedSetToSettingLocked("accessibility_button_targets", accessibilityUserState.mUserId, accessibilityUserState.getShortcutTargetsLocked(0), new Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda29
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    String lambda$removeShortcutTargetForUnboundServiceLocked$25;
                    lambda$removeShortcutTargetForUnboundServiceLocked$25 = AccessibilityManagerService.lambda$removeShortcutTargetForUnboundServiceLocked$25((String) obj);
                    return lambda$removeShortcutTargetForUnboundServiceLocked$25;
                }
            });
        }
    }

    private void updateRecommendedUiTimeoutLocked(AccessibilityUserState accessibilityUserState) {
        int userNonInteractiveUiTimeoutLocked = accessibilityUserState.getUserNonInteractiveUiTimeoutLocked();
        int userInteractiveUiTimeoutLocked = accessibilityUserState.getUserInteractiveUiTimeoutLocked();
        if (userNonInteractiveUiTimeoutLocked == 0 || userInteractiveUiTimeoutLocked == 0) {
            ArrayList<AccessibilityServiceConnection> arrayList = accessibilityUserState.mBoundServices;
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                int interactiveUiTimeoutMillis = arrayList.get(i3).getServiceInfo().getInteractiveUiTimeoutMillis();
                if (i < interactiveUiTimeoutMillis) {
                    i = interactiveUiTimeoutMillis;
                }
                int nonInteractiveUiTimeoutMillis = arrayList.get(i3).getServiceInfo().getNonInteractiveUiTimeoutMillis();
                if (i2 < nonInteractiveUiTimeoutMillis) {
                    i2 = nonInteractiveUiTimeoutMillis;
                }
            }
            if (userNonInteractiveUiTimeoutLocked == 0) {
                userNonInteractiveUiTimeoutLocked = i2;
            }
            if (userInteractiveUiTimeoutLocked == 0) {
                userInteractiveUiTimeoutLocked = i;
            }
        }
        accessibilityUserState.setNonInteractiveUiTimeoutLocked(userNonInteractiveUiTimeoutLocked);
        accessibilityUserState.setInteractiveUiTimeoutLocked(userInteractiveUiTimeoutLocked);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public KeyEventDispatcher getKeyEventDispatcher() {
        if (this.mKeyEventDispatcher == null) {
            this.mKeyEventDispatcher = new KeyEventDispatcher(this.mMainHandler, 8, this.mLock, this.mPowerManager);
        }
        return this.mKeyEventDispatcher;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public PendingIntent getPendingIntentActivity(Context context, int i, Intent intent, int i2) {
        return PendingIntent.getActivity(context, i, intent, i2);
    }

    public void performAccessibilityShortcut(String str) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.performAccessibilityShortcut", 4L, "targetName=" + str);
        }
        if (UserHandle.getAppId(Binder.getCallingUid()) != 1000 && this.mContext.checkCallingPermission("android.permission.MANAGE_ACCESSIBILITY") != 0) {
            throw new SecurityException("performAccessibilityShortcut requires the MANAGE_ACCESSIBILITY permission");
        }
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new AccessibilityManagerService$$ExternalSyntheticLambda1(), this, 0, 1, str));
    }

    public void performAccessibilityShortcutInternal(int i, int i2, String str) {
        List<String> accessibilityShortcutTargetsInternal = getAccessibilityShortcutTargetsInternal(i2);
        if (accessibilityShortcutTargetsInternal.isEmpty()) {
            Slog.d(LOG_TAG, "No target to perform shortcut, shortcutType=" + i2);
            return;
        }
        if (str != null && !AccessibilityUserState.doesShortcutTargetsStringContain(accessibilityShortcutTargetsInternal, str)) {
            Slog.v(LOG_TAG, "Perform shortcut failed, invalid target name:" + str);
            str = null;
        }
        if (str == null) {
            if (accessibilityShortcutTargetsInternal.size() > 1) {
                showAccessibilityTargetsSelection(i, i2);
                return;
            }
            str = accessibilityShortcutTargetsInternal.get(0);
        }
        if (str.equals("com.android.server.accessibility.MagnificationController")) {
            AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, AccessibilityShortcutController.MAGNIFICATION_COMPONENT_NAME, i2, !getMagnificationController().getFullScreenMagnificationController().isActivated(i));
            sendAccessibilityButtonToInputFilter(i);
            return;
        }
        ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
        if (unflattenFromString == null) {
            Slog.d(LOG_TAG, "Perform shortcut failed, invalid target name:" + str);
            return;
        }
        if (performAccessibilityFrameworkFeature(i, unflattenFromString, i2)) {
            return;
        }
        if (performAccessibilityShortcutTargetActivity(i, unflattenFromString)) {
            AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, unflattenFromString, i2);
        } else {
            performAccessibilityShortcutTargetService(i, i2, unflattenFromString);
        }
    }

    private boolean performAccessibilityFrameworkFeature(int i, ComponentName componentName, int i2) {
        Map frameworkShortcutFeaturesMap = AccessibilityShortcutController.getFrameworkShortcutFeaturesMap();
        if (!frameworkShortcutFeaturesMap.containsKey(componentName)) {
            return false;
        }
        AccessibilityShortcutController.FrameworkFeatureInfo frameworkFeatureInfo = (AccessibilityShortcutController.FrameworkFeatureInfo) frameworkShortcutFeaturesMap.get(componentName);
        SettingsStringUtil.SettingStringHelper settingStringHelper = new SettingsStringUtil.SettingStringHelper(this.mContext.getContentResolver(), frameworkFeatureInfo.getSettingKey(), this.mCurrentUserId);
        if (frameworkFeatureInfo instanceof AccessibilityShortcutController.LaunchableFrameworkFeatureInfo) {
            AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, componentName, i2, true);
            launchAccessibilityFrameworkFeature(i, componentName);
            return true;
        }
        if (!TextUtils.equals(frameworkFeatureInfo.getSettingOnValue(), settingStringHelper.read())) {
            AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, componentName, i2, true);
            settingStringHelper.write(frameworkFeatureInfo.getSettingOnValue());
        } else {
            AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, componentName, i2, false);
            settingStringHelper.write(frameworkFeatureInfo.getSettingOffValue());
        }
        return true;
    }

    private boolean performAccessibilityShortcutTargetActivity(int i, ComponentName componentName) {
        synchronized (this.mLock) {
            AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
            for (int i2 = 0; i2 < currentUserStateLocked.mInstalledShortcuts.size(); i2++) {
                if (currentUserStateLocked.mInstalledShortcuts.get(i2).getComponentName().equals(componentName)) {
                    launchShortcutTargetActivity(i, componentName);
                    return true;
                }
            }
            return false;
        }
    }

    private boolean performAccessibilityShortcutTargetService(int i, int i2, ComponentName componentName) {
        synchronized (this.mLock) {
            AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
            AccessibilityServiceInfo installedServiceInfoLocked = currentUserStateLocked.getInstalledServiceInfoLocked(componentName);
            if (installedServiceInfoLocked == null) {
                Slog.d(LOG_TAG, "Perform shortcut failed, invalid component name:" + componentName);
                return false;
            }
            AccessibilityServiceConnection serviceConnectionLocked = currentUserStateLocked.getServiceConnectionLocked(componentName);
            int i3 = installedServiceInfoLocked.getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion;
            boolean z = (installedServiceInfoLocked.flags & 256) != 0;
            if ((i3 <= 29 && i2 == 1) || (i3 > 29 && !z)) {
                if (serviceConnectionLocked == null) {
                    AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, componentName, i2, true);
                    enableAccessibilityServiceLocked(componentName, this.mCurrentUserId);
                } else {
                    AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, componentName, i2, false);
                    disableAccessibilityServiceLocked(componentName, this.mCurrentUserId);
                }
                return true;
            }
            if (i2 == 1 && i3 > 29 && z && !currentUserStateLocked.getEnabledServicesLocked().contains(componentName)) {
                enableAccessibilityServiceLocked(componentName, this.mCurrentUserId);
                return true;
            }
            if (serviceConnectionLocked != null && currentUserStateLocked.mBoundServices.contains(serviceConnectionLocked) && serviceConnectionLocked.mRequestAccessibilityButton) {
                AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, componentName, i2, true);
                serviceConnectionLocked.notifyAccessibilityButtonClickedLocked(i);
                return true;
            }
            Slog.d(LOG_TAG, "Perform shortcut failed, service is not ready:" + componentName);
            return false;
        }
    }

    private void launchAccessibilityFrameworkFeature(int i, ComponentName componentName) {
        if (componentName.equals(AccessibilityShortcutController.ACCESSIBILITY_HEARING_AIDS_COMPONENT_NAME)) {
            launchAccessibilitySubSettings(i, AccessibilityShortcutController.ACCESSIBILITY_HEARING_AIDS_COMPONENT_NAME);
        }
    }

    public List<String> getAccessibilityShortcutTargets(int i) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getAccessibilityShortcutTargets", 4L, "shortcutType=" + i);
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.MANAGE_ACCESSIBILITY") != 0) {
            throw new SecurityException("getAccessibilityShortcutService requires the MANAGE_ACCESSIBILITY permission");
        }
        return getAccessibilityShortcutTargetsInternal(i);
    }

    private List<String> getAccessibilityShortcutTargetsInternal(int i) {
        synchronized (this.mLock) {
            AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
            ArrayList arrayList = new ArrayList(currentUserStateLocked.getShortcutTargetsLocked(i));
            if (i != 0) {
                return arrayList;
            }
            for (int size = currentUserStateLocked.mBoundServices.size() - 1; size >= 0; size--) {
                AccessibilityServiceConnection accessibilityServiceConnection = currentUserStateLocked.mBoundServices.get(size);
                if (accessibilityServiceConnection.mRequestAccessibilityButton && accessibilityServiceConnection.getServiceInfo().getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion <= 29) {
                    String flattenToString = accessibilityServiceConnection.getComponentName().flattenToString();
                    if (!TextUtils.isEmpty(flattenToString)) {
                        arrayList.add(flattenToString);
                    }
                }
            }
            return arrayList;
        }
    }

    private void enableAccessibilityServiceLocked(ComponentName componentName, int i) {
        this.mTempComponentNameSet.clear();
        readComponentNamesFromSettingLocked("enabled_accessibility_services", i, this.mTempComponentNameSet);
        this.mTempComponentNameSet.add(componentName);
        persistComponentNamesToSettingLocked("enabled_accessibility_services", this.mTempComponentNameSet, i);
        AccessibilityUserState userStateLocked = getUserStateLocked(i);
        if (userStateLocked.mEnabledServices.add(componentName)) {
            onUserStateChangedLocked(userStateLocked);
        }
    }

    private void disableAccessibilityServiceLocked(ComponentName componentName, int i) {
        this.mTempComponentNameSet.clear();
        readComponentNamesFromSettingLocked("enabled_accessibility_services", i, this.mTempComponentNameSet);
        this.mTempComponentNameSet.remove(componentName);
        persistComponentNamesToSettingLocked("enabled_accessibility_services", this.mTempComponentNameSet, i);
        AccessibilityUserState userStateLocked = getUserStateLocked(i);
        if (userStateLocked.mEnabledServices.remove(componentName)) {
            onUserStateChangedLocked(userStateLocked);
        }
    }

    @Override // com.android.server.accessibility.AccessibilityWindowManager.AccessibilityEventSender
    public void sendAccessibilityEventForCurrentUserLocked(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getWindowChanges() == 1) {
            sendPendingWindowStateChangedEventsForAvailableWindowLocked(accessibilityEvent.getWindowId());
        }
        sendAccessibilityEventLocked(accessibilityEvent, this.mCurrentUserId);
    }

    private void sendAccessibilityEventLocked(AccessibilityEvent accessibilityEvent, int i) {
        accessibilityEvent.setEventTime(SystemClock.uptimeMillis());
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda21
            public final void accept(Object obj, Object obj2, Object obj3) {
                ((AccessibilityManagerService) obj).sendAccessibilityEvent((AccessibilityEvent) obj2, ((Integer) obj3).intValue());
            }
        }, this, accessibilityEvent, Integer.valueOf(i)));
    }

    public boolean sendFingerprintGesture(int i) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(131076L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.sendFingerprintGesture", 131076L, "gestureKeyCode=" + i);
        }
        synchronized (this.mLock) {
            if (UserHandle.getAppId(Binder.getCallingUid()) != 1000) {
                throw new SecurityException("Only SYSTEM can call sendFingerprintGesture");
            }
        }
        FingerprintGestureDispatcher fingerprintGestureDispatcher = this.mFingerprintGestureDispatcher;
        if (fingerprintGestureDispatcher == null) {
            return false;
        }
        return fingerprintGestureDispatcher.onFingerprintGesture(i);
    }

    public int getAccessibilityWindowId(IBinder iBinder) {
        int findWindowIdLocked;
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getAccessibilityWindowId", 4L, "windowToken=" + iBinder);
        }
        synchronized (this.mLock) {
            if (UserHandle.getAppId(Binder.getCallingUid()) != 1000) {
                throw new SecurityException("Only SYSTEM can call getAccessibilityWindowId");
            }
            findWindowIdLocked = this.mA11yWindowManager.findWindowIdLocked(this.mCurrentUserId, iBinder);
        }
        return findWindowIdLocked;
    }

    public long getRecommendedTimeoutMillis() {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getRecommendedTimeoutMillis", 4L);
        }
        synchronized (this.mLock) {
            int firstDeviceIdForUidLocked = this.mProxyManager.getFirstDeviceIdForUidLocked(Binder.getCallingUid());
            if (this.mProxyManager.isProxyedDeviceId(firstDeviceIdForUidLocked)) {
                return this.mProxyManager.getRecommendedTimeoutMillisLocked(firstDeviceIdForUidLocked);
            }
            return getRecommendedTimeoutMillisLocked(getCurrentUserStateLocked());
        }
    }

    private long getRecommendedTimeoutMillisLocked(AccessibilityUserState accessibilityUserState) {
        return IntPair.of(accessibilityUserState.getInteractiveUiTimeoutLocked(), accessibilityUserState.getNonInteractiveUiTimeoutLocked());
    }

    public void setWindowMagnificationConnection(IWindowMagnificationConnection iWindowMagnificationConnection) throws RemoteException {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(132L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.setWindowMagnificationConnection", 132L, "connection=" + iWindowMagnificationConnection);
        }
        this.mSecurityPolicy.enforceCallingOrSelfPermission("android.permission.STATUS_BAR_SERVICE");
        getWindowMagnificationMgr().setConnection(iWindowMagnificationConnection);
    }

    public WindowMagnificationManager getWindowMagnificationMgr() {
        WindowMagnificationManager windowMagnificationMgr;
        synchronized (this.mLock) {
            windowMagnificationMgr = this.mMagnificationController.getWindowMagnificationMgr();
        }
        return windowMagnificationMgr;
    }

    public MagnificationController getMagnificationController() {
        return this.mMagnificationController;
    }

    public void associateEmbeddedHierarchy(IBinder iBinder, IBinder iBinder2) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.associateEmbeddedHierarchy", 4L, "host=" + iBinder + ";embedded=" + iBinder2);
        }
        synchronized (this.mLock) {
            this.mA11yWindowManager.associateEmbeddedHierarchyLocked(iBinder, iBinder2);
        }
    }

    public void disassociateEmbeddedHierarchy(IBinder iBinder) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.disassociateEmbeddedHierarchy", 4L, "token=" + iBinder);
        }
        synchronized (this.mLock) {
            this.mA11yWindowManager.disassociateEmbeddedHierarchyLocked(iBinder);
        }
    }

    public int getFocusStrokeWidth() {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getFocusStrokeWidth", 4L);
        }
        synchronized (this.mLock) {
            int firstDeviceIdForUidLocked = this.mProxyManager.getFirstDeviceIdForUidLocked(Binder.getCallingUid());
            if (this.mProxyManager.isProxyedDeviceId(firstDeviceIdForUidLocked)) {
                return this.mProxyManager.getFocusStrokeWidthLocked(firstDeviceIdForUidLocked);
            }
            return getCurrentUserStateLocked().getFocusStrokeWidthLocked();
        }
    }

    public int getFocusColor() {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getFocusColor", 4L);
        }
        synchronized (this.mLock) {
            int firstDeviceIdForUidLocked = this.mProxyManager.getFirstDeviceIdForUidLocked(Binder.getCallingUid());
            if (this.mProxyManager.isProxyedDeviceId(firstDeviceIdForUidLocked)) {
                return this.mProxyManager.getFocusColorLocked(firstDeviceIdForUidLocked);
            }
            return getCurrentUserStateLocked().getFocusColorLocked();
        }
    }

    public boolean isAudioDescriptionByDefaultEnabled() {
        boolean isAudioDescriptionByDefaultEnabledLocked;
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.isAudioDescriptionByDefaultEnabled", 4L);
        }
        synchronized (this.mLock) {
            isAudioDescriptionByDefaultEnabledLocked = getCurrentUserStateLocked().isAudioDescriptionByDefaultEnabledLocked();
        }
        return isAudioDescriptionByDefaultEnabledLocked;
    }

    public void setAccessibilityWindowAttributes(int i, int i2, int i3, AccessibilityWindowAttributes accessibilityWindowAttributes) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.setAccessibilityWindowAttributes", 4L);
        }
        this.mA11yWindowManager.setAccessibilityWindowAttributes(i, i2, i3, accessibilityWindowAttributes);
    }

    @RequiresPermission("android.permission.SET_SYSTEM_AUDIO_CAPTION")
    public void setSystemAudioCaptioningEnabled(boolean z, int i) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.SET_SYSTEM_AUDIO_CAPTION", "setSystemAudioCaptioningEnabled");
        this.mCaptioningManagerImpl.setSystemAudioCaptioningEnabled(z, i);
    }

    public boolean isSystemAudioCaptioningUiEnabled(int i) {
        return this.mCaptioningManagerImpl.isSystemAudioCaptioningUiEnabled(i);
    }

    @RequiresPermission("android.permission.SET_SYSTEM_AUDIO_CAPTION")
    public void setSystemAudioCaptioningUiEnabled(boolean z, int i) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.SET_SYSTEM_AUDIO_CAPTION", "setSystemAudioCaptioningUiEnabled");
        this.mCaptioningManagerImpl.setSystemAudioCaptioningUiEnabled(z, i);
    }

    public boolean registerProxyForDisplay(IAccessibilityServiceClient iAccessibilityServiceClient, int i) throws RemoteException {
        this.mSecurityPolicy.enforceCallingOrSelfPermission("android.permission.CREATE_VIRTUAL_DEVICE");
        this.mSecurityPolicy.checkForAccessibilityPermissionOrRole();
        if (iAccessibilityServiceClient == null) {
            return false;
        }
        if (i < 0) {
            throw new IllegalArgumentException("The display id " + i + " is invalid.");
        }
        if (!isTrackedDisplay(i)) {
            throw new IllegalArgumentException("The display " + i + " does not exist or is not tracked by accessibility.");
        }
        if (this.mProxyManager.isProxyedDisplay(i)) {
            throw new IllegalArgumentException("The display " + i + " is already being proxy-ed");
        }
        if (!this.mProxyManager.displayBelongsToCaller(Binder.getCallingUid(), i)) {
            throw new SecurityException("The display " + i + " does not belong to the caller.");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ProxyManager proxyManager = this.mProxyManager;
            int i2 = sIdCounter;
            sIdCounter = i2 + 1;
            proxyManager.registerProxy(iAccessibilityServiceClient, i, i2, this.mSecurityPolicy, this, getTraceManager(), this.mWindowManagerService);
            synchronized (this.mLock) {
                notifyClearAccessibilityCacheLocked();
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return true;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    public boolean unregisterProxyForDisplay(int i) {
        this.mSecurityPolicy.enforceCallingOrSelfPermission("android.permission.CREATE_VIRTUAL_DEVICE");
        this.mSecurityPolicy.checkForAccessibilityPermissionOrRole();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mProxyManager.unregisterProxy(i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean isDisplayProxyed(int i) {
        return this.mProxyManager.isProxyedDisplay(i);
    }

    public boolean startFlashNotificationSequence(String str, int i, IBinder iBinder) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mFlashNotificationsController.startFlashNotificationSequence(str, i, iBinder);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean stopFlashNotificationSequence(String str) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mFlashNotificationsController.stopFlashNotificationSequence(str);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean startFlashNotificationEvent(String str, int i, String str2) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mFlashNotificationsController.startFlashNotificationEvent(str, i, str2);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0022 A[Catch: all -> 0x0052, TRY_LEAVE, TryCatch #0 {all -> 0x0052, blocks: (B:3:0x0004, B:5:0x0016, B:10:0x0022), top: B:2:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x004e A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isAccessibilityTargetAllowed(String str, int i, int i2) {
        boolean z;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            List permittedAccessibilityServices = ((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class)).getPermittedAccessibilityServices(i2);
            boolean z2 = true;
            if (permittedAccessibilityServices != null && !permittedAccessibilityServices.contains(str)) {
                z = false;
                if (z) {
                    return false;
                }
                int noteOpNoThrow = ((AppOpsManager) this.mContext.getSystemService(AppOpsManager.class)).noteOpNoThrow(119, i, str, (String) null, (String) null);
                if (this.mContext.getResources().getBoolean(17891689) && noteOpNoThrow != 0) {
                    z2 = false;
                }
                return z2;
            }
            z = true;
            if (z) {
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean sendRestrictedDialogIntent(String str, int i, int i2) {
        if (isAccessibilityTargetAllowed(str, i, i2)) {
            return false;
        }
        RestrictedLockUtils.EnforcedAdmin checkIfAccessibilityServiceDisallowed = RestrictedLockUtilsInternal.checkIfAccessibilityServiceDisallowed(this.mContext, str, i2);
        if (checkIfAccessibilityServiceDisallowed != null) {
            RestrictedLockUtils.sendShowAdminSupportDetailsIntent(this.mContext, checkIfAccessibilityServiceDisallowed);
            return true;
        }
        RestrictedLockUtils.sendShowRestrictedSettingDialogIntent(this.mContext, str, i);
        return true;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AccessibilityInputFilter accessibilityInputFilter;
        if (DumpUtils.checkDumpPermission(this.mContext, LOG_TAG, printWriter)) {
            synchronized (this.mLock) {
                printWriter.println("ACCESSIBILITY MANAGER (dumpsys accessibility)");
                printWriter.println();
                printWriter.append("currentUserId=").append((CharSequence) String.valueOf(this.mCurrentUserId));
                int i = this.mRealCurrentUserId;
                if (i != -2 && this.mCurrentUserId != i) {
                    printWriter.append(" (set for UiAutomation purposes; \"real\" current user is ").append((CharSequence) String.valueOf(this.mRealCurrentUserId)).append(")");
                }
                printWriter.println();
                if (this.mVisibleBgUserIds != null) {
                    printWriter.append("visibleBgUserIds=").append((CharSequence) this.mVisibleBgUserIds.toString());
                    printWriter.println();
                }
                printWriter.append("hasWindowMagnificationConnection=").append((CharSequence) String.valueOf(getWindowMagnificationMgr().isConnected()));
                printWriter.println();
                this.mMagnificationProcessor.dump(printWriter, getValidDisplayList());
                int size = this.mUserStates.size();
                for (int i2 = 0; i2 < size; i2++) {
                    this.mUserStates.valueAt(i2).dump(fileDescriptor, printWriter, strArr);
                }
                if (this.mUiAutomationManager.isUiAutomationRunningLocked()) {
                    this.mUiAutomationManager.dumpUiAutomationService(fileDescriptor, printWriter, strArr);
                    printWriter.println();
                }
                this.mA11yWindowManager.dump(fileDescriptor, printWriter, strArr);
                if (this.mHasInputFilter && (accessibilityInputFilter = this.mInputFilter) != null) {
                    accessibilityInputFilter.dump(fileDescriptor, printWriter, strArr);
                }
                printWriter.println("Global client list info:{");
                this.mGlobalClients.dump(printWriter, "    Client list ");
                printWriter.println("    Registered clients:{");
                for (int i3 = 0; i3 < this.mGlobalClients.getRegisteredCallbackCount(); i3++) {
                    printWriter.append((CharSequence) Arrays.toString(((Client) this.mGlobalClients.getRegisteredCallbackCookie(i3)).mPackageNames));
                }
                printWriter.println();
                this.mProxyManager.dump(fileDescriptor, printWriter, strArr);
                this.mA11yDisplayListener.dump(fileDescriptor, printWriter, strArr);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class MainHandler extends Handler {
        public static final int MSG_SEND_KEY_EVENT_TO_INPUT_FILTER = 8;

        public MainHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 8) {
                KeyEvent keyEvent = (KeyEvent) message.obj;
                int i = message.arg1;
                synchronized (AccessibilityManagerService.this.mLock) {
                    if (AccessibilityManagerService.this.mHasInputFilter && AccessibilityManagerService.this.mInputFilter != null) {
                        AccessibilityManagerService.this.mInputFilter.sendInputEvent(keyEvent, i);
                    }
                }
                keyEvent.recycle();
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public MagnificationProcessor getMagnificationProcessor() {
        return this.mMagnificationProcessor;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void onClientChangeLocked(boolean z) {
        onClientChangeLocked(z, false);
    }

    public void onClientChangeLocked(boolean z, boolean z2) {
        AccessibilityUserState userStateLocked = getUserStateLocked(this.mCurrentUserId);
        onUserStateChangedLocked(userStateLocked, z2);
        if (z) {
            scheduleNotifyClientsOfServicesStateChangeLocked(userStateLocked);
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void onProxyChanged(int i) {
        this.mProxyManager.onProxyChanged(i);
    }

    @Override // com.android.server.accessibility.ProxyManager.SystemSupport
    public void removeDeviceIdLocked(int i) {
        resetClientsLocked(i, getCurrentUserStateLocked().mUserClients);
        resetClientsLocked(i, this.mGlobalClients);
        onClientChangeLocked(true, true);
    }

    private void resetClientsLocked(int i, RemoteCallbackList<IAccessibilityManagerClient> remoteCallbackList) {
        if (remoteCallbackList == null || remoteCallbackList.getRegisteredCallbackCount() == 0) {
            return;
        }
        synchronized (this.mLock) {
            for (int i2 = 0; i2 < remoteCallbackList.getRegisteredCallbackCount(); i2++) {
                Client client = (Client) remoteCallbackList.getRegisteredCallbackCookie(i2);
                if (client.mDeviceId == i) {
                    client.mDeviceId = 0;
                }
            }
        }
    }

    @Override // com.android.server.accessibility.ProxyManager.SystemSupport
    public void updateWindowsForAccessibilityCallbackLocked() {
        updateWindowsForAccessibilityCallbackLocked(getUserStateLocked(this.mCurrentUserId));
    }

    @Override // com.android.server.accessibility.ProxyManager.SystemSupport
    public RemoteCallbackList<IAccessibilityManagerClient> getGlobalClientsLocked() {
        return this.mGlobalClients;
    }

    @Override // com.android.server.accessibility.ProxyManager.SystemSupport
    public RemoteCallbackList<IAccessibilityManagerClient> getCurrentUserClientsLocked() {
        return getCurrentUserState().mUserClients;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
        new AccessibilityShellCommand(this, this.mSystemActionPerformer).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class InteractionBridge {
        private final ComponentName COMPONENT_NAME;
        private final AccessibilityInteractionClient mClient;
        private final int mConnectionId;
        private final Display mDefaultDisplay;

        public InteractionBridge() {
            AccessibilityUserState currentUserStateLocked;
            ComponentName componentName = new ComponentName("com.android.server.accessibility", "InteractionBridge");
            this.COMPONENT_NAME = componentName;
            AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
            accessibilityServiceInfo.setCapabilities(1);
            accessibilityServiceInfo.flags = accessibilityServiceInfo.flags | 64 | 2;
            accessibilityServiceInfo.setAccessibilityTool(true);
            synchronized (AccessibilityManagerService.this.mLock) {
                currentUserStateLocked = AccessibilityManagerService.this.getCurrentUserStateLocked();
            }
            Context context = AccessibilityManagerService.this.mContext;
            int i = AccessibilityManagerService.sIdCounter;
            AccessibilityManagerService.sIdCounter = i + 1;
            AnonymousClass1 anonymousClass1 = new AccessibilityServiceConnection(currentUserStateLocked, context, componentName, accessibilityServiceInfo, i, AccessibilityManagerService.this.mMainHandler, AccessibilityManagerService.this.mLock, AccessibilityManagerService.this.mSecurityPolicy, AccessibilityManagerService.this, AccessibilityManagerService.this.getTraceManager(), AccessibilityManagerService.this.mWindowManagerService, AccessibilityManagerService.this.getSystemActionPerformer(), AccessibilityManagerService.this.mA11yWindowManager, AccessibilityManagerService.this.mActivityTaskManagerService) { // from class: com.android.server.accessibility.AccessibilityManagerService.InteractionBridge.1
                final /* synthetic */ AccessibilityManagerService val$this$0;

                @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
                public boolean supportsFlagForNotImportantViews(AccessibilityServiceInfo accessibilityServiceInfo2) {
                    return true;
                }

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                AnonymousClass1(AccessibilityUserState currentUserStateLocked2, Context context2, ComponentName componentName2, AccessibilityServiceInfo accessibilityServiceInfo2, int i2, Handler handler, Object obj, AccessibilitySecurityPolicy accessibilitySecurityPolicy, AbstractAccessibilityServiceConnection.SystemSupport systemSupport, AccessibilityTrace accessibilityTrace, WindowManagerInternal windowManagerInternal, SystemActionPerformer systemActionPerformer, AccessibilityWindowManager accessibilityWindowManager, ActivityTaskManagerInternal activityTaskManagerInternal, AccessibilityManagerService accessibilityManagerService) {
                    super(currentUserStateLocked2, context2, componentName2, accessibilityServiceInfo2, i2, handler, obj, accessibilitySecurityPolicy, systemSupport, accessibilityTrace, windowManagerInternal, systemActionPerformer, accessibilityWindowManager, activityTaskManagerInternal);
                    r31 = accessibilityManagerService;
                }
            };
            int i2 = anonymousClass1.mId;
            this.mConnectionId = i2;
            this.mClient = AccessibilityInteractionClient.getInstance(AccessibilityManagerService.this.mContext);
            AccessibilityInteractionClient.addConnection(i2, anonymousClass1, false);
            this.mDefaultDisplay = ((DisplayManager) AccessibilityManagerService.this.mContext.getSystemService("display")).getDisplay(0);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.android.server.accessibility.AccessibilityManagerService$InteractionBridge$1 */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public class AnonymousClass1 extends AccessibilityServiceConnection {
            final /* synthetic */ AccessibilityManagerService val$this$0;

            @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
            public boolean supportsFlagForNotImportantViews(AccessibilityServiceInfo accessibilityServiceInfo2) {
                return true;
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(AccessibilityUserState currentUserStateLocked2, Context context2, ComponentName componentName2, AccessibilityServiceInfo accessibilityServiceInfo2, int i2, Handler handler, Object obj, AccessibilitySecurityPolicy accessibilitySecurityPolicy, AbstractAccessibilityServiceConnection.SystemSupport systemSupport, AccessibilityTrace accessibilityTrace, WindowManagerInternal windowManagerInternal, SystemActionPerformer systemActionPerformer, AccessibilityWindowManager accessibilityWindowManager, ActivityTaskManagerInternal activityTaskManagerInternal, AccessibilityManagerService accessibilityManagerService) {
                super(currentUserStateLocked2, context2, componentName2, accessibilityServiceInfo2, i2, handler, obj, accessibilitySecurityPolicy, systemSupport, accessibilityTrace, windowManagerInternal, systemActionPerformer, accessibilityWindowManager, activityTaskManagerInternal);
                r31 = accessibilityManagerService;
            }
        }

        boolean getAccessibilityFocusClickPointInScreen(Point point) {
            return AccessibilityManagerService.this.getInteractionBridge().getAccessibilityFocusClickPointInScreenNotLocked(point);
        }

        public boolean performActionOnAccessibilityFocusedItemNotLocked(AccessibilityNodeInfo.AccessibilityAction accessibilityAction) {
            AccessibilityNodeInfo accessibilityFocusNotLocked = AccessibilityManagerService.this.mServiceExt.getAccessibilityFocusNotLocked(getAccessibilityFocusNotLocked(), accessibilityAction);
            if (accessibilityFocusNotLocked == null || !accessibilityFocusNotLocked.getActionList().contains(accessibilityAction)) {
                return false;
            }
            return accessibilityFocusNotLocked.performAction(accessibilityAction.getId());
        }

        public boolean getAccessibilityFocusClickPointInScreenNotLocked(Point point) {
            MagnificationSpec magnificationSpec;
            AccessibilityNodeInfo accessibilityFocusNotLocked = getAccessibilityFocusNotLocked();
            if (accessibilityFocusNotLocked == null) {
                return false;
            }
            synchronized (AccessibilityManagerService.this.mLock) {
                Rect rect = AccessibilityManagerService.this.mTempRect;
                accessibilityFocusNotLocked.getBoundsInScreen(rect);
                Point point2 = new Point(rect.centerX(), rect.centerY());
                Pair<float[], MagnificationSpec> windowTransformationMatrixAndMagnificationSpec = AccessibilityManagerService.this.getWindowTransformationMatrixAndMagnificationSpec(accessibilityFocusNotLocked.getWindowId());
                if (windowTransformationMatrixAndMagnificationSpec == null || windowTransformationMatrixAndMagnificationSpec.second == null) {
                    magnificationSpec = null;
                } else {
                    magnificationSpec = new MagnificationSpec();
                    magnificationSpec.setTo((MagnificationSpec) windowTransformationMatrixAndMagnificationSpec.second);
                }
                if (magnificationSpec != null && !magnificationSpec.isNop()) {
                    rect.offset((int) (-magnificationSpec.offsetX), (int) (-magnificationSpec.offsetY));
                    rect.scale(1.0f / magnificationSpec.scale);
                }
                Rect rect2 = AccessibilityManagerService.this.mTempRect1;
                AccessibilityManagerService.this.getWindowBounds(accessibilityFocusNotLocked.getWindowId(), rect2);
                if (!rect.intersect(rect2)) {
                    return false;
                }
                Point point3 = AccessibilityManagerService.this.mTempPoint;
                this.mDefaultDisplay.getRealSize(point3);
                if (!rect.intersect(0, 0, point3.x, point3.y)) {
                    return false;
                }
                point.set(point2.x, point2.y);
                return true;
            }
        }

        private AccessibilityNodeInfo getAccessibilityFocusNotLocked() {
            synchronized (AccessibilityManagerService.this.mLock) {
                int focusedWindowId = AccessibilityManagerService.this.mA11yWindowManager.getFocusedWindowId(2);
                if (focusedWindowId == -1) {
                    return null;
                }
                return getAccessibilityFocusNotLocked(focusedWindowId);
            }
        }

        private AccessibilityNodeInfo getAccessibilityFocusNotLocked(int i) {
            return this.mClient.findFocus(this.mConnectionId, i, AccessibilityNodeInfo.ROOT_NODE_ID, 2);
        }
    }

    public ArrayList<Display> getValidDisplayList() {
        return this.mA11yDisplayListener.getValidDisplayList();
    }

    private boolean isTrackedDisplay(int i) {
        Iterator<Display> it = getValidDisplayList().iterator();
        while (it.hasNext()) {
            if (it.next().getDisplayId() == i) {
                return true;
            }
        }
        return false;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AccessibilityDisplayListener implements DisplayManager.DisplayListener {
        private final DisplayManager mDisplayManager;
        private final ArrayList<Display> mDisplaysList = new ArrayList<>();
        private int mSystemUiUid;

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int i) {
        }

        AccessibilityDisplayListener(Context context, Handler handler) {
            this.mSystemUiUid = 0;
            DisplayManager displayManager = (DisplayManager) context.getSystemService("display");
            this.mDisplayManager = displayManager;
            displayManager.registerDisplayListener(this, handler);
            initializeDisplayList();
            PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
            if (packageManagerInternal != null) {
                this.mSystemUiUid = packageManagerInternal.getPackageUid(packageManagerInternal.getSystemUiServiceComponent().getPackageName(), 1048576L, AccessibilityManagerService.this.mCurrentUserId);
            }
        }

        public ArrayList<Display> getValidDisplayList() {
            ArrayList<Display> arrayList;
            synchronized (AccessibilityManagerService.this.mLock) {
                arrayList = this.mDisplaysList;
            }
            return arrayList;
        }

        private void initializeDisplayList() {
            Display[] displays = this.mDisplayManager.getDisplays();
            synchronized (AccessibilityManagerService.this.mLock) {
                this.mDisplaysList.clear();
                for (Display display : displays) {
                    if (isValidDisplay(display)) {
                        this.mDisplaysList.add(display);
                    }
                }
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int i) {
            Display display = this.mDisplayManager.getDisplay(i);
            if (isValidDisplay(display)) {
                synchronized (AccessibilityManagerService.this.mLock) {
                    this.mDisplaysList.add(display);
                    AccessibilityManagerService.this.mA11yOverlayLayers.put(i, AccessibilityManagerService.this.mWindowManagerService.getA11yOverlayLayer(i));
                    if (AccessibilityManagerService.this.mInputFilter != null) {
                        AccessibilityManagerService.this.mInputFilter.onDisplayAdded(display);
                    }
                    AccessibilityUserState currentUserStateLocked = AccessibilityManagerService.this.getCurrentUserStateLocked();
                    if (i != 0) {
                        ArrayList<AccessibilityServiceConnection> arrayList = currentUserStateLocked.mBoundServices;
                        for (int i2 = 0; i2 < arrayList.size(); i2++) {
                            arrayList.get(i2).onDisplayAdded(i);
                        }
                    }
                    AccessibilityManagerService.this.updateMagnificationLocked(currentUserStateLocked);
                    AccessibilityManagerService.this.updateWindowsForAccessibilityCallbackLocked(currentUserStateLocked);
                    AccessibilityManagerService.this.notifyClearAccessibilityCacheLocked();
                }
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int i) {
            synchronized (AccessibilityManagerService.this.mLock) {
                if (removeDisplayFromList(i)) {
                    AccessibilityManagerService.this.mA11yOverlayLayers.remove(i);
                    if (AccessibilityManagerService.this.mInputFilter != null) {
                        AccessibilityManagerService.this.mInputFilter.onDisplayRemoved(i);
                    }
                    AccessibilityUserState currentUserStateLocked = AccessibilityManagerService.this.getCurrentUserStateLocked();
                    if (i != 0) {
                        ArrayList<AccessibilityServiceConnection> arrayList = currentUserStateLocked.mBoundServices;
                        for (int i2 = 0; i2 < arrayList.size(); i2++) {
                            arrayList.get(i2).onDisplayRemoved(i);
                        }
                    }
                    AccessibilityManagerService.this.mMagnificationController.onDisplayRemoved(i);
                    AccessibilityManagerService.this.mA11yWindowManager.stopTrackingWindows(i);
                }
            }
        }

        @GuardedBy({"mLock"})
        private boolean removeDisplayFromList(int i) {
            for (int i2 = 0; i2 < this.mDisplaysList.size(); i2++) {
                if (this.mDisplaysList.get(i2).getDisplayId() == i) {
                    this.mDisplaysList.remove(i2);
                    return true;
                }
            }
            return false;
        }

        void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            printWriter.println("Accessibility Display Listener:");
            printWriter.println("    SystemUI uid: " + this.mSystemUiUid);
            int size = this.mDisplaysList.size();
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(size);
            objArr[1] = size == 1 ? "" : "s";
            printWriter.printf("    %d valid display%s: ", objArr);
            for (int i = 0; i < size; i++) {
                printWriter.print(this.mDisplaysList.get(i).getDisplayId());
                if (i < size - 1) {
                    printWriter.print(", ");
                }
            }
            printWriter.println();
        }

        private boolean isValidDisplay(Display display) {
            if (display == null || display.getType() == 4) {
                return false;
            }
            return display.getType() != 5 || (display.getFlags() & 4) == 0 || display.getOwnerUid() == this.mSystemUiUid;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class Client {
        final IAccessibilityManagerClient mCallback;
        int mDeviceId;
        int mLastSentRelevantEventTypes;
        final String[] mPackageNames;
        int mUid;

        /* synthetic */ Client(AccessibilityManagerService accessibilityManagerService, IAccessibilityManagerClient iAccessibilityManagerClient, int i, AccessibilityUserState accessibilityUserState, int i2, ClientIA clientIA) {
            this(iAccessibilityManagerClient, i, accessibilityUserState, i2);
        }

        private Client(IAccessibilityManagerClient iAccessibilityManagerClient, int i, AccessibilityUserState accessibilityUserState, int i2) {
            this.mDeviceId = 0;
            this.mCallback = iAccessibilityManagerClient;
            this.mPackageNames = AccessibilityManagerService.this.mPackageManager.getPackagesForUid(i);
            this.mUid = i;
            this.mDeviceId = i2;
            synchronized (AccessibilityManagerService.this.mLock) {
                if (AccessibilityManagerService.this.mProxyManager.isProxyedDeviceId(i2)) {
                    this.mLastSentRelevantEventTypes = AccessibilityManagerService.this.mProxyManager.computeRelevantEventTypesLocked(this);
                } else {
                    this.mLastSentRelevantEventTypes = AccessibilityManagerService.this.computeRelevantEventTypesLocked(accessibilityUserState, this);
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class AccessibilityContentObserver extends ContentObserver {
        private final Uri mAccessibilityButtonComponentIdUri;
        private final Uri mAccessibilityButtonTargetsUri;
        private final Uri mAccessibilityShortcutServiceIdUri;
        private final Uri mAccessibilitySoftKeyboardModeUri;
        private final Uri mAlwaysOnMagnificationUri;
        private final Uri mAudioDescriptionByDefaultUri;
        private final Uri mAutoclickEnabledUri;
        private final Uri mDisplayMagnificationEnabledUri;
        private final Uri mEnabledAccessibilityServicesUri;
        private final Uri mHighTextContrastUri;
        private final Uri mMagnificationCapabilityUri;
        private final Uri mMagnificationFollowTypingUri;
        private final Uri mMagnificationModeUri;
        private final Uri mShowImeWithHardKeyboardUri;
        private final Uri mTouchExplorationEnabledUri;
        private final Uri mTouchExplorationGrantedAccessibilityServicesUri;
        private final Uri mUserInteractiveUiTimeoutUri;
        private final Uri mUserNonInteractiveUiTimeoutUri;

        public AccessibilityContentObserver(Handler handler) {
            super(handler);
            this.mTouchExplorationEnabledUri = Settings.Secure.getUriFor("touch_exploration_enabled");
            this.mDisplayMagnificationEnabledUri = Settings.Secure.getUriFor("accessibility_display_magnification_enabled");
            this.mAutoclickEnabledUri = Settings.Secure.getUriFor("accessibility_autoclick_enabled");
            this.mEnabledAccessibilityServicesUri = Settings.Secure.getUriFor("enabled_accessibility_services");
            this.mTouchExplorationGrantedAccessibilityServicesUri = Settings.Secure.getUriFor("touch_exploration_granted_accessibility_services");
            this.mHighTextContrastUri = Settings.Secure.getUriFor("high_text_contrast_enabled");
            this.mAudioDescriptionByDefaultUri = Settings.Secure.getUriFor("enabled_accessibility_audio_description_by_default");
            this.mAccessibilitySoftKeyboardModeUri = Settings.Secure.getUriFor("accessibility_soft_keyboard_mode");
            this.mShowImeWithHardKeyboardUri = Settings.Secure.getUriFor("show_ime_with_hard_keyboard");
            this.mAccessibilityShortcutServiceIdUri = Settings.Secure.getUriFor("accessibility_shortcut_target_service");
            this.mAccessibilityButtonComponentIdUri = Settings.Secure.getUriFor("accessibility_button_target_component");
            this.mAccessibilityButtonTargetsUri = Settings.Secure.getUriFor("accessibility_button_targets");
            this.mUserNonInteractiveUiTimeoutUri = Settings.Secure.getUriFor("accessibility_non_interactive_ui_timeout_ms");
            this.mUserInteractiveUiTimeoutUri = Settings.Secure.getUriFor("accessibility_interactive_ui_timeout_ms");
            this.mMagnificationModeUri = Settings.Secure.getUriFor("accessibility_magnification_mode");
            this.mMagnificationCapabilityUri = Settings.Secure.getUriFor("accessibility_magnification_capability");
            this.mMagnificationFollowTypingUri = Settings.Secure.getUriFor("accessibility_magnification_follow_typing_enabled");
            this.mAlwaysOnMagnificationUri = Settings.Secure.getUriFor("accessibility_magnification_always_on_enabled");
        }

        public void register(ContentResolver contentResolver) {
            contentResolver.registerContentObserver(this.mTouchExplorationEnabledUri, false, this, -1);
            contentResolver.registerContentObserver(this.mDisplayMagnificationEnabledUri, false, this, -1);
            contentResolver.registerContentObserver(this.mAutoclickEnabledUri, false, this, -1);
            contentResolver.registerContentObserver(this.mEnabledAccessibilityServicesUri, false, this, -1);
            contentResolver.registerContentObserver(this.mTouchExplorationGrantedAccessibilityServicesUri, false, this, -1);
            contentResolver.registerContentObserver(this.mHighTextContrastUri, false, this, -1);
            contentResolver.registerContentObserver(this.mAudioDescriptionByDefaultUri, false, this, -1);
            contentResolver.registerContentObserver(this.mAccessibilitySoftKeyboardModeUri, false, this, -1);
            contentResolver.registerContentObserver(this.mShowImeWithHardKeyboardUri, false, this, -1);
            contentResolver.registerContentObserver(this.mAccessibilityShortcutServiceIdUri, false, this, -1);
            contentResolver.registerContentObserver(this.mAccessibilityButtonComponentIdUri, false, this, -1);
            contentResolver.registerContentObserver(this.mAccessibilityButtonTargetsUri, false, this, -1);
            contentResolver.registerContentObserver(this.mUserNonInteractiveUiTimeoutUri, false, this, -1);
            contentResolver.registerContentObserver(this.mUserInteractiveUiTimeoutUri, false, this, -1);
            contentResolver.registerContentObserver(this.mMagnificationModeUri, false, this, -1);
            contentResolver.registerContentObserver(this.mMagnificationCapabilityUri, false, this, -1);
            contentResolver.registerContentObserver(this.mMagnificationFollowTypingUri, false, this, -1);
            contentResolver.registerContentObserver(this.mAlwaysOnMagnificationUri, false, this, -1);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            synchronized (AccessibilityManagerService.this.mLock) {
                AccessibilityUserState currentUserStateLocked = AccessibilityManagerService.this.getCurrentUserStateLocked();
                if (this.mTouchExplorationEnabledUri.equals(uri)) {
                    if (AccessibilityManagerService.this.readTouchExplorationEnabledSettingLocked(currentUserStateLocked)) {
                        AccessibilityManagerService.this.onUserStateChangedLocked(currentUserStateLocked);
                    }
                } else if (this.mDisplayMagnificationEnabledUri.equals(uri)) {
                    if (AccessibilityManagerService.this.readMagnificationEnabledSettingsLocked(currentUserStateLocked)) {
                        AccessibilityManagerService.this.onUserStateChangedLocked(currentUserStateLocked);
                    }
                } else if (this.mAutoclickEnabledUri.equals(uri)) {
                    if (AccessibilityManagerService.this.readAutoclickEnabledSettingLocked(currentUserStateLocked)) {
                        AccessibilityManagerService.this.onUserStateChangedLocked(currentUserStateLocked);
                    }
                } else if (this.mEnabledAccessibilityServicesUri.equals(uri)) {
                    if (AccessibilityManagerService.this.readEnabledAccessibilityServicesLocked(currentUserStateLocked)) {
                        AccessibilityManagerService.this.mSecurityPolicy.onEnabledServicesChangedLocked(currentUserStateLocked.mUserId, currentUserStateLocked.mEnabledServices);
                        currentUserStateLocked.removeDisabledServicesFromTemporaryStatesLocked();
                        AccessibilityManagerService.this.onUserStateChangedLocked(currentUserStateLocked);
                    }
                } else if (this.mTouchExplorationGrantedAccessibilityServicesUri.equals(uri)) {
                    if (AccessibilityManagerService.this.readTouchExplorationGrantedAccessibilityServicesLocked(currentUserStateLocked)) {
                        AccessibilityManagerService.this.onUserStateChangedLocked(currentUserStateLocked);
                    }
                } else if (this.mHighTextContrastUri.equals(uri)) {
                    if (AccessibilityManagerService.this.readHighTextContrastEnabledSettingLocked(currentUserStateLocked)) {
                        AccessibilityManagerService.this.onUserStateChangedLocked(currentUserStateLocked);
                    }
                } else if (this.mAudioDescriptionByDefaultUri.equals(uri)) {
                    if (AccessibilityManagerService.this.readAudioDescriptionEnabledSettingLocked(currentUserStateLocked)) {
                        AccessibilityManagerService.this.onUserStateChangedLocked(currentUserStateLocked);
                    }
                } else {
                    if (!this.mAccessibilitySoftKeyboardModeUri.equals(uri) && !this.mShowImeWithHardKeyboardUri.equals(uri)) {
                        if (this.mAccessibilityShortcutServiceIdUri.equals(uri)) {
                            if (AccessibilityManagerService.this.readAccessibilityShortcutKeySettingLocked(currentUserStateLocked)) {
                                AccessibilityManagerService.this.onUserStateChangedLocked(currentUserStateLocked);
                            }
                        } else if (this.mAccessibilityButtonComponentIdUri.equals(uri)) {
                            if (AccessibilityManagerService.this.readAccessibilityButtonTargetComponentLocked(currentUserStateLocked)) {
                                AccessibilityManagerService.this.onUserStateChangedLocked(currentUserStateLocked);
                            }
                        } else if (this.mAccessibilityButtonTargetsUri.equals(uri)) {
                            if (AccessibilityManagerService.this.readAccessibilityButtonTargetsLocked(currentUserStateLocked)) {
                                AccessibilityManagerService.this.onUserStateChangedLocked(currentUserStateLocked);
                            }
                        } else {
                            if (!this.mUserNonInteractiveUiTimeoutUri.equals(uri) && !this.mUserInteractiveUiTimeoutUri.equals(uri)) {
                                if (this.mMagnificationModeUri.equals(uri)) {
                                    if (AccessibilityManagerService.this.readMagnificationModeForDefaultDisplayLocked(currentUserStateLocked)) {
                                        AccessibilityManagerService.this.updateMagnificationModeChangeSettingsLocked(currentUserStateLocked, 0);
                                    }
                                } else if (this.mMagnificationCapabilityUri.equals(uri)) {
                                    if (AccessibilityManagerService.this.readMagnificationCapabilitiesLocked(currentUserStateLocked)) {
                                        AccessibilityManagerService.this.updateMagnificationCapabilitiesSettingsChangeLocked(currentUserStateLocked);
                                    }
                                } else if (this.mMagnificationFollowTypingUri.equals(uri)) {
                                    AccessibilityManagerService.this.readMagnificationFollowTypingLocked(currentUserStateLocked);
                                } else if (this.mAlwaysOnMagnificationUri.equals(uri)) {
                                    AccessibilityManagerService.this.readAlwaysOnMagnificationLocked(currentUserStateLocked);
                                }
                            }
                            AccessibilityManagerService.this.readUserRecommendedUiTimeoutSettingsLocked(currentUserStateLocked);
                        }
                    }
                    currentUserStateLocked.reconcileSoftKeyboardModeWithSettingsLocked();
                }
            }
        }
    }

    public void updateMagnificationCapabilitiesSettingsChangeLocked(AccessibilityUserState accessibilityUserState) {
        ArrayList<Display> validDisplayList = getValidDisplayList();
        for (int i = 0; i < validDisplayList.size(); i++) {
            int displayId = validDisplayList.get(i).getDisplayId();
            if (fallBackMagnificationModeSettingsLocked(accessibilityUserState, displayId)) {
                updateMagnificationModeChangeSettingsLocked(accessibilityUserState, displayId);
            }
        }
        updateWindowMagnificationConnectionIfNeeded(accessibilityUserState);
        if ((accessibilityUserState.isDisplayMagnificationEnabledLocked() || accessibilityUserState.isShortcutMagnificationEnabledLocked()) && accessibilityUserState.getMagnificationCapabilitiesLocked() == 3) {
            return;
        }
        for (int i2 = 0; i2 < validDisplayList.size(); i2++) {
            getWindowMagnificationMgr().removeMagnificationButton(validDisplayList.get(i2).getDisplayId());
        }
    }

    private boolean fallBackMagnificationModeSettingsLocked(AccessibilityUserState accessibilityUserState, int i) {
        if (accessibilityUserState.isValidMagnificationModeLocked(i)) {
            return false;
        }
        Slog.w(LOG_TAG, "displayId " + i + ", invalid magnification mode:" + accessibilityUserState.getMagnificationModeLocked(i));
        int magnificationCapabilitiesLocked = accessibilityUserState.getMagnificationCapabilitiesLocked();
        accessibilityUserState.setMagnificationModeLocked(i, magnificationCapabilitiesLocked);
        if (i != 0) {
            return true;
        }
        persistMagnificationModeSettingsLocked(magnificationCapabilitiesLocked);
        return true;
    }

    private void persistMagnificationModeSettingsLocked(final int i) {
        BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                AccessibilityManagerService.this.lambda$persistMagnificationModeSettingsLocked$26(i);
            }
        });
    }

    public /* synthetic */ void lambda$persistMagnificationModeSettingsLocked$26(int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "accessibility_magnification_mode", i, this.mCurrentUserId);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public int getMagnificationMode(int i) {
        int magnificationModeLocked;
        synchronized (this.mLock) {
            magnificationModeLocked = getCurrentUserStateLocked().getMagnificationModeLocked(i);
        }
        return magnificationModeLocked;
    }

    public boolean readMagnificationModeForDefaultDisplayLocked(AccessibilityUserState accessibilityUserState) {
        int intForUser = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_magnification_mode", 1, accessibilityUserState.mUserId);
        if (intForUser == accessibilityUserState.getMagnificationModeLocked(0)) {
            return false;
        }
        accessibilityUserState.setMagnificationModeLocked(0, intForUser);
        return true;
    }

    public boolean readMagnificationCapabilitiesLocked(AccessibilityUserState accessibilityUserState) {
        int intForUser = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_magnification_capability", 1, accessibilityUserState.mUserId);
        if (intForUser == accessibilityUserState.getMagnificationCapabilitiesLocked()) {
            return false;
        }
        accessibilityUserState.setMagnificationCapabilitiesLocked(intForUser);
        this.mMagnificationController.setMagnificationCapabilities(intForUser);
        return true;
    }

    boolean readMagnificationFollowTypingLocked(AccessibilityUserState accessibilityUserState) {
        boolean z = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_magnification_follow_typing_enabled", 1, accessibilityUserState.mUserId) == 1;
        if (z == accessibilityUserState.isMagnificationFollowTypingEnabled()) {
            return false;
        }
        accessibilityUserState.setMagnificationFollowTypingEnabled(z);
        this.mMagnificationController.setMagnificationFollowTypingEnabled(z);
        return true;
    }

    public void updateAlwaysOnMagnification() {
        synchronized (this.mLock) {
            readAlwaysOnMagnificationLocked(getCurrentUserState());
        }
    }

    @GuardedBy({"mLock"})
    boolean readAlwaysOnMagnificationLocked(AccessibilityUserState accessibilityUserState) {
        boolean z = this.mMagnificationController.isAlwaysOnMagnificationFeatureFlagEnabled() && (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_magnification_always_on_enabled", 1, accessibilityUserState.mUserId) == 1);
        if (z == accessibilityUserState.isAlwaysOnMagnificationEnabled()) {
            return false;
        }
        accessibilityUserState.setAlwaysOnMagnificationEnabled(z);
        this.mMagnificationController.setAlwaysOnMagnificationEnabled(z);
        return true;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void setGestureDetectionPassthroughRegion(int i, Region region) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda3
            public final void accept(Object obj, Object obj2, Object obj3) {
                ((AccessibilityManagerService) obj).setGestureDetectionPassthroughRegionInternal(((Integer) obj2).intValue(), (Region) obj3);
            }
        }, this, Integer.valueOf(i), region));
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void setTouchExplorationPassthroughRegion(int i, Region region) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda47
            public final void accept(Object obj, Object obj2, Object obj3) {
                ((AccessibilityManagerService) obj).setTouchExplorationPassthroughRegionInternal(((Integer) obj2).intValue(), (Region) obj3);
            }
        }, this, Integer.valueOf(i), region));
    }

    public void setTouchExplorationPassthroughRegionInternal(int i, Region region) {
        AccessibilityInputFilter accessibilityInputFilter;
        synchronized (this.mLock) {
            if (this.mHasInputFilter && (accessibilityInputFilter = this.mInputFilter) != null) {
                accessibilityInputFilter.setTouchExplorationPassthroughRegion(i, region);
            }
        }
    }

    public void setGestureDetectionPassthroughRegionInternal(int i, Region region) {
        AccessibilityInputFilter accessibilityInputFilter;
        synchronized (this.mLock) {
            if (this.mHasInputFilter && (accessibilityInputFilter = this.mInputFilter) != null) {
                accessibilityInputFilter.setGestureDetectionPassthroughRegion(i, region);
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void setServiceDetectsGesturesEnabled(int i, boolean z) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda39
            public final void accept(Object obj, Object obj2, Object obj3) {
                ((AccessibilityManagerService) obj).setServiceDetectsGesturesInternal(((Integer) obj2).intValue(), ((Boolean) obj3).booleanValue());
            }
        }, this, Integer.valueOf(i), Boolean.valueOf(z)));
    }

    public void setServiceDetectsGesturesInternal(int i, boolean z) {
        AccessibilityInputFilter accessibilityInputFilter;
        synchronized (this.mLock) {
            getCurrentUserStateLocked().setServiceDetectsGesturesEnabled(i, z);
            if (this.mHasInputFilter && (accessibilityInputFilter = this.mInputFilter) != null) {
                accessibilityInputFilter.setServiceDetectsGesturesEnabled(i, z);
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void requestTouchExploration(int i) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda33
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((AccessibilityManagerService) obj).requestTouchExplorationInternal(((Integer) obj2).intValue());
            }
        }, this, Integer.valueOf(i)));
    }

    public void requestTouchExplorationInternal(int i) {
        AccessibilityInputFilter accessibilityInputFilter;
        synchronized (this.mLock) {
            if (this.mHasInputFilter && (accessibilityInputFilter = this.mInputFilter) != null) {
                accessibilityInputFilter.requestTouchExploration(i);
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void requestDragging(int i, int i2) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda27
            public final void accept(Object obj, Object obj2, Object obj3) {
                ((AccessibilityManagerService) obj).requestDraggingInternal(((Integer) obj2).intValue(), ((Integer) obj3).intValue());
            }
        }, this, Integer.valueOf(i), Integer.valueOf(i2)));
    }

    public void requestDraggingInternal(int i, int i2) {
        AccessibilityInputFilter accessibilityInputFilter;
        synchronized (this.mLock) {
            if (this.mHasInputFilter && (accessibilityInputFilter = this.mInputFilter) != null) {
                accessibilityInputFilter.requestDragging(i, i2);
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void requestDelegating(int i) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda37
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((AccessibilityManagerService) obj).requestDelegatingInternal(((Integer) obj2).intValue());
            }
        }, this, Integer.valueOf(i)));
    }

    public void requestDelegatingInternal(int i) {
        AccessibilityInputFilter accessibilityInputFilter;
        synchronized (this.mLock) {
            if (this.mHasInputFilter && (accessibilityInputFilter = this.mInputFilter) != null) {
                accessibilityInputFilter.requestDelegating(i);
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void onDoubleTap(int i) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda57
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((AccessibilityManagerService) obj).onDoubleTapInternal(((Integer) obj2).intValue());
            }
        }, this, Integer.valueOf(i)));
    }

    public void onDoubleTapInternal(int i) {
        AccessibilityInputFilter accessibilityInputFilter;
        synchronized (this.mLock) {
            if (!this.mHasInputFilter || (accessibilityInputFilter = this.mInputFilter) == null) {
                accessibilityInputFilter = null;
            }
        }
        if (accessibilityInputFilter != null) {
            accessibilityInputFilter.onDoubleTap(i);
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void onDoubleTapAndHold(int i) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((AccessibilityManagerService) obj).onDoubleTapAndHoldInternal(((Integer) obj2).intValue());
            }
        }, this, Integer.valueOf(i)));
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void requestImeLocked(AbstractAccessibilityServiceConnection abstractAccessibilityServiceConnection) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda52
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((AccessibilityManagerService) obj).createSessionForConnection((AbstractAccessibilityServiceConnection) obj2);
            }
        }, this, abstractAccessibilityServiceConnection));
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda53
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((AccessibilityManagerService) obj).bindAndStartInputForConnection((AbstractAccessibilityServiceConnection) obj2);
            }
        }, this, abstractAccessibilityServiceConnection));
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void unbindImeLocked(AbstractAccessibilityServiceConnection abstractAccessibilityServiceConnection) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda9
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((AccessibilityManagerService) obj).unbindInputForConnection((AbstractAccessibilityServiceConnection) obj2);
            }
        }, this, abstractAccessibilityServiceConnection));
    }

    public void createSessionForConnection(AbstractAccessibilityServiceConnection abstractAccessibilityServiceConnection) {
        synchronized (this.mLock) {
            if (this.mInputSessionRequested) {
                abstractAccessibilityServiceConnection.createImeSessionLocked();
            }
        }
    }

    public void bindAndStartInputForConnection(AbstractAccessibilityServiceConnection abstractAccessibilityServiceConnection) {
        synchronized (this.mLock) {
            if (this.mInputBound) {
                abstractAccessibilityServiceConnection.bindInputLocked();
                abstractAccessibilityServiceConnection.startInputLocked(this.mRemoteInputConnection, this.mEditorInfo, this.mRestarting);
            }
        }
    }

    public void unbindInputForConnection(AbstractAccessibilityServiceConnection abstractAccessibilityServiceConnection) {
        InputMethodManagerInternal.get().unbindAccessibilityFromCurrentClient(abstractAccessibilityServiceConnection.mId);
        synchronized (this.mLock) {
            abstractAccessibilityServiceConnection.unbindInputLocked();
        }
    }

    public void onDoubleTapAndHoldInternal(int i) {
        AccessibilityInputFilter accessibilityInputFilter;
        synchronized (this.mLock) {
            if (this.mHasInputFilter && (accessibilityInputFilter = this.mInputFilter) != null) {
                accessibilityInputFilter.onDoubleTapAndHold(i);
            }
        }
    }

    private void updateFocusAppearanceDataLocked(final AccessibilityUserState accessibilityUserState) {
        if (accessibilityUserState.mUserId != this.mCurrentUserId) {
            return;
        }
        if (this.mTraceManager.isA11yTracingEnabledForTypes(2L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.updateFocusAppearanceDataLocked", 2L, "userState=" + accessibilityUserState);
        }
        this.mMainHandler.post(new Runnable() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                AccessibilityManagerService.this.lambda$updateFocusAppearanceDataLocked$28(accessibilityUserState);
            }
        });
    }

    public /* synthetic */ void lambda$updateFocusAppearanceDataLocked$28(final AccessibilityUserState accessibilityUserState) {
        broadcastToClients(accessibilityUserState, FunctionalUtils.ignoreRemoteException(new FunctionalUtils.RemoteExceptionIgnoringConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda38
            public final void acceptOrThrow(Object obj) {
                AccessibilityManagerService.this.lambda$updateFocusAppearanceDataLocked$27(accessibilityUserState, (AccessibilityManagerService.Client) obj);
            }
        }));
    }

    public /* synthetic */ void lambda$updateFocusAppearanceDataLocked$27(AccessibilityUserState accessibilityUserState, Client client) throws RemoteException {
        if (this.mProxyManager.isProxyedDeviceId(client.mDeviceId)) {
            return;
        }
        client.mCallback.setFocusAppearance(accessibilityUserState.getFocusStrokeWidthLocked(), accessibilityUserState.getFocusColorLocked());
    }

    public AccessibilityTraceManager getTraceManager() {
        return this.mTraceManager;
    }

    public void scheduleBindInput() {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda20
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((AccessibilityManagerService) obj).bindInput();
            }
        }, this));
    }

    public void bindInput() {
        synchronized (this.mLock) {
            this.mInputBound = true;
            AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
            for (int size = currentUserStateLocked.mBoundServices.size() - 1; size >= 0; size--) {
                AccessibilityServiceConnection accessibilityServiceConnection = currentUserStateLocked.mBoundServices.get(size);
                if (accessibilityServiceConnection.requestImeApis()) {
                    accessibilityServiceConnection.bindInputLocked();
                }
            }
        }
    }

    public void scheduleUnbindInput() {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda23
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((AccessibilityManagerService) obj).unbindInput();
            }
        }, this));
    }

    public void unbindInput() {
        synchronized (this.mLock) {
            this.mInputBound = false;
            AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
            for (int size = currentUserStateLocked.mBoundServices.size() - 1; size >= 0; size--) {
                AccessibilityServiceConnection accessibilityServiceConnection = currentUserStateLocked.mBoundServices.get(size);
                if (accessibilityServiceConnection.requestImeApis()) {
                    accessibilityServiceConnection.unbindInputLocked();
                }
            }
        }
    }

    public void scheduleStartInput(IRemoteAccessibilityInputConnection iRemoteAccessibilityInputConnection, EditorInfo editorInfo, boolean z) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new QuadConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda45
            public final void accept(Object obj, Object obj2, Object obj3, Object obj4) {
                ((AccessibilityManagerService) obj).startInput((IRemoteAccessibilityInputConnection) obj2, (EditorInfo) obj3, ((Boolean) obj4).booleanValue());
            }
        }, this, iRemoteAccessibilityInputConnection, editorInfo, Boolean.valueOf(z)));
    }

    public void startInput(IRemoteAccessibilityInputConnection iRemoteAccessibilityInputConnection, EditorInfo editorInfo, boolean z) {
        synchronized (this.mLock) {
            this.mRemoteInputConnection = iRemoteAccessibilityInputConnection;
            this.mEditorInfo = editorInfo;
            this.mRestarting = z;
            AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
            for (int size = currentUserStateLocked.mBoundServices.size() - 1; size >= 0; size--) {
                AccessibilityServiceConnection accessibilityServiceConnection = currentUserStateLocked.mBoundServices.get(size);
                if (accessibilityServiceConnection.requestImeApis()) {
                    accessibilityServiceConnection.startInputLocked(iRemoteAccessibilityInputConnection, editorInfo, z);
                }
            }
        }
    }

    public void scheduleCreateImeSession(ArraySet<Integer> arraySet) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((AccessibilityManagerService) obj).createImeSession((ArraySet) obj2);
            }
        }, this, arraySet));
    }

    public void createImeSession(ArraySet<Integer> arraySet) {
        synchronized (this.mLock) {
            this.mInputSessionRequested = true;
            AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
            for (int size = currentUserStateLocked.mBoundServices.size() - 1; size >= 0; size--) {
                AccessibilityServiceConnection accessibilityServiceConnection = currentUserStateLocked.mBoundServices.get(size);
                if (!arraySet.contains(Integer.valueOf(accessibilityServiceConnection.mId)) && accessibilityServiceConnection.requestImeApis()) {
                    accessibilityServiceConnection.createImeSessionLocked();
                }
            }
        }
    }

    public void scheduleSetImeSessionEnabled(SparseArray<IAccessibilityInputMethodSession> sparseArray, boolean z) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda51
            public final void accept(Object obj, Object obj2, Object obj3) {
                ((AccessibilityManagerService) obj).setImeSessionEnabled((SparseArray) obj2, ((Boolean) obj3).booleanValue());
            }
        }, this, sparseArray, Boolean.valueOf(z)));
    }

    public void setImeSessionEnabled(SparseArray<IAccessibilityInputMethodSession> sparseArray, boolean z) {
        synchronized (this.mLock) {
            AccessibilityUserState currentUserStateLocked = getCurrentUserStateLocked();
            for (int size = currentUserStateLocked.mBoundServices.size() - 1; size >= 0; size--) {
                AccessibilityServiceConnection accessibilityServiceConnection = currentUserStateLocked.mBoundServices.get(size);
                if (sparseArray.contains(accessibilityServiceConnection.mId) && accessibilityServiceConnection.requestImeApis()) {
                    accessibilityServiceConnection.setImeSessionEnabledLocked(sparseArray.get(accessibilityServiceConnection.mId), z);
                }
            }
        }
    }

    public void injectInputEventToInputFilter(InputEvent inputEvent) {
        AccessibilityInputFilter accessibilityInputFilter;
        this.mSecurityPolicy.enforceCallingPermission("android.permission.INJECT_EVENTS", "injectInputEventToInputFilter");
        synchronized (this.mLock) {
            long uptimeMillis = SystemClock.uptimeMillis() + 1000;
            while (!this.mInputFilterInstalled && SystemClock.uptimeMillis() < uptimeMillis) {
                try {
                    this.mLock.wait(uptimeMillis - SystemClock.uptimeMillis());
                } catch (InterruptedException unused) {
                }
            }
        }
        if (this.mInputFilterInstalled && (accessibilityInputFilter = this.mInputFilter) != null) {
            accessibilityInputFilter.onInputEvent(inputEvent, 1090519040);
        } else {
            Slog.w(LOG_TAG, "Cannot injectInputEventToInputFilter because the AccessibilityInputFilter is not installed.");
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class SendWindowStateChangedEventRunnable implements Runnable {
        private final AccessibilityEvent mPendingEvent;
        private final int mWindowId;

        SendWindowStateChangedEventRunnable(AccessibilityEvent accessibilityEvent) {
            this.mPendingEvent = accessibilityEvent;
            this.mWindowId = accessibilityEvent.getWindowId();
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (AccessibilityManagerService.this.mLock) {
                Slog.w(AccessibilityManagerService.LOG_TAG, " wait for adding window timeout: " + this.mWindowId);
                sendPendingEventLocked();
            }
        }

        public void sendPendingEventLocked() {
            AccessibilityManagerService.this.mSendWindowStateChangedEventRunnables.remove(this);
            AccessibilityManagerService.this.dispatchAccessibilityEventLocked(this.mPendingEvent);
        }

        public int getWindowId() {
            return this.mWindowId;
        }
    }

    void sendPendingWindowStateChangedEventsForAvailableWindowLocked(int i) {
        for (int size = this.mSendWindowStateChangedEventRunnables.size() - 1; size >= 0; size--) {
            SendWindowStateChangedEventRunnable sendWindowStateChangedEventRunnable = this.mSendWindowStateChangedEventRunnables.get(size);
            if (sendWindowStateChangedEventRunnable.getWindowId() == i) {
                this.mMainHandler.removeCallbacks(sendWindowStateChangedEventRunnable);
                sendWindowStateChangedEventRunnable.sendPendingEventLocked();
            }
        }
    }

    private boolean postponeWindowStateEvent(AccessibilityEvent accessibilityEvent) {
        synchronized (this.mLock) {
            if (this.mA11yWindowManager.findWindowInfoByIdLocked(this.mA11yWindowManager.resolveParentWindowIdLocked(accessibilityEvent.getWindowId())) != null) {
                return false;
            }
            SendWindowStateChangedEventRunnable sendWindowStateChangedEventRunnable = new SendWindowStateChangedEventRunnable(new AccessibilityEvent(accessibilityEvent));
            this.mMainHandler.postDelayed(sendWindowStateChangedEventRunnable, 500L);
            this.mSendWindowStateChangedEventRunnables.add(sendWindowStateChangedEventRunnable);
            return true;
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void attachAccessibilityOverlayToDisplay(int i, SurfaceControl surfaceControl) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda7
            public final void accept(Object obj, Object obj2, Object obj3) {
                ((AccessibilityManagerService) obj).attachAccessibilityOverlayToDisplayInternal(((Integer) obj2).intValue(), (SurfaceControl) obj3);
            }
        }, this, Integer.valueOf(i), surfaceControl));
    }

    public void attachAccessibilityOverlayToDisplayInternal(int i, SurfaceControl surfaceControl) {
        if (!this.mA11yOverlayLayers.contains(i)) {
            this.mA11yOverlayLayers.put(i, this.mWindowManagerService.getA11yOverlayLayer(i));
        }
        SurfaceControl surfaceControl2 = this.mA11yOverlayLayers.get(i);
        if (surfaceControl2 == null) {
            Slog.e(LOG_TAG, "Unable to get accessibility overlay SurfaceControl.");
            this.mA11yOverlayLayers.remove(i);
        } else {
            SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
            transaction.reparent(surfaceControl, surfaceControl2).setTrustedOverlay(surfaceControl, true).apply();
            transaction.close();
        }
    }
}
