package com.android.server.audio;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.media.AudioDeviceAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioDevicePort;
import android.media.AudioManager;
import android.media.AudioPort;
import android.media.AudioRoutesInfo;
import android.media.AudioSystem;
import android.media.BluetoothProfileConnectionInfo;
import android.media.IAudioRoutesObserver;
import android.media.ICapturePresetDevicesRoleDispatcher;
import android.media.IStrategyNonDefaultDevicesDispatcher;
import android.media.IStrategyPreferredDevicesDispatcher;
import android.media.MediaMetrics;
import android.media.audiopolicy.AudioProductStrategy;
import android.media.permission.ClearCallingIdentityContext;
import android.media.permission.SafeCloseable;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.am.ProcessList;
import com.android.server.audio.AudioDeviceBroker;
import com.android.server.audio.AudioDeviceInventory;
import com.android.server.audio.AudioServiceEvents;
import com.android.server.utils.EventLogger;
import com.google.android.collect.Sets;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AudioDeviceInventory {
    private static final Set<Integer> BECOMING_NOISY_INTENT_DEVICES_SET;
    private static final String BIRECORD_DEVICE_NAME_PARAM = "binaural_recording_bt_name=";
    static final int[] CAPTURE_PRESETS;
    private static final String CONNECT_INTENT_KEY_ADDRESS = "address";
    private static final String CONNECT_INTENT_KEY_DEVICE_CLASS = "class";
    private static final String CONNECT_INTENT_KEY_HAS_CAPTURE = "hasCapture";
    private static final String CONNECT_INTENT_KEY_HAS_MIDI = "hasMIDI";
    private static final String CONNECT_INTENT_KEY_HAS_PLAYBACK = "hasPlayback";
    private static final String CONNECT_INTENT_KEY_PORT_NAME = "portName";
    private static final String CONNECT_INTENT_KEY_STATE = "state";
    static final Set<Integer> DEVICE_OVERRIDE_A2DP_ROUTE_ON_PLUG_SET;
    private static final String EXIT_GAME_MODE_PARAM = "AudioGameMode=0";
    private static final String TAG = "AS.AudioDeviceInventory";
    private static final String mMetricsId = "audio.device.";
    private IAudioDeviceInventoryExt mAdiExt;
    private AudioDeviceInventoryWrapper mAdiWrapper;

    @GuardedBy({"mDevicesLock"})
    private final ArrayMap<Integer, String> mApmConnectedDevices;
    private final ArrayMap<Pair<Integer, Integer>, List<AudioDeviceAttributes>> mAppliedPresetRoles;
    private final ArrayMap<Pair<Integer, Integer>, List<AudioDeviceAttributes>> mAppliedPresetRolesInt;
    private final ArrayMap<Pair<Integer, Integer>, List<AudioDeviceAttributes>> mAppliedStrategyRoles;
    private final ArrayMap<Pair<Integer, Integer>, List<AudioDeviceAttributes>> mAppliedStrategyRolesInt;
    private final AudioSystemAdapter mAudioSystem;
    final boolean mBluetoothDualModeEnabled;

    @GuardedBy({"mDevicesLock"})
    private final LinkedHashMap<String, DeviceInfo> mConnectedDevices;
    final AudioRoutesInfo mCurAudioRoutes;
    final RemoteCallbackList<ICapturePresetDevicesRoleDispatcher> mDevRoleCapturePresetDispatchers;
    private AudioDeviceBroker mDeviceBroker;
    private final Object mDevicesLock;
    private int mLastMusicBTdeviceConnected;
    final RemoteCallbackList<IStrategyNonDefaultDevicesDispatcher> mNonDefDevDispatchers;
    private final ArrayMap<Integer, List<AudioDeviceAttributes>> mNonDefaultDevices;
    final RemoteCallbackList<IStrategyPreferredDevicesDispatcher> mPrefDevDispatchers;
    private final ArrayMap<Integer, List<AudioDeviceAttributes>> mPreferredDevices;
    private final ArrayMap<Integer, List<AudioDeviceAttributes>> mPreferredDevicesForCapturePreset;
    final RemoteCallbackList<IAudioRoutesObserver> mRoutesObservers;
    final List<AudioProductStrategy> mStrategies;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface AudioSystemInterface {
        int deviceRoleAction(int i, int i2, List<AudioDeviceAttributes> list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioDeviceInventory(AudioDeviceBroker audioDeviceBroker) {
        this(audioDeviceBroker, AudioSystemAdapter.getDefaultAdapter());
    }

    AudioDeviceInventory(AudioSystemAdapter audioSystemAdapter) {
        this(null, audioSystemAdapter);
    }

    private AudioDeviceInventory(AudioDeviceBroker audioDeviceBroker, AudioSystemAdapter audioSystemAdapter) {
        this.mDevicesLock = new Object();
        this.mLastMusicBTdeviceConnected = 0;
        this.mAdiWrapper = new AudioDeviceInventoryWrapper();
        this.mAdiExt = (IAudioDeviceInventoryExt) ExtLoader.type(IAudioDeviceInventoryExt.class).base(this).create();
        this.mConnectedDevices = new LinkedHashMap<String, DeviceInfo>() { // from class: com.android.server.audio.AudioDeviceInventory.1
            @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
            public DeviceInfo put(String str, DeviceInfo deviceInfo) {
                DeviceInfo deviceInfo2 = (DeviceInfo) super.put((AnonymousClass1) str, (String) deviceInfo);
                record("put", true, str, deviceInfo);
                return deviceInfo2;
            }

            @Override // java.util.HashMap, java.util.Map
            public DeviceInfo putIfAbsent(String str, DeviceInfo deviceInfo) {
                DeviceInfo deviceInfo2 = (DeviceInfo) super.putIfAbsent((AnonymousClass1) str, (String) deviceInfo);
                if (deviceInfo2 == null) {
                    record("putIfAbsent", true, str, deviceInfo);
                }
                return deviceInfo2;
            }

            @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
            public DeviceInfo remove(Object obj) {
                DeviceInfo deviceInfo = (DeviceInfo) super.remove(obj);
                if (deviceInfo != null) {
                    record("remove", false, (String) obj, deviceInfo);
                }
                return deviceInfo;
            }

            @Override // java.util.HashMap, java.util.Map
            public boolean remove(Object obj, Object obj2) {
                boolean remove = super.remove(obj, obj2);
                if (remove) {
                    record("remove", false, (String) obj, (DeviceInfo) obj2);
                }
                return remove;
            }

            private void record(String str, boolean z, String str2, DeviceInfo deviceInfo) {
                new MediaMetrics.Item(AudioDeviceInventory.mMetricsId + AudioSystem.getDeviceName(deviceInfo.mDeviceType)).set(MediaMetrics.Property.ADDRESS, deviceInfo.mDeviceAddress).set(MediaMetrics.Property.EVENT, str).set(MediaMetrics.Property.NAME, deviceInfo.mDeviceName).set(MediaMetrics.Property.STATE, z ? "connected" : "disconnected").record();
            }
        };
        this.mApmConnectedDevices = new ArrayMap<>();
        this.mPreferredDevices = new ArrayMap<>();
        this.mNonDefaultDevices = new ArrayMap<>();
        this.mPreferredDevicesForCapturePreset = new ArrayMap<>();
        this.mCurAudioRoutes = new AudioRoutesInfo();
        this.mRoutesObservers = new RemoteCallbackList<>();
        this.mPrefDevDispatchers = new RemoteCallbackList<>();
        this.mNonDefDevDispatchers = new RemoteCallbackList<>();
        this.mDevRoleCapturePresetDispatchers = new RemoteCallbackList<>();
        this.mAppliedStrategyRoles = new ArrayMap<>();
        this.mAppliedStrategyRolesInt = new ArrayMap<>();
        this.mAppliedPresetRoles = new ArrayMap<>();
        this.mAppliedPresetRolesInt = new ArrayMap<>();
        this.mDeviceBroker = audioDeviceBroker;
        this.mAudioSystem = audioSystemAdapter;
        this.mAdiExt.init(audioDeviceBroker);
        this.mStrategies = AudioProductStrategy.getAudioProductStrategies();
        this.mBluetoothDualModeEnabled = SystemProperties.getBoolean("persist.bluetooth.enable_dual_mode_audio", false);
    }

    void setDeviceBroker(AudioDeviceBroker audioDeviceBroker) {
        this.mDeviceBroker = audioDeviceBroker;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class DeviceInfo {
        final String mDeviceAddress;
        int mDeviceCodecFormat;
        final String mDeviceName;
        final int mDeviceType;
        ArraySet<String> mDisabledModes;
        final UUID mSensorUuid;

        DeviceInfo(int i, String str, String str2, int i2, UUID uuid) {
            this.mDisabledModes = new ArraySet<>(0);
            this.mDeviceType = i;
            this.mDeviceName = str == null ? "" : str;
            this.mDeviceAddress = str2 == null ? "" : str2;
            this.mDeviceCodecFormat = i2;
            this.mSensorUuid = uuid;
        }

        void setModeDisabled(String str) {
            this.mDisabledModes.add(str);
        }

        void setModeEnabled(String str) {
            this.mDisabledModes.remove(str);
        }

        boolean isModeEnabled(String str) {
            return !this.mDisabledModes.contains(str);
        }

        boolean isOutputOnlyModeEnabled() {
            return isModeEnabled("audio_mode_output_only");
        }

        boolean isDuplexModeEnabled() {
            return isModeEnabled("audio_mode_duplex");
        }

        DeviceInfo(int i, String str, String str2, int i2) {
            this(i, str, str2, i2, null);
        }

        DeviceInfo(int i, String str, String str2) {
            this(i, str, str2, 0);
        }

        public String toString() {
            return "[DeviceInfo: type:0x" + Integer.toHexString(this.mDeviceType) + " (" + AudioSystem.getDeviceName(this.mDeviceType) + ") name:" + this.mDeviceName + " addr:" + this.mDeviceAddress + " codec: " + Integer.toHexString(this.mDeviceCodecFormat) + " sensorUuid: " + Objects.toString(this.mSensorUuid) + " disabled modes: " + this.mDisabledModes + "]";
        }

        String getKey() {
            return makeDeviceListKey(this.mDeviceType, this.mDeviceAddress);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String makeDeviceListKey(int i, String str) {
            return "0x" + Integer.toHexString(i) + ":" + str;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class WiredDeviceConnectionState {
        public final AudioDeviceAttributes mAttributes;
        public final String mCaller;
        public boolean mForTest = false;
        public final int mState;

        WiredDeviceConnectionState(AudioDeviceAttributes audioDeviceAttributes, int i, String str) {
            this.mAttributes = audioDeviceAttributes;
            this.mState = i;
            this.mCaller = str;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(final PrintWriter printWriter, final String str) {
        printWriter.println("\n" + str + "BECOMING_NOISY_INTENT_DEVICES_SET=");
        BECOMING_NOISY_INTENT_DEVICES_SET.forEach(new Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AudioDeviceInventory.lambda$dump$0(printWriter, (Integer) obj);
            }
        });
        printWriter.println("\n" + str + "Preferred devices for strategy:");
        this.mPreferredDevices.forEach(new BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AudioDeviceInventory.lambda$dump$1(printWriter, str, (Integer) obj, (List) obj2);
            }
        });
        printWriter.println("\n" + str + "Non-default devices for strategy:");
        this.mNonDefaultDevices.forEach(new BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AudioDeviceInventory.lambda$dump$2(printWriter, str, (Integer) obj, (List) obj2);
            }
        });
        printWriter.println("\n" + str + "Connected devices:");
        this.mConnectedDevices.forEach(new BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda6
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AudioDeviceInventory.lambda$dump$3(printWriter, str, (String) obj, (AudioDeviceInventory.DeviceInfo) obj2);
            }
        });
        printWriter.println("\n" + str + "APM Connected device (A2DP sink only):");
        this.mApmConnectedDevices.forEach(new BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda7
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AudioDeviceInventory.lambda$dump$4(printWriter, str, (Integer) obj, (String) obj2);
            }
        });
        printWriter.println("\n" + str + "Preferred devices for capture preset:");
        this.mPreferredDevicesForCapturePreset.forEach(new BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda8
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AudioDeviceInventory.lambda$dump$5(printWriter, str, (Integer) obj, (List) obj2);
            }
        });
        printWriter.println("\n" + str + "Applied devices roles for strategies (from API):");
        this.mAppliedStrategyRoles.forEach(new BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda9
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AudioDeviceInventory.lambda$dump$6(printWriter, str, (Pair) obj, (List) obj2);
            }
        });
        printWriter.println("\n" + str + "Applied devices roles for strategies (internal):");
        this.mAppliedStrategyRolesInt.forEach(new BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda10
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AudioDeviceInventory.lambda$dump$7(printWriter, str, (Pair) obj, (List) obj2);
            }
        });
        printWriter.println("\n" + str + "Applied devices roles for presets (from API):");
        this.mAppliedPresetRoles.forEach(new BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda11
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AudioDeviceInventory.lambda$dump$8(printWriter, str, (Pair) obj, (List) obj2);
            }
        });
        printWriter.println("\n" + str + "Applied devices roles for presets (internal:");
        this.mAppliedPresetRolesInt.forEach(new BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda12
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AudioDeviceInventory.lambda$dump$9(printWriter, str, (Pair) obj, (List) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$0(PrintWriter printWriter, Integer num) {
        printWriter.print(" 0x" + Integer.toHexString(num.intValue()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$1(PrintWriter printWriter, String str, Integer num, List list) {
        printWriter.println("  " + str + "strategy:" + num + " device:" + list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$2(PrintWriter printWriter, String str, Integer num, List list) {
        printWriter.println("  " + str + "strategy:" + num + " device:" + list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$3(PrintWriter printWriter, String str, String str2, DeviceInfo deviceInfo) {
        printWriter.println("  " + str + deviceInfo.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$4(PrintWriter printWriter, String str, Integer num, String str2) {
        printWriter.println("  " + str + " type:0x" + Integer.toHexString(num.intValue()) + " (" + AudioSystem.getDeviceName(num.intValue()) + ") addr:" + str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$5(PrintWriter printWriter, String str, Integer num, List list) {
        printWriter.println("  " + str + "capturePreset:" + num + " devices:" + list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$6(PrintWriter printWriter, String str, Pair pair, List list) {
        printWriter.println("  " + str + "strategy: " + pair.first + " role:" + pair.second + " devices:" + list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$7(PrintWriter printWriter, String str, Pair pair, List list) {
        printWriter.println("  " + str + "strategy: " + pair.first + " role:" + pair.second + " devices:" + list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$8(PrintWriter printWriter, String str, Pair pair, List list) {
        printWriter.println("  " + str + "preset: " + pair.first + " role:" + pair.second + " devices:" + list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$9(PrintWriter printWriter, String str, Pair pair, List list) {
        printWriter.println("  " + str + "preset: " + pair.first + " role:" + pair.second + " devices:" + list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onRestoreDevices() {
        synchronized (this.mDevicesLock) {
            for (DeviceInfo deviceInfo : this.mConnectedDevices.values()) {
                this.mAudioSystem.setDeviceConnectionState(new AudioDeviceAttributes(deviceInfo.mDeviceType, deviceInfo.mDeviceAddress, deviceInfo.mDeviceName), 1, deviceInfo.mDeviceCodecFormat);
            }
            this.mAppliedStrategyRolesInt.clear();
            this.mAppliedPresetRolesInt.clear();
            applyConnectedDevicesRoles_l();
        }
        reapplyExternalDevicesRoles();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reapplyExternalDevicesRoles() {
        synchronized (this.mDevicesLock) {
            this.mAppliedStrategyRoles.clear();
            this.mAppliedPresetRoles.clear();
        }
        synchronized (this.mPreferredDevices) {
            this.mPreferredDevices.forEach(new BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda21
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    AudioDeviceInventory.this.lambda$reapplyExternalDevicesRoles$10((Integer) obj, (List) obj2);
                }
            });
        }
        synchronized (this.mNonDefaultDevices) {
            this.mNonDefaultDevices.forEach(new BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda22
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    AudioDeviceInventory.this.lambda$reapplyExternalDevicesRoles$11((Integer) obj, (List) obj2);
                }
            });
        }
        synchronized (this.mPreferredDevicesForCapturePreset) {
            this.mPreferredDevicesForCapturePreset.forEach(new BiConsumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda23
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    AudioDeviceInventory.this.lambda$reapplyExternalDevicesRoles$12((Integer) obj, (List) obj2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reapplyExternalDevicesRoles$10(Integer num, List list) {
        setPreferredDevicesForStrategy(num.intValue(), list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reapplyExternalDevicesRoles$11(Integer num, List list) {
        addDevicesRoleForStrategy(num.intValue(), 2, list, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reapplyExternalDevicesRoles$12(Integer num, List list) {
        setDevicesRoleForCapturePreset(num.intValue(), 1, list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"AudioDeviceBroker.mDeviceStateLock"})
    public void onSetBtActiveDevice(AudioDeviceBroker.BtDeviceInfo btDeviceInfo, int i) {
        if (AudioService.DEBUG_DEVICES) {
            Log.d(TAG, "onSetBtActiveDevice, btDevice=" + btDeviceInfo.mDevice + ", profile=" + BluetoothProfile.getProfileName(btDeviceInfo.mProfile) + ", state=" + BluetoothProfile.getConnectionStateName(btDeviceInfo.mState) + ", device=0x" + Integer.toHexString(btDeviceInfo.mAudioSystemDevice) + ", vol=" + btDeviceInfo.mVolume + ", streamType=" + i);
        }
        String address = btDeviceInfo.mDevice.getAddress();
        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            address = "";
        }
        if (btDeviceInfo.mVolume != -1) {
            this.mDeviceBroker.getWrapper().setBluetoothDevice(btDeviceInfo.mDevice);
        }
        AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("BT connected: addr=" + address + " profile=" + btDeviceInfo.mProfile + " state=" + btDeviceInfo.mState + " codec=" + AudioSystem.audioFormatToString(btDeviceInfo.mCodec)));
        new MediaMetrics.Item("audio.device.onSetBtActiveDevice").set(MediaMetrics.Property.STATUS, Integer.valueOf(btDeviceInfo.mProfile)).set(MediaMetrics.Property.DEVICE, AudioSystem.getDeviceName(btDeviceInfo.mAudioSystemDevice)).set(MediaMetrics.Property.ADDRESS, address).set(MediaMetrics.Property.ENCODING, AudioSystem.audioFormatToString(btDeviceInfo.mCodec)).set(MediaMetrics.Property.EVENT, "onSetBtActiveDevice").set(MediaMetrics.Property.STREAM_TYPE, AudioSystem.streamToString(i)).set(MediaMetrics.Property.STATE, btDeviceInfo.mState == 2 ? "connected" : "disconnected").record();
        synchronized (this.mDevicesLock) {
            DeviceInfo deviceInfo = this.mConnectedDevices.get(DeviceInfo.makeDeviceListKey(btDeviceInfo.mAudioSystemDevice, address));
            boolean z = true;
            int i2 = 0;
            boolean z2 = deviceInfo != null;
            boolean z3 = z2 && btDeviceInfo.mState != 2;
            if (z2 || btDeviceInfo.mState != 2) {
                z = false;
            }
            int i3 = btDeviceInfo.mProfile;
            if (i3 != 2) {
                if (i3 != 11) {
                    if (i3 != 26) {
                        if (i3 != 21) {
                            if (i3 != 22) {
                                throw new IllegalArgumentException("Invalid profile " + BluetoothProfile.getProfileName(btDeviceInfo.mProfile));
                            }
                        } else if (z3) {
                            lambda$disconnectHearingAid$32(address);
                        } else if (z) {
                            makeHearingAidDeviceAvailable(address, BtHelper.getName(btDeviceInfo.mDevice), i, "onSetBtActiveDevice");
                        }
                    }
                    if (z3) {
                        makeLeAudioDeviceUnavailableNow(address, btDeviceInfo.mAudioSystemDevice);
                    } else if (z) {
                        makeLeAudioDeviceAvailable(btDeviceInfo, i, "onSetBtActiveDevice");
                        if (Build.isMtkPlatform()) {
                            if (i == -1) {
                                return;
                            }
                            if (this.mLastMusicBTdeviceConnected == 128 && btDeviceInfo.mAudioSystemDevice == 536870912 && btDeviceInfo.mVolume == -1) {
                                i2 = this.mDeviceBroker.getVssVolumeForDevice(i, 128);
                            }
                            int i4 = btDeviceInfo.mAudioSystemDevice;
                            if (i4 == 536870912) {
                                this.mLastMusicBTdeviceConnected = i4;
                            }
                            int i5 = btDeviceInfo.mVolume;
                            if (i5 != -1) {
                                i2 = i5 * 10;
                            }
                            if (i2 != -1) {
                                this.mDeviceBroker.postSetLeAudioVolumeIndex(i2, this.mDeviceBroker.getMaxVssVolumeForStream(i), i);
                                if (i == 3) {
                                    this.mDeviceBroker.postSetVolumeIndexOnDevice(i, i2, btDeviceInfo.mAudioSystemDevice, "onSetBtActiveDevice");
                                }
                            } else {
                                this.mDeviceBroker.postSetLeAudioVolumeIndex(this.mDeviceBroker.getVssVolumeForDevice(i, i4), this.mDeviceBroker.getMaxVssVolumeForStream(i), i);
                                this.mDeviceBroker.postApplyVolumeOnDevice(i, btDeviceInfo.mAudioSystemDevice, "onSetBtActiveDevice");
                            }
                        }
                        if (this.mDeviceBroker.getWrapper().getBluetoothVolSyncSupported()) {
                            if (i == -1) {
                                return;
                            }
                            int i6 = btDeviceInfo.mVolume;
                            if (i6 != -1) {
                                int finalBleVolume = this.mAdiExt.getFinalBleVolume(i6 * 10);
                                this.mDeviceBroker.postSetVolumeIndexOnDevice(i, finalBleVolume, btDeviceInfo.mAudioSystemDevice, "onSetBtActiveDevice");
                                this.mAdiExt.postAbsoluteBleVolume(finalBleVolume);
                            } else {
                                this.mDeviceBroker.postSetLeAudioVolumeIndex(this.mDeviceBroker.getVssVolumeForDevice(i, btDeviceInfo.mAudioSystemDevice), this.mDeviceBroker.getMaxVssVolumeForStream(i), i);
                                this.mDeviceBroker.postApplyVolumeOnDevice(i, btDeviceInfo.mAudioSystemDevice, "onSetBtActiveDevice");
                            }
                        }
                    }
                } else if (z3) {
                    lambda$disconnectA2dpSink$30(address);
                } else if (z) {
                    makeA2dpSrcAvailable(address, btDeviceInfo.mCodec);
                }
            } else if (z3) {
                makeA2dpDeviceUnavailableNow(address, deviceInfo.mDeviceCodecFormat);
            } else if (z) {
                int i7 = btDeviceInfo.mVolume;
                if (Build.isMtkPlatform() && this.mLastMusicBTdeviceConnected == 536870912 && btDeviceInfo.mVolume == -1) {
                    i7 = this.mDeviceBroker.getVssVolumeForDevice(i, AudioFormat.APTX) / 10;
                }
                if (i7 != -1) {
                    int finalA2dpVolume = this.mAdiExt.getFinalA2dpVolume(btDeviceInfo.mVolume);
                    this.mDeviceBroker.postSetVolumeIndexOnDevice(3, finalA2dpVolume * 10, btDeviceInfo.mAudioSystemDevice, "onSetBtActiveDevice");
                    this.mAdiExt.postAbsoluteA2dpVolume(finalA2dpVolume);
                }
                makeA2dpDeviceAvailable(btDeviceInfo, "onSetBtActiveDevice");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"AudioDeviceBroker.mDeviceStateLock"})
    public void onBluetoothDeviceConfigChange(AudioDeviceBroker.BtDeviceInfo btDeviceInfo, int i) {
        boolean z;
        boolean z2;
        MediaMetrics.Item item = new MediaMetrics.Item("audio.device.onBluetoothDeviceConfigChange").set(MediaMetrics.Property.EVENT, BtHelper.deviceEventToString(i));
        BluetoothDevice bluetoothDevice = btDeviceInfo.mDevice;
        if (bluetoothDevice == null) {
            item.set(MediaMetrics.Property.EARLY_RETURN, "btDevice null").record();
            if (this.mAdiExt.isMetaAudioSupport()) {
                this.mDeviceBroker.getWrapper().checkBtDeviceForCarkitBt(false, false);
                return;
            }
            return;
        }
        if (AudioService.DEBUG_DEVICES) {
            Log.d(TAG, "onBluetoothDeviceConfigChange btDevice=" + bluetoothDevice);
        }
        int i2 = btDeviceInfo.mVolume;
        int i3 = btDeviceInfo.mCodec;
        String address = bluetoothDevice.getAddress();
        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            address = "";
        }
        EventLogger eventLogger = AudioService.sDeviceLogger;
        eventLogger.enqueue(new EventLogger.StringEvent("onBluetoothDeviceConfigChange addr=" + address + " event=" + BtHelper.deviceEventToString(i)));
        synchronized (this.mDevicesLock) {
            String makeDeviceListKey = DeviceInfo.makeDeviceListKey(128, address);
            DeviceInfo deviceInfo = this.mConnectedDevices.get(makeDeviceListKey);
            if (this.mDeviceBroker.hasScheduledA2dpConnection(bluetoothDevice)) {
                this.mApmConnectedDevices.replace(128, makeDeviceListKey);
                eventLogger.enqueue(new EventLogger.StringEvent("A2dp config change ignored (scheduled connection change)").printLog(TAG));
                item.set(MediaMetrics.Property.EARLY_RETURN, "A2dp config change ignored").record();
                return;
            }
            if (deviceInfo == null) {
                Log.e(TAG, "invalid null DeviceInfo in onBluetoothDeviceConfigChange");
                item.set(MediaMetrics.Property.EARLY_RETURN, "null DeviceInfo").record();
                if (!Build.isQcomPlatform()) {
                    return;
                }
                synchronized (this.mConnectedDevices) {
                    Iterator<Map.Entry<String, DeviceInfo>> it = this.mConnectedDevices.entrySet().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Map.Entry<String, DeviceInfo> next = it.next();
                        if (next.getValue().mDeviceType == 128) {
                            this.mConnectedDevices.remove(next.getKey());
                            this.mConnectedDevices.put(makeDeviceListKey, new DeviceInfo(128, BtHelper.getName(bluetoothDevice), address, i3));
                            deviceInfo = this.mConnectedDevices.get(makeDeviceListKey);
                            item.set(MediaMetrics.Property.ADDRESS, address).set(MediaMetrics.Property.ENCODING, AudioSystem.audioFormatToString(i3)).set(MediaMetrics.Property.INDEX, Integer.valueOf(i2)).set(MediaMetrics.Property.NAME, deviceInfo.mDeviceName);
                            this.mApmConnectedDevices.replace(128, makeDeviceListKey);
                            if (i2 != -1) {
                                this.mDeviceBroker.getWrapper().setBluetoothDevice(bluetoothDevice);
                                int finalA2dpVolume = this.mAdiExt.getFinalA2dpVolume(i2);
                                this.mDeviceBroker.postSetVolumeIndexOnDevice(3, finalA2dpVolume * 10, 128, "onBluetoothA2dpDeviceConfigChange");
                                this.mAdiExt.postAbsoluteA2dpVolume(finalA2dpVolume);
                                i2 = finalA2dpVolume;
                            }
                        }
                    }
                }
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                item.set(MediaMetrics.Property.ADDRESS, address).set(MediaMetrics.Property.ENCODING, AudioSystem.audioFormatToString(i3)).set(MediaMetrics.Property.INDEX, Integer.valueOf(i2)).set(MediaMetrics.Property.NAME, deviceInfo.mDeviceName);
            }
            if (i == 0) {
                if (btDeviceInfo.mProfile == 2) {
                    if (z || deviceInfo.mDeviceCodecFormat == i3) {
                        z2 = false;
                    } else {
                        deviceInfo.mDeviceCodecFormat = i3;
                        this.mConnectedDevices.replace(makeDeviceListKey, deviceInfo);
                        z2 = true;
                    }
                    String str = "A2dpSuspended=0";
                    if (Build.isQcomPlatform()) {
                        str = AudioSystem.getParameters("A2dpSuspended");
                        Log.d(TAG, "onBluetoothDeviceConfigChange a2dpSuspended=" + str);
                    }
                    int handleDeviceConfigChange = this.mAudioSystem.handleDeviceConfigChange(btDeviceInfo.mAudioSystemDevice, address, BtHelper.getName(bluetoothDevice), i3);
                    if (this.mAdiExt.isMetaAudioSupport()) {
                        this.mDeviceBroker.getWrapper().checkBtDeviceForCarkitBt(false, true);
                    }
                    if (handleDeviceConfigChange != 0) {
                        AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("APM handleDeviceConfigChange failed for A2DP device addr=" + address + " codec=" + AudioSystem.audioFormatToString(i3)).printLog(TAG));
                        setBluetoothActiveDevice(new AudioDeviceBroker.BtDeviceInfo(btDeviceInfo, 0));
                    } else {
                        AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("APM handleDeviceConfigChange success for A2DP device addr=" + address + " codec=" + AudioSystem.audioFormatToString(i3)).printLog(TAG));
                    }
                    if (Build.isQcomPlatform() && ("A2dpSuspended=true".equals(str) || "A2dpSuspended=1".equals(str))) {
                        Log.d(TAG, "onBluetoothDeviceConfigChange a2dpSuspended set true");
                        AudioSystem.setParameters("A2dpSuspended=true");
                    }
                } else {
                    z2 = false;
                }
                if (!z2) {
                    updateBluetoothPreferredModes_l(bluetoothDevice);
                }
            }
            item.record();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onMakeA2dpDeviceUnavailableNow(String str, int i) {
        synchronized (this.mDevicesLock) {
            makeA2dpDeviceUnavailableNow(str, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onMakeLeAudioDeviceUnavailableNow(String str, int i) {
        synchronized (this.mDevicesLock) {
            makeLeAudioDeviceUnavailableNow(str, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onReportNewRoutes() {
        AudioRoutesInfo audioRoutesInfo;
        int beginBroadcast = this.mRoutesObservers.beginBroadcast();
        if (beginBroadcast > 0) {
            new MediaMetrics.Item("audio.device.onReportNewRoutes").set(MediaMetrics.Property.OBSERVERS, Integer.valueOf(beginBroadcast)).record();
            synchronized (this.mCurAudioRoutes) {
                audioRoutesInfo = new AudioRoutesInfo(this.mCurAudioRoutes);
            }
            while (beginBroadcast > 0) {
                beginBroadcast--;
                try {
                    this.mRoutesObservers.getBroadcastItem(beginBroadcast).dispatchAudioRoutesChanged(audioRoutesInfo);
                } catch (RemoteException unused) {
                }
            }
        }
        this.mRoutesObservers.finishBroadcast();
        this.mDeviceBroker.postObserveDevicesForAllStreams();
    }

    static {
        HashSet hashSet = new HashSet();
        DEVICE_OVERRIDE_A2DP_ROUTE_ON_PLUG_SET = hashSet;
        hashSet.add(4);
        hashSet.add(8);
        hashSet.add(131072);
        hashSet.addAll(AudioSystem.DEVICE_OUT_ALL_USB_SET);
        CAPTURE_PRESETS = new int[]{1, 5, 6, 7, 9, 10, 1999};
        HashSet hashSet2 = new HashSet();
        BECOMING_NOISY_INTENT_DEVICES_SET = hashSet2;
        hashSet2.add(4);
        hashSet2.add(8);
        hashSet2.add(1024);
        hashSet2.add(2048);
        hashSet2.add(131072);
        hashSet2.add(Integer.valueOf(AudioFormat.OPUS));
        hashSet2.add(Integer.valueOf(AudioFormat.APTX));
        hashSet2.add(536870914);
        hashSet2.addAll(AudioSystem.DEVICE_OUT_ALL_A2DP_SET);
        hashSet2.addAll(AudioSystem.DEVICE_OUT_ALL_USB_SET);
        hashSet2.addAll(AudioSystem.DEVICE_OUT_ALL_BLE_SET);
        hashSet2.add(Integer.valueOf(AudioDevice.OUT_IP));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSetWiredDeviceConnectionState(WiredDeviceConnectionState wiredDeviceConnectionState) {
        AudioDeviceInfo audioDeviceInfo;
        int internalType = wiredDeviceConnectionState.mAttributes.getInternalType();
        AudioService.sDeviceLogger.enqueue(new AudioServiceEvents.WiredDevConnectEvent(wiredDeviceConnectionState));
        MediaMetrics.Item item = new MediaMetrics.Item("audio.device.onSetWiredDeviceConnectionState").set(MediaMetrics.Property.ADDRESS, wiredDeviceConnectionState.mAttributes.getAddress()).set(MediaMetrics.Property.DEVICE, AudioSystem.getDeviceName(internalType)).set(MediaMetrics.Property.STATE, wiredDeviceConnectionState.mState == 0 ? "disconnected" : "connected");
        if (wiredDeviceConnectionState.mState == 0 && AudioSystem.DEVICE_OUT_ALL_USB_SET.contains(Integer.valueOf(wiredDeviceConnectionState.mAttributes.getInternalType()))) {
            AudioDeviceInfo[] devicesStatic = AudioManager.getDevicesStatic(2);
            int length = devicesStatic.length;
            for (int i = 0; i < length; i++) {
                audioDeviceInfo = devicesStatic[i];
                if (audioDeviceInfo.getInternalType() == wiredDeviceConnectionState.mAttributes.getInternalType()) {
                    break;
                }
            }
        }
        audioDeviceInfo = null;
        synchronized (this.mDevicesLock) {
            boolean z = true;
            if (wiredDeviceConnectionState.mState == 0 && DEVICE_OVERRIDE_A2DP_ROUTE_ON_PLUG_SET.contains(Integer.valueOf(internalType))) {
                this.mDeviceBroker.setBluetoothA2dpOnInt(true, false, "onSetWiredDeviceConnectionState state DISCONNECTED");
            }
            AudioDeviceAttributes audioDeviceAttributes = wiredDeviceConnectionState.mAttributes;
            if (wiredDeviceConnectionState.mState != 1) {
                z = false;
            }
            if (!handleDeviceConnection(audioDeviceAttributes, z, wiredDeviceConnectionState.mForTest, null)) {
                item.set(MediaMetrics.Property.EARLY_RETURN, "change of connection state failed").record();
                return;
            }
            if (wiredDeviceConnectionState.mState != 0) {
                if (DEVICE_OVERRIDE_A2DP_ROUTE_ON_PLUG_SET.contains(Integer.valueOf(internalType))) {
                    this.mDeviceBroker.setBluetoothA2dpOnInt(false, false, "onSetWiredDeviceConnectionState state not DISCONNECTED");
                }
                this.mDeviceBroker.checkMusicActive(internalType, wiredDeviceConnectionState.mCaller);
            }
            if (internalType == 1024) {
                this.mDeviceBroker.checkVolumeCecOnHdmiConnection(wiredDeviceConnectionState.mState, wiredDeviceConnectionState.mCaller);
            }
            if (wiredDeviceConnectionState.mState == 0 && AudioSystem.DEVICE_OUT_ALL_USB_SET.contains(Integer.valueOf(wiredDeviceConnectionState.mAttributes.getInternalType()))) {
                if (audioDeviceInfo != null) {
                    this.mDeviceBroker.dispatchPreferredMixerAttributesChangedCausedByDeviceRemoved(audioDeviceInfo);
                } else {
                    Log.e(TAG, "Didn't find AudioDeviceInfo to notify preferred mixer attributes change for type=" + wiredDeviceConnectionState.mAttributes.getType());
                }
            }
            sendDeviceConnectionIntent(internalType, wiredDeviceConnectionState.mState, wiredDeviceConnectionState.mAttributes.getAddress(), wiredDeviceConnectionState.mAttributes.getName());
            updateAudioRoutes(internalType, wiredDeviceConnectionState.mState);
            item.record();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onToggleHdmi() {
        MediaMetrics.Item item = new MediaMetrics.Item("audio.device.onToggleHdmi").set(MediaMetrics.Property.DEVICE, AudioSystem.getDeviceName(1024));
        synchronized (this.mDevicesLock) {
            if (this.mConnectedDevices.get(DeviceInfo.makeDeviceListKey(1024, "")) == null) {
                Log.e(TAG, "invalid null DeviceInfo in onToggleHdmi");
                item.set(MediaMetrics.Property.EARLY_RETURN, "invalid null DeviceInfo").record();
                return;
            }
            if (Build.isMtkPlatform()) {
                setWiredDeviceConnectionState(new AudioDeviceAttributes(1024, ""), 0, "android", false);
                setWiredDeviceConnectionState(new AudioDeviceAttributes(1024, ""), 1, "android", false);
            } else {
                setWiredDeviceConnectionState(new AudioDeviceAttributes(1024, ""), 0, "android");
                setWiredDeviceConnectionState(new AudioDeviceAttributes(1024, ""), 1, "android");
            }
            item.record();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSaveSetPreferredDevices(int i, List<AudioDeviceAttributes> list) {
        this.mPreferredDevices.put(Integer.valueOf(i), list);
        List<AudioDeviceAttributes> list2 = this.mNonDefaultDevices.get(Integer.valueOf(i));
        if (list2 != null) {
            list2.removeAll(list);
            if (list2.isEmpty()) {
                this.mNonDefaultDevices.remove(Integer.valueOf(i));
            } else {
                this.mNonDefaultDevices.put(Integer.valueOf(i), list2);
            }
            dispatchNonDefaultDevice(i, list2);
        }
        dispatchPreferredDevice(i, list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSaveRemovePreferredDevices(int i) {
        this.mPreferredDevices.remove(Integer.valueOf(i));
        dispatchPreferredDevice(i, new ArrayList());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSaveSetDeviceAsNonDefault(int i, AudioDeviceAttributes audioDeviceAttributes) {
        List<AudioDeviceAttributes> list = this.mNonDefaultDevices.get(Integer.valueOf(i));
        if (list == null) {
            list = new ArrayList<>();
        }
        if (!list.contains(audioDeviceAttributes)) {
            list.add(audioDeviceAttributes);
        }
        this.mNonDefaultDevices.put(Integer.valueOf(i), list);
        dispatchNonDefaultDevice(i, list);
        List<AudioDeviceAttributes> list2 = this.mPreferredDevices.get(Integer.valueOf(i));
        if (list2 != null) {
            list2.remove(audioDeviceAttributes);
            this.mPreferredDevices.put(Integer.valueOf(i), list2);
            dispatchPreferredDevice(i, list2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSaveRemoveDeviceAsNonDefault(int i, AudioDeviceAttributes audioDeviceAttributes) {
        List<AudioDeviceAttributes> list = this.mNonDefaultDevices.get(Integer.valueOf(i));
        if (list != null) {
            list.remove(audioDeviceAttributes);
            this.mNonDefaultDevices.put(Integer.valueOf(i), list);
            dispatchNonDefaultDevice(i, list);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSaveSetPreferredDevicesForCapturePreset(int i, List<AudioDeviceAttributes> list) {
        this.mPreferredDevicesForCapturePreset.put(Integer.valueOf(i), list);
        dispatchDevicesRoleForCapturePreset(i, 1, list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSaveClearPreferredDevicesForCapturePreset(int i) {
        this.mPreferredDevicesForCapturePreset.remove(Integer.valueOf(i));
        dispatchDevicesRoleForCapturePreset(i, 1, new ArrayList());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int setPreferredDevicesForStrategyAndSave(int i, List<AudioDeviceAttributes> list) {
        int preferredDevicesForStrategy = setPreferredDevicesForStrategy(i, list);
        if (preferredDevicesForStrategy == 0) {
            this.mDeviceBroker.postSaveSetPreferredDevicesForStrategy(i, list);
        }
        return preferredDevicesForStrategy;
    }

    int setPreferredDevicesForStrategy(int i, List<AudioDeviceAttributes> list) {
        SafeCloseable create = ClearCallingIdentityContext.create();
        try {
            AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("setPreferredDevicesForStrategy, strategy: " + i + " devices: " + list).printLog(TAG));
            int devicesRoleForStrategy = setDevicesRoleForStrategy(i, 1, list, false);
            if (create != null) {
                create.close();
            }
            return devicesRoleForStrategy;
        } catch (Throwable th) {
            if (create != null) {
                try {
                    create.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int setPreferredDevicesForStrategyInt(int i, List<AudioDeviceAttributes> list) {
        if (Build.isMtkPlatform()) {
            EventLogger eventLogger = AudioService.sDeviceLogger;
            eventLogger.enqueue(new EventLogger.StringEvent("setPreferredDevicesForStrategyInt, strategy: " + i + " devices: ").printLog(TAG));
            long uptimeMillis = SystemClock.uptimeMillis();
            int devicesRoleForStrategy = setDevicesRoleForStrategy(i, 1, list, true);
            if (devicesRoleForStrategy == 0) {
                eventLogger.enqueue(new EventLogger.StringEvent("setPreferredDevicesForStrategyInt, strategy: " + i + ", APM made devices: preferred device in" + (SystemClock.uptimeMillis() - uptimeMillis) + "ms").printLog(TAG));
            } else {
                eventLogger.enqueue(new EventLogger.StringEvent("setPreferredDevicesForStrategyInt, strategy: " + i + ", APM fail to set devices: ").printLog(TAG));
            }
            return devicesRoleForStrategy;
        }
        return setDevicesRoleForStrategy(i, 1, list, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int removePreferredDevicesForStrategyAndSave(int i) {
        int removePreferredDevicesForStrategy = removePreferredDevicesForStrategy(i);
        if (removePreferredDevicesForStrategy == 0) {
            this.mDeviceBroker.postSaveRemovePreferredDevicesForStrategy(i);
        }
        return removePreferredDevicesForStrategy;
    }

    int removePreferredDevicesForStrategy(int i) {
        SafeCloseable create = ClearCallingIdentityContext.create();
        try {
            AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("removePreferredDevicesForStrategy, strategy: " + i).printLog(TAG));
            int clearDevicesRoleForStrategy = clearDevicesRoleForStrategy(i, 1, false);
            if (create != null) {
                create.close();
            }
            return clearDevicesRoleForStrategy;
        } catch (Throwable th) {
            if (create != null) {
                try {
                    create.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int removePreferredDevicesForStrategyInt(int i) {
        if (Build.isMtkPlatform()) {
            EventLogger eventLogger = AudioService.sDeviceLogger;
            eventLogger.enqueue(new EventLogger.StringEvent("removePreferredDevicesForStrategyInt, strategy: " + i).printLog(TAG));
            long uptimeMillis = SystemClock.uptimeMillis();
            int clearDevicesRoleForStrategy = clearDevicesRoleForStrategy(i, 1, true);
            if (clearDevicesRoleForStrategy == 0) {
                eventLogger.enqueue(new EventLogger.StringEvent("removePreferredDevicesForStrategyInt APM removed, strategy: " + i + ", " + (SystemClock.uptimeMillis() - uptimeMillis) + "ms").printLog(TAG));
            } else {
                eventLogger.enqueue(new EventLogger.StringEvent("removePreferredDevicesForStrategyInt APM fail to removed, strategy: " + i + ", " + (SystemClock.uptimeMillis() - uptimeMillis) + "ms").printLog(TAG));
            }
            return clearDevicesRoleForStrategy;
        }
        return clearDevicesRoleForStrategy(i, 1, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int setDeviceAsNonDefaultForStrategyAndSave(int i, AudioDeviceAttributes audioDeviceAttributes) {
        SafeCloseable create = ClearCallingIdentityContext.create();
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(audioDeviceAttributes);
            AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("setDeviceAsNonDefaultForStrategyAndSave, strategy: " + i + " device: " + audioDeviceAttributes).printLog(TAG));
            int addDevicesRoleForStrategy = addDevicesRoleForStrategy(i, 2, arrayList, false);
            if (create != null) {
                create.close();
            }
            if (addDevicesRoleForStrategy == 0) {
                this.mDeviceBroker.postSaveSetDeviceAsNonDefaultForStrategy(i, audioDeviceAttributes);
            }
            return addDevicesRoleForStrategy;
        } catch (Throwable th) {
            if (create != null) {
                try {
                    create.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int removeDeviceAsNonDefaultForStrategyAndSave(int i, AudioDeviceAttributes audioDeviceAttributes) {
        SafeCloseable create = ClearCallingIdentityContext.create();
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(audioDeviceAttributes);
            AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("removeDeviceAsNonDefaultForStrategyAndSave, strategy: " + i + " devices: " + audioDeviceAttributes).printLog(TAG));
            int removeDevicesRoleForStrategy = removeDevicesRoleForStrategy(i, 2, arrayList, false);
            if (create != null) {
                create.close();
            }
            if (removeDevicesRoleForStrategy == 0) {
                this.mDeviceBroker.postSaveRemoveDeviceAsNonDefaultForStrategy(i, audioDeviceAttributes);
            }
            return removeDevicesRoleForStrategy;
        } catch (Throwable th) {
            if (create != null) {
                try {
                    create.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerStrategyPreferredDevicesDispatcher(IStrategyPreferredDevicesDispatcher iStrategyPreferredDevicesDispatcher) {
        this.mPrefDevDispatchers.register(iStrategyPreferredDevicesDispatcher);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterStrategyPreferredDevicesDispatcher(IStrategyPreferredDevicesDispatcher iStrategyPreferredDevicesDispatcher) {
        this.mPrefDevDispatchers.unregister(iStrategyPreferredDevicesDispatcher);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerStrategyNonDefaultDevicesDispatcher(IStrategyNonDefaultDevicesDispatcher iStrategyNonDefaultDevicesDispatcher) {
        this.mNonDefDevDispatchers.register(iStrategyNonDefaultDevicesDispatcher);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterStrategyNonDefaultDevicesDispatcher(IStrategyNonDefaultDevicesDispatcher iStrategyNonDefaultDevicesDispatcher) {
        this.mNonDefDevDispatchers.unregister(iStrategyNonDefaultDevicesDispatcher);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int setPreferredDevicesForCapturePresetAndSave(int i, List<AudioDeviceAttributes> list) {
        int preferredDevicesForCapturePreset = setPreferredDevicesForCapturePreset(i, list);
        if (preferredDevicesForCapturePreset == 0) {
            this.mDeviceBroker.postSaveSetPreferredDevicesForCapturePreset(i, list);
        }
        return preferredDevicesForCapturePreset;
    }

    private int setPreferredDevicesForCapturePreset(int i, List<AudioDeviceAttributes> list) {
        SafeCloseable create = ClearCallingIdentityContext.create();
        try {
            int devicesRoleForCapturePreset = setDevicesRoleForCapturePreset(i, 1, list);
            if (create != null) {
                create.close();
            }
            return devicesRoleForCapturePreset;
        } catch (Throwable th) {
            if (create != null) {
                try {
                    create.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int clearPreferredDevicesForCapturePresetAndSave(int i) {
        int clearPreferredDevicesForCapturePreset = clearPreferredDevicesForCapturePreset(i);
        if (clearPreferredDevicesForCapturePreset == 0) {
            this.mDeviceBroker.postSaveClearPreferredDevicesForCapturePreset(i);
        }
        return clearPreferredDevicesForCapturePreset;
    }

    private int clearPreferredDevicesForCapturePreset(int i) {
        SafeCloseable create = ClearCallingIdentityContext.create();
        try {
            int clearDevicesRoleForCapturePreset = clearDevicesRoleForCapturePreset(i, 1);
            if (create != null) {
                create.close();
            }
            return clearDevicesRoleForCapturePreset;
        } catch (Throwable th) {
            if (create != null) {
                try {
                    create.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private int addDevicesRoleForCapturePresetInt(int i, int i2, List<AudioDeviceAttributes> list) {
        return addDevicesRole(this.mAppliedPresetRolesInt, new AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda15
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i3, int i4, List list2) {
                int lambda$addDevicesRoleForCapturePresetInt$13;
                lambda$addDevicesRoleForCapturePresetInt$13 = AudioDeviceInventory.this.lambda$addDevicesRoleForCapturePresetInt$13(i3, i4, list2);
                return lambda$addDevicesRoleForCapturePresetInt$13;
            }
        }, i, i2, list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$addDevicesRoleForCapturePresetInt$13(int i, int i2, List list) {
        return this.mAudioSystem.addDevicesRoleForCapturePreset(i, i2, list);
    }

    private int removeDevicesRoleForCapturePresetInt(int i, int i2, List<AudioDeviceAttributes> list) {
        return removeDevicesRole(this.mAppliedPresetRolesInt, new AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda30
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i3, int i4, List list2) {
                int lambda$removeDevicesRoleForCapturePresetInt$14;
                lambda$removeDevicesRoleForCapturePresetInt$14 = AudioDeviceInventory.this.lambda$removeDevicesRoleForCapturePresetInt$14(i3, i4, list2);
                return lambda$removeDevicesRoleForCapturePresetInt$14;
            }
        }, i, i2, list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$removeDevicesRoleForCapturePresetInt$14(int i, int i2, List list) {
        return this.mAudioSystem.removeDevicesRoleForCapturePreset(i, i2, list);
    }

    private int setDevicesRoleForCapturePreset(int i, int i2, List<AudioDeviceAttributes> list) {
        return setDevicesRole(this.mAppliedPresetRoles, new AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda35
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i3, int i4, List list2) {
                int lambda$setDevicesRoleForCapturePreset$15;
                lambda$setDevicesRoleForCapturePreset$15 = AudioDeviceInventory.this.lambda$setDevicesRoleForCapturePreset$15(i3, i4, list2);
                return lambda$setDevicesRoleForCapturePreset$15;
            }
        }, new AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda36
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i3, int i4, List list2) {
                int lambda$setDevicesRoleForCapturePreset$16;
                lambda$setDevicesRoleForCapturePreset$16 = AudioDeviceInventory.this.lambda$setDevicesRoleForCapturePreset$16(i3, i4, list2);
                return lambda$setDevicesRoleForCapturePreset$16;
            }
        }, i, i2, list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$setDevicesRoleForCapturePreset$15(int i, int i2, List list) {
        return this.mAudioSystem.addDevicesRoleForCapturePreset(i, i2, list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$setDevicesRoleForCapturePreset$16(int i, int i2, List list) {
        return this.mAudioSystem.clearDevicesRoleForCapturePreset(i, i2);
    }

    private int clearDevicesRoleForCapturePreset(int i, int i2) {
        return clearDevicesRole(this.mAppliedPresetRoles, new AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda31
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i3, int i4, List list) {
                int lambda$clearDevicesRoleForCapturePreset$17;
                lambda$clearDevicesRoleForCapturePreset$17 = AudioDeviceInventory.this.lambda$clearDevicesRoleForCapturePreset$17(i3, i4, list);
                return lambda$clearDevicesRoleForCapturePreset$17;
            }
        }, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$clearDevicesRoleForCapturePreset$17(int i, int i2, List list) {
        return this.mAudioSystem.clearDevicesRoleForCapturePreset(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerCapturePresetDevicesRoleDispatcher(ICapturePresetDevicesRoleDispatcher iCapturePresetDevicesRoleDispatcher) {
        this.mDevRoleCapturePresetDispatchers.register(iCapturePresetDevicesRoleDispatcher);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterCapturePresetDevicesRoleDispatcher(ICapturePresetDevicesRoleDispatcher iCapturePresetDevicesRoleDispatcher) {
        this.mDevRoleCapturePresetDispatchers.unregister(iCapturePresetDevicesRoleDispatcher);
    }

    private int addDevicesRoleForStrategy(int i, int i2, List<AudioDeviceAttributes> list, boolean z) {
        return addDevicesRole(z ? this.mAppliedStrategyRolesInt : this.mAppliedStrategyRoles, new AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda0
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i3, int i4, List list2) {
                int lambda$addDevicesRoleForStrategy$18;
                lambda$addDevicesRoleForStrategy$18 = AudioDeviceInventory.this.lambda$addDevicesRoleForStrategy$18(i3, i4, list2);
                return lambda$addDevicesRoleForStrategy$18;
            }
        }, i, i2, list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$addDevicesRoleForStrategy$18(int i, int i2, List list) {
        return this.mAudioSystem.setDevicesRoleForStrategy(i, i2, list);
    }

    private int removeDevicesRoleForStrategy(int i, int i2, List<AudioDeviceAttributes> list, boolean z) {
        return removeDevicesRole(z ? this.mAppliedStrategyRolesInt : this.mAppliedStrategyRoles, new AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda20
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i3, int i4, List list2) {
                int lambda$removeDevicesRoleForStrategy$19;
                lambda$removeDevicesRoleForStrategy$19 = AudioDeviceInventory.this.lambda$removeDevicesRoleForStrategy$19(i3, i4, list2);
                return lambda$removeDevicesRoleForStrategy$19;
            }
        }, i, i2, list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$removeDevicesRoleForStrategy$19(int i, int i2, List list) {
        return this.mAudioSystem.removeDevicesRoleForStrategy(i, i2, list);
    }

    private int setDevicesRoleForStrategy(int i, int i2, List<AudioDeviceAttributes> list, boolean z) {
        return setDevicesRole(z ? this.mAppliedStrategyRolesInt : this.mAppliedStrategyRoles, new AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda17
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i3, int i4, List list2) {
                int lambda$setDevicesRoleForStrategy$20;
                lambda$setDevicesRoleForStrategy$20 = AudioDeviceInventory.this.lambda$setDevicesRoleForStrategy$20(i3, i4, list2);
                return lambda$setDevicesRoleForStrategy$20;
            }
        }, new AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda18
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i3, int i4, List list2) {
                int lambda$setDevicesRoleForStrategy$21;
                lambda$setDevicesRoleForStrategy$21 = AudioDeviceInventory.this.lambda$setDevicesRoleForStrategy$21(i3, i4, list2);
                return lambda$setDevicesRoleForStrategy$21;
            }
        }, i, i2, list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$setDevicesRoleForStrategy$20(int i, int i2, List list) {
        return this.mAudioSystem.setDevicesRoleForStrategy(i, i2, list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$setDevicesRoleForStrategy$21(int i, int i2, List list) {
        return this.mAudioSystem.clearDevicesRoleForStrategy(i, i2);
    }

    private int clearDevicesRoleForStrategy(int i, int i2, boolean z) {
        return clearDevicesRole(z ? this.mAppliedStrategyRolesInt : this.mAppliedStrategyRoles, new AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda16
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i3, int i4, List list) {
                int lambda$clearDevicesRoleForStrategy$22;
                lambda$clearDevicesRoleForStrategy$22 = AudioDeviceInventory.this.lambda$clearDevicesRoleForStrategy$22(i3, i4, list);
                return lambda$clearDevicesRoleForStrategy$22;
            }
        }, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$clearDevicesRoleForStrategy$22(int i, int i2, List list) {
        return this.mAudioSystem.clearDevicesRoleForStrategy(i, i2);
    }

    private int addDevicesRole(ArrayMap<Pair<Integer, Integer>, List<AudioDeviceAttributes>> arrayMap, AudioSystemInterface audioSystemInterface, int i, int i2, List<AudioDeviceAttributes> list) {
        synchronized (arrayMap) {
            Pair<Integer, Integer> pair = new Pair<>(Integer.valueOf(i), Integer.valueOf(i2));
            List<AudioDeviceAttributes> arrayList = new ArrayList<>();
            ArrayList arrayList2 = new ArrayList();
            if (arrayMap.containsKey(pair)) {
                arrayList = arrayMap.get(pair);
                for (AudioDeviceAttributes audioDeviceAttributes : list) {
                    if (!arrayList.contains(audioDeviceAttributes)) {
                        arrayList2.add(audioDeviceAttributes);
                    }
                }
            } else {
                arrayList2.addAll(list);
            }
            if (arrayList2.isEmpty()) {
                return 0;
            }
            int deviceRoleAction = audioSystemInterface.deviceRoleAction(i, i2, arrayList2);
            if (deviceRoleAction == 0) {
                arrayList.addAll(arrayList2);
                arrayMap.put(pair, arrayList);
            }
            return deviceRoleAction;
        }
    }

    private int removeDevicesRole(ArrayMap<Pair<Integer, Integer>, List<AudioDeviceAttributes>> arrayMap, AudioSystemInterface audioSystemInterface, int i, int i2, List<AudioDeviceAttributes> list) {
        synchronized (arrayMap) {
            Pair<Integer, Integer> pair = new Pair<>(Integer.valueOf(i), Integer.valueOf(i2));
            if (!arrayMap.containsKey(pair)) {
                return -2;
            }
            List<AudioDeviceAttributes> list2 = arrayMap.get(pair);
            ArrayList arrayList = new ArrayList();
            for (AudioDeviceAttributes audioDeviceAttributes : list) {
                if (list2.contains(audioDeviceAttributes)) {
                    arrayList.add(audioDeviceAttributes);
                }
            }
            if (arrayList.isEmpty()) {
                return 0;
            }
            int deviceRoleAction = audioSystemInterface.deviceRoleAction(i, i2, arrayList);
            if (deviceRoleAction == 0) {
                list2.removeAll(arrayList);
                if (list2.isEmpty()) {
                    arrayMap.remove(pair);
                } else {
                    arrayMap.put(pair, list2);
                }
            }
            return deviceRoleAction;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0041 A[Catch: all -> 0x0061, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x001f, B:7:0x002f, B:11:0x0041, B:12:0x004b, B:14:0x0051, B:17:0x0053, B:19:0x0059, B:20:0x005f, B:23:0x0048), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int setDevicesRole(ArrayMap<Pair<Integer, Integer>, List<AudioDeviceAttributes>> arrayMap, AudioSystemInterface audioSystemInterface, AudioSystemInterface audioSystemInterface2, int i, int i2, List<AudioDeviceAttributes> list) {
        boolean z;
        synchronized (arrayMap) {
            Pair<Integer, Integer> pair = new Pair<>(Integer.valueOf(i), Integer.valueOf(i2));
            List<AudioDeviceAttributes> arrayList = new ArrayList<>();
            ArrayList arrayList2 = new ArrayList();
            if (arrayMap.containsKey(pair)) {
                arrayList = arrayMap.get(pair);
                if (arrayList.size() == list.size()) {
                    arrayList.retainAll(list);
                    if (arrayList.size() == list.size()) {
                        z = true;
                        if (!z) {
                            arrayList.clear();
                            arrayList2.addAll(list);
                        }
                    }
                }
                z = false;
                if (!z) {
                }
            } else {
                arrayList2.addAll(list);
            }
            if (arrayList2.isEmpty()) {
                return 0;
            }
            int deviceRoleAction = audioSystemInterface.deviceRoleAction(i, i2, arrayList2);
            if (deviceRoleAction == 0) {
                arrayList.addAll(arrayList2);
                arrayMap.put(pair, arrayList);
            }
            return deviceRoleAction;
        }
    }

    private int clearDevicesRole(ArrayMap<Pair<Integer, Integer>, List<AudioDeviceAttributes>> arrayMap, AudioSystemInterface audioSystemInterface, int i, int i2) {
        synchronized (arrayMap) {
            Pair pair = new Pair(Integer.valueOf(i), Integer.valueOf(i2));
            if (!arrayMap.containsKey(pair)) {
                return -2;
            }
            int deviceRoleAction = audioSystemInterface.deviceRoleAction(i, i2, null);
            if (deviceRoleAction == 0) {
                arrayMap.remove(pair);
            }
            return deviceRoleAction;
        }
    }

    @GuardedBy({"mDevicesLock"})
    private void purgeDevicesRoles_l() {
        purgeRoles(this.mAppliedStrategyRolesInt, new AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda26
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i, int i2, List list) {
                int lambda$purgeDevicesRoles_l$23;
                lambda$purgeDevicesRoles_l$23 = AudioDeviceInventory.this.lambda$purgeDevicesRoles_l$23(i, i2, list);
                return lambda$purgeDevicesRoles_l$23;
            }
        });
        purgeRoles(this.mAppliedPresetRolesInt, new AudioSystemInterface() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda27
            @Override // com.android.server.audio.AudioDeviceInventory.AudioSystemInterface
            public final int deviceRoleAction(int i, int i2, List list) {
                int lambda$purgeDevicesRoles_l$24;
                lambda$purgeDevicesRoles_l$24 = AudioDeviceInventory.this.lambda$purgeDevicesRoles_l$24(i, i2, list);
                return lambda$purgeDevicesRoles_l$24;
            }
        });
        reapplyExternalDevicesRoles();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$purgeDevicesRoles_l$23(int i, int i2, List list) {
        return this.mAudioSystem.removeDevicesRoleForStrategy(i, i2, list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$purgeDevicesRoles_l$24(int i, int i2, List list) {
        return this.mAudioSystem.removeDevicesRoleForCapturePreset(i, i2, list);
    }

    @GuardedBy({"mDevicesLock"})
    private void purgeRoles(ArrayMap<Pair<Integer, Integer>, List<AudioDeviceAttributes>> arrayMap, AudioSystemInterface audioSystemInterface) {
        synchronized (arrayMap) {
            AudioDeviceInfo[] devicesStatic = AudioManager.getDevicesStatic(3);
            Iterator<Map.Entry<Pair<Integer, Integer>, List<AudioDeviceAttributes>>> it = arrayMap.entrySet().iterator();
            while (it.hasNext()) {
                Pair<Integer, Integer> key = it.next().getKey();
                Iterator<AudioDeviceAttributes> it2 = arrayMap.get(key).iterator();
                while (it2.hasNext()) {
                    final AudioDeviceAttributes next = it2.next();
                    if (((AudioDeviceInfo) Stream.of((Object[]) devicesStatic).filter(new Predicate() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda32
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            boolean lambda$purgeRoles$25;
                            lambda$purgeRoles$25 = AudioDeviceInventory.lambda$purgeRoles$25(next, (AudioDeviceInfo) obj);
                            return lambda$purgeRoles$25;
                        }
                    }).filter(new Predicate() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda33
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            boolean lambda$purgeRoles$26;
                            lambda$purgeRoles$26 = AudioDeviceInventory.lambda$purgeRoles$26(next, (AudioDeviceInfo) obj);
                            return lambda$purgeRoles$26;
                        }
                    }).findFirst().orElse(null)) == null) {
                        if (AudioService.DEBUG_DEVICES) {
                            Slog.i(TAG, "purgeRoles() removing device: " + next.toString() + ", for strategy: " + key.first + " and role: " + key.second);
                        }
                        audioSystemInterface.deviceRoleAction(((Integer) key.first).intValue(), ((Integer) key.second).intValue(), Arrays.asList(next));
                        it2.remove();
                    }
                }
                if (arrayMap.get(key).isEmpty()) {
                    it.remove();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$purgeRoles$25(AudioDeviceAttributes audioDeviceAttributes, AudioDeviceInfo audioDeviceInfo) {
        return audioDeviceInfo.getInternalType() == audioDeviceAttributes.getInternalType();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$purgeRoles$26(AudioDeviceAttributes audioDeviceAttributes, AudioDeviceInfo audioDeviceInfo) {
        return !AudioSystem.isBluetoothDevice(audioDeviceInfo.getInternalType()) || audioDeviceInfo.getAddress().equals(audioDeviceAttributes.getAddress());
    }

    public boolean isDeviceConnected(AudioDeviceAttributes audioDeviceAttributes) {
        boolean z;
        String makeDeviceListKey = DeviceInfo.makeDeviceListKey(audioDeviceAttributes.getInternalType(), audioDeviceAttributes.getAddress());
        synchronized (this.mDevicesLock) {
            z = this.mConnectedDevices.get(makeDeviceListKey) != null;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:52:0x02b3 A[Catch: all -> 0x031c, TryCatch #0 {, blocks: (B:14:0x00ca, B:16:0x00d2, B:17:0x00e8, B:20:0x00f7, B:22:0x00fb, B:25:0x011d, B:27:0x0142, B:28:0x016a, B:31:0x0186, B:35:0x01cc, B:37:0x01ee, B:38:0x01f3, B:40:0x0206, B:43:0x0218, B:44:0x021b, B:47:0x021d, B:49:0x0229, B:52:0x02b3, B:56:0x02bf, B:58:0x02c4, B:60:0x02c7, B:61:0x0305, B:63:0x030d, B:66:0x0317, B:68:0x031a, B:70:0x02d3, B:71:0x0198, B:73:0x01a2, B:76:0x023b, B:78:0x0245, B:79:0x026d, B:82:0x0278, B:85:0x0287, B:87:0x0295, B:88:0x0282), top: B:13:0x00ca }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x030d A[Catch: all -> 0x031c, TryCatch #0 {, blocks: (B:14:0x00ca, B:16:0x00d2, B:17:0x00e8, B:20:0x00f7, B:22:0x00fb, B:25:0x011d, B:27:0x0142, B:28:0x016a, B:31:0x0186, B:35:0x01cc, B:37:0x01ee, B:38:0x01f3, B:40:0x0206, B:43:0x0218, B:44:0x021b, B:47:0x021d, B:49:0x0229, B:52:0x02b3, B:56:0x02bf, B:58:0x02c4, B:60:0x02c7, B:61:0x0305, B:63:0x030d, B:66:0x0317, B:68:0x031a, B:70:0x02d3, B:71:0x0198, B:73:0x01a2, B:76:0x023b, B:78:0x0245, B:79:0x026d, B:82:0x0278, B:85:0x0287, B:87:0x0295, B:88:0x0282), top: B:13:0x00ca }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x02d3 A[Catch: all -> 0x031c, TryCatch #0 {, blocks: (B:14:0x00ca, B:16:0x00d2, B:17:0x00e8, B:20:0x00f7, B:22:0x00fb, B:25:0x011d, B:27:0x0142, B:28:0x016a, B:31:0x0186, B:35:0x01cc, B:37:0x01ee, B:38:0x01f3, B:40:0x0206, B:43:0x0218, B:44:0x021b, B:47:0x021d, B:49:0x0229, B:52:0x02b3, B:56:0x02bf, B:58:0x02c4, B:60:0x02c7, B:61:0x0305, B:63:0x030d, B:66:0x0317, B:68:0x031a, B:70:0x02d3, B:71:0x0198, B:73:0x01a2, B:76:0x023b, B:78:0x0245, B:79:0x026d, B:82:0x0278, B:85:0x0287, B:87:0x0295, B:88:0x0282), top: B:13:0x00ca }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean handleDeviceConnection(AudioDeviceAttributes audioDeviceAttributes, boolean z, boolean z2, BluetoothDevice bluetoothDevice) {
        boolean z3;
        int deviceConnectionState;
        String outputDeviceName;
        int internalType = audioDeviceAttributes.getInternalType();
        String address = audioDeviceAttributes.getAddress();
        String name = audioDeviceAttributes.getName();
        if (AudioService.DEBUG_DEVICES) {
            Slog.i(TAG, "handleDeviceConnection(" + z + " dev:" + Integer.toHexString(internalType) + " address:" + address + " name:" + name + ")");
            if ((internalType & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
                outputDeviceName = AudioSystem.getInputDeviceName(internalType);
            } else {
                outputDeviceName = AudioSystem.getOutputDeviceName(internalType);
            }
            Log.i(TAG, "handleDeviceConnection(" + z + " dev:" + Integer.toHexString(internalType) + "[" + outputDeviceName + "] address:" + address + " name:" + name + ")");
        }
        MediaMetrics.Item item = new MediaMetrics.Item("audio.device.handleDeviceConnection").set(MediaMetrics.Property.ADDRESS, address).set(MediaMetrics.Property.DEVICE, AudioSystem.getDeviceName(internalType)).set(MediaMetrics.Property.MODE, z ? "connect" : "disconnect").set(MediaMetrics.Property.NAME, name);
        synchronized (this.mDevicesLock) {
            String makeDeviceListKey = DeviceInfo.makeDeviceListKey(internalType, address);
            if (AudioService.DEBUG_DEVICES) {
                Slog.i(TAG, "deviceKey:" + makeDeviceListKey);
            }
            DeviceInfo deviceInfo = this.mConnectedDevices.get(makeDeviceListKey);
            boolean z4 = true;
            boolean z5 = deviceInfo != null;
            if (AudioService.DEBUG_DEVICES) {
                Slog.i(TAG, "deviceInfo:" + deviceInfo + " is(already)Connected:" + z5);
            }
            if (z && z5) {
                Log.e(TAG, "handleDeviceConnection device already connect ,disconnected frist dev:" + Integer.toHexString(internalType));
                int deviceConnectionState2 = this.mAudioSystem.setDeviceConnectionState(audioDeviceAttributes, 0, 0);
                if (AudioService.DEBUG_DEVICES) {
                    Log.i(TAG, "handleDeviceConnection(disconnected dev:" + Integer.toHexString(internalType) + ", res=" + deviceConnectionState2 + ")");
                }
                this.mConnectedDevices.remove(makeDeviceListKey);
                item.set(MediaMetrics.Property.EVENT, "device already connect");
                item.set(MediaMetrics.Property.STATE, "disconnected").record();
                z5 = false;
            }
            if (z && !z5) {
                boolean oplusHeadsetFadeInit = this.mDeviceBroker.getWrapper().getExtImpl().oplusHeadsetFadeInit(internalType);
                if (z2) {
                    deviceConnectionState = 0;
                } else {
                    deviceConnectionState = this.mAudioSystem.setDeviceConnectionState(audioDeviceAttributes, 1, 0);
                    if (AudioService.DEBUG_DEVICES) {
                        Log.i(TAG, "handleDeviceConnection(connected dev:" + Integer.toHexString(internalType) + ", res=" + deviceConnectionState + ")");
                    }
                }
                if (deviceConnectionState != 0) {
                    String str = "not connecting device 0x" + Integer.toHexString(internalType) + " due to command error " + deviceConnectionState;
                    if (AudioService.DEBUG_DEVICES) {
                        Slog.e(TAG, str);
                    }
                    item.set(MediaMetrics.Property.EARLY_RETURN, str).set(MediaMetrics.Property.STATE, "disconnected").record();
                    if (oplusHeadsetFadeInit) {
                        this.mDeviceBroker.getWrapper().getExtImpl().oplusHeadsetFadeSkipFadeIn(internalType);
                    }
                    if (internalType == -2147483640) {
                        disconnectSco();
                    }
                    return false;
                }
                this.mConnectedDevices.put(makeDeviceListKey, new DeviceInfo(internalType, name, address));
                if (oplusHeadsetFadeInit) {
                    this.mDeviceBroker.getWrapper().getExtImpl().oplusHeadsetFadeBeginFadeIn();
                }
            } else if (!z && z5) {
                int deviceConnectionState3 = this.mAudioSystem.setDeviceConnectionState(audioDeviceAttributes, 0, 0);
                if (AudioService.DEBUG_DEVICES) {
                    Log.i(TAG, "handleDeviceConnection(disconnected dev:" + Integer.toHexString(internalType) + ", res=" + deviceConnectionState3 + ")");
                }
                this.mConnectedDevices.remove(makeDeviceListKey);
            } else {
                if (!z && !z5) {
                    if ((internalType & 16) == 16) {
                        internalType = 32;
                    } else if ((internalType & 32) == 32) {
                        internalType = 16;
                    }
                    String makeDeviceListKey2 = DeviceInfo.makeDeviceListKey(internalType, address);
                    if (this.mConnectedDevices.get(makeDeviceListKey2) != null) {
                        this.mAudioSystem.setDeviceConnectionState(new AudioDeviceAttributes(internalType, address, name), 0, 0);
                        this.mConnectedDevices.remove(makeDeviceListKey2);
                        item.set(MediaMetrics.Property.STATE, "connected").record();
                    }
                }
                z3 = false;
                if (!z3) {
                    if (AudioSystem.isBluetoothScoDevice(internalType)) {
                        updateBluetoothPreferredModes_l(z ? bluetoothDevice : null);
                        if (!z) {
                            purgeDevicesRoles_l();
                        }
                    }
                    item.set(MediaMetrics.Property.STATE, "connected").record();
                } else {
                    Log.w(TAG, "handleDeviceConnection() failed, deviceKey=" + makeDeviceListKey + ", deviceSpec=" + deviceInfo + ", connect=" + z);
                    item.set(MediaMetrics.Property.STATE, "disconnected").record();
                }
                if (this.mAdiExt.isMetaAudioSupport()) {
                    IAudioDeviceBrokerWrapper wrapper = this.mDeviceBroker.getWrapper();
                    if (bluetoothDevice != null) {
                        z4 = false;
                    }
                    wrapper.checkBtDeviceForCarkitBt(z4, z);
                }
                return z3;
            }
            z3 = true;
            if (!z3) {
            }
            if (this.mAdiExt.isMetaAudioSupport()) {
            }
            return z3;
        }
    }

    private void disconnectA2dp() {
        synchronized (this.mDevicesLock) {
            final ArraySet arraySet = new ArraySet();
            this.mConnectedDevices.values().forEach(new Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda24
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AudioDeviceInventory.lambda$disconnectA2dp$27(arraySet, (AudioDeviceInventory.DeviceInfo) obj);
                }
            });
            new MediaMetrics.Item("audio.device.disconnectA2dp").record();
            if (arraySet.size() > 0) {
                final int checkSendBecomingNoisyIntentInt = checkSendBecomingNoisyIntentInt(128, 0, 0);
                arraySet.stream().forEach(new Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda25
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        AudioDeviceInventory.this.lambda$disconnectA2dp$28(checkSendBecomingNoisyIntentInt, (String) obj);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$disconnectA2dp$27(ArraySet arraySet, DeviceInfo deviceInfo) {
        if (deviceInfo.mDeviceType == 128) {
            arraySet.add(deviceInfo.mDeviceAddress);
        }
    }

    private void disconnectA2dpSink() {
        synchronized (this.mDevicesLock) {
            final ArraySet arraySet = new ArraySet();
            this.mConnectedDevices.values().forEach(new Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda13
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AudioDeviceInventory.lambda$disconnectA2dpSink$29(arraySet, (AudioDeviceInventory.DeviceInfo) obj);
                }
            });
            new MediaMetrics.Item("audio.device.disconnectA2dpSink").record();
            arraySet.stream().forEach(new Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda14
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AudioDeviceInventory.this.lambda$disconnectA2dpSink$30((String) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$disconnectA2dpSink$29(ArraySet arraySet, DeviceInfo deviceInfo) {
        if (deviceInfo.mDeviceType == -2147352576) {
            arraySet.add(deviceInfo.mDeviceAddress);
        }
    }

    private void disconnectHearingAid() {
        synchronized (this.mDevicesLock) {
            final ArraySet arraySet = new ArraySet();
            this.mConnectedDevices.values().forEach(new Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda28
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AudioDeviceInventory.lambda$disconnectHearingAid$31(arraySet, (AudioDeviceInventory.DeviceInfo) obj);
                }
            });
            new MediaMetrics.Item("audio.device.disconnectHearingAid").record();
            if (arraySet.size() > 0) {
                checkSendBecomingNoisyIntentInt(AudioFormat.OPUS, 0, 0);
                arraySet.stream().forEach(new Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda29
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        AudioDeviceInventory.this.lambda$disconnectHearingAid$32((String) obj);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$disconnectHearingAid$31(ArraySet arraySet, DeviceInfo deviceInfo) {
        if (deviceInfo.mDeviceType == 134217728) {
            arraySet.add(deviceInfo.mDeviceAddress);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onBtProfileDisconnected(int i) {
        if (i == 2) {
            disconnectA2dp();
        } else if (i == 11) {
            disconnectA2dpSink();
        } else if (i == 26) {
            disconnectLeAudioBroadcast();
        } else if (i == 21) {
            disconnectHearingAid();
        } else if (i == 22) {
            disconnectLeAudioUnicast();
        } else {
            Log.e(TAG, "onBtProfileDisconnected: Not a valid profile to disconnect " + BluetoothProfile.getProfileName(i));
        }
    }

    void disconnectLeAudio(final int i) {
        if (i != 536870912 && i != -1610612736 && i != 536870913 && i != 536870914) {
            Log.e(TAG, "disconnectLeAudio: Can't disconnect not LE Audio device " + i);
            return;
        }
        synchronized (this.mDevicesLock) {
            final ArraySet arraySet = new ArraySet();
            this.mConnectedDevices.values().forEach(new Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AudioDeviceInventory.lambda$disconnectLeAudio$33(i, arraySet, (AudioDeviceInventory.DeviceInfo) obj);
                }
            });
            new MediaMetrics.Item("audio.device.disconnectLeAudio").record();
            if (arraySet.size() > 0) {
                final int checkSendBecomingNoisyIntentInt = i != -1610612736 ? checkSendBecomingNoisyIntentInt(i, 0, 0) : 0;
                arraySet.stream().forEach(new Consumer() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        AudioDeviceInventory.this.lambda$disconnectLeAudio$34(i, checkSendBecomingNoisyIntentInt, (String) obj);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$disconnectLeAudio$33(int i, ArraySet arraySet, DeviceInfo deviceInfo) {
        if (deviceInfo.mDeviceType == i) {
            arraySet.add(deviceInfo.mDeviceAddress);
        }
    }

    void disconnectLeAudioUnicast() {
        disconnectLeAudio(AudioFormat.APTX);
        disconnectLeAudio(-1610612736);
        disconnectLeAudio(536870913);
    }

    void disconnectLeAudioBroadcast() {
        disconnectLeAudio(536870914);
    }

    int checkSendBecomingNoisyIntent(int i, int i2, int i3) {
        int checkSendBecomingNoisyIntentInt;
        synchronized (this.mDevicesLock) {
            checkSendBecomingNoisyIntentInt = checkSendBecomingNoisyIntentInt(i, i2, i3);
        }
        return checkSendBecomingNoisyIntentInt;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver iAudioRoutesObserver) {
        AudioRoutesInfo audioRoutesInfo;
        synchronized (this.mCurAudioRoutes) {
            audioRoutesInfo = new AudioRoutesInfo(this.mCurAudioRoutes);
            this.mRoutesObservers.register(iAudioRoutesObserver);
        }
        return audioRoutesInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioRoutesInfo getCurAudioRoutes() {
        return this.mCurAudioRoutes;
    }

    @GuardedBy({"AudioDeviceBroker.mDeviceStateLock"})
    @VisibleForTesting
    public int setBluetoothActiveDevice(AudioDeviceBroker.BtDeviceInfo btDeviceInfo) {
        int i;
        int i2;
        synchronized (this.mDevicesLock) {
            if (btDeviceInfo.mSupprNoisy || !((((i2 = btDeviceInfo.mProfile) == 22 || i2 == 26) && btDeviceInfo.mIsLeOutput) || i2 == 21 || i2 == 2)) {
                i = 0;
            } else {
                i = checkSendBecomingNoisyIntentInt(btDeviceInfo.mAudioSystemDevice, btDeviceInfo.mState == 2 ? 1 : 0, btDeviceInfo.mMusicDevice);
            }
            if (btDeviceInfo.mState == 2) {
                Log.d(TAG, "20H setBluetoothA2dpDeviceConnectionState checkMusicActive");
                this.mDeviceBroker.checkMusicActive(128, TAG);
            }
            if (AudioService.DEBUG_DEVICES) {
                Log.i(TAG, "setBluetoothActiveDevice device: " + btDeviceInfo.mDevice + " profile: " + BluetoothProfile.getProfileName(btDeviceInfo.mProfile) + " state: " + BluetoothProfile.getConnectionStateName(btDeviceInfo.mState) + " delay(ms): " + i + " codec:" + Integer.toHexString(btDeviceInfo.mCodec) + " suppressNoisyIntent: " + btDeviceInfo.mSupprNoisy);
            }
            this.mDeviceBroker.postBluetoothActiveDevice(btDeviceInfo, i);
            if (btDeviceInfo.mProfile == 21 && btDeviceInfo.mState == 2) {
                this.mDeviceBroker.setForceUse_Async(1, 0, "HEARING_AID set to CONNECTED");
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleBluetoothA2dpActiveDeviceChangeExt(BluetoothDevice bluetoothDevice, int i, int i2, boolean z, int i3) {
        this.mAdiExt.setActiveA2dpDeviceClass(bluetoothDevice, i);
        if (i == 0) {
            this.mDeviceBroker.queueOnBluetoothActiveDeviceChanged(new AudioDeviceBroker.BtDeviceChangedData(null, bluetoothDevice, new BluetoothProfileConnectionInfo(i2), "AudioDeviceInventory"));
            BtHelper.SetA2dpActiveDevice(null);
            return;
        }
        if (i == 2 && i2 == 11) {
            this.mDeviceBroker.queueOnBluetoothActiveDeviceChanged(new AudioDeviceBroker.BtDeviceChangedData(bluetoothDevice, null, new BluetoothProfileConnectionInfo(i2), "AudioDeviceInventory"));
            return;
        }
        synchronized (this.mConnectedDevices) {
            String address = bluetoothDevice.getAddress();
            int a2dpCodec = new BtHelper(this.mDeviceBroker).getA2dpCodec(bluetoothDevice);
            String makeDeviceListKey = DeviceInfo.makeDeviceListKey(128, address);
            DeviceInfo deviceInfo = this.mConnectedDevices.get(makeDeviceListKey);
            if (deviceInfo != null && a2dpCodec != deviceInfo.mDeviceCodecFormat) {
                this.mDeviceBroker.postBluetoothDeviceConfigChange(new AudioDeviceBroker.BtDeviceInfo(bluetoothDevice, i2));
                return;
            }
            for (Map.Entry<String, DeviceInfo> entry : this.mConnectedDevices.entrySet()) {
                if (entry.getValue().mDeviceType == 128) {
                    this.mConnectedDevices.remove(entry.getKey());
                    this.mConnectedDevices.put(makeDeviceListKey, new DeviceInfo(128, BtHelper.getName(bluetoothDevice), address, a2dpCodec));
                    if (BtHelper.isTwsPlusSwitch(bluetoothDevice, entry.getValue().mDeviceAddress)) {
                        BtHelper.SetA2dpActiveDevice(bluetoothDevice);
                        if (AudioService.DEBUG_DEVICES) {
                            Log.d(TAG, "TWS+ device switch");
                        }
                        return;
                    }
                    return;
                }
            }
            this.mDeviceBroker.queueOnBluetoothActiveDeviceChanged(new AudioDeviceBroker.BtDeviceChangedData(bluetoothDevice, null, new BluetoothProfileConnectionInfo(i2), "AudioDeviceInventory"));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int setWiredDeviceConnectionState(AudioDeviceAttributes audioDeviceAttributes, int i, String str) {
        int checkSendBecomingNoisyIntentInt;
        synchronized (this.mDevicesLock) {
            if (i == 2) {
                i = 0;
            }
            checkSendBecomingNoisyIntentInt = checkSendBecomingNoisyIntentInt(audioDeviceAttributes.getInternalType(), i, 0);
            this.mDeviceBroker.postSetWiredDeviceConnectionState(new WiredDeviceConnectionState(audioDeviceAttributes, i, str), checkSendBecomingNoisyIntentInt);
        }
        return checkSendBecomingNoisyIntentInt;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int setWiredDeviceConnectionState(AudioDeviceAttributes audioDeviceAttributes, int i, String str, boolean z) {
        int checkSendBecomingNoisyIntentInt;
        synchronized (this.mDevicesLock) {
            if (i == 2) {
                i = 0;
            }
            checkSendBecomingNoisyIntentInt = z ? 0 : checkSendBecomingNoisyIntentInt(audioDeviceAttributes.getInternalType(), i, 0);
            Log.i(TAG, "setWiredDeviceConnectionState(), " + z);
            this.mDeviceBroker.postSetWiredDeviceConnectionState(new WiredDeviceConnectionState(audioDeviceAttributes, i, str), checkSendBecomingNoisyIntentInt);
        }
        return checkSendBecomingNoisyIntentInt;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTestDeviceConnectionState(AudioDeviceAttributes audioDeviceAttributes, int i) {
        WiredDeviceConnectionState wiredDeviceConnectionState = new WiredDeviceConnectionState(audioDeviceAttributes, i, "com.android.server.audio");
        wiredDeviceConnectionState.mForTest = true;
        onSetWiredDeviceConnectionState(wiredDeviceConnectionState);
    }

    @GuardedBy({"mDevicesLock"})
    private void makeA2dpDeviceAvailable(AudioDeviceBroker.BtDeviceInfo btDeviceInfo, String str) {
        String[] strArr;
        String address = btDeviceInfo.mDevice.getAddress();
        String name = BtHelper.getName(btDeviceInfo.mDevice);
        int i = btDeviceInfo.mCodec;
        this.mDeviceBroker.setBluetoothA2dpOnInt(true, true, str);
        SystemClock.uptimeMillis();
        AudioDeviceAttributes audioDeviceAttributes = new AudioDeviceAttributes(128, address, name);
        int deviceConnectionState = this.mAudioSystem.setDeviceConnectionState(audioDeviceAttributes, 1, i);
        if (deviceConnectionState != 0) {
            AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("APM failed to make available A2DP device addr=" + address + " error=" + deviceConnectionState).printLog(TAG));
            if (deviceConnectionState != 100) {
                return;
            }
        } else {
            AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("A2DP device addr=" + address + " now available").printLog(TAG));
            this.mAudioSystem.setParameters(BIRECORD_DEVICE_NAME_PARAM + name);
        }
        if (Build.isMtkPlatform()) {
            String parameters = AudioSystem.getParameters("BTAudiosuspend");
            if (parameters != null) {
                if (AudioService.DEBUG_DEVICES) {
                    Log.i(TAG, "SCO status at BT end" + parameters);
                }
                strArr = parameters.split("=");
            } else {
                strArr = null;
            }
            if ((strArr != null && strArr.length >= 2 && (strArr[1].equals("true") || strArr[1].equals("1"))) || this.mDeviceBroker.isBluetoothScoActive()) {
                Log.i(TAG, "SCO is  on");
            } else {
                this.mAudioSystem.setParameters("A2dpsuspendonly=false");
            }
        }
        this.mDeviceBroker.clearA2dpSuspended(true);
        DeviceInfo deviceInfo = new DeviceInfo(128, name, address, i, UuidUtils.uuidFromAudioDeviceAttributes(audioDeviceAttributes));
        String key = deviceInfo.getKey();
        this.mConnectedDevices.put(key, deviceInfo);
        this.mApmConnectedDevices.put(128, key);
        this.mDeviceBroker.postAccessoryPlugMediaUnmute(128);
        setCurrentAudioRouteNameIfPossible(name, true);
        updateBluetoothPreferredModes_l(btDeviceInfo.mDevice);
        if (this.mAdiExt.isMetaAudioSupport()) {
            this.mDeviceBroker.getWrapper().checkBtDeviceForCarkitBt(false, true);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:47:0x0129, code lost:
    
        if (r2.isDuplexModeEnabled() != false) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0149, code lost:
    
        if (r2.isOutputOnlyModeEnabled() != false) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x015a, code lost:
    
        if (r2.isOutputOnlyModeEnabled() != false) goto L74;
     */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0202  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x020f  */
    @GuardedBy({"mDevicesLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void applyConnectedDevicesRoles_l() {
        boolean isDuplexModeEnabled;
        boolean isOutputOnlyModeEnabled;
        boolean z;
        if (this.mBluetoothDualModeEnabled) {
            DeviceInfo firstConnectedDeviceOfTypes = getFirstConnectedDeviceOfTypes(AudioSystem.DEVICE_OUT_ALL_BLE_SET);
            DeviceInfo firstConnectedDeviceOfTypes2 = getFirstConnectedDeviceOfTypes(AudioSystem.DEVICE_IN_ALL_BLE_SET);
            DeviceInfo firstConnectedDeviceOfTypes3 = getFirstConnectedDeviceOfTypes(AudioSystem.DEVICE_OUT_ALL_A2DP_SET);
            DeviceInfo firstConnectedDeviceOfTypes4 = getFirstConnectedDeviceOfTypes(AudioSystem.DEVICE_OUT_ALL_SCO_SET);
            DeviceInfo firstConnectedDeviceOfTypes5 = getFirstConnectedDeviceOfTypes(AudioSystem.DEVICE_IN_ALL_SCO_SET);
            boolean z2 = true;
            boolean z3 = firstConnectedDeviceOfTypes != null && firstConnectedDeviceOfTypes.isOutputOnlyModeEnabled();
            boolean z4 = (firstConnectedDeviceOfTypes != null && firstConnectedDeviceOfTypes.isDuplexModeEnabled()) || (firstConnectedDeviceOfTypes2 != null && firstConnectedDeviceOfTypes2.isDuplexModeEnabled());
            AudioDeviceBroker audioDeviceBroker = this.mDeviceBroker;
            AudioDeviceAttributes audioDeviceAttributes = null;
            if (audioDeviceBroker.mActiveCommunicationDevice != null && audioDeviceBroker.isInCommunication() && this.mDeviceBroker.mActiveCommunicationDevice != null) {
                audioDeviceAttributes = new AudioDeviceAttributes(this.mDeviceBroker.mActiveCommunicationDevice);
            }
            if (AudioService.DEBUG_DEVICES) {
                Log.i(TAG, "applyConnectedDevicesRoles_l\n - leOutDevice: " + firstConnectedDeviceOfTypes + "\n - leInDevice: " + firstConnectedDeviceOfTypes2 + "\n - a2dpDevice: " + firstConnectedDeviceOfTypes3 + "\n - scoOutDevice: " + firstConnectedDeviceOfTypes4 + "\n - scoInDevice: " + firstConnectedDeviceOfTypes5 + "\n - disableA2dp: " + z3 + ", disableSco: " + z4);
            }
            Iterator<DeviceInfo> it = this.mConnectedDevices.values().iterator();
            while (it.hasNext()) {
                DeviceInfo next = it.next();
                if (AudioSystem.isBluetoothDevice(next.mDeviceType)) {
                    AudioDeviceAttributes audioDeviceAttributes2 = new AudioDeviceAttributes(next.mDeviceType, next.mDeviceAddress, next.mDeviceName);
                    if (AudioService.DEBUG_DEVICES) {
                        Log.i(TAG, "  + checking Device: " + audioDeviceAttributes2);
                    }
                    if (!audioDeviceAttributes2.equalTypeAddress(audioDeviceAttributes)) {
                        if (AudioSystem.isBluetoothOutDevice(next.mDeviceType)) {
                            for (AudioProductStrategy audioProductStrategy : this.mStrategies) {
                                if (audioProductStrategy.getId() == this.mDeviceBroker.mCommunicationStrategyId) {
                                    if (AudioSystem.isBluetoothScoDevice(next.mDeviceType)) {
                                        if (!z4) {
                                        }
                                        z = z2;
                                    } else if (AudioSystem.isBluetoothLeDevice(next.mDeviceType)) {
                                        isOutputOnlyModeEnabled = next.isDuplexModeEnabled();
                                        z = isOutputOnlyModeEnabled ^ z2;
                                    }
                                    z = false;
                                } else if (AudioSystem.isBluetoothA2dpOutDevice(next.mDeviceType)) {
                                    if (!z3) {
                                    }
                                    z = z2;
                                } else {
                                    if (AudioSystem.isBluetoothScoDevice(next.mDeviceType)) {
                                        if (!z4) {
                                        }
                                        z = z2;
                                    } else if (AudioSystem.isBluetoothLeDevice(next.mDeviceType)) {
                                        isOutputOnlyModeEnabled = next.isOutputOnlyModeEnabled();
                                        z = isOutputOnlyModeEnabled ^ z2;
                                    }
                                    z = false;
                                }
                                if (AudioService.DEBUG_DEVICES) {
                                    Log.i(TAG, "     - strategy: " + audioProductStrategy.getId() + ", disable: " + z);
                                }
                                if (z) {
                                    addDevicesRoleForStrategy(audioProductStrategy.getId(), 2, Arrays.asList(audioDeviceAttributes2), z2);
                                } else {
                                    removeDevicesRoleForStrategy(audioProductStrategy.getId(), 2, Arrays.asList(audioDeviceAttributes2), z2);
                                }
                            }
                        }
                        if (AudioSystem.isBluetoothInDevice(next.mDeviceType)) {
                            int[] iArr = CAPTURE_PRESETS;
                            int length = iArr.length;
                            int i = 0;
                            while (i < length) {
                                int i2 = iArr[i];
                                if (AudioSystem.isBluetoothScoDevice(next.mDeviceType)) {
                                    if (z4 || !next.isDuplexModeEnabled()) {
                                        isDuplexModeEnabled = z2;
                                        if (AudioService.DEBUG_DEVICES) {
                                            Log.i(TAG, "      - capturePreset: " + i2 + ", disable: " + isDuplexModeEnabled);
                                        }
                                        if (!isDuplexModeEnabled) {
                                            addDevicesRoleForCapturePresetInt(i2, 2, Arrays.asList(audioDeviceAttributes2));
                                        } else {
                                            removeDevicesRoleForCapturePresetInt(i2, 2, Arrays.asList(audioDeviceAttributes2));
                                        }
                                        i++;
                                        z2 = true;
                                    }
                                    isDuplexModeEnabled = false;
                                    if (AudioService.DEBUG_DEVICES) {
                                    }
                                    if (!isDuplexModeEnabled) {
                                    }
                                    i++;
                                    z2 = true;
                                } else {
                                    if (AudioSystem.isBluetoothLeDevice(next.mDeviceType)) {
                                        isDuplexModeEnabled = next.isDuplexModeEnabled() ^ z2;
                                        if (AudioService.DEBUG_DEVICES) {
                                        }
                                        if (!isDuplexModeEnabled) {
                                        }
                                        i++;
                                        z2 = true;
                                    }
                                    isDuplexModeEnabled = false;
                                    if (AudioService.DEBUG_DEVICES) {
                                    }
                                    if (!isDuplexModeEnabled) {
                                    }
                                    i++;
                                    z2 = true;
                                }
                            }
                        }
                        z2 = true;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyConnectedDevicesRoles() {
        synchronized (this.mDevicesLock) {
            applyConnectedDevicesRoles_l();
        }
    }

    @GuardedBy({"mDevicesLock"})
    int checkProfileIsConnected(int i) {
        if (i == 1) {
            if (getFirstConnectedDeviceOfTypes(AudioSystem.DEVICE_OUT_ALL_SCO_SET) == null && getFirstConnectedDeviceOfTypes(AudioSystem.DEVICE_IN_ALL_SCO_SET) == null) {
                return 0;
            }
            return i;
        }
        if (i == 2) {
            if (getFirstConnectedDeviceOfTypes(AudioSystem.DEVICE_OUT_ALL_A2DP_SET) != null) {
                return i;
            }
            return 0;
        }
        if (i != 22 && i != 26) {
            return 0;
        }
        if (getFirstConnectedDeviceOfTypes(AudioSystem.DEVICE_OUT_ALL_BLE_SET) == null && getFirstConnectedDeviceOfTypes(AudioSystem.DEVICE_IN_ALL_BLE_SET) == null) {
            return 0;
        }
        return i;
    }

    @GuardedBy({"mDevicesLock"})
    private void updateBluetoothPreferredModes_l(BluetoothDevice bluetoothDevice) {
        int profileFromType;
        if (this.mBluetoothDualModeEnabled) {
            HashSet hashSet = new HashSet(0);
            for (DeviceInfo deviceInfo : this.mConnectedDevices.values()) {
                if (AudioSystem.isBluetoothDevice(deviceInfo.mDeviceType) && !hashSet.contains(deviceInfo.mDeviceAddress)) {
                    Bundle preferredAudioProfiles = BtHelper.getPreferredAudioProfiles(deviceInfo.mDeviceAddress);
                    if (AudioService.DEBUG_DEVICES) {
                        Log.i(TAG, "updateBluetoothPreferredModes_l processing device address: " + deviceInfo.mDeviceAddress + ", preferredProfiles: " + preferredAudioProfiles);
                    }
                    for (DeviceInfo deviceInfo2 : this.mConnectedDevices.values()) {
                        if (AudioSystem.isBluetoothDevice(deviceInfo2.mDeviceType) && deviceInfo.mDeviceAddress.equals(deviceInfo2.mDeviceAddress) && (profileFromType = BtHelper.getProfileFromType(deviceInfo2.mDeviceType)) != 0) {
                            int checkProfileIsConnected = checkProfileIsConnected(preferredAudioProfiles.getInt("audio_mode_duplex"));
                            if (checkProfileIsConnected == profileFromType || checkProfileIsConnected == 0) {
                                deviceInfo2.setModeEnabled("audio_mode_duplex");
                            } else {
                                deviceInfo2.setModeDisabled("audio_mode_duplex");
                            }
                            int checkProfileIsConnected2 = checkProfileIsConnected(preferredAudioProfiles.getInt("audio_mode_output_only"));
                            if (checkProfileIsConnected2 == profileFromType || checkProfileIsConnected2 == 0) {
                                deviceInfo2.setModeEnabled("audio_mode_output_only");
                            } else {
                                deviceInfo2.setModeDisabled("audio_mode_output_only");
                            }
                        }
                    }
                    hashSet.add(deviceInfo.mDeviceAddress);
                }
            }
            applyConnectedDevicesRoles_l();
            if (bluetoothDevice != null) {
                this.mDeviceBroker.postNotifyPreferredAudioProfileApplied(bluetoothDevice);
            }
        }
    }

    @GuardedBy({"mDevicesLock"})
    private void makeA2dpDeviceUnavailableNow(String str, int i) {
        MediaMetrics.Item item = new MediaMetrics.Item("audio.device.a2dp." + str).set(MediaMetrics.Property.ENCODING, AudioSystem.audioFormatToString(i)).set(MediaMetrics.Property.EVENT, "makeA2dpDeviceUnavailableNow");
        if (str == null) {
            item.set(MediaMetrics.Property.EARLY_RETURN, "address null").record();
            return;
        }
        String makeDeviceListKey = DeviceInfo.makeDeviceListKey(128, str);
        this.mConnectedDevices.remove(makeDeviceListKey);
        if (!makeDeviceListKey.equals(this.mApmConnectedDevices.get(128))) {
            AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("A2DP device " + str + " made unavailable, was not used").printLog(TAG));
            item.set(MediaMetrics.Property.EARLY_RETURN, "A2DP device made unavailable, was not used").record();
        }
        this.mDeviceBroker.getWrapper().getExtImpl().clearAvrcpAbsoluteVolumeSupportedwithAddr(str);
        SystemClock.uptimeMillis();
        int deviceConnectionState = this.mAudioSystem.setDeviceConnectionState(new AudioDeviceAttributes(128, str), 0, i);
        if (deviceConnectionState != 0) {
            AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("APM failed to make unavailable A2DP device addr=" + str + " error=" + deviceConnectionState).printLog(TAG));
        } else {
            AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("A2DP device addr=" + str + " made unavailable").printLog(TAG));
        }
        this.mAudioSystem.setParameters(EXIT_GAME_MODE_PARAM);
        this.mApmConnectedDevices.remove(128);
        setCurrentAudioRouteNameIfPossible(null, true);
        item.record();
        updateBluetoothPreferredModes_l(null);
        purgeDevicesRoles_l();
        if (this.mAdiExt.isMetaAudioSupport()) {
            this.mDeviceBroker.getWrapper().checkBtDeviceForCarkitBt(false, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mDevicesLock"})
    /* renamed from: makeA2dpDeviceUnavailableLater, reason: merged with bridge method [inline-methods] */
    public void lambda$disconnectA2dp$28(String str, int i) {
        if (Build.isMtkPlatform()) {
            this.mAudioSystem.setParameters("A2dpsuspendonly=true");
        } else if (!this.mAdiExt.isDhpResetting()) {
            this.mDeviceBroker.setA2dpSuspended(true, true, "makeA2dpDeviceUnavailableLater");
        }
        String makeDeviceListKey = DeviceInfo.makeDeviceListKey(128, str);
        DeviceInfo deviceInfo = this.mConnectedDevices.get(makeDeviceListKey);
        int i2 = deviceInfo != null ? deviceInfo.mDeviceCodecFormat : 0;
        this.mConnectedDevices.remove(makeDeviceListKey);
        this.mDeviceBroker.setA2dpTimeout(str, i2, i);
    }

    @GuardedBy({"mDevicesLock"})
    private void makeA2dpSrcAvailable(String str, int i) {
        this.mAudioSystem.setDeviceConnectionState(new AudioDeviceAttributes(AudioDevice.IN_BLUETOOTH_A2DP, str), 1, i);
        this.mConnectedDevices.put(DeviceInfo.makeDeviceListKey(AudioDevice.IN_BLUETOOTH_A2DP, str), new DeviceInfo(AudioDevice.IN_BLUETOOTH_A2DP, "", str, i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mDevicesLock"})
    /* renamed from: makeA2dpSrcUnavailable, reason: merged with bridge method [inline-methods] */
    public void lambda$disconnectA2dpSink$30(String str) {
        DeviceInfo deviceInfo = this.mConnectedDevices.get(DeviceInfo.makeDeviceListKey(AudioDevice.IN_BLUETOOTH_A2DP, str));
        this.mAudioSystem.setDeviceConnectionState(new AudioDeviceAttributes(AudioDevice.IN_BLUETOOTH_A2DP, str), 0, deviceInfo != null ? deviceInfo.mDeviceCodecFormat : 0);
        this.mConnectedDevices.remove(DeviceInfo.makeDeviceListKey(AudioDevice.IN_BLUETOOTH_A2DP, str));
    }

    @GuardedBy({"mDevicesLock"})
    private void makeHearingAidDeviceAvailable(String str, String str2, int i, String str3) {
        this.mDeviceBroker.postSetHearingAidVolumeIndex(this.mDeviceBroker.getVssVolumeForDevice(i, AudioFormat.OPUS), i);
        long uptimeMillis = SystemClock.uptimeMillis();
        int deviceConnectionState = this.mAudioSystem.setDeviceConnectionState(new AudioDeviceAttributes(AudioFormat.OPUS, str, str2), 1, 0);
        if (deviceConnectionState != 0) {
            AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("APM failed to make available hearing AID device addr=" + str + " error=" + deviceConnectionState).printLog(TAG));
        } else {
            AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("Hearing Aid device addr=" + str + " now available" + (SystemClock.uptimeMillis() - uptimeMillis) + "ms").printLog(TAG));
        }
        this.mConnectedDevices.put(DeviceInfo.makeDeviceListKey(AudioFormat.OPUS, str), new DeviceInfo(AudioFormat.OPUS, str2, str));
        this.mDeviceBroker.postAccessoryPlugMediaUnmute(AudioFormat.OPUS);
        this.mDeviceBroker.postApplyVolumeOnDevice(i, AudioFormat.OPUS, "makeHearingAidDeviceAvailable");
        setCurrentAudioRouteNameIfPossible(str2, false);
        MediaMetrics.Item item = new MediaMetrics.Item("audio.device.makeHearingAidDeviceAvailable");
        MediaMetrics.Key key = MediaMetrics.Property.ADDRESS;
        if (str == null) {
            str = "";
        }
        item.set(key, str).set(MediaMetrics.Property.DEVICE, AudioSystem.getDeviceName(AudioFormat.OPUS)).set(MediaMetrics.Property.NAME, str2).set(MediaMetrics.Property.STREAM_TYPE, AudioSystem.streamToString(i)).record();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mDevicesLock"})
    /* renamed from: makeHearingAidDeviceUnavailable, reason: merged with bridge method [inline-methods] */
    public void lambda$disconnectHearingAid$32(String str) {
        this.mAudioSystem.setDeviceConnectionState(new AudioDeviceAttributes(AudioFormat.OPUS, str), 0, 0);
        this.mConnectedDevices.remove(DeviceInfo.makeDeviceListKey(AudioFormat.OPUS, str));
        setCurrentAudioRouteNameIfPossible(null, false);
        MediaMetrics.Item item = new MediaMetrics.Item("audio.device.makeHearingAidDeviceUnavailable");
        MediaMetrics.Key key = MediaMetrics.Property.ADDRESS;
        if (str == null) {
            str = "";
        }
        item.set(key, str).set(MediaMetrics.Property.DEVICE, AudioSystem.getDeviceName(AudioFormat.OPUS)).record();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isHearingAidConnected() {
        return getFirstConnectedDeviceOfTypes(Sets.newHashSet(new Integer[]{Integer.valueOf(AudioFormat.OPUS)})) != null;
    }

    private DeviceInfo getFirstConnectedDeviceOfTypes(Set<Integer> set) {
        List<DeviceInfo> connectedDevicesOfTypes = getConnectedDevicesOfTypes(set);
        if (connectedDevicesOfTypes.isEmpty()) {
            return null;
        }
        return connectedDevicesOfTypes.get(0);
    }

    private List<DeviceInfo> getConnectedDevicesOfTypes(Set<Integer> set) {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mDevicesLock) {
            for (DeviceInfo deviceInfo : this.mConnectedDevices.values()) {
                if (set.contains(Integer.valueOf(deviceInfo.mDeviceType))) {
                    arrayList.add(deviceInfo);
                }
            }
        }
        return arrayList;
    }

    AudioDeviceAttributes getDeviceOfType(int i) {
        DeviceInfo firstConnectedDeviceOfTypes = getFirstConnectedDeviceOfTypes(Sets.newHashSet(new Integer[]{Integer.valueOf(i)}));
        if (firstConnectedDeviceOfTypes == null) {
            return null;
        }
        return new AudioDeviceAttributes(firstConnectedDeviceOfTypes.mDeviceType, firstConnectedDeviceOfTypes.mDeviceAddress, firstConnectedDeviceOfTypes.mDeviceName);
    }

    @GuardedBy({"mDevicesLock"})
    private void makeLeAudioDeviceAvailable(AudioDeviceBroker.BtDeviceInfo btDeviceInfo, int i, String str) {
        int i2;
        String address = btDeviceInfo.mDevice.getAddress();
        String name = BtHelper.getName(btDeviceInfo.mDevice);
        int i3 = btDeviceInfo.mVolume;
        int i4 = i3 == -1 ? -1 : i3 * 10;
        int i5 = btDeviceInfo.mAudioSystemDevice;
        if (i5 != 0) {
            this.mDeviceBroker.setBluetoothA2dpOnInt(true, false, str);
            AudioDeviceAttributes audioDeviceAttributes = new AudioDeviceAttributes(i5, address, name);
            int deviceConnectionState = AudioSystem.setDeviceConnectionState(audioDeviceAttributes, 1, 0);
            if (deviceConnectionState != 0) {
                AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("APM failed to make available LE Audio device addr=" + address + " error=" + deviceConnectionState).printLog(TAG));
            } else {
                AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("LE Audio device addr=" + address + " now available").printLog(TAG));
                if (Build.isMtkPlatform()) {
                    this.mDeviceBroker.postUpdateCommunicationRoute("makeLeAudioDeviceAvailable");
                }
            }
            this.mDeviceBroker.clearLeAudioSuspended(true);
            if (!Build.isMtkPlatform()) {
                this.mConnectedDevices.put(DeviceInfo.makeDeviceListKey(i5, address), new DeviceInfo(i5, name, address, 0, UuidUtils.uuidFromAudioDeviceAttributes(audioDeviceAttributes)));
            } else {
                this.mConnectedDevices.put(DeviceInfo.makeDeviceListKey(i5, address), new DeviceInfo(i5, name, address, 0, UuidUtils.uuidFromAudioDeviceAttributes(new AudioDeviceAttributes(AudioFormat.APTX, address))));
            }
            this.mDeviceBroker.postAccessoryPlugMediaUnmute(i5);
            setCurrentAudioRouteNameIfPossible(name, false);
            i2 = -1;
        } else {
            i2 = -1;
        }
        if (i == i2) {
            return;
        }
        if (!this.mDeviceBroker.getWrapper().getBluetoothVolSyncSupported()) {
            if (i4 == i2) {
                i4 = this.mDeviceBroker.getVssVolumeForDevice(i, i5);
            }
            this.mDeviceBroker.postSetLeAudioVolumeIndex(i4, this.mDeviceBroker.getMaxVssVolumeForStream(i), i);
            this.mDeviceBroker.postApplyVolumeOnDevice(i, i5, "makeLeAudioDeviceAvailable");
        }
        updateBluetoothPreferredModes_l(btDeviceInfo.mDevice);
    }

    @GuardedBy({"mDevicesLock"})
    private void makeLeAudioDeviceUnavailableNow(String str, int i) {
        if (i != 0) {
            int deviceConnectionState = AudioSystem.setDeviceConnectionState(new AudioDeviceAttributes(i, str), 0, 0);
            if (deviceConnectionState != 0) {
                AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("APM failed to make unavailable LE Audio device addr=" + str + " error=" + deviceConnectionState).printLog(TAG));
            } else {
                AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("LE Audio device addr=" + str + " made unavailable").printLog(TAG));
            }
            this.mAudioSystem.setParameters(EXIT_GAME_MODE_PARAM);
            this.mConnectedDevices.remove(DeviceInfo.makeDeviceListKey(i, str));
        }
        setCurrentAudioRouteNameIfPossible(null, false);
        updateBluetoothPreferredModes_l(null);
        purgeDevicesRoles_l();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mDevicesLock"})
    /* renamed from: makeLeAudioDeviceUnavailableLater, reason: merged with bridge method [inline-methods] */
    public void lambda$disconnectLeAudio$34(String str, int i, int i2) {
        this.mDeviceBroker.setLeAudioSuspended(true, true, "makeLeAudioDeviceUnavailableLater");
        this.mConnectedDevices.remove(DeviceInfo.makeDeviceListKey(i, str));
        this.mDeviceBroker.setLeAudioTimeout(str, i, i2);
    }

    @GuardedBy({"mDevicesLock"})
    private void setCurrentAudioRouteNameIfPossible(String str, boolean z) {
        synchronized (this.mCurAudioRoutes) {
            if (TextUtils.equals(this.mCurAudioRoutes.bluetoothName, str)) {
                return;
            }
            if (str != null || !isCurrentDeviceConnected()) {
                this.mCurAudioRoutes.bluetoothName = str;
                this.mDeviceBroker.postReportNewRoutes(z);
            }
        }
    }

    @GuardedBy({"mDevicesLock"})
    private boolean isCurrentDeviceConnected() {
        return this.mConnectedDevices.values().stream().anyMatch(new Predicate() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda19
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$isCurrentDeviceConnected$35;
                lambda$isCurrentDeviceConnected$35 = AudioDeviceInventory.this.lambda$isCurrentDeviceConnected$35((AudioDeviceInventory.DeviceInfo) obj);
                return lambda$isCurrentDeviceConnected$35;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$isCurrentDeviceConnected$35(DeviceInfo deviceInfo) {
        return TextUtils.equals(deviceInfo.mDeviceName, this.mCurAudioRoutes.bluetoothName);
    }

    @GuardedBy({"mDevicesLock"})
    private int checkSendBecomingNoisyIntentInt(int i, int i2, int i3) {
        MediaMetrics.Item item = new MediaMetrics.Item("audio.device.checkSendBecomingNoisyIntentInt").set(MediaMetrics.Property.DEVICE, AudioSystem.getDeviceName(i)).set(MediaMetrics.Property.STATE, i2 == 1 ? "connected" : "disconnected");
        int i4 = 0;
        if (i2 != 0) {
            Log.i(TAG, "not sending NOISY: state=" + i2);
            item.set(MediaMetrics.Property.DELAY_MS, 0).record();
            return 0;
        }
        Set<Integer> set = BECOMING_NOISY_INTENT_DEVICES_SET;
        if (!set.contains(Integer.valueOf(i))) {
            Log.i(TAG, "not sending NOISY: device=0x" + Integer.toHexString(i) + " not in set " + set);
            item.set(MediaMetrics.Property.DELAY_MS, 0).record();
            return 0;
        }
        HashSet hashSet = new HashSet();
        for (DeviceInfo deviceInfo : this.mConnectedDevices.values()) {
            int i5 = deviceInfo.mDeviceType;
            if ((Integer.MIN_VALUE & i5) == 0 && BECOMING_NOISY_INTENT_DEVICES_SET.contains(Integer.valueOf(i5))) {
                hashSet.add(Integer.valueOf(deviceInfo.mDeviceType));
                Log.i(TAG, "NOISY: adding 0x" + Integer.toHexString(deviceInfo.mDeviceType));
            }
        }
        if (i3 == 0) {
            i3 = this.mDeviceBroker.getDeviceForStream(3);
            Log.i(TAG, "NOISY: musicDevice changing from NONE to 0x" + Integer.toHexString(i3));
        }
        this.mAdiExt.setAudioDeviceDisconnect(i);
        boolean isInCommunication = this.mDeviceBroker.isInCommunication();
        boolean isSingleAudioDeviceType = AudioSystem.isSingleAudioDeviceType(hashSet, i);
        boolean hasMediaDynamicPolicy = this.mDeviceBroker.hasMediaDynamicPolicy();
        if ((i == i3 || isInCommunication || ((AudioSystem.DEVICE_OUT_ALL_A2DP_SET.contains(Integer.valueOf(i)) && AudioSystem.DEVICE_OUT_ALL_SCO_SET.contains(Integer.valueOf(i3))) || i == 536870912)) && isSingleAudioDeviceType && ((!hasMediaDynamicPolicy || (i == 8388608 && hasMediaDynamicPolicy)) && i3 != 32768)) {
            if (!this.mAudioSystem.isStreamActive(3, 0) && !this.mDeviceBroker.hasAudioFocusUsers()) {
                AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("dropping ACTION_AUDIO_BECOMING_NOISY").printLog(TAG));
                item.set(MediaMetrics.Property.DELAY_MS, 0).record();
                return 0;
            }
            this.mDeviceBroker.postBroadcastBecomingNoisy();
            i4 = SystemProperties.getInt("audio.sys.noisy.broadcast.delay", ProcessList.PREVIOUS_APP_ADJ);
        } else {
            Log.i(TAG, "not sending NOISY: device:0x" + Integer.toHexString(i) + " musicDevice:0x" + Integer.toHexString(i3) + " inComm:" + isInCommunication + " mediaPolicy:" + hasMediaDynamicPolicy + " singleDevice:" + isSingleAudioDeviceType);
        }
        item.set(MediaMetrics.Property.DELAY_MS, Integer.valueOf(i4)).record();
        return i4;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x00be, code lost:
    
        if (r17 != 262145) goto L34;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void sendDeviceConnectionIntent(int i, int i2, String str, String str2) {
        String outputDeviceName;
        if (AudioService.DEBUG_DEVICES) {
            Slog.i(TAG, "sendDeviceConnectionIntent(dev:0x" + Integer.toHexString(i) + " state:0x" + Integer.toHexString(i2) + " address:" + str + " name:" + str2 + ");");
            if ((i & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
                outputDeviceName = AudioSystem.getInputDeviceName(i);
            } else {
                outputDeviceName = AudioSystem.getOutputDeviceName(i);
            }
            Log.i(TAG, "sendDeviceConnectionIntent(dev:0x" + Integer.toHexString(i) + " state:0x" + Integer.toHexString(i2) + "[" + outputDeviceName + "] address:" + str + " name:" + str2 + ");");
        }
        Intent intent = new Intent();
        if (i != -2113929216) {
            if (i == 4) {
                intent.setAction("android.intent.action.HEADSET_PLUG");
                intent.putExtra("microphone", 1);
            } else {
                if (i != 8) {
                    if (i != 1024) {
                        if (i != 131072) {
                            if (i == 67108864) {
                                intent.setAction("android.intent.action.HEADSET_PLUG");
                                intent.putExtra("microphone", AudioSystem.getDeviceConnectionState(AudioDevice.IN_USB_HEADSET, "") == 1 ? 1 : 0);
                            } else if (i != 262144) {
                            }
                        }
                    }
                    configureHdmiPlugIntent(intent, i2);
                }
                intent.setAction("android.intent.action.HEADSET_PLUG");
                intent.putExtra("microphone", 0);
            }
        } else {
            if (AudioSystem.getDeviceConnectionState(67108864, "") != 1) {
                return;
            }
            intent.setAction("android.intent.action.HEADSET_PLUG");
            intent.putExtra("microphone", 1);
        }
        if (intent.getAction() == null) {
            if (AudioService.DEBUG_DEVICES) {
                Log.e(TAG, "Headset Plugged-out broadcast is not send.Action is null");
                return;
            }
            return;
        }
        if (i2 == 0 && intent.getAction() == "android.intent.action.HEADSET_PLUG" && (AudioSystem.getDeviceConnectionState(67108864, "") == 1 || AudioSystem.getDeviceConnectionState(8, "") == 1 || AudioSystem.getDeviceConnectionState(4, "") == 1)) {
            if (AudioService.DEBUG_DEVICES) {
                Log.e(TAG, "Headset Plugged-out broadcast is not send.Still headset is Plugged-in");
                return;
            }
            return;
        }
        intent.putExtra(CONNECT_INTENT_KEY_STATE, i2);
        intent.putExtra(CONNECT_INTENT_KEY_ADDRESS, str);
        intent.putExtra(CONNECT_INTENT_KEY_PORT_NAME, str2);
        intent.addFlags(1073741824);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mDeviceBroker.broadcastStickyIntentToCurrentProfileGroup(intent);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void updateAudioRoutes(int i, int i2) {
        int i3;
        if (i != 4) {
            i3 = 8;
            if (i != 8) {
                if (i != 1024) {
                    if (i != 16384) {
                        if (i != 131072) {
                            if (i != 67108864) {
                                if (i != 262144 && i != 262145) {
                                    i3 = 0;
                                }
                            }
                        }
                    }
                    i3 = 16;
                }
            }
            i3 = 2;
        } else {
            i3 = 1;
        }
        synchronized (this.mCurAudioRoutes) {
            if (i3 == 0) {
                return;
            }
            AudioRoutesInfo audioRoutesInfo = this.mCurAudioRoutes;
            int i4 = audioRoutesInfo.mainType;
            int i5 = i2 != 0 ? i4 | i3 : (~i3) & i4;
            if (i5 != i4) {
                audioRoutesInfo.mainType = i5;
                this.mDeviceBroker.postReportNewRoutes(false);
            }
        }
    }

    private void configureHdmiPlugIntent(Intent intent, int i) {
        intent.setAction("android.media.action.HDMI_AUDIO_PLUG");
        intent.putExtra("android.media.extra.AUDIO_PLUG_STATE", i);
        if (i != 1) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        int listAudioPorts = AudioSystem.listAudioPorts(arrayList, new int[1]);
        if (listAudioPorts != 0) {
            Log.e(TAG, "listAudioPorts error " + listAudioPorts + " in configureHdmiPlugIntent");
            return;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            AudioDevicePort audioDevicePort = (AudioPort) it.next();
            if (audioDevicePort instanceof AudioDevicePort) {
                AudioDevicePort audioDevicePort2 = audioDevicePort;
                if (audioDevicePort2.type() == 1024 || audioDevicePort2.type() == 262144 || audioDevicePort2.type() == 262145) {
                    int[] filterPublicFormats = android.media.AudioFormat.filterPublicFormats(audioDevicePort2.formats());
                    if (filterPublicFormats.length > 0) {
                        ArrayList arrayList2 = new ArrayList(1);
                        for (int i2 : filterPublicFormats) {
                            if (i2 != 0) {
                                arrayList2.add(Integer.valueOf(i2));
                            }
                        }
                        intent.putExtra("android.media.extra.ENCODINGS", arrayList2.stream().mapToInt(new ToIntFunction() { // from class: com.android.server.audio.AudioDeviceInventory$$ExternalSyntheticLambda34
                            @Override // java.util.function.ToIntFunction
                            public final int applyAsInt(Object obj) {
                                int intValue;
                                intValue = ((Integer) obj).intValue();
                                return intValue;
                            }
                        }).toArray());
                    }
                    int i3 = 0;
                    for (int i4 : audioDevicePort2.channelMasks()) {
                        int channelCountFromOutChannelMask = android.media.AudioFormat.channelCountFromOutChannelMask(i4);
                        if (channelCountFromOutChannelMask > i3) {
                            i3 = channelCountFromOutChannelMask;
                        }
                    }
                    intent.putExtra("android.media.extra.MAX_CHANNEL_COUNT", i3);
                }
            }
        }
    }

    private void dispatchPreferredDevice(int i, List<AudioDeviceAttributes> list) {
        int beginBroadcast = this.mPrefDevDispatchers.beginBroadcast();
        for (int i2 = 0; i2 < beginBroadcast; i2++) {
            try {
                this.mPrefDevDispatchers.getBroadcastItem(i2).dispatchPrefDevicesChanged(i, list);
            } catch (RemoteException unused) {
            }
        }
        this.mPrefDevDispatchers.finishBroadcast();
    }

    private void dispatchNonDefaultDevice(int i, List<AudioDeviceAttributes> list) {
        int beginBroadcast = this.mNonDefDevDispatchers.beginBroadcast();
        for (int i2 = 0; i2 < beginBroadcast; i2++) {
            try {
                this.mNonDefDevDispatchers.getBroadcastItem(i2).dispatchNonDefDevicesChanged(i, list);
            } catch (RemoteException unused) {
            }
        }
        this.mNonDefDevDispatchers.finishBroadcast();
    }

    private void dispatchDevicesRoleForCapturePreset(int i, int i2, List<AudioDeviceAttributes> list) {
        int beginBroadcast = this.mDevRoleCapturePresetDispatchers.beginBroadcast();
        for (int i3 = 0; i3 < beginBroadcast; i3++) {
            try {
                this.mDevRoleCapturePresetDispatchers.getBroadcastItem(i3).dispatchDevicesRoleChanged(i, i2, list);
            } catch (RemoteException unused) {
            }
        }
        this.mDevRoleCapturePresetDispatchers.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UUID getDeviceSensorUuid(AudioDeviceAttributes audioDeviceAttributes) {
        String makeDeviceListKey = DeviceInfo.makeDeviceListKey(audioDeviceAttributes.getInternalType(), audioDeviceAttributes.getAddress());
        if (AudioService.DEBUG_DEVICES) {
            Log.d(TAG, "getDeviceSensorUuid, key = " + makeDeviceListKey);
        }
        synchronized (this.mDevicesLock) {
            DeviceInfo deviceInfo = this.mConnectedDevices.get(makeDeviceListKey);
            if (deviceInfo == null) {
                return null;
            }
            return deviceInfo.mSensorUuid;
        }
    }

    @VisibleForTesting
    public boolean isA2dpDeviceConnected(BluetoothDevice bluetoothDevice) {
        Iterator<DeviceInfo> it = getConnectedDevicesOfTypes(Sets.newHashSet(new Integer[]{128})).iterator();
        while (it.hasNext()) {
            if (it.next().mDeviceAddress.equals(bluetoothDevice.getAddress())) {
                return true;
            }
        }
        return false;
    }

    public boolean isLeConnected() {
        Iterator<DeviceInfo> it = this.mConnectedDevices.values().iterator();
        while (it.hasNext()) {
            if (it.next().mDeviceType == 536870912) {
                return true;
            }
        }
        return false;
    }

    private void disconnectSco() {
        Log.d(TAG, "ConnectedDevices size : " + this.mConnectedDevices.size());
        DeviceInfo deviceInfo = null;
        String str = null;
        int i = 0;
        for (DeviceInfo deviceInfo2 : this.mConnectedDevices.values()) {
            Log.d(TAG, "mDeviceType :" + deviceInfo2.mDeviceType + " connectedScoCount:" + i);
            int i2 = deviceInfo2.mDeviceType;
            if (i2 == 16 || i2 == 32) {
                i++;
                str = DeviceInfo.makeDeviceListKey(i2, deviceInfo2.mDeviceAddress);
                deviceInfo = deviceInfo2;
            }
        }
        if (i != 1 || deviceInfo == null || str == null) {
            return;
        }
        AudioSystem.setDeviceConnectionState(new AudioDeviceAttributes(deviceInfo.mDeviceType, deviceInfo.mDeviceAddress, deviceInfo.mDeviceName), 0, 0);
        this.mConnectedDevices.remove(str);
    }

    public int getCurrentConnectedScoDevices() {
        int i;
        int i2;
        Iterator<DeviceInfo> it = this.mConnectedDevices.values().iterator();
        do {
            i = 32;
            if (!it.hasNext()) {
                break;
            }
            i2 = it.next().mDeviceType;
            if (i2 == 32) {
                return 32;
            }
            i = 16;
        } while (i2 != 16);
        return i;
    }

    public IAudioDeviceInventoryWrapper getWrapper() {
        return this.mAdiWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AudioDeviceInventoryWrapper implements IAudioDeviceInventoryWrapper {
        private AudioDeviceInventoryWrapper() {
        }

        @Override // com.android.server.audio.IAudioDeviceInventoryWrapper
        public IAudioDeviceInventoryExt getExtImpl() {
            return AudioDeviceInventory.this.mAdiExt;
        }

        @Override // com.android.server.audio.IAudioDeviceInventoryWrapper
        public String getConnectedDevices() {
            StringBuilder sb = new StringBuilder();
            if (!AudioDeviceInventory.this.mConnectedDevices.isEmpty()) {
                for (DeviceInfo deviceInfo : AudioDeviceInventory.this.mConnectedDevices.values()) {
                    if ((deviceInfo.mDeviceType & Integer.MIN_VALUE) != Integer.MIN_VALUE) {
                        if (sb.length() == 0) {
                            sb.append("0x" + Integer.toHexString(deviceInfo.mDeviceType));
                        } else {
                            sb.append("|0x" + Integer.toHexString(deviceInfo.mDeviceType));
                        }
                    }
                }
                return sb.toString();
            }
            sb.append("null");
            return sb.toString();
        }

        @Override // com.android.server.audio.IAudioDeviceInventoryWrapper
        public boolean isBluetoothScoDeviceConnected() {
            Iterator it = AudioDeviceInventory.this.mConnectedDevices.values().iterator();
            while (it.hasNext()) {
                int i = ((DeviceInfo) it.next()).mDeviceType;
                if (i == 16 || i == 32) {
                    return true;
                }
            }
            return false;
        }
    }
}
