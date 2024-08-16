package com.android.server.hdmi;

import android.hardware.hdmi.HdmiDeviceInfo;
import android.util.Slog;
import com.android.internal.util.Preconditions;
import com.android.server.hdmi.HdmiControlService;
import com.android.server.location.gnss.hal.GnssNative;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class DeviceDiscoveryAction extends HdmiCecFeatureAction {
    private static final int STATE_WAITING_FOR_DEVICES = 5;
    private static final int STATE_WAITING_FOR_DEVICE_POLLING = 1;
    private static final int STATE_WAITING_FOR_OSD_NAME = 3;
    private static final int STATE_WAITING_FOR_PHYSICAL_ADDRESS = 2;
    private static final int STATE_WAITING_FOR_POWER = 6;
    private static final int STATE_WAITING_FOR_VENDOR_ID = 4;
    private static final String TAG = "DeviceDiscoveryAction";
    private final DeviceDiscoveryCallback mCallback;
    private final int mDelayPeriod;
    private final ArrayList<DeviceInfo> mDevices;
    private boolean mIsTvDevice;
    private int mProcessedDeviceCount;
    private int mTimeoutRetry;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface DeviceDiscoveryCallback {
        void onDeviceDiscoveryDone(List<HdmiDeviceInfo> list);
    }

    private boolean verifyValidLogicalAddress(int i) {
        return i >= 0 && i < 15;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class DeviceInfo {
        private int mDeviceType;
        private String mDisplayName;
        private final int mLogicalAddress;
        private int mPhysicalAddress;
        private int mPortId;
        private int mPowerStatus;
        private int mVendorId;

        private DeviceInfo(int i) {
            this.mPhysicalAddress = GnssNative.GNSS_AIDING_TYPE_ALL;
            this.mPortId = -1;
            this.mVendorId = 16777215;
            this.mPowerStatus = -1;
            this.mDisplayName = "";
            this.mDeviceType = -1;
            this.mLogicalAddress = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public HdmiDeviceInfo toHdmiDeviceInfo() {
            return HdmiDeviceInfo.cecDeviceBuilder().setLogicalAddress(this.mLogicalAddress).setPhysicalAddress(this.mPhysicalAddress).setPortId(this.mPortId).setVendorId(this.mVendorId).setDeviceType(this.mDeviceType).setDisplayName(this.mDisplayName).setDevicePowerStatus(this.mPowerStatus).build();
        }
    }

    DeviceDiscoveryAction(HdmiCecLocalDevice hdmiCecLocalDevice, DeviceDiscoveryCallback deviceDiscoveryCallback, int i) {
        super(hdmiCecLocalDevice);
        this.mDevices = new ArrayList<>();
        this.mProcessedDeviceCount = 0;
        this.mTimeoutRetry = 0;
        this.mIsTvDevice = localDevice().mService.isTvDevice();
        Objects.requireNonNull(deviceDiscoveryCallback);
        this.mCallback = deviceDiscoveryCallback;
        this.mDelayPeriod = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeviceDiscoveryAction(HdmiCecLocalDevice hdmiCecLocalDevice, DeviceDiscoveryCallback deviceDiscoveryCallback) {
        this(hdmiCecLocalDevice, deviceDiscoveryCallback, 0);
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        this.mDevices.clear();
        this.mState = 1;
        pollDevices(new HdmiControlService.DevicePollingCallback() { // from class: com.android.server.hdmi.DeviceDiscoveryAction.1
            @Override // com.android.server.hdmi.HdmiControlService.DevicePollingCallback
            public void onPollingFinished(List<Integer> list) {
                if (list.isEmpty()) {
                    Slog.v(DeviceDiscoveryAction.TAG, "No device is detected.");
                    DeviceDiscoveryAction.this.wrapUpAndFinish();
                    return;
                }
                Slog.v(DeviceDiscoveryAction.TAG, "Device detected: " + list);
                DeviceDiscoveryAction.this.allocateDevices(list);
                if (DeviceDiscoveryAction.this.mDelayPeriod > 0) {
                    DeviceDiscoveryAction.this.startToDelayAction();
                } else {
                    DeviceDiscoveryAction.this.startPhysicalAddressStage();
                }
            }
        }, 131073, 1);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void allocateDevices(List<Integer> list) {
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            this.mDevices.add(new DeviceInfo(it.next().intValue()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startToDelayAction() {
        Slog.v(TAG, "Waiting for connected devices to be ready");
        this.mState = 5;
        checkAndProceedStage();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startPhysicalAddressStage() {
        Slog.v(TAG, "Start [Physical Address Stage]:" + this.mDevices.size());
        this.mProcessedDeviceCount = 0;
        this.mState = 2;
        checkAndProceedStage();
    }

    private void queryPhysicalAddress(int i) {
        if (!verifyValidLogicalAddress(i)) {
            checkAndProceedStage();
            return;
        }
        this.mActionTimer.clearTimerMessage();
        if (mayProcessMessageIfCached(i, 132)) {
            return;
        }
        sendCommand(HdmiCecMessageBuilder.buildGivePhysicalAddress(getSourceAddress(), i));
        addTimer(this.mState, 2000);
    }

    private void delayActionWithTimePeriod(int i) {
        this.mActionTimer.clearTimerMessage();
        addTimer(this.mState, i);
    }

    private void startOsdNameStage() {
        Slog.v(TAG, "Start [Osd Name Stage]:" + this.mDevices.size());
        this.mProcessedDeviceCount = 0;
        this.mState = 3;
        checkAndProceedStage();
    }

    private void queryOsdName(int i) {
        if (!verifyValidLogicalAddress(i)) {
            checkAndProceedStage();
            return;
        }
        this.mActionTimer.clearTimerMessage();
        if (mayProcessMessageIfCached(i, 71)) {
            return;
        }
        sendCommand(HdmiCecMessageBuilder.buildGiveOsdNameCommand(getSourceAddress(), i));
        addTimer(this.mState, 2000);
    }

    private void startVendorIdStage() {
        Slog.v(TAG, "Start [Vendor Id Stage]:" + this.mDevices.size());
        this.mProcessedDeviceCount = 0;
        this.mState = 4;
        checkAndProceedStage();
    }

    private void queryVendorId(int i) {
        if (!verifyValidLogicalAddress(i)) {
            checkAndProceedStage();
            return;
        }
        this.mActionTimer.clearTimerMessage();
        if (mayProcessMessageIfCached(i, 135)) {
            return;
        }
        sendCommand(HdmiCecMessageBuilder.buildGiveDeviceVendorIdCommand(getSourceAddress(), i));
        addTimer(this.mState, 2000);
    }

    private void startPowerStatusStage() {
        Slog.v(TAG, "Start [Power Status Stage]:" + this.mDevices.size());
        this.mProcessedDeviceCount = 0;
        this.mState = 6;
        checkAndProceedStage();
    }

    private void queryPowerStatus(int i) {
        if (!verifyValidLogicalAddress(i)) {
            checkAndProceedStage();
            return;
        }
        this.mActionTimer.clearTimerMessage();
        if (mayProcessMessageIfCached(i, 144)) {
            return;
        }
        sendCommand(HdmiCecMessageBuilder.buildGiveDevicePowerStatus(getSourceAddress(), i));
        addTimer(this.mState, 2000);
    }

    private boolean mayProcessMessageIfCached(int i, int i2) {
        HdmiCecMessage message = getCecMessageCache().getMessage(i, i2);
        if (message == null) {
            return false;
        }
        processCommand(message);
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        int i = this.mState;
        if (i == 2) {
            if (hdmiCecMessage.getOpcode() != 132) {
                return false;
            }
            handleReportPhysicalAddress(hdmiCecMessage);
            return true;
        }
        if (i == 3) {
            if (hdmiCecMessage.getOpcode() == 71) {
                handleSetOsdName(hdmiCecMessage);
                return true;
            }
            if (hdmiCecMessage.getOpcode() != 0 || (hdmiCecMessage.getParams()[0] & 255) != 70) {
                return false;
            }
            handleSetOsdName(hdmiCecMessage);
            return true;
        }
        if (i == 4) {
            if (hdmiCecMessage.getOpcode() == 135) {
                handleVendorId(hdmiCecMessage);
                return true;
            }
            if (hdmiCecMessage.getOpcode() != 0 || (hdmiCecMessage.getParams()[0] & 255) != 140) {
                return false;
            }
            handleVendorId(hdmiCecMessage);
            return true;
        }
        if (i != 6) {
            return false;
        }
        if (hdmiCecMessage.getOpcode() == 144) {
            handleReportPowerStatus(hdmiCecMessage);
            return true;
        }
        if (hdmiCecMessage.getOpcode() != 0 || (hdmiCecMessage.getParams()[0] & 255) != 144) {
            return false;
        }
        handleReportPowerStatus(hdmiCecMessage);
        return true;
    }

    private void handleReportPhysicalAddress(HdmiCecMessage hdmiCecMessage) {
        Preconditions.checkState(this.mProcessedDeviceCount < this.mDevices.size());
        DeviceInfo deviceInfo = this.mDevices.get(this.mProcessedDeviceCount);
        if (deviceInfo.mLogicalAddress != hdmiCecMessage.getSource()) {
            Slog.w(TAG, "Unmatched address[expected:" + deviceInfo.mLogicalAddress + ", actual:" + hdmiCecMessage.getSource());
            return;
        }
        byte[] params = hdmiCecMessage.getParams();
        deviceInfo.mPhysicalAddress = HdmiUtils.twoBytesToInt(params);
        deviceInfo.mPortId = getPortId(deviceInfo.mPhysicalAddress);
        deviceInfo.mDeviceType = params[2] & 255;
        deviceInfo.mDisplayName = "";
        if (this.mIsTvDevice) {
            localDevice().mService.getHdmiCecNetwork().updateCecSwitchInfo(deviceInfo.mLogicalAddress, deviceInfo.mDeviceType, deviceInfo.mPhysicalAddress);
        }
        increaseProcessedDeviceCount();
        checkAndProceedStage();
    }

    private int getPortId(int i) {
        return this.mIsTvDevice ? tv().getPortId(i) : source().getPortId(i);
    }

    private void handleSetOsdName(HdmiCecMessage hdmiCecMessage) {
        Preconditions.checkState(this.mProcessedDeviceCount < this.mDevices.size());
        DeviceInfo deviceInfo = this.mDevices.get(this.mProcessedDeviceCount);
        if (deviceInfo.mLogicalAddress != hdmiCecMessage.getSource()) {
            Slog.w(TAG, "Unmatched address[expected:" + deviceInfo.mLogicalAddress + ", actual:" + hdmiCecMessage.getSource());
            return;
        }
        String str = "";
        try {
            if (hdmiCecMessage.getOpcode() != 0) {
                str = new String(hdmiCecMessage.getParams(), "US-ASCII");
            }
        } catch (UnsupportedEncodingException unused) {
            Slog.w(TAG, "Failed to decode display name: " + hdmiCecMessage.toString());
        }
        deviceInfo.mDisplayName = str;
        increaseProcessedDeviceCount();
        checkAndProceedStage();
    }

    private void handleVendorId(HdmiCecMessage hdmiCecMessage) {
        Preconditions.checkState(this.mProcessedDeviceCount < this.mDevices.size());
        DeviceInfo deviceInfo = this.mDevices.get(this.mProcessedDeviceCount);
        if (deviceInfo.mLogicalAddress != hdmiCecMessage.getSource()) {
            Slog.w(TAG, "Unmatched address[expected:" + deviceInfo.mLogicalAddress + ", actual:" + hdmiCecMessage.getSource());
            return;
        }
        if (hdmiCecMessage.getOpcode() != 0) {
            deviceInfo.mVendorId = HdmiUtils.threeBytesToInt(hdmiCecMessage.getParams());
        }
        increaseProcessedDeviceCount();
        checkAndProceedStage();
    }

    private void handleReportPowerStatus(HdmiCecMessage hdmiCecMessage) {
        Preconditions.checkState(this.mProcessedDeviceCount < this.mDevices.size());
        DeviceInfo deviceInfo = this.mDevices.get(this.mProcessedDeviceCount);
        if (deviceInfo.mLogicalAddress != hdmiCecMessage.getSource()) {
            Slog.w(TAG, "Unmatched address[expected:" + deviceInfo.mLogicalAddress + ", actual:" + hdmiCecMessage.getSource());
            return;
        }
        if (hdmiCecMessage.getOpcode() != 0) {
            deviceInfo.mPowerStatus = hdmiCecMessage.getParams()[0] & 255;
        }
        increaseProcessedDeviceCount();
        checkAndProceedStage();
    }

    private void increaseProcessedDeviceCount() {
        this.mProcessedDeviceCount++;
        this.mTimeoutRetry = 0;
    }

    private void removeDevice(int i) {
        this.mDevices.remove(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wrapUpAndFinish() {
        Slog.v(TAG, "---------Wrap up Device Discovery:[" + this.mDevices.size() + "]---------");
        ArrayList arrayList = new ArrayList();
        Iterator<DeviceInfo> it = this.mDevices.iterator();
        while (it.hasNext()) {
            HdmiDeviceInfo hdmiDeviceInfo = it.next().toHdmiDeviceInfo();
            Slog.v(TAG, " DeviceInfo: " + hdmiDeviceInfo);
            arrayList.add(hdmiDeviceInfo);
        }
        Slog.v(TAG, "--------------------------------------------");
        this.mCallback.onDeviceDiscoveryDone(arrayList);
        finish();
        if (this.mIsTvDevice) {
            tv().processAllDelayedMessages();
        }
    }

    private void checkAndProceedStage() {
        if (this.mDevices.isEmpty()) {
            wrapUpAndFinish();
            return;
        }
        if (this.mProcessedDeviceCount == this.mDevices.size()) {
            this.mProcessedDeviceCount = 0;
            int i = this.mState;
            if (i == 2) {
                startOsdNameStage();
                return;
            }
            if (i == 3) {
                startVendorIdStage();
                return;
            } else if (i == 4) {
                startPowerStatusStage();
                return;
            } else {
                if (i != 6) {
                    return;
                }
                wrapUpAndFinish();
                return;
            }
        }
        sendQueryCommand();
    }

    private void sendQueryCommand() {
        int i = this.mDevices.get(this.mProcessedDeviceCount).mLogicalAddress;
        int i2 = this.mState;
        if (i2 == 2) {
            queryPhysicalAddress(i);
            return;
        }
        if (i2 == 3) {
            queryOsdName(i);
            return;
        }
        if (i2 == 4) {
            queryVendorId(i);
        } else if (i2 == 5) {
            delayActionWithTimePeriod(this.mDelayPeriod);
        } else {
            if (i2 != 6) {
                return;
            }
            queryPowerStatus(i);
        }
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
        int i2 = this.mState;
        if (i2 == 0 || i2 != i) {
            return;
        }
        if (i2 == 5) {
            startPhysicalAddressStage();
            return;
        }
        int i3 = this.mTimeoutRetry + 1;
        this.mTimeoutRetry = i3;
        if (i3 < 5) {
            sendQueryCommand();
            return;
        }
        this.mTimeoutRetry = 0;
        Slog.v(TAG, "Timeout[State=" + this.mState + ", Processed=" + this.mProcessedDeviceCount);
        int i4 = this.mState;
        if (i4 != 6 && i4 != 3) {
            removeDevice(this.mProcessedDeviceCount);
        } else {
            increaseProcessedDeviceCount();
        }
        checkAndProceedStage();
    }
}
