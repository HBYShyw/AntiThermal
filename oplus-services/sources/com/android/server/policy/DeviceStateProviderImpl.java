package com.android.server.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import com.android.server.DisplayThread;
import com.android.server.LocalServices;
import com.android.server.devicestate.DeviceState;
import com.android.server.devicestate.DeviceStateProvider;
import com.android.server.input.InputManagerInternal;
import com.android.server.policy.devicestate.config.CameraCondition;
import com.android.server.policy.devicestate.config.Conditions;
import com.android.server.policy.devicestate.config.DeviceStateConfig;
import com.android.server.policy.devicestate.config.DisplayCondition;
import com.android.server.policy.devicestate.config.Flags;
import com.android.server.policy.devicestate.config.KeyguardCondition;
import com.android.server.policy.devicestate.config.LidSwitchCondition;
import com.android.server.policy.devicestate.config.NumericRange;
import com.android.server.policy.devicestate.config.SensorCondition;
import com.android.server.policy.devicestate.config.XmlParser;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.ToIntFunction;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DeviceStateProviderImpl implements DeviceStateProvider, InputManagerInternal.LidSwitchCallback, SensorEventListener, PowerManager.OnThermalStatusChangedListener {
    private static final String CONFIG_FILE_NAME = "device_state_configuration.xml";
    private static final String DATA_CONFIG_FILE_PATH = "system/devicestate/";
    private static final String FLAG_APP_INACCESSIBLE = "FLAG_APP_INACCESSIBLE";
    private static final String FLAG_CANCEL_OVERRIDE_REQUESTS = "FLAG_CANCEL_OVERRIDE_REQUESTS";
    private static final String FLAG_CANCEL_WHEN_REQUESTER_NOT_ON_TOP = "FLAG_CANCEL_WHEN_REQUESTER_NOT_ON_TOP";
    private static final String FLAG_EMULATED_ONLY = "FLAG_EMULATED_ONLY";
    private static final String FLAG_UNSUPPORTED_WHEN_POWER_SAVE_MODE = "FLAG_UNSUPPORTED_WHEN_POWER_SAVE_MODE";
    private static final String FLAG_UNSUPPORTED_WHEN_THERMAL_STATUS_CRITICAL = "FLAG_UNSUPPORTED_WHEN_THERMAL_STATUS_CRITICAL";
    private static final String ODM_CONFIG_FILE_NAME = "device_state_configuration_private.xml";
    private static final String ODM_CONFIG_FILE_PATH = "etc/devicestate/";
    private static final String TAG = "DeviceStateProviderImpl";
    private static final String TYPE_SENSOR_HINGE_DETECT = "qti.sensor.hinge_detect";
    private static final String VENDOR_CONFIG_FILE_PATH = "etc/devicestate/";
    private CameraAvailabilityCallback mCameraAvailabilityCallback;
    private CameraManager mCameraManager;
    private boolean mCameraOpen;
    private final Context mContext;

    @GuardedBy({"mLock"})
    private Boolean mIsLidOpen;
    private boolean mKeyguardShow;
    private final DeviceState[] mOrderedStates;

    @GuardedBy({"mLock"})
    private boolean mPowerSaveModeEnabled;
    private String mSystemCameraName;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final BooleanSupplier TRUE_BOOLEAN_SUPPLIER = new BooleanSupplier() { // from class: com.android.server.policy.DeviceStateProviderImpl$$ExternalSyntheticLambda1
        @Override // java.util.function.BooleanSupplier
        public final boolean getAsBoolean() {
            boolean lambda$static$0;
            lambda$static$0 = DeviceStateProviderImpl.lambda$static$0();
            return lambda$static$0;
        }
    };
    private static final BooleanSupplier FALSE_BOOLEAN_SUPPLIER = new BooleanSupplier() { // from class: com.android.server.policy.DeviceStateProviderImpl$$ExternalSyntheticLambda2
        @Override // java.util.function.BooleanSupplier
        public final boolean getAsBoolean() {
            boolean lambda$static$1;
            lambda$static$1 = DeviceStateProviderImpl.lambda$static$1();
            return lambda$static$1;
        }
    };

    @VisibleForTesting
    static final DeviceState DEFAULT_DEVICE_STATE = new DeviceState(0, "DEFAULT", 0);
    public static IDeviceStateProviderImplExt sExtImpl = (IDeviceStateProviderImplExt) ExtLoader.type(IDeviceStateProviderImplExt.class).create();
    private final Object mLock = new Object();
    private final SparseArray<BooleanSupplier> mStateConditions = new SparseArray<>();

    @GuardedBy({"mLock"})
    private DeviceStateProvider.Listener mListener = null;

    @GuardedBy({"mLock"})
    private int mLastReportedState = -1;

    @GuardedBy({"mLock"})
    private final Map<Sensor, SensorEvent> mLatestSensorEvent = new ArrayMap();

    @GuardedBy({"mLock"})
    private int mThermalStatus = 0;
    private Sensor mHingeSensor = null;
    private Sensor mGSensor = null;
    private DeviceStateProviderImplWrapper mDsplWrapper = new DeviceStateProviderImplWrapper();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ReadableConfig {
        InputStream openRead() throws IOException;
    }

    private static boolean isThermalStatusCriticalOrAbove(int i) {
        return i == 4 || i == 5 || i == 6;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$static$0() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$static$1() {
        return false;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public static DeviceStateProviderImpl create(Context context) {
        File configurationFile = getConfigurationFile();
        if (configurationFile == null) {
            return createFromConfig(context, null);
        }
        return createFromConfig(context, new ReadableFileConfig(configurationFile));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00a1, code lost:
    
        switch(r11) {
            case 0: goto L53;
            case 1: goto L52;
            case 2: goto L51;
            case 3: goto L50;
            case 4: goto L48;
            case 5: goto L47;
            default: goto L49;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00a5, code lost:
    
        r8 = r8 | 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00cc, code lost:
    
        r7 = r7 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00a8, code lost:
    
        r8 = r8 | 32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00c1, code lost:
    
        r8 = r8 | 8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00c4, code lost:
    
        r8 = r8 | 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00c7, code lost:
    
        r8 = r8 | 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00ca, code lost:
    
        r8 = r8 | 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00aa, code lost:
    
        android.util.Slog.w(com.android.server.policy.DeviceStateProviderImpl.TAG, "Parsed unknown flag with name: " + r9);
     */
    /* JADX WARN: Removed duplicated region for block: B:16:0x004e  */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static DeviceStateProviderImpl createFromConfig(Context context, ReadableConfig readableConfig) {
        DeviceStateConfig parseConfig;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (readableConfig != null && (parseConfig = parseConfig(readableConfig)) != null) {
            for (com.android.server.policy.devicestate.config.DeviceState deviceState : parseConfig.getDeviceState()) {
                int intValue = deviceState.getIdentifier().intValue();
                String name = deviceState.getName() == null ? "" : deviceState.getName();
                Flags flags = deviceState.getFlags();
                int i = 0;
                if (flags != null) {
                    List<String> flag = flags.getFlag();
                    int i2 = 0;
                    int i3 = 0;
                    while (i2 < flag.size()) {
                        String str = flag.get(i2);
                        str.hashCode();
                        char c = 65535;
                        switch (str.hashCode()) {
                            case -1145436729:
                                if (str.equals(FLAG_EMULATED_ONLY)) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case -1134441332:
                                if (str.equals(FLAG_APP_INACCESSIBLE)) {
                                    c = 1;
                                    break;
                                }
                                break;
                            case -1054037563:
                                if (str.equals(FLAG_CANCEL_OVERRIDE_REQUESTS)) {
                                    c = 2;
                                    break;
                                }
                                break;
                            case -900521097:
                                if (str.equals(FLAG_CANCEL_WHEN_REQUESTER_NOT_ON_TOP)) {
                                    c = 3;
                                    break;
                                }
                                break;
                            case 574499747:
                                if (str.equals(FLAG_UNSUPPORTED_WHEN_POWER_SAVE_MODE)) {
                                    c = 4;
                                    break;
                                }
                                break;
                            case 1671240668:
                                if (str.equals(FLAG_UNSUPPORTED_WHEN_THERMAL_STATUS_CRITICAL)) {
                                    c = 5;
                                    break;
                                }
                                break;
                        }
                        while (i2 < flag.size()) {
                        }
                    }
                    i = i3;
                }
                arrayList.add(new DeviceState(intValue, name, i));
                arrayList2.add(deviceState.getConditions());
            }
        }
        if (arrayList.size() == 0) {
            arrayList.add(DEFAULT_DEVICE_STATE);
            arrayList2.add(null);
        }
        return new DeviceStateProviderImpl(context, arrayList, arrayList2);
    }

    private DeviceStateProviderImpl(Context context, List<DeviceState> list, List<Conditions> list2) {
        this.mSystemCameraName = null;
        Preconditions.checkArgument(list.size() == list2.size(), "Number of device states must be equal to the number of device state conditions.");
        this.mContext = context;
        DeviceState[] deviceStateArr = (DeviceState[]) list.toArray(new DeviceState[list.size()]);
        Arrays.sort(deviceStateArr, Comparator.comparingInt(new ToIntFunction() { // from class: com.android.server.policy.DeviceStateProviderImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                return ((DeviceState) obj).getIdentifier();
            }
        }));
        this.mOrderedStates = deviceStateArr;
        setStateConditions(list, list2);
        sExtImpl.init(this, context);
        this.mSystemCameraName = SystemProperties.get("ro.oplus.system.camera.name", "");
        final PowerManager powerManager = (PowerManager) context.getSystemService(PowerManager.class);
        if (powerManager != null) {
            if (hasThermalSensitiveState(list)) {
                powerManager.addThermalStatusListener(this);
            }
            if (hasPowerSaveSensitiveState(list)) {
                context.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.policy.DeviceStateProviderImpl.1
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(Context context2, Intent intent) {
                        if ("android.os.action.POWER_SAVE_MODE_CHANGED_INTERNAL".equals(intent.getAction())) {
                            DeviceStateProviderImpl.this.onPowerSaveModeChanged(powerManager.isPowerSaveMode());
                        }
                    }
                }, new IntentFilter("android.os.action.POWER_SAVE_MODE_CHANGED_INTERNAL"));
            }
        }
    }

    public void registerSensor() {
        sExtImpl.registerSensor(this);
    }

    private void setStateConditions(List<DeviceState> list, List<Conditions> list2) {
        boolean z;
        ArraySet<Sensor> arraySet;
        int i;
        int i2;
        boolean z2;
        ArraySet<Sensor> arraySet2;
        ArraySet<Sensor> arraySet3;
        int i3;
        List<DeviceState> list3 = list;
        ArraySet<Sensor> arraySet4 = new ArraySet<>();
        int i4 = 0;
        boolean z3 = false;
        while (i4 < list2.size()) {
            int identifier = list3.get(i4).getIdentifier();
            boolean z4 = DEBUG;
            if (z4) {
                Slog.d(TAG, "Evaluating conditions for device state " + identifier + " (" + list3.get(i4).getName() + ")");
            }
            Conditions conditions = list2.get(i4);
            if (conditions == null) {
                if (list3.get(i4).hasFlag(4)) {
                    this.mStateConditions.put(identifier, FALSE_BOOLEAN_SUPPLIER);
                } else {
                    this.mStateConditions.put(identifier, TRUE_BOOLEAN_SUPPLIER);
                }
                arraySet2 = arraySet4;
                i = i4;
            } else {
                ArraySet<? extends Sensor> arraySet5 = new ArraySet<>();
                ArrayList arrayList = new ArrayList();
                LidSwitchCondition lidSwitch = conditions.getLidSwitch();
                if (lidSwitch != null) {
                    arrayList.add(new LidSwitchBooleanSupplier(lidSwitch.getOpen()));
                    if (z4) {
                        Slog.d(TAG, "Lid switch required");
                    }
                    z = true;
                } else {
                    z = false;
                }
                List<SensorCondition> sensor = conditions.getSensor();
                int i5 = 0;
                while (true) {
                    if (i5 >= sensor.size()) {
                        arraySet = arraySet4;
                        i = i4;
                        i2 = identifier;
                        z2 = true;
                        break;
                    }
                    SensorCondition sensorCondition = sensor.get(i5);
                    List<SensorCondition> list4 = sensor;
                    String type = sensorCondition.getType();
                    String name = sensorCondition.getName();
                    i = i4;
                    Sensor findSensor = findSensor(type, name);
                    if (findSensor == null) {
                        Slog.e(TAG, "Failed to find Sensor with type: " + type + " and name: " + name);
                        arraySet = arraySet4;
                        i2 = identifier;
                        z2 = false;
                        break;
                    }
                    if (DEBUG) {
                        i3 = identifier;
                        StringBuilder sb = new StringBuilder();
                        arraySet3 = arraySet4;
                        sb.append("Found sensor with type: ");
                        sb.append(type);
                        sb.append(" (");
                        sb.append(name);
                        sb.append(")");
                        Slog.d(TAG, sb.toString());
                    } else {
                        arraySet3 = arraySet4;
                        i3 = identifier;
                    }
                    arrayList.add(new SensorBooleanSupplier(findSensor, sensorCondition.getValue(), sensorCondition.getUnregister()));
                    arraySet5.add(findSensor);
                    i5++;
                    sensor = list4;
                    i4 = i;
                    identifier = i3;
                    arraySet4 = arraySet3;
                }
                KeyguardCondition keyguard = conditions.getKeyguard();
                if (keyguard != null) {
                    arrayList.add(new KeyguardBooleanSupplier(keyguard.getShow()));
                    if (DEBUG) {
                        Slog.d(TAG, "keguard required");
                    }
                }
                CameraCondition camera = conditions.getCamera();
                if (camera != null) {
                    arrayList.add(new CameraBooleanSupplier(camera.getOpen()));
                    if (DEBUG) {
                        Slog.d(TAG, "camera required");
                    }
                }
                DisplayCondition display = conditions.getDisplay();
                if (display != null) {
                    arrayList.add(new DisplayBooleanSupplier(display.getDisplayOn(), display.getDisplayId()));
                }
                if (z2) {
                    z3 |= z;
                    arraySet2 = arraySet;
                    arraySet2.addAll(arraySet5);
                    if (arrayList.size() > 1) {
                        this.mStateConditions.put(i2, new AndBooleanSupplier(arrayList));
                    } else {
                        int i6 = i2;
                        if (arrayList.size() > 0) {
                            this.mStateConditions.put(i6, (BooleanSupplier) arrayList.get(0));
                        } else {
                            this.mStateConditions.put(i6, TRUE_BOOLEAN_SUPPLIER);
                        }
                    }
                } else {
                    arraySet2 = arraySet;
                    this.mStateConditions.put(i2, FALSE_BOOLEAN_SUPPLIER);
                }
                i4 = i + 1;
                arraySet4 = arraySet2;
                list3 = list;
            }
            i4 = i + 1;
            arraySet4 = arraySet2;
            list3 = list;
        }
        ArraySet<Sensor> arraySet6 = arraySet4;
        if (z3) {
            ((InputManagerInternal) LocalServices.getService(InputManagerInternal.class)).registerLidSwitchCallback(this);
        }
        sExtImpl.setNeededSensors(arraySet6);
    }

    private Sensor findSensor(String str, String str2) {
        List<Sensor> sensorList = ((SensorManager) this.mContext.getSystemService(SensorManager.class)).getSensorList(-1);
        for (int i = 0; i < sensorList.size(); i++) {
            Sensor sensor = sensorList.get(i);
            String stringType = sensor.getStringType();
            String name = sensor.getName();
            if (stringType != null && name != null && stringType.equals(str) && name.equals(str2)) {
                if (this.mHingeSensor == null && stringType.equals(TYPE_SENSOR_HINGE_DETECT)) {
                    this.mHingeSensor = sensor;
                } else if (this.mGSensor == null && stringType.equals("android.sensor.gravity")) {
                    this.mGSensor = sensor;
                }
                return sensor;
            }
        }
        return null;
    }

    public void setListener(DeviceStateProvider.Listener listener) {
        synchronized (this.mLock) {
            if (this.mListener != null) {
                throw new RuntimeException("Provider already has a listener set.");
            }
            this.mListener = listener;
        }
        notifySupportedStatesChanged(1);
        notifyDeviceStateChangedIfNeeded();
    }

    public void notifyKeyguardShowOrSleep(boolean z) {
        synchronized (this.mLock) {
            this.mKeyguardShow = z;
        }
        Slog.d(TAG, "keyguard show or sleep: " + z + " mLastReportedState " + this.mLastReportedState);
        notifyDeviceStateChangedIfNeeded();
        SensorManager sensorManager = (SensorManager) this.mContext.getSystemService(SensorManager.class);
        if (z) {
            if (!sExtImpl.unregisterSensorsIfLockStateChanged(this.mLastReportedState, z)) {
                return;
            }
            Sensor sensor = this.mHingeSensor;
            if (sensor != null) {
                sensorManager.unregisterListener(this, sensor);
                synchronized (this.mLock) {
                    this.mLatestSensorEvent.put(this.mHingeSensor, null);
                }
            }
            Sensor sensor2 = this.mGSensor;
            if (sensor2 != null) {
                sensorManager.unregisterListener(this, sensor2);
                synchronized (this.mLock) {
                    this.mLatestSensorEvent.put(this.mGSensor, null);
                }
            }
        } else {
            Sensor sensor3 = this.mHingeSensor;
            if (sensor3 != null) {
                sensorManager.registerListener(this, sensor3, 0);
            }
            Sensor sensor4 = this.mGSensor;
            if (sensor4 != null) {
                sensorManager.registerListener(this, sensor4, 3);
            }
        }
        initCameraCallbackIfNeeded();
    }

    private void initCameraCallbackIfNeeded() {
        if (this.mCameraManager == null) {
            CameraManager cameraManager = (CameraManager) this.mContext.getSystemService("camera");
            this.mCameraManager = cameraManager;
            if (cameraManager == null || this.mCameraAvailabilityCallback != null) {
                return;
            }
            CameraAvailabilityCallback cameraAvailabilityCallback = new CameraAvailabilityCallback();
            this.mCameraAvailabilityCallback = cameraAvailabilityCallback;
            this.mCameraManager.registerAvailabilityCallback(cameraAvailabilityCallback, new Handler(DisplayThread.get().getLooper()));
        }
    }

    private void notifySupportedStatesChanged(int i) {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mLock) {
            DeviceStateProvider.Listener listener = this.mListener;
            if (listener == null) {
                return;
            }
            for (DeviceState deviceState : this.mOrderedStates) {
                if ((!isThermalStatusCriticalOrAbove(this.mThermalStatus) || !deviceState.hasFlag(16)) && (!this.mPowerSaveModeEnabled || !deviceState.hasFlag(32))) {
                    arrayList.add(deviceState);
                }
            }
            listener.onSupportedDeviceStatesChanged((DeviceState[]) arrayList.toArray(new DeviceState[arrayList.size()]), i);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x007f, code lost:
    
        r3 = -1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void notifyDeviceStateChangedIfNeeded() {
        int i;
        int i2;
        synchronized (this.mLock) {
            if (this.mListener == null) {
                return;
            }
            int i3 = 0;
            while (true) {
                DeviceState[] deviceStateArr = this.mOrderedStates;
                if (i3 >= deviceStateArr.length) {
                    break;
                }
                i = deviceStateArr[i3].getIdentifier();
                boolean z = DEBUG;
                if (z) {
                    Slog.d(TAG, "Checking conditions for " + this.mOrderedStates[i3].getName() + "(" + i3 + ")");
                }
                try {
                } catch (IllegalStateException e) {
                    if (DEBUG) {
                        Slog.d(TAG, "Unable to check current state", e);
                    }
                }
                if (this.mStateConditions.get(i).getAsBoolean()) {
                    if (z) {
                        Slog.d(TAG, "Device State conditions satisfied, transition to " + i);
                    }
                    if (i > 3) {
                    }
                } else {
                    i3++;
                }
            }
            if (i == -1) {
                Slog.e(TAG, "No declared device states match any of the required conditions.");
                dumpSensorValues();
            }
            if (i == -1 || i == (i2 = this.mLastReportedState)) {
                i = -1;
            } else if (sExtImpl.isNeedInterceptDeviceState(i2, i)) {
                return;
            } else {
                this.mLastReportedState = i;
            }
            if (i != -1) {
                this.mListener.onStateChanged(i);
                synchronized (this.mLock) {
                    if (DEBUG) {
                        StringBuilder sb = new StringBuilder();
                        for (SensorEvent sensorEvent : this.mLatestSensorEvent.values()) {
                            if (sensorEvent != null) {
                                Sensor sensor = sensorEvent.sensor;
                                if (sensor == this.mHingeSensor) {
                                    sb.append(" Hinge:");
                                    sb.append(sensorEvent.values[0]);
                                } else if (sensor == this.mGSensor) {
                                    sb.append(" Gravity:");
                                    sb.append(sensorEvent.values[0]);
                                    sb.append(" ");
                                    sb.append(sensorEvent.values[1]);
                                    sb.append(" ");
                                    sb.append(sensorEvent.values[2]);
                                } else {
                                    sb.append(" Hall:");
                                    sb.append(sensorEvent.values[0]);
                                }
                            }
                        }
                        sb.append(" KeyguardShow:");
                        sb.append(this.mKeyguardShow);
                        sb.append(" Camera:");
                        sb.append(this.mCameraOpen);
                        Slog.d(TAG, "StateChanged to " + i + " " + sb.toString());
                    }
                }
            }
        }
    }

    @Override // com.android.server.input.InputManagerInternal.LidSwitchCallback
    public void notifyLidSwitchChanged(long j, boolean z) {
        synchronized (this.mLock) {
            this.mIsLidOpen = Boolean.valueOf(z);
        }
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("Lid switch state: ");
            sb.append(z ? "open" : "closed");
            Slog.d(TAG, sb.toString());
        }
        notifyDeviceStateChangedIfNeeded();
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        SensorEvent sensorEvent2 = new SensorEvent(sensorEvent.sensor, sensorEvent.accuracy, sensorEvent.timestamp, (float[]) sensorEvent.values.clone());
        synchronized (this.mLock) {
            if (shouldProcessSensorChange(sensorEvent2)) {
                this.mLatestSensorEvent.put(sensorEvent2.sensor, sensorEvent2);
                Sensor sensor = sensorEvent.sensor;
                if (sensor == this.mHingeSensor) {
                    Slog.i(TAG, "onSensorChanged Hinge value:" + sensorEvent.values[0] + "  hingeAccuracy: " + sensorEvent.values[1]);
                } else if (sensor == this.mGSensor) {
                    if (DEBUG) {
                        Slog.i(TAG, "onSensorChanged Gravity values:" + sensorEvent.values[0] + " " + sensorEvent.values[1] + " " + sensorEvent.values[2]);
                    }
                } else {
                    Slog.i(TAG, "onSensorChanged Hall value:" + sensorEvent.values[0]);
                }
                notifyDeviceStateChangedIfNeeded();
            }
        }
    }

    boolean shouldProcessSensorChange(SensorEvent sensorEvent) {
        boolean z;
        boolean z2;
        boolean z3;
        SensorEvent sensorEvent2 = this.mLatestSensorEvent.get(sensorEvent.sensor);
        if (sensorEvent2 == null) {
            return true;
        }
        Sensor sensor = sensorEvent.sensor;
        if (sensor == this.mHingeSensor) {
            return Math.abs(sensorEvent.values[0] - sensorEvent2.values[0]) >= 5.0f;
        }
        if (sensor != this.mGSensor) {
            return true;
        }
        float[] fArr = sensorEvent.values;
        float f = fArr[0];
        float f2 = fArr[1];
        float f3 = fArr[2];
        float[] fArr2 = sensorEvent2.values;
        float f4 = fArr2[0];
        float f5 = fArr2[1];
        float f6 = fArr2[2];
        if (Math.abs(f4 - f) < 0.3f) {
            sensorEvent.values[0] = f4;
            z = false;
        } else {
            z = true;
        }
        if (Math.abs(f5 - f2) < 0.3f) {
            sensorEvent.values[1] = f5;
            z2 = false;
        } else {
            z2 = true;
        }
        if (Math.abs(f6 - f3) < 0.3f) {
            sensorEvent.values[2] = f6;
            z3 = false;
        } else {
            z3 = true;
        }
        return z || z2 || z3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class LidSwitchBooleanSupplier implements BooleanSupplier {
        private final boolean mExpectedOpen;

        LidSwitchBooleanSupplier(boolean z) {
            this.mExpectedOpen = z;
        }

        @Override // java.util.function.BooleanSupplier
        public boolean getAsBoolean() {
            boolean z;
            synchronized (DeviceStateProviderImpl.this.mLock) {
                if (DeviceStateProviderImpl.this.mIsLidOpen == null) {
                    throw new IllegalStateException("Have not received lid switch value.");
                }
                z = DeviceStateProviderImpl.this.mIsLidOpen.booleanValue() == this.mExpectedOpen;
            }
            return z;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class KeyguardBooleanSupplier implements BooleanSupplier {
        private final boolean mShow;

        KeyguardBooleanSupplier(boolean z) {
            this.mShow = z;
        }

        @Override // java.util.function.BooleanSupplier
        public boolean getAsBoolean() {
            boolean z;
            synchronized (DeviceStateProviderImpl.this.mLock) {
                Slog.d(DeviceStateProviderImpl.TAG, "value: " + DeviceStateProviderImpl.this.mKeyguardShow + ", constraint KeyguardShow: " + this.mShow);
                z = this.mShow == DeviceStateProviderImpl.this.mKeyguardShow;
            }
            return z;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class CameraBooleanSupplier implements BooleanSupplier {
        private final boolean mOpen;

        CameraBooleanSupplier(boolean z) {
            this.mOpen = z;
        }

        @Override // java.util.function.BooleanSupplier
        public boolean getAsBoolean() {
            synchronized (DeviceStateProviderImpl.this.mLock) {
                boolean z = true;
                if (DeviceStateProviderImpl.this.mLastReportedState == 1) {
                    return true;
                }
                if (this.mOpen != DeviceStateProviderImpl.this.mCameraOpen) {
                    z = false;
                }
                return z;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class CameraAvailabilityCallback extends CameraManager.AvailabilityCallback {
        private String mCameraOwner;

        private CameraAvailabilityCallback() {
            this.mCameraOwner = null;
        }

        public void onCameraOpened(String str, String str2) {
            super.onCameraOpened(str, str2);
            this.mCameraOwner = str2;
            if (str2 != null && str2.equals(DeviceStateProviderImpl.this.mSystemCameraName)) {
                synchronized (DeviceStateProviderImpl.this.mLock) {
                    DeviceStateProviderImpl.this.mCameraOpen = true;
                }
            }
            DeviceStateProviderImpl.this.notifyDeviceStateChangedIfNeeded();
        }

        public void onCameraClosed(String str) {
            super.onCameraClosed(str);
            synchronized (DeviceStateProviderImpl.this.mLock) {
                DeviceStateProviderImpl.this.mCameraOpen = false;
            }
            this.mCameraOwner = null;
            DeviceStateProviderImpl.this.notifyDeviceStateChangedIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class DisplayBooleanSupplier implements BooleanSupplier {
        private final int mDisplayId;
        private final boolean mDisplayOn;

        DisplayBooleanSupplier(boolean z, BigInteger bigInteger) {
            this.mDisplayOn = z;
            this.mDisplayId = bigInteger.intValue();
        }

        @Override // java.util.function.BooleanSupplier
        public boolean getAsBoolean() {
            boolean z;
            synchronized (DeviceStateProviderImpl.this.mLock) {
                Slog.d(DeviceStateProviderImpl.TAG, "value: " + DeviceStateProviderImpl.sExtImpl.getDisplayOn(this.mDisplayId) + ", constraint DisplayOn: " + this.mDisplayOn);
                z = this.mDisplayOn == DeviceStateProviderImpl.sExtImpl.getDisplayOn(this.mDisplayId);
            }
            return z;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class SensorBooleanSupplier implements BooleanSupplier {
        private final List<NumericRange> mExpectedValues;
        private final Sensor mSensor;
        private boolean mUnregister;

        SensorBooleanSupplier(Sensor sensor, List<NumericRange> list, boolean z) {
            this.mSensor = sensor;
            this.mExpectedValues = list;
            this.mUnregister = z;
        }

        @Override // java.util.function.BooleanSupplier
        public boolean getAsBoolean() {
            synchronized (DeviceStateProviderImpl.this.mLock) {
                SensorEvent sensorEvent = (SensorEvent) DeviceStateProviderImpl.this.mLatestSensorEvent.get(this.mSensor);
                if (sensorEvent == null) {
                    Slog.d(DeviceStateProviderImpl.TAG, "no event for sensor: " + this.mSensor + "=mUnregister=" + this.mUnregister);
                    return this.mUnregister;
                }
                if (sensorEvent.values.length < this.mExpectedValues.size()) {
                    throw new RuntimeException("Number of supplied numeric range(s) does not match the number of values in the latest sensor event for sensor: " + this.mSensor);
                }
                for (int i = 0; i < this.mExpectedValues.size(); i++) {
                    if (!adheresToRange(sensorEvent.values[i], this.mExpectedValues.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }

        private boolean adheresToRange(float f, NumericRange numericRange) {
            BigDecimal min_optional = numericRange.getMin_optional();
            if (min_optional != null) {
                if (DeviceStateProviderImpl.DEBUG) {
                    Slog.d(DeviceStateProviderImpl.TAG, "value: " + f + ", constraint min: " + min_optional.floatValue());
                }
                if (f <= min_optional.floatValue()) {
                    return false;
                }
            }
            BigDecimal minInclusive_optional = numericRange.getMinInclusive_optional();
            if (minInclusive_optional != null) {
                if (DeviceStateProviderImpl.DEBUG) {
                    Slog.d(DeviceStateProviderImpl.TAG, "value: " + f + ", constraint min-inclusive: " + minInclusive_optional.floatValue());
                }
                if (f < minInclusive_optional.floatValue()) {
                    return false;
                }
            }
            BigDecimal max_optional = numericRange.getMax_optional();
            if (max_optional != null) {
                if (DeviceStateProviderImpl.DEBUG) {
                    Slog.d(DeviceStateProviderImpl.TAG, "value: " + f + ", constraint max: " + max_optional.floatValue());
                }
                if (f >= max_optional.floatValue()) {
                    return false;
                }
            }
            BigDecimal maxInclusive_optional = numericRange.getMaxInclusive_optional();
            if (maxInclusive_optional == null) {
                return true;
            }
            if (DeviceStateProviderImpl.DEBUG) {
                Slog.d(DeviceStateProviderImpl.TAG, "value: " + f + ", constraint max-inclusive: " + maxInclusive_optional.floatValue());
            }
            return f <= maxInclusive_optional.floatValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class AndBooleanSupplier implements BooleanSupplier {
        List<BooleanSupplier> mBooleanSuppliers;

        AndBooleanSupplier(List<BooleanSupplier> list) {
            this.mBooleanSuppliers = list;
        }

        @Override // java.util.function.BooleanSupplier
        public boolean getAsBoolean() {
            for (int i = 0; i < this.mBooleanSuppliers.size(); i++) {
                if (!this.mBooleanSuppliers.get(i).getAsBoolean()) {
                    return false;
                }
            }
            return true;
        }
    }

    private static File getConfigurationFile() {
        File buildPath = Environment.buildPath(Environment.getOdmDirectory(), new String[]{"etc/devicestate/", ODM_CONFIG_FILE_NAME});
        if (!buildPath.exists()) {
            buildPath = Environment.buildPath(Environment.getOdmDirectory(), new String[]{"etc/devicestate/", CONFIG_FILE_NAME});
        }
        if (buildPath.exists()) {
            Slog.d(TAG, "configFileFromOdmDir :" + buildPath.toString() + " is exists");
            return buildPath;
        }
        File buildPath2 = Environment.buildPath(Environment.getDataDirectory(), new String[]{DATA_CONFIG_FILE_PATH, CONFIG_FILE_NAME});
        if (buildPath2.exists()) {
            return buildPath2;
        }
        File buildPath3 = Environment.buildPath(Environment.getVendorDirectory(), new String[]{"etc/devicestate/", CONFIG_FILE_NAME});
        if (buildPath3.exists()) {
            return buildPath3;
        }
        return null;
    }

    @GuardedBy({"mLock"})
    private void dumpSensorValues() {
        Slog.i(TAG, "Sensor values:");
        for (Sensor sensor : this.mLatestSensorEvent.keySet()) {
            SensorEvent sensorEvent = this.mLatestSensorEvent.get(sensor);
            if (sensorEvent != null) {
                Slog.i(TAG, sensor.getName() + ": " + Arrays.toString(sensorEvent.values));
            } else {
                Slog.i(TAG, sensor.getName() + ": null");
            }
        }
    }

    private static DeviceStateConfig parseConfig(ReadableConfig readableConfig) {
        try {
            InputStream openRead = readableConfig.openRead();
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(openRead);
                try {
                    DeviceStateConfig read = XmlParser.read(bufferedInputStream);
                    bufferedInputStream.close();
                    if (openRead != null) {
                        openRead.close();
                    }
                    return read;
                } finally {
                }
            } catch (Throwable th) {
                if (openRead != null) {
                    try {
                        openRead.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException | DatatypeConfigurationException | XmlPullParserException e) {
            Slog.e(TAG, "Encountered an error while reading device state config", e);
            return null;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class ReadableFileConfig implements ReadableConfig {
        private final File mFile;

        private ReadableFileConfig(File file) {
            this.mFile = file;
        }

        @Override // com.android.server.policy.DeviceStateProviderImpl.ReadableConfig
        public InputStream openRead() throws IOException {
            return new FileInputStream(this.mFile);
        }
    }

    public IDeviceStateProviderImplWrapper getWrapper() {
        return this.mDsplWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class DeviceStateProviderImplWrapper implements IDeviceStateProviderImplWrapper {
        private DeviceStateProviderImplWrapper() {
        }

        @Override // com.android.server.policy.IDeviceStateProviderImplWrapper
        public void notifyDeviceStateChangedIfNeeded() {
            DeviceStateProviderImpl.this.notifyDeviceStateChangedIfNeeded();
        }

        @Override // com.android.server.policy.IDeviceStateProviderImplWrapper
        public Object getLock() {
            return DeviceStateProviderImpl.this.mLock;
        }
    }

    @VisibleForTesting
    void onPowerSaveModeChanged(boolean z) {
        synchronized (this.mLock) {
            if (this.mPowerSaveModeEnabled != z) {
                this.mPowerSaveModeEnabled = z;
                notifySupportedStatesChanged(z ? 4 : 5);
            }
        }
    }

    @Override // android.os.PowerManager.OnThermalStatusChangedListener
    public void onThermalStatusChanged(int i) {
        int i2;
        synchronized (this.mLock) {
            i2 = this.mThermalStatus;
            this.mThermalStatus = i;
        }
        boolean isThermalStatusCriticalOrAbove = isThermalStatusCriticalOrAbove(i);
        if (isThermalStatusCriticalOrAbove != isThermalStatusCriticalOrAbove(i2)) {
            Slog.i(TAG, "Updating supported device states due to thermal status change. isThermalStatusCriticalOrAbove: " + isThermalStatusCriticalOrAbove);
            notifySupportedStatesChanged(isThermalStatusCriticalOrAbove ? 3 : 2);
        }
    }

    private static boolean hasThermalSensitiveState(List<DeviceState> list) {
        Iterator<DeviceState> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().hasFlag(16)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasPowerSaveSensitiveState(List<DeviceState> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).hasFlag(32)) {
                return true;
            }
        }
        return false;
    }
}
