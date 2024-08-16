package com.android.server.hdmi;

import android.hardware.hdmi.IHdmiControlCallback;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SystemAudioActionFromAvr extends SystemAudioAction {
    /* JADX INFO: Access modifiers changed from: package-private */
    public SystemAudioActionFromAvr(HdmiCecLocalDevice hdmiCecLocalDevice, int i, boolean z, IHdmiControlCallback iHdmiControlCallback) {
        super(hdmiCecLocalDevice, i, z, iHdmiControlCallback);
        HdmiUtils.verifyAddressType(getSourceAddress(), 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        removeSystemAudioActionInProgress();
        handleSystemAudioActionFromAvr();
        return true;
    }

    private void handleSystemAudioActionFromAvr() {
        if (this.mTargetAudioStatus == tv().isSystemAudioActivated()) {
            finishWithCallback(0);
            return;
        }
        if (tv().isProhibitMode()) {
            sendCommand(HdmiCecMessageBuilder.buildFeatureAbortCommand(getSourceAddress(), this.mAvrLogicalAddress, HdmiCecKeycode.CEC_KEYCODE_F2_RED, 4));
            this.mTargetAudioStatus = false;
            sendSystemAudioModeRequest();
            return;
        }
        removeAction(SystemAudioAutoInitiationAction.class);
        if (this.mTargetAudioStatus) {
            setSystemAudioMode(true);
            finish();
        } else {
            setSystemAudioMode(false);
            finishWithCallback(0);
        }
    }
}
