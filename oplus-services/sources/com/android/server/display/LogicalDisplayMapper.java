package com.android.server.display;

import android.R;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.IndentingPrintWriter;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.DisplayAddress;
import android.view.DisplayInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.display.DisplayDeviceRepository;
import com.android.server.display.DisplayManagerService;
import com.android.server.display.layout.DisplayIdProducer;
import com.android.server.display.layout.Layout;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class LogicalDisplayMapper implements DisplayDeviceRepository.Listener {
    private static final int DEVICE_STATE_CLOSE = 0;
    private static final int DEVICE_STATE_HALF = 1;
    private static final int DEVICE_STATE_OPEN = 2;
    public static final int DISPLAY_GROUP_EVENT_ADDED = 1;
    public static final int DISPLAY_GROUP_EVENT_CHANGED = 2;
    public static final int DISPLAY_GROUP_EVENT_REMOVED = 3;
    public static final int LOGICAL_DISPLAY_EVENT_ADDED = 1;
    public static final int LOGICAL_DISPLAY_EVENT_CHANGED = 2;
    public static final int LOGICAL_DISPLAY_EVENT_DEVICE_STATE_TRANSITION = 6;
    public static final int LOGICAL_DISPLAY_EVENT_FRAME_RATE_OVERRIDES_CHANGED = 5;
    public static final int LOGICAL_DISPLAY_EVENT_HDR_SDR_RATIO_CHANGED = 7;
    public static final int LOGICAL_DISPLAY_EVENT_REMOVED = 3;
    public static final int LOGICAL_DISPLAY_EVENT_SWAPPED = 4;
    private static final int MSG_TRANSITION_TO_PENDING_DEVICE_STATE = 1;
    private static final String TAG = "LogicalDisplayMapper";
    private static final int TIMEOUT_STATE_TRANSITION_MILLIS = 500;
    private static final int UPDATE_STATE_NEW = 0;
    private static final int UPDATE_STATE_TRANSITION = 1;
    private static final int UPDATE_STATE_UPDATED = 2;
    private boolean mBootCompleted;
    private Context mContext;
    private Layout mCurrentLayout;
    private final SparseIntArray mDeviceDisplayGroupIds;
    private int mDeviceState;
    private int mDeviceStateToBeAppliedAfterBoot;
    private final DeviceStateToLayoutMap mDeviceStateToLayoutMap;
    private final SparseBooleanArray mDeviceStatesOnWhichToSleep;
    private final SparseBooleanArray mDeviceStatesOnWhichToWakeUp;
    private final DisplayDeviceRepository mDisplayDeviceRepo;
    private final ArrayMap<String, Integer> mDisplayGroupIdsByName;
    private final SparseArray<DisplayGroup> mDisplayGroups;
    private final SparseIntArray mDisplayGroupsToUpdate;
    private final LogicalDisplayMapperHandler mHandler;
    private final DisplayIdProducer mIdProducer;
    private boolean mInteractive;
    private final Listener mListener;
    private ILogicalDisplayMapperExt mLogicalDisplayMapperExt;
    private final SparseArray<LogicalDisplay> mLogicalDisplays;
    private final SparseIntArray mLogicalDisplaysToUpdate;
    private int mNextBuiltInDisplayId;
    private int mNextNonDefaultGroupId;
    private int mPendingDeviceState;
    private final PowerManager mPowerManager;
    private final boolean mSingleDisplayDemoMode;
    private final boolean mSupportsConcurrentInternalDisplays;
    private final DisplayManagerService.SyncRoot mSyncRoot;
    private final DisplayInfo mTempDisplayInfo;
    private final DisplayInfo mTempNonOverrideDisplayInfo;
    private final SparseIntArray mUpdatedDisplayGroups;
    private final SparseIntArray mUpdatedLogicalDisplays;
    private final ArrayMap<String, Integer> mVirtualDeviceDisplayMapping;
    private IOplusLogicDisplayMapperWrapper mWrapper;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static int sNextNonDefaultDisplayId = 1;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Listener {
        void onDisplayGroupEventLocked(int i, int i2);

        void onLogicalDisplayEventLocked(LogicalDisplay logicalDisplay, int i);

        void onTraversalRequested();
    }

    private int assignLayerStackLocked(int i) {
        return i;
    }

    private String displayEventToString(int i) {
        switch (i) {
            case 1:
                return "added";
            case 2:
                return "changed";
            case 3:
                return "removed";
            case 4:
                return "swapped";
            case 5:
                return "framerate_override";
            case 6:
                return "transition";
            case 7:
                return "hdr_sdr_ratio_changed";
            default:
                return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$new$0(boolean z) {
        if (z) {
            return 0;
        }
        int i = sNextNonDefaultDisplayId;
        sNextNonDefaultDisplayId = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LogicalDisplayMapper(Context context, DisplayDeviceRepository displayDeviceRepository, Listener listener, DisplayManagerService.SyncRoot syncRoot, Handler handler) {
        this(context, displayDeviceRepository, listener, syncRoot, handler, new DeviceStateToLayoutMap(new DisplayIdProducer() { // from class: com.android.server.display.LogicalDisplayMapper$$ExternalSyntheticLambda3
            @Override // com.android.server.display.layout.DisplayIdProducer
            public final int getId(boolean z) {
                int lambda$new$1;
                lambda$new$1 = LogicalDisplayMapper.lambda$new$1(z);
                return lambda$new$1;
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$new$1(boolean z) {
        if (z) {
            return 0;
        }
        int i = sNextNonDefaultDisplayId;
        sNextNonDefaultDisplayId = i + 1;
        return i;
    }

    LogicalDisplayMapper(Context context, DisplayDeviceRepository displayDeviceRepository, Listener listener, DisplayManagerService.SyncRoot syncRoot, Handler handler, DeviceStateToLayoutMap deviceStateToLayoutMap) {
        this.mTempDisplayInfo = new DisplayInfo();
        this.mTempNonOverrideDisplayInfo = new DisplayInfo();
        this.mLogicalDisplays = new SparseArray<>();
        this.mNextBuiltInDisplayId = 4096;
        this.mDisplayGroups = new SparseArray<>();
        this.mDeviceDisplayGroupIds = new SparseIntArray();
        this.mDisplayGroupIdsByName = new ArrayMap<>();
        this.mUpdatedLogicalDisplays = new SparseIntArray();
        this.mUpdatedDisplayGroups = new SparseIntArray();
        this.mLogicalDisplaysToUpdate = new SparseIntArray();
        this.mDisplayGroupsToUpdate = new SparseIntArray();
        this.mVirtualDeviceDisplayMapping = new ArrayMap<>();
        this.mNextNonDefaultGroupId = 1;
        this.mIdProducer = new DisplayIdProducer() { // from class: com.android.server.display.LogicalDisplayMapper$$ExternalSyntheticLambda0
            @Override // com.android.server.display.layout.DisplayIdProducer
            public final int getId(boolean z) {
                int lambda$new$0;
                lambda$new$0 = LogicalDisplayMapper.lambda$new$0(z);
                return lambda$new$0;
            }
        };
        this.mCurrentLayout = null;
        this.mDeviceState = -1;
        this.mPendingDeviceState = -1;
        this.mDeviceStateToBeAppliedAfterBoot = -1;
        this.mBootCompleted = false;
        this.mLogicalDisplayMapperExt = (ILogicalDisplayMapperExt) ExtLoader.type(ILogicalDisplayMapperExt.class).base(this).create();
        this.mWrapper = new OplusLogicDisplayMapperWrapper();
        this.mSyncRoot = syncRoot;
        PowerManager powerManager = (PowerManager) context.getSystemService(PowerManager.class);
        this.mPowerManager = powerManager;
        this.mInteractive = powerManager.isInteractive();
        this.mHandler = new LogicalDisplayMapperHandler(handler.getLooper());
        this.mDisplayDeviceRepo = displayDeviceRepository;
        this.mListener = listener;
        this.mSingleDisplayDemoMode = SystemProperties.getBoolean("persist.demo.singledisplay", false);
        this.mSupportsConcurrentInternalDisplays = context.getResources().getBoolean(17891852);
        this.mDeviceStatesOnWhichToWakeUp = toSparseBooleanArray(context.getResources().getIntArray(R.array.config_dropboxLowPriorityTags));
        this.mDeviceStatesOnWhichToSleep = toSparseBooleanArray(context.getResources().getIntArray(R.array.config_display_no_service_when_sim_unready));
        displayDeviceRepository.addListener(this);
        this.mDeviceStateToLayoutMap = deviceStateToLayoutMap;
        this.mContext = context;
        this.mLogicalDisplayMapperExt.setDisplayLayout(deviceStateToLayoutMap.getLayoutMap());
        this.mLogicalDisplayMapperExt.initDvMultiDisplay();
    }

    @Override // com.android.server.display.DisplayDeviceRepository.Listener
    public void onDisplayDeviceEventLocked(DisplayDevice displayDevice, int i) {
        if (i == 1) {
            if (DEBUG) {
                Slog.d(TAG, "Display device added: " + displayDevice.getDisplayDeviceInfoLocked());
            }
            handleDisplayDeviceAddedLocked(displayDevice);
            return;
        }
        if (i != 3) {
            return;
        }
        if (DEBUG) {
            Slog.d(TAG, "Display device removed: " + displayDevice.getDisplayDeviceInfoLocked());
        }
        handleDisplayDeviceRemovedLocked(displayDevice);
        updateLogicalDisplaysLocked();
    }

    @Override // com.android.server.display.DisplayDeviceRepository.Listener
    public void onDisplayDeviceChangedLocked(DisplayDevice displayDevice, int i) {
        if (DEBUG) {
            Slog.d(TAG, "Display device changed: " + displayDevice.getDisplayDeviceInfoLocked());
        }
        finishStateTransitionLocked(false);
        updateLogicalDisplaysLocked(i);
    }

    @Override // com.android.server.display.DisplayDeviceRepository.Listener
    public void onTraversalRequested() {
        this.mListener.onTraversalRequested();
    }

    public LogicalDisplay getDisplayLocked(int i) {
        return getDisplayLocked(i, true);
    }

    public LogicalDisplay getDisplayLocked(int i, boolean z) {
        LogicalDisplay logicalDisplay = this.mLogicalDisplays.get(i);
        if (logicalDisplay == null || logicalDisplay.isEnabledLocked() || z) {
            return logicalDisplay;
        }
        return null;
    }

    public LogicalDisplay getDisplayLocked(DisplayDevice displayDevice) {
        return getDisplayLocked(displayDevice, true);
    }

    public LogicalDisplay getDisplayLocked(DisplayDevice displayDevice, boolean z) {
        if (displayDevice == null) {
            return null;
        }
        int size = this.mLogicalDisplays.size();
        for (int i = 0; i < size; i++) {
            LogicalDisplay valueAt = this.mLogicalDisplays.valueAt(i);
            if (valueAt.getPrimaryDisplayDeviceLocked() == displayDevice) {
                if (valueAt.isEnabledLocked() || z) {
                    return valueAt;
                }
                return null;
            }
        }
        return null;
    }

    public int[] getDisplayIdsLocked(int i, boolean z) {
        int size = this.mLogicalDisplays.size();
        int[] iArr = new int[size];
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            LogicalDisplay valueAt = this.mLogicalDisplays.valueAt(i3);
            if ((valueAt.isEnabledLocked() || z) && valueAt.getDisplayInfoLocked().hasAccess(i) && !this.mLogicalDisplayMapperExt.filterSecondaryDisplay(this.mContext, valueAt.getDisplayIdLocked(), 0, i)) {
                iArr[i2] = this.mLogicalDisplays.keyAt(i3);
                i2++;
            }
        }
        return i2 != size ? Arrays.copyOfRange(iArr, 0, i2) : iArr;
    }

    public void forEachLocked(Consumer<LogicalDisplay> consumer) {
        forEachLocked(consumer, false);
    }

    public void forEachLocked(Consumer<LogicalDisplay> consumer, boolean z) {
        int size = this.mLogicalDisplays.size();
        for (int i = 0; i < size; i++) {
            LogicalDisplay valueAt = this.mLogicalDisplays.valueAt(i);
            if (valueAt.isEnabledLocked() || z) {
                consumer.accept(valueAt);
            }
        }
    }

    @VisibleForTesting
    public int getDisplayGroupIdFromDisplayIdLocked(int i) {
        LogicalDisplay displayLocked = getDisplayLocked(i);
        if (displayLocked == null) {
            return -1;
        }
        int size = this.mDisplayGroups.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (this.mDisplayGroups.valueAt(i2).containsLocked(displayLocked)) {
                return this.mDisplayGroups.keyAt(i2);
            }
        }
        return -1;
    }

    public DisplayGroup getDisplayGroupLocked(int i) {
        return this.mDisplayGroups.get(i);
    }

    public void setPowerHandler(Handler handler) {
        this.mLogicalDisplayMapperExt.setPowerHandler(handler);
    }

    public DisplayInfo getDisplayInfoForStateLocked(int i, int i2) {
        Layout.Display byId;
        Layout layout = this.mDeviceStateToLayoutMap.get(i);
        if (layout == null || (byId = layout.getById(i2)) == null) {
            return null;
        }
        DisplayDevice byAddressLocked = this.mDisplayDeviceRepo.getByAddressLocked(byId.getAddress());
        if (byAddressLocked == null) {
            Slog.w(TAG, "The display device (" + byId.getAddress() + "), is not available for the display state " + this.mDeviceState);
            return null;
        }
        LogicalDisplay displayLocked = getDisplayLocked(byAddressLocked, true);
        if (displayLocked == null) {
            Slog.w(TAG, "The logical display associated with address (" + byId.getAddress() + "), is not available for the display state " + this.mDeviceState);
            return null;
        }
        DisplayInfo displayInfo = new DisplayInfo(displayLocked.getDisplayInfoLocked());
        displayInfo.displayId = i2;
        return displayInfo;
    }

    public boolean isRemapDisabledSecondaryDisplayId(int i) {
        return this.mLogicalDisplayMapperExt.isRemapDisabledSecondaryDisplayId(i);
    }

    public void dumpLocked(PrintWriter printWriter) {
        printWriter.println("LogicalDisplayMapper:");
        PrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("mSingleDisplayDemoMode=" + this.mSingleDisplayDemoMode);
        indentingPrintWriter.println("mCurrentLayout=" + this.mCurrentLayout);
        indentingPrintWriter.println("mDeviceStatesOnWhichToWakeUp=" + this.mDeviceStatesOnWhichToWakeUp);
        indentingPrintWriter.println("mDeviceStatesOnWhichToSleep=" + this.mDeviceStatesOnWhichToSleep);
        indentingPrintWriter.println("mInteractive=" + this.mInteractive);
        indentingPrintWriter.println("mBootCompleted=" + this.mBootCompleted);
        indentingPrintWriter.println();
        indentingPrintWriter.println("mDeviceState=" + this.mDeviceState);
        indentingPrintWriter.println("mPendingDeviceState=" + this.mPendingDeviceState);
        indentingPrintWriter.println("mDeviceStateToBeAppliedAfterBoot=" + this.mDeviceStateToBeAppliedAfterBoot);
        indentingPrintWriter.println("mDeviceState=" + this.mDeviceState);
        indentingPrintWriter.println("mPendingDeviceState=" + this.mPendingDeviceState);
        int size = this.mLogicalDisplays.size();
        indentingPrintWriter.println();
        indentingPrintWriter.println("Logical Displays: size=" + size);
        for (int i = 0; i < size; i++) {
            int keyAt = this.mLogicalDisplays.keyAt(i);
            LogicalDisplay valueAt = this.mLogicalDisplays.valueAt(i);
            indentingPrintWriter.println("Display " + keyAt + ":");
            indentingPrintWriter.increaseIndent();
            valueAt.dumpLocked(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
        }
        this.mDeviceStateToLayoutMap.dumpLocked(indentingPrintWriter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void associateDisplayDeviceWithVirtualDevice(DisplayDevice displayDevice, int i) {
        this.mVirtualDeviceDisplayMapping.put(displayDevice.getUniqueId(), Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDeviceStateLocked(int i, boolean z) {
        int interceptBaseDeviceState = this.mLogicalDisplayMapperExt.interceptBaseDeviceState(this.mPendingDeviceState, i, z);
        if (interceptBaseDeviceState == -1) {
            return;
        }
        if (!this.mBootCompleted) {
            if (DEBUG) {
                Slog.d(TAG, "Postponing transition to state: " + this.mPendingDeviceState + " until boot is completed");
            }
            this.mDeviceStateToBeAppliedAfterBoot = interceptBaseDeviceState;
            return;
        }
        Slog.i(TAG, "Requesting Transition to state: " + interceptBaseDeviceState + ", from state=" + this.mDeviceState + ", interactive=" + this.mInteractive + ", mBootCompleted=" + this.mBootCompleted);
        resetLayoutLocked(this.mDeviceState, interceptBaseDeviceState, true);
        this.mPendingDeviceState = interceptBaseDeviceState;
        this.mDeviceStateToBeAppliedAfterBoot = -1;
        boolean shouldDeviceBeWoken = shouldDeviceBeWoken(interceptBaseDeviceState, this.mDeviceState, this.mInteractive, this.mBootCompleted);
        boolean shouldDeviceBePutToSleep = shouldDeviceBePutToSleep(this.mPendingDeviceState, this.mDeviceState, z, this.mInteractive, this.mBootCompleted);
        Slog.d(TAG, "setDeviceStateLocked state=" + this.mDeviceState + "->" + this.mPendingDeviceState + " wake=" + this.mDeviceStatesOnWhichToWakeUp.get(this.mDeviceState) + "->" + this.mDeviceStatesOnWhichToWakeUp.get(this.mPendingDeviceState) + " sleep=" + this.mDeviceStatesOnWhichToSleep.get(this.mDeviceState) + "->" + this.mDeviceStatesOnWhichToSleep.get(this.mPendingDeviceState) + " wakeDevice=" + shouldDeviceBeWoken + " sleepDevice=" + shouldDeviceBePutToSleep + " mInteractive=" + this.mInteractive + " isOverrideActive=" + z + " displaysOff=" + areAllTransitioningDisplaysOffLocked());
        int i2 = this.mPendingDeviceState;
        if (i2 == 3 || i2 == 0) {
            this.mLogicalDisplayMapperExt.updateDvsParam(i2);
        }
        if (areAllTransitioningDisplaysOffLocked() && !shouldDeviceBeWoken && !shouldDeviceBePutToSleep && (!this.mLogicalDisplayMapperExt.hasFoldRemapDisplayDisableFeature() || !this.mInteractive || this.mPendingDeviceState == 99)) {
            transitionToPendingStateLocked();
            this.mLogicalDisplayMapperExt.transitionToPendingStateLocked();
            return;
        }
        if (DEBUG) {
            Slog.d(TAG, "Postponing transition to state: " + this.mPendingDeviceState);
        }
        updateLogicalDisplaysLocked();
        this.mLogicalDisplayMapperExt.fastFreezeOnWakeup(this.mDeviceState, this.mPendingDeviceState);
        this.mLogicalDisplayMapperExt.setUxOnWakeup(this.mDeviceState, this.mPendingDeviceState);
        this.mLogicalDisplayMapperExt.screenOnCpuBoost(this.mDeviceState, this.mPendingDeviceState);
        if (shouldDeviceBeWoken || shouldDeviceBePutToSleep) {
            if (shouldDeviceBeWoken) {
                this.mHandler.post(new Runnable() { // from class: com.android.server.display.LogicalDisplayMapper$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        LogicalDisplayMapper.this.lambda$setDeviceStateLocked$2();
                    }
                });
            } else if (shouldDeviceBePutToSleep) {
                this.mHandler.post(new Runnable() { // from class: com.android.server.display.LogicalDisplayMapper$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        LogicalDisplayMapper.this.lambda$setDeviceStateLocked$3();
                    }
                });
            }
        }
        if (!this.mLogicalDisplayMapperExt.hasFoldRemapDisplayDisableFeature()) {
            this.mHandler.sendEmptyMessageDelayed(1, 500L);
        } else {
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessageDelayed(1, 500L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDeviceStateLocked$2() {
        this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 12, "server.display:unfold");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDeviceStateLocked$3() {
        this.mPowerManager.goToSleep(SystemClock.uptimeMillis(), 13, 2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBootCompleted() {
        synchronized (this.mSyncRoot) {
            this.mBootCompleted = true;
            requestDisplaySwitchOff();
            int i = this.mDeviceStateToBeAppliedAfterBoot;
            if (i != -1) {
                setDeviceStateLocked(i, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onEarlyInteractivityChange(boolean z) {
        synchronized (this.mSyncRoot) {
            if (this.mInteractive != z) {
                this.mInteractive = z;
                finishStateTransitionLocked(false);
            }
        }
    }

    @VisibleForTesting
    boolean shouldDeviceBeWoken(int i, int i2, boolean z, boolean z2) {
        return this.mDeviceStatesOnWhichToWakeUp.get(i) && !this.mDeviceStatesOnWhichToWakeUp.get(i2) && !z && z2;
    }

    @VisibleForTesting
    boolean shouldDeviceBePutToSleep(int i, int i2, boolean z, boolean z2, boolean z3) {
        return i2 != -1 && this.mDeviceStatesOnWhichToSleep.get(i) && !this.mDeviceStatesOnWhichToSleep.get(i2) && !z && z2 && z3;
    }

    private boolean areAllTransitioningDisplaysOffLocked() {
        DisplayDevice primaryDisplayDeviceLocked;
        int size = this.mLogicalDisplays.size();
        for (int i = 0; i < size; i++) {
            LogicalDisplay valueAt = this.mLogicalDisplays.valueAt(i);
            if (valueAt.isInTransitionLocked() && (primaryDisplayDeviceLocked = valueAt.getPrimaryDisplayDeviceLocked()) != null && primaryDisplayDeviceLocked.getDisplayDeviceInfoLocked().state != 1) {
                return false;
            }
        }
        return true;
    }

    private void transitionToPendingStateLocked() {
        resetLayoutLocked(this.mDeviceState, this.mPendingDeviceState, false);
        this.mDeviceState = this.mPendingDeviceState;
        this.mPendingDeviceState = -1;
        applyLayoutLocked();
        updateLogicalDisplaysLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishStateTransitionLocked(boolean z) {
        int i = this.mPendingDeviceState;
        if (i == -1) {
            return;
        }
        boolean z2 = false;
        boolean z3 = this.mDeviceStatesOnWhichToWakeUp.get(i) && !this.mDeviceStatesOnWhichToWakeUp.get(this.mDeviceState) && !this.mInteractive && this.mBootCompleted;
        boolean z4 = this.mDeviceStatesOnWhichToSleep.get(this.mPendingDeviceState) && !this.mDeviceStatesOnWhichToSleep.get(this.mDeviceState) && this.mInteractive && this.mBootCompleted;
        boolean areAllTransitioningDisplaysOffLocked = areAllTransitioningDisplaysOffLocked();
        if (areAllTransitioningDisplaysOffLocked && !z3 && !z4) {
            z2 = true;
        }
        Slog.d(TAG, "finishStateTransitionLocked state=" + this.mDeviceState + "->" + this.mPendingDeviceState + " wake=" + this.mDeviceStatesOnWhichToWakeUp.get(this.mDeviceState) + "->" + this.mDeviceStatesOnWhichToWakeUp.get(this.mPendingDeviceState) + " sleep=" + this.mDeviceStatesOnWhichToSleep.get(this.mDeviceState) + "->" + this.mDeviceStatesOnWhichToSleep.get(this.mPendingDeviceState) + " waitingWake=" + z3 + " waitingSleep=" + z4 + " mInteractive=" + this.mInteractive + " force=" + z + " displaysOff=" + areAllTransitioningDisplaysOffLocked + " isReadyToTransition=" + z2);
        if (z2 || z) {
            transitionToPendingStateLocked();
            this.mHandler.removeMessages(1);
            this.mLogicalDisplayMapperExt.transitionToPendingStateLocked();
        } else if (DEBUG) {
            Slog.d(TAG, "Not yet ready to transition to state=" + this.mPendingDeviceState + " with displays-off=" + areAllTransitioningDisplaysOffLocked + ", force=" + z + ", mInteractive=" + this.mInteractive + ", isReady=" + z2);
        }
    }

    private void handleDisplayDeviceAddedLocked(DisplayDevice displayDevice) {
        if ((displayDevice.getDisplayDeviceInfoLocked().flags & 1) != 0) {
            initializeDefaultDisplayDeviceLocked(displayDevice);
        }
        DisplayDeviceInfo displayDeviceInfoLocked = displayDevice.getDisplayDeviceInfoLocked();
        createNewLogicalDisplayLocked(displayDevice, Layout.assignDisplayIdLocked(false, displayDeviceInfoLocked.address));
        this.mLogicalDisplayMapperExt.setMainDisplayUniqueId(displayDeviceInfoLocked.uniqueId);
        applyLayoutLocked();
        updateLogicalDisplaysLocked();
    }

    private void handleDisplayDeviceRemovedLocked(DisplayDevice displayDevice) {
        Layout layout = this.mDeviceStateToLayoutMap.get(-1);
        Layout.Display byId = layout.getById(0);
        if (byId == null) {
            return;
        }
        DisplayDeviceInfo displayDeviceInfoLocked = displayDevice.getDisplayDeviceInfoLocked();
        this.mVirtualDeviceDisplayMapping.remove(displayDevice.getUniqueId());
        if (byId.getAddress().equals(displayDeviceInfoLocked.address)) {
            layout.removeDisplayLocked(0);
            for (int i = 0; i < this.mLogicalDisplays.size(); i++) {
                DisplayDevice primaryDisplayDeviceLocked = this.mLogicalDisplays.valueAt(i).getPrimaryDisplayDeviceLocked();
                if (primaryDisplayDeviceLocked != null) {
                    DisplayDeviceInfo displayDeviceInfoLocked2 = primaryDisplayDeviceLocked.getDisplayDeviceInfoLocked();
                    if ((displayDeviceInfoLocked2.flags & 1) != 0 && !displayDeviceInfoLocked2.address.equals(displayDeviceInfoLocked.address)) {
                        layout.createDefaultDisplayLocked(displayDeviceInfoLocked2.address, this.mIdProducer);
                        applyLayoutLocked();
                        return;
                    }
                }
            }
        }
    }

    private void updateLogicalDisplaysLocked() {
        updateLogicalDisplaysLocked(-1);
    }

    private void updateLogicalDisplaysLocked(int i) {
        int size = this.mLogicalDisplays.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            }
            int keyAt = this.mLogicalDisplays.keyAt(size);
            LogicalDisplay valueAt = this.mLogicalDisplays.valueAt(size);
            assignDisplayGroupLocked(valueAt);
            boolean isDirtyLocked = valueAt.isDirtyLocked();
            this.mTempDisplayInfo.copyFrom(valueAt.getDisplayInfoLocked());
            valueAt.getNonOverrideDisplayInfoLocked(this.mTempNonOverrideDisplayInfo);
            valueAt.updateLocked(this.mDisplayDeviceRepo);
            DisplayInfo displayInfoLocked = valueAt.getDisplayInfoLocked();
            int i2 = this.mUpdatedLogicalDisplays.get(keyAt, 0);
            boolean z = i2 != 0;
            if (!valueAt.isValidLocked()) {
                this.mUpdatedLogicalDisplays.delete(keyAt);
                DisplayGroup displayGroupLocked = getDisplayGroupLocked(getDisplayGroupIdFromDisplayIdLocked(keyAt));
                if (displayGroupLocked != null) {
                    displayGroupLocked.removeDisplayLocked(valueAt);
                }
                if (z) {
                    Slog.i(TAG, "Removing display: " + keyAt);
                    this.mLogicalDisplaysToUpdate.put(keyAt, 3);
                } else {
                    this.mLogicalDisplays.removeAt(size);
                }
                Slog.d(TAG, "invalid display:" + valueAt.toStringMini());
            } else {
                if (!z) {
                    Slog.i(TAG, "Adding new display: " + keyAt + ": " + displayInfoLocked);
                    this.mLogicalDisplaysToUpdate.put(keyAt, 1);
                } else if (!TextUtils.equals(this.mTempDisplayInfo.uniqueId, displayInfoLocked.uniqueId)) {
                    Slog.d(TAG, "uniqueId is not equal so send swapped event");
                    this.mLogicalDisplaysToUpdate.put(keyAt, 4);
                } else if (isDirtyLocked || !this.mTempDisplayInfo.equals(displayInfoLocked)) {
                    if (i == 8) {
                        this.mLogicalDisplaysToUpdate.put(keyAt, 7);
                    } else {
                        this.mLogicalDisplaysToUpdate.put(keyAt, 2);
                    }
                    this.mLogicalDisplayMapperExt.resetPowerModeChanged(valueAt);
                    this.mUpdatedLogicalDisplays.put(keyAt, 2);
                } else if (i2 == 1) {
                    this.mLogicalDisplaysToUpdate.put(keyAt, 6);
                } else if (!valueAt.getPendingFrameRateOverrideUids().isEmpty()) {
                    this.mLogicalDisplaysToUpdate.put(keyAt, 5);
                } else if (this.mLogicalDisplayMapperExt.updateLogicalDisplaysLocked(valueAt)) {
                    assignDisplayGroupLocked(valueAt);
                    this.mLogicalDisplaysToUpdate.put(keyAt, 2);
                } else if (this.mLogicalDisplayMapperExt.hasFoldRemapDisplayDisableFeature() && this.mInteractive && displayInfoLocked.state == 1) {
                    this.mLogicalDisplaysToUpdate.put(keyAt, 6);
                } else {
                    valueAt.getNonOverrideDisplayInfoLocked(this.mTempDisplayInfo);
                    if (!this.mTempNonOverrideDisplayInfo.equals(this.mTempDisplayInfo)) {
                        this.mLogicalDisplaysToUpdate.put(keyAt, 2);
                    }
                }
                this.mLogicalDisplayMapperExt.resetPowerModeChanged(valueAt);
                this.mUpdatedLogicalDisplays.put(keyAt, 2);
            }
            size--;
        }
        for (int size2 = this.mDisplayGroups.size() - 1; size2 >= 0; size2--) {
            int keyAt2 = this.mDisplayGroups.keyAt(size2);
            DisplayGroup valueAt2 = this.mDisplayGroups.valueAt(size2);
            boolean z2 = this.mUpdatedDisplayGroups.indexOfKey(keyAt2) > -1;
            int changeCountLocked = valueAt2.getChangeCountLocked();
            if (valueAt2.isEmptyLocked()) {
                this.mUpdatedDisplayGroups.delete(keyAt2);
                if (z2) {
                    this.mDisplayGroupsToUpdate.put(keyAt2, 3);
                }
            } else {
                if (!z2) {
                    this.mDisplayGroupsToUpdate.put(keyAt2, 1);
                } else if (this.mUpdatedDisplayGroups.get(keyAt2) != changeCountLocked) {
                    this.mDisplayGroupsToUpdate.put(keyAt2, 2);
                }
                this.mUpdatedDisplayGroups.put(keyAt2, changeCountLocked);
            }
        }
        if (DEBUG) {
            Slog.d(TAG, "updateLogicalDisplaysLocked updated=" + this.mUpdatedLogicalDisplays + " toUpdate=" + this.mLogicalDisplaysToUpdate + " toGroups=" + this.mDisplayGroupsToUpdate + " size=" + this.mLogicalDisplays.size());
        }
        sendUpdatesForDisplaysLocked(6);
        sendUpdatesForGroupsLocked(1);
        sendUpdatesForDisplaysLocked(3);
        sendUpdatesForDisplaysLocked(2);
        sendUpdatesForDisplaysLocked(5);
        sendUpdatesForDisplaysLocked(4);
        sendUpdatesForDisplaysLocked(1);
        sendUpdatesForDisplaysLocked(7);
        sendUpdatesForGroupsLocked(2);
        sendUpdatesForGroupsLocked(3);
        this.mLogicalDisplaysToUpdate.clear();
        this.mDisplayGroupsToUpdate.clear();
    }

    private void sendUpdatesForDisplaysLocked(int i) {
        for (int size = this.mLogicalDisplaysToUpdate.size() - 1; size >= 0; size--) {
            if (this.mLogicalDisplaysToUpdate.valueAt(size) == i) {
                int keyAt = this.mLogicalDisplaysToUpdate.keyAt(size);
                LogicalDisplay displayLocked = getDisplayLocked(keyAt);
                if (DEBUG) {
                    DisplayDevice primaryDisplayDeviceLocked = displayLocked.getPrimaryDisplayDeviceLocked();
                    Slog.d(TAG, "Sending " + displayEventToString(i) + " for display=" + keyAt + " with device=" + (primaryDisplayDeviceLocked == null ? "null" : primaryDisplayDeviceLocked.getUniqueId()));
                }
                if (displayLocked.isEnabledLocked()) {
                    this.mListener.onLogicalDisplayEventLocked(displayLocked, i);
                }
                if (i == 3) {
                    this.mLogicalDisplays.delete(keyAt);
                }
            }
        }
    }

    private void sendUpdatesForGroupsLocked(int i) {
        for (int size = this.mDisplayGroupsToUpdate.size() - 1; size >= 0; size--) {
            if (this.mDisplayGroupsToUpdate.valueAt(size) == i) {
                int keyAt = this.mDisplayGroupsToUpdate.keyAt(size);
                this.mListener.onDisplayGroupEventLocked(keyAt, i);
                if (i == 3) {
                    this.mDisplayGroups.delete(keyAt);
                    int indexOfValue = this.mDeviceDisplayGroupIds.indexOfValue(keyAt);
                    if (indexOfValue >= 0) {
                        this.mDeviceDisplayGroupIds.removeAt(indexOfValue);
                    }
                }
            }
        }
    }

    private void assignDisplayGroupLocked(LogicalDisplay logicalDisplay) {
        if (logicalDisplay.isValidLocked()) {
            DisplayDevice primaryDisplayDeviceLocked = logicalDisplay.getPrimaryDisplayDeviceLocked();
            int displayIdLocked = logicalDisplay.getDisplayIdLocked();
            Integer num = this.mVirtualDeviceDisplayMapping.get(primaryDisplayDeviceLocked.getUniqueId());
            int displayGroupIdFromDisplayIdLocked = getDisplayGroupIdFromDisplayIdLocked(displayIdLocked);
            Integer valueOf = (num == null || this.mDeviceDisplayGroupIds.indexOfKey(num.intValue()) <= 0) ? null : Integer.valueOf(this.mDeviceDisplayGroupIds.get(num.intValue()));
            DisplayGroup displayGroupLocked = getDisplayGroupLocked(displayGroupIdFromDisplayIdLocked);
            boolean z = false;
            boolean z2 = ((primaryDisplayDeviceLocked.getDisplayDeviceInfoLocked().flags & 16384) == 0 && TextUtils.isEmpty(logicalDisplay.getDisplayGroupNameLocked())) ? false : true;
            boolean z3 = displayGroupIdFromDisplayIdLocked != 0;
            boolean z4 = (z2 || num == null) ? false : true;
            if (valueOf != null && displayGroupIdFromDisplayIdLocked == valueOf.intValue()) {
                z = true;
            }
            if (displayGroupIdFromDisplayIdLocked == -1 || z3 != z2 || z != z4) {
                displayGroupIdFromDisplayIdLocked = assignDisplayGroupIdLocked(z2, logicalDisplay.getDisplayGroupNameLocked(), z4, num);
            }
            DisplayGroup displayGroupLocked2 = getDisplayGroupLocked(displayGroupIdFromDisplayIdLocked);
            if (displayGroupLocked2 == null) {
                displayGroupLocked2 = new DisplayGroup(displayGroupIdFromDisplayIdLocked);
                this.mDisplayGroups.append(displayGroupIdFromDisplayIdLocked, displayGroupLocked2);
            }
            if (displayGroupLocked != displayGroupLocked2) {
                if (displayGroupLocked != null) {
                    displayGroupLocked.removeDisplayLocked(logicalDisplay);
                }
                displayGroupLocked2.addDisplayLocked(logicalDisplay);
                logicalDisplay.updateDisplayGroupIdLocked(displayGroupIdFromDisplayIdLocked);
                StringBuilder sb = new StringBuilder();
                sb.append("Setting new display group ");
                sb.append(displayGroupIdFromDisplayIdLocked);
                sb.append(" for display ");
                sb.append(displayIdLocked);
                sb.append(", from previous group: ");
                sb.append(displayGroupLocked != null ? Integer.valueOf(displayGroupLocked.getGroupId()) : "null");
                Slog.i(TAG, sb.toString());
            }
        }
    }

    private void resetLayoutLocked(int i, int i2, boolean z) {
        Layout layout;
        Layout layout2 = this.mDeviceStateToLayoutMap.get(i);
        Layout layout3 = this.mDeviceStateToLayoutMap.get(i2);
        int size = this.mLogicalDisplays.size();
        int i3 = 0;
        while (i3 < size) {
            LogicalDisplay valueAt = this.mLogicalDisplays.valueAt(i3);
            int displayIdLocked = valueAt.getDisplayIdLocked();
            DisplayDevice primaryDisplayDeviceLocked = valueAt.getPrimaryDisplayDeviceLocked();
            if (primaryDisplayDeviceLocked == null) {
                layout = layout2;
            } else {
                DisplayAddress displayAddress = primaryDisplayDeviceLocked.getDisplayDeviceInfoLocked().address;
                Layout.Display byAddress = displayAddress != null ? layout2.getByAddress(displayAddress) : null;
                Layout.Display byAddress2 = displayAddress != null ? layout3.getByAddress(displayAddress) : null;
                boolean z2 = (byAddress == null) != (byAddress2 == null);
                boolean z3 = byAddress == null || byAddress.isEnabled();
                boolean z4 = byAddress2 == null || byAddress2.isEnabled();
                boolean z5 = (byAddress == null || byAddress2 == null || byAddress.getLogicalDisplayId() == byAddress2.getLogicalDisplayId()) ? false : true;
                boolean z6 = valueAt.isInTransitionLocked() || z3 != z4 || z5 || z2;
                if (DEBUG) {
                    StringBuilder sb = new StringBuilder();
                    layout = layout2;
                    sb.append("state:");
                    sb.append(i);
                    sb.append("->");
                    sb.append(i2);
                    sb.append(" isTransitioning=");
                    sb.append(z6);
                    sb.append(" wasEnabled=");
                    sb.append(z3);
                    sb.append(" willEnable=");
                    sb.append(z4);
                    sb.append(" newId=");
                    sb.append(z5);
                    sb.append(" display:");
                    sb.append(valueAt.toStringMini());
                    Slog.d(TAG, sb.toString());
                } else {
                    layout = layout2;
                }
                if (z6) {
                    if (z != valueAt.isInTransitionLocked()) {
                        Slog.i(TAG, "Set isInTransition on display " + displayIdLocked + ": " + z);
                    }
                    valueAt.setIsInTransitionLocked(z);
                    this.mUpdatedLogicalDisplays.put(displayIdLocked, 1);
                    if (z && displayIdLocked == 0 && z5) {
                        this.mLogicalDisplayMapperExt.notifyDisplaySwaped();
                    }
                }
            }
            i3++;
            layout2 = layout;
        }
    }

    private void applyLayoutLocked() {
        Layout layout = this.mCurrentLayout;
        this.mCurrentLayout = this.mDeviceStateToLayoutMap.get(this.mDeviceState);
        Slog.i(TAG, "Applying layout: " + this.mCurrentLayout + ", Previous layout: " + layout);
        int size = this.mCurrentLayout.size();
        for (int i = 0; i < size; i++) {
            Layout.Display at = this.mCurrentLayout.getAt(i);
            DisplayAddress address = at.getAddress();
            DisplayDevice byAddressLocked = this.mDisplayDeviceRepo.getByAddressLocked(address);
            if (byAddressLocked == null) {
                Slog.w(TAG, "The display device (" + address + "), is not available for the display state " + this.mDeviceState);
            } else {
                int logicalDisplayId = at.getLogicalDisplayId();
                LogicalDisplay displayLocked = getDisplayLocked(logicalDisplayId);
                if (displayLocked == null) {
                    displayLocked = createNewLogicalDisplayLocked(null, logicalDisplayId);
                }
                LogicalDisplay displayLocked2 = getDisplayLocked(byAddressLocked);
                if (DEBUG) {
                    Slog.d(TAG, "old:" + displayLocked2.toStringMini() + " new:" + displayLocked.toStringMini());
                }
                if (displayLocked != displayLocked2) {
                    displayLocked.swapDisplaysLocked(displayLocked2);
                }
                DisplayDeviceConfig displayDeviceConfig = byAddressLocked.getDisplayDeviceConfig();
                displayLocked.setDevicePositionLocked(at.getPosition());
                displayLocked.setLeadDisplayLocked(at.getLeadDisplayId());
                displayLocked.updateLayoutLimitedRefreshRateLocked(displayDeviceConfig.getRefreshRange(at.getRefreshRateZoneId()));
                displayLocked.updateThermalRefreshRateThrottling(displayDeviceConfig.getThermalRefreshRateThrottlingData(at.getRefreshRateThermalThrottlingMapId()));
                setEnabledLocked(displayLocked, this.mLogicalDisplayMapperExt.getOverrideState(at.isEnabled(), displayLocked));
                displayLocked.setThermalBrightnessThrottlingDataIdLocked(at.getThermalBrightnessThrottlingMapId() == null ? "default" : at.getThermalBrightnessThrottlingMapId());
                displayLocked.setDisplayGroupNameLocked(at.getDisplayGroupName());
            }
        }
    }

    private LogicalDisplay createNewLogicalDisplayLocked(DisplayDevice displayDevice, int i) {
        if (displayDevice != null && displayDevice.getNameLocked().startsWith("Mirage_")) {
            i = ((IMirageDisplayManagerExt) ExtLoader.type(IMirageDisplayManagerExt.class).create()).getLastAssignedDisplayId();
        }
        if (displayDevice != null && displayDevice.getDisplayDeviceInfoLocked().type != 1) {
            ((IMirageDisplayManagerExt) ExtLoader.type(IMirageDisplayManagerExt.class).create()).recordDisplayIdForDisplay(displayDevice, i);
        }
        LogicalDisplay logicalDisplay = new LogicalDisplay(i, assignLayerStackLocked(i), displayDevice);
        logicalDisplay.updateLocked(this.mDisplayDeviceRepo);
        logicalDisplay.getDisplayInfoLocked();
        this.mLogicalDisplays.put(i, logicalDisplay);
        return logicalDisplay;
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x0010, code lost:
    
        if (r3 != 1) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setEnabledLocked(LogicalDisplay logicalDisplay, boolean z) {
        boolean z2;
        int displayIdLocked = logicalDisplay.getDisplayIdLocked();
        DisplayInfo displayInfoLocked = logicalDisplay.getDisplayInfoLocked();
        if (this.mSingleDisplayDemoMode) {
            int i = displayInfoLocked.type;
            z2 = true;
        }
        z2 = false;
        if (z && z2) {
            Slog.i(TAG, "Not creating a logical display for a secondary display because single display demo mode is enabled: " + logicalDisplay.getDisplayInfoLocked());
            z = false;
        }
        if (logicalDisplay.isEnabledLocked() != z) {
            Slog.i(TAG, "SetEnabled on display " + displayIdLocked + ": " + z);
            logicalDisplay.setEnabledLocked(z);
        }
    }

    private int assignDisplayGroupIdLocked(boolean z, String str, boolean z2, Integer num) {
        if (z2 && num != null) {
            int i = this.mDeviceDisplayGroupIds.get(num.intValue());
            if (i != 0) {
                return i;
            }
            int i2 = this.mNextNonDefaultGroupId;
            this.mNextNonDefaultGroupId = i2 + 1;
            this.mDeviceDisplayGroupIds.put(num.intValue(), i2);
            return i2;
        }
        if (!z) {
            return 0;
        }
        Integer num2 = this.mDisplayGroupIdsByName.get(str);
        if (num2 == null) {
            int i3 = this.mNextNonDefaultGroupId;
            this.mNextNonDefaultGroupId = i3 + 1;
            num2 = Integer.valueOf(i3);
            this.mDisplayGroupIdsByName.put(str, num2);
        }
        return num2.intValue();
    }

    private void initializeDefaultDisplayDeviceLocked(DisplayDevice displayDevice) {
        Layout layout = this.mDeviceStateToLayoutMap.get(-1);
        if (layout.getById(0) != null) {
            return;
        }
        layout.createDefaultDisplayLocked(displayDevice.getDisplayDeviceInfoLocked().address, this.mIdProducer);
    }

    public SparseArray<LogicalDisplay> getLogicalDisplays() {
        return this.mLogicalDisplays;
    }

    private void requestDisplaySwitchOff() {
        DisplayDevice primaryDisplayDeviceLocked;
        for (int size = this.mLogicalDisplays.size() - 1; size >= 0; size--) {
            int keyAt = this.mLogicalDisplays.keyAt(size);
            LogicalDisplay valueAt = this.mLogicalDisplays.valueAt(size);
            DisplayInfo displayInfoLocked = valueAt.getDisplayInfoLocked();
            this.mUpdatedLogicalDisplays.get(keyAt, 0);
            if (!valueAt.isEnabledLocked() && displayInfoLocked.state == 2 && (primaryDisplayDeviceLocked = valueAt.getPrimaryDisplayDeviceLocked()) != null) {
                Runnable requestDisplayStateLocked = primaryDisplayDeviceLocked.requestDisplayStateLocked(1, -1.0f, -1.0f);
                Slog.d(TAG, "updateLogicalDisplaysLocked OFF id=" + primaryDisplayDeviceLocked.getUniqueId());
                if (requestDisplayStateLocked != null) {
                    this.mHandler.post(requestDisplayStateLocked);
                }
            }
        }
    }

    private SparseBooleanArray toSparseBooleanArray(int[] iArr) {
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray(2);
        for (int i = 0; iArr != null && i < iArr.length; i++) {
            sparseBooleanArray.put(iArr[i], true);
        }
        return sparseBooleanArray;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class LogicalDisplayMapperHandler extends Handler {
        LogicalDisplayMapperHandler(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            synchronized (LogicalDisplayMapper.this.mSyncRoot) {
                LogicalDisplayMapper.this.finishStateTransitionLocked(true);
            }
        }
    }

    public IOplusLogicDisplayMapperWrapper getWrapper() {
        return this.mWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class OplusLogicDisplayMapperWrapper implements IOplusLogicDisplayMapperWrapper {
        private OplusLogicDisplayMapperWrapper() {
        }

        @Override // com.android.server.display.IOplusLogicDisplayMapperWrapper
        public int getPendingDeviceState() {
            return LogicalDisplayMapper.this.mPendingDeviceState;
        }

        @Override // com.android.server.display.IOplusLogicDisplayMapperWrapper
        public void setPendingDeviceState(int i) {
            LogicalDisplayMapper.this.mPendingDeviceState = i;
        }

        @Override // com.android.server.display.IOplusLogicDisplayMapperWrapper
        public int getDeviceState() {
            return LogicalDisplayMapper.this.mDeviceState;
        }

        @Override // com.android.server.display.IOplusLogicDisplayMapperWrapper
        public void dispatchDelayedDeviceState(int i, boolean z) {
            synchronized (LogicalDisplayMapper.this.mSyncRoot) {
                LogicalDisplayMapper.this.setDeviceStateLocked(i, z);
            }
        }

        @Override // com.android.server.display.IOplusLogicDisplayMapperWrapper
        public Handler getHandler() {
            return LogicalDisplayMapper.this.mHandler;
        }
    }
}
