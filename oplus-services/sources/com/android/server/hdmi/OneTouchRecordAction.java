package com.android.server.hdmi;

import android.hardware.hdmi.IHdmiControlCallback;
import android.util.Slog;
import com.android.server.hdmi.HdmiControlService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class OneTouchRecordAction extends HdmiCecFeatureAction {
    private static final int RECORD_STATUS_TIMEOUT_MS = 120000;
    private static final int STATE_RECORDING_IN_PROGRESS = 2;
    private static final int STATE_WAITING_FOR_RECORD_STATUS = 1;
    private static final String TAG = "OneTouchRecordAction";
    private final byte[] mRecordSource;
    private final int mRecorderAddress;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OneTouchRecordAction(HdmiCecLocalDevice hdmiCecLocalDevice, int i, byte[] bArr) {
        super(hdmiCecLocalDevice);
        this.mRecorderAddress = i;
        this.mRecordSource = bArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        sendRecordOn();
        return true;
    }

    private void sendRecordOn() {
        sendCommand(HdmiCecMessageBuilder.buildRecordOn(getSourceAddress(), this.mRecorderAddress, this.mRecordSource), new HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.OneTouchRecordAction.1
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int i) {
                if (i != 0) {
                    OneTouchRecordAction.this.tv().announceOneTouchRecordResult(OneTouchRecordAction.this.mRecorderAddress, 49);
                    OneTouchRecordAction.this.finish();
                }
            }
        });
        this.mState = 1;
        addTimer(1, RECORD_STATUS_TIMEOUT_MS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(HdmiCecMessage hdmiCecMessage) {
        if (this.mState == 1 && this.mRecorderAddress == hdmiCecMessage.getSource() && hdmiCecMessage.getOpcode() == 10) {
            return handleRecordStatus(hdmiCecMessage);
        }
        return false;
    }

    private boolean handleRecordStatus(HdmiCecMessage hdmiCecMessage) {
        if (hdmiCecMessage.getSource() != this.mRecorderAddress) {
            return false;
        }
        byte b = hdmiCecMessage.getParams()[0];
        tv().announceOneTouchRecordResult(this.mRecorderAddress, b);
        Slog.i(TAG, "Got record status:" + ((int) b) + " from " + hdmiCecMessage.getSource());
        if (b == 1 || b == 2 || b == 3 || b == 4) {
            this.mState = 2;
            this.mActionTimer.clearTimerMessage();
        } else {
            finish();
        }
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int i) {
        if (this.mState != i) {
            Slog.w(TAG, "Timeout in invalid state:[Expected:" + this.mState + ", Actual:" + i + "]");
            return;
        }
        tv().announceOneTouchRecordResult(this.mRecorderAddress, 49);
        finish();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRecorderAddress() {
        return this.mRecorderAddress;
    }
}
