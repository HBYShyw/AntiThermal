package com.android.server.hdmi;

import android.hardware.hdmi.HdmiDeviceInfo;
import android.hardware.hdmi.IHdmiControlCallback;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.hdmi.HdmiControlService;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DeviceSelectActionFromTv extends HdmiCecFeatureAction {
    private static final int LOOP_COUNTER_MAX = 20;

    @VisibleForTesting
    static final int STATE_WAIT_FOR_DEVICE_POWER_ON = 3;
    private static final int STATE_WAIT_FOR_DEVICE_TO_TRANSIT_TO_STANDBY = 2;

    @VisibleForTesting
    static final int STATE_WAIT_FOR_REPORT_POWER_STATUS = 1;
    private static final String TAG = "DeviceSelect";
    private static final int TIMEOUT_POWER_ON_MS = 5000;
    private static final int TIMEOUT_TRANSIT_TO_STANDBY_MS = 5000;
    private final HdmiCecMessage mGivePowerStatus;
    private final boolean mIsCec20;
    private int mPowerStatusCounter;
    private final HdmiDeviceInfo mTarget;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeviceSelectActionFromTv(HdmiCecLocalDeviceTv hdmiCecLocalDeviceTv, HdmiDeviceInfo hdmiDeviceInfo, IHdmiControlCallback iHdmiControlCallback) {
        this(hdmiCecLocalDeviceTv, hdmiDeviceInfo, iHdmiControlCallback, hdmiCecLocalDeviceTv.getDeviceInfo().getCecVersion() >= 6 && hdmiDeviceInfo.getCecVersion() >= 6);
    }

    @VisibleForTesting
    DeviceSelectActionFromTv(HdmiCecLocalDeviceTv hdmiCecLocalDeviceTv, HdmiDeviceInfo hdmiDeviceInfo, IHdmiControlCallback iHdmiControlCallback, boolean z) {
        super(hdmiCecLocalDeviceTv, iHdmiControlCallback);
        this.mPowerStatusCounter = 0;
        this.mTarget = hdmiDeviceInfo;
        this.mGivePowerStatus = HdmiCecMessageBuilder.buildGiveDevicePowerStatus(getSourceAddress(), getTargetAddress());
        this.mIsCec20 = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getTargetAddress() {
        return this.mTarget.getLogicalAddress();
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        sendSetStreamPath();
        if (!this.mIsCec20) {
            queryDevicePowerStatus();
        } else {
            HdmiDeviceInfo cecDeviceInfo = localDevice().mService.getHdmiCecNetwork().getCecDeviceInfo(getTargetAddress());
            int devicePowerStatus = cecDeviceInfo != null ? cecDeviceInfo.getDevicePowerStatus() : -1;
            if (devicePowerStatus == -1) {
                queryDevicePowerStatus();
            } else if (devicePowerStatus == 0) {
                finishWithCallback(0);
                return true;
            }
        }
        this.mState = 1;
        addTimer(1, 2000);
        return true;
    }

    private void queryDevicePowerStatus() {
        sendCommand(this.mGivePowerStatus, new HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.DeviceSelectActionFromTv.1
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int i) {
                if (i != 0) {
                    DeviceSelectActionFromTv.this.finishWithCallback(7);
                }
            }
        });
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (hdmiCecMessage.getSource() != getTargetAddress()) {
            return false;
        }
        int opcode = hdmiCecMessage.getOpcode();
        byte[] params = hdmiCecMessage.getParams();
        if (this.mState == 1 && opcode == 144) {
            return handleReportPowerStatus(params[0]);
        }
        return false;
    }

    private boolean handleReportPowerStatus(int i) {
        if (i == 0) {
            selectDevice();
            return true;
        }
        if (i == 1) {
            if (this.mPowerStatusCounter == 0) {
                turnOnDevice();
                this.mState = 3;
                addTimer(3, HotplugDetectionAction.POLLING_INTERVAL_MS_FOR_TV);
            } else {
                selectDevice();
            }
            return true;
        }
        if (i == 2) {
            if (this.mPowerStatusCounter < 20) {
                this.mState = 3;
                addTimer(3, HotplugDetectionAction.POLLING_INTERVAL_MS_FOR_TV);
            } else {
                selectDevice();
            }
            return true;
        }
        if (i != 3) {
            return false;
        }
        if (this.mPowerStatusCounter < 4) {
            this.mState = 2;
            addTimer(2, HotplugDetectionAction.POLLING_INTERVAL_MS_FOR_TV);
        } else {
            selectDevice();
        }
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public void handleTimerEvent(int i) {
        int i2 = this.mState;
        if (i2 != i) {
            Slog.w(TAG, "Timer in a wrong state. Ignored.");
            return;
        }
        if (i2 == 1) {
            if (tv().isPowerStandbyOrTransient()) {
                finishWithCallback(6);
                return;
            } else {
                selectDevice();
                return;
            }
        }
        if (i2 == 2 || i2 == 3) {
            this.mPowerStatusCounter++;
            queryDevicePowerStatus();
            this.mState = 1;
            addTimer(1, 2000);
        }
    }

    private void turnOnDevice() {
        if (this.mIsCec20) {
            return;
        }
        sendUserControlPressedAndReleased(this.mTarget.getLogicalAddress(), 64);
        sendUserControlPressedAndReleased(this.mTarget.getLogicalAddress(), HdmiCecKeycode.CEC_KEYCODE_POWER_ON_FUNCTION);
    }

    private void selectDevice() {
        if (!this.mIsCec20) {
            sendSetStreamPath();
        }
        finishWithCallback(0);
    }

    private void sendSetStreamPath() {
        tv().getActiveSource().invalidate();
        tv().setActivePath(this.mTarget.getPhysicalAddress());
        sendCommand(HdmiCecMessageBuilder.buildSetStreamPath(getSourceAddress(), this.mTarget.getPhysicalAddress()));
    }
}
