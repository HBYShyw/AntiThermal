package com.android.server.audio;

import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.audio.common.V2_0.AudioChannelMask;
import android.media.AudioAttributes;
import android.media.AudioDeviceAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioFormat;
import android.media.AudioSystem;
import android.media.INativeSpatializerCallback;
import android.media.ISpatializer;
import android.media.ISpatializerCallback;
import android.media.ISpatializerHeadToSoundStagePoseCallback;
import android.media.ISpatializerHeadTrackerAvailableCallback;
import android.media.ISpatializerHeadTrackingCallback;
import android.media.ISpatializerHeadTrackingModeCallback;
import android.media.ISpatializerOutputCallback;
import android.media.MediaMetrics;
import android.media.Spatializer;
import android.os.Binder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseIntArray;
import com.android.internal.annotations.GuardedBy;
import com.android.server.am.IOplusSceneManager;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SpatializerHelper {
    private static final boolean DEBUG = true;
    private static final boolean DEBUG_MORE = false;
    private static final String METRICS_DEVICE_PREFIX = "audio.spatializer.device.";
    private static final String OPLUS_SPATIALIZER_USERSPACE_STATE = "OPLUS_SPATIALIZER_USERSPACE_STATE";
    static final int STATE_DISABLED_AVAILABLE = 6;
    static final int STATE_DISABLED_UNAVAILABLE = 3;
    static final int STATE_ENABLED_AVAILABLE = 5;
    static final int STATE_ENABLED_UNAVAILABLE = 4;
    static final int STATE_NOT_SUPPORTED = 1;
    static final int STATE_UNINITIALIZED = 0;
    private static final String TAG = "AS.SpatializerHelper";
    private static final int USER_STATE_DEFAULT = -1;
    private static final int USER_STATE_DISENABLED = 0;
    private static final int USER_STATE_ENABLED = 1;
    private final AudioSystemAdapter mASA;
    private final AudioService mAudioService;
    private HelperDynamicSensorCallback mDynSensorCallback;
    private SensorManager mSensorManager;
    private ISpatializer mSpat;
    private SpatializerCallback mSpatCallback;
    private static final SparseIntArray SPAT_MODE_FOR_DEVICE_TYPE = new SparseIntArray(14) { // from class: com.android.server.audio.SpatializerHelper.1
        {
            append(2, 1);
            append(3, 0);
            append(4, 0);
            append(8, 0);
            append(13, 1);
            append(12, 1);
            append(11, 1);
            append(22, 0);
            append(5, 1);
            append(6, 1);
            append(19, 1);
            append(26, 0);
            append(27, 1);
            append(30, 0);
        }
    };
    private static final int[] WIRELESS_TYPES = {7, 8, 26, 27, 30};
    private static final AudioAttributes DEFAULT_ATTRIBUTES = new AudioAttributes.Builder().setUsage(1).build();
    private static final AudioFormat DEFAULT_FORMAT = new AudioFormat.Builder().setEncoding(2).setSampleRate(AudioService.HD_SAMPLE_RATE).setChannelMask(AudioChannelMask.IN_6).build();
    private static ArrayList<AudioDeviceAttributes> sRoutingDevices = new ArrayList<>(0);
    private int mState = 0;
    private boolean mFeatureEnabled = false;
    private int mUserStateOfSpeaker = 0;
    private AudioDeviceAttributes newWirelessDevice = null;
    private boolean mSpatialzerForSpeakerSupport = false;
    private int mSpatLevel = 0;
    private int mCapableSpatLevel = 0;
    private boolean mTransauralSupported = false;
    private boolean mBinauralSupported = false;
    private boolean mIsHeadTrackingSupported = false;
    private int[] mSupportedHeadTrackingModes = new int[0];
    private int mActualHeadTrackingMode = -2;
    private int mDesiredHeadTrackingMode = 1;
    private boolean mHeadTrackerAvailable = false;
    private int mDesiredHeadTrackingModeWhenEnabled = 1;
    private int mSpatOutput = 0;
    private SpatializerHeadTrackingCallback mSpatHeadTrackingCallback = new SpatializerHeadTrackingCallback();
    private final ArrayList<Integer> mSACapableDeviceTypes = new ArrayList<>(0);

    @GuardedBy({"this"})
    private final ArrayList<SADeviceState> mSADevices = new ArrayList<>(0);
    final RemoteCallbackList<ISpatializerCallback> mStateCallbacks = new RemoteCallbackList<>();
    final RemoteCallbackList<ISpatializerHeadTrackingModeCallback> mHeadTrackingModeCallbacks = new RemoteCallbackList<>();
    final RemoteCallbackList<ISpatializerHeadTrackerAvailableCallback> mHeadTrackerCallbacks = new RemoteCallbackList<>();
    final RemoteCallbackList<ISpatializerHeadToSoundStagePoseCallback> mHeadPoseCallbacks = new RemoteCallbackList<>();
    final RemoteCallbackList<ISpatializerOutputCallback> mOutputCallbacks = new RemoteCallbackList<>();

    private static String spatStateString(int i) {
        return i != 0 ? i != 1 ? i != 3 ? i != 4 ? i != 5 ? i != 6 ? "invalid state" : "STATE_DISABLED_AVAILABLE" : "STATE_ENABLED_AVAILABLE" : "STATE_ENABLED_UNAVAILABLE" : "STATE_DISABLED_UNAVAILABLE" : "STATE_NOT_SUPPORTED" : "STATE_UNINITIALIZED";
    }

    private static void logd(String str) {
        Log.i(TAG, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SpatializerHelper(AudioService audioService, AudioSystemAdapter audioSystemAdapter, boolean z, boolean z2, boolean z3) {
        this.mAudioService = audioService;
        this.mASA = audioSystemAdapter;
        SADeviceState.sBinauralEnabledDefault = z;
        SADeviceState.sTransauralEnabledDefault = z2;
        SADeviceState.sHeadTrackingEnabledDefault = z3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void init(boolean z, String str) {
        byte[] supportedLevels;
        loglogi("init effectExpected=" + z);
        if (!z) {
            loglogi("init(): setting state to STATE_NOT_SUPPORTED due to effect not expected");
            this.mState = 1;
            return;
        }
        if (this.mState != 0) {
            throw new IllegalStateException(logloge("init() called in state " + this.mState));
        }
        SpatializerCallback spatializerCallback = new SpatializerCallback();
        this.mSpatCallback = spatializerCallback;
        ISpatializer spatializer = AudioSystem.getSpatializer(spatializerCallback);
        if (spatializer == null) {
            loglogi("init(): No Spatializer found");
            this.mState = 1;
            return;
        }
        resetCapabilities();
        try {
            try {
                supportedLevels = spatializer.getSupportedLevels();
            } catch (RemoteException unused) {
                resetCapabilities();
            }
            if (supportedLevels != null && supportedLevels.length != 0 && (supportedLevels.length != 1 || supportedLevels[0] != 0)) {
                int length = supportedLevels.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    byte b = supportedLevels[i];
                    loglogi("init(): found support for level: " + ((int) b));
                    if (b == 1) {
                        loglogi("init(): setting capable level to LEVEL_MULTICHANNEL");
                        this.mCapableSpatLevel = b;
                        break;
                    }
                    i++;
                }
                boolean isHeadTrackingSupported = spatializer.isHeadTrackingSupported();
                this.mIsHeadTrackingSupported = isHeadTrackingSupported;
                if (isHeadTrackingSupported) {
                    byte[] supportedHeadTrackingModes = spatializer.getSupportedHeadTrackingModes();
                    ArrayList arrayList = new ArrayList(0);
                    for (byte b2 : supportedHeadTrackingModes) {
                        if (b2 != 0 && b2 != 1) {
                            if (b2 == 2 || b2 == 3) {
                                arrayList.add(Integer.valueOf(headTrackingModeTypeToSpatializerInt(b2)));
                            } else {
                                Log.e(TAG, "Unexpected head tracking mode:" + ((int) b2), new IllegalArgumentException("invalid mode"));
                            }
                        }
                    }
                    this.mSupportedHeadTrackingModes = new int[arrayList.size()];
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        this.mSupportedHeadTrackingModes[i2] = ((Integer) arrayList.get(i2)).intValue();
                    }
                    this.mActualHeadTrackingMode = headTrackingModeTypeToSpatializerInt(spatializer.getActualHeadTrackingMode());
                } else {
                    this.mDesiredHeadTrackingModeWhenEnabled = -2;
                    this.mDesiredHeadTrackingMode = -2;
                }
                for (byte b3 : spatializer.getSupportedModes()) {
                    if (b3 == 0) {
                        this.mBinauralSupported = true;
                    } else if (b3 != 1) {
                        logloge("init(): Spatializer reports unknown supported mode:" + ((int) b3));
                    } else {
                        this.mTransauralSupported = true;
                    }
                }
                if (!this.mBinauralSupported && !this.mTransauralSupported) {
                    this.mState = 1;
                    try {
                        spatializer.release();
                    } catch (RemoteException unused2) {
                    }
                    return;
                }
                int i3 = 0;
                while (true) {
                    SparseIntArray sparseIntArray = SPAT_MODE_FOR_DEVICE_TYPE;
                    if (i3 >= sparseIntArray.size()) {
                        break;
                    }
                    int valueAt = sparseIntArray.valueAt(i3);
                    if ((valueAt == 0 && this.mBinauralSupported) || (valueAt == 1 && this.mTransauralSupported)) {
                        this.mSACapableDeviceTypes.add(Integer.valueOf(sparseIntArray.keyAt(i3)));
                    }
                    i3++;
                }
                if (str != null) {
                    setSADeviceSettings(str);
                }
                addCompatibleAudioDevice(new AudioDeviceAttributes(2, ""), false);
                addCompatibleAudioDevice(new AudioDeviceAttributes(8, ""), false);
                try {
                    spatializer.release();
                } catch (RemoteException unused3) {
                    if (this.mCapableSpatLevel == 0) {
                        this.mState = 1;
                        return;
                    }
                    this.mState = 3;
                    sRoutingDevices = getRoutingDevices(DEFAULT_ATTRIBUTES);
                    this.mSpatialzerForSpeakerSupport = this.mAudioService.getWrapper().getExtImpl().getSpatializerSpeakerSupported();
                    return;
                }
            }
            logloge("init(): found Spatializer is useless");
            this.mState = 1;
            try {
                spatializer.release();
            } catch (RemoteException unused4) {
            }
        } catch (Throwable th) {
            try {
                spatializer.release();
            } catch (RemoteException unused5) {
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void reset(boolean z) {
        loglogi("Resetting featureEnabled=" + z);
        releaseSpat();
        this.mState = 0;
        this.mSpatLevel = 0;
        this.mActualHeadTrackingMode = -2;
        init(true, null);
        setSpatializerEnabledInt(z, false);
    }

    private void resetCapabilities() {
        this.mCapableSpatLevel = 0;
        this.mBinauralSupported = false;
        this.mTransauralSupported = false;
        this.mIsHeadTrackingSupported = false;
        this.mSupportedHeadTrackingModes = new int[0];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onRoutingUpdated() {
        boolean z;
        int i = this.mState;
        if (i == 0 || i == 1) {
            return;
        }
        AudioAttributes audioAttributes = DEFAULT_ATTRIBUTES;
        ArrayList<AudioDeviceAttributes> routingDevices = getRoutingDevices(audioAttributes);
        sRoutingDevices = routingDevices;
        if (routingDevices.isEmpty()) {
            logloge("onRoutingUpdated: no device, no Spatial Audio");
            setDispatchAvailableState(false);
            return;
        }
        AudioDeviceAttributes audioDeviceAttributes = sRoutingDevices.get(0);
        if (isWireless(audioDeviceAttributes.getType())) {
            addWirelessDeviceIfNew(audioDeviceAttributes);
        }
        Pair<Boolean, Boolean> evaluateState = evaluateState(audioDeviceAttributes);
        if (((Boolean) evaluateState.second).booleanValue()) {
            z = canBeSpatializedOnDevice(audioAttributes, DEFAULT_FORMAT, sRoutingDevices);
            loglogi("onRoutingUpdated: can spatialize media 5.1:" + z + " on device:" + audioDeviceAttributes);
            setDispatchAvailableState(z);
        } else {
            loglogi("onRoutingUpdated: device:" + audioDeviceAttributes + " not available for Spatial Audio");
            setDispatchAvailableState(false);
            z = false;
        }
        SADeviceState findDeviceStateForAudioDeviceAttributes = findDeviceStateForAudioDeviceAttributes(audioDeviceAttributes);
        boolean z2 = z && ((Boolean) evaluateState.first).booleanValue() && findDeviceStateForAudioDeviceAttributes != null && findDeviceStateForAudioDeviceAttributes.mUserEnabled;
        if (z2) {
            loglogi("Enabling Spatial Audio since enabled for media device:" + audioDeviceAttributes);
        } else {
            loglogi("Disabling Spatial Audio since disabled for media device:" + audioDeviceAttributes);
        }
        if (this.mSpat == null && this.mDesiredHeadTrackingMode != -2) {
            loglogi("onRoutingUpdated() recreate Spat, mode " + this.mDesiredHeadTrackingMode);
            createSpat();
        }
        if (this.mSpat != null) {
            byte b = z2 ? (byte) 1 : (byte) 0;
            loglogi("Setting spatialization level to: " + ((int) b));
            if (audioDeviceAttributes.getType() != 7 && audioDeviceAttributes.getType() != 1) {
                AudioSystem.setParameters("OPLUS_SPATIALIZER_USERSPACE_STATE=" + (z2 ? 1 : 0));
            }
            try {
                this.mSpat.setLevel(b);
            } catch (RemoteException e) {
                Log.e(TAG, "onRoutingUpdated() Can't set spatializer level", e);
                postReset();
                return;
            }
        }
        setDispatchFeatureEnabledState(z2, "onRoutingUpdated");
        if (findDeviceStateForAudioDeviceAttributes != null && findDeviceStateForAudioDeviceAttributes.mHeadTrackerEnabled && this.mDesiredHeadTrackingMode != 1) {
            this.mDesiredHeadTrackingMode = 1;
            loglogi("Device headtrack is enabled, change mode to :" + this.mDesiredHeadTrackingMode);
        }
        if (this.mDesiredHeadTrackingMode == 1) {
            this.mFeatureEnabled = true;
            loglogi("mode:" + this.mDesiredHeadTrackingMode + ", need set feature enabled.");
        }
        int i2 = this.mDesiredHeadTrackingMode;
        if (i2 != -2 && i2 != -1) {
            postInitSensors();
        }
    }

    private void postReset() {
        this.mAudioService.postResetSpatializer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class SpatializerCallback extends INativeSpatializerCallback.Stub {
        private SpatializerCallback() {
        }

        public void onLevelChanged(byte b) {
            SpatializerHelper.loglogi("SpatializerCallback.onLevelChanged level:" + ((int) b));
            synchronized (SpatializerHelper.this) {
                SpatializerHelper.this.mSpatLevel = SpatializerHelper.spatializationLevelToSpatializerInt(b);
            }
            SpatializerHelper.this.postInitSensors();
        }

        public void onOutputChanged(int i) {
            int i2;
            SpatializerHelper.loglogi("SpatializerCallback.onOutputChanged output:" + i);
            synchronized (SpatializerHelper.this) {
                i2 = SpatializerHelper.this.mSpatOutput;
                SpatializerHelper.this.mSpatOutput = i;
            }
            if (i2 != i) {
                SpatializerHelper.this.dispatchOutputUpdate(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class SpatializerHeadTrackingCallback extends ISpatializerHeadTrackingCallback.Stub {
        private SpatializerHeadTrackingCallback() {
        }

        public void onHeadTrackingModeChanged(byte b) {
            int i;
            int i2;
            synchronized (this) {
                i = SpatializerHelper.this.mActualHeadTrackingMode;
                SpatializerHelper.this.mActualHeadTrackingMode = SpatializerHelper.headTrackingModeTypeToSpatializerInt(b);
                i2 = SpatializerHelper.this.mActualHeadTrackingMode;
            }
            SpatializerHelper.loglogi("SpatializerHeadTrackingCallback.onHeadTrackingModeChanged mode:" + Spatializer.headtrackingModeToString(i2));
            if (i != i2) {
                SpatializerHelper.this.dispatchActualHeadTrackingMode(i2);
            }
        }

        public void onHeadToSoundStagePoseUpdated(float[] fArr) {
            if (fArr == null) {
                Log.e(SpatializerHelper.TAG, "SpatializerHeadTrackingCallback.onHeadToStagePoseUpdatednull transform");
                return;
            }
            if (fArr.length != 6) {
                Log.e(SpatializerHelper.TAG, "SpatializerHeadTrackingCallback.onHeadToStagePoseUpdated invalid transform length" + fArr.length);
                return;
            }
            SpatializerHelper.this.dispatchPoseUpdate(fArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class HelperDynamicSensorCallback extends SensorManager.DynamicSensorCallback {
        private HelperDynamicSensorCallback() {
        }

        @Override // android.hardware.SensorManager.DynamicSensorCallback
        public void onDynamicSensorConnected(Sensor sensor) {
            synchronized (SpatializerHelper.this) {
                if (!(SpatializerHelper.this.mFeatureEnabled && SpatializerHelper.this.mSpatLevel != 0) && SpatializerHelper.this.newWirelessDevice == null && !SpatializerHelper.sRoutingDevices.isEmpty()) {
                    SpatializerHelper.this.newWirelessDevice = (AudioDeviceAttributes) SpatializerHelper.sRoutingDevices.get(0);
                    Log.d(SpatializerHelper.TAG, "onDynamicSensorConnected, spatializer is not enable, but we check the ht support of device first!");
                }
            }
            SpatializerHelper.this.postInitSensors();
        }

        @Override // android.hardware.SensorManager.DynamicSensorCallback
        public void onDynamicSensorDisconnected(Sensor sensor) {
            SpatializerHelper.this.postInitSensors();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized List<AudioDeviceAttributes> getCompatibleAudioDevices() {
        ArrayList arrayList;
        arrayList = new ArrayList();
        Iterator<SADeviceState> it = this.mSADevices.iterator();
        while (it.hasNext()) {
            SADeviceState next = it.next();
            if (next.mEnabled) {
                arrayList.add(next.getAudioDeviceAttributes());
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void addCompatibleAudioDevice(AudioDeviceAttributes audioDeviceAttributes) {
        addCompatibleAudioDevice(audioDeviceAttributes, true);
    }

    @GuardedBy({"this"})
    private void addCompatibleAudioDevice(AudioDeviceAttributes audioDeviceAttributes, boolean z) {
        if (isDeviceCompatibleWithSpatializationModes(audioDeviceAttributes)) {
            loglogi("addCompatibleAudioDevice: dev=" + audioDeviceAttributes);
            SADeviceState findDeviceStateForAudioDeviceAttributes = findDeviceStateForAudioDeviceAttributes(audioDeviceAttributes);
            if (findDeviceStateForAudioDeviceAttributes != null) {
                if (!z || findDeviceStateForAudioDeviceAttributes.mEnabled) {
                    findDeviceStateForAudioDeviceAttributes = null;
                } else {
                    findDeviceStateForAudioDeviceAttributes.mEnabled = true;
                }
            } else {
                int canonicalDeviceType = getCanonicalDeviceType(audioDeviceAttributes.getType());
                if (canonicalDeviceType == 0) {
                    Log.e(TAG, "addCompatibleAudioDevice with incompatible AudioDeviceAttributes " + audioDeviceAttributes);
                    return;
                }
                findDeviceStateForAudioDeviceAttributes = new SADeviceState(canonicalDeviceType, audioDeviceAttributes.getAddress());
                this.mSADevices.add(findDeviceStateForAudioDeviceAttributes);
            }
            if (findDeviceStateForAudioDeviceAttributes != null) {
                onRoutingUpdated();
                this.mAudioService.persistSpatialAudioDeviceSettings();
                logDeviceState(findDeviceStateForAudioDeviceAttributes, "addCompatibleAudioDevice");
            }
        }
    }

    private void logDeviceState(SADeviceState sADeviceState, String str) {
        new MediaMetrics.Item(METRICS_DEVICE_PREFIX + AudioSystem.getDeviceName(AudioDeviceInfo.convertDeviceTypeToInternalDevice(sADeviceState.mDeviceType))).set(MediaMetrics.Property.ADDRESS, sADeviceState.mDeviceAddress).set(MediaMetrics.Property.ENABLED, sADeviceState.mEnabled ? "true" : "false").set(MediaMetrics.Property.EVENT, TextUtils.emptyIfNull(str)).set(MediaMetrics.Property.HAS_HEAD_TRACKER, sADeviceState.mHasHeadTracker ? "true" : "false").set(MediaMetrics.Property.HEAD_TRACKER_ENABLED, sADeviceState.mHeadTrackerEnabled ? "true" : "false").record();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void removeCompatibleAudioDevice(AudioDeviceAttributes audioDeviceAttributes) {
        loglogi("removeCompatibleAudioDevice: dev=" + audioDeviceAttributes);
        SADeviceState findDeviceStateForAudioDeviceAttributes = findDeviceStateForAudioDeviceAttributes(audioDeviceAttributes);
        if (findDeviceStateForAudioDeviceAttributes != null && findDeviceStateForAudioDeviceAttributes.mEnabled) {
            findDeviceStateForAudioDeviceAttributes.mEnabled = false;
            onRoutingUpdated();
            this.mAudioService.persistSpatialAudioDeviceSettings();
            logDeviceState(findDeviceStateForAudioDeviceAttributes, "removeCompatibleAudioDevice");
        }
    }

    private static int getCanonicalDeviceType(int i) {
        if (isWireless(i)) {
            return i;
        }
        int i2 = SPAT_MODE_FOR_DEVICE_TYPE.get(i, Integer.MIN_VALUE);
        if (i2 == 1) {
            return 2;
        }
        return i2 == 0 ? 4 : 0;
    }

    @GuardedBy({"this"})
    private SADeviceState findDeviceStateForAudioDeviceAttributes(AudioDeviceAttributes audioDeviceAttributes) {
        int type = audioDeviceAttributes.getType();
        boolean isWireless = isWireless(type);
        int canonicalDeviceType = getCanonicalDeviceType(type);
        Iterator<SADeviceState> it = this.mSADevices.iterator();
        while (it.hasNext()) {
            SADeviceState next = it.next();
            if (next.mDeviceType == canonicalDeviceType && (!isWireless || audioDeviceAttributes.getAddress().equals(next.mDeviceAddress))) {
                return next;
            }
        }
        return null;
    }

    private synchronized Pair<Boolean, Boolean> evaluateState(AudioDeviceAttributes audioDeviceAttributes) {
        int type = audioDeviceAttributes.getType();
        if (!this.mSACapableDeviceTypes.contains(Integer.valueOf(type))) {
            Log.i(TAG, "Device incompatible with Spatial Audio dev:" + audioDeviceAttributes);
            Boolean bool = Boolean.FALSE;
            return new Pair<>(bool, bool);
        }
        if (SPAT_MODE_FOR_DEVICE_TYPE.get(type, Integer.MIN_VALUE) == Integer.MIN_VALUE) {
            Log.e(TAG, "no spatialization mode found for device type:" + type);
            Boolean bool2 = Boolean.FALSE;
            return new Pair<>(bool2, bool2);
        }
        SADeviceState findDeviceStateForAudioDeviceAttributes = findDeviceStateForAudioDeviceAttributes(audioDeviceAttributes);
        if (findDeviceStateForAudioDeviceAttributes == null) {
            Log.i(TAG, "no spatialization device state found for Spatial Audio device:" + audioDeviceAttributes);
            Boolean bool3 = Boolean.FALSE;
            return new Pair<>(bool3, bool3);
        }
        if (!this.mSpatialzerForSpeakerSupport) {
            updateUserEnableState(this.mUserStateOfSpeaker == 1, true);
        }
        Log.d(TAG, "Spatial Audio device type:" + type + ", mEnabled:" + findDeviceStateForAudioDeviceAttributes.mEnabled + ", mUserEnable:" + findDeviceStateForAudioDeviceAttributes.mUserEnabled + ", mUserStateOfSpeaker:" + this.mUserStateOfSpeaker);
        return new Pair<>(Boolean.valueOf(findDeviceStateForAudioDeviceAttributes.mEnabled), Boolean.TRUE);
    }

    private synchronized void addWirelessDeviceIfNew(AudioDeviceAttributes audioDeviceAttributes) {
        if (isDeviceCompatibleWithSpatializationModes(audioDeviceAttributes)) {
            if (findDeviceStateForAudioDeviceAttributes(audioDeviceAttributes) == null) {
                int canonicalDeviceType = getCanonicalDeviceType(audioDeviceAttributes.getType());
                if (canonicalDeviceType == 0) {
                    Log.e(TAG, "addWirelessDeviceIfNew with incompatible AudioDeviceAttributes " + audioDeviceAttributes);
                    return;
                }
                SADeviceState sADeviceState = new SADeviceState(canonicalDeviceType, audioDeviceAttributes.getAddress());
                this.mSADevices.add(sADeviceState);
                this.mAudioService.persistSpatialAudioDeviceSettings();
                logDeviceState(sADeviceState, "addWirelessDeviceIfNew");
                if (this.mDesiredHeadTrackingMode != -2) {
                    Log.d(TAG, "addWirelessDeviceIfNew postInitSensors");
                    this.newWirelessDevice = audioDeviceAttributes;
                    postInitSensors();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isEnabled() {
        int i = this.mState;
        if (i == 0 || i == 1 || i == 3 || i == 6) {
            loglogi("isEnabled false");
            return false;
        }
        loglogi("isEnabled true");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isAvailable() {
        int i = this.mState;
        if (i == 0 || i == 1 || i == 3 || i == 4) {
            loglogi("isAvailable false");
            return false;
        }
        loglogi("isAvailable true");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isAvailableForDevice(AudioDeviceAttributes audioDeviceAttributes) {
        if (audioDeviceAttributes.getRole() != 2) {
            return false;
        }
        return findDeviceStateForAudioDeviceAttributes(audioDeviceAttributes) != null;
    }

    private synchronized boolean canBeSpatializedOnDevice(AudioAttributes audioAttributes, AudioFormat audioFormat, ArrayList<AudioDeviceAttributes> arrayList) {
        if (arrayList.isEmpty()) {
            return false;
        }
        if (!isDeviceCompatibleWithSpatializationModes(arrayList.get(0))) {
            return false;
        }
        return AudioSystem.canBeSpatialized(audioAttributes, audioFormat, (AudioDeviceAttributes[]) arrayList.toArray(new AudioDeviceAttributes[arrayList.size()]));
    }

    private boolean isDeviceCompatibleWithSpatializationModes(AudioDeviceAttributes audioDeviceAttributes) {
        byte b = (byte) SPAT_MODE_FOR_DEVICE_TYPE.get(audioDeviceAttributes.getType(), -1);
        return (b == 0 && this.mBinauralSupported) || (b == 1 && this.mTransauralSupported);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setFeatureEnabled(boolean z, boolean z2) {
        loglogi("setFeatureEnabled(" + z + ") was featureEnabled:" + this.mFeatureEnabled);
        if (this.mFeatureEnabled == z) {
            if (sRoutingDevices.isEmpty()) {
                logloge("setFeatureEnabled: no device");
                return;
            }
            if (sRoutingDevices.get(0) != null) {
                if (sRoutingDevices.get(0).getType() == 2) {
                    if ((this.mUserStateOfSpeaker == 1) != z && z2) {
                        Log.v(TAG, "User change state of spatializer on speaker");
                    }
                }
                SADeviceState findDeviceStateForAudioDeviceAttributes = findDeviceStateForAudioDeviceAttributes(sRoutingDevices.get(0));
                if (findDeviceStateForAudioDeviceAttributes != null && findDeviceStateForAudioDeviceAttributes.mUserEnabled == z) {
                    return;
                }
            }
        }
        this.mFeatureEnabled = z;
        if (z2) {
            AudioSystem.setParameters("OPLUS_SPATIALIZER_USERSPACE_STATE=" + (z ? 1 : 0));
            if (sRoutingDevices.get(0) == null || sRoutingDevices.get(0).getType() != 2) {
                if (!this.mSpatialzerForSpeakerSupport) {
                    this.mUserStateOfSpeaker = 0;
                }
            } else if (z) {
                this.mUserStateOfSpeaker = 1;
            } else {
                this.mUserStateOfSpeaker = 0;
            }
        }
        if (this.mFeatureEnabled) {
            int i = this.mState;
            if (i == 1) {
                Log.e(TAG, "Can't enabled Spatial Audio, unsupported");
            } else {
                if (i == 0) {
                    init(true, null);
                }
                setSpatializerEnabledInt(true, z2);
            }
        } else {
            setSpatializerEnabledInt(false, z2);
        }
    }

    synchronized void setSpatializerEnabledInt(boolean z, boolean z2) {
        int i = this.mState;
        if (i != 0) {
            if (i != 1) {
                if (i != 3) {
                    if (i == 4 || i == 5) {
                        if (!z) {
                            releaseSpat();
                            setDispatchFeatureEnabledState(false, "setSpatializerEnabledInt");
                            if (z2) {
                                updateUserEnableState(false, false);
                                if (!this.mSpatialzerForSpeakerSupport) {
                                    updateUserEnableState(false, true);
                                }
                            }
                        }
                    } else if (i != 6) {
                    }
                }
                if (z) {
                    createSpat();
                    if (z2) {
                        updateUserEnableState(true, false);
                    }
                    onRoutingUpdated();
                }
            } else if (z) {
                Log.e(TAG, "Can't enable when unsupported");
            }
        } else if (z) {
            throw new IllegalStateException("Can't enable when uninitialized");
        }
    }

    synchronized void updateUserEnableState(boolean z, boolean z2) {
        SADeviceState findDeviceStateForAudioDeviceAttributes;
        if (z2) {
            Iterator<SADeviceState> it = this.mSADevices.iterator();
            while (it.hasNext()) {
                SADeviceState next = it.next();
                if (next != null && next.mDeviceType == 2) {
                    next.mUserEnabled = z;
                }
            }
        } else if (!sRoutingDevices.isEmpty() && sRoutingDevices.get(0) != null && (findDeviceStateForAudioDeviceAttributes = findDeviceStateForAudioDeviceAttributes(sRoutingDevices.get(0))) != null) {
            findDeviceStateForAudioDeviceAttributes.mUserEnabled = z;
        }
        this.mAudioService.persistSpatialAudioDeviceSettings();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getCapableImmersiveAudioLevel() {
        return this.mCapableSpatLevel;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void registerStateCallback(ISpatializerCallback iSpatializerCallback) {
        this.mStateCallbacks.register(iSpatializerCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void unregisterStateCallback(ISpatializerCallback iSpatializerCallback) {
        this.mStateCallbacks.unregister(iSpatializerCallback);
    }

    private synchronized void setDispatchFeatureEnabledState(boolean z, String str) {
        if (z) {
            int i = this.mState;
            if (i == 3) {
                this.mState = 4;
            } else {
                if (i == 4 || i == 5) {
                    loglogi("setDispatchFeatureEnabledState(" + z + ") no dispatch: mState:" + spatStateString(this.mState) + " src:" + str);
                    return;
                }
                if (i == 6) {
                    this.mState = 5;
                } else {
                    throw new IllegalStateException("Invalid mState:" + this.mState + " for enabled true");
                }
            }
        } else {
            int i2 = this.mState;
            if (i2 != 3) {
                if (i2 == 4) {
                    this.mState = 3;
                } else if (i2 == 5) {
                    this.mState = 6;
                } else if (i2 != 6) {
                    throw new IllegalStateException("Invalid mState:" + this.mState + " for enabled false");
                }
            }
            loglogi("setDispatchFeatureEnabledState(" + z + ") no dispatch: mState:" + spatStateString(this.mState) + " src:" + str);
            return;
        }
        loglogi("setDispatchFeatureEnabledState(" + z + ") mState:" + spatStateString(this.mState));
        int beginBroadcast = this.mStateCallbacks.beginBroadcast();
        for (int i3 = 0; i3 < beginBroadcast; i3++) {
            try {
                this.mStateCallbacks.getBroadcastItem(i3).dispatchSpatializerEnabledChanged(z);
            } catch (RemoteException e) {
                Log.e(TAG, "Error in dispatchSpatializerEnabledChanged", e);
            }
        }
        this.mStateCallbacks.finishBroadcast();
    }

    private synchronized void setDispatchAvailableState(boolean z) {
        int i = this.mState;
        if (i == 0 || i == 1) {
            throw new IllegalStateException("Should not update available state in state:" + this.mState);
        }
        if (i != 3) {
            if (i != 4) {
                if (i == 5) {
                    if (z) {
                        loglogi("setDispatchAvailableState(" + z + ") no dispatch: mState:" + spatStateString(this.mState));
                        return;
                    }
                    this.mState = 4;
                } else if (i == 6) {
                    if (z) {
                        loglogi("setDispatchAvailableState(" + z + ") no dispatch: mState:" + spatStateString(this.mState));
                        return;
                    }
                    this.mState = 3;
                }
            } else if (z) {
                this.mState = 5;
            } else {
                loglogi("setDispatchAvailableState(" + z + ") no dispatch: mState:" + spatStateString(this.mState));
                return;
            }
        } else if (z) {
            this.mState = 6;
        } else {
            loglogi("setDispatchAvailableState(" + z + ") no dispatch: mState:" + spatStateString(this.mState));
            return;
        }
        loglogi("setDispatchAvailableState(" + z + ") mState:" + spatStateString(this.mState));
        int beginBroadcast = this.mStateCallbacks.beginBroadcast();
        for (int i2 = 0; i2 < beginBroadcast; i2++) {
            try {
                this.mStateCallbacks.getBroadcastItem(i2).dispatchSpatializerAvailableChanged(z);
            } catch (RemoteException e) {
                Log.e(TAG, "Error in dispatchSpatializerEnabledChanged", e);
            }
        }
        this.mStateCallbacks.finishBroadcast();
    }

    private void createSpat() {
        if (this.mSpat == null) {
            SpatializerCallback spatializerCallback = new SpatializerCallback();
            this.mSpatCallback = spatializerCallback;
            ISpatializer spatializer = AudioSystem.getSpatializer(spatializerCallback);
            this.mSpat = spatializer;
            try {
                if (!this.mIsHeadTrackingSupported || spatializer == null) {
                    return;
                }
                this.mActualHeadTrackingMode = headTrackingModeTypeToSpatializerInt(spatializer.getActualHeadTrackingMode());
                this.mSpat.registerHeadTrackingCallback(this.mSpatHeadTrackingCallback);
            } catch (RemoteException e) {
                Log.e(TAG, "Can't configure head tracking", e);
                this.mState = 1;
                this.mCapableSpatLevel = 0;
                this.mActualHeadTrackingMode = -2;
            }
        }
    }

    private void releaseSpat() {
        ISpatializer iSpatializer = this.mSpat;
        if (iSpatializer != null) {
            this.mSpatCallback = null;
            try {
                if (this.mIsHeadTrackingSupported) {
                    iSpatializer.registerHeadTrackingCallback((ISpatializerHeadTrackingCallback) null);
                }
                this.mHeadTrackerAvailable = false;
                this.mSpat.release();
            } catch (RemoteException e) {
                Log.e(TAG, "Can't set release spatializer cleanly", e);
            }
            this.mSpat = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean canBeSpatialized(AudioAttributes audioAttributes, AudioFormat audioFormat) {
        int i = this.mState;
        if (i == 0 || i == 1 || i == 3 || i == 4) {
            logd("canBeSpatialized false due to state:" + this.mState);
            return false;
        }
        int usage = audioAttributes.getUsage();
        if (usage != 1 && usage != 14) {
            logd("canBeSpatialized false due to usage:" + audioAttributes.getUsage());
            return false;
        }
        ArrayList<AudioDeviceAttributes> routingDevices = getRoutingDevices(audioAttributes);
        if (routingDevices.isEmpty()) {
            logloge("canBeSpatialized got no device for " + audioAttributes);
            return false;
        }
        boolean canBeSpatializedOnDevice = canBeSpatializedOnDevice(audioAttributes, audioFormat, routingDevices);
        logd("canBeSpatialized usage:" + audioAttributes.getUsage() + " format:" + audioFormat.toLogFriendlyString() + " returning " + canBeSpatializedOnDevice);
        return canBeSpatializedOnDevice;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void registerHeadTrackingModeCallback(ISpatializerHeadTrackingModeCallback iSpatializerHeadTrackingModeCallback) {
        this.mHeadTrackingModeCallbacks.register(iSpatializerHeadTrackingModeCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void unregisterHeadTrackingModeCallback(ISpatializerHeadTrackingModeCallback iSpatializerHeadTrackingModeCallback) {
        this.mHeadTrackingModeCallbacks.unregister(iSpatializerHeadTrackingModeCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void registerHeadTrackerAvailableCallback(ISpatializerHeadTrackerAvailableCallback iSpatializerHeadTrackerAvailableCallback, boolean z) {
        if (z) {
            this.mHeadTrackerCallbacks.register(iSpatializerHeadTrackerAvailableCallback);
        } else {
            this.mHeadTrackerCallbacks.unregister(iSpatializerHeadTrackerAvailableCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int[] getSupportedHeadTrackingModes() {
        return this.mSupportedHeadTrackingModes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getActualHeadTrackingMode() {
        return this.mActualHeadTrackingMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getDesiredHeadTrackingMode() {
        return this.mDesiredHeadTrackingMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setGlobalTransform(float[] fArr) {
        if (fArr.length != 6) {
            throw new IllegalArgumentException("invalid array size" + fArr.length);
        }
        if (checkSpatializerForHeadTracking("setGlobalTransform")) {
            try {
                this.mSpat.setGlobalTransform(fArr);
            } catch (RemoteException e) {
                Log.e(TAG, "Error calling setGlobalTransform", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void recenterHeadTracker() {
        if (checkSpatializerForHeadTracking("recenterHeadTracker")) {
            try {
                this.mSpat.recenterHeadTracker();
            } catch (RemoteException e) {
                Log.e(TAG, "Error calling recenterHeadTracker", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setDisplayOrientation(float f) {
        if (checkSpatializer("setDisplayOrientation")) {
            try {
                this.mSpat.setDisplayOrientation(f);
            } catch (RemoteException e) {
                Log.e(TAG, "Error calling setDisplayOrientation", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setFoldState(boolean z) {
        if (checkSpatializer("setFoldState")) {
            try {
                this.mSpat.setFoldState(z);
            } catch (RemoteException e) {
                Log.e(TAG, "Error calling setFoldState", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setDesiredHeadTrackingMode(@Spatializer.HeadTrackingModeSet int i) {
        String nameForUid;
        if (checkSpatializerForHeadTracking("setDesiredHeadTrackingMode")) {
            PackageManager packageManager = this.mAudioService.mContext.getPackageManager();
            if (packageManager != null && (nameForUid = packageManager.getNameForUid(Binder.getCallingUid())) != null && !nameForUid.equals("android.media.audio.cts") && !sRoutingDevices.isEmpty() && sRoutingDevices.get(0) != null && !hasHeadTracker(sRoutingDevices.get(0))) {
                Log.e(TAG, "Called setDesiredHeadTrackingMode mode:" + Spatializer.headtrackingModeToString(i) + " device:" + sRoutingDevices.get(0) + " on a device without headtracker");
                return;
            }
            if (i != -1) {
                this.mDesiredHeadTrackingModeWhenEnabled = i;
            }
            try {
                if (this.mDesiredHeadTrackingMode != i) {
                    this.mDesiredHeadTrackingMode = i;
                    dispatchDesiredHeadTrackingMode(i);
                }
                Log.i(TAG, "setDesiredHeadTrackingMode(" + Spatializer.headtrackingModeToString(i) + ")");
                this.mSpat.setDesiredHeadTrackingMode(spatializerIntToHeadTrackingModeType(i));
            } catch (RemoteException e) {
                Log.e(TAG, "Error calling setDesiredHeadTrackingMode", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setHeadTrackerEnabled(boolean z, AudioDeviceAttributes audioDeviceAttributes) {
        if (!this.mIsHeadTrackingSupported) {
            Log.v(TAG, "no headtracking support, ignoring setHeadTrackerEnabled to " + z + " for " + audioDeviceAttributes);
        }
        SADeviceState findDeviceStateForAudioDeviceAttributes = findDeviceStateForAudioDeviceAttributes(audioDeviceAttributes);
        if (findDeviceStateForAudioDeviceAttributes == null) {
            return;
        }
        if (!findDeviceStateForAudioDeviceAttributes.mHasHeadTracker) {
            Log.e(TAG, "Called setHeadTrackerEnabled enabled:" + z + " device:" + audioDeviceAttributes + " on a device without headtracker");
            return;
        }
        Log.i(TAG, "setHeadTrackerEnabled enabled:" + z + " device:" + audioDeviceAttributes);
        findDeviceStateForAudioDeviceAttributes.mHeadTrackerEnabled = z;
        this.mAudioService.persistSpatialAudioDeviceSettings();
        logDeviceState(findDeviceStateForAudioDeviceAttributes, "setHeadTrackerEnabled");
        if (sRoutingDevices.isEmpty()) {
            logloge("setHeadTrackerEnabled: no device, bailing");
            return;
        }
        AudioDeviceAttributes audioDeviceAttributes2 = sRoutingDevices.get(0);
        if (audioDeviceAttributes2.getType() == audioDeviceAttributes.getType() && audioDeviceAttributes2.getAddress().equals(audioDeviceAttributes.getAddress())) {
            setDesiredHeadTrackingMode(z ? this.mDesiredHeadTrackingModeWhenEnabled : -1);
            if (z && !this.mHeadTrackerAvailable) {
                postInitSensors();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean hasHeadTracker(AudioDeviceAttributes audioDeviceAttributes) {
        boolean z = false;
        if (!this.mIsHeadTrackingSupported) {
            Log.v(TAG, "no headtracking support, hasHeadTracker always false for " + audioDeviceAttributes);
            return false;
        }
        SADeviceState findDeviceStateForAudioDeviceAttributes = findDeviceStateForAudioDeviceAttributes(audioDeviceAttributes);
        if (findDeviceStateForAudioDeviceAttributes != null && findDeviceStateForAudioDeviceAttributes.mHasHeadTracker) {
            z = true;
        }
        return z;
    }

    synchronized boolean setHasHeadTracker(AudioDeviceAttributes audioDeviceAttributes) {
        if (!this.mIsHeadTrackingSupported) {
            Log.v(TAG, "no headtracking support, setHasHeadTracker always false for " + audioDeviceAttributes);
            return false;
        }
        SADeviceState findDeviceStateForAudioDeviceAttributes = findDeviceStateForAudioDeviceAttributes(audioDeviceAttributes);
        if (findDeviceStateForAudioDeviceAttributes != null) {
            if (!findDeviceStateForAudioDeviceAttributes.mHasHeadTracker) {
                findDeviceStateForAudioDeviceAttributes.mHasHeadTracker = true;
                this.mAudioService.persistSpatialAudioDeviceSettings();
                logDeviceState(findDeviceStateForAudioDeviceAttributes, "setHasHeadTracker");
            }
            return findDeviceStateForAudioDeviceAttributes.mHeadTrackerEnabled;
        }
        Log.e(TAG, "setHasHeadTracker: device not found for:" + audioDeviceAttributes);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isHeadTrackerEnabled(AudioDeviceAttributes audioDeviceAttributes) {
        boolean z = false;
        if (!this.mIsHeadTrackingSupported) {
            Log.v(TAG, "no headtracking support, isHeadTrackerEnabled always false for " + audioDeviceAttributes);
            return false;
        }
        SADeviceState findDeviceStateForAudioDeviceAttributes = findDeviceStateForAudioDeviceAttributes(audioDeviceAttributes);
        if (findDeviceStateForAudioDeviceAttributes != null && findDeviceStateForAudioDeviceAttributes.mHasHeadTracker && findDeviceStateForAudioDeviceAttributes.mHeadTrackerEnabled) {
            z = true;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isHeadTrackerAvailable() {
        return this.mHeadTrackerAvailable;
    }

    private boolean checkSpatializer(String str) {
        int i = this.mState;
        if (i == 0 || i == 1) {
            return false;
        }
        if ((i != 3 && i != 4 && i != 5 && i != 6) || this.mSpat != null) {
            return true;
        }
        if (this.mFeatureEnabled) {
            Log.e(TAG, "checkSpatializer(): called from " + str + "(), native spatializer should not be null in state: " + this.mState);
            postReset();
        }
        return false;
    }

    private boolean checkSpatializerForHeadTracking(String str) {
        return checkSpatializer(str) && this.mIsHeadTrackingSupported;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchActualHeadTrackingMode(int i) {
        int beginBroadcast = this.mHeadTrackingModeCallbacks.beginBroadcast();
        for (int i2 = 0; i2 < beginBroadcast; i2++) {
            try {
                this.mHeadTrackingModeCallbacks.getBroadcastItem(i2).dispatchSpatializerActualHeadTrackingModeChanged(i);
            } catch (RemoteException e) {
                Log.e(TAG, "Error in dispatchSpatializerActualHeadTrackingModeChanged(" + i + ")", e);
            }
        }
        this.mHeadTrackingModeCallbacks.finishBroadcast();
    }

    private void dispatchDesiredHeadTrackingMode(int i) {
        int beginBroadcast = this.mHeadTrackingModeCallbacks.beginBroadcast();
        for (int i2 = 0; i2 < beginBroadcast; i2++) {
            try {
                this.mHeadTrackingModeCallbacks.getBroadcastItem(i2).dispatchSpatializerDesiredHeadTrackingModeChanged(i);
            } catch (RemoteException e) {
                Log.e(TAG, "Error in dispatchSpatializerDesiredHeadTrackingModeChanged(" + i + ")", e);
            }
        }
        this.mHeadTrackingModeCallbacks.finishBroadcast();
    }

    private void dispatchHeadTrackerAvailable(boolean z) {
        int beginBroadcast = this.mHeadTrackerCallbacks.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                this.mHeadTrackerCallbacks.getBroadcastItem(i).dispatchSpatializerHeadTrackerAvailable(z);
            } catch (RemoteException e) {
                Log.e(TAG, "Error in dispatchSpatializerHeadTrackerAvailable(" + z + ")", e);
            }
        }
        this.mHeadTrackerCallbacks.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void registerHeadToSoundstagePoseCallback(ISpatializerHeadToSoundStagePoseCallback iSpatializerHeadToSoundStagePoseCallback) {
        this.mHeadPoseCallbacks.register(iSpatializerHeadToSoundStagePoseCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void unregisterHeadToSoundstagePoseCallback(ISpatializerHeadToSoundStagePoseCallback iSpatializerHeadToSoundStagePoseCallback) {
        this.mHeadPoseCallbacks.unregister(iSpatializerHeadToSoundStagePoseCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchPoseUpdate(float[] fArr) {
        int beginBroadcast = this.mHeadPoseCallbacks.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                this.mHeadPoseCallbacks.getBroadcastItem(i).dispatchPoseChanged(fArr);
            } catch (RemoteException e) {
                Log.e(TAG, "Error in dispatchPoseChanged", e);
            }
        }
        this.mHeadPoseCallbacks.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setEffectParameter(int i, byte[] bArr) {
        int i2 = this.mState;
        if (i2 == 0 || i2 == 1) {
            throw new IllegalStateException("Can't set parameter key:" + i + " without a spatializer");
        }
        if ((i2 == 3 || i2 == 4 || i2 == 5 || i2 == 6) && this.mSpat == null) {
            Log.e(TAG, "setParameter(" + i + "): null spatializer in state: " + this.mState);
            return;
        }
        try {
            this.mSpat.setParameter(i, bArr);
        } catch (RemoteException e) {
            Log.e(TAG, "Error in setParameter for key:" + i, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void getEffectParameter(int i, byte[] bArr) {
        int i2 = this.mState;
        if (i2 == 0 || i2 == 1) {
            throw new IllegalStateException("Can't get parameter key:" + i + " without a spatializer");
        }
        if ((i2 == 3 || i2 == 4 || i2 == 5 || i2 == 6) && this.mSpat == null) {
            Log.e(TAG, "getParameter(" + i + "): null spatializer in state: " + this.mState);
            return;
        }
        try {
            this.mSpat.getParameter(i, bArr);
        } catch (RemoteException e) {
            Log.e(TAG, "Error in getParameter for key:" + i, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getOutput() {
        int i = this.mState;
        if (i == 0 || i == 1) {
            throw new IllegalStateException("Can't get output without a spatializer");
        }
        if ((i == 3 || i == 4 || i == 5 || i == 6) && this.mSpat == null) {
            throw new IllegalStateException("null Spatializer for getOutput");
        }
        try {
        } catch (RemoteException e) {
            Log.e(TAG, "Error in getOutput", e);
            return 0;
        }
        return this.mSpat.getOutput();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void registerSpatializerOutputCallback(ISpatializerOutputCallback iSpatializerOutputCallback) {
        this.mOutputCallbacks.register(iSpatializerOutputCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void unregisterSpatializerOutputCallback(ISpatializerOutputCallback iSpatializerOutputCallback) {
        this.mOutputCallbacks.unregister(iSpatializerOutputCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchOutputUpdate(int i) {
        int beginBroadcast = this.mOutputCallbacks.beginBroadcast();
        for (int i2 = 0; i2 < beginBroadcast; i2++) {
            try {
                this.mOutputCallbacks.getBroadcastItem(i2).dispatchSpatializerOutputChanged(i);
            } catch (RemoteException e) {
                Log.e(TAG, "Error in dispatchOutputUpdate", e);
            }
        }
        this.mOutputCallbacks.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postInitSensors() {
        this.mAudioService.postInitSpatializerHeadTrackingSensors();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't wrap try/catch for region: R(16:21|(1:72)(1:31)|32|(12:36|(1:40)|41|42|43|45|46|(1:48)(1:58)|(2:(1:51)(1:54)|52)|55|56|57)|63|(2:66|67)|65|42|43|45|46|(0)(0)|(0)|55|56|57) */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0181, code lost:
    
        r1 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0182, code lost:
    
        android.util.Log.e(com.android.server.audio.SpatializerHelper.TAG, "Error calling setHeadSensor:" + r0, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x013b, code lost:
    
        r4 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x013c, code lost:
    
        android.util.Log.e(com.android.server.audio.SpatializerHelper.TAG, "Error calling setScreenSensor:" + r3, r4);
     */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0172  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0177  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0174  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public synchronized void onInitSensors() {
        boolean z;
        int headSensorHandleUpdateTracker;
        int screenSensorHandle;
        HelperDynamicSensorCallback helperDynamicSensorCallback;
        boolean z2 = true;
        boolean z3 = this.mFeatureEnabled && this.mSpatLevel != 0;
        String str = z3 ? "initializing" : "releasing";
        if (this.mSpat == null) {
            logloge("not " + str + " sensors, null spatializer");
            return;
        }
        if (!this.mIsHeadTrackingSupported) {
            logloge("not " + str + " sensors, spatializer doesn't support headtracking");
            return;
        }
        if (this.newWirelessDevice == null || sRoutingDevices.isEmpty() || sRoutingDevices.get(0) == null || sRoutingDevices.get(0).getType() != this.newWirelessDevice.getType() || !sRoutingDevices.get(0).getAddress().equals(this.newWirelessDevice.getAddress())) {
            z = false;
        } else {
            Log.d(TAG, "onInitSensors, newWirelessDevice first add check headtracker");
            this.newWirelessDevice = null;
            z = true;
        }
        if (!z3 && !z) {
            SensorManager sensorManager = this.mSensorManager;
            if (sensorManager != null && (helperDynamicSensorCallback = this.mDynSensorCallback) != null) {
                sensorManager.unregisterDynamicSensorCallback(helperDynamicSensorCallback);
                this.mSensorManager = null;
                this.mDynSensorCallback = null;
            }
            headSensorHandleUpdateTracker = -1;
            screenSensorHandle = -1;
            Log.i(TAG, "setScreenSensor:" + screenSensorHandle);
            this.mSpat.setScreenSensor(screenSensorHandle);
            Log.i(TAG, "setHeadSensor:" + headSensorHandleUpdateTracker);
            this.mSpat.setHeadSensor(headSensorHandleUpdateTracker);
            if (this.mHeadTrackerAvailable != (headSensorHandleUpdateTracker == -1)) {
                if (headSensorHandleUpdateTracker == -1) {
                    z2 = false;
                }
                this.mHeadTrackerAvailable = z2;
                dispatchHeadTrackerAvailable(z2);
            }
            setDesiredHeadTrackingMode(this.mDesiredHeadTrackingMode);
        }
        if (this.mSensorManager == null) {
            try {
                this.mSensorManager = (SensorManager) this.mAudioService.mContext.getSystemService(IOplusSceneManager.APP_SCENE_SENSOR);
                HelperDynamicSensorCallback helperDynamicSensorCallback2 = new HelperDynamicSensorCallback();
                this.mDynSensorCallback = helperDynamicSensorCallback2;
                this.mSensorManager.registerDynamicSensorCallback(helperDynamicSensorCallback2);
            } catch (Exception e) {
                Log.e(TAG, "Error with SensorManager, can't initialize sensors", e);
                this.mSensorManager = null;
                this.mDynSensorCallback = null;
                return;
            }
        }
        headSensorHandleUpdateTracker = getHeadSensorHandleUpdateTracker();
        loglogi("head tracker sensor handle initialized to " + headSensorHandleUpdateTracker);
        screenSensorHandle = getScreenSensorHandle();
        Log.i(TAG, "found screen sensor handle initialized to " + screenSensorHandle);
        Log.i(TAG, "setScreenSensor:" + screenSensorHandle);
        this.mSpat.setScreenSensor(screenSensorHandle);
        Log.i(TAG, "setHeadSensor:" + headSensorHandleUpdateTracker);
        this.mSpat.setHeadSensor(headSensorHandleUpdateTracker);
        if (this.mHeadTrackerAvailable != (headSensorHandleUpdateTracker == -1)) {
        }
        setDesiredHeadTrackingMode(this.mDesiredHeadTrackingMode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int headTrackingModeTypeToSpatializerInt(byte b) {
        if (b == 0) {
            return 0;
        }
        if (b == 1) {
            return -1;
        }
        if (b == 2) {
            return 1;
        }
        if (b == 3) {
            return 2;
        }
        throw new IllegalArgumentException("Unexpected head tracking mode:" + ((int) b));
    }

    private static byte spatializerIntToHeadTrackingModeType(int i) {
        if (i == -1) {
            return (byte) 1;
        }
        if (i == 0) {
            return (byte) 0;
        }
        if (i == 1) {
            return (byte) 2;
        }
        if (i == 2) {
            return (byte) 3;
        }
        throw new IllegalArgumentException("Unexpected head tracking mode:" + i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int spatializationLevelToSpatializerInt(byte b) {
        if (b == 0) {
            return 0;
        }
        if (b == 1) {
            return 1;
        }
        if (b == 2) {
            return 2;
        }
        throw new IllegalArgumentException("Unexpected spatializer level:" + ((int) b));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.println("SpatializerHelper:");
        printWriter.println("\tmState:" + this.mState);
        printWriter.println("\tmSpatLevel:" + this.mSpatLevel);
        printWriter.println("\tmCapableSpatLevel:" + this.mCapableSpatLevel);
        printWriter.println("\tmIsHeadTrackingSupported:" + this.mIsHeadTrackingSupported);
        StringBuilder sb = new StringBuilder();
        for (int i : this.mSupportedHeadTrackingModes) {
            sb.append(Spatializer.headtrackingModeToString(i));
            sb.append(" ");
        }
        printWriter.println("\tsupported head tracking modes:" + ((Object) sb));
        printWriter.println("\tmDesiredHeadTrackingMode:" + Spatializer.headtrackingModeToString(this.mDesiredHeadTrackingMode));
        printWriter.println("\tmActualHeadTrackingMode:" + Spatializer.headtrackingModeToString(this.mActualHeadTrackingMode));
        printWriter.println("\theadtracker available:" + this.mHeadTrackerAvailable);
        printWriter.println("\tsupports binaural:" + this.mBinauralSupported + " / transaural:" + this.mTransauralSupported);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("\tmSpatOutput:");
        sb2.append(this.mSpatOutput);
        printWriter.println(sb2.toString());
        printWriter.println("\tdevices:");
        Iterator<SADeviceState> it = this.mSADevices.iterator();
        while (it.hasNext()) {
            printWriter.println("\t\t" + it.next());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class SADeviceState {
        static final String SETTING_DEVICE_SEPARATOR = "\\|";
        static final String SETTING_DEVICE_SEPARATOR_CHAR = "|";
        static final String SETTING_FIELD_SEPARATOR = ",";
        private static boolean sBinauralEnabledDefault = true;
        private static boolean sHeadTrackingEnabledDefault = false;
        private static boolean sTransauralEnabledDefault = true;
        final String mDeviceAddress;
        final int mDeviceType;
        boolean mEnabled;
        boolean mHeadTrackerEnabled;
        boolean mHasHeadTracker = false;
        boolean mUserEnabled = false;

        SADeviceState(int i, String str) {
            boolean z = false;
            this.mDeviceType = i;
            if (SpatializerHelper.isWireless(i)) {
                Objects.requireNonNull(str);
            } else {
                str = "";
            }
            this.mDeviceAddress = str;
            int i2 = SpatializerHelper.SPAT_MODE_FOR_DEVICE_TYPE.get(i, Integer.MIN_VALUE);
            if (i2 == 0) {
                z = sBinauralEnabledDefault;
            } else if (i2 == 1) {
                z = sTransauralEnabledDefault;
            }
            this.mEnabled = z;
            this.mHeadTrackerEnabled = sHeadTrackingEnabledDefault;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || SADeviceState.class != obj.getClass()) {
                return false;
            }
            SADeviceState sADeviceState = (SADeviceState) obj;
            return this.mDeviceType == sADeviceState.mDeviceType && this.mDeviceAddress.equals(sADeviceState.mDeviceAddress) && this.mEnabled == sADeviceState.mEnabled && this.mHasHeadTracker == sADeviceState.mHasHeadTracker && this.mHeadTrackerEnabled == sADeviceState.mHeadTrackerEnabled;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mDeviceType), this.mDeviceAddress, Boolean.valueOf(this.mEnabled), Boolean.valueOf(this.mHasHeadTracker), Boolean.valueOf(this.mHeadTrackerEnabled));
        }

        public String toString() {
            return "type: " + this.mDeviceType + " addr: " + this.mDeviceAddress + " enabled: " + this.mEnabled + " HT: " + this.mHasHeadTracker + " HTenabled: " + this.mHeadTrackerEnabled;
        }

        String toPersistableString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mDeviceType);
            sb.append(SETTING_FIELD_SEPARATOR);
            sb.append(this.mDeviceAddress);
            sb.append(SETTING_FIELD_SEPARATOR);
            sb.append(this.mEnabled ? "1" : "0");
            sb.append(SETTING_FIELD_SEPARATOR);
            sb.append(this.mHasHeadTracker ? "1" : "0");
            sb.append(SETTING_FIELD_SEPARATOR);
            sb.append(this.mHeadTrackerEnabled ? "1" : "0");
            sb.append(SETTING_FIELD_SEPARATOR);
            sb.append(this.mUserEnabled ? "1" : "0");
            return sb.toString();
        }

        static SADeviceState fromPersistedString(String str) {
            if (str == null || str.isEmpty()) {
                return null;
            }
            String[] split = TextUtils.split(str, SETTING_FIELD_SEPARATOR);
            if (split.length < 5) {
                return null;
            }
            try {
                SADeviceState sADeviceState = new SADeviceState(Integer.parseInt(split[0]), split[1]);
                sADeviceState.mEnabled = Integer.parseInt(split[2]) == 1;
                sADeviceState.mHasHeadTracker = Integer.parseInt(split[3]) == 1;
                sADeviceState.mHeadTrackerEnabled = Integer.parseInt(split[4]) == 1;
                if (split.length > 5) {
                    sADeviceState.mUserEnabled = Integer.parseInt(split[5]) == 1;
                }
                return sADeviceState;
            } catch (NumberFormatException e) {
                Log.e(SpatializerHelper.TAG, "unable to parse setting for SADeviceState: " + str, e);
                return null;
            }
        }

        public AudioDeviceAttributes getAudioDeviceAttributes() {
            int i = this.mDeviceType;
            String str = this.mDeviceAddress;
            if (str == null) {
                str = "";
            }
            return new AudioDeviceAttributes(2, i, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String getSADeviceSettings() {
        StringBuilder sb;
        sb = new StringBuilder(this.mSADevices.size() * 25);
        for (int i = 0; i < this.mSADevices.size(); i++) {
            sb.append(this.mSADevices.get(i).toPersistableString());
            if (i != this.mSADevices.size() - 1) {
                sb.append("|");
            }
        }
        return sb.toString();
    }

    synchronized void setSADeviceSettings(String str) {
        Objects.requireNonNull(str);
        for (String str2 : TextUtils.split(str, "\\|")) {
            SADeviceState fromPersistedString = SADeviceState.fromPersistedString(str2);
            if (fromPersistedString != null) {
                int i = fromPersistedString.mDeviceType;
                if (i == getCanonicalDeviceType(i) && isDeviceCompatibleWithSpatializationModes(fromPersistedString.getAudioDeviceAttributes())) {
                    this.mSADevices.add(fromPersistedString);
                    logDeviceState(fromPersistedString, "setSADeviceSettings");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isWireless(int i) {
        for (int i2 : WIRELESS_TYPES) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    private int getHeadSensorHandleUpdateTracker() {
        if (sRoutingDevices.isEmpty()) {
            logloge("getHeadSensorHandleUpdateTracker: no device, no head tracker");
            return -1;
        }
        AudioDeviceAttributes audioDeviceAttributes = sRoutingDevices.get(0);
        UUID deviceSensorUuid = this.mAudioService.getDeviceSensorUuid(audioDeviceAttributes);
        int i = -1;
        for (Sensor sensor : this.mSensorManager.getDynamicSensorList(37)) {
            UUID uuid = sensor.getUuid();
            if (uuid.equals(deviceSensorUuid) || isWireless(audioDeviceAttributes.getType())) {
                int handle = sensor.getHandle();
                if (setHasHeadTracker(audioDeviceAttributes)) {
                    return handle;
                }
                return -1;
            }
            if (uuid.equals(UuidUtils.STANDALONE_UUID)) {
                i = sensor.getHandle();
            }
        }
        return i;
    }

    private int getScreenSensorHandle() {
        Sensor defaultSensor = this.mSensorManager.getDefaultSensor(11);
        if (defaultSensor != null) {
            return defaultSensor.getHandle();
        }
        return -1;
    }

    private ArrayList<AudioDeviceAttributes> getRoutingDevices(AudioAttributes audioAttributes) {
        ArrayList<AudioDeviceAttributes> devicesForAttributes = this.mASA.getDevicesForAttributes(audioAttributes, false);
        Iterator<AudioDeviceAttributes> it = devicesForAttributes.iterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                return new ArrayList<>(0);
            }
        }
        return devicesForAttributes;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loglogi(String str) {
        AudioService.sSpatialLogger.enqueueAndLog(str, 0, TAG);
    }

    private static String logloge(String str) {
        AudioService.sSpatialLogger.enqueueAndLog(str, 1, TAG);
        return str;
    }

    void clearSADevices() {
        this.mSADevices.clear();
    }

    synchronized void forceStateForTest(int i) {
        this.mState = i;
    }

    synchronized void initForTest(boolean z, boolean z2) {
        this.mBinauralSupported = z;
        this.mTransauralSupported = z2;
    }
}
