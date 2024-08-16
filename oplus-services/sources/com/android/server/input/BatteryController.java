package com.android.server.input;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.input.IInputDeviceBatteryListener;
import android.hardware.input.IInputDeviceBatteryState;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UEventObserver;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.Slog;
import android.view.InputDevice;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.input.BatteryController;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class BatteryController {
    private static final boolean DEBUG = Log.isLoggable(BatteryController.class.getSimpleName(), 3);

    @VisibleForTesting
    static final long POLLING_PERIOD_MILLIS = 10000;
    private static final String TAG = "BatteryController";

    @VisibleForTesting
    static final long USI_BATTERY_VALIDITY_DURATION_MILLIS = 3600000;

    @GuardedBy({"mLock"})
    private BluetoothBatteryManager.BluetoothBatteryListener mBluetoothBatteryListener;
    private final BluetoothBatteryManager mBluetoothBatteryManager;
    private final Context mContext;

    @GuardedBy({"mLock"})
    private final ArrayMap<Integer, DeviceMonitor> mDeviceMonitors;
    private final Handler mHandler;
    private final InputManager.InputDeviceListener mInputDeviceListener;

    @GuardedBy({"mLock"})
    private boolean mIsInteractive;

    @GuardedBy({"mLock"})
    private boolean mIsPolling;

    @GuardedBy({"mLock"})
    private final ArrayMap<Integer, ListenerRecord> mListenerRecords;
    private final Object mLock;
    private final NativeInputManagerService mNative;
    private final UEventManager mUEventManager;

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface BluetoothBatteryManager {

        @VisibleForTesting
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public interface BluetoothBatteryListener {
            void onBluetoothBatteryChanged(long j, String str, int i);
        }

        void addBatteryListener(BluetoothBatteryListener bluetoothBatteryListener);

        void addMetadataListener(String str, BluetoothAdapter.OnMetadataChangedListener onMetadataChangedListener);

        int getBatteryLevel(String str);

        byte[] getMetadata(String str, int i);

        void removeBatteryListener(BluetoothBatteryListener bluetoothBatteryListener);

        void removeMetadataListener(String str, BluetoothAdapter.OnMetadataChangedListener onMetadataChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.input.BatteryController$1 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 implements UEventManager {
        AnonymousClass1() {
        }
    }

    public BatteryController(Context context, NativeInputManagerService nativeInputManagerService, Looper looper) {
        this(context, nativeInputManagerService, looper, new UEventManager() { // from class: com.android.server.input.BatteryController.1
            AnonymousClass1() {
            }
        }, new LocalBluetoothBatteryManager(context, looper));
    }

    @VisibleForTesting
    BatteryController(Context context, NativeInputManagerService nativeInputManagerService, Looper looper, UEventManager uEventManager, BluetoothBatteryManager bluetoothBatteryManager) {
        this.mLock = new Object();
        this.mListenerRecords = new ArrayMap<>();
        this.mDeviceMonitors = new ArrayMap<>();
        this.mIsPolling = false;
        this.mIsInteractive = true;
        this.mInputDeviceListener = new InputManager.InputDeviceListener() { // from class: com.android.server.input.BatteryController.2
            @Override // android.hardware.input.InputManager.InputDeviceListener
            public void onInputDeviceRemoved(int i) {
            }

            AnonymousClass2() {
            }

            @Override // android.hardware.input.InputManager.InputDeviceListener
            public void onInputDeviceAdded(int i) {
                synchronized (BatteryController.this.mLock) {
                    if (BatteryController.this.isUsiDevice(i) && !BatteryController.this.mDeviceMonitors.containsKey(Integer.valueOf(i))) {
                        BatteryController.this.mDeviceMonitors.put(Integer.valueOf(i), new UsiDeviceMonitor(i));
                    }
                }
            }

            @Override // android.hardware.input.InputManager.InputDeviceListener
            public void onInputDeviceChanged(int i) {
                synchronized (BatteryController.this.mLock) {
                    DeviceMonitor deviceMonitor = (DeviceMonitor) BatteryController.this.mDeviceMonitors.get(Integer.valueOf(i));
                    if (deviceMonitor == null) {
                        return;
                    }
                    deviceMonitor.onConfiguration(SystemClock.uptimeMillis());
                }
            }
        };
        this.mContext = context;
        this.mNative = nativeInputManagerService;
        this.mHandler = new Handler(looper);
        this.mUEventManager = uEventManager;
        this.mBluetoothBatteryManager = bluetoothBatteryManager;
    }

    public void systemRunning() {
        InputManager inputManager = (InputManager) this.mContext.getSystemService(InputManager.class);
        Objects.requireNonNull(inputManager);
        inputManager.registerInputDeviceListener(this.mInputDeviceListener, this.mHandler);
        for (int i : inputManager.getInputDeviceIds()) {
            this.mInputDeviceListener.onInputDeviceAdded(i);
        }
    }

    public void registerBatteryListener(int i, IInputDeviceBatteryListener iInputDeviceBatteryListener, int i2) {
        synchronized (this.mLock) {
            ListenerRecord listenerRecord = this.mListenerRecords.get(Integer.valueOf(i2));
            if (listenerRecord == null) {
                listenerRecord = new ListenerRecord(i2, iInputDeviceBatteryListener);
                try {
                    iInputDeviceBatteryListener.asBinder().linkToDeath(listenerRecord.mDeathRecipient, 0);
                    this.mListenerRecords.put(Integer.valueOf(i2), listenerRecord);
                    if (DEBUG) {
                        Slog.d(TAG, "Battery listener added for pid " + i2);
                    }
                } catch (RemoteException unused) {
                    Slog.i(TAG, "Client died before battery listener could be registered.");
                    return;
                }
            }
            if (listenerRecord.mListener.asBinder() != iInputDeviceBatteryListener.asBinder()) {
                throw new SecurityException("Cannot register a new battery listener when there is already another registered listener for pid " + i2);
            }
            if (!listenerRecord.mMonitoredDevices.add(Integer.valueOf(i))) {
                throw new IllegalArgumentException("The battery listener for pid " + i2 + " is already monitoring deviceId " + i);
            }
            DeviceMonitor deviceMonitor = this.mDeviceMonitors.get(Integer.valueOf(i));
            if (deviceMonitor == null) {
                deviceMonitor = new DeviceMonitor(i);
                this.mDeviceMonitors.put(Integer.valueOf(i), deviceMonitor);
                updateBluetoothBatteryMonitoring();
            }
            if (DEBUG) {
                Slog.d(TAG, "Battery listener for pid " + i2 + " is monitoring deviceId " + i);
            }
            updatePollingLocked(true);
            notifyBatteryListener(listenerRecord, deviceMonitor.getBatteryStateForReporting());
        }
    }

    private static void notifyBatteryListener(ListenerRecord listenerRecord, State state) {
        try {
            listenerRecord.mListener.onBatteryStateChanged(state);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to notify listener", e);
        }
        if (DEBUG) {
            Slog.d(TAG, "Notified battery listener from pid " + listenerRecord.mPid + " of state of deviceId " + ((IInputDeviceBatteryState) state).deviceId);
        }
    }

    public void notifyAllListenersForDevice(final State state) {
        synchronized (this.mLock) {
            if (DEBUG) {
                Slog.d(TAG, "Notifying all listeners of battery state: " + state);
            }
            this.mListenerRecords.forEach(new BiConsumer() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda9
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    BatteryController.lambda$notifyAllListenersForDevice$0(BatteryController.State.this, (Integer) obj, (BatteryController.ListenerRecord) obj2);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$notifyAllListenersForDevice$0(State state, Integer num, ListenerRecord listenerRecord) {
        if (listenerRecord.mMonitoredDevices.contains(Integer.valueOf(((IInputDeviceBatteryState) state).deviceId))) {
            notifyBatteryListener(listenerRecord, state);
        }
    }

    @GuardedBy({"mLock"})
    private void updatePollingLocked(boolean z) {
        if (!this.mIsInteractive || !anyOf(this.mDeviceMonitors, new Predicate() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((BatteryController.DeviceMonitor) obj).requiresPolling();
            }
        })) {
            this.mIsPolling = false;
            this.mHandler.removeCallbacks(new BatteryController$$ExternalSyntheticLambda7(this));
        } else {
            if (this.mIsPolling) {
                return;
            }
            this.mIsPolling = true;
            this.mHandler.postDelayed(new BatteryController$$ExternalSyntheticLambda7(this), z ? 10000L : 0L);
        }
    }

    private <R> R processInputDevice(int i, R r, Function<InputDevice, R> function) {
        InputManager inputManager = (InputManager) this.mContext.getSystemService(InputManager.class);
        Objects.requireNonNull(inputManager);
        InputDevice inputDevice = inputManager.getInputDevice(i);
        return inputDevice == null ? r : function.apply(inputDevice);
    }

    public String getInputDeviceName(int i) {
        return (String) processInputDevice(i, "<none>", new Function() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((InputDevice) obj).getName();
            }
        });
    }

    public boolean hasBattery(int i) {
        return ((Boolean) processInputDevice(i, Boolean.FALSE, new Function() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Boolean.valueOf(((InputDevice) obj).hasBattery());
            }
        })).booleanValue();
    }

    public boolean isUsiDevice(int i) {
        return ((Boolean) processInputDevice(i, Boolean.FALSE, new Function() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Boolean lambda$isUsiDevice$1;
                lambda$isUsiDevice$1 = BatteryController.lambda$isUsiDevice$1((InputDevice) obj);
                return lambda$isUsiDevice$1;
            }
        })).booleanValue();
    }

    public static /* synthetic */ Boolean lambda$isUsiDevice$1(InputDevice inputDevice) {
        return Boolean.valueOf(inputDevice.getHostUsiVersion() != null);
    }

    public BluetoothDevice getBluetoothDevice(int i) {
        return getBluetoothDevice(this.mContext, (String) processInputDevice(i, null, new Function() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((InputDevice) obj).getBluetoothAddress();
            }
        }));
    }

    public static BluetoothDevice getBluetoothDevice(Context context, String str) {
        if (str == null) {
            return null;
        }
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(BluetoothManager.class);
        Objects.requireNonNull(bluetoothManager);
        return bluetoothManager.getAdapter().getRemoteDevice(str);
    }

    @GuardedBy({"mLock"})
    private DeviceMonitor getDeviceMonitorOrThrowLocked(int i) {
        DeviceMonitor deviceMonitor = this.mDeviceMonitors.get(Integer.valueOf(i));
        Objects.requireNonNull(deviceMonitor, "Maps are out of sync: Cannot find device state for deviceId " + i);
        return deviceMonitor;
    }

    public void unregisterBatteryListener(int i, IInputDeviceBatteryListener iInputDeviceBatteryListener, int i2) {
        synchronized (this.mLock) {
            ListenerRecord listenerRecord = this.mListenerRecords.get(Integer.valueOf(i2));
            if (listenerRecord == null) {
                throw new IllegalArgumentException("Cannot unregister battery callback: No listener registered for pid " + i2);
            }
            if (listenerRecord.mListener.asBinder() != iInputDeviceBatteryListener.asBinder()) {
                throw new IllegalArgumentException("Cannot unregister battery callback: The listener is not the one that is registered for pid " + i2);
            }
            if (!listenerRecord.mMonitoredDevices.contains(Integer.valueOf(i))) {
                throw new IllegalArgumentException("Cannot unregister battery callback: The device is not being monitored for deviceId " + i);
            }
            unregisterRecordLocked(listenerRecord, i);
        }
    }

    @GuardedBy({"mLock"})
    private void unregisterRecordLocked(ListenerRecord listenerRecord, int i) {
        int i2 = listenerRecord.mPid;
        if (!listenerRecord.mMonitoredDevices.remove(Integer.valueOf(i))) {
            throw new IllegalStateException("Cannot unregister battery callback: The deviceId " + i + " is not being monitored by pid " + i2);
        }
        if (!hasRegisteredListenerForDeviceLocked(i)) {
            DeviceMonitor deviceMonitorOrThrowLocked = getDeviceMonitorOrThrowLocked(i);
            if (!deviceMonitorOrThrowLocked.isPersistent()) {
                deviceMonitorOrThrowLocked.onMonitorDestroy();
                this.mDeviceMonitors.remove(Integer.valueOf(i));
            }
        }
        if (listenerRecord.mMonitoredDevices.isEmpty()) {
            listenerRecord.mListener.asBinder().unlinkToDeath(listenerRecord.mDeathRecipient, 0);
            this.mListenerRecords.remove(Integer.valueOf(i2));
            if (DEBUG) {
                Slog.d(TAG, "Battery listener removed for pid " + i2);
            }
        }
        updatePollingLocked(false);
    }

    @GuardedBy({"mLock"})
    private boolean hasRegisteredListenerForDeviceLocked(int i) {
        for (int i2 = 0; i2 < this.mListenerRecords.size(); i2++) {
            if (this.mListenerRecords.valueAt(i2).mMonitoredDevices.contains(Integer.valueOf(i))) {
                return true;
            }
        }
        return false;
    }

    public void handleListeningProcessDied(int i) {
        synchronized (this.mLock) {
            ListenerRecord listenerRecord = this.mListenerRecords.get(Integer.valueOf(i));
            if (listenerRecord == null) {
                return;
            }
            if (DEBUG) {
                Slog.d(TAG, "Removing battery listener for pid " + i + " because the process died");
            }
            Iterator<Integer> it = listenerRecord.mMonitoredDevices.iterator();
            while (it.hasNext()) {
                unregisterRecordLocked(listenerRecord, it.next().intValue());
            }
        }
    }

    public void handleUEventNotification(int i, long j) {
        synchronized (this.mLock) {
            DeviceMonitor deviceMonitor = this.mDeviceMonitors.get(Integer.valueOf(i));
            if (deviceMonitor == null) {
                return;
            }
            deviceMonitor.onUEvent(j);
        }
    }

    public void handlePollEvent() {
        synchronized (this.mLock) {
            if (this.mIsPolling) {
                final long uptimeMillis = SystemClock.uptimeMillis();
                this.mDeviceMonitors.forEach(new BiConsumer() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda10
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        ((BatteryController.DeviceMonitor) obj2).onPoll(uptimeMillis);
                    }
                });
                this.mHandler.postDelayed(new BatteryController$$ExternalSyntheticLambda7(this), 10000L);
            }
        }
    }

    public void handleMonitorTimeout(int i) {
        synchronized (this.mLock) {
            DeviceMonitor deviceMonitor = this.mDeviceMonitors.get(Integer.valueOf(i));
            if (deviceMonitor == null) {
                return;
            }
            deviceMonitor.onTimeout(SystemClock.uptimeMillis());
        }
    }

    public void handleBluetoothBatteryLevelChange(long j, final String str, int i) {
        synchronized (this.mLock) {
            DeviceMonitor deviceMonitor = (DeviceMonitor) findIf(this.mDeviceMonitors, new Predicate() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda5
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$handleBluetoothBatteryLevelChange$3;
                    lambda$handleBluetoothBatteryLevelChange$3 = BatteryController.lambda$handleBluetoothBatteryLevelChange$3(str, (BatteryController.DeviceMonitor) obj);
                    return lambda$handleBluetoothBatteryLevelChange$3;
                }
            });
            if (deviceMonitor != null) {
                deviceMonitor.onBluetoothBatteryChanged(j, i);
            }
        }
    }

    public static /* synthetic */ boolean lambda$handleBluetoothBatteryLevelChange$3(String str, DeviceMonitor deviceMonitor) {
        return deviceMonitor.mBluetoothDevice != null && str.equals(deviceMonitor.mBluetoothDevice.getAddress());
    }

    public void handleBluetoothMetadataChange(final BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
        synchronized (this.mLock) {
            DeviceMonitor deviceMonitor = (DeviceMonitor) findIf(this.mDeviceMonitors, new Predicate() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda11
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$handleBluetoothMetadataChange$4;
                    lambda$handleBluetoothMetadataChange$4 = BatteryController.lambda$handleBluetoothMetadataChange$4(bluetoothDevice, (BatteryController.DeviceMonitor) obj);
                    return lambda$handleBluetoothMetadataChange$4;
                }
            });
            if (deviceMonitor != null) {
                deviceMonitor.onBluetoothMetadataChanged(SystemClock.uptimeMillis(), i, bArr);
            }
        }
    }

    public static /* synthetic */ boolean lambda$handleBluetoothMetadataChange$4(BluetoothDevice bluetoothDevice, DeviceMonitor deviceMonitor) {
        return bluetoothDevice.equals(deviceMonitor.mBluetoothDevice);
    }

    public IInputDeviceBatteryState getBatteryState(int i) {
        synchronized (this.mLock) {
            long uptimeMillis = SystemClock.uptimeMillis();
            DeviceMonitor deviceMonitor = this.mDeviceMonitors.get(Integer.valueOf(i));
            if (deviceMonitor == null) {
                return queryBatteryStateFromNative(i, uptimeMillis, hasBattery(i));
            }
            deviceMonitor.onPoll(uptimeMillis);
            return deviceMonitor.getBatteryStateForReporting();
        }
    }

    public void onInteractiveChanged(boolean z) {
        synchronized (this.mLock) {
            this.mIsInteractive = z;
            updatePollingLocked(false);
        }
    }

    public void notifyStylusGestureStarted(int i, long j) {
        synchronized (this.mLock) {
            DeviceMonitor deviceMonitor = this.mDeviceMonitors.get(Integer.valueOf(i));
            if (deviceMonitor == null) {
                return;
            }
            deviceMonitor.onStylusGestureStarted(j);
        }
    }

    public void dump(PrintWriter printWriter) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        synchronized (this.mLock) {
            indentingPrintWriter.println(TAG + ":");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println("State: Polling = " + this.mIsPolling + ", Interactive = " + this.mIsInteractive);
            StringBuilder sb = new StringBuilder();
            sb.append("Listeners: ");
            sb.append(this.mListenerRecords.size());
            sb.append(" battery listeners");
            indentingPrintWriter.println(sb.toString());
            indentingPrintWriter.increaseIndent();
            for (int i = 0; i < this.mListenerRecords.size(); i++) {
                indentingPrintWriter.println(i + ": " + this.mListenerRecords.valueAt(i));
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("Device Monitors: " + this.mDeviceMonitors.size() + " monitors");
            indentingPrintWriter.increaseIndent();
            for (int i2 = 0; i2 < this.mDeviceMonitors.size(); i2++) {
                indentingPrintWriter.println(i2 + ": " + this.mDeviceMonitors.valueAt(i2));
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.decreaseIndent();
        }
    }

    public void monitor() {
        synchronized (this.mLock) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.input.BatteryController$2 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass2 implements InputManager.InputDeviceListener {
        @Override // android.hardware.input.InputManager.InputDeviceListener
        public void onInputDeviceRemoved(int i) {
        }

        AnonymousClass2() {
        }

        @Override // android.hardware.input.InputManager.InputDeviceListener
        public void onInputDeviceAdded(int i) {
            synchronized (BatteryController.this.mLock) {
                if (BatteryController.this.isUsiDevice(i) && !BatteryController.this.mDeviceMonitors.containsKey(Integer.valueOf(i))) {
                    BatteryController.this.mDeviceMonitors.put(Integer.valueOf(i), new UsiDeviceMonitor(i));
                }
            }
        }

        @Override // android.hardware.input.InputManager.InputDeviceListener
        public void onInputDeviceChanged(int i) {
            synchronized (BatteryController.this.mLock) {
                DeviceMonitor deviceMonitor = (DeviceMonitor) BatteryController.this.mDeviceMonitors.get(Integer.valueOf(i));
                if (deviceMonitor == null) {
                    return;
                }
                deviceMonitor.onConfiguration(SystemClock.uptimeMillis());
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class ListenerRecord {
        public final IBinder.DeathRecipient mDeathRecipient;
        public final IInputDeviceBatteryListener mListener;
        public final Set<Integer> mMonitoredDevices = new ArraySet();
        public final int mPid;

        ListenerRecord(final int i, IInputDeviceBatteryListener iInputDeviceBatteryListener) {
            this.mPid = i;
            this.mListener = iInputDeviceBatteryListener;
            this.mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.android.server.input.BatteryController$ListenerRecord$$ExternalSyntheticLambda0
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    BatteryController.ListenerRecord.this.lambda$new$0(i);
                }
            };
        }

        public /* synthetic */ void lambda$new$0(int i) {
            BatteryController.this.handleListeningProcessDied(i);
        }

        public String toString() {
            return "pid=" + this.mPid + ", monitored devices=" + Arrays.toString(this.mMonitoredDevices.toArray());
        }
    }

    public State queryBatteryStateFromNative(int i, long j, boolean z) {
        return new State(i, j, z, z ? this.mNative.getBatteryStatus(i) : 1, z ? this.mNative.getBatteryCapacity(i) / 100.0f : Float.NaN);
    }

    public void updateBluetoothBatteryMonitoring() {
        synchronized (this.mLock) {
            if (anyOf(this.mDeviceMonitors, new Predicate() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$updateBluetoothBatteryMonitoring$5;
                    lambda$updateBluetoothBatteryMonitoring$5 = BatteryController.lambda$updateBluetoothBatteryMonitoring$5((BatteryController.DeviceMonitor) obj);
                    return lambda$updateBluetoothBatteryMonitoring$5;
                }
            })) {
                if (this.mBluetoothBatteryListener == null) {
                    if (DEBUG) {
                        Slog.d(TAG, "Registering bluetooth battery listener");
                    }
                    BluetoothBatteryManager.BluetoothBatteryListener bluetoothBatteryListener = new BluetoothBatteryManager.BluetoothBatteryListener() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda1
                        @Override // com.android.server.input.BatteryController.BluetoothBatteryManager.BluetoothBatteryListener
                        public final void onBluetoothBatteryChanged(long j, String str, int i) {
                            BatteryController.this.handleBluetoothBatteryLevelChange(j, str, i);
                        }
                    };
                    this.mBluetoothBatteryListener = bluetoothBatteryListener;
                    this.mBluetoothBatteryManager.addBatteryListener(bluetoothBatteryListener);
                }
            } else if (this.mBluetoothBatteryListener != null) {
                if (DEBUG) {
                    Slog.d(TAG, "Unregistering bluetooth battery listener");
                }
                this.mBluetoothBatteryManager.removeBatteryListener(this.mBluetoothBatteryListener);
                this.mBluetoothBatteryListener = null;
            }
        }
    }

    public static /* synthetic */ boolean lambda$updateBluetoothBatteryMonitoring$5(DeviceMonitor deviceMonitor) {
        return deviceMonitor.mBluetoothDevice != null;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class DeviceMonitor {
        private BluetoothDevice mBluetoothDevice;
        private BluetoothAdapter.OnMetadataChangedListener mBluetoothMetadataListener;
        protected final State mState;
        private UEventManager.UEventBatteryListener mUEventBatteryListener;
        protected boolean mHasBattery = false;
        long mBluetoothEventTime = 0;
        int mBluetoothBatteryLevel = -1;
        int mBluetoothMetadataBatteryLevel = -1;
        int mBluetoothMetadataBatteryStatus = 1;

        public boolean isPersistent() {
            return false;
        }

        public void onStylusGestureStarted(long j) {
        }

        public void onTimeout(long j) {
        }

        public boolean requiresPolling() {
            return true;
        }

        DeviceMonitor(int i) {
            this.mState = new State(i);
            configureDeviceMonitor(SystemClock.uptimeMillis());
        }

        protected void processChangesAndNotify(long j, Consumer<Long> consumer) {
            State batteryStateForReporting = getBatteryStateForReporting();
            consumer.accept(Long.valueOf(j));
            State batteryStateForReporting2 = getBatteryStateForReporting();
            if (batteryStateForReporting.equalsIgnoringUpdateTime(batteryStateForReporting2)) {
                return;
            }
            BatteryController.this.notifyAllListenersForDevice(batteryStateForReporting2);
        }

        public void onConfiguration(long j) {
            processChangesAndNotify(j, new Consumer() { // from class: com.android.server.input.BatteryController$DeviceMonitor$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    BatteryController.DeviceMonitor.this.configureDeviceMonitor(((Long) obj).longValue());
                }
            });
        }

        public void configureDeviceMonitor(long j) {
            int i = ((IInputDeviceBatteryState) this.mState).deviceId;
            if (this.mHasBattery != BatteryController.this.hasBattery(i)) {
                boolean z = !this.mHasBattery;
                this.mHasBattery = z;
                if (z) {
                    startNativeMonitoring();
                } else {
                    stopNativeMonitoring();
                }
                updateBatteryStateFromNative(j);
            }
            BluetoothDevice bluetoothDevice = BatteryController.this.getBluetoothDevice(i);
            if (Objects.equals(this.mBluetoothDevice, bluetoothDevice)) {
                return;
            }
            if (BatteryController.DEBUG) {
                String str = BatteryController.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Bluetooth device is now ");
                sb.append(bluetoothDevice != null ? "" : "not");
                sb.append(" present for deviceId ");
                sb.append(i);
                Slog.d(str, sb.toString());
            }
            this.mBluetoothBatteryLevel = -1;
            stopBluetoothMetadataMonitoring();
            this.mBluetoothDevice = bluetoothDevice;
            BatteryController.this.updateBluetoothBatteryMonitoring();
            if (this.mBluetoothDevice != null) {
                this.mBluetoothBatteryLevel = BatteryController.this.mBluetoothBatteryManager.getBatteryLevel(this.mBluetoothDevice.getAddress());
                startBluetoothMetadataMonitoring(j);
            }
        }

        private void startNativeMonitoring() {
            String batteryDevicePath = BatteryController.this.mNative.getBatteryDevicePath(((IInputDeviceBatteryState) this.mState).deviceId);
            if (batteryDevicePath == null) {
                return;
            }
            this.mUEventBatteryListener = new UEventManager.UEventBatteryListener() { // from class: com.android.server.input.BatteryController.DeviceMonitor.1
                final /* synthetic */ int val$deviceId;

                AnonymousClass1(int i) {
                    r2 = i;
                }

                @Override // com.android.server.input.BatteryController.UEventManager.UEventBatteryListener
                public void onBatteryUEvent(long j) {
                    BatteryController.this.handleUEventNotification(r2, j);
                }
            };
            BatteryController.this.mUEventManager.addListener(this.mUEventBatteryListener, "DEVPATH=" + formatDevPath(batteryDevicePath));
        }

        /* renamed from: com.android.server.input.BatteryController$DeviceMonitor$1 */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public class AnonymousClass1 extends UEventManager.UEventBatteryListener {
            final /* synthetic */ int val$deviceId;

            AnonymousClass1(int i) {
                r2 = i;
            }

            @Override // com.android.server.input.BatteryController.UEventManager.UEventBatteryListener
            public void onBatteryUEvent(long j) {
                BatteryController.this.handleUEventNotification(r2, j);
            }
        }

        private String formatDevPath(String str) {
            return str.startsWith("/sys") ? str.substring(4) : str;
        }

        private void stopNativeMonitoring() {
            if (this.mUEventBatteryListener != null) {
                BatteryController.this.mUEventManager.removeListener(this.mUEventBatteryListener);
                this.mUEventBatteryListener = null;
            }
        }

        private void startBluetoothMetadataMonitoring(long j) {
            Objects.requireNonNull(this.mBluetoothDevice);
            final BatteryController batteryController = BatteryController.this;
            this.mBluetoothMetadataListener = new BluetoothAdapter.OnMetadataChangedListener() { // from class: com.android.server.input.BatteryController$DeviceMonitor$$ExternalSyntheticLambda0
                public final void onMetadataChanged(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
                    BatteryController.this.handleBluetoothMetadataChange(bluetoothDevice, i, bArr);
                }
            };
            BatteryController.this.mBluetoothBatteryManager.addMetadataListener(this.mBluetoothDevice.getAddress(), this.mBluetoothMetadataListener);
            updateBluetoothMetadataState(j, 18, BatteryController.this.mBluetoothBatteryManager.getMetadata(this.mBluetoothDevice.getAddress(), 18));
            updateBluetoothMetadataState(j, 19, BatteryController.this.mBluetoothBatteryManager.getMetadata(this.mBluetoothDevice.getAddress(), 19));
        }

        private void stopBluetoothMetadataMonitoring() {
            if (this.mBluetoothMetadataListener == null) {
                return;
            }
            Objects.requireNonNull(this.mBluetoothDevice);
            BatteryController.this.mBluetoothBatteryManager.removeMetadataListener(this.mBluetoothDevice.getAddress(), this.mBluetoothMetadataListener);
            this.mBluetoothMetadataListener = null;
            this.mBluetoothMetadataBatteryLevel = -1;
            this.mBluetoothMetadataBatteryStatus = 1;
        }

        public void onMonitorDestroy() {
            stopNativeMonitoring();
            stopBluetoothMetadataMonitoring();
            this.mBluetoothDevice = null;
            BatteryController.this.updateBluetoothBatteryMonitoring();
        }

        public void updateBatteryStateFromNative(long j) {
            State state = this.mState;
            state.updateIfChanged(BatteryController.this.queryBatteryStateFromNative(((IInputDeviceBatteryState) state).deviceId, j, this.mHasBattery));
        }

        public void onPoll(long j) {
            processChangesAndNotify(j, new BatteryController$DeviceMonitor$$ExternalSyntheticLambda1(this));
        }

        public void onUEvent(long j) {
            processChangesAndNotify(j, new BatteryController$DeviceMonitor$$ExternalSyntheticLambda1(this));
        }

        public void onBluetoothBatteryChanged(long j, final int i) {
            processChangesAndNotify(j, new Consumer() { // from class: com.android.server.input.BatteryController$DeviceMonitor$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    BatteryController.DeviceMonitor.this.lambda$onBluetoothBatteryChanged$0(i, (Long) obj);
                }
            });
        }

        public /* synthetic */ void lambda$onBluetoothBatteryChanged$0(int i, Long l) {
            this.mBluetoothBatteryLevel = i;
            this.mBluetoothEventTime = l.longValue();
        }

        public void onBluetoothMetadataChanged(long j, final int i, final byte[] bArr) {
            processChangesAndNotify(j, new Consumer() { // from class: com.android.server.input.BatteryController$DeviceMonitor$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    BatteryController.DeviceMonitor.this.lambda$onBluetoothMetadataChanged$1(i, bArr, (Long) obj);
                }
            });
        }

        public /* synthetic */ void lambda$onBluetoothMetadataChanged$1(int i, byte[] bArr, Long l) {
            updateBluetoothMetadataState(l.longValue(), i, bArr);
        }

        private void updateBluetoothMetadataState(long j, int i, byte[] bArr) {
            if (i != 18) {
                if (i != 19) {
                    return;
                }
                this.mBluetoothEventTime = j;
                if (bArr != null) {
                    this.mBluetoothMetadataBatteryStatus = Boolean.parseBoolean(new String(bArr)) ? 2 : 3;
                    return;
                } else {
                    this.mBluetoothMetadataBatteryStatus = 1;
                    return;
                }
            }
            this.mBluetoothEventTime = j;
            this.mBluetoothMetadataBatteryLevel = -1;
            if (bArr != null) {
                try {
                    this.mBluetoothMetadataBatteryLevel = Integer.parseInt(new String(bArr));
                } catch (NumberFormatException unused) {
                    Slog.wtf(BatteryController.TAG, "Failed to parse bluetooth METADATA_MAIN_BATTERY with value '" + new String(bArr) + "' for device " + this.mBluetoothDevice);
                }
            }
        }

        public State getBatteryStateForReporting() {
            return (State) Objects.requireNonNullElseGet(resolveBluetoothBatteryState(), new Supplier() { // from class: com.android.server.input.BatteryController$DeviceMonitor$$ExternalSyntheticLambda2
                @Override // java.util.function.Supplier
                public final Object get() {
                    BatteryController.State lambda$getBatteryStateForReporting$2;
                    lambda$getBatteryStateForReporting$2 = BatteryController.DeviceMonitor.this.lambda$getBatteryStateForReporting$2();
                    return lambda$getBatteryStateForReporting$2;
                }
            });
        }

        public /* synthetic */ State lambda$getBatteryStateForReporting$2() {
            return new State(this.mState);
        }

        protected State resolveBluetoothBatteryState() {
            int i = this.mBluetoothMetadataBatteryLevel;
            if ((i < 0 || i > 100) && ((i = this.mBluetoothBatteryLevel) < 0 || i > 100)) {
                return null;
            }
            return new State(((IInputDeviceBatteryState) this.mState).deviceId, this.mBluetoothEventTime, true, this.mBluetoothMetadataBatteryStatus, i / 100.0f);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("DeviceId=");
            sb.append(((IInputDeviceBatteryState) this.mState).deviceId);
            sb.append(", Name='");
            sb.append(BatteryController.this.getInputDeviceName(((IInputDeviceBatteryState) this.mState).deviceId));
            sb.append("', NativeBattery=");
            sb.append(this.mState);
            sb.append(", UEventListener=");
            sb.append(this.mUEventBatteryListener != null ? "added" : "none");
            sb.append(", BluetoothState=");
            sb.append(resolveBluetoothBatteryState());
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class UsiDeviceMonitor extends DeviceMonitor {
        private Runnable mValidityTimeoutCallback;

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public boolean isPersistent() {
            return true;
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public void onPoll(long j) {
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public boolean requiresPolling() {
            return false;
        }

        UsiDeviceMonitor(int i) {
            super(i);
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public void onUEvent(long j) {
            processChangesAndNotify(j, new Consumer() { // from class: com.android.server.input.BatteryController$UsiDeviceMonitor$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    BatteryController.UsiDeviceMonitor.this.lambda$onUEvent$0((Long) obj);
                }
            });
        }

        public /* synthetic */ void lambda$onUEvent$0(Long l) {
            updateBatteryStateFromNative(l.longValue());
            markUsiBatteryValid();
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public void onStylusGestureStarted(long j) {
            processChangesAndNotify(j, new Consumer() { // from class: com.android.server.input.BatteryController$UsiDeviceMonitor$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    BatteryController.UsiDeviceMonitor.this.lambda$onStylusGestureStarted$1((Long) obj);
                }
            });
        }

        public /* synthetic */ void lambda$onStylusGestureStarted$1(Long l) {
            if ((this.mValidityTimeoutCallback != null) || ((IInputDeviceBatteryState) this.mState).capacity != 0.0f) {
                markUsiBatteryValid();
            }
        }

        public /* synthetic */ void lambda$onTimeout$2(Long l) {
            markUsiBatteryInvalid();
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public void onTimeout(long j) {
            processChangesAndNotify(j, new Consumer() { // from class: com.android.server.input.BatteryController$UsiDeviceMonitor$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    BatteryController.UsiDeviceMonitor.this.lambda$onTimeout$2((Long) obj);
                }
            });
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public void onConfiguration(long j) {
            super.onConfiguration(j);
            if (!this.mHasBattery) {
                throw new IllegalStateException("UsiDeviceMonitor: USI devices are always expected to report a valid battery, but no battery was detected!");
            }
        }

        private void markUsiBatteryValid() {
            if (this.mValidityTimeoutCallback != null) {
                BatteryController.this.mHandler.removeCallbacks(this.mValidityTimeoutCallback);
            } else {
                final int i = ((IInputDeviceBatteryState) this.mState).deviceId;
                this.mValidityTimeoutCallback = new Runnable() { // from class: com.android.server.input.BatteryController$UsiDeviceMonitor$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        BatteryController.UsiDeviceMonitor.this.lambda$markUsiBatteryValid$3(i);
                    }
                };
            }
            BatteryController.this.mHandler.postDelayed(this.mValidityTimeoutCallback, 3600000L);
        }

        public /* synthetic */ void lambda$markUsiBatteryValid$3(int i) {
            BatteryController.this.handleMonitorTimeout(i);
        }

        private void markUsiBatteryInvalid() {
            if (this.mValidityTimeoutCallback == null) {
                return;
            }
            BatteryController.this.mHandler.removeCallbacks(this.mValidityTimeoutCallback);
            this.mValidityTimeoutCallback = null;
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public State getBatteryStateForReporting() {
            return (State) Objects.requireNonNullElseGet(resolveBluetoothBatteryState(), new Supplier() { // from class: com.android.server.input.BatteryController$UsiDeviceMonitor$$ExternalSyntheticLambda2
                @Override // java.util.function.Supplier
                public final Object get() {
                    BatteryController.State lambda$getBatteryStateForReporting$4;
                    lambda$getBatteryStateForReporting$4 = BatteryController.UsiDeviceMonitor.this.lambda$getBatteryStateForReporting$4();
                    return lambda$getBatteryStateForReporting$4;
                }
            });
        }

        public /* synthetic */ State lambda$getBatteryStateForReporting$4() {
            return this.mValidityTimeoutCallback != null ? new State(this.mState) : new State(((IInputDeviceBatteryState) this.mState).deviceId);
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toString());
            sb.append(", UsiStateIsValid=");
            sb.append(this.mValidityTimeoutCallback != null);
            return sb.toString();
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface UEventManager {

        @VisibleForTesting
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public static abstract class UEventBatteryListener {
            private final UEventObserver mObserver = new UEventObserver() { // from class: com.android.server.input.BatteryController.UEventManager.UEventBatteryListener.1
                AnonymousClass1() {
                }

                public void onUEvent(UEventObserver.UEvent uEvent) {
                    long uptimeMillis = SystemClock.uptimeMillis();
                    if (BatteryController.DEBUG) {
                        Slog.d(BatteryController.TAG, "UEventListener: Received UEvent: " + uEvent + " eventTime: " + uptimeMillis);
                    }
                    if ("CHANGE".equalsIgnoreCase(uEvent.get("ACTION")) && "POWER_SUPPLY".equalsIgnoreCase(uEvent.get("SUBSYSTEM"))) {
                        UEventBatteryListener.this.onBatteryUEvent(uptimeMillis);
                    }
                }
            };

            public abstract void onBatteryUEvent(long j);

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: com.android.server.input.BatteryController$UEventManager$UEventBatteryListener$1 */
            /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
            public class AnonymousClass1 extends UEventObserver {
                AnonymousClass1() {
                }

                public void onUEvent(UEventObserver.UEvent uEvent) {
                    long uptimeMillis = SystemClock.uptimeMillis();
                    if (BatteryController.DEBUG) {
                        Slog.d(BatteryController.TAG, "UEventListener: Received UEvent: " + uEvent + " eventTime: " + uptimeMillis);
                    }
                    if ("CHANGE".equalsIgnoreCase(uEvent.get("ACTION")) && "POWER_SUPPLY".equalsIgnoreCase(uEvent.get("SUBSYSTEM"))) {
                        UEventBatteryListener.this.onBatteryUEvent(uptimeMillis);
                    }
                }
            }
        }

        default void addListener(UEventBatteryListener uEventBatteryListener, String str) {
            uEventBatteryListener.mObserver.startObserving(str);
        }

        default void removeListener(UEventBatteryListener uEventBatteryListener) {
            uEventBatteryListener.mObserver.stopObserving();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class LocalBluetoothBatteryManager implements BluetoothBatteryManager {

        @GuardedBy({"mBroadcastReceiver"})
        private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.input.BatteryController.LocalBluetoothBatteryManager.1
            AnonymousClass1() {
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                BluetoothDevice bluetoothDevice;
                if ("android.bluetooth.device.action.BATTERY_LEVEL_CHANGED".equals(intent.getAction()) && (bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE", BluetoothDevice.class)) != null) {
                    int intExtra = intent.getIntExtra("android.bluetooth.device.extra.BATTERY_LEVEL", -1);
                    synchronized (LocalBluetoothBatteryManager.this.mBroadcastReceiver) {
                        if (LocalBluetoothBatteryManager.this.mRegisteredListener != null) {
                            LocalBluetoothBatteryManager.this.mRegisteredListener.onBluetoothBatteryChanged(SystemClock.uptimeMillis(), bluetoothDevice.getAddress(), intExtra);
                        }
                    }
                }
            }
        };
        private final Context mContext;
        private final Executor mExecutor;

        @GuardedBy({"mBroadcastReceiver"})
        private BluetoothBatteryManager.BluetoothBatteryListener mRegisteredListener;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.android.server.input.BatteryController$LocalBluetoothBatteryManager$1 */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public class AnonymousClass1 extends BroadcastReceiver {
            AnonymousClass1() {
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                BluetoothDevice bluetoothDevice;
                if ("android.bluetooth.device.action.BATTERY_LEVEL_CHANGED".equals(intent.getAction()) && (bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE", BluetoothDevice.class)) != null) {
                    int intExtra = intent.getIntExtra("android.bluetooth.device.extra.BATTERY_LEVEL", -1);
                    synchronized (LocalBluetoothBatteryManager.this.mBroadcastReceiver) {
                        if (LocalBluetoothBatteryManager.this.mRegisteredListener != null) {
                            LocalBluetoothBatteryManager.this.mRegisteredListener.onBluetoothBatteryChanged(SystemClock.uptimeMillis(), bluetoothDevice.getAddress(), intExtra);
                        }
                    }
                }
            }
        }

        LocalBluetoothBatteryManager(Context context, Looper looper) {
            this.mContext = context;
            this.mExecutor = new HandlerExecutor(new Handler(looper));
        }

        @Override // com.android.server.input.BatteryController.BluetoothBatteryManager
        public void addBatteryListener(BluetoothBatteryManager.BluetoothBatteryListener bluetoothBatteryListener) {
            synchronized (this.mBroadcastReceiver) {
                if (this.mRegisteredListener != null) {
                    throw new IllegalStateException("Only one bluetooth battery listener can be registered at once.");
                }
                this.mRegisteredListener = bluetoothBatteryListener;
                this.mContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter("android.bluetooth.device.action.BATTERY_LEVEL_CHANGED"));
            }
        }

        @Override // com.android.server.input.BatteryController.BluetoothBatteryManager
        public void removeBatteryListener(BluetoothBatteryManager.BluetoothBatteryListener bluetoothBatteryListener) {
            synchronized (this.mBroadcastReceiver) {
                if (!bluetoothBatteryListener.equals(this.mRegisteredListener)) {
                    throw new IllegalStateException("Listener is not registered.");
                }
                this.mRegisteredListener = null;
                this.mContext.unregisterReceiver(this.mBroadcastReceiver);
            }
        }

        @Override // com.android.server.input.BatteryController.BluetoothBatteryManager
        public int getBatteryLevel(String str) {
            return BatteryController.getBluetoothDevice(this.mContext, str).getBatteryLevel();
        }

        @Override // com.android.server.input.BatteryController.BluetoothBatteryManager
        public void addMetadataListener(String str, BluetoothAdapter.OnMetadataChangedListener onMetadataChangedListener) {
            BluetoothManager bluetoothManager = (BluetoothManager) this.mContext.getSystemService(BluetoothManager.class);
            Objects.requireNonNull(bluetoothManager);
            bluetoothManager.getAdapter().addOnMetadataChangedListener(BatteryController.getBluetoothDevice(this.mContext, str), this.mExecutor, onMetadataChangedListener);
        }

        @Override // com.android.server.input.BatteryController.BluetoothBatteryManager
        public void removeMetadataListener(String str, BluetoothAdapter.OnMetadataChangedListener onMetadataChangedListener) {
            BluetoothManager bluetoothManager = (BluetoothManager) this.mContext.getSystemService(BluetoothManager.class);
            Objects.requireNonNull(bluetoothManager);
            bluetoothManager.getAdapter().removeOnMetadataChangedListener(BatteryController.getBluetoothDevice(this.mContext, str), onMetadataChangedListener);
        }

        @Override // com.android.server.input.BatteryController.BluetoothBatteryManager
        public byte[] getMetadata(String str, int i) {
            return BatteryController.getBluetoothDevice(this.mContext, str).getMetadata(i);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class State extends IInputDeviceBatteryState {
        State(int i) {
            reset(i);
        }

        State(IInputDeviceBatteryState iInputDeviceBatteryState) {
            copyFrom(iInputDeviceBatteryState);
        }

        State(int i, long j, boolean z, int i2, float f) {
            initialize(i, j, z, i2, f);
        }

        public void updateIfChanged(IInputDeviceBatteryState iInputDeviceBatteryState) {
            if (equalsIgnoringUpdateTime(iInputDeviceBatteryState)) {
                return;
            }
            copyFrom(iInputDeviceBatteryState);
        }

        public void reset(int i) {
            initialize(i, 0L, false, 1, Float.NaN);
        }

        private void copyFrom(IInputDeviceBatteryState iInputDeviceBatteryState) {
            initialize(iInputDeviceBatteryState.deviceId, iInputDeviceBatteryState.updateTime, iInputDeviceBatteryState.isPresent, iInputDeviceBatteryState.status, iInputDeviceBatteryState.capacity);
        }

        private void initialize(int i, long j, boolean z, int i2, float f) {
            ((IInputDeviceBatteryState) this).deviceId = i;
            ((IInputDeviceBatteryState) this).updateTime = j;
            ((IInputDeviceBatteryState) this).isPresent = z;
            ((IInputDeviceBatteryState) this).status = i2;
            ((IInputDeviceBatteryState) this).capacity = f;
        }

        public boolean equalsIgnoringUpdateTime(IInputDeviceBatteryState iInputDeviceBatteryState) {
            long j = ((IInputDeviceBatteryState) this).updateTime;
            ((IInputDeviceBatteryState) this).updateTime = iInputDeviceBatteryState.updateTime;
            boolean equals = equals(iInputDeviceBatteryState);
            ((IInputDeviceBatteryState) this).updateTime = j;
            return equals;
        }

        public String toString() {
            if (!((IInputDeviceBatteryState) this).isPresent) {
                return "State{<not present>}";
            }
            return "State{time=" + ((IInputDeviceBatteryState) this).updateTime + ", isPresent=" + ((IInputDeviceBatteryState) this).isPresent + ", status=" + ((IInputDeviceBatteryState) this).status + ", capacity=" + ((IInputDeviceBatteryState) this).capacity + "}";
        }
    }

    private static <K, V> boolean anyOf(ArrayMap<K, V> arrayMap, Predicate<V> predicate) {
        return findIf(arrayMap, predicate) != null;
    }

    private static <K, V> V findIf(ArrayMap<K, V> arrayMap, Predicate<V> predicate) {
        for (int i = 0; i < arrayMap.size(); i++) {
            V valueAt = arrayMap.valueAt(i);
            if (predicate.test(valueAt)) {
                return valueAt;
            }
        }
        return null;
    }
}
