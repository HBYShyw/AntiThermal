package com.android.server.hdmi;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class VolumeControlAction extends HdmiCecFeatureAction {
    private static final int MAX_VOLUME = 100;
    private static final int STATE_WAIT_FOR_NEXT_VOLUME_PRESS = 1;
    private static final String TAG = "VolumeControlAction";
    private static final int UNKNOWN_AVR_VOLUME = -1;
    private final int mAvrAddress;
    private boolean mIsVolumeUp;
    private boolean mLastAvrMute;
    private int mLastAvrVolume;
    private long mLastKeyUpdateTime;
    private boolean mSentKeyPressed;

    public static int scaleToCecVolume(int i, int i2) {
        return (i * 100) / i2;
    }

    public static int scaleToCustomVolume(int i, int i2) {
        return (i * i2) / 100;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VolumeControlAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i, boolean z) {
        super(hdmiCecLocalDevice);
        this.mAvrAddress = i;
        this.mIsVolumeUp = z;
        this.mLastAvrVolume = -1;
        this.mLastAvrMute = false;
        this.mSentKeyPressed = false;
        updateLastKeyUpdateTime();
    }

    private void updateLastKeyUpdateTime() {
        this.mLastKeyUpdateTime = System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        this.mState = 1;
        sendVolumeKeyPressed();
        resetTimer();
        return true;
    }

    private void sendVolumeKeyPressed() {
        sendCommand(HdmiCecMessageBuilder.buildUserControlPressed(getSourceAddress(), this.mAvrAddress, this.mIsVolumeUp ? 65 : 66));
        this.mSentKeyPressed = true;
    }

    private void resetTimer() {
        this.mActionTimer.clearTimerMessage();
        addTimer(1, 300);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleVolumeChange(boolean z) {
        boolean z2 = this.mIsVolumeUp;
        if (z2 != z) {
            HdmiLogger.debug("Volume Key Status Changed[old:%b new:%b]", Boolean.valueOf(z2), Boolean.valueOf(z));
            sendVolumeKeyReleased();
            this.mIsVolumeUp = z;
            sendVolumeKeyPressed();
            resetTimer();
        }
        updateLastKeyUpdateTime();
    }

    private void sendVolumeKeyReleased() {
        sendCommand(HdmiCecMessageBuilder.buildUserControlReleased(getSourceAddress(), this.mAvrAddress));
        this.mSentKeyPressed = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (this.mState != 1 || hdmiCecMessage.getSource() != this.mAvrAddress) {
            return false;
        }
        int opcode = hdmiCecMessage.getOpcode();
        if (opcode == 0) {
            return handleFeatureAbort(hdmiCecMessage);
        }
        if (opcode != 122) {
            return false;
        }
        return handleReportAudioStatus(hdmiCecMessage);
    }

    private boolean handleReportAudioStatus(HdmiCecMessage hdmiCecMessage) {
        boolean isAudioStatusMute = HdmiUtils.isAudioStatusMute(hdmiCecMessage);
        int audioStatusVolume = HdmiUtils.getAudioStatusVolume(hdmiCecMessage);
        this.mLastAvrVolume = audioStatusVolume;
        this.mLastAvrMute = isAudioStatusMute;
        if (!shouldUpdateAudioVolume(isAudioStatusMute)) {
            return true;
        }
        HdmiLogger.debug("Force volume change[mute:%b, volume=%d]", Boolean.valueOf(isAudioStatusMute), Integer.valueOf(audioStatusVolume));
        tv().setAudioStatus(isAudioStatusMute, audioStatusVolume);
        this.mLastAvrVolume = -1;
        this.mLastAvrMute = false;
        return true;
    }

    private boolean shouldUpdateAudioVolume(boolean z) {
        if (z) {
            return true;
        }
        AudioManagerWrapper audioManager = tv().getService().getAudioManager();
        int streamVolume = audioManager.getStreamVolume(3);
        return this.mIsVolumeUp ? streamVolume == audioManager.getStreamMaxVolume(3) : streamVolume == 0;
    }

    private boolean handleFeatureAbort(HdmiCecMessage hdmiCecMessage) {
        if ((hdmiCecMessage.getParams()[0] & 255) != 68) {
            return false;
        }
        finish();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public void clear() {
        super.clear();
        if (this.mSentKeyPressed) {
            sendVolumeKeyReleased();
        }
        if (this.mLastAvrVolume != -1) {
            tv().setAudioStatus(this.mLastAvrMute, this.mLastAvrVolume);
            this.mLastAvrVolume = -1;
            this.mLastAvrMute = false;
        }
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
        if (i != 1) {
            return;
        }
        if (System.currentTimeMillis() - this.mLastKeyUpdateTime >= 300) {
            finish();
        } else {
            sendVolumeKeyPressed();
            resetTimer();
        }
    }
}
