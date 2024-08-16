package com.android.server.audio;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothCodecConfig;
import android.bluetooth.BluetoothCodecStatus;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothHearingAid;
import android.bluetooth.BluetoothLeAudio;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.media.AudioDeviceAttributes;
import android.media.AudioSystem;
import android.media.BluetoothProfileConnectionInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.server.audio.AudioDeviceBroker;
import com.android.server.audio.AudioServiceEvents;
import com.android.server.utils.EventLogger;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BtHelper {
    private static final int BT_HEARING_AID_GAIN_MIN = -128;
    private static final int BT_LE_AUDIO_MAX_VOL = 255;
    private static final int BT_LE_AUDIO_MIN_VOL = 0;
    static final int EVENT_DEVICE_CONFIG_CHANGE = 0;
    private static final int GROUP_ID_END = 15;
    private static final int GROUP_ID_START = 0;
    private static final int SCO_MODE_MAX = 2;
    static final int SCO_MODE_UNDEFINED = -1;
    static final int SCO_MODE_VIRTUAL_CALL = 0;
    private static final int SCO_MODE_VR = 2;
    private static final int SCO_STATE_ACTIVATE_REQ = 1;
    private static final int SCO_STATE_ACTIVE_EXTERNAL = 2;
    private static final int SCO_STATE_ACTIVE_INTERNAL = 3;
    private static final int SCO_STATE_DEACTIVATE_REQ = 4;
    private static final int SCO_STATE_DEACTIVATING = 5;
    private static final int SCO_STATE_INACTIVE = 0;
    private static final String TAG = "AS.BtHelper";
    private static BluetoothA2dp mA2dp;
    private static BluetoothDevice mBluetoothA2dpActiveDevice;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothHeadset mBluetoothHeadset;
    private BluetoothDevice mBluetoothHeadsetDevice;
    private BluetoothDevice mBluetoothHeadsetDummyDevice;
    private final AudioDeviceBroker mDeviceBroker;
    private BluetoothHearingAid mHearingAid;
    private boolean mIsCgon;
    private BluetoothLeAudio mLeAudio;
    private int mScoAudioMode;
    private int mScoAudioState;
    private int mScoConnectionState;
    private boolean mAvrcpAbsVolSupported = false;
    public IBtHelperSocExt mBthSocExt = (IBtHelperSocExt) ExtLoader.type(IBtHelperSocExt.class).base(this).create();
    private HashMap<BluetoothDevice, Integer> mScoClientDevices = new HashMap<>();
    private BluetoothProfile.ServiceListener mBluetoothProfileServiceListener = new BluetoothProfile.ServiceListener() { // from class: com.android.server.audio.BtHelper.1
        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            Log.i(BtHelper.TAG, "In onServiceConnected(), profile: " + i + ", proxy: " + bluetoothProfile);
            if (i == 1 || i == 2 || i == 21 || i == 22) {
                AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("BT profile service: connecting " + BluetoothProfile.getProfileName(i) + " profile").printLog(BtHelper.TAG));
                BtHelper.this.mDeviceBroker.postBtProfileConnected(i, bluetoothProfile);
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            if (i == 1 || i == 2 || i == 21 || i == 22) {
                AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("BT profile service: disconnecting " + BluetoothProfile.getProfileName(i) + " profile").printLog(BtHelper.TAG));
                BtHelper.this.mDeviceBroker.postBtProfileDisconnected(i);
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public BtHelper(AudioDeviceBroker audioDeviceBroker) {
        this.mDeviceBroker = audioDeviceBroker;
    }

    public static String scoAudioModeToString(int i) {
        if (i == -1) {
            return "SCO_MODE_UNDEFINED";
        }
        if (i == 0) {
            return "SCO_MODE_VIRTUAL_CALL";
        }
        if (i == 2) {
            return "SCO_MODE_VR";
        }
        return "SCO_MODE_(" + i + ")";
    }

    public static String scoAudioStateToString(int i) {
        if (i == 0) {
            return "SCO_STATE_INACTIVE";
        }
        if (i == 1) {
            return "SCO_STATE_ACTIVATE_REQ";
        }
        if (i == 2) {
            return "SCO_STATE_ACTIVE_EXTERNAL";
        }
        if (i == 3) {
            return "SCO_STATE_ACTIVE_INTERNAL";
        }
        if (i == 5) {
            return "SCO_STATE_DEACTIVATING";
        }
        return "SCO_STATE_(" + i + ")";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String deviceEventToString(int i) {
        if (i == 0) {
            return "DEVICE_CONFIG_CHANGE";
        }
        return new String("invalid event:" + i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getName(BluetoothDevice bluetoothDevice) {
        String name = bluetoothDevice.getName();
        return name == null ? "" : name;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void SetA2dpActiveDevice(BluetoothDevice bluetoothDevice) {
        Log.w(TAG, "SetA2dpActiveDevice for TWS+ pair as " + bluetoothDevice);
        mBluetoothA2dpActiveDevice = bluetoothDevice;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isTwsPlusSwitch(BluetoothDevice bluetoothDevice, String str) {
        BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(str);
        if (bluetoothDevice == null || remoteDevice == null || bluetoothDevice.getTwsPlusPeerAddress() == null || !bluetoothDevice.isTwsPlusDevice() || !remoteDevice.isTwsPlusDevice() || !bluetoothDevice.getTwsPlusPeerAddress().equals(str)) {
            return false;
        }
        if (mBluetoothA2dpActiveDevice == null) {
            Log.w(TAG, "Not a TwsPlusSwitch as previous active device was null");
            return false;
        }
        Log.i(TAG, "isTwsPlusSwitch true");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"AudioDeviceBroker.mDeviceStateLock"})
    public synchronized void onSystemReady() {
        this.mScoConnectionState = -1;
        Log.i(TAG, "In onSystemReady(), calling resetBluetoothSco()");
        resetBluetoothSco();
        getBluetoothHeadset();
        Intent intent = new Intent("android.media.SCO_AUDIO_STATE_CHANGED");
        intent.putExtra("android.media.extra.SCO_AUDIO_STATE", 0);
        sendStickyBroadcastToAll(intent);
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null) {
            defaultAdapter.getProfileProxy(this.mDeviceBroker.getContext(), this.mBluetoothProfileServiceListener, 2);
            defaultAdapter.getProfileProxy(this.mDeviceBroker.getContext(), this.mBluetoothProfileServiceListener, 21);
            defaultAdapter.getProfileProxy(this.mDeviceBroker.getContext(), this.mBluetoothProfileServiceListener, 22);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onAudioServerDiedRestoreA2dp() {
        this.mDeviceBroker.setForceUse_Async(1, this.mDeviceBroker.getBluetoothA2dpEnabled() ? 0 : 10, "onAudioServerDied()");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isAvrcpAbsoluteVolumeSupported() {
        boolean z;
        if (mA2dp != null) {
            z = this.mAvrcpAbsVolSupported;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setAvrcpAbsoluteVolumeSupported(boolean z) {
        this.mAvrcpAbsVolSupported = z;
        Log.i(TAG, "setAvrcpAbsoluteVolumeSupported supported=" + z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setAvrcpAbsoluteVolumeIndex(int i) {
        if (mA2dp == null) {
            if (AudioService.DEBUG_VOL) {
                AudioService.sVolumeLogger.enqueue(new EventLogger.StringEvent("setAvrcpAbsoluteVolumeIndex: bailing due to null mA2dp").printLog(TAG));
            }
            return;
        }
        if (!this.mAvrcpAbsVolSupported) {
            AudioService.sVolumeLogger.enqueue(new EventLogger.StringEvent("setAvrcpAbsoluteVolumeIndex: abs vol not supported ").printLog(TAG));
            return;
        }
        if (AudioService.DEBUG_VOL) {
            Log.i(TAG, "setAvrcpAbsoluteVolumeIndex index=" + i);
        }
        AudioService.sVolumeLogger.enqueue(new AudioServiceEvents.VolumeEvent(4, i));
        try {
            mA2dp.setAvrcpAbsoluteVolume(i);
        } catch (Exception e) {
            Log.e(TAG, "Exception while changing abs volume", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"AudioDeviceBroker.mDeviceStateLock"})
    public synchronized boolean isNextBtActiveDeviceAvailableForMusic() {
        return this.mBthSocExt.isNextBtActiveDeviceAvailableForMusic(mA2dp, this.mLeAudio);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getA2dpCodec(BluetoothDevice bluetoothDevice) {
        BluetoothCodecStatus bluetoothCodecStatus;
        BluetoothA2dp bluetoothA2dp = mA2dp;
        if (bluetoothA2dp == null) {
            return 0;
        }
        try {
            bluetoothCodecStatus = bluetoothA2dp.getCodecStatus(bluetoothDevice);
        } catch (Exception e) {
            Log.e(TAG, "Exception while getting status of " + bluetoothDevice, e);
            bluetoothCodecStatus = null;
        }
        if (bluetoothCodecStatus == null) {
            return 0;
        }
        BluetoothCodecConfig codecConfig = bluetoothCodecStatus.getCodecConfig();
        if (codecConfig == null) {
            return 0;
        }
        return AudioSystem.bluetoothCodecToAudioFormat(codecConfig.getCodecType());
    }

    private void updateTwsPlusScoState(BluetoothDevice bluetoothDevice, Integer num) {
        if (this.mScoClientDevices.containsKey(bluetoothDevice)) {
            Integer num2 = this.mScoClientDevices.get(bluetoothDevice);
            Log.i(TAG, "updateTwsPlusScoState: prevState: " + num2 + "state: " + num);
            if (num != num2) {
                this.mScoClientDevices.remove(bluetoothDevice);
                this.mScoClientDevices.put(bluetoothDevice, num);
                return;
            }
            return;
        }
        this.mScoClientDevices.put(bluetoothDevice, num);
    }

    private boolean isAudioPathUp() {
        boolean z;
        this.mScoClientDevices.entrySet().iterator();
        Iterator<Integer> it = this.mScoClientDevices.values().iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            if (it.next().intValue() == 12) {
                z = true;
                break;
            }
        }
        Log.d(TAG, "isAudioPathUp returns" + z);
        return z;
    }

    private boolean checkAndUpdatTwsPlusScoState(Intent intent, Integer num) {
        BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
        Log.i(TAG, "device:" + bluetoothDevice);
        boolean z = true;
        if (bluetoothDevice == null) {
            Log.e(TAG, "checkAndUpdatTwsPlusScoState: device is null");
            return true;
        }
        if (bluetoothDevice.isTwsPlusDevice()) {
            if (num.intValue() == 12) {
                if (isAudioPathUp()) {
                    Log.i(TAG, "No need to bringup audio-path");
                    z = false;
                }
                updateTwsPlusScoState(bluetoothDevice, num);
            } else {
                updateTwsPlusScoState(bluetoothDevice, num);
                if (isAudioPathUp()) {
                    Log.i(TAG, "not good to tear down audio-path");
                    z = false;
                }
            }
        }
        Log.i(TAG, "checkAndUpdatTwsPlusScoState returns " + z);
        return z;
    }

    private boolean isGroupDevice(BluetoothDevice bluetoothDevice) {
        int deviceType = bluetoothDevice.getDeviceType();
        Log.i(TAG, "Bluetooth device type: " + deviceType);
        boolean z = deviceType >= 0 && deviceType <= 15;
        Log.i(TAG, "isGroupDevice return " + z);
        return z;
    }

    private void updateGroupScoState(BluetoothDevice bluetoothDevice, Integer num) {
        if (this.mScoClientDevices.containsKey(bluetoothDevice)) {
            Integer num2 = this.mScoClientDevices.get(bluetoothDevice);
            Log.i(TAG, "updateGroupScoState: prevState: " + num2 + "state: " + num);
            if (num != num2) {
                this.mScoClientDevices.remove(bluetoothDevice);
                this.mScoClientDevices.put(bluetoothDevice, num);
                return;
            }
            return;
        }
        this.mScoClientDevices.put(bluetoothDevice, num);
    }

    private boolean checkAndUpdateGroupScoState(Intent intent, Integer num) {
        BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
        Log.i(TAG, "device:" + bluetoothDevice);
        boolean z = true;
        if (bluetoothDevice == null) {
            Log.e(TAG, "checkAndUpdateGroupScoState: device is null");
            return true;
        }
        if (isGroupDevice(bluetoothDevice)) {
            if (num.intValue() == 12) {
                if (isAudioPathUp()) {
                    Log.i(TAG, "No need to bringup audio-path");
                    z = false;
                }
                updateGroupScoState(bluetoothDevice, num);
            } else {
                updateGroupScoState(bluetoothDevice, num);
                if (isAudioPathUp()) {
                    Log.i(TAG, "not good to tear down audio-path");
                    z = false;
                }
            }
        }
        Log.i(TAG, "checkAndUpdateGroupScoState returns " + z);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"AudioDeviceBroker.mDeviceStateLock"})
    public synchronized void receiveBtEvent(Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "receiveBtEvent action: " + action + " mScoAudioState: " + this.mScoAudioState);
        if (action.equals("android.bluetooth.headset.profile.action.ACTIVE_DEVICE_CHANGED")) {
            if (this.mBthSocExt.isLeAudioDevice(intent)) {
            } else {
                setBtScoActiveDevice((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE", BluetoothDevice.class));
            }
        } else if (action.equals("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED")) {
            int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", -1);
            Log.i(TAG, "receiveBtEvent ACTION_AUDIO_STATE_CHANGED: btState=" + intExtra + "{" + (intExtra == 12 ? "AudioConnected" : "AudioDisconnected") + "}");
            if (Build.isQcomPlatform()) {
                if (checkAndUpdatTwsPlusScoState(intent, Integer.valueOf(intExtra)) && checkAndUpdateGroupScoState(intent, Integer.valueOf(intExtra))) {
                    this.mDeviceBroker.postScoAudioStateChanged(intExtra);
                }
            } else {
                this.mDeviceBroker.postScoAudioStateChanged(intExtra);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @GuardedBy({"AudioDeviceBroker.mDeviceStateLock"})
    public void onScoAudioStateChanged(int i) {
        BluetoothHeadset bluetoothHeadset;
        BluetoothDevice bluetoothDevice;
        int i2 = 2;
        boolean z = false;
        switch (i) {
            case 10:
                this.mDeviceBroker.setBluetoothScoOn(false, "BtHelper.receiveBtEvent");
                if (this.mScoAudioState == 1 && (bluetoothHeadset = this.mBluetoothHeadset) != null && (bluetoothDevice = this.mBluetoothHeadsetDevice) != null && connectBluetoothScoAudioHelper(bluetoothHeadset, bluetoothDevice, this.mScoAudioMode)) {
                    this.mScoAudioState = 3;
                    break;
                } else {
                    r4 = this.mScoAudioState != 2;
                    this.mScoAudioState = 0;
                    i2 = 0;
                    break;
                }
                break;
            case 11:
                int i3 = this.mScoAudioState;
                if (i3 != 3 && i3 != 4) {
                    this.mScoAudioState = 2;
                }
                i2 = -1;
                r4 = z;
                break;
            case 12:
                int i4 = this.mScoAudioState;
                if (i4 != 3 && i4 != 4) {
                    this.mScoAudioState = 2;
                } else if (this.mDeviceBroker.isBluetoothScoRequested()) {
                    z = true;
                }
                this.mDeviceBroker.setBluetoothScoOn(true, "BtHelper.receiveBtEvent");
                i2 = 1;
                r4 = z;
                break;
            default:
                i2 = -1;
                r4 = z;
                break;
        }
        if (r4) {
            broadcastScoConnectionState(i2);
            if (AudioService.DEBUG_DEVICES) {
                Log.d(TAG, "receiveBtEvent(): BR SCOAudioStateChanged, scoAudioState=" + i2);
            }
            Intent intent = new Intent("android.media.SCO_AUDIO_STATE_CHANGED");
            intent.putExtra("android.media.extra.SCO_AUDIO_STATE", i2);
            sendStickyBroadcastToAll(intent);
        }
    }

    boolean isBluetoothAudioNotConnectedToEarbud() {
        boolean z;
        String twsPlusPeerAddress;
        BluetoothDevice bluetoothDevice = this.mBluetoothHeadsetDevice;
        if (bluetoothDevice != null && bluetoothDevice.isTwsPlusDevice() && (twsPlusPeerAddress = this.mBluetoothHeadsetDevice.getTwsPlusPeerAddress()) != null) {
            BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(twsPlusPeerAddress);
            Log.d(TAG, "peer device audio State: " + this.mBluetoothHeadset.getAudioState(remoteDevice));
            if (this.mBluetoothHeadset.getAudioState(remoteDevice) == 12 || this.mBluetoothHeadset.getAudioState(this.mBluetoothHeadsetDevice) == 12) {
                Log.w(TAG, "TwsPLus Case: one of eb SCO is connected");
                z = false;
                Log.d(TAG, "isBluetoothAudioConnectedToEarbud returns: " + z);
                return z;
            }
        }
        z = true;
        Log.d(TAG, "isBluetoothAudioConnectedToEarbud returns: " + z);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isBluetoothScoOn() {
        BluetoothDevice bluetoothDevice;
        BluetoothHeadset bluetoothHeadset = this.mBluetoothHeadset;
        if (bluetoothHeadset != null && (bluetoothDevice = this.mBluetoothHeadsetDevice) != null) {
            return bluetoothHeadset.getAudioState(bluetoothDevice) == 12;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isInbandRingingEnabled() {
        boolean isInbandRingingEnabled;
        BluetoothHeadset bluetoothHeadset = this.mBluetoothHeadset;
        isInbandRingingEnabled = (bluetoothHeadset == null || this.mBluetoothHeadsetDevice == null) ? false : bluetoothHeadset.isInbandRingingEnabled();
        if (AudioService.DEBUG_DEVICES) {
            Log.d(TAG, "isInbandRingingEnabled() = " + isInbandRingingEnabled);
        }
        return isInbandRingingEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"AudioDeviceBroker.mDeviceStateLock"})
    public synchronized boolean startBluetoothSco(int i, String str) {
        AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent(str));
        return requestScoState(12, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"AudioDeviceBroker.mDeviceStateLock"})
    public synchronized boolean stopBluetoothSco(String str) {
        AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent(str));
        return requestScoState(10, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateBleCGStateToBt(boolean z) {
        if (this.mLeAudio == null) {
            Log.d(TAG, "mLeAudio: null mLeAudio");
            return;
        }
        if (AudioService.DEBUG_VOL) {
            Log.i(TAG, "updateBleCGStateToBt, isCgOn:" + z + ", mIsCgon:" + this.mIsCgon);
        }
        if (z != this.mIsCgon) {
            this.mLeAudio.notifyBluetoothCallState(z);
        }
        this.mIsCgon = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setLeAudioVolume(int i, int i2, int i3) {
        if (this.mLeAudio == null) {
            if (AudioService.DEBUG_VOL) {
                Log.i(TAG, "setLeAudioVolume: null mLeAudio");
            }
            return;
        }
        int round = (int) Math.round((i * 255.0d) / i2);
        if (AudioService.DEBUG_VOL) {
            Log.i(TAG, "setLeAudioVolume: calling mLeAudio.setVolume idx=" + i + " volume=" + round);
        }
        AudioService.sVolumeLogger.enqueue(new AudioServiceEvents.VolumeEvent(10, i, i2));
        try {
            this.mLeAudio.setVolume(round);
        } catch (Exception e) {
            Log.e(TAG, "Exception while setting LE volume", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setHearingAidVolume(int i, int i2, boolean z) {
        if (this.mHearingAid == null) {
            if (AudioService.DEBUG_VOL) {
                Log.i(TAG, "setHearingAidVolume: null mHearingAid");
            }
            return;
        }
        int streamVolumeDB = (int) AudioSystem.getStreamVolumeDB(i2, i / 10, AudioFormat.OPUS);
        if (streamVolumeDB < BT_HEARING_AID_GAIN_MIN) {
            streamVolumeDB = BT_HEARING_AID_GAIN_MIN;
        }
        if (AudioService.DEBUG_VOL) {
            Log.i(TAG, "setHearingAidVolume: calling mHearingAid.setVolume idx=" + i + " gain=" + streamVolumeDB);
        }
        if (z) {
            AudioService.sVolumeLogger.enqueue(new AudioServiceEvents.VolumeEvent(3, i, streamVolumeDB));
        }
        try {
            this.mHearingAid.setVolume(streamVolumeDB);
        } catch (Exception e) {
            Log.i(TAG, "Exception while setting hearing aid volume", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onBroadcastScoConnectionState(int i) {
        if (i == this.mScoConnectionState) {
            return;
        }
        Intent intent = new Intent("android.media.ACTION_SCO_AUDIO_STATE_UPDATED");
        intent.putExtra("android.media.extra.SCO_AUDIO_STATE", i);
        intent.putExtra("android.media.extra.SCO_AUDIO_PREVIOUS_STATE", this.mScoConnectionState);
        sendStickyBroadcastToAll(intent);
        this.mScoConnectionState = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void disconnectAllBluetoothProfiles() {
        if (AudioService.DEBUG_DEVICES) {
            Log.d(TAG, "disconnectAllBluetoothProfiles()");
        }
        this.mDeviceBroker.postBtProfileDisconnected(2);
        this.mDeviceBroker.postBtProfileDisconnected(11);
        this.mDeviceBroker.postBtProfileDisconnected(1);
        this.mDeviceBroker.postBtProfileDisconnected(21);
        this.mDeviceBroker.postBtProfileDisconnected(22);
        this.mDeviceBroker.postBtProfileDisconnected(26);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void resetBluetoothSco() {
        Log.i(TAG, "In resetBluetoothSco(), calling clearAllScoClients()");
        this.mScoAudioState = 0;
        broadcastScoConnectionState(0);
        if (Build.isMtkPlatform()) {
            AudioSystem.setParameters("BTAudiosuspend=false");
        }
        this.mDeviceBroker.clearA2dpSuspended(false);
        this.mDeviceBroker.clearLeAudioSuspended(false);
        if (Build.isQcomPlatform()) {
            this.mScoClientDevices.clear();
        }
        this.mDeviceBroker.setBluetoothScoOn(false, "resetBluetoothSco");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"AudioDeviceBroker.mDeviceStateLock"})
    public synchronized void disconnectHeadset() {
        setBtScoActiveDevice(null);
        this.mBluetoothHeadset = null;
        this.mDeviceBroker.getWrapper().getExtImpl().clearAvrcpAbsoluteVolume();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onBtProfileDisconnected(int i) {
        if (i == 2) {
            mA2dp = null;
        } else if (i == 11 || i == 26) {
            Log.e(TAG, "onBtProfileDisconnected: Not a profile handled by BtHelper " + BluetoothProfile.getProfileName(i));
        } else if (i == 21) {
            this.mHearingAid = null;
        } else if (i == 22) {
            this.mLeAudio = null;
        } else {
            Log.e(TAG, "onBtProfileDisconnected: Not a valid profile to disconnect " + BluetoothProfile.getProfileName(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"AudioDeviceBroker.mDeviceStateLock"})
    public synchronized void onBtProfileConnected(int i, BluetoothProfile bluetoothProfile) {
        if (i == 1) {
            onHeadsetProfileConnected((BluetoothHeadset) bluetoothProfile);
            return;
        }
        if (i == 2) {
            mA2dp = (BluetoothA2dp) bluetoothProfile;
        } else if (i == 21) {
            this.mHearingAid = (BluetoothHearingAid) bluetoothProfile;
        } else if (i == 22) {
            this.mLeAudio = (BluetoothLeAudio) bluetoothProfile;
        }
        List<BluetoothDevice> connectedDevices = bluetoothProfile.getConnectedDevices();
        if (connectedDevices.isEmpty()) {
            return;
        }
        if (i == 4) {
            return;
        }
        BluetoothDevice bluetoothDevice = connectedDevices.get(0);
        if (bluetoothProfile.getConnectionState(bluetoothDevice) == 2) {
            this.mDeviceBroker.queueOnBluetoothActiveDeviceChanged(new AudioDeviceBroker.BtDeviceChangedData(bluetoothDevice, null, new BluetoothProfileConnectionInfo(i), "mBluetoothProfileServiceListener"));
        } else {
            this.mDeviceBroker.queueOnBluetoothActiveDeviceChanged(new AudioDeviceBroker.BtDeviceChangedData(null, bluetoothDevice, new BluetoothProfileConnectionInfo(i), "mBluetoothProfileServiceListener"));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:27:0x005d A[Catch: all -> 0x0064, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0013, B:6:0x0017, B:8:0x001e, B:9:0x0026, B:16:0x0035, B:18:0x0039, B:23:0x0042, B:25:0x004a, B:27:0x005d, B:31:0x004e, B:33:0x0056), top: B:2:0x0001 }] */
    @GuardedBy({"AudioDeviceBroker.mDeviceStateLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public synchronized void onHeadsetProfileConnected(BluetoothHeadset bluetoothHeadset) {
        boolean z;
        BluetoothDevice bluetoothDevice;
        this.mDeviceBroker.handleCancelFailureToConnectToBtHeadsetService();
        this.mBluetoothHeadset = bluetoothHeadset;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        List emptyList = Collections.emptyList();
        if (defaultAdapter != null) {
            emptyList = defaultAdapter.getActiveDevices(1);
        }
        setBtScoActiveDevice(emptyList.size() > 0 ? (BluetoothDevice) emptyList.get(0) : null);
        checkScoAudioState();
        int i = this.mScoAudioState;
        if (i == 1 || i == 4) {
            BluetoothHeadset bluetoothHeadset2 = this.mBluetoothHeadset;
            if (bluetoothHeadset2 != null && (bluetoothDevice = this.mBluetoothHeadsetDevice) != null) {
                if (i == 1) {
                    z = connectBluetoothScoAudioHelper(bluetoothHeadset2, bluetoothDevice, this.mScoAudioMode);
                    if (z) {
                        this.mScoAudioState = 3;
                    }
                } else if (i == 4) {
                    z = disconnectBluetoothScoAudioHelper(bluetoothHeadset2, bluetoothDevice, this.mScoAudioMode);
                    if (z) {
                        this.mScoAudioState = 5;
                    }
                }
                if (!z) {
                    this.mScoAudioState = 0;
                    broadcastScoConnectionState(0);
                }
            }
            z = false;
            if (!z) {
            }
        }
    }

    private void broadcastScoConnectionState(int i) {
        this.mDeviceBroker.postBroadcastScoConnectionState(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioDeviceAttributes getHeadsetAudioDevice() {
        BluetoothDevice bluetoothDevice = this.mBluetoothHeadsetDevice;
        if (bluetoothDevice == null) {
            return null;
        }
        return btHeadsetDeviceToAudioDevice(bluetoothDevice);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioDeviceAttributes getHeadsetAudioDummyDevice() {
        BluetoothDevice bluetoothDevice = this.mBluetoothHeadsetDummyDevice;
        if (bluetoothDevice == null) {
            return null;
        }
        return btHeadsetDeviceToAudioDevice(bluetoothDevice);
    }

    private static AudioDeviceAttributes btHeadsetDeviceToAudioDevice(BluetoothDevice bluetoothDevice) {
        BluetoothClass bluetoothClass;
        int i = 16;
        if (bluetoothDevice == null) {
            return new AudioDeviceAttributes(16, "");
        }
        String address = bluetoothDevice.getAddress();
        String name = !address.equals("00:00:00:00:00:00") ? getName(bluetoothDevice) : "";
        String str = BluetoothAdapter.checkBluetoothAddress(address) ? address : "";
        if (Build.isQcomPlatform()) {
            bluetoothClass = "00:00:00:00:00:00".equals(str) ? null : bluetoothDevice.getBluetoothClass();
        } else {
            bluetoothClass = bluetoothDevice.getBluetoothClass();
        }
        if (bluetoothClass != null) {
            int deviceClass = bluetoothClass.getDeviceClass();
            if (deviceClass == 1028 || deviceClass == 1032) {
                i = 32;
            } else if (deviceClass == 1056) {
                i = 64;
            }
        }
        if (AudioService.DEBUG_DEVICES && Build.isQcomPlatform()) {
            StringBuilder sb = new StringBuilder();
            sb.append("btHeadsetDeviceToAudioDevice btDevice: ");
            sb.append(bluetoothDevice);
            sb.append(" btClass: ");
            Object obj = bluetoothClass;
            if (bluetoothClass == null) {
                obj = "Unknown";
            }
            sb.append(obj);
            sb.append(" nativeType: ");
            sb.append(i);
            sb.append(" address: ");
            sb.append(str);
            Log.i(TAG, sb.toString());
        } else if (Build.isMtkPlatform()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("btHeadsetDeviceToAudioDevice btDevice: ");
            sb2.append(bluetoothDevice.getAnonymizedAddress());
            sb2.append(" btClass: ");
            Object obj2 = bluetoothClass;
            if (bluetoothClass == null) {
                obj2 = "Unknown";
            }
            sb2.append(obj2);
            sb2.append(" nativeType: ");
            sb2.append(i);
            Log.i(TAG, sb2.toString());
        }
        return new AudioDeviceAttributes(i, str, name);
    }

    private boolean handleBtScoActiveDeviceChange(BluetoothDevice bluetoothDevice, boolean z) {
        boolean z2;
        if (bluetoothDevice == null) {
            return true;
        }
        if (Build.isQcomPlatform() && !"00:00:00:00:00:00".equals(bluetoothDevice.getAddress())) {
            bluetoothDevice.getBluetoothClass();
        }
        AudioDeviceAttributes btHeadsetDeviceToAudioDevice = btHeadsetDeviceToAudioDevice(bluetoothDevice);
        String name = getName(bluetoothDevice);
        if (Build.isQcomPlatform() && name == null) {
            Log.i(TAG, "handleBtScoActiveDeviceChange: btDeviceName is null, sending empty string");
        }
        if (z) {
            z2 = this.mDeviceBroker.handleDeviceConnection(btHeadsetDeviceToAudioDevice, z, bluetoothDevice) | false;
        } else {
            int[] iArr = {16, 32, 64};
            boolean z3 = false;
            for (int i = 0; i < 3; i++) {
                z3 |= this.mDeviceBroker.handleDeviceConnection(new AudioDeviceAttributes(iArr[i], btHeadsetDeviceToAudioDevice.getAddress(), btHeadsetDeviceToAudioDevice.getName()), z, bluetoothDevice);
            }
            z2 = z3;
        }
        return this.mDeviceBroker.handleDeviceConnection(new AudioDeviceAttributes(-2147483640, btHeadsetDeviceToAudioDevice.getAddress(), btHeadsetDeviceToAudioDevice.getName()), z, bluetoothDevice) && z2;
    }

    private String getAnonymizedAddress(BluetoothDevice bluetoothDevice) {
        return bluetoothDevice == null ? "(null)" : bluetoothDevice.getAnonymizedAddress();
    }

    @GuardedBy({"BtHelper.this"})
    private void setBtScoActiveDevice(BluetoothDevice bluetoothDevice) {
        BluetoothDevice bluetoothDevice2 = bluetoothDevice;
        Log.i(TAG, "setBtScoActiveDevice: " + getAnonymizedAddress(this.mBluetoothHeadsetDevice) + " -> " + getAnonymizedAddress(bluetoothDevice));
        BluetoothDevice bluetoothDevice3 = this.mBluetoothHeadsetDevice;
        if (Objects.equals(bluetoothDevice2, bluetoothDevice3)) {
            return;
        }
        if (Build.isQcomPlatform()) {
            BluetoothDevice bluetoothDevice4 = this.mBluetoothHeadsetDevice;
            if (bluetoothDevice4 != null && bluetoothDevice4.isTwsPlusDevice() && bluetoothDevice2 != null && Objects.equals(this.mBluetoothHeadsetDevice.getTwsPlusPeerAddress(), bluetoothDevice.getAddress())) {
                Log.i(TAG, "setBtScoActiveDevice: Active device switch between twsplus devices");
                return;
            }
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter == null) {
                Log.i(TAG, "adapter is null, returning from setBtScoActiveDevice");
                return;
            }
            BluetoothDevice remoteDevice = defaultAdapter.getRemoteDevice("00:00:00:00:00:00");
            this.mBluetoothHeadsetDummyDevice = remoteDevice;
            if (this.mBluetoothHeadsetDevice == null && bluetoothDevice2 != null && !handleBtScoActiveDeviceChange(remoteDevice, true)) {
                Log.e(TAG, "setBtScoActiveDevice() failed to add new device " + bluetoothDevice2);
                bluetoothDevice2 = null;
            }
            if (this.mBluetoothHeadsetDevice != null && bluetoothDevice2 == null && !handleBtScoActiveDeviceChange(this.mBluetoothHeadsetDummyDevice, false)) {
                Log.w(TAG, "setBtScoActiveDevice() failed to remove previous device " + bluetoothDevice3);
            }
            this.mBluetoothHeadsetDevice = bluetoothDevice2;
            if (bluetoothDevice2 == null) {
                this.mDeviceBroker.getWrapper().sendIILMsg(81, 2, 0, 0, null, 0);
                this.mBluetoothHeadsetDummyDevice = null;
                Log.i(TAG, "In setBtScoActiveDevice(), calling resetBluetoothSco()");
                resetBluetoothSco();
                return;
            }
            return;
        }
        if (Build.isMtkPlatform() && !Objects.equals(bluetoothDevice2, bluetoothDevice3)) {
            this.mDeviceBroker.resetBluetoothScoOfApp();
        }
        if (!handleBtScoActiveDeviceChange(bluetoothDevice3, false)) {
            Log.w(TAG, "setBtScoActiveDevice() failed to remove previous device " + getAnonymizedAddress(bluetoothDevice3));
        }
        if (!handleBtScoActiveDeviceChange(bluetoothDevice2, true)) {
            Log.e(TAG, "setBtScoActiveDevice() failed to add new device " + getAnonymizedAddress(bluetoothDevice));
            bluetoothDevice2 = null;
        }
        this.mBluetoothHeadsetDevice = bluetoothDevice2;
        if (bluetoothDevice2 == null) {
            this.mDeviceBroker.getWrapper().sendIILMsg(81, 2, 0, 0, null, 0);
            if (Build.isMtkPlatform()) {
                this.mDeviceBroker.resetBluetoothScoOfApp();
            }
            resetBluetoothSco();
            return;
        }
        if (Build.isMtkPlatform()) {
            this.mDeviceBroker.restartScoInVoipCall();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:72:0x01b1, code lost:
    
        if (r10 != 0) goto L79;
     */
    @GuardedBy({"BtHelper.this"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean requestScoState(int i, int i2) {
        Log.i(TAG, "In requestScoState(), state: " + i + ", scoAudioMode: " + i2);
        checkScoAudioState();
        if (i == 12) {
            if (this.mScoAudioState == 3) {
                Log.i(TAG, "SCO already connected, Not broadcasting SCO_AUDIO_STATE_CONNECTING");
            } else {
                broadcastScoConnectionState(2);
            }
            int i3 = this.mScoAudioState;
            if (i3 == 0) {
                this.mScoAudioMode = i2;
                if (i2 == -1) {
                    this.mScoAudioMode = 0;
                    if (this.mBluetoothHeadsetDevice != null) {
                        int i4 = Settings.Global.getInt(this.mDeviceBroker.getContentResolver(), "bluetooth_sco_channel_" + this.mBluetoothHeadsetDevice.getAddress(), 0);
                        this.mScoAudioMode = i4;
                        if (i4 > 2 || i4 < 0) {
                            this.mScoAudioMode = 0;
                        }
                    }
                }
                BluetoothHeadset bluetoothHeadset = this.mBluetoothHeadset;
                if (bluetoothHeadset == null) {
                    if (getBluetoothHeadset()) {
                        this.mScoAudioState = 1;
                    } else {
                        Log.w(TAG, "requestScoState: getBluetoothHeadset failed during connection, mScoAudioMode=" + this.mScoAudioMode);
                        broadcastScoConnectionState(0);
                        return false;
                    }
                } else {
                    BluetoothDevice bluetoothDevice = this.mBluetoothHeadsetDevice;
                    if (bluetoothDevice == null) {
                        Log.w(TAG, "requestScoState: no active device while connecting, mScoAudioMode=" + this.mScoAudioMode);
                        broadcastScoConnectionState(0);
                        return false;
                    }
                    if (connectBluetoothScoAudioHelper(bluetoothHeadset, bluetoothDevice, this.mScoAudioMode)) {
                        this.mScoAudioState = 3;
                    } else {
                        Log.w(TAG, "requestScoState: connect to " + getAnonymizedAddress(this.mBluetoothHeadsetDevice) + " failed, mScoAudioMode=" + this.mScoAudioMode);
                        broadcastScoConnectionState(0);
                        return false;
                    }
                }
            } else if (i3 != 1) {
                if (i3 == 2) {
                    broadcastScoConnectionState(1);
                } else if (i3 == 3) {
                    Log.w(TAG, "requestScoState: already in ACTIVE mode, simply return");
                } else if (i3 == 4) {
                    this.mScoAudioState = 3;
                    broadcastScoConnectionState(1);
                } else if (i3 == 5) {
                    this.mScoAudioState = 1;
                } else {
                    Log.w(TAG, "requestScoState: failed to connect in state " + this.mScoAudioState + ", scoAudioMode=" + i2);
                    broadcastScoConnectionState(0);
                    return false;
                }
            } else if (Build.isMtkPlatform()) {
                Log.w(TAG, "requestScoState: already in req mode, simply return");
            }
        } else if (i == 10) {
            int i5 = this.mScoAudioState;
            if (i5 == 1) {
                this.mScoAudioState = 0;
                broadcastScoConnectionState(0);
            } else if (i5 == 2 || i5 == 3) {
                if (this.mBluetoothHeadset == null) {
                    if (getBluetoothHeadset()) {
                        this.mScoAudioState = 4;
                    } else {
                        Log.w(TAG, "requestScoState: getBluetoothHeadset failed during disconnection, mScoAudioMode=" + this.mScoAudioMode);
                        this.mScoAudioState = 0;
                        broadcastScoConnectionState(0);
                        return false;
                    }
                } else if (this.mBluetoothHeadsetDevice == null) {
                    this.mScoAudioState = 0;
                    broadcastScoConnectionState(0);
                } else {
                    if (i5 == 2) {
                        if (Build.isMtkPlatform()) {
                            int audioModeOwnerMode = this.mDeviceBroker.getAudioModeOwnerMode();
                            if (audioModeOwnerMode != 3) {
                            }
                        } else {
                            Log.e(TAG, "requestScoState: Error case when mScoAudioState == SCO_STATE_ACTIVE_EXTERNA return;");
                        }
                    }
                    if (disconnectBluetoothScoAudioHelper(this.mBluetoothHeadset, this.mBluetoothHeadsetDevice, this.mScoAudioMode)) {
                        this.mScoAudioState = 5;
                    } else {
                        this.mScoAudioState = 0;
                        broadcastScoConnectionState(0);
                    }
                }
            } else {
                Log.w(TAG, "requestScoState: failed to disconnect in state " + this.mScoAudioState + ", scoAudioMode=" + i2);
                broadcastScoConnectionState(0);
                return false;
            }
        }
        return true;
    }

    private void sendStickyBroadcastToAll(Intent intent) {
        intent.addFlags(AudioFormat.EVRC);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mDeviceBroker.getContext().sendStickyBroadcastAsUser(intent, UserHandle.ALL);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private static boolean disconnectBluetoothScoAudioHelper(BluetoothHeadset bluetoothHeadset, BluetoothDevice bluetoothDevice, int i) {
        Log.i(TAG, "In disconnectBluetoothScoAudioHelper(), scoAudioMode: " + i + ", bluetoothHeadset: " + bluetoothHeadset + ", BluetoothDevice: " + bluetoothDevice);
        if (i == 0) {
            Log.i(TAG, "In disconnectBluetoothScoAudioHelper(), calling stopScoUsingVirtualVoiceCall()");
            return bluetoothHeadset.stopScoUsingVirtualVoiceCall();
        }
        if (i != 2) {
            return false;
        }
        Log.i(TAG, "In disconnectBluetoothScoAudioHelper(), calling stopVoiceRecognition()");
        return bluetoothHeadset.stopVoiceRecognition(bluetoothDevice);
    }

    private static boolean connectBluetoothScoAudioHelper(BluetoothHeadset bluetoothHeadset, BluetoothDevice bluetoothDevice, int i) {
        Log.i(TAG, "In connectBluetoothScoAudioHelper(), scoAudioMode: " + i + ", bluetoothHeadset: " + bluetoothHeadset + ", BluetoothDevice: " + bluetoothDevice);
        if (i == 0) {
            Log.i(TAG, "In connectBluetoothScoAudioHelper(), calling startScoUsingVirtualVoiceCall()");
            return bluetoothHeadset.startScoUsingVirtualVoiceCall();
        }
        if (i != 2) {
            return false;
        }
        Log.i(TAG, "In connectBluetoothScoAudioHelper(), calling startVoiceRecognition()");
        return bluetoothHeadset.startVoiceRecognition(bluetoothDevice);
    }

    private void checkScoAudioState() {
        BluetoothDevice bluetoothDevice;
        BluetoothHeadset bluetoothHeadset = this.mBluetoothHeadset;
        if (bluetoothHeadset != null && (bluetoothDevice = this.mBluetoothHeadsetDevice) != null && this.mScoAudioState == 0 && bluetoothHeadset.getAudioState(bluetoothDevice) != 10) {
            this.mScoAudioState = 2;
        }
        Log.i(TAG, "In checkScoAudioState(), mScoAudioState: " + this.mScoAudioState);
    }

    private boolean getBluetoothHeadset() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean profileProxy = defaultAdapter != null ? defaultAdapter.getProfileProxy(this.mDeviceBroker.getContext(), this.mBluetoothProfileServiceListener, 1) : false;
        this.mDeviceBroker.handleFailureToConnectToBtHeadsetService(profileProxy ? 3000 : 0);
        return profileProxy;
    }

    public static String bluetoothCodecToEncodingString(int i) {
        if (i == 0) {
            return "ENCODING_SBC";
        }
        if (i == 1) {
            return "ENCODING_AAC";
        }
        if (i == 2) {
            return "ENCODING_APTX";
        }
        if (i == 3) {
            return "ENCODING_APTX_HD";
        }
        if (i == 4) {
            return "ENCODING_LDAC";
        }
        if (i == 6) {
            return "ENCODING_OPUS";
        }
        if (i == 18 || i == 19) {
            return "ENCODING_LHDC";
        }
        return "ENCODING_BT_CODEC_TYPE(" + i + ")";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getProfileFromType(int i) {
        if (AudioSystem.isBluetoothA2dpOutDevice(i)) {
            return 2;
        }
        if (AudioSystem.isBluetoothScoDevice(i)) {
            return 1;
        }
        return AudioSystem.isBluetoothLeDevice(i) ? 22 : 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Bundle getPreferredAudioProfiles(String str) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        return defaultAdapter.getPreferredAudioProfiles(defaultAdapter.getRemoteDevice(str));
    }

    public static void onNotifyPreferredAudioProfileApplied(BluetoothDevice bluetoothDevice) {
        BluetoothAdapter.getDefaultAdapter().notifyActiveDeviceChangeApplied(bluetoothDevice);
    }

    public static String btDeviceClassToString(int i) {
        switch (i) {
            case 1024:
                return "AUDIO_VIDEO_UNCATEGORIZED";
            case 1028:
                return "AUDIO_VIDEO_WEARABLE_HEADSET";
            case 1032:
                return "AUDIO_VIDEO_HANDSFREE";
            case 1036:
                return "AUDIO_VIDEO_RESERVED_0x040C";
            case 1040:
                return "AUDIO_VIDEO_MICROPHONE";
            case 1044:
                return "AUDIO_VIDEO_LOUDSPEAKER";
            case 1048:
                return "AUDIO_VIDEO_HEADPHONES";
            case 1052:
                return "AUDIO_VIDEO_PORTABLE_AUDIO";
            case 1056:
                return "AUDIO_VIDEO_CAR_AUDIO";
            case 1060:
                return "AUDIO_VIDEO_SET_TOP_BOX";
            case 1064:
                return "AUDIO_VIDEO_HIFI_AUDIO";
            case 1068:
                return "AUDIO_VIDEO_VCR";
            case 1072:
                return "AUDIO_VIDEO_VIDEO_CAMERA";
            case 1076:
                return "AUDIO_VIDEO_CAMCORDER";
            case 1080:
                return "AUDIO_VIDEO_VIDEO_MONITOR";
            case 1084:
                return "AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER";
            case 1088:
                return "AUDIO_VIDEO_VIDEO_CONFERENCING";
            case 1092:
                return "AUDIO_VIDEO_RESERVED_0x0444";
            case 1096:
                return "AUDIO_VIDEO_VIDEO_GAMING_TOY";
            default:
                return TextUtils.formatSimple("0x%04x", new Object[]{Integer.valueOf(i)});
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        BluetoothClass bluetoothClass;
        printWriter.println("\n" + str + "mBluetoothHeadset: " + this.mBluetoothHeadset);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("mBluetoothHeadsetDevice: ");
        sb.append(this.mBluetoothHeadsetDevice);
        printWriter.println(sb.toString());
        BluetoothDevice bluetoothDevice = this.mBluetoothHeadsetDevice;
        if (bluetoothDevice != null && (bluetoothClass = bluetoothDevice.getBluetoothClass()) != null) {
            printWriter.println(str + "mBluetoothHeadsetDevice.DeviceClass: " + btDeviceClassToString(bluetoothClass.getDeviceClass()));
        }
        printWriter.println(str + "mScoAudioState: " + scoAudioStateToString(this.mScoAudioState));
        printWriter.println(str + "mScoAudioMode: " + scoAudioModeToString(this.mScoAudioMode));
        printWriter.println("\n" + str + "mHearingAid: " + this.mHearingAid);
        printWriter.println("\n" + str + "mLeAudio: " + this.mLeAudio);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append("mA2dp: ");
        sb2.append(mA2dp);
        printWriter.println(sb2.toString());
        printWriter.println(str + "mAvrcpAbsVolSupported: " + this.mAvrcpAbsVolSupported);
    }

    public int getScoAudioState() {
        return this.mScoAudioState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothDevice getBtDevice() {
        if (this.mBluetoothHeadset == null) {
            return null;
        }
        return this.mBluetoothDevice;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBtDevice(BluetoothDevice bluetoothDevice) {
        if (this.mBluetoothHeadset == null) {
            this.mBluetoothDevice = null;
        } else {
            this.mBluetoothDevice = bluetoothDevice;
        }
    }

    public boolean isLoudSpeakerBt() {
        BluetoothClass bluetoothClass;
        BluetoothDevice btDevice = getBtDevice();
        if (btDevice == null || (bluetoothClass = btDevice.getBluetoothClass()) == null) {
            return false;
        }
        int deviceClass = bluetoothClass.getDeviceClass();
        return deviceClass == 1044 || deviceClass == 1084;
    }
}
