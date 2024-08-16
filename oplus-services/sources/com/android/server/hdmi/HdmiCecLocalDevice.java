package com.android.server.hdmi;

import android.hardware.hdmi.DeviceFeatures;
import android.hardware.hdmi.HdmiDeviceInfo;
import android.hardware.hdmi.IHdmiControlCallback;
import android.hardware.input.InputManagerGlobal;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Slog;
import android.view.KeyEvent;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.hdmi.Constants;
import com.android.server.hdmi.HdmiAnnotations;
import com.android.server.hdmi.HdmiCecController;
import com.android.server.hdmi.HdmiControlService;
import com.android.server.location.gnss.hal.GnssNative;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Predicate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class HdmiCecLocalDevice extends HdmiLocalDevice {
    private static final int DEVICE_CLEANUP_TIMEOUT = 5000;
    private static final int FOLLOWER_SAFETY_TIMEOUT = 550;
    private static final int MAX_HDMI_ACTIVE_SOURCE_HISTORY = 10;
    private static final int MSG_DISABLE_DEVICE_TIMEOUT = 1;
    private static final int MSG_USER_CONTROL_RELEASE_TIMEOUT = 2;
    private static final String TAG = "HdmiCecLocalDevice";

    @VisibleForTesting
    final ArrayList<HdmiCecFeatureAction> mActions;

    @GuardedBy({"mLock"})
    private int mActiveRoutingPath;
    private final ArrayBlockingQueue<HdmiCecController.Dumpable> mActiveSourceHistory;
    protected final HdmiCecMessageCache mCecMessageCache;

    @GuardedBy({"mLock"})
    private HdmiDeviceInfo mDeviceInfo;
    private final Handler mHandler;
    protected int mLastKeyRepeatCount;
    protected int mLastKeycode;
    protected PendingActionClearedCallback mPendingActionClearedCallback;
    protected int mPreferredAddress;
    HdmiCecStandbyModeHandler mStandbyHandler;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface PendingActionClearedCallback {
        void onCleared(HdmiCecLocalDevice hdmiCecLocalDevice);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean canGoToStandby() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int getPreferredAddress();

    protected abstract List<Integer> getRcFeatures();

    @Constants.RcProfile
    protected abstract int getRcProfile();

    @HdmiAnnotations.ServiceThreadOnly
    protected int handleActiveSource(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleGiveAudioStatus(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleGiveSystemAudioModeStatus(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleImageViewOn(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    @HdmiAnnotations.ServiceThreadOnly
    protected int handleInactiveSource(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleInitiateArc(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleMenuStatus(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleRecordStatus(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleRecordTvScreen(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleReportArcInitiate(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleReportArcTermination(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleReportAudioStatus(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleReportPowerStatus(HdmiCecMessage hdmiCecMessage) {
        return -1;
    }

    protected int handleReportShortAudioDescriptor(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    @HdmiAnnotations.ServiceThreadOnly
    protected int handleRequestActiveSource(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleRequestArcInitiate(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleRequestArcTermination(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleRequestShortAudioDescriptor(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleRoutingChange(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleRoutingInformation(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleSetAudioVolumeLevel(SetAudioVolumeLevelMessage setAudioVolumeLevelMessage) {
        return -2;
    }

    protected int handleSetOsdName(HdmiCecMessage hdmiCecMessage) {
        return -1;
    }

    protected int handleSetStreamPath(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleSetSystemAudioMode(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleSystemAudioModeRequest(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleSystemAudioModeStatus(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleTerminateArc(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleTextViewOn(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleTimerClearedStatus(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    protected int handleTimerStatus(HdmiCecMessage hdmiCecMessage) {
        return -2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isInputReady(int i) {
        return true;
    }

    protected abstract void onAddressAllocated(int i, int i2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onHotplug(int i, boolean z) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onInitializeCecComplete(int i) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onStandby(boolean z, int i) {
    }

    protected void preprocessBufferedMessages(List<HdmiCecMessage> list) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendStandby(int i) {
    }

    protected abstract void setPreferredAddress(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ActiveSource {
        int logicalAddress;
        int physicalAddress;

        public ActiveSource() {
            invalidate();
        }

        public ActiveSource(int i, int i2) {
            this.logicalAddress = i;
            this.physicalAddress = i2;
        }

        public static ActiveSource of(ActiveSource activeSource) {
            return new ActiveSource(activeSource.logicalAddress, activeSource.physicalAddress);
        }

        public static ActiveSource of(int i, int i2) {
            return new ActiveSource(i, i2);
        }

        public boolean isValid() {
            return HdmiUtils.isValidAddress(this.logicalAddress);
        }

        public void invalidate() {
            this.logicalAddress = -1;
            this.physicalAddress = GnssNative.GNSS_AIDING_TYPE_ALL;
        }

        public boolean equals(int i, int i2) {
            return this.logicalAddress == i && this.physicalAddress == i2;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ActiveSource)) {
                return false;
            }
            ActiveSource activeSource = (ActiveSource) obj;
            return activeSource.logicalAddress == this.logicalAddress && activeSource.physicalAddress == this.physicalAddress;
        }

        public int hashCode() {
            return (this.logicalAddress * 29) + this.physicalAddress;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            int i = this.logicalAddress;
            String format = i == -1 ? "invalid" : String.format("0x%02x", Integer.valueOf(i));
            sb.append("(");
            sb.append(format);
            int i2 = this.physicalAddress;
            String format2 = i2 != 65535 ? String.format("0x%04x", Integer.valueOf(i2)) : "invalid";
            sb.append(", ");
            sb.append(format2);
            sb.append(")");
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HdmiCecLocalDevice(HdmiControlService hdmiControlService, int i) {
        super(hdmiControlService, i);
        this.mLastKeycode = -1;
        this.mLastKeyRepeatCount = 0;
        this.mActiveSourceHistory = new ArrayBlockingQueue<>(10);
        this.mCecMessageCache = new HdmiCecMessageCache();
        this.mActions = new ArrayList<>();
        this.mHandler = new Handler() { // from class: com.android.server.hdmi.HdmiCecLocalDevice.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i2 = message.what;
                if (i2 == 1) {
                    HdmiCecLocalDevice.this.handleDisableDeviceTimeout();
                } else {
                    if (i2 != 2) {
                        return;
                    }
                    HdmiCecLocalDevice.this.handleUserControlReleased();
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecLocalDevice create(HdmiControlService hdmiControlService, int i) {
        if (i == 0) {
            return new HdmiCecLocalDeviceTv(hdmiControlService);
        }
        if (i == 4) {
            return new HdmiCecLocalDevicePlayback(hdmiControlService);
        }
        if (i != 5) {
            return null;
        }
        return new HdmiCecLocalDeviceAudioSystem(hdmiControlService);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void init() {
        assertRunOnServiceThread();
        this.mPreferredAddress = getPreferredAddress();
        if (this.mHandler.hasMessages(1)) {
            this.mHandler.removeMessages(1);
            handleDisableDeviceTimeout();
        }
        this.mPendingActionClearedCallback = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    public int dispatchMessage(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        int destination = hdmiCecMessage.getDestination();
        if (destination != this.mDeviceInfo.getLogicalAddress() && destination != 15) {
            return -2;
        }
        if (this.mService.isPowerStandby() && !this.mService.isWakeUpMessageReceived() && this.mStandbyHandler.handleCommand(hdmiCecMessage)) {
            return -1;
        }
        this.mCecMessageCache.cacheMessage(hdmiCecMessage);
        return onMessage(hdmiCecMessage);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HdmiAnnotations.ServiceThreadOnly
    @VisibleForTesting
    public boolean isAlreadyActiveSource(HdmiDeviceInfo hdmiDeviceInfo, int i, IHdmiControlCallback iHdmiControlCallback) {
        ActiveSource activeSource = getActiveSource();
        if (hdmiDeviceInfo.getDevicePowerStatus() != 0 || !activeSource.isValid() || i != activeSource.logicalAddress) {
            return false;
        }
        invokeCallback(iHdmiControlCallback, 0);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void clearDeviceInfoList() {
        assertRunOnServiceThread();
        this.mService.getHdmiCecNetwork().clearDeviceList();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HdmiAnnotations.ServiceThreadOnly
    public final int onMessage(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        if (dispatchMessageToAction(hdmiCecMessage)) {
            return -1;
        }
        if (hdmiCecMessage instanceof SetAudioVolumeLevelMessage) {
            return handleSetAudioVolumeLevel((SetAudioVolumeLevelMessage) hdmiCecMessage);
        }
        int opcode = hdmiCecMessage.getOpcode();
        if (opcode == 4) {
            return handleImageViewOn(hdmiCecMessage);
        }
        if (opcode == 10) {
            return handleRecordStatus(hdmiCecMessage);
        }
        if (opcode == 13) {
            return handleTextViewOn(hdmiCecMessage);
        }
        if (opcode == 15) {
            return handleRecordTvScreen(hdmiCecMessage);
        }
        if (opcode == 50) {
            return handleSetMenuLanguage(hdmiCecMessage);
        }
        if (opcode == 122) {
            return handleReportAudioStatus(hdmiCecMessage);
        }
        if (opcode == 137) {
            return handleVendorCommand(hdmiCecMessage);
        }
        if (opcode == 53) {
            return handleTimerStatus(hdmiCecMessage);
        }
        if (opcode == 54) {
            return handleStandby(hdmiCecMessage);
        }
        if (opcode == 125) {
            return handleGiveSystemAudioModeStatus(hdmiCecMessage);
        }
        if (opcode == 126) {
            return handleSystemAudioModeStatus(hdmiCecMessage);
        }
        switch (opcode) {
            case HdmiCecKeycode.CEC_KEYCODE_MUTE /* 67 */:
                return handleTimerClearedStatus(hdmiCecMessage);
            case HdmiCecKeycode.CEC_KEYCODE_PLAY /* 68 */:
                return handleUserControlPressed(hdmiCecMessage);
            case HdmiCecKeycode.CEC_KEYCODE_STOP /* 69 */:
                return handleUserControlReleased();
            case HdmiCecKeycode.CEC_KEYCODE_PAUSE /* 70 */:
                return handleGiveOsdName(hdmiCecMessage);
            case HdmiCecKeycode.CEC_KEYCODE_RECORD /* 71 */:
                return handleSetOsdName(hdmiCecMessage);
            default:
                switch (opcode) {
                    case HdmiCecKeycode.UI_BROADCAST_DIGITAL_CABLE /* 112 */:
                        return handleSystemAudioModeRequest(hdmiCecMessage);
                    case HdmiCecKeycode.CEC_KEYCODE_F1_BLUE /* 113 */:
                        return handleGiveAudioStatus(hdmiCecMessage);
                    case HdmiCecKeycode.CEC_KEYCODE_F2_RED /* 114 */:
                        return handleSetSystemAudioMode(hdmiCecMessage);
                    default:
                        switch (opcode) {
                            case 128:
                                return handleRoutingChange(hdmiCecMessage);
                            case 129:
                                return handleRoutingInformation(hdmiCecMessage);
                            case 130:
                                return handleActiveSource(hdmiCecMessage);
                            case 131:
                                return handleGivePhysicalAddress(hdmiCecMessage);
                            case 132:
                                return handleReportPhysicalAddress(hdmiCecMessage);
                            case 133:
                                return handleRequestActiveSource(hdmiCecMessage);
                            case 134:
                                return handleSetStreamPath(hdmiCecMessage);
                            default:
                                switch (opcode) {
                                    case 140:
                                        return handleGiveDeviceVendorId(hdmiCecMessage);
                                    case 141:
                                        return handleMenuRequest(hdmiCecMessage);
                                    case 142:
                                        return handleMenuStatus(hdmiCecMessage);
                                    case 143:
                                        return handleGiveDevicePowerStatus(hdmiCecMessage);
                                    case 144:
                                        return handleReportPowerStatus(hdmiCecMessage);
                                    case HdmiCecKeycode.UI_BROADCAST_DIGITAL_COMMNICATIONS_SATELLITE_2 /* 145 */:
                                        return handleGetMenuLanguage(hdmiCecMessage);
                                    default:
                                        switch (opcode) {
                                            case 157:
                                                return handleInactiveSource(hdmiCecMessage);
                                            case 158:
                                                return handleCecVersion();
                                            case 159:
                                                return handleGetCecVersion(hdmiCecMessage);
                                            case 160:
                                                return handleVendorCommandWithId(hdmiCecMessage);
                                            default:
                                                switch (opcode) {
                                                    case 163:
                                                        return handleReportShortAudioDescriptor(hdmiCecMessage);
                                                    case 164:
                                                        return handleRequestShortAudioDescriptor(hdmiCecMessage);
                                                    case 165:
                                                        return handleGiveFeatures(hdmiCecMessage);
                                                    default:
                                                        switch (opcode) {
                                                            case 192:
                                                                return handleInitiateArc(hdmiCecMessage);
                                                            case HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_STEP_PLUS /* 193 */:
                                                                return handleReportArcInitiate(hdmiCecMessage);
                                                            case HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_NEUTRAL /* 194 */:
                                                                return handleReportArcTermination(hdmiCecMessage);
                                                            case HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_STEP_MINUS /* 195 */:
                                                                return handleRequestArcInitiate(hdmiCecMessage);
                                                            case 196:
                                                                return handleRequestArcTermination(hdmiCecMessage);
                                                            case 197:
                                                                return handleTerminateArc(hdmiCecMessage);
                                                            default:
                                                                return -2;
                                                        }
                                                }
                                        }
                                }
                        }
                }
        }
    }

    @HdmiAnnotations.ServiceThreadOnly
    private boolean dispatchMessageToAction(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        Iterator it = new ArrayList(this.mActions).iterator();
        while (true) {
            boolean z = false;
            while (it.hasNext()) {
                boolean processCommand = ((HdmiCecFeatureAction) it.next()).processCommand(hdmiCecMessage);
                if (z || processCommand) {
                    z = true;
                }
            }
            return z;
        }
    }

    @HdmiAnnotations.ServiceThreadOnly
    protected int handleGivePhysicalAddress(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        int physicalAddress = this.mService.getPhysicalAddress();
        if (physicalAddress == 65535) {
            this.mService.maySendFeatureAbortCommand(hdmiCecMessage, 5);
            return -1;
        }
        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildReportPhysicalAddressCommand(this.mDeviceInfo.getLogicalAddress(), physicalAddress, this.mDeviceType));
        return -1;
    }

    @HdmiAnnotations.ServiceThreadOnly
    protected int handleGiveDeviceVendorId(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        int vendorId = this.mService.getVendorId();
        if (vendorId == 1) {
            this.mService.maySendFeatureAbortCommand(hdmiCecMessage, 5);
            return -1;
        }
        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildDeviceVendorIdCommand(this.mDeviceInfo.getLogicalAddress(), vendorId));
        return -1;
    }

    @HdmiAnnotations.ServiceThreadOnly
    protected int handleGetCecVersion(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildCecVersion(hdmiCecMessage.getDestination(), hdmiCecMessage.getSource(), this.mService.getCecVersion()));
        return -1;
    }

    @HdmiAnnotations.ServiceThreadOnly
    protected int handleCecVersion() {
        assertRunOnServiceThread();
        return -1;
    }

    @HdmiAnnotations.ServiceThreadOnly
    protected int handleGetMenuLanguage(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        Slog.w(TAG, "Only TV can handle <Get Menu Language>:" + hdmiCecMessage.toString());
        return -2;
    }

    @HdmiAnnotations.ServiceThreadOnly
    protected int handleSetMenuLanguage(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        Slog.w(TAG, "Only Playback device can handle <Set Menu Language>:" + hdmiCecMessage.toString());
        return -2;
    }

    @HdmiAnnotations.ServiceThreadOnly
    protected int handleGiveOsdName(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        buildAndSendSetOsdName(hdmiCecMessage.getSource());
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void buildAndSendSetOsdName(int i) {
        final HdmiCecMessage buildSetOsdNameCommand = HdmiCecMessageBuilder.buildSetOsdNameCommand(this.mDeviceInfo.getLogicalAddress(), i, this.mDeviceInfo.getDisplayName());
        if (buildSetOsdNameCommand != null) {
            this.mService.sendCecCommand(buildSetOsdNameCommand, new HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDevice.2
                @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
                public void onSendCompleted(int i2) {
                    if (i2 != 0) {
                        HdmiLogger.debug("Failed to send cec command " + buildSetOsdNameCommand, new Object[0]);
                    }
                }
            });
            return;
        }
        Slog.w(TAG, "Failed to build <Get Osd Name>:" + this.mDeviceInfo.getDisplayName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int handleReportPhysicalAddress(HdmiCecMessage hdmiCecMessage) {
        int source = hdmiCecMessage.getSource();
        if (hasAction(DeviceDiscoveryAction.class)) {
            Slog.i(TAG, "Ignored while Device Discovery Action is in progress: " + hdmiCecMessage);
            return -1;
        }
        HdmiDeviceInfo cecDeviceInfo = this.mService.getHdmiCecNetwork().getCecDeviceInfo(source);
        if (cecDeviceInfo != null && cecDeviceInfo.getDisplayName().equals(HdmiUtils.getDefaultDeviceName(source))) {
            this.mService.sendCecCommand(HdmiCecMessageBuilder.buildGiveOsdNameCommand(this.mDeviceInfo.getLogicalAddress(), source));
        }
        return -1;
    }

    protected DeviceFeatures computeDeviceFeatures() {
        return DeviceFeatures.NO_FEATURES_SUPPORTED;
    }

    private void updateDeviceFeatures() {
        setDeviceInfo(getDeviceInfo().toBuilder().setDeviceFeatures(computeDeviceFeatures()).build());
    }

    protected final DeviceFeatures getDeviceFeatures() {
        updateDeviceFeatures();
        return getDeviceInfo().getDeviceFeatures();
    }

    protected int handleGiveFeatures(HdmiCecMessage hdmiCecMessage) {
        if (this.mService.getCecVersion() < 6) {
            return 0;
        }
        reportFeatures();
        return -1;
    }

    protected void reportFeatures() {
        int logicalAddress;
        ArrayList arrayList = new ArrayList();
        Iterator<HdmiCecLocalDevice> it = this.mService.getAllCecLocalDevices().iterator();
        while (it.hasNext()) {
            arrayList.add(Integer.valueOf(it.next().mDeviceType));
        }
        int rcProfile = getRcProfile();
        List<Integer> rcFeatures = getRcFeatures();
        DeviceFeatures deviceFeatures = getDeviceFeatures();
        synchronized (this.mLock) {
            logicalAddress = this.mDeviceInfo.getLogicalAddress();
        }
        HdmiControlService hdmiControlService = this.mService;
        hdmiControlService.sendCecCommand(ReportFeaturesMessage.build(logicalAddress, hdmiControlService.getCecVersion(), arrayList, rcProfile, rcFeatures, deviceFeatures));
    }

    @HdmiAnnotations.ServiceThreadOnly
    protected int handleStandby(HdmiCecMessage hdmiCecMessage) {
        assertRunOnServiceThread();
        if (!this.mService.isCecControlEnabled() || this.mService.isProhibitMode() || !this.mService.isPowerOnOrTransient()) {
            return 1;
        }
        this.mService.standby();
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0080  */
    @HdmiAnnotations.ServiceThreadOnly
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int handleUserControlPressed(HdmiCecMessage hdmiCecMessage) {
        int i;
        assertRunOnServiceThread();
        this.mHandler.removeMessages(2);
        if (this.mService.isPowerOnOrTransient() && isPowerOffOrToggleCommand(hdmiCecMessage)) {
            this.mService.standby();
            return -1;
        }
        if (this.mService.isPowerStandbyOrTransient() && isPowerOnOrToggleCommand(hdmiCecMessage)) {
            this.mService.wakeUp();
            return -1;
        }
        if (this.mService.getHdmiCecVolumeControl() == 0 && isVolumeOrMuteCommand(hdmiCecMessage)) {
            return 4;
        }
        if (isPowerOffOrToggleCommand(hdmiCecMessage) || isPowerOnOrToggleCommand(hdmiCecMessage)) {
            return -1;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        byte[] params = hdmiCecMessage.getParams();
        int cecKeycodeAndParamsToAndroidKey = HdmiCecKeycode.cecKeycodeAndParamsToAndroidKey(params);
        int i2 = this.mLastKeycode;
        if (i2 != -1) {
            if (cecKeycodeAndParamsToAndroidKey == i2) {
                i = this.mLastKeyRepeatCount + 1;
                this.mLastKeycode = cecKeycodeAndParamsToAndroidKey;
                this.mLastKeyRepeatCount = i;
                if (cecKeycodeAndParamsToAndroidKey == -1) {
                    injectKeyEvent(uptimeMillis, 0, cecKeycodeAndParamsToAndroidKey, i);
                    Handler handler = this.mHandler;
                    handler.sendMessageDelayed(Message.obtain(handler, 2), 550L);
                    return -1;
                }
                if (params.length > 0) {
                    return handleUnmappedCecKeycode(params[0]);
                }
                return 3;
            }
            injectKeyEvent(uptimeMillis, 1, i2, 0);
        }
        i = 0;
        this.mLastKeycode = cecKeycodeAndParamsToAndroidKey;
        this.mLastKeyRepeatCount = i;
        if (cecKeycodeAndParamsToAndroidKey == -1) {
        }
    }

    @HdmiAnnotations.ServiceThreadOnly
    protected int handleUnmappedCecKeycode(int i) {
        if (i == 101) {
            this.mService.getAudioManager().adjustStreamVolume(3, -100, 1);
            return -1;
        }
        if (i != 102) {
            return 3;
        }
        this.mService.getAudioManager().adjustStreamVolume(3, 100, 1);
        return -1;
    }

    @HdmiAnnotations.ServiceThreadOnly
    protected int handleUserControlReleased() {
        assertRunOnServiceThread();
        this.mHandler.removeMessages(2);
        this.mLastKeyRepeatCount = 0;
        if (this.mLastKeycode != -1) {
            injectKeyEvent(SystemClock.uptimeMillis(), 1, this.mLastKeycode, 0);
            this.mLastKeycode = -1;
        }
        return -1;
    }

    static void injectKeyEvent(long j, int i, int i2, int i3) {
        KeyEvent obtain = KeyEvent.obtain(j, j, i, i2, i3, 0, -1, 0, 8, 33554433, null);
        InputManagerGlobal.getInstance().injectInputEvent(obtain, 0);
        obtain.recycle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isPowerOnOrToggleCommand(HdmiCecMessage hdmiCecMessage) {
        byte[] params = hdmiCecMessage.getParams();
        if (hdmiCecMessage.getOpcode() != 68) {
            return false;
        }
        byte b = params[0];
        return b == 64 || b == 109 || b == 107;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isPowerOffOrToggleCommand(HdmiCecMessage hdmiCecMessage) {
        byte[] params = hdmiCecMessage.getParams();
        if (hdmiCecMessage.getOpcode() != 68) {
            return false;
        }
        byte b = params[0];
        return b == 108 || b == 107;
    }

    static boolean isVolumeOrMuteCommand(HdmiCecMessage hdmiCecMessage) {
        byte[] params = hdmiCecMessage.getParams();
        if (hdmiCecMessage.getOpcode() != 68) {
            return false;
        }
        byte b = params[0];
        return b == 66 || b == 65 || b == 67 || b == 101 || b == 102;
    }

    protected int handleGiveDevicePowerStatus(HdmiCecMessage hdmiCecMessage) {
        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildReportPowerStatus(this.mDeviceInfo.getLogicalAddress(), hdmiCecMessage.getSource(), this.mService.getPowerStatus()));
        return -1;
    }

    protected int handleMenuRequest(HdmiCecMessage hdmiCecMessage) {
        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildReportMenuStatus(this.mDeviceInfo.getLogicalAddress(), hdmiCecMessage.getSource(), 0));
        return -1;
    }

    protected int handleVendorCommand(HdmiCecMessage hdmiCecMessage) {
        return !this.mService.invokeVendorCommandListenersOnReceived(this.mDeviceType, hdmiCecMessage.getSource(), hdmiCecMessage.getDestination(), hdmiCecMessage.getParams(), false) ? 4 : -1;
    }

    protected int handleVendorCommandWithId(HdmiCecMessage hdmiCecMessage) {
        byte[] params = hdmiCecMessage.getParams();
        HdmiUtils.threeBytesToInt(params);
        if (hdmiCecMessage.getDestination() != 15 && hdmiCecMessage.getSource() != 15) {
            return !this.mService.invokeVendorCommandListenersOnReceived(this.mDeviceType, hdmiCecMessage.getSource(), hdmiCecMessage.getDestination(), params, true) ? 4 : -1;
        }
        Slog.v(TAG, "Wrong broadcast vendor command. Ignoring");
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public final void handleAddressAllocated(int i, List<HdmiCecMessage> list, int i2) {
        assertRunOnServiceThread();
        preprocessBufferedMessages(list);
        this.mPreferredAddress = i;
        updateDeviceFeatures();
        if (this.mService.getCecVersion() >= 6) {
            reportFeatures();
        }
        onAddressAllocated(i, i2);
        setPreferredAddress(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getType() {
        return this.mDeviceType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiDeviceInfo getDeviceInfo() {
        HdmiDeviceInfo hdmiDeviceInfo;
        synchronized (this.mLock) {
            hdmiDeviceInfo = this.mDeviceInfo;
        }
        return hdmiDeviceInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDeviceInfo(HdmiDeviceInfo hdmiDeviceInfo) {
        synchronized (this.mLock) {
            this.mDeviceInfo = hdmiDeviceInfo;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public boolean isAddressOf(int i) {
        assertRunOnServiceThread();
        return i == this.mDeviceInfo.getLogicalAddress();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void addAndStartAction(HdmiCecFeatureAction hdmiCecFeatureAction) {
        assertRunOnServiceThread();
        this.mActions.add(hdmiCecFeatureAction);
        if (this.mService.isPowerStandby() || !this.mService.isAddressAllocated()) {
            Slog.i(TAG, "Not ready to start action. Queued for deferred start:" + hdmiCecFeatureAction);
            return;
        }
        hdmiCecFeatureAction.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void startNewAvbAudioStatusAction(int i) {
        assertRunOnServiceThread();
        removeAction(AbsoluteVolumeAudioStatusAction.class);
        addAndStartAction(new AbsoluteVolumeAudioStatusAction(this, i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void removeAvbAudioStatusAction() {
        assertRunOnServiceThread();
        removeAction(AbsoluteVolumeAudioStatusAction.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void updateAvbVolume(int i) {
        assertRunOnServiceThread();
        Iterator it = getActions(AbsoluteVolumeAudioStatusAction.class).iterator();
        while (it.hasNext()) {
            ((AbsoluteVolumeAudioStatusAction) it.next()).updateVolume(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void querySetAudioVolumeLevelSupport(final int i) {
        assertRunOnServiceThread();
        if (this.mService.getCecVersion() >= 6) {
            this.mService.sendCecCommand(HdmiCecMessageBuilder.buildGiveFeatures(getDeviceInfo().getLogicalAddress(), i));
        }
        if (getActions(SetAudioVolumeLevelDiscoveryAction.class).stream().noneMatch(new Predicate() { // from class: com.android.server.hdmi.HdmiCecLocalDevice$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$querySetAudioVolumeLevelSupport$0;
                lambda$querySetAudioVolumeLevelSupport$0 = HdmiCecLocalDevice.lambda$querySetAudioVolumeLevelSupport$0(i, (SetAudioVolumeLevelDiscoveryAction) obj);
                return lambda$querySetAudioVolumeLevelSupport$0;
            }
        })) {
            addAndStartAction(new SetAudioVolumeLevelDiscoveryAction(this, i, new IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiCecLocalDevice.3
                public void onComplete(int i2) {
                    if (i2 == 0) {
                        HdmiCecLocalDevice.this.getService().checkAndUpdateAbsoluteVolumeBehavior();
                    }
                }
            }));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$querySetAudioVolumeLevelSupport$0(int i, SetAudioVolumeLevelDiscoveryAction setAudioVolumeLevelDiscoveryAction) {
        return setAudioVolumeLevelDiscoveryAction.getTargetAddress() == i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void startQueuedActions() {
        assertRunOnServiceThread();
        Iterator it = new ArrayList(this.mActions).iterator();
        while (it.hasNext()) {
            HdmiCecFeatureAction hdmiCecFeatureAction = (HdmiCecFeatureAction) it.next();
            if (!hdmiCecFeatureAction.started()) {
                Slog.i(TAG, "Starting queued action:" + hdmiCecFeatureAction);
                hdmiCecFeatureAction.start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public <T extends HdmiCecFeatureAction> boolean hasAction(Class<T> cls) {
        assertRunOnServiceThread();
        Iterator<HdmiCecFeatureAction> it = this.mActions.iterator();
        while (it.hasNext()) {
            if (it.next().getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public <T extends HdmiCecFeatureAction> List<T> getActions(Class<T> cls) {
        assertRunOnServiceThread();
        ArrayList arrayList = (List<T>) Collections.emptyList();
        Iterator<HdmiCecFeatureAction> it = this.mActions.iterator();
        while (it.hasNext()) {
            HdmiCecFeatureAction next = it.next();
            if (next.getClass().equals(cls)) {
                if (arrayList.isEmpty()) {
                    arrayList = new ArrayList();
                }
                arrayList.add(next);
            }
        }
        return (List<T>) arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void removeAction(HdmiCecFeatureAction hdmiCecFeatureAction) {
        assertRunOnServiceThread();
        hdmiCecFeatureAction.finish(false);
        this.mActions.remove(hdmiCecFeatureAction);
        checkIfPendingActionsCleared();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public <T extends HdmiCecFeatureAction> void removeAction(Class<T> cls) {
        assertRunOnServiceThread();
        removeActionExcept(cls, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public <T extends HdmiCecFeatureAction> void removeActionExcept(Class<T> cls, HdmiCecFeatureAction hdmiCecFeatureAction) {
        assertRunOnServiceThread();
        Iterator<HdmiCecFeatureAction> it = this.mActions.iterator();
        while (it.hasNext()) {
            HdmiCecFeatureAction next = it.next();
            if (next != hdmiCecFeatureAction && next.getClass().equals(cls)) {
                next.finish(false);
                it.remove();
            }
        }
        checkIfPendingActionsCleared();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkIfPendingActionsCleared() {
        PendingActionClearedCallback pendingActionClearedCallback;
        if (!this.mActions.isEmpty() || (pendingActionClearedCallback = this.mPendingActionClearedCallback) == null) {
            return;
        }
        this.mPendingActionClearedCallback = null;
        pendingActionClearedCallback.onCleared(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void assertRunOnServiceThread() {
        if (Looper.myLooper() != this.mService.getServiceLooper()) {
            throw new IllegalStateException("Should run on service thread.");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final HdmiControlService getService() {
        return this.mService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public final boolean isConnectedToArcPort(int i) {
        assertRunOnServiceThread();
        return this.mService.isConnectedToArcPort(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActiveSource getActiveSource() {
        return this.mService.getLocalActiveSource();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setActiveSource(ActiveSource activeSource, String str) {
        setActiveSource(activeSource.logicalAddress, activeSource.physicalAddress, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setActiveSource(HdmiDeviceInfo hdmiDeviceInfo, String str) {
        setActiveSource(hdmiDeviceInfo.getLogicalAddress(), hdmiDeviceInfo.getPhysicalAddress(), str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setActiveSource(int i, int i2, String str) {
        this.mService.setActiveSource(i, i2, str);
        this.mService.setLastInputForMhl(-1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getActivePath() {
        int i;
        synchronized (this.mLock) {
            i = this.mActiveRoutingPath;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setActivePath(int i) {
        synchronized (this.mLock) {
            this.mActiveRoutingPath = i;
        }
        this.mService.setActivePortId(pathToPortId(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getActivePortId() {
        int pathToPortId;
        synchronized (this.mLock) {
            pathToPortId = this.mService.pathToPortId(this.mActiveRoutingPath);
        }
        return pathToPortId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setActivePortId(int i) {
        setActivePath(this.mService.portIdToPath(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPortId(int i) {
        return this.mService.pathToPortId(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public HdmiCecMessageCache getCecMessageCache() {
        assertRunOnServiceThread();
        return this.mCecMessageCache;
    }

    @HdmiAnnotations.ServiceThreadOnly
    int pathToPortId(int i) {
        assertRunOnServiceThread();
        return this.mService.pathToPortId(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void disableDevice(boolean z, final PendingActionClearedCallback pendingActionClearedCallback) {
        removeAction(AbsoluteVolumeAudioStatusAction.class);
        removeAction(SetAudioVolumeLevelDiscoveryAction.class);
        removeAction(ActiveSourceAction.class);
        this.mPendingActionClearedCallback = new PendingActionClearedCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDevice.4
            @Override // com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback
            public void onCleared(HdmiCecLocalDevice hdmiCecLocalDevice) {
                HdmiCecLocalDevice.this.mHandler.removeMessages(1);
                pendingActionClearedCallback.onCleared(hdmiCecLocalDevice);
            }
        };
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(Message.obtain(handler, 1), 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @HdmiAnnotations.ServiceThreadOnly
    public void handleDisableDeviceTimeout() {
        assertRunOnServiceThread();
        Iterator<HdmiCecFeatureAction> it = this.mActions.iterator();
        while (it.hasNext()) {
            it.next().finish(false);
            it.remove();
        }
        PendingActionClearedCallback pendingActionClearedCallback = this.mPendingActionClearedCallback;
        if (pendingActionClearedCallback != null) {
            pendingActionClearedCallback.onCleared(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HdmiAnnotations.ServiceThreadOnly
    public void sendKeyEvent(int i, boolean z) {
        assertRunOnServiceThread();
        if (!HdmiCecKeycode.isSupportedKeycode(i)) {
            Slog.w(TAG, "Unsupported key: " + i);
            return;
        }
        List actions = getActions(SendKeyAction.class);
        int findKeyReceiverAddress = findKeyReceiverAddress();
        if (findKeyReceiverAddress == -1 || findKeyReceiverAddress == this.mDeviceInfo.getLogicalAddress()) {
            Slog.w(TAG, "Discard key event: " + i + ", pressed:" + z + ", receiverAddr=" + findKeyReceiverAddress);
            return;
        }
        if (!actions.isEmpty()) {
            ((SendKeyAction) actions.get(0)).processKeyEvent(i, z);
        } else if (z) {
            addAndStartAction(new SendKeyAction(this, findKeyReceiverAddress, i));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @HdmiAnnotations.ServiceThreadOnly
    public void sendVolumeKeyEvent(int i, boolean z) {
        assertRunOnServiceThread();
        if (this.mService.getHdmiCecVolumeControl() == 0) {
            return;
        }
        if (!HdmiCecKeycode.isVolumeKeycode(i)) {
            Slog.w(TAG, "Not a volume key: " + i);
            return;
        }
        List actions = getActions(SendKeyAction.class);
        final int findAudioReceiverAddress = findAudioReceiverAddress();
        if (findAudioReceiverAddress == -1 || this.mService.getAllCecLocalDevices().stream().anyMatch(new Predicate() { // from class: com.android.server.hdmi.HdmiCecLocalDevice$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$sendVolumeKeyEvent$1;
                lambda$sendVolumeKeyEvent$1 = HdmiCecLocalDevice.lambda$sendVolumeKeyEvent$1(findAudioReceiverAddress, (HdmiCecLocalDevice) obj);
                return lambda$sendVolumeKeyEvent$1;
            }
        })) {
            Slog.w(TAG, "Discard volume key event: " + i + ", pressed:" + z + ", receiverAddr=" + findAudioReceiverAddress);
            return;
        }
        if (!actions.isEmpty()) {
            ((SendKeyAction) actions.get(0)).processKeyEvent(i, z);
        } else if (z) {
            addAndStartAction(new SendKeyAction(this, findAudioReceiverAddress, i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$sendVolumeKeyEvent$1(int i, HdmiCecLocalDevice hdmiCecLocalDevice) {
        return hdmiCecLocalDevice.getDeviceInfo().getLogicalAddress() == i;
    }

    protected int findKeyReceiverAddress() {
        Slog.w(TAG, "findKeyReceiverAddress is not implemented");
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int findAudioReceiverAddress() {
        Slog.w(TAG, "findAudioReceiverAddress is not implemented");
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @HdmiAnnotations.ServiceThreadOnly
    public void invokeCallback(IHdmiControlCallback iHdmiControlCallback, int i) {
        assertRunOnServiceThread();
        if (iHdmiControlCallback == null) {
            return;
        }
        try {
            iHdmiControlCallback.onComplete(i);
        } catch (RemoteException e) {
            Slog.e(TAG, "Invoking callback failed:" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendUserControlPressedAndReleased(int i, int i2) {
        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildUserControlPressed(this.mDeviceInfo.getLogicalAddress(), i, i2));
        this.mService.sendCecCommand(HdmiCecMessageBuilder.buildUserControlReleased(this.mDeviceInfo.getLogicalAddress(), i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addActiveSourceHistoryItem(ActiveSource activeSource, boolean z, String str) {
        ActiveSourceHistoryRecord activeSourceHistoryRecord = new ActiveSourceHistoryRecord(activeSource, z, str);
        if (this.mActiveSourceHistory.offer(activeSourceHistoryRecord)) {
            return;
        }
        this.mActiveSourceHistory.poll();
        this.mActiveSourceHistory.offer(activeSourceHistoryRecord);
    }

    public ArrayBlockingQueue<HdmiCecController.Dumpable> getActiveSourceHistory() {
        return this.mActiveSourceHistory;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("mDeviceType: " + this.mDeviceType);
        indentingPrintWriter.println("mPreferredAddress: " + this.mPreferredAddress);
        indentingPrintWriter.println("mDeviceInfo: " + this.mDeviceInfo);
        indentingPrintWriter.println("mActiveSource: " + getActiveSource());
        indentingPrintWriter.println(String.format("mActiveRoutingPath: 0x%04x", Integer.valueOf(this.mActiveRoutingPath)));
    }

    protected int getActivePathOnSwitchFromActivePortId(@Constants.LocalActivePort int i) {
        int physicalAddress = this.mService.getPhysicalAddress();
        int i2 = i << 8;
        for (int i3 = 3840; i3 > 15 && (physicalAddress & i3) != 0; i3 >>= 4) {
            i2 >>= 4;
        }
        return physicalAddress | i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ActiveSourceHistoryRecord extends HdmiCecController.Dumpable {
        private final ActiveSource mActiveSource;
        private final String mCaller;
        private final boolean mIsActiveSource;

        private ActiveSourceHistoryRecord(ActiveSource activeSource, boolean z, String str) {
            this.mActiveSource = activeSource;
            this.mIsActiveSource = z;
            this.mCaller = str;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.android.server.hdmi.HdmiCecController.Dumpable
        public void dump(IndentingPrintWriter indentingPrintWriter, SimpleDateFormat simpleDateFormat) {
            indentingPrintWriter.print("time=");
            indentingPrintWriter.print(simpleDateFormat.format(new Date(this.mTime)));
            indentingPrintWriter.print(" active source=");
            indentingPrintWriter.print(this.mActiveSource);
            indentingPrintWriter.print(" isActiveSource=");
            indentingPrintWriter.print(this.mIsActiveSource);
            indentingPrintWriter.print(" from=");
            indentingPrintWriter.println(this.mCaller);
        }
    }
}
