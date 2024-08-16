package com.android.server.hdmi;

import android.hardware.hdmi.DeviceFeatures;
import android.hardware.hdmi.HdmiDeviceInfo;
import android.hardware.hdmi.IHdmiControlCallback;
import com.android.server.hdmi.HdmiControlService;
import com.android.server.power.stats.BatteryStatsImpl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SetAudioVolumeLevelDiscoveryAction extends HdmiCecFeatureAction {
    private static final int STATE_WAITING_FOR_FEATURE_ABORT = 1;
    private static final String TAG = "SetAudioVolumeLevelDiscoveryAction";
    private final int mTargetAddress;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    public SetAudioVolumeLevelDiscoveryAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i, IHdmiControlCallback iHdmiControlCallback) {
        super(hdmiCecLocalDevice, iHdmiControlCallback);
        this.mTargetAddress = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        sendCommand(SetAudioVolumeLevelMessage.build(getSourceAddress(), this.mTargetAddress, BatteryStatsImpl.ExternalStatsSync.UPDATE_ALL), new HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.SetAudioVolumeLevelDiscoveryAction$$ExternalSyntheticLambda0
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public final void onSendCompleted(int i) {
                SetAudioVolumeLevelDiscoveryAction.this.lambda$start$0(i);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$0(int i) {
        if (i == 0) {
            this.mState = 1;
            addTimer(1, 2000);
        } else {
            finishWithCallback(7);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (this.mState == 1 && hdmiCecMessage.getOpcode() == 0) {
            return handleFeatureAbort(hdmiCecMessage);
        }
        return false;
    }

    private boolean handleFeatureAbort(HdmiCecMessage hdmiCecMessage) {
        if (hdmiCecMessage.getParams().length < 2 || (hdmiCecMessage.getParams()[0] & 255) != 115 || hdmiCecMessage.getSource() != this.mTargetAddress) {
            return false;
        }
        finishWithCallback(0);
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
        if (updateSetAudioVolumeLevelSupport(1)) {
            finishWithCallback(0);
        } else {
            finishWithCallback(5);
        }
    }

    private boolean updateSetAudioVolumeLevelSupport(@DeviceFeatures.FeatureSupportStatus int i) {
        HdmiCecNetwork hdmiCecNetwork = localDevice().mService.getHdmiCecNetwork();
        HdmiDeviceInfo cecDeviceInfo = hdmiCecNetwork.getCecDeviceInfo(this.mTargetAddress);
        if (cecDeviceInfo == null) {
            return false;
        }
        hdmiCecNetwork.updateCecDevice(cecDeviceInfo.toBuilder().setDeviceFeatures(cecDeviceInfo.getDeviceFeatures().toBuilder().setSetAudioVolumeLevelSupport(i).build()).build());
        return true;
    }

    public int getTargetAddress() {
        return this.mTargetAddress;
    }
}
