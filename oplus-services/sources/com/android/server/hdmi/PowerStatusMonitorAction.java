package com.android.server.hdmi;

import android.hardware.hdmi.HdmiDeviceInfo;
import android.hardware.hdmi.IHdmiControlCallback;
import android.util.SparseIntArray;
import com.android.server.hdmi.HdmiControlService;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PowerStatusMonitorAction extends HdmiCecFeatureAction {
    private static final int INVALID_POWER_STATUS = -2;
    private static final int MONITORING_INTERVAL_MS = 60000;
    private static final int REPORT_POWER_STATUS_TIMEOUT_MS = 5000;
    private static final int STATE_WAIT_FOR_NEXT_MONITORING = 2;
    private static final int STATE_WAIT_FOR_REPORT_POWER_STATUS = 1;
    private static final String TAG = "PowerStatusMonitorAction";
    private final SparseIntArray mPowerStatus;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PowerStatusMonitorAction(HdmiCecLocalDevice hdmiCecLocalDevice) {
        super(hdmiCecLocalDevice);
        this.mPowerStatus = new SparseIntArray();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        queryPowerStatus();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (this.mState == 1 && hdmiCecMessage.getOpcode() == 144) {
            return handleReportPowerStatus(hdmiCecMessage);
        }
        return false;
    }

    private boolean handleReportPowerStatus(HdmiCecMessage hdmiCecMessage) {
        int source = hdmiCecMessage.getSource();
        if (this.mPowerStatus.get(source, -2) == -2) {
            return false;
        }
        updatePowerStatus(source, hdmiCecMessage.getParams()[0] & 255, true);
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
        int i2 = this.mState;
        if (i2 == 1) {
            handleTimeout();
        } else {
            if (i2 != 2) {
                return;
            }
            queryPowerStatus();
        }
    }

    private void handleTimeout() {
        for (int i = 0; i < this.mPowerStatus.size(); i++) {
            updatePowerStatus(this.mPowerStatus.keyAt(i), -1, false);
        }
        this.mPowerStatus.clear();
        this.mState = 2;
    }

    private void resetPowerStatus(List<HdmiDeviceInfo> list) {
        this.mPowerStatus.clear();
        for (HdmiDeviceInfo hdmiDeviceInfo : list) {
            if (localDevice().mService.getCecVersion() < 6 || hdmiDeviceInfo.getCecVersion() < 6) {
                this.mPowerStatus.append(hdmiDeviceInfo.getLogicalAddress(), hdmiDeviceInfo.getDevicePowerStatus());
            }
        }
    }

    private void queryPowerStatus() {
        List<HdmiDeviceInfo> deviceInfoList = localDevice().mService.getHdmiCecNetwork().getDeviceInfoList(false);
        resetPowerStatus(deviceInfoList);
        for (HdmiDeviceInfo hdmiDeviceInfo : deviceInfoList) {
            if (localDevice().mService.getCecVersion() < 6 || hdmiDeviceInfo.getCecVersion() < 6) {
                final int logicalAddress = hdmiDeviceInfo.getLogicalAddress();
                sendCommand(HdmiCecMessageBuilder.buildGiveDevicePowerStatus(getSourceAddress(), logicalAddress), new HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.PowerStatusMonitorAction.1
                    @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
                    public void onSendCompleted(int i) {
                        if (i != 0) {
                            PowerStatusMonitorAction.this.updatePowerStatus(logicalAddress, -1, true);
                        }
                    }
                });
            }
        }
        this.mState = 1;
        addTimer(2, 60000);
        addTimer(1, 5000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePowerStatus(int i, int i2, boolean z) {
        localDevice().mService.getHdmiCecNetwork().updateDevicePowerStatus(i, i2);
        if (z) {
            this.mPowerStatus.delete(i);
        }
    }
}
