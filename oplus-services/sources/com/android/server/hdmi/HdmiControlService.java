package com.android.server.hdmi;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.hardware.display.DisplayManager;
import android.hardware.hdmi.HdmiDeviceInfo;
import android.hardware.hdmi.HdmiHotplugEvent;
import android.hardware.hdmi.HdmiPortInfo;
import android.hardware.hdmi.IHdmiCecSettingChangeListener;
import android.hardware.hdmi.IHdmiCecVolumeControlFeatureListener;
import android.hardware.hdmi.IHdmiControlCallback;
import android.hardware.hdmi.IHdmiControlService;
import android.hardware.hdmi.IHdmiControlStatusChangeListener;
import android.hardware.hdmi.IHdmiDeviceEventListener;
import android.hardware.hdmi.IHdmiHotplugEventListener;
import android.hardware.hdmi.IHdmiInputChangeListener;
import android.hardware.hdmi.IHdmiMhlVendorCommandListener;
import android.hardware.hdmi.IHdmiRecordListener;
import android.hardware.hdmi.IHdmiSystemAudioModeChangeListener;
import android.hardware.hdmi.IHdmiVendorCommandListener;
import android.media.AudioAttributes;
import android.media.AudioDescriptor;
import android.media.AudioDeviceAttributes;
import android.media.AudioDeviceVolumeManager;
import android.media.VolumeInfo;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.tv.TvInputManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.sysprop.HdmiProperties;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.SystemService;
import com.android.server.hdmi.HdmiAnnotations;
import com.android.server.hdmi.HdmiCecConfig;
import com.android.server.hdmi.HdmiCecController;
import com.android.server.hdmi.HdmiCecLocalDevice;
import com.android.server.hdmi.HdmiControlService;
import com.android.server.power.ShutdownThread;
import com.android.server.usb.descriptors.UsbACInterface;
import com.android.server.usb.descriptors.UsbTerminalTypes;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import libcore.util.EmptyArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class HdmiControlService extends SystemService {

    @VisibleForTesting
    static final AudioDeviceAttributes AUDIO_OUTPUT_DEVICE_HDMI;

    @VisibleForTesting
    static final AudioDeviceAttributes AUDIO_OUTPUT_DEVICE_HDMI_ARC;
    static final AudioDeviceAttributes AUDIO_OUTPUT_DEVICE_HDMI_EARC;
    private static final List<AudioDeviceAttributes> AVB_AUDIO_OUTPUT_DEVICES;
    static final int INITIATED_BY_BOOT_UP = 1;
    static final int INITIATED_BY_ENABLE_CEC = 0;
    static final int INITIATED_BY_ENABLE_EARC = 6;
    static final int INITIATED_BY_HOTPLUG = 4;
    static final int INITIATED_BY_SCREEN_ON = 2;
    static final int INITIATED_BY_SOUNDBAR_MODE = 5;
    static final int INITIATED_BY_WAKE_UP_MESSAGE = 3;
    static final String PERMISSION = "android.permission.HDMI_CEC";
    static final int STANDBY_SCREEN_OFF = 0;
    static final int STANDBY_SHUTDOWN = 1;

    @VisibleForTesting
    static final AudioAttributes STREAM_MUSIC_ATTRIBUTES;
    private static final String TAG = "HdmiControlService";
    static final int WAKE_UP_BOOT_UP = 1;
    static final int WAKE_UP_SCREEN_ON = 0;
    private AbsoluteVolumeChangedListener mAbsoluteVolumeChangedListener;

    @HdmiAnnotations.ServiceThreadOnly
    private int mActivePortId;

    @GuardedBy({"mLock"})
    protected final HdmiCecLocalDevice.ActiveSource mActiveSource;
    private boolean mAddressAllocated;
    private HdmiCecAtomWriter mAtomWriter;

    @GuardedBy({"mLock"})
    private Map<AudioDeviceAttributes, Integer> mAudioDeviceVolumeBehaviors;
    private AudioDeviceVolumeManagerWrapper mAudioDeviceVolumeManager;
    private AudioManagerWrapper mAudioManager;
    private HdmiCecController mCecController;
    private final List<Integer> mCecLocalDevices;
    private CecMessageBuffer mCecMessageBuffer;
    private int mCecVersion;
    private DeviceConfigWrapper mDeviceConfig;

    @GuardedBy({"mLock"})
    private final ArrayList<DeviceEventListenerRecord> mDeviceEventListenerRecords;
    private DisplayManager mDisplayManager;
    private IHdmiControlCallback mDisplayStatusCallback;
    private HdmiEarcController mEarcController;

    @GuardedBy({"mLock"})
    private boolean mEarcEnabled;
    private HdmiEarcLocalDevice mEarcLocalDevice;
    private int mEarcPortId;

    @GuardedBy({"mLock"})
    @VisibleForTesting
    private boolean mEarcSupported;

    @HdmiAnnotations.ServiceThreadOnly
    private boolean mEarcTxFeatureFlagEnabled;
    private final Handler mHandler;
    private HdmiCecConfig mHdmiCecConfig;
    private HdmiCecNetwork mHdmiCecNetwork;

    @GuardedBy({"mLock"})
    private final ArrayMap<String, RemoteCallbackList<IHdmiCecSettingChangeListener>> mHdmiCecSettingChangeListenerRecords;

    @GuardedBy({"mLock"})
    private int mHdmiCecVolumeControl;

    @GuardedBy({"mLock"})
    private final RemoteCallbackList<IHdmiCecVolumeControlFeatureListener> mHdmiCecVolumeControlFeatureListenerRecords;
    private final HdmiControlBroadcastReceiver mHdmiControlBroadcastReceiver;

    @GuardedBy({"mLock"})
    private int mHdmiControlEnabled;

    @GuardedBy({"mLock"})
    private final ArrayList<HdmiControlStatusChangeListenerRecord> mHdmiControlStatusChangeListenerRecords;

    @GuardedBy({"mLock"})
    private final ArrayList<HotplugEventListenerRecord> mHotplugEventListenerRecords;

    @GuardedBy({"mLock"})
    private InputChangeListenerRecord mInputChangeListenerRecord;
    private Looper mIoLooper;
    private final HandlerThread mIoThread;
    private boolean mIsCecAvailable;

    @HdmiAnnotations.ServiceThreadOnly
    private int mLastInputMhl;
    private final Object mLock;

    @HdmiAnnotations.ServiceThreadOnly
    private String mMenuLanguage;
    private HdmiMhlControllerStub mMhlController;

    @GuardedBy({"mLock"})
    private List<HdmiDeviceInfo> mMhlDevices;

    @GuardedBy({"mLock"})
    private boolean mMhlInputChangeEnabled;

    @GuardedBy({"mLock"})
    private final ArrayList<HdmiMhlVendorCommandListenerRecord> mMhlVendorCommandListenerRecords;

    @HdmiAnnotations.ServiceThreadOnly
    private boolean mNumericSoundbarVolumeUiOnTvFeatureFlagEnabled;
    private IHdmiControlCallback mOtpCallbackPendingAddressAllocation;
    private PowerManagerWrapper mPowerManager;
    private PowerManagerInternalWrapper mPowerManagerInternal;
    private HdmiCecPowerStatusController mPowerStatusController;

    @GuardedBy({"mLock"})
    private boolean mProhibitMode;

    @GuardedBy({"mLock"})
    private HdmiRecordListenerRecord mRecordListenerRecord;
    private final SelectRequestBuffer mSelectRequestBuffer;
    private final Executor mServiceThreadExecutor;
    private HdmiCecConfig.SettingChangeListener mSettingChangeListener;
    private final SettingsObserver mSettingsObserver;

    @HdmiAnnotations.ServiceThreadOnly
    private boolean mSoundbarModeFeatureFlagEnabled;

    @HdmiAnnotations.ServiceThreadOnly
    private boolean mStandbyMessageReceived;
    private int mStreamMusicMaxVolume;

    @GuardedBy({"mLock"})
    private boolean mSystemAudioActivated;
    private final ArrayList<SystemAudioModeChangeListenerRecord> mSystemAudioModeChangeListenerRecords;

    @HdmiAnnotations.ServiceThreadOnly
    private boolean mTransitionFromArcToEarcTxEnabled;
    private TvInputManager mTvInputManager;

    @GuardedBy({"mLock"})
    private final ArrayList<VendorCommandListenerRecord> mVendorCommandListenerRecords;

    @HdmiAnnotations.ServiceThreadOnly
    private boolean mWakeUpMessageReceived;
    private static final Locale HONG_KONG = new Locale("zh", "HK");
    private static final Locale MACAU = new Locale("zh", "MO");
    private static final Map<String, String> sTerminologyToBibliographicMap = createsTerminologyToBibliographicMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface DevicePollingCallback {
        void onPollingFinished(List<Integer> list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface SendMessageCallback {
        void onSendCompleted(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface WakeReason {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int toInt(boolean z) {
        return z ? 1 : 0;
    }

    @VisibleForTesting
    int getInitialPowerStatus() {
        return 3;
    }

    static {
        AudioDeviceAttributes audioDeviceAttributes = new AudioDeviceAttributes(2, 9, "");
        AUDIO_OUTPUT_DEVICE_HDMI = audioDeviceAttributes;
        AudioDeviceAttributes audioDeviceAttributes2 = new AudioDeviceAttributes(2, 10, "");
        AUDIO_OUTPUT_DEVICE_HDMI_ARC = audioDeviceAttributes2;
        AudioDeviceAttributes audioDeviceAttributes3 = new AudioDeviceAttributes(2, 29, "");
        AUDIO_OUTPUT_DEVICE_HDMI_EARC = audioDeviceAttributes3;
        AVB_AUDIO_OUTPUT_DEVICES = Collections.unmodifiableList(Arrays.asList(audioDeviceAttributes, audioDeviceAttributes2, audioDeviceAttributes3));
        STREAM_MUSIC_ATTRIBUTES = new AudioAttributes.Builder().setLegacyStreamType(3).build();
    }

    private static Map<String, String> createsTerminologyToBibliographicMap() {
        HashMap hashMap = new HashMap();
        hashMap.put("sqi", "alb");
        hashMap.put("hye", "arm");
        hashMap.put("eus", "baq");
        hashMap.put("mya", "bur");
        hashMap.put("ces", "cze");
        hashMap.put("nld", "dut");
        hashMap.put("kat", "geo");
        hashMap.put("deu", "ger");
        hashMap.put("ell", "gre");
        hashMap.put("fra", "fre");
        hashMap.put("isl", "ice");
        hashMap.put("mkd", "mac");
        hashMap.put("mri", "mao");
        hashMap.put("msa", "may");
        hashMap.put("fas", "per");
        hashMap.put("ron", "rum");
        hashMap.put("slk", "slo");
        hashMap.put("bod", "tib");
        hashMap.put("cym", "wel");
        return Collections.unmodifiableMap(hashMap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public static String localeToMenuLanguage(Locale locale) {
        if (locale.equals(Locale.TAIWAN) || locale.equals(HONG_KONG) || locale.equals(MACAU)) {
            return "chi";
        }
        String iSO3Language = locale.getISO3Language();
        Map<String, String> map = sTerminologyToBibliographicMap;
        return map.containsKey(iSO3Language) ? map.get(iSO3Language) : iSO3Language;
    }

    Executor getServiceThreadExecutor() {
        return this.mServiceThreadExecutor;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class HdmiControlBroadcastReceiver extends BroadcastReceiver {
        private HdmiControlBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        @HdmiAnnotations.ServiceThreadOnly
        public void onReceive(Context context, Intent intent) {
            HdmiControlService.this.assertRunOnServiceThread();
            boolean contains = SystemProperties.get(ShutdownThread.SHUTDOWN_ACTION_PROPERTY).contains("1");
            String action = intent.getAction();
            action.hashCode();
            char c = 65535;
            switch (action.hashCode()) {
                case -2128145023:
                    if (action.equals("android.intent.action.SCREEN_OFF")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1454123155:
                    if (action.equals("android.intent.action.SCREEN_ON")) {
                        c = 1;
                        break;
                    }
                    break;
                case 158859398:
                    if (action.equals("android.intent.action.CONFIGURATION_CHANGED")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1947666138:
                    if (action.equals("android.intent.action.ACTION_SHUTDOWN")) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    if (!HdmiControlService.this.isPowerOnOrTransient() || contains) {
                        return;
                    }
                    HdmiControlService.this.onStandby(0);
                    return;
                case 1:
                    if (HdmiControlService.this.isPowerStandbyOrTransient()) {
                        HdmiControlService.this.onWakeUp(0);
                        return;
                    }
                    return;
                case 2:
                    String localeToMenuLanguage = HdmiControlService.localeToMenuLanguage(Locale.getDefault());
                    if (HdmiControlService.this.mMenuLanguage.equals(localeToMenuLanguage)) {
                        return;
                    }
                    HdmiControlService.this.onLanguageChanged(localeToMenuLanguage);
                    return;
                case 3:
                    if (!HdmiControlService.this.isPowerOnOrTransient() || contains) {
                        return;
                    }
                    HdmiControlService.this.onStandby(1);
                    return;
                default:
                    return;
            }
        }
    }

    @VisibleForTesting
    HdmiControlService(Context context, List<Integer> list, AudioManagerWrapper audioManagerWrapper, AudioDeviceVolumeManagerWrapper audioDeviceVolumeManagerWrapper) {
        super(context);
        this.mServiceThreadExecutor = new Executor() { // from class: com.android.server.hdmi.HdmiControlService.1
            @Override // java.util.concurrent.Executor
            public void execute(Runnable runnable) {
                HdmiControlService.this.runOnServiceThread(runnable);
            }
        };
        this.mActiveSource = new HdmiCecLocalDevice.ActiveSource();
        this.mSystemAudioActivated = false;
        this.mAudioDeviceVolumeBehaviors = new HashMap();
        this.mIoThread = new HandlerThread("Hdmi Control Io Thread");
        this.mLock = new Object();
        this.mHdmiControlStatusChangeListenerRecords = new ArrayList<>();
        this.mHdmiCecVolumeControlFeatureListenerRecords = new RemoteCallbackList<>();
        this.mHotplugEventListenerRecords = new ArrayList<>();
        this.mDeviceEventListenerRecords = new ArrayList<>();
        this.mVendorCommandListenerRecords = new ArrayList<>();
        this.mHdmiCecSettingChangeListenerRecords = new ArrayMap<>();
        this.mEarcPortId = -1;
        this.mSystemAudioModeChangeListenerRecords = new ArrayList<>();
        Handler handler = new Handler();
        this.mHandler = handler;
        this.mHdmiControlBroadcastReceiver = new HdmiControlBroadcastReceiver();
        this.mDisplayStatusCallback = null;
        this.mOtpCallbackPendingAddressAllocation = null;
        this.mMenuLanguage = localeToMenuLanguage(Locale.getDefault());
        this.mStandbyMessageReceived = false;
        this.mWakeUpMessageReceived = false;
        this.mSoundbarModeFeatureFlagEnabled = false;
        this.mEarcTxFeatureFlagEnabled = false;
        this.mNumericSoundbarVolumeUiOnTvFeatureFlagEnabled = false;
        this.mTransitionFromArcToEarcTxEnabled = false;
        this.mActivePortId = -1;
        this.mMhlVendorCommandListenerRecords = new ArrayList<>();
        this.mLastInputMhl = -1;
        this.mAddressAllocated = false;
        this.mIsCecAvailable = false;
        this.mAtomWriter = new HdmiCecAtomWriter();
        this.mSelectRequestBuffer = new SelectRequestBuffer();
        this.mSettingChangeListener = new AnonymousClass25();
        this.mCecLocalDevices = list;
        this.mSettingsObserver = new SettingsObserver(handler);
        this.mHdmiCecConfig = new HdmiCecConfig(context);
        this.mDeviceConfig = new DeviceConfigWrapper();
        this.mAudioManager = audioManagerWrapper;
        this.mAudioDeviceVolumeManager = audioDeviceVolumeManagerWrapper;
    }

    public HdmiControlService(Context context) {
        super(context);
        this.mServiceThreadExecutor = new Executor() { // from class: com.android.server.hdmi.HdmiControlService.1
            @Override // java.util.concurrent.Executor
            public void execute(Runnable runnable) {
                HdmiControlService.this.runOnServiceThread(runnable);
            }
        };
        this.mActiveSource = new HdmiCecLocalDevice.ActiveSource();
        this.mSystemAudioActivated = false;
        this.mAudioDeviceVolumeBehaviors = new HashMap();
        this.mIoThread = new HandlerThread("Hdmi Control Io Thread");
        this.mLock = new Object();
        this.mHdmiControlStatusChangeListenerRecords = new ArrayList<>();
        this.mHdmiCecVolumeControlFeatureListenerRecords = new RemoteCallbackList<>();
        this.mHotplugEventListenerRecords = new ArrayList<>();
        this.mDeviceEventListenerRecords = new ArrayList<>();
        this.mVendorCommandListenerRecords = new ArrayList<>();
        this.mHdmiCecSettingChangeListenerRecords = new ArrayMap<>();
        this.mEarcPortId = -1;
        this.mSystemAudioModeChangeListenerRecords = new ArrayList<>();
        Handler handler = new Handler();
        this.mHandler = handler;
        this.mHdmiControlBroadcastReceiver = new HdmiControlBroadcastReceiver();
        this.mDisplayStatusCallback = null;
        this.mOtpCallbackPendingAddressAllocation = null;
        this.mMenuLanguage = localeToMenuLanguage(Locale.getDefault());
        this.mStandbyMessageReceived = false;
        this.mWakeUpMessageReceived = false;
        this.mSoundbarModeFeatureFlagEnabled = false;
        this.mEarcTxFeatureFlagEnabled = false;
        this.mNumericSoundbarVolumeUiOnTvFeatureFlagEnabled = false;
        this.mTransitionFromArcToEarcTxEnabled = false;
        this.mActivePortId = -1;
        this.mMhlVendorCommandListenerRecords = new ArrayList<>();
        this.mLastInputMhl = -1;
        this.mAddressAllocated = false;
        this.mIsCecAvailable = false;
        this.mAtomWriter = new HdmiCecAtomWriter();
        this.mSelectRequestBuffer = new SelectRequestBuffer();
        this.mSettingChangeListener = new AnonymousClass25();
        this.mCecLocalDevices = readDeviceTypes();
        this.mSettingsObserver = new SettingsObserver(handler);
        this.mHdmiCecConfig = new HdmiCecConfig(context);
        this.mDeviceConfig = new DeviceConfigWrapper();
    }

    @VisibleForTesting
    protected List<HdmiProperties.cec_device_types_values> getCecDeviceTypes() {
        return HdmiProperties.cec_device_types();
    }

    @VisibleForTesting
    protected List<Integer> getDeviceTypes() {
        return HdmiProperties.device_type();
    }

    @VisibleForTesting
    protected List<Integer> readDeviceTypes() {
        List<HdmiProperties.cec_device_types_values> cecDeviceTypes = getCecDeviceTypes();
        if (!cecDeviceTypes.isEmpty()) {
            if (cecDeviceTypes.contains(null)) {
                Slog.w(TAG, "Error parsing ro.hdmi.cec_device_types: " + SystemProperties.get("ro.hdmi.cec_device_types"));
            }
            return (List) cecDeviceTypes.stream().map(new Function() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    Integer enumToIntDeviceType;
                    enumToIntDeviceType = HdmiControlService.enumToIntDeviceType((HdmiProperties.cec_device_types_values) obj);
                    return enumToIntDeviceType;
                }
            }).filter(new Predicate() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return Objects.nonNull((Integer) obj);
                }
            }).collect(Collectors.toList());
        }
        List<Integer> deviceTypes = getDeviceTypes();
        if (deviceTypes.contains(null)) {
            Slog.w(TAG, "Error parsing ro.hdmi.device_type: " + SystemProperties.get("ro.hdmi.device_type"));
        }
        return (List) deviceTypes.stream().filter(new Predicate() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.nonNull((Integer) obj);
            }
        }).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.hdmi.HdmiControlService$28, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static /* synthetic */ class AnonymousClass28 {
        static final /* synthetic */ int[] $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values;

        static {
            int[] iArr = new int[HdmiProperties.cec_device_types_values.values().length];
            $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values = iArr;
            try {
                iArr[HdmiProperties.cec_device_types_values.TV.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[HdmiProperties.cec_device_types_values.RECORDING_DEVICE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[HdmiProperties.cec_device_types_values.RESERVED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[HdmiProperties.cec_device_types_values.TUNER.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[HdmiProperties.cec_device_types_values.PLAYBACK_DEVICE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[HdmiProperties.cec_device_types_values.AUDIO_SYSTEM.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[HdmiProperties.cec_device_types_values.PURE_CEC_SWITCH.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[HdmiProperties.cec_device_types_values.VIDEO_PROCESSOR.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Integer enumToIntDeviceType(HdmiProperties.cec_device_types_values cec_device_types_valuesVar) {
        if (cec_device_types_valuesVar == null) {
            return null;
        }
        switch (AnonymousClass28.$SwitchMap$android$sysprop$HdmiProperties$cec_device_types_values[cec_device_types_valuesVar.ordinal()]) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
            case 8:
                return 7;
            default:
                Slog.w(TAG, "Unrecognized device type in ro.hdmi.cec_device_types: " + cec_device_types_valuesVar.getPropValue());
                return null;
        }
    }

    protected static List<Integer> getIntList(String str) {
        ArrayList arrayList = new ArrayList();
        TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(',');
        simpleStringSplitter.setString(str);
        Iterator<String> it = simpleStringSplitter.iterator();
        while (it.hasNext()) {
            String next = it.next();
            try {
                arrayList.add(Integer.valueOf(Integer.parseInt(next)));
            } catch (NumberFormatException unused) {
                Slog.w(TAG, "Can't parseInt: " + next);
            }
        }
        return Collections.unmodifiableList(arrayList);
    }

    public void onStart() {
        initService();
        publishBinderService("hdmi_control", new BinderService());
        if (this.mCecController != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
            intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
            getContext().registerReceiver(this.mHdmiControlBroadcastReceiver, intentFilter);
            registerContentObserver();
        }
        this.mMhlController.setOption(HdmiCecKeycode.CEC_KEYCODE_SELECT_MEDIA_FUNCTION, 1);
    }

    @VisibleForTesting
    void initService() {
        if (this.mIoLooper == null) {
            this.mIoThread.start();
            this.mIoLooper = this.mIoThread.getLooper();
        }
        if (this.mPowerStatusController == null) {
            this.mPowerStatusController = new HdmiCecPowerStatusController(this);
        }
        this.mPowerStatusController.setPowerStatus(getInitialPowerStatus());
        setProhibitMode(false);
        this.mHdmiControlEnabled = this.mHdmiCecConfig.getIntValue("hdmi_cec_enabled");
        this.mSoundbarModeFeatureFlagEnabled = this.mDeviceConfig.getBoolean("enable_soundbar_mode", false);
        this.mEarcTxFeatureFlagEnabled = this.mDeviceConfig.getBoolean("enable_earc_tx", false);
        this.mTransitionFromArcToEarcTxEnabled = this.mDeviceConfig.getBoolean("transition_arc_to_earc_tx", false);
        this.mNumericSoundbarVolumeUiOnTvFeatureFlagEnabled = this.mDeviceConfig.getBoolean("enable_numeric_soundbar_volume_ui_on_tv", false);
        synchronized (this.mLock) {
            this.mEarcEnabled = this.mHdmiCecConfig.getIntValue("earc_enabled") == 1;
            if (isTvDevice()) {
                this.mEarcEnabled &= this.mEarcTxFeatureFlagEnabled;
            }
        }
        setHdmiCecVolumeControlEnabledInternal(getHdmiCecConfig().getIntValue("volume_control_enabled"));
        this.mMhlInputChangeEnabled = readBooleanSetting("mhl_input_switching_enabled", true);
        if (this.mCecMessageBuffer == null) {
            this.mCecMessageBuffer = new CecMessageBuffer(this);
        }
        if (this.mCecController == null) {
            this.mCecController = HdmiCecController.create(this, getAtomWriter());
        }
        if (this.mCecController == null) {
            Slog.i(TAG, "Device does not support HDMI-CEC.");
            return;
        }
        if (this.mMhlController == null) {
            this.mMhlController = HdmiMhlControllerStub.create(this);
        }
        if (!this.mMhlController.isReady()) {
            Slog.i(TAG, "Device does not support MHL-control.");
        }
        if (this.mEarcController == null) {
            this.mEarcController = HdmiEarcController.create(this);
        }
        if (this.mEarcController == null) {
            Slog.i(TAG, "Device does not support eARC.");
        }
        this.mHdmiCecNetwork = new HdmiCecNetwork(this, this.mCecController, this.mMhlController);
        if (isCecControlEnabled()) {
            initializeCec(1);
        } else {
            this.mCecController.enableCec(false);
        }
        synchronized (this.mLock) {
            this.mMhlDevices = Collections.emptyList();
        }
        this.mHdmiCecNetwork.initPortInfo();
        List<HdmiPortInfo> portInfo = getPortInfo();
        synchronized (this.mLock) {
            this.mEarcSupported = false;
            Iterator<HdmiPortInfo> it = portInfo.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                HdmiPortInfo next = it.next();
                boolean isEarcSupported = next.isEarcSupported();
                if (isEarcSupported && this.mEarcSupported) {
                    Slog.e(TAG, "HDMI eARC supported on more than 1 port.");
                    this.mEarcSupported = false;
                    this.mEarcPortId = -1;
                    break;
                } else if (isEarcSupported) {
                    this.mEarcPortId = next.getId();
                    this.mEarcSupported = isEarcSupported;
                }
            }
            this.mEarcSupported &= this.mEarcController != null;
        }
        if (isEarcSupported()) {
            if (isEarcEnabled()) {
                initializeEarc(1);
            } else {
                setEarcEnabledInHal(false, false);
            }
        }
        this.mHdmiCecConfig.registerChangeListener("hdmi_cec_enabled", new HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.2
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(String str) {
                HdmiControlService.this.setCecEnabled(HdmiControlService.this.mHdmiCecConfig.getIntValue("hdmi_cec_enabled"));
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("hdmi_cec_version", new HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.3
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(String str) {
                HdmiControlService.this.initializeCec(0);
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("routing_control", new HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.4
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(String str) {
                boolean z = HdmiControlService.this.mHdmiCecConfig.getIntValue("routing_control") == 1;
                if (HdmiControlService.this.isAudioSystemDevice()) {
                    if (HdmiControlService.this.audioSystem() == null) {
                        Slog.w(HdmiControlService.TAG, "Switch device has not registered yet. Can't turn routing on.");
                    } else {
                        HdmiControlService.this.audioSystem().setRoutingControlFeatureEnabled(z);
                    }
                }
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("system_audio_control", new HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.5
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(String str) {
                boolean z = HdmiControlService.this.mHdmiCecConfig.getIntValue("system_audio_control") == 1;
                if (HdmiControlService.this.isTvDeviceEnabled()) {
                    HdmiControlService.this.tv().setSystemAudioControlFeatureEnabled(z);
                }
                if (HdmiControlService.this.isAudioSystemDevice()) {
                    if (HdmiControlService.this.audioSystem() == null) {
                        Slog.e(HdmiControlService.TAG, "Audio System device has not registered yet. Can't turn system audio mode on.");
                    } else {
                        HdmiControlService.this.audioSystem().onSystemAudioControlFeatureSupportChanged(z);
                    }
                }
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("volume_control_enabled", new HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.6
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(String str) {
                HdmiControlService hdmiControlService = HdmiControlService.this;
                hdmiControlService.setHdmiCecVolumeControlEnabledInternal(hdmiControlService.getHdmiCecConfig().getIntValue("volume_control_enabled"));
            }
        }, this.mServiceThreadExecutor);
        this.mHdmiCecConfig.registerChangeListener("tv_wake_on_one_touch_play", new HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.7
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(String str) {
                if (HdmiControlService.this.isTvDeviceEnabled()) {
                    HdmiControlService.this.mCecController.enableWakeupByOtp(HdmiControlService.this.tv().getAutoWakeup());
                }
            }
        }, this.mServiceThreadExecutor);
        if (isTvDevice()) {
            this.mDeviceConfig.addOnPropertiesChangedListener(getContext().getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.hdmi.HdmiControlService.8
                public void onPropertiesChanged(DeviceConfig.Properties properties) {
                    int i = 0;
                    HdmiControlService.this.mEarcTxFeatureFlagEnabled = properties.getBoolean("enable_earc_tx", false);
                    boolean z = HdmiControlService.this.mHdmiCecConfig.getIntValue("earc_enabled") == 1;
                    HdmiControlService hdmiControlService = HdmiControlService.this;
                    if (z && hdmiControlService.mEarcTxFeatureFlagEnabled) {
                        i = 1;
                    }
                    hdmiControlService.setEarcEnabled(i);
                }
            });
        }
        this.mHdmiCecConfig.registerChangeListener("earc_enabled", new HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.9
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(String str) {
                if (HdmiControlService.this.isTvDevice()) {
                    int i = 0;
                    boolean z = HdmiControlService.this.mHdmiCecConfig.getIntValue("earc_enabled") == 1;
                    HdmiControlService hdmiControlService = HdmiControlService.this;
                    if (z && hdmiControlService.mEarcTxFeatureFlagEnabled) {
                        i = 1;
                    }
                    hdmiControlService.setEarcEnabled(i);
                    return;
                }
                HdmiControlService hdmiControlService2 = HdmiControlService.this;
                hdmiControlService2.setEarcEnabled(hdmiControlService2.mHdmiCecConfig.getIntValue("earc_enabled"));
            }
        }, this.mServiceThreadExecutor);
        this.mDeviceConfig.addOnPropertiesChangedListener(getContext().getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.hdmi.HdmiControlService.10
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                int i = 0;
                HdmiControlService.this.mSoundbarModeFeatureFlagEnabled = properties.getBoolean("enable_soundbar_mode", false);
                boolean z = HdmiControlService.this.mHdmiCecConfig.getIntValue("soundbar_mode") == 1;
                HdmiControlService hdmiControlService = HdmiControlService.this;
                if (z && hdmiControlService.mSoundbarModeFeatureFlagEnabled) {
                    i = 1;
                }
                hdmiControlService.setSoundbarMode(i);
            }
        });
        this.mHdmiCecConfig.registerChangeListener("soundbar_mode", new HdmiCecConfig.SettingChangeListener() { // from class: com.android.server.hdmi.HdmiControlService.11
            @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
            public void onChange(String str) {
                int i = 0;
                boolean z = HdmiControlService.this.mHdmiCecConfig.getIntValue("soundbar_mode") == 1;
                HdmiControlService hdmiControlService = HdmiControlService.this;
                if (z && hdmiControlService.mSoundbarModeFeatureFlagEnabled) {
                    i = 1;
                }
                hdmiControlService.setSoundbarMode(i);
            }
        }, this.mServiceThreadExecutor);
        this.mDeviceConfig.addOnPropertiesChangedListener(getContext().getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.hdmi.HdmiControlService.12
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                HdmiControlService.this.mTransitionFromArcToEarcTxEnabled = properties.getBoolean("transition_arc_to_earc_tx", false);
            }
        });
        this.mDeviceConfig.addOnPropertiesChangedListener(getContext().getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.hdmi.HdmiControlService.13
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                HdmiControlService.this.mNumericSoundbarVolumeUiOnTvFeatureFlagEnabled = properties.getBoolean("enable_numeric_soundbar_volume_ui_on_tv", false);
                HdmiControlService.this.checkAndUpdateAbsoluteVolumeBehavior();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isScreenOff() {
        return this.mDisplayManager.getDisplay(0).getState() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bootCompleted() {
        if (this.mPowerManager.isInteractive() && isPowerStandbyOrTransient()) {
            this.mPowerStatusController.setPowerStatus(0);
            if (this.mAddressAllocated) {
                Iterator<HdmiCecLocalDevice> it = getAllCecLocalDevices().iterator();
                while (it.hasNext()) {
                    it.next().startQueuedActions();
                }
            }
        }
    }

    @VisibleForTesting
    void setCecController(HdmiCecController hdmiCecController) {
        this.mCecController = hdmiCecController;
    }

    @VisibleForTesting
    void setEarcController(HdmiEarcController hdmiEarcController) {
        this.mEarcController = hdmiEarcController;
    }

    @VisibleForTesting
    void setHdmiCecNetwork(HdmiCecNetwork hdmiCecNetwork) {
        this.mHdmiCecNetwork = hdmiCecNetwork;
    }

    @VisibleForTesting
    void setHdmiCecConfig(HdmiCecConfig hdmiCecConfig) {
        this.mHdmiCecConfig = hdmiCecConfig;
    }

    public HdmiCecNetwork getHdmiCecNetwork() {
        return this.mHdmiCecNetwork;
    }

    @VisibleForTesting
    void setHdmiMhlController(HdmiMhlControllerStub hdmiMhlControllerStub) {
        this.mMhlController = hdmiMhlControllerStub;
    }

    public void onBootPhase(int i) {
        if (i != 500) {
            if (i == 1000) {
                runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        HdmiControlService.this.bootCompleted();
                    }
                });
                return;
            }
            return;
        }
        this.mDisplayManager = (DisplayManager) getContext().getSystemService(DisplayManager.class);
        this.mTvInputManager = (TvInputManager) getContext().getSystemService("tv_input");
        this.mPowerManager = new PowerManagerWrapper(getContext());
        this.mPowerManagerInternal = new PowerManagerInternalWrapper();
        if (this.mAudioManager == null) {
            this.mAudioManager = new DefaultAudioManagerWrapper(getContext());
        }
        this.mStreamMusicMaxVolume = getAudioManager().getStreamMaxVolume(3);
        if (this.mAudioDeviceVolumeManager == null) {
            this.mAudioDeviceVolumeManager = new DefaultAudioDeviceVolumeManagerWrapper(getContext());
        }
        getAudioDeviceVolumeManager().addOnDeviceVolumeBehaviorChangedListener(this.mServiceThreadExecutor, new AudioDeviceVolumeManager.OnDeviceVolumeBehaviorChangedListener() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda3
            public final void onDeviceVolumeBehaviorChanged(AudioDeviceAttributes audioDeviceAttributes, int i2) {
                HdmiControlService.this.onDeviceVolumeBehaviorChanged(audioDeviceAttributes, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TvInputManager getTvInputManager() {
        return this.mTvInputManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerTvInputCallback(TvInputManager.TvInputCallback tvInputCallback) {
        TvInputManager tvInputManager = this.mTvInputManager;
        if (tvInputManager == null) {
            return;
        }
        tvInputManager.registerCallback(tvInputCallback, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterTvInputCallback(TvInputManager.TvInputCallback tvInputCallback) {
        TvInputManager tvInputManager = this.mTvInputManager;
        if (tvInputManager == null) {
            return;
        }
        tvInputManager.unregisterCallback(tvInputCallback);
    }

    @VisibleForTesting
    void setDeviceConfig(DeviceConfigWrapper deviceConfigWrapper) {
        this.mDeviceConfig = deviceConfigWrapper;
    }

    @VisibleForTesting
    void setPowerManager(PowerManagerWrapper powerManagerWrapper) {
        this.mPowerManager = powerManagerWrapper;
    }

    @VisibleForTesting
    void setPowerManagerInternal(PowerManagerInternalWrapper powerManagerInternalWrapper) {
        this.mPowerManagerInternal = powerManagerInternalWrapper;
    }

    DeviceConfigWrapper getDeviceConfig() {
        return this.mDeviceConfig;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PowerManagerWrapper getPowerManager() {
        return this.mPowerManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PowerManagerInternalWrapper getPowerManagerInternal() {
        return this.mPowerManagerInternal;
    }

    @VisibleForTesting
    public void setSoundbarMode(int i) {
        boolean z;
        HdmiCecLocalDevicePlayback playback = playback();
        HdmiCecLocalDeviceAudioSystem audioSystem = audioSystem();
        if (playback == null) {
            Slog.w(TAG, "Device type not compatible to change soundbar mode.");
            return;
        }
        if (!SystemProperties.getBoolean("persist.sys.hdmi.property_arc_support", true)) {
            Slog.w(TAG, "Device type doesn't support ARC.");
            return;
        }
        if (i != 0 || audioSystem == null) {
            z = false;
        } else {
            z = audioSystem.isArcEnabled();
            if (isSystemAudioActivated()) {
                audioSystem.terminateSystemAudioMode();
            }
            if (z) {
                if (audioSystem.hasAction(ArcTerminationActionFromAvr.class)) {
                    audioSystem.removeAction(ArcTerminationActionFromAvr.class);
                }
                audioSystem.addAndStartAction(new ArcTerminationActionFromAvr(audioSystem, new IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiControlService.14
                    public void onComplete(int i2) {
                        HdmiControlService.this.mAddressAllocated = false;
                        HdmiControlService.this.initializeCecLocalDevices(5);
                    }
                }));
            }
        }
        if (z) {
            return;
        }
        this.mAddressAllocated = false;
        initializeCecLocalDevices(5);
    }

    public boolean isDeviceDiscoveryHandledByPlayback() {
        HdmiCecLocalDevicePlayback playback = playback();
        if (playback != null) {
            return playback.hasAction(DeviceDiscoveryAction.class) || playback.hasAction(HotplugDetectionAction.class);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInitializeCecComplete(int i) {
        updatePowerStatusOnInitializeCecComplete();
        int i2 = 0;
        this.mWakeUpMessageReceived = false;
        if (isTvDeviceEnabled()) {
            this.mCecController.enableWakeupByOtp(tv().getAutoWakeup());
        }
        if (i == 0) {
            i2 = 1;
        } else if (i != 1) {
            i2 = 2;
            if (i == 2) {
                Iterator<HdmiCecLocalDevice> it = getAllCecLocalDevices().iterator();
                while (it.hasNext()) {
                    it.next().onInitializeCecComplete(i);
                }
            } else if (i != 3) {
                i2 = -1;
            }
        }
        if (i2 != -1) {
            invokeVendorCommandListenersOnControlStateChanged(true, i2);
            announceHdmiControlStatusChange(1);
        }
    }

    private void updatePowerStatusOnInitializeCecComplete() {
        if (this.mPowerStatusController.isPowerStatusTransientToOn()) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    HdmiControlService.this.lambda$updatePowerStatusOnInitializeCecComplete$0();
                }
            });
        } else if (this.mPowerStatusController.isPowerStatusTransientToStandby()) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    HdmiControlService.this.lambda$updatePowerStatusOnInitializeCecComplete$1();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePowerStatusOnInitializeCecComplete$0() {
        this.mPowerStatusController.setPowerStatus(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePowerStatusOnInitializeCecComplete$1() {
        this.mPowerStatusController.setPowerStatus(1);
    }

    private void registerContentObserver() {
        ContentResolver contentResolver = getContext().getContentResolver();
        String[] strArr = {"mhl_input_switching_enabled", "mhl_power_charge_enabled", "device_name"};
        for (int i = 0; i < 3; i++) {
            contentResolver.registerContentObserver(Settings.Global.getUriFor(strArr[i]), false, this.mSettingsObserver, -1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class SettingsObserver extends ContentObserver {
        public SettingsObserver(Handler handler) {
            super(handler);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:17:0x002b, code lost:
        
            if (r4.equals("mhl_input_switching_enabled") == false) goto L4;
         */
        @Override // android.database.ContentObserver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onChange(boolean z, Uri uri) {
            String lastPathSegment = uri.getLastPathSegment();
            char c = 1;
            boolean readBooleanSetting = HdmiControlService.this.readBooleanSetting(lastPathSegment, true);
            lastPathSegment.hashCode();
            switch (lastPathSegment.hashCode()) {
                case -1543071020:
                    if (lastPathSegment.equals("device_name")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -1262529811:
                    break;
                case -885757826:
                    if (lastPathSegment.equals("mhl_power_charge_enabled")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    HdmiControlService.this.setDisplayName(HdmiControlService.this.readStringSetting(lastPathSegment, Build.MODEL));
                    return;
                case 1:
                    HdmiControlService.this.setMhlInputChangeEnabled(readBooleanSetting);
                    return;
                case 2:
                    HdmiControlService.this.mMhlController.setOption(HdmiCecKeycode.CEC_KEYCODE_RESTORE_VOLUME_FUNCTION, HdmiControlService.toInt(readBooleanSetting));
                    return;
                default:
                    return;
            }
        }
    }

    @VisibleForTesting
    boolean readBooleanSetting(String str, boolean z) {
        return Settings.Global.getInt(getContext().getContentResolver(), str, toInt(z)) == 1;
    }

    @VisibleForTesting
    int readIntSetting(String str, int i) {
        return Settings.Global.getInt(getContext().getContentResolver(), str, i);
    }

    void writeBooleanSetting(String str, boolean z) {
        Settings.Global.putInt(getContext().getContentResolver(), str, toInt(z));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    public void writeStringSystemProperty(String str, String str2) {
        SystemProperties.set(str, str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean readBooleanSystemProperty(String str, boolean z) {
        return SystemProperties.getBoolean(str, z);
    }

    String readStringSetting(String str, String str2) {
        String string = Settings.Global.getString(getContext().getContentResolver(), str);
        return TextUtils.isEmpty(string) ? str2 : string;
    }

    void writeStringSetting(String str, String str2) {
        Settings.Global.putString(getContext().getContentResolver(), str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initializeCec(int i) {
        this.mAddressAllocated = false;
        this.mCecVersion = Math.max(5, Math.min(getHdmiCecConfig().getIntValue("hdmi_cec_version"), this.mCecController.getVersion()));
        this.mCecController.enableSystemCecControl(true);
        this.mCecController.setLanguage(this.mMenuLanguage);
        initializeCecLocalDevices(i);
    }

    @HdmiAnnotations.ServiceThreadOnly
    private List<Integer> getCecLocalDeviceTypes() {
        ArrayList arrayList = new ArrayList(this.mCecLocalDevices);
        if (this.mHdmiCecConfig.getIntValue("soundbar_mode") == 1 && !arrayList.contains(5) && SystemProperties.getBoolean("persist.sys.hdmi.property_arc_support", true) && this.mSoundbarModeFeatureFlagEnabled) {
            arrayList.add(5);
        }
        return arrayList;
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    protected void initializeCecLocalDevices(int i) {
        assertRunOnServiceThread();
        ArrayList<HdmiCecLocalDevice> arrayList = new ArrayList<>();
        Iterator<Integer> it = getCecLocalDeviceTypes().iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            HdmiCecLocalDevice localDevice = this.mHdmiCecNetwork.getLocalDevice(intValue);
            if (localDevice == null) {
                localDevice = HdmiCecLocalDevice.create(this, intValue);
            }
            localDevice.init();
            arrayList.add(localDevice);
        }
        clearCecLocalDevices();
        this.mHdmiCecNetwork.clearDeviceList();
        allocateLogicalAddress(arrayList, i);
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    protected void allocateLogicalAddress(final ArrayList<HdmiCecLocalDevice> arrayList, final int i) {
        assertRunOnServiceThread();
        this.mCecController.clearLogicalAddress();
        final ArrayList arrayList2 = new ArrayList();
        final int[] iArr = new int[1];
        this.mAddressAllocated = arrayList.isEmpty();
        this.mSelectRequestBuffer.clear();
        Iterator<HdmiCecLocalDevice> it = arrayList.iterator();
        while (it.hasNext()) {
            final HdmiCecLocalDevice next = it.next();
            this.mCecController.allocateLogicalAddress(next.getType(), next.getPreferredAddress(), new HdmiCecController.AllocateAddressCallback() { // from class: com.android.server.hdmi.HdmiControlService.15
                @Override // com.android.server.hdmi.HdmiCecController.AllocateAddressCallback
                public void onAllocated(int i2, int i3) {
                    if (i3 == 15) {
                        Slog.e(HdmiControlService.TAG, "Failed to allocate address:[device_type:" + i2 + "]");
                    } else {
                        HdmiControlService hdmiControlService = HdmiControlService.this;
                        next.setDeviceInfo(hdmiControlService.createDeviceInfo(i3, i2, 0, hdmiControlService.getCecVersion()));
                        HdmiControlService.this.mHdmiCecNetwork.addLocalDevice(i2, next);
                        HdmiControlService.this.mHdmiCecNetwork.addCecDevice(next.getDeviceInfo());
                        HdmiControlService.this.mCecController.addLogicalAddress(i3);
                        arrayList2.add(next);
                    }
                    int size = arrayList.size();
                    int[] iArr2 = iArr;
                    int i4 = iArr2[0] + 1;
                    iArr2[0] = i4;
                    if (size == i4) {
                        int i5 = i;
                        if (i5 != 4 && i5 != 5) {
                            HdmiControlService.this.onInitializeCecComplete(i5);
                        }
                        HdmiControlService.this.mAddressAllocated = true;
                        HdmiControlService.this.notifyAddressAllocated(arrayList2, i);
                        if (HdmiControlService.this.mDisplayStatusCallback != null) {
                            HdmiControlService hdmiControlService2 = HdmiControlService.this;
                            hdmiControlService2.queryDisplayStatus(hdmiControlService2.mDisplayStatusCallback);
                            HdmiControlService.this.mDisplayStatusCallback = null;
                        }
                        if (HdmiControlService.this.mOtpCallbackPendingAddressAllocation != null) {
                            HdmiControlService hdmiControlService3 = HdmiControlService.this;
                            hdmiControlService3.oneTouchPlay(hdmiControlService3.mOtpCallbackPendingAddressAllocation);
                            HdmiControlService.this.mOtpCallbackPendingAddressAllocation = null;
                        }
                        HdmiControlService.this.mCecMessageBuffer.processMessages();
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @HdmiAnnotations.ServiceThreadOnly
    public void notifyAddressAllocated(ArrayList<HdmiCecLocalDevice> arrayList, int i) {
        assertRunOnServiceThread();
        List<HdmiCecMessage> buffer = this.mCecMessageBuffer.getBuffer();
        Iterator<HdmiCecLocalDevice> it = arrayList.iterator();
        while (it.hasNext()) {
            HdmiCecLocalDevice next = it.next();
            next.handleAddressAllocated(next.getDeviceInfo().getLogicalAddress(), buffer, i);
        }
        if (isTvDeviceEnabled()) {
            tv().setSelectRequestBuffer(this.mSelectRequestBuffer);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAddressAllocated() {
        return this.mAddressAllocated;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<HdmiPortInfo> getPortInfo() {
        List<HdmiPortInfo> portInfo;
        synchronized (this.mLock) {
            portInfo = this.mHdmiCecNetwork.getPortInfo();
        }
        return portInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiPortInfo getPortInfo(int i) {
        return this.mHdmiCecNetwork.getPortInfo(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int portIdToPath(int i) {
        return this.mHdmiCecNetwork.portIdToPath(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int pathToPortId(int i) {
        return this.mHdmiCecNetwork.physicalAddressToPortId(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isValidPortId(int i) {
        return this.mHdmiCecNetwork.getPortInfo(i) != null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    public Looper getIoLooper() {
        return this.mIoLooper;
    }

    @VisibleForTesting
    void setIoLooper(Looper looper) {
        this.mIoLooper = looper;
    }

    @VisibleForTesting
    void setCecMessageBuffer(CecMessageBuffer cecMessageBuffer) {
        this.mCecMessageBuffer = cecMessageBuffer;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Looper getServiceLooper() {
        return this.mHandler.getLooper();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPhysicalAddress() {
        return this.mCecController.getPhysicalAddress();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVendorId() {
        return this.mCecController.getVendorId();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public HdmiDeviceInfo getDeviceInfo(int i) {
        assertRunOnServiceThread();
        return this.mHdmiCecNetwork.getCecDeviceInfo(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public HdmiDeviceInfo getDeviceInfoByPort(int i) {
        assertRunOnServiceThread();
        HdmiMhlLocalDeviceStub localDevice = this.mMhlController.getLocalDevice(i);
        if (localDevice != null) {
            return localDevice.getInfo();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    public int getCecVersion() {
        return this.mCecVersion;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isConnectedToArcPort(int i) {
        return this.mHdmiCecNetwork.isConnectedToArcPort(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public boolean isConnected(int i) {
        assertRunOnServiceThread();
        return this.mCecController.isConnected(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void runOnServiceThread(Runnable runnable) {
        this.mHandler.post(new WorkSourceUidPreservingRunnable(runnable));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertRunOnServiceThread() {
        if (Looper.myLooper() != this.mHandler.getLooper()) {
            throw new IllegalStateException("Should run on service thread.");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void sendCecCommand(HdmiCecMessage hdmiCecMessage, SendMessageCallback sendMessageCallback) {
        assertRunOnServiceThread();
        if (hdmiCecMessage.getValidationResult() == 0 && verifyPhysicalAddresses(hdmiCecMessage)) {
            this.mCecController.sendCommand(hdmiCecMessage, sendMessageCallback);
            return;
        }
        HdmiLogger.error("Invalid message type:" + hdmiCecMessage, new Object[0]);
        if (sendMessageCallback != null) {
            sendMessageCallback.onSendCompleted(3);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void sendCecCommand(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        sendCecCommand(hdmiCecMessage, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void maySendFeatureAbortCommand(HdmiCecMessage hdmiCecMessage, int i) {
        assertRunOnServiceThread();
        this.mCecController.maySendFeatureAbortCommand(hdmiCecMessage, i);
    }

    boolean verifyPhysicalAddresses(HdmiCecMessage hdmiCecMessage) {
        byte[] params = hdmiCecMessage.getParams();
        int opcode = hdmiCecMessage.getOpcode();
        if (opcode == 112) {
            return params.length == 0 || verifyPhysicalAddress(params, 0);
        }
        if (opcode != 132 && opcode != 134 && opcode != 157) {
            if (opcode == 161 || opcode == 162) {
                return verifyExternalSourcePhysicalAddress(params, 7);
            }
            switch (opcode) {
                case 128:
                    return verifyPhysicalAddress(params, 0) && verifyPhysicalAddress(params, 2);
                case 129:
                case 130:
                    break;
                default:
                    return true;
            }
        }
        return verifyPhysicalAddress(params, 0);
    }

    private boolean verifyPhysicalAddress(byte[] bArr, int i) {
        if (!isTvDevice()) {
            return true;
        }
        if (bArr.length < i + 2) {
            return false;
        }
        int twoBytesToInt = HdmiUtils.twoBytesToInt(bArr, i);
        return (twoBytesToInt != 65535 && twoBytesToInt == getPhysicalAddress()) || pathToPortId(twoBytesToInt) != -1;
    }

    private boolean verifyExternalSourcePhysicalAddress(byte[] bArr, int i) {
        byte b = bArr[i];
        int i2 = i + 1;
        if (b != 5 || bArr.length - i2 < 2) {
            return true;
        }
        return verifyPhysicalAddress(bArr, i2);
    }

    private boolean sourceAddressIsLocal(HdmiCecMessage hdmiCecMessage) {
        Iterator<HdmiCecLocalDevice> it = getAllCecLocalDevices().iterator();
        while (it.hasNext()) {
            if (hdmiCecMessage.getSource() == it.next().getDeviceInfo().getLogicalAddress() && hdmiCecMessage.getSource() != 15) {
                HdmiLogger.warning("Unexpected source: message sent from device itself, " + hdmiCecMessage, new Object[0]);
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    public int handleCecCommand(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        int validationResult = hdmiCecMessage.getValidationResult();
        int i = 3;
        if (validationResult != 3 && validationResult != 5 && verifyPhysicalAddresses(hdmiCecMessage)) {
            i = -1;
            if (validationResult == 0 && !sourceAddressIsLocal(hdmiCecMessage)) {
                getHdmiCecNetwork().handleCecMessage(hdmiCecMessage);
                int dispatchMessageToLocalDevice = dispatchMessageToLocalDevice(hdmiCecMessage);
                if (this.mAddressAllocated || !this.mCecMessageBuffer.bufferMessage(hdmiCecMessage)) {
                    return dispatchMessageToLocalDevice;
                }
                return -1;
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enableAudioReturnChannel(int i, boolean z) {
        if (!this.mTransitionFromArcToEarcTxEnabled && z && this.mEarcController != null) {
            setEarcEnabledInHal(false, false);
        }
        this.mCecController.enableAudioReturnChannel(i, z);
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    protected int dispatchMessageToLocalDevice(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        Iterator<HdmiCecLocalDevice> it = this.mHdmiCecNetwork.getLocalDeviceList().iterator();
        while (it.hasNext()) {
            int dispatchMessage = it.next().dispatchMessage(hdmiCecMessage);
            if (dispatchMessage != -2 && hdmiCecMessage.getDestination() != 15) {
                return dispatchMessage;
            }
        }
        if (hdmiCecMessage.getDestination() == 15) {
            return -1;
        }
        HdmiLogger.warning("Unhandled cec command:" + hdmiCecMessage, new Object[0]);
        return -2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void onHotplug(int i, boolean z) {
        assertRunOnServiceThread();
        this.mHdmiCecNetwork.initPortInfo();
        if (z && !isTvDevice() && getPortInfo(i).getType() == 1) {
            ArrayList<HdmiCecLocalDevice> arrayList = new ArrayList<>();
            Iterator<Integer> it = getCecLocalDeviceTypes().iterator();
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                HdmiCecLocalDevice localDevice = this.mHdmiCecNetwork.getLocalDevice(intValue);
                if (localDevice == null) {
                    localDevice = HdmiCecLocalDevice.create(this, intValue);
                    localDevice.init();
                }
                arrayList.add(localDevice);
            }
            allocateLogicalAddress(arrayList, 4);
        }
        Iterator<HdmiCecLocalDevice> it2 = this.mHdmiCecNetwork.getLocalDeviceList().iterator();
        while (it2.hasNext()) {
            it2.next().onHotplug(i, z);
        }
        announceHotplugEvent(i, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void pollDevices(DevicePollingCallback devicePollingCallback, int i, int i2, int i3) {
        assertRunOnServiceThread();
        this.mCecController.pollDevices(devicePollingCallback, i, checkPollStrategy(i2), i3);
    }

    private int checkPollStrategy(int i) {
        int i2 = i & 3;
        if (i2 == 0) {
            throw new IllegalArgumentException("Invalid poll strategy:" + i);
        }
        int i3 = 196608 & i;
        if (i3 != 0) {
            return i2 | i3;
        }
        throw new IllegalArgumentException("Invalid iteration strategy:" + i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<HdmiCecLocalDevice> getAllCecLocalDevices() {
        assertRunOnServiceThread();
        return this.mHdmiCecNetwork.getLocalDeviceList();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkLogicalAddressConflictAndReallocate(int i, int i2) {
        if (i2 == getPhysicalAddress()) {
            return;
        }
        for (HdmiCecLocalDevice hdmiCecLocalDevice : getAllCecLocalDevices()) {
            if (hdmiCecLocalDevice.getDeviceInfo().getLogicalAddress() == i) {
                HdmiLogger.debug("allocate logical address for " + hdmiCecLocalDevice.getDeviceInfo(), new Object[0]);
                ArrayList<HdmiCecLocalDevice> arrayList = new ArrayList<>();
                arrayList.add(hdmiCecLocalDevice);
                allocateLogicalAddress(arrayList, 4);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object getServiceLock() {
        return this.mLock;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAudioStatus(boolean z, int i) {
        if (isTvDeviceEnabled() && tv().isSystemAudioActivated() && tv().isArcEstablished() && getHdmiCecVolumeControl() != 0) {
            AudioManagerWrapper audioManager = getAudioManager();
            boolean isStreamMute = audioManager.isStreamMute(3);
            if (z) {
                if (isStreamMute) {
                    return;
                }
                audioManager.setStreamMute(3, true);
                return;
            }
            if (isStreamMute) {
                audioManager.setStreamMute(3, false);
            }
            if (i < 0 || i > 100) {
                return;
            }
            Slog.i(TAG, "volume: " + i);
            audioManager.setStreamVolume(3, i, UsbTerminalTypes.TERMINAL_USB_STREAMING);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void announceSystemAudioModeChange(boolean z) {
        synchronized (this.mLock) {
            Iterator<SystemAudioModeChangeListenerRecord> it = this.mSystemAudioModeChangeListenerRecords.iterator();
            while (it.hasNext()) {
                invokeSystemAudioModeChangeLocked(it.next().mListener, z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HdmiDeviceInfo createDeviceInfo(int i, int i2, int i3, int i4) {
        return HdmiDeviceInfo.cecDeviceBuilder().setLogicalAddress(i).setPhysicalAddress(getPhysicalAddress()).setPortId(pathToPortId(getPhysicalAddress())).setDeviceType(i2).setVendorId(getVendorId()).setDisplayName(readStringSetting("device_name", Build.MODEL)).setDevicePowerStatus(i3).setCecVersion(i4).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisplayName(String str) {
        for (HdmiCecLocalDevice hdmiCecLocalDevice : getAllCecLocalDevices()) {
            HdmiDeviceInfo deviceInfo = hdmiCecLocalDevice.getDeviceInfo();
            if (!deviceInfo.getDisplayName().equals(str)) {
                hdmiCecLocalDevice.setDeviceInfo(deviceInfo.toBuilder().setDisplayName(str).build());
                sendCecCommand(HdmiCecMessageBuilder.buildSetOsdNameCommand(deviceInfo.getLogicalAddress(), 0, str));
            }
        }
    }

    @HdmiAnnotations.ServiceThreadOnly
    void handleMhlHotplugEvent(int i, boolean z) {
        assertRunOnServiceThread();
        if (z) {
            HdmiMhlLocalDeviceStub hdmiMhlLocalDeviceStub = new HdmiMhlLocalDeviceStub(this, i);
            HdmiMhlLocalDeviceStub addLocalDevice = this.mMhlController.addLocalDevice(hdmiMhlLocalDeviceStub);
            if (addLocalDevice != null) {
                addLocalDevice.onDeviceRemoved();
                Slog.i(TAG, "Old device of port " + i + " is removed");
            }
            invokeDeviceEventListeners(hdmiMhlLocalDeviceStub.getInfo(), 1);
            updateSafeMhlInput();
        } else {
            HdmiMhlLocalDeviceStub removeLocalDevice = this.mMhlController.removeLocalDevice(i);
            if (removeLocalDevice != null) {
                removeLocalDevice.onDeviceRemoved();
                invokeDeviceEventListeners(removeLocalDevice.getInfo(), 2);
                updateSafeMhlInput();
            } else {
                Slog.w(TAG, "No device to remove:[portId=" + i);
            }
        }
        announceHotplugEvent(i, z);
    }

    @HdmiAnnotations.ServiceThreadOnly
    void handleMhlBusModeChanged(int i, int i2) {
        assertRunOnServiceThread();
        HdmiMhlLocalDeviceStub localDevice = this.mMhlController.getLocalDevice(i);
        if (localDevice != null) {
            localDevice.setBusMode(i2);
            return;
        }
        Slog.w(TAG, "No mhl device exists for bus mode change[portId:" + i + ", busmode:" + i2 + "]");
    }

    @HdmiAnnotations.ServiceThreadOnly
    void handleMhlBusOvercurrent(int i, boolean z) {
        assertRunOnServiceThread();
        HdmiMhlLocalDeviceStub localDevice = this.mMhlController.getLocalDevice(i);
        if (localDevice != null) {
            localDevice.onBusOvercurrentDetected(z);
            return;
        }
        Slog.w(TAG, "No mhl device exists for bus overcurrent event[portId:" + i + "]");
    }

    @HdmiAnnotations.ServiceThreadOnly
    void handleMhlDeviceStatusChanged(int i, int i2, int i3) {
        assertRunOnServiceThread();
        HdmiMhlLocalDeviceStub localDevice = this.mMhlController.getLocalDevice(i);
        if (localDevice != null) {
            localDevice.setDeviceStatusChange(i2, i3);
            return;
        }
        Slog.w(TAG, "No mhl device exists for device status event[portId:" + i + ", adopterId:" + i2 + ", deviceId:" + i3 + "]");
    }

    @HdmiAnnotations.ServiceThreadOnly
    private void updateSafeMhlInput() {
        assertRunOnServiceThread();
        List<HdmiDeviceInfo> emptyList = Collections.emptyList();
        SparseArray<HdmiMhlLocalDeviceStub> allLocalDevices = this.mMhlController.getAllLocalDevices();
        for (int i = 0; i < allLocalDevices.size(); i++) {
            HdmiMhlLocalDeviceStub valueAt = allLocalDevices.valueAt(i);
            if (valueAt.getInfo() != null) {
                if (emptyList.isEmpty()) {
                    emptyList = new ArrayList<>();
                }
                emptyList.add(valueAt.getInfo());
            }
        }
        synchronized (this.mLock) {
            this.mMhlDevices = emptyList;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public List<HdmiDeviceInfo> getMhlDevicesLocked() {
        return this.mMhlDevices;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class HdmiMhlVendorCommandListenerRecord implements IBinder.DeathRecipient {
        private final IHdmiMhlVendorCommandListener mListener;

        public HdmiMhlVendorCommandListenerRecord(IHdmiMhlVendorCommandListener iHdmiMhlVendorCommandListener) {
            this.mListener = iHdmiMhlVendorCommandListener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            HdmiControlService.this.mMhlVendorCommandListenerRecords.remove(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class HdmiControlStatusChangeListenerRecord implements IBinder.DeathRecipient {
        private final IHdmiControlStatusChangeListener mListener;

        HdmiControlStatusChangeListenerRecord(IHdmiControlStatusChangeListener iHdmiControlStatusChangeListener) {
            this.mListener = iHdmiControlStatusChangeListener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (HdmiControlService.this.mLock) {
                HdmiControlService.this.mHdmiControlStatusChangeListenerRecords.remove(this);
            }
        }

        public boolean equals(Object obj) {
            if (obj instanceof HdmiControlStatusChangeListenerRecord) {
                return obj == this || ((HdmiControlStatusChangeListenerRecord) obj).mListener == this.mListener;
            }
            return false;
        }

        public int hashCode() {
            return this.mListener.hashCode();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class HotplugEventListenerRecord implements IBinder.DeathRecipient {
        private final IHdmiHotplugEventListener mListener;

        public HotplugEventListenerRecord(IHdmiHotplugEventListener iHdmiHotplugEventListener) {
            this.mListener = iHdmiHotplugEventListener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (HdmiControlService.this.mLock) {
                HdmiControlService.this.mHotplugEventListenerRecords.remove(this);
            }
        }

        public boolean equals(Object obj) {
            if (obj instanceof HotplugEventListenerRecord) {
                return obj == this || ((HotplugEventListenerRecord) obj).mListener == this.mListener;
            }
            return false;
        }

        public int hashCode() {
            return this.mListener.hashCode();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class DeviceEventListenerRecord implements IBinder.DeathRecipient {
        private final IHdmiDeviceEventListener mListener;

        public DeviceEventListenerRecord(IHdmiDeviceEventListener iHdmiDeviceEventListener) {
            this.mListener = iHdmiDeviceEventListener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (HdmiControlService.this.mLock) {
                HdmiControlService.this.mDeviceEventListenerRecords.remove(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class SystemAudioModeChangeListenerRecord implements IBinder.DeathRecipient {
        private final IHdmiSystemAudioModeChangeListener mListener;

        public SystemAudioModeChangeListenerRecord(IHdmiSystemAudioModeChangeListener iHdmiSystemAudioModeChangeListener) {
            this.mListener = iHdmiSystemAudioModeChangeListener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (HdmiControlService.this.mLock) {
                HdmiControlService.this.mSystemAudioModeChangeListenerRecords.remove(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class VendorCommandListenerRecord implements IBinder.DeathRecipient {
        private final IHdmiVendorCommandListener mListener;
        private final int mVendorId;

        VendorCommandListenerRecord(IHdmiVendorCommandListener iHdmiVendorCommandListener, int i) {
            this.mListener = iHdmiVendorCommandListener;
            this.mVendorId = i;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (HdmiControlService.this.mLock) {
                HdmiControlService.this.mVendorCommandListenerRecords.remove(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class HdmiRecordListenerRecord implements IBinder.DeathRecipient {
        private final IHdmiRecordListener mListener;

        public HdmiRecordListenerRecord(IHdmiRecordListener iHdmiRecordListener) {
            this.mListener = iHdmiRecordListener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (HdmiControlService.this.mLock) {
                if (HdmiControlService.this.mRecordListenerRecord == this) {
                    HdmiControlService.this.mRecordListenerRecord = null;
                }
            }
        }
    }

    private void setWorkSourceUidToCallingUid() {
        Binder.setCallingWorkSourceUid(Binder.getCallingUid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceAccessPermission() {
        getContext().enforceCallingOrSelfPermission(PERMISSION, TAG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initBinderCall() {
        enforceAccessPermission();
        setWorkSourceUidToCallingUid();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class BinderService extends IHdmiControlService.Stub {
        private BinderService() {
        }

        public int[] getSupportedTypes() {
            HdmiControlService.this.initBinderCall();
            int size = HdmiControlService.this.mCecLocalDevices.size();
            int[] iArr = new int[size];
            for (int i = 0; i < size; i++) {
                iArr[i] = ((Integer) HdmiControlService.this.mCecLocalDevices.get(i)).intValue();
            }
            return iArr;
        }

        public HdmiDeviceInfo getActiveSource() {
            HdmiControlService.this.initBinderCall();
            return HdmiControlService.this.getActiveSource();
        }

        public void deviceSelect(final int i, final IHdmiControlCallback iHdmiControlCallback) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.1
                @Override // java.lang.Runnable
                public void run() {
                    if (iHdmiControlCallback == null) {
                        Slog.e(HdmiControlService.TAG, "Callback cannot be null");
                        return;
                    }
                    HdmiCecLocalDeviceTv tv = HdmiControlService.this.tv();
                    HdmiCecLocalDevicePlayback playback = HdmiControlService.this.playback();
                    if (tv == null && playback == null) {
                        if (!HdmiControlService.this.mAddressAllocated) {
                            HdmiControlService.this.mSelectRequestBuffer.set(SelectRequestBuffer.newDeviceSelect(HdmiControlService.this, i, iHdmiControlCallback));
                            return;
                        } else if (HdmiControlService.this.isTvDevice()) {
                            Slog.e(HdmiControlService.TAG, "Local tv device not available");
                            return;
                        } else {
                            HdmiControlService.this.invokeCallback(iHdmiControlCallback, 2);
                            return;
                        }
                    }
                    if (tv != null) {
                        HdmiMhlLocalDeviceStub localDeviceById = HdmiControlService.this.mMhlController.getLocalDeviceById(i);
                        if (localDeviceById != null) {
                            if (localDeviceById.getPortId() == tv.getActivePortId()) {
                                HdmiControlService.this.invokeCallback(iHdmiControlCallback, 0);
                                return;
                            } else {
                                localDeviceById.turnOn(iHdmiControlCallback);
                                tv.doManualPortSwitching(localDeviceById.getPortId(), null);
                                return;
                            }
                        }
                        tv.deviceSelect(i, iHdmiControlCallback);
                        return;
                    }
                    playback.deviceSelect(i, iHdmiControlCallback);
                }
            });
        }

        public void portSelect(final int i, final IHdmiControlCallback iHdmiControlCallback) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.2
                @Override // java.lang.Runnable
                public void run() {
                    if (iHdmiControlCallback == null) {
                        Slog.e(HdmiControlService.TAG, "Callback cannot be null");
                        return;
                    }
                    HdmiCecLocalDeviceTv tv = HdmiControlService.this.tv();
                    if (tv != null) {
                        tv.doManualPortSwitching(i, iHdmiControlCallback);
                        return;
                    }
                    HdmiCecLocalDeviceAudioSystem audioSystem = HdmiControlService.this.audioSystem();
                    if (audioSystem != null) {
                        audioSystem.doManualPortSwitching(i, iHdmiControlCallback);
                    } else if (!HdmiControlService.this.mAddressAllocated) {
                        HdmiControlService.this.mSelectRequestBuffer.set(SelectRequestBuffer.newPortSelect(HdmiControlService.this, i, iHdmiControlCallback));
                    } else {
                        Slog.w(HdmiControlService.TAG, "Local device not available");
                        HdmiControlService.this.invokeCallback(iHdmiControlCallback, 2);
                    }
                }
            });
        }

        public void sendKeyEvent(final int i, final int i2, final boolean z) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.3
                @Override // java.lang.Runnable
                public void run() {
                    HdmiMhlLocalDeviceStub localDevice = HdmiControlService.this.mMhlController.getLocalDevice(HdmiControlService.this.mActivePortId);
                    if (localDevice != null) {
                        localDevice.sendKeyEvent(i2, z);
                        return;
                    }
                    if (HdmiControlService.this.mCecController != null) {
                        HdmiCecLocalDevice localDevice2 = HdmiControlService.this.mHdmiCecNetwork.getLocalDevice(i);
                        if (localDevice2 == null) {
                            Slog.w(HdmiControlService.TAG, "Local device not available to send key event.");
                        } else {
                            localDevice2.sendKeyEvent(i2, z);
                        }
                    }
                }
            });
        }

        public void sendVolumeKeyEvent(final int i, final int i2, final boolean z) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.4
                @Override // java.lang.Runnable
                public void run() {
                    if (HdmiControlService.this.mCecController == null) {
                        Slog.w(HdmiControlService.TAG, "CEC controller not available to send volume key event.");
                        return;
                    }
                    HdmiCecLocalDevice localDevice = HdmiControlService.this.mHdmiCecNetwork.getLocalDevice(i);
                    if (localDevice == null) {
                        Slog.w(HdmiControlService.TAG, "Local device " + i + " not available to send volume key event.");
                        return;
                    }
                    localDevice.sendVolumeKeyEvent(i2, z);
                }
            });
        }

        public void oneTouchPlay(final IHdmiControlCallback iHdmiControlCallback) {
            HdmiControlService.this.initBinderCall();
            Slog.d(HdmiControlService.TAG, "Process pid: " + Binder.getCallingPid() + " is calling oneTouchPlay.");
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.5
                @Override // java.lang.Runnable
                public void run() {
                    HdmiControlService.this.oneTouchPlay(iHdmiControlCallback);
                }
            });
        }

        public void toggleAndFollowTvPower() {
            HdmiControlService.this.initBinderCall();
            Slog.d(HdmiControlService.TAG, "Process pid: " + Binder.getCallingPid() + " is calling toggleAndFollowTvPower.");
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.6
                @Override // java.lang.Runnable
                public void run() {
                    HdmiControlService.this.toggleAndFollowTvPower();
                }
            });
        }

        public boolean shouldHandleTvPowerKey() {
            HdmiControlService.this.initBinderCall();
            return HdmiControlService.this.shouldHandleTvPowerKey();
        }

        public void queryDisplayStatus(final IHdmiControlCallback iHdmiControlCallback) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.7
                @Override // java.lang.Runnable
                public void run() {
                    HdmiControlService.this.queryDisplayStatus(iHdmiControlCallback);
                }
            });
        }

        public void addHdmiControlStatusChangeListener(IHdmiControlStatusChangeListener iHdmiControlStatusChangeListener) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.addHdmiControlStatusChangeListener(iHdmiControlStatusChangeListener);
        }

        public void removeHdmiControlStatusChangeListener(IHdmiControlStatusChangeListener iHdmiControlStatusChangeListener) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.removeHdmiControlStatusChangeListener(iHdmiControlStatusChangeListener);
        }

        public void addHdmiCecVolumeControlFeatureListener(IHdmiCecVolumeControlFeatureListener iHdmiCecVolumeControlFeatureListener) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.addHdmiCecVolumeControlFeatureListener(iHdmiCecVolumeControlFeatureListener);
        }

        public void removeHdmiCecVolumeControlFeatureListener(IHdmiCecVolumeControlFeatureListener iHdmiCecVolumeControlFeatureListener) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.removeHdmiControlVolumeControlStatusChangeListener(iHdmiCecVolumeControlFeatureListener);
        }

        public void addHotplugEventListener(IHdmiHotplugEventListener iHdmiHotplugEventListener) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.addHotplugEventListener(iHdmiHotplugEventListener);
        }

        public void removeHotplugEventListener(IHdmiHotplugEventListener iHdmiHotplugEventListener) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.removeHotplugEventListener(iHdmiHotplugEventListener);
        }

        public void addDeviceEventListener(IHdmiDeviceEventListener iHdmiDeviceEventListener) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.addDeviceEventListener(iHdmiDeviceEventListener);
        }

        public List<HdmiPortInfo> getPortInfo() {
            HdmiControlService.this.initBinderCall();
            if (HdmiControlService.this.getPortInfo() == null) {
                return Collections.emptyList();
            }
            return HdmiControlService.this.getPortInfo();
        }

        public boolean canChangeSystemAudioMode() {
            HdmiControlService.this.initBinderCall();
            HdmiCecLocalDeviceTv tv = HdmiControlService.this.tv();
            if (tv == null) {
                return false;
            }
            return tv.hasSystemAudioDevice();
        }

        public boolean getSystemAudioMode() {
            HdmiControlService.this.initBinderCall();
            HdmiCecLocalDeviceTv tv = HdmiControlService.this.tv();
            HdmiCecLocalDeviceAudioSystem audioSystem = HdmiControlService.this.audioSystem();
            return (tv != null && tv.isSystemAudioActivated()) || (audioSystem != null && audioSystem.isSystemAudioActivated());
        }

        public int getPhysicalAddress() {
            int physicalAddress;
            HdmiControlService.this.initBinderCall();
            synchronized (HdmiControlService.this.mLock) {
                physicalAddress = HdmiControlService.this.mHdmiCecNetwork.getPhysicalAddress();
            }
            return physicalAddress;
        }

        public void setSystemAudioMode(final boolean z, final IHdmiControlCallback iHdmiControlCallback) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.8
                @Override // java.lang.Runnable
                public void run() {
                    HdmiCecLocalDeviceTv tv = HdmiControlService.this.tv();
                    if (tv == null) {
                        Slog.w(HdmiControlService.TAG, "Local tv device not available");
                        HdmiControlService.this.invokeCallback(iHdmiControlCallback, 2);
                    } else {
                        tv.changeSystemAudioMode(z, iHdmiControlCallback);
                    }
                }
            });
        }

        public void addSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener iHdmiSystemAudioModeChangeListener) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.addSystemAudioModeChangeListner(iHdmiSystemAudioModeChangeListener);
        }

        public void removeSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener iHdmiSystemAudioModeChangeListener) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.removeSystemAudioModeChangeListener(iHdmiSystemAudioModeChangeListener);
        }

        public void setInputChangeListener(IHdmiInputChangeListener iHdmiInputChangeListener) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.setInputChangeListener(iHdmiInputChangeListener);
        }

        public List<HdmiDeviceInfo> getInputDevices() {
            HdmiControlService.this.initBinderCall();
            return HdmiUtils.mergeToUnmodifiableList(HdmiControlService.this.mHdmiCecNetwork.getSafeExternalInputsLocked(), HdmiControlService.this.getMhlDevicesLocked());
        }

        public List<HdmiDeviceInfo> getDeviceList() {
            HdmiControlService.this.initBinderCall();
            return HdmiControlService.this.mHdmiCecNetwork.getSafeCecDevicesLocked();
        }

        public void powerOffRemoteDevice(final int i, final int i2) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.9
                @Override // java.lang.Runnable
                public void run() {
                    Slog.w(HdmiControlService.TAG, "Device " + i + " power status is " + i2 + " before standby command sent out");
                    HdmiControlService hdmiControlService = HdmiControlService.this;
                    hdmiControlService.sendCecCommand(HdmiCecMessageBuilder.buildStandby(hdmiControlService.getRemoteControlSourceAddress(), i));
                }
            });
        }

        public void powerOnRemoteDevice(final int i, final int i2) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.10
                @Override // java.lang.Runnable
                public void run() {
                    Slog.i(HdmiControlService.TAG, "Device " + i + " power status is " + i2 + " before power on command sent out");
                    if (HdmiControlService.this.getSwitchDevice() != null) {
                        HdmiControlService.this.getSwitchDevice().sendUserControlPressedAndReleased(i, HdmiCecKeycode.CEC_KEYCODE_POWER_ON_FUNCTION);
                    } else {
                        Slog.e(HdmiControlService.TAG, "Can't get the correct local device to handle routing.");
                    }
                }
            });
        }

        public void askRemoteDeviceToBecomeActiveSource(final int i) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.11
                @Override // java.lang.Runnable
                public void run() {
                    HdmiCecMessage buildSetStreamPath = HdmiCecMessageBuilder.buildSetStreamPath(HdmiControlService.this.getRemoteControlSourceAddress(), i);
                    if (HdmiControlService.this.pathToPortId(i) != -1) {
                        if (HdmiControlService.this.getSwitchDevice() != null) {
                            HdmiControlService.this.getSwitchDevice().handleSetStreamPath(buildSetStreamPath);
                        } else {
                            Slog.e(HdmiControlService.TAG, "Can't get the correct local device to handle routing.");
                        }
                    }
                    HdmiControlService.this.sendCecCommand(buildSetStreamPath);
                }
            });
        }

        public void setSystemAudioVolume(final int i, final int i2, final int i3) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.12
                @Override // java.lang.Runnable
                public void run() {
                    HdmiCecLocalDeviceTv tv = HdmiControlService.this.tv();
                    if (tv == null) {
                        Slog.w(HdmiControlService.TAG, "Local tv device not available");
                    } else {
                        int i4 = i;
                        tv.changeVolume(i4, i2 - i4, i3);
                    }
                }
            });
        }

        public void setSystemAudioMute(final boolean z) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.13
                @Override // java.lang.Runnable
                public void run() {
                    HdmiCecLocalDeviceTv tv = HdmiControlService.this.tv();
                    if (tv == null) {
                        Slog.w(HdmiControlService.TAG, "Local tv device not available");
                    } else {
                        tv.changeMute(z);
                    }
                }
            });
        }

        public void setArcMode(final boolean z) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.14
                @Override // java.lang.Runnable
                public void run() {
                    HdmiCecLocalDeviceTv tv = HdmiControlService.this.tv();
                    if (tv == null) {
                        Slog.w(HdmiControlService.TAG, "Local tv device not available to change arc mode.");
                    } else {
                        tv.startArcAction(z);
                    }
                }
            });
        }

        public void setProhibitMode(boolean z) {
            HdmiControlService.this.initBinderCall();
            if (HdmiControlService.this.isTvDevice()) {
                HdmiControlService.this.setProhibitMode(z);
            }
        }

        public void addVendorCommandListener(IHdmiVendorCommandListener iHdmiVendorCommandListener, int i) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.addVendorCommandListener(iHdmiVendorCommandListener, i);
        }

        public void sendVendorCommand(final int i, final int i2, final byte[] bArr, final boolean z) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.15
                @Override // java.lang.Runnable
                public void run() {
                    HdmiCecLocalDevice localDevice = HdmiControlService.this.mHdmiCecNetwork.getLocalDevice(i);
                    if (localDevice == null) {
                        Slog.w(HdmiControlService.TAG, "Local device not available");
                    } else if (z) {
                        HdmiControlService.this.sendCecCommand(HdmiCecMessageBuilder.buildVendorCommandWithId(localDevice.getDeviceInfo().getLogicalAddress(), i2, HdmiControlService.this.getVendorId(), bArr));
                    } else {
                        HdmiControlService.this.sendCecCommand(HdmiCecMessageBuilder.buildVendorCommand(localDevice.getDeviceInfo().getLogicalAddress(), i2, bArr));
                    }
                }
            });
        }

        public void sendStandby(final int i, final int i2) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.16
                @Override // java.lang.Runnable
                public void run() {
                    HdmiMhlLocalDeviceStub localDeviceById = HdmiControlService.this.mMhlController.getLocalDeviceById(i2);
                    if (localDeviceById != null) {
                        localDeviceById.sendStandby();
                        return;
                    }
                    HdmiCecLocalDevice localDevice = HdmiControlService.this.mHdmiCecNetwork.getLocalDevice(i);
                    if (localDevice == null) {
                        localDevice = HdmiControlService.this.audioSystem();
                    }
                    if (localDevice == null) {
                        Slog.w(HdmiControlService.TAG, "Local device not available");
                    } else {
                        localDevice.sendStandby(i2);
                    }
                }
            });
        }

        public void setHdmiRecordListener(IHdmiRecordListener iHdmiRecordListener) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.setHdmiRecordListener(iHdmiRecordListener);
        }

        public void startOneTouchRecord(final int i, final byte[] bArr) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.17
                @Override // java.lang.Runnable
                public void run() {
                    if (!HdmiControlService.this.isTvDeviceEnabled()) {
                        Slog.w(HdmiControlService.TAG, "TV device is not enabled.");
                    } else {
                        HdmiControlService.this.tv().startOneTouchRecord(i, bArr);
                    }
                }
            });
        }

        public void stopOneTouchRecord(final int i) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.18
                @Override // java.lang.Runnable
                public void run() {
                    if (!HdmiControlService.this.isTvDeviceEnabled()) {
                        Slog.w(HdmiControlService.TAG, "TV device is not enabled.");
                    } else {
                        HdmiControlService.this.tv().stopOneTouchRecord(i);
                    }
                }
            });
        }

        public void startTimerRecording(final int i, final int i2, final byte[] bArr) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.19
                @Override // java.lang.Runnable
                public void run() {
                    if (!HdmiControlService.this.isTvDeviceEnabled()) {
                        Slog.w(HdmiControlService.TAG, "TV device is not enabled.");
                    } else {
                        HdmiControlService.this.tv().startTimerRecording(i, i2, bArr);
                    }
                }
            });
        }

        public void clearTimerRecording(final int i, final int i2, final byte[] bArr) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.20
                @Override // java.lang.Runnable
                public void run() {
                    if (!HdmiControlService.this.isTvDeviceEnabled()) {
                        Slog.w(HdmiControlService.TAG, "TV device is not enabled.");
                    } else {
                        HdmiControlService.this.tv().clearTimerRecording(i, i2, bArr);
                    }
                }
            });
        }

        public void sendMhlVendorCommand(final int i, final int i2, final int i3, final byte[] bArr) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.21
                @Override // java.lang.Runnable
                public void run() {
                    if (!HdmiControlService.this.isCecControlEnabled()) {
                        Slog.w(HdmiControlService.TAG, "Hdmi control is disabled.");
                        return;
                    }
                    if (HdmiControlService.this.mMhlController.getLocalDevice(i) == null) {
                        Slog.w(HdmiControlService.TAG, "Invalid port id:" + i);
                        return;
                    }
                    HdmiControlService.this.mMhlController.sendVendorCommand(i, i2, i3, bArr);
                }
            });
        }

        public void addHdmiMhlVendorCommandListener(IHdmiMhlVendorCommandListener iHdmiMhlVendorCommandListener) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.addHdmiMhlVendorCommandListener(iHdmiMhlVendorCommandListener);
        }

        public void setStandbyMode(final boolean z) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.22
                @Override // java.lang.Runnable
                public void run() {
                    HdmiControlService.this.setStandbyMode(z);
                }
            });
        }

        public void reportAudioStatus(final int i, int i2, int i3, boolean z) {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.23
                @Override // java.lang.Runnable
                public void run() {
                    if (HdmiControlService.this.mHdmiCecNetwork.getLocalDevice(i) == null) {
                        Slog.w(HdmiControlService.TAG, "Local device not available");
                        return;
                    }
                    if (HdmiControlService.this.audioSystem() == null) {
                        Slog.w(HdmiControlService.TAG, "audio system is not available");
                    } else if (!HdmiControlService.this.audioSystem().isSystemAudioActivated()) {
                        Slog.w(HdmiControlService.TAG, "audio system is not in system audio mode");
                    } else {
                        HdmiControlService.this.audioSystem().reportAudioStatus(0);
                    }
                }
            });
        }

        public void setSystemAudioModeOnForAudioOnlySource() {
            HdmiControlService.this.initBinderCall();
            HdmiControlService.this.runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.BinderService.24
                @Override // java.lang.Runnable
                public void run() {
                    if (!HdmiControlService.this.isAudioSystemDevice()) {
                        Slog.e(HdmiControlService.TAG, "Not an audio system device. Won't set system audio mode on");
                        return;
                    }
                    if (HdmiControlService.this.audioSystem() == null) {
                        Slog.e(HdmiControlService.TAG, "Audio System local device is not registered");
                    } else if (!HdmiControlService.this.audioSystem().checkSupportAndSetSystemAudioMode(true)) {
                        Slog.e(HdmiControlService.TAG, "System Audio Mode is not supported.");
                    } else {
                        HdmiControlService hdmiControlService = HdmiControlService.this;
                        hdmiControlService.sendCecCommand(HdmiCecMessageBuilder.buildSetSystemAudioMode(hdmiControlService.audioSystem().getDeviceInfo().getLogicalAddress(), 15, true));
                    }
                }
            });
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) throws RemoteException {
            HdmiControlService.this.initBinderCall();
            new HdmiControlShellCommand(this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }

        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(HdmiControlService.this.getContext(), HdmiControlService.TAG, printWriter)) {
                IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
                synchronized (HdmiControlService.this.mLock) {
                    indentingPrintWriter.println("mProhibitMode: " + HdmiControlService.this.mProhibitMode);
                }
                indentingPrintWriter.println("mPowerStatus: " + HdmiControlService.this.mPowerStatusController.getPowerStatus());
                indentingPrintWriter.println("mIsCecAvailable: " + HdmiControlService.this.mIsCecAvailable);
                indentingPrintWriter.println("mCecVersion: " + HdmiControlService.this.mCecVersion);
                indentingPrintWriter.println("mIsAbsoluteVolumeBehaviorEnabled: " + HdmiControlService.this.isAbsoluteVolumeBehaviorEnabled());
                indentingPrintWriter.println("System_settings:");
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.println("mMhlInputChangeEnabled: " + HdmiControlService.this.isMhlInputChangeEnabled());
                indentingPrintWriter.println("mSystemAudioActivated: " + HdmiControlService.this.isSystemAudioActivated());
                indentingPrintWriter.println("mHdmiCecVolumeControlEnabled: " + HdmiControlService.this.getHdmiCecVolumeControl());
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("CEC settings:");
                indentingPrintWriter.increaseIndent();
                HdmiCecConfig hdmiCecConfig = HdmiControlService.this.getHdmiCecConfig();
                List<String> allSettings = hdmiCecConfig.getAllSettings();
                HashSet hashSet = new HashSet(hdmiCecConfig.getUserSettings());
                for (String str : allSettings) {
                    if (hdmiCecConfig.isStringValueType(str)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(" (string): ");
                        sb.append(hdmiCecConfig.getStringValue(str));
                        sb.append(" (default: ");
                        sb.append(hdmiCecConfig.getDefaultStringValue(str));
                        sb.append(")");
                        sb.append(hashSet.contains(str) ? " [modifiable]" : "");
                        indentingPrintWriter.println(sb.toString());
                    } else if (hdmiCecConfig.isIntValueType(str)) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str);
                        sb2.append(" (int): ");
                        sb2.append(hdmiCecConfig.getIntValue(str));
                        sb2.append(" (default: ");
                        sb2.append(hdmiCecConfig.getDefaultIntValue(str));
                        sb2.append(")");
                        sb2.append(hashSet.contains(str) ? " [modifiable]" : "");
                        indentingPrintWriter.println(sb2.toString());
                    }
                }
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("mMhlController: ");
                indentingPrintWriter.increaseIndent();
                HdmiControlService.this.mMhlController.dump(indentingPrintWriter);
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.print("eARC local device: ");
                indentingPrintWriter.increaseIndent();
                if (HdmiControlService.this.mEarcLocalDevice == null) {
                    indentingPrintWriter.println("None. eARC is either disabled or not available.");
                } else {
                    HdmiControlService.this.mEarcLocalDevice.dump(indentingPrintWriter);
                }
                indentingPrintWriter.decreaseIndent();
                HdmiControlService.this.mHdmiCecNetwork.dump(indentingPrintWriter);
                if (HdmiControlService.this.mCecController != null) {
                    indentingPrintWriter.println("mCecController: ");
                    indentingPrintWriter.increaseIndent();
                    HdmiControlService.this.mCecController.dump(indentingPrintWriter);
                    indentingPrintWriter.decreaseIndent();
                }
            }
        }

        public boolean setMessageHistorySize(int i) {
            HdmiControlService.this.enforceAccessPermission();
            if (HdmiControlService.this.mCecController == null) {
                return false;
            }
            return HdmiControlService.this.mCecController.setMessageHistorySize(i);
        }

        public int getMessageHistorySize() {
            HdmiControlService.this.enforceAccessPermission();
            if (HdmiControlService.this.mCecController != null) {
                return HdmiControlService.this.mCecController.getMessageHistorySize();
            }
            return 0;
        }

        public void addCecSettingChangeListener(String str, IHdmiCecSettingChangeListener iHdmiCecSettingChangeListener) {
            HdmiControlService.this.enforceAccessPermission();
            HdmiControlService.this.addCecSettingChangeListener(str, iHdmiCecSettingChangeListener);
        }

        public void removeCecSettingChangeListener(String str, IHdmiCecSettingChangeListener iHdmiCecSettingChangeListener) {
            HdmiControlService.this.enforceAccessPermission();
            HdmiControlService.this.removeCecSettingChangeListener(str, iHdmiCecSettingChangeListener);
        }

        public List<String> getUserCecSettings() {
            HdmiControlService.this.initBinderCall();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return HdmiControlService.this.getHdmiCecConfig().getUserSettings();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public List<String> getAllowedCecSettingStringValues(String str) {
            HdmiControlService.this.initBinderCall();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return HdmiControlService.this.getHdmiCecConfig().getAllowedStringValues(str);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int[] getAllowedCecSettingIntValues(String str) {
            HdmiControlService.this.initBinderCall();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return HdmiControlService.this.getHdmiCecConfig().getAllowedIntValues(str).stream().mapToInt(new ToIntFunction() { // from class: com.android.server.hdmi.HdmiControlService$BinderService$$ExternalSyntheticLambda0
                    @Override // java.util.function.ToIntFunction
                    public final int applyAsInt(Object obj) {
                        int intValue;
                        intValue = ((Integer) obj).intValue();
                        return intValue;
                    }
                }).toArray();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public String getCecSettingStringValue(String str) {
            HdmiControlService.this.initBinderCall();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return HdmiControlService.this.getHdmiCecConfig().getStringValue(str);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setCecSettingStringValue(String str, String str2) {
            HdmiControlService.this.initBinderCall();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                HdmiControlService.this.getHdmiCecConfig().setStringValue(str, str2);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int getCecSettingIntValue(String str) {
            HdmiControlService.this.initBinderCall();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return HdmiControlService.this.getHdmiCecConfig().getIntValue(str);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setCecSettingIntValue(String str, int i) {
            HdmiControlService.this.initBinderCall();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                HdmiControlService.this.getHdmiCecConfig().setIntValue(str, i);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    @VisibleForTesting
    void setHdmiCecVolumeControlEnabledInternal(int i) {
        this.mHdmiCecVolumeControl = i;
        announceHdmiCecVolumeControlFeatureChange(i);
        runOnServiceThread(new HdmiControlService$$ExternalSyntheticLambda2(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRemoteControlSourceAddress() {
        if (isAudioSystemDevice()) {
            return audioSystem().getDeviceInfo().getLogicalAddress();
        }
        if (isPlaybackDevice()) {
            return playback().getDeviceInfo().getLogicalAddress();
        }
        return 15;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HdmiCecLocalDeviceSource getSwitchDevice() {
        if (isAudioSystemDevice()) {
            return audioSystem();
        }
        if (isPlaybackDevice()) {
            return playback();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    public void oneTouchPlay(IHdmiControlCallback iHdmiControlCallback) {
        assertRunOnServiceThread();
        if (!this.mAddressAllocated) {
            this.mOtpCallbackPendingAddressAllocation = iHdmiControlCallback;
            Slog.d(TAG, "Local device is under address allocation. Save OTP callback for later process.");
            return;
        }
        HdmiCecLocalDeviceSource playback = playback();
        if (playback == null) {
            playback = audioSystem();
        }
        if (playback == null) {
            Slog.w(TAG, "Local source device not available");
            invokeCallback(iHdmiControlCallback, 2);
        } else {
            playback.oneTouchPlay(iHdmiControlCallback);
        }
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    protected void toggleAndFollowTvPower() {
        assertRunOnServiceThread();
        HdmiCecLocalDeviceSource playback = playback();
        if (playback == null) {
            playback = audioSystem();
        }
        if (playback == null) {
            Slog.w(TAG, "Local source device not available");
        } else {
            playback.toggleAndFollowTvPower();
        }
    }

    @VisibleForTesting
    protected boolean shouldHandleTvPowerKey() {
        if (isTvDevice() || getHdmiCecConfig().getStringValue("power_control_mode").equals("none") || getHdmiCecConfig().getIntValue("hdmi_cec_enabled") != 1) {
            return false;
        }
        return this.mIsCecAvailable;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HdmiAnnotations.ServiceThreadOnly
    public void queryDisplayStatus(IHdmiControlCallback iHdmiControlCallback) {
        assertRunOnServiceThread();
        if (!this.mAddressAllocated) {
            this.mDisplayStatusCallback = iHdmiControlCallback;
            Slog.d(TAG, "Local device is under address allocation. Queue display callback for later process.");
            return;
        }
        HdmiCecLocalDeviceSource playback = playback();
        if (playback == null) {
            playback = audioSystem();
        }
        if (playback == null) {
            Slog.w(TAG, "Local source device not available");
            invokeCallback(iHdmiControlCallback, -1);
        } else {
            playback.queryDisplayStatus(iHdmiControlCallback);
        }
    }

    protected HdmiDeviceInfo getActiveSource() {
        int activePath;
        if (playback() != null && playback().isActiveSource()) {
            return playback().getDeviceInfo();
        }
        HdmiCecLocalDevice.ActiveSource localActiveSource = getLocalActiveSource();
        if (localActiveSource.isValid()) {
            HdmiDeviceInfo safeCecDeviceInfo = this.mHdmiCecNetwork.getSafeCecDeviceInfo(localActiveSource.logicalAddress);
            if (safeCecDeviceInfo != null) {
                return safeCecDeviceInfo;
            }
            int i = localActiveSource.physicalAddress;
            return HdmiDeviceInfo.hardwarePort(i, pathToPortId(i));
        }
        if (tv() == null || (activePath = tv().getActivePath()) == 65535) {
            return null;
        }
        HdmiDeviceInfo safeDeviceInfoByPath = this.mHdmiCecNetwork.getSafeDeviceInfoByPath(activePath);
        return safeDeviceInfoByPath != null ? safeDeviceInfoByPath : HdmiDeviceInfo.hardwarePort(activePath, tv().getActivePortId());
    }

    @VisibleForTesting
    void addHdmiControlStatusChangeListener(final IHdmiControlStatusChangeListener iHdmiControlStatusChangeListener) {
        final HdmiControlStatusChangeListenerRecord hdmiControlStatusChangeListenerRecord = new HdmiControlStatusChangeListenerRecord(iHdmiControlStatusChangeListener);
        try {
            iHdmiControlStatusChangeListener.asBinder().linkToDeath(hdmiControlStatusChangeListenerRecord, 0);
            synchronized (this.mLock) {
                this.mHdmiControlStatusChangeListenerRecords.add(hdmiControlStatusChangeListenerRecord);
            }
            runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.16
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (HdmiControlService.this.mLock) {
                        if (HdmiControlService.this.mHdmiControlStatusChangeListenerRecords.contains(hdmiControlStatusChangeListenerRecord)) {
                            synchronized (HdmiControlService.this.mLock) {
                                HdmiControlService hdmiControlService = HdmiControlService.this;
                                hdmiControlService.invokeHdmiControlStatusChangeListenerLocked(iHdmiControlStatusChangeListener, hdmiControlService.mHdmiControlEnabled);
                            }
                        }
                    }
                }
            });
        } catch (RemoteException unused) {
            Slog.w(TAG, "Listener already died");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeHdmiControlStatusChangeListener(IHdmiControlStatusChangeListener iHdmiControlStatusChangeListener) {
        synchronized (this.mLock) {
            Iterator<HdmiControlStatusChangeListenerRecord> it = this.mHdmiControlStatusChangeListenerRecords.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                HdmiControlStatusChangeListenerRecord next = it.next();
                if (next.mListener.asBinder() == iHdmiControlStatusChangeListener.asBinder()) {
                    iHdmiControlStatusChangeListener.asBinder().unlinkToDeath(next, 0);
                    this.mHdmiControlStatusChangeListenerRecords.remove(next);
                    break;
                }
            }
        }
    }

    @VisibleForTesting
    void addHdmiCecVolumeControlFeatureListener(final IHdmiCecVolumeControlFeatureListener iHdmiCecVolumeControlFeatureListener) {
        this.mHdmiCecVolumeControlFeatureListenerRecords.register(iHdmiCecVolumeControlFeatureListener);
        runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.17
            @Override // java.lang.Runnable
            public void run() {
                synchronized (HdmiControlService.this.mLock) {
                    try {
                        iHdmiCecVolumeControlFeatureListener.onHdmiCecVolumeControlFeature(HdmiControlService.this.mHdmiCecVolumeControl);
                    } catch (RemoteException e) {
                        Slog.e(HdmiControlService.TAG, "Failed to report HdmiControlVolumeControlStatusChange: " + HdmiControlService.this.mHdmiCecVolumeControl, e);
                    }
                }
            }
        });
    }

    @VisibleForTesting
    void removeHdmiControlVolumeControlStatusChangeListener(IHdmiCecVolumeControlFeatureListener iHdmiCecVolumeControlFeatureListener) {
        this.mHdmiCecVolumeControlFeatureListenerRecords.unregister(iHdmiCecVolumeControlFeatureListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addHotplugEventListener(final IHdmiHotplugEventListener iHdmiHotplugEventListener) {
        final HotplugEventListenerRecord hotplugEventListenerRecord = new HotplugEventListenerRecord(iHdmiHotplugEventListener);
        try {
            iHdmiHotplugEventListener.asBinder().linkToDeath(hotplugEventListenerRecord, 0);
            synchronized (this.mLock) {
                this.mHotplugEventListenerRecords.add(hotplugEventListenerRecord);
            }
            runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.18
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (HdmiControlService.this.mLock) {
                        if (HdmiControlService.this.mHotplugEventListenerRecords.contains(hotplugEventListenerRecord)) {
                            for (HdmiPortInfo hdmiPortInfo : HdmiControlService.this.getPortInfo()) {
                                HdmiHotplugEvent hdmiHotplugEvent = new HdmiHotplugEvent(hdmiPortInfo.getId(), HdmiControlService.this.mCecController.isConnected(hdmiPortInfo.getId()));
                                synchronized (HdmiControlService.this.mLock) {
                                    HdmiControlService.this.invokeHotplugEventListenerLocked(iHdmiHotplugEventListener, hdmiHotplugEvent);
                                }
                            }
                        }
                    }
                }
            });
        } catch (RemoteException unused) {
            Slog.w(TAG, "Listener already died");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeHotplugEventListener(IHdmiHotplugEventListener iHdmiHotplugEventListener) {
        synchronized (this.mLock) {
            Iterator<HotplugEventListenerRecord> it = this.mHotplugEventListenerRecords.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                HotplugEventListenerRecord next = it.next();
                if (next.mListener.asBinder() == iHdmiHotplugEventListener.asBinder()) {
                    iHdmiHotplugEventListener.asBinder().unlinkToDeath(next, 0);
                    this.mHotplugEventListenerRecords.remove(next);
                    break;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addDeviceEventListener(IHdmiDeviceEventListener iHdmiDeviceEventListener) {
        DeviceEventListenerRecord deviceEventListenerRecord = new DeviceEventListenerRecord(iHdmiDeviceEventListener);
        try {
            iHdmiDeviceEventListener.asBinder().linkToDeath(deviceEventListenerRecord, 0);
            synchronized (this.mLock) {
                this.mDeviceEventListenerRecords.add(deviceEventListenerRecord);
            }
        } catch (RemoteException unused) {
            Slog.w(TAG, "Listener already died");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invokeDeviceEventListeners(HdmiDeviceInfo hdmiDeviceInfo, int i) {
        synchronized (this.mLock) {
            Iterator<DeviceEventListenerRecord> it = this.mDeviceEventListenerRecords.iterator();
            while (it.hasNext()) {
                try {
                    it.next().mListener.onStatusChanged(hdmiDeviceInfo, i);
                } catch (RemoteException e) {
                    Slog.e(TAG, "Failed to report device event:" + e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addSystemAudioModeChangeListner(IHdmiSystemAudioModeChangeListener iHdmiSystemAudioModeChangeListener) {
        SystemAudioModeChangeListenerRecord systemAudioModeChangeListenerRecord = new SystemAudioModeChangeListenerRecord(iHdmiSystemAudioModeChangeListener);
        try {
            iHdmiSystemAudioModeChangeListener.asBinder().linkToDeath(systemAudioModeChangeListenerRecord, 0);
            synchronized (this.mLock) {
                this.mSystemAudioModeChangeListenerRecords.add(systemAudioModeChangeListenerRecord);
            }
        } catch (RemoteException unused) {
            Slog.w(TAG, "Listener already died");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener iHdmiSystemAudioModeChangeListener) {
        synchronized (this.mLock) {
            Iterator<SystemAudioModeChangeListenerRecord> it = this.mSystemAudioModeChangeListenerRecords.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SystemAudioModeChangeListenerRecord next = it.next();
                if (next.mListener.asBinder() == iHdmiSystemAudioModeChangeListener) {
                    iHdmiSystemAudioModeChangeListener.asBinder().unlinkToDeath(next, 0);
                    this.mSystemAudioModeChangeListenerRecords.remove(next);
                    break;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class InputChangeListenerRecord implements IBinder.DeathRecipient {
        private final IHdmiInputChangeListener mListener;

        public InputChangeListenerRecord(IHdmiInputChangeListener iHdmiInputChangeListener) {
            this.mListener = iHdmiInputChangeListener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (HdmiControlService.this.mLock) {
                if (HdmiControlService.this.mInputChangeListenerRecord == this) {
                    HdmiControlService.this.mInputChangeListenerRecord = null;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setInputChangeListener(IHdmiInputChangeListener iHdmiInputChangeListener) {
        synchronized (this.mLock) {
            this.mInputChangeListenerRecord = new InputChangeListenerRecord(iHdmiInputChangeListener);
            try {
                iHdmiInputChangeListener.asBinder().linkToDeath(this.mInputChangeListenerRecord, 0);
            } catch (RemoteException unused) {
                Slog.w(TAG, "Listener already died");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invokeInputChangeListener(HdmiDeviceInfo hdmiDeviceInfo) {
        synchronized (this.mLock) {
            InputChangeListenerRecord inputChangeListenerRecord = this.mInputChangeListenerRecord;
            if (inputChangeListenerRecord != null) {
                try {
                    inputChangeListenerRecord.mListener.onChanged(hdmiDeviceInfo);
                } catch (RemoteException e) {
                    Slog.w(TAG, "Exception thrown by IHdmiInputChangeListener: " + e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHdmiRecordListener(IHdmiRecordListener iHdmiRecordListener) {
        synchronized (this.mLock) {
            this.mRecordListenerRecord = new HdmiRecordListenerRecord(iHdmiRecordListener);
            try {
                iHdmiRecordListener.asBinder().linkToDeath(this.mRecordListenerRecord, 0);
            } catch (RemoteException e) {
                Slog.w(TAG, "Listener already died.", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] invokeRecordRequestListener(int i) {
        synchronized (this.mLock) {
            HdmiRecordListenerRecord hdmiRecordListenerRecord = this.mRecordListenerRecord;
            if (hdmiRecordListenerRecord != null) {
                try {
                    return hdmiRecordListenerRecord.mListener.getOneTouchRecordSource(i);
                } catch (RemoteException e) {
                    Slog.w(TAG, "Failed to start record.", e);
                }
            }
            return EmptyArray.BYTE;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invokeOneTouchRecordResult(int i, int i2) {
        synchronized (this.mLock) {
            HdmiRecordListenerRecord hdmiRecordListenerRecord = this.mRecordListenerRecord;
            if (hdmiRecordListenerRecord != null) {
                try {
                    hdmiRecordListenerRecord.mListener.onOneTouchRecordResult(i, i2);
                } catch (RemoteException e) {
                    Slog.w(TAG, "Failed to call onOneTouchRecordResult.", e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invokeTimerRecordingResult(int i, int i2) {
        synchronized (this.mLock) {
            HdmiRecordListenerRecord hdmiRecordListenerRecord = this.mRecordListenerRecord;
            if (hdmiRecordListenerRecord != null) {
                try {
                    hdmiRecordListenerRecord.mListener.onTimerRecordingResult(i, i2);
                } catch (RemoteException e) {
                    Slog.w(TAG, "Failed to call onTimerRecordingResult.", e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invokeClearTimerRecordingResult(int i, int i2) {
        synchronized (this.mLock) {
            HdmiRecordListenerRecord hdmiRecordListenerRecord = this.mRecordListenerRecord;
            if (hdmiRecordListenerRecord != null) {
                try {
                    hdmiRecordListenerRecord.mListener.onClearTimerRecordingResult(i, i2);
                } catch (RemoteException e) {
                    Slog.w(TAG, "Failed to call onClearTimerRecordingResult.", e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeCallback(IHdmiControlCallback iHdmiControlCallback, int i) {
        if (iHdmiControlCallback == null) {
            return;
        }
        try {
            iHdmiControlCallback.onComplete(i);
        } catch (RemoteException e) {
            Slog.e(TAG, "Invoking callback failed:" + e);
        }
    }

    private void invokeSystemAudioModeChangeLocked(IHdmiSystemAudioModeChangeListener iHdmiSystemAudioModeChangeListener, boolean z) {
        try {
            iHdmiSystemAudioModeChangeListener.onStatusChanged(z);
        } catch (RemoteException e) {
            Slog.e(TAG, "Invoking callback failed:" + e);
        }
    }

    private void announceHotplugEvent(int i, boolean z) {
        HdmiHotplugEvent hdmiHotplugEvent = new HdmiHotplugEvent(i, z);
        synchronized (this.mLock) {
            Iterator<HotplugEventListenerRecord> it = this.mHotplugEventListenerRecords.iterator();
            while (it.hasNext()) {
                invokeHotplugEventListenerLocked(it.next().mListener, hdmiHotplugEvent);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeHotplugEventListenerLocked(IHdmiHotplugEventListener iHdmiHotplugEventListener, HdmiHotplugEvent hdmiHotplugEvent) {
        try {
            iHdmiHotplugEventListener.onReceived(hdmiHotplugEvent);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to report hotplug event:" + hdmiHotplugEvent.toString(), e);
        }
    }

    private void announceHdmiControlStatusChange(int i) {
        assertRunOnServiceThread();
        synchronized (this.mLock) {
            ArrayList arrayList = new ArrayList(this.mHdmiControlStatusChangeListenerRecords.size());
            Iterator<HdmiControlStatusChangeListenerRecord> it = this.mHdmiControlStatusChangeListenerRecords.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().mListener);
            }
            invokeHdmiControlStatusChangeListenerLocked(arrayList, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeHdmiControlStatusChangeListenerLocked(IHdmiControlStatusChangeListener iHdmiControlStatusChangeListener, int i) {
        invokeHdmiControlStatusChangeListenerLocked(Collections.singletonList(iHdmiControlStatusChangeListener), i);
    }

    private void invokeHdmiControlStatusChangeListenerLocked(final Collection<IHdmiControlStatusChangeListener> collection, final int i) {
        if (i == 1) {
            queryDisplayStatus(new IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiControlService.19
                public void onComplete(int i2) {
                    HdmiControlService.this.mIsCecAvailable = i2 != -1;
                    if (collection.isEmpty()) {
                        return;
                    }
                    HdmiControlService hdmiControlService = HdmiControlService.this;
                    hdmiControlService.invokeHdmiControlStatusChangeListenerLocked(collection, i, hdmiControlService.mIsCecAvailable);
                }
            });
            return;
        }
        this.mIsCecAvailable = false;
        if (collection.isEmpty()) {
            return;
        }
        invokeHdmiControlStatusChangeListenerLocked(collection, i, this.mIsCecAvailable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeHdmiControlStatusChangeListenerLocked(Collection<IHdmiControlStatusChangeListener> collection, int i, boolean z) {
        Iterator<IHdmiControlStatusChangeListener> it = collection.iterator();
        while (it.hasNext()) {
            try {
                it.next().onStatusChange(i, z);
            } catch (RemoteException e) {
                Slog.e(TAG, "Failed to report HdmiControlStatusChange: " + i + " isAvailable: " + z, e);
            }
        }
    }

    private void announceHdmiCecVolumeControlFeatureChange(final int i) {
        assertRunOnServiceThread();
        synchronized (this.mLock) {
            this.mHdmiCecVolumeControlFeatureListenerRecords.broadcast(new Consumer() { // from class: com.android.server.hdmi.HdmiControlService$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    HdmiControlService.lambda$announceHdmiCecVolumeControlFeatureChange$2(i, (IHdmiCecVolumeControlFeatureListener) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$announceHdmiCecVolumeControlFeatureChange$2(int i, IHdmiCecVolumeControlFeatureListener iHdmiCecVolumeControlFeatureListener) {
        try {
            iHdmiCecVolumeControlFeatureListener.onHdmiCecVolumeControlFeature(i);
        } catch (RemoteException unused) {
            Slog.e(TAG, "Failed to report HdmiControlVolumeControlStatusChange: " + i);
        }
    }

    public HdmiCecLocalDeviceTv tv() {
        return (HdmiCecLocalDeviceTv) this.mHdmiCecNetwork.getLocalDevice(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTvDevice() {
        return this.mCecLocalDevices.contains(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAudioSystemDevice() {
        return this.mCecLocalDevices.contains(5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPlaybackDevice() {
        return this.mCecLocalDevices.contains(4);
    }

    boolean isSwitchDevice() {
        return ((Boolean) HdmiProperties.is_switch().orElse(Boolean.FALSE)).booleanValue();
    }

    boolean isTvDeviceEnabled() {
        return isTvDevice() && tv() != null;
    }

    protected HdmiCecLocalDevicePlayback playback() {
        return (HdmiCecLocalDevicePlayback) this.mHdmiCecNetwork.getLocalDevice(4);
    }

    public HdmiCecLocalDeviceAudioSystem audioSystem() {
        return (HdmiCecLocalDeviceAudioSystem) this.mHdmiCecNetwork.getLocalDevice(5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioManagerWrapper getAudioManager() {
        return this.mAudioManager;
    }

    private AudioDeviceVolumeManagerWrapper getAudioDeviceVolumeManager() {
        return this.mAudioDeviceVolumeManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCecControlEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = true;
            if (this.mHdmiControlEnabled != 1) {
                z = false;
            }
        }
        return z;
    }

    private boolean isEarcEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mEarcEnabled;
        }
        return z;
    }

    private boolean isEarcSupported() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mEarcSupported;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public int getPowerStatus() {
        assertRunOnServiceThread();
        return this.mPowerStatusController.getPowerStatus();
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    void setPowerStatus(int i) {
        assertRunOnServiceThread();
        this.mPowerStatusController.setPowerStatus(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public boolean isPowerOnOrTransient() {
        assertRunOnServiceThread();
        return this.mPowerStatusController.isPowerStatusOn() || this.mPowerStatusController.isPowerStatusTransientToOn();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public boolean isPowerStandbyOrTransient() {
        assertRunOnServiceThread();
        return this.mPowerStatusController.isPowerStatusStandby() || this.mPowerStatusController.isPowerStatusTransientToStandby();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public boolean isPowerStandby() {
        assertRunOnServiceThread();
        return this.mPowerStatusController.isPowerStatusStandby();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void wakeUp() {
        assertRunOnServiceThread();
        this.mWakeUpMessageReceived = true;
        this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 8, "android.server.hdmi:WAKE");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void standby() {
        assertRunOnServiceThread();
        if (canGoToStandby()) {
            this.mStandbyMessageReceived = true;
            this.mPowerManager.goToSleep(SystemClock.uptimeMillis(), 5, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isWakeUpMessageReceived() {
        return this.mWakeUpMessageReceived;
    }

    @VisibleForTesting
    protected boolean isStandbyMessageReceived() {
        return this.mStandbyMessageReceived;
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    protected void onWakeUp(int i) {
        int i2;
        assertRunOnServiceThread();
        int i3 = 2;
        this.mPowerStatusController.setPowerStatus(2, false);
        if (this.mCecController != null) {
            if (isCecControlEnabled()) {
                if (i == 0) {
                    i2 = this.mWakeUpMessageReceived ? 3 : 2;
                } else {
                    if (i != 1) {
                        Slog.e(TAG, "wakeUpAction " + i + " not defined.");
                        return;
                    }
                    i2 = 1;
                }
                initializeCec(i2);
            }
        } else {
            Slog.i(TAG, "Device does not support HDMI-CEC.");
        }
        if (isEarcSupported()) {
            if (isEarcEnabled()) {
                if (i != 0) {
                    if (i != 1) {
                        Slog.e(TAG, "wakeUpAction " + i + " not defined.");
                        return;
                    }
                    i3 = 1;
                }
                initializeEarc(i3);
                return;
            }
            setEarcEnabledInHal(false, false);
        }
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    protected void onStandby(final int i) {
        this.mWakeUpMessageReceived = false;
        assertRunOnServiceThread();
        this.mPowerStatusController.setPowerStatus(3, false);
        invokeVendorCommandListenersOnControlStateChanged(false, 3);
        final List<HdmiCecLocalDevice> allCecLocalDevices = getAllCecLocalDevices();
        if (!isStandbyMessageReceived() && !canGoToStandby()) {
            this.mPowerStatusController.setPowerStatus(1);
            Iterator<HdmiCecLocalDevice> it = allCecLocalDevices.iterator();
            while (it.hasNext()) {
                it.next().onStandby(this.mStandbyMessageReceived, i);
            }
            return;
        }
        disableCecLocalDevices(new HdmiCecLocalDevice.PendingActionClearedCallback() { // from class: com.android.server.hdmi.HdmiControlService.20
            @Override // com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback
            public void onCleared(HdmiCecLocalDevice hdmiCecLocalDevice) {
                Slog.v(HdmiControlService.TAG, "On standby-action cleared:" + hdmiCecLocalDevice.mDeviceType);
                allCecLocalDevices.remove(hdmiCecLocalDevice);
                if (allCecLocalDevices.isEmpty()) {
                    HdmiControlService.this.onPendingActionsCleared(i);
                }
            }
        });
    }

    boolean canGoToStandby() {
        Iterator<HdmiCecLocalDevice> it = this.mHdmiCecNetwork.getLocalDeviceList().iterator();
        while (it.hasNext()) {
            if (!it.next().canGoToStandby()) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @HdmiAnnotations.ServiceThreadOnly
    public void onLanguageChanged(String str) {
        assertRunOnServiceThread();
        this.mMenuLanguage = str;
        if (isTvDeviceEnabled()) {
            tv().broadcastMenuLanguage(str);
            this.mCecController.setLanguage(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public String getLanguage() {
        assertRunOnServiceThread();
        return this.mMenuLanguage;
    }

    private void disableCecLocalDevices(HdmiCecLocalDevice.PendingActionClearedCallback pendingActionClearedCallback) {
        if (this.mCecController != null) {
            Iterator<HdmiCecLocalDevice> it = this.mHdmiCecNetwork.getLocalDeviceList().iterator();
            while (it.hasNext()) {
                it.next().disableDevice(this.mStandbyMessageReceived, pendingActionClearedCallback);
            }
        }
        this.mMhlController.clearAllLocalDevices();
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    protected void clearCecLocalDevices() {
        assertRunOnServiceThread();
        HdmiCecController hdmiCecController = this.mCecController;
        if (hdmiCecController == null) {
            return;
        }
        hdmiCecController.clearLogicalAddress();
        this.mHdmiCecNetwork.clearLocalDevices();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @HdmiAnnotations.ServiceThreadOnly
    public void onPendingActionsCleared(int i) {
        assertRunOnServiceThread();
        Slog.v(TAG, "onPendingActionsCleared");
        if (this.mPowerStatusController.isPowerStatusTransientToStandby()) {
            this.mPowerStatusController.setPowerStatus(1);
            Iterator<HdmiCecLocalDevice> it = this.mHdmiCecNetwork.getLocalDeviceList().iterator();
            while (it.hasNext()) {
                it.next().onStandby(this.mStandbyMessageReceived, i);
            }
            if (!isAudioSystemDevice()) {
                this.mCecController.enableSystemCecControl(false);
                this.mMhlController.setOption(HdmiCecKeycode.CEC_KEYCODE_SELECT_MEDIA_FUNCTION, 0);
            }
        }
        this.mStandbyMessageReceived = false;
    }

    @VisibleForTesting
    void addVendorCommandListener(IHdmiVendorCommandListener iHdmiVendorCommandListener, int i) {
        VendorCommandListenerRecord vendorCommandListenerRecord = new VendorCommandListenerRecord(iHdmiVendorCommandListener, i);
        try {
            iHdmiVendorCommandListener.asBinder().linkToDeath(vendorCommandListenerRecord, 0);
            synchronized (this.mLock) {
                this.mVendorCommandListenerRecords.add(vendorCommandListenerRecord);
            }
        } catch (RemoteException unused) {
            Slog.w(TAG, "Listener already died");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean invokeVendorCommandListenersOnReceived(int i, int i2, int i3, byte[] bArr, boolean z) {
        synchronized (this.mLock) {
            if (this.mVendorCommandListenerRecords.isEmpty()) {
                return false;
            }
            Iterator<VendorCommandListenerRecord> it = this.mVendorCommandListenerRecords.iterator();
            while (it.hasNext()) {
                VendorCommandListenerRecord next = it.next();
                if (z) {
                    if (next.mVendorId != ((bArr[0] & 255) << 16) + ((bArr[1] & 255) << 8) + (bArr[2] & 255)) {
                        continue;
                    }
                }
                try {
                    next.mListener.onReceived(i2, i3, bArr, z);
                    return true;
                } catch (RemoteException e) {
                    Slog.e(TAG, "Failed to notify vendor command reception", e);
                }
            }
            return false;
        }
    }

    boolean invokeVendorCommandListenersOnControlStateChanged(boolean z, int i) {
        synchronized (this.mLock) {
            if (this.mVendorCommandListenerRecords.isEmpty()) {
                return false;
            }
            Iterator<VendorCommandListenerRecord> it = this.mVendorCommandListenerRecords.iterator();
            while (it.hasNext()) {
                try {
                    it.next().mListener.onControlStateChanged(z, i);
                } catch (RemoteException e) {
                    Slog.e(TAG, "Failed to notify control-state-changed to vendor handler", e);
                }
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addHdmiMhlVendorCommandListener(IHdmiMhlVendorCommandListener iHdmiMhlVendorCommandListener) {
        HdmiMhlVendorCommandListenerRecord hdmiMhlVendorCommandListenerRecord = new HdmiMhlVendorCommandListenerRecord(iHdmiMhlVendorCommandListener);
        try {
            iHdmiMhlVendorCommandListener.asBinder().linkToDeath(hdmiMhlVendorCommandListenerRecord, 0);
            synchronized (this.mLock) {
                this.mMhlVendorCommandListenerRecords.add(hdmiMhlVendorCommandListenerRecord);
            }
        } catch (RemoteException unused) {
            Slog.w(TAG, "Listener already died.");
        }
    }

    void invokeMhlVendorCommandListeners(int i, int i2, int i3, byte[] bArr) {
        synchronized (this.mLock) {
            Iterator<HdmiMhlVendorCommandListenerRecord> it = this.mMhlVendorCommandListenerRecords.iterator();
            while (it.hasNext()) {
                try {
                    it.next().mListener.onReceived(i, i2, i3, bArr);
                } catch (RemoteException e) {
                    Slog.e(TAG, "Failed to notify MHL vendor command", e);
                }
            }
        }
    }

    void setStandbyMode(boolean z) {
        assertRunOnServiceThread();
        if (isPowerOnOrTransient() && z) {
            this.mPowerManager.goToSleep(SystemClock.uptimeMillis(), 5, 0);
            if (playback() != null) {
                playback().sendStandby(0);
                return;
            }
            return;
        }
        if (!isPowerStandbyOrTransient() || z) {
            return;
        }
        this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 8, "android.server.hdmi:WAKE");
        if (playback() != null) {
            oneTouchPlay(new IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiControlService.21
                public void onComplete(int i) {
                    if (i != 0) {
                        Slog.w(HdmiControlService.TAG, "Failed to complete 'one touch play'. result=" + i);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getHdmiCecVolumeControl() {
        int i;
        synchronized (this.mLock) {
            i = this.mHdmiCecVolumeControl;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isProhibitMode() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mProhibitMode;
        }
        return z;
    }

    void setProhibitMode(boolean z) {
        synchronized (this.mLock) {
            this.mProhibitMode = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSystemAudioActivated() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSystemAudioActivated;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSystemAudioActivated(boolean z) {
        synchronized (this.mLock) {
            this.mSystemAudioActivated = z;
        }
        runOnServiceThread(new HdmiControlService$$ExternalSyntheticLambda2(this));
    }

    @HdmiAnnotations.ServiceThreadOnly
    void setCecEnabled(int i) {
        assertRunOnServiceThread();
        synchronized (this.mLock) {
            this.mHdmiControlEnabled = i;
        }
        if (i == 1) {
            onEnableCec();
            setHdmiCecVolumeControlEnabledInternal(getHdmiCecConfig().getIntValue("volume_control_enabled"));
        } else {
            setHdmiCecVolumeControlEnabledInternal(0);
            invokeVendorCommandListenersOnControlStateChanged(false, 1);
            runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.22
                @Override // java.lang.Runnable
                public void run() {
                    HdmiControlService.this.onDisableCec();
                }
            });
            announceHdmiControlStatusChange(i);
        }
    }

    @HdmiAnnotations.ServiceThreadOnly
    private void onEnableCec() {
        this.mCecController.enableCec(true);
        this.mCecController.enableSystemCecControl(true);
        this.mMhlController.setOption(HdmiCecKeycode.CEC_KEYCODE_TUNE_FUNCTION, 1);
        initializeCec(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @HdmiAnnotations.ServiceThreadOnly
    public void onDisableCec() {
        disableCecLocalDevices(new HdmiCecLocalDevice.PendingActionClearedCallback() { // from class: com.android.server.hdmi.HdmiControlService.23
            @Override // com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback
            public void onCleared(HdmiCecLocalDevice hdmiCecLocalDevice) {
                HdmiControlService.this.assertRunOnServiceThread();
                HdmiControlService.this.mCecController.flush(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.23.1
                    @Override // java.lang.Runnable
                    public void run() {
                        HdmiControlService.this.mCecController.enableCec(false);
                        HdmiControlService.this.mCecController.enableSystemCecControl(false);
                        HdmiControlService.this.mMhlController.setOption(HdmiCecKeycode.CEC_KEYCODE_TUNE_FUNCTION, 0);
                        HdmiControlService.this.clearCecLocalDevices();
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void setActivePortId(int i) {
        assertRunOnServiceThread();
        this.mActivePortId = i;
        setLastInputForMhl(-1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiCecLocalDevice.ActiveSource getLocalActiveSource() {
        HdmiCecLocalDevice.ActiveSource activeSource;
        synchronized (this.mLock) {
            activeSource = this.mActiveSource;
        }
        return activeSource;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void pauseActiveMediaSessions() {
        Iterator<MediaController> it = ((MediaSessionManager) getContext().getSystemService(MediaSessionManager.class)).getActiveSessions(null).iterator();
        while (it.hasNext()) {
            it.next().getTransportControls().pause();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setActiveSource(int i, int i2, String str) {
        synchronized (this.mLock) {
            HdmiCecLocalDevice.ActiveSource activeSource = this.mActiveSource;
            activeSource.logicalAddress = i;
            activeSource.physicalAddress = i2;
        }
        getAtomWriter().activeSourceChanged(i, i2, HdmiUtils.pathRelationship(getPhysicalAddress(), i2));
        for (HdmiCecLocalDevice hdmiCecLocalDevice : getAllCecLocalDevices()) {
            hdmiCecLocalDevice.addActiveSourceHistoryItem(new HdmiCecLocalDevice.ActiveSource(i, i2), i == hdmiCecLocalDevice.getDeviceInfo().getLogicalAddress() && i2 == getPhysicalAddress(), str);
        }
        runOnServiceThread(new HdmiControlService$$ExternalSyntheticLambda2(this));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAndBroadcastActiveSource(int i, int i2, int i3, String str) {
        if (i2 == 4) {
            HdmiCecLocalDevicePlayback playback = playback();
            playback.setActiveSource(playback.getDeviceInfo().getLogicalAddress(), i, str);
            playback.wakeUpIfActiveSource();
            playback.maySendActiveSource(i3);
        }
        if (i2 == 5) {
            HdmiCecLocalDeviceAudioSystem audioSystem = audioSystem();
            if (playback() == null) {
                audioSystem.setActiveSource(audioSystem.getDeviceInfo().getLogicalAddress(), i, str);
                audioSystem.wakeUpIfActiveSource();
                audioSystem.maySendActiveSource(i3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAndBroadcastActiveSourceFromOneDeviceType(int i, int i2, String str) {
        HdmiCecLocalDevicePlayback playback = playback();
        HdmiCecLocalDeviceAudioSystem audioSystem = audioSystem();
        if (playback != null) {
            playback.setActiveSource(playback.getDeviceInfo().getLogicalAddress(), i2, str);
            playback.wakeUpIfActiveSource();
            playback.maySendActiveSource(i);
        } else if (audioSystem != null) {
            audioSystem.setActiveSource(audioSystem.getDeviceInfo().getLogicalAddress(), i2, str);
            audioSystem.wakeUpIfActiveSource();
            audioSystem.maySendActiveSource(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void setLastInputForMhl(int i) {
        assertRunOnServiceThread();
        this.mLastInputMhl = i;
    }

    @HdmiAnnotations.ServiceThreadOnly
    int getLastInputForMhl() {
        assertRunOnServiceThread();
        return this.mLastInputMhl;
    }

    @HdmiAnnotations.ServiceThreadOnly
    void changeInputForMhl(int i, boolean z) {
        HdmiDeviceInfo deviceForPortId;
        assertRunOnServiceThread();
        if (tv() == null) {
            return;
        }
        final int activePortId = z ? tv().getActivePortId() : -1;
        if (i != -1) {
            tv().doManualPortSwitching(i, new IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiControlService.24
                public void onComplete(int i2) throws RemoteException {
                    HdmiControlService.this.setLastInputForMhl(activePortId);
                }
            });
        }
        tv().setActivePortId(i);
        HdmiMhlLocalDeviceStub localDevice = this.mMhlController.getLocalDevice(i);
        if (localDevice != null) {
            deviceForPortId = localDevice.getInfo();
        } else {
            deviceForPortId = this.mHdmiCecNetwork.getDeviceForPortId(i);
        }
        invokeInputChangeListener(deviceForPortId);
    }

    void setMhlInputChangeEnabled(boolean z) {
        this.mMhlController.setOption(HdmiCecKeycode.CEC_KEYCODE_MUTE_FUNCTION, toInt(z));
        synchronized (this.mLock) {
            this.mMhlInputChangeEnabled = z;
        }
    }

    @VisibleForTesting
    protected HdmiCecAtomWriter getAtomWriter() {
        return this.mAtomWriter;
    }

    boolean isMhlInputChangeEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mMhlInputChangeEnabled;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void displayOsd(int i) {
        assertRunOnServiceThread();
        Intent intent = new Intent("android.hardware.hdmi.action.OSD_MESSAGE");
        intent.putExtra("android.hardware.hdmi.extra.MESSAGE_ID", i);
        getContext().sendBroadcastAsUser(intent, UserHandle.ALL, PERMISSION);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void displayOsd(int i, int i2) {
        assertRunOnServiceThread();
        Intent intent = new Intent("android.hardware.hdmi.action.OSD_MESSAGE");
        intent.putExtra("android.hardware.hdmi.extra.MESSAGE_ID", i);
        intent.putExtra("android.hardware.hdmi.extra.MESSAGE_EXTRA_PARAM1", i2);
        getContext().sendBroadcastAsUser(intent, UserHandle.ALL, PERMISSION);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    public HdmiCecConfig getHdmiCecConfig() {
        return this.mHdmiCecConfig;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.hdmi.HdmiControlService$25, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass25 implements HdmiCecConfig.SettingChangeListener {
        AnonymousClass25() {
        }

        @Override // com.android.server.hdmi.HdmiCecConfig.SettingChangeListener
        public void onChange(final String str) {
            synchronized (HdmiControlService.this.mLock) {
                if (HdmiControlService.this.mHdmiCecSettingChangeListenerRecords.containsKey(str)) {
                    ((RemoteCallbackList) HdmiControlService.this.mHdmiCecSettingChangeListenerRecords.get(str)).broadcast(new Consumer() { // from class: com.android.server.hdmi.HdmiControlService$25$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            HdmiControlService.AnonymousClass25.this.lambda$onChange$0(str, (IHdmiCecSettingChangeListener) obj);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChange$0(String str, IHdmiCecSettingChangeListener iHdmiCecSettingChangeListener) {
            HdmiControlService.this.invokeCecSettingChangeListenerLocked(str, iHdmiCecSettingChangeListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addCecSettingChangeListener(String str, IHdmiCecSettingChangeListener iHdmiCecSettingChangeListener) {
        synchronized (this.mLock) {
            if (!this.mHdmiCecSettingChangeListenerRecords.containsKey(str)) {
                this.mHdmiCecSettingChangeListenerRecords.put(str, new RemoteCallbackList<>());
                this.mHdmiCecConfig.registerChangeListener(str, this.mSettingChangeListener);
            }
            this.mHdmiCecSettingChangeListenerRecords.get(str).register(iHdmiCecSettingChangeListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeCecSettingChangeListener(String str, IHdmiCecSettingChangeListener iHdmiCecSettingChangeListener) {
        synchronized (this.mLock) {
            if (this.mHdmiCecSettingChangeListenerRecords.containsKey(str)) {
                this.mHdmiCecSettingChangeListenerRecords.get(str).unregister(iHdmiCecSettingChangeListener);
                if (this.mHdmiCecSettingChangeListenerRecords.get(str).getRegisteredCallbackCount() == 0) {
                    this.mHdmiCecSettingChangeListenerRecords.remove(str);
                    this.mHdmiCecConfig.removeChangeListener(str, this.mSettingChangeListener);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeCecSettingChangeListenerLocked(String str, IHdmiCecSettingChangeListener iHdmiCecSettingChangeListener) {
        try {
            iHdmiCecSettingChangeListener.onChange(str);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to report setting change", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    public void onDeviceVolumeBehaviorChanged(AudioDeviceAttributes audioDeviceAttributes, int i) {
        assertRunOnServiceThread();
        if (AVB_AUDIO_OUTPUT_DEVICES.contains(audioDeviceAttributes)) {
            synchronized (this.mLock) {
                this.mAudioDeviceVolumeBehaviors.put(audioDeviceAttributes, Integer.valueOf(i));
            }
            checkAndUpdateAbsoluteVolumeBehavior();
        }
    }

    private int getDeviceVolumeBehavior(AudioDeviceAttributes audioDeviceAttributes) {
        if (AVB_AUDIO_OUTPUT_DEVICES.contains(audioDeviceAttributes)) {
            synchronized (this.mLock) {
                if (this.mAudioDeviceVolumeBehaviors.containsKey(audioDeviceAttributes)) {
                    return this.mAudioDeviceVolumeBehaviors.get(audioDeviceAttributes).intValue();
                }
            }
        }
        return getAudioManager().getDeviceVolumeBehavior(audioDeviceAttributes);
    }

    public boolean isAbsoluteVolumeBehaviorEnabled() {
        AudioDeviceAttributes avbAudioOutputDevice;
        if ((!isTvDevice() && !isPlaybackDevice()) || (avbAudioOutputDevice = getAvbAudioOutputDevice()) == null) {
            return false;
        }
        int deviceVolumeBehavior = getDeviceVolumeBehavior(avbAudioOutputDevice);
        return deviceVolumeBehavior == 3 || deviceVolumeBehavior == 5;
    }

    private AudioDeviceAttributes getAvbAudioOutputDevice() {
        if (tv() != null) {
            return tv().getSystemAudioOutputDevice();
        }
        if (playback() != null) {
            return AUDIO_OUTPUT_DEVICE_HDMI;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void checkAndUpdateAbsoluteVolumeBehavior() {
        HdmiCecLocalDevice playback;
        assertRunOnServiceThread();
        if (getAudioManager() == null) {
            return;
        }
        if (isTvDevice() && tv() != null) {
            playback = tv();
            if (!isSystemAudioActivated()) {
                switchToFullVolumeBehavior();
                return;
            }
        } else if (!isPlaybackDevice() || playback() == null) {
            return;
        } else {
            playback = playback();
        }
        HdmiDeviceInfo deviceInfo = getDeviceInfo(playback.findAudioReceiverAddress());
        int deviceVolumeBehavior = getDeviceVolumeBehavior(getAvbAudioOutputDevice());
        boolean z = deviceVolumeBehavior == 1 || deviceVolumeBehavior == 3 || deviceVolumeBehavior == 5;
        if (!(getHdmiCecVolumeControl() == 1) || !z) {
            switchToFullVolumeBehavior();
            return;
        }
        if (deviceInfo == null) {
            switchToFullVolumeBehavior();
            return;
        }
        int setAudioVolumeLevelSupport = deviceInfo.getDeviceFeatures().getSetAudioVolumeLevelSupport();
        if (setAudioVolumeLevelSupport == 0) {
            if (tv() == null || !this.mNumericSoundbarVolumeUiOnTvFeatureFlagEnabled) {
                switchToFullVolumeBehavior();
                return;
            } else {
                if (deviceVolumeBehavior != 5) {
                    if (deviceVolumeBehavior == 3) {
                        getAudioManager().setDeviceVolumeBehavior(getAvbAudioOutputDevice(), 1);
                    }
                    playback.startNewAvbAudioStatusAction(deviceInfo.getLogicalAddress());
                    return;
                }
                return;
            }
        }
        if (setAudioVolumeLevelSupport == 1) {
            if (deviceVolumeBehavior != 3) {
                playback.startNewAvbAudioStatusAction(deviceInfo.getLogicalAddress());
            }
        } else {
            if (setAudioVolumeLevelSupport != 2) {
                return;
            }
            if (deviceVolumeBehavior == 3) {
                switchToFullVolumeBehavior();
            }
            playback.querySetAudioVolumeLevelSupport(deviceInfo.getLogicalAddress());
        }
    }

    private void switchToFullVolumeBehavior() {
        if (playback() != null) {
            playback().removeAvbAudioStatusAction();
        } else if (tv() != null) {
            tv().removeAvbAudioStatusAction();
        }
        AudioDeviceAttributes avbAudioOutputDevice = getAvbAudioOutputDevice();
        int deviceVolumeBehavior = getDeviceVolumeBehavior(avbAudioOutputDevice);
        if (deviceVolumeBehavior == 3 || deviceVolumeBehavior == 5) {
            getAudioManager().setDeviceVolumeBehavior(avbAudioOutputDevice, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enableAbsoluteVolumeBehavior(AudioStatus audioStatus) {
        HdmiCecLocalDevice playback = isPlaybackDevice() ? playback() : tv();
        HdmiDeviceInfo deviceInfo = getDeviceInfo(playback.findAudioReceiverAddress());
        VolumeInfo build = new VolumeInfo.Builder(3).setMuted(audioStatus.getMute()).setVolumeIndex(audioStatus.getVolume()).setMaxVolumeIndex(100).setMinVolumeIndex(0).build();
        this.mAbsoluteVolumeChangedListener = new AbsoluteVolumeChangedListener(playback, deviceInfo);
        notifyAvbMuteChange(audioStatus.getMute());
        if (deviceInfo.getDeviceFeatures().getSetAudioVolumeLevelSupport() == 1) {
            getAudioDeviceVolumeManager().setDeviceAbsoluteVolumeBehavior(getAvbAudioOutputDevice(), build, this.mServiceThreadExecutor, this.mAbsoluteVolumeChangedListener, true);
        } else if (tv() != null) {
            getAudioDeviceVolumeManager().setDeviceAbsoluteVolumeAdjustOnlyBehavior(getAvbAudioOutputDevice(), build, this.mServiceThreadExecutor, this.mAbsoluteVolumeChangedListener, true);
        }
    }

    @VisibleForTesting
    AbsoluteVolumeChangedListener getAbsoluteVolumeChangedListener() {
        return this.mAbsoluteVolumeChangedListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AbsoluteVolumeChangedListener implements AudioDeviceVolumeManager.OnAudioDeviceVolumeChangedListener {
        private HdmiCecLocalDevice mLocalDevice;
        private HdmiDeviceInfo mSystemAudioDevice;

        private AbsoluteVolumeChangedListener(HdmiCecLocalDevice hdmiCecLocalDevice, HdmiDeviceInfo hdmiDeviceInfo) {
            this.mLocalDevice = hdmiCecLocalDevice;
            this.mSystemAudioDevice = hdmiDeviceInfo;
        }

        public void onAudioDeviceVolumeChanged(AudioDeviceAttributes audioDeviceAttributes, final VolumeInfo volumeInfo) {
            if (this.mSystemAudioDevice.getDeviceFeatures().getSetAudioVolumeLevelSupport() != 1) {
                return;
            }
            final int logicalAddress = this.mLocalDevice.getDeviceInfo().getLogicalAddress();
            HdmiControlService.this.sendCecCommand(SetAudioVolumeLevelMessage.build(logicalAddress, this.mSystemAudioDevice.getLogicalAddress(), volumeInfo.getVolumeIndex()), new SendMessageCallback() { // from class: com.android.server.hdmi.HdmiControlService$AbsoluteVolumeChangedListener$$ExternalSyntheticLambda0
                @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
                public final void onSendCompleted(int i) {
                    HdmiControlService.AbsoluteVolumeChangedListener.this.lambda$onAudioDeviceVolumeChanged$0(volumeInfo, logicalAddress, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAudioDeviceVolumeChanged$0(VolumeInfo volumeInfo, int i, int i2) {
            if (i2 == 0) {
                boolean isTvDevice = HdmiControlService.this.isTvDevice();
                HdmiControlService hdmiControlService = HdmiControlService.this;
                (isTvDevice ? hdmiControlService.tv() : hdmiControlService.playback()).updateAvbVolume(volumeInfo.getVolumeIndex());
                return;
            }
            HdmiControlService.this.sendCecCommand(HdmiCecMessageBuilder.buildGiveAudioStatus(i, this.mSystemAudioDevice.getLogicalAddress()));
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x001e  */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0030  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onAudioDeviceVolumeAdjusted(AudioDeviceAttributes audioDeviceAttributes, VolumeInfo volumeInfo, int i, int i2) {
            int i3;
            if (i != -100) {
                if (i == -1) {
                    i3 = 25;
                } else if (i == 1) {
                    i3 = 24;
                } else if (i != 100 && i != 101) {
                    return;
                }
                if (i2 != 0) {
                    this.mLocalDevice.sendVolumeKeyEvent(i3, true);
                    this.mLocalDevice.sendVolumeKeyEvent(i3, false);
                    return;
                } else if (i2 == 1) {
                    this.mLocalDevice.sendVolumeKeyEvent(i3, true);
                    return;
                } else {
                    if (i2 != 2) {
                        return;
                    }
                    this.mLocalDevice.sendVolumeKeyEvent(i3, false);
                    return;
                }
            }
            i3 = 164;
            if (i2 != 0) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyAvbVolumeChange(int i) {
        if (isAbsoluteVolumeBehaviorEnabled() && getAudioManager().getDevicesForAttributes(STREAM_MUSIC_ATTRIBUTES).contains(getAvbAudioOutputDevice())) {
            setStreamMusicVolume(i, isTvDevice() ? UsbACInterface.FORMAT_III_IEC1937AC3 : 8192);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyAvbMuteChange(boolean z) {
        if (isAbsoluteVolumeBehaviorEnabled() && getAudioManager().getDevicesForAttributes(STREAM_MUSIC_ATTRIBUTES).contains(getAvbAudioOutputDevice())) {
            getAudioManager().adjustStreamVolume(3, z ? -100 : 100, isTvDevice() ? UsbACInterface.FORMAT_III_IEC1937AC3 : 8192);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStreamMusicVolume(int i, int i2) {
        getAudioManager().setStreamVolume(3, (i * this.mStreamMusicMaxVolume) / 100, i2);
    }

    private void initializeEarc(int i) {
        Slog.i(TAG, "eARC initialized, reason = " + i);
        initializeEarcLocalDevice(i);
        if (i == 6) {
            setEarcEnabledInHal(true, true);
        } else {
            setEarcEnabledInHal(true, false);
        }
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    protected void initializeEarcLocalDevice(int i) {
        assertRunOnServiceThread();
        if (this.mEarcLocalDevice == null) {
            this.mEarcLocalDevice = HdmiEarcLocalDevice.create(this, 0);
        }
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    protected void setEarcEnabled(int i) {
        assertRunOnServiceThread();
        synchronized (this.mLock) {
            this.mEarcEnabled = i == 1;
            if (!isEarcSupported()) {
                Slog.i(TAG, "Enabled/disabled eARC setting, but the hardware doesn´t support eARC. This settings change doesn´t have an effect.");
            } else if (this.mEarcEnabled) {
                onEnableEarc();
            } else {
                runOnServiceThread(new Runnable() { // from class: com.android.server.hdmi.HdmiControlService.26
                    @Override // java.lang.Runnable
                    public void run() {
                        HdmiControlService.this.onDisableEarc();
                    }
                });
            }
        }
    }

    @VisibleForTesting
    protected void setEarcSupported(boolean z) {
        synchronized (this.mLock) {
            this.mEarcSupported = z;
        }
    }

    @HdmiAnnotations.ServiceThreadOnly
    private void onEnableEarc() {
        initializeEarc(6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @HdmiAnnotations.ServiceThreadOnly
    public void onDisableEarc() {
        disableEarcLocalDevice();
        setEarcEnabledInHal(false, false);
        clearEarcLocalDevice();
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    protected void clearEarcLocalDevice() {
        assertRunOnServiceThread();
        this.mEarcLocalDevice = null;
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    protected void addEarcLocalDevice(HdmiEarcLocalDevice hdmiEarcLocalDevice) {
        assertRunOnServiceThread();
        this.mEarcLocalDevice = hdmiEarcLocalDevice;
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    HdmiEarcLocalDevice getEarcLocalDevice() {
        assertRunOnServiceThread();
        return this.mEarcLocalDevice;
    }

    private void disableEarcLocalDevice() {
        HdmiEarcLocalDevice hdmiEarcLocalDevice = this.mEarcLocalDevice;
        if (hdmiEarcLocalDevice == null) {
            return;
        }
        hdmiEarcLocalDevice.disableDevice();
    }

    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    protected void setEarcEnabledInHal(final boolean z, boolean z2) {
        assertRunOnServiceThread();
        if (z2) {
            startArcAction(false, new IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiControlService.27
                public void onComplete(int i) throws RemoteException {
                    if (i != 0) {
                        Slog.w(HdmiControlService.TAG, "ARC termination before enabling eARC in the HAL failed with result: " + i);
                    }
                    HdmiControlService.this.mEarcController.setEarcEnabled(z);
                    HdmiControlService.this.mCecController.setHpdSignalType(z ? 1 : 0, HdmiControlService.this.mEarcPortId);
                }
            });
            return;
        }
        this.mEarcController.setEarcEnabled(z);
        this.mCecController.setHpdSignalType(z ? 1 : 0, this.mEarcPortId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void handleEarcStateChange(int i, int i2) {
        assertRunOnServiceThread();
        if (!getPortInfo(i2).isEarcSupported()) {
            Slog.w(TAG, "Tried to update eARC status on a port that doesn't support eARC.");
            return;
        }
        HdmiEarcLocalDevice hdmiEarcLocalDevice = this.mEarcLocalDevice;
        if (hdmiEarcLocalDevice != null) {
            hdmiEarcLocalDevice.handleEarcStateChange(i);
        } else if (i == 2) {
            notifyEarcStatusToAudioService(false, new ArrayList());
            startArcAction(true, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void notifyEarcStatusToAudioService(boolean z, List<AudioDescriptor> list) {
        getAudioManager().setWiredDeviceConnectionState(new AudioDeviceAttributes(2, 29, "", "", new ArrayList(), list), z ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void handleEarcCapabilitiesReported(byte[] bArr, int i) {
        assertRunOnServiceThread();
        if (!getPortInfo(i).isEarcSupported()) {
            Slog.w(TAG, "Tried to process eARC capabilities from a port that doesn't support eARC.");
            return;
        }
        HdmiEarcLocalDevice hdmiEarcLocalDevice = this.mEarcLocalDevice;
        if (hdmiEarcLocalDevice != null) {
            hdmiEarcLocalDevice.handleEarcCapabilitiesReported(bArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean earcBlocksArcConnection() {
        boolean z;
        if (this.mEarcLocalDevice == null) {
            return false;
        }
        synchronized (this.mLock) {
            z = this.mEarcLocalDevice.mEarcStatus != 2;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void startArcAction(boolean z, IHdmiControlCallback iHdmiControlCallback) {
        if (!isTvDeviceEnabled()) {
            invokeCallback(iHdmiControlCallback, 6);
        } else {
            tv().startArcAction(z, iHdmiControlCallback);
        }
    }
}
