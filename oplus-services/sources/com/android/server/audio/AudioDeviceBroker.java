package com.android.server.audio;

import android.app.compat.CompatChanges;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.media.AudioAttributes;
import android.media.AudioDeviceAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.AudioRoutesInfo;
import android.media.AudioSystem;
import android.media.BluetoothProfileConnectionInfo;
import android.media.IAudioRoutesObserver;
import android.media.ICapturePresetDevicesRoleDispatcher;
import android.media.ICommunicationDeviceDispatcher;
import android.media.IStrategyNonDefaultDevicesDispatcher;
import android.media.IStrategyPreferredDevicesDispatcher;
import android.media.MediaMetrics;
import android.media.audiopolicy.AudioProductStrategy;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.PrintWriterPrinter;
import com.android.internal.annotations.GuardedBy;
import com.android.server.audio.AudioDeviceBroker;
import com.android.server.audio.AudioDeviceInventory;
import com.android.server.audio.AudioServiceEvents;
import com.android.server.utils.EventLogger;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AudioDeviceBroker {
    private static final long BROKER_WAKELOCK_TIMEOUT_MS = 5000;
    static final int BTA2DP_DOCK_TIMEOUT_MS = 8000;
    private static final int BTA2DP_MUTE_CHECK_DELAY_MS = 100;
    private static final int BTA2DP_MUTE_CHECK_DELAY_MS_INCREASE = 400;
    private static final int BTA2DP_MUTE_MSG_DELAY_MS_INCREASE = 160;
    private static final int BT_DEVICE_CONNECTED_EVENT_DELAY_MS = 200;
    static final int BT_HEADSET_CNCT_TIMEOUT_MS = 3000;
    private static final Set<Integer> MESSAGES_MUTE_MUSIC;
    private static final int MSG_BROADCAST_AUDIO_BECOMING_NOISY = 12;
    private static final int MSG_BT_HEADSET_CNCT_FAILED = 9;
    private static final int MSG_CHECK_MUTE_MUSIC = 35;
    private static final int MSG_IIL_SET_FORCE_BT_A2DP_USE = 5;
    private static final int MSG_IIL_SET_FORCE_USE = 4;
    private static final int MSG_II_SET_HEARING_AID_VOLUME = 14;
    private static final int MSG_II_SET_LE_AUDIO_OUT_VOLUME = 46;
    private static final int MSG_IL_BTA2DP_TIMEOUT = 10;
    private static final int MSG_IL_BTLEAUDIO_TIMEOUT = 49;
    private static final int MSG_IL_BT_SERVICE_CONNECTED_PROFILE = 23;
    private static final int MSG_IL_SAVE_NDEF_DEVICE_FOR_STRATEGY = 47;
    private static final int MSG_IL_SAVE_PREF_DEVICES_FOR_CAPTURE_PRESET = 37;
    private static final int MSG_IL_SAVE_PREF_DEVICES_FOR_STRATEGY = 32;
    private static final int MSG_IL_SAVE_REMOVE_NDEF_DEVICE_FOR_STRATEGY = 48;
    private static final int MSG_IL_SET_PREF_DEVICES_FOR_STRATEGY = 113;
    private static final int MSG_I_BROADCAST_BT_CONNECTION_STATE = 3;
    private static final int MSG_I_BT_DEVICE_CONNECTED_EVENT = 78;
    private static final int MSG_I_BT_SERVICE_DISCONNECTED_PROFILE = 22;
    private static final int MSG_I_SAVE_CLEAR_PREF_DEVICES_FOR_CAPTURE_PRESET = 38;
    private static final int MSG_I_SAVE_REMOVE_PREF_DEVICES_FOR_STRATEGY = 33;
    private static final int MSG_I_SCO_AUDIO_STATE_CHANGED = 44;
    private static final int MSG_I_SET_AVRCP_ABSOLUTE_VOLUME = 15;
    private static final int MSG_I_SET_MODE_OWNER = 16;
    private static final int MSG_I_SET_RESTART_SCO_AUDIO = 114;
    private static final int MSG_L_A2DP_ACTIVE_DEVICE_CHANGE_EXT = 64;
    private static final int MSG_L_A2DP_DEVICE_CONFIG_CHANGE_SHO = 77;
    private static final int MSG_L_A2DP_DEVICE_CONNECTION_CHANGE_EXT = 29;
    private static final int MSG_L_BLUETOOTH_DEVICE_CONFIG_CHANGE = 11;
    private static final int MSG_L_BT_ACTIVE_DEVICE_CHANGE_EXT = 45;
    private static final int MSG_L_COMMUNICATION_ROUTE_CLIENT_DIED = 34;
    private static final int MSG_L_HEARING_AID_DEVICE_CONNECTION_CHANGE_EXT = 31;
    private static final int MSG_L_NOTIFY_PREFERRED_AUDIOPROFILE_APPLIED = 52;
    private static final int MSG_L_SET_BT_ACTIVE_DEVICE = 7;
    private static final int MSG_L_SET_COMMUNICATION_DEVICE_FOR_CLIENT = 42;
    private static final int MSG_L_SET_WIRED_DEVICE_CONNECTION_STATE = 2;
    private static final int MSG_L_UPDATE_COMMUNICATION_ROUTE = 39;
    private static final int MSG_L_UPDATE_COMMUNICATION_ROUTE_CLIENT = 43;
    private static final int MSG_REPORT_NEW_ROUTES = 13;
    private static final int MSG_REPORT_NEW_ROUTES_A2DP = 36;
    private static final int MSG_RESTORE_DEVICES = 1;
    private static final int MSG_TOGGLE_HDMI = 6;
    private static final int SENDMSG_NOOP = 1;
    private static final int SENDMSG_QUEUE = 2;
    private static final int SENDMSG_REPLACE = 0;
    private static final long SET_COMMUNICATION_DEVICE_TIMEOUT_MS = 3000;
    private static final String TAG = "AS.AudioDeviceBroker";
    public static final long USE_SET_COMMUNICATION_DEVICE = 243827847;

    @GuardedBy({"sLastDeviceConnectionMsgTimeLock"})
    private static long sLastDeviceConnectMsgTime;
    private int mAccessibilityStrategyId;
    AudioDeviceInfo mActiveCommunicationDevice;
    private IAudioDeviceBrokerExt mAdbExt;
    private AudioDeviceBrokerWrapper mAdbWrapper;
    private AudioModeInfo mAudioModeOwner;
    private final AudioService mAudioService;
    private final AudioSystemAdapter mAudioSystem;

    @GuardedBy({"mDeviceStateLock"})
    private boolean mBluetoothA2dpEnabled;

    @GuardedBy({"mBluetoothAudioStateLock"})
    private boolean mBluetoothA2dpSuspendedApplied;

    @GuardedBy({"mBluetoothAudioStateLock"})
    private boolean mBluetoothA2dpSuspendedExt;

    @GuardedBy({"mBluetoothAudioStateLock"})
    private boolean mBluetoothA2dpSuspendedInt;
    private final Object mBluetoothAudioStateLock;

    @GuardedBy({"mBluetoothAudioStateLock"})
    private boolean mBluetoothLeSuspendedApplied;

    @GuardedBy({"mBluetoothAudioStateLock"})
    private boolean mBluetoothLeSuspendedExt;

    @GuardedBy({"mBluetoothAudioStateLock"})
    private boolean mBluetoothLeSuspendedInt;

    @GuardedBy({"mBluetoothAudioStateLock"})
    private boolean mBluetoothScoOn;

    @GuardedBy({"mBluetoothAudioStateLock"})
    private boolean mBluetoothScoOnApplied;
    private PowerManager.WakeLock mBrokerEventWakeLock;
    private BrokerHandler mBrokerHandler;
    private BrokerThread mBrokerThread;
    private final BtHelper mBtHelper;
    final RemoteCallbackList<ICommunicationDeviceDispatcher> mCommDevDispatchers;

    @GuardedBy({"mDeviceStateLock"})
    private final LinkedList<CommunicationRouteClient> mCommunicationRouteClients;
    int mCommunicationStrategyId;
    private final Context mContext;
    private AudioDeviceAttributes mCurCommunicationDevice;

    @GuardedBy({"mDeviceStateLock"})
    int mCurCommunicationPortId;
    private final AudioDeviceInventory mDeviceInventory;
    private final Object mDeviceStateLock;
    private AtomicBoolean mMusicMuted;
    private AudioDeviceAttributes mPreCommunicationDevice;
    private AudioDeviceAttributes mPreferredCommunicationDevice;
    private List<Integer> mSampleRateHD;
    private List<Integer> mSampleRateNormal;
    final Object mSetModeLock;
    private final SystemServerAdapter mSystemServer;
    private static final Object sLastDeviceConnectionMsgTimeLock = new Object();
    private static final int[] VALID_COMMUNICATION_DEVICE_TYPES = {2, 7, 3, 22, 1, 4, 23, 26, 11, 27, 5, 9, 19, 20};

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isMessageHandledUnderWakelock(int i) {
        return i == 2 || i == 29 || i == 31 || i == 35 || i == 49 || i == 64 || i == 6 || i == 7 || i == 10 || i == 11;
    }

    @GuardedBy({"mDeviceStateLock"})
    AudioDeviceAttributes getDefaultCommunicationDevice() {
        return null;
    }

    static {
        HashSet hashSet = new HashSet();
        MESSAGES_MUTE_MUSIC = hashSet;
        hashSet.add(7);
        hashSet.add(11);
        hashSet.add(45);
        hashSet.add(77);
        hashSet.add(29);
        hashSet.add(5);
        if (Build.isMtkPlatform()) {
            hashSet.add(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class AudioModeInfo {
        final int mMode;
        final int mPid;
        final int mUid;

        /* JADX INFO: Access modifiers changed from: package-private */
        public AudioModeInfo(int i, int i2, int i3) {
            this.mMode = i;
            this.mPid = i2;
            this.mUid = i3;
        }

        public String toString() {
            return "AudioModeInfo: mMode=" + AudioSystem.modeToString(this.mMode) + ", mPid=" + this.mPid + ", mUid=" + this.mUid;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioDeviceBroker(Context context, AudioService audioService, AudioSystemAdapter audioSystemAdapter) {
        this.mCommunicationStrategyId = -1;
        this.mAccessibilityStrategyId = -1;
        this.mDeviceStateLock = new Object();
        this.mSetModeLock = new Object();
        this.mAudioModeOwner = new AudioModeInfo(0, 0, 0);
        this.mAdbWrapper = new AudioDeviceBrokerWrapper();
        this.mAdbExt = (IAudioDeviceBrokerExt) ExtLoader.type(IAudioDeviceBrokerExt.class).base(this).create();
        this.mBluetoothAudioStateLock = new Object();
        this.mCommDevDispatchers = new RemoteCallbackList<>();
        this.mCurCommunicationPortId = -1;
        this.mMusicMuted = new AtomicBoolean(false);
        this.mCommunicationRouteClients = new LinkedList<>();
        this.mSampleRateNormal = new ArrayList();
        this.mSampleRateHD = new ArrayList();
        this.mContext = context;
        this.mAudioService = audioService;
        this.mBtHelper = new BtHelper(this);
        this.mDeviceInventory = new AudioDeviceInventory(this);
        this.mSystemServer = SystemServerAdapter.getDefaultAdapter(context);
        this.mAudioSystem = audioSystemAdapter;
        init();
        this.mAdbExt.initAdbExtInner(context, this);
    }

    AudioDeviceBroker(Context context, AudioService audioService, AudioDeviceInventory audioDeviceInventory, SystemServerAdapter systemServerAdapter, AudioSystemAdapter audioSystemAdapter) {
        this.mCommunicationStrategyId = -1;
        this.mAccessibilityStrategyId = -1;
        this.mDeviceStateLock = new Object();
        this.mSetModeLock = new Object();
        this.mAudioModeOwner = new AudioModeInfo(0, 0, 0);
        this.mAdbWrapper = new AudioDeviceBrokerWrapper();
        this.mAdbExt = (IAudioDeviceBrokerExt) ExtLoader.type(IAudioDeviceBrokerExt.class).base(this).create();
        this.mBluetoothAudioStateLock = new Object();
        this.mCommDevDispatchers = new RemoteCallbackList<>();
        this.mCurCommunicationPortId = -1;
        this.mMusicMuted = new AtomicBoolean(false);
        this.mCommunicationRouteClients = new LinkedList<>();
        this.mSampleRateNormal = new ArrayList();
        this.mSampleRateHD = new ArrayList();
        this.mContext = context;
        this.mAudioService = audioService;
        this.mBtHelper = new BtHelper(this);
        this.mDeviceInventory = audioDeviceInventory;
        this.mSystemServer = systemServerAdapter;
        this.mAudioSystem = audioSystemAdapter;
        init();
        this.mAdbExt.initAdbExtInner(context, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initRoutingStrategyIds() {
        List<AudioProductStrategy> audioProductStrategies = AudioProductStrategy.getAudioProductStrategies();
        this.mCommunicationStrategyId = -1;
        this.mAccessibilityStrategyId = -1;
        for (AudioProductStrategy audioProductStrategy : audioProductStrategies) {
            if (this.mCommunicationStrategyId == -1 && audioProductStrategy.getAudioAttributesForLegacyStreamType(0) != null) {
                this.mCommunicationStrategyId = audioProductStrategy.getId();
            }
            if (this.mAccessibilityStrategyId == -1 && audioProductStrategy.getAudioAttributesForLegacyStreamType(10) != null) {
                this.mAccessibilityStrategyId = audioProductStrategy.getId();
            }
        }
    }

    private void init() {
        setupMessaging(this.mContext);
        initAudioHalBluetoothState();
        initRoutingStrategyIds();
        this.mPreferredCommunicationDevice = null;
        updateActiveCommunicationDevice();
        this.mAdbExt.oplusHeadsetFadeInstantiate(this.mContext, this.mAudioService);
        this.mSystemServer.registerUserStartedReceiver(this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Context getContext() {
        return this.mContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSystemReady() {
        synchronized (this.mSetModeLock) {
            synchronized (this.mDeviceStateLock) {
                this.mAudioModeOwner = this.mAudioService.getAudioModeOwner();
                this.mBtHelper.onSystemReady();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAudioModeOwnerMode() {
        return this.mAudioModeOwner.mMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAudioServerDied() {
        sendMsgNoDelay(1, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForceUse_Async(int i, int i2, String str) {
        sendIILMsgNoDelay(4, 2, i, i2, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void toggleHdmiIfConnected_Async() {
        sendMsgNoDelay(6, 2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void disconnectAllBluetoothProfiles() {
        synchronized (this.mDeviceStateLock) {
            this.mBtHelper.disconnectAllBluetoothProfiles();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void receiveBtEvent(Intent intent) {
        synchronized (this.mSetModeLock) {
            synchronized (this.mDeviceStateLock) {
                this.mBtHelper.receiveBtEvent(intent);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBluetoothA2dpOn_Async(boolean z, String str) {
        synchronized (this.mDeviceStateLock) {
            if (this.mBluetoothA2dpEnabled == z) {
                return;
            }
            this.mBluetoothA2dpEnabled = z;
            this.mBrokerHandler.removeMessages(5);
            sendIILMsgNoDelay(5, 2, 1, this.mBluetoothA2dpEnabled ? 0 : 10, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSpeakerphoneOn(IBinder iBinder, int i, boolean z, String str) {
        if (AudioService.DEBUG_COMM_RTE) {
            Log.v(TAG, "setSpeakerphoneOn, on: " + z + " pid: " + i);
        }
        postSetCommunicationDeviceForClient(new CommunicationDeviceInfo(iBinder, i, new AudioDeviceAttributes(2, ""), z, -1, str, false));
    }

    private boolean hasCommunicationRouteClientForPidInt(int i, int i2) {
        AudioDeviceAttributes device;
        Iterator<CommunicationRouteClient> it = this.mCommunicationRouteClients.iterator();
        while (it.hasNext()) {
            CommunicationRouteClient next = it.next();
            if (next.getPid() == i && (device = next.getDevice()) != null && device.getType() == i2) {
                return true;
            }
        }
        return false;
    }

    private void removeCommunicationRouteClientsForPidInt(IBinder iBinder, int i, int i2) {
        AudioDeviceAttributes device;
        Iterator<CommunicationRouteClient> it = this.mCommunicationRouteClients.iterator();
        while (it.hasNext()) {
            CommunicationRouteClient next = it.next();
            if (next.getPid() == i && next.getBinder() != iBinder && (device = next.getDevice()) != null && device.getType() == i2) {
                Log.d(TAG, "removeSpeakerClientsForPid pid: " + i + " deviceType: " + i2);
                next.unregisterDeathRecipient();
                it.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasCommunicationRouteClientForPid(int i, int i2) {
        boolean hasCommunicationRouteClientForPidInt;
        synchronized (this.mDeviceStateLock) {
            hasCommunicationRouteClientForPidInt = hasCommunicationRouteClientForPidInt(i, i2);
        }
        return hasCommunicationRouteClientForPidInt;
    }

    IBinder getCommunicationRouteBluetoothScoCbForPid(int i) {
        synchronized (this.mDeviceStateLock) {
            CommunicationRouteClient communicationRouteClientForPid = getCommunicationRouteClientForPid(i);
            AudioDeviceAttributes device = communicationRouteClientForPid.getDevice();
            if (device == null || device.getType() != 7) {
                return null;
            }
            return communicationRouteClientForPid.getBinder();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setCommunicationDevice(IBinder iBinder, int i, AudioDeviceInfo audioDeviceInfo, String str) {
        boolean z;
        if (AudioService.DEBUG_COMM_RTE) {
            Log.v(TAG, "setCommunicationDevice, device: " + audioDeviceInfo + ", pid: " + i);
        }
        CommunicationDeviceInfo communicationDeviceInfo = new CommunicationDeviceInfo(iBinder, i, audioDeviceInfo != null ? new AudioDeviceAttributes(audioDeviceInfo) : null, audioDeviceInfo != null, -1, str, true);
        postSetCommunicationDeviceForClient(communicationDeviceInfo);
        synchronized (communicationDeviceInfo) {
            long currentTimeMillis = System.currentTimeMillis();
            long j = 0;
            while (communicationDeviceInfo.mWaitForStatus) {
                try {
                    communicationDeviceInfo.wait(3000 - j);
                } catch (InterruptedException unused) {
                    j = System.currentTimeMillis() - currentTimeMillis;
                    if (j >= 3000) {
                        communicationDeviceInfo.mStatus = false;
                        communicationDeviceInfo.mWaitForStatus = false;
                    }
                }
            }
            z = communicationDeviceInfo.mStatus;
        }
        return z;
    }

    @GuardedBy({"mDeviceStateLock"})
    boolean onSetCommunicationDeviceForClient(CommunicationDeviceInfo communicationDeviceInfo) {
        CommunicationRouteClient communicationRouteClientForPid;
        AudioDeviceAttributes audioDeviceAttributes;
        AudioDeviceAttributes audioDeviceAttributes2;
        if (AudioService.DEBUG_COMM_RTE) {
            Log.v(TAG, "onSetCommunicationDeviceForClient: " + communicationDeviceInfo);
        }
        if (!communicationDeviceInfo.mOn && ((communicationRouteClientForPid = getCommunicationRouteClientForPid(communicationDeviceInfo.mPid)) == null || (((audioDeviceAttributes = communicationDeviceInfo.mDevice) != null && !audioDeviceAttributes.equals(communicationRouteClientForPid.getDevice()) && communicationDeviceInfo.mDevice.getType() != 2) || ((audioDeviceAttributes2 = communicationDeviceInfo.mDevice) != null && audioDeviceAttributes2.getType() == 2 && communicationRouteClientForPid.getDevice().getType() != 2)))) {
            return false;
        }
        AudioDeviceAttributes audioDeviceAttributes3 = communicationDeviceInfo.mOn ? communicationDeviceInfo.mDevice : null;
        if (!communicationDeviceInfo.mEventSource.equals(IAudioDeviceBrokerExt.REMOVE_INACTIVE_ROUTE_CLIENT)) {
            this.mAdbExt.addAudioRouteEventTrack(communicationDeviceInfo.mPid, 1, -1, audioDeviceAttributes3 != null ? audioDeviceAttributes3.getType() : 0);
        }
        setCommunicationRouteForClient(communicationDeviceInfo.mCb, communicationDeviceInfo.mPid, audioDeviceAttributes3, communicationDeviceInfo.mScoAudioMode, communicationDeviceInfo.mEventSource);
        return true;
    }

    @GuardedBy({"mDeviceStateLock"})
    void setCommunicationRouteForClient(IBinder iBinder, int i, AudioDeviceAttributes audioDeviceAttributes, int i2, String str) {
        CommunicationRouteClient removeCommunicationRouteClient;
        if (AudioService.DEBUG_COMM_RTE) {
            Log.v(TAG, "setCommunicationRouteForClient: device: " + audioDeviceAttributes);
        }
        AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("setCommunicationRouteForClient for pid: " + i + " device: " + audioDeviceAttributes + " from API: " + str).printLog(TAG));
        boolean isBluetoothScoRequested = isBluetoothScoRequested();
        CommunicationRouteClient communicationRouteClientForPid = getCommunicationRouteClientForPid(i);
        AudioDeviceAttributes device = communicationRouteClientForPid != null ? communicationRouteClientForPid.getDevice() : null;
        if (audioDeviceAttributes != null) {
            if (this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported()) {
                getWrapper().clearRedundancyClient(i, iBinder);
            }
            removeCommunicationRouteClient = addCommunicationRouteClient(iBinder, i, audioDeviceAttributes);
            if (removeCommunicationRouteClient == null) {
                Log.w(TAG, "setCommunicationRouteForClient: could not add client for pid: " + i + " and device: " + audioDeviceAttributes);
            }
        } else {
            removeCommunicationRouteClient = removeCommunicationRouteClient(iBinder, true);
        }
        if (removeCommunicationRouteClient == null) {
            return;
        }
        boolean isBluetoothScoRequested2 = isBluetoothScoRequested();
        boolean z = Build.isMtkPlatform() ? !this.mBtHelper.isBluetoothScoOn() : true;
        if (isBluetoothScoRequested2 && ((!isBluetoothScoRequested || !isBluetoothScoActive()) && z)) {
            if (!this.mBtHelper.startBluetoothSco(i2, str)) {
                Log.w(TAG, "setCommunicationRouteForClient: failure to start BT SCO for pid: " + i);
                if (device != null) {
                    addCommunicationRouteClient(iBinder, i, device);
                } else {
                    removeCommunicationRouteClient(iBinder, true);
                }
                postBroadcastScoConnectionState(0);
            }
        } else if (isBluetoothScoRequested2 && isBluetoothScoRequested && !this.mBtHelper.isBluetoothScoOn()) {
            Log.w(TAG, "setCommunicationRouteForClient: when isBtScoRequested &: wasBtScoRequestedare true and BT SCO is off" + i);
            if (!this.mBtHelper.startBluetoothSco(i2, str)) {
                Log.w(TAG, "setCommunicationRouteForClient: failure to start BT SCO for pid: " + i);
                if (device != null) {
                    addCommunicationRouteClient(iBinder, i, device);
                } else {
                    removeCommunicationRouteClient(iBinder, true);
                }
                postBroadcastScoConnectionState(0);
            }
        } else if (!isBluetoothScoRequested2 && isBluetoothScoRequested) {
            this.mBtHelper.stopBluetoothSco(str);
        }
        if (isBluetoothLeAudioRequested() && audioDeviceAttributes != null) {
            if (getWrapper().getBluetoothVolSyncSupported() && (audioDeviceAttributes.getInternalType() == 536870912 || audioDeviceAttributes.getInternalType() == 536870913)) {
                int bluetoothContextualVolumeStream = this.mAudioService.getBluetoothContextualVolumeStream();
                if (bluetoothContextualVolumeStream == 0) {
                    bluetoothContextualVolumeStream = 6;
                }
                int vssVolumeForDevice = getVssVolumeForDevice(bluetoothContextualVolumeStream, audioDeviceAttributes.getInternalType());
                int maxVssVolumeForStream = getMaxVssVolumeForStream(bluetoothContextualVolumeStream);
                if (AudioService.DEBUG_COMM_RTE) {
                    Log.v(TAG, "setCommunicationRouteForClient restoring LE Audio device volume lvl.");
                }
                postSetLeAudioVolumeIndex(vssVolumeForDevice, maxVssVolumeForStream, bluetoothContextualVolumeStream);
            } else {
                int bluetoothContextualVolumeStream2 = this.mAudioService.getBluetoothContextualVolumeStream();
                int vssVolumeForDevice2 = getVssVolumeForDevice(bluetoothContextualVolumeStream2, audioDeviceAttributes.getInternalType());
                int maxVssVolumeForStream2 = getMaxVssVolumeForStream(bluetoothContextualVolumeStream2);
                if (AudioService.DEBUG_COMM_RTE) {
                    Log.v(TAG, "setCommunicationRouteForClient restoring LE Audio device volume lvl.");
                }
                postSetLeAudioVolumeIndex(vssVolumeForDevice2, maxVssVolumeForStream2, bluetoothContextualVolumeStream2);
            }
        }
        updateCommunicationRoute(str);
    }

    @GuardedBy({"mDeviceStateLock"})
    private CommunicationRouteClient topCommunicationRouteClient() {
        Iterator<CommunicationRouteClient> it = this.mCommunicationRouteClients.iterator();
        while (it.hasNext()) {
            CommunicationRouteClient next = it.next();
            if (next.getPid() == this.mAudioModeOwner.mPid) {
                return next;
            }
        }
        if (this.mCommunicationRouteClients.isEmpty() || this.mAudioModeOwner.mPid != 0) {
            return null;
        }
        if (this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported() && this.mAudioService.getWrapper().getExtImpl().selectRouteSetting(1, this.mCommunicationRouteClients.get(0).getPid(), 0, null)) {
            return null;
        }
        return this.mCommunicationRouteClients.get(0);
    }

    @GuardedBy({"mDeviceStateLock"})
    private AudioDeviceAttributes requestedCommunicationDevice() {
        CommunicationRouteClient communicationRouteClient = topCommunicationRouteClient();
        AudioDeviceAttributes device = communicationRouteClient != null ? communicationRouteClient.getDevice() : null;
        if (AudioService.DEBUG_COMM_RTE) {
            Log.v(TAG, "requestedCommunicationDevice: " + device + " mAudioModeOwner: " + this.mAudioModeOwner.toString());
        }
        return device;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isValidCommunicationDevice(AudioDeviceInfo audioDeviceInfo) {
        for (int i : VALID_COMMUNICATION_DEVICE_TYPES) {
            if (audioDeviceInfo.getType() == i) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<AudioDeviceInfo> getAvailableCommunicationDevices() {
        ArrayList arrayList = new ArrayList();
        for (AudioDeviceInfo audioDeviceInfo : AudioManager.getDevicesStatic(2)) {
            if (isValidCommunicationDevice(audioDeviceInfo)) {
                arrayList.add(audioDeviceInfo);
            }
        }
        return arrayList;
    }

    private AudioDeviceInfo getCommunicationDeviceOfType(final int i) {
        return getAvailableCommunicationDevices().stream().filter(new Predicate() { // from class: com.android.server.audio.AudioDeviceBroker$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getCommunicationDeviceOfType$0;
                lambda$getCommunicationDeviceOfType$0 = AudioDeviceBroker.lambda$getCommunicationDeviceOfType$0(i, (AudioDeviceInfo) obj);
                return lambda$getCommunicationDeviceOfType$0;
            }
        }).findFirst().orElse(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getCommunicationDeviceOfType$0(int i, AudioDeviceInfo audioDeviceInfo) {
        return audioDeviceInfo.getType() == i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioDeviceInfo getCommunicationDevice() {
        AudioDeviceInfo audioDeviceInfo;
        synchronized (this.mDeviceStateLock) {
            updateActiveCommunicationDevice();
            audioDeviceInfo = this.mActiveCommunicationDevice;
            if (audioDeviceInfo != null && audioDeviceInfo.getType() == 13) {
                audioDeviceInfo = getCommunicationDeviceOfType(2);
            }
            if ((audioDeviceInfo == null || !isValidCommunicationDevice(audioDeviceInfo)) && (audioDeviceInfo = getCommunicationDeviceOfType(1)) == null) {
                List<AudioDeviceInfo> availableCommunicationDevices = getAvailableCommunicationDevices();
                if (!availableCommunicationDevices.isEmpty()) {
                    audioDeviceInfo = availableCommunicationDevices.get(0);
                }
            }
        }
        return audioDeviceInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioDeviceInfo getCommunicationDeviceForClient() {
        AudioDeviceInfo audioDeviceInfo;
        synchronized (this.mDeviceStateLock) {
            AudioDeviceAttributes requestedCommunicationDevice = requestedCommunicationDevice();
            if (requestedCommunicationDevice != null) {
                if (AudioService.DEBUG_COMM_RTE) {
                    Log.d(TAG, "getCommunicationDeviceForClient, device=" + AudioSystem.getOutputDeviceName(requestedCommunicationDevice.getInternalType()));
                }
                this.mActiveCommunicationDevice = AudioManager.getDeviceInfoFromTypeAndAddress(requestedCommunicationDevice.getType(), requestedCommunicationDevice.getAddress());
            } else {
                if (AudioService.DEBUG_COMM_RTE) {
                    Log.d(TAG, "getCommunicationDeviceForClient, updateActiveCommunicationDevice");
                }
                updateActiveCommunicationDevice();
            }
            audioDeviceInfo = this.mActiveCommunicationDevice;
        }
        return audioDeviceInfo;
    }

    @GuardedBy({"mDeviceStateLock"})
    void updateActiveCommunicationDevice() {
        AudioDeviceAttributes preferredCommunicationDevice = preferredCommunicationDevice();
        if (preferredCommunicationDevice == null) {
            ArrayList<AudioDeviceAttributes> devicesForAttributes = this.mAudioSystem.getDevicesForAttributes(AudioProductStrategy.getAudioAttributesForStrategyWithLegacyStreamType(0), false);
            if (devicesForAttributes.isEmpty()) {
                if (this.mAudioService.isPlatformVoice()) {
                    Log.w(TAG, "updateActiveCommunicationDevice(): no device for phone strategy");
                }
                this.mActiveCommunicationDevice = null;
                return;
            }
            preferredCommunicationDevice = devicesForAttributes.get(0);
        }
        this.mActiveCommunicationDevice = AudioManager.getDeviceInfoFromTypeAndAddress(preferredCommunicationDevice.getType(), preferredCommunicationDevice.getAddress());
    }

    private boolean isDeviceRequestedForCommunication(int i) {
        boolean z;
        synchronized (this.mDeviceStateLock) {
            AudioDeviceAttributes requestedCommunicationDevice = requestedCommunicationDevice();
            z = false;
            if (i == 7 && !this.mCommunicationRouteClients.isEmpty() && (this.mAudioModeOwner.mPid == 0 || (this.mAudioService.getWrapper().getExtImpl().isPhoneCallIdle() && this.mAudioModeOwner.mPid == Process.myPid()))) {
                requestedCommunicationDevice = this.mCommunicationRouteClients.get(0).getDevice();
            }
            if (requestedCommunicationDevice != null && requestedCommunicationDevice.getType() == i) {
                z = true;
            }
        }
        return z;
    }

    private boolean isDeviceOnForCommunication(int i) {
        boolean z;
        synchronized (this.mDeviceStateLock) {
            AudioDeviceAttributes preferredCommunicationDevice = preferredCommunicationDevice();
            z = preferredCommunicationDevice != null && preferredCommunicationDevice.getType() == i;
        }
        return z;
    }

    private boolean isDeviceActiveForCommunication(int i) {
        AudioDeviceAttributes audioDeviceAttributes;
        AudioDeviceInfo audioDeviceInfo = this.mActiveCommunicationDevice;
        return audioDeviceInfo != null && audioDeviceInfo.getType() == i && (audioDeviceAttributes = this.mPreferredCommunicationDevice) != null && audioDeviceAttributes.getType() == i;
    }

    private boolean isSpeakerphoneRequested() {
        return isDeviceRequestedForCommunication(2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSpeakerphoneOn() {
        return isDeviceOnForCommunication(2);
    }

    private boolean isSpeakerphoneActive() {
        return isDeviceActiveForCommunication(2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isBluetoothScoRequested() {
        return isDeviceRequestedForCommunication(7);
    }

    boolean isBluetoothLeAudioRequested() {
        return isDeviceRequestedForCommunication(26) || isDeviceRequestedForCommunication(27);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isBluetoothScoOn() {
        return isDeviceOnForCommunication(7) || isDeviceOnForCommunication(26);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isBluetoothScoActive() {
        return isDeviceActiveForCommunication(7);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDeviceConnected(AudioDeviceAttributes audioDeviceAttributes) {
        boolean isDeviceConnected;
        synchronized (this.mDeviceStateLock) {
            isDeviceConnected = this.mDeviceInventory.isDeviceConnected(audioDeviceAttributes);
        }
        return isDeviceConnected;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWiredDeviceConnectionState(AudioDeviceAttributes audioDeviceAttributes, int i, String str) {
        boolean z;
        synchronized (this.mDeviceStateLock) {
            if (Build.isMtkPlatform()) {
                if (i == 0) {
                    synchronized (this.mDeviceStateLock) {
                        z = this.mBtHelper.isNextBtActiveDeviceAvailableForMusic();
                    }
                } else {
                    z = false;
                }
                this.mDeviceInventory.setWiredDeviceConnectionState(audioDeviceAttributes, i, str, z);
            } else {
                this.mDeviceInventory.setWiredDeviceConnectionState(audioDeviceAttributes, i, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWiredDeviceConnectionState(AudioDeviceAttributes audioDeviceAttributes, int i, String str, boolean z) {
        synchronized (this.mDeviceStateLock) {
            this.mDeviceInventory.setWiredDeviceConnectionState(audioDeviceAttributes, i, str, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTestDeviceConnectionState(AudioDeviceAttributes audioDeviceAttributes, int i) {
        synchronized (this.mDeviceStateLock) {
            this.mDeviceInventory.setTestDeviceConnectionState(audioDeviceAttributes, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restartScoInVoipCall() {
        this.mAudioService.mAsSocExt.restartScoInVoipCall();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class BleVolumeInfo {
        final int mIndex;
        final int mMaxIndex;
        final int mStreamType;

        BleVolumeInfo(int i, int i2, int i3) {
            this.mIndex = i;
            this.mMaxIndex = i2;
            this.mStreamType = i3;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class BtDeviceChangedData {
        final String mEventSource;
        final BluetoothProfileConnectionInfo mInfo;
        final BluetoothDevice mNewDevice;
        final BluetoothDevice mPreviousDevice;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BtDeviceChangedData(BluetoothDevice bluetoothDevice, BluetoothDevice bluetoothDevice2, BluetoothProfileConnectionInfo bluetoothProfileConnectionInfo, String str) {
            this.mNewDevice = bluetoothDevice;
            this.mPreviousDevice = bluetoothDevice2;
            this.mInfo = bluetoothProfileConnectionInfo;
            this.mEventSource = str;
        }

        public String toString() {
            return "BtDeviceChangedData profile=" + BluetoothProfile.getProfileName(this.mInfo.getProfile()) + ", switch device: [" + this.mPreviousDevice + "] -> [" + this.mNewDevice + "]";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class BtDeviceInfo {
        final int mAudioSystemDevice;
        final int mCodec;
        final BluetoothDevice mDevice;
        final String mEventSource;
        final boolean mIsLeOutput;
        final int mMusicDevice;
        final int mProfile;
        final int mState;
        final boolean mSupprNoisy;
        final int mVolume;

        BtDeviceInfo(BtDeviceChangedData btDeviceChangedData, BluetoothDevice bluetoothDevice, int i, int i2, int i3) {
            this.mDevice = bluetoothDevice;
            this.mState = i;
            this.mProfile = btDeviceChangedData.mInfo.getProfile();
            this.mSupprNoisy = btDeviceChangedData.mInfo.isSuppressNoisyIntent();
            this.mVolume = btDeviceChangedData.mInfo.getVolume();
            this.mIsLeOutput = btDeviceChangedData.mInfo.isLeOutput();
            this.mEventSource = btDeviceChangedData.mEventSource;
            this.mAudioSystemDevice = i2;
            this.mMusicDevice = 0;
            this.mCodec = i3;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public BtDeviceInfo(BluetoothDevice bluetoothDevice, int i) {
            this.mDevice = bluetoothDevice;
            this.mProfile = i;
            this.mEventSource = "";
            this.mMusicDevice = 0;
            this.mCodec = 0;
            this.mAudioSystemDevice = 0;
            this.mState = 0;
            this.mSupprNoisy = false;
            this.mVolume = -1;
            this.mIsLeOutput = false;
        }

        BtDeviceInfo(BluetoothDevice bluetoothDevice, int i, int i2, int i3, int i4) {
            this.mDevice = bluetoothDevice;
            this.mProfile = i;
            this.mEventSource = "";
            this.mMusicDevice = i3;
            this.mCodec = 0;
            this.mAudioSystemDevice = i4;
            this.mState = i2;
            this.mSupprNoisy = false;
            this.mVolume = -1;
            this.mIsLeOutput = false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public BtDeviceInfo(BtDeviceInfo btDeviceInfo, int i) {
            this.mDevice = btDeviceInfo.mDevice;
            this.mState = i;
            this.mProfile = btDeviceInfo.mProfile;
            this.mSupprNoisy = btDeviceInfo.mSupprNoisy;
            this.mVolume = btDeviceInfo.mVolume;
            this.mIsLeOutput = btDeviceInfo.mIsLeOutput;
            this.mEventSource = btDeviceInfo.mEventSource;
            this.mAudioSystemDevice = btDeviceInfo.mAudioSystemDevice;
            this.mMusicDevice = btDeviceInfo.mMusicDevice;
            this.mCodec = btDeviceInfo.mCodec;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof BtDeviceInfo)) {
                return false;
            }
            BtDeviceInfo btDeviceInfo = (BtDeviceInfo) obj;
            return this.mProfile == btDeviceInfo.mProfile && this.mDevice.equals(btDeviceInfo.mDevice);
        }
    }

    BtDeviceInfo createBtDeviceInfo(BtDeviceChangedData btDeviceChangedData, BluetoothDevice bluetoothDevice, int i) {
        int a2dpCodec;
        int i2;
        int profile = btDeviceChangedData.mInfo.getProfile();
        if (profile != 2) {
            a2dpCodec = 0;
            if (profile == 11) {
                i2 = AudioDevice.IN_BLUETOOTH_A2DP;
            } else if (profile == 26) {
                i2 = 536870914;
            } else if (profile == 21) {
                i2 = AudioFormat.OPUS;
            } else if (profile == 22) {
                i2 = btDeviceChangedData.mInfo.isLeOutput() ? AudioFormat.APTX : -1610612736;
            } else {
                throw new IllegalArgumentException("Invalid profile " + btDeviceChangedData.mInfo.getProfile());
            }
        } else {
            synchronized (this.mDeviceStateLock) {
                a2dpCodec = this.mBtHelper.getA2dpCodec(bluetoothDevice);
            }
            i2 = 128;
        }
        return new BtDeviceInfo(btDeviceChangedData, bluetoothDevice, i, i2, a2dpCodec);
    }

    private void btMediaMetricRecord(BluetoothDevice bluetoothDevice, String str, BtDeviceChangedData btDeviceChangedData) {
        new MediaMetrics.Item("audio.device.queueOnBluetoothActiveDeviceChanged").set(MediaMetrics.Property.STATE, str).set(MediaMetrics.Property.STATUS, Integer.valueOf(btDeviceChangedData.mInfo.getProfile())).set(MediaMetrics.Property.NAME, TextUtils.emptyIfNull(bluetoothDevice.getName())).record();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void queueOnBluetoothActiveDeviceChanged(BtDeviceChangedData btDeviceChangedData) {
        BluetoothDevice bluetoothDevice;
        boolean isQcomPlatform = Build.isQcomPlatform();
        if (btDeviceChangedData.mInfo.getProfile() == 2 && (bluetoothDevice = btDeviceChangedData.mPreviousDevice) != null && ((!isQcomPlatform && bluetoothDevice.equals(btDeviceChangedData.mNewDevice)) || (isQcomPlatform && btDeviceChangedData.mNewDevice != null))) {
            IAudioDeviceBrokerExt iAudioDeviceBrokerExt = this.mAdbExt;
            if (iAudioDeviceBrokerExt != null && !iAudioDeviceBrokerExt.checkPreviousDeviceIsConnected(btDeviceChangedData.mPreviousDevice, btDeviceChangedData.mInfo.getProfile())) {
                synchronized (this.mDeviceStateLock) {
                    BtDeviceInfo createBtDeviceInfo = createBtDeviceInfo(btDeviceChangedData, btDeviceChangedData.mNewDevice, 2);
                    this.mBrokerHandler.removeEqualMessages(45, createBtDeviceInfo);
                    this.mBrokerHandler.removeEqualMessages(7, createBtDeviceInfo);
                    Log.d(TAG, "restart connect the a2dp");
                    btMediaMetricRecord(btDeviceChangedData.mNewDevice, "connected", btDeviceChangedData);
                    sendLMsgNoDelay(45, 2, createBtDeviceInfo);
                }
                return;
            }
            new MediaMetrics.Item("audio.device.queueOnBluetoothActiveDeviceChanged_update").set(MediaMetrics.Property.NAME, TextUtils.emptyIfNull(btDeviceChangedData.mNewDevice.getName())).set(MediaMetrics.Property.STATUS, Integer.valueOf(btDeviceChangedData.mInfo.getProfile())).record();
            synchronized (this.mDeviceStateLock) {
                postBluetoothDeviceConfigChange(createBtDeviceInfo(btDeviceChangedData, btDeviceChangedData.mNewDevice, 2));
            }
            return;
        }
        synchronized (this.mDeviceStateLock) {
            BluetoothDevice bluetoothDevice2 = btDeviceChangedData.mPreviousDevice;
            if (bluetoothDevice2 != null) {
                btMediaMetricRecord(bluetoothDevice2, "disconnected", btDeviceChangedData);
                sendLMsgNoDelay(45, 2, createBtDeviceInfo(btDeviceChangedData, btDeviceChangedData.mPreviousDevice, 0));
            }
            BluetoothDevice bluetoothDevice3 = btDeviceChangedData.mNewDevice;
            if (bluetoothDevice3 != null) {
                btMediaMetricRecord(bluetoothDevice3, "connected", btDeviceChangedData);
                sendLMsgNoDelay(45, 2, createBtDeviceInfo(btDeviceChangedData, btDeviceChangedData.mNewDevice, 2));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetBluetoothScoOfApp() {
        if (AudioService.DEBUG_COMM_RTE) {
            Log.v(TAG, "resetBluetoothScoOfApp");
        }
        this.mAudioService.resetBluetoothScoOfApp();
    }

    private void initAudioHalBluetoothState() {
        synchronized (this.mBluetoothAudioStateLock) {
            this.mBluetoothScoOnApplied = false;
            AudioSystem.setParameters("BT_SCO=off");
            this.mBluetoothA2dpSuspendedApplied = false;
            AudioSystem.setParameters("A2dpSuspended=false");
            this.mBluetoothLeSuspendedApplied = false;
            AudioSystem.setParameters("LeAudioSuspended=false");
        }
    }

    @GuardedBy({"mBluetoothAudioStateLock"})
    private void updateAudioHalBluetoothState() {
        if (this.mBluetoothScoOn != this.mBluetoothScoOnApplied) {
            if (AudioService.DEBUG_COMM_RTE) {
                Log.v(TAG, "updateAudioHalBluetoothState() mBluetoothScoOn: " + this.mBluetoothScoOn + ", mBluetoothScoOnApplied: " + this.mBluetoothScoOnApplied);
            }
            if (this.mBluetoothScoOn) {
                if (!this.mBluetoothA2dpSuspendedApplied) {
                    AudioSystem.setParameters("A2dpSuspended=true");
                    this.mBluetoothA2dpSuspendedApplied = true;
                }
                if (!this.mBluetoothLeSuspendedApplied) {
                    AudioSystem.setParameters("LeAudioSuspended=true");
                    this.mBluetoothLeSuspendedApplied = true;
                }
                AudioSystem.setParameters("BT_SCO=on");
                this.mAudioService.setBinauralRecordParameters(false);
            } else {
                AudioSystem.setParameters("BT_SCO=off");
                this.mAudioService.setBinauralRecordParameters(true);
            }
            this.mBluetoothScoOnApplied = this.mBluetoothScoOn;
        }
        if (this.mBluetoothScoOnApplied) {
            return;
        }
        if ((this.mBluetoothA2dpSuspendedExt || this.mBluetoothA2dpSuspendedInt) != this.mBluetoothA2dpSuspendedApplied) {
            if (AudioService.DEBUG_COMM_RTE) {
                Log.v(TAG, "updateAudioHalBluetoothState() mBluetoothA2dpSuspendedExt: " + this.mBluetoothA2dpSuspendedExt + ", mBluetoothA2dpSuspendedInt: " + this.mBluetoothA2dpSuspendedInt + ", mBluetoothA2dpSuspendedApplied: " + this.mBluetoothA2dpSuspendedApplied);
            }
            boolean z = this.mBluetoothA2dpSuspendedExt || this.mBluetoothA2dpSuspendedInt;
            this.mBluetoothA2dpSuspendedApplied = z;
            if (z) {
                AudioSystem.setParameters("A2dpSuspended=true");
            } else {
                AudioSystem.setParameters("A2dpSuspended=false");
            }
        }
        if ((this.mBluetoothLeSuspendedExt || this.mBluetoothLeSuspendedInt) != this.mBluetoothLeSuspendedApplied) {
            if (AudioService.DEBUG_COMM_RTE) {
                Log.v(TAG, "updateAudioHalBluetoothState() mBluetoothLeSuspendedExt: " + this.mBluetoothLeSuspendedExt + ", mBluetoothLeSuspendedInt: " + this.mBluetoothLeSuspendedInt + ", mBluetoothLeSuspendedApplied: " + this.mBluetoothLeSuspendedApplied);
            }
            boolean z2 = this.mBluetoothLeSuspendedExt || this.mBluetoothLeSuspendedInt;
            this.mBluetoothLeSuspendedApplied = z2;
            if (z2) {
                AudioSystem.setParameters("LeAudioSuspended=true");
            } else {
                AudioSystem.setParameters("LeAudioSuspended=false");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBluetoothScoOn(boolean z, String str) {
        if (AudioService.DEBUG_COMM_RTE) {
            Log.v(TAG, "setBluetoothScoOn: " + z + " " + str);
        }
        if (!z) {
            this.mAudioService.resetBluetoothScoOfApp();
        }
        boolean equalsIgnoreCase = SystemProperties.get("persist.sys.oplus.bt.le_audio", "false").equalsIgnoreCase("true");
        synchronized (this.mBluetoothAudioStateLock) {
            this.mBluetoothScoOn = z;
            if (equalsIgnoreCase && z && !this.mBtHelper.isBluetoothScoOn()) {
                Log.v(TAG, "skip updateAudioHalBluetoothState if SCO is not on");
            } else {
                updateAudioHalBluetoothState();
            }
            postUpdateCommunicationRouteClient(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setA2dpSuspended(boolean z, boolean z2, String str) {
        synchronized (this.mBluetoothAudioStateLock) {
            if (AudioService.DEBUG_COMM_RTE) {
                Log.v(TAG, "setA2dpSuspended source: " + str + ", enable: " + z + ", internal: " + z2 + ", mBluetoothA2dpSuspendedInt: " + this.mBluetoothA2dpSuspendedInt + ", mBluetoothA2dpSuspendedExt: " + this.mBluetoothA2dpSuspendedExt);
            }
            if (z2) {
                this.mBluetoothA2dpSuspendedInt = z;
            } else {
                this.mBluetoothA2dpSuspendedExt = z;
            }
            updateAudioHalBluetoothState();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearA2dpSuspended(boolean z) {
        if (AudioService.DEBUG_COMM_RTE) {
            Log.v(TAG, "clearA2dpSuspended, internalOnly: " + z);
        }
        synchronized (this.mBluetoothAudioStateLock) {
            this.mBluetoothA2dpSuspendedInt = false;
            if (!z) {
                this.mBluetoothA2dpSuspendedExt = false;
            }
            updateAudioHalBluetoothState();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLeAudioSuspended(boolean z, boolean z2, String str) {
        synchronized (this.mBluetoothAudioStateLock) {
            if (AudioService.DEBUG_COMM_RTE) {
                Log.v(TAG, "setLeAudioSuspended source: " + str + ", enable: " + z + ", internal: " + z2 + ", mBluetoothLeSuspendedInt: " + this.mBluetoothA2dpSuspendedInt + ", mBluetoothLeSuspendedExt: " + this.mBluetoothA2dpSuspendedExt);
            }
            if (z2) {
                this.mBluetoothLeSuspendedInt = z;
            } else {
                this.mBluetoothLeSuspendedExt = z;
            }
            updateAudioHalBluetoothState();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearLeAudioSuspended(boolean z) {
        if (AudioService.DEBUG_COMM_RTE) {
            Log.v(TAG, "clearLeAudioSuspended, internalOnly: " + z);
        }
        synchronized (this.mBluetoothAudioStateLock) {
            this.mBluetoothLeSuspendedInt = false;
            if (!z) {
                this.mBluetoothLeSuspendedExt = false;
            }
            updateAudioHalBluetoothState();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver iAudioRoutesObserver) {
        AudioRoutesInfo startWatchingRoutes;
        synchronized (this.mDeviceStateLock) {
            startWatchingRoutes = this.mDeviceInventory.startWatchingRoutes(iAudioRoutesObserver);
        }
        return startWatchingRoutes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioRoutesInfo getCurAudioRoutes() {
        AudioRoutesInfo curAudioRoutes;
        synchronized (this.mDeviceStateLock) {
            curAudioRoutes = this.mDeviceInventory.getCurAudioRoutes();
        }
        return curAudioRoutes;
    }

    boolean isAvrcpAbsoluteVolumeSupported() {
        boolean isAvrcpAbsoluteVolumeSupported;
        synchronized (this.mDeviceStateLock) {
            isAvrcpAbsoluteVolumeSupported = this.mBtHelper.isAvrcpAbsoluteVolumeSupported();
        }
        return isAvrcpAbsoluteVolumeSupported;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isBluetoothA2dpOn() {
        boolean z;
        synchronized (this.mDeviceStateLock) {
            z = this.mBluetoothA2dpEnabled;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postSetAvrcpAbsoluteVolumeIndex(int i) {
        sendIMsgNoDelay(15, 0, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postSetHearingAidVolumeIndex(int i, int i2) {
        sendIIMsgNoDelay(14, 0, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postSetLeAudioVolumeIndex(int i, int i2, int i3) {
        sendLMsgNoDelay(46, 0, new BleVolumeInfo(i, i2, i3));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postSetModeOwner(int i, int i2, int i3) {
        sendLMsgNoDelay(16, 0, new AudioModeInfo(i, i2, i3));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postBluetoothDeviceConfigChange(BtDeviceInfo btDeviceInfo) {
        sendLMsgNoDelay(11, 2, btDeviceInfo);
    }

    void postBluetoothA2dpDeviceConfigChange(BtDeviceInfo btDeviceInfo) {
        sendLMsgNoDelay(77, 2, btDeviceInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startBluetoothScoForClient(IBinder iBinder, int i, int i2, String str) {
        if (AudioService.DEBUG_COMM_RTE) {
            Log.v(TAG, "startBluetoothScoForClient, pid: " + i);
        }
        if (!Build.isMtkPlatform() && this.mDeviceInventory.isLeConnected()) {
            for (AudioDeviceInfo audioDeviceInfo : AudioManager.getDevicesStatic(2)) {
                if (audioDeviceInfo.getType() == 26) {
                    setCommunicationDevice(iBinder, i, audioDeviceInfo, str);
                    Log.d(TAG, "startBluetoothLe, pid: " + i);
                    return;
                }
            }
        }
        postSetCommunicationDeviceForClient(new CommunicationDeviceInfo(iBinder, i, new AudioDeviceAttributes(16, ""), true, i2, str, false));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopBluetoothScoForClient(IBinder iBinder, int i, String str) {
        if (AudioService.DEBUG_COMM_RTE) {
            Log.v(TAG, "stopBluetoothScoForClient, pid: " + i);
        }
        if (!Build.isMtkPlatform()) {
            synchronized (this.mSetModeLock) {
                synchronized (this.mDeviceStateLock) {
                    CommunicationRouteClient communicationRouteClientForPid = getCommunicationRouteClientForPid(i);
                    if (communicationRouteClientForPid == null) {
                        return;
                    }
                    AudioDeviceAttributes device = communicationRouteClientForPid.getDevice();
                    boolean z = device != null && device.getType() == 26;
                    if (z) {
                        setCommunicationDevice(iBinder, i, null, str);
                        Log.d(TAG, "stopBluetoothLe, pid: " + i);
                        return;
                    }
                }
            }
        }
        postSetCommunicationDeviceForClient(new CommunicationDeviceInfo(iBinder, i, new AudioDeviceAttributes(16, ""), false, -1, str, false));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int setPreferredDevicesForStrategySync(int i, List<AudioDeviceAttributes> list) {
        return this.mDeviceInventory.setPreferredDevicesForStrategyAndSave(i, list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int removePreferredDevicesForStrategySync(int i) {
        return this.mDeviceInventory.removePreferredDevicesForStrategyAndSave(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int setDeviceAsNonDefaultForStrategySync(int i, AudioDeviceAttributes audioDeviceAttributes) {
        return this.mDeviceInventory.setDeviceAsNonDefaultForStrategyAndSave(i, audioDeviceAttributes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int removeDeviceAsNonDefaultForStrategySync(int i, AudioDeviceAttributes audioDeviceAttributes) {
        return this.mDeviceInventory.removeDeviceAsNonDefaultForStrategyAndSave(i, audioDeviceAttributes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerStrategyPreferredDevicesDispatcher(IStrategyPreferredDevicesDispatcher iStrategyPreferredDevicesDispatcher) {
        this.mDeviceInventory.registerStrategyPreferredDevicesDispatcher(iStrategyPreferredDevicesDispatcher);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterStrategyPreferredDevicesDispatcher(IStrategyPreferredDevicesDispatcher iStrategyPreferredDevicesDispatcher) {
        this.mDeviceInventory.unregisterStrategyPreferredDevicesDispatcher(iStrategyPreferredDevicesDispatcher);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerStrategyNonDefaultDevicesDispatcher(IStrategyNonDefaultDevicesDispatcher iStrategyNonDefaultDevicesDispatcher) {
        this.mDeviceInventory.registerStrategyNonDefaultDevicesDispatcher(iStrategyNonDefaultDevicesDispatcher);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterStrategyNonDefaultDevicesDispatcher(IStrategyNonDefaultDevicesDispatcher iStrategyNonDefaultDevicesDispatcher) {
        this.mDeviceInventory.unregisterStrategyNonDefaultDevicesDispatcher(iStrategyNonDefaultDevicesDispatcher);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int setPreferredDevicesForCapturePresetSync(int i, List<AudioDeviceAttributes> list) {
        return this.mDeviceInventory.setPreferredDevicesForCapturePresetAndSave(i, list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int clearPreferredDevicesForCapturePresetSync(int i) {
        return this.mDeviceInventory.clearPreferredDevicesForCapturePresetAndSave(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerCapturePresetDevicesRoleDispatcher(ICapturePresetDevicesRoleDispatcher iCapturePresetDevicesRoleDispatcher) {
        this.mDeviceInventory.registerCapturePresetDevicesRoleDispatcher(iCapturePresetDevicesRoleDispatcher);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterCapturePresetDevicesRoleDispatcher(ICapturePresetDevicesRoleDispatcher iCapturePresetDevicesRoleDispatcher) {
        this.mDeviceInventory.unregisterCapturePresetDevicesRoleDispatcher(iCapturePresetDevicesRoleDispatcher);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerCommunicationDeviceDispatcher(ICommunicationDeviceDispatcher iCommunicationDeviceDispatcher) {
        this.mCommDevDispatchers.register(iCommunicationDeviceDispatcher);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterCommunicationDeviceDispatcher(ICommunicationDeviceDispatcher iCommunicationDeviceDispatcher) {
        this.mCommDevDispatchers.unregister(iCommunicationDeviceDispatcher);
    }

    @GuardedBy({"mDeviceStateLock"})
    private void dispatchCommunicationDevice() {
        AudioDeviceInfo communicationDevice = getCommunicationDevice();
        int id = communicationDevice != null ? communicationDevice.getId() : 0;
        if (id == this.mCurCommunicationPortId) {
            return;
        }
        this.mCurCommunicationPortId = id;
        int beginBroadcast = this.mCommDevDispatchers.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                this.mCommDevDispatchers.getBroadcastItem(i).dispatchCommunicationDeviceChanged(id);
            } catch (RemoteException unused) {
            }
        }
        this.mCommDevDispatchers.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postAccessoryPlugMediaUnmute(int i) {
        this.mAudioService.postAccessoryPlugMediaUnmute(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVssVolumeForDevice(int i, int i2) {
        return this.mAudioService.getVssVolumeForDevice(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMaxVssVolumeForStream(int i) {
        return this.mAudioService.getMaxVssVolumeForStream(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDeviceForStream(int i) {
        return this.mAudioService.getDeviceForStream(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postApplyVolumeOnDevice(int i, int i2, String str) {
        this.mAudioService.postApplyVolumeOnDevice(i, i2, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postSetVolumeIndexOnDevice(int i, int i2, int i3, String str) {
        this.mAudioService.postSetVolumeIndexOnDevice(i, i2, i3, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postObserveDevicesForAllStreams() {
        this.mAudioService.postObserveDevicesForAllStreams();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInCommunication() {
        return this.mAudioService.isInCommunication();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasMediaDynamicPolicy() {
        return this.mAudioService.hasMediaDynamicPolicy();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentResolver getContentResolver() {
        return this.mAudioService.getContentResolver();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkMusicActive(int i, String str) {
        this.mAudioService.checkMusicActive(i, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkVolumeCecOnHdmiConnection(int i, String str) {
        this.mAudioService.postCheckVolumeCecOnHdmiConnection(i, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasAudioFocusUsers() {
        return this.mAudioService.hasAudioFocusUsers();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postBroadcastScoConnectionState(int i) {
        sendIMsgNoDelay(3, 2, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postBroadcastBecomingNoisy() {
        sendMsgNoDelay(12, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mDeviceStateLock"})
    public void postBluetoothActiveDevice(BtDeviceInfo btDeviceInfo, int i) {
        sendLMsg(7, 2, btDeviceInfo, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postSetWiredDeviceConnectionState(AudioDeviceInventory.WiredDeviceConnectionState wiredDeviceConnectionState, int i) {
        sendLMsg(2, 2, wiredDeviceConnectionState, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postBtProfileDisconnected(int i) {
        sendIMsgNoDelay(22, 2, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postBtProfileConnected(int i, BluetoothProfile bluetoothProfile) {
        sendILMsgNoDelay(23, 2, i, bluetoothProfile);
    }

    void postCommunicationRouteClientDied(CommunicationRouteClient communicationRouteClient) {
        sendLMsgNoDelay(34, 2, communicationRouteClient);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postSaveSetPreferredDevicesForStrategy(int i, List<AudioDeviceAttributes> list) {
        sendILMsgNoDelay(32, 2, i, list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postSaveRemovePreferredDevicesForStrategy(int i) {
        sendIMsgNoDelay(33, 2, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postSaveSetDeviceAsNonDefaultForStrategy(int i, AudioDeviceAttributes audioDeviceAttributes) {
        sendILMsgNoDelay(47, 2, i, audioDeviceAttributes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postSaveRemoveDeviceAsNonDefaultForStrategy(int i, AudioDeviceAttributes audioDeviceAttributes) {
        sendILMsgNoDelay(48, 2, i, audioDeviceAttributes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postSaveSetPreferredDevicesForCapturePreset(int i, List<AudioDeviceAttributes> list) {
        sendILMsgNoDelay(37, 2, i, list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postSaveClearPreferredDevicesForCapturePreset(int i) {
        sendIMsgNoDelay(38, 2, i);
    }

    void postBtDeviceConnectedEvent(BluetoothDevice bluetoothDevice, int i) {
        sendLMsg(78, 0, bluetoothDevice, i);
    }

    void postUpdateCommunicationRouteClient(String str) {
        sendLMsgNoDelay(43, 2, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postUpdateCommunicationRoute(String str) {
        sendLMsgNoDelay(39, 2, str);
    }

    void postSetCommunicationDeviceForClient(CommunicationDeviceInfo communicationDeviceInfo) {
        sendLMsgNoDelay(42, 2, communicationDeviceInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postScoAudioStateChanged(int i) {
        sendIMsgNoDelay(44, 2, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postNotifyPreferredAudioProfileApplied(BluetoothDevice bluetoothDevice) {
        sendLMsgNoDelay(52, 2, bluetoothDevice);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class CommunicationDeviceInfo {
        final IBinder mCb;
        final AudioDeviceAttributes mDevice;
        final String mEventSource;
        final boolean mOn;
        final int mPid;
        final int mScoAudioMode;
        boolean mStatus = false;
        boolean mWaitForStatus;

        CommunicationDeviceInfo(IBinder iBinder, int i, AudioDeviceAttributes audioDeviceAttributes, boolean z, int i2, String str, boolean z2) {
            this.mCb = iBinder;
            this.mPid = i;
            this.mDevice = audioDeviceAttributes;
            this.mOn = z;
            this.mScoAudioMode = i2;
            this.mEventSource = str;
            this.mWaitForStatus = z2;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CommunicationDeviceInfo)) {
                return false;
            }
            CommunicationDeviceInfo communicationDeviceInfo = (CommunicationDeviceInfo) obj;
            return this.mCb.equals(communicationDeviceInfo.mCb) && this.mPid == communicationDeviceInfo.mPid;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("CommunicationDeviceInfo mCb=");
            sb.append(this.mCb.toString());
            sb.append(" mPid=");
            sb.append(this.mPid);
            sb.append(" mDevice=[");
            AudioDeviceAttributes audioDeviceAttributes = this.mDevice;
            sb.append(audioDeviceAttributes != null ? audioDeviceAttributes.toString() : "null");
            sb.append("] mOn=");
            sb.append(this.mOn);
            sb.append(" mScoAudioMode=");
            sb.append(this.mScoAudioMode);
            sb.append(" mEventSource=");
            sb.append(this.mEventSource);
            sb.append(" mWaitForStatus=");
            sb.append(this.mWaitForStatus);
            sb.append(" mStatus=");
            sb.append(this.mStatus);
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBluetoothA2dpOnInt(boolean z, boolean z2, String str) {
        String str2 = "setBluetoothA2dpOn(" + z + ") from u/pid:" + Binder.getCallingUid() + "/" + Binder.getCallingPid() + " src:" + str;
        synchronized (this.mDeviceStateLock) {
            this.mBluetoothA2dpEnabled = z;
            this.mBrokerHandler.removeMessages(5);
            onSetForceUse(1, this.mBluetoothA2dpEnabled ? 0 : 10, z2, str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean handleDeviceConnection(AudioDeviceAttributes audioDeviceAttributes, boolean z, BluetoothDevice bluetoothDevice) {
        boolean handleDeviceConnection;
        synchronized (this.mDeviceStateLock) {
            handleDeviceConnection = this.mDeviceInventory.handleDeviceConnection(audioDeviceAttributes, z, false, bluetoothDevice);
        }
        return handleDeviceConnection;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleFailureToConnectToBtHeadsetService(int i) {
        sendMsg(9, 0, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleCancelFailureToConnectToBtHeadsetService() {
        this.mBrokerHandler.removeMessages(9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postReportNewRoutes(boolean z) {
        sendMsgNoDelay(z ? 36 : 13, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasScheduledA2dpConnection(BluetoothDevice bluetoothDevice) {
        return this.mBrokerHandler.hasEqualMessages(7, new BtDeviceInfo(bluetoothDevice, 2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setA2dpTimeout(String str, int i, int i2) {
        sendILMsg(10, 2, i, str, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLeAudioTimeout(String str, int i, int i2) {
        sendILMsg(49, 2, i, str, i2);
    }

    void setAvrcpAbsoluteVolumeSupported(boolean z) {
        boolean isMtkPlatform = Build.isMtkPlatform();
        Log.d(TAG, "isMtk" + isMtkPlatform);
        if (isMtkPlatform) {
            this.mBtHelper.setAvrcpAbsoluteVolumeSupported(z);
            return;
        }
        synchronized (this.mDeviceStateLock) {
            this.mBtHelper.setAvrcpAbsoluteVolumeSupported(z);
        }
    }

    void clearAvrcpAbsoluteVolumeSupported() {
        setAvrcpAbsoluteVolumeSupported(false);
        this.mAudioService.setAvrcpAbsoluteVolumeSupported(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getBluetoothA2dpEnabled() {
        boolean z;
        synchronized (this.mDeviceStateLock) {
            z = this.mBluetoothA2dpEnabled;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void broadcastStickyIntentToCurrentProfileGroup(Intent intent) {
        this.mSystemServer.broadcastStickyIntentToCurrentProfileGroup(intent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(final PrintWriter printWriter, final String str) {
        if (this.mBrokerHandler != null) {
            printWriter.println(str + "Message handler (watch for unhandled messages):");
            this.mBrokerHandler.dump(new PrintWriterPrinter(printWriter), str + "  ");
        } else {
            printWriter.println("Message handler is null");
        }
        this.mDeviceInventory.dump(printWriter, str);
        printWriter.println("\n" + str + "Communication route clients:");
        this.mCommunicationRouteClients.forEach(new Consumer() { // from class: com.android.server.audio.AudioDeviceBroker$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AudioDeviceBroker.lambda$dump$1(printWriter, str, (AudioDeviceBroker.CommunicationRouteClient) obj);
            }
        });
        printWriter.println("\n" + str + "Computed Preferred communication device: " + preferredCommunicationDevice());
        printWriter.println("\n" + str + "Applied Preferred communication device: " + this.mPreferredCommunicationDevice);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("Active communication device: ");
        sb.append((Object) (this.mActiveCommunicationDevice == null ? "None" : new AudioDeviceAttributes(this.mActiveCommunicationDevice)));
        printWriter.println(sb.toString());
        printWriter.println(str + "mCommunicationStrategyId: " + this.mCommunicationStrategyId);
        printWriter.println(str + "mAccessibilityStrategyId: " + this.mAccessibilityStrategyId);
        printWriter.println("\n" + str + "mAudioModeOwner: " + this.mAudioModeOwner);
        this.mBtHelper.dump(printWriter, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$1(PrintWriter printWriter, String str, CommunicationRouteClient communicationRouteClient) {
        printWriter.println("  " + str + "pid: " + communicationRouteClient.getPid() + " device: " + communicationRouteClient.getDevice() + " cb: " + communicationRouteClient.getBinder());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSetForceUse(int i, int i2, boolean z, String str) {
        if (i == 1) {
            postReportNewRoutes(z);
        }
        AudioService.sForceUseLogger.enqueue(new AudioServiceEvents.ForceUseEvent(i, i2, str));
        new MediaMetrics.Item("audio.forceUse." + AudioSystem.forceUseUsageToString(i)).set(MediaMetrics.Property.EVENT, "onSetForceUse").set(MediaMetrics.Property.FORCE_USE_DUE_TO, str).set(MediaMetrics.Property.FORCE_USE_MODE, AudioSystem.forceUseConfigToString(i2)).record();
        if (AudioService.DEBUG_COMM_RTE) {
            Log.v(TAG, "onSetForceUse(useCase<" + AudioSystem.forceUseUsageToString(i) + ">, config<" + AudioSystem.forceUseConfigToString(i2) + ">, fromA2dp<" + z + ">, eventSource<" + str + ">)");
        }
        this.mAudioSystem.setForceUse(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSendBecomingNoisyIntent() {
        AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("broadcast ACTION_AUDIO_BECOMING_NOISY").printLog(TAG));
        this.mSystemServer.sendDeviceBecomingNoisyIntent();
    }

    private void setupMessaging(Context context) {
        this.mBrokerEventWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "handleAudioDeviceEvent");
        BrokerThread brokerThread = new BrokerThread();
        this.mBrokerThread = brokerThread;
        brokerThread.start();
        waitForBrokerHandlerCreation();
    }

    private void waitForBrokerHandlerCreation() {
        synchronized (this) {
            while (this.mBrokerHandler == null) {
                try {
                    wait();
                } catch (InterruptedException unused) {
                    Log.e(TAG, "Interruption while waiting on BrokerHandler");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class BrokerThread extends Thread {
        Looper mLooper;

        BrokerThread() {
            super("AudioDeviceBroker");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            synchronized (AudioDeviceBroker.this) {
                AudioDeviceBroker.this.mBrokerHandler = new BrokerHandler();
                this.mLooper = Looper.myLooper();
                AudioDeviceBroker.this.notify();
            }
            Looper.loop();
        }

        public Looper getLooper() {
            return this.mLooper;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class BrokerHandler extends Handler {
        private BrokerHandler() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Failed to find 'out' block for switch in B:19:0x002d. Please report as an issue. */
        /* JADX WARN: Removed duplicated region for block: B:248:0x0349  */
        @Override // android.os.Handler
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void handleMessage(Message message) {
            boolean onSetCommunicationDeviceForClient;
            int i;
            int i2 = message.what;
            if (i2 != 22) {
                if (i2 != 23) {
                    if (i2 == 52) {
                        BtHelper.onNotifyPreferredAudioProfileApplied((BluetoothDevice) message.obj);
                    } else if (i2 == 64) {
                        BtDeviceInfo btDeviceInfo = (BtDeviceInfo) message.obj;
                        AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("handleBluetoothA2dpActiveDeviceChangeExt  state=" + btDeviceInfo.mState + " prof=" + btDeviceInfo.mProfile + " supprNoisy=" + btDeviceInfo.mSupprNoisy + " vol=" + btDeviceInfo.mVolume).printLog(AudioDeviceBroker.TAG));
                        synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                            AudioDeviceBroker.this.mDeviceInventory.handleBluetoothA2dpActiveDeviceChangeExt(btDeviceInfo.mDevice, btDeviceInfo.mState, btDeviceInfo.mProfile, btDeviceInfo.mSupprNoisy, btDeviceInfo.mVolume);
                        }
                    } else if (i2 == 113) {
                        AudioDeviceAttributes headsetAudioDevice = AudioDeviceBroker.this.getHeadsetAudioDevice();
                        if (headsetAudioDevice != null) {
                            AudioDeviceBroker.this.mDeviceInventory.setPreferredDevicesForStrategyInt(AudioDeviceBroker.this.mCommunicationStrategyId, Arrays.asList(headsetAudioDevice));
                        } else if (AudioService.DEBUG_DEVICES) {
                            Log.d(AudioDeviceBroker.TAG, "HFP inband ring tone, prefer device is null");
                        }
                    } else if (i2 == 77) {
                        BtDeviceInfo btDeviceInfo2 = (BtDeviceInfo) message.obj;
                        if (btDeviceInfo2.mDevice != null) {
                            synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                AudioDeviceBroker.this.mDeviceInventory.onBluetoothDeviceConfigChange(btDeviceInfo2, 0);
                            }
                        }
                    } else if (i2 != 78) {
                        if (i2 == 81) {
                            synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                AudioDeviceBroker.this.getWrapper().getExtImpl().addAudioRouteEventTrack(0, 2, -1, 0);
                            }
                        } else if (i2 != 82) {
                            switch (i2) {
                                case 1:
                                    synchronized (AudioDeviceBroker.this.mSetModeLock) {
                                        synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                            AudioDeviceBroker.this.initRoutingStrategyIds();
                                            AudioDeviceBroker.this.updateActiveCommunicationDevice();
                                            AudioDeviceBroker.this.mDeviceInventory.onRestoreDevices();
                                            AudioDeviceBroker.this.mBtHelper.onAudioServerDiedRestoreA2dp();
                                            AudioDeviceBroker.this.updateCommunicationRoute("MSG_RESTORE_DEVICES");
                                        }
                                        break;
                                    }
                                case 2:
                                    synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                        AudioDeviceBroker.this.mDeviceInventory.onSetWiredDeviceConnectionState((AudioDeviceInventory.WiredDeviceConnectionState) message.obj);
                                    }
                                    break;
                                case 3:
                                    synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                        AudioDeviceBroker.this.mBtHelper.onBroadcastScoConnectionState(message.arg1);
                                    }
                                    break;
                                case 4:
                                case 5:
                                    AudioDeviceBroker.this.onSetForceUse(message.arg1, message.arg2, i2 == 5, (String) message.obj);
                                    break;
                                case 6:
                                    synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                        AudioDeviceBroker.this.mDeviceInventory.onToggleHdmi();
                                    }
                                    break;
                                case 7:
                                    synchronized (AudioDeviceBroker.this.mSetModeLock) {
                                        synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                            BtDeviceInfo btDeviceInfo3 = (BtDeviceInfo) message.obj;
                                            AudioDeviceBroker.this.mDeviceInventory.onSetBtActiveDevice(btDeviceInfo3, (btDeviceInfo3.mProfile != 22 || btDeviceInfo3.mIsLeOutput) ? AudioDeviceBroker.this.mAudioService.getBluetoothContextualVolumeStream() : -1);
                                            int i3 = btDeviceInfo3.mProfile;
                                            if (i3 == 22 || i3 == 21) {
                                                AudioDeviceBroker.this.onUpdateCommunicationRouteClient(IAudioDeviceBrokerExt.SET_BLUETOOTH_ACTIVE_DEVICE);
                                            }
                                        }
                                        break;
                                    }
                                    break;
                                default:
                                    switch (i2) {
                                        case 9:
                                            synchronized (AudioDeviceBroker.this.mSetModeLock) {
                                                synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                    AudioDeviceBroker.this.mBtHelper.resetBluetoothSco();
                                                }
                                                break;
                                            }
                                        case 10:
                                            synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                AudioDeviceBroker.this.mDeviceInventory.onMakeA2dpDeviceUnavailableNow((String) message.obj, message.arg1);
                                            }
                                            break;
                                        case 11:
                                            synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                AudioDeviceBroker.this.mDeviceInventory.onBluetoothDeviceConfigChange((BtDeviceInfo) message.obj, 0);
                                            }
                                            break;
                                        case 12:
                                            AudioDeviceBroker.this.onSendBecomingNoisyIntent();
                                            break;
                                        case 13:
                                            synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                AudioDeviceBroker.this.mDeviceInventory.onReportNewRoutes();
                                            }
                                            break;
                                        case 14:
                                            synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                AudioDeviceBroker.this.mBtHelper.setHearingAidVolume(message.arg1, message.arg2, AudioDeviceBroker.this.mDeviceInventory.isHearingAidConnected());
                                            }
                                            break;
                                        case 15:
                                            synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                AudioDeviceBroker.this.mBtHelper.setAvrcpAbsoluteVolumeIndex(message.arg1);
                                            }
                                            break;
                                        case 16:
                                            synchronized (AudioDeviceBroker.this.mSetModeLock) {
                                                synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                    AudioDeviceBroker.this.mAudioModeOwner = (AudioModeInfo) message.obj;
                                                    if (AudioDeviceBroker.this.mAudioModeOwner.mMode != 1) {
                                                        AudioDeviceBroker.this.onUpdateCommunicationRouteClient("setNewModeOwner");
                                                    }
                                                    if (AudioDeviceBroker.this.mAudioModeOwner.mMode == 0) {
                                                        if (AudioDeviceBroker.this.mAudioService.isVendorBeforeAndroidU() && (AudioDeviceBroker.this.isBleHDRecordActive() || !AudioDeviceBroker.this.isBleRecordingIdle())) {
                                                            AudioDeviceBroker.this.mAudioService.mAsSocExt.restartBleRecord();
                                                        } else if (AudioDeviceBroker.this.isBleRecordingIdle()) {
                                                            AudioDeviceBroker.this.mBtHelper.updateBleCGStateToBt(false);
                                                        }
                                                    }
                                                }
                                                break;
                                            }
                                        default:
                                            switch (i2) {
                                                case 32:
                                                    AudioDeviceBroker.this.mDeviceInventory.onSaveSetPreferredDevices(message.arg1, (List) message.obj);
                                                    break;
                                                case 33:
                                                    AudioDeviceBroker.this.mDeviceInventory.onSaveRemovePreferredDevices(message.arg1);
                                                    break;
                                                case 34:
                                                    synchronized (AudioDeviceBroker.this.mSetModeLock) {
                                                        synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                            AudioDeviceBroker.this.onCommunicationRouteClientDied((CommunicationRouteClient) message.obj);
                                                        }
                                                        break;
                                                    }
                                                case 35:
                                                    AudioDeviceBroker.this.checkMessagesMuteMusic(0);
                                                    break;
                                                case 36:
                                                    break;
                                                case 37:
                                                    AudioDeviceBroker.this.mDeviceInventory.onSaveSetPreferredDevicesForCapturePreset(message.arg1, (List) message.obj);
                                                    break;
                                                case 38:
                                                    AudioDeviceBroker.this.mDeviceInventory.onSaveClearPreferredDevicesForCapturePreset(message.arg1);
                                                    break;
                                                case 39:
                                                    synchronized (AudioDeviceBroker.this.mSetModeLock) {
                                                        synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                            AudioDeviceBroker.this.updateCommunicationRoute((String) message.obj);
                                                        }
                                                        break;
                                                    }
                                                default:
                                                    switch (i2) {
                                                        case 42:
                                                            CommunicationDeviceInfo communicationDeviceInfo = (CommunicationDeviceInfo) message.obj;
                                                            synchronized (AudioDeviceBroker.this.mSetModeLock) {
                                                                synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                                    onSetCommunicationDeviceForClient = AudioDeviceBroker.this.onSetCommunicationDeviceForClient(communicationDeviceInfo);
                                                                }
                                                            }
                                                            synchronized (communicationDeviceInfo) {
                                                                if (communicationDeviceInfo.mWaitForStatus) {
                                                                    communicationDeviceInfo.mStatus = onSetCommunicationDeviceForClient;
                                                                    communicationDeviceInfo.mWaitForStatus = false;
                                                                    communicationDeviceInfo.notify();
                                                                }
                                                            }
                                                            break;
                                                        case 43:
                                                            synchronized (AudioDeviceBroker.this.mSetModeLock) {
                                                                synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                                    AudioDeviceBroker.this.onUpdateCommunicationRouteClient((String) message.obj);
                                                                }
                                                                break;
                                                            }
                                                        case 44:
                                                            synchronized (AudioDeviceBroker.this.mSetModeLock) {
                                                                synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                                    AudioDeviceBroker.this.mBtHelper.onScoAudioStateChanged(message.arg1);
                                                                }
                                                                break;
                                                            }
                                                        case 45:
                                                            BtDeviceInfo btDeviceInfo4 = (BtDeviceInfo) message.obj;
                                                            if (btDeviceInfo4.mDevice != null) {
                                                                AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("msg: onBluetoothActiveDeviceChange  state=" + btDeviceInfo4.mState + " addr=" + btDeviceInfo4.mDevice.getAddress() + " prof=" + btDeviceInfo4.mProfile + " supprNoisy=" + btDeviceInfo4.mSupprNoisy + " src=" + btDeviceInfo4.mEventSource).printLog(AudioDeviceBroker.TAG));
                                                                synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                                    AudioDeviceBroker.this.mDeviceInventory.setBluetoothActiveDevice(btDeviceInfo4);
                                                                }
                                                                break;
                                                            }
                                                            break;
                                                        case 46:
                                                            BleVolumeInfo bleVolumeInfo = (BleVolumeInfo) message.obj;
                                                            synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                                if (AudioDeviceBroker.this.getWrapper().getBluetoothVolSyncSupported()) {
                                                                    int i4 = bleVolumeInfo.mStreamType;
                                                                    if (i4 != 0 && i4 != 6 && i4 != 2) {
                                                                        i = 3;
                                                                        Log.d(AudioDeviceBroker.TAG, "info.mStreamType " + bleVolumeInfo.mStreamType + " forceStream " + i + "  blue stream " + AudioDeviceBroker.this.mAudioService.getBluetoothContextualVolumeStream());
                                                                        AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().setBleForceStream(i);
                                                                    }
                                                                    i = 0;
                                                                    Log.d(AudioDeviceBroker.TAG, "info.mStreamType " + bleVolumeInfo.mStreamType + " forceStream " + i + "  blue stream " + AudioDeviceBroker.this.mAudioService.getBluetoothContextualVolumeStream());
                                                                    AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().setBleForceStream(i);
                                                                }
                                                                AudioDeviceBroker.this.mBtHelper.setLeAudioVolume(bleVolumeInfo.mIndex, bleVolumeInfo.mMaxIndex, bleVolumeInfo.mStreamType);
                                                            }
                                                            break;
                                                        case 47:
                                                            AudioDeviceBroker.this.mDeviceInventory.onSaveSetDeviceAsNonDefault(message.arg1, (AudioDeviceAttributes) message.obj);
                                                            break;
                                                        case 48:
                                                            AudioDeviceBroker.this.mDeviceInventory.onSaveRemoveDeviceAsNonDefault(message.arg1, (AudioDeviceAttributes) message.obj);
                                                            break;
                                                        case 49:
                                                            synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                                                AudioDeviceBroker.this.mDeviceInventory.onMakeLeAudioDeviceUnavailableNow((String) message.obj, message.arg1);
                                                            }
                                                            break;
                                                        default:
                                                            if ((AudioDeviceBroker.this.mAudioService.isVendorBeforeAndroidU() && AudioDeviceBroker.this.mAudioService.mAsSocExt.isBleAudioFeatureSupported()) || message.what == 114) {
                                                                AudioDeviceBroker.this.mAudioService.mAsSocExt.handleMessageExt(message);
                                                                break;
                                                            } else {
                                                                Log.w(AudioDeviceBroker.TAG, "Invalid message " + message.what);
                                                                break;
                                                            }
                                                            break;
                                                    }
                                            }
                                    }
                            }
                        } else {
                            synchronized (AudioDeviceBroker.this.mSetModeLock) {
                                synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                                    AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().clearRouteSettingCheck(1, 0, 0, null);
                                }
                            }
                        }
                    } else {
                        AudioDeviceBroker.this.mAdbExt.sendBtDeviceConnectedEvent((BluetoothDevice) message.obj);
                    }
                } else if (message.arg1 != 1) {
                    synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                        AudioDeviceBroker.this.mBtHelper.onBtProfileConnected(message.arg1, (BluetoothProfile) message.obj);
                    }
                } else {
                    synchronized (AudioDeviceBroker.this.mSetModeLock) {
                        synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                            AudioDeviceBroker.this.mBtHelper.onHeadsetProfileConnected((BluetoothHeadset) message.obj);
                        }
                    }
                }
            } else if (message.arg1 != 1) {
                synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                    AudioDeviceBroker.this.mBtHelper.onBtProfileDisconnected(message.arg1);
                    AudioDeviceBroker.this.mDeviceInventory.onBtProfileDisconnected(message.arg1);
                }
            } else {
                synchronized (AudioDeviceBroker.this.mSetModeLock) {
                    synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                        AudioDeviceBroker.this.mBtHelper.disconnectHeadset();
                    }
                }
            }
            if (AudioDeviceBroker.MESSAGES_MUTE_MUSIC.contains(Integer.valueOf(message.what))) {
                int i5 = message.what;
                int i6 = (i5 == 7 || i5 == 11 || i5 == 29 || i5 == 2 || i5 == 77) ? 400 : 0;
                if (AudioService.DEBUG_DEVICES) {
                    Log.d(AudioDeviceBroker.TAG, "Music stream's unmute delay is increased by " + (i6 + 100) + " msfor A2DP/LE/Wired connection changes, msgid=" + message.what);
                }
                AudioDeviceBroker.this.sendMsg(35, 0, i6 + 100);
            }
            if (AudioDeviceBroker.isMessageHandledUnderWakelock(message.what)) {
                try {
                    AudioDeviceBroker.this.mBrokerEventWakeLock.release();
                } catch (Exception e) {
                    Log.e(AudioDeviceBroker.TAG, "Exception releasing wakelock", e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMsg(int i, int i2, int i3) {
        sendIILMsg(i, i2, 0, 0, null, i3);
    }

    private void sendILMsg(int i, int i2, int i3, Object obj, int i4) {
        sendIILMsg(i, i2, i3, 0, obj, i4);
    }

    private void sendLMsg(int i, int i2, Object obj, int i3) {
        sendIILMsg(i, i2, 0, 0, obj, i3);
    }

    private void sendIMsg(int i, int i2, int i3, int i4) {
        sendIILMsg(i, i2, i3, 0, null, i4);
    }

    private void sendMsgNoDelay(int i, int i2) {
        sendIILMsg(i, i2, 0, 0, null, 0);
    }

    private void sendIMsgNoDelay(int i, int i2, int i3) {
        sendIILMsg(i, i2, i3, 0, null, 0);
    }

    private void sendIIMsgNoDelay(int i, int i2, int i3, int i4) {
        sendIILMsg(i, i2, i3, i4, null, 0);
    }

    private void sendILMsgNoDelay(int i, int i2, int i3, Object obj) {
        sendIILMsg(i, i2, i3, 0, obj, 0);
    }

    private void sendLMsgNoDelay(int i, int i2, Object obj) {
        sendIILMsg(i, i2, 0, 0, obj, 0);
    }

    private void sendIILMsgNoDelay(int i, int i2, int i3, int i4, Object obj) {
        sendIILMsg(i, i2, i3, i4, obj, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendIILMsg(int i, int i2, int i3, int i4, Object obj, int i5) {
        AudioDeviceAttributes audioDeviceAttributes;
        if (i2 == 0) {
            this.mBrokerHandler.removeMessages(i);
        } else if (i2 == 1 && this.mBrokerHandler.hasMessages(i)) {
            return;
        }
        if (isMessageHandledUnderWakelock(i)) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                try {
                    this.mBrokerEventWakeLock.acquire(BROKER_WAKELOCK_TIMEOUT_MS);
                } catch (Exception e) {
                    Log.e(TAG, "Exception acquiring wakelock", e);
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        if (MESSAGES_MUTE_MUSIC.contains(Integer.valueOf(i))) {
            if (Build.isMtkPlatform() && i == 2) {
                AudioDeviceInventory.WiredDeviceConnectionState wiredDeviceConnectionState = (AudioDeviceInventory.WiredDeviceConnectionState) obj;
                if (wiredDeviceConnectionState.mState == 1 || (((audioDeviceAttributes = wiredDeviceConnectionState.mAttributes) != null && audioDeviceAttributes.getInternalType() == -2146959360) || wiredDeviceConnectionState.mAttributes.getInternalType() == 8388608)) {
                    if (AudioService.DEBUG_DEVICES) {
                        Log.d(TAG, "wirde device connected,do not mute");
                    }
                } else if (AudioSystem.isStreamActive(3, 0) && this.mAudioService.getDeviceForStream(3) == wiredDeviceConnectionState.mAttributes.getInternalType()) {
                    i5 += 160;
                    checkMessagesMuteMusic(i);
                }
            } else {
                i5 += 160;
                checkMessagesMuteMusic(i);
            }
        }
        synchronized (sLastDeviceConnectionMsgTimeLock) {
            long uptimeMillis = SystemClock.uptimeMillis() + i5;
            if (i == 2 || i == 7 || i == 49 || i == 77 || i == 10 || i == 11) {
                long j = sLastDeviceConnectMsgTime;
                if (j >= uptimeMillis) {
                    uptimeMillis = j + 30;
                }
                sLastDeviceConnectMsgTime = uptimeMillis;
            }
            BrokerHandler brokerHandler = this.mBrokerHandler;
            brokerHandler.sendMessageAtTime(brokerHandler.obtainMessage(i, i3, i4, obj), uptimeMillis);
        }
    }

    private static <T> boolean hasIntersection(Set<T> set, Set<T> set2) {
        Iterator<T> it = set.iterator();
        while (it.hasNext()) {
            if (set2.contains(it.next())) {
                return true;
            }
        }
        return false;
    }

    boolean messageMutesMusic(int i) {
        if (i == 0) {
            return false;
        }
        return ((i == 7 || i == 29 || i == 11 || (Build.isQcomPlatform() && i == 77)) && AudioSystem.isStreamActive(3, 0) && hasIntersection(AudioDeviceInventory.DEVICE_OVERRIDE_A2DP_ROUTE_ON_PLUG_SET, this.mAudioService.getDeviceSetForStream(3))) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postCheckMessagesMuteMusic() {
        Log.d(TAG, "postCheckMessagesMuteMusic() true");
        checkMessagesMuteMusic(7);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkMessagesMuteMusic(int i) {
        boolean messageMutesMusic = messageMutesMusic(i);
        if (!messageMutesMusic) {
            Iterator<Integer> it = MESSAGES_MUTE_MUSIC.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                int intValue = it.next().intValue();
                if (this.mBrokerHandler.hasMessages(intValue) && messageMutesMusic(intValue)) {
                    messageMutesMusic = true;
                    break;
                }
            }
        }
        if (messageMutesMusic != this.mMusicMuted.getAndSet(messageMutesMusic)) {
            this.mAudioService.setMusicMute(messageMutesMusic);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class CommunicationRouteClient implements IBinder.DeathRecipient {
        private final IBinder mCb;
        private AudioDeviceAttributes mDevice;
        private final int mPid;

        CommunicationRouteClient(IBinder iBinder, int i, AudioDeviceAttributes audioDeviceAttributes) {
            this.mCb = iBinder;
            this.mPid = i;
            this.mDevice = audioDeviceAttributes;
        }

        public boolean registerDeathRecipient() {
            try {
                this.mCb.linkToDeath(this, 0);
                return true;
            } catch (RemoteException unused) {
                Log.w(AudioDeviceBroker.TAG, "CommunicationRouteClient could not link to " + this.mCb + " binder death");
                return false;
            }
        }

        public void unregisterDeathRecipient() {
            try {
                this.mCb.unlinkToDeath(this, 0);
            } catch (NoSuchElementException unused) {
                Log.w(AudioDeviceBroker.TAG, "CommunicationRouteClient could not not unregistered to binder");
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            AudioDeviceBroker.this.postCommunicationRouteClientDied(this);
        }

        IBinder getBinder() {
            return this.mCb;
        }

        int getPid() {
            return this.mPid;
        }

        AudioDeviceAttributes getDevice() {
            return this.mDevice;
        }

        public String toString() {
            return "CommunicationRouteClient :={ @" + Integer.toHexString(hashCode()) + ", mCb=" + this.mCb + ", mPid=" + this.mPid + ", mDevice={" + this.mDevice + " }}";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mDeviceStateLock"})
    public void onCommunicationRouteClientDied(CommunicationRouteClient communicationRouteClient) {
        if (communicationRouteClient == null) {
            return;
        }
        Log.w(TAG, "Communication client died");
        setCommunicationRouteForClient(communicationRouteClient.getBinder(), communicationRouteClient.getPid(), null, -1, "onCommunicationRouteClientDied");
    }

    @GuardedBy({"mDeviceStateLock"})
    private AudioDeviceAttributes preferredCommunicationDevice() {
        boolean z;
        AudioDeviceAttributes headsetAudioDevice;
        AudioDeviceAttributes preferredCommunicationDevice;
        if (this.mAudioService.isVendorBeforeAndroidU() && this.mAudioService.mAsSocExt.isBleAudioFeatureSupported() && (preferredCommunicationDevice = this.mAudioService.mAsSocExt.preferredCommunicationDevice()) != null) {
            return preferredCommunicationDevice;
        }
        boolean isBluetoothScoOn = this.mBtHelper.isBluetoothScoOn();
        synchronized (this.mBluetoothAudioStateLock) {
            if (isBluetoothScoOn) {
                try {
                    z = this.mBluetoothScoOn;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        if (z) {
            if (Build.isQcomPlatform()) {
                headsetAudioDevice = this.mBtHelper.getHeadsetAudioDummyDevice();
            } else {
                headsetAudioDevice = this.mBtHelper.getHeadsetAudioDevice();
            }
            if (headsetAudioDevice != null) {
                return headsetAudioDevice;
            }
        }
        AudioDeviceAttributes requestedCommunicationDevice = requestedCommunicationDevice();
        if (requestedCommunicationDevice == null || requestedCommunicationDevice.getType() == 7) {
            return null;
        }
        if (Build.isMtkPlatform() && this.mAudioService.isVendorBeforeAndroidU() && requestedCommunicationDevice.getType() == 26) {
            return null;
        }
        return requestedCommunicationDevice;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioDeviceAttributes getHeadsetAudioDevice() {
        return this.mBtHelper.getHeadsetAudioDevice();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mDeviceStateLock"})
    public void updateCommunicationRoute(String str) {
        AudioDeviceAttributes leAudioDevice;
        AudioDeviceAttributes preferredCommunicationDevice = preferredCommunicationDevice();
        if (AudioService.DEBUG_COMM_RTE) {
            Log.v(TAG, "updateCommunicationRoute, preferredCommunicationDevice: " + preferredCommunicationDevice + " eventSource: " + str);
        }
        AudioService.sDeviceLogger.enqueue(new EventLogger.StringEvent("updateCommunicationRoute, preferredCommunicationDevice: " + preferredCommunicationDevice + " eventSource: " + str));
        this.mPreCommunicationDevice = this.mCurCommunicationDevice;
        if (preferredCommunicationDevice == null) {
            AudioDeviceAttributes defaultCommunicationDevice = getDefaultCommunicationDevice();
            if (Build.isMtkPlatform() && defaultCommunicationDevice != null && defaultCommunicationDevice.getType() == 26) {
                AudioDeviceAttributes leAudioDevice2 = this.mAudioService.mAsSocExt.getLeAudioDevice();
                if (leAudioDevice2 != null) {
                    defaultCommunicationDevice = leAudioDevice2;
                }
                if (this.mAudioService.isVendorBeforeAndroidU() && !this.mAudioService.mAsSocExt.isBluetoothLeCgStateOn()) {
                    IAudioServiceSocExt iAudioServiceSocExt = this.mAudioService.mAsSocExt;
                    AudioModeInfo audioModeInfo = this.mAudioModeOwner;
                    iAudioServiceSocExt.startBluetoothLeCg(audioModeInfo.mPid, audioModeInfo.mUid, 3, iAudioServiceSocExt.getModeCb());
                }
                if (getWrapper().getBluetoothVolSyncSupported() && leAudioDevice2 != null && (leAudioDevice2.getInternalType() == 536870912 || leAudioDevice2.getInternalType() == 536870913)) {
                    int bluetoothContextualVolumeStream = this.mAudioService.getBluetoothContextualVolumeStream();
                    if (bluetoothContextualVolumeStream == 0) {
                        bluetoothContextualVolumeStream = 6;
                    }
                    int vssVolumeForDevice = getVssVolumeForDevice(bluetoothContextualVolumeStream, leAudioDevice2.getInternalType());
                    int maxVssVolumeForStream = getMaxVssVolumeForStream(bluetoothContextualVolumeStream);
                    if (AudioService.DEBUG_COMM_RTE) {
                        Log.v(TAG, "setCommunicationRouteForClient restoring LE Audio device volume lvl.");
                    }
                    postSetLeAudioVolumeIndex(vssVolumeForDevice, maxVssVolumeForStream, bluetoothContextualVolumeStream);
                }
            }
            this.mCurCommunicationDevice = defaultCommunicationDevice;
            if (Build.isMtkPlatform() && !this.mAudioService.isVendorBeforeAndroidU()) {
                AudioDeviceAttributes audioDeviceAttributes = this.mPreCommunicationDevice;
                AudioDeviceAttributes audioDeviceAttributes2 = this.mCurCommunicationDevice;
                if (audioDeviceAttributes != audioDeviceAttributes2) {
                    if (audioDeviceAttributes2 != null && audioDeviceAttributes2.getType() == 26) {
                        this.mBtHelper.updateBleCGStateToBt(true);
                    } else {
                        AudioDeviceAttributes audioDeviceAttributes3 = this.mPreCommunicationDevice;
                        if (audioDeviceAttributes3 != null && audioDeviceAttributes3.getType() == 26 && isBleRecordingIdle()) {
                            this.mBtHelper.updateBleCGStateToBt(false);
                        }
                    }
                }
            }
            if (defaultCommunicationDevice != null) {
                this.mDeviceInventory.setPreferredDevicesForStrategyInt(this.mCommunicationStrategyId, Arrays.asList(defaultCommunicationDevice));
                this.mDeviceInventory.setPreferredDevicesForStrategyInt(this.mAccessibilityStrategyId, Arrays.asList(defaultCommunicationDevice));
            } else {
                this.mDeviceInventory.removePreferredDevicesForStrategyInt(this.mCommunicationStrategyId);
                this.mDeviceInventory.removePreferredDevicesForStrategyInt(this.mAccessibilityStrategyId);
            }
            this.mDeviceInventory.applyConnectedDevicesRoles();
            this.mDeviceInventory.reapplyExternalDevicesRoles();
        } else {
            if (Build.isMtkPlatform() && preferredCommunicationDevice.getType() == 26 && (leAudioDevice = this.mAudioService.mAsSocExt.getLeAudioDevice()) != null) {
                preferredCommunicationDevice = leAudioDevice;
            }
            this.mCurCommunicationDevice = preferredCommunicationDevice;
            if (!this.mAudioService.isVendorBeforeAndroidU()) {
                AudioDeviceAttributes audioDeviceAttributes4 = this.mPreCommunicationDevice;
                AudioDeviceAttributes audioDeviceAttributes5 = this.mCurCommunicationDevice;
                if (audioDeviceAttributes4 != audioDeviceAttributes5) {
                    if (audioDeviceAttributes5 != null && audioDeviceAttributes5.getType() == 26) {
                        this.mBtHelper.updateBleCGStateToBt(true);
                    } else {
                        AudioDeviceAttributes audioDeviceAttributes6 = this.mPreCommunicationDevice;
                        if (audioDeviceAttributes6 != null && audioDeviceAttributes6.getType() == 26 && isBleRecordingIdle()) {
                            this.mBtHelper.updateBleCGStateToBt(false);
                        }
                    }
                }
            }
            this.mDeviceInventory.setPreferredDevicesForStrategyInt(this.mCommunicationStrategyId, Arrays.asList(preferredCommunicationDevice));
            this.mDeviceInventory.setPreferredDevicesForStrategyInt(this.mAccessibilityStrategyId, Arrays.asList(preferredCommunicationDevice));
        }
        onUpdatePhoneStrategyDevice(preferredCommunicationDevice);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mDeviceStateLock"})
    public void onUpdateCommunicationRouteClient(String str) {
        updateCommunicationRoute(str);
        CommunicationRouteClient communicationRouteClient = topCommunicationRouteClient();
        Log.v(TAG, "onUpdateCommunicationRouteClient, crc: " + communicationRouteClient + " eventSource: " + str);
        if (communicationRouteClient != null) {
            if (str.equals(IAudioDeviceBrokerExt.SET_BLUETOOTH_ACTIVE_DEVICE)) {
                setCommunicationRouteForClient(communicationRouteClient.getBinder(), communicationRouteClient.getPid(), this.mAdbExt.checkWhetherAnotherLeDevice(communicationRouteClient.getDevice()), -1, str);
            } else {
                setCommunicationRouteForClient(communicationRouteClient.getBinder(), communicationRouteClient.getPid(), communicationRouteClient.getDevice(), -1, str);
            }
        }
    }

    @GuardedBy({"mDeviceStateLock"})
    private void onUpdatePhoneStrategyDevice(AudioDeviceAttributes audioDeviceAttributes) {
        boolean isSpeakerphoneActive = isSpeakerphoneActive();
        this.mPreferredCommunicationDevice = audioDeviceAttributes;
        if (this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported()) {
            this.mAudioService.getWrapper().getExtImpl().updatePreferredCommunicationDevice(this.mPreferredCommunicationDevice);
        }
        updateActiveCommunicationDevice();
        if (isSpeakerphoneActive != isSpeakerphoneActive()) {
            try {
                this.mContext.sendBroadcastAsUser(new Intent("android.media.action.SPEAKERPHONE_STATE_CHANGED").setFlags(1073741824), UserHandle.ALL);
            } catch (Exception e) {
                Log.w(TAG, "failed to broadcast ACTION_SPEAKERPHONE_STATE_CHANGED: " + e);
            }
        }
        this.mAudioService.postUpdateRingerModeServiceInt();
        dispatchCommunicationDevice();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CommunicationRouteClient removeCommunicationRouteClient(IBinder iBinder, boolean z) {
        Iterator<CommunicationRouteClient> it = this.mCommunicationRouteClients.iterator();
        while (it.hasNext()) {
            CommunicationRouteClient next = it.next();
            if (next.getBinder() == iBinder) {
                if (z) {
                    next.unregisterDeathRecipient();
                }
                this.mCommunicationRouteClients.remove(next);
                if (this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported()) {
                    this.mAudioService.getWrapper().getExtImpl().removeRouteClientActiveState(next.getPid());
                }
                return next;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mDeviceStateLock"})
    public CommunicationRouteClient addCommunicationRouteClient(IBinder iBinder, int i, AudioDeviceAttributes audioDeviceAttributes) {
        removeCommunicationRouteClient(iBinder, true);
        CommunicationRouteClient communicationRouteClient = new CommunicationRouteClient(iBinder, i, audioDeviceAttributes);
        if (!communicationRouteClient.registerDeathRecipient()) {
            return null;
        }
        this.mCommunicationRouteClients.add(0, communicationRouteClient);
        if (this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported()) {
            this.mAudioService.getWrapper().getExtImpl().addRouteClientActiveState(i, audioDeviceAttributes.getType());
        }
        return communicationRouteClient;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mDeviceStateLock"})
    public CommunicationRouteClient getCommunicationRouteClientForPid(int i) {
        Iterator<CommunicationRouteClient> it = this.mCommunicationRouteClients.iterator();
        while (it.hasNext()) {
            CommunicationRouteClient next = it.next();
            if (next.getPid() == i) {
                return next;
            }
        }
        return null;
    }

    public int getCurrentConnectedScoDevices() {
        int currentConnectedScoDevices;
        synchronized (this.mDeviceStateLock) {
            currentConnectedScoDevices = this.mDeviceInventory.getCurrentConnectedScoDevices();
        }
        return currentConnectedScoDevices;
    }

    public boolean isLeConnected() {
        boolean isLeConnected;
        synchronized (this.mDeviceStateLock) {
            isLeConnected = this.mDeviceInventory.isLeConnected();
        }
        return isLeConnected;
    }

    @GuardedBy({"mDeviceStateLock"})
    private boolean communnicationDeviceCompatOn() {
        AudioModeInfo audioModeInfo = this.mAudioModeOwner;
        return (audioModeInfo.mMode != 3 || CompatChanges.isChangeEnabled(USE_SET_COMMUNICATION_DEVICE, audioModeInfo.mUid) || this.mAudioModeOwner.mUid == 1000) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UUID getDeviceSensorUuid(AudioDeviceAttributes audioDeviceAttributes) {
        UUID deviceSensorUuid;
        synchronized (this.mDeviceStateLock) {
            deviceSensorUuid = this.mDeviceInventory.getDeviceSensorUuid(audioDeviceAttributes);
        }
        return deviceSensorUuid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchPreferredMixerAttributesChangedCausedByDeviceRemoved(AudioDeviceInfo audioDeviceInfo) {
        this.mAudioService.dispatchPreferredMixerAttributesChanged(new AudioAttributes.Builder().setUsage(1).build(), audioDeviceInfo.getId(), null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInbandRingingEnabled() {
        return this.mBtHelper.isInbandRingingEnabled();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleRecordingConfigurationChanged(int i, int i2, int i3) {
        Integer valueOf = Integer.valueOf(i3);
        if (i != 1) {
            if (i != 2 || this.mSampleRateHD.contains(valueOf) || this.mSampleRateNormal.contains(valueOf)) {
                return;
            }
            if (this.mAudioService.isVendorBeforeAndroidU()) {
                this.mAudioService.mAsSocExt.startBluetoothLeCgForRecord(new Binder(), i3, i2);
            }
            if (i2 >= 48000) {
                this.mSampleRateHD.add(valueOf);
            } else {
                this.mSampleRateNormal.add(valueOf);
            }
            if (this.mAudioService.isVendorBeforeAndroidU()) {
                return;
            }
            this.mBtHelper.updateBleCGStateToBt(true);
            return;
        }
        if (i2 >= 48000) {
            this.mSampleRateHD.remove(valueOf);
        } else {
            this.mSampleRateNormal.remove(valueOf);
        }
        CommunicationRouteClient communicationRouteClientForPid = getCommunicationRouteClientForPid(i3);
        IBinder binder = communicationRouteClientForPid != null ? communicationRouteClientForPid.getBinder() : null;
        if (isBleRecordingIdle() && this.mAudioModeOwner.mMode == 0) {
            if (this.mAudioService.isVendorBeforeAndroidU()) {
                this.mAudioService.mAsSocExt.stopBluetoothLeCgForRecord(binder, i3);
            } else {
                this.mBtHelper.updateBleCGStateToBt(false);
            }
        } else {
            Log.d(TAG, "Don't stop cg, isBleRecordingIdle: " + isBleRecordingIdle());
        }
        if (binder != null) {
            removeCommunicationRouteClient(binder, true);
        }
    }

    boolean isBleRecordingIdle() {
        return this.mSampleRateHD.isEmpty() && this.mSampleRateNormal.isEmpty();
    }

    boolean isBleHDRecordActive() {
        return !this.mSampleRateHD.isEmpty();
    }

    public boolean isLoudSpeakerBt() {
        return this.mBtHelper.isLoudSpeakerBt();
    }

    public IAudioDeviceBrokerWrapper getWrapper() {
        return this.mAdbWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AudioDeviceBrokerWrapper implements IAudioDeviceBrokerWrapper {
        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public int getSetAvrcpAbsVolMsg() {
            return 15;
        }

        private AudioDeviceBrokerWrapper() {
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public IAudioDeviceBrokerExt getExtImpl() {
            return AudioDeviceBroker.this.mAdbExt;
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public AudioDeviceInventory getDeviceInventory() {
            return AudioDeviceBroker.this.mDeviceInventory;
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public boolean isSpeakerA2dpDevice() {
            return AudioDeviceBroker.this.mDeviceInventory.getWrapper().getExtImpl().isSpeakerA2dpDevice();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public int getA2dpVolume(boolean z, int i) {
            return AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().getA2dpVolume(z, i);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void sendIILMsg(int i, int i2, int i3, int i4, Object obj, int i5) {
            AudioDeviceBroker.this.sendIILMsg(i, i2, i3, i4, obj, i5);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public boolean isDeviceConnected(AudioDeviceAttributes audioDeviceAttributes) {
            return AudioDeviceBroker.this.isDeviceConnected(audioDeviceAttributes);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public boolean isAudioRouteSupported() {
            return AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().isAudioRouteSupported();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void removeInactiveRouteClientForPid(int i) {
            CommunicationRouteClient communicationRouteClientForPid = AudioDeviceBroker.this.getCommunicationRouteClientForPid(i);
            if (communicationRouteClientForPid != null) {
                Log.d(AudioDeviceBroker.TAG, "pid " + communicationRouteClientForPid.getPid() + " has no active audio for a long time ,remove it");
                AudioDeviceBroker.this.postSetCommunicationDeviceForClient(new CommunicationDeviceInfo(communicationRouteClientForPid.getBinder(), i, communicationRouteClientForPid.getDevice(), false, -1, IAudioDeviceBrokerExt.REMOVE_INACTIVE_ROUTE_CLIENT, false));
            }
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void checkClearSpeakerDevice(int i) {
            if (AudioDeviceBroker.this.getCommunicationRouteClientForPid(i) == null) {
                AudioDeviceBroker.this.mDeviceInventory.removePreferredDevicesForStrategyInt(AudioDeviceBroker.this.mCommunicationStrategyId);
                AudioDeviceBroker.this.mDeviceInventory.removePreferredDevicesForStrategyInt(AudioDeviceBroker.this.mAccessibilityStrategyId);
                Log.d(AudioDeviceBroker.TAG, "checkClearSpeakerDevice remove speaker");
            }
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void removeRouteClientForPid(int i) {
            synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                CommunicationRouteClient communicationRouteClientForPid = AudioDeviceBroker.this.getCommunicationRouteClientForPid(i);
                if (communicationRouteClientForPid != null) {
                    AudioDeviceBroker.this.removeCommunicationRouteClient(communicationRouteClientForPid.getBinder(), true);
                }
            }
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void stopBluethoothScoToBT(String str) {
            AudioDeviceBroker.this.mBtHelper.stopBluetoothSco(str);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void clearRedundancyClient(int i, IBinder iBinder) {
            synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                CommunicationRouteClient communicationRouteClientForPid = AudioDeviceBroker.this.getCommunicationRouteClientForPid(i);
                if (communicationRouteClientForPid != null && communicationRouteClientForPid.getBinder() != iBinder) {
                    AudioDeviceBroker.this.addCommunicationRouteClient(iBinder, i, AudioDeviceBroker.this.removeCommunicationRouteClient(communicationRouteClientForPid.getBinder(), true).getDevice());
                    Log.d(AudioDeviceBroker.TAG, "clearRedundancyClient for pid " + i);
                }
            }
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void checkBuildRouteForSco(int i, Object obj) {
            if (i == -1 || obj == null) {
                return;
            }
            IBinder iBinder = (IBinder) obj;
            if (AudioDeviceBroker.this.mDeviceInventory.getWrapper().isBluetoothScoDeviceConnected() && AudioDeviceBroker.this.getCommunicationRouteClientForPid(i) == null) {
                synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                    AudioDeviceBroker.this.addCommunicationRouteClient(iBinder, i, new AudioDeviceAttributes(16, ""));
                    Log.d(AudioDeviceBroker.TAG, "Build SCO Route for pid " + i);
                }
            }
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void checkTimeoutInactiveRouteClient() {
            synchronized (AudioDeviceBroker.this.mSetModeLock) {
                synchronized (AudioDeviceBroker.this.mDeviceStateLock) {
                    AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().checkTimeoutInactiveRouteClient();
                }
            }
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public int getUidByPid(int i) {
            return AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().getUidByPid(i);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public int getLatestPreferredDeviceType() {
            return AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().getLatestPreferredDeviceType();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public int getLatestModeOwnerPid() {
            return AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().getLatestModeOwnerPid();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public Looper getBrokerLooper() {
            return AudioDeviceBroker.this.mBrokerThread.getLooper();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public BluetoothDevice getBluetoothDevice() {
            return AudioDeviceBroker.this.mBtHelper.getBtDevice();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
            AudioDeviceBroker.this.mBtHelper.setBtDevice(bluetoothDevice);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public int getBleVolume(boolean z, int i) {
            return AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().getBleVolume(z, i);
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public boolean getBluetoothVolSyncSupported() {
            return AudioDeviceBroker.this.mAudioService.getWrapper().getExtImpl().getBluetoothVolSyncSupported();
        }

        @Override // com.android.server.audio.IAudioDeviceBrokerWrapper
        public void checkBtDeviceForCarkitBt(boolean z, boolean z2) {
            if (z) {
                if (z2) {
                    AudioDeviceBroker.this.mAudioService.getWrapper().checkCarkitBtAndSetValue(false);
                    return;
                } else {
                    AudioDeviceBroker.this.mAudioService.getWrapper().checkCarkitBtAndSetValue(true);
                    return;
                }
            }
            if (z2) {
                AudioDeviceBroker.this.mAudioService.getWrapper().checkCarkitBtAndSetValue(true);
            } else {
                AudioDeviceBroker.this.mAudioService.getWrapper().checkCarkitBtAndSetValue(false);
            }
        }
    }
}
