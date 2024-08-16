package com.android.server.hdmi;

import android.hardware.hdmi.IHdmiControlCallback;
import android.util.Slog;
import com.android.server.hdmi.HdmiControlService;
import java.util.Arrays;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class TimerRecordingAction extends HdmiCecFeatureAction {
    private static final int STATE_WAITING_FOR_TIMER_STATUS = 1;
    private static final String TAG = "TimerRecordingAction";
    private static final int TIMER_STATUS_TIMEOUT_MS = 120000;
    private final byte[] mRecordSource;
    private final int mRecorderAddress;
    private final int mSourceType;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TimerRecordingAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i, int i2, byte[] bArr) {
        super(hdmiCecLocalDevice);
        this.mRecorderAddress = i;
        this.mSourceType = i2;
        this.mRecordSource = bArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        sendTimerMessage();
        return true;
    }

    private void sendTimerMessage() {
        HdmiCecMessage buildSetDigitalTimer;
        int i = this.mSourceType;
        if (i == 1) {
            buildSetDigitalTimer = HdmiCecMessageBuilder.buildSetDigitalTimer(getSourceAddress(), this.mRecorderAddress, this.mRecordSource);
        } else if (i == 2) {
            buildSetDigitalTimer = HdmiCecMessageBuilder.buildSetAnalogueTimer(getSourceAddress(), this.mRecorderAddress, this.mRecordSource);
        } else if (i == 3) {
            buildSetDigitalTimer = HdmiCecMessageBuilder.buildSetExternalTimer(getSourceAddress(), this.mRecorderAddress, this.mRecordSource);
        } else {
            tv().announceTimerRecordingResult(this.mRecorderAddress, 2);
            finish();
            return;
        }
        sendCommand(buildSetDigitalTimer, new HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.TimerRecordingAction.1
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int i2) {
                if (i2 != 0) {
                    TimerRecordingAction.this.tv().announceTimerRecordingResult(TimerRecordingAction.this.mRecorderAddress, 1);
                    TimerRecordingAction.this.finish();
                } else {
                    TimerRecordingAction timerRecordingAction = TimerRecordingAction.this;
                    timerRecordingAction.mState = 1;
                    timerRecordingAction.addTimer(1, TimerRecordingAction.TIMER_STATUS_TIMEOUT_MS);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (this.mState != 1 || hdmiCecMessage.getSource() != this.mRecorderAddress) {
            return false;
        }
        int opcode = hdmiCecMessage.getOpcode();
        if (opcode == 0) {
            return handleFeatureAbort(hdmiCecMessage);
        }
        if (opcode != 53) {
            return false;
        }
        return handleTimerStatus(hdmiCecMessage);
    }

    private boolean handleTimerStatus(HdmiCecMessage hdmiCecMessage) {
        byte[] params = hdmiCecMessage.getParams();
        if (params.length == 1 || params.length == 3) {
            tv().announceTimerRecordingResult(this.mRecorderAddress, bytesToInt(params));
            Slog.i(TAG, "Received [Timer Status Data]:" + Arrays.toString(params));
        } else {
            Slog.w(TAG, "Invalid [Timer Status Data]:" + Arrays.toString(params));
        }
        finish();
        return true;
    }

    private boolean handleFeatureAbort(HdmiCecMessage hdmiCecMessage) {
        byte[] params = hdmiCecMessage.getParams();
        int i = params[0] & 255;
        if (i != 52 && i != 151 && i != 162) {
            return false;
        }
        Slog.i(TAG, "[Feature Abort] for " + i + " reason:" + (params[1] & 255));
        tv().announceTimerRecordingResult(this.mRecorderAddress, 1);
        finish();
        return true;
    }

    private static int bytesToInt(byte[] bArr) {
        if (bArr.length > 4) {
            throw new IllegalArgumentException("Invalid data size:" + Arrays.toString(bArr));
        }
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            i |= (bArr[i2] & 255) << ((3 - i2) * 8);
        }
        return i;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
        if (this.mState != i) {
            Slog.w(TAG, "Timeout in invalid state:[Expected:" + this.mState + ", Actual:" + i + "]");
            return;
        }
        tv().announceTimerRecordingResult(this.mRecorderAddress, 1);
        finish();
    }
}
