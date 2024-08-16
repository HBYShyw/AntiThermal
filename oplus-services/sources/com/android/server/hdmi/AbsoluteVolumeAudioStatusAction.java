package com.android.server.hdmi;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class AbsoluteVolumeAudioStatusAction extends HdmiCecFeatureAction {
    private static final int STATE_MONITOR_AUDIO_STATUS = 2;
    private static final int STATE_WAIT_FOR_INITIAL_AUDIO_STATUS = 1;
    private static final String TAG = "AbsoluteVolumeAudioStatusAction";
    private int mInitialAudioStatusRetriesLeft;
    private AudioStatus mLastAudioStatus;
    private final int mTargetAddress;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbsoluteVolumeAudioStatusAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i) {
        super(hdmiCecLocalDevice);
        this.mInitialAudioStatusRetriesLeft = 2;
        this.mTargetAddress = i;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        this.mState = 1;
        sendGiveAudioStatus();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateVolume(int i) {
        this.mLastAudioStatus = new AudioStatus(i, this.mLastAudioStatus.getMute());
    }

    private void sendGiveAudioStatus() {
        addTimer(this.mState, 2000);
        sendCommand(HdmiCecMessageBuilder.buildGiveAudioStatus(getSourceAddress(), this.mTargetAddress));
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (hdmiCecMessage.getOpcode() != 122) {
            return false;
        }
        return handleReportAudioStatus(hdmiCecMessage);
    }

    private boolean handleReportAudioStatus(HdmiCecMessage hdmiCecMessage) {
        if (this.mTargetAddress != hdmiCecMessage.getSource() || hdmiCecMessage.getParams().length == 0) {
            return false;
        }
        boolean isAudioStatusMute = HdmiUtils.isAudioStatusMute(hdmiCecMessage);
        int audioStatusVolume = HdmiUtils.getAudioStatusVolume(hdmiCecMessage);
        if (audioStatusVolume == -1) {
            return true;
        }
        AudioStatus audioStatus = new AudioStatus(audioStatusVolume, isAudioStatusMute);
        int i = this.mState;
        if (i == 1) {
            localDevice().getService().enableAbsoluteVolumeBehavior(audioStatus);
            this.mState = 2;
        } else if (i == 2) {
            if (audioStatus.getVolume() != this.mLastAudioStatus.getVolume()) {
                localDevice().getService().notifyAvbVolumeChange(audioStatus.getVolume());
            }
            if (audioStatus.getMute() != this.mLastAudioStatus.getMute()) {
                localDevice().getService().notifyAvbMuteChange(audioStatus.getMute());
            }
        }
        this.mLastAudioStatus = audioStatus;
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
        int i2;
        if (this.mState == i && (i2 = this.mInitialAudioStatusRetriesLeft) > 0) {
            this.mInitialAudioStatusRetriesLeft = i2 - 1;
            sendGiveAudioStatus();
        }
    }
}
