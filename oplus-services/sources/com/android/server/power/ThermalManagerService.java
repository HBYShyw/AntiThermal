package com.android.server.power;

import android.content.Context;
import android.hardware.thermal.IThermal;
import android.hardware.thermal.IThermalChangedCallback;
import android.hardware.thermal.TemperatureThreshold;
import android.hardware.thermal.V1_0.IThermal;
import android.hardware.thermal.V1_0.ThermalStatus;
import android.hardware.thermal.V1_1.IThermalCallback;
import android.hardware.thermal.V2_0.IThermal;
import android.hardware.thermal.V2_0.IThermalChangedCallback;
import android.os.Binder;
import android.os.CoolingDevice;
import android.os.Handler;
import android.os.IBinder;
import android.os.IHwBinder;
import android.os.IThermalEventListener;
import android.os.IThermalService;
import android.os.IThermalStatusListener;
import android.os.PowerManager;
import android.os.ProjectManager;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ShellCallback;
import android.os.ShellCommand;
import android.os.SystemClock;
import android.os.Temperature;
import android.util.ArrayMap;
import android.util.EventLog;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.DumpUtils;
import com.android.server.FgThread;
import com.android.server.SystemService;
import com.android.server.job.controllers.JobStatus;
import com.android.server.power.ThermalManagerService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ThermalManagerService extends SystemService {
    private static final boolean DEBUG = false;
    public static final int MAX_FORECAST_SEC = 60;
    public static final int MIN_FORECAST_SEC = 0;
    private static final String TAG = "ThermalManagerService";
    private final AtomicBoolean mHalReady;
    private ThermalHalWrapper mHalWrapper;

    @GuardedBy({"mLock"})
    private boolean mIsStatusOverride;
    private final Object mLock;

    @VisibleForTesting
    final IThermalService.Stub mService;

    @GuardedBy({"mLock"})
    private int mStatus;

    @GuardedBy({"mLock"})
    private ArrayMap<String, Temperature> mTemperatureMap;

    @VisibleForTesting
    final TemperatureWatcher mTemperatureWatcher;

    @GuardedBy({"mLock"})
    private final RemoteCallbackList<IThermalEventListener> mThermalEventListeners;

    @GuardedBy({"mLock"})
    private final RemoteCallbackList<IThermalStatusListener> mThermalStatusListeners;

    public ThermalManagerService(Context context) {
        this(context, null);
    }

    @VisibleForTesting
    ThermalManagerService(Context context, ThermalHalWrapper thermalHalWrapper) {
        super(context);
        this.mLock = new Object();
        this.mThermalEventListeners = new RemoteCallbackList<>();
        this.mThermalStatusListeners = new RemoteCallbackList<>();
        this.mTemperatureMap = new ArrayMap<>();
        this.mHalReady = new AtomicBoolean();
        this.mTemperatureWatcher = new TemperatureWatcher();
        this.mService = new IThermalService.Stub() { // from class: com.android.server.power.ThermalManagerService.1
            public boolean registerThermalEventListener(IThermalEventListener iThermalEventListener) {
                ThermalManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                synchronized (ThermalManagerService.this.mLock) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        if (!ThermalManagerService.this.mThermalEventListeners.register(iThermalEventListener, null)) {
                            return false;
                        }
                        ThermalManagerService.this.postEventListenerCurrentTemperatures(iThermalEventListener, null);
                        return true;
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            }

            public boolean registerThermalEventListenerWithType(IThermalEventListener iThermalEventListener, int i) {
                ThermalManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                synchronized (ThermalManagerService.this.mLock) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        if (!ThermalManagerService.this.mThermalEventListeners.register(iThermalEventListener, new Integer(i))) {
                            return false;
                        }
                        ThermalManagerService.this.postEventListenerCurrentTemperatures(iThermalEventListener, new Integer(i));
                        return true;
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            }

            public boolean unregisterThermalEventListener(IThermalEventListener iThermalEventListener) {
                boolean unregister;
                ThermalManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                synchronized (ThermalManagerService.this.mLock) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        unregister = ThermalManagerService.this.mThermalEventListeners.unregister(iThermalEventListener);
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
                return unregister;
            }

            public Temperature[] getCurrentTemperatures() {
                ThermalManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (!ThermalManagerService.this.mHalReady.get()) {
                        return new Temperature[0];
                    }
                    List<Temperature> currentTemperatures = ThermalManagerService.this.mHalWrapper.getCurrentTemperatures(false, 0);
                    return (Temperature[]) currentTemperatures.toArray(new Temperature[currentTemperatures.size()]);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }

            public Temperature[] getCurrentTemperaturesWithType(int i) {
                ThermalManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (!ThermalManagerService.this.mHalReady.get()) {
                        return new Temperature[0];
                    }
                    List<Temperature> currentTemperatures = ThermalManagerService.this.mHalWrapper.getCurrentTemperatures(true, i);
                    return (Temperature[]) currentTemperatures.toArray(new Temperature[currentTemperatures.size()]);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }

            public boolean registerThermalStatusListener(IThermalStatusListener iThermalStatusListener) {
                synchronized (ThermalManagerService.this.mLock) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        if (!ThermalManagerService.this.mThermalStatusListeners.register(iThermalStatusListener)) {
                            return false;
                        }
                        ThermalManagerService.this.postStatusListener(iThermalStatusListener);
                        return true;
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            }

            public boolean unregisterThermalStatusListener(IThermalStatusListener iThermalStatusListener) {
                boolean unregister;
                synchronized (ThermalManagerService.this.mLock) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        unregister = ThermalManagerService.this.mThermalStatusListeners.unregister(iThermalStatusListener);
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
                return unregister;
            }

            public int getCurrentThermalStatus() {
                int i;
                synchronized (ThermalManagerService.this.mLock) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        i = ThermalManagerService.this.mStatus;
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
                return i;
            }

            public CoolingDevice[] getCurrentCoolingDevices() {
                ThermalManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (!ThermalManagerService.this.mHalReady.get()) {
                        return new CoolingDevice[0];
                    }
                    List<CoolingDevice> currentCoolingDevices = ThermalManagerService.this.mHalWrapper.getCurrentCoolingDevices(false, 0);
                    return (CoolingDevice[]) currentCoolingDevices.toArray(new CoolingDevice[currentCoolingDevices.size()]);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }

            public CoolingDevice[] getCurrentCoolingDevicesWithType(int i) {
                ThermalManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (!ThermalManagerService.this.mHalReady.get()) {
                        return new CoolingDevice[0];
                    }
                    List<CoolingDevice> currentCoolingDevices = ThermalManagerService.this.mHalWrapper.getCurrentCoolingDevices(true, i);
                    return (CoolingDevice[]) currentCoolingDevices.toArray(new CoolingDevice[currentCoolingDevices.size()]);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }

            public float getThermalHeadroom(int i) {
                if (ThermalManagerService.this.mHalReady.get() && i >= 0 && i <= 60) {
                    return ThermalManagerService.this.mTemperatureWatcher.getForecast(i);
                }
                return Float.NaN;
            }

            protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
                ThermalManagerService.this.dumpInternal(fileDescriptor, printWriter, strArr);
            }

            private boolean isCallerShell() {
                int callingUid = Binder.getCallingUid();
                return callingUid == 2000 || callingUid == 0;
            }

            /* JADX WARN: Multi-variable type inference failed */
            public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
                if (!isCallerShell()) {
                    Slog.w(ThermalManagerService.TAG, "Only shell is allowed to call thermalservice shell commands");
                } else {
                    new ThermalShellCommand().exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
                }
            }
        };
        this.mHalWrapper = thermalHalWrapper;
        if (thermalHalWrapper != null) {
            thermalHalWrapper.setCallback(new ThermalManagerService$$ExternalSyntheticLambda0(this));
        }
        this.mStatus = 0;
    }

    public void onStart() {
        publishBinderService("thermalservice", this.mService);
    }

    public void onBootPhase(int i) {
        if (i == 550) {
            onActivityManagerReady();
        }
    }

    private void onActivityManagerReady() {
        synchronized (this.mLock) {
            boolean z = this.mHalWrapper != null;
            if (!z) {
                ThermalHalAidlWrapper thermalHalAidlWrapper = new ThermalHalAidlWrapper(new ThermalManagerService$$ExternalSyntheticLambda0(this));
                this.mHalWrapper = thermalHalAidlWrapper;
                z = thermalHalAidlWrapper.connectToHal();
            }
            if (!z) {
                ThermalHal20Wrapper thermalHal20Wrapper = new ThermalHal20Wrapper(new ThermalManagerService$$ExternalSyntheticLambda0(this));
                this.mHalWrapper = thermalHal20Wrapper;
                z = thermalHal20Wrapper.connectToHal();
            }
            if (!z) {
                ThermalHal11Wrapper thermalHal11Wrapper = new ThermalHal11Wrapper(new ThermalManagerService$$ExternalSyntheticLambda0(this));
                this.mHalWrapper = thermalHal11Wrapper;
                z = thermalHal11Wrapper.connectToHal();
            }
            if (!z) {
                ThermalHal10Wrapper thermalHal10Wrapper = new ThermalHal10Wrapper(new ThermalManagerService$$ExternalSyntheticLambda0(this));
                this.mHalWrapper = thermalHal10Wrapper;
                z = thermalHal10Wrapper.connectToHal();
            }
            if (!z) {
                Slog.w(TAG, "No Thermal HAL service on this device");
                return;
            }
            List<Temperature> currentTemperatures = this.mHalWrapper.getCurrentTemperatures(false, 0);
            int size = currentTemperatures.size();
            if (size == 0) {
                Slog.w(TAG, "Thermal HAL reported invalid data, abort connection");
            }
            for (int i = 0; i < size; i++) {
                onTemperatureChanged(currentTemperatures.get(i), false);
            }
            onTemperatureMapChangedLocked();
            this.mTemperatureWatcher.updateSevereThresholds();
            this.mHalReady.set(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postStatusListener(final IThermalStatusListener iThermalStatusListener) {
        if (FgThread.getHandler().post(new Runnable() { // from class: com.android.server.power.ThermalManagerService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ThermalManagerService.this.lambda$postStatusListener$0(iThermalStatusListener);
            }
        })) {
            return;
        }
        Slog.e(TAG, "Thermal callback failed to queue");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postStatusListener$0(IThermalStatusListener iThermalStatusListener) {
        try {
            iThermalStatusListener.onStatusChange(this.mStatus);
        } catch (RemoteException | RuntimeException e) {
            Slog.e(TAG, "Thermal callback failed to call", e);
        }
    }

    private void notifyStatusListenersLocked() {
        int beginBroadcast = this.mThermalStatusListeners.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                postStatusListener(this.mThermalStatusListeners.getBroadcastItem(i));
            } finally {
                this.mThermalStatusListeners.finishBroadcast();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTemperatureMapChangedLocked() {
        int size = this.mTemperatureMap.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            Temperature valueAt = this.mTemperatureMap.valueAt(i2);
            if (valueAt.getType() == 3 && valueAt.getStatus() >= i) {
                i = valueAt.getStatus();
            }
        }
        if (this.mIsStatusOverride) {
            return;
        }
        setStatusLocked(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setStatusLocked(int i) {
        if (i != this.mStatus) {
            this.mStatus = i;
            notifyStatusListenersLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postEventListenerCurrentTemperatures(IThermalEventListener iThermalEventListener, Integer num) {
        synchronized (this.mLock) {
            int size = this.mTemperatureMap.size();
            for (int i = 0; i < size; i++) {
                postEventListener(this.mTemperatureMap.valueAt(i), iThermalEventListener, num);
            }
        }
    }

    private void postEventListener(final Temperature temperature, final IThermalEventListener iThermalEventListener, Integer num) {
        if ((num == null || num.intValue() == temperature.getType()) && !FgThread.getHandler().post(new Runnable() { // from class: com.android.server.power.ThermalManagerService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ThermalManagerService.lambda$postEventListener$1(iThermalEventListener, temperature);
            }
        })) {
            Slog.e(TAG, "Thermal callback failed to queue");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$postEventListener$1(IThermalEventListener iThermalEventListener, Temperature temperature) {
        try {
            iThermalEventListener.notifyThrottling(temperature);
        } catch (RemoteException | RuntimeException e) {
            Slog.e(TAG, "Thermal callback failed to call", e);
        }
    }

    private void notifyEventListenersLocked(Temperature temperature) {
        int beginBroadcast = this.mThermalEventListeners.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                postEventListener(temperature, this.mThermalEventListeners.getBroadcastItem(i), (Integer) this.mThermalEventListeners.getBroadcastCookie(i));
            } catch (Throwable th) {
                this.mThermalEventListeners.finishBroadcast();
                throw th;
            }
        }
        this.mThermalEventListeners.finishBroadcast();
        EventLog.writeEvent(2737, temperature.getName(), Integer.valueOf(temperature.getType()), Float.valueOf(temperature.getValue()), Integer.valueOf(temperature.getStatus()), Integer.valueOf(this.mStatus));
    }

    private void shutdownIfNeeded(Temperature temperature) {
        if (temperature.getStatus() != 6) {
            return;
        }
        PowerManager powerManager = (PowerManager) getContext().getSystemService(PowerManager.class);
        int type = temperature.getType();
        if (type != 0 && type != 1) {
            if (type == 2) {
                powerManager.shutdown(false, "thermal,battery", false);
                return;
            } else if (type != 3 && type != 9) {
                return;
            }
        }
        powerManager.shutdown(false, "thermal", false);
    }

    private void onTemperatureChanged(Temperature temperature, boolean z) {
        if (11 != ProjectManager.getEngVersion()) {
            shutdownIfNeeded(temperature);
        }
        synchronized (this.mLock) {
            Temperature put = this.mTemperatureMap.put(temperature.getName(), temperature);
            if (put == null || put.getStatus() != temperature.getStatus()) {
                notifyEventListenersLocked(temperature);
            }
            if (z) {
                onTemperatureMapChangedLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTemperatureChangedCallback(Temperature temperature) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            onTemperatureChanged(temperature, true);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private static void dumpItemsLocked(PrintWriter printWriter, String str, Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            printWriter.println(str + it.next().toString());
        }
    }

    private static void dumpTemperatureThresholds(PrintWriter printWriter, String str, List<TemperatureThreshold> list) {
        for (TemperatureThreshold temperatureThreshold : list) {
            printWriter.println(str + "TemperatureThreshold{mType=" + temperatureThreshold.type + ", mName=" + temperatureThreshold.name + ", mHotThrottlingThresholds=" + Arrays.toString(temperatureThreshold.hotThrottlingThresholds) + ", mColdThrottlingThresholds=" + Arrays.toString(temperatureThreshold.coldThrottlingThresholds) + "}");
        }
    }

    @VisibleForTesting
    void dumpInternal(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(getContext(), TAG, printWriter)) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (this.mLock) {
                    printWriter.println("IsStatusOverride: " + this.mIsStatusOverride);
                    printWriter.println("ThermalEventListeners:");
                    this.mThermalEventListeners.dump(printWriter, "\t");
                    printWriter.println("ThermalStatusListeners:");
                    this.mThermalStatusListeners.dump(printWriter, "\t");
                    printWriter.println("Thermal Status: " + this.mStatus);
                    printWriter.println("Cached temperatures:");
                    dumpItemsLocked(printWriter, "\t", this.mTemperatureMap.values());
                    printWriter.println("HAL Ready: " + this.mHalReady.get());
                    if (this.mHalReady.get()) {
                        printWriter.println("HAL connection:");
                        this.mHalWrapper.dump(printWriter, "\t");
                        printWriter.println("Current temperatures from HAL:");
                        dumpItemsLocked(printWriter, "\t", this.mHalWrapper.getCurrentTemperatures(false, 0));
                        printWriter.println("Current cooling devices from HAL:");
                        dumpItemsLocked(printWriter, "\t", this.mHalWrapper.getCurrentCoolingDevices(false, 0));
                        printWriter.println("Temperature static thresholds from HAL:");
                        dumpTemperatureThresholds(printWriter, "\t", this.mHalWrapper.getTemperatureThresholds(false, 0));
                    }
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class ThermalShellCommand extends ShellCommand {
        ThermalShellCommand() {
        }

        public int onCommand(String str) {
            String str2 = str != null ? str : "";
            if (str2.equals("reset")) {
                return runReset();
            }
            if (str2.equals("override-status")) {
                return runOverrideStatus();
            }
            return handleDefaultCommands(str);
        }

        private int runReset() {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (ThermalManagerService.this.mLock) {
                    ThermalManagerService.this.mIsStatusOverride = false;
                    ThermalManagerService.this.onTemperatureMapChangedLocked();
                }
                return 0;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        private int runOverrideStatus() {
            PrintWriter outPrintWriter;
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                outPrintWriter = getOutPrintWriter();
                int parseInt = Integer.parseInt(getNextArgRequired());
                if (!Temperature.isValidStatus(parseInt)) {
                    outPrintWriter.println("Invalid status: " + parseInt);
                    return -1;
                }
                synchronized (ThermalManagerService.this.mLock) {
                    ThermalManagerService.this.mIsStatusOverride = true;
                    ThermalManagerService.this.setStatusLocked(parseInt);
                }
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return 0;
            } catch (RuntimeException e) {
                outPrintWriter.println("Error: " + e.toString());
                return -1;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void onHelp() {
            PrintWriter outPrintWriter = getOutPrintWriter();
            outPrintWriter.println("Thermal service (thermalservice) commands:");
            outPrintWriter.println("  help");
            outPrintWriter.println("    Print this help text.");
            outPrintWriter.println("");
            outPrintWriter.println("  override-status STATUS");
            outPrintWriter.println("    sets and locks the thermal status of the device to STATUS.");
            outPrintWriter.println("    status code is defined in android.os.Temperature.");
            outPrintWriter.println("  reset");
            outPrintWriter.println("    unlocks the thermal status of the device.");
            outPrintWriter.println();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static abstract class ThermalHalWrapper {
        protected static final String TAG = "ThermalHalWrapper";
        protected static final int THERMAL_HAL_DEATH_COOKIE = 5612;
        protected TemperatureChangedCallback mCallback;
        protected final Object mHalLock = new Object();

        /* JADX INFO: Access modifiers changed from: package-private */
        @FunctionalInterface
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public interface TemperatureChangedCallback {
            void onValues(Temperature temperature);
        }

        protected abstract boolean connectToHal();

        protected abstract void dump(PrintWriter printWriter, String str);

        protected abstract List<CoolingDevice> getCurrentCoolingDevices(boolean z, int i);

        protected abstract List<Temperature> getCurrentTemperatures(boolean z, int i);

        protected abstract List<TemperatureThreshold> getTemperatureThresholds(boolean z, int i);

        ThermalHalWrapper() {
        }

        @VisibleForTesting
        protected void setCallback(TemperatureChangedCallback temperatureChangedCallback) {
            this.mCallback = temperatureChangedCallback;
        }

        protected void resendCurrentTemperatures() {
            synchronized (this.mHalLock) {
                List<Temperature> currentTemperatures = getCurrentTemperatures(false, 0);
                int size = currentTemperatures.size();
                for (int i = 0; i < size; i++) {
                    this.mCallback.onValues(currentTemperatures.get(i));
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public final class DeathRecipient implements IHwBinder.DeathRecipient {
            DeathRecipient() {
            }

            public void serviceDied(long j) {
                if (j == 5612) {
                    Slog.e(ThermalHalWrapper.TAG, "Thermal HAL service died cookie: " + j);
                    synchronized (ThermalHalWrapper.this.mHalLock) {
                        ThermalHalWrapper.this.connectToHal();
                        ThermalHalWrapper.this.resendCurrentTemperatures();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ThermalHalAidlWrapper extends ThermalHalWrapper implements IBinder.DeathRecipient {
        private IThermal mInstance = null;
        private final IThermalChangedCallback mThermalChangedCallback = new IThermalChangedCallback.Stub() { // from class: com.android.server.power.ThermalManagerService.ThermalHalAidlWrapper.1
            public String getInterfaceHash() throws RemoteException {
                return "76e77ca374a7860f09aeac48e98b2ec61f576767";
            }

            public int getInterfaceVersion() throws RemoteException {
                return 1;
            }

            public void notifyThrottling(android.hardware.thermal.Temperature temperature) throws RemoteException {
                Temperature temperature2 = new Temperature(temperature.value, temperature.type, temperature.name, temperature.throttlingStatus);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    ThermalHalAidlWrapper.this.mCallback.onValues(temperature2);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        };

        ThermalHalAidlWrapper(ThermalHalWrapper.TemperatureChangedCallback temperatureChangedCallback) {
            this.mCallback = temperatureChangedCallback;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected List<Temperature> getCurrentTemperatures(boolean z, int i) {
            android.hardware.thermal.Temperature[] temperatures;
            synchronized (this.mHalLock) {
                ArrayList arrayList = new ArrayList();
                IThermal iThermal = this.mInstance;
                if (iThermal == null) {
                    return arrayList;
                }
                try {
                    try {
                        if (z) {
                            temperatures = iThermal.getTemperaturesWithType(i);
                        } else {
                            temperatures = iThermal.getTemperatures();
                        }
                    } catch (RemoteException e) {
                        Slog.e(ThermalHalWrapper.TAG, "Couldn't getCurrentTemperatures, reconnecting", e);
                        connectToHal();
                    }
                } catch (IllegalArgumentException | IllegalStateException e2) {
                    Slog.e(ThermalHalWrapper.TAG, "Couldn't getCurrentCoolingDevices due to invalid status", e2);
                }
                if (temperatures == null) {
                    return arrayList;
                }
                for (android.hardware.thermal.Temperature temperature : temperatures) {
                    if (!Temperature.isValidStatus(temperature.throttlingStatus)) {
                        Slog.e(ThermalHalWrapper.TAG, "Invalid temperature status " + temperature.throttlingStatus + " received from AIDL HAL");
                        temperature.throttlingStatus = 0;
                    }
                    if (!z || temperature.type == i) {
                        arrayList.add(new Temperature(temperature.value, temperature.type, temperature.name, temperature.throttlingStatus));
                    }
                }
                return arrayList;
            }
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected List<CoolingDevice> getCurrentCoolingDevices(boolean z, int i) {
            android.hardware.thermal.CoolingDevice[] coolingDevices;
            synchronized (this.mHalLock) {
                ArrayList arrayList = new ArrayList();
                IThermal iThermal = this.mInstance;
                if (iThermal == null) {
                    return arrayList;
                }
                try {
                    try {
                        if (z) {
                            coolingDevices = iThermal.getCoolingDevicesWithType(i);
                        } else {
                            coolingDevices = iThermal.getCoolingDevices();
                        }
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        Slog.e(ThermalHalWrapper.TAG, "Couldn't getCurrentCoolingDevices due to invalid status", e);
                    }
                } catch (RemoteException e2) {
                    Slog.e(ThermalHalWrapper.TAG, "Couldn't getCurrentCoolingDevices, reconnecting", e2);
                    connectToHal();
                }
                if (coolingDevices == null) {
                    return arrayList;
                }
                for (android.hardware.thermal.CoolingDevice coolingDevice : coolingDevices) {
                    if (!CoolingDevice.isValidType(coolingDevice.type)) {
                        Slog.e(ThermalHalWrapper.TAG, "Invalid cooling device type " + coolingDevice.type + " from AIDL HAL");
                    } else if (!z || coolingDevice.type == i) {
                        arrayList.add(new CoolingDevice(coolingDevice.value, coolingDevice.type, coolingDevice.name));
                    }
                }
                return arrayList;
            }
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected List<TemperatureThreshold> getTemperatureThresholds(boolean z, final int i) {
            TemperatureThreshold[] temperatureThresholds;
            synchronized (this.mHalLock) {
                ArrayList arrayList = new ArrayList();
                IThermal iThermal = this.mInstance;
                if (iThermal == null) {
                    return arrayList;
                }
                try {
                    try {
                        if (z) {
                            temperatureThresholds = iThermal.getTemperatureThresholdsWithType(i);
                        } else {
                            temperatureThresholds = iThermal.getTemperatureThresholds();
                        }
                        if (temperatureThresholds == null) {
                            return arrayList;
                        }
                        if (z) {
                            return (List) Arrays.stream(temperatureThresholds).filter(new Predicate() { // from class: com.android.server.power.ThermalManagerService$ThermalHalAidlWrapper$$ExternalSyntheticLambda0
                                @Override // java.util.function.Predicate
                                public final boolean test(Object obj) {
                                    boolean lambda$getTemperatureThresholds$0;
                                    lambda$getTemperatureThresholds$0 = ThermalManagerService.ThermalHalAidlWrapper.lambda$getTemperatureThresholds$0(i, (TemperatureThreshold) obj);
                                    return lambda$getTemperatureThresholds$0;
                                }
                            }).collect(Collectors.toList());
                        }
                        return Arrays.asList(temperatureThresholds);
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        Slog.e(ThermalHalWrapper.TAG, "Couldn't getTemperatureThresholds due to invalid status", e);
                        return arrayList;
                    }
                } catch (RemoteException e2) {
                    Slog.e(ThermalHalWrapper.TAG, "Couldn't getTemperatureThresholds, reconnecting...", e2);
                    connectToHal();
                    return arrayList;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$getTemperatureThresholds$0(int i, TemperatureThreshold temperatureThreshold) {
            return temperatureThreshold.type == i;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected boolean connectToHal() {
            synchronized (this.mHalLock) {
                initProxyAndRegisterCallback(Binder.allowBlocking(ServiceManager.waitForDeclaredService(IThermal.DESCRIPTOR + "/default")));
            }
            return this.mInstance != null;
        }

        @VisibleForTesting
        void initProxyAndRegisterCallback(IBinder iBinder) {
            synchronized (this.mHalLock) {
                if (iBinder != null) {
                    this.mInstance = IThermal.Stub.asInterface(iBinder);
                    try {
                        iBinder.linkToDeath(this, 0);
                    } catch (RemoteException e) {
                        Slog.e(ThermalHalWrapper.TAG, "Unable to connect IThermal AIDL instance", e);
                        connectToHal();
                    }
                    if (this.mInstance != null) {
                        Slog.i(ThermalHalWrapper.TAG, "Thermal HAL AIDL service connected.");
                        registerThermalChangedCallback();
                    }
                }
            }
        }

        @VisibleForTesting
        void registerThermalChangedCallback() {
            try {
                this.mInstance.registerThermalChangedCallback(this.mThermalChangedCallback);
            } catch (RemoteException e) {
                Slog.e(ThermalHalWrapper.TAG, "Unable to connect IThermal AIDL instance", e);
                connectToHal();
            } catch (IllegalArgumentException | IllegalStateException e2) {
                Slog.e(ThermalHalWrapper.TAG, "Couldn't registerThermalChangedCallback due to invalid status", e2);
            }
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected void dump(PrintWriter printWriter, String str) {
            synchronized (this.mHalLock) {
                printWriter.print(str);
                StringBuilder sb = new StringBuilder();
                sb.append("ThermalHAL AIDL 1  connected: ");
                sb.append(this.mInstance != null ? "yes" : "no");
                printWriter.println(sb.toString());
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public synchronized void binderDied() {
            Slog.w(ThermalHalWrapper.TAG, "Thermal AIDL HAL died, reconnecting...");
            connectToHal();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ThermalHal10Wrapper extends ThermalHalWrapper {

        @GuardedBy({"mHalLock"})
        private android.hardware.thermal.V1_0.IThermal mThermalHal10 = null;

        ThermalHal10Wrapper(ThermalHalWrapper.TemperatureChangedCallback temperatureChangedCallback) {
            this.mCallback = temperatureChangedCallback;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected List<Temperature> getCurrentTemperatures(final boolean z, final int i) {
            synchronized (this.mHalLock) {
                final ArrayList arrayList = new ArrayList();
                android.hardware.thermal.V1_0.IThermal iThermal = this.mThermalHal10;
                if (iThermal == null) {
                    return arrayList;
                }
                try {
                    iThermal.getTemperatures(new IThermal.getTemperaturesCallback() { // from class: com.android.server.power.ThermalManagerService$ThermalHal10Wrapper$$ExternalSyntheticLambda0
                        public final void onValues(ThermalStatus thermalStatus, ArrayList arrayList2) {
                            ThermalManagerService.ThermalHal10Wrapper.lambda$getCurrentTemperatures$0(z, i, arrayList, thermalStatus, arrayList2);
                        }
                    });
                } catch (RemoteException e) {
                    Slog.e(ThermalHalWrapper.TAG, "Couldn't getCurrentTemperatures, reconnecting...", e);
                    connectToHal();
                }
                return arrayList;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$getCurrentTemperatures$0(boolean z, int i, List list, ThermalStatus thermalStatus, ArrayList arrayList) {
            if (thermalStatus.code == 0) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    android.hardware.thermal.V1_0.Temperature temperature = (android.hardware.thermal.V1_0.Temperature) it.next();
                    if (!z || i == temperature.type) {
                        list.add(new Temperature(temperature.currentValue, temperature.type, temperature.name, 0));
                    }
                }
                return;
            }
            Slog.e(ThermalHalWrapper.TAG, "Couldn't get temperatures because of HAL error: " + thermalStatus.debugMessage);
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected List<CoolingDevice> getCurrentCoolingDevices(final boolean z, final int i) {
            synchronized (this.mHalLock) {
                final ArrayList arrayList = new ArrayList();
                android.hardware.thermal.V1_0.IThermal iThermal = this.mThermalHal10;
                if (iThermal == null) {
                    return arrayList;
                }
                try {
                    iThermal.getCoolingDevices(new IThermal.getCoolingDevicesCallback() { // from class: com.android.server.power.ThermalManagerService$ThermalHal10Wrapper$$ExternalSyntheticLambda1
                        public final void onValues(ThermalStatus thermalStatus, ArrayList arrayList2) {
                            ThermalManagerService.ThermalHal10Wrapper.lambda$getCurrentCoolingDevices$1(z, i, arrayList, thermalStatus, arrayList2);
                        }
                    });
                } catch (RemoteException e) {
                    Slog.e(ThermalHalWrapper.TAG, "Couldn't getCurrentCoolingDevices, reconnecting...", e);
                    connectToHal();
                }
                return arrayList;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$getCurrentCoolingDevices$1(boolean z, int i, List list, ThermalStatus thermalStatus, ArrayList arrayList) {
            if (thermalStatus.code == 0) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    android.hardware.thermal.V1_0.CoolingDevice coolingDevice = (android.hardware.thermal.V1_0.CoolingDevice) it.next();
                    if (!z || i == coolingDevice.type) {
                        list.add(new CoolingDevice(coolingDevice.currentValue, coolingDevice.type, coolingDevice.name));
                    }
                }
                return;
            }
            Slog.e(ThermalHalWrapper.TAG, "Couldn't get cooling device because of HAL error: " + thermalStatus.debugMessage);
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected List<TemperatureThreshold> getTemperatureThresholds(boolean z, int i) {
            return new ArrayList();
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected boolean connectToHal() {
            boolean z;
            synchronized (this.mHalLock) {
                z = true;
                try {
                    android.hardware.thermal.V1_0.IThermal service = android.hardware.thermal.V1_0.IThermal.getService(true);
                    this.mThermalHal10 = service;
                    service.linkToDeath(new ThermalHalWrapper.DeathRecipient(), 5612L);
                    Slog.i(ThermalHalWrapper.TAG, "Thermal HAL 1.0 service connected, no thermal call back will be called due to legacy API.");
                } catch (RemoteException | NoSuchElementException unused) {
                    Slog.e(ThermalHalWrapper.TAG, "Thermal HAL 1.0 service not connected.");
                    this.mThermalHal10 = null;
                }
                if (this.mThermalHal10 == null) {
                    z = false;
                }
            }
            return z;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected void dump(PrintWriter printWriter, String str) {
            synchronized (this.mHalLock) {
                printWriter.print(str);
                StringBuilder sb = new StringBuilder();
                sb.append("ThermalHAL 1.0 connected: ");
                sb.append(this.mThermalHal10 != null ? "yes" : "no");
                printWriter.println(sb.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ThermalHal11Wrapper extends ThermalHalWrapper {

        @GuardedBy({"mHalLock"})
        private android.hardware.thermal.V1_1.IThermal mThermalHal11 = null;
        private final IThermalCallback.Stub mThermalCallback11 = new IThermalCallback.Stub() { // from class: com.android.server.power.ThermalManagerService.ThermalHal11Wrapper.1
            public void notifyThrottling(boolean z, android.hardware.thermal.V1_0.Temperature temperature) {
                Temperature temperature2 = new Temperature(temperature.currentValue, temperature.type, temperature.name, z ? 3 : 0);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    ThermalHal11Wrapper.this.mCallback.onValues(temperature2);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        };

        ThermalHal11Wrapper(ThermalHalWrapper.TemperatureChangedCallback temperatureChangedCallback) {
            this.mCallback = temperatureChangedCallback;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected List<Temperature> getCurrentTemperatures(final boolean z, final int i) {
            synchronized (this.mHalLock) {
                final ArrayList arrayList = new ArrayList();
                android.hardware.thermal.V1_1.IThermal iThermal = this.mThermalHal11;
                if (iThermal == null) {
                    return arrayList;
                }
                try {
                    iThermal.getTemperatures(new IThermal.getTemperaturesCallback() { // from class: com.android.server.power.ThermalManagerService$ThermalHal11Wrapper$$ExternalSyntheticLambda0
                        public final void onValues(ThermalStatus thermalStatus, ArrayList arrayList2) {
                            ThermalManagerService.ThermalHal11Wrapper.lambda$getCurrentTemperatures$0(z, i, arrayList, thermalStatus, arrayList2);
                        }
                    });
                } catch (RemoteException e) {
                    Slog.e(ThermalHalWrapper.TAG, "Couldn't getCurrentTemperatures, reconnecting...", e);
                    connectToHal();
                }
                return arrayList;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$getCurrentTemperatures$0(boolean z, int i, List list, ThermalStatus thermalStatus, ArrayList arrayList) {
            if (thermalStatus.code == 0) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    android.hardware.thermal.V1_0.Temperature temperature = (android.hardware.thermal.V1_0.Temperature) it.next();
                    if (!z || i == temperature.type) {
                        list.add(new Temperature(temperature.currentValue, temperature.type, temperature.name, 0));
                    }
                }
                return;
            }
            Slog.e(ThermalHalWrapper.TAG, "Couldn't get temperatures because of HAL error: " + thermalStatus.debugMessage);
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected List<CoolingDevice> getCurrentCoolingDevices(final boolean z, final int i) {
            synchronized (this.mHalLock) {
                final ArrayList arrayList = new ArrayList();
                android.hardware.thermal.V1_1.IThermal iThermal = this.mThermalHal11;
                if (iThermal == null) {
                    return arrayList;
                }
                try {
                    iThermal.getCoolingDevices(new IThermal.getCoolingDevicesCallback() { // from class: com.android.server.power.ThermalManagerService$ThermalHal11Wrapper$$ExternalSyntheticLambda1
                        public final void onValues(ThermalStatus thermalStatus, ArrayList arrayList2) {
                            ThermalManagerService.ThermalHal11Wrapper.lambda$getCurrentCoolingDevices$1(z, i, arrayList, thermalStatus, arrayList2);
                        }
                    });
                } catch (RemoteException e) {
                    Slog.e(ThermalHalWrapper.TAG, "Couldn't getCurrentCoolingDevices, reconnecting...", e);
                    connectToHal();
                }
                return arrayList;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$getCurrentCoolingDevices$1(boolean z, int i, List list, ThermalStatus thermalStatus, ArrayList arrayList) {
            if (thermalStatus.code == 0) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    android.hardware.thermal.V1_0.CoolingDevice coolingDevice = (android.hardware.thermal.V1_0.CoolingDevice) it.next();
                    if (!z || i == coolingDevice.type) {
                        list.add(new CoolingDevice(coolingDevice.currentValue, coolingDevice.type, coolingDevice.name));
                    }
                }
                return;
            }
            Slog.e(ThermalHalWrapper.TAG, "Couldn't get cooling device because of HAL error: " + thermalStatus.debugMessage);
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected List<TemperatureThreshold> getTemperatureThresholds(boolean z, int i) {
            return new ArrayList();
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected boolean connectToHal() {
            boolean z;
            synchronized (this.mHalLock) {
                z = true;
                try {
                    android.hardware.thermal.V1_1.IThermal service = android.hardware.thermal.V1_1.IThermal.getService(true);
                    this.mThermalHal11 = service;
                    service.linkToDeath(new ThermalHalWrapper.DeathRecipient(), 5612L);
                    this.mThermalHal11.registerThermalCallback(this.mThermalCallback11);
                    Slog.i(ThermalHalWrapper.TAG, "Thermal HAL 1.1 service connected, limited thermal functions due to legacy API.");
                } catch (RemoteException | NoSuchElementException unused) {
                    Slog.e(ThermalHalWrapper.TAG, "Thermal HAL 1.1 service not connected.");
                    this.mThermalHal11 = null;
                }
                if (this.mThermalHal11 == null) {
                    z = false;
                }
            }
            return z;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected void dump(PrintWriter printWriter, String str) {
            synchronized (this.mHalLock) {
                printWriter.print(str);
                StringBuilder sb = new StringBuilder();
                sb.append("ThermalHAL 1.1 connected: ");
                sb.append(this.mThermalHal11 != null ? "yes" : "no");
                printWriter.println(sb.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ThermalHal20Wrapper extends ThermalHalWrapper {

        @GuardedBy({"mHalLock"})
        private android.hardware.thermal.V2_0.IThermal mThermalHal20 = null;
        private final IThermalChangedCallback.Stub mThermalCallback20 = new IThermalChangedCallback.Stub() { // from class: com.android.server.power.ThermalManagerService.ThermalHal20Wrapper.1
            public void notifyThrottling(android.hardware.thermal.V2_0.Temperature temperature) {
                Temperature temperature2 = new Temperature(temperature.value, temperature.type, temperature.name, temperature.throttlingStatus);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    ThermalHal20Wrapper.this.mCallback.onValues(temperature2);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        };

        ThermalHal20Wrapper(ThermalHalWrapper.TemperatureChangedCallback temperatureChangedCallback) {
            this.mCallback = temperatureChangedCallback;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected List<Temperature> getCurrentTemperatures(boolean z, int i) {
            synchronized (this.mHalLock) {
                final ArrayList arrayList = new ArrayList();
                android.hardware.thermal.V2_0.IThermal iThermal = this.mThermalHal20;
                if (iThermal == null) {
                    return arrayList;
                }
                try {
                    iThermal.getCurrentTemperatures(z, i, new IThermal.getCurrentTemperaturesCallback() { // from class: com.android.server.power.ThermalManagerService$ThermalHal20Wrapper$$ExternalSyntheticLambda1
                        public final void onValues(ThermalStatus thermalStatus, ArrayList arrayList2) {
                            ThermalManagerService.ThermalHal20Wrapper.lambda$getCurrentTemperatures$0(arrayList, thermalStatus, arrayList2);
                        }
                    });
                } catch (RemoteException e) {
                    Slog.e(ThermalHalWrapper.TAG, "Couldn't getCurrentTemperatures, reconnecting...", e);
                    connectToHal();
                }
                return arrayList;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$getCurrentTemperatures$0(List list, ThermalStatus thermalStatus, ArrayList arrayList) {
            if (thermalStatus.code == 0) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    android.hardware.thermal.V2_0.Temperature temperature = (android.hardware.thermal.V2_0.Temperature) it.next();
                    if (!Temperature.isValidStatus(temperature.throttlingStatus)) {
                        Slog.e(ThermalHalWrapper.TAG, "Invalid status data from HAL");
                        temperature.throttlingStatus = 0;
                    }
                    list.add(new Temperature(temperature.value, temperature.type, temperature.name, temperature.throttlingStatus));
                }
                return;
            }
            Slog.e(ThermalHalWrapper.TAG, "Couldn't get temperatures because of HAL error: " + thermalStatus.debugMessage);
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected List<CoolingDevice> getCurrentCoolingDevices(boolean z, int i) {
            synchronized (this.mHalLock) {
                final ArrayList arrayList = new ArrayList();
                android.hardware.thermal.V2_0.IThermal iThermal = this.mThermalHal20;
                if (iThermal == null) {
                    return arrayList;
                }
                try {
                    iThermal.getCurrentCoolingDevices(z, i, new IThermal.getCurrentCoolingDevicesCallback() { // from class: com.android.server.power.ThermalManagerService$ThermalHal20Wrapper$$ExternalSyntheticLambda0
                        public final void onValues(ThermalStatus thermalStatus, ArrayList arrayList2) {
                            ThermalManagerService.ThermalHal20Wrapper.lambda$getCurrentCoolingDevices$1(arrayList, thermalStatus, arrayList2);
                        }
                    });
                } catch (RemoteException e) {
                    Slog.e(ThermalHalWrapper.TAG, "Couldn't getCurrentCoolingDevices, reconnecting...", e);
                    connectToHal();
                }
                return arrayList;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$getCurrentCoolingDevices$1(List list, ThermalStatus thermalStatus, ArrayList arrayList) {
            if (thermalStatus.code == 0) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    android.hardware.thermal.V2_0.CoolingDevice coolingDevice = (android.hardware.thermal.V2_0.CoolingDevice) it.next();
                    list.add(new CoolingDevice(coolingDevice.value, coolingDevice.type, coolingDevice.name));
                }
                return;
            }
            Slog.e(ThermalHalWrapper.TAG, "Couldn't get cooling device because of HAL error: " + thermalStatus.debugMessage);
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected List<TemperatureThreshold> getTemperatureThresholds(boolean z, int i) {
            synchronized (this.mHalLock) {
                final ArrayList arrayList = new ArrayList();
                android.hardware.thermal.V2_0.IThermal iThermal = this.mThermalHal20;
                if (iThermal == null) {
                    return arrayList;
                }
                try {
                    iThermal.getTemperatureThresholds(z, i, new IThermal.getTemperatureThresholdsCallback() { // from class: com.android.server.power.ThermalManagerService$ThermalHal20Wrapper$$ExternalSyntheticLambda2
                        public final void onValues(ThermalStatus thermalStatus, ArrayList arrayList2) {
                            ThermalManagerService.ThermalHal20Wrapper.this.lambda$getTemperatureThresholds$2(arrayList, thermalStatus, arrayList2);
                        }
                    });
                } catch (RemoteException e) {
                    Slog.e(ThermalHalWrapper.TAG, "Couldn't getTemperatureThresholds, reconnecting...", e);
                }
                return arrayList;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getTemperatureThresholds$2(List list, ThermalStatus thermalStatus, ArrayList arrayList) {
            if (thermalStatus.code == 0) {
                list.addAll((Collection) arrayList.stream().map(new Function() { // from class: com.android.server.power.ThermalManagerService$ThermalHal20Wrapper$$ExternalSyntheticLambda3
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        TemperatureThreshold convertToAidlTemperatureThreshold;
                        convertToAidlTemperatureThreshold = ThermalManagerService.ThermalHal20Wrapper.this.convertToAidlTemperatureThreshold((android.hardware.thermal.V2_0.TemperatureThreshold) obj);
                        return convertToAidlTemperatureThreshold;
                    }
                }).collect(Collectors.toList()));
                return;
            }
            Slog.e(ThermalHalWrapper.TAG, "Couldn't get temperature thresholds because of HAL error: " + thermalStatus.debugMessage);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public TemperatureThreshold convertToAidlTemperatureThreshold(android.hardware.thermal.V2_0.TemperatureThreshold temperatureThreshold) {
            TemperatureThreshold temperatureThreshold2 = new TemperatureThreshold();
            temperatureThreshold2.name = temperatureThreshold.name;
            temperatureThreshold2.type = temperatureThreshold.type;
            temperatureThreshold2.coldThrottlingThresholds = temperatureThreshold.coldThrottlingThresholds;
            temperatureThreshold2.hotThrottlingThresholds = temperatureThreshold.hotThrottlingThresholds;
            return temperatureThreshold2;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected boolean connectToHal() {
            boolean z;
            synchronized (this.mHalLock) {
                z = true;
                try {
                    android.hardware.thermal.V2_0.IThermal service = android.hardware.thermal.V2_0.IThermal.getService(true);
                    this.mThermalHal20 = service;
                    service.linkToDeath(new ThermalHalWrapper.DeathRecipient(), 5612L);
                    this.mThermalHal20.registerThermalChangedCallback(this.mThermalCallback20, false, 0);
                    Slog.i(ThermalHalWrapper.TAG, "Thermal HAL 2.0 service connected.");
                } catch (RemoteException | NoSuchElementException unused) {
                    Slog.e(ThermalHalWrapper.TAG, "Thermal HAL 2.0 service not connected.");
                    this.mThermalHal20 = null;
                }
                if (this.mThermalHal20 == null) {
                    z = false;
                }
            }
            return z;
        }

        @Override // com.android.server.power.ThermalManagerService.ThermalHalWrapper
        protected void dump(PrintWriter printWriter, String str) {
            synchronized (this.mHalLock) {
                printWriter.print(str);
                StringBuilder sb = new StringBuilder();
                sb.append("ThermalHAL 2.0 connected: ");
                sb.append(this.mThermalHal20 != null ? "yes" : "no");
                printWriter.println(sb.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class TemperatureWatcher {
        private static final float DEGREES_BETWEEN_ZERO_AND_ONE = 30.0f;
        private static final int INACTIVITY_THRESHOLD_MILLIS = 10000;
        private static final int MINIMUM_SAMPLE_COUNT = 3;
        private static final int RING_BUFFER_SIZE = 30;
        private final Handler mHandler = BackgroundThread.getHandler();

        @GuardedBy({"mSamples"})
        @VisibleForTesting
        final ArrayMap<String, ArrayList<Sample>> mSamples = new ArrayMap<>();

        @GuardedBy({"mSamples"})
        @VisibleForTesting
        ArrayMap<String, Float> mSevereThresholds = new ArrayMap<>();

        @GuardedBy({"mSamples"})
        private long mLastForecastCallTimeMillis = 0;

        @VisibleForTesting
        long mInactivityThresholdMillis = JobStatus.DEFAULT_TRIGGER_UPDATE_DELAY;

        TemperatureWatcher() {
        }

        void updateSevereThresholds() {
            synchronized (this.mSamples) {
                List<TemperatureThreshold> temperatureThresholds = ThermalManagerService.this.mHalWrapper.getTemperatureThresholds(true, 3);
                for (int i = 0; i < temperatureThresholds.size(); i++) {
                    TemperatureThreshold temperatureThreshold = temperatureThresholds.get(i);
                    float[] fArr = temperatureThreshold.hotThrottlingThresholds;
                    if (fArr.length > 3 && !Float.isNaN(fArr[3])) {
                        this.mSevereThresholds.put(temperatureThreshold.name, Float.valueOf(temperatureThreshold.hotThrottlingThresholds[3]));
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateTemperature() {
            synchronized (this.mSamples) {
                if (SystemClock.elapsedRealtime() - this.mLastForecastCallTimeMillis < this.mInactivityThresholdMillis) {
                    this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.power.ThermalManagerService$TemperatureWatcher$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            ThermalManagerService.TemperatureWatcher.this.updateTemperature();
                        }
                    }, 1000L);
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    List<Temperature> currentTemperatures = ThermalManagerService.this.mHalWrapper.getCurrentTemperatures(true, 3);
                    for (int i = 0; i < currentTemperatures.size(); i++) {
                        Temperature temperature = currentTemperatures.get(i);
                        if (!Float.isNaN(temperature.getValue())) {
                            ArrayList<Sample> computeIfAbsent = this.mSamples.computeIfAbsent(temperature.getName(), new Function() { // from class: com.android.server.power.ThermalManagerService$TemperatureWatcher$$ExternalSyntheticLambda1
                                @Override // java.util.function.Function
                                public final Object apply(Object obj) {
                                    ArrayList lambda$updateTemperature$0;
                                    lambda$updateTemperature$0 = ThermalManagerService.TemperatureWatcher.lambda$updateTemperature$0((String) obj);
                                    return lambda$updateTemperature$0;
                                }
                            });
                            if (computeIfAbsent.size() == 30) {
                                computeIfAbsent.remove(0);
                            }
                            computeIfAbsent.add(new Sample(elapsedRealtime, temperature.getValue()));
                        }
                    }
                    return;
                }
                this.mSamples.clear();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ ArrayList lambda$updateTemperature$0(String str) {
            return new ArrayList(30);
        }

        @VisibleForTesting
        float getSlopeOf(List<Sample> list) {
            long j = 0;
            float f = 0.0f;
            float f2 = 0.0f;
            long j2 = 0;
            for (int i = 0; i < list.size(); i++) {
                Sample sample = list.get(i);
                j2 += sample.time;
                f2 += sample.temperature;
            }
            long size = j2 / list.size();
            float size2 = f2 / list.size();
            for (int i2 = 0; i2 < list.size(); i2++) {
                Sample sample2 = list.get(i2);
                long j3 = sample2.time - size;
                j += j3 * j3;
                f += ((float) j3) * (sample2.temperature - size2);
            }
            return f / ((float) j);
        }

        @VisibleForTesting
        float normalizeTemperature(float f, float f2) {
            synchronized (this.mSamples) {
                float f3 = f2 - DEGREES_BETWEEN_ZERO_AND_ONE;
                if (f <= f3) {
                    return 0.0f;
                }
                return (f - f3) / DEGREES_BETWEEN_ZERO_AND_ONE;
            }
        }

        float getForecast(int i) {
            float normalizeTemperature;
            synchronized (this.mSamples) {
                this.mLastForecastCallTimeMillis = SystemClock.elapsedRealtime();
                if (this.mSamples.isEmpty()) {
                    updateTemperature();
                }
                float f = Float.NaN;
                if (this.mSamples.isEmpty()) {
                    Slog.e(ThermalManagerService.TAG, "No temperature samples found");
                    return Float.NaN;
                }
                if (this.mSevereThresholds.isEmpty()) {
                    Slog.e(ThermalManagerService.TAG, "No temperature thresholds found");
                    return Float.NaN;
                }
                for (Map.Entry<String, ArrayList<Sample>> entry : this.mSamples.entrySet()) {
                    String key = entry.getKey();
                    ArrayList<Sample> value = entry.getValue();
                    Float f2 = this.mSevereThresholds.get(key);
                    if (f2 == null) {
                        Slog.e(ThermalManagerService.TAG, "No threshold found for " + key);
                    } else {
                        float f3 = value.get(0).temperature;
                        if (value.size() < 3) {
                            normalizeTemperature = normalizeTemperature(f3, f2.floatValue());
                            if (!Float.isNaN(f) && normalizeTemperature <= f) {
                            }
                            f = normalizeTemperature;
                        } else {
                            normalizeTemperature = normalizeTemperature(f3 + (getSlopeOf(value) * i * 1000.0f), f2.floatValue());
                            if (!Float.isNaN(f) && normalizeTemperature <= f) {
                            }
                            f = normalizeTemperature;
                        }
                    }
                }
                return f;
            }
        }

        @VisibleForTesting
        Sample createSampleForTesting(long j, float f) {
            return new Sample(j, f);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @VisibleForTesting
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public class Sample {
            public float temperature;
            public long time;

            Sample(long j, float f) {
                this.time = j;
                this.temperature = f;
            }
        }
    }
}
