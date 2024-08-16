package com.android.server.hdmi;

import com.android.internal.annotations.VisibleForTesting;
import com.android.server.hdmi.HdmiControlService;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SystemAudioAutoInitiationAction extends HdmiCecFeatureAction {

    @VisibleForTesting
    static final int RETRIES_ON_TIMEOUT = 1;
    private static final int STATE_WAITING_FOR_SYSTEM_AUDIO_MODE_STATUS = 1;
    private final int mAvrAddress;
    private int mRetriesOnTimeOut;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SystemAudioAutoInitiationAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i) {
        super(hdmiCecLocalDevice);
        this.mRetriesOnTimeOut = 1;
        this.mAvrAddress = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        this.mState = 1;
        addTimer(1, 2000);
        sendGiveSystemAudioModeStatus();
        return true;
    }

    private void sendGiveSystemAudioModeStatus() {
        sendCommand(HdmiCecMessageBuilder.buildGiveSystemAudioModeStatus(getSourceAddress(), this.mAvrAddress), new HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.SystemAudioAutoInitiationAction.1
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int i) {
                if (i != 0) {
                    SystemAudioAutoInitiationAction.this.handleSystemAudioModeStatusTimeout();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (this.mState != 1 || this.mAvrAddress != hdmiCecMessage.getSource() || hdmiCecMessage.getOpcode() != 126) {
            return false;
        }
        handleSystemAudioModeStatusMessage(HdmiUtils.parseCommandParamSystemAudioStatus(hdmiCecMessage));
        return true;
    }

    private void handleSystemAudioModeStatusMessage(boolean z) {
        if (!canChangeSystemAudio()) {
            HdmiLogger.debug("Cannot change system audio mode in auto initiation action.", new Object[0]);
            finish();
            return;
        }
        boolean isSystemAudioControlFeatureEnabled = tv().isSystemAudioControlFeatureEnabled();
        if (z != isSystemAudioControlFeatureEnabled) {
            addAndStartAction(new SystemAudioActionFromTv(tv(), this.mAvrAddress, isSystemAudioControlFeatureEnabled, null));
        } else {
            tv().setSystemAudioMode(isSystemAudioControlFeatureEnabled);
        }
        finish();
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
        int i2 = this.mState;
        if (i2 == i && i2 == 1) {
            int i3 = this.mRetriesOnTimeOut;
            if (i3 > 0) {
                this.mRetriesOnTimeOut = i3 - 1;
                addTimer(i2, 2000);
                sendGiveSystemAudioModeStatus();
                return;
            }
            handleSystemAudioModeStatusTimeout();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSystemAudioModeStatusTimeout() {
        if (!canChangeSystemAudio()) {
            HdmiLogger.debug("Cannot change system audio mode in auto initiation action.", new Object[0]);
            finish();
        } else {
            addAndStartAction(new SystemAudioActionFromTv(tv(), this.mAvrAddress, tv().isSystemAudioControlFeatureEnabled(), null));
            finish();
        }
    }

    private boolean canChangeSystemAudio() {
        return (tv().hasAction(SystemAudioActionFromTv.class) || tv().hasAction(SystemAudioActionFromAvr.class)) ? false : true;
    }
}
