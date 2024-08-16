package com.android.server.hdmi;

import android.hardware.hdmi.HdmiDeviceInfo;
import android.hardware.hdmi.IHdmiControlCallback;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class OneTouchPlayAction extends HdmiCecFeatureAction {
    private static final int LOOP_COUNTER_MAX = 10;

    @VisibleForTesting
    static final int STATE_WAITING_FOR_REPORT_POWER_STATUS = 1;
    private static final String TAG = "OneTouchPlayAction";
    private final boolean mIsCec20;
    private int mPowerStatusCounter;
    private HdmiCecLocalDeviceSource mSource;
    private final int mTargetAddress;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OneTouchPlayAction create(HdmiCecLocalDeviceSource hdmiCecLocalDeviceSource, int i, IHdmiControlCallback iHdmiControlCallback) {
        if (hdmiCecLocalDeviceSource == null || iHdmiControlCallback == null) {
            Slog.e(TAG, "Wrong arguments");
            return null;
        }
        return new OneTouchPlayAction(hdmiCecLocalDeviceSource, i, iHdmiControlCallback);
    }

    private OneTouchPlayAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i, IHdmiControlCallback iHdmiControlCallback) {
        this(hdmiCecLocalDevice, i, iHdmiControlCallback, hdmiCecLocalDevice.getDeviceInfo().getCecVersion() >= 6 && getTargetCecVersion(hdmiCecLocalDevice, i) >= 6);
    }

    @VisibleForTesting
    OneTouchPlayAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i, IHdmiControlCallback iHdmiControlCallback, boolean z) {
        super(hdmiCecLocalDevice, iHdmiControlCallback);
        this.mPowerStatusCounter = 0;
        this.mTargetAddress = i;
        this.mIsCec20 = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        this.mSource = source();
        sendCommand(HdmiCecMessageBuilder.buildTextViewOn(getSourceAddress(), this.mTargetAddress));
        boolean z = this.mIsCec20 && getTargetDevicePowerStatus(this.mSource, this.mTargetAddress, -1) == 0;
        setAndBroadcastActiveSource();
        if (shouldTurnOnConnectedAudioSystem()) {
            sendCommand(HdmiCecMessageBuilder.buildSystemAudioModeRequest(getSourceAddress(), 5, getSourcePath(), true));
        }
        if (!this.mIsCec20) {
            queryDevicePowerStatus();
        } else {
            int targetDevicePowerStatus = getTargetDevicePowerStatus(this.mSource, this.mTargetAddress, -1);
            if (targetDevicePowerStatus == -1) {
                queryDevicePowerStatus();
            } else if (targetDevicePowerStatus == 0) {
                if (!z) {
                    maySendActiveSource();
                }
                finishWithCallback(0);
                return true;
            }
        }
        this.mState = 1;
        addTimer(1, 2000);
        return true;
    }

    private void setAndBroadcastActiveSource() {
        this.mSource.mService.setAndBroadcastActiveSourceFromOneDeviceType(this.mTargetAddress, getSourcePath(), "OneTouchPlayAction#broadcastActiveSource()");
        if (this.mSource.mService.audioSystem() != null) {
            this.mSource = this.mSource.mService.audioSystem();
        }
        this.mSource.setRoutingPort(0);
        this.mSource.setLocalActivePort(0);
    }

    private void maySendActiveSource() {
        this.mSource.maySendActiveSource(this.mTargetAddress);
    }

    private void queryDevicePowerStatus() {
        sendCommand(HdmiCecMessageBuilder.buildGiveDevicePowerStatus(getSourceAddress(), this.mTargetAddress));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (this.mState != 1 || this.mTargetAddress != hdmiCecMessage.getSource() || hdmiCecMessage.getOpcode() != 144) {
            return false;
        }
        if (hdmiCecMessage.getParams()[0] == 0) {
            maySendActiveSource();
            finishWithCallback(0);
        }
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
        if (this.mState == i && i == 1) {
            int i2 = this.mPowerStatusCounter;
            this.mPowerStatusCounter = i2 + 1;
            if (i2 < 10) {
                queryDevicePowerStatus();
                addTimer(this.mState, 2000);
            } else {
                finishWithCallback(1);
            }
        }
    }

    private boolean shouldTurnOnConnectedAudioSystem() {
        HdmiControlService hdmiControlService = this.mSource.mService;
        if (hdmiControlService.isAudioSystemDevice()) {
            return false;
        }
        String stringValue = hdmiControlService.getHdmiCecConfig().getStringValue("power_control_mode");
        return stringValue.equals("to_tv_and_audio_system") || stringValue.equals("broadcast");
    }

    private static int getTargetCecVersion(HdmiCecLocalDevice hdmiCecLocalDevice, int i) {
        HdmiDeviceInfo cecDeviceInfo = hdmiCecLocalDevice.mService.getHdmiCecNetwork().getCecDeviceInfo(i);
        if (cecDeviceInfo != null) {
            return cecDeviceInfo.getCecVersion();
        }
        return 5;
    }

    private static int getTargetDevicePowerStatus(HdmiCecLocalDevice hdmiCecLocalDevice, int i, int i2) {
        HdmiDeviceInfo cecDeviceInfo = hdmiCecLocalDevice.mService.getHdmiCecNetwork().getCecDeviceInfo(i);
        return cecDeviceInfo != null ? cecDeviceInfo.getDevicePowerStatus() : i2;
    }
}
