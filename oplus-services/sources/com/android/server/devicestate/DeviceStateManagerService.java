package com.android.server.devicestate;

import android.R;
import android.app.ActivityManager;
import android.app.TaskStackListener;
import android.content.Context;
import android.hardware.devicestate.DeviceStateInfo;
import android.hardware.devicestate.DeviceStateManagerInternal;
import android.hardware.devicestate.IDeviceStateManager;
import android.hardware.devicestate.IDeviceStateManagerCallback;
import android.os.Binder;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.DisplayThread;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.devicestate.DeviceStateManagerService;
import com.android.server.devicestate.DeviceStatePolicy;
import com.android.server.devicestate.DeviceStateProvider;
import com.android.server.devicestate.OverrideRequestController;
import com.android.server.statusbar.StatusBarManagerInternal;
import com.android.server.wm.ActivityTaskManagerInternal;
import com.android.server.wm.WindowManagerInternal;
import com.android.server.wm.WindowProcessController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DeviceStateManagerService extends SystemService {
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final String TAG = "DeviceStateManagerService";

    @GuardedBy({"mLock"})
    private Optional<OverrideRequest> mActiveBaseStateOverride;

    @GuardedBy({"mLock"})
    private Optional<OverrideRequest> mActiveOverride;

    @VisibleForTesting
    public ActivityTaskManagerInternal mActivityTaskManagerInternal;

    @GuardedBy({"mLock"})
    private Optional<DeviceState> mBaseState;
    private final BinderService mBinderService;

    @GuardedBy({"mLock"})
    private Optional<DeviceState> mCommittedState;
    private IDeviceStateManagerServiceExt mDeviceStateManagerServiceExt;
    private final DeviceStateNotificationController mDeviceStateNotificationController;
    private final DeviceStatePolicy mDeviceStatePolicy;
    private final DeviceStateProviderListener mDeviceStateProviderListener;

    @GuardedBy({"mLock"})
    private SparseArray<DeviceState> mDeviceStates;
    private Set<Integer> mDeviceStatesAvailableForAppRequests;
    private boolean mDeviceStatesEnabled;
    private Set<Integer> mFoldedDeviceStates;
    private final Handler mHandler;

    @GuardedBy({"mLock"})
    private boolean mIsPolicyWaitingForState;
    private final Object mLock;
    private final OverrideRequestController mOverrideRequestController;

    @VisibleForTesting
    ActivityTaskManagerInternal.ScreenObserver mOverrideRequestScreenObserver;

    @VisibleForTesting
    TaskStackListener mOverrideRequestTaskStackListener;

    @GuardedBy({"mLock"})
    private Optional<DeviceState> mPendingState;

    @GuardedBy({"mLock"})
    private final SparseArray<ProcessRecord> mProcessRecords;

    @GuardedBy({"mLock"})
    private OverrideRequest mRearDisplayPendingOverrideRequest;
    private DeviceState mRearDisplayState;
    private final SystemPropertySetter mSystemPropertySetter;
    public WindowManagerInternal mWindowManagerInternal;
    private IDeviceStateManagerServiceWrapper mWrapper;

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface SystemPropertySetter {
        void setDebugTracingDeviceStateProperty(String str);
    }

    public DeviceStateManagerService(Context context) {
        this(context, DeviceStatePolicy.Provider.fromResources(context.getResources()).instantiate(context));
    }

    private DeviceStateManagerService(Context context, DeviceStatePolicy deviceStatePolicy) {
        this(context, deviceStatePolicy, new SystemPropertySetter() { // from class: com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda6
            @Override // com.android.server.devicestate.DeviceStateManagerService.SystemPropertySetter
            public final void setDebugTracingDeviceStateProperty(String str) {
                SystemProperties.set("debug.tracing.device_state", str);
            }
        });
    }

    @VisibleForTesting
    DeviceStateManagerService(Context context, DeviceStatePolicy deviceStatePolicy, SystemPropertySetter systemPropertySetter) {
        super(context);
        this.mLock = new Object();
        this.mDeviceStates = new SparseArray<>();
        this.mCommittedState = Optional.empty();
        this.mPendingState = Optional.empty();
        this.mIsPolicyWaitingForState = false;
        this.mBaseState = Optional.empty();
        this.mActiveOverride = Optional.empty();
        this.mActiveBaseStateOverride = Optional.empty();
        this.mProcessRecords = new SparseArray<>();
        this.mDeviceStatesAvailableForAppRequests = new HashSet();
        this.mDeviceStateManagerServiceExt = (IDeviceStateManagerServiceExt) ExtLoader.type(IDeviceStateManagerServiceExt.class).base(this).create();
        this.mOverrideRequestTaskStackListener = new OverrideRequestTaskStackListener();
        this.mOverrideRequestScreenObserver = new OverrideRequestScreenObserver();
        this.mWrapper = new DeviceStateManagerServiceWrapper();
        this.mSystemPropertySetter = systemPropertySetter;
        Handler handler = new Handler(DisplayThread.get().getLooper());
        this.mHandler = handler;
        this.mOverrideRequestController = new OverrideRequestController(new OverrideRequestController.StatusChangeListener() { // from class: com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda4
            @Override // com.android.server.devicestate.OverrideRequestController.StatusChangeListener
            public final void onStatusChanged(OverrideRequest overrideRequest, int i, int i2) {
                DeviceStateManagerService.this.onOverrideRequestStatusChangedLocked(overrideRequest, i, i2);
            }
        });
        this.mDeviceStatePolicy = deviceStatePolicy;
        DeviceStateProviderListener deviceStateProviderListener = new DeviceStateProviderListener();
        this.mDeviceStateProviderListener = deviceStateProviderListener;
        deviceStatePolicy.getDeviceStateProvider().setListener(deviceStateProviderListener);
        deviceStatePolicy.getDeviceStateProvider().registerSensor();
        this.mBinderService = new BinderService();
        this.mActivityTaskManagerInternal = (ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class);
        this.mDeviceStateNotificationController = new DeviceStateNotificationController(context, handler, new Runnable() { // from class: com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                DeviceStateManagerService.this.lambda$new$1();
            }
        });
    }

    public /* synthetic */ void lambda$new$1() {
        synchronized (this.mLock) {
            Optional<OverrideRequest> optional = this.mActiveOverride;
            OverrideRequestController overrideRequestController = this.mOverrideRequestController;
            Objects.requireNonNull(overrideRequestController);
            optional.ifPresent(new DeviceStateManagerService$$ExternalSyntheticLambda3(overrideRequestController));
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("device_state", this.mBinderService);
        publishLocalService(DeviceStateManagerInternal.class, new LocalService());
        synchronized (this.mLock) {
            readStatesAvailableForRequestFromApps();
            this.mFoldedDeviceStates = readFoldedStates();
        }
        this.mActivityTaskManagerInternal.registerTaskStackListener(this.mOverrideRequestTaskStackListener);
        this.mActivityTaskManagerInternal.registerScreenObserver(this.mOverrideRequestScreenObserver);
    }

    @VisibleForTesting
    Handler getHandler() {
        return this.mHandler;
    }

    public Optional<DeviceState> getCommittedState() {
        Optional<DeviceState> optional;
        synchronized (this.mLock) {
            optional = this.mCommittedState;
        }
        return optional;
    }

    @VisibleForTesting
    Optional<DeviceState> getPendingState() {
        Optional<DeviceState> optional;
        synchronized (this.mLock) {
            optional = this.mPendingState;
        }
        return optional;
    }

    public Optional<DeviceState> getBaseState() {
        Optional<DeviceState> optional;
        synchronized (this.mLock) {
            optional = this.mBaseState;
        }
        return optional;
    }

    public Optional<DeviceState> getOverrideState() {
        synchronized (this.mLock) {
            if (this.mActiveOverride.isPresent()) {
                return getStateLocked(this.mActiveOverride.get().getRequestedState());
            }
            return Optional.empty();
        }
    }

    Optional<DeviceState> getOverrideBaseState() {
        synchronized (this.mLock) {
            if (this.mActiveBaseStateOverride.isPresent()) {
                return getStateLocked(this.mActiveBaseStateOverride.get().getRequestedState());
            }
            return Optional.empty();
        }
    }

    public DeviceState[] getSupportedStates() {
        DeviceState[] supportedStates;
        synchronized (this.mLock) {
            int size = this.mDeviceStates.size();
            DeviceState[] deviceStateArr = new DeviceState[size];
            for (int i = 0; i < size; i++) {
                deviceStateArr[i] = this.mDeviceStates.valueAt(i);
            }
            supportedStates = this.mDeviceStateManagerServiceExt.getSupportedStates(deviceStateArr, this.mDeviceStates);
        }
        return supportedStates;
    }

    public int[] getSupportedStateIdentifiersLocked() {
        int size = this.mDeviceStates.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = this.mDeviceStates.valueAt(i).getIdentifier();
        }
        return this.mDeviceStateManagerServiceExt.getSupportedStateIdentifiersLocked(iArr, this.mDeviceStates);
    }

    @GuardedBy({"mLock"})
    public DeviceStateInfo getDeviceStateInfoLocked() {
        return new DeviceStateInfo(getSupportedStateIdentifiersLocked(), this.mBaseState.isPresent() ? this.mBaseState.get().getIdentifier() : -1, this.mCommittedState.isPresent() ? this.mCommittedState.get().getIdentifier() : -1);
    }

    @VisibleForTesting
    IDeviceStateManager getBinderService() {
        return this.mBinderService;
    }

    public void updateSupportedStates(DeviceState[] deviceStateArr, int i) {
        synchronized (this.mLock) {
            int[] supportedStateIdentifiersLocked = getSupportedStateIdentifiersLocked();
            this.mDeviceStates.clear();
            boolean z = false;
            for (DeviceState deviceState : deviceStateArr) {
                if (deviceState.hasFlag(1)) {
                    z = true;
                }
                this.mDeviceStates.put(deviceState.getIdentifier(), deviceState);
            }
            this.mOverrideRequestController.setStickyRequestsAllowed(z);
            int[] supportedStateIdentifiersLocked2 = getSupportedStateIdentifiersLocked();
            if (Arrays.equals(supportedStateIdentifiersLocked, supportedStateIdentifiersLocked2)) {
                return;
            }
            this.mOverrideRequestController.handleNewSupportedStates(supportedStateIdentifiersLocked2, i);
            updatePendingStateLocked();
            setRearDisplayStateLocked();
            notifyDeviceStateInfoChangedAsync();
            this.mHandler.post(new DeviceStateManagerService$$ExternalSyntheticLambda0(this));
        }
    }

    @GuardedBy({"mLock"})
    private void setRearDisplayStateLocked() {
        int integer = getContext().getResources().getInteger(R.integer.config_jobSchedulerIdleWindowSlop);
        if (integer != -1) {
            this.mRearDisplayState = this.mDeviceStates.get(integer);
        }
    }

    private boolean isSupportedStateLocked(int i) {
        return this.mDeviceStates.contains(i);
    }

    public Optional<DeviceState> getStateLocked(int i) {
        return Optional.ofNullable(this.mDeviceStates.get(i));
    }

    public void setBaseState(int i) {
        synchronized (this.mLock) {
            Optional<DeviceState> stateLocked = getStateLocked(i);
            if (!stateLocked.isPresent()) {
                throw new IllegalArgumentException("Base state is not supported");
            }
            DeviceState deviceState = stateLocked.get();
            if (this.mBaseState.isPresent() && this.mBaseState.get().equals(deviceState)) {
                return;
            }
            if (this.mRearDisplayPendingOverrideRequest != null) {
                handleRearDisplayBaseStateChangedLocked(i);
            }
            Optional<DeviceState> of = Optional.of(deviceState);
            this.mBaseState = of;
            Optional<DeviceState> stateLocked2 = getStateLocked(this.mDeviceStateManagerServiceExt.overrideBaseState(of, i));
            if (stateLocked2.isPresent()) {
                this.mBaseState = Optional.of(stateLocked2.get());
                boolean canCancelRequestState = this.mDeviceStateManagerServiceExt.canCancelRequestState();
                if (shouldCancelOverrideRequest() && (canCancelRequestState || this.mDeviceStateManagerServiceExt.hasFoldRemapDisplayDisableFeature())) {
                    this.mOverrideRequestController.cancelOverrideRequest();
                }
                this.mOverrideRequestController.handleBaseStateChanged(i);
                updatePendingStateLocked();
                notifyDeviceStateInfoChangedAsync();
                if (this.mDeviceStateManagerServiceExt.notifyPolicyImmediately()) {
                    this.mHandler.postAtFrontOfQueue(new DeviceStateManagerService$$ExternalSyntheticLambda0(this));
                } else {
                    this.mHandler.post(new DeviceStateManagerService$$ExternalSyntheticLambda0(this));
                }
            }
        }
    }

    private boolean updatePendingStateLocked() {
        DeviceState deviceState;
        if (this.mPendingState.isPresent()) {
            return false;
        }
        if (this.mActiveOverride.isPresent()) {
            deviceState = getStateLocked(this.mActiveOverride.get().getRequestedState()).get();
        } else {
            deviceState = (this.mBaseState.isPresent() && isSupportedStateLocked(this.mBaseState.get().getIdentifier())) ? this.mBaseState.get() : null;
        }
        if (deviceState == null) {
            return false;
        }
        if (this.mCommittedState.isPresent() && deviceState.equals(this.mCommittedState.get())) {
            return false;
        }
        this.mPendingState = Optional.of(deviceState);
        this.mIsPolicyWaitingForState = true;
        return true;
    }

    public void notifyPolicyIfNeeded() {
        if (Thread.holdsLock(this.mLock)) {
            Throwable th = new Throwable("Attempting to notify DeviceStatePolicy with service lock held");
            th.fillInStackTrace();
            Slog.w(TAG, th);
        }
        synchronized (this.mLock) {
            if (this.mIsPolicyWaitingForState) {
                this.mIsPolicyWaitingForState = false;
                int identifier = this.mPendingState.get().getIdentifier();
                if (DEBUG) {
                    Slog.d(TAG, "Notifying policy to configure state: " + identifier);
                }
                if (identifier == 3 || identifier == 0 || identifier == 2) {
                    this.mDeviceStateManagerServiceExt.setSwitchingTrackerSensorEventLog();
                }
                this.mDeviceStatePolicy.configureDeviceForState(identifier, new Runnable() { // from class: com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        DeviceStateManagerService.this.commitPendingState();
                    }
                });
            }
        }
    }

    public void commitPendingState() {
        ProcessRecord processRecord;
        synchronized (this.mLock) {
            DeviceState deviceState = this.mPendingState.get();
            if (DEBUG) {
                Slog.d(TAG, "Committing state: " + deviceState);
            }
            FrameworkStatsLog.write(350, deviceState.getIdentifier(), !this.mCommittedState.isPresent());
            String str = deviceState.getIdentifier() + ":" + deviceState.getName();
            Trace.instantForTrack(524288L, "DeviceStateChanged", str);
            this.mSystemPropertySetter.setDebugTracingDeviceStateProperty(str);
            this.mCommittedState = Optional.of(deviceState);
            this.mPendingState = Optional.empty();
            updatePendingStateLocked();
            notifyDeviceStateInfoChangedAsync();
            OverrideRequest orElse = this.mActiveOverride.orElse(null);
            if (orElse != null && orElse.getRequestedState() == deviceState.getIdentifier() && (processRecord = this.mProcessRecords.get(orElse.getPid())) != null) {
                processRecord.notifyRequestActiveAsync(orElse.getToken());
            }
            this.mHandler.post(new DeviceStateManagerService$$ExternalSyntheticLambda0(this));
        }
    }

    private void notifyDeviceStateInfoChangedAsync() {
        synchronized (this.mLock) {
            if (this.mPendingState.isPresent()) {
                Slog.i(TAG, "Cannot notify device state info change when pending state is present.");
                return;
            }
            if (this.mBaseState.isPresent() && this.mCommittedState.isPresent()) {
                if (this.mCommittedState.isPresent()) {
                    this.mDeviceStateManagerServiceExt.setDeviceStateInfo(getDeviceStateInfoLocked());
                }
                if (this.mProcessRecords.size() == 0) {
                    return;
                }
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < this.mProcessRecords.size(); i++) {
                    arrayList.add(this.mProcessRecords.valueAt(i));
                }
                this.mDeviceStateManagerServiceExt.shouldInjectTransitoryState(this.mCommittedState);
                DeviceStateInfo deviceStateInfoLocked = getDeviceStateInfoLocked();
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    ((ProcessRecord) arrayList.get(i2)).notifyDeviceStateInfoAsync(deviceStateInfoLocked);
                }
                return;
            }
            Slog.e(TAG, "Cannot notify device state info change before the initial state has been committed.");
        }
    }

    @GuardedBy({"mLock"})
    public void onOverrideRequestStatusChangedLocked(OverrideRequest overrideRequest, int i, int i2) {
        if (overrideRequest.getRequestType() == 1) {
            if (i == 1) {
                enableBaseStateRequestLocked(overrideRequest);
                return;
            }
            if (i == 2) {
                if (this.mActiveBaseStateOverride.isPresent() && this.mActiveBaseStateOverride.get() == overrideRequest) {
                    this.mActiveBaseStateOverride = Optional.empty();
                }
            } else {
                throw new IllegalArgumentException("Unknown request status: " + i);
            }
        } else {
            if (overrideRequest.getRequestType() != 0) {
                throw new IllegalArgumentException("Unknown OverrideRest type: " + overrideRequest.getRequestType());
            }
            if (i == 1) {
                this.mActiveOverride = Optional.of(overrideRequest);
                this.mDeviceStateNotificationController.showStateActiveNotificationIfNeeded(overrideRequest.getRequestedState(), overrideRequest.getUid());
            } else if (i == 2) {
                if (this.mActiveOverride.isPresent() && this.mActiveOverride.get() == overrideRequest) {
                    this.mActiveOverride = Optional.empty();
                    this.mDeviceStateNotificationController.cancelNotification(overrideRequest.getRequestedState());
                    if ((i2 & 1) == 1) {
                        this.mDeviceStateNotificationController.showThermalCriticalNotificationIfNeeded(overrideRequest.getRequestedState());
                    } else if ((i2 & 2) == 2) {
                        this.mDeviceStateNotificationController.showPowerSaveNotificationIfNeeded(overrideRequest.getRequestedState());
                    }
                }
            } else {
                throw new IllegalArgumentException("Unknown request status: " + i);
            }
        }
        this.mDeviceStateManagerServiceExt.setRequestState(i, overrideRequest.getRequestedState(), overrideRequest.getPid(), overrideRequest.getFlags());
        if (DEBUG) {
            Slog.i(TAG, "overrideRequestStatusChange " + OverrideRequestController.statusToString(i) + " requestedState:" + overrideRequest.getRequestedState() + " flags:" + overrideRequest.getFlags() + " pid:" + overrideRequest.getPid() + " " + Debug.getCallers(15));
        }
        boolean updatePendingStateLocked = updatePendingStateLocked();
        ProcessRecord processRecord = this.mProcessRecords.get(overrideRequest.getPid());
        if (processRecord == null) {
            this.mHandler.post(new DeviceStateManagerService$$ExternalSyntheticLambda0(this));
            return;
        }
        if (i == 1) {
            if (!updatePendingStateLocked && !this.mPendingState.isPresent()) {
                processRecord.notifyRequestActiveAsync(overrideRequest.getToken());
            }
        } else {
            processRecord.notifyRequestCanceledAsync(overrideRequest.getToken());
        }
        this.mHandler.post(new DeviceStateManagerService$$ExternalSyntheticLambda0(this));
    }

    @GuardedBy({"mLock"})
    private void enableBaseStateRequestLocked(OverrideRequest overrideRequest) {
        setBaseState(overrideRequest.getRequestedState());
        this.mActiveBaseStateOverride = Optional.of(overrideRequest);
        this.mProcessRecords.get(overrideRequest.getPid()).notifyRequestActiveAsync(overrideRequest.getToken());
    }

    public void registerProcess(int i, IDeviceStateManagerCallback iDeviceStateManagerCallback) {
        synchronized (this.mLock) {
            if (this.mProcessRecords.contains(i)) {
                throw new SecurityException("The calling process has already registered an IDeviceStateManagerCallback.");
            }
            ProcessRecord processRecord = new ProcessRecord(iDeviceStateManagerCallback, i, new ProcessRecord.DeathListener() { // from class: com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda1
                @Override // com.android.server.devicestate.DeviceStateManagerService.ProcessRecord.DeathListener
                public final void onProcessDied(DeviceStateManagerService.ProcessRecord processRecord2) {
                    DeviceStateManagerService.this.handleProcessDied(processRecord2);
                }
            }, this.mHandler);
            try {
                iDeviceStateManagerCallback.asBinder().linkToDeath(processRecord, 0);
                this.mProcessRecords.put(i, processRecord);
                DeviceStateInfo deviceStateInfoLocked = this.mCommittedState.isPresent() ? getDeviceStateInfoLocked() : null;
                if (deviceStateInfoLocked != null) {
                    processRecord.notifyDeviceStateInfoAsync(deviceStateInfoLocked);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handleProcessDied(ProcessRecord processRecord) {
        synchronized (this.mLock) {
            this.mProcessRecords.remove(processRecord.mPid);
            this.mOverrideRequestController.handleProcessDied(processRecord.mPid);
        }
    }

    public void requestStateInternal(int i, int i2, int i3, int i4, IBinder iBinder, boolean z) {
        DeviceState deviceState;
        synchronized (this.mLock) {
            if (this.mProcessRecords.get(i3) == null) {
                throw new IllegalStateException("Process " + i3 + " has no registered callback.");
            }
            if (this.mOverrideRequestController.hasRequest(iBinder, 0)) {
                throw new IllegalStateException("Request has already been made for the supplied token: " + iBinder);
            }
            if (!getStateLocked(i).isPresent()) {
                throw new IllegalArgumentException("Requested state: " + i + " is not supported.");
            }
            if (this.mDeviceStateManagerServiceExt.canRequestState(this.mBaseState, i)) {
                if (this.mBaseState.isPresent() && this.mBaseState.get().getIdentifier() == 3 && i == 101) {
                    Slog.w(TAG, "Requested state: " + i + " is not supported when baseState is DEVICE_STATE_OPEN");
                    return;
                }
                OverrideRequest overrideRequest = new OverrideRequest(iBinder, i3, i4, i, i2, 0);
                if (!z && (deviceState = this.mRearDisplayState) != null && i == deviceState.getIdentifier()) {
                    showRearDisplayEducationalOverlayLocked(overrideRequest);
                } else {
                    this.mOverrideRequestController.addRequest(overrideRequest);
                }
            }
        }
    }

    @GuardedBy({"mLock"})
    private void showRearDisplayEducationalOverlayLocked(OverrideRequest overrideRequest) {
        this.mRearDisplayPendingOverrideRequest = overrideRequest;
        StatusBarManagerInternal statusBarManagerInternal = (StatusBarManagerInternal) LocalServices.getService(StatusBarManagerInternal.class);
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.showRearDisplayDialog(this.mBaseState.get().getIdentifier());
        }
    }

    public void cancelStateRequestInternal(int i) {
        synchronized (this.mLock) {
            if (this.mProcessRecords.get(i) == null) {
                throw new IllegalStateException("Process " + i + " has no registered callback.");
            }
            Optional<OverrideRequest> optional = this.mActiveOverride;
            OverrideRequestController overrideRequestController = this.mOverrideRequestController;
            Objects.requireNonNull(overrideRequestController);
            optional.ifPresent(new DeviceStateManagerService$$ExternalSyntheticLambda3(overrideRequestController));
        }
    }

    public void requestBaseStateOverrideInternal(int i, int i2, int i3, int i4, IBinder iBinder) {
        synchronized (this.mLock) {
            if (!getStateLocked(i).isPresent()) {
                throw new IllegalArgumentException("Requested state: " + i + " is not supported.");
            }
            if (this.mProcessRecords.get(i3) == null) {
                throw new IllegalStateException("Process " + i3 + " has no registered callback.");
            }
            if (this.mOverrideRequestController.hasRequest(iBinder, 1)) {
                throw new IllegalStateException("Request has already been made for the supplied token: " + iBinder);
            }
            this.mOverrideRequestController.addBaseStateRequest(new OverrideRequest(iBinder, i3, i4, i, i2, 1));
        }
    }

    public void cancelBaseStateOverrideInternal(int i) {
        synchronized (this.mLock) {
            if (this.mProcessRecords.get(i) == null) {
                throw new IllegalStateException("Process " + i + " has no registered callback.");
            }
            setBaseState(this.mDeviceStateProviderListener.mCurrentBaseState);
        }
    }

    public void onStateRequestOverlayDismissedInternal(boolean z) {
        synchronized (this.mLock) {
            OverrideRequest overrideRequest = this.mRearDisplayPendingOverrideRequest;
            if (overrideRequest != null) {
                if (z) {
                    this.mProcessRecords.get(overrideRequest.getPid()).notifyRequestCanceledAsync(this.mRearDisplayPendingOverrideRequest.getToken());
                } else {
                    this.mOverrideRequestController.addRequest(overrideRequest);
                }
                this.mRearDisplayPendingOverrideRequest = null;
            }
        }
    }

    public void dumpInternal(PrintWriter printWriter) {
        printWriter.println("DEVICE STATE MANAGER (dumpsys device_state)");
        synchronized (this.mLock) {
            printWriter.println("  mCommittedState=" + this.mCommittedState);
            printWriter.println("  mPendingState=" + this.mPendingState);
            printWriter.println("  mBaseState=" + this.mBaseState);
            printWriter.println("  mOverrideState=" + getOverrideState());
            int size = this.mProcessRecords.size();
            printWriter.println();
            printWriter.println("Registered processes: size=" + size);
            for (int i = 0; i < size; i++) {
                printWriter.println("  " + i + ": mPid=" + this.mProcessRecords.valueAt(i).mPid);
            }
            this.mOverrideRequestController.dumpInternal(printWriter);
        }
    }

    public void assertCanRequestDeviceState(int i, int i2, int i3) {
        if (isTopApp(i) && isForegroundApp(i, i2) && isStateAvailableForAppRequests(i3)) {
            return;
        }
        getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_STATE", "Permission required to request device state, or the call must come from the top app and be a device state that is available for apps to request.");
    }

    public void assertCanControlDeviceState(int i, int i2) {
        if (isTopApp(i) && isForegroundApp(i, i2)) {
            return;
        }
        getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_STATE", "Permission required to request device state, or the call must come from the top app.");
    }

    private boolean isForegroundApp(int i, int i2) {
        try {
            List runningAppProcesses = ActivityManager.getService().getRunningAppProcesses();
            for (int i3 = 0; i3 < runningAppProcesses.size(); i3++) {
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) runningAppProcesses.get(i3);
                if (runningAppProcessInfo.pid == i && runningAppProcessInfo.uid == i2 && runningAppProcessInfo.importance <= 100) {
                    return true;
                }
            }
        } catch (RemoteException e) {
            Slog.w(TAG, "am.getRunningAppProcesses() failed", e);
        }
        return false;
    }

    public boolean isTopApp(int i) {
        WindowProcessController topApp = this.mActivityTaskManagerInternal.getTopApp();
        return topApp != null && topApp.getPid() == i;
    }

    private boolean isStateAvailableForAppRequests(int i) {
        boolean contains;
        synchronized (this.mLock) {
            contains = this.mDeviceStatesAvailableForAppRequests.contains(Integer.valueOf(i));
        }
        return contains;
    }

    @GuardedBy({"mLock"})
    private void readStatesAvailableForRequestFromApps() {
        for (String str : getContext().getResources().getStringArray(R.array.config_displayWhiteBalanceLowLightAmbientBrightnesses)) {
            int integer = getContext().getResources().getInteger(getContext().getResources().getIdentifier(str, "integer", "android"));
            if (isValidState(integer)) {
                this.mDeviceStatesAvailableForAppRequests.add(Integer.valueOf(integer));
            } else {
                Slog.e(TAG, "Invalid device state was found in the configuration file. State id: " + integer);
            }
        }
    }

    private Set<Integer> readFoldedStates() {
        HashSet hashSet = new HashSet();
        for (int i : getContext().getResources().getIntArray(R.array.config_screenThresholdLevels)) {
            hashSet.add(Integer.valueOf(i));
        }
        return hashSet;
    }

    @GuardedBy({"mLock"})
    private boolean isValidState(int i) {
        for (int i2 = 0; i2 < this.mDeviceStates.size(); i2++) {
            if (i == this.mDeviceStates.valueAt(i2).getIdentifier()) {
                return true;
            }
        }
        return false;
    }

    @GuardedBy({"mLock"})
    private void handleRearDisplayBaseStateChangedLocked(int i) {
        if (isDeviceOpeningLocked(i)) {
            onStateRequestOverlayDismissedInternal(false);
        }
    }

    @GuardedBy({"mLock"})
    private boolean isDeviceOpeningLocked(final int i) {
        return this.mBaseState.filter(new Predicate() { // from class: com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$isDeviceOpeningLocked$2;
                lambda$isDeviceOpeningLocked$2 = DeviceStateManagerService.this.lambda$isDeviceOpeningLocked$2(i, (DeviceState) obj);
                return lambda$isDeviceOpeningLocked$2;
            }
        }).isPresent();
    }

    public /* synthetic */ boolean lambda$isDeviceOpeningLocked$2(int i, DeviceState deviceState) {
        return this.mFoldedDeviceStates.contains(Integer.valueOf(deviceState.getIdentifier())) && !this.mFoldedDeviceStates.contains(Integer.valueOf(i));
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DeviceStateProviderListener implements DeviceStateProvider.Listener {
        int mCurrentBaseState;

        /* synthetic */ DeviceStateProviderListener(DeviceStateManagerService deviceStateManagerService, DeviceStateProviderListenerIA deviceStateProviderListenerIA) {
            this();
        }

        private DeviceStateProviderListener() {
        }

        @Override // com.android.server.devicestate.DeviceStateProvider.Listener
        public void onSupportedDeviceStatesChanged(DeviceState[] deviceStateArr, int i) {
            if (deviceStateArr.length == 0) {
                throw new IllegalArgumentException("Supported device states must not be empty");
            }
            DeviceStateManagerService.this.updateSupportedStates(deviceStateArr, i);
        }

        @Override // com.android.server.devicestate.DeviceStateProvider.Listener
        public void onStateChanged(int i) {
            if (i < 0 || i > 255) {
                throw new IllegalArgumentException("Invalid identifier: " + i);
            }
            this.mCurrentBaseState = i;
            DeviceStateManagerService.this.setBaseState(i);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class ProcessRecord implements IBinder.DeathRecipient {
        private static final int STATUS_ACTIVE = 0;
        private static final int STATUS_CANCELED = 2;
        private static final int STATUS_SUSPENDED = 1;
        private final IDeviceStateManagerCallback mCallback;
        private final DeathListener mDeathListener;
        private final Handler mHandler;
        private final WeakHashMap<IBinder, Integer> mLastNotifiedStatus = new WeakHashMap<>();
        private final int mPid;

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public interface DeathListener {
            void onProcessDied(ProcessRecord processRecord);
        }

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        private @interface RequestStatus {
        }

        ProcessRecord(IDeviceStateManagerCallback iDeviceStateManagerCallback, int i, DeathListener deathListener, Handler handler) {
            this.mCallback = iDeviceStateManagerCallback;
            this.mPid = i;
            this.mDeathListener = deathListener;
            this.mHandler = handler;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.mDeathListener.onProcessDied(this);
        }

        public void notifyDeviceStateInfoAsync(final DeviceStateInfo deviceStateInfo) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.devicestate.DeviceStateManagerService$ProcessRecord$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DeviceStateManagerService.ProcessRecord.this.lambda$notifyDeviceStateInfoAsync$0(deviceStateInfo);
                }
            });
        }

        public /* synthetic */ void lambda$notifyDeviceStateInfoAsync$0(DeviceStateInfo deviceStateInfo) {
            try {
                this.mCallback.onDeviceStateInfoChanged(deviceStateInfo);
            } catch (RemoteException e) {
                Slog.w(DeviceStateManagerService.TAG, "Failed to notify process " + this.mPid + " that device state changed.", e);
            }
        }

        public void notifyRequestActiveAsync(final IBinder iBinder) {
            Integer num = this.mLastNotifiedStatus.get(iBinder);
            if (num == null || !(num.intValue() == 0 || num.intValue() == 2)) {
                this.mLastNotifiedStatus.put(iBinder, 0);
                this.mHandler.post(new Runnable() { // from class: com.android.server.devicestate.DeviceStateManagerService$ProcessRecord$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        DeviceStateManagerService.ProcessRecord.this.lambda$notifyRequestActiveAsync$1(iBinder);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$notifyRequestActiveAsync$1(IBinder iBinder) {
            try {
                this.mCallback.onRequestActive(iBinder);
            } catch (RemoteException e) {
                Slog.w(DeviceStateManagerService.TAG, "Failed to notify process " + this.mPid + " that request state changed.", e);
            }
        }

        public void notifyRequestCanceledAsync(final IBinder iBinder) {
            Integer num = this.mLastNotifiedStatus.get(iBinder);
            if (num == null || num.intValue() != 2) {
                this.mLastNotifiedStatus.put(iBinder, 2);
                this.mHandler.post(new Runnable() { // from class: com.android.server.devicestate.DeviceStateManagerService$ProcessRecord$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        DeviceStateManagerService.ProcessRecord.this.lambda$notifyRequestCanceledAsync$2(iBinder);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$notifyRequestCanceledAsync$2(IBinder iBinder) {
            try {
                this.mCallback.onRequestCanceled(iBinder);
            } catch (RemoteException e) {
                Slog.w(DeviceStateManagerService.TAG, "Failed to notify process " + this.mPid + " that request state changed.", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class BinderService extends IDeviceStateManager.Stub {
        /* synthetic */ BinderService(DeviceStateManagerService deviceStateManagerService, BinderServiceIA binderServiceIA) {
            this();
        }

        private BinderService() {
        }

        public DeviceStateInfo getDeviceStateInfo() {
            DeviceStateInfo deviceStateInfoLocked;
            synchronized (DeviceStateManagerService.this.mLock) {
                deviceStateInfoLocked = DeviceStateManagerService.this.getDeviceStateInfoLocked();
            }
            return deviceStateInfoLocked;
        }

        public void registerCallback(IDeviceStateManagerCallback iDeviceStateManagerCallback) {
            if (iDeviceStateManagerCallback == null) {
                throw new IllegalArgumentException("Device state callback must not be null.");
            }
            int callingPid = Binder.getCallingPid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DeviceStateManagerService.this.registerProcess(callingPid, iDeviceStateManagerCallback);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void requestState(IBinder iBinder, int i, int i2) {
            int callingPid = Binder.getCallingPid();
            int callingUid = Binder.getCallingUid();
            DeviceStateManagerService.this.assertCanRequestDeviceState(callingPid, callingUid, i);
            if (iBinder == null) {
                throw new IllegalArgumentException("Request token must not be null.");
            }
            boolean z = DeviceStateManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.CONTROL_DEVICE_STATE") == 0;
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DeviceStateManagerService.this.requestStateInternal(i, i2, callingPid, callingUid, iBinder, z);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void cancelStateRequest() {
            int callingPid = Binder.getCallingPid();
            DeviceStateManagerService.this.assertCanControlDeviceState(callingPid, Binder.getCallingUid());
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DeviceStateManagerService.this.cancelStateRequestInternal(callingPid);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void requestBaseStateOverride(IBinder iBinder, int i, int i2) {
            int callingPid = Binder.getCallingPid();
            int callingUid = Binder.getCallingUid();
            DeviceStateManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_STATE", "Permission required to control base state of device.");
            if (iBinder == null) {
                throw new IllegalArgumentException("Request token must not be null.");
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DeviceStateManagerService.this.requestBaseStateOverrideInternal(i, i2, callingPid, callingUid, iBinder);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void cancelBaseStateOverride() {
            int callingPid = Binder.getCallingPid();
            DeviceStateManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_STATE", "Permission required to control base state of device.");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DeviceStateManagerService.this.cancelBaseStateOverrideInternal(callingPid);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void onStateRequestOverlayDismissed(boolean z) {
            DeviceStateManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_STATE", "CONTROL_DEVICE_STATE permission required to control the state request overlay");
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                DeviceStateManagerService.this.onStateRequestOverlayDismissedInternal(z);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
            new DeviceStateManagerShellCommand(DeviceStateManagerService.this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }

        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(DeviceStateManagerService.this.getContext(), DeviceStateManagerService.TAG, printWriter)) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    DeviceStateManagerService.this.dumpInternal(printWriter);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }
    }

    void notifyKeyguardShowOrSleepUnLocked(boolean z) {
        this.mDeviceStatePolicy.getDeviceStateProvider().notifyKeyguardShowOrSleep(z);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class LocalService extends DeviceStateManagerInternal {
        /* synthetic */ LocalService(DeviceStateManagerService deviceStateManagerService, LocalServiceIA localServiceIA) {
            this();
        }

        private LocalService() {
        }

        public int[] getSupportedStateIdentifiers() {
            int[] supportedStateIdentifiersLocked;
            synchronized (DeviceStateManagerService.this.mLock) {
                supportedStateIdentifiersLocked = DeviceStateManagerService.this.getSupportedStateIdentifiersLocked();
            }
            return supportedStateIdentifiersLocked;
        }

        public void notifyKeyguardShowOrSleep(boolean z) {
            DeviceStateManagerService.this.notifyKeyguardShowOrSleepUnLocked(z);
        }

        public void enableDeviceStateAfterBoot(boolean z) {
            synchronized (DeviceStateManagerService.this.mLock) {
                DeviceStateManagerService.this.mDeviceStatesEnabled = z;
                DeviceStateManagerService.this.mDeviceStateManagerServiceExt.enableDeviceStateAfterBoot(z);
            }
        }

        public void setSecondaryDisplayKeepTurnOn(boolean z) {
            if (DeviceStateManagerService.this.mBaseState.isEmpty()) {
                return;
            }
            int identifier = ((DeviceState) DeviceStateManagerService.this.mBaseState.get()).getIdentifier();
            if (z && identifier != 0 && identifier != 1) {
                if (DeviceStateManagerService.this.getOverrideState().isPresent() && DeviceStateManagerService.this.getOverrideState().get().getIdentifier() == 99) {
                    return;
                }
                DeviceStateManagerService.this.requestStateInternal(99, 0, Process.myPid(), Process.myUid(), new Binder(), true);
            }
            if (!z && DeviceStateManagerService.this.getOverrideState().isPresent() && DeviceStateManagerService.this.getOverrideState().get().getIdentifier() == 99) {
                DeviceStateManagerService.this.cancelStateRequestInternal(Process.myPid());
            }
        }
    }

    private WindowManagerInternal getWindowManagerInternal() {
        if (this.mWindowManagerInternal == null) {
            this.mWindowManagerInternal = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
        }
        return this.mWindowManagerInternal;
    }

    private boolean shouldSimultaneousDisplay() {
        WindowManagerInternal windowManagerInternal;
        if (this.mWindowManagerInternal == null) {
            getWindowManagerInternal();
        }
        if ((this.mBaseState.get().getIdentifier() != 2 && this.mBaseState.get().getIdentifier() != 3) || (windowManagerInternal = this.mWindowManagerInternal) == null || !windowManagerInternal.keepSimultaneousDisplay()) {
            return false;
        }
        if (!getOverrideState().isEmpty() || !this.mBaseState.isPresent()) {
            return true;
        }
        requestStateInternal(99, 1, Process.myPid(), Process.myUid(), new Binder(), true);
        return true;
    }

    private boolean shouldCancelOverrideRequest() {
        if (this.mBaseState.get().getIdentifier() == 0 && getOverrideState().isPresent() && getOverrideState().get().getIdentifier() == 101) {
            return false;
        }
        if (this.mBaseState.get().hasFlag(1)) {
            return true;
        }
        return this.mBaseState.get().getIdentifier() == 3 && getOverrideState().isPresent() && getOverrideState().get().getIdentifier() == 101;
    }

    public IDeviceStateManagerServiceWrapper getWrapper() {
        return this.mWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class DeviceStateManagerServiceWrapper implements IDeviceStateManagerServiceWrapper {
        /* synthetic */ DeviceStateManagerServiceWrapper(DeviceStateManagerService deviceStateManagerService, DeviceStateManagerServiceWrapperIA deviceStateManagerServiceWrapperIA) {
            this();
        }

        private DeviceStateManagerServiceWrapper() {
        }

        @Override // com.android.server.devicestate.IDeviceStateManagerServiceWrapper
        public Optional<DeviceState> getCommittedState() {
            return DeviceStateManagerService.this.mCommittedState;
        }

        @Override // com.android.server.devicestate.IDeviceStateManagerServiceWrapper
        public Optional<DeviceState> getBaseState() {
            return DeviceStateManagerService.this.mBaseState;
        }

        @Override // com.android.server.devicestate.IDeviceStateManagerServiceWrapper
        public Optional<DeviceState> getStateLocked(int i) {
            return DeviceStateManagerService.this.getStateLocked(i);
        }
    }

    @GuardedBy({"mLock"})
    public boolean shouldCancelOverrideRequestWhenRequesterNotOnTop() {
        if (this.mActiveOverride.isEmpty()) {
            return false;
        }
        return this.mDeviceStates.get(this.mActiveOverride.get().getRequestedState()).hasFlag(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class OverrideRequestTaskStackListener extends TaskStackListener {
        /* synthetic */ OverrideRequestTaskStackListener(DeviceStateManagerService deviceStateManagerService, OverrideRequestTaskStackListenerIA overrideRequestTaskStackListenerIA) {
            this();
        }

        private OverrideRequestTaskStackListener() {
        }

        public void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
            synchronized (DeviceStateManagerService.this.mLock) {
                if (DeviceStateManagerService.this.shouldCancelOverrideRequestWhenRequesterNotOnTop()) {
                    OverrideRequest overrideRequest = (OverrideRequest) DeviceStateManagerService.this.mActiveOverride.get();
                    if (!DeviceStateManagerService.this.isTopApp(overrideRequest.getPid())) {
                        DeviceStateManagerService.this.mOverrideRequestController.cancelRequest(overrideRequest);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class OverrideRequestScreenObserver implements ActivityTaskManagerInternal.ScreenObserver {
        /* synthetic */ OverrideRequestScreenObserver(DeviceStateManagerService deviceStateManagerService, OverrideRequestScreenObserverIA overrideRequestScreenObserverIA) {
            this();
        }

        private OverrideRequestScreenObserver() {
        }

        public void onAwakeStateChanged(boolean z) {
            synchronized (DeviceStateManagerService.this.mLock) {
                if (!z) {
                    if (DeviceStateManagerService.this.shouldCancelOverrideRequestWhenRequesterNotOnTop()) {
                        DeviceStateManagerService.this.mOverrideRequestController.cancelRequest((OverrideRequest) DeviceStateManagerService.this.mActiveOverride.get());
                    }
                }
            }
        }

        public void onKeyguardStateChanged(boolean z) {
            synchronized (DeviceStateManagerService.this.mLock) {
                if (z) {
                    if (DeviceStateManagerService.this.shouldCancelOverrideRequestWhenRequesterNotOnTop()) {
                        DeviceStateManagerService.this.mOverrideRequestController.cancelRequest((OverrideRequest) DeviceStateManagerService.this.mActiveOverride.get());
                    }
                }
            }
        }
    }
}
