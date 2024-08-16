package com.android.server.accessibility;

import android.accessibilityservice.AccessibilityGestureEvent;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.AccessibilityTrace;
import android.accessibilityservice.IAccessibilityServiceClient;
import android.accessibilityservice.IAccessibilityServiceConnection;
import android.accessibilityservice.MagnificationConfig;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ParceledListSlice;
import android.graphics.ParcelableColorSpace;
import android.graphics.Region;
import android.hardware.HardwareBuffer;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManagerInternal;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.Trace;
import android.provider.Settings;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MagnificationSpec;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import android.view.inputmethod.EditorInfo;
import android.window.ScreenCapture;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.compat.IPlatformCompat;
import com.android.internal.inputmethod.IAccessibilityInputMethodSession;
import com.android.internal.inputmethod.IAccessibilityInputMethodSessionCallback;
import com.android.internal.inputmethod.IRemoteAccessibilityInputConnection;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.LocalServices;
import com.android.server.accessibility.AccessibilityWindowManager;
import com.android.server.accessibility.FingerprintGestureDispatcher;
import com.android.server.accessibility.KeyEventDispatcher;
import com.android.server.accessibility.magnification.MagnificationProcessor;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.wm.WindowManagerInternal;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class AbstractAccessibilityServiceConnection extends IAccessibilityServiceConnection.Stub implements ServiceConnection, IBinder.DeathRecipient, KeyEventDispatcher.KeyEventFilter, FingerprintGestureDispatcher.FingerprintGestureClient {
    private static final boolean DEBUG = false;
    public static final int DISPLAY_TYPE_DEFAULT = 1;
    public static final int DISPLAY_TYPE_PROXY = 2;
    private static final String LOG_TAG = "AbstractAccessibilityServiceConnection";
    protected static final String TAKE_SCREENSHOT = "takeScreenshot";
    private static final String TRACE_SVC_CLIENT = "AbstractAccessibilityServiceConnection.IAccessibilityServiceClient";
    private static final String TRACE_SVC_CONN = "AbstractAccessibilityServiceConnection.IAccessibilityServiceConnection";
    private static final String TRACE_WM = "WindowManagerInternal";
    private static final int WAIT_WINDOWS_TIMEOUT_MILLIS = 5000;
    final AccessibilityWindowManager mA11yWindowManager;
    protected final AccessibilityServiceInfo mAccessibilityServiceInfo;
    protected String mAttributionTag;
    boolean mCaptureFingerprintGestures;
    final ComponentName mComponentName;
    protected final Context mContext;
    private final DisplayManager mDisplayManager;
    public Handler mEventDispatchHandler;
    int mEventTypes;
    int mFeedbackType;
    int mFetchFlags;
    int mGenericMotionEventSources;
    final int mId;
    public final InvocationHandler mInvocationHandler;
    boolean mIsDefault;
    boolean mLastAccessibilityButtonCallbackState;
    protected final Object mLock;
    private final Handler mMainHandler;
    long mNotificationTimeout;
    private final PowerManager mPowerManager;
    boolean mReceivedAccessibilityButtonCallbackSinceBind;
    boolean mRequestAccessibilityButton;
    boolean mRequestFilterKeyEvents;
    boolean mRequestImeApis;
    private boolean mRequestMultiFingerGestures;
    private long mRequestTakeScreenshotTimestampMs;
    boolean mRequestTouchExplorationMode;
    private boolean mRequestTwoFingerPassthrough;
    boolean mRetrieveInteractiveWindows;
    protected final AccessibilitySecurityPolicy mSecurityPolicy;
    private boolean mSendMotionEvents;
    IBinder mService;
    private boolean mServiceHandlesDoubleTap;
    IAccessibilityServiceClient mServiceInterface;
    private final SystemActionPerformer mSystemActionPerformer;
    protected final SystemSupport mSystemSupport;
    protected final AccessibilityTrace mTrace;
    protected final WindowManagerInternal mWindowManagerService;
    protected int mDisplayTypes = 1;
    Set<String> mPackageNames = new HashSet();
    private SparseArray<Boolean> mServiceDetectsGestures = new SparseArray<>(0);
    final SparseArray<AccessibilityEvent> mPendingEvents = new SparseArray<>();
    boolean mUsesAccessibilityCache = false;
    final SparseArray<IBinder> mOverlayWindowTokens = new SparseArray<>();
    private SparseArray<Long> mRequestTakeScreenshotOfWindowTimestampMs = new SparseArray<>();
    private final IPlatformCompat mIPlatformCompat = IPlatformCompat.Stub.asInterface(ServiceManager.getService("platform_compat"));

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface DisplayTypes {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface SystemSupport {
        void attachAccessibilityOverlayToDisplay(int i, SurfaceControl surfaceControl);

        int getCurrentUserIdLocked();

        FingerprintGestureDispatcher getFingerprintGestureDispatcher();

        KeyEventDispatcher getKeyEventDispatcher();

        MagnificationProcessor getMagnificationProcessor();

        MotionEventInjector getMotionEventInjectorForDisplayLocked(int i);

        PendingIntent getPendingIntentActivity(Context context, int i, Intent intent, int i2);

        Pair<float[], MagnificationSpec> getWindowTransformationMatrixAndMagnificationSpec(int i);

        boolean isAccessibilityButtonShown();

        void onClientChangeLocked(boolean z);

        void onDoubleTap(int i);

        void onDoubleTapAndHold(int i);

        void onProxyChanged(int i);

        void persistComponentNamesToSettingLocked(String str, Set<ComponentName> set, int i);

        void requestDelegating(int i);

        void requestDragging(int i, int i2);

        void requestImeLocked(AbstractAccessibilityServiceConnection abstractAccessibilityServiceConnection);

        void requestTouchExploration(int i);

        void setGestureDetectionPassthroughRegion(int i, Region region);

        void setServiceDetectsGesturesEnabled(int i, boolean z);

        void setTouchExplorationPassthroughRegion(int i, Region region);

        void unbindImeLocked(AbstractAccessibilityServiceConnection abstractAccessibilityServiceConnection);
    }

    public List<AccessibilityServiceInfo> getInstalledAndEnabledServices() {
        return null;
    }

    protected abstract boolean hasRightsToCurrentUserLocked();

    public void setInstalledAndEnabledServices(List<AccessibilityServiceInfo> list) {
    }

    public AbstractAccessibilityServiceConnection(Context context, ComponentName componentName, AccessibilityServiceInfo accessibilityServiceInfo, int i, Handler handler, Object obj, AccessibilitySecurityPolicy accessibilitySecurityPolicy, SystemSupport systemSupport, AccessibilityTrace accessibilityTrace, WindowManagerInternal windowManagerInternal, SystemActionPerformer systemActionPerformer, AccessibilityWindowManager accessibilityWindowManager) {
        this.mContext = context;
        this.mWindowManagerService = windowManagerInternal;
        this.mId = i;
        this.mComponentName = componentName;
        this.mAccessibilityServiceInfo = accessibilityServiceInfo;
        this.mLock = obj;
        this.mSecurityPolicy = accessibilitySecurityPolicy;
        this.mSystemActionPerformer = systemActionPerformer;
        this.mSystemSupport = systemSupport;
        this.mTrace = accessibilityTrace;
        this.mMainHandler = handler;
        this.mInvocationHandler = new InvocationHandler(handler.getLooper());
        this.mA11yWindowManager = accessibilityWindowManager;
        this.mDisplayManager = (DisplayManager) context.getSystemService("display");
        this.mPowerManager = (PowerManager) context.getSystemService("power");
        this.mEventDispatchHandler = new Handler(handler.getLooper()) { // from class: com.android.server.accessibility.AbstractAccessibilityServiceConnection.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                AbstractAccessibilityServiceConnection.this.notifyAccessibilityEventInternal(message.what, (AccessibilityEvent) message.obj, message.arg1 != 0);
            }
        };
        setDynamicallyConfigurableProperties(accessibilityServiceInfo);
    }

    @Override // com.android.server.accessibility.KeyEventDispatcher.KeyEventFilter
    public boolean onKeyEvent(KeyEvent keyEvent, int i) {
        if (!this.mRequestFilterKeyEvents || this.mServiceInterface == null || (this.mAccessibilityServiceInfo.getCapabilities() & 8) == 0 || !this.mSecurityPolicy.checkAccessibilityAccess(this)) {
            return false;
        }
        try {
            if (svcClientTracingEnabled()) {
                logTraceSvcClient("onKeyEvent", keyEvent + ", " + i);
            }
            this.mServiceInterface.onKeyEvent(keyEvent, i);
            return true;
        } catch (RemoteException unused) {
            return false;
        }
    }

    public void setDynamicallyConfigurableProperties(AccessibilityServiceInfo accessibilityServiceInfo) {
        this.mEventTypes = accessibilityServiceInfo.eventTypes;
        this.mFeedbackType = accessibilityServiceInfo.feedbackType;
        String[] strArr = accessibilityServiceInfo.packageNames;
        this.mPackageNames.clear();
        if (strArr != null) {
            this.mPackageNames.addAll(Arrays.asList(strArr));
        }
        this.mNotificationTimeout = accessibilityServiceInfo.notificationTimeout;
        this.mIsDefault = (accessibilityServiceInfo.flags & 1) != 0;
        this.mGenericMotionEventSources = accessibilityServiceInfo.getMotionEventSources();
        if (supportsFlagForNotImportantViews(accessibilityServiceInfo)) {
            if ((accessibilityServiceInfo.flags & 2) != 0) {
                this.mFetchFlags |= 128;
            } else {
                this.mFetchFlags &= -129;
            }
        }
        if ((accessibilityServiceInfo.flags & 16) != 0) {
            this.mFetchFlags |= 256;
        } else {
            this.mFetchFlags &= -257;
        }
        if (this.mAccessibilityServiceInfo.isAccessibilityTool()) {
            this.mFetchFlags |= 512;
        } else {
            this.mFetchFlags &= -513;
        }
        int i = accessibilityServiceInfo.flags;
        this.mRequestTouchExplorationMode = (i & 4) != 0;
        this.mServiceHandlesDoubleTap = (i & 2048) != 0;
        this.mRequestMultiFingerGestures = (i & 4096) != 0;
        this.mRequestTwoFingerPassthrough = (i & 8192) != 0;
        this.mSendMotionEvents = (i & 16384) != 0;
        this.mRequestFilterKeyEvents = (i & 32) != 0;
        this.mRetrieveInteractiveWindows = (i & 64) != 0;
        this.mCaptureFingerprintGestures = (i & 512) != 0;
        this.mRequestAccessibilityButton = (i & 256) != 0;
        this.mRequestImeApis = (i & 32768) != 0;
    }

    protected boolean supportsFlagForNotImportantViews(AccessibilityServiceInfo accessibilityServiceInfo) {
        return accessibilityServiceInfo.getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion >= 16;
    }

    public boolean canReceiveEventsLocked() {
        return (this.mEventTypes == 0 || this.mService == null) ? false : true;
    }

    public void setOnKeyEventResult(boolean z, int i) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setOnKeyEventResult", "handled=" + z + ";sequence=" + i);
        }
        this.mSystemSupport.getKeyEventDispatcher().setOnKeyEventResult(this, z, i);
    }

    public AccessibilityServiceInfo getServiceInfo() {
        AccessibilityServiceInfo accessibilityServiceInfo;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getServiceInfo", "");
        }
        synchronized (this.mLock) {
            accessibilityServiceInfo = this.mAccessibilityServiceInfo;
        }
        return accessibilityServiceInfo;
    }

    public int getCapabilities() {
        return this.mAccessibilityServiceInfo.getCapabilities();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRelevantEventTypes() {
        return this.mEventTypes | (this.mUsesAccessibilityCache ? 4307005 : 32);
    }

    public void setServiceInfo(AccessibilityServiceInfo accessibilityServiceInfo) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setServiceInfo", "info=" + accessibilityServiceInfo);
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                boolean z = this.mRequestImeApis;
                AccessibilityServiceInfo accessibilityServiceInfo2 = this.mAccessibilityServiceInfo;
                if (accessibilityServiceInfo2 != null) {
                    accessibilityServiceInfo2.updateDynamicallyConfigurableProperties(this.mIPlatformCompat, accessibilityServiceInfo);
                    setDynamicallyConfigurableProperties(accessibilityServiceInfo2);
                } else {
                    setDynamicallyConfigurableProperties(accessibilityServiceInfo);
                }
                this.mSystemSupport.onClientChangeLocked(true);
                if (!z && this.mRequestImeApis) {
                    this.mSystemSupport.requestImeLocked(this);
                } else if (z && !this.mRequestImeApis) {
                    this.mSystemSupport.unbindImeLocked(this);
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setAttributionTag(String str) {
        this.mAttributionTag = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getAttributionTag() {
        return this.mAttributionTag;
    }

    public AccessibilityWindowInfo.WindowListSparseArray getWindows() {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getWindows", "");
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return null;
            }
            if (!this.mSecurityPolicy.canRetrieveWindowsLocked(this)) {
                return null;
            }
            if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                return null;
            }
            AccessibilityWindowInfo.WindowListSparseArray windowListSparseArray = new AccessibilityWindowInfo.WindowListSparseArray();
            ArrayList<Integer> displayListLocked = this.mA11yWindowManager.getDisplayListLocked(this.mDisplayTypes);
            int size = displayListLocked.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    int intValue = displayListLocked.get(i).intValue();
                    ensureWindowsAvailableTimedLocked(intValue);
                    List<AccessibilityWindowInfo> windowsByDisplayLocked = getWindowsByDisplayLocked(intValue);
                    if (windowsByDisplayLocked != null) {
                        windowListSparseArray.put(intValue, windowsByDisplayLocked);
                    }
                }
            }
            return windowListSparseArray;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setDisplayTypes(int i) {
        this.mDisplayTypes = i;
    }

    public AccessibilityWindowInfo getWindow(int i) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getWindow", "windowId=" + i);
        }
        synchronized (this.mLock) {
            ensureWindowsAvailableTimedLocked(i != -1 ? this.mA11yWindowManager.getDisplayIdByUserIdAndWindowIdLocked(this.mSystemSupport.getCurrentUserIdLocked(), i) : -1);
            if (!hasRightsToCurrentUserLocked()) {
                return null;
            }
            if (!this.mSecurityPolicy.canRetrieveWindowsLocked(this)) {
                return null;
            }
            if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                return null;
            }
            AccessibilityWindowInfo findA11yWindowInfoByIdLocked = this.mA11yWindowManager.findA11yWindowInfoByIdLocked(i);
            if (findA11yWindowInfoByIdLocked == null) {
                return null;
            }
            AccessibilityWindowInfo obtain = AccessibilityWindowInfo.obtain(findA11yWindowInfoByIdLocked);
            obtain.setConnectionId(this.mId);
            return obtain;
        }
    }

    public String[] findAccessibilityNodeInfosByViewId(int i, long j, String str, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws RemoteException {
        Region region;
        int i3;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("findAccessibilityNodeInfosByViewId", "accessibilityWindowId=" + i + ";accessibilityNodeId=" + j + ";viewIdResName=" + str + ";interactionId=" + i2 + ";callback=" + iAccessibilityInteractionConnectionCallback + ";interrogatingTid=" + j2);
        }
        Region obtain = Region.obtain();
        synchronized (this.mLock) {
            this.mUsesAccessibilityCache = true;
            if (!hasRightsToCurrentUserLocked()) {
                return null;
            }
            int resolveAccessibilityWindowIdLocked = resolveAccessibilityWindowIdLocked(i);
            if (!this.mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this.mSystemSupport.getCurrentUserIdLocked(), this, resolveAccessibilityWindowIdLocked)) {
                return null;
            }
            AccessibilityWindowManager.RemoteAccessibilityConnection connectionLocked = this.mA11yWindowManager.getConnectionLocked(this.mSystemSupport.getCurrentUserIdLocked(), resolveAccessibilityWindowIdLocked);
            if (connectionLocked == null) {
                return null;
            }
            if (this.mA11yWindowManager.computePartialInteractiveRegionForWindowLocked(resolveAccessibilityWindowIdLocked, obtain)) {
                region = obtain;
            } else {
                obtain.recycle();
                region = null;
            }
            Pair<float[], MagnificationSpec> windowTransformationMatrixAndMagnificationSpec = getWindowTransformationMatrixAndMagnificationSpec(resolveAccessibilityWindowIdLocked);
            float[] fArr = (float[]) windowTransformationMatrixAndMagnificationSpec.first;
            MagnificationSpec magnificationSpec = (MagnificationSpec) windowTransformationMatrixAndMagnificationSpec.second;
            if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                return null;
            }
            int callingPid = Binder.getCallingPid();
            Region region2 = region;
            IAccessibilityInteractionConnectionCallback replaceCallbackIfNeeded = replaceCallbackIfNeeded(iAccessibilityInteractionConnectionCallback, resolveAccessibilityWindowIdLocked, i2, callingPid, j2);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            if (intConnTracingEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append(j);
                sb.append(";");
                sb.append(str);
                sb.append(";");
                sb.append(region2);
                sb.append(";");
                sb.append(i2);
                sb.append(";");
                sb.append(replaceCallbackIfNeeded);
                sb.append(";");
                sb.append(this.mFetchFlags);
                sb.append(";");
                i3 = callingPid;
                sb.append(i3);
                sb.append(";");
                sb.append(j2);
                sb.append(";");
                sb.append(magnificationSpec);
                sb.append(";");
                sb.append(Arrays.toString(fArr));
                logTraceIntConn("findAccessibilityNodeInfosByViewId", sb.toString());
            } else {
                i3 = callingPid;
            }
            try {
                connectionLocked.getRemote().findAccessibilityNodeInfosByViewId(j, str, region2, i2, replaceCallbackIfNeeded, this.mFetchFlags, i3, j2, magnificationSpec, fArr);
                String[] computeValidReportedPackages = this.mSecurityPolicy.computeValidReportedPackages(connectionLocked.getPackageName(), connectionLocked.getUid());
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 != null && Binder.isProxy(connectionLocked.getRemote())) {
                    region2.recycle();
                }
                return computeValidReportedPackages;
            } catch (RemoteException unused) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 == null || !Binder.isProxy(connectionLocked.getRemote())) {
                    return null;
                }
                region2.recycle();
                return null;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 != null && Binder.isProxy(connectionLocked.getRemote())) {
                    region2.recycle();
                }
                throw th;
            }
        }
    }

    public String[] findAccessibilityNodeInfosByText(int i, long j, String str, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws RemoteException {
        Region region;
        int i3;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("findAccessibilityNodeInfosByText", "accessibilityWindowId=" + i + ";accessibilityNodeId=" + j + ";text=" + str + ";interactionId=" + i2 + ";callback=" + iAccessibilityInteractionConnectionCallback + ";interrogatingTid=" + j2);
        }
        Region obtain = Region.obtain();
        synchronized (this.mLock) {
            this.mUsesAccessibilityCache = true;
            if (!hasRightsToCurrentUserLocked()) {
                return null;
            }
            int resolveAccessibilityWindowIdLocked = resolveAccessibilityWindowIdLocked(i);
            if (!this.mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this.mSystemSupport.getCurrentUserIdLocked(), this, resolveAccessibilityWindowIdLocked)) {
                return null;
            }
            AccessibilityWindowManager.RemoteAccessibilityConnection connectionLocked = this.mA11yWindowManager.getConnectionLocked(this.mSystemSupport.getCurrentUserIdLocked(), resolveAccessibilityWindowIdLocked);
            if (connectionLocked == null) {
                return null;
            }
            if (this.mA11yWindowManager.computePartialInteractiveRegionForWindowLocked(resolveAccessibilityWindowIdLocked, obtain)) {
                region = obtain;
            } else {
                obtain.recycle();
                region = null;
            }
            Pair<float[], MagnificationSpec> windowTransformationMatrixAndMagnificationSpec = getWindowTransformationMatrixAndMagnificationSpec(resolveAccessibilityWindowIdLocked);
            float[] fArr = (float[]) windowTransformationMatrixAndMagnificationSpec.first;
            MagnificationSpec magnificationSpec = (MagnificationSpec) windowTransformationMatrixAndMagnificationSpec.second;
            if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                return null;
            }
            int callingPid = Binder.getCallingPid();
            Region region2 = region;
            IAccessibilityInteractionConnectionCallback replaceCallbackIfNeeded = replaceCallbackIfNeeded(iAccessibilityInteractionConnectionCallback, resolveAccessibilityWindowIdLocked, i2, callingPid, j2);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            if (intConnTracingEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append(j);
                sb.append(";");
                sb.append(str);
                sb.append(";");
                sb.append(region2);
                sb.append(";");
                sb.append(i2);
                sb.append(";");
                sb.append(replaceCallbackIfNeeded);
                sb.append(";");
                sb.append(this.mFetchFlags);
                sb.append(";");
                i3 = callingPid;
                sb.append(i3);
                sb.append(";");
                sb.append(j2);
                sb.append(";");
                sb.append(magnificationSpec);
                sb.append(";");
                sb.append(Arrays.toString(fArr));
                logTraceIntConn("findAccessibilityNodeInfosByText", sb.toString());
            } else {
                i3 = callingPid;
            }
            try {
                connectionLocked.getRemote().findAccessibilityNodeInfosByText(j, str, region2, i2, replaceCallbackIfNeeded, this.mFetchFlags, i3, j2, magnificationSpec, fArr);
                String[] computeValidReportedPackages = this.mSecurityPolicy.computeValidReportedPackages(connectionLocked.getPackageName(), connectionLocked.getUid());
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 != null && Binder.isProxy(connectionLocked.getRemote())) {
                    region2.recycle();
                }
                return computeValidReportedPackages;
            } catch (RemoteException unused) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 == null || !Binder.isProxy(connectionLocked.getRemote())) {
                    return null;
                }
                region2.recycle();
                return null;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 != null && Binder.isProxy(connectionLocked.getRemote())) {
                    region2.recycle();
                }
                throw th;
            }
        }
    }

    public String[] findAccessibilityNodeInfoByAccessibilityId(int i, long j, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, long j2, Bundle bundle) throws RemoteException {
        Region region;
        int i4;
        Region region2;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("findAccessibilityNodeInfoByAccessibilityId", "accessibilityWindowId=" + i + ";accessibilityNodeId=" + j + ";interactionId=" + i2 + ";callback=" + iAccessibilityInteractionConnectionCallback + ";flags=" + i3 + ";interrogatingTid=" + j2 + ";arguments=" + bundle);
        }
        Region obtain = Region.obtain();
        synchronized (this.mLock) {
            this.mUsesAccessibilityCache = true;
            if (!hasRightsToCurrentUserLocked()) {
                return null;
            }
            int resolveAccessibilityWindowIdLocked = resolveAccessibilityWindowIdLocked(i);
            if (!this.mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this.mSystemSupport.getCurrentUserIdLocked(), this, resolveAccessibilityWindowIdLocked)) {
                return null;
            }
            AccessibilityWindowManager.RemoteAccessibilityConnection connectionLocked = this.mA11yWindowManager.getConnectionLocked(this.mSystemSupport.getCurrentUserIdLocked(), resolveAccessibilityWindowIdLocked);
            if (connectionLocked == null) {
                return null;
            }
            if (this.mA11yWindowManager.computePartialInteractiveRegionForWindowLocked(resolveAccessibilityWindowIdLocked, obtain)) {
                region = obtain;
            } else {
                obtain.recycle();
                region = null;
            }
            Pair<float[], MagnificationSpec> windowTransformationMatrixAndMagnificationSpec = getWindowTransformationMatrixAndMagnificationSpec(resolveAccessibilityWindowIdLocked);
            float[] fArr = (float[]) windowTransformationMatrixAndMagnificationSpec.first;
            MagnificationSpec magnificationSpec = (MagnificationSpec) windowTransformationMatrixAndMagnificationSpec.second;
            if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                return null;
            }
            int callingPid = Binder.getCallingPid();
            Region region3 = region;
            IAccessibilityInteractionConnectionCallback replaceCallbackIfNeeded = replaceCallbackIfNeeded(iAccessibilityInteractionConnectionCallback, resolveAccessibilityWindowIdLocked, i2, callingPid, j2);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            if (intConnTracingEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append(j);
                sb.append(";");
                sb.append(region3);
                sb.append(";");
                sb.append(i2);
                sb.append(";");
                sb.append(replaceCallbackIfNeeded);
                sb.append(";");
                sb.append(this.mFetchFlags | i3);
                sb.append(";");
                i4 = callingPid;
                sb.append(i4);
                sb.append(";");
                sb.append(j2);
                sb.append(";");
                sb.append(magnificationSpec);
                sb.append(";");
                sb.append(Arrays.toString(fArr));
                sb.append(";");
                region2 = region3;
                sb.append(bundle);
                logTraceIntConn("findAccessibilityNodeInfoByAccessibilityId", sb.toString());
            } else {
                i4 = callingPid;
                region2 = region3;
            }
            try {
                connectionLocked.getRemote().findAccessibilityNodeInfoByAccessibilityId(j, region2, i2, replaceCallbackIfNeeded, i3 | this.mFetchFlags, i4, j2, magnificationSpec, fArr, bundle);
                String[] computeValidReportedPackages = this.mSecurityPolicy.computeValidReportedPackages(connectionLocked.getPackageName(), connectionLocked.getUid());
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 != null && Binder.isProxy(connectionLocked.getRemote())) {
                    region2.recycle();
                }
                return computeValidReportedPackages;
            } catch (RemoteException unused) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 == null || !Binder.isProxy(connectionLocked.getRemote())) {
                    return null;
                }
                region2.recycle();
                return null;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 != null && Binder.isProxy(connectionLocked.getRemote())) {
                    region2.recycle();
                }
                throw th;
            }
        }
    }

    public String[] findFocus(int i, long j, int i2, int i3, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws RemoteException {
        Region region;
        int i4;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("findFocus", "accessibilityWindowId=" + i + ";accessibilityNodeId=" + j + ";focusType=" + i2 + ";interactionId=" + i3 + ";callback=" + iAccessibilityInteractionConnectionCallback + ";interrogatingTid=" + j2);
        }
        Region obtain = Region.obtain();
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return null;
            }
            int resolveAccessibilityWindowIdForFindFocusLocked = resolveAccessibilityWindowIdForFindFocusLocked(i, i2);
            if (!this.mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this.mSystemSupport.getCurrentUserIdLocked(), this, resolveAccessibilityWindowIdForFindFocusLocked)) {
                return null;
            }
            AccessibilityWindowManager.RemoteAccessibilityConnection connectionLocked = this.mA11yWindowManager.getConnectionLocked(this.mSystemSupport.getCurrentUserIdLocked(), resolveAccessibilityWindowIdForFindFocusLocked);
            if (connectionLocked == null) {
                return null;
            }
            if (this.mA11yWindowManager.computePartialInteractiveRegionForWindowLocked(resolveAccessibilityWindowIdForFindFocusLocked, obtain)) {
                region = obtain;
            } else {
                obtain.recycle();
                region = null;
            }
            Pair<float[], MagnificationSpec> windowTransformationMatrixAndMagnificationSpec = getWindowTransformationMatrixAndMagnificationSpec(resolveAccessibilityWindowIdForFindFocusLocked);
            float[] fArr = (float[]) windowTransformationMatrixAndMagnificationSpec.first;
            MagnificationSpec magnificationSpec = (MagnificationSpec) windowTransformationMatrixAndMagnificationSpec.second;
            if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                return null;
            }
            int callingPid = Binder.getCallingPid();
            Region region2 = region;
            IAccessibilityInteractionConnectionCallback replaceCallbackIfNeeded = replaceCallbackIfNeeded(iAccessibilityInteractionConnectionCallback, resolveAccessibilityWindowIdForFindFocusLocked, i3, callingPid, j2);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            if (intConnTracingEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append(j);
                sb.append(";");
                sb.append(i2);
                sb.append(";");
                sb.append(region2);
                sb.append(";");
                sb.append(i3);
                sb.append(";");
                sb.append(replaceCallbackIfNeeded);
                sb.append(";");
                sb.append(this.mFetchFlags);
                sb.append(";");
                i4 = callingPid;
                sb.append(i4);
                sb.append(";");
                sb.append(j2);
                sb.append(";");
                sb.append(magnificationSpec);
                sb.append(";");
                sb.append(Arrays.toString(fArr));
                logTraceIntConn("findFocus", sb.toString());
            } else {
                i4 = callingPid;
            }
            try {
                connectionLocked.getRemote().findFocus(j, i2, region2, i3, replaceCallbackIfNeeded, this.mFetchFlags, i4, j2, magnificationSpec, fArr);
                String[] computeValidReportedPackages = this.mSecurityPolicy.computeValidReportedPackages(connectionLocked.getPackageName(), connectionLocked.getUid());
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 != null && Binder.isProxy(connectionLocked.getRemote())) {
                    region2.recycle();
                }
                return computeValidReportedPackages;
            } catch (RemoteException unused) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 == null || !Binder.isProxy(connectionLocked.getRemote())) {
                    return null;
                }
                region2.recycle();
                return null;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 != null && Binder.isProxy(connectionLocked.getRemote())) {
                    region2.recycle();
                }
                throw th;
            }
        }
    }

    public String[] focusSearch(int i, long j, int i2, int i3, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws RemoteException {
        Region region;
        int i4;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("focusSearch", "accessibilityWindowId=" + i + ";accessibilityNodeId=" + j + ";direction=" + i2 + ";interactionId=" + i3 + ";callback=" + iAccessibilityInteractionConnectionCallback + ";interrogatingTid=" + j2);
        }
        Region obtain = Region.obtain();
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return null;
            }
            int resolveAccessibilityWindowIdLocked = resolveAccessibilityWindowIdLocked(i);
            if (!this.mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this.mSystemSupport.getCurrentUserIdLocked(), this, resolveAccessibilityWindowIdLocked)) {
                return null;
            }
            AccessibilityWindowManager.RemoteAccessibilityConnection connectionLocked = this.mA11yWindowManager.getConnectionLocked(this.mSystemSupport.getCurrentUserIdLocked(), resolveAccessibilityWindowIdLocked);
            if (connectionLocked == null) {
                return null;
            }
            if (this.mA11yWindowManager.computePartialInteractiveRegionForWindowLocked(resolveAccessibilityWindowIdLocked, obtain)) {
                region = obtain;
            } else {
                obtain.recycle();
                region = null;
            }
            Pair<float[], MagnificationSpec> windowTransformationMatrixAndMagnificationSpec = getWindowTransformationMatrixAndMagnificationSpec(resolveAccessibilityWindowIdLocked);
            float[] fArr = (float[]) windowTransformationMatrixAndMagnificationSpec.first;
            MagnificationSpec magnificationSpec = (MagnificationSpec) windowTransformationMatrixAndMagnificationSpec.second;
            if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                return null;
            }
            int callingPid = Binder.getCallingPid();
            Region region2 = region;
            IAccessibilityInteractionConnectionCallback replaceCallbackIfNeeded = replaceCallbackIfNeeded(iAccessibilityInteractionConnectionCallback, resolveAccessibilityWindowIdLocked, i3, callingPid, j2);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            if (intConnTracingEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append(j);
                sb.append(";");
                sb.append(i2);
                sb.append(";");
                sb.append(region2);
                sb.append(";");
                sb.append(i3);
                sb.append(";");
                sb.append(replaceCallbackIfNeeded);
                sb.append(";");
                sb.append(this.mFetchFlags);
                sb.append(";");
                i4 = callingPid;
                sb.append(i4);
                sb.append(";");
                sb.append(j2);
                sb.append(";");
                sb.append(magnificationSpec);
                sb.append(";");
                sb.append(Arrays.toString(fArr));
                logTraceIntConn("focusSearch", sb.toString());
            } else {
                i4 = callingPid;
            }
            try {
                connectionLocked.getRemote().focusSearch(j, i2, region2, i3, replaceCallbackIfNeeded, this.mFetchFlags, i4, j2, magnificationSpec, fArr);
                String[] computeValidReportedPackages = this.mSecurityPolicy.computeValidReportedPackages(connectionLocked.getPackageName(), connectionLocked.getUid());
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 != null && Binder.isProxy(connectionLocked.getRemote())) {
                    region2.recycle();
                }
                return computeValidReportedPackages;
            } catch (RemoteException unused) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 == null || !Binder.isProxy(connectionLocked.getRemote())) {
                    return null;
                }
                region2.recycle();
                return null;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (region2 != null && Binder.isProxy(connectionLocked.getRemote())) {
                    region2.recycle();
                }
                throw th;
            }
        }
    }

    public void sendGesture(int i, ParceledListSlice parceledListSlice) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("sendGesture", "sequence=" + i + ";gestureSteps=" + parceledListSlice);
        }
    }

    public void dispatchGesture(int i, ParceledListSlice parceledListSlice, int i2) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("dispatchGesture", "sequence=" + i + ";gestureSteps=" + parceledListSlice + ";displayId=" + i2);
        }
    }

    public boolean performAccessibilityAction(int i, long j, int i2, Bundle bundle, int i3, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws RemoteException {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("performAccessibilityAction", "accessibilityWindowId=" + i + ";accessibilityNodeId=" + j + ";action=" + i2 + ";arguments=" + bundle + ";interactionId=" + i3 + ";callback=" + iAccessibilityInteractionConnectionCallback + ";interrogatingTid=" + j2);
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return false;
            }
            int resolveAccessibilityWindowIdLocked = resolveAccessibilityWindowIdLocked(i);
            if (!this.mSecurityPolicy.canGetAccessibilityNodeInfoLocked(this.mSystemSupport.getCurrentUserIdLocked(), this, resolveAccessibilityWindowIdLocked)) {
                return false;
            }
            if (this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                return performAccessibilityActionInternal(this.mSystemSupport.getCurrentUserIdLocked(), resolveAccessibilityWindowIdLocked, j, i2, bundle, i3, iAccessibilityInteractionConnectionCallback, this.mFetchFlags, j2);
            }
            return false;
        }
    }

    public boolean performGlobalAction(int i) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("performGlobalAction", "action=" + i);
        }
        synchronized (this.mLock) {
            if (hasRightsToCurrentUserLocked()) {
                return this.mSystemActionPerformer.performSystemAction(i);
            }
            return false;
        }
    }

    public List<AccessibilityNodeInfo.AccessibilityAction> getSystemActions() {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getSystemActions", "");
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return Collections.emptyList();
            }
            return this.mSystemActionPerformer.getSystemActions();
        }
    }

    public boolean isFingerprintGestureDetectionAvailable() {
        FingerprintGestureDispatcher fingerprintGestureDispatcher;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("isFingerprintGestureDetectionAvailable", "");
        }
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.fingerprint") && isCapturingFingerprintGestures() && (fingerprintGestureDispatcher = this.mSystemSupport.getFingerprintGestureDispatcher()) != null && fingerprintGestureDispatcher.isFingerprintGestureDetectionAvailable();
    }

    public MagnificationConfig getMagnificationConfig(int i) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getMagnificationConfig", "displayId=" + i);
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return null;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return this.mSystemSupport.getMagnificationProcessor().getMagnificationConfig(i);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public float getMagnificationScale(int i) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getMagnificationScale", "displayId=" + i);
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return 1.0f;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return this.mSystemSupport.getMagnificationProcessor().getScale(i);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public Region getMagnificationRegion(int i) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getMagnificationRegion", "displayId=" + i);
        }
        synchronized (this.mLock) {
            Region obtain = Region.obtain();
            if (!hasRightsToCurrentUserLocked()) {
                return obtain;
            }
            MagnificationProcessor magnificationProcessor = this.mSystemSupport.getMagnificationProcessor();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                magnificationProcessor.getFullscreenMagnificationRegion(i, obtain, this.mSecurityPolicy.canControlMagnification(this));
                return obtain;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public Region getCurrentMagnificationRegion(int i) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getCurrentMagnificationRegion", "displayId=" + i);
        }
        synchronized (this.mLock) {
            Region obtain = Region.obtain();
            if (!hasRightsToCurrentUserLocked()) {
                return obtain;
            }
            MagnificationProcessor magnificationProcessor = this.mSystemSupport.getMagnificationProcessor();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                magnificationProcessor.getCurrentMagnificationRegion(i, obtain, this.mSecurityPolicy.canControlMagnification(this));
                return obtain;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public float getMagnificationCenterX(int i) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getMagnificationCenterX", "displayId=" + i);
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return 0.0f;
            }
            MagnificationProcessor magnificationProcessor = this.mSystemSupport.getMagnificationProcessor();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return magnificationProcessor.getCenterX(i, this.mSecurityPolicy.canControlMagnification(this));
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public float getMagnificationCenterY(int i) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getMagnificationCenterY", "displayId=" + i);
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return 0.0f;
            }
            MagnificationProcessor magnificationProcessor = this.mSystemSupport.getMagnificationProcessor();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return magnificationProcessor.getCenterY(i, this.mSecurityPolicy.canControlMagnification(this));
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0050, code lost:
    
        if (r3.isMagnifying(r4) == false) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean resetMagnification(int i, boolean z) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("resetMagnification", "displayId=" + i + ";animate=" + z);
        }
        synchronized (this.mLock) {
            boolean z2 = false;
            if (!hasRightsToCurrentUserLocked()) {
                return false;
            }
            if (!this.mSecurityPolicy.canControlMagnification(this)) {
                return false;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                MagnificationProcessor magnificationProcessor = this.mSystemSupport.getMagnificationProcessor();
                if (!magnificationProcessor.resetFullscreenMagnification(i, z)) {
                }
                z2 = true;
                return z2;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0050, code lost:
    
        if (r3.isMagnifying(r4) == false) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean resetCurrentMagnification(int i, boolean z) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("resetCurrentMagnification", "displayId=" + i + ";animate=" + z);
        }
        synchronized (this.mLock) {
            boolean z2 = false;
            if (!hasRightsToCurrentUserLocked()) {
                return false;
            }
            if (!this.mSecurityPolicy.canControlMagnification(this)) {
                return false;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                MagnificationProcessor magnificationProcessor = this.mSystemSupport.getMagnificationProcessor();
                if (!magnificationProcessor.resetCurrentMagnification(i, z)) {
                }
                z2 = true;
                return z2;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public boolean setMagnificationConfig(int i, MagnificationConfig magnificationConfig, boolean z) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setMagnificationSpec", "displayId=" + i + ", config=" + magnificationConfig.toString());
        }
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                return false;
            }
            if (!this.mSecurityPolicy.canControlMagnification(this)) {
                return false;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return this.mSystemSupport.getMagnificationProcessor().setMagnificationConfig(i, magnificationConfig, z, this.mId);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public void setMagnificationCallbackEnabled(int i, boolean z) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setMagnificationCallbackEnabled", "displayId=" + i + ";enabled=" + z);
        }
        this.mInvocationHandler.setMagnificationCallbackEnabled(i, z);
    }

    public boolean isMagnificationCallbackEnabled(int i) {
        return this.mInvocationHandler.isMagnificationCallbackEnabled(i);
    }

    public void setSoftKeyboardCallbackEnabled(boolean z) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setSoftKeyboardCallbackEnabled", "enabled=" + z);
        }
        this.mInvocationHandler.setSoftKeyboardCallbackEnabled(z);
    }

    public void takeScreenshotOfWindow(int i, int i2, ScreenCapture.ScreenCaptureListener screenCaptureListener, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback) throws RemoteException {
        long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - this.mRequestTakeScreenshotOfWindowTimestampMs.get(i, 0L).longValue() <= 333) {
            iAccessibilityInteractionConnectionCallback.sendTakeScreenshotOfWindowError(3, i2);
            return;
        }
        this.mRequestTakeScreenshotOfWindowTimestampMs.put(i, Long.valueOf(uptimeMillis));
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                iAccessibilityInteractionConnectionCallback.sendTakeScreenshotOfWindowError(1, i2);
                return;
            }
            if (!this.mSecurityPolicy.canTakeScreenshotLocked(this)) {
                iAccessibilityInteractionConnectionCallback.sendTakeScreenshotOfWindowError(2, i2);
                return;
            }
            if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                iAccessibilityInteractionConnectionCallback.sendTakeScreenshotOfWindowError(2, i2);
                return;
            }
            AccessibilityWindowManager.RemoteAccessibilityConnection connectionLocked = this.mA11yWindowManager.getConnectionLocked(this.mSystemSupport.getCurrentUserIdLocked(), resolveAccessibilityWindowIdLocked(i));
            if (connectionLocked == null) {
                iAccessibilityInteractionConnectionCallback.sendTakeScreenshotOfWindowError(5, i2);
            } else {
                connectionLocked.getRemote().takeScreenshotOfWindow(i2, screenCaptureListener, iAccessibilityInteractionConnectionCallback);
            }
        }
    }

    public void takeScreenshot(final int i, final RemoteCallback remoteCallback) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn(TAKE_SCREENSHOT, "displayId=" + i + ";callback=" + remoteCallback);
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        long j = this.mRequestTakeScreenshotTimestampMs;
        if (j != 0 && uptimeMillis - j <= 333) {
            sendScreenshotFailure(3, remoteCallback);
            return;
        }
        this.mRequestTakeScreenshotTimestampMs = uptimeMillis;
        synchronized (this.mLock) {
            if (!hasRightsToCurrentUserLocked()) {
                sendScreenshotFailure(1, remoteCallback);
                return;
            }
            if (!this.mSecurityPolicy.canTakeScreenshotLocked(this)) {
                throw new SecurityException("Services don't have the capability of taking the screenshot.");
            }
            if (!this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                sendScreenshotFailure(2, remoteCallback);
                return;
            }
            Display display = ((DisplayManager) this.mContext.getSystemService("display")).getDisplay(i);
            if (display == null || (display.getType() == 5 && (display.getFlags() & 4) != 0)) {
                sendScreenshotFailure(4, remoteCallback);
                return;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mMainHandler.post(PooledLambda.obtainRunnable(new Consumer() { // from class: com.android.server.accessibility.AbstractAccessibilityServiceConnection$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        AbstractAccessibilityServiceConnection.this.lambda$takeScreenshot$0(i, remoteCallback, obj);
                    }
                }, (Object) null).recycleOnUse());
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$takeScreenshot$0(int i, RemoteCallback remoteCallback, Object obj) {
        ScreenCapture.ScreenshotHardwareBuffer userScreenshot = ((DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class)).userScreenshot(i);
        if (userScreenshot != null) {
            sendScreenshotSuccess(userScreenshot, remoteCallback);
        } else {
            sendScreenshotFailure(4, remoteCallback);
        }
    }

    private void sendScreenshotSuccess(ScreenCapture.ScreenshotHardwareBuffer screenshotHardwareBuffer, RemoteCallback remoteCallback) {
        HardwareBuffer hardwareBuffer = screenshotHardwareBuffer.getHardwareBuffer();
        ParcelableColorSpace parcelableColorSpace = new ParcelableColorSpace(screenshotHardwareBuffer.getColorSpace());
        Bundle bundle = new Bundle();
        bundle.putInt("screenshot_status", 0);
        bundle.putParcelable("screenshot_hardwareBuffer", hardwareBuffer);
        bundle.putParcelable("screenshot_colorSpace", parcelableColorSpace);
        bundle.putLong("screenshot_timestamp", SystemClock.uptimeMillis());
        remoteCallback.sendResult(bundle);
        hardwareBuffer.close();
    }

    private void sendScreenshotFailure(final int i, final RemoteCallback remoteCallback) {
        this.mMainHandler.post(PooledLambda.obtainRunnable(new Consumer() { // from class: com.android.server.accessibility.AbstractAccessibilityServiceConnection$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AbstractAccessibilityServiceConnection.lambda$sendScreenshotFailure$1(i, remoteCallback, obj);
            }
        }, (Object) null).recycleOnUse());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendScreenshotFailure$1(int i, RemoteCallback remoteCallback, Object obj) {
        Bundle bundle = new Bundle();
        bundle.putInt("screenshot_status", i);
        remoteCallback.sendResult(bundle);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, LOG_TAG, printWriter)) {
            synchronized (this.mLock) {
                printWriter.append((CharSequence) ("Service[label=" + ((Object) this.mAccessibilityServiceInfo.getResolveInfo().loadLabel(this.mContext.getPackageManager()))));
                printWriter.append((CharSequence) (", feedbackType" + AccessibilityServiceInfo.feedbackTypeToString(this.mFeedbackType)));
                printWriter.append((CharSequence) (", capabilities=" + this.mAccessibilityServiceInfo.getCapabilities()));
                printWriter.append((CharSequence) (", eventTypes=" + AccessibilityEvent.eventTypeToString(this.mEventTypes)));
                printWriter.append((CharSequence) (", notificationTimeout=" + this.mNotificationTimeout));
                printWriter.append((CharSequence) (", requestA11yBtn=" + this.mRequestAccessibilityButton));
                printWriter.append("]");
            }
        }
    }

    public void onAdded() {
        for (Display display : this.mDisplayManager.getDisplays()) {
            onDisplayAdded(display.getDisplayId());
        }
    }

    public void onDisplayAdded(int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Binder binder = new Binder();
            if (wmTracingEnabled()) {
                logTraceWM("addWindowToken", binder + ";TYPE_ACCESSIBILITY_OVERLAY;" + i + ";null");
            }
            this.mWindowManagerService.addWindowToken(binder, 2032, i, (Bundle) null);
            synchronized (this.mLock) {
                this.mOverlayWindowTokens.put(i, binder);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void onRemoved() {
        for (Display display : this.mDisplayManager.getDisplays()) {
            onDisplayRemoved(display.getDisplayId());
        }
    }

    public void onDisplayRemoved(int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        if (wmTracingEnabled()) {
            logTraceWM("addWindowToken", this.mOverlayWindowTokens.get(i) + ";true;" + i);
        }
        try {
            this.mWindowManagerService.removeWindowToken(this.mOverlayWindowTokens.get(i), true, i);
            synchronized (this.mLock) {
                this.mOverlayWindowTokens.remove(i);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public IBinder getOverlayWindowToken(int i) {
        IBinder iBinder;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getOverlayWindowToken", "displayId=" + i);
        }
        synchronized (this.mLock) {
            iBinder = this.mOverlayWindowTokens.get(i);
        }
        return iBinder;
    }

    public int getWindowIdForLeashToken(IBinder iBinder) {
        int windowIdLocked;
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("getWindowIdForLeashToken", "token=" + iBinder);
        }
        synchronized (this.mLock) {
            windowIdLocked = this.mA11yWindowManager.getWindowIdLocked(iBinder);
        }
        return windowIdLocked;
    }

    public void resetLocked() {
        this.mSystemSupport.getKeyEventDispatcher().flush(this);
        try {
            if (this.mServiceInterface != null) {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("init", "null, " + this.mId + ", null");
                }
                this.mServiceInterface.init((IAccessibilityServiceConnection) null, this.mId, (IBinder) null);
            }
        } catch (RemoteException unused) {
        }
        IBinder iBinder = this.mService;
        if (iBinder != null) {
            try {
                iBinder.unlinkToDeath(this, 0);
            } catch (NoSuchElementException unused2) {
                Slog.e(LOG_TAG, "Failed unregistering death link");
            }
            this.mService = null;
        }
        this.mServiceInterface = null;
        this.mReceivedAccessibilityButtonCallbackSinceBind = false;
    }

    public boolean isConnectedLocked() {
        return this.mService != null;
    }

    public void notifyAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Message obtainMessage;
        synchronized (this.mLock) {
            int eventType = accessibilityEvent.getEventType();
            boolean wantsEventLocked = wantsEventLocked(accessibilityEvent);
            int i = 1;
            boolean z = this.mUsesAccessibilityCache && (4307005 & eventType) != 0;
            if (wantsEventLocked || z) {
                if (this.mSecurityPolicy.checkAccessibilityAccess(this)) {
                    AccessibilityEvent obtain = AccessibilityEvent.obtain(accessibilityEvent);
                    if (this.mNotificationTimeout > 0 && eventType != 2048) {
                        AccessibilityEvent accessibilityEvent2 = this.mPendingEvents.get(eventType);
                        this.mPendingEvents.put(eventType, obtain);
                        if (accessibilityEvent2 != null) {
                            this.mEventDispatchHandler.removeMessages(eventType);
                            accessibilityEvent2.recycle();
                        }
                        obtainMessage = this.mEventDispatchHandler.obtainMessage(eventType);
                    } else {
                        obtainMessage = this.mEventDispatchHandler.obtainMessage(eventType, obtain);
                    }
                    if (!wantsEventLocked) {
                        i = 0;
                    }
                    obtainMessage.arg1 = i;
                    this.mEventDispatchHandler.sendMessageDelayed(obtainMessage, this.mNotificationTimeout);
                }
            }
        }
    }

    private boolean wantsEventLocked(AccessibilityEvent accessibilityEvent) {
        if (!canReceiveEventsLocked()) {
            return false;
        }
        boolean z = (this.mFetchFlags & 128) != 0;
        if (accessibilityEvent.getWindowId() != -1 && !accessibilityEvent.isImportantForAccessibility() && !z) {
            return false;
        }
        if (accessibilityEvent.isAccessibilityDataSensitive() && (this.mFetchFlags & 512) == 0) {
            return false;
        }
        int eventType = accessibilityEvent.getEventType();
        if ((this.mEventTypes & eventType) != eventType) {
            return false;
        }
        Set<String> set = this.mPackageNames;
        return set.isEmpty() || set.contains(accessibilityEvent.getPackageName() != null ? accessibilityEvent.getPackageName().toString() : null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAccessibilityEventInternal(int i, AccessibilityEvent accessibilityEvent, boolean z) {
        synchronized (this.mLock) {
            IAccessibilityServiceClient iAccessibilityServiceClient = this.mServiceInterface;
            if (iAccessibilityServiceClient == null) {
                return;
            }
            if (accessibilityEvent == null) {
                accessibilityEvent = this.mPendingEvents.get(i);
                if (accessibilityEvent == null) {
                    return;
                } else {
                    this.mPendingEvents.remove(i);
                }
            }
            if (this.mSecurityPolicy.canRetrieveWindowContentLocked(this)) {
                accessibilityEvent.setConnectionId(this.mId);
            } else {
                accessibilityEvent.setSource(null);
            }
            accessibilityEvent.setSealed(true);
            try {
                try {
                    if (svcClientTracingEnabled()) {
                        logTraceSvcClient("onAccessibilityEvent", accessibilityEvent + ";" + z);
                    }
                    iAccessibilityServiceClient.onAccessibilityEvent(accessibilityEvent, z);
                } catch (RemoteException e) {
                    Slog.e(LOG_TAG, "Error during sending " + accessibilityEvent + " to " + iAccessibilityServiceClient, e);
                }
            } finally {
                accessibilityEvent.recycle();
            }
        }
    }

    public void notifyGesture(AccessibilityGestureEvent accessibilityGestureEvent) {
        this.mInvocationHandler.obtainMessage(1, accessibilityGestureEvent).sendToTarget();
    }

    public void notifySystemActionsChangedLocked() {
        this.mInvocationHandler.sendEmptyMessage(9);
    }

    public void notifyClearAccessibilityNodeInfoCache() {
        this.mInvocationHandler.sendEmptyMessage(2);
    }

    public void notifyMagnificationChangedLocked(int i, Region region, MagnificationConfig magnificationConfig) {
        this.mInvocationHandler.notifyMagnificationChangedLocked(i, region, magnificationConfig);
    }

    public void notifySoftKeyboardShowModeChangedLocked(int i) {
        this.mInvocationHandler.notifySoftKeyboardShowModeChangedLocked(i);
    }

    public void notifyAccessibilityButtonClickedLocked(int i) {
        this.mInvocationHandler.notifyAccessibilityButtonClickedLocked(i);
    }

    public void notifyAccessibilityButtonAvailabilityChangedLocked(boolean z) {
        this.mInvocationHandler.notifyAccessibilityButtonAvailabilityChangedLocked(z);
    }

    public void createImeSessionLocked() {
        this.mInvocationHandler.createImeSessionLocked();
    }

    public void setImeSessionEnabledLocked(IAccessibilityInputMethodSession iAccessibilityInputMethodSession, boolean z) {
        this.mInvocationHandler.setImeSessionEnabledLocked(iAccessibilityInputMethodSession, z);
    }

    public void bindInputLocked() {
        this.mInvocationHandler.bindInputLocked();
    }

    public void unbindInputLocked() {
        this.mInvocationHandler.unbindInputLocked();
    }

    public void startInputLocked(IRemoteAccessibilityInputConnection iRemoteAccessibilityInputConnection, EditorInfo editorInfo, boolean z) {
        this.mInvocationHandler.startInputLocked(iRemoteAccessibilityInputConnection, editorInfo, z);
    }

    private Pair<float[], MagnificationSpec> getWindowTransformationMatrixAndMagnificationSpec(int i) {
        return this.mSystemSupport.getWindowTransformationMatrixAndMagnificationSpec(i);
    }

    public boolean wantsGenericMotionEvent(MotionEvent motionEvent) {
        return (this.mGenericMotionEventSources & (motionEvent.getSource() & (-256))) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyMagnificationChangedInternal(int i, Region region, MagnificationConfig magnificationConfig) {
        IAccessibilityServiceClient serviceInterfaceSafely = getServiceInterfaceSafely();
        if (serviceInterfaceSafely != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("onMagnificationChanged", i + ", " + region + ", " + magnificationConfig.toString());
                }
                serviceInterfaceSafely.onMagnificationChanged(i, region, magnificationConfig);
            } catch (RemoteException e) {
                Slog.e(LOG_TAG, "Error sending magnification changes to " + this.mService, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySoftKeyboardShowModeChangedInternal(int i) {
        IAccessibilityServiceClient serviceInterfaceSafely = getServiceInterfaceSafely();
        if (serviceInterfaceSafely != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("onSoftKeyboardShowModeChanged", String.valueOf(i));
                }
                serviceInterfaceSafely.onSoftKeyboardShowModeChanged(i);
            } catch (RemoteException e) {
                Slog.e(LOG_TAG, "Error sending soft keyboard show mode changes to " + this.mService, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAccessibilityButtonClickedInternal(int i) {
        IAccessibilityServiceClient serviceInterfaceSafely = getServiceInterfaceSafely();
        if (serviceInterfaceSafely != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("onAccessibilityButtonClicked", String.valueOf(i));
                }
                serviceInterfaceSafely.onAccessibilityButtonClicked(i);
            } catch (RemoteException e) {
                Slog.e(LOG_TAG, "Error sending accessibility button click to " + this.mService, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAccessibilityButtonAvailabilityChangedInternal(boolean z) {
        if (this.mReceivedAccessibilityButtonCallbackSinceBind && this.mLastAccessibilityButtonCallbackState == z) {
            return;
        }
        this.mReceivedAccessibilityButtonCallbackSinceBind = true;
        this.mLastAccessibilityButtonCallbackState = z;
        IAccessibilityServiceClient serviceInterfaceSafely = getServiceInterfaceSafely();
        if (serviceInterfaceSafely != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("onAccessibilityButtonAvailabilityChanged", String.valueOf(z));
                }
                serviceInterfaceSafely.onAccessibilityButtonAvailabilityChanged(z);
            } catch (RemoteException e) {
                Slog.e(LOG_TAG, "Error sending accessibility button availability change to " + this.mService, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyGestureInternal(AccessibilityGestureEvent accessibilityGestureEvent) {
        IAccessibilityServiceClient serviceInterfaceSafely = getServiceInterfaceSafely();
        if (serviceInterfaceSafely != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("onGesture", accessibilityGestureEvent.toString());
                }
                serviceInterfaceSafely.onGesture(accessibilityGestureEvent);
            } catch (RemoteException e) {
                Slog.e(LOG_TAG, "Error during sending gesture " + accessibilityGestureEvent + " to " + this.mService, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySystemActionsChangedInternal() {
        IAccessibilityServiceClient serviceInterfaceSafely = getServiceInterfaceSafely();
        if (serviceInterfaceSafely != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("onSystemActionsChanged", "");
                }
                serviceInterfaceSafely.onSystemActionsChanged();
            } catch (RemoteException e) {
                Slog.e(LOG_TAG, "Error sending system actions change to " + this.mService, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyClearAccessibilityCacheInternal() {
        IAccessibilityServiceClient serviceInterfaceSafely = getServiceInterfaceSafely();
        if (serviceInterfaceSafely != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("clearAccessibilityCache", "");
                }
                serviceInterfaceSafely.clearAccessibilityCache();
            } catch (RemoteException e) {
                Slog.e(LOG_TAG, "Error during requesting accessibility info cache to be cleared.", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createImeSessionInternal() {
        IAccessibilityServiceClient serviceInterfaceSafely = getServiceInterfaceSafely();
        if (serviceInterfaceSafely != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("createImeSession", "");
                }
                serviceInterfaceSafely.createImeSession(new AccessibilityCallback());
            } catch (RemoteException e) {
                Slog.e(LOG_TAG, "Error requesting IME session from " + this.mService, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImeSessionEnabledInternal(IAccessibilityInputMethodSession iAccessibilityInputMethodSession, boolean z) {
        IAccessibilityServiceClient serviceInterfaceSafely = getServiceInterfaceSafely();
        if (serviceInterfaceSafely == null || iAccessibilityInputMethodSession == null) {
            return;
        }
        try {
            if (svcClientTracingEnabled()) {
                logTraceSvcClient("createImeSession", "");
            }
            serviceInterfaceSafely.setImeSessionEnabled(iAccessibilityInputMethodSession, z);
        } catch (RemoteException e) {
            Slog.e(LOG_TAG, "Error requesting IME session from " + this.mService, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindInputInternal() {
        IAccessibilityServiceClient serviceInterfaceSafely = getServiceInterfaceSafely();
        if (serviceInterfaceSafely != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("bindInput", "");
                }
                serviceInterfaceSafely.bindInput();
            } catch (RemoteException e) {
                Slog.e(LOG_TAG, "Error binding input to " + this.mService, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindInputInternal() {
        IAccessibilityServiceClient serviceInterfaceSafely = getServiceInterfaceSafely();
        if (serviceInterfaceSafely != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("unbindInput", "");
                }
                serviceInterfaceSafely.unbindInput();
            } catch (RemoteException e) {
                Slog.e(LOG_TAG, "Error unbinding input to " + this.mService, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startInputInternal(IRemoteAccessibilityInputConnection iRemoteAccessibilityInputConnection, EditorInfo editorInfo, boolean z) {
        IAccessibilityServiceClient serviceInterfaceSafely = getServiceInterfaceSafely();
        if (serviceInterfaceSafely != null) {
            try {
                if (svcClientTracingEnabled()) {
                    logTraceSvcClient("startInput", "editorInfo=" + editorInfo + " restarting=" + z);
                }
                serviceInterfaceSafely.startInput(iRemoteAccessibilityInputConnection, editorInfo, z);
            } catch (RemoteException e) {
                Slog.e(LOG_TAG, "Error starting input to " + this.mService, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IAccessibilityServiceClient getServiceInterfaceSafely() {
        IAccessibilityServiceClient iAccessibilityServiceClient;
        synchronized (this.mLock) {
            iAccessibilityServiceClient = this.mServiceInterface;
        }
        return iAccessibilityServiceClient;
    }

    private int resolveAccessibilityWindowIdLocked(int i) {
        if (i == Integer.MAX_VALUE) {
            i = this.mA11yWindowManager.getActiveWindowId(this.mSystemSupport.getCurrentUserIdLocked());
            if (!this.mA11yWindowManager.windowIdBelongsToDisplayType(i, this.mDisplayTypes)) {
                return -1;
            }
        }
        return i;
    }

    int resolveAccessibilityWindowIdForFindFocusLocked(int i, int i2) {
        if (i == -2) {
            i = this.mA11yWindowManager.getFocusedWindowId(i2);
            if (!this.mA11yWindowManager.windowIdBelongsToDisplayType(i, this.mDisplayTypes)) {
                return -1;
            }
        }
        return i;
    }

    private void ensureWindowsAvailableTimedLocked(int i) {
        if (i != -1 && this.mA11yWindowManager.getWindowListLocked(i) == null) {
            if (!this.mA11yWindowManager.isTrackingWindowsLocked(i)) {
                this.mSystemSupport.onClientChangeLocked(false);
            }
            if (this.mA11yWindowManager.isTrackingWindowsLocked(i)) {
                long uptimeMillis = SystemClock.uptimeMillis();
                while (this.mA11yWindowManager.getWindowListLocked(i) == null) {
                    long uptimeMillis2 = 5000 - (SystemClock.uptimeMillis() - uptimeMillis);
                    if (uptimeMillis2 <= 0) {
                        return;
                    } else {
                        try {
                            this.mLock.wait(uptimeMillis2);
                        } catch (InterruptedException unused) {
                        }
                    }
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0024 A[Catch: all -> 0x00fc, TryCatch #1 {, blocks: (B:4:0x000b, B:6:0x0014, B:16:0x0024, B:17:0x002c, B:19:0x0034, B:21:0x003a, B:24:0x0044, B:25:0x004a), top: B:3:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x006c A[Catch: all -> 0x00f2, RemoteException -> 0x00f7, TryCatch #3 {RemoteException -> 0x00f7, all -> 0x00f2, blocks: (B:28:0x0053, B:33:0x006c, B:34:0x0071, B:36:0x0077, B:37:0x00d8, B:42:0x0065), top: B:27:0x0053 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0077 A[Catch: all -> 0x00f2, RemoteException -> 0x00f7, TryCatch #3 {RemoteException -> 0x00f7, all -> 0x00f2, blocks: (B:28:0x0053, B:33:0x006c, B:34:0x0071, B:36:0x0077, B:37:0x00d8, B:42:0x0065), top: B:27:0x0053 }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00ce  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean performAccessibilityActionInternal(int i, int i2, long j, int i3, Bundle bundle, int i4, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i5, long j2) {
        boolean z;
        IBinder windowTokenForUserAndWindowIdLocked;
        AccessibilityWindowInfo findA11yWindowInfoByIdLocked;
        long clearCallingIdentity;
        synchronized (this.mLock) {
            AccessibilityWindowManager.RemoteAccessibilityConnection connectionLocked = this.mA11yWindowManager.getConnectionLocked(i, i2);
            if (connectionLocked == null) {
                return false;
            }
            try {
                if (i3 != 64 && i3 != 128) {
                    z = false;
                    windowTokenForUserAndWindowIdLocked = z ? this.mA11yWindowManager.getWindowTokenForUserAndWindowIdLocked(i, i2) : null;
                    findA11yWindowInfoByIdLocked = this.mA11yWindowManager.findA11yWindowInfoByIdLocked(i2);
                    if (findA11yWindowInfoByIdLocked != null && findA11yWindowInfoByIdLocked.isInPictureInPictureMode() && this.mA11yWindowManager.getPictureInPictureActionReplacingConnection() != null && !z) {
                        connectionLocked = this.mA11yWindowManager.getPictureInPictureActionReplacingConnection();
                    }
                    int callingPid = Binder.getCallingPid();
                    clearCallingIdentity = Binder.clearCallingIdentity();
                    this.mPowerManager.userActivity(SystemClock.uptimeMillis(), 3, 0);
                    if (i3 != 16 || i3 == 32) {
                        this.mA11yWindowManager.notifyOutsideTouch(i, i2);
                    }
                    if (windowTokenForUserAndWindowIdLocked != null) {
                        this.mWindowManagerService.requestWindowFocus(windowTokenForUserAndWindowIdLocked);
                    }
                    if (!intConnTracingEnabled()) {
                        logTraceIntConn("performAccessibilityAction", j + ";" + i3 + ";" + bundle + ";" + i4 + ";" + iAccessibilityInteractionConnectionCallback + ";" + this.mFetchFlags + ";" + callingPid + ";" + j2);
                    }
                    connectionLocked.getRemote().performAccessibilityAction(j, i3, bundle, i4, iAccessibilityInteractionConnectionCallback, i5, callingPid, j2);
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return true;
                }
                this.mPowerManager.userActivity(SystemClock.uptimeMillis(), 3, 0);
                if (i3 != 16) {
                }
                this.mA11yWindowManager.notifyOutsideTouch(i, i2);
                if (windowTokenForUserAndWindowIdLocked != null) {
                }
                if (!intConnTracingEnabled()) {
                }
                connectionLocked.getRemote().performAccessibilityAction(j, i3, bundle, i4, iAccessibilityInteractionConnectionCallback, i5, callingPid, j2);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return true;
            } catch (RemoteException unused) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return false;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
            z = true;
            if (z) {
            }
            findA11yWindowInfoByIdLocked = this.mA11yWindowManager.findA11yWindowInfoByIdLocked(i2);
            if (findA11yWindowInfoByIdLocked != null) {
                connectionLocked = this.mA11yWindowManager.getPictureInPictureActionReplacingConnection();
            }
            int callingPid2 = Binder.getCallingPid();
            clearCallingIdentity = Binder.clearCallingIdentity();
        }
    }

    private IAccessibilityInteractionConnectionCallback replaceCallbackIfNeeded(IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i, int i2, int i3, long j) {
        AccessibilityWindowManager.RemoteAccessibilityConnection pictureInPictureActionReplacingConnection = this.mA11yWindowManager.getPictureInPictureActionReplacingConnection();
        synchronized (this.mLock) {
            AccessibilityWindowInfo findA11yWindowInfoByIdLocked = this.mA11yWindowManager.findA11yWindowInfoByIdLocked(i);
            if (findA11yWindowInfoByIdLocked != null && findA11yWindowInfoByIdLocked.isInPictureInPictureMode() && pictureInPictureActionReplacingConnection != null) {
                return new ActionReplacingCallback(iAccessibilityInteractionConnectionCallback, pictureInPictureActionReplacingConnection.getRemote(), i2, i3, j);
            }
            return iAccessibilityInteractionConnectionCallback;
        }
    }

    private List<AccessibilityWindowInfo> getWindowsByDisplayLocked(int i) {
        List<AccessibilityWindowInfo> windowListLocked = this.mA11yWindowManager.getWindowListLocked(i);
        if (windowListLocked == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int size = windowListLocked.size();
        for (int i2 = 0; i2 < size; i2++) {
            AccessibilityWindowInfo obtain = AccessibilityWindowInfo.obtain(windowListLocked.get(i2));
            obtain.setConnectionId(this.mId);
            arrayList.add(obtain);
        }
        return arrayList;
    }

    public ComponentName getComponentName() {
        return this.mComponentName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class InvocationHandler extends Handler {
        private static final int MSG_BIND_INPUT = 12;
        public static final int MSG_CLEAR_ACCESSIBILITY_CACHE = 2;
        private static final int MSG_CREATE_IME_SESSION = 10;
        private static final int MSG_ON_ACCESSIBILITY_BUTTON_AVAILABILITY_CHANGED = 8;
        private static final int MSG_ON_ACCESSIBILITY_BUTTON_CLICKED = 7;
        public static final int MSG_ON_GESTURE = 1;
        private static final int MSG_ON_MAGNIFICATION_CHANGED = 5;
        private static final int MSG_ON_SOFT_KEYBOARD_STATE_CHANGED = 6;
        private static final int MSG_ON_SYSTEM_ACTIONS_CHANGED = 9;
        private static final int MSG_SET_IME_SESSION_ENABLED = 11;
        private static final int MSG_START_INPUT = 14;
        private static final int MSG_UNBIND_INPUT = 13;
        private boolean mIsSoftKeyboardCallbackEnabled;

        @GuardedBy({"mlock"})
        private final SparseArray<Boolean> mMagnificationCallbackState;

        public InvocationHandler(Looper looper) {
            super(looper, null, true);
            this.mMagnificationCallbackState = new SparseArray<>(0);
            this.mIsSoftKeyboardCallbackEnabled = false;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                AbstractAccessibilityServiceConnection.this.notifyGestureInternal((AccessibilityGestureEvent) message.obj);
                return;
            }
            if (i == 2) {
                AbstractAccessibilityServiceConnection.this.notifyClearAccessibilityCacheInternal();
                return;
            }
            switch (i) {
                case 5:
                    SomeArgs someArgs = (SomeArgs) message.obj;
                    AbstractAccessibilityServiceConnection.this.notifyMagnificationChangedInternal(someArgs.argi1, (Region) someArgs.arg1, (MagnificationConfig) someArgs.arg2);
                    someArgs.recycle();
                    return;
                case 6:
                    AbstractAccessibilityServiceConnection.this.notifySoftKeyboardShowModeChangedInternal(message.arg1);
                    return;
                case 7:
                    AbstractAccessibilityServiceConnection.this.notifyAccessibilityButtonClickedInternal(message.arg1);
                    return;
                case 8:
                    AbstractAccessibilityServiceConnection.this.notifyAccessibilityButtonAvailabilityChangedInternal(message.arg1 != 0);
                    return;
                case 9:
                    AbstractAccessibilityServiceConnection.this.notifySystemActionsChangedInternal();
                    return;
                case 10:
                    AbstractAccessibilityServiceConnection.this.createImeSessionInternal();
                    return;
                case 11:
                    AbstractAccessibilityServiceConnection.this.setImeSessionEnabledInternal((IAccessibilityInputMethodSession) message.obj, message.arg1 != 0);
                    return;
                case 12:
                    AbstractAccessibilityServiceConnection.this.bindInputInternal();
                    return;
                case 13:
                    AbstractAccessibilityServiceConnection.this.unbindInputInternal();
                    return;
                case 14:
                    boolean z = message.arg1 != 0;
                    SomeArgs someArgs2 = (SomeArgs) message.obj;
                    AbstractAccessibilityServiceConnection.this.startInputInternal((IRemoteAccessibilityInputConnection) someArgs2.arg1, (EditorInfo) someArgs2.arg2, z);
                    someArgs2.recycle();
                    return;
                default:
                    throw new IllegalArgumentException("Unknown message: " + i);
            }
        }

        public void notifyMagnificationChangedLocked(int i, Region region, MagnificationConfig magnificationConfig) {
            synchronized (AbstractAccessibilityServiceConnection.this.mLock) {
                if (this.mMagnificationCallbackState.get(i) == null) {
                    return;
                }
                SomeArgs obtain = SomeArgs.obtain();
                obtain.arg1 = region;
                obtain.arg2 = magnificationConfig;
                obtain.argi1 = i;
                obtainMessage(5, obtain).sendToTarget();
            }
        }

        public void setMagnificationCallbackEnabled(int i, boolean z) {
            synchronized (AbstractAccessibilityServiceConnection.this.mLock) {
                if (z) {
                    this.mMagnificationCallbackState.put(i, Boolean.TRUE);
                } else {
                    this.mMagnificationCallbackState.remove(i);
                }
            }
        }

        public boolean isMagnificationCallbackEnabled(int i) {
            boolean z;
            synchronized (AbstractAccessibilityServiceConnection.this.mLock) {
                z = this.mMagnificationCallbackState.get(i) != null;
            }
            return z;
        }

        public void notifySoftKeyboardShowModeChangedLocked(int i) {
            if (this.mIsSoftKeyboardCallbackEnabled) {
                obtainMessage(6, i, 0).sendToTarget();
            }
        }

        public void setSoftKeyboardCallbackEnabled(boolean z) {
            this.mIsSoftKeyboardCallbackEnabled = z;
        }

        public void notifyAccessibilityButtonClickedLocked(int i) {
            obtainMessage(7, i, 0).sendToTarget();
        }

        public void notifyAccessibilityButtonAvailabilityChangedLocked(boolean z) {
            obtainMessage(8, z ? 1 : 0, 0).sendToTarget();
        }

        public void createImeSessionLocked() {
            obtainMessage(10).sendToTarget();
        }

        public void setImeSessionEnabledLocked(IAccessibilityInputMethodSession iAccessibilityInputMethodSession, boolean z) {
            obtainMessage(11, z ? 1 : 0, 0, iAccessibilityInputMethodSession).sendToTarget();
        }

        public void bindInputLocked() {
            obtainMessage(12).sendToTarget();
        }

        public void unbindInputLocked() {
            obtainMessage(13).sendToTarget();
        }

        public void startInputLocked(IRemoteAccessibilityInputConnection iRemoteAccessibilityInputConnection, EditorInfo editorInfo, boolean z) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = iRemoteAccessibilityInputConnection;
            obtain.arg2 = editorInfo;
            obtainMessage(14, z ? 1 : 0, 0, obtain).sendToTarget();
        }
    }

    public boolean isServiceHandlesDoubleTapEnabled() {
        return this.mServiceHandlesDoubleTap;
    }

    public boolean isMultiFingerGesturesEnabled() {
        return this.mRequestMultiFingerGestures;
    }

    public boolean isTwoFingerPassthroughEnabled() {
        return this.mRequestTwoFingerPassthrough;
    }

    public boolean isSendMotionEventsEnabled() {
        return this.mSendMotionEvents;
    }

    public void setGestureDetectionPassthroughRegion(int i, Region region) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setGestureDetectionPassthroughRegion", "displayId=" + i + ";region=" + region);
        }
        this.mSystemSupport.setGestureDetectionPassthroughRegion(i, region);
    }

    public void setTouchExplorationPassthroughRegion(int i, Region region) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setTouchExplorationPassthroughRegion", "displayId=" + i + ";region=" + region);
        }
        this.mSystemSupport.setTouchExplorationPassthroughRegion(i, region);
    }

    public void setFocusAppearance(int i, int i2) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setFocusAppearance", "strokeWidth=" + i + ";color=" + i2);
        }
    }

    public void setCacheEnabled(boolean z) {
        if (svcConnTracingEnabled()) {
            logTraceSvcConn("setCacheEnabled", "enabled=" + z);
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                this.mUsesAccessibilityCache = z;
                this.mSystemSupport.onClientChangeLocked(true);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void logTrace(long j, String str, long j2, String str2, int i, long j3, int i2, Bundle bundle) {
        if (this.mTrace.isA11yTracingEnabledForTypes(j2)) {
            ArrayList arrayList = (ArrayList) bundle.getSerializable("call_stack", ArrayList.class);
            this.mTrace.logTrace(j, str, j2, str2, i, j3, i2, (StackTraceElement[]) arrayList.toArray(new StackTraceElement[arrayList.size()]), (HashSet) bundle.getSerializable("ignore_call_stack", HashSet.class));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean svcClientTracingEnabled() {
        return this.mTrace.isA11yTracingEnabledForTypes(2L);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void logTraceSvcClient(String str, String str2) {
        this.mTrace.logTrace("AbstractAccessibilityServiceConnection.IAccessibilityServiceClient." + str, 2L, str2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean svcConnTracingEnabled() {
        return this.mTrace.isA11yTracingEnabledForTypes(1L);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void logTraceSvcConn(String str, String str2) {
        this.mTrace.logTrace("AbstractAccessibilityServiceConnection.IAccessibilityServiceConnection." + str, 1L, str2);
    }

    protected boolean intConnTracingEnabled() {
        return this.mTrace.isA11yTracingEnabledForTypes(16L);
    }

    protected void logTraceIntConn(String str, String str2) {
        this.mTrace.logTrace("AbstractAccessibilityServiceConnection." + str, 16L, str2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean wmTracingEnabled() {
        return this.mTrace.isA11yTracingEnabledForTypes(512L);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void logTraceWM(String str, String str2) {
        this.mTrace.logTrace("WindowManagerInternal." + str, 512L, str2);
    }

    public void setServiceDetectsGesturesEnabled(int i, boolean z) {
        this.mServiceDetectsGestures.put(i, Boolean.valueOf(z));
        this.mSystemSupport.setServiceDetectsGesturesEnabled(i, z);
    }

    public boolean isServiceDetectsGesturesEnabled(int i) {
        if (this.mServiceDetectsGestures.contains(i)) {
            return this.mServiceDetectsGestures.get(i).booleanValue();
        }
        return false;
    }

    public void requestTouchExploration(int i) {
        this.mSystemSupport.requestTouchExploration(i);
    }

    public void requestDragging(int i, int i2) {
        this.mSystemSupport.requestDragging(i, i2);
    }

    public void requestDelegating(int i) {
        this.mSystemSupport.requestDelegating(i);
    }

    public void onDoubleTap(int i) {
        this.mSystemSupport.onDoubleTap(i);
    }

    public void onDoubleTapAndHold(int i) {
        this.mSystemSupport.onDoubleTapAndHold(i);
    }

    public void setAnimationScale(float f) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Settings.Global.putFloat(this.mContext.getContentResolver(), "window_animation_scale", f);
            Settings.Global.putFloat(this.mContext.getContentResolver(), "transition_animation_scale", f);
            Settings.Global.putFloat(this.mContext.getContentResolver(), "animator_duration_scale", f);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class AccessibilityCallback extends IAccessibilityInputMethodSessionCallback.Stub {
        private AccessibilityCallback() {
        }

        public void sessionCreated(IAccessibilityInputMethodSession iAccessibilityInputMethodSession, int i) {
            Trace.traceBegin(32L, "AACS.sessionCreated");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                InputMethodManagerInternal.get().onSessionForAccessibilityCreated(i, iAccessibilityInputMethodSession);
                Binder.restoreCallingIdentity(clearCallingIdentity);
                Trace.traceEnd(32L);
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
        }
    }

    public void attachAccessibilityOverlayToDisplay(int i, SurfaceControl surfaceControl) {
        this.mSystemSupport.attachAccessibilityOverlayToDisplay(i, surfaceControl);
    }

    public void attachAccessibilityOverlayToWindow(int i, SurfaceControl surfaceControl) throws RemoteException {
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        transaction.setTrustedOverlay(surfaceControl, true).apply();
        transaction.close();
        synchronized (this.mLock) {
            AccessibilityWindowManager.RemoteAccessibilityConnection connectionLocked = this.mA11yWindowManager.getConnectionLocked(this.mSystemSupport.getCurrentUserIdLocked(), resolveAccessibilityWindowIdLocked(i));
            if (connectionLocked == null) {
                Slog.e(LOG_TAG, "unable to get remote accessibility connection.");
            } else {
                connectionLocked.getRemote().attachAccessibilityOverlayToWindow(surfaceControl);
            }
        }
    }
}
