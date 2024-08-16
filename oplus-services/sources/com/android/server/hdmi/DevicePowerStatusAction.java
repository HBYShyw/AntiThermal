package com.android.server.hdmi;

import android.hardware.hdmi.HdmiDeviceInfo;
import android.hardware.hdmi.IHdmiControlCallback;
import android.util.Slog;
import com.android.server.hdmi.HdmiControlService;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DevicePowerStatusAction extends HdmiCecFeatureAction {
    private static final int STATE_WAITING_FOR_REPORT_POWER_STATUS = 1;
    private static final String TAG = "DevicePowerStatusAction";
    private int mRetriesOnTimeout;
    private final int mTargetAddress;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DevicePowerStatusAction create(HdmiCecLocalDevice hdmiCecLocalDevice, int i, IHdmiControlCallback iHdmiControlCallback) {
        if (hdmiCecLocalDevice == null || iHdmiControlCallback == null) {
            Slog.e(TAG, "Wrong arguments");
            return null;
        }
        return new DevicePowerStatusAction(hdmiCecLocalDevice, i, iHdmiControlCallback);
    }

    private DevicePowerStatusAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i, IHdmiControlCallback iHdmiControlCallback) {
        super(hdmiCecLocalDevice, iHdmiControlCallback);
        this.mRetriesOnTimeout = 1;
        this.mTargetAddress = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        HdmiDeviceInfo cecDeviceInfo;
        int devicePowerStatus;
        HdmiControlService hdmiControlService = localDevice().mService;
        if (hdmiControlService.getCecVersion() >= 6 && (cecDeviceInfo = hdmiControlService.getHdmiCecNetwork().getCecDeviceInfo(this.mTargetAddress)) != null && cecDeviceInfo.getCecVersion() >= 6 && (devicePowerStatus = cecDeviceInfo.getDevicePowerStatus()) != -1) {
            finishWithCallback(devicePowerStatus);
            return true;
        }
        queryDevicePowerStatus();
        this.mState = 1;
        addTimer(1, 2000);
        return true;
    }

    private void queryDevicePowerStatus() {
        sendCommand(HdmiCecMessageBuilder.buildGiveDevicePowerStatus(getSourceAddress(), this.mTargetAddress), new HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.DevicePowerStatusAction$$ExternalSyntheticLambda0
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public final void onSendCompleted(int i) {
                DevicePowerStatusAction.this.lambda$queryDevicePowerStatus$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$queryDevicePowerStatus$0(int i) {
        if (i == 1) {
            finishWithCallback(-1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (this.mState != 1 || this.mTargetAddress != hdmiCecMessage.getSource() || hdmiCecMessage.getOpcode() != 144) {
            return false;
        }
        finishWithCallback(hdmiCecMessage.getParams()[0]);
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
        if (this.mState == i && i == 1) {
            int i2 = this.mRetriesOnTimeout;
            if (i2 > 0) {
                this.mRetriesOnTimeout = i2 - 1;
                start();
            } else {
                finishWithCallback(-1);
            }
        }
    }
}
